/*
 * @(#)Test.java
 *
 * Project:		JHotdraw - a GUI framework for technical drawings
 *				http://www.jhotdraw.org
 *				http://jhotdraw.sourceforge.net
 * Copyright:	? by the original author(s) and all contributors
 * License:		Lesser GNU Public License (LGPL)
 *				http://www.opensource.org/licenses/lgpl-license.html
 */
package CH.ifa.draw.test.contrib;

import CH.ifa.draw.contrib.CTXCommandMenu;
import junit.framework.TestCase;
// JUnitDoclet begin import
// JUnitDoclet end import

/*
 * Generated by JUnitDoclet, a tool provided by
 * ObjectFab GmbH under LGPL.
 * Please see www.junitdoclet.org, www.gnu.org
 * and www.objectfab.de for informations about
 * the tool, the licence and the authors.
 */

// JUnitDoclet begin javadoc_class
/**
 * TestCase CTXCommandMenuTest is generated by
 * JUnitDoclet to hold the tests for CTXCommandMenu.
 * @see CH.ifa.draw.contrib.CTXCommandMenu
 */
// JUnitDoclet end javadoc_class
public class CTXCommandMenuTest
// JUnitDoclet begin extends_implements
extends TestCase
// JUnitDoclet end extends_implements
{
	// JUnitDoclet begin class
	// instance variables, helper methods, ... put them in this marker
	private CTXCommandMenu ctxcommandmenu;
	// JUnitDoclet end class

	/**
	 * Constructor CTXCommandMenuTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
	public CTXCommandMenuTest(String name) {
		// JUnitDoclet begin method CTXCommandMenuTest
		super(name);
		// JUnitDoclet end method CTXCommandMenuTest
	}

	/**
	 * Factory method for instances of the class to be tested.
	 */
	public CH.ifa.draw.contrib.CTXCommandMenu createInstance() throws Exception {
		// JUnitDoclet begin method testcase.createInstance
		return new CH.ifa.draw.contrib.CTXCommandMenu("TestCTXCommandMenu");
		// JUnitDoclet end method testcase.createInstance
	}

	/**
	 * Method setUp is overwriting the framework method to
	 * prepare an instance of this TestCase for a single test.
	 * It's called from the JUnit framework only.
	 */
	protected void setUp() throws Exception {
		// JUnitDoclet begin method testcase.setUp
		super.setUp();
		ctxcommandmenu = createInstance();
		// JUnitDoclet end method testcase.setUp
	}

	/**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
	protected void tearDown() throws Exception {
		// JUnitDoclet begin method testcase.tearDown
		ctxcommandmenu = null;
		super.tearDown();
		// JUnitDoclet end method testcase.tearDown
	}

	// JUnitDoclet begin javadoc_method add()
	/**
	 * Method testAdd is testing add
	 * @see CH.ifa.draw.contrib.CTXCommandMenu#add(CH.ifa.draw.util.Command)
	 */
	// JUnitDoclet end javadoc_method add()
	public void testAdd() throws Exception {
		// JUnitDoclet begin method add
		// JUnitDoclet end method add
	}

	// JUnitDoclet begin javadoc_method addCheckItem()
	/**
	 * Method testAddCheckItem is testing addCheckItem
	 * @see CH.ifa.draw.contrib.CTXCommandMenu#addCheckItem(CH.ifa.draw.util.Command)
	 */
	// JUnitDoclet end javadoc_method addCheckItem()
	public void testAddCheckItem() throws Exception {
		// JUnitDoclet begin method addCheckItem
		// JUnitDoclet end method addCheckItem
	}

	// JUnitDoclet begin javadoc_method remove()
	/**
	 * Method testRemove is testing remove
	 * @see CH.ifa.draw.contrib.CTXCommandMenu#remove(CH.ifa.draw.util.Command)
	 */
	// JUnitDoclet end javadoc_method remove()
	public void testRemove() throws Exception {
		// JUnitDoclet begin method remove
		// JUnitDoclet end method remove
	}

	// JUnitDoclet begin javadoc_method enable()
	/**
	 * Method testEnable is testing enable
	 * @see CH.ifa.draw.contrib.CTXCommandMenu#enable(java.lang.String, boolean)
	 */
	// JUnitDoclet end javadoc_method enable()
	public void testEnable() throws Exception {
		// JUnitDoclet begin method enable
		// JUnitDoclet end method enable
	}

	// JUnitDoclet begin javadoc_method checkEnabled()
	/**
	 * Method testCheckEnabled is testing checkEnabled
	 * @see CH.ifa.draw.contrib.CTXCommandMenu#checkEnabled()
	 */
	// JUnitDoclet end javadoc_method checkEnabled()
	public void testCheckEnabled() throws Exception {
		// JUnitDoclet begin method checkEnabled
		// JUnitDoclet end method checkEnabled
	}

	// JUnitDoclet begin javadoc_method actionPerformed()
	/**
	 * Method testActionPerformed is testing actionPerformed
	 * @see CH.ifa.draw.contrib.CTXCommandMenu#actionPerformed(java.awt.event.ActionEvent)
	 */
	// JUnitDoclet end javadoc_method actionPerformed()
	public void testActionPerformed() throws Exception {
		// JUnitDoclet begin method actionPerformed
		// JUnitDoclet end method actionPerformed
	}

	// JUnitDoclet begin javadoc_method commandExecuted()
	/**
	 * Method testCommandExecuted is testing commandExecuted
	 * @see CH.ifa.draw.contrib.CTXCommandMenu#commandExecuted(java.util.EventObject)
	 */
	// JUnitDoclet end javadoc_method commandExecuted()
	public void testCommandExecuted() throws Exception {
		// JUnitDoclet begin method commandExecuted
		// JUnitDoclet end method commandExecuted
	}

	// JUnitDoclet begin javadoc_method commandExecutable()
	/**
	 * Method testCommandExecutable is testing commandExecutable
	 * @see CH.ifa.draw.contrib.CTXCommandMenu#commandExecutable(java.util.EventObject)
	 */
	// JUnitDoclet end javadoc_method commandExecutable()
	public void testCommandExecutable() throws Exception {
		// JUnitDoclet begin method commandExecutable
		// JUnitDoclet end method commandExecutable
	}

	// JUnitDoclet begin javadoc_method commandNotExecutable()
	/**
	 * Method testCommandNotExecutable is testing commandNotExecutable
	 * @see CH.ifa.draw.contrib.CTXCommandMenu#commandNotExecutable(java.util.EventObject)
	 */
	// JUnitDoclet end javadoc_method commandNotExecutable()
	public void testCommandNotExecutable() throws Exception {
		// JUnitDoclet begin method commandNotExecutable
		// JUnitDoclet end method commandNotExecutable
	}

	// JUnitDoclet begin javadoc_method testVault
	/**
	 * JUnitDoclet moves marker to this method, if there is not match
	 * for them in the regenerated code and if the marker is not empty.
	 * This way, no test gets lost when regenerating after renaming.
	 * <b>Method testVault is supposed to be empty.</b>
	 */
	// JUnitDoclet end javadoc_method testVault
	public void testVault() throws Exception {
		// JUnitDoclet begin method testcase.testVault
		// JUnitDoclet end method testcase.testVault
	}
}
