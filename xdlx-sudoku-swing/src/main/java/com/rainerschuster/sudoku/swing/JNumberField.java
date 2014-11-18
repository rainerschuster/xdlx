/*
 * Copyright 2007 Rainer Schuster
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rainerschuster.sudoku.swing;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/** Provides an input field for <em>non negative</em> numbers. */
public class JNumberField extends JTextField {

	// TODO assert that minNumber <= maxNumber

	private static final long serialVersionUID = 1L;

	private int minNumber = -1;
	private int maxNumber = -1;
	//private int maxChars = -1;
	/*private char[] allowedChars = new char[] {
		'1', '2', '3', '4', '5', '6', '7', '8', '9'
	};*/

	protected class JNumberDocument extends PlainDocument {
		private static final long serialVersionUID = 1L;

		@Override
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			try {
				//if (maxChars >= 0 && offs + str.length() <= maxChars) {
				// ensures that the string is a number
				int num = Integer.parseInt(getText(0, getLength()) + str);
				
				if (minNumber >= 0 && num < minNumber) {
					System.err.println("Number too small!");
					return;
				}
				if (maxNumber >= 0 && num > maxNumber) {
					System.err.println("Number too big!");
					return;
				}
				
				super.insertString(offs, str, a);
			} catch (NumberFormatException e) {
				System.err.println("Invalid number!");
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTextField#createDefaultModel()
	 */
	@Override
	protected Document createDefaultModel() {
		return new JNumberDocument();
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.JTextComponent#setText(java.lang.String)
	 */
	@Override
	public void setText(String t) {
		setNumber(Integer.valueOf(t));
		//super.setText(t);
	}

	public Integer getNumber() {
		if (super.getText().equals("")) {
			return 0;
		}
		return Integer.valueOf(super.getText());
	}

	public void setNumber(Integer val) {
		super.setText(val == null ? "" : val.toString());
	}

	public int getMinNumber() {
		return minNumber;
	}

	public void setMinNumber(int minNumber) {
		this.minNumber = minNumber;
	}

	public int getMaxNumber() {
		return maxNumber;
	}

	public void setMaxNumber(int maxNumber) {
		this.maxNumber = maxNumber;
	}

	/**
	 * @return the maximum number of allowed characters
	 */
	/*public int getMaxChars() {
		return maxChars;
	}*/

	/**
	 * @param maxChars sets the maximum number of allowed characters
	 */
	/*public void setMaxChars(int maxChars) {
		this.maxChars = maxChars;
	}*/

}
