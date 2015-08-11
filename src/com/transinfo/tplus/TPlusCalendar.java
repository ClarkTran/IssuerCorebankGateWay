
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.transinfo.tplus.db.TransactionDB;
//import java.sql.*;
/**
*This class inherits the GregorianCalendar and have TPlus Specific functionalities.
*
*/

public class TPlusCalendar extends GregorianCalendar
{

public static void main(String s[])
{
	//TPlusCalendar tcalendar = new TPlusCalendar();
	//System.out.println(tcalendar.getTransmissionTime());
long ltime = System.currentTimeMillis();
System.out.println(new Date(ltime));
System.out.println(getJulianDate());

	/*Calendar cal = new GregorianCalendar();
		//cal.set(Calendar.HOUR_OF_DAY,8);
	    // Get the components of the time
	    Calendar japanCal = new GregorianCalendar(TimeZone.getTimeZone("GMT+08.00"));
		    japanCal.setTimeInMillis(cal.getTimeInMillis());

		    // Get the foreign time
		    int hour = japanCal.get(Calendar.HOUR);            // 3
		    int min = japanCal.get(Calendar.MINUTE);       // 0
		    int sec = japanCal.get(Calendar.SECOND);       // 0
		    boolean am = japanCal.get(Calendar.AM_PM) == Calendar.AM; //true

System.out.println(hour+" "+"  "+min+" "+sec);*/

}

	/**
	 * This method sets the time with current day with the time specified.
	 * @param intHour It will set the HOUR_OF_DAY for the calendar.
	 * @param intHour It will set the MINIUTE for the calendar.
	 * @param intHour It will set the SECOND for the calendar.
	 */
	public void setTime(int intHour, int intMinute,int intSecond)
	{
		super.set(GregorianCalendar.HOUR_OF_DAY,intHour);

		super.set(GregorianCalendar.MINUTE,intMinute);
		super.set(GregorianCalendar.SECOND,intSecond);

		super.set(GregorianCalendar.MILLISECOND,0);

	}

	/**
	 * This method sets the time with current day with the time specified.
	 * @param intHour It will set the HOUR_OF_DAY for the calendar.
	 * @param intHour It will set the MINIUTE for the calendar.
	 * @param intHour It will set the SECOND for the calendar.
	 */
	public void setTime(java.util.Calendar cal)
	{
		super.setTime(cal.getTime());
	}
	/**
	 * This method returns the time in miilliseconds.
	 */
	public long getTimeInMillis()
	{
		 return super.getTimeInMillis();
	}
	/**
	 * This method sets the time to input millisecond.
	 */
	public void setTimeInMillis(long lngTime)
	{
		super.setTimeInMillis(lngTime);
	}
	/**
	 * This will return the date in the format specified
	 */

	public String getFormatedDate()
	{

		SimpleDateFormat sdfDate = new SimpleDateFormat(TPlusConfig.strDateFormat);
		Date dt = super.getTime();
		return	sdfDate.format(dt);
	}


	/**
	 * This will return the transmission Date Time in the format specified
	 */

	public String getTransmissionDateTime()
	{
		String strDatetime =null;
		//SimpleDateFormat sdfDate = new SimpleDateFormat("MMddHHmmss");
		//Date dt = new Date();
		//return	sdfDate.format(dt);
		try
		{
			TransactionDB tranxDB = new TransactionDB();
			strDatetime =tranxDB.getDBDateTime();
			if(strDatetime == null)
			{

				SimpleDateFormat sdfDate = new SimpleDateFormat("MMddHHmmss");
				Date dt = new Date();
				strDatetime =	sdfDate.format(dt);
		 	}
	 	}
	 	catch(Exception exp){System.out.println(exp);}

	 	return strDatetime;
	}


	/**
	 * This will return the transmission time in the format specified
	 */

	public String getTransmissionTime()
	{

		SimpleDateFormat sdfDate = new SimpleDateFormat("HHmmss");
		Date dt = super.getTime();
		return	sdfDate.format(dt);
	}


	/**
	 * This will return the transmission Date in the format specified
	 */

	 public String getTransmissionDate()
	{

		SimpleDateFormat sdfDate = new SimpleDateFormat("MMdd");
		Date dt = super.getTime();
		return	sdfDate.format(dt);
	}

