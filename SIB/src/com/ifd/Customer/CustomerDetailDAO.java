package com.ifd.Customer;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;

public class CustomerDetailDAO extends BaseAction {

	// commented by senthil

	/*
	 * public String checkValid(String cardno, JdbcTemplate jdbctemplate) {
	 * String valid = "", validqry = ""; try { validqry =
	 * "SELECT COUNT(1) FROM PERS_CARD_PROCESS WHERE CARD_NO='"+cardno+
	 * "' AND CARD_STATUS IN ('01')"; enctrace("validqry customer name edit-->"
	 * +validqry); valid = (String) jdbctemplate.queryForObject(validqry,
	 * String.class); } catch (Exception e) { e.printStackTrace(); } return
	 * valid; }
	 * 
	 * public List<Map<String, Object>> getEmbossingName(String cardno, String
	 * type, JdbcTemplate jdbctemplate) { String embnameqry = "", cond = "";
	 * List custdetails = null; try { if("CARD".equals(type)){ cond =
	 * "CARD_NO='"+cardno+"'"; }else{ cond = "CIN='"+cardno+"'"; } embnameqry =
	 * "SELECT EMB_NAME,MOBILENO FROM PERS_CARD_PROCESS WHERE "+cond+
	 * " AND ROWNUM=1"; enctrace("embnameqry customer name edit-->"+embnameqry);
	 * custdetails = jdbctemplate.queryForList(embnameqry); } catch (Exception
	 * e) { e.printStackTrace(); } return custdetails; }
	 * 
	 * public String checkValidWithCustid(CustomerDetailBean bean,JdbcTemplate
	 * jdbctemplate) { String validcustid = "", validcustidqry = ""; try {
	 * validcustidqry = "SELECT COUNT(1) FROM PERS_CARD_PROCESS WHERE CIN='"
	 * +bean.getCardno()+"' AND CARD_STATUS IN ('01')"; enctrace(
	 * "validcustidqry customer name edit-->"+validcustidqry); validcustid =
	 * (String) jdbctemplate.queryForObject(validcustidqry, String.class);
	 * enctrace("validcustid in process-->"+validcustid); } catch (Exception e)
	 * { e.printStackTrace(); } return validcustid; }
	 * 
	 * public String checkValidinOrder(CustomerDetailBean bean, JdbcTemplate
	 * jdbctemplate) { String validcustid = "", validcustidqry = "";
	 * validcustidqry = "SELECT COUNT(1) FROM PERS_CARD_ORDER WHERE CIN='"
	 * +bean.getCardno()+"' AND ORDER_STATUS IN ('01')"; enctrace(
	 * "validcustidqry in order table customer name edit -->"+validcustidqry);
	 * validcustid = (String) jdbctemplate.queryForObject(validcustidqry,
	 * String.class); enctrace("validcustid in order-->"+validcustid); return
	 * validcustid; }
	 * 
	 * public String getEmbossingNameInOrder(String custid, JdbcTemplate
	 * jdbctemplate) { String embname = "", embnameqry = ""; try { embnameqry =
	 * "SELECT EMBOSSING_NAME FROM PERS_CARD_ORDER WHERE CIN='"+custid+
	 * "' AND ROWNUM=1"; enctrace(
	 * "embnameqry in order tab customer name edit-->"+embnameqry); embname =
	 * (String) jdbctemplate.queryForObject(embnameqry, String.class); } catch
	 * (Exception e) { e.printStackTrace(); } return embname; }
	 * 
	 * public int updateEmbName(CustomerDetailBean bean, String cardno,
	 * JdbcTemplate jdbctemplate) { String embnameupdateqry = "",cond = "",
	 * validcustidqry= "" , validcustid= ""; int update=0;
	 * if(bean.getCardno().length() > 6){ cond = "CARD_NO = '"+cardno+"'";
	 * }else{ cond = "CIN = '"+bean.getCardno()+"'"; } try{
	 * 
	 * validcustidqry = "SELECT COUNT(1) FROM PERS_CARD_PROCESS WHERE "+cond+
	 * " AND CARD_STATUS IN ('01')"; enctrace(
	 * "validcustidqry customer name edit-->"+validcustidqry); validcustid =
	 * (String) jdbctemplate.queryForObject(validcustidqry, String.class);
	 * if("0".equals(validcustid)){ embnameupdateqry =
	 * " UPDATE PERS_CARD_ORDER SET EMBOSSING_NAME='"
	 * +bean.getEmbossingname().trim()+"',ENCODE_DATA='"+bean.getEmbossingname()
	 * .trim()+"' WHERE CIN='"+bean.getCardno()+"'"; }else{ embnameupdateqry =
	 * "UPDATE PERS_CARD_PROCESS SET EMB_NAME='"
	 * +bean.getEmbossingname().trim()+"',ENC_NAME='"+bean.getEmbossingname().
	 * trim()+"',MOBILENO='"+bean.getMobileno().trim()+"' WHERE "+cond+""; }
	 * enctrace("embnameupdateqry-->"+embnameupdateqry); update =
	 * jdbctemplate.update(embnameupdateqry); }catch (Exception e) {
	 * e.printStackTrace(); } return update; }
	 * 
	 * public int updateMobileNo(CustomerDetailBean bean, String cardno,
	 * JdbcTemplate jdbctemplate) { int update=0; String cond =
	 * "",mobileupdateqry = "",validcustidqry = "",validcustid = "";
	 * if(bean.getCardno().length() > 6){ cond = "CARD_NO = '"+cardno+"'";
	 * }else{ cond = "CIN = '"+bean.getCardno()+"'"; } try{ validcustidqry =
	 * "SELECT COUNT(1) FROM PERS_CARD_PROCESS WHERE "+cond+
	 * " AND CARD_STATUS IN ('01')"; enctrace(
	 * "validcustidqry customer name edit-->"+validcustidqry); validcustid =
	 * (String) jdbctemplate.queryForObject(validcustidqry, String.class);
	 * if("0".equals(validcustid)){ mobileupdateqry =
	 * "UPDATE CUSTOMERINFO SET MOBILE='"+bean.getMobileno()+
	 * "' WHERE CIN=(SELECT CIN FROM PERS_CARD_ORDER WHERE "+cond+
	 * " AND ROWNUM=1)"; }else{ mobileupdateqry =
	 * "UPDATE CUSTOMERINFO SET MOBILE='"+bean.getMobileno()+
	 * "' WHERE CIN=(SELECT CIN FROM PERS_CARD_PROCESS WHERE "+cond+
	 * " AND ROWNUM=1)"; } enctrace("mobileupdateqry-->"+mobileupdateqry);
	 * update = jdbctemplate.update(mobileupdateqry); }catch (Exception e) {
	 * e.printStackTrace(); } return update; }
	 */

