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
 * VERSION   | DATE      | DEVELOPER  | DESC
 * -----------------------------------------------------------------
 * 3.0        May2013      chooli      creation
 * 
 *
 */


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jumkid.base.exception.ConcurrentModificationException;
import com.jumkid.base.util.Constants;
import com.jumkid.base.util.SearchCriteria;

@Repository
@Transactional
public abstract class AbstractHibernateDAO<T extends Serializable> implements ICommonBeanDao{
	
	protected final Log logger = LogFactory.getLog(this.getClass());

	@SuppressWarnings("rawtypes")
	protected final Class entityClass;
    @Autowired
    SessionFactory sessionFactory;
 
    public AbstractHibernateDAO(@SuppressWarnings("rawtypes") final Class entityClass) {
        this.entityClass = entityClass;
    }
    
	public CommonBean load(final java.lang.Integer id)
    {        
        return (CommonBean)this.getById(id);
    }
	
	@SuppressWarnings("unchecked")
	public List<T> load(DetachedCriteria dc,Integer start, Integer limit, String sortCriteria, String sortOrder){
    	
    	// sorting        
        if (sortCriteria!=null && Constants.SORTORDER_ASC.equals(sortOrder)) dc.addOrder(Order.asc(sortCriteria));
        else if (sortCriteria!=null && Constants.SORTORDER_DESC.equals(sortOrder)) dc.addOrder(Order.desc(sortCriteria));         
        
        return this.findByCriteria(dc, start, limit);
		
    }    
 
    @SuppressWarnings("unchecked")
	public T getById(final Integer id) {
        if (id == null){
            throw new IllegalArgumentException("'id' cannot be null");
        }
        return (T) this.getCurrentSession().get(this.entityClass, id);
    }
 
    @SuppressWarnings("unchecked")
    @Override
	public List<T> loadAll() {
        return this.getCurrentSession()
                .createQuery("from " + this.entityClass.getName()).list();
    }
    
    @SuppressWarnings("unchecked")
	@Override
    public List<T> loadAll(Integer start, Integer limit){
    	DetachedCriteria criteria = DetachedCriteria.forClass(this.entityClass);
    	return this.findByCriteria(criteria, start, limit);
    }
    
    /*
     * (non-Javadoc)
     * @see com.jumkid.base.model.corporateaccount.category.ICorporateAccountCategoryDao#create(com.jumkid.base.model.corporateaccount.category.CorporateAccountCategory)
     */
	public CommonBean create(final CommonBean bean)
    {
    	if (bean == null)
        {
            throw new IllegalArgumentException("bean cannot be null");
        }        
    	bean.setCreatedOn( new Timestamp(Calendar.getInstance().getTimeInMillis()) );   	       	
        this.getCurrentSession().persist(bean);   
        
        this.getCurrentSession().flush();
        
        return bean;
    }
	
	/**
	 * 
	 * @param bean
	 * @throws ConcurrentModificationException
	 */
	public void update(CommonBean bean) throws ConcurrentModificationException{
	      
        if (bean == null) 
        {
            throw new IllegalArgumentException("bean cannot be null");
        }
    		
        DetachedCriteria criteria = DetachedCriteria.forClass(this.entityClass);
    	  criteria.add(Restrictions.eq("id", bean.getId()))
    			.setProjection(Projections.property("modifiedOn"));
    	
    	Timestamp persistentModifiedOn = (Timestamp)this.findByCriteria(criteria).get(0);            
        if (persistentModifiedOn==null || (bean.getModifiedOn()!=null && persistentModifiedOn.equals(bean.getModifiedOn())) ) {
        	bean.setModifiedOn( new Timestamp(Calendar.getInstance().getTimeInMillis()));
            this.getCurrentSession().merge(bean);
        } else {
            throw new ConcurrentModificationException(Constants.CMSG_CONCURRENT_MODIFICATION);
        }

        this.getCurrentSession().flush();
        this.getCurrentSession().clear();
    }
 
