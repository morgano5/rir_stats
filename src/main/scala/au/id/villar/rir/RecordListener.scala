package au.id.villar.rir

trait RecordListener {

  def onVersion(version: VersionLine)

  def onRecord(record: Record)

  def onSummary(summary: Summary)

  def onNonValidLine(error: NonValidLine)

}
