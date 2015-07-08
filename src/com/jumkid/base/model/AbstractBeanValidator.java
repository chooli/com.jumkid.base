package com.jumkid.base.model;

/* 
 * This software is written by HULUBOY and subject
 * to a contract between HULUBOY and its customer.
 *
 * This software stays property of HULUBOY unless differing
 * arrangements between HULUBOY and its customer apply.
 *
 *
 * (c)2013 HULUBOY All rights reserved.
 *
 * VERSION   | DATE      | DEVELOPER  | DESC
 * -----------------------------------------------------------------
 * 1.0         July2013    chooli       creation
 * 
 *
 */

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import net.astesana.javaluator.AbstractEvaluator;
import net.astesana.javaluator.BracketPair;
import net.astesana.javaluator.Operator;
import net.astesana.javaluator.Parameters;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.jumkid.base.exception.BeanValidateException;
import com.jumkid.base.model.AbstractBean;
import com.jumkid.base.util.Constants;
import com.jumkid.base.util.Formatter;

public class AbstractBeanValidator extends AbstractEvaluator<String> implements IAbstractBeanValidator{
	
	protected final Log logger = LogFactory.getLog(AbstractBeanValidator.class);
	
	DuplicatedRecordValidator duplicatedRecordValidator;	
	
	/** The logical AND operator.*/
	final static Operator NOT = new Operator("!", 1, Operator.Associativity.RIGHT, 1);
	/** The logical AND operator.*/
	final static Operator AND = new Operator("&", 2, Operator.Associativity.LEFT, 3);
	/** The logical OR operator.*/
    final static Operator OR = new Operator("|", 2, Operator.Associativity.LEFT, 2);
	
	private static final Parameters PARAMETERS;
		
	static {
	  // Create the evaluator's parameters
	  PARAMETERS = new Parameters();
	  // Add the supported operators
	  PARAMETERS.add(NOT);
	  PARAMETERS.add(AND);
	  PARAMETERS.add(OR);
	  // Add the parentheses
	  PARAMETERS.addExpressionBracket(BracketPair.PARENTHESES);
	}
	
	public AbstractBeanValidator(){
		super(PARAMETERS);
	}
	
	/**
	 * 
	 */
	public AbstractBeanValidator validate(String vtype, String expression, AbstractBean bean) throws BeanValidateException{
		List<String> invalidFields = new ArrayList<String>();
		
		String logicGates = "";
		try{
			StringTokenizer patternToken = new StringTokenizer(expression, "()&|!", true);
			
			String token;
			boolean isValid = true;
			while ((token = getNextToken(patternToken)) != null) {
				
				if ("()&|!".contains(token)){
		        	logicGates += token;
		        }else{
		        	
		        	if(VTYPE_EMPTY.equals(vtype)){
						Object value = bean.getClass().getMethod("get" + Formatter.getInstance().capitalize(token)).invoke(bean);
						if(value==null || value.toString().isEmpty()) isValid = false;
						else isValid = true;
					}
		        	
		        	if (isValid){	        	
			        	logicGates += "T";
			        }	        	
			        else{
			        	invalidFields.add(token);
			        	logicGates += "F";
			        }
		        }
				
			}			
			
			
		}catch(Exception e){
			logger.error("Failed to perform validation "+e.getMessage());
			throw new BeanValidateException("bean validation is failed");
		}
		
		String result = evaluate(logicGates, new ArrayList<String>());
		logger.debug("validate result = "+result);
		if(result.endsWith("false") || "F".equals(result)) throw new BeanValidateException("invalid fields "+invalidFields.toString());
		
		return this;
	}
	
	/**
	 * using reflection to validate duplicated record
	 * 
	 */
	public AbstractBeanValidator validate(String vtype, String propertyName, 
			AbstractBean bean, ICommonBeanDao objectDAO) throws BeanValidateException{
		try{
			Method method = bean.getClass().getMethod("get" + Formatter.getInstance().capitalize(propertyName));
			Object value = method.invoke(bean);
			
			return validate(vtype, propertyName, value, objectDAO);
						
		}catch(BeanValidateException bve){
			throw bve;
		}catch(Exception e){
			logger.error("Failed to perform validation "+e.getMessage());
		}
		
		return this;
	}
	
