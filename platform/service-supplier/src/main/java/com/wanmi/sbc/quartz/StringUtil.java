package com.wanmi.sbc.quartz;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;


/**
 * @author chenzhen
 */
public class StringUtil {

	/**
	 * 得到yyyy-MM-dd HH:mm:ss<br>
	 * 例如：return 2007-05:03 25:03:19<br>
	 */
	public static String getCurrentAllDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 小写的hh取得12小时，大写的HH取的是24小时
		Date date = new Date();
		return df.format(date);
	}

	/**
	 * yyyy-MM-dd HH:mm:ss 天数+-
	 * @return
	 */
	public static String dayDiff(String datetime,int days){

		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try
		{
			date = format.parse(datetime);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
		if (date==null) return "";
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH,days);
		date=cal.getTime();
		//System.out.println("3 days after(or before) is "+format.format(date));
		cal=null;
		return format.format(date);
	}

	public static boolean isBlank(String strIn) {
		if ((strIn == null) || (strIn.trim().equals("")) || (strIn.toLowerCase().equals("null")) || strIn.length() == 0) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isNotBlank(String strIn) {
		if ((strIn == null) || (strIn.trim().equals("")) || (strIn.toLowerCase().equals("null")) || strIn.length() == 0) {
			return false;
		} else {
			return true;
		}
	}

}