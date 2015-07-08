package com.jumkid.base.model;

/* 
 * This software is written by Jumkid Ltd. and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 * Jumkid Ltd.
 * 20/F Central Tower
 * 28 Queen's Road Central
 * Hong Kong
 * 
 * Tel: +852 8199 9605
 * http://www.jumkid.com
 * mailto:info@jumkid.com
 *
 * (c)2008 Jumkid Ltd. All rights reserved.
 *
 */

import java.lang.reflect.Field;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.jumkid.base.model.user.User;
import com.jumkid.base.util.Formatter;

@MappedSuperclass
public abstract class CommonBean extends AbstractBean{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String PROPERTY_ID = "id";
	public static final String PROPERTY_LOCALE = "locale";
	public static final String PROPERTY_CREATEDBY = "createdBy";
	public static final String PROPERTY_CREATEDON = "createdOn";
	public static final String PROPERTY_CREATE_DATE = "createdDate";
	public static final String PROPERTY_MODIFYBY = "modifiedBy";
	public static final String PROPERTY_MODIFYON = "modifiedOn";
	public static final String PROPERTY_MODIFY_DATE = "modifiedDate";
	public static final String PROPERTY_ASSIGNEDUSERID = "assignedUserId";
	public static final String PROPERTY_ASSIGNEDUSER = "assignedUser";
	public static final String PROPERTY_METADATA = "_metadata";  
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "ID", unique = true, updatable = false, nullable = false)
	protected Integer id;
	
	@Column (name = "created_by")
	protected String createdBy;
	
	@Column (name = "created_on")
	protected Timestamp createdOn;
	
	@Column (name = "modified_by")
	protected String modifiedBy;
	
	@Column (name = "modified_on")
	protected Timestamp modifiedOn;
	
	//not for persistent
	@Transient
	private String metadata;
	@Transient
	private String createDate;
	@Transient
	private String modifiedDate;
	
	@Transient
    private User assignedUser;
	@Transient
    private Integer assignedUserId;
    
	@Transient
    private String[] propertyNames;
	@Transient
    private String presentedTitle;
	
	/**
     * toXML method using reflection
     * @param locale
     * @return Object as XML
     */
    @SuppressWarnings({ "unchecked" })
	public String toXML() {
        String xml = Formatter.toXMLStartTag(this.getClass().getSimpleName().toLowerCase());
        
        xml += Formatter.toXMLTag(PROPERTY_ID, this.getId(), false);
        xml += Formatter.toXMLTag(PROPERTY_CREATEDON, this.getCreatedOn(), false);
        xml += Formatter.toXMLTag(PROPERTY_CREATEDBY, this.getCreatedBy(), false);
        xml += Formatter.toXMLTag(PROPERTY_MODIFYON, this.getModifiedOn(), false);
        xml += Formatter.toXMLTag(PROPERTY_ASSIGNEDUSERID, this.getAssignedUserId(), false);
        xml += Formatter.toXMLTag(PROPERTY_ASSIGNEDUSER, 
        		(assignedUser==null?null:assignedUser.getUsername()), false);
        
        try {
        	int totalFields = this.getClass().getDeclaredFields().length;
            for (int i=0;i<totalFields;i++) {
        		Field field = this.getClass().getDeclaredFields()[i];
        		if(!java.lang.reflect.Modifier.isStatic(field.getModifiers())){
	                xml += Formatter.toXMLStartTag(field.getName());
	                if (!"serialVersionUID".equals(field.getName()) && !"logger".equals(field.getName())) {
	                    Object value = this.getClass().getMethod("get" + Formatter.getInstance().capitalize(field.getName())).invoke(this);
	                    if(value instanceof AbstractBean){
	                    	AbstractBean bean = (AbstractBean)value;
	                    	xml += bean.toXML();
	                    }else 
	                    if(value instanceof java.util.Collection<?>){
	                    	java.util.Collection<AbstractBean> vList = (java.util.Collection<AbstractBean>)value;
	                    	for(AbstractBean bean:vList){
	                    		xml += bean.toXML();
	                    	}
	                    }else{
	                    	xml +=  value==null?"":value.toString() ;
	                    }
	                    
	                }
	                xml += Formatter.toXMLEndTag(field.getName());
            	}
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        xml += Formatter.toXMLEndTag(this.getClass().getSimpleName().toLowerCase());
        return xml;
    }
    
    public String toJSONMeta() {
    	String json = "{";
    	
    	json += Formatter.toJSONString(PROPERTY_ID, Formatter.TYPE_JSON_STRING, 
				Integer.class.getName(), false);
		json += Formatter.toJSONString(PROPERTY_CREATEDON, Formatter.TYPE_JSON_STRING, 
				Timestamp.class.getName(), false);
		json += Formatter.toJSONString(PROPERTY_CREATEDBY, Formatter.TYPE_JSON_STRING, 
				String.class.getName(), false);
		json += Formatter.toJSONString(PROPERTY_CREATE_DATE, Formatter.TYPE_JSON_STRING, 
				String.class.getName(), false);
		json += Formatter.toJSONString(PROPERTY_MODIFYON, Formatter.TYPE_JSON_STRING, 
				Timestamp.class.getName(), false);
		json += Formatter.toJSONString(PROPERTY_MODIFY_DATE, Formatter.TYPE_JSON_STRING, 
				String.class.getName(), false);
		json += Formatter.toJSONString(PROPERTY_ASSIGNEDUSERID, Formatter.TYPE_JSON_STRING, 
				Integer.class.getName(), false);
		json += Formatter.toJSONString(PROPERTY_ASSIGNEDUSER, Formatter.TYPE_JSON_STRING, 
				User.class.getName(), false);

    	try {
    		Field[] myFields = this.getClass().getDeclaredFields();
    		Field[] parentFields = this.getClass().getSuperclass().getDeclaredFields();
    		Field[] fields = combine(myFields, parentFields);
    		int flength = fields.length;
            for (int i=0;i<flength;i++) {
            	
            	java.lang.reflect.Field field = fields[i];
            	field.setAccessible(true);
                
                if(!java.lang.reflect.Modifier.isStatic(field.getModifiers())){
                    
                	json += Formatter.toJSONString(field.getName(), Formatter.TYPE_JSON_STRING, 
                			field.getType().getName(), (i+1==flength?true:false));
                        
                }
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    	json += "}";        
        
        return json;
    }
    
    /**
     * toJSON method using reflection
     * 
     * @param locale
     * @return Object as XML
     */
    public String toJSON(){
    	String json = "{";
        
    	json += Formatter.toJSONString(PROPERTY_METADATA, Formatter.TYPE_JSON_OBJECT, 
				this.toJSONMeta(), false);
    	
    	json += Formatter.toJSONString(PROPERTY_ID, Formatter.TYPE_JSON_NUMBER, 
    											this.getId(), false);
    	json += Formatter.toJSONString(PROPERTY_CREATEDON, Formatter.TYPE_JSON_DATETIME, 
												this.getCreatedOn(), false);
    	json += Formatter.toJSONString(PROPERTY_CREATEDBY, Formatter.TYPE_JSON_STRING, 
												this.getCreatedBy(), false);
    	json += Formatter.toJSONString(PROPERTY_CREATE_DATE, Formatter.TYPE_JSON_STRING, 
												this.getCreateDate(), false);
    	json += Formatter.toJSONString(PROPERTY_MODIFYON, Formatter.TYPE_JSON_DATETIME, 
												this.getModifiedOn(), false);
    	json += Formatter.toJSONString(PROPERTY_MODIFY_DATE, Formatter.TYPE_JSON_STRING, 
												this.getModifiedDate(), false);
    	json += Formatter.toJSONString(PROPERTY_ASSIGNEDUSERID, Formatter.TYPE_JSON_NUMBER, 
												this.getAssignedUserId(), false);
    	json += Formatter.toJSONString(PROPERTY_ASSIGNEDUSER, Formatter.TYPE_JSON_STRING, 
										(assignedUser==null?null:assignedUser.getUsername()), false);
    	
    	try {
    		Field[] myFields = this.getClass().getDeclaredFields();
    		Field[] parentFields = this.getClass().getSuperclass().getDeclaredFields();
    		Field[] fields = combine(myFields, parentFields);
    		int flength = fields.length;
            for (int i=0;i<flength;i++) {
            	
            	java.lang.reflect.Field field = fields[i];
                
                if(!java.lang.reflect.Modifier.isStatic(field.getModifiers())){
                	Object value = this.getClass().getMethod("get" + Formatter.getInstance().capitalize(field.getName())).invoke(this);
                    
                	json += toJSONString(field, value, (i+1==flength?true:false));
                        
                }
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
        json += "}";        
        
        return json;
    }   
    
    /**
     * 
     * @return
     */
    public String toCSV(){
    	String csv = "";
        
    	try {
    		Field[] fields = this.getClass().getDeclaredFields();
            for (int i=0;i<fields.length;i++) {
                
                if(!java.lang.reflect.Modifier.isStatic(fields[i].getModifiers())){
                	Object value = this.getClass().getMethod("get" + Formatter.getInstance().capitalize(fields[i].getName())).invoke(this);
                    
                	csv += (value!=null ? String.valueOf(value):"") + (i<fields.length-1 ? ", " : "");
                        
                }
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    	        
        return csv;
    }
    
    /**
     * 
     * @param field
     * @param value
     * @return
     * @throws Exception
     */
    protected String toJSONString(Field field, Object value, boolean islast) throws Exception{
    	String json = super.toJSONString(field, value, islast);
    	
    	if(json.isEmpty() && field.getType().isAssignableFrom(CommonBean.class) ){
    		if(value==null) {
    			json += Formatter.toJSONString(field.getName(), Formatter.TYPE_JSON_NULL, 
            			islast);
    		}else{
    			String jsonString = (String)(value.getClass().getMethod("toJSON").invoke(value));
            	json += Formatter.toJSONString(field.getName(), Formatter.TYPE_JSON_OBJECT, 
            			jsonString, islast);
    		}
        }
		
    	return json;
    }
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Timestamp modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	
	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(String modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public User getAssignedUser() {
		return assignedUser;
	}

	public void setAssignedUser(User assignedUser) {
		this.assignedUser = assignedUser;
	}

	public Integer getAssignedUserId() {
		return assignedUserId;
	}

	public void setAssignedUserId(Integer assignedUserId) {
		this.assignedUserId = assignedUserId;
	}

	public String[] getPropertyNames() {
		return propertyNames;
	}

	public void setPropertyNames(String[] propertyNames) {
		this.propertyNames = propertyNames;
	}

	public String getPresentedTitle() {
		return presentedTitle;
	}

	public void setPresentedTitle(String presentedTitle) {
		this.presentedTitle = presentedTitle;
	}

	public Field[] combine(Field[] f1, Field[] f2){
		int length = f1.length + f2.length;
		Field[] result = new Field[length];
        System.arraycopy(f1, 0, result, 0, f1.length);
        System.arraycopy(f2, 0, result, f1.length, f2.length);
        
        return result;
	}
	
}
