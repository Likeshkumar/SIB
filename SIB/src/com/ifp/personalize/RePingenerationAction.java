package com.ifp.personalize;

import com.ifp.Action.BaseAction;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.ThalesAction;
import com.ifp.dao.InstCardActivateProcessDAO;
import com.ifp.dao.PinGenerationDAO;
import com.ifp.instant.HSMParameter;
import com.ifp.instant.InstCardPinProcess;
import com.ifp.util.CommonDesc;
import com.ifp.util.DebugWriter;
import com.ifp.util.IfpTransObj;


public class RePingenerationAction extends BaseAction
{
	Properties prop = new Properties();
	private static final long serialVersionUID = 1L;

	 
	CommonDesc commondesc = new CommonDesc();
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

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
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
	
	
	private String act;
	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	
	private List branchlist;
	
	
	public List getBranchlist() {
		return branchlist;
	}

	public void setBranchlist(List branchlist) {
		this.branchlist = branchlist;
	}


	private List personalproductlist;
	public List getPersonalproductlist() {
		return personalproductlist;
	}

	public void setPersonalproductlist(List personalproductlist) {
		this.personalproductlist = personalproductlist;
	}
		
		
	public String personalrepingenerationhome()
	{
		List pers_prodlist=null,br_list=null;
		String inst_id =comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		HttpSession session = getRequest().getSession();
	 
		String temp = act;
		System.out.println(temp);
		session.setAttribute("REPINGEN_ACT", act);
		String session_act = (String) session.getAttribute("REPINGEN_ACT");
		System.out.println("session_act " + session_act);
		
		System.out.println("The DATEFILTER_REQ===> "+(String)session.getAttribute("DATEFILTER_REQ"));
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
				}
			}
			pers_prodlist=commondesc.getProductList(inst_id,jdbctemplate, session);
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

		return "repingenerationhome";
	}
	private List perspingenorders;
	public List getPerspingenorders() {
		return perspingenorders;
	}
	public void setPerspingenorders(List perspingenorders) {
		this.perspingenorders = perspingenorders;
	}
	
	
	
	public String getRePingenerationcards() throws Exception
	{
		HttpSession session = getRequest().getSession();
	 
		
		//String pingentype = getRequest().getParameter("pingentype");
		String branch = getRequest().getParameter("branchcode");
		String binno = getRequest().getParameter("cardtype");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
	 
		
		System.out.println("branch==> "+branch+"   binno==>"+binno+"  fromdate===> "+fromdate+"  todate===>"+todate);
		String dateflag ="GENERATED_DATE";
		String inst_id=comInstId();
		//prop.load(new FileInputStream("D://DECEMBER_IFPSETUP//IFPSETUP_20Dec2012//src//com//ifp//personalize//maintenance.properties"));
		//String caf = prop.getProperty("REPIN_STATUS");
		//System.out.println("CAF STatuys===> "+caf);
		String cardstatus = "01",mkckstatus="P",caf_rec_status="R";
		List authorizeorderlist = null;
		String session_act = (String) session.getAttribute("REPINGEN_ACT");
		System.out.println("session_act " + session_act);		
		try {
			
			int pingen = commondesc.checkPingenerationstatus(inst_id,branch,jdbctemplate);
			System.out.println("Card Gen Status"+pingen);
			if(pingen > 0)
			{
				setAct((String)session.getAttribute("REPINGEN_ACT"));
				System.out.println("Another uSer Processing Card Generation");
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg"," Another User Processing Pin Generation, Please Wait.... ");
				return personalrepingenerationhome();
			}
			String condition = commondesc.filterCondition(binno, branch, fromdate, todate, dateflag);
			System.out.println("Condition Value----->  "+condition);
			
			
			authorizeorderlist = commondesc.maintenanceCardslist( inst_id,cardstatus, mkckstatus,caf_rec_status,condition, jdbctemplate);
			System.out.println("authorizeorderlist===> "+authorizeorderlist);
			if(!(authorizeorderlist.isEmpty())){
				setPerspingenorders(authorizeorderlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg","");
				
			}else{
				setPerspingenorders(authorizeorderlist);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg"," No Orders To Generate Pin");
				setAct((String) session.getAttribute("REPINGEN_ACT"));
				return personalrepingenerationhome();
			}
		} catch (Exception e) {
			System.out.println("Exception--->"+e);
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg","Error While Fetching The Orders To Card Genetaion ,ERROR:"+e.getMessage());
			
		}
		return "repingenerationlist";
	}
	
	public String rePingenerationProcess()
	{
		HttpSession session = getRequest().getSession(); 
		
		String instid = comInstId();
		String userid = comUserId();
		String brcode = getRequest().getParameter("code_branch");
		 
		int pingen = commondesc.checkPingenerationstatus(instid,brcode,jdbctemplate);
		System.out.println("Card Gen Status"+pingen);
		if(pingen > 0)
		{
			setAct((String)session.getAttribute("REPINGEN_ACT"));
			System.out.println("Another uSer Processing Card Generation");
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg"," Another User Processing Pin Generation, Please Wait.... ");
			return personalrepingenerationhome();
		}
		else
		{
			String insert_cardgen = "INSERT INTO CARDGEN_STATUS (INST_ID,BRANCH_CODE,USER_ID,CARDGEN_STATUS,LAST_DATE) VALUES ('"+instid+"','"+brcode+"','"+userid+"','P',(sysdate))";
			IfpTransObj pinstatus_insrt = commondesc.myTranObject("REPIN",txManager);
			try {
				int insertstatus = commondesc.executeTransaction(insert_cardgen, jdbctemplate);
				System.out.println("insert_cardgen status 1111111111111=====> "+insertstatus);
				if(insertstatus == 1)
				{
					System.out.println("Commiting Trasaction 1");
					pinstatus_insrt.txManager.commit(pinstatus_insrt.status);
				}else{
					System.out.println("Roll Back Trasction 1");
					pinstatus_insrt.txManager.rollback(pinstatus_insrt.status);
					session.setAttribute("prevmsg"," Insert Into Card Gen STATUS table is failed ");
					session.setAttribute("preverr","E");
					setAct((String)session.getAttribute("REPINGEN_ACT"));
					return personalrepingenerationhome();
				}
			} catch (Exception e) 
			{
				pinstatus_insrt.txManager.rollback(pinstatus_insrt.status);
				session.setAttribute("prevmsg","Error : "+e);
				session.setAttribute("preverr","E");
				setAct((String)session.getAttribute("REPINGEN_ACT"));
				return personalrepingenerationhome();
			}
			
		}
		String order_refnum[] = getRequest().getParameterValues("personalrefnum");
		String bin = getRequest().getParameter("binno");
		//IfpTransObj trnasct = this.commondesc.myTranObject();
		ThalesAction thalesact = new ThalesAction(); 
		DebugWriter debug_obj = new DebugWriter();
		PinGenerationDAO pinprocs = new PinGenerationDAO();
		
		//String chn = "0";
		String hsmmsg;
		int cvv1 = 0;
		int cvv2 = 0;
		int y=0;
		String errormsg="";
		int pinoffset=0;
		HSMParameter hsmParam_obj;
		hsmParam_obj = commondesc.gettingBin_details(bin,instid, jdbctemplate);
		if( hsmParam_obj == null ){
			System.out.println( "No HSM Properties found for the bin " + bin);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg",  "No HSM Properties found for the bin " + bin );
			setAct((String) session.getAttribute("REPINGEN_ACT"));
			deletePingenerationStatus(instid,brcode, jdbctemplate);
			return personalrepingenerationhome();
		}
		
		Socket connect_id = null;  
		DataOutputStream out = null;
		DataInputStream in = null;
		System.out.println( "Bin Details Found"+hsmParam_obj.CHNLEN+"====="+hsmParam_obj.GEN_METHOD);
		try { 
			
			String actiontype = (String) session.getAttribute("REPINGEN_ACT");
			System.out.println("Action Type is ==>"+actiontype);
			String makerid = "",checkerid="",makerdate="",checkerdate="",mkckstatus="";
			String updatequry="";
			if(actiontype.equals("M")){
				makerid = userid;
				makerdate = "(sysdate)";
				mkckstatus = "P";
				updatequry = "UPDATE PERS_CARD_PROCESS SET CARD_STATUS='02',MAKER_ID='"+makerid+"',MAKER_DATE=(sysdate),MKCK_STATUS='"+mkckstatus+"',PIN_DATE=(sysdate) ";
				
			}else if(actiontype.equals("D")){
				makerid = userid;
				checkerid = userid;
				makerdate = "(sysdate)";
				checkerdate = "(sysdate)";
				mkckstatus = "P";
				updatequry = "UPDATE PERS_CARD_PROCESS SET CARD_STATUS='02',MAKER_ID='"+makerid+"',MAKER_DATE=(sysdate),CHECKER_ID='"+checkerid+"',CHECKER_DATE=(sysdate),MKCK_STATUS='"+mkckstatus+"',PIN_DATE=(sysdate) ";
			}
			System.out.println( "Action Schecked ");
			
			debug_obj.hsmLog( instid, "Trying to connect HSM- IP " +  hsmParam_obj.HSMADDRESS + " PORT - "+ hsmParam_obj.HSMPORT + " Timeout - " + hsmParam_obj.HSMTIMEOUT);
			System.out.println(  "Trying to connect HSM- IP " +  hsmParam_obj.HSMADDRESS + " PORT - "+ hsmParam_obj.HSMPORT + " Timeout - " + hsmParam_obj.HSMTIMEOUT );
			connect_id = pinprocs.ConnectingHSM(hsmParam_obj.HSMADDRESS,hsmParam_obj.HSMPORT,hsmParam_obj.HSMTIMEOUT);
			System.out.println( "Connection status is   "  + connect_id);
			if ( connect_id != null ) {
				hsmmsg = "======== HSM Connected Successfully =========" + connect_id;
				in= new DataInputStream (new BufferedInputStream(connect_id.getInputStream()));
				out = new DataOutputStream (new BufferedOutputStream(connect_id.getOutputStream()));
				System.out.println(hsmmsg);
				debug_obj.hsmLog( instid,hsmmsg);
				 
			}else{
				hsmmsg =  "======== HSM NOT CONNECTED========="; 
				System.out.println(hsmmsg);
				debug_obj.hsmLog( instid,hsmmsg);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg",  " Could not connect hsm " );
				setAct((String) session.getAttribute("REPINGEN_ACT"));
				deletePingenerationStatus(instid,brcode, jdbctemplate);
				return personalrepingenerationhome();
			}
			String Hsmtype = hsmParam_obj.HSMTYPE;
			System.out.println("Hsm Type is ############"+Hsmtype);
			if ( hsmParam_obj.HSMTYPE.equals("THALES") ){
				System.out.println("Forming Load format data");
				debug_obj.hsmLog( instid,"Forming Load format data");
				/*int loadformval = pinprocs.genLoadFormatData(instid,thalesact,session,connect_id, hsmParam_obj, in, out, debug_obj);
				System.out.println("loadformval  returns=====>"+loadformval);
				if( loadformval < 0  ){
					System.out.println( "<===== Load Fomate Data is failed ====>");
					setAct((String) session.getAttribute("REPINGEN_ACT"));
					deletePingenerationStatus(instid,brcode);
					return personalrepingenerationhome();
				}
				System.out.println( "load format data generated ");*/
				
			} 
				
				System.out.println( "CVV Required " + hsmParam_obj.CVV_REQUIRED);
				
				int totalcards = 0,i=0;
				System.out.println( "Cards Selected is ---> "+order_refnum.length);
				for ( i=0; i<order_refnum.length;i++)
				{
					String CHN =  order_refnum[i].toString().trim();
					IfpTransObj trnasctchn = this.commondesc.myTranObject("RECVV",txManager);
					System.out.println( " CHN  " + CHN);
					/*if( hsmParam_obj.CVV_REQUIRED.equals("Y") ){
								System.out.println( " CVV Required .................Y ");
								String exp_table = "PERSONAL"; 
								 cvv1 = pinprocs.generateCVV(session,"cvv1", connect_id, hsmParam_obj, CHN, thalesact, in, out, instid, debug_obj,exp_table);
								  if( cvv1 < 0  ){
									  deletePingenerationStatus(instid,brcode);
									  return personalrepingenerationhome();
								  }
								 System.out.println("cvv1 is  ####### " + cvv1 );
								 cvv2 = pinprocs.generateCVV(session,"cvv2", connect_id, hsmParam_obj, CHN, thalesact, in, out, instid, debug_obj,exp_table);
								 if( cvv2 < 0  ){
									 deletePingenerationStatus(instid,brcode);
									 return personalrepingenerationhome();
								 } 
								 System.out.println("cvv2 is  @@@@@@ " + cvv2 );
							}
								pinoffset = pinprocs.generatePin(instid,session,connect_id,hsmParam_obj,CHN,thalesact,in,out,bin,debug_obj,"P");
								System.out.println( "pinoffset =====>" + pinoffset );
						 
								if( pinoffset < 0  ){
									String f = (String)session.getAttribute("prevmsg");
									System.out.println( "Session Error is===>"+f);
									session.setAttribute("preverr", "E");
									session.setAttribute("prevmsg",  " Pin Generation Failed ");
									setAct((String)session.getAttribute("REPINGEN_ACT"));
									deletePingenerationStatus(instid,brcode);
									return personalrepingenerationhome();
								} */
								
								
								
	
								

								int updatecard_prod=0,del_cardprocess_status=0,updatecardstatus = 0;
								try {
									
									
										String cafrecstatus = commondesc.gettingCAFstatus(CHN,"",instid,jdbctemplate);
										
										System.out.println("THe Caf Rec Status is ===> "+cafrecstatus+" For the CHN ===> "+CHN);
										if(cafrecstatus.equals("R"))
										{
											//CAF_REC_STATUS " R " Re-Pin Card Queries
											System.out.println("CAF REC STATUS IS REPIN");
											
											//String updaterepin_qury = "update IFP_PIN_PRODUCTION set OLD_PIN_OFFSET=PIN_OFFSET,OLD_PIN_DATE=PIN_DATE,CVV1='"+cvv1+"',CVV2='"+cvv2+"',PVV='"+pinoffset+"',PIN_DATE=(sysdate),USER_CODE='"+userid+"' where INST_ID='"+instid+"' AND CARD_NO='"+CHN+"'";
											//System.out.println( "updaterepin qry => " + updaterepin_qury);
											String update_cardprod_qury = "UPDATE CARD_PRODUCTION SET CARD_STATUS='01',CAF_REC_STATUS='A',REPIN_DATE=(sysdate),STATUS_CODE='01',OLD_PIN_OFFSET=PIN_OFFSET,PIN_OFFSET='"+pinoffset+"',PIN_RETRY_CNT=0 WHERE  INST_ID='"+instid+"' AND CARD_NO='"+CHN+"'";
											System.out.println( "Pin update qry => " + update_cardprod_qury);
											String delete_process_qury="delete from PERS_CARD_PROCESS where INST_ID='"+instid+"' and CARD_NO='"+CHN+"'";
											//CAF_REC_STATUS " R " Queries
											
											//update_repin_status = commondesc.executeTransaction(updaterepin_qury);
											//System.out.println("update_repin_status Pin ===>"+update_repin_status);
											updatecard_prod = commondesc.executeTransaction(update_cardprod_qury, jdbctemplate);
											System.out.println("updatecard_prod  ===>"+updatecard_prod);
											del_cardprocess_status = commondesc.executeTransaction(delete_process_qury, jdbctemplate);
											System.out.println("del_cardprocess_status ===>"+del_cardprocess_status);
											if (  updatecard_prod != 1 || del_cardprocess_status != 1)
											{
												trnasctchn.txManager.rollback( trnasctchn.status );
												System.out.println( "Rollback succesfully ");
											}
											else
											{
												trnasctchn.txManager.commit( trnasctchn.status );
												System.out.println( "Commit succesfully ");
												totalcards = totalcards + 1;
											}
										}
										if(cafrecstatus.equals("S"))
										{
											// CAF_REC_STATUS " S " Reissue Card PIN
											
											System.out.println("CAF REC STATUS IS  REISUUSE CARD");											
											//String insprocesupdqry = updatequry+",PIN_OFFSET='"+pinoffset+"',PIN_RETRY_CNT=0,OLD_PIN_OFFSET='"+pinoffset+"' WHERE INST_ID='"+instid+"' AND CARD_NO='"+CHN+"'";
											String insprocesupdqry = updatequry+",PIN_OFFSET='"+pinoffset+"',OLD_PIN_OFFSET='"+pinoffset+"' WHERE INST_ID='"+instid+"' AND CARD_NO='"+CHN+"'";
											System.out.println( "Pin update qry => " + insprocesupdqry); 
											updatecardstatus = commondesc.executeTransaction(insprocesupdqry, jdbctemplate);
											System.out.println(" updatecardstatus===> "+updatecardstatus);
											if( updatecardstatus != 1)
											{
												trnasctchn.txManager.rollback( trnasctchn.status );
											}
											else
											{
												trnasctchn.txManager.commit( trnasctchn.status );
												totalcards = totalcards + 1;
											}
										}

									
								} catch (Exception e) 
								{ 
									trnasctchn.txManager.rollback( trnasctchn.status );
									System.out.println( "Exception Rollback succesfully " + e);
									y = 1;
									errormsg = e.getMessage();
								}
								if(y == 1)
								{
									System.out.println("Error Occured Process Breaks");
									break;
								}
		}//For Loop Ends
		deletePingenerationStatus(instid, brcode, jdbctemplate);
        System.out.println("Total Cards===> "+totalcards+" I value is ===> "+i); 
		if(totalcards == i){
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg",  " Pin Generated Successfuly ");
		}else{
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg",  " Error While Generating Pin ");
		}
		if(y == 1){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg",  " Error While Generating Pin "+errormsg);
		}
		} 
		catch (Exception e) 
		{
			
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg",  " Could not update card order details. Error while update " + e.getMessage() );  
		} finally{ 
			if( connect_id != null){
				try {
					connect_id.close();
				} catch (Exception e) {
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg",  " Could not Close the Connection .");  
					return personalrepingenerationhome();
				}
			}else{
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg",  " Could not Connect HSM.");  
				return personalrepingenerationhome();
			}
			System.out.println("Socket Connection closed properly ");
		}
		setAct((String) session.getAttribute("REPINGEN_ACT"));
		return personalrepingenerationhome();
		//return "repingenerationprocess";
	}
	
	public void deletePingenerationStatus(String instid,String brcode, JdbcTemplate jdbctemplate)
	{
		String deleteCardgenstatus = "DELETE FROM CARDGEN_STATUS WHERE INST_ID='"+instid+"' AND BRANCH_CODE='"+brcode+"' AND CARDGEN_STATUS='P'";
		System.out.println("Delete Qury uis ==> "+deleteCardgenstatus);
		IfpTransObj deletestatus = commondesc.myTranObject("DELPINGEN",txManager);
		try{
			
			int delete_status = commondesc.executeTransaction(deleteCardgenstatus, jdbctemplate);
			System.out.println("delete_status ====> "+delete_status);
			if(delete_status == 1)
			{
				deletestatus.txManager.commit(deletestatus.status);
				System.out.println("delete_status Comitted");
			}else{
				deletestatus.txManager.rollback(deletestatus.status);
				System.out.println("delete_status Rollback");
			}
			} catch (Exception e) {
				deletestatus.txManager.rollback(deletestatus.status);
				System.out.println("Error While Deleting==> "+e);
			}
			System.out.println("Delete Tnasaction Completed ===> "+deletestatus.status.isCompleted());
	}
	
}
