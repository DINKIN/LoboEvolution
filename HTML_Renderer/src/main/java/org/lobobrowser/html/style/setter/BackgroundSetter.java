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
package org.lobobrowser.html.style.setter;

import org.lobobrowser.html.renderstate.BackgroundRenderState;
import org.lobobrowser.html.style.AbstractCSS2Properties;
import org.lobobrowser.html.style.HtmlValues;
import org.lobobrowser.util.Strings;
import org.lobobrowser.util.gui.ColorFactory;
import org.w3c.dom.css.CSSStyleDeclaration;

import com.steadystate.css.util.CSSProperties;

public class BackgroundSetter implements SubPropertySetter, CSSProperties {

	@Override
	public void changeValue(AbstractCSS2Properties properties, String newValue, CSSStyleDeclaration declaration,
			boolean important) {
		properties.setPropertyValueLCAlt(BACKGROUND, newValue, important);
		if (!Strings.isBlank(newValue)) {
			String[] tokens = HtmlValues.splitCssValue(newValue);
			boolean hasXPosition = false;
			boolean hasYPosition = false;
			String color = null;
			String image = null;
			String backgroundRepeat = null;
			String position = null;
			for (String token : tokens) {
				if (ColorFactory.getInstance().isColor(token)) {
					color = token;
				} else if (HtmlValues.isUrl(token)) {
					image = token;
				} else if (BackgroundRenderState.isBackgroundRepeat(token)) {
					backgroundRepeat = token;
				} else if (BackgroundRenderState.isBackgroundPosition(token)) {
					if (hasXPosition && !hasYPosition) {
						position += " " + token;
						hasYPosition = true;
					} else {
						hasXPosition = true;
						position = token;
					}
				} else if ("none".equals(newValue.toLowerCase())) {
					color = "transparent";
					image = "none";
				}
			}
			if (color != null) {
				properties.setPropertyValueLCAlt(BACKGROUND_COLOR, color, important);
			}
			if (image != null) {
				properties.setPropertyValueProcessed(BACKGROUND_IMAGE, image, declaration, important);
			}
			if (backgroundRepeat != null) {
				properties.setPropertyValueLCAlt(BACKGROUND_REPEAT, backgroundRepeat, important);
			}
			if (position != null) {
				properties.setPropertyValueLCAlt(BACKGROUND_POSITION, position, important);
			}
		}
	}

	/**
	 * Change value.
	 *
	 * @param properties
	 *            the properties
	 * @param newValue
	 *            the new value
	 * @param declaration
	 *            the declaration
	 */
	public void changeValue(AbstractCSS2Properties properties, String newValue, CSSStyleDeclaration declaration) {
		this.changeValue(properties, newValue, declaration, true);
	}

}
