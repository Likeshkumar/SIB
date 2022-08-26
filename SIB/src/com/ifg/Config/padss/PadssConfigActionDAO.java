package com.ifg.Config.padss;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;

public class PadssConfigActionDAO extends BaseAction {

	
	
	/*
	public List getPadssConfigByUsername(String usercode,JdbcTemplate jdbctemplate) {
		String qury="select KEYID,KEYDESC from PADSSKEY where ADDED_BY!='"+usercode+"' and AUTH_CODE='0' order by KEYID";
		enctrace("Query getting PADSSKEY Details for auth/deauth .."+qury);
		List seqdetailslist =jdbctemplate.queryForList(qury);
		return seqdetailslist;
	}
	
	public List getPadssConfigByKEYID(String keyid,JdbcTemplate jdbctemplate) {
		
		String qury="select KEYID, KEYDESC, SUBSTR(DMK_KVC,0,6)DMK_KVC,  SUBSTR(DPK_KVC,0,6) DPK_KVC, SALT_KEY, KEY_LENGTH,DECODE(AUTH_CODE,'0','Waiting for authorization','1','Authorized','9','Rejected') AS AUTH_CODE,nvl(to_char(AUTH_DATE,'dd-mm-yyyy'),'--') as AUTH_DATE,NVL(AUTH_BY,'--') AS AUTH_BY,NVL(ADDED_BY,'--') AS ADDED_BY,nvl(to_char(ADDED_DATE,'dd-mm-yyyy'),'--') as ADDED_DATE,NVL(REMARKS,'--') AS REMARKS from PADSSKEY where KEYID='"+keyid+"'";
		enctrace("getPadssConfigByKEYID:::"+qury);
		List keydetail =jdbctemplate.queryForList(qury);
		return keydetail;
	}  
	
public List getPadssConfig(JdbcTemplate jdbctemplate) {
		
		String qury="select KEYID, KEYDESC, SUBSTR(DMK_KVC,0,6)DMK_KVC,  SUBSTR(DPK_KVC,0,6) DPK_KVC, SALT_KEY, KEY_LENGTH,DECODE(AUTH_CODE,'0','Waiting for authorization','1','Authorized','9','Rejected') AS AUTH_CODE,nvl(to_char(AUTH_DATE,'dd-mm-yyyy'),'--') as AUTH_DATE,NVL(AUTH_BY,'--') AS AUTH_BY,NVL(ADDED_BY,'--') AS ADDED_BY,nvl(to_char(ADDED_DATE,'dd-mm-yyyy'),'--') as ADDED_DATE,NVL(REMARKS,'--') AS REMARKS from PADSSKEY ";
		enctrace("getPadssConfig:::"+qury);
		List keydetail =jdbctemplate.queryForList(qury);
		return keydetail;
	}  
	
	public int updateAuthKey(String userid,String keyid,JdbcTemplate jdbctemplate) {
		String update_authdeauth_qury = "UPDATE PADSSKEY SET AUTH_CODE='1',AUTH_DATE=sysdate,AUTH_BY='"+userid+"' WHERE KEYID='"+keyid+"'";
		enctrace("update to Authorize key .. "+update_authdeauth_qury);
		int update_authdeauth = jdbctemplate.update(update_authdeauth_qury);
		return update_authdeauth;
	}
	
	public int updateDeAuthKey(String userid,String remarks,String keyid,JdbcTemplate jdbctemplate) {
		String update_authdeauth_qury = "UPDATE PADSSKEY SET AUTH_CODE='9',AUTH_DATE=sysdate,AUTH_BY='"+userid+"',REMARKS='"+remarks+"' where KEYID='"+keyid+"'";
		enctrace("update to Authorize key .. "+update_authdeauth_qury);
		int update_authdeauth = jdbctemplate.update(update_authdeauth_qury);
		return update_authdeauth;
	}
	

public List getPadssConfigByKEYID1(String keyid,JdbcTemplate jdbctemplate) {
		
		String qury="select KEYID, KEYDESC, SUBSTR(DMK_KVC,0,6)DMK_KVC,  SUBSTR(DPK_KVC,0,6) DPK_KVC, SALT_KEY, KEY_LENGTH,DECODE(AUTH_CODE,'2','Waiting for authorization','1','Authorized','9','Rejected') AS AUTH_CODE,nvl(to_char(AUTH_DATE,'dd-mm-yyyy'),'--') as AUTH_DATE,NVL(AUTH_BY,'--') AS AUTH_BY,NVL(ADDED_BY,'--') AS ADDED_BY,nvl(to_char(ADDED_DATE,'dd-mm-yyyy'),'--') as ADDED_DATE,NVL(REMARKS,'--') AS REMARKS from PADSSKEY where KEYID='"+keyid+"'";
		enctrace("getPadssConfigByKEYID:::"+qury);
		List keydetail =jdbctemplate.queryForList(qury);
		return keydetail;
	}  




// by siva 22-07-19
	public List getPadssConfigByUsername1(String usercode,JdbcTemplate jdbctemplate) {
		String qury="select KEYID,KEYDESC from PADSSKEY where ADDED_BY!='"+usercode+"' and AUTH_CODE='2' order by KEYID";
		enctrace("Query getting PADSSKEY Details for auth/deauth .."+qury);
		List seqdetailslist =jdbctemplate.queryForList(qury);
		return seqdetailslist;
	}
	*/

	
	//------------------by gowtham030919-----------------------------
	
	
	
