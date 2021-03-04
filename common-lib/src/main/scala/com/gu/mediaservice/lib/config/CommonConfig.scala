package com.gu.mediaservice.lib.config

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.auth._
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.gu.mediaservice.lib.aws.{AwsClientBuilderUtils, KinesisSenderConfig}
import com.typesafe.config.ConfigException
import com.typesafe.scalalogging.StrictLogging
import play.api.Configuration

import java.util.UUID
import scala.util.Try

abstract class CommonConfig(val configuration: Configuration) extends AwsClientBuilderUtils with StrictLogging {
  final val elasticsearchStack = "media-service"

  final val elasticsearchApp = "elasticsearch"
  final val elasticsearch6App = "elasticsearch6"

  final val stackName = "media-service"

  final val sessionId = UUID.randomUUID().toString

  // TODO:SAH - remove these and favour explicit config for anything that is derived from here
  val stage: String = string(GridConfigLoader.STAGE_KEY)
  val appName: String = string(GridConfigLoader.APP_KEY)
  val isProd: Boolean = stage == "PROD"
  val isDev: Boolean = stage == "DEV"

  override val awsRegion: String = stringDefault("aws.region", "eu-west-1")

  val awsLocalEndpoint: Option[String] = if(isDev) stringOpt("aws.local.endpoint") else None

  lazy val authKeyStoreBucket = string("auth.keystore.bucket")

  val useLocalAuth: Boolean = isDev && boolean("auth.useLocal")

  val permissionsBucket: String = stringDefault("permissions.bucket", "permissions-cache")

  val localLogShipping: Boolean = sys.env.getOrElse("LOCAL_LOG_SHIPPING", "false").toBoolean

  lazy val thrallKinesisStream = string("thrall.kinesis.stream.name")
  lazy val thrallKinesisLowPriorityStream = string("thrall.kinesis.lowPriorityStream.name")

  lazy val thrallKinesisStreamConfig = getKinesisConfigForStream(thrallKinesisStream)
  lazy val thrallKinesisLowPriorityStreamConfig = getKinesisConfigForStream(thrallKinesisLowPriorityStream)

  val requestMetricsEnabled: Boolean = boolean("metrics.request.enabled")

  // Note: had to make these lazy to avoid init order problems ;_;
  val domainRoot: String = string("domain.root")
  val rootAppName: String = stringDefault("app.name.root", "media")
  val serviceHosts = ServiceHosts(
    stringDefault("hosts.kahunaPrefix", s"$rootAppName."),
    stringDefault("hosts.apiPrefix", s"api.$rootAppName."),
    stringDefault("hosts.loaderPrefix", s"loader.$rootAppName."),
    stringDefault("hosts.cropperPrefix", s"cropper.$rootAppName."),
    stringDefault("hosts.adminToolsPrefix", s"admin-tools.$rootAppName."),
    stringDefault("hosts.metadataPrefix", s"$rootAppName-metadata."),
    stringDefault("hosts.imgopsPrefix", s"$rootAppName-imgops."),
    stringDefault("hosts.usagePrefix", s"$rootAppName-usage."),
    stringDefault("hosts.collectionsPrefix", s"$rootAppName-collections."),
    stringDefault("hosts.leasesPrefix", s"$rootAppName-leases."),
    stringDefault("hosts.authPrefix", s"$rootAppName-auth.")
  )

  val corsAllowedOrigins: Set[String] = getStringSet("security.cors.allowedOrigins")

  val services = new Services(domainRoot, serviceHosts, corsAllowedOrigins)

  def awsCredentials: AWSCredentialsProvider = {
    // Allow key based auth in container based installs which don't want to provision profile files in the container image
    val keyBasedCredentialProvider = stringOpt("aws.accessKey").flatMap { accessKey =>
      stringOpt("aws.accessSecret").map { accessSecret =>
        logger.info("Using access key and secret AWS credentials")
        new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, accessSecret))
      }
    }

    keyBasedCredentialProvider.getOrElse {
      // Guardian's profile and instance profile based chain
      logger.info("Using media-service profile AWS credentials")
      new AWSCredentialsProviderChain(
        new ProfileCredentialsProvider("media-service"),
        InstanceProfileCredentialsProvider.getInstance()
      )
    }
  }

  final def awsEndpointConfiguration: Option[EndpointConfiguration] = awsLocalEndpoint match {
    case Some(endpoint) if isDev => Some(new EndpointConfiguration(endpoint, awsRegion))
    case _ => None
  }

  private def getKinesisConfigForStream(streamName: String) = KinesisSenderConfig(awsRegion, awsCredentials, awsEndpointConfiguration, streamName)

  final def getStringSet(key: String): Set[String] = Try {
    configuration.get[Seq[String]](key)
  }.recover {
    case _:ConfigException.WrongType => configuration.get[String](key).split(",").toSeq.map(_.trim)
  }.map(_.toSet)
   .getOrElse(Set.empty)

  final def apply(key: String): String =
    string(key)

  final def string(key: String): String =
    configuration.getOptional[String](key) getOrElse missing(key, "string")

  final def stringDefault(key: String, default: String): String =
    configuration.getOptional[String](key) getOrElse default

  final def stringOpt(key: String): Option[String] = configuration.getOptional[String](key)

  final def int(key: String): Int =
    configuration.getOptional[Int](key) getOrElse missing(key, "integer")

  final def intDefault(key: String, default: Int): Int =
    configuration.getOptional[Int](key) getOrElse default

  final def boolean(key: String): Boolean =
    configuration.getOptional[Boolean](key).getOrElse(false)

  private def missing(key: String, type_ : String): Nothing =
    sys.error(s"Required $type_ configuration property missing: $key")
}
