package com.jumkid.base.model;
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
 * VERSION   |   DATE      | DEVELOPER  | DESC
 * -----------------------------------------------------------------
 * 3.0         July2013       chooli       creation
 * 
 *
 */

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;

import com.jumkid.base.model.datalog.DataLog;
import com.jumkid.base.model.datalog.IDataLogService;
import com.jumkid.base.search.SearchableModuleEntry;
import com.jumkid.base.search.SolrSearchableModuleEntryRepository;
import com.jumkid.base.util.UUIDGenerator;

@Scope("session")
public class CommonServiceAdvice{

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private SolrSearchableModuleEntryRepository moduleEntrySearchRepository;
	protected IDataLogService dataLogService;
	
	protected DataLog datalog;

	public void doBefore(Command cmd, String beanName, ICommonBeanDao commonBeanDao)
			throws Throwable {
		
		if( "save".equals(cmd.getAction()) ){
			
			CommonBean bean = (CommonBean)cmd.getParams().get(beanName);
						
			datalog = new DataLog();
			synchronized(datalog){
				this.setAction(datalog, cmd, beanName);
				
				if(commonBeanDao!=null){
					CommonBean existEntity = null;
		        	if("UPDATE".equals(datalog.getAction())){
		        		existEntity = (CommonBean)commonBeanDao.load(bean.getId());
		        	}
		        	datalog = dataLogService.diffObjectChanges(bean, existEntity, datalog); 
				}
			}
						
		}else
		if( "delete".equals(cmd.getAction()) ){
			Integer id = (Integer)cmd.getParams().get("id");
			
			datalog = new DataLog();
			synchronized(datalog){
				this.setAction(datalog, cmd, beanName);
				
				if(commonBeanDao!=null){
					CommonBean entity = (CommonBean)commonBeanDao.load(id);
					datalog.setObject( entity.toJSON() );
				}
			}
						
		}
	
	}

	public void doAfterReturning(Command cmd, String beanName, ICommonBeanDao commonBeanDao) throws Throwable {
		String site = (String)cmd.getParams().get("site");
		
		if( "save".equals(cmd.getAction()) ){
			CommonBean bean = (CommonBean)cmd.getResults().get(beanName);
			Integer id = bean.getId();
			
			/*-- save entry for full-text search --*/
			if(id!=null){
				SearchableModuleEntry entry = moduleEntrySearchRepository.findByModuleAndInternalId(site, beanName, 
						String.valueOf(bean.getId()) );
				synchronized(entry){
					if(entry==null){
						entry = new SearchableModuleEntry();
						entry.setId( UUIDGenerator.next() );
					}
					
					entry.setInternalId( String.valueOf(bean.getId()) );
					entry.setTitle(bean.getPresentedTitle());
					entry.setModifiedOn(bean.getModifiedOn());
					entry.setModule(beanName);
					entry.setContent(bean.toPlainText());
					
					moduleEntrySearchRepository.save(entry);
				}
				
			}
			/*-- save entry for full-text search end --*/
			
			//save data log
			if(datalog!=null){
				synchronized(datalog){
					this.setId(datalog, bean);
					datalog.setObject(bean.toJSON());
					saveDataLog(datalog);
				}
				
			}
			
		}else
		if( "delete".equals(cmd.getAction()) ){
			
			/*-- remove entry for full-text search --*/
			Integer id = (Integer)cmd.getParams().get("id");
			String uuid = (String)cmd.getParams().get("uuid");
			
			if(id!=null){
				SearchableModuleEntry bean = moduleEntrySearchRepository.findByModuleAndInternalId(site, beanName, 
						String.valueOf(id) );
				if(bean!=null){
					moduleEntrySearchRepository.delete(bean);
				}
			}
			
			/*-- remove entry for full-text search end --*/
			
			//save data log
			if(datalog!=null){
				synchronized(datalog){
					datalog.setObjectId(id==null?uuid:id.toString());
					saveDataLog(datalog);
				}
			}
			
		}
		
	}
	
	private synchronized void setAction(DataLog datalog, Command cmd, String beanName){
		CommonBean bean = (CommonBean)cmd.getParams().get(beanName);
		
		try{
			if(bean==null){
				datalog.setAction("DELETE");
			}else
			if(bean.getId()!=null){
				datalog.setAction("UPDATE");
			}else{
				String uuid = (String)bean.getClass().getMethod("getUuid").invoke(bean);
				if(uuid!=null && !uuid.isEmpty()) datalog.setAction("UPDATE");
				else datalog.setAction("CREATE");
			}
			
			datalog.setModule(beanName);
			datalog.setCreatedBy(this.getLoginUserName());
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private synchronized void setId(DataLog datalog, CommonBean bean){
		try{
			if(bean.getId()!=null){
				datalog.setObjectId( bean.getId().toString());
			}else{
				Object uuid = bean.getClass().getMethod("getUuid").invoke(bean);
				if(uuid!=null) datalog.setObjectId(uuid.toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	protected void saveDataLog(DataLog datalog){
		
		try{
			
			if(datalog!=null){	                    
	            HashMap<String, Object> params = new HashMap<String, Object>();
	            params.put("datalog", datalog);                                
	            dataLogService.execute(new Command("logmanager", "save", params));
			}
			
		}catch(Exception e){
			logger.error("failed to save datalog "+e.getMessage());
		}
		
	}
	
	private String getLoginUserName(){
		org.springframework.security.core.userdetails.User _user = (org.springframework.security.core.userdetails.User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return _user.getUsername();
	}
	
	public IDataLogService getDataLogService() {
		return dataLogService;
	}
	public void setDataLogService(IDataLogService dataLogService) {
		this.dataLogService = dataLogService;
	}

	public DataLog getDatalog() {
		return datalog;
	}

	public void setDatalog(DataLog datalog) {
		this.datalog = datalog;
	}

	public SolrSearchableModuleEntryRepository getModuleEntrySearchRepository() {
		return moduleEntrySearchRepository;
	}

	public void setModuleEntrySearchRepository(
			SolrSearchableModuleEntryRepository moduleEntrySearchRepository) {
		this.moduleEntrySearchRepository = moduleEntrySearchRepository;
	}
	
}