	public List getPadssConfigByUsername(String usercode,JdbcTemplate jdbctemplate) {
		
		/*String qury="select KEYID,KEYDESC from PADSSKEY where ADDED_BY!='"+usercode+"' and AUTH_CODE='0' order by KEYID";
		enctrace("Query getting PADSSKEY Details for auth/deauth .."+qury);
		List seqdetailslist =jdbctemplate.queryForList(qury);*/
		
		///by gowtham
		String qury="select KEYID,KEYDESC from PADSSKEY where ADDED_BY!=? and AUTH_CODE=? order by KEYID";
		enctrace("Query getting PADSSKEY Details for auth/deauth .."+qury);
		List seqdetailslist =jdbctemplate.queryForList(qury,new Object[]{usercode,"0"});
		
		return seqdetailslist;
	}
	
	public List getPadssConfigByKEYID(String keyid,JdbcTemplate jdbctemplate) {
		
		
		/*String qury="select KEYID, KEYDESC, SUBSTR(DMK_KVC,0,6)DMK_KVC,  SUBSTR(DPK_KVC,0,6) DPK_KVC, SALT_KEY, KEY_LENGTH,DECODE(AUTH_CODE,'0','Waiting for authorization','1','Authorized','9','Rejected') AS AUTH_CODE,nvl(to_char(AUTH_DATE,'dd-mm-yyyy'),'--') as AUTH_DATE,NVL(AUTH_BY,'--') AS AUTH_BY,NVL(ADDED_BY,'--') AS ADDED_BY,nvl(to_char(ADDED_DATE,'dd-mm-yyyy'),'--') as ADDED_DATE,NVL(REMARKS,'--') AS REMARKS from PADSSKEY where KEYID='"+keyid+"'";
		enctrace("getPadssConfigByKEYID:::"+qury);
		List keydetail =jdbctemplate.queryForList(qury);*/
		
		String qury="select KEYID, KEYDESC, SUBSTR(DMK_KVC,0,6)DMK_KVC,  SUBSTR(DPK_KVC,0,6) DPK_KVC, SALT_KEY, KEY_LENGTH,DECODE(AUTH_CODE,'0','Waiting for authorization','1','Authorized','9','Rejected') AS AUTH_CODE,nvl(to_char(AUTH_DATE,'dd-mm-yyyy'),'--') as AUTH_DATE,NVL(AUTH_BY,'--') AS AUTH_BY,NVL(ADDED_BY,'--') AS ADDED_BY,nvl(to_char(ADDED_DATE,'dd-mm-yyyy'),'--') as ADDED_DATE,NVL(REMARKS,'--') AS REMARKS from PADSSKEY where KEYID=? ";
		enctrace("getPadssConfigByKEYID:::"+qury);
		List keydetail =jdbctemplate.queryForList(qury,new Object[]{keyid});
		
		return keydetail;
	}  
	
public List getPadssConfig(JdbcTemplate jdbctemplate) {
		
		String qury="select KEYID, KEYDESC, SUBSTR(DMK_KVC,0,6)DMK_KVC,  SUBSTR(DPK_KVC,0,6) DPK_KVC, SALT_KEY, KEY_LENGTH,DECODE(AUTH_CODE,'0','Waiting for authorization','1','Authorized','9','Rejected') AS AUTH_CODE,nvl(to_char(AUTH_DATE,'dd-mm-yyyy'),'--') as AUTH_DATE,NVL(AUTH_BY,'--') AS AUTH_BY,NVL(ADDED_BY,'--') AS ADDED_BY,nvl(to_char(ADDED_DATE,'dd-mm-yyyy'),'--') as ADDED_DATE,NVL(REMARKS,'--') AS REMARKS from PADSSKEY ";
		enctrace("getPadssConfig:::"+qury);
		List keydetail =jdbctemplate.queryForList(qury);
		
		return keydetail;
	}  
	
