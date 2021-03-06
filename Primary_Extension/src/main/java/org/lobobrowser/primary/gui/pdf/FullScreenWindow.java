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
package org.lobobrowser.primary.gui.pdf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * The Class FullScreenWindow.
 */
public class FullScreenWindow {

	/**
	 * The screen that the user last chose for displaying a FullScreenWindow.
	 */

	private static GraphicsDevice defaultScreen;

	/** The current screen for the FullScreenWindow. */

	private GraphicsDevice screen;

	/** The JFrame filling the screen. */

	private JFrame jf;

	/**
	 *
	 * Whether this FullScreenWindow has been used. Each FullScreenWindow
	 *
	 * can only be displayed once.
	 *
	 */

	private boolean dead = false;
	
	/**
	 * Flag indicating whether the user has selected a screen or not.
	 */

	private Flag flag = new Flag();

	/** The picked device. */
	private GraphicsDevice pickedDevice;

	/**
	 *
	 * Create a full screen window containing a JComponent, and ask the
	 *
	 * user which screen they'd like to use if more than one is present.
	 *
	 * @param part
	 *            the JComponent to display
	 *
	 * @param forcechoice
	 *            true if you want force the display of the screen
	 *
	 *            choice buttons. If false, buttons will only display if the
	 *            user
	 *
	 *            hasn't previously picked a screen.
	 *
	 */

	public FullScreenWindow(JComponent part, boolean forcechoice) {
		init(part, forcechoice);
	}

	/**
	 *
	 * Create a full screen window containing a JComponent. The user
	 *
	 * will only be asked which screen to display on if there are multiple
	 *
	 * monitors attached and the user hasn't already made a choice.
	 *
	 * @param part
	 *            the JComponent to display
	 *
	 */

	public FullScreenWindow(JComponent part) {

		// super();

		init(part, false);

	}

	/**
	 *
	 * Close the full screen window. This particular FullScreenWindow
	 *
	 * object cannot be used again.
	 *
	 */

	public void close() {

		dead = true;

		flag.set();

		screen.setFullScreenWindow(null);

		if (jf != null) {

			jf.dispose();

		}

	}

	/**
	 * Create the window, asking for which screen to use if there are multiple
	 * monitors and either forcechoice is true, or the user hasn't already
	 * picked a screen.
	 *
	 * @param part
	 *            the JComponent to display
	 * @param forcechoice
	 *            false if user shouldn't be asked twice which of several
	 *            monitors to use.
	 */

	private void init(JComponent part, boolean forcechoice) {

		if (forcechoice) {

			defaultScreen = null;

		}

		screen = null;

		GraphicsEnvironment ge =

				GraphicsEnvironment.getLocalGraphicsEnvironment();

		GraphicsDevice screens[] = ge.getScreenDevices();

		if (defaultScreen != null) {

			for (GraphicsDevice screen2 : screens) {

				if (screen2 == defaultScreen) {

					screen = defaultScreen;

				}

			}

		}

		if (screens.length == 1) {

			screen = screens[0];

		}

		if (screen == null) {

			screen = pickScreen(screens);

		}

		if (dead) {

			return;

		}

		defaultScreen = screen;

		GraphicsConfiguration gc = screen.getDefaultConfiguration();

		jf = new JFrame(gc);

		jf.setUndecorated(true);

		jf.setBounds(gc.getBounds());

		jf.getContentPane().add(part);

		jf.setVisible(true);

		screen.setFullScreenWindow(jf);

	}

	/**
	 * A button that appears on a particular graphics device, asking whether
	 * that device should be used for multiple-monitor choices.
	 */

	private class PickMe extends JFrame {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -658915481325845436L;

		/** The mygd. */
		private GraphicsDevice mygd;

		/**
		 * Creates the PickMe button on a particular display.
		 *
		 * @param gd
		 *            the GraphicsDevice (display) to use for this button
		 */

		public PickMe(GraphicsDevice gd) {

			super(gd.getDefaultConfiguration());

			// super((java.awt.Frame)null, false);

			setUndecorated(true);

			mygd = gd;

			JButton jb = new JButton("Click here to use this screen");

			jb.setBackground(Color.yellow);

			jb.addActionListener(evt -> pickDevice(mygd));

			Dimension sz = jb.getPreferredSize();

			sz.width += 30;

			sz.height = 0;

			jb.setPreferredSize(sz);

			getContentPane().add(jb);

			pack();

			Rectangle bounds = gd.getDefaultConfiguration().getBounds();

			int x = bounds.width / 2 - sz.width / 2 + bounds.x;

			int y = bounds.height / 2 - sz.height / 2 + bounds.y;

			setLocation(x, y);

			setVisible(true);

		}
	}

	/**
	 *
	 * Select a particular screen for display of this window, and set
	 *
	 * the flag.
	 *
	 */

	private void pickDevice(GraphicsDevice gd) {

		pickedDevice = gd;

		flag.set();

	}

	/**
	 *
	 * Displays a button on each attached monitor, and returns the
	 *
	 * GraphicsDevice object associated with that monitor.
	 *
	 * @param scrns
	 *            a list of GraphicsDevices on which to display buttons
	 *
	 * @return the GraphicsDevice selected.
	 *
	 */

	private GraphicsDevice pickScreen(GraphicsDevice scrns[]) {

		flag.clear();

		PickMe pickers[] = new PickMe[scrns.length];

		for (int i = 0; i < scrns.length; i++) {
			pickers[i] = new PickMe(scrns[i]);
		}

		flag.waitForFlag();

		for (PickMe picker : pickers) {

			if (picker != null) {

				picker.dispose();

			}

		}

		return pickedDevice;

	}

}
