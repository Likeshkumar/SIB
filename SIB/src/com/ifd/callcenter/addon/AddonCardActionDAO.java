package com.ifd.callcenter.addon;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;

public final class AddonCardActionDAO extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	CommonDesc commondesc = new CommonDesc();

	/*
	 * public int validateActvatedCard(String instid,String padssenable,String
	 * cardno,JdbcTemplate jdbcTemplate) { int validcard = -1; StringBuilder
	 * valdcardqry = new StringBuilder(); if(padssenable.equals("Y")){
	 * 
	 * valdcardqry.append(
	 * "select count(1) from CARD_PRODUCTION where Hcard_no in (select chn from EZCARDINFO ) and Hcard_no='"
	 * +cardno+"' AND CARD_STATUS='05' AND INST_ID='"+instid+"' ");
	 * valdcardqry.append(
	 * "select count(1) from CARD_PRODUCTION where ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE Hcard_no='"
	 * +cardno+"') AND CARD_STATUS='05' AND INST_ID='"+instid+"' ");
	 * 
	 * }else { valdcardqry.append(
	 * "select count(1) from CARD_PRODUCTION where card_no in (select chn from EZCARDINFO ) and card_no='"
	 * +cardno+"' AND CARD_STATUS='05' AND INST_ID='"+instid+"' "); }
	 * enctrace("valdcardqry::"+valdcardqry.toString()); validcard =
	 * jdbcTemplate.queryForInt(valdcardqry.toString()); return validcard; }
	 * 
	 * 
	 * 
	 * public int updateCardStatusDate(String instid,String padssenable, String
	 * cardno, String dbcolumnfld, String TABLENAME, JdbcTemplate jdbctemplate)
	 * { int x = -1;String qry = ""; if(padssenable.equals("Y")){ qry ="UPDATE "
	 * + TABLENAME+" SET "+dbcolumnfld+"=SYSDATE WHERE INST_ID='"+instid +
	 * "' AND HCARD_NO='"+cardno+"'"; }else{ qry ="UPDATE "+TABLENAME+" SET "
	 * +dbcolumnfld +"=SYSDATE WHERE INST_ID='"+instid+"' AND CARD_NO='"
	 * +cardno+"'"; } enctrace("updateCardStatusDate::"+qry); x =
	 * jdbctemplate.update(qry); return x; }
	 * 
	 * public int moveCardToProcess( String instid,String order_ref_no, String
	 * padssenable,String accountno, String cardno, String newcardno,String
	 * newmcardno,String newhcardno, String caf_recstatus, String
	 * card_status,String addonname, String expperiod, String clearchn, String
	 * collectbranch, JdbcTemplate jdbctemplate ) {
	 * 
	 * StringBuilder query=new StringBuilder(); StringBuilder query1=new
	 * StringBuilder(); String expirydatecond =
	 * "add_months(sysdate,"+expperiod+")";
	 * 
	 * query.append("INSERT INTO  PERS_CARD_PROCESS ("); query.append(
	 * "INST_ID,MCARD_NO,ACCT_NO,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY, CIN, ORDER_REF_NO, CARD_STATUS, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY, "
	 * ); query.append(
	 * "GENERATED_DATE, EXPIRY_DATE, PRE_DATE,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE,"
	 * ); query.append(
	 * "APP_DATE, PIN_DATE, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT,"
	 * ); query.append(
	 * "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID,"
	 * ); query.append("BIN, AUTH_DATE, STATUS_CODE,CARD_COLLECT_BRANCH)");
	 * query.append(" SELECT INST_ID, '"+newmcardno+"','"+accountno+
	 * "', ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN, '" +order_ref_no+"', '"
	 * +card_status+
	 * "', CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY,"
	 * ); query.append("GENERATED_DATE, "+expirydatecond+
	 * ", PRE_DATE, MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, 'M', SERVICE_CODE, REG_DATE, "
	 * ); query.append(
	 * "APP_DATE, sysdate, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, '"
	 * +caf_recstatus+"', FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT, ");
	 * query. append("WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, '"
	 * +addonname. toUpperCase ()+"', '"+addonname.toUpperCase()+
	 * "', APP_NO, ORDER_FLAG, '"+newcardno +"', USED_CHN, COURIER_ID, ");
	 * query.append("BIN, AUTH_DATE, STATUS_CODE,'" +collectbranch+
	 * "' FROM CARD_PRODUCTION WHERE INST_ID='"+instid+
	 * "' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"
	 * +cardno+"')"); enctrace( "moveCardToProcess : " + query.toString() ); int
	 * x = jdbctemplate.update(query.toString());
	 * 
	 * if(padssenable.equals("Y")) { query.append(
	 * "BIN, AUTH_DATE, STATUS_CODE,'" +collectbranch+
	 * "' FROM CARD_PRODUCTION WHERE INST_ID='" +instid+"' AND HCARD_NO='"
	 * +cardno+"'"); } else { query.append("BIN, AUTH_DATE, STATUS_CODE,'"
	 * +collectbranch+"' FROM CARD_PRODUCTION WHERE INST_ID='" +instid+
	 * "' AND CARD_NO='"+cardno+"'"); } query1.append(
	 * "INSERT INTO  PERS_CARD_PROCESS_HASH ("); query1.append(
	 * "INST_ID,HCARD_NO,ACCT_NO,CIN, ORDER_REF_NO, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, "
	 * ); query1.append(
	 * "GENERATED_DATE, MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE,");
	 * query1.append("BIN)"); query1.append(" SELECT INST_ID,'"
	 * +newhcardno+"','"+ accountno+"',CIN, '"+order_ref_no +
	 * "', CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE,");
	 * query1.append (
	 * "GENERATED_DATE,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, ");
	 * query1.append("BIN FROM CARD_PRODUCTION WHERE INST_ID='"+instid+
	 * "' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"
	 * +cardno+"')"); enctrace( "moveCardToProcess : " + query1.toString() );
	 * int x1 = jdbctemplate.update(query1.toString());
	 * 
	 * return x1; }
	 * 
	 * 
	 * public int updateaddonCount( String instid,String padssenable, String
	 * cardno, JdbcTemplate jdbctemplate){ String fchupdcntqry ="";
	 * 
	 * String cntqry = "";
	 * 
	 * //if(padssenable.equals("Y")){
	 * 
	 * //cntqry =
	 * "UPDATE CARD_PRODUCTION SET ADDON_FLAG='A',ADDONCARDCOUNT= NVL(ADDONCARDCOUNT,'0')+1, ADDON_DATE=sysdate WHERE INST_ID='"
	 * +instid+"' AND HCARD_NO='"+cardno+"' "; cntqry =
	 * "UPDATE CARD_PRODUCTION SET ADDON_FLAG='A',ADDONCARDCOUNT= NVL(ADDONCARDCOUNT,'0')+1, ADDON_DATE=sysdate WHERE INST_ID='"
	 * +instid+
	 * "' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"
	 * +cardno+"') ";
	 * 
	 * }else{ cntqry =
	 * "UPDATE CARD_PRODUCTION SET ADDON_FLAG='A',ADDONCARDCOUNT= NVL(ADDONCARDCOUNT,'0')+1, ADDON_DATE=sysdate WHERE INST_ID='"
	 * +instid+"' AND CARD_NO='"+cardno+"' "; } enctrace(
	 * "ADDONCARDCOUNT UPDATE QRY __" + cntqry ); int x =
	 * commondesc.executeTransaction(cntqry, jdbctemplate); return x;
	 * 
	 * }
	 * 
	 * public List getcarddettailForAddon(String instid,String
	 * padssenable,String cardno,JdbcTemplate jdbcTemplate) { List cardlist =
	 * null; StringBuilder cardq = new StringBuilder();
	 * 
	 * cardq.append(
	 * "SELECT ORG_CHN as CARD_NO,(SELECT HCARD_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"
	 * +cardno+
	 * "' ) as HCARD_NO,MCARD_NO,ACCOUNT_NO,MOBILE,NAME,TO_CHAR(DOB,'DD-mm-YYYY')DOB,CIN,BIN, "
	 * ); cardq.append("(SELECT STATUS FROM ezcardinfo WHERE CHN='"+cardno+
	 * "')STATUSCODE , "); cardq.append(
	 * "(SELECT STATUS_DESC FROM MAINTAIN_DESC WHERE STATUS in (STATUS_CODE) and INST_ID='"
	 * +instid+"' ) STATUS_DESC,"); cardq.append(
	 * "(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE PRODUCT_CODE in (cp.PRODUCT_CODE) and INST_ID='"
	 * +instid+"') PRODUCT_DESC, "); cardq.append(
	 * "PRODUCT_CODE,ORG_CHN ORG_CARDNO from  CARD_PRODUCTION cp , EZCUSTOMERINFO ezc "
	 * );
	 * 
	 * if(padssenable.equals("Y")){ cardq.append(
	 * "WHERE cp.inst_id=ezc.INSTID and cp.CIN = ezc.CUSTID AND HCARD_NO='"
	 * +cardno+"' "); }else{ cardq.append(
	 * "WHERE cp.inst_id=ezc.INSTID and cp.CIN = ezc.CUSTID AND CARD_NO='"
	 * +cardno+"' "); }
	 * 
	 * cardq.append(
	 * "WHERE cp.inst_id=ezc.INSTID and cp.CIN = ezc.CUSTID AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH  WHERE HCARD_NO='"
	 * +cardno+"') "); enctrace("cardq-----:"+cardq.toString());
	 * 
	 * cardlist = jdbcTemplate.queryForList(cardq.toString()); return cardlist;
	 * }
	 */

	public int validateActvatedCard(String instid, String padssenable, String cardno, JdbcTemplate jdbcTemplate) {
		int validcard = -1;
		StringBuilder valdcardqry = new StringBuilder();
		/* if(padssenable.equals("Y")){ */

		/*
		 * valdcardqry.append(
		 * "select count(1) from CARD_PRODUCTION where Hcard_no in (select chn from EZCARDINFO ) and Hcard_no='"
		 * +cardno+"' AND CARD_STATUS='05' AND INST_ID='"+instid+"' ");
		 */

		// /by gowtham-300819
		
		 valdcardqry.append(
		 "select count(1) from CARD_PRODUCTION where ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE Hcard_no='"
		 +cardno+"') AND CARD_STATUS='05' AND CARD_FLAG='P'  AND INST_ID='"+instid+"' AND STATUS_CODE IN(SELECT STATUS FROM EZCARDINFO WHERE STATUS='50' AND CHN='"+cardno+"' )");
		 
		//valdcardqry.append(
				//"select count(1) from CARD_PRODUCTION where ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE Hcard_no=?) AND CARD_STATUS=? AND INST_ID=? AND CARD_FLAG=? ");
		/*
		 * }else { valdcardqry.append(
		 * "select count(1) from CARD_PRODUCTION where card_no in (select chn from EZCARDINFO ) and card_no='"
		 * +cardno+"' AND CARD_STATUS='05' AND INST_ID='"+instid+"' "); }
		 */
		enctrace("valdcardqry::" + valdcardqry.toString());

		 validcard = jdbcTemplate.queryForInt(valdcardqry.toString()); 
	//	validcard = jdbcTemplate.queryForInt(valdcardqry.toString(), new Object[] { cardno, "05", instid,"P" });

		return validcard;

	}

	public int updateCardStatusDate(String instid, String padssenable, String cardno, String dbcolumnfld,
			String TABLENAME, JdbcTemplate jdbctemplate) {
		int x = -1;
		String qry = "";
		if (padssenable.equals("Y")) {
			qry = "UPDATE " + TABLENAME + " SET " + dbcolumnfld + "=SYSDATE WHERE INST_ID='" + instid
					+ "' AND HCARD_NO='" + cardno + "'";
		} else {
			qry = "UPDATE " + TABLENAME + " SET " + dbcolumnfld + "=SYSDATE WHERE INST_ID='" + instid
					+ "' AND CARD_NO='" + cardno + "'";
		}
		enctrace("updateCardStatusDate::" + qry);
		x = jdbctemplate.update(qry);
		return x;
	}

	public int moveCardToProcess(String instid, String order_ref_no, String padssenable, String accountno,
			String cardno, String newcardno, String newmcardno, String newhcardno, String caf_recstatus,
			String card_status, String addonname, String expperiod, String clearchn, String collectbranch,
			String subProductId,String refrenceId, String productcode, JdbcTemplate jdbctemplate) {

		StringBuilder query = new StringBuilder();
		StringBuilder query1 = new StringBuilder();
		String expirydatecond = "add_months(sysdate," + expperiod + ")";

		query.append("INSERT INTO  PERS_CARD_PROCESS (");
		query.append(
				"INST_ID,MCARD_NO,ACCT_NO,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY, CIN, ORDER_REF_NO, CARD_STATUS, CARD_TYPE_ID, SUB_PROD_ID,BULK_REG_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY, ");
		query.append(
				"GENERATED_DATE, EXPIRY_DATE, PRE_DATE,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE,");
		query.append(
				"APP_DATE, PIN_DATE, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT,");
		query.append(
				"WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID,");
		query.append("BIN, AUTH_DATE, CARD_FLAG,STATUS_CODE,CARD_COLLECT_BRANCH,MOBILENO)");
		query.append(" SELECT INST_ID, '" + newmcardno + "','" + accountno
				+ "', ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN, '" + order_ref_no + "', '" + card_status
				+ "', CARD_TYPE_ID, '" + subProductId + "','"+refrenceId+"','" + productcode +
				"' , BRANCH_CODE, PC_FLAG, CARD_CCY,");
		query.append("GENERATED_DATE, " + expirydatecond
				+ ", PRE_DATE, MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, 'M', SERVICE_CODE, REG_DATE, ");
		query.append("APP_DATE, sysdate, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, '" + caf_recstatus
				+ "', FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT, ");
		query.append("WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, '" + addonname.toUpperCase() + "', '"
				+ addonname.toUpperCase() + "', APP_NO, ORDER_FLAG, '" + newcardno + "', USED_CHN, COURIER_ID, ");
		query.append("BIN, AUTH_DATE, 'C',STATUS_CODE,'" + collectbranch + "',MOBILENO FROM CARD_PRODUCTION WHERE INST_ID='" + instid
				+ "' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='" + cardno
				+ "')");
		enctrace("moveCardToProcess:::    " + query.toString());
		int x = jdbctemplate.update(query.toString());

		/*
		 * if(padssenable.equals("Y")) { query.append(
		 * "BIN, AUTH_DATE, STATUS_CODE,'" +collectbranch+
		 * "' FROM CARD_PRODUCTION WHERE INST_ID='" +instid+"' AND HCARD_NO='"
		 * +cardno+"'"); } else { query.append("BIN, AUTH_DATE, STATUS_CODE,'"
		 * +collectbranch+"' FROM CARD_PRODUCTION WHERE INST_ID='" +instid+
		 * "' AND CARD_NO='"+cardno+"'"); }
		 */
		query1.append("INSERT INTO  PERS_CARD_PROCESS_HASH (");
		query1.append(
				"INST_ID,HCARD_NO,ACCT_NO,CIN, ORDER_REF_NO, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, ");
		query1.append("GENERATED_DATE, MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE,");
		query1.append("BIN)");
		query1.append(" SELECT INST_ID,'" + newhcardno + "','" + accountno + "',CIN, '" + order_ref_no
				+ "', CARD_TYPE_ID, '" + subProductId + "', PRODUCT_CODE, BRANCH_CODE,");
		query1.append("GENERATED_DATE,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, ");
		query1.append("BIN FROM CARD_PRODUCTION WHERE INST_ID='" + instid
				+ "' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='" + cardno
				+ "')");
		enctrace("moveCardToProcess : " + query1.toString());
		int x1 = jdbctemplate.update(query1.toString());

		return x1;
	}

	public int updateaddonCount(String instid, String padssenable, String cardno, JdbcTemplate jdbctemplate) {
		String fchupdcntqry = "";

		String cntqry = "";

		// if(padssenable.equals("Y")){

		// cntqry =
		// "UPDATE CARD_PRODUCTION SET ADDON_FLAG='A',ADDONCARDCOUNT=
		// NVL(ADDONCARDCOUNT,'0')+1, ADDON_DATE=sysdate WHERE
		// INST_ID='"+instid+"' AND HCARD_NO='"+cardno+"' ";

		/*
		 * cntqry =
		 * "UPDATE CARD_PRODUCTION SET ADDON_FLAG='A',ADDONCARDCOUNT= NVL(ADDONCARDCOUNT,'0')+1, ADDON_DATE=sysdate WHERE INST_ID='"
		 * +instid+
		 * "' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"
		 * +cardno+"') ";
		 */

		// by gowtham-300819
		cntqry = "UPDATE CARD_PRODUCTION SET ADDON_FLAG='A',ADDONCARDCOUNT= NVL(ADDONCARDCOUNT,'0')+1, ADDON_DATE=sysdate WHERE INST_ID=? AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO=? ) ";
		/*
		 * }else{ cntqry =
		 * "UPDATE CARD_PRODUCTION SET ADDON_FLAG='A',ADDONCARDCOUNT= NVL(ADDONCARDCOUNT,'0')+1, ADDON_DATE=sysdate WHERE INST_ID='"
		 * +instid+"' AND CARD_NO='"+cardno+"' "; }
		 */
		enctrace("ADDONCARDCOUNT UPDATE QRY __" + cntqry);
		/* int x = commondesc.executeTransaction(cntqry, jdbctemplate); */

		int x = jdbctemplate.update(cntqry, new Object[] { instid, cardno });
		return x;

	}

	public List getcarddettailForAddon(String instid, String padssenable, String cardno, JdbcTemplate jdbcTemplate) {
		List cardlist = null;
		StringBuilder cardq = new StringBuilder();

		/*
		 * cardq.append(
		 * "SELECT ORG_CHN as CARD_NO,(SELECT HCARD_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"
		 * +cardno+
		 * "' ) as HCARD_NO,MCARD_NO,ACCOUNT_NO,MOBILE,NAME,TO_CHAR(DOB,'DD-mm-YYYY')DOB,CIN,BIN, "
		 * ); cardq.append("(SELECT STATUS FROM ezcardinfo WHERE CHN='"+cardno+
		 * "')STATUSCODE , "); cardq.append(
		 * "(SELECT STATUS_DESC FROM MAINTAIN_DESC WHERE STATUS in (STATUS_CODE) and INST_ID='"
		 * +instid+"' ) STATUS_DESC,"); cardq.append(
		 * "(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE PRODUCT_CODE in (cp.PRODUCT_CODE) and INST_ID='"
		 * +instid+"') PRODUCT_DESC, "); cardq.append(
		 * " (SELECT PRODUCT_CODE,SUB_PROD_ID,SUB_PROD_NAME FROM INSTPROD_DETAILS  WHERE AUTH_STATUS='1' AND INST_ID='"
		 * +instid+"') SUB_PROD,  "); cardq.append(
		 * "PRODUCT_CODE,ORG_CHN ORG_CARDNO from  CARD_PRODUCTION cp , EZCUSTOMERINFO ezc "
		 * ); // if(padssenable.equals("Y")){ // cardq.append(
		 * "WHERE cp.inst_id=ezc.INSTID and cp.CIN = ezc.CUSTID AND HCARD_NO='"
		 * +cardno+"' "); // }else{ // cardq.append(
		 * "WHERE cp.inst_id=ezc.INSTID and cp.CIN = ezc.CUSTID AND CARD_NO='"
		 * +cardno+"' "); // } cardq.append(
		 * "WHERE cp.inst_id=ezc.INSTID and cp.CIN = ezc.CUSTID AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH  WHERE HCARD_NO='"
		 * +cardno+"') "); enctrace("cardq-----:"+cardq.toString()); cardlist
		 * =jdbcTemplate.queryForList(cardq.toString());
		 */

		// /by gowtham-300819

		cardq.append(
				"SELECT ORG_CHN as CARD_NO,(SELECT HCARD_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO=? ) as HCARD_NO,MCARD_NO,ACCOUNT_NO,MOBILE,NAME,TO_CHAR(DOB,'DD-mm-YYYY')DOB,CIN,BIN, ");
		cardq.append("(SELECT STATUS FROM ezcardinfo WHERE CHN=?)STATUSCODE , ");
		cardq.append(
				"(SELECT STATUS_DESC FROM MAINTAIN_DESC WHERE STATUS in (STATUS_CODE) and INST_ID=? ) STATUS_DESC,");
		cardq.append(
				"(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE PRODUCT_CODE in (cp.PRODUCT_CODE) and INST_ID=?) PRODUCT_DESC, ");
		cardq.append("PRODUCT_CODE,ORG_CHN ORG_CARDNO from  CARD_PRODUCTION cp , EZCUSTOMERINFO ezc ");
		cardq.append(
				"WHERE cp.inst_id=ezc.INSTID and cp.CIN = ezc.CUSTID AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH  WHERE HCARD_NO=? ) ");
		enctrace("cardq-----:" + cardq.toString());
		cardlist = jdbcTemplate.queryForList(cardq.toString(), new Object[] { cardno, cardno, instid, instid, cardno });

		return cardlist;
	}

	public List getSubProduct(String instid, JdbcTemplate jdbcTemplate) {
		List subProdList = null;

		String subProdQry = "SELECT PRODUCT_CODE,SUB_PROD_ID,SUB_PRODUCT_NAME "
				+ "  FROM INSTPROD_DETAILS WHERE AUTH_STATUS='1' AND INST_ID='" + instid + "' ";

		enctrace("cardq-----:" + subProdQry.toString());
		subProdList = jdbcTemplate.queryForList(subProdQry.toString());

		return subProdList;
	}

	public String getProductCode(String instid, String subproductCode, JdbcTemplate jdbcTemplate) {
		String productCode = null;

		String subProdQry = "SELECT PRODUCT_CODE  FROM INSTPROD_DETAILS WHERE AUTH_STATUS='1' AND INST_ID='" + instid
				+ "'" + " AND SUB_PROD_ID='" + subproductCode + "' ";

		enctrace("cardq-----:" + subProdQry);
		productCode = (String) jdbcTemplate.queryForObject(subProdQry, String.class);

		return productCode;
	}

	/**
	 * This method is used to get the AccountNumber
	 * @param instid
	 * @param encCardno
	 * @param jdbcTemplate
	 * @return
	 */
	public String getAccountNo(String instid, String encCardno, JdbcTemplate jdbcTemplate) {
		String accountNo = null;

		String accountQry = "SELECT ACCOUNT_NO  FROM  CARD_PRODUCTION  WHERE ORG_CHN='" + encCardno + "'";
		enctrace("cardq-----:" + accountQry);
		accountNo = (String) jdbcTemplate.queryForObject(accountQry, String.class);

		return accountNo;
	}
	
	
	/**
	 * This method is used to get the Card Details from Account number
	 * 
	 * @param instid
	 * @param accountNo
	 * @param jdbcTemplate
	 * @return
	 */
	public List  getCardDeatils(String instid,String accountNo,JdbcTemplate jdbcTemplate) {
		
		List cardLst=null;
		
		String cardDetailsQry="SELECT MCARD_NO,ACCOUNT_NO,CIN,EMB_NAME AS NAME ,MOBILENO AS MOBILE ,BIN,  DECODE(CARD_STATUS,'05','ACTIVE')STATUS_DESC,ORG_CHN , BULK_REG_ID,SUB_PROD_ID,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE "
				+ "PRODUCT_CODE in (cp.PRODUCT_CODE) and INST_ID='"+instid+"') PRODUCT_DESC,DECODE(CARD_FLAG,'P','PARENT CARD','C','CHILD CARD')FLAG"
						+ " FROM CARD_PRODUCTION cp WHERE ACCOUNT_NO='"+accountNo+"' ";
		enctrace("cardDetailsQry-----:" + cardDetailsQry);
		cardLst=	jdbcTemplate.queryForList(cardDetailsQry);
		
		return cardLst;
	}

	public int validateCardnumber(String instid,String encCardno,JdbcTemplate jdbcTemplate) {
		int cardno = 0;

		String checkCount = "SELECT count(*)  FROM  CARD_PRODUCTION  WHERE ORG_CHN='" + encCardno + "'";
		enctrace("cardq-----:" + checkCount);
		cardno = jdbcTemplate.queryForInt(checkCount);

		return cardno;
	}
		
		
}
