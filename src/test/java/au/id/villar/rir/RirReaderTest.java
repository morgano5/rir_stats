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
				System.out.println("Version line: " + version.registry());
			}

			@Override
			public void onNonValidLine(NonValidLine error) {
				counter.count++;
				counter.nonValidLineCount++;
				System.out.println("Error line: " + error.fields());
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

		assertEquals(390335, counter.count);
		assertEquals(5, counter.versionCount);
		assertEquals(0, counter.nonValidLineCount);
		assertEquals(15, counter.summaryCount);
		assertEquals(390315, counter.recordCount);
	}

}