	/**
	 * This will return the transaction Date in the format specified
	 */

	public String getTransactionDate()
	{

		SimpleDateFormat sdfDate = new SimpleDateFormat("yyMMdd");
		Date dt = super.getTime();
		return	sdfDate.format(dt);
	}

	/**
	 * This will return the transaction time in the format specified
	 */
	public String getTransactionTime()
	{

			SimpleDateFormat sdfDate = new SimpleDateFormat("HHmmss");
			Date dt = super.getTime();
			return	sdfDate.format(dt);
	}


	/**
	 * This will return the Ref No in the format specified
	 */
	public String getRefNo()
	{

			Calendar valCal = Calendar.getInstance();
			int y = valCal.get(Calendar.YEAR) % 10;
			int dy = valCal.get(Calendar.DAY_OF_YEAR) ;
			int h = valCal.get(Calendar.HOUR_OF_DAY) ;

			return ""+y+dy+h;

	}


	/**
	 * This method validate the input string is a valid date and time.
	 * @param String should be in format YYYYMMDDHHmmSS
	 *									 MM should be in 00-12
	 *                                   HH should be in 00-23
	 *                                   mm should be in 00-59
	 *                                   SS should be in 00-59
	 * @returns boolean - true if the date is  valid. else false.
	 */
	public static boolean validateDateAndTime(String strInput)
	{

		try
		{
			if (strInput.length() > 14)
			{
				return false;
			}

			int intYear = Integer.parseInt(strInput.substring(0,4));
			int intMonth = Integer.parseInt(strInput.substring(4,6));
			intMonth = intMonth - 1;
			int intDay = Integer.parseInt(strInput.substring(6,8));
			int intHour = Integer.parseInt(strInput.substring(8,10));
			int intMin = Integer.parseInt(strInput.substring(10,12));
			int intSec = Integer.parseInt(strInput.substring(12,14));
			Calendar valCal = Calendar.getInstance();
			valCal.clear();
			valCal.set(intYear,intMonth,intDay,intHour,intMin,intSec);

			if (intYear != valCal.get(Calendar.YEAR) ||
				intMonth != valCal.get(Calendar.MONTH) ||
					intDay != valCal.get(Calendar.DAY_OF_MONTH) ||
						intHour != valCal.get(Calendar.HOUR_OF_DAY) ||
							intMin != valCal.get(Calendar.MINUTE) ||
									intSec != valCal.get(Calendar.SECOND))
			{
				return false;
			}
		}catch (Exception e)
		{
			//throw new Exception(" Invalid  Date/Time. " + strInput + " Exception is " + e.toString());
			return false;
		}

		return true;
	}

	/**
	* This method validate the input string is a valid date and time.
	* @param String should be in format YYYYMMDD HH:mm:SS
	*									MM should be in 00-12
	*                                   HH should be in 00-23
	*                                   mm should be in 00-59
	*                                   SS should be in 00-59
	* @returns boolean - true if the date is  valid. else false.
	*/
	public static boolean validateTransactionDate(String strInput)
	{

		try
		{
			if (strInput.length() > 17)
			{
				return false;
			}
			int intYear = Integer.parseInt(strInput.substring(0,4));
			int intMonth = Integer.parseInt(strInput.substring(4,6));
			intMonth = intMonth - 1;
			int intDay = Integer.parseInt(strInput.substring(6,8));
			int intHour = Integer.parseInt(strInput.substring(9,11));
			int intMin = Integer.parseInt(strInput.substring(12,14));
			int intSec = Integer.parseInt(strInput.substring(15,17));
			Calendar valCal = Calendar.getInstance();
			valCal.clear();
			valCal.set(intYear,intMonth,intDay,intHour,intMin,intSec);

			if (intYear != valCal.get(Calendar.YEAR) ||
				intMonth != valCal.get(Calendar.MONTH) ||
					intDay != valCal.get(Calendar.DAY_OF_MONTH) ||
						intHour != valCal.get(Calendar.HOUR_OF_DAY) ||
							intMin != valCal.get(Calendar.MINUTE) ||
								intSec != valCal.get(Calendar.SECOND))
			{
				return false;
			}
		}catch (Exception e)
		{
			//throw new Exception(" Invalid  Date/Time. " + strInput + " Exception is " + e.toString());
			return false;
		}
		return true;
	}
	/**
	* This method validate the input string is a valid year and month
	* @param String should be in format YYMM
	*									MM should be in 00-12
	* @returns boolean - true if the date is  valid. else false.
	*/
	public static boolean validateExpiry(String strInput)
	{
		try
		{
			strInput = strInput.trim();
			if (strInput.length() != 4 )
			{
				return false;
			}
			int intYear		= Integer.parseInt(strInput.substring(0,2));
			int intMonth	= Integer.parseInt(strInput.substring(2,4));
			intMonth		= intMonth - 1;

			Calendar valCal = Calendar.getInstance();
			valCal.clear();
			valCal.set(intYear,intMonth,01);

			if (intYear != valCal.get(Calendar.YEAR) ||
					intMonth != valCal.get(Calendar.MONTH))
			{
				 return false;
			}
		}catch(Exception e)
		{
				return false;
		}
		return true;
	}

