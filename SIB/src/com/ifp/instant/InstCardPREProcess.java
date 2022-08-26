package com.ifp.instant;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.json.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.personalize.PreprocessAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.ExcelGenerator;
import com.ifp.util.IfpTransObj;
import com.ifp.util.TrackEncryption;

import connection.Dbcon;

/**
 * SRNP0004
 * @author CGSPL
 *
 */
public class InstCardPREProcess extends BaseAction 
{ 
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8376161637970676446L;
	private static String OS = System.getProperty("os.name").toLowerCase();
	private static Boolean  initmail = true; 
	private static  String parentid = "000";
	TrackEncryption EncDec = new TrackEncryption();
	CommonUtil comutil= new CommonUtil(); 
	CommonDesc commondesc = new CommonDesc();
	
	public CommonDesc getCommondesc() {
		return commondesc;
	}
	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

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
	public CommonUtil getComutil() {
		return comutil;
	}
	public void setComutil(CommonUtil comutil) {
		this.comutil = comutil;
	}
	
	String act;  
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

 
	public String comInstId(){
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	} 
	public String comUserCode(){
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("USERID"); 
		return instid;
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
	
	AuditBeans auditbean = new AuditBeans();
	
	public AuditBeans getAuditbean() {
		return auditbean;
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
	}
	
	public String preGenHome()  {
		trace("******* Instant pre generation begin *******\n");
		enctrace("******* Instant pre generation begin *******\n");
		String instid =  comInstId(); 
		
		HttpSession session = getRequest().getSession();
		List pers_prodlist=null,br_list=null;
		String inst_id =comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		String cardstatus="02";
		String mkrstatus="P";
		try {
			
			String act = getRequest().getParameter("act");
			if( act != null ){
				session.setAttribute("act", act);
			}
			
			System.out.println("Inst Id===>"+inst_id+"  Branch Code ===>"+branch);
			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = commondesc.InstgetBranchCodefmProcess(inst_id, cardstatus, mkrstatus, jdbctemplate);
				System.out.println("Branch list "+br_list);
				if(!(br_list.isEmpty())){
					setBranchlist(br_list);
					System.out.println("Branch list is not empty");
					//setCardgenstatus('Y');
				}
				else{
					addActionError("No Cards Waiting For Pre Generation ... ");
					System.out.println("Branch List is empty ");
					return "required_home";    
					
				}
			}
			pers_prodlist=commondesc.InstgetProductListBySelected(inst_id, cardstatus, mkrstatus, jdbctemplate);
			if (!(pers_prodlist.isEmpty())){
				setProdlist(pers_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg","");
				System.out.println("Product List is ===> "+pers_prodlist);
				//setCardgenstatus('Y');
			} else{
				System.out.println("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg"," No Product Details Found ");
				//setCardgenstatus('N');
			}
		
		
	 
		/*HttpSession session = getRequest().getSession(); 
		 
		try{
			int reqch= commondesc.reqCheck().requiredCheck(instid, session, jdbctemplate);
			if( reqch < 0 ){
				return "required_home";
			}
			
			
			String act = getRequest().getParameter("act");
			if( act != null ){
				session.setAttribute("act", act);
			}
			
			trace("Getting branchlist...");
			branchlist =  commondesc.generateBranchList(instid, jdbctemplate );
			setBranchlist(branchlist);
			
			trace("Getting product list...");
			prodlist = commondesc.getProductList( instid, jdbctemplate , session); 
			System.out.println( "john__" + prodlist );
			setProdlist(prodlist); */
			
			
			/*** MAIL BLOCK ****/
			System.out.println( "initmail--" + initmail +" parentid :  " + this.parentid );  
			if( initmail ){
				HttpServletRequest req = getRequest();
				String menuid = comutil.getUrlMenuId( req, jdbctemplate ); 
				if( !menuid.equals("NOREC")){
					System.out.println( "parentid--"+menuid); 
					this.parentid = menuid;
				}else{ 
					this.parentid = "000";
				}
				initmail = false;
			} 
			/*** MAIL BLOCK ****/
			
		}catch(Exception e ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception : could not continue the process..." );
			trace("Exception : could not continue the process..." + e.getMessage() );
			e.printStackTrace();
		}
		
		
		return "instpreorder_home"; 
	}
	
	public String viewPREList() throws Exception {
		System.out.println( "PRE view list....");
		
		HttpSession session = getRequest().getSession();
	 
		String instid =  comInstId(); 
		String card_status = "02";
		String mkckstatus = "P";
		String act = (String)session.getAttribute("act");
		String  datefld = "PIN_DATE"; 
		String branch = getRequest().getParameter("branchcode");
		String cardtype = getRequest().getParameter("cardtype");
		//String bin = commondesc.getBin(instid, cardtype, jdbctemplate);
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		
		try{

			String filtercond = commondesc.filterCondition(cardtype,branch,fromdate,todate,datefld);
			System.out.println( "filter condition " + filtercond );
			
			List waitingforcardpin = commondesc.waitingForInstCardProcess(instid, card_status,  mkckstatus, filtercond, jdbctemplate);
			System.out.println( "waitingforcardpin " + waitingforcardpin);
			if( waitingforcardpin.isEmpty() ){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " No records found " ); 
				return this.preGenHome();
			}
			
			setInstorderlist(waitingforcardpin); 
			
			if( waitingforcardpin.isEmpty() ){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " No records found " ); 
				return this.preGenHome();
			}
		}catch(Exception e ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception : could not continue the process..." );
			trace("Exception : could not continue the process..." + e.getMessage() );
			e.printStackTrace();
		} 
		return "instcardpre_list";   
	}
	
	
	public String genInstPRE() throws Exception {

		trace("******* instant preGenerationprocess *******\n");
		enctrace("******* instant preGenerationprocess  *******\n");

		String bin = null;						String track1 = null;					String track2 = null;				String authmsg = null;
		String dcardno = null;			String general_data = null;		String custdata = null;			String CDMK = null;
		String CDPK = null;				String mcardno = null;				String makerid = null;			String checkerid = null;
		String makerdate = null;		String checkerdate = null;		String mkckflag = null;		String ckdate = null;
		String cardnum = null;			String expiry_1 = null;				String enc_name = null;		String expiry_2 = null;
		String cvv1 = null;					String cvv2 = null;						String icvv = null;					String servicecode = null;
		String cardref_no = null;		String printdata = null;			String predata=null;				String prerecord=null;
		String ADDRESS1 = null;		String ADDRESS2 = null;			String ADDRESS3 = null;		String ADDRESS4 = null;
		String ADDRESS5 = null;		String PHONENO = null;
		
		
		String errormsg="";
		String encode_name="";
		String countrycode="";
		String cardcurrency="";
		
		StringBuilder sb=new StringBuilder("");

		int status = 0;
		int process_count = 0;
		int ordercnt = 0;
		Boolean presuc = true;
		PadssSecurity padsssec = null;
		List carddetails = null;
		List cvvdata = null;
		List addressdetails = null;

	 
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus txnstatus = txManager.getTransaction(def);

		HttpSession session = getRequest().getSession();
		String branchcode = getRequest().getParameter("branchcode");
		String[] order_refnum = getRequest().getParameterValues("instorderrefnum");
		String instid = comInstId();
		System.out.println("instid---->"+instid);
		String usercode = comUserCode();
	    trace("Total Orders Selected : " + order_refnum.length);
		
		 
		String productcode = getRequest().getParameter("product");
		trace("Getting binno" + productcode);
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
		List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);

		Properties props = getCommonDescProperty();
		String EDPK = props.getProperty("EDPK");
    	String  ip=(String) session.getAttribute("REMOTE_IP");


    	//added by gowtham_240221
    	int size = 0;
		int[] updateCounts = new int[size];
		int[] insertCounts = new int[size];
		int[] insertauditCounts = new int[size];
		Connection conn = null;
		Dbcon dbcon = new Dbcon(); 
		conn = dbcon.getDBConnection();
		PreparedStatement insertpstmt = null;
		PreparedStatement updatepstmt = null;
		PreparedStatement AUDITRANpstmt = null;
		
		int update_count=0,insert_count=0;
  	
		try {

			// String act = (String)session.getAttribute("act") ;
			String act = "M";
			System.out.println("ACT VALUE FOR PRE FILE GENERATION" + act);

			if (act.equals("M")) {
				System.out.println("act value " + act);
				makerid = usercode;
				mkckflag = "M";
				ckdate = commondesc.default_date_query;
				makerdate = "SYSDATE";
				authmsg = " and Waiting for PRE Authorization ........";
			} else { // D
				System.out.println("act value " + act);
				makerid = usercode;
				checkerid = makerid;
				mkckflag = "P";
				ckdate = "sysdate";
				makerdate = "SYSDATE";
			}

			String table = "INST_CARD_PROCESS";

			DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
			Date date = new Date();

			String userdesc = commondesc.getUserName(instid, usercode, jdbctemplate);

			String downproductname = productcode;
			String prename = downproductname + "_" + userdesc + "_" + dateFormat.format(date);

			trace("prename : " + prename);
			
			String preinst_qury = "INSERT INTO INST_PRE_DATA ( INST_ID,BIN, PRODUCT_CODE, CARD_NO,ORDER_REF_NO,BRANCH_CODE,GENERATED_DATE,PRE_NAME,TRACK_DATA,USER_CODE, "
					            + " EMB_NAME, ENC_NAME, TRACK1, TRACK2, CVV1, CVV2, ICVV, COUNTRY_CODE, CURRENCY_CODE,CURRENCY_EXPONENET, EXPDATE, APP_NAME, PAN_SEQNO, SERVICE_CODE,VALID_FROM, VALID_TO, CARD_REFNO, EMB_CARDNO,ADDRESS1,ADDRESS2,ADDRESS3,ADDRESS4,ADDRESS5,PHONENO,CIN,BRANCH_NAME,AUTH_CODE  ) VALUES "
					            + "(?,?,?,?,?,?, SYSDATE, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
							  insertpstmt = conn.prepareStatement(preinst_qury);
							  enctrace("update_qury : " + preinst_qury);
			
			String pre_upd_qury = "UPDATE INST_CARD_PROCESS SET  CARD_STATUS=?,PRE_DATE=sysdate,PRE_FILE=?,  MAKER_DATE= " + makerdate + ", MKCK_STATUS = ? "
					            + " WHERE INST_ID=? AND ORG_CHN=? ";
					           enctrace("update_qury : " + pre_upd_qury);
					           updatepstmt = conn.prepareStatement(pre_upd_qury);
					           
					           
			String Auditinsert="INSERT INTO AUDITRAN (INST_ID, BIN, PRODUCTCODE, SUBPRODUCT, CARDNO, USERCODE, ACTIONDATE, AUDITMSG, REMARKS , AUDITACTCODE, APPLICATIONID, PREFILE_NAME, APPTYPE,ACTIONTYPE,ACCNO,CUSTNAME,BRANCHCODE,CIN,CHECKERID,IP_ADDRESS ) VALUES "
							    + " (?,?,?,?, ?,?,SYSDATE,?, ?,?,?,?, ?,?,?,?,?,?,?,?)";
								enctrace("insert_qury : " + Auditinsert);
								AUDITRANpstmt = conn.prepareStatement(Auditinsert);

			for (int i = 0; i < order_refnum.length; i++) {
				
				padsssec=new PadssSecurity();
				String refnum = order_refnum[i].toString().trim();
				trace("File generation started Ref Num : " + refnum);
			    trace("Getting card list for the order-ref-no [ " + refnum + " ] ");
				List chnlist = commondesc.getCardsFromOrder(instid, refnum, "INSTANT", "P", "02", jdbctemplate);

				if (!chnlist.isEmpty()) {
					Iterator itr = chnlist.iterator();

					while (itr.hasNext()) {
						Map temp = (Map) itr.next();
						String cardno = (String) temp.get("ORG_CHN");
						trace("Getting card details .... for the cardno[ " + cardno + " ] ");
						Iterator secitr = secList.iterator();
						while (secitr.hasNext()) {
							Map map = (Map) secitr.next();
							CDMK = ((String) map.get("DMK"));
							// eDPK = ((String)map.get("DPK"));
							CDPK = padsssec.decryptDPK(CDMK, EDPK);
							dcardno = padsssec.getCHN(CDPK, cardno);
						}
								
			carddetails = commondesc.getCarddetails(table, instid, cardno, padssenable, keyid, secList,jdbctemplate);
						
						if (!(carddetails.isEmpty())) {
							Iterator crdItr = carddetails.iterator();

							String validto = null;
							while (crdItr.hasNext()) {
								Map crdmap = (Map) crdItr.next();
								cardnum = ((String) crdmap.get("CARD_NO"));
  							    expiry_1 = ((String) crdmap.get("EXP_2"));
								trace("Expiry date1 " + expiry_1);
								validto = ((String) crdmap.get("EXP_1"));
								trace("Expiry date2 " + validto);
								enc_name = ((String) crdmap.get("ENCNAME"));
								trace("Encoding name" + enc_name);
								expiry_2 = ((String) crdmap.get("EXP_2"));
								cvv1 = ((String) crdmap.get("CVV1"));
								cvv2 = ((String) crdmap.get("CVV2"));
								icvv = ((String) crdmap.get("ICVV"));
								servicecode = ((String) crdmap.get("SERVICE_CODE"));
								branchcode = (String) crdmap.get("BRANCH_CODE");
								cardref_no = (String) crdmap.get("CARD_REF_NO");
								track1 = (String) crdmap.get("TRACK1");
								track2 = (String) crdmap.get("TRACK2");
								general_data = (String) crdmap.get("GENERAL_DATA");
								custdata = (String) crdmap.get("GENDATA");
								productcode = (String) crdmap.get("PRODUCT_CODE");
								bin = (String) crdmap.get("BIN");

							}
							String embossingcardno = commondesc.embossingCardNumber(dcardno);
							String validfrom = commondesc.getDate("MM/YY");
					 
							if (enc_name == null) {
								System.out.println(" Enc / Emb name empty ");
								
								status = -5;
								 break;
							}
							if(cvv1==null || cvv2==null || icvv==null){
								System.out.println("  CVV values  name empty ");
								status = -3;
								break;
										}
							
							 encode_name = commondesc.formateEncodingname(enc_name);
							
							countrycode = commondesc.getCountryCode(instid, jdbctemplate);
							if (countrycode == null) {
								//session.setAttribute("preverr", "E");
								//session.setAttribute("prevmsg", "Could not get country code....");
								errormsg="Could not get country code....";
								return "required_home";
							}

							 cardcurrency = commondesc.getCardCurrencyCode(instid, "INST", cardno, jdbctemplate);
							if (cardcurrency == null) {
								//session.setAttribute("preverr", "E");
								//session.setAttribute("prevmsg","Could not get card currecny code [" + cardno + "] ....");
								errormsg="Could not get card currecny code [" + cardno + "] ....";
								return "required_home";
							}
				 
							mcardno = padsssec.getMakedCardno(cardno);
							sb=new StringBuilder(dcardno);
							String appname = "IFD";
							String panseqno = "01";
							
						
							if (padssenable.equals("Y")) {
								embossingcardno = commondesc.embossingCardNumber(dcardno);
								printdata = dcardno + expiry_1 + encode_name;
							} else {
								printdata = cardno + expiry_1 + encode_name;
							}

					        predata = track1 + track2;
					        prerecord = printdata +cvv2+ track1 + track2 + icvv;
					        
 							int insrt_status = -1;
							try {
					 			trace("Inserting pre data....");
 							
					 			insertpstmt.setString(1, instid);
					 			insertpstmt.setString(2, bin);
					 			insertpstmt.setString(3, productcode);
								insertpstmt.setString(4, cardnum);
								insertpstmt.setString(5, order_refnum[i]);
								insertpstmt.setString(6, branchcode);
								insertpstmt.setString(7, prename);
								insertpstmt.setString(8, prerecord);
								insertpstmt.setString(9, usercode);
								insertpstmt.setString(10, encode_name);
								insertpstmt.setString(11, encode_name);
								insertpstmt.setString(12, track1);
								insertpstmt.setString(13, track2);
								insertpstmt.setString(14, cvv1);
								insertpstmt.setString(15, cvv2);
								insertpstmt.setString(16, icvv);
								insertpstmt.setString(17, countrycode);
								insertpstmt.setString(18, cardcurrency);
								insertpstmt.setString(19, "2");
								insertpstmt.setString(20, expiry_1);
								insertpstmt.setString(21, appname);
								insertpstmt.setString(22, panseqno);
								insertpstmt.setString(23, servicecode);
								insertpstmt.setString(24, validfrom);
								insertpstmt.setString(25, validto);
								insertpstmt.setString(26, cardref_no);
								insertpstmt.setString(27, embossingcardno);
								insertpstmt.setString(28, ADDRESS1);
								insertpstmt.setString(29, ADDRESS2);
								insertpstmt.setString(30, ADDRESS3);
								insertpstmt.setString(31, ADDRESS4);
								insertpstmt.setString(32, ADDRESS5);
								insertpstmt.setString(33, PHONENO);
								insertpstmt.setString(34, "--");
								insertpstmt.setString(35, "--");
								insertpstmt.setString(36, "0");
								insertpstmt.addBatch();
								
	 						 	enctrace("preinst_qury-------->"+preinst_qury);
			 	 				trace("Got PRE-INSERT VALUE.... " + preinst_qury);
								
	 						} catch (Exception e) {
						        e.printStackTrace();
								insrt_status = -1;
							}
 					
							try{
								
							trace("inside try block and the order ref no is --> "+refnum);
							updatepstmt.setString(1, "03");
							updatepstmt.setString(2, prename);
							updatepstmt.setString(3, mkckflag);
							updatepstmt.setString(4, instid);
							updatepstmt.setString(5, cardnum);
							updatepstmt.addBatch();
					 		 
							enctrace("preinst_qury-------->"+pre_upd_qury);
							trace("preinst_qury-------->"+pre_upd_qury);
							 
							}catch (Exception e) {
							    e.printStackTrace();
								insrt_status = -1;
							}
		 					
							try{
								
								trace("ip address======>  "+ip);
								auditbean.setIpAdress(ip);
     							mcardno = commondesc.getMaskedCardbyproc(instid, cardnum, table, "C", jdbctemplate);
								
								trace("inside try block and the order ref no is --> "+refnum);
								
		 						AUDITRANpstmt.setString(1,instid);
								AUDITRANpstmt.setString(2,auditbean.getBin());
								AUDITRANpstmt.setString(3,auditbean.getProduct());
								AUDITRANpstmt.setString(4,auditbean.getSubproduct());
								AUDITRANpstmt.setString(5,mcardno);
								AUDITRANpstmt.setString(6,usercode);
								AUDITRANpstmt.setString(7,auditbean.getActmsg());
								AUDITRANpstmt.setString(8,auditbean.getRemarks());
								AUDITRANpstmt.setString(9,"0204");
								AUDITRANpstmt.setString(10,order_refnum[i].toString().trim());
								AUDITRANpstmt.setString(11,auditbean.getPrefilename());
								AUDITRANpstmt.setString(12,"IM");
								AUDITRANpstmt.setString(13,auditbean.getActiontype());
								AUDITRANpstmt.setString(14,auditbean.getAccoutnno());
								AUDITRANpstmt.setString(15,auditbean.getCustname());
								AUDITRANpstmt.setString(16,auditbean.getCardcollectbranch());
								AUDITRANpstmt.setString(17,auditbean.getCin());
								AUDITRANpstmt.setString(18,auditbean.getChecker());
								AUDITRANpstmt.setString(19,auditbean.getIpAdress());
								AUDITRANpstmt.addBatch();
		 						
								enctrace("Auditinsert_qury-------->"+Auditinsert);
								trace("Auditinsert_qury-------->"+Auditinsert);
								
							}catch(Exception e) {
								e.printStackTrace();
								insrt_status = -1;
							}
							
			 			} else {
							trace("No Card Details Found for the " + cardno);
							status = -2;
						 
							break;
						}
					}
				} else {
					errormsg="No Cards data found the the Ref no  " + refnum;
					trace("No Cards data found the the Ref no  " + refnum);
				}
				 

				if (status == -1) {
			    	errormsg="Card Numbers List is Empty===> For the order" + order_refnum[i];
					trace("Card Numbers List is Empty For the order : " + order_refnum[i]);
					presuc = false;
 					break;
				}
				if (status == -2) {
	 				errormsg="Card Numbers List is Empty===> For the order" + order_refnum[i];
					trace("Card Numbers List is Empty : " + order_refnum[i]);
					presuc = false;
			 		break;
				}
				if (status == -3) {
 					errormsg="CVV Data List is Empty===> For the order" + order_refnum[i];
					trace("CVV Data List is Empty For the order : " + order_refnum[i]);
					presuc = false;
				 
				 
					break;
				}
				if (status == -4) {
	 				errormsg="Error While Insert The PRE Data " + order_refnum[i];
					trace("Error While Insert The PRE Data : " + order_refnum[i]);
					presuc = false;
	 				break;
				}

				if (status == -5) {
	 				errormsg= "Encoding / Embossing name should not be empty ";
					trace("Encoding / Embossing name should not be empty");
					presuc = false;
	 				break;
				}

				ordercnt++;
			}
			
			System.out.println("pre status-->"+presuc);
	 		
			if (presuc && conn != null) {
				System.out.println(" connection opend----->\n");
				trace("connectionopend---\n");
				enctrace("connectionopend---\n");
				conn.setAutoCommit(false);
			
			long start = System.currentTimeMillis();
			insertCounts = insertpstmt.executeBatch();
			updateCounts = updatepstmt.executeBatch();
			insertauditCounts=AUDITRANpstmt.executeBatch();
		 	long end = System.currentTimeMillis();
		 	
			System.out.println("insertpreCounts--->"+insertCounts.length);
			System.out.println("updateCounts--->"+updateCounts.length);
			System.out.println("insertauditCounts--->"+insertauditCounts.length);
			
			trace("insertpreCounts--->"+insertCounts.length);
			trace("updateCounts--->"+updateCounts.length);
			trace("insertauditCounts--->"+insertauditCounts.length);
			
			enctrace("insertpreCounts--->"+insertCounts.length);
			enctrace("updateCounts--->"+updateCounts.length);
			enctrace("insertauditCounts--->"+insertauditCounts.length);
		 	
				if((insertCounts.length==updateCounts.length) && (insertauditCounts.length==order_refnum.length))
				
				{
				conn.commit();
	 			trace(ordercnt + " Ordered PRE Files Generated..got committed");
	 			errormsg= ordercnt + " Ordered PRE Files Generated " + authmsg;
	 			
	 			session.setAttribute("prevmsg", ordercnt + " Card(s) PRE File Generated Successfully. " + authmsg);
				session.setAttribute("preverr", "S");
	 			
				addActionMessage(errormsg);
				}
				
				else{
					insertpstmt.clearBatch() ;
					updatepstmt.clearBatch() ;
					AUDITRANpstmt.clearBatch() ;
 					addActionError("could not continue the Personalization process...!!!");
					return "required_home";
		 		}	}  
	 
			else {
			    System.out.println("connection not opended-----/n--");
			    trace("connection not opended-------");
				insertpstmt.clearBatch() ;
				updatepstmt.clearBatch() ;
				AUDITRANpstmt.clearBatch() ;
	   		    addActionError("could not continue the Personalization process...!!! "+errormsg);
		        return "required_home";
			}
	 	} catch (Exception e) {
	 		addActionError("CATCH could not continue the Personalization process...!!! " + errormsg);
	 		trace("Exception : could not continue the PRE Process " + e.getMessage());
			e.printStackTrace();
			return "required_home";
			
		}finally {
			
	 		bin = null;			 				track1 = null;				track2 = null;				authmsg = null;			general_data = null;		custdata = null;
			CDMK = null;					CDPK = null;					mcardno = null;			makerid = null;			checkerid = null;			makerdate = null;
			checkerdate = null;		mkckflag = null;			ckdate = null;				cardnum = null;			expiry_1 = null;			enc_name = null;
			expiry_2 = null;				cvv1 = null;					cvv2 = null;					icvv = null;				servicecode = null;							cardref_no = null;
			printdata = null;				predata=null;				prerecord=null;			ADDRESS1 = null;		ADDRESS2 = null;			ADDRESS3 = null;
			ADDRESS4 = null;			ADDRESS5 = null;		PHONENO = null;		dcardno = null;		sb=null;
			status = 0;							ordercnt = 0;					presuc = true;				padsssec = null;		carddetails = null;		cvvdata = null;
			addressdetails = null;
	 			 
						try {
							if (insertpstmt != null) {
								insertpstmt.close();
								System.out.println("prepare stmt closed for insert query -----");
							}
							
							if (updatepstmt != null) {
								updatepstmt.close();
								System.out.println("prepare stmt closed for update query-----");
							}
							
							if (AUDITRANpstmt != null) {
								AUDITRANpstmt.close();
								System.out.println("prepare stmt closed for insert query Auditran-----");
							}
							if (conn != null) {
								conn.close();
								System.out.println("Jdbc connection closed properly-----> ");
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
	 
		return this.preGenHome();
	}
	
	
	
	
	
	//by siva 18-07-2019
	
	/*public String genInstPRE() throws Exception  {

		trace("******* instant preGenerationprocess *******\n");
		enctrace("******* instant preGenerationprocess  *******\n");
		
		
		//IfpTransObj insertpre = commondesc.myTranObject();
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus txnstatus = txManager.getTransaction(def);
		
		HttpSession session = getRequest().getSession();	
		String[] order_refnum = getRequest().getParameterValues("instorderrefnum");   
		//String[] order_refnum = getRequest().getParameterValues("instorderrefnum");  
		System.out.println("pre list of cards"+order_refnum);
		String instid =  comInstId(); 
		String usercode = comUserCode();
		String username = comUsername();
		trace("Total Orders Selected : " +order_refnum.length); 
		//String productcode = getRequest().getParameter("product");
		String productcode = "";
		trace("Getting binno"+productcode);
		//String bin = commondesc.getBin(instid, productcode, jdbctemplate);
		String bin = "";
		String branchcode = getRequest().getParameter("branchcode");  
		String track1="";
		String track2="";
		String authmsg="";
		String dcardno="";
		String general_data="";
		String custdata="";
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
		PadssSecurity padsssec = new PadssSecurity();
		List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
		String CDMK = "",CDPK="",mcardno="";
		
		Properties props=getCommonDescProperty();
		String EDPK=props.getProperty("EDPK");
	
		try{    
			String makerid = "",checkerid="",makerdate="",checkerdate="", mkckflag="", ckdate="";
			//String act = (String)session.getAttribute("act") ;
			String act = "M" ;
			System.out.println("ACT VALUE FOR PRE FILE GENERATION"+act);
			
			if ( act.equals("M")){ 
				System.out.println( "act value " + act); 
				 makerid = usercode; 
			     mkckflag = "M";
				 ckdate = commondesc.default_date_query;
			     makerdate = "SYSDATE";
			     authmsg = " and Waiting for Authorization ........";
			}else {  // D 
				System.out.println( "act value " + act);
				makerid = usercode;
				checkerid = makerid;
				mkckflag = "P";
				ckdate = "sysdate";
				makerdate = "SYSDATE";
			}
			  
			String table = "INST_CARD_PROCESS";
			int status = 0,prestatus = 0;
			DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
			Date date = new Date();
		 
			String userdesc = commondesc.getUserName(instid, usercode, jdbctemplate); 
			
			String downproductname = productcode;
			if( productcode.equals("983471107")){downproductname="983471901";} 
			String prename = downproductname+"_"+userdesc+"_"+dateFormat.format(date);
			
			trace("prename : "+prename);
			String cardnum="X",expiry_1="X",enc_name="X",expiry_2="X",cvv1="X",cvv2="X",icvv="",servicecode="",cardref_no="";
			int ordercount = order_refnum.length; 
			 String ADDRESS1="",ADDRESS2="",ADDRESS3="",ADDRESS4="",ADDRESS5="",PHONENO="";
			int process_count = 0;
			Boolean presuc = true;
			int ordercnt = 0;
			
			
			for ( int i=0; i<order_refnum.length;i++){				
				String refnum = order_refnum[i].toString().trim();				 
				trace("File generation started Ref Num : "+refnum);
				//String prodcode = commondesc.getProductCode(instid, refnum, jdbctemplate);	
				trace("Getting card list for the order-ref-no [ "+refnum+" ] ");
				List chnlist = commondesc.getCardsFromOrder( instid, refnum, "INSTANT","P","02", jdbctemplate );
				
				if( ! chnlist.isEmpty() ){ 
					Iterator itr = chnlist.iterator();
					List carddetails = null,cvvdata = null,addressdetails=null;
					 
					while( itr.hasNext() ){
						Map temp = (Map)itr.next(); 
						String cardno =  (String)temp.get("ORG_CHN");  
						trace("Getting card details .... for the cardno[ "+cardno+" ] ");
						Iterator secitr = secList.iterator();
						while(secitr.hasNext())
						{
							Map map = (Map) secitr.next(); 
							CDMK = ((String)map.get("DMK"));
							//eDPK = ((String)map.get("DPK"));
							CDPK=padsssec.decryptDPK(CDMK, EDPK);
							dcardno = padsssec.getCHN(CDPK,cardno);
						} 
						carddetails = commondesc.getCarddetails(table, instid, cardno,padssenable,keyid,secList, jdbctemplate);
						if(!(carddetails.isEmpty()))   
						{
							Iterator crdItr = carddetails.iterator();
							 
							String validto = null;
							while(crdItr.hasNext())
							{
								Map crdmap = (Map)crdItr.next();
								cardnum = ((String)crdmap.get("CARD_NO"));
								trace("cardnumber"+cardnum);
								
								expiry_1 = ((String)crdmap.get("EXPIRY_DATE"));
								trace("Expiry date"+expiry_1);
								enc_name = ((String)crdmap.get("ENC_NAME"));
								trace("Encoding name"+enc_name);
								expiry_2 = ((String)crdmap.get("EXP_2")); 
								
								expiry_1 = ((String)crdmap.get("EXP_2"));
								trace("Expiry date1"+expiry_1);
								
								validto = ((String)crdmap.get("EXP_1"));
								trace("Expiry date2"+validto);
								// by siva
								
								enc_name = ((String) crdmap.get("ENCNAME"));
								trace("Encoding name" + enc_name);
								
								expiry_2 = ((String) crdmap.get("EXP_2"));
								
								cvv1 = ((String)crdmap.get("CVV1"));
								cvv2  = ((String)crdmap.get("CVV2"));
								icvv  = ((String)crdmap.get("ICVV"));
								servicecode = ((String)crdmap.get("SERVICE_CODE"));
								branchcode = (String)crdmap.get("BRANCH_CODE"); 
								cardref_no = (String)crdmap.get("CARD_REF_NO");
								track1 =   (String)crdmap.get("TRACK1");
								track2 =   (String)crdmap.get("TRACK2");
								general_data=(String)crdmap.get("GENERAL_DATA");
								custdata=(String)crdmap.get("GENDATA");
								productcode=(String)crdmap.get("PRODUCT_CODE");
								bin=(String)crdmap.get("BIN");
								
							}
							
							String embossingcardno = commondesc.embossingCardNumber(dcardno);
							String validfrom = commondesc.getDate("MM/YY");
							//String validto = expiry_1;
							if( enc_name == null ){
								System.out.println( " Enc / Emb name empty ");
								status = -5;
								break;
							}
							String encode_name = commondesc.formateEncodingname(enc_name);
							
							String countrycode = commondesc.getCountryCode(instid, jdbctemplate);
							if( countrycode == null ){
								session.setAttribute("preverr", "E");	session.setAttribute("prevmsg",  "Could not get country code...." );
								return "required_home";
							}
							
							String cardcurrency =  commondesc.getCardCurrencyCode(instid, "INST", cardno, jdbctemplate );
							if( cardcurrency == null ){
								session.setAttribute("preverr", "E");	session.setAttribute("prevmsg",  "Could not get card currecny code ["+cardno+"] ...." );
								return "required_home";
							}
							mcardno = padsssec.getMakedCardno(cardno);
							
							
							String appname = "IFD";
							String panseqno = "01";
							String printdata = "";
							
							System.out.println("keyid::"+keyid);
						
							if(padssenable.equals("Y"))
							{
							embossingcardno = commondesc.embossingCardNumber(dcardno);	
							printdata = dcardno+expiry_1+encode_name;
							}
							else   
							{
								printdata = cardno+expiry_1+encode_name;
							}
							
							String predata = track1+track2;   
							String prerecord = general_data+track1+track2+custdata;
							 
							
							// by siva in 27-06-19
							trace(" trace checking 111111");
							String ETRACK1 = EncDec.encrypt(track1);
							trace("ETRACK1------>"+ETRACK1);
							String ETRACK2 = EncDec.encrypt(track2);
							trace("ETRACK2------>"+ETRACK2);
							String TRACK = EncDec.encrypt(prerecord);
							trace("TRACK------>"+TRACK);									
							// by siva in 27-06-19
							
							int insrt_status=1;
							try{
							trace("Inserting pre data....");
							String preinst_qury = "INSERT INTO INST_PRE_DATA ( INST_ID,BIN, PRODUCT_CODE, CARD_NO,ORDER_REF_NO,BRANCH_CODE,GENERATED_DATE,PRE_NAME,TRACK_DATA,USER_CODE, ";
							preinst_qury += " EMB_NAME, ENC_NAME, TRACK1, TRACK2, CVV1, CVV2, ICVV, COUNTRY_CODE, CURRENCY_CODE, EXPDATE, APP_NAME, PAN_SEQNO, SERVICE_CODE,VALID_FROM, VALID_TO, CARD_REFNO, EMB_CARDNO,ADDRESS1,ADDRESS2,ADDRESS3,ADDRESS4,ADDRESS5,PHONENO ) VALUES "  ;
							preinst_qury += "('"+instid+"', '"+bin+"', '"+productcode+"', '"+cardnum+"', '"+order_refnum[i]+"', '"+branchcode+"', SYSDATE, '"+prename+"', '"+TRACK+"', '"+usercode+"',  ";
							preinst_qury += " '"+encode_name+"','"+encode_name+"','"+ETRACK1+"','"+ETRACK2+"','"+cvv1+"','"+cvv2+"','"+icvv+"','"+countrycode+"','"+cardcurrency+"','"+expiry_1+"','"+appname+"', '"+panseqno+"', '"+servicecode+"','"+validfrom+"','"+validto+"', '"+cardref_no+"', '"+embossingcardno+"','"+ADDRESS1+"','"+ADDRESS2+"','"+ADDRESS3+"','"+ADDRESS4+"','"+ADDRESS5+"','"+PHONENO+"') ";
							enctrace("PRE-INSERT QUERY------> : " + preinst_qury );
							insrt_status = commondesc.executeTransaction(preinst_qury, jdbctemplate);
							trace("Got PRE-INSERT VALUE...." + insrt_status);
							}
							catch (Exception e){
								e.printStackTrace();
								 insrt_status =-1;
							}
							
							trace("updating pre status....");
							System.out.println("Updating cardnumber"+cardnum);
							//String pre_upd_qury = "UPDATE INST_CARD_PROCESS SET CARD_STATUS='03', PRE_DATE=sysdate,PRE_FILE= '"+prename+"', MAKER_DATE= "+makerdate+", MKCK_STATUS = '"+mkckflag+"' WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardnum+"'";
							
							String pre_upd_qury = "UPDATE INST_CARD_PROCESS SET CARD_STATUS='03', PRE_DATE=sysdate,PRE_FILE= '"+prename+"', MAKER_DATE= "+makerdate+", MKCK_STATUS = '"+mkckflag+"' WHERE INST_ID='"+instid+"' AND ORG_CHN='"+cardnum+"'";
							
							String pre_upd_qury = "UPDATE INST_CARD_PROCESS SET CVV1='0',CVV2='0',ICVV='0',CARD_STATUS='03', PRE_DATE=sysdate,PRE_FILE= '"+prename+"', MAKER_DATE= "+makerdate+", MKCK_STATUS = '"+mkckflag+"' WHERE INST_ID='"+instid+"' AND ORG_CHN='"+cardnum+"'";
							enctrace("update_qury : "+pre_upd_qury);							
							int update_status = commondesc.executeTransaction(pre_upd_qury, jdbctemplate);
							trace("got :" + update_status  ) ;
							if(insrt_status == 1 && update_status == 1)
							{
								process_count = process_count + 1;	
								
								
								try{
									
									mcardno = commondesc.getMaskedCardbyproc(instid, cardnum,table,"C", jdbctemplate);
									auditbean.setActmsg(" Instant Card Number [ "+mcardno+" ] "+ authmsg);
									//auditbean.setUsercode(usercode);
									auditbean.setUsercode(comUsername());
									auditbean.setCardcollectbranch(branchcode);
									auditbean.setActiontype("IM");
									auditbean.setAuditactcode("0140"); //0204
									auditbean.setCardno(mcardno);
									auditbean.setApplicationid(order_refnum[i].toString().trim());
									//auditbean.setCardnumber(cardnum);
									//commondesc.insertAuditTrail(instid, userid, auditbean, jdbctemplate, txManager);
									commondesc.insertAuditTrailPendingCommit(instid, comUsername(), auditbean, jdbctemplate, txManager);
								 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
								
							}
							else{
								status = -4;
								break;
							}
							
							
						}else{
							trace("No Card Details Found for the " + cardno);
							status = -2;
							break;
						}  
					} 
				}else{
				 	session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg",  "No Cards data found the the Ref no  " + refnum );  
					trace("No Cards data found the the Ref no  " + refnum );
				}
				
				session.setAttribute("preverr", "E");
				

				if(status == -1) {
					
					session.setAttribute("prevmsg",  "Card Numbers List is Empty===> For the order"+order_refnum[i] );  
					trace("Card Numbers List is Empty For the order : "+order_refnum[i]);
					presuc = false;
					break;
				}
				if(status == -2) {
					session.setAttribute("prevmsg",  "Card Numbers List is Empty===> For the order"+order_refnum[i] ); 
					trace("Card Numbers List is Empty : "+order_refnum[i]);
					presuc = false;
					break;
				}
				if(status == -3) {
					session.setAttribute("prevmsg",  "CVV Data List is Empty===> For the order"+order_refnum[i] ); 
					trace("CVV Data List is Empty For the order : " + order_refnum[i]);
					presuc = false;
					break;
				}
				if(status == -4) {
					session.setAttribute("prevmsg", "Error While Insert The PRE Data "+order_refnum[i]); 
					trace("Error While Insert The PRE Data : "+order_refnum[i]);
					presuc = false;
					break;
				}
				
				if(status == -5) { 
					session.setAttribute("prevmsg",  "Encoding / Embossing name should not be empty " );  
					trace("Encoding / Embossing name should not be empty");
					presuc = false;
					break;
				}
				
				ordercnt ++ ;
			}
			
			if( presuc ){
				txManager.commit(txnstatus);
				trace ( ordercnt + " Ordered PRE Files Generated..got committed");
				session.setAttribute("preverr", "S");
				//session.setAttribute("prevmsg",  ordercnt + " Ordered PRE Files Generated Successfully ");
				session.setAttribute("prevmsg",  ordercnt + " Ordered PRE Files Generated " +authmsg);
			
			}else{
				txManager.rollback(txnstatus);
				trace( "PRE insert got failed...got rolledback.");
			} 
			
		}catch (  Exception e ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg",  "Exception : Could not continue the Personalization process "); 
			txManager.rollback(txnstatus);
			trace( "Exception : could not continue the PRE Process " + e.getMessage() ); 
			e.printStackTrace();
		} 
		
		
		return this.preGenHome(); 
	}
	*/
	

	
	public String deleteInstPreFile(){
		trace("******* Delete personalization file begiin********");
		enctrace("******* Delete personalization file begin********");
		HttpSession session = getRequest().getSession();
	 
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);
		
		String instid = comInstId();
		String prefilename = getRequest().getParameter("prefilename");
		try{
			trace("Getting downloaded count for the file : " + prefilename );
			int checkdowncnt = this.checkDownloadCount(instid, prefilename, jdbctemplate );
			trace( "Got : checkdowncnt " + checkdowncnt );
			if( checkdowncnt <= 0 ){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Could not delete. Download personalization file and then delete...");
				trace("Personalization file deleted successfully..got committed");
				return this.preDownloadHome();
			}
			
			trace("Deleting the personalization file...");
			int deletepre = this.delelePreFile(instid, prefilename, jdbctemplate);
			trace("Got deletepre : " + deletepre );
			if( deletepre < 0 ){
				txManager.rollback(status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Could not delte the file ");
				trace("Could not delte the file ..got rolled back");
				return this.preDownloadHome();
			} 
			
			txManager.commit(status);
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", "Personalization file deleted successfully");
			trace("Personalization file deleted successfully..got committed");
			
		}catch(Exception e){
			txManager.rollback(status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception : Could not delete file");
			trace("Exception : could not delete : " + e.getMessage());
			e.printStackTrace();
		}
		
		trace("******* Delete personalization file end********\n\n");
		enctrace("******* Delete personalization file end********\n\n");
		
		return this.preDownloadHome();
	}
	
	/*public int checkDownloadCount(String instid, String prefilename, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		try {
			String downcntqry = "SELECT count(*)as count FROM INST_PRE_DATA WHERE PRODUCT_CODE='"+instid+"' AND PRE_NAME='"+prefilename+"'";
			enctrace(downcntqry);
			x = jdbctemplate.queryForInt(downcntqry);
		} catch (EmptyResultDataAccessException e) {}
		return x; 
	}

	public int delelePreFile(String instid, String prefilename, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String delteqry = "DELETE FROM INST_PRE_DATA WHERE INST_ID='"+instid+"' AND PRE_NAME='"+prefilename+"'";
		enctrace(delteqry);
		x = jdbctemplate.update(delteqry);
		return x; 
	}
	
	public int updatePreFileDownloadCnt(String instid, String prefilename, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String delteqry = "UPDATE INST_PRE_DATA SET DOWN_CNT=DOWN_CNT+1 WHERE INST_ID='"+instid+"' AND PRE_NAME='"+prefilename+"'";
		enctrace("downloadqry :"+ delteqry );
		x = jdbctemplate.update(delteqry);
		return x; 
	}
	
	public int updatePersonalizePreFileDownloadCnt(String instid, String prefilename, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String downloadqry = "UPDATE PERS_PRE_DATA SET DOWN_CNT=DOWN_CNT+1 WHERE INST_ID='"+instid+"' AND PRE_NAME='"+prefilename+"'";
		enctrace("downloadqry :"+ downloadqry);
		x = jdbctemplate.update(downloadqry);
		return x; 
	}
	*/
	
	
	

	public int checkDownloadCount(String instid, String prefilename, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		try {
			
		/*	String downcntqry = "SELECT count(*)as count FROM INST_PRE_DATA WHERE PRODUCT_CODE='"+instid+"' AND PRE_NAME='"+prefilename+"'";
			enctrace(downcntqry);
			x = jdbctemplate.queryForInt(downcntqry);*/
			
			///by gowtham-280819
			String downcntqry = "SELECT count(*)as count FROM INST_PRE_DATA WHERE PRODUCT_CODE=? AND PRE_NAME=?";
			enctrace(downcntqry);
			x = jdbctemplate.queryForInt(downcntqry,new Object[]{instid,prefilename});
			
		} catch (EmptyResultDataAccessException e) {}
		return x; 
	}

	public int delelePreFile(String instid, String prefilename, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		
	/*	String delteqry = "DELETE FROM INST_PRE_DATA WHERE INST_ID='"+instid+"' AND PRE_NAME='"+prefilename+"'";
		enctrace(delteqry);
		x = jdbctemplate.update(delteqry);*/
		
		///by gowtham-280819
		String delteqry = "DELETE FROM INST_PRE_DATA WHERE INST_ID=? AND PRE_NAME=? ";
		enctrace(delteqry);
		x = jdbctemplate.update(delteqry,new Object[]{instid,prefilename});
		
		return x; 
	}
	
	public int updatePreFileDownloadCnt(String instid, String prefilename, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		
	/*	String delteqry = "UPDATE INST_PRE_DATA SET DOWN_CNT=DOWN_CNT+1 WHERE INST_ID='"+instid+"' AND PRE_NAME='"+prefilename+"'";
		enctrace("downloadqry :"+ delteqry );
		x = jdbctemplate.update(delteqry);*/
		
		///by gowtham-280819
		String delteqry = "UPDATE INST_PRE_DATA SET DOWN_CNT=DOWN_CNT+1 WHERE INST_ID=? AND PRE_NAME=?  AND AUTH_CODE='1'";
		enctrace("downloadqry :"+ delteqry );
		x = jdbctemplate.update(delteqry,new Object[]{instid,prefilename});
		
		return x; 
	}
	
	public int updatePersonalizePreFileDownloadCnt(String instid, String prefilename, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		
		/*String downloadqry = "UPDATE PERS_PRE_DATA SET DOWN_CNT=DOWN_CNT+1 WHERE INST_ID='"+instid+"' AND PRE_NAME='"+prefilename+"'";
		enctrace("downloadqry :"+ downloadqry);
		x = jdbctemplate.update(downloadqry);*/
		
		
		//by gowtham-260819
		String downloadqry = "UPDATE PERS_PRE_DATA SET DOWN_CNT=DOWN_CNT+1 WHERE INST_ID=? AND PRE_NAME=?";
		enctrace("downloadqry :"+ downloadqry);
		x = jdbctemplate.update(downloadqry,new Object[]{instid,prefilename});
		
		return x; 
	}
	public String preAuthHome() {
		String instid =  comInstId(); 
		
		HttpSession session = getRequest().getSession(); 
	 
		 	
			
			
			/*branchlist =  this.commondesc.generateBranchList(instid, jdbctemplate );
			setBranchlist(branchlist);
			
			prodlist = commondesc.getProductList( instid, jdbctemplate, session ); 
			System.out.println( "john__" + prodlist );
			setProdlist(prodlist); */
						
			List pers_prodlist=null,br_list=null;
			String inst_id =comInstId();
			String usertype = comuserType();
			String branch = comBranchId();
			String cardstatus="03";
			String mkrstatus="M";
			try {
				
				int reqch= commondesc.reqCheck().requiredCheck(instid, session, jdbctemplate);
				if( reqch < 0 ){
					return "required_home";
				}
				
				System.out.println("Inst Id===>"+inst_id+"  Branch Code ===>"+branch);
				if (usertype.equals("INSTADMIN")) {
					System.out.println("Branch list start");
					br_list = commondesc.InstgetBranchCodefmProcess(inst_id, cardstatus, mkrstatus, jdbctemplate);
					System.out.println("Branch list "+br_list);
					if(!(br_list.isEmpty())){
						setBranchlist(br_list);
						System.out.println("Branch list is not empty");
						//setCardgenstatus('Y');
					}
					else{
						addActionError("No Cards Waiting For Pre Authorization ... ");
						System.out.println("Branch List is empty ");
						return "required_home";    
						
					}
				}
				pers_prodlist=commondesc.InstgetProductListBySelected(inst_id, cardstatus, mkrstatus, jdbctemplate);
				if (!(pers_prodlist.isEmpty())){
					setProdlist(pers_prodlist);
					session.setAttribute("curerr", "S");
					session.setAttribute("curmsg","");
					System.out.println("Product List is ===> "+pers_prodlist);
					//setCardgenstatus('Y');
				} else{
					System.out.println("No Product Details Found ");
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg"," No Product Details Found ");
					//setCardgenstatus('N');
				}
			
		}catch( Exception e ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.getMessage());
			e.printStackTrace();
		}
		return "instpreauth_home"; 
	}
	 
	
	public String viewPREAuthList(){
		 
		HttpSession session = getRequest().getSession();
		
		String instid =  comInstId(); 
		String card_status = "03";
		String mkckstatus = "M";
		 
		String datefld =""; 
		datefld = "PRE_DATE";
		 
		
		String branch = getRequest().getParameter("branchcode");
		String cardtype = getRequest().getParameter("cardtype");
		String[] bin = cardtype.split("~");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		
		String filtercond = commondesc.filterCondition(bin[0],branch,fromdate,todate,datefld);
		System.out.println( "filter condition " + filtercond );
		
		List waitingforcardpin;
		try {
			waitingforcardpin = this.commondesc.waitingForInstCardProcess(instid, card_status,  mkckstatus, filtercond, jdbctemplate);
		
			System.out.println( "waitingforcardpin " + waitingforcardpin);
			if( waitingforcardpin.isEmpty() ){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " No records found " ); 
				return this.preGenHome();
			}
			
			setInstorderlist(waitingforcardpin); 
			
			if( waitingforcardpin.isEmpty() ){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " No records found " ); 
				return this.preGenHome();
			}
		} catch (Exception e) {
			System.out.println( "Exception : Could not continue the process");
			trace("Exception : while view pre auth list :  " + e.getMessage() );
			e.printStackTrace();
		} 
		return "instpreauth_list";   
	}
	
	public String authourizeInstPRE() {
		HttpSession session = getRequest().getSession();		
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);
		String username=comUsername();
		String userid =comUserCode(); 
		String instid =  comInstId(); 
		
		String table = "INST_CARD_PROCESS";
		trace("********Authoroze intant PRE begin ********** ");
		enctrace("********Authoroze intant PRE begin ********** ");
		String[] order_refnum = getRequest().getParameterValues("instorderrefnum");
		
		System.out.println("order_refnum__" + order_refnum);
		

		//added by gowtham_220719
		String  ip=(String) session.getAttribute("REMOTE_IP");
		Date date =  new Date();

		int size = 0;
		int[] updateCounts = new int[size];
		int[] updateCountsPRE = new int[size];
		Connection conn = null;
		Dbcon dbcon = new Dbcon(); 
		conn = dbcon.getDBConnection();
		PreparedStatement pstmt = null;
		PreparedStatement pstmtPRE = null;
		
		
		
