/**
 * 
 */
package com.dev.kaizen.util;

import android.util.Log;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Anggoro Biandono
 * 3:13:43 PM
 */
public class DateUtil {
	private static SimpleDateFormat dateFormat;
	
	private static final String[]monthNames = new String[] {
		"Jan", "Feb", "Mar", "Apr", "Mei", "Jun",
		"Jul", "Aug", "Sep", "Okt", "Nov", "Des"
	};
	
	private DateUtil() {
		// TODO Auto-generated constructor stub
	}
	/**
     * Get current Date
     *
     * @return currentDate Date
     *
     */
    public static Date now() {

        Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    /**
     * Get current Timestamp
     *
     * @return currentTimestamp TimeStamp
     */
    public static Timestamp getTimestamp() {

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp currentTimestamp = new Timestamp(now.getTime());

        return currentTimestamp;
    }
    
    public static SimpleDateFormat getSimpleDateFormatInstance(String formatDate){
		if(formatDate == null)
			formatDate = Constant.DATE_FORMAT;
		if(dateFormat == null){			
			dateFormat = new SimpleDateFormat(formatDate);
		}else{
			dateFormat.applyPattern(formatDate);
		}
		
		return dateFormat;
	}
    
    public static SimpleDateFormat getSimpleDateFormatInstanceWithLocale(String formatDate, Locale locale){
    	
    	SimpleDateFormat outputFormatter =  new SimpleDateFormat(formatDate, locale);
		return outputFormatter;
	}
    
    public static boolean validateValueDateBefore(Calendar valCalendar){
		Calendar now = Calendar.getInstance();
		boolean result = true;
		if(valCalendar.get(Calendar.YEAR) < now.get(Calendar.YEAR))
			result = false;
		
		if(valCalendar.get(Calendar.MONTH) < now.get(Calendar.MONTH))
			result = false;
		
		if(valCalendar.get(Calendar.DATE) < now.get(Calendar.DATE))
			result = false;
		
		return result;
	}
    
    public static boolean validateValueDateAfter(Calendar valCalendar){
		Calendar now = Calendar.getInstance();
		return !now.before(valCalendar);
		/*
		boolean result = true;
		if(valCalendar.get(Calendar.YEAR) > now.get(Calendar.YEAR))
			result = false;
		if(valCalendar.get(Calendar.MONTH) > now.get(Calendar.MONTH))
			result = false;
		if(valCalendar.get(Calendar.MONTH) >= now.get(Calendar.MONTH) && valCalendar.get(Calendar.DATE) > now.get(Calendar.DATE))
			result = false;
		
		return result;
		*/
	}
    
    public static String parseLocaleDate(Date date){
		Locale locale = new Locale("in", "ID");
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy", locale);
		String parseLocaleDate = sdf.format(date);
		return parseLocaleDate;
	}
    
    public static String parseLocaleDateNoDay(Date date){
		Locale locale = new Locale("in", "ID");
		SimpleDateFormat sdf = new SimpleDateFormat("MMMM/yyyy", locale);
		String parseLocaleDate = sdf.format(date);
		return parseLocaleDate;
	}
    
    
    public static String toPrintableDateString(Date date){
    	Locale locale = new Locale("in", "ID");
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss", locale);
		return date.getDate() + " " + monthNames[date.getMonth()] + " " + String.valueOf(1900 + date.getYear()) + " " +
			sdf.format(date);
	}
    
    public static String getTodaysDate() {
    	 
        final Calendar c = Calendar.getInstance();
        int todaysDate =     (c.get(Calendar.YEAR) * 10000) +
        ((c.get(Calendar.MONTH) + 1) * 100) +
        (c.get(Calendar.DAY_OF_MONTH));
        if(Constant.SHOW_LOG){
        	Log.i("DATE:", String.valueOf(todaysDate));
        }
        
        return(String.valueOf(todaysDate));
 
    }
 
    public static String getCurrentTime() {
 
        final Calendar c = Calendar.getInstance();
        int currentTime =     (c.get(Calendar.HOUR_OF_DAY) * 10000) +
        (c.get(Calendar.MINUTE) * 100) +
        (c.get(Calendar.SECOND));
        if(Constant.SHOW_LOG){
        	Log.i("TIME:", String.valueOf(currentTime));
        }
        
        return(String.valueOf(currentTime));
 
    }
}
