package com.ifd.personalize;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.ifp.Action.BaseAction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
public class DebitBulkCustomerRegisterDAO extends BaseAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JdbcTemplate jdbcTemplate = new JdbcTemplate();
	public void setDataSource(DataSource datasource)
	{
		this.jdbcTemplate = new JdbcTemplate(datasource);
	}

public List getBulkConfigList(String instid,String cname ,JdbcTemplate jdbctemplate) {
		
		List result= null;
		trace("Getting Bulk Config data");
		String qury="select COLUMN_NAME,FROM_FLD,TO_FLD from BULK_REGCONFIG  ORDER BY POSITION";
		enctrace("Getting getBulkConfigList : " + qury); 
		result =jdbctemplate.queryForList(qury);    
		return result;
			}

public List getFailureList(String instid, JdbcTemplate jdbctemplate) {
	List result= null;
	trace("Getting Failure Line data");
	String qury="select line_no from BULK_REG_STATUS where inst_id='ORBL' group by line_no order by to_number(line_no)";
	enctrace("Getting ailure Line data : " + qury); 
	result =jdbctemplate.queryForList(qury); 
	return result;
	
}

public String  getMAxLineNo(String instid, JdbcTemplate jdbctemplate) {
	String result= null;
	String qury="select max(to_number(TO_FLD)) MAX_LENGTH from BULK_REGCONFIG ";
	result =(String) jdbctemplate.queryForObject(qury, String.class); 
	trace("result::"+result);
	return result;
}


