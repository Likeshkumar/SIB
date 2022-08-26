package com.ifp.maintain;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;

public class CallCenter extends BaseAction {
	
	CommonDesc commondesc = new CommonDesc();
	CommonUtil comutil = new CommonUtil();
	
	
	public CommonUtil getComutil() {
		return comutil;
	}
	public void setComutil(CommonUtil comutil) {
		this.comutil = comutil;
	}


	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private JdbcTemplate mmsjdbctemplate = new JdbcTemplate();
	
	
	public JdbcTemplate getMmsjdbctemplate() {
		return mmsjdbctemplate;
	}
	public void setMmsjdbctemplate(JdbcTemplate mmsjdbctemplate) {
		this.mmsjdbctemplate = mmsjdbctemplate;
	}
	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}
	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}
	public String comInstId( HttpSession session  ){ 
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	} 
	public String comUserCode( HttpSession session ){
		 
		String instid = (String)session.getAttribute("USERID"); 
		return instid;
	}
	
	
	public CommonDesc getCommondesc() {
		return commondesc;
	}


	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}


	public void sendUsernameasSms() throws IOException  {
		String result = "";
		try{
			trace("Sending webportal username to customer");
			HttpSession session = getRequest().getSession();
			
			String cardno = getRequest().getParameter("cardno");
			String username = getRequest().getParameter("username");
			String instid = comInstId(session);
			
			String usernametext = "Dear Customer, Your Bpesa portal username is "+username+".Thank you for using Bpesa";
			String mobilenumber = commondesc.getMobileNumber(instid, cardno, jdbctemplate);
			//trace("Got mobile number : " + mobilenumber);
			if( mobilenumber == null || mobilenumber.equals("NA")){
				getResponse().getWriter().write( "Mobile number not registered with this customer");
				return ;
			}
			
			comutil.sendSmsMessage(mobilenumber, usernametext); 
			result = "Web portal username sent to customer successfully";
		}catch(Exception e){
			result = "Unable to continue the process";
			trace("Exception : " + e.getMessage());
			e.printStackTrace();
		}
		getResponse().getWriter().write(result);
	}
	
	
	public void sendSequritiyQuestions() throws Exception {
		HttpSession session = getRequest().getSession();
		try{
			String cardno = getRequest().getParameter("cardno");
			String username = getRequest().getParameter("username");
			String instid = comInstId(session);
			List sequrityquestions = null;
			
			String customertype = commondesc.checkEntityValueType(instid, cardno,jdbctemplate) ;
			trace("Customer type....:"+customertype);
			if( customertype == null ){
				sequrityquestions = this.getSequrityQuestionsList(instid, username, jdbctemplate);
				trace("Sequrity question list....:"+sequrityquestions);
			}else{
				if( customertype.equals("$MC")){
					sequrityquestions = this.getSequrityQuestionsList(instid, username, mmsjdbctemplate);
					trace("Sequrity question list....:"+sequrityquestions);
				}
			}
			
			if( sequrityquestions.isEmpty() ){
				getResponse().getWriter().write( "No Details Found...Customer not enrolled in portal");
				return ;
			}
			
			if( !sequrityquestions.isEmpty() && sequrityquestions !=null ){
				String qid="",qans="",questsms="Dear Customer, Your bpesa portal sequrity questions and answer below \n";
				Iterator itr = sequrityquestions.iterator();
				while( itr.hasNext() ){
					Map mp = (Map)itr.next();
					qid = (String)mp.get("QID");
					qans = (String)mp.get("QANS");
					questsms += qid +"-"+qans+"\n";
				}
				
				String mobilenumber = commondesc.getMobileNumber(instid, cardno, jdbctemplate);
				if( mobilenumber == null || mobilenumber.equals("NA")){
					getResponse().getWriter().write( "Mobile number not registered with this customer");
					return ;
				}
				
				comutil.sendSmsMessage(mobilenumber, questsms); 
			}else{
				getResponse().getWriter().write("Unable to send security answer to customer");
				return ;
			}
		}catch(Exception e){
			e.printStackTrace();
			trace("Exception : "+ e.getMessage() );
			getResponse().getWriter().write("Exception : "+ e.getMessage());
			return;
		}
		
		getResponse().getWriter().write("Security questions sent to customer successfully");
		return ;
	}
	
	
	public List getSequrityQuestionsList(String instid, String username, JdbcTemplate jdbctemplate) throws Exception {
		List questionlist = null;		
		String questionlistqry = "SELECT QID, QANS FROM IFPS_CARD_SEQURITY WHERE INST_ID='"+instid+"' AND USERNAME='"+username+"' AND RTRIM(LTRIM(QANS)) <>' '";
		enctrace("questionlistqry : "+questionlistqry );
		questionlist = jdbctemplate.queryForList(questionlistqry);
		return questionlist ;	
	}
	
	 
}