    public void delete(final T entity) {
    	if (entity == null){
            throw new IllegalArgumentException("'entity' cannot be null");
        }
        this.getCurrentSession().delete(entity);
    }
 
    public void deleteById(final Integer entityId) {
        final T entity = this.getById(entityId);
        if (entity == null){
            throw new IllegalArgumentException("'entity' cannot be null");
        }
        this.delete(entity);
    }
 
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
 
    protected final Session getCurrentSession() {
        return this.sessionFactory.getCurrentSession();
    }
    
    @Override
    @SuppressWarnings({ "rawtypes" })
	public List findByCriteria(DetachedCriteria dc) {    	
        return dc.getExecutableCriteria(this.getCurrentSession()).list();
    }
    
    @Override
    @SuppressWarnings({ "rawtypes" })
	public List findByCriteria(DetachedCriteria dc, Integer start, Integer limit) {
    	Criteria criteria = dc.getExecutableCriteria(this.getCurrentSession());
    	if(start!=null) criteria.setFirstResult(start);
    	if(limit!=null) criteria.setMaxResults(limit);
        return criteria.list();
    }
    
    @Override
    @SuppressWarnings("unchecked")
	public List<T> find(String hql){
    	Query query = this.getCurrentSession().createQuery(hql);
    	return query.list();
    }
    
	@SuppressWarnings("hiding")
	@Override
	public <T> int getRowCount(DetachedCriteria criteria, Class<T> objectClass) {
		if(criteria==null) criteria = DetachedCriteria.forClass(objectClass); 
    	
        Criteria crit = criteria.getExecutableCriteria(this.getCurrentSession());
        int totalCount = ((Long) crit.setProjection(Projections.rowCount()).uniqueResult()).intValue();
        return totalCount;
	}

	@Override
	public void deactivate(Integer id) throws IllegalArgumentException{
		if (id == null) {
            throw new IllegalArgumentException(
                "id cannot be null");
        }
		CommonBean entity = (CommonBean)this.getById(id);
		this.deactivate(entity);
	}
    
    /**
     * Set status to disable the object
     * 
     * 
     * @see com.jumkid.base.model.corporateaccount.category.ICorporateAccountCategoryDao#deactivate(com.jumkid.base.model.corporateaccount.category.CorporateAccountCategory)
     */
    public void deactivate(CommonBean entity) throws IllegalArgumentException{    	
        if (entity == null) {
            throw new IllegalArgumentException(
                "entity cannot be null");
        }
        logger.debug("deactivate entity "+entity.getClass().getName());
                
        this.getCurrentSession().merge(entity);
    }
    
    /*
     * (non-Javadoc)
     * @see com.jumkid.base.model.ICommonBeanDao#remove(java.lang.Integer)
     */
    public void remove(Integer id) throws IllegalArgumentException{
    	this.deleteById(id);
    }
    
    @SuppressWarnings("unchecked")
	public void remove(CommonBean entity) throws IllegalArgumentException {
        this.delete((T) entity);
    }   
    
