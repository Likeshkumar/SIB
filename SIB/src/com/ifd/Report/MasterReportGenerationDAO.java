package com.ifd.Report;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ifd.beans.ActiveCardDTO;
import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;

public class MasterReportGenerationDAO extends BaseAction{
	
	
	
	
	public List getAuditActionList( String headactcode,String reptype, JdbcTemplate jdbctemplate ){
		List actionlist = null;
		String conditionqry = "";
		if(  !headactcode.equals("$ALL")){
			conditionqry = " WHERE REPORT_TYPE = '"+reptype+"' AND ACTION LIKE '"+headactcode+"%' AND ACTION!='ALL' ORDER BY ID ASC ";            
		}else{
			conditionqry = " WHERE REPORT_TYPE = '"+reptype+"' AND ACTION != '"+headactcode+"' ORDER BY ID ASC  ";      
		}
		
		String actinlistqry = "SELECT ACTION, ACTION_DESC FROM CARDREPORTACTION "+conditionqry;
		enctrace("cardActionlist  :"+ actinlistqry );
		actionlist = jdbctemplate.queryForList(actinlistqry);  
		return actionlist ;
	}      
	
	public List getActionList(JdbcTemplate jdbctemplate ){
		List actionlist = null;
		String actinlistqry = "select ACTION,ACTION_NAME from CARDREPORTACTION group by ID,ACTION,ACTION_NAME  order by ID ";
		enctrace("getActionList  :"+ actinlistqry );
		actionlist = jdbctemplate.queryForList(actinlistqry);  
		return actionlist ;
	}
	
	List getReportConfigDetails(String instid, String reportno,JdbcTemplate jdbctemplate) {
		List getrep = null;
		try {
			String getrepqry= "select REPORTNAME,PASSWORD_REQ,PAGENO_REQ,FOOTERCONTENT from PDFREPORTGENRATOR where RNO='"+reportno+"' ";
			enctrace("getrepqry : " + getrepqry );
			getrep = jdbctemplate.queryForList(getrepqry);
		} catch (EmptyResultDataAccessException e) { getrep=null; }
		return getrep;
	}
	
	
	
	public List getMasterReportList(String instid,String eDMK,String eDPK,PadssSecurity sec,String reportQuery,JdbcTemplate jdbctemplate ) throws Exception{
		List actionlist = null;
		trace("reportQuery111111::::::"+reportQuery);  
		
		String branchName="",org_chn="",orderRefNo="",mcardNo="",cin="",embName="";
		List cardList=new ArrayList();
		if(reportQuery==null)
		{
			//reportQuery = "SELECT 'TECHNICAL ISSUE' FROM DUAL";
		}
		else
		{          
			      
			try {
			actionlist = jdbctemplate.queryForList(reportQuery);
			
			//List<Map<String, Object>>  actionlist= jdbctemplate.queryForList(reportQuery);
			//System.out.println("printing the actionlist query " +reportQuery);
			trace("The Query Result is ==========>"+actionlist);
			
			/*for(Map<String, Object> row:actionlist){
				
			}*/
			
			
			/*while( itr.hasNext() ){
				Map temp = (Map)itr.next();
				String cardno = (String)temp.get("CARD_NO");
				temp.put("CARD_NO",sec.getCHN(eDMK, eDPK, cardno));          
				itr.remove();
				itr.add(temp);
			}*/
		}
		catch(Exception e)
		{
			trace("Eeptionnnnnn"+e);       
		}
		}
		enctrace("getMasterReportList  :"+ reportQuery );                
		System.out.println("getMasterReportList"+actionlist);
		//actionlist = jdbctemplate.queryForList(reportQuery);  
		return actionlist ;
	}
	
	
	
	
	
