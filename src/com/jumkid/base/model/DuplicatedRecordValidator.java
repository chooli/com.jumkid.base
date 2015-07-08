package com.jumkid.base.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jumkid.base.exception.BeanValidateException;
import com.jumkid.base.model.AbstractBean;
import com.jumkid.base.util.Constants;
import com.jumkid.base.util.Formatter;

public class DuplicatedRecordValidator{
	
	protected final Log logger = LogFactory.getLog(DuplicatedRecordValidator.class);
	
	public void validate(){
		//void
	}
	
	public void validate(AbstractBean bean1, AbstractBean bean2, String property) throws BeanValidateException{
		
		try{
			Object value1 = bean1.getClass().getMethod("get" + Formatter.getInstance().capitalize(property)).invoke(this);
			Object value2 = bean2.getClass().getMethod("get" + Formatter.getInstance().capitalize(property)).invoke(this);
			
			if(value1.equals(value2)) throw new BeanValidateException(Constants.CMSG_DUPLICATED_REC);
			
		}catch(Exception e){
			logger.error("Failed to perform validation "+e.getMessage());
		}
		
		
	}
	
}