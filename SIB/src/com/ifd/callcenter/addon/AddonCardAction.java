package com.ifd.callcenter.addon;
import com.ifg.Bean.ServerValidationBean;
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

public class AddonCardAction extends BaseAction{
	
	
	/**
	 * 
	 * kumar
	 */
	
	PadssSecurity sec = new PadssSecurity();
	
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	CommonDesc commondesc = new CommonDesc();
	AuditBeans auditbean = new AuditBeans();
	private List carddetlist;
	private String padssenabled;
	private String hcardNumber;
	String TABLENAME="CARD_PRODUCTION";
	List cardcollectbranchlist;
	
	
	public String getHcardNumber() {
		return hcardNumber;
	}

	public void setHcardNumber(String hcardNumber) {
		this.hcardNumber = hcardNumber;
	}

	private String collectbranch;
	private List subProdList;
	
	public List getSubProdList() {
		return subProdList;
	}

	public void setSubProdList(List subProdList) {
		this.subProdList = subProdList;
	}

	ServerValidationBean bean = new ServerValidationBean();
	public ServerValidationBean getBean() {
		return bean;
	}

	public void setBean(ServerValidationBean bean) {
		this.bean = bean;
	}
	public List getCardcollectbranchlist() {
		return cardcollectbranchlist;
	}

