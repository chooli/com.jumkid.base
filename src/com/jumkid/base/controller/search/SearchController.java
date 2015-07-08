package com.jumkid.base.controller.search;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jumkid.base.model.Command;
import com.jumkid.base.model.ServiceSession;
import com.jumkid.base.search.IModuleEntrySearchService;
import com.jumkid.base.search.SearchableModuleEntry;

@Controller
public class SearchController{
	
	protected final Log logger = LogFactory.getLog(this.getClass());	
	
	private IModuleEntrySearchService moduleEntrySearchService;

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/search", method=RequestMethod.POST, produces={"application/json; charset=UTF-8"})
	@ResponseBody
	public String search(HttpServletRequest request){
		HashMap<String, Object> params = new HashMap<String, Object>();
		ServiceSession sSession = new ServiceSession(request);
		
		String keyword = request.getParameter("keyword");
		String module = request.getParameter("module");
		String _start = request.getParameter("start");
		Integer start = _start==null?0:Integer.valueOf(_start);
		String _limit = request.getParameter("limit");
		Integer limit = _limit==null?20:Integer.valueOf(_limit);
		
		params.put("keyword", keyword);
		params.put("module", module);
		params.put("start", start);
		params.put("limit", limit);
		
		try {
			Command cmd = moduleEntrySearchService.execute(new Command("searchmanager", "search", params));
						
			Page<SearchableModuleEntry> page = (Page<SearchableModuleEntry>)cmd.getResults().get("page");
			cmd.addResults("moduleentries", page.getContent());
			
			sSession.wrapPage(page, start);
			sSession.setSuccess(true);
			
			return sSession.wrapCommand(cmd, "moduleentries", true).getJsonResult();
			
		} catch (Exception e) {
	       	sSession.setErrors( e.getMessage()!=null?e.getMessage():e.getClass().getName() );
	    }
		
		return sSession.toServiceJSONResult();
		
	}

	public IModuleEntrySearchService getModuleEntrySearchService() {
		return moduleEntrySearchService;
	}

	public void setModuleEntrySearchService(
			IModuleEntrySearchService moduleEntrySearchService) {
		this.moduleEntrySearchService = moduleEntrySearchService;
	}

}
