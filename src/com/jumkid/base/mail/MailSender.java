package com.jumkid.base.mail;


import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StopWatch;

public class MailSender{
	
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private String defaultFromName;
	private String defaultFromAddress;
   	 
    private boolean overrideToAddress = false;
    private InternetAddress defaultToAddress;
	
	private JavaMailSender javaMailSender;
	
	/**
	    * send mail implementation
	    * 
	    * @param senderName
	    * @param senderAddress
	    * @param to
	    * @param cc
	    * @param bcc
	    * @param subject
	    * @param mergeObjects
	    * @param templateLocation
	    * @throws Exception
	    */   
	@Async
   public void send(String senderName,
	                    String senderAddress,
	                    InternetAddress[] to, 
	                    InternetAddress[] cc, 
	                    InternetAddress[] bcc,
	                    String subject, 
	                    String content) throws Exception {
	   	
	   	// create a mime message using the mail sender implementation
	   	final MimeMessage message = javaMailSender.createMimeMessage();	    	
	   	    
	   	// create the message using the specified template   	
	   	MimeMessageHelper helper;   	
	   	
	   	try {
	   	    helper = new MimeMessageHelper(message, true, "UTF-8");
	   	} catch(MessagingException e) {
	   	    throw new Exception("unable to create the mime message helper", e);
	   	}
	   	
	   	// add the sender to the message
	   	senderName =( senderName==null? this.getDefaultFromName() : senderName );
	   	senderAddress =( senderAddress==null? this.getDefaultFromAddress() : senderAddress );
	   	helper.setFrom(senderAddress, senderName);
	   	    
	   	if(overrideToAddress) {           
	           helper.addCc(defaultToAddress);
	    }else
	   	if(cc != null && cc.length>0) {   	    
	   	    for(int i=0;i<cc.length;i++) {   	           	        
	   	        helper.addCc(cc[i]);
	   	    }
	   	}
	   	    
	   	// add the bcc 
	   	if(overrideToAddress) {           
	        helper.addBcc(defaultToAddress);
	   	}else
		if(bcc != null && bcc.length>0) {   	    
		    for(int i=0;i<bcc.length;i++) {   	           	        
		        helper.addBcc(bcc[i]);
		    }
		}
	   	    
	   	// add the to 
	   	if(to != null && to.length>0) {
	   		for(int i=0;i<to.length;i++) {   	           	        
		        helper.addTo(to[i]);
		    }
	   	} else {
	   	    // use the default to address
	   		helper.addTo(defaultToAddress);
	   	}	    
	   	    
	   	helper.setText(content, true);
	   	    
	   	helper.setSubject(subject);
	   	
	   	logger.debug("Send out mail ... ");
	    StopWatch stopWatch = new StopWatch();
	    stopWatch.start();
	    javaMailSender.send(message);													        	   
		stopWatch.stop();
		logger.debug("Finish sending a mail within "+stopWatch.getTotalTimeMillis()+"ms");
          	   
    }

	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}

	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public String getDefaultFromName() {
		return defaultFromName;
	}

	public void setDefaultFromName(String defaultFromName) {
		this.defaultFromName = defaultFromName;
	}

	public String getDefaultFromAddress() {
		return defaultFromAddress;
	}

	public void setDefaultFromAddress(String defaultFromAddress) {
		this.defaultFromAddress = defaultFromAddress;
	}
	
	   
}
