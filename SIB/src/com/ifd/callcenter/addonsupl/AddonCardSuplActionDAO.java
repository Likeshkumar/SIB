package com.ifd.callcenter.addonsupl;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;

public final class AddonCardSuplActionDAO extends BaseAction{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	CommonDesc commondesc = new CommonDesc();
	
	public int validateActvatedCard(String instid,String padssenable,String cardno,JdbcTemplate jdbcTemplate)
	{
		int validcard = -1;
		StringBuilder valdcardqry = new StringBuilder();
		/*if(padssenable.equals("Y")){*/
		
		/*valdcardqry.append("select count(1) from CARD_PRODUCTION where Hcard_no in (select chn from EZCARDINFO ) and CARD_STATUS='05' AND Hcard_no='"+cardno+"' AND INST_ID='"+instid+"' ");*/
	
		
		///by gowtham
		/*valdcardqry.append("select count(1) from CARD_PRODUCTION where CARD_STATUS='05' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE Hcard_no='"+cardno+"') AND INST_ID='"+instid+"' ");*/
		valdcardqry.append("select count(1) from CARD_PRODUCTION where CARD_STATUS='05' AND ORDER_REF_NO "
		+ "IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE Hcard_no=? ) AND INST_ID=? ");
		
		
		/*}else
		{
			valdcardqry.append("select count(1) from CARD_PRODUCTION where card_no in (select chn from EZCARDINFO ) and CARD_STATUS='05' AND  card_no='"+cardno+"' AND INST_ID='"+instid+"' ");	
		}*/
		enctrace("valdcardqry::"+valdcardqry.toString()); 
		
		/*validcard = jdbcTemplate.queryForInt(valdcardqry.toString());*/
		validcard = jdbcTemplate.queryForInt(valdcardqry.toString(),new Object[]{cardno,instid});
		
		return validcard;
	}  
	
	
	
	public int getbincountinproduct(String instid,String padssenable,String cardno,String bin,JdbcTemplate jdbcTemplate)
	{
		
		int validcard = -1;
		StringBuilder valdcardqry = new StringBuilder();
		if(padssenable.equals("Y")){
		valdcardqry.append("select count(1) from CARD_PRODUCTION where INST_ID='"+instid+"' and BIN='"+bin+"' and   Hcard_no in (select chn from EZCARDINFO ) and CARD_STATUS='05' AND Hcard_no='"+cardno+"' AND INST_ID='"+instid+"' ");
		}else
		{
			valdcardqry.append("select count(1) from CARD_PRODUCTION where INST_ID='"+instid+"' and BIN='"+bin+"' and   card_no in (select chn from EZCARDINFO ) and CARD_STATUS='05' AND  card_no='"+cardno+"' AND INST_ID='"+instid+"' ");	
		}
		enctrace("valdcardqry::"+valdcardqry.toString());  
		validcard = jdbcTemplate.queryForInt(valdcardqry.toString());
		return validcard;
	}
			
	
	public int updateCardStatusDate(String instid,String padssenable, String cardno, String dbcolumnfld, String TABLENAME, JdbcTemplate jdbctemplate) {
		int x = -1;String qry = "";
		
		// by siva 10-07-19
		/*if(padssenable.equals("Y")){
		qry ="UPDATE "+TABLENAME+" SET "+dbcolumnfld+"=SYSDATE WHERE INST_ID='"+instid+"' AND HCARD_NO='"+cardno+"'";
		}else{
			qry ="UPDATE "+TABLENAME+" SET "+dbcolumnfld+"=SYSDATE WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";	
		}*/
		
		/*qry ="UPDATE "+TABLENAME+" SET "+dbcolumnfld+"=SYSDATE WHERE INST_ID='"+instid+"' AND"
		+ " ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"+cardno+"')";	
		enctrace("updateCardStatusDate::"+qry);
		x  = jdbctemplate.update(qry);*/
		
		///by gowtham
		qry ="UPDATE "+TABLENAME+" SET "+dbcolumnfld+"=SYSDATE WHERE INST_ID=? AND"
		+ " ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO=? )";	
		enctrace("updateCardStatusDate::"+qry);
		x  = jdbctemplate.update(qry,new Object[]{instid,cardno});
		
		return x;
	}
	
