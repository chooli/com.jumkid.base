package com.jumkid.base.util;

/* 
 * This software is written by Jumkid and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 * (c)2013 Jumkid All rights reserved.
 *
 * Modification History
 *
 * VERSION     |     DATE      |   DEVELOPER  |  DESC
 * ----------------------------------------------------------------
 * 1.0           Jan2008         Chooli          Creation
 * 
 */
 
import java.io.*;

import org.apache.commons.codec.binary.Base64;

public class XmlUtil {
		
	public static String cdata(Object data) {
        return "<![CDATA[" + (data==null?"":SanitizeXmlString(data.toString())) + "]]>";
    }
	
	public static String escapeQuotation(String content){
		String s1 = "";
		s1 = content.replaceAll("\"", "\\\\\"");
		s1 = s1.replaceAll("\\n", "");
		s1 = s1.replaceAll("\\t", "");
		return s1;
	}
	
	public static String escapeWhiteSpace(String content){
		if(content!=null){
			String s1 = content.trim();
			s1 = s1.replaceAll(" ", "");
			s1 = s1.replaceAll("\\n", "");
			s1 = s1.replaceAll("\\t", "");
			return s1;
		}
		
		return content;
	}
	
	/*
	 *  <summary>
	 *  Remove illegal XML characters from a string.
	 *  </summary>
	 */	
	public static String SanitizeXmlString(String xml)
	{
	    if (xml==null || xml.isEmpty())
	    {
	        return xml;
	    }
	 
	    StringBuilder buffer = new StringBuilder(xml.length());
	 
	 
	    for (int i=0;i<xml.length();i++)
	    {
	        if (IsLegalXmlChar(xml.charAt(i)))
	        {
	            buffer.append(xml.charAt(i));
	        }
	    }
	 
	    return buffer.toString();
	}
	 
	/*
	 *  <summary>
	 *  Whether a given character is allowed by XML 1.0.
	 *  </summary>
	 */	
	public static boolean IsLegalXmlChar(int character)
	{					
	    return
	    (
	         character == 0x9 /* == '\t' == 9   */        ||
	         character == 0xA /* == '\n' == 10  */        ||
	         character == 0xD /* == '\r' == 13  */        ||
	        (character >= 0x20    && character <= 0xD7FF) ||
	        (character >= 0xE000  && character <= 0xFFFD) ||
	        (character >= 0x10000 && character <= 0x10FFFF)
	    );
	}
	
	/**
     * remote xml instruction string
     *
     * @param xmlstring
     * @return String
     */
    public static String removeInstruction(String xmlstring){
        int instrstart = xmlstring.indexOf("<?");
        int instrend = xmlstring.indexOf("?>");
        if (instrstart==-1 || instrend==-1){
            return xmlstring;
        }
        //replace the xml instruction
        return xmlstring.substring(instrend+2);
    }
    
    /**
     * Append system default xml instruction string
     * If the instruction string is existed, then remove it first
     * @param xml
     * @return String
     */
    public static String appendInstruction(String xml){
        String tmpxml = removeInstruction(xml);
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + tmpxml;
    }

    /**
     * Remove xml comment
     *
     * @param xmlstring
     * @return String
     */
    public static String removeComment(String xmlstring){
        int instrstart = xmlstring.indexOf("<!--");
        int instrend = xmlstring.indexOf("-->");
        if (instrstart==-1 || instrend==-1){
            return xmlstring;
        }
        //replace the xml instrution
        return xmlstring.substring(instrend+4);
    }    
    
    /**
     * Determin the given file name is a XML file or not
     *
     * @param fileName
     * @return boolean
     */
    public static boolean isXMLFile(String fileName){
        if(fileName.endsWith(".xml") || fileName.endsWith(".xsl") || fileName.endsWith(".xsd")){
            return true;
        }
        return false;
    }
    
    public static boolean isXQueryFile(String fileName){
    	if(fileName.endsWith(".xq") || fileName.endsWith(".xql")){
    		return true;
    	}
    	return false;
    }
        
    /**
     * Determin the given file name is a HTML file or not
     *
     * @param fileName
     * @return boolean
     */
    public static boolean isHTMLFile(String fileName){
        if(fileName.endsWith(".htm") || fileName.endsWith(".html") || fileName.endsWith(".xhtml")){
            return true;
        }
        return false;
    }
    
    /**
     * Determin the given file name is a JavaScript file or not
     *
     * @param fileName
     * @return boolean
     */
    public static boolean isJSFile(String fileName){
        if(fileName.endsWith(".js")){
            return true;
        }
        return false;
    }

    /**
     * Determin the given name is valid XML name or not
     *
     * @param name
     * @return boolean
     */
    public static boolean isValidName(String name){
        if(name.indexOf(">")!=-1 || name.indexOf("<")!=-1 ||
                name.indexOf("&")!=-1) return false;
        return true;
    }
    
    /**
     * capture a inner content of single tag. If found the xml entity, then return it without entity quotation
     *
     * @param xml
     * @return String
     */
    public static String captureSingleTagValue(String xml){
        if (!xml.startsWith("<") || !xml.endsWith(">") ||  (xml.indexOf("</")==-1)){
            return null;
        }
        int startTagOff = xml.indexOf(">")+1;
        int endTagOn = xml.lastIndexOf("</");
        return removeEntityTag(xml.substring(startTagOff, endTagOn));
    }

    /**
     * remove xml entity tag
     * @param xml
     * @return String
     */
    private static String removeEntityTag(String xml){
        if (xml.startsWith("<![CDATA[") && xml.endsWith("]]>")){
            return xml.substring(9, xml.lastIndexOf("]]>"));
        }
        return xml;
    }
    
    
    /**
     * encodeBase64 convert a file object to a base64 encoded string
     *
     * @param file
     * @return
     * @throws XMLDataProcessException
     */
    public static String encodeBase64(File file) throws Exception{
        if (file==null || !file.exists()){
            return null;
        }
        String base64 = null;
        try{
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis, 1024);
            byte[] buf = new byte[fis.available()];
            bis.read(buf);
            byte[] encoded = Base64.encodeBase64(buf);
            base64 = new String(encoded);
                        
            bis.close();
        }catch(Exception e){
            throw new Exception("Fail to encode the file to base64");
        }
        return base64;
    }

    /**
     * decodeBase64 convert a base64 encoded string to byte[]
     *
     * @param b64code
     * @return byte[]
     */
    public static byte[] decodeBase64(String b64code){
        if (b64code==null || b64code.equals("")){
            return null;
        }
        return decodeBase64(b64code.getBytes());
    }

    public static byte[] decodeBase64(byte[] b64bytes){
        return Base64.decodeBase64(b64bytes);
    }

    /**
     * decodeBase64(b64code, fileName) convert a base64 code string to a source file
     *
     * @param b64code
     * @param fileName
     * @return
     * @throws XMLDataProcessException
     */
    public static File decodeBase64(String b64code, String fileName) throws Exception{
        byte[] source = decodeBase64(b64code);
        try {
            File target = new File(fileName);
            if (!target.exists()){
                if (!target.createNewFile()){
                    throw new Exception("Fail to create the souce file");
                }
            }
            FileOutputStream fos = new FileOutputStream(target);
            BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);
            bos.write(source);
            
            bos.close();
            
            return target;
        }catch(Exception e){
            throw new Exception(e);
        }
    }
    
}
