//
// Copyright (C) 2005 United States Government as represented by the
// Administrator of the National Aeronautics and Space Administration
// (NASA).  All Rights Reserved.
// 
// This software is distributed under the NASA Open Source Agreement
// (NOSA), version 1.3.  The NOSA has been approved by the Open Source
// Initiative.  See the file NOSA.txt at the top of the distribution
// directory tree for the complete NOSA document.
// 
// THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY
// KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT
// LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO
// SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR
// A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT
// THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT
// DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE.
//
package junit.samples.money;

/**
 * The common interface for simple Monies and MoneyBags
 *
 */
public interface IMoney {
	/**
	 * Adds a money to this money.
	 */
	public abstract IMoney add(IMoney m);
	/**
	 * Adds a simple Money to this money. This is a helper method for
	 * implementing double dispatch
	 */
	public abstract IMoney addMoney(Money m);
	/**
	 * Adds a MoneyBag to this money. This is a helper method for
	 * implementing double dispatch
	 */
	public abstract IMoney addMoneyBag(MoneyBag s);
	/**
	 * Tests whether this money is zero
	 */
	public abstract boolean isZero();
	/**
	 * Multiplies a money by the given factor.
	 */
	public abstract IMoney multiply(int factor);
	/**
	 * Negates this money.
	 */
	public abstract IMoney negate();
	/**
	 * Subtracts a money from this money.
	 */
	public abstract IMoney subtract(IMoney m);
	/**
	 * Append this to a MoneyBag m.
	 */
	public abstract void appendTo(MoneyBag m);
}