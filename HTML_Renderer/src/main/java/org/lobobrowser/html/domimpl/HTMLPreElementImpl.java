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
 * Created on Feb 12, 2006
 */
package org.lobobrowser.html.domimpl;


import org.lobobrowser.html.renderstate.PreRenderState;
import org.lobobrowser.html.renderstate.RenderState;
import org.lobobrowser.html.style.HtmlValues;
import org.lobobrowser.w3c.html.HTMLPreElement;

/**
 * The Class HTMLPreElementImpl.
 */
public class HTMLPreElementImpl extends HTMLAbstractUIElement implements HTMLPreElement {

	/**
	 * Instantiates a new HTML pre element impl.
	 *
	 * @param name
	 *            the name
	 */
	public HTMLPreElementImpl(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLPreElement#getWidth()
	 */
	@Override
	public int getWidth() {
		String widthText = this.getAttribute(WIDTH);
		return HtmlValues.getPixelSize(widthText, null, 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLPreElement#setWidth(int)
	 */
	@Override
	public void setWidth(int width) {
		this.setAttribute(WIDTH, String.valueOf(width));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.html.domimpl.HTMLElementImpl#createRenderState(org.
	 * lobobrowser .html.renderstate.RenderState)
	 */
	@Override
	protected RenderState createRenderState(RenderState prevRenderState) {
		return new PreRenderState(prevRenderState, this);
	}
}
