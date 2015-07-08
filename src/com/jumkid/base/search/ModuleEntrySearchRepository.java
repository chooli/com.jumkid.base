package com.jumkid.base.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

public class ModuleEntrySearchRepository extends SimpleSolrRepository<SearchableModuleEntry, String>
		implements SolrSearchableModuleEntryRepository {

	private String site;
	
	@Override
	public SearchableModuleEntry findById(String id, String _site) {
		
		_site = (_site==null) ? this.getSite() : _site;
		
		Query query = new SimpleQuery(new Criteria("id").is(id))
							.addCriteria(new Criteria("site").is(_site));
		return getSolrOperations().queryForObject(query, SearchableModuleEntry.class);
	}

	@Override
	public SearchableModuleEntry findByModuleAndInternalId(String _site,
			String module, String internalId) {
		
		_site = (_site==null) ? this.getSite() : _site;
		
		Query query = new SimpleQuery(new Criteria("internal_id").is(internalId))
							.addCriteria(new Criteria("site").is(_site))
							.addCriteria(new Criteria("module").is(module));
				
		return getSolrOperations().queryForObject(query, SearchableModuleEntry.class);
	}

	@Override
	public Page<SearchableModuleEntry> findByModuleAndContent(String _site,
			String module, String content, Pageable pager) {
		
		_site = (_site==null) ? this.getSite() : _site;
		
		Query query = new SimpleQuery(new Criteria("content").contains(content))
							.addCriteria(new Criteria("site").is(_site))
		                    .addCriteria(new Criteria("module").is(module));
		if(pager!=null) {
			query.setPageRequest(pager);
		}
				
		return getSolrOperations().queryForPage(query, SearchableModuleEntry.class);
	}
	
	@Override
	public Page<SearchableModuleEntry> findByContent(String _site,
			String content, Pageable pager) {
		
		_site = (_site==null) ? this.getSite() : _site;
		
		Query query = new SimpleQuery(new Criteria("text").contains(content))
							.addCriteria(new Criteria("site").is(_site));
		if(pager!=null) {
			query.setPageRequest(pager);
		}
				
		return getSolrOperations().queryForPage(query, SearchableModuleEntry.class);
	}
	
	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

}
