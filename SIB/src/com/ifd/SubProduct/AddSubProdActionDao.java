package com.ifd.SubProduct;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;

import test.Date;

@Transactional
public class AddSubProdActionDao extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// private JdbcTemplate jdbctemplate = new JdbcTemplate();
	// private PlatformTransactionManager txManager=new
	// DataSourceTransactionManager();

	public String getQueryScheme(String instid, JdbcTemplate jdbctemplate) {
		String query_scheme = null;
		// by gowtham-210819
		// query_scheme="select FEE_CODE,FEE_DESC from FEE_DESC where INST_ID=?
		// and AUTH_CODE=?";
		query_scheme = "select FEE_CODE,FEE_DESC from FEE_DESC where INST_ID='" + instid + "' and AUTH_CODE='1'";

		return query_scheme;
	}

	public String getProductMasterDetails(String instid, JdbcTemplate jdbctemplate) {
		String productMasterDetails;
		productMasterDetails = "SELECT Convert(nvarchar(50),PRODUCT_CODE)+'-'+Convert(nvarchar(50),BIN) as PRODUCT_CODE, CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID='"
				+ instid + "'";
		return productMasterDetails;
	}

	public String getQueryCurrency1(String instid, JdbcTemplate jdbctemplate) {
		String query_currency = null;
		query_currency = "SELECT NUMERIC_CODE as CUR_CODE,CURRENCY_DESC as CURRENCY_DESC FROM GLOBAL_CURRENCY ";
		return query_currency;
	}

	public String getQueryCurrency(String instid, JdbcTemplate jdbctemplate) {
		String query_currency = null;
		query_currency = "SELECT p.CUR_CODE,h.CURRENCY_DESC FROM GLOBAL_CURRENCY h JOIN INSTITUTION_CURRENCY p ON (h.NUMERIC_CODE=p.CUR_CODE  and p.INST_ID  ='"
				+ instid + "')";
		return query_currency;
	}

	public String getServiceCodelist(String instid, String productid, String subproductid, JdbcTemplate jdbctemplate) {

		String servicecodeqry = "SELECT SERVICE_CODE FROM INSTPROD_DETAILS WHERE INST_ID='" + instid
				+ "'AND  PRODUCT_CODE='" + productid + "' AND SUB_PROD_ID='" + subproductid + "'  AND AUTH_STATUS='1'";
		// String servicecodeqry = "SELECT SERVICE_CODE FROM INSTPROD_DETAILS
		// WHERE INST_ID=? AND PRODUCT_CODE=? AND SUB_PROD_ID=? AND
		// AUTH_STATUS=?";

		return servicecodeqry;
	}

	public String getLimitExistlist(String instid, JdbcTemplate jdbctemplate) {

		String limitcodeqry = "SELECT LIMIT_ID, LIMIT_DESC FROM LIMIT_DESC WHERE INST_ID='" + instid
				+ "' AND AUTH_STATUS='1' AND LIMIT_BASE='P'";
		// String limitcodeqry = "SELECT LIMIT_ID, LIMIT_DESC FROM LIMIT_DESC
		// WHERE INST_ID=? AND AUTH_STATUS=? AND LIMIT_BASE=?";
		trace("limitcodeqry__" + limitcodeqry);
		return limitcodeqry;
	}

	/*
	 * public String getCardtypeProductlist(String instid,JdbcTemplate
	 * jdbctemplate) { String prodlist =
	 * "SELECT PRODUCT_CODE, CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID='"
	 * +instid+"' and AUTH_CODE='1' AND DELETED_FLAG !='2' "; trace(
	 * "prodlist --- "+prodlist);System.out.println("prodlist ---- "+prodlist);
	 * return prodlist; }
	 */
	public List getSuproductSelectResult(String[] prodbin, String i_Name, JdbcTemplate jdbctemplate) {
		List suproduct_select_result = null;

		/*
		 * String suproduct_select =
		 * "select SUB_PROD_ID,SUB_PROD_DESC from IFPRODUCT_SUBTYPE where CARD_TYPE_ID='"
		 * +prodbin[0]+
		 * "' and SUB_PROD_ID not in (select SUB_PROD_ID from INSTPROD_DETAILS where CARD_TYPE_ID='"
		 * +prodbin[0]+"' and bin='"+prodbin[1]+"' and INST_ID='"+i_Name+"')";
		 * //String qury=
		 * "select PRODUCT_CODE,PRODUCT_DESC from IFPRODUCT_SUBTYPE where PRODUCT_ID='"
		 * +prodid+"'"; System.out.println("The Result is  " +
		 * suproduct_select); suproduct_select_result
		 * =jdbctemplate.queryForList(suproduct_select);
		 */

		// by gowtham-210819
		String suproduct_select = "select SUB_PROD_ID,SUB_PROD_DESC from IFPRODUCT_SUBTYPE where CARD_TYPE_ID=? and SUB_PROD_ID not in (select SUB_PROD_ID from INSTPROD_DETAILS where CARD_TYPE_ID=? and bin=? and INST_ID=? )";
		// String qury="select PRODUCT_CODE,PRODUCT_DESC from IFPRODUCT_SUBTYPE
		// where PRODUCT_ID='"+prodid+"'";
		System.out.println("The Result is  " + suproduct_select);
		suproduct_select_result = jdbctemplate.queryForList(suproduct_select,
				new Object[] { prodbin[0], prodbin[0], prodbin[1], i_Name });

		return suproduct_select_result;
	}

	public List getViewProdresult(String i_Name, JdbcTemplate jdbctemplate) {
		trace("getViewProdresult started");
		List View_prod_result = null;
		String viewProduct_details = "select * from PRODUCTINFO where inst_id='" + i_Name
				+ "' and bin in (select DISTINCT(bin) from INSTPROD_DETAILS where inst_id='" + i_Name
				+ "' ) order by BIN ";
		trace("viewProduct_details--->" + viewProduct_details);
		enctrace("subprd query for direct display " + viewProduct_details);
		View_prod_result = jdbctemplate.queryForList(viewProduct_details);
		trace("View_prod_result--->" + View_prod_result);
		trace("getViewProdresult started");
		return View_prod_result;
	}

	public String getAuthSubqry(String productcode, String subproductid, JdbcTemplate jdbctemplate) {
		String authstatus = null;

		/*
		 * String authsubqry =
		 * "SELECT AUTH_STATUS FROM INSTPROD_DETAILS WHERE PRODUCT_CODE='"
		 * +productcode+"' AND SUB_PROD_ID='"+subproductid+"'"; enctrace(
		 * "authsubqry --- "+authsubqry); authstatus =
		 * (String)jdbctemplate.queryForObject(authsubqry, String.class);
		 */

		// by gowtham-210819
		String authsubqry = "SELECT AUTH_STATUS FROM INSTPROD_DETAILS WHERE PRODUCT_CODE=? AND SUB_PROD_ID=?";
		enctrace("authsubqry --- " + authsubqry);
		authstatus = (String) jdbctemplate.queryForObject(authsubqry, new Object[] { productcode, subproductid },
				String.class);

		return authstatus;
	}

	public List getViewProdResult(String i_Name, JdbcTemplate jdbctemplate) {
		List View_prod_result = null;
		String viewProduct_details = "select * from PRODUCTINFO where inst_id='" + i_Name
				+ "' and bin in (select DISTINCT(bin) from INSTPROD_DETAILS where inst_id='" + i_Name
				+ "' ) order by BIN ";
		enctrace("subprd query for direct display " + viewProduct_details);
		View_prod_result = jdbctemplate.queryForList(viewProduct_details);
		return View_prod_result;

	}

	public String getProductinfoList(String inst_Name, String productname, String subproduct,
			JdbcTemplate jdbctemplate) {
		StringBuilder product_query = new StringBuilder();

		product_query.append("SELECT ");
		product_query.append(
				"INST_ID, PRODUCT_CODE, SUB_PROD_ID, SUB_PRODUCT_NAME, FEE_CODE, LIMIT_ID, SERVICE_CODE, EXPIRY_PERIOD, DECODE(PERSONALIZED,1,'ONLY PERSONALIZED','0','ONLY INSTANT','2','INSTANT AND PERSONALIZED') PERSONALIZED, ");
		product_query.append(
				"DECODE(PIN_GEN_REQ,'Y','YES','N','NO') PIN_GEN_REQ, DECODE(MAINTAIN_REQ,'Y','YES','N','NO') MAINTAIN_REQ,  ");
		product_query.append(
				"DECODE(AUTH_STATUS,'0','Waiting for authorize','1','Authorized','9','Deauthorized') AUTH_STATUS, CONFIG_BY,  ");
		product_query.append("to_char(CONFIG_DATE,'DD-MON-YYYY') CONFIG_DATE, AUTH_BY, AUTH_DATE, REMARKS ");
		product_query.append("FROM ");
		product_query.append("INSTPROD_DETAILS ");
		product_query.append("where INST_ID='" + inst_Name + "' and PRODUCT_CODE='" + productname
				+ "' and SUB_PROD_ID='" + subproduct + "'");
		enctrace("product_query---> " + product_query.toString());

		/*
		 * // by gowtham-210819 product_query.append("SELECT ");
		 * product_query.append(
		 * "INST_ID, PRODUCT_CODE, SUB_PROD_ID, SUB_PRODUCT_NAME, FEE_CODE, LIMIT_ID, SERVICE_CODE, EXPIRY_PERIOD, DECODE(PERSONALIZED,1,'ONLY PERSONALIZED','0','ONLY INSTANT','2','INSTANT AND PERSONALIZED') PERSONALIZED, "
		 * ); product_query.append(
		 * "DECODE(PIN_GEN_REQ,'Y','YES','N','NO') PIN_GEN_REQ, DECODE(MAINTAIN_REQ,'Y','YES','N','NO') MAINTAIN_REQ,  "
		 * ); product_query.append(
		 * "DECODE(AUTH_STATUS,'0','Waiting for authorize','1','Authorized','9','Deauthorized') AUTH_STATUS, CONFIG_BY,  "
		 * ); product_query.append(
		 * "to_char(CONFIG_DATE,'DD-MON-YYYY') CONFIG_DATE, AUTH_BY, AUTH_DATE, REMARKS "
		 * ); product_query.append("FROM "); product_query.append(
		 * "INSTPROD_DETAILS "); product_query.append(
		 * "where INST_ID=? and PRODUCT_CODE=? and SUB_PROD_ID=?"); enctrace(
		 * "product_query---> " + product_query.toString());
		 */

		return product_query.toString();
	}

	public String getFeedescList(String instid, String code, JdbcTemplate jdbctemplate) {
		String qury_str = "SELECT FEE_DESC FROM FEE_DESC where INST_ID='" + instid + "' AND FEE_CODE='" + code + "'";
		/*
		 * String qury_str =
		 * "SELECT FEE_DESC FROM FEE_DESC where INST_ID=? AND FEE_CODE=?";
		 */

		return qury_str;
	}

	public String getLimitdescList(String instid, String code, JdbcTemplate jdbctemplate) {
		String qury_str = "SELECT LIMIT_DESC FROM LIMIT_DESC where INST_ID='" + instid + "' AND LIMIT_ID='" + code
				+ "'";
		// String qury_str = "SELECT LIMIT_DESC FROM LIMIT_DESC where INST_ID=?
		// AND LIMIT_ID=?";

		return qury_str;
	}

	public String getSchemedescList(String instid, String code, JdbcTemplate jdbctemplate) {
		String qury_str = "SELECT SCH_NAME FROM IFP_GL_SCHEME_MASTER where INST_ID='" + instid + "' AND SCH_CODE='"
				+ code + "'";
		/*
		 * String qury_str =
		 * "SELECT SCH_NAME FROM IFP_GL_SCHEME_MASTER where INST_ID=? AND SCH_CODE=?"
		 * ;
		 */
		return qury_str;

	}

	public String getProductdesc(String instid, String prodcode, JdbcTemplate jdbctemplate) {

		/*
		 * String qryproductdesc =
		 * "SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID='"+instid+
		 * "' and PRODUCT_CODE='"+prodcode+"'"; enctrace( " getProductdesc " +
		 * qryproductdesc);
		 * 
		 * if ( jdbctemplate.getMaxRows() == 0 ) { String bin_desc =
		 * (String)jdbctemplate.queryForObject(qryproductdesc, String.class);
		 */

		// by gowtham-210819
		String qryproductdesc = "SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID=? and PRODUCT_CODE=?";
		enctrace(" getProductdesc " + qryproductdesc);

		if (jdbctemplate.getMaxRows() == 0) {
			String bin_desc = (String) jdbctemplate.queryForObject(qryproductdesc, new Object[] { instid, prodcode },
					String.class);

			return bin_desc;
		} else {
			return "UNKNOWN PRODUCT";
		}
	}

	public List getProdDetResult(String inst_Name, String product_code, JdbcTemplate jdbctemplate) {
		/*
		 * List prod_det_result = null; String prod_det_query=
		 * "select * from INSTPROD_DETAILS where INST_ID='"+inst_Name+
		 * "' and PRODUCT_CODE='"+product_code+"'"; enctrace(
		 * "QUERY generateSubprodlist "+prod_det_query); prod_det_result
		 * =jdbctemplate.queryForList(prod_det_query);
		 */

		// by gowtham-210819
		List prod_det_result = null;
		String prod_det_query = "select * from INSTPROD_DETAILS where INST_ID=? and PRODUCT_CODE=?";
		enctrace("QUERY generateSubprodlist " + prod_det_query);
		prod_det_result = jdbctemplate.queryForList(prod_det_query, new Object[] { inst_Name, product_code });

		return prod_det_result;
	}

	public int getProdDelResult(String inst_Name, String product_code, JdbcTemplate jdbctemplate) {
		int prod_del_result = -1;

		/*
		 * String prod_det_query="delete from INSTPROD_DETAILS where INST_ID='"
		 * +inst_Name+"' and PRODUCT_CODE='"+product_code+"'";
		 * System.out.println("QUERY delete subproduct "+prod_det_query);
		 * prod_del_result =jdbctemplate.update(prod_det_query);
		 */

		// by gowtham-210819
		String prod_det_query = "delete from INSTPROD_DETAILS where INST_ID=? and PRODUCT_CODE=?";
		System.out.println("QUERY delete subproduct " + prod_det_query);
		prod_del_result = jdbctemplate.update(prod_det_query, new Object[] { inst_Name, product_code });

		return prod_del_result;
	}

	public List getGlschemeList(String instid, String glcode, JdbcTemplate jdbctemplate) throws Exception {
		List glschemelist = null;

		/*
		 * String glschemelistqry =
		 * "SELECT SCH_CODE, SCH_NAME  FROM IFP_GL_SCHEME_MASTER WHERE INST_ID='"
		 * +instid+"' AND GL_CODE='"+glcode+"' AND SCH_STATUS='1' ";
		 * System.out.println("glschemelistqry==> "+glschemelistqry);
		 * glschemelist = jdbctemplate.queryForList(glschemelistqry);
		 */

		// by gowtham-210819
		String glschemelistqry = "SELECT SCH_CODE, SCH_NAME  FROM IFP_GL_SCHEME_MASTER WHERE INST_ID=? AND GL_CODE=? AND SCH_STATUS=? ";
		System.out.println("glschemelistqry==> " + glschemelistqry);
		glschemelist = jdbctemplate.queryForList(glschemelistqry, new Object[] { instid, glcode, "1" });
		return glschemelist;

	}

	public List getSelectList(JdbcTemplate jdbctemplate) {
		List selectlist = null;

		/*
		 * String mcclistqry =
		 * "SELECT MCC_CODE, MCC_DESC  FROM EZMMS_MCC_INFO WHERE ALLOWED='1'";
		 * System.out.println("sel_qury==>"+mcclistqry); selectlist
		 * =jdbctemplate.queryForList(mcclistqry);
		 */

		// by gowtham-210819
		String mcclistqry = "SELECT MCC_CODE, MCC_DESC  FROM EZMMS_MCC_INFO WHERE ALLOWED=?";
		System.out.println("sel_qury==>" + mcclistqry);
		selectlist = jdbctemplate.queryForList(mcclistqry, new Object[] { "1" });
		return selectlist;
	}

	public List getSelectList(String instid, JdbcTemplate jdbctemplate) {
		List selectlist = null;

		/*
		 * String networklistqry =
		 * "SELECT NETWORK_ID, NETWORK_DESC  FROM IFP_NETWORK_MASTER WHERE NETWORK_STATUS='1' AND INST_ID='"
		 * +instid+"'"; System.out.println("sel_qury==>"+networklistqry);
		 * selectlist =jdbctemplate.queryForList(networklistqry);
		 */

		// by gowtham-210819
		String networklistqry = "SELECT NETWORK_ID, NETWORK_DESC  FROM IFP_NETWORK_MASTER WHERE NETWORK_STATUS=? AND INST_ID=?";
		System.out.println("sel_qury==>" + networklistqry);
		selectlist = jdbctemplate.queryForList(networklistqry, new Object[] { "1", instid });

		return selectlist;
	}

	public List getSelectListMerchant(String instid, JdbcTemplate jdbctemplate) {
		List selectlistmerchant = null;

		/*
		 * String networklistqry =
		 * "SELECT MERCH_ID,MERCH_NAME  FROM EZMMS_MERCHANTPROFILE WHERE INST_ID='"
		 * +instid+"'"; System.out.println("sel_qury==>"+networklistqry);
		 * selectlistmerchant =jdbctemplate.queryForList(networklistqry);
		 */

		// by gowtham-210819
		String networklistqry = "SELECT MERCH_ID,MERCH_NAME  FROM EZMMS_MERCHANTPROFILE WHERE INST_ID=?";
		System.out.println("sel_qury==>" + networklistqry);
		selectlistmerchant = jdbctemplate.queryForList(networklistqry, new Object[] { instid });

		return selectlistmerchant;
	}

	public int deleteMappedRecord(String instid, String recordid, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;

		/*
		 * String delqry = "DELETE FROM IFP_GL_KEYS_MAPPING WHERE INST_ID='"
		 * +instid+"' AND RECORD_ID='"+recordid+"'"; enctrace("delqry  : " +
		 * delqry ); x = jdbctemplate.update(delqry );
		 */

		// by gowtham-210819
		String delqry = "DELETE FROM IFP_GL_KEYS_MAPPING WHERE INST_ID=? AND RECORD_ID=?";
		enctrace("delqry  : " + delqry);
		x = jdbctemplate.update(delqry, new Object[] { instid, recordid });

		return x;
	}

	public int insertSubGlMapping(String instid, String recordid, String product, String subproductid, String glkey,
			String subglcode, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;

		/*
		 * String insertqry =
		 * "INSERT INTO IFP_GL_KEYS_MAPPING (INST_ID,RECORD_ID, PRODUCT_CODE, SUBPRODUCT_ID,GL_TYPE, GL_CODE ) VALUES ('"
		 * +instid+"','"+recordid+"','"+product+"','"+subproductid+"','"+glkey+
		 * "','"+subglcode+"')"; enctrace("insertqry : " + insertqry ); x =
		 * jdbctemplate.update(insertqry);
		 */

		// by gowtham-210819
		String insertqry = "INSERT INTO IFP_GL_KEYS_MAPPING (INST_ID,RECORD_ID, PRODUCT_CODE, SUBPRODUCT_ID,GL_TYPE, GL_CODE ) VALUES (?,?,?,?,?,?)";
		enctrace("insertqry : " + insertqry);
		x = jdbctemplate.update(insertqry, new Object[] { instid, recordid, product, subproductid, glkey, subglcode });

		return x;
	}

	public List getGlKeys(String instid, JdbcTemplate jdbctemplate) throws Exception {
		List glkeys = null;

		/*
		 * String glkeysqry =
		 * "SELECT TXNKEY, TXNKEYDESC FROM IFP_GL_KEYS WHERE STATUS='1'";
		 * enctrace("glkeysqry : "+glkeysqry); glkeys =
		 * jdbctemplate.queryForList(glkeysqry);
		 */

		// by gowtham-210819
		String glkeysqry = "SELECT TXNKEY, TXNKEYDESC FROM IFP_GL_KEYS WHERE STATUS=?";
		enctrace("glkeysqry : " + glkeysqry);
		glkeys = jdbctemplate.queryForList(glkeysqry, new Object[] { "1" });

		return glkeys;
	}

	public List getSubProdMappedGl(String instid, String productcode, String subproduct, JdbcTemplate jdbctemplate)
			throws Exception {
		List mappedgl = null;

		/*
		 * String mappedglqry =
		 * "SELECT RECORD_ID,GL_TYPE, GL_CODE FROM IFP_GL_KEYS_MAPPING WHERE INST_ID='"
		 * +instid+"' AND PRODUCT_CODE='"+productcode+"' AND SUBPRODUCT_ID='"
		 * +subproduct+"' "; enctrace("mappedglqry :" + mappedglqry ); mappedgl
		 * = jdbctemplate.queryForList(mappedglqry);
		 */

		// by gowtham-210819
		String mappedglqry = "SELECT RECORD_ID,GL_TYPE, GL_CODE FROM IFP_GL_KEYS_MAPPING WHERE INST_ID=? AND PRODUCT_CODE=? AND SUBPRODUCT_ID=? ";
		enctrace("mappedglqry :" + mappedglqry);
		mappedgl = jdbctemplate.queryForList(mappedglqry, new Object[] { instid, productcode, subproduct });

		return mappedgl;
	}

	public String getKeyDesc(String glkey, JdbcTemplate jdbctemplate) throws Exception {
		String glkeydesc = null;

		/*
		 * String glkeydescqry =
		 * "SELECT TXNKEYDESC FROM IFP_GL_KEYS WHERE TXNKEY='"+glkey+"'";
		 * glkeydesc =(String) jdbctemplate.queryForObject(glkeydescqry,
		 * String.class);
		 */

		// by gowtham-210819
		String glkeydescqry = "SELECT TXNKEYDESC FROM IFP_GL_KEYS WHERE TXNKEY=?";
		glkeydesc = (String) jdbctemplate.queryForObject(glkeydescqry, new Object[] { glkey }, String.class);

		return glkeydesc;
	}

	public List getCurCode(String bin, String i_Name, JdbcTemplate jdbctemplate) throws Exception {
		List curcode = null;
		String selectbinccy_query = "select CUR_CODE from ifp_bin_currency where  bin='" + bin + "' and INST_ID='"
				+ i_Name + "'";
		trace("selectbinccy_query-->" + selectbinccy_query);
		curcode = jdbctemplate.queryForList(selectbinccy_query);
		return curcode;
	}

	public String getCurcodeWithoutBin(String instid, JdbcTemplate jdbctemplate) {
		String curcode = null;
		curcode = "select CUR_CODE from IFP_BIN_CURRENCY where INST_ID='" + instid + "'";
		return curcode;
	}

	public List getbaseCurrency(String i_Name, JdbcTemplate jdbctemplate) throws Exception {
		List base_cur_list;
		String selct_inst = "select BASE_CURRENCY from INSTITUTION where inst_id='" + i_Name + "'";
		base_cur_list = jdbctemplate.queryForList(selct_inst);
		return base_cur_list;
	}

	public String getGLcode(String instid, String prodcode, JdbcTemplate jdbctemplate) throws Exception {

		/*
		 * String glcodeqry =
		 * "SELECT GL_CODE FROM PRODUCT_MASTER WHERE INST_ID='"+instid+
		 * "' AND PRODUCT_CODE='"+prodcode+"' and rownum<=1";
		 * System.out.println("glcodeqry==>" +glcodeqry); String glcode =
		 * (String)jdbctemplate.queryForObject(glcodeqry,String.class);
		 */

		// by gowtham-210819
		String glcodeqry = "SELECT GL_CODE FROM PRODUCT_MASTER WHERE INST_ID=? AND PRODUCT_CODE=? and rownum<=?";
		System.out.println("glcodeqry==>" + glcodeqry);
		String glcode = (String) jdbctemplate.queryForObject(glcodeqry, new Object[] { instid, prodcode, "1" },
				String.class);

		return glcode;
	}

	public String getLimitDetails(String instid, JdbcTemplate jdbctemplate) {
		String limitcodeqry = null;
		limitcodeqry = "SELECT LIMIT_ID, LIMIT_DESC FROM LIMIT_DESC WHERE INST_ID='" + instid + "' AND AUTH_STATUS='1'";
		return limitcodeqry;
	}

	public String getInstProdDetails(String instid, String product_code, String sub_prod_id,
			JdbcTemplate jdbctemplate) {
		String prod_det_query = null;
		prod_det_query = "select * from INSTPROD_DETAILS where INST_ID='" + instid + "' and PRODUCT_CODE='"
				+ product_code + "' AND SUB_PROD_ID='" + sub_prod_id + "'";
		return prod_det_query;
	}

	public List getStaticCardTypeList(String instid, JdbcTemplate jdbctemplate) throws Exception {
		List cardtypelist = null;
		String cardtypelistqry = "SELECT CARD_TYPE_KEY, CARD_TYPE_NAME FROM IFD_GLOBAL_STATIC_CARD_TYPE WHERE INST_ID='"
				+ instid + "' ORDER BY PREFERENCE";
		enctrace("cardtypelistqry : " + cardtypelistqry);
		cardtypelist = jdbctemplate.queryForList(cardtypelistqry);
		return cardtypelist;
	}

	public int updateDeAuthStatus(String instid, String productid, String subproductid, String usercode, String remarks,
			JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		Date date = new Date();
		/*
		 * String updstatusqry =
		 * "UPDATE INSTPROD_DETAILS SET AUTH_STATUS='9', AUTH_BY='"+usercode+
		 * "', AUTH_DATE=sysdate,REMARKS='"+remarks+"' WHERE INST_ID='"+instid+
		 * "' AND PRODUCT_CODE='"+productid+"' AND SUB_PROD_ID='"
		 * +subproductid+"'"; enctrace("updstatusqry : " + updstatusqry ); x =
		 * jdbctemplate.update(updstatusqry);
		 */

		// by gowtham-210819
		String updstatusqry = "UPDATE INSTPROD_DETAILS SET AUTH_STATUS=?, AUTH_BY=?, AUTH_DATE=?,REMARKS=? WHERE INST_ID=? AND PRODUCT_CODE=? AND SUB_PROD_ID=?";
		enctrace("updstatusqry : " + updstatusqry);
		x = jdbctemplate.update(updstatusqry,
				new Object[] { "9", usercode, date.getCurrentDate(), remarks, instid, productid, subproductid });

		return x;
	}

	public int updateAuthStatus(String instid, String productid, String subproductid, String usercode,
			JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		Date date = new Date();
		/*
		 * String updstatusqry =
		 * "UPDATE INSTPROD_DETAILS SET AUTH_STATUS='1', AUTH_BY='"+usercode+
		 * "', AUTH_DATE=sysdate  WHERE INST_ID='"+instid+"' AND PRODUCT_CODE='"
		 * +productid+"' AND SUB_PROD_ID='"+subproductid+"'"; enctrace(
		 * "updstatusqry : " + updstatusqry ); x =
		 * jdbctemplate.update(updstatusqry);
		 */

		// by gowtham-210819
		String updstatusqry = "UPDATE INSTPROD_DETAILS SET AUTH_STATUS=?, AUTH_BY=?, AUTH_DATE=?  WHERE INST_ID=? AND PRODUCT_CODE=? AND SUB_PROD_ID=?";
		enctrace("updstatusqry : " + updstatusqry);
		x = jdbctemplate.update(updstatusqry,
				new Object[] { "1", usercode, date.getCurrentDate(), instid, productid, subproductid });

		return x;
	}

	public int getAuthexistsubprodList(String instid, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String del_qury = "SELECT COUNT(*) FROM  INSTPROD_DETAILS  where INST_ID='" + instid + "' and AUTH_STATUS='0'";
		enctrace("del_qury : " + del_qury);
		x = jdbctemplate.queryForInt(del_qury);
		return x;
	}

	public int getmaintablestatus(String instid, String prodcode, String subprodcode, JdbcTemplate jdbctemplate) {
		String count_main_qury = "SELECT COUNT(*) FROM INSTPROD_DETAILS WHERE PRODUCT_CODE='" + prodcode
				+ "' AND SUB_PROD_ID='" + subprodcode + "' AND INST_ID='" + instid + "'";
		System.out.println("--- count_main_qury --- " + count_main_qury);
		int cntinst = jdbctemplate.queryForInt(count_main_qury);
		return cntinst;
	}

	/*
	 * public String updateSubProductInTempTable(String instid, String
	 * productCode,String subProductId ,String subProductName ,String feeCode
	 * ,String limitCode ,String serviceCode ,String expiryDate ,String
	 * personalizedCard ,String pinRequired ,String maintainRequired,String
	 * usercode,String authCode){
	 */
	public String updateSubProductInTempTable(String instid, String productCode, String subProductId,
			String subProductName, String feeCode, String limitCode, String serviceCode, String expiryDate,
			String personalizedCard, String pinRequired, String maintainRequired, String usercode, String authcode) {
		/*
		 * String query_currency =
		 * "update INSTPROD_DETAILS set SUB_PRODUCT_NAME='"
		 * +subProductName+"',PERSONALIZED='"+personalizedCard+
		 * "',EXPIRY_PERIOD='"+expiryDate+"',SERVICE_CODE='"+serviceCode+
		 * "',FEE_CODE='"+feeCode+"',LIMIT_ID='"+limitCode+"',PIN_GEN_REQ='"+
		 * pinRequired+"',MAINTAIN_REQ='"+maintainRequired+"',AUTH_STATUS='"+
		 * authcode+"',CONFIG_BY='"+usercode+
		 * "',CONFIG_DATE=sysdate  WHERE PRODUCT_CODE='"+productCode+
		 * "' AND SUB_PROD_ID='"+subProductId+"' AND INST_ID='"+instid+"'";
		 */
		String query_currency = "update INSTPROD_DETAILS set SUB_PRODUCT_NAME=?,PERSONALIZED=?,EXPIRY_PERIOD=?,SERVICE_CODE=?,FEE_CODE=?,LIMIT_ID=?,PIN_GEN_REQ=?,MAINTAIN_REQ=?,AUTH_STATUS=?,CONFIG_BY=?,CONFIG_DATE=?  WHERE PRODUCT_CODE=? AND SUB_PROD_ID=? AND INST_ID=?";
		trace("query_currency  == " + query_currency);
		return query_currency;
	}

	public String updateSubProductInMainTable(String instid, String productCode, String subProductId,
			String subProductName, String feeCode, String limitCode, String serviceCode, String expiryDate,
			String personalizedCard, String pinRequired, String maintainRequired, String usercode, String authcode) {
		/*
		 * public String updateSubProductInMainTable(String instid, String
		 * productCode,String subProductId ,String subProductName ,String
		 * feeCode ,String limitCode ,String serviceCode ,String expiryDate
		 * ,String personalizedCard ,String pinRequired ,String
		 * maintainRequired,String usercode,String authCode){
		 */
		/*
		 * String query_currency =
		 * "update INSTPROD_DETAILS set SUB_PRODUCT_NAME='"
		 * +subProductName+"',PERSONALIZED='"+personalizedCard+
		 * "',EXPIRY_PERIOD='"+expiryDate+"',SERVICE_CODE='"+serviceCode+
		 * "',FEE_CODE='"+feeCode+"',LIMIT_ID='"+limitCode+"',PIN_GEN_REQ='"+
		 * pinRequired+"',MAINTAIN_REQ='"+maintainRequired+"',AUTH_STATUS='"+
		 * authCode+"',CONFIG_BY='"+usercode+
		 * "',CONFIG_DATE=sysdate  WHERE PRODUCT_CODE='"+productCode+
		 * "' AND SUB_PROD_ID='"+subProductId+"' AND INST_ID='"+instid+"'";
		 */
		String query_currency = "update INSTPROD_DETAILS set SUB_PRODUCT_NAME='" + subProductName + "',PERSONALIZED='"
				+ personalizedCard + "',EXPIRY_PERIOD='" + expiryDate + "',SERVICE_CODE='" + serviceCode
				+ "',FEE_CODE='" + feeCode + "',LIMIT_ID='" + limitCode + "',PIN_GEN_REQ='" + pinRequired
				+ "',MAINTAIN_REQ='" + maintainRequired + "',AUTH_STATUS='" + authcode + "',CONFIG_BY='" + usercode
				+ "',CONFIG_DATE=sysdate  WHERE PRODUCT_CODE='" + productCode + "' AND SUB_PROD_ID='" + subProductId
				+ "' AND INST_ID='" + instid + "'";
		trace("query_currency  == " + query_currency);
		return query_currency;
	}

	public String getProductdetInsertstatus(String instid, String productCode, String subProductId,
			String subProductName, String feeCode, String limitCode, String serviceCode, String expiryDate,
			String personalizedCard, String currencymulti, String pinRequired, String maintainRequired, String usercode,
			String auth_code, String mkchkrstatus) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO INSTPROD_DETAILS ( ");
		sql.append(
				"INST_ID, PRODUCT_CODE, SUB_PROD_ID, SUB_PRODUCT_NAME, FEE_CODE, LIMIT_ID, SERVICE_CODE, EXPIRY_PERIOD, PERSONALIZED, CARD_CCY,PIN_GEN_REQ, MAINTAIN_REQ, AUTH_STATUS, CONFIG_BY, CONFIG_DATE, AUTH_BY, AUTH_DATE, REMARKS, DELETED_FLAG) ");
		sql.append("VALUES ( ");

		// by gowtham-210819
		// sql.append("'"+instid+"','"+productCode+"','"+subProductId+"','"+subProductName+"','"+feeCode+"','"+limitCode+"','"+serviceCode+"','"+expiryDate+"','"+personalizedCard+"','"+currencymulti+"','"+pinRequired+"','"+maintainRequired+"','"+auth_code+"','"+usercode+"',
		// sysdate,'','','','0') ");
		sql.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
		enctrace("Subproduct qry : " + sql.toString());
		return sql.toString();
	}

	public String inserttempname(String instid, String productCode, String subProductId) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO INST_TEMPNAME ( ");
		sql.append("INST_ID, PRODUCT_CODE,SUBPRODUCT,EMBNAME,ENCNAME) ");
		sql.append("VALUES ( ");

		// by gowtham-210819
		// sql.append("'"+instid+"','"+productCode+"','"+subProductId+"','INSTANT
		// CARD','INSTANT CARD') ");
		sql.append("?,?,?,?,? ) ");

		enctrace("Subproduct qry : " + sql.toString());
		return sql.toString();
	}

	public String getProductdetTempInsertstatus(String instid, String productCode, String subProductId,
			String subProductName, String feeCode, String limitCode, String serviceCode, String expiryDate,
			String personalizedCard, String currencymulti, String pinRequired, String maintainRequired, String usercode,
			String auth_code, String mkchkrstatus) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO INSTPROD_DETAILS ( ");
		sql.append(
				"INST_ID, PRODUCT_CODE, SUB_PROD_ID, SUB_PRODUCT_NAME, FEE_CODE, LIMIT_ID, SERVICE_CODE, EXPIRY_PERIOD, PERSONALIZED,CARD_CCY, PIN_GEN_REQ, MAINTAIN_REQ, AUTH_STATUS, CONFIG_BY, CONFIG_DATE, AUTH_BY, AUTH_DATE, REMARKS, DELETED_FLAG) ");
		sql.append("VALUES ( ");

		// by gowtha,-210819
		// sql.append("'"+instid+"','"+productCode+"','"+subProductId+"','"+subProductName+"','"+feeCode+"','"+limitCode+"','"+serviceCode+"','"+expiryDate+"','"+personalizedCard+"','"+currencymulti+"','"+pinRequired+"','"+maintainRequired+"','"+auth_code+"','"+usercode+"',
		// sysdate,'','','','0' ) ");
		sql.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?,?,?,?,? ) ");
		enctrace("Subproduct qry : " + sql.toString());
		return sql.toString();
	}

	public String movetomaintable(String productcode, String subproductid, String i_Name, JdbcTemplate jdbctemplate) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO INSTPROD_DETAILS ");
		sql.append(
				"(INST_ID, PRODUCT_CODE, SUB_PROD_ID, SUB_PRODUCT_NAME, FEE_CODE, LIMIT_ID, SERVICE_CODE, EXPIRY_PERIOD, PERSONALIZED,CARD_CCY,PIN_GEN_REQ, MAINTAIN_REQ, AUTH_STATUS, CONFIG_BY, CONFIG_DATE, AUTH_BY, AUTH_DATE, REMARKS, DELETED_FLAG) ");
		sql.append("SELECT  ");
		sql.append(
				"INST_ID, PRODUCT_CODE, SUB_PROD_ID, SUB_PRODUCT_NAME, FEE_CODE, LIMIT_ID, SERVICE_CODE, EXPIRY_PERIOD, PERSONALIZED,CARD_CCY, PIN_GEN_REQ, MAINTAIN_REQ, AUTH_STATUS, CONFIG_BY, CONFIG_DATE, AUTH_BY, AUTH_DATE, REMARKS, DELETED_FLAG ");
		sql.append("FROM ");
		sql.append("INSTPROD_DETAILS WHERE INST_ID='" + i_Name + "' AND PRODUCT_CODE = '" + productcode
				+ "' AND SUB_PROD_ID='" + subproductid + "'");
		enctrace("Subproduct qry : " + sql.toString());
		return sql.toString();
	}

	public String getsubproddetails(String productid, String subproductid, String instid, JdbcTemplate jdbctemplate) {

		String subproddetails_qry = "SELECT * FROM INSTPROD_DETAILS WHERE PRODUCT_CODE='" + productid
				+ "' AND  SUB_PROD_ID='" + subproductid + "' AND INST_ID='" + instid + "'";

		// String subproddetails_qry = "SELECT * FROM INSTPROD_DETAILS WHERE
		// PRODUCT_CODE=? AND SUB_PROD_ID=? AND INST_ID=?";

		enctrace("subproddetails_qry --- " + subproddetails_qry);
		System.out.println("subproddetails_qry ---- " + subproddetails_qry);
		return subproddetails_qry;
	}

	public String getkycdetailsdetails(String productid, String subproductid, String instid,
			JdbcTemplate jdbctemplate) {
		String subproddetails_qry = "SELECT * FROM ifp_kyc_limit WHERE PRODUCT_CODE='" + productid
				+ "' AND  SUB_PROD_ID='" + subproductid + "' AND INST_ID='" + instid + "'";
		enctrace("subproddetails_qry --- " + subproddetails_qry);
		System.out.println("subproddetails_qry ---- " + subproddetails_qry);
		return subproddetails_qry;
	}

	public String deleteSubProductInMainTable(String instid, String productcode, String subproductid,
			JdbcTemplate jdbctemplate) {

		/*
		 * String delete_qry="DELETE FROM INSTPROD_DETAILS WHERE INST_ID='"
		 * +instid+"' AND PRODUCT_CODE='"+productcode+"' AND SUB_PROD_ID='"
		 * +subproductid+"'";
		 */
		String delete_qry = "DELETE FROM INSTPROD_DETAILS WHERE INST_ID=? AND PRODUCT_CODE=? AND SUB_PROD_ID=?";

		enctrace("delete_qry :" + delete_qry);

		return delete_qry;
	}

	public int updateDeletedStatus(String instid, String deletedflag, String prodcode, String subprodcode,
			String authcode, String usercode, String remarks, JdbcTemplate jdbctemplate) {
		int x = -1;
		Date date = new Date();
		/*
		 * String deleteqrystatus =
		 * "update INSTPROD_DETAILS set  DELETED_FLAG='"
		 * +deletedflag+"',AUTH_STATUS='"+authcode+"', CONFIG_BY='"+usercode+
		 * "', CONFIG_DATE=SYSDATE, REMARKS='"+remarks+"'  WHERE INST_ID='"
		 * +instid+"' AND PRODUCT_CODE='"+prodcode+"' AND SUB_PROD_ID='"
		 * +subprodcode+"'"; enctrace("deleteqrystatus : "+deleteqrystatus); x =
		 * jdbctemplate.update(deleteqrystatus);
		 */

		// by gowtham-210819
		String deleteqrystatus = "update INSTPROD_DETAILS set  DELETED_FLAG=?,AUTH_STATUS=?, CONFIG_BY=?, CONFIG_DATE=?, REMARKS=?  WHERE INST_ID=? AND PRODUCT_CODE=? AND SUB_PROD_ID=?";
		enctrace("deleteqrystatus : " + deleteqrystatus);
		x = jdbctemplate.update(deleteqrystatus,
				new Object[] { deletedflag, authcode, date.getCurrentDate(), remarks, instid, prodcode, subprodcode });

		return x;
	}

	public int updateDeletedAuthStatus(String instid, String deletedflag, String prodcode, String subprodcode,
			String authcode, String usercode, String remarks, JdbcTemplate jdbctemplate) {
		int x = -1;

		/*
		 * String deleteqrystatus =
		 * "update INSTPROD_DETAILS set  DELETED_FLAG='"+deletedflag+
		 * "', AUTH_BY='"+usercode+"',AUTH_STATUS='"+authcode+
		 * "', AUTH_DATE=SYSDATE, REMARKS='"+remarks+"'  WHERE INST_ID='"
		 * +instid+"' AND PRODUCT_CODE='"+prodcode+"' AND SUB_PROD_ID='"
		 * +subprodcode+"'"; enctrace("deleteqrystatus : "+deleteqrystatus); x =
		 * jdbctemplate.update(deleteqrystatus);
		 * 
		 */

		// by gowtham-210819
		String deleteqrystatus = "update INSTPROD_DETAILS set  DELETED_FLAG=?, AUTH_BY=?,AUTH_STATUS=?, AUTH_DATE=?, REMARKS=?  WHERE INST_ID=? AND PRODUCT_CODE=? AND SUB_PROD_ID=? ";
		enctrace("deleteqrystatus : " + deleteqrystatus);
		x = jdbctemplate.update(deleteqrystatus, new Object[] { deletedflag, usercode, authcode, Date.getCurrentDate(),
				remarks, instid, prodcode, subprodcode });

		return x;
	}

	public List gettingBinList(String instid, JdbcTemplate jdbctemplate) {
		List res = null;
		try {

			/*
			 * String qry =
			 * "SELECT BIN,BASELEN FROM PRODUCTINFO WHERE INST_ID='"+instid+
			 * "' AND ATTACH_PRODTYPE_CARDTYPE='P'"; enctrace(
			 * "Query for binlist ::: "+qry); res =
			 * jdbctemplate.queryForList(qry);
			 */

			// by gowtham-210819
			String qry = "SELECT PRD_CODE,BASE_NO_LEN FROM PRODUCTINFO WHERE INST_ID=?";
			enctrace("Query for binlist ::: " + qry);
			res = jdbctemplate.queryForList(qry, new Object[] { instid, "p" });

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	/*
	 * public String getQueryScheme(String instid,JdbcTemplate jdbctemplate) {
	 * String query_scheme =null; query_scheme=
	 * "select FEE_CODE,FEE_DESC from FEE_DESC where INST_ID='"+instid+
	 * "' and AUTH_CODE='1'"; return query_scheme; } public String
	 * getProductMasterDetails(String instid,JdbcTemplate jdbctemplate){ String
	 * productMasterDetails; productMasterDetails =
	 * "SELECT Convert(nvarchar(50),PRODUCT_CODE)+'-'+Convert(nvarchar(50),BIN) as PRODUCT_CODE, CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID='"
	 * +instid+"'"; return productMasterDetails; } public String
	 * getQueryCurrency1(String instid,JdbcTemplate jdbctemplate) { String
	 * query_currency = null; query_currency=
	 * "SELECT NUMERIC_CODE as CUR_CODE,CURRENCY_DESC as CURRENCY_DESC FROM GLOBAL_CURRENCY "
	 * ; return query_currency; }
	 * 
	 * public String getQueryCurrency(String instid,JdbcTemplate jdbctemplate) {
	 * String query_currency = null; query_currency=
	 * "SELECT p.CUR_CODE,h.CURRENCY_DESC FROM GLOBAL_CURRENCY h JOIN INSTITUTION_CURRENCY p ON (h.NUMERIC_CODE=p.CUR_CODE  and p.INST_ID  ='"
	 * +instid+"')"; return query_currency; } public String
	 * getServiceCodelist(String instid,String productid,String
	 * subproductid,JdbcTemplate jdbctemplate){ String servicecodeqry =
	 * "SELECT SERVICE_CODE FROM INSTPROD_DETAILS WHERE INST_ID='"+instid+
	 * "'AND  PRODUCT_CODE='"+productid+"' AND SUB_PROD_ID='"+subproductid+
	 * "'  AND AUTH_STATUS='1'"; return servicecodeqry; } public String
	 * getLimitExistlist(String instid,JdbcTemplate jdbctemplate) { String
	 * limitcodeqry =
	 * "SELECT LIMIT_ID, LIMIT_DESC FROM LIMIT_DESC WHERE INST_ID='"+instid+
	 * "' AND AUTH_STATUS='1' AND LIMIT_BASE='P'"; trace( "limitcodeqry__" +
	 * limitcodeqry ); return limitcodeqry; } public String
	 * getCardtypeProductlist(String instid,JdbcTemplate jdbctemplate) { String
	 * prodlist =
	 * "SELECT PRODUCT_CODE, CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID='"
	 * +instid+"' and AUTH_CODE='1' AND DELETED_FLAG !='2' "; trace(
	 * "prodlist --- "+prodlist);System.out.println("prodlist ---- "+prodlist);
	 * return prodlist; } public List getSuproductSelectResult(String[]
	 * prodbin,String i_Name,JdbcTemplate jdbctemplate) { List
	 * suproduct_select_result= null; String suproduct_select =
	 * "select SUB_PROD_ID,SUB_PROD_DESC from IFPRODUCT_SUBTYPE where CARD_TYPE_ID='"
	 * +prodbin[0]+
	 * "' and SUB_PROD_ID not in (select SUB_PROD_ID from INSTPROD_DETAILS where CARD_TYPE_ID='"
	 * +prodbin[0]+"' and bin='"+prodbin[1]+"' and INST_ID='"+i_Name+"')";
	 * //String qury=
	 * "select PRODUCT_CODE,PRODUCT_DESC from IFPRODUCT_SUBTYPE where PRODUCT_ID='"
	 * +prodid+"'"; System.out.println("The Result is  " + suproduct_select);
	 * 
	 * suproduct_select_result =jdbctemplate.queryForList(suproduct_select);
	 * return suproduct_select_result; }
	 * 
	 * 
	 * 
	 * public List getViewProdresult(String i_Name,JdbcTemplate jdbctemplate) {
	 * trace("getViewProdresult started"); List View_prod_result= null; String
	 * viewProduct_details = "select * from PRODUCTINFO where inst_id='"
	 * +i_Name+
	 * "' and bin in (select DISTINCT(bin) from INSTPROD_DETAILS where inst_id='"
	 * +i_Name+"' ) order by BIN " ;
	 * trace("viewProduct_details--->"+viewProduct_details); enctrace(
	 * "subprd query for direct display "+viewProduct_details); View_prod_result
	 * =jdbctemplate.queryForList(viewProduct_details);
	 * trace("View_prod_result--->"+View_prod_result); trace(
	 * "getViewProdresult started"); return View_prod_result; } public String
	 * getAuthSubqry (String productcode,String subproductid,JdbcTemplate
	 * jdbctemplate) { String authstatus= null; String authsubqry =
	 * "SELECT AUTH_STATUS FROM INSTPROD_DETAILS WHERE PRODUCT_CODE='"
	 * +productcode+"' AND SUB_PROD_ID='"+subproductid+"'"; enctrace(
	 * "authsubqry --- "+authsubqry); authstatus =
	 * (String)jdbctemplate.queryForObject(authsubqry, String.class); return
	 * authstatus; }
	 * 
	 * public List getViewProdResult(String i_Name,JdbcTemplate jdbctemplate) {
	 * List View_prod_result = null; String viewProduct_details =
	 * "select * from PRODUCTINFO where inst_id='"+i_Name+
	 * "' and bin in (select DISTINCT(bin) from INSTPROD_DETAILS where inst_id='"
	 * +i_Name+"' ) order by BIN " ; enctrace("subprd query for direct display "
	 * +viewProduct_details); View_prod_result
	 * =jdbctemplate.queryForList(viewProduct_details); return View_prod_result;
	 * 
	 * } public String getProductinfoList(String inst_Name,String
	 * productname,String subproduct,JdbcTemplate jdbctemplate) { StringBuilder
	 * product_query = new StringBuilder(); product_query.append("SELECT ");
	 * product_query.append(
	 * "INST_ID, PRODUCT_CODE, SUB_PROD_ID, SUB_PRODUCT_NAME, FEE_CODE, LIMIT_ID, SERVICE_CODE, EXPIRY_PERIOD, DECODE(PERSONALIZED,1,'ONLY PERSONALIZED','0','ONLY INSTANT','2','INSTANT AND PERSONALIZED') PERSONALIZED, "
	 * ); product_query.append(
	 * "DECODE(PIN_GEN_REQ,'Y','YES','N','NO') PIN_GEN_REQ, DECODE(MAINTAIN_REQ,'Y','YES','N','NO') MAINTAIN_REQ,  "
	 * ); product_query.append(
	 * "DECODE(AUTH_STATUS,'0','Waiting for authorize','1','Authorized','9','Deauthorized') AUTH_STATUS, CONFIG_BY,  "
	 * ); product_query.append(
	 * "to_char(CONFIG_DATE,'DD-MON-YYYY') CONFIG_DATE, AUTH_BY, AUTH_DATE, REMARKS "
	 * ); product_query.append("FROM "); product_query.append(
	 * "INSTPROD_DETAILS "); product_query.append("where INST_ID='"+inst_Name+
	 * "' and PRODUCT_CODE='"+productname+"' and SUB_PROD_ID='"+subproduct+"'");
	 * enctrace("product_query---> "+product_query.toString()); return
	 * product_query.toString(); } public String getFeedescList(String
	 * instid,String code,JdbcTemplate jdbctemplate) { String qury_str =
	 * "SELECT FEE_DESC FROM FEE_DESC where INST_ID='"+instid+"' AND FEE_CODE='"
	 * +code+"'"; return qury_str; } public String getLimitdescList(String
	 * instid,String code,JdbcTemplate jdbctemplate) { String qury_str =
	 * "SELECT LIMIT_DESC FROM LIMIT_DESC where INST_ID='"+instid+
	 * "' AND LIMIT_ID='"+code+"'"; return qury_str; } public String
	 * getSchemedescList(String instid,String code,JdbcTemplate jdbctemplate) {
	 * String qury_str =
	 * "SELECT SCH_NAME FROM IFP_GL_SCHEME_MASTER where INST_ID='"+instid+
	 * "' AND SCH_CODE='"+code+"'"; return qury_str;
	 * 
	 * } public String getProductdesc( String instid, String
	 * prodcode,JdbcTemplate jdbctemplate ) { String qryproductdesc =
	 * "SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE INST_ID='"+instid+
	 * "' and PRODUCT_CODE='"+prodcode+"'"; enctrace( " getProductdesc " +
	 * qryproductdesc);
	 * 
	 * if ( jdbctemplate.getMaxRows() == 0 ) { String bin_desc =
	 * (String)jdbctemplate.queryForObject(qryproductdesc, String.class); return
	 * bin_desc; }else{ return "UNKNOWN PRODUCT"; } } public List
	 * getProdDetResult(String inst_Name,String product_code,JdbcTemplate
	 * jdbctemplate) { List prod_det_result = null; String prod_det_query=
	 * "select * from INSTPROD_DETAILS where INST_ID='"+inst_Name+
	 * "' and PRODUCT_CODE='"+product_code+"'"; enctrace(
	 * "QUERY generateSubprodlist "+prod_det_query); prod_det_result
	 * =jdbctemplate.queryForList(prod_det_query); return prod_det_result; }
	 * public int getProdDelResult(String inst_Name,String
	 * product_code,JdbcTemplate jdbctemplate) { int prod_del_result = -1;
	 * String prod_det_query="delete from INSTPROD_DETAILS where INST_ID='"
	 * +inst_Name+"' and PRODUCT_CODE='"+product_code+"'"; System.out.println(
	 * "QUERY delete subproduct "+prod_det_query); prod_del_result
	 * =jdbctemplate.update(prod_det_query); return prod_del_result; } public
	 * List getGlschemeList(String instid,String glcode,JdbcTemplate
	 * jdbctemplate) throws Exception { List glschemelist= null; String
	 * glschemelistqry =
	 * "SELECT SCH_CODE, SCH_NAME  FROM IFP_GL_SCHEME_MASTER WHERE INST_ID='"
	 * +instid+"' AND GL_CODE='"+glcode+"' AND SCH_STATUS='1' ";
	 * System.out.println("glschemelistqry==> "+glschemelistqry); glschemelist =
	 * jdbctemplate.queryForList(glschemelistqry); return glschemelist; } public
	 * List getSelectList(JdbcTemplate jdbctemplate) { List selectlist = null;
	 * String mcclistqry =
	 * "SELECT MCC_CODE, MCC_DESC  FROM EZMMS_MCC_INFO WHERE ALLOWED='1'";
	 * System.out.println("sel_qury==>"+mcclistqry); selectlist
	 * =jdbctemplate.queryForList(mcclistqry); return selectlist; } public List
	 * getSelectList(String instid,JdbcTemplate jdbctemplate) { List selectlist
	 * = null; String networklistqry =
	 * "SELECT NETWORK_ID, NETWORK_DESC  FROM IFP_NETWORK_MASTER WHERE NETWORK_STATUS='1' AND INST_ID='"
	 * +instid+"'"; System.out.println("sel_qury==>"+networklistqry); selectlist
	 * =jdbctemplate.queryForList(networklistqry); return selectlist; } public
	 * List getSelectListMerchant(String instid,JdbcTemplate jdbctemplate) {
	 * List selectlistmerchant = null; String networklistqry =
	 * "SELECT MERCH_ID,MERCH_NAME  FROM EZMMS_MERCHANTPROFILE WHERE INST_ID='"
	 * +instid+"'"; System.out.println("sel_qury==>"+networklistqry);
	 * selectlistmerchant =jdbctemplate.queryForList(networklistqry); return
	 * selectlistmerchant; } public int deleteMappedRecord(String instid, String
	 * recordid,JdbcTemplate jdbctemplate) throws Exception { int x = -1; String
	 * delqry = "DELETE FROM IFP_GL_KEYS_MAPPING WHERE INST_ID='"+instid+
	 * "' AND RECORD_ID='"+recordid+"'"; enctrace("delqry  : " + delqry ); x =
	 * jdbctemplate.update(delqry ); return x; } public int
	 * insertSubGlMapping(String instid, String recordid, String product, String
	 * subproductid, String glkey, String subglcode, JdbcTemplate jdbctemplate )
	 * throws Exception{ int x = -1; String insertqry =
	 * "INSERT INTO IFP_GL_KEYS_MAPPING (INST_ID,RECORD_ID, PRODUCT_CODE, SUBPRODUCT_ID,GL_TYPE, GL_CODE ) VALUES ('"
	 * +instid+"','"+recordid+"','"+product+"','"+subproductid+"','"+glkey+"','"
	 * +subglcode+"')"; enctrace("insertqry : " + insertqry ); x =
	 * jdbctemplate.update(insertqry); return x; } public List getGlKeys(String
	 * instid, JdbcTemplate jdbctemplate) throws Exception { List glkeys = null;
	 * String glkeysqry =
	 * "SELECT TXNKEY, TXNKEYDESC FROM IFP_GL_KEYS WHERE STATUS='1'"; enctrace(
	 * "glkeysqry : "+glkeysqry); glkeys = jdbctemplate.queryForList(glkeysqry);
	 * return glkeys; }
	 * 
	 * public List getSubProdMappedGl(String instid, String productcode, String
	 * subproduct, JdbcTemplate jdbctemplate ) throws Exception { List mappedgl
	 * = null; String mappedglqry =
	 * "SELECT RECORD_ID,GL_TYPE, GL_CODE FROM IFP_GL_KEYS_MAPPING WHERE INST_ID='"
	 * +instid+"' AND PRODUCT_CODE='"+productcode+"' AND SUBPRODUCT_ID='"
	 * +subproduct+"' "; enctrace("mappedglqry :" + mappedglqry ); mappedgl =
	 * jdbctemplate.queryForList(mappedglqry); return mappedgl; }
	 * 
	 * public String getKeyDesc( String glkey, JdbcTemplate jdbctemplate )
	 * throws Exception { String glkeydesc = null; String glkeydescqry =
	 * "SELECT TXNKEYDESC FROM IFP_GL_KEYS WHERE TXNKEY='"+glkey+"'"; glkeydesc
	 * =(String) jdbctemplate.queryForObject(glkeydescqry, String.class); return
	 * glkeydesc; } public List getCurCode( String bin, String i_Name
	 * ,JdbcTemplate jdbctemplate) throws Exception { List curcode = null;
	 * String selectbinccy_query=
	 * "select CUR_CODE from ifp_bin_currency where  bin='"+bin+
	 * "' and INST_ID='"+i_Name+"'";
	 * trace("selectbinccy_query-->"+selectbinccy_query); curcode
	 * =jdbctemplate.queryForList(selectbinccy_query); return curcode; } public
	 * String getCurcodeWithoutBin(String instid,JdbcTemplate jdbctemplate){
	 * String curcode = null; curcode =
	 * "select CUR_CODE from IFP_BIN_CURRENCY where INST_ID='"+instid+"'";
	 * return curcode; } public List getbaseCurrency( String i_Name
	 * ,JdbcTemplate jdbctemplate ) throws Exception { List base_cur_list;
	 * String selct_inst="select BASE_CURRENCY from INSTITUTION where inst_id='"
	 * +i_Name+"'"; base_cur_list =jdbctemplate.queryForList(selct_inst); return
	 * base_cur_list; }
	 * 
	 * public String getGLcode( String instid ,String prodcode ,JdbcTemplate
	 * jdbctemplate ) throws Exception { String glcodeqry =
	 * "SELECT GL_CODE FROM PRODUCT_MASTER WHERE INST_ID='"+instid+
	 * "' AND PRODUCT_CODE='"+prodcode+"' and rownum<=1";
	 * System.out.println("glcodeqry==>" +glcodeqry); String glcode =
	 * (String)jdbctemplate.queryForObject(glcodeqry,String.class); return
	 * glcode; } public String getLimitDetails(String instid,JdbcTemplate
	 * jdbctemplate){ String limitcodeqry = null; limitcodeqry =
	 * "SELECT LIMIT_ID, LIMIT_DESC FROM LIMIT_DESC WHERE INST_ID='"+instid+
	 * "' AND AUTH_STATUS='1'"; return limitcodeqry; } public String
	 * getInstProdDetails(String instid,String product_code,String
	 * sub_prod_id,JdbcTemplate jdbctemplate){ String prod_det_query = null;
	 * prod_det_query="select * from INSTPROD_DETAILS where INST_ID='"+instid+
	 * "' and PRODUCT_CODE='"+product_code+"' AND SUB_PROD_ID='"
	 * +sub_prod_id+"'"; return prod_det_query; }
	 * 
	 * public List getStaticCardTypeList(String instid, JdbcTemplate
	 * jdbctemplate) throws Exception { List cardtypelist = null; String
	 * cardtypelistqry =
	 * "SELECT CARD_TYPE_KEY, CARD_TYPE_NAME FROM IFD_GLOBAL_STATIC_CARD_TYPE WHERE INST_ID='"
	 * +instid+"' ORDER BY PREFERENCE"; enctrace("cardtypelistqry : " +
	 * cardtypelistqry ); cardtypelist =
	 * jdbctemplate.queryForList(cardtypelistqry); return cardtypelist ; }
	 * 
	 * public int updateDeAuthStatus(String instid, String productid, String
	 * subproductid, String usercode,String remarks, JdbcTemplate jdbctemplate )
	 * throws Exception { int x = -1; String updstatusqry =
	 * "UPDATE INSTPROD_DETAILS SET AUTH_STATUS='9', AUTH_BY='"+usercode+
	 * "', AUTH_DATE=sysdate,REMARKS='"+remarks+"' WHERE INST_ID='"+instid+
	 * "' AND PRODUCT_CODE='"+productid+"' AND SUB_PROD_ID='"+subproductid+"'";
	 * enctrace("updstatusqry : " + updstatusqry ); x =
	 * jdbctemplate.update(updstatusqry); return x; } public int
	 * updateAuthStatus(String instid, String productid, String subproductid,
	 * String usercode, JdbcTemplate jdbctemplate ) throws Exception { int x =
	 * -1; String updstatusqry =
	 * "UPDATE INSTPROD_DETAILS SET AUTH_STATUS='1', AUTH_BY='"+usercode+
	 * "', AUTH_DATE=sysdate  WHERE INST_ID='"+instid+"' AND PRODUCT_CODE='"
	 * +productid+"' AND SUB_PROD_ID='"+subproductid+"'"; enctrace(
	 * "updstatusqry : " + updstatusqry ); x =
	 * jdbctemplate.update(updstatusqry); return x; } public int
	 * getAuthexistsubprodList(String instid,JdbcTemplate jdbctemplate) throws
	 * Exception { int x = -1; String del_qury=
	 * "SELECT COUNT(*) FROM  INSTPROD_DETAILS  where INST_ID='"+instid+
	 * "' and AUTH_STATUS='0'"; enctrace( "del_qury : " + del_qury ); x=
	 * jdbctemplate.queryForInt(del_qury); return x; }
	 * 
	 * public int getmaintablestatus(String instid,String prodcode,String
	 * subprodcode,JdbcTemplate jdbctemplate) { String count_main_qury =
	 * "SELECT COUNT(*) FROM INSTPROD_DETAILS WHERE PRODUCT_CODE='"+prodcode+
	 * "' AND SUB_PROD_ID='"+subprodcode+"' AND INST_ID='"+instid+"'";
	 * System.out.println("--- count_main_qury --- "+count_main_qury); int
	 * cntinst = jdbctemplate.queryForInt(count_main_qury); return cntinst; }
	 * public String updateSubProductInTempTable(String instid, String
	 * productCode,String subProductId ,String subProductName ,String feeCode
	 * ,String limitCode ,String serviceCode ,String expiryDate ,String
	 * personalizedCard ,String pinRequired ,String maintainRequired,String
	 * usercode,String authCode){ String query_currency =
	 * "update INSTPROD_DETAILS set SUB_PRODUCT_NAME='"
	 * +subProductName+"',PERSONALIZED='"+personalizedCard+"',EXPIRY_PERIOD='"+
	 * expiryDate+"',SERVICE_CODE='"+serviceCode+"',FEE_CODE='"+feeCode+
	 * "',LIMIT_ID='"+limitCode+"',PIN_GEN_REQ='"+pinRequired+"',MAINTAIN_REQ='"
	 * +maintainRequired+"',AUTH_STATUS='"+authCode+"',CONFIG_BY='"+usercode+
	 * "',CONFIG_DATE=sysdate  WHERE PRODUCT_CODE='"+productCode+
	 * "' AND SUB_PROD_ID='"+subProductId+"' AND INST_ID='"+instid+"'"; trace(
	 * "query_currency  == "+query_currency ); return query_currency ; } public
	 * String updateSubProductInMainTable(String instid, String
	 * productCode,String subProductId ,String subProductName ,String feeCode
	 * ,String limitCode ,String serviceCode ,String expiryDate ,String
	 * personalizedCard ,String pinRequired ,String maintainRequired,String
	 * usercode,String authCode){ String query_currency =
	 * "update INSTPROD_DETAILS set SUB_PRODUCT_NAME='"
	 * +subProductName+"',PERSONALIZED='"+personalizedCard+"',EXPIRY_PERIOD='"+
	 * expiryDate+"',SERVICE_CODE='"+serviceCode+"',FEE_CODE='"+feeCode+
	 * "',LIMIT_ID='"+limitCode+"',PIN_GEN_REQ='"+pinRequired+"',MAINTAIN_REQ='"
	 * +maintainRequired+"',AUTH_STATUS='"+authCode+"',CONFIG_BY='"+usercode+
	 * "',CONFIG_DATE=sysdate  WHERE PRODUCT_CODE='"+productCode+
	 * "' AND SUB_PROD_ID='"+subProductId+"' AND INST_ID='"+instid+"'"; trace(
	 * "query_currency  == "+query_currency ); return query_currency ; } public
	 * String getProductdetInsertstatus(String instid, String productCode,String
	 * subProductId ,String subProductName ,String feeCode ,String limitCode
	 * ,String serviceCode ,String expiryDate ,String personalizedCard ,String
	 * currencymulti,String pinRequired ,String maintainRequired,String
	 * usercode, String auth_code,String mkchkrstatus) { StringBuilder sql = new
	 * StringBuilder(); sql.append("INSERT INTO INSTPROD_DETAILS ( ");
	 * sql.append(
	 * "INST_ID, PRODUCT_CODE, SUB_PROD_ID, SUB_PRODUCT_NAME, FEE_CODE, LIMIT_ID, SERVICE_CODE, EXPIRY_PERIOD, PERSONALIZED, CARD_CCY,PIN_GEN_REQ, MAINTAIN_REQ, AUTH_STATUS, CONFIG_BY, CONFIG_DATE, AUTH_BY, AUTH_DATE, REMARKS, DELETED_FLAG) "
	 * ); sql.append("VALUES ( ");
	 * sql.append("'"+instid+"','"+productCode+"','"+subProductId+"','"+
	 * subProductName+"','"+feeCode+"','"+limitCode+"','"+serviceCode+"','"+
	 * expiryDate+"','"+personalizedCard+"','"+currencymulti+"','"+pinRequired+
	 * "','"+maintainRequired+"','"+auth_code+"','"+usercode+
	 * "', sysdate,'','','','0') ");
	 * 
	 * enctrace("Subproduct qry : " + sql.toString() ); return sql.toString(); }
	 * public String inserttempname(String instid, String productCode,String
	 * subProductId) { StringBuilder sql = new StringBuilder(); sql.append(
	 * "INSERT INTO INST_TEMPNAME ( "); sql.append(
	 * "INST_ID, PRODUCT_CODE,SUBPRODUCT,EMBNAME,ENCNAME) "); sql.append(
	 * "VALUES ( "); sql.append("'"+instid+"','"+productCode+"','"+subProductId+
	 * "','INSTANT CARD','INSTANT CARD') ");
	 * 
	 * enctrace("Subproduct qry : " + sql.toString() ); return sql.toString(); }
	 * public String getProductdetTempInsertstatus(String instid, String
	 * productCode,String subProductId ,String subProductName ,String feeCode
	 * ,String limitCode ,String serviceCode ,String expiryDate ,String
	 * personalizedCard ,String currencymulti,String pinRequired ,String
	 * maintainRequired,String usercode, String auth_code,String mkchkrstatus) {
	 * StringBuilder sql = new StringBuilder(); sql.append(
	 * "INSERT INTO INSTPROD_DETAILS ( "); sql.append(
	 * "INST_ID, PRODUCT_CODE, SUB_PROD_ID, SUB_PRODUCT_NAME, FEE_CODE, LIMIT_ID, SERVICE_CODE, EXPIRY_PERIOD, PERSONALIZED,CARD_CCY, PIN_GEN_REQ, MAINTAIN_REQ, AUTH_STATUS, CONFIG_BY, CONFIG_DATE, AUTH_BY, AUTH_DATE, REMARKS, DELETED_FLAG) "
	 * ); sql.append("VALUES ( ");
	 * sql.append("'"+instid+"','"+productCode+"','"+subProductId+"','"+
	 * subProductName+"','"+feeCode+"','"+limitCode+"','"+serviceCode+"','"+
	 * expiryDate+"','"+personalizedCard+"','"+currencymulti+"','"+pinRequired+
	 * "','"+maintainRequired+"','"+auth_code+"','"+usercode+
	 * "', sysdate,'','','','0' ) ");
	 * 
	 * enctrace("Subproduct qry : " + sql.toString() ); return sql.toString(); }
	 * public String movetomaintable(String productcode,String
	 * subproductid,String i_Name,JdbcTemplate jdbctemplate) { StringBuilder sql
	 * = new StringBuilder(); sql.append("INSERT INTO INSTPROD_DETAILS ");
	 * sql.append(
	 * "(INST_ID, PRODUCT_CODE, SUB_PROD_ID, SUB_PRODUCT_NAME, FEE_CODE, LIMIT_ID, SERVICE_CODE, EXPIRY_PERIOD, PERSONALIZED,CARD_CCY,PIN_GEN_REQ, MAINTAIN_REQ, AUTH_STATUS, CONFIG_BY, CONFIG_DATE, AUTH_BY, AUTH_DATE, REMARKS, DELETED_FLAG) "
	 * ); sql.append("SELECT  "); sql.append(
	 * "INST_ID, PRODUCT_CODE, SUB_PROD_ID, SUB_PRODUCT_NAME, FEE_CODE, LIMIT_ID, SERVICE_CODE, EXPIRY_PERIOD, PERSONALIZED,CARD_CCY, PIN_GEN_REQ, MAINTAIN_REQ, AUTH_STATUS, CONFIG_BY, CONFIG_DATE, AUTH_BY, AUTH_DATE, REMARKS, DELETED_FLAG "
	 * ); sql.append("FROM "); sql.append("INSTPROD_DETAILS WHERE INST_ID='"
	 * +i_Name+"' AND PRODUCT_CODE = '"+productcode+"' AND SUB_PROD_ID='"
	 * +subproductid+"'"); enctrace("Subproduct qry : " + sql.toString() );
	 * return sql.toString(); } public String getsubproddetails(String
	 * productid,String subproductid,String instid,JdbcTemplate jdbctemplate){
	 * String subproddetails_qry =
	 * "SELECT * FROM INSTPROD_DETAILS WHERE PRODUCT_CODE='"+productid+
	 * "' AND  SUB_PROD_ID='"+subproductid+"' AND INST_ID='"+instid+"'";
	 * enctrace("subproddetails_qry --- "
	 * +subproddetails_qry);System.out.println("subproddetails_qry ---- "
	 * +subproddetails_qry); return subproddetails_qry; } public String
	 * getkycdetailsdetails(String productid,String subproductid,String
	 * instid,JdbcTemplate jdbctemplate){ String subproddetails_qry =
	 * "SELECT * FROM ifp_kyc_limit WHERE PRODUCT_CODE='"+productid+
	 * "' AND  SUB_PROD_ID='"+subproductid+"' AND INST_ID='"+instid+"'";
	 * enctrace("subproddetails_qry --- "
	 * +subproddetails_qry);System.out.println("subproddetails_qry ---- "
	 * +subproddetails_qry); return subproddetails_qry; }
	 * 
	 * public String deleteSubProductInMainTable(String instid,String
	 * productcode,String subproductid,JdbcTemplate jdbctemplate){ String
	 * delete_qry="DELETE FROM INSTPROD_DETAILS WHERE INST_ID='"+instid+
	 * "' AND PRODUCT_CODE='"+productcode+"' AND SUB_PROD_ID='"
	 * +subproductid+"'"; enctrace("delete_qry :"+delete_qry); return
	 * delete_qry; }
	 * 
	 * 
	 * public int updateDeletedStatus(String instid, String deletedflag, String
	 * prodcode,String subprodcode, String authcode, String usercode, String
	 * remarks, JdbcTemplate jdbctemplate){ int x =-1; String deleteqrystatus =
	 * "update INSTPROD_DETAILS set  DELETED_FLAG='"
	 * +deletedflag+"',AUTH_STATUS='"+authcode+"', CONFIG_BY='"+usercode+
	 * "', CONFIG_DATE=SYSDATE, REMARKS='"+remarks+"'  WHERE INST_ID='"+instid+
	 * "' AND PRODUCT_CODE='"+prodcode+"' AND SUB_PROD_ID='"+subprodcode+"'";
	 * enctrace("deleteqrystatus : "+deleteqrystatus); x =
	 * jdbctemplate.update(deleteqrystatus); return x; }
	 * 
	 * public int updateDeletedAuthStatus(String instid, String deletedflag,
	 * String prodcode,String subprodcode, String authcode, String usercode,
	 * String remarks, JdbcTemplate jdbctemplate){ int x =-1; String
	 * deleteqrystatus = "update INSTPROD_DETAILS set  DELETED_FLAG='"
	 * +deletedflag+"', AUTH_BY='"+usercode+"',AUTH_STATUS='"+authcode+
	 * "', AUTH_DATE=SYSDATE, REMARKS='"+remarks+"'  WHERE INST_ID='"+instid+
	 * "' AND PRODUCT_CODE='"+prodcode+"' AND SUB_PROD_ID='"+subprodcode+"'";
	 * enctrace("deleteqrystatus : "+deleteqrystatus); x =
	 * jdbctemplate.update(deleteqrystatus); return x; }
	 * 
	 * 
	 * public List gettingBinList(String instid, JdbcTemplate jdbctemplate) {
	 * List res = null; try { String qry =
	 * "SELECT BIN,BASELEN FROM PRODUCTINFO WHERE INST_ID='"+instid+
	 * "' AND ATTACH_PRODTYPE_CARDTYPE='P'"; enctrace("Query for binlist ::: "
	 * +qry); res = jdbctemplate.queryForList(qry); } catch (Exception e) {
	 * e.printStackTrace(); } return res; }
	 */
}
