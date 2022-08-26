package com.ifd.Product;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;

import test.Date;

@Transactional
public class AddMainProductActionDao extends BaseAction{
	public String getGlobalCardtypeqry(String instid)
	{
		String globalcardtypeqry = null;
		globalcardtypeqry = "SELECT CARD_TYPE_ID, CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID='"+instid+"' AND AUTH_CODE='1'";
		return globalcardtypeqry;
	}
	
	
	public String getGlobalcardtpelist(String instid)
	{
		String globalcardtypeqry = null;
		
		//globalcardtypeqry = "SELECT CARD_TYPE_ID, CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID='"+instid+"' AND AUTH_CODE='1' ";
		
		//by gowtham210819
		globalcardtypeqry = "SELECT CARD_TYPE_ID, CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID=? AND AUTH_CODE=? ";
		
		return globalcardtypeqry;
	}
	/*public String getBinList(String instid)
	{
		String binlistqry = "SELECT BIN, BIN_DESC  FROM PRODUCTBIN_REL WHERE INST_ID='"+instid+"' ";
		return binlistqry;
	}*/

	public String getDeplymnt()
	{
		String qury = "select DEPLOYTYPE,DEPLOYID from DEPLOYMENT ";
		return qury;
	}    
	public String getFieldQury(String columname)
	{
		String field_qury = null;
		field_qury = "select count(*) from user_tab_cols where TABLE_NAME='BRANCH_MASTER' and COLUMN_NAME='"+columname+"'";
		return field_qury;
	}
	public String getAddfeild_qury(String columname) 
	{
	String addfeild_qury = null; 
	addfeild_qury = "alter table BRANCH_MASTER add(\""+columname+"\" varchar2(20) default '1')";
	return addfeild_qury;
	}
	public String getProductQuery(String instid, String prodseq, String productid, String productdesc, String prodsubtype, String bin, String usercode, String glcode,String auth_code,String mkchkrstatus)
	{
		String productquery=null;
		productquery="INSERT INTO PRODUCT_MASTER(INST_ID,PRODUCT_CODE, CARD_TYPE_ID,CARD_TYPE_NAME,SUB_PROD_CNT,BIN,CONFIGURED_BY,CONFIGURED_DATE,ACTIVE_FLAG, GL_CODE,AUTH_CODE,MAKER_ID,MAKER_DATE,MKCK_STATUS) VALUES ";
		
		//by gowtham210819
		  productquery += "('"+instid+"','"+prodseq+"','"+productid+"','"+productdesc+"','"+prodsubtype+"', '"+bin+"', '"+usercode+"', sysdate, '1', '"+glcode+"','"+auth_code+"','"+usercode+"',sysdate,'"+mkchkrstatus+"')";
		//productquery += "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		return productquery;
	}
	public int checkProductExist(String instid, String productseq,  JdbcTemplate jdbctemplate ){
		
		/*String productqry = "SELECT COUNT(*) FROM PRODUCT_MASTER WHERE INST_ID='"+instid+"' AND PRODUCT_CODE='"+productseq+"'";
		System.out.println("product__" + productqry );
		int x = jdbctemplate.queryForInt(productqry);*/

		//by gowtham210819
		String productqry = "SELECT COUNT(*) FROM PRODUCT_MASTER WHERE INST_ID=? AND PRODUCT_CODE=? ";
		System.out.println("product__" + productqry );
		int x = jdbctemplate.queryForInt(productqry,new Object[]{instid,productseq});
		return x;
		
	}
	public int checkCardtypeExist( String productcode, String instid,JdbcTemplate jdbctemplate ){
		
		/*String cardtypeqry = "SELECT COUNT(*) FROM PRODUCT_MASTER WHERE INST_ID='"+instid+"' AND CARD_TYPE_ID='"+productcode+"'";
		System.out.println("product__" + cardtypeqry );
		int x = jdbctemplate.queryForInt(cardtypeqry);*/
		
		//by gowtham-210819
		String cardtypeqry = "SELECT COUNT(*) FROM PRODUCT_MASTER WHERE INST_ID=? AND CARD_TYPE_ID=? ";
		System.out.println("product__" + cardtypeqry );
		int x = jdbctemplate.queryForInt(cardtypeqry,new Object[]{instid,productcode});
		
		return x;
	}

