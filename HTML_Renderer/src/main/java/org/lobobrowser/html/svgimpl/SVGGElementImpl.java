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
package org.lobobrowser.html.svgimpl;

import org.lobobrowser.html.jsimpl.EventException;
import org.lobobrowser.w3c.events.Event;
import org.lobobrowser.w3c.svg.SVGGElement;
import org.w3c.dom.DOMException;

public class SVGGElementImpl extends SVGSVGElementImpl implements SVGGElement {

	private SVGSVGElementImpl svg;

	public SVGGElementImpl(String name) {
		super(name);
	}

	@Override
	public boolean dispatchEvent(Event evt) throws EventException, DOMException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @return the svg
	 */
	public SVGSVGElementImpl getSvg() {
		return svg;
	}

	/**
	 * @param svg
	 *            the svg to set
	 */
	public void setSvg(SVGSVGElementImpl svg) {
		this.svg = svg;
	}

}
