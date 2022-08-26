package com.ifp.dao;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;

public class pinMailerConfigurationActiondDAO extends BaseAction {
	
	public int createPinmailer(String in_name,JdbcTemplate jdbcTemplate) throws Exception { 
		int pinmailer_id = -1,maxcount = -1;
		
		/*String maxcnt = "SELECT COUNT(*) FROM PINMAILER_DESC WHERE INST_ID='"+in_name+"'";
		enctrace("maxcnt :" + maxcnt);
		maxcount = jdbcTemplate.queryForInt(maxcnt); */
		
		//by gowtham
		String maxcnt = "SELECT COUNT(*) FROM PINMAILER_DESC WHERE INST_ID=? ";
		enctrace("maxcnt :" + maxcnt);
		maxcount = jdbcTemplate.queryForInt(maxcnt,new Object[]{in_name}); 
		
		if(maxcount == 0){
			pinmailer_id = 1;
		}else {
			
			/*String pinmaxcount = "SELECT MAX(PINMAILER_ID)+1 FROM PINMAILER_DESC WHERE INST_ID='"+in_name+"'";
			enctrace("pinmaxcount : "+pinmaxcount);
			pinmailer_id = jdbcTemplate.queryForInt(pinmaxcount);*/

			///by gowtham
			String pinmaxcount = "SELECT MAX(PINMAILER_ID)+1 FROM PINMAILER_DESC WHERE INST_ID=? ";
			enctrace("pinmaxcount : "+pinmaxcount);
			pinmailer_id = jdbcTemplate.queryForInt(pinmaxcount,new Object[]{in_name});
			
			System.out.println("pinmailer_id--> "+pinmailer_id);
		}

		return pinmailer_id;
	}
	
	
	public int checkPinmailernameexist(String in_name,String pname,JdbcTemplate jdbcTemplate) throws Exception {
		int namecnt = -1;
		
		/*String qury="select count(*) from PINMAILER_DESC where PINMAILER_NAME='"+pname+"' and INST_ID='"+in_name+"'";
		enctrace("qury--> "+qury);
		namecnt = jdbcTemplate.queryForInt(qury); */
//by gowtham
		String qury="select count(*) from PINMAILER_DESC where PINMAILER_NAME=? and INST_ID=? ";
		enctrace("qury--> "+qury);
		namecnt = jdbcTemplate.queryForInt(qury,new Object[]{pname,in_name}); 
		
		
		return namecnt;
	}
	
	public int moveMailerDescToProduction( String instid, String mailerid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String movemailerqry = "INSERT INTO PINMAILER_DESC SELECT * FROM PINMAILER_DESC WHERE INST_ID='"+instid+"' AND PINMAILER_ID='"+mailerid+"' ";
		enctrace("movemailerqry :" + movemailerqry);
		x = jdbctemplate.update(movemailerqry);
		return x;
	}
	
	public int deleteFromMailerProduction ( String instid, String mailerid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		
	/*	String movemailerqry = "DELETE FROM PINMAILER_DESC  WHERE INST_ID='"+instid+"' AND "
		+ "PINMAILER_ID='"+mailerid+"' ";
		enctrace("movemailerqry :" + movemailerqry);
		x = jdbctemplate.update(movemailerqry);*/

		//by gowtham
		String movemailerqry = "DELETE FROM PINMAILER_DESC  WHERE INST_ID=? AND "+ "PINMAILER_ID=? ";
				enctrace("movemailerqry :" + movemailerqry);
				x = jdbctemplate.update(movemailerqry,new Object[]{instid,mailerid});
		return x;
	}
	
	public int moveMailerPropertiesToProduction( String instid, String mailerid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		
		/*String movemailerqry = "INSERT INTO PINMAILER_PROPERTY SELECT * FROM PINMAILER_PROPERTY"
		+ " WHERE INST_ID='"+instid+"' AND PINMAILER_ID='"+mailerid+"' ";
		enctrace("movemailerqry :" + movemailerqry);
		x = jdbctemplate.update(movemailerqry);*/
		
		///by gowtham
		String movemailerqry = "INSERT INTO PINMAILER_PROPERTY SELECT * FROM PINMAILER_PROPERTY"
				+ " WHERE INST_ID=? AND PINMAILER_ID=? ";
				enctrace("movemailerqry :" + movemailerqry);
				x = jdbctemplate.update(movemailerqry,new Object[]{instid,mailerid});
		
		return x;
		
	}
	
