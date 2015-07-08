package com.jumkid.base.util;

import java.nio.charset.Charset;
import java.util.Locale;
/* 
 * This software is written by Jumkid and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 *
 * (c)2013 Jumkid All rights reserved.
 *
 * VERSION   | DATE      | DEVELOPER  | DESC
 * -----------------------------------------------------------------
 * 3.0        Jan2013      chooli      creation
 * 
 *
 */

public interface Constants {
	
	public static final String DEFAULT_ENCODING = "UTF-8";
	
	public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");
	
	public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	public static final String DEFAULT_LOCALE_STRING = Locale.ENGLISH.toString();
	
	public static final Locale CHINESE_LOCALE = Locale.CHINESE;
	public static final String CHINESE_LOCALE_STRING = Locale.CHINESE.toString();

	public static final int STATUS_ACTIVE = 1;
    public static final Integer STATUS_ACTIVE_AS_INTEGER = new Integer(STATUS_ACTIVE);
    public static final int STATUS_DELETED = 0;
    public static final Integer STATUS_DELETED_AS_INTEGER = new Integer(STATUS_DELETED);
	
	/*-- client message constants --*/
	public final static String CMSG_NO_LANGCODE = "CMSG_NO_LANGCODE";
	
	public final static String CMSG_NO_NAME = "CMSG_NO_NAME";
			
	public final static String CMSG_INVALID_USER = "CMSG_INVALID_USER";	
	
	public final static String CMSG_INVALID_VTYPE = "CMSG_INVALID_VTYPE";
	
	public final static String CMSG_INVALID_ARGUMENT = "CMSG_INVALID_ARGUMENT";
	
	public final static String CMSG_DUPLICATED_REC = "CMSG_DUPLICATED_REC";
	
	public final static String CMSG_EMPTY_VALUE = "CMSG_EMPTY_VALUE";
	
	public final static String CMSG_REMOTEMR_ERROR = "CMSG_REMOTEMR_ERROR";
	
	public final static String CMSG_NOT_RECORD_OWNER = "CMSG_NOT_RECORD_OWNER";
	
	public final static String CMSG_NO_DATA = "CMSG_NO_DATA";
	
	public final static String CMSG_NO_PERMISSION = "CMSG_NO_PERMISSION";
		
	public final static String CMSG_CONCURRENT_MODIFICATION = "CMSG_CONCURRENT_MODIFICATION";
	
	public static final String PUBLIC = "PUBLIC";
	public static final String PRIVATE = "PRIVATE";
	
	public static final String SORTORDER_ASC = "asc";
    public static final String SORTORDER_DESC = "desc";
	
	public static final String USER_UNKNOWN = "unknown";
	
	public static final int DEFAULT_1K = 1024;
	
	public static final int DEFAULT_2K = 1024 * 2;
	
	public static final int DEFAULT_4K = 1024 * 4;
	
	public static final int DEFAULT_8K = 1024 * 8;
	
	public static final int DEFAULT_16K = 1024 * 16;
	
	public static final int DEFAULT_32K = 1024 * 32;
	
	public static final int DEFAULT_64K = 1024 * 64;
	
}
