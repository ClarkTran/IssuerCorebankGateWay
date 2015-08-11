/**
* Copyright (c) 2007-2008 Trans-Info Pte Ltd. Singapore. All Rights Reserved.
* This work contains trade secrets and confidential material of
* Trans-Info Pte Ltd. Singapore and its use of disclosure in whole
* or in part without express written permission of
* Trans-Info Pte Ltd. Singapore. is prohibited.
* Date of Creation   : Feb 25, 2008
* Version Number     : 1.0
*                   Modification History:
* Date          Version No.         Modified By           Modification Details.
*/

package com.transinfo.tplus;

import java.util.Comparator;

/**
*This class inherits the GregorianCalendar and have TPlus Specific functionalities.
*
*/

public class TPlusCalendarComparator implements Comparator
{

	/**
	* This method compares the two TPlusCalendars
	* returns zero if object1 = object2
	* returns -1 if object1 less than object2
	* returns 1 obj1 greater than obj2.
	* The comparison is made on the millisecond time stored for the date.
	*
	* @param o1 object1
	* @param o2 object2
	* @return 0 or 1 or 2
	* @throws ClassCastException if the input objects are not TPlusCalendar.
	*/
	public int compare(Object o1, Object o2)
	{

		TPlusCalendar cal1 = (TPlusCalendar) o1;
		TPlusCalendar cal2 = (TPlusCalendar) o2;

		long lng1 = cal1.getTimeInMillis();
		long lng2 = cal2.getTimeInMillis();

		int intReturn = 0;
		if (lng1 < lng2)
		{
			intReturn  = -1;
		}
		else
		{
			if (lng1 > lng2)
			{
				intReturn = 1;
			}
			else
			{
				intReturn = 0;
			}

		}
		return intReturn;
	}

	/**
	 * This is an empty implementation of the equals method.
	 * @returns true.
	 */
	public boolean equals(Object obj)
	{

		return true;
	}


}