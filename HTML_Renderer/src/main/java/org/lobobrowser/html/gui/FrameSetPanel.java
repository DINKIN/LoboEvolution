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
 * Created on Jan 29, 2006
 */
package org.lobobrowser.html.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lobobrowser.html.BrowserFrame;
import org.lobobrowser.html.HtmlAttributeProperties;
import org.lobobrowser.html.HtmlRendererContext;
import org.lobobrowser.html.dombl.FrameNode;
import org.lobobrowser.html.domimpl.DOMNodeImpl;
import org.lobobrowser.html.domimpl.HTMLElementImpl;
import org.lobobrowser.html.renderer.NodeRenderer;
import org.lobobrowser.html.style.HtmlLength;
import org.lobobrowser.util.gui.WrapperLayout;

/**
 * A Swing panel used to render FRAMESETs only. It is used by {@link HtmlPanel}
 * when a document is determined to be a FRAMESET.
 *
 * @see HtmlPanel
 * @see HtmlBlockPanel
 */
public class FrameSetPanel extends JComponent implements NodeRenderer, HtmlAttributeProperties {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(FrameSetPanel.class.getName());
	
	/** The root node. */
	private HTMLElementImpl rootNode;
	
	/** The html context. */
	private HtmlRendererContext htmlContext;

	/** The frame components. */
	private Component[] frameComponents;

	/** The dom invalid. */
	private boolean domInvalid = true;

	/**
	 * Instantiates a new frame set panel.
	 */
	public FrameSetPanel() {
		super();
		this.setLayout(WrapperLayout.getInstance());
		// TODO: This should be a temporary preferred size
		this.setPreferredSize(new Dimension(600, 400));
	}