	public int checkBinExist( String bincode,String instid,JdbcTemplate jdbctemplate ){
		
		/*String binqry = "SELECT COUNT(*) FROM PRODUCT_MASTER WHERE INST_ID='"+instid+"' AND BIN='"+bincode+"'";
		enctrace("bin__" + binqry );
		int x = jdbctemplate.queryForInt(binqry);*/
		
		//by gowtham210819
		String binqry = "SELECT COUNT(*) FROM PRODUCT_MASTER WHERE INST_ID=? AND BIN=? ";
		enctrace("bin__" + binqry );
		int x = jdbctemplate.queryForInt(binqry,new Object[]{instid,bincode});
		
		return x;
	}
	public List fchBinMapping( String instid, JdbcTemplate jdbctemplate){
		String mappingqry = "SELECT BIN, CARD_TYPE FROM BIN_CARD_MAPPING WHERE INST_ID='"+instid+"'";
		List mappinglist = jdbctemplate.queryForList(mappingqry);
		return mappinglist;
	}
	
	public String getProduct(String instid ){
		String mappingqry = "select * from PRODUCT_MASTER where AUTH_CODE='1' and INST_ID='"+instid+"' AND DELETED_FLAG != '2' ";		
		return mappingqry;
	}
	
	public String getProductForView(String instid, String doact ){
		String filtercond = "";
		
		
		if( doact.equals("$DELAUTH")){
			filtercond = " AND DELETED_FLAG = '2' AND AUTH_CODE='0'";
		}else{
			filtercond = " AND DELETED_FLAG != '2'  ";
		}
		String mappingqry = "select * from PRODUCT_MASTER where   INST_ID='"+instid+"'"+filtercond;
		
		enctrace("mappingqry :" + mappingqry);
		return mappingqry;
	} 
	
	public String getProductdetails( String inst_Name,  String product_code){
		
		//String prod_det_query="select INST_ID,PRODUCT_CODE,CARD_TYPE_ID,CARD_TYPE_NAME,SUB_PROD_CNT,BIN,CONFIGURED_BY,CONFIGURED_DATE,ACTIVE_FLAG,GL_CODE,STATUS,SUB_PRODUCT_SEQ,DECODE(AUTH_CODE,'0','Waiting for authorize','1','Authorized','9','Deauthorized') as AUTH_CODE,MAKER_ID,MAKER_DATE,MKCK_STATUS,NVL(REMARKS,'--') as REMARKS,CHECKER_ID,CHECKER_DATE from PRODUCT_MASTER where INST_ID='"+inst_Name+"' and PRODUCT_CODE='"+product_code+"'";		
		String prod_det_query="select INST_ID,PRODUCT_CODE,CARD_TYPE_ID,CARD_TYPE_NAME,SUB_PROD_CNT,BIN,CONFIGURED_BY,CONFIGURED_DATE,ACTIVE_FLAG,GL_CODE,STATUS,SUB_PRODUCT_SEQ,DECODE(AUTH_CODE,'0','Waiting for authorize','1','Authorized','9','Deauthorized') as AUTH_CODE,MAKER_ID,MAKER_DATE,MKCK_STATUS,NVL(REMARKS,'--') as REMARKS,CHECKER_ID,CHECKER_DATE from PRODUCT_MASTER where INST_ID=? and PRODUCT_CODE=? ";
		return prod_det_query;
	}
	/*public String getgldesc(String inst_Name,String glcode, JdbcTemplate jdbcTemplate) {
		String gldesc = "Select GL_NAME from IFP_GL_MASTER where GL_CODE='"+glcode+"' and INST_ID='"+inst_Name+"'";
		enctrace("gldesc--> "+gldesc);
		String ret_val = (String) jdbcTemplate.queryForObject(gldesc, String.class);
		return ret_val;
	}*/
 
