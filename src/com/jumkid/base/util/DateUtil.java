package com.jumkid.base.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;

public class DateUtil {

	public static Date getPreviousDate(Integer numOfDay){
		long currentTimeMillis = Calendar.getInstance().getTimeInMillis();
		Timestamp startDate = new Timestamp(currentTimeMillis - (numOfDay * Formatter.ONE_DAY_TIME_LONG) ); //7 days ago
		
		return new Date(startDate.getTime());
	}
	
	public static Date getAfterDate(Integer numOfDay){
		long currentTimeMillis = Calendar.getInstance().getTimeInMillis();
		Timestamp startDate = new Timestamp(currentTimeMillis + (numOfDay * Formatter.ONE_DAY_TIME_LONG) ); //7 days ago
		
		return new Date(startDate.getTime());
	}
	
}
