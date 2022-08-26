package com.ifg.usermgt;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;

import test.Date;

public class userManagementActionDAO  extends BaseAction{


	public int deleteProfileFromProduction( String instid, String profileid, JdbcTemplate jdbctemplate ) throws Exception {
		int x= -1;
		
		/*String profilemoveqry = "DELETE FROM "+getProfilelistMain()+" WHERE INSTID='"+instid+"' AND PROFILE_ID='"+profileid+"'";
		enctrace("profilemoveqry :" + profilemoveqry);
		x = jdbctemplate.update(profilemoveqry);*/
		
		//by gowtham140819
		String profilemoveqry = "DELETE FROM "+getProfilelistMain()+" WHERE INSTID=? AND PROFILE_ID=?";
		enctrace("profilemoveqry :" + profilemoveqry);
		x = jdbctemplate.update(profilemoveqry,new Object[]{instid,profileid});
		
		return x;
	} 
	
	public int updateDeletedStatusToTemp( String instid, String profileid, String authcode, String usercode, String remarks, JdbcTemplate jdbctemplate ) throws Exception {
		int x= -1;
		
		Date date =  new Date();
		
		/*String profilemoveqry = "UPDATE "+getProfilelistTemp()+" SET DELETED_FLAG='2',ADDED_BY='"+usercode+"',AUTH_CODE='"+authcode+"', ADDED_DATE=SYSDATE, REMARKS='"+remarks+"', AUTH_BY='', AUTH_DATE=''  WHERE INSTID='"+instid+"' AND PROFILE_ID='"+profileid+"'";
		enctrace("profilemoveqry :" + profilemoveqry);
		x = jdbctemplate.update(profilemoveqry);*/
		
		//by gowtham-140819
		String profilemoveqry = "UPDATE "+getProfilelistTemp()+" SET DELETED_FLAG=?,ADDED_BY=?,AUTH_CODE=?, ADDED_DATE=?, REMARKS=?, AUTH_BY=?, AUTH_DATE=?  WHERE INSTID=?AND PROFILE_ID=?";
		enctrace("profilemoveqry123445 :" + profilemoveqry);
		x = jdbctemplate.update(profilemoveqry,new Object[]{"2",usercode,authcode,date.getCurrentDate(),remarks,"","",instid,profileid});
		
		return x;
	} 		

	
	public int updateDeletedAuthStatus( String instid, String profileid, String deletedflag, String authcode, String usercode, String remarks, JdbcTemplate jdbctemplate ) throws Exception {
		int x= -1;
		
		/*String profilemoveqry = "UPDATE "+getProfilelistTemp()+" SET DELETED_FLAG='"+deletedflag+"',AUTH_BY='"+usercode+"', AUTH_CODE='"+authcode+"', AUTH_DATE=SYSDATE, REMARKS='"+remarks+"' WHERE INSTID='"+instid+"' AND PROFILE_ID='"+profileid+"'";
		enctrace("profilemoveqry :" + profilemoveqry);
		x = jdbctemplate.update(profilemoveqry);*/
		Date date=new Date();
		//by gowtham140819
		String profilemoveqry = "UPDATE "+getProfilelistTemp()+" SET DELETED_FLAG=?,AUTH_BY=?, AUTH_CODE=?, AUTH_DATE=?, REMARKS=? WHERE INSTID=? AND PROFILE_ID=?";
		enctrace("profilemoveqry 123:" + profilemoveqry);
		x = jdbctemplate.update(profilemoveqry,new Object[]{deletedflag,usercode,authcode,date.getCurrentDate(),remarks,instid,profileid});
		
		return x;
	} 
	
	
	/*
	public int deleteProfileFromProduction( String instid, String profileid, JdbcTemplate jdbctemplate ) throws Exception {
		int x= -1;
		String profilemoveqry = "DELETE FROM "+getProfilelistMain()+" WHERE INSTID='"+instid+"' AND PROFILE_ID='"+profileid+"'";
		enctrace("profilemoveqry :" + profilemoveqry);
		x = jdbctemplate.update(profilemoveqry);
		return x;
	} 
	
	public int updateDeletedStatusToTemp( String instid, String profileid, String authcode, String usercode, String remarks, JdbcTemplate jdbctemplate ) throws Exception {
		int x= -1;
		String profilemoveqry = "UPDATE "+getProfilelistTemp()+" SET DELETED_FLAG='2',ADDED_BY='"+usercode+"',AUTH_CODE='"+authcode+"', ADDED_DATE=SYSDATE, REMARKS='"+remarks+"', AUTH_BY='', AUTH_DATE=''  WHERE INSTID='"+instid+"' AND PROFILE_ID='"+profileid+"'";
		enctrace("profilemoveqry :" + profilemoveqry);
		x = jdbctemplate.update(profilemoveqry);
		return x;
	} 

	
	public int updateDeletedAuthStatus( String instid, String profileid, String deletedflag, String authcode, String usercode, String remarks, JdbcTemplate jdbctemplate ) throws Exception {
		int x= -1;
		String profilemoveqry = "UPDATE "+getProfilelistTemp()+" SET DELETED_FLAG='"+deletedflag+"',AUTH_BY='"+usercode+"', AUTH_CODE='"+authcode+"', AUTH_DATE=SYSDATE, REMARKS='"+remarks+"' WHERE INSTID='"+instid+"' AND PROFILE_ID='"+profileid+"'";
		enctrace("profilemoveqry :" + profilemoveqry);
		x = jdbctemplate.update(profilemoveqry);
		return x;
	} 
	*/
	public int deleteProfilePrivilageFromProduct( String instid, String profileid, JdbcTemplate jdbctemplate ) throws Exception {
		int x= -1;
		String profilemoveqry = "DELETE FROM "+getPROFILE_PRIVILEGE()+" WHERE INST_ID='"+instid+"' AND PROFILE_ID='"+profileid+"'";
		enctrace("profilemoveqry :" + profilemoveqry);
		x = jdbctemplate.update(profilemoveqry);
		return x;
	} 
	 
