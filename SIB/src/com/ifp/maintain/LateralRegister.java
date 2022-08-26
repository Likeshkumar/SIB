package com.ifp.maintain;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

//import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.dao.LateralRegisterDAO;
import com.ifp.util.CommonDesc;
import com.ifp.util.CustomerBean;
import com.ifp.util.IfpTransObj;

public class LateralRegister extends BaseAction 
{ 
	
	 
	CommonDesc commondesc = new CommonDesc();
	LateralRegisterDAO lateraldao = new LateralRegisterDAO();
	CustomerBean custregbean = new CustomerBean();
	final String VALIDCARDSTATUS = "05";
	final String VALICSTATUSCODE = "50";
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	
	
	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public CustomerBean getCustregbean() {
		return custregbean;
	}

	public void setCustregbean(CustomerBean custregbean) {
		this.custregbean = custregbean;
	}

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	public LateralRegisterDAO getLateraldao() {
		return lateraldao;
	}

	public void setLateraldao(LateralRegisterDAO lateraldao) {
		this.lateraldao = lateraldao;
	}

	 
	
	
	public String comInstId(){
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	}
	
	public String comUserId(){
		HttpSession session = getRequest().getSession();
		String userid = (String)session.getAttribute("USERID"); 
		return userid;
	}
	
	public String registerHome() {	
		HttpSession session = getRequest().getSession();
		System.out.println( "\nRegistering Customer Home....");
		String act = getRequest().getParameter("act");
		if( act != null ){
			session.setAttribute("act", act);
		} 
		
		return "lateral_reg_home";
	}
	
	public String customerEntry(){
		HttpSession session = getRequest().getSession(); 
		String instid = comInstId();
		String cardno = getRequest().getParameter("cardno"); 
		try{
			
			int  cardexist = lateraldao.checkCardExist(instid, cardno, jdbctemplate);
			if( cardexist <= 0 ){
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "[" + cardno + "] KYC Already Done.");
				return registerHome();
			} 
			String cardstatus = lateraldao.checkValidCard(instid, cardno, session, jdbctemplate);
			if( !cardstatus.equals(VALIDCARDSTATUS) ){
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "[" + cardno + "] Not a valid card.");
				return registerHome();
			} 
			
			String cardstatuscode = lateraldao.checkCardStatus(instid, cardno, jdbctemplate);
			if( !cardstatuscode.equals(VALICSTATUSCODE) ){
				session.setAttribute("curerr", "E");
				String localstatus = commondesc.getCardStatusCode(instid, cardstatuscode, jdbctemplate);
				String statusdesc = commondesc.getCardStatusDesc(instid, localstatus, jdbctemplate);
				session.setAttribute("curmsg", "[" + cardno + "] Not a valid card. Card Status is  " + statusdesc);
				return registerHome();
			} 
			
			String customerid = lateraldao.checkAlreadyRegistered( instid, cardno, session, jdbctemplate );
			if( !customerid.contains("UNK")){
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg",  "Card number [ "+cardno+" ] already registered. Existing customer id is [ "+customerid+" ]  ");
				return registerHome();
			} 
			