	public List<ActiveCardDTO>  getMasterReportList1(String instid,String eDMK,String eDPK,PadssSecurity sec,String reportQuery,JdbcTemplate jdbctemplate ) throws Exception{
		//List actionlist = null;
		List<Map<String, Object>>  rows=null;
		List<Map<String, Object>>  rowss=null;
		
		trace("reportQuery111111::::::"+reportQuery);  
		List accountlist=null;
		String customerid="", Primary_acctno="";
		List<ActiveCardDTO> cardList=new ArrayList<ActiveCardDTO>();
		if(reportQuery==null)
		{
			//reportQuery = "SELECT 'TECHNICAL ISSUE' FROM DUAL";
		}
		else
		{          
			      
			try {
			//actionlist = jdbctemplate.queryForList(reportQuery);
			
			  rows= jdbctemplate.queryForList(reportQuery);
			//System.out.println("printing the actionlist query " +reportQuery);
			
			
		     for(Map<String, Object> row:rows){
				ActiveCardDTO cardDto=new ActiveCardDTO();
				cardDto.setBranchName((String)row.get("BRANCH_NAME"));
				cardDto.setOrderRefNo((String)row.get("ORDER_REF_NO"));
				cardDto.setOrgChn((String)row.get("ORG_CHN"));
				cardDto.setMcardNo((String)row.get("MCARD_NO"));
				cardDto.setCin((String)row.get("CIN"));
				customerid=(String)row.get("CIN");
				trace("customerid-->"+customerid);
				cardDto.setEmbName((String)row.get("EMB_NAME"));
				cardDto.setAccountNo((String)row.get("ACCT_NO"));
				Primary_acctno=(String)row.get("ACCT_NO");
				trace("ACCT_NO-->"+Primary_acctno);
				cardDto.setMakerId((String)row.get("MAKER_ID"));
				cardDto.setCardType((String)row.get("CARD_TYPE"));
				cardDto.setBulkRefId((String)row.get("BULK_REG_ID"));
				cardDto.setCardFalg((String)row.get("CARD_FLAG"));
				//cardDto.setPcFlag((String)row.get("PC_FLAG"));
				cardDto.setIssueDate((row.get("ISSUE_DATE").toString()));
				//cardDto.setCardStatus((String)row.get("CARDSTATUS"));
				cardDto.setpaccountno((String)row.get("P_ACCOUNTNO"));
				cardDto.sets1accountno((String)row.get("S1_ACCOUNTNO"));
				cardDto.sets2accountno((String)row.get("S2_ACCOUNTNO"));
				cardDto.sets3accountno((String)row.get("S3_ACCOUNTNO"));
				cardDto.sets4accountno((String)row.get("S4_ACCOUNTNO"));
				cardDto.sets5accountno((String)row.get("S5_ACCOUNTNO"));
				cardList.add(cardDto);
			}
		     
		     
			
		}
		catch(Exception e)
		{
			trace("Eeptionnnnnn"+e.getMessage());      
			e.printStackTrace();
		}}
			
		/*	String bulkfound="select count(*) from customer_bulk_load where customer_id='"+customerid+"'";
			enctrace("bulkfound---->"+bulkfound);
			int bulkcount=jdbctemplate.queryForInt(bulkfound);
			System.out.println("bulkcount---->"+bulkcount);
					 
			if(bulkcount==1){
			try{
				
			String multipeacctquery= "select S1_ACCT_NO AS S1_ACCOUNT_NUMBER,S2_ACCT_NO AS S2_ACCOUNT_NUMBER,"
					+ "S3_ACCT_NO AS S3_ACCOUNT_NUMBER, S4_ACCT_NO AS S4_ACCOUNT_NUMBER, S5_ACCT_NO AS S5_ACCOUNTNO"
					+ " FROM CUSTOMER_BULK_LOAD WHERE CUSTOMER_ID='"+customerid+"' and REG_STATUS='Success' and p_account_no='"+Primary_acctno+"'";
			
			accountlist = jdbctemplate.queryForList(multipeacctquery);
			Iterator acctlist = accountlist.iterator();
			while (acctlist.hasNext()) {
				
				
				Map mapitr_tsfvalue1 = (Map) acctlist.next();
				
			String 	S1_ACCOUNT_NUMBER = ((String) mapitr_tsfvalue1.get("S1_ACCOUNT_NUMBER"));
			String 	S2_ACCOUNT_NUMBER = ((String) mapitr_tsfvalue1.get("S2_ACCOUNT_NUMBER"));
			String 	S3_ACCOUNT_NUMBER = ((String) mapitr_tsfvalue1.get("S3_ACCOUNT_NUMBER"));
			String 	S4_ACCOUNT_NUMBER = ((String) mapitr_tsfvalue1.get("S4_ACCOUNT_NUMBER"));
			String 	S5_ACCOUNT_NUMBER = ((String) mapitr_tsfvalue1.get("S5_ACCOUNT_NUMBER"));
			cardList.add(S1_ACCOUNT_NUMBER);
			
			}
			for(Map<String, Object> row:rowss){
				ActiveCardDTTO cardDtoo=new ActiveCardDTTO();
				//cardDtoo.setBranchName((String)row.get("BRANCH_NAME"));
				cardDtoo.setS1_ACCOUNT_NUMBER((String)row.get("S1_ACCOUNT_NUMBER"));
				cardDtoo.setS2_ACCOUNT_NUMBER((String)row.get("S2_ACCOUNT_NUMBER"));
				cardDtoo.setS3_ACCOUNT_NUMBER((String)row.get("S3_ACCOUNT_NUMBER"));
				cardDtoo.setS4_ACCOUNT_NUMBER((String)row.get("S4_ACCOUNT_NUMBER"));
				cardDtoo.setS5_ACCOUNT_NUMBER((String)row.get("S5_ACCOUNT_NUMBER"));
				cardList.add(cardDtoo);
			}
			
			} catch(EmptyResultDataAccessException e){accountlist=null;}}	
			
		}*/
		//enctrace("getMasterReportList  :"+ reportQuery );                
		//trace("The Query Result is ==========>"+cardList);  
		return cardList ;
	}
	
	
	
