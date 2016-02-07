package au.id.villar.rir

import java.util.Date

class Record(val registry: String, val cc: String, val resourceType: String, val start: String, val value: Int,
             val date: Date, val status: String, val extensions: List[String]) extends FileLine
