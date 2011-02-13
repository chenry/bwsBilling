/*
 * Created on Oct 4, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.hs.bws.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * @author flanderb
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SortTableUtilComparator implements Comparator {
	private int columnIndex;
	private int currCompareType = -1;
	public static final int COMPARE_TYPE_STRING = -1;
	public static final int COMPARE_TYPE_DOUBLE = 100;
	public static final int COMPARE_TYPE_DATE = 200;
	public SortTableUtilComparator(int columnIndex) {
		this.columnIndex = columnIndex;
	}
	/**
	 * @param i
	 * @param integer2
	 */
	public SortTableUtilComparator(int i, int integer2) {
		columnIndex = i;
		currCompareType = integer2;
	}
	/**
	 * By default will do a string comparison, unless specified
	 * otherwise through the constructor
	 */
	public int compare(Object arg0, Object arg1) {
		int retValue = 0;
		String[] row1 = (String[]) arg0;
		String[] row2 = (String[]) arg1;
		if (currCompareType > 0) {
			if (COMPARE_TYPE_DOUBLE == currCompareType) {
				Double row1Double = new Double(row1[columnIndex]);
				Double row2Double = new Double(row2[columnIndex]);
				retValue = row1Double.compareTo(row2Double);
			} else if (COMPARE_TYPE_DATE == currCompareType) {
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				Date row1Date = sdf.parse(row1[columnIndex], new ParsePosition(0));
				Date row2Date = sdf.parse(row2[columnIndex], new ParsePosition(0));
				retValue = row1Date.compareTo(row2Date);
			}
		} else {
			retValue = row1[columnIndex].compareTo(row2[columnIndex]);
		}
		return retValue;
	}

}