	public int deleteFromMailerPropertiesProduction ( String instid, String mailerid, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		
		/*String movemailerqry = "DELETE FROM PINMAILER_PROPERTY WHERE INST_ID='"+instid+"'"
		+ " AND PINMAILER_ID='"+mailerid+"' ";
		enctrace("movemailerqry :" + movemailerqry);
		x = jdbctemplate.update(movemailerqry);*/
		
		///byg owtham
		String movemailerqry = "DELETE FROM PINMAILER_PROPERTY WHERE INST_ID=? "
				+ " AND PINMAILER_ID=? ";
				enctrace("movemailerqry :" + movemailerqry);
				x = jdbctemplate.update(movemailerqry,new Object[]{instid,mailerid});
		
		return x;
	}
	
	public int updateMailerDetails ( String instid, String mailerid, String reason, String usercode, String status, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		
		/*String updatemailerqry = "UPDATE PINMAILER_DESC SET AUTH_BY='"+usercode+"', AUTH_DATE=SYSDATE,"
		+ "STATUS='"+status+"', REMARKS='"+reason+"' WHERE INST_ID='"+instid+"' AND PINMAILER_ID='"+mailerid+"' ";
		enctrace("updatemailerqry:" + updatemailerqry);
		x = jdbctemplate.update(updatemailerqry);*/
		
		//by gowtham
		String updatemailerqry = "UPDATE PINMAILER_DESC SET AUTH_BY=?, AUTH_DATE=SYSDATE,"
				+ "STATUS=?, REMARKS=? WHERE INST_ID=? AND PINMAILER_ID=? ";
				enctrace("updatemailerqry:" + updatemailerqry);
				x = jdbctemplate.update(updatemailerqry,new Object[]{usercode,status,reason,instid,mailerid});
		
		return x;
	}
	
	public List getMailerDescFromTemp(String instid, String mailerid, JdbcTemplate jdbctemplate ) throws Exception {
		List mailerdata = null;
		
		/*String pinmailer_data = "select FIELD_NAME,FIELD_LENGTH,X_POS,Y_POS,DECODE(PRINT_REQUIRED,'Y','YES') AS "
		+ "PRINT_REQUIRED from PINMAILER_PROPERTY where PINMAILER_ID='"+mailerid+"' and inst_id='"+instid+"'";
		enctrace("pinmailer_data :" + pinmailer_data);
		mailerdata = jdbctemplate.queryForList(pinmailer_data);*/

		//by gowtham
		String pinmailer_data = "select FIELD_NAME,FIELD_LENGTH,X_POS,Y_POS,DECODE(PRINT_REQUIRED,'Y','YES') AS "
				+ "PRINT_REQUIRED from PINMAILER_PROPERTY where PINMAILER_ID=? and inst_id=? ";
				enctrace("pinmailer_data :" + pinmailer_data);
				mailerdata = jdbctemplate.queryForList(pinmailer_data,new Object[]{mailerid,instid});
		
		return mailerdata;		
	}
	
	public List getMailerIdDistinct( String instid, String mailerid, JdbcTemplate jdbctemplate ) throws Exception {
		List mailerdata = null;
		
		/*String pins_data = "SELECT D"
		+ "ISTINCT PINMAILER_ID,DECODE(DOCUMENT_TYPE,'A','FORM-1','B','FORM-2','C','FORM-3') AS "
		+ "DOCUMENT_TYPE,MAILER_HEIGHT,MAILER_LENGHT FROM PINMAILER_PROPERTY WHERE inst_id='"+instid+"'"
		+ " AND PINMAILER_ID ='"+mailerid+"'";
		enctrace("pins_data :" + pins_data);
		mailerdata = jdbctemplate.queryForList(pins_data);*/
		
		//by gowtham
		String pins_data = "SELECT D"
				+ "ISTINCT PINMAILER_ID,DECODE(DOCUMENT_TYPE,'A','FORM-1','B','FORM-2','C','FORM-3') AS "
				+ "DOCUMENT_TYPE,MAILER_HEIGHT,MAILER_LENGHT FROM PINMAILER_PROPERTY WHERE inst_id=?"
				+ " AND PINMAILER_ID =? ";
				enctrace("pins_data :" + pins_data);
				mailerdata = jdbctemplate.queryForList(pins_data,new Object[]{instid,mailerid});
		
		return mailerdata;		
	}
	