	public List<ActiveCardDTO>  getMasterReportList1INST(String instid,String eDMK,String eDPK,PadssSecurity sec,String reportQuery,JdbcTemplate jdbctemplate ) throws Exception{
		//List actionlist = null;
		List<Map<String, Object>>  rows=null;
		List<Map<String, Object>>  rowss=null;
		
		trace("reportQuery111111::::::"+reportQuery);  
		List accountlist=null;
		String customerid="", Primary_acctno="",card_flag="";
		List<ActiveCardDTO> cardList=new ArrayList<ActiveCardDTO>();
		if(reportQuery==null)
		{
			//reportQuery = "SELECT 'TECHNICAL ISSUE' FROM DUAL";
		}
		else
		{          
			      
			try {
			//actionlist = jdbctemplate.queryForList(reportQuery);
			
			  rows= jdbctemplate.queryForList(reportQuery);
			//System.out.println("printing the actionlist query " +reportQuery);
			
			
		     for(Map<String, Object> row:rows){
				ActiveCardDTO cardDto=new ActiveCardDTO();
				cardDto.setBranchName((String)row.get("BRANCH_NAME"));
				cardDto.setOrderRefNo((String)row.get("ORDER_REF_NO"));
				cardDto.setOrgChn((String)row.get("ORG_CHN"));
				cardDto.setMcardNo((String)row.get("MCARD_NO"));
				cardDto.setCin((String)row.get("CIN"));
				customerid=(String)row.get("CIN");
				trace("customerid-->"+customerid);
				cardDto.setEmbName((String)row.get("EMB_NAME"));
				cardDto.setAccountNo((String)row.get("ACCT_NO"));
				Primary_acctno=(String)row.get("ACCT_NO");
				trace("ACCT_NO-->"+Primary_acctno);
				cardDto.setMakerId((String)row.get("MAKER_ID"));
				cardDto.setCardType((String)row.get("CARD_TYPE"));
				cardDto.setBulkRefId((String)row.get("BULK_REG_ID"));
				card_flag=(String)row.get("BULK_REG_ID");
				trace("card_flag-->"+card_flag);
				cardDto.setCardFalg((String)row.get("CARD_FLAG"));
				//cardDto.setPcFlag((String)row.get("PC_FLAG"));
				cardDto.setIssueDate((row.get("ISSUE_DATE").toString()));
				//cardDto.setCardStatus((String)row.get("CARDSTATUS"));
				cardDto.setpaccountno((String)row.get("P_ACCOUNTNO"));
			//	cardDto.sets1accountno((String)row.get("S1_ACCOUNTNO"));
				//cardDto.sets2accountno((String)row.get("S2_ACCOUNTNO"));
				//cardDto.sets3accountno((String)row.get("S3_ACCOUNTNO"));
				//cardDto.sets4accountno((String)row.get("S4_ACCOUNTNO"));
				//cardDto.sets5accountno((String)row.get("S5_ACCOUNTNO"));
				cardList.add(cardDto);
			}
		     
		     
			
		}
		catch(Exception e)
		{
			trace("Eeptionnnnnn"+e.getMessage());      
			e.printStackTrace();
		}}
			
		/*	String bulkfound="select count(*) from customer_bulk_load where customer_id='"+customerid+"'";
			enctrace("bulkfound---->"+bulkfound);
			int bulkcount=jdbctemplate.queryForInt(bulkfound);
			System.out.println("bulkcount---->"+bulkcount);
					 
			if(bulkcount==1){
			try{
				
			String multipeacctquery= "select S1_ACCT_NO AS S1_ACCOUNT_NUMBER,S2_ACCT_NO AS S2_ACCOUNT_NUMBER,"
					+ "S3_ACCT_NO AS S3_ACCOUNT_NUMBER, S4_ACCT_NO AS S4_ACCOUNT_NUMBER, S5_ACCT_NO AS S5_ACCOUNTNO"
					+ " FROM CUSTOMER_BULK_LOAD WHERE CUSTOMER_ID='"+customerid+"' and REG_STATUS='Success' and p_account_no='"+Primary_acctno+"'";
			
			accountlist = jdbctemplate.queryForList(multipeacctquery);
			Iterator acctlist = accountlist.iterator();
			while (acctlist.hasNext()) {
				
				
				Map mapitr_tsfvalue1 = (Map) acctlist.next();
				
			String 	S1_ACCOUNT_NUMBER = ((String) mapitr_tsfvalue1.get("S1_ACCOUNT_NUMBER"));
			String 	S2_ACCOUNT_NUMBER = ((String) mapitr_tsfvalue1.get("S2_ACCOUNT_NUMBER"));
			String 	S3_ACCOUNT_NUMBER = ((String) mapitr_tsfvalue1.get("S3_ACCOUNT_NUMBER"));
			String 	S4_ACCOUNT_NUMBER = ((String) mapitr_tsfvalue1.get("S4_ACCOUNT_NUMBER"));
			String 	S5_ACCOUNT_NUMBER = ((String) mapitr_tsfvalue1.get("S5_ACCOUNT_NUMBER"));
			cardList.add(S1_ACCOUNT_NUMBER);
			
			}
			for(Map<String, Object> row:rowss){
				ActiveCardDTTO cardDtoo=new ActiveCardDTTO();
				//cardDtoo.setBranchName((String)row.get("BRANCH_NAME"));
				cardDtoo.setS1_ACCOUNT_NUMBER((String)row.get("S1_ACCOUNT_NUMBER"));
				cardDtoo.setS2_ACCOUNT_NUMBER((String)row.get("S2_ACCOUNT_NUMBER"));
				cardDtoo.setS3_ACCOUNT_NUMBER((String)row.get("S3_ACCOUNT_NUMBER"));
				cardDtoo.setS4_ACCOUNT_NUMBER((String)row.get("S4_ACCOUNT_NUMBER"));
				cardDtoo.setS5_ACCOUNT_NUMBER((String)row.get("S5_ACCOUNT_NUMBER"));
				cardList.add(cardDtoo);
			}
			
			} catch(EmptyResultDataAccessException e){accountlist=null;}}	
			
		}*/
		//enctrace("getMasterReportList  :"+ reportQuery );                
		//trace("The Query Result is ==========>"+cardList);  
		return cardList ;
	}
	
	
	
