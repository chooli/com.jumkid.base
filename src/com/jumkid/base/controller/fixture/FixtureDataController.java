package com.jumkid.base.controller.fixture;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.jumkid.base.model.Command;
import com.jumkid.base.model.ServiceSession;
import com.jumkid.base.model.fixture.FixtureData;
import com.jumkid.base.model.fixture.IFixtureDataService;

@Controller
public class FixtureDataController {

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private IFixtureDataService fixtureDataService;
	
	/**
	 * 
	 * @param uuid
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/fixturedata/load/{uuid}", method=RequestMethod.GET, produces={"application/json; charset=UTF-8"})
	@ResponseBody
    public String load(@PathVariable("uuid") String uuid, HttpServletRequest request){
		ServiceSession sSession = new ServiceSession(false);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("uuid", uuid);
		
		try {
			Command cmd = fixtureDataService.execute( new Command("fixtureDataManager", "load", params) );
						
			return sSession.wrapCommand(cmd, "fixturedata", false).getJsonResult();
			
		} catch (Exception e) {
	       	sSession.setErrors( e.getMessage()!=null?e.getMessage():e.getClass().getName() );
	    }
		
		return sSession.toServiceJSONResult();
		
	}
	
	/**
	 * 
	 * @param keyword
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="/fixturedata/search", method=RequestMethod.POST, produces={"application/json; charset=UTF-8"})
	@ResponseBody
    public String search(HttpServletRequest request){
		ServiceSession sSession = new ServiceSession(false);
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		String keyword = request.getParameter("keyword");
		String facetField = request.getParameter("facetField");
		String facetPrefix = request.getParameter("facetPrefix");
		String locale = request.getParameter("locale");
		String _start = request.getParameter("start");
		Integer start = _start!=null ? Integer.parseInt(_start):0;
		String _limit = request.getParameter("limit");
		Integer limit = _limit!=null ? Integer.parseInt(_limit):20;
		
		params.put("keyword", keyword);
		params.put("facetField", facetField);
		params.put("facetPrefix", facetPrefix);
		params.put("locale", locale);
		params.put("start", start);
		params.put("limit", limit);
		
		try {
			Command cmd = fixtureDataService.execute( new Command("fixtureDataManager", "search", params) );
			if(facetField!=null && !facetField.isEmpty()){
				FacetPage<FixtureData> page = (FacetPage<FixtureData>)cmd.getResults().get("page");
				cmd.addResults("fixturedatas", page.getContent());

				sSession.wrapPage(page, start);
			}else{
				Page<FixtureData> page = (Page<FixtureData>)cmd.getResults().get("page");
				cmd.addResults("fixturedatas", page.getContent());
				
				sSession.wrapPage(page, start);
			}
						
			return sSession.wrapCommand(cmd, "fixturedatas", true).getJsonResult();
			
		} catch (Exception e) {
        	sSession.setErrors( e.getMessage()!=null?e.getMessage():e.getClass().getName() );
        }
			
		return sSession.toServiceJSONResult();
	}
	
	@RequestMapping(value="/fixturedata/save", method=RequestMethod.POST, produces={"application/json; charset=UTF-8"})
	@ResponseBody
	public String save(HttpServletRequest request){
		ServiceSession sSession = new ServiceSession(request);
		
		HashMap<String, Object> params = new HashMap<String, Object>();
				        
        try{            
        	FixtureData fixtureData = fixtureDataService.transformRequestToFixtureData(request);
        	params.put("fixturedata", fixtureData);
        	
        	Command cmd = fixtureDataService.execute( new Command("fixtureDataManager", "save", params) );
        	
            return sSession.wrapCommand(cmd, "fixturedata", false).getJsonResult();
            
        } catch (Exception e) {
        	sSession.setErrors(e.getLocalizedMessage());
        }
        
        return sSession.toServiceJSONResult();
		
	}
	
	@RequestMapping(value="/fixturedata/remove/{uuid}", method=RequestMethod.DELETE, produces={"application/json; charset=UTF-8"})
	@ResponseBody
	public String remove(@PathVariable("uuid") String uuid, HttpServletRequest request){
		ServiceSession sSession = new ServiceSession(false);
		
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("uuid", uuid);
				        
        try{
        	Command cmd = fixtureDataService.execute(new Command("fixtureDataManager", "delete", params));
        	
        	return sSession.wrapCommand(cmd).getJsonResult();
        	
        }catch (Exception e) {
        	sSession.setErrors(e.getLocalizedMessage());
        }
        return sSession.toServiceJSONResult();
	}

	public IFixtureDataService getFixtureDataService() {
		return fixtureDataService;
	}

	public void setFixtureDataService(IFixtureDataService fixtureDataService) {
		this.fixtureDataService = fixtureDataService;
	}
	
	/**
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/fixturedata/import", method=RequestMethod.POST)
	@ResponseBody
    public String upload(@RequestParam("file") CommonsMultipartFile file, HttpServletRequest request){
		ServiceSession sSession = new ServiceSession(request);
		HashMap<String, Object> params = new HashMap<String, Object>();
       			
		try {
            byte[] bytes = file.getBytes();                          
            params.put("file", bytes);
            
            Command cmd = fixtureDataService.execute( new Command("fixtureDataManager", "import", params) );
            int total = (int)cmd.getResults().get("total");
            
            sSession.setTotalRecords(new Long(total));            
            
        } catch (Exception e) {
        	sSession.setErrors(e.getLocalizedMessage());
        }
		return sSession.toServiceJSONResult();
    }
	
}