	public String getProductByCHN(String instid, String cardno,	JdbcTemplate jdbctemplate, String padssenable) {
		
		String prodqry = "";
		//if(padssenable.equals("Y")){
		/*prodqry ="SELECT PRODUCT_CODE FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND HCARD_NO='"+cardno+"' AND ROWNUM <=1" ;*/
		
		
		/*prodqry ="SELECT PRODUCT_CODE FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"+cardno+"') AND ROWNUM <=1" ;*/
		prodqry ="SELECT PRODUCT_CODE FROM CARD_PRODUCTION WHERE INST_ID=? AND ORDER_REF_NO "
		+ "IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO=? ) AND ROWNUM <=?" ;
		
		
		/*}else
		{
			prodqry ="SELECT PRODUCT_CODE FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"' AND ROWNUM <=1" ;	
		}*/
		trace("prodqry__"+prodqry);
		String prodcode = null;
		try {
			
			/*prodcode=(String)jdbctemplate.queryForObject(prodqry,String.class);*/
			prodcode=(String)jdbctemplate.queryForObject(prodqry,new Object[]{instid,cardno,"1"},String.class);
			
			
			trace("prodcode__" + prodcode );
		} catch (EmptyResultDataAccessException e) { 
			prodcode= "NOREC";
		} 
		return  prodcode;  
	}
	
public int moveCardToProcess( String instid,String order_ref_no,String padssenable,String productCode,String subproductcode,String binno,String accountno, String cardno, String newecardno,String newmcardno,String newhcardno, String caf_recstatus, String card_status,String suppname, String newcardno, String expperiod, String collectbranch,String newservicecode, JdbcTemplate jdbctemplate ) {
		
		String cardtypeid = subproductcode.substring(5,8);
		String cardccy = null;
		String expirydatecond = "add_months(sysdate,"+expperiod+")";
		StringBuilder query=new StringBuilder();
		StringBuilder query1=new StringBuilder();
		trace("checking card number --->"+cardno);
		query.append("INSERT INTO  PERS_CARD_PROCESS (");
		query.append("INST_ID,MCARD_NO,ACCT_NO,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY, CIN, ORDER_REF_NO, CARD_STATUS, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY, ");
		query.append("GENERATED_DATE, EXPIRY_DATE, PRE_DATE,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE,");
		query.append("APP_DATE, PIN_DATE, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT,");
		query.append("WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID,");
		query.append("BIN, AUTH_DATE, STATUS_CODE,CARDISSUETYPE,CARD_COLLECT_BRANCH)");
		query.append(" SELECT INST_ID,'"+newmcardno+"','"+accountno+"', ACCTTYPE_ID,ACCTSUB_TYPE_ID, ACC_CCY,CIN , '"+order_ref_no+"', '"+card_status+"', '"+cardtypeid+"', '"+subproductcode+"', '"+productCode+"', BRANCH_CODE, PC_FLAG, '"+cardccy+"',");
		query.append("GENERATED_DATE, "+expirydatecond+", PRE_DATE, MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, 'M','"+newservicecode+"', REG_DATE, ");
		query.append("APP_DATE, sysdate, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, '"+caf_recstatus+"', FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT, ");
		query.append("WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, '"+suppname.toUpperCase()+"', '"+suppname.toUpperCase()+"', APP_NO, ORDER_FLAG, '"+newecardno+"', USED_CHN, COURIER_ID, ");
		//if(padssenable.equals("Y")){
		query.append("'"+binno+"', AUTH_DATE, STATUS_CODE,'P','"+collectbranch+"' FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"+cardno+"')");
		/*}
		else   
		{    
		query.append("BIN, AUTH_DATE, STATUS_CODE,'P','"+collectbranch+"' FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'");	
		}*/
		int x  = jdbctemplate.update(query.toString());
		
		
		query1.append("INSERT INTO  PERS_CARD_PROCESS_HASH (");
		query1.append("INST_ID, HCARD_NO,ACCT_NO, CIN, ORDER_REF_NO,  CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, ");
		query1.append("GENERATED_DATE, MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, ");
		query1.append("BIN)");
		query1.append(" SELECT INST_ID,'"+newhcardno+"','"+accountno+"', CIN , '"+order_ref_no+"', '"+cardtypeid+"', '"+subproductcode+"', '"+productCode+"', BRANCH_CODE, ");
		query1.append("GENERATED_DATE, MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE,");
		//if(padssenable.equals("Y")){
		query1.append("'"+binno+"'  FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"+cardno+"')");
		/*}
		else   
		{    
		query.append("BIN, AUTH_DATE, STATUS_CODE,'P','"+collectbranch+"' FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'");	
		}*/
		enctrace( "moveCardToProcess : " + query1.toString() ); 
	           
		int x1  = jdbctemplate.update(query1.toString());
		return x1;
}


public int updateaddonCount( String instid,String padssenable, String cardno, JdbcTemplate jdbctemplate){
	String fchupdcntqry ="";
	
	//by siva
	/*if(padssenable.equals("Y")){
	fchupdcntqry = "SELECT REISSUE_CNT FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND HCARD_NO='"+cardno+"'";
	}else{
	fchupdcntqry = "SELECT REISSUE_CNT FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";	
	}*/
	
	/*fchupdcntqry = "SELECT REISSUE_CNT FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"+cardno+"')";
	enctrace("fchupdcntqry::"+fchupdcntqry);
	int updcnt = 0;
	try 
	{   
		updcnt = jdbctemplate.queryForInt(fchupdcntqry); */
	
	//by goqtham
	fchupdcntqry = "SELECT REISSUE_CNT FROM CARD_PRODUCTION WHERE INST_ID=? AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO=? )";
	enctrace("fchupdcntqry::"+fchupdcntqry);
	int updcnt = 0;
	try 
	{   
		updcnt = jdbctemplate.queryForInt(fchupdcntqry,new Object[]{instid,cardno});
		
	} catch (EmptyResultDataAccessException e) { 
		 updcnt = 0;
	}
	
	int newcnt = updcnt+1;String cntqry = "";
	
	
	/*if(padssenable.equals("Y")){
	cntqry = "UPDATE CARD_PRODUCTION SET ADDON_FLAG='S', ADDONCARDCOUNT= '"+newcnt+"', ADDON_DATE=sysdate WHERE INST_ID='"+instid+"' AND HCARD_NO='"+cardno+"' ";
	}else{
		cntqry = "UPDATE CARD_PRODUCTION SET ADDON_FLAG='S', ADDONCARDCOUNT= '"+newcnt+"', ADDON_DATE=sysdate WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"' ";	
	}*/
	
	
	
	//by gowtham
	/*cntqry = "UPDATE CARD_PRODUCTION SET ADDON_FLAG='S', ADDONCARDCOUNT= '"+newcnt+"', ADDON_DATE=sysdate WHERE INST_ID='"+instid+"' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"+cardno+"') ";
	enctrace( "REISSUE UPDATE QRY __" +  cntqry );
	int x = commondesc.executeTransaction(cntqry, jdbctemplate);*/	
	cntqry = "UPDATE CARD_PRODUCTION SET ADDON_FLAG='S', ADDONCARDCOUNT= ?, ADDON_DATE=sysdate WHERE INST_ID=? AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO=? ) ";
	enctrace( "REISSUE UPDATE QRY __" +  cntqry );
	int x = jdbctemplate.update(cntqry,new Object[]{newcnt,instid,cardno});
	return x;		
	
}

