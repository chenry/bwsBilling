/*
 * Created on Oct 4, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.hs.bws.util;

import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

/**
 * @author flanderb
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SortTableUtilComparatorTest extends TestCase {
	Logger logger = Logger.getLogger(SortTableUtilComparatorTest.class);
	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Constructor for SortTableUtilComparatorTest.
	 * @param arg0
	 */
	public SortTableUtilComparatorTest(String arg0) {
		super(arg0);
	}
	
	public void testSortData() {
		String[][] data = new String[3][4];
		/*
		 * henry,carlus,26,10/27/1977
		 * flanders,ben,28,3/3/1976
		 * sova,adam,20 ,4/1/1978
		 */
		data[0][0] = "henry";
		data[0][1] = "carlus";
		data[0][2] = "26";
		data[0][3] = "10/27/1977";

		data[1][0] = "flanders";
		data[1][1] = "ben";
		data[1][2] = "100";
		data[1][3] = "3/3/1976";

		data[2][0] = "sova";
		data[2][1] = "adam";
		data[2][2] = "24";
		data[2][3] = "4/1/1978";
		
		Arrays.sort(data, new SortTableUtilComparator(0));
		
		assertEquals("flanders", data[0][0]);
		assertEquals("henry", data[1][0]);
		assertEquals("sova", data[2][0]);
		
		Arrays.sort(data, new SortTableUtilComparator(1));
		assertEquals("adam", data[0][1]);
		assertEquals("ben", data[1][1]);
		assertEquals("carlus", data[2][1]);

		Arrays.sort(data, new SortTableUtilComparator(2, SortTableUtilComparator.COMPARE_TYPE_DOUBLE));
		assertEquals("adam", data[0][1]);
		assertEquals("carlus", data[1][1]);
		assertEquals("ben", data[2][1]);

		Arrays.sort(data, new SortTableUtilComparator(3, SortTableUtilComparator.COMPARE_TYPE_DATE));
		assertEquals("ben", data[0][1]);
		assertEquals("carlus", data[1][1]);
		assertEquals("adam", data[2][1]);
	}

}
