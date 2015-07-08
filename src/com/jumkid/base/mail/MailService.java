package com.jumkid.base.mail;
/* 
 * This software is written by Jumkid Ltd. and subject
 * to a contract between Jumkid and its customer.
 *
 * This software stays property of Jumkid unless differing
 * arrangements between Jumkid and its customer apply.
 *
 * Jumkid Ltd.
 * 20/F Central Tower
 * 28 Queen's Road Central
 * Hong Kong
 * 
 * Tel: +852 8199 9605
 * http://www.jumkid.com
 * mailto:info@jumkid.com
 *
 * (c)2008 Jumkid Ltd. All rights reserved.
 *
 * VERSION     |    DATE      |   DEVELOPER  |  DESC
 * ----------------------------------------------------------------
 * 1.0b625       Jun2008        chooli         creation
 *
 */
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.mail.internet.InternetAddress;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.jumkid.base.model.AbstractCommandService;
import com.jumkid.base.model.Command;
import com.jumkid.base.model.user.IUserDao;
import com.jumkid.base.model.user.User;
import com.jumkid.base.util.Constants;

public class MailService extends AbstractCommandService implements IMailService{

	public final static String ADDRESS_SEPARATOR = ";"; 
			
	private IUserDao userDao;
	private MailSender mailSender;
	private VelocityEngine velocityEngine;
	
	private String defaultFromName;
	private String defaultFromAddress;
   	 
    private boolean overrideToAddress = false;
    private InternetAddress defaultToAddress;
	
