package lib

import com.gu.mediaservice.model.{Agency, UsageRights}

import scala.concurrent.Future

case class ImageNotFound() extends Exception("Image not found")
case class NoUsageQuota() extends Exception("No usage found for this image")

trait UsageQuota {

  def isOverQuota(rights: UsageRights, waitMillis: Int = 100): Boolean

  def getUsageStatusForUsageRights(usageRights: UsageRights): Future[UsageStatus]

  def getUsageStatus(): Future[StoreAccess]

  def overQuotaAgencies: List[Agency]

}
