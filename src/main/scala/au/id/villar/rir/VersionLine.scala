package au.id.villar.rir

import java.util.Date

class VersionLine(val registry: String, val serial:String, val records: Int, val startDate: Date, val endDate: Date,
                  val utcOffset: Int) extends FileLine
