/**
 * 
 */
package com.ifd.personalize;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.ifp.beans.AuditBeans;
 





import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

 




//import com.ifc.dao.CreditCustRegisterationDAO;
import com.ifd.beans.CustomerRegisterationBeans;
import com.ifd.dao.DebitCustomerRegisterDAO;
import com.ifp.Action.BaseAction;
import com.ifd.fee.FeeConfigDAO;
import com.ifp.mail.EmailService;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.IfpTransObj;
import com.opensymphony.xwork2.ModelDriven;

/**
 * @author kumar
 *
 */
public class DebitCustomerRegister extends BaseAction implements ModelDriven{
	CommonDesc commondesc = new CommonDesc();
	CommonUtil comutil = new CommonUtil();
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	AuditBeans auditbean = new AuditBeans();
	//CreditCustRegisterationDAO creditdao = new CreditCustRegisterationDAO();
	final String  ERRORHOME = "required_home";
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

	CustomerRegisterationBeans dbtcustregbean = new CustomerRegisterationBeans();
	DebitCustomerRegisterDAO dbtcustdao = new DebitCustomerRegisterDAO();
	
	
	public DebitCustomerRegisterDAO getDbtcustdao() {
		return dbtcustdao;
	}

	public void setDbtcustdao(DebitCustomerRegisterDAO dbtcustdao) {
		this.dbtcustdao = dbtcustdao;
	}

	public CustomerRegisterationBeans getDbtcustregbean() {
		return dbtcustregbean;
	}

	public void setDbtcustregbean(CustomerRegisterationBeans dbtcustregbean) {
		this.dbtcustregbean = dbtcustregbean;
	}
	
	public String comUserCode( HttpSession session ){ 
		String instid = (String)session.getAttribute("USERID"); 
		return instid;
	}  
	
	public String comUserId(){
		HttpSession session = getRequest().getSession();
		String userid = (String)session.getAttribute("USERID"); 
		return userid;
	}
	public String comBranchId(){
		HttpSession session = getRequest().getSession();
		String br_id = (String)session.getAttribute("BRANCHCODE"); 
		return br_id;
	}
	public String comuserType(){
		HttpSession session = getRequest().getSession();
		String usertype = (String)session.getAttribute("USERTYPE"); 
		return usertype;
	}
	public String comUsername(){
		HttpSession session = getRequest().getSession();
		String username = (String)session.getAttribute("USERNAME"); 
		return username;
	}
	public String comInstId(){
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	}
	
	
	
	@Override
	public Object getModel() { 
		 
		return dbtcustregbean;
	}
	
	public String debitCustomerRegHome(){
		
		return "debitreg_home";
	}
	String renewal;
	
	public String getRenewal() {
		return renewal;
	}

	public void setRenewal(String renewal) {
		this.renewal = renewal;
	}

	public String authorizeCustomerRegHome() throws Exception{
		trace("authorizeCustomerRegHome started...");
		List pers_prodlist=null,br_list=null;
		String inst_id =comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		HttpSession session = getRequest().getSession();
		String branchcondition="";
		branchcondition = " AND BRANCH_CODE = '"+branch+"'";
		String checkcondition = branchcondition +" AND ORDER_STATUS='01' AND MKCK_STATUS='M' AND REG_LEVEL='4'";
		if(!usertype.equals("INSTADMIN"))
		{
			dbtcustregbean.setApplicationlist(dbtcustdao.getApplicationDataByBranch(inst_id, checkcondition, jdbctemplate));
		}
		else   
		{
			StringBuilder brlist = new StringBuilder();
			brlist.append("select BRANCH_CODE,(SELECT BRANCH_NAME FROM BRANCH_MASTER a WHERE a.BRANCH_CODE=B.BRANCH_CODE)BRANCH_NAME ");
			brlist.append("from PERS_CARD_ORDER b WHERE INST_ID='"+inst_id+"' AND ORDER_STATUS='01' AND MKCK_STATUS='M' AND REG_LEVEL='4'  GROUP BY b.BRANCH_CODE ");
			enctrace("brlistbrlist::"+brlist.toString());
			dbtcustregbean.setBranchlist(jdbctemplate.queryForList(brlist.toString()));
			
			dbtcustregbean.setApplicationlist(dbtcustdao.getApplicationList(inst_id, jdbctemplate));
		}
		trace("authorizeCustomerRegHome ended...");
		return "authorizeCustomerRegHome";
	}
	public void getApplicationNo() throws Exception{
		String branchcode = getRequest().getParameter("branch");
		String inst_id =comInstId();
		List applist = null;
		if("ALL".equalsIgnoreCase(branchcode)){
			applist = dbtcustdao.getApplicationList(inst_id, jdbctemplate);
		}else{
			String checkcondition =  "AND BRANCH_CODE = '"+branchcode+"' AND ORDER_STATUS='01' AND MKCK_STATUS='M' AND REG_LEVEL='4'";
			applist = dbtcustdao.getApplicationDataByBranch(inst_id, checkcondition, jdbctemplate);
		}
		 Iterator itr = applist.iterator();
		 String opt = "";
		 opt = "<option value=''> ---Select--- </option>";
		 while( itr.hasNext() ){
			 Map mp = (Map)itr.next();
			 opt += "<option value='"+(String)mp.get("ORDER_REF_NO")+"'> "+(String)mp.get("APPLICANT")+" </option>";
		 }
		 getResponse().getWriter().write(opt.toString());
		
	}
	public String EditCustomerRegHome() throws Exception{
		trace("EditCustomerRegHome started...");
		String inst_id =comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		String branchcondition="";
		branchcondition = " AND BRANCH_CODE = '"+branch+"'";
		String checkcondition = branchcondition +" AND ORDER_STATUS='01' AND MKCK_STATUS='M' AND REG_LEVEL !='4' ";
		if(!usertype.equals("INSTADMIN"))
		{
			dbtcustregbean.setApplicationlist(dbtcustdao.getApplicationDataByBranch(inst_id, checkcondition, jdbctemplate));
		}
		else   
		{
			dbtcustregbean.setApplicationlist(dbtcustdao.getApplicationListForEdit(inst_id, jdbctemplate));
		}
		trace("EditCustomerRegHome ended...");
		return "EditCustomerRegHome";
	}
	
	
	//tabbed details
	
	public String tab2PersionalDetails()
	{
		System.out.println("tab2PersionalDetails called");
		return "tab2_home";
	}
	
	
	public String tab3ContactDetails()
	{
		System.out.println("tab3ContactDetails called");
		return "tab3_home";
	}
	
	/*Ajax Call Methods*/
	
	//getting product List
	public void getProductListByBranch() throws Exception {
		trace("calling ..."+Thread.currentThread().getStackTrace()[1].getMethodName());
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
		//String branchcode = getRequest().getParameter("branchcode");
		trace("calling ..."+Thread.currentThread().getStackTrace()[1].getMethodName()+"---"+"istid:"+instid);
		
		StringBuilder result = new StringBuilder();    
				
		result.append("<option value='-1'> Select Product </option>");
		try{
			//List productlist = commondesc.getProductList(instid, jdbctemplate, branchcode);
			List productlist = commondesc.getProductListView(instid, jdbctemplate);
			   
			   
			if( productlist.isEmpty() ){ 
				getResponse().getWriter().write("No Product confugured...");
				return;
			} 
			Iterator itr = productlist.iterator(); 
			while( itr.hasNext() ){
				Map mp = (Map)itr.next();
				result =result.append("<option value='"+mp.get("PRODUCT_CODE")+"'> " + mp.get("CARD_TYPE_NAME")+ " </option>");
				
			}
		}catch(Exception e ){
			e.printStackTrace();
			trace("Exception ...||"+Thread.currentThread().getStackTrace()[1].getMethodName()+"--"+ e.getMessage());
		}
		
		getResponse().getWriter().write(result.toString());
	}
	
public void getCurrencyconfig() throws Exception {
		
		trace("fercurrencydetails: method called");
		FeeConfigDAO feedao = new FeeConfigDAO();
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
		String SUBPRODUCTVAL = getRequest().getParameter("SUBPRODUCTVAL");
		String subproduct = getRequest().getParameter("SUBPRODUCT");
		
		String result = "<option value='-1'>Select Currency</option>";
		System.out.println("getCurrencyBySubProduct list...");  
		try{
			String Currcode = commondesc.getCurrencyvalues(instid, SUBPRODUCTVAL, subproduct, jdbctemplate);
			if( Currcode == null ){
				getResponse().getWriter().write("<option value='-1'> No Currency configured for this subproduct </option>");
				return;
			}
			
			String currname = commondesc.getcurrencydesc(instid, Currcode, jdbctemplate);
			    
			result =  "<option value='"+Currcode+"'> "+currname+" </option>";
		 
		}catch(Exception e ){
			e.printStackTrace();
			trace("Exception ..."+ e.getMessage());
		}
		
		getResponse().getWriter().write(result);
	}
	
	//getting subproduct List  
	public void getSubProductListByProduct() throws Exception {  
		trace("********************get subproduct method entered****************");
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname");
		String productcode = getRequest().getParameter("prodid");
		String status = getRequest().getParameter("status");
		System.out.println("productcode:"+productcode);
		System.out.println("status:"+status);
		System.out.println("instid:"+instid);    
		String suproductselect = null;
		try{
			suproductselect = commondesc.getListOfSubProduct(instid, productcode, status, jdbctemplate);
			trace("Getting subproduct list for the product code [ "+productcode+" ] ...got " + suproductselect );
		}catch(Exception e ){
			e.printStackTrace(); 
			trace("Exception...." + e.getMessage());
			suproductselect = "<option value='-1'>Unable to get sub product list </option>";
		}
		
		getResponse().getWriter().write(suproductselect);
	}
	
	public void validateSelectedDate() throws Exception {      
		
		String selecteddate = getRequest().getParameter("selecteddate");
		
		String dateresult="";
		try{
			dateresult = commondesc.dateGreatcurrentDate(selecteddate, jdbctemplate);
			trace("Getting dateresult Got " + dateresult );
		}catch(Exception e ){
			e.printStackTrace(); 
			trace("Exception...." + e.getMessage());
		}
		
		getResponse().getWriter().write(dateresult);
	}
	
	
	