	public List<ActiveCardDTO> processReportQuery(String instid,String query,JdbcTemplate jdbctemplate ){
		String EDPK = "", CDPK = "", CDMK = "", clearCardNumber = "", encCardNumber = "";
		Properties props = null;
		List<ActiveCardDTO> cardList=null;
		PadssSecurity sec = new PadssSecurity();
		CommonDesc commondesc = new CommonDesc();
		
		try{
		
		props = getCommonDescProperty();
		String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
		List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
		// System.out.println("secList::"+secList);
		Iterator secitr = secList.iterator();
		
		String dcardno = "", eDMK = "", eDPK = "";
		if (!secList.isEmpty()) {
			while (secitr.hasNext()) {
				Map map = (Map) secitr.next();
				CDMK = ((String) map.get("DMK"));
				EDPK = props.getProperty("EDPK");
				CDPK = sec.decryptDPK(CDMK, EDPK);

			}
		}
		cardList = this.getMasterReportList1(instid, eDMK, eDPK, sec, query, jdbctemplate);
		trace("cardlist size :: " + cardList.size());

		for (ActiveCardDTO dto : cardList) {
			encCardNumber = dto.getOrgChn();
			clearCardNumber = sec.getCHN(CDPK, encCardNumber);
			dto.setOrgChn(clearCardNumber);
		}
		}catch(Exception exce){
			exce.printStackTrace();
			trace("exception ocuured ::: "+exce.getMessage());
		}

		return cardList;
	}
	

	public List<ActiveCardDTO> processReportQueryINST(String instid,String query,JdbcTemplate jdbctemplate ){
		String EDPK = "", CDPK = "", CDMK = "", clearCardNumber = "", encCardNumber = "";
		Properties props = null;
		List<ActiveCardDTO> cardList=null;
		PadssSecurity sec = new PadssSecurity();
		CommonDesc commondesc = new CommonDesc();
		
		try{
		
		props = getCommonDescProperty();
		String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
		List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
		// System.out.println("secList::"+secList);
		Iterator secitr = secList.iterator();
		
		String dcardno = "", eDMK = "", eDPK = "";
		if (!secList.isEmpty()) {
			while (secitr.hasNext()) {
				Map map = (Map) secitr.next();
				CDMK = ((String) map.get("DMK"));
				EDPK = props.getProperty("EDPK");
				CDPK = sec.decryptDPK(CDMK, EDPK);

			}
		}
		cardList = this.getMasterReportList1INST(instid, eDMK, eDPK, sec, query, jdbctemplate);
		trace("cardlist size :: " + cardList.size());

		for (ActiveCardDTO dto : cardList) {
			encCardNumber = dto.getOrgChn();
			clearCardNumber = sec.getCHN(CDPK, encCardNumber);
			dto.setOrgChn(clearCardNumber);
		}
		}catch(Exception exce){
			exce.printStackTrace();
			trace("exception ocuured ::: "+exce.getMessage());
		}

		return cardList;
	}
	

	
}
