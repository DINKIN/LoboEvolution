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
package org.lobobrowser.primary.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class ColorComboBox extends JComboBox<Object> {

	private static final long serialVersionUID = 1L;

	public ColorComboBox() {
		int[] values = new int[] { 0, 128, 192, 255 };
		for (int value : values) {
			for (int value2 : values) {
				for (int value3 : values) {
					Color c = new Color(value, value2, value3);
					addItem(c);
				}
			}
		}
		setRenderer(new ColorComboRenderer());

	}

	class ColorComboRenderer extends JPanel implements ListCellRenderer<Object> {
		private static final long serialVersionUID = 1L;
		protected Color m_c = Color.black;

		public ColorComboRenderer() {
			super();
			setBorder(new CompoundBorder(new MatteBorder(2, 10, 2, 10, Color.white), new LineBorder(Color.black)));
		}

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object obj, int row, boolean sel,
				boolean hasFocus) {
			if (obj instanceof Color) {
				m_c = (Color) obj;
			}
			return this;
		}

		@Override
		public void paint(Graphics g) {
			setBackground(m_c);
			super.paint(g);
		}

	}

}