	public void getLimitBySubProduct() throws Exception {
		trace("getfeebysubproduct: method called");
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname");
		String productcode = getRequest().getParameter("productid");
		trace("product code getting for limitvalue"  + productcode);
		
		String subproduct = getRequest().getParameter("subproductid");  
		trace("sub product getting for limitvalue"  + subproduct);
		String result = "<option value='-1'> Select Limit </option>";
		System.out.println("getLimitIdBySubProduct...");
		try{
			String limitid = commondesc.getLimitIdBySubProduct(instid, productcode, subproduct, jdbctemplate);
			if( limitid == null ){
				getResponse().getWriter().write("<option value='-1'> No Limit configured for this subproduct </option>");
				return;
			}
			
			String limitname = commondesc.getLimitDesc(instid, limitid, jdbctemplate);
			
			result =  "<option value='"+limitid+"'> "+limitname+" </option>";
		 
		}catch(Exception e ){
			e.printStackTrace();
			trace("Exception ..."+ e.getMessage());
		}
		
		getResponse().getWriter().write(result);
	}
	
	
	public void getFeeBySubProduct() throws Exception {
		
		trace("getFeeBySubProduct: method called");
		FeeConfigDAO feedao = new FeeConfigDAO();
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
		String productcode = getRequest().getParameter("PRODUCT");
		String subproduct = getRequest().getParameter("SUBPRODUCT");
		
		String result = "<option value='-1'>Select Fee</option>";
		System.out.println("getFeeBySubProduct list...");  
		try{
			String feecode = commondesc.getFeeBySubProduct(instid, productcode, subproduct, jdbctemplate);
			if( feecode == null ){
				getResponse().getWriter().write("<option value='-1'> No Fee configured for this subproduct </option>");
				return;
			}
			
			String feename = commondesc.getFeeDesc(instid, feecode, jdbctemplate);
			    
			result =  "<option value='"+feecode+"'> "+feename+" </option>";
		 
		}catch(Exception e ){
			e.printStackTrace();
			trace("Exception ..."+ e.getMessage());
		}
		
		getResponse().getWriter().write(result);
	}
	
	
public void getAccutSubTypeList() throws Exception {
		
		trace("getAccutSubTypeList: method called");
		FeeConfigDAO feedao = new FeeConfigDAO();
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
		String accounttypeid = getRequest().getParameter("accounttypeid");
		
		String result = "<option value='-1'> -SELECT- </option>";
		System.out.println("getAccutSubTypeList list..."); 
		StringBuilder subaccttyperesult = new StringBuilder();
		try{
			List  subaccounttypelist = dbtcustdao.getAcctSubTypeList(instid, accounttypeid, jdbctemplate);
			Iterator  itr = subaccounttypelist.iterator();
			
			subaccttyperesult.append("<option value='-1'> - Select - </option>"); 
			while( itr.hasNext() ){
				Map map = (Map)itr.next();
				String subAccounttypeid = (String)map.get("ACCTSUBTYPEID"); 
				String subAccountypedesc = (String)map.get("ACCTSUBTYPEDESC");
				subaccttyperesult = subaccttyperesult.append("<option value='"+subAccounttypeid+"'>"+subAccountypedesc+"</option>");
				//result = result + max;
			}
			
		}catch(Exception e ){
			e.printStackTrace();
			trace("Exception ..."+ e.getMessage());
		}
		//System.out.println(subaccttyperesult);
		getResponse().getWriter().write(subaccttyperesult.toString());
	}
	
	
	
	/*Ajax Call Methods*/
	
	
	public String debitCustomerRegView(){
		trace("----Registering customer view......");
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname");
		String customertype = getRequest().getParameter("customertype");
		System.out.println( "customertype :" + customertype  );
		trace( "customertype :" + customertype  );
		try{
			if ( customertype == null || customertype.equals("$NEW") ) {
				trace("Redirecting to new customer registration");
				
				
				
				return debitCustomerRegEntry();
			}else if ( customertype.equals("$EXIST") ) {
				List applicationlist = dbtcustdao.getApplicationList(instid, jdbctemplate);
				trace("Getting application count : " + applicationlist.size());
				dbtcustregbean.setApplicationlist(applicationlist);
				trace("Redirecting to existing  customer search");
				 return "debitreg_search";
			}
		}catch(Exception e ){
			e.printStackTrace();
			trace("Exception...." + e.getMessage());
		}
		
		trace("Redirecting to new customer registration..out of if condition");
		return "debitreg_search";
	}
	
	public String debitCustomerRegEntry() throws Exception {
		//getRequest().setCharacterEncoding("UTF-8");
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname");
		String orderrefno = getRequest().getParameter("orderrefno");
		List branchlist=null;
		try{
			dbtcustregbean.setListofyears( commondesc.getListOfPreviousYear() );
			trace("Got list of year...");
	
			List binlist = commondesc.getListOfBins(instid, jdbctemplate);
			if( binlist.isEmpty() ){
				addActionError("Configure the bin...and try again for the registeration");
				return "required_home";
			}
			dbtcustregbean.setBinlist(binlist);
			
	/*1*/	branchlist = commondesc.generateBranchList(instid, jdbctemplate);
			
			if( branchlist.isEmpty() ){
				addActionError("Could not get branch list");
				return "required_home";
			}
			dbtcustregbean.setBranchlist(branchlist);
			
			List instConfDet = commondesc.getConfigDetails(instid, jdbctemplate);
			trace("InstConfigDetails : " + instConfDet );
			
			Iterator itr = instConfDet.iterator(); 
			while( itr.hasNext() ){
				Map mp = (Map)itr.next();
				dbtcustregbean.setAccountnolength((String) mp.get("ACCT_LEN"));
				dbtcustregbean.setCinidbasedon((String) mp.get("CUSTID_BASEDON"));
				dbtcustregbean.setCinnoLength( mp.get("CIN_LEN").toString());
			}
			 
			trace("setting accttypelist");
			List accttypelist = dbtcustdao.getAcctTypeList(instid, jdbctemplate);
			dbtcustregbean.setAccttypelist(accttypelist);  
			  
			// -
			if(session.getAttribute("USERTYPE")!="INSTADMIN")
			{
			List productlist = commondesc.getProductList(instid, jdbctemplate, session);
			
			if( productlist.isEmpty() ){
				addActionError("No Product configured ....");
				return "required_home";
			}   
			dbtcustregbean.setProductlist(productlist);   
			}
			
			trace("setting currency list");
			List currencylist = commondesc.getCurList(instid, jdbctemplate);
			dbtcustregbean.setCurrencylist(currencylist);    
		
			 
			//dbtcustregbean.setTab1Status("");        
			dbtcustregbean.setTab2Status("true");
			dbtcustregbean.setTab3Status("true");
			dbtcustregbean.setTab4Status("true");    
			   
			/*
			if( orderrefno != null ){
				String reglevel = creditdao.getCustomerRegLevel(instid, orderrefno, jdbctemplate);
				trace("Getting reglevel....got : " + reglevel );
				dbtcustregbean.setReglevel(reglevel);
			}
			*/
		}catch(Exception e){
			trace("Exception : " + e.getMessage() );
			e.printStackTrace();
			
		}
		
		return "debitreg_entry";
	}
	
	public String authorizedebitCustomerRegEntry() throws Exception {
		//getRequest().setCharacterEncoding("UTF-8");
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname");
		String orderrefno = getRequest().getParameter("orderrefno");
		
		try{
			dbtcustregbean.setListofyears( commondesc.getListOfPreviousYear() );
			trace("Got list of year...");
			
			
			/*
			List relationlist = commondesc.getSuplimentaryRelation( instid, jdbctemplate );
			if( !relationlist.isEmpty() ){
				dbtcustregbean.setSupplimentrelationlist(relationlist);
			}else{
				addActionError("No Supplimentary Relation Details Configured. Configure the  Details And Try Again ");
			}*/
			
		//	List binlist = commondesc.getListOfBins(instid, jdbctemplate);
		//	if( binlist.isEmpty() ){
		//		addActionError("Configure the bin...and try again for the registeration");
		//		return "required_home";
		//	}
		//	dbtcustregbean.setBinlist(binlist);
			
			List branchlist = commondesc.generateBranchList(instid, jdbctemplate);
			if( branchlist.isEmpty() ){
				addActionError("Could not get branch list");
				return "required_home";
			}
			dbtcustregbean.setBranchlist(branchlist);
			
			String acctnolen = commondesc.getAccountNoLength(instid, jdbctemplate);
			trace("Getting acct number length ...got : " + acctnolen );
			if( acctnolen == null ){
				addActionError("Account Number length not set for institution ");
				return "required_home";
			}
			dbtcustregbean.setAcctnolen(acctnolen); 
			
			
			trace("setting accttypelist");
			List accttypelist = dbtcustdao.getAcctTypeList(instid, jdbctemplate);
			dbtcustregbean.setAccttypelist(accttypelist);  
			
			
			
			// -
			
			//List productlist = commondesc.getProductList(instid, jdbctemplate, session);
			//if( productlist.isEmpty() ){
			//	addActionError("No Product configured ....");
			//	return "required_home";
			//}   
			//dbtcustregbean.setProductlist(productlist);
			
			/*
			if( orderrefno != null ){
				String reglevel = creditdao.getCustomerRegLevel(instid, orderrefno, jdbctemplate);
				trace("Getting reglevel....got : " + reglevel );
				dbtcustregbean.setReglevel(reglevel);
			}
			*/
		}catch(Exception e){
			trace("Exception : " + e.getMessage() );
			e.printStackTrace();
			
		}
		
		return "authorizedebitCustomerRegEntry";
	}
	
	
	
	
	
