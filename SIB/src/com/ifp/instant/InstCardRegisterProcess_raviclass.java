package com.ifp.instant;

import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import oracle.sql.DATE;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.ByteSource;
import org.apache.sanselan.common.ByteSourceFile;
import org.apache.sanselan.formats.jpeg.JpegImageParser;
import org.apache.sanselan.formats.jpeg.segments.UnknownSegment;
import org.json.JSONObject;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.google.common.base.Splitter;
import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.beans.CardActivationBeans;
import com.ifp.beans.InstCardRegisterProcessBeans;
import com.ifp.dao.CardActivationDAO;
import com.ifp.dao.InstCardActivateProcessDAO;
import com.ifp.dao.InstCardRegisterProcessDAO;
import com.ifp.dao.PinGenerationDAO;
import com.ifp.personalize.PersonalCardReceiveIssueAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.CustomerBean;
import com.ifp.util.IfpTransObj;
import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;

import org.json.JSONObject;

import pin.safenet.HsmTcpIp;
import pin.safenet.OtpSmsCall;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import connection.CBSConnection;
import connection.Dbcon;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
public class InstCardRegisterProcess_raviclass extends BaseAction 
{ 
	private static final long serialVersionUID = -8376161637970676446L;
	
	AuditBeans auditbean = new AuditBeans();
	OtpSmsCall Smscallobj=new OtpSmsCall();
	
	PersonalCardReceiveIssueAction persisspro= new PersonalCardReceiveIssueAction();
	public AuditBeans getAuditbean() {
		return auditbean;
	}
	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
	}
	private String act;
	
	public String getAct() {
		return act;
	}
	public void setAct(String act) {
		this.act = act;
	}
	CommonUtil comutil= new CommonUtil(); 
	CommonDesc commondesc = new CommonDesc();
	InstCardRegisterProcessDAO cardregdao = new InstCardRegisterProcessDAO();
	InstCardRegisterProcessBeans cardregbean = new InstCardRegisterProcessBeans();
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	
	CardActivationDAO cardactdao = new CardActivationDAO();
	CardActivationBeans cardactbean = new CardActivationBeans();
	public CardActivationBeans getCardactbean() {
		return cardactbean;
	}
	public void setCardactbean(CardActivationBeans cardactbean) {
		this.cardactbean = cardactbean;
	}
	public CardActivationDAO getCardactdao() {
		return cardactdao;
	}
	public void setCardactdao(CardActivationDAO cardactdao) {
		this.cardactdao = cardactdao;
	}
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

	
	private List branchlist;
	
	private List prodlist;
	
	
	
	
	
	public List getBranchlist() {
		return branchlist;
	}
	public void setBranchlist(List branchlist) {
		this.branchlist = branchlist;
	}
	public List getProdlist() {
		return prodlist;
	}
	
	public void setProdlist(List prodlist) {
		this.prodlist = prodlist;
	}
	
	public File uploadedphoto; 
	private String uploadedphotoContentType;
	public File uploadsignature; 
	private String uploadsignatureContentType;
	public File uploadidproof; 
	private String uploadidproofContentType;
	
	private static Boolean  initmail = true; 
	private static  String parentid = "000";
	final String regallowedstatus = "09";
	final String nextstatus = "09";
	
	public List acctproduct;
	public List Currency;
	
	public List getAcctproduct() {
		return acctproduct;
	}
	public void setAcctproduct(List acctproduct) {
		this.acctproduct = acctproduct;
	}
	public List getCurrency() {
		return Currency;
	}
	public void setCurrency(List currency) {
		Currency = currency;
	}
	private String cardno;
	
