package au.id.villar.rir;

import java.io.IOException;

public interface RirReader {

	@SuppressWarnings("unused")
	void read(java.io.Reader reader, RecordListener listener) throws IOException;

}