	public List getcarddettailForSuplimentary(String instid,String padssenable,String productCode,String subprodcode,String binno,String cardno,JdbcTemplate jdbcTemplate)
	{
		List cardlist = null;
		StringBuilder cardq = new StringBuilder();
		
		/*cardq.append("SELECT ORG_CHN as CARD_NO,ORDER_REF_NO,(SELECT HCARD_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"+cardno+"' ) as HCARD_NO,MCARD_NO,ACCOUNT_NO,MOBILENO,ENC_NAME,TO_CHAR(DOB,'DD-mm-YYYY')DOB,CIN,'"+binno+"',BIN, ");
		cardq.append("(SELECT STATUS FROM ezcardinfo WHERE CHN='"+cardno+"')STATUSCODE , ");
		cardq.append("(SELECT STATUS_DESC FROM MAINTAIN_DESC WHERE STATUS in (STATUS_CODE) AND INST_ID='"+instid+"'  ) STATUS_DESC,");
		cardq.append("(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE PRODUCT_CODE ='"+productCode+"' AND INST_ID='"+instid+"' ) PRODUCT_DESC, ");
		cardq.append("(SELECT SUB_PRODUCT_NAME FROM INSTPROD_DETAILS WHERE SUB_PROD_ID ='"+subprodcode+"' AND INST_ID='"+instid+"' ) ");
		cardq.append("SUBPRODUCT_DESC, '"+subprodcode+"' SUBPRODUCT_CODE ,");
		cardq.append("'"+productCode+"' PRODUCT_CODE,ORG_CHN from  CARD_PRODUCTION cp ,  EZCUSTOMERINFO ezc "); 
		///if(padssenable.equals("Y")){
		//cardq.append("WHERE cp.inst_id=ezc.INSTID and cp.CIN = ezc.CUSTID AND HCARD_NO='"+cardno+"' ");
		cardq.append("WHERE cp.inst_id=ezc.INSTID and cp.CIN = ezc.CUSTID AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE HCARD_NO='"+cardno+"') ");
		///*}else{
		//cardq.append("WHERE cp.inst_id=ezc.INSTID and cp.CIN = ezc.CUSTID AND CARD_NO='"+cardno+"' ");	
		//}
		enctrace("cardq-----:"+cardq.toString());         
		cardlist = jdbcTemplate.queryForList(cardq.toString());*/
		
		cardq.append("SELECT ORG_CHN as CARD_NO,ORDER_REF_NO,(SELECT HCARD_NO FROM CARD_PRODUCTION_HASH "
		+ "WHERE HCARD_NO=? ) as HCARD_NO,MCARD_NO,ACCOUNT_NO,MOBILENO,ENC_NAME,TO_CHAR(DOB,'DD-mm-YYYY')DOB,"
		+ "CIN,?,BIN, ");
		cardq.append("(SELECT STATUS FROM ezcardinfo WHERE CHN=? )STATUSCODE , ");
		cardq.append("(SELECT STATUS_DESC FROM MAINTAIN_DESC WHERE STATUS in (STATUS_CODE) AND INST_ID=?  ) STATUS_DESC,");
		cardq.append("(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE PRODUCT_CODE =? AND "
		+ "INST_ID=? ) PRODUCT_DESC, ");
		cardq.append("(SELECT SUB_PRODUCT_NAME FROM INSTPROD_DETAILS WHERE SUB_PROD_ID =? AND "
		+ "INST_ID=? ) ");
		cardq.append("SUBPRODUCT_DESC, '"+subprodcode+"' SUBPRODUCT_CODE ,");
		cardq.append("'"+productCode+"' PRODUCT_CODE,ORG_CHN from  CARD_PRODUCTION cp ,  EZCUSTOMERINFO ezc "); 
		cardq.append("WHERE cp.inst_id=ezc.INSTID and cp.CIN = ezc.CUSTID AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM"
		+ " CARD_PRODUCTION_HASH WHERE HCARD_NO=? ) ");
		enctrace("cardq-----:"+cardq.toString());         
		cardlist = jdbcTemplate.queryForList(cardq.toString(),new Object[]{cardno,binno,cardno,instid,productCode,instid,subprodcode
		,instid,cardno});
		
		
		return cardlist;
	}   

	

}