	public IUserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}

	/**
     * Executes the commands and returns it back to controller with results
     * @param cmd is the command bean
     * @return command which is same as input param plus result values
     */
   public Command execute(Command cmd) throws Exception {
	   
	   try{
		   super.execute(cmd);
		   
		   if (isManager("mailManager")) {
               if (isAction("send")) {
            	 String toLst = (String)cmd.getParams().get("to");
      			 String ccLst = (String)cmd.getParams().get("cc");
      			 String bccLst = (String)cmd.getParams().get("bcc");
      			 String subject = (String)cmd.getParams().get("subject");
      			 String tempalteName = (String)cmd.getParams().get("tempalteName");
      			  
      			 Template template = velocityEngine.getTemplate(tempalteName, Constants.DEFAULT_ENCODING);
      			  
      			 VelocityContext velocityContext = new VelocityContext();
      			 Iterator<String> keys = cmd.getParams().keySet().iterator();
      			 while(keys.hasNext()){
      				 String key = keys.next();
      				 velocityContext.put(key, cmd.getParams().get(key));
      			 }
           		  
      			 StringWriter stringWriter = new StringWriter();
           		 template.merge(velocityContext, stringWriter);
           		  
           		 String content = stringWriter.toString();
           		 //process attachments     		  
           		 //ArrayList attLst = (ArrayList)cmd.getParams().get("attach");     		       		 
           		       		  
      	    	 sendMail("jumkid.com", "support@jumkid.com", toLst, ccLst, bccLst, subject, content);
               }
               
		   }
		   
	   }catch (Exception e) {
	       	logger.error("failed to perform "+cmd.getAction()+" in "+cmd.getManager()+" due to "+e.getMessage());
	        cmd.setError(e.getLocalizedMessage());
	   }
	   
	   return cmd;
   }
   
   /**
    * 
    * @param senderName
    * @param senderAddress
    * @param toList
    * @param ccLst
    * @param bccLst
    * @param subject
    * @param content
    * @return
    */
   public void sendMail(String senderName, 
						String senderAddress, 
						List<User> toList, 
						List<User> ccLst,
						List<User> bccLst, 
						String subject, 
						String content){
	   
	   String to = "";
	   if(toList!=null){
		   for(int i=0;i<toList.size();i++){
		       String userName = ((User)toList.get(i)).getFullName();
		       String email = ((User)toList.get(i)).getEmail();
		       to = to+( (email==null||email.trim().equals("")) ? "" : userName+" <"+email+">;" );
		   }
	   }
	   
	   
	   String cc = "";
	   if(ccLst!=null){
		   for(int i=0;i<ccLst.size();i++){
		       String userName = ((User)ccLst.get(i)).getFullName();
		       String email = ((User)ccLst.get(i)).getEmail();
		       cc = cc+( (email==null||email.trim().equals("")) ? "" : userName+" <"+email+">;" );
		   }
	   }	   
	   
	   String bcc = "";
	   if(bccLst!=null){
		   for(int i=0;i<bccLst.size();i++){
		       String userName = ((User)bccLst.get(i)).getFullName();
		       String email = ((User)bccLst.get(i)).getEmail();
		       bcc = bcc+( (email==null||email.trim().equals("")) ? "" : userName+" <"+email+">;" );
		   }
	   }
	   
	   sendMail(senderName, senderAddress, to, cc, bcc, subject, content);
   }
   
   /**
    * 
    * @param senderName
    * @param senderAddress
    * @param toList
    * @param ccLst
    * @param bccLst
    * @param subject
    * @param content
    * @return
    */
   public void sendMail(String senderName, String senderAddress, String toList, String ccLst, String bccLst, 
		   String subject, String content){	   
	   
	   try{
		   //parse to address
		   StringTokenizer addressTokens = new StringTokenizer(toList, ADDRESS_SEPARATOR);
		   ArrayList<InternetAddress> to = new ArrayList<InternetAddress>();
		   while(addressTokens.hasMoreTokens()){
			   String token = addressTokens.nextToken();
			   if(token!=null && !token.trim().isEmpty()){
				   InternetAddress addr = new InternetAddress(token);
				   addr.setPersonal(addr.getPersonal(), Constants.DEFAULT_ENCODING);
				   to.add(addr);
			   }			   
	       }
		  
	       //parse cc address
		   ArrayList<InternetAddress> cc = new ArrayList<InternetAddress>();
		   if(ccLst!=null){
			   addressTokens = new StringTokenizer(ccLst, ADDRESS_SEPARATOR);
			   while(addressTokens.hasMoreTokens()){
				   String token = addressTokens.nextToken();
				   if(token!=null && !token.trim().isEmpty()){
					   InternetAddress addr = new InternetAddress(token);
					   addr.setPersonal(addr.getPersonal(), Constants.DEFAULT_ENCODING);
					   cc.add(addr);
				   }			   
		       }
		   }
		   
		   
	       //parse bcc address
		   ArrayList<InternetAddress> bcc = new ArrayList<InternetAddress>();
		   if(ccLst!=null){
			   addressTokens = new StringTokenizer(bccLst, ADDRESS_SEPARATOR);
			   while(addressTokens.hasMoreTokens()){
				   String token = addressTokens.nextToken();
				   if(token!=null && !token.trim().isEmpty()){
					   InternetAddress addr = new InternetAddress(token);
					   addr.setPersonal(addr.getPersonal(), Constants.DEFAULT_ENCODING);
					   bcc.add(addr);
				   }			   
		       }
		   }
		   
		   mailSender.send(senderName, senderAddress, to.toArray(new InternetAddress[to.size()]), 
				   cc.toArray(new InternetAddress[cc.size()]), bcc.toArray(new InternetAddress[bcc.size()]), subject, content);
	   }catch(Exception e){
		   logger.error("Failed to send mail "+e.getMessage());
	   }
	   	   	  
   }
      

	public MailSender getMailSender() {
		return mailSender;
	}
	
	public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
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
	
	public boolean isOverrideToAddress() {
		return overrideToAddress;
	}
	
	public void setOverrideToAddress(boolean overrideToAddress) {
		this.overrideToAddress = overrideToAddress;
	}
	
	public InternetAddress getDefaultToAddress() {
		return defaultToAddress;
	}
	
	public void setDefaultToAddress(InternetAddress defaultToAddress) {
		this.defaultToAddress = defaultToAddress;
	}

	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

}
