package com.jumkid.base.util;

/* 
 * This software is written by SocialStudio and subject
 * to a contract between SocialStudio and its customer.
 *
 * This software stays property of SocialStudio unless differing
 * arrangements between SocialStudio and its customer apply.
 *
 *
 * (c)2013 SocialStudio All rights reserved.
 *
 * VERSION   | DATE      | DEVELOPER  | DESC
 * -----------------------------------------------------------------
 * 1.0        Jan2013      chooli      creation
 * 
 *
 */

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CommonUtil {

	public static String streamToString(final InputStream is, String encoding) {
				
	    String result = null;
	    if (is != null) {
	        BufferedInputStream bis = new BufferedInputStream(is);
	        bis.mark(Integer.MAX_VALUE);
	        final StringBuilder stringBuilder = new StringBuilder();
	        try {
	            // stream reader that handle encoding
	            final InputStreamReader readerForEncoding = new InputStreamReader(bis);
	            final BufferedReader bufferedReaderForEncoding = new BufferedReader(readerForEncoding);
	            
	            if (encoding == null) {
	                encoding = Constants.DEFAULT_ENCODING;
	            }

	            // stream reader that handle encoding
	            bis.reset();
	            final InputStreamReader readerForContent = new InputStreamReader(bis, encoding);
	            final BufferedReader bufferedReaderForContent = new BufferedReader(readerForContent);

	            String line = bufferedReaderForContent.readLine();
	            //logger.info("line: " + line );
	            while (line != null) {
	                stringBuilder.append(line); 
	                line  = bufferedReaderForContent.readLine();
	            } 
	            bufferedReaderForContent.close();
	            bufferedReaderForEncoding.close();
	        } catch (IOException e) { 
	            // reset string builder
	            stringBuilder.delete(0, stringBuilder.length());
	        }  
	        result = stringBuilder.toString();
	    }else {
	        result = null;
	    }
	    
	    return result;
	}
	
	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws java.io.IOException
	 */
	public static String fileToString(String filePath)throws java.io.IOException{	
		try{
			File file = new File(filePath);
			if(file.isFile() && file.exists()){
				return streamToString(new FileInputStream(file), null);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String [] mergeStrings(String [] x, String [] y) {
		if(x==null) return y;
		if(y==null) return x;
		
	    List<String> mergedList = new ArrayList<String>();
	    int xp = 0, yp = 0;
	    while ( xp < x.length && yp < y.length){
	        if (x[xp].compareTo(y[yp]) < 0) {
	            mergedList.add(x[xp++]);
	        } else if (x[xp].compareTo(y[yp]) > 0) {
	            mergedList.add(y[yp++]);
	        } else {
	            mergedList.add(x[xp]);
	            xp++; yp++;
	        }
	    }
	    while (xp < x.length) {
	        mergedList.add(x[xp++]);
	    }
	    while (yp < y.length) {
	        mergedList.add(y[yp++]);
	    }
	    return mergedList.toArray(new String[0]);
	}

}