	// ended by senthil

	public String checkValid1(String cardno, JdbcTemplate jdbctemplate) {
		String valid = "", validqry = "";
		try {
			validqry = "SELECT COUNT(1) FROM EZCARDINFO WHERE CHN='" + cardno + "' AND STATUS IN ('16','50')";
			enctrace("validqry customer name edit-->" + validqry);
			valid = (String) jdbctemplate.queryForObject(validqry, String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valid;
	}

	public String checkValid(String cardno, String type, JdbcTemplate jdbctemplate) {
		String valid = "", validqry = "", condi = "";
		if (type.equalsIgnoreCase("CARD")) {
			condi = "HCARD_NO='" + cardno.trim() + "'";

		} else if (type.equalsIgnoreCase("PHONE")) {
			condi = "MOBILENO='" + cardno.trim() + "'";
		} else if (type.equalsIgnoreCase("order")) {
			condi = "ORDER_REF_NO='" + cardno.trim() + "'";
		} else if (type.equalsIgnoreCase("CIN")) {
			condi = "CIN='" + cardno.trim() + "'";
		}
		try {
			validqry = "SELECT COUNT(1) FROM CARD_PRODUCTION WHERE " + condi + " AND STATUS_CODE NOT IN ('62')";
			enctrace("validqry customer name edit-->sardar" + validqry);
			valid = (String) jdbctemplate.queryForObject(validqry, String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valid;
	}

	public String checkValidbranchcard(String cardno, String type, String cardcollectbranch,
			JdbcTemplate jdbctemplate) {
		String valid = "", validqry = "", condi = "";

		if (type.equalsIgnoreCase("CARD")) {
			condi = "ORG_CHN='" + cardno.trim() + "'";

		} else if (type.equalsIgnoreCase("PHONE")) {
			condi = "MOBILENO='" + cardno.trim() + "'";
		} else if (type.equalsIgnoreCase("order")) {
			condi = "ORDER_REF_NO='" + cardno.trim() + "'";
		} else if (type.equalsIgnoreCase("CIN")) {
			condi = "CIN='" + cardno.trim() + "'";
		}
		try {
			validqry = "SELECT COUNT(1) FROM CARD_PRODUCTION WHERE " + condi + " AND trim(CARD_COLLECT_BRANCH)='"
					+ cardcollectbranch + "'";
			enctrace("validqry crduserbranch-->" + validqry);
			valid = (String) jdbctemplate.queryForObject(validqry, String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valid;
	}

	public String checkorderrefno1(String orderrefno, JdbcTemplate jdbctemplate) {
		String validphoneno = "", validmobnoqry = "";
		try {
			validmobnoqry = "SELECT COUNT(1) FROM CARD_PRODUCTION WHERE ORDER_REF_NO='" + orderrefno
					+ "' AND CARD_STATUS IN ('09','05')";
			enctrace("validcustidqry customer name edit-->" + validmobnoqry);
			validphoneno = (String) jdbctemplate.queryForObject(validmobnoqry, String.class);
			enctrace("validphoneno in process-->" + validphoneno);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return validphoneno;
	}

	public int checkcardinprocess(String type, String cardno, String cardcollectbranch, JdbcTemplate jdbctemplate) {
		int valid = 0;
		String validqry = "", condi = "";
		trace("customer editing with " + type);
		if (type.equalsIgnoreCase("CARD")) {
			condi = "ORG_CHN='" + cardno.trim() + "'";
		} else if (type.equalsIgnoreCase("PH")) {
			condi = "MOBILENO='" + cardno.trim() + "'";
		} else if (type.equalsIgnoreCase("OREF")) {
			condi = "ORDER_REF_NO='" + cardno.trim() + "'";
		} else {
			condi = "CIN='" + cardno.trim() + "'";
		}
		try {
			validqry = "SELECT COUNT(1) FROM PERS_CARD_PROCESS WHERE " + condi + " AND CARD_STATUS='15'";

			enctrace("validqry checkcardinprocess-->" + validqry);
			valid = jdbctemplate.queryForInt(validqry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valid;
	}

	public int checkcardproc(String cardno, String type, JdbcTemplate jdbctemplate) {
		int valid = -1;
		String condi = "";
		if (type.equalsIgnoreCase("CARD")) {
			condi = "ORG_CHN='" + cardno + "'";
		}
		if (type.equalsIgnoreCase("ORDER")) {
			condi = "ORDER_REF_NO='" + cardno + "'";
		}
		if (type.equalsIgnoreCase("PHONE")) {
			condi = "MOBILENO='" + cardno + "'";
		}
		if (type.equalsIgnoreCase("CIN")) {
			condi = "CIN='" + cardno + "'";
		}

		try {
			String validqry = "SELECT COUNT(1) FROM PERS_CARD_PROCESS WHERE " + condi + "";
			enctrace("validqry customer name edit-->" + validqry);
			valid = jdbctemplate.queryForInt(validqry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return valid;
	}

	public String getbrachuser(String instid, String cardno, String type, JdbcTemplate jdbctemplate) {
		String BRANCH_CODE = null, cond = "";
		if ("CARD".equalsIgnoreCase(type)) {
			cond = "ORG_CHN='" + cardno + "'";

		} else if ("PHONE".equalsIgnoreCase(type)) {
			cond = "MOBILENO='" + cardno + "'";

		} else if ("ORDER".equalsIgnoreCase(type)) {
			cond = "ORDER_REF_NO='" + cardno + "'";

		} else {
			cond = "CIN='" + cardno + "'";

		}
		try {
			String BRANCHCODE = "SELECT trim(CARD_COLLECT_BRANCH) FROM PERS_CARD_PROCESS WHERE " + cond
					+ " AND  INST_ID='" + instid + "'";
			enctrace("editcard :" + BRANCHCODE);
			BRANCH_CODE = (String) jdbctemplate.queryForObject(BRANCHCODE, String.class);
		} catch (EmptyResultDataAccessException e) {
		}
		return BRANCH_CODE;

	}

	public List<Map<String, Object>> getEmbossingName(String cardno, String type, JdbcTemplate jdbctemplate) {
		String embnameqry = "", cond = "";
		List custdetails = null;
		try {
			if ("CARD".equalsIgnoreCase(type)) {
				cond = "ORG_CHN='" + cardno + "'";

			} else if ("phoneno".equalsIgnoreCase(type)) {
				cond = "MOBILENO='" + cardno + "'";

			} else if ("orderNO".equalsIgnoreCase(type)) {
				cond = "ORDER_REF_NO='" + cardno + "'";

			} else {
				cond = "CIN='" + cardno + "'";

			}

			embnameqry = "SELECT ORG_CHN,EMB_NAME,MOBILENO,CIN FROM CARD_PRODUCTION WHERE " + cond + " AND ROWNUM=1";

			// embnameqry = "SELECT
			// A.EMB_NAME,A.MOBILENO,B.FNAME,B.MNAME,B.LNAME,TO_CHAR(B.DOB,'DD/MM/YYYY')
			// AS
			// DOB,B.MARITAL_STATUS,B.NATIONALITY,B.DOCUMENT_PROVIDED,B.DOCUMENT_NUMBER,B.SPOUCE_NAME,B.MOTHER_NAME,B.FATHER_NAME,B.E_MAIL,B.P_PO_BOX,B.P_HOUSE_NO,B.P_STREET_NAME,B.P_WARD_NAME,B.P_CITY,B.P_DISTRICT,B.P_PHONE1,B.P_PHONE2
			// FROM CARD_PRODUCTION A,CUSTOMERINFO B WHERE "+cond+" AND
			// "+cond2+"";
			enctrace("embnameqry customer name edit-->" + embnameqry);
			custdetails = jdbctemplate.queryForList(embnameqry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return custdetails;
	}

	public String checkValidWithCustid(String custid, JdbcTemplate jdbctemplate) {
		String validcustid = "", validcustidqry = "";
		try {
			validcustidqry = "SELECT COUNT(1) FROM EZCARDINFO WHERE CUSTID='" + custid + "' AND STATUS IN ('16','50')";
			enctrace("validcustidqry customer name edit-->" + validcustidqry);
			validcustid = (String) jdbctemplate.queryForObject(validcustidqry, String.class);
			enctrace("validcustid in process-->" + validcustid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return validcustid;
	}
	
	public String getValidHcardNoWithCustid(String custid, JdbcTemplate jdbctemplate) {
		String validcustid = "", validcustidqry = "";
		try {
			validcustidqry = "SELECT CHN FROM EZCARDINFO WHERE CUSTID='" + custid + "' AND STATUS IN ('16','50')";
			enctrace("validcustidqry customer name edit-->" + validcustidqry);
			validcustid = (String) jdbctemplate.queryForObject(validcustidqry, String.class);
			enctrace("validcustid in process-->" + validcustid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return validcustid;
	}

	public String checkphoneno(String phoneno, JdbcTemplate jdbctemplate) {
		String validphoneno = "", validmobnoqry = "";
		try {
			validmobnoqry = "SELECT COUNT(1) FROM CARD_PRODUCTION WHERE MOBILENO='" + phoneno
					+ "' AND CARD_STATUS IN ('09','05')";
			enctrace("validcustidqry customer name edit-->" + validmobnoqry);
			validphoneno = (String) jdbctemplate.queryForObject(validmobnoqry, String.class);
			enctrace("validphoneno in process-->" + validphoneno);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return validphoneno;
	}

	public String checkorderrefno(String orderrefno, JdbcTemplate jdbctemplate) {
		String validphoneno = "", validmobnoqry = "";
		try {
			validmobnoqry = "SELECT COUNT(1) FROM CARD_PRODUCTION WHERE ORDER_REF_NO='" + orderrefno
					+ "' AND CARD_STATUS IN ('09','05')";
			enctrace("validcustidqry customer name edit-->" + validmobnoqry);
			validphoneno = (String) jdbctemplate.queryForObject(validmobnoqry, String.class);
			enctrace("validphoneno in process-->" + validphoneno);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return validphoneno;
	}

	/*
	 * public String checkValidinOrder(CustomerDetailBean bean, JdbcTemplate
	 * jdbctemplate) { String validcustid = "", validcustidqry = "";
	 * validcustidqry = "SELECT COUNT(1) FROM PERS_CARD_ORDER WHERE CIN='"
	 * +bean.getCardno()+"' AND ORDER_STATUS IN ('01')"; enctrace(
	 * "validcustidqry in order table customer name edit -->"+validcustidqry);
	 * validcustid = (String) jdbctemplate.queryForObject(validcustidqry,
	 * String.class); enctrace("validcustid in order-->"+validcustid); return
	 * validcustid; }
	 * 
	 * public String getEmbossingNameInOrder(String custid, JdbcTemplate
	 * jdbctemplate) { String embname = "", embnameqry = ""; try { embnameqry =
	 * "SELECT EMBOSSING_NAME FROM PERS_CARD_ORDER WHERE CIN='"+custid+
	 * "' AND ROWNUM=1"; enctrace(
	 * "embnameqry in order tab customer name edit-->"+embnameqry); embname =
	 * (String) jdbctemplate.queryForObject(embnameqry, String.class); } catch
	 * (Exception e) { e.printStackTrace(); } return embname; }
	 */

	/*
	 * public int updateEmbNameinproc(CustomerDetailBean bean, String
	 * cardno,String userid, JdbcTemplate jdbctemplate) {
	 * 
	 * String embnameupdateqry = "",embnameswitch="",cond = "", validcustidqry=
	 * "" , validcustid= ""; String embnameupdateqry1 = "",embnameswitch1="";
	 * int update=0; if(bean.getCardno().length() > 6){ cond = "CARD_NO = '"
	 * +cardno+"' AND ROWNUM=1"; }else{ cond = "CIN = '"+bean.getCustid()+
	 * "' AND ROWNUM=1"; } try{
	 * 
	 * validcustidqry = "SELECT COUNT(1) FROM CARD_PRODUCTION WHERE CARD_NO = '"
	 * +cardno+"' AND CIN = '"+bean.getCustid()+
	 * "' AND CARD_STATUS IN ('09','05') "; enctrace(
	 * "validcustidqry customer name edit-->"+validcustidqry); validcustid =
	 * (String) jdbctemplate.queryForObject(validcustidqry, String.class);
	 * 
	 * //embnameupdateqry =
	 * "UPDATE CARD_PRODUCTION SET CARD_STATUS='05',STATUS_CODE='97', EMB_NAME='"
	 * +bean.getEmbossingname().trim()+"',ENC_NAME='"+bean.getEmbossingname().
	 * trim()+"',MOBILENO='"+bean.getMobileno().trim()+"' WHERE "+cond+"";
	 * 
	 * 
	 * if("0".equals(validcustid)){ //embnameupdateqry =
	 * " UPDATE PERS_CARD_ORDER SET EMBOSSING_NAME='"
	 * +bean.getEmbossingname().trim()+"',ENCODE_DATA='"+bean.getEmbossingname()
	 * .trim()+"' WHERE CIN='"+bean.getCardno()+"'"; embnameupdateqry =
	 * "UPDATE CARD_PRODUCTION SET EMB_NAME='"
	 * +bean.getEmbossingname().trim()+"',ENC_NAME='"+bean.getEmbossingname().
	 * trim()+"',MOBILENO='"+bean.getMobileno().trim()+"' WHERE "+cond+"";
	 * }else{ embnameupdateqry = "UPDATE PERS_CARD_PROCESS SET EMB_NAME='"
	 * +bean.getEmbossingname().trim()+"',ENC_NAME='"+bean.getEmbossingname().
	 * trim()+"',MOBILENO='"+bean.getMobileno().trim()+"' WHERE "+cond+""; }
	 * 
	 * embnameupdateqry =
	 * "INSERT INTO PERS_CARD_PROCESS (INST_ID,ORDER_REF_NO,BIN,CARD_NO,HCARD_NO, MCARD_NO,ACCT_NO ,ACCTTYPE_ID, ACCTSUB_TYPE_ID,"
	 * +
	 * "ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,status_code,"+
	 * "CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,"+
	 * "SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,"+
	 * "PUR_CNT,ECOM_AMT,ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,PIN_OFFSET,AUTO_ACCT_FLAG,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO,"+
	 * "MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,CARD_COLLECT_BRANCH)"+
	 * " SELECT "+
	 * "INST_ID,ORDER_REF_NO,BIN,CARD_NO,HCARD_NO, MCARD_NO,ACCOUNT_NO,"+
	 * "ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,"
	 * +
	 * "BRANCH_CODE,'15',CAF_REC_STATUS,STATUS_CODE,CARD_CCY,PC_FLAG,ORDER_FLAG,"+
	 * "ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,"+
	 * "SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,"+
	 * "ECOM_CNT,'"+bean.getEmbossingname()+"','"+bean.getEmbossingname()+
	 * "',COURIER_ID,AUTH_DATE,PIN_OFFSET,"+
	 * "AUTO_ACCT_FLAG,CVV1,CVV2,ICVV,'"+bean.getMobileno()+"',CARD_REF_NO,'"+
	 * userid+
	 * "',SYSDATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,CARD_COLLECT_BRANCH FROM CARD_PRODUCTION"
	 * + " WHERE CARD_NO = '"+cardno+"' AND CIN = '"+bean.getCustid()+
	 * "'AND ROWNUM=1 ";
	 * 
	 * enctrace("embnameupdateqry-->"+embnameupdateqry); update =
	 * jdbctemplate.update(embnameupdateqry);
	 * 
	 * 
	 * }catch (Exception e) { e.printStackTrace(); } return update; }
	 */

	public int updateEmbNameinproc(CustomerDetailBean bean, String cardno, String userid,String hcardno, JdbcTemplate jdbctemplate) {

		String embnameupdateqry = "", embnameswitch = "", embnameupdateqry_Hash = "", cond = "", validcustidqry = "",
				validcustid = "";
		String embnameupdateqry1 = "", embnameswitch1 = "";
		int update = 0;
		int update1=0;
		
		int res=0;
		/*
		 * if(bean.getCardno().length() > 6){ cond = "CARD_NO = '"+cardno+
		 * "' AND ROWNUM=1"; }else{ cond = "CIN = '"+bean.getCustid()+
		 * "' AND ROWNUM=1"; }
		 */
		try {

			validcustidqry = "SELECT COUNT(1) FROM CARD_PRODUCTION WHERE ORG_CHN = '" + cardno + "' AND CIN = '"
					+ bean.getCustid() + "' AND CARD_STATUS IN ('09','05') ";
			enctrace("validcustidqry customer name edit-->" + validcustidqry);
			validcustid = (String) jdbctemplate.queryForObject(validcustidqry, String.class);

			// embnameupdateqry = "UPDATE CARD_PRODUCTION SET
			// CARD_STATUS='05',STATUS_CODE='97',
			// EMB_NAME='"+bean.getEmbossingname().trim()+"',ENC_NAME='"+bean.getEmbossingname().trim()+"',MOBILENO='"+bean.getMobileno().trim()+"'
			// WHERE "+cond+"";

			/*
			 * if("0".equals(validcustid)){ //embnameupdateqry =
			 * " UPDATE PERS_CARD_ORDER SET EMBOSSING_NAME='"
			 * +bean.getEmbossingname().trim()+"',ENCODE_DATA='"+bean.
			 * getEmbossingname().trim()+"' WHERE CIN='"+bean.getCardno()+"'";
			 * embnameupdateqry = "UPDATE CARD_PRODUCTION SET EMB_NAME='"
			 * +bean.getEmbossingname().trim()+"',ENC_NAME='"+bean.
			 * getEmbossingname().trim()+"',MOBILENO='"+bean.getMobileno().trim(
			 * )+"' WHERE "+cond+""; }else{ embnameupdateqry =
			 * "UPDATE PERS_CARD_PROCESS SET EMB_NAME='"
			 * +bean.getEmbossingname().trim()+"',ENC_NAME='"+bean.
			 * getEmbossingname().trim()+"',MOBILENO='"+bean.getMobileno().trim(
			 * )+"' WHERE "+cond+""; }
			 */

			embnameupdateqry = "INSERT INTO PERS_CARD_PROCESS (INST_ID,ORDER_REF_NO,BIN, MCARD_NO,ACCT_NO ,ACCTTYPE_ID, ACCTSUB_TYPE_ID,"
					+ "ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,status_code,"
					+ "CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,"
					+ "SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,"
					+ "PUR_CNT,ECOM_AMT,ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,PIN_OFFSET,AUTO_ACCT_FLAG,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO,"
					+ "MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,CARD_COLLECT_BRANCH)" + " SELECT "
					+ "INST_ID,ORDER_REF_NO,BIN, MCARD_NO,ACCOUNT_NO,"
					+ "ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,"
					+ "BRANCH_CODE,'15',CAF_REC_STATUS,STATUS_CODE,CARD_CCY,PC_FLAG,ORDER_FLAG,"
					+ "ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,"
					+ "SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,"
					+ "ECOM_CNT,'" + bean.getEmbossingname() + "','" + bean.getEmbossingname()
					+ "',COURIER_ID,AUTH_DATE,PIN_OFFSET," + "AUTO_ACCT_FLAG,CVV1,CVV2,ICVV,'" + bean.getMobileno()
					+ "',CARD_REF_NO,'" + userid
					+ "',SYSDATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,CARD_COLLECT_BRANCH FROM CARD_PRODUCTION"
					+ " WHERE ORG_CHN = '" + cardno + "' AND CIN = '" + bean.getCustid() + "'AND ROWNUM=1 ";

			enctrace("embnameupdateqry-->" + embnameupdateqry);
			update = jdbctemplate.update(embnameupdateqry);

			embnameupdateqry_Hash = "INSERT INTO PERS_CARD_PROCESS_HASH (INST_ID,ORDER_REF_NO,BIN,HCARD_NO,ACCT_NO,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE)"
					+ " SELECT INST_ID,ORDER_REF_NO,BIN,HCARD_NO,ACCOUNT_NO,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,"
					+ "GENERATED_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"+hcardno+"' AND  "
							+ " CIN='"+bean.getCustid() +"' AND ROWNUM=1 ";

			
			enctrace("embnameupdateqry_Hash-->" + embnameupdateqry_Hash);
			update1 = jdbctemplate.update(embnameupdateqry_Hash);
			
			trace("update==== "+update +" update1 ====  "+update1);
			if(update==1 && update1==1){
				res=1;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public int updatestatusinezcard(CustomerDetailBean bean, String cardno, String hcardno, String padssenable,
			JdbcTemplate jdbctemplate) {
		String cond = "", validcustidqry = "", validcustid = "";
		String embnameupdateqry1 = "", embnameswitch1 = "";
		int update = 0;
		if (padssenable.equals("Y")) {
			cond = "CHN = '" + hcardno + "'";
		} else {
			cond = "CHN = '" + cardno + "'";
		}
		try {

			validcustidqry = "SELECT COUNT(1) FROM EZCARDINFO WHERE " + cond + "";
			enctrace("validcustidqry customer name edit-->" + validcustidqry);
			validcustid = (String) jdbctemplate.queryForObject(validcustidqry, String.class);
			if (!validcustid.isEmpty()) {
				embnameupdateqry1 = "UPDATE EZCARDINFO SET STATUS='97'  WHERE " + cond + "";
			}
			enctrace("embnameupdateqry-->" + embnameupdateqry1);
			update = jdbctemplate.update(embnameupdateqry1);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return update;
	}

	public int updatedetailincust(CustomerDetailBean bean, String cardno, String userid, JdbcTemplate jdbctemplate) {
		int update = 0;
		String cond = "", mobileupdateqry = "", validcustidqry = "", validcustid = "";
		/*
		 * if(bean.getCardno().length() > 6){ cond = "CARD_NO = '"+cardno+"'";
		 * }else{ cond = "CIN = '"+bean.getCustid()+"'"; }
		 */
		try {
			validcustidqry = "SELECT COUNT(1) FROM CARD_PRODUCTION WHERE ORG_CHN = '" + cardno + "' AND CIN = '"
					+ bean.getCustid() + "'AND ROWNUM=1 ";
			enctrace("validcustidqry customer name edit-->" + validcustidqry);
			validcustid = (String) jdbctemplate.queryForObject(validcustidqry, String.class);
			/*
			 * if("0".equals(validcustid)){ //mobileupdateqry =
			 * "UPDATE CUSTOMERINFO SET MOBILE='"+bean.getMobileno()+
			 * "' WHERE CIN=(SELECT CIN FROM PERS_CARD_ORDER WHERE "+cond+
			 * " AND ROWNUM=1)"; mobileupdateqry =
			 * "UPDATE CUSTOMERINFO SET MOBILE='"+bean.getMobileno()+
			 * "' WHERE CIN=(SELECT CIN FROM CARD_PRODUCTION WHERE "+cond+
			 * " AND ROWNUM=1)"; }else{ mobileupdateqry =
			 * "UPDATE CUSTOMERINFO SET MOBILE='"+bean.getMobileno()+
			 * "' WHERE CIN=(SELECT CIN FROM PERS_CARD_PROCESS WHERE "+cond+
			 * " AND ROWNUM=1)"; }
			 */
			// mobileupdateqry = "UPDATE CUSTOMERINFO SET
			// MOBILE='"+bean.getMobileno()+"', WHERE CIN=(SELECT CIN FROM
			// CARD_PRODUCTION WHERE "+cond+" AND ROWNUM=1)";
			// mobileupdateqry = "UPDATE CUSTOMERINFO SET
			// MOBILE='"+bean.getMobileno()+"',FNAME='"+bean.getFname()+"',MNAME='"+bean.getMname()+"',LNAME='"+bean.getLname()+"',DOB='"+bean.getDob()+"',MARITAL_STATUS='"+bean.getMartialstatus()+"',NATIONALITY='"+bean.getNationality()+"',SPOUCE_NAME='"+bean.getSpousename()+"',";
			// mobileupdateqry
			// +="MOTHER_NAME='"+bean.getMothername()+"',FATHER_NAME='"+bean.getFathername()+"',E_MAIL='"+bean.getEmail()+"',P_PO_BOX='"+bean.getPobox()+"',P_HOUSE_NO='"+bean.getPhouseno()+"',P_STREET_NAME='"+bean.getPstname()+"',P_WARD_NAME='"+bean.getPwardname()+"',P_CITY='"+bean.getPcity()+"',P_DISTRICT='"+bean.getPcity()+"'";
			// mobileupdateqry +=" WHERE CIN=(SELECT CIN FROM CARD_PRODUCTION
			// WHERE "+cond+" AND ROWNUM=1)";

			mobileupdateqry = "INSERT INTO CUSTOMERINFO_PROCESS (INST_ID,ORDER_REF_NO,CIN,FNAME,MNAME,LNAME,DOB,GENDER,MARITAL_STATUS,NATIONALITY,";
			mobileupdateqry += "DOCUMENT_PROVIDED,DOCUMENT_NUMBER,SPOUCE_NAME,MOTHER_NAME,FATHER_NAME,MOBILE,E_MAIL,P_PO_BOX,P_HOUSE_NO,";
			mobileupdateqry += "P_STREET_NAME,P_WARD_NAME,P_CITY,P_DISTRICT,P_PHONE1,P_PHONE2,C_PO_BOX,C_HOUSE_NO,C_STREET_NAME,C_WARD_NAME";
			mobileupdateqry += ",C_CITY,C_DISTRICT,C_PHONE1,C_PHONE2,MAKER_DATE,MAKER_ID,CHECKER_DATE,CHECKER_ID,MKCK_STATUS,CUSTOMER_STATUS)";
			mobileupdateqry += " SELECT ";
			mobileupdateqry += "INST_ID,ORDER_REF_NO,CIN,'" + bean.getFname() + "','" + bean.getMname() + "','"
					+ bean.getLname() + "',TO_DATE('" + bean.getDob() + "','DD-MM-YY'),GENDER,'"
					+ bean.getMartialstatus() + "',";
			mobileupdateqry += "'" + bean.getNationality() + "',DOCUMENT_PROVIDED,DOCUMENT_NUMBER,'"
					+ bean.getSpousename() + "','" + bean.getMothername() + "',";
			mobileupdateqry += "'" + bean.getFathername() + "','" + bean.getMobileno() + "','" + bean.getEmail() + "','"
					+ bean.getPobox() + "','" + bean.getPhouseno() + "','" + bean.getPstname() + "','"
					+ bean.getPwardname() + "','" + bean.getPcity() + "','" + bean.getPdist() + "',";
			mobileupdateqry += "P_PHONE1,P_PHONE2,C_PO_BOX,C_HOUSE_NO,C_STREET_NAME,C_WARD_NAME,C_CITY,C_DISTRICT,C_PHONE1,";
			mobileupdateqry += "C_PHONE2,MAKER_DATE,'" + userid
					+ "',CHECKER_DATE,CHECKER_ID,MKCK_STATUS,CUSTOMER_STATUS";
			mobileupdateqry += " FROM CUSTOMERINFO WHERE TRIM(CIN)=(SELECT TRIM(CIN) FROM CARD_PRODUCTION WHERE ORG_CHN = '"
					+ cardno + "' AND CIN = '" + bean.getCustid() + "'AND ROWNUM=1 )";

			update = jdbctemplate.update(mobileupdateqry);
			enctrace("**********insertedin the custprocess-->" + mobileupdateqry + "\n *********" + update
					+ "for custid " + bean.getCustid());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return update;
	}

	public int updatezcustinfoswitch(CustomerDetailBean bean, String cardno, String hcardno, String padssenable,
			JdbcTemplate jdbctemplate) {
		int update = 0;
		String cond = "", mobileupdateqry1 = "", validcustidqry = "", validcustid = "";
		if (padssenable.equals("Y")) {
			if (bean.getCardno().length() > 6) {
				cond = "CHN = '" + hcardno + "'";
			} else {
				cond = "CHN = '" + cardno + "'";
			}
		}

		String dob = "", condi = "";
		if (bean.getDob() == "" || bean.getDob() == null) {
			// dob="sysdate";
			condi = "DOB=TO_DATE(sysdate,'DD-MM-YY'),";
		} else {
			// dob=bean.getD ob();
			// DOB= TO_DATE('"+bean.getDob()+"','DD-MM-YY'),
			condi = "DOB=TO_DATE('" + bean.getDob() + "','DD-MM-YY'),";
			System.out.println("customer registration availableee dob" + dob);
		}
		trace("customer registration dob" + condi);

		try {
			validcustidqry = "SELECT COUNT(1) FROM EZCARDINFO WHERE " + cond + "";
			enctrace("validcustidqry customer name edit-->" + validcustidqry);
			validcustid = (String) jdbctemplate.queryForObject(validcustidqry, String.class);
			if (!"0".equalsIgnoreCase(validcustid))
				mobileupdateqry1 = "UPDATE EZCUSTOMERINFO SET MOBILE='" + bean.getMobileno() + "',NAME='"
						+ bean.getEmbossingname() + "'," + condi + "ADDRESS1='" + bean.getPobox() + "',ADDRESS2='"
						+ bean.getPhouseno() + "',ADDRESS3='" + bean.getPstname() + "'";
			mobileupdateqry1 += "WHERE CUSTID=(SELECT CUSTID FROM EZCARDINFO WHERE " + cond + " AND ROWNUM=1)";
			enctrace("mobileupdateqry-->" + mobileupdateqry1);
			update = jdbctemplate.update(mobileupdateqry1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return update;
	}

	public List<Map<String, Object>> getCustDetails(String cin, String type, String encardno,
			JdbcTemplate jdbctemplate) {

		String embnameqry = "", condi = "";
		List custdetails = null;
		try {
			if (type.equalsIgnoreCase("CIN")) {
				condi = "";
			}
			if (type.equalsIgnoreCase("order")) {
				condi = "AND ORG_CHN='" + encardno + "'";
			}
			if (type.equalsIgnoreCase("PHONE")) {
				condi = "AND ORG_CHN='" + encardno + "'";
			}
			if (type.equalsIgnoreCase("CARD")) {
				condi = "AND ORG_CHN='" + encardno + "'";
			}

			// embnameqry = "SELECT EMB_NAME,MOBILENO,CIN FROM CARD_PRODUCTION
			// WHERE "+cond+" AND ROWNUM=1";
			// embnameqry = "SELECT A.BRANCH_CODE,A.CARD_NO,A.EMB_NAME AS
			// EMB_NAME,NVL(A.MOBILENO,'--') AS MOBILENO,NVL(B.FNAME,'--') AS
			// FNAME,NVL(B.MNAME,'--') AS MNAME,NVL(B.LNAME,'--') AS LNAME,B.DOB
			// AS DOB,NVL(B.MARITAL_STATUS,'--') AS
			// MARITAL_STATUS,NVL(B.NATIONALITY,'--') AS
			// NATIONALITY,NVL(B.DOCUMENT_PROVIDED,'--') AS DOCUMENT_PROVIDED
			// ,NVL(B.DOCUMENT_NUMBER,'--') AS
			// DOCUMENT_NUMBER,NVL(B.SPOUCE_NAME,'--') AS
			// SPOUCE_NAME,NVL(B.MOTHER_NAME,'--') AS
			// MOTHER_NAME,NVL(B.FATHER_NAME,'--') AS
			// FATHER_NAME,NVL(B.E_MAIL,'--') AS E_MAIL,NVL(B.P_PO_BOX,'--') AS
			// P_PO_BOX, NVL(B.P_HOUSE_NO,'--') AS
			// P_HOUSE_NO,NVL(B.P_STREET_NAME,'--') AS
			// P_STREET_NAME,NVL(B.P_WARD_NAME,'--') AS
			// P_WARD_NAME,NVL(B.P_CITY,'--') AS P_CITY,NVL(B.P_DISTRICT,'--')
			// AS P_DISTRICT,B.P_PHONE1,B.P_PHONE2,A.HCARD_NO as HCARD_NO FROM
			// CARD_PRODUCTION A,CUSTOMERINFO B WHERE A.CIN='"+cin+"' "+condi+"
			// AND B.CIN=A.CIN";

			embnameqry = "SELECT A.BRANCH_CODE,A.ORG_CHN,A.EMB_NAME AS EMB_NAME,NVL(A.MOBILENO,'--') AS MOBILENO,NVL(B.FNAME,'--') AS FNAME,NVL(B.MNAME,'--') AS MNAME,NVL(B.LNAME,'--') AS LNAME,B.DOB AS DOB,NVL(B.MARITAL_STATUS,'--') AS MARITAL_STATUS,NVL(B.NATIONALITY,'--') AS NATIONALITY,NVL(B.DOCUMENT_PROVIDED,'--') AS DOCUMENT_PROVIDED ,NVL(B.DOCUMENT_NUMBER,'--') AS DOCUMENT_NUMBER,NVL(B.SPOUCE_NAME,'--') AS SPOUCE_NAME,NVL(B.MOTHER_NAME,'--') AS MOTHER_NAME,NVL(B.FATHER_NAME,'--') AS FATHER_NAME,NVL(B.E_MAIL,'--') AS E_MAIL,NVL(B.P_PO_BOX,'--') AS P_PO_BOX, NVL(B.P_HOUSE_NO,'--') AS P_HOUSE_NO,NVL(B.P_STREET_NAME,'--') AS P_STREET_NAME,NVL(B.P_WARD_NAME,'--') AS P_WARD_NAME,NVL(B.P_CITY,'--') AS P_CITY,NVL(B.P_DISTRICT,'--') AS P_DISTRICT,B.P_PHONE1,B.P_PHONE2 FROM CARD_PRODUCTION A,CUSTOMERINFO B WHERE A.CIN='"
					+ cin + "' " + condi + " AND B.CIN=A.CIN";

			enctrace("cin customer name edit-->" + embnameqry);
			custdetails = jdbctemplate.queryForList(embnameqry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return custdetails;

	}

	public List getcardeditdetailsforauditlog(String instid, String cardno, JdbcTemplate jdbctemplate) {

		String embnameqry = "";
		List custdetails = null;
		try {

			// embnameqry = "SELECT EMB_NAME,MOBILENO,CIN FROM CARD_PRODUCTION
			// WHERE "+cond+" AND ROWNUM=1";
			embnameqry = "SELECT * FROM CARD_PRODUCTION WHERE ORG_CHN='" + cardno + "' AND inst_id='" + instid + "'";

			enctrace("getcardeditdetailsforauditlog_edit_qry-->" + embnameqry);
			custdetails = jdbctemplate.queryForList(embnameqry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return custdetails;

	}

	public int updateEmbNameinproduction(CustomerDetailBean bean, String cardno, String userid,
			JdbcTemplate jdbctemplate) {

		String embnameupdateqry = "", embnameswitch = "", cond = "", validcustidqry = "";
		String embnameupdateqry1 = "", embnameswitch1 = "";
		int update = 0;

		try {

			/*
			 * validcustidqry =
			 * "SELECT COUNT(1) FROM CARD_PRODUCTION WHERE CARD_NO='"
			 * +cardno+"'"; enctrace("validcustidqry customer name edit-->"
			 * +validcustidqry); int validcustid
			 * =jdbctemplate.queryForInt(validcustidqry);
			 * enctrace("testbysardar"+validcustid); if(validcustid==1){
			 */
			String name = "";
			if (bean.getEmbossingname() == "" || bean.getEmbossingname() == null) {
				name = "NA";
				trace("adding custname for customer EDIT" + name);
			} else {
				name = bean.getEmbossingname().trim();
				trace("available name for customer EDIT" + name);

			}
			embnameupdateqry = "UPDATE CARD_PRODUCTION SET CARD_STATUS='09',STATUS_CODE='16', EMB_NAME='" + name
					+ "',ENC_NAME='" + name + "',CHECKER_ID='" + userid + "',CHECKER_DATE=SYSDATE,MOBILENO='"
					+ bean.getMobileno().trim() + "' WHERE ORG_CHN='" + cardno + "'";
			// }
			enctrace("embnameupdateqryCARD_PRODUCTION:::  " + embnameupdateqry);
			update = jdbctemplate.update(embnameupdateqry);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return update;
	}

	/*
	 * public int updateEmbNameinswitch(CustomerDetailBean bean, String
	 * cardno,String hcardno,String padssenable, JdbcTemplate jdbctemplate) {
	 * String cond = "", validcustidqry= "" , validcustid= ""; String
	 * embnameupdateqry1 = "",embnameswitch1=""; int update=0;
	 * if(padssenable.equals("Y")){ if(bean.getCardno().length() > 6){ cond =
	 * "CHN = '"+hcardno+"'"; }else{ cond = "CUSTID = '"+bean.getCardno()+"'"; }
	 * } else{
	 * 
	 * if(bean.getCardno().length() > 6){ cond = "CHN = '"+cardno+"'"; }else{
	 * cond = "CUSTID = '"+bean.getCardno()+"'"; }
	 * 
	 * } try{
	 * 
	 * validcustidqry = "SELECT COUNT(1) FROM EZCARDINFO WHERE "+cond+"";
	 * enctrace("validcustidqry customer name edit-->"+validcustidqry);
	 * validcustid = (String) jdbctemplate.queryForObject(validcustidqry,
	 * String.class); embnameupdateqry1 =
	 * "UPDATE EZCARDINFO SET STATUS='97'  WHERE "+cond+"";
	 * 
	 * if("0".equals(validcustid)){ //embnameupdateqry =
	 * " UPDATE PERS_CARD_ORDER SET EMBOSSING_NAME='"
	 * +bean.getEmbossingname().trim()+"',ENCODE_DATA='"+bean.getEmbossingname()
	 * .trim()+"' WHERE CIN='"+bean.getCardno()+"'"; embnameupdateqry =
	 * "UPDATE CARD_PRODUCTION SET EMB_NAME='"
	 * +bean.getEmbossingname().trim()+"',ENC_NAME='"+bean.getEmbossingname().
	 * trim()+"',MOBILENO='"+bean.getMobileno().trim()+"' WHERE "+cond+"";
	 * }else{ embnameupdateqry = "UPDATE PERS_CARD_PROCESS SET EMB_NAME='"
	 * +bean.getEmbossingname().trim()+"',ENC_NAME='"+bean.getEmbossingname().
	 * trim()+"',MOBILENO='"+bean.getMobileno().trim()+"' WHERE "+cond+""; }
	 * enctrace("embnameupdateqry-->"+embnameupdateqry1); update =
	 * jdbctemplate.update(embnameupdateqry1);
	 * 
	 * 
	 * }catch (Exception e) { e.printStackTrace(); } return update; }
	 */

	public int updatedetailincustinfo(CustomerDetailBean bean, String cardno, String userid, String padssenable,
			String hcardno, JdbcTemplate jdbctemplate) {
		int update = 0;
		String cond = "", mobileupdateqry = "", validcustidqry = "", validcustid = "";

		try {
			validcustidqry = "SELECT COUNT(1) FROM CARD_PRODUCTION WHERE ORG_CHN='" + cardno + "'";
			enctrace("validcustidqry customer name edit-->" + validcustidqry);

			validcustid = (String) jdbctemplate.queryForObject(validcustidqry, String.class);
			if (!"0".equalsIgnoreCase(validcustid)) {
				String dob = "", condi = "";
				if (bean.getDob() == "" || bean.getDob() == null) {
					// dob="sysdate";
					condi = "DOB=TO_DATE(sysdate,'DD-MM-YY'),";

				} else {
					dob = bean.getDob();
					condi = "DOB=TO_DATE('" + dob + "','DD-MM-YY'),";
					System.out.println("customer registration availableee dob" + dob);
				}
				trace("customer registration dob" + condi);

				// mobileupdateqry = "UPDATE CUSTOMERINFO SET
				// MOBILE='"+bean.getMobileno()+"', WHERE CIN=(SELECT CIN FROM
				// CARD_PRODUCTION WHERE "+cond+" AND ROWNUM=1)";
				mobileupdateqry = "UPDATE CUSTOMERINFO SET " + condi + "MOBILE='" + bean.getMobileno() + "',FNAME='"
						+ bean.getFname() + "',MNAME='" + bean.getMname() + "',LNAME='" + bean.getLname()
						+ "', MARITAL_STATUS='" + bean.getMartialstatus() + "',NATIONALITY='" + bean.getNationality()
						+ "',SPOUCE_NAME='" + bean.getSpousename() + "',";
				mobileupdateqry += "MOTHER_NAME='" + bean.getMothername() + "',FATHER_NAME='" + bean.getFathername()
						+ "',E_MAIL='" + bean.getEmail() + "',P_PO_BOX='" + bean.getPobox() + "',P_HOUSE_NO='"
						+ bean.getPhouseno() + "',P_STREET_NAME='" + bean.getPstname() + "',P_WARD_NAME='"
						+ bean.getPwardname() + "',P_CITY='" + bean.getPcity() + "',P_DISTRICT='" + bean.getPcity()
						+ "'";
				mobileupdateqry += " WHERE CIN=(SELECT CIN FROM CARD_PRODUCTION WHERE ORG_CHN='" + cardno
						+ "' AND ROWNUM=1)";
			}

			enctrace("authcustdetails:::::: " + mobileupdateqry);
			update = jdbctemplate.update(mobileupdateqry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return update;
	}
	
	public int checkInprocessTable(CustomerDetailBean bean,JdbcTemplate jdbctemplate){
		int count=0;
		
		String checkProcess="SELECT COUNT(*) FROM PERS_CARD_PROCESS WHERE CARD_STATUS='15' AND CIN='"+bean.getCustid()+"'";
		enctrace("checkProcess ::" + checkProcess);
		
		count=jdbctemplate.queryForInt(checkProcess);
		return count;
	}


}
