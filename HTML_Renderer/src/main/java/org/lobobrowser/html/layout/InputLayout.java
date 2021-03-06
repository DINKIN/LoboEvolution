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
package org.lobobrowser.html.layout;


import org.lobobrowser.html.control.BaseInputControl;
import org.lobobrowser.html.control.InputButtonControl;
import org.lobobrowser.html.control.InputCheckboxControl;
import org.lobobrowser.html.control.InputColorPickerControl;
import org.lobobrowser.html.control.InputDatePickerControl;
import org.lobobrowser.html.control.InputEmailControl;
import org.lobobrowser.html.control.InputFileControl;
import org.lobobrowser.html.control.InputHiddenControl;
import org.lobobrowser.html.control.InputImageControl;
import org.lobobrowser.html.control.InputNumberControl;
import org.lobobrowser.html.control.InputPasswordControl;
import org.lobobrowser.html.control.InputPhoneControl;
import org.lobobrowser.html.control.InputRadioControl;
import org.lobobrowser.html.control.InputTextControl;
import org.lobobrowser.html.control.InputUrlControl;
import org.lobobrowser.html.control.RUIControl;
import org.lobobrowser.html.domimpl.HTMLBaseInputElement;
import org.lobobrowser.html.domimpl.HTMLElementImpl;
import org.lobobrowser.html.renderer.RBlockViewport;
import org.lobobrowser.html.renderer.RElement;

/**
 * The Class InputLayout.
 */
public class InputLayout extends CommonWidgetLayout {

	/**
	 * Instantiates a new input layout.
	 */
	public InputLayout() {
		super(ADD_INLINE, true);
	}

	@Override
	protected RElement createRenderable(RBlockViewport bodyLayout, HTMLElementImpl markupElement) {
		HTMLBaseInputElement bie = (HTMLBaseInputElement) markupElement;
		BaseInputControl uiControl = createInputControl(bie);
		if (uiControl == null) {
			return null;
		}
		bie.setInputContext(uiControl);
		return new RUIControl(markupElement, uiControl, bodyLayout.getContainer(), bodyLayout.getFrameContext(),
				bodyLayout.getUserAgentContext());
	}

	/**
	 * Creates the input control.
	 *
	 * @param markupElement
	 *            the markup element
	 * @return the base input control
	 */
	private final BaseInputControl createInputControl(HTMLBaseInputElement markupElement) {
		String type = markupElement.getAttribute(TYPE) == null ? ""
				: markupElement.getAttribute(TYPE);

		switch (type) {
		case "text":
			return new InputTextControl(markupElement);
		case "hidden":
			return new InputHiddenControl(markupElement);
		case "submit":
			return new InputButtonControl(markupElement);
		case "password":
			return new InputPasswordControl(markupElement);
		case "file":
			return new InputFileControl(markupElement);
		case "number":
			return new InputNumberControl(markupElement);
		case "email":
			return new InputEmailControl(markupElement);
		case "color":
			return new InputColorPickerControl(markupElement);
		case "url":
			return new InputUrlControl(markupElement);
		case "tel":
			return new InputPhoneControl(markupElement);
		case "date":
		case "datetime":
		case "datetime-local":
		case "month":
			return new InputDatePickerControl(markupElement);
		case "radio":
			return new InputRadioControl(markupElement);
		case "checkbox":
			return new InputCheckboxControl(markupElement);
		case "button":
			return new InputButtonControl(markupElement);
		case "image":
			return new InputImageControl(markupElement);
		case "reset":
			return new InputButtonControl(markupElement);
		default:
			return new InputTextControl(markupElement);
		}
	}
}