	public void getAcctTypeList() throws Exception {
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
		String result = "<option value='-1'> -SELECT- </option>";
		System.out.println("Getting acct type list...");
		try{
			List accttypelist = dbtcustdao.getAcctTypeList(instid, jdbctemplate);
			if( accttypelist.isEmpty() ){ 
				getResponse().getWriter().write("No Acct type confugured...");
				return;
			} 
			Iterator itr = accttypelist.iterator(); 
			while( itr.hasNext() ){
				Map mp = (Map)itr.next();
				result += "<option value='"+mp.get("ACCTTYPEID")+"'> " + mp.get("ACCTTYPEDESC")+ " </option>";
				
			}
		}catch(Exception e ){
			e.printStackTrace();
			trace("Exception ..."+ e.getMessage());
		}
		
		getResponse().getWriter().write(result);
	}
	 
	
	public void checkCustomerIdExist() throws IOException
	{
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname");
		String customerid = getRequest().getParameter("customerid");
		String result="";
		int res = commondesc.checkCustomerexist(instid, customerid, "PERS_CARD_ORDER", jdbctemplate);
		if(res>0)
		{
			getResponse().getWriter().write("EXIST");
		}
		else
		{
			getResponse().getWriter().write("NEW");
		}
	}
	
	
	public void checkAccountNoExist() throws IOException
	{
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname");
		String accountnovalue = getRequest().getParameter("accountnovalue");
		String result="";
		int res = commondesc.checkAccountNoexist(instid, accountnovalue, jdbctemplate);
		if(res>0)
		{
			getResponse().getWriter().write("EXIST");
		}
		else
		{
			getResponse().getWriter().write("NEW");
		}
		
	}  
	
