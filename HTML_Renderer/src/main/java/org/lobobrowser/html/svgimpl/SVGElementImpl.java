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

import org.lobobrowser.html.domimpl.HTMLAbstractUIElement;
import org.lobobrowser.w3c.svg.SVGElement;
import org.lobobrowser.w3c.svg.SVGSVGElement;
import org.w3c.dom.Node;
import org.w3c.dom.svg.SVGSymbolElement;

public class SVGElementImpl extends HTMLAbstractUIElement implements SVGElement {

	private SVGDocumentImpl ownerDoc = null;

	public SVGElementImpl(String name) {
		super(name);
	}

	public SVGDocumentImpl getOwnerDoc() {
		return ownerDoc;
	}

	public void setOwnerDoc(SVGDocumentImpl v) {
		this.ownerDoc = v;
	}

	@Override
	public SVGSVGElement getOwnerSVGElement() {
		if (getParentNode() == ownerDoc) {
			return null;
		}
		Node parent = getParentNode();
		while (parent != null && !(parent instanceof SVGSVGElement)) {
			parent = parent.getParentNode();
		}

		if (parent instanceof SVGGElementImpl) {
			SVGGElementImpl a = (SVGGElementImpl) parent;
			return a.getSvg();
		}

		return (SVGSVGElement) parent;
	}

	@Override
	public SVGElement getViewportElement() {
		if (getParentNode() == ownerDoc) {
			return null;
		}
		Node parent = getParentNode();
		while (parent != null && !(parent instanceof SVGSVGElement || parent instanceof SVGSymbolElement)) {
			parent = parent.getParentNode();
		}
		return (SVGElement) parent;
	}
}