	public String getbindesc(String inst_Name,String bin,JdbcTemplate jdbcTemplate)
	{
		/*String bindesc = "Select BIN_DESC from PRODUCTBIN_REL where BIN='"+bin+"' and INST_ID='"+inst_Name+"'";
		enctrace("bin==> "+bindesc);
		String ret_val = (String)jdbcTemplate.queryForObject(bindesc, String.class);*/
		
		//by gowtham210819
		String bindesc = "Select PRD_DESC from PRODUCTINFO where PRD_CODE=? and INST_ID=? ";
		enctrace("bin==> "+bindesc);
		String ret_val = (String)jdbcTemplate.queryForObject(bindesc,new Object[]{bin,inst_Name}, String.class);
		
		return ret_val;
	}
	public String updateAuthProduct(String authstatus,String userid,String instid,String cardtype,String prodcode) {
		
		
		String update_authdeauth_qury = "UPDATE PRODUCT_MASTER SET MKCK_STATUS=?, AUTH_CODE=?,CHECKER_DATE=?,CHECKER_ID=? WHERE INST_ID=? AND CARD_TYPE_ID=? AND PRODUCT_CODE=? ";
		//String update_authdeauth_qury = "UPDATE PRODUCT_MASTER SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='1',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"' WHERE INST_ID='"+instid+"' AND CARD_TYPE_ID='"+cardtype+"' AND PRODUCT_CODE='"+prodcode+"'";
		enctrace("update_authdeauth_qury --- "+update_authdeauth_qury);
		return update_authdeauth_qury;
	}
	public String updateDeAuthProduct(String authstatus,String userid,String remarks,String instid,String cardtype,String prodcode) {
		
		String update_authdeauth_qury = "UPDATE PRODUCT_MASTER SET MKCK_STATUS=?, AUTH_CODE=?,CHECKER_DATE=?,CHECKER_ID=?',REMARKS=? WHERE INST_ID=? AND CARD_TYPE_ID=? AND PRODUCT_CODE=?";
		//String update_authdeauth_qury = "UPDATE PRODUCT_MASTER SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='9',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND CARD_TYPE_ID='"+cardtype+"' AND PRODUCT_CODE='"+prodcode+"'";
		enctrace("update_authdeauth_qury --- "+update_authdeauth_qury);
		return update_authdeauth_qury;
	}
	public int getAuthexistCardTypeList(String instid,JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String del_qury="SELECT COUNT(*) FROM  PRODUCT_MASTER  where INST_ID='"+instid+"' and AUTH_CODE='0'";
		enctrace( "del_qury : " + del_qury );
		x= jdbctemplate.queryForInt(del_qury);
		return x; 
	}
	public String getProductForChecker(String instid ){
		String mappingqry = "select * from PRODUCT_MASTER where AUTH_CODE='0'  and INST_ID='"+instid+"' AND DELETED_FLAG != '2'  ";		
		return mappingqry;
	} 
	
	public int moveProductToProduction(String instid, String productcode, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1; 
		String update_authdeauth_qury = "INSERT INTO PRODUCT_MASTER SELECT * FROM PRODUCT_MASTER WHERE INST_ID='"+instid+"' AND PRODUCT_CODE='"+productcode+"'";
		enctrace ( "move to production : "+ update_authdeauth_qury );
		x = jdbctemplate.update(update_authdeauth_qury);
		return x; 
	}
	
	public int deleteProductFromTable(String instid, String tablename, String productcode, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		
		/*String delprodqry = "DELETE FROM "+tablename+" WHERE INST_ID='"+instid+"' AND PRODUCT_CODE='"+productcode+"'";
		enctrace ( "delprodqry : "+ delprodqry );
		x = jdbctemplate.update(delprodqry);*/
		
		//by gowtham-210819
		String delprodqry = "DELETE FROM "+tablename+" WHERE INST_ID=? AND PRODUCT_CODE=? ";
		enctrace ( "delprodqry : "+ delprodqry );
		x = jdbctemplate.update(delprodqry,new Object[]{instid,productcode});
		
		return x;
	}
	
