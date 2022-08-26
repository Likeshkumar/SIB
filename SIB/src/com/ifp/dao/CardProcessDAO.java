package com.ifp.dao;

import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;

@Transactional
public class CardProcessDAO extends BaseAction {
	public int checkCardActivateEligibilty( String instid, String cardno, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String eligibleqry = "SELECT COUNT(*) AS CNT FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'  AND CARD_STATUS IN ('09','05')";
		System.out.println("eligibleqry ----> "+eligibleqry);
		x = jdbctemplate.queryForInt(eligibleqry);
		return x;
	}
	
	public int updateCardStatus(String instid, String cardno, String ifpcardstatus, String switchcardstatus, String phonenumber, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String updstatusqry = "UPDATE CARD_PRODUCTION SET CARD_STATUS='"+ifpcardstatus+"',STATUS_CODE='"+switchcardstatus+"',MOBILENO='"+phonenumber+"', ACTIVE_DATE=sysdate WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
		x = jdbctemplate.update(updstatusqry);
		return x;
	}
	
	
	public List getActivationCardList(String instid, String statuscode, String condition, JdbcTemplate jdbctemplate ) throws Exception {
		List cardlist = null;
		String activationqry = "SELECT CARD_NO, CIN, ORDER_REF_NO, to_char(ISSUE_DATE,'dd-mm-yyyy') as ISSUE_DATE, CARD_STATUS,  PRODUCT_CODE, SUB_PROD_ID, CARD_STATUS, EMB_NAME FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' "+condition;
		enctrace("activationqry  :"+ activationqry );
		cardlist = jdbctemplate.queryForList(activationqry);
		return cardlist;
	}

	public List getListOfCourieId(String instid, String statuscode, JdbcTemplate jdbctemplate) {
		List courierlist = null;
		String courierlistqry = "SELECT ( COURIER_NAME ||'  ' || COURIERID ) AS COURIERNAME, A.COURIERTRACKID AS COURIERTRACKID  FROM IFP_COURIER_TRACK A, IFP_COURIER_MASTER B WHERE A.INST_ID=B.INST_ID AND A.COURIERMASTER_ID=B.COURIERMASTER_ID AND A.INST_ID='"+instid+"' AND ( TO_NUMBER(NOOFCARDS) - TO_NUMBER(CURRENTLY_ACTIVATED) ) > 0";
		enctrace("courierlistqry  :"+ courierlistqry );
		courierlist  = jdbctemplate.queryForList(courierlistqry);
		return courierlist ;
	}
	
	public List getCourierDataList(String instid, String courierrefid, JdbcTemplate jdbctemplate ) throws Exception{
		List courierlist = null;
		String courierlistqry = "SELECT COURIER_NAME, COURIERID, NOOFCARDS, TO_CHAR(COURIERDATE,'DD-MM-YYYY') AS COURIERDATE, TOADDRESS, AGENTID, CURRENTLY_ACTIVATED , " ;
		courierlistqry += " TO_NUMBER(NOOFCARDS) - TO_NUMBER(CURRENTLY_ACTIVATED) AS PENDINGCARDS FROM IFP_COURIER_TRACK A, IFP_COURIER_MASTER B WHERE A.INST_ID=B.INST_ID  AND A.COURIERMASTER_ID=B.COURIERMASTER_ID AND A.INST_ID='"+instid+"' ";
		courierlistqry += " AND COURIERTRACKID ='"+courierrefid+"'" ;
		enctrace("courierlistqry :"+ courierlistqry );
		courierlist = jdbctemplate.queryForList(courierlistqry);
		return courierlist ;
	}
	
	
	
	public String getCourerDesc (String instid, String corierrefno, JdbcTemplate jdbctemplate) {
		String couriername= null;
		String courierlistqry = "SELECT COURIER_NAME ||'  ' || COURIERID FROM IFP_COURIER_TRACK A, IFP_COURIER_MASTER B WHERE A.INST_ID=B.INST_ID  AND A.COURIERMASTER_ID=B.COURIERMASTER_ID AND A.INST_ID='"+instid+"' AND A.COURIERTRACKID='"+corierrefno+"'";
		enctrace("courierlistqry  :"+ courierlistqry );
		try{
			couriername= (String)jdbctemplate.queryForObject(courierlistqry, String.class);
		}catch(EmptyResultDataAccessException e){}
		
		return couriername ;
	}
	
	public int activateCardProcess(String instid, String cardno, String cardstatus, String switchcardstatus, JdbcTemplate jdbctemplate ) throws Exception{
		int x = -1;
		String activationprocesqry = "UPDATE CARD_PRODUCTION SET CARD_STATUS='"+cardstatus+"', STATUS_CODE='"+switchcardstatus+"' WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
		enctrace("activationprocesqry :"+activationprocesqry);
		x = jdbctemplate.update(activationprocesqry);
		return x;
	}

	public int updateCourierActivationCount(String instid, String courierrefno, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String activationprocesqry = "UPDATE IFP_COURIER_TRACK SET CURRENTLY_ACTIVATED=CURRENTLY_ACTIVATED+1 WHERE INST_ID='"+instid+"' AND COURIERTRACKID='"+courierrefno+"'";
		enctrace("activationprocesqry :"+activationprocesqry);
		x = jdbctemplate.update(activationprocesqry);
		return x;
	}
}
