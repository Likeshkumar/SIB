package com.ifp.personalize;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Session;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;

import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.DebugWriter;
import com.ifp.util.IfpTransObj;

import com.ifp.beans.AuditBeans;
import com.ifp.beans.PersonalCardOrderActionBeans;
import com.ifp.personalize.PersonalCardOrderBean;
public class PersonalCardOrderAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private String act; 
	public File uploadphoto; 
	private String uploadphotoContentType;
	public File uploadsignature; 
	private String uploadsignatureContentType;
	public File uploadidproof; 
	private String uploadidproofContentType;
	Boolean uploadreq = false;
	JdbcTemplate jdbctemplate = new JdbcTemplate();
	CommonDesc commondesc = new CommonDesc();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	AuditBeans auditbean = new AuditBeans();
	PersonalCardOrderActionBeans personalbean = new PersonalCardOrderActionBeans();
	
	List courierlist ;
	
	public List getCourierlist() {
		return courierlist;
	}

	public void setCourierlist(List courierlist) {
		this.courierlist = courierlist;
	}

	public PersonalCardOrderActionBeans getPersonalbean() {
		return personalbean;
	}

	public void setPersonalbean(PersonalCardOrderActionBeans personalbean) {
		this.personalbean = personalbean;
	}

	public AuditBeans getAuditbean() {
		return auditbean;
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
	}
	 
	public PlatformTransactionManager getTxManager() {
		return txManager;
	}


	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
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


	public Boolean getUploadreq() {
		return uploadreq;
	}


	public void setUploadreq(Boolean uploadreq) {
		this.uploadreq = uploadreq;
	}

 
	
	
	public File getUploadphoto() {
		return uploadphoto;
	}


	public void setUploadphoto(File uploadphoto) {
		this.uploadphoto = uploadphoto;
	}


	public String getUploadphotoContentType() {
		return uploadphotoContentType;
	}


	public void setUploadphotoContentType(String uploadphotoContentType) {
		this.uploadphotoContentType = uploadphotoContentType;
	}


	public File getUploadsignature() {
		return uploadsignature;
	}


	public void setUploadsignature(File uploadsignature) {
		this.uploadsignature = uploadsignature;
	}


	public String getUploadsignatureContentType() {
		return uploadsignatureContentType;
	}


	public void setUploadsignatureContentType(String uploadsignatureContentType) {
		this.uploadsignatureContentType = uploadsignatureContentType;
	}


	public File getUploadidproof() {
		return uploadidproof;
	}


	public void setUploadidproof(File uploadidproof) {
		this.uploadidproof = uploadidproof;
	}


	public String getUploadidproofContentType() {
		return uploadidproofContentType;
	}


	public void setUploadidproofContentType(String uploadidproofContentType) {
		this.uploadidproofContentType = uploadidproofContentType;
	}


	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	
	
	
	private List branch_list;
	private List personalproductlist;
	private List nationlist;
	private List questionlist;
	private List documenttypelist;

	public List getDocumenttypelist() {
		return documenttypelist;
	}


	public void setDocumenttypelist(List documenttypelist) {
		this.documenttypelist = documenttypelist;
	}


	public List getNationlist() {
		return nationlist;
	}

	public void setNationlist(List nationlist) {
		this.nationlist = nationlist;
	}

	public List getQuestionlist() {
		return questionlist;
	}

	public void setQuestionlist(List questionlist) {
		this.questionlist = questionlist;
	}

	public List getBranch_list() {
		return branch_list;
	}
	public void setBranch_list(List branch_list) {
		this.branch_list = branch_list;
	}

	public List getPersonalproductlist() {
		return personalproductlist;
	}

	public void setPersonalproductlist(List personalproductlist) {
		this.personalproductlist = personalproductlist;
	}
	
	
	public void clearNationlist()
	{
		this.nationlist.clear();
	}
	public void clearQuestionlist()
	{
		this.questionlist.clear();
	}
	
	private List personalorderlist;
	
	public List getPersonalorderlist() {
		return personalorderlist;
	}

	public void setPersonalorderlist(List personalorderlist) {
		this.personalorderlist = personalorderlist;
	}
	
	
