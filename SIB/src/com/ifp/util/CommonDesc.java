package com.ifp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.xwork.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.dao.CardCvvDecryption;
import com.ifp.exceptions.exception;
import com.ifp.instant.HSMParameter;
import com.ifp.personalize.Personalizeorderdetails;
import com.ifp.instant.RequiredCheck;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

import it.sauronsoftware.base64.Base64;
import oracle.sql.DATE;
import java.util.Calendar;

public class CommonDesc extends BaseAction {

	private static final long serialVersionUID = 1L;
	String NOREC = "NOREC";

	public final static String default_date_query = "TO_DATE('01-JAN-1900', 'DD-MON-YYYY')";
	public final static String def_time = "(select trim(substr(to_char(sysdate,'dd-MON-yy hh24:mi:ss'),10,10)) from dual)";
	public final static String ACTIVECARDSTATUS = "09";

	public final static String NORMALCARD = "05";

	// public static boolean initmail = true;
	public CommonDesc() {
	}

	private ByteArrayInputStream input_stream;
	private ByteArrayOutputStream output_stream;

	public ByteArrayInputStream getInput_stream() {
		return input_stream;
	}

	public void setInput_stream(ByteArrayInputStream input_stream) {
		this.input_stream = input_stream;
	}

	public ByteArrayOutputStream getOutput_stream() {
		return output_stream;
	}

	public void setOutput_stream(ByteArrayOutputStream output_stream) {
		this.output_stream = output_stream;
	}

	private String genpdfdownloadfilename;

	public String getGenpdfdownloadfilename() {
		return genpdfdownloadfilename;
	}

	public void setGenpdfdownloadfilename(String genpdfdownloadfilename) {
		this.genpdfdownloadfilename = genpdfdownloadfilename;
	}

	List genreport_list;

	public List getGenreport_list() {
		return genreport_list;
	}

	public void setGenreport_list(List genreport_list) {
		this.genreport_list = genreport_list;
	}

	/*
	 * public CommonDesc( DataSource dataSource ){ this.dataSource = dataSource;
	 * }
	 */
	DebugWriter debugger = new DebugWriter();

	/*
	 * public Properties getCommonDescProperty (){ String filename =
	 * "CommonDesc.properties"; Properties prop = new Properties(); try {
	 * prop.load(this.getClass().getClassLoader().getResourceAsStream(filename))
	 * ; } catch (IOException e) {
	 * 
	 * e.printStackTrace(); } return prop; }
	 */

	/*
	 * public void commitTxn(JdbcTemplate jdbctemplate){
	 * jdbctemplate.execute("commit"); }
	 * 
	 * public void rollbackTxn(JdbcTemplate jdbctemplate){
	 * jdbctemplate.execute("rollback");
	 * 
	 * }
	 */

	public IfpTransObj myTranObject(String setname,
			PlatformTransactionManager txManager) {
		// PlatformTransactionManager txManager=new
		// DataSourceTransactionManager();
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(1000);
		setname = setname + randomInt;
		trace(" Transaction Name " + setname);
		def.setName(setname);
		def.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);// ISOLATION_SERIALIZABLE
		TransactionStatus status = txManager.getTransaction(def);
		return new IfpTransObj(txManager, status);
	}

	public List gettingNations(JdbcTemplate jdbcTemplate) throws Exception {
		List nation_result;
		String nation_id, nationn_name;

		String National_list = "select NATION,nvl(NATION_ID,'XXX') AS NATION_ID from IFP_COUNTRY_MASTER order by NATION";
		// // enctrace("3030National_list : " + National_list );
		nation_result = jdbcTemplate.queryForList(National_list);
		return nation_result;
	}

	public String getCountryCode(String instid, JdbcTemplate jdbctemplate)
			throws Exception {
		String countrycode = null;
		try {

			/*
			 * String countrycodeqry =
			 * "SELECT COUNTRY_CODE FROM INSTITUTION WHERE INST_ID='" + instid+
			 * "' AND  ROWNUM <=1"; trace("countrycodeqry : " + countrycodeqry);
			 * countrycode = (String)
			 * jdbctemplate.queryForObject(countrycodeqry, String.class);
			 */

			// by gowtham-260819
			String countrycodeqry = "SELECT COUNTRY_CODE FROM INSTITUTION WHERE INST_ID=? AND  ROWNUM <=?";
			trace("countrycodeqry : " + countrycodeqry);
			countrycode = (String) jdbctemplate.queryForObject(countrycodeqry,
					new Object[] { instid, "1" }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return countrycode;
	}

	public String getResponseCodeDesc(String instid, String responsecode,
			JdbcTemplate jdbctemplate) throws Exception {
		if (responsecode == null) {
			return responsecode;
		}

		String resdescr = responsecode;
		try {
			String resdes = "SELECT TO_CHAR(DESCRIPTION) as DESCRIPTION  FROM IFD_RESPCODE WHERE RESPCODE='"
					+ responsecode + "'";
			// // enctrace("3030resdes :" + resdes);
			resdescr = (String) jdbctemplate.queryForObject(resdes,
					String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return resdescr;
	}

	public List getAllInstitution(JdbcTemplate jdbctemplate) throws Exception {
		List instidlist = null;
		String instqry = "SELECT INST_ID, INST_NAME FROM INSTITUTION where AUTH_CODE='1'";
		instidlist = jdbctemplate.queryForList(instqry);
		return instidlist;
	}

	public String getInstDesc(String instid, JdbcTemplate jdbctemplate) {

		/*
		 * String instnameqry =
		 * "SELECT INST_NAME FROM INSTITUTION WHERE INST_ID='" + instid +
		 * "' AND  ROWNUM <=1"; trace("instnameqry__" + instnameqry); String
		 * instname = null; try { instname = (String)
		 * jdbctemplate.queryForObject(instnameqry, String.class);
		 */

		// by gowtham-140819
		String instnameqry = "SELECT INST_NAME FROM INSTITUTION WHERE INST_ID=? AND  ROWNUM <=?";
		trace("instnameqry__" + instnameqry);
		String instname = null;
		try {
			instname = (String) jdbctemplate.queryForObject(instnameqry,
					new Object[] { instid, "1" }, String.class);

		} catch (EmptyResultDataAccessException e) {
			instname = NOREC;
		}
		return instname;
	}

	public String getCardCurrencyCode(String instid, String processtype,
			String cardno, JdbcTemplate jdbctemplate) {
		String cardcurrency = null;
		try {
			String tablename = "";
			if (processtype.equals("INST")) {
				tablename = "INST_CARD_PROCESS";
			} else if (processtype.equals("PERS")) {
				tablename = "PERS_CARD_PROCESS";
			} else {
				return null;
			}

			/*
			 * String cardcurrencyqry = "SELECT CARD_CCY  FROM " + tablename +
			 * " WHERE INST_ID='" + instid+ "' AND ORG_CHN='" + cardno +
			 * "'  AND ROWNUM <=1"; //// enctrace("3030cardcurrencyqry : "+
			 * cardcurrencyqry ); cardcurrency = (String)
			 * jdbctemplate.queryForObject(cardcurrencyqry, String.class);
			 */

			// by gowtham-260819
			String cardcurrencyqry = "SELECT CARD_CCY  FROM " + tablename
					+ " WHERE INST_ID=? AND ORG_CHN=?  AND ROWNUM <=?";
			// // enctrace("3030cardcurrencyqry : "+ cardcurrencyqry );
			cardcurrency = (String) jdbctemplate.queryForObject(
					cardcurrencyqry, new Object[] { instid, cardno, "1" },
					String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return cardcurrency;
	}

	public List gettingSequrityQuestion(JdbcTemplate jdbcTemplate) {
		List security_list;
		String question, questionid;
		String Security_list = "select QUESTION,QUESTION_ID from IFPSECUTIRY_QUESTION order by question_id";
		security_list = jdbcTemplate.queryForList(Security_list);
		// trace("Question list "+security_list);
		// trace("Question list Created Successfully ****");
		return security_list;
	}

	/**************************** suresh ****************************************/
	/*
	 * public String getProductdesc( String instid, String prodcode ) { String
	 * qryproductdesc =
	 * "SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID='"+instid+
	 * "' and PRODUCT_CODE='"+prodcode+"'"; trace( " getProductdesc " +
	 * qryproductdesc);
	 * 
	 * if ( jdbcTemplate.getMaxRows() == 0 ) { String bin_desc =
	 * (String)jdbcTemplate.queryForObject(qryproductdesc, String.class); return
	 * bin_desc; }else{ return "UNKNOWN PRODUCT"; } }
	 */

	/*
	 * public String getProductdesc( String instid, String prodcode,
	 * JdbcTemplate jdbctemplate ) { String qryproductdesc =
	 * "SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID='"+instid+
	 * "' and PRODUCT_CODE='"+prodcode+"'"; trace( " getProductdesc " +
	 * qryproductdesc +"jdbctemplate.getMaxRows()===> "
	 * +jdbctemplate.getMaxRows()); if ( jdbctemplate.getMaxRows() == 0 ) {
	 * trace( " Entered IF condition "); String bin_desc =
	 * (String)jdbctemplate.queryForObject(qryproductdesc, String.class); trace(
	 * " BIN DESCRIPTION "+bin_desc); return bin_desc; }else{ return
	 * "UNKNOWN PRODUCT"; } }
	 */

	/*
	 * public String getSubProductdesc( String instid, String subprodid ){
	 * String qryproductdesc =
	 * "SELECT SUB_PRODUCT_NAME FROM INSTPROD_DETAILS WHERE INST_ID='"+instid+
	 * "' and SUB_PROD_ID='"+subprodid+"'"; trace( " getProductdesc " +
	 * qryproductdesc); JdbcTemplate jdbcTemplate = new
	 * JdbcTemplate(dataSource); if ( jdbcTemplate.getMaxRows() == 0 ) { String
	 * bin_desc = (String)jdbcTemplate.queryForObject(qryproductdesc,
	 * String.class); return bin_desc; }else{ return "UNKNOWN PRODUCT"; } }
	 */

	public String getSubProductdesc(String instid, String subprodid,
			JdbcTemplate jdbcemplate) throws Exception {

		/*
		 * String qryproductdesc =
		 * "SELECT SUB_PRODUCT_NAME FROM INSTPROD_DETAILS WHERE INST_ID='" +
		 * instid+ "' and SUB_PROD_ID='" + subprodid + "'"; enctrace(
		 * " getProductdesc " + qryproductdesc); if (jdbcemplate.getMaxRows() ==
		 * 0) { String bin_desc = (String)
		 * jdbcemplate.queryForObject(qryproductdesc, String.class);
		 */

		// by gowtham-280819
		String qryproductdesc = "SELECT SUB_PRODUCT_NAME FROM INSTPROD_DETAILS WHERE INST_ID=? and SUB_PROD_ID=? ";
		enctrace(" getProductdesc " + qryproductdesc);
		if (jdbcemplate.getMaxRows() == 0) {
			String bin_desc = (String) jdbcemplate.queryForObject(
					qryproductdesc, new Object[] { instid, subprodid },
					String.class);

			return bin_desc;
		} else {
			return "UNKNOWN PRODUCT";
		}
	}

	public String getusrname(String instid, String userid,
			JdbcTemplate jdbctemplate) {
		String desc_detail = null;
		try {

			/*
			 * String qrydesc =
			 * "SELECT USERNAME  FROM USER_DETAILS WHERE INSTID='" + instid +
			 * "' and USERID='" + userid+ "'"; trace("qrydescforusername :" +
			 * qrydesc); desc_detail = (String)
			 * jdbctemplate.queryForObject(qrydesc, String.class);
			 */

			// / by gowtham-280819
			String qrydesc = "SELECT USERNAME  FROM USER_DETAILS WHERE INSTID=? and USERID=?";
			trace("qrydescforusername :" + qrydesc);
			desc_detail = (String) jdbctemplate.queryForObject(qrydesc,
					new Object[] { instid, userid }, String.class);

		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return desc_detail;
	}

	public String getAccounno(String instid, String custid,
			JdbcTemplate jdbcemplate) throws Exception {

		/*
		 * String qryproductdesc = "SELECT ACCOUNTNO FROM ACCOUNTINFO " +
		 * "WHERE INST_ID='" + instid + "' and CIN='" + custid+ "' AND ROWNUM=1"
		 * ; enctrace(" getAccounno " + qryproductdesc); if
		 * (jdbcemplate.getMaxRows() == 0) { String bin_desc = (String)
		 * jdbcemplate.queryForObject(qryproductdesc, String.class);
		 */

		// / by gowtham-280819
		String qryproductdesc = "SELECT ACCOUNTNO FROM ACCOUNTINFO "
				+ "WHERE INST_ID=? and CIN=? AND ROWNUM=?";
		enctrace(" getAccounno " + qryproductdesc);
		if (jdbcemplate.getMaxRows() == 0) {
			String bin_desc = (String) jdbcemplate.queryForObject(
					qryproductdesc, new Object[] { instid, custid, "1" },
					String.class);

			return bin_desc;
		} else {
			return "UNKNOWN getAccounno";
		}
	}

	public String getSubProductExpPeriod(String instid, String subprodid,
			JdbcTemplate jdbcemplate) throws Exception {

		/*
		 * String qryproductdesc = "SELECT EXPIRY_PERIOD FROM INSTPROD_DETAILS "
		 * + "WHERE INST_ID='" + instid+ "' and SUB_PROD_ID='" + subprodid +
		 * "'"; enctrace(" EXPIRY_PERIOD " + qryproductdesc); if
		 * (jdbcemplate.getMaxRows() == 0) { String bin_desc = (String)
		 * jdbcemplate.queryForObject(qryproductdesc, String.class);
		 */

		// by gowtham300819
		String qryproductdesc = "SELECT EXPIRY_PERIOD FROM INSTPROD_DETAILS "
				+ "WHERE INST_ID=? and SUB_PROD_ID=? ";
		enctrace(" EXPIRY_PERIOD " + qryproductdesc);
		if (jdbcemplate.getMaxRows() == 0) {
			String bin_desc = (String) jdbcemplate.queryForObject(
					qryproductdesc, new Object[] { instid, subprodid },
					String.class);

			return bin_desc;
		} else {
			return "UNKNOWN PRODUCT";
		}
	}

	public String getCardTypeDesc(String instid, String bin,
			JdbcTemplate jdbctemplate) throws Exception {
		/*
		 * String bincond = ""; if (!bin.equals("ALL")) { bincond = " and BIN='"
		 * + bin + "' "; } String qryproductdesc =
		 * "SELECT BIN_DESC FROM PRODUCTINFO WHERE INST_ID='" + instid + "' "
		 * + bincond + " and rownum <= 1"; trace("Bin condition " +
		 * qryproductdesc); String bin_desc = (String)
		 * jdbctemplate.queryForObject(qryproductdesc, String.class); return
		 * bin_desc;
		 */

		String bincond = "";
		String bin_desc = "";

		if (!bin.equals("ALL")) {
			bincond = " and BIN='" + bin + "' ";

			String qryproductdesc = "SELECT PRD_DESC FROM PRODUCTINFO WHERE INST_ID=? and PRD_CODE=?  and rownum <= ?";
			trace("Bin condition " + qryproductdesc);
			bin_desc = (String) jdbctemplate.queryForObject(qryproductdesc,
					new Object[] { instid, bin, "1" }, String.class);
		} else {
			bincond = "";

			String qryproductdesc = "SELECT PRD_DESC FROM PRODUCTINFO WHERE INST_ID=? "
					+ bincond + " and rownum <=?";
			trace("Bin condition " + qryproductdesc);
			bin_desc = (String) jdbctemplate.queryForObject(qryproductdesc,
					new Object[] { instid, "1" }, String.class);
		}
		;

		return bin_desc;

	}

	public String getSTATUSFromEZLINK(String instid, String cardno,
			JdbcTemplate jdbctemplate) throws Exception {
		String ezstatus = "";
		try {
			String ezstatusqry = "SELECT STATUS FROM EZCARDINFO WHERE INSTID='"
					+ instid + "' AND CHN='" + cardno + "' and rownum <= 1";
			trace("ezstatusqry " + ezstatusqry);
			ezstatus = (String) jdbctemplate.queryForObject(ezstatusqry,
					String.class);
		} catch (Exception e) {
			trace("getSTATUSFromEZLINK::" + e);
			return ezstatus;
		}
		return ezstatus;
	}

	/*
	 * public String getCardTypeDesc( String instid, String bin ) throws
	 * Exception{ String qryproductdesc =
	 * "SELECT BIN_DESC FROM PRODUCTINFO WHERE INST_ID='"+instid+
	 * "' and BIN='"+bin+"' and rownum <= 1"; trace( " getCardTypeDesc " +
	 * qryproductdesc);
	 * 
	 * JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource); String bin_desc
	 * = (String)jdbcTemplate.queryForObject(qryproductdesc, String.class);
	 * return bin_desc; }
	 */

	public String getBinDesc(String instid, String bin,
			JdbcTemplate jdbctemplate) {
		String bin_desc = null;
		try {
			String qryproductdesc = "SELECT PRD_DESC FROM PRODUCTINFO WHERE INST_ID='"
					+ instid + "' and PRD_CODE='" + bin + "' and rownum <= 1";
			trace(" getCardTypeDesc " + qryproductdesc);
			bin_desc = (String) jdbctemplate.queryForObject(qryproductdesc,
					String.class);
		} catch (EmptyResultDataAccessException e) {
			bin_desc = null;
		}
		return bin_desc;
	}

	public String getUserName(String instid, String userid,
			JdbcTemplate jdbctemplate) {
		String desc_detail = null;
		try {
			/*
			 * String qrydesc =
			 * "SELECT USERNAME  FROM USER_DETAILS WHERE INSTID='" + instid +
			 * "' and USERID='" + userid+ "' " + "OR USERNAME='" + userid +
			 * "' and rownum <= 1"; trace("qrydesc :" + qrydesc); desc_detail =
			 * (String) jdbctemplate.queryForObject(qrydesc, String.class);
			 */

			// added by gowtham130819
			String qrydesc = "SELECT USERNAME  FROM USER_DETAILS WHERE INSTID=? and USERID=? OR USERNAME=? and rownum <=?";
			trace("qrydesc :" + qrydesc);
			desc_detail = (String) jdbctemplate.queryForObject(qrydesc,
					new Object[] { instid, userid, userid, "1" }, String.class);

		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return desc_detail;
	}

	public String getCbsUserName(String instid, String userid,
			JdbcTemplate jdbctemplate) {
		String desc_detail = null;
		try {
			String qrydesc = "SELECT CBS_USERNAME  FROM USER_DETAILS WHERE INSTID='"
					+ instid
					+ "' and USERID='"
					+ userid
					+ "' OR USERNAME='"
					+ userid + "' and rownum <= 1";
			trace("qrydesc :" + qrydesc);
			desc_detail = (String) jdbctemplate.queryForObject(qrydesc,
					String.class);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return desc_detail;
	}

	// -----------added by sardar--------------------15/12/15------------//
	public String getfeename(String instid, String mfeecode, String userid,
			JdbcTemplate jdbctemplate) {
		String desc_detail1 = null;
		try {
			String qrydesc = "SELECT FEE_DESC  FROM FEE_DESC WHERE INST_ID='"
					+ instid + "' and FEE_CODE='" + mfeecode + "'";
			trace("qrydesc :" + qrydesc);
			desc_detail1 = (String) jdbctemplate.queryForObject(qrydesc,
					String.class);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return desc_detail1;
	}

	public String getUserNameFromTemp(String instid, String userid,
			JdbcTemplate jdbctemplate) {
		String desc_detail = null;
		try {

			/*
			 * String qrydesc =
			 * "SELECT USERNAME  FROM USER_DETAILS WHERE INSTID='" + instid +
			 * "' and USERID='" + userid+ "'  OR USERNAME='" + userid +
			 * "' and rownum <= 1"; trace("qrydesc :" + qrydesc); desc_detail =
			 * (String) jdbctemplate.queryForObject(qrydesc, String.class);
			 */

			// by gowtham-140819
			String qrydesc = "SELECT USERNAME  FROM USER_DETAILS WHERE INSTID=?and USERID=?  OR USERNAME=? and rownum <= ?";
			trace("qrydesc :" + qrydesc);
			desc_detail = (String) jdbctemplate.queryForObject(qrydesc,
					new Object[] { instid, userid, userid, "1" }, String.class);

		} catch (DataAccessException e) {
		}
		return desc_detail;
	}

	public String getUserEmailFromTemp(String instid, String userid,
			JdbcTemplate jdbctemplate) {
		String email = null;
		try {
			/*
			 * String qrydesc =
			 * "SELECT EMAILID  FROM USER_DETAILS WHERE INSTID='" + instid +
			 * "' and USERID='" + userid + "'  OR USERNAME='" + userid +
			 * "' and rownum <= 1"; trace("qrydesc :" + qrydesc); email =
			 * (String) jdbctemplate.queryForObject(qrydesc, String.class);
			 */

			// by gowtham-130819
			String qrydesc = "SELECT EMAILID  FROM USER_DETAILS WHERE INSTID=? and USERID=? OR USERNAME=? and rownum <=?";
			trace("qrydesc :" + qrydesc);
			email = (String) jdbctemplate.queryForObject(qrydesc, new Object[] {
					instid, userid, userid, "1" }, String.class);

		} catch (DataAccessException e) {
		}
		return email;
	}

	public String getUserIdFromTemp(String instid, String userid,
			JdbcTemplate jdbctemplate) {
		String desc_detail = null;
		try {
			String qrydesc = "SELECT USERID  FROM USER_DETAILS WHERE INSTID='"
					+ instid + "' AND USERNAME='" + userid
					+ "' and rownum <= 1";
			trace("qrydesc :" + qrydesc);
			desc_detail = (String) jdbctemplate.queryForObject(qrydesc,
					String.class);
		} catch (DataAccessException e) {
		}
		return desc_detail;
	}

	/*
	 * public String getBranchDesc( String instid, String branchcode ) throws
	 * Exception{ String qrydesc =
	 * "SELECT BRANCH_NAME  FROM BRANCH_MASTER WHERE INST_ID='"+instid+
	 * "' and BRANCH_CODE='"+branchcode+"' and rownum <= 1";
	 * //System.out.println("Branch desc : " + qrydesc ); JdbcTemplate
	 * jdbcTemplate = new JdbcTemplate(dataSource); String desc_detail =
	 * (String)jdbcTemplate.queryForObject(qrydesc, String.class); return
	 * desc_detail; }
	 */

	public String getBranchDesc(String instid, String branchcode,
			JdbcTemplate jdbctemplate) throws Exception {

		/*
		 * String qrydesc =
		 * "SELECT trim(BRANCH_NAME)  FROM BRANCH_MASTER WHERE INST_ID='" +
		 * instid + "' and BRANCH_CODE='"+ branchcode.trim() +
		 * "' and rownum <= 1"; trace("Branch desc : " + qrydesc); String
		 * desc_detail = (String) jdbctemplate.queryForObject(qrydesc,
		 * String.class);
		 */

		// by gowtham-140819
		String qrydesc = "SELECT trim(BRANCH_NAME)  FROM BRANCH_MASTER WHERE INST_ID=?and BRANCH_CODE=? and rownum <= ?";
		trace("Branch desc : " + qrydesc);
		String desc_detail = (String) jdbctemplate.queryForObject(qrydesc,
				new Object[] { instid, branchcode.trim(), "1" }, String.class);

		return desc_detail;
	}

	/*
	 * public List generateBranchList(String instid) { JdbcTemplate jdbcTemplate
	 * = new JdbcTemplate(dataSource); String query=
	 * "select BRANCH_CODE,BRANCH_NAME from BRANCH_MASTER where INST_ID='"
	 * +instid+"' order by BRANCH_CODE"; ////enctrace("3030query : "+query);
	 * List branchList = jdbcTemplate.queryForList(query); return branchList; }
	 */

	
	public List generateBranchListPRE(String instid, JdbcTemplate jdbctemplate) {
		List branchList = null;
		try {

			/*
			 * String query =
			 * "select BRANCH_CODE,BRANCH_NAME from BRANCH_MASTER where INST_ID='"
			 * + instid+
			 * "' and AUTH_CODE='1' AND BRANCH_CODE!='000' order by BRANCH_CODE"
			 * ; enctrace(" branch query : " + query); branchList =
			 * jdbctemplate.queryForList(query);
			 */

			// BY GOWTHAM-210819
			
			/*SELECT BRANCH_CODE,(SELECT BRANCH_NAME FROM BRANCH_MASTER a WHERE trim(a.BRANCH_CODE)=trim(b.BRANCH_CODE) and AUTH_CODE='1' AND BRANCH_CODE!='000' )BRANCH_NAME 
			FROM Pers_PRE_DATA b WHERE b.GENERATED_DATE BETWEEN  SYSDATE-1 AND SYSDATE   
			ORDER BY TO_DATE(GENERATED_DATE) DESC;*/
			
			String query = "SELECT   b.BRANCH_CODE,(SELECT BRANCH_NAME FROM BRANCH_MASTER a WHERE trim(a.BRANCH_CODE)=trim(b.BRANCH_CODE) and AUTH_CODE='1' AND BRANCH_CODE!='000') BRANCH_NAME " 
			+" FROM Pers_PRE_DATA b WHERE b.inst_id='"+instid+"' and b.GENERATED_DATE BETWEEN  SYSDATE-1 AND SYSDATE group by b.branch_code "
			+ " ORDER BY  b.branch_code ";
			enctrace(" branch query : " + query);
		/*	branchList = jdbctemplate.queryForList(query, new Object[] {
					instid, "1", "000" });*/
			branchList = jdbctemplate.queryForList(query);

		} catch (Exception e) {
			trace("the exception for gettting branch list" + e.getMessage());
			e.printStackTrace();
		}
		return branchList;
	}
	
	public List generateBranchList(String instid, JdbcTemplate jdbctemplate) {
		List branchList = null;
		try {

			/*
			 * String query =
			 * "select BRANCH_CODE,BRANCH_NAME from BRANCH_MASTER where INST_ID='"
			 * + instid+
			 * "' and AUTH_CODE='1' AND BRANCH_CODE!='000' order by BRANCH_CODE"
			 * ; enctrace(" branch query : " + query); branchList =
			 * jdbctemplate.queryForList(query);
			 */

			// BY GOWTHAM-210819
		
			String query = "select BRANCH_CODE,BRANCH_NAME from BRANCH_MASTER where INST_ID=? and AUTH_CODE=? AND BRANCH_CODE!=? order by BRANCH_CODE";
			enctrace(" branch query : " + query);
			branchList = jdbctemplate.queryForList(query, new Object[] {
					instid, "1", "000" });

		} catch (Exception e) {
			trace("the exception for gettting branch list" + e.getMessage());
			e.printStackTrace();
		}
		return branchList;
	}
	
	public List generateBranchListINSTPRE(String instid, JdbcTemplate jdbctemplate) {
		List branchList = null;
		try {

			 
		String query="SELECT   b.BRANCH_CODE,(SELECT BRANCH_NAME FROM BRANCH_MASTER a WHERE trim(a.BRANCH_CODE)=trim(b.BRANCH_CODE) and AUTH_CODE='1' AND BRANCH_CODE!='000') BRANCH_NAME " 
			+" FROM inst_PRE_DATA b WHERE b.inst_id=? group by b.branch_code "
			+ " ORDER BY  b.branch_code ";
			//String query = "select BRANCH_CODE,BRANCH_NAME from BRANCH_MASTER where INST_ID=? and AUTH_CODE=? AND BRANCH_CODE!=? order by BRANCH_CODE";
			enctrace(" branch query : " + query);
			branchList = jdbctemplate.queryForList(query, new Object[] {
					instid  });

		} catch (Exception e) {
			trace("the exception for gettting branch list" + e.getMessage());
			e.printStackTrace();
		}
		return branchList;
	}


	public List generateBranchListCardGen(String instid,
			JdbcTemplate jdbctemplate) {
		String query = "select BRANCH_CODE,(SELECT BRANCH_NAME FROM BRANCH_MASTER a WHERE a.BRANCH_CODE=b.BRANCH_CODE)BRANCH_NAME from PERS_CARD_ORDER b WHERE INST_ID='"
				+ instid + "' AND ORDER_STATUS='01' AND MKCK_STATUS='P'";
		enctrace(" branch query : " + query);
		List branchList = jdbctemplate.queryForList(query);
		return branchList;
	}

	public List generateBranchList(String instid, String branchcode,
			JdbcTemplate jdbctemplate) throws Exception {
		String query = "select BRANCH_CODE,BRANCH_NAME from BRANCH_MASTER where INST_ID='"
				+ instid
				+ "' and BRANCH_CODE='"
				+ branchcode
				+ "' AND AUTH_CODE='1' order by BRANCH_CODE";
		enctrace(" branch query : " + query);
		List branchList = jdbctemplate.queryForList(query);
		return branchList;
	}

	public String genBranchDesc(String branchid, String instid,
			JdbcTemplate jdbcTemplate) throws Exception {
		String query = "select BRANCH_NAME from BRANCH_MASTER where INST_ID='"
				+ instid + "' and BRANCH_CODE='" + branchid + "'";
		// // enctrace("3030genBranchDesc query"+query) ;
		String temp = (String) jdbcTemplate.queryForObject(query, String.class);
		// trace("branch DESC"+temp);
		return temp;
	}

	/*
	 * public String getCardnofromprod(String accountno,String instid,
	 * JdbcTemplate jdbcTemplate) throws Exception { String query=
	 * "select CARD_NO from CARD_PRODUCTION where INST_ID='"+instid+
	 * "' and ACCOUNT_NO='"+accountno+"' AND ROWNUM=1"; ////enctrace(
	 * "3030genBranchDesc query"+query) ; String
	 * temp=(String)jdbcTemplate.queryForObject(query,String.class); //trace(
	 * "branch DESC"+temp); return temp; }
	 */

	public List getCardnofromprod(String accountno, String instid,
			JdbcTemplate jdbcTemplate) throws Exception {
		// BY SIVA
		/*
		 * String getcardlist =
		 * "select CARD_NO,HCARD_NO from CARD_PRODUCTION where INST_ID='" +
		 * instid+ "' and ACCOUNT_NO='" + accountno + "' AND ROWNUM=1";
		 */
		/*
		 * String getcardlist =
		 * "select MCARD_NO,ORG_CHN,(SELECT HCARD_NO FROM CARD_PRODUCTION_HASH WHERE ACCOUNT_NO='"
		 * + accountno + "' ) as HCARD_NO  from CARD_PRODUCTION where INST_ID='"
		 * + instid + "' and ACCOUNT_NO='" + accountno + "'";
		 */

		String getcardlist = "select MCARD_NO,ORG_CHN,(SELECT HCARD_NO FROM CARD_PRODUCTION_HASH WHERE ACCOUNT_NO='"
				+ accountno
				+ "' AND ROWNUM=1 ) as HCARD_NO  from CARD_PRODUCTION where INST_ID='"
				+ instid + "' and ACCOUNT_NO='" + accountno + "' AND ROWNUM=1";

		enctrace(" get card list query ::   " + getcardlist);
		List getcarddet = jdbcTemplate.queryForList(getcardlist);
		return getcarddet;
	}

	/*
	 * public String getCardnofromprodUsingcustid(String custid,String instid,
	 * JdbcTemplate jdbcTemplate) throws Exception { String query=
	 * "select CARD_NO from CARD_PRODUCTION where INST_ID='"+instid+
	 * "' and CIN='"+custid+"'"; ////enctrace("3030genBranchDesc query"+query) ;
	 * String temp=(String)jdbcTemplate.queryForObject(query,String.class);
	 * //trace("branch DESC"+temp); return temp; }
	 */

	public List getCardnofromprodUsingcustid(String custid, String instid,
			JdbcTemplate jdbcTemplate) throws Exception {

		String getcarddetails = "select ORG_CHN,(SELECT HCARD_NO FROM CARD_PRODUCTION_HASH WHERE CIN='"
				+ custid
				+ "' AND ROWNUM=1) as HCARD_NO from CARD_PRODUCTION where INST_ID='"
				+ instid + "' and CIN='" + custid + "' AND ROWNUM=1";
		enctrace(" get card list query : " + getcarddetails);
		List getcustdetails = jdbcTemplate.queryForList(getcarddetails);
		return getcustdetails;

	}

	// Sankar Start ##############################################////

	/*
	 * public synchronized String generateCardRefNumber(String instid, String
	 * subprodid, String cardrefnolen, JdbcTemplate jdbctemplate) throws
	 * Exception { StringBuilder cardrefno = null;
	 * 
	 * try { int chekdigitlen = 2; String cardrefnoqry =
	 * "SELECT CARDREFNO_SEQ FROM INSTPROD_DETAILS WHERE INST_ID='" + instid +
	 * "' AND SUB_PROD_ID='" + subprodid + "'"; ////
	 * enctrace("3030cardrefnoqry111"+cardrefnoqry); String cardrefnoseq =
	 * (String) jdbctemplate.queryForObject(cardrefnoqry, String.class); String
	 * cardrequpdqry =
	 * "UPDATE INSTPROD_DETAILS SET CARDREFNO_SEQ=CARDREFNO_SEQ+1 WHERE INST_ID='"
	 * + instid + "' AND SUB_PROD_ID='" + subprodid + "'"; ////
	 * enctrace("3030cardrefnoqry22"+cardrequpdqry); int x =
	 * jdbctemplate.update(cardrequpdqry); if (x != 1) { return cardrefno =
	 * null; }
	 * 
	 * int padzerolen = Integer.parseInt(cardrefnolen) - subprodid.length() -
	 * chekdigitlen; cardrefno = subprodid + paddingZero(cardrefnoseq,
	 * padzerolen); int checkdigit = 0; for (int i = 0; i < chekdigitlen; i++) {
	 * checkdigit = this.generateCheckDigit(cardrefno); cardrefno = cardrefno +
	 * Integer.toString(checkdigit); } if (Integer.parseInt(cardrefnolen) ==
	 * cardrefno.length()) { return cardrefno; } } catch
	 * (EmptyResultDataAccessException e) { cardrefno = null; } return
	 * cardrefno;
	 * 
	 * }
	 */

	public synchronized String generateCardRefNumber(String instid,
			String subprodid, String cardrefnolen, JdbcTemplate jdbctemplate)
			throws Exception {
		String cardrefno = null;

		try {
			int chekdigitlen = 2;

			/*
			 * String cardrefnoqry =
			 * "SELECT CARDREFNO_SEQ FROM INSTPROD_DETAILS " + "WHERE INST_ID='"
			 * + instid+ "' AND SUB_PROD_ID='" + subprodid + "'"; ////
			 * enctrace("3030cardrefnoqry111"+cardrefnoqry); String cardrefnoseq
			 * = (String) jdbctemplate.queryForObject(cardrefnoqry,
			 * String.class);
			 */

			// by gowtham-220819
			String cardrefnoqry = "SELECT CARDREFNO_SEQ FROM INSTPROD_DETAILS "
					+ "WHERE INST_ID=? AND SUB_PROD_ID=?";
			// // enctrace("3030cardrefnoqry111"+cardrefnoqry);
			String cardrefnoseq = (String) jdbctemplate.queryForObject(
					cardrefnoqry, new Object[] { instid, subprodid },
					String.class);

			String cardrequpdqry = "UPDATE INSTPROD_DETAILS SET CARDREFNO_SEQ=CARDREFNO_SEQ+1 WHERE INST_ID='"
					+ instid + "' AND SUB_PROD_ID='" + subprodid + "'";
			// // enctrace("3030cardrefnoqry22"+cardrequpdqry);
			int x = jdbctemplate.update(cardrequpdqry);
			if (x != 1) {
				return cardrefno = null;
			}

			int padzerolen = Integer.parseInt(cardrefnolen)
					- subprodid.length() - chekdigitlen;
			cardrefno = subprodid + paddingZero(cardrefnoseq, padzerolen);
			int checkdigit = 0;
			for (int i = 0; i < chekdigitlen; i++) {
				checkdigit = this.generateCheckDigit(new StringBuilder(
						cardrefno));
				cardrefno = cardrefno + Integer.toString(checkdigit);
			}
			if (Integer.parseInt(cardrefnolen) == cardrefno.length()) {
				return cardrefno;
			}
		} catch (EmptyResultDataAccessException e) {
			cardrefno = null;
		}
		return cardrefno;

	}

	public String generateAccountNumber(String instid, String curcode,
			JdbcTemplate jdbctemplate) throws Exception {
		String acctno = null;
		int curacctseq = this.fchAcctSequance(instid, jdbctemplate);
		trace("Getting current acctno seq : " + curacctseq);
		String acctnolen = this.getAccountNoLength(instid, jdbctemplate);
		trace("Getting current acctno len : " + acctnolen);
		acctno = curcode
				+ this.paddingZero(Integer.toString(curacctseq),
						Integer.parseInt(acctnolen) - curcode.length());
		trace("Generated acct number is : " + acctno);
		if (Integer.parseInt(acctnolen) != acctno.length()) {
			addActionError("Unable to generate acct number. Try again.");
			return null;
		}
		return acctno;
	}

	public synchronized String cinnumberGeneratoer(String instname,
			JdbcTemplate jdbctemplate) {
		String CIN = "N", cin_no = "N", newcin = "N", cinmax = "X";
		char Alpha_chars[] = { 'Z', 'X', 'Y', 'W', 'V', 'I', 'J', 'L', 'U',
				'H', 'O', 'M', 'N' };
		char[] copyTo = new char[2];
		Random random = new Random();
		int cinlen = 0, rannum;
		;
		try {

			/*
			 * String cin_len =
			 * "select CIN_LEN from INSTITUTION where trim(INST_ID)='" +
			 * instname + "'"; String cinno =
			 * "select CIN_NO from SEQUENCE_MASTER where trim(INST_ID)='" +
			 * instname + "'"; enctrace(cinno); cinlen = (Integer)
			 * jdbctemplate.queryForObject(cin_len, Integer.class); cin_no =
			 * (String) jdbctemplate.queryForObject(cinno, String.class);
			 */

			// by gowtham-210819
			String cin_len = "select CIN_LEN from INSTITUTION where trim(INST_ID)=?";
			String cinno = "select CIN_NO from SEQUENCE_MASTER where trim(INST_ID)=?";
			enctrace(cinno);
			cinlen = (Integer) jdbctemplate.queryForObject(cin_len,
					new Object[] { instname }, Integer.class);
			cin_no = (String) jdbctemplate.queryForObject(cinno,
					new Object[] { instname }, String.class);

			newcin = cinNumberformatter(cinlen, cin_no);
			if (!(newcin.equals("N")) && !(newcin.equals("X"))) {
				cinmax = checkMaxorderrefnum(newcin, cinlen);
				if (!(cinmax.equals("X")) && !(cinmax.equals("N"))) {
					if (cinmax.equals("M")) {
						CIN = "M";
					} else {
						for (int i = 0; i <= 1; i++) {
							rannum = randomgenerator(1, 12, random);
							copyTo[i] = Alpha_chars[rannum];
						}
						CIN = instname + copyTo[0] + copyTo[1] + cinmax;
					}
				}
			}
		} catch (Exception e) {
			// System.out.println("Error While CIN Generation"+e.getMessage());
			CIN = "E";
		}
		trace("Generated customer id Is :  " + CIN + " \n");
		return CIN;
	}

	public String cinNumberformatter(int cinlen, String cinno) {
		String cinvalue = "X";
		int newcinlen = 0;
		int curr_newcin_len = 0;
		int currcin_len = cinno.length();
		// trace("Current CIN Length====> "+currcin_len+" CIN length Configured
		// is "+cinlen);
		if (currcin_len == cinlen) {
			cinvalue = cinno;
		} else if (currcin_len < cinlen) {
			newcinlen = cinno.length();
			while (newcinlen != cinlen) {
				for (int j = 0; j < cinlen; j++) {
					cinno = "0" + cinno;
					curr_newcin_len = cinno.length();
					if (curr_newcin_len == cinlen) {
						break;
					}
				}
				newcinlen = cinno.length();
			}
			cinvalue = cinno;
		}
		return cinvalue;
	}

	public int randomgenerator(int s, int e, Random ran) {
		long range = (long) e - (long) s + 1;
		long fraction = (long) (range * ran.nextDouble());
		int randomNumber = (int) (fraction + s);
		return randomNumber;
	}

	/*
	 * public synchronized String generateorderRefno(String inst_id,
	 * JdbcTemplate jdbcTemplate) throws Exception { String
	 * Order_ref_no="N",refnomax="N",refnum="N"; int ref_len=0;
	 * 
	 * String refnum_len =
	 * "select ORD_REF_LEN from INSTITUTION where trim(INST_ID)='"+inst_id+"'";
	 * ////enctrace("3030refnum_len : " + refnum_len ); ref_len =
	 * (Integer)jdbcTemplate.queryForObject(refnum_len, Integer.class); trace(
	 * "Order ref-no len : " + ref_len ); String refno =
	 * "select 	ORDER_REFNO from SEQUENCE_MASTER where trim(INST_ID)='"
	 * +inst_id+"'"; ////enctrace("3030refno : " + refno); refnum =
	 * (String)jdbcTemplate.queryForObject(refno, String.class); trace(
	 * "Ref-No seq : " + refnum ); if(!(refnum.equals("N")) && ref_len !=0) {
	 * Order_ref_no = orderreferenceno(refnum,ref_len); }
	 * if(!(Order_ref_no.equals("N")) && !(Order_ref_no.equals("X"))) {
	 * refnomax=checkMaxorderrefnum(Order_ref_no,ref_len); }
	 * if(!(refnomax.equals("N")) && !(refnomax.equals("X"))) {
	 * if(refnomax.equals("M")) { Order_ref_no = "M"; } else { Order_ref_no =
	 * refnomax;
	 * 
	 * } } trace("Generatd order-ref-num : " + Order_ref_no); return
	 * Order_ref_no; }
	 */

	public synchronized String generateorderRefno(String inst_id,
			JdbcTemplate jdbcTemplate) throws Exception {
		String Order_ref_no = "N", refnomax = "N", refnum = "N";
		int ref_len = 0;

		/*
		 * String refnum_len =
		 * "select ORD_REF_LEN from INSTITUTION where trim(INST_ID)='" + inst_id
		 * + "'"; enctrace("refnum_len : " + refnum_len); ref_len = (Integer)
		 * jdbcTemplate.queryForObject(refnum_len, Integer.class);
		 */

		// by gowham210819
		String refnum_len = "select ORD_REF_LEN from INSTITUTION where trim(INST_ID)=?";
		enctrace("refnum_len : " + refnum_len);
		ref_len = (Integer) jdbcTemplate.queryForObject(refnum_len,
				new Object[] { inst_id }, Integer.class);

		trace("Order refme-no len : " + ref_len);
		// String refno = "select ORDER_REFNO from SEQUENCE_MASTER where
		// trim(INST_ID)='"+inst_id+"'";

		/*
		 * String refno = "SELECT ORDERREF_SEQUENCE('" + inst_id +
		 * "') FROM DUAL"; enctrace("refno : " + refno); refnum = (String)
		 * jdbcTemplate.queryForObject(refno, String.class);
		 */

		// by gowtham-300819
		String refno = "SELECT ORDERREF_SEQUENCE(?) FROM DUAL";
		enctrace("refno : " + refno);
		refnum = (String) jdbcTemplate.queryForObject(refno,
				new Object[] { inst_id }, String.class);

		trace("Ref-No seq : " + refnum);
		if (!(refnum.equals("N")) && ref_len != 0) {
			Order_ref_no = orderreferenceno(refnum, ref_len);
		}
		if (!(Order_ref_no.equals("N")) && !(Order_ref_no.equals("X"))) {
			refnomax = checkMaxorderrefnum(Order_ref_no, ref_len);
		}
		if (!(refnomax.equals("N")) && !(refnomax.equals("X"))) {
			if (refnomax.equals("M")) {
				Order_ref_no = "M";
			} else {
				Order_ref_no = refnomax;

			}
		}
		trace("Generatd order-ref-num : " + Order_ref_no);
		return Order_ref_no;
	}

	public String orderreferenceno(String refnum, int ref_len) {
		String ref_num = "X";
		// trace("############### Refnum Recived is " + refnum + " Refnum Len "
		// + ref_len);
		int curr_len = refnum.length();
		if (curr_len == ref_len) {
			ref_num = refnum;
		} else if (curr_len < ref_len) {
			int refnum_len = refnum.length();
			trace(" The Len of Exsist Ref num is " + refnum_len);
			int newlength;
			while (refnum_len != ref_len) {
				trace("Inside While Loop " + ref_len);
				for (int j = 0; j < ref_len; j++) {
					refnum = "0" + refnum;
					newlength = refnum.length();
					if (ref_len == newlength) {
						break;
					}
				}
				// trace(" Ref Num Generated is " + refnum);

				refnum_len = refnum.length();
				// trace("refnum_len===InSide While Loop > " + refnum_len);
			}
			ref_num = refnum;
		}
		return ref_num;
	}

	public String checkMaxorderrefnum(String ref, int len) {
		// trace("RefNum Received ==>"+ref+" The Len is ###==> "+len);

		String maxno = "X";
		String maxnum = "";
		for (int n = 0; n < len; n++) {
			maxnum = maxnum + "9";
			// trace("== maxnum =="+maxnum+"=======");
		}
		// trace("Refnum Before Parse int ==> "+ref);
		long curr_refnum = Long.parseLong(ref);
		// trace("maxnum After "+maxnum.trim());
		long maxnumber = Long.parseLong(maxnum);
		// trace("curr_refnum ====> "+curr_refnum);
		// trace("maxnumber====>"+maxnumber);
		if (curr_refnum == maxnumber) {
			// trace("Both Values are Same ");
			maxno = "M";
		} else {
			maxno = ref;
		}
		// trace("maxno====> "+maxno);

		return maxno;
	}

	/*
	 * public int executeTransaction(String update_qury) { int result =
	 * -1,updated = -1;
	 * 
	 * JdbcTemplate jdbctemplate = new JdbcTemplate(dataSource);
	 * 
	 * updated = jdbctemplate.update(update_qury); if(updated == 1) { result =
	 * 1; } return result; }
	 */
	// Method overloading....

	int result = -1, updated = -1;

	public int executeTransaction(String update_qury, JdbcTemplate jdbctemplate) {
		Date date = new Date();
		String instid;
		String cardtpeidnew;

		updated = jdbctemplate.update(update_qury);
		if (updated > 0) {
			result = 1;
		}
		return result;
	}

	/**
	 * 
	 * @param update_qury
	 * @param jdbctemplate
	 * @return
	 */

	public int executeTransaction1(String update_qury, JdbcTemplate jdbctemplate) {
		int result = -1, updated = -1;

		updated = jdbctemplate.update(update_qury);
		if (updated > 0) {
			result = 1;
		}
		return result;
	}

	public int executeMerchantTransaction(String update_qury,
			JdbcTemplate jdbctemplate) {
		int result = -1, updated = -1;
		updated = jdbctemplate.update(update_qury);
		if (updated == 1) {
			result = 1;
		}
		return result;
	}

	public synchronized String updateOrderrefnumcount(String instid) {

		return "update SEQUENCE_MASTER set ORDER_REFNO=ORDER_REFNO+1,CIN_NO=CIN_NO+1 where INST_ID='"
				+ instid + "'";

		/*
		 * //by gowtham-220819 return
		 * "update SEQUENCE_MASTER set ORDER_REFNO=ORDER_REFNO+1,CIN_NO=CIN_NO+1 where INST_ID=? "
		 * ;
		 */

	}

	public synchronized String updatecustidcount(String instid) {

		/*
		 * return "update SEQUENCE_MASTER set CIN_NO=CIN_NO+1 where INST_ID='" +
		 * instid + "'";
		 */

		// by gowtham=220819
		return "update SEQUENCE_MASTER set CIN_NO=CIN_NO+1 where INST_ID=?";
	}

	public synchronized String updateCINcount(String instid) {
		return "update SEQUENCE_MASTER set CIN_NO=CIN_NO+1 where INST_ID='"
				+ instid + "'";
	}

	public List getPersonalOrderList(String instid, String filtercond,
			JdbcTemplate jdbctemplate) throws Exception {

		/*
		 * String listoforderqry =
		 * "SELECT ORDER_REF_NO, 	CARD_TYPE_ID, BIN,BRANCH_CODE,PRODUCT_CODE,SUB_PROD_ID, CARD_QUANTITY, EMBOSSING_NAME, MAKER_ID, to_char(ORDERED_DATE, 'dd-MON-yyyy') as ORDERED_DATE,CIN, decode(MKCK_STATUS,'M','Waiting For Authorization','P','Authorized','D','DeAuthorized') as STATUS , NVL(REMARKS,'--') AS REMARKS FROM PERS_CARD_ORDER WHERE INST_ID='"
		 * +instid+"'"+filtercond +" order by ORDER_REF_NO DESC"; enctrace(
		 * "getinstatcarorderlist is  : " + listoforderqry );
		 */

		String listoforderqry = "SELECT ORDER_REF_NO, CARD_TYPE_ID, BIN,BRANCH_CODE,PRODUCT_CODE,SUB_PROD_ID, CARD_QUANTITY, EMBOSSING_NAME, MAKER_ID, to_char(ORDERED_DATE, 'dd-MON-yyyy') as ORDERED_DATE,CIN, decode(MKCK_STATUS,'M','Waiting For Authorization','P','Authorized','D','DeAuthorized') as STATUS , NVL(REMARKS,'--') AS REMARKS,"
				+ " (SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID=A.INST_ID and PRODUCT_CODE=A.PRODUCT_CODE) PRODBINDESC,"
				+ " (SELECT PRD_DESC FROM PRODUCTINFO WHERE INST_ID=A.INST_ID and BIN=A.BIN and rownum <= 1) CARDTYPEDESC, "
				+ " (SELECT USERNAME  FROM USER_DETAILS WHERE INSTID=A.INST_ID and USERID=A.MAKER_ID OR USERNAME=A.MAKER_ID and rownum <= 1) USERNAME, "
				+ " (SELECT SUB_PRODUCT_NAME FROM INSTPROD_DETAILS WHERE INST_ID=A.INST_ID and SUB_PROD_ID=A.SUB_PROD_ID) SUBPRODDESC"
				+ " FROM PERS_CARD_ORDER A WHERE INST_ID='"
				+ instid
				+ "'"
				+ filtercond + " order by ORDER_REF_NO DESC";

		enctrace("getinstatcarorderlist is  : " + listoforderqry);
		List listoforder = jdbctemplate.queryForList(listoforderqry);

		/*
		 * // by gowtham String listoforderqry =
		 * "SELECT ORDER_REF_NO, CARD_TYPE_ID, BIN,BRANCH_CODE,PRODUCT_CODE,SUB_PROD_ID, CARD_QUANTITY, EMBOSSING_NAME, MAKER_ID, to_char(ORDERED_DATE, 'dd-MON-yyyy') as ORDERED_DATE,CIN, decode(MKCK_STATUS,'M','Waiting For Authorization','P','Authorized','D','DeAuthorized') as STATUS , NVL(REMARKS,'--') AS REMARKS,"
		 * +
		 * " (SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID=A.INST_ID and PRODUCT_CODE=A.PRODUCT_CODE) PRODBINDESC,"
		 * +
		 * " (SELECT BIN_DESC FROM PRODUCTINFO WHERE INST_ID=A.INST_ID and BIN=A.BIN and rownum <= 1) CARDTYPEDESC, "
		 * +
		 * " (SELECT USERNAME  FROM USER_DETAILS WHERE INSTID=A.INST_ID and USERID=A.MAKER_ID OR USERNAME=A.MAKER_ID and rownum <= 1) USERNAME, "
		 * +
		 * " (SELECT SUB_PRODUCT_NAME FROM INSTPROD_DETAILS WHERE INST_ID=A.INST_ID and SUB_PROD_ID=A.SUB_PROD_ID) SUBPRODDESC"
		 * + " FROM PERS_CARD_ORDER A WHERE INST_ID=? " + filtercond +
		 * " order by ORDER_REF_NO DESC";
		 * 
		 * enctrace("getinstatcarorderlist is  : " + listoforderqry); List
		 * listoforder = jdbctemplate.queryForList(listoforderqry, new Object[]
		 * { instid });
		 */

		/*
		 * ListIterator itr = listoforder.listIterator(); while( itr.hasNext()
		 * ){ Map temp = (Map)itr.next(); String bin = (String)temp.get("BIN");
		 * trace("BIN IS==> "+bin); String usercode =
		 * (String)temp.get("MAKER_ID"); trace("USER CODE IS==> "+usercode);
		 * String prodcode = (String)temp.get("PRODUCT_CODE"); trace(
		 * "PRODUCT CODE IS==> "+prodcode); String productdesc =
		 * getProductdesc(instid,prodcode, jdbctemplate); trace(
		 * "productdesc==> "+productdesc); String cardtypedesc =
		 * getCardTypeDesc(instid, bin, jdbctemplate); trace("cardtypedesc==> "
		 * +cardtypedesc); String subprodid = (String)temp.get("SUB_PROD_ID");
		 * String subproductname = getSubProductdesc(instid, subprodid,
		 * jdbctemplate); String username = getUserName(instid, usercode,
		 * jdbctemplate); trace("username==> "+username);
		 * temp.put("PRODBINDESC", productdesc); temp.put("CARDTYPEDESC",
		 * cardtypedesc); temp.put("SUBPRODDESC", subproductname);
		 * temp.put("USERNAME", username); itr.remove(); itr.add(temp); }
		 */
		return listoforder;

	}

	public List getCustomerdetails(String instid, String orderrefnum,
			JdbcTemplate jdbcTemplate) throws Exception {

		List custdetails = null;
		String cin_qury = "(select CIN from PERS_CARD_ORDER where INST_ID='"
				+ instid + "' AND ORDER_REF_NO='" + orderrefnum + "')";

		String custinfo_qury = "select FNAME||' '||decode(MNAME,'NO_DATA','',MNAME)||' '||LNAME as FULNAME,FATHER_NAME,"
				+ "MOTHER_NAME,decode(MARITAL_STATUS,'M','Married','U','Unmarried') as MARITAL_STATUS,"
				+ "decode(GENDER,'M','Male','F','Female') as GENDER,to_char(DOB,'DD-MON-YYYY') as DOB,NATIONALITY,EMAIL_ADDRESS,"
				+ "MOBILE_NO,PHONE_NO,OCCUPATION,ID_NUMBER,ID_DOCUMENT,"
				+ "POST_ADDR1 as pone,POST_ADDR2 as ptwo,POST_ADDR3 as "
				+ "pthree,POST_ADDR4 as pfour,RES_ADDR1 as rone,RES_ADDR2 as rtwo,RES_ADDR3 as rthree,RES_ADDR4 as rfour,"
				+ "to_char(MAKER_DATE,'DD-MON-YY') as MAKER_DATE from IFP_CUSTINFO_PROCESS "
				+ "where INST_ID='" + instid + "' and  CIN=" + cin_qury;
		// // enctrace("3030custinfo_qury :"+custinfo_qury);
		custdetails = jdbcTemplate.queryForList(custinfo_qury);

		ListIterator itr = custdetails.listIterator();
		while (itr.hasNext()) {
			Map custdet = (Map) itr.next();
			String nationid = (String) custdet.get("NATIONALITY");
			String nation = getNation(nationid, jdbcTemplate);
			custdet.put("NATION", nation);
			itr.remove();
			itr.add(custdet);
		}

		return custdetails;
	}

	public String getNation(String nationality, JdbcTemplate jdbctemplate)
			throws Exception {
		String nationname = null;
		try {
			String nation_qury = "SELECT NATION FROM IFP_COUNTRY_MASTER WHERE NATION_ID='"
					+ nationality + "' and rownum <= 1";
			// System.out.println( " getProductdesc " + nation_qury);
			nationname = (String) jdbctemplate.queryForObject(nation_qury,
					String.class);
		} catch (EmptyResultDataAccessException e) {
			nationname = "--";
		}

		return nationname;
	}

	public int checkCardgenerationstatus(String instid, String branch,
			JdbcTemplate jdbcTemplate) {

		/*
		 * String check_qury = "SELECT COUNT(*) AS CNT FROM CARDGEN_STATUS" +
		 * " WHERE INST_ID='" + instid+ "' AND BRANCH_CODE='" + branch +
		 * "' AND CARDGEN_STATUS='C'"; //// enctrace("3030check_qury : " +
		 * check_qury); int cardgenstatus =
		 * jdbcTemplate.queryForInt(check_qury);
		 */

		// by gowtham-220819
		String check_qury = "SELECT COUNT(*) AS CNT FROM CARDGEN_STATUS"
				+ " WHERE INST_ID=? AND BRANCH_CODE=? AND CARDGEN_STATUS=?";
		// // enctrace("3030check_qury : " + check_qury);
		int cardgenstatus = jdbcTemplate.queryForInt(check_qury, new Object[] {
				instid, branch, "C" });

		// return cardgenstatus;
		return 0;
	}

	public int checkPingenerationstatus(String instid, String branch,
			JdbcTemplate jdbcTemplate) {

		/*
		 * String check_qury =
		 * "SELECT COUNT(*) FROM CARDGEN_STATUS WHERE INST_ID='" + instid +
		 * "' AND BRANCH_CODE='"+ branch + "' AND CARDGEN_STATUS='P'"; int
		 * pingenstatus = jdbcTemplate.queryForInt(check_qury);
		 */

		// by gowtham260819
		String check_qury = "SELECT COUNT(*) FROM CARDGEN_STATUS WHERE INST_ID=? AND BRANCH_CODE=? AND CARDGEN_STATUS=? ";
		int pingenstatus = jdbcTemplate.queryForInt(check_qury, new Object[] {
				instid, branch, "P" });

		// return pingenstatus;
		return 0;
	}

	/*
	 * public int executeIfpQuery( String qry, JdbcTemplate jdbctemplate ) { //
	 * this method is using for single query insert or update or delete
	 * 
	 * 
	 * try {
	 * 
	 * int x = jdbctemplate.update(qry); trace(x+" Rows Affected "); if( x > 0
	 * ){
	 * 
	 * commitTxn(jdbctemplate);
	 * 
	 * return 1; }else{ //trace("Txn Failed Roll Back");
	 * rollbackTxn(jdbctemplate); return -1; }
	 * 
	 * } catch (Exception e) { rollbackTxn(jdbctemplate); trace("Exception : " +
	 * e.getMessage() ); e.printStackTrace(); //trace(
	 * "Error while insert the qry _____ "+ e + "_________"+qry ); return -1; }
	 * }
	 */

	public Personalizeorderdetails gettingOrderdetailsByCin(String instid,
			String order_refnum, JdbcTemplate jdbctemplate) {
		Personalizeorderdetails personaloder = null;
		List orderdetailslist;
		String inst_id = "0", order_ref_no = "0",bulk_reg_id="",cardFlag="", card_type_id = "0", mcardno = "0", sub_prod_id = "0", product_code = "0", card_quantity = "0", embossing_name = "0", encode_data = "0", branch_code = "0", bin = "0", cin = "0", appno = "0", appdate = "0";

		/*
		 * String order_qury =
		 * "SELECT INST_ID,ORDER_REF_NO,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,CARD_QUANTITY,EMBOSSING_NAME,ENCODE_DATA,BRANCH_CODE,BRANCH_CODE,BIN,to_char(APP_DATE,'dd-MON-yyyy') as APP_DATE,CIN,APP_NO FROM PERS_CARD_ORDER "
		 * + "WHERE INST_ID='" + instid + "' AND CIN='" + order_refnum + "'";
		 * trace("order_qury~~~~~~>" + order_qury); orderdetailslist =
		 * jdbctemplate.queryForList(order_qury);
		 */

		// by gowtham-220819
		String order_qury = "SELECT INST_ID,ORDER_REF_NO,CARD_TYPE_ID,SUB_PROD_ID,BULK_REG_ID,CARD_FLAG,PRODUCT_CODE,CARD_QUANTITY,EMBOSSING_NAME,ENCODE_DATA,BRANCH_CODE,BRANCH_CODE,BIN,to_char(APP_DATE,'dd-MON-yyyy') as APP_DATE,CIN,APP_NO FROM PERS_CARD_ORDER "
				+ "WHERE INST_ID=? AND CIN=?";
		trace("order_qury~~~~~~>" + order_qury);
		orderdetailslist = jdbctemplate.queryForList(order_qury, new Object[] {
				instid, order_refnum });

		trace("orderdetailslist~~~~>>>>>>>" + orderdetailslist);
		int i = 0;
		Iterator orderitr = orderdetailslist.iterator();
		while (orderitr.hasNext()) {
			trace("##################      " + i + "    ##############");
			Map ordermap = (Map) orderitr.next();
			inst_id = (String) ordermap.get("INST_ID");
			order_ref_no = (String) ordermap.get("ORDER_REF_NO");
			card_type_id = (String) ordermap.get("CARD_TYPE_ID");
			sub_prod_id = (String) ordermap.get("SUB_PROD_ID");
			product_code = (String) ordermap.get("PRODUCT_CODE");
			card_quantity = (String) ordermap.get("CARD_QUANTITY");
			embossing_name = (String) ordermap.get("EMBOSSING_NAME");
			encode_data = (String) ordermap.get("ENCODE_DATA");
			branch_code = (String) ordermap.get("BRANCH_CODE");
			bin = (String) ordermap.get("BIN");
			cin = (String) ordermap.get("CIN");
			appno = (String) ordermap.get("APP_NO");
			appdate = (String) ordermap.get("APP_DATE");
		}
		personaloder = new Personalizeorderdetails(inst_id, order_ref_no,
				card_type_id, sub_prod_id, product_code, card_quantity,
				embossing_name, encode_data, branch_code, bin, cin, appno,
				appdate, mcardno);
		return personaloder;
	}

	public Personalizeorderdetails gettingOrderdetails(String instid,
			String order_refnum, JdbcTemplate jdbctemplate) {
		Personalizeorderdetails personaloder = null;
		List orderdetailslist;
		String inst_id = "0", order_ref_no = "0", card_type_id = "0", sub_prod_id = "0", mcardno = "0", product_code = "0", card_quantity = "0", embossing_name = "0", encode_data = "0", branch_code = "0", bin = "0", cin = "0", appno = "0", appdate = "0";
		String order_qury = "SELECT INST_ID,ORDER_REF_NO,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,CARD_QUANTITY,EMBOSSING_NAME,ENCODE_DATA,BRANCH_CODE,BRANCH_CODE,BIN,to_char(APP_DATE,'dd-MON-yyyy') as APP_DATE,CIN,APP_NO FROM PERS_CARD_ORDER "
				+ "WHERE INST_ID='"
				+ instid
				+ "' AND ORDER_REF_NO='"
				+ order_refnum + "'";
		trace("order_qury~~~~~~>" + order_qury);
		orderdetailslist = jdbctemplate.queryForList(order_qury);
		trace("orderdetailslist~~~~>>>>>>>" + orderdetailslist);
		int i = 0;
		Iterator orderitr = orderdetailslist.iterator();
		while (orderitr.hasNext()) {
			trace("##################      " + i + "    ##############");
			Map ordermap = (Map) orderitr.next();
			inst_id = (String) ordermap.get("INST_ID");
			order_ref_no = (String) ordermap.get("ORDER_REF_NO");
			card_type_id = (String) ordermap.get("CARD_TYPE_ID");
			sub_prod_id = (String) ordermap.get("SUB_PROD_ID");
			product_code = (String) ordermap.get("PRODUCT_CODE");
			card_quantity = (String) ordermap.get("CARD_QUANTITY");
			embossing_name = (String) ordermap.get("EMBOSSING_NAME");
			encode_data = (String) ordermap.get("ENCODE_DATA");
			branch_code = (String) ordermap.get("BRANCH_CODE");
			bin = (String) ordermap.get("BIN");
			cin = (String) ordermap.get("CIN");
			appno = (String) ordermap.get("APP_NO");
			appdate = (String) ordermap.get("APP_DATE");
		}
		personaloder = new Personalizeorderdetails(inst_id, order_ref_no,
				card_type_id, sub_prod_id, product_code, card_quantity,
				embossing_name, encode_data, branch_code, bin, cin, appno,
				appdate, mcardno);
		return personaloder;
	}

	public Personalizeorderdetails gettingBindetails(String instid,
			String binno, JdbcTemplate jdbcTemplate) throws Exception {
		Personalizeorderdetails bindetail = null;
		List bindetailslist;

		String chnlen = "0", prodcard = "0", brcode = "0", baselen = "0", cardscount = "0", chn_base_no = "0", apptype_attached = "0", apptype_value = "0", servicecode = "0";

		/*
		 * String bin_qury =
		 * "SELECT CHNLEN,ATTACH_PRODTYPE_CARDTYPE,ATTACH_BRCODE,BASELEN,NOS_CARDS_GEN,CHN_BASE_NO,ATTACH_APP_TYPE FROM PRODUCTINFO"
		 * + " WHERE INST_ID='"+ instid + "' AND BIN='" + binno + "'";
		 * enctrace("bin_qury__111111" + bin_qury); bindetailslist =
		 * jdbcTemplate.queryForList(bin_qury);
		 */

		// by gowtham-220819
		String bin_qury = "SELECT CHN_LENGTH,BASE_NO_LEN,CHN_BASE_NO,CUSTOMER_ID FROM PRODUCTINFO"
				+ " WHERE INST_ID=? AND BIN=?";
		enctrace("bin_qury__111111" + bin_qury);
		bindetailslist = jdbcTemplate.queryForList(bin_qury, new Object[] {
				instid, binno });

		if (!bindetailslist.isEmpty()) {
			Iterator binitr = bindetailslist.iterator();
			while (binitr.hasNext()) {
				Map binmap = (Map) binitr.next();
				chnlen = (String) binmap.get("CHNLEN");
				prodcard = (String) binmap.get("ATTACH_PRODTYPE_CARDTYPE");
				brcode = (String) binmap.get("ATTACH_BRCODE");
				baselen = (String) binmap.get("BASELEN");
				cardscount = (String) binmap.get("NOS_CARDS_GEN");
				chn_base_no = (String) binmap.get("CHN_BASE_NO");
				apptype_attached = (String) binmap.get("ATTACH_APP_TYPE");
				servicecode = (String) binmap.get("SERVICE_CODE");
				apptype_value = this.getAppTypeValue(instid, jdbcTemplate);
			}
			bindetail = new Personalizeorderdetails(chnlen, prodcard, brcode,
					baselen, cardscount, chn_base_no, apptype_attached,
					apptype_value, servicecode);
		}
		return bindetail;
	}

	public String getAppTypeValue(String instid, JdbcTemplate jdbctemplate)
			throws Exception {
		String apptypeval = null;

		/*
		 * String applenqry = "SELECT APP_CODE FROM INSTITUTION WHERE INST_ID='"
		 * + instid + "'"; //// enctrace("3030applenqry :" + applenqry ); try {
		 * apptypeval = (String) jdbctemplate.queryForObject(applenqry,
		 * String.class);
		 */

		// by gowtham210819
		String applenqry = "SELECT APP_CODE FROM INSTITUTION WHERE INST_ID=? ";
		// // enctrace("3030applenqry :" + applenqry );
		try {
			apptypeval = (String) jdbctemplate.queryForObject(applenqry,
					new Object[] { instid }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return apptypeval;
	}

	public String gettingBreakupvalue(String inst, String bin, String cardtype,
			String sunprod, String productcode, String branch,
			JdbcTemplate jdbctemplate) {
		// trace("##################################1");
		String breakupvalu = "";
		List brak_list = null;
		String prodcardtype = "N", branchadd = "N";

		String breakup_qury = "SELECT ATTACH_PRODTYPE_CARDTYPE as PRODCARD,ATTACH_BRCODE as BRCODE FROM PRODUCTINFO WHERE inst_id='"
				+ inst + "' AND BIN='" + bin + "'";
		// trace("##################################2"+breakup_qury);
		brak_list = jdbctemplate.queryForList(breakup_qury);
		Iterator itr = brak_list.iterator();
		while (itr.hasNext()) {
			Map brk = (Map) itr.next();
			prodcardtype = (String) brk.get("PRODCARD");
			branchadd = (String) brk.get("BRCODE");
		}
		// trace("The Vaklue 1 is ==> "+prodcardtype+" The Value 2 is
		// ==>"+branchadd);
		if (prodcardtype.equals("C")) {
			breakupvalu = breakupvalu + cardtype;
		} else if (prodcardtype.equals("P")) {
			breakupvalu = breakupvalu + productcode;
		}
		if (branchadd.equals("Y")) {
			breakupvalu = breakupvalu + branch;
		}
		// trace("breakupvalu ====> "+breakupvalu);
		return breakupvalu;
	}

	public String getChnbreakupvalues(String instid, String prodcardtype,
			String attchbranch, String cardtype, String subprod,
			String productcode, String branch, String apptypeattached,
			String apptypevalue) {
		String breakupvalu = "";
		trace("prodcardtype " + prodcardtype + "\nattchbranch " + attchbranch
				+ "\ncardtype  " + cardtype + "\nsunprod " + subprod
				+ "\nproductcode " + productcode + "\n branch " + branch);
		if (apptypeattached != null) {
			if (apptypeattached.equals("Y")) {
				breakupvalu = breakupvalu + apptypevalue;
			}
		}
		if (prodcardtype.equals("C")) {
			breakupvalu = breakupvalu + cardtype;
			// System.out.println("breakupvalu inside C -> "+breakupvalu);
		} else if (prodcardtype.equals("P")) {
			// breakupvalu = breakupvalu + productcode;
			breakupvalu = breakupvalu + subprod;
			// System.out.println("breakupvalu inside P -> "+breakupvalu);
		}
		if (attchbranch.equals("Y")) {

			if (instid.equals("AZIZI")) {
				// breakupvalu = breakupvalu + branch;
				// int branchc=Integer.parseInt(branch);

				// String s = Integer.toString(branchc);

				// System.out.println("branchinside azizi"+branch);

				breakupvalu = branch.substring(1, 5) + breakupvalu;

				// System.out.println("substring value"+branch.substring(1,5));
			} else {
				breakupvalu = breakupvalu + branch;
			}
			// System.out.println("breakupvalu inside Y -> "+breakupvalu);
		}

		/*
		 * if(accttypeattach.equals("Y")) { breakupvalu = breakupvalu +
		 * accttypeval; //System.out.println("breakupvalu  inside C -> "
		 * +breakupvalu); }
		 */
		trace("breakupvalu-->" + breakupvalu);
		// System.out.println("breakupvalu -> "+breakupvalu);
		return breakupvalu;
	}

	public synchronized String gettingSequnceNumber(String instid, String bin,
			String branch_code, String baselen, String cardtype,
			JdbcTemplate jdbctemplate, String branchattch) {
		String sequnceno = "N";
		System.out.println(branchattch + "-----------branchattch--------");
		String brach_chnseq = "SELECT TRIM(\""
				+ cardtype
				+ "_CHN_BASE_NO\") AS CHN_BASE_NO FROM BRANCH_MASTER WHERE INST_ID='"
				+ instid + "' AND BRANCH_CODE='" + branch_code.trim() + "'";
		// // enctrace("3030brach_chnseq --- > "+brach_chnseq);
		String inst_chnseq = " SELECT TRIM(CHN_BASE_NO) AS CHN_BASE_NO FROM PRODUCTINFO WHERE INST_ID='"
				+ instid + "' AND PRD_CODE='" + bin + "'";
		// String card_typeseq = "SELECT TRIM(\""+cardtype+"_CHN_BASE_NO\") AS
		// CHN_BASE_NO FROM BRANCH_MASTER WHERE INST_ID='"+instid+"' AND
		// BRANCH_CODE='000'";
		// // enctrace("3030inst_chnseq --- > "+inst_chnseq);
		/*
		 * if(producttype.equalsIgnoreCase("C")){ enctrace(
		 * "3030inst_chnseq --- > "+inst_chnseq); sequnceno =
		 * (String)jdbctemplate.queryForObject(card_typeseq, String.class);
		 * trace("sequnceno---> "+sequnceno); }else{
		 */
		if (branchattch.equals("Y")) {
			sequnceno = (String) jdbctemplate.queryForObject(brach_chnseq,
					String.class);
			trace("sequnceno---> " + sequnceno);
		} else {
			sequnceno = (String) jdbctemplate.queryForObject(inst_chnseq,
					String.class);
			trace("sequnceno---> " + sequnceno);
		}

		return sequnceno;
	}

	public synchronized String gettingSequnceNumberNew(String instid,
			String bin, String branch_code, String baselen, String cardtype,
			String subprodid, JdbcTemplate jdbctemplate, String branchattch,
			String prodcardtype_attach) {

		String sequnceno = "N";
		trace("branchattch--------" + branchattch + "subprod/cardtype----"
				+ prodcardtype_attach);
		trace("bin number ----" + bin + "branch_code---" + branch_code
				+ "card type---" + cardtype + "subprod---" + subprodid);
		
	   trace("for cardtype_cardtype_chnseq+");

		/*
		 * String branch_chnseq =
		 * "SELECT TRIM(CHN_BASE_NO) AS CHN_BASE_NO FROM BASENO WHERE INST_ID='"
		 * + instid + "' AND BASENO_CODE='" + branch_code.trim() + "' AND BIN='"
		 * + bin.trim() + "'";
		 * 
		 * String cardtype_chnseq =
		 * "SELECT TRIM(CHN_BASE_NO) AS CHN_BASE_NO FROM BASENO WHERE INST_ID='"
		 * + instid + "' AND BASENO_CODE='" + cardtype.trim() + "' AND BIN='" +
		 * bin.trim() + "'";
		 * 
		 * String subprodid_chnseq =
		 * "SELECT TRIM(CHN_BASE_NO) AS CHN_BASE_NO FROM BASENO WHERE INST_ID='"
		 * + instid + "' AND BASENO_CODE='" + subprodid.trim() + "' AND BIN='" +
		 * bin.trim() + "'";
		 * 
		 * String inst_chnseq =
		 * " SELECT TRIM(CHN_BASE_NO) AS CHN_BASE_NO FROM PRODUCTINFO WHERE INST_ID='"
		 * + instid + "' AND BIN='" + bin + "'";
		 * 
		 * // String accttype_chnseq = "SELECT TRIM(CHN_BASE_NO) AS CHN_BASE_NO
		 * // FROM BASENO WHERE INST_ID='"+instid+"' AND //
		 * BASENO_CODE='"+accttypeval.trim()+"' AND BIN='"+bin.trim()+"'";
		 * trace("sardarchecing " + prodcardtype_attach); if
		 * (branchattch.equals("Y")) {
		 * 
		 * trace("branchattch " + branchattch); trace(
		 * "sequence number based on branch qry--- " + branch_chnseq); sequnceno
		 * = (String) jdbctemplate.queryForObject(branch_chnseq, String.class);
		 * //trace("sequnceno---> " + sequnceno); } else if
		 * (prodcardtype_attach.equals("C")) {
		 * 
		 * trace("prodcardtype_attach " + prodcardtype_attach); trace(
		 * "sequence number based on cardtype qry--- " + cardtype_chnseq);
		 * sequnceno = (String) jdbctemplate.queryForObject(cardtype_chnseq,
		 * String.class); //trace("sequnceno---> " + sequnceno); } else if
		 * (prodcardtype_attach.equals("P")) { trace("prodcardtype_attach " +
		 * prodcardtype_attach); trace(
		 * "sequence number based on subprod qry--- " + subprodid_chnseq);
		 * sequnceno = (String) jdbctemplate.queryForObject(subprodid_chnseq,
		 * String.class); //trace("sequnceno---> " + sequnceno); } else if
		 * (accttypeattach.equals("Y")) { trace(
		 * "sequence number based on accttype qry--- "+accttype_chnseq);
		 * sequnceno = (String)jdbctemplate.queryForObject(accttype_chnseq,
		 * String.class); trace("sequnceno---> "+sequnceno); } else { trace(
		 * "sequence number based on bin qry--- " + inst_chnseq); sequnceno =
		 * (String) jdbctemplate.queryForObject(inst_chnseq, String.class);
		 * //trace("sequnceno---> " + sequnceno); }
		 */

		// by gowtham-220819
		String branch_chnseq = "SELECT TRIM(CHN_BASE_NO) AS CHN_BASE_NO FROM BASENO WHERE INST_ID=? AND BASENO_CODE=? AND BIN=?";

		String cardtype_chnseq = "SELECT TRIM(CHN_BASE_NO) AS CHN_BASE_NO FROM BASENO WHERE INST_ID=? AND BASENO_CODE=? AND BIN=?";

		String subprodid_chnseq = "SELECT TRIM(CHN_BASE_NO) AS CHN_BASE_NO FROM BASENO WHERE INST_ID=? AND BASENO_CODE=? AND BIN=?";

		String inst_chnseq = " SELECT TRIM(CHN_BASE_NO) AS CHN_BASE_NO FROM PRODUCTINFO WHERE INST_ID=? AND PRD_CODE=?";

		trace("sardarchecing " + prodcardtype_attach);

		if (branchattch.equals("Y")) {

			trace("branchattch " + branchattch);
			trace("sequence number based on branch qry--- " + branch_chnseq);
			sequnceno = (String) jdbctemplate.queryForObject(branch_chnseq,
					new Object[] { instid, branch_code.trim(), bin.trim() },
					String.class);

		} else if (prodcardtype_attach.equals("C")) {

			trace("prodcardtype_attach " + prodcardtype_attach
					+ " cardtype :   " + cardtype + " bin number ::: " + bin);
			trace("sequence number based on cardtype qry--- " + cardtype_chnseq);
			sequnceno = (String) jdbctemplate.queryForObject(cardtype_chnseq,
					new Object[] { instid, cardtype.trim(), bin.trim() },
					String.class);

		} else if (prodcardtype_attach.equals("P")) {
			trace("prodcardtype_attach " + prodcardtype_attach);
			trace("sequence number based on subprod qry--- " + subprodid_chnseq);
			sequnceno = (String) jdbctemplate.queryForObject(subprodid_chnseq,
					new Object[] { instid, subprodid.trim(), bin.trim() },
					String.class);

		} else {
			trace("sequence number based on bin qry--- " + inst_chnseq);
			sequnceno = (String) jdbctemplate.queryForObject(inst_chnseq,
					new Object[] { instid, bin }, String.class);

		}

		return sequnceno;
	}

	public Personalizeorderdetails getCcyexpiry(String instid, String binn,
			String cardtype, String subproduct, String prodcode,
			JdbcTemplate jdbctemplate) {
		Personalizeorderdetails vlaue = null;
		List misvalue;
		String glcode = "0", expiry_period = "0", service_code = "0", fee_code = "0", card_ccy = "0", limit_id = "0";

		/*
		 * String expccy_qury =
		 * "SELECT GL_SCHEME_CODE ,EXPIRY_PERIOD,SERVICE_CODE,FEE_CODE,CARD_CCY,LIMIT_ID FROM INSTPROD_DETAILS "
		 * + "WHERE INST_ID='" + instid + "' AND SUB_PROD_ID='" + subproduct +
		 * "' AND PRODUCT_CODE='" + prodcode+ "'"; trace(
		 * "TTHe Extra Details Query====> " + expccy_qury); misvalue =
		 * jdbctemplate.queryForList(expccy_qury);
		 */

		// by gowtham-220819
		String expccy_qury = "SELECT GL_SCHEME_CODE ,EXPIRY_PERIOD,SERVICE_CODE,FEE_CODE,CARD_CCY,LIMIT_ID FROM INSTPROD_DETAILS "
				+ "WHERE INST_ID=? AND SUB_PROD_ID=? AND PRODUCT_CODE=?";
		trace("TTHe Extra Details Query====> " + expccy_qury);
		misvalue = jdbctemplate.queryForList(expccy_qury, new Object[] {
				instid, subproduct, prodcode });

		Iterator itr = misvalue.iterator();
		while (itr.hasNext()) {
			Map map = (Map) itr.next();
			glcode = (String) map.get("GL_SCHEME_CODE");
			expiry_period = (String) map.get("EXPIRY_PERIOD");
			service_code = (String) map.get("SERVICE_CODE");
			fee_code = (String) map.get("FEE_CODE");
			card_ccy = (String) map.get("CARD_CCY");
			limit_id = (String) map.get("LIMIT_ID");

		}
		vlaue = new Personalizeorderdetails(glcode, expiry_period,
				service_code, fee_code, card_ccy, limit_id, expccy_qury, "", "");
		return vlaue;

	}

	/*
	 * public String generatedCheckDigit(String cardno) { int checkdigit =
	 * generateCheckDigit(cardno); String chkdigit =
	 * Integer.toString(checkdigit); return chkdigit; }
	 * 
	 * public int generateCheckDigit(String s) { int digit = 10 -
	 * getCheckDigit(s, true) % 10; if (digit == 10) { digit = 0; } return
	 * digit; }
	 * 
	 * private int getCheckDigit(String s, boolean eventposition) { int sum = 0;
	 * for (int i = s.length() - 1; i >= 0; i--) { int n =
	 * Integer.parseInt(s.substring(i, i + 1)); if (eventposition) { n *= 2; if
	 * (n > 9) { n = (n % 10) + 1; } } sum += n; eventposition = !eventposition;
	 * } return sum; }
	 */

	public String generatedCheckDigit(StringBuilder cardno) {
		int checkdigit = generateCheckDigit(cardno);
		String chkdigit = Integer.toString(checkdigit);
		return chkdigit;
	}

	public int generateCheckDigit(StringBuilder s) {
		int digit = 10 - getCheckDigit(s, true) % 10;
		if (digit == 10) {
			digit = 0;
		}
		return digit;
	}

	private int getCheckDigit(StringBuilder s, boolean eventposition) {
		int sum = 0;
		for (int i = s.length() - 1; i >= 0; i--) {
			int n = Integer.parseInt(s.substring(i, i + 1));
			if (eventposition) {
				n *= 2;
				if (n > 9) {
					n = (n % 10) + 1;
				}
			}
			sum += n;
			eventposition = !eventposition;
		}
		
		trace("sum-->"+sum);
		return sum;
	}

	public String checkBranchattached(String instid, JdbcTemplate jdbcTemplate) {

		/*
		 * String attchbranch_qury =
		 * "SELECT TRIM(BRANCHATTCHED) AS BRANCHATTCHED FROM INSTITUTION WHERE INST_ID='"
		 * + instid + "'"; return ((String)
		 * jdbcTemplate.queryForObject(attchbranch_qury, String.class));
		 */

		// / by gowtham-220819
		String attchbranch_qury = "SELECT TRIM(BRANCHATTCHED) AS BRANCHATTCHED FROM INSTITUTION WHERE INST_ID=? ";
		return ((String) jdbcTemplate.queryForObject(attchbranch_qury,
				new Object[] { instid }, String.class));

	}

	public List checkattachedtype(String instid, String bin,
			JdbcTemplate jdbcTemplate) {
		List getattchtype = null;

		/*
		 * String getattachedtype =
		 * "SELECT ATTACH_PRODTYPE_CARDTYPE,ATTACH_BRCODE,ATTACH_ACCTTYPE FROM PRODUCTINFO"
		 * + " WHERE INST_ID='"+ instid + "' and BIN='" + bin + "'";
		 * enctrace("getattachedtype-->" + getattachedtype); getattchtype =
		 * jdbcTemplate.queryForList(getattachedtype);
		 */

		// BY GOWTHAM-220819
		String getattachedtype = "SELECT ATTACH_PRODTYPE_CARDTYPE,ATTACH_BRCODE,ATTACH_ACCTTYPE FROM PRODUCTINFO"
				+ " WHERE INST_ID=? and BIN=?";
		enctrace("getattachedtype-->" + getattachedtype);
		getattchtype = jdbcTemplate.queryForList(getattachedtype, new Object[] {
				instid, bin });

		return getattchtype;
	}

	public String checkPadssEnable(String instid, JdbcTemplate jdbcTemplate) {
		String checkPadssEnable = "SELECT PADSS_ENABLE FROM INSTITUTION WHERE INST_ID=?";
		enctrace("3030checkPadssEnable::" + checkPadssEnable);
		return ((String) jdbcTemplate.queryForObject(checkPadssEnable,
				new Object[] { instid }, String.class));
	}

	public String getSecurityKeyid(String instid, JdbcTemplate jdbcTemplate) {

		/*
		 * String attchbranch_qury =
		 * "SELECT PADSS_KEY FROM INSTITUTION WHERE INST_ID='" + instid + "'";
		 * enctrace("getSecurityKeyid" + attchbranch_qury); return ((String)
		 * jdbcTemplate.queryForObject(attchbranch_qury, String.class));
		 */

		System.out.println("instid =====>  " + instid);
		// by gowtham-200819
		String attchbranch_qury = "SELECT PADSS_KEY FROM INSTITUTION WHERE INST_ID=?";
		enctrace("getSecurityKeyid" + attchbranch_qury);
		return ((String) jdbcTemplate.queryForObject(attchbranch_qury,
				new Object[] { instid }, String.class));

	}

	public String getstatusfromezcardinfo(String instid, String cardno,
			JdbcTemplate jdbcTemplate) {
		String status = "SELECT STATUS FROM EZCARDINFO WHERE INSTID='"
				+ instid
				+ "' and chn in (select hcard_no from CARD_PRODUCTION_HASH where HCARD_NO='"
				+ cardno + "')";
		enctrace("updateCustStaus" + status);
		return ((String) jdbcTemplate.queryForObject(status, String.class));
	}

	// old code
	/*
	 * public String generateCHN(String genchn, String sequnce, int baselen, int
	 * chnlen) throws Exception { trace("generateCHN method started .........");
	 * PadssSecurity sec = new PadssSecurity(); String cardno = "N"; trace(
	 * " genchn " + genchn + "---  sequnce, " + sequnce + " , " + baselen +
	 * " baselen " + "chnlen " + chnlen); String newsequnce =
	 * orderreferenceno(sequnce, baselen); if (newsequnce.equals("X")) { //
	 * System.out.println(" Card Sequnce Number Reached Maximum"); return "M"; }
	 * trace("New Sequnce Number is ====>" + newsequnce + "Chn Len got is " +
	 * chnlen); String newcardno = genchn + newsequnce; //trace(
	 * "New Card Number is ====>" + newcardno); int nechnlen =
	 * newcardno.length(); trace("Length Of The CHN is ===>" + nechnlen); if
	 * (nechnlen == 15 || nechnlen == 18) { String check_digit =
	 * generatedCheckDigit(newcardno); trace("Check Digit is ===>" +
	 * check_digit); cardno = newcardno + check_digit; trace(
	 * "Card Number Generated is ===>" + sec.getMakedCardno(cardno)); } else {
	 * cardno = "N"; trace(
	 * "Card Lenght is Not matched ...Generated card number lenghth without check digit: "
	 * + nechnlen); }
	 * 
	 * int cardlen = cardno.length(); trace(
	 * "configured chn len=============================>"+chnlen); trace(
	 * "generated cardlen=============================>"+cardlen);
	 * 
	 * if(cardlen-(genchn.length()) != chnlen) { cardno = "N"; trace(
	 * "Card Lenght is Not matched "); }
	 * 
	 * trace(" ......... generateCHN method ended...."); return cardno;
	 * 
	 * }
	 */

	/**
	 * 
	 * @param genchn
	 * @param sequnce
	 * @param baselen
	 * @param chnlen
	 * @return
	 * @throws Exception
	 */

	public String generateCHN(String genchn, String sequnce, int baselen,
			int chnlen) throws Exception {
		trace("generateCHN method started ........."+genchn+"=="+sequnce+"=="+baselen);
		// PadssSecurity sec = new PadssSecurity();

		String cardno = "N";
		// String newcardno="";
		// StringBuilder cardno = new StringBuilder();
		String newsequnce = null;
		StringBuilder newcardno = new StringBuilder();

		try {

			newsequnce = orderreferenceno(sequnce, baselen);
			if (newsequnce.equals("X")) {
				// System.out.println(" Card Sequnce Number Reached Maximum");
				return cardno = "M";
			}
			// trace("New Sequnce Number is ====>" + newsequnce + "Chn Len got
			// is " + chnlen);
			// newcardno = genchn + newsequnce;
			newcardno.append(genchn + newsequnce);

			int nechnlen = newcardno.length();
			trace("Length Of The CHN is ===>" + nechnlen);
			if (nechnlen == 15 || nechnlen == 18) {
				String check_digit = generatedCheckDigit(newcardno);
				cardno = (newcardno + check_digit);

			} else {

				// cardno.append("N");
				cardno = "N";
				// cardno.append("N");
				trace("Card Lenght is Not matched ...Generated card number lenghth without check digit: "
						+ nechnlen);
			}

		} catch (Exception exce) {
			exce.printStackTrace();
		} finally {
			// nullify
			newcardno = null;
		}
		// trace("newcardno"+newcardno);
		// trace("cardno"+cardno);
		trace(" ......... generateCHN method ended....");
		return cardno;
	}

	public List personaliseCardauthlist(String instid, String cardstatus,
			String mkckstatus, String condition, JdbcTemplate jdbctemplate)
			throws Exception {

		/*
		 * String authcards_query =
		 * "select distinct(cp.order_ref_no) as ORDER_REF_NO,to_char(cp."
		 * +dateflag+
		 * ",'DD-MON-YYYY') as GENDATE,ud.USERNAME as USERNAME,co.card_quantity as COUNT,co.card_type_id as CARDID,co.sub_prod_id as SUBID,"
		 * +
		 * "co.product_code as PCODE,co.BRANCH_CODE as BRANCH_CODE,co.BIN as BIN,pd.product_name as PNAME from PERS_CARD_PROCESS cp,PERS_CARD_ORDER co,INSTPROD_DETAILS pd, USER_DETAILS ud "
		 * + "where cp.inst_id='"+instid+"' and cp.card_status ='"+cardstatus+
		 * "' and cp.mkck_status='"+mkckstatus+"' and cp.branch_code='"+brcode+
		 * "' and cp.bin='"+bin+
		 * "' and ud.userid = cp.maker_id and cp.order_ref_no = co.order_ref_no and "
		 * +
		 * "cp.inst_id = co.inst_id and  co.inst_id = pd.inst_id and cp.product_code = co.product_code and co.product_code = pd.product_code and "
		 * + "(to_date('"+fromdate+"','dd-mm-yyyy') <= cp."+dateflag+
		 * ") AND (to_date('"+todate+"','dd-mm-yyyy')+1 >= cp."+dateflag+
		 * ") order  by cp.order_ref_no";
		 * 
		 * 
		 * String authcards_query =
		 * "select ORDER_REF_NO,count(*) as COUNT,CART_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,MAKER_ID,to_char("
		 * +dateflag+",'DD-MON-YYYY') as  GENDATE," +
		 * "BIN from PERS_CARD_PROCESS where inst_id='"+instid+
		 * "' and CARD_STATUS='"+cardstatus+"' and MKCK_STATUS='"+mkckstatus+
		 * "' and (to_date('"+fromdate+"','dd-mm-yyyy') <= "+dateflag+
		 * ") AND (to_date('"+todate+"','dd-mm-yyyy')+1 >= "+dateflag+
		 * ") and bin='"+bin+"' " +
		 * "group by order_ref_no,CART_TYPE_ID, sub_prod_id, product_code,MAKER_ID,to_char("
		 * +dateflag+",'DD-MON-YYYY'),bin order by order_ref_no";
		 */

		/*
		 * String pingen = (String)session.getAttribute("PINGEN_TYPE"); trace(
		 * " PinGen Type-----> "+pingen);
		 */

		// String field_1="CARD_NO",orderflag="CARD_NO";

		String select_query = "select distinct(order_ref_no) as ORDER_REF_NO, ORG_CHN,CIN, MCARD_NO,ACCT_NO, bin,card_type_id as CARDTYPE,sub_prod_id as SUBPRODID,product_code,EMB_NAME,to_char(generated_date,'DD-MON-YY') as GENDATE,MAKER_ID,branch_code,MOBILENO,"
				+ " (SELECT USERNAME  FROM USER_DETAILS WHERE  USERID=A.CHECKER_ID) AS CHECKER,(SELECT BRANCH_NAME FROM BRANCH_MASTER WHERE BRANCH_CODE=CARD_COLLECT_BRANCH) AS CARD_COLLECT_BRANCH,"
				+ " (SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID=A.INST_ID and PRODUCT_CODE=A.PRODUCT_CODE) PNAME,"
				+ " (SELECT USERNAME  FROM USER_DETAILS WHERE INSTID=A.INST_ID and USERID=A.MAKER_ID OR USERNAME=A.MAKER_ID and rownum <= 1) USERNAME, "
				+ " (SELECT SUB_PRODUCT_NAME FROM INSTPROD_DETAILS WHERE INST_ID=A.INST_ID and SUB_PROD_ID=A.SUB_PROD_ID) SUBPRODDESC"
				+ "  from PERS_CARD_PROCESS A"
				+ " where inst_id='"
				+ instid
				+ "' and trim(CARD_STATUS)='"
				+ cardstatus
				+ "' and mkck_status='"
				+ mkckstatus
				+ "' "
				+ condition
				+ " order by order_ref_no";
		// AND CAF_REC_STATUS in ('S','A') This was added for maintenance
		// activity
		// // enctrace("3030authcards_query : "+select_query);
		enctrace("checking select query for authlist" + select_query);
		List persorderlist = jdbctemplate.queryForList(select_query);

		/*
		 * trace("The Query Result is ==========>"+persorderlist); ListIterator
		 * itr = persorderlist.listIterator(); while( itr.hasNext() ){ Map temp
		 * = (Map)itr.next(); String refnum = (String)temp.get("ORDER_REF_NO");
		 * String prodcode = (String)temp.get("PRODUCT_CODE"); String usercode =
		 * (String)temp.get("MAKER_ID"); String binno = (String)temp.get("BIN");
		 * String embossname= (String)temp.get("EMB_NAME"); //String cin =
		 * (String)temp.get("CIN"); //String productdesc =
		 * getProductdesc(instid, binno,prodcode); String productdesc =
		 * getProductdesc(instid,prodcode, jdbctemplate); trace(
		 * "INstituoion ID "+instid+"User Code ===> "+usercode); String username
		 * = getUserName(instid, usercode, jdbctemplate); String count =
		 * getCardcount(instid,refnum,cardstatus,mkckstatus,"REFNUM",
		 * jdbctemplate); List
		 * embossname=getEmbossingName(instid,(String)temp.get("CIN"),"CIN",
		 * jdbctemplate); ListIterator lit = embossname.listIterator();
		 * while(lit.hasNext()){ Map hm = (Map) lit.next(); String embossname2 =
		 * (String)hm.get("EMB_NAME"); trace("EBOSSNAME========== "
		 * +embossname2); hm.put("EMBOSSING_NAME", embossname2); itr.add(hm); }
		 * trace("111111111111111111"); temp.put("SUBPRODDESC",
		 * getSubProductdesc(instid, (String)temp.get("SUBPRODID"),
		 * jdbctemplate)) ; trace("22222222222222222222"); temp.put("CARDNO",
		 * (String)temp.get("CARD_NO")); trace("3333333333333333");
		 * temp.put("COUNT", count); trace("44444444444444444");
		 * temp.put("EMBOSSING_NAME", embossname); temp.put("PNAME",
		 * productdesc); trace("555555555555555555"); temp.put("USERNAME",
		 * username); trace("66666666666666666"); itr.remove(); itr.add(temp);
		 * 
		 * }
		 */

		return persorderlist;
	}

	public List personaliseCardauthlistpin(String instid, String cardstatus,
			String mkckstatus, String condition, JdbcTemplate jdbctemplate)
			throws Exception {

		/*
		 * String select_query =
		 * "select distinct(order_ref_no) as ORDER_REF_NO, CIN, ORG_CHN,MCARD_NO,ACCT_NO, bin,card_type_id as CARDTYPE,sub_prod_id as SUBPRODID,product_code,EMB_NAME,to_char(generated_date,'DD-MON-YY') as GENDATE,MAKER_ID,branch_code,MOBILENO,"
		 * +
		 * " (SELECT USERNAME  FROM USER_DETAILS WHERE  USERID=A.CHECKER_ID) AS CHECKER,(SELECT BRANCH_NAME FROM BRANCH_MASTER WHERE BRANCH_CODE=CARD_COLLECT_BRANCH) AS CARD_COLLECT_BRANCH,"
		 * +
		 * " (SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID=A.INST_ID and PRODUCT_CODE=A.PRODUCT_CODE) PNAME,"
		 * +
		 * " (SELECT USERNAME  FROM USER_DETAILS WHERE INSTID=A.INST_ID and USERID=A.MAKER_ID OR USERNAME=A.MAKER_ID and rownum <= 1) USERNAME, "
		 * +
		 * " (SELECT SUB_PRODUCT_NAME FROM INSTPROD_DETAILS WHERE INST_ID=A.INST_ID and SUB_PROD_ID=A.SUB_PROD_ID) SUBPRODDESC"
		 * + "  from PERS_CARD_PROCESS A" +
		 * " where CAF_REC_STATUS NOT IN('R') AND inst_id='" + instid +
		 * "' and trim(CARD_STATUS)='" + cardstatus + "' and mkck_status='" +
		 * mkckstatus + "'" + condition + " order by order_ref_no";
		 */// AND CAF_REC_STATUS in ('S','A') This was added for maintenance
		String select_query = "select distinct(order_ref_no) as ORDER_REF_NO, CIN, ORG_CHN,MCARD_NO,ACCT_NO, bin,card_type_id as CARDTYPE,sub_prod_id as SUBPRODID,product_code,EMB_NAME,to_char(generated_date,'DD-MON-YY') as GENDATE,MAKER_ID,branch_code,MOBILENO,"
				+ " (SELECT USERNAME  FROM USER_DETAILS WHERE  USERID=A.CHECKER_ID) AS CHECKER,(SELECT BRANCH_NAME FROM BRANCH_MASTER WHERE BRANCH_CODE=CARD_COLLECT_BRANCH) AS CARD_COLLECT_BRANCH,"
				+ " (SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID=A.INST_ID and PRODUCT_CODE=A.PRODUCT_CODE) PNAME,"
				+ " (SELECT USERNAME  FROM USER_DETAILS WHERE INSTID=A.INST_ID and USERID=A.MAKER_ID OR USERNAME=A.MAKER_ID and rownum <= 1) USERNAME, "
				+ " (SELECT SUB_PRODUCT_NAME FROM INSTPROD_DETAILS WHERE INST_ID=A.INST_ID and SUB_PROD_ID=A.SUB_PROD_ID) SUBPRODDESC"
				+ "  from PERS_CARD_PROCESS A"
				+ " where  inst_id='"
				+ instid
				+ "' and trim(CARD_STATUS)='"
				+ cardstatus
				+ "' and mkck_status='"
				+ mkckstatus
				+ "'"
				+ condition
				+ " order by order_ref_no";
		// activity
		// // enctrace("3030authcards_query : "+select_query);
		enctrace("checking select query for pinorder" + select_query);
		List persorderlist = jdbctemplate.queryForList(select_query);

		return persorderlist;
	}

	public String getCardcount(String instid, String refnum, String cardstatus,
			String mkckstatus, String flag, JdbcTemplate jdbcTemplate) {
		// String cafstatus = " AND CAF_REC_STATUS in('S','A','D')";
		String condtion = "ORDER_REF_NO";
		if (flag.equals("CARD")) {
			condtion = "CARD_NO";
			// cafstatus ="";
		}

		String count = "";

		String countqury = "select count(*) from INST_CARD_PROCESS where INST_ID='"
				+ instid
				+ "' and "
				+ condtion
				+ "='"
				+ refnum
				+ "' and CARD_STATUS='"
				+ cardstatus
				+ "' and MKCK_STATUS='"
				+ mkckstatus + "'";// +cafstatus;
		trace("=countqury====> " + countqury);
		int cardcount = jdbcTemplate.queryForInt(countqury);
		count = Integer.toString(cardcount);
		return count;
	}

	public List getEmbossingName(String instid, String refnum, String flag,
			JdbcTemplate jdbctemplate) {
		String condition = "ORDER_REF_NO";
		if (flag.equals("CARD")) {
			condition = "CARD_NO";
		} else if (flag.equals("CIN")) {
			condition = "CIN";
		}
		List embname = null;
		String embqury = "select distinct(emb_name)  from PERS_CARD_PROCESS where inst_id='"
				+ instid + "' and " + condition + "='" + refnum + "'";
		trace("embqury====> " + embqury);

		embname = (List) jdbctemplate.queryForList(embqury);
		return embname;
	}

	public HSMParameter gettingBin_details(String binn, String inst_name,
			JdbcTemplate jdbctemplate) {
		trace("===================Geeting call bindetails Starts ============================");

		HSMParameter hsmparams = null;

		try {

			List bindetails_List = null;
			String HSMNAME = "0", HSMPROTOCOL = "0", HSMTYPE = "0", HSMADDRESS = "0", HEADERTYPE = "0", HSM_ID = "0", CHNLEN = "0", PVK = "0";
			String PIN_LENGTH = "0", PAN_OFFSET = "0", PINOFFSET_LENGTH = "0", PIN_PAD_CHAR = "0", DECIMILISATION_TABLE = "0";
			String MSG_HEADER = "0", MAIL_LENGTH = "0", MAIL_HEIGHT = "0", CVV_REQUIRED = "0", CVV_LENGTH = "0", CVVK1 = "0", CVVK2 = "0";
			String PVK1 = "0", PVK2 = "0", GEN_METHOD = "0", PVKI = "0", PANVALIDATION_LENGTH = "0", PVK3 = "0", PVK4 = "0", PPK = "0", CVK4 = "0";
			String CVK3 = "0", PROTOCOL_TYPE = "0", STOP_BITS = "0", PARITY_VALUE = "0", DATA_BITS = "0", BAUD_RATE = "0", PORT_TYPE = "0";
			String EPVK = "0", PANPADCHAR = "0", DESLENGTH = "0", PINMAILER_ID = "0", SERVICE_CODE = "0", PINMAILER_DOC_TYPE = "C";
			int HSMPORT = 0, HEADERLEN = 0, HSMHEADERLEN = 0, HSMTIMEOUT = 0, CONNECTIONINTERVAL = 0, HSMSTATUS = 0;

			String binDetails_Query = "select hd.HSMNAME as HSMNAME,hd.HSMPROTOCOL as HSMPROTOCOL,hd.HSMTYPE as HSMTYPE,hd.HSMADDRESS as HSMADDRESS,"
					+ "hd.HSMPORT as HSMPORT,hd.HEADERLEN as HEADERLEN,hd.HEADERTYPE as HEADERTYPE,hd.HSMHEADERLEN as HSMHEADERLEN,"
					+ "hd.HSMTIMEOUT as HSMTIMEOUT,hd.CONNECTIONINTERVAL as CONNECTIONINTERVAL,hd.HSMSTATUS as HSMSTATUS,hd.HSM_ID as HSM_ID,"
					+ "br.CHNLEN as CHNLEN,br.PVK as PVK,br.PPK as PPK,br.PIN_LENGTH as PIN_LENGTH,br.PAN_OFFSET as PAN_OFFSET,br.PINOFFSET_LENGTH as PINOFFSET_LENGTH,"
					+ "br.DECIMILISATION_TABLE as DECIMILISATION_TABLE,br.CVV_REQUIRED as CVV_REQUIRED,br.CVV_LENGTH as CVV_LENGTH,br.CVK1 as CVVK1,br.CVK2 as CVVK2,br.PVK1 as PVK1,"
					+ "br.PVK2 as PVK2,br.GEN_METHOD as GEN_METHOD,br.PVKI as PVKI,br.PANVALIDATION_LENGTH as PANVALIDATION_LENGTH,"
					+ "br.EPVK as EPVK,br.PANPADCHAR as PANPADCHAR,br.DESLENGTH as DESLENGTH,"
					+ "br.PINMAILER_ID as PINMAILER_ID"
					+ " from HSM_DETAILS hd,PRODUCTINFO br where  br.INST_ID='"
					+ inst_name
					+ "' and br.BIN='"
					+ binn
					+ "' and hd.HSM_ID=br.HSM_ID ";

			trace(" Bin Details Query is =====> " + binDetails_Query);
			bindetails_List = jdbctemplate.queryForList(binDetails_Query);
			if (!(bindetails_List.isEmpty())) {
				Iterator bin_itr = bindetails_List.iterator();
				while (bin_itr.hasNext()) {
					Map map = (Map) bin_itr.next();
					HSMNAME = ((String) map.get("HSMNAME"));
					HSMPROTOCOL = ((String) map.get("HSMPROTOCOL"));
					HSMTYPE = ((String) map.get("HSMTYPE"));
					HSMADDRESS = ((String) map.get("HSMADDRESS"));
					HSMPORT = ((BigDecimal) map.get("HSMPORT")).intValue();
					HEADERLEN = ((BigDecimal) map.get("HEADERLEN")).intValue();
					HEADERTYPE = ((String) map.get("HEADERTYPE"));
					HSMHEADERLEN = ((BigDecimal) map.get("HSMHEADERLEN"))
							.intValue();
					HSMTIMEOUT = ((BigDecimal) map.get("HSMTIMEOUT"))
							.intValue();
					CONNECTIONINTERVAL = ((BigDecimal) map
							.get("CONNECTIONINTERVAL")).intValue();
					HSMSTATUS = ((BigDecimal) map.get("HSMSTATUS")).intValue();
					HSM_ID = ((String) map.get("HSM_ID"));
					CHNLEN = ((String) map.get("CHNLEN"));
					PVK = ((String) map.get("PVK"));
					PIN_LENGTH = ((String) map.get("PIN_LENGTH"));
					PAN_OFFSET = ((String) map.get("PAN_OFFSET"));
					PINOFFSET_LENGTH = ((String) map.get("PINOFFSET_LENGTH"));
					PIN_PAD_CHAR = ((String) map.get("PIN_PAD_CHAR"));
					DECIMILISATION_TABLE = ((String) map
							.get("DECIMILISATION_TABLE"));
					MSG_HEADER = ((String) map.get("MSG_HEADER"));
					MAIL_LENGTH = ((String) map.get("MAIL_LENGTH"));
					MAIL_HEIGHT = ((String) map.get("MAIL_HEIGHT"));
					CVV_REQUIRED = ((String) map.get("CVV_REQUIRED"));
					CVV_LENGTH = ((String) map.get("CVV_LENGTH"));
					CVVK1 = ((String) map.get("CVVK1"));
					CVVK2 = ((String) map.get("CVVK2"));
					PVK1 = ((String) map.get("PVK1"));
					PVK2 = ((String) map.get("PVK2"));
					GEN_METHOD = ((String) map.get("GEN_METHOD"));
					PVKI = ((String) map.get("PVKI"));
					PANVALIDATION_LENGTH = ((String) map
							.get("PANVALIDATION_LENGTH"));
					PVK3 = ((String) map.get("PVK3"));
					PVK4 = ((String) map.get("PVK4"));
					CVK4 = ((String) map.get("CVK4"));
					CVK3 = ((String) map.get("CVK3"));
					PROTOCOL_TYPE = ((String) map.get("PROTOCOL_TYPE"));
					STOP_BITS = ((String) map.get("STOP_BITS"));
					PARITY_VALUE = ((String) map.get("PARITY_VALUE"));
					DATA_BITS = ((String) map.get("DATA_BITS"));
					BAUD_RATE = ((String) map.get("BAUD_RATE"));
					PORT_TYPE = ((String) map.get("PORT_TYPE"));
					EPVK = ((String) map.get("EPVK"));
					PANPADCHAR = ((String) map.get("PANPADCHAR"));
					DESLENGTH = ((String) map.get("DESLENGTH"));
					PINMAILER_ID = ((String) map.get("PINMAILER_ID"));
					SERVICE_CODE = ((String) map.get("SERVICE_CODE"));

				}
				hsmparams = new HSMParameter(HSMNAME, HSMPROTOCOL, HSMTYPE,
						HSMADDRESS, HSMPORT, HEADERLEN, HEADERTYPE,
						HSMHEADERLEN, HSMTIMEOUT, CONNECTIONINTERVAL,
						HSMSTATUS, HSM_ID, CHNLEN, PVK, PPK, PIN_LENGTH,
						PAN_OFFSET, PINOFFSET_LENGTH, PIN_PAD_CHAR,
						DECIMILISATION_TABLE, MSG_HEADER, MAIL_LENGTH,
						MAIL_HEIGHT, CVV_REQUIRED, CVV_LENGTH, CVVK1, CVVK2,
						PVK1, PVK2, GEN_METHOD, PVKI, PANVALIDATION_LENGTH,
						PVK3, PVK4, CVK4, CVK3, PROTOCOL_TYPE, STOP_BITS,
						PARITY_VALUE, DATA_BITS, BAUD_RATE, PORT_TYPE, EPVK,
						PANPADCHAR, DESLENGTH, PINMAILER_ID, SERVICE_CODE,
						PINMAILER_DOC_TYPE);
				// hsmParam = hsmparams;
				// hsmParam.HSMParameter(HSMNAME, HSMPROTOCOL, HSMTYPE,
				// HSMADDRESS, HSMPORT, HEADERLEN, HEADERTYPE, HSMHEADERLEN,
				// HSMTIMEOUT, CONNECTIONINTERVAL, HSMSTATUS, HSM_ID, CHNLEN,
				// PVK, PIN_LENGTH, PAN_OFFSET, PINOFFSET_LENGTH, PIN_PAD_CHAR,
				// DECIMILISATION_TABLE, MSG_HEADER, MAIL_LENGTH, MAIL_HEIGHT,
				// CVV_REQUIRED, CVV_LENGTH, CVVK1, CVVK2, PVK1, PVK2,
				// GEN_METHOD, PVKI, PANVALIDATION_LENGTH, PVK3, PVK4, CVK4,
				// CVK3, PROTOCOL_TYPE, STOP_BITS, PARITY_VALUE, DATA_BITS,
				// BAUD_RATE, PORT_TYPE, EPVK, PANPADCHAR, DESLENGTH,
				// PINMAILER_ID,SERVICE_CODE);
				// bin_details ="Y";
				hsmparams.setError_hsmparameter("Y");
			}
		} catch (Exception e) {
			// bin_details ="E";
			trace("Excetion in BIN DETAILS ======> " + e);
			hsmparams.setError_hsmparameter("E");

		}
		trace("===================Geeting call bindetails ENDS ============================");
		return hsmparams;
	}

	public List getCardnumbers(String table, String instid, String orderrefnum,
			JdbcTemplate jdbctemplate) {
		List cards = null;

		String cards_qury = "SELECT TRIM(CARD_NO) AS CARDNO FROM " + table
				+ " WHERE INST_ID='" + instid + "' AND ORDER_REF_NO='"
				+ orderrefnum + "'";
		cards = jdbctemplate.queryForList(cards_qury);
		return cards;
	}

	/**
	 * 
	 * @param table
	 * @param instid
	 * @param ecardno
	 * @param padssenable
	 * @param keyid
	 * @param secList
	 * @param jdbctemplate
	 * @return
	 * @throws Exception
	 */

	public List getCarddetails(String table, String instid, String ecardno,
			String padssenable, String keyid, List secList,
			JdbcTemplate jdbctemplate) throws Exception {

		CardCvvDecryption dec = new CardCvvDecryption();
		StringBuilder carddetail_qury = new StringBuilder();
		List carddetail = null;
		String EDPK = "";
		String dcardno = "";
		StringBuilder sb = null;

		TrackEncryption encTrack = new TrackEncryption();
		PadssSecurity padsssec = null;
		Properties props = getCommonDescProperty();
		EDPK = props.getProperty("EDPK");

		String cvv1 = null;
		String cvv2 = null;
		String icvv = null;

		String encCvv1 = null;
		String encCvv2 = null;
		String enIcvv = null;

		/*
		 * List encCvvValues= dec.getCVVValues(ecardno, jdbctemplate);
		 * 
		 * trace("list values =======  "+encCvvValues);
		 * 
		 * if (!(encCvvValues.isEmpty())) { Iterator crdItr =
		 * encCvvValues.iterator();
		 * 
		 * while (crdItr.hasNext()) { Map crdmap = (Map) crdItr.next(); encCvv1
		 * = ((String) crdmap.get("CVV1"));
		 * 
		 * cvv1=encTrack.decrypt(encCvv1); trace("cvv1 === " + cvv1);
		 * 
		 * 
		 * encCvv2 = ((String) crdmap.get("CVV2")); trace("cvv2  " + cvv2);
		 * 
		 * 
		 * enIcvv = ((String) crdmap.get("ICVV")); trace("icvv " + icvv); }
		 * 
		 * }
		 */

		try {

			padsssec = new PadssSecurity();

			if (padssenable.equals("Y")) {
				Iterator secitr = secList.iterator();
				if (!secList.isEmpty()) {
					while (secitr.hasNext()) {
						Map map = (Map) secitr.next();
						String CDMK = ((String) map.get("DMK"));
						String CDPK = padsssec.decryptDPK(CDMK, EDPK);
						dcardno = padsssec.getCHN(CDPK, ecardno);
					}
				}
			}
			if (instid.equalsIgnoreCase("IBA")) {

				// by siva 05-07-2019

				/*
				 * carddetail_qury.append(
				 * "select CARD_NO as CARD_NO,ORDER_REF_NO,SERVICE_CODE, ");
				 * carddetail_qury .append(
				 * "BRANCH_CODE,to_char(EXPIRY_DATE,'MM/YY') as EXP_1,trim(ENC_NAME) as ENCNAME,EMB_NAME, "
				 * ); carddetail_qury.append(
				 * "to_char(EXPIRY_DATE,'YYMM') as EXP_2,CVV1,CVV2, ICVV, CARD_REF_NO , "
				 * );
				 */

				carddetail_qury
						.append("select BIN,PRODUCT_CODE,CARD_NO as CARD_NO,ORDER_REF_NO,SERVICE_CODE, ");
				carddetail_qury
						.append("BRANCH_CODE,to_char(EXPIRY_DATE,'MM/YY') as EXP_1,trim(ENC_NAME) as ENCNAME,EMB_NAME, ");
				carddetail_qury
						.append("to_char(EXPIRY_DATE,'YYMM') as EXP_2,CVV1,CVV2, ICVV, CARD_REF_NO , ");

				// by siva 05-07-2019

				// carddetail_qury.append("'%B'|| ");
				if (padssenable.equals("Y")) {
					carddetail_qury.append("'" + dcardno + "'|| ");
				} else {
					carddetail_qury.append("CARD_NO|| ");
				}
				carddetail_qury.append("'^'|| ");
				carddetail_qury.append("RPAD(ENC_NAME,'26',' ')|| ");
				carddetail_qury.append("'^'|| ");
				carddetail_qury.append("to_char(EXPIRY_DATE,'yymm')|| ");
				carddetail_qury.append("LPAD(SERVICE_CODE,'3',' ')|| ");
				carddetail_qury.append("'0'|| ");// --pvki
				carddetail_qury
						.append("lpad( NVL(to_char( PIN_OFFSET),0),4,'0') || ");// --pvv
				carddetail_qury.append("'99999999'|| ");
				carddetail_qury.append("'00'|| ");
				carddetail_qury.append("CVV1|| ");// cvc1
				carddetail_qury.append("'000000'|| ");
				carddetail_qury.append("'' TRACK1, ");
				carddetail_qury.append("' '|| ");
				if (padssenable.equals("Y")) {
					carddetail_qury.append("'" + dcardno + "'|| ");
				} else {
					carddetail_qury.append("CARD_NO|| ");
				}
				carddetail_qury.append("'='|| ");
				carddetail_qury.append("to_char(EXPIRY_DATE,'yymm')|| ");
				carddetail_qury.append("LPAD(SERVICE_CODE,'3',' ')|| ");
				carddetail_qury.append("'0'|| ");// pvki
				carddetail_qury
						.append("lpad( NVL(to_char( PIN_OFFSET),0),4,'0')|| ");// pvv
				carddetail_qury.append("CVV1|| ");// --cvc1
				carddetail_qury.append("'99999'|| ");
				carddetail_qury.append("''TRACK2,");
				carddetail_qury.append("'ORDER_REF_NO'");
				carddetail_qury.append(" from " + table + " ");
				carddetail_qury.append("where inst_id='" + instid
						+ "' and card_no='" + ecardno
						+ "' ORDER BY ORDER_REF_NO");

			}

			else {
				// System.out.println("dcardno::::"+dcardno+"ecard::::"+ecardno+"padssenable::"+padssenable);

				carddetail_qury
						.append("select BIN,CARD_COLLECT_BRANCH,CIN,PRODUCT_CODE,ORG_CHN as CARD_NO,ORDER_REF_NO,SERVICE_CODE, ");
				carddetail_qury
						.append("BRANCH_CODE,to_char(EXPIRY_DATE,'MM/YY') as EXP_1,trim(ENC_NAME) as ENCNAME,EMB_NAME, ");
				carddetail_qury
						.append("to_char(EXPIRY_DATE,'YYMMDD') as EXP_2,CVV1,CVV2, ICVV, CARD_REF_NO , ");
				carddetail_qury.append("'B'|| ");

				if (padssenable.equals("Y")) {
					carddetail_qury.append("'" + dcardno + "'|| ");
				} else {
					carddetail_qury.append("CARD_NO|| ");
				}
				carddetail_qury.append("'^'|| ");
				carddetail_qury.append("RPAD(ENC_NAME,'25',' ')|| ");
				carddetail_qury.append("'/'|| ");
				carddetail_qury.append("'^'|| ");
				carddetail_qury.append("to_char(EXPIRY_DATE,'yymm')|| ");
				carddetail_qury.append("LPAD(SERVICE_CODE,'3',' ')|| ");
				carddetail_qury.append("'0'|| ");// --pvki
				carddetail_qury
						.append("lpad( NVL(to_char( PIN_OFFSET),0),4,'0') || ");// --pvv
				carddetail_qury.append("'00000000'|| ");
				carddetail_qury.append("'00'|| ");
				// carddetail_qury.append("CVV1|| ");// cvc1
				// carddetail_qury.append("|| ");// cvc1
				carddetail_qury.append("'000000'||");
				carddetail_qury.append("'0' TRACK1, ");
				// carddetail_qury.append("|| ");
				// carddetail_qury.append("';'|| ");
				if (padssenable.equals("Y")) {
					carddetail_qury.append("'" + dcardno + "'|| ");
				} else {
					carddetail_qury.append("CARD_NO|| ");
				}
				carddetail_qury.append("'='|| ");
				carddetail_qury.append("to_char(EXPIRY_DATE,'yymm')|| ");
				carddetail_qury.append("LPAD(SERVICE_CODE,'3',' ')|| ");
				carddetail_qury.append("'0'|| ");// pvki
				// carddetail_qury.append("lpad( NVL(to_char(
				// PIN_OFFSET),0),4,'0')|| ");// pvv
				// carddetail_qury.append(cvv1);
				carddetail_qury.append("CVV1|| ");// cvc1
				//carddetail_qury.append("'000000' ");
				carddetail_qury.append("'00000' ");
				carddetail_qury.append("TRACK2,");
				carddetail_qury.append("'ORDER_REF_NO'");
				carddetail_qury.append(" from " + table + " ");
				carddetail_qury.append("where inst_id='" + instid
						+ "' and ORG_CHN='" + ecardno
						+ "' ORDER BY ORDER_REF_NO");

			}

			enctrace("carddetail_qury ====     " + carddetail_qury);

			dcardno = "0000000000000000";
			dcardno = null;

		} catch (Exception exce) {
			exce.printStackTrace();
		} finally {

			dcardno = null;
			padsssec = null;
		}
		carddetail = jdbctemplate.queryForList(carddetail_qury.toString());
		return carddetail;
	}

	public List getCardaddress(String instid, String productcode,
			JdbcTemplate jdbctemplate) {
		List addresdetails = null;

		String cvvdata_qury = "select ADDRESS from INST_TEMPNAME where INST_ID='"
				+ instid + "' and PRODUCT_CODE='" + productcode + "'";
		trace("cvvdata_qury====>" + cvvdata_qury);
		addresdetails = jdbctemplate.queryForList(cvvdata_qury);
		return addresdetails;
	}

	public List getCVVdata(String instid, String cardno, String ordflag,
			JdbcTemplate jdbctemplate) {
		List cvvdata = null;

		String cvvdata_qury = "select CVV1,CVV2 from IFP_PIN_PROCESS where INST_ID='"
				+ instid
				+ "' and CARD_NO='"
				+ cardno
				+ "' and ORDER_FLAG='"
				+ ordflag + "'";
		trace("cvvdata_qury====>" + cvvdata_qury);
		cvvdata = jdbctemplate.queryForList(cvvdata_qury);
		return cvvdata;
	}

	public String formateEncodingname(String encname) {
		String name = "X";
		int namelen = encname.length();
		// trace("The Lenght of the Name is =====> "+namelen);
		if (namelen == 25) {
			name = encname;
		}
		if (namelen < 25) {
			for (int i = namelen; i < 25; i++) {
				encname = encname + " ";
			}
			name = encname;
		}
		if (namelen > 25) {
			name = encname.substring(0, 25);
		}
		// trace("The Return Value is====>"+name);
		return name;
	}

	public int maintenanceRecordscheck(String inst_id, String condition,
			JdbcTemplate jdbctemplate) {
		int recordcount = 0;
		String maintainqury = "SELECT COUNT(*) FROM PERS_CARD_PROCESS WHERE INST_ID='"
				+ inst_id + "' AND " + condition;
		trace("===maintainqury====> " + maintainqury);
		recordcount = (Integer) jdbctemplate.queryForObject(maintainqury,
				Integer.class);
		trace("Record Count----> " + recordcount);
		return recordcount;
	}

	public String getISOBitmap(String inst_id, String pcode,
			JdbcTemplate jdbctemplate) {
		String bitmaps = null;
		String bitmap_qury = "SELECT ISO_BITMAP FROM IFP_ISO8583_SPEC WHERE INST_ID='"
				+ inst_id + "' AND PROC_CODE='" + pcode + "'";
		bitmaps = (String) jdbctemplate.queryForObject(bitmap_qury,
				String.class);
		return bitmaps;
	}

	public String getLocaldatetime(String flag) {
		String datetime = null;
		String formatflag = "";
		if (flag.equals("DT")) {
			formatflag = "ddMMyyHHmmss";
		}
		if (flag.equals("D")) {
			formatflag = "ddMMyy";
		}
		if (flag.equals("T")) {
			formatflag = "HHmmss";
		}
		DateFormat dateFormat = new SimpleDateFormat(formatflag);
		Date date = new Date();
		datetime = dateFormat.format(date);
		trace("Local Date Time===>" + datetime);
		return datetime;
	}

	public String getInvoicetrace(String instid, JdbcTemplate jdbctemplate) {
		String invoice = null;

		String invoice_seq = "SELECT INVOICE_TRACE,INVOICE_LEN FROM INSTITUTION WHERE INST_ID='"
				+ instid + "'";
		String seq = "N", len = "N", geninvoice = "N";

		List invoicedata = jdbctemplate.queryForList(invoice_seq);
		Iterator itr = invoicedata.iterator();
		while (itr.hasNext()) {
			Map map = (Map) itr.next();
			seq = (String) map.get("INVOICE_TRACE");
			len = (String) map.get("INVOICE_LEN");
		}
		trace("Invoice Sequnce ===>" + seq + " InVoice Length===> " + len);
		if (!(seq.equals("N")) && !(len.equals("N"))) {
			geninvoice = orderreferenceno(seq, Integer.parseInt(len));
			if (!(geninvoice.equals("N")) && !(geninvoice.equals("X"))) {
				invoice = geninvoice;
			}
		}
		trace("InVoice Genereated Return is ---> " + invoice);
		return invoice;

	}

	public List maintenanceCardslist(String instid, String cardstatus,
			String mkckstatus, String cafstatus, String condition,
			JdbcTemplate jdbctemplate) throws Exception {
		List maintaincardlist = null;
		String cafcheck = "";
		if (!(cafstatus.equals("N"))) {
			cafcheck = "AND CAF_REC_STATUS in('S','AC','D','DE','BR','BN','"
					+ cafstatus + "')";
		}

		/*
		 * String select_query =
		 * "select ORDER_REF_NO,ORG_CHN,MCARD_NO,(SELECT HCARD_NO FROM PERS_CARD_PROCESS_HASH WHERE ORDER_REF_NO=A.ORDER_REF_NO) AS HCARD_NO,ACCT_NO,bin,CIN,card_type_id as CARDTYPE,EMB_NAME,sub_prod_id as SUBPRODID,product_code,to_char(generated_date,'DD-MON-YY') as GENDATE,MAKER_ID,branch_code,PRE_FILE,  "
		 * +
		 * " (SELECT USERNAME  FROM USER_DETAILS WHERE  USERID=A.CHECKER_ID) AS CHECKER,(SELECT BRANCH_NAME FROM BRANCH_MASTER WHERE BRANCH_CODE=CARD_COLLECT_BRANCH) AS CARD_COLLECT_BRANCH,"
		 * +
		 * " (SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID=A.INST_ID and PRODUCT_CODE=A.PRODUCT_CODE) PNAME,"
		 * +
		 * " (SELECT USERNAME  FROM USER_DETAILS WHERE INSTID=A.INST_ID and USERID=A.MAKER_ID OR USERNAME=A.MAKER_ID and rownum <= 1) USERNAME, "
		 * +
		 * " (SELECT SUB_PRODUCT_NAME FROM INSTPROD_DETAILS WHERE INST_ID=A.INST_ID and SUB_PROD_ID=A.SUB_PROD_ID) SUBPRODDESC"
		 * + " from PERS_CARD_PROCESS A" + " where inst_id='" + instid +
		 * "' and CARD_STATUS='" + cardstatus + "' " + cafcheck +
		 * " and mkck_status='" + mkckstatus + "'" + condition +
		 * " order by CAF_REC_STATUS";
		 */

		// by gowtham-260819
		String select_query = "select ORDER_REF_NO,ORG_CHN,MCARD_NO,(SELECT HCARD_NO FROM PERS_CARD_PROCESS_HASH WHERE ORDER_REF_NO=A.ORDER_REF_NO) AS HCARD_NO,ACCT_NO,bin,CIN,card_type_id as CARDTYPE,EMB_NAME,sub_prod_id as SUBPRODID,product_code,to_char(generated_date,'DD-MON-YY') as GENDATE,MAKER_ID,branch_code,PRE_FILE,  "
				+ " (SELECT USERNAME  FROM USER_DETAILS WHERE  USERID=A.CHECKER_ID) AS CHECKER,(SELECT BRANCH_NAME FROM BRANCH_MASTER WHERE BRANCH_CODE=CARD_COLLECT_BRANCH) AS CARD_COLLECT_BRANCH,"
				+ " (SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID=A.INST_ID and PRODUCT_CODE=A.PRODUCT_CODE) PNAME,"
				+ " (SELECT USERNAME  FROM USER_DETAILS WHERE INSTID=A.INST_ID and USERID=A.MAKER_ID OR USERNAME=A.MAKER_ID and rownum <= 1) USERNAME, "
				+ " (SELECT SUB_PRODUCT_NAME FROM INSTPROD_DETAILS WHERE INST_ID=A.INST_ID and SUB_PROD_ID=A.SUB_PROD_ID) SUBPRODDESC"
				+ " from PERS_CARD_PROCESS A"
				+ " where inst_id=? and CARD_STATUS=? "
				+ cafcheck
				+ " and mkck_status=? "
				+ condition
				+ " order by CAF_REC_STATUS";

		// // enctrace("3030authcards_query ====="+select_query);

		/*
		 * String select_query=
		 * "select ORDER_REF_NO,CARD_NO,MCARD_NO,HCARD_NO,ACCT_NO,bin,card_type_id as CARDTYPE,EMB_NAME,sub_prod_id as SUBPRODID,product_code,to_char(generated_date,'DD-MON-YY') as GENDATE,MAKER_ID,branch_code,PRE_FILE  from PERS_CARD_PROCESS "
		 * + "where inst_id='"+instid+"' and CARD_STATUS='"+cardstatus+"' "
		 * +cafcheck+" and mkck_status='"+mkckstatus+
		 * "' AND EXPIRY_DATE > '29-FEB-2016' order by CAF_REC_STATUS";
		 * ////enctrace("3030authcards_query ====="+select_query);
		 */

		enctrace("The Query is ==========>" + select_query);

		/* maintaincardlist = jdbctemplate.queryForList(select_query); */

		maintaincardlist = jdbctemplate.queryForList(select_query,
				new Object[] { instid, cardstatus, mkckstatus });

		trace("The Query Result is ==========>" + maintaincardlist);
		/*
		 * ListIterator itr = maintaincardlist.listIterator(); while(
		 * itr.hasNext() ){ Map temp = (Map)itr.next(); String refnum =
		 * (String)temp.get("ORDER_REF_NO"); String prodcode =
		 * (String)temp.get("PRODUCT_CODE"); String usercode =
		 * (String)temp.get("MAKER_ID"); String binno = (String)temp.get("BIN");
		 * //String productdesc = getProductdesc(instid, binno,prodcode); String
		 * productdesc = getProductdesc(instid,prodcode, jdbctemplate); String
		 * username = getUserName(instid, usercode, jdbctemplate); String count
		 * =
		 * getCardcount(instid,(String)temp.get("CARD_NO"),cardstatus,mkckstatus
		 * ,"CARD",jdbctemplate ); //String
		 * embossname=getEmbossingName(instid,refnum,"CARD",jdbctemplate);
		 * temp.put("SUBPRODDESC", getSubProductdesc(instid,
		 * (String)temp.get("SUBPRODID"), jdbctemplate)); temp.put("CARDNO",
		 * (String)temp.get("CARD_NO")); temp.put("PRODUCTDESC", productdesc);
		 * temp.put("COUNT", count); temp.put("EMBOSSING_NAME",
		 * (String)temp.get("EMB_NAME") ); temp.put("PNAME", productdesc);
		 * temp.put("USERNAME", username); itr.remove(); itr.add(temp); }
		 */

		return maintaincardlist;

	}

	public int checkCustomerexist(String instid, String custnum,
			JdbcTemplate jdbcTemplate) {
		int existcount = -1;
		String count_qury = "select count(*) from IFP_CUSTINFO_PRODUCTION where INST_ID='"
				+ instid + "' and CIN='" + custnum + "'";
		trace("Cutomer Checked Count Qury ==> " + count_qury);
		existcount = jdbcTemplate.queryForInt(count_qury);
		return existcount;
	}

	public List getKYCdetails(String instid, String cin,
			JdbcTemplate jdbcTemplate) {
		List kycdetails = null;

		/*
		 * String custdetail_qury =
		 * "SELECT FNAME,MNAME,LNAME,FATHER_NAME,MOTHER_NAME,MARITAL_STATUS,SPOUSE_NAME,GENDER,"
		 * +
		 * "OCCUPATION,NATIONALITY,to_char(DOB,'DD-MM-YYYY') as DOB,EMAIL_ADDRESS,MOBILE_NO,PHONE_NO,ID_NUMBER,ID_DOCUMENT,"
		 * +
		 * "POST_ADDR1,POST_ADDR2,POST_ADDR3,POST_ADDR4,RES_ADDR1,RES_ADDR2,RES_ADDR3,RES_ADDR4 FROM IFP_CUSTINFO_PRODUCTION"
		 * + " where INST_ID='"+ instid + "' and CIN='" + cin + "'"; kycdetails
		 * = jdbcTemplate.queryForList(custdetail_qury);
		 */

		// by gowtham
		String custdetail_qury = "SELECT FNAME,MNAME,LNAME,FATHER_NAME,MOTHER_NAME,MARITAL_STATUS,SPOUSE_NAME,GENDER,"
				+ "OCCUPATION,NATIONALITY,to_char(DOB,'DD-MM-YYYY') as DOB,EMAIL_ADDRESS,MOBILE_NO,PHONE_NO,ID_NUMBER,ID_DOCUMENT,"
				+ "POST_ADDR1,POST_ADDR2,POST_ADDR3,POST_ADDR4,RES_ADDR1,RES_ADDR2,RES_ADDR3,RES_ADDR4 FROM IFP_CUSTINFO_PRODUCTION"
				+ " where INST_ID=? and CIN=?";
		kycdetails = jdbcTemplate.queryForList(custdetail_qury, new Object[] {
				instid, cin });

		return kycdetails;
	}

	public String gettingCAFstatus(String cardno, String padssenable,
			String instid, JdbcTemplate jdbcTemplate) {
		String cafstatus = "X", caf_qury = "";
		if (padssenable.equals("Y")) {
			caf_qury = "select trim(CAF_REC_STATUS) as CAF_REC_STATUS from PERS_CARD_PROCESS where INST_ID='"
					+ instid + "' and ORG_CHN='" + cardno + "'";
		} else {
			caf_qury = "select trim(CAF_REC_STATUS) as CAF_REC_STATUS from PERS_CARD_PROCESS where INST_ID='"
					+ instid + "' and ORG_CHN='" + cardno + "'";
		}
		cafstatus = (String) jdbcTemplate
				.queryForObject(caf_qury, String.class);
		return cafstatus;
	}

	public String gettingInstCAFstatus(String cardno, String padssenable,
			String instid, JdbcTemplate jdbcTemplate) {
		String cafstatus = "X", caf_qury = "";
		// if (padssenable.equals("Y")) {

		/*
		 * caf_qury =
		 * "select trim(CAF_REC_STATUS) as CAF_REC_STATUS from INST_CARD_PROCESS "
		 * + "where INST_ID='" + instid+ "' and ORG_CHN='" + cardno + "' ";
		 * trace("checking query-->" + caf_qury);
		 */

		// / by gowtham-280819
		caf_qury = "select trim(CAF_REC_STATUS) as CAF_REC_STATUS from INST_CARD_PROCESS "
				+ "where INST_ID=? and ORG_CHN=? ";
		trace("checking query-->" + caf_qury);

		// BY SIVA 15-07-2019
		/*
		 * } else { caf_qury =
		 * "select trim(CAF_REC_STATUS) as CAF_REC_STATUS from INST_CARD_PROCESS where INST_ID='"
		 * + instid + "' and CARD_NO='" + cardno + "'"; }
		 */
		/*
		 * cafstatus = (String) jdbcTemplate.queryForObject(caf_qury,
		 * String.class);
		 */

		cafstatus = (String) jdbcTemplate.queryForObject(caf_qury,
				new Object[] { instid, cardno }, String.class);

		return cafstatus;
	}

	public List getCardAccountnumbers(String cardno, String instid,
			JdbcTemplate jdbctemplate) {
		List accountnos = null;
		// String acct_qury = "select distinct(acct_no) as ACCTNO,ACCT_CCY from
		// IFD_CARD_ACCT_LINK where INST_ID='BUCB' and cin = (select
		// distinct(cin) from IFD_CARD_ACCT_LINK where card_no='"+cardno+"') and
		// ACCT_STATUS='1'";

		String acct_qury = "select ACCT_NO,ACCT_CCY from IFD_CARD_ACCT_LINK where INST_ID='"
				+ instid + "' and card_no='" + cardno + "' and acct_status='1'";
		trace("acct_qury===> " + acct_qury);
		accountnos = jdbctemplate.queryForList(acct_qury);
		trace("accountnos ==> " + accountnos);
		if (!(accountnos.isEmpty())) {
			trace("Query Resulkt is NOT EMPTY ");
			ListIterator itr = accountnos.listIterator();
			while (itr.hasNext()) {
				Map temp = (Map) itr.next();
				String ccy = (String) temp.get("ACCT_CCY");
				String ccurency_desc = getGlobalcurrencydesc(ccy, jdbctemplate);
				trace("ccurency_desc===> " + ccurency_desc);
				temp.put("CCY_DESC", ccurency_desc);
				itr.remove();
				itr.add(temp);
			}
		}
		trace("Rerun the result===> " + accountnos);
		return accountnos;
	}

	public String getGlobalcurrencydesc(String ccy, JdbcTemplate jdbctemplate) {
		String ccy_desc = null;

		/*
		 * String cur_desc =
		 * "select CURRENCY_DESC from GLOBAL_CURRENCY where CURRENCY_CODE='" +
		 * ccy+ "' OR NUMERIC_CODE='" + ccy + "'"; trace("Cuuren Qury ==> " +
		 * cur_desc); ccy_desc = (String) jdbctemplate.queryForObject(cur_desc,
		 * String.class);
		 */

		// by gowtham-200819
		String cur_desc = "select CURRENCY_DESC from GLOBAL_CURRENCY where CURRENCY_CODE=? OR NUMERIC_CODE=?";
		trace("Cuuren Qury ==> " + cur_desc);
		ccy_desc = (String) jdbctemplate.queryForObject(cur_desc, new Object[] {
				ccy, ccy, }, String.class);

		trace("Currency Desc ==> " + ccy_desc);
		return ccy_desc;
	}

	public String getOrderStatus(String inst, String refnum, String ordertype,
			JdbcTemplate jdbcTemplate) {
		String table = "";
		if (ordertype.equals("I")) {
			table = "INST_CARD_ORDER";
		} else {
			table = "PERS_CARD_ORDER";
		}
		String orderstatus = null;
		String orderstatus_qury = "select ORDER_STATUS from " + table
				+ " where INST_ID='" + inst + "' and ORDER_REF_NO='" + refnum
				+ "'";
		trace("====>" + orderstatus_qury);
		orderstatus = (String) jdbcTemplate.queryForObject(orderstatus_qury,
				String.class);
		trace("The Rusult Is ----> " + orderstatus);
		return orderstatus;
	}

	public List geteditdeleteOrderdetails(String instid, String refno,
			JdbcTemplate jdbctemplate) throws Exception {

		List orderinfo = null;
		// String orderinfo_qury = "select inst_id, order_ref_no, card_type_id,
		// sub_prod_id, product_code, bin, card_quantity,
		// to_char(ordered_date,'DD-MON-YY HH24:MI:SS') as ordered_date,
		// maker_id, embossing_name, encode_data, to_char(app_date,'DD-MON-YY')
		// as app_date, app_no from "+table+" where INST_ID='"+instid+"' and
		// ORDER_REF_NO='"+refno+"'";

		/*
		 * String orderinfo_qury =
		 * "select co.inst_id as inst_id,co.order_ref_no as order_ref_no,co.card_type_id as card_type_id,co.sub_prod_id as sub_prod_id,co.product_code as product_code,co.bin as bin,"
		 * +
		 * "co.card_quantity as card_quantity,to_char(co.ordered_date,'DD-MON-YY HH24:MI:SS') as ordered_date,co.maker_id as maker_id,co.embossing_name as embossing_name,co.encode_data as encode_data,"
		 * +
		 * "to_char(co.app_date,'DD-MON-YY') as app_date,co.cin as cin,co.app_no as app_no,co.branch_code as branch_code,cp.FNAME as FNAME,cp.MNAME as MNAME,cp.LNAME as LNAME,cp.FATHER_NAME as FATHER_NAME,cp.MOTHER_NAME as MOTHER_NAME,"
		 * +
		 * "cp.MARITAL_STATUS as MARITAL_STATUS,cp.SPOUSE_NAME as SPOUSE_NAME,cp.GENDER as GENDER,to_char(cp.DOB,'DD-MON-YYYY') as DOB,cp.NATIONALITY as NATIONALITY,cp.EMAIL_ADDRESS as EMAIL_ADDRESS,"
		 * +
		 * "cp.MOBILE_NO as MOBILE_NO,cp.PHONE_NO as PHONE_NO,cp.OCCUPATION as OCCUPATION,cp.ID_NUMBER as ID_NUMBER,cp.ID_DOCUMENT as ID_DOCUMENT,cp.POST_ADDR1 as POST_ADDR1,cp.POST_ADDR2 as POST_ADDR2,"
		 * +
		 * "cp.POST_ADDR3 as POST_ADDR3,cp.POST_ADDR4 as POST_ADDR4,cp.RES_ADDR1 as RES_ADDR1,cp.RES_ADDR2 as RES_ADDR2,cp.RES_ADDR3 as RES_ADDR3,cp.RES_ADDR4 as RES_ADDR4,REMARKS as REMARKS , cp.PHOTO_URL as PHOTO_URL,"
		 * +
		 * " cp.SIGNATURE_URL as SIGNATURE_URL, cp.IDPROOF_URL as IDPROOF_URL from PERS_CARD_ORDER co,IFP_CUSTINFO_PROCESS cp "
		 * + "where co.INST_ID='" + instid + "' and co.ORDER_REF_NO='" + refno +
		 * "' and co.INST_ID=cp.INST_ID and co.CIN=cp.CIN"; trace(
		 * "orderinfo_qury===> " + orderinfo_qury); orderinfo =
		 * jdbctemplate.queryForList(orderinfo_qury);
		 */

		// by gowtham
		String orderinfo_qury = "select co.inst_id as inst_id,co.order_ref_no as order_ref_no,co.card_type_id as card_type_id,co.sub_prod_id as sub_prod_id,co.product_code as product_code,co.bin as bin,"
				+ "co.card_quantity as card_quantity,to_char(co.ordered_date,'DD-MON-YY HH24:MI:SS') as ordered_date,co.maker_id as maker_id,co.embossing_name as embossing_name,co.encode_data as encode_data,"
				+ "to_char(co.app_date,'DD-MON-YY') as app_date,co.cin as cin,co.app_no as app_no,co.branch_code as branch_code,cp.FNAME as FNAME,cp.MNAME as MNAME,cp.LNAME as LNAME,cp.FATHER_NAME as FATHER_NAME,cp.MOTHER_NAME as MOTHER_NAME,"
				+ "cp.MARITAL_STATUS as MARITAL_STATUS,cp.SPOUSE_NAME as SPOUSE_NAME,cp.GENDER as GENDER,to_char(cp.DOB,'DD-MON-YYYY') as DOB,cp.NATIONALITY as NATIONALITY,cp.EMAIL_ADDRESS as EMAIL_ADDRESS,"
				+ "cp.MOBILE_NO as MOBILE_NO,cp.PHONE_NO as PHONE_NO,cp.OCCUPATION as OCCUPATION,cp.ID_NUMBER as ID_NUMBER,cp.ID_DOCUMENT as ID_DOCUMENT,cp.POST_ADDR1 as POST_ADDR1,cp.POST_ADDR2 as POST_ADDR2,"
				+ "cp.POST_ADDR3 as POST_ADDR3,cp.POST_ADDR4 as POST_ADDR4,cp.RES_ADDR1 as RES_ADDR1,cp.RES_ADDR2 as RES_ADDR2,cp.RES_ADDR3 as RES_ADDR3,cp.RES_ADDR4 as RES_ADDR4,REMARKS as REMARKS , cp.PHOTO_URL as PHOTO_URL,"
				+ " cp.SIGNATURE_URL as SIGNATURE_URL, cp.IDPROOF_URL as IDPROOF_URL from PERS_CARD_ORDER co,IFP_CUSTINFO_PROCESS cp "
				+ "where co.INST_ID=? and co.ORDER_REF_NO=? and co.INST_ID=cp.INST_ID and co.CIN=cp.CIN";
		trace("orderinfo_qury===> " + orderinfo_qury);
		orderinfo = jdbctemplate.queryForList(orderinfo_qury, new Object[] {
				instid, refno });

		if (!(orderinfo.isEmpty())) {
			ListIterator itr = orderinfo.listIterator();
			while (itr.hasNext()) {
				Map temp = (Map) itr.next();
				String refnum = (String) temp.get("ORDER_REF_NO");
				String subprodcode = (String) temp.get("SUB_PROD_ID");
				String usercode = (String) temp.get("MAKER_ID");
				String binno = (String) temp.get("BIN");
				String idtype = (String) temp.get("ID_DOCUMENT");
				String gender = (String) temp.get("GENDER");
				String prodname = getSubProductdesc(instid, subprodcode,
						jdbctemplate);
				String username = getUserName(instid, usercode, jdbctemplate);
				String idtypedesc = this.getDocumentName(instid, idtype,
						jdbctemplate);
				if (gender != null) {
					if (gender.equals("M")) {
						gender = "Male";
					} else if (gender.equals("F")) {
						gender = "FeMale";
					}
				} else {
					gender = "--";
				}
				temp.put("GENDER", gender);
				temp.put("ID_DOCUMENT", idtypedesc);
				temp.put("PROD_NAME", prodname);
				temp.put("USER_NAME", username);
				itr.remove();
				itr.add(temp);
			}

		}
		trace("Vorderinfo===> " + orderinfo);
		return orderinfo;
	}

	public int checkOrderexsist(String instid, String refno, String ordertype,
			JdbcTemplate jdbcTemplate) {
		int ordercount = 0;

		String table = "";
		if (ordertype.equals("I")) {
			table = "INST_CARD_ORDER";
		} else {
			table = "PERS_CARD_ORDER";
		}
		String ordercount_qury = "select count(*) from " + table
				+ " where INST_ID='" + instid + "' and ORDER_REF_NO='" + refno
				+ "'";
		trace("ordercount_qury===> " + ordercount_qury);
		ordercount = jdbcTemplate.queryForInt(ordercount_qury);
		return ordercount;
	}

	public List getOrderdelete(String instid, String kyc_flag,
			String mkck_flag, String condition, JdbcTemplate jdbctemplate)
			throws Exception {
		List deletelist = null;

		/*
		 * String order_del_list_qury =
		 * "select ORDER_REF_NO,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,CARD_QUANTITY,"
		 * +
		 * "to_char(ORDERED_DATE,'DD-MON-YY') as ORDERED_DATE,EMBOSSING_NAME,MKCK_STATUS,BRANCH_CODE,MAKER_ID,CIN,"
		 * +
		 * " decode(KYC_FLAG,'0','New Order','1','KYC Order') KYC_FLAG from PERS_CARD_ORDER where INST_ID='"
		 * + instid + "' and ORDER_STATUS='01' and MKCK_STATUS='" + mkck_flag +
		 * "'  " + condition+ " order by order_ref_no,ORDERED_DATE"; trace(
		 * "The order_del_list_qury===> " + order_del_list_qury); deletelist =
		 * jdbctemplate.queryForList(order_del_list_qury);
		 */

		// by gowtham
		String order_del_list_qury = "select ORDER_REF_NO,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,CARD_QUANTITY,"
				+ "to_char(ORDERED_DATE,'DD-MON-YY') as ORDERED_DATE,EMBOSSING_NAME,MKCK_STATUS,BRANCH_CODE,MAKER_ID,CIN,"
				+ " decode(KYC_FLAG,'0','New Order','1','KYC Order') KYC_FLAG from PERS_CARD_ORDER "
				+ "where INST_ID=?  and ORDER_STATUS=? and MKCK_STATUS=?  "
				+ condition + " order by order_ref_no,ORDERED_DATE";
		trace("The order_del_list_qury===> " + order_del_list_qury);
		deletelist = jdbctemplate.queryForList(order_del_list_qury,
				new Object[] { instid, "01", mkck_flag });

		if (!(deletelist.isEmpty())) {
			ListIterator listitr = deletelist.listIterator();
			while (listitr.hasNext()) {
				Map temp = (Map) listitr.next();
				String subprodcode = (String) temp.get("SUB_PROD_ID");
				String usercode = (String) temp.get("MAKER_ID");
				String prodname = getSubProductdesc(instid, subprodcode,
						jdbctemplate);
				String username = getUserName(instid, usercode, jdbctemplate);
				temp.put("PROD_NAME", prodname);
				temp.put("USER_NAME", username);
				listitr.remove();
				listitr.add(temp);
			}
		}

		return deletelist;
	}

	public String personalOrdercustomerregistrationcheck(String instid,
			String product_code, String subproduct, JdbcTemplate jdbcTemplate)
			throws Exception {
		String cust_required = "X";

		/*
		 * String reg_qury =
		 * "select trim(CUST_REG_REQ) as CUST_REG_REQ from INSTPROD_DETAILS where inst_id='"
		 * + instid + "' and PRODUCT_CODE='" + product_code +
		 * "' and SUB_PROD_ID='" + subproduct + "'"; //// enctrace(
		 * "3030reg_qury : "+reg_qury); cust_required = (String)
		 * jdbcTemplate.queryForObject(reg_qury, String.class);
		 */

		// / by gowtham
		String reg_qury = "select trim(CUST_REG_REQ) as CUST_REG_REQ from INSTPROD_DETAILS where inst_id=? and PRODUCT_CODE=? and SUB_PROD_ID=? ";
		// // enctrace("3030reg_qury : "+reg_qury);
		cust_required = (String) jdbcTemplate
				.queryForObject(reg_qury, new Object[] { instid, product_code,
						subproduct }, String.class);

		return cust_required;
	}

	public int checkCustomerOrderExsist(String instid, String custnum,
			JdbcTemplate jdbcTemplate) {
		int exsist = -1;
		String count_qury = "select count(*) from PERS_CARD_ORDER where inst_id='"
				+ instid + "' and cin='" + custnum + "'";
		// String count_qury = "select count(*) from PERS_CARD_ORDER where
		// inst_id='"+instid+"' and cin='"+custnum+"' and order_status='02'";
		exsist = jdbcTemplate.queryForInt(count_qury);
		trace("KYC ORder COunt is ---> " + exsist);
		return exsist;

	}

	public String checkCustomerregistrationconfig(String instid, String refnum,
			JdbcTemplate jdbcTempalte) throws Exception {
		String check = null;

		/*
		 * String getinfo_qury =
		 * "select CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE from " +
		 * "PERS_CARD_ORDER where INST_ID='"+ instid + "' and ORDER_REF_NO='" +
		 * refnum + "'"; List getinfo_list =
		 * jdbcTempalte.queryForList(getinfo_qury);
		 */

		// bygowtham
		String getinfo_qury = "select CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE from "
				+ "PERS_CARD_ORDER where INST_ID=?  and ORDER_REF_NO=? ";
		List getinfo_list = jdbcTempalte.queryForList(getinfo_qury,
				new Object[] { instid, refnum });

		if (!(getinfo_list.isEmpty())) {
			Iterator itr = getinfo_list.iterator();
			while (itr.hasNext()) {
				Map map = (Map) itr.next();
				String cardtype = (String) map.get("CARD_TYPE_ID");
				String subprod = (String) map.get("SUB_PROD_ID");
				String produt_code = (String) map.get("PRODUCT_CODE");
				trace("Card Type ===> " + cardtype + "SUB_PROD_ID===> "
						+ subprod + " PRODUCT_CODE==> " + produt_code);
				check = personalOrdercustomerregistrationcheck(instid,
						produt_code, subprod, jdbcTempalte);

			}
		}

		trace("The Value Return is===> " + check);
		return check;
	}

	public String getSubproductname(String instid, String product_code,
			String subid, JdbcTemplate jdbcTemplate) {
		String sub_name = null;

		/*
		 * String prodname_qury =
		 * "SELECT TRIM(SUB_PRODUCT_NAME) AS SUB_PRODUCT_NAME FROM INSTPROD_DETAILS WHERE INST_ID='"
		 * + instid + "' AND PRODUCT_CODE='" + product_code +
		 * "' AND SUB_PROD_ID='" + subid + "'"; sub_name = (String)
		 * jdbcTemplate.queryForObject(prodname_qury, String.class);
		 */

		// by gowtham-210819
		String prodname_qury = "SELECT TRIM(SUB_PRODUCT_NAME) AS SUB_PRODUCT_NAME FROM INSTPROD_DETAILS WHERE INST_ID=? AND PRODUCT_CODE=? AND SUB_PROD_ID=?";
		sub_name = (String) jdbcTemplate.queryForObject(prodname_qury,
				new Object[] { instid, product_code, subid }, String.class);

		return sub_name;
	}

	public List getPersonalorderdetails(String instid, String orderref,
			JdbcTemplate jdbctemplate) {
		List orderinfo = null;

		/*
		 * String order_detqury =
		 * "select INST_ID,ORDER_REF_NO,SUB_PROD_ID,PRODUCT_CODE,CARD_QUANTITY,to_char(ORDERED_DATE,"
		 * +
		 * "'DD-MON-YY HH24:MI:SS') as ORDERED_DATE,EMBOSSING_NAME,ENCODE_DATA,MAKER_ID,BRANCH_CODE,"
		 * +
		 * "BIN,to_char(APP_DATE,'DD-MON-YY') as APP_DATE,APP_NO from PERS_CARD_ORDER where inst_id='"
		 * + instid + "' and" + " order_ref_no='" + orderref + "'"; orderinfo =
		 * jdbctemplate.queryForList(order_detqury);
		 */

		// by gowtham
		String order_detqury = "select INST_ID,ORDER_REF_NO,SUB_PROD_ID,PRODUCT_CODE,CARD_QUANTITY,to_char(ORDERED_DATE,"
				+ "'DD-MON-YY HH24:MI:SS') as ORDERED_DATE,EMBOSSING_NAME,ENCODE_DATA,MAKER_ID,BRANCH_CODE,"
				+ "BIN,to_char(APP_DATE,'DD-MON-YY') as APP_DATE,APP_NO from PERS_CARD_ORDER where inst_id=? and  order_ref_no=? ";
		orderinfo = jdbctemplate.queryForList(order_detqury, new Object[] {
				instid, orderref });

		if (!(orderinfo.isEmpty())) {
			ListIterator itr = orderinfo.listIterator();
			while (itr.hasNext()) {
				Map map = (Map) itr.next();
				String subprod_id = (String) map.get("SUB_PROD_ID");
				String product_code = (String) map.get("PRODUCT_CODE");
				String userid = (String) map.get("MAKER_ID");
				String productname = getSubproductname(instid, product_code,
						subprod_id, jdbctemplate);
				String username = getUserName(instid, userid, jdbctemplate);
				map.put("PROD_NAME", productname);
				map.put("USER_NAME", username);
				itr.remove();
				itr.add(map);
			}
		}
		trace("Order Only-==-=-=-=-> " + orderinfo);
		return orderinfo;
	}

	public List gettingDocumnettype(String instid, JdbcTemplate jdbcTemplate) {
		trace("%^#^#%^#%^#%^%^%^^%%^#^ DOCUMENT TYPE FUNCTION ");
		List docutypelist = null;
		try {

			/*
			 * String docu_qury = "select * from IDENTYDOC_TYPE WHERE INST_ID='"
			 * + instid + "' ORDER BY DOC_ID ASC"; //// enctrace(
			 * "3030docutypelist====> "+docutypelist); docutypelist =
			 * jdbcTemplate.queryForList(docu_qury);
			 */

			// by gowtham220819
			String docu_qury = "select * from IDENTYDOC_TYPE WHERE INST_ID=? ORDER BY DOC_ID ASC";
			// // enctrace("3030docutypelist====> "+docutypelist);
			docutypelist = jdbcTemplate.queryForList(docu_qury,
					new Object[] { instid });

		} catch (Exception e) {
			trace("the exception is" + e.getMessage());
		}
		return docutypelist;

	}

	public String getDocumentName(String instid, String documentid,
			JdbcTemplate jdbctemplate) throws Exception {
		String docname = null;
		try {
			String docnameqry = "SELECT DOC_TYPE FROM IDENTYDOC_TYPE WHERE INST_ID='"
					+ instid + "' AND DOC_ID='" + documentid + "' ";
			docname = (String) jdbctemplate.queryForObject(docnameqry,
					String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return docname;
	}

	public String instcardstaus(String instid, String documentid,
			JdbcTemplate jdbctemplate) throws Exception {
		String docname = null;
		try {
			String docnameqry = "SELECT DOC_TYPE FROM IDENTYDOC_TYPE WHERE INST_ID='"
					+ instid + "' AND DOC_ID='" + documentid + "' ";
			docname = (String) jdbctemplate.queryForObject(docnameqry,
					String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return docname;
	}

	public String getGenderDesc(String genderid) throws Exception {
		String gender = null;
		if (genderid == null) {
			gender = "--";
		} else if (genderid.equals("M")) {
			gender = "Male";
		} else if (genderid.equals("M")) {
			gender = "Fe-Male";
		} else {
			gender = "--";
		}
		return gender;
	}

	public String getMaritalStatus(String maritalid) throws Exception {
		if (maritalid == null) {
			maritalid = "--";
		} else if (maritalid.equals("M")) {
			maritalid = "Married";
		} else if (maritalid.equals("U")) {
			maritalid = "Un-Married";
		} else {
			maritalid = "--";
		}
		return maritalid;
	}

	public String checkInstcustregistration(String instid, String cardno,
			JdbcTemplate jdbcTemplate) throws Exception {
		String req = "X";
		String cardinfo_qury = "select SUB_PROD_ID,PRODUCT_CODE from INST_CARD_PROCESS where INST_ID='"
				+ instid + "' and CARD_NO='" + cardno + "'";
		List carddet = jdbcTemplate.queryForList(cardinfo_qury);
		String subtype = "0", prodcode = "0";
		if (!(carddet.isEmpty())) {
			Iterator citr = carddet.iterator();
			while (citr.hasNext()) {
				Map cmap = (Map) citr.next();
				subtype = (String) cmap.get("SUB_PROD_ID");
				prodcode = (String) cmap.get("PRODUCT_CODE");
			}
			trace("SubProd ===> " + subtype + " Prod Code ===> " + prodcode);
			req = personalOrdercustomerregistrationcheck(instid, prodcode,
					subtype, jdbcTemplate);
		}
		trace("RETYUN VALUE ##########===> " + req);
		return req;
	}

	public int checkCustincardproduction(String inst_id, String cin,
			JdbcTemplate jdbcTemplate) {
		int id_count = -1;
		String custcount_qury = "select count(*) from CARD_PRODUCTION where INST_ID='"
				+ inst_id + "' and CIN='" + cin + "'";
		trace("Custcount Qury ===> " + custcount_qury);
		id_count = jdbcTemplate.queryForInt(custcount_qury);
		trace("ID Count ===> " + id_count);
		return id_count;
	}

	public List getConfigurationdetails(String instid, String cardtypesel,
			JdbcTemplate jdbcTemplate) {
		List prodlist = new ArrayList();
		String prodlist_qury = "select (inst_id||'~'||product_code||'~'||sub_prod_id) as KEY , pin_gen_req as VALUE from INSTPROD_DETAILS where INST_ID='"
				+ instid + "' and PRODUCT_CODE='" + cardtypesel + "'";
		trace("Product list qury ==> " + prodlist_qury);
		List cardprodlist = jdbcTemplate.queryForList(prodlist_qury);
		trace("THis is Lisy --> " + cardprodlist);
		if (!(cardprodlist.isEmpty())) {
			trace("Card Porduct is not Empty");
			Map pingenmap = new HashMap();
			trace("Map Object --> " + pingenmap);
			Iterator itrp = cardprodlist.iterator();
			while (itrp.hasNext()) {
				trace("While Loop ");
				Map pmap = (Map) itrp.next();
				String map_key = (String) pmap.get("KEY");
				String map_val = (String) pmap.get("VALUE");
				trace("Map Keyy==> " + map_key);
				trace("Map val==> " + map_val);
				pingenmap.put(map_key, map_val);
			}
			trace("Map Valus is ===> " + pingenmap);
			prodlist.add(pingenmap);
		}
		trace("THis is Return value==>" + prodlist);
		return prodlist;
	}

	public String findPingenerationrequired(List configs, String instid,
			String CHN, String cardtable, JdbcTemplate jdbcTemplate) {
		String pingenflag = "X";
		String keyis = "X";
		String keyframe_qury = "SELECT (INST_ID||'~'||PRODUCT_CODE||'~'||SUB_PROD_ID) AS KEY_VAL FROM "
				+ cardtable
				+ " WHERE INST_ID='"
				+ instid
				+ "' AND CARD_NO='"
				+ CHN + "'";
		trace("keyframe_qury==> " + keyframe_qury);
		keyis = (String) jdbcTemplate.queryForObject(keyframe_qury,
				String.class);
		trace("keyis===> " + keyis);
		Iterator pitr = configs.iterator();
		while (pitr.hasNext()) {
			Map pinmap = (Map) pitr.next();
			pingenflag = (String) pinmap.get(keyis);
		}
		trace("pingenflag##########-----> " + pingenflag);
		return pingenflag;
	}

	// Sankar END ##############################################//

	/**************************
	 * pritto
	 * 
	 * @throws Exception
	 ********************************/

	@SuppressWarnings("unchecked")
	public List getInstantOrderList(String instid, String filtercond,
			JdbcTemplate jdbctemplate) throws Exception {
		String listoforderqry = "SELECT ORDER_REF_NO, CARD_TYPE_ID, BIN, PRODUCT_CODE, CARD_QUANTITY, EMBOSSING_NAME, MAKER_ID, to_char(MAKER_DATE, 'dd-mon-yyyy') as ORDERED_DATE"
				+ " ,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID=A.INST_ID and PRODUCT_CODE=A.PRODUCT_CODE) PRODBINDESC,"
				+ "(SELECT PRD_DESC FROM PRODUCTINFO WHERE INST_ID=A.INST_ID and BIN=A.BIN and rownum <= 1) CARDTYPEDESC,"
				+ "(SELECT USERNAME  FROM USER_DETAILS WHERE INSTID=A.INST_ID and USERID=A.MAKER_ID OR USERNAME=A.MAKER_ID and rownum <= 1) USERNAME"

				/*
				 * + " FROM INST_CARD_ORDER A WHERE INST_ID='" + instid +
				 * "'  AND MKCK_STATUS='M' " + filtercond +
				 * " ORDER BY ORDER_REF_NO"; enctrace("getInstantOrderList  : "
				 * + listoforderqry);
				 * 
				 * @SuppressWarnings("rawtypes") List listoforder =
				 * jdbctemplate.queryForList(listoforderqry);
				 */

				// by gowtham-300819
				+ " FROM INST_CARD_ORDER A WHERE INST_ID=?  AND MKCK_STATUS=? "
				+ filtercond + " ORDER BY ORDER_REF_NO";
		enctrace("getInstantOrderList  : " + listoforderqry);
		@SuppressWarnings("rawtypes")
		List listoforder = jdbctemplate.queryForList(listoforderqry,
				new Object[] { instid, "M" });

		trace(" get instant order list " + listoforder.size());
		return listoforder;

	}

	public List getInstantOrderViewOnlyList(String instid, String status,
			String filtercond, JdbcTemplate jdbctemplate) throws Exception {
		String statuscond = "";
		if (status != "") {
			statuscond = " AND MKCK_STATUS='" + status + "' ";
		}

		/*
		 * String listoforderqry =
		 * "SELECT ORDER_REF_NO, CARD_TYPE_ID, BIN, PRODUCT_CODE, CARD_QUANTITY, DECODE(MKCK_STATUS,'P','Authorized', 'M','Waiting For Auth', 'D', 'De-Authorized',MKCK_STATUS) as MKCK_STATUS,BRANCH_CODE,  EMBOSSING_NAME, MAKER_ID, to_char(MAKER_DATE, 'dd-mon-yyyy') as ORDERED_DATE FROM INST_CARD_ORDER WHERE INST_ID='"
		 * + instid + "' " + statuscond + " " + filtercond +
		 * " ORDER BY ORDER_REF_NO desc"; // System.out.println(
		 * "getinstatcarorderlist is ____" + listoforderqry );
		 * 
		 * @SuppressWarnings("rawtypes") List listoforder =
		 * jdbctemplate.queryForList(listoforderqry);
		 */

		// by gowtham-270819
		String listoforderqry = "SELECT ORDER_REF_NO, CARD_TYPE_ID, BIN, PRODUCT_CODE, CARD_QUANTITY, DECODE(MKCK_STATUS,'P','Authorized', 'M','Waiting For Auth', 'D', 'De-Authorized',MKCK_STATUS) as MKCK_STATUS,BRANCH_CODE,  EMBOSSING_NAME, MAKER_ID, to_char(MAKER_DATE, 'dd-mon-yyyy') as ORDERED_DATE FROM INST_CARD_ORDER WHERE INST_ID=? "
				+ statuscond + " " + filtercond + " ORDER BY ORDER_REF_NO desc";
		// System.out.println( "getinstatcarorderlist is ____" + listoforderqry
		// );
		@SuppressWarnings("rawtypes")
		List listoforder = jdbctemplate.queryForList(listoforderqry,
				new Object[] { instid });

		@SuppressWarnings("rawtypes")
		ListIterator itr = listoforder.listIterator();
		while (itr.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map temp = (Map) itr.next();
			String bin = (String) temp.get("BIN");
			String usercode = (String) temp.get("MAKER_ID");
			String branchcode = (String) temp.get("BRANCH_CODE");
			String prodcode = (String) temp.get("PRODUCT_CODE");

			// String productdesc = getProductdesc(instid, bin,prodcode);
			String productdesc = getProductdesc(instid, prodcode, jdbctemplate);
			String cardtypedesc = getCardTypeDesc(instid, bin, jdbctemplate);
			String username = getUserName(instid, usercode, jdbctemplate);
			String branchname = getBranchDesc(instid, branchcode, jdbctemplate);
			// trace("desc is " + productdesc + cardtypedesc );

			temp.put("PRODBINDESC", productdesc);
			temp.put("CARDTYPEDESC", cardtypedesc);
			temp.put("USERNAME", username);
			temp.put("BRANCHDESC", branchname);

			// trace( "temp value is " + temp.keySet()) ;
			itr.remove();
			itr.add(temp);
		}

		// trace( " get instant order list " + listoforder );
		return listoforder;

	}

	public List getProductListWithoutJdbc(String instid,
			JdbcTemplate jdbctemplate) {
		String query = "select PRODUCT_CODE,  CARD_TYPE_NAME from PRODUCT_MASTER where INST_ID='"
				+ instid + "'";
		trace(query + " __  get prod list ");
		return (jdbctemplate.queryForList(query));
	}

	public List getProductListView(String instid, JdbcTemplate jdbctemplate,
			HttpSession session) throws Exception {

		String query = "select PRODUCT_CODE, CARD_TYPE_NAME from "
				+ " PRODUCT_MASTER where INST_ID='" + instid
				+ "' AND AUTH_CODE=1 AND DELETED_FLAG !='2'";
		/*
		 * // BY GOWTHAM-210819 String query =
		 * "select PRODUCT_CODE,  CARD_TYPE_NAME from PRODUCT_MASTER where INST_ID=? AND AUTH_CODE=? AND DELETED_FLAG !=? "
		 * ; enctrace(" get prod list : " + query);
		 */

		enctrace("getProductListView ====== ::::::    " + query);
		return (jdbctemplate.queryForList(query));
	}

	public List getProductListViewForSupl(String instid, String productcode,
			JdbcTemplate jdbctemplate, HttpSession session) throws Exception {

		/*
		 * String query =
		 * "select PRODUCT_CODE,  CARD_TYPE_NAME from PRODUCT_MASTER where INST_ID='"
		 * + instid + "' AND PRODUCT_CODE!='" + productcode +
		 * "' AND AUTH_CODE=1 AND DELETED_FLAG !='2'"; enctrace(
		 * " getProductListViewForSupl : " + query); return
		 * (jdbctemplate.queryForList(query));
		 */

		// by gowtham
		String query = "select PRODUCT_CODE,  CARD_TYPE_NAME from PRODUCT_MASTER"
				+ " where INST_ID=? AND PRODUCT_CODE!=? AND AUTH_CODE=? AND DELETED_FLAG !=? ";
		enctrace(" getProductListViewForSupl : " + query);
		return (jdbctemplate.queryForList(query, new Object[] { instid,
				productcode, "1", "2" }));
	}

	public void getSubProductList(JdbcTemplate jdbctemplate) throws IOException {
		String instid = (String) getRequest().getParameter("instid");
		String prodid = (String) getRequest().getParameter("prodid");
		String authstatus = (String) getRequest().getParameter("AUTHSTATUS");
		String query = "select SUB_PRODUCT_NAME, SUB_PROD_ID from INSTPROD_DETAILS where inst_id='"
				+ instid
				+ "' and PRODUCT_CODE='"
				+ prodid
				+ "' AND AUTH_STATUS=1";
		trace(query + " __  get sub prod list ");
		List subprodlist = jdbctemplate.queryForList(query);
		Iterator itr = subprodlist.iterator();
		String result = "<option value='-1'> - select - </option>";

		while (itr.hasNext()) {
			Map map = (Map) itr.next();
			String maxallowedcard = (String) map.get("NO_NONPERCARD_ALLOWED");
			String subprodname = (String) map.get("SUB_PRODUCT_NAME");
			String subprodid = (String) map.get("SUB_PROD_ID");
			result += "<option value='" + subprodid + "'>" + subprodname
					+ "</option>";
			// result = result + max;
		}
		trace("result======>" + result);
		getResponse().getWriter().write(result);
		// return(jdbcTemplate.queryForList(query));
	}

	public List getSubProductList(String instid, String prodid,
			JdbcTemplate jdbctemplate) throws IOException {
		String query = "";
		List subprodlist = null;
		try {

			/*
			 * if (prodid.equals("ALL")) { query =
			 * "select SUB_PRODUCT_NAME, SUB_PROD_ID from INSTPROD_DETAILS where inst_id='"
			 * + instid+ "' and AUTH_STATUS=1 "; } else { query =
			 * "select SUB_PRODUCT_NAME, SUB_PROD_ID from INSTPROD_DETAILS where inst_id='"
			 * + instid+ "' and PRODUCT_CODE='" + prodid +
			 * "' AND AUTH_STATUS=1 "; } enctrace(" __  get sub prod list" +
			 * query + " "); subprodlist = jdbctemplate.queryForList(query);
			 */

			// by gowtham220819
			if (prodid.equals("ALL")) {
				query = "select SUB_PRODUCT_NAME, SUB_PROD_ID from INSTPROD_DETAILS where inst_id=? and AUTH_STATUS=? ";
				subprodlist = jdbctemplate.queryForList(query, new Object[] {
						instid, "1" });
			} else {
				query = "select SUB_PRODUCT_NAME, SUB_PROD_ID from INSTPROD_DETAILS where inst_id=? and PRODUCT_CODE=? AND AUTH_STATUS=? ";
				subprodlist = jdbctemplate.queryForList(query, new Object[] {
						instid, prodid, "1" });
			}
			enctrace(" __  get sub prod list" + query + " ");

		} catch (Exception e) {
			trace("the exception is " + e.getMessage());
		}
		return subprodlist;
	}

	public List getSubProductListForAuth(String instid, String prodid,
			String subprodid, JdbcTemplate jdbctemplate) throws IOException {
		String query = null;
		List subprodlist = null;

		/*
		 * if (prodid.equals("ALL")) { query =
		 * "select SUB_PRODUCT_NAME, SUB_PROD_ID from INSTPROD_DETAILS where inst_id='"
		 * + instid+ "' and AUTH_STATUS=1 "; } else { query =
		 * "select SUB_PRODUCT_NAME, SUB_PROD_ID from INSTPROD_DETAILS where inst_id='"
		 * + instid+ "' and PRODUCT_CODE='" + prodid + "' AND SUB_PROD_ID='" +
		 * subprodid + "' AND AUTH_STATUS=1 "; } enctrace(
		 * " __  get sub prod list" + query + " "); List subprodlist =
		 * jdbctemplate.queryForList(query);
		 */

		// by gowtham-220819
		if (prodid.equals("ALL")) {
			query = "select SUB_PRODUCT_NAME, SUB_PROD_ID from INSTPROD_DETAILS where inst_id=? and AUTH_STATUS=? ";
			subprodlist = jdbctemplate.queryForList(query, new Object[] {
					instid, "1" });
		} else {
			query = "select SUB_PRODUCT_NAME, SUB_PROD_ID from INSTPROD_DETAILS where inst_id=? and PRODUCT_CODE=? AND SUB_PROD_ID=? AND AUTH_STATUS=? ";
			subprodlist = jdbctemplate.queryForList(query, new Object[] {
					instid, prodid, subprodid, "1" });
		}
		enctrace(" __  get sub prod list" + query + " ");

		return subprodlist;
	}

	public String comProductId(String instid, String prodbin,
			JdbcTemplate jdbctemplate) {
		String query = "select CARD_TYPE_ID from PRODUCTINFO where INST_ID='"
				+ instid + "' and BIN='" + prodbin + "' and rownum<=1";
		String temp = (String) jdbctemplate.queryForObject(query, String.class);
		// trace("product id "+temp);
		return temp;
	}

	/*
	 * public String comProdEmbName(String instid, String prodbin, String
	 * subproductid){ String query=
	 * "select SUB_PRODUCT_NAME from INSTPROD_DETAILS where INST_ID='"+instid+
	 * "' and PRODUCT_CODE='"+prodbin+"' and rownum<=1"; trace( query );
	 * JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource); String temp =
	 * null; try { temp =
	 * (String)jdbcTemplate.queryForObject(query,String.class); } catch
	 * (EmptyResultDataAccessException e) { temp = NOREC; e.printStackTrace(); }
	 * trace("product id "+temp); return temp; }
	 */

	public String getIntantEmbName(String instid, String product,
			String subproductid, JdbcTemplate jdbctemplate) {

		/*
		 * String query = "SELECT EMBNAME FROM INST_TEMPNAME WHERE INST_ID='" +
		 * instid + "' AND PRODUCT_CODE='" + product+ "' AND SUBPRODUCT='" +
		 * subproductid + "' AND ROWNUM<=1"; trace("getIntantEmbName" + query);
		 * enctrace("getIntantEmbName" + query); String temp = null; try { temp
		 * = (String) jdbctemplate.queryForObject(query, String.class);
		 */

		// by gowtham-270819
		String query = "SELECT EMBNAME FROM INST_TEMPNAME WHERE INST_ID=? AND PRODUCT_CODE=? AND SUBPRODUCT=? AND ROWNUM<=?";
		trace("getIntantEmbName" + query);
		enctrace("getIntantEmbName" + query);
		String temp = null;
		try {
			temp = (String) jdbctemplate.queryForObject(query, new Object[] {
					instid, product, subproductid, "1" }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return temp;
	}

	public String getIntantEncName(String instid, String product,
			String subproductid, JdbcTemplate jdbctemplate) {

		/*
		 * String query = "SELECT ENCNAME FROM INST_TEMPNAME WHERE INST_ID='" +
		 * instid + "' AND PRODUCT_CODE='" + product+ "' AND SUBPRODUCT='" +
		 * subproductid + "' AND ROWNUM<=1"; enctrace(query); String temp =
		 * null; try { temp = (String) jdbctemplate.queryForObject(query,
		 * String.class);
		 */

		// by gowtham-270819
		String query = "SELECT ENCNAME FROM INST_TEMPNAME WHERE INST_ID=? AND PRODUCT_CODE=? AND SUBPRODUCT=? AND ROWNUM<=?";
		enctrace(query);
		String temp = null;
		try {
			temp = (String) jdbctemplate.queryForObject(query, new Object[] {
					instid, product, subproductid, "1" }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return temp;
	}

	public IfpTransObj myTranObject(DataSource dataSource) {
		PlatformTransactionManager txManager = new DataSourceTransactionManager(
				dataSource);
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);
		IfpTransObj ifptransobj = new IfpTransObj(txManager, status);
		return ifptransobj;
	}

	public IfpTransObj myMerchantTranObject1(DataSource merchdatasource) {
		PlatformTransactionManager merchtxManager = new DataSourceTransactionManager(
				merchdatasource);
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = merchtxManager.getTransaction(def);
		IfpTransObj ifptransobj = new IfpTransObj(merchtxManager, status);
		return ifptransobj;
	}

	/*
	 * public synchronized int updateUserIdSeq(String instid, JdbcTemplate
	 * jdbctemplate) throws Exception { String updqry =
	 * "update SEQUENCE_MASTER SET USERID_SEQ=USERID_SEQ+1 where INST_ID='" +
	 * instid + "'"; trace("updqry : " + updqry);
	 * 
	 * int x = jdbctemplate.update(updqry); trace("x : " + x);
	 * 
	 * 
	 * 
	 * 
	 * return x; }
	 */

	public synchronized int updateUserIdSeq(String instid,
			JdbcTemplate jdbctemplate) throws Exception {

		String usr_seq1 = null;
		String usr_seq = "SELECT USERID_SEQ FROM SEQUENCE_MASTER WHERE inst_ID='"
				+ instid + "'";
		trace("usr_seq ` : " + usr_seq);
		usr_seq1 = (String) jdbctemplate.queryForObject(usr_seq, String.class);
		String updqry = "update SEQUENCE_MASTER SET USERID_SEQ='" + usr_seq1
				+ "'+1 where INST_ID='" + instid + "'";
		trace("updqry : " + updqry);
		int x = jdbctemplate.update(updqry);
		trace("x : " + x);
		return x;
	}

	public synchronized String generateUserIdSeq(String instid,
			JdbcTemplate jdbctemplate) throws Exception {
		String recordid = null;

		try {
			String recordidqry = "SELECT USERID_SEQ FROM SEQUENCE_MASTER WHERE INST_ID='"
					+ instid + "'";
			trace("recordidqry : " + recordidqry);
			recordid = (String) jdbctemplate.queryForObject(recordidqry,
					String.class);
			trace("recordid   --- >  " + recordid);
		} catch (EmptyResultDataAccessException e) {
			// System.out.println("Error: ");
		}
		return recordid;
	}

	public int maxGeneratableCards(String instid, String prodbin,
			JdbcTemplate jdbctemplate) {
		String query = "select NO_NONPERCARD_ALLOWED from INSTPROD_DETAILS where INST_ID='"
				+ instid + "' and BIN='" + prodbin + "' and rownum<=1";

		int temp = (Integer) jdbctemplate.queryForInt(query);
		// trace("no of cards "+temp);
		return temp;
	}

	public String filterCondition(String productcode, String branch,
			String fromdate, String todate, String dateflag) {
		String bincond = "";
		String branchcond = "";

		String datecond = "";
		if ("ALL".equalsIgnoreCase(branch)) {
			branchcond = "";
		} else {
			branchcond = " AND BRANCH_CODE='" + branch + "' ";
		}

		
		if ("ALL".equalsIgnoreCase(productcode)) {
			bincond = "";
		} else {
			bincond = " AND PRODUCT_CODE='" + productcode + "' ";
		}

		if (fromdate != null && todate != null) {
			datecond = "AND ( to_date('" + fromdate + "', 'dd-mm-yyyy') <= "
					+ dateflag + " AND to_date('" + todate
					+ "', 'dd-mm-yyyy' )+1 >= " + dateflag + ") ";
		}

		String filtercond = branchcond+bincond + datecond;
		trace("filter conditing for card issuance" + filtercond);
		return filtercond;

	}
	
	
	//ADDED ON 12-02-2021
	public String filterConditionINST(String productcode, String branch,
			String fromdate, String todate, String dateflag) {
		String bincond = "";
		String branchcond = "";

		String datecond = "";
		if ("ALL".equalsIgnoreCase(branch)) {
			branchcond = "";
		} else {
			branchcond = " AND A.BRANCH_CODE='" + branch + "' ";
		}

		
		if ("ALL".equalsIgnoreCase(productcode)) {
			bincond = "";
		} else {
			bincond = " AND A.PRODUCT_CODE='" + productcode + "' ";
		}

		if (fromdate != null && todate != null) {
			datecond = "AND ( to_date('" + fromdate + "', 'dd-mm-yyyy') <= "
					+ dateflag + " AND to_date('" + todate
					+ "', 'dd-mm-yyyy' )+1 >= " + dateflag + ") ";
		}

		String filtercond = branchcond+bincond + datecond;
		trace("filter conditing for card issuance" + filtercond);
		return filtercond;

	}
	
	

	public String filterConditionWithPreFile(String productcode, String branch,
			String prefilename, String fromdate, String todate, String dateflag) {
		String bincond = "";
		String branchcond = "";

		String datecond = "";

		if ("ALL".equalsIgnoreCase(productcode)) {
			bincond = "";
		} else {
			bincond = " AND PRODUCT_CODE='" + productcode
					+ "' and PRE_FILE = '" + prefilename + "'";
		}

		if ("ALL".equalsIgnoreCase(branch)) {
			branchcond = "";
		} else {
			branchcond = " AND BRANCH_CODE='" + branch + "'";
		}

		if (fromdate != null && todate != null) {
			datecond = "AND ( to_date('" + fromdate + "', 'dd-mm-yyyy') <= "
					+ dateflag + " AND to_date('" + todate
					+ "', 'dd-mm-yyyy' )+1 >= " + dateflag + ")";
		}

		if ("ALL".equalsIgnoreCase(productcode)
				&& "ALL".equalsIgnoreCase(branch)) {
			datecond = " and PRE_FILE = '" + prefilename + "' " + datecond;
			return datecond;
		}
		String filtercond = bincond + branchcond + datecond;

		return filtercond;

	}

	public String comInstId() {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String comUserCode() {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("USERID");
		return instid;
	}

	public List waitingForInstCardGen(String instid, String filtercond,
			JdbcTemplate jdbctemplate) throws Exception {
		String listoforderqry = "SELECT ORDER_REF_NO, CARD_TYPE_ID, BIN, PRODUCT_CODE, CARD_QUANTITY, MKCK_STATUS, BRANCH_CODE,  EMBOSSING_NAME, CHECKER_ID, to_char(MAKER_DATE, 'dd-mon-yyyy') as ORDERED_DATE,"
				+ " (SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID=A.INST_ID and PRODUCT_CODE=A.PRODUCT_CODE) PRODBINDESC,"
				+ " (SELECT PRD_DESC FROM PRODUCTINFO WHERE INST_ID=A.INST_ID and BIN=A.BIN and rownum <= 1) CARDTYPEDESC, "
				+ " (SELECT USERNAME  FROM USER_DETAILS WHERE INSTID=A.INST_ID and USERID=A.MAKER_ID OR USERNAME=A.MAKER_ID and rownum <= 1) USERNAME, "
				+ " (SELECT trim(BRANCH_NAME)  FROM BRANCH_MASTER WHERE INST_ID=A.INST_ID and BRANCH_CODE=A.BRANCH_CODE and rownum <= 1) BRANCHDESC"
				/*
				 * + " FROM INST_CARD_ORDER A WHERE INST_ID='" + instid +
				 * "' AND  ORDER_STATUS='01'  AND MKCK_STATUS='P' " + filtercond
				 * + " ORDER BY ORDER_REF_NO"; enctrace(
				 * "waitingForInstCardGen  is  : " + listoforderqry);
				 * 
				 * @SuppressWarnings("rawtypes") List listoforder =
				 * jdbctemplate.queryForList(listoforderqry);
				 */

				// / by gowtham-300819

				+ " FROM INST_CARD_ORDER A WHERE INST_ID=? AND  ORDER_STATUS=?  AND MKCK_STATUS=? "
				+ filtercond + " ORDER BY ORDER_REF_NO";
		enctrace("waitingForInstCardGen  is  : " + listoforderqry);
		@SuppressWarnings("rawtypes")
		List listoforder = jdbctemplate.queryForList(listoforderqry,
				new Object[] { instid, "01", "P" });
		/*
		 * @SuppressWarnings("rawtypes") ListIterator itr =
		 * listoforder.listIterator(); while( itr.hasNext() ){
		 * 
		 * @SuppressWarnings("rawtypes") Map temp = (Map)itr.next(); String bin
		 * = (String)temp.get("BIN"); String usercode =
		 * (String)temp.get("CHECKER_ID"); String branchcode =
		 * (String)temp.get("BRANCH_CODE"); String prodcode =
		 * (String)temp.get("PRODUCT_CODE"); //String productdesc =
		 * getProductdesc(instid, bin,prodcode); String productdesc =
		 * getProductdesc(instid,prodcode,jdbctemplate); String cardtypedesc =
		 * getCardTypeDesc(instid, bin, jdbctemplate); String username =
		 * getUserName(instid, usercode, jdbctemplate); String branchname =
		 * getBranchDesc(instid, branchcode,jdbctemplate); trace("desc is  " +
		 * productdesc + cardtypedesc ); temp.put("PRODBINDESC", productdesc);
		 * temp.put("CARDTYPEDESC", cardtypedesc); temp.put("USERNAME",
		 * username); temp.put("BRANCHDESC", branchname);
		 * 
		 * trace( "temp value is " + temp.keySet()) ; itr.remove();
		 * itr.add(temp); }
		 */

		trace(" get instant order list " + listoforder);
		return listoforder;
	}

	public List waitingForInstCardProcess(String instid, String card_status,
			String mkckstatus, String filtercond, JdbcTemplate jdbctemplate)
			throws Exception {
		List listoforder = null;
		String cond = "";
		if (card_status.equals("01P")) {

			cond = "AND  PRIVILEGE_CODE='" + card_status + "' ";

		} else {
			cond = "AND  CARD_STATUS='" + card_status + "'";
		}
		try {
			/*
			 * String listoforderqry =
			 * "SELECT distinct ORDER_REF_NO,CARD_NO,MCARD_NO, CARD_TYPE_ID, BIN, PRODUCT_CODE,  MKCK_STATUS, BRANCH_CODE,  EMB_NAME, CHECKER_ID, to_char(MAKER_DATE, 'dd-mon-yyyy') as ORDERED_DATE, MAKER_ID,"
			 * +
			 * " (SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID=A.INST_ID and PRODUCT_CODE=A.PRODUCT_CODE) PRODBINDESC,"
			 * +
			 * " (SELECT BIN_DESC FROM PRODUCTINFO WHERE INST_ID=A.INST_ID and BIN=A.BIN and rownum <= 1) CARDTYPEDESC, "
			 * +
			 * " (SELECT USERNAME  FROM USER_DETAILS WHERE INSTID=A.INST_ID and USERID=A.MAKER_ID OR USERNAME=A.MAKER_ID and rownum <= 1) USERNAME, "
			 * +
			 * " (SELECT trim(BRANCH_NAME)  FROM BRANCH_MASTER WHERE INST_ID=A.INST_ID and BRANCH_CODE=A.BRANCH_CODE and rownum <= 1) BRANCHDESC"
			 * + "  FROM INST_CARD_PROCESS A WHERE INST_ID='" + instid + "' " +
			 * cond + "AND MKCK_STATUS='" + mkckstatus + "' " + filtercond +
			 * " ORDER BY order_ref_no ";
			 */

			String listoforderqry = "SELECT distinct ORDER_REF_NO,ORG_CHN, MCARD_NO,CARD_TYPE_ID, BIN, PRODUCT_CODE,  MKCK_STATUS, BRANCH_CODE,  EMB_NAME, CHECKER_ID, to_char(MAKER_DATE, 'dd-mon-yyyy') as ORDERED_DATE, MAKER_ID,"
					+ " (SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID=A.INST_ID and PRODUCT_CODE=A.PRODUCT_CODE) PRODBINDESC,"
					+ " (SELECT PRD_DESC FROM PRODUCTINFO WHERE INST_ID=A.INST_ID and BIN=A.BIN and rownum <= 1) CARDTYPEDESC, "
					+ " (SELECT USERNAME  FROM USER_DETAILS WHERE INSTID=A.INST_ID and USERID=A.MAKER_ID OR USERNAME=A.MAKER_ID and rownum <= 1) USERNAME, "
					+ " (SELECT trim(BRANCH_NAME)  FROM BRANCH_MASTER WHERE INST_ID=A.INST_ID and BRANCH_CODE=A.BRANCH_CODE and rownum <= 1) BRANCHDESC"
					+ "  FROM INST_CARD_PROCESS A "
					/*
					 * + "WHERE INST_ID='" + instid + "' " + cond +
					 * "AND MKCK_STATUS='" + mkckstatus + "' " + filtercond +
					 * " ORDER BY order_ref_no "; enctrace(
					 * "waitingForInstCardProcess  is ____" + listoforderqry);
					 * listoforder = jdbctemplate.queryForList(listoforderqry);
					 */

					// / by gowtham-280819
					+ "WHERE INST_ID=? "
					+ cond
					+ "AND MKCK_STATUS=? "
					+ filtercond + " ORDER BY order_ref_no ";
			enctrace("waitingForInstCardProcess  is ____" + listoforderqry);
			listoforder = jdbctemplate.queryForList(listoforderqry,
					new Object[] { instid, mkckstatus });

			/*
			 * ListIterator itr = listoforder.listIterator(); while(
			 * itr.hasNext() ){
			 * 
			 * @SuppressWarnings("rawtypes") Map temp = (Map)itr.next(); String
			 * bin = (String)temp.get("BIN"); String usercode =
			 * (String)temp.get("MAKER_ID"); String
			 * card_no=(String)temp.get("CARD_NO"); String
			 * MCARD_NO=(String)temp.get("MCARD_NO"); String branchcode =
			 * (String)temp.get("BRANCH_CODE"); String orderrefnum =
			 * (String)temp.get("ORDER_REF_NO"); String prodcode =
			 * (String)temp.get("PRODUCT_CODE"); //int cardcount =
			 * getNoOfCardsInOrder(instid, orderrefnum, card_status,
			 * "INST_CARD_PROCESS", jdbctemplate); //String productdesc =
			 * getProductdesc(instid, bin,prodcode); String productdesc =
			 * getProductdesc(instid,prodcode,jdbctemplate); String cardtypedesc
			 * = getCardTypeDesc(instid, bin, jdbctemplate); String username =
			 * getUserName(instid, usercode, jdbctemplate); String branchname =
			 * getBranchDesc(instid, branchcode, jdbctemplate); trace(
			 * "desc is  " + productdesc + cardtypedesc );
			 * temp.put("PRODBINDESC", productdesc); temp.put("CARDTYPEDESC",
			 * cardtypedesc); temp.put("USERNAME", username);
			 * temp.put("BRANCHDESC", branchname); //temp.put("CARD_QUANTITY",
			 * cardcount);
			 * 
			 * trace( "temp value is " + temp.keySet()) ; itr.remove();
			 * itr.add(temp); }
			 */

			trace(" get instant order list " + listoforder);
		} catch (Exception e) {
			trace("waitingForInstCardProcess__" + e);
			trace("Exception : " + e.getMessage());
			e.printStackTrace();
		}

		return listoforder;
	}

	public List waitingForInstCardProcesspin(String instid, String card_status,
			String mkckstatus, String filtercond, JdbcTemplate jdbctemplate)
			throws Exception {
		List listoforder = null;
		String cond = "";
		if (card_status.equals("01P")) {

			cond = "AND  PRIVILEGE_CODE='" + card_status + "' ";

		} else {
			cond = "AND  CARD_STATUS='" + card_status + "'";
		}
		try {
			String listoforderqry = "SELECT distinct ORDER_REF_NO,CARD_NO,MCARD_NO, CARD_TYPE_ID, BIN, PRODUCT_CODE,  MKCK_STATUS, BRANCH_CODE,  EMB_NAME, CHECKER_ID, to_char(MAKER_DATE, 'dd-mon-yyyy') as ORDERED_DATE, MAKER_ID,"
					+ " (SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID=A.INST_ID and PRODUCT_CODE=A.PRODUCT_CODE) PRODBINDESC,"
					+ " (SELECT PRD_DESC FROM PRODUCTINFO WHERE INST_ID=A.INST_ID and BIN=A.BIN and rownum <= 1) CARDTYPEDESC, "
					+ " (SELECT USERNAME  FROM USER_DETAILS WHERE INSTID=A.INST_ID and USERID=A.MAKER_ID OR USERNAME=A.MAKER_ID and rownum <= 1) USERNAME, "
					+ " (SELECT trim(BRANCH_NAME)  FROM BRANCH_MASTER WHERE INST_ID=A.INST_ID and BRANCH_CODE=A.BRANCH_CODE and rownum <= 1) BRANCHDESC"
					+ "  FROM INST_CARD_PROCESS A WHERE CAF_REC_STATUS NOT IN('R') AND INST_ID='"
					+ instid
					+ "' "
					+ cond
					+ "AND MKCK_STATUS='"
					+ mkckstatus
					+ "' " + filtercond + " ORDER BY order_ref_no ";
			enctrace("waitingForInstCardProcess  ispin ____" + listoforderqry);
			listoforder = jdbctemplate.queryForList(listoforderqry);

			/*
			 * ListIterator itr = listoforder.listIterator(); while(
			 * itr.hasNext() ){
			 * 
			 * @SuppressWarnings("rawtypes") Map temp = (Map)itr.next(); String
			 * bin = (String)temp.get("BIN"); String usercode =
			 * (String)temp.get("MAKER_ID"); String
			 * card_no=(String)temp.get("CARD_NO"); String
			 * MCARD_NO=(String)temp.get("MCARD_NO"); String branchcode =
			 * (String)temp.get("BRANCH_CODE"); String orderrefnum =
			 * (String)temp.get("ORDER_REF_NO"); String prodcode =
			 * (String)temp.get("PRODUCT_CODE"); //int cardcount =
			 * getNoOfCardsInOrder(instid, orderrefnum, card_status,
			 * "INST_CARD_PROCESS", jdbctemplate); //String productdesc =
			 * getProductdesc(instid, bin,prodcode); String productdesc =
			 * getProductdesc(instid,prodcode,jdbctemplate); String cardtypedesc
			 * = getCardTypeDesc(instid, bin, jdbctemplate); String username =
			 * getUserName(instid, usercode, jdbctemplate); String branchname =
			 * getBranchDesc(instid, branchcode, jdbctemplate); trace(
			 * "desc is  " + productdesc + cardtypedesc );
			 * temp.put("PRODBINDESC", productdesc); temp.put("CARDTYPEDESC",
			 * cardtypedesc); temp.put("USERNAME", username);
			 * temp.put("BRANCHDESC", branchname); //temp.put("CARD_QUANTITY",
			 * cardcount);
			 * 
			 * trace( "temp value is " + temp.keySet()) ; itr.remove();
			 * itr.add(temp); }
			 */

			trace(" get instant order listpin " + listoforder);
		} catch (Exception e) {
			trace("waitingForInstCardProcess__pin" + e);
			trace("Exception : " + e.getMessage());
			e.printStackTrace();
		}

		return listoforder;
	}

	/*
	 * public List waitingForInstCardProcess( String instid, String card_status,
	 * String mkckstatus, String filtercond ) throws Exception { List
	 * listoforder=null; try{ String listoforderqry =
	 * "SELECT distinct ORDER_REF_NO, CARD_TYPE_ID, BIN, PRODUCT_CODE,  MKCK_STATUS, BRANCH_CODE,  EMB_NAME, CHECKER_ID, to_char(MAKER_DATE, 'dd-mon-yyyy') as ORDERED_DATE, MAKER_ID  FROM INST_CARD_PROCESS WHERE INST_ID='"
	 * +instid+"' AND  CARD_STATUS='"+card_status+"'  AND MKCK_STATUS='"
	 * +mkckstatus+"' "+ filtercond +" ORDER BY MKCK_STATUS"; enctrace(
	 * "waitingForInstCardProcess  : " + listoforderqry ); JdbcTemplate
	 * jdbctemplate = new JdbcTemplate(dataSource); listoforder =
	 * jdbctemplate.queryForList(listoforderqry);
	 * 
	 * ListIterator itr = listoforder.listIterator(); while( itr.hasNext() ){
	 * 
	 * @SuppressWarnings("rawtypes") Map temp = (Map)itr.next(); String bin =
	 * (String)temp.get("BIN"); String usercode = (String)temp.get("MAKER_ID");
	 * String branchcode = (String)temp.get("BRANCH_CODE"); String orderrefnum =
	 * (String)temp.get("ORDER_REF_NO"); String prodcode =
	 * (String)temp.get("PRODUCT_CODE"); int cardcount =
	 * getNoOfCardsInOrder(instid, orderrefnum, card_status,
	 * "INST_CARD_PROCESS", jdbctemplate); //String productdesc =
	 * getProductdesc(instid, bin,prodcode); String productdesc =
	 * getProductdesc(instid,prodcode,jdbctemplate); String cardtypedesc =
	 * getCardTypeDesc(instid, bin, jdbctemplate); String username =
	 * getUserName(instid, usercode, jdbctemplate); String branchname =
	 * getBranchDesc(instid, branchcode,jdbctemplate); trace("desc is  " +
	 * productdesc + cardtypedesc ); temp.put("PRODBINDESC", productdesc);
	 * temp.put("CARDTYPEDESC", cardtypedesc); temp.put("USERNAME", username);
	 * temp.put("BRANCHDESC", branchname); temp.put("CARD_QUANTITY", cardcount);
	 * 
	 * trace( "temp value is " + temp.keySet()) ; itr.remove(); itr.add(temp); }
	 * 
	 * trace( " get instant order list " + listoforder ); }catch( Exception e ){
	 * trace( "waitingForInstCardProcess__" +e ); e.printStackTrace(); }
	 * 
	 * return listoforder; }
	 */

	/**
	 * This method is used to get the waiting for Received cards
	 * 
	 * @param instid
	 * @param card_status
	 * @param mkckstatus
	 * @param filtercond
	 * @param jdbctemplate
	 * @return
	 * @throws Exception
	 */
	public List waitingForInstCardRecvList(String instid, String card_status,
			String mkckstatus, String filtercond, JdbcTemplate jdbctemplate)
			throws Exception {
		List listoforder = null;
		try {
			/*
			 * String listoforderqry =
			 * "SELECT CARD_NO, ORDER_REF_NO, CARD_TYPE_ID, BIN, PRODUCT_CODE,  MKCK_STATUS, BRANCH_CODE,  EMB_NAME, CHECKER_ID, to_char(MAKER_DATE, 'dd-mon-yyyy') as ORDERED_DATE, MAKER_ID  FROM INST_CARD_PROCESS WHERE INST_ID='"
			 * + instid + "' AND  CARD_STATUS='" + card_status +
			 * "'  AND MKCK_STATUS='" + mkckstatus + "' " + filtercond +
			 * " ORDER BY order_ref_no";
			 */
			String listoforderqry = "SELECT  ORDER_REF_NO, CARD_TYPE_ID, BIN, PRODUCT_CODE,  MKCK_STATUS, BRANCH_CODE,  EMB_NAME, CHECKER_ID, to_char(MAKER_DATE, 'dd-mon-yyyy') as ORDERED_DATE, MAKER_ID  FROM INST_CARD_PROCESS WHERE INST_ID='"
					+ instid
					+ "' AND  CARD_STATUS='"
					+ card_status
					+ "'  AND MKCK_STATUS='"
					+ mkckstatus
					+ "' "
					+ filtercond
					+ " ORDER BY order_ref_no";
			trace("waitingForInstCardProcess  is ____" + listoforderqry);

			listoforder = jdbctemplate.queryForList(listoforderqry);
			Boolean order = false;
			String ORDER_REF = "";
			String ORDERCNT = "";
			ListIterator itr = listoforder.listIterator();
			while (itr.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map temp = (Map) itr.next();

				String bin = (String) temp.get("BIN");
				String usercode = (String) temp.get("MAKER_ID");
				String branchcode = (String) temp.get("BRANCH_CODE");
				String orderrefnum = (String) temp.get("ORDER_REF_NO");

				if (!ORDER_REF.equals(orderrefnum)) {
					temp.put("ORDER_REF", orderrefnum);
					int cardcount = getNoOfCardsInOrder(instid, orderrefnum,
							card_status, "INST_CARD_PROCESS", jdbctemplate);
					temp.put("CARD_QUANTITY", cardcount);
				}
				ORDER_REF = orderrefnum;
				String prodcode = (String) temp.get("PRODUCT_CODE");

				// String productdesc = getProductdesc(instid, bin,prodcode);
				String productdesc = getProductdesc(instid, prodcode,
						jdbctemplate);
				String cardtypedesc = getCardTypeDesc(instid, bin, jdbctemplate);
				String username = getUserName(instid, usercode, jdbctemplate);
				String branchname = getBranchDesc(instid, branchcode,
						jdbctemplate);
				trace("desc is  " + productdesc + cardtypedesc);
				temp.put("PRODBINDESC", productdesc);
				temp.put("CARDTYPEDESC", cardtypedesc);
				temp.put("USERNAME", username);
				temp.put("BRANCHDESC", branchname);

				trace("temp value is " + temp.keySet());
				itr.remove();
				itr.add(temp);
			}

			trace(" get instant order list " + listoforder);
		} catch (Exception e) {
			trace("waitingForInstCardProcess__" + e);
			e.printStackTrace();
		}
		return listoforder;
	}

	public List waitingForInstCardRecvListForPersonal(String instid,
			String card_status, String mkckstatus, String filtercond,
			JdbcTemplate jdbctemplate) throws Exception {
		List listoforder = null;
		try {
			String listoforderqry = "SELECT CARD_NO, ORDER_REF_NO, CARD_TYPE_ID, BIN, PRODUCT_CODE,  MKCK_STATUS, BRANCH_CODE,  EMB_NAME, CHECKER_ID, to_char(MAKER_DATE, 'dd-mon-yyyy') as ORDERED_DATE, MAKER_ID, CAF_REC_STATUS  FROM PERS_CARD_PROCESS WHERE INST_ID='"
					+ instid
					+ "' AND  CARD_STATUS='"
					+ card_status
					+ "'  AND MKCK_STATUS='"
					+ mkckstatus
					+ "' "
					+ filtercond
					+ " ORDER BY order_ref_no";
			trace("waitingForInstCardProcess  is ____" + listoforderqry);

			listoforder = jdbctemplate.queryForList(listoforderqry);
			Boolean order = false;
			String ORDER_REF = "";
			String ORDERCNT = "";
			ListIterator itr = listoforder.listIterator();
			while (itr.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map temp = (Map) itr.next();

				String bin = (String) temp.get("BIN");
				String usercode = (String) temp.get("MAKER_ID");
				String branchcode = (String) temp.get("BRANCH_CODE");
				String orderrefnum = (String) temp.get("ORDER_REF_NO");

				if (!ORDER_REF.equals(orderrefnum)) {
					temp.put("ORDER_REF", orderrefnum);
					int cardcount = getNoOfCardsInOrder(instid, orderrefnum,
							card_status, "INST_CARD_PROCESS", jdbctemplate);
					temp.put("CARD_QUANTITY", cardcount);
				}
				ORDER_REF = orderrefnum;
				String prodcode = (String) temp.get("PRODUCT_CODE");

				// String productdesc = getProductdesc(instid, bin,prodcode);
				String productdesc = getProductdesc(instid, prodcode,
						jdbctemplate);
				String cardtypedesc = getCardTypeDesc(instid, bin, jdbctemplate);
				String username = getUserName(instid, usercode, jdbctemplate);
				String branchname = getBranchDesc(instid, branchcode,
						jdbctemplate);
				trace("desc is  " + productdesc + cardtypedesc);
				temp.put("PRODBINDESC", productdesc);
				temp.put("CARDTYPEDESC", cardtypedesc);
				temp.put("USERNAME", username);
				temp.put("BRANCHDESC", branchname);

				trace("temp value is " + temp.keySet());
				itr.remove();
				itr.add(temp);
			}

			trace(" get instant order list " + listoforder);
		} catch (Exception e) {
			trace("waitingForInstCardProcess__" + e);
			e.printStackTrace();
		}
		return listoforder;
	}

	public int getNoOfCardsInOrder(String instid, String order_ref_no,
			String card_status, String table, JdbcTemplate jdbctemplate) {
		String query = "select count(*) from " + table + " where INST_ID='"
				+ instid + "' and ORDER_REF_NO='" + order_ref_no
				+ "' AND CARD_STATUS='" + card_status + "'";
		trace("count qry __" + query);
		int temp = (Integer) jdbctemplate.queryForInt(query);
		trace("getNoOfCardsInOrder  " + temp);
		return temp;
	}

	public List getCardsFromOrder(String instid, String orderrefno,
			String ordertype, String mkckstatus, String cardstatus,
			JdbcTemplate jdbctemplate) { // order type is
											// "INSTANT" OR
											// "PERSONAL"
		String tablename = null;
		trace("ordertype----" + ordertype);
		List chnlist = null;
		if (ordertype.equals("INSTANT")) {
			tablename = "INST_CARD_PROCESS";
		} else if (ordertype.equals("PERSONAL")) {
			tablename = "PERS_CARD_PROCESS";
		} else {
			return chnlist;
		}

		/*
		 * String cond = ""; if (cardstatus.equals("01P")) { cond =
		 * "and PRIVILEGE_CODE='" + cardstatus + "' "; } else { cond =
		 * "and CARD_STATUS='" + cardstatus + "' "; } String query =
		 * "select ORG_CHN,BRANCH_CODE,PRODUCT_CODE from " + tablename +
		 * " where INST_ID='" + instid + "' " + cond + " and ORDER_REF_NO='" +
		 * orderrefno + "'  and mkck_status='" + mkckstatus + "'"; trace(
		 * "query CHECK--->" + query); chnlist =
		 * jdbctemplate.queryForList(query);
		 */

		// by gowtham260819

		String cond = "";

		if (cardstatus.equals("01P")) {

			cond = "and PRIVILEGE_CODE='" + cardstatus + "' ";
			String query = "select ORG_CHN,BRANCH_CODE,PRODUCT_CODE from "
					+ tablename
					+ " where INST_ID=? and PRIVILEGE_CODE=? and ORDER_REF_NO=?  and mkck_status=? ";
			trace("query CHECK--->" + query);
			chnlist = jdbctemplate.queryForList(query, new Object[] { instid,
					cardstatus, orderrefno, mkckstatus, });

		} else {
			cond = "and CARD_STATUS='" + cardstatus + "' ";

			String query = "select ORG_CHN,BRANCH_CODE,PRODUCT_CODE from "
					+ tablename
					+ " where INST_ID=? and CARD_STATUS=? and ORDER_REF_NO=?  and mkck_status=?";
			trace("query CHECK--->" + query);
			chnlist = jdbctemplate.queryForList(query, new Object[] { instid,
					cardstatus, orderrefno, mkckstatus });

		}

		trace("chnlist.size()--->" + chnlist.size());
		trace("chnlist value --->" + chnlist);
		// System.out.println("query : " + query);
		return chnlist;

	}

	/*
	 * public String getServiceCode(String instid, String bin, String prodcode,
	 * JdbcTemplate jdbctemplate ) throws Exception { String query=
	 * "select SERVICE_CODE from INSTPROD_DETAILS where INST_ID='"+instid+
	 * "' and BIN='"+bin+"' and PRODUCT_CODE='"+prodcode+"' and rownum<=1";
	 * ////enctrace("3030getServiceCode__  "+query ); String
	 * temp=(String)jdbctemplate.queryForObject(query,String.class); return
	 * temp; }
	 */

	public String getServiceCode(String instid, String bin,
			JdbcTemplate jdbctemplate) throws Exception {

		/*
		 * String query = "select SERVICE_CODE from PRODUCTINFO " +
		 * "where INST_ID='" + instid + "' and BIN='" + bin+ "' and rownum<=1";
		 * enctrace("getServiceCodealkur  " + query); String temp = (String)
		 * jdbctemplate.queryForObject(query, String.class);
		 */

		// by gowtham-220819
		String query = "select SERVICE_CODE from PRODUCTINFO "
				+ "where INST_ID=? and PRD_CODE=? and rownum<=?";
		enctrace("getServiceCodealkur  " + query);
		String temp = (String) jdbctemplate.queryForObject(query, new Object[] {
				instid, bin, "1" }, String.class);

		return temp;
	}

	public String getServiceCodeByChn(String instid, String cardno,
			String processtype, JdbcTemplate jdbctemplate) throws Exception {
		String servicecode = null;
		String tablename = "INST_CARD_PROCESS";
		if (!processtype.equals("INSTANT")) {
			tablename = "PERS_CARD_PROCESS";
		}
		try {

			/*
			 * String query = "SELECT SERVICE_CODE FROM " + tablename +
			 * " WHERE INST_ID='" + instid + "' AND ORG_CHN='"+ cardno +
			 * "' AND ROWNUM<=1"; enctrace("3030getServiceCode__  " + query);
			 * servicecode = (String) jdbctemplate.queryForObject(query,
			 * String.class);
			 */

			// by gowtham-270819
			String query = "SELECT SERVICE_CODE FROM " + tablename
					+ " WHERE INST_ID=? AND ORG_CHN=? AND ROWNUM<=?";
			enctrace("3030getServiceCode__  " + query);
			servicecode = (String) jdbctemplate.queryForObject(query,
					new Object[] { instid, cardno, "1" }, String.class);

		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
		}
		return servicecode;
	}

	public String getProductCode(String instid, String orderrefno,
			JdbcTemplate jdbctemplate) {
		String query = "select PRODUCT_CODE from PERS_CARD_PROCESS   where INST_ID='"
				+ instid
				+ "' AND  ORDER_REF_NO='"
				+ orderrefno
				+ "' and rownum<=1";
		trace("getProductCode__  " + query);

		String temp = (String) jdbctemplate.queryForObject(query, String.class);
		return temp;
	}

	public String getProductCodeByChn(String instid, String padssenable,
			String chn, String tablename, JdbcTemplate jdbctemplate) {
		String query = "";
		/* if (padssenable.equals("Y")) { */

		/*
		 * query = "select PRODUCT_CODE from " + tablename +
		 * "   where INST_ID='" + instid + "' AND  HCARD_NO='" + chn+
		 * "' and rownum<=1";
		 */
		query = "select PRODUCT_CODE from " + tablename + "   where INST_ID='"
				+ instid + "' AND  ORG_CHN='" + chn + "' and rownum<=1";

		/*
		 * } else { query = "select PRODUCT_CODE from " + tablename +
		 * "   where INST_ID='" + instid + "' AND  CARD_NO='" + chn +
		 * "' and rownum<=1"; }
		 */

		trace("getProductCode__  " + query);
		String temp = null;

		try {
			temp = (String) jdbctemplate.queryForObject(query, String.class);
		} catch (DataAccessException e) {
			temp = NOREC;
		}

		return temp;
	}

	public String getSubProductByOrder(String instid, String orderrefno,
			JdbcTemplate jdbctemplate, String processtype) {
		String table = "";
		if (processtype.equals("INSTANT")) {
			table = "INST_CARD_ORDER";
		} else {
			table = "PERS_CARD_ORDER";
		}

		/*
		 * String query = "select SUB_PROD_ID from  " + table +
		 * "   where INST_ID='" + instid + "' AND  ORDER_REF_NO='" + orderrefno
		 * + "' and rownum<=1"; trace("order getProductCode__  " + query);
		 * String temp = null;
		 * 
		 * try { temp = (String) jdbctemplate.queryForObject(query,
		 * String.class);
		 */

		// by gowtham-270819
		String query = "select SUB_PROD_ID from  " + table
				+ "   where INST_ID=? AND  ORDER_REF_NO=? and rownum<=?";
		trace("order getProductCode__  " + query);
		String temp = null;

		try {
			temp = (String) jdbctemplate.queryForObject(query, new Object[] {
					instid, orderrefno, "1" }, String.class);

		} catch (DataAccessException e) {
			temp = NOREC;
		}

		return temp;
	}

	public Personalizeorderdetails gettingInstantorderDeatils(String instid,
			String order_refnum, JdbcTemplate jdbctemplate) {
		Personalizeorderdetails personaloder = null;
		List orderdetailslist;
		String inst_id = "0", order_ref_no = "0", card_type_id = "0", mcardno = "0", sub_prod_id = "0", product_code = "0", card_quantity = "0", embossing_name = "0", encode_data = "0", branch_code = "0", bin = "0", cin = "0", appno = "0", appdate = "NODATE";

		/*
		 * String order_qury =
		 * "SELECT INST_ID, ORDER_REF_NO,CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, CARD_QUANTITY, EMBOSSING_NAME,  ENCODE_DATA,  BRANCH_CODE, BIN FROM INST_CARD_ORDER WHERE INST_ID='"
		 * + instid + "' AND ORDER_REF_NO='" + order_refnum + "'"; trace(
		 * "order_qury__  " + order_qury); orderdetailslist =
		 * jdbctemplate.queryForList(order_qury);
		 */

		// / by gowtham-270819
		String order_qury = "SELECT INST_ID, ORDER_REF_NO,CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, CARD_QUANTITY, EMBOSSING_NAME,  ENCODE_DATA,  BRANCH_CODE, BIN FROM INST_CARD_ORDER WHERE INST_ID=? AND ORDER_REF_NO=?";
		trace("order_qury__  " + order_qury);
		orderdetailslist = jdbctemplate.queryForList(order_qury, new Object[] {
				instid, order_refnum });

		Iterator orderitr = orderdetailslist.iterator();
		while (orderitr.hasNext()) {
			Map ordermap = (Map) orderitr.next();
			inst_id = (String) ordermap.get("INST_ID");
			order_ref_no = (String) ordermap.get("ORDER_REF_NO");
			card_type_id = (String) ordermap.get("CARD_TYPE_ID");
			sub_prod_id = (String) ordermap.get("SUB_PROD_ID");
			product_code = (String) ordermap.get("PRODUCT_CODE");
			card_quantity = (String) ordermap.get("CARD_QUANTITY");
			embossing_name = (String) ordermap.get("EMBOSSING_NAME");
			encode_data = (String) ordermap.get("ENCODE_DATA");
			branch_code = (String) ordermap.get("BRANCH_CODE");
			bin = (String) ordermap.get("BIN");
			mcardno = (String) ordermap.get("MCARD_NO");

			appno = "000000";
		}
		personaloder = new Personalizeorderdetails(inst_id, order_ref_no,
				card_type_id, sub_prod_id, product_code, card_quantity,
				embossing_name, encode_data, branch_code, bin, cin, appno,
				appdate, mcardno);
		return personaloder;
	}

	public String getEncEmbName(String instid, String orderrefno,
			JdbcTemplate jdbctemplate) throws Exception {

		/*
		 * String query =
		 * "select EMBOSSING_NAME from INST_CARD_ORDER  where INST_ID='" +
		 * instid + "' and ORDER_REF_NO='"+ orderrefno + "' and rownum<=1";
		 * String temp = (String) jdbctemplate.queryForObject(query,
		 * String.class);
		 */

		// / by gowtham-270819
		String query = "select EMBOSSING_NAME from INST_CARD_ORDER  where INST_ID=? and ORDER_REF_NO=? and rownum<=1";
		String temp = (String) jdbctemplate.queryForObject(query, new Object[] {
				instid, orderrefno }, String.class);

		// trace("product id "+temp);
		return temp;
	}

	public List getPersonalPREList(String instid, String branch, String bin,
			int showdays, String filetype, JdbcTemplate jdbctemplate) {

		/*
		 * String query=
		 * "select distinct(PRE_NAME),to_char(GENERATED_DATE,'DD-MON-YY') from PERS_PRE_DATA  where INST_ID='"
		 * +instid+"' and PRODUCT_CODE='"+bin+"' and BRANCH_CODE='"+branch+
		 * "'  order by to_char(GENERATED_DATE,'DD-MON-YY')"; trace(
		 * "PRE NAME LIST----> "+query); prelist =
		 * jdbctemplate.queryForList(query);
		 */

		/*
		 * String filetypecond = ""; if (filetype != null) { if
		 * (filetype.equals("$NEW")) { filetypecond = " AND DOWN_CNT=0  "; }
		 * else if (filetype.equals("$EXIST")) { filetypecond =
		 * " AND DOWN_CNT != 0  "; }
		 * 
		 * }
		 * 
		 * String query =
		 * " SELECT DISTINCT PRE_NAME, TO_CHAR(GENERATED_DATE) AS GENDATE FROM PERS_PRE_DATA  WHERE INST_ID='"
		 * + instid + "' AND AUTH_CODE='1' " + filetypecond +
		 * "  AND PRODUCT_CODE='" + bin + "' "; query +=
		 * " AND GENERATED_DATE BETWEEN   SYSDATE-" + showdays +
		 * " AND SYSDATE    ORDER BY TO_DATE(GENDATE) DESC ";
		 * 
		 * enctrace("PRE NAME LIST QRY :  " + query); return
		 * jdbctemplate.queryForList(query);
		 */

		// / added by gowtham-260819
System.out.println("branch-->"+branch);
		String filetypecond = "";
		if (filetype != null) {
			if (filetype.equals("$NEW")) {
				filetypecond = " AND DOWN_CNT=0  ";

				String query = " SELECT DISTINCT PRE_NAME, TO_CHAR(GENERATED_DATE) AS GENDATE FROM PERS_PRE_DATA  WHERE INST_ID='"
						+ instid
						+ "' AND AUTH_CODE='1' AND DOWN_CNT=0 AND PRODUCT_CODE='"
						+ bin + "' AND BRANCH_CODE='"+branch+"' ";
				query += " AND GENERATED_DATE BETWEEN   SYSDATE-" + showdays
						+ " AND SYSDATE    ORDER BY TO_DATE(GENDATE) DESC ";

				enctrace("PRE NAME LIST QRY :  " + query);
				return jdbctemplate.queryForList(query);

			} else if (filetype.equals("$EXIST")) {
				filetypecond = " AND DOWN_CNT != 0  ";

				String query = " SELECT DISTINCT PRE_NAME, TO_CHAR(GENERATED_DATE) AS GENDATE FROM PERS_PRE_DATA  WHERE INST_ID='"
						+ instid
						+ "' AND AUTH_CODE='1' AND DOWN_CNT != 0 AND BRANCH_CODE='"+branch+"' AND PRODUCT_CODE='"
						+ bin + "' ";
				query += " AND GENERATED_DATE BETWEEN   SYSDATE-" + showdays
						+ " AND SYSDATE    ORDER BY TO_DATE(GENDATE) DESC ";

				enctrace("PRE NAME LIST QRY :  " + query);
				return jdbctemplate.queryForList(query);
			}

		}

		String query = " SELECT DISTINCT PRE_NAME, TO_CHAR(GENERATED_DATE) AS GENDATE FROM PERS_PRE_DATA  WHERE INST_ID=? AND AUTH_CODE=? AND BRANCH_CODE=? "
				+ filetypecond + "  AND PRODUCT_CODE=?  ";
		query += " AND GENERATED_DATE BETWEEN   SYSDATE-" + showdays
				+ " AND SYSDATE    ORDER BY TO_DATE(GENDATE) DESC ";

		enctrace("PRE NAME LIST QRY :  " + query);
		return jdbctemplate.queryForList(query,
				new Object[] { instid, "1", bin, branch });

	}

	@SuppressWarnings("unchecked")
	public List<String> getInstPREList(String instid, String productcode,
			int showdays, String filetype, JdbcTemplate jdbctemplate)
			throws Exception {
		// String query="select PRE_NAME from INST_PRE_DATA where
		// INST_ID='"+instid+"' and PRODUCT_CODE='"+productcode+"' group by
		// pre_name";

		String filetypecond = "";
		if (filetype != null) {
			if (filetype.equals("$NEW")) {
				filetypecond = " AND DOWN_CNT=0  ";
			} else if (filetype.equals("$EXIST")) {
				filetypecond = " AND DOWN_CNT != 0  ";
			}

		}

		/*
		 * String query =
		 * " SELECT DISTINCT PRE_NAME, TO_CHAR(GENERATED_DATE) AS GENDATE FROM INST_PRE_DATA"
		 * + "  WHERE INST_ID='"+ instid + "'  " + filetypecond +
		 * "  AND PRODUCT_CODE='" + productcode + "' "; query +=
		 * " AND trunc(GENERATED_DATE) BETWEEN trunc(SYSDATE-" + showdays+
		 * ") AND trunc(SYSDATE)  " + " ORDER BY  TO_DATE(GENDATE) DESC  ";
		 * enctrace("Instant PRE__  " + query); return
		 * jdbctemplate.queryForList(query);
		 */

		// // by gowtham-280819
		String query = " SELECT DISTINCT PRE_NAME, TO_CHAR(GENERATED_DATE) AS GENDATE FROM INST_PRE_DATA"
				+ "  WHERE INST_ID=?  "
				+ filetypecond
				+ "  AND PRODUCT_CODE=? ";
		query += " AND trunc(GENERATED_DATE) BETWEEN trunc(SYSDATE-" + showdays
				+ ") AND trunc(SYSDATE)      "
				+ " ORDER BY  TO_DATE(GENDATE) DESC  ";
		enctrace("Instant PRE FETCH QUERY-->  " + query);
		return jdbctemplate.queryForList(query, new Object[] { instid,
				productcode });

	}

	public List generatePREList(String instid, String productcode,
			String prefilename, String tablename, JdbcTemplate jdbctemplate) {
		String query = "select TRACK_DATA from " + tablename
				+ "  where INST_ID='" + instid + "' and PRODUCT_CODE='"
				+ productcode + "' AND PRE_NAME='" + prefilename + "'";
		trace("order_qury__  " + query);
		return jdbctemplate.queryForList(query);
	}

	public int getPREDisplayDays(String instid, JdbcTemplate jdbctemplate)
			throws Exception {
		int dipdays = 90;
		try {

			/*
			 * String dispqueries =
			 * "SELECT PREDISPLAYDAYS FROM INSTITUTION WHERE INST_ID='" + instid
			 * + "'"; enctrace("dispqueries :  " + dispqueries); dipdays =
			 * jdbctemplate.queryForInt(dispqueries);
			 */

			// by gowtham-260819
			String dispqueries = "SELECT PREDISPLAYDAYS FROM INSTITUTION WHERE INST_ID=?";
			enctrace("dispqueries :  " + dispqueries);
			dipdays = jdbctemplate.queryForInt(dispqueries,
					new Object[] { instid });

			enctrace("dipdays :  " + dipdays);
		} catch (EmptyResultDataAccessException e) {
		}
		return dipdays;
	}

	public int deletePREFiles(String instid, String productcode,
			String prefilename, String tablename, JdbcTemplate jdbctemplate) {
		int delpre_status = -1;

		/*
		 * String predel_query = "select down_cnt from " + tablename +
		 * "  where INST_ID='" + instid+ "' and PRODUCT_CODE='" + productcode +
		 * "' AND PRE_NAME='" + prefilename + "' AND  rownum<=1"; trace(
		 * "PRE DOWN LOAD COUNT ===> " + predel_query); delpre_status =
		 * jdbctemplate.queryForInt(predel_query);
		 */

		// by gowtham-260819
		String predel_query = "select down_cnt from "
				+ tablename
				+ "  where INST_ID=? and PRODUCT_CODE=? AND PRE_NAME=? AND  rownum<=?";
		trace("PRE DOWN LOAD COUNT ===> " + predel_query);
		delpre_status = jdbctemplate.queryForInt(predel_query, new Object[] {
				instid, productcode, prefilename, "1" });

		if (delpre_status > 0) {

			/*
			 * String delqry = "DELETE FROM " + tablename + " where inst_id='" +
			 * instid + "' AND PRODUCT_CODE='"+ productcode + "' AND PRE_NAME='"
			 * + prefilename + "' AND DOWN_CNT <> 0"; trace("delqry " + delqry);
			 * delpre_status = jdbctemplate.update(delqry);
			 */

			// by gowtham-260819
			String delqry = "DELETE FROM "
					+ tablename
					+ " where inst_id=? AND PRODUCT_CODE=? AND PRE_NAME=? AND DOWN_CNT <> ?";
			trace("delqry " + delqry);
			delpre_status = jdbctemplate.update(delqry, new Object[] { instid,
					productcode, prefilename, "0" });

		}
		trace("Delpre Satus====> " + delpre_status);
		return delpre_status;
	}

	/*
	 * public int updPRECntQry(String instid, String productcode, String
	 * prefilename, String tablename, JdbcTemplate jdbctemplate ){ String query=
	 * "select down_cnt from "+ tablename +"  where INST_ID='"+instid+
	 * "' and BIN='"+bin+"' AND PRE_NAME='"+prefilename+"' AND  rownum<=1";
	 * trace( query );
	 * 
	 * String temp=(String)jdbcTemplate.queryForObject(query,String.class); int
	 * newcnt = Integer.parseInt(temp)+1;
	 * 
	 * //String updqry = "UPDATE "+ tablename+
	 * " SET down_cnt=down_cnt+1  where INST_ID='"+instid+"' and BIN='"+bin+
	 * "' AND PRE_NAME='"+prefilename+"'"; String updqry = "UPDATE "+ tablename+
	 * " SET down_cnt=down_cnt+1  where INST_ID='"+instid+"' and PRODUCT_CODE='"
	 * +productcode+"' AND PRE_NAME='"+prefilename+"'"; trace( updqry ); int x =
	 * executeIfpQuery(updqry, jdbctemplate); trace("UPdate Count is ===>"+x);
	 * if( x < 0 ){ return -1; } return x; }
	 */

	public List waitingForInstCardProcessCards(String instid,
			String card_status, String mkckstatus, String filtercond,
			JdbcTemplate jdbctemplate) throws Exception {
		List listoforder = null;
		try {

			/*
			 * String listoforderqry =
			 * "SELECT CARD_NO, ORDER_REF_NO, CARD_TYPE_ID, BIN, PRODUCT_CODE,  MKCK_STATUS,"
			 * +
			 * " BRANCH_CODE,  EMB_NAME, CHECKER_ID, to_char(MAKER_DATE, 'dd-mon-yyyy') as ORDERED_DATE, MAKER_ID "
			 * + " FROM INST_CARD_PROCESS WHERE INST_ID='"+ instid +
			 * "' AND  CARD_STATUS='" + card_status + "'  AND " +
			 * "MKCK_STATUS='" + mkckstatus + "' " + filtercond +
			 * " ORDER BY MKCK_STATUS"; trace(
			 * "waitingForInstCardProcess  is ____" + listoforderqry);
			 * 
			 * listoforder = jdbctemplate.queryForList(listoforderqry);
			 */

			// / by gowtham
			String listoforderqry = "SELECT CARD_NO, ORDER_REF_NO, CARD_TYPE_ID, BIN, PRODUCT_CODE,  MKCK_STATUS,"
					+ " BRANCH_CODE,  EMB_NAME, CHECKER_ID, to_char(MAKER_DATE, 'dd-mon-yyyy') as ORDERED_DATE, MAKER_ID "
					+ " FROM INST_CARD_PROCESS WHERE INST_ID=? AND  CARD_STATUS=?  AND "
					+ "MKCK_STATUS=? " + filtercond + " ORDER BY MKCK_STATUS";
			trace("waitingForInstCardProcess  is ____" + listoforderqry);

			listoforder = jdbctemplate.queryForList(listoforderqry,
					new Object[] { instid, card_status, mkckstatus });

			ListIterator itr = listoforder.listIterator();
			while (itr.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map temp = (Map) itr.next();
				String bin = (String) temp.get("BIN");

				String usercode = (String) temp.get("MAKER_ID");
				String branchcode = (String) temp.get("BRANCH_CODE");
				String orderrefnum = (String) temp.get("ORDER_REF_NO");
				String prodcode = (String) temp.get("PRODUCT_CODE");
				int cardcount = getNoOfCardsInOrder(instid, orderrefnum,
						card_status, "INST_CARD_PROCESS", jdbctemplate);
				// String productdesc = getProductdesc(instid, bin,prodcode);
				String cardtypedesc = getCardTypeDesc(instid, bin, jdbctemplate);
				String productdesc = getProductdesc(instid, prodcode,
						jdbctemplate);
				String username = getUserName(instid, usercode, jdbctemplate);
				String branchname = getBranchDesc(instid, branchcode,
						jdbctemplate);
				trace("desc is  " + productdesc + cardtypedesc);
				temp.put("PRODBINDESC", productdesc);
				temp.put("CARDTYPEDESC", cardtypedesc);
				temp.put("USERNAME", username);
				temp.put("BRANCHDESC", branchname);
				temp.put("CARD_QUANTITY", cardcount);

				trace("temp value is " + temp.keySet());
				itr.remove();
				itr.add(temp);
			}

			trace(" get instant order list " + listoforder);
		} catch (Exception e) {
			trace("waitingForInstCardProcess__" + e);
			e.printStackTrace();
		}

		return listoforder;

	}

	public String getFeeAmount(String instid, String actioncode) {
		return "0";
	}

	public int sendMessage(String message) {
		return 1;
	}

	public String fchCustName(String instid, String custid,
			JdbcTemplate jdbctemplate) throws Exception {
		String custname = null;

		/*
		 * String custnameqry =
		 * "SELECT ( FNAME || ' ' || MNAME || ' '|| LNAME ) AS NAME FROM CUSTOMERINFO WHERE INST_ID='"
		 * + instid + "' AND CIN='" + custid + "' AND ROWNUM<=1"; enctrace(
		 * "custnameqry__ " + custnameqry); try { custname = (String)
		 * jdbctemplate.queryForObject(custnameqry, String.class);
		 */
		// by gowtham-300819
		String custnameqry = "SELECT ( FNAME || ' ' || MNAME || ' '|| LNAME ) AS NAME FROM CUSTOMERINFO WHERE INST_ID=? AND CIN=? AND ROWNUM<=? ";
		enctrace("custnameqry__ " + custnameqry);
		try {
			custname = (String) jdbctemplate.queryForObject(custnameqry,
					new Object[] { instid, custid, "1" }, String.class);

		} catch (EmptyResultDataAccessException e) {
			custname = "--";
		}

		// System.out.println(custname);
		return custname;
	}

	/*
	 * public String fchEmbName(String instid, String hcardno, JdbcTemplate
	 * jdbctemplate) throws Exception { String embName = null; String embNameqry
	 * = "SELECT EMB_NAME FROM CARD_PRODUCTION WHERE INST_ID='" + instid +
	 * "' AND HCARD_NO='" + hcardno + "' and rownum=1"; enctrace("embNameqry " +
	 * embName); try { embName = (String)
	 * jdbctemplate.queryForObject(embNameqry, String.class); } catch
	 * (EmptyResultDataAccessException e) { embName = "--"; } return embName; }
	 */

	public String fchEmbName1(String instid, String ecardno,
			JdbcTemplate jdbctemplate) throws Exception {
		String embName = null;
		String embNameqry = "SELECT EMB_NAME FROM CARD_PRODUCTION WHERE INST_ID='"
				+ instid + "' AND ORG_CHN='" + ecardno + "' and rownum=1";
		enctrace("embNameqry " + embNameqry);
		embName = (String) jdbctemplate
				.queryForObject(embNameqry, String.class);
		trace("emb name checking --" + embName);
		return embName;
	}

	public String getCustomerNameByCard(String instid, String cardno,
			JdbcTemplate jdbctemplate) throws Exception {
		String customername = null;
		String customerid = null;
		try {
			String customernameqry = "SELECT CIN FROM CARD_PRODUCTION WHERE INST_ID='"
					+ instid + "' AND CARD_NO='" + cardno + "'";
			// // enctrace("3030customernameqry :" + customernameqry);
			customerid = (String) jdbctemplate.queryForObject(customernameqry,
					String.class);
			customername = this.fchCustName(instid, customerid, jdbctemplate);
		} catch (Exception e) {
		}
		return customername;
	}

	public Object fchCustDOB(String instid, String custid,
			JdbcTemplate jdbcTemplate) {
		Object dob = null;
		try {
			String custnameqry = "SELECT dob FROM CUSTOMERINFO WHERE INST_ID='"
					+ instid + "' AND CIN='" + custid + "' AND ROWNUM<=1";
			trace("custnameqry__" + custnameqry);
			dob = jdbcTemplate.queryForObject(custnameqry, Object.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return dob;
	}

	public String getCardStatusCode(String instid, String switch_status_code,
			JdbcTemplate jdbcTemplate) {

		String statusqry = "SELECT CARD_ACT_CODE FROM MAINTAIN_DESC WHERE INST_ID='"
				+ instid
				+ "' AND STATUS='"
				+ switch_status_code
				+ "' AND ROWNUM<=1";
		trace("statusqry__***" + statusqry);
		String statusdesc = (String) jdbcTemplate.queryForObject(statusqry,
				String.class);
		return statusdesc;
	}

	public String getCardStatusDesc(String instid, String status_code,
			JdbcTemplate jdbctemplate) throws Exception {
		String statusdesc = null;
		try {
			String statusqry = "SELECT status_desc FROM MAINTAIN_DESC WHERE INST_ID='"
					+ instid
					+ "' AND card_act_code='"
					+ status_code
					+ "' AND ROWNUM<=1";
			// System.out.println( "statusqry__" + statusqry );
			enctrace("statusqry :" + statusqry);
			statusdesc = (String) jdbctemplate.queryForObject(statusqry,
					String.class);
		} catch (EmptyResultDataAccessException e) {
			// System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}

		return statusdesc;
	}

	public String getSwitchCardStatus(String instid, String cardstatus,
			JdbcTemplate jdbctemplate) {
		String switchcardstatus = null;
		try {

			/*
			 * String statusqry =
			 * "SELECT STATUS FROM MAINTAIN_DESC WHERE INST_ID='" + instid +
			 * "' AND card_act_code='"+ cardstatus + "' AND ROWNUM<=1";
			 * enctrace("getSwitchCardStatus" + statusqry); switchcardstatus =
			 * (String) jdbctemplate.queryForObject(statusqry, String.class);
			 */

			// by gowtham220819
			String statusqry = "SELECT STATUS FROM MAINTAIN_DESC WHERE INST_ID=? AND card_act_code=?  AND ROWNUM<=? ";
			enctrace("getSwitchCardStatus" + statusqry);
			switchcardstatus = (String) jdbctemplate.queryForObject(statusqry,
					new Object[] { instid, cardstatus, "1" }, String.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return switchcardstatus;
	}

	public String getCardCurrentStatus(String instid, String padssenable,
			String cardno, String tablename, JdbcTemplate jdbcTemplate)
			throws Exception {
		String statuscode = null, statusqry = null;
		try {
			/*
			 * if (padssenable.equals("Y")) {
			 */
			/*
			 * statusqry =
			 * "select STATUS from ezcardinfo where chn in (select hcard_no from CARD_PRODUCTION) and chn='"
			 * + cardno + "'";
			 */

			// by gowtham-220819
			/*-------	statusqry = "select STATUS from ezcardinfo where chn='" + cardno + "'";-----------*/

			statusqry = "select STATUS from ezcardinfo where chn=? ";

			/*
			 * } else { statusqry =
			 * "select STATUS from ezcardinfo where chn in (select card_no from CARD_PRODUCTION) and chn='"
			 * + cardno + "'"; }
			 */
			// // enctrace("3030statusqry:::"+statusqry);

			// by gowtham-220819
			/*--------statuscode = (String) jdbcTemplate.queryForObject(statusqry, String.class);---------*/

			statuscode = (String) jdbcTemplate.queryForObject(statusqry,
					new Object[] { cardno }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return statuscode;
	}

	// added by senthil 17-dec-2015
	public String getcardmaintaindesc(String instid, String mact,
			JdbcTemplate jdbctemplate) {
		String maintaindesc = null, statuscode = null;
		maintaindesc = "select status_desc from MAINTAIN_DESC where inst_id='"
				+ instid + "' and card_act_code='" + mact + "'";
		// // enctrace("3030maintaindesc:::"+maintaindesc);
		statuscode = (String) jdbctemplate.queryForObject(maintaindesc,
				String.class);
		return statuscode;
	}

	// ended

	public String escSql(String str) {
		if (str == null) {
			return null;
		}
		return StringEscapeUtils.escapeSql(str);
	}

	public String escString(String str) {
		if (str == null) {
			return null;
		}
		return StringEscapeUtils.escapeJava(str);
	}

	public String getProfileName(String instid, String profileid,
			JdbcTemplate jdbcTemplate) {
		String statusqry = "SELECT PROFILE_NAME FROM " + getProfilelistMain()
				+ " WHERE INSTID='" + instid + "' AND PROFILE_ID='" + profileid
				+ "'  AND ROWNUM<=1";
		String statusdesc = (String) jdbcTemplate.queryForObject(statusqry,
				String.class);
		return statusdesc;
	}

	public synchronized int fchAcctSequance(String instid,
			JdbcTemplate jdbctemplate) {
		String statusqry = "SELECT ACCT_SEQ_NO FROM SEQUENCE_MASTER WHERE INST_ID='"
				+ instid + "' AND ROWNUM<=1";
		trace("statusqry__" + statusqry);
		String sequance = (String) jdbctemplate.queryForObject(statusqry,
				String.class);
		int seqno = Integer.parseInt(sequance);
		int newseq = seqno + 1;
		String udpqry = "UPDATE SEQUENCE_MASTER SET ACCT_SEQ_NO='" + newseq
				+ "' WHERE INST_ID='" + instid + "'";
		jdbctemplate.update(udpqry);
		return seqno;
	}

	public int updateAcctSequance(String instid, String newseq,
			JdbcTemplate jdbctemplate) {
		String udpqry = "UPDATE SEQUENCE_MASTER SET ACCT_SEQ_NO='" + newseq
				+ "' WHERE INST_ID='" + instid + "'";
		int x = executeTransaction(udpqry, jdbctemplate);
		return x;
	}

	/*
	 * public String generateAccountNo( String instid, String currentseq,
	 * JdbcTemplate jdbctempldate, HttpSession session){ String statusqry =
	 * "SELECT  ACCT_LEN FROM INSTITUTION WHERE INST_ID='"+instid+
	 * "' AND ROWNUM<=1"; String acctlen
	 * =(String)jdbctempldate.queryForObject(statusqry,String.class); String
	 * acctno = org.apache.commons.lang.StringUtils.leftPad(currentseq,
	 * Integer.parseInt(acctlen), '0') ; return acctno;
	 * 
	 * }
	 */
	public List getConfigDetails(String instid, JdbcTemplate jdbctempldate) {

		/*
		 * String instConfDet =
		 * "SELECT  ACCT_LEN,CIN_LEN,CUSTID_BASEDON FROM INSTITUTION WHERE INST_ID='"
		 * + instid+ "' AND ROWNUM<=1"; List instConfigDetails =
		 * jdbctempldate.queryForList(instConfDet);
		 */

		// by gowtham-210819
		String instConfDet = "SELECT  ACCT_LEN,CIN_LEN,CUSTID_BASEDON FROM INSTITUTION WHERE INST_ID=? AND ROWNUM<=?";
		List instConfigDetails = jdbctempldate.queryForList(instConfDet,
				new Object[] { instid, "1" });

		// // enctrace("3030instConfDet::"+instConfigDetails);

		return instConfigDetails;

	}

	public String getAccountNoLength(String instid, JdbcTemplate jdbctempldate) {
		String instConfigDetails = "";
		try {

			/*
			 * String acctnolengthQ =
			 * "SELECT  ACCT_LEN FROM INSTITUTION WHERE INST_ID='" + instid +
			 * "' AND ROWNUM<=1"; instConfigDetails = (String)
			 * jdbctempldate.queryForObject(acctnolengthQ, String.class);
			 */

			// by gowtham-210819
			String acctnolengthQ = "SELECT  ACCT_LEN FROM INSTITUTION WHERE INST_ID=? AND ROWNUM<=?";
			instConfigDetails = (String) jdbctempldate.queryForObject(
					acctnolengthQ, new Object[] { instid, "1" }, String.class);

			// // enctrace("3030acctnolengthQ::"+acctnolengthQ);
		} catch (Exception e) {
			trace("the exception for getting acctno length:" + e.getMessage());
		}
		return instConfigDetails;

	}

	public String fchSubProduct(String instid, String cardno, String table,
			JdbcTemplate jdbctemplate) {
		String statusqry = "SELECT SUB_PROD_ID FROM " + table
				+ " WHERE INST_ID='" + instid + "' AND CARD_NO='" + cardno
				+ "'  AND ROWNUM<=1";
		String statusdesc = (String) jdbctemplate.queryForObject(statusqry,
				String.class);
		return statusdesc;
	}

	public String fchDefaultCurrency(String instid, String cardno,
			String table_type, JdbcTemplate jdbctemplate) {
		String defcur = NOREC;
		try {

			/*
			 * String defcurqry = "SELECT CARD_CCY FROM " + table_type +
			 * " WHERE INST_ID='" + instid + "' AND CARD_NO='" + cardno +
			 * "'  AND ROWNUM<=1"; trace("defcurqry__" + defcurqry); defcur =
			 * (String) jdbctemplate.queryForObject(defcurqry, String.class);
			 */

			// by gowtham
			String defcurqry = "SELECT CARD_CCY FROM " + table_type
					+ " WHERE INST_ID=? AND CARD_NO=?  AND ROWNUM<=?";
			trace("defcurqry__" + defcurqry);
			defcur = (String) jdbctemplate.queryForObject(defcurqry,
					new Object[] { instid, cardno, "1" }, String.class);

		} catch (EmptyResultDataAccessException e) {
			defcur = NOREC;
		}
		return defcur;
	}

	public String fchDefaultCurrency(String instid, JdbcTemplate jdbctemplate) {
		String defcur = NOREC;
		try {
			String defcurqry = "SELECT BASE_CURRENCY FROM INSTITUTION  WHERE INST_ID='"
					+ instid + "'  AND ROWNUM<=1";
			trace("defcurqry__" + defcurqry);
			defcur = (String) jdbctemplate.queryForObject(defcurqry,
					String.class);
		} catch (EmptyResultDataAccessException e) {
			defcur = NOREC;
		}
		return defcur;
	}

	public String fchCustomerId(String instid, String cardno,
			JdbcTemplate jdbctemplate) {

		/*
		 * String statusqry =
		 * "SELECT CIN FROM INST_CARD_PROCESS WHERE INST_ID='" + instid +
		 * "' AND CARD_NO='" + cardno + "'  AND ROWNUM<=1"; trace("statusqry__"
		 * + statusqry); String statusdesc = (String)
		 * jdbctemplate.queryForObject(statusqry, String.class);
		 */

		// / byg owtham
		String statusqry = "SELECT CIN FROM INST_CARD_PROCESS WHERE INST_ID=? AND CARD_NO=?  AND ROWNUM<=?";
		trace("statusqry__" + statusqry);
		String statusdesc = (String) jdbctemplate.queryForObject(statusqry,
				new Object[] { instid, cardno, "1" }, String.class);

		return statusdesc;
	}

	public String getCurDesc(String curcode, JdbcTemplate jdbctemplate) {

		// by gowtham-200819
		// String statusqry = "SELECT CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE
		// NUMERIC_CODE='" + curcode+ "' OR CURRENCY_CODE='" + curcode + "' AND
		// ROWNUM<=1";
		String statusqry = "SELECT CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE NUMERIC_CODE=? OR CURRENCY_CODE=? AND ROWNUM<=?";

		// System.out.println("currency desc__" + statusqry );
		String statusdesc;
		try {

			// statusdesc = (String) jdbctemplate.queryForObject(statusqry,
			// String.class);
			statusdesc = (String) jdbctemplate.queryForObject(statusqry,
					new Object[] { curcode, curcode, "1" }, String.class);

		} catch (EmptyResultDataAccessException e) {

			statusdesc = NOREC;
			e.printStackTrace();
		}
		return statusdesc;

	}

	public List fchInstCurrencyList(String instid, JdbcTemplate jdbctemplate)
			throws Exception {
		String curqry = "SELECT  CUR_CODE  FROM INSTITUTION_CURRENCY WHERE INST_ID='"
				+ instid + "'";
		// // enctrace("3030 currency query............:"+curqry);
		List curlist = null;
		curlist = jdbctemplate.queryForList(curqry);
		if (!curlist.isEmpty()) {
			ListIterator curitr = curlist.listIterator();
			while (curitr.hasNext()) {
				Map mp = (Map) curitr.next();
				String curcode = (String) mp.get("CUR_CODE");
				String curdesc = (String) getCurDesc(curcode, jdbctemplate);
				mp.put("CUR_DESC", curdesc);
				curitr.remove();
				curitr.add(mp);
			}
		}
		return curlist;
	}

	public RequiredCheck reqCheck() {
		RequiredCheck req = new RequiredCheck();
		return req;
	}

	public String getInstDefaultCurrency(String instid,
			JdbcTemplate jdbctemplate) {
		String basecur = NOREC;
		try {

			/*
			 * String basecurqry =
			 * "SELECT BASE_CURRENCY FROM INSTITUTION WHERE INST_ID='" + instid
			 * + "'  AND ROWNUM<=1"; trace("binqry__" + basecurqry); basecur =
			 * (String) jdbctemplate.queryForObject(basecurqry, String.class);
			 */

			// by gowtham
			String basecurqry = "SELECT BASE_CURRENCY FROM INSTITUTION WHERE INST_ID=?  AND ROWNUM<=? ";
			trace("binqry__" + basecurqry);
			basecur = (String) jdbctemplate.queryForObject(basecurqry,
					new Object[] { instid, "1" }, String.class);

			trace("basecur_" + basecur);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return basecur;
	}

	public String getBin(String instid, String productcode,
			JdbcTemplate jdbctemplate) throws Exception {

		/*
		 * String binqry = "SELECT BIN FROM PRODUCT_MASTER WHERE INST_ID='" +
		 * instid + "' AND PRODUCT_CODE='" + productcode+ "' AND rownum<=1";
		 * enctrace("3030binqry__bin" + binqry); String bin = (String)
		 * jdbctemplate.queryForObject(binqry, String.class);
		 */

		// by gowtham-210819
		String binqry = "SELECT BIN FROM PRODUCT_MASTER WHERE INST_ID=? AND PRODUCT_CODE=? AND rownum<=?";
		enctrace("3030binqry__bin" + binqry);
		String bin = (String) jdbctemplate.queryForObject(binqry, new Object[] {
				instid, productcode, "1" }, String.class);

		return bin;
	}

	public String getService_code(String instid, String bin,
			JdbcTemplate jdbctemplate) throws Exception {
		String product = "SELECT SERVICE_CODE FROM PRODUCTINFO WHERE INST_ID='"
				+ instid + "' AND PRD_CODE='" + bin + "' ";
		enctrace("getting new product based service code");
		trace("getting new product based service code");
		String resultt = (String) jdbctemplate.queryForObject(product,
				String.class);
		trace("service_code in dao file value" + resultt);
		return resultt;
	}

	public String subprod(String instid, String productcode,
			JdbcTemplate jdbctemplate) throws Exception {
		String binqry = "SELECT BIN FROM PRODUCT_MASTER WHERE INST_ID='"
				+ instid + "' AND PRODUCT_CODE='" + productcode
				+ "' AND rownum<=1";
		enctrace("3030binqry__bin" + binqry);
		String bin = (String) jdbctemplate.queryForObject(binqry, String.class);
		return bin;
	}

	public String getBinByCardProduction(String instid, String cardno,
			JdbcTemplate jdbctemplate) throws Exception {
		String bin = null;
		String binqry = "SELECT BIN FROM CARD_PRODUCTION WHERE INST_ID='"
				+ instid + "' AND CARD_NO='" + cardno + "'";
		// // enctrace("3030binqry__"+binqry);
		try {
			bin = (String) jdbctemplate.queryForObject(binqry, String.class);
		} catch (Exception e) {
		}
		return bin;
	}

	public String getCardType(String instid, String productcode,
			JdbcTemplate jdbctemplate) throws Exception {

		/*
		 * String cardtypeqry =
		 * "SELECT CARD_TYPE_ID FROM PRODUCT_MASTER WHERE INST_ID='" + instid +
		 * "' AND PRODUCT_CODE='"+ productcode + "' AND ROWNUM<=1"; ////
		 * enctrace("3030cardtypeqry : " + cardtypeqry); String bin = (String)
		 * jdbctemplate.queryForObject(cardtypeqry, String.class);
		 */

		// by gowtham-210819
		String cardtypeqry = "SELECT CARD_TYPE_ID FROM PRODUCT_MASTER WHERE INST_ID=? AND PRODUCT_CODE=? AND ROWNUM<=?";
		// // enctrace("3030cardtypeqry : " + cardtypeqry);
		String bin = (String) jdbctemplate.queryForObject(cardtypeqry,
				new Object[] { instid, productcode, "1" }, String.class);

		return bin;
	}

	public synchronized String fchSubProductSeq(String instid,
			JdbcTemplate jdbctemplate) {
		int subprodlen = 8;
		String subprodstart = "4";

		// by gowtham
		/*
		 * String subprodqry =
		 * "SELECT SUB_PRODUCT_SEQ FROM SEQUENCE_MASTER WHERE INST_ID='" +
		 * instid + "'  AND ROWNUM<=1"; String subprodqseq = (String)
		 * jdbctemplate.queryForObject(subprodqry, String.class);
		 */

		String subprodqry = "SELECT SUB_PRODUCT_SEQ FROM SEQUENCE_MASTER WHERE INST_ID=?  AND ROWNUM<=? ";
		String subprodqseq = (String) jdbctemplate.queryForObject(subprodqry,
				new Object[] { instid, "1" }, String.class);

		// String subprod = subprodstart+this.paddingZero(subprodqseq,
		// subprodlen-1);
		String subprod = this.paddingZero(subprodqseq, subprodlen);
		trace("THIS IS SUPROUDTC----> " + subprod);

		String updqry = "UPDATE SEQUENCE_MASTER  SET SUB_PRODUCT_SEQ=SUB_PRODUCT_SEQ+1 WHERE INST_ID='"
				+ instid + "' ";
		int x = executeTransaction(updqry, jdbctemplate);

		return subprod;
	}

	public List getProductListView(String instid, JdbcTemplate jdbctemplate)
			throws Exception {
		String query = "";
		try {

			/*
			 * query =
			 * "select PRODUCT_CODE,  CARD_TYPE_NAME from PRODUCT_MASTER where INST_ID='"
			 * + instid+ "' AND AUTH_CODE=1 AND DELETED_FLAG !='2'"; enctrace(
			 * " get prod list : " + query); } catch (Exception e) { trace(
			 * "the exception is" + e.getMessage());} return
			 * (jdbctemplate.queryForList(query));
			 */

			// by gowtham-210819
			query = "select PRODUCT_CODE,  CARD_TYPE_NAME from PRODUCT_MASTER where INST_ID=? AND AUTH_CODE=? AND DELETED_FLAG !=?";
			enctrace(" get prod list : " + query);
		} catch (Exception e) {
			trace("the exception is" + e.getMessage());
		}
		return (jdbctemplate.queryForList(query, new Object[] { instid, "1",
				"2" }));

	}

	public List getProductListCarGen(String instid, JdbcTemplate jdbctemplate)
			throws Exception {

		String query = "select PRODUCT_CODE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER a WHERE a.PRODUCT_CODE=b.PRODUCT_CODE)CARD_TYPE_NAME from PERS_CARD_ORDER b"
				+ " WHERE INST_ID='"
				+ instid
				+ "' AND ORDER_STATUS='01' AND MKCK_STATUS='P' GROUP BY PRODUCT_CODE  ORDER BY PRODUCT_CODE ";
		enctrace(" get prod list : " + query);
		return (jdbctemplate.queryForList(query));

		/*
		 * //by gowtham-220819 String query =
		 * "select PRODUCT_CODE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER a WHERE a.PRODUCT_CODE=b.PRODUCT_CODE)CARD_TYPE_NAME from PERS_CARD_ORDER b"
		 * +
		 * " WHERE INST_ID=? AND ORDER_STATUS=? AND MKCK_STATUS=? GROUP BY PRODUCT_CODE  ORDER BY PRODUCT_CODE "
		 * ; enctrace(" get prod list : " + query); return
		 * (jdbctemplate.queryForList(query,new Object[]{instid,"01","P",}));
		 */

	}

	public List getPREFilenameByBranchAndProduct(String instid,
			String condition, JdbcTemplate jdbctemplate) throws Exception {

		/*
		 * String query =
		 * "select PRE_FILE from PERS_CARD_PROCESS where INST_ID='" + instid +
		 * "'  " + condition+ "   group by PRE_FILE  order by PRE_FILE";
		 * enctrace(" getPREFilenameByBranchAndProduct  : " + query); return
		 * (jdbctemplate.queryForList(query));
		 */

		// / by gowtham-270819
		String query = "select PRE_FILE from PERS_CARD_PROCESS where INST_ID=?  "
				+ condition + "   group by PRE_FILE  order by PRE_FILE";
		enctrace(" getPREFilenameByBranchAndProduct  : " + query);
		return (jdbctemplate.queryForList(query, new Object[] { instid }));

	}

	public List getProductList(String instid, JdbcTemplate jdbctemplate,
			HttpSession session) throws Exception {
		trace("getProductList:::" + session);
		if (session != null) {
			String branchcode = (String) session.getAttribute("BRANCHCODE");

			return getProductList(instid, branchcode, jdbctemplate);
		} else {
			return getProductListView(instid, jdbctemplate);
		}
	}

	public List getProductList(String instid, JdbcTemplate jdbctemplate,
			String branchcode) throws Exception {
		return getProductList(instid, branchcode, jdbctemplate);
	}

	public List getProductList(String instid, String branchcode,
			JdbcTemplate jdbctemplate) throws Exception {
		List prodlist = null;
		try {

			/*
			 * String query =
			 * "SELECT PRODUCT_CODE FROM PRODUCT_MASTER WHERE INST_ID='" +
			 * instid + "' "; enctrace(" get prod list : " + query); prodlist =
			 * jdbctemplate.queryForList(query);
			 */

			// by gowtham-210819
			String query = "SELECT PRODUCT_CODE FROM PRODUCT_MASTER WHERE INST_ID=? ";
			enctrace(" get prod list : " + query);
			prodlist = jdbctemplate
					.queryForList(query, new Object[] { instid });

			if (!prodlist.isEmpty()) {
				ListIterator itr = prodlist.listIterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					String productcode = (String) mp.get("PRODUCT_CODE");
					if (!productcode.equals("$ALL")) {
						String productdesc = this.getProductdesc(instid,
								productcode, jdbctemplate);
						mp.put("CARD_TYPE_NAME", productdesc);
						itr.remove();
						itr.add(mp);
					} else {
						return getProductListView(instid, jdbctemplate);
					}
				}
			} else {
				return getProductListView(instid, jdbctemplate);
			}
		} catch (Exception e) {
			trace("the exception for getting productlist" + e.getMessage());
			e.printStackTrace();

		}
		return prodlist;
	}

	/*
	 * public String fchSubProductSeq( String instid, JdbcTemplate jdbctemplate
	 * ){ int subprodlen = 2; String subprodstart = "4"; String subprodqry =
	 * "SELECT SUB_PRODUCT_SEQ FROM PRODUCT_MASTER WHERE INST_ID='"+instid+
	 * "'  AND ROWNUM<=1"; String subprodqseq
	 * =(String)jdbctemplate.queryForObject(subprodqry,String.class); //String
	 * subprod = subprodstart+this.paddingZero(subprodqseq, subprodlen-1);
	 * String subprod = this.paddingZero(subprodqseq, subprodlen); trace(
	 * "THIS IS SUPROUDTC----> "+subprod);
	 * 
	 * 
	 * 
	 * String updqry =
	 * "UPDATE PRODUCT_MASTER  SET SUB_PRODUCT_SEQ=SUB_PRODUCT_SEQ+1 WHERE INST_ID='"
	 * +instid+"' "; int x = executeTransaction(updqry, jdbctemplate);
	 * 
	 * return subprod; }
	 */

	synchronized String fchCardTypeSequance(String instid,
			JdbcTemplate jdbctemplate) {
		int cardtypelen = 3;
		String cardtyepstart = "1";

		String cardtypeqry = "SELECT CARD_TYPE_SEQ FROM SEQUENCE_MASTER WHERE INST_ID='"
				+ instid + "'  AND ROWNUM<=1";
		String cardtyepseq = (String) jdbctemplate.queryForObject(cardtypeqry,
				String.class);

		String cardtypeid = cardtyepstart
				+ this.paddingZero(cardtyepseq, cardtypelen - 1);

		String updqry = "UPDATE SEQUENCE_MASTER  SET CARD_TYPE_SEQ=CARD_TYPE_SEQ+1 WHERE INST_ID='"
				+ instid + "' ";
		int x = executeTransaction(updqry, jdbctemplate);

		return cardtypeid;
	}

	/*
	 * public int updhSubProductSeq__nouse( String instid, String curseqno){ int
	 * curseq = Integer.parseInt( curseqno ); int nextseq = curseq+1; String
	 * updqry = "UPDATE SEQUENCE_MASTER  SET SUB_PRODUCT_SEQ='"+nextseq+
	 * "' WHERE INST_ID='"+instid+"' "; int x = executeTransaction(updqry,
	 * jdbctemplate); return x; }
	 */

	public String paddingZero(String orignalvalue, int strlen) {
		String val = orignalvalue.toString();

		String formattedresult = org.apache.commons.lang.StringUtils.leftPad(
				val, strlen, '0');
		return formattedresult;

	}

	public static String padRight(String s, int n) {
		return String.format("%1$-" + n + "s", s);
	}

	public static String padLeft(String s, int n) {
		return String.format("%1$" + n + "s", s);
	}

	public String getOrderRefNo(String instid, String padssenable,
			String cardno, String tablename, JdbcTemplate jdbctemplate) {

		String orderrefqry = "";
		/* if (padssenable.equals("Y")) { */

		// orderrefqry = "SELECT ORDER_REF_NO FROM " + tablename + " WHERE
		// INST_ID='" + instid + "' AND HCARD_NO='"+ cardno + "' AND ROWNUM<=1";

		/*
		 * orderrefqry = "SELECT ORDER_REF_NO FROM " + tablename +
		 * " WHERE INST_ID='" + instid +
		 * "' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"
		 * + cardno+ "') AND ROWNUM<=1";
		 */

		// by gowtham-220819

		orderrefqry = "SELECT ORDER_REF_NO FROM "
				+ tablename
				+ " WHERE INST_ID=? AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO=?) AND ROWNUM<=? ";

		/*
		 * } else { orderrefqry = "SELECT ORDER_REF_NO FROM " + tablename +
		 * " WHERE INST_ID='" + instid + "' AND CARD_NO='" + cardno +
		 * "' AND ROWNUM<=1"; }
		 */
		trace("orderrefqry__" + orderrefqry);
		String orderrfno = null;
		try {

			/*
			 * orderrfno = (String) jdbctemplate.queryForObject(orderrefqry,
			 * String.class);
			 */

			// by gowtham-220819
			orderrfno = (String) jdbctemplate.queryForObject(orderrefqry,
					new Object[] { instid, cardno, "1" }, String.class);

			trace("orderrfno__" + orderrfno);
		} catch (EmptyResultDataAccessException e) {
			orderrfno = NOREC;
		}
		return orderrfno;

	}

	public List getAccountDetails(String instid, String padssenable,
			String cardno, String tablename, JdbcTemplate jdbctemplate) {

		List acclist = null;
		String cardproddet = "";

		/* if (padssenable.equals("Y")) { */

		/*
		 * cardproddet =
		 * "SELECT ORDER_REF_NO,ORG_CHN,CIN,LIMIT_ID,EMB_NAME,STATUS_CODE,BRANCH_CODE FROM "
		 * + tablename + " WHERE INST_ID='" + instid+
		 * "' AND ORDER_REF_NO =(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH "
		 * + "WHERE HCARD_NO='" + cardno+ "' AND ROWNUM<=1)";
		 */

		// / by gowtham-300819
		cardproddet = "SELECT ORDER_REF_NO,ORG_CHN,CIN,LIMIT_ID,EMB_NAME,STATUS_CODE,BRANCH_CODE FROM "
				+ tablename
				+ " WHERE INST_ID=? AND ORDER_REF_NO =(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH "
				+ "WHERE HCARD_NO=? AND ROWNUM<=?)";

		/*
		 * } else { cardproddet =
		 * "SELECT ORDER_REF_NO,ORG_CHN,CIN,LIMIT_ID,EMB_NAME,STATUS_CODE,BRANCH_CODE FROM "
		 * + tablename + " WHERE INST_ID='" + instid + "' AND CARD_NO='" +
		 * cardno + "' AND ROWNUM<=1"; }
		 */
		trace("cardproddet" + cardproddet);

		try {

			/* acclist = jdbctemplate.queryForList(cardproddet); */
			// / bygowtham300819
			acclist = jdbctemplate.queryForList(cardproddet, new Object[] {
					instid, cardno, "1" });

		} catch (Exception e) {

		}
		return acclist;

	}

	public String getCustomerid(String instid, String cardno, String tablename,
			JdbcTemplate jdbctemplate) {

		String orderrefqry = "SELECT CIN FROM " + tablename
				+ " WHERE INST_ID='" + instid + "' AND CARD_NO='" + cardno
				+ "' AND ROWNUM<=1";
		trace("binqry__" + orderrefqry);
		String orderrfno = null;
		try {
			orderrfno = (String) jdbctemplate.queryForObject(orderrefqry,
					String.class);
			trace("orderrfno__" + orderrfno);
		} catch (EmptyResultDataAccessException e) {
			orderrfno = NOREC;
		}
		return orderrfno;

	}

	public String getOrderRefNoFromPersProcess(String instid, String cardno,
			JdbcTemplate jdbctemplate) {

		String orderrefqry = "SELECT ORDER_REF_NO FROM PERS_CARD_PROCESS WHERE INST_ID='"
				+ instid + "' AND CARD_NO='" + cardno + "' AND ROWNUM<=1";
		trace("binqry__" + orderrefqry);
		String orderrfno = null;
		try {
			orderrfno = (String) jdbctemplate.queryForObject(orderrefqry,
					String.class);
			trace("orderrfno__" + orderrfno);
		} catch (EmptyResultDataAccessException e) {
			orderrfno = NOREC;
		}
		return orderrfno;

	}

	public String getCurSymbol(String curcode, JdbcTemplate jdbctemplate) {

		/*
		 * String cursymqry =
		 * "SELECT CUR_SYMBOL FROM GLOBAL_CURRENCY WHERE CURRENCY_CODE='" +
		 * curcode + "' OR  NUMERIC_CODE='" + curcode + "' AND ROWNUM<=1";
		 * trace("binqry__" + cursymqry); String cursymb = null; try { cursymb =
		 * (String) jdbctemplate.queryForObject(cursymqry, String.class);
		 */

		// / by gowtham
		String cursymqry = "SELECT CUR_SYMBOL FROM GLOBAL_CURRENCY WHERE CURRENCY_CODE=? OR  NUMERIC_CODE=? AND ROWNUM<=? ";
		trace("binqry__" + cursymqry);
		String cursymb = null;
		try {
			cursymb = (String) jdbctemplate.queryForObject(cursymqry,
					new Object[] { curcode, curcode, "1" }, String.class);

			trace("cursymb__" + cursymb);

		} catch (EmptyResultDataAccessException e) {
			cursymb = NOREC;
		}
		return cursymb;
	}

	public String getFeeCode(String instid, String subprodcode,
			JdbcTemplate jdbctemplate) {

		/*
		 * String feeqry =
		 * "SELECT FEE_CODE FROM INSTPROD_DETAILS WHERE INST_ID='" + instid +
		 * "' AND SUB_PROD_ID='" + subprodcode + "' AND ROWNUM <=1";
		 * trace("binqry__" + feeqry); String feecode = null; try { feecode =
		 * (String) jdbctemplate.queryForObject(feeqry, String.class);
		 */

		// / by gowtham
		String feeqry = "SELECT FEE_CODE FROM INSTPROD_DETAILS WHERE INST_ID=? AND SUB_PROD_ID=? AND ROWNUM <=?";
		trace("binqry__" + feeqry);
		String feecode = null;
		try {
			feecode = (String) jdbctemplate.queryForObject(feeqry,
					new Object[] { instid, subprodcode, "1" }, String.class);

			trace("cursymb__" + feecode);

		} catch (EmptyResultDataAccessException e) {
			feecode = NOREC;
		}
		return feecode;
	}

	public String getFeeAmount(String instid, String actioncode,
			String feecode, JdbcTemplate jdbctemplate) {
		String feeamtqry = "SELECT FEE_AMOUNT FROM FEE_MASTER WHERE INST_ID='"
				+ instid + "' AND FEE_CODE='" + feecode + "' AND FEE_ACTION='"
				+ actioncode + "' AND ROWNUM <=1";
		trace("binqry__" + feeamtqry);
		String fee = null;
		try {
			fee = (String) jdbctemplate.queryForObject(feeamtqry, String.class);
			trace("fee__" + fee);

		} catch (EmptyResultDataAccessException e) {
			fee = NOREC;
		}
		return fee;
	}

	public String getAccountRuleCode(String args, JdbcTemplate jdbctemplate) {
		String acctrule = null;
		try {
			String acctrulecodeqry = "SELECT ACCTRULEID FROM IFP_ACCT_RULE WHERE ( INST_ID||SUBPRODUCT||MSGTYPE||TXNCODE||RESPCODE||ORGINCHANNEL||DEVICETYPE )='"
					+ args + "'";
			trace("acctrulecodeqry__" + acctrulecodeqry);
			acctrule = (String) jdbctemplate.queryForObject(acctrulecodeqry,
					String.class);
		} catch (EmptyResultDataAccessException e) {
			acctrule = "NOREC";
			e.printStackTrace();
		}
		return acctrule;
	}

	public List getListOfAffectingFees(String instid, String acctruleid,
			JdbcTemplate jdbctemplate) {
		List feelist = null;
		try {
			String feelistqry = "SELECT SCH.APP_ACTION FROM IFP_GL_MAPPING MAP, IFP_GL_SCHEME_MASTER SCH WHERE MAP.INST_ID=SCH.INST_ID	"
					+ "AND SCH.GL_COM_ID='FEE' AND MAP.AFFECTING_SCH_CODE=SCH.SCH_CODE AND sch.INST_ID='"
					+ instid + "' AND  ACTION_CODE='" + acctruleid + "' ";
			feelist = jdbctemplate.queryForList(feelistqry);
		} catch (Exception e) {
			feelist = null;
			e.printStackTrace();
		}
		return feelist;
	}

	public String getConversionRate(String instid, String curcode,
			JdbcTemplate jdbctemplate) {
		// String conversionrateqry ="SELECT CURRENT_RATE FROM IFP_CUR_CONVERTER
		// WHERE INST_ID='"+instid+"' AND CURRENCY_CODE='"+curcode+"' AND ROWNUM
		// <=1" ;
		String numeric_cur = this.getCurrencyAlphaCode(curcode, jdbctemplate);
		String conversionrateqry = "SELECT SELLING_RATE FROM IFP_CUR_CONVERTER WHERE INST_ID='"
				+ instid
				+ "' AND CURRENCY_CODE='"
				+ numeric_cur
				+ "' OR CURRENCY_CODE='" + curcode + "'  AND ROWNUM <=1";
		trace("conversionrateqry__" + conversionrateqry);
		String conversionrate = null;
		try {
			conversionrate = (String) jdbctemplate.queryForObject(
					conversionrateqry, String.class);
			trace("conversionrate__" + conversionrate);

		} catch (EmptyResultDataAccessException e) {
			conversionrate = NOREC;
		}
		return conversionrate;
	}

	/************************** pritto ********************************/
	// Pavithra Start $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

	public List limitMasterdesc(JdbcTemplate jdbctemplat, String instid) {
		List limit_masterlist = null;

		/*
		 * String limitmast_qury =
		 * "select ACTION_DESC, TXN_CODE, ACTION_CODE  from ACTIONCODES where LIMIT_FLAG ='1' AND INST_ID='"
		 * + instid + "'"; // IFP_LIMIT_MASTER enctrace(limitmast_qury);
		 * limit_masterlist = jdbctemplat.queryForList(limitmast_qury);
		 */

		// by gowtham-200819
		String limitmast_qury = "select ACTION_DESC, TXN_CODE, ACTION_CODE  from ACTIONCODES where LIMIT_FLAG =? AND INST_ID=?"; // IFP_LIMIT_MASTER
		enctrace(limitmast_qury);
		limit_masterlist = jdbctemplat.queryForList(limitmast_qury,
				new Object[] { "1", instid });

		return limit_masterlist;
	}

	public List getLimitValueDetailsForedit(String instid, String limitid,
			String currencycode, JdbcTemplate jdbctemplate) {
		List limitlist = null;
		StringBuilder limitlistqry = new StringBuilder();

		limitlistqry
				.append("SELECT INSTID, LIMITTYPE, LIMITID,ACTION_DESC, TRIM(TO_CHAR(TXNCODE,'09'))TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, ");
		limitlistqry
				.append("MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, ACCFLAG,TO_CHAR(FROMDATE,'DD-MM-YYYY')FROMDATE,TO_CHAR(TODATE,'DD-MM-YYYY')TODATE ");
		limitlistqry
				.append("FROM  EZLIMITINFO,ACTIONCODES  WHERE TXN_CODE = TXNCODE ");
		limitlistqry.append("AND INST_ID=INSTID AND INSTID='" + instid
				+ "' AND LIMIT_RECID = '" + limitid + "'");

		/*
		 * //by gowtham200819 limitlistqry.append(
		 * "SELECT INSTID, LIMITTYPE, LIMITID,ACTION_DESC, TRIM(TO_CHAR(TXNCODE,'09'))TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, "
		 * ); limitlistqry.append(
		 * "MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, ACCFLAG,TO_CHAR(FROMDATE,'DD-MM-YYYY')FROMDATE,TO_CHAR(TODATE,'DD-MM-YYYY')TODATE "
		 * ); limitlistqry.append(
		 * "FROM  EZLIMITINFO,ACTIONCODES  WHERE TXN_CODE ?");
		 * limitlistqry.append("AND INST_ID=?AND INSTID=? AND LIMIT_RECID =?");
		 */

		// System.out.println("Query :"+limitlistqry);
		enctrace("3030limitlistqry : " + limitlistqry.toString());
		limitlist = jdbctemplate.queryForList(limitlistqry.toString());
		// limitlist = jdbctemplate.queryForList(limitlistqry.toString(),new
		// Object[]{"TXNCODE","INSTID",instid,limitid});
		return limitlist;
	}

	public List FeeMasterdesc(JdbcTemplate jdbctemplat, String instid) {
		List limit_masterlist = null;

		/*
		 * String limitmast_qury =
		 * "select ACTION_DESC, TXN_CODE, ACTION_CODE  from ACTIONCODES where FEE_REQ ='1' AND INST_ID='"
		 * + instid + "'"; // IFP_LIMIT_MASTER enctrace(limitmast_qury);
		 * limit_masterlist = jdbctemplat.queryForList(limitmast_qury);
		 */

		// by gowtham200819
		String limitmast_qury = "select ACTION_DESC, TXN_CODE, ACTION_CODE  from ACTIONCODES where FEE_REQ =? AND INST_ID=?"; // IFP_LIMIT_MASTER
		enctrace(limitmast_qury);
		limit_masterlist = jdbctemplat.queryForList(limitmast_qury,
				new Object[] { "1", instid });

		return limit_masterlist;
	}

	public String getProductByCHN(String instid, String cardno,
			JdbcTemplate jdbctemplate, String processtype) {

		String tablename = "PERS_CARD_PROCESS";
		if (processtype.equals("INSTANT")) {
			tablename = "INST_CARD_PROCESS";
		}

		/*
		 * String prodqry = "SELECT PRODUCT_CODE FROM " + tablename +
		 * " WHERE INST_ID='" + instid + "' AND CARD_NO='" + cardno +
		 * "' AND ROWNUM <=1"; trace("prodqry__" + prodqry); String prodcode =
		 * null; try { prodcode = (String) jdbctemplate.queryForObject(prodqry,
		 * String.class);
		 */

		// / byg owtham
		String prodqry = "SELECT PRODUCT_CODE FROM " + tablename
				+ " WHERE INST_ID=? AND CARD_NO=? AND ROWNUM <=? ";
		trace("prodqry__" + prodqry);
		String prodcode = null;
		try {
			prodcode = (String) jdbctemplate.queryForObject(prodqry,
					new Object[] { instid, cardno, "1" }, String.class);

			trace("prodcode__" + prodcode);

		} catch (EmptyResultDataAccessException e) {
			prodcode = NOREC;
		}
		return prodcode;

	}

	public String getProductBySubProduct(String instid, String subproduct,
			JdbcTemplate jdbctemplate) throws Exception {
		String product = null;
		String productqry = "SELECT SUB_PROD_ID FROM INSTPROD_DETAILS WHERE INST_ID='"
				+ instid + "' AND SUB_PROD_ID='" + subproduct + "' ";
		// // enctrace("3030productqry :" + productqry );
		try {
			product = (String) jdbctemplate.queryForObject(productqry,
					String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return product;
	}

	public String getProductByOrder(String instid, String orderrefno,
			JdbcTemplate jdbctemplate, String processtype) {

		String tablename = "PERS_CARD_ORDER";
		if (processtype.equals("INSTANT")) {
			tablename = "INST_CARD_ORDER";
		}

		String prodqry = "SELECT PRODUCT_CODE FROM " + tablename
				+ " WHERE INST_ID='" + instid + "' AND ORDER_REF_NO='"
				+ orderrefno + "' AND ROWNUM <=1";
		trace("prodqry__" + prodqry);
		String prodcode = null;
		try {
			prodcode = (String) jdbctemplate.queryForObject(prodqry,
					String.class);
			trace("prodcode__" + prodcode);

		} catch (EmptyResultDataAccessException e) {
			prodcode = NOREC;
		}
		return prodcode;

	}

	public String getSubProductByCHNProduction(String instid, String cardno,
			JdbcTemplate jdbctemplate) throws Exception {
		String subprodcode = null;
		String subprodcodeqry = "SELECT SUB_PROD_ID FROM INST_CARD_PROCESS WHERE INST_ID='"
				+ instid + "' AND CARD_NO='" + cardno + "' AND ROWNUM <=1";
		trace("subprodcodeqry__" + subprodcodeqry);
		try {
			subprodcode = (String) jdbctemplate.queryForObject(subprodcodeqry,
					String.class);
			trace("subprodcode__" + subprodcode);

		} catch (EmptyResultDataAccessException e) {
			subprodcode = null;
		}
		return subprodcode;
	}

	public String getSubProductByCHN(String instid, String cardno,
			JdbcTemplate jdbctemplate, String processtype) throws Exception {
		String tablename = "PERS_CARD_PROCESS";

		if (processtype.equals("INSTANT")) {
			tablename = "INST_CARD_PROCESS";
		}

		if (processtype.equals("PERS")) {
			tablename = "PERS_CARD_PROCESS";
		}

		if (processtype.equals("PROD")) {
			tablename = "CARD_PRODUCTION";
		}

		// String subprodcodeqry = "SELECT SUB_PROD_ID FROM " + tablename + "
		// WHERE INST_ID='" + instid + "' AND ORDER_REF_NO IN(SELECT
		// ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"+ cardno +
		// "') AND ROWNUM <=1";

		/*
		 * String subprodcodeqry = "SELECT SUB_PROD_ID FROM " + tablename +
		 * " WHERE INST_ID='" + instid + "' AND ORG_CHN='"+ cardno + "'";
		 * trace("subprodcodeqry__" + subprodcodeqry); String subprodcode =
		 * null; try { subprodcode = (String)
		 * jdbctemplate.queryForObject(subprodcodeqry, String.class);
		 */

		// by gowtham
		String subprodcodeqry = "SELECT SUB_PROD_ID FROM " + tablename
				+ " WHERE INST_ID=? AND ORG_CHN=? ";
		trace("subprodcodeqry__" + subprodcodeqry);
		String subprodcode = null;
		try {
			subprodcode = (String) jdbctemplate.queryForObject(subprodcodeqry,
					new Object[] { instid, cardno }, String.class);

			trace("subprodcode__" + subprodcode);

		} catch (EmptyResultDataAccessException e) {
			subprodcode = NOREC;
		}
		return subprodcode;

	}

	// by siva

	public String getSubProductByCHN11(String instid, String cardno,
			JdbcTemplate jdbctemplate, String processtype) throws Exception {
		String tablename = "PERS_CARD_PROCESS";

		if (processtype.equals("INSTANT")) {
			tablename = "INST_CARD_PROCESS";
		}

		if (processtype.equals("PERS")) {
			tablename = "PERS_CARD_PROCESS";
		}

		if (processtype.equals("PROD")) {
			tablename = "CARD_PRODUCTION";
		}

		/*
		 * String subprodcodeqry = "SELECT SUB_PROD_ID FROM " + tablename + " "
		 * + "WHERE INST_ID='" + instid+
		 * "' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH "
		 * + "WHERE HCARD_NO='" + cardno+ "') AND ROWNUM <=1";
		 * trace("subprodcodeqry__" + subprodcodeqry); String subprodcode =
		 * null; try { subprodcode = (String)
		 * jdbctemplate.queryForObject(subprodcodeqry, String.class);
		 */

		// by gowtham-300819
		String subprodcodeqry = "SELECT SUB_PROD_ID FROM "
				+ tablename
				+ " "
				+ "WHERE INST_ID=? AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH "
				+ "WHERE HCARD_NO=?) AND ROWNUM <=? ";
		trace("subprodcodeqry__" + subprodcodeqry);
		String subprodcode = null;
		try {
			subprodcode = (String) jdbctemplate.queryForObject(subprodcodeqry,
					new Object[] { instid, cardno, "1" }, String.class);

			trace("subprodcode__" + subprodcode);

		} catch (EmptyResultDataAccessException e) {
			subprodcode = NOREC;
		}
		return subprodcode;

	}

	public String getSubProductByHCHN(String instid, String cardno,
			JdbcTemplate jdbctemplate, String processtype) throws Exception {
		String tablename = "PERS_CARD_PROCESS";
		if (processtype.equals("INSTANT")) {
			tablename = "INST_CARD_PROCESS";
		}

		if (processtype.equals("PERS")) {
			tablename = "PERS_CARD_PROCESS";
		}

		if (processtype.equals("PROD")) {
			tablename = "CARD_PRODUCTION";
		}
		String subprodcodeqry = "SELECT SUB_PROD_ID FROM " + tablename
				+ " WHERE INST_ID='" + instid + "' AND HCARD_NO='" + cardno
				+ "' AND ROWNUM <=1";
		trace("subprodcodeqry__" + subprodcodeqry);
		String subprodcode = null;
		try {
			subprodcode = (String) jdbctemplate.queryForObject(subprodcodeqry,
					String.class);
			trace("subprodcode__" + subprodcode);

		} catch (EmptyResultDataAccessException e) {
			subprodcode = NOREC;
		}
		return subprodcode;

	}

	public String getSubProductLimitId(String instid, String sub_prod_id,
			JdbcTemplate jdbctemplate) {
		String limitid_qry = "SELECT LIMIT_ID FROM INSTPROD_DETAILS WHERE INST_ID='"
				+ instid
				+ "' AND SUB_PROD_ID='"
				+ sub_prod_id
				+ "' AND ROWNUM <=1";
		trace("subprodcodeqry__" + limitid_qry);
		String limitid = null;
		try {
			limitid = (String) jdbctemplate.queryForObject(limitid_qry,
					String.class);
			trace("limitid__" + limitid);

		} catch (EmptyResultDataAccessException e) {
			limitid = NOREC;
		}
		return limitid;
	}

	public String getSubProductFeeCode(String instid, String sub_prod_id,
			JdbcTemplate jdbctemplate) {
		String feecode_qry = "SELECT FEE_CODE FROM INSTPROD_DETAILS WHERE INST_ID='"
				+ instid
				+ "' AND SUB_PROD_ID='"
				+ sub_prod_id
				+ "' AND ROWNUM <=1";
		trace("feecode__" + feecode_qry);
		String feecode = null;
		try {
			feecode = (String) jdbctemplate.queryForObject(feecode_qry,
					String.class);
			trace("limitid__" + feecode);

		} catch (EmptyResultDataAccessException e) {
			feecode = NOREC;
		}
		return feecode;
	}

	public String getSubProductCurrencyCode(String instid, String subprodid,
			JdbcTemplate jdbctemplate) {

		/*
		 * String curcodeqry =
		 * "SELECT CARD_CCY FROM INSTPROD_DETAILS WHERE INST_ID='" + instid +
		 * "' AND SUB_PROD_ID='" + subprodid + "' AND ROWNUM <=1";
		 */

		String curcodeqry = "SELECT CARD_CCY FROM INSTPROD_DETAILS WHERE INST_ID=? AND SUB_PROD_ID=? AND ROWNUM <=?";
		trace("curcodeqry__" + curcodeqry);
		String curcode = null;
		try {

			/*
			 * curcode = (String) jdbctemplate.queryForObject(curcodeqry,
			 * String.class);
			 */

			curcode = (String) jdbctemplate.queryForObject(curcodeqry,
					new Object[] { instid, subprodid, "1" }, String.class);

			trace("curcode__" + curcode);

		} catch (EmptyResultDataAccessException e) {
			curcode = NOREC;
		}
		return curcode;
	}

	public String checkPinGenRequired(String instid, String subprodid,
			JdbcTemplate jdbctemplate) {
		String pingenreq_qry = "SELECT PIN_GEN_REQ FROM INSTPROD_DETAILS WHERE INST_ID='"
				+ instid
				+ "' AND SUB_PROD_ID='"
				+ subprodid
				+ "' AND ROWNUM <=1";
		trace("pingenreq_qry" + pingenreq_qry);
		String pingenreq = null;
		try {
			pingenreq = (String) jdbctemplate.queryForObject(pingenreq_qry,
					String.class);
			trace("pingenreq__ :::  " + pingenreq);
			if (pingenreq.equals("-1")) {
				pingenreq = NOREC;
			}

		} catch (EmptyResultDataAccessException e) {
			pingenreq = NOREC;
		}
		return pingenreq;
	}

	public String checkRegisterRequired(String instid, String subprodid,
			JdbcTemplate jdbctemplate) throws Exception {
		String pingenreq_qry = "SELECT CUST_REG_REQ FROM INSTPROD_DETAILS WHERE INST_ID='"
				+ instid
				+ "' AND SUB_PROD_ID='"
				+ subprodid
				+ "' AND ROWNUM <=1";
		// System.out.println("pingenreq_qry"+pingenreq_qry);
		String custregreq = null;
		try {
			custregreq = (String) jdbctemplate.queryForObject(pingenreq_qry,
					String.class);
			trace("pingenreq__" + custregreq);
			if (custregreq.equals("-1")) {
				custregreq = NOREC;
			}

		} catch (EmptyResultDataAccessException e) {
			custregreq = NOREC;
		}
		return custregreq;
	}

	public String checkMaintainenceRequired(String instid, String subprodid,
			JdbcTemplate jdbctemplate) throws Exception {
		String pingenreq_qry = "SELECT MAINTAIN_REQ FROM INSTPROD_DETAILS WHERE INST_ID='"
				+ instid
				+ "' AND SUB_PROD_ID='"
				+ subprodid
				+ "' AND ROWNUM <=1";
		trace("checkMaintainenceRequired" + pingenreq_qry);
		String custregreq = null;
		try {
			custregreq = (String) jdbctemplate.queryForObject(pingenreq_qry,
					String.class);
			trace("checkMaintainenceRequired" + custregreq);
			if (custregreq.equals("-1")) {
				custregreq = NOREC;
			}

		} catch (EmptyResultDataAccessException e) {
			custregreq = NOREC;
		}
		return custregreq;
	}

	public String fchGlSchemeCode(String instid, String subproduct,
			JdbcTemplate jdbctemplate) {

		/*
		 * String glschemeqry =
		 * "SELECT GL_SCHEME_CODE FROM INSTPROD_DETAILS WHERE INST_ID='" +
		 * instid + "' AND SUB_PROD_ID='" + subproduct + "' AND ROWNUM <=1";
		 */

		String glschemeqry = "SELECT GL_SCHEME_CODE FROM INSTPROD_DETAILS WHERE INST_ID=? AND SUB_PROD_ID=? AND ROWNUM <=?";

		trace("pingenreq_qry" + glschemeqry);
		String glscheme = null;
		try {

			/*
			 * glscheme = (String) jdbctemplate.queryForObject(glschemeqry,
			 * String.class);
			 */

			glscheme = (String) jdbctemplate.queryForObject(glschemeqry,
					new Object[] { instid, subproduct, "1" }, String.class);

			trace("glscheme__" + glscheme);
			if (glscheme.equals("-1")) {
				glscheme = NOREC;
			}

		} catch (EmptyResultDataAccessException e) {
			glscheme = NOREC;
		}
		return glscheme;
	}

	public String getAcctRuleId(String instid, String product,
			String subproduct, String msgtype, String respcode, String txncode,
			String orginchannel, String devicetype, JdbcTemplate jdbctemplate) {
		// String acctruleqry = "SELECT ACCTRULEID FROM IFP_ACCT_RULE WHERE
		// INST_ID='' AND "
		return "acctruleid";
	}

	public List getAffectingFeeGlCodes(String instid, String prod_gl_scheme,
			String actioncode, JdbcTemplate jdbctemplate) {
		String qry = "SELECT AFFECTING_SCH_CODE FROM IFP_GL_MAPPING MP, IFP_GL_MASTER GL, IFP_GL_SCHEME_MASTER SCH WHERE MP.INST_ID = GL.INST_ID AND GL.INST_ID=SCH.INST_ID";
		qry += " AND MP.AFFECTING_SCH_CODE=SCH.SCH_CODE AND SCH.GL_CODE=GL.GL_CODE AND GL.GL_COM_ID='FEE' AND MP.SCH_STATUS='1' AND MP.INST_ID='"
				+ instid + "' AND MP.MASTER_SCH_CODE='" + prod_gl_scheme + "'";
		qry += " AND  ACTION_CODE='" + actioncode + "'";
		trace("affecting gl mapping__" + qry);
		List affectschemelist = jdbctemplate.queryForList(qry);
		if (!affectschemelist.isEmpty()) {
			return affectschemelist;
		} else {
			return null;
		}
	}

	public List getGlCodesAll(String instid, JdbcTemplate jdcbtemplate) {
		String qry = "SELECT GL_CODE, GL_NAME FROM IFP_GL_MASTER WHERE INST_ID='"
				+ instid + "'";
		List glcodelist = jdcbtemplate.queryForList(qry);
		return glcodelist;
	}

	public String getSubGlescription(String instid, String subglcode,
			JdbcTemplate jdbctemplate) {

		String subgldescqry = "SELECT SCH_NAME FROM IFP_GL_SCHEME_MASTER WHERE INST_ID=? AND SCH_CODE=?  AND ROWNUM <=?";

		trace("subgldescqry__" + subgldescqry);
		String subgldesc = "NOREC";
		try {

			/*
			 * subgldesc = (String) jdbctemplate.queryForObject(subgldescqry,
			 * String.class);
			 */
			subgldesc = (String) jdbctemplate.queryForObject(subgldescqry,
					new Object[] { instid, subglcode, "1" }, String.class);

			trace("feeactdesc__" + subgldesc);

		} catch (EmptyResultDataAccessException e) {
			subgldesc = "NOREC";
		}
		return subgldesc;
	}

	public synchronized String generateRecordSequance(String instid,
			JdbcTemplate jdbctemplate) throws Exception {
		String recordid = null;
		try {

			/*
			 * String recordidqry =
			 * "SELECT RECORD_SEQ FROM SEQUENCE_MASTER WHERE INST_ID='" + instid
			 * + "'"; //// enctrace("3030recordidqry : " + recordidqry);
			 * recordid = (String) jdbctemplate.queryForObject(recordidqry,
			 * String.class);
			 */

			// by gowtham-210819
			String recordidqry = "SELECT RECORD_SEQ FROM SEQUENCE_MASTER WHERE INST_ID=?";
			// // enctrace("3030recordidqry : " + recordidqry);
			recordid = (String) jdbctemplate.queryForObject(recordidqry,
					new Object[] { instid }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return recordid;
	}

	public synchronized int updateReocrdid(String instid,
			JdbcTemplate jdbctemplate) throws Exception {
		int recordid = -1;
		try {

			/*
			 * String recordidqry =
			 * "UPDATE SEQUENCE_MASTER SET  RECORD_SEQ = RECORD_SEQ+1  WHERE INST_ID='"
			 * + instid+ "'"; //// enctrace("3030recordidqry :" + recordidqry);
			 * recordid = jdbctemplate.update(recordidqry);
			 */

			// / by gowtham-210819
			String recordidqry = "UPDATE SEQUENCE_MASTER SET  RECORD_SEQ = RECORD_SEQ+1  WHERE INST_ID=?";
			// // enctrace("3030recordidqry :" + recordidqry);
			recordid = jdbctemplate
					.update(recordidqry, new Object[] { instid });

		} catch (EmptyResultDataAccessException e) {
		}
		return recordid;
	}

	public String getFeeDescription(String instid, String actioncode,
			JdbcTemplate jdbctemplate) {

		/*
		 * String feedescqry = "SELECT FEE_DESC FROM FEE_DESC WHERE INST_ID='" +
		 * instid + "' AND FEE_CODE='" + actioncode+ "'  AND ROWNUM <=1"; ////
		 * enctrace("3030Query for getting fee desc.........: "+feedescqry); //
		 * trace("feedescqry__"+feedescqry); String feeactdesc = null; try {
		 * feeactdesc = (String) jdbctemplate.queryForObject(feedescqry,
		 * String.class);
		 */

		// by gowtham220819
		String feedescqry = "SELECT FEE_DESC FROM FEE_DESC WHERE INST_ID=? AND FEE_CODE=?  AND ROWNUM <=?";
		// // enctrace("3030Query for getting fee desc.........: "+feedescqry);
		// trace("feedescqry__"+feedescqry);
		String feeactdesc = null;
		try {
			feeactdesc = (String) jdbctemplate.queryForObject(feedescqry,
					new Object[] { instid, actioncode, "1" }, String.class);

			trace("feeactdesc__" + feeactdesc);

		} catch (EmptyResultDataAccessException e) {
			feeactdesc = "NOREC";
		}
		return feeactdesc;
	}

	public String getAffectingFees(String instid, String affgl,
			JdbcTemplate jdbctemplate) {
		String glschemeqry = "SELECT APP_ACTION FROM IFP_GL_SCHEME_MASTER WHERE INST_ID='"
				+ instid + "' AND SCH_CODE='" + affgl + "' AND ROWNUM <=1";
		trace("pingenreq_qry__" + glschemeqry);
		String actfeedesc = null;
		try {
			actfeedesc = (String) jdbctemplate.queryForObject(glschemeqry,
					String.class);
			trace("actfeedesc__" + actfeedesc);
			if (actfeedesc.equals("-1")) {
				actfeedesc = NOREC;
			}

		} catch (EmptyResultDataAccessException e) {
			actfeedesc = NOREC;
		}
		return actfeedesc;
	}

	public int checkCustomerdetailsexsist(String inst_id, String refnum,
			JdbcTemplate jdbcTemplate) {
		int cust_exist = 0;

		/*
		 * String qury =
		 * "select count(*) from IFP_CUSTINFO_PROCESS where INST_ID='" +
		 * inst_id+ "' " +
		 * "and trim(cin)=(select trim(cin) from PERS_CARD_ORDER where INST_ID='"
		 * + inst_id+ "' and " + "ORDER_REF_NO='" + refnum + "')"; trace(
		 * "Count Query===> " + qury); cust_exist =
		 * jdbcTemplate.queryForInt(qury);
		 */

		// by gowtham
		String qury = "select count(*) from IFP_CUSTINFO_PROCESS where INST_ID=? "
				+ "and trim(cin)=(select trim(cin) from PERS_CARD_ORDER where INST_ID=? and "
				+ "ORDER_REF_NO=? )";
		trace("Count Query===> " + qury);
		cust_exist = jdbcTemplate.queryForInt(qury, new Object[] { inst_id,
				inst_id, refnum });

		trace("Custromer Exsist=====> " + cust_exist);
		return cust_exist;
	}

	public List getInstcarddetails(String instid, String refname,
			JdbcTemplate jdbctemplate) throws Exception {
		List listprodcode = null;
		String query = "SELECT BRANCH_CODE,CARD_TYPE_ID,PRODUCT_CODE,ORDER_REF_NO,SUB_PROD_ID,CARD_QUANTITY FROM INST_CARD_ORDER WHERE INST_ID='"
				+ instid + "' AND  ORDER_REF_NO='" + refname + "'";
		trace(query + " __  PRODUCT_CODE ");
		listprodcode = jdbctemplate.queryForList(query);
		if (!(listprodcode.isEmpty())) {
			ListIterator itr = listprodcode.listIterator();
			while (itr.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map temp = (Map) itr.next();
				String branchcode = (String) temp.get("BRANCH_CODE");

				String prodcode = (String) temp.get("PRODUCT_CODE");
				String cardtypeid = (String) temp.get("CARD_TYPE_ID");
				String sub_prod_id = (String) temp.get("SUB_PROD_ID");
				String productname = getProductdesc(instid, prodcode,
						jdbctemplate);
				String sub_productname = getSubproductname(instid, prodcode,
						sub_prod_id, jdbctemplate);

				temp.put("SUBPRODDESC", sub_productname);
				temp.put("PROD_NAME", productname);

				itr.remove();
				itr.add(temp);
			}
		}
		trace("listprodcode===>  " + listprodcode);
		return listprodcode;
	}

	public List getInstantdeleteViewList(String instid, String filtercond,
			JdbcTemplate jdbctemplate) throws Exception {
		String listoforderqry = "SELECT ORDER_REF_NO, CARD_TYPE_ID, BIN, PRODUCT_CODE, CARD_QUANTITY, MKCK_STATUS, BRANCH_CODE,  EMBOSSING_NAME, MAKER_ID, to_char(MAKER_DATE, 'dd-mon-yyyy') as ORDERED_DATE FROM INST_CARD_ORDER WHERE INST_ID='"
				+ instid
				+ "' AND MKCK_STATUS='D' "
				+ filtercond
				+ " ORDER BY ORDER_REF_NO";
		trace("GET DELETE VIEW LIST==> " + listoforderqry);

		@SuppressWarnings("rawtypes")
		List listoforder = jdbctemplate.queryForList(listoforderqry);
		@SuppressWarnings("rawtypes")
		ListIterator itr = listoforder.listIterator();
		while (itr.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map temp = (Map) itr.next();
			String bin = (String) temp.get("BIN");
			String usercode = (String) temp.get("MAKER_ID");
			String branchcode = (String) temp.get("BRANCH_CODE");
			String prodcode = (String) temp.get("PRODUCT_CODE");

			// String productdesc = getProductdesc(instid, bin,prodcode);
			String productdesc = getProductdesc(instid, prodcode, jdbctemplate);
			String cardtypedesc = getCardTypeDesc(instid, bin, jdbctemplate);
			String username = getUserName(instid, usercode, jdbctemplate);
			String branchname = getBranchDesc(instid, branchcode, jdbctemplate);
			// trace("desc is " + productdesc + cardtypedesc );

			temp.put("PRODBINDESC", productdesc);
			temp.put("CARDTYPEDESC", cardtypedesc);
			temp.put("USERNAME", username);
			temp.put("BRANCHDESC", branchname);
			// temp.put("BRANCHCODE",branchcode);

			// trace( "temp value is " + temp.keySet()) ;
			itr.remove();
			itr.add(temp);
		}

		// trace( " get instant order list " + listoforder );
		return listoforder;

	}

	public int deleteInstantorder(String instid, String orderef,
			String filtercond, JdbcTemplate jdbctemplate) {

		String delqry = "DELETE FROM INST_CARD_ORDER WHERE INST_ID='" + instid
				+ "' AND MKCK_STATUS='D' and ORDER_REF_NO='" + orderef + "'"
				+ filtercond + " ORDER BY orderef";

		trace("delqry " + delqry);
		int x = jdbctemplate.update(delqry); // executeIfpQuery(delqry,
												// jdbctemplate);
		return x;

	}

	public List msgtypeList(JdbcTemplate jdbctemplate) {
		String acctmsgtypeqry = "SELECT MSGTYPE  FROM IFP_ACCT_MSGTYPE";
		trace("acctmsgtypeqry__" + acctmsgtypeqry);
		List msgtypelist = null;
		try {
			msgtypelist = (List) jdbctemplate.queryForList(acctmsgtypeqry);
			trace("msgtypelist ___" + msgtypelist);
			if (msgtypelist.equals("-1")) {
				msgtypelist = null;
			}

		} catch (EmptyResultDataAccessException e) {
			msgtypelist = null;
		}
		return msgtypelist;

	}

	public List txncodeList(String instid, JdbcTemplate jdbctemplate) {
		String txncodelistqry = "SELECT TXN_CODE, ACTION_DESC  FROM ACTIONCODES WHERE INST_ID='"
				+ instid + "' AND TXN_FLAG=1";
		trace("txncodelistqry__" + txncodelistqry);
		List txncodelist = null;
		try {
			txncodelist = (List) jdbctemplate.queryForList(txncodelistqry);
			trace("txncodelist ___" + txncodelist);
			if (txncodelist.equals("-1")) {
				txncodelist = null;
			}

		} catch (EmptyResultDataAccessException e) {
			txncodelist = null;
		}
		return txncodelist;

	}

	public List respCodeList(JdbcTemplate jdbctemplate) {
		String respcodeqry = "SELECT RESPCODE FROM IFP_ACCT_RESPCODE ORDER BY RESPCODE";
		trace("respcodeqry__" + respcodeqry);
		List respcodelist = null;
		try {
			respcodelist = (List) jdbctemplate.queryForList(respcodeqry);
			trace("respcodelist ___" + respcodelist);
			if (respcodelist.equals("-1")) {
				respcodelist = null;
			}

		} catch (EmptyResultDataAccessException e) {
			respcodelist = null;
		}
		return respcodelist;

	}

	public List orginChanelList(JdbcTemplate jdbctemplate) {
		String orginchannelqry = "SELECT ORGIN_CHANNEL FROM IFP_ACCT_ORGINCHANNEL ORDER BY ORGIN_CHANNEL";
		trace("orginchannelqry__" + orginchannelqry);
		List orginchannel_list = null;
		try {
			orginchannel_list = (List) jdbctemplate
					.queryForList(orginchannelqry);
			trace("orginchannel_list ___" + orginchannel_list);
			if (orginchannel_list.equals("-1")) {
				orginchannel_list = null;
			}

		} catch (EmptyResultDataAccessException e) {
			orginchannel_list = null;
		}
		return orginchannel_list;
	}

	public int acctRuleExist(String instid, JdbcTemplate jdbctemplate) {
		String rule_exist = "SELECT COUNT(*) FROM IFP_ACCT_RULE WHERE INST_ID='"
				+ instid + "'";
		// // enctrace("3030count query for account rule.....:"+rule_exist);
		int x = jdbctemplate.queryForInt(rule_exist);
		return x;
	}

	public String getCurrencyNumerals(String curalphacode,
			JdbcTemplate jdbctemplate) {

		String curcode = "NOREC";
		try {
			String curcodenumeral = "SELECT NUMERIC_CODE FROM GLOBAL_CURRENCY WHERE  CURRENCY_CODE='"
					+ curalphacode + "' OR NUMERIC_CODE='" + curalphacode + "'";
			trace("curcodenumeral__" + curcodenumeral);
			curcode = (String) jdbctemplate.queryForObject(curcodenumeral,
					String.class);
		} catch (DataAccessException e) {

			e.printStackTrace();
			return curcode;
		}

		return curcode;

	}

	public String getCurrencyAlphaCode(String curalphacode,
			JdbcTemplate jdbctemplate) {
		String curcode = "NOREC";
		try {

			/*
			 * String curcodenumeral =
			 * "SELECT CURRENCY_CODE FROM GLOBAL_CURRENCY WHERE  CURRENCY_CODE='"
			 * + curalphacode + "' OR NUMERIC_CODE='" + curalphacode + "'";
			 */

			// / by gowtham
			String curcodenumeral = "SELECT CURRENCY_CODE FROM GLOBAL_CURRENCY WHERE  CURRENCY_CODE=? OR NUMERIC_CODE=? ";

			// // enctrace("3030curcodenumeral : " + curcodenumeral );

			/*
			 * curcode = (String) jdbctemplate.queryForObject(curcodenumeral,
			 * String.class);
			 */
			curcode = (String) jdbctemplate.queryForObject(curcodenumeral,
					new Object[] { curalphacode, curalphacode }, String.class);

		} catch (DataAccessException e) {

			e.printStackTrace();
			return curcode;
		}

		return curcode;

	}

	public String getTransactionDesc(String instid, String strtxncode,
			JdbcTemplate jdbctemplate) {
		String txndesc = NOREC;
		try {

			String txndescqry = "SELECT ACTION_DESC FROM ACTIONCODES WHERE INST_ID='"
					+ instid
					+ "' and TXN_CODE='"
					+ strtxncode
					+ "' OR ACTION_CODE='" + strtxncode + "'  AND ROWNUM<=1";
			enctrace("3030txndescqry__" + txndescqry);
			txndesc = (String) jdbctemplate.queryForObject(txndescqry,
					String.class);

			System.out.println("instid  ---> " + instid + " txncode----> "
					+ strtxncode);

			/*
			 * //by gowtham String txndescqry =
			 * "SELECT ACTION_DESC FROM ACTIONCODES WHERE INST_ID=? and TXN_CODE=? OR ACTION_CODE=?  AND ROWNUM<=? "
			 * ; enctrace("3030txndescqry__" + txndescqry); txndesc = (String)
			 * jdbctemplate.queryForObject(txndescqry,new
			 * Object[]{instid,strtxncode,strtxncode,"1"}, String.class);
			 */
		} catch (DataAccessException e) {

			e.printStackTrace();
		}
		return txndesc;
	}

	public List getviewglgrplistList(String instid, String glcode,
			JdbcTemplate jdbctemplate) throws Exception {
		String listofglgrpqry = "select GL_COM_ID,CUR_CODE,GROUP_CODE,DECODE(GL_ALIE,'ASRT','ASSERT','LIAB','LIABLITY','INC','INCOME','EXP','EXPENDITURE') AS GLALIE,DECODE(GL_BAL_TYPE,'CR','CREDIT','DR','DEBIT','BT','BOTH') AS GLBALTYPE,GL_CODE,DECODE(GL_ENTRY_ALLOWED,'Y','YES','N','NO') AS GLENTRYALLOWED,GL_NAME,DECODE(GL_POSITION,'BALSHEET','BALANCE SHEET','TRIALBAL','TRIAL BALANCE','PROFLOSS','PROFILE LOSS STATEMENT') AS GLPOSITION,GL_SHORT_NAME,DECODE(GL_STATEMENT_TYPE,'DT','DETAILS','BR','BRIEF') AS GLSTATEMENTTYPE from IFP_GL_MASTER where INST_ID='"
				+ instid + "' and GL_CODE='" + glcode + "'";
		trace("listofglgrpqry is ____" + listofglgrpqry);

		@SuppressWarnings("rawtypes")
		List listglgrpqry = jdbctemplate.queryForList(listofglgrpqry);
		@SuppressWarnings("rawtypes")
		ListIterator listitr = listglgrpqry.listIterator();
		while (listitr.hasNext()) {
			Map temp = (Map) listitr.next();
			String curcode = (String) temp.get("CUR_CODE");
			// System.out.println("curcode--> "+curcode);
			String grpcode = (String) temp.get("GROUP_CODE");
			String curdesc = getCurDesc(curcode, jdbctemplate);
			String grpname = getGrpName(grpcode, jdbctemplate, instid);
			trace("curdesc__" + curdesc);
			temp.put("CUR_DESC", curdesc);
			temp.put("GROUP_NAME", grpname);
			listitr.remove();
			listitr.add(temp);
		}
		return listglgrpqry;
	}

	public String getGrpName(String grpcode, JdbcTemplate jdbctemplate,
			String instid) {
		String statusqry = "SELECT GROUP_NAME FROM IFP_GL_GROUP WHERE GROUP_CODE='"
				+ grpcode + "' AND INST_ID='" + instid + "'";
		trace("GROUP NAME__" + statusqry);
		String statusdesc;
		try {
			statusdesc = (String) jdbctemplate.queryForObject(statusqry,
					String.class);
		} catch (EmptyResultDataAccessException e) {

			statusdesc = NOREC;
			e.printStackTrace();
		}
		return statusdesc;

	}

	public String getTxcCodeByAction(String instid, String actioncode,
			JdbcTemplate jdbctemplate) {

		// / by gowtham
		/*
		 * String statusqry = "SELECT TXN_CODE FROM ACTIONCODES WHERE INST_ID='"
		 * + instid + "' AND ACTION_CODE='"+ actioncode + "'";
		 */
		String statusqry = "SELECT TXN_CODE FROM ACTIONCODES WHERE INST_ID=? AND ACTION_CODE=? ";
		trace("TXN_CODE__" + statusqry);

		String txncode;
		try {

			/*
			 * txncode = (String) jdbctemplate.queryForObject(statusqry,
			 * String.class);
			 */
			txncode = (String) jdbctemplate.queryForObject(statusqry,
					new Object[] { instid, actioncode, }, String.class);

			// System.out.println("txncode---> "+txncode);
		} catch (EmptyResultDataAccessException e) {

			txncode = NOREC;
			e.printStackTrace();
		}
		return txncode;

	}

	public String getActionCodeByTxn(String instid, String actioncode,
			JdbcTemplate jdbctemplate) {
		String statusqry = "SELECT ACTION_CODE FROM ACTIONCODES WHERE INST_ID='"
				+ instid
				+ "' AND TXN_CODE='"
				+ actioncode
				+ "' OR ACTION_CODE='" + actioncode + "' ";
		// // enctrace("3030TXN_CODE__" + statusqry );
		String txncode;
		try {
			txncode = (String) jdbctemplate.queryForObject(statusqry,
					String.class);
		} catch (EmptyResultDataAccessException e) {

			txncode = NOREC;
			e.printStackTrace();
		}
		return txncode;

	}

	public String getYesterday() {
		Date ydate = getDayBefore(new Date());
		DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
		// DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		String yesdate = dateFormat.format(ydate);
		return yesdate;
	}

	public static Date getDayBefore(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -2);
		return cal.getTime();
	}

	public String getDatetime() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
		String today_date = dateFormat.format(date);
		return today_date;
	}

	public String getDate(String format) throws Exception {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat(format);
		String today_date = dateFormat.format(date);
		return today_date;
	}

	public String getDatetimeDDMMYY() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("ddMMyy");
		String today_date = dateFormat.format(date);
		return today_date;
	}

	public String getDateTimeStamp() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_m_ss");
		String today_date = dateFormat.format(date);
		return today_date;
	}

	// by siva 05012019

	public String getDateTimeStamp1() {
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_hhmm");
		String today_date = dateFormat.format(date);
		return today_date;
	}

	// by siva 05012019

	public static long calculateDays(String startDate, String endDate) {
		Date sDate = new Date(startDate);
		Date eDate = new Date(endDate);
		Calendar cal3 = Calendar.getInstance();
		cal3.setTime(sDate);
		Calendar cal4 = Calendar.getInstance();
		cal4.setTime(eDate);
		return daysBetween(cal3, cal4);
	}

	public static long daysBetween(Calendar startDate, Calendar endDate) {
		Calendar date = (Calendar) startDate.clone();
		long daysBetween = 0;
		while (date.before(endDate)) {
			date.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		return daysBetween;
	}

	public String getLogFilePath() {
		Properties prop = getCommonDescProperty();
		String filePath = prop.getProperty("fileName");
		return filePath;
	}

	public String getDecriptedFilePath() {
		Properties prop = getCommonDescProperty();
		String filePath = prop.getProperty("decriptedfilepath");
		return filePath;
	}

	public void printLog(String DebugMessage) {
		try {
			trace(DebugMessage);
		} catch (NullPointerException e) {
		}
	}

	public List listOfActiveMerchants(String instid, JdbcTemplate jdbctemplate) {
		List merchlist = null;
		String query = "select MERCH_NAME,MERCH_ID from EZMMS_MERCHANTPROFILE WHERE INST_ID='"
				+ instid + "'";
		// System.out.println("Query ==> "+query);
		merchlist = jdbctemplate.queryForList(query);
		if (!merchlist.isEmpty()) {
			ListIterator litr = merchlist.listIterator();
			while (litr.hasNext()) {
				Map mp = (Map) litr.next();
				String merchid = (String) mp.get("MERCH_ID");
				String merchname = (String) mp.get("MERCH_NAME");
				String newmerchdesc = merchname + "-" + merchid;
				mp.put("NEWMERCHDESC", newmerchdesc);
				litr.remove();
				litr.add(mp);
			}
		}
		return merchlist;
	}

	public synchronized String generateTxnDisputeId(String instid,
			String tablename, String filed_name, int seqlenght,
			JdbcTemplate jdbcTemplate) {
		String disputeid = "X";
		String dispute_qury = "select trim(" + filed_name
				+ ") as DISPUTE_ID_SEQ from " + tablename + " where INST_ID='"
				+ instid + "'";
		// System.out.println("Query ==> "+dispute_qury);
		String curr_disputeseq = (String) jdbcTemplate.queryForObject(
				dispute_qury, String.class);
		// System.out.println("Dispute Id===> "+disputeid);
		int curr_len = curr_disputeseq.length();
		// System.out.println("Current Legth ==> "+curr_len);
		if (curr_len == seqlenght) {
			disputeid = curr_disputeseq;
		} else {
			int dispute_seq = curr_disputeseq.length();
			// System.out.println(" The Len of Exsist Ref num is "+dispute_seq);
			int newlength;
			while (dispute_seq != seqlenght) {
				for (int j = 0; j < seqlenght; j++) {
					curr_disputeseq = "0" + curr_disputeseq;
					newlength = curr_disputeseq.length();
					if (seqlenght == newlength) {
						break;
					}
				}
				dispute_seq = curr_disputeseq.length();
			}
			disputeid = curr_disputeseq;
		}
		// System.out.println(" Dispute Id ---> "+disputeid);
		return disputeid;
	}

	{

	}

	public List getKeydesc(JdbcTemplate jdbctemplate) {
		List Listgetkeydesc = null;
		String keydesc = "SELECT * FROM EZFMS_KEYDESC";
		// System.out.println(" keydesc " + keydesc);

		if (jdbctemplate.getMaxRows() == 0) {
			Listgetkeydesc = jdbctemplate.queryForList(keydesc);
			return Listgetkeydesc;
		}
		return Listgetkeydesc;
	}

	// Pavithra End $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
	public List selectMerchantlist(String instid, JdbcTemplate merchjdbc) {
		List return_merchnamelist = null;
		String merchdescqry = "select MERCH_ID,MERCH_NAME from EZMMS_MERCHANTPROFILE WHERE  INST_ID='"
				+ instid + "'";
		// System.out.println("merchdescqry___ " + merchdescqry );
		return_merchnamelist = merchjdbc.queryForList(merchdescqry);
		return return_merchnamelist;
	}

	public List selectSchemelist(String instid, JdbcTemplate merchjdbc) {
		List return_schemedescqry = null;
		String schemedescqry = "select BIN,SCHEME_NAME from EZMMS_SCHEMEMASTER";
		return_schemedescqry = merchjdbc.queryForList(schemedescqry);
		return return_schemedescqry;
	}

	public List selectComminisionlist(String instid, JdbcTemplate merchjdbc) {
		List return_commissiondescqry = null;
		try {
			String commissiondescqry = "select COM_MASTERCODE,COM_DESC from EZMMS_COMMISSIONDESC WHERE INST_ID='"
					+ instid + "'";
			// System.out.println("merchdescqry___ " + commissiondescqry );
			return_commissiondescqry = merchjdbc
					.queryForList(commissiondescqry);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return return_commissiondescqry;
	}

	public List selectDiscountlist(String instid, JdbcTemplate merchjdbc) {
		List discountlist = null;
		try {
			String discount_qury = "SELECT DISC_MASTERCODE,DISC_DESC FROM EZMMS_DISCOUNTDESC WHERE INST_ID='"
					+ instid + "'";
			// // enctrace("3030discount_qury : " + discount_qury );
			discountlist = merchjdbc.queryForList(discount_qury);
		} catch (DataAccessException e) {

			e.printStackTrace();
		}
		return discountlist;
	}

	public List selectFeesList(String inst, JdbcTemplate merchjdbc) {
		List feeslist = null;
		String feelist_qury = "SELECT FEE_CODE,FEE_DESC FROM EZMMS_FEE_DESC WHERE INST_ID='"
				+ inst + "'";
		// // enctrace("3030feelist_qury : " + feelist_qury );
		feeslist = merchjdbc.queryForList(feelist_qury);
		return feeslist;
	}

	public List getMerchantStoreList(String instid, String merchid,
			JdbcTemplate merchjdbc) {
		List storeslist = null;
		String storelist_qury = "SELECT STORE_ID,STORE_NAME FROM EZMMS_STOREPROFILE WHERE INST_ID='"
				+ instid
				+ "' AND MERCH_ID='"
				+ merchid
				+ "' AND STORE_STATUS='1' ORDER BY STORE_ID";
		storeslist = merchjdbc.queryForList(storelist_qury);
		return storeslist;
	}

	public DecimalFormat currencyFormatter(String curcode,
			JdbcTemplate jdbctemplate) {
		String curalphacode = this.getCurrencyAlphaCode(curcode, jdbctemplate);
		DecimalFormat d = new DecimalFormat("'" + curalphacode + " '0.00");
		return d;

	}

	public String getCardSchemeName(String instid, String cardscheme,
			JdbcTemplate jdbctemplate) {

		String cardschemename = NOREC;
		try {
			String cardschemeqry = "SELECT SCHEME_DESC FROM EZMMS_SCHEMEMASTER WHERE INST_ID='"
					+ instid + "' AND SCHEME_NAME='" + cardscheme + "'";
			// System.out.println("cardschemeqry--"+cardschemeqry);
			cardschemename = (String) jdbctemplate.queryForObject(
					cardschemeqry, String.class);
		} catch (DataAccessException e) {
			cardschemename = NOREC;
			e.printStackTrace();
		}
		return cardschemename;
	}

	public synchronized String feecodeSequance(String instid,
			JdbcTemplate jdbctemplate) {
		// System.out.println("feecodeSequance");
		int feecodelen = 3;
		String feecodestart = "1";
		String feecodeqry = "SELECT FEE_CODE_SEQ FROM SEQUENCE_MASTER WHERE INST_ID='"
				+ instid + "'  AND ROWNUM<=1";
		// System.out.println("feecodeqry "+feecodeqry);
		String feecodeseq = (String) jdbctemplate.queryForObject(feecodeqry,
				String.class);
		String feecode = feecodestart
				+ this.paddingZero(feecodeseq, feecodelen - 1);
		// System.out.println("feecode "+feecode);
		String updqry = "UPDATE SEQUENCE_MASTER  SET FEE_CODE_SEQ=FEE_CODE_SEQ+1 WHERE INST_ID='"
				+ instid + "' ";
		// System.out.println("updqry "+updqry);
		int x = executeTransaction(updqry, jdbctemplate);

		return feecode;
	}

	public synchronized String subfeecodeSequance(String instid,
			JdbcTemplate jdbctemplate) {

		// System.out.println("SUBFEECODESequance");
		int subfeecodelen = 3;
		String subfeecodestart = "2";
		String subfeecodeqry = "SELECT FEE_SUBCODE_SEQ FROM SEQUENCE_MASTER WHERE INST_ID='"
				+ instid + "'  AND ROWNUM<=1";
		// System.out.println("subfeecodeqry");
		String subfeecodeseq = (String) jdbctemplate.queryForObject(
				subfeecodeqry, String.class);
		// System.out.println("subfeecodeseq");
		String subfeecode = subfeecodestart
				+ this.paddingZero(subfeecodeseq, subfeecodelen - 1);
		// System.out.println("subfeecode");
		String updqry = "UPDATE SEQUENCE_MASTER  SET FEE_SUBCODE_SEQ=FEE_SUBCODE_SEQ+1 WHERE INST_ID='"
				+ instid + "' ";
		// System.out.println("updqry");
		int x = executeTransaction(updqry, jdbctemplate);

		return subfeecode;
	}

	public synchronized String commisioncodeSequance(String instid,
			JdbcTemplate jdbctemplate) {
		// System.out.println("feecodeSequance");
		int commisioncodelen = 3;
		String commisioncodestart = "1";
		String commisioncodeqry = "SELECT COMMISSION_CODE_SEQ FROM SEQUENCE_MASTER WHERE INST_ID='"
				+ instid + "'  AND ROWNUM<=1";
		// System.out.println("commisioncodeqry "+commisioncodeqry);
		String commisioncodeseq = (String) jdbctemplate.queryForObject(
				commisioncodeqry, String.class);
		String commisioncode = commisioncodestart
				+ this.paddingZero(commisioncodeseq, commisioncodelen - 1);
		// System.out.println("commisioncode "+commisioncode);
		String updqry = "UPDATE SEQUENCE_MASTER  SET COMMISSION_CODE_SEQ=COMMISSION_CODE_SEQ+1 WHERE INST_ID='"
				+ instid + "' ";
		// System.out.println("updqry sdsdsdsd "+updqry);
		int x = jdbctemplate.update(updqry);

		return commisioncode;
	}

	public synchronized String subcommisioncodeSequance(String instid,
			JdbcTemplate jdbctemplate) {

		// System.out.println("SUBFEECODESequance");
		int subcommisioncodelen = 3;
		String subcommisioncodestart = "2";
		String subcommisioncodeqry = "SELECT COMMISSION_SUBCODE_SEQ FROM SEQUENCE_MASTER WHERE INST_ID='"
				+ instid + "'  AND ROWNUM<=1";
		// System.out.println("subfeecodeqry "+subcommisioncodeqry);
		String subcommisioncodeseq = (String) jdbctemplate.queryForObject(
				subcommisioncodeqry, String.class);
		// System.out.println("subfeecodeseq"+subcommisioncodeseq);
		String subcommisioncode = subcommisioncodestart
				+ this.paddingZero(subcommisioncodeseq, subcommisioncodelen - 1);
		// System.out.println("subfeecode");
		String updqry = "UPDATE SEQUENCE_MASTER  SET COMMISSION_SUBCODE_SEQ=COMMISSION_SUBCODE_SEQ+1 WHERE INST_ID='"
				+ instid + "' ";
		// System.out.println("updqry IFP SEQUENCE MASTER");
		int x = jdbctemplate.update(updqry);
		return subcommisioncode;
	}

	public List getListOfBins(String instid, String condition,
			JdbcTemplate jdbctemplate, HttpSession session) throws Exception {
		List listofbins = null;
		String listofbinsqry = "SELECT PRD_CODE, PRD_CODE || '-' || PRD_DESC as PRD_DESC FROM PRODUCTINFO WHERE INST_ID='"
				+ instid
				+ "'"
				+ condition
				+ "  ORDER BY PRD_DESC";
		// // enctrace("3030listofbinsqry : " + listofbinsqry );
		listofbins = jdbctemplate.queryForList(listofbinsqry);
		return listofbins;
	}

	public List getListOfBins(String instid, JdbcTemplate jdbctemplate)
			throws Exception {
		List listofbins = null;

		/*
		 * String listofbinsqry =
		 * "SELECT BIN, BIN_DESC FROM PRODUCTINFO WHERE INST_ID='" + instid+
		 * "' AND AUTH_CODE='1' AND DELETED_FLAG !='2'"; //// enctrace(
		 * "3030listofbinsqry : " + listofbinsqry ); listofbins =
		 * jdbctemplate.queryForList(listofbinsqry);
		 */

		// by gowtham-210819
		String listofbinsqry = "SELECT PRD_CODE, PRD_DESC FROM PRODUCTINFO WHERE INST_ID=? AND AUTH_STATUS=?";
		// // enctrace("3030listofbinsqry : " + listofbinsqry );
		listofbins = jdbctemplate.queryForList(listofbinsqry, new Object[] {
				instid, "1", "2" });

		return listofbins;
	}

	public List getMerchantBin(String instid, JdbcTemplate jdbctemplate,
			HttpSession session) throws Exception {
		List listofbins = null;
		String listofbinsqry = "SELECT BIN, SCHEME_NAME as BIN_DESC FROM EZMMS_SCHEMEMASTER  WHERE INST_ID='"
				+ instid + "'";
		// // enctrace("3030listofbinsqry : " + listofbinsqry );
		listofbins = jdbctemplate.queryForList(listofbinsqry);
		return listofbins;
	}

	public String getMccDescriptionIFP(String mcc_code,
			JdbcTemplate merchjdbctemplate) {
		String mcclistqry = "SELECT MCC_DESC FROM  EZMMS_MCC_INFO  WHERE MCC_CODE='"
				+ mcc_code + "' AND ROWNUM<=1";
		// // enctrace("3030mcclistqry__" + mcclistqry );
		String mccdesc = null;
		try {
			mccdesc = (String) merchjdbctemplate.queryForObject(mcclistqry,
					String.class);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}

		return mccdesc;
	}

	public String getterminal_name(String terminalid,
			JdbcTemplate merchjdbctemplate) {
		String qry_terminalname = "SELECT TERMINAL_NAME FROM EZMMS_TERMINALPROFILE WHERE TERMINAL_ID='"
				+ terminalid + "'";
		// // System.out.println("qry_terminalname---> "+qry_terminalname);
		String terminalname = (String) merchjdbctemplate.queryForObject(
				qry_terminalname, String.class);
		return terminalname;
	}

	// siva 160719
	/*
	 * public String fchCustomerId(String instid, String padssenable, String
	 * cardno, String table, JdbcTemplate jdbctemplate) { String statusqry = "";
	 * 
	 * // by siva if (padssenable.equals("Y")) { statusqry = "SELECT CIN FROM "
	 * + table + " WHERE INST_ID='" + instid + "' AND HCARD_NO='" + cardno +
	 * "'  AND ROWNUM<=1"; } else { statusqry = "SELECT CIN FROM " + table +
	 * " WHERE INST_ID='" + instid + "' AND CARD_NO='" + cardno +
	 * "'  AND ROWNUM<=1"; }
	 * 
	 * 
	 * 
	 * statusqry = "SELECT CIN FROM " + table + " WHERE INST_ID='" + instid +
	 * "' AND ='" + cardno+ "'  AND ROWNUM<=1"; trace("statusqry__" +
	 * statusqry); String statusdesc = (String)
	 * jdbctemplate.queryForObject(statusqry, String.class); return statusdesc;
	 * }
	 */

	public String fchCustomerId(String instid, String padssenable,
			String cardno, String table, JdbcTemplate jdbctemplate) {
		String statusqry = "";
		if (padssenable.equals("Y")) {
			statusqry = "SELECT CIN FROM " + table + " WHERE INST_ID='"
					+ instid + "' AND ORG_CHN='" + cardno + "'  AND ROWNUM<=1";
		} else {
			statusqry = "SELECT CIN FROM " + table + " WHERE INST_ID='"
					+ instid + "' AND ORG_CHN='" + cardno + "'  AND ROWNUM<=1";
		}
		trace("statusqry__" + statusqry);
		String statusdesc = (String) jdbctemplate.queryForObject(statusqry,
				String.class);
		return statusdesc;
	}

	public String persfchCustomerId(String instid, String padssenable,
			String cardno, String table, JdbcTemplate jdbctemplate) {
		String statusqry = "";
		// if (padssenable.equals("Y")) {

		/*
		 * statusqry = "SELECT CIN FROM " + table + " WHERE INST_ID='" + instid
		 * + "' AND ORG_CHN='" + cardno+ "'  AND ROWNUM<=1";
		 */
		// by gowtham270819
		statusqry = "SELECT CIN FROM " + table
				+ " WHERE INST_ID=? AND ORG_CHN=?  AND ROWNUM<=?";

		trace("statusqry__" + statusqry);

		/*
		 * } else { statusqry = "SELECT CIN FROM " + table + " WHERE INST_ID='"
		 * + instid + "' AND ORG_CHN='" + cardno + "'  AND ROWNUM<=1"; }
		 */

		trace("statusqry__" + statusqry);

		/*
		 * String statusdesc = (String) jdbctemplate.queryForObject(statusqry,
		 * String.class);
		 */

		// by gowtham-270819
		String statusdesc = (String) jdbctemplate.queryForObject(statusqry,
				new Object[] { instid, cardno, "1" }, String.class);

		return statusdesc;
	}

	public int getcarddetailshcard(String instid, String padssenable,
			String cardno, String table, JdbcTemplate jdbctemplate) {
		int existcount = -1;

		/*
		 * String statusqry = "SELECT count(*) FROM CARD_PRODUCTION " +
		 * "WHERE INST_ID='" + instid + "' AND ORG_CHN='" + cardno+
		 * "' AND STATUS_CODE='62'  AND ROWNUM<=1"; trace(
		 * "checkFeenameexist Count Qury ==> " + statusqry); existcount =
		 * jdbctemplate.queryForInt(statusqry);
		 */

		// by gowtham-280819
		String statusqry = "SELECT count(*) FROM CARD_PRODUCTION "
				+ "WHERE INST_ID=? AND ORG_CHN=? AND STATUS_CODE=?  AND ROWNUM<=1";
		trace("checkFeenameexist Count Qury ==> " + statusqry);
		existcount = jdbctemplate.queryForInt(statusqry, new Object[] { instid,
				cardno, "62", "1" });
		return existcount;
	}

	public List fchCustomerAccountDetails(String instid, String cardno,
			String table, JdbcTemplate jdbctemplate) {
		List accdet = null;
		String statusqry = "SELECT CIN FROM " + table + " WHERE INST_ID='"
				+ instid + "' AND CARD_NO='" + cardno + "'  AND ROWNUM<=1";
		trace("fchCustomerAccountDetails___" + statusqry);
		accdet = jdbctemplate.queryForList(statusqry);

		return accdet;
	}

	public int checkCustomerexist(String instid, String custnum,
			String table_name, JdbcTemplate jdbcTemplate) {
		int existcount = -1;
		StringBuilder count_qury = new StringBuilder();

		/*
		 * count_qury.append(
		 * "select count(*) from PERS_CARD_ORDER where INST_ID='" + instid +
		 * "'  and CIN='" + custnum + "' "); count_qury.append("	UNION ");
		 * count_qury.append(
		 * "	select count(*) from PERS_CARD_PROCESS where INST_ID='" + instid +
		 * "'  and CIN='"+ custnum + "' "); count_qury.append("	UNION ");
		 * count_qury.append(
		 * "	select count(*) from CARD_PRODUCTION where INST_ID='" + instid +
		 * "'  and CIN='"+ custnum + "' "); trace(
		 * "Cutomer Checked Count Qury ==> " + count_qury.toString());
		 * existcount = jdbcTemplate.queryForInt(count_qury.toString());
		 */

		// by gowtham-210819
		count_qury
				.append("select count(*) from PERS_CARD_ORDER where INST_ID=?  and CIN=? ");
		count_qury.append("	UNION ");
		count_qury
				.append("	select count(*) from PERS_CARD_PROCESS where INST_ID=?  and CIN=? ");
		count_qury.append("	UNION ");
		count_qury
				.append("	select count(*) from CARD_PRODUCTION where INST_ID=?  and CIN=? ");
		trace("Cutomer Checked Count Qury ==> " + count_qury.toString());
		existcount = jdbcTemplate.queryForInt(count_qury.toString(),
				new Object[] { instid, custnum, instid, custnum, instid,
						custnum });

		return existcount;
	}

	public int checkFeenameexist(String instid, String feename,
			JdbcTemplate jdbcTemplate) {
		int existcount = -1;

		/*
		 * String count_qury = "select count(*) from FEE_DESC where INST_ID='" +
		 * instid + "' and FEE_DESC='" + feename + "'"; trace(
		 * "checkFeenameexist Count Qury ==> " + count_qury); existcount =
		 * jdbcTemplate.queryForInt(count_qury);
		 */

		// by gowtham-200819
		String count_qury = "select count(*) from FEE_DESC where INST_ID=? and FEE_DESC=?";
		trace("checkFeenameexist Count Qury ==> " + count_qury);
		existcount = jdbcTemplate.queryForInt(count_qury, new Object[] {
				instid, feename });

		return existcount;
	}

	public int checkAccountNoexist(String instid, String acctnum,
			JdbcTemplate jdbcTemplate) {
		int existcount = -1;

		/*
		 * String count_qury =
		 * "select count(*) from ACCOUNTINFO where INST_ID='" + instid +
		 * "' and ACCOUNTNO='" + acctnum+ "'"; trace(
		 * "Cutomer Checked Count Qury ==> " + count_qury); existcount =
		 * jdbcTemplate.queryForInt(count_qury);
		 */

		// by gowtham-210819
		String count_qury = "select count(*) from ACCOUNTINFO where INST_ID=? and ACCOUNTNO=? ";
		trace("Cutomer Checked Count Qury ==> " + count_qury);
		existcount = jdbcTemplate.queryForInt(count_qury, new Object[] {
				instid, acctnum });

		return existcount;
	}

	public String getCardExpiryDate(String instid, String cardnumber,
			String format, JdbcTemplate jdbctemplate) throws Exception {
		String expdate = null;
		try {
			String expdateqry = "SELECT to_char(EXPIRY_DATE,'" + format
					+ "') FROM CARD_PRODUCTION WHERE INST_ID='" + instid
					+ "' AND CARD_NO ='" + cardnumber + "'";
			// // enctrace("3030expdateqry : " + expdateqry );
			// System.out.println("expdateqry : " + expdateqry );
			expdate = (String) jdbctemplate.queryForObject(expdateqry,
					String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return expdate;
	}

	public String getCVV2CardNumber(String instid, String cardnumber,
			JdbcTemplate jdbctemplate) throws Exception {
		String cvv2 = null;
		try {
			String cvv2_qry = "SELECT CVV2 FROM CARD_PRODUCTION WHERE INST_ID='"
					+ instid + "' AND CARD_NO ='" + cardnumber + "'";
			// // enctrace("3030cvv2_qry : " + cvv2_qry );
			// System.out.println("cvv2_qry :" + cvv2_qry );
			cvv2 = (String) jdbctemplate.queryForObject(cvv2_qry, String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return cvv2;
	}

	public String getCVV1CardNumber(String instid, String cardnumber,
			JdbcTemplate jdbctemplate) throws Exception {
		String cvv1 = null;
		try {
			String cvv1_qry = "SELECT CVV2 FROM CARD_PRODUCTION WHERE INST_ID='"
					+ instid + "' AND CARD_NO ='" + cardnumber + "'";
			// // enctrace("3030cvv1_qry : " + cvv1_qry );
			cvv1 = (String) jdbctemplate.queryForObject(cvv1_qry, String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return cvv1;
	}

	public String getCardccy(String instid, String card, String tablename,
			JdbcTemplate jdbctemplate) throws Exception {
		String ccy = "X";
		String ccyqury = "SELECT CARD_CCY FROM " + tablename
				+ " WHERE INST_ID='" + instid + "' AND CARD_NO='" + card + "'";
		try {
			ccy = (String) jdbctemplate.queryForObject(ccyqury, String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return ccy;
	}

	public String getCafRecStatus(String instid, String card, String tablename,
			JdbcTemplate jdbctemplate) throws Exception {
		String cafrec = null;
		String ccyqury = "SELECT CAF_REC_STATUS FROM " + tablename
				+ " WHERE INST_ID='" + instid + "' AND CARD_NO='" + card + "'";
		try {
			cafrec = (String) jdbctemplate
					.queryForObject(ccyqury, String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return cafrec;
	}

	public Personalizeorderdetails gettingReissueCardDetails(String instid,
			String cardno, JdbcTemplate jdbctemplate) {
		Personalizeorderdetails personaloder = null;
		List orderdetailslist;
		String inst_id = "0", order_ref_no = "0", card_type_id = "0", mcardno = "0", sub_prod_id = "0", product_code = "0", card_quantity = "0", embossing_name = "0", encode_data = "0", branch_code = "0", bin = "0", cin = "0", appno = "0", appdate = "NODATE";

		String order_qury = "SELECT INST_ID, ORDER_REF_NO, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE,'0' as CARD_QUANTITY ,EMB_NAME,ENC_NAME,  BRANCH_CODE, BIN FROM CARD_PRODUCTION WHERE INST_ID='"
				+ instid + "' AND CARD_NO='" + cardno + "'";
		trace("order_qury__  " + order_qury);
		orderdetailslist = jdbctemplate.queryForList(order_qury);
		Iterator orderitr = orderdetailslist.iterator();
		while (orderitr.hasNext()) {
			Map ordermap = (Map) orderitr.next();
			inst_id = (String) ordermap.get("INST_ID");
			order_ref_no = (String) ordermap.get("ORDER_REF_NO");
			card_type_id = (String) ordermap.get("CARD_TYPE_ID");
			sub_prod_id = (String) ordermap.get("SUB_PROD_ID");
			product_code = (String) ordermap.get("PRODUCT_CODE");
			card_quantity = (String) ordermap.get("CARD_QUANTITY");
			embossing_name = (String) ordermap.get("EMBOSSING_NAME");
			encode_data = (String) ordermap.get("ENCODE_DATA");
			branch_code = (String) ordermap.get("BRANCH_CODE");
			bin = (String) ordermap.get("BIN");
			appno = "000000";
		}
		personaloder = new Personalizeorderdetails(inst_id, order_ref_no,
				card_type_id, sub_prod_id, product_code, card_quantity,
				embossing_name, encode_data, branch_code, bin, cin, appno,
				appdate, mcardno);
		return personaloder;
	}

	public List getCinchn(String instid, String newcrdno,
			JdbcTemplate jdbctemplate) {
		List cinchn = null;
		String curcard_qury = "SELECT CIN,ORG_CHN FROM PERS_CARD_PROCESS WHERE INST_ID='"
				+ instid + "' AND CARD_NO='" + newcrdno + "'";
		// System.out.println("curcard_qury =====> "+curcard_qury);
		List cardlist = jdbctemplate.queryForList(curcard_qury);
		if (!(cardlist.isEmpty())) {
			cinchn = cardlist;
		}
		return cinchn;
	}

	public String gettingCustomerOldAccountNumber(String instid,
			String newcrdno, JdbcTemplate jdbctemplate) {
		String acctno = "X";
		String acct_qury = "SELECT ACCT_NO FROM IFD_CARD_ACCT_LINK ";
		List cardcinlist = null;
		cardcinlist = getCinchn(instid, newcrdno, jdbctemplate);
		String cinno = "", oldchn = "";
		if (!(cardcinlist.isEmpty())) {
			Iterator itr = cardcinlist.iterator();
			while (itr.hasNext()) {
				Map map = (Map) itr.next();
				cinno = (String) map.get("CIN");
				oldchn = (String) map.get("ORG_CHN");
				// System.out.println("CIN ===> "+cinno+" OLD CHN===> "+oldchn);
			}

			String acctselect_qury = acct_qury + "WHERE INST_ID='" + instid
					+ "' AND CARD_NO='" + oldchn + "' AND CIN='" + cinno + "'";
			// System.out.println("Acct Select Qury ===> "+acctselect_qury);

			acctno = (String) jdbctemplate.queryForObject(acctselect_qury,
					String.class);
			// System.out.println("Accut No Is"+acctno);

		}
		return acctno;
	}

	public int updateOldCardAccountStatus(String instid, String newcardno,
			JdbcTemplate jdbctemplate) {
		int updatestatus = -1;
		List chnlist = getCinchn(instid, newcardno, jdbctemplate);
		String cinno = "", oldchn = "";
		String updateacct_status = "UPDATE IFD_CARD_ACCT_LINK SET ACCT_OLD_STATUS=ACCT_STATUS,ACCT_STATUS='0' WHERE ";
		if (!(chnlist.isEmpty())) {
			Iterator itr = chnlist.iterator();
			while (itr.hasNext()) {
				Map map = (Map) itr.next();
				cinno = (String) map.get("CIN");
				oldchn = (String) map.get("ORG_CHN");
				// System.out.println("CIN ===> "+cinno+" OLD CHN===> "+oldchn);
			}

			String update_qury = updateacct_status + "INST_ID='" + instid
					+ "' AND CARD_NO='" + oldchn + "' AND CIN='" + cinno + "'";
			// System.out.println("UPDATE QUERY===> "+update_qury);
			updatestatus = jdbctemplate.update(update_qury);
		}

		return updatestatus;
	}

	public synchronized String getProfileid(String instid,
			JdbcTemplate jdbctemplate) throws Exception {
		String profileid = "X";
		String profileid_query = "SELECT USER_PROFILE_ID FROM SEQUENCE_MASTER WHERE INST_ID='"
				+ instid + "'";
		// System.out.println("profileid_query===> "+profileid_query);
		// // enctrace("3030profileid_query is:"+profileid_query);
		profileid = (String) jdbctemplate.queryForObject(profileid_query,
				String.class);
		// System.out.println("Profile Id is ===> "+profileid);
		return profileid;
	}

	public synchronized int updateProfileId(String instid,
			JdbcTemplate jdbctemplate) throws Exception {
		int status = -1;
		/*
		 * String updateprofileid_query =
		 * "UPDATE SEQUENCE_MASTER SET USER_PROFILE_ID=USER_PROFILE_ID+1 WHERE INST_ID='"
		 * + instid + "'"; //// enctrace("3030updateprofileid_query is:"
		 * +updateprofileid_query); status =
		 * jdbctemplate.update(updateprofileid_query);
		 */

		String updateprofileid_query = "UPDATE SEQUENCE_MASTER SET USER_PROFILE_ID=USER_PROFILE_ID+1 WHERE INST_ID=?";
		// // enctrace("3030updateprofileid_query is:"+updateprofileid_query);
		status = jdbctemplate.update(updateprofileid_query,
				new Object[] { instid });

		return status;
	}

	public String getCardReferenceNumberLen(String instid,
			JdbcTemplate jdbctemplate) throws Exception {
		String cardrefnolen = null;

		/*
		 * String cardrefnolenqry =
		 * "SELECT CARDREFNO_LEN FROM INSTITUTION  WHERE  INST_ID='" + instid +
		 * "'"; //// enctrace("3030cardrefnolenqry : "+ cardrefnolenqry );
		 * cardrefnolen = (String) jdbctemplate.queryForObject(cardrefnolenqry,
		 * String.class);
		 */

		// by gowtham-220819
		String cardrefnolenqry = "SELECT CARDREFNO_LEN FROM INSTITUTION  WHERE  INST_ID=?";
		// // enctrace("3030cardrefnolenqry : "+ cardrefnolenqry );
		cardrefnolen = (String) jdbctemplate.queryForObject(cardrefnolenqry,
				new Object[] { instid }, String.class);

		return cardrefnolen;
	}

	public String embossingCardNumber(String cardno) throws Exception {
		String splitchar = " ";
		String embcardno = "";

		int cardlen = cardno.length();

		if (cardno.length() == 16)
			embcardno = cardno.substring(0, cardlen - 12) + splitchar
					+ cardno.substring(4, cardlen - 8) + splitchar
					+ cardno.substring(8, cardlen - 4) + splitchar
					+ cardno.substring(12, cardlen);
		else
			embcardno = cardno.substring(0, cardlen - 15) + splitchar
					+ cardno.substring(4, cardlen - 11) + splitchar
					+ cardno.substring(8, cardlen - 7) + splitchar
					+ cardno.substring(12, cardlen - 3) + splitchar
					+ cardno.substring(16, cardlen);

		return embcardno;
	}

	public int checkforgetrequest(String instid, String usertext,
			JdbcTemplate jdbctemplate) throws Exception {
		{
			int checkreq = 0;
			String forgetrequestquery = "SELECT COUNT(*) FROM USER_DETAILS WHERE INSTID='"
					+ instid + "' " + usertext + " AND FORGOTPASSWORDFLAG='1'";
			// System.out.println(forgetrequestquery);
			checkreq = jdbctemplate.queryForInt(forgetrequestquery);
			return checkreq;
		}
	}

	public int checkCardNumberValid(String instid, String corpratecardno,
			JdbcTemplate jdbctemplate) {
		int x = -1;
		String activecardstatus = CommonDesc.ACTIVECARDSTATUS;

		String validqry = "SELECT COUNT(*) FROM CARD_PRODUCTION WHERE ORG_CHN='"
				+ corpratecardno + "' AND CARD_STATUS='05'";
		enctrace("3030validqry : " + validqry);
		x = jdbctemplate.queryForInt(validqry);

		/*
		 * // by gowtham-200819 // String validqry =
		 * "SELECT COUNT(*) FROM CARD_PRODUCTION WHERE CARD_NO=? AND CARD_STATUS=?"
		 * ; //// enctrace("3030validqry : " + validqry ); x =
		 * jdbctemplate.queryForInt(validqry, new Object[] { corpratecardno,
		 * "05" });
		 */

		return x;
	}

	public String getCustomerIdByCardNumber(String instid,
			String corpratecardno, JdbcTemplate jdbctemplate) throws Exception {
		String customerid = null;
		try {

			/*
			 * String customeridqry =
			 * "SELECT CIN FROM IFD_CARD_ACCT_LINK WHERE INST_ID='" + instid +
			 * "' AND  CARD_NO='" + corpratecardno + "' AND ROWNUM<=1"; ////
			 * enctrace("3030customeridqry : " + customeridqry ); customerid =
			 * (String) jdbctemplate.queryForObject(customeridqry,
			 * String.class);
			 */

			// by gowtham
			String customeridqry = "SELECT CIN FROM IFD_CARD_ACCT_LINK WHERE INST_ID=? AND  CARD_NO=? AND ROWNUM<=?";
			// // enctrace("3030customeridqry : " + customeridqry );
			customerid = (String) jdbctemplate.queryForObject(customeridqry,
					new Object[] { instid, corpratecardno, "1" }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return customerid;
	}

	public String checkValidSubProduct(String instid, String product,
			String subproduct, JdbcTemplate jdbctemplate) throws Exception {
		String authstatus = null;
		try {

			/*
			 * String subproductauthqry =
			 * "SELECT AUTH_STATUS FROM INSTPROD_DETAILS WHERE INST_ID='" +
			 * instid+ "' AND PRODUCT_CODE='" + product + "' AND SUB_PROD_ID='"
			 * + subproduct + "'"; //// enctrace("3030subproductauthqry : " +
			 * subproductauthqry ); authstatus = (String)
			 * jdbctemplate.queryForObject(subproductauthqry, String.class);
			 */

			// BY GOWTHAM-270819
			String subproductauthqry = "SELECT AUTH_STATUS FROM INSTPROD_DETAILS WHERE INST_ID=? AND PRODUCT_CODE=? AND SUB_PROD_ID=? ";
			// // enctrace("3030subproductauthqry : " + subproductauthqry );
			authstatus = (String) jdbctemplate.queryForObject(
					subproductauthqry, new Object[] { instid, product,
							subproduct, }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return authstatus;
	}

	public String genRandomNumber(int charLength) {
		return String.valueOf(charLength < 1 ? 0 : new Random()
				.nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
				+ (int) Math.pow(10, charLength - 1));
	}

	public int checkGLKeysConfigured(String instid, String product,
			String subproduct, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String glkeys_configured = "select count(*) as CNT from IFP_GL_KEYS_MAPPING WHERE INST_ID='"
				+ instid
				+ "' AND PRODUCT_CODE='"
				+ product
				+ "' AND SUBPRODUCT_ID='" + subproduct + "'";
		// // enctrace("3030glkeys_configuredqry : " + glkeys_configured );
		x = jdbctemplate.queryForInt(glkeys_configured);
		return x;
	}

	public int checkEntityValueConfigured(String instid, String bin,
			JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String entityqry = "SELECT count(*) as cnt FROM IFP_CARD_ENTITY_MAP WHERE INST_ID='"
				+ instid + "' AND ENTITY_ID LIKE '" + bin + "%'";
		// // enctrace("3030entityqry : " + entityqry );
		x = jdbctemplate.queryForInt(entityqry);
		return x;
	}

	public synchronized String generateLimitSequance(String instid,
			JdbcTemplate jtemp) throws Exception {
		String limitid = null;
		try {

			String limitid_qry = "SELECT LIMIT_SEQ FROM SEQUENCE_MASTER WHERE INST_ID='"
					+ instid + "'";
			enctrace("3030limitid_qry : " + limitid_qry);
			limitid = (String) jtemp.queryForObject(limitid_qry, String.class);

			/*
			 * // by gowtham-200819 String limitid_qry =
			 * "SELECT LIMIT_SEQ FROM SEQUENCE_MASTER WHERE INST_ID=?"; ////
			 * enctrace("3030limitid_qry : " + limitid_qry ); limitid = (String)
			 * jtemp.queryForObject(limitid_qry, new Object[] { instid },
			 * String.class);
			 */

		} catch (EmptyResultDataAccessException e) {
		}
		return limitid;
	}

	public synchronized int updateLimitSequance(String instid,
			JdbcTemplate jtemp) throws Exception {
		int x = -1;
		try {

			/*
			 * String updlimitid_qry =
			 * "UPDATE SEQUENCE_MASTER SET  LIMIT_SEQ = LIMIT_SEQ+1  WHERE INST_ID='"
			 * + instid + "'"; //// enctrace("3030updlimitid_qry : " +
			 * updlimitid_qry ); x = jtemp.update(updlimitid_qry);
			 */

			// by gowtham200819
			String updlimitid_qry = "UPDATE SEQUENCE_MASTER SET  LIMIT_SEQ = LIMIT_SEQ+2  WHERE INST_ID=?";

			x = jtemp.update(updlimitid_qry, new Object[] { instid });

		} catch (EmptyResultDataAccessException e) {
		}
		return x;
	}

	public synchronized String generateFeeSequance(String instid,
			JdbcTemplate jtemp) throws Exception {
		String limitid = null;
		try {

			/*
			 * String limitid_qry =
			 * "SELECT FEE_CODE_SEQ FROM SEQUENCE_MASTER WHERE INST_ID='" +
			 * instid + "'"; //// enctrace("3030limitid_qry : " + limitid_qry );
			 * limitid = (String) jtemp.queryForObject(limitid_qry,
			 * String.class);
			 */

			// by gowtham-200819
			String limitid_qry = "SELECT FEE_CODE_SEQ FROM SEQUENCE_MASTER WHERE INST_ID=?";
			// // enctrace("3030limitid_qry : " + limitid_qry );
			limitid = (String) jtemp.queryForObject(limitid_qry,
					new Object[] { instid }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return limitid;
	}

	// edited by sardar on 14-12-15-----------//

	public synchronized String generatefeeid1(String instid,
			String masterfeename, JdbcTemplate jtemp) throws Exception {
		String limitid = null;
		try {

			/*
			 * String limitid_qry =
			 * "SELECT FEE_CODE FROM FEE_DESC WHERE INST_ID='" + instid +
			 * "'and FEE_DESC='" + masterfeename + "' and auth_code=1"; ////
			 * enctrace("3030limitid_qry : " + limitid_qry ); limitid = (String)
			 * jtemp.queryForObject(limitid_qry, String.class);
			 */

			// by gowtham-200819
			String limitid_qry = "SELECT FEE_CODE FROM FEE_DESC WHERE INST_ID=? and FEE_DESC=? and auth_code=";

			limitid = (String) jtemp.queryForObject(limitid_qry, new Object[] {
					instid, masterfeename, "1" }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return limitid;
	}

	public List getFeeAuthList(String instid, String feecode,
			JdbcTemplate jdbctemplate) throws Exception {
		List subfeelist = null;
		StringBuilder sb = new StringBuilder();

		/*
		 * sb.append(
		 * "select INST_ID, FEE_ID, CURR_CODE,(SELECT CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE INST_ID='"
		 * + instid + "' AND CURR_CODE =NUMERIC_CODE)CURRENCY_DESC, ");
		 * sb.append(
		 * "TXNCODE,(SELECT ACTION_DESC FROM ACTIONCODES WHERE INST_ID='" +
		 * instid+ "' AND TXN_CODE =TXNCODE)TXN_DESC,"); sb.append(
		 * "FEEAMT, (SELECT FEE_DESC FROM FEE_DESC WHERE INST_ID='" + instid +
		 * "' AND FEE_CODE =FEE_ID)FEE_DESC "); sb.append(
		 * "from FEE_MASTER where FEE_ID='" + feecode + "' AND INST_ID='" +
		 * instid + "'");
		 */

		// BY GOWTHAM-200819
		sb.append("select INST_ID, FEE_ID, CURR_CODE,(SELECT CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE INST_ID=? AND CURR_CODE =? )CURRENCY_DESC, ");
		sb.append("TXNCODE,(SELECT ACTION_DESC FROM ACTIONCODES WHERE INST_ID=? AND TXN_CODE =? )TXN_DESC,");
		sb.append("FEEAMT, (SELECT FEE_DESC FROM FEE_DESC WHERE INST_ID=? AND FEE_CODE =? )FEE_DESC ");
		sb.append("from FEE_MASTER where FEE_ID=? AND INST_ID=?");

		// sb.append("SELECT FEE_DESC,TXNCODE,CURR_CODE,FEEAMT FROM
		// FEE_MASTER,FEE_DESC WHERE FEE_MASTER.INST_ID='"+instid+"' AND
		// FEE_MASTER.FEE_ID='"+feecode+"' AND
		// FEE_MASTER.INST_ID=FEE_DESC.INST_ID AND
		// FEE_MASTER.FEE_ID=FEE_DESC.FEE_CODE");
		// // enctrace("3030getFeeAuthListValue : " + sb.toString() );

		subfeelist = jdbctemplate.queryForList(sb.toString(), new Object[] {
				instid, "NUMERIC_CODE", instid, "TXNCODE", instid, "FEE_ID",
				feecode, instid });
		// subfeelist = jdbctemplate.queryForList(sb.toString());

		return subfeelist;
	}

	// Edit end by sardar on 14-12-15-----------//

	public synchronized int UpdateFeeSequance(String instid, JdbcTemplate jtemp)
			throws Exception {
		int x = -1;
		try {

			/*
			 * String updlimitid_qry =
			 * "UPDATE SEQUENCE_MASTER SET  FEE_CODE_SEQ = FEE_CODE_SEQ+1  WHERE INST_ID='"
			 * + instid + "'"; //// enctrace("3030updlimitid_qry : " +
			 * updlimitid_qry ); x = jtemp.update(updlimitid_qry);
			 */

			// by gowtham-270819
			String updlimitid_qry = "UPDATE SEQUENCE_MASTER SET  FEE_CODE_SEQ = FEE_CODE_SEQ+1  WHERE INST_ID=?";
			// // enctrace("3030updlimitid_qry : " + updlimitid_qry );
			x = jtemp.update(updlimitid_qry, new Object[] { instid });

		} catch (EmptyResultDataAccessException e) {
		}
		return x;
	}

	public int insertKycLimit(String instid, String productcode,
			String subproduct, String kyclevel, String limitedflag,
			String kyclimitamt, String kyclimitcount, String shiftdays,
			JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		try {

			/*
			 * String kycinsertqry =
			 * " insert into ifp_kyc_limit  (inst_id, product_code, sub_prod_id, kyc_level, kyc_limit_flag, kyc_limit_amount, kyc_limit_count, kyc_shift_duration) "
			 * ; kycinsertqry += "values ('" + instid + "', '" + productcode +
			 * "', '" + subproduct + "', '" + kyclevel + "', '" + limitedflag +
			 * "', " + kyclimitamt + ", " + kyclimitcount + ", " + shiftdays +
			 * ") "; //// enctrace("3030kycinsertqry :" + kycinsertqry ); x =
			 * jdbctemplate.update(kycinsertqry);
			 */

			// by gowtham-200819
			String kycinsertqry = " insert into ifp_kyc_limit  (inst_id, product_code, sub_prod_id, kyc_level, kyc_limit_flag, kyc_limit_amount, kyc_limit_count, kyc_shift_duration) ";
			kycinsertqry += "values (?,?,?, ?,?,?, ?,?) ";

			x = jdbctemplate.update(kycinsertqry, new Object[] { instid,
					productcode, subproduct, kyclevel, limitedflag,
					kyclimitamt, kyclimitcount, shiftdays });
			// x = jdbctemplate.update(kycinsertqry);

		} catch (EmptyResultDataAccessException e) {
		}
		return x;
	}

	public List getTransactionDetails(String instid, String cardno,
			String refno, String traceno, JdbcTemplate jdbctemplate)
			throws Exception {
		List txndetails = null;

		/*
		 * String txndetailsqry =
		 * "SELECT TXNCODE, AMOUNT,TERMINALID,TERMLOC, TO_CHAR(TRANDATE,'DDMMyy')||TO_CHAR(TRANTIME) AS TRANDATETIME, DEVICETYPE, ACQUIRERID, ACCEPTORID FROM IFP_TRANSACTION_MASTER  "
		 * ; txndetailsqry += " WHERE INST_ID='" + instid + "' AND CHN='" +
		 * cardno + "' AND  REFNUM='" + refno + "' AND TRACENO='" + traceno +
		 * "' "; //// enctrace("3030txndetailsqry :" + txndetailsqry );
		 * txndetails = jdbctemplate.queryForList(txndetailsqry);
		 */

		// by gowtham-200819
		String txndetailsqry = "SELECT TXNCODE, AMOUNT,TERMINALID,TERMLOC, TO_CHAR(TRANDATE,'DDMMyy')||TO_CHAR(TRANTIME) AS TRANDATETIME, DEVICETYPE, ACQUIRERID, ACCEPTORID FROM IFP_TRANSACTION_MASTER  ";
		txndetailsqry += " WHERE INST_ID=? AND CHN=? AND  REFNUM=? AND TRACENO=? ";
		// // enctrace("3030txndetailsqry :" + txndetailsqry );
		txndetails = jdbctemplate.queryForList(txndetailsqry, new Object[] {
				instid, cardno, refno, traceno });

		return txndetails;
	}

	public String getCardHolderType(String instid, String cardno,
			JdbcTemplate jdbctemplate) throws Exception {
		String cardholdertype = null;
		int existmerchant = -1;

		/*
		 * String cardholdertypeqry =
		 * "SELECT COUNT(*) AS CNT FROM IFP_CARD_ENTITY_MAP WHERE INST_ID='" +
		 * instid+ "' AND ENTITY_TYPE='$MC'  AND CARDNO='" + cardno + "'";
		 * existmerchant = jdbctemplate.queryForInt(cardholdertypeqry);
		 */

		// by gowtham-200819
		String cardholdertypeqry = "SELECT COUNT(*) AS CNT FROM IFP_CARD_ENTITY_MAP WHERE INST_ID=? AND ENTITY_TYPE=?  AND CARDNO=?";
		existmerchant = jdbctemplate.queryForInt(cardholdertypeqry,
				new Object[] { instid, "$MC", cardno });

		if (existmerchant > 0) {
			cardholdertype = "$MC";
		} else {
			cardholdertype = "$FC";
		}
		return cardholdertype;
	}

	public String getAccountNumberByCardNo(String instid, String cardno,
			String currencycode, JdbcTemplate jdbctemplate) throws Exception {
		String acctno = null;
		try {
			String acctnoqry = "SELECT ACCT_NO FROM IFD_CARD_ACCT_LINK WHERE INST_ID='"
					+ instid
					+ "' AND CARD_NO='"
					+ cardno
					+ "' AND ACCT_CCY='"
					+ currencycode + "'";
			// // enctrace("3030acctnoqry :" + acctnoqry);
			acctno = (String) jdbctemplate.queryForObject(acctnoqry,
					String.class);
		} catch (Exception e) {
		}
		return acctno;
	}

	public String getMobileNumber(String instid, String cardno,
			JdbcTemplate jdbctemplate) {
		String mobilnoqry = "SELECT NVL(MOBILENO ,'NA' ) AS MOBILENO FROM    CARD_PRODUCTION WHERE INST_ID='"
				+ instid + "' AND CARD_NO='" + cardno + "'";
		String mobileno = (String) jdbctemplate.queryForObject(mobilnoqry,
				String.class);
		return mobileno;
	}

	public String getMobileNumberByCustomerProcess(String instid, String cin,
			JdbcTemplate jdbctemplate) throws Exception {
		String mobileno = null;

		
		 String mobilnoqry =
		 "SELECT  NVL(MOBILE ,'0000000000' ) AS MOBILENO FROM   CUSTOMERINFO"
		  + " WHERE INST_ID='"+ instid + "' AND CIN='" + cin + "'"; ////
		  enctrace("3030mobilnoqry"+mobilnoqry); 
		  System.out.println(" mobilewuryy@@@@ "+mobilnoqry);
		  try {
			  mobileno = (String) jdbctemplate.queryForObject(mobilnoqry, String.class);
		  }catch(Exception e){
			 trace("exception ocuured ::: "+e.getMessage());
			 e.printStackTrace();
		  }

		// by gowtham-220819
		/*String mobilnoqry = "SELECT  NVL(MOBILE ,'0000000000' ) AS MOBILENO FROM   CUSTOMERINFO"
				+ " WHERE INST_ID=? AND CIN=?";
*/		// // enctrace("3030mobilnoqry"+mobilnoqry);
			
		return mobileno;
	}

	public List getAccountNoDetailsAcctinfo(String instid, String cin,
			JdbcTemplate jdbctemplate) throws Exception {
		List accountno = null;

		/*
		 * String accountnoqry =
		 * "SELECT  NVL(ACCOUNTNO ,'0000000000' ) AS ACCOUNTNO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACCT_CURRENCY FROM   ACCOUNTINFO "
		 * + "WHERE INST_ID='"+ instid + "' AND CIN='" + cin + "'"; ////
		 * enctrace("3030getAccountNoDetailsAcctinfo:::"+accountnoqry); try {
		 * accountno = jdbctemplate.queryForList(accountnoqry);
		 */

		// by gowtham-220819
		String accountnoqry = "SELECT  NVL(ACCOUNTNO ,'0000000000' ) AS ACCOUNTNO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACCT_CURRENCY FROM   ACCOUNTINFO "
				+ "WHERE INST_ID=? AND CIN=?";
		// // enctrace("3030getAccountNoDetailsAcctinfo:::"+accountnoqry);
		try {
			accountno = jdbctemplate.queryForList(accountnoqry, new Object[] {
					instid, cin });

		} catch (EmptyResultDataAccessException e) {
		}
		return accountno;
	}

	public void insertAuditTrailPendingCommit(String instid, String usercode,
			AuditBeans auditbean, JdbcTemplate jdbctemplate,
			PlatformTransactionManager txManager) throws Exception {
		IfpTransObj transact = this.myTranObject("AUDIT", txManager);
		int x = -1;
		try {

			x = insertAuditTrailProcess(instid, usercode, auditbean,
					jdbctemplate, txManager);
			if (x > 0) {
				txManager.commit(transact.status);
				transact = null;
			}
			if (x < 0) {
				txManager.rollback(transact.status);
				transact = null;
			}
		} catch (Exception e) {
			// trace("Auditran Exception : " + e.getMessage());
			txManager.rollback(transact.status);
			transact = null;
		}

	}

	public void updatecheckerdata(String instid, String usercode,
			AuditBeans auditbean, JdbcTemplate jdbctemplate,
			PlatformTransactionManager txManager) throws Exception {
		IfpTransObj transact = this.myTranObject("AUDIT", txManager);
		int x = -1;
		try {

			x = updateAuditTrailProcess(instid, usercode, auditbean,
					jdbctemplate, txManager);
			if (x > 0) {
				txManager.commit(transact.status);
				transact = null;
			}
			if (x < 0) {
				txManager.rollback(transact.status);
				transact = null;
			}
		} catch (Exception e) {
			// trace("Auditran Exception : " + e.getMessage());
			txManager.rollback(transact.status);
			transact = null;
		}

	}

	public void insertAuditTrail(String instid, String usercode,
			AuditBeans auditbean, JdbcTemplate jdbctemplate,
			PlatformTransactionManager txManager) throws Exception {
		IfpTransObj transact = this.myTranObject("AUDIT", txManager);
		int x = -1;
		try {
			x = insertAuditTrailProcess(instid, usercode, auditbean,
					jdbctemplate, txManager);
			/*
			 * if(x > 0) { txManager.commit(transact.status); transact = null; }
			 */if (x < 0) {
				txManager.rollback(transact.status);
				transact = null;
			}
		} catch (Exception e) {
			// trace("Auditran Exception : " + e.getMessage());
			txManager.rollback(transact.status);
			transact = null;
		} finally {
			txManager.commit(transact.status);
			transact = null;
		}
	}

	public int insertAuditTrailProcess(String instid, String usercode,
			AuditBeans auditbean, JdbcTemplate jdbctemplate,
			PlatformTransactionManager txManager) throws Exception {
		int x = -1;

		Date date = new Date();
		String actmsg = null, product = null, subproduct = null, cardno = null, bin = null, remarks = null, auditactcode = null, applicationid = null, prefilename = null, apptype = null, cardnumber = null;
		String custname=null,  cardcollectbranch=null,Checker_id=null;
		try {
			actmsg = auditbean.getActmsg();
			product = auditbean.getProduct();
			subproduct = auditbean.getSubproduct();
			cardno = auditbean.getCardno();
			bin = auditbean.getBin();
			remarks = auditbean.getRemarks();
			auditactcode = auditbean.getAuditactcode();
			applicationid = auditbean.getApplicationid();
			prefilename = auditbean.getPrefilename();
			apptype = auditbean.getApptype();
			custname=auditbean.getCustname();
			cardcollectbranch=auditbean.getCardcollectbranch();
			Checker_id=auditbean.getChecker();
			
			// cardnumber=auditbean.getCardnumber();

			// auditbean.setCin(cin);
			// auditbean.setCustname(embname);
			// auditbean.setAccoutnno(acctno);
			// auditbean.setCardcollectbranch(cardcollectbranch);
			// auditbean.setChecker(CHECKER_ID);

			if (actmsg == null) {
				actmsg = "";
			}
			if (product == null) {
				product = "";
			}
			if (subproduct == null) {
				subproduct = "";
			}
			if (bin == null) {
				bin = "";
			}
			if (cardno == null) {
				cardno = "";
			}
			if (remarks == null) {
				remarks = "";
			}
			if (auditactcode == null) {
				auditactcode = "";
			}
			if (applicationid == null) {
				applicationid = "";
			}
			if (prefilename == null) {
				prefilename = "";
			}
			if (apptype == null) {
				apptype = "";
				}
			if (custname == null) {
				custname = "";
			}
			if(cardcollectbranch==null){
				cardcollectbranch="";
				
				if(Checker_id==null){
					Checker_id="";
				}
			}
			// if( cardnumber == null){ cardnumber = "" ;}

			/*
			 * String insertauditqry =
			 * "INSERT INTO AUDITRAN (INST_ID, BIN, PRODUCTCODE, SUBPRODUCT, CARDNO, USERCODE, ACTIONDATE, AUDITMSG, REMARKS , AUDITACTCODE, APPLICATIONID, PREFILE_NAME, APPTYPE,ACTIONTYPE,ACCNO,CUSTNAME,BRANCHCODE,CIN,CHECKERID,IP_ADDRESS ) VALUES "
			 * ; insertauditqry += "('" + instid + "','" + bin + "','" + product
			 * + "','" + subproduct + "','" + cardno + "','" + usercode +
			 * "',SYSDATE,'" + actmsg + "','" + remarks + "','" + auditactcode +
			 * "','" + applicationid + "','" + prefilename + "', '" + apptype +
			 * "','" + auditbean.getActiontype() + "','" +
			 * auditbean.getAccoutnno() + "','" + auditbean.getCustname() +
			 * "','" + auditbean.getCardcollectbranch() + "','" +
			 * auditbean.getCin() + "','" + auditbean.getChecker()+ "','" +
			 * auditbean.getIpAdress() + "')";//------------------- //enctrace(
			 * "insertauditqry :" + insertauditqry); x =
			 * jdbctemplate.update(insertauditqry);
			 */

			// / by gowtham-260819
			String insertauditqry = "INSERT INTO AUDITRAN (INST_ID, BIN, PRODUCTCODE, SUBPRODUCT, CARDNO, USERCODE, ACTIONDATE, AUDITMSG, REMARKS , AUDITACTCODE, APPLICATIONID, PREFILE_NAME, APPTYPE,ACTIONTYPE,ACCNO,CUSTNAME,BRANCHCODE,CIN,CHECKERID,IP_ADDRESS ) VALUES ";

			insertauditqry += "(?,?,?,?, ?,?,SYSDATE,?, ?,?,?,?, ?,?,?,?,?,?,?,?)"; // -------------------

			enctrace("insertauditqry :" + insertauditqry);
			x = jdbctemplate.update(
					insertauditqry,
					new Object[] { instid, bin, product, subproduct, cardno,
							usercode, actmsg, remarks, auditactcode,
							applicationid, prefilename, apptype,
							auditbean.getActiontype(),
							auditbean.getAccoutnno(), auditbean.getCustname(),
							auditbean.getCardcollectbranch(),
							auditbean.getCin(), auditbean.getChecker(),
							auditbean.getIpAdress() });

		} catch (Exception e) {
			e.printStackTrace();
			// trace("Auditran Exception : " + e.getMessage());
		} finally {
			actmsg = null;
			product = null;
			subproduct = null;
			cardno = null;
			bin = null;
			remarks = null;
			auditactcode = null;
			applicationid = null;
			prefilename = null;
			apptype = null;
		}
		return x;
	}

	public int updateAuditTrailProcess(String instid, String usercode,
			AuditBeans auditbean, JdbcTemplate jdbctemplate,
			PlatformTransactionManager txManager) throws Exception {
		int x = -1;
		String actmsg = null, product = null, subproduct = null, cardno = null, bin = null, remarks = null, auditactcode = null, applicationid = null, prefilename = null, apptype = null, cardnumber = null;
		try {
			actmsg = auditbean.getActmsg();
			product = auditbean.getProduct();
			subproduct = auditbean.getSubproduct();
			cardno = auditbean.getCardno();
			bin = auditbean.getBin();
			remarks = auditbean.getRemarks();
			auditactcode = auditbean.getAuditactcode();
			applicationid = auditbean.getApplicationid();
			prefilename = auditbean.getPrefilename();
			apptype = auditbean.getApptype();
			// cardnumber=auditbean.getCardnumber();

			if (actmsg == null) {
				actmsg = "";
			}
			if (product == null) {
				product = "";
			}
			if (subproduct == null) {
				subproduct = "";
			}
			if (bin == null) {
				bin = "";
			}
			if (cardno == null) {
				cardno = "";
			}
			if (remarks == null) {
				remarks = "";
			}
			if (auditactcode == null) {
				auditactcode = "";
			}
			if (applicationid == null) {
				applicationid = "";
			}
			if (prefilename == null) {
				prefilename = "";
			}
			if (apptype == null) {
				apptype = "";
			}
			// if( cardnumber == null){ cardnumber = "" ;}

			String updateauditqry = "UPDATE  AUDITRAN  SET CHECKERID='"
					+ auditbean.getChecker() + "' where BRANCHCODE in('"
					+ auditbean.getCardcollectbranch()
					+ "','000') AND AUDITACTCODE='4109' AND INST_ID='" + instid
					+ "' ";
			// // enctrace("3030AuditQuery :" +insertauditqry);
			x = jdbctemplate.update(updateauditqry);
		} catch (Exception e) {
			// trace("Auditran Exception update: " + e.getMessage());
		} finally {
			actmsg = null;
			product = null;
			subproduct = null;
			cardno = null;
			bin = null;
			remarks = null;
			auditactcode = null;
			applicationid = null;
			prefilename = null;
			apptype = null;
		}
		return x;
	}

	public int checkCardExistInSystem(String instid, String cardno,
			String tablename, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;

		/*
		 * String cardexistqry = "SELECT COUNT(ORG_CHN) as cnt FROM " +
		 * tablename + " " + "WHERE INST_ID='" + instid+
		 * "' AND ORDER_REF_NO=(SELECT ORDER_REF_NO FROM PERS_CARD_PROCESS_HASH "
		 * + "WHERE HCARD_NO='" + cardno+ "')"; x =
		 * jdbctemplate.queryForInt(cardexistqry);
		 */

		// by gowtham-300819
		String cardexistqry = "SELECT COUNT(ORG_CHN) as cnt FROM "
				+ tablename
				+ " "
				+ "WHERE INST_ID=? AND ORDER_REF_NO=(SELECT ORDER_REF_NO FROM PERS_CARD_PROCESS_HASH "
				+ "WHERE HCARD_NO=?)";
		x = jdbctemplate.queryForInt(cardexistqry, new Object[] { instid,
				cardno });

		return x;
	}

	public String checkcardavailabelclose(String instid, String hascard,
			JdbcTemplate jdbctemplate) throws Exception {

		String cardexistqry = "SELECT decode(STATUS,'263','OTP TRIES EXCEED','97','MIGRATED','70','TEMP BLOCK','74','LOST / STOLEN','76', 'CLOSE','50','ACTIVE','61','REPIN','62','REISSUE','53',  'FIRST USE CARD','16','NOT ACTIVATED','77','DAMAGE','75','PIN LOCKED') as STATUS FROM ezcardinfo WHERE INSTID='"
				+ instid + "' AND chn='" + hascard + "' ";
		String cardstaus = (String) jdbctemplate.queryForObject(cardexistqry,
				String.class);
		trace("cardexistqry======>" + cardexistqry);
		return cardstaus;
	}

	public int checckcardcountforReissue(String instid, String hascard,
			JdbcTemplate jdbctemplate) throws Exception {
		int x = 0;
		String cardexistqry = "SELECT COUNT(*)  FROM ezcardinfo WHERE INSTID='"
				+ instid + "' AND chn='" + hascard + "' ";
		x = jdbctemplate.queryForInt(cardexistqry);
		trace("checckcardcountforReissue======>" + cardexistqry);
		return x;
	}

	public List getProfileList(String instid, JdbcTemplate jdbcTemplate) {
		String statusqry = "SELECT PROFILE_NAME FROM " + getProfilelistMain()
				+ " WHERE INST_ID='" + instid + "'";
		List statusdesc = jdbcTemplate.queryForList(statusqry);
		return statusdesc;
	}

	public String getProductListbybin(JdbcTemplate jdbctemplate, String bin,
			String instid) throws IOException {

		String query = null;
		if (bin.equals("ALL")) {
			query = "select PRODUCT_CODE, CARD_TYPE_NAME from PRODUCT_MASTER where INST_ID='"
					+ instid + "' and STATUS=1";

		} else {
			query = "select PRODUCT_CODE, CARD_TYPE_NAME from PRODUCT_MASTER where INST_ID='"
					+ instid + "' and BIN='" + bin + "' and STATUS=1";
		}
		// System.out.println(query + " __ get sub prod list ");
		List subprodlist = jdbctemplate.queryForList(query);
		Iterator itr = subprodlist.iterator();
		String result = "<option value='-1'> - select - </option>";
		result += "<option value='ALL'> - ALL - </option>";

		while (itr.hasNext()) {
			Map map = (Map) itr.next();
			String subprodid = (String) map.get("PRODUCT_CODE");
			String subprodname = (String) map.get("CARD_TYPE_NAME");
			result += "<option value='" + subprodid + "'>" + subprodname
					+ "</option>";
			// result = result + max;
		}
		trace("result======>" + result);
		return result;

		// return(jdbcTemplate.queryForList(query));
	}

	public String genReportView(List listqry, String returnurl) {
		trace("****************  statement report View begin ****************");
		// // enctrace("3030**************** statement report View begin
		// // ****************");
		trace("\n\n");
		// // enctrace("3030\n\n");
		if (!(listqry.isEmpty())) {
			trace("List size" + listqry.size());
			setGenreport_list(listqry);
		}
		return returnurl;
		// return "statementview";
	}

	public String getProfileDesc(String instid, BigDecimal profileid,
			JdbcTemplate jdbcTemplate) {
		/*
		 * String statusqry = "SELECT PROFILE_NAME FROM " + getProfilelistMain()
		 * + " WHERE INSTID='" + instid + "' AND PROFILE_ID='" + profileid +
		 * "'  AND ROWNUM<=1"; String statusdesc = (String)
		 * jdbcTemplate.queryForObject(statusqry, String.class);
		 */

		// BY GOWTHAM-130819
		String statusqry = "SELECT PROFILE_NAME FROM " + getProfilelistMain()
				+ " WHERE INSTID=?AND PROFILE_ID=?  AND ROWNUM<=?";
		String statusdesc = (String) jdbcTemplate.queryForObject(statusqry,
				new Object[] { instid, profileid, "1" }, String.class);
		return statusdesc;
	}

	public String getProfileDesc(String instid, String profileid,
			JdbcTemplate jdbcTemplate) throws Exception {
		String statusdesc = null;
		try {

			/*
			 * String statusqry = "SELECT PROFILE_NAME FROM " +
			 * getProfilelistTemp() + " WHERE INSTID='" + instid+
			 * "' AND PROFILE_ID='" + profileid + "'  AND ROWNUM<=1"; trace(
			 * "PROFILE_NAME==> " + statusqry); statusdesc = (String)
			 * jdbcTemplate.queryForObject(statusqry, String.class);
			 */

			// by gowtham140819
			String statusqry = "SELECT PROFILE_NAME FROM "
					+ getProfilelistTemp()
					+ " WHERE INSTID=?AND PROFILE_ID=?  AND ROWNUM<=?";
			trace("PROFILE_NAME==> " + statusqry);
			statusdesc = (String) jdbcTemplate.queryForObject(statusqry,
					new Object[] { instid, profileid, "1" }, String.class);

		} catch (Exception e) {
		}
		return statusdesc;
	}

	public String getGlname(String instid, String glcode,
			JdbcTemplate jdbcTemplate) {
		String listofglgrpqry = "select GL_NAME from IFP_GL_MASTER where INST_ID='"
				+ instid + "' and GL_CODE='" + glcode + "'";
		// System.out.println(listofglgrpqry+" <---listofglgrpqry");
		String glname = (String) jdbcTemplate.queryForObject(listofglgrpqry,
				String.class);
		return glname;
	}

	public String getReason(String instid, String reasoncode,
			JdbcTemplate jdbctemplate) {
		String qry_reasondesc = "SELECT REASON_DESC FROM IFP_TRASACTION_REASON WHERE REASONCODE='"
				+ reasoncode + "'";
		// System.out.println("qry_terminalname---> "+qry_reasondesc);
		String reasondesc = (String) jdbctemplate.queryForObject(
				qry_reasondesc, String.class);
		return reasondesc;
	}

	public List getPersonalReissueList(String instid, String filtercond,
			JdbcTemplate jdbctemplate) throws Exception {
		String listoforderqry = "SELECT ORDER_REF_NO,CARD_TYPE_ID, BIN,BRANCH_CODE,PRODUCT_CODE,SUB_PROD_ID,EMB_NAME, MAKER_ID, to_char(REISSUE_DATE, 'dd-MON-yyyy') as REISSUE_DATE,CIN,decode(MKCK_STATUS,'M','Waiting For Authorization','P','Authorized','D','DeAuthorized') as STATUS FROM CARD_PRODUCTION WHERE INST_ID='"
				+ instid + "'" + filtercond + " order by ORDER_REF_NO DESC";
		enctrace("getinstatcarorderlist is  : " + listoforderqry);

		List listoforder = jdbctemplate.queryForList(listoforderqry);

		ListIterator itr = listoforder.listIterator();
		while (itr.hasNext()) {
			Map temp = (Map) itr.next();
			String bin = (String) temp.get("BIN");
			trace("BIN IS==> " + bin);
			String usercode = (String) temp.get("MAKER_ID");
			trace("USER CODE IS==> " + usercode);
			String prodcode = (String) temp.get("PRODUCT_CODE");
			trace("PRODUCT CODE IS==> " + prodcode);
			String productdesc = getProductdesc(instid, prodcode, jdbctemplate);
			trace("productdesc==> " + productdesc);
			String cardtypedesc = getCardTypeDesc(instid, bin, jdbctemplate);
			trace("cardtypedesc==> " + cardtypedesc);
			String subprodid = (String) temp.get("SUB_PROD_ID");
			String subproductname = getSubProductdesc(instid, subprodid,
					jdbctemplate);
			String username = getUserName(instid, usercode, jdbctemplate);
			trace("username==> " + username);
			temp.put("PRODBINDESC", productdesc);
			temp.put("CARDTYPEDESC", cardtypedesc);
			temp.put("SUBPRODDESC", subproductname);
			temp.put("USERNAME", username);
			itr.remove();
			itr.add(temp);
		}
		return listoforder;

	}

	public Personalizeorderdetails gettingPersonalizeorderDetails(
			String instid, String order_refnum, JdbcTemplate jdbctemplate) {
		Personalizeorderdetails personaloder = null;
		List orderdetailslist;
		String inst_id = "0", order_ref_no = "0", card_type_id = "0", mcardno = "", sub_prod_id = "0", product_code = "0", card_quantity = "0", embossing_name = "0", encode_data = "0", branch_code = "0", bin = "0", cin = "0", appno = "0", appdate = "NODATE";
		String order_qury;
		order_qury = "SELECT INST_ID, ORDER_REF_NO, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, CARD_QUANTITY, EMBOSSING_NAME,  ENCODE_DATA,  BRANCH_CODE, BIN FROM PERS_CARD_ORDER WHERE INST_ID='"
				+ instid + "' AND ORDER_REF_NO='" + order_refnum + "'";
		trace(" PERS_CARD_ORDER order_qury__  " + order_qury);
		orderdetailslist = jdbctemplate.queryForList(order_qury);
		Iterator orderitr = orderdetailslist.iterator();
		while (orderitr.hasNext()) {
			Map ordermap = (Map) orderitr.next();
			inst_id = (String) ordermap.get("INST_ID");
			order_ref_no = (String) ordermap.get("ORDER_REF_NO");
			card_type_id = (String) ordermap.get("CARD_TYPE_ID");
			sub_prod_id = (String) ordermap.get("SUB_PROD_ID");
			product_code = (String) ordermap.get("PRODUCT_CODE");
			card_quantity = (String) ordermap.get("CARD_QUANTITY");
			embossing_name = (String) ordermap.get("EMBOSSING_NAME");
			encode_data = (String) ordermap.get("ENCODE_DATA");
			branch_code = (String) ordermap.get("BRANCH_CODE");
			bin = (String) ordermap.get("BIN");
			// System.out.println(" bin value "+bin);
			appno = "000000";
		}
		personaloder = new Personalizeorderdetails(inst_id, order_ref_no,
				card_type_id, sub_prod_id, product_code, card_quantity,
				embossing_name, encode_data, branch_code, bin, cin, appno,
				appdate, mcardno);
		return personaloder;
	}

	public Personalizeorderdetails gettingPersonalizeorderDetailsFromProd(
			String instid, String order_refnum, String condition,
			JdbcTemplate jdbctemplate) {
		Personalizeorderdetails personaloder = null;
		List orderdetailslist;
		String inst_id = "0", order_ref_no = "0", mcardno = "0", card_type_id = "0", sub_prod_id = "0", product_code = "0", card_quantity = "1", embossing_name = "0", encode_data = "0", branch_code = "0", bin = "0", cin = "0", appno = "0", appdate = "NODATE";
		String order_qury;

		/*
		 * order_qury =
		 * "SELECT INST_ID, ORDER_REF_NO, CARD_TYPE_ID,MCARD_NO, SUB_PROD_ID, PRODUCT_CODE, EMB_NAME,  ENC_NAME, BRANCH_CODE, BIN FROM CARD_PRODUCTION WHERE INST_ID='"
		 * + instid + "' AND ORDER_REF_NO='" + order_refnum + "' AND ROWNUM<=1 "
		 * ; trace(" CARD_PRODUCTION order_qury__  " + order_qury);
		 * orderdetailslist = jdbctemplate.queryForList(order_qury);
		 */

		// by gowtham-300819
		order_qury = "SELECT INST_ID, ORDER_REF_NO, CARD_TYPE_ID,MCARD_NO, SUB_PROD_ID, PRODUCT_CODE, EMB_NAME,  ENC_NAME, BRANCH_CODE, BIN FROM CARD_PRODUCTION "
				+ "WHERE INST_ID=? AND ORDER_REF_NO=? AND ROWNUM<=? ";
		trace(" CARD_PRODUCTION order_qury__  " + order_qury);
		orderdetailslist = jdbctemplate.queryForList(order_qury, new Object[] {
				instid, order_refnum, "1" });

		Iterator orderitr = orderdetailslist.iterator();
		while (orderitr.hasNext()) {
			Map ordermap = (Map) orderitr.next();
			inst_id = (String) ordermap.get("INST_ID");
			order_ref_no = (String) ordermap.get("ORDER_REF_NO");
			card_type_id = (String) ordermap.get("CARD_TYPE_ID");
			sub_prod_id = (String) ordermap.get("SUB_PROD_ID");
			product_code = (String) ordermap.get("PRODUCT_CODE");
			embossing_name = (String) ordermap.get("EMB_NAME");
			encode_data = (String) ordermap.get("ENC_NAME");
			branch_code = (String) ordermap.get("BRANCH_CODE");
			bin = (String) ordermap.get("BIN");
			mcardno = (String) ordermap.get("MCARD_NO");
			// System.out.println(" bin value "+bin);
			appno = "000000";
		}
		personaloder = new Personalizeorderdetails(inst_id, order_ref_no,
				card_type_id, sub_prod_id, product_code, card_quantity,
				embossing_name, encode_data, branch_code, bin, cin, appno,
				appdate, mcardno);
		return personaloder;
	}

	public String getCurDetails() {
		String curDetails_query;
		curDetails_query = "select CURRENCY_DESC,CURRENCY_CODE,NUMERIC_CODE,CASE CURRENCY_STATUS WHEN '1' THEN 'Active' WHEN '0' THEN 'InActive' ELSE CURRENCY_STATUS END as CURRENCY_STATUS from GLOBAL_CURRENCY";
		// // enctrace("3030curDetails_query::::"+curDetails_query);
		return curDetails_query;
	}

	public List getlogindetails(int profileid, String instid,
			JdbcTemplate jdbctemplate) {

		/*
		 * String list = "SELECT * FROM " + getProfilelistMain() +
		 * " where PROFILE_ID='" + profileid + "' and INSTID='"+ instid + "'";
		 * List profileloginlist = jdbctemplate.queryForList(list);
		 */

		// by gowtham-140819
		String list = "SELECT * FROM " + getProfilelistMain()
				+ " where PROFILE_ID=?and INSTID=?";
		List profileloginlist = jdbctemplate.queryForList(list, new Object[] {
				profileid, instid });

		return profileloginlist;
	}

	public String getcurrentDateStringmmyy() {
		String resultdate = null;
		resultdate = "Select TO_CHAR(sysdate,'mm/yy') from dual";
		return resultdate;
	}

	// ADDED BY PRITTO

	public int checkBinExist(String instid, String bin,
			JdbcTemplate jdbctemplate) throws Exception {

		String totalbinqry = "SELECT COUNT(*) FROM PRODUCTINFO WHERE PRD_CODE LIKE '"
				+ bin + "%'";
		int totalbin = jdbctemplate.queryForInt(totalbinqry);
		return totalbin;
	}

	public String generateProperatoryBin(String instid,
			JdbcTemplate jdbctemplate) throws Exception {
		String basicbin5digit = "9999";
		int totalbin = 0;
		int binlen = 6;
		String propbin = "";
		for (int cnt = 0; cnt < 99; cnt++) {
			propbin = basicbin5digit + cnt;
			totalbin = checkBinExist(instid, propbin, jdbctemplate);
			if (totalbin == 0) {
				basicbin5digit = basicbin5digit + cnt;
				if (propbin.length() > binlen) {
					break;
				}
				if (propbin.length() == binlen) {
					break;
				}
			}
		}
		return propbin;
	}

	public String getAddress(String instid, String productcode,
			String subproduct, String cin, String type, String addrskey,
			JdbcTemplate jdbctemplate) throws Exception {
		String address = "", addressqry = "";
		try {
			if (type.equals("INSTANT")) {

				addressqry = "SELECT NVL(ADDRESS,'--') AS ADDRESS FROM INST_TEMPNAME WHERE INST_ID='"
						+ instid
						+ "' and PRODUCT_CODE ='"
						+ productcode
						+ "' AND SUBPRODUCT ='" + subproduct + "'";
				address = (String) jdbctemplate.queryForObject(addressqry,
						String.class);
			} else {

				// addressqry = "SELECT P_PO_BOX || P_HOUSE_NO AS RES_ADDR FROM
				// CUSTOMERINFO WHERE INST_ID='" + instid
				// + "' and CIN ='" + cin + "'";

				/*addressqry = "SELECT P_street_name ||',' ||p_ward_name || ',' || c_city  AS RES_ADDR FROM CUSTOMERINFO WHERE INST_ID='"
						+ instid + "' and CIN ='" + cin + "'";
*/
				
				
				addressqry = "SELECT nvl(P_street_name,'RCBank') AS RES_ADDR FROM CUSTOMERINFO WHERE INST_ID='"
						+ instid + "' and CIN ='" + cin + "'";

				enctrace("addressqry ::::::   " + addressqry);
				address = (String) jdbctemplate.queryForObject(addressqry,
						String.class);
				
				/*if(address.equals("")||address==null){
					address = "NA";
				}*/
			}
			return address;
		} catch (EmptyResultDataAccessException e) {
			return address;
		}
	}
	
	public String getAddress1(String instid, String productcode,
			String subproduct, String cin, String type, String addrskey,
			JdbcTemplate jdbctemplate) throws Exception {
		String address = "", addressqry = "";
		try {
			if (type.equals("INSTANT")) {

				addressqry = "SELECT NVL(ADDRESS,'--') AS ADDRESS FROM INST_TEMPNAME WHERE INST_ID='"
						+ instid
						+ "' and PRODUCT_CODE ='"
						+ productcode
						+ "' AND SUBPRODUCT ='" + subproduct + "'";
				address = (String) jdbctemplate.queryForObject(addressqry,
						String.class);
			} else {

				// addressqry = "SELECT P_PO_BOX || P_HOUSE_NO AS RES_ADDR FROM
				// CUSTOMERINFO WHERE INST_ID='" + instid
				// + "' and CIN ='" + cin + "'";

				/*addressqry = "SELECT P_street_name ||',' ||p_ward_name || ',' || c_city  AS RES_ADDR FROM CUSTOMERINFO WHERE INST_ID='"
						+ instid + "' and CIN ='" + cin + "'";
*/
				
				
				addressqry = "SELECT nvl(p_ward_name,'NA') ||','||p_city  AS RES_ADDR FROM CUSTOMERINFO WHERE INST_ID='"
						+ instid + "' and CIN ='" + cin + "'";

				enctrace("addressqry ::::::   " + addressqry);
				address = (String) jdbctemplate.queryForObject(addressqry,
						String.class);
				
				/*if(address.equals("")||address==null){
					address = "NA";
				}*/
			}
			return address;
		} catch (EmptyResultDataAccessException e) {
			return address;
		}
	}

	public String getCustomerNameByProces(String instid, String productcode,
			String subproduct, String cin, String type,
			JdbcTemplate jdbctemplate) throws Exception {
		String address = "", addressqry = "";
		try {
			if (type.equals("INSTANT")) {
				addressqry = "SELECT NVL(ENCNAME,'--') AS ENCNAME FROM INST_TEMPNAME WHERE INST_ID='"
						+ instid
						+ "' and PRODUCT_CODE ='"
						+ productcode
						+ "' AND SUBPRODUCT ='" + subproduct + "'";
				// // enctrace("3030addressqry :" + addressqry);
				address = (String) jdbctemplate.queryForObject(addressqry,
						String.class);
			} else {
				addressqry = "SELECT NVL(EMB_NAME,'--') AS ENCNAME FROM PERS_CARD_PROCESS WHERE INST_ID='"
						+ instid + "' and CIN ='" + cin + "' and rownum=1";
				// addressqry = "SELECT NVL(FNAME || ' '|| LNAME,'--') AS
				// ENCNAME FROM CUSTOMERINFO WHERE INST_ID='"+instid+"' and CIN
				// ='"+cin+"'";
				// // enctrace("3030addressqry :" + addressqry);
				address = (String) jdbctemplate.queryForObject(addressqry,
						String.class);
			}
		} catch (EmptyResultDataAccessException e) {
			return "";
		}
		return address;
	}

	public String getCustomerNameByProduction(String instid,
			String productcode, String subproduct, String cin, String type,
			JdbcTemplate jdbctemplate) throws Exception {
		String address = "", addressqry = "";
		try {
			if (type.equals("INSTANT")) {
				addressqry = "SELECT NVL(ENCNAME,'--') AS ENCNAME FROM INST_TEMPNAME WHERE INST_ID='"
						+ instid
						+ "' and PRODUCT_CODE ='"
						+ productcode
						+ "' AND SUBPRODUCT ='" + subproduct + "'";
				// // enctrace("3030addressqry :" + addressqry);
				address = (String) jdbctemplate.queryForObject(addressqry,
						String.class);
			} else {
				addressqry = "SELECT NVL(EMB_NAME,'--') AS ENCNAME FROM CARD_PRODUCTION WHERE INST_ID='"
						+ instid + "' and CIN ='" + cin + "' and rownum=1";
				// addressqry = "SELECT NVL(FNAME || ' '|| LNAME,'--') AS
				// ENCNAME FROM IFP_CUSTINFO_PRODUCTION WHERE
				// INST_ID='"+instid+"' and CIN ='"+cin+"'";
				// // enctrace("3030addressqry :" + addressqry);
				address = (String) jdbctemplate.queryForObject(addressqry,
						String.class);
			}
		} catch (EmptyResultDataAccessException e) {
			return "";
		}
		return address;
	}

	public String getProductCodeByChnProcess(String instid, String chn,
			String gentype, JdbcTemplate jdbctemplate) {
		String query = "select PRODUCT_CODE from PERS_CARD_PROCESS   where INST_ID='"
				+ instid + "' AND  CARD_NO='" + chn + "' and rownum<=1";
		if (gentype.equals("INSTANT")) {
			query = "select PRODUCT_CODE from INST_CARD_PROCESS   where INST_ID='"
					+ instid + "' AND  CARD_NO='" + chn + "' and rownum<=1";
		}
		trace("getProductCode__  " + query);
		String temp = null;

		try {
			temp = (String) jdbctemplate.queryForObject(query, String.class);
		} catch (DataAccessException e) {
			return null;
		}
		return temp;
	}

	public String getSubProductByCHNProcess(String instid, String CHN,
			String processtype, JdbcTemplate jdbctemplate) throws Exception {
		//String tablename = "CARD_PRODUCTION";
		String tablename = "PERS_CARD_PROCESS";
		System.out.println("processtype------->"+processtype);
		if (processtype.equals("INSTANT")) {
			tablename = "CARD_PRODUCTION";
		}else if (processtype.equals("CARD_PRODUCTION")) {
			tablename = "CARD_PRODUCTION";
		}

		String subprodcodeqry = "SELECT SUB_PROD_ID FROM " + tablename
				+ " WHERE INST_ID='" + instid + "' AND ORG_CHN='" + CHN
				+ "' AND ROWNUM <=1";
		enctrace("subprodcodeqry__ ::::  " + subprodcodeqry);
		String subprodcode = null;
		try {
			subprodcode = (String) jdbctemplate.queryForObject(subprodcodeqry,
					String.class);
			enctrace("subprodcode__" + subprodcode);

		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			subprodcode = NOREC;
		}
		return subprodcode;

	}

	public String getCustomerIdByCardNumberProcess(String instid,
			String cardno, String processtype, JdbcTemplate jdbctemplate)
			throws Exception {
		String customerid = null;

		String tablename = "PERS_CARD_PROCESS";
		if (processtype.equals("INSTANT")) {
			tablename = "INST_CARD_PROCESS";
		}
		try {

			/*
			 * String customeridqry = "SELECT NVL(CIN,'00') AS CIN FROM " +
			 * tablename + " WHERE INST_ID='" + instid+ "' AND  ORG_CHN='" +
			 * cardno + "' AND ROWNUM<=1"; enctrace("3030customeridqry : " +
			 * customeridqry); customerid = (String)
			 * jdbctemplate.queryForObject(customeridqry, String.class);
			 */

			// by gowtham-270819
			String customeridqry = "SELECT NVL(CIN,'00') AS CIN FROM "
					+ tablename
					+ " WHERE INST_ID=? AND  ORG_CHN=? AND ROWNUM<=?";
			enctrace("3030customeridqry : " + customeridqry);
			customerid = (String) jdbctemplate.queryForObject(customeridqry,
					new Object[] { instid, cardno, "1" }, String.class);

		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
		}
		return customerid;
	}

	public Map getBinAttributes(String instid, String bin,
			JdbcTemplate jdbctemplate) throws Exception {
		Map mastermp = new HashMap();
		String pinmailerqry = "SELECT PINMAILER_ID, PVK, HSM_ID FROM PRODUCTINFO WHERE INST_ID='"
				+ instid + "' AND BIN='" + bin + "'";
		// // enctrace("3030pinmailerqry : "+pinmailerqry );
		List pinmailerattr = jdbctemplate.queryForList(pinmailerqry);
		if (!pinmailerattr.isEmpty()) {
			Iterator itr = pinmailerattr.iterator();
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				mastermp.put("PINMAILER_ID", (String) mp.get("PINMAILER_ID"));
				mastermp.put("PVK", (String) mp.get("PVK"));
				mastermp.put("HSM_ID", (String) mp.get("HSM_ID"));
			}
		} else {
			return null;
		}
		return mastermp;
	}

	public Map getHsmDetails(String instid, String hsmid,
			JdbcTemplate jdbctemplate) throws Exception {
		Map mastermp = new HashMap();
		String hsmidqry = "SELECT HSMADDRESS, HSMPORT, HSMTIMEOUT FROM HSM_DETAILS WHERE  HSM_ID='"
				+ hsmid + "'";
		// // enctrace("3030hsmidqry : "+hsmidqry);
		List hsmattr = jdbctemplate.queryForList(hsmidqry);
		if (!hsmattr.isEmpty()) {
			Iterator itr = hsmattr.iterator();
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				mastermp.put("HSMADDRESS", (String) mp.get("HSMADDRESS"));
				mastermp.put("HSMPORT", (Object) mp.get("HSMPORT"));
				mastermp.put("HSMTIMEOUT", (Object) mp.get("HSMTIMEOUT"));

			}
		} else {
			return null;
		}
		return mastermp;
	}

	public String getDualAuthEnabledForSuperAdmin(JdbcTemplate jdbctemplate)
			throws Exception {
		String dualauth = "Y";
		String dualauthchekqry = "SELECT DUAL_AUTH_ENABLE FROM IFP_ADMIN_MAKERCHECKER";
		try {
			dualauth = (String) jdbctemplate.queryForObject(dualauthchekqry,
					String.class);
		} catch (Exception e) {
		}
		return dualauth;
	}

	public String getHcardNo(String cardNo, JdbcTemplate jdbcTemplate) {

		String hashCardNo = "";

		/*
		 * String hashCardQry =
		 * "SELECT HCARD_NO FROM  PERS_CARD_PROCESS_HASH WHERE" +
		 * " ORDER_REF_NO=(SELECT ORDER_REF_NO FROM PERS_CARD_PROCESS WHERE ORG_CHN='"
		 * + cardNo + "') "; try { hashCardNo = (String)
		 * jdbcTemplate.queryForObject(hashCardQry, String.class);
		 */

		// by gowtham-270819
		String hashCardQry = "SELECT HCARD_NO FROM  PERS_CARD_PROCESS_HASH WHERE"
				+ " ORDER_REF_NO=(SELECT ORDER_REF_NO FROM PERS_CARD_PROCESS WHERE ORG_CHN=?) ";
		try {
			hashCardNo = (String) jdbcTemplate.queryForObject(hashCardQry,
					new Object[] { cardNo }, String.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return hashCardNo;
	}

	public String getAdminMkrChkrStatus(JdbcTemplate jdbctemplate)
			throws Exception {
		String dualauth = "Y";
		String dualauthchekqry = "SELECT DUAL_AUTH_ENABLE FROM ADMIN_MAKERCHECKER";
		try {
			dualauth = (String) jdbctemplate.queryForObject(dualauthchekqry,
					String.class);
		} catch (Exception e) {
		}
		return dualauth;
	}

	public List getPADSSKEY(JdbcTemplate jdbctemplate) throws Exception {
		List padsskey = null;
		String padssqry = "SELECT KEYID,KEYDESC FROM PADSSKEY WHERE AUTH_CODE='1' ";
		try {
			padsskey = jdbctemplate.queryForList(padssqry);
		} catch (Exception e) {
		}
		return padsskey;
	}

	public List getPADSSDetailById(String keyid, JdbcTemplate jdbctemplate)
			throws Exception {
		List padsskey = null;
		try {
			String padssqry = "SELECT DMK ,SALT_KEY FROM PADSSKEY WHERE KEYID = '"
					+ keyid + "' AND AUTH_CODE='1' ";

			padsskey = jdbctemplate.queryForList(padssqry);
			enctrace("padssqry>==============<" + padssqry);

			enctrace("end..  000000000000000000000000");

			// by gowtham-200819
			// String padssqry = "SELECT DMK ,SALT_KEY FROM PADSSKEY WHERE KEYID
			// =? AND AUTH_CODE=? ";

			// padsskey = jdbctemplate.queryForList(padssqry, new Object[] {
			// keyid, "1" });

		} catch (Exception e) {
			e.printStackTrace();
		}
		return padsskey;
	}

	// by siva
	public String getPADSSDetailById111(String keyid, JdbcTemplate jdbctemplate)
			throws Exception {
		String padsskey = null;
		String padssqry = "SELECT DMK FROM PADSSKEY WHERE KEYID = '" + keyid
				+ "' AND AUTH_CODE='1' ";
		enctrace("getPADSSDetailById::" + padssqry);
		try {
			padsskey = (String) jdbctemplate.queryForObject(padssqry,
					String.class);
		} catch (Exception e) {
		}
		return padsskey;
	}

	// by siva

	// PAVITHRA PATCH
	public String getProductdesc(String instid, String prodcode,
			JdbcTemplate jdbctemplate) {
		String bin_desc = null;

		/*
		 * String qryproductdesc =
		 * "SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID='" + instid+
		 * "' and PRODUCT_CODE='" + prodcode + "'";
		 */

		// / by gowtham-280819
		String qryproductdesc = "SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID=? and PRODUCT_CODE=?";
		trace(" getProductdesc " + qryproductdesc
				+ "jdbctemplate.getMaxRows()===> " + jdbctemplate.getMaxRows());
		if (jdbctemplate.getMaxRows() == 0) {
			trace(" Entered IF condition ");
			try {
				/*
				 * bin_desc = (String)
				 * jdbctemplate.queryForObject(qryproductdesc, String.class);
				 */
				bin_desc = (String) jdbctemplate.queryForObject(qryproductdesc,
						new Object[] { instid, prodcode }, String.class);

				trace(" BIN DESCRIPTION " + bin_desc);
			} catch (EmptyResultDataAccessException e) {
			}
			return bin_desc;
		} else {
			return "UNKNOWN PRODUCT";
		}
	}

	// pritto

	// made by siva 24-10-2018

	public String getProductdesc1(String instid, String bin,
			JdbcTemplate jdbctemplate) {
		String bin_desc = null;

		String qryproductdesc = "SELECT BIN_FLAG FROM PRODUCT_MASTER WHERE INST_ID='"
				+ instid + "' and PRODUCT_CODE='" + bin + "'";
		trace(" getProductdesc " + qryproductdesc
				+ "jdbctemplate.getMaxRows()===> " + jdbctemplate.getMaxRows());
		if (jdbctemplate.getMaxRows() == 0) {
			trace(" Entered IF condition ");
			try {
				bin_desc = (String) jdbctemplate.queryForObject(qryproductdesc,
						String.class);
				trace(" BIN DESCRIPTION " + bin_desc);
			} catch (EmptyResultDataAccessException e) {
			}
			return bin_desc;
		} else {
			return "UNKNOWN PRODUCT";

		}

		/*
		 * // by gowtham-260819 String qryproductdesc =
		 * "SELECT BIN_FLAG FROM PRODUCT_MASTER WHERE INST_ID=? and PRODUCT_CODE=? "
		 * ; trace(" getProductdesc " + qryproductdesc +
		 * "jdbctemplate.getMaxRows()===> " + jdbctemplate.getMaxRows()); if
		 * (jdbctemplate.getMaxRows() == 0) { trace(" Entered IF condition ");
		 * try { bin_desc = (String) jdbctemplate.queryForObject(qryproductdesc,
		 * new Object[] { instid, bin }, String.class); trace(
		 * " BIN DESCRIPTION " + bin_desc); } catch
		 * (EmptyResultDataAccessException e) { } return bin_desc; } else {
		 * return "UNKNOWN PRODUCT"; }
		 */

	}

	// made by siva 24-10-2018

	public String getListOfSubProduct(String instid, String prodid,
			String authstatus, JdbcTemplate jdbctemplate) throws Exception {

		/*
		 * String query =
		 * "select SUB_PRODUCT_NAME, SUB_PROD_ID from INSTPROD_DETAILS where inst_id='"
		 * + instid+ "' and PRODUCT_CODE='" + prodid + "' AND AUTH_STATUS='" +
		 * authstatus + "'"; enctrace("listofsubproduct  " + query); List
		 * subprodlist = jdbctemplate.queryForList(query);
		 */

		// by gowtham-210819
		String query = "select SUB_PRODUCT_NAME, SUB_PROD_ID from INSTPROD_DETAILS where inst_id=? and PRODUCT_CODE=? AND AUTH_STATUS=?";
		enctrace("listofsubproduct  " + query);
		List subprodlist = jdbctemplate.queryForList(query, new Object[] {
				instid, prodid, authstatus });

		Iterator itr = subprodlist.iterator();
		StringBuilder result = new StringBuilder();
		result.append("<option value='-1'> - Select - </option>");
		while (itr.hasNext()) {
			Map map = (Map) itr.next();
			String maxallowedcard = (String) map.get("NO_NONPERCARD_ALLOWED");
			String subprodname = (String) map.get("SUB_PRODUCT_NAME");
			String subprodid = (String) map.get("SUB_PROD_ID");
			result = result.append("<option value='" + subprodid + "'>"
					+ subprodname + "</option>");
			// result = result + max;
			System.out.println("resutl---->"+result);
			trace("maxallowedcard-->"+maxallowedcard);
			trace("subprodname--->"+subprodname);
			trace("subprodid==>>"+subprodid);
		}

		return result.toString();

	}

	public String dateGreatcurrentDate(String currentdate,
			JdbcTemplate jdbctemplate) throws Exception {

		StringBuilder query = new StringBuilder();
		query.append("select  case when trunc(sysdate) >trunc(to_date('"
				+ currentdate + "','dd-mm-yyyy')) then 'TRUE' ");
		query.append("ELSE '''"
				+ currentdate
				+ "''' ||'-'   ||'Entered Date is Greater than Current Date ' || ''''||to_char(sysdate,'DD-MM-YYYY')||'''' ");
		query.append("end VALID_DATE from dual ");
		enctrace("dateGreatcurrentDate  " + query.toString());
		String dateqryResult = (String) jdbctemplate.queryForObject(
				query.toString(), String.class);
		return dateqryResult;

	}

	public List getCurList(String instid, JdbcTemplate jdbctemplate)
			throws Exception {
		List curlist = null;
		try {

			/*
			 * String curDetails_query =
			 * "SELECT NUMERIC_CODE, CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE AUTH_CODE='1'  "
			 * ; //// enctrace("3030curDetails_query :" + curDetails_query );
			 * curlist = jdbctemplate.queryForList(curDetails_query);
			 */

			// by gowtham210819
			String curDetails_query = "SELECT NUMERIC_CODE, CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE AUTH_CODE=?  ";
			// // enctrace("3030curDetails_query :" + curDetails_query );
			curlist = jdbctemplate.queryForList(curDetails_query,
					new Object[] { "1" });

		} catch (Exception e) {
			trace("the exception is" + e.getMessage());
		}
		return curlist;
	}

	public String getProductDesc(String instid, String prodcode,
			JdbcTemplate jdbctemplate) throws Exception {
		String productdesc = null;

		/*
		 * String qryproductdesc =
		 * "SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID='" + instid+
		 * "' and PRODUCT_CODE='" + prodcode + "'"; trace(
		 * " getProductdesc john " + qryproductdesc); try { productdesc =
		 * (String) jdbctemplate.queryForObject(qryproductdesc, String.class);
		 */

		// by gowtham-210819
		String qryproductdesc = "SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID=? and PRODUCT_CODE=? ";
		trace(" getProductdesc john " + qryproductdesc);
		try {
			productdesc = (String) jdbctemplate.queryForObject(qryproductdesc,
					new Object[] { instid, prodcode }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return productdesc;
	}

	public int getProductCountForBin(String instid, String bin,
			JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;

		/*
		 * String bincountqry =
		 * "SELECT COUNT(*) FROM PRODUCT_MASTER WHERE INST_ID='" + instid +
		 * "' AND BIN='" + bin + "'"; //// enctrace("3030bincountqry :" +
		 * bincountqry ); x = jdbctemplate.queryForInt(bincountqry);
		 */

		// by gowtham-190819
		String bincountqry = "SELECT COUNT(*) FROM PRODUCT_MASTER WHERE INST_ID=? AND BIN=?";
		// // enctrace("3030bincountqry :" + bincountqry );
		x = jdbctemplate.queryForInt(bincountqry, new Object[] { instid, bin });

		return x;
	}

	public static String padRightChar(String s, int n, String padchar) {
		if (n > 0) {
			int strlen = s.length();
			for (int i = 0; i < n - strlen; i++) {
				s = s + padchar;
			}
		} else {
			return s;
		}
		return s;
	}

	public String checkPersonalizeFileExist(String instid, String cardno,
			String tablename, JdbcTemplate jdbctemplate) {
		String filename = null;
		try {
			String fileexistqry = "SELECT PRE_NAME FROM " + tablename
					+ " WHERE INST_ID='" + instid + "' AND CARD_NO='" + cardno
					+ "'";

			filename = (String) jdbctemplate.queryForObject(fileexistqry,
					String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return filename;

	}

	public String getvalidbranch(String instid, String hcardno, String maptype,
			JdbcTemplate jdbctemplate) {
		String BRANCH_CODE = null;
		String condi = "";
		if (maptype == "ORDERREFNO") {
			condi = "AND ORDER_REF_NO='" + hcardno + "'";
		} else {
			condi = "AND HCARD_NO='" + hcardno + "'";
		}
		try {

			/*
			 * String BRANCHCODE =
			 * "SELECT trim(CARD_COLLECT_BRANCH) FROM INST_CARD_PROCESS " +
			 * "WHERE INST_ID='" + instid+ "' " + condi + " "; enctrace(
			 * "3030BRANCH_CODE FOR CBS VALID:" + BRANCHCODE); BRANCH_CODE =
			 * (String) jdbctemplate.queryForObject(BRANCHCODE, String.class);
			 */

			// // by gowtham-280819
			String BRANCHCODE = "SELECT trim(CARD_COLLECT_BRANCH) FROM INST_CARD_PROCESS "
					+ "WHERE INST_ID=? " + condi + " ";
			enctrace("3030BRANCH_CODE FOR CBS VALID:" + BRANCHCODE);
			BRANCH_CODE = (String) jdbctemplate.queryForObject(BRANCHCODE,
					new Object[] { instid }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return BRANCH_CODE;

	}
	public String getvalidbranchinstant(String instid, String ecardno, String maptype,
			JdbcTemplate jdbctemplate) {
		String BRANCH_CODE = null;
		 
		try {

			 ///REFERED BY getvalidbranch METHOD IN COMMONDESC
			String BRANCHCODE = "SELECT trim(CARD_COLLECT_BRANCH) FROM INST_CARD_PROCESS "
					+ "WHERE INST_ID=?   and ORG_CHN=? ";
			enctrace("3030BRANCH_CODE FOR CBS VALID:" + BRANCHCODE);
			BRANCH_CODE = (String) jdbctemplate.queryForObject(BRANCHCODE,
					new Object[] { instid,ecardno }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return BRANCH_CODE;

	}

	public String getvalidbranchaddonaccount(String instid, String hcardno,
			JdbcTemplate jdbctemplate) {
		String BRANCH_CODE = null;
		try {

			/*
			 * String BRANCHCODE =
			 * "SELECT trim(CARD_COLLECT_BRANCH) FROM CARD_PRODUCTION " +
			 * "WHERE INST_ID='" + instid+
			 * "' AND ORDER_REF_NO=(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH "
			 * + "WHERE HCARD_NO='" + hcardno+ "')"; enctrace(
			 * "getvalidbranchaddonaccount FOR CBS VALID:" + BRANCHCODE);
			 * BRANCH_CODE = (String) jdbctemplate.queryForObject(BRANCHCODE,
			 * String.class);
			 */

			// / by gowtham-300819
			String BRANCHCODE = "SELECT trim(CARD_COLLECT_BRANCH) FROM CARD_PRODUCTION "
					+ "WHERE INST_ID=? AND ORDER_REF_NO=(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH "
					+ "WHERE HCARD_NO=?)";
			enctrace("getvalidbranchaddonaccount FOR CBS VALID:" + BRANCHCODE);
			BRANCH_CODE = (String) jdbctemplate.queryForObject(BRANCHCODE,
					new Object[] { instid, hcardno }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return BRANCH_CODE;

	}

	public String getReissuvalidbranch(String instid, String hcardno,
			JdbcTemplate jdbctemplate) {
		String BRANCH_CODE = null;
		try {

			/*
			 * String BRANCHCODE =
			 * "SELECT trim(CARD_COLLECT_BRANCH) FROM CARD_PRODUCTION WHERE INST_ID='"
			 * + instid+ "' AND HCARD_NO='" + hcardno + "'"; enctrace(
			 * "3030BRANCH_CODE FOR CBS VALID:" + BRANCHCODE); BRANCH_CODE =
			 * (String) jdbctemplate.queryForObject(BRANCHCODE, String.class);
			 */

			// / by gowtham-280819
			String BRANCHCODE = "SELECT trim(CARD_COLLECT_BRANCH) FROM CARD_PRODUCTION WHERE INST_ID=? AND HCARD_NO=? ";
			enctrace("3030BRANCH_CODE FOR CBS VALID:" + BRANCHCODE);
			BRANCH_CODE = (String) jdbctemplate.queryForObject(BRANCHCODE,
					new Object[] { instid, hcardno }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return BRANCH_CODE;

	}

	public String getorderrefstaus(String instid, String masterorderno,
			JdbcTemplate jdbctemplate) {
		String BRANCH_CODE = null;
		try {

			/*
			 * String BRANCHCODE =
			 * "select CARD_STATUS||MKCK_STATUS from INST_CARD_PROCESS WHERE INST_ID='"
			 * + instid+ "' AND ORDER_REF_NO='" + masterorderno + "'";
			 * enctrace("getorderrefstaus:" + BRANCHCODE); BRANCH_CODE =
			 * (String) jdbctemplate.queryForObject(BRANCHCODE, String.class);
			 */

			// / by gowtham-280819
			String BRANCHCODE = "select CARD_STATUS||MKCK_STATUS from INST_CARD_PROCESS WHERE INST_ID=? AND ORDER_REF_NO=? ";
			enctrace("getorderrefstaus:" + BRANCHCODE);
			BRANCH_CODE = (String) jdbctemplate.queryForObject(BRANCHCODE,
					new Object[] { instid, masterorderno }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return BRANCH_CODE;

	}

	public List newcardnofororder(String instid, String masterorderno,
			JdbcTemplate jdbctemplate) {
		List cardcollect = null;

		/*
		 * String cardcollectqry =
		 * "select BIN,HCARD_NO,trim(CARD_COLLECT_BRANCH) AS  CARD_COLLECT_BRANCH from INST_CARD_PROCESS WHERE INST_ID='"
		 * + instid +
		 * "' AND CARD_STATUS='05' and MKCK_STATUS='P'  AND ORDER_REF_NO='" +
		 * masterorderno + "'"; trace("cardcollectqrdsady" + cardcollectqry);
		 * 
		 * try { cardcollect = jdbctemplate.queryForList(cardcollectqry);
		 */

		// by gowtham-280819
		String cardcollectqry = "select BIN,HCARD_NO,trim(CARD_COLLECT_BRANCH) AS  CARD_COLLECT_BRANCH from INST_CARD_PROCESS "
				+ "WHERE INST_ID=? AND CARD_STATUS=? and MKCK_STATUS=?  AND ORDER_REF_NO=? ";
		trace("cardcollectqrdsady" + cardcollectqry);

		try {
			cardcollect = jdbctemplate.queryForList(cardcollectqry,
					new Object[] { instid, "05", "P", masterorderno });

		} catch (EmptyResultDataAccessException e) {
		}
		return cardcollect;
	}

	public int getReissuvalidcardavailable(String instid, String hcardno,
			JdbcTemplate jdbctemplate) {
		int x = -1;

		/*
		 * String cardavailable =
		 * "SELECT COUNT(1) FROM CARD_PRODUCTION WHERE INST_ID='" + instid +
		 * "' AND HCARD_NO='"+ hcardno + "'"; enctrace("getcardreissucardvalid:"
		 * + cardavailable); x = jdbctemplate.queryForInt(cardavailable);
		 */

		// / by gowtham-280819
		String cardavailable = "SELECT COUNT(1) FROM CARD_PRODUCTION WHERE INST_ID=? AND HCARD_NO=? ";
		enctrace("getcardreissucardvalid:" + cardavailable);
		x = jdbctemplate.queryForInt(cardavailable, new Object[] { instid,
				hcardno });

		System.out.println("ssssss" + x);
		return x;
	}

	public int getcardreissucardvalid(String instid, String hcardno,
			JdbcTemplate jdbctemplate) {
		int x = 0;

		/*
		 * String cardvalid = "SELECT COUNT(*) FROM INST_CARD_PROCESS " +
		 * "WHERE INST_ID='" + instid+
		 * "'  AND CARD_STATUS='05' AND MKCK_STATUS='P' AND HCARD_NO='" +
		 * hcardno + "'"; enctrace("getcardreissucardvalid:" + cardvalid); x =
		 * jdbctemplate.queryForInt(cardvalid);
		 */

		// / by gowtham-280819
		String cardvalid = "SELECT COUNT(*) FROM INST_CARD_PROCESS "
				+ "WHERE INST_ID=?  AND CARD_STATUS=? AND MKCK_STATUS=? AND HCARD_NO=? ";
		enctrace("getcardreissucardvalid:" + cardvalid);
		x = jdbctemplate.queryForInt(cardvalid, new Object[] { instid, "05",
				"P", hcardno });
		return x;
	}

	public List getcardcollectbranch(String instid, String hcardno,
			JdbcTemplate jdbctemplate) {
		List cardcollect = null;

		/*
		 * String cardcollectqry =
		 * "SELECT ORDER_REF_NO,trim(card_collect_branch) AS card_collect_branch FROM INST_CARD_PROCESS"
		 * + " WHERE INST_ID='"+ instid + "' AND HCARD_NO='" + hcardno + "'";
		 * trace("cardcollectqrdsady" + cardcollectqry);
		 * 
		 * try { cardcollect = jdbctemplate.queryForList(cardcollectqry);
		 */

		// / by gowtham-280819
		String cardcollectqry = "SELECT ORDER_REF_NO,trim(card_collect_branch) AS card_collect_branch FROM INST_CARD_PROCESS"
				+ " WHERE INST_ID=? AND HCARD_NO=? ";
		trace("cardcollectqrdsady" + cardcollectqry);

		try {
			cardcollect = jdbctemplate.queryForList(cardcollectqry,
					new Object[] { instid, hcardno });

		} catch (EmptyResultDataAccessException e) {
		}
		return cardcollect;
	}

	public String getLimitDesc(String instid, String limitid,
			JdbcTemplate jdbctemplate) {
		String limitname = null;

		// by gowtham-200819
		// String limitnameqry = "SELECT LIMIT_DESC FROM LIMIT_DESC WHERE
		// INST_ID=? AND LIMIT_ID=?";
		String limitnameqry = "SELECT LIMIT_DESC FROM LIMIT_DESC WHERE INST_ID='"
				+ instid + "' AND LIMIT_ID='" + limitid + "'";

		trace("limitnameqry__" + limitnameqry);

		try {

			limitname = (String) jdbctemplate.queryForObject(limitnameqry,
					String.class);
			// limitname = (String) jdbctemplate.queryForObject(limitnameqry,
			// new Object[] { instid, limitid },
			// String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return limitname;
	}

	public String getLimitAccountFlag(String instid, String limitid,
			JdbcTemplate jdbctemplate) {
		String limitname = null;

		// String limitnameqry = "SELECT LIMITTXNTYPE FROM LIMIT_DESC WHERE
		// INST_ID='" + instid + "' AND LIMIT_ID='"+ limitid + "'";
		// by gowtham-200819
		String limitnameqry = "SELECT LIMITTXNTYPE FROM LIMIT_DESC WHERE INST_ID=? AND LIMIT_ID=?";

		trace("getLimitAccountFlag" + limitnameqry);

		try {

			// limitname = (String) jdbctemplate.queryForObject(limitnameqry,
			// String.class);
			limitname = (String) jdbctemplate.queryForObject(limitnameqry,
					new Object[] { instid, limitid }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return limitname;
	}

	public String getLimitCurrency(String instid, String limitid,
			JdbcTemplate jdbctemplate) {
		String limitname = null;

		// by gowtham
		// String limitnameqry = "SELECT CUR_CODE FROM LIMIT_DESC WHERE
		// INST_ID='" + instid + "' AND LIMIT_ID='" + limitid+ "'";
		String limitnameqry = "SELECT CUR_CODE FROM LIMIT_DESC WHERE INST_ID=? AND LIMIT_ID=?";

		trace("getLimitAccountFlag" + limitnameqry);

		try {

			// limitname = (String) jdbctemplate.queryForObject(limitnameqry,
			// String.class);
			limitname = (String) jdbctemplate.queryForObject(limitnameqry,
					new Object[] { instid, limitid }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return limitname;
	}

	public List getLimitDetails(String instid, String limitid,
			JdbcTemplate jdbctemplate) throws Exception {
		List limitdata = null;
		String limitdetailsqry = "SELECT DECODE(LIMITTYPE,'CDTP','SUB-PRODUCT BASED', 'CRD','OWN-LIMIT', LIMITTYPE ) AS LIMIT_TYPE, TXNCODE,  COUNT AS LIMIT_COUNT, AMOUNT AS LIMIT_AMOUNT FROM LIMITINFO WHERE INSTID='"
				+ instid + "' AND  LIMITID='" + limitid + "'";

		enctrace("limitdetailsqry===." + limitdetailsqry);
		limitdata = jdbctemplate.queryForList(limitdetailsqry);
		return limitdata;
	}

	public List getLimitReachedDetails(String instid, String limitid,
			String txncode, JdbcTemplate jdbctemplate) throws Exception {
		List limitdata = null;
		String limitdetailsqry = "SELECT ACCUM_AMOUNT, ACCUM_COUNT FROM IFP_ACCUMINFO WHERE INST_ID='"
				+ instid
				+ "' AND TXNCODE='"
				+ txncode
				+ "' AND LIMIT_ID='"
				+ limitid + "'";
		// // enctrace("3030limitdetailsqry :"+ limitdetailsqry );
		limitdata = jdbctemplate.queryForList(limitdetailsqry);
		return limitdata;
	}

	public String formatCurrency(String strval) {
		if (strval == null) {
			return strval;
		}
		BigDecimal bigdecimaldatavalue = new BigDecimal(strval);
		DecimalFormat myFormatter = new DecimalFormat("#,##0.00");
		String s = myFormatter.format(bigdecimaldatavalue.floatValue());
		return s;
	}

	public String formatCurrency(BigDecimal d) {
		DecimalFormat myFormatter = new DecimalFormat("#,##0.00");
		String s = myFormatter.format(d.floatValue());
		return s;
	}

	public String getAvailableBalance(String instid, String cardno,
			JdbcTemplate jdbctemplate) throws Exception {
		String accdetails = null;
		String accnt = "select ACCT_NO from IFD_CARD_ACCT_LINK where card_no='"
				+ cardno + "' AND INST_ID='" + instid + "'";
		// // enctrace("3030The accnt is " + accnt);
		List accnt_result = jdbctemplate.queryForList(accnt);
		trace("accnt_result.size()  " + accnt_result.size()
				+ "/n  accnt_result.size()" + accnt_result);
		Iterator itraccnt = accnt_result.iterator();
		if (!accnt_result.isEmpty()) {
			while (itraccnt.hasNext()) {
				Map map = (Map) itraccnt.next();
				trace("Nect to map");
				String acctno = ((String) map.get("ACCT_NO"));
				trace("acctno	" + acctno);
				trace("account Number " + acctno);
				// AVAILABLE BALANCE
				String account_details = "SELECT TO_CHAR(AVAIL_BALANCE) AS  AVAIL_BALANCE,ACCT_CCY FROM ACCOUNTINFO WHERE ACCT_NO='"
						+ acctno + "' AND INST_ID='" + instid + "'";
				enctrace(account_details);
				List account_details_result = jdbctemplate
						.queryForList(account_details);
				trace("account_details_result length "
						+ account_details_result.size());
				Iterator itr_account_details = account_details_result
						.iterator();
				while (itr_account_details.hasNext()) {
					Map mapaccount_details = (Map) itr_account_details.next();
					String availbalance = ((String) mapaccount_details
							.get("AVAIL_BALANCE"));
					String txncur = ((String) mapaccount_details
							.get("ACCT_CCY"));
					String txncurstr = this.getCurrencyAlphaCode(txncur,
							jdbctemplate);
					accdetails = availbalance + " " + txncurstr;
				}
			}
		}
		return accdetails;
	}

	public String getWebPortalUsername(String instid, String cardno,
			JdbcTemplate jdbctemplate) {
		String username = null;
		try {
			String usernameqry = "select USERNAME from IFPS_ADDON_CARDS where INST_ID='"
					+ instid + "' and card_no='" + cardno + "' ";
			username = (String) jdbctemplate.queryForObject(usernameqry,
					String.class);
		} catch (DataAccessException e) {
		}
		return username;
	}

	public String checkEntityValue(String instid, String cardno,
			JdbcTemplate jdbctemplate) throws Exception {
		String entitytype = null;
		try {
			String entityqry = "SELECT ENTITY_ID FROM IFP_CARD_ENTITY_MAP WHERE INST_ID='"
					+ instid + "' AND CARDNO='" + cardno + "' AND ROWNUM<=1";
			// // enctrace("3030entityqry : " + entityqry );
			entitytype = (String) jdbctemplate.queryForObject(entityqry,
					String.class);
		} catch (Exception e) {
		}
		return entitytype;
	}

	public String getEntityDesc(String entitykey, JdbcTemplate jdbctemplate)
			throws Exception {
		String entitytype = null;
		try {
			String entityqry = "SELECT TXNKEYDESC FROM IFD_GL_KEYS WHERE TXNKEY='"
					+ entitykey + "' AND ROWNUM<=1";
			// // enctrace("3030entityqry : " + entityqry );
			entitytype = (String) jdbctemplate.queryForObject(entityqry,
					String.class);
		} catch (Exception e) {
		}
		return entitytype;
	}

	public String checkEntityValueType(String instid, String cardno,
			JdbcTemplate jdbctemplate) throws Exception {
		String entitytype = null;
		try {
			String entityqry = "SELECT ENTITY_TYPE FROM IFP_CARD_ENTITY_MAP WHERE INST_ID='"
					+ instid + "' AND CARDNO='" + cardno + "' AND ROWNUM<=1";
			// // enctrace("3030entityqry : " + entityqry );
			entitytype = (String) jdbctemplate.queryForObject(entityqry,
					String.class);
		} catch (Exception e) {
		}
		return entitytype;
	}

	public String generateRandomNumber(int charLength) {
		return String.valueOf(charLength < 1 ? 0 : new Random()
				.nextInt((9 * (int) Math.pow(10, charLength - 1)) - 1)
				+ (int) Math.pow(10, charLength - 1));
	}

	public List getCustomerdetailsByCIN(String instid, String cin,
			JdbcTemplate jdbcTemplate) throws Exception {

		List custdetails = null;

		String custinfo_qury = "select  FNAME, LNAME, FNAME||' '||decode(MNAME,'NO_DATA','',MNAME)||' '||LNAME as FULNAME,FATHER_NAME,MOTHER_NAME,decode(MARITAL_STATUS,'M','Married','U','Unmarried') as MARITAL_STATUS, MARITAL_STATUS AS MARITAL_STATUS1, "
				+ "decode(GENDER,'M','Male','F','Female') as GENDER, GENDER AS GENDER1, to_char(DOB,'DD-MM-YYYY') as DOB,NATIONALITY,EMAIL_ADDRESS,MOBILE_NO,PHONE_NO,OCCUPATION,ID_NUMBER,ID_DOCUMENT,"
				+ "POST_ADDR1 as pone,POST_ADDR2 as ptwo,POST_ADDR3 as pthree,POST_ADDR4 as pfour,RES_ADDR1 as rone,RES_ADDR2 as rtwo,RES_ADDR3 as rthree,RES_ADDR4 as rfour,to_char(MAKER_DATE,'DD-MON-YY') as MAKER_DATE from IFP_CUSTINFO_PRODUCTION "

				/* + "where INST_ID='" + instid + "' and  CIN='" + cin + "'"; */
				+ "where INST_ID=? and  CIN=? ";

		// // enctrace("3030custinfo_qury :"+custinfo_qury);
		custdetails = jdbcTemplate.queryForList(custinfo_qury, new Object[] {
				instid, cin });
		ListIterator itr = custdetails.listIterator();

		while (itr.hasNext()) {
			Map custdet = (Map) itr.next();
			String nationid = (String) custdet.get("NATIONALITY");
			String nation = getNation(nationid, jdbcTemplate);
			custdet.put("NATION", nation);
			itr.remove();
			itr.add(custdet);
		}

		return custdetails;
	}

	public String getPhotoForCustomerProcess(String instid, String CHN,
			String processtype, JdbcTemplate jdbctemplate) throws Exception {
		String customerphoto = null;
		String cin = this.getCustomerIdByCardNumberProcess(instid, CHN,
				processtype, jdbctemplate);
		String customerphotoqry = "SELECT MOBILE FROM CUSTOMERINFO WHERE INST_ID='"
				+ instid + "' AND CIN='" + cin + "'";
		// // enctrace("3030customerphotoqry :"+customerphotoqry);
		try {
			customerphoto = (String) jdbctemplate.queryForObject(
					customerphotoqry, String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return customerphoto;
	}

	public String getPhotoForCustomerProduction(String instid, String CHN,
			JdbcTemplate jdbctemplate) throws Exception {
		String customerphoto = null;
		String cin = this.getCustomerIdByCardNumber(instid, CHN, jdbctemplate);
		String customerphotoqry = "SELECT PHOTO_URL FROM IFP_CUSTINFO_PRODUCTION WHERE INST_ID='"
				+ instid + "' AND CIN='" + cin + "'";
		// // enctrace("3030customerphotoqry :"+customerphotoqry);
		try {
			customerphoto = (String) jdbctemplate.queryForObject(
					customerphotoqry, String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return customerphoto;
	}

	public String getCardIssueTypeByOrder(String instid, String order_ref_no,
			String processtype, JdbcTemplate jdbctemplate) throws Exception {
		String cardissuetype = null;
		try {

			/*
			 * String cardissuetypeqry =
			 * "SELECT CARDISSUETYPE  FROM PERS_CARD_ORDER " + "WHERE INST_ID='"
			 * + instid+ "' AND ORDER_REF_NO='" + order_ref_no +
			 * "' AND ROWNUM<=1"; //// enctrace("3030cardissuetypeqry : " +
			 * cardissuetypeqry ); cardissuetype = (String)
			 * jdbctemplate.queryForObject(cardissuetypeqry, String.class);
			 */

			// by gowtham-220819
			String cardissuetypeqry = "SELECT CARDISSUETYPE  FROM PERS_CARD_ORDER "
					+ "WHERE INST_ID=? AND ORDER_REF_NO=? AND ROWNUM<=?";
			// // enctrace("3030cardissuetypeqry : " + cardissuetypeqry );
			cardissuetype = (String) jdbctemplate.queryForObject(
					cardissuetypeqry,
					new Object[] { instid, order_ref_no, "1" }, String.class);

		} catch (Exception e) {
		}
		return cardissuetype;
	}

	/*
	 * public String getParentCardNumberByOrder(String instid, String
	 * order_ref_no, String processtype, JdbcTemplate jdbctemplate) throws
	 * Exception { String parentcardno = null; try { String parentcardnoqry =
	 * "SELECT PARENTCARD  FROM PERS_CARD_ORDER WHERE INST_ID='" + instid +
	 * "' AND ORDER_REF_NO='" + order_ref_no + "' AND ROWNUM<=1"; //// enctrace(
	 * "3030parentcardnoqry : " + parentcardnoqry ); parentcardno = (String)
	 * jdbctemplate.queryForObject(parentcardnoqry, String.class); } catch
	 * (Exception e) { } return parentcardno; }
	 */

	/**
	 * 
	 * @param instid
	 * @param order_ref_no
	 * @param processtype
	 * @param jdbctemplate
	 * @return
	 * @throws Exception
	 */

	public String getParentCardNumberByOrder(String instid,
			String order_ref_no, String processtype, JdbcTemplate jdbctemplate)
			throws Exception {

		String parentcardno = null;
		try {

			/*
			 * String parentcardnoqry =
			 * "SELECT PARENTCARD  FROM PERS_CARD_ORDER " + "WHERE INST_ID='" +
			 * instid+ "' AND ORDER_REF_NO='" + order_ref_no + "' AND ROWNUM<=1"
			 * ; parentcardno = (String)
			 * jdbctemplate.queryForObject(parentcardnoqry, String.class);
			 */

			// by gowtham-220819
			String parentcardnoqry = "SELECT PARENTCARD  FROM PERS_CARD_ORDER "
					+ "WHERE INST_ID=? AND ORDER_REF_NO=? AND ROWNUM<=?";
			parentcardno = (String) jdbctemplate.queryForObject(
					parentcardnoqry,
					new Object[] { instid, order_ref_no, "1" }, String.class);

		} catch (Exception e) {
		}
		return parentcardno;
	}

	public String getParentCardNumberByCard(String instid, String cardno,
			String processtype, JdbcTemplate jdbctemplate) throws Exception {
		String parentcardno = null;
		try {
			String parentcardnoqry = "SELECT ORG_CHN  FROM PERS_CARD_PROCESS WHERE INST_ID='"
					+ instid + "' AND CARD_NO='" + cardno + "' AND ROWNUM<=1";
			// // enctrace("3030parentcardnoqry : " + parentcardnoqry );
			parentcardno = (String) jdbctemplate.queryForObject(
					parentcardnoqry, String.class);
		} catch (Exception e) {
		}
		return parentcardno;
	}

	public String getCardIssueTypeByCard(String instid, String padssenable,
			String cardno, String processtype, JdbcTemplate jdbctemplate)
			throws Exception {
		String cardissuetype = null, cardissuetypeqry = "";
		try {
			if (processtype.equals("PERSONAL")) {
				/*
				 * if (padssenable.equals("Y")) {
				 */

				/*
				 * cardissuetypeqry =
				 * "SELECT CARDISSUETYPE FROM PERS_CARD_PROCESS WHERE INST_ID='"
				 * + instid+ "' AND ORG_CHN='" + cardno + "' AND ROWNUM<=1";
				 * enctrace("3020cardissuetypeqry : " + cardissuetypeqry);
				 */

				// / BY GOWTHAM-270819
				cardissuetypeqry = "SELECT CARDISSUETYPE FROM PERS_CARD_PROCESS WHERE INST_ID=? AND ORG_CHN=? AND ROWNUM<=?";
				enctrace("3020cardissuetypeqry : " + cardissuetypeqry);
				cardissuetype = (String) jdbctemplate.queryForObject(
						cardissuetypeqry, new Object[] { instid, cardno, "1" },
						String.class);
				/*
				 * } else { cardissuetypeqry =
				 * "SELECT CARDISSUETYPE FROM PERS_CARD_PROCESS WHERE INST_ID='"
				 * + instid + "' AND ORG_CHN='" + cardno + "' AND ROWNUM<=1"; }
				 */
			}

			else {

				/*
				 * if (padssenable.equals("Y")) {
				 */

				/*
				 * cardissuetypeqry =
				 * "SELECT CARDISSUETYPE FROM INST_CARD_PROCESS WHERE INST_ID='"
				 * + instid+ "' AND ORG_CHN='" + cardno + "' AND ROWNUM<=1";
				 * enctrace("3030cardissuetypeqry : " + cardissuetypeqry);
				 */

				// BY GOWTHAM-270819
				cardissuetypeqry = "SELECT CARDISSUETYPE FROM INST_CARD_PROCESS WHERE INST_ID=? AND ORG_CHN=? AND ROWNUM<=?";
				enctrace("3030cardissuetypeqry : " + cardissuetypeqry);
				cardissuetype = (String) jdbctemplate.queryForObject(
						cardissuetypeqry, new Object[] { instid, cardno, "1" },
						String.class);
				/*
				 * } else { cardissuetypeqry =
				 * "SELECT CARDISSUETYPE FROM INST_CARD_PROCESS WHERE INST_ID='"
				 * + instid + "' AND ORG_CHN='" + cardno + "' AND ROWNUM<=1"; }
				 */

			}
			// // enctrace("3030cardissuetypeqry : " + cardissuetypeqry );

			// BY GOWTHAM-270819
			/*
			 * cardissuetype = (String)
			 * jdbctemplate.queryForObject(cardissuetypeqry, String.class);
			 */

		} catch (Exception e) {
		}
		return cardissuetype;
	}

	public String getCardPrimaryAccount(String instid, String cardno,
			String curcode, JdbcTemplate jdbctemplate) throws Exception {
		String primaryacctno = null;
		String primaryacctnoqry = "SELECT ACCT_NO FROM IFD_CARD_ACCT_LINK WHERE INST_ID='"
				+ instid
				+ "' AND CARD_NO='"
				+ cardno
				+ "' AND ACCT_CCY='"
				+ curcode + "' AND ACCT_PRIORITY='1'";
		// // enctrace("3030primaryacctnoqry :" + primaryacctnoqry);
		try {
			primaryacctno = (String) jdbctemplate.queryForObject(
					primaryacctnoqry, String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return primaryacctno;

	}

	public int updateAddonCountToParent(String instid, String cardno,
			JdbcTemplate jdbctemplate) {
		int x = -1;
		String updaddoncount = "UPDATE CARD_PRODUCTION SET ADDONCARDCOUNT=ADDONCARDCOUNT+1 WHERE INST_ID='"
				+ instid + "' AND CARD_NO='" + cardno + "'";
		// // enctrace("3030updaddoncount :" + updaddoncount );
		x = jdbctemplate.update(updaddoncount);
		return x;
	}

	public int updateMobileNumberToProduction(String instid, String cardno,
			String mobileno, JdbcTemplate jdbctemplate) {
		int x = -1;

		/*
		 * String updaddoncount = "UPDATE CARD_PRODUCTION SET MOBILENO='" +
		 * mobileno + "' " + "WHERE INST_ID='" + instid+ "' AND CARD_NO='" +
		 * cardno + "'"; //// enctrace("3030updaddoncount :" + updaddoncount );
		 * x = jdbctemplate.update(updaddoncount);
		 */

		// / by gowtham-280819
		String updaddoncount = "UPDATE CARD_PRODUCTION SET MOBILENO=? "
				+ "WHERE INST_ID=? AND CARD_NO=?";
		// // enctrace("3030updaddoncount :" + updaddoncount );
		x = jdbctemplate.update(updaddoncount, new Object[] { mobileno, instid,
				cardno });

		return x;
	}

	public int getSupplimentEligiblity(String instid, String corpratecardno,
			int allowedaddoncardcount, JdbcTemplate jdbctemplate) {
		int x = -1;

		/*
		 * String validqry =
		 * "SELECT CARDISSUETYPE, ADDONCARDCOUNT FROM CARD_PRODUCTION" +
		 * " WHERE CARD_NO='" + corpratecardno+ "' AND CARD_STATUS='05'"; ////
		 * enctrace("3030validqry : " + validqry ); List cardlist =
		 * jdbctemplate.queryForList(validqry);
		 */

		// by gowtham
		String validqry = "SELECT CARDISSUETYPE, ADDONCARDCOUNT FROM CARD_PRODUCTION"
				+ " WHERE CARD_NO=? AND CARD_STATUS=? ";
		// // enctrace("3030validqry : " + validqry );
		List cardlist = jdbctemplate.queryForList(validqry, new Object[] {
				corpratecardno, "05" });

		if (cardlist.isEmpty()) {
			x = 1;
			return x;
		} else {
			Iterator itr = cardlist.iterator();
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				String cardissuetype = (String) mp.get("CARDISSUETYPE");
				String addoncard = (String) mp.get("ADDONCARDCOUNT");
				if (cardissuetype != null && cardissuetype.equals("$SUPLIMENT")) {
					x = 2;
					return x;
				} else {
					if (Integer.parseInt(addoncard) >= allowedaddoncardcount) {
						x = 3;
						return x;
					}
				}
			}
			x = 0;
		}
		return x;
	}

	public String getCourierTrackEnabled(String instid,
			JdbcTemplate jdbctemplate) throws Exception {
		String couriertrackenabled = "0";

		String couriertrackenabledqry = "SELECT COURIERTRACK_ENABLED FROM INSTITUTION WHERE INST_ID='"
				+ instid + "'";
		enctrace("3030couriertrackenabledqry :" + couriertrackenabledqry);
		// System.out.println("couriertrackenabledqry===>"+couriertrackenabledqry);
		try {
			couriertrackenabled = (String) jdbctemplate.queryForObject(
					couriertrackenabledqry, String.class);

			/*
			 * //by gowtham-270819 String couriertrackenabledqry =
			 * "SELECT COURIERTRACK_ENABLED FROM INSTITUTION WHERE INST_ID=?";
			 * enctrace("3030couriertrackenabledqry :"+couriertrackenabledqry );
			 * try { couriertrackenabled = (String)
			 * jdbctemplate.queryForObject(couriertrackenabledqry,new
			 * Object[]{instid}, String.class);
			 */
		} catch (EmptyResultDataAccessException e) {
		}
		return couriertrackenabled;
	}

	public String getCustomerIdBasedon(String instid, JdbcTemplate jdbctemplate)
			throws Exception {
		String customeridbasedon = null;
		String customeridbasedonqry = "SELECT CUSTID_BASEDON FROM INSTITUTION WHERE INST_ID='"
				+ instid + "'";
		// // enctrace("3030customeridbasedonqry :"+customeridbasedonqry );
		try {
			customeridbasedon = (String) jdbctemplate.queryForObject(
					customeridbasedonqry, String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return customeridbasedon;
	}

	public String getCourierId(String instid, String cardno,
			String processtype, JdbcTemplate jdbctemplate) {
		String courierid = null;
		String courieridqry = "SELECT COURIER_ID FROM PERS_CARD_PROCESS  WHERE INST_ID='"
				+ instid + "' AND CARD_NO='" + cardno + "' ";
		// // enctrace("3030courieridqry :"+courieridqry);
		try {
			courierid = (String) jdbctemplate.queryForObject(courieridqry,
					String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return courierid;
	}

	public Boolean checkFeeApplicable(String instid, String cardno,
			String feecode, String processtype, JdbcTemplate jdbctemplate)
			throws Exception {
		Boolean feeneeded = false;
		String fldvar = "";
		String fld = null;
		if (feecode == null) {
			return false;
		}
		if (feecode.equals("REPIN")) {
			fldvar = "REPINFEE";
		} else if (feecode.equals("DAMAGE")) {
			fldvar = "DAMAGEFEE";
		} else if (feecode.equals("REISSUE")) {
			fldvar = "REISSUEFEE";
		} else if (feecode.equals("NEWCARD")) {
			fldvar = "NEWCARDISSUEFEE";
		} else {
			return false;
		}

		String subproduct = this.getSubProductByCHN(instid, cardno,
				jdbctemplate, processtype);
		trace("Getting subproduct from process : " + subproduct);
		if (subproduct == null || subproduct.equals("NOREC")) {
			subproduct = this.getSubProductByCHNProduction(instid, cardno,
					jdbctemplate);
			trace("Getting subproduct from production : " + subproduct);
		}

		try {
			String fldvarqry = "SELECT " + fldvar
					+ " FROM INSTPROD_DETAILS WHERE INST_ID='" + instid
					+ "' AND SUB_PROD_ID='" + subproduct + "'";
			// // enctrace("3030feeenabledqry :" + fldvarqry);
			fld = (String) jdbctemplate.queryForObject(fldvarqry, String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		if (fld == null || fld.equals("N")) {
			return false;
		} else {
			return true;
		}
	}

	public String getBinLen(String instid, JdbcTemplate jdbctemplate)
			throws Exception {

		/*
		 * String binlen = null; String binlenqry =
		 * "SELECT BINLEN FROM INSTITUTION WHERE INST_ID='" + instid + "'";
		 */

		// by gowtham 190819
		String binlen = null;
		String binlenqry = "SELECT BINLEN FROM INSTITUTION WHERE INST_ID=?";

		// // enctrace("3030binlenqry :"+ binlenqry);
		try {
			binlen = (String) jdbctemplate.queryForObject(binlenqry,
					new Object[] { instid }, String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return binlen;
	}

	public int getBinLen(String instid, String cardno, JdbcTemplate jdbctemplate)
			throws Exception {
		int binlen = 6;
		String binlenqry = "SELECT TO_NUMBER(NVL(BINLEN,0)) AS BINLEN  FROM INSTITUTION WHERE INST_ID='"
				+ instid + "'";
		// // enctrace("3030binlenqry :"+ binlenqry);
		try {
			binlen = jdbctemplate.queryForInt(binlenqry);
		} catch (EmptyResultDataAccessException e) {
		}
		return binlen;
	}

	public String getCardTypeLen(String instid, JdbcTemplate jdbctemplate)
			throws Exception {
		String cardtypelen = null;

		// / BY GOWTHAM-190819
		// String cardtypelenqry = "SELECT CARDTYPELEN FROM INSTITUTION WHERE
		// / INST_ID='" + instid + "'";
		String cardtypelenqry = "SELECT CARDTYPELEN FROM INSTITUTION WHERE INST_ID=?";

		// // enctrace("3030cardtypelenqry :"+ cardtypelenqry );
		try {

			// cardtypelen = (String)
			// jdbctemplate.queryForObject(cardtypelenqry, String.class);
			cardtypelen = (String) jdbctemplate.queryForObject(cardtypelenqry,
					new Object[] { instid }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return cardtypelen;
	}

	public String getAccountTypeLen(String instid, JdbcTemplate jdbctemplate)
			throws Exception {
		String cardtypelen = null;

		/*
		 * String cardtypelenqry =
		 * "SELECT ACCOUNT_TYPE_LENGTH FROM INSTITUTION WHERE INST_ID='" +
		 * instid + "'"; //// enctrace("3030getAccountTypeLen :"+ cardtypelenqry
		 * ); try { cardtypelen = (String)
		 * jdbctemplate.queryForObject(cardtypelenqry, String.class);
		 */

		// by gowtham-190819
		String cardtypelenqry = "SELECT ACCOUNT_TYPE_LENGTH FROM INSTITUTION WHERE INST_ID=?";
		// // enctrace("3030getAccountTypeLen :"+ cardtypelenqry );
		try {
			cardtypelen = (String) jdbctemplate.queryForObject(cardtypelenqry,
					new Object[] { instid }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return cardtypelen;
	}

	public String getAccountSubTypeLen(String instid, JdbcTemplate jdbctemplate)
			throws Exception {
		String cardtypelen = null;

		// by gowtham-200819
		/*
		 * String cardtypelenqry =
		 * "SELECT ACCTSUBTYPE_LENGTH FROM INSTITUTION WHERE INST_ID='" + instid
		 * + "'";
		 */
		String cardtypelenqry = "SELECT ACCTSUBTYPE_LENGTH FROM INSTITUTION WHERE INST_ID=?";

		// // enctrace("3030getAccountSubTypeLen :"+ cardtypelenqry );
		try {

			/*
			 * cardtypelen = (String)
			 * jdbctemplate.queryForObject(cardtypelenqry, String.class);
			 */
			cardtypelen = (String) jdbctemplate.queryForObject(cardtypelenqry,
					new Object[] { instid }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return cardtypelen;
	}

	public Boolean hasBranchBasedProductEnabled(String instid,
			JdbcTemplate jdbctemplate) throws Exception {
		Boolean isbranchbasedprodcut = false;

		/*
		 * String branchbasedqry =
		 * "SELECT  BRANCH_BASEDPRODUCT  FROM INSTITUTION  WHERE INST_ID='" +
		 * instid + "'"; //// enctrace("3030branchbasedqry :"+ branchbasedqry );
		 * try { String branchbased = (String)
		 * jdbctemplate.queryForObject(branchbasedqry, String.class);
		 */

		// BY GOWTHAM-190819
		String branchbasedqry = "SELECT  BRANCH_BASEDPRODUCT  FROM INSTITUTION  WHERE INST_ID=?";
		// // enctrace("3030branchbasedqry :"+ branchbasedqry );
		try {
			String branchbased = (String) jdbctemplate.queryForObject(
					branchbasedqry, new Object[] { instid }, String.class);

			if (branchbased == null) {
				isbranchbasedprodcut = false;
			} else if (branchbased.equals("1")) {
				isbranchbasedprodcut = true;
			}

		} catch (EmptyResultDataAccessException e) {
		}
		return isbranchbasedprodcut;
	}

	public String isCardSupplimentCard(String instid, String cardno,
			String tablename, JdbcTemplate jdbctemplate) throws Exception {
		int supplimentcnt = -1;
		String is_suppliment = "$NO";
		String is_supplimentqry = "SELECT COUNT(*) AS CNT FROM " + tablename
				+ " WHERE INST_ID='" + instid + "' AND CARD_NO='" + cardno
				+ "' AND CARDISSUETYPE='$SUPLIMENT'";
		supplimentcnt = jdbctemplate.queryForInt(is_supplimentqry);
		if (supplimentcnt > 0) {
			is_suppliment = "$YES";
		}
		return is_suppliment;
	}

	public List getListOfPreviousYear() {
		int tot = 50;
		int curyear = 2014;
		int i = 0;
		List yearlist = new ArrayList();
		while (i < tot) {
			yearlist.add(curyear);
			curyear--;
			tot--;
		}
		return yearlist;
	}

	public List getSuplimentaryRelation(String instid, JdbcTemplate jdbctemplate)
			throws Exception {
		List relationlist = null;
		String relationlistqry = "SELECT RELATIONCODE, RELATIONDESC  FROM  IFC_SUPPLIMENTRELATION  WHERE INST_ID='"
				+ instid + "' and STATUS='1'";
		// // enctrace("3030relationlistqry :"+ relationlistqry);
		relationlist = jdbctemplate.queryForList(relationlistqry);
		return relationlist;
	}

	public String getLimitIdBySubProduct(String instid, String productcode,
			String subproduct, JdbcTemplate jdbctemplate) throws Exception {
		String limitid = null;

		/*
		 * String limitidqry =
		 * "SELECT LIMIT_ID FROM INSTPROD_DETAILS WHERE INST_ID='" + instid +
		 * "' AND PRODUCT_CODE='"+ productcode + "' AND SUB_PROD_ID='" +
		 * subproduct + "'"; //// enctrace("3030limitidqry : " + limitidqry);
		 * limitid = (String) jdbctemplate.queryForObject(limitidqry,
		 * String.class);
		 */

		// by gowtham-210819
		String limitidqry = "SELECT LIMIT_ID FROM INSTPROD_DETAILS WHERE INST_ID=? AND PRODUCT_CODE=? AND SUB_PROD_ID=? ";
		// // enctrace("3030limitidqry : " + limitidqry);
		limitid = (String) jdbctemplate.queryForObject(limitidqry,
				new Object[] { instid, productcode, subproduct }, String.class);

		return limitid;
	}

	public String getFeeBySubProduct(String instid, String productcode,
			String subproduct, JdbcTemplate jdbctemplate) throws Exception {
		String feecode = null;

		/*
		 * String feecodeqry =
		 * "SELECT FEE_CODE FROM INSTPROD_DETAILS WHERE INST_ID='" + instid +
		 * "' AND PRODUCT_CODE='"+ productcode + "' AND SUB_PROD_ID='" +
		 * subproduct + "'"; //// enctrace("3030feecodeqry : " + feecodeqry);
		 * feecode = (String) jdbctemplate.queryForObject(feecodeqry,
		 * String.class);
		 */

		// by gowtham-210819
		String feecodeqry = "SELECT FEE_CODE FROM INSTPROD_DETAILS WHERE INST_ID=? AND PRODUCT_CODE=? AND SUB_PROD_ID=?";
		// // enctrace("3030feecodeqry : " + feecodeqry);
		feecode = (String) jdbctemplate.queryForObject(feecodeqry,
				new Object[] { instid, productcode, subproduct }, String.class);

		return feecode;
	}

	public String getFeeDesc(String instid, String feecode,
			JdbcTemplate jdbctemplate) throws Exception {
		String feedesc = null;

		/*
		 * String feecodeqry = "SELECT FEE_DESC from FEE_DESC WHERE INST_ID='" +
		 * instid + "' AND FEE_CODE='" + feecode+ "'"; //// enctrace(
		 * "3030feecodeqry : " + feecodeqry); feedesc = (String)
		 * jdbctemplate.queryForObject(feecodeqry, String.class);
		 */

		// by gowtham-210819
		String feecodeqry = "SELECT FEE_DESC from FEE_DESC WHERE INST_ID=? AND FEE_CODE=?";
		// // enctrace("3030feecodeqry : " + feecodeqry);
		feedesc = (String) jdbctemplate.queryForObject(feecodeqry,
				new Object[] { instid, feecode }, String.class);

		return feedesc;
	}

	public String getcurrencydesc(String instid, String numericcode,
			JdbcTemplate jdbctemplate) throws Exception {
		String feedesc = null;
		String feecodeqry = "SELECT CURRENCY_DESC from GLOBAL_CURRENCY WHERE  NUMERIC_CODE='"
				+ numericcode + "'";
		// // enctrace("3030Currcodeqry : " + feecodeqry);
		feedesc = (String) jdbctemplate
				.queryForObject(feecodeqry, String.class);
		return feedesc;
	}

	public String getCurrencyvalues(String instid, String productcode,
			String subproduct, JdbcTemplate jdbctemplate) throws Exception {
		String feecode = null;

		/*
		 * String feecodeqry =
		 * "SELECT CARD_CCY FROM INSTPROD_DETAILS WHERE INST_ID='" + instid +
		 * "' AND SUB_PROD_ID='"+ subproduct + "'"; //// enctrace(
		 * "3030Currencycode : " + feecodeqry); feecode = (String)
		 * jdbctemplate.queryForObject(feecodeqry, String.class);
		 */

		// BY GOWTHAM-210819
		String feecodeqry = "SELECT CARD_CCY FROM INSTPROD_DETAILS WHERE INST_ID=? AND SUB_PROD_ID=?";
		// // enctrace("3030Currencycode : " + feecodeqry);
		feecode = (String) jdbctemplate.queryForObject(feecodeqry,
				new Object[] { instid, subproduct }, String.class);

		return feecode;
	}

	public List getLimitValueDetails(String instid, String limitid,
			String currencycode, JdbcTemplate jdbctemplate) {
		List limitlist = null;
		StringBuilder limitlistqry = new StringBuilder();

		/*
		 * limitlistqry.append(
		 * "SELECT INSTID, LIMITTYPE, LIMITID,ACTION_DESC, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, "
		 * ); limitlistqry.append(
		 * "MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, ACCFLAG,TO_CHAR(FROMDATE,'DD-MM-YYYY')FROMDATE,TO_CHAR(TODATE,'DD-MM-YYYY')TODATE "
		 * ); limitlistqry.append(
		 * "FROM  LIMITINFO,ACTIONCODES  WHERE TXN_CODE = TXNCODE ");
		 * limitlistqry.append("AND INST_ID=INSTID AND INSTID='" + instid +
		 * "' AND LIMIT_RECID = '" + limitid + "' and AUTH_CODE='0'"); ////
		 * enctrace("3030limitlistqry :"+ limitlistqry.toString()); limitlist =
		 * jdbctemplate.queryForList(limitlistqry.toString());
		 */

		// by gowtham-200819
		limitlistqry
				.append("SELECT INSTID, LIMITTYPE, LIMITID,ACTION_DESC, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, ");
		limitlistqry
				.append("MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, ACCFLAG,TO_CHAR(FROMDATE,'DD-MM-YYYY')FROMDATE,TO_CHAR(TODATE,'DD-MM-YYYY')TODATE ");
		limitlistqry
				.append("FROM  LIMITINFO,ACTIONCODES  WHERE TXN_CODE = TXNCODE ");
		limitlistqry.append("AND INST_ID=INSTID AND INSTID='" + instid
				+ "' AND LIMIT_RECID = '" + limitid + "' and AUTH_CODE='0'");
		limitlist = jdbctemplate.queryForList(limitlistqry.toString());

		enctrace("getLimitValueDetails ======>   " + limitlistqry);
		return limitlist;
	}

	public String getLimitBasedOn(String instid, String limitid,
			JdbcTemplate jdbctemplate) {
		String limitbasedon = null;

		/*
		 * String limitlistqry =
		 * "SELECT LIMITTYPE FROM  LIMITINFO WHERE INSTID='" + instid +
		 * "' AND LIMIT_RECID = '"+ limitid + "' and rownum<=1"; //// enctrace(
		 * "3030limitlistqry :"+ limitlistqry); limitbasedon = (String)
		 * jdbctemplate.queryForObject(limitlistqry, String.class);
		 */

		// by gowtham-220819
		String limitlistqry = "SELECT LIMITTYPE FROM  LIMITINFO WHERE INSTID=? AND LIMIT_RECID =? and rownum<=?";
		// // enctrace("3030limitlistqry :"+ limitlistqry);
		limitbasedon = (String) jdbctemplate.queryForObject(limitlistqry,
				new Object[] { instid, limitid, "1" }, String.class);

		return limitbasedon;
	}

	public String getAccflagBylimitid(String instid, String limitid,
			JdbcTemplate jdbctemplate) {
		String accflag = null;

		/*
		 * String limitlistqry =
		 * "select ACCT_FLAG from GLOBAL_CARDDETAILS where LIMIT_TYPE in ( " +
		 * "select LIMITTYPE from LIMITINFO where limit_recid='" + limitid +
		 * "' and INSTID='" + instid+ "' and rownum<=1)"; //// enctrace(
		 * "3030accflag :"+ limitlistqry); accflag = (String)
		 * jdbctemplate.queryForObject(limitlistqry, String.class);
		 */

		// / by gowtham-300819
		String limitlistqry = "select ACCT_FLAG from GLOBAL_CARDDETAILS where LIMIT_TYPE in ( "
				+ "select LIMITTYPE from LIMITINFO where limit_recid=? and INSTID=? and rownum<=?)";
		// // enctrace("3030accflag :"+ limitlistqry);
		accflag = (String) jdbctemplate.queryForObject(limitlistqry,
				new Object[] { limitid, instid, "1" }, String.class);

		return accflag;
	}

	public StatusandMsgBean getStatusandMsg(StatusandMsgBean statusbean) {
		StatusandMsgBean sb = new StatusandMsgBean();

		if (sb.getCurrStatus().equals("CARD")
				&& sb.getMkrckrStatus().equals("M")) {
			sb.setStatusCode("01");
			sb.setStatusMsg("Card Generated Successfully and Waiting For Authorization .......");

		}

		if (sb.getCurrStatus().equals("CARD")
				&& sb.getMkrckrStatus().equals("P")) {
			sb.setStatusCode("01");
			sb.setStatusMsg("Card Generated Successfully and Waiting For Authorization .......");

		}

		return sb;
	}

	public List getSequritygenerationdetails(String instid, String bin,
			JdbcTemplate jdbctemplate) {
		List seclist = null;
		StringBuilder secqury = new StringBuilder();
		// GEN_METHOD added for Orient Bank Requirements
		// Its to update pin offset for VISA And IBMDES

		/*
		 * secqury.append("SELECT CARD_TYPE,SEQ_OPTION,GEN_METHOD ");
		 * secqury.append("FROM  PRODUCTINFO  WHERE ");
		 * secqury.append("INST_ID='" + instid + "' AND BIN = '" + bin + "' ");
		 * //// enctrace("3030secqury :"+ secqury.toString()); seclist =
		 * jdbctemplate.queryForList(secqury.toString());
		 */

		// by gowtham260819
		secqury.append("SELECT CARD_TYPE,SEQ_OPTION,GEN_METHOD ");
		secqury.append("FROM  PRODUCTINFO  WHERE ");
		secqury.append("INST_ID=? AND BIN = ? ");
		// // enctrace("3030secqury :"+ secqury.toString());
		seclist = jdbctemplate.queryForList(secqury.toString(), new Object[] {
				instid, bin });

		return seclist;
	}

	public String getMaskedCardNo(String inst, String cHN, String cond,
			JdbcTemplate jdbctemplate) {

		/*
		 * if ("C".equalsIgnoreCase(cond)) { cond = " ORG_CHN='" + cHN + "'"; }
		 * else if ("H".equalsIgnoreCase(cond)) { cond = " HCARD_NO='" + cHN +
		 * "'"; } else if ("order".equalsIgnoreCase(cond)) { cond =
		 * " ORDER_REF_NO='" + cHN + "'"; }else { cond = " CIN='" + cHN + "'"; }
		 * System.out.println("condtion is ---->  " + cond); String chn = "";
		 * try { String query =
		 * "select MCARD_NO from PERS_CARD_PROCESS where INST_ID='" + inst +
		 * "' and " + cond + "";
		 * 
		 * chn = (String) jdbctemplate.queryForObject(query, String.class);
		 * enctrace("3030MCARD_NO query :" + query.toString()); //
		 * System.out.println("query : " + query); } catch (Exception e) {
		 * e.printStackTrace(); }
		 */

		// by gowtham-260819

		String chn = "";
		try {

			if ("C".equalsIgnoreCase(cond)) {
				cond = " ORG_CHN='" + cHN + "'";

				String query = "select MCARD_NO from PERS_CARD_PROCESS where INST_ID=? and ORG_CHN=? ";

				chn = (String) jdbctemplate.queryForObject(query, new Object[] {
						inst, cHN }, String.class);
				enctrace("3030MCARD_NO query :" + query.toString());

			} else if ("H".equalsIgnoreCase(cond)) {
				cond = " HCARD_NO='" + cHN + "'";

				String query = "select MCARD_NO from PERS_CARD_PROCESS where INST_ID=? and  HCARD_NO=? ";

				chn = (String) jdbctemplate.queryForObject(query, new Object[] {
						inst, cHN }, String.class);
				enctrace("3030MCARD_NO query :" + query.toString());

			} else if ("order".equalsIgnoreCase(cond)) {

				cond = " ORDER_REF_NO='" + cHN + "'";

				String query = "select MCARD_NO from PERS_CARD_PROCESS where INST_ID=? and ORDER_REF_NO=?";

				chn = (String) jdbctemplate.queryForObject(query, new Object[] {
						inst, cHN }, String.class);

				enctrace("3030MCARD_NO query :" + query.toString());
			} else {
				cond = " CIN='" + cHN + "'";

				String query = "select MCARD_NO from PERS_CARD_PROCESS where INST_ID=? and CIN=?";

				chn = (String) jdbctemplate.queryForObject(query, new Object[] {
						inst, cHN }, String.class);
				enctrace("3030MCARD_NO query :" + query.toString());
			}
			System.out.println("condtion is ---->  " + cond);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return chn;
	}

	public String getMaskedCardbyproc(String inst, String cHN,
			String tablename, String cond, JdbcTemplate jdbctemplate) {
		if ("C".equalsIgnoreCase(cond)) {
			cond = " ORG_CHN='" + cHN + "'";
		}

		else if ("H".equalsIgnoreCase(cond)) {
			cond = " ORG_CHN='" + cHN + "'";
		}

		else if ("O".equalsIgnoreCase(cond)) {
			cond = " ORDER_REF_NO='" + cHN + "'";
		}

		else {
			cond = " CIN='" + cHN + "'";

		}

		String chn = "";
		try {

			/*
			 * String query = "select MCARD_NO from " + tablename +
			 * " where INST_ID='" + inst + "' and " + cond + ""; enctrace(
			 * "getMaskedCardbyproc query :" + query.toString()); chn = (String)
			 * jdbctemplate.queryForObject(query, String.class);
			 */

			// // by gowtham-280819
			String query = "select MCARD_NO from " + tablename
					+ " where INST_ID=? and " + cond + "";
			enctrace("getMaskedCardbyproc query :" + query.toString());
			chn = (String) jdbctemplate.queryForObject(query,
					new Object[] { inst }, String.class);

			// System.out.println("query : " + query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return chn;
	}

	/**
	 * This method is used to get the Masked card number from process table
	 * 
	 * @param instid
	 * @param chn
	 * @param jdbctemplate
	 * @return masked cardno
	 */
	public String getMaksedCardFromProcess1(String instid, String chn,
			JdbcTemplate jdbctemplate) {

		String maskedQuery = null;
		String mcardNo = null;
		try {

			/*
			 * maskedQuery =
			 * "SELECT MCARD_NO FROM INST_CARD_PROCESS WHERE INST_ID='" + instid
			 * + "'  AND ORG_CHN='" + chn+ "'"; trace("maskedQuery---->  " +
			 * maskedQuery); mcardNo = (String)
			 * jdbctemplate.queryForObject(maskedQuery, String.class);
			 */
			String condition = "";

			/*
			 * if("ORDER_REF_NO".equals(status)){ condition=" AND ORDER_REF_NO";
			 * }else{ condition=" AND ORG_CHN"; }
			 */

			// by gowtham-280819
			maskedQuery = "SELECT MCARD_NO FROM PERS_CARD_PROCESS WHERE INST_ID='"
					+ instid + "'  AND ORDER_REF_NO='" + chn + "'";
			enctrace("maskedQuery:::   " + maskedQuery);
			mcardNo = (String) jdbctemplate.queryForObject(maskedQuery,
					String.class);

		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
		}
		return mcardNo;
	}

	/**
	 * This method is used to get the Masked card number from process table
	 * 
	 * @param instid
	 * @param chn
	 * @param jdbctemplate
	 * @return masked cardno
	 */
	public String getMaksedCardFromProcess(String instid, String chn,
			JdbcTemplate jdbctemplate) {

		String maskedQuery = null;
		String mcardNo = null;
		try {

			/*
			 * maskedQuery =
			 * "SELECT MCARD_NO FROM INST_CARD_PROCESS WHERE INST_ID='" + instid
			 * + "'  AND ORG_CHN='" + chn+ "'"; trace("maskedQuery---->  " +
			 * maskedQuery); mcardNo = (String)
			 * jdbctemplate.queryForObject(maskedQuery, String.class);
			 */
			/*
			 * String condition="";
			 * 
			 * if("ORDER_REF_NO".equals(status)){ condition=" AND ORDER_REF_NO";
			 * }else{ condition=" AND ORG_CHN"; }
			 */
			// by gowtham-280819
			maskedQuery = "SELECT MCARD_NO FROM INST_CARD_PROCESS WHERE INST_ID=?  AND  ORG_CHN='"
					+ chn + "'";
			trace("maskedQuery---->  " + maskedQuery);
			mcardNo = (String) jdbctemplate.queryForObject(maskedQuery,
					new Object[] { instid }, String.class);

		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
		}
		return mcardNo;
	}

	public String getMaskedCardNoFromProd(String inst, String cHN, String cond,
			JdbcTemplate jdbctemplate) {
		String condition2 = "";
		if ("C".equalsIgnoreCase(cond)) {
			cond = " ORG_CHN='" + cHN + "'";
		} else if ("H".equalsIgnoreCase(cond)) {
			cond = " ORG_CHN='" + cHN + "'";
		} else {
			cond = " ORG_CHN='" + cHN + "'";
		}
		// '" + hcardno + "'
		String chn = "";

		try {

			String query = "select COUNT(*) from CARD_PRODUCTION where INST_ID=? and "
					+ cond + "";
			int cardavialble = jdbctemplate.queryForInt(query,
					new Object[] { inst });
			if (cardavialble > 0) {
				condition2 = "CARD_PRODUCTION";
				System.out.println("CARD_PRODUCTION");
			}
			String query1 = "select COUNT(*) from INST_CARD_PROCESS where INST_ID=? and "
					+ cond + "";
			int cardavialble1 = jdbctemplate.queryForInt(query1,
					new Object[] { inst });
			// System.out.println(query1);
			if (cardavialble1 > 0) {
				condition2 = "INST_CARD_PROCESS";
				System.out.println("INST_CARD_PROCESS");
			}

			String query2 = "select COUNT(*) from PERS_CARD_PROCESS where INST_ID=? and "
					+ cond + "";
			int cardavialble3 = jdbctemplate.queryForInt(query2,
					new Object[] { inst });
			// System.out.println(query2);
			if (cardavialble3 > 0) {
				condition2 = "PERS_CARD_PROCESS";
				System.out.println("PERS_CARD_PROCESS");
			}

			String queryprod = "select MCARD_NO from " + condition2
					+ " where INST_ID=? and " + cond + "";
			enctrace("3030MCARD_NO query :" + queryprod.toString());
			// // System.out.println("query : " + query);

			chn = (String) jdbctemplate.queryForObject(queryprod,
					new Object[] { inst }, String.class);
		} catch (Exception exce) {
			exce.printStackTrace();
		}
		/*
		 * if(cardavialble1 >0){ String queryprod2=
		 * "select CARD_NO from INST_CARD_PROCESS where INST_ID='"+inst+"' and "
		 * +cond+""; ////enctrace("3030MCARD_NO query :"+ query.toString());
		 * ////System.out.println("query : " + query);
		 * 
		 * chn = (String) jdbctemplate.queryForObject(queryprod2,
		 * String.class);}
		 * 
		 * if(cardavialble3 >0){ String queryprod3=
		 * "select CARD_NO from INST_CARD_PROCESS where INST_ID='"+inst+"' and "
		 * +cond+""; ////enctrace("3030MCARD_NO query :"+ query.toString());
		 * ////System.out.println("query : " + query);
		 * 
		 * chn = (String) jdbctemplate.queryForObject(queryprod3,
		 * String.class);}
		 * 
		 * if(cardavialble3 >0){ String queryprod3=
		 * "select CARD_NO from PERS_CARD_PROCESS where INST_ID='"+inst+"' and "
		 * +cond+""; ////enctrace("3030MCARD_NO query :"+ query.toString());
		 * ////System.out.println("query : " + query);
		 * 
		 * chn = (String) jdbctemplate.queryForObject(queryprod3,
		 * String.class);}
		 */
		// // enctrace("3030MCARD_NO query :"+ query.toString());
		// System.out.println("query : " + query);
		return chn;
	}

	// by siva 160719
	/*
	 * public String getMaskedCardNoFromProd(String inst, String cHN, String
	 * cond, JdbcTemplate jdbctemplate) { String condition2 = ""; if
	 * ("C".equalsIgnoreCase(cond)) { cond = " CARD_NO='" + cHN + "'"; } else if
	 * ("H".equalsIgnoreCase(cond)) { cond = " HCARD_NO='" + cHN + "'"; } else {
	 * cond = " CIN='" + cHN + "'"; } String chn = ""; String query =
	 * "select COUNT(*) from CARD_PRODUCTION where INST_ID='" + inst + "' and "
	 * + cond + ""; int cardavialble = jdbctemplate.queryForInt(query); if
	 * (cardavialble > 0) { condition2 = "CARD_PRODUCTION"; } String query1 =
	 * "select COUNT(*) from INST_CARD_PROCESS where INST_ID='" + inst +
	 * "' and " + cond + ""; int cardavialble1 =
	 * jdbctemplate.queryForInt(query1); // System.out.println(query1); if
	 * (cardavialble1 > 0) { condition2 = "INST_CARD_PROCESS"; }
	 * 
	 * String query2 = "select COUNT(*) from PERS_CARD_PROCESS where INST_ID='"
	 * + inst + "' and " + cond + ""; int cardavialble3 =
	 * jdbctemplate.queryForInt(query2); // System.out.println(query2); if
	 * (cardavialble3 > 0) { condition2 = "PERS_CARD_PROCESS"; }
	 * 
	 * String queryprod = "select CARD_NO from " + condition2 +
	 * " where INST_ID='" + inst + "' and " + cond + ""; enctrace(
	 * "3030MCARD_NO query :" + queryprod.toString()); //// System.out.println(
	 * "query : " + query);
	 * 
	 * chn = (String) jdbctemplate.queryForObject(queryprod, String.class);
	 * 
	 * 
	 * if(cardavialble1 >0){ String queryprod2=
	 * "select CARD_NO from INST_CARD_PROCESS where INST_ID='"+inst+"' and "
	 * +cond+""; ////enctrace("3030MCARD_NO query :"+ query.toString());
	 * ////System.out.println("query : " + query);
	 * 
	 * chn = (String) jdbctemplate.queryForObject(queryprod2, String.class);}
	 * 
	 * if(cardavialble3 >0){ String queryprod3=
	 * "select CARD_NO from INST_CARD_PROCESS where INST_ID='"+inst+"' and "
	 * +cond+""; ////enctrace("3030MCARD_NO query :"+ query.toString());
	 * ////System.out.println("query : " + query);
	 * 
	 * chn = (String) jdbctemplate.queryForObject(queryprod3, String.class);}
	 * 
	 * if(cardavialble3 >0){ String queryprod3=
	 * "select CARD_NO from PERS_CARD_PROCESS where INST_ID='"+inst+"' and "
	 * +cond+""; ////enctrace("3030MCARD_NO query :"+ query.toString());
	 * ////System.out.println("query : " + query);
	 * 
	 * chn = (String) jdbctemplate.queryForObject(queryprod3, String.class);}
	 * 
	 * //// enctrace("3030MCARD_NO query :"+ query.toString()); //
	 * System.out.println("query : " + query); return chn; }
	 */

	public List getProductListBySelected(String instid, String cardStatus,
			String mkrstatus, JdbcTemplate jdbctemplate) throws Exception {
		// Condition For Each Stages
		String condition = null;

		if ("01".equals(cardStatus) && "M".equalsIgnoreCase(mkrstatus)) {
			condition = " AND CARD_STATUS='01' AND MKCK_STATUS='M' ";
		} else if ("01".equals(cardStatus) && "P".equalsIgnoreCase(mkrstatus)) {
			condition = " AND CARD_STATUS='01' AND MKCK_STATUS='P' ";
		} else if ("02".equals(cardStatus) && "M".equalsIgnoreCase(mkrstatus)) {
			condition = " AND CARD_STATUS='02' AND MKCK_STATUS='M' ";
		} else if ("02".equals(cardStatus) && "P".equalsIgnoreCase(mkrstatus)) {
			condition = " AND CARD_STATUS='02' AND MKCK_STATUS='P' ";
		} else if ("03".equals(cardStatus) && "M".equalsIgnoreCase(mkrstatus)) {
			condition = " AND CARD_STATUS='03' AND MKCK_STATUS='M' ";
		} else if ("03".equals(cardStatus) && "P".equalsIgnoreCase(mkrstatus)) {
			condition = " AND CARD_STATUS='03' AND MKCK_STATUS='P' ";
		} else if ("04".equals(cardStatus) && "M".equalsIgnoreCase(mkrstatus)) {
			condition = " AND CARD_STATUS='04' AND MKCK_STATUS='M' ";
		} else if ("04".equals(cardStatus) && "P".equalsIgnoreCase(mkrstatus)) {
			condition = " AND CARD_STATUS='04' AND MKCK_STATUS='P' ";
		} else if ("05".equals(cardStatus) && "M".equalsIgnoreCase(mkrstatus)) {
			condition = " AND CARD_STATUS='05' AND MKCK_STATUS='M' ";
		}

		/*
		 * String query =
		 * "select PRODUCT_CODE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER a WHERE a.PRODUCT_CODE=b.PRODUCT_CODE)CARD_TYPE_NAME from PERS_CARD_PROCESS b WHERE INST_ID='"
		 * + instid + "' " + condition +
		 * " GROUP BY PRODUCT_CODE  ORDER BY PRODUCT_CODE ";
		 * 
		 * 
		 * enctrace(" get prod list---> : " + query); return
		 * (jdbctemplate.queryForList(query));
		 */

		// by gowtham-260819
		String query = "select PRODUCT_CODE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER a WHERE a.PRODUCT_CODE=b.PRODUCT_CODE)CARD_TYPE_NAME from PERS_CARD_PROCESS b"
				+ " WHERE INST_ID=? "
				+ condition
				+ " GROUP BY PRODUCT_CODE  ORDER BY PRODUCT_CODE ";

		enctrace(" get prod list : " + query);
		return (jdbctemplate.queryForList(query, new Object[] { instid }));

	}

	/*
	 * //BY GOWTHAM-260819
	 * 
	 * 
	 * if ("01".equals(cardStatus) && "M".equalsIgnoreCase(mkrstatus)) {
	 * condition = " AND CARD_STATUS='01' AND MKCK_STATUS='M' ";
	 * 
	 * String query =
	 * "select PRODUCT_CODE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER a WHERE a.PRODUCT_CODE=b.PRODUCT_CODE)CARD_TYPE_NAME from PERS_CARD_PROCESS b WHERE INST_ID=? AND CARD_STATUS=? AND MKCK_STATUS=? GROUP BY PRODUCT_CODE  ORDER BY PRODUCT_CODE "
	 * ; enctrace(" get prod list : " + query); return
	 * (jdbctemplate.queryForList(query,new Object[]{instid,"01","M"}));
	 * 
	 * 
	 * } else if ("01".equals(cardStatus) && "P".equalsIgnoreCase(mkrstatus)) {
	 * condition = " AND CARD_STATUS='01' AND MKCK_STATUS='P' ";
	 * 
	 * String query =
	 * "select PRODUCT_CODE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER a WHERE a.PRODUCT_CODE=b.PRODUCT_CODE)"
	 * +
	 * "CARD_TYPE_NAME from PERS_CARD_PROCESS b WHERE INST_ID=?  AND CARD_STATUS=? AND MKCK_STATUS=? GROUP BY PRODUCT_CODE  ORDER BY PRODUCT_CODE "
	 * ; enctrace(" get prod list : " + query); return
	 * (jdbctemplate.queryForList(query,new Object[]{instid,"01","P"}));
	 * 
	 * 
	 * } else if ("02".equals(cardStatus) && "M".equalsIgnoreCase(mkrstatus)) {
	 * condition = " AND CARD_STATUS='02' AND MKCK_STATUS='M' ";
	 * 
	 * String query =
	 * "select PRODUCT_CODE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER a WHERE a.PRODUCT_CODE=b.PRODUCT_CODE)"
	 * +
	 * "CARD_TYPE_NAME from PERS_CARD_PROCESS b WHERE INST_ID=? AND CARD_STATUS=? AND MKCK_STATUS=? GROUP BY PRODUCT_CODE  ORDER BY PRODUCT_CODE "
	 * ; enctrace(" get prod list : " + query); return
	 * (jdbctemplate.queryForList(query,new Object[]{instid,"02","M"}));
	 * 
	 * 
	 * } else if ("02".equals(cardStatus) && "P".equalsIgnoreCase(mkrstatus)) {
	 * condition = " AND CARD_STATUS='02' AND MKCK_STATUS='P' ";
	 * 
	 * String query =
	 * "select PRODUCT_CODE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER a WHERE a.PRODUCT_CODE=b.PRODUCT_CODE)"
	 * +
	 * "CARD_TYPE_NAME from PERS_CARD_PROCESS b WHERE INST_ID=?  AND CARD_STATUS=? AND MKCK_STATUS=? GROUP BY PRODUCT_CODE  ORDER BY PRODUCT_CODE "
	 * ; enctrace(" get prod list : " + query); return
	 * (jdbctemplate.queryForList(query,new Object[]{instid,"02","P"}));
	 * 
	 * 
	 * } else if ("03".equals(cardStatus) && "M".equalsIgnoreCase(mkrstatus)) {
	 * condition = " AND CARD_STATUS='03' AND MKCK_STATUS='M' ";
	 * 
	 * String query =
	 * "select PRODUCT_CODE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER a WHERE a.PRODUCT_CODE=b.PRODUCT_CODE)"
	 * +
	 * "CARD_TYPE_NAME from PERS_CARD_PROCESS b WHERE INST_ID=? AND CARD_STATUS=? AND MKCK_STATUS=? GROUP BY PRODUCT_CODE  ORDER BY PRODUCT_CODE "
	 * ; enctrace(" get prod list : " + query); return
	 * (jdbctemplate.queryForList(query,new Object[]{instid,"03","M"}));
	 * 
	 * } else if ("03".equals(cardStatus) && "P".equalsIgnoreCase(mkrstatus)) {
	 * condition = " AND CARD_STATUS='03' AND MKCK_STATUS='P' ";
	 * 
	 * String query =
	 * "select PRODUCT_CODE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER a WHERE a.PRODUCT_CODE=b.PRODUCT_CODE)"
	 * +
	 * "CARD_TYPE_NAME from PERS_CARD_PROCESS b WHERE INST_ID=? AND CARD_STATUS=? AND MKCK_STATUS=? GROUP BY PRODUCT_CODE  ORDER BY PRODUCT_CODE "
	 * ; enctrace(" get prod list : " + query); return
	 * (jdbctemplate.queryForList(query,new Object[]{instid,"03","P"}));
	 * 
	 * } else if ("04".equals(cardStatus) && "M".equalsIgnoreCase(mkrstatus)) {
	 * condition = " AND CARD_STATUS='04' AND MKCK_STATUS='M' ";
	 * 
	 * 
	 * String query =
	 * "select PRODUCT_CODE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER a WHERE a.PRODUCT_CODE=b.PRODUCT_CODE)"
	 * +
	 * "CARD_TYPE_NAME from PERS_CARD_PROCESS b WHERE INST_ID=? AND CARD_STATUS=? AND MKCK_STATUS=? GROUP BY PRODUCT_CODE  ORDER BY PRODUCT_CODE "
	 * ; enctrace(" get prod list : " + query); return
	 * (jdbctemplate.queryForList(query,new Object[]{instid,"04","M"}));
	 * 
	 * 
	 * } else if ("04".equals(cardStatus) && "P".equalsIgnoreCase(mkrstatus)) {
	 * condition = " AND CARD_STATUS='04' AND MKCK_STATUS='P' ";
	 * 
	 * String query =
	 * "select PRODUCT_CODE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER a WHERE a.PRODUCT_CODE=b.PRODUCT_CODE)CARD_TYPE_NAME from PERS_CARD_PROCESS b WHERE INST_ID=? AND CARD_STATUS=? AND MKCK_STATUS=? GROUP BY PRODUCT_CODE  ORDER BY PRODUCT_CODE "
	 * ; enctrace(" get prod list : " + query); return
	 * (jdbctemplate.queryForList(query,new Object[]{instid,"04","P"}));
	 * 
	 * 
	 * } else if ("05".equals(cardStatus) && "M".equalsIgnoreCase(mkrstatus)) {
	 * condition = " AND CARD_STATUS='05' AND MKCK_STATUS='M' ";
	 * 
	 * 
	 * String query =
	 * "select PRODUCT_CODE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER a WHERE a.PRODUCT_CODE=b.PRODUCT_CODE)CARD_TYPE_NAME from PERS_CARD_PROCESS b WHERE INST_ID=? AND CARD_STATUS=? AND MKCK_STATUS=? GROUP BY PRODUCT_CODE  ORDER BY PRODUCT_CODE "
	 * ; enctrace(" get prod list : " + query); return
	 * (jdbctemplate.queryForList(query,new Object[]{instid,"05","M"}));
	 * 
	 * }
	 * 
	 * return null; }
	 */
	// by xxx 160719
	/*
	 * public List getfeedetails(String instid, String F_cardno, JdbcTemplate
	 * jdbctemplate) { List gettingcarddetails = null; String getfeedetails =
	 * "select CARD_NO,HCARD_NO,MCARD_NO,ACCOUNT_NO,CAF_REC_STATUS,FEE_CODE,BRANCH_CODE from CARD_PRODUCTION WHERE INST_ID='"
	 * + instid + "' and " + F_cardno + " "; //// enctrace(
	 * "3030getting fee details :"+ getfeedetails); gettingcarddetails =
	 * jdbctemplate.queryForList(getfeedetails); return gettingcarddetails;
	 * 
	 * }
	 */

	public List getfeedetails(String instid, String F_cardno,
			JdbcTemplate jdbctemplate) {
		List gettingcarddetails = null;
		/*
		 * String getfeedetails =
		 * "select CARD_NO,HCARD_NO,MCARD_NO,ACCOUNT_NO,CAF_REC_STATUS,FEE_CODE,BRANCH_CODE from CARD_PRODUCTION WHERE INST_ID='"
		 */

		String getfeedetails = "select MCARD_NO,ACCOUNT_NO,CAF_REC_STATUS,FEE_CODE,BRANCH_CODE from CARD_PRODUCTION WHERE INST_ID='"
				+ instid + "' and " + F_cardno + " ";
		enctrace("getting fee details :::   " + getfeedetails);
		gettingcarddetails = jdbctemplate.queryForList(getfeedetails);

		/*
		 * // by gowtham-270819 String getfeedetails =
		 * "select MCARD_NO,ACCOUNT_NO,CAF_REC_STATUS,FEE_CODE,BRANCH_CODE from CARD_PRODUCTION WHERE INST_ID=? and "
		 * + F_cardno + " ";
		 */
		// enctrace("getting fee details ::: " + getfeedetails);
		// gettingcarddetails = jdbctemplate.queryForList(getfeedetails, new
		// Object[] { instid });

		return gettingcarddetails;

	}

	public List getfeedetailsinproc(String instid, String Fee_cardno,
			JdbcTemplate jdbctemplate) {
		List gettingcarddetails = null;

		// BY SIVA
		// String getfeedetails = "select
		// CARD_NO,HCARD_NO,MCARD_NO,ACCT_NO,CAF_REC_STATUS,FEE_CODE,BRANCH_CODE
		// from INST_CARD_PROCESS WHERE INST_ID='"+ instid + "' and " +
		// Fee_cardno + "";

		/*
		 * String getfeedetails =
		 * "select ORG_CHN,(SELECT HCARD_NO FROM INST_CARD_PROCESS_HASH WHERE ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM "
		 * + "INST_CARD_PROCESS WHERE INST_ID='"+ instid + "' and " +
		 * Fee_cardno+
		 * ")) as HCARD_NO,MCARD_NO,ACCT_NO,CAF_REC_STATUS,FEE_CODE," +
		 * "BRANCH_CODE from INST_CARD_PROCESS WHERE INST_ID='"+ instid +
		 * "' and " + Fee_cardno + ""; enctrace("3030getting fee details :" +
		 * getfeedetails); try { gettingcarddetails =
		 * jdbctemplate.queryForList(getfeedetails);
		 */

		// / by gowtham-280819
		String getfeedetails = "select ORG_CHN,(SELECT HCARD_NO FROM INST_CARD_PROCESS_HASH WHERE ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM "
				+ "INST_CARD_PROCESS WHERE INST_ID=? and "
				+ Fee_cardno
				+ ")) as HCARD_NO,MCARD_NO,ACCT_NO,CAF_REC_STATUS,FEE_CODE,"
				+ "BRANCH_CODE from INST_CARD_PROCESS WHERE INST_ID=? and "
				+ Fee_cardno + "";
		enctrace("3030getting fee details :" + getfeedetails);
		try {
			gettingcarddetails = jdbctemplate.queryForList(getfeedetails,
					new Object[] { instid, instid, });

			enctrace("gettingcarddetails" + gettingcarddetails);
		} catch (Exception e) {
			System.out.println("Exception in Fee" + e.getMessage());
			e.printStackTrace();
		}
		return gettingcarddetails;

	}

	public List feeinsertactivity(String instid, String cardno, String hcardno,
			String mcardno, String accountno, String cafflag, String feeid,
			String userid, JdbcTemplate jdbctemplate) {

		String feelistqry = "";
		List feeresultqry = null;
		trace("caf flag status " + cafflag);

		switch (cafflag.trim()) {
		case "A":
			feelistqry = "select FEEAMT,ACTION_CODE from FEE_MASTER,ACTIONCODES WHERE FEE_MASTER.INST_ID=ACTIONCODES.INST_ID AND FEE_MASTER.TXNCODE=ACTIONCODES.TXN_CODE AND"
					+ " FEE_MASTER.INST_ID=? AND FEE_MASTER.FEE_ID=? AND FEE_REQ='1' AND ACTION_CODE like 'NEWCARD'";
			break;
		case "S":
			feelistqry = "select FEEAMT,ACTION_CODE from FEE_MASTER,ACTIONCODES WHERE FEE_MASTER.INST_ID=ACTIONCODES.INST_ID AND FEE_MASTER.TXNCODE=ACTIONCODES.TXN_CODE AND"
					+ " FEE_MASTER.INST_ID=? AND FEE_MASTER.FEE_ID=? AND FEE_REQ='1' AND ACTION_CODE like 'REISSUE'";
			break;
		case "BR":
			feelistqry = "select FEEAMT,ACTION_CODE from FEE_MASTER,ACTIONCODES WHERE FEE_MASTER.INST_ID=ACTIONCODES.INST_ID AND FEE_MASTER.TXNCODE=ACTIONCODES.TXN_CODE AND"
					+ " FEE_MASTER.INST_ID=? AND FEE_MASTER.FEE_ID=? AND FEE_REQ='1' AND ACTION_CODE like 'RENEWAL'";
			break;
		case "BN":
			feelistqry = "select FEEAMT,ACTION_CODE from FEE_MASTER,ACTIONCODES WHERE FEE_MASTER.INST_ID=ACTIONCODES.INST_ID AND FEE_MASTER.TXNCODE=ACTIONCODES.TXN_CODE AND"
					+ " FEE_MASTER.INST_ID=? AND FEE_MASTER.FEE_ID=? AND FEE_REQ='1' AND ACTION_CODE like 'RENEWAL'";
			break;
		case "AC":
			feelistqry = "select FEEAMT,ACTION_CODE from FEE_MASTER,ACTIONCODES WHERE FEE_MASTER.INST_ID=ACTIONCODES.INST_ID AND FEE_MASTER.TXNCODE=ACTIONCODES.TXN_CODE AND"
					+ " FEE_MASTER.INST_ID=? AND FEE_MASTER.FEE_ID=? AND FEE_REQ='1' AND ACTION_CODE like 'ADD-ON'";
			break;
		case "D":
			feelistqry = "select FEEAMT,ACTION_CODE from FEE_MASTER,ACTIONCODES WHERE FEE_MASTER.INST_ID=ACTIONCODES.INST_ID AND FEE_MASTER.TXNCODE=ACTIONCODES.TXN_CODE AND "
					+ "FEE_MASTER.INST_ID=? AND FEE_MASTER.FEE_ID=? AND FEE_REQ='1' AND ACTION_CODE like 'DAMAGE'";
			break;
		case "DE":
			feelistqry = "select FEEAMT,ACTION_CODE from FEE_MASTER,ACTIONCODES WHERE FEE_MASTER.INST_ID=ACTIONCODES.INST_ID AND FEE_MASTER.TXNCODE=ACTIONCODES.TXN_CODE AND"
					+ " FEE_MASTER.INST_ID=? AND FEE_MASTER.FEE_ID=? AND FEE_REQ='1' AND ACTION_CODE like 'DAMAGE'";
			break;
		case "R":
			feelistqry = "select FEEAMT,ACTION_CODE from FEE_MASTER,ACTIONCODES WHERE FEE_MASTER.INST_ID=ACTIONCODES.INST_ID AND FEE_MASTER.TXNCODE=ACTIONCODES.TXN_CODE AND"
					+ " FEE_MASTER.INST_ID=? AND FEE_MASTER.FEE_ID=? AND FEE_REQ='1' AND ACTION_CODE like 'REPIN'";
			break;
		case "NR":
			feelistqry = "select FEEAMT,ACTION_CODE from FEE_MASTER,ACTIONCODES WHERE FEE_MASTER.INST_ID=ACTIONCODES.INST_ID AND FEE_MASTER.TXNCODE=ACTIONCODES.TXN_CODE AND "
					+ "FEE_MASTER.INST_ID=? AND FEE_MASTER.FEE_ID=? AND FEE_REQ='1' AND ACTION_CODE like 'REISSUE'";
			break;
		default:
			feelistqry = "";
			break;
		// NR :New reisse cards ...
		}
		enctrace("3030feelistqry getting fee values " + feelistqry);
		feeresultqry = jdbctemplate.queryForList(feelistqry, new Object[] {
				instid, feeid });

		return feeresultqry;

	}

	public List getCardStatus(String instid, String cardno,
			JdbcTemplate jdbctemplate) {
		List statuscode = null;
		try {

			/*
			 * String statusqry =
			 * "SELECT CARD_STATUS,BIN FROM CARD_PRODUCTION WHERE INST_ID='" +
			 * instid+ "' AND ORG_CHN='" + cardno + "' "; enctrace(
			 * " get statusqry : " + statusqry); statuscode =
			 * jdbctemplate.queryForList(statusqry);
			 */

			// by gowtham-220819
			String statusqry = "SELECT CARD_STATUS,BIN FROM CARD_PRODUCTION WHERE INST_ID=? AND ORG_CHN=? ";
			enctrace(" get statusqry : " + statusqry);
			statuscode = jdbctemplate.queryForList(statusqry, new Object[] {
					instid, cardno, });

		} catch (Exception e) {
			statuscode = null;
			e.printStackTrace();
		}
		return statuscode;
	}

	public List<Map<String, Object>> getAccountdesc(String instid,
			String hcardno, String accountno, JdbcTemplate jdbctemplate)
			throws Exception {
		List<Map<String, Object>> accountrel = null;
		try {
			String statusqry = "SELECT DECODE(ACCOUNTFLAG,'P','PRIMARY ACCOUNT','S','SECONDARY ACCOUNT') AS ACCOUNTDESC FROM EZAUTHREL WHERE INSTID='"
					+ instid
					+ "' and CHN='"
					+ hcardno
					+ "' and ACCOUNTNO='"
					+ accountno + "'";
			// System.out.println( "accountdescqry" + statusqry );
			enctrace("statusqry :" + statusqry);
			accountrel = jdbctemplate.queryForList(statusqry);
		} catch (EmptyResultDataAccessException e) {
			// System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}

		return accountrel;
	}

	public List<Map<String, Object>> getAccountdesc(String instid,
			String accountno, JdbcTemplate jdbctemplate) throws Exception {
		List<Map<String, Object>> accountrel = null;
		try {
			String statusqry = "SELECT CHN,DECODE(ACCOUNTFLAG,'P','PRIMARY ACCOUNT','S','SECONDARY ACCOUNT') AS ACCOUNTDESC FROM EZAUTHREL WHERE INSTID='"
					+ instid + "' and ACCOUNTNO='" + accountno + "'";
			// System.out.println( "accountdescqry" + statusqry );
			enctrace("statusqry_getAccountdesc ::: " + statusqry);
			accountrel = jdbctemplate.queryForList(statusqry);
		} catch (EmptyResultDataAccessException e) {
			// System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}

		return accountrel;
	}

	/*
	 * public List<Map<String, Object>> getAccountdescFromProd(String instid,
	 * String hcardno, String accountno, JdbcTemplate jdbctemplate) {
	 * 
	 * List<Map<String, Object>> accountrel = null; try { String statusqry =
	 * "SELECT DECODE(CAF_REC_STATUS,'AC','SUPPLYMENTARY ') AS ACCOUNTDESC FROM CARD_PRODUCTION WHERE INST_ID='"
	 * + instid + "' and HCARD_NO='" + hcardno + "' "; // System.out.println(
	 * "accountdescqry" + statusqry ); enctrace("statusqry :" + statusqry);
	 * accountrel = jdbctemplate.queryForList(statusqry); } catch
	 * (EmptyResultDataAccessException e) { // System.out.println("Exception: "
	 * + e.getMessage()); e.printStackTrace(); }
	 * 
	 * return accountrel; }
	 */

	public List<Map<String, Object>> getAccountdescFromProd1(String instid,
			String ecardno, String accountno, JdbcTemplate jdbctemplate) {

		List<Map<String, Object>> accountrel = null;
		try {
			String statusqry = "SELECT DECODE(CAF_REC_STATUS,'AC','SUPPLYMENTARY ') AS ACCOUNTDESC FROM CARD_PRODUCTION WHERE INST_ID='"
					+ instid + "' and ORG_CHN='" + ecardno + "' ";
			// System.out.println( "accountdescqry" + statusqry );
			enctrace("statusqry :" + statusqry);
			accountrel = jdbctemplate.queryForList(statusqry);
		} catch (EmptyResultDataAccessException e) {
			// System.out.println("Exception: " + e.getMessage());
			e.printStackTrace();
		}

		return accountrel;
	}

	public int insertfeedetails(String instid, String cardno, String hcardno,
			String mcardno, String accountno, String cafflag, String feeid,
			String userid, HashMap feedetails, String BRANCH_CODE,
			JdbcTemplate jdbctemplate) {
		int insertfeeupdate = -1;
		// String branchcode = accountno.substring(12,14);

		/*
		 * String insertfee =
		 * "INSERT INTO FEE_COLLECTION ( INSTID,CARDNO,HCARD_NO,MCARD_NO,ACCOUNT_NO,FEE_AMOUNT,USERNAME,FEE_GEN_DATE,CARDACTIVITY,CURR_CODE,DR_CR,BRANCH_CODE) VALUES "
		 * ; insertfee += "('" + instid + "','" + cardno + "','" + hcardno +
		 * "','" + mcardno + "','" + accountno + "','"+ feedetails.get("FEEAMT")
		 * + "','" + userid + "',sysdate ,'" + feedetails.get("ACTION_CODE")+
		 * "','1','D',LPAD('" + BRANCH_CODE + "', 3,'0'))"; //// enctrace(
		 * "3030AuditQuery :" +insertfee); insertfeeupdate =
		 * jdbctemplate.update(insertfee);
		 */

		// by gowtham-270819
		String insertfee = "INSERT INTO FEE_COLLECTION ( INSTID,CARDNO,HCARD_NO,MCARD_NO,ACCOUNT_NO,FEE_AMOUNT,USERNAME,FEE_GEN_DATE,CARDACTIVITY,CURR_CODE,DR_CR,BRANCH_CODE) VALUES ";
		insertfee += "(?,?,?,?,?,?,?,sysdate ,?,?,?,LPAD(?,?,?))";
		// // enctrace("3030AuditQuery :" +insertfee);
		insertfeeupdate = jdbctemplate.update(insertfee, new Object[] { instid,
				cardno, hcardno, mcardno, accountno, feedetails.get("FEEAMT"),
				userid, feedetails.get("ACTION_CODE"), "1", "D", BRANCH_CODE,
				"3", "0" });

		return insertfeeupdate;

	}

	public int glinsertfeedetails(String instid, String cardno, String hcardno,
			String mcardno, String accountno, String cafflag, String feeid,
			String userid, HashMap feedetails, String custamt,
			String branchcode, JdbcTemplate jdbctemplate) {
		int glinsertfeeupdate = -1;
		Date date = new Date();

		/*
		 * String glinsertfee =
		 * "INSERT INTO FEE_COLLECTION ( INSTID,CARDNO,HCARD_NO,MCARD_NO,ACCOUNT_NO,FEE_AMOUNT,USERNAME,FEE_GEN_DATE,CARDACTIVITY,CURR_CODE,DR_CR,BRANCH_CODE) VALUES "
		 * ; glinsertfee += "('" + instid + "','" + cardno + "','" + hcardno +
		 * "','" + mcardno + "','" + accountno + "','" + custamt + "','" +
		 * userid + "',sysdate ,'" + feedetails.get("ACTION_CODE") +
		 * "','2','C',LPAD('"+ branchcode + "', 3,'0'))"; //// enctrace(
		 * "3030AuditQuery :" +glinsertfee); glinsertfeeupdate =
		 * jdbctemplate.update(glinsertfee);
		 */

		// by gowtham-270819
		String glinsertfee = "INSERT INTO FEE_COLLECTION ( INSTID,CARDNO,HCARD_NO,MCARD_NO,ACCOUNT_NO,FEE_AMOUNT,USERNAME,FEE_GEN_DATE,CARDACTIVITY,CURR_CODE,DR_CR,BRANCH_CODE) VALUES ";
		glinsertfee += "(?,?,?,?,?,?,?,sysdate ,'"
				+ feedetails.get("ACTION_CODE") + "',?,?,LPAD(?,?,?))";
		// // enctrace("3030AuditQuery :" +glinsertfee);
		glinsertfeeupdate = jdbctemplate.update(glinsertfee, new Object[] {
				instid, cardno, hcardno, mcardno, accountno, custamt, userid,
				"2", "C", branchcode, "3", "0" });

		return glinsertfeeupdate;

	}

	public int taxinsertfeedetails(String instid, String cardno,
			String hcardno, String mcardno, String accountno, String cafflag,
			String feeid, String userid, HashMap feedetails, String taxamount,
			String branchcode, JdbcTemplate jdbctemplate) {
		int glinsertfeeupdate = -1;
		Date date = new Date();

		/*
		 * String glinsertfee =
		 * "INSERT INTO FEE_COLLECTION ( INSTID,CARDNO,HCARD_NO,MCARD_NO,ACCOUNT_NO,FEE_AMOUNT,USERNAME,FEE_GEN_DATE,CARDACTIVITY,CURR_CODE,DR_CR,BRANCH_CODE) VALUES "
		 * ; glinsertfee += "('" + instid + "','" + cardno + "','" + hcardno +
		 * "','" + mcardno + "','" + accountno + "','" + taxamount + "','" +
		 * userid + "',sysdate ,'" + feedetails.get("ACTION_CODE") +
		 * "','3','C',LPAD('"+ branchcode + "', 3,'0'))"; //// enctrace(
		 * "3030AuditQuery :" +glinsertfee); glinsertfeeupdate =
		 * jdbctemplate.update(glinsertfee);
		 */

		// by gowtham-270819

		String glinsertfee = "INSERT INTO FEE_COLLECTION ( INSTID,CARDNO,HCARD_NO,MCARD_NO,ACCOUNT_NO,FEE_AMOUNT,USERNAME,FEE_GEN_DATE,CARDACTIVITY,CURR_CODE,DR_CR,BRANCH_CODE) VALUES ";
		glinsertfee += "(?,?,?,?,?,?,?,sysdate ,?,?,?,LPAD(?,?,?))";
		// // enctrace("3030AuditQuery :" +glinsertfee);
		glinsertfeeupdate = jdbctemplate
				.update(glinsertfee,
						new Object[] { instid, cardno, hcardno, mcardno,
								accountno, taxamount, userid,
								feedetails.get("ACTION_CODE"), "3", "C",
								branchcode, "3", "0" });

		return glinsertfeeupdate;

	}

	public synchronized String getBatchSeqNo(String instid,
			JdbcTemplate jdbctemplate) {
		String batchnoqry = "SELECT BATCH_NO_SEQ FROM SEQUENCE_MASTER WHERE INST_ID='"
				+ instid + "'  AND ROWNUM<=1";
		String batchno = (String) jdbctemplate.queryForObject(batchnoqry,
				String.class);

		String batchnoupdqry = "UPDATE SEQUENCE_MASTER  SET BATCH_NO_SEQ=BATCH_NO_SEQ+1 WHERE INST_ID='"
				+ instid + "' ";
		int x = executeTransaction(batchnoupdqry, jdbctemplate);

		return batchno;
	}

	public List getRenewalFlag(String instid, String order_ref_no,
			JdbcTemplate jdbctemplate) {
		List renewalflag = null;

		/*
		 * String renewalflagqry =
		 * "SELECT RENEWALFLAG, CARD_COLLECT_BRANCH FROM PERS_CARD_ORDER" +
		 * " WHERE INST_ID='" + instid+ "' AND ORDER_REF_NO='" + order_ref_no +
		 * "' AND ROWNUM<=1"; ////
		 * enctrace("3030renewalflag:::"+renewalflagqry); try { renewalflag =
		 * jdbctemplate.queryForList(renewalflagqry);
		 */

		// by gowtham-220819
		String renewalflagqry = "SELECT RENEWALFLAG, CARD_COLLECT_BRANCH FROM PERS_CARD_ORDER"
				+ " WHERE INST_ID=? AND ORDER_REF_NO=?AND ROWNUM<=?";
		// // enctrace("3030renewalflag:::"+renewalflagqry);
		try {
			renewalflag = jdbctemplate.queryForList(renewalflagqry,
					new Object[] { instid, order_ref_no, "1" });

		} catch (EmptyResultDataAccessException e) {
		}
		return renewalflag;
	}

	public List getRenewalFlagByProd(String instid, String Fee_cardno,
			JdbcTemplate jdbctemplate) {
		List renewalflag = null;

		/*
		 * String renewalflagqry =
		 * "SELECT RENEWALFLAG  FROM CARD_PRODUCTION WHERE INST_ID='" + instid+
		 * "' AND ROWNUM<=1 AND " + Fee_cardno + " "; ////
		 * enctrace("3030renewalflagByProd:::"+renewalflagqry); try {
		 * renewalflag = jdbctemplate.queryForList(renewalflagqry);
		 */

		// by gowtham-270819
		String renewalflagqry = "SELECT RENEWALFLAG  FROM CARD_PRODUCTION WHERE INST_ID=? AND ROWNUM<=? AND "
				+ Fee_cardno + " ";
		// // enctrace("3030renewalflagByProd:::"+renewalflagqry);
		try {
			renewalflag = jdbctemplate.queryForList(renewalflagqry,
					new Object[] { instid, "1" });

		} catch (EmptyResultDataAccessException e) {
		}
		return renewalflag;
	}

	public String getSubProductExpPeriodwithCardno(String instid,
			String newhashcardno, JdbcTemplate jdbctemplate) {
		String qryproductdesc = "SELECT EXPIRY_PERIOD FROM INSTPROD_DETAILS WHERE INST_ID='"
				+ instid
				+ "' and SUB_PROD_ID=(select SUB_PROD_ID from CARD_PRODUCTION where INST_ID='"
				+ instid + "' " + newhashcardno + ")";
		enctrace(" EXPIRY_PERIOD " + qryproductdesc);
		if (jdbctemplate.getMaxRows() == 0) {
			String bin_desc = (String) jdbctemplate.queryForObject(
					qryproductdesc, String.class);
			return bin_desc;
		} else {
			return "UNKNOWN PRODUCT";
		}
	}

	/*
	 * /////by gowtham220819 public String
	 * getSubProductExpPeriodwithCardno1(String instid, String cardno,
	 * JdbcTemplate jdbctemplate) { String qryproductdesc =
	 * "SELECT EXPIRY_PERIOD FROM INSTPROD_DETAILS WHERE INST_ID=?" +
	 * " and SUB_PROD_ID=(select SUB_PROD_ID from CARD_PRODUCTION where INST_ID=? AND CARD_NO=?)"
	 * ; enctrace(" EXPIRY_PERIOD " + qryproductdesc); if
	 * (jdbctemplate.getMaxRows() == 0) { String bin_desc = (String)
	 * jdbctemplate.queryForObject(qryproductdesc,new
	 * Object[]{instid,instid,cardno}, String.class); return bin_desc; } else {
	 * return "UNKNOWN PRODUCT"; } }
	 */

	public String getRenewal(String instid, String orderrefno,
			JdbcTemplate jdbctemplate) {
		String feedescqry = "SELECT RENEWALFLAG FROM PERS_CARD_ORDER WHERE INST_ID='"
				+ instid
				+ "' AND ORDER_REF_NO='"
				+ orderrefno
				+ "'  AND ROWNUM <=1";
		// // enctrace("3030Query for getting renewalflag.........:
		// // "+feedescqry);
		// trace("feedescqry__"+feedescqry);
		String feeactdesc = null;
		try {
			feeactdesc = (String) jdbctemplate.queryForObject(feedescqry,
					String.class);
			trace("renewalflag" + feeactdesc);

		} catch (EmptyResultDataAccessException e) {
			feeactdesc = "NOREC";
		}
		return feeactdesc;
	}

	public synchronized String fchCbsRefNoSeq(String instid,
			JdbcTemplate jdbctemplate) {
		int refnolen = 14;
		String refnolenseqqry = "SELECT CBS_REFNO_SEQ FROM SEQUENCE_MASTER WHERE INST_ID='"
				+ instid + "'  AND ROWNUM<=1";
		trace("THIS IS refnolenseqqry----> " + refnolenseqqry);
		String refnoseq = (String) jdbctemplate.queryForObject(refnolenseqqry,
				String.class);
		String refno = this.paddingZero(refnoseq, refnolen);
		trace("THIS IS refnolenseqqry----> " + refnolenseqqry);
		String updqry = "UPDATE SEQUENCE_MASTER  SET CBS_REFNO_SEQ=CBS_REFNO_SEQ+1 WHERE INST_ID='"
				+ instid + "' ";
		int x = executeTransaction(updqry, jdbctemplate);

		return refno;
	}

	public String getBinType(String instid, String bin,
			JdbcTemplate jdbctemplate) throws Exception {
		String bintypeqry = "SELECT CARD_TYPE FROM PRODUCTINFO WHERE INST_ID='"
				+ instid
				+ "' AND BIN='"
				+ bin
				+ "' and AUTH_CODE='1' AND ROWNUM<=1";
		// // enctrace("3030bintype : " + bintypeqry);
		String bintype = (String) jdbctemplate.queryForObject(bintypeqry,
				String.class);
		return bintype;
	}

	public String getOrderrefno(String instid, String cardno,
			String processtype, JdbcTemplate jdbctemplate) throws Exception {
		String tablename = "PERS_CARD_PROCESS";
		if (processtype.equals("INSTANT")) {
			tablename = "INST_CARD_PROCESS";
		}

		String getorderrefnoqry = "SELECT ORDER_REF_NO FROM " + tablename
				+ " WHERE INST_ID='" + instid + "' AND CARD_NO='" + cardno
				+ "' AND ROWNUM <=1";
		trace("getorderrefnoqry__" + getorderrefnoqry);
		String orderrefno = null;
		try {
			orderrefno = (String) jdbctemplate.queryForObject(getorderrefnoqry,
					String.class);
			trace("orderrefno_" + orderrefno);

		} catch (EmptyResultDataAccessException e) {
			orderrefno = NOREC;
		}
		return orderrefno;

	}

	public String getAcctTypeValue(String instid, String subaccttype,
			JdbcTemplate jdbctemplate) throws Exception {
		String accounttype = null;
		try {

			/*
			 * String acctypeqry =
			 * "SELECT ACCTTYPEID FROM ACCTSUBTYPE WHERE INST_ID='" + instid +
			 * "' AND ACCTSUBTYPEID='"+ subaccttype + "' AND ROWNUM<=1"; ////
			 * enctrace("3030entityqry : " + entityqry ); accounttype = (String)
			 * jdbctemplate.queryForObject(acctypeqry, String.class);
			 */

			// / by gowtham-280819
			String acctypeqry = "SELECT ACCTTYPEID FROM ACCTSUBTYPE WHERE INST_ID=? AND ACCTSUBTYPEID=? AND ROWNUM<=?";
			// // enctrace("3030entityqry : " + entityqry );
			accounttype = (String) jdbctemplate.queryForObject(acctypeqry,
					new Object[] { instid, subaccttype, "1" }, String.class);

		} catch (Exception e) {
		}
		return accounttype;
	}

	public List InstgetProductListBySelected(String instid, String cardStatus,
			String mkrstatus, JdbcTemplate jdbctemplate) throws Exception {
		// Condition For Each Stages
		String condition = null;
		if ("01".equals(cardStatus) && "M".equalsIgnoreCase(mkrstatus)) {
			condition = " AND CARD_STATUS='01' AND MKCK_STATUS='M' ";
		} else if ("01".equals(cardStatus) && "P".equalsIgnoreCase(mkrstatus)) {
			condition = " AND CARD_STATUS='01' AND MKCK_STATUS='P' ";
		} else if ("02".equals(cardStatus) && "M".equalsIgnoreCase(mkrstatus)) {
			condition = " AND CARD_STATUS='02' AND MKCK_STATUS='M' ";
		} else if ("02".equals(cardStatus) && "P".equalsIgnoreCase(mkrstatus)) {
			condition = " AND CARD_STATUS='02' AND MKCK_STATUS='P' ";
		} else if ("03".equals(cardStatus) && "M".equalsIgnoreCase(mkrstatus)) {
			condition = " AND CARD_STATUS='03' AND MKCK_STATUS='M' ";
		} else if ("03".equals(cardStatus) && "P".equalsIgnoreCase(mkrstatus)) {
			condition = " AND CARD_STATUS='03' AND MKCK_STATUS='P' ";
		} else if ("04".equals(cardStatus) && "M".equalsIgnoreCase(mkrstatus)) {
			condition = " AND CARD_STATUS='04' AND MKCK_STATUS='M' ";
		} else if ("04".equals(cardStatus) && "P".equalsIgnoreCase(mkrstatus)) {
			condition = " AND CARD_STATUS='04' AND MKCK_STATUS='P' ";
		} else if ("05".equals(cardStatus) && "M".equalsIgnoreCase(mkrstatus)) {
			condition = " AND CARD_STATUS='05' AND MKCK_STATUS='M' ";
		} else if ("06".equals(cardStatus) && "M".equalsIgnoreCase(mkrstatus)) {
			condition = " AND CARD_STATUS='06' AND MKCK_STATUS='M' ";
		}

		/*
		 * String query =
		 * "select distinct PRODUCT_CODE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER a WHERE a.PRODUCT_CODE=b.PRODUCT_CODE)CARD_TYPE_NAME from INST_CARD_PROCESS b WHERE INST_ID='"
		 * + instid + "' " + condition +
		 * " GROUP BY PRODUCT_CODE  ORDER BY PRODUCT_CODE "; enctrace(
		 * " get prod list : " + query);
		 */

		// / by gowtham-280819
		String query = "select distinct PRODUCT_CODE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER a WHERE a.PRODUCT_CODE=b.PRODUCT_CODE)CARD_TYPE_NAME from INST_CARD_PROCESS b WHERE INST_ID=? "
				+ condition
				+ ""
				+ " GROUP BY PRODUCT_CODE  ORDER BY PRODUCT_CODE ";
		enctrace(" get prod list : " + query);

		return (jdbctemplate.queryForList(query, new Object[] { instid }));
	}

	public List InstgetProductListBySelectedforpin(String instid,
			String PRIVILEGE_CODE, String mkrstatus, JdbcTemplate jdbctemplate)
			throws Exception {
		// Condition For Each Stages
		String condition = null;

		String query = "select distinct PRODUCT_CODE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER a WHERE a.PRODUCT_CODE=b.PRODUCT_CODE)CARD_TYPE_NAME from INST_CARD_PROCESS b WHERE INST_ID='"
				+ instid
				+ "' AND PRIVILEGE_CODE='"
				+ PRIVILEGE_CODE
				+ "' GROUP BY PRODUCT_CODE  ORDER BY PRODUCT_CODE ";
		enctrace(" get prod list : " + query);
		return (jdbctemplate.queryForList(query));
	}

	public List InstgetBranchCodefmProcess(String instid, String orderStatus,
			String mkrstatus, JdbcTemplate jdbctemplate) {
		StringBuilder query = new StringBuilder();

		/*
		 * query.append("select distinct a.BRANCH_CODE,a.BRANCH_NAME");
		 * query.append(" from INST_CARD_PROCESS b,BRANCH_MASTER a ");
		 * query.append(
		 * "WHERE a.INST_ID=b.INST_ID and  trim(a.BRANCH_CODE)=trim(b.BRANCH_CODE)"
		 * ); query.append(" and b.INST_ID='" + instid + "' AND b.CARD_STATUS='"
		 * + orderStatus + "' AND b.MKCK_STATUS='"+ mkrstatus +
		 * "' ORDER BY a.BRANCH_CODE "); enctrace(" getBranchCodefmProcess : " +
		 * query.toString()); List branchList =
		 * jdbctemplate.queryForList(query.toString());
		 */

		// by gowtham-270819
		query.append("select distinct a.BRANCH_CODE,a.BRANCH_NAME");
		query.append(" from INST_CARD_PROCESS b,BRANCH_MASTER a ");
		query.append("WHERE a.INST_ID=b.INST_ID and  trim(a.BRANCH_CODE)=trim(b.BRANCH_CODE)");
		query.append(" and b.INST_ID=? AND b.CARD_STATUS=? AND b.MKCK_STATUS=? ORDER BY a.BRANCH_CODE ");
		enctrace(" getBranchCodefmProcess : " + query.toString());
		List branchList = jdbctemplate.queryForList(query.toString(),
				new Object[] { instid, orderStatus, mkrstatus });

		return branchList;
	}

	public List InstgetBranchCodefmProcessforpin(String instid,
			String orderStatus, String mkrstatus, JdbcTemplate jdbctemplate) {
		StringBuilder query = new StringBuilder();
		query.append("select distinct a.BRANCH_CODE,a.BRANCH_NAME");
		query.append(" from INST_CARD_PROCESS b,BRANCH_MASTER a ");
		query.append("WHERE a.INST_ID=b.INST_ID and  trim(a.BRANCH_CODE)=trim(b.BRANCH_CODE)");
		query.append(" and b.INST_ID='" + instid + "' AND b.PRIVILEGE_CODE='"
				+ orderStatus + "' AND b.MKCK_STATUS='" + mkrstatus
				+ "' ORDER BY a.BRANCH_CODE ");

		enctrace(" getBranchCodefmProcess : " + query.toString());
		List branchList = jdbctemplate.queryForList(query.toString());
		return branchList;
	}

	public List insatantissubranch(String instid, String orderStatus,
			String mkrstatus, JdbcTemplate jdbctemplate) {
		StringBuilder query = new StringBuilder();
		query.append("select distinct a.BRANCH_CODE,a.BRANCH_NAME");
		query.append(" from INST_CARD_PROCESS b,BRANCH_MASTER a ");
		query.append("WHERE a.INST_ID=b.INST_ID and  trim(a.BRANCH_CODE)=trim(b.BRANCH_CODE)");
		query.append(" and b.INST_ID='" + instid + "' AND b.PRIVILEGE_CODE='"
				+ orderStatus + "'AND  b.CARD_STATUS='04'  AND b.MKCK_STATUS='"
				+ mkrstatus + "' ORDER BY a.BRANCH_CODE ");

		enctrace(" getBranchCodefmProcess : " + query.toString());
		List branchList = jdbctemplate.queryForList(query.toString());
		return branchList;
	}

	public List instantcardmapping(String instid, String orderStatus,
			String mkrstatus, JdbcTemplate jdbctemplate) {
		StringBuilder query = new StringBuilder();

		/*
		 * query.append("select distinct a.BRANCH_CODE,a.BRANCH_NAME");
		 * query.append(" from INST_CARD_PROCESS b,BRANCH_MASTER a ");
		 * query.append(
		 * "WHERE a.INST_ID=b.INST_ID and  trim(a.BRANCH_CODE)=trim(b.BRANCH_CODE)"
		 * ); query.append(" and b.INST_ID='" + instid +
		 * "'  AND b.CARD_STATUS='" + orderStatus + "'   AND b.MKCK_STATUS='"+
		 * mkrstatus + "' ORDER BY a.BRANCH_CODE "); enctrace(
		 * " getBranchCodefmProcess : " + query.toString()); List branchList =
		 * jdbctemplate.queryForList(query.toString());
		 */

		// / by gowtham-280819
		query.append("select distinct a.BRANCH_CODE,a.BRANCH_NAME");
		query.append(" from INST_CARD_PROCESS b,BRANCH_MASTER a ");
		query.append("WHERE a.INST_ID=b.INST_ID and  trim(a.BRANCH_CODE)=trim(b.BRANCH_CODE)");
		query.append(" and b.INST_ID=?  AND b.CARD_STATUS=?   AND b.MKCK_STATUS=? ORDER BY a.BRANCH_CODE ");
		enctrace(" getBranchCodefmProcess : " + query.toString());
		List branchList = jdbctemplate.queryForList(query.toString(),
				new Object[] { instid, orderStatus, mkrstatus });

		return branchList;
	}

	public List InstgetcollectBranchCodefmProcess(String instid,
			String orderStatus, String mkrstatus, JdbcTemplate jdbctemplate) {
		StringBuilder query = new StringBuilder();
		query.append("select distinct a.BRANCH_CODE,a.BRANCH_NAME");
		query.append(" from INST_CARD_PROCESS b,BRANCH_MASTER a ");
		query.append("WHERE a.INST_ID=b.INST_ID and  trim(a.BRANCH_CODE)=trim(b.CARD_COLLECT_BRANCH)");

		/*
		 * query.append(" and b.INST_ID='" + instid + "' AND b.CARD_STATUS='" +
		 * orderStatus + "' AND b.MKCK_STATUS='"+ mkrstatus +
		 * "' ORDER BY a.BRANCH_CODE "); enctrace(" getBranchCodefmProcess : " +
		 * query.toString()); List branchList =
		 * jdbctemplate.queryForList(query.toString());
		 */

		// / by gowtham-280819
		System.out.println("orderStatus--->"+orderStatus);
		System.out.println("mkrstatus-->"+mkrstatus);
		System.out.println("instid-->"+instid);
		
		query.append(" and b.INST_ID=? AND b.CARD_STATUS=? AND b.MKCK_STATUS=? ORDER BY a.BRANCH_CODE ");
		enctrace(" getBranchCodefmProcess : " + query.toString());
		List branchList = jdbctemplate.queryForList(query.toString(),
				new Object[] { instid, orderStatus, mkrstatus });

		return branchList;
	}

	public List Instgetorderbybranch(String instid, String orderStatus,
			String mkrstatus, JdbcTemplate jdbctemplate) {
		StringBuilder query = new StringBuilder();

		/*
		 * query.append("select distinct a.BRANCH_CODE,a.BRANCH_NAME");
		 * query.append(" from INST_CARD_ORDER b,BRANCH_MASTER a ");
		 * query.append(
		 * "WHERE a.INST_ID=b.INST_ID and  a.BRANCH_CODE=b.BRANCH_CODE");
		 * query.append(" and b.INST_ID='" + instid + "' AND b.ORDER_STATUS='" +
		 * orderStatus + "' AND b.MKCK_STATUS='"+ mkrstatus +
		 * "' ORDER BY a.BRANCH_CODE "); enctrace(" getBranchCodefmProcess : " +
		 * query.toString()); List branchList =
		 * jdbctemplate.queryForList(query.toString());
		 */

		// BY GOWTHAM-270819
		query.append("select distinct a.BRANCH_CODE,a.BRANCH_NAME");
		query.append(" from INST_CARD_ORDER b,BRANCH_MASTER a ");
		query.append("WHERE a.INST_ID=b.INST_ID and  a.BRANCH_CODE=b.BRANCH_CODE");
		query.append(" and b.INST_ID=? AND b.ORDER_STATUS=? AND b.MKCK_STATUS=? ORDER BY a.BRANCH_CODE ");
		enctrace(" getBranchCodefmProcess : " + query.toString());
		List branchList = jdbctemplate.queryForList(query.toString(),
				new Object[] { instid, orderStatus, mkrstatus });

		return branchList;
	}

	public List InstgetorderbyProductList(String instid, String cardStatus,
			String mkrstatus, JdbcTemplate jdbctemplate) throws Exception {
		// Condition For Each Stages
		String condition = null;

		if ("01".equals(cardStatus) && "M".equalsIgnoreCase(mkrstatus)) {
			condition = " AND b.ORDER_STATUS='01' AND b.MKCK_STATUS='M' ";
		} else if ("01".equals(cardStatus) && "P".equalsIgnoreCase(mkrstatus)) {
			condition = " AND b.ORDER_STATUS='01' AND b.MKCK_STATUS='P' ";
		}

		/*
		 * String query =
		 * "select distinct a.PRODUCT_CODE,a.CARD_TYPE_NAME FROM PRODUCT_MASTER a,INST_CARD_ORDER b  WHERE a.INST_ID=b.INST_ID and a.PRODUCT_CODE=b.PRODUCT_CODE and a.INST_ID='"
		 * + instid + "' " + condition + " ORDER BY a.PRODUCT_CODE "; enctrace(
		 * " get prod list : " + query); return
		 * (jdbctemplate.queryForList(query));
		 */

		// by gowtham-270819
		String query = "select distinct a.PRODUCT_CODE,a.CARD_TYPE_NAME FROM PRODUCT_MASTER a,INST_CARD_ORDER b  WHERE a.INST_ID=b.INST_ID and a.PRODUCT_CODE=b.PRODUCT_CODE and a.INST_ID=? "
				+ condition + " ORDER BY a.PRODUCT_CODE ";
		enctrace(" get prod list : " + query);
		return (jdbctemplate.queryForList(query, new Object[] { instid }));

	}

	public String filtercondcollectbranch(String productcode, String branch,
			String fromdate, String todate, String dateflag) {
		String bincond = "";
		String branchcond = "";

		String datecond = "";

		if ("ALL".equalsIgnoreCase(productcode)) {
			bincond = "";
		} else {
			bincond = " AND PRODUCT_CODE='" + productcode + "' ";
		}

		if ("ALL".equalsIgnoreCase(branch)) {
			branchcond = "";
		} else {
			branchcond = " AND trim(CARD_COLLECT_BRANCH)='" + branch.trim()
					+ "'";
		}

		if (fromdate != null && todate != null) {
			datecond = "AND ( to_date('" + fromdate + "', 'dd-mm-yyyy') <= "
					+ dateflag + " AND to_date('" + todate
					+ "', 'dd-mm-yyyy' )+1 >= " + dateflag + ") ";
		}

		String filtercond = bincond + branchcond + datecond;

		return filtercond;

	}

	public int ChangeCustomerStatusReIssueDAO(String instid, String CardNo,
			String ActionCode, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1, y = -1, z = -1;
		String Switch_Query = "";

		Switch_Query += "MERGE INTO EZCARDINFO TBL_A ";
		Switch_Query += "USING (  ";
		Switch_Query += "  SELECT TBL_B.CIN,TBL_B.STATUS_CODE ";
		Switch_Query += "  FROM CARD_PRODUCTION TBL_B WHERE TBL_B.HCARD_NO = '"
				+ CardNo + "' AND TBL_B.INST_ID='" + instid
				+ "' AND TBL_B.STATUS_CODE!='62' AND TBL_B.STATUS_CODE='74'  ";
		Switch_Query += "  ) TMP ON (TBL_A.CUSTID = TMP.CIN) ";
		Switch_Query += "WHEN MATCHED THEN  ";
		Switch_Query += "UPDATE SET TBL_A.STATUS = '" + ActionCode + "'  ";
		enctrace("updatefeeqry :" + Switch_Query);

		/*
		 * String Cardman_Query = ""; Cardman_Query =
		 * "UPDATE CARD_PRODUCTION SET CARD_STATUS='07'," + " STATUS_CODE ='" +
		 * ActionCode+ "',CAF_REC_STATUS='S',REISSUE_DATE=SYSDATE," +
		 * " REISSUE_CNT=REISSUE_CNT+1 WHERE HCARD_NO = '" + CardNo+
		 * "' AND INST_ID='" + instid + "' AND" +
		 * " STATUS_CODE!='62' AND STATUS_CODE='74'"; enctrace("Cardman_Query :"
		 * + Cardman_Query);
		 * 
		 * x = jdbctemplate.update(Switch_Query); y =
		 * jdbctemplate.update(Cardman_Query);
		 */

		// // by gowtham-280819
		String Cardman_Query = "";
		Cardman_Query = "UPDATE CARD_PRODUCTION SET CARD_STATUS=?,"
				+ " STATUS_CODE =? ,CAF_REC_STATUS=? ,REISSUE_DATE=SYSDATE,"
				+ " REISSUE_CNT=REISSUE_CNT+1 WHERE HCARD_NO = ? AND INST_ID=? AND"
				+ " STATUS_CODE!=? AND STATUS_CODE=? ";
		enctrace("Cardman_Query :" + Cardman_Query);

		x = jdbctemplate.update(Switch_Query);
		y = jdbctemplate.update(Cardman_Query, new Object[] { "07", ActionCode,
				"S", CardNo, instid, "62", "74" });

		if (x == -1 || y == -1)
			z = -1;
		if (x == 0 || y == 0)
			z = 0;
		if (x == 1 || y == 1)
			z = 1;

		return z;
	}

	public String getAcctTypeLen(String instid, JdbcTemplate jdbctemplate) {
		String accttypelen = null;

		// by gowtham-190819
		String accttypelenqry = "SELECT ACCOUNT_TYPE_LENGTH FROM INSTITUTION WHERE INST_ID=?";
		// String accttypelenqry = "SELECT ACCOUNT_TYPE_LENGTH FROM INSTITUTION
		// WHERE INST_ID='" + instid + "'";

		enctrace("accttypelenqry  :" + accttypelen);
		try {

			// accttypelen = (String)
			// jdbctemplate.queryForObject(accttypelenqry, String.class);
			accttypelen = (String) jdbctemplate.queryForObject(accttypelenqry,
					new Object[] { instid }, String.class);

		} catch (EmptyResultDataAccessException e) {
		}
		return accttypelen;
	}

	public int checkCardNoExists(String instid, StringBuffer hcardno,
			JdbcTemplate jdbctemplate) {
		int exists = -1;

		/*
		 * String duplicteCheckQry = "SELECT COUNT(1) AS CNT FROM EZCARDINFO" +
		 * " WHERE INSTID='" + instid + "' AND CHN='"+ hcardno + "'"; enctrace(
		 * "duplicteCheckQry :" + duplicteCheckQry); try { exists =
		 * jdbctemplate.queryForInt(duplicteCheckQry);
		 */

		// by gowtham-220819
		String duplicteCheckQry = "SELECT COUNT(1) AS CNT FROM EZCARDINFO"
				+ " WHERE INSTID=? AND CHN=?";
		enctrace("duplicteCheckQry :" + duplicteCheckQry);
		try {
			exists = jdbctemplate.queryForInt(duplicteCheckQry, new Object[] {
					instid, hcardno, });

		} catch (EmptyResultDataAccessException e) {
		}
		return exists;
	}

	public String istviewqry(String instid, String binclear,
			JdbcTemplate jdbcTemplate) {
		String checkPadssEnable = "select switchfiletype from PRODUCTINFO where inst_id='"
				+ instid + "' and bin='" + binclear + "' ";
		enctrace("istviewqry::" + checkPadssEnable);
		return ((String) jdbcTemplate.queryForObject(checkPadssEnable,
				String.class));
	}

	public String getnetwroktype(String instid, String prename,
			JdbcTemplate jdbctemplate) throws Exception {
		String tracktypeqry = "SELECT DISTINCT TRACK_TYPE FROM PERS_PRE_DATA WHERE INST_ID='"
				+ instid + "' AND PRE_NAME='" + prename + "'  AND ROWNUM<=1";
		enctrace("tracktypeqry : " + tracktypeqry);
		String tracktype = (String) jdbctemplate.queryForObject(tracktypeqry,
				String.class);
		return tracktype;
	}

	/*
	 * public List<?> binlistForChecker(String instid,JdbcTemplate jdbctemplate)
	 * { System.out.println("Qu"); List binlist=new ArrayList();
	 * 
	 * String qury="select BIN from PRODUCTINFO where inst_id='"+instid+
	 * "' and AUTH_CODE='0' AND MKCK_STATUS='M'"; enctrace(qury); binlist
	 * =jdbctemplate.queryForList(qury); System.out.println("Getting Bin List"
	 * +binlist); return binlist; }
	 */

	public List getPADSSDetailByIdd(String keyid, JdbcTemplate jdbctemplate)
			throws Exception {
		List padsskey = new ArrayList<>();
		/*
		 * String padssqry = "SELECT DMK ,DPK FROM PADSSKEY WHERE KEYID = '" +
		 * keyid+ "' AND AUTH_CODE='1' ";
		 */
		// enctrace("getPADSSDetailById::" + padssqry);

		// added by gowtham_120819
		String padssqry = "SELECT DMK ,DPK FROM PADSSKEY WHERE KEYID = ? AND AUTH_CODE=? ";
		enctrace("getPADSSDetailById::" + padssqry);
		try {
			padsskey = jdbctemplate.queryForList(padssqry, new Object[] {
					keyid, "1" });
		} catch (Exception e) {
			System.out.println("error sdjbs");
			e.printStackTrace();
		}
		return padsskey;
	}

	// BY SIVA 23-07-2019
	public List getPADSSDetailById1(String keyid, JdbcTemplate jdbctemplate)
			throws Exception {
		List padsskey = null;
		/*
		 * String padssqry = "SELECT DMK ,DPK FROM PADSSKEY WHERE KEYID = '" +
		 * keyid+ "' AND AUTH_CODE='1' "; enctrace("getPADSSDetailById::" +
		 * padssqry);
		 */

		// added by gowtham_120819
		String padssqry = "SELECT DMK ,DPK FROM PADSSKEY WHERE KEYID =? AND AUTH_CODE=?";
		enctrace("getPADSSDetailById::" + padssqry);

		try {
			padsskey = jdbctemplate.queryForList(padssqry, new Object[] {
					keyid, "1" });
		} catch (Exception e) {
		}
		return padsskey;
	}

	/**
	 * This method is used to get the Branch name
	 * 
	 * @param branchCode
	 * @param jdbctemplate
	 * @return
	 */
	public String getBranchName(String branchCode, JdbcTemplate jdbctemplate) {
		String branchNameQry = "SELECT   BRANCH_NAME FROM BRANCH_MASTER  WHERE BRANCH_CODE='"
				+ branchCode + "'";
		enctrace("branchNameQry:::: ::      " + branchNameQry);
		String branchName = "";
		try {
			branchName = (String) jdbctemplate.queryForObject(branchNameQry,
					String.class);
		} catch (Exception e) {
		}
		return branchName;
	}

	/**
	 * 
	 * @param cardNo
	 * @param instid
	 * @param jdbctemplate
	 * @return
	 */
	public String getCinFromProcess(String cardNo, String instid,
			String condition, JdbcTemplate jdbctemplate) {

		String value = "";
		if ("PERS_CIN".equals(condition)) {
			value = "ORDER_REF_NO='" + cardNo + "' ";
		} else {
			value = "ORG_CHN='" + cardNo + "' ";
		}

		String branchNameQry = "SELECT   CIN  FROM PERS_CARD_PROCESS  WHERE "
				+ value + " AND INST_ID='" + instid + "'";
		enctrace("cin query:::: ::      " + branchNameQry);
		String cin = "";
		try {
			cin = (String) jdbctemplate.queryForObject(branchNameQry,
					String.class);
		} catch (Exception e) {
		}
		return cin;
	}

	/**
	 * 
	 * @param cardNo
	 * @param instid
	 * @param jdbctemplate
	 * @return
	 */
	public String getAccountFromProcess(String cardNo, String instid,
			JdbcTemplate jdbctemplate) {

		/*
		 * String value=""; if("PERS_CIN".equals(condition)){
		 * value="ORDER_REF_NO='"+cardNo+"' "; }else{ value="ORG_CHN='"+cardNo+
		 * "' "; }
		 */

		String branchNameQry = "SELECT   CIN  FROM PERS_CARD_PROCESS  WHERE  ORG_CHN='"
				+ cardNo + "' AND INST_ID='" + instid + "'";
		enctrace("Account no query:::: ::      " + branchNameQry);
		String cin = "";
		try {
			cin = (String) jdbctemplate.queryForObject(branchNameQry,
					String.class);
		} catch (Exception e) {
		}
		return cin;
	}

	public String getSubProduct(String instid, String encCard,
			JdbcTemplate jdbctemplate) {

		String subProdId = "", subProdDesc = "";

		try {
			String query = "SELECT SUB_PROD_ID FROM PERS_CARD_PROCESS WHERE ORG_CHN='"
					+ encCard + "'";
			subProdId = (String) jdbctemplate.queryForObject(query,
					String.class);

			if (subProdId != null) {

				String querForSubProdDesc = "SELECT SUB_PRODUCT_NAME FROM INSTPROD_DETAILS WHERE SUB_PROD_ID='"
						+ subProdId + "'";
				subProdDesc = (String) jdbctemplate.queryForObject(
						querForSubProdDesc, String.class);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return subProdDesc;
	}
	
	public String getBulkRefID(String cin, String instid,
			JdbcTemplate jdbctemplate) {

		

		String bulkRegQry = "SELECT  BULK_REG_ID  FROM PERS_CARD_ORDER  WHERE  cin='"
				+ cin + "' AND INST_ID='" + instid + "'";
		String bulkRefId="";
		
		try {
			bulkRefId =  (String) jdbctemplate.queryForObject(bulkRegQry,String.class);
			
			enctrace("bulkRegQry:::: ::      " + bulkRegQry );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bulkRefId;
	}

	
	
	
	public String getCardFlag(String cin, String instid,
			JdbcTemplate jdbctemplate) {

		String cardFlagQry = "SELECT  CARD_FLAG  FROM PERS_CARD_ORDER  WHERE  cin='"
				+ cin + "' AND INST_ID='" + instid + "'";
		String cardFlag="";
		
		try {
			cardFlag =  (String) jdbctemplate.queryForObject(cardFlagQry,String.class);
			enctrace("cardFlagQry:::: ::      " + cardFlagQry );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cardFlag;
	}
	

}// end class
