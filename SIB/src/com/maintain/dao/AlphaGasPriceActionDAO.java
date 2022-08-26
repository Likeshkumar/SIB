package com.maintain.dao;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import com.ifp.Action.BaseAction;

public class AlphaGasPriceActionDAO extends BaseAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public List getUnitPriceListForAuth(String instid,String productid,JdbcTemplate jdbctemplate) throws Exception{
		String query="SELECT PRODUCTID,INST_ID,UNITPRICE,PRODUCTDESC,ACTIVESTATUS,CONFIGBY,CONFIG_DATE FROM IFPS_ALPHAGASPRICE_HISTORY WHERE INST_ID='"+instid+"' AND PRODUCTID='"+productid+"' AND ACTIVESTATUS='0' AND CONFIGSTATUS!='9' AND CONFIGSTATUS='0'";
		System.out.println( "query : " + query );
		enctrace( "query : " + query );	 
		List x = jdbctemplate.queryForList(query);
		return x;
	}
	
	public int updateAuthUnitPrice(String instid,String unitprice,String username,String productid,JdbcTemplate jdbctemplate) throws Exception{
		String update_authdeauth_qury = "UPDATE IFPS_ALPHAGASPRICE_HISTORY SET ACTIVESTATUS='1',CONFIGSTATUS='1',AUTHBY='"+username+"',AUTHDATE=(sysdate) WHERE INST_ID='"+instid+"' AND PRODUCTID='"+productid+"' AND UNITPRICE='"+unitprice+"' AND ACTIVESTATUS='0' AND CONFIGSTATUS!='9' AND CONFIGSTATUS='0'";
		enctrace( "updateAuthUnitPrice query : " + update_authdeauth_qury );	 
		int x = jdbctemplate.update(update_authdeauth_qury);
		System.out.println("x--- "+x);
		return x;
	}
	
	public List getAuthorizedUnitPriceList(String instid,String productid,JdbcTemplate jdbctemplate) throws Exception{
		String query="SELECT PRODUCTID,INST_ID,UNITPRICE,PRODUCTDESC,DECODE(ACTIVESTATUS,'0','INACTIVE','1','ACTIVE') AS ACTIVESTATUS,CONFIGBY,CONFIG_DATE,CONFIGSTATUS FROM IFPS_ALPHAGASPRICE WHERE INST_ID='"+instid+"' AND PRODUCTID='"+productid+"' AND ACTIVESTATUS='1' AND CONFIGSTATUS!='9'";
		System.out.println("query -- > "+query);
		enctrace( "getAuthorizedUnitPriceList query : " + query );	 
		List x = jdbctemplate.queryForList(query);
		return x;
	}
	
	public int deletepricefromHistory(String instid,String unitprice,String productid,JdbcTemplate jdbctemplate) throws Exception{
		String delete_hiatory_qury = "DELETE FROM IFPS_ALPHAGASPRICE WHERE INST_ID='"+instid+"' AND PRODUCTID='"+productid+"'";
		enctrace( "delete_hiatory_qury_query : " + delete_hiatory_qury );	 
		int x = jdbctemplate.update(delete_hiatory_qury);
		return x;
	}
	
	public int movehistorytoalphaprice( String instid, String productid,String unitprice,JdbcTemplate jdbctemplate ) throws Exception{		
		String query="";		
		query = "INSERT INTO IFPS_ALPHAGASPRICE (INST_ID,PRODUCTID,UNITPRICE,PRODUCTDESC,ACTIVESTATUS,CONFIGBY,CONFIG_DATE,AUTHBY,AUTHDATE,CONFIGSTATUS)";	
		query += " SELECT INST_ID,PRODUCTID,UNITPRICE,PRODUCTDESC,ACTIVESTATUS,CONFIGBY,CONFIG_DATE,AUTHBY,AUTHDATE,CONFIGSTATUS FROM IFPS_ALPHAGASPRICE_HISTORY WHERE INST_ID='"+instid+"' AND PRODUCTID='"+productid+"' AND UNITPRICE='"+unitprice+"' AND ACTIVESTATUS='1' AND CONFIGSTATUS!='9'";		
		System.out.println( "insert and select query : " + query ); 	 
		int x  = jdbctemplate.update(query);
		return x;
	}
	
	public int updateDeAuthUnitPrice(String instid,String unitprice,String username,String productid,JdbcTemplate jdbctemplate) throws Exception{
		String update_authdeauth_qury = "UPDATE IFPS_ALPHAGASPRICE_HISTORY SET CONFIGSTATUS='9',AUTHBY='"+username+"',AUTHDATE=(sysdate) WHERE INST_ID='"+instid+"' AND PRODUCTID='"+productid+"' AND UNITPRICE='"+unitprice+"' AND ACTIVESTATUS='0' AND CONFIGSTATUS!='9' AND CONFIGSTATUS='0'";
		enctrace( "updateDeAuthUnitPrice : " + update_authdeauth_qury );	 
		int x = jdbctemplate.update(update_authdeauth_qury);
		return x;
	}
	
	public int checkDeAuthorization(String instid, String productid,JdbcTemplate jdbctemplate) throws Exception{
		String query="SELECT COUNT(*) FROM IFPS_ALPHAGASPRICE_HISTORY WHERE INST_ID='"+instid+"' AND PRODUCTID='"+productid+"' AND CONFIGSTATUS='9'";
		enctrace( "query : " + query );	 
		int x = jdbctemplate.queryForInt(query);
		return x;
	}
	
	public int checkAuthorization(String instid, String productid,String unit_price,JdbcTemplate jdbctemplate) throws Exception{
		String query="SELECT COUNT(*) FROM IFPS_ALPHAGASPRICE_HISTORY WHERE INST_ID='"+instid+"' AND PRODUCTID='"+productid+"'  AND ACTIVESTATUS='0' AND CONFIGSTATUS!='9' AND CONFIGSTATUS='0'";
		enctrace( "checkAuthorization query : " + query );
		System.out.println("checkAuthorization query : " + query );	 
		int x = jdbctemplate.queryForInt(query);
		return x;
	}
	
	public int insertUnitPrice(String instid, String productid, String unit_price, String product_desc, String username,String usercode, JdbcTemplate jdbctemplate) throws Exception{
		String query="INSERT INTO IFPS_ALPHAGASPRICE_HISTORY(INST_ID,PRODUCTID,UNITPRICE,PRODUCTDESC,ACTIVESTATUS,CONFIGBY,CONFIG_DATE,CONFIGSTATUS)" +
		"VALUES('"+instid+"','"+productid+"','"+unit_price+"','"+product_desc+"','0','"+username+"',(sysdate),'0') ";		
		System.out.println(" insertUnitPrice "+query);
		enctrace( "query : " + query );	 
		int x  = jdbctemplate.update(query);
		System.out.println(" insertUnitPrice "+x);
		return x;
	}
	
	public int getproductHistoryexist(String instid,String productid,JdbcTemplate jdbctemplate) throws Exception{
		String query="SELECT COUNT(*) FROM IFPS_ALPHAGASPRICE_HISTORY WHERE INST_ID='"+instid+"' AND PRODUCTID='"+productid+"' AND ACTIVESTATUS='1' AND CONFIGSTATUS!='9'";
		enctrace( "query : " + query );	 
		int x = jdbctemplate.queryForInt(query);
		return x;
	}
	
	public List getHistoryList(String instid,String productid,JdbcTemplate jdbctemplate) throws Exception{
		String query="SELECT PRODUCTID,INST_ID,UNITPRICE,PRODUCTDESC,DECODE(ACTIVESTATUS,'0','INACTIVE','1','ACTIVE') AS STATUS,CONFIGBY,CONFIG_DATE,DECODE(CONFIGSTATUS,'1','ACTIVE','9','DELETED') AS DELETE_STATUS FROM IFPS_ALPHAGASPRICE_HISTORY WHERE INST_ID='"+instid+"' AND PRODUCTID='"+productid+"' order by CONFIG_DATE";
		System.out.println("query -- > "+query);
		enctrace( "getAuthorizedUnitPriceList query : " + query );	 
		List x = jdbctemplate.queryForList(query);
		return x;
	}
	
	public int checkUnitprice(String instid,String productid,String unit_price,JdbcTemplate jdbctemplate) throws Exception{
		System.out.println("unit_price----- "+unit_price);
		String query="SELECT COUNT(*) FROM IFPS_ALPHAGASPRICE WHERE INST_ID='"+instid+"' AND PRODUCTID='"+productid+"' AND UNITPRICE='"+unit_price+"'";
		System.out.println("query -- > "+query);
		enctrace( "getAuthorizedUnitPriceList query : " + query );	 
		int x = jdbctemplate.queryForInt(query);
		return x;
	}
	
	public int updateActivestatus(String instid,String productid,JdbcTemplate jdbctemplate){
		int x = 0;
		try {
			String update_query="UPDATE IFPS_ALPHAGASPRICE_HISTORY SET ACTIVESTATUS='0' WHERE INST_ID='"+instid+"' AND PRODUCTID='"+productid+"'";
			System.out.println("query -- > "+update_query);
			enctrace( "update_query query : " + update_query );	 
			x = jdbctemplate.update(update_query);
		}catch (Exception e) {
			trace("Exception: could not excute updateActivestatus query ");
			e.printStackTrace();
		}
		return x;
	}
}
