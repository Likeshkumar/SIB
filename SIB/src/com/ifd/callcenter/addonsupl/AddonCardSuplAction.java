package com.ifd.callcenter.addonsupl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.personalize.Personalizeorderdetails;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

import test.Date;
import test.Validation;

public class AddonCardSuplAction extends BaseAction{
	
	
	/**
	 * 
	 * kumar
	 */
	
	
	
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	public List getProductlist() {
		return productlist;
	}

	public void setProductlist(List productlist) {
		this.productlist = productlist;
	}

	CommonDesc commondesc = new CommonDesc();
	AuditBeans auditbean = new AuditBeans();
	
	private List productlist;
	private List carddetlist;
	private String padssenabled;
	
	
	private String cardnumber;
	private String productcode;
	
	List cardcollectbranchlist;
	private String collectbranch;
	
	public List getCardcollectbranchlist() {
		return cardcollectbranchlist;
	}

	public void setCardcollectbranchlist(List cardcollectbranchlist) {
		this.cardcollectbranchlist = cardcollectbranchlist;
	}
	
	public String getCollectbranch() {
		return collectbranch;
	}

	public void setCollectbranch(String collectbranch) {
		this.collectbranch = collectbranch;
	}
	public String getCardnumber() {
		return cardnumber;
	}

	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}

	public String getProductcode() {
		return productcode;
	}

	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}

	String TABLENAME="";
	
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
	
	private static final long serialVersionUID = 1L;
	AddonCardSuplActionDAO dao = new AddonCardSuplActionDAO();

	public String addonSuplHome()
	{
		trace("addonSuplHome method called.....");
		return "addonSuplHome";
	}
	
	public void validate(){
		HttpSession session = getRequest().getSession();
		String apptype = (String)session.getAttribute("APPLICATIONTYPE");
		if( apptype != null && apptype.equals("CREDIT")){
			TABLENAME = " IFC_CARD_PRODUCTION ";
		}else if( apptype != null && apptype.equals("DEBIT")){
			TABLENAME = " CARD_PRODUCTION ";
		}else{
			TABLENAME = " CARD_PRODUCTION ";
		}
	}
	
	public String addonCardSuplAction() throws Exception
	{
		trace("addonCardAction method called.....");
		
		HttpSession session = getRequest().getSession();
		
		String sessioncsrftoken = (String) session.getAttribute("token");
		System.out.println("Global session token---> " + sessioncsrftoken);
		String jspcsrftoken = getRequest().getParameter("token");
		System.out.println("addonCardSuplAction() method   token---->    " + jspcsrftoken);
		if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) {
			session.setAttribute("message", "CSRF Token Mismatch");
			/* addActionError("CSRF Token Mismatch"); */
			return "invaliduser";
		}

		
		boolean check;
		int checkvalue;
		String instid = comInstId();
		String cardno = getRequest().getParameter("cardnum");
		//System.out.println(cardno);
		
		checkvalue=Validation.number(cardno);
		if(checkvalue==0)
		{
			addActionError("Enter Proper Card Number");
			return addonSuplHome();
		}
		
		
		StringBuilder sb=null;		
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		
		String keyid = "";
		String EDMK="", EDPK="";
		StringBuffer hcardno = new StringBuffer();
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
					String eDMK = ((String)map.get("DMK"));
					String eDPK = ((String)map.get("DPK"));
					hcardno = padsssec.getHashedValue(cardno+instid);
				}      
				}
		} 
		    
		int checkvalid =-1;   
		String productCodeByCard = "";
		
	//	if(padssenable.equals("Y"))
	//	{
		
			checkvalid = dao.validateActvatedCard(instid,padssenable, hcardno.toString(), jdbctemplate);
			//setCardnumber(hcardno.toString());
			try
			{
				productCodeByCard = dao.getProductByCHN(instid, hcardno.toString(), jdbctemplate, padssenable);
				}
			
			catch(Exception e)
				{
					trace("exception getting productCodeByCard ::"+productCodeByCard);
					
				}
			
			//BY SIVA
			
	//	}
		/*else
		{
			checkvalid = dao.validateActvatedCard(instid,padssenable, cardno, jdbctemplate);
			//setCardnumber(cardno);
			try{
				productCodeByCard = dao.getProductByCHN(instid, cardno, jdbctemplate, padssenable);
				}catch(Exception e)
				{
					trace("exception getting productCodeByCard ::"+e);
					
				}addSuplCardDetails
		}   */
		
		//trace("got cardno: "+cardno+"got productcode::"+productCodeByCard);		
		setCardnumber(cardno);
		cardcollectbranchlist = commondesc.generateBranchList(instid, jdbctemplate);
		
		
		
		//System.out.println(cardno);
		
		cardno="0000000000000000";
		sb=new StringBuilder(cardno);
		sb.setLength(0);
		
		cardno=null;
		sb=null;
		
		//System.out.println(cardno);
		
		if(checkvalid>0)
		{
			
			return addonSuplProduct(productCodeByCard);
		}
		else   
		{
			addActionError("Entered Card no Not Activated ...Enter Valid Card No.");
		}
		
	
		
		return "addonCardSuplAction";
		
	}
	
	//getting subproduct List  
		public void getSubProductListByProduct() throws Exception {    
			
			trace("**************getsubproduct method entered********************");
			HttpSession session = getRequest().getSession();
			String instid = (String)session.getAttribute("Instname");
			String productcode = getRequest().getParameter("productcode");
			String status = getRequest().getParameter("status");
			System.out.println("productcode:"+productcode);
			System.out.println("status:"+status);
			//System.out.println("status:"+status);
			System.out.println("instid:"+instid);        
			String suproductselect = null;
			try{
				suproductselect = commondesc.getListOfSubProduct(instid, productcode, status, jdbctemplate);
				//trace("Getting subproduct list for the product code [ "+productcode+" ] ...got " + suproductselect );
			}catch(Exception e ){
				e.printStackTrace(); 
				trace("Exception...." + e.getMessage());
				suproductselect = "<option value='-1'>Unable to get sub product list </option>";
			}
			
			getResponse().getWriter().write(suproductselect);
		}
	
	public String addSuplCardDetails() throws Exception
	{
		StringBuilder sb=null;
	
		String instid = comInstId();
		String cardno =getRequest().getParameter("cardnumber");
		//System.out.println(cardno);
		String productCode =getRequest().getParameter("productcode");
		String subproduct =getRequest().getParameter("subproduct");
		
		System.out.println("productcode+"+productCode+"subproduct-"+subproduct);
		
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		collectbranch = getRequest().getParameter("collectbranch");
		String keyid = "";
		String EDMK="", EDPK="";
		StringBuffer hcardno = new StringBuffer();
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
					String eDMK = ((String)map.get("DMK"));
					String eDPK = ((String)map.get("DPK"));
					hcardno = padsssec.getHashedValue(cardno+instid);
				}      
				}
			
			
			
			
			cardno="0000000000000000";
			sb=new StringBuilder(cardno);
			sb.setLength(0);
			
			cardno=null;
			sb=null;
		} 
		
		
		
		List carlist = null;
		String binno = commondesc.getBin(instid, productCode, jdbctemplate);
		
		/*if(padssenable.equals("Y"))
		{*/
		/*carlist = dao.getcarddettailForSuplimentary(instid,padssenable,productCode,subproduct,binno, hcardno.toString(),jdbctemplate);*/
		carlist = dao.getcarddettailForSuplimentary(instid,padssenable,productCode,subproduct,binno, hcardno.toString(),jdbctemplate);
		
		/*}else
		{
		carlist = dao.getcarddettailForSuplimentary(instid,padssenable,productCode,subproduct,binno, cardno,jdbctemplate);	
		}*/
		
		
		
		if(carlist.isEmpty())
		{
			addActionError("Not get Card List");
		}
		setCarddetlist(carlist);
		//trace("carlist::::"+carlist);
		trace("carlist::::");
		setPadssenabled(padssenable);
		
		
		//System.out.println(cardno+sb);
		
		return "addonSuplProduct";
	}
	
	
	public String addonSuplProduct(String productcode)
	{
		String prodcode = null;
		HttpSession session = getRequest().getSession();
		try{
		String instid = (String)session.getAttribute("Instname");
		//setInstid(i_Name);
		List cardtype_productlist=null;;
	
		try{
		cardtype_productlist =commondesc.getProductListViewForSupl(instid,productcode, jdbctemplate,session) ;
		}catch(Exception e)
		{
			trace("Exception in getting cardtype_productlist"+e);
		}
		trace("prodlist==>" + cardtype_productlist.size());
		if((cardtype_productlist.isEmpty()))  
		{
			addActionError("No Product Exists ");
			trace("No Suplimentary Product Exists ");
			return "required_home";
			 
		}
		else
		{  
			setProductlist(cardtype_productlist);
		}
			
		}
		catch (Exception e) 
		{
			session.setAttribute("dispsubprod_localMessage", " Error While Getting the Product Details For Adding Sub-Product ");
			session.setAttribute("dispsubprod_localError", "E");
			trace( "Exception : Error While Getting the Product Details For Adding Sub-Product " + e.getMessage());
			e.printStackTrace();
			trace("\n\n"); 
			enctrace("\n\n");
		}
		trace("\n\n"); 
		enctrace("\n\n");
		
		return "addonSuplProduct";
	}
	
	
	public String generateAddoncardSuplimentAction() throws Exception
	{
		
		//added by gowtham_250719
		HttpSession session = getRequest().getSession();
		
		String sessioncsrftoken = (String) session.getAttribute("token");
		System.out.println("Global session token---> " + sessioncsrftoken);
		String jspcsrftoken = getRequest().getParameter("token");
		System.out.println("generateAddonaccountAction() method   token---->    " + jspcsrftoken);
		if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) {
			session.setAttribute("message", "CSRF Token Mismatch");
			/* addActionError("CSRF Token Mismatch"); */
			return "invaliduser";
		}
		
		trace("generateAddoncardSuplimentAction method called....");
		
		//Date date=new Date();
		StringBuilder sb=null;
		String newcardno;
		
		System.out.println( "card status changing as reissue");
		
		
		//added by gowtham_250719
		String  ip=(String) session.getAttribute("REMOTE_IP");
		Date date =  new Date();
		
		try
		{
			List newprores=null;
		String instid = comInstId();
		String cardno = getRequest().getParameter("hcard");
		//trace("getting card number"+cardno);
		
		String cardno1 = getRequest().getParameter("card");
	//	trace("getting card number"+cardno1);
		
		
		String maskedcardno = getRequest().getParameter("mcardnumber");
		trace("getting mcard number"+maskedcardno);
		collectbranch = getRequest().getParameter("collectbranch");
		//System.out.println("cardno:::"+cardno);      
		//System.out.println("maskedcardno:::"+maskedcardno);   
		String productcode = getRequest().getParameter("productcode");
		String subproductcode = getRequest().getParameter("subproductcode");
		String accountno = getRequest().getParameter("accountno");
		String suppname = getRequest().getParameter("suppname");
		if (suppname==null)
		{			
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg","Supplementary Emossing Details not found.");
			return "generateAddoncardSuplimentAction";
		}
		
	
		
		trace("Supp Name___"+suppname);
		String usercode = comUserCode();
		
		trace("productcode___"+productcode);
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		
		// BY SIVA 10-07-19
		
		
		
		String orderrefno = commondesc.generateorderRefno(instid, jdbctemplate);
		//String orderrefno = commondesc.getOrderRefNo(instid,padssenable, cardno, TABLENAME, jdbctemplate);
		System.out.println( "order ref number ---> " +orderrefno);
		
		
		
		
		
		//String branchattch = commondesc.checkBranchattached(instid, jdbctemplate);
		//System.out.println( "branch attached === >"  +branchattch);
		
		String bin = commondesc.getBin(instid, productcode, jdbctemplate);	
		String newservicecode=commondesc.getService_code(instid,bin,jdbctemplate);
		trace("New service code based on new product"+newservicecode);
		trace("bin___"+bin);
		String branchattch="";
		String prodcardtype_attach="";
		List attachedtype = commondesc.checkattachedtype(instid,bin,jdbctemplate);
		Iterator attype = attachedtype.iterator();
		if(!attachedtype.isEmpty()){
			while(attype.hasNext())
			{
				Map map = (Map) attype.next(); 
				prodcardtype_attach= ((String)map.get("ATTACH_PRODTYPE_CARDTYPE"));
				branchattch = ((String)map.get("ATTACH_BRCODE")); 				 
			}      
		}
		IfpTransObj transact = commondesc.myTranObject("ADDONSUPLCARD", txManager);
		
		String card_status = "01";
		String caf_recstatus = "AC";
		if( orderrefno.equals("NOREC")){
			addActionError(" Could not get order ref no for the card " + cardno); 
			return "generateAddoncardSuplimentAction"; 
		}
		/*int persocalizeReissuecheck = cardmaindao.checkPersonalizecard(instid, cardno, jdbctemplate);					
		if( persocalizeReissuecheck != 1 ){
			addActionError(" Given Card number [ "+cardno+"] is not a personalized card...");
			return this.searchHome();
		}		*/		
		int updatestatus = -1;
		
		updatestatus = dao.updateCardStatusDate(instid,padssenable, cardno, "REISSUE_DATE", TABLENAME,  jdbctemplate);
		
		if( updatestatus < 0 ){
			addActionError("Could not continue the process... unable to update status" );
			return this.addonSuplHome();
		}
		
		String condition = "";
		
		/*if(padssenable.equals("Y")){
			condition = "AND HCARD_NO='"+cardno+"'";
			}
			else
			{
				condition = "AND CARD_NO='"+cardno+"'";
			}*/
		
		System.out.println( "bin__" + bin);
		Personalizeorderdetails bindetails = commondesc.gettingBindetails(instid,bin,jdbctemplate);
		//(doubt)
		Personalizeorderdetails personlizeorderdetails = commondesc.gettingPersonalizeorderDetailsFromProd(instid,orderrefno,condition,jdbctemplate);
		personlizeorderdetails.card_type_id = productcode.substring(7,9);
		personlizeorderdetails.sub_prod_id = subproductcode;
		personlizeorderdetails.product_code = productcode;
		System.out.println("instantorderdetails-------" +personlizeorderdetails);
		String breakupvalue = commondesc.getChnbreakupvalues(instid,bindetails.prodcard_expiry,bindetails.brcode_servicecode,personlizeorderdetails.card_type_id,personlizeorderdetails.sub_prod_id,personlizeorderdetails.product_code,personlizeorderdetails.branch_code,bindetails.apptypelen,bindetails.apptypevalue);				
		System.out.println( "breakupvalue__" + breakupvalue);
		String newchn = bin+breakupvalue;
		
		/*System.out.println("newchn Generated is ===>"+newchn);*/
		//System.out.println("newchn Generated is ===>");
		
		//String sequncenumber = commondesc.gettingSequnceNumber(instid,bin,personlizeorderdetails.branch_code,bindetails.baselen_feecode,personlizeorderdetails.card_type_id ,jdbctemplate, branchattch);
		  String sequncenumber = commondesc.gettingSequnceNumberNew(instid,bin,personlizeorderdetails.branch_code,bindetails.baselen_feecode,personlizeorderdetails.card_type_id,personlizeorderdetails.sub_prod_id,jdbctemplate,branchattch,prodcardtype_attach);

		long sequn_no = Long.parseLong(sequncenumber);				
		String strseq = Long.toString(sequn_no);
		newcardno = commondesc.generateCHN(newchn,strseq,Integer.parseInt(bindetails.baselen_feecode),Integer.parseInt(bindetails.chnbaseno_limitid));
		//System.out.println("newcardno --- > "+newcardno);
		if( newcardno.equals("N")){
			addActionError(" Could not generate new card number.."); 
			txManager.rollback(transact.status);
			return "generateAddoncardSuplimentAction"; 
		}
		//System.out.println( "NEW CARD NO __" + newcardno );
		System.out.println( "caf_recstatus__" + caf_recstatus );
		
		//trace("newcardno====>"+newcardno);
		
		String keyid = "";
		String EDMK="", EDPK="",newecardno = null;
		StringBuffer newhcardno = new StringBuffer();
		PadssSecurity padsssec = new PadssSecurity();
		String newmcardno = padsssec.getMakedCardno(newcardno);
		if(padssenable.equals("Y"))
		{
			
			keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			System.out.println("keyid::"+keyid);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			

			Properties props=getCommonDescProperty();
			EDPK=props.getProperty("EDPK");
			 
		//	System.out.println("secList::"+secList);  
			Iterator secitr = secList.iterator();
			if(!secList.isEmpty()){
				while(secitr.hasNext())
				{
					Map map = (Map) secitr.next(); 
					String CDMK = ((String)map.get("DMK"));
					//String eDPK = ((String)map.get("DPK"));
					String CDPK=padsssec.decryptDPK(CDMK, EDPK);
					//System.out.println("CEPK====>"+EDPK);
					//System.out.println("CDPK====>"+CDPK);
					newhcardno = padsssec.getHashedValue(newcardno+instid);
					newecardno = padsssec.getECHN(CDPK,newcardno);
					System.out.println("newecardno===>"+newecardno);
					System.out.println("newhcardno===>"+newhcardno);
				
				}      
				}
		} 
		
		
		//reissue move to process  
		
		int cardnoexists = commondesc.checkCardNoExists(instid,newhcardno,jdbctemplate);
		if(cardnoexists > 0){
			txManager.rollback(transact.status);
			//trace("duplicate card no generated for this card--->"+newhcardno);
			addActionError("Unable To Continue Process...!!!SUP");
			return "required_home";
		}




		
		String expperiod = commondesc.getSubProductExpPeriod(instid, subproductcode, jdbctemplate);
		int moveprocess = -1;
		//trace("TTTTTTT--->"+cardno);
		moveprocess = dao.moveCardToProcess(instid,orderrefno,padssenable,productcode,subproductcode,
		bin,accountno, cardno, newecardno,newmcardno,newhcardno.toString(), caf_recstatus, card_status,
		suppname,newcardno,expperiod,collectbranch,newservicecode,jdbctemplate);
		if( moveprocess != 1 ){
			addActionError(" Could not move the production records to process"); 
			txManager.rollback(transact.status);
			return "generateAddoncardSuplimentAction";    
		}
		int reissucnt = dao.updateaddonCount(instid,padssenable,cardno,jdbctemplate);
		if( reissucnt != 1 ){
			txManager.rollback(transact.status);
			addActionError(" COULD NOT UDPATE RE ISSUE COUNT."); 
			return "generateAddoncardSuplimentAction";
		}
		
		
		trace("personlizeorderdetails.cardtypeid::::::::::"+personlizeorderdetails.card_type_id);
		 String updateseq = null;int update_seq = 0;
			int ucnt=0;

		
		String readytoprocessqry ="";
		
		if(padssenable.equals("Y")){
			
		//readytoprocessqry = "UPDATE PERS_CARD_PROCESS SET GENERATED_DATE=SYSDATE WHERE INST_ID='"+instid+"' AND HCARD_NO='"+newhcardno.toString()+"'";
		readytoprocessqry = "UPDATE PERS_CARD_PROCESS SET GENERATED_DATE=SYSDATE WHERE INST_ID=?  AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM PERS_CARD_PROCESS_HASH WHERE HCARD_NO=?) ";
		ucnt = jdbctemplate.update(readytoprocessqry,new Object[]{instid,newhcardno.toString()});
		}
		else{
		readytoprocessqry = "UPDATE PERS_CARD_PROCESS SET GENERATED_DATE=SYSDATE WHERE INST_ID=? AND CARD_NO=? ";
	  	ucnt = jdbctemplate.update(readytoprocessqry,new Object[]{instid,newecardno});
		}
		
		enctrace( "readytoprocessqry__"  + readytoprocessqry );
		System.out.println("BEFOEEEE   ucnt=====>"+ucnt);
	  	//ucnt = jdbctemplate.update(readytoprocessqry);
		System.out.println("ucnt=====>"+ucnt);
	//	System.out.println(newecardno);
		System.out.println("new hcardno====> "+cardno);
		StringBuilder addonlink = new StringBuilder();
		
		
		addonlink.append("INSERT INTO CARDADDON_LINK ");
		addonlink.append("(INST_ID, CARD_NO, ADDON_CARD, ACCOUNT_NO, MAKER_ID, MAKER_DATE, ACCT_CCY) ");
		
		/*addonlink.append("VALUES ");
		addonlink.append("('"+instid+"','"+cardno+"','"+newecardno+"','"+accountno+"','"+usercode+"',sysdate,'')");
		enctrace("addonlink::::"+addonlink);
		
		int uaddoncnt = -1;
		try{
		uaddoncnt = jdbctemplate.update(addonlink.toString());*/
		//by gowtham
		addonlink.append("VALUES ");
		addonlink.append("(?,?,? ,?,?,?,?)");
		enctrace("addonlink::::"+addonlink);
		
		int uaddoncnt = -1;
		try{
		uaddoncnt = jdbctemplate.update(addonlink.toString(),new Object[]{instid,cardno,newecardno,accountno,usercode,date.getCurrentDate(),""});
		
		
		}catch(Exception e)
		{
			//txManager.rollback(transact.status);
			addActionError("Unable tocontinue Add-on card PROCESS"+e); 
		}
		String changedmsg = "Suplimentary ";        
		String waitingmsg = "Card Is Waiting For Authorization";
		auditbean.setRemarks("Generated New Card Is [ "+newmcardno+" ] ");
		 sequn_no++;
		 
		 
		 /*************AUDIT BLOCK**************/ 
		  try{
				 
			  				auditbean.setActmsg("Supplementart Card Generated and Waiting For Authorization" );
							auditbean.setUsercode(usercode);
							auditbean.setAuditactcode("3001"); 
							
							//added by gowtham_220719
							trace("ip address======>  "+ip);
							auditbean.setIpAdress(ip);

							
							commondesc.insertAuditTrail(instid,usercode, auditbean, jdbctemplate, txManager);
						 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
						 /*************AUDIT BLOCK**************/
		 
		  
		  
		  ///by gowtham
					/*if(branchattch.equals("Y")){
						updateseq = "UPDATE BASENO SET CHN_BASE_NO='"+sequn_no+"' WHERE INST_ID='"+instid+"' AND BIN='"+bin+"' AND BASENO_CODE='"+personlizeorderdetails.branch_code+"' ";
					}else if (prodcardtype_attach.equals("C")) {
						updateseq = "UPDATE BASENO SET CHN_BASE_NO='"+sequn_no+"' WHERE INST_ID='"+instid+"' AND BIN='"+bin+"' AND BASENO_CODE='"+personlizeorderdetails.card_type_id+"' ";
					}else if (prodcardtype_attach.equals("P")) {
						updateseq = "UPDATE BASENO SET CHN_BASE_NO='"+sequn_no+"' WHERE INST_ID='"+instid+"' AND BIN='"+bin+"' AND BASENO_CODE='"+personlizeorderdetails.sub_prod_id+"' ";
					}else{
						updateseq = "UPDATE PRODUCTBIN_REL SET CHN_BASE_NO='"+sequn_no+"' WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
					}
				
					update_seq = jdbctemplate.update(updateseq);*/
		  
		  if(branchattch.equals("Y")){
				
			  updateseq = "UPDATE BASENO SET CHN_BASE_NO=? WHERE INST_ID=? AND BIN=? AND BASENO_CODE=? ";
			  update_seq = jdbctemplate.update(updateseq,new Object[]{sequn_no,instid,bin,personlizeorderdetails.branch_code});
		  }else if (prodcardtype_attach.equals("C")) {
				updateseq = "UPDATE BASENO SET CHN_BASE_NO=? WHERE INST_ID=? AND BIN=? AND BASENO_CODE=? ";
				update_seq = jdbctemplate.update(updateseq,new Object[]{sequn_no,instid,bin,personlizeorderdetails.card_type_id});
		  }else if (prodcardtype_attach.equals("P")) {
				updateseq = "UPDATE BASENO SET CHN_BASE_NO=? WHERE INST_ID=? AND BIN=? AND BASENO_CODE=? ";
				update_seq = jdbctemplate.update(updateseq,new Object[]{sequn_no,instid,bin,personlizeorderdetails.sub_prod_id});
		  }else{
				updateseq = "UPDATE PRODUCTINFO SET CHN_BASE_NO=? WHERE INST_ID=? AND PRD_CODE=? ";
				update_seq = jdbctemplate.update(updateseq,new Object[]{sequn_no,instid,bin});
		  }
		
			
		  
			
		System.out.println("updateseq----->123 "+updateseq);
		
		System.out.println("ucnt-----> "+ucnt);	
		System.out.println("update_seq-----> "+update_seq);		
		System.out.println("uaddoncnt-----> "+uaddoncnt);		
		
		if( ucnt == 1 && update_seq ==1 && uaddoncnt==1)
		{
			txManager.commit(transact.status);
			addActionMessage(  newmcardno + " has been Generated and this cardno " + changedmsg + " Into . "+maskedcardno + " "+ waitingmsg );
			return "generateAddoncardSuplimentAction";
		}
		else
		{
			txManager.rollback(transact.status);		
			addActionError("Unable tocontinue Add-on Suplimentary Process"); 
			return "generateAddoncardAction";
		}	
		}
		catch(Exception e)
		{
			//txManager.rollback(transact.status);
			e.printStackTrace();
			addActionError("Unable to continue ..... "); 
			trace("Exception in generateAddoncardAction"+e);
		}  
		trace("generateAddoncardSuplimentAction method ended....");
		//	System.out.println(newcardno);
			newcardno="0000000000000000";
			sb=new StringBuilder(newcardno);
			sb.setLength(0);
			
			newcardno=null;
			sb=null;
			trace("newcardno====1>"+newcardno);
		
		return "generateAddoncardSuplimentAction";
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



	public List getCarddetlist() {
		return carddetlist;
	}



	public void setCarddetlist(List carddetlist) {
		this.carddetlist = carddetlist;
	}

	public String getPadssenabled() {
		return padssenabled;
	}

	public void setPadssenabled(String padssenabled) {
		this.padssenabled = padssenabled;
	}

}
