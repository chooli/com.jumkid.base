package com.jumkid.base.search;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.jumkid.base.model.AbstractCommandService;
import com.jumkid.base.model.Command;

public class ModuleEntrySearchService extends AbstractCommandService implements IModuleEntrySearchService{
	
	private SolrSearchableModuleEntryRepository moduleEntrySearchRepository;
	
	@Override
	public Command execute(Command cmd) throws Exception {
		super.execute(cmd);
		
		try{
        	if (isManager("searchmanager")) {
        		if(isAction("search")) {
        			String site = (String)cmd.getParams().get("site");
        			String keyword = (String)cmd.getParams().get("keyword");
                    String module = (String)cmd.getParams().get("module");
                    Integer start = (Integer)cmd.getParams().get("start");
                    if(start==null) start = 0;
                    Integer limit = (Integer)cmd.getParams().get("limit");
                    if(limit==null) limit = 20;
                    
                    Page<SearchableModuleEntry> page = null;
                    Pageable pager =new PageRequest(start, limit); 
                    if(module!=null && !module.isEmpty()){
                    	page = moduleEntrySearchRepository.findByModuleAndContent(site, module, keyword, pager);
                    }else{
                    	page = moduleEntrySearchRepository.findByContent(site, keyword, pager);
                    }
                    
                    cmd.addResults("page", page);
                
                } 
        		
        	}
		} catch(Exception e){
        	logger.error("failed to perform "+cmd.getAction()+" in "+cmd.getManager()+" due to "+e.getMessage());
            cmd.setError(e.getLocalizedMessage());
        }
        
        return cmd;

	}

	public SolrSearchableModuleEntryRepository getModuleEntrySearchRepository() {
		return moduleEntrySearchRepository;
	}

	public void setModuleEntrySearchRepository(
			SolrSearchableModuleEntryRepository moduleEntrySearchRepository) {
		this.moduleEntrySearchRepository = moduleEntrySearchRepository;
	}
        	

}