	public void setCardcollectbranchlist(List cardcollectbranchlist) {
		this.cardcollectbranchlist = cardcollectbranchlist;
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
	
	private static final long serialVersionUID = 1L;
	AddonCardActionDAO dao = new AddonCardActionDAO();

	public String addonHome()
	{
		trace("----------------addonHome method called.....");
		String instid = comInstId();
		cardcollectbranchlist = commondesc.generateBranchList(instid, jdbctemplate);
		return "addonhome";
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
	
	public String addonCardAction() throws Exception
	{
		enctrace("--------addonCardAction method called.....");
		trace("-----------addonCardAction method called.....");
		
		//added by prasad 30102019
		HttpSession session = getRequest().getSession(false);
		String sessioncsrftoken = (String) session.getAttribute("token");
	     System.out.println("Global session token---> "+sessioncsrftoken);
		String jspcsrftoken = getRequest().getParameter("token");
		   System.out.println("addonCardAction() method   token---->    "+jspcsrftoken);
		if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) {
			session.setAttribute("message", "CSRF Token Mismatch");
			/* addActionError("CSRF Token Mismatch"); */
			return "invaliduser";
		}
		
		StringBuilder sb=null;
		boolean check;
		int checkvalue;
		String instid = comInstId();
		String cardno = getRequest().getParameter("cardnum");
		//String branch =getRequest().getParameter("collectbranch");
		collectbranch = getRequest().getParameter("collectbranch");
		//System.out.println(cardno);
		//System.out.println("branch --->"+collectbranch);
		
		bean.setCardno(cardno);
		checkvalue=Validation.number(cardno);
		System.out.println("checking value--->"+checkvalue);
		if(checkvalue==0)
		{
			addActionError("Enter Proper Card Number");
			return addonHome();
		}
	
	/*	if(collectbranch.equals(""))
		{
			addActionError("Select collect branch");
			return addonHome();
		}*/
		    
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		
		String keyid = "";
		String EDMK="", EDPK="",CDPK="";
		
	   Properties	props = getCommonDescProperty();
		EDPK = props.getProperty("EDPK");
		
		StringBuffer hcardno = new StringBuffer();
		PadssSecurity padsssec = new PadssSecurity();
		if(padssenable.equals("Y"))
		{
			
			enctrace("calling000000000000000000000000");
			
			keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			Iterator secitr = secList.iterator();
			enctrace("ddddd  ..  000000000000000000000000");
			if(!secList.isEmpty()){
				while(secitr.hasNext())
				{
					Map map = (Map) secitr.next(); 
					String CDMK = ((String)map.get("DMK"));
					CDPK = padsssec.decryptDPK(CDMK, EDPK);
				
					hcardno = padsssec.getHashedValue(cardno+instid);
				}      
				}
		} 
		    
		int checkvalid =-1;
		String  encryptCardno = padsssec.getECHN(CDPK, cardno);;
		
	/*	if(padssenable.equals("Y"))
		{*/
			
		
	/*	checkvalid = dao.validateActvatedCard(instid,padssenable, hcardno.toString(), jdbctemplate);*/
		
		int res=dao.validateCardnumber(instid, encryptCardno, jdbctemplate);
		if(res==0)
		{
			addActionError("Invalid Card No...Enter Valid Card No.");
			return "addonCardAction";
		}
		
		String accoutnNo=dao.getAccountNo(instid, encryptCardno, jdbctemplate);
		
		checkvalid = dao.validateActvatedCard(instid,padssenable, hcardno.toString(), jdbctemplate);
		
		
		
		/*}
		else
		{
			checkvalid = dao.validateActvatedCard(instid,padssenable, cardno, jdbctemplate);
		}*/
		
		if(checkvalid<=0)
		{
			addActionError("Entered Card Not in ACTIVE State OR Entered CardNo is not ParentCard ...Enter Valid Card No.");
			return "addonCardAction";
		} 
		
		
		StringBuilder addoncnt = new StringBuilder();
		addoncnt.append("select case ADDONCARDCOUNT when ");
		addoncnt.append("(select MAXALWD_ADDCARD FROM INSTITUTION where INST_ID='"+instid+"' AND MAXALWD_ADDCARD>=ADDONCARDCOUNT) ");  
		addoncnt.append("THEN 'This Cardno Reached Maximum - '||(select MAXALWD_ADDCARD FROM INSTITUTION where INST_ID='"+instid+"') ");
		addoncnt.append("||' - Add-on Card Limit' else 'SUCCESS' END ADDON_STATUS from ");
		addoncnt.append("CARD_PRODUCTION where  ");
		/*if(padssenable.equals("Y"))
		{
		addoncnt.append("hcard_no='"+hcardno.toString()+"' AND INST_ID='"+instid+"'");
		}else
		{
			addoncnt.append("card_no='"+cardno+"'  AND INST_ID='"+instid+"'");	
		}*/
		
		addoncnt.append("ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM CARD_PRODUCTION_HASH WHERE hcard_no='"+hcardno.toString()+"') AND INST_ID='"+instid+"'");
		enctrace("Checking addcardcount::"+addoncnt.toString());
		
		String addcardcntresult = (String)jdbctemplate.queryForObject(addoncnt.toString(),String.class);
		
		if(!addcardcntresult.trim().equals("SUCCESS"))
		{
			addActionError(addcardcntresult);
			return "addonCardAction";
		}
		
		List carlist = null;
		    
		
		// BY SIVA 10-07-2019
		/*if(padssenable.equals("Y"))
		{
		carlist = dao.getcarddettailForAddon(instid,padssenable, hcardno.toString(),jdbctemplate);
		}else
		{
		carlist = dao.getcarddettailForAddon(instid,padssenable, cardno,jdbctemplate);	
		}*/
		List subprodList=null;
		
		//carlist = dao.getcarddettailForAddon(instid,padssenable, hcardno.toString(),jdbctemplate);
		carlist=dao.getCardDeatils(instid, accoutnNo, jdbctemplate);
		subprodList=dao.getSubProduct(instid, jdbctemplate);
		
		if(carlist.isEmpty())
		{
			addActionError("Not get Card List");
		}
		setCarddetlist(carlist);
		setSubProdList(subprodList);
		trace("carlist::::"+carlist);
		//trace("carlist::: :");
		
		setPadssenabled(padssenable);
		setHcardNumber(hcardno.toString());
		
		cardcollectbranchlist = commondesc.generateBranchList(instid, jdbctemplate);
		//System.out.println(cardno);
		
		cardno="0000000000000000";
		sb=new StringBuilder(cardno);
		sb.setLength(0);
		cardno=null;
		sb=null;
		
		System.out.println(cardno);
		
		return "addonCardAction";
		
	}
	
	public String generateAddoncardAction() throws Exception
	{
		
		
		//added by gowtham_250719
	
		trace("generateAddoncardAction method called....");
		HttpSession session = getRequest().getSession(false);
		String sessioncsrftoken = (String) session.getAttribute("token");
	     System.out.println("Global session token---> "+sessioncsrftoken);
		String jspcsrftoken = getRequest().getParameter("token");
		   System.out.println("generateAddoncardAction() method   token---->    "+jspcsrftoken);
		if (!jspcsrftoken.equalsIgnoreCase(sessioncsrftoken)) {
			session.setAttribute("message", "CSRF Token Mismatch");
			/* addActionError("CSRF Token Mismatch"); */
			return "invaliduser";
		}
		//System.out.println( "card status changing as reissue");
		
		StringBuilder sb =null;
		
		//added by gowtham_220719
		String  ip=(String) session.getAttribute("REMOTE_IP");
		String usernamre="";
		Date date =  new Date();
		
		try{
		String instid = comInstId();
		String org_cardno = getRequest().getParameter("cardnumber");
		
		System.out.println("card number--> "+org_cardno);
		
		PadssSecurity sec = new PadssSecurity();   
		String maskedcardno = getRequest().getParameter("maskedcardno");
		trace("got masked cardno::"+maskedcardno);   
		String productcode = getRequest().getParameter("productcode");
		String accountno = getRequest().getParameter("accountno");
		String usercode = comUserCode();
		String addonname=getRequest().getParameter("addonname");
		String refenceId=getRequest().getParameter("bulkregid");
		String subProductId=getRequest().getParameter("subprodid");
	//	collectbranch = getRequest().getParameter("cardcollectbranch");
		String collectbranch = getRequest().getParameter("collectbranch");
		String cin= getRequest().getParameter("cin");
		System.out.println("CIN---->"+cin);
		
		trace("maskedcardno ["+maskedcardno+"] productcode ["+productcode+"] accountno ["+accountno+"] usercode ["+usercode+"]");
		trace("addonname ["+addonname+"] refenceId ["+refenceId+"] subProductId ["+subProductId+"] collectbranch ["+collectbranch+"]");
		//String dob = getRequest().getParameter("dob");
		trace("getting subProductId" + subProductId  +" productName ::::  "+productcode +"         collectionbranch:::: "+collectbranch +"   encrypt cardnumber::"+org_cardno);
		
		//System.out.println("addonname getting new" + addonname);
		
		
		String hcardno_qry="SELECT cph.HCARD_NO FROM CARD_PRODUCTION cp,CARD_PRODUCTION_HASH cph WHERE  cp.CIN=cph.CIN AND cp.ORDER_REF_NO=cph.ORDER_REF_NO"
				+ " AND  cp.ORG_CHN='"+org_cardno+"' and cp.ACCOUNT_NO='"+accountno+"'";
		enctrace("hcardno_qry-->"+hcardno_qry);
		 String cardno = (String)jdbctemplate.queryForObject(hcardno_qry.toString(),String.class);
		System.out.println("H card number-->"+cardno);
		
		trace("addonname getting new" +addonname);
		if (addonname==null && subProductId.equals("-1") && refenceId==null)
		{			
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg","Add on  Details not found.");
			return "generateAddoncardAction";
		}
		if (addonname == null) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Add on Emossing Details not found.");
			return "generateAddoncardAction";
		}
	
		//trace("addonname getting....." + addonname);
		//System.out.println("getting name from addoncard" + addonname);
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		
	    productcode=dao.getProductCode(instid, subProductId, jdbctemplate);
		
		String orderrefno = commondesc.generateorderRefno(instid, jdbctemplate);
		trace("Generated order reference number is : " + orderrefno);
		String orderrefno1 = commondesc.getOrderRefNo(instid,padssenable, cardno, TABLENAME, jdbctemplate);
		trace("OLD order reference number is : " + orderrefno1);
		
		//System.out.println( "order ref number ---> " +orderrefno);
		//String branchattch = commondesc.checkBranchattached(instid, jdbctemplate);
		//System.out.println( "branch attached === >"  +branchattch);
		
		String bin = commondesc.getBin(instid, productcode, jdbctemplate);	
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
		
		
		IfpTransObj transact = commondesc.myTranObject("ADDONCARD", txManager);
		
		String card_status = "01";
		String caf_recstatus = "AC";
		if( orderrefno.equals("NOREC"))
		{
			addActionError(" Could not get order ref no for the card " + cardno); 
			return "generateAddoncardAction"; 
		}
		
		
		String condition = "";
		/*if(padssenable.equals("Y")){
			condition = "AND HCARD_NO='"+cardno+"'";
			}
			else
			{
				condition = "AND CARD_NO='"+cardno+"'";
			}*/
		
		trace( "bin__" + bin);
		Personalizeorderdetails bindetails = commondesc.gettingBindetails(instid,bin,jdbctemplate);
		//(doubt)
		Personalizeorderdetails personlizeorderdetails = commondesc.gettingPersonalizeorderDetailsFromProd(instid,orderrefno1,condition,jdbctemplate);   
		System.out.println("instantorderdetails-------" +personlizeorderdetails);
		
		trace(bindetails.prodcard_expiry+"||"+bindetails.brcode_servicecode+"||"+personlizeorderdetails.card_type_id+"||"+personlizeorderdetails.sub_prod_id+"||"+personlizeorderdetails.product_code+"||"+personlizeorderdetails.branch_code+"||"+bindetails.apptypelen+"||"+bindetails.apptypevalue);    
		
		String breakupvalue = commondesc.getChnbreakupvalues(instid,bindetails.prodcard_expiry,bindetails.brcode_servicecode,personlizeorderdetails.card_type_id,personlizeorderdetails.sub_prod_id,personlizeorderdetails.product_code,personlizeorderdetails.branch_code,bindetails.apptypelen,bindetails.apptypevalue);				
		trace( "bin :: "+personlizeorderdetails.bin+"  breakupvalue__" + subProductId.substring(6, 8));
		
		String newchn = personlizeorderdetails.bin+personlizeorderdetails.card_type_id;
	    //System.out.println("newchn Generated is ===>"+newchn);
		//String sequncenumber = commondesc.gettingSequnceNumber(instid,personlizeorderdetails.bin,personlizeorderdetails.branch_code,bindetails.baselen_feecode,personlizeorderdetails.card_type_id ,jdbctemplate, branchattch);
		  String sequncenumber = commondesc.gettingSequnceNumberNew(instid,personlizeorderdetails.bin,personlizeorderdetails.branch_code,bindetails.baselen_feecode,personlizeorderdetails.card_type_id,personlizeorderdetails.sub_prod_id,jdbctemplate,branchattch,prodcardtype_attach);
		long sequn_no = Long.parseLong(sequncenumber);				
		String strseq = Long.toString(sequn_no);
		String newcardno = commondesc.generateCHN(newchn,strseq,Integer.parseInt(bindetails.baselen_feecode),Integer.parseInt(bindetails.chnlen_glcode));
		trace("newcardno --- > "+newcardno);
		if( newcardno.equals("N")){
			addActionError(" Could not generate new card number.."); 
			txManager.rollback(transact.status);
			return "generateAddoncardAction"; 
		}
		//System.out.println( "NEW CARD NO __" + newcardno );
		System.out.println( "caf_recstatus__" + caf_recstatus );
		
	    String keyid = "";
		String EDMK="", EDPK="";
		String newclearchn = newcardno;
		StringBuffer newhcardno = new StringBuffer();
		PadssSecurity padsssec = new PadssSecurity();
		String newmcardno = padsssec.getMakedCardno(newcardno);
		if(padssenable.equals("Y"))
		{
			
			keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			//System.out.println("keyid::"+keyid);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			

			Properties props=getCommonDescProperty();
			 EDPK=props.getProperty("EDPK");
		
			//System.out.println("secList::"+secList);  
			Iterator secitr = secList.iterator();
			if(!secList.isEmpty()){
				while(secitr.hasNext())
				{
					Map map = (Map) secitr.next(); 
					String CDMK = ((String)map.get("DMK"));
					//String eDPK = ((String)map.get("DPK"));
					String CDPK=padsssec.decryptDPK(CDMK, EDPK);
					newhcardno = padsssec.getHashedValue(newcardno+instid);
					newcardno = padsssec.getECHN(CDPK, newcardno);
				}      
				}
		} 
		
		//reissue move to process
		int cardnoexists = commondesc.checkCardNoExists(instid,newhcardno,jdbctemplate);
		if(cardnoexists > 0){
			txManager.rollback(transact.status);
			trace("duplicate card no generated for this card--->"+newhcardno.toString());
			addActionError("Unable To Continue Process...RI!!!");
			return "required_home";
		}
 	//added for generate cardreference number on 26-07-2021
		String cardrefnulen = commondesc.getCardReferenceNumberLen(instid, jdbctemplate);
		trace("Got : " + cardrefnulen);
		if (cardrefnulen == null) {
			addActionError("Could not generate card... Card Referene number length is empty...");
			trace("Could not generate card... Card Referene number length is empty...");
			return "required_home";
		}
		
String card_ref_no = commondesc.generateCardRefNumber(instid, subProductId, cardrefnulen,
				jdbctemplate);
		trace("Got cardrefno : " + card_ref_no);
		if (card_ref_no == null) {
		   addActionError("Could not generate card... Got Card Referene number  is empty...");
			trace("Could not generate card... Card Referene number  is empty...");
			 return  "required_home";
		}
		
		trace("card_ref_no-->"+card_ref_no);
	 
		int moveprocess = -1;
		String expperiod = commondesc.getSubProductExpPeriod(instid, personlizeorderdetails.sub_prod_id, jdbctemplate);
		moveprocess = dao.moveCardToProcess(instid,orderrefno,padssenable,accountno, cardno, newcardno,newmcardno,newhcardno.toString(), caf_recstatus, card_status,addonname,expperiod,newclearchn,collectbranch,subProductId,refenceId,productcode,jdbctemplate); 
		if( moveprocess != 1 ){
			addActionError(" Could not move the production records to process"); 
			txManager.rollback(transact.status);
			return "generatTest GeAddoncardAction";    
		}
		int reissucnt = dao.updateaddonCount(instid,padssenable,cardno,jdbctemplate);
		if( reissucnt != 1 ){
			txManager.rollback(transact.status);
			addActionError(" COULD NOT UDPATE RE ISSUE COUNT."); 
			return "generateAddoncardAction";
		}
	 	
		trace("personlizeorderdetails.cardtypeid::::::::::"+personlizeorderdetails.card_type_id);
		 String updateseq = null;
		 int update_seq = 0;
	 
		 String readytoprocessqry ="";
		
		/*if(padssenable.equals("Y")){
		readytoprocessqry = "UPDATE PERS_CARD_PROCESS SET GENERATED_DATE=SYSDATE WHERE INST_ID='"+instid+"' AND HCARD_NO='"+newhcardno.toString()+"'";
		}else{
		readytoprocessqry = "UPDATE PERS_CARD_PROCESS SET GENERATED_DATE=SYSDATE WHERE INST_ID='"+instid+"' AND CARD_NO='"+newcardno+"'";	
		}*/
		
		
		/*readytoprocessqry = "UPDATE PERS_CARD_PROCESS SET GENERATED_DATE=SYSDATE WHERE INST_ID='"+instid+"' AND ORDER_REF_NO IN(SELECT ORDER_REF_NO FROM PERS_CARD_PROCESS_HASH WHERE HCARD_NO='"+newhcardno.toString()+"')";*/
		 	/*readytoprocessqry = "UPDATE PERS_CARD_PROCESS SET GENERATED_DATE=SYSDATE WHERE INST_ID='"+instid+"' AND ORDER_REF_NO='"+orderrefno+"' ";
		enctrace( "readytoprocessqry__"  + readytoprocessqry );
		int ucnt = jdbctemplate.update(readytoprocessqry);*/
		
		
		///by gowtham-300819
		
		System.out.println("orderrefno--->"+orderrefno);
		System.out.println("card_ref_no--->"+card_ref_no);
				readytoprocessqry = "UPDATE PERS_CARD_PROCESS SET GENERATED_DATE=SYSDATE , CARD_REF_NO=? WHERE INST_ID=? AND ORDER_REF_NO=? ";
				enctrace( "readytoprocessqry__"  + readytoprocessqry );
				int ucnt = jdbctemplate.update(readytoprocessqry,new Object[]{card_ref_no,instid,orderrefno});
				System.out.println("ucnt ucnt--->"+ucnt);
		
		
				StringBuilder addonlink = new StringBuilder();
		/*StringBuilder addonlink = new StringBuilder();
		addonlink.append("INSERT INTO CARDADDON_LINK ");
		addonlink.append("(INST_ID, CARD_NO, ADDON_CARD, ACCOUNT_NO, MAKER_ID, MAKER_DATE, ACCT_CCY) ");
		addonlink.append("VALUES ");
		addonlink.append("('"+instid+"','"+cardno+"','"+newcardno+"','"+accountno+"','"+usercode+"',sysdate,'')");
		enctrace("addonlink::::"+addonlink);
		
		int uaddoncnt = -1;
		try
		{
		uaddoncnt = jdbctemplate.update(addonlink.toString());
		*/
			
					//by gowtham-300819
				addonlink.append("INSERT INTO CARDADDON_LINK ");
				addonlink.append("(INST_ID, CARD_NO, ADDON_CARD, ACCOUNT_NO, MAKER_ID, MAKER_DATE, ACCT_CCY) ");
				addonlink.append("VALUES ");
				addonlink.append("(?,?,?,?,?,?,?)");
				enctrace("addonlink::::"+addonlink);	
				int uaddoncnt = -1;
				try{
				uaddoncnt = jdbctemplate.update(addonlink.toString(),new Object[]{instid,cardno,newcardno,accountno,usercode,date.getCurrentDate(),""});
		
		System.out.println("uaddoncnt------>"+uaddoncnt);
	 	
		}
		catch(Exception e)
		{
			//txManager.rollback(transact.status);
			addActionError("Unable tocontinue Add-on card PROCESS"); 
			trace("Unable tocontinue Add-on card PROCESS"+e);   
		}
		String changedmsg = "ADD-ON";        
		//String waitingmsg = "Card Is Waiting For Authorization";
		auditbean.setRemarks("Generated New Card Is [ "+newmcardno+" ] ");
		sequn_no++;
		if(branchattch.equals("Y"))
		/*{
					updateseq = "UPDATE BASENO SET CHN_BASE_NO='"+sequn_no+"' WHERE INST_ID='"+instid+"' AND BIN='"+personlizeorderdetails.bin+"' AND BASENO_CODE='"+personlizeorderdetails.branch_code+"' ";
				}else if (prodcardtype_attach.equals("C")) {
					updateseq = "UPDATE BASENO SET CHN_BASE_NO='"+sequn_no+"' WHERE INST_ID='"+instid+"' AND BIN='"+personlizeorderdetails.bin+"' AND BASENO_CODE='"+personlizeorderdetails.card_type_id+"' ";
				}else if (prodcardtype_attach.equals("P")) {
					updateseq = "UPDATE BASENO SET CHN_BASE_NO='"+sequn_no+"' WHERE INST_ID='"+instid+"' AND BIN='"+personlizeorderdetails.bin+"' AND BASENO_CODE='"+personlizeorderdetails.sub_prod_id+"' ";
				}else{
					updateseq = "UPDATE PRODUCTBIN_REL SET CHN_BASE_NO='"+sequn_no+"' WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
		}
			*/
			
			
		{	
			
			updateseq = "UPDATE BASENO SET CHN_BASE_NO=? WHERE INST_ID=? AND "
			+ "BIN=? AND BASENO_CODE=? ";
			update_seq = jdbctemplate.update(updateseq,new Object[]{sequn_no,instid,personlizeorderdetails.bin,personlizeorderdetails.branch_code});
			}else if (prodcardtype_attach.equals("C")) {
			updateseq = "UPDATE BASENO SET CHN_BASE_NO=? WHERE INST_ID=? AND "
			+ "BIN=? AND BASENO_CODE=? ";
			update_seq = jdbctemplate.update(updateseq,new Object[]{sequn_no,instid,personlizeorderdetails.bin,personlizeorderdetails.card_type_id});
			}else if (prodcardtype_attach.equals("P")) {
			updateseq = "UPDATE BASENO SET CHN_BASE_NO=? WHERE INST_ID=? AND "
			+ "BIN=? AND BASENO_CODE=? ";
			update_seq = jdbctemplate.update(updateseq,new Object[]{sequn_no,instid,personlizeorderdetails.bin,personlizeorderdetails.sub_prod_id});
			}else{updateseq = "UPDATE PRODUCTINFO SET CHN_BASE_NO=? WHERE INST_ID=? AND PRD_CODE=? ";
			update_seq = jdbctemplate.update(updateseq,new Object[]{sequn_no,instid,bin});
		
					
			}
		  //update_seq = jdbctemplate.update(updateseq);
		
	System.out.println("updateseq----->123 "+updateseq);
	System.out.println("updateseq-----> "+update_seq);	
	 	
		if( ucnt == 1 && update_seq ==1 && uaddoncnt==1)
		{
			txManager.commit(transact.status);
			//addActionMessage(  newmcardno + " has been Generated and this cardno " + changedmsg + " Into . "+maskedcardno + " " );
			addActionMessage("New [ " + newmcardno+" ] "+   changedmsg + " CARD has been Generated for this  [ "+maskedcardno + " ]" );
			
			//added by gowtham_220719
			/*************AUDIT BLOCK**************/ 
			  try{
								auditbean.setBin(bin);
								auditbean.setProduct(productcode);
								auditbean.setSubproduct(subProductId);
								auditbean.setCin(cin);
								auditbean.setCardcollectbranch(collectbranch);
								auditbean.setAccoutnno(accountno);
				  				auditbean.setCardno(maskedcardno);
				  				auditbean.setCustname(addonname);
				  				auditbean.setActmsg("New [ " + newmcardno+" ] "+   changedmsg + " CARD has been Generated for this  [ "+maskedcardno + " ]");
								auditbean.setUsercode(usercode);
								auditbean.setAuditactcode("3001"); 
								
							
								trace("ip address======>  "+ip);
								auditbean.setIpAdress(ip);

								
								commondesc.insertAuditTrail(instid,usercode, auditbean, jdbctemplate, txManager);
							 }catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
		 	  
							 /*************AUDIT BLOCK**************/
			  
				//nullify 
			   //System.out.println(cardno);
			   //System.out.println(newcardno);
			   //System.out.println(newclearchn);
			  
			  // Here NEWCLEARCHN only coming not cardno and newcardno
			  
				newcardno="0000000000000000";
				sb=new StringBuilder(newcardno);
				sb.setLength(0);
				newcardno=null;
				sb=null;
				
				newclearchn="0000000000000000";
				sb=new StringBuilder(newclearchn);
				sb.setLength(0);
				newclearchn=null;
				sb=null;
				
				cardno="0000000000000000";
				sb=new StringBuilder(cardno);
				sb.setLength(0);
				cardno=null;
				sb=null;
 			
				 System.out.println( " null values cardnumber[ "+cardno+" ] new card number [ "+newcardno+" ] newclearchn ["+newclearchn+" ]");
				 /* System.out.println(newcardno);
				  System.out.println(newclearchn);
				  */
		 	return "generateAddoncardAction";
		}
		
	 	
		else
		{
			txManager.rollback(transact.status);
			
			addActionError("Unable tocontinue Add-on card PROCESS"); 
			return "generateAddoncardAction";
		}
		
		
		
		}
		catch(Exception e)
		{
			addActionError("COULD NOT UPDATE GENERATED DATE TO PROCESS"); 
			trace("Exception in generateAddoncardAction"+e);
			//System.out.println("Exception in generateAddoncardAction"+e);
		}  
		

		return "generateAddoncardAction";
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

	public String getCollectbranch() {
		return collectbranch;
	}

	public void setCollectbranch(String collectbranch) {
		this.collectbranch = collectbranch;
	}

}
