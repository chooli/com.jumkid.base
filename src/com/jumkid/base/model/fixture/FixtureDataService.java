package com.jumkid.base.model.fixture;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;

import com.jumkid.base.exception.BeanValidateException;
import com.jumkid.base.exception.SystemServiceException;
import com.jumkid.base.model.AbstractCommandService;
import com.jumkid.base.model.Command;
import com.jumkid.base.model.IAbstractBeanValidator;
import com.jumkid.base.util.UUIDGenerator;
import com.jumkid.base.util.XmlUtil;

public class FixtureDataService extends AbstractCommandService implements IFixtureDataService{
			
	private IFixtureDataRepository fixtureDataRepository;
	
	private IAbstractBeanValidator abstractBeanValidator;
	
	private Integer maxLimit;

	@Override
	public Command execute(Command cmd) throws SystemServiceException {
		
		try{
			super.execute(cmd);
			        	
        	if (isManager("fixtureDataManager")) {
                if (isAction("save")) {
                	FixtureData fixtureData = (FixtureData)cmd.getParams().get("fixturedata");
                	
                	synchronized(fixtureData){
                		if(fixtureData.getUuid()==null ||
                				fixtureData.getUuid().isEmpty()){
                    		fixtureData.setUuid(UUIDGenerator.next());
                    	}
                		fixtureData = fixtureDataRepository.save(fixtureData);
                	}
                	
                	cmd.getResults().put("fixturedata", fixtureData);
                	
                }else
            	if(isAction("load")){
            		String uuid = (String)cmd.getParams().get("uuid");
            		                	            		
                	FixtureData fixturedata = fixtureDataRepository.findById(uuid, null);
                	
                	cmd.getResults().put("fixturedata", fixturedata);
            	}else
            	if(isAction("search")){
            		String keyword = (String)cmd.getParams().get("keyword");
            		String facetField = (String)cmd.getParams().get("facetField");
            		String facetPrefix = (String)cmd.getParams().get("facetPrefix");
            		String locale = (String)cmd.getParams().get("locale");
            		
            		Integer start = (Integer)cmd.getParams().get("start");
                	Integer limit = (Integer)cmd.getParams().get("limit");
                	
                	if(facetField!=null && !facetField.isEmpty()){
                		FacetPage<FixtureData> page = null;
                    	Pageable pager =new PageRequest(start, limit); 
                		
                    	page = fixtureDataRepository.findByFacetField(facetField, facetPrefix, keyword, locale, null, pager);
                    	
                    	cmd.getResults().put("page", page);
                	}else{
                		Page<FixtureData> page = null;
                    	Pageable pager =new PageRequest(start, limit); 
                		
                    	page = fixtureDataRepository.findByText(keyword, null, pager);
                    	
                    	cmd.getResults().put("page", page);
                	}
                	
                	
            	}else
        		if(isAction("delete")){
        			String uuid = (String)cmd.getParams().get("uuid");
                	
        			try{
        				if(uuid!=null){
        					fixtureDataRepository.remove(uuid, this.site);
        				}
        			}catch(Exception e){
        				cmd.setError("Failed to delete social comment "+e.getMessage());
        			}
        		}else
        		if(isAction("import")){
        			byte[] bytes = (byte[])cmd.getParams().get("file");
                	
        			try{
        				if(bytes!=null && bytes.length>0){
        					ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        					BufferedReader bfReader = new BufferedReader(new InputStreamReader(bais)); 
        					
        					String separator = ",";
        					String line = null;
        					int counter = 0 ;
        		            while((line = bfReader.readLine()) != null){
        		            	line = XmlUtil.escapeQuotation(line);
        		            	String[] columns = line.split(separator);
        		            	if(columns!=null && columns.length>0){
        		            		String vlname = columns[0];
            		            	String vlvalue = columns[1];		
            		            	String locale = columns[2];
            		            	String description = columns[3];
            		            	
            		            	FixtureData fixtureData = new FixtureData(vlname, vlvalue, locale, description);
            		            	fixtureData.setUuid(UUIDGenerator.next());
            		            	
            		            	fixtureDataRepository.save(fixtureData);
            		            	counter++;
        		            	}
        		            	
        		            }
        		            cmd.getResults().put("total", counter);
        				}
        			}catch(Exception e){
        				cmd.setError("Failed to import file "+e.getMessage());
        			}
        		}
                
        	}
        	
		}catch (Exception e) {
        	logger.error("failed to perform "+cmd.getAction()+" in "+cmd.getManager()+" due to "+e.getMessage());
            cmd.setError(e.getLocalizedMessage());
        }
        
        return cmd;
	}

	@Override
	public FixtureData transformRequestToFixtureData(HttpServletRequest request) throws Exception {
		String uuid = request.getParameter("uuid");
		FixtureData fixtureData;
		
		try {
			
			if (uuid!=null && !uuid.isEmpty()) {
				fixtureData = (FixtureData)fixtureDataRepository.findById(uuid, this.site);
				
				fixtureData = (FixtureData)this.fillInValueByRequest(fixtureData, request);
	        	
	        } else {
	        	fixtureData = new FixtureData();
	        	
	        	fixtureData = (FixtureData)this.fillInValueByRequest(fixtureData, request);
	        	
	        	fixtureData.setSite(this.site);
	        }    
	        
			fixtureData = (FixtureData)this.fillInConcurrencyInfo(fixtureData, request);   
    				
			//bean validation
	        abstractBeanValidator.validate(IAbstractBeanValidator.VTYPE_EMPTY, "vlname & vlvalue & locale", fixtureData);

		}catch(BeanValidateException bve) {
			throw new SystemServiceException(bve.getMessage());
		}

		return fixtureData;
	}

	public IFixtureDataRepository getFixtureDataRepository() {
		return fixtureDataRepository;
	}

	public void setFixtureDataRepository(IFixtureDataRepository fixtureDataRepository) {
		this.fixtureDataRepository = fixtureDataRepository;
	}

	public IAbstractBeanValidator getAbstractBeanValidator() {
		return abstractBeanValidator;
	}

	public void setAbstractBeanValidator(
			IAbstractBeanValidator abstractBeanValidator) {
		this.abstractBeanValidator = abstractBeanValidator;
	}

	public Integer getMaxLimit() {
		return maxLimit;
	}

	public void setMaxLimit(Integer maxLimit) {
		this.maxLimit = maxLimit;
	}

}