	public List getMailerProperties( String instid, String mailerid, JdbcTemplate jdbctemplate ) throws Exception {
		List mailerdata = null;
		
		/*String pinmailer_name = "SELECT PINMAILER_NAME, ADDED_BY,AUTH_BY,STATUS, TO_CHAR(ADDED_DATE,'DD-MM-YYYY') AS "
		+ "ADDED_DATE, TO_CHAR(AUTH_DATE,'DD-MM-YYYY') AS AUTH_DATE, REMARKS FROM PINMAILER_DESC"
		+ " WHERE INST_ID='"+instid+"' AND PINMAILER_ID='"+mailerid+"'";
		enctrace("pinmailer_name :" + pinmailer_name);
		mailerdata = jdbctemplate.queryForList(pinmailer_name);
*/
		
		String pinmailer_name = "SELECT PINMAILER_NAME, ADDED_BY,AUTH_BY,STATUS, TO_CHAR(ADDED_DATE,'DD-MM-YYYY') AS "
				+ "ADDED_DATE, TO_CHAR(AUTH_DATE,'DD-MM-YYYY') AS AUTH_DATE, REMARKS FROM PINMAILER_DESC"
				+ " WHERE INST_ID=? AND PINMAILER_ID=?";
				enctrace("pinmailer_name :" + pinmailer_name);
				mailerdata = jdbctemplate.queryForList(pinmailer_name,new Object[]{instid,mailerid});

		
		return mailerdata;		
	}
	
	public int insertPinMailerDesc(String instid, String tablename, int pinmailerid, String pname, String authstatus,String mkckstatus, String usercode, JdbcTemplate jdbctemplate ) throws Exception {
		int x =-1;
		
		/*String pindesc = "INSERT INTO "+tablename+" (INST_ID,PINMAILER_ID,PINMAILER_NAME,STATUS,MKCK_STATUS,ADDED_BY,"
		+ "ADDED_DATE) "
		+ "VALUES('"+instid+"','"+pinmailerid+"','"+pname+"','"+authstatus+"','"+mkckstatus+"','"+usercode+"',SYSDATE)";
		enctrace("pindesc :"+pindesc);
		x = jdbctemplate.update(pindesc);*/
		
		///by gowtham
		String pindesc = "INSERT INTO "+tablename+" (INST_ID,PINMAILER_ID,PINMAILER_NAME,STATUS,MKCK_STATUS,ADDED_BY,"
		+ "ADDED_DATE) "
		+ "VALUES(?,?,?,?,?,?,SYSDATE)";
		enctrace("pindesc :"+pindesc);
		x = jdbctemplate.update(pindesc,new Object[]{instid,pinmailerid,pname,authstatus,mkckstatus,usercode});
		
		return x;
	}
	
	
	public String getPinMailerName( String instid, String mailerid, JdbcTemplate jdbctemplate ) throws Exception {
		String mailername = null;
		
		/*String pinmailer_name = "SELECT PINMAILER_NAME  FROM PINMAILER_DESC"
		+ " WHERE INST_ID='"+instid+"' AND PINMAILER_ID='"+mailerid+"'";
		enctrace("pinmailer_name :" + pinmailer_name);
		try{
			mailername = (String)jdbctemplate.queryForObject(pinmailer_name, String.class);*/

		//by gowtham
		String pinmailer_name = "SELECT PINMAILER_NAME  FROM PINMAILER_DESC"
				+ " WHERE INST_ID=?  AND PINMAILER_ID=? ";
				enctrace("pinmailer_name :" + pinmailer_name);
				try{
					mailername = (String)jdbctemplate.queryForObject(pinmailer_name,new Object[]{instid,mailerid}, String.class);
		
		}catch(EmptyResultDataAccessException e){}
		return mailername;		
	}
	
	
	public int insertPinMailerProperties(String instid,  String tablename, String doctype, String fname, String field_length, String x_pos, String y_pos, int pinmailerid, String pin_height, String pin_length, String parameters [], JdbcTemplate jdbctemplate ) throws Exception {
		int x =-1;
		
	    /* String pinmailerdata_insert = "INSERT INTO "+tablename+" (PINMAILER_ID,INST_ID, " +
		"DOCUMENT_TYPE, FIELD_NAME, FIELD_LENGTH, X_POS, Y_POS, PRINT_REQUIRED,MAILER_HEIGHT,MAILER_LENGHT) "
		+ "VALUES ('"+pinmailerid+"', " +"'"+instid+"', '"+doctype+"', '"+fname+"', '"+field_length+"',"
		+ " '"+x_pos+"', '"+y_pos+"', 'Y','"+pin_height+"','"+pin_length+"')";
		enctrace("pinmailerdata_insert.."+pinmailerdata_insert);
		x = jdbctemplate.update(pinmailerdata_insert);*/
		
		////by gowtham-
		 String pinmailerdata_insert = "INSERT INTO "+tablename+" (PINMAILER_ID,INST_ID, " +
					"DOCUMENT_TYPE, FIELD_NAME, FIELD_LENGTH, X_POS, Y_POS, PRINT_REQUIRED,MAILER_HEIGHT,MAILER_LENGHT) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?)";
					enctrace("pinmailerdata_insert.."+pinmailerdata_insert);
					x = jdbctemplate.update(pinmailerdata_insert,new Object[]{pinmailerid,instid,doctype,fname,field_length,x_pos,y_pos, "Y",pin_height,pin_length});
		
		return x;
	}
	 
}
