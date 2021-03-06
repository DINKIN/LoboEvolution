/*
    GNU GENERAL LICENSE
    Copyright (C) 2006 The Lobo Project. Copyright (C) 2014 - 2017 Lobo Evolution

    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public
    License as published by the Free Software Foundation; either
    verion 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    General License for more details.

    You should have received a copy of the GNU General Public
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
    

    Contact info: lobochief@users.sourceforge.net; ivan.difrancesco@yahoo.it
 */
/*
 * Created on Mar 14, 2005
 */
package org.lobobrowser.protocol.res;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * The Class ResURLConnection.
 *
 * @author J. H. S.
 */
public class ResURLConnection extends URLConnection {

	/**
	 * Instantiates a new res url connection.
	 *
	 * @param url
	 *            the url
	 */
	public ResURLConnection(URL url) {
		super(url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.net.URLConnection#connect()
	 */
	@Override
	public void connect() throws IOException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.net.URLConnection#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		String host = this.url.getHost();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		if (classLoader == null) {
			classLoader = this.getClass().getClassLoader();
		}
		String file = this.url.getPath();
		InputStream in = classLoader.getResourceAsStream(file);
		if (in == null && file.startsWith("/")) {
			file = file.substring(1);
			in = classLoader.getResourceAsStream(file);
			if (in == null) {
				throw new IOException("Resource " + file + " not found in " + host + ".");
			}
		}
		return in;
	}
}
