package au.id.villar.rir

import java.util.Date

class Record(registry: String, cc: String, resourceType: String, start: String, value: Int, date: Date, status: String,
             extensions: List[String]) extends FileLine {

  override def toString = s"Record($registry, $cc, $resourceType, $start, $value, $date, $status, $extensions)"

}
