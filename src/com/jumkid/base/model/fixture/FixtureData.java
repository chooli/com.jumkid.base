package com.jumkid.base.model.fixture;

import org.apache.solr.client.solrj.beans.Field;

import com.jumkid.base.model.CommonBean;

public class FixtureData extends CommonBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4990621247400508299L;

	@Field
	public String uuid;
	
	@Field
	private String vlname;
	
	@Field
	private String vlvalue;
	
	@Field
	private String locale;
	
	@Field
	private String site;
	
	@Field("ref_code")
	private String refCode;
	
	@Field
	private String description;
	
	public FixtureData(){
		//void
	}
	
	public FixtureData(String vlname, String vlvalue, String locale,
			String description){
		this.vlname = vlname;
		this.vlvalue = vlvalue;
		this.locale = locale;
		this.description = description;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getVlname() {
		return vlname;
	}

	public void setVlname(String vlname) {
		this.vlname = vlname;
	}

	public String getVlvalue() {
		return vlvalue;
	}

	public void setVlvalue(String vlvalue) {
		this.vlvalue = vlvalue;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRefCode() {
		return refCode;
	}

	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}
	
		
}
