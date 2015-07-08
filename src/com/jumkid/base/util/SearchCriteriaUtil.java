/* 
 * This software is written by CLIENTFILLS Ltd. and subject
 * to a contract between CLIENTFILLS and its customer.
 *
 * This software stays property of CLIENTFILLS unless differing
 * arrangements between CLIENTFILLS and its customer apply.
 *
 * CLIENTFILLS Ltd.
 * 20/F Central Tower
 * 28 Queen's Road Central
 * Hong Kong
 * 
 * Tel: +852 8199 9605
 * http://www.clientfills.com
 * mailto:info@clientfills.com
 *
 * (c)2008 CLIENTFILLS Ltd. All rights reserved.
 * 
 * VERSION   | DATE      | DEVELOPER  | DESC
 * -----------------------------------------------------------------
 * 1.0b729     aug2008     chooli       creation
 * 
 */
package com.jumkid.base.util;

import java.io.StringReader;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.InputSource;

public class SearchCriteriaUtil {
	
	protected static final Log logger = LogFactory.getLog(SearchCriteriaUtil.class);
	
	/**
     * Transfer xml string to searchcriteria object list
     * 
     * @param xml
     * @return
     * @throws Exception
     */
//    public static List<SearchCriteria> transformXmlToSearchCriterias(String xml) throws Exception {
//    	
//    	logger.debug("parsing " + xml);
//        Digester digester = new Digester();
//        digester.setNamespaceAware(true);
//        digester.setValidating(false);
//        digester.addObjectCreate("criterias", SearchCriterias.class);
//        digester.addObjectCreate("criterias/criteria", SearchCriteria.class);
//        digester.addBeanPropertySetter("criterias/criteria/fieldName", "fieldName");
//        digester.addBeanPropertySetter("criterias/criteria/operator", "operator");
//        digester.addBeanPropertySetter("criterias/criteria/condition", "condition");        
//        digester.addBeanPropertySetter("criterias/criteria/value", "value");          
//        digester.addBeanPropertySetter("criterias/criteria/type", "type");
//        digester.addSetNext("criterias/criteria", "addCriteria");  
//        SearchCriterias criterias = (SearchCriterias)digester.parse( new InputSource(new StringReader(xml)) );               
//        
//        return criterias.getCriterias();
//    }
	
	/**
     * Transfer xml string to searchcriteria object list
     * 
     * @param xml
     * @return
     * @throws Exception
     */
    public static List<SearchCriteria> transformJsonToSearchCriterias(String json) throws Exception {
    	//TODO
    	return null;
    }
	
	
}
