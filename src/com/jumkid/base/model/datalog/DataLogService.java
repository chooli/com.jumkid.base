package com.jumkid.base.model.datalog;
/* 
 * This software is written by Jumkid Ltd. and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 * 
 *
 * (c)2008 Jumkid Ltd. All rights reserved.
 *
 * VERSION   |    DATE      |     DEVELOPER     |    DESC
 * -----------------------------------------------------------
 * 1.3.1       Mar2009            chooli             creation
 * 
 */
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.util.StringUtils;

import com.jumkid.base.model.AbstractCommandService;
import com.jumkid.base.model.Command;
import com.jumkid.base.model.AbstractBean;



/**
 * @see com.jumkid.base.model.datalog.IDataLogService
 */
public class DataLogService extends AbstractCommandService implements IDataLogService{
   
	 private IDataLogDao dataLogDao;	 

	/**
      * Executes the commands and returns it back
      * @param cmd is the command bean
      * @return command which is same as input param plus result values
      */
    public Command execute(Command cmd) throws Exception {
    	super.execute(cmd);
    	
    	try {
	        if (isManager("logmanager")) {
	            // load datalog
	            if (isAction("load")) {
	                Integer id = (Integer)cmd.getParams().get("id");
	                DataLog datalog = getDataLogById( id );
	                cmd.addResults( "datalog", datalog );
	                
	            }else 
	            // save datalog
	            if (isAction("save")) {                
	                DataLog datalog = (DataLog)cmd.getParams().get("datalog");
	                datalog = saveDataLog(datalog);
	                cmd.addResults( "datalog", datalog );
	                
	            } else
	            // delete datalog
	            if (isAction("delete")) {
	                Integer id = (Integer)cmd.getParams().get("id");	                
                    deleteDataLog(id);
	                           
	            } else
	            // list or search datalog
	            if (isAction("list")) {
	                Integer start = (Integer)cmd.getParams().get("start");
	                Integer limit = (Integer)cmd.getParams().get("limit");
	                String sortOrder = (String)cmd.getParams().get("sortOrder");
	                String sortCriteria = (String)cmd.getParams().get("sortCriteria");    
	                String module = (String)cmd.getParams().get("module");                
	                String objectId = (String)cmd.getParams().get("objectId");
	                String action = (String)cmd.getParams().get("action");
	                Date dateBefore = (Date)cmd.getParams().get("dateBefore");
	                
	                DetachedCriteria criteria = null;                
	                if(dateBefore!=null){
	                	criteria = getDataLogDao().createDetachedCriteria(module, dateBefore, action);
	                }else{
	                	criteria = getDataLogDao().createDetachedCriteria(module, objectId);
	                }
	                
	                
	                List<DataLog> datalogs = this.getDataLogsByCriteria(criteria, start, limit, sortCriteria, sortOrder);
	                cmd.setTotoalRecords(getDataLogDao().getRowCount(criteria, DataLog.class));
	                
	                cmd.addResults("datalogs", datalogs);              
	            }
	        }
    	} catch (Exception e) {
    		logger.error(e.getMessage());
            cmd.setError(e.getMessage());    
        }
        
    	return cmd;
    }
    
    private DataLog getDataLogById(Integer id) throws Exception {
        return (DataLog)dataLogDao.load(id);        
    }
	 
    public void deleteDataLog(DataLog datalog) throws Exception {
        deleteDataLog(datalog.getId());
    }
    
    public void deleteDataLog(Integer id) throws Exception {
        getDataLogDao().remove(id);        
    }
    
    public List<DataLog> getDataLogsByCriteria(DetachedCriteria criteria, Integer start, Integer limit, String sortCriteria, String sortOrder) throws Exception {    	    	    	
        return new ArrayList<DataLog>(getDataLogDao().getDataLogList(criteria, start, limit, sortCriteria, sortOrder));
    }
    
    public DataLog diffObjectChanges(AbstractBean saveObject, AbstractBean existObject, DataLog datalog){
    	StringBuffer diffXmlSB = new StringBuffer();
    	
    	Class[] parameterTypeList = new Class[]{};
		Object[] actualParameterList = new Object[]{};
		
		try{
			for (String propertyName: (String[]) saveObject.getClass().getMethod("getPropertyNames", parameterTypeList).invoke(saveObject, actualParameterList)) {
				String methodName = "get" + StringUtils.capitalize(propertyName);
				// get values
				Object newValue = saveObject.getClass().getMethod(methodName, parameterTypeList).invoke(saveObject, actualParameterList);
				Object oldValue = ( existObject!=null ? existObject.getClass().getMethod(methodName, parameterTypeList).invoke(existObject, actualParameterList) : null );
				
				String diff = generateDiffXml(propertyName, newValue, oldValue);
				if(diff!=null){
					diffXmlSB.append(diff);				
				}
			}
		}catch(Exception e){
			logger.error(e);
		}
		
		datalog.setKeyFieldChanges(diffXmlSB.toString());
		
		return datalog;
    }

    private String generateDiffXml(String keyfield, Object newValue, Object oldValue){       	
    	if(oldValue==null){    		
    		if(newValue!=null && !newValue.toString().isEmpty()) return "<field>"+keyfield+"</field>";
    	}else if(newValue==null){
    		if(oldValue!=null) return "<field>"+keyfield+"</field>";    		
    	}else if(newValue!=null && oldValue!=null){
    		if(!newValue.equals(oldValue)) return "<field>"+keyfield+"</field>";
    	}
    	return null;
    }
    
    /**
     * Sets the reference to <code>datalog</code>'s DAO.
     */
    public void setDataLogDao(IDataLogDao datalogDao)
    {
        this.dataLogDao = datalogDao;
    }

    /**
     * Gets the reference to <code>users</code>'s DAO.
     */
    protected IDataLogDao getDataLogDao()
    {
        return this.dataLogDao;
    }

    /**
     * @see com.clientvision.crm.model.client.datalog.IDataLogService#saveDataLog(com.clientvision.crm.model.client.datalog.DataLog)
     */
    private DataLog saveDataLog(DataLog datalog){

        if (datalog == null) {
            throw new IllegalArgumentException("'datalog' can not be null");
        }
        datalog = (DataLog)dataLogDao.create(datalog);
        return datalog; 
        
    }     
    
}