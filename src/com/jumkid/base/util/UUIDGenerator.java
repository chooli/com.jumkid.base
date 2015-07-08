package com.jumkid.base.util;

import java.util.UUID;

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
 * 3.0        Dec2013      chooli      creation
 * 
 *
 */

public class UUIDGenerator {

	private UUIDGenerator() {// singleton
		//void
	} 
	
	public static String next() {
		UUID u = UUID.randomUUID();
	    return toIDString(u.getMostSignificantBits()) + toIDString(u.getLeastSignificantBits());
	}

	private static String toIDString(long i) {
	    char[] buf = new char[32];
	    int z = 64; // 1 << 6;
	    int cp = 32;
	    long b = z - 1;
	    do {
	        buf[--cp] = DIGITS65[(int)(i & b)];
	        i >>>= 6;
	    } while (i != 0);
	    return new String(buf, cp, (32-cp));
	}

	// array de 64+2 digitos 
	private final static char[] DIGITS65 = {
	    '0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
	    'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
	    '-','_','~'
	};
	
}
