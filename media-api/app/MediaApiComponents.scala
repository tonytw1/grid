import com.gu.mediaservice.lib.aws.ThrallMessageSender
import com.gu.mediaservice.lib.elasticsearch.ElasticSearchConfig
import com.gu.mediaservice.lib.imaging.ImageOperations
import com.gu.mediaservice.lib.management.{ElasticSearchHealthCheck, ManagementWithPermissions}
import com.gu.mediaservice.lib.play.GridComponents
import com.gu.mediaservice.model.Agency
import controllers._
import lib._
import lib.elasticsearch.ElasticSearch
import play.api.ApplicationLoader.Context
import router.Routes

import scala.concurrent.Future

class MediaApiComponents(context: Context) extends GridComponents(context, new MediaApiConfig(_)) {
  final override val buildInfo = utils.buildinfo.BuildInfo

  val imageOperations = new ImageOperations(context.environment.rootPath.getAbsolutePath)

  val messageSender = new ThrallMessageSender(config.thrallKinesisStreamConfig)
  val mediaApiMetrics = new MediaApiMetrics(config)

  val es6Config: ElasticSearchConfig = ElasticSearchConfig(
    alias = config.imagesAlias,
    url = config.elasticsearch6Url,
    shards = config.elasticsearch6Shards,
    replicas = config.elasticsearch6Replicas
  )

  val s3Client = new S3Client(config)

  val usageQuota: Option[UsageQuota] = None // Some(new UsageQuota(config, actorSystem.scheduler))
  usageQuota.foreach { usageQuota =>
    usageQuota.quotaStore.update()
    usageQuota.scheduleUpdates()
    applicationLifecycle.addStopHook(() => Future {
      usageQuota.stopUpdates()
    })
  }

  private val agencies: () => List[Agency] = () => usageQuota.map(_.usageStore.overQuotaAgencies).getOrElse{
    def noAgencies: () => List[Agency] = () => {List.empty}
    noAgencies.apply()
  }

  val elasticSearch = new ElasticSearch(config, mediaApiMetrics, es6Config, agencies)
  elasticSearch.ensureAliasAssigned()

  val imageResponse = new ImageResponse(config, s3Client, usageQuota)

  val mediaApi = new MediaApi(auth, messageSender, elasticSearch, imageResponse, config, controllerComponents, s3Client, mediaApiMetrics, wsClient)
  val suggestionController = new SuggestionController(auth, elasticSearch, controllerComponents)
  val aggController = new AggregationController(auth, elasticSearch, controllerComponents)
  val usageController = new UsageController(auth, config, elasticSearch, usageQuota, controllerComponents)
  val elasticSearchHealthCheck = new ElasticSearchHealthCheck(controllerComponents, elasticSearch)
  val healthcheckController = new ManagementWithPermissions(controllerComponents, mediaApi, buildInfo)

  override val router = new Routes(httpErrorHandler, mediaApi, suggestionController, aggController, usageController, elasticSearchHealthCheck, healthcheckController)
}
