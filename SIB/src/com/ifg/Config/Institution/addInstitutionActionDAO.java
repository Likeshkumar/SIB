package com.ifg.Config.Institution;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;

import test.Date;

@Transactional
public class addInstitutionActionDAO extends BaseAction {

	public List getListOfInstitution(String doact, JdbcTemplate jdbctemplate) {
		List instlist = null;
		String filtercond = "";
		if (doact.equals("$AUTHORIZED")) {
			filtercond = " AND   AUTH_CODE='1' ";
		} else if (doact.equals("$TOAUTH")) {
			filtercond = " AND   AUTH_CODE='0' ";
		}

		/*
		 * String instlistqry =
		 * "SELECT INST_NAME,INST_ID,DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZATION','1','AUTHORIZED','9','De-Authourized') AS AUTH_CODE,NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') as ADDED_BY,NVL(REMARKS,'--') as REMARKS, DECODE(STATUS, '1','ACTIVE','0','IN-ACTIVE', STATUS) AS STATUS  FROM INSTITUTION WHERE  STATUS !='2' "
		 * +filtercond+" ORDER BY PREFERENCE "; enctrace("instlistqry :" +
		 * instlistqry); instlist = jdbctemplate.queryForList(instlistqry);
		 */

		// by gowtham-130819
		String instlistqry = "SELECT INST_NAME,INST_ID,DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZATION','1','AUTHORIZED','9','De-Authourized') AS AUTH_CODE,NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') as ADDED_BY,NVL(REMARKS,'--') as REMARKS, DECODE(STATUS, '1','ACTIVE','0','IN-ACTIVE', STATUS) AS STATUS  FROM INSTITUTION WHERE  STATUS !=? "
				+ filtercond + " ORDER BY PREFERENCE ";
		enctrace("instlistqry :" + instlistqry);
		instlist = jdbctemplate.queryForList(instlistqry, new Object[] { "2" });
		return instlist;
	}

	public List getListOfInstitutionFromProduction(JdbcTemplate jdbctemplate) {
		List instlist = null;
		/*
		 * //String instlistqry =
		 * "SELECT INST_NAME,INST_ID,DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZATION','1','AUTHORIZED','9','De-Authourized') AS AUTH_CODE,NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') as ADDED_BY,NVL(REMARKS,'--') as REMARKS, DECODE(STATUS, '1','ACTIVE','0','IN-ACTIVE', STATUS) AS STATUS, BRANCHATTCHED  FROM INSTITUTION WHERE  STATUS !='2'  ORDER BY PREFERENCE "
		 * ; String instlistqry =
		 * "SELECT INST_NAME,INST_ID,DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZATION','1','AUTHORIZED','9','De-Authourized') AS AUTH_CODE,NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') as ADDED_BY,NVL(REMARKS,'--') as REMARKS, DECODE(STATUS, '1','ACTIVE','0','IN-ACTIVE', STATUS) AS STATUS, BRANCHATTCHED  FROM INSTITUTION WHERE AUTH_CODE='1' AND STATUS !='2'  ORDER BY PREFERENCE "
		 * ; enctrace("instlistqry :" + instlistqry); instlist =
		 * jdbctemplate.queryForList(instlistqry);
		 */

		// by gowtham-130819
		String instlistqry = "SELECT INST_NAME,INST_ID,DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZATION','1','AUTHORIZED','9','De-Authourized') AS AUTH_CODE,NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') as ADDED_BY,NVL(REMARKS,'--') as REMARKS, DECODE(STATUS, '1','ACTIVE','0','IN-ACTIVE', STATUS) AS STATUS, BRANCHATTCHED  FROM INSTITUTION WHERE AUTH_CODE=?AND STATUS !=?  ORDER BY PREFERENCE ";
		enctrace("instlistqry :" + instlistqry);
		instlist = jdbctemplate.queryForList(instlistqry, new Object[] { "1", "2" });
		System.out.println(instlist);
		return instlist;
	}

	public List getListOfInstitutionFromProductionByInsit(String instid, JdbcTemplate jdbctemplate) {

		if (instid == null) {
			return null;
		}
		String instcond = "";
		if (instid.trim().equals("")) {
			instcond = "";
		} else {
			instcond = " AND INST_ID='" + instid + "'";
			System.out.println(instcond);
		}
		List instlist = null;
		// String instlistqry = "SELECT
		// INST_NAME,INST_ID,DECODE(AUTH_CODE,'0','WAITING FOR
		// AUTHORIZATION','1','AUTHORIZED','9','De-Authourized') AS
		// AUTH_CODE,NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') as
		// ADDED_BY,NVL(REMARKS,'--') as REMARKS, DECODE(STATUS,
		// '1','ACTIVE','0','IN-ACTIVE', STATUS) AS STATUS, BRANCHATTCHED FROM
		// INSTITUTION WHERE STATUS !='2' "+instcond+" ORDER BY PREFERENCE ";
		String instlistqry = "SELECT INST_NAME,INST_ID,DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZATION','1','AUTHORIZED','9','De-Authourized') AS AUTH_CODE,NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') as ADDED_BY,NVL(REMARKS,'--') as REMARKS, DECODE(STATUS, '1','ACTIVE','0','IN-ACTIVE', STATUS) AS STATUS, BRANCHATTCHED  FROM INSTITUTION WHERE AUTH_CODE='1' AND STATUS !='2' "
				+ instcond + "  ORDER BY PREFERENCE ";
		enctrace("instlistqry :" + instlistqry);
		instlist = jdbctemplate.queryForList(instlistqry);
		return instlist;

	}

	public List getInstitutionDetails(String instid, JdbcTemplate jdbctemplate) {
		List instdetails = null;

		/*
		 * String qury_ifpinstitution =
		 * "select INST_ID,INST_NAME,DEPLOY_ID,DEPLOYMENT_TYPE,BIN_COUNT,PREFERENCE,DECODE(BRANCHATTCHED,'Y','YES','N','NO') as BRANCHATTCHED,CHNLEN,ATTACH_BRCODE_CARDTYPE,BASELEN,LOGIN_RETRY_CNT,PIN_RETRY_CNT,BRCODELEN,INST_ADDDATE,BASE_CURRENCY,COUNTRY_CODE,ORD_REF_LEN,CIN_LEN,ACCT_LEN,ACCT_SEQ_NO,DECODE(GL_CODE_GEN,'AUTO','AUTOMATIC','MAN','MANUAL') AS GL_CODE_GEN,INVOICE_TRACE,INVOICE_LEN,DECODE(MAIL_ALERT_REQ,'N','NO','Y','YES') AS MAIL_ALERT_REQ,DECODE(SMS_ALERT_REQ,'N','NO','Y','YES') AS SMS_ALERT_REQ,PHOTOUPLOAD_REQ,CARDREFNO_LEN,PREDISPLAYDAYS,APP_CODE,AUTH_CODE,AUTH_DATE,AUTH_BY,ADDED_BY,ADDED_DATE,REMARKS,ACCOUNT_TYPE_LENGTH,ACCTSUBTYPE_LENGTH,CUSTID_BASEDON,MAXALWD_ADDCARD, MAXALWD_ADDACC, DECODE(PADSS_ENABLE,'Y','YES','N','NO')PADSS_ENABLE,PADSS_ENABLE PADSS_ENABLEVALUE,(SELECT KEYDESC FROM PADSSKEY WHERE KEYID=PADSS_KEY)PADSS_KEY,RENEWAL_PERIODS from INSTITUTION where INST_ID='"
		 * +instid+"'"; enctrace("qury_ifpinstitution : " + qury_ifpinstitution
		 * ); instdetails= jdbctemplate.queryForList(qury_ifpinstitution);
		 */

		// by gowtham-170819
		String qury_ifpinstitution = "select INST_ID,INST_NAME,DEPLOY_ID,DEPLOYMENT_TYPE,BIN_COUNT,PREFERENCE,DECODE(BRANCHATTCHED,'Y','YES','N','NO') as BRANCHATTCHED,CHNLEN,ATTACH_BRCODE_CARDTYPE,BASELEN,LOGIN_RETRY_CNT,PIN_RETRY_CNT,BRCODELEN,INST_ADDDATE,BASE_CURRENCY,COUNTRY_CODE,ORD_REF_LEN,CIN_LEN,ACCT_LEN,ACCT_SEQ_NO,DECODE(GL_CODE_GEN,'AUTO','AUTOMATIC','MAN','MANUAL') AS GL_CODE_GEN,INVOICE_TRACE,INVOICE_LEN,DECODE(MAIL_ALERT_REQ,'N','NO','Y','YES') AS MAIL_ALERT_REQ,DECODE(SMS_ALERT_REQ,'N','NO','Y','YES') AS SMS_ALERT_REQ,PHOTOUPLOAD_REQ,CARDREFNO_LEN,PREDISPLAYDAYS,APP_CODE,AUTH_CODE,AUTH_DATE,AUTH_BY,ADDED_BY,ADDED_DATE,REMARKS,ACCOUNT_TYPE_LENGTH,ACCTSUBTYPE_LENGTH,CUSTID_BASEDON,MAXALWD_ADDCARD, MAXALWD_ADDACC, DECODE(PADSS_ENABLE,'Y','YES','N','NO')PADSS_ENABLE,PADSS_ENABLE PADSS_ENABLEVALUE,(SELECT KEYDESC FROM PADSSKEY WHERE KEYID=PADSS_KEY)PADSS_KEY,RENEWAL_PERIODS from INSTITUTION where INST_ID=?";
		enctrace("qury_ifpinstitution : " + qury_ifpinstitution);
		instdetails = jdbctemplate.queryForList(qury_ifpinstitution, new Object[] { instid });

		return instdetails;
	}

	public List getSeqKeyList(JdbcTemplate jdbctemplate) {
		List keydetails = null;
		/*
		 * String qury_keydetails =
		 * "select KEYID,KEYDESC from PADSSKEY WHERE AUTH_CODE='1' "; enctrace(
		 * "qury_keydetails : " + qury_keydetails ); keydetails=
		 * jdbctemplate.queryForList(qury_keydetails);
		 */

		// added by gowtham130819
		String qury_keydetails = "select KEYID,KEYDESC from PADSSKEY WHERE AUTH_CODE=?";
		enctrace("qury_keydetails : " + qury_keydetails);
		keydetails = jdbctemplate.queryForList(qury_keydetails, new Object[] { "1" });
		return keydetails;
	}