			custregbean.cardno = cardno;
			custregbean.nationalitylist = commondesc.gettingNations( jdbctemplate );
			custregbean.documenttypelist = commondesc.gettingDocumnettype(instid, jdbctemplate);
			
		}catch(Exception e ){
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg",  e.getMessage() );
			return registerHome();
		} 
		
		return "customer_entry";
	}
	
	public String registerCustomerAction() {
		
		HttpSession session = getRequest().getSession();
		 
		JSONObject jsoncust = new JSONObject();
		IfpTransObj transact = commondesc.myTranObject("REGCUST", txManager);
		String instid = comInstId();
		String usercode = comUserId();
		String cardno = getRequest().getParameter("cardno"); 
		
		String apno  =  getRequest().getParameter("appno");
		String kycuser = null;
		String makerid = "",checkerid="",makerdate="",checkerdate="", mkckflag="", ckdate="";
		String act =(String) session.getAttribute("act");
		if ( act.equals("M")){ 
			System.out.println( "act value " + act); 
			makerid = usercode; 
		    mkckflag = "M";
			ckdate = commondesc.default_date_query;
			makerdate = "SYSDATE";
		}else {  // D 
			System.out.println( "act value " + act);
			makerid = usercode;
			checkerid = makerid;
			mkckflag = "P";
			ckdate = "sysdate";
			makerdate = "SYSDATE";
		}
		
		System.out.println( "id document : "  + getRequest().getParameter("reqdocuement"));
		
		jsoncust.put("FNAME", getRequest().getParameter("firstname")) ;
		jsoncust.put("MNAME", getRequest().getParameter("midname")) ;
		jsoncust.put("LNAME", getRequest().getParameter("lastname")) ;
		jsoncust.put("FATHER_NAME", getRequest().getParameter("fahtername")) ;
		jsoncust.put("MOTHER_NAME", getRequest().getParameter("mothername")) ;
		jsoncust.put("GENDER", getRequest().getParameter("gender")) ;
		jsoncust.put("MARITAL_STATUS", getRequest().getParameter("mstatus")) ;
		jsoncust.put("SPOUSE_NAME", getRequest().getParameter("spname")) ;
		jsoncust.put("OCCUPATION", getRequest().getParameter("occupation")) ;
		jsoncust.put("ID_DOCUMENT", getRequest().getParameter("reqdocuement")) ;
		jsoncust.put("ID_NUMBER", getRequest().getParameter("documentid")) ;
		jsoncust.put("NATIONALITY", getRequest().getParameter("nations")) ;
		jsoncust.put("DOB", getRequest().getParameter("dob")) ;
		jsoncust.put("MOBILE_NO", getRequest().getParameter("mobileno")) ;
		jsoncust.put("PHONE_NO", getRequest().getParameter("phoneno")) ;
		jsoncust.put("EMAIL_ADDRESS", getRequest().getParameter("email")) ;
		jsoncust.put("POST_ADDR1", getRequest().getParameter("paddress1")) ;
		jsoncust.put("POST_ADDR2", getRequest().getParameter("paddress2")) ;
		jsoncust.put("POST_ADDR3", getRequest().getParameter("paddress3")) ;
		jsoncust.put("POST_ADDR4", getRequest().getParameter("paddress4")) ; 
		String resaddress1 =  "";
		String resaddress2 =  "";
		String resaddress3 =  "";
		String resaddress4 =  "";
		if( getRequest().getParameter("residentreq") != null){
			System.out.println( " residren req check box checked ");
			resaddress1 = getRequest().getParameter("paddress1") ;
			resaddress2 = getRequest().getParameter("paddress2");
			resaddress3 = getRequest().getParameter("paddress3");
			resaddress4 = getRequest().getParameter("paddress4");
		}else{
			resaddress1 = getRequest().getParameter("resaddress1") ;
			resaddress2 = getRequest().getParameter("resaddress2");
			resaddress3 = getRequest().getParameter("resaddress3");
			resaddress4 = getRequest().getParameter("resaddress4"); 
		}
		jsoncust.put("RES_ADDR1", resaddress1 ) ; 
		jsoncust.put("RES_ADDR2", resaddress2 ) ;
		jsoncust.put("RES_ADDR3", resaddress3 ) ;
		jsoncust.put("RES_ADDR4", resaddress4 ) ; 
		
		try{
			String customerid = commondesc.cinnumberGeneratoer(instid, jdbctemplate); 
			int insertcustomer = lateraldao.insertCustomerData(instid, customerid, jsoncust, jdbctemplate);
			if( insertcustomer <= 0 ){
				transact.txManager.rollback(transact.status);
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "Could not insert the records. " );
				return registerHome();
			}
			
			transact.txManager.commit(transact.status);
			session.setAttribute("curerr", "S");
			session.setAttribute("curmsg", "Customer Registered Successfully...Customer id is [ "+customerid+" ] " );
			return registerHome();
			
		}catch(Exception e ){
			transact.txManager.rollback(transact.status);
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg",  e.getMessage() ); 
			e.printStackTrace();
		}
		
		return registerHome();
	}//end registerCustomer


	
}//end class
 