try{
			
			if(order_refnum== null ){
				addActionError("No card number selected ....");
					return "required_home";
			}
			int crdcnt = 0;
		
			 
			System.out.println("SYSDATE--->"+test.Date.getCurrentDate());
			System.out.println("instid-->"+instid);
			System.out.println("userid-->"+userid);
			
			System.out.println("order_refnum-->"+order_refnum.length);
			
			/*String cardauthqry = "UPDATE INST_CARD_PROCESS SET CARD_STATUS=?,  MKCK_STATUS=?, AUTH_DATE=sysdate,PRIVILEGE_CODE=? WHERE INST_ID=? AND ORDER_REF_NO=? and CARD_STATUS=? "; 
			enctrace( "cardgenqry : " + cardauthqry );
		int x =jdbctemplate.update(cardauthqry,new Object[]{"04","P","02P",instid,order_refnum[i],"03"});
			 */
			
			
			
			if (conn != null) {
				System.out.println(" connection opend----->\n");
				trace("connectionopend---\n");
				enctrace("connectionopend---\n");
				conn.setAutoCommit(false);
				String sql = "UPDATE INST_CARD_PROCESS SET CARD_STATUS='04', MKCK_STATUS='P', AUTH_DATE=SYSDATE ,PRIVILEGE_CODE='02P' WHERE INST_ID=? AND ORDER_REF_NO=? and CARD_STATUS='03' ";
				String pre="UPDATE INST_PRE_DATA SET AUTH_CODE='1' WHERE INST_ID=? AND ORDER_REF_NO=?";
				pstmt = conn.prepareStatement(sql);
				pstmtPRE = conn.prepareStatement(pre);
				trace("sql qry for update : " + sql);
				
				for (int i = 0; i < order_refnum.length; i++) {
					pstmt.setString(1,instid );
				    pstmt.setString(2,order_refnum[i]);
					pstmt.addBatch();
					
					pstmtPRE.setString(1,instid );
				    pstmtPRE.setString(2,order_refnum[i]);
					pstmtPRE.addBatch();
					
		 		}
				
				long start = System.currentTimeMillis();
				updateCounts = pstmt.executeBatch();
				updateCountsPRE = pstmtPRE.executeBatch();
				long end = System.currentTimeMillis();
				System.out.println("updateCounts--->"+updateCounts.length);
				System.out.println("awaitcardlist.length---->"+order_refnum.length);
				System.out.println("updateCountsPRE.length---->"+updateCountsPRE.length);
				trace("awaitcardlist.length---->"+order_refnum.length);
				
				if((updateCounts.length==order_refnum.length) && (updateCountsPRE.length==updateCounts.length) && (updateCountsPRE.length==order_refnum.length)){
				conn.commit();
				addActionMessage( updateCounts.length + "    Card(s)Authorized Successfully...");
				
				session.setAttribute("prevmsg", updateCounts.length + "    Card(s)Authorized Successfully...");
				session.setAttribute("preverr", "S");
				
				System.out.append("Update records-----------> /n" + updateCounts.length);
				trace(updateCounts.length + " Card(s) Authorized Successfully..got committed...");
				trace(order_refnum.length + " Card(s) Authorized Successfully..got committed...");
				trace(updateCountsPRE.length + " Card(s) Authorized Successfully..got committed...");
				return preAuthHome();}
				else{
					conn.rollback();
					pstmt.clearBatch() ;
					pstmtPRE.clearBatch() ;
					System.out.println("could not continue the process.../n--");
					trace("could not continue the process...---");
					addActionError("could not continue the process...!!!");
					return preAuthHome();
					}
		
				
			}}catch(Exception e){
				txManager.rollback(status);
				addActionError("Exception : could not continue the issuance process...");
				trace("Exception : could not continue the issuance process..."+ e.getMessage());
				e.printStackTrace();
				return "required_home";
			}finally {
				try {
					if (pstmt != null) {
						pstmt.close();
						pstmtPRE.close();
						System.out.println("prepare stmt closed-----");
					}
					if (conn != null) {
						conn.close();
						System.out.println("Jdbc connection closed-----> ");
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
	 	
		
		
		
		
		
		
		
		
		
		
		
		
		/*
		if( order_refnum != null ){
			try {
				int cnt = 0;
				for ( int i=0; i<order_refnum.length; i++ ){
					trace("Updating status for the order " + order_refnum[i] );
					
					String cardauthqry = "UPDATE INST_CARD_PROCESS SET CARD_STATUS='04',  MKCK_STATUS='P', AUTH_DATE=sysdate,PRIVILEGE_CODE='02P' WHERE INST_ID='"+instid+"' AND ORDER_REF_NO='"+order_refnum[i]+"' and CARD_STATUS='03'"; 
					enctrace( "cardgenqry : " + cardauthqry );
					int x = commondesc.executeTransaction(cardauthqry, jdbctemplate);
					
					
					
					///by gowtham-280819
					String cardauthqry = "UPDATE INST_CARD_PROCESS SET CARD_STATUS=?,  MKCK_STATUS=?, AUTH_DATE=sysdate,PRIVILEGE_CODE=? WHERE INST_ID=? AND ORDER_REF_NO=? and CARD_STATUS=? "; 
					enctrace( "cardgenqry : " + cardauthqry );
				int x =jdbctemplate.update(cardauthqry,new Object[]{"04","P","02P",instid,order_refnum[i],"03"});
					
					trace( "Got : " + x );
					
					cardauthqry = "UPDATE INST_CARD_PROCESS SET  CARD_STATUS='04' ,MKCK_STATUS='P', AUTH_DATE=sysdate WHERE INST_ID='"+instid+"' AND ORDER_REF_NO='"+order_refnum[i]+"' and CARD_STATUS='03'"; 
					enctrace( "cardgenqry : " + cardauthqry );
					x = commondesc.executeTransaction(cardauthqry, jdbctemplate);
					
					
					cnt++;
					
					*//************* AUDIT BLOCK**************//* 
					try{ 
						
					//added by gowtham_220719
						trace("ip address======>  "+ip);
						auditbean.setIpAdress(ip);

						
						String mcardno = commondesc.getMaskedCardbyproc(instid, order_refnum[i],table,"O", jdbctemplate);
						if(mcardno==null){mcardno=order_refnum[i];}
						auditbean.setActmsg("Instant File Authorized  [ "+mcardno+" ]");
						auditbean.setUsercode(userid);
						
						auditbean.setUsercode(comUsername());
						//auditbean.setCardcollectbranch(branchcode);
						auditbean.setActiontype("IC");
						
						auditbean.setAuditactcode("0204");  
						auditbean.setCardno(mcardno);
						auditbean.setApplicationid(order_refnum[i].toString().trim());
						//auditbean.setCardnumber(order_refnum[i].toString());
						//commondesc.insertAuditTrail(in_name, Maker_id, auditbean, jdbctemplate, txManager);
						commondesc.insertAuditTrailPendingCommit(instid, comUsername(), auditbean, jdbctemplate, txManager);
					 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
					
					
					*//************* AUDIT BLOCK**************//*
				}
				
				session.setAttribute("preverr", "S");
				session.setAttribute("prevmsg", cnt + " Ordred PRE authorized successfully" );   
				txManager.commit( status );
				trace(cnt + " Ordred PRE authorized successfully...got committed...");
				
			} catch (Exception e) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg",  " Error while process the PRE... " + e );   
				txManager.rollback( status );
				trace("Exception : could not continue the process : " + e.getMessage()  );
				e.printStackTrace();
			} 
		}else{
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg",  "COULD NOT FETCH RECORDS FOR THE ORDER " + order_refnum);  
			trace("COULD NOT FETCH RECORDS FOR THE ORDER " + order_refnum);
		}
		*/
	 		
		return this.preAuthHome();
	}
	
	
	
	public String preDownloadHome() 
	{
		List pers_prodlist=null,br_list=null;
		String inst_id =comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		HttpSession session = getRequest().getSession();
		
		try {
			System.out.println("Inst Id===>"+inst_id+"  Branch Code ===>"+branch);
			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = commondesc.generateBranchListINSTPRE(inst_id, jdbctemplate);
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
				}
			}
			pers_prodlist=commondesc.getProductListView(inst_id,jdbctemplate);
			if (!pers_prodlist.isEmpty()){
				setProdlist(pers_prodlist);
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
			e.printStackTrace();
		}
		
		return "instpredown_home";
	}
		
	
	
	public void getPREFileList() throws Exception {
		String instid =comInstId();
		String filename = getRequest().getParameter("filename");
		String cardlist = "";
		String dcardno = null;
		String keyid = null;
		String CDMK = null;
		String CDPK = null;
		String EDPK = null;
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		Properties props = null;
		
		if (padssenable.equals("Y")) {

			keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			// System.out.println("keyid====="+keyid);
			Iterator secitr = secList.iterator();
			// System.out.println("secList====="+secList);
			if (!secList.isEmpty()) {
				while (secitr.hasNext()) {
					Map map = (Map) secitr.next();
					CDMK = ((String) map.get("DMK"));
				}
			}
		}
		props = getCommonDescProperty();
		EDPK = props.getProperty("EDPK");
		PadssSecurity	padsssec = new PadssSecurity();	
		CDPK = padsssec.decryptDPK(CDMK, EDPK);
		
		
		
		System.out.println("CDMK-->"+CDMK);
		System.out.println("CDPK-->"+CDPK);
		
		
		try{
			
			/*String previewqry = "SELECT CARD_NO FROM INST_PRE_DATA WHERE INST_ID='"+instid+"' AND  PRE_NAME='"+filename+"'";
			List previewqryview = jdbctemplate.queryForList( previewqry );*/
			
			
			///by gowtham-280819
			String previewqry = "SELECT CARD_NO FROM INST_PRE_DATA WHERE INST_ID=? AND  PRE_NAME=? ";
			List previewqryview = jdbctemplate.queryForList( previewqry,new Object[]{instid,filename} );
			
			
			if( !previewqryview.isEmpty() ){
				Iterator itr = previewqryview.iterator();
				cardlist += "!!! Card Number List !!! \n\n";
				while( itr.hasNext() ){
					Map mp = (Map)itr.next();
					/*cardlist += (String)mp.get("CARD_NO") + "\n";*/
					
					 dcardno=(String)mp.get("CARD_NO") + "\n";
					cardlist += padsssec.getCHN(CDPK,dcardno) + "\n";
					
					System.out.println("cardlist---->"+cardlist);
					
					//dcardno = , CHN);
				}
			}
		}catch(Exception e ){
			trace( "Exception : " + e.getMessage() );
			 cardlist = "COULD NOT GET CARDNUMBER LIST";
		}
		getResponse().getWriter().write(cardlist);
	}
	
		 
	public void getPREFilesDetails() throws Exception {
		String instid = getRequest().getParameter("instid");
		String filename = getRequest().getParameter("filename");
		String product = getRequest().getParameter("prodid");
		JSONObject prejson = new JSONObject();
		try{
			
			/*String previewqry = "SELECT DOWN_CNT, PRE_NAME, COUNT(*) AS CARDCOUNT  FROM INST_PRE_DATA WHERE PRODUCT_CODE='"+product+"' AND  PRE_NAME='"+filename+"'  GROUP BY PRE_NAME,DOWN_CNT";
			List previewqryview = jdbctemplate.queryForList( previewqry );*/
			
			

			////by gowtham-280819
			String previewqry = "SELECT DOWN_CNT, PRE_NAME, COUNT(*) AS CARDCOUNT  FROM INST_PRE_DATA WHERE PRODUCT_CODE=? AND  PRE_NAME=? AND AUTH_CODE='1'   GROUP BY PRE_NAME,DOWN_CNT";
			List previewqryview = jdbctemplate.queryForList( previewqry,new Object[]{product,filename} );
			
			if( !previewqryview.isEmpty() ){
				
				Iterator itr = previewqryview.iterator();
				
				while( itr.hasNext() ){
					Map mp = (Map)itr.next();
					prejson.put("DOWNCNT", (String)(Object)mp.get("DOWN_CNT").toString() );
					prejson.put("PRENAME", (String)mp.get("PRE_NAME") );
					prejson.put("CARDCOUNT", (String)(Object)mp.get("CARDCOUNT").toString() );
				}
				//Gold Card_12092017_175940
				System.out.println("filename"+filename);
				String[] filearray = filename.split("_");
				String usercode = (String)filearray[1];
				String gendate1 = (String)filearray[2].substring(0, 2) + "-" + (String)filearray[2].substring(2, 4)+ "-" + (String)filearray[2].substring(4, 8);
				String gendate = gendate1+ " " +filearray[3];
				
				//String username = commondesc.getUserName(instid, usercode, jdbctemplate);
				//System.out.println("username---> "+username);
				prejson.put("USERNAME", usercode );
				prejson.put("GENDATE", gendate ); 
				prejson.put("RESP", 0);
			}
			
		}catch(Exception e){
			prejson.put("RESPREASON", "Could not get filename view" );
			prejson.put("RESP", 1);
			trace("Exception Pre view : " + e.getMessage() );
			e.printStackTrace();
		}
		getResponse().getWriter().write(prejson.toString() );
	}
	
	
	public void getPREFiles() throws Exception{
		
		System.out.println("*********Inside gerPREFile method*********8");
		String instid = getRequest().getParameter("instid");
		String productcode = getRequest().getParameter("prodid");
		String productcode1 = getRequest().getParameter("prodid");
		System.out.println("productcode1-->"+productcode1);
		System.out.println("productcode__"+productcode+"_instid_"+instid);
		String filetype = getRequest().getParameter("filetype");
		trace("file type-->"+filetype);
		System.out.println("file type-->"+filetype);
		int predispdays = commondesc.getPREDisplayDays(instid, jdbctemplate); 
		trace(" Display timer intervel on days : " + predispdays );
		List  prenamelist = commondesc.getInstPREList(instid, productcode, predispdays, filetype, jdbctemplate);
		System.out.println(prenamelist.toString());
		String opt ="<option value='-1'> -SELECT- </option>";
		if ( prenamelist.isEmpty() ) {
			opt = "<option value='-1'> No Files found </option>";
		}else{
			Iterator  itr = prenamelist.iterator();
			while( itr.hasNext() ){ 
				Map temp = (Map)itr.next(); 
				String prename = (String)temp.get("PRE_NAME");
				opt += "<option value='"+prename+"'>"+prename+".PRE</option>";
			} 
		}
		getResponse().getWriter().write(opt);
	}
	
	
	 
	
	String prefilename_new;
	
	 
	public String getPrefilename_new() {
		return prefilename_new;
	}

	public void setPrefilename_new(String prefilename_new) {
		this.prefilename_new = prefilename_new;
	}

	
	
/*	
public String downInstPREFiles() 
{
	trace("inside download pre method");
	Properties prop = commondesc.getCommonDescProperty();
	String prefilelocation = prop.getProperty("PREFILELOCATION");
	String dwnprename = "";
	System.out.println("padsadsath"+prefilelocation);
	
	String actname = getRequest().getParameter("submit");
	System.out.println( "action name is " + actname );
	trace( "action name is " + actname );
	String instid =  comInstId(); 
	String usercode = comUserCode();
	String bin = getRequest().getParameter("cardtype");
	trace("cardtype"+bin);
	String prefilename = getRequest().getParameter("prefilename");
	trace("prefilename"+prefilename);
	HttpSession session = getRequest().getSession();	
	String tablename = "INST_PRE_DATA";
	IfpTransObj transact = commondesc.myTranObject("PREDOWNLOAD", txManager);
	
	
	JSONObject listofpreheaders = this.generatePREHeader();
	trace("generatePREHeader");
	JSONObject predbfields = this.generatePREFDBFields();
	trace("generatePREFDBFields");
	ExcelGenerator excelgen = new ExcelGenerator();
	trace("ExcelGenerator");
	try
	{
		trace("inside try");
		if( actname.equals("Delete"))
		{
			String delmsg = null;
			int predel;
			try 
			{
				predel = commondesc.deletePREFiles(instid, bin, prefilename, tablename, jdbctemplate);
				if( predel < 0    ) {
					session.setAttribute("preverr", "E");
					delmsg = "No Records Deleted";
				}else if( predel == 0   ) {
					session.setAttribute("preverr", "E");
					delmsg = "Download the file atleast one time. Then try again to delete";
				}else if( predel > 0 ) {
					delmsg = predel +" Records Deleted successfully ";
					session.setAttribute("preverr", "S");
				}
				System.out.println( "delete rec count is " + predel );
			} catch (Exception e)  {
				session.setAttribute("preverr", "E");
				delmsg = "Error while delete Records " + e ;
				e.printStackTrace();
			}  
			session.setAttribute("prevmsg",  delmsg ); 
			return preDownloadHome();
		} 
		PreprocessAction preprocess = new PreprocessAction();
		int cardcount =  this.checkDownloadCount(bin,prefilename,jdbctemplate );
		
		int preSeqNo = preprocess.sequencePREFILE(instid, jdbctemplate);
		
		trace("Generating presonalization file....excel");
		//int x = excelgen.generatePRE(instid, bin, prefilename, tablename, listofpreheaders, predbfields, session, jdbctemplate, commondesc );
		int x = preprocess.generatePREInst(instid, bin,cardcount, prefilename, tablename, preSeqNo, session, jdbctemplate, commondesc,preprocess );
		trace("Generating presonalization file....got : " + x);
		if( x < 0 ){ 
			return "required_home";
		}
		
		
		int downcnt = updatePreFileDownloadCnt(instid, prefilename, jdbctemplate);
		if( downcnt  < 0 ){
			//commondesc.rollbackTxn(jdbctemplate);
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Could not continue the download process..");
			trace("Could not update the download count. got rolled back");
		}
		
		
		

		//by siva 05-07-2019 PRE file encryption
		


		File f1=null;
		//------PGB EncryptIOn..Filename
		String newFIle = "";
		dwnprename = session.getAttribute("PRENAME").toString();
		System.out.println("inside PGB EncryptIOn..Filename" + dwnprename);

		if (dwnprename.contains(".pre")) 
		{
			int index = dwnprename.lastIndexOf(".");
			String newExtension = ".pgp";
			System.out.println("----  " + index);
			f1 = new File(dwnprename);
			String s = f1.getName().substring(0, index);
			System.out.println("----  " + s);
			File f2 = new File(s + newExtension);
			newFIle = f2.toString();
			System.out.println("----  " + newFIle);
		}
		try
		{
		File nwfile = new File(prefilelocation + dwnprename);
		if (nwfile.exists()) 
		{
			System.out.println(OS);
			
			if (isWindows())
			{
				System.out.println("This is Windows");
				System.out.println("inside PGB EncryptIOn..Filename encrypt cmd" + dwnprename);
				String command = "cmd /c start /B gpg --encrypt -r Oberthur <" + dwnprename + ">" + newFIle + "\"";			
				//String command = " gpg --yes --batch --passphrase=[Oberthur] -c  <" + dwnprename + ">" + newFIle + "\" ";
				System.out.println(command);
				Runtime.getRuntime().exec(command, null, new File(prefilelocation));
						
			}
			else if (isMac()) 
			{
				System.out.println("This is Mac");
			} 
			else if (isUnix()) 
			{
				System.out.println("This is Unix or Linux");
				String source1 = new String(prefilelocation +dwnprename);
				String source2 = new String(prefilelocation + newFIle);
				Process proc = Runtime.getRuntime().exec(new String[]{prefilelocation + "./start.sh", source1 , source2 });
				System.out.println("proc"+proc);
			}
			else if (isSolaris()) 
			{
				System.out.println("This is Solaris");
			} 
			else
			{
				System.out.println("Your OS is not support!!");
			}
			
			Thread.sleep(2000);
				File newexists = new File(prefilelocation + newFIle);
				System.out.println("newexists"+newexists);
				if (newexists.exists())
					{
					System.out.println("newexists"+newexists);
						File delf = new File(prefilelocation +dwnprename);
						if (delf.delete())
						{System.out.println("file was deleted"+delf);	}
						else
						{System.out.println("file was not deleted"+delf);}
					}
			
		    	}
		}	
			 catch (Exception e) 
			{					
					txManager.rollback(transact.status);
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "Exception : could not convert PGB convertion proces...");
					trace("Exception : could not continue the dowload process : " + e.getMessage());
					e.printStackTrace();
			 }
			
		
		
		int predelete = deletepre(instid, prefilename, jdbctemplate);
		trace("delete pre count--->"+predelete);
		if( predelete <0 )
		{
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "While deleting PRE getting issue");
			trace("While deleting PRE getting issue");
		}
		
		
		//by siva 05-07-2019 PRE file encryption
		
		
		
		
		//commondesc.commitTxn(jdbctemplate);
		txManager.commit(transact.status);
		session.setAttribute("preverr", "S");
		String filename = "<span style='color:maroon'>"+prefilelocation+session.getAttribute("PRENAME")+"</span>";
		
		session.setAttribute("prevmsg", "Persionalization file generated successfully under the specified folder.File name : <br/> "+filename+"  ");
		trace("Persionalization file generated successfully under the specified folder.File name[ "+filename+" ].");
		
	}catch(Exception e){ 
		//commondesc.rollbackTxn(jdbctemplate);
		txManager.rollback(transact.status);
		session.setAttribute("preverr", "E");
		session.setAttribute("prevmsg", "Exception : could not continue the download proces...");
		trace("Exception : could not continue the dowload process : " + e.getMessage());				
		e.printStackTrace();
	} 
	
	return this.preDownloadHome();
	
	 //IfpTransObj transObj = commondesc.myTranObject();
}
	
	
*/	
	
	
	public String downInstPREFiles() 
	{
		trace("inside download pre method");
		Properties prop = commondesc.getCommonDescProperty();
		String prefilelocation = prop.getProperty("PREFILELOCATION");
		
		System.out.println("padsadsath"+prefilelocation);
		
		String actname = getRequest().getParameter("submit");
		System.out.println( "action name is " + actname );
		trace( "action name is " + actname );
		String instid =  comInstId(); 
		String usercode = comUserCode();
		String bin = getRequest().getParameter("cardtype");
		trace("cardtype"+bin);
		String prefilename = getRequest().getParameter("prefilename");
		trace("prefilename"+prefilename);
		HttpSession session = getRequest().getSession();	
		String tablename = "INST_PRE_DATA";
		IfpTransObj transact = commondesc.myTranObject("PREDOWNLOAD", txManager);
		
		
		JSONObject listofpreheaders = this.generatePREHeader();
		trace("generatePREHeader");
		JSONObject predbfields = this.generatePREFDBFields();
		trace("generatePREFDBFields");
		ExcelGenerator excelgen = new ExcelGenerator();
		trace("ExcelGenerator");
		try
		{
			trace("inside try");
			if( actname.equals("Delete"))
			{
				String delmsg = null;
				int predel;
				try 
				{
					predel = commondesc.deletePREFiles(instid, bin, prefilename, tablename, jdbctemplate);
					if( predel < 0    ) {
						session.setAttribute("preverr", "E");
						delmsg = "No Records Deleted";
					}else if( predel == 0   ) {
						session.setAttribute("preverr", "E");
						delmsg = "Download the file atleast one time. Then try again to delete";
					}else if( predel > 0 ) {
						delmsg = predel +" Records Deleted successfully ";
						session.setAttribute("preverr", "S");
					}
					System.out.println( "delete rec count is " + predel );
				} catch (Exception e)  {
					session.setAttribute("preverr", "E");
					delmsg = "Error while delete Records " + e ;
					e.printStackTrace();
				}  
				session.setAttribute("prevmsg",  delmsg ); 
				return preDownloadHome();
			} 
			PreprocessAction preprocess = new PreprocessAction();
			int cardcount =  this.checkDownloadCount(bin,prefilename,jdbctemplate );
			
			int preSeqNo = preprocess.sequencePREFILE(instid, jdbctemplate);
			
			trace("Generating presonalization file....excel" +predbfields);
			int x = excelgen.generatePRE(instid, bin, prefilename, tablename, listofpreheaders, predbfields, session, jdbctemplate, commondesc );
			//int x = preprocess.generatePREInst(instid, bin,cardcount, prefilename, tablename, preSeqNo, session, jdbctemplate, commondesc,preprocess );
			trace("Generating presonalization file....got : " + x);
			if( x < 0 ){ 
				return "required_home";
			}
			
			
			int downcnt = updatePreFileDownloadCnt(instid, prefilename, jdbctemplate);
			if( downcnt  < 0 ){
				//commondesc.rollbackTxn(jdbctemplate);
				txManager.rollback(transact.status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Could not continue the download process..");
				trace("Could not update the download count. got rolled back");
			}
			//commondesc.commitTxn(jdbctemplate);
			txManager.commit(transact.status);
			session.setAttribute("preverr", "S");
			String filename = "<span style='color:maroon'>"+prefilelocation+session.getAttribute("PRENAME")+"</span>";
			
			session.setAttribute("prevmsg", "Persionalization file generated successfully under the specified folder.File name : <br/> "+filename+"  ");
			trace("Persionalization file generated successfully under the specified folder.File name[ "+filename+" ].");
			
		}catch(Exception e){ 
			//commondesc.rollbackTxn(jdbctemplate);
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception : could not continue the download proces...");
			trace("Exception : could not continue the dowload process : " + e.getMessage());				
			e.printStackTrace();
		} 
		
		return this.preDownloadHome();
		
		 //IfpTransObj transObj = commondesc.myTranObject();
	}

	
	/*private InputStream inputStreaminst;

	
	public InputStream getInputStreaminst() {
		return inputStreaminst;
	}
	public void setInputStreaminst(InputStream inputStreaminst) {
		this.inputStreaminst = inputStreaminst;
	}*/

	/*private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}*/

	private JSONObject downloadFilePreBDField() {
		JSONObject jsonpre = new JSONObject(); 
		jsonpre.put("H1", "EXPDATE");
		jsonpre.put("H2", "CARD_NO");
		jsonpre.put("H3", "EMB_NAME");
		jsonpre.put("H4", "ENC_NAME");
		jsonpre.put("H5", "TRACK1");
		jsonpre.put("H6", "TRACK2");
		jsonpre.put("H7", "CVV2");
		jsonpre.put("H8", "CVV1");
		jsonpre.put("H9", "SERVICE_CODE");		
		jsonpre.put("H10", "ICVV");
		return jsonpre;
	}	
	/*public String downInstPREFiles()
	{
		trace("***************** downloadFilePreFormat is begin ****************** \n\n");
		enctrace("******************** downloadFilePreFormat is begin ****************** \n\n");
		String actname = getRequest().getParameter("submit");
		trace( "action name is " + actname );
		String instid =  comInstId(); 
		String usercode = comUserCode();
		String bin = getRequest().getParameter("cardtype");
		String prefilename = getRequest().getParameter("prefilename");
		HttpSession session = getRequest().getSession();	
		String tablename = "INST_PRE_DATA";	
		InstCardPREProcess instprocess = new InstCardPREProcess();
		JSONObject listofpreheaders = this.generatePREHeader();
		JSONObject predbfields = this.downloadFilePreBDField();
		ExcelGenerator excelgen = new ExcelGenerator();
		IfpTransObj transact = commondesc.myTranObject("PREDONW", txManager);
		try{
			
			if( actname.equals("Delete"))
			{
				String delmsg = null;
				int predel;
				try 
				{
					predel = commondesc.deletePREFiles(instid, bin, prefilename, tablename, jdbctemplate);
					if( predel < 0    ) {
						session.setAttribute("preverr", "E");
						delmsg = "No Records Deleted";
					}else if( predel == 0   ) {
						session.setAttribute("preverr", "E");
						delmsg = "Download the file atleast one time. Then try again to delete";
					}else if( predel > 0 ) {
						txManager.commit(transact.status);
						delmsg = predel +" Records Deleted successfully ";
						session.setAttribute("preverr", "S");
					}
					System.out.println( "delete rec count is " + predel );
				} catch (Exception e)  {
					txManager.rollback(transact.status);
					session.setAttribute("preverr", "E");
					delmsg = "Error while delete Records " + e ;
					e.printStackTrace();
				}  
				session.setAttribute("prevmsg",  delmsg ); 
				return this.preDownloadHome();
			} 
			
			trace("Generating presonalization file....");
			int x = excelgen.generatePRE(instid, bin, prefilename, tablename, listofpreheaders, predbfields, session, jdbctemplate, commondesc );
			trace("Generating presonalization file....got : " + x);
			if( x < 0 ){ 
				return "required_home";
			}
			Properties prop = commondesc.getCommonDescProperty();
			String prefilelocation = prop.getProperty("PREFILELOCATION");
			fileName = prefilename.replaceAll(" ", "_");
			String predtls[] = prefilename.split("_");
			String predate = predtls[2];
			String datesplit = predate.substring(0,4);
			String year =  predate.substring(6,8);
			predate = datesplit+year;
			enctrace("prefilename--->"+prefilename);
			String username=comUsername();
			
			 //fileInputStream = new FileInputStream(new File("C:\\temp\\"+id+"\\"+filename));
			inputStreaminst = new FileInputStream(new File(prefilelocation+"_"+predate+"_"+prefilename+".pre"));
			int downcnt =instprocess.updatePersonalizePreFileDownloadCnt(instid, prefilename, jdbctemplate);
			if( downcnt  < 0 ){
				txManager.rollback(transact.status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Could not continue the download process..");
				trace("Could not update the download count. got rolled back");
			}
			
			trace("update PRE seqno ...");
			int preseq = jdbctemplate.update(this.updatePRESEQNO(instid));
			
			if( preseq  < 0 ){
				txManager.rollback(transact.status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Could not continue the download process..");
				trace("Could not update the PRE SEQ NO got rolled back");
			}
			 
			session.setAttribute("preverr", "S");
			String filename = "<span style='color:maroon'>"+session.getAttribute("PRENAME")+"</span>";
			
			//session.setAttribute("prevmsg", "Personalization file downloaded successfully  <br/> "+filename+"  ");
			trace("Persionalization file generated successfully under the specified folder.File name[ "+prefilename+" ].");
			
			
			
			
			txManager.commit(transact.status);
			
			*//*************AUDIT BLOCK Edited by sardar on 11-12-15**************//* 
			try{ 
				auditbean.setActmsg("Personalization File [ "+prefilename+" ] Downloaded");
				auditbean.setUsercode(username);
				auditbean.setAuditactcode("0104"); 
				auditbean.setPrefilename((String)session.getAttribute("PRENAME"));
				auditbean.setProduct(bin);
				//commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
				commondesc.insertAuditTrailPendingCommit(instid, usercode, auditbean, jdbctemplate, txManager);
			 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
			 *//*************AUDIT BLOCK Ended By sardar on 11-12-15**************//*
			
		}catch(Exception e){ 
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception : could not continue the download proces...");
			trace("Exception : could not continue the dowload process : " + e.getMessage());				
			e.printStackTrace();
		} 
		
		return "predownloadinst";		
		 //IfpTransObj transObj = commondesc.myTranObject();
		
	}*/
	
	public synchronized String updatePRESEQNO(String instid)
	{
		return "update SEQUENCE_MASTER set PREFILE_SEQ=PREFILE_SEQ+1 where INST_ID='"+instid+"'";
	}
	
	private JSONObject generatePREHeader() {
		JSONObject jsonpre = new JSONObject();
		jsonpre.put("H1", "SerialNo");
		jsonpre.put("H2", "CardNo");
		jsonpre.put("H3", "EmbName");
		jsonpre.put("H4", "EncName");
		jsonpre.put("H5", "Track1");
		jsonpre.put("H6", "Track2");
		jsonpre.put("H7", "CVV2");
		jsonpre.put("H8", "ICVV");
		jsonpre.put("H9", "ServiceCode");
		jsonpre.put("H10", "ExpiryDate");
		jsonpre.put("H11", "CountryCode");
		jsonpre.put("H12", "CurrencyCode");
		jsonpre.put("H13", "CurrencyExponent");
		jsonpre.put("H14", "ValidFrom");
		jsonpre.put("H15", "ToDate");
		jsonpre.put("H16", "AppName");
		jsonpre.put("H17", "PANSeqNo"); 
		jsonpre.put("H18", "CardRefNumber"); 
		jsonpre.put("H19", "EmbCardNo"); 
		//jsonpre.put("H20", "CIN");
		jsonpre.put("H20", "BRANCH_NAME");
		jsonpre.put("H21", "CARD_TYPE");
		//jsonpre.put("H21", "BRANCH_CODE");
		//jsonpre.put("H22", "BRANCH_NAME");
		return jsonpre;
	}
	
	private JSONObject generatePREFDBFields()  {
		JSONObject jsonpre = new JSONObject(); 
		jsonpre.put("H1", "SLNO");
		jsonpre.put("H2", "CARD_NO");
		jsonpre.put("H3", "EMB_NAME");
		jsonpre.put("H4", "ENC_NAME");
		jsonpre.put("H5", "TRACK1");
		jsonpre.put("H6", "TRACK2");
		jsonpre.put("H7", "CVV2");
		jsonpre.put("H8", "ICVV");
		jsonpre.put("H9", "SERVICE_CODE");
		jsonpre.put("H10", "EXPDATE");
		jsonpre.put("H11", "COUNTRY_CODE");
		jsonpre.put("H12", "CURRENCY_CODE");
		jsonpre.put("H13", "CURRENCY_EXPONENET");
		jsonpre.put("H14", "VALID_FROM");
		jsonpre.put("H15", "VALID_TO");
		jsonpre.put("H16", "APP_NAME");
		jsonpre.put("H17", "PAN_SEQNO"); 
		jsonpre.put("H18", "CARD_REFNO"); 
		jsonpre.put("H19", "EMB_CARDNO");
		jsonpre.put("H20", "CIN");
		jsonpre.put("H21", "BRANCH_CODE");
		jsonpre.put("H22", "BRANCH_NAME");
		return jsonpre;
	}
	// by siva 
	
		public static boolean isWindows() {

			return (OS.indexOf("win") >= 0);

		}

		public static boolean isMac() {

			return (OS.indexOf("mac") >= 0);

		}

		public static boolean isUnix() {

			return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
			
		}

		public static boolean isSolaris() {

			return (OS.indexOf("sunos") >= 0);

		}
		
		// by siva 
		
		
		public int deletepre(String instid, String prefilename, JdbcTemplate jdbctemplate) throws Exception 
		{
			int x = -1;
			/*String delteqry = "DELETE FROM INST_PRE_DATA WHERE INST_ID='"+instid+"' AND PRE_NAME='"+prefilename+"'";
			enctrace("delete query -->"+delteqry);
			x = jdbctemplate.update(delteqry);*/
			
			
			///by gowtham-280819
			String delteqry = "DELETE FROM INST_PRE_DATA WHERE INST_ID=? AND PRE_NAME=? ";
			enctrace("delete query -->"+delteqry);
			x = jdbctemplate.update(delteqry,new Object[]{instid,prefilename});
			return x; 
		}
	
}
 