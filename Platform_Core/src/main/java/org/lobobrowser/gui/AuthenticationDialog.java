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
package org.lobobrowser.gui;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.net.PasswordAuthentication;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Dialog used in HTTP and proxy authentication.
 */
public class AuthenticationDialog extends JDialog {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The user name field. */
	private final JTextField userNameField = new JTextField();

	/** The password field. */
	private final JPasswordField passwordField = new JPasswordField();
	
	/** The authentication. */
	private PasswordAuthentication authentication;

	/**
	 * Instantiates a new authentication dialog.
	 *
	 * @param owner
	 *            the owner
	 * @throws HeadlessException
	 *             the headless exception
	 */
	public AuthenticationDialog(Frame owner) throws HeadlessException {
		super(owner);
		this.init();
	}

	/**
	 * Instantiates a new authentication dialog.
	 *
	 * @param owner
	 *            the owner
	 * @throws HeadlessException
	 *             the headless exception
	 */
	public AuthenticationDialog(Dialog owner) throws HeadlessException {
		super(owner);
		this.init();
	}

	/**
	 * Inits the.
	 */
	private void init() {
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new FlowLayout());

		Box rootBox = new Box(BoxLayout.Y_AXIS);
		rootBox.setBorder(new EmptyBorder(4, 4, 4, 4));

		Box userNameBox = new Box(BoxLayout.X_AXIS);
		JLabel userNameLabel = new JLabel("User name:");
		int unph = userNameLabel.getPreferredSize().height;
		userNameLabel.setPreferredSize(new Dimension(100, unph));
		userNameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		userNameBox.add(userNameLabel);
		userNameBox.add(Box.createRigidArea(new Dimension(4, 1)));
		userNameBox.add(this.userNameField);
		userNameBox.setPreferredSize(new Dimension(300, unph + 4));

		Box passwordBox = new Box(BoxLayout.X_AXIS);
		JLabel passwordLabel = new JLabel("Password:");
		int pwph = passwordLabel.getPreferredSize().height;
		passwordLabel.setPreferredSize(new Dimension(100, pwph));
		passwordLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		passwordBox.add(passwordLabel);
		passwordBox.add(Box.createRigidArea(new Dimension(4, 1)));
		passwordBox.add(this.passwordField);
		passwordBox.setPreferredSize(new Dimension(300, pwph + 4));

		Box buttonBox = new Box(BoxLayout.X_AXIS);
		JButton okButton = new JButton();
		okButton.setAction(new OkAction());
		okButton.setText("OK");
		JButton cancelButton = new JButton();
		cancelButton.setAction(new CancelAction());
		cancelButton.setText("Cancel");
		buttonBox.add(Box.createHorizontalGlue());
		buttonBox.add(okButton);
		buttonBox.add(Box.createHorizontalStrut(4));
		buttonBox.add(cancelButton);
		buttonBox.add(Box.createHorizontalGlue());

		rootBox.add(userNameBox);
		rootBox.add(Box.createVerticalStrut(2));
		rootBox.add(passwordBox);
		rootBox.add(Box.createVerticalStrut(4));
		rootBox.add(buttonBox);

		contentPane.add(rootBox);
	}

	/**
	 * Sets the user name.
	 *
	 * @param userName
	 *            the new user name
	 */
	public void setUserName(String userName) {
		this.userNameField.setText(userName);
		this.passwordField.grabFocus();
	}

	/**
	 * Gets the authentication.
	 *
	 * @return the authentication
	 */
	public PasswordAuthentication getAuthentication() {
		return this.authentication;
	}

	/**
	 * The Class OkAction.
	 */
	private class OkAction extends AbstractAction {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.
		 * ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			authentication = new PasswordAuthentication(userNameField.getText(), passwordField.getPassword());
			AuthenticationDialog.this.dispose();
		}
	}

	/**
	 * The Class CancelAction.
	 */
	private class CancelAction extends AbstractAction {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1L;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.
		 * ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			authentication = null;
			AuthenticationDialog.this.dispose();
		}
	}
}