	public int afterInsertTheInsutition(String instid, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;

		trace("Inserting sequance....");
		x = this.insertInitialSequance(instid, jdbctemplate);
		trace("Inserting sequance.....got : " + x);
		if (x < 0) {
			return -1;
		}

		trace("Inserting action codes.....");
		x = this.insertingActionCodes(instid, jdbctemplate);
		trace("Inserting action codes.....got : " + x);
		if (x < 0) {
			return -1;
		}

		/*
		 * trace("Inserting feedesc temp....."); x =
		 * this.insertingFeedescTemp(instid, jdbctemplate); trace(
		 * "Inserting feedesc temp.....t : " + x ); if( x < 0 ){ return -1; }
		 */

		trace("Inserting Bin card mapping.....");
		x = this.insertingBinCardMapping(instid, jdbctemplate);
		trace("Inserting  Bin card mapping.....got : " + x);
		if (x < 0) {
			return -1;
		}

		trace("Inserting insertingacqTemp.....");
		x = this.insertingacqTemp(instid, jdbctemplate);
		trace("insertingacqTemp.....got : " + x);
		if (x < 0) {
			return -1;
		}

		trace("Inserting Maintenence config.....");
		x = this.insertingMaintain(instid, jdbctemplate);
		trace("Inserting Maintenence config.....got : " + x);
		if (x < 0) {
			return -1;
		}

		trace("Inserting Maintenence description.....");
		x = this.insertingMaintainDesc(instid, jdbctemplate);
		trace("Inserting Maintenence description.....got : " + x);
		if (x < 0) {
			return -1;
		}

		trace("Inserting Identity type....");
		x = this.insertingIdentityType(instid, jdbctemplate);
		trace("Inserting Identity type....got : " + x);
		if (x < 0) {
			return -1;
		}

		trace("Inserting default pin mailer...");
		x = this.insertDefaultPinMailer(instid, jdbctemplate);
		trace("Inserting default pin mailer...got : " + x);
		if (x < 0) {
			return -1;
		}

		trace("inserting into switch table");
		StringBuilder asdf = new StringBuilder();
		asdf.append("INSERT INTO EZCURRENCY (INSTID,CURRCODE,SELLINGRATE,BUYINGRATE,MULTIPLY_DIVIDE)  select '" + instid
				+ "', NUMERIC_CODE , '0', '0', '0' from GLOBAL_CURRENCY ");

		trace("asdf::::EZCURRENCY ::::" + asdf.toString());

		int insertezcuu = jdbctemplate.update(asdf.toString());

		trace("Inserted switch status :::" + insertezcuu);

		if (insertezcuu <= 0) {
			return -1;
		}

		/*
		 * trace("Inserting security question..."); x =
		 * this.insertSequerityQuestion(instid, jdbctemplate); trace(
		 * "Inserting security question....got : " + x); if( x < 0 ){ return -1;
		 * }
		 */
		/*
		 * trace("Inserting global cardtype.."); x =
		 * this.insertGlobalCartType(instid, jdbctemplate); trace(
		 * "Inserting security question....got : " + x); if( x < 0 ){ return -1;
		 * }
		 */

		trace("Inserting maker checker properties...");
		x = this.insertMakerCheckerProperties(instid, jdbctemplate);
		trace("Inserting security question....got : " + x);
		if (x < 0) {
			return -1;
		}

		/*
		 * trace("Inserting SwitchTables ......"); x =
		 * this.insertMakerCheckerProperties(instid, jdbctemplate); trace(
		 * "Inserting security question....got : " + x); if( x < 0 ){ return -1;
		 * }
		 */
		/*
		 * trace("Inserting SwitchTables insertSwitchCurrency......"); x =
		 * this.insertSwitchCurrency(instid, jdbctemplate); trace(
		 * "Inserting insertSwitchCurrency...got : " + x); if( x < 0 ){ return
		 * -1; }
		 */

		/*
		 * trace("Inserting credit card related acct type..."); x =
		 * this.creditAcctType(instid, jdbctemplate); trace(
		 * "Inserting credit card related acct type....got : " + x); if( x < 0
		 * ){ return -1; }
		 * 
		 * trace("Inserting credit card pay type..."); x =
		 * this.credtiPayType(instid, jdbctemplate); trace(
		 * "Inserting credit card pay type....got : " + x); if( x < 0 ){ return
		 * -1; }
		 * 
		 * trace("Inserting credit limit details..."); x =
		 * this.creditLimitDetails(instid, jdbctemplate); trace(
		 * "Inserting credit limit details....got : " + x); if( x < 0 ){ return
		 * -1; }
		 * 
		 * trace("Inserting supplimentary details.."); x =
		 * this.creditSupplimentary(instid, jdbctemplate); trace(
		 * "Inserting supplimentary details...got : " + x); if( x < 0 ){ return
		 * -1; }
		 * 
		 * trace("Inserting Credit Master Limit.."); x =
		 * this.creditMasterLimit(instid, jdbctemplate); trace(
		 * "Inserting Credit Master Limit...got : " + x); if( x < 0 ){ return
		 * -1; }
		 */

		return x;

	}

	public int insertInitialSequance(String instid, JdbcTemplate jdbctemplate) {
		int x = -1;

		/*
		 * String sequance_qry =
		 * "INSERT INTO SEQUENCE_MASTER   (INST_ID, ORDER_REFNO, CIN_NO, SUB_PRODUCT_SEQ, CARD_TYPE_SEQ, FEE_CODE_SEQ, FEE_SUBCODE_SEQ, COMMISSION_CODE_SEQ, COMMISSION_SUBCODE_SEQ, DISCOUNT_CODE_SEQ, "
		 * ; sequance_qry +=
		 * " DISCOUNT_SUBCODE_SEQ, DISPUTE_CODE_SEQ, ACCT_SEQ, USER_PROFILE_ID, CARDREFNO_SEQ,   GLSETTLE_SEQNO, VAS_BATCHID, VAS_AIRTIME_SEQ, SERVICEPROVIDER_SEQ,TOPUP_SEQ,RECORD_SEQ,LIMIT_SEQ,PREFILE_SEQ) VALUES  "
		 * ; sequance_qry += "('"+instid+
		 * "', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', 1, '1', '1', '1', '1', '1', '1','1','1','1','1')"
		 * ; enctrace("Inserting sequance query : " + sequance_qry ); x =
		 * jdbctemplate.update(sequance_qry);
		 */

		// by gowtham170819
		String sequance_qry = "INSERT INTO SEQUENCE_MASTER   (INST_ID, ORDER_REFNO, CIN_NO, SUB_PRODUCT_SEQ, CARD_TYPE_SEQ, FEE_CODE_SEQ, FEE_SUBCODE_SEQ, COMMISSION_CODE_SEQ, COMMISSION_SUBCODE_SEQ, DISCOUNT_CODE_SEQ, ";
		sequance_qry += " DISCOUNT_SUBCODE_SEQ, DISPUTE_CODE_SEQ, ACCT_SEQ, USER_PROFILE_ID, CARDREFNO_SEQ,   GLSETTLE_SEQNO, VAS_BATCHID, VAS_AIRTIME_SEQ, SERVICEPROVIDER_SEQ,TOPUP_SEQ,RECORD_SEQ,LIMIT_SEQ,PREFILE_SEQ) VALUES  ";
		sequance_qry += "(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?,?)";
		enctrace("Inserting sequance query : " + sequance_qry);
		x = jdbctemplate.update(sequance_qry, new Object[] { instid, "1", "1", "1", "1", "1", "1", "1", "1", "1", "1",
				"1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1" });

		return x;

	}