	public int updateAuthKey(String userid,String keyid,JdbcTemplate jdbctemplate) {
		
		/*String update_authdeauth_qury = "UPDATE PADSSKEY SET AUTH_CODE='1',AUTH_DATE=sysdate,AUTH_BY='"+userid+"' WHERE KEYID='"+keyid+"'";
		enctrace("update to Authorize key .. "+update_authdeauth_qury);
		int update_authdeauth = jdbctemplate.update(update_authdeauth_qury);*/
		
		String update_authdeauth_qury = "UPDATE PADSSKEY SET AUTH_CODE=?,AUTH_DATE=sysdate,AUTH_BY=? WHERE KEYID=? ";
		enctrace("update to Authorize key .. "+update_authdeauth_qury);
		int update_authdeauth = jdbctemplate.update(update_authdeauth_qury,new Object[]{"1",userid,keyid});
		
		return update_authdeauth;
	}
	
	public int updateDeAuthKey(String userid,String remarks,String keyid,JdbcTemplate jdbctemplate) {
		String update_authdeauth_qury = "UPDATE PADSSKEY SET AUTH_CODE='9',AUTH_DATE=sysdate,AUTH_BY='"+userid+"',REMARKS='"+remarks+"' where KEYID='"+keyid+"'";
		enctrace("update to Authorize key .. "+update_authdeauth_qury);
		int update_authdeauth = jdbctemplate.update(update_authdeauth_qury);
		return update_authdeauth;
	}
	

public List getPadssConfigByKEYID1(String keyid,JdbcTemplate jdbctemplate) {
		
		/*String qury="select KEYID, KEYDESC, SUBSTR(DMK_KVC,0,6)DMK_KVC,  SUBSTR(DPK_KVC,0,6) DPK_KVC, SALT_KEY, KEY_LENGTH,DECODE(AUTH_CODE,'2','Waiting for authorization','1','Authorized','9','Rejected') AS AUTH_CODE,nvl(to_char(AUTH_DATE,'dd-mm-yyyy'),'--') as AUTH_DATE,NVL(AUTH_BY,'--') AS AUTH_BY,NVL(ADDED_BY,'--') AS ADDED_BY,nvl(to_char(ADDED_DATE,'dd-mm-yyyy'),'--') as ADDED_DATE,NVL(REMARKS,'--') AS REMARKS from PADSSKEY where KEYID='"+keyid+"'";
		enctrace("getPadssConfigByKEYID:::"+qury);
		List keydetail =jdbctemplate.queryForList(qury);*/
	
	String qury="select KEYID, KEYDESC, SUBSTR(DMK_KVC,0,6)DMK_KVC,  SUBSTR(DPK_KVC,0,6) DPK_KVC, SALT_KEY, KEY_LENGTH,DECODE(AUTH_CODE,'2','Waiting for authorization','1','Authorized','9','Rejected') AS AUTH_CODE,nvl(to_char(AUTH_DATE,'dd-mm-yyyy'),'--') as AUTH_DATE,NVL(AUTH_BY,'--') AS AUTH_BY,NVL(ADDED_BY,'--') AS ADDED_BY,nvl(to_char(ADDED_DATE,'dd-mm-yyyy'),'--') as ADDED_DATE,NVL(REMARKS,'--') AS REMARKS from PADSSKEY where KEYID=? ";
	enctrace("getPadssConfigByKEYID:::"+qury);
	List keydetail =jdbctemplate.queryForList(qury,new Object[]{keyid});
		
		return keydetail;
	}  




// by siva 22-07-19
	public List getPadssConfigByUsername1(String usercode,JdbcTemplate jdbctemplate) {
		
		/*String qury="select KEYID,KEYDESC from PADSSKEY where ADDED_BY!='"+usercode+"' and AUTH_CODE='2' order by KEYID";
		enctrace("Query getting PADSSKEY Details for auth/deauth .."+qury);
		List seqdetailslist =jdbctemplate.queryForList(qury);*/
		
		String qury="select KEYID,KEYDESC from PADSSKEY where ADDED_BY!=? and AUTH_CODE=? order by KEYID";
		enctrace("Query getting PADSSKEY Details for auth/deauth .."+qury);
		List seqdetailslist =jdbctemplate.queryForList(qury,new Object[]{usercode,"2"});
		
		return seqdetailslist;
	}
	
	
	
}
