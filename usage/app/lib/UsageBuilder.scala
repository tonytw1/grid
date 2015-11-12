package lib

import org.joda.time.DateTime
import com.gu.mediaservice.model.{Usage, UsageSource}

import model.MediaUsage

object UsageBuilder {
  import com.gu.mediaservice.lib.IntUtils._

  def build(usage: MediaUsage): Usage = {
    usage.usageType match {
      case "web" => buildWeb(usage)
      case "print" => buildPrint(usage)
    }
  }

  private def buildWeb(usage: MediaUsage): Usage = {
    Usage(
      usage.data.getOrElse("webTitle", "No title specified."),
      buildUsageSource(usage),
      usage.usageType,
      usage.mediaType,
      usage.status.toString,
      usage.dateAdded,
      usage.dateRemoved,
      usage.lastModified
    )
  }

  private def buildPrint(usage: MediaUsage): Usage = {
    val usageTitle = List(
      usage.data.get("issueDate").map(date => new DateTime(date).toString("YYYY-MM-dd")),
      usage.data.get("pageNumber").map(page => s"Page $page"),
      usage.data.get("edition").map(edition => s"${edition.toInt.toOrdinal} edition")
    ).flatten.mkString(", ")

    Usage(
      usageTitle,
      buildUsageSource(usage),
      usage.usageType,
      usage.mediaType,
      usage.status.toString,
      usage.dateAdded,
      usage.dateRemoved,
      usage.lastModified
    )
  }

  private def buildUsageSource(usage: MediaUsage): List[UsageSource] = {
    usage.usageType match {
      case "web" => buildWebUsageSource(usage)
      case "print" => buildPrintUsageSource(usage)
    }
  }

  private def buildPrintUsageSource(usage: MediaUsage) = List(
    UsageSource("indesign", usage.data.get("containerId"), usage.data.get("storyName")))

  private def buildWebUsageSource(usage: MediaUsage) = List(
    UsageSource("frontend", usage.data.get("webUrl"), usage.data.get("webTitle"))) ++
    usage.data.get("composerUrl").map(composerUrl => UsageSource("composer", Some(composerUrl)))

}