	/*
	 * public List getListOfInstitution( String doact, JdbcTemplate jdbctemplate
	 * ){ List instlist = null; String filtercond = ""; if(
	 * doact.equals("$AUTHORIZED")){ filtercond = " AND   AUTH_CODE='1' "; }else
	 * if( doact.equals("$TOAUTH")){ filtercond = " AND   AUTH_CODE='0' "; }
	 * String instlistqry =
	 * "SELECT INST_NAME,INST_ID,DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZATION','1','AUTHORIZED','9','De-Authourized') AS AUTH_CODE,NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') as ADDED_BY,NVL(REMARKS,'--') as REMARKS, DECODE(STATUS, '1','ACTIVE','0','IN-ACTIVE', STATUS) AS STATUS  FROM INSTITUTION WHERE  STATUS !='2' "
	 * +filtercond+" ORDER BY PREFERENCE "; enctrace("instlistqry :" +
	 * instlistqry); instlist = jdbctemplate.queryForList(instlistqry); return
	 * instlist ; }
	 * 
	 * public List getListOfInstitutionFromProduction( JdbcTemplate jdbctemplate
	 * ){ List instlist = null; //String instlistqry =
	 * "SELECT INST_NAME,INST_ID,DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZATION','1','AUTHORIZED','9','De-Authourized') AS AUTH_CODE,NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') as ADDED_BY,NVL(REMARKS,'--') as REMARKS, DECODE(STATUS, '1','ACTIVE','0','IN-ACTIVE', STATUS) AS STATUS, BRANCHATTCHED  FROM INSTITUTION WHERE  STATUS !='2'  ORDER BY PREFERENCE "
	 * ; String instlistqry =
	 * "SELECT INST_NAME,INST_ID,DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZATION','1','AUTHORIZED','9','De-Authourized') AS AUTH_CODE,NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') as ADDED_BY,NVL(REMARKS,'--') as REMARKS, DECODE(STATUS, '1','ACTIVE','0','IN-ACTIVE', STATUS) AS STATUS, BRANCHATTCHED  FROM INSTITUTION WHERE AUTH_CODE='1' AND STATUS !='2'  ORDER BY PREFERENCE "
	 * ; enctrace("instlistqry :" + instlistqry); instlist =
	 * jdbctemplate.queryForList(instlistqry); return instlist ; }
	 * 
	 * public List getListOfInstitutionFromProductionByInsit( String instid,
	 * JdbcTemplate jdbctemplate ){ if( instid ==null){ return null; } String
	 * instcond = ""; if( instid.trim().equals("")){ instcond = ""; }else{
	 * instcond = " AND INST_ID='"+instid+"'"; } List instlist = null; //String
	 * instlistqry =
	 * "SELECT INST_NAME,INST_ID,DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZATION','1','AUTHORIZED','9','De-Authourized') AS AUTH_CODE,NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') as ADDED_BY,NVL(REMARKS,'--') as REMARKS, DECODE(STATUS, '1','ACTIVE','0','IN-ACTIVE', STATUS) AS STATUS, BRANCHATTCHED  FROM INSTITUTION WHERE  STATUS !='2' "
	 * +instcond+"  ORDER BY PREFERENCE "; String instlistqry =
	 * "SELECT INST_NAME,INST_ID,DECODE(AUTH_CODE,'0','WAITING FOR AUTHORIZATION','1','AUTHORIZED','9','De-Authourized') AS AUTH_CODE,NVL(AUTH_BY,'--') as AUTH_BY,NVL(ADDED_BY,'--') as ADDED_BY,NVL(REMARKS,'--') as REMARKS, DECODE(STATUS, '1','ACTIVE','0','IN-ACTIVE', STATUS) AS STATUS, BRANCHATTCHED  FROM INSTITUTION WHERE AUTH_CODE='1' AND STATUS !='2' "
	 * +instcond+"  ORDER BY PREFERENCE "; enctrace("instlistqry :" +
	 * instlistqry); instlist = jdbctemplate.queryForList(instlistqry); return
	 * instlist ; }
	 * 
	 * 
	 * public List getInstitutionDetails( String instid, JdbcTemplate
	 * jdbctemplate ){ List instdetails = null; String qury_ifpinstitution =
	 * "select INST_ID,INST_NAME,DEPLOY_ID,DEPLOYMENT_TYPE,BIN_COUNT,PREFERENCE,DECODE(BRANCHATTCHED,'Y','YES','N','NO') as BRANCHATTCHED,CHNLEN,ATTACH_BRCODE_CARDTYPE,BASELEN,LOGIN_RETRY_CNT,PIN_RETRY_CNT,BRCODELEN,INST_ADDDATE,BASE_CURRENCY,COUNTRY_CODE,ORD_REF_LEN,CIN_LEN,ACCT_LEN,ACCT_SEQ_NO,DECODE(GL_CODE_GEN,'AUTO','AUTOMATIC','MAN','MANUAL') AS GL_CODE_GEN,INVOICE_TRACE,INVOICE_LEN,DECODE(MAIL_ALERT_REQ,'N','NO','Y','YES') AS MAIL_ALERT_REQ,DECODE(SMS_ALERT_REQ,'N','NO','Y','YES') AS SMS_ALERT_REQ,PHOTOUPLOAD_REQ,CARDREFNO_LEN,PREDISPLAYDAYS,APP_CODE,AUTH_CODE,AUTH_DATE,AUTH_BY,ADDED_BY,ADDED_DATE,REMARKS,ACCOUNT_TYPE_LENGTH,ACCTSUBTYPE_LENGTH,CUSTID_BASEDON,MAXALWD_ADDCARD, MAXALWD_ADDACC, DECODE(PADSS_ENABLE,'Y','YES','N','NO')PADSS_ENABLE,PADSS_ENABLE PADSS_ENABLEVALUE,(SELECT KEYDESC FROM PADSSKEY WHERE KEYID=PADSS_KEY)PADSS_KEY,RENEWAL_PERIODS from INSTITUTION where INST_ID='"
	 * +instid+"'"; enctrace("qury_ifpinstitution : " + qury_ifpinstitution );
	 * instdetails= jdbctemplate.queryForList(qury_ifpinstitution); return
	 * instdetails; }
	 * 
	 * 
	 * public List getSeqKeyList(JdbcTemplate jdbctemplate ){ List keydetails =
	 * null; String qury_keydetails =
	 * "select KEYID,KEYDESC from PADSSKEY WHERE AUTH_CODE='1' "; enctrace(
	 * "qury_keydetails : " + qury_keydetails ); keydetails=
	 * jdbctemplate.queryForList(qury_keydetails); return keydetails; }
	 * 
	 * public int afterInsertTheInsutition(String instid, JdbcTemplate
	 * jdbctemplate ) throws Exception { int x = -1;
	 * 
	 * trace("Inserting sequance...."); x = this.insertInitialSequance(instid,
	 * jdbctemplate); trace("Inserting sequance.....got : " + x ); if( x < 0 ){
	 * return -1; }
	 * 
	 * trace("Inserting action codes....."); x =
	 * this.insertingActionCodes(instid, jdbctemplate); trace(
	 * "Inserting action codes.....got : " + x ); if( x < 0 ){ return -1; }
	 * 
	 * 
	 * 
	 * trace("Inserting feedesc temp....."); x =
	 * this.insertingFeedescTemp(instid, jdbctemplate); trace(
	 * "Inserting feedesc temp.....t : " + x ); if( x < 0 ){ return -1; }
	 * 
	 * 
	 * 
	 * trace("Inserting Bin card mapping....."); x =
	 * this.insertingBinCardMapping(instid, jdbctemplate); trace(
	 * "Inserting  Bin card mapping.....got : " + x ); if( x < 0 ){ return -1; }
	 * 
	 * trace("Inserting insertingacqTemp....."); x =
	 * this.insertingacqTemp(instid, jdbctemplate); trace(
	 * "insertingacqTemp.....got : " + x ); if( x < 0 ){ return -1; }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * trace("Inserting Maintenence config....."); x =
	 * this.insertingMaintain(instid, jdbctemplate); trace(
	 * "Inserting Maintenence config.....got : " + x ); if( x < 0 ){ return -1;
	 * }
	 * 
	 * 
	 * trace("Inserting Maintenence description....."); x =
	 * this.insertingMaintainDesc(instid, jdbctemplate); trace(
	 * "Inserting Maintenence description.....got : " + x ); if( x < 0 ){ return
	 * -1; }
	 * 
	 * 
	 * 
	 * trace("Inserting Identity type...."); x =
	 * this.insertingIdentityType(instid, jdbctemplate); trace(
	 * "Inserting Identity type....got : " + x ); if( x < 0 ){ return -1; }
	 * 
	 * 
	 * trace("Inserting default pin mailer..."); x =
	 * this.insertDefaultPinMailer(instid, jdbctemplate); trace(
	 * "Inserting default pin mailer...got : " + x); if( x < 0 ){ return -1; }
	 * 
	 * 
	 * trace("inserting into switch table"); StringBuilder asdf = new
	 * StringBuilder(); asdf.append(
	 * "INSERT INTO EZCURRENCY (INSTID,CURRCODE,SELLINGRATE,BUYINGRATE,MULTIPLY_DIVIDE)  select '"
	 * +instid+"', NUMERIC_CODE , '0', '0', '0' from GLOBAL_CURRENCY ");
	 * 
	 * trace("asdf::::EZCURRENCY ::::"+asdf.toString());
	 * 
	 * int insertezcuu = jdbctemplate.update(asdf.toString());
	 * 
	 * trace("Inserted switch status :::"+insertezcuu);
	 * 
	 * if( insertezcuu <= 0 ){ return -1; }
	 * 
	 * 
	 * trace("Inserting security question..."); x =
	 * this.insertSequerityQuestion(instid, jdbctemplate); trace(
	 * "Inserting security question....got : " + x); if( x < 0 ){ return -1; }
	 * 
	 * 
	 * trace("Inserting global cardtype.."); x =
	 * this.insertGlobalCartType(instid, jdbctemplate); trace(
	 * "Inserting security question....got : " + x); if( x < 0 ){ return -1; }
	 * 
	 * 
	 * trace("Inserting maker checker properties..."); x =
	 * this.insertMakerCheckerProperties(instid, jdbctemplate); trace(
	 * "Inserting security question....got : " + x); if( x < 0 ){ return -1; }
	 * 
	 * 
	 * 
	 * trace("Inserting SwitchTables ......"); x =
	 * this.insertMakerCheckerProperties(instid, jdbctemplate); trace(
	 * "Inserting security question....got : " + x); if( x < 0 ){ return -1; }
	 * 
	 * 
	 * trace("Inserting SwitchTables insertSwitchCurrency......"); x =
	 * this.insertSwitchCurrency(instid, jdbctemplate); trace(
	 * "Inserting insertSwitchCurrency...got : " + x); if( x < 0 ){ return -1; }
	 * 
	 * 
	 * 
	 * 
	 * 
	 * trace("Inserting credit card related acct type..."); x =
	 * this.creditAcctType(instid, jdbctemplate); trace(
	 * "Inserting credit card related acct type....got : " + x); if( x < 0 ){
	 * return -1; }
	 * 
	 * trace("Inserting credit card pay type..."); x =
	 * this.credtiPayType(instid, jdbctemplate); trace(
	 * "Inserting credit card pay type....got : " + x); if( x < 0 ){ return -1;
	 * }
	 * 
	 * trace("Inserting credit limit details..."); x =
	 * this.creditLimitDetails(instid, jdbctemplate); trace(
	 * "Inserting credit limit details....got : " + x); if( x < 0 ){ return -1;
	 * }
	 * 
	 * trace("Inserting supplimentary details.."); x =
	 * this.creditSupplimentary(instid, jdbctemplate); trace(
	 * "Inserting supplimentary details...got : " + x); if( x < 0 ){ return -1;
	 * }
	 * 
	 * trace("Inserting Credit Master Limit.."); x =
	 * this.creditMasterLimit(instid, jdbctemplate); trace(
	 * "Inserting Credit Master Limit...got : " + x); if( x < 0 ){ return -1; }
	 * 
	 * 
	 * return x;
	 * 
	 * }
	 * 
	 * public int insertInitialSequance(String instid, JdbcTemplate jdbctemplate
	 * ){ int x =-1; String sequance_qry =
	 * "INSERT INTO SEQUENCE_MASTER   (INST_ID, ORDER_REFNO, CIN_NO, SUB_PRODUCT_SEQ, CARD_TYPE_SEQ, FEE_CODE_SEQ, FEE_SUBCODE_SEQ, COMMISSION_CODE_SEQ, COMMISSION_SUBCODE_SEQ, DISCOUNT_CODE_SEQ, "
	 * ; sequance_qry +=
	 * " DISCOUNT_SUBCODE_SEQ, DISPUTE_CODE_SEQ, ACCT_SEQ, USER_PROFILE_ID, CARDREFNO_SEQ,   GLSETTLE_SEQNO, VAS_BATCHID, VAS_AIRTIME_SEQ, SERVICEPROVIDER_SEQ,TOPUP_SEQ,RECORD_SEQ,LIMIT_SEQ,PREFILE_SEQ) VALUES  "
	 * ; sequance_qry += "('"+instid+
	 * "', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', 1, '1', '1', '1', '1', '1', '1','1','1','1','1')"
	 * ; enctrace("Inserting sequance query : " + sequance_qry ); x =
	 * jdbctemplate.update(sequance_qry); return x;
	 * 
	 * }
	 */
	public int insertingActionCodes(String instid, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String ACTIONCODESqry = "";
		ACTIONCODESqry = " Insert into ACTIONCODES (INST_ID, ACTION_CODE, ACTION_DESC, TXN_CODE, ACTION_STATUS, TXN_FLAG, LIMIT_FLAG, FEE_REQ, DISP_ORDER) Values ('"
				+ instid + "', 'NEWCARD', 'NEW CARD', '820000', '1', '0', '0', '1', 1) ";
		x = jdbctemplate.update(ACTIONCODESqry);
		ACTIONCODESqry = " Insert into ACTIONCODES (INST_ID, ACTION_CODE, ACTION_DESC, TXN_CODE, ACTION_STATUS, TXN_FLAG, LIMIT_FLAG, FEE_REQ, DISP_ORDER) Values ('"
				+ instid + "', 'REPIN', 'RE PIN', '820001', '1',   '0', '0', '1', 7) ";
		x = jdbctemplate.update(ACTIONCODESqry);
		ACTIONCODESqry = " Insert into ACTIONCODES (INST_ID, ACTION_CODE, ACTION_DESC, TXN_CODE, ACTION_STATUS, TXN_FLAG, LIMIT_FLAG, FEE_REQ, DISP_ORDER) Values ('"
				+ instid + "', 'DAMAGE', 'DAMAGE CARD', '820002', '1',   '0', '0', '1', 2) ";
		x = jdbctemplate.update(ACTIONCODESqry);
		ACTIONCODESqry = " Insert into ACTIONCODES (INST_ID, ACTION_CODE, ACTION_DESC, TXN_CODE, ACTION_STATUS, TXN_FLAG, LIMIT_FLAG, FEE_REQ, DISP_ORDER) Values ('"
				+ instid + "', 'ADD-ON', 'ADD-ON CARD', '820003', '1',   '0', '0', '1', 3) ";
		x = jdbctemplate.update(ACTIONCODESqry);
		ACTIONCODESqry = " Insert into ACTIONCODES (INST_ID, ACTION_CODE, ACTION_DESC, TXN_CODE, ACTION_STATUS, TXN_FLAG, LIMIT_FLAG, FEE_REQ, DISP_ORDER) Values ('"
				+ instid + "', 'SUP-CARD', 'SUPPLEMENTARY CARD', '820004', '1',   '0', '0', '1', 4) ";
		x = jdbctemplate.update(ACTIONCODESqry);
		ACTIONCODESqry = " Insert into ACTIONCODES (INST_ID, ACTION_CODE, ACTION_DESC, TXN_CODE, ACTION_STATUS, TXN_FLAG, LIMIT_FLAG, FEE_REQ, DISP_ORDER) Values ('"
				+ instid + "', 'WITHDRAW', 'ATM WITHDRAWAL', '01', '1',   '0', '1', '0', 1) ";
		x = jdbctemplate.update(ACTIONCODESqry);
		ACTIONCODESqry = " Insert into ACTIONCODES (INST_ID, ACTION_CODE, ACTION_DESC, TXN_CODE, ACTION_STATUS, TXN_FLAG, LIMIT_FLAG, FEE_REQ, DISP_ORDER) Values ('"
				+ instid + "', 'PURCHASE', 'POS PURCHASE', '61', '1',   '0', '1', '0', 3) ";
		x = jdbctemplate.update(ACTIONCODESqry);
		ACTIONCODESqry = " Insert into ACTIONCODES (INST_ID, ACTION_CODE, ACTION_DESC, TXN_CODE, ACTION_STATUS, TXN_FLAG, LIMIT_FLAG, FEE_REQ, DISP_ORDER) Values ('"
				+ instid + "', 'TRANSFER', 'ATM TRANSFER', '40', '1',   '0', '1', '0', 2) ";
		x = jdbctemplate.update(ACTIONCODESqry);
		ACTIONCODESqry = " Insert into ACTIONCODES (INST_ID, ACTION_CODE, ACTION_DESC, TXN_CODE, ACTION_STATUS, TXN_FLAG, LIMIT_FLAG, FEE_REQ, DISP_ORDER) Values ('"
				+ instid + "', 'RENEWAL', 'CARD RENEWAL', '820005', '1',   '0', '0', '1', 5) ";
		x = jdbctemplate.update(ACTIONCODESqry);
		ACTIONCODESqry = " Insert into ACTIONCODES (INST_ID, ACTION_CODE, ACTION_DESC, TXN_CODE, ACTION_STATUS, TXN_FLAG, LIMIT_FLAG, FEE_REQ, DISP_ORDER) Values ('"
				+ instid + "', 'REISSUE', 'CARD REISSUE', '820006', '1',   '0', '0', '1', 6) ";
		x = jdbctemplate.update(ACTIONCODESqry);

		return x;
	}

