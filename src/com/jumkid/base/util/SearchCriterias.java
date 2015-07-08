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
 * VERSION   |   DATE    |  DEVELOPER  |  DESC
 * -----------------------------------------------------------------
 * 1.0b729      july2008    chooli        creation
 * 
 */
package com.jumkid.base.util;

import java.util.ArrayList;

public class SearchCriterias {
	
	private ArrayList<SearchCriteria> criterias;

	public SearchCriterias(){
		criterias = new ArrayList<SearchCriteria>();
	}

	public ArrayList<SearchCriteria> getCriterias() {
		return criterias;
	}

	public void setCriterias(ArrayList<SearchCriteria> criterias) {
		this.criterias = criterias;
	}
	
	public void addCriteria(SearchCriteria critera){
		if(criterias==null) criterias = new ArrayList<SearchCriteria>();
		criterias.add(critera);
	}
	
}
