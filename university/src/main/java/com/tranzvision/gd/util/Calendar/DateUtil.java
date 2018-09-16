package com.tranzvision.gd.util.Calendar;

import java.util.Date;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 时间的格式转换
 * 
 * @author CAOY
 *
 */
public class DateUtil {

	public DateUtil() {

	}

	public static Date geLastWeekMonday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getThisWeekMonday(date));
		cal.add(Calendar.DATE, -7);
		return cal.getTime();
	}

	/**
	 * 本周开始时间
	 * 
	 * @param formatString
	 * @return
	 */
	public static String getWeekStartTime(String formatString) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
		Calendar cal = Calendar.getInstance();
		int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0) {
			day_of_week = 7;
		}
		cal.add(Calendar.DATE, -day_of_week + 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return simpleDateFormat.format(cal.getTime());
	}
	
	/**
	 * 指定时间加一天返回
	 * @param time
	 * @param formatString
	 * @return
	 */
	public static String getAddOneDay(String time, String formatString) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
		Date d = parse(time);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, 1);
		return simpleDateFormat.format(cal.getTime());
	}

	/**
	 * end 本周结束时间戳 - 以星期一为本周的第一天
	 */
	public static String getWeekEndTime(String formatString) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
		Calendar cal = Calendar.getInstance();
		int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0) {
			day_of_week = 7;
		}
		cal.add(Calendar.DATE, -day_of_week + 7);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return simpleDateFormat.format(cal.getTime());
	}

	/**
	 * 下周开始时间
	 * 
	 * @param formatString
	 * @return
	 */
	public static String getNextWeekStartTime(String formatString) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
		Calendar cal = Calendar.getInstance();
		cal.setTime(getNextWeekMonday(new java.util.Date()));
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return simpleDateFormat.format(cal.getTime());
	}

	/**
	 * 下周结束时间
	 * 
	 * @param formatString
	 * @return
	 */
	public static String getNextWeekEndTime(String formatString) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString);
		Calendar cal = Calendar.getInstance();
		cal.setTime(getNextWeekEndTime());
		return simpleDateFormat.format(cal.getTime());
	}

	public static Date getNextWeekEndTime() {
		// SimpleDateFormat simpleDateFormat = new
		// SimpleDateFormat(formatString);
		Calendar cal = Calendar.getInstance();
		int day_of_week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (day_of_week == 0) {
			day_of_week = 7;
		}
		cal.add(Calendar.DATE, -day_of_week + 7);
		cal.add(Calendar.DATE, 7);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	public static Date getThisWeekMonday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 获得当前日期是一个星期的第几天
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		// 获得当前日期是一个星期的第几天
		int day = cal.get(Calendar.DAY_OF_WEEK);
		// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
		return cal.getTime();
	}

	public static Date getNextWeekMonday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(getThisWeekMonday(date));
		cal.add(Calendar.DATE, 7);
		return cal.getTime();
	}

	/**
	 * dataString: 2002-10-11, 2002/12/12, 2002.12.12
	 * 
	 * @param dateString
	 * @author: CAOY
	 * @date: 2002-10-20
	 */
	public static java.util.Date parse(String dateString) {
		java.util.StringTokenizer s = new java.util.StringTokenizer(dateString, "-,/,.");

		if (s.countTokens() == 3) {
			int year = Integer.parseInt(s.nextToken());
			int month = Integer.parseInt(s.nextToken()) - 1;
			int day = Integer.parseInt(s.nextToken());

			java.util.Calendar c = java.util.Calendar.getInstance();
			c.set(year, month, day, 0, 0, 0);
			java.util.Date d = c.getTime();
			c = null;
			return d;
		}
		return null;
	}

	/**
	 * dataString: 2002-10-11 12:12:12, 2002/12/12 12:12:12, 2002.12.12 12:12:12
	 * 
	 * @param dateString
	 * @author: Li.Wang
	 * @date: 2002-10-20
	 */
	public static java.util.Date parseTimeStamp(String dateString) {
		java.util.StringTokenizer ss = new java.util.StringTokenizer(dateString, " ");
		if (ss.countTokens() == 2) {
			java.util.StringTokenizer s = new java.util.StringTokenizer(ss.nextToken(), "-,/,.");
			java.util.StringTokenizer _s = new java.util.StringTokenizer(ss.nextToken(), ":");
			if (s.countTokens() == 3 && _s.countTokens() == 3) {
				int year = Integer.parseInt(s.nextToken());
				int month = Integer.parseInt(s.nextToken()) - 1;
				int day = Integer.parseInt(s.nextToken());

				int hour = Integer.parseInt(_s.nextToken());
				int minute = Integer.parseInt(_s.nextToken());
				int second = Integer.parseInt(_s.nextToken());

				java.util.Calendar c = java.util.Calendar.getInstance();
				c.set(year, month, day, hour, minute, second);
				java.util.Date d = c.getTime();
				c = null;
				return d;
			}
		}
		return null;
	}

	/**
	 * parseDate(String)
	 * 
	 * @param dataString
	 *            MUST be "19990101" formal
	 * @return java.util.Date
	 */
	public static java.util.Date parseDate(String dateString) {
		if (dateString.length() != 8)
			return null;

		int year = Integer.parseInt(dateString.substring(0, 4));
		// Debug.p("year=" + year);
		int month = Integer.parseInt(dateString.substring(4, 6));
		// Debug.p("month=" + month);
		int date = Integer.parseInt(dateString.substring(6, 8));
		// Debug.p("date=" + date);
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.set(year, month - 1, date, 0, 0, 0);
		return c.getTime();
	}

	/**
	 * ISOSECDateString
	 * 
	 * @param java.util.Date
	 *            instance
	 * @return String such as: 2002-10-11
	 */
	public static String ISOSECDateString(java.util.Date d) {
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		StringBuffer dateStringBuffer = new StringBuffer();
		dateStringBuffer.append(c.get(Calendar.YEAR)).append("-").append(c.get(Calendar.MONTH) + 1).append("-")
				.append(c.get(Calendar.DATE));
		c.clear();
		c = null;
		return dateStringBuffer.toString();
	}

	/**
	 * formatDate
	 * 
	 * @param d
	 *            java.util.Date instance
	 * @param formatString
	 *            such as : yyyy-MM-dd hh:mm:ss, yyyy-MM-dd
	 * @return formatedDateString
	 */
	public static String formatDate(java.util.Date d, String formatString) {
		if (d == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		StringBuffer sb = sdf.format(d, new StringBuffer(), new FieldPosition(0));
		return sb.toString();
	}

	public static String formatLongDate(java.util.Date date) {
		return formatDate(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String formatShortDate(java.util.Date date) {
		return formatDate(date, "yyyyMMdd");
	}

	public static String getYear(java.util.Date date) {
		return formatShortDate(date).substring(0, 4);
	}

	public static String getMonth(java.util.Date date) {
		return formatShortDate(date).substring(4, 6);
	}

	public static String getDay(java.util.Date date) {
		return formatShortDate(date).substring(6, 8);
	}

	public static void main(String[] args) {
		String a = "2018-09-16 09:00:01";
		System.out.println(a.substring(0, 16));
		System.out.println("----begin----");
		// try {
		// java.lang.Thread.sleep(100000);
		// } catch (InterruptedException ex) {
		// }
		System.out.println("----end----");
		String v = "yyyy-MM-dd";
		System.out.println(getWeekStartTime(v));
		System.out.println(getWeekEndTime(v));
		System.out.println(getNextWeekStartTime(v));
		System.out.println(getAddOneDay("2018-09-30",v));

		StringBuffer selectDate = new StringBuffer();
		// 首先计算结束日
		Calendar cd = Calendar.getInstance();
		Date lastDate = DateUtil.getNextWeekEndTime();
		cd.setTime(lastDate);
		int year = cd.get(Calendar.YEAR);
		int month = cd.get(Calendar.MONTH);
		int day = cd.get(Calendar.DAY_OF_MONTH);

		int _year = 0;
		int _month = 0;
		int _day = 0;

		Date now = new Date();
		String strDate = "";
		do {
			cd.setTime(now);
			cd.add(Calendar.DATE, 1);
			_year = cd.get(Calendar.YEAR);
			_month = cd.get(Calendar.MONTH);
			_day = cd.get(Calendar.DAY_OF_MONTH);
			now = cd.getTime();
			strDate = DateUtil.ISOSECDateString(now);
			selectDate.append("<option value =\"" + strDate + "\">" + strDate + "</option>");
			selectDate.append("\n");
		} while (!(_year == year && _month == month && day == _day));
		System.out.println(selectDate.toString());
		// System.out.println(getWeekStartTime(a));
		// System.out.println(formatLongDate(new
		// java.util.Date(System.currentTimeMillis())));
		// System.out.println(Date.parse("2002-10-10"));
		// System.out.println(Date.parse("2002.11.12"));
		// System.out.println(Date.parse("2003/12/12"));

	}
}
