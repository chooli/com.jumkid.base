package com.jumkid.base.model.event;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.jumkid.base.model.AbstractHibernateDAO;
import com.jumkid.base.util.Constants;

public class EventDao extends AbstractHibernateDAO<Event> implements IEventDao {

	public EventDao() {
		super(Event.class);
	}
	
	@Override
	public DetachedCriteria createDetachedCriteriaForModule(String module) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);        
        
		if(module!=null && !module.trim().isEmpty()){
        	criteria.add(Restrictions.eq("module", module));
        }
        
        criteria.add(Restrictions.eq("fired", false));
        
        return criteria;
	}
	
	@Override
	public DetachedCriteria createDetachedCriteriaForTarget(String targetRefId) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Event.class);        

        if(targetRefId!=null && !targetRefId.trim().isEmpty()){
        	criteria.add(Restrictions.eq("targetRefId", targetRefId));
        }
        
        criteria.add(Restrictions.eq("fired", false));
        
        return criteria;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Event> getEntityList(DetachedCriteria criteria,
			Integer start, Integer limit, String sortCriteria, String sortOrder)
			throws Exception {
		List<Event> results = null;        
		
		//not fired only
		criteria.add(Restrictions.eq("fired", Boolean.FALSE));
		
        // sorting
        if (sortCriteria!=null && Constants.SORTORDER_ASC.equals(sortOrder)) criteria.addOrder(Order.asc(sortCriteria));
        else if (sortCriteria!=null && Constants.SORTORDER_DESC.equals(sortOrder)) criteria.addOrder(Order.desc(sortCriteria));
        else criteria.addOrder(Order.desc("createdOn"));
                
        // paging (optional)
        results = (start!=null && limit!=null) ? this.findByCriteria(criteria, start, limit) : this.findByCriteria(criteria);            
        
        return results;
	}

}