/*
public int  getSubProductList(String instid,String binno, JdbcTemplate jdbctemplate) {
	int result= -1;
	String qury="select COUNT(1) from INSTPROD_DETAILS  where INST_ID='"+instid+"' AND SUB_PROD_ID='"+binno+"' AND AUTH_STATUS = '1' ORDER BY SUB_PROD_ID ";
	result =jdbctemplate.queryForInt(qury); 
	return result;
}

public List<String>  getSubProductList(String instid, JdbcTemplate jdbctemplate) {
	
	List result=null;
	String qury="select SUB_PROD_ID from INSTPROD_DETAILS  where INST_ID='"+instid+"' AND AUTH_STATUS = '1' ORDER BY SUB_PROD_ID ";
	result =jdbctemplate.queryForList(qury); 
	List getSubProdAList = new ArrayList<>();
    Iterator bitr = result.iterator();
	while(bitr.hasNext()) {      
		 Map map = (Map)bitr.next();
		 getSubProdAList.add((String)map.get("SUB_PROD_ID"));
	}
	return getSubProdAList;
	
}

public List<String>  getBranchList(String instid, JdbcTemplate jdbctemplate) {
	List result=null;
	String qury="select BRANCH_CODE from BRANCH_MASTER  where INST_ID='"+instid+"' AND AUTH_CODE = '1' AND BRANCH_CODE!='000' ORDER BY BRANCH_CODE";
	
	enctrace("getBranchList::"+qury);
	result =jdbctemplate.queryForList(qury); 
	
	List getBranchAList = new ArrayList<>();
    Iterator bitr = result.iterator();
	while(bitr.hasNext()) {      
		 Map map = (Map)bitr.next();
		 getBranchAList.add((String)map.get("BRANCH_CODE"));
	}
	return getBranchAList;
}



public List<String> getAccountList(String instid, JdbcTemplate jdbctemplate) {
	List result=null;
	String qury="select ACCOUNTNO from ACCOUNTINFO  where INST_ID='"+instid+"' ORDER BY ACCOUNTNO ";
	result =jdbctemplate.queryForList(qury); 
	List getAccAList = new ArrayList<>();
    Iterator bitr = result.iterator();
	while(bitr.hasNext()) {      
		 Map map = (Map)bitr.next();
		 getAccAList.add((String)map.get("ACCOUNTNO"));
		}
	return getAccAList;
	
}

public List<String> getAccountTypeList(String instid, JdbcTemplate jdbctemplate) {
	List result=null;
	String qury="select ACCTTYPEID from ACCTTYPE  where INST_ID='"+instid+"' AND AUTH_CODE = '1' ORDER BY ACCTTYPEID ";
	result =jdbctemplate.queryForList(qury); 
	
	List getAccTypeAList = new ArrayList<>();
    Iterator bitr = result.iterator();
	while(bitr.hasNext()) {      
		 Map map = (Map)bitr.next();
		 getAccTypeAList.add((String)map.get("ACCTTYPEID"));
		
	}
	return getAccTypeAList;
}  

public List<String> getAccountSubTypeList(String instid, JdbcTemplate jdbctemplate) {
	List result=null;
	String qury="select ACCTSUBTYPEID from ACCTSUBTYPE  where  INST_ID='"+instid+"' ORDER BY ACCTSUBTYPEID ";
	result =jdbctemplate.queryForList(qury); 
	
	List getList = new ArrayList<>();
    Iterator bitr = result.iterator();
	while(bitr.hasNext()) {      
		 Map map = (Map)bitr.next();
		 getList.add((String)map.get("ACCTSUBTYPEID"));
		
	}
	return getList;
}  

public List<String> getAccountCcyList(JdbcTemplate jdbctemplate) {
	List result=null;
	String qury="select NUMERIC_CODE from GLOBAL_CURRENCY  WHERE AUTH_CODE='1' ORDER BY NUMERIC_CODE ";
	result =jdbctemplate.queryForList(qury); 
	
	List getList = new ArrayList<>();
    Iterator bitr = result.iterator();
	while(bitr.hasNext()) {      
		 Map map = (Map)bitr.next();
		 getList.add((String)map.get("NUMERIC_CODE"));
		
	}
	return getList;
	
}

public List<String> getCustIdList(String instid,JdbcTemplate jdbctemplate) {
	List result=null;
	String qury="select CIN from ACCOUNTINFO  where INST_ID='"+instid+"' ORDER BY CIN ";
	result =jdbctemplate.queryForList(qury); 
	
	List getList = new ArrayList<>();
    Iterator bitr = result.iterator();
	while(bitr.hasNext()) {      
		 Map map = (Map)bitr.next();
		 getList.add((String)map.get("CIN"));
		
	}
	return getList;
}


public String getFailRegStatusQry(String instid,String fileName,int iterat,String resoncode,String usercode)
{
StringBuilder failStatus = new StringBuilder();
failStatus.append("INSERT INTO BULKFAIL_REG_STATUS ");
failStatus.append("(INST_ID, FILENAME, LINE_NO, REASON, ADDED_BY, ADDED_DATE , FAIL_REJECT)");
failStatus.append("VALUES");
failStatus.append("('"+instid+"', '"+fileName+"', '"+iterat+"', '"+resoncode+"', '"+usercode+"', sysdate,'F')");
enctrace("failStatus"+failStatus.toString());
return failStatus.toString();
}






public List getUploadStatus(String instid, String uploadFileFileName,JdbcTemplate jdbctemplate) {
	List result= null;
	trace("Getting getUploadStatus ");
	StringBuilder qury = new StringBuilder();
	qury.append("select INST_ID, FILENAME, TO_CHAR(UPLOADDATE,'DD-MM-YYYY')UPLOADDATE, (SELECT USERNAME FROM USER_DETAILS WHERE USERID in ");
	qury.append("(SELECT UPLOADEDBY FROM BULK_REG_STATUS where FILENAME='"+uploadFileFileName+"' )) UPLOADEDBY, STATUS, TATAL_RECORD, SUCCESS, FAIL, REJECT_REC from "); 
	qury.append("BULK_REG_STATUS where filename='"+uploadFileFileName+"' AND inst_id='"+instid+"' AND FILENAME = '"+uploadFileFileName+"'" );
	enctrace("Getting ailure Line data : " + qury); 
	result =jdbctemplate.queryForList(qury.toString()); 
	return result;
	
}


public int getBulkfilenameCount(String instid, String uploadFileFileName,JdbcTemplate jdbctemplate) {
	int result= -1;
	trace("Getting getBulkfilenameCount ");
	String qury="select COUNT(1) from BULK_REG_STATUS where inst_id='"+instid+"' AND FILENAME = '"+uploadFileFileName+"'";
	enctrace("getBulkfilenameCount : " + qury); 
	result =jdbctemplate.queryForInt(qury);     
	return result;
	
}
*/


public int  getSubProductList(String instid,String binno, JdbcTemplate jdbctemplate) {
	int result= -1;
	
	///by gowtham
	/*String qury="select COUNT(1) from INSTPROD_DETAILS  where INST_ID='"+instid+"' AND SUB_PROD_ID='"+binno+"' AND AUTH_STATUS = '1' ORDER BY SUB_PROD_ID ";
	result =jdbctemplate.queryForInt(qury);*/ 
	
	String qury="select COUNT(1) from INSTPROD_DETAILS  where INST_ID=? AND SUB_PROD_ID=? AND AUTH_STATUS = ? ORDER BY SUB_PROD_ID ";
	result =jdbctemplate.queryForInt(qury,new Object[]{instid,binno,"1",});
	return result;
}

