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
 * Created on Jan 14, 2006
 */
package org.lobobrowser.html.domimpl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.lobobrowser.html.FormInput;
import org.lobobrowser.html.HtmlRendererContext;
import org.lobobrowser.html.dombl.DescendentHTMLCollection;
import org.lobobrowser.html.dombl.NodeVisitor;
import org.lobobrowser.html.dombl.StopVisitorException;
import org.lobobrowser.html.domfilter.InputFilter;
import org.lobobrowser.html.js.Executor;
import org.lobobrowser.w3c.html.HTMLCollection;
import org.lobobrowser.w3c.html.HTMLFormElement;
import org.mozilla.javascript.Function;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * The Class HTMLFormElementImpl.
 */
public class HTMLFormElementImpl extends HTMLAbstractUIElement implements HTMLFormElement {
	
	/** The elements. */
	private HTMLCollection elements;

	/** The onsubmit. */
	private Function onsubmit;

	/**
	 * Instantiates a new HTML form element impl.
	 *
	 * @param name
	 *            the name
	 */
	public HTMLFormElementImpl(String name) {
		super(name);
	}

	/**
	 * Instantiates a new HTML form element impl.
	 */
	public HTMLFormElementImpl() {
		super(FORM);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#namedItem(java.lang.String)
	 */
	@Override
	public Object namedItem(final String name) {
		try {
			// TODO: This could use document.namedItem.
			this.visit(node -> {
				if (HTMLFormElementImpl.isInput(node)
						&& name.equals(((Element) node).getAttribute(NAME))) {
					throw new StopVisitorException(node);
				}				
			});
		} catch (StopVisitorException sve) {
			return sve.getTag();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#item(int)
	 */
	@Override
	public Object item(final int index) {
		try {
			this.visit(new NodeVisitor() {
				private int current = 0;

				@Override
				public void visit(Node node) {
					if (HTMLFormElementImpl.isInput(node)) {
						if (this.current == index) {
							throw new StopVisitorException(node);
						}
						this.current++;
					}
				}
			});
		} catch (StopVisitorException sve) {
			return sve.getTag();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#getElements()
	 */
	@Override
	public HTMLCollection getElements() {
		HTMLCollection elements = this.elements;
		if (elements == null) {
			elements = new DescendentHTMLCollection(this, new InputFilter(), this.getTreeLock(), false);
			this.elements = elements;
		}
		return elements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#getLength()
	 */
	@Override
	public int getLength() {
		return this.getElements().getLength();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#getName()
	 */
	@Override
	public String getName() {
		return this.getAttribute(NAME);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		this.setAttribute(NAME, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#getAcceptCharset()
	 */
	@Override
	public String getAcceptCharset() {
		return this.getAttribute(ACCEPTCHARSET);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#setAcceptCharset(java.lang.
	 * String)
	 */
	@Override
	public void setAcceptCharset(String acceptCharset) {
		this.setAttribute(ACCEPTCHARSET, acceptCharset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#getAction()
	 */
	@Override
	public String getAction() {
		return this.getAttribute(ACTION);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#setAction(java.lang.String)
	 */
	@Override
	public void setAction(String action) {
		this.setAttribute(ACTION, action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#getEnctype()
	 */
	@Override
	public String getEnctype() {
		return this.getAttribute(ENCTYPE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.lobobrowser.w3c.html.HTMLFormElement#setEnctype(java.lang.String)
	 */
	@Override
	public void setEnctype(String enctype) {
		this.setAttribute(ENCTYPE, enctype);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#getMethod()
	 */
	@Override
	public String getMethod() {
		String method = this.getAttribute(METHOD);
		if (method == null) {
			method = "GET";
		}
		return method;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#setMethod(java.lang.String)
	 */
	@Override
	public void setMethod(String method) {
		this.setAttribute(METHOD, method);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#getTarget()
	 */
	@Override
	public String getTarget() {
		return this.getAttribute(TARGET);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#setTarget(java.lang.String)
	 */
	@Override
	public void setTarget(String target) {
		this.setAttribute(TARGET, target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#submit()
	 */
	@Override
	public void submit() {
		this.submit(null);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.html.domimpl.HTMLAbstractUIElement#setOnsubmit(org.
	 * mozilla .javascript.Function)
	 */
	@Override
	public void setOnsubmit(Function value) {
		this.onsubmit = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.html.domimpl.HTMLAbstractUIElement#getOnsubmit()
	 */
	@Override
	public Function getOnsubmit() {
		return this.getEventFunction(this.onsubmit, "onsubmit");
	}

	/**
	 * This method should be called when form submission is done by a submit
	 * button.
	 *
	 * @param extraFormInputs
	 *            Any additional form inputs that need to be submitted, e.g. the
	 *            submit button parameter.
	 */
	public final void submit(final FormInput[] extraFormInputs) {
		Function onsubmit = this.getOnsubmit();
		if (onsubmit != null && !Executor.executeFunction(this, onsubmit, null)) {
			return;
		}
		
		HtmlRendererContext context = this.getHtmlRendererContext();
		if (context != null) {
			final ArrayList<FormInput> formInputs = new ArrayList<FormInput>();
			if (extraFormInputs != null) {
				for (FormInput extraFormInput : extraFormInputs) {
					formInputs.add(extraFormInput);
				}
			}
			this.visit(node -> {
				if (node instanceof HTMLElementImpl) {
					FormInput[] fis = ((HTMLElementImpl) node).getFormInputs();
					if (fis != null) {
						for (FormInput fi : fis) {
							if (fi.getName() == null) {
								throw new IllegalStateException("Form input does not have a name: " + node);
							}
							formInputs.add(fi);
						}
					}
				}
			});
			FormInput[] fia = formInputs.toArray(FormInput.EMPTY_ARRAY);
			String href = this.getAction();
			if (href == null) {
				href = this.getBaseURI();
			}
			try {
				URL url = this.getFullURL(href);
				context.submitForm(this.getMethod(), url, this.getTarget(), this.getEnctype(), fia);
			} catch (MalformedURLException mfu) {
				logger.error("submit()", mfu);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#reset()
	 */
	@Override
	public void reset() {
		this.visit(node -> {
			if (node instanceof HTMLBaseInputElement) {
				((HTMLBaseInputElement) node).resetInput();
			}
		});
	}

	/**
	 * Checks if is input.
	 *
	 * @param node
	 *            the node
	 * @return true, if is input
	 */
	public static boolean isInput(Node node) {
		String name = node.getNodeName().toLowerCase();
		return name.equals("input") || name.equals("textarea") || name.equals("select");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#getAutocomplete()
	 */
	@Override
	public boolean getAutocomplete() {
		String autocomplete = this.getAttribute(AUTOCOMPLETE);
		return AUTOCOMPLETE.equalsIgnoreCase(autocomplete);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#setAutocomplete(boolean)
	 */
	@Override
	public void setAutocomplete(boolean autocomplete) {
		this.setAttribute(AUTOCOMPLETE,
				autocomplete ? AUTOCOMPLETE : null);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#getNoValidate()
	 */
	@Override
	public boolean getNoValidate() {
		String noValidate = this.getAttribute(NOVALIDATE);
		return NOVALIDATE.equalsIgnoreCase(noValidate);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#setNoValidate(boolean)
	 */
	@Override
	public void setNoValidate(boolean noValidate) {
		this.setAttribute(NOVALIDATE, noValidate ? NOVALIDATE : null);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#checkValidity()
	 */
	@Override
	public boolean checkValidity() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.lobobrowser.w3c.html.HTMLFormElement#dispatchFormInput()
	 */
	@Override
	public void dispatchFormInput() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAutocomplete(String autocomplete) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getEncoding() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setEncoding(String encoding) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getElement(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getElement(String name) {
		// TODO Auto-generated method stub
		return null;
	}
}
