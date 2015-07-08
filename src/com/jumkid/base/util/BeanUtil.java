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
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanUtil {
	
	public static void copy(Object dest, Object source) throws IntrospectionException, IllegalArgumentException, IllegalAccessException,
	    InvocationTargetException {
		
		BeanInfo beanInfo = Introspector.getBeanInfo(source.getClass());
		PropertyDescriptor[] pdList = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor pd : pdList) {
		    Method writeMethod = null;
		    Method readMethod = null;
		    try {
		        writeMethod = pd.getWriteMethod();
		        readMethod = pd.getReadMethod();
		    } catch (Exception e) {
		    	//void
		    }
		
		    if (readMethod == null || writeMethod == null) {
		        continue;
		    }
		
		    Object sourceVal = readMethod.invoke(source);
		    Object destVal = readMethod.invoke(dest);
		    if(destVal==null) writeMethod.invoke(dest, sourceVal);
		}
	}

	/**
	 * verify the given object has a getter method with the given property name
	 * 
	 * @param object
	 * @param propertyName
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static boolean hasGetter(Class objectClass, String propertyName){
		try{
			Method method = objectClass.getMethod("loadBy" + Formatter.getInstance().capitalize(propertyName));
			if(method!=null && method.isAccessible()){
				return true;
			}
		}catch(Exception e){
			//void
		}
		return false;
	}
    
    /**
     * When ajax call requests a list of objects, certain fields can be explicitly defined 
     * to be shown or hidden. showCheck checks if a property fields shall be included or excluded
     * @param property is the field to be checked if it shall be shown or hidden
     * @param include is the array of fields to be explicitly displayed
     * @param exclude is the array of fields to be explicitly hidden
     * @return true if property shall be displayed
     */
    public static boolean showCheck(String property, String[] include, String[] exclude) {
        return ( (include!=null && contains(property,include)) || 
                (exclude!=null && !contains(property,exclude)) ||
                (include==null && exclude==null) );
    }
    
    /**
     * Array helper class to check whether an array contains a certain elements
     * @param needle to search for
     * @param haystack to search within
     * @return true if needle is found in haystack
     */
    public static boolean contains(String needle, String[] haystack) {
        if (needle==null || haystack==null) return false;
        for (int i=0;i<haystack.length;i++) {
            if (haystack[i].equals(needle)) return true;
        }
        return false;
    }
    
    /**
     * When requests a list of objects, certain names can be explicitly defined 
     * to be shown or hidden. includeCheck checks if a name shall be included or excluded
     * @param name is the field to be checked if it shall be shown or hidden
     * @param include is the array of fields to be explicitly displayed
     * @param exclude is the array of fields to be explicitly hidden
     * @return true if property shall be displayed
     */
    public static boolean includeCheck(String name, String[] include, String[] exclude) {
        return ( (include!=null && includes(name,include)) || 
                (exclude!=null && !includes(name,exclude)) ||
                (include==null && exclude==null) );
    }
    
    /**
     * Array helper class to check whether an array includes a certain elements
     * @param needle to search for
     * @param haystack to search within
     * @return true if needle is found in haystack
     */
    public static boolean includes(String haystack, String[] needles) {
        if (needles==null || haystack==null) return false;
        for (int i=0;i<needles.length;i++) {        	
            if (haystack.contains(needles[i])) return true;
        }
        return false;
    }
    
}