public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
/*public DriverManagerDataSource getConnection(){
		
		DriverManagerDataSource datasource = new DriverManagerDataSource();
		datasource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
		datasource.setUrl("jdbc:oracle:thin:@172.16.10.14:1521:orcl");
		datasource.setUsername("dbbl_cbs");
		datasource.setPassword("dbbl_cbs");
		return datasource;
	}*/
        Dbcon dbcon = new Dbcon();

	DataSource ds = Dbcon.getConnection();
	private JdbcTemplate jdbctemplate2 = new JdbcTemplate(ds);
	
	
	final String CARDACTIVATEDCODE=CommonDesc.ACTIVECARDSTATUS;
	public CommonUtil getComutil() {
		return comutil;
	}
	public void setComutil(CommonUtil comutil) {
		this.comutil = comutil;
	}
	public CommonDesc getCommondesc() {
		return commondesc;
	}
	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}
	
	public InstCardRegisterProcessDAO getCardregdao() {
		return cardregdao;
	}
	public void setCardregdao(InstCardRegisterProcessDAO cardregdao) {
		this.cardregdao = cardregdao;
	}
	public InstCardRegisterProcessBeans getCardregbean() {
		return cardregbean;
	}
	public void setCardregbean(InstCardRegisterProcessBeans cardregbean) {
		this.cardregbean = cardregbean;
	} 
	public File getUploadedphoto() {
		return uploadedphoto;
	}
	public void setUploadedphoto(File uploadedphoto) {
		this.uploadedphoto = uploadedphoto;
	}
	
	public String getUploadedphotoContentType() {
		return uploadedphotoContentType;
	}
	public void setUploadedphotoContentType(String uploadedphotoContentType) {
		this.uploadedphotoContentType = uploadedphotoContentType;
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
	
	public String comUsername1(){
		HttpSession session = getRequest().getSession();
		String username = (String)session.getAttribute("USERNAME"); 
		
		return username;
	}
	
	
	public String comBranchId(){
		HttpSession session = getRequest().getSession();
		String br_id = (String)session.getAttribute("BRANCHCODE"); 
		return br_id;
	}
	
	
	public void validate(){ 
		 int allowedsize_mb = 10;
		 long allowedsize_bits = allowedsize_mb * 1024 * 1024;
		 if(getUploadedphoto() !=null){ 
			 trace( "uploaded size " + uploadedphoto.length() );
			
			 if(getUploadedphoto().length()> allowedsize_bits ){    
				trace( "validated....Could not register. Uploade Photo size is too large ! Select less than 2 MB file" );
				addActionError("Could not register. Uploade Photo size is too large ! Select less than 2 MB file");
			} 
			 
			if(!getUploadedphotoContentType() .contains("image")) {
				addActionError("Could not register.Uploaded photo is not valid format..");
				trace("Could not register.Uploaded photo is not valid format ..");
			} 
		}
		 
		 if(getUploadsignature() !=null){ 
			 trace( "uploaded signature image size : " + uploadsignature.length() ); 
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
			 trace( "uploaded id proof size : " + uploadidproof.length() ); 
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
	
	// ********************Edited by sardar ******************************//
	
	
	public String findreissucardsbin() { 
		trace("Reissu Instant card register ...\n");
		enctrace("Reissu Instant card register ...\n");
		HttpSession session = getRequest().getSession();
		String instid =  comInstId(); 
		/*if (comBranchId().equals("000")){
				session.setAttribute("preverr", "E"); 
				session.setAttribute("prevmsg", " Please Login With branch User ..."  );
				trace("Please Login With branch User ...");
				return "required_home";
			}*/
		
		List prodlist;
		try {
			prodlist = commondesc.getProductListView(instid, jdbctemplate, session );
			System.out.println( "sardar__" + prodlist );
			
		//	String productcode="",prodesc="";
			//Iterator secitr = prodlist.iterator();
			
					
					 setProdlist(prodlist);
			   
				
		
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return "InstantReissuecardsbinwise"; 
	}
	
	
	public String ReissuregisterInstCardHome() { 
		trace("Reissu Instant card register ...\n");
		enctrace("Reissu Instant card register ...\n");
		HttpSession session = getRequest().getSession();
		String instid = comInstId();
		String cardtype = getRequest().getParameter("cardtype").trim();
		
	
		/*if (comBranchId().equals("000")){
			
				session.setAttribute("preverr", "E"); 
				session.setAttribute("prevmsg", " Please Login With branch User ..."  );
				trace("Please Login With branch User ...");
				return "required_home";
			}*/
	
		cardregbean.setMsterbin(cardtype.substring(0, 6));
		if(cardtype.substring(0, 6).equals("551270")){//
			
			return "inatntreissuehomeorderbase";
		}
		else{
			//cardregbean.setPropriatorybin(cardtype.substring(0, 6));
			return "reissuinsregcard_home"; 
		}
		
	}
	
	
	public List getcustdetails( String instid, String chn, JdbcTemplate jdbctemplate  )  throws Exception  {
		List custdetail  = null;
		
		/*String custdetailsqry = " SELECT * from CARD_PRODUCTION where inst_id='"+instid+"' and CARD_NO='"+chn+"'"; 
		enctrace( "getcarddetailscustomermap : " + custdetailsqry ); 
		custdetail = jdbctemplate.queryForList(custdetailsqry); */
		
		///by gowtham-280819
				String custdetailsqry = " SELECT * from CARD_PRODUCTION where inst_id=? and CARD_NO=?"; 
				enctrace( "getcarddetailscustomermap : " + custdetailsqry ); 
				custdetail = jdbctemplate.queryForList(custdetailsqry,new Object[]{instid,chn}); 
		
		return custdetail;
	}
	
	public String ReissuCustReg() {
		trace("************Reissu Customer Reisteration Home **************\n\n");
		enctrace("************Reissu Customer Reisteration Home **************\n\n");
		
		HttpSession session = getRequest().getSession();
		String instid = comInstId();
		String branchid=comBranchId();
		String msterorprobbin = getRequest().getParameter("msterorprobbin").trim();
		
		//String br_id = (String)session.getAttribute("BRANCHCODE"); 
		System.out.println("keyid::");
		String card_no = getRequest().getParameter("cardno").trim();
		System.out.println("card_noaaa::"+card_no);
		String accountno = getRequest().getParameter("accountno").trim();
		String processtype = "INSTANT";
		Boolean kyc = false;
		String regtype = getRequest().getParameter("regtype").trim();
		String custid = "";
		String subprodid = "";
		String subproddesc ="";
		String actiontype = "EDIT";
		String apptype = (String)session.getAttribute("APPLICATIONTYPE");
		StringBuffer hcardno = new StringBuffer();
		String getjsonvalues="";
		JSONObject js = null;
		String name="",mobileno="",customerid="",acctcur="",dob="",addr1="",addr2="",addr3="";
		int branch_code;
		try{
			
			String act = getRequest().getParameter("act");
			if( act != null ){
				session.setAttribute("act", act);
			}
			
			
			String keyid = "", ecardno ="";
			PadssSecurity padsssec = new PadssSecurity();
			//if(padssenable.equals("Y"))
			//{
				
				keyid ="1"; //commondesc.getSecurityKeyid(instid, jdbctemplate);
				System.out.println("keyid::"+keyid);
				List secList = commondesc.getPADSSDetailById(keyid,jdbctemplate);
				//System.out.println("secList::"+secList);  
				
				Properties props=getCommonDescProperty();
				String EDPK=props.getProperty("EDPK");
				
				Iterator secitr = secList.iterator();
				if(!secList.isEmpty()){
					while(secitr.hasNext())
					{
						Map map = (Map) secitr.next(); 
						String CDMK = ((String)map.get("DMK"));
						//String eDPK = ((String)map.get("DPK"));
						String CDPK=padsssec.decryptDPK(CDMK, EDPK);
						hcardno = padsssec.getHashedValue(card_no+instid);
						ecardno = padsssec.getECHN(CDPK,card_no);	
					}      
				}
				//} 
			
				String maskedchn=padsssec.getMakedCardno(card_no);	
				System.out.println(branchid+"decrypted card number for encod and maintanance"+ecardno+"clear chn value"+hcardno);
			
				
				int cardavailabe = commondesc.getReissuvalidcardavailable(instid,hcardno.toString(),jdbctemplate);
				
				System.out.println("sassas"+cardavailabe);
				if(cardavailabe == 0 || card_no.length() !=16 ){
				
				//if (!branchid.equals(validbranch)){
					
					
					session.setAttribute("preverr", "E"); 
					session.setAttribute("prevmsg", " Entered CardNo Is "+card_no+", Enter Correct Card Number/Card is not available .."  );
					trace("[ "+ card_no +" ]CCard Collect Branch not Configured. ..."+cardavailabe);
					return "required_home";
				//}	
				}
				
				
				String validbranch = commondesc.getReissuvalidbranch(instid,hcardno.toString(),jdbctemplate);
				if(validbranch==null ){
				//if (!branchid.equals(validbranch)){
					
					System.out.println(branchid+"decrypted card"+validbranch);
					session.setAttribute("preverr", "E"); 
					session.setAttribute("prevmsg", " "+maskedchn+" Card is Available / Card Collect Branch not Available .."  );
					trace("[ "+ card_no +" ]Card Collect Branch not Configured. ...");
					return "required_home";
				//}	
				}
				
				
				trace("Got validbranch: " + validbranch );
				
				int cardrequired = cardregdao.cardstatusforReissu(instid,hcardno.toString(), jdbctemplate);
				trace("Got regreq: " + cardrequired );
				
			if(cardrequired ==2){
					session.setAttribute("prevmsg", " "+maskedchn+"  Card is  Already Reissued With New Card Number.. " );
					session.setAttribute("preverr", "E"); 
					trace("[ "+ card_no +" ] Card is  Already Reissued With New Card Number..");
					return "required_home";
				}
				
			if( cardrequired != 1 ){
				session.setAttribute("prevmsg", " "+maskedchn+"  Card is  Active, Mark As Lost/Stolen For Mapping another card number"  );
				session.setAttribute("preverr", "E"); 
				trace("[ "+ card_no +" ]Card is  Active, Mark As Lost/Stolen For Mapping another card number..");
				return "required_home";
			}																										
				
				
				List carddet = cardregdao.getReissucarddetails(instid,hcardno.toString(),jdbctemplate);
				trace("cardforcbs"+carddet);
				if(carddet.isEmpty())
				{
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg",  "Could not fetch the details from the insatmcms...");   
				}
				Iterator itrat = carddet.iterator();
				
					while(itrat.hasNext())
					{
						Map map = (Map) itrat.next(); 
						
						int WDLAMT=((BigDecimal)map.get("WDL_AMT")).intValue();
						int WDLCNT=((BigDecimal)map.get("WDL_CNT")).intValue();
						int PURAMT=((BigDecimal)map.get("PUR_AMT")).intValue();
						int PURCNT=((BigDecimal)map.get("PUR_CNT")).intValue();
						
					
						cardregbean.setWdl_lmt_amt(WDLAMT);
						cardregbean.setOrder_ref_no((String)map.get("ORDER_REF_NO"));
	         			cardregbean.setChn_msk((String)map.get("MCARD_NO"));
						cardregbean.setHascard((String)map.get("HCARD_NO"));
						cardregbean.setEncrptchn((String)map.get("CARD_NO"));
						cardregbean.setOrg_chn((String)map.get("CHN"));
						//System.out.println("orgchn"+(String)map.get("CHN"));
						
						cardregbean.setWdl_lmt_cnt(WDLCNT);
						cardregbean.setPur_lmt_amt(PURAMT);
						cardregbean.setPur_lmt_cnt(PURCNT);
												
						cardregbean.setLmt_based_on((String)map.get("LMT_BASED_ON"));
						cardregbean.setProd_code((String)map.get("BIN"));
						cardregbean.setBranchcode((String) map.get("CARD_COLLECT_BRANCH"));
						cardregbean.setCod_prod(((String) map.get("SUB_PROD_ID").toString().trim()));
						
						String cardno=(String)map.get("CARD_NO");
						String productdesc = commondesc.getProductdesc(instid,(String) map.get("PRODUCT_CODE").toString().trim(), jdbctemplate);
						trace("productdesc==> "+productdesc);
						cardregbean.setProductdesc(productdesc);
						String cardtypedesc = commondesc.getCardTypeDesc(instid, (String)map.get("BIN"), jdbctemplate);
						cardregbean.setCardtypedesc(cardtypedesc);
						
						cardregbean.setCardtype(Integer.parseInt((String) map.get("CARD_TYPE_ID").toString().trim()));
						
						//getjsonvalues = this.getcustdetails(instid, cin,orderrefno,jdbctemplate);
						//js = new JSONObject(getjsonvalues);
						cardregbean.setCustname((String)map.get("FNAME"));
						cardregbean.setPhoneno((String)map.get("MOBILE"));				
						cardregbean.setCustidno((String)map.get("CIN"));
					    cardregbean.setCard_ccy((String)map.get("ACC_CCY"));
						cardregbean.setDob((String)map.get("DOB"));
						cardregbean.setPaddress1((String)map.get("P_PO_BOX"));
						cardregbean.setPaddress2((String)map.get("P_HOUSE_NO"));
						cardregbean.setPaddress3((String)map.get("P_STREET_NAME"));
						cardregbean.setPaddress4((String)map.get("P_WARD_NAME"));
						//cardregbean.setPaddress5(js.getString("P_CITY"));
						cardregbean.setPaddress6((String)map.get("P_DISTRICT"));
						cardregbean.setCity((String)map.get("P_CITY"));				
						//cardregbean.setCountrydesc(js.getString("Country code"));
						cardregbean.setAccountno((String)map.get("ACCOUNT_NO"));
						
												
																		
					}   
					setCardno(card_no);
				/*	int regreq = cardregdao.checkcustomeridexist(instid,accountno, jdbctemplate);
					trace("Got regreq: " + regreq );
					if( regreq > 0 ){
						//subproddesc = commondesc.getSubProductdesc(instid, subprodid, jdbctemplate);
						session.setAttribute("preverr", "E"); 
						session.setAttribute("prevmsg",  "[ "+ accountno +" ] Account Already Exist. Registeration not required"  );
						trace("[ "+ accountno +" ] Account Number Alredy Exist. Registeration not required");
						return "required_home";
					}*/
				
																  
			
		}catch( Exception e ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg","Exception : Could not continue the process");
			e.printStackTrace();
			trace("Exception : Could not continue the process..." + e.getMessage() );
			return "required_home";
		}
		cardregbean.setMsterbin( getRequest().getParameter("msterorprobbin"));
		
		
         if(msterorprobbin.equals("551270")){//
			
			return "inatntreissuehomeorderbase";
		}
		else{
			//cardregbean.setPropriatorybin(cardtype.substring(0, 6));
			return "reissuinsregcard_home"; 
		}
		//return "inscustomer_view";
		
	}
	
	
	@SuppressWarnings("unused")
	public String SaveReissuCustomerReg() throws Exception
	  {
		trace("********Save Reissue customer details.....*************\n");
		enctrace("********Save Reissue customer details.....*************\n");
		 String instid = comInstId();
		// String branchid=
		 IfpTransObj trasact = commondesc.myTranObject("CustomerReisuueRegister", txManager);
		 String acctnumber="",accttypevalue="",accttype="";
		String username=	comUsername();
		String usercode =	comUserCode();
		HttpSession session = getRequest().getSession();
		StringBuffer newhashcard = new StringBuffer();
			String loststolencard=getRequest().getParameter("cardno");
		
			String oldorgchn=getRequest().getParameter("orgchn");
		
			
			cardregbean.setOrg_chn(oldorgchn);
		//	String newcardno=getRequest().getParameter("newcardno");
			
			System.out.println("newcardnodada::"+"oldorgchn"+oldorgchn);
			String keyid = "", ecardno ="";
			PadssSecurity padsssec = new PadssSecurity();
			//if(padssenable.equals("Y"))
			//{
			String newcardno="" ,oldhcardno="" ;
			trace("masterbin "+getRequest().getParameter("msterorprobbin"));
			
			//added by gowtham_220719
			String  ip=(String) session.getAttribute("REMOTE_IP");


			
		if(	getRequest().getParameter("orderrefno") !=null){
				
				trace("orderrefNumber base"+getRequest().getParameter("msterorprobbin"));
				String orderno=getRequest().getParameter("orderrefno");
			//	System.out.println("getRequest().getParameter"+getRequest().getParameter("msterorprobbin"));
				
				String validorderformaping = commondesc.getorderrefstaus(instid,getRequest().getParameter("orderrefno"),jdbctemplate);
				
				if((validorderformaping==null)||(validorderformaping.equals(""))){
					session.setAttribute("preverr", "E"); 
					session.setAttribute("prevmsg", "   Entered  Order Ref  Number is "+orderno+" ,Is Not Available... "  );
					trace("Entered New  Order Ref Number is "+getRequest().getParameter("newcardno")+" Waiting for the PinGeneration..");
					return "required_home";
				}
				
				 if (!(validorderformaping.equals(""))||(validorderformaping !=null)){
					
					 
					 List listvalfornewcard= commondesc.newcardnofororder(instid,orderno,jdbctemplate);
		  	          Iterator refandcrdbranch = listvalfornewcard.iterator();
		  	          String nehcardno="",newcardcollectbranch="",bin="";
						if(!listvalfornewcard.isEmpty()){
					while(refandcrdbranch.hasNext())
						{
							Map map1 = (Map) refandcrdbranch.next(); 
							nehcardno = ((String)map1.get("HCARD_NO"));
							 newcardcollectbranch = ((String)map1.get("CARD_COLLECT_BRANCH"));
							 bin = ((String)map1.get("BIN"));

					}      
					  }
						
						
						
						else if(validorderformaping.equals("01P")){
							session.setAttribute("preverr", "E"); 
							session.setAttribute("prevmsg", "   Entered  Order Ref  Number is "+orderno+" ,Waiting for the PinGeneration "  );
							trace("Entered New  Order Ref Number is "+getRequest().getParameter("newcardno")+" Waiting for the PinGeneration..");
							return "required_home";
						}
						else if (validorderformaping.equals("02M")){
							
								session.setAttribute("preverr", "E"); 
								session.setAttribute("prevmsg", "   Entered Order Ref  Number is "+orderno+" ,Waiting for the PinGeneration Authorization. "  );
								trace("Entered New Card Number is "+getRequest().getParameter("newcardno")+" Enter 16 digit Valid Card Number...");
								return "required_home";
							}
						else if (validorderformaping.equals("02P")){
							
							session.setAttribute("preverr", "E"); 
							session.setAttribute("prevmsg", "   Entered Order Ref  Number is "+orderno+" ,Waiting for the PRE Generation. "  );
							trace("Entered New Card Number is "+getRequest().getParameter("newcardno")+" Enter 16 digit Valid Card Number...");
							return "required_home";
						}else if (validorderformaping.equals("03M")){
							
							session.setAttribute("preverr", "E"); 
							session.setAttribute("prevmsg", "   Entered Order Ref  Number is "+orderno+" ,Waiting for the PRE Authorization. "  );
							trace("Entered New Card Number is "+getRequest().getParameter("newcardno")+" Enter 16 digit Valid Card Number...");
							return "required_home";
						}
						else if (validorderformaping.equals("04M")){
							session.setAttribute("preverr", "E"); 
							session.setAttribute("prevmsg", "   Entered Order Ref  Number is "+orderno+" ,Issued  To the Branch And Waiting for Authorization . "  );
							trace("Entered Order Ref Number is "+getRequest().getParameter("newcardno")+" Enter 16 digit Valid Card Number...");
							return "required_home";
						}
						else if (validorderformaping.equals("04P")){
							session.setAttribute("preverr", "E"); 
							session.setAttribute("prevmsg", "   Entered Order Ref  Number is "+orderno+" ,Ready for the Issuance To Braches . . "  );
							trace("Entered Order Ref Number is "+getRequest().getParameter("newcardno")+" Enter 16 digit Valid Card Number...");
							return "required_home";
						}
								
						
						if(!bin.equals("551270"))
						{
							session.setAttribute("preverr", "E"); 
							session.setAttribute("prevmsg", "   Entered  Order Ref  Number is "+orderno+" , not Master Card Bin"  );
							trace("Entered  Order Ref  Number is "+orderno+" , not Master Card Bin");
							return "required_home";	
						}
						
						
				//		System.out.println("nehcardno::"+nehcardno+"newcardcollectbranch"+newcardcollectbranch); 
						else  if((nehcardno !=null )&&(newcardcollectbranch ==null )){
	  	            	
	  	            	session.setAttribute("preverr", "E"); 
						session.setAttribute("prevmsg", " "+orderno+"Order Number  is available & Card Collect Branch is not Available ...  " );
						trace("[ "+ orderno +" ]"+orderno+"  Card Collect Branch is not AVailable ");
						return "required_home";
	  	            }
	  	           // System.out.println("newhashcard::"+nehcardno+"ecardno"+ecardno+"\n newcardcollectbranch "+newcardcollectbranch+"\n  comBranchId"+comBranchId());
	  	            else  if (!(newcardcollectbranch.equals(comBranchId()))){
					
						session.setAttribute("preverr", "E"); 
						session.setAttribute("prevmsg", " "+orderno+" Order Number  is not Valid to map ..Card Collect Branch is '"+newcardcollectbranch+"' " );
						trace("[ "+ newcardno +" ]"+orderno+"  Card Collect Branch is '"+newcardcollectbranch+"'");
						return "required_home";
					}	
					
					
						cardregbean.setHascard(nehcardno.toString());
						 cardregbean.setCardno(nehcardno.toString()); 
				}
				
				
				
			}
			
			else if(getRequest().getParameter("newcardno")!=null){
				trace("card number base");
				newcardno=getRequest().getParameter("newcardno");
				if(getRequest().getParameter("newcardno").length() !=16){
					session.setAttribute("preverr", "E"); 
					session.setAttribute("prevmsg", "   Entered New Card Number is "+newcardno+" ,Enter 16 digit Valid Card Number... "  );
					trace("Entered New Card Number is "+getRequest().getParameter("newcardno")+" Enter 16 digit Valid Card Number...");
				
				}
			
			
				keyid ="1"; //commondesc.getSecurityKeyid(instid, jdbctemplate);
				System.out.println("keyid::"+keyid);
				List secList = commondesc.getPADSSDetailById(keyid,jdbctemplate);
				//System.out.println("secList::"+secList);  
				
				Properties props=getCommonDescProperty();
			  String 	EDPK=props.getProperty("EDPK");
				
				Iterator secitr = secList.iterator();
				if(!secList.isEmpty()){
					while(secitr.hasNext())
					{
						Map map = (Map) secitr.next(); 
						String CDMK = ((String)map.get("DMK"));
						//String eDPK = ((String)map.get("DPK"));
						String CDPK=padsssec.decryptDPK(CDMK, EDPK);
						newhashcard = padsssec.getHashedValue(newcardno+instid);
						ecardno = padsssec.getECHN(CDPK, newcardno.toString());
						
						
					}      
				}
	 			//} 
				cardregbean.setHascard(newhashcard.toString());
				 cardregbean.setCardno(newhashcard.toString());
				 
  	            List listvalfornewcard= commondesc.getcardcollectbranch(instid,newhashcard.toString(),jdbctemplate);
	  	          Iterator refandcrdbranch = listvalfornewcard.iterator();
	  	          String orderfornewcard="",newcardcollectbranch="";
					if(!listvalfornewcard.isEmpty()){
					while(refandcrdbranch.hasNext())
					{
						Map map1 = (Map) refandcrdbranch.next(); 
						 orderfornewcard = ((String)map1.get("ORDER_REF_NO"));
						 newcardcollectbranch = ((String)map1.get("CARD_COLLECT_BRANCH"));
						
						
					}      
				}
  	            
  	            System.out.println("newhashcard::"+newhashcard+"ecardno"+ecardno+"\n"+newcardcollectbranch);
  	            
  	            if(newcardcollectbranch==null){
  	            	session.setAttribute("preverr", "E"); 
					session.setAttribute("prevmsg", " "+newcardno+"Card is not available/Card Collect Branch is not configured ...  " );
					trace("[ "+ newcardno +" ]"+newcardno+"  Card Collect Branch is not configured ");
					return "required_home";
  	            }
  	            
  	            if (!(newcardcollectbranch.equals(comBranchId()))){
				
					session.setAttribute("preverr", "E"); 
					session.setAttribute("prevmsg", " "+newcardno+" Card is not Valid to map ..Card Collect Branch is '"+newcardcollectbranch+"' " );
					trace("[ "+ newcardno +" ]"+newcardno+"  Card Collect Branch is '"+newcardcollectbranch+"'");
					return "required_home";
				}	
				
				
				
				int reissustatusupdate =commondesc.ChangeCustomerStatusReIssueDAO(instid,oldhcardno,"62",jdbctemplate);
				//System.out.println("dsssssss"+reissustatusupdate);
				if(reissustatusupdate < 0){
					
					session.setAttribute("preverr", "E"); 
					session.setAttribute("prevmsg", "Lost/Stolen card is mismatching from DB"  );
					trace("[ "+ loststolencard +" ]Lost/Stolen card is mismatching from DB....");
					return "required_home";
					
				}
				if(oldhcardno.equals(newhashcard)){
					
					session.setAttribute("preverr", "E"); 
					session.setAttribute("prevmsg", "Lost/Stolen card and  Enterd card number is same  .."  );
					trace("[ "+ loststolencard +" ]Lost/Stolen card and  Enterd card number is same  ....");
					return "required_home";
				}
				int validreissucard= commondesc.getcardreissucardvalid(instid,newhashcard.toString(),jdbctemplate);
				
				if (validreissucard < 0){
					
				
					session.setAttribute("preverr", "E"); 
					session.setAttribute("prevmsg", " "+newcardno+"  Card is not Valid to map . ."  );
					trace("[ "+ newcardno +" ]"+newcardno+"  Card is not Valid to map . .");
					return "required_home";
				}	
				
			}
			 //System.out.println("hash card"+newhashcard);
		
			 oldhcardno = (String)getRequest().getParameter("hashcard");
			 cardregbean.setCustname((String)getRequest().getParameter("customername"));
			
			String branchcode= getRequest().getParameter("branchcode");
			System.out.println("branchcode"+branchcode);
			int bracnh=Integer.parseInt(branchcode);
			 cardregbean.setBranch_code(bracnh);
			 
			 acctnumber = (String)getRequest().getParameter("accountno");
			 
			 cardregbean.setAccountno((String)getRequest().getParameter("accountno"));
			 
			 
			 if (acctnumber.length() ==14){
				 
				 accttypevalue = acctnumber.substring(4, 6);				 
			 }else{
				 
				 accttypevalue = acctnumber.substring(5, 7);
			 }
			 
			accttype = commondesc.getAcctTypeValue(instid,accttypevalue,jdbctemplate);
			 
			cardregbean.setAccttype(accttype);
			
		 	
			 
				String copro=	 (String)getRequest().getParameter("codeprod");
			  //int codeprod=Integer.parseInt(copro);
					 cardregbean.setCod_prod(copro);
					 String cusid=	 (String)getRequest().getParameter("customerid");
					 System.out.println("custid"+cusid);
					 //int custid=Integer.parseInt(cusid);
		   
					 //cardregbean.setCustid(cusid); 
		     cardregbean.setCustidno(cusid);
	    	 cardregbean.setProd_code((String)getRequest().getParameter("productcode"));
		 
		     cardregbean.setUsercode(usercode);
		     cardregbean.setUsername(username);
		     
		      String WDLLMTAMT= (String) getRequest().getParameter("wdl_lmt_amt");
		      int WDL_LMT_AMT=Integer.parseInt(WDLLMTAMT);
		     
		     cardregbean.setWdl_lmt_amt(WDL_LMT_AMT);
		     
		     String WDLLMTCNT= (String) getRequest().getParameter("wdl_lmt_cnt");
		      int WDL_LMT_CNT=Integer.parseInt(WDLLMTCNT);
		     
		     cardregbean.setWdl_lmt_cnt(WDL_LMT_CNT);
		     
		     String PURLMTAMT= (String) getRequest().getParameter("pur_lmt_amt");
		      int PUR_LMT_AMT=Integer.parseInt(PURLMTAMT);
		     
		     cardregbean.setPur_lmt_amt(PUR_LMT_AMT);


		     String PURLMTCNT= (String) getRequest().getParameter("pur_lmt_cnt");
		      int PUR_LMT_CNT=Integer.parseInt(PURLMTCNT);
		     
		     cardregbean.setPur_lmt_cnt(PUR_LMT_CNT);
		     		   
		     cardregbean.setOrder_ref_no((String)getRequest().getParameter("orderreferno"));
		     		     
		     cardregbean.setLmt_based_on((String)getRequest().getParameter("lmt_based_on"));
		     cardregbean.setEncrptchn(ecardno);
		     cardregbean.setChn_msk((String)getRequest().getParameter("chnmask"));
		     
	
		     cardregbean.setEmail((String)getRequest().getParameter("emailid"));
		     cardregbean.setPaddress1((String)getRequest().getParameter("address1"));
		     cardregbean.setPaddress2((String)getRequest().getParameter("address2"));
		     cardregbean.setPaddress3((String)getRequest().getParameter("address3"));
		     cardregbean.setPaddress4((String)getRequest().getParameter("address4"));
		     cardregbean.setPaddress5((String)getRequest().getParameter("address5"));
		     cardregbean.setPaddress6((String)getRequest().getParameter("address6"));
		     cardregbean.setCountrydesc((String)getRequest().getParameter("address6"));
		     
		     String dob = getRequest().getParameter("DOB");
		     cardregbean.setDob(dob);
	
		     cardregbean.setPhoneno((String)getRequest().getParameter("phonenumber"));
		     cardregbean.setGender((String)getRequest().getParameter("sex"));
		     cardregbean.setMothername((String)getRequest().getParameter("mothername"));
		     cardregbean.setCard_ccy((String)getRequest().getParameter("cardcurr"));
		     String currcode="";
		
		    	 cardregbean.setCard_ccy((String)getRequest().getParameter("card_ccy"));
		     
		     
		     cardregbean.setCardtype(Integer.parseInt((String)getRequest().getParameter("cardtype").trim()));
		     
		     System.out.println("start to insert customerdet");
		     StringBuilder getLimitType = new StringBuilder();
				getLimitType.append("select LIMITTYPE from LIMITINFO where LIMIT_RECID in ( ");
				getLimitType.append("select b.LIMIT_ID from PRODUCT_MASTER a, INSTPROD_DETAILS B where A.PRODUCT_CODE in "); 
				
				/*getLimitType.append("(select PRODUCT_CODE from INSTPROD_DETAILS where  SUB_PROD_ID='"+cardregbean.getCod_prod()+"' AND INST_ID='"+instid+"') AND A.PRODUCT_CODE=B.PRODUCT_CODE and B.SUB_PROD_ID='"+cardregbean.getCod_prod()+"'");
				getLimitType.append("		) and rownum=1 ");
				
				enctrace("limitlistqry :"+ getLimitType.toString());    
				String limitbasedon= (String) jdbctemplate.queryForObject(getLimitType.toString(), String.class);*/
				
				
				//by gowtham-280819
				getLimitType.append("(select PRODUCT_CODE from INSTPROD_DETAILS where  SUB_PROD_ID=? AND INST_ID=?) AND A.PRODUCT_CODE=B.PRODUCT_CODE and B.SUB_PROD_ID=?");
				getLimitType.append("		) and rownum=? ");
				enctrace("limitlistqry :"+ getLimitType.toString());    
				String limitbasedon= (String) jdbctemplate.queryForObject(getLimitType.toString(), new Object[]{cardregbean.getCod_prod(),instid,cardregbean.getCod_prod(),"1"},String.class);
				
				
				cardregbean.setLmt_based_on(limitbasedon);
				
				StringBuilder ai = new StringBuilder(); 
		     

		  int updateinstcardproces = cardregdao.updateinstcardprocess(instid,usercode,cardregbean,jdbctemplate);   
		  int insertinsacctdet = cardregdao.insertinsacctdetails(instid, cardregbean, jdbctemplate);
		  int insertinscustdet = cardregdao.insertinscustdetails(instid,cardregbean, jdbctemplate);
		  int updateezcardinfo = cardregdao.updateordeezcardinfofornewcard(instid, oldhcardno.toString(), jdbctemplate);

		  
		  // int updatereffornewcard = cardregdao.updateordernofornewcard(instid, orderfornewcard,(String)getRequest().getParameter("orderreferno"), jdbctemplate);
		 // int updateezauthrel = cardregdao.updateordeezauthrelfornewcard(instid, newhashcard.toString(),oldhcardno.toString(), jdbctemplate);

		  
		  
		//int   insertcardaccountlink=cardregdao.insertcardaccountlink(instid,cardregbean, jdbctemplate);

		// int insertacctdet = cardregdao.insertacctdetails(instid, cardregbean, jdbctemplate);
		// int insertcustdet = cardregdao.insertcustdetails(instid,cardregbean, jdbctemplate);
		 //int movetoproduct = cardregdao.insertproduction(instid,cardregbean,jdbctemplate);
		  
		 

		  
		  
		  try {
				trace("getting count updateinstcardproces insatmcms "+updateinstcardproces
						+" \n insertinsacctdet"+insertinsacctdet+" \n insertinscustdet"+insertinscustdet
					);
				//trace("getting count insert insertinsacctdet "+insertinsacctdet); 
				//trace("getting count inseert insertinscustdet "+insertinscustdet); 
				
			//	trace("getting count update atmcms "+insertacctdet);
				//trace("getting count insert acct "+insertcustdet); 
				//trace("getting count inseert movetoproduct "+movetoproduct); 
			
					//txManager.commit(trasact.status);
					 if ((updateinstcardproces >0 || insertinsacctdet > 0 || insertinscustdet > 0 && updateezcardinfo >0 ) ){
					    	txManager.commit(trasact.status);
					    	addActionMessage(cardregbean.getChn_msk() + " Card Registered Successfully .Waiting for Authorization ");					    	
							trace("Committed successfully....for the card no " + cardregbean.getChn_msk() );
							
							//---------------audit code edited by sardar on 11-12-15---------//	
							try{ 
								

								//added by gowtham_220719
								trace("ip address======>  "+ip);
								auditbean.setIpAdress(ip);
								
								String mcardno = commondesc.getMaskedCardbyproc(instid, cardregbean.getEncrptchn(),"INST_CARD_PROCESS","C", jdbctemplate);
								if(mcardno==null){mcardno=cardregbean.getEncrptchn();}
								auditbean.setActmsg("Reissue card "+newcardno+" is Mapped to New card [ "+mcardno+" ] ");
								//auditbean.setUsercode(usercode);
								auditbean.setUsercode(comUsername());
								auditbean.setCardcollectbranch(branchcode);
								auditbean.setActiontype("IM");
								auditbean.setAuditactcode("4104");  
								
								//added by gowtham_010819
								String pcode=null;
								 auditbean.setProduct((String)getRequest().getParameter("pcode"));  
								 
								auditbean.setCardno(mcardno);
								auditbean.setApplicationid(cardregbean.getOrder_ref_no());
								//auditbean.setCardnumber(order_refnum[i]);
								//commondesc.insertAuditTrail(in_name, Maker_id, auditbean, jdbctemplate, txManager);
								commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
							 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage() ); }
							 								
							//--------------End on 11-12-15-------------//
					    }
					 
															 
					 else{
					    	txManager.rollback( trasact.status );
					    	addActionError("Could not Register the Card " + cardregbean.getChn_msk());
							trace("Exception : While Register the cards");
					    }
					
				
				
			  
				
				
			} catch (Exception e) {
				txManager.rollback( trasact.status );
				addActionError("Unable to Register the Cards");   
				trace("Exception : While Register Issue the cards : " + e.getMessage() );
			} 
		  
		  cardregbean.setMsterbin(getRequest().getParameter("msterorprobbin").substring(0, 6));
		//  return this.registerInstCardHome();
		  return this.findreissucardsbin();
	  }
	
	
	
	

	public String findbinsformapping() { 
		trace("Reissu Instant card register ...\n");
		enctrace("Reissu Instant card register ...\n");
		HttpSession session = getRequest().getSession();
		String instid =  comInstId(); 
		/*if (comBranchId().equals("000")){
				session.setAttribute("preverr", "E"); 
				session.setAttribute("prevmsg", " Please Login With branch User ..."  );
				trace("Please Login With branch User ...");
				return "required_home";
			}*/
		
		List prodlist;
		try {
			prodlist = commondesc.getProductListView(instid, jdbctemplate, session );
			System.out.println( "john__" + prodlist );
			
			String productcode="",prodesc="";
			Iterator secitr = prodlist.iterator();
			
					
					 setProdlist(prodlist);
			   
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return "instantcardBinlist"; 
	}
	
	
	
	
	
	

	public String getregistrationtype( String instid, String cardtype, JdbcTemplate jdbctemplate ) {   
		String instype = null;
		try {
			String qrydesc = null;
			trace("qrydesc :"+qrydesc); 
			instype = (String)jdbctemplate.queryForObject(qrydesc, String.class);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}  
		return instype; 
	} 
	
	public String registerInstCardHome() { 
		trace("Instant card register ...\n");
		enctrace("Instant card register ...\n");
		String instid =  comInstId(); 
		
		String cardtype = getRequest().getParameter("cardtype").trim();
		//String cardtype = "551270";
		HttpSession session = getRequest().getSession();
		String typeofregistration="";
		try{
			  
			 // typeofregistration=this.getregistrationtype(instid,cardtype,jdbctemplate);
				
				//trace("typeofregistration"+typeofregistration);
				//cardregbean.setInsttypereg(typeofregistration);
				
				
			 String act = getRequest().getParameter("act");
			 if( act != null ){
				session.setAttribute("act", act);
			 }
			
			 /*if (comBranchId().equals("000")){
					session.setAttribute("preverr", "E"); 
					session.setAttribute("prevmsg", " Please Login With branch User ..."  );
					trace("Please Login With branch User ...");
					return "required_home";
				}*/
			 
			 trace("Getting branchlist....");
			 List branchlist =  this.commondesc.generateBranchList(instid, jdbctemplate);
			 if( branchlist== null || branchlist.isEmpty() ){
				 session.setAttribute("preverr", "E");	session.setAttribute("prevmsg", "No Branch Configured....");
				 return "required_home";
			 }
			 cardregbean.setBranchlist(branchlist);
			 
			 trace("Getting product list...");
			 List prodlist = commondesc.getProductListView( instid, jdbctemplate); 
			 if( prodlist== null || prodlist.isEmpty() ){
				 session.setAttribute("preverr", "E");	session.setAttribute("prevmsg", "No Product Configured....");
				 return "required_home";
			 }
			 cardregbean.setProdlist(prodlist);
			
			 
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
			session.setAttribute("prevmsg", "Exception : Could not continue the registeration process.");
			e.printStackTrace();
			trace("Exception : Could not continue the registeration process... " + e.getMessage() );
			return "required_home";
		}
	
	cardregbean.setMsterbin(cardtype.substring(0, 6));
	if(cardtype.substring(0, 6).equals("551270")){//
	
	
			return "instmaporderrefbase";
		}
	/*else if(typeofregistration.equalsIgnoreCase("ENTER MANUALLY")){
		//return "manualentry_home"; 
		return "instmanualmap";
	}*/
		else{
		return "insregcard_home"; 
		}
	}
	
	public String newcustregtype(){
		
		HttpSession session = getRequest().getSession();
		String insttypereg = getRequest().getParameter("insttypereg").trim();
		//String insttypereg = (String)session.getAttribute("insttypereg")
		System.out.println("insttypereg--->"+insttypereg);
		insttypereg="FETCH DATA USING WEB SERVICE";
		if(insttypereg.equalsIgnoreCase("FETCH DATA USING WEB SERVICE")){
			cardregbean.formaction="newcustregWithwebserviceInstCardRegisterProcess.do";
			//System.out.println("cardregbean.formaction"+cardregbean.formaction);
			return newcustregWithwebservice();
			//return "insregcard_home"; 
		}
		if(insttypereg.equalsIgnoreCase("FETCH DATA USING CBS LINK")){
			return newCustReg();
		}
		if(insttypereg.equalsIgnoreCase("FETCH DATA USING OTHER SCHEMA")){
			return schemacustdetails();
		}
		if(insttypereg.equalsIgnoreCase("ENTER MANUALLY")){
			return "instmanualmap"; 
		}
		return "required_home";
	}
	public String manualreginstant()
	{
		

		trace("************manualreginstant Customer Reisteration Home **************\n\n");
		enctrace("************manualreginstant Customer Reisteration Home **************\n\n");
		
		HttpSession session = getRequest().getSession();
		String instid = comInstId();
		String br_id = (String)session.getAttribute("BRANCHCODE"); 
		
		//String card_no = getRequest().getParameter("cardno").trim();
		
		
		
		//String accountno = getRequest().getParameter("accountno").trim();
		String processtype = "INSTANT";
		Boolean kyc = false;
		//String regtype = getRequest().getParameter("regtype").trim();
		String custid = "";
		String subprodid = "";
		String subproddesc ="";
		String actiontype = "EDIT";
		String apptype = (String)session.getAttribute("APPLICATIONTYPE");
		StringBuffer hcardno = new StringBuffer();
		String getjsonvalues="";
		JSONObject js = null;
		String name="",mobileno="",customerid="",acctcur="",dob="",addr1="",addr2="",addr3="";
		int branch_code;
		try{
			
			

		String	maptype="" ,message="";
        List validbranch=null;
		
			String card_no = getRequest().getParameter("cardno").trim();	
			maptype="CARDBASE";
				
				if(getRequest().getParameter("cardno").trim().length() !=16){
					session.setAttribute("preverr", "E"); 
					session.setAttribute("prevmsg", "Entered  Card NO is "+card_no+"  ,Enter Valid 16 digit card number.."  );
					trace("Entered  "+card_no+"  is less than 16 digit,Kindly Check ...");
					return this.findbinsformapping();
					
				}
				
				cardregbean.setCardno(card_no);
				String keyid = "";
				PadssSecurity padsssec = new PadssSecurity();
				
				hcardno = padsssec.getHashedValue(card_no+instid);
				cardregbean.setHascard(hcardno.toString());
				System.out.println("hcardno_kangra"+hcardno);
					//} 
				
					String maskedchn=padsssec.getMakedCardno(card_no);	
					System.out.println("decrypted card number for encod and maintanance  "+hcardno+" clear chn value/orderrefno"+hcardno);
				
					// validbranch = commondesc.getvalidbranch(instid,hcardno.toString(),maptype,jdbctemplate);
					
					int cardavailable = cardregdao.cardavaialbel(instid,hcardno.toString(),maptype, jdbctemplate);
					
					
					if( cardavailable ==3){
						//subproddesc = commondesc.getSubProductdesc(instid, subprodid, jdbctemplate);
						session.setAttribute("preverr", "E"); 
						session.setAttribute("prevmsg", " "+card_no+" Card no is not found in records .."  );
						//trace("[ "+ card_no +" ]Card is not found in records .");
						return this.findbinsformapping();
					}
                    int cardcollectbranch = cardregdao.cardcollectbranch(instid,hcardno.toString(),maptype, jdbctemplate);
					
					
					if( cardcollectbranch <=0){
						//subproddesc = commondesc.getSubProductdesc(instid, subprodid, jdbctemplate);
						session.setAttribute("preverr", "E"); 
						session.setAttribute("prevmsg", " "+card_no+" Card Collect Branch is not available..."  );
						//trace("[ "+ card_no +" ]Card Collect Branch is not available .");
						return "required_home";
					}
                    /*int samebranchcard = cardregdao.samebranchcard(instid,hcardno.toString(),br_id, jdbctemplate);
					
					
					if( samebranchcard <=0){
						//subproddesc = commondesc.getSubProductdesc(instid, subprodid, jdbctemplate);
						session.setAttribute("preverr", "E"); 
						session.setAttribute("prevmsg", " "+card_no+" Some other Branch Card..."  );
						trace("[ "+ card_no +" ]Some other Branch Card .");
						return "required_home";
					}*/
			
			           int cardrequired = cardregdao.cardstatsusformap(instid,hcardno.toString(),maptype, jdbctemplate);
						
						trace("Got regreq: " + cardrequired );
						if( cardrequired ==1 ){
							//subproddesc = commondesc.getSubProductdesc(instid, subprodid, jdbctemplate);
							session.setAttribute("preverr", "E"); 
							session.setAttribute("prevmsg", " "+card_no+"  card is  Already Mapped with Customer ...Waiting for Authorization..."  );
							trace("[ "+ card_no +" ]card is  Already Mapped with Customer .. .Waiting for Authorization ...");
							return this.findbinsformapping();
						}
						if( cardrequired ==2 ){
							//subproddesc = commondesc.getSubProductdesc(instid, subprodid, jdbctemplate);
							session.setAttribute("preverr", "E"); 
							session.setAttribute("prevmsg", " "+card_no+" Card is not ready for mapping .."  );
							trace("[ "+ card_no +" ]Card is not ready for mapping ..");
							return this.findbinsformapping();
						}
					
			

						List carddet = cardregdao.getcarddetails(instid,hcardno.toString(),maptype,jdbctemplate);
						trace("cardforcbs"+carddet);
						if(carddet.isEmpty())
						{
							
							session.setAttribute("preverr", "E");
							session.setAttribute("prevmsg",  "No Record Found With "+card_no+" ");   
							return this.registerInstCardHome();
						}
						else{
							System.out.println("dsadsadas");
							String eligible="ELIGIBLE";
							cardregbean.setCardeligibility(eligible);
							System.out.println("next step");
							//setCardeligible("ELIGIBLE");
						}
				
					
				
				 List prodlist = cardregdao.getaccttype( instid, jdbctemplate); 
				 if( prodlist== null || prodlist.isEmpty() ){
					 session.setAttribute("preverr", "E");	session.setAttribute("prevmsg", "No Product Configured....");
					 return this.findbinsformapping();
				 }else{
				 cardregbean.setAcctproduct(prodlist);
				 }

				 List curencylist = cardregdao.getcurrency( instid, jdbctemplate); 
				 if( curencylist== null || curencylist.isEmpty() ){
					 session.setAttribute("preverr", "E");	session.setAttribute("prevmsg", "No Currency Configured....");
					 return this.findbinsformapping();
				 }else{
				 cardregbean.setCurrency(curencylist);
				 }
				 
				 
				 
				Iterator itrat = carddet.iterator();
				
					while(itrat.hasNext())
					{
						Map map = (Map) itrat.next(); 
						int WDLAMT=((BigDecimal)map.get("WDL_AMT")).intValue();
						int WDLCNT=((BigDecimal)map.get("WDL_CNT")).intValue();
						int PURAMT=((BigDecimal)map.get("PUR_AMT")).intValue();
						int PURCNT=((BigDecimal)map.get("PUR_CNT")).intValue();
						
						cardregbean.setOrder_ref_no((String)map.get("ORDER_REF_NO"));
	         			cardregbean.setChn_msk((String)map.get("MCARD_NO"));
						cardregbean.setHascard((String)map.get("HCARD_NO"));
						cardregbean.setEncrptchn((String)map.get("CARD_NO"));
						cardregbean.setOrg_chn((String)map.get("CHN"));
						cardregbean.setCod_prod(((String) map.get("SUB_PROD_ID").toString().trim()));

						//cardregbean.setWdl_lmt_amt(WDLAMT);
						cardregbean.setLmt_based_on((String)map.get("LMT_BASED_ON"));
						String cardtype=(String)map.get("BIN");
						String typeofcarddesc=commondesc.getCardTypeDesc(instid, cardtype, jdbctemplate);
						cardregbean.setCardtypedesc(typeofcarddesc);
						
								String cardtypevalue=(String)map.get("CARD_TYPE_ID");
						int cardtypeid=Integer.parseInt(cardtypevalue);
								cardregbean.setCardtype(cardtypeid);
						cardregbean.setWdl_lmt_amt(WDLAMT);
						cardregbean.setWdl_lmt_cnt(WDLCNT);
						Date GENERATED_DATE = (Date) map.get("GENERATED_DATE");
						//String appdate=(String)map.get("GENERATED_DATE");
						//String APPDATE=appdate.substring(0, 10);
					 	//String trimmingtime="select to_date('"+APPDATE+"', 'YYYY-MM-dd')  from dual ";
						//String nodaysforexpiry = (String)jdbctemplate.queryForObject(trimmingtime, String.class);

						//dateFormatter(APPDATE);
						//System.out.println(appdate+"time_qury"+APPDATE);
						//System.out.println(appdate);
						cardregbean.setBranchcode((String)map.get("CARD_COLLECT_BRANCH"));
						cardregbean.setCustidno((String)map.get("CIN"));
						cardregbean.setEncrptchn((String)map.get("HCARD_NO"));
						cardregbean.setCardgendate(GENERATED_DATE);
						cardregbean.setLmt_based_on((String)map.get("LMT_BASED_ON"));
						Date expdate = (Date) map.get("EXPIRY_DATE");
						cardregbean.setExpdate(expdate);
						System.out.println(expdate);
						cardregbean.setProd_code((String)map.get("PRODUCT_CODE"));
						
					
																		
					}   
														  
			
		}catch( Exception e ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg","Exception : Could not continue the process");
			e.printStackTrace();
			trace("Exception : Could not continue the process..." + e.getMessage() );
			return "required_home";
		}
		
		return "instmanualmap"; 
		
	
	}
	public void getaccountype() throws Exception {      
		
		trace("***************get accounttype method entered***************");
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname");
		String productcode = getRequest().getParameter("accountprodid");
		String status = getRequest().getParameter("status");
		System.out.println("productcode:"+productcode);
		System.out.println("status:"+status);
		System.out.println("instid:"+instid);    
		String accttype = null;
		try{
			accttype = "";//commondesc.getListOfaccounttype1(instid, productcode, status, jdbctemplate);
			//trace("Getting subproduct list for the product code [ "+productcode+" ] ...got " + accttype );
		}catch(Exception e ){
			e.printStackTrace(); 
			trace("Exception...." + e.getMessage());
			accttype = "<option value='-1'>Unable to get sub product list </option>";
		}
		
		getResponse().getWriter().write(accttype);
	}
	
public String newcustregWithwebservice(){
	
	trace("************Web ServiceCustomer Reisteration Home **************\n\n");
	enctrace("************Web Service Customer Reisteration Home **************\n\n");
	
	HttpSession session = getRequest().getSession();
	StringBuffer hcardno = new StringBuffer();
	String instid = comInstId();
	String validbranch="";
	String br_id = (String)session.getAttribute("BRANCHCODE");
	String accountno = getRequest().getParameter("accountno").trim();
	String card_no = getRequest().getParameter("cardno").trim();
	String	maptype="",ecardno ="";
	
	try {
		/*if(getRequest().getParameter("cardno").trim().length() !=16){
			session.setAttribute("preverr", "E"); 
			session.setAttribute("prevmsg", "Entered  Card NO is "+card_no+"  ,Enter Valid 16 digit card number.."  );
			trace("Entered  "+card_no+"  is less than 16 digit,Kindly Check ...");
			return "required_home";	
			
		}
		
		String keyid = "";
		PadssSecurity padsssec = new PadssSecurity();
			keyid ="1"; //commondesc.getSecurityKeyid(instid, jdbctemplate);
			System.out.println("keyid::"+keyid);
			List secList = commondesc.getPADSSDetailById(keyid,jdbctemplate);
			System.out.println("secList::"+secList);  
			Iterator secitr = secList.iterator();
			if(!secList.isEmpty()){
				while(secitr.hasNext())
				{
					Map map = (Map) secitr.next(); 
					String eDMK = ((String)map.get("DMK"));
					String eDPK = ((String)map.get("DPK"));
					
					hcardno = padsssec.getHashedValue(card_no+instid);
					ecardno = padsssec.getECHN(eDMK, eDPK, card_no);
					
					
				}      
			}
		
			String maskedchn=padsssec.getMakedCardno(card_no);	
			
			
			
			int cardavailable = cardregdao.cardavaialbel(instid,hcardno.toString(),maptype, jdbctemplate);
			
			
				if( cardavailable ==3){
					//subproddesc = commondesc.getSubProductdesc(instid, subprodid, jdbctemplate);
					session.setAttribute("preverr", "E"); 
					session.setAttribute("prevmsg", " "+card_no+" Card is not found in records .."  );
					trace("[ "+ card_no +" ]Card is not found in records .");
					return "required_home";
				}
		
		           int cardrequired = cardregdao.cardstatsusformap(instid,hcardno.toString(),maptype, jdbctemplate);
					
					trace("Got regreq: " + cardrequired );
					if( cardrequired ==1 ){
						//subproddesc = commondesc.getSubProductdesc(instid, subprodid, jdbctemplate);
						session.setAttribute("preverr", "E"); 
						session.setAttribute("prevmsg", " "+card_no+"  card is  Already Mapped with Customer ...Waiting for Authorization..."  );
						trace("[ "+ card_no +" ]card is  Already Mapped with Customer .. .Waiting for Authorization ...");
						return "required_home";
					}
					if( cardrequired ==2 ){
						//subproddesc = commondesc.getSubProductdesc(instid, subprodid, jdbctemplate);
						session.setAttribute("preverr", "E"); 
						session.setAttribute("prevmsg", " "+card_no+" Card is not ready for mapping .."  );
						trace("[ "+ card_no +" ]Card is not ready for mapping ..");
						return "required_home";
					}
				
			validbranch = commondesc.getvalidbranch(instid,hcardno.toString(),maptype,jdbctemplate);
		
			
		if (validbranch==null){
			session.setAttribute("preverr", "E"); 
			session.setAttribute("prevmsg", " "+maskedchn+"  Entered Card Number "+card_no+"  / Cardcollect branch  is not Available ..."  );
			trace("[ "+ card_no +" ]Card is not valid for this Branch User. ...");
			return "required_home";
		}
		
		*/
		
		   Client client = Client.create();
		   WebResource webResource = client.resource("http://172.16.10.67:8083/Orient_WebService_v.2/rest/testapi");
		  // WebResource webResource = client.resource("http://172.16.10.67:8080/InstantServiceRegister_v.1/rest/testapi");
		   /*WebResource webResource = client.resource("http://172.16.10.40:8989/RestApi/rest/crunchifyService");*/

		   JSONObject js=new JSONObject();
		   js.put("USER", "CGS"); 
		     js.put("Pass", "Test@123");
		     js.put("SERVICETYPE", "MAPNEWACCOUNT");
		     js.put("REQUESTTYPE", "INSTANT");
		     js.put("INSTID", instid);
		     
		     js.put("ReferenceID", "12345"); 
		     js.put("Entity", " APPREQUEST "); 
		     js.put("AccountNo", accountno);
		     js.put("CardNo", card_no);
		     js.put("Customername", "CGS");
		     js.put("MOBILE", "7845612358");
		     js.put("CIN", "17270000000002");
		     js.put("Currency", "584/945");
		     js.put("Acctype", "10/20");
		     js.put("DOB", "10-JAN-1990");
		     js.put("Address1", "kampala");
		     js.put("Address2", "1st cross road");
		     js.put("Address3", "kampala street");
		     js.put("Address4", "4343");
		     js.put("Address5", "");
		     js.put("CITY", "UGHANDA");
		     js.put("CountryCode", "456");
		     js.put("Datetime", "currentdatetime"); 
		  
		   //data.put("PASSWD", "honey");
		  // data.put("INSTID", "PMT");

		   ClientResponse response = (ClientResponse) webResource.type("application/json")
		      .post(ClientResponse.class, js.toString());
		               System.out.println(response.getStatus());
		               if (response.getStatus() != 200) {
		          throw new RuntimeException("Failed : HTTP error code : "
		               + response.getStatus());
		          }
		   System.out.println("Output from Server .... \n");
		 
		   String output = (String) response.getEntity(String.class);
		      System.out.println("myfinal response"+output);
		 
			
			   maptype="CARDBASE";
			   
			 //  JSONObject js=null;
			   
			
		   
			List carddet = cardregdao.getcarddetails(instid,ecardno,maptype,jdbctemplate);
			trace("cardforcbs"+carddet);
			if(carddet.isEmpty())
			{
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg",  "Could not fetch the details from the insatmcms...");   
			
				return "required_home";
			}
			Iterator itrat = carddet.iterator();
			
				while(itrat.hasNext())
				{
					Map map = (Map) itrat.next(); 
					
					int WDLAMT=((BigDecimal)map.get("WDL_AMT")).intValue();
					int WDLCNT=((BigDecimal)map.get("WDL_CNT")).intValue();
					int PURAMT=((BigDecimal)map.get("PUR_AMT")).intValue();
					int PURCNT=((BigDecimal)map.get("PUR_CNT")).intValue();
					
					
					cardregbean.setWdl_lmt_amt(WDLAMT);
					cardregbean.setOrder_ref_no((String)map.get("ORDER_REF_NO"));
					cardregbean.setChn_msk((String)map.get("MCARD_NO"));
					cardregbean.setHascard((String)map.get("HCARD_NO"));
					cardregbean.setEncrptchn((String)map.get("CARD_NO"));
					
					cardregbean.setWdl_lmt_cnt(WDLCNT);
					cardregbean.setPur_lmt_amt(PURAMT);
					cardregbean.setPur_lmt_cnt(PURCNT);
											
					cardregbean.setLmt_based_on((String)map.get("LMT_BASED_ON"));
					cardregbean.setProd_code((String)map.get("BIN"));
					cardregbean.setCardcollbranch(validbranch.trim());
					cardregbean.setCod_prod(((String) map.get("SUB_PROD_ID").toString().trim()));
					
					
					String productdesc = commondesc.getProductdesc(instid,(String) map.get("PRODUCT_CODE").toString().trim(), jdbctemplate);
					trace("productdesc==> "+productdesc);
					cardregbean.setProductdesc(productdesc);
					String cardtypedesc = commondesc.getCardTypeDesc(instid, (String)map.get("BIN"), jdbctemplate);
					cardregbean.setCardtypedesc(cardtypedesc);
					
					cardregbean.setCardtype(Integer.parseInt((String) map.get("CARD_TYPE_ID").toString().trim()));
					
					//getjsonvalues = this.getaccountdetailsfromcbs(instid, accountno);
					System.out.println("getjsonvalues"+output);
					
					
					js = new JSONObject(output);
					
					String acct=js.getString("NREC");
					System.out.println("acct"+acct);
					
					if(acct.equals("NREC")){
						
						session.setAttribute("preverr", "E"); 
						session.setAttribute("prevmsg", " "+accountno+"  Account Number is Not Available..."  );
						trace("[ "+ accountno +" ]Account Number is Not Available...");
						return "required_home";
					}
					cardregbean.setAccountno(js.getString("ACCTNO"));
					
					
					cardregbean.setCustname(js.getString("NAME"));
					cardregbean.setPhoneno(js.getString("MOB_NUM"));				
					cardregbean.setCustid(Integer.parseInt(js.getString("CIN")));
					cardregbean.setCard_ccy(js.getString("CURRENCY"));
					cardregbean.setDob(js.getString("DOB"));
					cardregbean.setPaddress1(js.getString("ADDRSS1"));
					cardregbean.setPaddress2(js.getString("ADDRSS2"));
					cardregbean.setPaddress3(js.getString("Address3"));
					cardregbean.setPaddress4(js.getString("Address4"));
					cardregbean.setPaddress5(js.getString("Address5"));
					cardregbean.setPaddress6(js.getString("Address6"));
          			cardregbean.setCity(js.getString("CITY"));				
					cardregbean.setCountrydesc(js.getString("COUNTRY_CODE"));
					
																	
				}   
	

		    } catch (Exception e) {

		   System.out.println(e.getMessage());
		   System.out.println(e.getClass());

		    }
	
	return "inscustomer_view";
	
}

public String schemacustdetails(){
	
	
	trace("************ schemacustdetails  Reisteration Home **************\n\n");
    enctrace("************ schemacustdetails Reisteration Home **************\n\n");
	
	HttpSession session = getRequest().getSession();
	StringBuffer hcardno = new StringBuffer(); 
	String instid = comInstId();
	String validbranch="";
	String br_id = (String)session.getAttribute("BRANCHCODE");
	String accountno = getRequest().getParameter("accountno").trim();
	String card_no = getRequest().getParameter("cardno").trim();
	String	maptype="",ecardno ="";
	
	try {
		if(getRequest().getParameter("cardno").trim().length() !=16){
			session.setAttribute("preverr", "E"); 
			session.setAttribute("prevmsg", "Entered  Card NO is "+card_no+"  ,Enter Valid 16 digit card number.."  );
			trace("Entered  "+card_no+"  is less than 16 digit,Kindly Check ...");
			return "required_home";	
			
		}
		
		String keyid = "";
		PadssSecurity padsssec = new PadssSecurity();
			keyid ="1"; //commondesc.getSecurityKeyid(instid, jdbctemplate);
			System.out.println("keyid::"+keyid);
			List secList = commondesc.getPADSSDetailById(keyid,jdbctemplate);
			
			Properties props=getCommonDescProperty();
			String EDPK=props.getProperty("EDPK");
			
			
			//System.out.println("secList::"+secList);  
			Iterator secitr = secList.iterator();
			if(!secList.isEmpty()){
				while(secitr.hasNext())
				{
					Map map = (Map) secitr.next(); 
					String CDMK = ((String)map.get("DMK"));
					//String eDPK = ((String)map.get("DPK"));
					
					String CDPK=padsssec.decryptDPK(CDMK, EDPK);
					hcardno = padsssec.getHashedValue(card_no+instid);
					ecardno = padsssec.getECHN(CDPK,card_no);
					
					
				}      
			}
		
			String maskedchn=padsssec.getMakedCardno(card_no);	
			
			
			
			int cardavailable = cardregdao.cardavaialbel(instid,hcardno.toString(),maptype, jdbctemplate);
			
			
				if( cardavailable ==3){
					//subproddesc = commondesc.getSubProductdesc(instid, subprodid, jdbctemplate);
					session.setAttribute("preverr", "E"); 
					session.setAttribute("prevmsg", " "+card_no+" Card is not found in records .."  );
					trace("[ "+ card_no +" ]Card is not found in records .");
					return "required_home";
				}
		
		           int cardrequired = cardregdao.cardstatsusformap(instid,hcardno.toString(),maptype, jdbctemplate);
					
					trace("Got regreq: " + cardrequired );
					if( cardrequired ==1 ){
						//subproddesc = commondesc.getSubProductdesc(instid, subprodid, jdbctemplate);
						session.setAttribute("preverr", "E"); 
						session.setAttribute("prevmsg", " "+card_no+"  card is  Already Mapped with Customer ...Waiting for Authorization..."  );
						trace("[ "+ card_no +" ]card is  Already Mapped with Customer .. .Waiting for Authorization ...");
						return "required_home";
					}
					if( cardrequired ==2 ){
						//subproddesc = commondesc.getSubProductdesc(instid, subprodid, jdbctemplate);
						session.setAttribute("preverr", "E"); 
						session.setAttribute("prevmsg", " "+card_no+" Card is not ready for mapping .."  );
						trace("[ "+ card_no +" ]Card is not ready for mapping ..");
						return "required_home";
					}
				
			validbranch = commondesc.getvalidbranch(instid,hcardno.toString(),maptype,jdbctemplate);
		
			
		if (validbranch==null){
			session.setAttribute("preverr", "E"); 
			session.setAttribute("prevmsg", " "+maskedchn+"  Entered Card Number "+card_no+"  / Cardcollect branch  is not Available ..."  );
			trace("[ "+ card_no +" ]Card is not valid for this Branch User. ...");
			return "required_home";
		}
		
		
			
			   maptype="CARDBASE";
			   
			   JSONObject js=null;
			   
			
		   
			List carddet = cardregdao.getcarddetails(instid,ecardno,maptype,jdbctemplate);
			trace("cardforcbs"+carddet);
			if(carddet.isEmpty())
			{
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg",  "Could not fetch the details from the insatmcms...");   
			
				return "required_home";
			}
			Iterator itrat = carddet.iterator();
			
				while(itrat.hasNext())
				{
					Map map = (Map) itrat.next(); 
					
					int WDLAMT=((BigDecimal)map.get("WDL_AMT")).intValue();
					int WDLCNT=((BigDecimal)map.get("WDL_CNT")).intValue();
					int PURAMT=((BigDecimal)map.get("PUR_AMT")).intValue();
					int PURCNT=((BigDecimal)map.get("PUR_CNT")).intValue();
					
					
					cardregbean.setWdl_lmt_amt(WDLAMT);
					cardregbean.setOrder_ref_no((String)map.get("ORDER_REF_NO"));
					cardregbean.setChn_msk((String)map.get("MCARD_NO"));
					cardregbean.setHascard((String)map.get("HCARD_NO"));
					cardregbean.setEncrptchn((String)map.get("CARD_NO"));
					
					cardregbean.setWdl_lmt_cnt(WDLCNT);
					cardregbean.setPur_lmt_amt(PURAMT);
					cardregbean.setPur_lmt_cnt(PURCNT);
											
					cardregbean.setLmt_based_on((String)map.get("LMT_BASED_ON"));
					cardregbean.setProd_code((String)map.get("BIN"));
					cardregbean.setCardcollbranch(validbranch.trim());
					cardregbean.setCod_prod(((String) map.get("SUB_PROD_ID").toString().trim()));
					
					
					String productdesc = commondesc.getProductdesc(instid,(String) map.get("PRODUCT_CODE").toString().trim(), jdbctemplate);
					trace("productdesc==> "+productdesc);
					cardregbean.setProductdesc(productdesc);
					String cardtypedesc = commondesc.getCardTypeDesc(instid, (String)map.get("BIN"), jdbctemplate);
					cardregbean.setCardtypedesc(cardtypedesc);
					
					cardregbean.setCardtype(Integer.parseInt((String) map.get("CARD_TYPE_ID").toString().trim()));
					
					//getjsonvalues = this.getaccountdetailsfromcbs(instid, accountno);
					
					Connection con = null;
					PreparedStatement pstmt = null;
					ResultSet rrs = null;
					String cardNo = "",accno = "",documentno="";
					CBSConnection cbscon = new CBSConnection();
					con = cbscon.OtherdatabaseConnection();
					if(!carddet.isEmpty() && con!=null){
						try {
							
								
								String cbsdtls = "SELECT * from INST_CUST_DETAILS where ACCTNO='"+accountno+"'";
										
								enctrace("cbsdtls : " + cbsdtls);
								pstmt = con.prepareStatement(cbsdtls);
								rrs = pstmt.executeQuery(cbsdtls);
								
							//System.out.println("rss--->"+rrs);
				
									while (rrs.next()) {
										//System.out.println("asdasdas");
										String acct= rrs.getString("ACCTNO");
										//System.out.println("accooooo"+acct);
										
										if(acct.equals("")){
											
											session.setAttribute("preverr", "E"); 
											session.setAttribute("prevmsg", " "+accountno+"  Account Number is Not Available..."  );
											trace("[ "+ accountno +" ]Account Number is Not Available...");
											return "required_home";
										}
										cardregbean.setAccountno(rrs.getString("ACCTNO"));
										cardregbean.setCustid(Integer.parseInt(rrs.getString("CIN")));
										
										cardregbean.setCustname(rrs.getString("NAME") );
										cardregbean.setDob(rrs.getString("DOB"));
										cardregbean.setPaddress1(rrs.getString("ADDRSS1"));
										cardregbean.setPaddress2(rrs.getString("ADDRSS2"));
										cardregbean.setCard_ccy(rrs.getString("CURRENCY"));
										cardregbean.setCity(rrs.getString("CITY"));	
										cardregbean.setCountrydesc(rrs.getString("COUNTRY_CODE"));
										cardregbean.setPhoneno(rrs.getString("MOB_NUM"));				
										
										
										
										
										
										cardregbean.setPaddress3(rrs.getString("Address3"));
										cardregbean.setPaddress4(rrs.getString("Address4"));
										cardregbean.setPaddress5(rrs.getString("Address5"));
										cardregbean.setPaddress6(rrs.getString("Address6"));
					          						
										
										
									
										
									}
									
								
				
	

		    } catch (Exception e) {

		   System.out.println(e.getMessage());
		   System.out.println(e.getClass());

		    }
					}
				}
				}
				
				 catch (Exception e) {

					   System.out.println(e.getMessage());
					   System.out.println(e.getClass());

					    }
	return "inscustomer_view";
	
}




	public String newCustReg() {
		trace("************Customer Reisteration Home **************\n\n");
		enctrace("************Customer Reisteration Home **************\n\n");
		
		HttpSession session = getRequest().getSession();
		String instid = comInstId();
		String br_id = (String)session.getAttribute("BRANCHCODE"); 
		
		//String card_no = getRequest().getParameter("cardno").trim();
		/*Connection conn = null;
		Dbcon dbcon = new Dbcon(); 
		conn = dbcon.getDBConnection();*/
		
		
		String accountno = getRequest().getParameter("accountno").trim();
		String processtype = "INSTANT";
		Boolean kyc = false;
		String regtype = getRequest().getParameter("regtype").trim();
		String custid = "";
		String subprodid = "";
		String subproddesc ="";
		String actiontype = "EDIT";
		String apptype = (String)session.getAttribute("APPLICATIONTYPE");
		StringBuffer hcardno = new StringBuffer();
		String getjsonvalues="";
		JSONObject js = null;
		String name="",mobileno="",customerid="",acctcur="",dob="",addr1="",addr2="",addr3="";
		int branch_code;
		try{
			
			
System.out.println("typebase"+getRequest().getParameter("regtype").trim());
		String	maptype="", validbranch="",message="",ecardno ="";
//orderbase
		if(!getRequest().getParameter("regtype").trim().equals("cardbase"))
			{
			
				String orderrefno=getRequest().getParameter("orderno").trim();
				
				
				maptype="ORDERREFNO";
				
				
				
				 validbranch = commondesc.getvalidbranch(instid,orderrefno,maptype,jdbctemplate);
				
				if (validbranch==null){
					session.setAttribute("preverr", "E"); 
					session.setAttribute("prevmsg", " "+orderrefno+"  Entered OrderRef Number "+orderrefno+"  / Cardcollect branch  is not Available ..."  );
					trace("[ "+ orderrefno +" ]order is not valid for this Branch User. ...");
					return "required_home";
				}
				
				
				
				  int ordervalid = cardregdao.orderreadytomap(instid,orderrefno,maptype, jdbctemplate);
					
					trace("Got ordervalid: " + ordervalid );
					if( ordervalid < 0 ||ordervalid == 0 ){
						//subproddesc = commondesc.getSubProductdesc(instid, subprodid, jdbctemplate);
						session.setAttribute("preverr", "E"); 
						session.setAttribute("prevmsg", " "+orderrefno+"  Kindly check,orderRefNo is  Not ready to map.."  );
						trace("[ "+ orderrefno +" ]orderrefno is  Not ready to map.");
						return "required_home";
					}
					
	      int cardrequired = cardregdao.cardstatsusformap(instid,orderrefno,maptype, jdbctemplate);
				
				trace("Got regreq: " + cardrequired );
				if( cardrequired > 0 ){
					//subproddesc = commondesc.getSubProductdesc(instid, subprodid, jdbctemplate);
					session.setAttribute("preverr", "E"); 
					session.setAttribute("prevmsg", " "+orderrefno+"  orderrefno is  Already Mapped with Customer ...Waiting for Authorization..."  );
					trace("[ "+ orderrefno +" ]order is  Already Mapped with Customer .. .Waiting for Authorization ...");
					return "required_home";
				}
				
				ecardno=orderrefno;
				message=""+orderrefno+" Order Ref Number is not valid for this Branch User....Check with valid Order Ref Number..";
				trace("[ "+ orderrefno +" ]Order Ref Number is not valid for this Branch User. ...");

			}
	//cardbase
		else{
			String card_no = getRequest().getParameter("cardno").trim();	
			maptype="CARDBASE";
				
				if(getRequest().getParameter("cardno").trim().length() !=16){
					session.setAttribute("preverr", "E"); 
					session.setAttribute("prevmsg", "Entered  Card NO is "+card_no+"  ,Enter Valid 16 digit card number.."  );
					trace("Entered  "+card_no+"  is less than 16 digit,Kindly Check ...");
					return "required_home";	
					
				}
				

				String keyid = "";
				PadssSecurity padsssec = new PadssSecurity();
				//if(padssenable.equals("Y"))
				//{
					
					keyid ="1"; //commondesc.getSecurityKeyid(instid, jdbctemplate);
					System.out.println("keyid::"+keyid);
					List secList = commondesc.getPADSSDetailById(keyid,jdbctemplate);
				//	System.out.println("secList::"+secList);  
					
					Properties props=getCommonDescProperty();
					String EDPK=props.getProperty("EDPK");
					
					Iterator secitr = secList.iterator();
					if(!secList.isEmpty()){
						while(secitr.hasNext())
						{
							Map map = (Map) secitr.next(); 
							String CDMK = ((String)map.get("DMK"));
							//String eDPK = ((String)map.get("DPK"));
							
							String CDPK=padsssec.decryptDPK(CDMK, EDPK);
							hcardno = padsssec.getHashedValue(card_no+instid);
							ecardno = padsssec.getECHN(CDPK,  card_no);
							
							
						}      
					}
					//} 
				
					String maskedchn=padsssec.getMakedCardno(card_no);	
					System.out.println("decrypted card number for encod and maintanance"+ecardno+"clear chn value/orderrefno"+hcardno);
				
					 validbranch = commondesc.getvalidbranch(instid,hcardno.toString(),maptype,jdbctemplate);
					
					if (validbranch==null){
						session.setAttribute("preverr", "E"); 
						session.setAttribute("prevmsg", " "+maskedchn+"  Entered Card Number "+card_no+"  / Cardcollect branch  is not Available ..."  );
						trace("[ "+ card_no +" ]Card is not valid for this Branch User. ...");
						return "required_home";
					}
					
					message=""+maskedchn+"  Card is not valid for this Branch User....Check with valid Card..";
					trace("[ "+ card_no +" ]Card/order is not valid for this Branch User. ...");

					
					int cardrequired = cardregdao.cardstatsusformap(instid,hcardno.toString(), maptype,jdbctemplate);
					
					trace("Got regreq: " + cardrequired );
					if( cardrequired > 0 ){
						//subproddesc = commondesc.getSubProductdesc(instid, subprodid, jdbctemplate);
						session.setAttribute("preverr", "E"); 
						session.setAttribute("prevmsg", " "+maskedchn+"  Card is  Already Mapped with Customer ...Waiting for Authorization..."  );
						trace("[ "+ card_no +" ]Card is  Already Mapped with Customer .. .Waiting for Authorization ...");
						return "required_home";
					}
					
			}
			
			
				
				
				
					
				trace("Got validbranch: " + validbranch );
				if (!br_id.equals("0000")){
					if( !validbranch.equalsIgnoreCase(br_id)){
						//subproddesc = commondesc.getSubProductdesc(instid, subprodid, jdbctemplate);
						session.setAttribute("preverr", "E"); 
						session.setAttribute("prevmsg", " "+message+" "  );
						return "required_home";
					}
				}
				
			
				String org_chn = this.findinprocess(instid,accountno,jdbctemplate);
				if( org_chn !=null ||org_chn==""){
					/*String org_chn = this.getorgchnexist(instid,accountno,jdbctemplate);
						if(org_chn ==null ||org_chn==""){
						
							org_chn = this.getacountinprocess(instid,accountno,jdbctemplate);
		
								
						}
							*/				//subproddesc = commondesc.getSubProductdesc(instid, subprodid, jdbctemplate);
					session.setAttribute("preverr", "E"); 
					session.setAttribute("prevmsg",  "Account Already Exist [ "+ accountno +" ] with "+org_chn+" Waiting for Cuatomer Mapping Authorization.."  );
					trace("[ "+ accountno +" ] Account Number Alredy Exist. Registeration not required");
					return "required_home";
				}
						
				int regreq = cardregdao.checkcustomeridexist(instid,accountno, jdbctemplate);
				trace("Got regreq: " + regreq );
				if( regreq > 0 ){
					/*String org_chn = this.getorgchnexist(instid,accountno,jdbctemplate);
						if(org_chn ==null ||org_chn==""){
						
							org_chn = this.getacountinprocess(instid,accountno,jdbctemplate);
		
								
						}
							*/				//subproddesc = commondesc.getSubProductdesc(instid, subprodid, jdbctemplate);
					session.setAttribute("preverr", "E"); 
					session.setAttribute("prevmsg",  "Account Already Exist [ "+ accountno +" ] Enter the Account No In the Maintenance to find the card.."  );
					trace("[ "+ accountno +" ] Account Number Alredy Exist. Registeration not required");
					return "required_home";
				}
																												
				
				
				List carddet = cardregdao.getcarddetails(instid,ecardno,maptype,jdbctemplate);
				trace("cardforcbs"+carddet);
				if(carddet.isEmpty())
				{
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg",  "Could not fetch the details from the insatmcms...");   
				}
				Iterator itrat = carddet.iterator();
				
					while(itrat.hasNext())
					{
						Map map = (Map) itrat.next(); 
						
						int WDLAMT=((BigDecimal)map.get("WDL_AMT")).intValue();
						int WDLCNT=((BigDecimal)map.get("WDL_CNT")).intValue();
						int PURAMT=((BigDecimal)map.get("PUR_AMT")).intValue();
						int PURCNT=((BigDecimal)map.get("PUR_CNT")).intValue();
						
						
						cardregbean.setWdl_lmt_amt(WDLAMT);
						cardregbean.setOrder_ref_no((String)map.get("ORDER_REF_NO"));
						cardregbean.setChn_msk((String)map.get("MCARD_NO"));
						cardregbean.setHascard((String)map.get("HCARD_NO"));
						cardregbean.setEncrptchn((String)map.get("CARD_NO"));
						
						cardregbean.setWdl_lmt_cnt(WDLCNT);
						cardregbean.setPur_lmt_amt(PURAMT);
						cardregbean.setPur_lmt_cnt(PURCNT);
												
						cardregbean.setLmt_based_on((String)map.get("LMT_BASED_ON"));
						cardregbean.setProd_code((String)map.get("BIN"));
						cardregbean.setCardcollbranch(validbranch.trim());
						cardregbean.setCod_prod(((String) map.get("SUB_PROD_ID").toString().trim()));
						
						
						String productdesc = commondesc.getProductdesc(instid,(String) map.get("PRODUCT_CODE").toString().trim(), jdbctemplate);
						trace("productdesc==> "+productdesc);
						cardregbean.setProductdesc(productdesc);
						String cardtypedesc = commondesc.getCardTypeDesc(instid, (String)map.get("BIN"), jdbctemplate);
						cardregbean.setCardtypedesc(cardtypedesc);
						
						cardregbean.setCardtype(Integer.parseInt((String) map.get("CARD_TYPE_ID").toString().trim()));
						
						/*getjsonvalues = this.getaccountdetailsfromcbs(instid, accountno);
						System.out.println("getjsonvalues"+getjsonvalues);
						
						js = new JSONObject(getjsonvalues);
						cardregbean.setCustname(js.getString("Name"));
						cardregbean.setPhoneno(js.getString("Mobile Number"));				
						cardregbean.setCustid(Integer.parseInt(js.getString("Customer ID")));
						cardregbean.setCard_ccy(js.getString("Account Currency"));
						cardregbean.setDob(js.getString("DOB"));
						cardregbean.setPaddress1(js.getString("Address1"));
						cardregbean.setPaddress2(js.getString("Address2"));
						cardregbean.setPaddress3(js.getString("Address3"));
						cardregbean.setPaddress4(js.getString("Address4"));
						cardregbean.setPaddress5(js.getString("Address5"));
						cardregbean.setPaddress6(js.getString("Address6"));
	           			cardregbean.setCity(js.getString("City"));				
						cardregbean.setCountrydesc(js.getString("Country code"));
						cardregbean.setAccountno(accountno);*/
						
																		
					}   
					
					
																  
			
		}catch( Exception e ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg","Exception : Could not continue the process");
			e.printStackTrace();
			trace("Exception : Could not continue the process..." + e.getMessage() );
			return "required_home";
		}
		
		return "inscustomer_view";
		
	}
	
	
	
	
	
	private File getImageFile(String imageId, String PHOTOTYPE) { 
		Properties prop = commondesc.getCommonDescProperty();
		String photodir  = "";
		if( PHOTOTYPE.equals("PHOTO")){
			photodir = prop.getProperty("PHOTOFILELOCATION");
		}else if( PHOTOTYPE.equals("SIGNATURE")){
			photodir = prop.getProperty("SIGNATUREFILELOCATION");
		}else if( PHOTOTYPE.equals("IDPROOF")  ){
			photodir = prop.getProperty("IDPROOFLOCATION");
		}
		File file = new File(photodir+"/"+imageId);
		return file;
	}
	
	public String getCustomContentType() {
		return "image/jpeg";
	}
 
	String imageId;
	byte[] imageInByte = null;
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
	/*public void imageAction() throws IOException{
		imageId = getRequest().getParameter("imageId");
		String imagetype = getRequest().getParameter("imagetype");
		BufferedImage originalImage;
		try {
			originalImage = ImageIO.read(getImageFile(this.imageId, imagetype ));
			 
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(originalImage, "jpg", baos);
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close();
		} catch (IOException e) {
			trace("Exception : " + e.getMessage() );
			e.printStackTrace();
		}
 
		getResponse().setContentType(getCustomContentType());
		getResponse().getOutputStream().write(imageInByte);
		getResponse().getOutputStream().flush(); 
		 
	}
	*/
	
	
 
    
	public void imageAction() throws IOException{
		System.out.println("New image action");
		imageId = getRequest().getParameter("imageId");
		String imagetype = getRequest().getParameter("imagetype");
		BufferedImage originalImage; 
		try { 
			 
		     File file = getImageFile(this.imageId, imagetype );
		     com.sun.image.codec.jpeg.JPEGImageDecoder jpegDecoder =  JPEGCodec.createJPEGDecoder (new FileInputStream(file));
		     originalImage = jpegDecoder.decodeAsBufferedImage();
		     ByteArrayOutputStream baos = new ByteArrayOutputStream();
			 try{
				 ImageIO.write(originalImage, "jpg", baos);
			 }catch(ImageFormatException ee){
				 trace("Not jpg file...exception..continuing....");
				 ImageIO.write(originalImage, "png", baos);
				 
			 }
			 baos.flush();
			 imageInByte = baos.toByteArray();
			 baos.close();
		} catch (Exception e) {
			trace("Exception : " + e.getMessage() );
			e.printStackTrace();
		}
 
		getResponse().setContentType(getCustomContentType());
		getResponse().getOutputStream().write(imageInByte);
		getResponse().getOutputStream().flush(); 
		 
	}
	
	
	public String getaccountdetailsfromcbs( String instid, String accountno ) {
		trace("********Generate customer details.....*************\n");
		enctrace("********Generate customer details.....*************\n");
		HttpSession session = getRequest().getSession();
		String charset = "UTF-8";
		String data="";
		JSONObject js = new JSONObject();
		 try {		      
		  URLConnection connection = null;
	      BufferedReader reader = null;
	      StringBuffer stringBuilder = new StringBuffer();
	      InputStreamReader in = null;
	    
	      try
	      {
		        connection = new URL("http://172.18.2.55:8080/ords/azsms/cmsfetch?acc=" + accountno).openConnection();
		        connection.setDoOutput(true);

		        in = new InputStreamReader(connection.getInputStream(), 
		          Charset.defaultCharset());
		        reader = new BufferedReader(in);
		        
		        String line = null;
		        while ((line = reader.readLine()) != null)
		        {
		          stringBuilder.append(line);
		        }
		        System.out.println("output from the required string" + stringBuilder.toString());
		        data = stringBuilder.toString();
		        if (data.contains("Account is not valid,Please enter a valid account")) {
		          addActionError("Entered " + accountno + "Account is not Available in the CBS/Kindly Contact Head Office ...");
		          
		          trace("Could not continue the customer registeration process.." + accountno + ".Account is not valid");
		          String str1 = registerInstCardHome();return str1;
		        }
		        
		        System.out.println("after replacing the pipe symbol value we can get as" + data);
		        
		        String Footer = "|";
		        int cnamepipeendlength = data.indexOf(Footer);
		        String custname = data.substring(0, cnamepipeendlength);
		        System.out.println("custname length     :  " + custname.length());
		        String custname1 = "";String custname2 = "";
		        custname1 = custname.replace(",", "");
		        if (custname.length() > 26) {
		          custname1 = custname.substring(0, 26);
		        }
		        if (custname.length() < 26) {
		          custname1 = custname;
		        }
		        System.out.println("custname     :  " + custname1);
		        custname2 = custname1.replace("'", "");
		        System.out.println("existed qote with '  :  " + custname2);

		        trace("custname  after remove all special char   :  " + custname2);
		        js.put("Name", custname2);
		        String aftercustremoved = data.substring(custname.length() + 1);
		        System.out.println("after custname removed" + aftercustremoved);

		        int phonelen = aftercustremoved.indexOf(Footer);
		        String phone = aftercustremoved.substring(0, phonelen);
		        System.out.println("phone length     :  " + phone.length());
		        
		        System.out.println("phone     :  " + phone);
		        js.put("Mobile Number", phone);
		        
		        String s3 = aftercustremoved.substring(phone.length() + 1);
		        System.out.println("after custname and phoneno removed" + s3);
		        


		        int y2 = s3.indexOf(Footer);
		        String cin = s3.substring(0, y2);
		        System.out.println("cin len     :  " + cin.length());
		        System.out.println("cin     :  " + cin);
		        
		        js.put("Customer ID", cin);
		        

		        String s4 = s3.substring(cin.length() + 1);
		        System.out.println("after custphCin RM" + s4);
		        


		        int y3 = s4.indexOf(Footer);
		        String currency = s4.substring(0, y3);
		        System.out.println("currency len     :  " + currency.length());
		        
		        System.out.println("currency     :  " + currency);
		        
		        js.put("Account Currency", currency);
		        

		        String s5 = s4.substring(currency.length() + 1);
		        System.out.println("aftter curr RM" + s5);
		        


		        int y4 = s5.indexOf(Footer);
		        String dob = s5.substring(0, y4);
		        System.out.println("DOB len     :  " + dob.length());
		        System.out.println("DOB     :  " + dob);
		        

		        js.put("DOB", dob);
		        
		        String s6 = s5.substring(dob.length() + 1);
		        System.out.println("dob rem" + s6);

		        String addressdetails = s6.replace("'", "");
		        String[] datas = addressdetails.split(",");
		        
		        trace("custname" + custname1 + "\n phone" + phone + "\n cin" + cin + " \n currency" + currency + 
		          "\n dob" + dob + "\n adda1" + datas[0] + "\n datas[1]-add2" + datas[1] + 
		          "\n datas[2]-add3 " + datas[2] + "\n add4" + datas[3] + 
		          "\n add5" + datas[4] + "\n add6" + datas[5] + 
		          "\n city " + datas[6] + "\n country code" + datas[7]);
		        
		        js.put("Address1", datas[0]);
		        js.put("Address2", datas[1]);
		        js.put("Address3", datas[2]);
		        js.put("Address4", datas[3]);
		        js.put("Address5", datas[4]);
		        js.put("Address6", datas[5]);
		        js.put("City", datas[6]);
		        js.put("Country code", datas[7]);
		        
		        System.out.println("getting json values " + js.toString());

		      }
	      catch (Exception e)
	      {
	        System.out.println("Exception Happened" + e.getMessage());
	      }
	      finally {
	        in.close();
	      }
	    }
	    catch (Exception e)
	    {
	      session.setAttribute("preverr", "E");
	      session.setAttribute("prevmsg", "Exception : Could not continue the customer registeration process...");
	      e.printStackTrace();
	      trace("Exception : while getting the customer details ... " + e.getMessage());
	    }
	    
	    return js.toString();
	  }

		
	/*public String getaccountdetailsfromcbs( String instid, String accountno,String ecardno ) {
		trace("********Generate customer details.....*************\n");
		enctrace("********Generate customer details.....*************\n");
		HttpSession session = getRequest().getSession();
		String charset = "UTF-8";
		
		try{
			URLConnection connection = null;
			BufferedReader reader = null;
			StringBuffer stringBuilder= new StringBuffer();		
			InputStreamReader in = null;
			try{
					
				//String query = String.format("acc=%s",URLEncoder.encode(acctno, charset));
				
				
				connection = new URL("http://172.18.2.55:8080/ords/azsms/cmsfetch?acc="+accountno+"").openConnection();
				connection.setDoOutput(true);
				//connection.setRequestProperty("Accept-Charset", charset);
				//connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
				//connection.getOutputStream().write(query.getBytes(charset));
				
					in = new InputStreamReader(connection.getInputStream(),
							Charset.defaultCharset());
					reader = new BufferedReader(in);
					
		             String line = null;
				      while ((line = reader.readLine()) != null)
				      {
				        stringBuilder.append(line );
				      }
				      System.out.println("output from the required string"+stringBuilder.toString());
				      String data = stringBuilder.toString();
				      data  = data.replace('|', ',');
				      System.out.println("after replacing the pipe symbol value we can get as"+data);
				      String[] datas= data.split(",");
				      JSONObject js = new JSONObject();
				      js.put("Name", datas[0]);
				      js.put("Mobile Number", datas[1]);
				      js.put("Customer ID", datas[2]);
				      js.put("Account Currency", datas[3]);
				      js.put("DOB", datas[4]);				      
				      js.put("Address1", datas[5]);
				      js.put("Address2", datas[6]);
				      js.put("Address3", datas[7]);
				      js.put("Address4", datas[8]);
				      js.put("Address5", datas[9]);
				      js.put("Address6", datas[10]);				      				      
				      js.put("City", datas[11]);
				      js.put("Country code", datas[12]);
				
				System.out.println("getting json values " + js.toString());
				
				String query = String.format("acc=%s",URLEncoder.encode(accountno, charset));
				connection = new URL("http://172.18.2.55:8080/ords/azsms/cmsfetch?acc="+accountno+"").openConnection();
				connection.setDoOutput(true);
				connection.setRequestProperty("Accept-Charset", charset);
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
				connection.getOutputStream().write(query.getBytes(charset));
			}
			catch(Exception e)
			{
				System.out.println("Exception Happened"+e.getMessage());
			}
			finally {
				in.close();			
			}
				//InputStream response = connection.getInputStream();
			try{
				reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				stringBuilder = new StringBuffer();
				String line = null;
				while ((line = reader.readLine()) != null)
				{
					stringBuilder.append(line );
				}
				String data = stringBuilder.toString();
				data.replace("|", ",");
				System.out.println("output from the required string"+stringBuilder.toString());
			}catch(Exception e){
				System.out.println("Exception Happend in Reading data"+e.getMessage());
			}
			
		}catch( Exception e ){
			session.setAttribute("preverr","E");
			session.setAttribute("prevmsg", "Exception : Could not continue the customer registeration process..." );
			e.printStackTrace();
			trace("Exception : while getting the customer details ... " + e.getMessage() );
			return this.registerInstCardHome();
		}*/
		
		//String instid = comInstId();
		/*try{			
			//trace("Getting customer details for the customer id [ "+customerid+" ] ");
			List custdata = cardregdao.getaccountdetaCBS(instid, accountno, jdbctemplate2);
			trace("Got...[ "+custdata.size()+" ]");
			if( custdata == null || custdata.isEmpty() ){
				session.setAttribute("preverr", "S"); session.setAttribute("prevmsg",  ""  );
				cardregbean.setCusttype("new");
				cardregbean.setKycuser("1");
				trace("New Customer....");
				return "inscustomer_entry";
			}else{
				session.setAttribute("preverr", "S"); session.setAttribute("prevmsg",  ""  );
				trace("Existing customer...need to update....");
			}
			trace("Getting document type....");
			List documenttypelist = commondesc.gettingDocumnettype(instid, jdbctemplate);
			if( documenttypelist==null || documenttypelist.isEmpty() ){
				session.setAttribute("preverr", "E"); session.setAttribute("prevmsg",  "No document type configured...."  );
				trace("No document type configured....");
				return "required_home";
			}
		
			
			//cardregbean.setCustid(customerid);
			//cardregbean.setChn(chn);
			Iterator itr1 = custdata.iterator();
			while( itr1.hasNext() ){
				Map temp = (Map)itr1.next(); 
				
				
				
				String todaydate = commondesc.getDate( "dd-MM-yyyy" );
				cardregbean.setAccountno((String)temp.get("P_ACCT_NO"));
				
				int COD_PROD=((BigDecimal)temp.get("COD_PROD")).intValue();
				
		
				cardregbean.setCOD_PROD(COD_PROD);
			
				
				int CUSTOMERID=((BigDecimal)temp.get("CUSTOMERID")).intValue();
				
				
				
				int regreq = cardregdao.checkcustomeridexist(instid,CUSTOMERID, jdbctemplate);
				trace("Got regreq: " + regreq );
				if( regreq > 0 ){
					//subproddesc = commondesc.getSubProductdesc(instid, subprodid, jdbctemplate);
					session.setAttribute("preverr", "E"); 
					session.setAttribute("prevmsg",  "[ "+ CUSTOMERID +" ] Customer Already Exist. Registeration not required"  );
					trace("[ "+ CUSTOMERID +" ] Customer Id Alredy Exist. Registeration not required");
					return "required_home";
				}
				
				
				
				System.out.println("cusid"+CUSTOMERID);
				
				//cardregbean.setCUSTOMERID(  CUSTOMERID );
				cardregbean.setCustid(CUSTOMERID);
				cardregbean.setFirstname(  (String)temp.get("CUST_NAME") );
				//cardregbean.setCUST_NAME(  (String)temp.get("CUST_NAME") );	
				
				int BRANCH_CODE=((BigDecimal)temp.get("BRANCH_CODE")).intValue();
				
				cardregbean.setBRANCH_CODE(  BRANCH_CODE );
				
				
				
				cardregbean.setADDRESS_1(  (String)temp.get("ADDRESS_1") );
				
				cardregbean.setADDRESS_2(  (String)temp.get("ADDRESS_2") );
				cardregbean.setADDRESS_3(  (String)temp.get("ADDRESS_3") );
				
				cardregbean.setPaddress1((String)temp.get("ADDRESS_1"));
				cardregbean.setPaddress2((String)temp.get("ADDRESS_2"));
				cardregbean.setPaddress3((String)temp.get("ADDRESS_3"));
		
				
				
				cardregbean.setDob( (String) (Object)temp.get("DOB").toString() );
				cardregbean.setEmail(  (String)temp.get("EMAILID") );
				
				cardregbean.setPhoneno(  (String)temp.get("PHONENUMBER") );
				
				cardregbean.setGender( (String)temp.get("SEX"));
				   cardregbean.setMothername(  (String)temp.get("MOTHERS_NAME") );

				
				
				//cardregbean.setDOB( (String) (Object)temp.get("DOB").toString() );
				//cardregbean.setSEX(  (String)temp.get("SEX") );  
			//	cardregbean.setPHONENUMBER(  (String)temp.get("PHONENUMBER") );
				//cardregbean.setEMAILID(  (String)temp.get("EMAILID") );
				//cardregbean.setMOTHERS_NAME(  (String)temp.get("MOTHERS_NAME") );

				List carddet = cardregdao.getcarddetails(instid,ecardno,jdbctemplate);
				trace("cardforcbs"+carddet);
				if(carddet.isEmpty())
				{
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg",  "Could not fetch the details from the insatmcms...");   
				}
				Iterator itrat = carddet.iterator();
				
					while(itrat.hasNext())
					{
						Map map = (Map) itrat.next(); 
						
						int WDLAMT=((BigDecimal)map.get("WDL_AMT")).intValue();
						int WDLCNT=((BigDecimal)map.get("WDL_CNT")).intValue();
						int PURAMT=((BigDecimal)map.get("PUR_AMT")).intValue();
						int PURCNT=((BigDecimal)map.get("PUR_CNT")).intValue();
						
						
						cardregbean.setWDL_LMT_AMT(WDLAMT);
						cardregbean.setORDER_REF_NO((String)map.get("ORDER_REF_NO"));
						cardregbean.setChn_msk((String)map.get("MCARD_NO"));
						cardregbean.setHascard((String)map.get("HCARD_NO"));
						cardregbean.setEncrptchn((String)map.get("CARD_NO"));
						
						cardregbean.setWDL_LMT_CNT(WDLCNT);
						cardregbean.setPUR_LMT_AMT(PURAMT);
						cardregbean.setPUR_LMT_CNT(PURCNT);
						
						cardregbean.setTRF_LMT_AMT((String)map.get("TRF_LMT_AMT"));
						cardregbean.setTRF_LMT_CNT((String)map.get("TRF_LMT_CNT"));
						cardregbean.setLMT_BASED_ON((String)map.get("LMT_BASED_ON"));
						cardregbean.setPROD_CODE((String)map.get("BIN"));
						cardregbean.setCARD_CCY((String)map.get("CARD_CCY"));
						
						
					}    
				
			
				
			} 
			System.out.println("custdate--->"+custdata);	 
		}catch( Exception e ){
			session.setAttribute("preverr","E");
			session.setAttribute("prevmsg", "Exception : Could not continue the customer registeration process..." );
			e.printStackTrace();
			trace("Exception : while getting the customer details ... " + e.getMessage() );
			return this.registerInstCardHome();
		}*/
		
		/*if( actiontype.equals("EDIT")){
			trace("Edit customer.....");
			return "inscustomer_entry";
		}else{
			trace("View customer.....");*/
			//return "inscustomer_view";

	
	
	
	/*public String genCustomerDetails1(String instid,String  accountno,String hcardno,String jdbctemplate2 ) {
		trace("********Generate customer details.....*************\n");
		enctrace("********Generate customer details.....*************\n");
		HttpSession session = getRequest().getSession();
		try{	
				
			 * 		
			trace("Getting customer details for the customer id [ "+customerid+" ] ");
			List custdata = cardregdao.getCustomerDetails(instid, customerid, jdbctemplate);
			trace("Got...[ "+custdata.size()+" ]");
			if( custdata == null || custdata.isEmpty() ){
				session.setAttribute("preverr", "S"); session.setAttribute("prevmsg",  ""  );
				cardregbean.setCusttype("new");
				cardregbean.setKycuser("1");
				trace("New Customer....");
				return "inscustomer_entry";
			}else{
				session.setAttribute("preverr", "S"); session.setAttribute("prevmsg",  ""  );
				trace("Existing customer...need to update....");
			}
			 
			trace("Getting document type....");
			List documenttypelist = commondesc.gettingDocumnettype(instid, jdbctemplate);
			if( documenttypelist==null || documenttypelist.isEmpty() ){
				session.setAttribute("preverr", "E"); session.setAttribute("prevmsg",  "No document type configured...."  );
				trace("No document type configured....");
				return "required_home";
			}
			
			cardregbean.setCustid(customerid);
			Iterator itr = custdata.iterator();
			while( itr.hasNext() ){
				Map temp = (Map)itr.next();  
				String todaydate = commondesc.getDate( "dd-MM-yyyy" );					
				cardregbean.setFirstname(  (String)temp.get("FNAME") );
				cardregbean.setLastname(  (String)temp.get("MNAME") );
				cardregbean.setMidname(  (String)temp.get("LNAME") );				 
				cardregbean.setFirstname(  (String)temp.get("FNAME") );
				cardregbean.setLastname(  (String)temp.get("LNAME") );
				cardregbean.setMidname(  (String)temp.get("MNAME") );
				cardregbean.setFahtername(  (String)temp.get("FATHER_NAME") );
				cardregbean.setMothername(  (String)temp.get("MOTHER_NAME") );
				cardregbean.setMstatus(  (String)temp.get("MARITAL_STATUS") ); 
				String maritaldesc = commondesc.getMaritalStatus( (String)temp.get("MARITAL_STATUS") );
				cardregbean.setMaritaldesc(maritaldesc);
				if(  (String)temp.get("SPOUSE_NAME") != null ){
					cardregbean.setSpname( (String)temp.get("SPOUSE_NAME") );
				}else{
					cardregbean.setSpname( "" );
				}
				cardregbean.setGender(  (String)temp.get("GENDER") );  
				String genderdesc = commondesc.getGenderDesc( (String)temp.get("GENDER") );
				cardregbean.setGenderdesc(genderdesc);
				
				cardregbean.setNationality(  (String)temp.get("NATIONALITY")  );
				cardregbean.setDob( (String) (Object)temp.get("DOB").toString() );
				cardregbean.setEmail(  (String)temp.get("E_MAIL") );
				cardregbean.setMobileno(  (String)temp.get("MOBILE") );
				cardregbean.setPhoneno(  (String)temp.get("PHONE_NO") );
				cardregbean.setOccupation( (String)temp.get("OCCUPATION") ); 
				cardregbean.setReqdocuement(  (String)temp.get("DOCUMENT_PROVIDED") ); 
				String documentname = commondesc.getDocumentName(instid, (String)temp.get("DOCUMENT_PROVIDED"), jdbctemplate);
				cardregbean.setDocumentdesc(documentname);
				cardregbean.setDocumentid(  (String)temp.get("DOCUMENT_NUMBER") ); 
				
				cardregbean.setPaddress1(  (String)temp.get("PA") );
				cardregbean.setPaddress2(   (String)temp.get("POST_ADDR2") );
				cardregbean.setPaddress3( (String)temp.get("POST_ADDR3") );
				cardregbean.setPaddress4( (String)temp.get("POST_ADDR4") ); 
				
				cardregbean.setResaddress1( (String)temp.get("RA") );
				cardregbean.setResaddress2( (String)temp.get("RES_ADDR2") );
				cardregbean.setResaddress3( (String)temp.get("RES_ADDR3") );
				cardregbean.setResaddress4( (String)temp.get("RES_ADDR4")  );
				
				//cardregbean.setNationality(  (String)temp.get("NATIONALITY")  );
				cardregbean.setCountrydesc(  commondesc.getNation( (String)temp.get("NATIONALITY"), jdbctemplate  ) );
				cardregbean.setAppdate(todaydate); 
				cardregbean.setPhotourl( (String)temp.get("PHOTO_URL") ); 
				cardregbean.setExistingcust(true);
				cardregbean.setCusttype("kyc");
				cardregbean.setKycuser("1");
				
				
				
			} 
			System.out.println("custdate--->"+custdata);	 
		}catch( Exception e ){
			session.setAttribute("preverr","E");
			session.setAttribute("prevmsg", "Exception : Could not continue the customer registeration process..." );
			e.printStackTrace();
			trace("Exception : while getting the customer details ... " + e.getMessage() );
			return this.registerInstCardHome();
		}
		if( actiontype.equals("EDIT")){
			trace("Edit customer.....");
			return "inscustomer_entry";
		}else{
			trace("View customer.....");
			return "inscustomer_view";
		}
	}
	*/
	
	public String viewAndEditRegHome(){
		trace("View or Edit registeration home....");
		HttpSession session = getRequest().getSession();
		try{ 
			
			return "customer_editview";
		}catch(Exception e ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.getMessage());
			e.printStackTrace();
			return "required_home";
		} 
	}
	
	public String editViewCustomerAction(){
		trace("View or Edit registeration home....");
		HttpSession session = getRequest().getSession();
		
		String instid = comInstId();
		String inputtype = getRequest().getParameter("inputtype");
		String actiontype = getRequest().getParameter("actiontype");
		String accountno = getRequest().getParameter("cardno").trim();
		String cardno ="";
		String customerid = "";
		 int custid=0;
		List appdata  = null;
		String hcardno = "";//new StringBuffer();
		try{
			trace("Input type is : " + inputtype);
			if( inputtype.equals("cardno")){
				cardno = getRequest().getParameter("cardno").trim();
				trace("Getting application number for the cardno[ "+cardno+" ]");
				appdata = cardregdao.getApplicationNumber(instid, cardno, "CARD_PRODUCTION", jdbctemplate);
				if( appdata == null || appdata.isEmpty()  ){
					session.setAttribute("preverr","E");
					session.setAttribute("prevmsg", "Could not get Application data for the card number [ "+cardno+" ] ..." );
					trace("Could not get Application data for the card number [ "+cardno+" ] ...");
					//return "required_home";
				}else{
					Iterator apitr = appdata.iterator();
					while( apitr.hasNext() ){
						Map apmp = (Map) apitr.next();
						cardregbean.setAppdate( (String)apmp.get("APP_DATE") );
						cardregbean.setAppno( (String)apmp.get("APP_NO") );
					}
				} 
				
				trace("Getting customer id for the cardno [ "+cardno+" ] ");
				customerid = cardregdao.getCustomerIdByCard(instid, cardno, jdbctemplate);
				//custid=Integer.parseInt(customerid);
				if( customerid == null ){
					session.setAttribute("preverr","E");
					session.setAttribute("prevmsg", "Could not get customer id for the card number [ "+cardno+" ] ..." );
					trace("Could not get customer id for the card number [ "+cardno+" ] ..." );
					return "required_home";
				}
			}else{
				 customerid = getRequest().getParameter("custid").trim();
				 custid=Integer.parseInt(customerid);
			}
			trace("Customer id : " + customerid );
			cardregbean.setCardno(cardno);
			cardregbean.setCustid(custid);
			cardregbean.setCusttype("UPDATECUSTOMER");
			
			trace("Getting nationality list...");
			/*List nationalitylist = commondesc.gettingNations( jdbctemplate );
			if( nationalitylist==null || nationalitylist.isEmpty() ){
				session.setAttribute("preverr", "E"); session.setAttribute("prevmsg",  "Could not get nationality list...."  );
				trace( "Could not get nationality list....");
				return "required_home";
			}
			cardregbean.setNationalitylist(nationalitylist);*/
			
			trace("Getting document type list...");
			List documenttypelist = commondesc.gettingDocumnettype(instid, jdbctemplate);			
			if( documenttypelist==null || documenttypelist.isEmpty() ){
				session.setAttribute("preverr", "E"); session.setAttribute("prevmsg",  "No document type configured...."  );
				trace("No document type configured....");
				return "required_home";
			}
			cardregbean.setDocumenttypelist(documenttypelist); 
			
			trace("Getting document type list...");
			
			/*List applicationdata = cardregdao.getApplicationNumber(instid, cardno, jdbctemplate);
			if( applicationdata==null || applicationdata.isEmpty() ){
				session.setAttribute("preverr","E");	session.setAttribute("prevmsg","No Application data found for ["+cardno+"] ");
				trace("No Application data found for ["+cardno+"] ");
				return registerInstCardHome(); 
			}else{
				Iterator apitr = applicationdata.iterator();
				while( apitr.hasNext() ){
					Map apmp = (Map) apitr.next();
					cardregbean.setAppdate( (String)apmp.get("APP_DATE") );
					cardregbean.setAppno( (String)apmp.get("APP_NO") );
				}
			} */
			//return this.getaccountdetailsfromcbs(instid, accountno);
			return "inscustomer_view";
		}catch(Exception e ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception : Could not continue the registeration process " );
			trace("Exception : could not continue the register : " + e.getMessage() );
			e.printStackTrace();
			return "required_home";
		} 
	}
	

	public String SaveCustomerReg() throws Exception
	  {
		trace("********Save customer details.....*************\n");
		enctrace("********Save customer details.....*************\n");
		 String instid = comInstId();
		 
		 HttpSession session = getRequest().getSession();
		 
		 IfpTransObj trasact = commondesc.myTranObject("CustomerRegister", txManager);
		 String acctnumber="",accttypevalue="",accttype="";
		String username=	comUsername();
		String usercode =	comUserCode();
		StringBuffer hashcard = new StringBuffer();
		String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
		trace("TTYTYT-->"+keyid);
		PadssSecurity padsssec = new PadssSecurity();
		String CDMK = commondesc.getPADSSDetailById111(keyid, jdbctemplate);
		//trace("TTYTYT-->"+CDMK);

		
		  try {
		Properties props=getCommonDescProperty();
		String EDPK=props.getProperty("EDPK");
		//trace("EDPK-->"+EDPK);
		
		    String	CIN_NO ="";int updateref = -1;
			//String hashcard = (String)getRequest().getParameter("hashcard");
			//System.out.println("hash card"+hashcard);
			//String cardnumber = (String)getRequest().getParameter("cardnumber");
			//System.out.println("hash card"+cardnumber);
			String orgchn = (String)getRequest().getParameter("orgchn");
			//System.out.println("hash card"+orgchn);
			
		
			
			
			        hashcard = padsssec.getHashedValue(orgchn+instid);
			        trace("hashcard-->"+hashcard);
					String CDPK=padsssec.decryptDPK(CDMK, EDPK);
					//trace("CDPK-->"+CDPK);
					String encryptedcard = padsssec.getECHN(CDPK, orgchn);
					trace("encryptedcard-->"+encryptedcard);
			
			
			
			
			 cardregbean.setCardno(hashcard.toString());
			 cardregbean.setCustname((String)getRequest().getParameter("customername"));
			
			String branchcode= getRequest().getParameter("branchcode");
			System.out.println("branchcode"+branchcode);
			int bracnh=Integer.parseInt(branchcode);
			 cardregbean.setBranch_code(bracnh);
			 
			 acctnumber = (String)getRequest().getParameter("accountno");
			 
			 cardregbean.setAccountno((String)getRequest().getParameter("accountno"));
			 
			//added by gowtham_220719
			String  ip=(String) session.getAttribute("REMOTE_IP");


			 
			 if(instid.equalsIgnoreCase("AZIZI")){
			 if (acctnumber.length() ==14){
				 
				 accttypevalue = acctnumber.substring(4, 6);				 
			 }else{
				 
				 accttypevalue = acctnumber.substring(5, 7);
			 }
			 accttype = commondesc.getAcctTypeValue(instid,accttypevalue,jdbctemplate);
			 }
			 else{
				 
				 accttype= (String)getRequest().getParameter("acctprodcode");
			 }
			 
			
			 
			 
			 
			cardregbean.setAccttype(accttype);
			
		 	cardregbean.setHascard((String)getRequest().getParameter("hashcard"));
			 
				String copro=	 (String)getRequest().getParameter("codeprod");
			  //int codeprod=Integer.parseInt(copro);
					 cardregbean.setCod_prod(copro);
					 String cusid=	 (String)getRequest().getParameter("customerid");
					 System.out.println("custid"+cusid);
					// int custid=Integer.parseInt(cusid);
		    
	    	 cardregbean.setProd_code((String)getRequest().getParameter("productcode"));
		 
		     cardregbean.setUsercode(usercode);
		     cardregbean.setUsername(username);
		     
		      //String WDLLMTAMT= (String) getRequest().getParameter("wdl_lmt_amt");
		      int WDL_LMT_AMT=0;//Integer.parseInt(WDLLMTAMT);
		     
		     cardregbean.setWdl_lmt_amt(WDL_LMT_AMT);
		     
		    // String WDLLMTCNT= (String) getRequest().getParameter("wdl_lmt_cnt");
		      int WDL_LMT_CNT=0;//Integer.parseInt(WDLLMTCNT);
		     
		     cardregbean.setWdl_lmt_cnt(WDL_LMT_CNT);
		     
		     //String PURLMTAMT= (String) getRequest().getParameter("pur_lmt_amt");
		      int PUR_LMT_AMT=0;//Integer.parseInt(PURLMTAMT);
		     
		     cardregbean.setPur_lmt_amt(PUR_LMT_AMT);


		    // String PURLMTCNT= (String) getRequest().getParameter("pur_lmt_cnt");
		      int PUR_LMT_CNT=0;//Integer.parseInt(PURLMTCNT);
		     
		     cardregbean.setPur_lmt_cnt(PUR_LMT_CNT);
		     		   
		     cardregbean.setOrder_ref_no((String)getRequest().getParameter("orderreferno"));
		     		     
		     cardregbean.setLmt_based_on((String)getRequest().getParameter("lmt_based_on"));
		     cardregbean.setEncrptchn((String)getRequest().getParameter("encrptchn"));
		     cardregbean.setChn_msk((String)getRequest().getParameter("chnmask"));
		     
	
		     cardregbean.setEmail((String)getRequest().getParameter("emailid"));
		     cardregbean.setPaddress1((String)getRequest().getParameter("address1"));
		     cardregbean.setPaddress2((String)getRequest().getParameter("address2"));
		     cardregbean.setPaddress3((String)getRequest().getParameter("address3"));
		     cardregbean.setPaddress4((String)getRequest().getParameter("address4"));
		     cardregbean.setPaddress5((String)getRequest().getParameter("address5"));
		     cardregbean.setPaddress6((String)getRequest().getParameter("address6"));
		     cardregbean.setCountrydesc((String)getRequest().getParameter("address6"));
		     
		     
		     String dob = getRequest().getParameter("dob");
		     cardregbean.setDob(dob);
	
		     cardregbean.setPhoneno((String)getRequest().getParameter("phonenumber"));
		     cardregbean.setGender((String)getRequest().getParameter("sex"));
		     cardregbean.setMothername((String)getRequest().getParameter("mothername"));
		     /*cardregbean.setCard_ccy((String)getRequest().getParameter("cardcurr"));
		     String currcode="";
		     
		     if(cardregbean.getCard_ccy().equalsIgnoreCase("AFN")){
		    	 
		    	 currcode = "971";
		    	 cardregbean.setCard_ccy(currcode);
		     }else if(cardregbean.getCard_ccy().equalsIgnoreCase("USD")){
		    	 currcode = "840";
		    	 cardregbean.setCard_ccy(currcode);
		     }else{*/
		    	 cardregbean.setCard_ccy((String)getRequest().getParameter("currcode"));
		    	 //addActionError("Currcode is Invalid");
		    	// return this.registerInstCardHome();
		    // }
		     
		     cardregbean.setCardtype(Integer.parseInt((String)getRequest().getParameter("cardtype").trim()));
		     
		     System.out.println("start to insert customerdet");
		     StringBuilder getLimitType = new StringBuilder();
				getLimitType.append("select LIMITTYPE from LIMITINFO where LIMIT_RECID in ( ");
				getLimitType.append("select b.LIMIT_ID from PRODUCT_MASTER a, INSTPROD_DETAILS B where A.PRODUCT_CODE in "); 
			
				/*getLimitType.append("(select PRODUCT_CODE from INSTPROD_DETAILS where  SUB_PROD_ID='"+cardregbean.getCod_prod()+"' AND INST_ID='"+instid+"') AND A.PRODUCT_CODE=B.PRODUCT_CODE and B.SUB_PROD_ID='"+cardregbean.getCod_prod()+"'");
				getLimitType.append("		) and rownum=1 ");
				
				enctrace("limitlistqry :"+ getLimitType.toString());    
				String limitbasedon= (String) jdbctemplate.queryForObject(getLimitType.toString(), String.class);*/
				
				///by gowtham-280819
				getLimitType.append("(select PRODUCT_CODE from INSTPROD_DETAILS where  SUB_PROD_ID=?  AND INST_ID=?) AND A.PRODUCT_CODE=B.PRODUCT_CODE and B.SUB_PROD_ID=? ");
						getLimitType.append("		) and rownum=? ");
						enctrace("limitlistqry :"+ getLimitType.toString());    
						String limitbasedon= (String) jdbctemplate.queryForObject(getLimitType.toString(),new Object[]{cardregbean.getCod_prod(),instid,cardregbean.getCod_prod(),1}, String.class);
				
				
				cardregbean.setLmt_based_on(limitbasedon);
				
				StringBuilder ai = new StringBuilder(); 
		     
				
				
				synchronized (this) 
				{
				trace("customerid based on cbs false");
				//customerid = commondesc.cinnumberGeneratoer(instid,jdbctemplate);
				List SEQ = GetOrderNumber( instid,jdbctemplate );
				String order="",cin="";
				Iterator itr1 = SEQ.iterator();
				while (itr1.hasNext()) {
					Map map = (Map) itr1.next();
					order = ((String) map.get("ORDER_REFNO"));
					cin = ((String) map.get("CIN_NO"));
					
			
				}
				 // Integer Counter = new Integer( 1 );
				int Counter = Integer.parseInt(cin)+1;
				trace("counter for cin"+Counter);
				CIN_NO = instid+GenerateRandom4Digit()+String.format("%09d", Counter);
				}

				
				String updaterefqry = commondesc.updatecustidcount(instid);
				updateref = jdbctemplate.update(updaterefqry,new Object[]{instid});
				
				 cardregbean.setCustidno(CIN_NO); 
			trace("TTTTT-->"+encryptedcard);
		  int updateinstcardproces = cardregdao.updatefornewcardreg(instid,usercode,cardregbean,encryptedcard,jdbctemplate);   
		  int insertinsacctdet = cardregdao.insertinsacctdetails(instid, cardregbean, jdbctemplate);
		  int insertinscustdet = cardregdao.insertinscustdetails(instid,cardregbean, jdbctemplate);
		  
			//int   insertcardaccountlink=cardregdao.insertcardaccountlink(instid,cardregbean, jdbctemplate);

		// int insertacctdet = cardregdao.insertacctdetails(instid, cardregbean, jdbctemplate);
		// int insertcustdet = cardregdao.insertcustdetails(instid,cardregbean, jdbctemplate);
		 //int movetoproduct = cardregdao.insertproduction(instid,cardregbean,jdbctemplate);
		  
		 

		  
		  
		
				trace("getting count updateinstcardproces insatmcms "+updateinstcardproces);
				trace("getting count insert insertinsacctdet "+insertinsacctdet); 
				trace("getting count inseert insertinscustdet "+insertinscustdet); 
				
			//	trace("getting count update atmcms "+insertacctdet);
				//trace("getting count insert acct "+insertcustdet); 
				//trace("getting count inseert movetoproduct "+movetoproduct); 
			
					//txManager.commit(trasact.status);
					 if (updateinstcardproces >0 &&insertinsacctdet > 0 && insertinscustdet > 0){
					    	txManager.commit(trasact.status);
					    	addActionMessage(cardregbean.getChn_msk() + " Card Registered Successfully .Waiting for Authorization ");					    	
							trace("Committed successfully....for the card no " + cardregbean.getChn_msk() );
							
							//---------------audit code edited by sardar on 11-12-15---------//	
							try{ 
								

								//added by gowtham_220719
								trace("ip address======>  "+ip);
								auditbean.setIpAdress(ip);
								
								String mcardno = commondesc.getMaskedCardbyproc(instid, encryptedcard,"INST_CARD_PROCESS","C", jdbctemplate);
								if(mcardno==null){mcardno=cardregbean.getEncrptchn();}
								auditbean.setActmsg("Instant card "+mcardno+" Mapped to Account number from maker[ "+acctnumber+" ] from "+branchcode+" ");
								auditbean.setUsercode(usercode);
								
								//auditbean.setUsercode(comUsername());
								
								auditbean.setActiontype("IM");
								
								auditbean.setAuditactcode("4109");  
								auditbean.setCardno(mcardno);
								auditbean.setApplicationid(cardregbean.getOrder_ref_no());
								auditbean.setAccoutnno(acctnumber);
								auditbean.setCustname((String)getRequest().getParameter("customername"));
								auditbean.setCardcollectbranch(branchcode);
								auditbean.setCin(cusid);
								
								//added by gowtham_010819
								String pcode=null;
								auditbean.setProduct((String)getRequest().getParameter("pcode"));
								
								//auditbean.setCardnumber(order_refnum[i]);
								//commondesc.insertAuditTrail(in_name, Maker_id, auditbean, jdbctemplate, txManager);
								commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
							 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage() ); }
							 								
							//--------------End on 11-12-15-------------//
					    }
					 
															 
					 else{
					    	txManager.rollback( trasact.status );
					    	addActionError("Could not Register the Card " + cardregbean.getChn_msk());
							trace("Exception : While Register the cards");
							return "required_home";
					    }
					
				
				
			  
				
				
			} catch (Exception e) {
				txManager.rollback( trasact.status );
				addActionError("Unable to Register the Cards");   
				trace("Exception : While Register Issue the cards : " + e.getMessage() );
				return "required_home";
			} 
		  
		 // return this.registerInstCardHome();
		  return this.findbinsformapping();
	  }
	
	
	public String saveCustDetail() {
		trace("Save customer details....");
		enctrace("Save customer details....");
		HttpSession session = getRequest().getSession();
		 
		IfpTransObj trasact = commondesc.myTranObject("ADDCUSTOMER", txManager);
		
		commondesc.printLog( " save customer details " + getRequest().getParameter("custtype") );
		String instid = comInstId();
		String usercod = comUserCode();
		
		
		String usernam=	comUsername1();
		
		String custype =  getRequest().getParameter("custtype");
		String customerid = "";
		String cardno = ( String ) getRequest().getParameter("cardno"); 
		 
		String photourl = "", signatureurl="", idproofurl="";
		String newphotoname = "",newsignaturename = "",newidproofname = "";
		
		String kycuser = null;
		String makerid = "",checkerid="",makerdate="",mkckflag="", ckdate="";
		try{  
			String act =(String) session.getAttribute("act");
			
			makerid = usercod;
			checkerid = makerid;
			mkckflag = "P";
			ckdate = "sysdate";
			makerdate = "SYSDATE";
			
			if( act != null ){
				if ( act.equals("M")){  
					makerid = usercod; 
				    mkckflag = "M";
					ckdate = commondesc.default_date_query;
					makerdate = "SYSDATE";
					cardregbean.setMakerid(makerid);
				}else {  // D 
					 
					makerid = usercod;
					checkerid = makerid;
					mkckflag = "P";
					ckdate = "sysdate";
					makerdate = "SYSDATE";
				}
			}
			
			
			String hashcard = (String)getRequest().getParameter("hashcard");
			//System.out.println("hash card"+hashcard);
		
			 cardregbean.setCardno(hashcard);
			 
			 cardregbean.setFirstname((String)getRequest().getParameter("customername"));
			String branchcode= getRequest().getParameter("branchcode");
			System.out.println("branchcode"+branchcode);
			int bracnh=Integer.parseInt(branchcode);
			 cardregbean.setBranch_code(bracnh);
			 
			 cardregbean.setAccountno((String)getRequest().getParameter("accountno"));
		 	cardregbean.setHascard((String)getRequest().getParameter("hashcard"));
			 
				String copro=	 (String)getRequest().getParameter("codeprod");
			  //int codeprod=Integer.parseInt(copro);
					 cardregbean.setCod_prod(copro);
					 String cusid=	 (String)getRequest().getParameter("customerid");
					 int custid=Integer.parseInt(cusid);
		     cardregbean.setCustid(custid); 
	    	 cardregbean.setProd_code((String)getRequest().getParameter("productcode"));
		 
		  
		     
			cardregbean.setMakerid(makerid);
			cardregbean.setMakerdate(makerdate);
			cardregbean.setCheckerdate(ckdate);
			cardregbean.setCheckerid(checkerid);
			cardregbean.setMkckflag(mkckflag);
			
			cardregbean.setFirstname(	getRequest().getParameter("customername") );
			
			cardregbean.setMothername(	getRequest().getParameter("mothername") );
			cardregbean.setGender(		getRequest().getParameter("gender") );
			
			//cardregbean.setMstatus (	getRequest().getParameter("mstatus") );
		//	cardregbean.setSpname(		getRequest().getParameter("spname") );
			//cardregbean.setOccupation(	getRequest().getParameter("occupation") ); 
			//cardregbean.setReqdocuement(	getRequest().getParameter("documentname") );
			//cardregbean.setDocumentid(	getRequest().getParameter("documentnumber") );  
			//cardregbean.setNationality(	getRequest().getParameter("nations") ); 
			cardregbean.setDob(			getRequest().getParameter("dob") );
			//cardregbean.setMobileno(	getRequest().getParameter("mobileno") );
			cardregbean.setPhoneno(	getRequest().getParameter("phonenumber") );
			cardregbean.setEmail(	getRequest().getParameter("emailid") );  
			
			cardregbean.setResaddress1(	getRequest().getParameter("address1") );
			cardregbean.setResaddress2(	getRequest().getParameter("address2") );
			cardregbean.setResaddress3(	getRequest().getParameter("address3") );
			//cardregbean.setResaddress4(	getRequest().getParameter("resaddress4") );
				//cardregbean.setAppno( getRequest().getParameter("appno")   );
			//cardregbean.setAppdate( getRequest().getParameter("appdate")   );
					
			commondesc.printLog( "getRequest().getParameter(residentreq) " + getRequest().getParameter("residentreq")  );
		    System.out.println( "getRequest().getParameter(residentreq) " + getRequest().getParameter("residentreq")  );
			if( getRequest().getParameter("residentreq") == null){
				commondesc.printLog( " residren req check box checked ");
				System.out.println( " residren req check box checked ");				
				cardregbean.setPaddress1(	getRequest().getParameter("paddress1") );
				cardregbean.setPaddress2(	getRequest().getParameter("paddress2") );
				cardregbean.setPaddress3(	getRequest().getParameter("paddress3") );
				cardregbean.setPaddress4(	getRequest().getParameter("paddress4") );
				cardregbean.setPaddress5(   getRequest().getParameter("paddress5") );
			}			
			cardregbean.setKycuser("1");
			trace("Getting customer id ");
			trace("Register type : " + custype);
			
			if( custype.equals("new")){ 
				customerid =  commondesc.cinnumberGeneratoer(instid, jdbctemplate);
				
				
				commondesc.printLog("The CIN Generated is : "+customerid);
				if(customerid.equals("E") || customerid.equals("N")){
					session.setAttribute("preverr","E");
					session.setAttribute("prevmsg","Error While Getting The CIN ");
					return registerInstCardHome(); 
				}
				kycuser = "0";
				cardregbean.setCustid(custid);
				
				if( getUploadedphoto() != null ){
					newphotoname = customerid+"_"+commondesc.getDateTimeStamp()+".jpg";
				}
				
				if( getUploadsignature() != null ){
					newsignaturename = customerid+"_"+commondesc.getDateTimeStamp()+".jpg";
				}
				
				if( getUploadidproof() != null ){
					newidproofname = customerid+"_"+commondesc.getDateTimeStamp()+".jpg";
				}
				 
				cardregbean.setPhotourl(newphotoname);
				cardregbean.setSignatureurl(newsignaturename);
				cardregbean.setIdproofurl(newidproofname);
				
				int newcustmove = cardregdao.insertCustomerDetailsProduction( instid, customerid, cardregbean, jdbctemplate );
				trace("Inserting customer details....for the customer id [ "+customerid+" ] Got : " + newcustmove); 
				if( newcustmove != 1 ){
					txManager.rollback(trasact.status);
					session.setAttribute("preverr","E");
					session.setAttribute("prevmsg","Could not move the data to production for the card ["+cardno+"] ");
					trace("Could not move the data to production for the card ["+cardno+"] ");
					return registerInstCardHome(); 
				}
				trace("Updating account link....");
				int updcardlink = cardregdao.updateCardAccountLink(instid, cardno, customerid, jdbctemplate);
				trace("Got : "+ updcardlink );
				if( updcardlink < 0 ){
					txManager.rollback(trasact.status);
					session.setAttribute("preverr","E");
					session.setAttribute("prevmsg","Could not update the account link...");
					trace("Could not update the account link...");
					return registerInstCardHome(); 
				}
				trace("Updating new customerid ");
				int updcustid = cardregdao.updateCardOriginalCustid(instid, cardno, customerid, jdbctemplate);
				trace("Got : " + updcustid );
				if( updcustid < 0 ){
					txManager.rollback(trasact.status);
					session.setAttribute("preverr","E");
					session.setAttribute("prevmsg","Could not update customer ..");
					trace("Could no update customer id ...got rolled back...");
					return registerInstCardHome(); 
				}
			}else if( custype.equals("kyc")){
				customerid =  getRequest().getParameter("custid");
				trace("Update customer prodcution...");
				
				if( getUploadedphoto() != null ){
					newphotoname = customerid+"_"+commondesc.getDateTimeStamp()+".jpg";
				}
				
				if( getUploadsignature() != null ){
					newsignaturename = customerid+"_"+commondesc.getDateTimeStamp()+".jpg";
				}
				
				if( getUploadidproof() != null ){
					newidproofname = customerid+"_"+commondesc.getDateTimeStamp()+".jpg";
				}
				cardregbean.setPhotourl(newphotoname);
				cardregbean.setSignatureurl(newsignaturename);
				cardregbean.setIdproofurl(newidproofname);
				
				int updcust = cardregdao.updateCustomerProduction(instid, customerid, cardregbean, jdbctemplate);
				trace("Got : "+ updcust );
				if( updcust < 0 ){
					txManager.rollback(trasact.status);
					session.setAttribute("preverr","E");
					session.setAttribute("prevmsg","Could not update customer ..");
					trace("Could not update customer ...got rolled back");
					return registerInstCardHome(); 
				} 
				
				int updmobileno = commondesc.updateMobileNumberToProduction(instid, cardno, cardregbean.getPhoneno(), jdbctemplate);
				trace("Updating Mobile Number to prodcution...");
				if( updmobileno < 0 ){
					txManager.rollback(trasact.status);
					session.setAttribute("preverr","E");
					session.setAttribute("prevmsg","Could not able to update mobile number....");
					trace("Could not update customer ...got rolled back");
					return registerInstCardHome(); 
				}
				/*int updapplication = cardregdao.updateApplicationData(instid, cardno, cardregbean.getAppno(), cardregbean.getAppdate(), jdbctemplate);
				trace("Got : " + updapplication );
				if( updapplication < 0 ){
					txManager.rollback(status);
					session.setAttribute("preverr","E");
					session.setAttribute("prevmsg","Could not continue the proces...error on update application data");
					trace("Could not continue the proces...error on update application data...got rolled back");
					return registerInstCardHome(); 
				}*/
			}
			
			
			
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
			 
			
			if( uploadedphoto != null  ){
				File fileToCreate = new File(photodirfile, newphotoname); 
				FileUtils.copyFile(uploadedphoto, fileToCreate); 
				photourl = newphotoname;
				cardregbean.setPhotourl(photourl);
				
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
			
			cardregbean.setKycuser(kycuser);
			
			 
					
			trace("Update customer id count....");
			int updatecin = cardregdao.updateCustomerIdCount(instid, jdbctemplate);
			if( updatecin  < 0 ){
				txManager.rollback(trasact.status);
				session.setAttribute("preverr","E");	session.setAttribute("prevmsg","Could not update the customer id.");
				trace("Could not update the customer id.");
				return registerInstCardHome(); 
			}
			 
			txManager.commit(trasact.status);
			session.setAttribute("preverr","S");
			session.setAttribute("prevmsg"," Customer Registered Successfully for the card [ "+cardno+" ]. Generated Customer id is  " + customerid );
			trace("Committed successfully....");
				
			/*** MAIL BLOCK ****/
			/*IfpTransObj transactmail = commondesc.myTranObject(dataSource); 
			try {
				String alertid = this.parentid; 
				if( alertid != null && ! alertid.equals("000")){
					String keymsg = "Customer Registered with the customer id " + customerid ;
					int mail = comutil.sendMail( instid, alertid, keymsg, jdbctemplate, session, getMailSender() );
					System.out.println( "mail return__" + mail);
				} 
			} catch (Exception e) {  e.printStackTrace(); }
			  finally{
				transactmail.txManager.commit(transactmail.status);
			} */
			/*** MAIL BLOCK ****/
			
			session.setAttribute("preverr","S");
			session.setAttribute("prevmsg","KYC Done Successfully. Card number [ "+cardno+" ]  Customer id[ "+customerid+" ]"); 
			trace("KYC Done Successfully. Card number [ "+cardno+" ]  Customer id[ "+customerid+" ] \n\n");
			return this.registerInstCardHome();  
			
		}catch (Exception e) { 
			txManager.rollback(trasact.status); 
			session.setAttribute("preverr","E");
			session.setAttribute("prevmsg","Exception: Unable to register customer...."); 
			trace("Exception : could not register customer : " + e.getMessage() );
			e.printStackTrace();
		} 
		return this.registerInstCardHome();  
	}
	
	public String authInstCustomerHome(){ 
		String instid = comInstId(); 
		
		HttpSession session = getRequest().getSession();
		//String br_id = (String)session.getAttribute("BRANCHCODE");
		String usertype = comuserType();
		List pers_prodlist=null,br_list=null;
		String inst_id =comInstId();
		
		String br_id = comBranchId();
		
		
		/*if(br_id.equals("000"))
		{
			session.setAttribute("preverr", "E"); 
			session.setAttribute("prevmsg", " Please Login With Branch User..."  );
			return "required_home";
		
		}*/
		String processtype = "INST",processstatus="06",mkck_status="M";
		try{			
			/* List branchlist =  this.commondesc.generateBranchList(instid, jdbctemplate);
			 if( branchlist== null || branchlist.isEmpty() ){
				 session.setAttribute("preverr", "E");	session.setAttribute("prevmsg", "No Branch Configured....");
				 return "required_home";
			 }
			 cardregbean.setBranchlist(branchlist);*/
			 
			 /*List prodlist = commondesc.getProductList( instid, jdbctemplate, session ); 
			 if( prodlist== null || prodlist.isEmpty() ){
				 session.setAttribute("preverr", "E");	session.setAttribute("prevmsg", "No Product Configured....");
				 return "required_home";
			 }
			 cardregbean.setProdlist(prodlist);
			 */
			/* trace("Getting product list....");
				List prodlist = cardactdao.getinstProductList( instid, processstatus, processtype, jdbctemplate );
				if( prodlist==null || prodlist.isEmpty() ){
					addActionError( "No Cards for Issue....");
					trace("No Cards for Issue....");
					return "required_home";
				}
				ListIterator proditr = prodlist.listIterator();
				while( proditr.hasNext() ){
					Map  prodmp = (Map) proditr.next();
					String prodcode = (String)prodmp.get("PRODUCT_CODE");
					String productdesc = commondesc.getProductdesc(instid, prodcode,  jdbctemplate);
					prodmp.put("PRODUCT_DESC", productdesc);
					proditr.remove();
					proditr.add(prodmp);
				}
			 cardactbean.setProdlist(prodlist);*/
			 
			 int x= commondesc.reqCheck().requiredCheck(instid, session, jdbctemplate);
				if( x < 0 ){
					return "required_home";
				}
				
				System.out.println("Inst Id===>"+inst_id+"  Branch Code ===>"+br_id);
				if (usertype.equals("INSTADMIN")) {
					System.out.println("Branch list start");
					br_list = commondesc.InstgetBranchCodefmProcess(inst_id, processstatus, mkck_status, jdbctemplate);
					System.out.println("Branch list "+br_list);
					if(!(br_list.isEmpty())){
						setBranchlist(br_list);
						System.out.println("Branch list is not empty");
						//setCardgenstatus('Y');
					}
					else{
						addActionError("No Cards Waiting For Card Issuance ... ");
						System.out.println("Branch List is empty ");
						return "required_home";    
						
					}
				}
				pers_prodlist=commondesc.InstgetProductListBySelected(inst_id, processstatus, mkck_status, jdbctemplate);
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
			session.setAttribute("prevmsg", "Excption : could not continue the process ");
			e.printStackTrace();
			trace("Exception : while authorize customer : " + e.getMessage() );
			return "required_home";
		}
		return "inscustomer_auth";
	}
	
	public String authInstCustomerList() throws Exception
	{
		HttpSession session = getRequest().getSession();	
		String card_status = "06";
		String mkckstatus = "M",processtype="INST";
		String  datefld = "ISSUE_DATE";
		String instid = comInstId(); 
		String branch = getRequest().getParameter("branchcode");
		trace("branch code checking-->"+branch);
		String prodcode = getRequest().getParameter("cardtype");
		trace("prodcode code checking-->"+prodcode);
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		
		
		String filtercond = commondesc.filtercondcollectbranch(prodcode,branch,fromdate,todate,datefld);
		trace("filter code checking-->"+filtercond);
		
		//commondesc.printLog( "filter condition " + filtercond );
		//List waitingforcardpin = commondesc.waitingForInstCardProcessCards(instid, card_status,  mkckstatus, filtercond, jdbctemplate);
		//test	
		
		List waitingforcardpin = cardactdao.authgetCardActivationWaitingCards(instid,prodcode, card_status, processtype,filtercond, jdbctemplate );
        trace("waitingforcardpin"+waitingforcardpin);
		if( waitingforcardpin.isEmpty() )
		{
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " No records found for custauth" ); 
			return this.authInstCustomerHome();
		} 
		
		 ListIterator awtitr = waitingforcardpin.listIterator();
		 String product_desc = commondesc.getProductdesc(instid, prodcode, jdbctemplate);
		 while( awtitr.hasNext() )
		 {
			 Map awtmp = (Map)awtitr.next();
			 String subproductode = (String)awtmp.get("SUB_PROD_ID");
			 trace("subproductode "+subproductode);
			 String custid = (String)awtmp.get("CIN");
			 trace("customerid "+custid);
			 String MAKER_ID = (String)awtmp.get("MAKER_ID");
			// String accountno = (String)awtmp.get("ACCT_NO");
			
			 if( custid.isEmpty() )
			 {
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " No records found for custauth .." ); 
					return this.authInstCustomerHome();
				} 
			 
			 String accountno=commondesc.getAccounno(instid,custid,jdbctemplate);		 
			 String subprod_desc = commondesc.getSubProductdesc(instid, subproductode,  jdbctemplate);
			 String getusrname = commondesc.getusrname(instid, MAKER_ID,  jdbctemplate);
			 awtmp.put("mkuser", getusrname);
			 awtmp.put("accountno", accountno);
			 awtmp.put("SUBPRODUCUT_DESC", subprod_desc );
			 awtmp.put("PRODUCUT_DESC", product_desc );
		 }
		cardregbean.setInstorderlist(waitingforcardpin);  
		return "inscustomer_auth_list";
	}
	
	
	public synchronized String  InstconfirmCardissueorders()
	{
		trace( "*****confirmCardissueorders*****");enctrace( "*****confirmCardissueorders*****");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("CONFIRMISSUE", txManager);
		//String actiontype = (String)session.getAttribute("ACTIONTYPE");
		//System.out.println("Action Type is ----> "+actiontype);
		String instid = comInstId();
		String userid =comUserCode();
		String username = comUsername1();
		
		String update_qury="";
		String actiontype="INST";
		String ecard[] = getRequest().getParameterValues("instorderrefnum");
		
		/*String ecard = getRequest().getParameter("ecard");
		trace("checking comming ecard number--->"+ecard);
		String mcard = getRequest().getParameter("mcard");
		trace("checking comming mcard number--->"+mcard);*/
		
		
		int ordercount = ecard.length;
		int updatecount = 0;
		int personalcardissue = 0;
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		
		String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
		PadssSecurity padsssec = new PadssSecurity();
		String CDMK = "",CDPK="";
		
		//added by gowtham_220719
				String  ip=(String) session.getAttribute("REMOTE_IP");


		
		try {
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			Properties props=getCommonDescProperty();
			String EDPK=props.getProperty("EDPK");
			
			Iterator secitr = secList.iterator();
				while(secitr.hasNext())
				{
					Map map = (Map) secitr.next(); 
					CDMK = ((String)map.get("DMK"));
					//eDPK = ((String)map.get("DPK"));
					CDPK=padsssec.decryptDPK(CDMK, EDPK);
				} 
		} 
		catch (Exception e1)
		{		
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return authInstCustomerHome();
		}
		
		String cafflag = "";
		int update_status = 0,del_process = 0,del_process1=0,del_pin=0,update_product = 0,updezcardinfo=0,upd_process=-1;
		String cardstatus = CARDACTIVATEDCODE;
		String switchstatus = commondesc.getSwitchCardStatus(instid, cardstatus, jdbctemplate);
		String statusmsg="";
		
		String tablename="INST_CARD_PROCESS";
		try{
			for(int i=0;i<ordercount;i++)
			{
				trace("checking comming card number--->"+ecard[i]);
				cafflag = commondesc.gettingInstCAFstatus(ecard[i],padssenable, instid, jdbctemplate);
				trace("CAF REC STATUS : "+cafflag);
				trace("ecard:::"+ecard[i]);     
				
				if (  getRequest().getParameter("authorize") != null )
				{							
				statusmsg ="Card Issued Successfully";
					if( cafflag.equals("D") ) 
					{
						trace("DAMAGE CARD PART...................");
						StringBuilder update_production = new StringBuilder();
						
					/*	update_production.append("UPDATE CARD_PRODUCTION SET CARD_STATUS='"+cardstatus+"', STATUS_CODE='"+switchstatus+"',MKCK_STATUS = 'P' ,  ISSUE_DATE=(SYSDATE),ACTIVE_DATE=(sysdate),MAKER_ID='"+userid+"', " );
						update_production.append("MAKER_DATE=(sysdate) WHERE INST_ID='"+instid+"' ");
//						if(padssenable.equals("Y")){		
//						update_production.append("AND HCARD_NO='"+Hashchn[i]+"' ");
//						}else{
//						update_production.append("AND ORG_CHN='"+ecard[i]+"' ");	    
//						//}
						
						enctrace("update_production :"+update_production.toString());
						update_product = jdbctemplate.update(update_production.toString()); */


////bygowtham-280819
update_production.append("UPDATE CARD_PRODUCTION SET CARD_STATUS=?, STATUS_CODE=?,MKCK_STATUS = ? ,  ISSUE_DATE=(SYSDATE),ACTIVE_DATE=(sysdate),MAKER_ID=?, " );
update_production.append("MAKER_DATE=(sysdate) WHERE INST_ID=? ");
update_production.append("AND ORG_CHN=? ");	    
enctrace("update_production :"+update_production.toString());
update_product = jdbctemplate.update(update_production.toString(),new Object[]{cardstatus,switchstatus,"p",userid,instid,ecard[i]}); 

						
						// by siva
						
						String hcard_no="SELECT HCARD_NO FROM INST_CARD_PROCESS_HASH WHERE ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM INST_CARD-PROCESS WHERE ORG_CHN='"+ecard[i]+"') ";
						trace("hcard-no query check -->"+hcard_no);
						
						StringBuilder updEzcard = new StringBuilder();
						
					/*	updEzcard.append("UPDATE EZCARDINFO set ");
						updEzcard.append("PANSEQNO = '00' ");
						updEzcard.append(",STATUS = '"+switchstatus+"' WHERE CHN = '"+hcard_no+"' AND INSTID='"+instid+"'");
						
						trace("updEzcard :"+updEzcard.toString());
						updezcardinfo = jdbctemplate.update(updEzcard.toString()); 
						*/
						
						
						
						///by gowtham-280819
						updEzcard.append("UPDATE EZCARDINFO set ");
						updEzcard.append("PANSEQNO = ? ");
						updEzcard.append(",STATUS = ? WHERE CHN =? AND INSTID=? ");
						trace("updEzcard :"+updEzcard.toString());
						updezcardinfo = jdbctemplate.update(updEzcard.toString(),new Object[]{"00",switchstatus,hcard_no,instid}); 
						
						
						StringBuilder deletefromProcess = new StringBuilder();
						StringBuilder deletefromProcess1 = new StringBuilder();
						/*deletefromProcess.append("DELETE FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND CARD_NO='"+ecard[i]+"' ");
						deletefromProcess1.append("DELETE FROM INST_CARD_PROCESS_HASH WHERE INST_ID='"+instid+"' AND HCARD_NO='"+hcard_no+"' ");
						*/
						
						///by gowtham-280819
						deletefromProcess.append("DELETE FROM INST_CARD_PROCESS WHERE INST_ID=? AND CARD_NO=? ");
						deletefromProcess1.append("DELETE FROM INST_CARD_PROCESS_HASH WHERE INST_ID=? AND HCARD_NO=? ");
						
						
						
						
						
						/*if(padssenable.equals("Y")){		
							deletefromProcess.append("AND HCARD_NO='"+Hashchn[i]+"' ");
							}else{
							deletefromProcess.append("AND CARD_NO='"+Hashchn[i]+"' ");	
							}*/
						
						enctrace("deletefromProcess::::::::"+deletefromProcess.toString());
						
						trace("update_product :"+update_product+" del_process :"+del_process);
						/*del_process1 = jdbctemplate.update(deletefromProcess1.toString());
						del_process = jdbctemplate.update(deletefromProcess.toString());     */
						
						
						
						
						///by gowtham-280819
						del_process1 = jdbctemplate.update(deletefromProcess1.toString(),new Object[]{instid,hcard_no});
						del_process = jdbctemplate.update(deletefromProcess.toString(),new Object[]{instid,ecard[i]});     
						
						
						
						//del_pin = jdbctemplate.update(delete_pin);
						
						
						
						if( update_product >0 && del_process >0 && updezcardinfo>0 && del_process1 >0 )	{ 
							String mifpcode = "DAMAGE";
							updatecount = updatecount + 1;
						}else{
							System.out.println("Process Breaked ====> 1");
							break;
						}		
						trace("..................DAMAGE CARD PART");	
					}
					
					//Reissued card with new card . . sardar 13/04/2017...
					if( cafflag.equals("NR") ) 
					{				
						trace("Reissued card with new card . . .");
						StringBuilder reissuecardupdate_production = new StringBuilder();
						String usedchnnNEWreissedcard="",orgchn="",orderfornewcard="",newhcardno="",
								usedecardno="",cardcon="",newhencyptedcardno="",cardnumber="",
								PIN_OFFSET="",EXPIRYDATE="",cardtype="";
						StringBuffer usedhcardno=new StringBuffer();
						List carddet = cardregdao.getrejectcarddetailsforResissue(instid,ecard[i],jdbctemplate);
						trace("cardforcbs"+carddet);
						int ezcustomerid=0;
						Iterator itrat = carddet.iterator();
						
							while(itrat.hasNext())
							{
								Map map = (Map) itrat.next(); 
								orderfornewcard=((String)map.get("ORDER_REF_NO"));
								cardtype=((String)map.get("CARD_TYPE_ID"));
								
								newhcardno=((String)map.get("HCARD_NO"));
								newhencyptedcardno=((String)map.get("CARD_NO"));
								EXPIRYDATE  = map.get("EXPIRYDATE").toString();
								PIN_OFFSET=((String)map.get("PIN_OFFSET"));
								orgchn=((String)map.get("ORG_CHN"));	
						        usedchnnNEWreissedcard=((String)map.get("USED_CHN"));
							}
						
							
							usedhcardno = padsssec.getHashedValue(usedchnnNEWreissedcard+instid);
							usedecardno = padsssec.getECHN( CDPK, usedchnnNEWreissedcard);
								trace("usedhcardno reissue"+usedhcardno.toString());
				 
								/*String binno="SELECT PRODUCT_CODE FROM INST_CARD_PROCESS WHERE ORG_CHN='"+orgchn+"' ";
								String binnumber=(String)jdbctemplate.queryForObject(binno,String.class);*/
								
								
								
								///by gowtham-280819
								String binno="SELECT PRODUCT_CODE FROM INST_CARD_PROCESS WHERE ORG_CHN=? ";
								String binnumber=(String)jdbctemplate.queryForObject(binno,new Object[]{orgchn},String.class);
								
								
								String statuscode="";
						if(binnumber.equals("5018171005")){
						
							 statuscode="97";
						}
						if(binnumber.substring(0, 6).equals("551270")){
							 statuscode="53";
						}
						
						else if(binnumber.equals("900404")){
							 statuscode="53";
						}		
						else{
							 statuscode="50";
						}
								
								
						 if(padssenable.equals("Y"))
						 {
							 cardcon="HCARD_NO";
							 cardnumber=newhcardno;				 
						 }
						 
						 else
						 {
							 cardcon="CARD_NO";
							 cardnumber=newhencyptedcardno;						 
						 }
							
							String deletefromProcess ="";
							
							
							String movetoproduction = "INSERT INTO CARD_PRODUCTION (INST_ID,ORDER_REF_NO,BINMCARD_NO,ACCOUNT_NO,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE," +
									"EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE,REG_DATE,RECV_DATE,ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT," +
									"ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,ADDON_FLAG,ADDON_CNT,REISSUE_DATE,REISSUE_CNT,REPIN_DATE,REPIN_CNT,DAMAGE_DATE,BLOCK_DATE,HOT_GENDATE,CLOSE_DATE,PIN_OFFSET,PIN_RETRY_CNT,AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO,CARDISSUETYPE,RENEWALFLAG,CARD_COLLECT_BRANCH)" +
									"(SELECT INST_ID,ORDER_REF_NO,BIN,MCARD_NO,ACCT_NO,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY, CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,'05','A','50',CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE," +
									"PRE_FILE,REG_DATE,RECV_DATE,(SYSDATE),SYSDATE,'"+userid+"',(SYSDATE),CHECKER_ID,CHECKER_DATE,'P',SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT," +
									"ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,'','0',SYSDATE,'1',SYSDATE,'0',SYSDATE,SYSDATE,SYSDATE,sysdate,PIN_OFFSET,'0',AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO,CARDISSUETYPE,RENEWALFLAG,CARD_COLLECT_BRANCH FROM  INST_CARD_PROCESS" +
									" WHERE INST_ID='"+instid+"' AND ORG_CHN='"+newhencyptedcardno+"' )";
									enctrace("movetoproduction _reissue: " + movetoproduction );
									
									// BY SIVA
									String movetoproduction1 = "INSERT INTO CARD_PRODUCTION_HASH (INST_ID,ORDER_REF_NO,BIN,HCARD_NO,ACCOUNT_NO,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE," +
											"MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE)" +
											"(SELECT INST_ID,ORDER_REF_NO,BIN,HCARD_NO,ACCT_NO, CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE," +
											" '"+userid+"',(SYSDATE),CHECKER_ID,CHECKER_DATE FROM  INST_CARD_PROCESS_HASH" +
											" WHERE INST_ID='"+instid+"' AND HCARD_NO='"+newhcardno+"' )";
											enctrace("movetoproduction _reissue: " + movetoproduction );
							
							 StringBuilder crdinf_3 = new StringBuilder();		
								crdinf_3.append("INSERT INTO EZCARDINFO ");
								crdinf_3.append("(INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME,CVV, PANSEQNO ) ");
								crdinf_3.append(" SELECT INSTID,'"+newhcardno+"' ,'"+cardtype+"' ,CUSTID,TXNGROUPID,LIMITFLG,TO_DATE('"+EXPIRYDATE+"','MM/DD/YYYY'),'"+statuscode+"','"+PIN_OFFSET+"' ,OLDPINOFFSET,TPINOFFSET,OLDTPINOFFSET,PINRETRYCOUNT,TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME,CVV, PANSEQNO FROM ");
								crdinf_3.append( "  EZCARDINFO WHERE CHN='"+usedhcardno+"' AND STATUS='62'");
							enctrace("crdinf_3:::::"+crdinf_3.toString());
							
							String deleteinstprocess="DELETE FROM INST_CARD_PROCESS WHERE ORG_CHN='"+newhencyptedcardno+"' ";
							
							// BY SIVA
							String deleteinstprocess1="DELETE FROM INST_CARD_PROCESS_HASH WHERE HCARD_NO='"+newhcardno+"'";
							
							String oldorderno="SELECT ORDER_REF_NO FROM CARD_PRODUCTION WHERE ORG_CHN='"+usedchnnNEWreissedcard+"'AND ROWNUM=1";
							enctrace("oldorderno:::::"+oldorderno+"\n"+deleteinstprocess);
							String orderforoldcar=(String)jdbctemplate.queryForObject(oldorderno,String.class);
							
						//int updatereffornewcard = cardregdao.updateordernofornewcard(instid, orderfornewcard,orderforoldcar, jdbctemplate);
						int updateezauthrel = cardregdao.updateordeezauthrelfornewcard(instid, newhcardno.toString(),usedhcardno.toString(), jdbctemplate);

					   int  ezcardinfo = jdbctemplate.update(crdinf_3.toString());
					   int production_insert = jdbctemplate.update(movetoproduction);
					   int  delteinstprocess1 = jdbctemplate.update(deleteinstprocess1);
					   int  delteinstprocess = jdbctemplate.update(deleteinstprocess);
					   
					   int production_insert1 = jdbctemplate.update(movetoproduction1);
					  
					   
					   trace(+updateezauthrel+"_"+ezcardinfo+"_"+production_insert+"_"+delteinstprocess);
						if( updateezauthrel > 0 && ezcardinfo >0 && production_insert > 0 && delteinstprocess > 0 && production_insert1 > 0 && delteinstprocess1 > 0){
							updatecount = updatecount + 1;
							trace("updatecount for new reissue cards"+updatecount);
						}	
					
					}
		
					
					///  DAMAGE WITH EXPIRY DATE
						if( cafflag.equals("DE") ) 
						{
						trace("DAMAGE CARD ATE WITH EXPIRY DATE...................");				
						StringBuilder update_production = new StringBuilder();
						
						/*update_production.append("UPDATE CARD_PRODUCTION SET CARD_STATUS='"+cardstatus+"', STATUS_CODE='"+switchstatus+"',MKCK_STATUS = 'P' ,  ISSUE_DATE=(SYSDATE),ACTIVE_DATE=(sysdate),MAKER_ID='"+userid+"', " );
						update_production.append("MAKER_DATE=(sysdate) WHERE INST_ID='"+instid+"' ");
						update_production.append("AND ORG_CHN='"+ecard[i]+"' AND INST_ID='"+instid+"'");*/
						
						
						
///by gowtham-280819
						
						update_production.append("UPDATE CARD_PRODUCTION SET CARD_STATUS=?, STATUS_CODE=?,MKCK_STATUS = ? ,  ISSUE_DATE=(SYSDATE),ACTIVE_DATE=(sysdate),MAKER_ID=?, " );
						update_production.append("MAKER_DATE=(sysdate) WHERE INST_ID=? ");
						update_production.append("AND ORG_CHN=? AND INST_ID=? ");
						
						// BY SIVA 
						/*if(padssenable.equals("Y"))
						{		
						update_production.append("AND HCARD_NO='"+Hashchn[i]+"' AND INST_ID='"+instid+"'");
						}else{
						update_production.append("AND CARD_NO='"+Hashchn[i]+"' AND INST_ID='"+instid+"'");    
						}*/
						/*
						trace("update_production :"+update_production.toString());
						update_product = jdbctemplate.update(update_production.toString());   
						*/
						
						
						trace("update_production :"+update_production.toString());
						update_product = jdbctemplate.update(update_production.toString(),new Object[]{cardstatus,switchstatus,"P",userid,instid,ecard[i],instid});   
						
						
						
						
						/*StringBuilder updEzcard = new StringBuilder();
						updEzcard.append("UPDATE EZCARDINFO set ");
						updEzcard.append("EXPIRYDATE = (SELECT EXPIRY_DATE from INST_CARD_PROCESS where ");
						updEzcard.append("HCARD_NO='"+Hashchn[i]+"' AND INSTID='"+instid+"')");
						updEzcard.append(",STATUS = '"+switchstatus+"',PANSEQNO = '00'  WHERE CHN = '"+Hashchn[i]+"' AND INSTID='"+instid+"'");*/
						
						
						StringBuilder updEzcard = new StringBuilder();
						updEzcard.append("UPDATE EZCARDINFO set ");
						updEzcard.append("EXPIRYDATE = (SELECT EXPIRY_DATE from INST_CARD_PROCESS where ");
						updEzcard.append("ORG_CHN='"+ecard[i]+"' AND INSTID='"+instid+"')");
						updEzcard.append(",STATUS = '"+switchstatus+"',PANSEQNO = '00'  WHERE CHN = (SELECT HCARD_NO FROM CARD_PRODUCTION_HASH WHERE ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION WHERE ORG_CHN='"+ecard[i]+"')) AND INSTID='"+instid+"'");
						
						
						
						trace("updEzcard :"+updEzcard.toString());
						updezcardinfo = jdbctemplate.update(updEzcard.toString()); 
						
						StringBuilder deletefromProcess = new StringBuilder();
						StringBuilder deletefromProcess1 = new StringBuilder();
						
						deletefromProcess.append("DELETE FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND ORG_CHN='"+ecard[i]+"' ");
						deletefromProcess1.append("DELETE FROM INST_CARD_PROCESS_HASH WHERE INST_ID='"+instid+"' ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM INST_CARD_PROCESS WHERE ORG_CHN='"+ecard[i]+"'");
						
						
						// BY SIVA
						/*if(padssenable.equals("Y")){		
							deletefromProcess.append("AND HCARD_NO='"+Hashchn[i]+"' ");
							}else{
							deletefromProcess.append("AND CARD_NO='"+Hashchn[i]+"' ");	
							}*/
						
						trace("update_product :"+update_product+" del_process :"+del_process);
						del_process1 = jdbctemplate.update(deletefromProcess1.toString()); 
						del_process = jdbctemplate.update(deletefromProcess.toString());
						
						
						//del_pin = jdbctemplate.update(delete_pin);
						
						
						    
						if( update_product >0 && del_process >0 && updezcardinfo >0 && del_process1 >0)	{ 
							String mifpcode = "DAMAGE";
							updatecount = updatecount + 1;
						}else{
							System.out.println("Process Breaked ====> 1");
							break;
						}						
						trace("..................DAMAGE CARD PART");		
					}
					
					if(cafflag.equals("A"))
					{					
						System.out.println("############################################################################"+cafflag);						
						try 
						{							
							personalcardissue = InstpersonalCardIssuence(ecard[i],padssenable,instid,userid,tablename,jdbctemplate);
							trace("prersnalcard issuance getting count " + personalcardissue);
							
							if(personalcardissue >0)
							{
								updatecount = updatecount + 1;
							}								
							trace("checking the update count value " +updatecount);
							
							/*int update = cbsTableUpdateDetails(Hashchn[i],padssenable,instid,userid,padsssec,eDMK,eDPK,cafflag,jdbctemplate);
							if(update == 1){
								enctrace(" ----NEW CARD - CBS UPDATE SUCCESS----");
							}else{
								enctrace(" ----NEW CARD - CBS UPDATE FAIL----");
							}*/
						
						}
						catch(Exception e)
						{
							 
							trace("Exception in personalcardissue::::"+e);
							e.printStackTrace();
							break;     
						}
						trace("Issuing the card....got : "+personalcardissue);			
					}
					
					else if(cafflag.equals("S"))
					{
						
						
						enctrace("#################################");
						//enctrace("####### REISSSUE CARD ###"+Hashchn[i]);
						
						String padsscond="";
						//if(padssenable.equals("Y")){padsscond="HCARD_NO='"+Hashchn[i]+"'";}else{padsscond="CARD_NO='"+Hashchn[i]+"'";}
						
						String custcin = commondesc.fchCustomerId(instid,padssenable, ecard[i], "INST_CARD_PROCESS", jdbctemplate);
						trace("Got the customer id : " + custcin);
						System.out.println("CUSTOMERN NUMBER IS =====> "+custcin);  
						int res_1 =0;int res_2 =0;int res_3=0;int res_4=0;int res_5 = 0;
						
						String deletefromProcess = "DELETE FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND ORG_CHN='"+ecard[i]+"' ";       
						String deletefromProcess1 = "DELETE FROM INST_CARD_PROCESS_HASH WHERE INST_ID='"+instid+"' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CRAD_PRODUCTION WHERE ORG_CHN='"+ecard[i]+"')";
						
						String reissuedate="''",reissue_count = "0",repindate="''",repincount="0",damgedate="''",blockdate="''",hotdate="''",closedte="''",pinretry_count="0";
						String active_date="''"; 
						
						String status_code= commondesc.getSwitchCardStatus(instid, cardstatus, jdbctemplate);
						
						String movetoproduction = "INSERT INTO CARD_PRODUCTION (INST_ID,ORDER_REF_NO,BIN,MCARD_NO,ACCOUNT_NO,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE," +
						"EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE,REG_DATE,RECV_DATE,ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT," +
						"ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,ADDON_FLAG,ADDON_CNT,REISSUE_DATE,REISSUE_CNT,REPIN_DATE,REPIN_CNT,DAMAGE_DATE,BLOCK_DATE,HOT_GENDATE,CLOSE_DATE,PIN_OFFSET,PIN_RETRY_CNT,AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO)" +
						"(SELECT INST_ID,ORDER_REF_NO,BIN,MCARD_NO,ACCT_NO ,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,'"+cardstatus+"',CAF_REC_STATUS,"+status_code+",CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE," +
						"PRE_FILE,REG_DATE,RECV_DATE,(SYSDATE),SYSDATE,'"+userid+"',(SYSDATE),CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT," +
						"ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,'','0',"+reissuedate+",'"+reissue_count+"',"+repindate+",'"+repincount+"',"+damgedate+","+blockdate+","+hotdate+","+closedte+",PIN_OFFSET,'"+pinretry_count+"',AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,'',CARD_REF_NO FROM INST_CARD_PROCESS " +
						"WHERE INST_ID='"+instid+"' AND ORG_CHN='"+ecard[i]+"')";   
						enctrace("movetoproduction1---- > "+movetoproduction);    
						
						
						String movetoproduction1 = "INSERT INTO CARD_PRODUCTION (INST_ID,ORDER_REF_NO,BIN,HCARD_NO, ACCOUNT_NO,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE," +
								"MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE)" +
								"(SELECT INST_ID,ORDER_REF_NO,BIN,HCARD_NO, ACCT_NO ,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE," +
								"'"+userid+"',(SYSDATE),CHECKER_ID,CHECKER_DATE FROM INST_CARD_PROCESS_HASH " +
								"WHERE INST_ID='"+instid+"' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CRAD_PRODUCTION WHERE ORG_CHN='"+ecard[i]+"'))";   
								enctrace("movetoproduction1 f---- > "+movetoproduction);
						       
						
						String INSTID_1="" ,CHN_1="" ,ACCOUNTNO_1="" ,ACCOUNTTYPE_1="" ,ACCOUNTFLAG_1="" ,ACCOUNTPRIORITY_1="" ,CURRCODE_1="" ;
						String INSTID_3="" ,CHN_3="" ,CARDTYPE_3="" ,CUSTID_3="" ,TXNGROUPID_3="" ,LIMITFLAG_3="" ,EXPIRYDATE_3="" ,STATUS="" ,PINOFFSET_3="" ,OLDPINOFFSET_3="" ,TPINOFFSET_3="" ,OLDTPINOFFSET_3="" ,PINRETRYCOUNT_3="" ,TPINRETRYCOUNT_3="" ,PVKI_3="" ,LASTTXNDATE_3="" ,LASTTXNTIME_3="" ,PANSEQNO_3=""; 
						
						
						
						StringBuilder mv = new StringBuilder();
						
						mv.append("SELECT ");
						//--EZAUTHREL start
						//--INSTID, CHN,ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE,
						mv.append("'"+instid+"' INSTID_1, '"+ecard[i]+"' CHN_1,AI.ACCOUNTNO ACCOUNTNO_1,AI.ACCTTYPE_ID ACCOUNTTYPE_1,PCP.PC_FLAG ACCOUNTFLAG_1,'1' ACCOUNTPRIORITY_1 ,AI.ACCT_CURRENCY CURRCODE_1, ");
						//--EZAUTHREL end  
						
						//-- EZCARDINFO start
						//--INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME, PANSEQNO
						
						mv.append("'"+instid+"' INSTID_3, PCP.HCARD_NO CHN_3, PCP.CARD_TYPE_ID CARDTYPE_3,'"+custcin+"' CUSTID_3,'01' TXNGROUPID_3, ");
						
						// by siva
						/*if(padssenable.equals("Y")){
						mv.append("'"+instid+"' INSTID_3, PCP.HCARD_NO CHN_3, PCP.CARD_TYPE_ID CARDTYPE_3,'"+custcin+"' CUSTID_3,'01' TXNGROUPID_3, "); 
						}
						else
						{
						mv.append("'"+instid+"' INSTID_3, PCP.CARD_NO CHN_3, PCP.CARD_TYPE_ID CARDTYPE_3,'"+custcin+"' CUSTID_3,'01' TXNGROUPID_3, ");	
						}*/
						mv.append("(SELECT CARD_FLAG FROM GLOBAL_CARDDETAILS WHERE LIMIT_TYPE = AI.ACCOUNTTYPE) LIMITFLAG_3,  ");
						mv.append("TO_CHAR(PCP.EXPIRY_DATE,'MM/DD/YYYY')  EXPIRYDATE_3,'"+status_code+"' STATUS , NVL(PCP.PIN_OFFSET,0)  PINOFFSET_3, NVL(PCP.OLD_PIN_OFFSET,0) OLDPINOFFSET_3, "); 
						mv.append("'0' TPINOFFSET_3,'0' OLDTPINOFFSET_3,NVL(PIN_RETRY_COUNT,0) PINRETRYCOUNT_3,'0' TPINRETRYCOUNT_3, '0' PVKI_3,  ");
						mv.append("TO_CHAR(SYSDATE,'MM/DD/YYYY') LASTTXNDATE_3,TO_CHAR(SYSDATE,'HH24MISS')  LASTTXNTIME_3, '00' PANSEQNO_3 ");  
						//-- EZCARDINFO end  
						
						mv.append(" FROM CUSTOMERINFO CI ,ACCOUNTINFO AI ,INST_CARD_PROCESS PCP "); 
						mv.append("WHERE ");
						//mv.append("WHERE (CI.ORDER_REF_NO = AI.ORDER_REF_NO AND AI.ORDER_REF_NO = PCP.ORDER_REF_NO AND PCP.ORDER_REF_NO = CI.ORDER_REF_NO) AND ");
						mv.append("(CI.CIN = AI.CIN AND AI.CIN = PCP.CIN AND PCP.CIN = CI.CIN and pcp.acct_no=ai.ACCOUNTNO) AND ");
						mv.append("(CI.INST_ID = AI.INST_ID AND AI.INST_ID = PCP.INST_ID AND PCP.INST_ID = CI.INST_ID) ");
						mv.append("AND CI.INST_ID='"+instid+"' AND AI.INST_ID='"+instid+"' AND PCP.INST_ID='"+instid+"'  ");
						mv.append("AND CI.CIN='"+custcin+"' AND AI.CIN='"+custcin+"' AND PCP.CIN='"+custcin+"' AND "+padsscond+" "); 
						  
						enctrace("Move to Production CafRec status SS-----------------------------------\n");
						enctrace(mv.toString());
						
						
						List movetoSwitchP = jdbctemplate.queryForList(mv.toString());
						Iterator custitr = movetoSwitchP.iterator();
						while(custitr.hasNext())
						{
							Map mp = (Map)custitr.next();
							//fname = (String)mp.get("FNAME");
									INSTID_1   = (String) mp.get(" INSTID_1 ");
									CHN_1  = (String) mp.get("CHN_1");
									ACCOUNTNO_1  = (String) mp.get("ACCOUNTNO_1");
									ACCOUNTTYPE_1  =(String)  mp.get("ACCOUNTTYPE_1");
									ACCOUNTFLAG_1  ="1";
									//ACCOUNTFLAG_1  =(String)  mp.get("ACCOUNTFLAG_1");
									ACCOUNTPRIORITY_1  =(String)  mp.get("ACCOUNTPRIORITY_1");
									CURRCODE_1  = (String) mp.get("CURRCODE_1");
									
									CHN_3  = mp.get("CHN_3").toString();
									CARDTYPE_3  = mp.get("CARDTYPE_3").toString();
									CUSTID_3  = mp.get("CUSTID_3").toString();
									TXNGROUPID_3  = mp.get("TXNGROUPID_3").toString();
									LIMITFLAG_3  = mp.get("LIMITFLAG_3").toString();
									EXPIRYDATE_3  = mp.get("EXPIRYDATE_3").toString();
									STATUS  = mp.get("STATUS").toString();
									PINOFFSET_3  = mp.get("PINOFFSET_3") .toString();
									OLDPINOFFSET_3  = mp.get("OLDPINOFFSET_3").toString();
									TPINOFFSET_3  = mp.get("TPINOFFSET_3").toString();
									OLDTPINOFFSET_3  = mp.get("OLDTPINOFFSET_3").toString(); 
									PINRETRYCOUNT_3  = mp.get("PINRETRYCOUNT_3").toString();
									TPINRETRYCOUNT_3  = mp.get("TPINRETRYCOUNT_3").toString();
									PVKI_3  = mp.get("PVKI_3").toString();
									LASTTXNDATE_3  = mp.get("LASTTXNDATE_3").toString();
									LASTTXNTIME_3  = mp.get("LASTTXNTIME_3").toString();
									PANSEQNO_3  = mp.get("PANSEQNO_3") .toString();
						}
						
						
						StringBuilder crdinf_3 = new StringBuilder();	
						
						crdinf_3.append("INSERT INTO EZCARDINFO ");
						crdinf_3.append("(INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME,CVV, PANSEQNO) ");
						crdinf_3.append("VALUES ");
						crdinf_3.append("('"+instid+"','"+CHN_3+"','"+CARDTYPE_3+"','"+CUSTID_3+"','"+TXNGROUPID_3+"','"+LIMITFLAG_3+"',TO_DATE('"+EXPIRYDATE_3+"','MM/DD/YYYY'),'"+STATUS+"','"+PINOFFSET_3+"','"+OLDPINOFFSET_3+"',");
						crdinf_3.append("'"+TPINOFFSET_3+"','"+OLDTPINOFFSET_3+"','"+PINRETRYCOUNT_3+"','"+TPINRETRYCOUNT_3+"','"+PVKI_3+"',TO_DATE('"+LASTTXNDATE_3+"','MM/DD/YYYY'),'"+LASTTXNTIME_3+"'  ,'0' ,");
						crdinf_3.append("'"+PANSEQNO_3+"' )");
						
						enctrace("crdinf_3:::::"+crdinf_3.toString());
						
						
						   
						StringBuilder authrel_1 = new StringBuilder();	
						authrel_1.append("INSERT INTO EZAUTHREL ");
						authrel_1.append("(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) ");
						authrel_1.append("VALUES ");
						authrel_1.append("('"+instid+"','"+CHN_1+"','"+ACCOUNTNO_1+"','"+ACCOUNTTYPE_1+"','"+ACCOUNTFLAG_1+"','"+ACCOUNTPRIORITY_1+"','"+CURRCODE_1+"') ");
				        enctrace("authrel_1::::"+authrel_1.toString());
						
				        
				        res_3 = jdbctemplate.update(crdinf_3.toString());
				        
						res_1 = jdbctemplate.update(authrel_1.toString());

						
						
						/*crdinf_3.append("INSERT INTO EZCARDINFO ");
						crdinf_3.append("(INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME,CVV, PANSEQNO) ");
						crdinf_3.append("VALUES ");
						crdinf_3.append("(?,?,?,?,?,?,TO_DATE(?,'MM/DD/YYYY'),?,?,?,");
						crdinf_3.append("?,?,?,?,?,TO_DATE(?,'MM/DD/YYYY'),? ,? ,");
						crdinf_3.append("? )");
						
						enctrace("crdinf_3:::::"+crdinf_3.toString());
						StringBuilder authrel_1 = new StringBuilder();	
						
						authrel_1.append("INSERT INTO EZAUTHREL ");
						authrel_1.append("(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) ");
						authrel_1.append("VALUES ");
						authrel_1.append("(?,?,?,?,?,?,?) ");
				        enctrace("authrel_1::::"+authrel_1.toString());
						
				         res_3 = jdbctemplate.update(crdinf_3.toString(),new Object[]{instid,CHN_3,CARDTYPE_3,CUSTID_3,TXNGROUPID_3,LIMITFLAG_3,EXPIRYDATE_3,STATUS,PINOFFSET_3,OLDPINOFFSET_3,TPINOFFSET_3,OLDTPINOFFSET_3,PINRETRYCOUNT_3,
				        		 TPINRETRYCOUNT_3,PVKI_3,LASTTXNDATE_3,LASTTXNTIME_3,"0",PANSEQNO_3});
				        
				         res_1 = jdbctemplate.update(authrel_1.toString(),new Object[]{instid,CHN_1,ACCOUNTNO_1,ACCOUNTTYPE_1,ACCOUNTFLAG_1,ACCOUNTPRIORITY_1,CURRCODE_1});
				        
				        */
						
				        
						
						
						
						int production_insert = jdbctemplate.update(movetoproduction);
						int deletefromprocess1 = jdbctemplate.update(deletefromProcess1);
						int deletefromprocess = jdbctemplate.update(deletefromProcess);
						  
						int production_insert1 = jdbctemplate.update(movetoproduction1);
						
						
						
						enctrace("\n--------------------------------------Move to Production Query CAF REC S");
						trace("result :::::::::::"+res_1+res_3+production_insert+deletefromprocess);   
						if(res_1 >0 && res_3>0 && production_insert>0 && deletefromprocess>0 && production_insert1>0 && deletefromprocess1>0)   
						{
							trace("PROCESS COMPLETED");
							updatecount = updatecount + 1;
						}
						else
						{
							System.out.println("Error While Insert and Delete ");
							break;
						}
					}
						/*int update = cbsTableUpdateDetails(Hashchn[i],padssenable,instid,userid,padsssec,eDMK,eDPK,cafflag,jdbctemplate);  
						
						if(update == 1){
							enctrace(" ----REISSSUE CARD - CBS UPDATE SUCCESS----");
						}else{
							enctrace(" ----REISSSUE CARD - CBS UPDATE FAIL----");
						}*/
				
		
					
						else if(cafflag.equals("AC")){
						
						
						enctrace("#################################");
						enctrace("####### REISSSUE CARD ###"+ecard[i]);
						    
						String custcin = commondesc.fchCustomerId(instid,padssenable, ecard[i], "INST_CARD_PROCESS", jdbctemplate);
						trace("Got the customer id : " + custcin);
						System.out.println("CUSTOMERN NUMBER IS =====> "+custcin);  
						
						int res_1 =0;int res_2 =0;int res_3=0;int res_4=0;int res_5 = 0;
						
						String deletefromProcess = "";
						
						/*if(padssenable.equals("Y")){
						deletefromProcess = "DELETE FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND HCARD_NO='"+Hashchn[i]+"' ";
						}else
						{
						deletefromProcess = "DELETE FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND CARD_NO='"+Hashchn[i]+"' ";	
						}*/
						
						
						deletefromProcess = "DELETE FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND ORG_CHN='"+ecard[i]+"' ";
						String deletefromProcess1 = "DELETE FROM INST_CARD_PROCESS_HASH WHERE INST_ID='"+instid+"' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CRAD_PRODUCTION WHERE ORG_CHN='"+ecard[i]+"') ";
						enctrace("deletefromProcess::"+deletefromProcess);
						
						String reissuedate="''",reissue_count = "0",repindate="''",repincount="0",damgedate="''",blockdate="''",hotdate="''",closedte="''",pinretry_count="0";
						String active_date="''"; 
						String status_code= commondesc.getSwitchCardStatus(instid, cardstatus, jdbctemplate);
						StringBuilder movetoproduction = new StringBuilder();
						movetoproduction.append("INSERT INTO CARD_PRODUCTION (INST_ID,ORDER_REF_NO,BIN,MCARD_NO,ACCOUNT_NO,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,");
						movetoproduction.append("EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE,REG_DATE,RECV_DATE,ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,");
						movetoproduction.append("ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,ADDON_FLAG,ADDON_CNT,REISSUE_DATE,REISSUE_CNT,REPIN_DATE,REPIN_CNT,DAMAGE_DATE,BLOCK_DATE,HOT_GENDATE,CLOSE_DATE,PIN_OFFSET,PIN_RETRY_CNT,AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO)");
						movetoproduction.append("(SELECT INST_ID,ORDER_REF_NO,BIN,MCARD_NO,ACCT_NO ,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,'"+cardstatus+"',CAF_REC_STATUS,"+status_code+",CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE," );
						movetoproduction.append("PRE_FILE,REG_DATE,RECV_DATE,(SYSDATE),SYSDATE,'"+userid+"',(SYSDATE),CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT," );
						movetoproduction.append("ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,'','0',"+reissuedate+",'"+reissue_count+"',"+repindate+",'"+repincount+"',"+damgedate+","+blockdate+","+hotdate+","+closedte+",PIN_OFFSET,'"+pinretry_count+"',AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,'',CARD_REF_NO FROM INST_CARD_PROCESS " );
						movetoproduction.append("WHERE INST_ID='"+instid+"' AND ORG_CHN='"+ecard[i]+"')");
						enctrace("movetoproduction for AC---- > "+movetoproduction);
						
						
						StringBuilder movetoproduction1 = new StringBuilder();
						movetoproduction1.append("INSERT INTO CARD_PRODUCTION (INST_ID,ORDER_REF_NO,BIN,HCARD_NO, ACCOUNT_NO,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE,");
						movetoproduction1.append("MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE)");
						movetoproduction1.append("(SELECT INST_ID,ORDER_REF_NO,BIN,HCARD_NO, ACCT_NO ,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE," );
						movetoproduction1.append("'"+userid+"',(SYSDATE),CHECKER_ID,CHECKER_DATE FROM INST_CARD_PROCESS_HASH " );
						movetoproduction1.append("WHERE INST_ID='"+instid+"' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CRAD_PRODUCTION WHERE ORG_CHN='"+ecard[i]+"')");
						enctrace("movetoproduction for AC---- > "+movetoproduction1);
						
						// by siva 
						/*if(padssenable.equals("Y")){
						movetoproduction.append("WHERE INST_ID='"+instid+"' AND HCARD_NO='"+Hashchn[i]+"')");
						}else{
						movetoproduction.append("WHERE INST_ID='"+instid+"' AND CARD_NO='"+Hashchn[i]+"')");	
						}*/
						
						
						
						    
						String INSTID_1="" ,CHN_1="" ,ACCOUNTNO_1="" ,ACCOUNTTYPE_1="" ,ACCOUNTFLAG_1="" ,ACCOUNTPRIORITY_1="" ,CURRCODE_1="" ;
						String INSTID_3="" ,CHN_3="" ,CARDTYPE_3="" ,CUSTID_3="" ,TXNGROUPID_3="" ,LIMITFLAG_3="" ,EXPIRYDATE_3="" ,STATUS="" ,PINOFFSET_3="" ,OLDPINOFFSET_3="" ,TPINOFFSET_3="" ,OLDTPINOFFSET_3="" ,PINRETRYCOUNT_3="" ,TPINRETRYCOUNT_3="" ,PVKI_3="" ,LASTTXNDATE_3="" ,LASTTXNTIME_3="" ,PANSEQNO_3=""; 
						
						
						
						StringBuilder mv = new StringBuilder();
						
						mv.append("SELECT ");
						//--EZAUTHREL start
						//--INSTID, CHN,ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE,
						mv.append("'"+instid+"' INSTID_1, '"+ecard[i]+"' CHN_1,AI.ACCOUNTNO ACCOUNTNO_1,AI.ACCTTYPE_ID ACCOUNTTYPE_1,PCP.PC_FLAG ACCOUNTFLAG_1,'1' ACCOUNTPRIORITY_1 ,AI.ACCT_CURRENCY CURRCODE_1, ");
						//--EZAUTHREL end  
						
						//-- EZCARDINFO start
						//--INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME, PANSEQNO
						mv.append("'"+instid+"' INSTID_3, '"+ecard[i]+"' CHN_3, PCP.CARD_TYPE_ID CARDTYPE_3,'"+custcin+"' CUSTID_3,'01' TXNGROUPID_3, "); 
						mv.append("(SELECT CARD_FLAG FROM GLOBAL_CARDDETAILS WHERE LIMIT_TYPE = AI.ACCOUNTTYPE) LIMITFLAG_3,  ");
						mv.append("TO_CHAR(PCP.EXPIRY_DATE,'MM/DD/YYYY')  EXPIRYDATE_3,'"+status_code+"' STATUS , NVL(PCP.PIN_OFFSET,0) PINOFFSET_3, NVL(PCP.OLD_PIN_OFFSET,0) OLDPINOFFSET_3, "); 
						mv.append("'0' TPINOFFSET_3,'0' OLDTPINOFFSET_3,NVL(PIN_RETRY_COUNT,0) PINRETRYCOUNT_3,'0' TPINRETRYCOUNT_3, '0' PVKI_3,  ");
						mv.append("TO_CHAR(SYSDATE,'MM/DD/YYYY') LASTTXNDATE_3,TO_CHAR(SYSDATE,'HH24MISS')  LASTTXNTIME_3, '00' PANSEQNO_3 ");  
						//-- EZCARDINFO end  
						
						mv.append(" FROM CUSTOMERINFO CI ,ACCOUNTINFO AI ,INST_CARD_PROCESS PCP "); 
						//mv.append("WHERE (CI.ORDER_REF_NO = AI.ORDER_REF_NO AND AI.ORDER_REF_NO = PCP.ORDER_REF_NO AND PCP.ORDER_REF_NO = CI.ORDER_REF_NO) AND ");
						mv.append(" WHERE (CI.CIN = AI.CIN AND AI.CIN = PCP.CIN AND PCP.CIN = CI.CIN) AND ");
						mv.append("(CI.INST_ID = AI.INST_ID AND AI.INST_ID = PCP.INST_ID AND PCP.INST_ID = CI.INST_ID and pcp.acct_no=ai.ACCOUNTNO) ");
						mv.append("AND CI.INST_ID='"+instid+"' AND AI.INST_ID='"+instid+"' AND PCP.INST_ID='"+instid+"'  ");
						mv.append("AND CI.CIN='"+custcin+"' AND AI.CIN='"+custcin+"' AND PCP.CIN='"+custcin+"' "); 
						mv.append("AND PCP.INST_ID='"+instid+"' AND PCP.ORG_CHN='"+ecard[i]+"'");
						
						// by siva 
						/*if(padssenable.equals("Y")){
							mv.append("AND PCP.INST_ID='"+instid+"' AND PCP.HCARD_NO='"+Hashchn[i]+"'");
							}else{
							mv.append("AND PCP.INST_ID='"+instid+"' AND PCP.CARD_NO='"+Hashchn[i]+"'");	
							}  */
						
						enctrace("Move to Production CafRec status SS-----------------------------------\n");
						enctrace(mv.toString());
						enctrace("Move to Production CafRec status SS New-----------------------------------\n"+mv.toString());
						
						List movetoSwitchP = jdbctemplate.queryForList(mv.toString());
						Iterator custitr = movetoSwitchP.iterator();
						while(custitr.hasNext())
						{
							Map mp = (Map)custitr.next();
							//fname = (String)mp.get("FNAME");
									INSTID_1   = (String) mp.get(" INSTID_1 ");
									CHN_1  = (String) mp.get("CHN_1");
									ACCOUNTNO_1  = (String) mp.get("ACCOUNTNO_1");
									ACCOUNTTYPE_1  =(String)  mp.get("ACCOUNTTYPE_1");
									//ACCOUNTFLAG_1  =(String)  mp.get("ACCOUNTFLAG_1");
									ACCOUNTFLAG_1="1";
									ACCOUNTPRIORITY_1  =(String)  mp.get("ACCOUNTPRIORITY_1");
									CURRCODE_1  = (String) mp.get("CURRCODE_1");
									
									CHN_3  = mp.get("CHN_3").toString();
									CARDTYPE_3  = mp.get("CARDTYPE_3").toString();
									CUSTID_3  = mp.get("CUSTID_3").toString();
									TXNGROUPID_3  = mp.get("TXNGROUPID_3").toString();
									LIMITFLAG_3  = mp.get("LIMITFLAG_3").toString();
									EXPIRYDATE_3  = mp.get("EXPIRYDATE_3").toString();
									STATUS  = mp.get("STATUS").toString();
									PINOFFSET_3  = mp.get("PINOFFSET_3") .toString();
									OLDPINOFFSET_3  = mp.get("OLDPINOFFSET_3").toString();
									TPINOFFSET_3  = mp.get("TPINOFFSET_3").toString();
									OLDTPINOFFSET_3  = mp.get("OLDTPINOFFSET_3").toString(); 
									PINRETRYCOUNT_3  = mp.get("PINRETRYCOUNT_3").toString();
									TPINRETRYCOUNT_3  = mp.get("TPINRETRYCOUNT_3").toString();
									PVKI_3  = mp.get("PVKI_3").toString();
									LASTTXNDATE_3  = mp.get("LASTTXNDATE_3").toString();
									LASTTXNTIME_3  = mp.get("LASTTXNTIME_3").toString();
									PANSEQNO_3  = mp.get("PANSEQNO_3") .toString();
						}
						
						StringBuilder crdinf_3 = new StringBuilder();		    
						crdinf_3.append("INSERT INTO EZCARDINFO ");
						crdinf_3.append("(INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME,CVV, PANSEQNO) ");
						crdinf_3.append("VALUES ");
						crdinf_3.append("('"+instid+"','"+CHN_3+"','"+CARDTYPE_3+"','"+CUSTID_3+"','"+TXNGROUPID_3+"','"+LIMITFLAG_3+"',TO_DATE('"+EXPIRYDATE_3+"','MM/DD/YYYY'),'"+STATUS+"','"+PINOFFSET_3+"','"+OLDPINOFFSET_3+"',");
						crdinf_3.append("'"+TPINOFFSET_3+"','"+OLDTPINOFFSET_3+"','"+PINRETRYCOUNT_3+"','"+TPINRETRYCOUNT_3+"','"+PVKI_3+"',TO_DATE('"+LASTTXNDATE_3+"','MM/DD/YYYY'),'"+LASTTXNTIME_3+"'  ,'0' ,");
						crdinf_3.append("'"+PANSEQNO_3+"' )");
						
						enctrace("crdinf_3:::::"+crdinf_3.toString());
						     
						res_3 = jdbctemplate.update(crdinf_3.toString());
						  
						StringBuilder authrel_1 = new StringBuilder();	
						authrel_1.append("INSERT INTO EZAUTHREL ");
						authrel_1.append("(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) ");
						authrel_1.append("VALUES ");
						authrel_1.append("('"+instid+"','"+CHN_1+"','"+ACCOUNTNO_1+"','"+ACCOUNTTYPE_1+"','"+ACCOUNTFLAG_1+"','"+ACCOUNTPRIORITY_1+"','"+CURRCODE_1+"') ");
				        enctrace("authrel_1::::"+authrel_1.toString());
						
				        
						res_1 = jdbctemplate.update(authrel_1.toString());
				        
				        
						
						
						
						int production_insert = jdbctemplate.update(movetoproduction.toString());
						int deletefromprocess1 = jdbctemplate.update(deletefromProcess1);
						int deletefromprocess = jdbctemplate.update(deletefromProcess);
						int production_insert1 = jdbctemplate.update(movetoproduction1.toString());
						
						           
						
						
						enctrace("\n--------------------------------------Move to Production Query CAF REC S");
						trace("result :::::::::::"+res_1+res_3+production_insert+deletefromprocess);   
						if(res_1>0 && res_3>0 && production_insert>0 && deletefromprocess>0 && production_insert1>0 && deletefromprocess1>0)   
						{
							trace("PROCESS COMPLETED");
							updatecount = updatecount + 1;
						}
						else
						{   
							System.out.println("Error While Insert and Delete ");
							addActionError("Unable to continue process ....");
							return authInstCustomerHome();
						}
						
						/*int update = cbsTableUpdateDetails(Hashchn[i],padssenable,instid,userid,padsssec,eDMK,eDPK,cafflag,jdbctemplate);  
						
						if(update == 1){
							enctrace(" ----ADDON CARD - CBS UPDATE SUCCESS----");
						}else{
							enctrace(" ----ADDON CARD - CBS UPDATE FAIL----");
						}*/
						
					}
					
						else if(cafflag.equals("BN")){
							      
							
							enctrace("#################################");
							//enctrace("####### BULK RENUED WITH NEW card ###"+Hashchn[i]);
							//trace("####### BULK RENUED WITH NEW card ###"+Hashchn[i]);
							    
							String custcin = commondesc.fchCustomerId(instid,padssenable, ecard[i], "INST_CARD_PROCESS", jdbctemplate);
							trace("Got the customer id : " + custcin);
							System.out.println("CUSTOMERN NUMBER IS =====> "+custcin);  
							    
							int res_1 =0;int res_2 =0;int res_3=0;int res_4=0;int res_5 = 0;
							
							String deletefromProcess = "";
							
							/*if(padssenable.equals("Y")){
							deletefromProcess = "DELETE FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND HCARD_NO='"+Hashchn[i]+"' ";
							}else
							{
							deletefromProcess = "DELETE FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND CARD_NO='"+Hashchn[i]+"' ";	
							}*/
							deletefromProcess = "DELETE FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND ORG_CHN='"+ecard[i]+"' ";
							String deletefromProcess1 = "DELETE FROM INST_CARD_PROCESS_HASH WHERE INST_ID='"+instid+"' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CRAD_PRODUCTION WHERE ORG_CHN='"+ecard[i]+"') ";
							enctrace("deletefromProcess::"+deletefromProcess);
							enctrace("deletefromProcess::"+deletefromProcess1);
							
							String reissuedate="''",reissue_count = "0",repindate="''",repincount="0",damgedate="''",blockdate="''",hotdate="''",closedte="''",pinretry_count="0";
							String active_date="''"; 
							String status_code= commondesc.getSwitchCardStatus(instid, cardstatus, jdbctemplate);
							
							StringBuilder movetoproduction = new StringBuilder();
							movetoproduction.append("INSERT INTO CARD_PRODUCTION (INST_ID,ORDER_REF_NO,BIN, MCARD_NO,ACCOUNT_NO,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,");
							movetoproduction.append("EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE,REG_DATE,RECV_DATE,ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT,");
							movetoproduction.append("ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,ADDON_FLAG,ADDON_CNT,REISSUE_DATE,REISSUE_CNT,REPIN_DATE,REPIN_CNT,DAMAGE_DATE,BLOCK_DATE,HOT_GENDATE,CLOSE_DATE,PIN_OFFSET,PIN_RETRY_CNT,AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO)");
							movetoproduction.append("(SELECT INST_ID,ORDER_REF_NO,BIN,MCARD_NO,ACCT_NO ,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,'"+cardstatus+"',CAF_REC_STATUS,"+status_code+",CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE," );
							movetoproduction.append("PRE_FILE,REG_DATE,RECV_DATE,(SYSDATE),SYSDATE,'"+userid+"',(SYSDATE),CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT," );
							movetoproduction.append("ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,'','0',"+reissuedate+",'"+reissue_count+"',"+repindate+",'"+repincount+"',"+damgedate+","+blockdate+","+hotdate+","+closedte+",PIN_OFFSET,'"+pinretry_count+"',AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,'',CARD_REF_NO FROM INST_CARD_PROCESS " );
							movetoproduction.append("WHERE INST_ID='"+instid+"' AND ORG_CHN='"+ecard[i]+"')");
							enctrace("movetoproduction for renewal---- > "+movetoproduction);
							
							
							StringBuilder movetoproduction1 = new StringBuilder();
							movetoproduction1.append("INSERT INTO CARD_PRODUCTION_HASH (INST_ID,ORDER_REF_NO,BIN,HCARD_NO, ACCOUNT_NO,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE,");
							movetoproduction1.append("MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE)");
							movetoproduction1.append("(SELECT INST_ID,ORDER_REF_NO,BIN,HCARD_NO, ACCT_NO ,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE," );
							movetoproduction1.append("'"+userid+"',(SYSDATE),CHECKER_ID,CHECKER_DATE FROM INST_CARD_PROCESS " );
							movetoproduction1.append("WHERE INST_ID='"+instid+"' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION WHERE ORG_CHN='"+ecard[i]+"'))");
							enctrace("movetoproduction for renewal ---- > "+movetoproduction1);
							
							/*if(padssenable.equals("Y")){
							movetoproduction.append("WHERE INST_ID='"+instid+"' AND HCARD_NO='"+Hashchn[i]+"')");
							}else{
							movetoproduction.append("WHERE INST_ID='"+instid+"' AND CARD_NO='"+Hashchn[i]+"')");	
							}*/
							//enctrace("movetoproduction---- > "+movetoproduction);
							
							
							    
							String INSTID_1="" ,CHN_1="" ,ACCOUNTNO_1="" ,ACCOUNTTYPE_1="" ,ACCOUNTFLAG_1="" ,ACCOUNTPRIORITY_1="" ,CURRCODE_1="" ;
							String INSTID_3="" ,CHN_3="" ,CARDTYPE_3="" ,CUSTID_3="" ,TXNGROUPID_3="" ,LIMITFLAG_3="" ,EXPIRYDATE_3="" ,STATUS="" ,PINOFFSET_3="" ,OLDPINOFFSET_3="" ,TPINOFFSET_3="" ,OLDTPINOFFSET_3="" ,PINRETRYCOUNT_3="" ,TPINRETRYCOUNT_3="" ,PVKI_3="" ,LASTTXNDATE_3="" ,LASTTXNTIME_3="" ,PANSEQNO_3=""; 
							
							
							
							StringBuilder mv = new StringBuilder();
							
							mv.append("SELECT ");
							//--EZAUTHREL start
							//--INSTID, CHN,ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE,
							mv.append("'"+instid+"' INSTID_1, '"+ecard[i]+"' CHN_1,PCP.ACCT_NO ACCOUNTNO_1,AI.ACCTTYPE_ID ACCOUNTTYPE_1,PCP.PC_FLAG ACCOUNTFLAG_1,'1' ACCOUNTPRIORITY_1 ,AI.ACCT_CURRENCY CURRCODE_1, ");
							//--EZAUTHREL end  
							
							//-- EZCARDINFO start
							//--INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME, PANSEQNO
							mv.append("'"+instid+"' INSTID_3, '"+ecard[i]+"' CHN_3, PCP.CARD_TYPE_ID CARDTYPE_3,'"+custcin+"' CUSTID_3,'01' TXNGROUPID_3, "); 
							mv.append("(SELECT CARD_FLAG FROM GLOBAL_CARDDETAILS WHERE LIMIT_TYPE = AI.ACCOUNTTYPE) LIMITFLAG_3,  ");
							mv.append("TO_CHAR(PCP.EXPIRY_DATE,'MM/DD/YYYY')  EXPIRYDATE_3,'"+status_code+"' STATUS , NVL(PCP.PIN_OFFSET,0) PINOFFSET_3, NVL(PCP.OLD_PIN_OFFSET,0) OLDPINOFFSET_3, "); 
							mv.append("'0' TPINOFFSET_3,'0' OLDTPINOFFSET_3,NVL(PIN_RETRY_COUNT,0) PINRETRYCOUNT_3,'0' TPINRETRYCOUNT_3, '0' PVKI_3,  ");
							mv.append("TO_CHAR(SYSDATE,'MM/DD/YYYY') LASTTXNDATE_3,TO_CHAR(SYSDATE,'HH24MISS')  LASTTXNTIME_3, '00' PANSEQNO_3 ");  
							//-- EZCARDINFO end  
							
							mv.append(" FROM CUSTOMERINFO CI ,ACCOUNTINFO AI ,INST_CARD_PROCESS PCP ");
							mv.append("WHERE");
							//mv.append("WHERE (CI.ORDER_REF_NO = AI.ORDER_REF_NO AND AI.ORDER_REF_NO = PCP.ORDER_REF_NO AND PCP.ORDER_REF_NO = CI.ORDER_REF_NO) AND ");
							mv.append("(CI.CIN = AI.CIN AND AI.CIN = PCP.CIN AND PCP.CIN = CI.CIN) AND ");
							mv.append("(CI.INST_ID = AI.INST_ID AND AI.INST_ID = PCP.INST_ID AND PCP.INST_ID = CI.INST_ID and pcp.acct_no=ai.ACCOUNTNO) ");
							mv.append("AND CI.INST_ID='"+instid+"' AND AI.INST_ID='"+instid+"' AND PCP.INST_ID='"+instid+"'  ");
							mv.append("AND CI.CIN='"+custcin+"' AND AI.CIN='"+custcin+"' AND PCP.CIN='"+custcin+"' "); 
							mv.append("AND PCP.INST_ID='"+instid+"' AND PCP.ORG_CHN='"+ecard[i]+"'");
							
							
							/*if(padssenable.equals("Y")){
								mv.append("AND PCP.INST_ID='"+instid+"' AND PCP.HCARD_NO='"+Hashchn[i]+"'");
								}else{
								mv.append("AND PCP.INST_ID='"+instid+"' AND PCP.CARD_NO='"+Hashchn[i]+"'");	
								}  */
							
							enctrace("Move to Production CafRec status BN-----------------------------------\n");
							enctrace(mv.toString());
							enctrace("Move to Production CafRec status BN New-----------------------------------\n"+mv.toString());
							
							List movetoSwitchP = jdbctemplate.queryForList(mv.toString());    
							Iterator custitr = movetoSwitchP.iterator();
							while(custitr.hasNext())
							{
								Map mp = (Map)custitr.next();
								//fname = (String)mp.get("FNAME");
										INSTID_1   = (String) mp.get(" INSTID_1 ");
										CHN_1  = (String) mp.get("CHN_1");
										ACCOUNTNO_1  = (String) mp.get("ACCOUNTNO_2");
										ACCOUNTTYPE_1  =(String)  mp.get("ACCOUNTTYPE_1");
										//ACCOUNTFLAG_1  =(String)  mp.get("ACCT_FLAG");
										ACCOUNTFLAG_1="1";
										ACCOUNTPRIORITY_1  =(String)  mp.get("ACCOUNTPRIORITY_1");
										CURRCODE_1  = (String) mp.get("CURRCODE_1");
										
										CHN_3  = mp.get("CHN_3").toString();
										CARDTYPE_3  = mp.get("CARDTYPE_3").toString();
										CUSTID_3  = mp.get("CUSTID_3").toString();
										TXNGROUPID_3  = mp.get("TXNGROUPID_3").toString();
										LIMITFLAG_3  = mp.get("LIMITFLAG_3").toString();
										EXPIRYDATE_3  = mp.get("EXPIRYDATE_3").toString();
										STATUS  = mp.get("STATUS").toString();
										PINOFFSET_3  = mp.get("PINOFFSET_3") .toString();
										OLDPINOFFSET_3  = mp.get("OLDPINOFFSET_3").toString();
										TPINOFFSET_3  = mp.get("TPINOFFSET_3").toString();
										OLDTPINOFFSET_3  = mp.get("OLDTPINOFFSET_3").toString(); 
										PINRETRYCOUNT_3  = mp.get("PINRETRYCOUNT_3").toString();
										TPINRETRYCOUNT_3  = mp.get("TPINRETRYCOUNT_3").toString();
										PVKI_3  = mp.get("PVKI_3").toString();
										LASTTXNDATE_3  = mp.get("LASTTXNDATE_3").toString();
										LASTTXNTIME_3  = mp.get("LASTTXNTIME_3").toString();
										PANSEQNO_3  = mp.get("PANSEQNO_3") .toString();
							}
							
							StringBuilder crdinf_3 = new StringBuilder();		    
							/*crdinf_3.append("INSERT INTO EZCARDINFO ");
							crdinf_3.append("(INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME,CVV, PANSEQNO) ");
							crdinf_3.append("VALUES ");
							crdinf_3.append("('"+instid+"','"+CHN_3+"','"+CARDTYPE_3+"','"+CUSTID_3+"','"+TXNGROUPID_3+"','"+LIMITFLAG_3+"',TO_DATE('"+EXPIRYDATE_3+"','MM/DD/YYYY'),'"+STATUS+"','"+PINOFFSET_3+"','"+OLDPINOFFSET_3+"',");
							crdinf_3.append("'"+TPINOFFSET_3+"','"+OLDTPINOFFSET_3+"','"+PINRETRYCOUNT_3+"','"+TPINRETRYCOUNT_3+"','"+PVKI_3+"',TO_DATE('"+LASTTXNDATE_3+"','MM/DD/YYYY'),'"+LASTTXNTIME_3+"'  ,'0' ,");
							crdinf_3.append("'"+PANSEQNO_3+"' )");
							
							enctrace("crdinf_3:::::"+crdinf_3.toString());
							     
							res_3 = jdbctemplate.update(crdinf_3.toString());
							  
							StringBuilder authrel_1 = new StringBuilder();	
							authrel_1.append("INSERT INTO EZAUTHREL ");
							authrel_1.append("(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) ");
							authrel_1.append("VALUES ");
							authrel_1.append("('"+instid+"','"+CHN_1+"','"+ACCOUNTNO_1+"','"+ACCOUNTTYPE_1+"','"+ACCOUNTFLAG_1+"','"+ACCOUNTPRIORITY_1+"','"+CURRCODE_1+"') ");
					        enctrace("authrel_1::::"+authrel_1.toString());
							
					        
							res_1 = jdbctemplate.update(authrel_1.toString());
					        */
							
							
							///by gowtham-280819
							crdinf_3.append("INSERT INTO EZCARDINFO ");
							crdinf_3.append("(INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME,CVV, PANSEQNO) ");
							crdinf_3.append("VALUES ");
							crdinf_3.append("(?,?,?,?,?,?,TO_DATE(?,'MM/DD/YYYY'),?,?,?,");
							crdinf_3.append("?,?,?,?,?,TO_DATE(?,'MM/DD/YYYY'),?  ,? ,");
							crdinf_3.append("?)");
							enctrace("crdinf_3:::::"+crdinf_3.toString());
							res_3 = jdbctemplate.update(crdinf_3.toString(),new Object[]{instid,CHN_3,CARDTYPE_3,CUSTID_3,TXNGROUPID_3,LIMITFLAG_3,EXPIRYDATE_3,STATUS,PINOFFSET_3,OLDPINOFFSET_3,TPINOFFSET_3,OLDTPINOFFSET_3,PINRETRYCOUNT_3,TPINRETRYCOUNT_3,PVKI_3,LASTTXNDATE_3,LASTTXNTIME_3,"0" ,PANSEQNO_3});
							  
							StringBuilder authrel_1 = new StringBuilder();	
							authrel_1.append("INSERT INTO EZAUTHREL ");
							authrel_1.append("(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) ");
							authrel_1.append("VALUES ");
							authrel_1.append("(?,?,?,?,?,?,?) ");
					        enctrace("authrel_1::::"+authrel_1.toString());
							res_1 = jdbctemplate.update(authrel_1.toString(),new Object[]{instid,CHN_1,ACCOUNTNO_1,ACCOUNTTYPE_1,ACCOUNTFLAG_1,ACCOUNTPRIORITY_1,CURRCODE_1});
					        
					        
					        
							
							
							
							int production_insert = jdbctemplate.update(movetoproduction.toString());
							int deletefromprocess1 = jdbctemplate.update(deletefromProcess1);
							int deletefromprocess = jdbctemplate.update(deletefromProcess);
							int production_insert1 = jdbctemplate.update(movetoproduction1.toString());
							
							           
							
							
							enctrace("\n--------------------------------------Move to Production Query CAF REC S");
							trace("result :::::::::::"+res_1+res_3+production_insert+deletefromprocess);   
							if(res_1>0 && res_3>0 && production_insert>0 && deletefromprocess>0 && production_insert1>0 && deletefromprocess1>0)   
							{
								trace("PROCESS COMPLETED");
								updatecount = updatecount + 1;
							}
							else
							{   
								System.out.println("Error While Insert and Delete ");
								addActionError("Unable to continue process ....");
								return authInstCustomerHome();
							}
							
							/*int update = cbsTableUpdateDetails(Hashchn[i],padssenable,instid,userid,padsssec,eDMK,eDPK,cafflag,jdbctemplate);  
							
							if(update == 1){
								enctrace(" ----BULK RENEWAL CARD - CBS UPDATE SUCCESS----");
							}else{
								enctrace(" ----BULK RENEWAL CARD - CBS UPDATE FAIL----");
							}*/
							
						}
					
					
						if( cafflag.equals("BR") ) {
							
							
							trace("BULK RENUAL CARD ATE WITH EXPIRY DATE...................");
							
							
							StringBuilder update_production = new StringBuilder();
							/*update_production.append("UPDATE CARD_PRODUCTION SET CARD_STATUS='"+cardstatus+"', STATUS_CODE='"+switchstatus+"',MKCK_STATUS = 'P' ,  ISSUE_DATE=(SYSDATE),ACTIVE_DATE=(sysdate),MAKER_ID='"+userid+"', " );
							update_production.append("MAKER_DATE=(sysdate) WHERE INST_ID='"+instid+"' ");
							update_production.append("AND ORG_CHN='"+ecard[i]+"' AND INST_ID='"+instid+"'");*/
							
						////by gowtham-289819
							update_production.append("UPDATE CARD_PRODUCTION SET CARD_STATUS=?, STATUS_CODE=?,MKCK_STATUS =? ,  ISSUE_DATE=(SYSDATE),ACTIVE_DATE=(sysdate),MAKER_ID=?, " );
							update_production.append("MAKER_DATE=(sysdate) WHERE INST_ID=? ");
							update_production.append("AND ORG_CHN=? AND INST_ID=?");
							
							
							// by siva 
							/*if(padssenable.equals("Y")){		
							update_production.append("AND HCARD_NO='"+Hashchn[i]+"' AND INST_ID='"+instid+"'");
							}else{
							update_production.append("AND CARD_NO='"+Hashchn[i]+"' AND INST_ID='"+instid+"'");    
							}*/
							
							trace("update_production :"+update_production.toString());
							update_product = jdbctemplate.update(update_production.toString());   
							
							StringBuilder updEzcard = new StringBuilder();
							/*updEzcard.append("UPDATE EZCARDINFO set ");
							updEzcard.append("EXPIRYDATE = (SELECT EXPIRY_DATE from INST_CARD_PROCESS where ");
							updEzcard.append("ORG_CHN='"+ecard[i]+"' AND INSTID='"+instid+"')");
							updEzcard.append(",STATUS = '"+switchstatus+"' WHERE CHN = (SELECT HCARD_NO FROM CARD_PRODUCTION_HASH WHERE ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION WHERE ORG_CHN='"+ecard[i]+"'))  AND INSTID='"+instid+"'");
							
							trace("updEzcard :"+updEzcard.toString());
							updezcardinfo = jdbctemplate.update(updEzcard.toString()); */
							
							
							
							///by gowtham-280819
							updEzcard.append("UPDATE EZCARDINFO set ");
							updEzcard.append("EXPIRYDATE = (SELECT EXPIRY_DATE from INST_CARD_PROCESS where ");
							updEzcard.append("ORG_CHN=? AND INSTID=?)");
							updEzcard.append(",STATUS = ? WHERE CHN = (SELECT HCARD_NO FROM CARD_PRODUCTION_HASH WHERE ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION WHERE ORG_CHN=?))  AND INSTID=? ");
							trace("updEzcard :"+updEzcard.toString());
							updezcardinfo = jdbctemplate.update(updEzcard.toString(),new Object[]{ecard[i],instid,switchstatus,ecard[i],instid}); 
							
							
							if( update_product >0  && updezcardinfo >0 )	{ 
								
								updatecount = updatecount + 1;
							}else{
								System.out.println("Process Breaked ====> 1");
								break;
							}
							
							
							
							
						}
				
					/********************fee insert***********************/
					 
					
					String F_cardno="";
					String F_hcardno="";
					String F_mcardno="";
					String F_accountno="";
					String F_caf_rec_status="";
					String F_feeid="";
					String Fee_cardno="";
					
					
				/*	if(padssenable.equals("Y")){		
						Fee_cardno="HCARD_NO='"+Hashchn[i]+"'";
					}else{
						Fee_cardno="CARD_NO='"+Hashchn[i]+"'";	
					}*/
					String acctsubtype="",F_branchcode="";
					Fee_cardno="ORG_CHN='"+ecard[i]+"'";	
					System.out.println("getting card number value " + Fee_cardno);
					List feedetails = commondesc.getfeedetailsinproc(instid,Fee_cardno, jdbctemplate);
					Iterator custitr = feedetails.iterator();
					while(custitr.hasNext()){
						Map mp =(Map) custitr.next();
						F_cardno=(String) mp.get("CARD_NO");
						F_hcardno=(String)mp.get("HCARD_NO");
						F_mcardno=(String)mp.get("MCARD_NO");
						F_accountno=(String)mp.get("ACCT_NO");
						System.out.println("getting account number value " + F_accountno);
						if (F_accountno.length()==14){
							acctsubtype = F_accountno.substring(4,6);
													
						}else{
							acctsubtype=F_accountno.substring(5,7);
						}
						trace("getting acctsubtype value " +acctsubtype);
						F_caf_rec_status=(String)mp.get("CAF_REC_STATUS");
						F_feeid=(String)mp.get("FEE_CODE");
						F_branchcode = (String)mp.get("BRANCH_CODE");
					}
					
					System.out.println("getting accounttype value " + acctsubtype);
					
					/*String checkvalidqry = "SELECT COUNT(1) as CNT FROM FEE_SKIP_DETAILS WHERE ACCT_SUBTYPE_ID='"+acctsubtype+"'";
					String cnt =(String)jdbctemplate.queryForObject(checkvalidqry,String.class);*/
					
					
					
					///by gowtham-280819
					String checkvalidqry = "SELECT COUNT(1) as CNT FROM FEE_SKIP_DETAILS WHERE ACCT_SUBTYPE_ID=? ";
					String cnt =(String)jdbctemplate.queryForObject(checkvalidqry,new Object[]{acctsubtype},String.class);
					
					
					if("0".equalsIgnoreCase(cnt)){
						String renewalflag = "";
						List<Map<String,Object>> getrenewalflag = commondesc.getRenewalFlagByProd(instid, Fee_cardno, jdbctemplate);
						if(!getrenewalflag.isEmpty()){
							renewalflag = (String) getrenewalflag.get(0).get("RENEWALFLAG");
							if(renewalflag == null){
								renewalflag = "N";
							}
						}
						if(renewalflag.equalsIgnoreCase("Y")){
							F_caf_rec_status = "BN";
						}
						trace("renewalflag" + renewalflag);
						//Properties prop = getCommonDescProperty();
						//String F_GLACCOUNTNO = prop.getProperty("fee.glaccountno");
						//String F_TAXGLACCOUNTNO = prop.getProperty("fee.taxglaccountno");
						//String F_GLACCOUNTNO ="34643634643643";
						//String batchno = commondesc.getBatchSeqNo(instid, jdbctemplate);
						String F_TAXGLACCOUNTNO="";
						String F_GLACCOUNTNO="";
					    List checkfeedetails = commondesc.feeinsertactivity(instid,F_cardno,F_hcardno,F_mcardno,F_accountno,cafflag,F_feeid,userid,jdbctemplate);
					    if (!checkfeedetails.isEmpty()){	
					    	trace("checkfeedetails" + checkfeedetails);
					    	Iterator itr=checkfeedetails.iterator();
					    	HashMap mp = (HashMap)itr.next();
					    	
					    	String actualamt = (String) mp.get("FEEAMT");
					    	int taxpercent = 10;
					    	int taxamt  = (Integer.parseInt((actualamt))/100)*taxpercent;
					    	int custdebitamt = Integer.parseInt(actualamt) - taxamt;
					    	
					    	String custamt = String.valueOf(custdebitamt);
					    	String taxamount = String.valueOf(taxamt);
					    	String branchcode = F_branchcode;
					    	int checkinsertfeedet= commondesc.insertfeedetails(instid,F_cardno,F_hcardno,F_mcardno, F_accountno,cafflag,F_feeid,userid,mp,branchcode,jdbctemplate);
					    	trace("checking fee values "+ checkinsertfeedet);
					    	if(checkinsertfeedet!= 1){								
								txManager.rollback(transact.status);
								trace("Fee Details rollbacked Successfully");
								session.setAttribute("preverr", "E");
								session.setAttribute("prevmsg","Fee Details not updated ");
								return "authcardissuehome";
					    	}
					    	
					    	int glcheckinsertfeedet= commondesc.glinsertfeedetails(instid,F_cardno,F_hcardno,F_mcardno, F_GLACCOUNTNO,F_caf_rec_status,F_feeid,userid,mp,custamt,branchcode,jdbctemplate);
					    	trace("checkingglfeeinsert "+ glcheckinsertfeedet);
					    	if(glcheckinsertfeedet!= 1){								
								txManager.rollback(transact.status);
								trace("Fee Details rollbacked Successfully");
								session.setAttribute("preverr", "E");
								session.setAttribute("prevmsg","Fee Details not updated ");
								return "authcardissuehome";
					    	}
					    	int taxinsertfeedet= commondesc.taxinsertfeedetails(instid,F_cardno,F_hcardno,F_mcardno, F_TAXGLACCOUNTNO,F_caf_rec_status,F_feeid,userid,mp,taxamount,branchcode,jdbctemplate);
					    	trace("insert taxinsert fee det " +taxinsertfeedet);
					    	if(taxinsertfeedet!= 1){								
								txManager.rollback(transact.status);
								trace("Fee Details rollbacked Successfully");
								session.setAttribute("preverr", "E");
								session.setAttribute("prevmsg","Fee Details not updated ");
								return "authcardissuehome";
					    	}
					    }
					}
					trace("..................BULK RENUAL EXPIRY DATE ");
				/*********************end fee insert*****************/
					
					/*************AUDIT BLOCK**************/ 
					try{ 
						String cond ="";
						if(padssenable.equals("Y")){		
							cond ="H";
						}else{
							cond = "C";	
						}
						

						//added by gowtham_220719
						trace("ip address======>  "+ip);
						auditbean.setIpAdress(ip);
						
						String mcardno = commondesc.getMaskedCardNoFromProd(instid, ecard[i],cond, jdbctemplate);
						//if(mcardno==null){mcardno=Hashchn[i];}
						auditbean.setActmsg(statusmsg + "Issued Card from checker [ "+mcardno+" ]");
						//auditbean.setUsercode(userid);
						auditbean.setAuditactcode("0206"); 
						auditbean.setActiontype("IC");
						auditbean.setCardno(mcardno);
						//auditbean.setCardnumber(Hashchn[i]);	
						auditbean.setChecker(username);
						auditbean.setCardcollectbranch(comBranchId());
						//commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
						commondesc.insertAuditTrailPendingCommit(instid, userid, auditbean, jdbctemplate, txManager);
						
						commondesc.updatecheckerdata(instid, username, auditbean, jdbctemplate, txManager);

						       
						
						//del_pin = jdbctemplate.update(delete_pin);
						
						
						/*System.out.println("checking values for update product "+ update_product);
						System.out.println("checking values for del_process "+ del_process);
						System.out.println("checking values for updezcardinfo "+ updezcardinfo);
						
						if(  del_process >0 )	{ 
							
							updatecount = updatecount + 1;
						}else{
							System.out.println("Process Breaked ====> 1");
							break;
						}*/
						     
						
						
						
						
						
					 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
				
					
				}else{
					
					trace("**************InstpersonalCardIssuence**************");enctrace("**************InstpersonalCardIssuence**************");
					 
					//trace("Activation the card number : "  + Hashchn[i] );
					 
					final String CARDNORMALSTATUS=CommonDesc.NORMALCARD;			 					
					cardstatus = CARDNORMALSTATUS;
					String custcin ="",productionhcardno="";
					String remarks = (String)getRequest().getParameter("remarks"); 
					String usedchnnNEWreissedcard="",orgchn="",orderfornewcard="",newhcardno="",
							usedecardno="",cardcon="",newhencyptedcardno="",cardnumber="",
							PIN_OFFSET="",EXPIRYDATE="",cardtype="";
					StringBuffer usedhcardno=new StringBuffer();
					
					List carddet = cardregdao.getrejectcarddetailsforResissue(instid,ecard[i],jdbctemplate);
					trace("cardforcbs"+carddet);
					int ezcustomerid=0;
					Iterator itrat = carddet.iterator();
					
						while(itrat.hasNext())
						{
							Map map = (Map) itrat.next(); 
							orderfornewcard=((String)map.get("ORDER_REF_NO"));
							cardtype=((String)map.get("CARDTYPE"));
							
							newhcardno=((String)map.get("HCARD_NO"));
							newhencyptedcardno=((String)map.get("CARD_NO"));
							EXPIRYDATE  = map.get("EXPIRYDATE").toString();
							PIN_OFFSET=((String)map.get("PIN_OFFSET"));
							orgchn=((String)map.get("ORG_CHN"));	
					        usedchnnNEWreissedcard=((String)map.get("USED_CHN"));
						}
					
						usedhcardno = padsssec.getHashedValue(usedchnnNEWreissedcard+instid);
						usedecardno = padsssec.getECHN( CDPK, usedchnnNEWreissedcard);
						
					
					/*if(padssenable.equals("Y")){
					custcin = commondesc.persfchCustomerId(instid,padssenable, Hashchn[i], "INST_CARD_PROCESS", jdbctemplate);
					}else{
					custcin = commondesc.persfchCustomerId(instid,padssenable, Hashchn[i], "INST_CARD_PROCESS", jdbctemplate);	
					}*/
					 
					
					//String cardcon = "";	
					custcin = commondesc.persfchCustomerId(instid,padssenable, ecard[i], "INST_CARD_PROCESS", jdbctemplate);
					trace("checking customer id-->"+custcin);
					//if(padssenable.equals("Y")){cardcon="HCARD_NO";}else{cardcon="CARD_NO";};
						
					cardcon="ORG_CHN";
					
						String deletefromaccount ="",deletefromcust="";
						StringBuilder update_production = new StringBuilder();
						StringBuilder update_process = new StringBuilder();
						StringBuilder update_productiontab = new StringBuilder();
						
						
						int cardvailablle = commondesc.getcarddetailshcard(instid,padssenable, usedchnnNEWreissedcard, "INST_CARD_PROCESS", jdbctemplate);

						if(cardvailablle >0)
					
						{
							int updateproduction=0,updateprocess=0;
							update_productiontab.append("UPDATE IFD_CARD_PRODUCTION  SET  CARD_STATUS='03', STATUS_CODE='74' " );
							update_productiontab.append(" WHERE INST_ID='"+instid+"' AND ORG_CHN ='"+usedchnnNEWreissedcard+"'");
							
							update_process.append("UPDATE IFD_INST_CARD_PROCESS  SET  CARD_STATUS='05',ACCT_NO='',ACCTTYPE_ID='',USED_CHN=ORG_CHN, MKCK_STATUS='P' ,REMARKS='"+remarks+"',CAF_REC_STATUS='A'" );
							update_process.append(" WHERE INST_ID='"+instid+"' AND ORG_CHN ='"+orgchn+"'");
							enctrace("fdf"+update_process+"\n"+update_productiontab);
							
							
							updateproduction = jdbctemplate.update(update_productiontab.toString());
							updateprocess = jdbctemplate.update(update_process.toString());
							
							trace("sssssssss"+updateproduction+"updateprocess"+updateprocess);
							if(updateproduction > 0 && updateprocess >0){
								update_production.append("UPDATE EZCARDINFO  SET  STATUS='74' " );
								update_production.append(" WHERE CHN in (select hcard_no from ifd_card_production where INSTID='"+instid+"' AND ORG_CHN ='"+usedchnnNEWreissedcard+"')");
								
							}
							trace("dsafds"+update_production);
							
						}
						
						else{
							int deleteifdaccount=0,deleteifdcust=0;
							deletefromaccount = "DELETE FROM IFD_ACCOUNTINFO WHERE INST_ID='"+instid+"' and cin='"+custcin+"'";
							deletefromcust = "DELETE FROM IFD_CUSTOMERINFO WHERE INST_ID='"+instid+"' and cin='"+custcin+"'";				 				
							
							deleteifdaccount = jdbctemplate.update(deletefromaccount);
							
							if(deleteifdaccount > 0)
							{
								deleteifdcust = jdbctemplate.update(deletefromcust);
								if(deleteifdcust > 0)
								{
									update_production.append("UPDATE "+tablename+" SET CAF_REC_STATUS='A',ACCT_NO='',ACCTTYPE_ID='',CARD_STATUS='"+cardstatus+"', STATUS_CODE='',MKCK_STATUS = 'P',CHECKER_DATE=(SYSDATE),CHECKER_ID='"+userid+"', " );
									update_production.append("CIN='',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' and "+cardcon+"='"+orgchn+"'");
									
								}
							}
							
							
						}
						
						

						enctrace("update query for instcard processs "+update_production);
					
					
						int deleteifdcust = -1;int deleteifdaccount = -1,deauth_updprocess=-1;
					
						try{
						
							
						deauth_updprocess = jdbctemplate.update(update_production.toString());
						trace("checking deauth_updprocess-->"+deauth_updprocess);
						
						System.out.println("result :::::::::::"+deauth_updprocess+deleteifdaccount+deleteifdcust);
						trace("result :::::::::::"+deauth_updprocess+deleteifdaccount+deleteifdcust);   
						if(deauth_updprocess >0)   
						{
							
							
							updatecount = updatecount + 1;
							statusmsg="Card Rejected Successfully";
							
							/*************AUDIT BLOCK**************/ 
							try{ 
								String cond ="";
								if(padssenable.equals("Y")){		
									cond ="H";
								}else{
									cond = "C";	
								}
								

								//added by gowtham_220719
								trace("ip address======>  "+ip);
								auditbean.setIpAdress(ip);
								
								String mcardno = commondesc.getMaskedCardNoFromProd(instid, ecard[i],cond, jdbctemplate);
								//if(mcardno==null){mcardno=Hashchn[i];}
								auditbean.setActmsg(statusmsg + "Issued Card [ "+mcardno+" ]");
								auditbean.setUsercode(userid);
								auditbean.setAuditactcode("0206");  
								auditbean.setCardno(mcardno);
								//auditbean.setCardnumber(Hashchn[i]);
								auditbean.setRemarks(remarks);
								//commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
								commondesc.insertAuditTrailPendingCommit(instid, userid, auditbean, jdbctemplate, txManager);
							 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
							
						}
						
						}catch(Exception e)
						{
							trace("Exception in moving production :: 2:::"+e);
							
						}
					
				}	
				
			}
			
						
				
				/*if(personalcardissue >0){
					//txManager.commit(transact.status);
					//trace("getting udpate count " + upd_process);
					
					if(upd_process > 0){
						updatecount = updatecount + 1;
					}				
				else	{
						System.out.println("Process Breaked ====> in CAF REC STATUS A 1");
						txManager.rollback(transact.status);
						addActionError("Unable to continue...");
						return authInstCustomerHome();
					}
					
					
					//trace("Order Status Updated Successfully and Commited ");
					//session.setAttribute("preverr", "S");
					//session.setAttribute("prevmsg",ordercount+" Card Mapped Sucessfully");
				}else{
					trace("Process Breaked ====> in CAF REC STATUS A 2");
					addActionError("Unable to Continue...");
					txManager.rollback(transact.status);
					return authInstCustomerHome();
				}
				System.out.println("############################################################################");
			}*/		
			
		} catch (Exception e){			
			trace("Error While Execute the Query ---->"+e.getMessage());
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg","Unable to continue the process");
			e.printStackTrace();
		} 
		
		trace("ordercount == ordercount:::"+ordercount);
		trace("updatecount == updatecount:::"+updatecount);
		if(ordercount == updatecount)
		{
				//if(ordercount>0){     
			txManager.commit(transact.status);
			trace("Order Status Updated Successfully and Commited ");
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg",statusmsg);						
										
		 /*************AUDIT BLOCK**************/
		}
		else{
			txManager.rollback(transact.status);
			trace("Txn Got Rollbacked ---->");
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg","Card Status not updated ");
		}
		
		setAct((String)session.getAttribute("ACTIONTYPE")); 
		return authInstCustomerHome();
	}
	
	/// Method added by sardar on 29-07-2016 *****************//
	public synchronized int InstpersonalCardIssuence(String cardno,String padssenable,String instid,String maker_id,String tablename,JdbcTemplate jdbctemplate) throws Exception	{
		trace("**************InstpersonalCardIssuence**************");
		enctrace("**************InstpersonalCardIssuence**************");
		int issue_status = -1, custinfo_move = -1,delete_status = -1,custinfo_status = -1;
		
		trace("Activation the card number : "  + cardno );
		String reissuedate="''",reissue_count = "0",repindate="''",repincount="0",damgedate="''",blockdate="''",hotdate="''",closedte="''",pinretry_count="0";
		String active_date="''";
		String status_code= commondesc.getSwitchCardStatus(instid, CARDACTIVATEDCODE, jdbctemplate);
		String userid =comUserCode();  
		trace("checking switch status code" + status_code);
		
		/*String deletefromProcess ="";
		if(padssenable.equals("Y")){   
		deletefromProcess = "DELETE FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND HCARD_NO='"+cardno+"' ";
		}else
		{
		deletefromProcess = "DELETE FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"' ";	
		}
		trace("checking deletion in card process" + deletefromProcess);*/
		
		String cardstatus = CARDACTIVATEDCODE;
		String switchstatus = commondesc.getSwitchCardStatus(instid, cardstatus, jdbctemplate);
		String cardissuetype=null;
		
		/*if(padssenable.equals("Y"))
		{*/
		
		
		cardissuetype = commondesc.getCardIssueTypeByCard(instid,padssenable, cardno, "INSTANT", jdbctemplate);
		trace("Getting cardissuetype...got :  " + cardissuetype);
		
		/*}else{
		cardissuetype = commondesc.getCardIssueTypeByCard(instid,padssenable, cardno, "INSTANT", jdbctemplate);	
		}*/
		
		
		
		String custcin ="";
		//if(padssenable.equals("Y")){
		
		custcin = commondesc.persfchCustomerId(instid,padssenable, cardno, "INST_CARD_PROCESS", jdbctemplate);
		trace("Got the customer id : " + custcin);
		
		/*}else{
		custcin = commondesc.persfchCustomerId(instid,padssenable, cardno, "INST_CARD_PROCESS", jdbctemplate);	
		}*/
		
		
		System.out.println("CUSTOMERN NUMBER IS =====> "+custcin);  
		
		String acctno = null;
		int acctinfo_insert =-1;
		int acctpriority = -1;
		if( cardissuetype !=null && cardissuetype.equals("$SUPLIMENT"))
		{
			acctpriority = 2;
			//String parentcardno = CommonDesc.getParentCardNumberByCard(instid, cardno, "PERSONAL", jdbctemplate);
			//acctno = commondesc.getCardPrimaryAccount(instid, parentcardno, "currency", jdbctemplate);
			//trace("Got the acct number from parent card  : " + acctno );
			//commondesc.updateAddonCountToParent(instid,parentcardno,jdbctemplate);
			acctinfo_insert =1;
		}
		else
		{
			
			trace("coming into card issuance");
			acctpriority = 1;
	//		acctno = curcode+commondesc.paddingZero(Integer.toString(curacctseq), Integer.parseInt(acctnolen)-curcode.length());
	//		trace("Generated account number : " + acctno );
			String INSTID_1="" ,CHN_1="" ,BIN="",ACCOUNTNO_1="" ,ACCOUNTTYPE_1="" ,ACCOUNTFLAG_1="" ,ACCOUNTPRIORITY_1="" ,CURRCODE_1="" ;
			String INSTID_2="" ,ACCOUNTNO_2="" ,ACCOUNTTYPE_2="" ,CURRCODE_2="" ,AVAILBAL_2="" ,LEDGERBAL_2="" ,LIMITFLAG_2="1" ,STATUS_2="" ,TXNGROUPID_2="" ,LASTTXNDATE_2="" ,LASTTXNTIME_2="" ,BRANCHCODE_2="" ,PRODUCTCODE_2=""; 
			String INSTID_3="" ,CHN_3="" ,CARDTYPE_3="" ,CUSTID_3="" ,TXNGROUPID_3="" ,LIMITFLAG_3="1" ,EXPIRYDATE_3="" ,STATUS="" ,PINOFFSET_3="" ,OLDPINOFFSET_3="" ,TPINOFFSET_3="" ,OLDTPINOFFSET_3="" ,PINRETRYCOUNT_3="" ,TPINRETRYCOUNT_3="" ,PVKI_3="" ,LASTTXNDATE_3="" ,LASTTXNTIME_3="" ,PANSEQNO_3=""; 
			String INSTID_4="" ,CUSTID_4="" ,NAME_4="" ,DOB_4="" ,SPOUSENAME_4="" ,ADDRESS1_4="" ,ADDRESS2_4="" ,ADDRESS3_4="" ,OFFPHONE_4="" ,MOBILE_4="" ,EMAIL_4="" ,RESPHONE_4=""; 
			String LIMITRECID_5="",cardcon1="";
			int res_1 =0;int res_2 =0;int res_3=0;int res_4=0;int res_5 = 0;
			StringBuilder mv = new StringBuilder();
			
			mv.append("SELECT ");
			//--EZAUTHREL start
			//--INSTID, CHN,ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE,
			//if(padssenable.equals("Y")){
			
			
			mv.append("'"+instid+"' INSTID_1, PCP.ORG_CHN CHN_1,PCP.ACCT_NO ACCOUNTNO_1,PCP.BIN,AI.ACCTTYPE_ID ACCOUNTTYPE_1,PCP.CARDISSUETYPE ACCOUNTFLAG_1,'1' ACCOUNTPRIORITY_1 ,AI.ACCT_CURRENCY CURRCODE_1, ");
			
			
			/*}else{
			mv.append("'"+instid+"' INSTID_1, PCP.CARD_NO CHN_1,AI.ACCOUNTNO ACCOUNTNO_1,PCP.BIN,AI.ACCTTYPE_ID ACCOUNTTYPE_1,PCP.CARDISSUETYPE ACCOUNTFLAG_1,'1' ACCOUNTPRIORITY_1 ,AI.ACCT_CURRENCY CURRCODE_1, ");	
			}*/
		/*	if(padssenable.equals("Y"))
			{
				cardcon1="HCARD_NO";
				
			}		
			else
			{*/
			
				cardcon1="ORG_CHN";
				
			//};
			
			//--EZAUTHREL end  
			//--EZACCOUNTINFO start
			//--INSTID, ACCOUNTNO, ACCOUNTTYPE, CURRCODE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE
			mv.append("'"+instid+"' INSTID_2, AI.ACCOUNTNO ACCOUNTNO_2, AI.ACCTTYPE_ID ACCOUNTTYPE_2, AI.ACCT_CURRENCY CURRCODE_2, '0' AVAILBAL_2, '0' LEDGERBAL_2,  ");
			mv.append("(SELECT ACCT_FLAG FROM GLOBAL_CARDDETAILS WHERE LIMIT_TYPE = AI.ACCOUNTTYPE) LIMITFLAG_2, '"+status_code+"' STATUS_2,  ");
			mv.append("'01' TXNGROUPID_2, TO_CHAR(SYSDATE,'MM/DD/YYYY') LASTTXNDATE_2,TO_CHAR(SYSDATE,'HH24MISS')  LASTTXNTIME_2,  ");
			mv.append("PCP.BRANCH_CODE BRANCHCODE_2, AI.ACCTSUB_TYPE_ID PRODUCTCODE_2, ");
			//-- EZACCOUNTINFO end
			//-- EZCARDINFO start
			//--INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME, PANSEQNO
			/*if(padssenable.equals("Y"))
			{*/
			
			mv.append("'"+instid+"' INSTID_3, PCP.ORG_CHN CHN_3, PCP.CARD_TYPE_ID CARDTYPE_3,'"+custcin+"' CUSTID_3,'01' TXNGROUPID_3, "); 
			
			/*}else{
			mv.append("'"+instid+"' INSTID_3, PCP.CARD_NO CHN_3, PCP.CARD_TYPE_ID CARDTYPE_3,'"+custcin+"' CUSTID_3,'01' TXNGROUPID_3, ");	
			}*/
			mv.append("(SELECT CARD_FLAG FROM GLOBAL_CARDDETAILS WHERE LIMIT_TYPE = AI.ACCOUNTTYPE) LIMITFLAG_3,  ");
			mv.append("TO_CHAR(PCP.EXPIRY_DATE,'MM/DD/YYYY')  EXPIRYDATE_3,'"+status_code+"' STATUS , NVL(PCP.PIN_OFFSET,0)  PINOFFSET_3, NVL(PCP.OLD_PIN_OFFSET,0) OLDPINOFFSET_3, "); 
			mv.append("'0' TPINOFFSET_3,'0' OLDTPINOFFSET_3,NVL(PCP.PIN_RETRY_COUNT,0) PINRETRYCOUNT_3,'0' TPINRETRYCOUNT_3, '0' PVKI_3,  ");
			mv.append("TO_CHAR(SYSDATE,'MM/DD/YYYY') LASTTXNDATE_3,TO_CHAR(SYSDATE,'HH24MISS')  LASTTXNTIME_3, '00' PANSEQNO_3, ");  
			//-- EZCARDINFO end  
			//--EZCUSTOMERINFO start
			//--INSTID, CUSTID, NAME, DOB, SPOUSENAME, ADDRESS1, ADDRESS2, ADDRESS3, OFFPHONE, MOBILE, EMAIL, RESPHONE
			mv.append("'"+instid+"' INSTID_4,'"+custcin+"' CUSTID_4, CI.FNAME NAME_4, CI.DOB  DOB_4,CI.SPOUCE_NAME SPOUSENAME_4, ");
			mv.append("CI.C_HOUSE_NO ADDRESS1_4, CI.C_STREET_NAME ADDRESS2_4,CI.C_CITY ADDRESS3_4, CI.C_PHONE1 OFFPHONE_4,CI.MOBILE MOBILE_4, ");
			mv.append("CI.E_MAIL  EMAIL_4 ,CI.C_PHONE2 RESPHONE_4, PCP.LIMIT_ID LIMIT_RECORDID_5 FROM ");
			//--EZCUSTOMERINFO end  
			mv.append(" CUSTOMERINFO CI ,ACCOUNTINFO AI ,INST_CARD_PROCESS PCP "); 
			mv.append("WHERE  ");
			//mv.append(" (CI.ORDER_REF_NO = AI.ORDER_REF_NO AND AI.ORDER_REF_NO = PCP.ORDER_REF_NO AND PCP.ORDER_REF_NO = CI.ORDER_REF_NO) AND"); migdata changes
			mv.append("(CI.CIN = AI.CIN AND AI.CIN = PCP.CIN AND PCP.CIN = CI.CIN) AND ");
			mv.append("(CI.INST_ID = AI.INST_ID AND AI.INST_ID = PCP.INST_ID AND PCP.INST_ID = CI.INST_ID) ");
			mv.append("AND CI.INST_ID='"+instid+"' AND AI.INST_ID='"+instid+"' AND PCP.INST_ID='"+instid+"'  ");
			mv.append("AND CI.CIN='"+custcin+"' AND AI.CIN='"+custcin+"' AND PCP.CIN='"+custcin+"' AND "+cardcon1+"='"+cardno+"' "); 
			  
			enctrace("Move to Production Query-----------------------------------\n"+ mv.toString());
			//enctrace(mv.toString());
			
			
			//added by senthil
			
			
			/*mv.append("SELECT ");
			//--EZAUTHREL start
			//--INSTID, CHN,ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE,
			if(padssenable.equals("Y")){
			mv.append("'"+instid+"' INSTID_1, PCP.HCARD_NO CHN_1,AI.ACCOUNTNO ACCOUNTNO_1,AI.ACCTTYPE_ID ACCOUNTTYPE_1,PCP.PC_FLAG ACCOUNTFLAG_1,'1' ACCOUNTPRIORITY_1 ,AI.ACCT_CURRENCY CURRCODE_1, ");
			}else{
			mv.append("'"+instid+"' INSTID_1, PCP.CARD_NO CHN_1,AI.ACCOUNTNO ACCOUNTNO_1,AI.ACCTTYPE_ID ACCOUNTTYPE_1,PCP.PC_FLAG ACCOUNTFLAG_1,'1' ACCOUNTPRIORITY_1 ,AI.ACCT_CURRENCY CURRCODE_1, ");	
			}
			if(padssenable.equals("Y")){cardcon1="HCARD_NO";}else{cardcon1="CARD_NO";};
			//--EZAUTHREL end  
			//--EZACCOUNTINFO start
			//--INSTID, ACCOUNTNO, ACCOUNTTYPE, CURRCODE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE
			mv.append("'"+instid+"' INSTID_2, AI.ACCOUNTNO ACCOUNTNO_2, AI.ACCTTYPE_ID ACCOUNTTYPE_2, AI.ACCT_CURRENCY CURRCODE_2, '0' AVAILBAL_2, '0' LEDGERBAL_2,  ");
			mv.append("(SELECT ACCT_FLAG FROM GLOBAL_CARDDETAILS WHERE LIMIT_TYPE = AI.ACCOUNTTYPE) LIMITFLAG_2, '"+status_code+"' STATUS_2,  ");
			mv.append("'01' TXNGROUPID_2, TO_CHAR(SYSDATE,'MM/DD/YYYY') LASTTXNDATE_2,TO_CHAR(SYSDATE,'HH24MISS')  LASTTXNTIME_2,  ");
			mv.append("PCP.BRANCH_CODE BRANCHCODE_2, AI.ACCTSUB_TYPE_ID PRODUCTCODE_2, ");
			//-- EZACCOUNTINFO end
			//-- EZCARDINFO start
			//--INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME, PANSEQNO
			if(padssenable.equals("Y")){
			mv.append("'"+instid+"' INSTID_3, PCP.HCARD_NO CHN_3, PCP.CARD_TYPE_ID CARDTYPE_3,'"+custcin+"' CUSTID_3,'01' TXNGROUPID_3, "); 
			}else{
			mv.append("'"+instid+"' INSTID_3, PCP.CARD_NO CHN_3, PCP.CARD_TYPE_ID CARDTYPE_3,'"+custcin+"' CUSTID_3,'01' TXNGROUPID_3, ");	
			}
			mv.append("(SELECT CARD_FLAG FROM GLOBAL_CARDDETAILS WHERE LIMIT_TYPE = AI.ACCOUNTTYPE) LIMITFLAG_3,  ");
			mv.append("TO_CHAR(PCP.EXPIRY_DATE,'MM/DD/YYYY')  EXPIRYDATE_3,'"+status_code+"' STATUS , NVL(PCP.PIN_OFFSET,0)  PINOFFSET_3, NVL(PCP.OLD_PIN_OFFSET,0) OLDPINOFFSET_3, "); 
			mv.append("'0' TPINOFFSET_3,'0' OLDTPINOFFSET_3,NVL(PIN_RETRY_COUNT,0) PINRETRYCOUNT_3,'0' TPINRETRYCOUNT_3, '0' PVKI_3,  ");
			mv.append("TO_CHAR(SYSDATE,'MM/DD/YYYY') LASTTXNDATE_3,TO_CHAR(SYSDATE,'HH24MISS')  LASTTXNTIME_3, '00' PANSEQNO_3, ");  
			//-- EZCARDINFO end  
			//--EZCUSTOMERINFO start
			//--INSTID, CUSTID, NAME, DOB, SPOUSENAME, ADDRESS1, ADDRESS2, ADDRESS3, OFFPHONE, MOBILE, EMAIL, RESPHONE
			mv.append("'"+instid+"' INSTID_4,'"+custcin+"' CUSTID_4, CI.FNAME NAME_4, CI.DOB  DOB_4,CI.SPOUCE_NAME SPOUSENAME_4, ");
			mv.append("CI.C_HOUSE_NO ADDRESS1_4, CI.C_STREET_NAME ADDRESS2_4,CI.C_CITY ADDRESS3_4, CI.C_PHONE1 OFFPHONE_4,CI.MOBILE MOBILE_4, ");
			mv.append("CI.E_MAIL  EMAIL_4 ,CI.C_PHONE2 RESPHONE_4, PCP.LIMIT_ID LIMIT_RECORDID_5 FROM ");
			//--EZCUSTOMERINFO end  
			mv.append(" CUSTOMERINFO CI ,ACCOUNTINFO AI ,INST_CARD_PROCESS PCP "); 
			mv.append("WHERE  ");
			//modifed by senthil
			//uncommented
			//mv.append(" (CI.ORDER_REF_NO = AI.ORDER_REF_NO AND AI.ORDER_REF_NO = PCP.ORDER_REF_NO AND PCP.ORDER_REF_NO = CI.ORDER_REF_NO) AND");
			//commened
			mv.append("(CI.CIN = AI.CIN AND AI.CIN = PCP.CIN AND PCP.CIN = CI.CIN) AND ");
			
			mv.append("(CI.INST_ID = AI.INST_ID AND AI.INST_ID = PCP.INST_ID AND PCP.INST_ID = CI.INST_ID and pcp.acct_no=ai.ACCOUNTNO) ");
			mv.append("AND CI.INST_ID='"+instid+"' AND AI.INST_ID='"+instid+"' AND PCP.INST_ID='"+instid+"'  ");
			//commented
			mv.append("AND CI.CIN='"+custcin+"' AND AI.CIN='"+custcin+"' AND PCP.CIN='"+custcin+"' AND "+cardcon1+"='"+cardno+"' "); 
			mv.append("AND "+cardcon1+"='"+cardno+"' ");
			//end
			enctrace("Move to Production Query-----------------------------------\n");
			enctrace(mv.toString());
			
			//end
*/			
			
			List movetoSwitchP = jdbctemplate.queryForList(mv.toString());
			Iterator custitr = movetoSwitchP.iterator();
			while(custitr.hasNext())
			{
				Map mp = (Map)custitr.next();  
				//fname = (String)mp.get("FNAME");
						INSTID_1   = (String) mp.get(" INSTID_1 ");
						CHN_1  = (String) mp.get("CHN_1");
						ACCOUNTNO_1  = (String) mp.get("ACCOUNTNO_1");
						ACCOUNTTYPE_1  =(String)  mp.get("ACCOUNTTYPE_1");
						ACCOUNTFLAG_1  =(String)  mp.get("ACCOUNTFLAG_1");
						BIN  =(String)  mp.get("BIN");
						 
						ACCOUNTPRIORITY_1  =(String)  mp.get("ACCOUNTPRIORITY_1");
						CURRCODE_1  = (String) mp.get("CURRCODE_1");
						INSTID_2  = (String) mp.get("INSTID_2");
						ACCOUNTNO_2  = (String) mp.get("ACCOUNTNO_2");
						ACCOUNTTYPE_2  = (String) mp.get("ACCOUNTTYPE_2");
						CURRCODE_2  = (String) mp.get("CURRCODE_2");
						AVAILBAL_2  = (String) mp.get("AVAILBAL_2");
						LEDGERBAL_2  = (String) mp.get("LEDGERBAL_2"); 
						LIMITFLAG_2  = (String) mp.get("LIMITFLAG_2");
						//LIMITFLAG_2  = "2";
						//STATUS_2  = (String) mp.get("STATUS_2");
						STATUS_2="50";
						TXNGROUPID_2  = (String) mp.get("TXNGROUPID_2");
						LASTTXNDATE_2  = (String) mp.get("LASTTXNDATE_2");
						LASTTXNTIME_2  = (String) mp.get("LASTTXNTIME_2");
						BRANCHCODE_2  = (String) mp.get("BRANCHCODE_2");
						PRODUCTCODE_2  = (String) mp.get("PRODUCTCODE_2");
						
						//For identify added by gowtham
						System.out.println("Values Checking=====>");
						
						System.out.println("1"+mp.get("INSTID_3").toString());
						System.out.println("2"+mp.get("INSTID_3").toString());
						System.out.println("4"+mp.get("CHN_3").toString());
						System.out.println("5"+mp.get("CARDTYPE_3").toString());
						System.out.println("6"+mp.get("CUSTID_3").toString());
						System.out.println("7"+mp.get("TXNGROUPID_3").toString());
						System.out.println("8"+mp.get("LIMITFLAG_3").toString());
						System.out.println("8"+mp.get("EXPIRYDATE_3").toString());
						System.out.println("9"+mp.get("STATUS").toString());
						System.out.println("0"+mp.get("PINOFFSET_3") .toString());
						System.out.println("1"+mp.get("OLDPINOFFSET_3").toString());
						System.out.println("3"+mp.get("TPINOFFSET_3").toString());
						System.out.println("5"+mp.get("OLDTPINOFFSET_3").toString()); 
						System.out.println("6"+mp.get("PINRETRYCOUNT_3").toString());
						System.out.println("7"+mp.get("TPINRETRYCOUNT_3").toString());
						
						
						CHN_3  = mp.get("CHN_3").toString();
						CARDTYPE_3  = mp.get("CARDTYPE_3").toString();
						CUSTID_3  = mp.get("CUSTID_3").toString();
						TXNGROUPID_3  = mp.get("TXNGROUPID_3").toString();
						LIMITFLAG_3  = mp.get("LIMITFLAG_3").toString();
						//LIMITFLAG_3="5";
						EXPIRYDATE_3  = mp.get("EXPIRYDATE_3").toString();
						STATUS  = mp.get("STATUS").toString();
						PINOFFSET_3  = mp.get("PINOFFSET_3") .toString();
						OLDPINOFFSET_3  = mp.get("OLDPINOFFSET_3").toString();
						TPINOFFSET_3  = mp.get("TPINOFFSET_3").toString();
						OLDTPINOFFSET_3  = mp.get("OLDTPINOFFSET_3").toString(); 
						PINRETRYCOUNT_3  = mp.get("PINRETRYCOUNT_3").toString();
						TPINRETRYCOUNT_3  = mp.get("TPINRETRYCOUNT_3").toString();
						PVKI_3  = mp.get("PVKI_3").toString();
						LASTTXNDATE_3  = mp.get("LASTTXNDATE_3").toString();
						LASTTXNTIME_3  = mp.get("LASTTXNTIME_3").toString();
						PANSEQNO_3  = mp.get("PANSEQNO_3") .toString();
						
						
						INSTID_4  = mp.get("INSTID_4").toString();
						CUSTID_4  = mp.get("CUSTID_4").toString();
						NAME_4  = (String)mp.get("NAME_4");
						DOB_4  = (String)mp.get("DOB_4");
						SPOUSENAME_4  = (String)mp.get("SPOUSENAME_4");
						ADDRESS1_4  = (String)mp.get("ADDRESS1_4");
						ADDRESS2_4  = (String)mp.get("ADDRESS2_4");
						ADDRESS3_4  = (String)mp.get("ADDRESS3_4");
						OFFPHONE_4  = (String)mp.get("OFFPHONE_4");
						MOBILE_4  = (String)mp.get("MOBILE_4");
						EMAIL_4  = (String)mp.get("EMAIL_4");
						RESPHONE_4 = (String)mp.get("RESPHONE_4");
						LIMITRECID_5 = (String)mp.get("LIMIT_RECORDID_5");
						
			}
		String cardcon = "";	
		/*if(padssenable.equals("Y"))
		{
			cardcon="HCARD_NO";		
		}
		else
		{*/
			//cardcon="ORG_CHN";			
		//};
			
			String deletefromProcess ="";
			//deletefromProcess = "DELETE FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' and "+cardcon+"='"+cardno+"'";
		
			/*String movetoproduction = "INSERT INTO CARD_PRODUCTION  (";
			movetoproduction += "INST_ID, CARD_NO, CIN, ORDER_REF_NO, CARD_STATUS, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY, ";
			movetoproduction += "GENERATED_DATE, EXPIRY_DATE, PRE_DATE,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE,";
			movetoproduction += "APP_DATE, PIN_DATE, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT,";
			movetoproduction += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID,";
			movetoproduction += "BIN, AUTH_DATE, STATUS_CODE, PIN_OFFSET, OLD_PIN_OFFSET, PIN_RETRY_CNT, AUTO_ACCT_FLAG, CVV1, CVV2, ICVV, CARD_REF_NO,HCARD_NO,MCARD_NO)";
				  
			movetoproduction += " SELECT INST_ID, CARD_NO, CIN, ORDER_REF_NO, CARD_STATUS, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY,";
			movetoproduction += "GENERATED_DATE, EXPIRY_DATE, PRE_DATE, MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE, ";
			movetoproduction += "APP_DATE, PIN_DATE, RECV_DATE, SYSDATE, ACTIVE_DATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT, ";
			movetoproduction += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID, ";
			movetoproduction += "BIN, AUTH_DATE,  STATUS_CODE, PIN_OFFSET, OLD_PIN_OFFSET, PIN_RETRY_CNT, AUTO_ACCT_FLAG, CVV1, CVV2, ICVV, CARD_REF_NO,HCARD_NO,MCARD_NO FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' and "+cardcon+"='"+cardno+"'";
			  enctrace("moveCardToProduction   "+movetoproduction);*/
			
			if(BIN.equals("900404")){//azizi bin
				cardstatus="05";
				STATUS="53";
				status_code="53";
				switchstatus="53";
			}
			else if(BIN.equals("501817")){
				cardstatus="05";
				STATUS="97";
				status_code="97";
				switchstatus="97";
			}
			
			else if(BIN.equals("551270")){
				cardstatus="05";
				STATUS="53";
				status_code="53";
				switchstatus="53";
			}
			else if(BIN.equals("413703")){//bkb bin
				cardstatus="09";
				STATUS="16";
				status_code="16";
				switchstatus="16";
			}

			
			String movetoproduction = "INSERT INTO CARD_PRODUCTION (INST_ID,ORDER_REF_NO,BIN,MCARD_NO,ACCOUNT_NO,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE," +
					"EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE,REG_DATE,RECV_DATE,ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT," +
					"ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,ADDON_FLAG,ADDON_CNT,REISSUE_DATE,REISSUE_CNT,REPIN_DATE,REPIN_CNT,DAMAGE_DATE,BLOCK_DATE,HOT_GENDATE,CLOSE_DATE,PIN_OFFSET,PIN_RETRY_CNT,AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO,CARDISSUETYPE,RENEWALFLAG,CARD_COLLECT_BRANCH)" +
					"(SELECT INST_ID,ORDER_REF_NO,BIN,MCARD_NO,ACCT_NO,ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACC_CCY, CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,'"+CARDACTIVATEDCODE+"',CAF_REC_STATUS,"+status_code+",CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE," +
					"PRE_FILE,REG_DATE,RECV_DATE,(SYSDATE),SYSDATE,MAKER_ID,(SYSDATE),'"+userid+"',CHECKER_DATE,'P',SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT," +
					"ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,'','0',"+reissuedate+",'"+reissue_count+"',"+repindate+",'"+repincount+"',"+damgedate+","+blockdate+","+hotdate+","+closedte+",PIN_OFFSET,'"+pinretry_count+"',AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO,CARDISSUETYPE,RENEWALFLAG,CARD_COLLECT_BRANCH FROM  INST_CARD_PROCESS" +
					" WHERE INST_ID='"+instid+"' AND ORG_CHN='"+cardno+"')";
					enctrace("movetoproduction for 2 : " + movetoproduction );
					
					String movetoproduction1 = "INSERT INTO CARD_PRODUCTION_HASH (INST_ID,ORDER_REF_NO,BIN,HCARD_NO,ACCOUNT_NO,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE," +
							"MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE) "+
							"(SELECT INST_ID,ORDER_REF_NO,BIN,HCARD_NO,ACCT_NO,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,GENERATED_DATE," +
							"MAKER_ID,(SYSDATE),'"+userid+"',CHECKER_DATE FROM  INST_CARD_PROCESS_HASH" +
							" WHERE INST_ID='"+instid+"' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM INST_CARD_PROCESS WHERE ORG_CHN ='"+cardno+"'))";
							enctrace("movetoproduction  for 2: " + movetoproduction );
			
		
			/*String movetoproduction = "INSERT INTO CARD_PRODUCTION (INST_ID,ORDER_REF_NO,BIN,CARD_NO,HCARD_NO,MCARD_NO,ACCOUNT_NO,ACCTTYPE_ID, ACCTSUB_TYPE_ID,CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,CARD_STATUS,CAF_REC_STATUS,STATUS_CODE,CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE," +
					"EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE,PRE_FILE,REG_DATE,RECV_DATE,ISSUE_DATE,ACTIVE_DATE,MAKER_ID,MAKER_DATE,CHECKER_ID,CHECKER_DATE,MKCK_STATUS,SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT," +
					"ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,ADDON_FLAG,ADDON_CNT,REISSUE_DATE,REISSUE_CNT,REPIN_DATE,REPIN_CNT,DAMAGE_DATE,BLOCK_DATE,HOT_GENDATE,CLOSE_DATE,PIN_OFFSET,PIN_RETRY_CNT,AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO,CARDISSUETYPE)" +
					"(SELECT INST_ID,ORDER_REF_NO,BIN,CARD_NO,HCARD_NO,MCARD_NO,ACCT_NO,ACCTTYPE_ID, ACCTSUB_TYPE_ID, CIN,CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,BRANCH_CODE,'"+CARDACTIVATEDCODE+"',CAF_REC_STATUS,"+status_code+",CARD_CCY,PC_FLAG,ORDER_FLAG,ORG_CHN,USED_CHN,GENERATED_DATE,EXPIRY_DATE,APP_NO,APP_DATE,PIN_DATE,PRE_DATE," +
					"PRE_FILE,REG_DATE,RECV_DATE,(SYSDATE),SYSDATE,'"+maker_id+"',(SYSDATE),CHECKER_ID,CHECKER_DATE,'P',SERVICE_CODE,FEE_CODE,LIMIT_ID,PRIVILEGE_CODE,WDL_AMT,WDL_CNT,PUR_AMT,PUR_CNT,ECOM_AMT," +
					"ECOM_CNT,EMB_NAME,ENC_NAME,COURIER_ID,AUTH_DATE,'','0',"+reissuedate+",'"+reissue_count+"',"+repindate+",'"+repincount+"',"+damgedate+","+blockdate+","+hotdate+","+closedte+",PIN_OFFSET,'"+pinretry_count+"',AUTO_ACCT_FLAG,OLD_PIN_OFFSET,CVV1,CVV2,ICVV,MOBILENO,CARD_REF_NO,CARDISSUETYPE FROM INST_CARD_PROCESS " +
					"WHERE INST_ID='"+instid+"' AND "+cardcon+"='"+cardno+"')";*/
					
					
		
		//1	
							
			
			String custexistqry = "SELECT COUNT(*) as cnt FROM EZCUSTOMERINFO WHERE CUSTID= '"+custcin+"'";		
			String custcount = (String) jdbctemplate.queryForObject(custexistqry,String.class);		
			StringBuilder cinf_4 = new StringBuilder();		
					cinf_4.append("INSERT INTO EZCUSTOMERINFO ");
					cinf_4.append("(INSTID, CUSTID, NAME, DOB, SPOUSENAME, ADDRESS1, ADDRESS2, ADDRESS3, OFFPHONE, MOBILE, EMAIL, RESPHONE) ");
					cinf_4.append("VALUES ");
					cinf_4.append("('"+instid+"','"+CUSTID_4+"','"+NAME_4+"',SYSDATE,'"+SPOUSENAME_4+"', ");
					cinf_4.append("'"+ADDRESS1_4+"','"+ADDRESS2_4+"','"+ADDRESS3_4+"','"+OFFPHONE_4+"','"+MOBILE_4+"','"+EMAIL_4+"','"+RESPHONE_4+"') ");
					
					enctrace("cinf_4:::::"+cinf_4.toString());
					
					 try{
				    		if("0".equalsIgnoreCase(custcount)){
				    			res_4 = jdbctemplate.update(cinf_4.toString());
				    		}else{res_4 = 1;}
				    		}catch(Exception e)
				    		{
				    			trace("Exception in moving production :: 1:::::::"+e);
				    			return -1;
				    		}
		//2			
					 //trace("HCARD_NO is "+HCARD_NO);
		StringBuilder crdinf_3 = new StringBuilder();		
						crdinf_3.append("INSERT INTO EZCARDINFO ");
						crdinf_3.append("(INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME,CVV, PANSEQNO) ");
						crdinf_3.append("VALUES ");
					/*	crdinf_3.append("('"+instid+"','"+CHN_3+"','"+CARDTYPE_3+"','"+CUSTID_3+"','"+TXNGROUPID_3+"','"+LIMITFLAG_3+"',TO_DATE('"+EXPIRYDATE_3+"','MM/DD/YYYY'),'"+STATUS+"','"+PINOFFSET_3+"','"+OLDPINOFFSET_3+"',");*/
						crdinf_3.append("('"+instid+"',(select hcard_no from INST_CARD_PROCESS_HASH where ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM INST_CARD_PROCESS WHERE ORG_CHN='"+CHN_3+"')),'"+CARDTYPE_3+"','"+CUSTID_3+"','"+TXNGROUPID_3+"','"+LIMITFLAG_3+"',TO_DATE('"+EXPIRYDATE_3+"','MM/DD/YYYY'),'"+STATUS+"','"+PINOFFSET_3+"','"+OLDPINOFFSET_3+"',");
						crdinf_3.append("'"+TPINOFFSET_3+"','"+OLDTPINOFFSET_3+"','"+PINRETRYCOUNT_3+"','"+TPINRETRYCOUNT_3+"','"+PVKI_3+"',TO_DATE('"+LASTTXNDATE_3+"','MM/DD/YYYY'),'"+LASTTXNTIME_3+"'  ,'0' ,");
						crdinf_3.append("'"+PANSEQNO_3+"' )");
						
						enctrace("crdinf_3 for nc:::::"+crdinf_3.toString());
						
						 try{
					    		res_3 = jdbctemplate.update(crdinf_3.toString());
					    		}catch(Exception e)
					    		{
					    			trace("Exception in moving production :: 1:::::::"+e);
					    			return -1;
					    		}
		//3	
						 String acctexistqry = "SELECT COUNT(*) as cnt FROM EZACCOUNTINFO WHERE ACCOUNTNO= '"+ACCOUNTNO_2+"'";		
							String acctexist = (String) jdbctemplate.queryForObject(acctexistqry,String.class);	
		StringBuilder ezac_2 = new StringBuilder();		
							ezac_2.append("INSERT INTO EZACCOUNTINFO ");
							ezac_2.append("(INSTID, ACCOUNTNO, ACCOUNTTYPE, CURRCODE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE) ");
							ezac_2.append("VALUES ");
							/*ezac_2.append("('"+instid+"','"+ACCOUNTNO_2+"','"+ACCOUNTTYPE_2+"','"+CURRCODE_2+"','"+AVAILBAL_2+"','"+LEDGERBAL_2+"','"+LIMITFLAG_2+"','"+STATUS_2+"',");*/
							ezac_2.append("('"+instid+"','"+ACCOUNTNO_2+"','"+ACCOUNTTYPE_2+"','"+CURRCODE_2+"','"+AVAILBAL_2+"','"+LEDGERBAL_2+"','"+LIMITFLAG_2+"','"+STATUS_2+"',");
							ezac_2.append("'"+TXNGROUPID_2+"',TO_DATE('"+LASTTXNDATE_2+"','MM/DD/YYYY'),'"+LASTTXNTIME_2+"','"+BRANCHCODE_2+"','"+PRODUCTCODE_2+"' )");	
							enctrace("ezac_2::::"+ezac_2.toString());	
							
							
							 try{
								 if("0".equalsIgnoreCase(acctexist)){
									 res_2 = jdbctemplate.update(ezac_2.toString());
						    		}else{res_2 = 1;}
						    		
						    		}catch(Exception e)
						    		{
						    			trace("Exception in moving production :: 1:::::::"+e);
						    			return -1;
						    		}
							      
											      			 
		//4	
		StringBuilder authrel_1 = new StringBuilder();	
		authrel_1.append("INSERT INTO EZAUTHREL ");
		authrel_1.append("(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE,CHNFLAG) ");
		authrel_1.append("VALUES ");
		/*authrel_1.append("('"+instid+"','"+CHN_1+"','"+ACCOUNTNO_1+"','"+ACCOUNTTYPE_1+"','"+ACCOUNTFLAG_1+"','"+ACCOUNTPRIORITY_1+"','"+CURRCODE_1+"','"+ACCOUNTFLAG_1+"') ");*/
		authrel_1.append("('"+instid+"',(select hcard_no from INST_CARD_PROCESS_HASH where ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM INST_CARD_PROCESS WHERE ORG_CHN='"+CHN_3+"')),'"+ACCOUNTNO_1+"','"+ACCOUNTTYPE_1+"','"+ACCOUNTFLAG_1+"','"+ACCOUNTPRIORITY_1+"','"+CURRCODE_1+"','"+ACCOUNTFLAG_1+"') ");
        enctrace("authrel_1::::"+authrel_1.toString());
		
        try{
    		
    		res_1 = jdbctemplate.update(authrel_1.toString());
    		}catch(Exception e)
    		{
    			trace("Exception in moving production :: 1:::::::"+e);
    			return -1;
    		}
		
      //5	
              
              
		
		
		String INSTID_5="", LIMITTYPE_5="", LIMITID_5="", TXNCODE_5="", CURRCODE_5="", AMOUNT_5="", COUNT_5="", WAMOUNT_5="", WCOUNT_5="", MAMOUNT_5="", MCOUNT_5="", YAMOUNT_5="", YCOUNT_5="", LIMITDATE_5="";
		
		StringBuilder accinfo_5 = new StringBuilder();
		accinfo_5.append("SELECT ");
		//--EZACCUMINFO start
		//--INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, LIMITDATE
		accinfo_5.append("INSTID INSTID_5, CASE  LIMITTYPE ");
		accinfo_5.append("WHEN 'CDTP'  THEN 'CARD' ");  
		accinfo_5.append("WHEN 'CARD' THEN 'CARD' "); 
		accinfo_5.append("WHEN 'ACTP'  THEN 'ACCT' ");  
		accinfo_5.append("WHEN 'ACCT' THEN 'ACCT' END LIMITTYPE_5 ,"); 
		accinfo_5.append("CASE  LIMITTYPE "); 
		if(padssenable.equals("Y")){
		accinfo_5.append(" WHEN 'CDTP'  THEN '"+cardno+"'");      
		accinfo_5.append(" WHEN 'CARD' THEN '"+cardno+"' "); 
		}
		//wait i wil come
		{
		}
		accinfo_5.append(" WHEN 'ACTP'  THEN '"+ACCOUNTNO_2+"' ");  
		accinfo_5.append(" WHEN 'ACCT' THEN '"+ACCOUNTNO_2+"' END LIMITID_5 ,");  
		accinfo_5.append(" TXNCODE TXNCODE_5, CURRCODE CURRCODE_5, AMOUNT AMOUNT_5, COUNT COUNT_5, "); 
		accinfo_5.append("WAMOUNT WAMOUNT_5, WCOUNT WCOUNT_5, MAMOUNT MAMOUNT_5, MCOUNT MCOUNT_5, YAMOUNT YAMOUNT_5, YCOUNT YCOUNT_5, ");
		accinfo_5.append("(select TO_CHAR(AUTH_DATE,'DD-MON-YYYY') from LIMIT_DESC where INSTID='"+instid+"' AND LIMIT_ID='"+LIMITRECID_5+"') LIMITDATE_5 FROM LIMITINFO ");
		accinfo_5.append("WHERE INSTID='"+instid+"' AND LIMIT_RECID = '"+LIMITRECID_5+"'");
		//--EZACCUMINFO end  
		enctrace("accinfo_5:::::"+accinfo_5.toString());
		
		
		
		
		List movetoAccuminfo= jdbctemplate.queryForList(accinfo_5.toString());
		trace("movetoAccuminfo:::::::::::"+movetoAccuminfo.size()+":::::::"+movetoAccuminfo);   
		Iterator accitr = movetoAccuminfo.iterator();
		int as =0; int incCount = 0;
		while(accitr.hasNext())
		{
			System.out.println("testing :::::::::::::::::::::::1"+as++);
			incCount = incCount+1;    
			
			Map mp2 = (Map)accitr.next();  
			INSTID_5   = mp2.get("INSTID_5").toString();
			System.out.println("testing :::::::::::::::::::::::2"+as++);
			LIMITTYPE_5   = mp2.get("LIMITTYPE_5").toString();
			System.out.println("testing :::::::::::::::::::::::3"+as++);
			LIMITID_5   = mp2.get("LIMITID_5").toString();
			System.out.println("testing :::::::::::::::::::::::4"+mp2.get("LIMITID_5").toString());
			TXNCODE_5= mp2.get("TXNCODE_5").toString();;
			System.out.println("testing :::::::::::TXNCODE_5::::::::::::4"+mp2.get("TXNCODE_5").toString());
		
			CURRCODE_5   = mp2.get("CURRCODE_5").toString();;
		System.out.println("testing :::::::::::currr::::::::::"+mp2.get("CURRCODE_5").toString());
		AMOUNT_5   = mp2.get("AMOUNT_5").toString();;
		System.out.println("testing :::::::::::::::::::::::"+as++);
		COUNT_5   = mp2.get("COUNT_5").toString();
		System.out.println("testing :::::::::::::::::::::::"+as++);
		WAMOUNT_5   = mp2.get("WAMOUNT_5").toString();
		System.out.println("testing :::::::::::::::::::::::"+as++);
		WCOUNT_5   = mp2.get("WCOUNT_5").toString();
		System.out.println("testing :::::::::::::::::::::::"+as++);
		MAMOUNT_5   = mp2.get("MAMOUNT_5").toString();
		System.out.println("testing :::::::::::::::::::::::"+as++);
		MCOUNT_5   = mp2.get("MCOUNT_5").toString();
		System.out.println("testing :::::::::::::::::::::::"+as++);
		YAMOUNT_5   = mp2.get("YAMOUNT_5").toString();
		YCOUNT_5   = mp2.get("YCOUNT_5").toString();
		System.out.println("testing :::::::::::::::::::::::"+as++);
		LIMITDATE_5   = mp2.get("LIMITDATE_5").toString();
		System.out.println("testing :::::::::::::::::::::::"+as++);
    
		
				
		
		String getres_5Query = persisspro.getres_5Query(instid, LIMITTYPE_5, LIMITID_5, TXNCODE_5, CURRCODE_5, AMOUNT_5, COUNT_5, WAMOUNT_5, WCOUNT_5, MAMOUNT_5, MCOUNT_5, YAMOUNT_5, YCOUNT_5, LIMITDATE_5);

		enctrace("getres_5Query::"+getres_5Query);  
		 try{
	    		
	    		res_5 = jdbctemplate.update(getres_5Query.toString());
	    		}catch(Exception e)
	    		{
	    			trace("Exception in moving production :: 1:::::::"+e);
	    			return -1;
	    		}
		
		
		
		
		
		//AccinfiInsert = "";
		} 
		
		
		StringBuilder update_production = new StringBuilder();
		update_production.append("UPDATE CARD_PRODUCTION SET CARD_STATUS='"+cardstatus+"', STATUS_CODE='"+switchstatus+"',MKCK_STATUS = 'P' ,  ISSUE_DATE=(SYSDATE),ACTIVE_DATE=(sysdate),MAKER_ID='"+userid+"', " );
		update_production.append("MAKER_DATE=(sysdate) WHERE INST_ID='"+instid+"' and ORG_CHN='"+cardno+"'");
		
		enctrace("update query for instcard processs "+update_production);
		
		
		
		 
		
		
		
		
		
		
		StringBuilder deleteifdinstproc = new StringBuilder();
		StringBuilder deleteifdinstproc1 = new StringBuilder();
		deleteifdinstproc1.append("DELETE FROM INST_CARD_PROCESS_HASH WHERE INST_ID='"+instid+"' AND ORDER_REF_NO =(SELECT ORDER_REF_NO FROM INST_CARD_PROCESS WHERE ORG_CHN='"+cardno+"') ");
		deleteifdinstproc.append("DELETE FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND ORG_CHN='"+cardno+"' ");	
		trace("checking delete query--->"+deleteifdinstproc);
		trace("checking delete query--->"+deleteifdinstproc1);
		
		/*if(padssenable.equals("Y")){		
			deleteifdinstproc.append("AND HCARD_NO='"+cardno+"' ");
			}else{
				deleteifdinstproc.append("AND ORG_CHN='"+cardno+"' ");	
			}*/
		
		int production_insert = -1,production_insert1 = -1,upd_process=-1,del_process=-1;
		
		int del_process1=-1;
		
		
		
		
		try{
		
			
			
		production_insert = jdbctemplate.update(movetoproduction);
		production_insert1 = jdbctemplate.update(movetoproduction1);
		
		
		del_process1 = jdbctemplate.update(deleteifdinstproc1.toString());
		trace("check-->"+del_process1);
		del_process = jdbctemplate.update(deleteifdinstproc.toString());
		trace("checking delete query--->"+deleteifdinstproc1);
		
		upd_process = jdbctemplate.update(update_production.toString());
		//deleteifdinstproc = jdbctemplate.update(deletefromProcess);
		}catch(Exception e)
		{
			trace("Exception in moving production :: 2:::"+e);
			return -1;
		}
		
		
		
		enctrace("\n--------------------------------------Move to Production Query");
		//trace("####### custinfo_status : "+custinfo_status);
		trace("result :::::::::::"+res_1+res_2+res_3+res_4+res_5 +upd_process+production_insert+production_insert1+del_process+del_process1);   
		
		if(res_1 >0 && res_2 >0 && res_3 >0 && res_4 >0 && res_5 >0  && upd_process >0 && production_insert> 0 && production_insert1> 0 && del_process >0 && del_process1 >0)   
		{
			trace("PROCESS COMPLETED");
			issue_status = 1;
		}
		else
		{
			issue_status = -1;
		}	
		
		}	
		return issue_status;
	}
	
	
	public String authInstCustomer() {
		
		IfpTransObj transact = commondesc.myTranObject("AUTHCUST", txManager);
		HttpSession session = getRequest().getSession();
		String instid = comInstId(); 
		try {
			String[] chnlist = getRequest().getParameterValues("instorderrefnum"); 
			int cardcnt = 0;
			for ( int i=0; i<chnlist.length;i++){
				String chn = chnlist[i].toString();
				
				int x = cardregdao.updateAuthStatus(instid, chn, nextstatus, jdbctemplate);
				if( x < 0 ){
					session.setAttribute("preverr", "E"); 
					session.setAttribute("prevmsg", " Could not authorzie the card " );
					return "required_home";
				} 
				cardcnt++;
			}
			
			txManager.commit(transact.status); 
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", cardcnt + " Card(s)  Authorized successfully" ); 
			commondesc.printLog( "Committed successfully ");
			
		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " Error while authorize the instatnt customer reg" ); 
			trace( " Error while authorize the instatnt customer reg " + e.getMessage() );
			e.printStackTrace();
		} 
		return this.authInstCustomerHome(); 
	}
	/*public String getorgchnexist(String instid, String accountno,  JdbcTemplate jdbctemplate ) {
		String org_chn=null,processinst = null,productionavilable = null,secondaryaccount = null;
		try{
			org_chn = "SELECT CHN FROM EZAUTHREL WHERE INSTID='"+instid+"' AND ACCOUNTNO='"+accountno+"'";
			String 	hascard = (String)jdbctemplate.queryForObject(org_chn, String.class);
			enctrace("originaclchn222222222" + hascard );
			processinst = "SELECT ORG_CHN FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND HCARD_NO='"+hascard+"'";

				 secondaryaccount = (String)jdbctemplate.queryForObject(processinst, String.class);
				 enctrace("originaclchn222222222" + hascard );
				if(hascard==null||hascard.equals("")){
					return secondaryaccount ;
				}
	}catch(EmptyResultDataAccessException e){}
		//return org_chn;
		return secondaryaccount + " As Secondary Account .." ;
	}
	public String getacountinprocess(String instid, String accountno,  JdbcTemplate jdbctemplate ) {
		String org_chn=null,processinst = null,productionavilable = null,secondaryaccount = null;
		try{
			String originaclchn = "SELECT ORG_CHN FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND ACCT_NO='"+accountno+"'";
			processinst = (String)jdbctemplate.queryForObject(originaclchn, String.class);
			System.out.println("processinst" + processinst );
			
				
	}catch(EmptyResultDataAccessException e){}
		//return org_chn;
		return processinst + "Wating for Customer mapping authorization.." ;
	}*/
	
	public String findinprocess (String instid, String accountno,  JdbcTemplate jdbctemplate ) {
		String org_chn=null,processinst = null,productionavilable = null,secondaryaccount = null;
		try{
			
			/*String originaclchn = "SELECT ORG_CHN FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND ACCT_NO='"+accountno+"'";
			processinst = (String)jdbctemplate.queryForObject(originaclchn, String.class);*/
			
			
			///BY GOWTHAM-280819
			String originaclchn = "SELECT ORG_CHN FROM INST_CARD_PROCESS WHERE INST_ID=? AND"
					+ " ACCT_NO=? ";
					processinst = (String)jdbctemplate.queryForObject(originaclchn,new Object[]{instid,accountno}, String.class);
			
			
			System.out.println("processinst" + processinst );
			
				
	}catch(EmptyResultDataAccessException e){}
		//return org_chn;
		return processinst  ;
	}

	/*public static void main(String[] args){
		try{
		Client client = Client.create();
		   
		   WebResource webResource = client.resource("http://172.16.10.175:8080/InstantServiceRegister/rest/checkFunction");
		   WebResource webResource = client.resource("http://172.16.10.40:8989/RestApi/rest/crunchifyService");

		   JSONObject data=new JSONObject();
		   data.put("ACCOUNTNO", "8189968318");
		
		  // data.put("INSTID", "PMT");
		   System.out.println("income");

		   ClientResponse response = (ClientResponse) webResource.type("application/json")
		      .post(ClientResponse.class, data.toString());
		               System.out.println(response.getStatus());
		               if (response.getStatus() != 200) {
		          throw new RuntimeException("Failed : HTTP error code : "
		               + response.getStatus());
		          }
		   System.out.println("Output from Server .... \n");
		   String output = (String) response.getEntity(String.class);
		   System.out.println(output);

		    } catch (Exception e) {

		   System.out.println(e.getMessage());
		   System.out.println(e.getClass());

		    }
		}*/// end class
	
	
	
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
		
		
		//by gowtham-28019
				String qry = "SELECT CIN_NO, ORDER_REFNO FROM SEQUENCE_MASTER WHERE INST_ID=? ";
				List listoforder = jdbctemplate.queryForList(qry,new Object[]{instid});
		return listoforder;
	}
	
	
	
	public String pinOTPhome() { 
		
		return "pinotphome"; 
	}
	
	
	public String otpcardslistrepinAndActive() throws Exception{
		HttpSession session = getRequest().getSession();
		String instid = comInstId();
		System.out.println("instid-->"+instid);
		String branchid=comBranchId();
		String otptype =  getRequest().getParameter("otptype");
		System.out.println("otpyype"+otptype);
		List waitingforotp=null;
		
		if (  getRequest().getParameter("cardno") != "" ){
			System.out.println("1111"+getRequest().getParameter("cardno") );
			PadssSecurity padsssec = new PadssSecurity();
			String ecardno="";
			StringBuffer newhashcard = new StringBuffer();
			//String keyid ="BIC";
			String keyid ="2";
			System.out.println("keyid::"+keyid);
			List secList = commondesc.getPADSSDetailById(keyid,jdbctemplate);
			//System.out.println("secList::"+secList);  
			
			Properties props=getCommonDescProperty();
			String EDPK=props.getProperty("EDPK");
			
			Iterator secitr = secList.iterator();
			if(!secList.isEmpty()){
				while(secitr.hasNext())
				{
					Map map = (Map) secitr.next(); 
					String CDMK = ((String)map.get("DMK"));
					//String eDPK = ((String)map.get("DPK"));
					
					String CDPK=padsssec.decryptDPK(CDMK, EDPK);
					
					newhashcard = padsssec.getHashedValue(getRequest().getParameter("cardno")+instid);
					System.out.println("newhashcard-->"+newhashcard);
					ecardno = padsssec.getECHN( CDPK, getRequest().getParameter("cardno").toString());
					System.out.println("ecardno-->"+ecardno);
					
				}      
			}
			
			if(otptype.equalsIgnoreCase("RO")){
				 System.out.println("income for card RO");
				 waitingforotp = cardregdao.otpcardslist(instid,ecardno.toString(), jdbctemplate );
		        trace("waitingRO"+waitingforotp);
				if( waitingforotp.isEmpty() ){
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " No records found for custauth" ); 
					 return "pinotphome";
				} 
				
			}
			else if (otptype.equalsIgnoreCase("NO")){
				 System.out.println("income forcard NO");
				 waitingforotp = cardregdao.otpcardshcardnolist(instid,ecardno.toString(), newhashcard.toString(),jdbctemplate );
		        trace("waitingNO"+waitingforotp);
				if( waitingforotp.isEmpty() ){
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " No records found for custauth" ); 
					 return "pinotphome";
				} 
			}
			System.out.println("cardno---->"+cardno);
			System.out.println("waitingforotp----->"+waitingforotp);
			cardregbean.setCardno(getRequest().getParameter("cardno"));
			 cardregbean.setInstorderlist(waitingforotp);  
		}
	
		else{
               if(otptype.equalsIgnoreCase("RO")){
            	   System.out.println("income for RO");
            	   waitingforotp = cardregdao.otpforrepincardslist(instid, jdbctemplate );
			        trace("waitingROLIST"+waitingforotp);
					if( waitingforotp.isEmpty() ){
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", " No records found for custauth" ); 
						 return "pinotphome";
					} 
				
			   }
			   else if (otptype.equalsIgnoreCase("NO")){
				   System.out.println("income for NO");
				   waitingforotp = cardregdao.otpcardsfornotactivehcardnolist(instid, jdbctemplate );
			        trace("waitingforcardpin"+waitingforotp);
					if( waitingforotp.isEmpty() ){
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", " No records found for custauth" ); 
						 return "pinotphome";
					} 
				
			   }
						
				       
		
               cardregbean.setInstorderlist(waitingforotp);  
		}

		
		return "pinotphome";
 
		
	}
	public synchronized String  confrimotpchangestatus() throws Exception{
		trace( "*****confrimotpchangestatus*****");enctrace( "*****confrimotpchangestatus*****");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("CONFIROTP", txManager);
		String instid = comInstId();
		//String newhashcard="";
		StringBuffer hcardno = null;
		//String hcardno="";
		StringBuffer newhashcard = new StringBuffer();
		String userid =comUserCode();
		String username = comUsername1();
		PinGenerationDAO pin = new PinGenerationDAO();
		HsmTcpIp hsm = new HsmTcpIp();
		String orgchn[] = getRequest().getParameterValues("orgchn");
		
		int cardcardlength = orgchn.length;
		System.out.println("cardcardlength---->"+cardcardlength);
		int smssentcount=0,successcount=0,failure=0;
		
		System.out.println(cardcardlength);
		
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		String keyid = "",dcardno = "", CDMK = "", CDPK = "" ;
		
		Properties props=getCommonDescProperty();
		String EDPK=props.getProperty("EDPK");
		
		PadssSecurity padsssec = new PadssSecurity();
		if(padssenable.equals("Y"))
		{

			keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			System.out.println("keyid::"+keyid);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			//System.out.println("secList::"+secList);  
			Iterator secitr = secList.iterator();
			if(!secList.isEmpty()){
				while(secitr.hasNext())
				{
					Map map = (Map) secitr.next(); 
					CDMK = ((String)map.get("DMK"));
					//eDPK = ((String)map.get("DPK"));
					CDPK=padsssec.decryptDPK(CDMK, EDPK);
				}      
				}
		} 
		
		for(int i=0;i<cardcardlength;i++)
		{
			String cardonebyon=orgchn[i];

			try {
				hcardno = new StringBuffer();
				String mobilno="",CHN="",bin="",card_type="",cin="",mcardno="",cafstatus="";
				
				/*String query="SELECT CARD_NO,MOBILENO,BIN,CARD_TYPE_ID,CIN,MCARD_NO,CAF_REC_STATUS FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"'AND HCARD_NO='"+cardonebyon+"' ";
				System.out.println(query);
				List result=jdbctemplate.queryForList(query);*/
				
				
				///by gowtham-270819
				String query="SELECT ORG_CHN,MOBILENO,BIN,CARD_TYPE_ID,CIN,MCARD_NO,CAF_REC_STATUS FROM CARD_PRODUCTION "
						+ "WHERE INST_ID=?AND ORG_CHN=? ";
						System.out.println(query);
						List result=jdbctemplate.queryForList(query,new Object[]{instid,cardonebyon});
				
				Iterator secitr = result.iterator();
				if(result.isEmpty()){
					addActionError("NO records found");
					trace("NO records found for OTP");
					return "pinotphome"; 
				}
				if(!result.isEmpty()){
					while(secitr.hasNext())
					{
						Map map = (Map) secitr.next(); 
						 mobilno = ((String)map.get("MOBILENO"));
						 CHN = ((String)map.get("ORG_CHN"));
						 System.out.println("CHN--->"+CHN);
						 bin = ((String)map.get("BIN"));
						 cin = ((String)map.get("CIN"));
						 card_type = ((String)map.get("CARD_TYPE_ID"));
						 mcardno = ((String)map.get("MCARD_NO"));
						 cafstatus = ((String)map.get("CAF_REC_STATUS"));
					}
					
					if(padssenable.equals("Y"))
					{
						System.out.println("instid-->"+instid);
					dcardno = padsssec.getCHN( CDPK, CHN);
					System.out.println("dcardno--->"+dcardno);
					//newhashcard = padsssec.getHashedValue(getRequest().getParameter("dcardno")+instid);
					hcardno = padsssec.getHashedValue(dcardno + instid);
					System.out.println("newhashcard--->"+hcardno);
					}
				
					System.out.println("mobilno"+mobilno+" \n ");
					//String response=hsm.OTPGeneration( instid, ORG_CHN, cardonebyon, mobilno,  jdbctemplate );
					
					HSMParameter hsmobj;
					Socket connect_id = null;  
					DataOutputStream out = null;
					DataInputStream in = null;
					PinGenerationDAO pindao = new PinGenerationDAO();
					trace( "Getting the security attributes....");
					hsmobj = pindao.gettingBin_details(bin,instid, jdbctemplate);
					if( hsmobj == null ){
						trace( "No HSM Properties found for the bin " + bin);
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg",  "No HSM Properties found for the bin " + bin ); 
					}
					
					connect_id = pindao.ConnectingHSM(hsmobj.HSMADDRESS,hsmobj.HSMPORT,hsmobj.HSMTIMEOUT);
					if ( connect_id != null ) {		
						trace("Connection socket object ... : "+connect_id);
						in	= new DataInputStream (new BufferedInputStream(connect_id.getInputStream()));
						out = new DataOutputStream (new BufferedOutputStream(connect_id.getOutputStream()));  
					}else{				 
						trace("Could not connect hsm");	
						addActionError("Could not connect hsm ");
						return "pinotphome";
					}
					
				//	String mcardno="";
					
					String pinoffset = hsm.printRacalPin(instid,dcardno,mcardno, cin, card_type, bin, "CARD_PRODUCTION",  in, out, jdbctemplate, hsmobj,CHN);
					//String pinoffset = hsm.printRacalPin(instid,dcardno, mcardno, cin, card_type, bin, "CARD_PRODUCTION",  in, out, jdbctemplate, hsmobj,CHN);
					trace("Generated pin command...got : " + pinoffset);
					
					String output[] = pinoffset.split("~");
					String pinoff = output[0];
					String encpinblock = output[1];
					String clearpin = output[2];
					//String clearpinresp = clearpin.substring(6, 8);
					String clearpinresp = "00";
					if(clearpinresp.equals("00")){
						//clearpin = clearpin.substring(8, 12);
						clearpin="1234";
					}else{
						addActionError("Unable to send OTP,HSM is not in Authorized State");
						trace("Bad Response from HSM---> " +clearpin);
						return "pinotphome"; 
					}
					Properties prop = getCommonDescProperty();
		/*		String server = prop.getProperty("con.server");
					String port =prop.getProperty("con.port");
					String Username =prop.getProperty("con.username");
					String password =prop.getProperty("con.password");
					String dlr =prop.getProperty("con.dlr");
					String type=prop.getProperty("con.type");
					String source =prop.getProperty("con.source");*/
					String api_key =prop.getProperty("con.api_key");
					String sender_id =prop.getProperty("con.sender_id");
				
					 mobilno =mobilno;
					 
				 	 /*smslog("Values Reading from property fil server -->"  +server +" ,port -->" + port + ","
					 		+ "username -->" +username +",password -->"+password+" ,api_key -->"+api_key+" ,sender_id -->"+sender_id) ;
					 */
					 
					 smslog("Values Reading from property fil  api_key -->"+api_key+" ,sender_id -->"+sender_id) ;
					 
					 String	 message ="Dear Customer, Welcome to SIBL Bank, "+clearpin+" is the PIN for your card "+mcardno+". Please do not share with anyone.";
					
					//String urlstr="http://api.nalosolutions.com/bulksms/?username="+Username+"&password="+password+"&type="+type+"&dlr="+dlr+"&destination=233275116346&source="+source+"&message="+message+"";
 //String urlstr="https://apps.txtmategh.com/sms/api?action=send-sms&api_key="+api_key+"&to="+mobilno+"&from="+sender_id+"&sms="+message;
 //smslog("response from sms vendor -->"+urlstr);
		
		
 //URL url=https://apps.txtmategh.com/sms/api?action=sendsms& api_key=XXXXXXX&to=PhoneNumber&from=SenderID&sms=YourMessage
 //SAMPLE: https://apps.txtmategh.com/sms/apiaction=sendsms&api_key=PWl4cHdsSkl5eXhsT3V1RU5jdEg=&to=23275845005&from=SIBLL&sms=GOWTHAM
  //sms API key= PWl4cHdsSkl5eXhsT3V1RU5jdEg=
					 String charset = "UTF-8";
					String data="";
					
					//JSONObject js = new JSONObject();
					 try {		     
						 JSONObject js = new JSONObject();
						 //JSONObject  smsresp1=	Smscallobj.smscall(mobilno,message);
						 
						 
						 //String smsresp=	Smscallobj.uploadToServer(mobilno,message);
					
					  /*URLConnection connection = null;
				      BufferedReader reader = null;
				      StringBuffer stringBuilder = new StringBuffer();
				      InputStreamReader in1 = null;
				      trace ("urlstr-->"+urlstr);
				      connection = new URL(urlstr).openConnection();
			          connection.setDoOutput(true);
			          in1 = new InputStreamReader(connection.getInputStream(), 
			          Charset.defaultCharset());
			          reader = new BufferedReader(in1);
			          
			          String line = null;
			          String smsresp =null;
			          while ((line = reader.readLine()) != null)
			          {
			            stringBuilder.append(line);
			          }
			          System.out.println("output from the required string" + stringBuilder.toString());
			          data = stringBuilder.toString();
			          trace("Sms Repsonse --->"+data);
			          smslog("Sms Repsonse --->"+data);
				     
			          //data = data.replace('|', ',');
			          System.out.println("after replacing the pipe symbol value we can get as" + data);*/
			          
			          String smsresp="";
			          String[] datas = smsresp.split("\\|");
			          
			          String Response= datas[0];
			          String Mobile_Number=datas[1];
			          String MessageId=datas[2];
			          
			         System.out.println("getting json values " + js.toString());
					
			         if(Response.contains("ok"))
			         {
			        	 smsresp="SUCCESS";
			        	 trace ("Successfully Send");
			         }
			         else if (Response.equals("100")){
			        	 smsresp ="Bad gateway requested";
			         }
			         else if(Response.equals("101")){
			        	 smsresp="Wrong Action";
			         }
			         else if(Response.equals("102")){
			        	 smsresp="Authentication Failed.";
			         }
			         else if(Response.equals("103")){
			        	 smsresp="Invalid Phone Number on your list.";}
			        	
			         else if(Response.equals("104")){
			        	 smsresp="Subscription list not found'";}
			        	
			         else if(Response.equals("105")){
			        	 smsresp="Insufficient balance or invalid coverage.";}
			        	
			         else if(Response.equals("106")){
			        	 smsresp="Invalid Sender id.";}
			        	
			         else if(Response.equals("107")){
			        	 smsresp="Invalid SMS Type.";}
			        	
			         else if(Response.equals("108")){
			        	 smsresp="SMS Gateway not active.";}
			        	
			         else if(Response.equals("109")){
			        	 smsresp="Invalid Schedule Time.";}
			         
			         else if(Response.equals("110")){
			        	 smsresp="Media url required.";}
			         
			         else if(Response.equals("111")){
			        	 smsresp="SMS contain spam word.";}
			         
			         else if(Response.equals("112")){
			        	 smsresp="Destination number contain in blacklist number.";}
			         
			         else {
			        	 smsresp="Insufficient Credit Reseller";}
			         
//					String smsresp="00~SUCCESS";
			         
					//*************************
				if(smsresp.contains("SUCCESS")){
						String updateqry = "",updqry = "";
						if("A".equals(cafstatus)){
							updateqry = "UPDATE CARD_PRODUCTION SET OTPSTATUS='S',PIN_OFFSET='"+pinoff+"',PIN_DATE=sysdate,CARD_STATUS='05',STATUS_CODE='53',MAKER_DATE=sysdate,MAKER_ID='"+userid+"' WHERE INST_ID='"+instid+"' AND ORG_CHN='"+cardonebyon+"'";
							updqry = "UPDATE EZCARDINFO SET STATUS='53',PINOFFSET='"+pinoff+"',OTPGEN_DATE=SYSDATE WHERE INSTID='"+instid+"' AND CHN='"+hcardno+"'";
						}else if("R".equals(cafstatus)){
							updateqry = "UPDATE EZCARDINFO SET STATUS='53',PINOFFSET='"+pinoff+"',PINRETRYCOUNT='0',OTP_INTVL='0',OTPGEN_DATE=SYSDATE WHERE INSTID='"+instid+"' AND CHN='"+cardonebyon+"'";
							updqry = "UPDATE CARD_PRODUCTION SET OTPSTATUS='S',PIN_OFFSET='"+pinoff+"',STATUS_CODE='53',CARD_STATUS='05',PIN_DATE=sysdate,CAF_REC_STATUS='A' WHERE INST_ID='"+instid+"' AND ORG_CHN='"+hcardno+"'";
						}else{
							updateqry = "UPDATE CARD_PRODUCTION SET OTPSTATUS='S',PIN_OFFSET='"+pinoff+"',PIN_DATE=sysdate,CARD_STATUS='05',STATUS_CODE='53',MAKER_DATE=sysdate,MAKER_ID='"+userid+"' WHERE INST_ID='"+instid+"' AND ORG_CHN='"+cardonebyon+"'";
							updqry = "UPDATE EZCARDINFO SET STATUS='53',PINOFFSET='"+pinoff+"',OTPGEN_DATE=SYSDATE WHERE INSTID='"+instid+"' AND CHN='"+hcardno+"'";
						}
						trace("updqry-->"+updqry);
						trace("updateqry-->"+updateqry);
						jdbctemplate.execute(updateqry);
						jdbctemplate.execute(updqry);
						txManager.commit(transact.status);
						addActionMessage( "OTP sent successfully to the customer for the card [ "+mcardno+" ] and card is activated "  );
						trace( "OTP sent successfully to the customer for the card [ "+mcardno+" ] "  );
					}
					else{
						txManager.rollback(transact.status);
						trace("  OTP failure rollbak");
						//addActionError("OTP failure ---"+smsrespsplit[2]);
						addActionError("OTP failure ---" +smsresp);
					}

			       //OTP end
				
					 }
					 catch (Exception e)
					    {
					      session.setAttribute("preverr", "E");
					      session.setAttribute("prevmsg", "Exception : Could not able to generate PIN...");
					      e.printStackTrace();
					      trace("Exception : while generating the PIN  ... " + e.getMessage());
					    }	}
			
			 }catch (Exception e) {
				txManager.rollback(transact.status);
				e.printStackTrace();
				addActionError("OTP generated , but card is not activated");
			}
		
		}
		return "pinotphome";
	
				}
}
/*
public synchronized String  confrimotpchangestatus() throws Exception{
	trace( "*****confrimotpchangestatus*****");enctrace( "*****confrimotpchangestatus*****");
	HttpSession session = getRequest().getSession();
	IfpTransObj transact = commondesc.myTranObject("CONFIROTP", txManager);
	String instid = comInstId();
	String userid =comUserCode();
	String username = comUsername1();
	PinGenerationDAO pin = new PinGenerationDAO();
	HsmTcpIp hsm = new HsmTcpIp();
	String Hashchn[] = getRequest().getParameterValues("hcardno");
	
	int cardcardlength = Hashchn.length;
	System.out.println("cardcardlength---->"+cardcardlength);
	int smssentcount=0,successcount=0,failure=0;
	
	System.out.println(cardcardlength);
	
	String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
	String keyid = "",dcardno = "", CDMK = "", CDPK = "";
	
	Properties props=getCommonDescProperty();
	String EDPK=props.getProperty("EDPK");
	
	PadssSecurity padsssec = new PadssSecurity();
	if(padssenable.equals("Y"))
	{
		
		keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
		System.out.println("keyid::"+keyid);
		List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
		//System.out.println("secList::"+secList);  
		Iterator secitr = secList.iterator();
		if(!secList.isEmpty()){
			while(secitr.hasNext())
			{
				Map map = (Map) secitr.next(); 
				CDMK = ((String)map.get("DMK"));
				//eDPK = ((String)map.get("DPK"));
				CDPK=padsssec.decryptDPK(CDMK, EDPK);
			}      
			}
	} 
	
	for(int i=0;i<cardcardlength;i++)
	{
		String cardonebyon=Hashchn[i];

		try {
			String mobilno="",CHN="",bin="",card_type="",cin="",mcardno="",cafstatus="";
			
			String query="SELECT CARD_NO,MOBILENO,BIN,CARD_TYPE_ID,CIN,MCARD_NO,CAF_REC_STATUS FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"'AND HCARD_NO='"+cardonebyon+"' ";
			System.out.println(query);
			List result=jdbctemplate.queryForList(query);
			
			
			///by gowtham-270819
			String query="SELECT CARD_NO,MOBILENO,BIN,CARD_TYPE_ID,CIN,MCARD_NO,CAF_REC_STATUS FROM CARD_PRODUCTION "
					+ "WHERE INST_ID=?AND HCARD_NO=? ";
					System.out.println(query);
					List result=jdbctemplate.queryForList(query,new Object[]{instid,cardonebyon});
			
			Iterator secitr = result.iterator();
			if(result.isEmpty()){
				addActionError("NO records found");
				trace("NO records found for OTP");
				return "pinotphome"; 
			}
			if(!result.isEmpty()){
				while(secitr.hasNext())
				{
					Map map = (Map) secitr.next(); 
					 mobilno = ((String)map.get("MOBILENO"));
					 CHN = ((String)map.get("CARD_NO"));
					 bin = ((String)map.get("BIN"));
					 cin = ((String)map.get("CIN"));
					 card_type = ((String)map.get("CARD_TYPE_ID"));
					 mcardno = ((String)map.get("MCARD_NO"));
					 cafstatus = ((String)map.get("CAF_REC_STATUS"));
				}
				
				if(padssenable.equals("Y"))
				{
				dcardno = padsssec.getCHN( CDPK, CHN);        
				}
			
				System.out.println("mobilno"+mobilno+" \n ");
				//String response=hsm.OTPGeneration( instid, ORG_CHN, cardonebyon, mobilno,  jdbctemplate );
				
				HSMParameter hsmobj;
				Socket connect_id = null;  
				DataOutputStream out = null;
				DataInputStream in = null;
				PinGenerationDAO pindao = new PinGenerationDAO();
				trace( "Getting the security attributes....");
				hsmobj = pindao.gettingBin_details(bin,instid, jdbctemplate);
				if( hsmobj == null ){
					trace( "No HSM Properties found for the bin " + bin);
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg",  "No HSM Properties found for the bin " + bin ); 
				}
				
				connect_id = pindao.ConnectingHSM(hsmobj.HSMADDRESS,hsmobj.HSMPORT,hsmobj.HSMTIMEOUT);
				if ( connect_id != null ) {		
					trace("Connection socket object ... : "+connect_id);
					in	= new DataInputStream (new BufferedInputStream(connect_id.getInputStream()));
					out = new DataOutputStream (new BufferedOutputStream(connect_id.getOutputStream()));  
				}else{				 
					trace("Could not connect hsm");	 
					//////this.removeBlockedUser(instid, branchcode, jdbctemplate, delstatus);
				}
				
			//	String mcardno="";
				
				String pinoffset = hsm.printRacalPin(instid,dcardno,mcardno, cin, card_type, bin, "INSTANT",  in, out, jdbctemplate, hsmobj,CHN); 
				trace("Generated pin command...got : " + pinoffset);
				
				String output[] = pinoffset.split("~");
				String pinoff = output[0];
				String encpinblock = output[1];
				String clearpin = output[2];
				String clearpinresp = clearpin.substring(6, 8);
				if(clearpinresp.equals("00")){
					clearpin = clearpin.substring(8, 14);
				}else{
					addActionError("Unable to send OTP,HSM is not in Authorized State");
					trace("Bad Response from HSM---> " +clearpin);
					return "pinotphome"; 
				}
				
				//OTP start
				String otpmsg = "Dear Customer, Welcome to BIC Bank, "+clearpin+" is the PIN for your card "+mcardno+". Please do not share with anyone.";
				otpmsg = otpmsg.replaceAll(" ", "%20");
				System.out.println(otpmsg);
				String urlStr="http://10.10.5.5:8002/bic_sms/sendSMSLink.do?mobileno="+mobilno+"&message="+otpmsg+"&type=M";
				String smsresp = "";
				URL url=new URL(urlStr);
				java.net.URLConnection urlCon=url.openConnection();
				BufferedReader br=new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
				InputStream fileCon=null;
				synchronized(fileCon=urlCon.getInputStream())
				{
					String msg = "";
					br=new BufferedReader(new InputStreamReader(fileCon));								
					while((msg=br.readLine()) != null)
					{
						smsresp+=msg.trim();
						
					}
			
				}
				System.out.println(smsresp);
				String smsrespsplit[] = smsresp.split("~");
				if(smsresp.contains("SUCCESS")){
					String updateqry = "",updqry = "";
					if("A".equals(cafstatus)){
						updateqry = "UPDATE CARD_PRODUCTION SET OTPSTATUS='S',PIN_OFFSET='"+pinoff+"',PIN_DATE=sysdate,CARD_STATUS='05',STATUS_CODE='53',MAKER_DATE=sysdate,MAKER_ID='"+userid+"' WHERE INST_ID='"+instid+"' AND HCARD_NO='"+cardonebyon+"'";
						updqry = "UPDATE EZCARDINFO SET STATUS='53',PINOFFSET='"+pinoff+"',OTPGEN_DATE=SYSDATE WHERE INSTID='"+instid+"' AND CHN='"+cardonebyon+"'";
					}else if("R".equals(cafstatus)){
						updateqry = "UPDATE EZCARDINFO SET STATUS='53',PINOFFSET='"+pinoff+"',PINRETRYCOUNT='0',OTP_INTVL='0',OTPGEN_DATE=SYSDATE WHERE INSTID='"+instid+"' AND CHN='"+cardonebyon+"'";
						updqry = "UPDATE CARD_PRODUCTION SET OTPSTATUS='S',PIN_OFFSET='"+pinoff+"',STATUS_CODE='53',CARD_STATUS='05',PIN_DATE=sysdate,CAF_REC_STATUS='A' WHERE INST_ID='"+instid+"' AND HCARD_NO='"+cardonebyon+"'";
					}else{
						updateqry = "UPDATE CARD_PRODUCTION SET OTPSTATUS='S',PIN_OFFSET='"+pinoff+"',PIN_DATE=sysdate,CARD_STATUS='05',STATUS_CODE='53',MAKER_DATE=sysdate,MAKER_ID='"+userid+"' WHERE INST_ID='"+instid+"' AND HCARD_NO='"+cardonebyon+"'";
						updqry = "UPDATE EZCARDINFO SET STATUS='53',PINOFFSET='"+pinoff+"',OTPGEN_DATE=SYSDATE WHERE INSTID='"+instid+"' AND CHN='"+cardonebyon+"'";
					}
					trace("updqry-->"+updqry);
					trace("updateqry-->"+updateqry);
					jdbctemplate.execute(updateqry);
					jdbctemplate.execute(updqry);
					txManager.commit(transact.status);
					addActionMessage( "OTP sent successfully to the customer for the card [ "+mcardno+" ] and card is activated "  );
					trace( "OTP sent successfully to the customer for the card [ "+mcardno+" ] "  );
				}
				else{
					txManager.rollback(transact.status);
					trace("  OTP failure rollbak");
					addActionError("OTP failure ---"+smsrespsplit[2]);
				}

		       //OTP end
			
			}

		} 
		
		catch (Exception e) {
			txManager.rollback(transact.status);
			e.printStackTrace();
			addActionError("OTP generated , but card is not activated");
		}
	}
	
	return "pinotphome";

}


}*/