//===================================================================================================
	public String comInstId(){
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	}
	public String orderref()
	{
	HttpSession session = getRequest().getSession();
	String orderref = (String) session.getAttribute(refname);
	return orderref;
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
	
	DebugWriter debugger = new DebugWriter();
	
	public void validate(){ 
		 int allowedsize_mb = 10;
		 long allowedsize_bits = allowedsize_mb * 1024 * 1024;
		 if(getUploadphoto() !=null){ 
			 trace( "uploaded size " + getUploadphoto().length() );
			
			 if(getUploadphoto().length()> allowedsize_bits ){    
				trace( "validated....Could not register. Uploade Photo size is too large ! Select less than 2 MB file" );
				addActionError("Could not register. Uploade Photo size is too large ! Select less than 2 MB file");
			} 
			 
			if(!getUploadphotoContentType().contains("image")) {
				addActionError("Could not register.Uploaded photo is not valid format..");
				trace("Could not register.Uploaded photo is not valid format ..");
			} 
		}
		 
		 if(getUploadsignature()  !=null){ 
			 trace( "uploaded signature image size : " + getUploadsignature().length() ); 
			if(getUploadsignature().length()> allowedsize_bits ){    
				trace( "validated....Could not register. Uploade Singnature image size is too large ! Select less than 2 MB file" );
				addActionError("Could not register. Uploade Photo size is too large ! Select less than 2 MB file");
			}
			 
			if(!getUploadsignatureContentType().contains("image")) {
				addActionError("Could not register.Uploaded photo is not valid format..");
				trace("Could not register.Uploaded photo is not valid format ..");
			} 
		}
		 
		 
		 if(getUploadidproof() !=null){ 
			 trace( "uploaded id proof size : " + getUploadidproof().length() ); 
			if(getUploadidproof().length()> allowedsize_bits ){    
				trace( "validated....Could not register. Uploade ID-Proof size is too large ! Select less than 2 MB file" );
				addActionError("Could not register. Uploade ID-Proof size is too large ! Select less than 2 MB file");
			}
			 
			if(!getUploadidproofContentType().contains("image")) {
				addActionError("Could not register.Uploaded id-proof is not valid format..");
				trace("Could not register.Uploaded photo is not valid format ..");
			} 
		}
	}
	
	
	public String cardOrder() {

		HttpSession session = getRequest().getSession();
		String inst_id = comInstId();
		String userid = comUserId(); 
		session.setAttribute("CARDOREDR_ACT", act);
		String session_act = (String) session.getAttribute("CARDOREDR_ACT");
		System.out.println("session_act " + session_act); 
		return "cardorder_home";
	}
	
	public String personalProductdetails() {
		trace("**************Personal product details***********");
		
		List pers_prodlist,br_list=null;
		String inst_id =comInstId();
		String usertype = comuserType();
		
		HttpSession session = getRequest().getSession();
		
		PersonlRequiredCheck checkobject = new PersonlRequiredCheck();
		
		int reqcheck = checkobject.cardorderRequiredcheck(inst_id,session,jdbctemplate);
		if(reqcheck != 0)
		{
			System.out.println("Tis is rerurn pages ---> "+reqcheck);
			trace( "Configuration Failure");
			return "cardorder_home";
		}
		try {
			if (usertype.equals("INSTADMIN")) {
						
				trace("Getting branch list...");
				br_list = commondesc.generateBranchList(inst_id, jdbctemplate); 
				if(!(br_list.isEmpty())){
					setBranch_list(br_list);
					
				}
				else{
					//setBranch_list(br_list);
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg"," No Branch Details Found ");
					trace("Branch List is empty ");				
					return "personalproducts";
				}
				pers_prodlist=commondesc.getProductList(inst_id, jdbctemplate, session);	
			}else{			
				pers_prodlist=commondesc.getProductList(inst_id, (String)session.getAttribute("BRANCHCODE"), jdbctemplate);
			}
			
			trace("Getting product list..."+pers_prodlist);
					
			if (!(pers_prodlist.isEmpty())){
				setPersonalproductlist(pers_prodlist);				
			} else{
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg"," No Product Details Found ");
				trace("No product details found..."); 
			}

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg","Exception : Could not continue the process" );
			trace("Exception: While Fetching The Product Details  "+ e.getMessage() );
		} 
		trace("\n\n");enctrace("\n\n");
		return "personalproducts";
	}
	private List branchlist;
	
	
	public List getBranchlist() {
		return branchlist;
	}

	public void setBranchlist(List branchlist) {
		this.branchlist = branchlist;
	}
	
	


	public void getPersonalSubProductetails() {
		trace("************The Function Getting the getPersonalSubProductetails ***************");
		enctrace("************The Function Getting the getPersonalSubProductetails ***************");
		

		String subproduct, productname;
		List result;
		String instid =comInstId();
		String prodid=getRequest().getParameter("prodid");
		trace("Got the product id : " + prodid );	
		
 		try {			
 			
 			trace("Getting Sub-Product list ");
 			
 			/*String subproduct_qury = "select SUB_PRODUCT_NAME, SUB_PROD_ID from IFP_INSTPROD_DETAILS"
 			+ " where inst_id='"+instid+"' and PRODUCT_CODE='"+prodid+"'  and AUTH_STATUS='1'";
 			//and  PERSONALIZED in ('1','2')
 			enctrace("subproduct_qury : " + subproduct_qury ); 
			result = jdbctemplate.queryForList(subproduct_qury); */
 			
 			///by  gowtham
 			String subproduct_qury = "select SUB_PRODUCT_NAME, SUB_PROD_ID from IFP_INSTPROD_DETAILS"
 		 			+ " where inst_id=? and PRODUCT_CODE=?  and AUTH_STATUS=? ";
 		 			//and  PERSONALIZED in ('1','2')
 		 			enctrace("subproduct_qury : " + subproduct_qury ); 
 					result = jdbctemplate.queryForList(subproduct_qury,new Object[]{instid,prodid,"1"}); 
			
			if (!(result.isEmpty())) {
				String sel = null; 
				sel = "<select name='subproductlist' id='subproductlist'>";
				sel = sel+ "<option value=\"-1\">--Select SubProduct--</option>";
				Iterator itr = result.iterator();
				while (itr.hasNext()) {
					Map map = (Map) itr.next();
					subproduct = ((String) map.get("SUB_PROD_ID"));
					productname = ((String) map.get("SUB_PRODUCT_NAME"));
					sel = sel + "<option value=\" " + subproduct.trim() + "\">"+ productname + "</option>";
				}
				sel = sel + "</select>";

				try {
					System.out.println("response  +++++++++++++++++++++++ "+sel);
					debugger.debugWriter("PersonalCardOrderAction", instid,"Sending the result");
					getResponse().getWriter().write(sel);
				} catch (IOException ioe) {
					debugger.debugWriter("PersonalCardOrderAction", instid,"ERROR : "+ioe.getMessage());
				}
			} else {
				try {
					trace("No Sub-Product Found");
					String noproduct = null;				
					noproduct = "<select name='subproductlist' id='subproductlist' >";
					noproduct = noproduct+ "<option value=\"-1\">--No Product--</option></select>";
					debugger.debugWriter("PersonalCardOrderAction", instid,"Sending the result");
					getResponse().getWriter().write(noproduct);
				} catch (IOException ioe) {
					trace("IO Exception : " + ioe.getMessage() );
				}
			}
		} catch (Exception e) 
		{
			trace("Exception : " + e.getMessage() );			
			try {
				String noproduct = null;			
				noproduct = "<select name='subproductlist' id='subproductlist'>";			
				noproduct = noproduct+ "<option value=\"-1\">--ERROR--</option></select>";
				getResponse().getWriter().write(noproduct);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	public void getPersonalSubProductcount()
	{
		trace("*******The Function Getting the getPersonalSubProductcount **********");
		enctrace("*******The Function Getting the getPersonalSubProductcount **********");
		
		HttpSession session = getRequest().getSession();
		
		String instid = comInstId();
		String productid = getRequest().getParameter("productid");
		
		String subprodid=getRequest().getParameter("subprodid");
		
		trace("Request  Got the productid :" + productid + ", Subprodutid : " + subprodid );
		try{
			trace("Checking customer registeration required...");
			String custreg_requ = commondesc.personalOrdercustomerregistrationcheck(instid,productid,subprodid.trim(),jdbctemplate);
			trace("Got : "+custreg_requ);
			if(custreg_requ.equals("N"))
			{
				System.out.println("divis");				
				String divis="N"; 
				getResponse().getWriter().write(divis);				
			}
		} catch (Exception ioe) {
			ioe.printStackTrace();
			trace("Exception: " + ioe.getMessage() );
		}
		trace("\n\n");enctrace("\n\n");
	}
	
	
	public String saveintosession()	{
		trace("**** Save into session *********** ");
		enctrace("**** Save into session *********** ");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("SAVEORDER", txManager);
		String instid = comInstId();
		String userid = comUserId();
		Date date=new Date();
		String custreg_requ = getRequest().getParameter("cust_reg_req");
		String card_branchcode = getRequest().getParameter("branchcode").trim();
		String cardtype=(getRequest().getParameter("cardtype")).trim();
		String subproduct = (getRequest().getParameter("subproductlist")).trim();
		trace("subproduct:::"+subproduct);
		String product_count = (getRequest().getParameter("Count")).trim();
		String embossName = (getRequest().getParameter("emposs")).trim();
		String encoding = (getRequest().getParameter("encode")).trim();
		trace("Request Got custreg_requ ["+custreg_requ+"] card_branchcode["+card_branchcode+"] cardtype["+cardtype+"]  subproduct["+subproduct+"]   product_count["+product_count+"]   embossName["+embossName+"]  encoding["+encoding+"]"    );
		try{  
			
			trace("Checking uploading files mandotory....");
			String uploadreq = "N";
			try {
				
				/*String uploadmand = "SELECT PHOTOUPLOAD_REQ FROM INSTITUTION WHERE INST_ID='"+instid+"'";
				trace("uploadmand query:"+uploadmand);
				 uploadreq = (String) jdbctemplate.queryForObject(uploadmand, String.class);*/
				
				//byg owtham
				String uploadmand = "SELECT PHOTOUPLOAD_REQ FROM INSTITUTION WHERE INST_ID=? ";
				trace("uploadmand query:"+uploadmand);
				 uploadreq = (String) jdbctemplate.queryForObject(uploadmand,new Object[]{instid}, String.class);
				 
			} catch (EmptyResultDataAccessException e) {}
			 
			 
			if( uploadreq.equals("Y")){
				setUploadreq(true);
			}
			
			String subproductstatus = commondesc.checkValidSubProduct(instid,cardtype,subproduct,jdbctemplate );
			trace("Checking valid sub-product...got : " + subproductstatus);
			if( subproductstatus==null || !subproductstatus.equals("1")){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg",  "Could not continue the process...Selected product not authorized" );
				return this.personalProductdetails();
			}
			
			
			if(custreg_requ.equals("N"))
			{
				
				String CIN = null,order_refno=null;
				System.out.println("Customer Registration Not required Insering the Order here itself");
				int refnum_update = 1,cinnum_update = -1;
				String	CIN_NO ="";
				synchronized (this) 
				{
					/*trace("Generating customer id ....");
					CIN =  commondesc.cinnumberGeneratoer(instid,jdbctemplate);
					trace("Got : " + CIN ); 
					if(CIN.equals("E") || CIN.equals("N") || CIN == null)
					{
						session.setAttribute("preverr","E");
						session.setAttribute("prevmsg","Error While Getting The CIN ");
						trace( "Error While Getting The CIN ");
						return personalProductdetails();
					}
					if(CIN.equals("M")){
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg","CIN Length Reached Maximum Level");
						trace("CIN Length Reached Maximum Level");
						return personalProductdetails();
					}*/
					trace("Generating order ref no ...");
					order_refno=commondesc.generateorderRefno(instid,jdbctemplate);
					trace("Generating order ref no ...got : " + order_refno);
					if(order_refno.equals("N") || order_refno.equals("E") || order_refno == null)
					{
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg","Error While Getting the Order Reference Number");
						trace("Error While Getting the Order Reference Number");
						return personalProductdetails();
					}
					if(order_refno.equals("M"))
					{
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg","Order Reference Number Length Reached Maximum Level");
						trace("Order Reference Number Length Reached Maximum Level");
						return personalProductdetails();
					}
					
					List SEQ = GetOrderNumber( instid,jdbctemplate );
					String order="",cin="";
					Iterator itr = SEQ.iterator();
					while (itr.hasNext()) {
						Map map = (Map) itr.next();
						order = ((String) map.get("ORDER_REFNO"));
						cin = ((String) map.get("CIN_NO"));
						
				
					}
					  Integer Counter = new Integer( 1 );
					 Counter = Integer.parseInt(cin)+1;
					CIN_NO = instid+GenerateRandom4Digit()+String.format("%09d", Counter);



					
					
					trace("Updating sequance master....");
					
					/*String cinoder_sequpdate = "UPDATE SEQUENCE_MASTER SET CIN_NO=CIN_NO+1,ORDER_REFNO=ORDER_REFNO+1 WHERE INST_ID='"+instid+"'";
					enctrace("cinoder_sequpdate : "+cinoder_sequpdate);
					cinnum_update = jdbctemplate.update(cinoder_sequpdate);*/
					
					///by gowtham
					String cinoder_sequpdate = "UPDATE SEQUENCE_MASTER SET CIN_NO=CIN_NO+1,ORDER_REFNO=ORDER_REFNO+1 WHERE INST_ID=? ";
					enctrace("cinoder_sequpdate : "+cinoder_sequpdate);
					cinnum_update = jdbctemplate.update(cinoder_sequpdate,new Object[]{instid});
					
					trace("Got : " + cinnum_update  );
					 	
				}
				trace("Getting bin for the card type [ "+cardtype+" ]....");
				String binno = commondesc.getBin(instid, cardtype, jdbctemplate);
				trace("Got binno : " + binno );
				trace("Getting card type for the product code :  " + cardtype );
				String cardtypeid = commondesc.getCardType(instid, cardtype, jdbctemplate);
				trace("cardtypeid : " + cardtypeid  );
				String product_code = cardtype,order_type="P";
				String Maker_id = userid;
				String Checker_id = "";
				String Remark = "";
				String appdate = CommonDesc.default_date_query;
				String appno =  "000000000000";
				String checker_date = CommonDesc.default_date_query; //this is default date 
				String order_time = "(select trim(substr(to_char(sysdate,'dd-MON-yy hh24:mi:ss'),10,10)) from dual)";
				String maker_time = "(select trim(substr(to_char(sysdate,'dd-MON-yy hh24:mi:ss'),10,10)) from dual)";
				String checker_time ="'00:00:00'";
				String card_order_act=(String)session.getAttribute("CARDOREDR_ACT");
				System.out.print("card_order_act "+card_order_act);
				String order_status="X",mkck_status="X";
				if(card_order_act.equals("M"))
				{
					order_status="01";
					mkck_status="M";
				}
				else if( card_order_act.equals("D") ) 	{
						mkck_status="P";
						order_status="01";
						Checker_id=Maker_id;
						checker_date = "(sysdate)";
						checker_time = "(select trim(substr(to_char(sysdate,'dd-MON-yy hh24:mi:ss'),10,10)) from dual)";
					}else{
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg","Invalid Parameters,Please Contact Admin");
						trace("Invalid Parameters,Please Contact Admin");
						return personalProductdetails();
					}
				trace("Inserting PERS_CARD_ORDER ... ");
				
				String order_qury = "INSERT INTO PERS_CARD_ORDER (INST_ID, ORDER_REF_NO, CARD_TYPE_ID, SUB_PROD_ID, " +
				"PRODUCT_CODE, CARD_QUANTITY, ORDER_STATUS, ORDER_TYPE, ORDERED_DATE, ORDERED_TIME, EMBOSSING_NAME, " +
				"MKCK_STATUS, REMARKS,ENCODE_DATA, MAKER_DATE, MAKER_TIME, MAKER_ID, CHECKER_DATE, CHECKER_TIME, CHECKER_ID, BRANCH_CODE, " +
				"BIN, APP_DATE, CIN, APP_NO,KYC_FLAG) VALUES"
			
				/*+ " ('"+instid+"', '"+order_refno+"', '"+cardtypeid+"', '"+subproduct+"', '"+product_code+"',"
				+ " '"+product_count+"', '"+order_status+"', '"+order_type+"',(sysdate), " +order_time+", '"+embossName+"',"
				+ "'"+mkck_status+"','"+Remark+"','"+encoding+"',(sysdate), "+maker_time+", '"+Maker_id+"', "+checker_date+", "
				+ ""+checker_time+", '"+Checker_id+"','"+card_branchcode+"'," +"'"+binno+"',to_date('"+appdate+"','dd-mm-yyyy'),"
				+ "'"+CIN_NO+"','"+appno+"','0')";
				enctrace("Order Query : "+order_qury);
				int order_insert = jdbctemplate.update(order_qury);*/
				
			+ " (?,?,?,?,?,?,?,?,(sysdate), " +order_time+", ?,?,?,?,(sysdate), "+maker_time+", ?, "+checker_date+", "
			+ ""+checker_time+",?,?,?,to_date(?,'dd-mm-yyyy'),?,?,?)";
				enctrace("Order Query : "+order_qury);
				int order_insert = jdbctemplate.update(order_qury,new Object[]{instid,order_refno,cardtypeid,subproduct,product_code,
						product_count,order_status,order_type,embossName,mkck_status,Remark,encoding,Maker_id,Checker_id,
						card_branchcode,binno,appdate,CIN_NO,appno,"0"});
				
				trace("Got order_insert : " + order_insert );
				
				String updateorder_qury = commondesc.updateOrderrefnumcount(instid);
				
				String updatecin_qury = commondesc.updateCINcount(instid);	
				
				 
				
				if(order_insert == 1 && refnum_update == 1 && cinnum_update == 1)
				{
					//commondesc.commitTxn(jdbctemplate);
					txManager.commit(transact.status);
					
					session.setAttribute("preverr","S");
					session.setAttribute("prevmsg","Card Ordered Sucessfully , The Reference Number is : "+order_refno+" Customer Id is : "+CIN);
					trace("Card Ordered Sucessfully , The Reference Number is : "+order_refno+" Customer Id is : "+CIN + "...got committed");
				}
				else
				{
					//commondesc.rollbackTxn(jdbctemplate);
					txManager.rollback(transact.status);
					
					session.setAttribute("preverr","E");
					session.setAttribute("prevmsg","Card Order Falied");	
					trace("Card Order Falied...got rolled back");
				}
				return personalProductdetails();
				
			}else if(custreg_requ.equals("Y"))
			{ 
				session.setAttribute("CARD_BRANCH",card_branchcode);
				session.setAttribute("Ordered_Card",cardtype);
				session.setAttribute("Ordered_SubProduct",subproduct);
				session.setAttribute("Ordered__Count",product_count);
				session.setAttribute("Embossname",embossName);
				session.setAttribute("Encodename",encoding); 
				setNationlist(commondesc.gettingNations(jdbctemplate)); 
				setDocumenttypelist(commondesc.gettingDocumnettype(instid,jdbctemplate));
				session.setAttribute("getcustomedetailsErrorStatus", "S");
				
				SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
				String todaydate = sdf.format(date);
				session.setAttribute("todaydate",todaydate);
			}
		}
		catch (Exception e){
			session.setAttribute("getcustomedetailsErrorStatus", "E");
			session.setAttribute("getcustomedetailsStatusMsg", "Exception : Could not continue the process...");			
			trace("Exception : " + e.getMessage() );
		}
		trace("\n\n");enctrace("\n\n");
		return "customerdetails";
	}
	public String savepersonalorder(){
		trace("Save personal card order....");
		enctrace("Save personal card order....");
		HttpSession session = getRequest().getSession();
		String in_name = comInstId();
		String userid = comUserId();
		String brcode = comBranchId();		
		String order_refno=null,CIN=null;
		IfpTransObj transact = commondesc.myTranObject("SAVEORDER", txManager);
		
		//added by gowtham_240719
		String  ip=(String) session.getAttribute("REMOTE_IP");
		
		try{
			
			
			int cinupdate = 1,orderupdate = 1;
		synchronized (this) 
		{
			
			trace("Generating customer id....");
			CIN =  commondesc.cinnumberGeneratoer(in_name,jdbctemplate);
			trace("The CIN Generated is : "+CIN);
			if(CIN.equals("E") || CIN.equals("N") || CIN == null)
			{
				session.setAttribute("preverr","E");
				session.setAttribute("prevmsg","Error While Getting The CIN ");
				trace("Error While Getting The CIN ");
				return personalProductdetails();
			}
			if(CIN.equals("M")){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg","CIN Length Reached Maximum Level");
				trace("CIN Length Reached Maximum Level");
				return personalProductdetails();
			}
			trace("Generating order ref-no...");
			order_refno=commondesc.generateorderRefno(in_name,jdbctemplate);
			 trace("order_refno : " + order_refno );
			if(order_refno.equals("N") || order_refno.equals("E") || order_refno == null)
			{
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg","Error While Getting the Order Reference Number");
				trace("Error While Getting the Order Reference Number");
				return personalProductdetails();
			}
			if(order_refno.equals("M"))
			{
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg","Order Reference Number Length Reached Maximum Level");
				trace("Order Reference Number Length Reached Maximum Level");
				return personalProductdetails();
			}
			 
			trace("Updating sequance master....");
			
			/*String cinorder_sequpdate = "UPDATE SEQUENCE_MASTER SET CIN_NO=CIN_NO+1,ORDER_REFNO=ORDER_REFNO+1 "
			+ "WHERE INST_ID='"+in_name+"'";
			enctrace("cinorder_sequpdate :  "+cinorder_sequpdate);
			orderupdate = jdbctemplate.update(cinorder_sequpdate);*/
			
			//by gowtham
			String cinorder_sequpdate = "UPDATE SEQUENCE_MASTER SET CIN_NO=CIN_NO+1,ORDER_REFNO=ORDER_REFNO+1 "
					+ "WHERE INST_ID=? ";
					enctrace("cinorder_sequpdate :  "+cinorder_sequpdate);
					orderupdate = jdbctemplate.update(cinorder_sequpdate,new Object[]{in_name});
			
			trace("Got : " + orderupdate );
			 
			String Maker_id = userid;
			String Checker_id = "";
			String Remark = "";
			String checker_date = CommonDesc.default_date_query; //this is default date 
			String order_time = "(select trim(substr(to_char(sysdate,'dd-MON-yy hh24:mi:ss'),10,10)) from dual)";
			String maker_time = "(select trim(substr(to_char(sysdate,'dd-MON-yy hh24:mi:ss'),10,10)) from dual)";
			String checker_time ="'00:00:00'";
			String card_order_act=(String)session.getAttribute("CARDOREDR_ACT");
			System.out.print("card_order_act "+card_order_act);
			String order_status="X",Customer_status="X",mkck_status="X";
			if(card_order_act.equals("M"))
			{
				order_status="01";
				Customer_status="U";
				mkck_status="M";
			}
			else if(card_order_act.equals("D"))
				{
					mkck_status="P";
					order_status="01";
					Customer_status="A";
					Checker_id=Maker_id;
					checker_date = "(sysdate)";
					checker_time = "(select trim(substr(to_char(sysdate,'dd-MON-yy hh24:mi:ss'),10,10)) from dual)";
				}

	
		}
		System.out.println(" $$$$$$$$$$$$$$ The Order Refenrence Number Cretaed is $$$$$$$$$$$$$$$"+order_refno);
		String First_name= commondesc.escSql( getRequest().getParameter("fname") );  
		String Middle_name= commondesc.escSql( getRequest().getParameter("mname") );   
		String Last_name= commondesc.escSql( getRequest().getParameter("lname") ); 
		String Father_name= commondesc.escSql( getRequest().getParameter("f_name") ); 
		String Mother_name= commondesc.escSql( getRequest().getParameter("m_name") ); 
		String Married=(getRequest().getParameter("mstatus"));
		String Spouse_name= commondesc.escSql( getRequest().getParameter("spname") );   
		String Sex=(getRequest().getParameter("gender"));
		String BDay=(getRequest().getParameter("dob"));
		String Nationality= getRequest().getParameter("nations");
		String E_mail=  getRequest().getParameter("email"); 
		String Mobile_No= getRequest().getParameter("mobile");  
		String Phone_No= getRequest().getParameter("phone");  
		String Occupation = getRequest().getParameter("work");  
		String Identity_No = getRequest().getParameter("idno");  
		String Identity_document= (getRequest().getParameter("iddoc"));
		String app_date = (getRequest().getParameter("appdate"));
		String app_no=(getRequest().getParameter("appno"));
		String postal_address1 = commondesc.escSql( getRequest().getParameter("paddress1") );
		String postal_address2 =  commondesc.escSql( getRequest().getParameter("paddress2") );
		String postal_address3 =  commondesc.escSql( getRequest().getParameter("paddress3") );
		String postal_address4 =  commondesc.escSql( getRequest().getParameter("paddress4") );
		String Samepostal_address = (getRequest().getParameter("same"));
		String resident_address1 =  commondesc.escSql( getRequest().getParameter("raddress1") ); 
		String resident_address2 =  commondesc.escSql( getRequest().getParameter("raddress2") ); 
		String resident_address3 =  commondesc.escSql( getRequest().getParameter("raddress3") ); 
		String resident_address4 = commondesc.escSql( getRequest().getParameter("raddress4") ); 
		String Security = "";
		String Aswer = "";
		System.out.println("postal_address1 ==> "+postal_address1+"\npostal_address2 ==> "+postal_address2+"\npostal_address3 ==> "+postal_address3+"\npostal_address4 ==> "+postal_address4);
		System.out.println("resident_address1 ==> "+resident_address1+"\nresident_address2 ==> "+resident_address2+"\nresident_address3 ==> "+resident_address3+"\nresident_address4 ==> "+resident_address4);
		String cardtype_id  = (String)session.getAttribute("Ordered_Card");
		String subproduct = (String)session.getAttribute("Ordered_SubProduct");
		String ordercount =  (String)session.getAttribute("Ordered__Count");
		String Embossingname = (String)session.getAttribute("Embossname");
		String Encodingname = (String)session.getAttribute("Encodename");
		String branch_code = (String)session.getAttribute("CARD_BRANCH");
		//String app_date = (String)session.getAttribute("APP_DATE");
		//String app_no = (String)session.getAttribute("APP_NUM");
		String cardissuetype = getRequest().getParameter("cardissuetype");
		String parentcardno = getRequest().getParameter("parentcard");
		String Order_Type = "P"; 
		trace("Getting bin....for the product : " + cardtype_id );
		String binno = commondesc.getBin(in_name, cardtype_id, jdbctemplate);
		trace("Got binno : " + binno);
		
		trace("Getting card type id for the produt : " + cardtype_id );
		String cardtype = commondesc.getCardType(in_name, cardtype_id, jdbctemplate);
		trace("Got cardtype : " + cardtype );
		String product_code = cardtype_id;
		if( postal_address1 == "" || postal_address1 ==null )
		{
			postal_address1 = "";
		}
		if(postal_address2 == "" || postal_address2 ==null )
		{
			postal_address2 = "";
		}
		if(postal_address3 == "" || postal_address3 ==null )
		{
			postal_address3 = "";
		}
		if(postal_address4 == "" || postal_address4 ==null )
		{
			postal_address4 = "";
		}
		if( resident_address1 == "" || resident_address1 == null )
		{
			resident_address1 = "";
		}
		if( resident_address2 == "" || resident_address2 == null )
		{
			resident_address2 = "";
		}
		if( resident_address3 == "" || resident_address3 == null )
		{
			resident_address3 = "";
		}		
		if( resident_address4 == "" || resident_address4 == null )
		{
			resident_address4 = "";
		}


		if(Samepostal_address != null)
		{
			resident_address1 = postal_address1;
			resident_address2 = postal_address2;
			resident_address3 = postal_address3;
			resident_address4 = postal_address4;
		}

		
		String postal_address = postal_address1+"|"+postal_address2+"|"+postal_address3+"|"+postal_address4+"|";
		String resident_address = resident_address1+"|"+resident_address2+"|"+resident_address3+"|"+resident_address4+"|";
		trace("Postal Address compiednd is : "+postal_address);
		trace("Residentr Address Compined is : "+resident_address);
		
		
		
		if(Mother_name == "")
		{
			Mother_name = "";
		}
		if(Middle_name =="")
		{
			Middle_name = "";
		}
		if(Spouse_name == null)
		{
			Spouse_name ="";
		}
		String Maker_id = userid;
		String Checker_id = "";
		String Remark = "";
		String checker_date = CommonDesc.default_date_query; //this is default date 
		String order_time = "(select trim(substr(to_char(sysdate,'dd-MON-yy hh24:mi:ss'),10,10)) from dual)";
		String maker_time = "(select trim(substr(to_char(sysdate,'dd-MON-yy hh24:mi:ss'),10,10)) from dual)";
		String checker_time ="'00:00:00'";
		String card_order_act=(String)session.getAttribute("CARDOREDR_ACT");
		System.out.print("card_order_act "+card_order_act);
		String order_status="X",Customer_status="X",mkck_status="X";
		if(card_order_act.equals("M"))
		{
			order_status="01";
			Customer_status="U";
			mkck_status="M";
		}
		else if(card_order_act.equals("D"))
			{
				mkck_status="P";
				order_status="01";
				Customer_status="A";
				Checker_id=Maker_id;
				checker_date = "(sysdate)";
				checker_time = "(select trim(substr(to_char(sysdate,'dd-MON-yy hh24:mi:ss'),10,10)) from dual)";
			}
		
		Properties prop = commondesc.getCommonDescProperty();
		
		String photourl = "", signatureurl="", idproofurl="";
		String newphotoname = CIN+"_"+commondesc.getDateTimeStamp()+".jpg";
		String newsignaturename = CIN+"_"+commondesc.getDateTimeStamp()+".jpg";
		String newidproofname = CIN+"_"+commondesc.getDateTimeStamp()+".jpg"; 
		
		if( uploadphoto != null ){
			trace( "uploaded photo size " + uploadphoto.length() ); 
			if( uploadphoto != null  ){ 
				photourl = newphotoname; 
			} 
		}
		if( uploadsignature != null ){
			trace( "uploaded signature image size : " + uploadsignature.length() );
			if( uploadsignature != null  ){ 
				signatureurl = newsignaturename; 
			} 
			
		}
		if( uploadidproof != null ){
			trace( "uploaded id proof size : " + uploadidproof.length() );   
			if( uploadidproof != null  ){ 
				idproofurl = newidproofname; 
			} 
		}
		
		trace("Inserting ifp_custinfo process....");
		
		String custinfo_qury="INSERT INTO IFP_CUSTINFO_PROCESS(INST_ID,CIN,FNAME,MNAME,LNAME,FATHER_NAME,MOTHER_NAME,MARITAL_STATUS," +
		"SPOUSE_NAME,GENDER,DOB,NATIONALITY,EMAIL_ADDRESS,MOBILE_NO,PHONE_NO,OCCUPATION,ID_NUMBER, " +
		"ID_DOCUMENT, SECUTIRY_QUESTION, SECURITY_ANSWER,POST_ADDR1,POST_ADDR2,POST_ADDR3,POST_ADDR4,RES_ADDR1,RES_ADDR2,RES_ADDR3,RES_ADDR4,MAKER_DATE," +
		"MAKER_ID,CHECKER_DATE,CHECKER_ID,MKCK_STATUS,CUSTOMER_STATUS,KYC_FLAG,PHOTO_URL,SIGNATURE_URL,IDPROOF_URL) VALUES ('"+in_name+"','"+CIN+"','"+First_name+"'," +
		"'"+Middle_name+"','"+Last_name+"','"+Father_name+"','"+Mother_name+"','"+Married+"','"+Spouse_name+"','"+Sex+"',to_date('"+BDay+"','DD-MM-YYYY'),'"+Nationality+"','"+E_mail+"','"+Mobile_No+"'," +
		"'"+Phone_No+"','"+Occupation+"','"+Identity_No+"','"+Identity_document+"', '"+Security+"','"+Aswer+"','"+postal_address1+"','"+postal_address2+"','"+postal_address3+"','"+postal_address4+"','"+resident_address1+"','"+resident_address2+"','"+resident_address3+"','"+resident_address4+"',(sysdate), " +
		"'"+Maker_id+"',(sysdate), '"+Checker_id+"', 'M', '"+Customer_status+"','0','"+photourl+"', '"+signatureurl+"', '"+idproofurl+"')";
		enctrace("custinfo_query: " + custinfo_qury );
		int custinfo = commondesc.executeTransaction(custinfo_qury, jdbctemplate);
		trace("Got : " + custinfo );
		
		trace("Inserting PERS_CARD_ORDER.....");
		String order_qury = "INSERT INTO PERS_CARD_ORDER (INST_ID, ORDER_REF_NO, CARD_TYPE_ID, SUB_PROD_ID, " +
				"PRODUCT_CODE, CARD_QUANTITY, ORDER_STATUS, ORDER_TYPE, ORDERED_DATE, ORDERED_TIME, EMBOSSING_NAME, " +
				"MKCK_STATUS, REMARKS,ENCODE_DATA, MAKER_DATE, MAKER_TIME, MAKER_ID, CHECKER_DATE, CHECKER_TIME, CHECKER_ID, BRANCH_CODE, " +
				"BIN, APP_DATE, CIN, APP_NO,KYC_FLAG, CARDISSUETYPE,PARENTCARD) VALUES ('"+in_name+"', '"+order_refno+"', '"+cardtype+"', '"+subproduct+"', '"+product_code+"', '"+ordercount+"', '"+order_status+"', '"+Order_Type+"',(sysdate), " +
				order_time+", '"+Embossingname+"','"+mkck_status+"','"+Remark+"','"+Encodingname+"',(sysdate), "+maker_time+", '"+Maker_id+"', "+checker_date+", "+checker_time+", '"+Checker_id+"','"+branch_code+"'," +
				"'"+binno+"',to_date('"+app_date+"','dd-mm-yyyy'),'"+CIN+"','"+app_no+"','0', '"+cardissuetype+"','"+parentcardno+"')";
		enctrace("order_qury : " + order_qury );
		int order = commondesc.executeTransaction(order_qury, jdbctemplate);
		trace("Got : " + order  );
		 if(custinfo == 1 && order == 1 && orderupdate == 1 && cinupdate == 1){
			//commondesc.commitTxn(jdbctemplate);
			 txManager.commit(transact.status);
			session.setAttribute("preverr","S");
			session.setAttribute("prevmsg","Card Ordered Sucessfully , The Reference Number is : "+order_refno+" and Customer Id is : "+CIN);
			trace("Card Ordered Sucessfully , The Reference Number is : "+order_refno+" and Customer Id is : "+CIN);
			
			
			/*************AUDIT BLOCK**************/ 
			try{ 
				
			
				//added by gowtham_240719
				trace("ip address======>  "+ip);
				auditbean.setIpAdress(ip);
				
				auditbean.setActmsg("Order [ "+order_refno+" ] customerid [ "+CIN+" ] Generated");
				auditbean.setUsercode(Maker_id);
				auditbean.setAuditactcode("0101"); 
				
				//added by gowtham_010819
				String pcode=null;
				auditbean.setProduct(product_code);
				
				auditbean.setSubproduct(subproduct);
				auditbean.setApplicationid(order_refno);
				commondesc.insertAuditTrail(in_name, Maker_id, auditbean, jdbctemplate, txManager);
			 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
			 /*************AUDIT BLOCK**************/ 
			
			
		}else{
			//commondesc.rollbackTxn(jdbctemplate);
			txManager.rollback(transact.status);
			trace("Could not process...got rolled back...");
		}
		
		String photodir = prop.getProperty("PHOTOFILELOCATION");
		File photodirfile = new File(photodir);
		if( !photodirfile.exists() ){
			photodirfile.mkdir();
		}
		
		String signaturedir = prop.getProperty("SIGNATUREFILELOCATION");
		File signaturedirfile = new File( signaturedir );
		if( !signaturedirfile.exists() ){
			signaturedirfile.mkdir();
		}
		
		String idproofdir = prop.getProperty("IDPROOFLOCATION");
		File idproofdirfile = new File(photodir);
		if( !idproofdirfile.exists() ){
			idproofdirfile.mkdir();
		}
		
		if( uploadphoto != null  ){
			File fileToCreate = new File(photodirfile, newphotoname); 
			FileUtils.copyFile(uploadphoto, fileToCreate); 
			photourl = newphotoname; 
		} 
		
		if( uploadsignature != null  ){
			File fileToCreate = new File(signaturedirfile, newsignaturename); 
			FileUtils.copyFile(uploadsignature, fileToCreate); 
			signatureurl = newsignaturename; 
		} 
		
		if( uploadidproof != null  ){
			File fileToCreate = new File(idproofdir, newidproofname); 
			FileUtils.copyFile(uploadidproof, fileToCreate); 
			idproofurl = newidproofname; 
		} 
				
			
	}
	catch (Exception ecr) 
	{
		System.out.println("Error while executinh the insert quries "+ ecr);
		//commondesc.rollbackTxn(jdbctemplate);
		txManager.rollback(transact.status);
		session.setAttribute("preverr","E");
		session.setAttribute("prevmsg","Exception : Could not continue the process");
		ecr.printStackTrace();
		trace("Exception :  " + ecr.getMessage() );
	}
		 
		session.removeAttribute("Ordered_Card");
		session.removeAttribute("Ordered_SubProduct");
		session.removeAttribute("Ordered__Count");
		session.removeAttribute("Embossname");
		session.removeAttribute("Encodename");
		session.removeAttribute("CARD_BRANCH");
		session.removeAttribute("APP_DATE");
		session.removeAttribute("APP_NUM");
		trace("\n\n");enctrace("\n\n");
		return personalProductdetails();
	}
	
	// VIEW ORDERS STARTS

	public String viewOrdershome()
	{
		trace("**** View order home ****** ");
		List persprodlist=null,branchlist=null;
		
		String inst_id =comInstId();
		String usertype = comuserType();
		HttpSession session = getRequest().getSession();
		
		try {
			if (usertype.equals("INSTADMIN")) {
				trace("Getting branch list....");
				branchlist = commondesc.generateBranchList(inst_id, jdbctemplate);
				if(!(branchlist.isEmpty())){ 
					setBranch_list(branchlist);
					session.setAttribute("curerr", "S");
					session.setAttribute("curmsg","");
					System.out.println("Branch list is not empty");
					trace("Branch list is not empty");
				}
				else{					
					setBranch_list(branchlist);
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg"," No Branch Details Found ");
					trace("Branch List is empty ");
					return "personalproducts";
				}
				
				persprodlist=commondesc.getProductList(inst_id, jdbctemplate, session);
			}else{
				persprodlist=commondesc.getProductList(inst_id, (String)session.getAttribute("BRANCHCODE"), jdbctemplate);
			}
			
			trace("Getting product list....");
			
			if (!(persprodlist.isEmpty())){				
				setPersonalproductlist(persprodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg","");
				trace("Product List is : "+persprodlist);
			} else{
				setPersonalproductlist(persprodlist);				
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg","No Product Details Found ");
				trace("No Product Details Found ");
			}

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg","Exception : Unable to continue the process...");
			trace("Exception : while fetching the product  " + e.getMessage() );
		}
		return "vieworderhome";
	}
	


	public String getPersonalorders()
	{
		
		HttpSession session = getRequest().getSession();
		String brcode = getRequest().getParameter("branchcode");
		String cardtype = getRequest().getParameter("cardtype");
		String order_status = getRequest().getParameter("orderstatus");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		String kyc_flag = getRequest().getParameter("kyc_flag");
		trace("Request brcode "+brcode+" cardtype "+cardtype+" fromdate "+fromdate+" todate "+todate);
		String dateflag="ORDERED_DATE";
		String inst_id =comInstId();
		String usertype = comuserType();
		String mkck_status="";
		List vieworderlist = null;
		 
		if( cardtype.equals("ALL")){
			cardtype="";
		}
		if( order_status.equals("ALL")){
			mkck_status = "";
		}
		
		try {
			trace("Getting order list...");
			
			String condition = mkck_status+" AND ORDER_STATUS='01'"+commondesc.filterCondition(cardtype, 
				brcode, fromdate,todate, dateflag);
			enctrace(" View Condition us :  "+condition);
			vieworderlist = commondesc.getPersonalOrderList(inst_id,condition, jdbctemplate);
			
			System.out.println("  vieworderlist   ===> "+vieworderlist);
			if(!(vieworderlist.isEmpty()))
			{
				setPersonalorderlist(vieworderlist);
		 
			}else
			{
				setPersonalorderlist(vieworderlist);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "No Order Found");
				trace("No order found....");
				return viewOrdershome();
			}
		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception: Could not continue the proces...");
			trace("Exception : While Fetching the Orders "+e.getMessage());
			e.printStackTrace();
		}
		return "viewpersonalorder";
	}
	
	private List customerdetailslist;
	public List getCustomerdetailslist() {
		return customerdetailslist;
	}
	public void setCustomerdetailslist(List customerdetailslist) {
		this.customerdetailslist = customerdetailslist;
	}

	private char custdetailsstatus;
	public char getCustdetailsstatus() {
		return custdetailsstatus;
	}

	public void setCustdetailsstatus(char custdetailsstatus) {
		this.custdetailsstatus = custdetailsstatus;
	}
	public String getCustomerdetails()
	{
		trace("***** Getting customer details *********");
		enctrace("***** Getting customer details *********");
		HttpSession session = getRequest().getSession();
		
		String orderref = getRequest().getParameter("orderrefnum");
		System.out.println("  orderref   ===> "+orderref);
		String inst_id =comInstId();
		try {
			List customerinfo=null;
			trace("Getting customer details....");
			customerinfo = commondesc.getCustomerdetails(inst_id, orderref, jdbctemplate);
			System.out.println(" customerinfo "+customerinfo);
			if(!(customerinfo.isEmpty())){
				setCustomerdetailslist(customerinfo);
				setCustdetailsstatus('Y'); 
			}
			else{
				setCustdetailsstatus('N');
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "No Customer Details Found");
				trace("No Customer Details Found");
			}
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Exception : Could not continue the process...");
			trace("Exception: While getting the customer details : " + e.getMessage() );
		}
		trace("\n\n");enctrace("\n\n");
		return "success";
	}
	
	public String orderAuthorizehome()
	{
		trace("***** Order authorze *********** ");enctrace("***** Order authorze *********** ");
		List persprodlist=null; 
		String inst_id =comInstId();
		HttpSession session = getRequest().getSession();
		
		try {
			trace("Getting product list....");
			persprodlist=commondesc.getProductList(inst_id, jdbctemplate, session);
			if (!(persprodlist.isEmpty())){ 
				setPersonalproductlist(persprodlist);				
			} else{				
				setPersonalproductlist(persprodlist);			
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg","No Product Details Found ");
				trace("No Product Details Found ");
			}

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg","Exception : Could not continue the process");
			trace("Exception : Error While Fetching The Product Details  "+ e.getMessage());
		}
		trace("\n\n");enctrace("\n\n");
		return "orderauthorizehome";
	}
	private List persauthorderlist;
	public List getPersauthorderlist() {
		return persauthorderlist;
	}
	public void setPersauthorderlist(List persauthorderlist) {
		this.persauthorderlist = persauthorderlist;
	}

	public String getAuthrizeorders()
	{
		trace("****** Get authorze order ********** ");
		HttpSession session = getRequest().getSession(); 
		
		
		String branch = getRequest().getParameter("branchcode");
		String binno = getRequest().getParameter("cardtype");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		trace("Request got branch : "+branch+"   binno: "+binno+"  fromdate : "+fromdate+"  todate : "+todate);
		String dateflag ="MAKER_DATE";
		String inst_id=comInstId();
		List authorizeorderlist = null;
		
		try {
			String condition1 =" AND ORDER_STATUS='01' AND MKCK_STATUS='M'";
			trace("Getting order list ....");
			String condtion = condition1+commondesc.filterCondition(binno, branch, fromdate, todate, dateflag);
			enctrace("order list condtion : "+condtion);
			authorizeorderlist = commondesc.getPersonalOrderList(inst_id,condtion, jdbctemplate);
			
			trace("authorizeorderlist : "+authorizeorderlist);
			if(!(authorizeorderlist.isEmpty())){
				setPersauthorderlist(authorizeorderlist);				
			}else{
				setPersauthorderlist(authorizeorderlist);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg"," No Orders To Authorize ");
				trace("No Orders to Authorze....");
				return orderAuthorizehome();				
			}
		} catch (Exception e) { 
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg","Exception : Could not get orders...");
			trace("Exception : while fetching the ordes to authorize : " + e.getMessage());
			
		}
		trace("\n\n");enctrace("\n\n");
		return "authorizeorders";
	}
	
	
	public String orderAuthdeauth(){
		trace("Order authorize / deauthorze.....");
		enctrace("Order authorize / deauthorze.....");
		HttpSession session = getRequest().getSession(); 
		IfpTransObj transact = commondesc.myTranObject("AUTHORDER", txManager);
		String instid = comInstId();
		String userid = comUserId();
		String authstatus = "";
		String statusmsg = "";
		String err_msg="";
		String order_refnum[] = getRequest().getParameterValues("personalrefnum");
		String remarks = getRequest().getParameter("reason");
		trace("Total Orders Selected : "+order_refnum.length);
		
		//added by gowtham_240719
				String  ip=(String) session.getAttribute("REMOTE_IP");
		
		if (  getRequest().getParameter("authorize") != null ){
			trace( "AUTHORIZE..........." );
			authstatus = "P";
			statusmsg = " Authorized ";
			err_msg="Authorize";
		}else  if (  getRequest().getParameter("deauthorize") != null  ){
			trace( "DE AUTHORIZE..........." );
			authstatus = "D";
			statusmsg = " De-Authorized ";
			err_msg="De-Authorize";
		} 
		try {
			int cnt=0;
			for(int i=0;i<order_refnum.length;i++)
			{
				trace("Selected Refnums : "+order_refnum[i]);
				trace("Updating order status....");
				
				/*String update_authdeauth_qury = "UPDATE PERS_CARD_ORDER SET MKCK_STATUS='"+authstatus+"',"
				+ "REMARKS='"+remarks+"',CHECKER_DATE=SYSDATE,CHECKER_TIME="+commondesc.def_time+","
				+ "CHECKER_ID='"+userid+"' WHERE INST_ID='"+instid+"' AND ORDER_REF_NO='"+order_refnum[i].trim()+"'";
				enctrace("update_authdeauth_qury : " + update_authdeauth_qury  );
				cnt = cnt+commondesc.executeTransaction(update_authdeauth_qury, jdbctemplate);*/
				
				//by gowtham
				String update_authdeauth_qury = "UPDATE PERS_CARD_ORDER SET MKCK_STATUS='"+authstatus+"',"
				+ "REMARKS='"+remarks+"',CHECKER_DATE=SYSDATE,CHECKER_TIME="+commondesc.def_time+","
				+ "CHECKER_ID='"+userid+"' WHERE INST_ID='"+instid+"' AND ORDER_REF_NO='"+order_refnum[i].trim()+"'";
				enctrace("update_authdeauth_qury : " + update_authdeauth_qury  );
				cnt = cnt+commondesc.executeTransaction(update_authdeauth_qury, jdbctemplate);
				
				trace("Got : " + cnt);
				
				/*************AUDIT BLOCK**************/ 
				try{  
					
					//added by gowtham_240719
					trace("ip address======>  "+ip);
					auditbean.setIpAdress(ip);
					
					String subproduct = commondesc.getSubProductByOrder(instid, order_refnum[i], jdbctemplate, "PERS"); 
					auditbean.setActmsg("Order " + statusmsg + "");
					auditbean.setUsercode(userid);
					auditbean.setAuditactcode("0101");  
					
					//added by gowtham_010819
					String pcode=null;
					auditbean.setSubproduct(subproduct);
					
					auditbean.setApplicationid(order_refnum[i]);
					auditbean.setRemarks(remarks);
					commondesc.insertAuditTrail(instid, userid, auditbean, jdbctemplate, txManager);
				 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
				 /*************AUDIT BLOCK**************/ 
			}
			if(order_refnum.length == cnt){
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", cnt + "  Order " + statusmsg + " successfully." );
			//commondesc.commitTxn(jdbctemplate);
			txManager.commit(transact.status);
			trace( cnt + "  Order " + statusmsg + " successfully. got committed..." ); 
			}
			else{
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " Selected Orders Not "+statusmsg+" Successfully");
				//commondesc.rollbackTxn(jdbctemplate);
				txManager.rollback(transact.status);
				trace(" Selected Orders Not "+statusmsg+" Successfully");
			}
		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Unable to "+err_msg+" The Orders ");
			//commondesc.rollbackTxn(jdbctemplate);
			txManager.rollback(transact.status);
			trace("Exception: "+ e.getMessage() );
		}
		return orderAuthorizehome();
	}
	

	public String kyCustomercardorder()	{
		System.out.println("=======================Inside the KYC CRDS Function=====================");
		List pers_prodlist,br_list=null;
		String inst_id =comInstId();
		String usertype = comuserType();
		HttpSession session = getRequest().getSession(); 
		try {
			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = commondesc.generateBranchList(inst_id, jdbctemplate);
				if(!(br_list.isEmpty())){
					setBranch_list(br_list);
					session.setAttribute("curerr", "S");
					session.setAttribute("curmsg","");
					System.out.println("Branch list is not empty");
				}
				else{
					setBranch_list(br_list);
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg"," No Branch Details Found ");
					System.out.println("Branch List is empty ");
					return "kyccardorderhome";
				}
			}
			pers_prodlist=commondesc.getProductList(inst_id, jdbctemplate, session);
			if (!(pers_prodlist.isEmpty())){
				setPersonalproductlist(pers_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg","");
				System.out.println("Product List is ===> "+pers_prodlist);
			} else{
				System.out.println("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg"," No Product Details Found ");
			}

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg"," Error While Fetching The Product Details  "+ e.getMessage());
		}
		System.out.println("return personalproducts");
		
		
		return "kyccardorderhome";
	}
	
	
	private List kyccustdetails;
	
	public List getKyccustdetails() {
		return kyccustdetails;
	}
	public void setKyccustdetails(List kyccustdetails) {
		this.kyccustdetails = kyccustdetails;
	}



	public String saveKYCCardorder() { 
		HttpSession session = getRequest().getSession();
		
		IfpTransObj transact = commondesc.myTranObject("SAVEKYCORDER", txManager);
		String card_branchcode = getRequest().getParameter("branchcode").trim();
		String cardtype=(getRequest().getParameter("cardtype")).trim();
		String subproduct = (getRequest().getParameter("subproductlist")).trim();
		String product_count = (getRequest().getParameter("Count")).trim();
		String embossName = (getRequest().getParameter("emposs")).trim();
		String encoding = (getRequest().getParameter("encode")).trim();
		String existingcardno = getRequest().getParameter("existingcardno");

		String instid = comInstId();
		String action = (String)session.getAttribute("CARDOREDR_ACT");
		trace("Action Typr =====> "+action+"Instituion id ===> "+instid);
		
		trace("card_branchcode ==> "+card_branchcode+"\ncardtype ==> "+cardtype+"\nsubproduct ==> "+subproduct+	"\nproduct_count ==> "+product_count+"\nembossName ==> "+embossName+"\nencoding ==> "+encoding+" existingcardno ==> "+existingcardno);
		 
		
		try	{
			
			int validcardno = commondesc.checkCardNumberValid(instid, existingcardno, jdbctemplate); 
			trace("Checking existing card number valid or not ...got : "+validcardno);
			if(validcardno == 0 || validcardno == -1) { 
				addActionError("Invalid Existing Card  Number ");
				setAct((String)session.getAttribute("CARDOREDR_ACT"));
				return kyCustomercardorder();
			}
			
			String custnum = commondesc.getCustomerIdByCardNumber(instid, existingcardno, jdbctemplate);
			if( custnum == null ){
				addActionError("Customer Details Not Found");
				return kyCustomercardorder();
			}
			
			String cust_regist_req = commondesc.personalOrdercustomerregistrationcheck(instid,cardtype,subproduct,jdbctemplate);  
			int cust_exist = commondesc.checkCustomerexist(instid,custnum,"IFP_CUSTINFO_PRODUCTION",jdbctemplate);
			System.out.println("Customer Registraion Required");
			System.out.println("The Customer Num Count ====> "+cust_exist);
			if(cust_exist == 0 || cust_exist == -1) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Invalid Customer Number ");
				setAct((String)session.getAttribute("CARDOREDR_ACT"));
				return kyCustomercardorder();
			}
			
			List cutomerdetails = commondesc.getKYCdetails(instid,custnum,jdbctemplate);
			if(cutomerdetails == null) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "No Customer Details Found ");
				setAct((String)session.getAttribute("CARDOREDR_ACT"));
				return kyCustomercardorder();
			}
			System.out.println("KYC Customer Detaik ===> "+cutomerdetails); 
			session.setAttribute("KYC_BRCODE",card_branchcode);
			session.setAttribute("KYC_CARDTYPE",cardtype);
			session.setAttribute("KYC_SUBPROD",subproduct);
			session.setAttribute("KYC_ORDERCOUNT",product_count);
			session.setAttribute("KYC_EMBOSS",embossName);
			session.setAttribute("KYC_ENCODE",encoding);
			session.setAttribute("KYC_CIN",custnum);
			//session.setAttribute("APPDATE",appdate);
			//session.setAttribute("APPNUM",appno);
			setKyccustdetails(cutomerdetails);
			setNationlist(commondesc.gettingNations(jdbctemplate));
			//setQuestionlist(commondesc.gettingSequrityQuestion(jdbctemplate));
			setDocumenttypelist(commondesc.gettingDocumnettype(instid, jdbctemplate));
		 
		}
		catch (Exception e)  {
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Error "+e.getMessage());
			setAct((String)session.getAttribute("CARDOREDR_ACT"));
			System.out.println("Error is --> "+e.getMessage());
			return kyCustomercardorder();
		}
		return "kyccustomerdetails"; 
	}

	
	public String savepersonalKYCorder()
	{
		HttpSession session = getRequest().getSession();
		
		IfpTransObj transact = commondesc.myTranObject("SAVEORDER", txManager);
		String brcode = (String)session.getAttribute("KYC_BRCODE");
		String productcode = (String)session.getAttribute("KYC_CARDTYPE");
		String subprod = (String)session.getAttribute("KYC_SUBPROD");
		String count = (String)session.getAttribute("KYC_ORDERCOUNT");
		String embossname = (String)session.getAttribute("KYC_EMBOSS");
		String encodename = (String)session.getAttribute("KYC_ENCODE");
		String cin = (String)session.getAttribute("KYC_CIN");
		//String apl_date = (String)session.getAttribute("APPDATE");
		//String apl_no = (String)session.getAttribute("APPNUM");
		String instid = comInstId();
		String userid = comUserId();
		String card_order_act=(String)session.getAttribute("CARDOREDR_ACT");
		
		System.out.println("card_branchcode ==> "+brcode+"\nproductcode ==> "+productcode+"\nsubproduct ==> "+subprod+
				"\nproduct_count ==> "+count+"\nembossName ==> "+embossname+"\nencoding ==> "+encodename+"\ncustnum ==> "+cin);

		try {
			String order_refno ="N";
			int orderupdate = -1;
			synchronized (this) {
			order_refno=commondesc.generateorderRefno(instid,jdbctemplate);
			System.out.println("New Order Refnumber is ====> "+order_refno);
			// order_refno = "N","M","E"
			if(order_refno.equals("N") || order_refno.equals("E"))
			{
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg","Error While Getting the Order Reference Number");
				return kyCustomercardorder();
			}
			if(order_refno.equals("M"))
			{
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg","Order Reference Number Length Reached Maximum Level");
				return kyCustomercardorder();
			}
			
			/*String cinoder_sequpdate = "UPDATE SEQUENCE_MASTER SET ORDER_REFNO=ORDER_REFNO+1 WHERE INST_ID='"+instid+"'";
			System.out.println("cinoder_sequpdate====> "+cinoder_sequpdate);
			orderupdate = jdbctemplate.update(cinoder_sequpdate);*/
			
			//by gowtham
			String cinoder_sequpdate = "UPDATE SEQUENCE_MASTER SET ORDER_REFNO=ORDER_REFNO+1 WHERE INST_ID=? ";
			System.out.println("cinoder_sequpdate====> "+cinoder_sequpdate);
			orderupdate = jdbctemplate.update(cinoder_sequpdate,new Object[]{instid});
			
			System.out.println("orderupdate====> "+orderupdate);
		}
			String binno = commondesc.getBin(instid, productcode, jdbctemplate);
			String cardtype = commondesc.getCardType(instid, productcode, jdbctemplate);
			
			
			String First_name=(getRequest().getParameter("fname"));
			String Middle_name=(getRequest().getParameter("mname"));
			String Last_name=(getRequest().getParameter("lname"));
			String Father_name=(getRequest().getParameter("f_name"));
			String Mother_name=(getRequest().getParameter("m_name"));
			String Married=(getRequest().getParameter("mstatus"));
			String Spouse_name=(getRequest().getParameter("spname"));
			String Sex=(getRequest().getParameter("gender"));
			String BDay=(getRequest().getParameter("dob"));
			String Nationality=(getRequest().getParameter("nations"));
			String E_mail=(getRequest().getParameter("email"));
			String Mobile_No=(getRequest().getParameter("mobile"));
			String Phone_No=(getRequest().getParameter("phone"));
			String Occupation=(getRequest().getParameter("work"));
			String appdate = (getRequest().getParameter("appdate")).trim();
			String appno = (getRequest().getParameter("appno")).trim();			
			String Identity_No=(getRequest().getParameter("idno"));
			String Identity_document=(getRequest().getParameter("iddoc"));
			String postal_address1 = (getRequest().getParameter("paddress1"));
			String postal_address2 = (getRequest().getParameter("paddress2"));
			String postal_address3 = (getRequest().getParameter("paddress3"));
			String postal_address4 = (getRequest().getParameter("paddress4"));
			String Samepostal_address = (getRequest().getParameter("same"));
			String resident_address1 = (getRequest().getParameter("raddress1"));
			String resident_address2 = (getRequest().getParameter("raddress2"));
			String resident_address3 = (getRequest().getParameter("raddress3"));
			String resident_address4 = (getRequest().getParameter("raddress4"));
			String Security = "";
			String Aswer = "";
			System.out.println("postal_address1 ==> "+postal_address1+"\npostal_address2 ==> "+postal_address2+"\npostal_address3 ==> "+postal_address3+"\npostal_address4 ==> "+postal_address4);
			System.out.println("resident_address1 ==> "+resident_address1+"\nresident_address2 ==> "+resident_address2+"\nresident_address3 ==> "+resident_address3+"\nresident_address4 ==> "+resident_address4);

			String Order_Type = "P"; 
			
			
			if( postal_address1 == "" || postal_address1 ==null )
			{
				postal_address1 = "";
			}
			if(postal_address2 == "" || postal_address2 ==null )
			{
				postal_address2 = "";
			}
			if(postal_address3 == "" || postal_address3 ==null )
			{
				postal_address3 = "";
			}
			if(postal_address4 == "" || postal_address4 ==null )
			{
				postal_address4 = "";
			}
			if( resident_address1 == "" || resident_address1 == null )
			{
				resident_address1 = "";
			}
			if( resident_address2 == "" || resident_address2 == null )
			{
				resident_address2 = "";
			}
			if( resident_address3 == "" || resident_address3 == null )
			{
				resident_address3 = "";
			}		
			if( resident_address4 == "" || resident_address4 == null )
			{
				resident_address4 = "";
			}


			if(Samepostal_address != null)
			{
				resident_address1 = postal_address1;
				resident_address2 = postal_address2;
				resident_address3 = postal_address3;
				resident_address4 = postal_address4;
			}

			
			String postal_address = postal_address1+"|"+postal_address2+"|"+postal_address3+"|"+postal_address4+"|";
			String resident_address = resident_address1+"|"+resident_address2+"|"+resident_address3+"|"+resident_address4+"|";
			System.out.println("Postal Address compiednd is ===> "+postal_address);
			System.out.println("Residentr Address Compined is ==="+resident_address);
			
			
			
			if(Mother_name == "")
			{
				Mother_name = "";
			}
			if(Middle_name =="")
			{
				Middle_name = "";
			}
			if(Spouse_name == null)
			{
				Spouse_name ="";
			}
			
			String Maker_id = userid;
			String Checker_id = "";
			String Remark = "";
			String checker_date = CommonDesc.default_date_query; //this is default date 
			String order_time = "(select trim(substr(to_char(sysdate,'dd-MON-yy hh24:mi:ss'),10,10)) from dual)";
			String maker_time = "(select trim(substr(to_char(sysdate,'dd-MON-yy hh24:mi:ss'),10,10)) from dual)";
			String checker_time ="'00:00:00'";
			String order_status = "X",mkck_status="X",Customer_status="X";
			if(card_order_act.equals("M"))
			{
				order_status="01";
				mkck_status="M";
				Customer_status = "U";
			}
			else if(card_order_act.equals("D"))
				{
					mkck_status="P";
					Customer_status="A";
					order_status="01";
					Checker_id=Maker_id;
					checker_date = "(sysdate)";
					checker_time = "(select trim(substr(to_char(sysdate,'dd-MON-yy hh24:mi:ss'),10,10)) from dual)";
				}

				
				String custinfo_qury="INSERT INTO IFP_CUSTINFO_PROCESS(INST_ID,CIN,FNAME,MNAME,LNAME,FATHER_NAME,MOTHER_NAME,MARITAL_STATUS," +
						"SPOUSE_NAME,GENDER,DOB,NATIONALITY,EMAIL_ADDRESS,MOBILE_NO,PHONE_NO,OCCUPATION,ID_NUMBER, " +
						"ID_DOCUMENT, SECUTIRY_QUESTION, SECURITY_ANSWER,POST_ADDR1,POST_ADDR2,POST_ADDR3,POST_ADDR4,RES_ADDR1,RES_ADDR2,RES_ADDR3,RES_ADDR4,MAKER_DATE," +
						"MAKER_ID,CHECKER_DATE,CHECKER_ID,MKCK_STATUS,CUSTOMER_STATUS,KYC_FLAG) VALUES ('"+instid+"','"+cin+"','"+First_name+"'," +
						"'"+Middle_name+"','"+Last_name+"','"+Father_name+"','"+Mother_name+"','"+Married+"','"+Spouse_name+"','"+Sex+"',to_date('"+BDay+"','DD-MM-YYYY'),'"+Nationality+"','"+E_mail+"','"+Mobile_No+"'," +
						"'"+Phone_No+"','"+Occupation+"','"+Identity_No+"','"+Identity_document+"', '"+Security+"','"+Aswer+"','"+postal_address1+"','"+postal_address2+"','"+postal_address3+"','"+postal_address4+"','"+resident_address1+"','"+resident_address2+"','"+resident_address3+"','"+resident_address4+"',(sysdate), " +
						"'"+Maker_id+"',(sysdate), '"+Checker_id+"', '"+mkck_status+"', '"+Customer_status+"','1')";
				System.out.println("INSERT INTO IFP_CUSTINFO_PROCESS ===> "+custinfo_qury);
				String order_qury = "INSERT INTO PERS_CARD_ORDER (INST_ID, ORDER_REF_NO, CARD_TYPE_ID, SUB_PROD_ID, " +
						"PRODUCT_CODE, CARD_QUANTITY, ORDER_STATUS, ORDER_TYPE, ORDERED_DATE, ORDERED_TIME, EMBOSSING_NAME, " +
						"MKCK_STATUS, REMARKS,ENCODE_DATA, MAKER_DATE, MAKER_TIME, MAKER_ID, CHECKER_DATE, CHECKER_TIME, CHECKER_ID, BRANCH_CODE, " +
						"BIN, APP_DATE, CIN, APP_NO,KYC_FLAG) VALUES ('"+instid+"', '"+order_refno+"', '"+cardtype+"', '"+subprod+"', '"+productcode+"', '"+count+"', '"+order_status+"', '"+Order_Type+"',(sysdate), " +
						order_time+", '"+embossname+"','"+mkck_status+"','"+Remark+"','"+encodename+"',(sysdate), "+maker_time+", '"+Maker_id+"', "+checker_date+", "+checker_time+", '"+Checker_id+"','"+brcode+"'," +
						"'"+binno+"',to_date('"+appdate+"','dd-mm-yyyy'),'"+cin+"','"+appno+"','1')";
				System.out.println("INSERT INTO PERS_CARD_ORDER====> "+order_qury);
				
				//String updateorder_qury = commondesc.updateOrderrefnumcount(instid);
				//System.out.println("UPdate Order Refnum Query ===> "+updateorder_qury);
				 
				int custinfo = commondesc.executeTransaction(custinfo_qury, jdbctemplate);
				int order = commondesc.executeTransaction(order_qury, jdbctemplate);
				//int orderupdate = commondesc.executeTransaction(updateorder_qury);
				System.out.println("custinfo ===> "+custinfo+" order===> "+order+" orderupdate===> "+orderupdate);
				if(custinfo == 1 && order == 1 && orderupdate == 1)
				{
					//commondesc.commitTxn(jdbctemplate);
					txManager.commit(transact.status);
					session.setAttribute("preverr","S");
					session.setAttribute("prevmsg","Card Ordered Sucessfully , The Reference Number is : "+order_refno+" and Customer Id is : "+cin);
					System.out.println("This Txn got Commited ");
				}else
				{
					
					//commondesc.rollbackTxn(jdbctemplate);
					txManager.rollback(transact.status);
					session.setAttribute("preverr","E");
					session.setAttribute("prevmsg","Card Order Failed");
					System.out.println("THis Txn Got RolledBack ");
				}
		} catch (Exception e) 
		{
			txManager.rollback(transact.status);
			session.setAttribute("preverr","E");
			session.setAttribute("prevmsg","Card Order Failed:Error "+e.getMessage());
			System.out.println("Exception in KYC Order ===> "+e.getMessage());
		}
							
			
			setAct((String)session.getAttribute("CARDOREDR_ACT"));
			session.removeAttribute("KYC_BRCODE");
			session.removeAttribute("KYC_CARDTYPE");
			session.removeAttribute("KYC_SUBPROD");
			session.removeAttribute("KYC_ORDERCOUNT");
			session.removeAttribute("KYC_EMBOSS");
			session.removeAttribute("KYC_ENCODE");
			session.removeAttribute("KYC_CIN");
			session.removeAttribute("APPDATE");
			session.removeAttribute("APPNUM");
			return kyCustomercardorder();
	}
	
	
// Edit Order Starts
	private List allorderList;
	public List getAllorderList() {
		return allorderList;
	}


	public void setAllorderList(List allorderList) {
		this.allorderList = allorderList;
	}

	private String values;
	public String getValues() {
		return values;
	}
	public void setValues(String values) {
		this.values = values;
	}

	public void getAjaxvaluefromaction()
	{
		HttpSession session = getRequest().getSession();
		
		try{
			
		
		System.out.println("Values ---> "+getValues());
		String qury ="select trim(order_ref_no) as order_ref from PERS_CARD_ORDER where order_ref_no like '%"+getValues()+"%' order by order_ref_no";
		System.out.println("qury===> "+qury);
		List result = jdbctemplate.queryForList(qury);
		System.out.println("List is==> "+result);
		Iterator itr = result.iterator();
		String s = "<input type=\"text\" name=\"search_value\" id=\"search_value\" onkeyup=\'callAjaxfunctions(this.value);\'";
		while(itr.hasNext())
		{
			Map map = (Map)itr.next();
			String d = (String)map.get("ORDER_REF");
			s = s + "value='"+d+"'";
		}
		s = s+">";
		System.out.println("Reulst--> "+s);
		}
		catch (Exception e) {
			System.out.println("Exceptionm "+e.getMessage());
		}
	}
	
	
	// Edit Order Ends
	
	
	
	
	public String deleteCardorderhome()
	{
		System.out.println("=======================Inside the getpersonalProductetails Function=====================");
		List pers_prodlist,br_list=null;
		String inst_id =comInstId();
		String usertype = comuserType();
		HttpSession session = getRequest().getSession();
		
		try {
			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = commondesc.generateBranchList(inst_id, jdbctemplate);
				if(!(br_list.isEmpty())){
					setBranch_list(br_list);
					session.setAttribute("curerr", "S");
					session.setAttribute("curmsg","");
					System.out.println("Branch list is not empty");
				}
				else{
					setBranch_list(br_list);
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg"," No Branch Details Found ");
					System.out.println("Branch List is empty ");
					return "deletecardorderhome";
				}
				pers_prodlist=commondesc.getProductList(inst_id, jdbctemplate, session);
			}else{
				pers_prodlist=commondesc.getProductList(inst_id, (String)session.getAttribute("BRANCHCODE"), jdbctemplate);
			}
			
			if (!(pers_prodlist.isEmpty())){
				setPersonalproductlist(pers_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg","");
				System.out.println("Product List is ===> "+pers_prodlist);
			} else{
				System.out.println("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg"," No Product Details Found ");
			}

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg"," Error While Fetching The Product Details  "+ e.getMessage());
		}
		System.out.println("return deletecardorderhome");
		
		return "deletecardorderhome";
	}
	
	
	private List deleteorderlist;
	
	public List getDeleteorderlist() {
		return deleteorderlist;
	}
	public void setDeleteorderlist(List deleteorderlist) {
		this.deleteorderlist = deleteorderlist;
	}


	public String getDeleteCardorders()
	{
		List deleteorderdetails;
		HttpSession session = getRequest().getSession();
		
		
		String branch = getRequest().getParameter("branchcode");
		String productcode = getRequest().getParameter("cardtype");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		String orderstatus  = getRequest().getParameter("orderstatus");
		String kyc_flag  = getRequest().getParameter("kyc_flag");
		String instid = comInstId();
		String dateflag="ORDERED_DATE";
		System.out.println("kyc_flag==> "+kyc_flag);
		System.out.println("Branch==> "+branch+" Form Date==> "+fromdate+" todate ==> "+todate+" orderstatus===> "+orderstatus);
		/*System.out.println("Branch==> "+branch+" Card Type ===> "+productcode+" Form Date==> "+fromdate+" todate ==> "+todate+" orderstatus===> "+orderstatus);*/
		try
		{
			if( productcode.equals("ALL") ){
				productcode = "";
			}
			String condition_one =commondesc.filterCondition(productcode, branch, fromdate, todate, dateflag);
			System.out.println("Condition Data===> "+condition_one);
			deleteorderdetails = commondesc.getOrderdelete(instid,"D",orderstatus, condition_one, jdbctemplate);
			System.out.println("deleteorderdetails===> "+deleteorderdetails);
			if(deleteorderdetails.isEmpty())
			{
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg","No Records Available for Delete.");
				return deleteCardorderhome();
			}
			else{
				setDeleteorderlist(deleteorderdetails);
				session.setAttribute("prevmsg","");
			}
		}
		catch (Exception e) 
		{
			System.out.println("Erorrr---> "+e.getMessage());
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg","Error While Getting The Order Details "+e.getMessage());
			e.printStackTrace();
			return deleteCardorderhome();
		}
		
		return "deletecardorderlist";
	}
	
	private List deleteorderinfo;
	public List getDeleteorderinfo() {
		return deleteorderinfo;
	}

	public void setDeleteorderinfo(List deleteorderinfo) {
		this.deleteorderinfo = deleteorderinfo;
	}

	private char customer_reg;
	
	
	public char getCustomer_reg() {
		return customer_reg;
	}


	public void setCustomer_reg(char customer_reg) {
		this.customer_reg = customer_reg;
	}

	public String getCardorderdetails()
	{
		HttpSession session = getRequest().getSession();
		
		String instid = comInstId();
		String orderref = getRequest().getParameter("orderrefnum");
		System.out.println("Order Refnum ----> "+orderref);
		try
		{
			String cust_regreq = commondesc.checkCustomerregistrationconfig(instid,orderref,jdbctemplate);
			System.out.println("cust_regreq====> "+cust_regreq);
			if(cust_regreq.equals("N"))
			{
				System.out.println("Get Order Details Only");
				int cin_exsist = commondesc.checkCustomerdetailsexsist(instid,orderref,jdbctemplate);
				System.out.println("cin_exsist===> "+cin_exsist);
				if(cin_exsist == 1)
				{
					List orderinfo = commondesc.geteditdeleteOrderdetails(instid, orderref, jdbctemplate);
					setCustomer_reg('Y');
					setDeleteorderinfo(orderinfo);
					session.setAttribute("prevmsg", "");
				}else{
					System.out.println("Get Order Details Only");
					List orderinfo = commondesc.getPersonalorderdetails(instid,orderref,jdbctemplate);
					setCustomer_reg('N');
					setDeleteorderinfo(orderinfo);
					session.setAttribute("prevmsg", "");
				}

			}
			else
			{
				System.out.println("Get Order and customer Details Only"+orderref);
				int cin_exsist = commondesc.checkCustomerdetailsexsist(instid,orderref,jdbctemplate);
				System.out.println("cin_exsist===> "+cin_exsist);
				if(cin_exsist == 1)
				{
					List orderinfo = commondesc.geteditdeleteOrderdetails(instid, orderref, jdbctemplate);
					setCustomer_reg('Y');
					setDeleteorderinfo(orderinfo);
					session.setAttribute("prevmsg", "");
				}else{
					List orderinfo = commondesc.getPersonalorderdetails(instid,orderref,jdbctemplate);
					setCustomer_reg('N');
					setDeleteorderinfo(orderinfo);
					session.setAttribute("prevmsg", "");
				}

			}

		}
		catch (Exception e) 
		{
			System.out.println("ERROR+ ===> "+e.getMessage());
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Error "+e.getMessage());
		}
		return "deleteorderdetails";
	}
	
	
	public String deleteCardorderConfirm()	{
		
		HttpSession session = getRequest().getSession();
		
		IfpTransObj transact = commondesc.myTranObject("SAVEORDER", txManager);
		String instid = comInstId();
		String[] refnum = getRequest().getParameterValues("personalrefnum");
		try 
		{
			int alldelete_status = 0;
			int ordercount = refnum.length;
			for(int i=0;i<ordercount;i++)
			{
				
				StringBuffer cin_qury = new StringBuffer("select cin from PERS_CARD_ORDER where inst_id='"+instid+"' and ");
				StringBuffer delete_order_qury = new StringBuffer("delete from PERS_CARD_ORDER where inst_id='"+instid+"' and ");
				StringBuffer delete_cust_qury = new StringBuffer("delete from IFP_CUSTINFO_PROCESS where inst_id='"+instid+"' and ");
				int cust_delete = 0, order_delete = 0;
				cin_qury = cin_qury.append("order_ref_no='"+refnum[i]+"'");
				System.out.println("cin_qury  ===> "+cin_qury);
				delete_order_qury = delete_order_qury.append("order_ref_no='"+refnum[i]+"'");
				System.out.println("delete_order_qury===>  "+delete_order_qury);
				delete_cust_qury =  delete_cust_qury.append("cin=("+cin_qury+")");
				System.out.println("delete_cust_qury==> "+delete_cust_qury);
				
				StringBuffer count_qury = new StringBuffer("select count(*) from IFP_CUSTINFO_PROCESS where INST_ID='"+instid+"' and cin=");
				count_qury = count_qury.append("("+cin_qury+")");
				System.out.println("count_qury=====> "+count_qury);
				String cust_reg = commondesc.checkCustomerregistrationconfig(instid,refnum[i],jdbctemplate);
				if(cust_reg.equals("N"))
				{
					System.out.println("Customer Registration Not Required");
					int cust_exist = jdbctemplate.queryForInt(count_qury.toString());
					System.out.println("cust_exist====> "+cust_exist);
					if(cust_exist == 1)
					{
						System.out.println(" Customer Info Exsists So Try TO Delete ");
						cust_delete = jdbctemplate.update(delete_cust_qury.toString());
					}else{
						System.out.println(" Customer Info Not Exsists So Just Have The Status ");
						cust_delete = 2;
					}
					System.out.println("cust_exist_count====> "+cust_delete);
					order_delete = jdbctemplate.update(delete_order_qury.toString());
					System.out.println("order_delete ===> "+order_delete);
					if(order_delete == 1 && (cust_delete == 1 || cust_delete == 2)){
						alldelete_status = alldelete_status + 1;
					}else{
						break;
					}
				}			
				else
				{
					System.out.println("Customer Registration Required");
					int cust_exist = jdbctemplate.queryForInt(count_qury.toString());
					System.out.println("cust_exist====> "+cust_exist);
					if(cust_exist == 1)
					{
						cust_delete = jdbctemplate.update(delete_cust_qury.toString());
					}
					else{
						cust_delete = 2;
					}
					System.out.println("order_delete ===> "+cust_delete);
					order_delete = jdbctemplate.update(delete_order_qury.toString());
					System.out.println("cust_delete ===> "+order_delete);
					if( (cust_delete == 1 ||cust_delete == 2 ) && order_delete == 1 )
					{
						alldelete_status = alldelete_status + 1;
					}else{
						break;
					}
				}
			}
			System.out.println("alldelete_status====> "+alldelete_status);
			if(ordercount == alldelete_status)
			{
				//commondesc.commitTxn(jdbctemplate);
				txManager.commit(transact.status);
				System.out.println("Transaction Commited SuccessFully ");
				session.setAttribute("preverr","S");
				session.setAttribute("prevmsg"," Selected Order Deleted Successfully");
			}else{
				//commondesc.rollbackTxn(jdbctemplate);
				txManager.rollback(transact.status);
				System.out.println("Transaction Rollbacked SuccessFully ");
				session.setAttribute("preverr","E");
				session.setAttribute("prevmsg"," Order Delete Failed ");
			}

		} catch (Exception e) 
		{
			//commondesc.rollbackTxn(jdbctemplate);
			txManager.rollback(transact.status);
			System.out.println("Transaction Rollbacked SuccessFully ");
			session.setAttribute("preverr","E");
			session.setAttribute("prevmsg"," Order Delete Failed "+e.getMessage());
		}

		return deleteCardorderhome();
	}	

	
	public String testreport(){
		
		return "showreport";
	}
	
	
	public void testFunction()
	{
		 
		String qury = "select * from IFPROFILE_PRIVILEGE where PROFILE_ID='203'";
		List menulist = null;
		List alllist = new ArrayList();
		String emenilist = null;
		menulist = jdbctemplate.queryForList(qury);
		Map mapper = null;
		if(!(menulist.isEmpty()))
		{
		
			Iterator itr = menulist.iterator();
			while(itr.hasNext())
			{
				Map map = (Map)itr.next();
				emenilist = (String)map.get("MENU_LIST");
			}
			System.out.println("Menu lIsts is ===> "+emenilist);
			
			if(emenilist != null)
			{
				String[] arraymenu = emenilist.split(",");
				System.out.println("The Lenght iof the Array===> "+arraymenu.length);
				for(int i=0;i<arraymenu.length;i++)
				{
					
					if(i !=0 || i != arraymenu.length -1)
					{
						System.out.println("THe Menu in "+i+" Is -->"+arraymenu[i].trim());
						mapper.put("MENUID",arraymenu[i].trim().toString());
					}
				}
				
			}
		}
		
	}

private String name;

	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	public void getvlaue()
	{
		
		
		try{
			
			String buffer = "<div>";
		String value = getName();
		
		String uery = "select ORDER_REF_NO from PERS_CARD_ORDER where ORDER_REF_NO like '"+value+"%'";
		System.out.println("Query ==> "+uery);
		List refnum = jdbctemplate.queryForList(uery);
		
		Iterator itr = refnum.iterator();
		while(itr.hasNext())
		{
			Map map = (Map)itr.next();
			String ornum = (String)map.get("ORDER_REF_NO");
			System.out.println("Refnum value ---> "+ornum);
			buffer = buffer+ornum+"<br>";
		}
		buffer = buffer+"</div>";
		System.out.println("buffer ==> "+buffer);
		getResponse().getWriter().println(buffer);
		}
		catch (Exception e) 
		{
			System.out.println("Exception ===> "+e.getMessage());
		}
		
	}
	

//##############################Pavithra########################33	
	private List editorder;

	public List getEditorder() {
		return editorder;
	}


	public void setEditorder(List editorder) {
		this.editorder = editorder;
	}


	public String  editpersonalOrderhome() 
	{
		System.out.println("=======================edit order HOME=====================");
		HttpSession session = getRequest().getSession();
		session.setAttribute("CARDOREDR_EDIT", act);
		String session_act = (String) session.getAttribute("CARDOREDR_EDIT");
		System.out.println("session_act " + session_act);
		
		List pers_prodlist=null,br_list=null;
		String inst_id =comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		System.out.println("session_act " + session_act+" DATEFILTER_REQ====>"+(String)session.getAttribute("DATEFILTER_REQ"));
		try {
			System.out.println("Inst Id===>"+inst_id+"  Branch Code ===>"+branch);
			

			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = commondesc.generateBranchList(inst_id, jdbctemplate);
				System.out.println("Branch list "+br_list);
				if(!(br_list.isEmpty())){
					setBranchlist(br_list);
					session.setAttribute("curerr", "S");
					session.setAttribute("curmsg","");
					System.out.println("Branch list is not empty");
				}
				else{
					setBranchlist(br_list);
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg"," No Branch Details Found ");
					System.out.println("Branch List is empty ");
					return "editorderhome";
				}
				
				pers_prodlist=commondesc.getProductList(inst_id, jdbctemplate, session);
			}else{
				pers_prodlist=commondesc.getProductList(inst_id, (String)session.getAttribute("BRANCHCODE"), jdbctemplate);
			}
			
			if (!(pers_prodlist.isEmpty())){
				setPersonalproductlist(pers_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg","");
				System.out.println("Product List is ===> "+pers_prodlist);
				
			} else{
				System.out.println("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg"," No Product Details Found ");
				
			}

		} catch (Exception e) 
		{
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg"," Error While Fetching The Product Details  "+ e.getMessage());
		}		
		return "editorderhome";	
	}

	private List perscardgenlist;
	public List getPerscardgenlist() {
		return perscardgenlist;
	}

	public void setPerscardgenlist(List perscardgenlist) {
		this.perscardgenlist = perscardgenlist;
	}
	public String getCardeditorder(){
		System.out.println("Welcome to customer details in view page");
		HttpSession session = getRequest().getSession();
		
		
		//String temp = act;
		//System.out.println("temp==> "+temp);
		String session_act = (String) session.getAttribute("CARDOREDR_EDIT");
		System.out.println("session_act " + session_act);
		
		String branch = getRequest().getParameter("branchcode");
		String binno = getRequest().getParameter("cardtype");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		String ordertype_flag  = getRequest().getParameter("order_flag");
		System.out.println("ORdertype is----> "+ordertype_flag);
		System.out.println("branch==> "+branch+"   binno==>"+binno+"  fromdate===> "+fromdate+"  todate===>"+todate);
		String dateflag ="ORDERED_DATE";
		String inst_id=comInstId();
		List authorizeorderlist = null;
		
		
		System.out.println("session_act " + session_act);
		
		String condition1 ="";
		try {
			/*if(session_act.equals("M")){
				
				condition1 =" AND ORDER_STATUS='01' AND MKCK_STATUS='M'";
			}
			else{
				condition1 =" AND ORDER_STATUS='01' AND MKCK_STATUS in ('P','M')";
			}*/
			condition1 =" AND ORDER_STATUS='01' "; //AND MKCK_STATUS in ('D','M')
			
			if( binno.equals("ALL")){
				binno="";
			}
			
			String condtion = condition1+commondesc.filterCondition(binno, branch, fromdate, todate, dateflag);
			System.out.println("condtion==>"+condtion);
			authorizeorderlist = commondesc.getPersonalOrderList(inst_id,condtion, jdbctemplate );
			System.out.println("authorizeorderlist===> "+authorizeorderlist);
			if(!(authorizeorderlist.isEmpty())){
				setPerscardgenlist(authorizeorderlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg","");
				
			}else
			{
				setPerscardgenlist(authorizeorderlist);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg"," No Orders Available for Edit");
				return editpersonalOrderhome() ;
				
			}
		} catch (Exception e) {
			System.out.println("Exception--->"+e);
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg","Error While Fetching The Orders To Card Order ,ERROR:"+e.getMessage());
			e.printStackTrace();
			
		}
		return "personalcardeditorders";
	}
	
	
	
	
	private List customerinformationlist;
	public List getCustomerinformationlist() {
		return customerinformationlist;
	}


	public void setCustomerinformationlist(List customerinformationlist) {
		this.customerinformationlist = customerinformationlist;
	}


	private char custinformationstatus;
	
	public char getCustinformationstatus() {
		return custinformationstatus;
	}


	public void setCustinformationstatus(char custinformationstatus) {
		this.custinformationstatus = custinformationstatus;
	}
	
	
	private String refname;
	public String getRefname() {
		return refname;
	}

	public void setRefname(String refname) {
		this.refname = refname;
	}

	public char chregistration;
	
	public char getChregistration() {
		return chregistration;
	}


	public void setChregistration(char chregistration) {
		this.chregistration = chregistration;
	}

	
	public String getMakerCheckerValueForOrder(String instid, String order_refnum, JdbcTemplate jdbctemplate ){
		String mkckvalue = null;
		try {
			
			/*String mkckvalueqry = "SELECT MKCK_STATUS FROM PERS_CARD_ORDER WHERE INST_ID='"+instid+"' "
			+ "AND ORDER_REF_NO='"+order_refnum+"'";
			mkckvalue = (String)jdbctemplate.queryForObject(mkckvalueqry, String.class);*/
			
			//by gowtham
			String mkckvalueqry = "SELECT MKCK_STATUS FROM PERS_CARD_ORDER WHERE INST_ID=?  "
		+ "AND ORDER_REF_NO=?";
		mkckvalue = (String)jdbctemplate.queryForObject(mkckvalueqry,new Object[]{instid,order_refnum}, String.class);
			
		} catch (DataAccessException e) {}
		return mkckvalue;
	}

	public String personalCardetails()
	{
	
    		System.out.println("Order Edit ...got Refnum : "+getRefname());
			HttpSession session = getRequest().getSession();
			//String orderref = getRequest().getParameter("orderrefnum");
			String usertype = comuserType();
			System.out.println("  orderref   ===> "+getRefname());
			String inst_id =comInstId();
			
			try {
				
				String existmkckvalue = this.getMakerCheckerValueForOrder(inst_id, getRefname(), jdbctemplate);
				if( existmkckvalue!= null ){
					if( existmkckvalue.equals("P")){
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg","Could not edit this order. Already Authorized");
						System.out.println("Could not edit this order. Already Authorized");
						return this.editpersonalOrderhome();
					}
				}
				
				if (usertype.equals("INSTADMIN")) {
					System.out.println("Branch list start");
					List br_list = commondesc.generateBranchList(inst_id, jdbctemplate);
					if(!(br_list.isEmpty())){
						setBranch_list(br_list);
						session.setAttribute("curerr", "S");
						session.setAttribute("curmsg","");
						System.out.println("Branch list is not empty");
					}
					else{
						setBranch_list(br_list);
						session.setAttribute("curerr", "E");
						session.setAttribute("curmsg"," No Branch Details Found ");
						System.out.println("Branch List is empty ");
						return "personalproducts";
					}
					
				}
				List pers_prodlist=commondesc.getProductList(inst_id, jdbctemplate, session);
				if (!(pers_prodlist.isEmpty())){
					setPersonalproductlist(pers_prodlist);
					session.setAttribute("curerr", "S");
					session.setAttribute("curmsg","");
					System.out.println("Product List is ===> "+pers_prodlist);
				} else{
					System.out.println("No Product Details Found ");
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg"," No Product Details Found ");
				}
				String cust_regreq = commondesc.checkCustomerregistrationconfig(inst_id,getRefname(),jdbctemplate);
				System.out.println("cust_regreq==> "+cust_regreq);
				if(cust_regreq.equals("N"))
				{
					int cin_exsist = commondesc.checkCustomerdetailsexsist(inst_id,getRefname(),jdbctemplate);
					if(cin_exsist == 1)
					{
						setChregistration('Y');
						List customerinfo=null;
						customerinfo = commondesc.geteditdeleteOrderdetails(inst_id, getRefname(),jdbctemplate);
						System.out.println(" customerinfo "+customerinfo);
						if(!(customerinfo.isEmpty())){
							setCustomerinformationlist(customerinfo);
							setNationlist(commondesc.gettingNations(jdbctemplate));
							setDocumenttypelist(commondesc.gettingDocumnettype(inst_id, jdbctemplate));
							//setCustinformationstatus('Y');
							session.setAttribute("curerr", "S");
							session.setAttribute("curmsg", "");
						}
						return "personalcardetails";
					}
					else
					{
						List orderinfo = commondesc.getPersonalorderdetails(inst_id,getRefname(),jdbctemplate);
						setChregistration('N');
						setCustomerinformationlist(orderinfo);
						return "personalcardetails";
					}
				}
				else
				{
					
					int cin_exsist = commondesc.checkCustomerdetailsexsist(inst_id,getRefname(),jdbctemplate);
					if(cin_exsist == 1)
					{
						setChregistration('Y');
						List customerinfo=null;
						customerinfo = commondesc.geteditdeleteOrderdetails(inst_id, getRefname(),jdbctemplate);
						System.out.println(" customerinfo "+customerinfo);
						if(!(customerinfo.isEmpty())){
							setCustomerinformationlist(customerinfo);
							setNationlist(commondesc.gettingNations(jdbctemplate));
							//setCustinformationstatus('Y');
							setDocumenttypelist(commondesc.gettingDocumnettype(inst_id, jdbctemplate));
							session.setAttribute("curerr", "S");
							session.setAttribute("curmsg", "");
						}
					}
					else
					{
						List orderinfo = commondesc.getPersonalorderdetails(inst_id,getRefname(),jdbctemplate);
						setChregistration('N');
						setCustomerinformationlist(orderinfo);
						return "personalcardetails";
					}
					

					
				}
				
			} catch (Exception e) {
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "Error While Getting the Customer Details "+e.getMessage());
			}
			
		return "personalcardetails";
	}
	public String updatepersonalorder() 
	{
		trace("Edit card order....");
		String cin= getRequest().getParameter("cin");
		trace("Got the customer id ... " + cin);
		 
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("UPDORDER", txManager);
		String cust_reg_req = getRequest().getParameter("custreg_req");
		trace("Customer reg req : " + cust_reg_req );
		
		//added by gowtham_240719
				String  ip=(String) session.getAttribute("REMOTE_IP");
				int subprod_id=0;
		
		String instid = comInstId();
		String userid = comUserId();
		String temp = act;
		
		session.setAttribute("CARDORDER_EDIT", act);
		String session_act = (String) session.getAttribute("CARDOREDR_EDIT");
		 
		try 
		{
			String product_code = getRequest().getParameter("cardtype");
			String subpord_id = getRequest().getParameter("subproductlist");
			String bin = commondesc.getBin(instid, product_code, jdbctemplate);
			String cardtype_id = commondesc.getCardType(instid, product_code, jdbctemplate);
			String orderef =getRequest().getParameter("orderno");
			String time_qury = "(select trim(substr(to_char(sysdate,'dd-MON-yy hh24:mi:ss'),10,10)) from dual)";
			if(cust_reg_req.equals("Y"))
			{
				String branchcode = getRequest().getParameter("branchcode");
				String First_name=(getRequest().getParameter("fname"));
				String Middle_name=(getRequest().getParameter("mname"));
				String Last_name=(getRequest().getParameter("lname"));
				String Father_name=(getRequest().getParameter("f_name"));
				String Mother_name=(getRequest().getParameter("m_name"));
				String Married=(getRequest().getParameter("mstatus"));
				String Spouse_name=(getRequest().getParameter("spname"));
				String Sex=(getRequest().getParameter("gender"));
				String BDay=(getRequest().getParameter("dob"));
				String Nationality=(getRequest().getParameter("nations"));
				String E_mail=(getRequest().getParameter("email"));
				String Mobile_No=(getRequest().getParameter("mobile"));
				String Phone_No=(getRequest().getParameter("phone"));
				String Occupation=(getRequest().getParameter("work"));
				String Identity_No=(getRequest().getParameter("idno"));
				String Identity_document=(getRequest().getParameter("iddoc"));
				String app_date = (getRequest().getParameter("appdate"));
				String app_no=(getRequest().getParameter("appno"));
				String postal_address1 = (getRequest().getParameter("paddress1"));
				String postal_address2 = (getRequest().getParameter("paddress2"));
				String postal_address3 = (getRequest().getParameter("paddress3"));
				String postal_address4 = (getRequest().getParameter("paddress4"));
				String Samepostal_address = (getRequest().getParameter("same"));
				String resident_address1 = (getRequest().getParameter("raddress1"));
				String resident_address2 = (getRequest().getParameter("raddress2"));
				String resident_address3 = (getRequest().getParameter("raddress3"));
				String resident_address4 = (getRequest().getParameter("raddress4"));
				String noofcard = getRequest().getParameter("Count");
				String cardtype = getRequest().getParameter("cardtype");
				//String subproductlist =getRequest().getParameter("subproductlist");
				String embossingname= getRequest().getParameter("emposs");
				String encodingname = getRequest().getParameter("encode");
				String remarks = "";//getRequest().getParameter("remark");
				System.out.println("orderef " + orderef);
				int update_cusinfo = 0;
				int update_cardorder = 0;
				String subset_info = "";
				String cust_info="";
				
				String uploaduupdqry = "";
				 
				
				if(session_act.equals("M"))
				{
					subset_info = "MAKER_DATE=(sysdate),MAKER_TIME="+time_qury+",MAKER_ID='"+userid+"'";
					cust_info = "MAKER_DATE=(sysdate),MAKER_ID='"+userid+"'";
				}else if(session_act.equals("D"))
				{
					subset_info = "MAKER_DATE=(sysdate),MAKER_TIME="+time_qury+",MAKER_ID='"+userid+"',CHECKER_DATE=(sysdate),CHECKER_TIME="+time_qury+",CHECKER_ID='"+userid+"'";
					cust_info = "MAKER_DATE=(sysdate),MAKER_ID='"+userid+"',CHECKER_DATE=(sysdate),CHECKER_ID='"+userid+"'";
				}
				
				String photourl = "", signatureurl="", idproofurl="";
				String newphotoname = cin+"_"+commondesc.getDateTimeStamp()+".jpg";
				String newsignaturename = cin+"_"+commondesc.getDateTimeStamp()+".jpg";
				String newidproofname = cin+"_"+commondesc.getDateTimeStamp()+".jpg";  
				
				
				
				
				
				if( uploadphoto != null  ){ 
					trace( "uploaded photo size " + uploadphoto.length() ); 
					photourl = newphotoname; 
					uploaduupdqry += " , PHOTO_URL='"+photourl+"' ";
				} 
				
				if( uploadsignature != null  ){ 
					trace( "uploaded signature image size : " + uploadsignature.length() ); 
					signatureurl = newsignaturename;
					uploaduupdqry += " , SIGNATURE_URL='"+signatureurl+"' ";
				} 
				
				if( uploadidproof != null  ){ 
					trace( "uploaded id proof size : " + uploadidproof.length() ); 
					idproofurl = newidproofname; 
					uploaduupdqry += " , IDPROOF_URL='"+idproofurl+"' ";
				} 
				trace("Updating personal card order ....");
				System.out.println("MKCK VALUE IS : " + session_act);
				
				/*String update_order = "UPDATE PERS_CARD_ORDER SET CARD_TYPE_ID='"+cardtype_id+"',"
				+ "MKCK_STATUS='"+session_act+"',SUB_PROD_ID='"+subpord_id+"',"
				+ "PRODUCT_CODE='"+product_code+"',CARD_QUANTITY='"+noofcard+"',"
				+ "EMBOSSING_NAME='"+embossingname+"',REMARKS='"+remarks+"',ENCODE_DATA='"+encodingname+"',"
				+ ""+subset_info+",BRANCH_CODE='"+branchcode+"',BIN='"+bin+"',"
				+ "APP_DATE=to_date('"+app_date+"','dd-mm-yyyy'),APP_NO='"+app_no+"' "
				+ " where INST_ID='"+instid+"' and ORDER_REF_NO='"+orderef+"'";
				enctrace("update_order : "+update_order);
				update_cardorder = commondesc.executeTransaction(update_order,jdbctemplate );*/
				
				//by gowtham
				String update_order = "UPDATE PERS_CARD_ORDER SET CARD_TYPE_ID=?,"
						+ "MKCK_STATUS=?,SUB_PROD_ID=?,"
						+ "PRODUCT_CODE=?,CARD_QUANTITY=?,"
						+ "EMBOSSING_NAME=?,REMARKS=?,ENCODE_DATA=?,"
						+ ""+subset_info+",BRANCH_CODE=?,BIN=?,"
						+ "APP_DATE=to_date(?,'dd-mm-yyyy'),APP_NO=? "
						+ " where INST_ID=? and ORDER_REF_NO=?";
						enctrace("update_order : "+update_order);
						
				update_cardorder =jdbctemplate.update(update_order,new Object[]{cardtype_id,session_act,subprod_id,product_code,
				noofcard,embossingname,remarks,encodingname,branchcode,bin,app_date,app_no,instid,orderef});
				
				trace("Got : " + update_cardorder );
				
				trace("Updating customer process info...");
				
				String upadte_cusinfo = "update IFP_CUSTINFO_PROCESS SET FNAME='"+First_name+"',MNAME='"+Middle_name+"',"
				+ "LNAME='"+Last_name+"',FATHER_NAME='"+Father_name+"',MOTHER_NAME='"+Mother_name+"',"
				+ "MARITAL_STATUS='"+Married+"',SPOUSE_NAME='"+Spouse_name+"',GENDER='"+Sex+"',"
				+ "DOB=to_date('"+BDay+"','DD-MM-YYYY'),NATIONALITY='"+Nationality+"',EMAIL_ADDRESS='"+E_mail+"',"
				+ "MOBILE_NO='"+Mobile_No+"',PHONE_NO='"+Phone_No+"',OCCUPATION='"+Occupation+"',"
				+ "ID_NUMBER='"+Identity_No+"',ID_DOCUMENT='"+Identity_document+"',POST_ADDR1='"+postal_address1+"',"
				+ "POST_ADDR2='"+postal_address2+"',POST_ADDR3='"+postal_address3+"',POST_ADDR4='"+postal_address4+"',"
				+ "RES_ADDR1='"+resident_address1+"',RES_ADDR2='"+resident_address2+"',RES_ADDR3='"+resident_address3+"',"
				+ "RES_ADDR4='"+resident_address4+"',"+cust_info+" "+ uploaduupdqry +" where INST_ID='"+instid+"' and"
				+ " CIN='"+cin+"'";
				enctrace("upadte_cusinfo : " + upadte_cusinfo );
				update_cusinfo = commondesc.executeTransaction(upadte_cusinfo,jdbctemplate );

				
				trace("Got update_cusinfo : " + update_cusinfo);
				
				Properties prop = commondesc.getCommonDescProperty();
				String photodir = prop.getProperty("PHOTOFILELOCATION"); 
				
				File photodirfile = new File(photodir);
				if( !photodirfile.exists() ){
					photodirfile.mkdir();
				}
				
				String signaturedir = prop.getProperty("SIGNATUREFILELOCATION");
				File signaturedirfile = new File( signaturedir );
				if( !signaturedirfile.exists() ){
					signaturedirfile.mkdir();
				}
				
				String idproofdir = prop.getProperty("IDPROOFLOCATION");
				File idproofdirfile = new File(photodir);
				if( !idproofdirfile.exists() ){
					idproofdirfile.mkdir();
				}
				
				if( uploadphoto != null  ){
					File fileToCreate = new File(photodirfile, newphotoname); 
					FileUtils.copyFile(uploadphoto, fileToCreate);  
				} 
				
				if( uploadsignature != null  ){
					File fileToCreate = new File(signaturedirfile, newsignaturename); 
					FileUtils.copyFile(uploadsignature, fileToCreate); 
					signatureurl = newsignaturename; 
				} 
				
				if( uploadidproof != null  ){
					File fileToCreate = new File(idproofdir, newidproofname); 
					FileUtils.copyFile(uploadidproof, fileToCreate); 
					idproofurl = newidproofname; 
				} 
				
				if(update_cardorder == 1 && update_cusinfo == 1){
					System.out.println("UpDATED");
					//commondesc.commitTxn(jdbctemplate);
					txManager.commit(transact.status);
					session.setAttribute("preverr", "S");
					session.setAttribute("prevmsg", "Successfully Updated Card Order and Customer Information");
					trace("Successfully Updated Card Order and Customer Information");
					
					/*************AUDIT BLOCK**************/ 
					try{  
						
						//added by gowtham_240719
						trace("ip address======>  "+ip);
						auditbean.setIpAdress(ip);

						
						auditbean.setActmsg("Customer Details Updated for the application id [ "+orderef+" ]");
						auditbean.setUsercode(userid);
						auditbean.setAuditactcode("0101"); 
						
						//added by gowtham_010819
						String pcode=null;
						auditbean.setProduct(pcode);
						auditbean.setSubproduct(subpord_id);
						auditbean.setApplicationid(orderef);
						auditbean.setRemarks(remarks);
						commondesc.insertAuditTrail(instid, userid, auditbean, jdbctemplate, txManager);
					 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
					 /*************AUDIT BLOCK**************/ 
					
				}
				else{
					System.out.println("Roll backed UpDATED");
					//commondesc.rollbackTxn(jdbctemplate);
					txManager.rollback(transact.status);
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "Error while Updating Card Order and Cus Info");
					trace("Error while Updating Card Order and Cus Info");
				}
				 
				setAct((String)session.getAttribute("CARDOREDR_EDIT"));
				return editpersonalOrderhome();
			}
			else if(cust_reg_req.equals("N")) 
			{
				int update_cardorder = 0;
				String branchcode = getRequest().getParameter("branchcode");
				//String cardtype = getRequest().getParameter("cardtype");
				//String subproductlist = getRequest().getParameter("subproductlist");
				String noofcards = getRequest().getParameter("Count");
				String embossingname = getRequest().getParameter("emposs");
				String encodingname = getRequest().getParameter("encode");
				String appdate = getRequest().getParameter("appdate");
				String appnumber = getRequest().getParameter("appno");
				String remarks= getRequest().getParameter("remark");
				System.out.println("orderef " + orderef);
				System.out.println("branch==> "+branchcode+"   Product_code==>"+product_code+"  subproductlist===> "+subpord_id+"  noofcards===>"+noofcards+ "   embossingname==> "+embossingname+    "embossingname==> "+embossingname+  "appdate==>"+appdate+ "appnumber==>"+appnumber+ "remarks==> "+remarks+" Card TYpe ID==> "+cardtype_id);
				String subset_info = "";
				//String upadte_cusinfo = "UPDATE PERS_CARD_ORDER set BRANCH_CODE='"+branchcode+"',BIN='"+cardtype+"',SUB_PROD_ID='"+subproductlist+"',CARD_QUANTITY='"+noofcards+"', EMBOSSING_NAME='"+embossingname+"', ENCODE_DATA='"+encodingname+"',APP_DATE=to_date('"+appdate+"','dd-mm-yyyy'),APP_NO='"+appnumber+"',REMARKS='"+remarks+"' where INST_ID='"+inst_id+"' and ORDER_REF_NO='"+orderef+"'";
				if(session_act.equals("M"))
				{
					subset_info = "MAKER_DATE=(sysdate),MAKER_TIME="+time_qury+",MAKER_ID='"+userid+"'";
				}else if(session_act.equals("D"))
				{
					subset_info = "MAKER_DATE=(sysdate),MAKER_TIME="+time_qury+",MAKER_ID='"+userid+"',CHECKER_DATE=(sysdate),CHECKER_TIME="+time_qury+",CHECKER_ID='"+userid+"'";
				}
				System.out.println("MKCK VALUE IS : " + session_act);
				String update_order = "UPDATE PERS_CARD_ORDER SET CARD_TYPE_ID='"+cardtype_id+"',SUB_PROD_ID='"+subpord_id+"',PRODUCT_CODE='"+product_code+"',CARD_QUANTITY='"+noofcards+"',MKCK_STATUS='"+session_act+"',EMBOSSING_NAME='"+embossingname+"',REMARKS='"+remarks+"',ENCODE_DATA='"+encodingname+"',"+subset_info+",BRANCH_CODE='"+branchcode+"',BIN='"+bin+"',APP_DATE=to_date('"+appdate+"','dd-mm-yyyy'),APP_NO='"+appnumber+"' where INST_ID='"+instid+"' and ORDER_REF_NO='"+orderef+"'";
				
				System.out.println("update_order QURY====> "+update_order);
				update_cardorder = commondesc.executeTransaction(update_order,jdbctemplate);
				System.out.println("update_cardorder ==> "+update_cardorder);	
				if( update_cardorder == 1 )
				{
					//commondesc.commitTxn(jdbctemplate);
					txManager.commit(transact.status);
					session.setAttribute("preverr","S");
					session.setAttribute("prevmsg","Order Edited Successfully");
					System.out.println("THis Txn Got Commited ");
				}
				else
				{
					//commondesc.rollbackTxn(jdbctemplate);
					txManager.rollback(transact.status);
					session.setAttribute("preverr","E");
					session.setAttribute("prevmsg","Order Edit Failed");
					System.out.println("THis Txn Got RolledBack ");
				}
			}
			System.out.println("CARDOREDR_EDIT==> "+(String)session.getAttribute("CARDOREDR_EDIT"));
			setAct((String)session.getAttribute("CARDOREDR_EDIT"));
			return editpersonalOrderhome();
		
		}
		catch (Exception e) 
		{

			//commondesc.rollbackTxn(jdbctemplate);
			txManager.rollback(transact.status);
			session.setAttribute("preverr","E");
			session.setAttribute("prevmsg","Exception : Could not edit the card order....");
			trace("Exception: card order edit failed..."+ e.getMessage());
			e.printStackTrace();
		}
		setAct((String)session.getAttribute("CARDOREDR_EDIT"));
		return editpersonalOrderhome();
	}

	public String kyCustomereditcardorder() {
		System.out.println("editpersonalproducts");
		List pers_prodlist,br_list=null;
		String inst_id =comInstId();
		String usertype = comuserType();
		HttpSession session = getRequest().getSession();
		
		try {
			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = commondesc.generateBranchList(inst_id, jdbctemplate);
				if(!(br_list.isEmpty())){
					setBranchlist(br_list);
					session.setAttribute("curerr", "S");
					session.setAttribute("curmsg","");
					System.out.println("Branch list is not empty");
				}
				else{
					setBranchlist(br_list);
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg"," No Branch Details Found ");
					System.out.println("Branch List is empty ");
					return "editpersonalproducts";
				}
			}
			pers_prodlist=commondesc.getProductList(inst_id, jdbctemplate, session);
			if (!(pers_prodlist.isEmpty())){
				setPersonalproductlist(pers_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg","");
				System.out.println("Product List is ===> "+pers_prodlist);
			} else{
				System.out.println("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg"," No Product Details Found ");
			}

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg"," Error While Fetching The Product Details  "+ e.getMessage());
		}
		System.out.println("return personalproducts");

		return "editpersonalproducts";
		
	}
	
	
	public String supplimentCardDetails() {
		HttpSession session = getRequest().getSession();
		
		IfpTransObj transact = commondesc.myTranObject("SAVEKYCORDER", txManager);
		String card_branchcode = getRequest().getParameter("branchcode").trim();
		String cardtype=(getRequest().getParameter("cardtype")).trim();
		String subproduct = (getRequest().getParameter("subproductlist")).trim();
		String product_count = (getRequest().getParameter("Count")).trim();
		String embossName = (getRequest().getParameter("emposs")).trim();
		String encoding = (getRequest().getParameter("encode")).trim(); 
		personalbean.parentcard =  getRequest().getParameter("existingcardno"); 
		String instid = comInstId();
		String action = (String)session.getAttribute("CARDOREDR_ACT");
		trace("Action Typr =====> "+action+"Instituion id ===> "+instid);
		
		trace("card_branchcode ==> "+card_branchcode+"\ncardtype ==> "+cardtype+"\nsubproduct ==> "+subproduct+	"\nproduct_count ==> "+product_count+"\nembossName ==> "+embossName+"\nencoding ==> "+encoding+" existingcardno ==> "+personalbean.parentcard);
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date date = new Date();
		String todaydate = sdf.format(date);
		session.setAttribute("todaydate",todaydate);
		
		try	{
			
			/*int validcardno = commondesc.checkCardNumberValid(instid, personalbean.parentcard, jdbctemplate); 
			trace("Checking existing card number valid or not ...got : "+validcardno);
			if(validcardno == 0 || validcardno == -1) { 
				addActionError("Invalid Existing Card  Number ");
				setAct((String)session.getAttribute("CARDOREDR_ACT"));
				return kyCustomercardorder();
			}*/
			
			int allowedaddoncardcount =4;
			int checkeligible = commondesc.getSupplimentEligiblity(instid,personalbean.parentcard, allowedaddoncardcount, jdbctemplate);
			trace("Validating Supplimentary card eligibility...got : " + checkeligible);
			if( checkeligible != 0 ){
				if( checkeligible == -1 ){
					addActionError("Unable to continue the process"); 
					return kyCustomercardorder();
				}
				if( checkeligible == 1 ){
					addActionError("Invalid Existing Card  Number "); 
					return kyCustomercardorder();
				}else if( checkeligible == 2 ){
					addActionError("Given Card Number is not primary card. Could not provide additional cards"); 
					return kyCustomercardorder();
				}else if( checkeligible == 3 ){
					addActionError("Allowed "+allowedaddoncardcount+" Supplimentary Cards Per Primary Cards. Limit Already Reached"); 
					return kyCustomercardorder();
				}
			} 
			String custnum = commondesc.getCustomerIdByCardNumber(instid, personalbean.parentcard, jdbctemplate);
			if( custnum == null ){
				addActionError("Customer Details Not Found");
				return kyCustomercardorder();
			} 
			String cust_regist_req = commondesc.personalOrdercustomerregistrationcheck(instid,cardtype,subproduct,jdbctemplate);  
			int cust_exist = commondesc.checkCustomerexist(instid,custnum,"IFP_CUSTINFO_PRODUCTION",jdbctemplate);
			System.out.println("Customer Registraion Required");
			System.out.println("The Customer Num Count ====> "+cust_exist);
			if(cust_exist == 0 || cust_exist == -1) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Invalid Customer Number ");
				setAct((String)session.getAttribute("CARDOREDR_ACT"));
				return kyCustomercardorder();
			}
			
			List cutomerdetails = commondesc.getKYCdetails(instid,custnum,jdbctemplate);
			if(cutomerdetails == null) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "No Customer Details Found ");
				setAct((String)session.getAttribute("CARDOREDR_ACT"));
				return kyCustomercardorder();
			}
			
			session.setAttribute("CARD_BRANCH",card_branchcode);
			session.setAttribute("Ordered_Card",cardtype);
			session.setAttribute("Ordered_SubProduct",subproduct);
			session.setAttribute("Ordered__Count",product_count);
			session.setAttribute("Embossname",embossName);
			session.setAttribute("Encodename",encoding); 
			setNationlist(commondesc.gettingNations(jdbctemplate)); 
			setDocumenttypelist(commondesc.gettingDocumnettype(instid,jdbctemplate));
			session.setAttribute("getcustomedetailsErrorStatus", "S");
			
			System.out.println("KYC Customer Detaik ===> "+cutomerdetails); 
			session.setAttribute("KYC_BRCODE",card_branchcode);
			session.setAttribute("KYC_CARDTYPE",cardtype);
			session.setAttribute("KYC_SUBPROD",subproduct);
			session.setAttribute("KYC_ORDERCOUNT",product_count);
			session.setAttribute("KYC_EMBOSS",embossName);
			session.setAttribute("KYC_ENCODE",encoding);
			session.setAttribute("KYC_CIN",custnum);
			//session.setAttribute("APPDATE",appdate);
			//session.setAttribute("APPNUM",appno);
			setKyccustdetails(cutomerdetails);
			setNationlist(commondesc.gettingNations(jdbctemplate));
			//setQuestionlist(commondesc.gettingSequrityQuestion(jdbctemplate));
			setDocumenttypelist(commondesc.gettingDocumnettype(instid, jdbctemplate));
			
			List customerdetails = commondesc.getCustomerdetailsByCIN(instid, custnum, jdbctemplate);
			trace("Getting customer details for the customer id [ "+custnum+" ] ");
			if( customerdetails.isEmpty()  ){
				addActionError("Unable to get customer details.");
				return this.kyCustomercardorder();
			}
		 
			personalbean.cardissuetypecode = "$SUPLIMENT";
			personalbean.cardissuetypedesc = "Supplimentary Card ";
			Iterator itr = customerdetails.iterator();
			while ( itr.hasNext() ){
				Map mp = (Map)itr.next();
				personalbean.firstname = (String)mp.get("FNAME");
				personalbean.middlename= (String)mp.get("MNAME");
				personalbean.lastname = (String)mp.get("LNAME"); 
				personalbean.fathername = (String)mp.get("FATHER_NAME");
				personalbean.mothername = (String)mp.get("MOTHER_NAME");
				personalbean.married = (String)mp.get("MARITAL_STATUS1");
				personalbean.sex = (String)mp.get("GENDER1");
				personalbean.bday = (String)mp.get("DOB");
				personalbean.nationality = (String)mp.get("NATIONALITY");
				personalbean.email = (String)mp.get("EMAIL_ADDRESS");
				personalbean.mobileno = (String)mp.get("MOBILE_NO");
				personalbean.phoneno = (String)mp.get("PHONE_NO");
				personalbean.occupation = (String)mp.get("OCCUPATION");
				personalbean.identityno = (String)mp.get("ID_NUMBER");
				personalbean.postaladdress1 = (String)mp.get("PONE");
				personalbean.postaladdress2 = (String)mp.get("PTWO");
				personalbean.postaladdress3 = (String)mp.get("PTHREE");
				personalbean.postaladdress4 = (String)mp.get("PFOUR");
				personalbean.residentaddress1 = (String)mp.get("RONE");
				personalbean.residentaddress2 = (String)mp.get("RTWO");
				personalbean.residentaddress3 = (String)mp.get("RTHREE");
				personalbean.residentaddress4 = (String)mp.get("RFOUR"); 
				
				personalbean.postaladdress =personalbean.postaladdress1;
				personalbean.residentaladdress =personalbean.residentaddress1;
			}
		 
		}
		catch (Exception e)  {
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Error "+e.getMessage());
			setAct((String)session.getAttribute("CARDOREDR_ACT"));
			System.out.println("Error is --> "+e.getMessage());
			return kyCustomercardorder();
		}
		return "customerdetails";
	}
	
	
	public void getCardProductDetails() throws Exception {
		String cardno = getRequest().getParameter("cardno");
		String instid = comInstId();
		JSONObject jsonresult = new JSONObject(); 
		try{
			jsonresult = this.getCardProductDetails(instid, cardno, jdbctemplate);
		}catch( Exception  e) {
			jsonresult.put("RESPCODE", 1);
			jsonresult.put("RESPREASON", "Unable to Process");
			e.printStackTrace();
		}
		getResponse().getWriter().write(jsonresult.toString());
	}
	
	public JSONObject getCardProductDetails( String instid, String cardno, JdbcTemplate jdbctemplate ) throws Exception {
		JSONObject jsoncard = new JSONObject(); 
		
		/*String carddetailsqry = "SELECT PRODUCT_CODE, SUB_PROD_ID, BRANCH_CODE FROM  CARD_PRODUCTION WHERE"
		+ " INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
		enctrace("carddetailsqry :" + carddetailsqry );
		List carddata = jdbctemplate.queryForList(carddetailsqry);*/
		
		//by gowtham
		String carddetailsqry = "SELECT PRODUCT_CODE, SUB_PROD_ID, BRANCH_CODE FROM  CARD_PRODUCTION WHERE"
				+ " INST_ID=? AND CARD_NO=? ";
				enctrace("carddetailsqry :" + carddetailsqry );
				List carddata = jdbctemplate.queryForList(carddetailsqry,new Object[]{instid,cardno});
		
		if( carddata.isEmpty() ){
			jsoncard.put("RESPCODE", 1);
			jsoncard.put("RESPREASON", "Invalid Card Number. No Records Found"); 
		}else{
			ListIterator itr = carddata.listIterator();
			while ( itr.hasNext() ){
				Map mp = (Map)itr.next();
				String productcode = (String)mp.get("PRODUCT_CODE");
				String subproduct  = (String)mp.get("SUB_PROD_ID");
				String branchcode  = (String)mp.get("BRANCH_CODE");
				
				String productdesc = commondesc.getProductdesc(instid, productcode, jdbctemplate);
				trace("Getting the product description for the productcode [ "+productcode+" ] ....got : " + productdesc );
				if( productdesc == null ){
					jsoncard.put("RESPCODE", 1);
					jsoncard.put("RESPREASON", "This Card Number Exist on the product no logner exist now. Could not issue the card.");
					return jsoncard;
				}
				
				String subproductdesc = commondesc.getSubProductdesc(instid, subproduct, jdbctemplate);
				trace("Getting the product description for the productcode [ "+subproduct+" ] ....got : " + subproductdesc );
				if( subproductdesc == null ){
					jsoncard.put("RESPCODE", 1);
					jsoncard.put("RESPREASON", "This Card Number Exist on the sub-product no logner exist now. Could not issue the card.");
					return jsoncard;
				}
				
				String branchdesc = commondesc.getBranchDesc(instid, branchcode, jdbctemplate);
				trace("Getting the product description for the productcode [ "+branchcode+" ] ....got : " + branchdesc );
				if( branchdesc == null ){
					jsoncard.put("RESPCODE", 1);
					jsoncard.put("RESPREASON", "This Card Number Exist on the branch no logner exist now. Could not issue the card.");
					return jsoncard;
				}
				
				
				String productopt = "<option value='"+productcode+"'>"+productdesc+"</option>";
				String subproductopt = "<option value='"+subproduct+"'>"+subproductdesc+"</option>";
				String branchopt = "<option value='"+branchcode+"'>"+branchdesc+"</option>";
				
				jsoncard.put("RESPCODE", 0);
				jsoncard.put("PRODUCTOPTION", productopt);
				jsoncard.put("SUBPRODUCTOPTION", subproductopt);
				jsoncard.put("BRANCHOPTION", branchopt);   
			} 
		}
		return jsoncard; 
	}
	public static String GenerateRandom4Digit() 
	{
	    Random rand = new Random();
	    int x = (int)(rand.nextDouble()*10000L);
	    String s = String.format("%04d", x);
	    return s;
	}
	
	public List GetOrderNumber( String instid, JdbcTemplate jdbctemplate  ) throws Exception {
		
		/*String qry = "SELECT CIN_NO, ORDER_REFNO FROM IFD_SEQUENCE_MASTER WHERE INST_ID='"+instid+"' ";
		List listoforder = jdbctemplate.queryForList(qry);*/
		
		///by gowtham
		String qry = "SELECT CIN_NO, ORDER_REFNO FROM IFD_SEQUENCE_MASTER WHERE INST_ID=? ";
		List listoforder = jdbctemplate.queryForList(qry,new Object[]{instid});
		
		return listoforder;
	}

}//end class