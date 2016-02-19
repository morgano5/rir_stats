package au.id.villar.rir;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

public class RirReaderTest {

	@Test
	public void basicTest() throws IOException {

		class Counter { int count, versionCount, nonValidLineCount, summaryCount, recordCount; }
		final Counter counter = new Counter();

		RecordListener listener = new RecordListener() {

			@Override
			public void onVersion(VersionLine version) {
				counter.count++;
				counter.versionCount++;
			}

			@Override
			public void onNonValidLine(NonValidLine error) {
				counter.count++;
				counter.nonValidLineCount++;
			}

			@Override
			public void onSummary(Summary summary) {
				counter.count++;
				counter.summaryCount++;
			}

			@Override
			public void onRecord(Record record) {
				counter.count++;
				counter.recordCount++;
			}
		};

		try(java.io.Reader input = new InputStreamReader(ClassLoader.getSystemResourceAsStream("consolidated.txt"))) {

			new ObjectRirReader().read(input, listener);

		}

		assertEquals(281081, counter.count);
		assertEquals(3, counter.versionCount);
		assertEquals(32269, counter.nonValidLineCount);
		assertEquals(12, counter.summaryCount);
		assertEquals(248797, counter.recordCount);
	}

}
