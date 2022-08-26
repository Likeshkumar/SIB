package com.mms.dao;

import it.sauronsoftware.base64.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;
import com.opensymphony.xwork2.Result;

import com.ifp.beans.DisputeBeans;
public class DisputeDAO extends BaseAction 
{ 
	
	 
	private static final long serialVersionUID = -8376161637970676446L;
	
	
	public List getTransactionDetails(String instid, String cardno, String refno, JdbcTemplate jdbctemplate, HttpSession session){
		List txndetailslist = null;
		try {
			String txndetailsqry = "SELECT BIN , TXNCODE, TERMINALID AS TERMID, REFNUM, AMOUNT, TERMLOC, ISSCURRENCYCODE as TXNCURRENCY, TRACENO FROM  IFD_TRANSACTION_MASTER WHERE INST_ID='"+instid+"' AND CHN='"+cardno+"' AND REFNUM='"+refno+"' ";
			System.out.println("txndetailsqry--"+txndetailsqry);
			txndetailslist = jdbctemplate.queryForList(txndetailsqry); 
		} catch (DataAccessException e) {	
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.toString());
			e.printStackTrace(); 
		}
		return txndetailslist;
	}
	 
	
	public int insertCompliant(String instid, String dispute_id, String usercode, DisputeBeans dispute, JdbcTemplate jdbctemplate, HttpSession session){
		int registercomplaints = -1;
		try {
			String compqry = "INSERT INTO EZ_COMPLIANTS (INST_ID,COMPLIANT_CODE,CARD_NO,ACCT_NO,CUSTOMER_NAME,EMAIL_ADDRS,PHONE_NO,TXN_DATE,COMPLIANT_DATE,";
			compqry += " REF_NO,TRACE_NO,AMT_REQUESTED,AMOUNT_DISPENSED,CLAIM_AMT,COMMENTS,COMPLAINT_REG_BY,REVERSETYPE ) VALUES ("; 
			compqry += "'"+instid+"','"+dispute_id+"','"+dispute.getCardnobean()+"','"+dispute.getAcctnumberbean()+"','"+dispute.getCustomernamebean()+"','"+dispute.emailbean+"',";
			compqry += "'"+dispute.getPhonenumberbean()+"',to_date('"+dispute.getTxndatebean()+"','DD-MM-YYYY'),SYSDATE,";
			compqry += "'"+dispute.getRefnobean()+"','"+dispute.getTrancenobean()+"','"+dispute.getAmtreqbean()+"','"+dispute.getAmtdispensedbean()+"','"+dispute.getAmtclaimedbean()+"','"+dispute.getCommentbean()+"','"+usercode+"','"+dispute.getReversemode()+"')";
			System.out.println("insert_registercomplaints		"+compqry);
			registercomplaints = jdbctemplate.update(compqry);
		} catch (DataAccessException e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.toString());
			e.printStackTrace();  
		}
		 return registercomplaints;
	}
	
	public int updateSequance(String instid, JdbcTemplate jdbctemplate, HttpSession session ){
		int update_seq = -1;
		try {
			String update_dispute_qury = "UPDATE SEQUENCE_MASTER SET DISPUTE_CODE_SEQ=DISPUTE_CODE_SEQ+1 WHERE INST_ID='"+instid+"'";
			System.out.println("Update Quryr ==> "+update_dispute_qury);
			update_seq = jdbctemplate.update(update_dispute_qury);
			System.out.println("update_seq ==> "+update_seq);
		} catch (DataAccessException e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.toString());
			e.printStackTrace();
		}
		return update_seq;
	}


	public List getListOfCompliants(String instid, JdbcTemplate jdbctemplate) throws Exception {
		List complist = null;
		String complistqry = "SELECT COMPLIANT_CODE FROM EZ_COMPLIANTS WHERE INST_ID='"+instid+"' AND STATUS='O'";
		System.out.println( "complistqry : " + complistqry );
		complist = jdbctemplate.queryForList(complistqry);
		return complist;
	}


	public List getCompliantData(String instid, String compliantid,	JdbcTemplate jdbctemplate) throws Exception{
		List compdata = null;
		String compdataqry = "SELECT CARD_NO, ACCT_NO, CUSTOMER_NAME, EMAIL_ADDRS, PHONE_NO, to_char(TXN_DATE, 'dd-mon-yyyy') as TXN_DATE , to_char(COMPLIANT_DATE, 'dd-mon-yyyy') as COMPLIANT_DATE, REF_NO, TRACE_NO, AMT_REQUESTED, AMOUNT_DISPENSED, CLAIM_AMT, COMMENTS, STATUS, COMPLAINT_REG_BY,REVERSETYPE FROM EZ_COMPLIANTS WHERE INST_ID='"+instid+"' AND COMPLIANT_CODE='"+compliantid+"'";
		System.out.println( "compdataqry : " + compdataqry );
		compdata = jdbctemplate.queryForList(compdataqry);
		return compdata;
	}


	public int updateCompliantStatus(String instid, String compliantid,	String compstatus, String closecomment, String usercode, JdbcTemplate jdbctemplate) {
		int x = -1;
		String compupdqry = "UPDATE  EZ_COMPLIANTS SET STATUS='"+compstatus+"', CLOSING_COMMENT='"+closecomment+"', CLOSED_BY='"+usercode+"', CLOSED_DATE=SYSDATE WHERE INST_ID='"+instid+"' AND COMPLIANT_CODE='"+compliantid+"'";
		x = jdbctemplate.update(compupdqry);
		return x;
	}
	
	public List getCompliantTxnRefno(String instid, String compliantid, JdbcTemplate jdbctemplate) throws Exception {
		List compdata = null;
		String compdataqry = "SELECT CARD_NO, TRACE_NO, REF_NO, AMT_REQUESTED FROM EZ_COMPLIANTS WHERE INST_ID='"+instid+"' AND COMPLIANT_CODE='"+compliantid+"'";
		System.out.println( "compdataqry : " + compdataqry );
		compdata = jdbctemplate.queryForList(compdataqry);
		return compdata;
	}


	
}
 