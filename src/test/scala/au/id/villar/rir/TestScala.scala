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

import java.io.{FileWriter, InputStreamReader}
import java.net.URL

import scala.collection.mutable.ListBuffer

object TestScala {

  def main(args : Array[String]) {

    val fileData = new FileWriter("/home/rafael/Desktop/ipRanges.sql")

    List[URL](
      new URL("http://ftp.apnic.net/stats/apnic/delegated-apnic-latest"),
      new URL("http://ftp.lacnic.net/pub/stats/lacnic/delegated-lacnic-latest"),
      new URL("http://ftp.afrinic.net/pub/stats/afrinic/delegated-afrinic-latest"),
      new URL("http://ftp.arin.net/pub/stats/arin/delegated-arin-extended-latest"),
      new URL("http://ftp.ripe.net/pub/stats/ripencc/delegated-ripencc-latest")
    ).foreach(urlToRecords)


    fileData.close()



    def urlToRecords(url: URL): Unit = {

      println(s"Getting data from $url")

      val reader = new InputStreamReader(url.openStream())
      val buffer = new ListBuffer[FileLine]

      object Listener extends RecordListener {

        override def onVersion(version: VersionLine) = println(
          s"Retrieving ${version.records} items, data from ${version.startDate} to ${version.endDate},")

        override def onNonValidLine(error: NonValidLine) = println(
          s"ERROR: not a valid line: ${error.fields}")

        override def onSummary(summary: Summary) = println(
          s"${summary.registry}: Total of resouces '${summary.resourceType}': ${summary.count}")

        override def onRecord(record: Record) = fileData write
          s"${record.registry}, ${record.cc}, ${record.resourceType}, ${record.start}, ${record.value}, ${record.date}," +
            s" ${record.status}, ${record.extensions}"

      }

      Reader.read(reader, Listener)

      println("Data transferred")

      reader.close()

    }

  }

}
