package com.jumkid.base.model.fixture;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FacetOptions;
import org.springframework.data.solr.core.query.FacetQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFacetQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.repository.support.SimpleSolrRepository;

public class FixtureDataRepository extends SimpleSolrRepository<FixtureData, String>
								implements IFixtureDataRepository {
	
	protected final Log logger = LogFactory.getLog(this.getClass());	
	
	private String site;
	
	private static final Sort sortOrder = new Sort(Sort.Direction.DESC, "vlvalue");		
		
	public FixtureDataRepository(){
		this.setIdFieldName("uuid");
		this.setEntityClass(this.getEntityClass());
	}

	@Override
	public FixtureData findById(String uuid, String _site) {
		_site = (_site==null) ? this.getSite() : _site;

		Query query = new SimpleQuery(new Criteria(this.getIdFieldName()).is(uuid))
							.addCriteria(new Criteria("site").is(_site));
					
		return getSolrOperations().queryForObject(query, this.getEntityClass());
	}
	
	@Override
	public Page<FixtureData> findByName(String name, String _site, Pageable pager) {
		
		_site = (_site==null) ? this.getSite() : _site;
		
		Query query = new SimpleQuery(new Criteria("vlname").is(name))
							.addCriteria(new Criteria("site").is(_site));

		if(pager!=null) {
			query.setPageRequest(pager);
		}
		query.addSort( sortOrder );
		
		
		return getSolrOperations().queryForPage(query, this.getEntityClass());
	}

	@Override
	public Page<FixtureData> findByNameAndValue(String name, String value, String locale, 
												String _site, Pageable pager) {
		_site = (_site==null) ? this.getSite() : _site;
		
		Query query = new SimpleQuery(value)
							.addCriteria(new Criteria("vlname").is(name))
							.addCriteria(new Criteria("locale").is(locale))
							.addCriteria(new Criteria("site").is(_site));
		
				
		if(pager!=null) {
			query.setPageRequest(pager);
		}
		query.addSort( sortOrder );
		
		return getSolrOperations().queryForPage(query, this.getEntityClass());
	}
	
	@Override
	public Page<FixtureData> findByText(String keyword, String _site, Pageable pager) {
		
		_site = (_site==null) ? this.getSite() : _site;
		
		Query query = new SimpleQuery(keyword)
							.addCriteria(new Criteria("site").is(_site));

		if(pager!=null) {
			query.setPageRequest(pager);
		}
		query.addSort( sortOrder );
		
		
		return getSolrOperations().queryForPage(query, this.getEntityClass());
	}
	
	@Override
	public synchronized FacetPage<FixtureData> findByFacetField(String fieldName, String prefix, String keyword, 
															String locale, String _site, Pageable pager) {
		
		_site = (_site==null) ? this.getSite() : _site;
		
		FacetOptions options = new FacetOptions().addFacetOnField(fieldName);
		//if(prefix!=null && !prefix.isEmpty()) options.setFacetPrefix(prefix);
		
		FacetQuery query = new SimpleFacetQuery( new Criteria("text").contains(keyword) )
				  						.setFacetOptions( options )				  						
				  						.addCriteria(new Criteria("site").is(_site));
		
		if(locale!=null && !locale.isEmpty()) query.addCriteria(new Criteria("locale").is(locale));
		if(prefix!=null && !prefix.isEmpty()) query.addCriteria(new Criteria(fieldName).startsWith(prefix));
		
		if(pager!=null) {
			query.setPageRequest(pager);
		}
		
		FacetPage<FixtureData> page = getSolrOperations().queryForFacetPage( query, this.getEntityClass() );
		
		return page;
	}

	@SuppressWarnings("unchecked")
	@Override
	public FixtureData save(FixtureData fixtureData) {
		this.checkSite(fixtureData);
		
		if(fixtureData!=null){
			fixtureData.setSite(site);
			return super.save(fixtureData);
		}
    	return null;
	}

	@Override
	public void remove(FixtureData fixtureData) {
		this.checkSite(fixtureData);
		
		if(fixtureData!=null){
			delete(fixtureData);
		}
	}

	@Override
	public void remove(String uuid, String _site) {
		if(uuid==null) return;
		
		FixtureData fixtureData = this.findById(uuid, _site);
		
		if(fixtureData!=null){
			this.remove(fixtureData);
		}
	}
	
	private synchronized void checkSite(FixtureData fixtureData){
		if(fixtureData.getSite()==null || fixtureData.getSite().isEmpty()){
			fixtureData.setSite(this.getSite());
		}
	}

	public void setSite(String site) {
		this.site = site;
	}
	
	@Override
	public String getSite() {
		return this.site;
	}

}