	public AbstractBeanValidator validate(String vtype, String propertyName,
			Object beanValue, ICommonBeanDao objectDAO) throws BeanValidateException {
		
		this.validateByDetachedCriteria(vtype, propertyName, beanValue, (ICommonBeanDao)objectDAO);
			
		return this;
		
	}

//	@Override
//	public AbstractBeanValidator validate(String vtype, String propertyName,
//			Object beanValue, Object objectDAO) throws BeanValidateException {
//		try{
//			if(VTYPE_DUPLICATE.equals(vtype)){
//				//use reflection to retrive abstract bean from DAO
//				Class<?> argType;
//				if(beanValue instanceof Integer){
//					argType = Integer.class;
//				}else
//				if(beanValue instanceof Long){
//					argType = Long.class;
//				}else
//				if(beanValue instanceof Double){
//					argType = Double.class;
//				}else{
//					argType = String.class;
//				}
//				
//				
//				Method method = objectDAO.getClass().getMethod("loadBy" + Formatter.getInstance().capitalize(propertyName), argType);
//				logger.debug("method: "+method.toGenericString());
//				
//				AbstractBean existingBean = (AbstractBean)method.invoke(objectDAO, beanValue);
//				
//				if(existingBean!=null){
//					logger.debug("existing bean: "+existingBean.toJSON());
//					Object existingValue = existingBean.getClass().getMethod("get" + Formatter.getInstance().capitalize(propertyName)).invoke(existingBean);
//					logger.debug("existing value = "+existingValue);
//					
//					if(beanValue.equals(existingValue)) throw new BeanValidateException(Constants.CMSG_DUPLICATED_REC);
//				}
//				
//				
//			}else
//			if(VTYPE_EMPTY.equals(vtype)){
//				
//			}else{
//				throw new BeanValidateException(Constants.CMSG_INVALID_VTYPE);
//			}
//			
//		}catch(BeanValidateException bve){
//			throw bve;
//		}catch(Exception e){
//			logger.error("Failed to perform validation "+e.getMessage());
//		}
//		
//		return this;
//		
//	}
	
	@SuppressWarnings({ "unchecked" })
	private void validateByDetachedCriteria(String vtype, String propertyName, Object beanValue, ICommonBeanDao objectDAO) 
			throws BeanValidateException{
		
		DetachedCriteria criteria = DetachedCriteria.forClass(objectDAO.getClass())
								.add(Restrictions.eq(propertyName, beanValue));
		
		List<AbstractBean> hits = objectDAO.findByCriteria(criteria);
		if(hits!=null && hits.size()==1) {
			if(VTYPE_DUPLICATE.equals(vtype)){
				throw new BeanValidateException(Constants.CMSG_DUPLICATED_REC);
			}
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected String evaluate(Operator operator, Iterator<String> operands,
	      Object evaluationContext) {		
	    List<String> tree = (List<String>) evaluationContext;	
	    String o1 = operands.hasNext()?operands.next():"";
	    String o2 = operands.hasNext()?operands.next():"";
	    Boolean result;
	    if (operator == OR) {
	      result = getValue(o1) || getValue(o2);
	    } else if (operator == AND) {
	      result = getValue(o1) && getValue(o2);
	    }  else if (operator == NOT) {
	      result = !getValue(o1);
	    } else {
	      throw new IllegalArgumentException();
	    }
	    String eval = "("+o1+" "+operator.getSymbol()+" "+o2+")="+result;
	    tree.add(eval);
	    return eval;
	}
	
	/**
	 * 
	 * @param literal
	 * @return
	 */
	private boolean getValue(String literal) {
	    if ("T".equals(literal) || literal.endsWith("=true")) return true;
	    else if ("F".equals(literal) || literal.endsWith("=false")) return false;
	    throw new IllegalArgumentException("Unknown literal : "+literal);
	}
	
	/**
	 * 
	 * @param token
	 * @return
	 */
	private String getNextToken(StringTokenizer token) {
	    while (token.hasMoreTokens()) {
	        String tok = token.nextToken().trim();	        
	        if (tok.length() > 0)
	            return tok;
	    }
	    return null;
	}

	@Override
	protected String toValue(String literal, Object evaluationContext) {
		return literal;
	}
	
}

