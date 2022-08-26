package com.ifp.maintain;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.beans.CardMaintainActionBeans;
import com.ifp.dao.CardMaintainActionDAO;
import com.ifp.dao.CardProcessDAO;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.IfpTransObj;

public class CardProcess extends BaseAction {
	CommonDesc commondesc = new CommonDesc();
	JdbcTemplate jdbctemplate = new JdbcTemplate();
	CommonUtil comutil = new CommonUtil();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager(); 
	CardProcessDAO cardprocessdao = new CardProcessDAO();
	String NOTACTIVATEDSTATUS = "09";
	
	public CardProcessDAO getCardprocessdao() {
		return cardprocessdao;
	}

	public void setCardprocessdao(CardProcessDAO cardprocessdao) {
		this.cardprocessdao = cardprocessdao;
	}

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public CommonUtil getComutil() {
		return comutil;
	}

	public void setComutil(CommonUtil comutil) {
		this.comutil = comutil;
	}

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	 


	String act;  
	List courierlist;
	List cardlist;
	String courierrefid;
	
	
	public String getCourierrefid() {
		return courierrefid;
	}

	public void setCourierrefid(String courierrefid) {
		this.courierrefid = courierrefid;
	}

	public List getCardlist() {
		return cardlist;
	}

	public void setCardlist(List cardlist) {
		this.cardlist = cardlist;
	}

	public List getCourierlist() {
		return courierlist;
	}

	public void setCourierlist(List courierlist) {
		this.courierlist = courierlist;
	}

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	List <String> prodlist;
	
	public List <String> getProdlist() {
		return prodlist;
	}

	public void setProdlist(List<String> prodlist) {
		this.prodlist = prodlist;
	}
	
	List <String> branchlist;

	public List <String> getBranchlist() {
		return branchlist;
	}

	public void setBranchlist(List<String>	 branchlist) {
		this.branchlist = branchlist ;
	}

	List instorderlist;
	public List getInstorderlist() {
		return instorderlist;
	}

	public void setInstorderlist(List instorderlist) {
		this.instorderlist = instorderlist;
	}
	
	List <String>prenamelist;
	
	public List<String> getPrenamelist() {
		return prenamelist;
	}

	public void setPrenamelist(List<String> prenamelist) {
		this.prenamelist = prenamelist;
	}

	 
	public String comInstId( HttpSession session ){ 
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	} 
	public String comUserCode( HttpSession session  ){ 
		String instid = (String)session.getAttribute("USERID"); 
		return instid;
	}
	
	public String cardActivationHome(){
		HttpSession session = getRequest().getSession();		 
		String instid = comInstId(session);
		List activationcourerlist = cardprocessdao.getListOfCourieId(instid, NOTACTIVATEDSTATUS, jdbctemplate);
		setCourierlist(activationcourerlist);
		 
		
		
		trace("Card activation home...");
		try{
			
		}catch(Exception e){
			trace("Exception : "+e.getMessage());
		}
		return "cardactivationhome";
	}
	
	
	public String activateCardNumber(){
		HttpSession session = getRequest().getSession();
		String instid = comInstId( session );
		String usercode = comUserCode( session );
		String cardno =  getRequest().getParameter("originalcardno");
		String mobileno = getRequest().getParameter("mobileno");
		String pinoption = getRequest().getParameter("pinoption");
		
		IfpTransObj transact = commondesc.myTranObject("CARDACT", txManager);
		CardMaintainActionDAO maintaindao = new CardMaintainActionDAO();
		AuditBeans auditbean = new AuditBeans();
		trace("Got the request...mobileno : "+mobileno);
		try{					 
			auditbean.setCardno(cardno);
			trace("Selected pin option..."+pinoption);
			//Checking eligibility
			int cardstatus = cardprocessdao.checkCardActivateEligibilty(instid, cardno, jdbctemplate);
			trace("Checking activate eligibilty for the cardnumber [ "+cardno+" ]....");
			if( cardstatus < 1 ){
				trace("Card is not eligibile for this action");
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "This card is not applicable for this action");
				return this.cardActivationHome();
			}
			
			String activestatus = "05"; 
			String switchstatus = commondesc.getSwitchCardStatus(instid, activestatus, jdbctemplate);
			trace("Gettign switch status for the ifpstatus  [ "+activestatus+" ] ....got : "+switchstatus);
			
			
			//READING OLD MOBILE NUMBER
			String oldmobileno = commondesc.getMobileNumber(instid, cardno, jdbctemplate );
			trace("Getting old mobile number....got : "+oldmobileno); 
			
			//inserting history information
			String prevstatus = commondesc.getCardCurrentStatus(instid,"Z", cardno, "CARD_PRODUCTION", jdbctemplate);
			maintaindao.insertMainintainHistory(instid, cardno, prevstatus, activestatus, usercode, jdbctemplate);
			
			mobileno = comutil.formateMobile(mobileno);
			int x= cardprocessdao.updateCardStatus(instid, cardno, activestatus, switchstatus, mobileno, jdbctemplate);
			if( x < 0 ){
				trace("Unable to continue the process");
				txManager.rollback(transact.status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Unable to continue the process");
			}
			trace("Updating mobile number ["+mobileno+"] and the status [ "+activestatus+" ] for the card [ "+cardno+" ]");
			
			 
			String masknumber = comutil.maskNumbers(cardno); 
			
			if( pinoption.equals("NOPIN") ){   
				if( !oldmobileno.equals("NA") || oldmobileno.length() > 9){
					String remapsms = "Dear Customer, Your card "+masknumber+ " has been Linked with another mobile number. If not you, Contact customer care and block the transaction.";
					comutil.sendSmsMessage(oldmobileno, remapsms);
				} 
				//Activecontent
				auditbean.setActmsg(mobileno +" linked and activated");
				String activatecontent = "Dear Customer, Your card "+masknumber+ " has been activated. Thanks for using our network";
				//comutil.sendSmsMessage(mobileno, activatecontent);
				
				
			}else if( pinoption.equals("CVVREQ") ){
				String cvv2 = commondesc.getCVV2CardNumber(instid, cardno, jdbctemplate);
				trace("Getting cvv value....got : "+cvv2);
				if( cvv2 == null ){
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "Could not get Cvv value...");
				}
				
				String expdate = commondesc.getCardExpiryDate(instid, cardno, "MM-YYYY", jdbctemplate);
				trace("Getting expiry date....got : "+expdate);
				if( expdate == null ){
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "Could not get Expiry date ...");
				}
				
				auditbean.setActmsg(" CVV Related information sent to the mobile number "+ mobileno);
				
				String activatecontent = "Dear Customer, Your card "+masknumber+ " has been activated. CVV is "+cvv2+" and Expiry date is "+expdate;
				activatecontent += " Please do not disclose to anyone. Thank you for using our network";
				//comutil.sendSmsMessage(mobileno, activatecontent);
				
			}
			
			
			/*** AUDIT TRAIL ***/
				trace("Inserting audit trail");
				commondesc.insertAuditTrail(instid, usercode, auditbean, jdbctemplate, txManager); 
			/******/
			
			
			