    /**
     * create criteria for list of search criteria
     * SearchCriteria can netting a list of searchCriteria 
     * and the search will perform Conjunction (AND) query among the netting searchCriteria  
     * 
     * @param locale
     * @param searchCriterias
     * @return
     */
    public DetachedCriteria createDetachedCriteria(String locale, List<SearchCriteria> searchCriterias, 
    		@SuppressWarnings("rawtypes") Class objectClass) {    	    	
    	// init filter and sort criteria
        DetachedCriteria criteria = DetachedCriteria.forClass(objectClass);                     
            
        if(locale!=null) {
        	criteria.createAlias("localizedData","localizedData");
        	criteria.add(Restrictions.eq("localizedData.locale", locale));
        }
        
        if (searchCriterias!=null) {
        	TreeSet<String> distinctSet = new TreeSet<String>();  //contain distinct alias names
        	Disjunction disjunction = Restrictions.disjunction();
        	
        	for(int i=0;i<searchCriterias.size();i++){
            	SearchCriteria search = (SearchCriteria)searchCriterias.get(i);
            	//extra alias
            	String fieldname = search.getFieldName();
            	if(fieldname.indexOf(".")!=-1){
            		String[] fs = fieldname.split("[.]");
            		String alias = "";
            		String mapping = "";            		
            		for(int j=0;j<fs.length-1;j++){
            			if(alias.equals("")) {
            				alias = fs[j];
            				mapping = fs[j];
            			}else{
            				alias = alias + fs[j];
            				mapping = mapping + "." + fs[j];
            			}
            			if(!alias.equals("localizedData") && !distinctSet.contains(alias)) criteria.createAlias(mapping, alias);
            			distinctSet.add(alias);
            		}            		
            		search.setFieldName(alias+"."+fs[fs.length-1]);
            	}            	            	
            	
            	if(search.getOperator().equals("=")){ 
            		if(search.getCondition().equals("or")) {
            			if(search.hasNetting()){
            				ArrayList<SearchCriteria> nettingCriteriaList = search.getNettingCriteriaList();
            				Conjunction conj = Restrictions.conjunction();
            				conj.add(Restrictions.eq(search.getFieldName(), search.getValue()));
            				for(int j=0;j<nettingCriteriaList.size();j++){
            					SearchCriteria nettingSearch = (SearchCriteria)nettingCriteriaList.get(j);            					
            					conj.add(Restrictions.eq(nettingSearch.getFieldName(), nettingSearch.getValue()));				
            				}
            				disjunction.add(conj);
            			}else{
            				disjunction.add(Restrictions.eq(search.getFieldName(), search.getValue()));
            			}
            		}
            		else criteria.add(Restrictions.eq(search.getFieldName(), search.getValue()));            		            	
            	}else if(search.getOperator().equals("!=")){
            		if(search.getCondition().equals("or")) {
            			if(search.hasNetting()){
            				ArrayList<SearchCriteria> nettingCriteriaList = search.getNettingCriteriaList();
            				Conjunction conj = Restrictions.conjunction();
            				conj.add(Restrictions.ne(search.getFieldName(), search.getValue()));
            				for(int j=0;j<nettingCriteriaList.size();j++){
            					SearchCriteria nettingSearch = (SearchCriteria)nettingCriteriaList.get(j);            					
            					conj.add(Restrictions.ne(nettingSearch.getFieldName(), nettingSearch.getValue()));				
            				}
            				disjunction.add(conj);
            			}else{
            				disjunction.add(Restrictions.ne(search.getFieldName(), search.getValue()));
            			}
            		}
            		else criteria.add(Restrictions.ne(search.getFieldName(), search.getValue()));
            	}else if(search.getOperator().equals("<")){
            		if(search.getCondition().equals("or")) {
            			if(search.hasNetting()){
            				ArrayList<SearchCriteria> nettingCriteriaList = search.getNettingCriteriaList();
            				Conjunction conj = Restrictions.conjunction();
            				conj.add(Restrictions.lt(search.getFieldName(), search.getValue()));
            				for(int j=0;j<nettingCriteriaList.size();j++){
            					SearchCriteria nettingSearch = (SearchCriteria)nettingCriteriaList.get(j);            					
            					conj.add(Restrictions.lt(nettingSearch.getFieldName(), nettingSearch.getValue()));				
            				}
            				disjunction.add(conj);
            			}else{
            				disjunction.add(Restrictions.lt(search.getFieldName(), search.getValue()));
            			}
            		}
            		else criteria.add(Restrictions.lt(search.getFieldName(), search.getValue()));
            	}else if(search.getOperator().equals(">")){
            		if(search.getCondition().equals("or")) {
            			if(search.hasNetting()){
            				ArrayList<SearchCriteria> nettingCriteriaList = search.getNettingCriteriaList();
            				Conjunction conj = Restrictions.conjunction();
            				conj.add(Restrictions.gt(search.getFieldName(), search.getValue()));
            				for(int j=0;j<nettingCriteriaList.size();j++){
            					SearchCriteria nettingSearch = (SearchCriteria)nettingCriteriaList.get(j);            					
            					conj.add(Restrictions.gt(nettingSearch.getFieldName(), nettingSearch.getValue()));				
            				}
            				disjunction.add(conj);
            			}else{
            				disjunction.add(Restrictions.gt(search.getFieldName(), search.getValue()));
            			}
            		}
            		else criteria.add(Restrictions.gt(search.getFieldName(), search.getValue()));
            	}else if(search.getOperator().equals(">=")){
            		if(search.getCondition().equals("or")) {
            			if(search.hasNetting()){
            				ArrayList<SearchCriteria> nettingCriteriaList = search.getNettingCriteriaList();
            				Conjunction conj = Restrictions.conjunction();
            				conj.add(Restrictions.ge(search.getFieldName(), search.getValue()));
            				for(int j=0;j<nettingCriteriaList.size();j++){
            					SearchCriteria nettingSearch = (SearchCriteria)nettingCriteriaList.get(j);            					
            					conj.add(Restrictions.ge(nettingSearch.getFieldName(), nettingSearch.getValue()));				
            				}
            				disjunction.add(conj);
            			}else{
            				disjunction.add(Restrictions.ge(search.getFieldName(), search.getValue()));
            			}
            		}
            		else criteria.add(Restrictions.ge(search.getFieldName(), search.getValue()));
            	}else if(search.getOperator().equals("<=")){
            		if(search.getCondition().equals("or")) {
            			if(search.hasNetting()){
            				ArrayList<SearchCriteria> nettingCriteriaList = search.getNettingCriteriaList();
            				Conjunction conj = Restrictions.conjunction();
            				conj.add(Restrictions.le(search.getFieldName(), search.getValue()));
            				for(int j=0;j<nettingCriteriaList.size();j++){
            					SearchCriteria nettingSearch = (SearchCriteria)nettingCriteriaList.get(j);            					
            					conj.add(Restrictions.le(nettingSearch.getFieldName(), nettingSearch.getValue()));				
            				}
            				disjunction.add(conj);
            			}else{
            				disjunction.add(Restrictions.le(search.getFieldName(), search.getValue()));
            			}
            		}
            		else criteria.add(Restrictions.le(search.getFieldName(), search.getValue()));
            	}else if(search.getOperator().equals("like")){
            		if(search.getCondition().equals("or")) {
            			if(search.hasNetting()){
            				ArrayList<SearchCriteria> nettingCriteriaList = search.getNettingCriteriaList();
            				Conjunction conj = Restrictions.conjunction();
            				conj.add(Restrictions.like(search.getFieldName(), search.getValue()).ignoreCase());
            				for(int j=0;j<nettingCriteriaList.size();j++){
            					SearchCriteria nettingSearch = (SearchCriteria)nettingCriteriaList.get(j);            					
            					conj.add(Restrictions.like(nettingSearch.getFieldName(), nettingSearch.getValue()).ignoreCase());				
            				}
            				disjunction.add(conj);
            			}else{
            				disjunction.add(Restrictions.like(search.getFieldName(), search.getValue()).ignoreCase());
            			}
            		}
            		else criteria.add(Restrictions.like(search.getFieldName(), search.getValue()));
            	}else if(search.getOperator().equals("in")){
            		String value = (String)search.getValue();        		
            		String[] values = value.split(",");            		
            		Disjunction disj = Restrictions.disjunction();
            		for(int j=0;j<values.length;j++) {        			
            			disj.add(Restrictions.like(search.getFieldName(), values[j].trim(), MatchMode.ANYWHERE).ignoreCase());
            		}
            		criteria.add(disj);
            	}            	
            }
        	criteria.add(disjunction);
        }        
        
        return criteria;
    }
    
}
