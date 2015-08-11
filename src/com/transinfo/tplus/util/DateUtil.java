package com.transinfo.tplus.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil extends Object{

	public static int checkCompDate(String str1 , String str2, String format) {
		int coud= 0;
		boolean f1=checkDate(str1,format);
		boolean f2=checkDate(str2,format);
		if (f1==true && f2==true){
			try{
				SimpleDateFormat df = new SimpleDateFormat(format); //formate = "dd-MM-yyyy"
						df.setLenient(false);		 // this is important!
						java.util.Date variable1 = df.parse(str1);
						java.util.Date variable2 = df.parse(str2);

						int result = variable1.compareTo(variable2);
						if ( result < 0 ) {
							coud = 0;
						} else {
							if ( result > 0 ) {
								coud = 1;   //end date must be greater than start date
							} else {
								coud = 0;
							}
						}
			}catch (ParseException e){
				coud = 1; //end date must be greater than start date
			}
		}else {
			coud=2;  //invalid Date
		}
		return coud;
	}

	public static boolean checkDate(String str,String format){
		String  dt=str;
		try {
			SimpleDateFormat df = new SimpleDateFormat(format); //format = "dd-MM-yyyy"
			df.setLenient(false);		 // this is important!
			java.util.Date dt2 = df.parse(dt);
			return true;
		} catch (ParseException e) {
			return false;
		} catch (IllegalArgumentException e) {
			return false;
		}
	} // end of checkdate

	public static int getDayOfWeek(String date,String format) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date d = sdf.parse(date);
			Calendar c = Calendar.getInstance();
			c.setTime(d);
			return c.get(Calendar.DAY_OF_WEEK);
		}
		catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	static final long ONE_HOUR = 60 * 60 * 1000L;
	//usage :: System.out.println(daysBetween("12/01/2004","16/01/2004","dd/MM/yyyy"));
	public static long daysBetween(String startDate, String endDate, String format){
		long diffDays = 0;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date d1 = sdf.parse(startDate);
			Date d2 = sdf.parse(endDate);
			diffDays = ((d2.getTime() - d1.getTime() + ONE_HOUR) / (ONE_HOUR * 24));
		}
		catch(Exception e) {
			e.printStackTrace();
			return -1;
		}
		return diffDays;
	}
	

	public static void main(String[] args) {
		
		/*String startDate = "11/06/2014";
		String endDate = "13/06/2014";
		String format = "dd/MM/yyyy";
		
		System.out.println(daysBetween(startDate, endDate, format));*/
		
		System.out.println(getFormattedDate());
		
		
	}

	//usage :: System.out.println(daysAddSub("28/01/2004",30,"dd/MM/yyyy"));
	public static String daysAddSub(String strDate,int noOfDays, String format){
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(format);
		Calendar c1 = Calendar.getInstance();
		String optDate = "";
		try{
			Date d1 = sdf.parse(strDate);
			c1.setTime(d1);
			System.out.println("Date is : " + sdf.format(c1.getTime()));
			c1.add(Calendar.DATE,noOfDays);
			System.out.println("Date + "+noOfDays+" days is : " + sdf.format(c1.getTime()));
			optDate = sdf.format(c1.getTime());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return optDate;
	}

	public static String convDate(String str) {
		if (str == null || str.trim().equals("")){
			str = "''";
			return str;
		}else {
			str = "to_date('"+str+"','dd-mm-yyyy')";
			return str;
		}
	}

	/**
	 * This method returns the current date and time.
	 * @param dateFormat Format of the date.
	 * @return Formated date
	 */

	public static String getDateValue(String dateFormat){
		String rtnCode = "";
		String padValue = "0";
		java.util.GregorianCalendar gc = new java.util.GregorianCalendar();
		String day = StringUtil.LPAD(String.valueOf(gc.get(java.util.Calendar.DAY_OF_MONTH)),2,padValue);
		String mth = StringUtil.LPAD(String.valueOf(gc.get(java.util.Calendar.MONTH) + 1),2,padValue);
		String yr = StringUtil.LPAD(String.valueOf(gc.get(java.util.Calendar.YEAR)),4,padValue);
		String hr = StringUtil.LPAD(String.valueOf(gc.get(java.util.Calendar.HOUR_OF_DAY)),2,padValue);
		String min = StringUtil.LPAD(String.valueOf(gc.get(java.util.Calendar.MINUTE)),2,padValue);
		String sec = StringUtil.LPAD(String.valueOf(gc.get(java.util.Calendar.SECOND)),2,padValue);
		if(dateFormat.trim().equals("DD")) rtnCode = day;
		if(dateFormat.trim().equals("MM")) rtnCode = mth;
		if(dateFormat.trim().equals("YYYY")) rtnCode = yr;
		if(dateFormat.trim().equals("HH")) rtnCode = hr;
		if(dateFormat.trim().equals("MI")) rtnCode = min;
		if(dateFormat.trim().equals("SS")) rtnCode = sec;
		if(dateFormat.trim().equals("YYYYMMDD")) rtnCode = yr+mth+day;
		if(dateFormat.trim().equals("HHMISS")) rtnCode = hr+min+sec;
		if(dateFormat.trim().equals("DD/MM/YYYY HH:MI:SS")) rtnCode = day + "/" + mth + "/" + yr + " " + hr + ":" + min + ":" + sec;
		return rtnCode;
	}

	public static String getYesterdayDate(){

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		cal.add(Calendar.DATE, -1);
		
		DateFormat dfDateOnly = new SimpleDateFormat("dd/MM/yyyy");

		return dfDateOnly.format(cal.getTime());

	}

	public static String getTodayDate(){

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		DateFormat dfDateOnly = new SimpleDateFormat("dd/MM/yyyy");

		return dfDateOnly.format(cal.getTime());

	}

	public static String getFormattedDate(){

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		
		DateFormat dfDateOnly = new SimpleDateFormat("ddMMyyyyHHmmss");

		return dfDateOnly.format(cal.getTime());

	}
	
	

}