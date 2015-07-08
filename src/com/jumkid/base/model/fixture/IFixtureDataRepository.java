package com.jumkid.base.model.fixture;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.solr.core.query.result.FacetPage;

public interface IFixtureDataRepository {

    public String getSite();
		
	public FixtureData findById(String uuid, String site);
	
	public Page<FixtureData> findByName(String name, String site, Pageable pager);
		
	public Page<FixtureData> findByNameAndValue(String name, String value, String locale, String site, Pageable pager);
	
	public Page<FixtureData> findByText(String keyword, String site, Pageable pager);
	
	public FacetPage<FixtureData> findByFacetField(String fieldName, String prefix, String keyword, 
													String locale, String site, Pageable pager);
	
	public FixtureData save(FixtureData fixtureData);
	
	public void remove(FixtureData fixtureData);

	public void remove(String uuid, String site);
	
}