	/**
	 * Gets the lengths.
	 *
	 * @param spec
	 *            the spec
	 * @return the lengths
	 */
	private HtmlLength[] getLengths(String spec) {
		if (spec == null) {
			return new HtmlLength[] { new HtmlLength("1*") };
		}
		StringTokenizer tok = new StringTokenizer(spec, ",");
		ArrayList<HtmlLength> lengths = new ArrayList<HtmlLength>();
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken().trim();
			try {
				lengths.add(new HtmlLength(token));
			} catch (Exception err) {
				logger.warn("Frame rows or cols value [" + spec + "] is invalid.");
			}
		}
		return lengths.toArray(HtmlLength.EMPTY_ARRAY);
	}

	/**
	 * Gets the sub frames.
	 *
	 * @param parent
	 *            the parent
	 * @return the sub frames
	 */
	private HTMLElementImpl[] getSubFrames(HTMLElementImpl parent) {
		DOMNodeImpl[] children = parent.getChildrenArray();
		ArrayList<DOMNodeImpl> subFrames = new ArrayList<DOMNodeImpl>();
		for (DOMNodeImpl child : children) {
			if (child instanceof HTMLElementImpl) {
				String nodeName = child.getNodeName();
				if ("FRAME".equalsIgnoreCase(nodeName) || "FRAMESET".equalsIgnoreCase(nodeName)) {
					subFrames.add(child);
				}
			}
		}
		return subFrames.toArray(new HTMLElementImpl[0]);
	}

	/**
	 * Sets the FRAMESET node and invalidates the component so it can be
	 * rendered immediately in the GUI thread.
	 *
	 * @param node
	 *            the new root node
	 */
	@Override
	public void setRootNode(DOMNodeImpl node) {
		// Method expected to be called in the GUI thread.
		if (!(node instanceof HTMLElementImpl)) {
			throw new IllegalArgumentException("node=" + node);
		}
		HTMLElementImpl element = (HTMLElementImpl) node;
		this.rootNode = element;
		HtmlRendererContext context = element.getHtmlRendererContext();
		this.htmlContext = context;
		this.domInvalid = true;
		this.invalidate();
		this.validateAll();
		this.repaint();
	}

	/**
	 * Validate all.
	 */
	protected void validateAll() {
		Component toValidate = this;
		for (;;) {
			Container parent = toValidate.getParent();
			if (parent == null || parent.isValid()) {
				break;
			}
			toValidate = parent;
		}
		toValidate.validate();
	}

	/**
	 * Process document notifications.
	 *
	 * @param notifications
	 *            the notifications
	 */
	public final void processDocumentNotifications(DocumentNotification[] notifications) {
		// Called in the GUI thread.
		if (notifications.length > 0) {
			// Not very efficient, but it will do.
			this.domInvalid = true;
			this.invalidate();
			if (this.isVisible()) {
				this.validate();
				this.repaint();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#setBounds(int, int, int, int)
	 */
	@Override
	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, y, w, h);
	}

	/**
	 * This method is invoked by AWT in the GUI thread to lay out the component.
	 * This implementation is an override.
	 */
	@Override
	public void doLayout() {
		if (this.domInvalid) {
			this.domInvalid = false;
			this.removeAll();
			HtmlRendererContext context = this.htmlContext;
			if (context != null) {
				HTMLElementImpl element = this.rootNode;
				String rows = element.getAttribute(ROWS);
				String cols = element.getAttribute(COLS);
				HtmlLength[] rowLengths = this.getLengths(rows);
				HtmlLength[] colLengths = this.getLengths(cols);
				HTMLElementImpl[] subframes = this.getSubFrames(element);
				Component[] frameComponents = new Component[subframes.length];
				this.frameComponents = frameComponents;
				for (int i = 0; i < subframes.length; i++) {
					HTMLElementImpl frameElement = subframes[i];
					if (frameElement != null && "FRAMESET".equalsIgnoreCase(frameElement.getTagName())) {
						FrameSetPanel fsp = new FrameSetPanel();
						fsp.setRootNode(frameElement);
						frameComponents[i] = fsp;
					} else {
						if (frameElement instanceof FrameNode) {
							BrowserFrame frame = context.createBrowserFrame();
							((FrameNode) frameElement).setBrowserFrame(frame);
							String src = frameElement.getAttribute(SRC);
							if (src != null) {
								URL url;
								try {
									url = frameElement.getFullURL(src);
									if (url != null) {
										frame.loadURL(url);
									}
								} catch (MalformedURLException mfu) {
									logger.warn("Frame URI=[" + src + "] is malformed.");
								}
							}
							frameComponents[i] = frame.getComponent();
						} else {
							frameComponents[i] = new JPanel();
						}
					}

				}
				HtmlLength[] rhl = rowLengths;
				HtmlLength[] chl = colLengths;
				Component[] fc = this.frameComponents;
				if (rhl != null && chl != null && fc != null) {
					Dimension size = this.getSize();
					Insets insets = this.getInsets();
					int width = size.width - insets.left - insets.right;
					int height = size.height - insets.left - insets.right;
					int[] absColLengths = this.getAbsoluteLengths(chl, width);
					int[] absRowLengths = this.getAbsoluteLengths(rhl, height);
					this.add(this.getSplitPane(this.htmlContext, absColLengths, 0, absColLengths.length, absRowLengths,
							0, absRowLengths.length, fc));
				}
			}
		}
		super.doLayout();
	}

	/**
	 * Gets the absolute lengths.
	 *
	 * @param htmlLengths
	 *            the html lengths
	 * @param totalSize
	 *            the total size
	 * @return the absolute lengths
	 */
	private int[] getAbsoluteLengths(HtmlLength[] htmlLengths, int totalSize) {
		int[] absLengths = new int[htmlLengths.length];
		int totalSizeNonMulti = 0;
		int sumMulti = 0;
		for (int i = 0; i < htmlLengths.length; i++) {
			HtmlLength htmlLength = htmlLengths[i];
			int lengthType = htmlLength.getLengthType();
			if (lengthType == HtmlLength.PIXELS) {
				int absLength = htmlLength.getRawValue();
				totalSizeNonMulti += absLength;
				absLengths[i] = absLength;
			} else if (lengthType == HtmlLength.LENGTH) {
				int absLength = htmlLength.getLength(totalSize);
				totalSizeNonMulti += absLength;
				absLengths[i] = absLength;
			} else {
				sumMulti += htmlLength.getRawValue();
			}
		}
		int remaining = totalSize - totalSizeNonMulti;
		if (remaining > 0 && sumMulti > 0) {
			for (int i = 0; i < htmlLengths.length; i++) {
				HtmlLength htmlLength = htmlLengths[i];
				if (htmlLength.getLengthType() == HtmlLength.MULTI_LENGTH) {
					int absLength = remaining * htmlLength.getRawValue() / sumMulti;
					absLengths[i] = absLength;
				}
			}
		}
		return absLengths;
	}

	/**
	 * Gets the split pane.
	 *
	 * @param context
	 *            the context
	 * @param colLengths
	 *            the col lengths
	 * @param firstCol
	 *            the first col
	 * @param numCols
	 *            the num cols
	 * @param rowLengths
	 *            the row lengths
	 * @param firstRow
	 *            the first row
	 * @param numRows
	 *            the num rows
	 * @param frameComponents
	 *            the frame components
	 * @return the split pane
	 */
	private Component getSplitPane(HtmlRendererContext context, int[] colLengths, int firstCol, int numCols,
			int[] rowLengths, int firstRow, int numRows, Component[] frameComponents) {
		if (numCols == 1) {
			int frameindex = colLengths.length * firstRow + firstCol;
			Component topComponent = frameindex < frameComponents.length ? frameComponents[frameindex] : null;
			if (numRows == 1) {
				return topComponent;
			} else {
				Component bottomComponent = this.getSplitPane(context, colLengths, firstCol, numCols, rowLengths,
						firstRow + 1, numRows - 1, frameComponents);
				JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topComponent, bottomComponent);
				sp.setDividerLocation(rowLengths[firstRow]);
				return sp;
			}
		} else {
			Component rightComponent = this.getSplitPane(context, colLengths, firstCol + 1, numCols - 1, rowLengths,
					firstRow, numRows, frameComponents);
			Component leftComponent = this.getSplitPane(context, colLengths, firstCol, 1, rowLengths, firstRow, numRows,
					frameComponents);
			JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftComponent, rightComponent);
			sp.setDividerLocation(colLengths[firstCol]);
			return sp;
		}
	}
}
