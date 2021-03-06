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
package CH.ifa.draw.test.framework;

import java.awt.Point;

import junit.framework.TestCase;

// JUnitDoclet begin import
import CH.ifa.draw.figures.RectangleFigure;
import CH.ifa.draw.framework.FigureChangeEvent;
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
 * TestCase FigureChangeEventTest is generated by
 * JUnitDoclet to hold the tests for FigureChangeEvent.
 * @see CH.ifa.draw.framework.FigureChangeEvent
 */
// JUnitDoclet end javadoc_class
public class FigureChangeEventTest
// JUnitDoclet begin extends_implements
extends TestCase
// JUnitDoclet end extends_implements
{
	// JUnitDoclet begin class
	// instance variables, helper methods, ... put them in this marker
	private FigureChangeEvent figurechangeevent;
	// JUnitDoclet end class

	/**
	 * Constructor FigureChangeEventTest is
	 * basically calling the inherited constructor to
	 * initiate the TestCase for use by the Framework.
	 */
	public FigureChangeEventTest(String name) {
		// JUnitDoclet begin method FigureChangeEventTest
		super(name);
		// JUnitDoclet end method FigureChangeEventTest
	}

	/**
	 * Factory method for instances of the class to be tested.
	 */
	public CH.ifa.draw.framework.FigureChangeEvent createInstance() throws Exception {
		// JUnitDoclet begin method testcase.createInstance
		return new CH.ifa.draw.framework.FigureChangeEvent(new RectangleFigure(new Point(10, 10), new Point(100, 100)));
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
		figurechangeevent = createInstance();
		// JUnitDoclet end method testcase.setUp
	}

	/**
	 * Method tearDown is overwriting the framework method to
	 * clean up after each single test of this TestCase.
	 * It's called from the JUnit framework only.
	 */
	protected void tearDown() throws Exception {
		// JUnitDoclet begin method testcase.tearDown
		figurechangeevent = null;
		super.tearDown();
		// JUnitDoclet end method testcase.tearDown
	}

	// JUnitDoclet begin javadoc_method getFigure()
	/**
	 * Method testGetFigure is testing getFigure
	 * @see CH.ifa.draw.framework.FigureChangeEvent#getFigure()
	 */
	// JUnitDoclet end javadoc_method getFigure()
	public void testGetFigure() throws Exception {
		// JUnitDoclet begin method getFigure
		// JUnitDoclet end method getFigure
	}

	// JUnitDoclet begin javadoc_method getInvalidatedRectangle()
	/**
	 * Method testGetInvalidatedRectangle is testing getInvalidatedRectangle
	 * @see CH.ifa.draw.framework.FigureChangeEvent#getInvalidatedRectangle()
	 */
	// JUnitDoclet end javadoc_method getInvalidatedRectangle()
	public void testGetInvalidatedRectangle() throws Exception {
		// JUnitDoclet begin method getInvalidatedRectangle
		// JUnitDoclet end method getInvalidatedRectangle
	}

	// JUnitDoclet begin javadoc_method getNestedEvent()
	/**
	 * Method testGetNestedEvent is testing getNestedEvent
	 * @see CH.ifa.draw.framework.FigureChangeEvent#getNestedEvent()
	 */
	// JUnitDoclet end javadoc_method getNestedEvent()
	public void testGetNestedEvent() throws Exception {
		// JUnitDoclet begin method getNestedEvent
		// JUnitDoclet end method getNestedEvent
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
