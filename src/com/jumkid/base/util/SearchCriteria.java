package com.jumkid.base.util;

import java.util.ArrayList;

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
 * 1.0b729     july2008     chooli      creation
 * 
 *
 */

public class SearchCriteria {

	private String fieldName;
	private String operator;
	private String condition = "and";
	private String type;
	private Object value;
	private ArrayList<SearchCriteria> nettingCriteriaList;
	
	public SearchCriteria(){
		//void
	}
	
	public SearchCriteria(String fieldName, String operator, String condition, String type, Object value){
		this.fieldName = fieldName;
		this.operator = operator;
		this.condition = condition;
		this.type = type;
		this.value = value;
	}
	
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getCondition() {
		return condition;
	}
	public void setCondition(String condition) {
		this.condition = condition;
	}
	public Object getValue() {
		try{
			if(this.getType().equals("bool")) return Boolean.valueOf((String)value);
			if(this.getType().equals("integer")) return Integer.valueOf((String)value);
		    if(this.getType().equals("number")) return Long.valueOf((String)value);
		    if(this.getType().equals("double")) return Double.valueOf((String)value);
		    if(this.getType().equals("date")) return Formatter.getInstance().stringToTimestamp((String)value, Formatter.yyyy_MM_dd);
		    return value;
		}catch(Exception e){
			return value;
		}
	}
	public void setValue(Object value) {		
		this.value = value;				
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public boolean hasNetting(){
		if(nettingCriteriaList!=null && !nettingCriteriaList.isEmpty()) return true;
		return false;
	}
	
	public void addNettingCriteria(SearchCriteria criteria){
		if(nettingCriteriaList==null) nettingCriteriaList = new ArrayList();
		nettingCriteriaList.add(criteria);
	}
	
	public ArrayList<SearchCriteria> getNettingCriteriaList() {
		return nettingCriteriaList;
	}

	public void setNettingCriteriaList(ArrayList<SearchCriteria> nettingCriteriaList) {
		this.nettingCriteriaList = nettingCriteriaList;
	}
	
}