	public int insertingFeedescTemp(String instid, JdbcTemplate jdbctemplate) throws Exception {
		StringBuilder feedesc = new StringBuilder();
		feedesc.append("Insert into FEE_DESC ");
		feedesc.append("(INST_ID, FEE_CODE, STATUS_FLAG, USER_NAME, INTRODATE, FEE_DESC, AUTH_CODE) ");
		feedesc.append("Values ");
		feedesc.append("('" + instid + "', '1', '1', '" + instid
				+ "', TO_DATE('08/24/2015 19:51:33', 'MM/DD/YYYY HH24:MI:SS'), 'DEFAULT', '1') ");
		int x = jdbctemplate.update(feedesc.toString());
		return x;
	}

	public int insertingBinCardMapping(String instid, JdbcTemplate jdbctemplate) throws Exception {
		String hostmaster = "INSERT INTO BIN_CARD_MAPPING   (INST_ID, BIN, CARD_TYPE) VALUES   ('" + instid
				+ "', 'N', 'N')";
		int x = jdbctemplate.update(hostmaster);
		return x;
	}

	public int insertingacqTemp(String instid, JdbcTemplate jdbctemplate) throws Exception {
		String acqtemp = "Insert into ACQUIRER (INST_ID, ACQUIRERID, ACQUIRERNAME, STATUS) Values ('" + instid + "', '"
				+ instid + "', '" + instid + "', '1')";
		int x = jdbctemplate.update(acqtemp);
		return x;
	}

	public int insertingMaintain(String instid, JdbcTemplate jdbctemplate) throws Exception {
		String maintainconfig = "";
		int x = -1;
		maintainconfig = "Insert into MAINTAIN_CONFIG (INST_ID, CARD_ACT_CODE, APPLICABLE_ACTION, STATUS) Values ('"
				+ instid + "', '05', '02', '1') ";
		x = jdbctemplate.update(maintainconfig);
		maintainconfig = "Insert into MAINTAIN_CONFIG (INST_ID, CARD_ACT_CODE, APPLICABLE_ACTION, STATUS) Values ('"
				+ instid + "', '05', '03', '1') ";
		x = jdbctemplate.update(maintainconfig);
		maintainconfig = "Insert into MAINTAIN_CONFIG (INST_ID, CARD_ACT_CODE, APPLICABLE_ACTION, STATUS) Values ('"
				+ instid + "', '05', '04', '1')";
		x = jdbctemplate.update(maintainconfig);
		maintainconfig = "Insert into MAINTAIN_CONFIG (INST_ID, CARD_ACT_CODE, APPLICABLE_ACTION, STATUS) Values ('"
				+ instid + "', '05', '06', '1') ";
		x = jdbctemplate.update(maintainconfig);
		maintainconfig = "Insert into MAINTAIN_CONFIG (INST_ID, CARD_ACT_CODE, APPLICABLE_ACTION, STATUS) Values ('"
				+ instid + "', '05', '10', '1')";
		x = jdbctemplate.update(maintainconfig);
		maintainconfig = "Insert into MAINTAIN_CONFIG (INST_ID, CARD_ACT_CODE, APPLICABLE_ACTION, STATUS) Values ('"
				+ instid + "', '02', '05', '1')";
		x = jdbctemplate.update(maintainconfig);
		maintainconfig = "Insert into MAINTAIN_CONFIG (INST_ID, CARD_ACT_CODE, APPLICABLE_ACTION, STATUS) Values ('"
				+ instid + "', '02', '03', '1')";
		x = jdbctemplate.update(maintainconfig);
		maintainconfig = "Insert into MAINTAIN_CONFIG (INST_ID, CARD_ACT_CODE, APPLICABLE_ACTION, STATUS) Values ('"
				+ instid + "', '03', '04', '1')";
		x = jdbctemplate.update(maintainconfig);
		maintainconfig = "Insert into MAINTAIN_CONFIG (INST_ID, CARD_ACT_CODE, APPLICABLE_ACTION, STATUS) Values ('"
				+ instid + "', '03', '07', '1')";
		x = jdbctemplate.update(maintainconfig);
		maintainconfig = "Insert into MAINTAIN_CONFIG (INST_ID, CARD_ACT_CODE, APPLICABLE_ACTION, STATUS) Values ('"
				+ instid + "', '01', '06', '1')";
		x = jdbctemplate.update(maintainconfig);
		maintainconfig = "Insert into MAINTAIN_CONFIG (INST_ID, CARD_ACT_CODE, APPLICABLE_ACTION, STATUS) Values ('"
				+ instid + "', '11', '05', '1')";
		x = jdbctemplate.update(maintainconfig);
		maintainconfig = "Insert into MAINTAIN_CONFIG		   (INST_ID, CARD_ACT_CODE) Values ('" + instid
				+ "', '05')";
		x = jdbctemplate.update(maintainconfig);
		maintainconfig = "Insert into MAINTAIN_CONFIG (INST_ID, CARD_ACT_CODE, APPLICABLE_ACTION) Values ('" + instid
				+ "', '05', '09')";
		x = jdbctemplate.update(maintainconfig);
		maintainconfig = "Insert into MAINTAIN_CONFIG (INST_ID, CARD_ACT_CODE, APPLICABLE_ACTION, STATUS) Values ('"
				+ instid + "', '09', '05', '1')";
		x = jdbctemplate.update(maintainconfig);
		return x;
	}

	public int insertingMaintainDesc(String instid, JdbcTemplate jdbctemplate) throws Exception {
		String maintaindesc = "";
		int x = -1;

		maintaindesc = "INSERT INTO MAINTAIN_DESC (CARD_ACT_CODE, STATUS_DESC, STATUS, INST_ID) VALUES ('02', 'TEMP BLOCK', '70', '"
				+ instid + "')";
		x = jdbctemplate.update(maintaindesc);
		maintaindesc = "INSERT INTO MAINTAIN_DESC (CARD_ACT_CODE, STATUS_DESC, STATUS, INST_ID) VALUES ('03', 'LOST / STOLEN', '75', '"
				+ instid + "')";
		x = jdbctemplate.update(maintaindesc);
		maintaindesc = "INSERT INTO MAINTAIN_DESC (CARD_ACT_CODE, STATUS_DESC, STATUS, INST_ID) VALUES ('04', 'CLOSE', '76', '"
				+ instid + "')";
		x = jdbctemplate.update(maintaindesc);
		maintaindesc = "INSERT INTO MAINTAIN_DESC (CARD_ACT_CODE, STATUS_DESC, STATUS, INST_ID) VALUES ('05', 'ACTIVE', '50', '"
				+ instid + "')";
		x = jdbctemplate.update(maintaindesc);
		maintaindesc = "INSERT INTO MAINTAIN_DESC (CARD_ACT_CODE, STATUS_DESC, STATUS, INST_ID) VALUES ('06', 'REPIN', '61', '"
				+ instid + "')";
		x = jdbctemplate.update(maintaindesc);
		maintaindesc = "INSERT INTO MAINTAIN_DESC (CARD_ACT_CODE, STATUS_DESC, STATUS, INST_ID) VALUES ('07', 'REISSUE', '62', '"
				+ instid + "')";
		x = jdbctemplate.update(maintaindesc);
		maintaindesc = "INSERT INTO MAINTAIN_DESC (CARD_ACT_CODE, STATUS_DESC, STATUS, INST_ID) VALUES ('08', 'FIRST USE CARD', '53', '"
				+ instid + "')";
		x = jdbctemplate.update(maintaindesc);
		maintaindesc = "INSERT INTO MAINTAIN_DESC (CARD_ACT_CODE, STATUS_DESC, STATUS, INST_ID) VALUES ('09', 'NOT ACTIVATED', '16', '"
				+ instid + "')";
		x = jdbctemplate.update(maintaindesc);
		maintaindesc = "INSERT INTO MAINTAIN_DESC (CARD_ACT_CODE, STATUS_DESC, STATUS, INST_ID) VALUES ('10', 'DAMAGE', '77', '"
				+ instid + "')";
		x = jdbctemplate.update(maintaindesc);
		maintaindesc = "INSERT INTO MAINTAIN_DESC (CARD_ACT_CODE, STATUS_DESC, STATUS, INST_ID) VALUES ('11', 'PIN LOCKED', '00', '"
				+ instid + "')";
		x = jdbctemplate.update(maintaindesc);
		return x;
	}