	public int deleteProductFromTemp(String instid, String productcode, String usercode, String authcode, String mkckstatus, String remarks, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		Date date =  new Date();
		/*String deletetempprod = "UPDATE PRODUCT_MASTER SET DELETED_FLAG='2', MAKER_ID='"+usercode+"',AUTH_CODE='"+authcode+"', MAKER_DATE=SYSDATE, MKCK_STATUS='"+mkckstatus+"', CHECKER_ID='', CHECKER_DATE='', REMARKS='"+remarks+"'  WHERE INST_ID='"+instid+"' AND PRODUCT_CODE='"+productcode+"'";
		enctrace ( "deletetempprod: "+ deletetempprod);
		x = jdbctemplate.update(deletetempprod);*/
		
		//by gowtham-210819
		String deletetempprod = "UPDATE PRODUCT_MASTER SET DELETED_FLAG=?, MAKER_ID=?,AUTH_CODE=?, MAKER_DATE=?, MKCK_STATUS=?, CHECKER_ID=?, CHECKER_DATE=?, REMARKS=?  WHERE INST_ID=? AND PRODUCT_CODE=? ";
		enctrace ( "deletetempprod: "+ deletetempprod);
		x = jdbctemplate.update(deletetempprod,new Object[]{"2",usercode,authcode,date.getCurrentDate(),mkckstatus,"","",remarks,instid,productcode});
		
		return x;
	}
	
	 
	
