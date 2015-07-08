package com.jumkid.base.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.repository.SolrCrudRepository;

public interface SolrSearchableModuleEntryRepository extends
		SolrCrudRepository<SearchableModuleEntry, String> {

	public SearchableModuleEntry findById(String id, String site);
	
	public SearchableModuleEntry findByModuleAndInternalId(String site, String module, String internalId);
	
	public Page<SearchableModuleEntry> findByModuleAndContent(String site, String module, String content, Pageable pager);
	
	public Page<SearchableModuleEntry> findByContent(String site, String content, Pageable pager);
	
	
}
