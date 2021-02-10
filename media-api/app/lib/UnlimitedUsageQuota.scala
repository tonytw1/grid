package lib
import com.gu.mediaservice.model.{Agency, UsageRights}
import org.joda.time.DateTime

import scala.concurrent.Future

class UnlimitedUsageQuota extends UsageQuota {

  override def isOverQuota(rights: UsageRights, waitMillis: Int): Boolean = false

  override def getUsageStatusForUsageRights(usageRights: UsageRights): Future[UsageStatus] = Future.failed(new Exception("Image is not supplied by Agency")) // TODO what does this mean?

  override def getUsageStatus(): Future[StoreAccess] = Future.successful{
    StoreAccess(
      store = Map.empty,
      lastUpdated = DateTime.now()
    )
  }

  override def overQuotaAgencies: List[Agency] = List.empty
}