	public int insertingIdentityType(String instid, JdbcTemplate jdbctemplate) throws Exception {
		String identitytype = "";
		int x = -1;
		identitytype = "INSERT INTO IDENTYDOC_TYPE (INST_ID,DOC_ID,DOC_TYPE) values ('" + instid
				+ "','001','PASSPORT')";
		x = jdbctemplate.update(identitytype);
		identitytype = "INSERT INTO IDENTYDOC_TYPE (INST_ID,DOC_ID,DOC_TYPE) values ('" + instid + "','999','OTHERS')";
		x = jdbctemplate.update(identitytype);
		identitytype = "INSERT INTO IDENTYDOC_TYPE (INST_ID,DOC_ID,DOC_TYPE) values ('" + instid
				+ "','002','DRIVING LICENCE')";
		x = jdbctemplate.update(identitytype);
		return x;
	}

	public int insertGlSequance(String instid, JdbcTemplate jdbctemplate) throws Exception {
		String glseqqry = "";
		int x = -1;
		glseqqry = "INSERT INTO IFP_GL_SEQUANCE (INST_ID,GL_GRP_SEQ,GL_SEQ,GL_SCHM_SEQ) values ('" + instid
				+ "','1','1','1')";
		x = jdbctemplate.update(glseqqry);
		return x;
	}

	public int insertMailReseveKeys(String instid, JdbcTemplate jdbctemplate) throws Exception {
		String mailreskeys = "";
		int x = -1;
		mailreskeys = "INSERT INTO IFP_MAIL_RESERVED_KEYS (INST_ID, RESV_KEY, KEY_DESC) VALUES   ('" + instid
				+ "', '$USERCODE$', 'ACTION DONE BY THE USER CODE')";
		x = jdbctemplate.update(mailreskeys);
		mailreskeys = "INSERT INTO IFP_MAIL_RESERVED_KEYS (INST_ID, RESV_KEY, KEY_DESC) VALUES   ('" + instid
				+ "', '$USERNAME$', 'ACTION DONE BY THE USER NAME')";
		x = jdbctemplate.update(mailreskeys);
		mailreskeys = "INSERT INTO IFP_MAIL_RESERVED_KEYS (INST_ID, RESV_KEY, KEY_DESC) VALUES   ('" + instid
				+ "', '$KEYMSG$', 'THE KEY MESSAGE SEND FROM CODE')";
		x = jdbctemplate.update(mailreskeys);
		mailreskeys = "INSERT INTO IFP_MAIL_RESERVED_KEYS (INST_ID, RESV_KEY, KEY_DESC) VALUES   ('" + instid
				+ "', '$DATE$', 'CURRENT DATE')";
		x = jdbctemplate.update(mailreskeys);
		mailreskeys = "INSERT INTO IFP_MAIL_RESERVED_KEYS (INST_ID, RESV_KEY, KEY_DESC) VALUES   ('" + instid
				+ "', '$DATETIME$', 'CURRENT DATE WITH TIME')";
		x = jdbctemplate.update(mailreskeys);
		return x;
	}

	public int insertAccountType(String instid, JdbcTemplate jdbctemplate) throws Exception {
		String accttype = "";
		int x = -1;
		accttype = "Insert into EZMMS_ACCOUNTTYPE (INST_ID, ACCT_TYPE, ACCT_TYPE_DESC) Values  ('" + instid
				+ "', 'BPS', 'B-PESA CARD NO')";
		x = jdbctemplate.update(accttype);
		accttype = "Insert into EZMMS_ACCOUNTTYPE  (INST_ID, ACCT_TYPE, ACCT_TYPE_DESC) Values   ('" + instid
				+ "', 'CBS', 'CBS ACCT NO')";
		x = jdbctemplate.update(accttype);
		return x;
	}

	public int insertMerchantUserType(String instid, JdbcTemplate jdbctemplate) throws Exception {
		String accttype = "";
		int x = -1;
		accttype = "INSERT INTO EZMMS_MASTERUSERCODE  (INST_ID, MASTERCODE, MASTERCODEDESC) VALUES  ('" + instid
				+ "', '1', 'ADMIN')";
		x = jdbctemplate.update(accttype);
		accttype = "INSERT INTO EZMMS_MASTERUSERCODE  (INST_ID, MASTERCODE, MASTERCODEDESC) VALUES  ('" + instid
				+ "', '2', 'USER')";
		x = jdbctemplate.update(accttype);
		accttype = "INSERT INTO EZMMS_MASTERUSERCODE  (INST_ID, MASTERCODE, MASTERCODEDESC) VALUES  ('" + instid
				+ "', '3', 'OPERATOR')";
		x = jdbctemplate.update(accttype);
		return x;
	}

	public int insertMmsSequance(String instid, JdbcTemplate jdbctemplate) throws Exception {
		String mmssequance = "";
		int x = -1;
		mmssequance = "INSERT INTO EZMMS_SEQUANCE_MASTER(INST_ID, ACCT_SEQ, DISPUTE_ID_SEQ, MERCHANTID_SEQ, STOREID_SEQ, TERMINALID_SEQ, MERCHANTTYPE_SEQ, RECON_SEQ, SETTLE_SEQ, COMMISSION_SEQ, DISCOUNT_SEQ, FEE_SEQ,WALLET_SEQ,PRICING_SEQ) VALUES ";
		mmssequance += "('" + instid + "', '1', '1', '1', '1', '1', 1, 1, 1, 1, 1, 1,1,1) ";
		x = jdbctemplate.update(mmssequance);
		return x;
	}

	public int insertMmsActionCode(String instid, JdbcTemplate jdbctemplate) throws Exception {
		String mmsactionqry = "";
		int x = -1;
		mmsactionqry = "INSERT INTO EZMMS_ACTIONCODE  (INST_ID, ACTION_CODE, ACTION_DESC, ACTION_TXNTYPE, ACTION_STATUS) VALUES ('"
				+ instid + "', 'COM', 'COMMISION', 'CR', '1')";
		x = jdbctemplate.update(mmsactionqry);
		mmsactionqry = "INSERT INTO EZMMS_ACTIONCODE  (INST_ID, ACTION_CODE, ACTION_DESC, ACTION_TXNTYPE, ACTION_STATUS) VALUES ('"
				+ instid + "', 'DISC', 'DISCOUNT', 'DR', '1')";
		x = jdbctemplate.update(mmsactionqry);
		mmsactionqry = "INSERT INTO EZMMS_ACTIONCODE  (INST_ID, ACTION_CODE, ACTION_DESC, ACTION_TXNTYPE, ACTION_STATUS) VALUES ('"
				+ instid + "', 'FEE', 'FEE', 'CR', '1')";
		x = jdbctemplate.update(mmsactionqry);
		return x;
	}

	public int insertTerminalType(String instid, JdbcTemplate jdbctemplate) throws Exception {
		String terminaltypeqry = "";
		int x = -1;
		terminaltypeqry = "INSERT INTO EZMMS_TERMINALTYPE   (INST_ID, TERMINAL_TYPE, TERMINAL_TYPE_DESC) VALUES   ('"
				+ instid + "', 'CASTLE', 'CASTLE POS')";
		x = jdbctemplate.update(terminaltypeqry);
		return x;
	}

	public int insertServiceCode(String instid, JdbcTemplate jdbctemplate) throws Exception {
		String servicecodeqry = "";
		int x = -1;
		servicecodeqry = "INSERT INTO IFD_SERVICE_CODE (INST_ID, SERVICE_CODE, STATUS) VALUES ('" + instid
				+ "', '221', '1')";
		x = jdbctemplate.update(servicecodeqry);
		return x;
	}

	public int insertDefaultPinMailer(String instid, JdbcTemplate jdbctemplate) throws Exception {
		String pinmailerqry = "";
		int x = -1;
		pinmailerqry = "INSERT INTO PINMAILER_DESC (INST_ID, PINMAILER_ID, PINMAILER_NAME, STATUS) VALUES ('" + instid
				+ "', '00', 'DEFAULT', '1')";
		x = jdbctemplate.update(pinmailerqry);
		pinmailerqry = "INSERT INTO PINMAILER_PROPERTY (PINMAILER_ID, INST_ID, DOCUMENT_TYPE, FIELD_NAME, FIELD_LENGTH,  X_POS, Y_POS, PRINT_REQUIRED, MAILER_HEIGHT, MAILER_LENGHT) VALUES ('00', '"
				+ instid + "', 'A', 'PINNO', '2','2', '2', 'Y', '33', '33')";
		x = jdbctemplate.update(pinmailerqry);
		return x;
	}

	public int insertSequerityQuestion(String instid, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String securityquestion = "";
		securityquestion = "INSERT INTO IFPS_SEQURITY_QUESTION  ( INST_ID, QID, QNAME, STATUS ) VALUES ('" + instid
				+ "', 'Q1', '--------------', '1')";
		x = jdbctemplate.update(securityquestion);

		securityquestion = "INSERT INTO IFPS_SEQURITY_QUESTION  ( INST_ID, QID, QNAME, STATUS ) VALUES ('" + instid
				+ "', 'Q2', '---------------', '1')";
		x = jdbctemplate.update(securityquestion);

		return x;
	}

	public int insertGlobalCartType(String instid, JdbcTemplate jdbctemplate) throws Exception {
		String pinmailerqry = "";
		int x = -1;
		pinmailerqry = "Insert into IFP_GLOBAL_STATIC_CARD_TYPE   (INST_ID, CARD_TYPE_KEY, CARD_TYPE_NAME, PREFERENCE) Values   ('"
				+ instid + "', '$CORP', 'CORPORATE PRODUCT', 2)";
		x = jdbctemplate.update(pinmailerqry);
		pinmailerqry = "Insert into IFP_GLOBAL_STATIC_CARD_TYPE   (INST_ID, CARD_TYPE_KEY, CARD_TYPE_NAME, PREFERENCE) Values   ('"
				+ instid + "', '$GENERAL', 'COMMON PRODUCT', 1)";
		x = jdbctemplate.update(pinmailerqry);
		return x;
	}