	//Added by Sock Hoon
	/**
	* This method validate the input string is not before the present year and month
	* @param String should be in format YYMM
	*									MM should be in 00-12
	* @returns boolean - true if the card is not expired. else false.
	*/
	public static boolean checkCardExpired(String strInput)
	{
		try
		{

			strInput = strInput.trim();
			if (strInput.length() != 4 )
			{
				return false;
			}
			int intYear		= Integer.parseInt(strInput.substring(0,2));
			int intMonth	= Integer.parseInt(strInput.substring(2,4));

			Calendar valCal = Calendar.getInstance();

			if (intYear < (valCal.get(Calendar.YEAR)-2000))
			{
				 return false;
			}
			if (intYear == (valCal.get(Calendar.YEAR)-2000))
			{
				if (intMonth < (valCal.get(Calendar.MONTH)+1))
				{
						return false;
				}
			}

		}catch(Exception e)
		{
				return false;
		}
		return true;
	}

	//EOC



	/**
	* This method validate the input string is a valid year and month
	* @param String should be in format YYYYMMDD
	*									MM should be in 00-12
	* @returns boolean - true if the date is  valid. else false.
	*/
	public static boolean validateDate(String strInput)
	{
		try
		{
			strInput = strInput.trim();
			if (strInput.length() != 8 )
			{
				return false;
			}
			int intYear		= Integer.parseInt(strInput.substring(0,4));
			int intMonth	= Integer.parseInt(strInput.substring(4,6));
			intMonth		= intMonth - 1;
			int intDay		= Integer.parseInt(strInput.substring(6,8));

			Calendar valCal = Calendar.getInstance();
			valCal.clear();
			valCal.set(intYear,intMonth,intDay);

			if (intYear != valCal.get(Calendar.YEAR) ||
				intMonth != valCal.get(Calendar.MONTH) ||
				 intDay != valCal.get(Calendar.DAY_OF_MONTH))
			{
				return false;
			}
		}catch(Exception e)
		{
			return false;
		}
		return true;
	}


public static  String getGMTTime()
  {
    Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
    //Gregorian Calendar defines Jan as 0, Feb as 1 etc... That's why we have
    //to add 1 to the returned month to get the actual value
    String result = lpad(cal.get(Calendar.MONTH)+1) + lpad(cal.get(Calendar.DATE)) +
                    lpad(cal.get(Calendar.HOUR)) + lpad(cal.get(Calendar.MINUTE)) +
                    lpad(cal.get(Calendar.SECOND));
    return result;
  }


public static long  getJulianDate()
{

	GregorianCalendar cal = new GregorianCalendar();
	cal.setGregorianChange(new Date(Long.MAX_VALUE)); // setting the calendar to act as a pure Julian calendar.

	Date todayJD = cal.getTime(); // getting the calculated time of today's Julian date
	SimpleDateFormat sdfJulianDate = new SimpleDateFormat("yyDDDHHmmss");
	SimpleDateFormat sdfJuliandayOfYear = new SimpleDateFormat("DDD");

	return Long.parseLong(sdfJulianDate.format(todayJD));



}



  private static String lpad(int i)
  {
    if (i<10)
      return "0"+i;
    return ""+i;
  }





}