public List<String>  getSubProductList(String instid, JdbcTemplate jdbctemplate) {
	
	List result=null;
	
	/*String qury="select SUB_PROD_ID from INSTPROD_DETAILS  where INST_ID='"+instid+"' AND "
	+ "AUTH_STATUS = '1' ORDER BY SUB_PROD_ID ";
	result =jdbctemplate.queryForList(qury);*/
	
	///by gowtham
	String qury="select SUB_PROD_ID from INSTPROD_DETAILS  where INST_ID=? AND "
			+ "AUTH_STATUS = ? ORDER BY SUB_PROD_ID ";
			result =jdbctemplate.queryForList(qury,new Object[]{instid,"1"});
	
	List getSubProdAList = new ArrayList<>();
    Iterator bitr = result.iterator();
	while(bitr.hasNext()) {      
		 Map map = (Map)bitr.next();
		 getSubProdAList.add((String)map.get("SUB_PROD_ID"));
	}
	return getSubProdAList;
	
}

public List<String>  getBranchList(String instid, JdbcTemplate jdbctemplate) {
	List result=null;
	
	/*String qury="select BRANCH_CODE from BRANCH_MASTER  where INST_ID='"+instid+"' AND"
	+ " AUTH_CODE = '1' AND BRANCH_CODE!='000' ORDER BY BRANCH_CODE";
	enctrace("getBranchList::"+qury);
	result =jdbctemplate.queryForList(qury); */
	
	//by gowtham
	String qury="select BRANCH_CODE from BRANCH_MASTER  where INST_ID=? AND"
			+ " AUTH_CODE = ? AND BRANCH_CODE!=? ORDER BY BRANCH_CODE";
			enctrace("getBranchList::"+qury);
			result =jdbctemplate.queryForList(qury,new Object[]{instid,"1","000"}); 
	
	List getBranchAList = new ArrayList<>();
    Iterator bitr = result.iterator();
	while(bitr.hasNext()) {      
		 Map map = (Map)bitr.next();
		 getBranchAList.add((String)map.get("BRANCH_CODE"));
	}
	return getBranchAList;
}



public List<String> getAccountList(String instid, JdbcTemplate jdbctemplate) {
	List result=null;
	
	/*String qury="select ACCOUNTNO from ACCOUNTINFO  where INST_ID='"+instid+"' ORDER BY ACCOUNTNO ";
	result =jdbctemplate.queryForList(qury);*/
	
	//by gowtham
	String qury="select ACCOUNTNO from ACCOUNTINFO  where INST_ID=? ORDER BY ACCOUNTNO ";
	result =jdbctemplate.queryForList(qury,new Object[]{instid});
	
	List getAccAList = new ArrayList<>();
    Iterator bitr = result.iterator();
	while(bitr.hasNext()) {      
		 Map map = (Map)bitr.next();
		 getAccAList.add((String)map.get("ACCOUNTNO"));
		}
	return getAccAList;
	
}

public List<String> getAccountTypeList(String instid, JdbcTemplate jdbctemplate) {
	List result=null;
	
	/*String qury="select ACCTTYPEID from ACCTTYPE  where INST_ID='"+instid+"' AND AUTH_CODE = '1' ORDER BY ACCTTYPEID ";
	result =jdbctemplate.queryForList(qury); */
	
	///by gowtham
	String qury="select ACCTTYPEID from ACCTTYPE  where INST_ID=? AND AUTH_CODE = ? ORDER BY ACCTTYPEID ";
	result =jdbctemplate.queryForList(qury,new Object[]{instid,"1"}); 
	
	List getAccTypeAList = new ArrayList<>();
    Iterator bitr = result.iterator();
	while(bitr.hasNext()) {      
		 Map map = (Map)bitr.next();
		 getAccTypeAList.add((String)map.get("ACCTTYPEID"));
		
	}
	return getAccTypeAList;
}  

