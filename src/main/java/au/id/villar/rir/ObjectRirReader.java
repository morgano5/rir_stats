package au.id.villar.rir;

import java.io.Reader;

public class ObjectRirReader implements RirReader {

	public void read(Reader reader, RecordListener listener) {
		Reader$.MODULE$.read(reader, listener);
	}

}
