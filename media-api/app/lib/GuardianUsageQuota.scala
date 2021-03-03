package lib

import akka.actor.Scheduler
import com.gu.mediaservice.lib.FeatureToggle
import com.gu.mediaservice.model.{Agency, UsageRights}

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.Try

class GuardianUsageQuota(config: MediaApiConfig, scheduler: Scheduler, quotaStoreKey: String, quotaStoreBucket: String, usageMailBucket: String) extends UsageQuota {

  val quotaStore = new QuotaStore(
    quotaStoreKey,
    quotaStoreBucket,
    config
  )

  val usageStore = new UsageStore(
    usageMailBucket,
    config,
    quotaStore
  )

  override def isOverQuota(rights: UsageRights, waitMillis: Int = 100): Boolean = Try {
    Await.result(
      usageStore.getUsageStatusForUsageRights(rights),
      waitMillis.millis)
  }.toOption.exists(_.exceeded) && FeatureToggle.get("usage-quota-ui")

  override def getUsageStatusForUsageRights(usageRights: UsageRights): Future[UsageStatus] = usageStore.getUsageStatusForUsageRights(usageRights)

  override def getUsageStatus(): Future[StoreAccess] = usageStore.getUsageStatus()

  override def overQuotaAgencies: List[Agency] = usageStore.overQuotaAgencies

  def scheduleUpdates(): Unit = {
    quotaStore.scheduleUpdates(scheduler)
    usageStore.scheduleUpdates(scheduler)
  }

  def stopUpdates(): Unit = {
    quotaStore.stopUpdates()
    usageStore.stopUpdates()
  }

}
