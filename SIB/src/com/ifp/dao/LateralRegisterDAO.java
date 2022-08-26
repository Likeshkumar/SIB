package com.ifp.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.json.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;
import com.ifp.personalize.Personalizeorderdetails;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;


@Transactional
public class LateralRegisterDAO extends BaseAction 
{

	public int checkCardExist( String instid, String cardno, JdbcTemplate jdbctemplate ){
		String cardvalidqry = "SELECT COUNT(*) AS CNT FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'" ;
		System.out.println("\ncardvalidqry : " + cardvalidqry );
		int valid = jdbctemplate.queryForInt(cardvalidqry);
		return valid;
	}
	
	public String checkValidCard(String instid, String cardno,	HttpSession session, JdbcTemplate jdbctemplate) throws Exception {
		String cardvalidqry = "SELECT CARD_STATUS FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'" ;
		System.out.println("\ncardvalidqry : " + cardvalidqry );
		String valid = (String)jdbctemplate.queryForObject(cardvalidqry, String.class); 
		return valid;
	}

	public String checkAlreadyRegistered(String instid, String cardno,	HttpSession session, JdbcTemplate jdbctemplate) throws Exception {
		String validcustomerqry = "SELECT CIN FROM IFP_CARD_ACCT_LINK WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"' AND ROWNUM=1" ;
		System.out.println("\n validcustomerqry: " + validcustomerqry );
		String cin = (String)jdbctemplate.queryForObject(validcustomerqry, String.class); 
		return cin;
	}

	public int insertCustomerData(String instid, String customerid, JSONObject jsoncust,	JdbcTemplate jdbctemplate) throws Exception { 
		String FNAME = jsoncust.getString("FNAME");
		String MNAME = jsoncust.getString("MNAME");
		String LNAME = jsoncust.getString("LNAME");
		String FATHER_NAME = jsoncust.getString("FATHER_NAME");
		String MOTHER_NAME = jsoncust.getString("MOTHER_NAME");
		String MARITAL_STATUS = jsoncust.getString("MARITAL_STATUS");
		String SPOUSE_NAME = jsoncust.getString("SPOUSE_NAME");
		String GENDER = jsoncust.getString("GENDER");
		String DOB = jsoncust.getString("DOB");
		String NATIONALITY = jsoncust.getString("NATIONALITY");
		String EMAIL_ADDRESS = jsoncust.getString("EMAIL_ADDRESS");
		String MOBILE_NO = jsoncust.getString("MOBILE_NO");
		String PHONE_NO  = jsoncust.getString("PHONE_NO");
		String OCCUPATION = jsoncust.getString("OCCUPATION");
		String ID_NUMBER = jsoncust.getString("ID_NUMBER");
		String ID_DOCUMENT = jsoncust.getString("ID_DOCUMENT");
		String POST_ADDR1 = jsoncust.getString("POST_ADDR1");
		String POST_ADDR2 = jsoncust.getString("POST_ADDR2");
		String POST_ADDR3 = jsoncust.getString("POST_ADDR3");
		String POST_ADDR4 = jsoncust.getString("POST_ADDR4");
		String RES_ADDR1 = jsoncust.getString("RES_ADDR1");
		String RES_ADDR2 = jsoncust.getString("RES_ADDR2");
		String RES_ADDR3 = jsoncust.getString("RES_ADDR3");
		String RES_ADDR4 = jsoncust.getString("RES_ADDR4");
		 
		String insertcustinfo = "INSERT INTO IFP_CUSTINFO_PRODUCTION (";
		insertcustinfo += "INST_ID, CIN, FNAME, MNAME, LNAME, FATHER_NAME, MOTHER_NAME, MARITAL_STATUS, SPOUSE_NAME, GENDER, DOB,";
		insertcustinfo += "NATIONALITY, EMAIL_ADDRESS, MOBILE_NO, PHONE_NO, OCCUPATION, ID_NUMBER, ID_DOCUMENT, POST_ADDR1, POST_ADDR2, POST_ADDR3, POST_ADDR4,";
		insertcustinfo += "RES_ADDR1, RES_ADDR2, RES_ADDR3, RES_ADDR4, CUSTOMER_STATUS,KYC_FLAG ) VALUES ( ";
		insertcustinfo += "'"+instid+"','"+customerid+"','"+FNAME+"','"+MNAME+"','"+LNAME+"','"+FATHER_NAME+"','"+MOTHER_NAME+"','"+MARITAL_STATUS+"','"+SPOUSE_NAME+"','"+GENDER+"',to_date('"+DOB+"','dd-mm-yyyy')";
		insertcustinfo += ",'"+NATIONALITY+"','"+EMAIL_ADDRESS+"','"+MOBILE_NO+"','"+PHONE_NO+"','"+OCCUPATION+"','"+ID_NUMBER+"','"+ID_DOCUMENT+"','"+POST_ADDR1+"','"+POST_ADDR2+"','"+POST_ADDR3+"','"+POST_ADDR4+"'";
		insertcustinfo += ",'"+RES_ADDR1+"', '"+RES_ADDR2+"', '"+RES_ADDR3+"', '"+RES_ADDR4+"', '1','0'";
		insertcustinfo += ")"; 
		System.out.println( " insertcustinfo : " + insertcustinfo );
		int x = jdbctemplate.update(insertcustinfo);
		return x;
	}

	public String checkCardStatus(String instid, String cardno,	JdbcTemplate jdbctemplate) {
		String cardvalidqry = "SELECT STATUS_CODE FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'" ;
		System.out.println("\ncardvalidqry : " + cardvalidqry );
		String valid = (String)jdbctemplate.queryForObject(cardvalidqry, String.class); 
		return valid;
	}  
	
	
	
}//end class
 