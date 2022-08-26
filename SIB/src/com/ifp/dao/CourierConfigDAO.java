package com.ifp.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;

public class CourierConfigDAO extends BaseAction {

	/**
	 * Author : john
	 */
	private static final long serialVersionUID = 1L;

	public int insertCourierData( String instid, String couriername, String courieroffice, String contactnumber, String cstatus, JdbcTemplate jdbctemplate ) throws Exception {
		 int x = -1;
		 String insertcourier = "INSERT INTO IFP_COURIER_MASTER (INST_ID, COURIERMASTER_ID, COURIER_NAME, COURIER_HOFFICE, COURIER_CONTACTNO, COURIER_STATUS ) VALUES ('"+instid+"',IFC_REFERENCESEQ.nextval,'"+couriername+"','"+courieroffice+"','"+contactnumber+"','"+cstatus+"')";
		 enctrace("insertcourier :"+ insertcourier  );
		 x = jdbctemplate.update(insertcourier);
		 return x;
	}
	
	
	public int updateCourierDetails( String instid, String courierid, String couriername, String courieroffice, String contactnumber, String cstatus, JdbcTemplate jdbctemplate ) throws Exception {
		 int x = -1;
		 String insertcourier = "UPDATE IFP_COURIER_MASTER SET COURIER_NAME='"+couriername+"', COURIER_HOFFICE='"+courieroffice+"', COURIER_CONTACTNO ='"+contactnumber+"', COURIER_STATUS='"+cstatus+"' WHERE INST_ID='"+instid+"' AND COURIERMASTER_ID='"+courierid+"' ";
		 enctrace("insertcourier :"+ insertcourier  );
		 x = jdbctemplate.update(insertcourier);
		 return x;
	}
	
	public List getCourierList( String instid, String courierid, JdbcTemplate jdbctemplate ) throws Exception {
		List clist = null;
		String clistqry = "SELECT * FROM IFP_COURIER_MASTER WHERE INST_ID='"+instid+"' AND COURIERMASTER_ID='"+courierid+"' ";
		enctrace("clistqry :"+clistqry );
		clist = jdbctemplate.queryForList(clistqry);
		return clist;
	}
	
	
	public List getAllCourierList( String instid,  JdbcTemplate jdbctemplate ) throws Exception {
		List clist = null;
		String clistqry = "SELECT INST_ID, COURIERMASTER_ID, COURIER_NAME, COURIER_HOFFICE, COURIER_CONTACTNO, DECODE(COURIER_STATUS,'1','Active','0','In-Active',COURIER_STATUS) AS COURIER_STATUS FROM IFP_COURIER_MASTER WHERE INST_ID='"+instid+"'";
		enctrace("clistqry :"+clistqry );
		clist = jdbctemplate.queryForList(clistqry);
		return clist;
	}
	
}