			txManager.commit(transact.status);
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", "Card has been activated successfully");
			return this.cardActivationHome();
			
		}catch(Exception e ){
			txManager.rollback(transact.status);
			trace("Exception : "+e.getMessage() );
			e.printStackTrace();
		}
		
		return this.cardActivationHome();
	}
	
	
	
	
	
	public String listOfActivationCards(){ 
		HttpSession session = getRequest().getSession();
		String instid = comInstId( session );
		String usercode = comUserCode( session );
		 
		/*String selecttype = getRequest().getParameter("selecttype");
		String cardno =  getRequest().getParameter("cardno");
		String courierid = getRequest().getParameter("entcourierid");
		*/
		String selectedcourierid = getRequest().getParameter("selectedcourierid"); 
		IfpTransObj transact = commondesc.myTranObject("CARDACT", txManager);
		CardMaintainActionDAO maintaindao = new CardMaintainActionDAO();
		setCourierrefid(selectedcourierid);
		 
		try{			
			
			List corierlist = cardprocessdao.getCourierDataList(instid, selectedcourierid, jdbctemplate);
			setCourierlist(corierlist);
			
			String qrycond = " AND COURIER_ID =  '"+selectedcourierid.trim()+"'";  
			List cardlist = cardprocessdao.getActivationCardList(instid, NOTACTIVATEDSTATUS, qrycond, jdbctemplate);
			if( cardlist.isEmpty() ){
				addActionError("No Records Found");
				return this.cardActivationHome();  
			}
			ListIterator itr = cardlist.listIterator();
			while( itr.hasNext() ){
				Map mp = (Map)itr.next();
				String cardstatus = (String)mp.get("CARD_STATUS");
				String statusdesc = commondesc.getCardStatusDesc(instid, cardstatus, jdbctemplate);
				mp.put("CARD_STATUSDESC", statusdesc);
				itr.remove();
				itr.add(mp);
			}
			setCardlist(cardlist);
			return "activecardlist";
		}catch(Exception e ){
			txManager.rollback(transact.status);
			trace("Exception : "+e.getMessage() );
			e.printStackTrace();
		}
		
		return this.cardActivationHome();
	}
	
	
	public String cardActivationAction(){
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		IfpTransObj transct = commondesc.myTranObject("ACTIVATE", txManager);
		String[] cardlist = getRequest().getParameterValues("instorderrefnum");
		String cardno = null;
		String normalstatus = CommonDesc.NORMALCARD;
		String switchcardstatus = null;
		String courierid = getRequest().getParameter("courierid");
		Boolean process_success = true;
		try{
			if( cardlist == null ){
				addActionError("No Cards Selected");
				return this.cardActivationHome();
			}
			
			
			for( int i=0; i<cardlist.length; i++ ){
				cardno = cardlist[i];
				switchcardstatus = commondesc.getSwitchCardStatus(instid, normalstatus, jdbctemplate);
				int x= cardprocessdao.activateCardProcess(instid, cardno, normalstatus, switchcardstatus, jdbctemplate);
				trace("Updating card status...got : " + x );
				if( x == 1){
					x = cardprocessdao.updateCourierActivationCount(instid, courierid,  jdbctemplate);
					trace("Updating courier activated status..got : " + x );
				}
				
				if( x < 0 ){ 
					process_success = false;
					break;
				}
			}
			
			if(!process_success){
				addActionError("Unable to continue the process");
				transct.txManager.rollback(transct.status);
				return this.cardActivationHome(); 
			}
			
			addActionMessage(cardlist.length + " Cars(s) Activated Successfully");
			transct.txManager.commit(transct.status);
			
		}catch(Exception e ){
			transct.txManager.rollback(transct.status);
			addActionError("Unable to process");
			trace("Exception : " + e.getMessage() );
			e.printStackTrace();
		}
		
		return this.cardActivationHome();
	}
}
