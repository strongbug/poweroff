package cn.power;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static final String DEFAULT_TIME = "1970-01-01 00:00:00";
	
	
	public static String DATE_FORMAT = "yyyy-MM-dd";
	public static String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
	
	private static SimpleDateFormat date_formatter = new SimpleDateFormat(DATE_FORMAT);
	private static SimpleDateFormat time_formatter = new SimpleDateFormat(DATE_TIME_FORMAT);
	
	public static String getDateString(Date date) {
		return date_formatter.format(date);
	}
	
	public static Date getDate(String str) {
		Date date = null;
		try {
			date = date_formatter.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}
		
		return date;
	}
	
	public static String getTimeString(Date date) {
		return time_formatter.format(date);
	}
	
	public static Date getTime(String str) {
		Date date = null;
		try {
			date = time_formatter.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
		}
		
		return date;
	}
}