public List<String> getAccountSubTypeList(String instid, JdbcTemplate jdbctemplate) {
	List result=null;
	
	/*String qury="select ACCTSUBTYPEID from ACCTSUBTYPE  where  INST_ID='"+instid+"' ORDER BY ACCTSUBTYPEID ";
	result =jdbctemplate.queryForList(qury); */
	
	String qury="select ACCTSUBTYPEID from ACCTSUBTYPE  where  INST_ID=? ORDER BY ACCTSUBTYPEID ";
	result =jdbctemplate.queryForList(qury,new Object[]{instid}); 
	
	List getList = new ArrayList<>();
    Iterator bitr = result.iterator();
	while(bitr.hasNext()) {      
		 Map map = (Map)bitr.next();
		 getList.add((String)map.get("ACCTSUBTYPEID"));
		
	}
	return getList;
}  

public List<String> getAccountCcyList(JdbcTemplate jdbctemplate) {
	List result=null;
	
	/*String qury="select NUMERIC_CODE from GLOBAL_CURRENCY  WHERE AUTH_CODE='1' ORDER BY NUMERIC_CODE ";
	result =jdbctemplate.queryForList(qury); */
	
	String qury="select NUMERIC_CODE from GLOBAL_CURRENCY  WHERE AUTH_CODE=? ORDER BY NUMERIC_CODE ";
	result =jdbctemplate.queryForList(qury,new Object[]{"1"}); 
	
	List getList = new ArrayList<>();
    Iterator bitr = result.iterator();
	while(bitr.hasNext()) {      
		 Map map = (Map)bitr.next();
		 getList.add((String)map.get("NUMERIC_CODE"));
		
	}
	return getList;
	
}

public List<String> getCustIdList(String instid,JdbcTemplate jdbctemplate) {
	List result=null;
	
	/*String qury="select CIN from ACCOUNTINFO  where INST_ID='"+instid+"' ORDER BY CIN ";
	result =jdbctemplate.queryForList(qury); */
	
	///by gowtham\
	String qury="select CIN from ACCOUNTINFO  where INST_ID=? ORDER BY CIN ";
	result =jdbctemplate.queryForList(qury,new Object[]{instid}); 
	
	List getList = new ArrayList<>();
    Iterator bitr = result.iterator();
	while(bitr.hasNext()) {      
		 Map map = (Map)bitr.next();
		 getList.add((String)map.get("CIN"));
		
	}
	return getList;
}


public String getFailRegStatusQry(String instid,String fileName,int iterat,String resoncode,String usercode)
{
StringBuilder failStatus = new StringBuilder();
failStatus.append("INSERT INTO BULKFAIL_REG_STATUS ");
failStatus.append("(INST_ID, FILENAME, LINE_NO, REASON, ADDED_BY, ADDED_DATE , FAIL_REJECT)");
failStatus.append("VALUES");
failStatus.append("('"+instid+"', '"+fileName+"', '"+iterat+"', '"+resoncode+"', '"+usercode+"', sysdate,'F')");
enctrace("failStatus"+failStatus.toString());
return failStatus.toString();
}






public List getUploadStatus(String instid, String uploadFileFileName,JdbcTemplate jdbctemplate) {
	List result= null;
	trace("Getting getUploadStatus ");
	StringBuilder qury = new StringBuilder();
	qury.append("select INST_ID, FILENAME, TO_CHAR(UPLOADDATE,'DD-MM-YYYY')UPLOADDATE, (SELECT USERNAME FROM USER_DETAILS WHERE USERID in ");
	qury.append("(SELECT UPLOADEDBY FROM BULK_REG_STATUS where FILENAME='"+uploadFileFileName+"' )) UPLOADEDBY, STATUS, TATAL_RECORD, SUCCESS, FAIL, REJECT_REC from "); 
	qury.append("BULK_REG_STATUS where filename='"+uploadFileFileName+"' AND inst_id='"+instid+"' AND FILENAME = '"+uploadFileFileName+"'" );
	enctrace("Getting ailure Line data : " + qury); 
	result =jdbctemplate.queryForList(qury.toString()); 
	return result;
	
}


public int getBulkfilenameCount(String instid, String uploadFileFileName,JdbcTemplate jdbctemplate) {
	int result= -1;
	
	trace("Getting getBulkfilenameCount ");
	String qury="select COUNT(1) from BULK_REG_STATUS where inst_id='"+instid+"' AND FILENAME = '"+uploadFileFileName+"'";
	enctrace("getBulkfilenameCount : " + qury); 
	result =jdbctemplate.queryForInt(qury);     
	return result;
	
}


}
