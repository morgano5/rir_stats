package au.id.villar.rir

import java.io.BufferedReader

import scala.collection.mutable.ListBuffer

object Reader {

  private val rirs = List("afrinic", "apnic", "arin", "iana", "lacnic", "ripencc")
  private val types = List("asn", "ipv4", "ipv6")
  private val splitPattern = "\\|".r

  def read(reader: java.io.Reader, listener: RecordListener) = {

    val lineReader = new BufferedReader(reader)
    var line: String = lineReader.readLine()
    val dateParser = new java.text.SimpleDateFormat("yyyyMMdd")
//    val buffer = new ListBuffer[String]

//    while(line != null) { buffer += line; line = lineReader.readLine() }

    val buffer = new LineReaderIterator(reader)

    buffer.filter(_.length > 0)
      .filter(!_.startsWith("#"))
      .map(splitPattern split _)
      .filter( _.length > 0)
      .foreach(createFromFields)

    def createFromFields(fields: Array[String]) = {

      try {
        if (fields(0) == "2") {

          listener.onVersion(new VersionLine(rirs(rirs.indexOf(fields(1))), fields(2), fields(3).toInt,
            dateParser.parse(fields(4)), dateParser.parse(fields(5)), fields(6).toInt))

        } else if (fields.length == 6 && fields(5) == "summary") {

          listener.onSummary(new Summary(rirs(rirs.indexOf(fields(0))),
            types(types.indexOf(fields(2))), fields(4).toInt))

        } else {

          listener.onRecord(new Record(fields(0), fields(1), fields(2), fields(3), fields(4).toInt,
            dateParser.parse(fields(5)), fields(6), fields.toList.drop(7)))

        }

      } catch {

        case e: Exception => listener.onNonValidLine(new NonValidLine(fields.toList))

      }

    }

  }

  private class LineReaderIterator(reader: java.io.Reader) extends Iterator[String] {

    private val flow = new BufferedReader(reader)
    private var nextLine: String = null
    private var endReached = false

    override def hasNext: Boolean = {
      if(nextLine != null) return true
      if(!endReached) nextLine = flow.readLine()
      if(nextLine == null) endReached = true
      !endReached
    }

    override def next() = {
      val v = nextLine
      nextLine = null
      v
    }

  }

}

