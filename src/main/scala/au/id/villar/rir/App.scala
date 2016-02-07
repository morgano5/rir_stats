package au.id.villar.rir

import java.io.{IOException, FileReader}

/**
 * @author ${user.name}
 */
object App {

  def main(args : Array[String]) {

    val file = new FileReader("/home/rafael/Desktop/consolidated.txt")

    object Listener extends RecordListener {

      override def onVersion(version: VersionLine) = println(version)

      override def onNonValidLine(error: NonValidLine) = println(error)

      override def onSummary(summary: Summary) = println(summary)

      override def onRecord(record: Record) = println(record)

    }

    Reader.read(file, Listener)


  }



}
