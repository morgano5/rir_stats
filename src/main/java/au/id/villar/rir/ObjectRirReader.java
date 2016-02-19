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

import java.io.Reader;

public class ObjectRirReader implements RirReader {

	public void read(Reader reader, RecordListener listener) {
		Reader$.MODULE$.read(reader, listener);
	}

}
