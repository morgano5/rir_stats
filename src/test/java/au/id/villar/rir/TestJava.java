package au.id.villar.rir;

import java.io.*;

public class TestJava {

	public static void main(String[] args) {

		Reader.read(new TestReader(), listener);

	}

	private static class TestReader extends java.io.Reader {

		@Override
		public int read(char[] cbuf, int off, int len) throws IOException {
			throw new IOException("Booom!");
		}

		@Override
		public void close() throws IOException {

		}
	}

	private static RecordListener listener = new RecordListener() {

		@Override
		public void onVersion(VersionLine version) {
			// TODO implement

		}

		@Override
		public void onNonValidLine(NonValidLine error) {
			// TODO implement

		}

		@Override
		public void onSummary(Summary summary) {
			// TODO implement

		}

		@Override
		public void onRecord(Record record) {
			// TODO implement

		}
	};

}
