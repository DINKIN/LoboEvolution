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
 * Created on Jun 5, 2005
 */
package com.izforge.izpack.panels;

import java.io.File;

import com.izforge.izpack.installer.InstallData;
import com.izforge.izpack.installer.InstallerFrame;
import com.izforge.izpack.installer.IzPanel;
import com.izforge.izpack.util.NativeLibraryClient;

/**
 * The Class RegistryPanel.
 *
 * @author J. H. S.
 */
public class RegistryPanel extends IzPanel implements NativeLibraryClient {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new registry panel.
	 *
	 * @param arg0
	 *            the arg0
	 * @param arg1
	 *            the arg1
	 */
	public RegistryPanel(InstallerFrame arg0, InstallData arg1) {
		super(arg0, arg1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.izforge.izpack.installer.IzPanel#panelActivate()
	 */
	@Override
	public void panelActivate() {
		super.panelActivate();
		this.performFileActions();
		this.performRegistryActions();
	}

	/**
	 * Perform file actions.
	 */
	private void performFileActions() {
		try {
			File userHome = new File(System.getProperty("user.home"));
			File loboHome = new File(userHome, ".lobo");
			File cacheHome = new File(loboHome, "cache");
			this.deleteDecorationFiles(cacheHome);
		} catch (Exception err) {
			err.printStackTrace();
		}
	}

	/**
	 * Delete decoration files.
	 *
	 * @param rootDir
	 *            the root dir
	 */
	private void deleteDecorationFiles(File rootDir) {
		// Deletes decoration files in cache directory
		File[] files = rootDir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					this.deleteDecorationFiles(file);
				} else {
					String name = file.getName().toLowerCase();
					if (name.endsWith(".decor")) {
						file.delete();
					}
				}
			}
		}
	}

	/**
	 * Perform registry actions.
	 */
	private void performRegistryActions() {
		parent.skipPanel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.izforge.izpack.util.NativeLibraryClient#freeLibrary(String)
	 */
	@Override
	public void freeLibrary(String arg0) {
	}
}
