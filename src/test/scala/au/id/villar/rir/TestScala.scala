package au.id.villar.rir

import java.io.FileReader

/**
 * @author ${user.name}
 */
object TestScala {

  def main(args : Array[String]) {

    val file = new FileReader("/home/rafael/Desktop/consolidated.txt")

    object Listener extends RecordListener {

      override def onVersion(version: VersionLine) = println(
        s"${version.registry}, ${version.serial}, ${version.records}, ${version.startDate}, ${version.endDate}," +
          s" ${version.utcOffset}")

      override def onNonValidLine(error: NonValidLine) = println(s"${error.fields}")

      override def onSummary(summary: Summary) = println(
        s"${summary.registry}, ${summary.resourceType}, ${summary.count}")

      override def onRecord(record: Record) = println(
        s"${record.registry}, ${record.cc}, ${record.resourceType}, ${record.start}, ${record.value}, ${record.date}," +
          s" ${record.status}, ${record.extensions}")

    }

    Reader.read(file, Listener)

  }

}
