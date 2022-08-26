package com.ifp.dao;

import it.sauronsoftware.base64.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;
import com.opensymphony.xwork2.Result;

import com.ifp.beans.DisputeBeans;

@Transactional
public class ReconDAO extends BaseAction 
{ 
	
	 
	private static final long serialVersionUID = -8376161637970676446L;
	
	
	public List getTransactionDetails(String instid, String cardno, String refno, JdbcTemplate jdbctemplate, HttpSession session){
		List txndetailslist = null;
		try {
			String txndetailsqry = "SELECT CARDSCHEME, TXNCODE, TERMID, REFNUM, TXNAMOUNT, TERMLOC, TXNCURRENCY, TRACENO FROM EZMMS_TRANSACTION WHERE INST_ID='"+instid+"' AND CHN='"+cardno+"' AND TXNREFNUM='"+refno+"' ";
			enctrace("txndetailsqry--"+txndetailsqry);
			txndetailslist = jdbctemplate.queryForList(txndetailsqry); 
		} catch (DataAccessException e) {	
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.toString());
			e.printStackTrace(); 
		}
		return txndetailslist;
	}


	public List genenrateMMSReconFile(String instid, String tablename,	String selectcond,  String wherecondition, 	JdbcTemplate jdbctemplate, HttpSession session) {
		List reconfilelist = null;
		
		try {
			String reconfileqry = "SELECT " + selectcond + " AS RECONFILECONTENT FROM " + tablename + " WHERE INSTID='"+instid+"' " +wherecondition ;
			enctrace("reconfileqry : " + reconfileqry);
			reconfilelist = jdbctemplate.queryForList(reconfileqry);
		} catch (Exception e) { 
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.toString());
			trace("Exception : " + e.getMessage() );
			e.printStackTrace();
		}
		
		return reconfilelist;
	}

	public List genenratePrepaidReconFile(String instid, String tablename,	String selectcond,  String wherecondition, 	JdbcTemplate jdbctemplate, HttpSession session) {
		List reconfilelist = null;
		
		try {
			String reconfileqry = "SELECT " + selectcond + " AS RECONFILECONTENT FROM " + tablename + " WHERE INST_ID='"+instid+"' " +wherecondition ;
			enctrace("reconfileqry--" + reconfileqry);
			reconfilelist = jdbctemplate.queryForList(reconfileqry);
		} catch (Exception e) { 
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.toString());
			e.printStackTrace();
		}
		
		return reconfilelist;
	}

	public int insertReconFile(String instid, String bin, String reconfilename,	String reconflecontent, String usercode, JdbcTemplate jdbctemplate, HttpSession session) {
		int insertval = 0;
		try {			
			String reconinsert = "INSERT INTO IFP_RECONPROCESS ( INST_ID, BIN, RECON_FILE_NAME, FILE_CONTENT, GENERATED_DATE, USER_CODE) VALUES ( ";
			reconinsert += " '"+instid+"', '"+bin+"', '"+reconfilename+"', '"+reconflecontent+"', SYSDATE, '"+usercode+"' ) ";
			enctrace(reconinsert);
			insertval = jdbctemplate.update(reconinsert);
		} catch (Exception e) { 
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.toString());
			e.printStackTrace();
		}
		return insertval;
	}


	public int insertMerchantReconFile(String instid, String bin, String reconfilename,	String reconflecontent, String usercode, JdbcTemplate jdbctemplate, HttpSession session) {
		int insertval = 0;
		try {			
			String reconinsert = "INSERT INTO EZMMS_RECONPROCESS ( INST_ID, BIN, RECON_FILE_NAME, FILE_CONTENT, GENERATED_DATE, USER_CODE) VALUES ( ";
			reconinsert += " '"+instid+"', '"+bin+"', '"+reconfilename+"', '"+reconflecontent+"', SYSDATE, '"+usercode+"' ) ";
			enctrace(reconinsert);
			insertval = jdbctemplate.update(reconinsert);
		} catch (Exception e) { 
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.toString());
			e.printStackTrace();
		}
		return insertval;
	}
	
	public String getMerchantBinDesc( String instid, String bin, JdbcTemplate jdbctemplate ) throws Exception {   
		String bin_desc = null;
		try {
			String qryproductdesc = "SELECT SCHEME_NAME FROM EZMMS_SCHEMEMASTER WHERE INST_ID='"+instid+"' and BIN='"+bin+"' and rownum <= 1";
			enctrace( " qryproductdesc : " + qryproductdesc); 
			bin_desc = (String)jdbctemplate.queryForObject(qryproductdesc, String.class);
		} catch (EmptyResultDataAccessException e) { 
			 bin_desc = null;
		} 
		return bin_desc; 
	}
	public int updReconFilename(String instid, String tablename, String reconfilename, String wherecondition, JdbcTemplate jdbctemplate, HttpSession session) {
		int updfilename = 0;
		try {
			String reconinsert = " UPDATE "+tablename+" SET RECON_FILE_NAME='"+reconfilename+"', RECON_FILE_GENERATED='Y' WHERE INST_ID='"+instid+"'" +wherecondition ;
			updfilename = jdbctemplate.update(reconinsert);
		} catch (Exception e) { 
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.toString());
			e.printStackTrace();
		}
		return updfilename;
	}

	public int updMmsReconFilename(String instid, String tablename, String reconfilename, String wherecondition, JdbcTemplate jdbctemplate, HttpSession session) {
		int updfilename = 0;
		try {
			String reconinsert = " UPDATE "+tablename+" SET RECON_FILE_NAME='"+reconfilename+"', RECON_FILE_GENERATED='Y' WHERE INSTID='"+instid+"'" +wherecondition ;
			updfilename = jdbctemplate.update(reconinsert);
		} catch (Exception e) { 
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.toString());
			e.printStackTrace();
		}
		return updfilename;
	}
	

	public int updateReconHistory(String instid, String bin, String usercode,	JdbcTemplate jdbctemplate, HttpSession session) {
		int updfilename = 0;
		try {
			String txnqry = "";
			int x = this.checkReconBinExist( instid, bin, jdbctemplate );
			if( x == 0 ){
				txnqry = "INSERT INTO IFP_RECON_HIST(INST_ID, BIN,  LAST_GENDATE, USERCODE ) VALUES ('"+instid+"','"+bin+"',sysdate,'"+usercode+"')";
			}else if ( x > 0 ){
				txnqry = "UPDATE IFP_RECON_HIST SET LAST_GENDATE=SYSDATE,  USERCODE = '"+usercode+"' WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
			} 
			updfilename = jdbctemplate.update(txnqry);
		} catch (Exception e) { 
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.toString());
			e.printStackTrace();
		}
		return updfilename;
	}


	private int checkReconBinExist(String instid, String bin, JdbcTemplate jdbctemplate) {
		String checkqry = "SELECT COUNT(*) FROM IFP_RECON_HIST WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
		enctrace( "checkqry--" + checkqry);
		int x = jdbctemplate.queryForInt(checkqry);
		return x;
	}


	public List getlastrecohistory(String instid, JdbcTemplate jdbctemplate, HttpSession session) {
		List txndetailslist = null;
		try {
			String txndetailsqry = " SELECT BIN, to_char(LAST_GENDATE, 'dd-mon-yy hh:mi:ss') as LAST_GENDATE, USERCODE FROM  IFP_RECON_HIST WHERE INST_ID='"+instid+"'";
			enctrace("txndetailsqry--"+txndetailsqry);
			txndetailslist = jdbctemplate.queryForList(txndetailsqry); 
		} catch (DataAccessException e) {	
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.toString());
			e.printStackTrace(); 
		}
		return txndetailslist;
		 
	}


	public List getReconFileList(String instid, String tablename, String bin, JdbcTemplate jdbctemplate, HttpSession session) { 
		List datalist = null;
		try {
			String qrystring = " SELECT DISTINCT RECON_FILE_NAME  FROM  "+tablename+"  WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
			enctrace("qrystring--"+qrystring);
			datalist = jdbctemplate.queryForList(qrystring); 
		} catch (DataAccessException e) {	
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.toString());
			e.printStackTrace(); 
		}
		return datalist;
	}
}
 