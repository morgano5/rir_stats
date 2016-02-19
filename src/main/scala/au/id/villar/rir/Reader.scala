/*
 * Rir Stats - Small library to parse RIR files
 * Copyright (C) 2016 Rafael Villar Villar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Rafael Villar rafael@villar.me
 */
package au.id.villar.rir

import java.io.BufferedReader

object Reader {

  private val supportedVersions = List("2", "2.3")

  private val rirs = List("afrinic", "apnic", "arin", "iana", "lacnic", "ripencc")
  private val types = List("asn", "ipv4", "ipv6")
  private val status = List("available", "allocated", "assigned", "reserved")

  private val splitPattern = "\\|".r
  private val versionPattern = "[0-9.]+".r

  def read(reader: java.io.Reader, listener: RecordListener) = {

    val dateParser = new java.text.SimpleDateFormat("yyyyMMdd")

    val buffer = new LineReaderIterator(reader)

    buffer
      .map(_.trim)
      .filter(_.length > 0)
      .filter(!_.startsWith("#"))
      .map(splitPattern split _)
      .foreach(createFromFields)

    def createFromFields(fields: Array[String]) = {

      try {
        if (versionPattern.pattern.matcher(fields(0)).matches()) {

          if(!supportedVersions.contains(fields(0))) throw new RuntimeException("Version not supported: " + fields(0))

          val date = if(!fields(4).isEmpty) dateParser.parse(fields(4)) else null

          listener.onVersion(new VersionLine(rirs(rirs.indexOf(fields(1))), fields(2), fields(3).toInt,
            date, dateParser.parse(fields(5)), fields(6).toInt))

        } else if (fields.length == 6 && fields(5) == "summary") {

          listener.onSummary(new Summary(rirs(rirs.indexOf(fields(0))),
            types(types.indexOf(fields(2))), fields(4).toInt))

        } else if(validateRecord(fields)) {

          val date = if(fields(5) != "00000000" && !fields(5).isEmpty) dateParser.parse(fields(5)) else null
          val cc = if(fields(1).length == 2) fields(1) else null

          listener.onRecord(new Record(fields(0), cc, fields(2), fields(3), fields(4).toInt,
            date, fields(6),
            fields.toList.drop(7)))

        } else {
          listener.onNonValidLine(new NonValidLine(fields.toList))

        }

      } catch {

        case e: Exception => listener.onNonValidLine(new NonValidLine(fields.toList))

      }

    }

    def validateRecord(fields: Array[String]): Boolean = {
      if(fields.length < 7 ) return false
      if(!rirs.contains(fields(0))) return false
      if(fields(1).length != 2 && !fields(1).isEmpty) return false
      if(!types.contains(fields(2))) return false
      if(!status.contains(fields(6))) return false
      true
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