	public int insertMakerCheckerProperties(String instid, JdbcTemplate jdbctemplate) throws Exception {
		String makercheckeqry = "";
		int x = -1;
		makercheckeqry = "INSERT INTO MKRCHKR_CONFIG  (INSTID, MENUID) VALUES ";
		makercheckeqry += " ('" + instid
				+ "', '[00, E-002, E-003, E-004, E-005, E-006, D-009, E-001, E-010, E-011, E-012, E-013, E-014, D-015, E-020, D-021, D-022, E-024, 00]')";
		x = jdbctemplate.update(makercheckeqry);
		return x;
	}

	public int insertSwitchCurrency(String instid, JdbcTemplate jdbctemplate) throws Exception {
		String makercheckeqry = "";
		int x = -1;
		makercheckeqry = "INSERT INTO EZCURRENCY  (INSTID, CURRCODE, SELLINGRATE, BUYINGRATE, MULTIPLY_DIVIDE) VALUES ";
		makercheckeqry += " ('" + instid + "', '000','0','0','0')";
		x = jdbctemplate.update(makercheckeqry);
		return x;
	}

	/*
	 * public String insertInstitutionInMainTable(String instid ,String instname
	 * ,String deploymenttype, String orderref,String branchattched,String
	 * brcodelen,String countrycode,String accnumleng,String cid,String
	 * cidbasedon,String mailalertreq,String smsalertreq,String
	 * accttypelength,String acctsubtypelength,String login_retry_cnt,String
	 * pin_retry_cnt,String preference,String maxaddoncards ,String
	 * maxaddonaccounts ,String pcadssenble, String seqkeyvalue,String
	 * currencytype,String authcode,String usercode,String renewalperiods,String
	 * cardtypelength){
	 * 
	 * StringBuilder insttempquery = new StringBuilder();
	 * 
	 * insttempquery.append("INSERT INTO INSTITUTION "); insttempquery.append(
	 * "(INST_ID, INST_NAME, DEPLOY_ID, DEPLOYMENT_TYPE, BIN_COUNT, PREFERENCE, BRANCHATTCHED, CHNLEN, ATTACH_BRCODE_CARDTYPE, BASELEN, LOGIN_RETRY_CNT, PIN_RETRY_CNT, BRCODELEN, INST_ADDDATE, BASE_CURRENCY, COUNTRY_CODE, ORD_REF_LEN, "
	 * ); insttempquery.append(
	 * "CIN_LEN, CUSTID_BASEDON, ACCT_LEN, ACCT_SEQ_NO, GL_CODE_GEN, INVOICE_TRACE, INVOICE_LEN, MAIL_ALERT_REQ, SMS_ALERT_REQ, PHOTOUPLOAD_REQ, CARDREFNO_LEN, PREDISPLAYDAYS, APP_CODE, STATUS, REMARKS, ADDED_DATE, ADDED_BY, AUTH_CODE, AUTH_DATE, AUTH_BY, COMMONUSERMGMT, COURIERTRACK_ENABLED, NEWCARDISSUEFEE, REPINFEE, REISSUEFEE, DAMAGEFEE, BINLEN, CARDTYPELEN, BRANCH_BASEDPRODUCT, ACCOUNT_TYPE_LENGTH, ACCTSUBTYPE_LENGTH, MAXALWD_ADDCARD, MAXALWD_ADDACC, PADSS_ENABLE, PADSS_KEY,RENEWAL_PERIODS) "
	 * ); insttempquery.append("VALUES ");
	 * insttempquery.append("('"+instid.toUpperCase()+"','"+
	 * instname.toUpperCase()+"', '"+ deploymenttype +
	 * "', (select DEPLOYTYPE from DEPLOYMENT where DEPLOYID='"+deploymenttype+
	 * "'),'0' ,'"+preference+"' , '"+branchattched+"' ,'16' ,'','','"
	 * +login_retry_cnt+"' ,'"+pin_retry_cnt+"' ,'"+brcodelen+"',sysdate, '"
	 * +currencytype+"' ,'"+countrycode+"' ,'"+orderref+"' , )");
	 * insttempquery.append("'"+cid+"' ,'"+cidbasedon+"' ,'"+accnumleng+
	 * "' ,'1' ,'N','N','1','"+mailalertreq+"','"+smsalertreq+
	 * "' ,'N' ,'10' ,'1' ,'1' ,'1','',sysdate,'"+usercode+"', '"+authcode+
	 * "' ,sysdate,'"+usercode+"','N','N','N','N','N','N','6','"+cardtypelength+
	 * "','N' ,'"+accttypelength+"','"+acctsubtypelength+"', '"+maxaddoncards+
	 * "' ,'"+maxaddonaccounts+"' ,'"+pcadssenble+"' ,'"+seqkeyvalue+"', '"
	 * +renewalperiods+"' ) ");
	 * enctrace("insertInstitutionInMainTable"+insttempquery.toString()); return
	 * insttempquery.toString(); }
	 * 
	 * public String insertInstitutionInTempTable(String instid ,String instname
	 * ,String deploymenttype, String orderref,String branchattched,String
	 * brcodelen,String countrycode,String accnumleng,String cid,String
	 * cidbasedon,String mailalertreq,String smsalertreq,String
	 * accttypelength,String acctsubtypelength,String login_retry_cnt,String
	 * pin_retry_cnt,String preference,String maxaddoncards , String
	 * maxaddonaccounts ,String pcadssenble,String seqkeyvalue,String
	 * currencytype,String authcode,String usercode,String renewalperiods,String
	 * cardtypelength){
	 * 
	 * StringBuilder instmainquery = new StringBuilder();
	 * 
	 * instmainquery.append("INSERT INTO INSTITUTION "); instmainquery.append(
	 * "(INST_ID, INST_NAME, DEPLOY_ID, DEPLOYMENT_TYPE, BIN_COUNT, PREFERENCE, BRANCHATTCHED, CHNLEN, ATTACH_BRCODE_CARDTYPE, BASELEN, LOGIN_RETRY_CNT, PIN_RETRY_CNT, BRCODELEN, INST_ADDDATE, BASE_CURRENCY, COUNTRY_CODE, ORD_REF_LEN, "
	 * ); instmainquery.append(
	 * "CIN_LEN, CUSTID_BASEDON, ACCT_LEN, ACCT_SEQ_NO, GL_CODE_GEN, INVOICE_TRACE, INVOICE_LEN, MAIL_ALERT_REQ, SMS_ALERT_REQ, PHOTOUPLOAD_REQ, CARDREFNO_LEN, PREDISPLAYDAYS, APP_CODE, STATUS, REMARKS, ADDED_DATE, ADDED_BY, AUTH_CODE, AUTH_DATE, AUTH_BY, COMMONUSERMGMT, COURIERTRACK_ENABLED, NEWCARDISSUEFEE, REPINFEE, REISSUEFEE, DAMAGEFEE, BINLEN, CARDTYPELEN, BRANCH_BASEDPRODUCT, ACCOUNT_TYPE_LENGTH, ACCTSUBTYPE_LENGTH, MAXALWD_ADDCARD, MAXALWD_ADDACC, PADSS_ENABLE, PADSS_KEY,RENEWAL_PERIODS) "
	 * ); instmainquery.append("VALUES ");
	 * instmainquery.append("('"+instid.toUpperCase()+"','"+
	 * instname.toUpperCase()+"', '"+ deploymenttype +
	 * "' ,(select DEPLOYTYPE from DEPLOYMENT where DEPLOYID='"+deploymenttype+
	 * "') ,'0' ,'"+preference+"' , '"+branchattched+"' ,'16' ,'','','"
	 * +login_retry_cnt+"' ,'"+pin_retry_cnt+"' ,'"+brcodelen+"',sysdate, '"
	 * +currencytype+"' ,'"+countrycode+"' ,'"+orderref+"' , ");
	 * instmainquery.append("'"+cid+"' ,'"+cidbasedon+"' ,'"+accnumleng+
	 * "' ,'1' ,'N','N','1','"+mailalertreq+"','"+smsalertreq+
	 * "' ,'N' ,'10' ,'1' ,'1' ,'1','',sysdate,'"+usercode+"', '"+authcode+
	 * "' ,sysdate,'"+usercode+"','N','N','N','N','N','N','6','"+cardtypelength+
	 * "' ,'N' ,'"+accttypelength+"','"+acctsubtypelength+"','"+maxaddoncards+
	 * "' ,'"+maxaddonaccounts+"' ,'"+pcadssenble+"' ,'"+seqkeyvalue+"', '"
	 * +renewalperiods+"'  ) ");
	 * enctrace("insertInstitutionInTempTable"+instmainquery.toString()); return
	 * instmainquery.toString(); }
	 * 
	 * 
	 * public String updateInstitutionInMainTable(String instid,String
	 * instname,String countrycode,String mailalertreq,String smsalertreq,
	 * String status, String authcode,String usercode){ String
	 * update_institution = "UPDATE INSTITUTION SET INST_NAME='"
	 * +instname+"',COUNTRY_CODE='"+countrycode+"',MAIL_ALERT_REQ='"+
	 * mailalertreq+"',SMS_ALERT_REQ='"+smsalertreq+"',STATUS='"+status+
	 * "',AUTH_CODE='"+authcode+"',ADDED_BY='"+usercode+
	 * "',ADDED_DATE=sysdate WHERE INST_ID='"+instid+"'"; trace(
	 * "update_institution == "+update_institution ); return update_institution;
	 * } public String updateInstitutionInTempTable(String instid,String
	 * instname,String countrycode,String mailalertreq,String smsalertreq,
	 * String status,String authcode,String usercode){ String
	 * update_temp_institution = "UPDATE INSTITUTION SET INST_NAME='"
	 * +instname+"',COUNTRY_CODE='"+countrycode+"',MAIL_ALERT_REQ='"+
	 * mailalertreq+"',SMS_ALERT_REQ='"+smsalertreq+"',STATUS='"+status+
	 * "',AUTH_CODE='"+authcode+"',ADDED_BY='"+usercode+
	 * "',ADDED_DATE=sysdate, AUTH_BY='', AUTH_DATE='',REMARKS='' WHERE INST_ID='"
	 * +instid+"'"; trace("update_temp_institution == "
	 * +update_temp_institution); return update_temp_institution; }
	 * 
	 * public int updateAuthInst( String userid,String instid, String remarks,
	 * JdbcTemplate jdbctemplate ) throws Exception { int x =-1; String
	 * update_authdeauth_qury =
	 * "UPDATE INSTITUTION SET AUTH_CODE='1',AUTH_DATE=sysdate,AUTH_BY='"
	 * +userid+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"'"; enctrace(
	 * "update_authdeauth_qury  :" + update_authdeauth_qury ); x =
	 * jdbctemplate.update(update_authdeauth_qury); return x; }
	 * 
	 * public int updateDeAuthInst(String userid, String instid, String remarks,
	 * JdbcTemplate jdbctemplate ) throws Exception { int x=-1; String
	 * update_authdeauth_qury =
	 * "UPDATE INSTITUTION SET AUTH_CODE='9',AUTH_DATE=sysdate,AUTH_BY='"
	 * +userid+"',REMARKS='"+remarks+"', STATUS='1' WHERE INST_ID='"+instid+"'";
	 * enctrace( "update_authdeauth_qury : " + update_authdeauth_qury ); x =
	 * jdbctemplate.update(update_authdeauth_qury); return x; }
	 */
	public String insertInstitutionInMainTable(String instid, String instname, String deploymenttype, String orderref,
			String branchattched, String brcodelen, String countrycode, String accnumleng, String cid,
			String cidbasedon, String mailalertreq, String smsalertreq, String accttypelength, String acctsubtypelength,
			String login_retry_cnt, String pin_retry_cnt, String preference, String maxaddoncards,
			String maxaddonaccounts, String pcadssenble, String seqkeyvalue, String currencytype, String authcode,
			String usercode, String renewalperiods, String cardtypelength) {

		StringBuilder insttempquery = new StringBuilder();

		insttempquery.append("INSERT INTO INSTITUTION ");
		insttempquery.append(
				"(INST_ID, INST_NAME, DEPLOY_ID, DEPLOYMENT_TYPE, BIN_COUNT, PREFERENCE, BRANCHATTCHED, CHNLEN, ATTACH_BRCODE_CARDTYPE, BASELEN, LOGIN_RETRY_CNT, PIN_RETRY_CNT, BRCODELEN, INST_ADDDATE, BASE_CURRENCY, COUNTRY_CODE, ORD_REF_LEN, ");
		insttempquery.append(
				"CIN_LEN, CUSTID_BASEDON, ACCT_LEN, ACCT_SEQ_NO, GL_CODE_GEN, INVOICE_TRACE, INVOICE_LEN, MAIL_ALERT_REQ, SMS_ALERT_REQ, PHOTOUPLOAD_REQ, CARDREFNO_LEN, PREDISPLAYDAYS, APP_CODE, STATUS, REMARKS, ADDED_DATE, ADDED_BY, AUTH_CODE, AUTH_DATE, AUTH_BY, COMMONUSERMGMT, COURIERTRACK_ENABLED, NEWCARDISSUEFEE, REPINFEE, REISSUEFEE, DAMAGEFEE, BINLEN, CARDTYPELEN, BRANCH_BASEDPRODUCT, ACCOUNT_TYPE_LENGTH, ACCTSUBTYPE_LENGTH, MAXALWD_ADDCARD, MAXALWD_ADDACC, PADSS_ENABLE, PADSS_KEY,RENEWAL_PERIODS) ");
		insttempquery.append("VALUES ");
		/*
		 * insttempquery.append("('"+instid.toUpperCase()+"','"+
		 * instname.toUpperCase()+"', '"+ deploymenttype +
		 * "', (select DEPLOYTYPE from DEPLOYMENT where DEPLOYID='"
		 * +deploymenttype+"'),'0' ,'"+preference+"' , '"+branchattched+
		 * "' ,'16' ,'','','"+login_retry_cnt+"' ,'"+pin_retry_cnt+"' ,'"
		 * +brcodelen+"',sysdate, '"+currencytype+"' ,'"+countrycode+"' ,'"
		 * +orderref+"' , )"); insttempquery.append("'"+cid+"' ,'"+cidbasedon+
		 * "' ,'"+accnumleng+"' ,'1' ,'N','N','1','"
		 * +mailalertreq+"','"+smsalertreq+
		 * "' ,'N' ,'10' ,'1' ,'1' ,'1','',sysdate,'"+usercode+"', '"+authcode+
		 * "' ,sysdate,'"
		 * +usercode+"','N','N','N','N','N','N','6','"+cardtypelength+"','N' ,'"
		 * +accttypelength+"','"+acctsubtypelength+"', '"+maxaddoncards+"' ,'"
		 * +maxaddonaccounts+"' ,'"+pcadssenble+"' ,'"+seqkeyvalue+"', '"
		 * +renewalperiods+"' ) ");
		 * enctrace("insertInstitutionInMainTable"+insttempquery.toString());
		 */

		// added by gowtham-130819
		insttempquery.append("(?,?,?,(select DEPLOYTYPE from DEPLOYMENT where DEPLOYID=?),?,?,?,?,?,?,?,?,?,?,?,?,?,)");
		insttempquery.append(" ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		enctrace("insertInstitutionInMainTable" + insttempquery.toString());

		return insttempquery.toString();
	}

	public String insertInstitutionInTempTable(String instid, String instname, String deploymenttype, String orderref,
			String branchattched, String brcodelen, String countrycode, String accnumleng, String cid,
			String cidbasedon, String mailalertreq, String smsalertreq, String accttypelength, String acctsubtypelength,
			String login_retry_cnt, String pin_retry_cnt, String preference, String maxaddoncards,
			String maxaddonaccounts, String pcadssenble, String seqkeyvalue, String currencytype, String authcode,
			String usercode, String renewalperiods, String cardtypelength) {

		StringBuilder instmainquery = new StringBuilder();

		instmainquery.append("INSERT INTO INSTITUTION ");
		instmainquery.append(
				"(INST_ID, INST_NAME, DEPLOY_ID, DEPLOYMENT_TYPE, BIN_COUNT, PREFERENCE, BRANCHATTCHED, CHNLEN, ATTACH_BRCODE_CARDTYPE, BASELEN, LOGIN_RETRY_CNT, PIN_RETRY_CNT, BRCODELEN, INST_ADDDATE, BASE_CURRENCY, COUNTRY_CODE, ORD_REF_LEN, ");
		instmainquery.append(
				"CIN_LEN, CUSTID_BASEDON, ACCT_LEN, ACCT_SEQ_NO, GL_CODE_GEN, INVOICE_TRACE, INVOICE_LEN, MAIL_ALERT_REQ, SMS_ALERT_REQ, PHOTOUPLOAD_REQ, CARDREFNO_LEN, PREDISPLAYDAYS, APP_CODE, STATUS, REMARKS, ADDED_DATE, ADDED_BY, AUTH_CODE, AUTH_DATE, AUTH_BY, COMMONUSERMGMT, COURIERTRACK_ENABLED, NEWCARDISSUEFEE, REPINFEE, REISSUEFEE, DAMAGEFEE, BINLEN, CARDTYPELEN, BRANCH_BASEDPRODUCT, ACCOUNT_TYPE_LENGTH, ACCTSUBTYPE_LENGTH, MAXALWD_ADDCARD, MAXALWD_ADDACC, PADSS_ENABLE, PADSS_KEY,RENEWAL_PERIODS) ");
		instmainquery.append("VALUES ");

		/*
		 * instmainquery.append("('"+instid.toUpperCase()+"','"+
		 * instname.toUpperCase()+"', '"+ deploymenttype +
		 * "' ,(select DEPLOYTYPE from DEPLOYMENT where DEPLOYID='"
		 * +deploymenttype+"') ,'0' ,'"+preference+"' , '"+branchattched+
		 * "' ,'16' ,'','','"+login_retry_cnt+"' ,'"+pin_retry_cnt+"' ,'"
		 * +brcodelen+"',sysdate, '"+currencytype+"' ,'"+countrycode+"' ,'"
		 * +orderref+"' , "); instmainquery.append("'"+cid+"' ,'"+cidbasedon+
		 * "' ,'"+accnumleng+"' ,'1' ,'N','N','1','"
		 * +mailalertreq+"','"+smsalertreq+
		 * "' ,'N' ,'10' ,'1' ,'1' ,'1','',sysdate,'"+usercode+"', '"+authcode+
		 * "' ,sysdate,'"
		 * +usercode+"','N','N','N','N','N','N','6','"+cardtypelength+
		 * "' ,'N' ,'"
		 * +accttypelength+"','"+acctsubtypelength+"','"+maxaddoncards+"' ,'"
		 * +maxaddonaccounts+"' ,'"+pcadssenble+"' ,'"+seqkeyvalue+"', '"
		 * +renewalperiods+"'  ) ");
		 * enctrace("insertInstitutionInTempTable"+instmainquery.toString());
		 */

		// by gowtham-130819
		instmainquery.append("(?,?,?,(select DEPLOYTYPE from DEPLOYMENT where DEPLOYID=?),?,?,?,?,?,?,?,?,?,?,?,?,?,");
		instmainquery.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		enctrace("insertInstitutionInTempTable" + instmainquery.toString());

		return instmainquery.toString();
	}

	public String updateInstitutionInMainTable(String instid, String instname, String countrycode, String mailalertreq,
			String smsalertreq, String status, String authcode, String usercode) {

		/*
		 * String update_institution = "UPDATE INSTITUTION SET INST_NAME='"
		 * +instname+"',COUNTRY_CODE='"+countrycode+"',MAIL_ALERT_REQ='"+
		 * mailalertreq+"',SMS_ALERT_REQ='"+smsalertreq+"',STATUS='"+status+
		 * "',AUTH_CODE='"+authcode+"',ADDED_BY='"+usercode+
		 * "',ADDED_DATE=sysdate WHERE INST_ID='"+instid+"'"; trace(
		 * "update_institution == "+update_institution );
		 */

		// by gowtham170819
		String update_institution = "UPDATE INSTITUTION SET INST_NAME=?,COUNTRY_CODE=?,MAIL_ALERT_REQ=?,SMS_ALERT_REQ=?,STATUS=?,AUTH_CODE=?,ADDED_BY=?,ADDED_DATE=? WHERE INST_ID=?";
		trace("update_institution == " + update_institution);

		return update_institution;
	}

	public String updateInstitutionInTempTable(String instid, String instname, String countrycode, String mailalertreq,
			String smsalertreq, String status, String authcode, String usercode) {

		/*
		 * String update_temp_institution = "UPDATE INSTITUTION SET INST_NAME='"
		 * +instname+"',COUNTRY_CODE='"+countrycode+"',MAIL_ALERT_REQ='"+
		 * mailalertreq+"',SMS_ALERT_REQ='"+smsalertreq+"',STATUS='"+status+
		 * "',AUTH_CODE='"+authcode+"',ADDED_BY='"+usercode+
		 * "',ADDED_DATE=sysdate, AUTH_BY='', AUTH_DATE='',REMARKS='' WHERE INST_ID='"
		 * +instid+"'"; trace("update_temp_institution == "
		 * +update_temp_institution);
		 */

		String update_temp_institution = "UPDATE INSTITUTION SET INST_NAME=?,COUNTRY_CODE=?,MAIL_ALERT_REQ=?,SMS_ALERT_REQ=?,STATUS=?,AUTH_CODE=?,ADDED_BY=?,ADDED_DATE=?, AUTH_BY=?, AUTH_DATE=?,REMARKS=? WHERE INST_ID=?";
		trace("update_temp_institution == " + update_temp_institution);

		return update_temp_institution;
	}

	public int updateAuthInst(String userid, String instid, String remarks, JdbcTemplate jdbctemplate)
			throws Exception {
		int x = -1;

		Date date = new Date();

		/*
		 * String update_authdeauth_qury =
		 * "UPDATE INSTITUTION SET AUTH_CODE='1',AUTH_DATE=sysdate,AUTH_BY='"
		 * +userid+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"'";
		 * enctrace("update_authdeauth_qury  :" + update_authdeauth_qury ); x =
		 * jdbctemplate.update(update_authdeauth_qury);
		 */

		// by gowtham-170819
		String update_authdeauth_qury = "UPDATE INSTITUTION SET AUTH_CODE=?,AUTH_DATE=?,AUTH_BY=?,REMARKS=? WHERE INST_ID=?";
		enctrace("update_authdeauth_qury  :" + update_authdeauth_qury);
		x = jdbctemplate.update(update_authdeauth_qury,
				new Object[] { "1", date.getCurrentDate(), userid, remarks, instid });

		return x;
	}

	public int updateDeAuthInst(String userid, String instid, String remarks, JdbcTemplate jdbctemplate)
			throws Exception {
		int x = -1;

		Date date = new Date();

		/*
		 * String update_authdeauth_qury =
		 * "UPDATE INSTITUTION SET AUTH_CODE='9',AUTH_DATE=sysdate,AUTH_BY='"
		 * +userid+"',REMARKS='"+remarks+"', STATUS='1' WHERE INST_ID='"
		 * +instid+"'"; enctrace( "update_authdeauth_qury : " +
		 * update_authdeauth_qury ); x =
		 * jdbctemplate.update(update_authdeauth_qury);
		 */

		// by gowtham-170819
		String update_authdeauth_qury = "UPDATE INSTITUTION SET AUTH_CODE=?,AUTH_DATE=?,AUTH_BY=?,REMARKS=? WHERE INST_ID=?";
		enctrace("update_authdeauth_qury  :" + update_authdeauth_qury);
		x = jdbctemplate.update(update_authdeauth_qury,
				new Object[] { "1", date.getCurrentDate(), userid, remarks, instid });

		return x;
	}

	public int insertInstMaintable(String instid, JdbcTemplate jdbctemplate) {
		int x = -1;
		String insert_main_qury = "INSERT INTO INSTITUTION (INST_ID, INST_NAME, DEPLOY_ID, DEPLOYMENT_TYPE, BIN_COUNT, PREFERENCE, BRANCHATTCHED, CHNLEN, ATTACH_BRCODE_CARDTYPE, BASELEN, LOGIN_RETRY_CNT, PIN_RETRY_CNT, BRCODELEN, INST_ADDDATE, BASE_CURRENCY, COUNTRY_CODE, ORD_REF_LEN, CIN_LEN, CUSTID_BASEDON, ACCT_LEN, ACCT_SEQ_NO, GL_CODE_GEN, INVOICE_TRACE, INVOICE_LEN, MAIL_ALERT_REQ, SMS_ALERT_REQ, PHOTOUPLOAD_REQ, CARDREFNO_LEN, PREDISPLAYDAYS, APP_CODE, STATUS, REMARKS, ADDED_DATE, ADDED_BY, AUTH_CODE, AUTH_DATE, AUTH_BY, COMMONUSERMGMT, COURIERTRACK_ENABLED, NEWCARDISSUEFEE, REPINFEE, REISSUEFEE, DAMAGEFEE, BINLEN, CARDTYPELEN, BRANCH_BASEDPRODUCT, ACCOUNT_TYPE_LENGTH, ACCTSUBTYPE_LENGTH,MAXALWD_ADDCARD, MAXALWD_ADDACC, PADSS_ENABLE, PADSS_KEY,RENEWAL_PERIODS)"
				+ "(SELECT INST_ID, INST_NAME, DEPLOY_ID, DEPLOYMENT_TYPE, BIN_COUNT, PREFERENCE, BRANCHATTCHED, CHNLEN, ATTACH_BRCODE_CARDTYPE, BASELEN, LOGIN_RETRY_CNT, PIN_RETRY_CNT, BRCODELEN, INST_ADDDATE, BASE_CURRENCY, COUNTRY_CODE, ORD_REF_LEN, CIN_LEN, CUSTID_BASEDON, ACCT_LEN, ACCT_SEQ_NO, GL_CODE_GEN, INVOICE_TRACE, INVOICE_LEN, MAIL_ALERT_REQ, SMS_ALERT_REQ, PHOTOUPLOAD_REQ, CARDREFNO_LEN, PREDISPLAYDAYS, APP_CODE, STATUS, REMARKS, ADDED_DATE, ADDED_BY, AUTH_CODE, AUTH_DATE, AUTH_BY, COMMONUSERMGMT, COURIERTRACK_ENABLED, NEWCARDISSUEFEE, REPINFEE, REISSUEFEE, DAMAGEFEE, BINLEN, CARDTYPELEN, BRANCH_BASEDPRODUCT, ACCOUNT_TYPE_LENGTH, ACCTSUBTYPE_LENGTH,MAXALWD_ADDCARD, MAXALWD_ADDACC, PADSS_ENABLE, PADSS_KEY,RENEWAL_PERIODS FROM INSTITUTION WHERE INST_ID='"
				+ instid + "')";
		enctrace(" insert_main_qury  : " + insert_main_qury);
		x = jdbctemplate.update(insert_main_qury);
		return x;
	}

	public int getmaintablestatus(String instid, JdbcTemplate jdbctemplate) {
		String count_main_qury = "SELECT COUNT(*) FROM INSTITUTION WHERE INST_ID='" + instid + "'";
		System.out.println("--- count_main_qury --- " + count_main_qury);
		int cntinst = jdbctemplate.queryForInt(count_main_qury);
		return cntinst;
	}

	public int deleteProductionInstitute(String instid, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String deleteinstqry = "DELETE FROM INSTITUTION WHERE INST_ID='" + instid + "' ";
		enctrace("deleteinstqry :" + deleteinstqry);
		x = jdbctemplate.update(deleteinstqry);
		return x;
	}

	// Credit Card Related
	protected int creditAcctType(String instid, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String deleteinstqry = "Insert into IFC_ACCTTYPE  (INST_ID, ACCTTYPEID, ACCTTYPEDESC, ACCTTYPESTATUS) Values  ('"
				+ instid + "', '01', 'SAVINGS', '1')";
		enctrace("deleteinstqry :" + deleteinstqry);
		x = jdbctemplate.update(deleteinstqry);
		deleteinstqry = "Insert into IFC_ACCTTYPE  (INST_ID, ACCTTYPEID, ACCTTYPEDESC, ACCTTYPESTATUS) Values  ('"
				+ instid + "', '02', 'CURRENT', '1')";
		enctrace("deleteinstqry :" + deleteinstqry);
		x = jdbctemplate.update(deleteinstqry);
		return x;
	}

	protected int credtiPayType(String instid, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String deleteinstqry = "Insert into IFC_PAYTYPE  (INST_ID, PAYTYPEID, PAYTYPEVALUE, PAYSTATUS) Values  ('"
				+ instid + "', '$TOTDUE', 'TOTAL DUE AMOUNT -  100%', '1')";
		enctrace("deleteinstqry :" + deleteinstqry);
		x = jdbctemplate.update(deleteinstqry);
		deleteinstqry = "Insert into IFC_PAYTYPE  (INST_ID, PAYTYPEID, PAYTYPEVALUE, PAYSTATUS) Values  ('" + instid
				+ "', '$FIXED', 'FIXED', '1')";
		enctrace("deleteinstqry :" + deleteinstqry);
		x = jdbctemplate.update(deleteinstqry);
		deleteinstqry = "Insert into IFC_PAYTYPE  (INST_ID, PAYTYPEID, PAYTYPEVALUE, PAYSTATUS) Values  ('" + instid
				+ "', '$MINDUE', 'MIN DUE AMOUNT  - 10%', '1')";
		enctrace("deleteinstqry :" + deleteinstqry);
		x = jdbctemplate.update(deleteinstqry);
		return x;
	}

	protected int creditLimitDetails(String instid, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String deleteinstqry = "Insert into IFC_CREDITLIMIT   (INST_ID, CRLIMITID, CRLIMITDESC, CRLIMITSTATUS) Values  ('"
				+ instid + "', '$100', '100% PAYABLE', '1')";
		enctrace("deleteinstqry :" + deleteinstqry);
		x = jdbctemplate.update(deleteinstqry);
		deleteinstqry = "Insert into IFC_CREDITLIMIT  (INST_ID, CRLIMITID, CRLIMITDESC, CRLIMITSTATUS) Values  ('"
				+ instid + "', '$10', '10% PAYABLE', '1')";
		enctrace("deleteinstqry :" + deleteinstqry);
		x = jdbctemplate.update(deleteinstqry);
		return x;
	}

	protected int creditSupplimentary(String instid, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String deleteinstqry = "INSERT INTO IFC_SUPPLIMENTRELATION  (INST_ID, RELATIONCODE, RELATIONDESC, GENDER, STATUS) VALUES  ('"
				+ instid + "', '$WIFE', 'WIFE', 'M', '1')";
		enctrace("deleteinstqry :" + deleteinstqry);
		x = jdbctemplate.update(deleteinstqry);
		deleteinstqry = "INSERT INTO IFC_SUPPLIMENTRELATION  (INST_ID, RELATIONCODE, RELATIONDESC, GENDER, STATUS) VALUES  ('"
				+ instid + "', '$DAUGHT', 'DAUGHTER', 'F', '1')";
		enctrace("deleteinstqry :" + deleteinstqry);
		x = jdbctemplate.update(deleteinstqry);
		deleteinstqry = "INSERT INTO IFC_SUPPLIMENTRELATION  (INST_ID, RELATIONCODE, RELATIONDESC, GENDER, STATUS) VALUES  ('"
				+ instid + "', '$MOT', 'MOTHER', 'F', '1')";
		enctrace("deleteinstqry :" + deleteinstqry);
		x = jdbctemplate.update(deleteinstqry);
		deleteinstqry = "INSERT INTO IFC_SUPPLIMENTRELATION  (INST_ID, RELATIONCODE, RELATIONDESC, GENDER, STATUS) VALUES  ('"
				+ instid + "', '$SON', 'SON', 'M', '1')";
		enctrace("deleteinstqry :" + deleteinstqry);
		x = jdbctemplate.update(deleteinstqry);
		deleteinstqry = "INSERT INTO IFC_SUPPLIMENTRELATION  (INST_ID, RELATIONCODE, RELATIONDESC, GENDER, STATUS) VALUES  ('"
				+ instid + "', '$FAT', 'FATHER', 'M', '1')";
		enctrace("deleteinstqry :" + deleteinstqry);
		x = jdbctemplate.update(deleteinstqry);
		return x;
	}

	protected int creditMasterLimit(String instid, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String deleteinstqry = "INSERT INTO IFC_MASTERLIMIT  (INST_ID, CRLIMITID, CRLIMITNAME, CRSTATUS) VALUES  ('"
				+ instid + "', '1000', '10%', '1')";
		enctrace("deleteinstqry :" + deleteinstqry);
		x = jdbctemplate.update(deleteinstqry);
		return x;
	}

}