	public int moveProfileToProduction( String instid, String profileid, JdbcTemplate jdbctemplate ) throws Exception {
		int x= -1;
		String profilemoveqry = "INSERT INTO "+getProfilelistMain()+" SELECT * FROM "+getProfilelistTemp()+" WHERE INSTID='"+instid+"' AND PROFILE_ID='"+profileid+"'";
		enctrace("profilemoveqry :" + profilemoveqry);
		x = jdbctemplate.update(profilemoveqry);
		return x;
	}    
	
	public int moveProfilePrivtoProduction( String instid, String profileid, JdbcTemplate jdbctemplate ) throws Exception {
		int x= -1;
		String profilemoveqry = "INSERT INTO "+getPROFILE_PRIVILEGE()+" SELECT * FROM "+getPROFILE_PRIVILEGE_TEMP()+" WHERE INST_ID='"+instid+"' AND PROFILE_ID='"+profileid+"'";
		enctrace("profilemoveqry :" + profilemoveqry);
		x = jdbctemplate.update(profilemoveqry);
		return x;
	}  
	
	 

	public int deleteFromProduction( String instid, String instidcond, String usercode, JdbcTemplate jdbctemplate ) throws Exception {
		int x= -1;
		String profilemoveqry = "DELETE FROM USER_DETAILS WHERE INSTID='"+instid+"' AND  USERID='"+usercode+"' "+instidcond+"";
		enctrace("profilemoveqry :" + profilemoveqry );
		x = jdbctemplate.update(profilemoveqry);
		return x;

	}
	
	public int moveUserToProduction( String instid, String instidcond, String usercode, JdbcTemplate jdbctemplate ) throws Exception {
		int x= -1;
		String profilemoveqry = "INSERT INTO USER_DETAILS SELECT * FROM USER_DETAILS WHERE  INSTID='"+instid+"' AND USERID='"+usercode+"' "+instidcond+"";
		enctrace("profilemoveqry :" + profilemoveqry );
		x = jdbctemplate.update(profilemoveqry);
		return x;

	}

	/*public int updateUserAuthStatus( String instid, String instidcond, String usercode, String authstatus, String authusercode, String remarks, JdbcTemplate jdbctemplate ) throws Exception {
		int x= -1; 
		String profilemoveqry = "UPDATE USER_DETAILS SET AUTH_STATUS='"+authstatus+"', REMARKS='"+remarks+"', AUTH_BY='"+authusercode+"', AUTH_DATE=SYSDATE WHERE INSTID='"+instid+"' AND  USERID='"+usercode+"' "+instidcond+"";
		enctrace("profilemoveqry :" + profilemoveqry );
		x = jdbctemplate.update(profilemoveqry);
		return x;

	} */
	public int updateUserAuthStatus( String instid, String instidcond, String usercode, String authstatus, String authusercode, String remarks, JdbcTemplate jdbctemplate ) throws Exception {
		int x= -1; 
		
		Date date=new Date();
		/*String profilemoveqry = "UPDATE USER_DETAILS SET AUTH_STATUS='"+authstatus+"', REMARKS='"+remarks+"', AUTH_BY='"+authusercode+"', AUTH_DATE=SYSDATE WHERE INSTID='"+instid+"' AND  USERID='"+usercode+"' "+instidcond+"";
		enctrace("profilemoveqry :" + profilemoveqry );
		x = jdbctemplate.update(profilemoveqry);
*/
		//by gowtham140819
		String profilemoveqry = "UPDATE USER_DETAILS SET AUTH_STATUS=?, REMARKS=?, AUTH_BY=?, AUTH_DATE=? WHERE INSTID=? AND  USERID='"+usercode+"' "+instidcond+"";
		enctrace("profilemoveqry :" + profilemoveqry );
		x = jdbctemplate.update(profilemoveqry,new Object[]{authstatus,remarks,authusercode,date.getCurrentDate(),instid,});
		
		return x;

	} 
	 
}