	public String saveProductInformation( ) throws Exception {
		
		trace("saveProductInformation started....");
		HttpSession session = getRequest().getSession();
		
		
		
		String instid = (String)session.getAttribute("Instname");
		String usercode = comUserCode(session);
		IfpTransObj transact = commondesc.myTranObject("REF", txManager);
		int cardorderinsert =-1;
		int appidinsert = -1;
		int updateref = -1;
		int custinfoinsert = -1;
		int acctinfo = -1;
		String customerid = "";
		try{
			String	CIN_NO ="";
			
			String orderrefno = commondesc.generateorderRefno(instid, jdbctemplate);
			trace("Generated order reference number is : " + orderrefno);
			
			if(dbtcustregbean.getCinidbasedon().equals("CBS"))
			{
				trace("customerid based on cbs true");
				customerid=dbtcustregbean.getCustomeridno();
			}
			else
			{
				trace("customerid based on cbs false");
				//customerid = commondesc.cinnumberGeneratoer(instid,jdbctemplate);
				/*List SEQ = GetOrderNumber( instid,jdbctemplate );
				String order="",cin="";
				Iterator itr = SEQ.iterator();
				while (itr.hasNext()) {
					Map map = (Map) itr.next();
					order = ((String) map.get("ORDER_REFNO"));
					cin = ((String) map.get("CIN_NO"));
					
			
				}
				 // Integer Counter = new Integer( 1 );
				int Counter = Integer.parseInt(cin)+1;
				trace("counter for cin"+Counter);
				CIN_NO = instid+GenerateRandom4Digit()+String.format("%09d", Counter);*/

				CIN_NO = commondesc.cinnumberGeneratoer(instid,jdbctemplate);

				 	
			
			}
			trace("Generated customer id from  : "+dbtcustregbean.getCinidbasedon()+"::" + customerid);
			
			String cardtypeid = commondesc.getCardType(instid, dbtcustregbean.getProductcode(), jdbctemplate);
			trace("Getting cardtype id ...got : " + cardtypeid);
			if( cardtypeid == null ){
				addActionError("Could not get card type id .....");
				return  ERRORHOME;
			}
			dbtcustregbean.setCardtypeid(cardtypeid);
			
			String bin = commondesc.getBin(instid, dbtcustregbean.getProductcode(), jdbctemplate);
			if( bin == null ){
				addActionError("Could not get bin code for the product");
				return 	ERRORHOME;
			}
			dbtcustregbean.setBin(bin);
			//String applicationid = dbtcustregbean.getEmbname()+"-"+orderrefno;
			String applicationid = orderrefno;
			trace("applicationid---"+applicationid);  
			dbtcustregbean.setUsercode(usercode);   
			
			trace(" bean details ====  "+dbtcustregbean);
			
			cardorderinsert = dbtcustdao.insertCardOrderPersonal(instid, orderrefno, customerid, dbtcustregbean, jdbctemplate);
			trace("Inserting order reference details...got : " + cardorderinsert );
			
			String limitBasedon = commondesc.getLimitBasedOn(instid, dbtcustregbean.getLimitid(), jdbctemplate);
			trace("limit based on ....."+limitBasedon);
			dbtcustregbean.setLimitbasedon(limitBasedon);
			//String reglevel = creditdao.getCustomerRegLevel(instid, orderrefno, jdbctemplate);
			//trace("Getting reglevel....got : " + reglevel );
			
			appidinsert = dbtcustdao.insertCustomerRegisterationLevel(instid, orderrefno, "2", dbtcustregbean, jdbctemplate);
			
			custinfoinsert = dbtcustdao.insertCustomerDetails(instid, orderrefno , customerid, usercode, jdbctemplate);
			
			acctinfo = dbtcustdao.insertAccountInfo(instid, orderrefno ,limitBasedon, customerid, usercode,dbtcustregbean, jdbctemplate);
			 
			
			dbtcustregbean.setUsercode(usercode);
			String updaterefqry = commondesc.updatecustidcount(instid);
			
			
			/*updateref = jdbctemplate.update(updaterefqry);*/
			//by gowtham-220819
			updateref = jdbctemplate.update(updaterefqry,new Object[]{instid});
			
			System.out.println("referance::::"+cardorderinsert +appidinsert+updateref+custinfoinsert+acctinfo);
			
			
			if(cardorderinsert == 1 && appidinsert ==1 && updateref ==1 && custinfoinsert ==1 && acctinfo==1)
			{
				transact.txManager.commit(transact.status);
				addActionMessage("Success !!...Generate Card Reference Number is : " +orderrefno  );
				dbtcustregbean.setReglevel("2");
				dbtcustregbean.setApplicationid(applicationid);  
				
				dbtcustregbean.setBranchcode(dbtcustregbean.getBranchcode());
				dbtcustregbean.setEmbname(dbtcustregbean.getEmbname());
				dbtcustregbean.setEncname(dbtcustregbean.getEncname());
				dbtcustregbean.setProductcode(dbtcustregbean.getProductcode());
				dbtcustregbean.setSubproduct(dbtcustregbean.getSubproduct());
				dbtcustregbean.setLimitbasedon(dbtcustregbean.getLimitbasedon());
				dbtcustregbean.setLimitid(dbtcustregbean.getLimitid());
				dbtcustregbean.setFeecode(dbtcustregbean.getFeecode());
				dbtcustregbean.setCustomeridno(dbtcustregbean.getCustomeridno());
				dbtcustregbean.setRenewal(dbtcustregbean.getRenewal());
				
				trace("getBranchcode::"+dbtcustregbean.getBranchcode());
				trace("getEmbname::"+dbtcustregbean.getEmbname());
				trace("getEncname:::"+dbtcustregbean.getEncname());
				trace("getProductcode:::"+dbtcustregbean.getProductcode());
				trace("getSubproduct:::"+dbtcustregbean.getSubproduct());
				trace("getLimitbasedon:::"+dbtcustregbean.getLimitbasedon());
				trace("getFeecode:::"+dbtcustregbean.getFeecode());    
				trace("getCustomeridno:::"+dbtcustregbean.getCustomeridno());    
				
				trace("getAccounttypevalue:::"+dbtcustregbean.getAccounttypevalue());
				trace("getAccountsubtypevalue:::"+dbtcustregbean.getAccountsubtypevalue());
				trace("getTab2_currency:::"+dbtcustregbean.getTab2_currency());
				trace("getAccountnovalue:::"+dbtcustregbean.getAccountnovalue());
				
				   
				List branchlist = commondesc.generateBranchList(instid, jdbctemplate);
				if( branchlist.isEmpty() ){
					addActionError("Could not get branch list");
					return "required_home";
				}
				dbtcustregbean.setBranchlist(branchlist);
				
							
				
				trace("Getting Product List ....");
				List productlist = commondesc.getProductList(instid, jdbctemplate, dbtcustregbean.getBranchcode());
				trace("Got Product List"+productlist);
				   
				dbtcustregbean.setProductlist(productlist);  
				
				trace("Getting sub-Product List ....");
				List suproductlist = commondesc.getSubProductList(instid, dbtcustregbean.getProductcode(), jdbctemplate);
				trace("Got Sub - Product List"+suproductlist);
				
				dbtcustregbean.setSubproductlist(suproductlist);
				
				String limitname = commondesc.getLimitDesc(instid, dbtcustregbean.getLimitid(), jdbctemplate);
				dbtcustregbean.setLimitname(limitname);
				
				String feename = commondesc.getFeeDescription(instid, dbtcustregbean.getFeecode(), jdbctemplate);
				dbtcustregbean.setFeename(feename);
				
				 
				trace("setting accttypelist");
				List accttypelist = dbtcustdao.getAcctTypeList(instid, jdbctemplate);
				dbtcustregbean.setAccttypelist(accttypelist);  
				
				trace("set account no length");
				dbtcustregbean.setAccountnolength(commondesc.getAccountNoLength(instid, jdbctemplate));
				
				List documentlist = commondesc.gettingDocumnettype(instid, jdbctemplate); 
				if( !documentlist.isEmpty() ){ 
					trace("Got list of master document...:" + documentlist.size());
					dbtcustregbean.setDocumentlist(documentlist);
				}
				
				
				String renewalflag = dbtcustregbean.getRenewal();
				if("N".equalsIgnoreCase(renewalflag)){
					dbtcustregbean.setRenewal("No");
				}else{
					dbtcustregbean.setRenewal("Yes");
				}
				
				trace("setting currency list");
				
				List currencylist = commondesc.getCurList(instid, jdbctemplate);
				dbtcustregbean.setCurrencylist(currencylist);
				
				//dbtcustregbean.setTab1Status("");     
				//dbtcustregbean.setTab2Status("");
				dbtcustregbean.setTab3Status("true");
				dbtcustregbean.setTab4Status("true");
				
				
				
				
			} 
			else
			{
				transact.txManager.rollback(transact.status);
				trace("cardorderinsert:::: " +cardorderinsert );
				trace("appidinsert:::: " +appidinsert );
				trace("updateref:::: " + updateref);
				addActionError("Unable to continue the process");
			}
			
			
		}catch(Exception e ){
			trace("Exception : " + e.getMessage());
			addActionError("Unable to continue the process");   
			transact.txManager.rollback(transact.status);
			e.printStackTrace();
			
			
		}
		
		trace("saveProductInformation ended....");
		return "debitreg_entry";
	}  
	
	
	public String customerDetailsAdd() throws Exception {
		
		
		System.out.println("customerDetailsAdd calledd .......");
		trace("customerDetailsAdd calledd .......");
		
		HttpSession session = getRequest().getSession();
		
				
		
		
		String instid = (String)session.getAttribute("Instname");
		String usercode = comUserCode(session);
		IfpTransObj transact = commondesc.myTranObject("REF", txManager);
		int result =-1;
		String orderrefno = getRequest().getParameter("applicationid");
		String customerid = getRequest().getParameter("customerid");
		
		String accounttypevalue = getRequest().getParameter("accounttypevalue");
		String accountsubtypevalue = getRequest().getParameter("accountsubtypevalue");
		String tab2_currency = getRequest().getParameter("tab2_currency");
		String accountnovalue = getRequest().getParameter("accountnovalue");
		
		
		trace("accounttypevalue::{"+accounttypevalue+"}:::accountsubtypevalue{"+accountsubtypevalue+"tab2_currency:"+tab2_currency+"accountnovalue"+accountnovalue);
		
		/*
		String accounttypevalue[] = getRequest().getParameterValues("accounttypevalue");
		String accountnovalue[] = getRequest().getParameterValues("accountnovalue");
		
		for (int i = 0; i < accounttypevalue.length; i++) {
			
			System.out.println("accounttypevalue----"+accounttypevalue[i]);
			System.out.println("accountnovalue----"+accountnovalue[i]);
			
		}   
		*/
		
		int updatecustlevel = -1;
		int updcustdetails = -1;
		int updateacctdetails = -1;
		Iterator itr1 = null;
		try{
			
			
			
			
			System.out.println("===getFirstname==="+dbtcustregbean.getMiddlename());                      
			System.out.println("===getFirstname==="+dbtcustregbean.getAccountnovalue());  
			
			System.out.println("orderrefno ::"+orderrefno);  
			
			updatecustlevel = dbtcustdao.updateCustomerRegisterationLevel(instid, orderrefno, "3", dbtcustregbean, jdbctemplate);
			
			updcustdetails = dbtcustdao.updateCustomerContactPersionalDetails(instid, orderrefno,usercode, "4", dbtcustregbean, jdbctemplate);
			
	//		updateacctdetails = dbtcustdao.updateAccountDetails(instid, orderrefno,usercode, accounttypevalue, accountsubtypevalue,tab2_currency,accountnovalue, dbtcustregbean, jdbctemplate);
			
			//result = dbtcustdao.insertCustomerDetails(instid, usercode, customerid, dbtcustregbean, jdbctemplate); 
			trace("customerDetailsAdd : " + updatecustlevel+updcustdetails+updateacctdetails );
			
			if(updatecustlevel==1 && updcustdetails ==1 )       
			{
				
				transact.txManager.commit(transact.status);
				
				dbtcustregbean.setReglevel("3");
				dbtcustregbean.setApplicationid(orderrefno);     
				
				
				trace("getting product details........");
				List productDetails = dbtcustdao.getApplicationData(instid,orderrefno, jdbctemplate);
				Iterator itr = productDetails.iterator(); 
				while( itr.hasNext() ){
					Map mp = (Map)itr.next();
					  
				dbtcustregbean.setBranchcode((String) mp.get("BRANCH_CODE"));
				dbtcustregbean.setEmbname((String) mp.get("EMBOSSING_NAME"));
				dbtcustregbean.setEncname((String) mp.get("ENCODE_DATA"));
				dbtcustregbean.setProductcode((String) mp.get("PRODUCT_CODE"));
				dbtcustregbean.setSubproduct((String) mp.get("SUB_PROD_ID"));
				dbtcustregbean.setLimitbasedon((String) mp.get("LIMIT_BASEDON"));
				dbtcustregbean.setLimitid((String) mp.get("LIMIT_ID"));
				dbtcustregbean.setFeecode((String) mp.get("FEE_CODE"));
				if(session.getAttribute("USERTYPE")!="INSTADMIN")
				{
				dbtcustregbean.setCustomeridno((String) mp.get("CIN"));
				}
				}   
				
				trace("getting Customer details........");
				List customerDetails = dbtcustdao.getCustomerData(instid,orderrefno, jdbctemplate);
				itr1 = customerDetails.iterator(); 
				while( itr1.hasNext() ){  
					Map mp = (Map)itr1.next();  
					System.out.println("====f"+(String) mp.get("MOBILE"));
						dbtcustregbean.setMobile((String) mp.get("MOBILE"));                      
						dbtcustregbean.setEmail((String) mp.get("E_MAIL"));                       
						dbtcustregbean.setP_poxbox((String) mp.get("P_PO_BOX"));                       
						dbtcustregbean.setP_houseno((String) mp.get("P_HOUSE_NO"));                         
						dbtcustregbean.setP_streetname((String) mp.get("P_STREET_NAME"));                      
						dbtcustregbean.setP_wardnumber((String) mp.get("P_WARD_NAME"));              
						dbtcustregbean.setP_city((String) mp.get("P_CITY"));                 
						dbtcustregbean.setP_district((String) mp.get("P_DISTRICT"));           
						dbtcustregbean.setP_phone1((String) mp.get("P_PHONE1"));             
						dbtcustregbean.setP_phone2((String) mp.get("P_PHONE2"));                 
						
						dbtcustregbean.setC_poxbox((String) mp.get("C_PO_BOX"));                       
						dbtcustregbean.setC_houseno((String) mp.get("C_HOUSE_NO"));                         
						dbtcustregbean.setC_streetname((String) mp.get("C_STREET_NAME"));                      
						dbtcustregbean.setC_wardnumber((String) mp.get("C_WARD_NAME"));              
						dbtcustregbean.setC_city((String) mp.get("C_CITY"));                 
						dbtcustregbean.setC_district((String) mp.get("C_DISTRICT"));           
						dbtcustregbean.setC_phone1((String) mp.get("C_PHONE1"));             
						dbtcustregbean.setC_phone2((String) mp.get("C_PHONE2"));
						//dbtcustregbean.setMiddlename((String) mp.get("FATHER_NAME"));
						  
				}   
				
				
				List branchlist = commondesc.generateBranchList(instid, jdbctemplate);
				if( branchlist.isEmpty() ){
					addActionError("Could not get branch list");
					return "required_home";
				}
				dbtcustregbean.setBranchlist(branchlist);
				
				String acctnolen = commondesc.getAccountNoLength(instid, jdbctemplate);
				trace("Getting acct number length ...got : " + acctnolen );
				if( acctnolen == null ){
					addActionError("Account Number length not set for institution ");
					return "required_home";
				}
				dbtcustregbean.setAcctnolen(acctnolen); 
				
				
				trace("setting accttypelist");
				List accttypelist = dbtcustdao.getAcctTypeList(instid, jdbctemplate);
				dbtcustregbean.setAccttypelist(accttypelist);  
				
				
				trace("Getting Product List ....");
				List productlist = commondesc.getProductList(instid, jdbctemplate, dbtcustregbean.getBranchcode());
				trace("Got Product List"+productlist);
				   
				dbtcustregbean.setProductlist(productlist);  
				
				trace("Getting sub-Product List ....");
				List suproductlist = commondesc.getSubProductList(instid, dbtcustregbean.getProductcode(), jdbctemplate);
				trace("Got Sub - Product List"+suproductlist);
				
				dbtcustregbean.setSubproductlist(suproductlist);
				
				String limitname = commondesc.getLimitDesc(instid, dbtcustregbean.getLimitid(), jdbctemplate);
				dbtcustregbean.setLimitname(limitname);
				
				String feename = commondesc.getFeeDescription(instid, dbtcustregbean.getFeecode(), jdbctemplate);
				dbtcustregbean.setFeename(feename);
				
							
				trace("setting getAcctSubTypeList ");
				List  subaccounttypelist = dbtcustdao.getAcctSubTypeList(instid, dbtcustregbean.getAccounttypevalue(), jdbctemplate);
				dbtcustregbean.setAccuntsubtypelist(subaccounttypelist);
				
				trace("setting currency list");
				List currencylist = commondesc.getCurList(instid, jdbctemplate);
				dbtcustregbean.setCurrencylist(currencylist);
				
				List documentlist = commondesc.gettingDocumnettype(instid, jdbctemplate); 
				if( !documentlist.isEmpty() ){ 
					trace("Got list of master document...:" + documentlist.size());
					dbtcustregbean.setDocumentlist(documentlist);
				}
				 
				
				trace("set account no length");
				dbtcustregbean.setAccountnolength(commondesc.getAccountNoLength(instid, jdbctemplate));
				
				trace("setting currency list");
				dbtcustregbean.setCurrencylist(commondesc.getCurList(instid, jdbctemplate));
				
				//dbtcustregbean.setTab1Status("");     
				//dbtcustregbean.setTab2Status("");
				//dbtcustregbean.setTab3Status("");
				dbtcustregbean.setTab4Status("true");
				  
				//transact.txManager.commit(transact.status);
				
			}
			else
			{
				transact.txManager.rollback(transact.status);
				addActionError("Unable to continue the process");
			}
			   
		}catch(Exception e ){
			transact.txManager.rollback(transact.status);
			e.printStackTrace();
			trace("Exception : " + e.getMessage());
			addActionError("Unable to continue the process");
		}
		
		System.out.println("customerDetailsAdd ended .......");
		trace("customerDetailsAdd ended .......");
		return "debitreg_entry";
		
	}
	
	
public String contactDetailsAdd() throws Exception {
		
		HttpSession session = getRequest().getSession();
		
		System.out.println("contactDetailsAdd calledd .......");
		trace("contactDetailsAdd calledd .......");
		
		
		
		
		String instid = (String)session.getAttribute("Instname");
		String usercode = comUserCode(session);
		IfpTransObj transact = commondesc.myTranObject("REF", txManager);
		int result =-1;
		String orderrefno = getRequest().getParameter("applicationid");
		String customerid = getRequest().getParameter("customerid");
		int updatecustlevel = -1;
		int updcustdetails = -1;
		Iterator itr1 = null;
		try{
			
			
			
			
			System.out.println("======"+dbtcustregbean.getFirstname());                      
			System.out.println("==midddd===="+dbtcustregbean.getMiddlename());  
			
			System.out.println("orderrefno ::"+orderrefno);  
			
			
			trace("getting Customer details........");
			List customerDetails = dbtcustdao.getCustomerData(instid,orderrefno, jdbctemplate);
			itr1 = customerDetails.iterator(); 
			while( itr1.hasNext() ){  
				Map mp = (Map)itr1.next();  
				System.out.println("====f"+(String) mp.get("MNAME"));
					dbtcustregbean.setFirstname((String) mp.get("FNAME"));                      
					dbtcustregbean.setMiddlename((String) mp.get("MNAME"));                       
					dbtcustregbean.setLastname((String) mp.get("LNAME"));                       
					dbtcustregbean.setDob((String) mp.get("DOB"));                         
					dbtcustregbean.setGender((String) mp.get("GENDER"));                      
					dbtcustregbean.setMstatus((String) mp.get("MARITAL_STATUS"));              
					dbtcustregbean.setNationality((String) mp.get("NATIONALITY"));                 
					dbtcustregbean.setDocumentprovided((String) mp.get("DOCUMENT_PROVIDED"));           
					dbtcustregbean.setDocumentnumber((String) mp.get("DOCUMENT_NUMBER"));             
					dbtcustregbean.setSpousename((String) mp.get("SPOUCE_NAME"));                 
					dbtcustregbean.setMothername((String) mp.get("MOTHER_NAME"));                 
					dbtcustregbean.setFathername((String) mp.get("FATHER_NAME"));  
					//dbtcustregbean.setMiddlename((String) mp.get("FATHER_NAME"));
					  
			}   
			
			
			
			updatecustlevel = dbtcustdao.updateCustomerRegisterationLevel(instid, orderrefno, "3", dbtcustregbean, jdbctemplate);
			
			updcustdetails = dbtcustdao.updateCustomerContactPersionalDetails(instid, orderrefno,usercode, "4", dbtcustregbean, jdbctemplate);
			
			//result = dbtcustdao.insertCustomerDetails(instid, usercode, customerid, dbtcustregbean, jdbctemplate); 
			trace("updatecustlevel : " + updatecustlevel );
			
			if(updatecustlevel==1 && updcustdetails ==1)       
			{
				dbtcustregbean.setReglevel("4");
				dbtcustregbean.setApplicationid(orderrefno);     
				
				
				trace("getting product details........");
				List productDetails = dbtcustdao.getApplicationData(instid,orderrefno, jdbctemplate);
				Iterator itr = productDetails.iterator(); 
				while( itr.hasNext() ){
					Map mp = (Map)itr.next();
								
							dbtcustregbean.setBranchcode((String) mp.get("BRANCH_CODE"));
							dbtcustregbean.setEmbname((String) mp.get("EMBOSSING_NAME"));
							dbtcustregbean.setEncname((String) mp.get("ENCODE_DATA"));
							dbtcustregbean.setProductcode((String) mp.get("PRODUCT_CODE"));
							dbtcustregbean.setSubproduct((String) mp.get("SUB_PROD_ID"));
							dbtcustregbean.setLimitbasedon((String) mp.get("LIMIT_BASEDON"));
							dbtcustregbean.setLimitid((String) mp.get("LIMIT_ID"));
							dbtcustregbean.setFeecode((String) mp.get("FEE_CODE"));
							dbtcustregbean.setCustomeridno((String) mp.get("CIN"));
				}   
				System.out.println(dbtcustregbean.getBranchcode());
				System.out.println(dbtcustregbean.getEmbname());     
				System.out.println(dbtcustregbean.getEncname());
				System.out.println(dbtcustregbean.getProductcode());
				System.out.println(dbtcustregbean.getSubproduct());
				System.out.println(dbtcustregbean.getLimitbasedon());
				System.out.println(dbtcustregbean.getFeecode());    
				   
				
				trace("getting product details........");
				List accounttypedetails = dbtcustdao.getAccounttypeinfo(instid,orderrefno, jdbctemplate);
				itr = accounttypedetails.iterator(); 
				while( itr.hasNext() ){
					Map mp = (Map)itr.next();
				System.out.println("-----------------test--"+(String) mp.get("ACCTTYPE_ID"));	  
				dbtcustregbean.setAccounttypevalue((String) mp.get("ACCTTYPE_ID"));
				dbtcustregbean.setAccountsubtypevalue((String) mp.get("ACCTSUB_TYPE_ID"));
				dbtcustregbean.setTab2_currency((String) mp.get("ACCT_CURRENCY"));
				dbtcustregbean.setAccountnovalue((String) mp.get("ACCOUNTNO"));
				dbtcustregbean.setAccountnolength(commondesc.getAccountNoLength(instid, jdbctemplate));
				
				}   
				List branchlist = commondesc.generateBranchList(instid, jdbctemplate);
				if( branchlist.isEmpty() ){
					addActionError("Could not get branch list");
					return "required_home";
				}
				dbtcustregbean.setBranchlist(branchlist);  
				
				trace("Getting Product List ....");
				List productlist = commondesc.getProductList(instid, jdbctemplate, dbtcustregbean.getBranchcode());
				trace("Got Product List"+productlist);
				   
				dbtcustregbean.setProductlist(productlist);  
				
				trace("Getting sub-Product List ....");
				List suproductlist = commondesc.getSubProductList(instid, dbtcustregbean.getProductcode(), jdbctemplate);
				trace("Got Sub - Product List"+suproductlist);
				
				dbtcustregbean.setSubproductlist(suproductlist);
				
				String limitname = commondesc.getLimitDesc(instid, dbtcustregbean.getLimitid(), jdbctemplate);
				dbtcustregbean.setLimitname(limitname);
				
				String feename = commondesc.getFeeDescription(instid, dbtcustregbean.getFeecode(), jdbctemplate);
				dbtcustregbean.setFeename(feename);
				
				trace("setting accttypelist");
				List accttypelist = dbtcustdao.getAcctTypeList(instid, jdbctemplate);
				dbtcustregbean.setAccttypelist(accttypelist); 
				
				trace("setting getAcctSubTypeList ");
				List  subaccounttypelist = dbtcustdao.getAcctSubTypeList(instid, dbtcustregbean.getAccounttypevalue(), jdbctemplate);
				dbtcustregbean.setAccuntsubtypelist(subaccounttypelist);
				
				trace("setting currency list");
				List currencylist = commondesc.getCurList(instid, jdbctemplate);
				dbtcustregbean.setCurrencylist(currencylist);
				
				 
				
				trace("set account no length");
				dbtcustregbean.setAccountnolength(commondesc.getAccountNoLength(instid, jdbctemplate));
				
				
				//dbtcustregbean.setTab1Status("true");     
				//dbtcustregbean.setTab2Status("true");
				//dbtcustregbean.setTab3Status("true");  
				//dbtcustregbean.setTab4Status("true");
				
			//	trace("setting currency list");
			//	dbtcustregbean.setCurrencylist(commondesc.getCurList(instid, jdbctemplate));
				  
				
				transact.txManager.commit(transact.status);
			}
			else
			{
				transact.txManager.rollback(transact.status);
				addActionError("Unable to continue the process");
			}
			   
		}catch(Exception e ){
			transact.txManager.rollback(transact.status);
			e.printStackTrace();
			trace("Exception : " + e.getMessage());
			addActionError("Unable to continue the process");
		}
		System.out.println("contactDetailsAdd ended .......");
		trace("contactDetailsAdd ended .......");
		return "debitreg_entry";
		
	}



public String authorizecardOrder() throws Exception
{
    trace ("dbtcustre gbean------------>"+dbtcustregbean);
	trace("authorizecardOrder started ....");
	EmailService emailservice = new EmailService();
	HttpSession session =getRequest().getSession();
	String instid = (String)session.getAttribute("Instname");
	String orderrefno = getRequest().getParameter("tab4_applicationid");
	String usercode = comUserCode(session);
	String username=comUsername();
	String act = getRequest().getParameter("act");
	trace("act ..."+act);
	IfpTransObj transact = commondesc.myTranObject("AUTHCORDER", txManager);
	
	//String mkrckr = "M";
	String mkrckr = "P";
	
	//added by gowtham_220719
	String  ip=(String) session.getAttribute("REMOTE_IP");

	
	if(act.equals("C"))
	{
		mkrckr = "P";
	}
	
	try{
			int authStatus = dbtcustdao.authorizeCardOrder(instid,dbtcustregbean.getProductcode(), dbtcustregbean.getSubproduct(),dbtcustregbean.getLimitid(),dbtcustregbean.getFeecode(), orderrefno, usercode, mkrckr, jdbctemplate);
				if(authStatus==1)
				{
					/*
					String content = "",subject="",mailreq="";
					List mailconfiglistlist =jdbctemplate.queryForList("SELECT * FROM IFD_MAILALERTCONFIG WHERE ACTIONNAME='CARDORDER' and instid='"+instid+"'");
					if(!(mailconfiglistlist.isEmpty()))     
					{
						Iterator itr1 = mailconfiglistlist.iterator();
						 while( itr1.hasNext()){
							Map temp=(Map) itr1.next();
							content=(String) temp.get("MAIL_DATA");
							subject=(String) temp.get("SUBJECT");
							mailreq=(String) temp.get("MAIL_REQ");
						 }
					}        
					content = content.replaceAll("#LB#", "\n");    
					content = content.replaceAll("#LT#", "\t"); 
					content = content.replaceAll("#ORDERREFNO#", orderrefno); 
					
					String email = commondesc.getUserEmailFromTemp(instid, usercode, jdbctemplate);
			    	trace("Getting email for the usercode....got : " + email );
			    	if( email == null ){
			    		return null;
			    	}
			    	String sendmail = emailservice.sendMail(email, subject, content);
			    	
			    	trace("mail sent successfully"+sendmail);      
					*/   
					
					
					addActionMessage(orderrefno+"...Order Authorized Successfully  " );
					trace("authorizecardOrder Authorized successfullly");
					transact.txManager.commit(transact.status);
					
					try{ 
						
						//added by gowtham_220719
						trace("ip address======>  "+ip);
						auditbean.setIpAdress(ip);
						
						auditbean.setActmsg("Register Details for Card Refference Number[ "+ orderrefno  +" ] is Authorized ");
						auditbean.setUsercode(username);
						auditbean.setAuditactcode("9009"); 
						commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
					 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
																	
				}
				else
				{   addActionError(orderrefno+"...Card Not Authorized  " );
					trace("authorizecardOrder Rollbacked");
					transact.txManager.rollback(transact.status);
				}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			trace("authorizecardOrder Rollbacked  "+e.getMessage());
			transact.txManager.rollback(transact.status);
		}
	trace("authorizecardOrder ended ....");
	return "required_home";  
} 


public String submitcardOrder() throws Exception
{
	String orderrefno = getRequest().getParameter("tab4_applicationid");
	
	
	HttpSession session =getRequest().getSession();
	
	
			/*// by siva 210819
			HttpSession ses = getRequest().getSession();
			String sessioncsrftoken = (String) ses.getAttribute("csrfToken");
			String jspcsrftoken = getRequest().getParameter("token");
			System.out.println("jspcsrftoken----->    "+jspcsrftoken);
			if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) {
				ses.setAttribute("message", "CSRF Token Mismatch");
				 addActionError("CSRF Token Mismatch"); 
				return "invaliduser";
			}
			// by siva 210819*/	
	
	
	String instid = (String)session.getAttribute("Instname");
	String usercode = comUserCode(session);
	String username=comUsername();
	IfpTransObj transact = commondesc.myTranObject("SUBMITCORDER", txManager);
	int authStatus = 0;
	
	
	//added by gowtham_220719
			String  ip=(String) session.getAttribute("REMOTE_IP");
	
	try{
	authStatus = dbtcustdao.ConfirmCardOrder(instid, orderrefno, usercode, jdbctemplate);
	}catch(Exception e)
	{
		transact.txManager.rollback(transact.status);
	}
	if(authStatus==1)
	{
		addActionMessage(orderrefno+"...Card Ordered Successfully  Waiting for Authorization: " );
		transact.txManager.commit(transact.status);
		
		try{ 
			
			//added by gowtham_220719
			trace("ip address======>  "+ip);
			auditbean.setIpAdress(ip);
			
			auditbean.setActmsg("Customer Registration Details for [ "+ orderrefno  +" - " +" ] is Registred Successfully.waiting for Authorization ");
			auditbean.setUsercode(username);
			auditbean.setAuditactcode("9009"); 
			commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
		 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
	}
	else
	{
		addActionError(orderrefno+"...Card Ordered With Exception ... Unable to Continue .... " );
		transact.txManager.rollback(transact.status);
	}
	
	
	return "required_home";
}
	
	
	//Search customer
		public String customerSearch() throws Exception {
			
			trace("customerSearch method called");
			
			HttpSession session =getRequest().getSession();
			String instid = (String)session.getAttribute("Instname");
			String branch = getRequest().getParameter("branchcode");
			String binno = getRequest().getParameter("cardtype");
			String fromdate = getRequest().getParameter("fromdate");
			String todate = getRequest().getParameter("todate");
			String dateflag ="ORDERED_DATE";
			String applicationid = getRequest().getParameter("applicationid");
			//String dob = getRequest().getParameter("dob");
			//String customername = getRequest().getParameter("customername");
			String doact = getRequest().getParameter("doact");
			String branchcondition ="" , checkcondition = "",applicationidcond = "";
			if( doact != null ){
				dbtcustregbean.setDoact( doact ) ;
			}
			trace("got search request applicationid ["+applicationid+"]   ");
			try{
				
				
				String usertype=(String)session.getAttribute("USERTYPE");
				if(!usertype.equals("INSTADMIN") || !branch.equals("ALL"))
				{
					branchcondition = " AND BRANCH_CODE = '"+branch+"'";
				}
				if("ALL".equalsIgnoreCase(branch)){
					branchcondition= "";
				}
				if("ALL".equalsIgnoreCase(applicationid)){
					applicationidcond = "";
				}else{
					applicationidcond = " AND ORDER_REF_NO='"+applicationid+"'";
				}
				checkcondition = branchcondition +" AND ORDER_STATUS='01' AND MKCK_STATUS='M' "+applicationidcond+"";
				
				System.out.println("checkcondition::::"+checkcondition);
				trace("getting product details........");
				List productDetails = dbtcustdao.getApplicationDataByBranch(instid,checkcondition, jdbctemplate);
				Iterator itr = productDetails.iterator(); 
				while( itr.hasNext() ){
					Map mp = (Map)itr.next();
					String branchcode = (String) mp.get("BRANCH_CODE");
				dbtcustregbean.setBranchcode(commondesc.getBranchDesc(instid, branchcode, jdbctemplate));
				dbtcustregbean.setEmbname((String) mp.get("EMBOSSING_NAME"));
				dbtcustregbean.setEncname((String) mp.get("ENCODE_DATA"));
				dbtcustregbean.setProductcode((String) mp.get("PRODUCT_CODE"));
				dbtcustregbean.setSubproduct((String) mp.get("SUB_PROD_ID"));
				dbtcustregbean.setLimitbasedon((String) mp.get("LIMIT_BASEDON"));
				dbtcustregbean.setLimitid((String) mp.get("LIMIT_ID"));
				dbtcustregbean.setFeecode((String) mp.get("FEE_CODE"));
				dbtcustregbean.setCustomeridno((String) mp.get("CIN")); 
				String renewalflag = (String) mp.get("RENEWALFLAG"); 
				if("N".equalsIgnoreCase(renewalflag)){
					dbtcustregbean.setRenewal("No");
				}else{
					dbtcustregbean.setRenewal("Yes");
				}	
				dbtcustregbean.setCollectbranch((String) mp.get("CARD_COLLECT_BRANCH"));
				}   
				List instConfDet = commondesc.getConfigDetails(instid, jdbctemplate);
				trace("InstConfigDetails : " + instConfDet );
				
				Iterator itr3 = instConfDet.iterator(); 
				while( itr3.hasNext() ){
					Map mp = (Map)itr3.next();        
					dbtcustregbean.setCinidbasedon((String) mp.get("CUSTID_BASEDON"));
					}
				
				System.out.println("cinbased:::"+dbtcustregbean.getCinidbasedon());    
				
				List customerDetails = dbtcustdao.getCustomerData(instid,applicationid, jdbctemplate);
				Iterator itr1 = customerDetails.iterator(); 
				while( itr1.hasNext() ){  
					Map mp = (Map)itr1.next();  
					//System.out.println("====f"+(String) mp.get("MOBILE"));
						dbtcustregbean.setFirstname((String) mp.get("FNAME"));                      
						dbtcustregbean.setMiddlename((String) mp.get("MNAME"));                       
						dbtcustregbean.setLastname((String) mp.get("LNAME"));                       
						dbtcustregbean.setDob((String) mp.get("DOB"));                         
						dbtcustregbean.setGender((String) mp.get("GENDER"));                      
						dbtcustregbean.setMstatus((String) mp.get("MARITAL_STATUS"));              
						dbtcustregbean.setNationality((String) mp.get("NATIONALITY"));                 
						dbtcustregbean.setDocumentprovided((String) mp.get("DOCUMENT_PROVIDED"));           
						dbtcustregbean.setDocumentnumber((String) mp.get("DOCUMENT_NUMBER"));             
						dbtcustregbean.setSpousename((String) mp.get("SPOUCE_NAME"));                 
						dbtcustregbean.setMothername((String) mp.get("MOTHER_NAME"));                 
						dbtcustregbean.setFathername((String) mp.get("FATHER_NAME"));  
					
						dbtcustregbean.setMobile((String) mp.get("MOBILE"));                      
						dbtcustregbean.setEmail((String) mp.get("E_MAIL"));                       
						dbtcustregbean.setP_poxbox((String) mp.get("P_PO_BOX"));                       
						dbtcustregbean.setP_houseno((String) mp.get("P_HOUSE_NO"));                         
						dbtcustregbean.setP_streetname((String) mp.get("P_STREET_NAME"));                      
						dbtcustregbean.setP_wardnumber((String) mp.get("P_WARD_NAME"));              
						dbtcustregbean.setP_city((String) mp.get("P_CITY"));                 
						dbtcustregbean.setP_district((String) mp.get("P_DISTRICT"));           
						dbtcustregbean.setP_phone1((String) mp.get("P_PHONE1"));             
						dbtcustregbean.setP_phone2((String) mp.get("P_PHONE2"));                 
						
						dbtcustregbean.setC_poxbox((String) mp.get("C_PO_BOX"));                       
						dbtcustregbean.setC_houseno((String) mp.get("C_HOUSE_NO"));                         
						dbtcustregbean.setC_streetname((String) mp.get("C_STREET_NAME"));                      
						dbtcustregbean.setC_wardnumber((String) mp.get("C_WARD_NAME"));              
						dbtcustregbean.setC_city((String) mp.get("C_CITY"));                 
						dbtcustregbean.setC_district((String) mp.get("C_DISTRICT"));           
						dbtcustregbean.setC_phone1((String) mp.get("C_PHONE1"));             
						dbtcustregbean.setC_phone2((String) mp.get("C_PHONE2"));
						//dbtcustregbean.setMiddlename((String) mp.get("FATHER_NAME"));
						
				}   
				
				
				trace("getting product details........");
				List accounttypedetails = dbtcustdao.getAccounttypeinfo(instid,applicationid, jdbctemplate);
				itr = accounttypedetails.iterator(); 
				while( itr.hasNext() ){
					Map mp = (Map)itr.next();
				System.out.println("-----------------test--"+(String) mp.get("ACCTTYPE_ID"));	  
				dbtcustregbean.setAccounttypevalue((String) mp.get("ACCTTYPE_ID"));
				dbtcustregbean.setAccountsubtypevalue((String) mp.get("ACCTSUB_TYPE_ID"));
				dbtcustregbean.setTab2_currency((String) mp.get("ACCT_CURRENCY"));
				dbtcustregbean.setAccountnovalue((String) mp.get("ACCOUNTNO"));
				dbtcustregbean.setAccountnolength(commondesc.getAccountNoLength(instid, jdbctemplate));
				
				}   
				
				trace("Getting Product List ....");
				List productlist = commondesc.getProductList(instid, jdbctemplate, dbtcustregbean.getBranchcode());
				trace("Got Product List"+productlist);
				   
				dbtcustregbean.setProductlist(productlist);  
				
				trace("Getting sub-Product List ....");
				List suproductlist = commondesc.getSubProductListForAuth(instid, dbtcustregbean.getProductcode(),dbtcustregbean.getSubproduct(), jdbctemplate);
				trace("Got Sub - Product List"+suproductlist);
				
				dbtcustregbean.setSubproductlist(suproductlist);
				
				String limitname = commondesc.getLimitDesc(instid, dbtcustregbean.getLimitid(), jdbctemplate);
				dbtcustregbean.setLimitname(limitname);
				
				String feename = commondesc.getFeeDescription(instid, dbtcustregbean.getFeecode(), jdbctemplate);
				dbtcustregbean.setFeename(feename);
				
				
				trace("setting accttypelist");
				List accttypelist = dbtcustdao.getAcctTypeList(instid, jdbctemplate);
				dbtcustregbean.setAccttypelist(accttypelist);  
				
				trace("setting getAcctSubTypeList ");
				List  subaccounttypelist = dbtcustdao.getAcctSubTypeList(instid, dbtcustregbean.getAccounttypevalue(), jdbctemplate);
				dbtcustregbean.setAccuntsubtypelist(subaccounttypelist);
				
				trace("setting currency list");
				List currencylist = commondesc.getCurList(instid, jdbctemplate);
				dbtcustregbean.setCurrencylist(currencylist);
				
				
				List documentlist = commondesc.gettingDocumnettype(instid, jdbctemplate); 
				if( !documentlist.isEmpty() ){ 
					trace("Got list of master document...:" + documentlist.size());
					dbtcustregbean.setDocumentlist(documentlist);
				}
				System.out.println("nranchcode:::"+dbtcustregbean.getBranchcode());
				
				
				
			}catch(Exception e ){
				e.printStackTrace();
				trace("Exception...."+e.getMessage());
				addActionError("Unable to continue the process");
				
			} 
			trace("\n");enctrace("\n");
			if("ALL".equalsIgnoreCase(applicationid)){
				List listall = dbtcustdao.getAllRecords(instid,checkcondition, jdbctemplate);	
				authorizeall = listall;
				return "customerSearchAll";
			}else{
				return "customerSearch";
			}
			
		}
		
		
public String EditcustomerSearch() throws Exception {
			
			trace("EditcustomerSearch method called");
			
			HttpSession session =getRequest().getSession();
			String instid = (String)session.getAttribute("Instname");
			String branch = getRequest().getParameter("branchcode");
			String binno = getRequest().getParameter("cardtype");
			String fromdate = getRequest().getParameter("fromdate");
			String todate = getRequest().getParameter("todate");
			String dateflag ="ORDERED_DATE";
			String applicationid = getRequest().getParameter("applicationid");
			//String dob = getRequest().getParameter("dob");
			//String customername = getRequest().getParameter("customername");
			String doact = getRequest().getParameter("doact");
			String branchcondition ="";
			if( doact != null ){
				dbtcustregbean.setDoact( doact ) ;
			}
			trace("got search request applicationid ["+applicationid+"]   ");
			try{
				
				
				String usertype=(String)session.getAttribute("USERTYPE");
				if(!usertype.equals("INSTADMIN"))
				{
					branchcondition = " AND BRANCH_CODE = '"+branch+"'";
				}
								
				
				String checkcondition = branchcondition +" AND ORDER_STATUS='01' AND MKCK_STATUS='M'";
				
				System.out.println("checkcondition::::"+checkcondition);
				trace("getting product details........");
				List productDetails = dbtcustdao.getApplicationDataByBranch(instid,checkcondition, jdbctemplate);
				Iterator itr = productDetails.iterator(); 
				while( itr.hasNext() ){
					Map mp = (Map)itr.next();
					String branchcode = (String) mp.get("BRANCH_CODE");
				dbtcustregbean.setBranchcode(commondesc.getBranchDesc(instid, branchcode, jdbctemplate));
				dbtcustregbean.setEmbname((String) mp.get("EMBOSSING_NAME"));
				dbtcustregbean.setEncname((String) mp.get("ENCODE_DATA"));
				dbtcustregbean.setProductcode((String) mp.get("PRODUCT_CODE"));
				dbtcustregbean.setSubproduct((String) mp.get("SUB_PROD_ID"));
				dbtcustregbean.setLimitbasedon((String) mp.get("LIMIT_BASEDON"));
				dbtcustregbean.setLimitid((String) mp.get("LIMIT_ID"));
				dbtcustregbean.setFeecode((String) mp.get("FEE_CODE"));
				dbtcustregbean.setCustomeridno((String) mp.get("CIN"));    
				
				}   
				List instConfDet = commondesc.getConfigDetails(instid, jdbctemplate);
				trace("InstConfigDetails : " + instConfDet );
				
				Iterator itr3 = instConfDet.iterator(); 
				while( itr3.hasNext() ){
					Map mp = (Map)itr3.next();        
					dbtcustregbean.setCinidbasedon((String) mp.get("CUSTID_BASEDON"));
					}
				
				System.out.println("cinbased:::"+dbtcustregbean.getCinidbasedon());    
				
				List customerDetails = dbtcustdao.getCustomerData(instid,applicationid, jdbctemplate);
				Iterator itr1 = customerDetails.iterator(); 
				while( itr1.hasNext() ){  
					Map mp = (Map)itr1.next();  
					//System.out.println("====f"+(String) mp.get("MOBILE"));
						dbtcustregbean.setFirstname((String) mp.get("FNAME"));                      
						dbtcustregbean.setMiddlename((String) mp.get("MNAME"));                       
						dbtcustregbean.setLastname((String) mp.get("LNAME"));                       
						dbtcustregbean.setDob((String) mp.get("DOB"));                         
						dbtcustregbean.setGender((String) mp.get("GENDER"));                      
						dbtcustregbean.setMstatus((String) mp.get("MARITAL_STATUS"));              
						dbtcustregbean.setNationality((String) mp.get("NATIONALITY"));                 
						dbtcustregbean.setDocumentprovided((String) mp.get("DOCUMENT_PROVIDED"));           
						dbtcustregbean.setDocumentnumber((String) mp.get("DOCUMENT_NUMBER"));             
						dbtcustregbean.setSpousename((String) mp.get("SPOUCE_NAME"));                 
						dbtcustregbean.setMothername((String) mp.get("MOTHER_NAME"));                 
						dbtcustregbean.setFathername((String) mp.get("FATHER_NAME"));  
					
						dbtcustregbean.setMobile((String) mp.get("MOBILE"));                      
						dbtcustregbean.setEmail((String) mp.get("E_MAIL"));                       
						dbtcustregbean.setP_poxbox((String) mp.get("P_PO_BOX"));                       
						dbtcustregbean.setP_houseno((String) mp.get("P_HOUSE_NO"));                         
						dbtcustregbean.setP_streetname((String) mp.get("P_STREET_NAME"));                      
						dbtcustregbean.setP_wardnumber((String) mp.get("P_WARD_NAME"));              
						dbtcustregbean.setP_city((String) mp.get("P_CITY"));                 
						dbtcustregbean.setP_district((String) mp.get("P_DISTRICT"));           
						dbtcustregbean.setP_phone1((String) mp.get("P_PHONE1"));             
						dbtcustregbean.setP_phone2((String) mp.get("P_PHONE2"));                 
						
						dbtcustregbean.setC_poxbox((String) mp.get("C_PO_BOX"));                       
						dbtcustregbean.setC_houseno((String) mp.get("C_HOUSE_NO"));                         
						dbtcustregbean.setC_streetname((String) mp.get("C_STREET_NAME"));                      
						dbtcustregbean.setC_wardnumber((String) mp.get("C_WARD_NAME"));              
						dbtcustregbean.setC_city((String) mp.get("C_CITY"));                 
						dbtcustregbean.setC_district((String) mp.get("C_DISTRICT"));           
						dbtcustregbean.setC_phone1((String) mp.get("C_PHONE1"));             
						dbtcustregbean.setC_phone2((String) mp.get("C_PHONE2"));
						//dbtcustregbean.setMiddlename((String) mp.get("FATHER_NAME"));
						
				}   
				
				
				trace("getting product details........");
				List accounttypedetails = dbtcustdao.getAccounttypeinfo(instid,applicationid, jdbctemplate);
				itr = accounttypedetails.iterator(); 
				while( itr.hasNext() ){
					Map mp = (Map)itr.next();
				System.out.println("-----------------test--"+(String) mp.get("ACCTTYPE_ID"));	  
				dbtcustregbean.setAccounttypevalue((String) mp.get("ACCTTYPE_ID"));
				dbtcustregbean.setAccountsubtypevalue((String) mp.get("ACCTSUB_TYPE_ID"));
				dbtcustregbean.setTab2_currency((String) mp.get("ACCT_CURRENCY"));
				dbtcustregbean.setAccountnovalue((String) mp.get("ACCOUNTNO"));
				dbtcustregbean.setAccountnolength(commondesc.getAccountNoLength(instid, jdbctemplate));
				
				}   
				
				trace("Getting Product List ....");
				List productlist = commondesc.getProductList(instid, jdbctemplate, dbtcustregbean.getBranchcode());
				trace("Got Product List"+productlist);
				   
				dbtcustregbean.setProductlist(productlist);  
				
				trace("Getting sub-Product List ....");
				List suproductlist = commondesc.getSubProductList(instid, dbtcustregbean.getProductcode(), jdbctemplate);
				trace("Got Sub - Product List"+suproductlist);
				
				dbtcustregbean.setSubproductlist(suproductlist);
				
				String limitname = commondesc.getLimitDesc(instid, dbtcustregbean.getLimitid(), jdbctemplate);
				dbtcustregbean.setLimitname(limitname);
				
				String feename = commondesc.getFeeDescription(instid, dbtcustregbean.getFeecode(), jdbctemplate);
				dbtcustregbean.setFeename(feename);
				
				
				trace("setting accttypelist");
				List accttypelist = dbtcustdao.getAcctTypeList(instid, jdbctemplate);
				dbtcustregbean.setAccttypelist(accttypelist);  
				
				trace("setting getAcctSubTypeList ");
				List  subaccounttypelist = dbtcustdao.getAcctSubTypeList(instid, dbtcustregbean.getAccounttypevalue(), jdbctemplate);
				dbtcustregbean.setAccuntsubtypelist(subaccounttypelist);
				
				trace("setting currency list");
				List currencylist = commondesc.getCurList(instid, jdbctemplate);
				dbtcustregbean.setCurrencylist(currencylist);
				
				List documentlist = commondesc.gettingDocumnettype(instid, jdbctemplate); 
				if( !documentlist.isEmpty() ){ 
					trace("Got list of master document...:" + documentlist.size());
					dbtcustregbean.setDocumentlist(documentlist);
				}
				dbtcustregbean.setTab4Status("true");   
				System.out.println("nranchcode:::"+dbtcustregbean.getBranchcode());
				
				
				
			}catch(Exception e ){
				e.printStackTrace();
				trace("Exception...."+e.getMessage());
				addActionError("Unable to continue the process");
				
			} 
			trace("EditcustomerSearch ended\n");enctrace("\n");
			return "EditcustomerSearch";
			
		}
		
		
		public String getcustomerData() throws Exception  {
			
			HttpSession session = getRequest().getSession();
			String instid = (String)session.getAttribute("Instname");
			String applicationid = getRequest().getParameter("applicationid");
			
			List productlist = dbtcustdao.getApplicationData( instid, applicationid, jdbctemplate );
			if( !productlist.isEmpty() ){
				Iterator itr = productlist.iterator();
				while( itr.hasNext() ){
					Map mp = (Map)itr.next();
					dbtcustregbean.setBranchcode( (String)mp.get("BRANCH_CODE")  );
					//System.out.println("branch code : " + dbtcustregbean.getBranchcode());
					dbtcustregbean.setProductcode( (String)mp.get("PRODUCT_CODE") );
					dbtcustregbean.setSubproduct( (String)mp.get("SUB_PROD_ID") );
					dbtcustregbean.setEmbname( (String)mp.get("EMBOSSING_NAME") );
					dbtcustregbean.setEncname( (String)mp.get("ENCODE_DATA") );
					dbtcustregbean.setLimitid( (String)mp.get("LIMIT_ID")  );
					dbtcustregbean.setFeecode( (String)mp.get("FEE_CODE")  );
					dbtcustregbean.setLimitbasedon( (String)mp.get("LIMIT_BASEDON")  );
					dbtcustregbean.setCustomeridno((String)mp.get("CIN") );
					System.out.println( (String)mp.get("CIN") + "Limit basedon: " + dbtcustregbean.getCustomeridno());
					
				}
			}
			return debitCustomerRegEntry();
			   
		}
		
		public String deleteApplication(){
			enctrace("Customer Register De Authorize Enter");
			IfpTransObj transact = commondesc.myTranObject("AUTHCORDER", txManager);
			try{
				String orderrefno = getRequest().getParameter("tab4_applicationid");
				String cin = getRequest().getParameter("customerid");
				String acctno = getRequest().getParameter("accountnovalue");
				enctrace("orderrefno---->"+orderrefno+"cin--"+cin+"acctno--"+acctno);
				/*
				String orderdeleteqry = "DELETE FROM PERS_CARD_ORDER WHERE ORDER_REF_NO='"+orderrefno+"' AND CIN='"+cin+"' AND ACCOUNT_NO='"+acctno+"'";
				trace("orderdeleteqry--->"+orderdeleteqry);
				int a = jdbctemplate.update(orderdeleteqry);
				trace("orderdeleteqry--->got"+a);
				
				String acctdeleteqry = "DELETE FROM ACCOUNTINFO WHERE ORDER_REF_NO='"+orderrefno+"' AND CIN='"+cin+"' AND ACCOUNTNO='"+acctno+"'";
				trace("acctdeleteqry--->"+acctdeleteqry);
				int b = jdbctemplate.update(acctdeleteqry);
				trace("orderdeleteqry--->got"+b);
				
				String custdeleteqry = "DELETE FROM CUSTOMERINFO WHERE ORDER_REF_NO='"+orderrefno+"' AND CIN='"+cin+"'";
				trace("custdeleteqry--->"+custdeleteqry);
				int c = jdbctemplate.update(custdeleteqry);
				trace("orderdeleteqry--->got"+c);*/
				
				
				
				//by gowtham220819
				String orderdeleteqry = "DELETE FROM PERS_CARD_ORDER WHERE ORDER_REF_NO=? AND CIN=? AND ACCOUNT_NO=?";
				trace("orderdeleteqry--->"+orderdeleteqry);
				int a = jdbctemplate.update(orderdeleteqry,new Object[]{orderrefno,cin,acctno});
				trace("orderdeleteqry--->got"+a);
				
				String acctdeleteqry = "DELETE FROM ACCOUNTINFO WHERE ORDER_REF_NO=? AND CIN=? AND ACCOUNTNO=?";
				trace("acctdeleteqry--->"+acctdeleteqry);
				int b = jdbctemplate.update(acctdeleteqry,new Object[]{orderrefno,cin,acctno});
				trace("orderdeleteqry--->got"+b);
				
				String custdeleteqry = "DELETE FROM CUSTOMERINFO WHERE ORDER_REF_NO=? AND CIN=?";
				trace("custdeleteqry--->"+custdeleteqry);
				int c = jdbctemplate.update(custdeleteqry,new Object[]{orderrefno,cin});
				trace("orderdeleteqry--->got"+c);
				
				if(a==1 && b==1 && c==1){
					enctrace("delete success");
					transact.txManager.commit(transact.status);	
				}else{
					enctrace("delete fail");
					transact.txManager.rollback(transact.status);
				}
			}
			catch (Exception e) {
				transact.txManager.rollback(transact.status);
				trace("Exception in delete the application -->registration de authorize");
				e.printStackTrace();
			}
			enctrace("End Customer Register De Authorize Enter");
			return "deletesuccess";
		}
		
		public String authorizeAllCardOrder() throws Exception
		{
			trace("authorizeAllcardOrder started ....");
			HttpSession session =getRequest().getSession();
			String instid = (String)session.getAttribute("Instname");
			String refno[] = getRequest().getParameterValues("instorderrefnum");
			String usercode = comUserCode(session);
			String username=comUsername();
			IfpTransObj transact = commondesc.myTranObject("AUTHCORDER", txManager);
			String mkrckr = "P";
			int authStatus = 0,count = 0;
			
			//added by gowtham_220719
			String  ip=(String) session.getAttribute("REMOTE_IP");
			
			trace("authorizeAllcardOrder Selected...."+refno.length);
			try{
					for(int i = 0;i<refno.length;i++){
						count = count + 1;
						authStatus = dbtcustdao.authorizeAllCardOrder(instid, refno[i], usercode, mkrckr, jdbctemplate);
							
						try{ 
							
							
							//added by gowtham_220719
							trace("ip address======>  "+ip);
							auditbean.setIpAdress(ip);
	
							auditbean.setActmsg("Register Details for Card Refference Number[ "+ refno[i]  +" ] is Authorized ");
							auditbean.setUsercode(username);
							auditbean.setAuditactcode("9009"); 
							commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
						 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
					}													
					if(authStatus > 0)
					{
						addActionMessage(count+" Cards Authorized Successfully  " );
						trace("authorizecardOrder Authorized successfullly");
						transact.txManager.commit(transact.status);
					}
					else
					{   addActionError("Card Not Authorized  " );
						trace("authorizecardOrder Rollbacked");
						transact.txManager.rollback(transact.status);
					}
				}
				catch(Exception e)
				{
					trace("authorizecardOrder Rollbacked");
					transact.txManager.rollback(transact.status);
				}
			trace("authorizecardOrder ended ....");
			return "required_home";  
		} 

	
	/*public String viewCustomerDataForView(){
		String instid = (String)session.getAttribute("Instname");
		String customerid = getRequest().getParameter("customerid");
		try{
			
		}catch(Exception e ){
			e.printStackTrace();
			trace("Exception...."+ e.getMessage() );
		}
	}*/ 
		  
	/*public String changeProduct(){
		HttpSession session =getRequest().getSession();
		String instid = (String)session.getAttribute("Instname");
		String orderrefno = getRequest().getParameter("tab4_applicationid");
		IfpTransObj transact = commondesc.myTranObject("AUTHCORDER", txManager);
		String usercode = comUserCode(session);
		try{
			addActionMessage("The Product has been changed Successfully  " );
				trace("changeProduct: method called");
				int updateProdDtls = dbtcustdao.updateProductDetails(instid,dbtcustregbean.getProductcode(), dbtcustregbean.getSubproduct(),dbtcustregbean.getLimitid(),dbtcustregbean.getFeecode(), orderrefno, usercode, jdbctemplate);
				dbtcustregbean.setTab2Status("true");
				if(updateProdDtls==1){
					addActionMessage("The Product has been changed Successfully  " );
					trace("The Product has been changed Successfully");
					transact.txManager.commit(transact.status);											
				}
				else
				{   addActionError("Unable to Change The Product  " );
					trace("Unable to Change The Product");
					transact.txManager.rollback(transact.status);
				}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return "customerSearch";
	}*/
	
		public List authorizeall;
		public List getAuthorizeall() {
			return authorizeall;
		}

		public void setAuthorizeall(List authorizeall) {
			this.authorizeall = authorizeall;
		}
		
		
		
public static String GenerateRandom4Digit() 
		{
		    Random rand = new Random();
		    int x = (int)(rand.nextDouble()*10000L);
		    String s = String.format("%04d", x);
		    return s;
		}
		
		public List GetOrderNumber( String instid, JdbcTemplate jdbctemplate  ) throws Exception {
			
			/*String qry = "SELECT CIN_NO, ORDER_REFNO FROM SEQUENCE_MASTER WHERE INST_ID='"+instid+"' ";
			List listoforder = jdbctemplate.queryForList(qry);*/
			
			//by gowtham-220819
			String qry = "SELECT CIN_NO, ORDER_REFNO FROM SEQUENCE_MASTER WHERE INST_ID=? ";
			List listoforder = jdbctemplate.queryForList(qry,new Object[]{instid});
			
			return listoforder;
		}
}




