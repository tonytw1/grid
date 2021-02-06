package com.gu.mediaservice.lib.aws

import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.gu.mediaservice.lib.logging.GridLogging

trait AwsClientBuilderUtils extends GridLogging {

  def awsEndpointConfiguration: Option[EndpointConfiguration]
  def awsCredentials: AWSCredentialsProvider
  def awsRegion: String

  final def withAWSCredentials[T, S <: AwsClientBuilder[S, T]](builder: AwsClientBuilder[S, T], localstackAware: Boolean = true): S = {
    awsEndpointConfiguration match {
      case Some(endpointConfiguration) if localstackAware => {
        logger.info(s"creating aws client with local endpoint $endpointConfiguration")
        builder.withCredentials(awsCredentials).withEndpointConfiguration(endpointConfiguration)
      }
      case _ => builder.withCredentials(awsCredentials).withRegion(awsRegion)
    }
  }

}