	public int deleteAuthStatusProductFromTemp(String instid, String productcode, String deletedflag, String usercode, String authcode, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		Date date =  new Date();
		/*String deletetempprod = "UPDATE PRODUCT_MASTER SET DELETED_FLAG='"+deletedflag+"', CHECKER_ID='"+usercode+"',AUTH_CODE='"+authcode+"', CHECKER_DATE=SYSDATE  WHERE INST_ID='"+instid+"' AND PRODUCT_CODE='"+productcode+"'";
		enctrace ( "deletetempprod: "+ deletetempprod);
		x = jdbctemplate.update(deletetempprod);*/
		
		//by gowtham-210819
		String deletetempprod = "UPDATE PRODUCT_MASTER SET DELETED_FLAG=?, CHECKER_ID=?,AUTH_CODE=?, CHECKER_DATE=?  WHERE INST_ID=? AND PRODUCT_CODE=? ";
		enctrace ( "deletetempprod: "+ deletetempprod);
		x = jdbctemplate.update(deletetempprod,new Object[]{deletedflag,usercode,authcode,Date.getCurrentDate(),instid,productcode});
		
		return x; 
	}
	
	
	/*public String getGlobalcardtpelist(String instid)
	{
		String globalcardtypeqry = null;
		globalcardtypeqry = "SELECT CARD_TYPE_ID, CARD_TYPE_DESC FROM CARD_TYPE WHERE INST_ID='"+instid+"' AND AUTH_CODE='1' ";
		return globalcardtypeqry;
	}
	public String getBinList(String instid)
	{
		String binlistqry = "SELECT BIN, BIN_DESC  FROM PRODUCTBIN_REL WHERE INST_ID='"+instid+"' ";
		return binlistqry;
	}

	public String getDeplymnt()
	{
		String qury = "select DEPLOYTYPE,DEPLOYID from DEPLOYMENT ";
		return qury;
	}    
	public String getFieldQury(String columname)
	{
		String field_qury = null;
		field_qury = "select count(*) from user_tab_cols where TABLE_NAME='BRANCH_MASTER' and COLUMN_NAME='"+columname+"'";
		return field_qury;
	}
	public String getAddfeild_qury(String columname) 
	{
	String addfeild_qury = null; 
	addfeild_qury = "alter table BRANCH_MASTER add(\""+columname+"\" varchar2(20) default '1')";
	return addfeild_qury;
	}
	public String getProductQuery(String instid, String prodseq, String productid, String productdesc, String prodsubtype, String bin, String usercode, String glcode,String auth_code,String mkchkrstatus)
	{
		String productquery=null;
		productquery="INSERT INTO PRODUCT_MASTER(INST_ID,PRODUCT_CODE, CARD_TYPE_ID,CARD_TYPE_NAME,SUB_PROD_CNT,BIN,CONFIGURED_BY,CONFIGURED_DATE,ACTIVE_FLAG, GL_CODE,AUTH_CODE,MAKER_ID,MAKER_DATE,MKCK_STATUS) VALUES ";
		productquery += "('"+instid+"','"+prodseq+"','"+productid+"','"+productdesc+"','"+prodsubtype+"', '"+bin+"', '"+usercode+"', sysdate, '1', '"+glcode+"','"+auth_code+"','"+usercode+"',sysdate,'"+mkchkrstatus+"')";
		return productquery;
	}
	public int checkProductExist(String instid, String productseq,  JdbcTemplate jdbctemplate ){
		String productqry = "SELECT COUNT(*) FROM PRODUCT_MASTER WHERE INST_ID='"+instid+"' AND PRODUCT_CODE='"+productseq+"'";
		System.out.println("product__" + productqry );
		int x = jdbctemplate.queryForInt(productqry);
		return x;
		
	}
	public int checkCardtypeExist( String productcode, String instid,JdbcTemplate jdbctemplate ){
		String cardtypeqry = "SELECT COUNT(*) FROM PRODUCT_MASTER WHERE INST_ID='"+instid+"' AND CARD_TYPE_ID='"+productcode+"'";
		System.out.println("product__" + cardtypeqry );
		int x = jdbctemplate.queryForInt(cardtypeqry);
		return x;
	}

	public int checkBinExist( String bincode,String instid,JdbcTemplate jdbctemplate ){
		String binqry = "SELECT COUNT(*) FROM PRODUCT_MASTER WHERE INST_ID='"+instid+"' AND BIN='"+bincode+"'";
		enctrace("bin__" + binqry );
		int x = jdbctemplate.queryForInt(binqry);
		return x;
	}
	public List fchBinMapping( String instid, JdbcTemplate jdbctemplate){
		String mappingqry = "SELECT BIN, CARD_TYPE FROM BIN_CARD_MAPPING WHERE INST_ID='"+instid+"'";
		List mappinglist = jdbctemplate.queryForList(mappingqry);
		return mappinglist;
	}
	
	public String getProduct(String instid ){
		String mappingqry = "select * from PRODUCT_MASTER where AUTH_CODE='1' and INST_ID='"+instid+"' AND DELETED_FLAG != '2' ";		
		return mappingqry;
	}
	
	public String getProductForView(String instid, String doact ){
		String filtercond = "";
		if( doact.equals("$DELAUTH")){
			filtercond = " AND DELETED_FLAG = '2' AND AUTH_CODE='0'";
		}else{
			filtercond = " AND DELETED_FLAG != '2'  ";
		}
		String mappingqry = "select * from PRODUCT_MASTER where   INST_ID='"+instid+"'"+filtercond;
		enctrace("mappingqry :" + mappingqry);
		return mappingqry;
	} 
	
	public String getProductdetails( String inst_Name,  String product_code){
		String prod_det_query="select INST_ID,PRODUCT_CODE,CARD_TYPE_ID,CARD_TYPE_NAME,SUB_PROD_CNT,BIN,CONFIGURED_BY,CONFIGURED_DATE,ACTIVE_FLAG,GL_CODE,STATUS,SUB_PRODUCT_SEQ,DECODE(AUTH_CODE,'0','Waiting for authorize','1','Authorized','9','Deauthorized') as AUTH_CODE,MAKER_ID,MAKER_DATE,MKCK_STATUS,NVL(REMARKS,'--') as REMARKS,CHECKER_ID,CHECKER_DATE from PRODUCT_MASTER where INST_ID='"+inst_Name+"' and PRODUCT_CODE='"+product_code+"'";		
		return prod_det_query;
	}
	public String getgldesc(String inst_Name,String glcode, JdbcTemplate jdbcTemplate) {
		String gldesc = "Select GL_NAME from IFP_GL_MASTER where GL_CODE='"+glcode+"' and INST_ID='"+inst_Name+"'";
		enctrace("gldesc--> "+gldesc);
		String ret_val = (String) jdbcTemplate.queryForObject(gldesc, String.class);
		return ret_val;
	}
 
	public String getbindesc(String inst_Name,String bin,JdbcTemplate jdbcTemplate)
	{
		String bindesc = "Select BIN_DESC from PRODUCTBIN_REL where BIN='"+bin+"' and INST_ID='"+inst_Name+"'";
		enctrace("bin==> "+bindesc);
		String ret_val = (String)jdbcTemplate.queryForObject(bindesc, String.class);
		return ret_val;
	}
	public String updateAuthProduct(String authstatus,String userid,String instid,String cardtype,String prodcode) {
		String update_authdeauth_qury = "UPDATE PRODUCT_MASTER SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='1',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"' WHERE INST_ID='"+instid+"' AND CARD_TYPE_ID='"+cardtype+"' AND PRODUCT_CODE='"+prodcode+"'";
		enctrace("update_authdeauth_qury --- "+update_authdeauth_qury);
		return update_authdeauth_qury;
	}
	public String updateDeAuthProduct(String authstatus,String userid,String remarks,String instid,String cardtype,String prodcode) {
		String update_authdeauth_qury = "UPDATE PRODUCT_MASTER SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='9',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND CARD_TYPE_ID='"+cardtype+"' AND PRODUCT_CODE='"+prodcode+"'";
		enctrace("update_authdeauth_qury --- "+update_authdeauth_qury);
		return update_authdeauth_qury;
	}
	public int getAuthexistCardTypeList(String instid,JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String del_qury="SELECT COUNT(*) FROM  PRODUCT_MASTER  where INST_ID='"+instid+"' and AUTH_CODE='0'";
		enctrace( "del_qury : " + del_qury );
		x= jdbctemplate.queryForInt(del_qury);
		return x; 
	}
	public String getProductForChecker(String instid ){
		String mappingqry = "select * from PRODUCT_MASTER where AUTH_CODE='0'  and INST_ID='"+instid+"' AND DELETED_FLAG != '2'  ";		
		return mappingqry;
	} 
	
	public int moveProductToProduction(String instid, String productcode, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1; 
		String update_authdeauth_qury = "INSERT INTO PRODUCT_MASTER SELECT * FROM PRODUCT_MASTER WHERE INST_ID='"+instid+"' AND PRODUCT_CODE='"+productcode+"'";
		enctrace ( "move to production : "+ update_authdeauth_qury );
		x = jdbctemplate.update(update_authdeauth_qury);
		return x; 
	}
	
	public int deleteProductFromTable(String instid, String tablename, String productcode, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String delprodqry = "DELETE FROM "+tablename+" WHERE INST_ID='"+instid+"' AND PRODUCT_CODE='"+productcode+"'";
		enctrace ( "delprodqry : "+ delprodqry );
		x = jdbctemplate.update(delprodqry);
		return x;
	}
	
	public int deleteProductFromTemp(String instid, String productcode, String usercode, String authcode, String mkckstatus, String remarks, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String deletetempprod = "UPDATE PRODUCT_MASTER SET DELETED_FLAG='2', MAKER_ID='"+usercode+"',AUTH_CODE='"+authcode+"', MAKER_DATE=SYSDATE, MKCK_STATUS='"+mkckstatus+"', CHECKER_ID='', CHECKER_DATE='', REMARKS='"+remarks+"'  WHERE INST_ID='"+instid+"' AND PRODUCT_CODE='"+productcode+"'";
		enctrace ( "deletetempprod: "+ deletetempprod);
		x = jdbctemplate.update(deletetempprod);
		return x;
	}
	
	 
	
	public int deleteAuthStatusProductFromTemp(String instid, String productcode, String deletedflag, String usercode, String authcode, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String deletetempprod = "UPDATE PRODUCT_MASTER SET DELETED_FLAG='"+deletedflag+"', CHECKER_ID='"+usercode+"',AUTH_CODE='"+authcode+"', CHECKER_DATE=SYSDATE  WHERE INST_ID='"+instid+"' AND PRODUCT_CODE='"+productcode+"'";
		enctrace ( "deletetempprod: "+ deletetempprod);
		x = jdbctemplate.update(deletetempprod);
		return x; 
	}*/
	
}
