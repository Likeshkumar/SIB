package com.ifp.instant;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.net.nntp.NewGroupsOrNewsQuery;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import pin.safenet.HsmTcpIp;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.beans.PinGenerationBeans;
import com.ifp.dao.CardMaintainActionDAO;
import com.ifp.dao.PinGenerationDAO;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.IfpTransObj;

import connection.Dbcon;

import com.ifp.util.TrackEncryption;

/**
 * SRNP0004
 * 
 * @author CGSPL
 *
 */
public class InstCardPinProcess extends BaseAction {
	TrackEncryption EncDec = new TrackEncryption();
	private static final long serialVersionUID = 5505352292589141634L;

	private static final String Expiry = null;

	CommonDesc commondesc = new CommonDesc();
	CommonUtil comutil = new CommonUtil();
	PinGenerationBeans pinbean = new PinGenerationBeans();
	PinGenerationDAO pindao = new PinGenerationDAO();
	private static Boolean initmail = true;
	private static String parentid = "000";
	final String CARDNEXTSTATUS = "02";
	int SOCKETLIMIT = 5000;
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();
	AuditBeans auditbean = new AuditBeans();

	public AuditBeans getAuditbean() {
		return auditbean;
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
	}

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	private List instperspingenauthlist;

	public List getInstperspingenauthlist() {
		return instperspingenauthlist;
	}

	public void setInstperspingenauthlist(List instperspingenauthlist) {
		this.instperspingenauthlist = instperspingenauthlist;
	}

	private List Instpersonalproductlist;

	public List getInstpersonalproductlist() {
		return Instpersonalproductlist;
	}

	public void setInstpersonalproductlist(List instpersonalproductlist) {
		Instpersonalproductlist = instpersonalproductlist;
	}

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public PinGenerationBeans getPinbean() {
		return pinbean;
	}

	public void setPinbean(PinGenerationBeans pinbean) {
		this.pinbean = pinbean;
	}

	public PinGenerationDAO getPindao() {
		return pindao;
	}

	public void setPindao(PinGenerationDAO pindao) {
		this.pindao = pindao;
	}

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	public CommonUtil getComutil() {
		return comutil;
	}

	public void setComutil(CommonUtil comutil) {
		this.comutil = comutil;
	}

	public String comInstId() {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String comUserCode() {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("USERID");
		return instid;
	}

	public String comUserId() {
		HttpSession session = getRequest().getSession();
		String userid = (String) session.getAttribute("USERID");
		return userid;
	}

	public String comBranchId() {
		HttpSession session = getRequest().getSession();
		String br_id = (String) session.getAttribute("BRANCHCODE");
		return br_id;
	}

	public String comuserType() {
		HttpSession session = getRequest().getSession();
		String usertype = (String) session.getAttribute("USERTYPE");
		return usertype;
	}

	public String comUsername() {
		HttpSession session = getRequest().getSession();
		String username = (String) session.getAttribute("USERNAME");
		return username;
	}

	List prodlist;
	List branchlist;
	List instorderlist;
	private String cardtype_selected;
	String act;

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	public String getCardtype_selected() {
		return cardtype_selected;
	}

	public void setCardtype_selected(String cardtype_selected) {
		this.cardtype_selected = cardtype_selected;
	}

	public List getInstorderlist() {
		return instorderlist;
	}

	public void setInstorderlist(List instorderlist) {
		this.instorderlist = instorderlist;
	}

	public List getProdlist() {
		return prodlist;
	}

	public void setProdlist(List prodlist) {
		this.prodlist = prodlist;
	}

	public List getBranchlist() {
		return branchlist;
	}

	public void setBranchlist(List branchlist) {
		this.branchlist = branchlist;
	}

	public String cardPinHome() {
		HttpSession session = getRequest().getSession();

		String instid = comInstId();
		String mkrstatus = "P";
		String cardStatus = "01";
		String usertype = comuserType();
		String branch = comBranchId();
		List br_list = null, pers_prodlist = null;
		try {

			String act = getRequest().getParameter("act");
			if (act != null) {
				session.setAttribute("act", act);
			}

			System.out.println("Inst Id===>" + instid + "  Branch Code ===>" + branch);
			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = commondesc.InstgetBranchCodefmProcess(instid, cardStatus, mkrstatus, jdbctemplate);
				System.out.println("Branch list " + br_list);
				if (!(br_list.isEmpty())) {
					setBranchlist(br_list);
					System.out.println("Branch list is not empty");
					// setCardgenstatus('Y');
				} else {
					addActionError("No Cards Waiting For Security Data Generation ... ");
					System.out.println("Branch List is empty ");
					return "required_home";

				}
			}
			pers_prodlist = commondesc.InstgetProductListBySelected(instid, cardStatus, mkrstatus, jdbctemplate);
			if (!(pers_prodlist.isEmpty())) {
				setProdlist(pers_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "");
				System.out.println("Product List is ===> " + pers_prodlist);
				// setCardgenstatus('Y');
			} else {
				System.out.println("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", " No Product Details Found ");
				// setCardgenstatus('N');
			}

			/*** MAIL BLOCK ****/
			System.out.println("initmail--" + initmail + " parentid :  " + this.parentid);
			if (initmail) {
				HttpServletRequest req = getRequest();
				String menuid = comutil.getUrlMenuId(req, jdbctemplate);
				if (!menuid.equals("NOREC")) {
					System.out.println("parentid--" + menuid);
					this.parentid = menuid;
				} else {
					this.parentid = "000";
				}
				initmail = false;
			}
			/*** MAIL BLOCK ****/

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Could not generate pin");
			trace("Exception : Colud not generate pin " + e.getMessage());
			e.printStackTrace();
		}
		return "instcardpin_home";
	}

	public String viewPinGenList() {
		trace("****** Pin generation list begin ******");
		enctrace("****** Pin generation list begin******");
		HttpSession session = getRequest().getSession();

		String instid = comInstId();
		String card_status = "01", mkckstatus = "P";
		String datefld = "AUTH_DATE";
		String branch = getRequest().getParameter("branchcode");
		String cardtype = getRequest().getParameter("cardtype");
		// String[] bin = cardtype.split("~");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		try {

			String act = getRequest().getParameter("act");
			if (act != null) {
				session.setAttribute("act", act);
			}

			trace("Getting filter condition....");
			String filtercond = commondesc.filterCondition(cardtype, branch, fromdate, todate, datefld);
			trace("filter condition " + filtercond);
			trace("Getting waiting for pin generation....");
			List waitingforcardpin = commondesc.waitingForInstCardProcess(instid, card_status, mkckstatus, filtercond,
					jdbctemplate);
			if (waitingforcardpin.isEmpty()) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "No record found");
				return "required_home";
			}

			setInstorderlist(waitingforcardpin);
			setCardtype_selected(cardtype);
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Could not generate pin");
			trace("Exception : Colud not generate pin " + e.getMessage());
			e.printStackTrace();
		}
		trace("****** Pin generation list end ******");
		enctrace("****** Pin generation list end******");
		return "instcardpin_list";
	}

	public String generateInstCardPin() {

		trace("****** Generating instant card pin/cvv begins  ******");
		enctrace("****** Generating instant card pin/cvv begins ******");
		HttpSession session = getRequest().getSession();

		// IfpTransObj transact = commondesc.myTranObject(dataSource);
		IfpTransObj transact = commondesc.myTranObject("GENERATPIN", txManager);

		String apptype = (String) session.getAttribute("APPLICATIONTYPE");

		String instid = comInstId();
		String act = (String) session.getAttribute("act");
		String processtype = "INSTANT";
		if (apptype != null && apptype.equals("CREDIT")) {
			processtype = "CREDIT";
		}
		String authmsg = "", mkckstatus = "", actiontype = "";
		if (act != null && act.equals("M")) {
			authmsg = " Waiting  For Authorization";
			mkckstatus = "M";
			actiontype = "IM";
		} else {
			mkckstatus = "P";
			actiontype = "IC";
		}
		String generationtypeforonlycvv = getRequest().getParameter("generationtypeforonlycvv");
		trace("generationtypeforonlycvv" + generationtypeforonlycvv);
		String branchcode = getRequest().getParameter("brch_code");
		String card_type = getRequest().getParameter("cardtype_sel");
		String[] order_refnum = getRequest().getParameterValues("instorderrefnum");
		String usercode = comUserCode();
		String bin = getRequest().getParameter("bin");
		// IfpTransObj ifpdelete = commondesc.myTranObject(dataSource);

		// IfpTransObj in = commondesc.myTranObject(dataSource);
		try {

			/*
			 * trace( "Checking any user generating pin....."); int pingen =
			 * commondesc.checkPingenerationstatus(instid,branchcode,
			 * jdbctemplate); trace("Got : " + pingen); if(pingen > 0) {
			 * setAct((String)session.getAttribute("act")); trace(
			 * "Another uSer Processing Card Generation");
			 * session.setAttribute("preverr", "E");
			 * session.setAttribute("prevmsg",
			 * " Another User Processing Pin Generation, Please Wait.... ");
			 * return this.cardPinHome(); } else{ trace(
			 * "Inserting pin generation status...."); int pingenprocess =
			 * pindao.blockAnotherUserGeneratePin(instid, branchcode, usercode,
			 * jdbctemplate); if( pingenprocess < 0 ){
			 * txManager.rollback(insertstatus); session.setAttribute("preverr",
			 * "E"); session.setAttribute("prevmsg",
			 * " Unable to continue cvv generaiont."); trace(
			 * "Unable to continue cvv generaiont. got rolled back"); return
			 * this.cardPinHome(); }else{ txManager.commit(insertstatus); trace(
			 * "Pin generation status inserted got committed."); } }
			 */

			int cvvstatus = pinCvvGenerationAction(instid, generationtypeforonlycvv, branchcode, card_type,
					order_refnum, usercode, processtype, mkckstatus, bin, txManager, jdbctemplate, transact.status,
					session);
			trace("cvvstatus :" + cvvstatus);

			if (cvvstatus < 0) {
				// transact.txManager.rollback(transact.status);
				txManager.rollback(transact.status);
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "Unable to Continue CVV Generation");
				trace("Could not continue the cvv generation....got rolled back");
				return this.cardPinHome();
			}

			transact.txManager.commit(transact.status);
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", "Security Data Generated Successfully. ");
			trace("Cvv generated successfully[ Insatnt ]...got committed...");

		} catch (Exception e) {
			// transact.txManager.rollback(transact.status);
			// txManager.rollback(transact.status);
			// if(transact.status != null ) {
			// txManager.rollback(transact.status); }
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Unable to generate cvv...");
			trace("Exception unable to generate cvv : " + e.getMessage());
			e.printStackTrace();
		}
		trace("****** Generating instant card pin/cvv end  ******\n\n");
		enctrace("****** Generating instant card pin/cvv end******\n\n");
		return this.cardPinHome();

	}

	/**
	 * This method is to generate CVV values
	 * 
	 * 
	 * 
	 * @param instid
	 * @param generationtypeforonlycvv
	 * @param branchcode
	 * @param card_type
	 * @param order_refnum
	 * @param usercode
	 * @param processtype
	 * @param mkckstatus
	 * @param bin
	 * @param txManagerNew
	 * @param jdbctemplate
	 * @param delstatus
	 * @param session
	 * @return
	 * @throws Exception
	 */

	public int pinCvvGenerationAction(String instid, String generationtypeforonlycvv, String branchcode,
			String card_type, String[] order_refnum, String usercode, String processtype, String mkckstatus, String bin,
			PlatformTransactionManager txManagerNew, JdbcTemplate jdbctemplate, TransactionStatus delstatus,
			HttpSession session) throws Exception {

		HSMParameter hsmobj;
		PadssSecurity padsssec = null;
		CardMaintainActionDAO maintaindao = null;

		Properties props = null;
		Socket connect_id = null;
		DataOutputStream out = null;
		DataInputStream in = null;
		String pinoffset = null;
		String hcardno = null;
		String encryptpinblcokval = null;

		String keyid = null;
		String dcardno = null;
		String CDMK = null;
		String CDPK = null;
		String EDPK = null;
		String cvv1_value = null;
		String cvv2_value = null;
		String icvv_value = null;
		String cardtypemaporchip = null;
		String ECVV1 = null;
		String ECVV2 = null;
		String EICVV = null;

		String gentype = null;
		String genmethod = null;
		String BRANCH_CODE = null;

		StringBuilder sb = new StringBuilder();

		// String table = "INST_CARD_PROCESS";
		HsmTcpIp hsmtcpip = new HsmTcpIp();
		trace("Getting the security attributes....");
		hsmobj = pindao.gettingBin_details(bin, instid, jdbctemplate);

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		if (hsmobj == null) {
			trace("No HSM Properties found for the bin " + bin);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "No HSM Properties found for the bin " + bin);
			return -1;
		}
		trace("Got security attributes");
		trace("Getting sub product configuration....");

		/*
		 * List configs =
		 * commondesc.getConfigurationdetails(instid,card_type,jdbctemplate);
		 * trace("Got configuration "+configs.size()); if(configs.isEmpty()) {
		 * //this.removeBlockedUser(instid, branchcode, jdbctemplate,
		 * delstatus); return -1; }
		 */

		trace("Connectin HSM on ip : " + hsmobj.HSMADDRESS + ", PORT : " + hsmobj.HSMPORT + "  HSMTIMEOUT "
				+ hsmobj.HSMTIMEOUT + "PINLENGTH:" + hsmobj.PIN_LENGTH);
		connect_id = pindao.ConnectingHSM(hsmobj.HSMADDRESS, hsmobj.HSMPORT, hsmobj.HSMTIMEOUT);

		if (connect_id != null) {
			trace("Connection socket object ... : " + connect_id);
			in = new DataInputStream(new BufferedInputStream(connect_id.getInputStream()));
			out = new DataOutputStream(new BufferedOutputStream(connect_id.getOutputStream()));
		} else {
			trace("Could not connect hsm");
			addActionError("Could not connect to hsm");
			////// this.removeBlockedUser(instid, branchcode, jdbctemplate,
			////// delstatus);
			return -1;
		}

		trace("Iterating cards one by one....");
		trace(" CVV Required check : " + hsmobj.CVV_REQUIRED);
		// strace("The configured length of the cvv value [ " +
		// hsmobj.CVV_LENGTH + " ]");
		// trace( " CVK1 : " + hsmobj.CVVK1);
		// trace( " CVK2 : " + hsmobj.CVVK2);

		// getting dec cardno

		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);

		props = getCommonDescProperty();
		EDPK = props.getProperty("EDPK");

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

		// getting clear DPK by passing clear dmk,iv,EDPK values
		padsssec = new PadssSecurity();
		CDPK = padsssec.decryptDPK(CDMK, EDPK);

		///

		// GEN_METHOD added for Orient Bank Requirements
		// Its to update pin offset for VISA And IBMDES
		List getsecDetils = commondesc.getSequritygenerationdetails(instid, bin, jdbctemplate);
		Iterator secitr = getsecDetils.iterator();

		while (secitr.hasNext()) {
			Map secmap = (Map) secitr.next();

			cardtypemaporchip = (String) secmap.get("CARD_TYPE");
			trace("TTTTTTTTTTT" + cardtypemaporchip);
			gentype = (String) secmap.get("SEQ_OPTION");
			genmethod = (String) secmap.get("GEN_METHOD");
		}

		try {

			maintaindao = new CardMaintainActionDAO();

			trace("order_refnum.length total---->" + order_refnum.length);
			enctrace("order_refnum.length total---->" + order_refnum.length);

			for (int i = 0; i < order_refnum.length; i++) {

				// String pin_req = "X";
				trace("order_refnum one by one---->" + order_refnum[i]);
				enctrace("order_refnum one by one---->" + i);
				// String CHN = order_refnum[i].toString().trim();
				String refnum = order_refnum[i].toString().trim();

				List chnlist = commondesc.getCardsFromOrder(instid, refnum, processtype, "P", "01", jdbctemplate);

				// we are passing ORG_CHN Value instead of Encrypted card number

				if (!chnlist.isEmpty()) {
					Iterator itr = chnlist.iterator();
					int cardcount = 0;
					while (itr.hasNext()) {
						Map temp = (Map) itr.next();

						card_type = (String) temp.get("PRODUCT_CODE");
						String CHN = (String) temp.get("ORG_CHN");
						BRANCH_CODE = (String) temp.get("BRANCH_CODE");

						if (padssenable.equals("Y")) {
							dcardno = padsssec.getCHN(CDPK, CHN);
						}

						String cin = commondesc.getCustomerIdByCardNumberProcess(instid, CHN, processtype,
								jdbctemplate);
						trace("Getting customer id ...got : " + cin);
						String hcardnoQry = "SELECT  HCARD_NO  FROM PERS_CARD_PROCESS_HASH WHERE CIN='" + cin + "' and ORDER_REF_NO='" + order_refnum[i] + "' ";
						if(processtype.equals("INSTANT")){
							hcardnoQry = "SELECT  HCARD_NO  FROM inst_card_process_hash WHERE ORDER_REF_NO='" + order_refnum[i] + "' ";
						}
						
						//String hcardnoQry = "SELECT  HCARD_NO  FROM inst_card_process_hash WHERE CIN='" + cin + "' ";
						enctrace("hcardnoQry-->"+hcardnoQry);
						hcardno = (String) jdbctemplate.queryForObject(hcardnoQry, String.class);

						IfpTransObj pintransact = commondesc.myTranObject("PIN" + cardcount, txManagerNew);

						trace("gentype-----:::" + gentype);

						HsmTcpIp hsmtcp = new HsmTcpIp();
						trace("Pin generating process begins for the card no [ " + CHN + " ] ");

						// if( gentype == null ){
						// addActionError("Pin Generation Required Not
						// Configured For the subproduct [ "+subproduct+" ] ");
						// return -1;
						// }

						// remove "NO" for Pin AND CVV Generation

						if (gentype.equals("PC") && generationtypeforonlycvv.equals("NO")
								|| gentype.equals("P") && generationtypeforonlycvv.equals("NO")) {

							trace("###########################pin printing #############");

							/*
							 * pinoffset =
							 * hsmtcp.printRacalPin(instid,CHN,dcardno, cin,
							 * card_type, bin, processtype, in, out,
							 * jdbctemplate, hsmobj); trace(
							 * "Generated pin command...got : " + pinoffset);
							 */
							/*
							 * if( pinoffsetresp == null ){
							 * txManagerNew.rollback(pintransact.status); trace(
							 * "Could not generate pin..1...got rolledback");
							 * return -1; } trace("getting pin offset value " +
							 * pinoffsetresp); String pinoffsetattr[] =
							 * pinoffsetresp.split("~"); if(
							 * pinoffsetattr[1].equals("0")){
							 * 
							 * pinoffset =pinoffsetattr[0]; trace(
							 * "getting pin offset value " + pinoffset); }else{
							 * session.setAttribute("preverr", "E");
							 * session.setAttribute("prevmsg",
							 * pinoffsetattr[0]);
							 * addActionError(pinoffsetattr[0]);
							 * txManagerNew.rollback(pintransact.status); trace(
							 * "Could not generate pin..2..got rolledback");
							 * return -1; }
							 */

						}

						trace("::::::::: gentype---testing--::::::::::::::" + gentype);

						String mcard = "", pinoffsetresp = "";
						String respmsg = null;

						//processtype
						//String subproduct = commondesc.getSubProductByCHNProcess(instid, CHN, gentype, jdbctemplate);
						String subproduct = commondesc.getSubProductByCHNProcess(instid, CHN, processtype, jdbctemplate);
						trace("Getting sub-product code ..got : " + subproduct);

						// commented on 260717
						String haspinreq = commondesc.checkPinGenRequired(instid, subproduct, jdbctemplate);
						trace("Pin Required For This Sub-Product : " + haspinreq);

						if (haspinreq == null) {
							addActionError("Pin Generation Required Not Configured For the subproduct [ " + subproduct
									+ " ] ");
							return -1;
						}

						/*
						 * =========================================== PIN
						 * PRINTING Starts HERE
						 * =====================================================
						 */

						if (haspinreq.equals("Y")) {

							if (gentype.equals("PC") || gentype.equals("P")) {

								trace("###########################pin printing #############");

								if (hsmobj.HSMTYPE.equals("THALES")) {

									/*
									 * pinoffset = hsmtcp.printRacalPin(instid,
									 * dcardno, mcard, cin, card_type, bin,
									 * processtype, in, out, jdbctemplate,
									 * hsmobj,CHN);
									 */
									if("PERSONAL".equals(processtype)){
										pinoffset = hsmtcp.printRacalPin(instid, dcardno, dcardno, cin, card_type, bin,
												processtype, in, out, jdbctemplate, hsmobj, CHN);
										trace("genetrated final pinoffset :" + pinoffset);
									}else{
										pinoffset = "0000";
									}
									
									if (pinoffset.length() > 4) {
										txManagerNew.rollback(pintransact.status);
										respmsg = hsmtcpip.getRespCode(pinoffset);
										trace("Error :" + respmsg);
										addActionError(
												"Unable to Generation Pin for that card [" + CHN + "]..." + respmsg);
										return -1;
									}

									if (pinoffset.length() == 2) {
										txManagerNew.rollback(pintransact.status);
										respmsg = hsmtcpip.getRespCode(pinoffset);
										trace("Error :" + respmsg);
										addActionError(
												"Unable to Generation Pin for that card [" + CHN + "]..." + respmsg);
										return -1;
									}
									if (pinoffset.equals("-1")) {
										txManagerNew.rollback(pintransact.status);
										trace("Could not generate pin..got rolledback");
										return -1;
									}
									if (pinoffset == null || pinoffset == "") {
										txManagerNew.rollback(pintransact.status);
										trace("Could not generate pin..got rolledback");
										return -1;
									}

								} else {
									pinoffsetresp = hsmtcp.printPin(instid, CHN, cin, card_type, bin, processtype, in,
											out, jdbctemplate, hsmobj);

									if (pinoffsetresp == null) {
										txManagerNew.rollback(pintransact.status);
										trace("Could not generate pin..1...got rolledback");
										return -1;
									}
									String pinoffsetattr[] = pinoffsetresp.split("~");
									if (pinoffsetattr[1].equals("0")) {
										pinoffset = pinoffsetattr[0];
									} else {
										session.setAttribute("preverr", "E");
										session.setAttribute("prevmsg", pinoffsetattr[0]);
										addActionError(pinoffsetattr[0]);
										txManagerNew.rollback(pintransact.status);
										trace("Could not generate pin..2..got rolledback");
										return -1;
									}
								}
							}
						} else {

							pinoffset = "0000";
						}

						/*
						 * ==================================== PIN PRINTING
						 * ENDS HERE =========================================
						 */

						/*
						 * ==================================== CVV PRINTING
						 * STARTS HERE =========================================
						 */

						if (hsmobj.CVV_REQUIRED.equals("Y")) {

							if (gentype.equals("C") || gentype.equals("PC")) {
								String expiry_date1 = null;
								String REQCVV = null;
								String servicecode = commondesc.getServiceCodeByChn(instid, CHN, processtype,
										jdbctemplate);
								trace("========= Cvv1 generating process begins for the card no [ " + dcardno
										+ " ] ==============");
								REQCVV = "cvv1";
								trace("Generating " + REQCVV);
								//expiry_date1 = pindao.getChnExpiryDatecvv1(instid, processtype, CHN, REQCVV,jdbctemplate);
								expiry_date1 = pindao.getChnExpiryDate(instid, processtype, CHN, REQCVV,jdbctemplate);
								trace("CHN[" + dcardno + "], EXPDATE[" + expiry_date1 + "], SERVICECODEP[" + servicecode
										+ "]");

								System.out.println("generating thales cvv1" + hsmobj.HSMTYPE);
								if (padssenable.equals("Y")) {
									if (hsmobj.HSMTYPE.equals("THALES")) {

										trace("generating thales cvv1");
										cvv1_value = hsmtcpip.generateRacalCVV_Values(dcardno, expiry_date1,
												servicecode, hsmobj, in, out); // this.generateCVV(CHN,
																				// expiry_date,
																				// servicecode,
																				// in,
																				// out,
																				// hsmobj);
										if (cvv1_value.length() == 2) {
											txManagerNew.rollback(pintransact.status);
											respmsg = hsmtcpip.getRespCode(cvv1_value);
											trace("Error :" + respmsg);
											addActionError(
													"Unable to Generate CVV1 for that card [" + CHN + "]..." + respmsg);
											return -1;
										}

									} else {
										cvv1_value = hsmtcpip.generateCVV_Values(dcardno, expiry_date1, servicecode,
												hsmobj, in, out); // this.generateCVV(CHN,
																	// expiry_date,
																	// servicecode,
																	// in, out,
																	// hsmobj);
									}

								}

								else {
									if (hsmobj.HSMTYPE.equals("THALES")) {
										cvv1_value = hsmtcpip.generateRacalCVV_Values(dcardno, expiry_date1,
												servicecode, hsmobj, in, out); // this.generateCVV(CHN,
																				// expiry_date,
																				// servicecode,
																				// in,
																				// out,
																				// hsmobj);
										if (cvv1_value.length() == 2) {
											txManagerNew.rollback(pintransact.status);
											respmsg = hsmtcpip.getRespCode(cvv1_value);
											trace("Error :" + respmsg);
											addActionError(
													"Unable to Generate CVV1 for that card [" + CHN + "]..." + respmsg);
											return -1;
										}

									} else {
										cvv1_value = hsmtcpip.generateCVV_Values(CHN, expiry_date1, servicecode, hsmobj,
												in, out); // this.generateCVV(CHN,
															// expiry_date,
															// servicecode, in,
															// out, hsmobj);

									}

								}

								if (cvv1_value == null || cvv1_value == "-1") {
									txManagerNew.rollback(pintransact.status);
									trace("Unable to generate cvv1 for the card number [ " + CHN
											+ " ]...got rolledback ");
									addActionError("Unable to generate cvv1 for the card number [ " + CHN + " ] ");
									return -1;
								}
								trace("Got cvv1_value : " + cvv1_value + " and length : " + cvv1_value.length());
								
								trace("Cvv2 generating process begins for the card no [ " + CHN + " ] ");
								REQCVV = "cvv2";
								trace("Generating " + REQCVV);
								//getChnExpiryDate
								//String expiry_date2 = pindao.getChnExpiryDatecvv2(instid, processtype, CHN, REQCVV,jdbctemplate);
								String expiry_date2 = pindao.getChnExpiryDate(instid, processtype, CHN, REQCVV,jdbctemplate);
								trace("CHN[" + CHN + "], EXPDATE[" + expiry_date2 + "], SERVICECODEP[" + servicecode
										+ " ]");

								if (padssenable.equals("Y")) {

									if (hsmobj.HSMTYPE.equals("THALES")) {
										cvv2_value = hsmtcpip.generateRacalCVV_Values(dcardno, expiry_date2, "000",
												hsmobj, in, out);// this.generateCVV(CHN,
																	// expiry_date,
																	// "000",
																	// in, out,
																	// hsmobj);
										if (cvv2_value.length() == 2) {
											txManagerNew.rollback(pintransact.status);
											respmsg = hsmtcpip.getRespCode(cvv2_value);
											trace("Error :" + respmsg);
											addActionError(
													"Unable to Generate CVV2 for that card [" + CHN + "]..." + respmsg);
											return -1;
										}
									} else {
										cvv2_value = hsmtcpip.generateCVV_Values(dcardno, expiry_date2, "000", hsmobj,
												in, out);// this.generateCVV(CHN,
															// expiry_date,
															// "000", in, out,
															// hsmobj);
									}

								} else {

									if (hsmobj.HSMTYPE.equals("THALES")) {
										cvv2_value = hsmtcpip.generateRacalCVV_Values(dcardno, expiry_date2, "000",
												hsmobj, in, out);// this.generateCVV(CHN,
																	// expiry_date,
																	// "000",
																	// in, out,
																	// hsmobj);
										if (cvv2_value.length() == 2) {
											txManagerNew.rollback(pintransact.status);
											respmsg = hsmtcpip.getRespCode(cvv2_value);
											trace("Error :" + respmsg);
											addActionError(
													"Unable to Generate CVV2 for that card [" + CHN + "]..." + respmsg);
											return -1;
										}

									} else {
										cvv2_value = hsmtcpip.generateCVV_Values(dcardno, expiry_date2, "000", hsmobj,
												in, out);// this.generateCVV(CHN,
															// expiry_date,
															// "000",
															// in, out, hsmobj);

									}

								}

								if (cvv2_value == null || cvv2_value == "-1") {
									txManagerNew.rollback(pintransact.status);
									trace("Unable to generate cvv2 for the card number [ " + CHN
											+ " ]...got rolledback ");
									addActionError("Unable to generate cvv1 for the card number [ " + CHN + " ] ");
									return -1;
								}

								trace("Got cvv2_value : " + cvv2_value + " and length : " + cvv2_value.length());
								/*
								 * if( cvv2_value.equals("-1")){
								 * session.setAttribute("preverr", "E");
								 * session.setAttribute("prevmsg",
								 * "Unable to continue the pingneration process"
								 * ); trace(
								 * "Could not generate cvv2 value of the card [ "
								 * +CHN+" ] "); }
								 */

								trace("Icvv generating process begins for the card no [ " + CHN + " ] ");
								REQCVV = "icvv";
								trace("Generating " + REQCVV);
								//String expiry_date3 = pindao.getChnExpiryDate(instid, processtype, CHN, REQCVV,jdbctemplate);
								String expiry_date3 = pindao.getChnExpiryDate(instid, processtype, CHN, REQCVV,jdbctemplate);
								trace("CHN[" + dcardno + "], EXPDATE[" + expiry_date3 + "], SERVICECODEP[ 999 ]");

								trace("cardtypemaporchip::::::" + cardtypemaporchip + "::padssenable" + padssenable);

								trace("cardtypemaporchip.equals(C):::" + cardtypemaporchip.equals("C"));
								if (cardtypemaporchip.equals("C")) {

									System.out.println("padssenable:::::" + padssenable);

									if (padssenable.equals("Y")) {
										trace("dcard::::" + dcardno + "-----" + expiry_date3 + "-----" + "999" + "-----"
												+ hsmobj + "-----" + in + "-----" + out);

										if (hsmobj.HSMTYPE.equals("THALES")) {
											icvv_value = hsmtcpip.generateRacalCVV_Values(dcardno, expiry_date3, "999",
													hsmobj, in, out);// this.generateCVV(CHN,
																		// expiry_date,
																		// "999",
																		// in,
																		// out,
																		// hsmobj);
											if (icvv_value.length() == 2) {
												txManagerNew.rollback(pintransact.status);
												respmsg = hsmtcpip.getRespCode(icvv_value);
												trace("Error :" + respmsg);
												addActionError("Unable to Generate ICVV for that card [" + CHN + "]..."
														+ respmsg);
												return -1;
											}

										} else {
											icvv_value = hsmtcpip.generateCVV_Values(dcardno, expiry_date3, "999",
													hsmobj, in, out);// this.generateCVV(CHN,
																		// expiry_date,
																		// "999",
																		// in,
																		// out,
																		// hsmobj);
										}

									} else {
										if (hsmobj.HSMTYPE.equals("THALES")) {
											icvv_value = hsmtcpip.generateRacalCVV_Values(dcardno, expiry_date3, "999",
													hsmobj, in, out);// this.generateCVV(CHN,
																		// expiry_date,
																		// "999",
																		// in,
																		// out,
																		// hsmobj);
											if (icvv_value.length() == 2) {
												txManagerNew.rollback(pintransact.status);
												respmsg = hsmtcpip.getRespCode(icvv_value);
												trace("Error :" + respmsg);
												addActionError("Unable to Generate ICVV for that card [" + CHN + "]..."
														+ respmsg);
												return -1;
											}

										} else {
											icvv_value = hsmtcpip.generateCVV_Values(dcardno, expiry_date3, "999",
													hsmobj, in, out);// this.generateCVV(CHN,
																		// expiry_date,
																		// "999",
																		// in,
																		// out,
																		// hsmobj);

										}

									}

									trace("got icvv_value :::" + icvv_value);
									if (icvv_value == null || icvv_value == "-1") {
										txManagerNew.rollback(pintransact.status);
										trace("Unable to generate cvv2 for the card number [ " + dcardno
												+ " ]...got rolledback ");
										addActionError(
												"Unable to generate cvv1 for the card number [ " + dcardno + " ] ");
										return -1;
									}

								}

							}

							// nullify
							dcardno = "0000000000000000";
							dcardno = null;
							sb.append(dcardno);
							sb.delete(0, sb.length());

							

						}

						

						String cafrecstatus = pindao.getCafRecStatus(instid, CHN, processtype, jdbctemplate);
						trace("Getting caf rec status....got : " + cafrecstatus);
						if (cafrecstatus == null) {
							addActionError("Unable to continue the process");
							trace("Unable to get Caf Rec status for the card [ " + CHN + " ] processtype [ "
									+ processtype + " ]");
						}
						trace("Updating cvv value into database....");
						int updatecvvs = -1;
						String mcardno = "";

						if (cafrecstatus.equals("R")) {
							String apptype = (String) session.getAttribute("APPLICATIONTYPE");
							trace("Application type is : " + apptype);
							String switchstatus = commondesc.getSwitchCardStatus(instid, "05", jdbctemplate);

							updatecvvs = pindao.updatePinToProduction(instid, CHN, "05", switchstatus, apptype,
									cvv1_value, cvv2_value, icvv_value, pinoffset, jdbctemplate);

							trace("Caf Rec status is " + cafrecstatus + "...Updating to production ...got : "
									+ updatecvvs);
							trace("Updating pin values ...got : " + updatecvvs);

							trace("switchupdate " + apptype);
							int switchupdate = pindao.updatePinToSwitch(instid, hcardno.toString(), switchstatus,
									pinoffset, jdbctemplate);
							trace("switchupdate : got" + switchupdate);

							if (switchupdate != 1) {
								addActionError("Unable to continue the process");
								txManagerNew.rollback(pintransact.status);
								trace("Updating pin values into SwitchTable failed....got rolledback");
								return -1;
							}

							maintaindao.insertMainintainHistory(instid, CHN, "06", "05", usercode, jdbctemplate);

							if (updatecvvs == 1) {
								updatecvvs = pindao.deleteFromProcess(instid, CHN, processtype, hcardno, jdbctemplate);
								trace("Deleting from card process...got : " + updatecvvs);
								String mifpcode = "REPIN";
								/*
								 * Boolean allowmaintainfee =
								 * commondesc.checkFeeApplicable(instid, CHN,
								 * mifpcode , processtype, jdbctemplate);
								 * trace(mifpcode + " Fee Allowed or Not : " +
								 * allowmaintainfee ); if( allowmaintainfee ){
								 * 
								 * }
								 */

							} else {
								addActionError("Unable to continue the process");
								txManagerNew.rollback(pintransact.status);
								trace("Updating pin values into production failed....got rolledback");
								return -1;
							}

							/********************
							 * fee insert
							 ***********************/

							String F_cardno = "";
							String F_hcardno = "";
							String F_mcardno = "";
							String F_accountno = "";
							String F_caf_rec_status = "";
							String F_feeid = "";
							String Fee_cardno = "";

							if (padssenable.equals("Y")) {
								Fee_cardno = "ORG_CHN='" + CHN + "'";
							} else {
								Fee_cardno = "HCARD_NO='" + hcardno + "'";
							}

							List feedetails = commondesc.getfeedetails(instid, Fee_cardno, jdbctemplate);
							Iterator custitr = feedetails.iterator();
							while (custitr.hasNext()) {
								Map mp = (Map) custitr.next();
								F_mcardno = (String) mp.get("MCARD_NO");
								F_accountno = (String) mp.get("ACCOUNT_NO");

								F_caf_rec_status = (String) mp.get("CAF_REC_STATUS");
								F_feeid = (String) mp.get("FEE_CODE");
							}
							String acctsubtype = F_accountno.substring(6, 8);

							/*
							 * String checkvalidqry =
							 * "SELECT COUNT(1) as CNT FROM FEE_SKIP_DETAILS WHERE ACCT_SUBTYPE_ID='"
							 * + acctsubtype + "'"; String cnt = (String)
							 * jdbctemplate.queryForObject(checkvalidqry,
							 * String.class);
							 */

							// by gowtham-280819
							String checkvalidqry = "SELECT COUNT(1) as CNT FROM FEE_SKIP_DETAILS WHERE ACCT_SUBTYPE_ID=? ";
							String cnt = (String) jdbctemplate.queryForObject(checkvalidqry,
									new Object[] { acctsubtype }, String.class);

							if ("0".equalsIgnoreCase(cnt)) {

								Properties prop = getCommonDescProperty();
								String F_GLACCOUNTNO = prop.getProperty("fee.glaccountno");
								String F_TAXGLACCOUNTNO = prop.getProperty("fee.taxglaccountno");
								List checkfeedetails = commondesc.feeinsertactivity(instid, F_cardno, F_hcardno,
										F_mcardno, F_accountno, F_caf_rec_status, F_feeid, usercode, jdbctemplate);
								if (!checkfeedetails.isEmpty()) {
									trace("checkfeedetails" + checkfeedetails);
									Iterator feeitr = checkfeedetails.iterator();
									HashMap mp = (HashMap) feeitr.next();

									String actualamt = (String) mp.get("FEEAMT");
									int taxpercent = 10;
									int taxamt = (Integer.parseInt((actualamt)) / 100) * taxpercent;
									int custdebitamt = Integer.parseInt(actualamt) - taxamt;

									String custamt = String.valueOf(custdebitamt);
									String taxamount = String.valueOf(taxamt);
									String bcode = "";// F_accountno.substring(12,14);
									int checkinsertfeedet = commondesc.insertfeedetails(instid, F_cardno, F_hcardno,
											F_mcardno, F_accountno, F_caf_rec_status, F_feeid, usercode, mp, bcode,
											jdbctemplate);
									if (checkinsertfeedet != 1) {
										txManager.rollback(pintransact.status);
										trace("Fee Details rollbacked Successfully");
										session.setAttribute("preverr", "E");
										session.setAttribute("prevmsg", "Fee Details not updated ");
										// return "authcardissuehome";
									}

									int glcheckinsertfeedet = commondesc.glinsertfeedetails(instid, F_cardno, F_hcardno,
											F_mcardno, F_GLACCOUNTNO, F_caf_rec_status, F_feeid, usercode, mp, custamt,
											bcode, jdbctemplate);
									if (glcheckinsertfeedet != 1) {
										txManager.rollback(pintransact.status);
										trace("Fee Details rollbacked Successfully");
										session.setAttribute("preverr", "E");
										session.setAttribute("prevmsg", "Fee Details not updated ");
										// return "authcardissuehome";
									}
									int taxinsertfeedet = commondesc.taxinsertfeedetails(instid, F_cardno, F_hcardno,
											F_mcardno, F_TAXGLACCOUNTNO, F_caf_rec_status, F_feeid, usercode, mp,
											taxamount, bcode, jdbctemplate);
									if (taxinsertfeedet != 1) {
										txManager.rollback(pintransact.status);
										trace("Fee Details rollbacked Successfully");
										session.setAttribute("preverr", "E");
										session.setAttribute("prevmsg", "Fee Details not updated ");
										// return "authcardissuehome";
									}
								}

							}

							/*********************
							 * end fee insert
							 *****************/

							// added by gowtham_220719
							trace("ip address======>  " + ip);
							auditbean.setIpAdress(ip);

							auditbean.setActmsg("Re-Pin Generated for the card [ " + CHN + " ]");
						} else {
							/*
							 * if("IBMDES".equalsIgnoreCase(genmethod)){
							 * pinoffset = "0000"; }else{ //First digit should
							 * be PVKI value others PVV value totally five digit
							 * }
							 */
							if (pinoffset == null) {
								pinoffset = "";
							}
							// by siva 05-07-19
							/* CVV values encryption */

							/*
							 * ECVV1 = EncDec.encrypt(cvv1_value);
							 * trace("ECVV1------>" + ECVV1); ECVV2 =
							 * EncDec.encrypt(cvv2_value); trace("ECVV2------>"
							 * + ECVV2); EICVV = EncDec.encrypt(icvv_value);
							 * trace("EICVV------>" + EICVV);
							 */

							// by siva 05-07-19

							updatecvvs = pindao.updateCvvValues(instid, CHN, CARDNEXTSTATUS, processtype, mkckstatus,
									cvv1_value, cvv2_value, icvv_value, pinoffset, encryptpinblcokval, jdbctemplate);
							trace("Updating pin values ...got : " + updatecvvs);
							if (updatecvvs < 0) {
								txManagerNew.rollback(pintransact.status);
								trace("Pin generation update failed...got....rollback");
								return -1;
							}

						}

						cvv1_value = "0000000000000000";
						cvv2_value = "0000000000000000";
						icvv_value = "0000000000000000";
						cvv1_value = null;
						cvv2_value = null;
						icvv_value = null;
						sb.append(cvv1_value + cvv2_value + icvv_value);
						sb.setLength(0);

						/************* AUDIT BLOCK **************/
						try {

							// added by gowtham_220719
							trace("ip address======>  " + ip);
							auditbean.setIpAdress(ip);
							
							mcardno = "NA";

							// System.out.println("chn ----> " + CHN);
							mcardno = commondesc.getMaksedCardFromProcess(instid, CHN, jdbctemplate);
							auditbean.setActmsg("Pin Generated for the card [ " + mcardno + " ]");
							auditbean.setActiontype("IM");
							auditbean.setCardcollectbranch(BRANCH_CODE);
							auditbean.setActiontype("IM");
							auditbean.setAuditactcode("0203");
							auditbean.setCardno(mcardno);
							auditbean.setCin(cin);
							auditbean.setApplicationid(refnum);
							// auditbean.setAccoutnno(accoutnno);
							// commondesc.insertAuditTrail(instid, usercode,
							// auditbean, jdbctemplate, txManagernew);
							// System.out.println("====================" +
							// usercode);
							trace("user code is --->  user name is  " + usercode);
							commondesc.insertAuditTrailPendingCommit(instid, usercode, auditbean, jdbctemplate,
									txManagerNew);
						} catch (Exception audite) {
							audite.printStackTrace();
							audite.printStackTrace();
							trace("Exception in auditran : " + audite.getMessage());

						}
						/************* AUDIT BLOCK **************/

						txManagerNew.commit(pintransact.status);
						// trace("Cvv generating process end for the card no [ "
						// + CHN + " ]...got committed \n");
						trace("pintransact.status commit status.... :" + pintransact.status.isCompleted());

						cardcount++;
						trace("****************" + cardcount + " Card CVV generated....");
						if (cardcount == SOCKETLIMIT) {
							trace("Reached the limit : " + SOCKETLIMIT);
							connect_id.close();
							trace("Closing connection.....");
							connect_id = pindao.ConnectingHSM(hsmobj.HSMADDRESS, hsmobj.HSMPORT, hsmobj.HSMTIMEOUT);
							trace("Re-Opened the connection....");
							if (connect_id != null) {
								in = new DataInputStream(new BufferedInputStream(connect_id.getInputStream()));
								out = new DataOutputStream(new BufferedOutputStream(connect_id.getOutputStream()));
							} else {
								trace("Could not connect hsm");
								// this.removeBlockedUser(instid, branchcode,
								// jdbctemplate, delstatus);
								return -1;
							}
						}
						trace("Cvv values generated for [ " + cardcount + " ] cards....\n");
					}
				}

				dcardno = "0000000000000000";
				cvv1_value = "#####";
				cvv2_value = "$$$$";
				icvv_value = "xxxxxx";
				sb = new StringBuilder(dcardno);
				sb.append(cvv1_value);
				sb.append(cvv2_value);
				sb.append(icvv_value);
				sb.setLength(0);

			}

		} catch (Exception e) {
			e.printStackTrace();
			trace("exception in pin and cvv:::::::::::" + e.getMessage());
			out.close();
		} finally {

			// closing the streams and nullify the objects
			connect_id.close();
			out.close();
			in.close();
			props = null;
			out = null;
			in = null;
			pinoffset = null;
			hcardno = null;
			encryptpinblcokval = null;

			keyid = null;

			CDMK = null;
			CDPK = null;
			EDPK = null;

			cvv1_value = null;
			cvv2_value = null;
			icvv_value = null;
			dcardno = null;
			sb.setLength(0);
			sb = null;

			trace("Socket Closed properly");
			System.out.println("Socket closed properly....");
		}
		trace("Cvv generation completed successfully...");

		// removeBlockedUser(instid, branchcode, jdbctemplate, delstatus);
		trace("****** Generating instant card pin/cvv end  ******\n\n");
		enctrace("****** Generating instant card pin/cvv end ******\n\n");
		return 1;
	}

	/*
	 * public int pinCvvGenerationAction(String instid, String
	 * generationtypeforonlycvv, String branchcode, String card_type, String[]
	 * order_refnum, String usercode, String processtype, String mkckstatus,
	 * String bin, PlatformTransactionManager txManagerNew, JdbcTemplate
	 * jdbctemplate, TransactionStatus delstatus, HttpSession session) throws
	 * Exception { HSMParameter hsmobj; Socket connect_id = null;
	 * DataOutputStream out = null; DataInputStream in = null; String table =
	 * "INST_CARD_PROCESS"; HsmTcpIp hsmtcpip = new HsmTcpIp(); trace(
	 * "Getting the security attributes...."); hsmobj =
	 * pindao.gettingBin_details(bin, instid, jdbctemplate); if (hsmobj == null)
	 * { trace("No HSM Properties found for the bin " + bin);
	 * session.setAttribute("preverr", "E"); session.setAttribute("prevmsg",
	 * "No HSM Properties found for the bin " + bin); return -1; } trace(
	 * "Got security attributes"); trace("Getting sub product configuration...."
	 * );
	 * 
	 * List configs =
	 * commondesc.getConfigurationdetails(instid,card_type,jdbctemplate); trace(
	 * "Got configuration "+configs.size()); if(configs.isEmpty()) {
	 * //this.removeBlockedUser(instid, branchcode, jdbctemplate, delstatus);
	 * return -1; }
	 * 
	 * 
	 * trace("Connectin HSM on ip : " + hsmobj.HSMADDRESS + ", PORT : " +
	 * hsmobj.HSMPORT + "  HSMTIMEOUT " + hsmobj.HSMTIMEOUT + "PINLENGTH:" +
	 * hsmobj.PIN_LENGTH); connect_id = pindao.ConnectingHSM(hsmobj.HSMADDRESS,
	 * hsmobj.HSMPORT, hsmobj.HSMTIMEOUT); if (connect_id != null) { trace(
	 * "Connection socket object ... : " + connect_id); in = new
	 * DataInputStream(new BufferedInputStream(connect_id.getInputStream()));
	 * out = new DataOutputStream(new
	 * BufferedOutputStream(connect_id.getOutputStream())); } else { trace(
	 * "Could not connect hsm"); ////// this.removeBlockedUser(instid,
	 * branchcode, jdbctemplate, ////// delstatus); return -1; }
	 * 
	 * trace("Iterating cards one by one...."); trace(" CVV Required check : " +
	 * hsmobj.CVV_REQUIRED); //strace(
	 * "The configured length of the cvv value [ " + hsmobj.CVV_LENGTH + " ]");
	 * // trace( " CVK1 : " + hsmobj.CVVK1); // trace( " CVK2 : " +
	 * hsmobj.CVVK2);
	 * 
	 * // getting dec cardno
	 * 
	 * String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
	 * 
	 * String keyid = "", dcardno = "", CDMK = "", CDPK=""; String EDPK="";
	 * Properties props=getCommonDescProperty(); EDPK=props.getProperty("EDPK");
	 * PadssSecurity padsssec = new PadssSecurity(); if
	 * (padssenable.equals("Y")) {
	 * 
	 * keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
	 * System.out.println("keyid::" + keyid); List secList =
	 * commondesc.getPADSSDetailById(keyid, jdbctemplate);
	 * System.out.println("secList::" + secList); Iterator secitr =
	 * secList.iterator(); if (!secList.isEmpty()) { while (secitr.hasNext()) {
	 * Map map = (Map) secitr.next(); CDMK = ((String) map.get("DMK")); //eDPK =
	 * ((String) map.get("DPK")); } } }
	 * 
	 * //getting clear DPK by passing clear dmk,iv,EDPK values
	 * CDPK=padsssec.decryptDPK(CDMK, EDPK);
	 * 
	 * /// String pinoffset = null, hcardno = "", encryptpinblcokval = "";
	 * 
	 * // GEN_METHOD added for Orient Bank Requirements // Its to update pin
	 * offset for VISA And IBMDES List getsecDetils =
	 * commondesc.getSequritygenerationdetails(instid, bin, jdbctemplate);
	 * Iterator secitr = getsecDetils.iterator(); String cardtypemaporchip = "",
	 * gentype = "", genmethod = "", BRANCH_CODE = ""; while (secitr.hasNext())
	 * { Map secmap = (Map) secitr.next();
	 * 
	 * cardtypemaporchip = (String) secmap.get("CARD_TYPE"); trace("TTTTTTTTTTT"
	 * + cardtypemaporchip); gentype = (String) secmap.get("SEQ_OPTION");
	 * genmethod = (String) secmap.get("GEN_METHOD"); }
	 * 
	 * try { trace("order_refnum.length total---->" + order_refnum.length);
	 * enctrace("order_refnum.length total---->" + order_refnum.length);
	 * 
	 * for (int i = 0; i < order_refnum.length; i++) { String pin_req = "X";
	 * trace("order_refnum one by one---->" + order_refnum[i]); enctrace(
	 * "order_refnum one by one---->" + i); // String CHN =
	 * order_refnum[i].toString().trim(); String refnum =
	 * order_refnum[i].toString().trim(); auditbean.setApplicationid(refnum);
	 * List chnlist = commondesc.getCardsFromOrder(instid, refnum, processtype,
	 * "P", "01", jdbctemplate);
	 * 
	 * // we are passing ORG_CHN Value instead of Encrypted card number
	 * 
	 * if (!chnlist.isEmpty()) { Iterator itr = chnlist.iterator(); int
	 * cardcount = 0; while (itr.hasNext()) { Map temp = (Map) itr.next();
	 * 
	 * card_type = (String) temp.get("PRODUCT_CODE"); String CHN = (String)
	 * temp.get("ORG_CHN"); // hcardno = (String)temp.get("HCARD_NO");
	 * BRANCH_CODE = (String) temp.get("BRANCH_CODE");
	 * 
	 * if (padssenable.equals("Y")) { dcardno = padsssec.getCHN(CDPK, CHN); }
	 * 
	 * 
	 * // dcardno means de-crypted card number // trace("Processing....." + CHN
	 * ); String cin = commondesc.getCustomerIdByCardNumberProcess(instid, CHN,
	 * processtype, jdbctemplate); trace("Getting customer id ...got : " + cin);
	 * IfpTransObj pintransact = commondesc.myTranObject("PIN" + cardcount,
	 * txManagerNew);
	 * 
	 * trace("gentype-----:::" + gentype); System.out.println("gentype-----:::"
	 * + gentype);
	 * 
	 * HsmTcpIp hsmtcp = new HsmTcpIp(); trace(
	 * "Pin generating process begins for the card no [ " + CHN + " ] ");
	 * 
	 * // if( gentype == null ){ // addActionError("Pin Generation Required Not
	 * // Configured For the subproduct [ "+subproduct+" ] "); // return -1; //
	 * }
	 * 
	 * // remove "NO" for Pin AND CVV Generation
	 * 
	 * if (gentype.equals("PC") && generationtypeforonlycvv.equals("NO") ||
	 * gentype.equals("P") && generationtypeforonlycvv.equals("NO")) {
	 * 
	 * trace("###########################pin printing #############");
	 * 
	 * 
	 * pinoffset = hsmtcp.printRacalPin(instid,CHN,dcardno, cin, card_type, bin,
	 * processtype, in, out, jdbctemplate, hsmobj); trace(
	 * "Generated pin command...got : " + pinoffset);
	 * 
	 * 
	 * if( pinoffsetresp == null ){ txManagerNew.rollback(pintransact.status);
	 * trace( "Could not generate pin..1...got rolledback"); return -1; } trace(
	 * "getting pin offset value " + pinoffsetresp); String pinoffsetattr[] =
	 * pinoffsetresp.split("~"); if( pinoffsetattr[1].equals("0")){
	 * 
	 * pinoffset =pinoffsetattr[0]; trace( "getting pin offset value " +
	 * pinoffset); }else{ session.setAttribute("preverr", "E");
	 * session.setAttribute("prevmsg", pinoffsetattr[0]);
	 * addActionError(pinoffsetattr[0]);
	 * txManagerNew.rollback(pintransact.status); trace(
	 * "Could not generate pin..2..got rolledback"); return -1; }
	 * 
	 * 
	 * }
	 * 
	 * trace("");
	 * 
	 * String cvv1_value = null; String cvv2_value = null; String icvv_value =
	 * null;
	 * 
	 * if (hsmobj.CVV_REQUIRED.equals("Y")) {
	 * 
	 * if (gentype.equals("C") || gentype.equals("PC")) { String expiry_date =
	 * null; String REQCVV = null; String servicecode =
	 * commondesc.getServiceCodeByChn(instid, CHN, processtype, jdbctemplate);
	 * 
	 * trace("Cvv1 generating process begins for the card no [ " + CHN + " ] ");
	 * REQCVV = "cvv1"; trace("Generating " + REQCVV); expiry_date =
	 * pindao.getChnExpiryDate(instid, processtype, CHN, REQCVV, jdbctemplate);
	 * trace("CHN[" + CHN + "], EXPDATE[" + expiry_date + "], SERVICECODEP[" +
	 * servicecode + "]");
	 * 
	 * if (padssenable.equals("Y")) { cvv1_value =
	 * hsmtcpip.generateRacalCVV_Values(dcardno, expiry_date, servicecode,
	 * hsmobj, in, out); // this.generateCVV(CHN, // expiry_date, //
	 * servicecode, // in, out, // hsmobj); } else { cvv1_value =
	 * hsmtcpip.generateRacalCVV_Values(CHN, expiry_date, servicecode, hsmobj,
	 * in, out); // this.generateCVV(CHN, // expiry_date, // servicecode, in,
	 * out, // hsmobj); }
	 * 
	 * if (cvv1_value == null) { txManagerNew.rollback(pintransact.status);
	 * trace("Unable to generate cvv1 for the card number [ " + CHN +
	 * " ]...got rolledback "); addActionError(
	 * "Unable to generate cvv1 for the card number [ " + CHN + " ] "); return
	 * -1; }
	 * 
	 * trace("Got cvv1_value  length : " + cvv1_value.length());
	 * 
	 * 
	 * if( cvv1_value.equals("-1") ){ session.setAttribute("preverr", "E");
	 * session.setAttribute("prevmsg",
	 * "Unable to continue the pingneration process" ); trace(
	 * "Could not generate cvv1 value of the card [ " +CHN+" ] "); }
	 * 
	 * 
	 * trace("Cvv2 generating process begins for the card no [ " + CHN + " ] ");
	 * REQCVV = "cvv2"; trace("Generating " + REQCVV); expiry_date =
	 * pindao.getChnExpiryDate(instid, processtype, CHN, REQCVV, jdbctemplate);
	 * trace("CHN[" + CHN + "], EXPDATE[" + expiry_date + "], SERVICECODEP[000]"
	 * );
	 * 
	 * if (padssenable.equals("Y")) { cvv2_value =
	 * hsmtcpip.generateRacalCVV_Values(dcardno, expiry_date, "000", hsmobj, in,
	 * out);// this.generateCVV(CHN, // expiry_date, "000", // in, out, hsmobj);
	 * } else { cvv2_value = hsmtcpip.generateRacalCVV_Values(CHN, expiry_date,
	 * "000", hsmobj, in, out);// this.generateCVV(CHN, // expiry_date, "000",
	 * in, // out, hsmobj); }
	 * 
	 * if (cvv2_value == null) { txManagerNew.rollback(pintransact.status);
	 * trace("Unable to generate cvv2 for the card number [ " + CHN +
	 * " ]...got rolledback "); addActionError(
	 * "Unable to generate cvv1 for the card number [ " + CHN + " ] "); return
	 * -1; }
	 * 
	 * trace("Got cvv2_value :  length : " + cvv2_value.length());
	 * 
	 * 
	 * if( cvv2_value.equals("-1")){ session.setAttribute("preverr", "E");
	 * session.setAttribute("prevmsg",
	 * "Unable to continue the pingneration process" ); trace(
	 * "Could not generate cvv2 value of the card [ " +CHN+" ] "); }
	 * 
	 * 
	 * trace("Icvv generating process begins for the card no [ " + CHN + " ] ");
	 * REQCVV = "icvv"; trace("Generating " + REQCVV); expiry_date =
	 * pindao.getChnExpiryDate(instid, processtype, CHN, REQCVV, jdbctemplate);
	 * trace("CHN[" + CHN + "], EXPDATE[" + expiry_date +
	 * "], SERVICECODEP[ 999 ]"); trace("cardtypemaporchip::::::" +
	 * cardtypemaporchip + "::padssenable" + padssenable);
	 * 
	 * trace("cardtypemaporchip.equals(C):::" + cardtypemaporchip.equals("C"));
	 * trace("TTTTTTTTTTT-1" + cardtypemaporchip); if
	 * (cardtypemaporchip.equals("C")) { trace("TTTTTTTTTTT-2" +
	 * cardtypemaporchip); System.out.println("padssenable:::::" + padssenable);
	 * 
	 * if (padssenable.equals("Y")) { trace("TTTTTTTTTTT-3" +
	 * cardtypemaporchip); trace("dcard::::" + dcardno + "-----" + expiry_date +
	 * "-----" + "999" + "-----" + hsmobj + "-----" + in + "-----" + out);
	 * icvv_value = hsmtcpip.generateRacalCVV_Values(dcardno, expiry_date,
	 * "999", hsmobj, in, out);// this.generateCVV(CHN, // expiry_date, //
	 * "999", // in, out, // hsmobj); } else { icvv_value =
	 * hsmtcpip.generateRacalCVV_Values(CHN, expiry_date, "999", hsmobj, in,
	 * out);// this.generateCVV(CHN, // expiry_date, // "999", in, out, //
	 * hsmobj); }
	 * 
	 * //trace("got icvv_value :::" + icvv_value); if (icvv_value == null) {
	 * txManagerNew.rollback(pintransact.status); trace(
	 * "Unable to generate cvv2 for the card number [ " + CHN +
	 * " ]...got rolledback "); addActionError(
	 * "Unable to generate cvv1 for the card number [ " + CHN + " ] "); return
	 * -1; }
	 * 
	 * }
	 * 
	 * 
	 * pinoffset = hsmtcp.printRacalPin(instid,CHN,dcardno, cin, card_type, bin,
	 * processtype, in, out, jdbctemplate, hsmobj); trace(
	 * "Generated pin command...got : " + pinoffset);
	 * 
	 * String output[] = pinoffset.split("~");
	 * 
	 * 
	 * pinoffset = "000000"; encryptpinblcokval = "0000000000000000";
	 * 
	 * } }
	 * 
	 * if( icvv_value.equals("-1") ){ session.setAttribute("preverr", "E");
	 * session.setAttribute("prevmsg",
	 * "Unable to continue the pingneration process" ); trace(
	 * "Could not generate icvv value of the card [ " +CHN+" ] "); }
	 * 
	 * 
	 * 
	 * ///************** PIN VERIFICATION trace( "Pin verification initiated");
	 * String storedpinoffset ="8529";// storedpinoffset =
	 * CommonDesc.padRightChar(storedpinoffset, 12, "0"); String pinblock =
	 * "A8DAAFD21B5F6DFE"; String ppk = "BC6A9B4080CD5F58020DCF33D1121F22";
	 * String epvk = hsmobj.PVK; String pinvalid =
	 * hsmtcp.pinVerifiCationInit("4660421004230075", storedpinoffset, pinblock,
	 * ppk, epvk, in, out); trace("Pin verifcation result : " + pinvalid );
	 * 
	 * ///************** PIN VERIFICATION
	 * 
	 * ///**********GENERATING SESSION KEY trace( "Generating session key " );
	 * String sessionkey = hsmtcp.generateSessionKey(in, out); trace(
	 * "session key : " + sessionkey );
	 * 
	 * ///**********GENERATING SESSION KEY
	 * 
	 * 
	 * // String subproduct = // commondesc.getSubProductByCHNProcess(instid,
	 * CHN, // processtype, jdbctemplate); // trace(
	 * "Getting Sub Product....got : " + subproduct ); /// String haspinreq = //
	 * commondesc.checkPinGenRequired(instid, subproduct, // jdbctemplate); //
	 * trace("Pin Required For This Sub-Product : " + // haspinreq );
	 * 
	 * // if( gentype == null ){ // addActionError("Pin Generation Required Not
	 * // Configured For the subproduct [ "+subproduct+" ] "); // return -1; //
	 * }
	 * 
	 * trace("gentype---testing--:::" + gentype);
	 * 
	 * // if( haspinreq.equals("Y")){
	 * 
	 * // if( gentype.equals("P") || gentype.equals("PC")){
	 * 
	 * // }
	 * 
	 * // else{ // pinoffset = "0000"; // }
	 * 
	 * String cafrecstatus = pindao.getCafRecStatus(instid, CHN, processtype,
	 * jdbctemplate); trace("Getting caf rec status....got : " + cafrecstatus);
	 * if (cafrecstatus == null) { addActionError(
	 * "Unable to continue the process"); trace(
	 * "Unable to get Caf Rec status for the card [ " + CHN +
	 * " ] processtype [ " + processtype + " ]"); } trace(
	 * "Updating cvv value into database...."); int updatecvvs = -1; String
	 * mcardno = "";
	 * 
	 * if (cafrecstatus.equals("R")) { String apptype = (String)
	 * session.getAttribute("APPLICATIONTYPE"); trace("Application type is : " +
	 * apptype); String switchstatus = commondesc.getSwitchCardStatus(instid,
	 * "05", jdbctemplate);
	 * 
	 * // by siva 05-07-19
	 * 
	 * String ECVV1 = EncDec.encrypt(cvv1_value); trace("ECVV1------>"+ECVV1);
	 * String ECVV2 = EncDec.encrypt(cvv2_value); trace("ECVV2------>"+ECVV2);
	 * String EICVV = EncDec.encrypt(icvv_value); trace("EICVV------>"+EICVV);
	 * 
	 * // by siva 05-07-19
	 * 
	 * updatecvvs = pindao.updatePinToProduction(instid, CHN, "05",
	 * switchstatus, apptype,ECVV1, ECVV2, EICVV, pinoffset, jdbctemplate);
	 * trace("Caf Rec status is " + cafrecstatus +
	 * "...Updating to production ...got : " + updatecvvs); trace("Update");
	 * 
	 * trace("switchupdate " + apptype); int switchupdate =
	 * pindao.updatePinToSwitch(instid, hcardno.toString(), switchstatus,
	 * pinoffset, jdbctemplate); trace("switchupdate : got" + switchupdate); if
	 * (switchupdate != 1) { addActionError("Unable to continue the process");
	 * txManagerNew.rollback(pintransact.status); trace(
	 * "Updating pin values into SwitchTable failed....got rolledback"); return
	 * -1; }
	 * 
	 * CardMaintainActionDAO maintaindao = new CardMaintainActionDAO();
	 * maintaindao.insertMainintainHistory(instid, CHN, "06", "05", usercode,
	 * jdbctemplate);
	 * 
	 * if (updatecvvs == 1) { updatecvvs = pindao.deleteFromProcess(instid, CHN,
	 * processtype, jdbctemplate); trace("Deleting from card process...got : " +
	 * updatecvvs); String mifpcode = "REPIN";
	 * 
	 * Boolean allowmaintainfee = commondesc.checkFeeApplicable(instid, CHN,
	 * mifpcode , processtype, jdbctemplate); trace(mifpcode +
	 * " Fee Allowed or Not : " + allowmaintainfee ); if( allowmaintainfee ){
	 * 
	 * }
	 * 
	 * 
	 * } else { addActionError("Unable to continue the process");
	 * txManagerNew.rollback(pintransact.status); trace(
	 * "Updating pin values into production failed....got rolledback"); return
	 * -1; }
	 * 
	 *//********************
		 * fee insert
		 ***********************/
	/*
	 * 
	 * String F_cardno = ""; String F_hcardno = ""; String F_mcardno = "";
	 * String F_accountno = ""; String F_caf_rec_status = ""; String F_feeid =
	 * ""; String Fee_cardno = ""; if (padssenable.equals("Y")) { Fee_cardno =
	 * "HCARD_NO='" + hcardno + "'"; } else { Fee_cardno = "ORG_CHN='" + CHN +
	 * "'"; }
	 * 
	 * List feedetails = commondesc.getfeedetails(instid, Fee_cardno,
	 * jdbctemplate); Iterator custitr = feedetails.iterator(); while
	 * (custitr.hasNext()) { Map mp = (Map) custitr.next(); F_cardno = (String)
	 * mp.get("CARD_NO"); F_hcardno = (String) mp.get("HCARD_NO"); F_mcardno =
	 * (String) mp.get("MCARD_NO"); F_accountno = (String) mp.get("ACCOUNT_NO");
	 * 
	 * F_caf_rec_status = (String) mp.get("CAF_REC_STATUS"); F_feeid = (String)
	 * mp.get("FEE_CODE"); } String acctsubtype = F_accountno.substring(6, 8);
	 * String checkvalidqry =
	 * "SELECT COUNT(1) as CNT FROM FEE_SKIP_DETAILS WHERE ACCT_SUBTYPE_ID='" +
	 * acctsubtype + "'"; String cnt = (String)
	 * jdbctemplate.queryForObject(checkvalidqry, String.class); if
	 * ("0".equalsIgnoreCase(cnt)) {
	 * 
	 * Properties prop = getCommonDescProperty(); String F_GLACCOUNTNO =
	 * prop.getProperty("fee.glaccountno"); String F_TAXGLACCOUNTNO =
	 * prop.getProperty("fee.taxglaccountno"); List checkfeedetails =
	 * commondesc.feeinsertactivity(instid, F_cardno, F_hcardno, F_mcardno,
	 * F_accountno, F_caf_rec_status, F_feeid, usercode, jdbctemplate); if
	 * (!checkfeedetails.isEmpty()) { trace("checkfeedetails" +
	 * checkfeedetails); Iterator feeitr = checkfeedetails.iterator(); HashMap
	 * mp = (HashMap) feeitr.next();
	 * 
	 * String actualamt = (String) mp.get("FEEAMT"); int taxpercent = 10; int
	 * taxamt = (Integer.parseInt((actualamt)) / 100) * taxpercent; int
	 * custdebitamt = Integer.parseInt(actualamt) - taxamt;
	 * 
	 * String custamt = String.valueOf(custdebitamt); String taxamount =
	 * String.valueOf(taxamt); String bcode = "";//
	 * F_accountno.substring(12,14); int checkinsertfeedet =
	 * commondesc.insertfeedetails(instid, F_cardno, F_hcardno, F_mcardno,
	 * F_accountno, F_caf_rec_status, F_feeid, usercode, mp, bcode,
	 * jdbctemplate); if (checkinsertfeedet != 1) {
	 * txManager.rollback(pintransact.status); trace(
	 * "Fee Details rollbacked Successfully"); session.setAttribute("preverr",
	 * "E"); session.setAttribute("prevmsg", "Fee Details not updated "); //
	 * return "authcardissuehome"; }
	 * 
	 * int glcheckinsertfeedet = commondesc.glinsertfeedetails(instid, F_cardno,
	 * F_hcardno, F_mcardno, F_GLACCOUNTNO, F_caf_rec_status, F_feeid, usercode,
	 * mp, custamt, bcode, jdbctemplate); if (glcheckinsertfeedet != 1) {
	 * txManager.rollback(pintransact.status); trace(
	 * "Fee Details rollbacked Successfully"); session.setAttribute("preverr",
	 * "E"); session.setAttribute("prevmsg", "Fee Details not updated "); //
	 * return "authcardissuehome"; } int taxinsertfeedet =
	 * commondesc.taxinsertfeedetails(instid, F_cardno, F_hcardno, F_mcardno,
	 * F_TAXGLACCOUNTNO, F_caf_rec_status, F_feeid, usercode, mp, taxamount,
	 * bcode, jdbctemplate); if (taxinsertfeedet != 1) {
	 * txManager.rollback(pintransact.status); trace(
	 * "Fee Details rollbacked Successfully"); session.setAttribute("preverr",
	 * "E"); session.setAttribute("prevmsg", "Fee Details not updated "); //
	 * return "authcardissuehome"; } }
	 * 
	 * }
	 * 
	 *//*********************
		 * end fee insert
		 *****************/
	/*
	 * 
	 * auditbean.setActmsg("Re-Pin Generated for the card [ " + CHN + " ]"); }
	 * else {
	 * 
	 * if("IBMDES".equalsIgnoreCase(genmethod)){ pinoffset = "0000"; }else{
	 * //First digit should be PVKI value others PVV value totally five digit }
	 * 
	 * if (pinoffset == null) { pinoffset = ""; } // by siva 05-07-19
	 * 
	 * String ECVV1 = EncDec.encrypt(cvv1_value); trace("ECVV1------>"+ECVV1);
	 * String ECVV2 = EncDec.encrypt(cvv2_value); trace("ECVV2------>"+ECVV2);
	 * String EICVV = EncDec.encrypt(icvv_value); trace("EICVV------>"+EICVV);
	 * 
	 * // by siva 05-07-19
	 * 
	 * updatecvvs = pindao.updateCvvValues(instid, CHN, CARDNEXTSTATUS,
	 * processtype, mkckstatus, ECVV1, ECVV2, EICVV, pinoffset,
	 * encryptpinblcokval, jdbctemplate); trace("Updating pin values ...got : "
	 * + updatecvvs); if (updatecvvs < 0) {
	 * txManagerNew.rollback(pintransact.status); trace(
	 * "Pin generation update failed...got....rollback"); return -1; }
	 * 
	 * }
	 * 
	 *//************* AUDIT BLOCK **************/

	/*
	 * try { // mcardno = commondesc.getMaskedCardbyproc(instid, //
	 * hcardno.toString(),table,"H", jdbctemplate); if (mcardno == null) {
	 * mcardno = CHN; } auditbean.setActmsg("Pin Generated for the card [ " +
	 * mcardno + " ]"); auditbean.setActiontype("IM"); //
	 * auditbean.setUsercode(comUsername());
	 * auditbean.setCardcollectbranch(BRANCH_CODE);
	 * auditbean.setActiontype("IM"); auditbean.setAuditactcode("0203");
	 * auditbean.setCardno(mcardno); // commondesc.insertAuditTrail(instid,
	 * usercode, // auditbean, jdbctemplate, txManagernew);
	 * System.out.println("===================="+usercode); trace(
	 * "user code is --->  user name is  "+ usercode);
	 * commondesc.insertAuditTrailPendingCommit(instid, usercode, auditbean,
	 * jdbctemplate, txManagerNew); } catch (Exception audite) {
	 * audite.printStackTrace(); trace("Exception in auditran : " +
	 * audite.getMessage()); audite.printStackTrace(); }
	 *//************* AUDIT BLOCK **************//*
												 * 
												 * txManagerNew.commit(
												 * pintransact.status); trace(
												 * "Cvv generating process end for the card no [ "
												 * + CHN +
												 * " ]...got committed \n");
												 * trace(
												 * "pintransact.status commit status.... :"
												 * + pintransact.status.
												 * isCompleted());
												 * 
												 * cardcount++;
												 * trace("****************" +
												 * cardcount +
												 * " Card CVV generated....");
												 * if (cardcount == SOCKETLIMIT)
												 * { trace(
												 * "Reached the limit : " +
												 * SOCKETLIMIT);
												 * connect_id.close(); trace(
												 * "Closing connection.....");
												 * connect_id =
												 * pindao.ConnectingHSM(hsmobj.
												 * HSMADDRESS, hsmobj.HSMPORT,
												 * hsmobj.HSMTIMEOUT); trace(
												 * "Re-Opened the connection...."
												 * ); if (connect_id != null) {
												 * in = new DataInputStream(new
												 * BufferedInputStream(
												 * connect_id.getInputStream()))
												 * ; out = new
												 * DataOutputStream(new
												 * BufferedOutputStream(
												 * connect_id.getOutputStream())
												 * ); } else { trace(
												 * "Could not connect hsm"); //
												 * this.removeBlockedUser(
												 * instid, branchcode, //
												 * jdbctemplate, delstatus);
												 * return -1; } }
												 * 
												 * trace(
												 * "Cvv values generated for [ "
												 * + cardcount +
												 * " ] cards....\n"); }
												 * 
												 * } }
												 * 
												 * } catch (Exception e) {
												 * System.out.println(e.
												 * getMessage());
												 * e.printStackTrace(); trace(
												 * "exception in pin and cvv:::::::::::"
												 * + e); out.close(); } finally
												 * {
												 * 
												 * connect_id.close();
												 * out.close(); in.close();
												 * trace(
												 * "Socket Closed properly");
												 * System.out.println(
												 * "Socket closed properly...."
												 * ); } trace(
												 * "Cvv generation completed successfully..."
												 * );
												 * 
												 * // removeBlockedUser(instid,
												 * branchcode, jdbctemplate,
												 * delstatus); trace(
												 * "****** Generating instant card pin/cvv end  ******\n\n"
												 * ); enctrace(
												 * "****** Generating instant card pin/cvv end ******\n\n"
												 * ); return 1; }
												 */
	/*
	 * public String generateCVV(String CHN, String expirydate, String
	 * servicecode, DataInputStream in, DataOutputStream out, HSMParameter
	 * hsmobj) throws Exception { return "123"; }
	 */

	public String generateCVV(String CHN, String expirydate, String servicecode, DataInputStream in,
			DataOutputStream out, HSMParameter hsmobj) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		String CVK = (hsmobj.CVVK1 + hsmobj.CVVK2).toUpperCase();
		trace("CVK value is : " + CVK);
		String Message = "";
		outputStream = pindao.eracom_out("01", 2, "length1", 1, outputStream);
		outputStream = pindao.eracom_out("01", 2, "length2", 1, outputStream);
		outputStream = pindao.eracom_out("0001", 4, "length3", 1, outputStream);
		String val = "0026";
		outputStream.write(pindao.ascii_to_bcd(val, 2));
		System.out.println("OUTPUT STRAEM ---- > " + outputStream);
		pindao.ComposeCVV(CVK, CHN, expirydate, servicecode, outputStream);
		pindao.converttoString(Message);
		trace("----request begin------");
		trace(new String(Hex.encodeHex(outputStream.toByteArray())));
		trace("----request end ------");
		out.write(outputStream.toByteArray());
		byte[] response = new byte[18];
		/* in.read(response); */
		trace("----response begin ------");
		trace(new String(Hex.encodeHex(response)));
		trace("----response end------");
		String rsp = new String(Hex.encodeHex(response));
		String cvv_value = pindao.getCVVResponse(rsp);
		outputStream.flush();
		outputStream.close();
		return cvv_value;

	}

	public void removeBlockedUser(String instid, String branchcode, JdbcTemplate jdbctemplate,
			TransactionStatus delstatus) throws Exception {
		trace("Removing processed pin generation processed user....");

		int x = pindao.deletePingenerationStatus(instid, branchcode, jdbctemplate);
		if (x < 0) {
			txManager.rollback(delstatus);
			trace("Could not delete pin gen process...");
		}
		txManager.commit(delstatus);
		trace("Pin gen process record Deleted successfully");
	}

	public String InstauthPingenerationhome() {
		List pers_prodlist = null, br_list = null;
		String inst_id = comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		HttpSession session = getRequest().getSession();

		String temp = act;
		System.out.println(temp);

		session.setAttribute("PINGEN_ACT", act);
		String cardStatus = "02", mkrstatus = "M";
		String session_act = (String) session.getAttribute("PINGEN_ACT");
		System.out.println("session_act " + session_act);
		try {
			System.out.println("Inst Id===>" + inst_id + "  Branch Code ===>" + branch);
			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = commondesc.InstgetBranchCodefmProcess(inst_id, cardStatus, mkrstatus, jdbctemplate);
				System.out.println("Branch list " + br_list);
				if (!(br_list.isEmpty())) {
					setBranchlist(br_list);
					System.out.println("Branch list is not empty");
					// setCardgenstatus('Y');
				} else {
					addActionError("No Cards Waiting For Security Data Generation ... ");
					System.out.println("Branch List is empty ");
					return "required_home";

				}
			}
			pers_prodlist = commondesc.InstgetProductListBySelected(inst_id, cardStatus, mkrstatus, jdbctemplate);

			if (!(pers_prodlist.isEmpty())) {
				setInstpersonalproductlist(pers_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "");
				System.out.println("Product List is ===> " + pers_prodlist);
				// setCardgenstatus('Y');
			} else {
				System.out.println("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", " No Product Details Found ");
				// setCardgenstatus('N');
			}

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Error While Fetching The Product Details  " + e.getMessage());
			// setCardgenstatus('N');
		}

		return "instpinauth_home";
	}

	public String viewPinAuthList() {
		HttpSession session = getRequest().getSession();
		System.out.println("coming into view auth pin list area");
		try {
			String branch = getRequest().getParameter("branchcode");
			String cardtype = getRequest().getParameter("cardtype");
			String fromdate = getRequest().getParameter("fromdate");
			String todate = getRequest().getParameter("todate");
			String instid = comInstId();
			String cardstatus = "02";
			String mkckstatus = "M";
			System.out.println("Branch====> " + branch + " Card Type====> " + cardtype + " From Date ====> " + fromdate
					+ "  To Date ====> " + todate);
			List authcardorder = null;
			String dateflag = "PIN_DATE";
			String condition = commondesc.filterCondition(cardtype, branch, fromdate, todate, dateflag);
			authcardorder = this.InstpersonaliseCardauthlist(instid, cardstatus, mkckstatus, condition, jdbctemplate);
			System.out.println("authcardorder=====>" + authcardorder);
			if (authcardorder.isEmpty()) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "No Orders Found");
				return InstauthPingenerationhome();
			} else {
				setInstperspingenauthlist(authcardorder);
				session.setAttribute("curerr", "S");
			}
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Error :" + e.getMessage());
		}

		return "instauthdpin_list";
	}

	public List InstpersonaliseCardauthlist(String instid, String cardstatus, String mkckstatus, String condition,
			JdbcTemplate jdbctemplate) throws Exception {

		/*
		 * String authcards_query =
		 * "select distinct(cp.order_ref_no) as ORDER_REF_NO,to_char(cp."
		 * +dateflag+
		 * ",'DD-MON-YYYY') as GENDATE,ud.USERNAME as USERNAME,co.card_quantity as COUNT,co.card_type_id as CARDID,co.sub_prod_id as SUBID,"
		 * +
		 * "co.product_code as PCODE,co.BRANCH_CODE as BRANCH_CODE,co.BIN as BIN,pd.product_name as PNAME from PERS_CARD_PROCESS cp,PERS_CARD_ORDER co,INSTPROD_DETAILS pd, USER_DETAILS ud "
		 * + "where cp.inst_id='"+instid+"' and cp.card_status ='"+cardstatus+
		 * "' and cp.mkck_status='"+mkckstatus+"' and cp.branch_code='"+brcode+
		 * "' and cp.bin='"+bin+
		 * "' and ud.userid = cp.maker_id and cp.order_ref_no = co.order_ref_no and "
		 * +
		 * "cp.inst_id = co.inst_id and  co.inst_id = pd.inst_id and cp.product_code = co.product_code and co.product_code = pd.product_code and "
		 * + "(to_date('"+fromdate+"','dd-mm-yyyy') <= cp."+dateflag+
		 * ") AND (to_date('"+todate+"','dd-mm-yyyy')+1 >= cp."+dateflag+
		 * ") order  by cp.order_ref_no";
		 * 
		 * 
		 * String authcards_query =
		 * "select ORDER_REF_NO,count(*) as COUNT,CART_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,MAKER_ID,to_char("
		 * +dateflag+",'DD-MON-YYYY') as  GENDATE," +
		 * "BIN from PERS_CARD_PROCESS where inst_id='"+instid+
		 * "' and CARD_STATUS='"+cardstatus+"' and MKCK_STATUS='"+mkckstatus+
		 * "' and (to_date('"+fromdate+"','dd-mm-yyyy') <= "+dateflag+
		 * ") AND (to_date('"+todate+"','dd-mm-yyyy')+1 >= "+dateflag+
		 * ") and bin='"+bin+"' " +
		 * "group by order_ref_no,CART_TYPE_ID, sub_prod_id, product_code,MAKER_ID,to_char("
		 * +dateflag+",'DD-MON-YYYY'),bin order by order_ref_no";
		 */

		/*
		 * String pingen = (String)session.getAttribute("PINGEN_TYPE"); trace(
		 * " PinGen Type-----> "+pingen);
		 */

		// String field_1="CARD_NO",orderflag="CARD_NO";

		/*
		 * String select_query =
		 * "select distinct(order_ref_no) as ORDER_REF_NO, CIN, CARD_NO,HCARD_NO,MCARD_NO,ACCT_NO, bin,card_type_id as CARDTYPE,sub_prod_id as SUBPRODID,product_code,EMB_NAME,to_char(generated_date,'DD-MON-YY') as GENDATE,MAKER_ID,branch_code,MOBILENO,"
		 * +
		 * " (SELECT USERNAME  FROM USER_DETAILS WHERE  USERID=INST_CARD_PROCESS.CHECKER_ID) AS CHECKER,(SELECT BRANCH_NAME FROM BRANCH_MASTER WHERE BRANCH_CODE=INST_CARD_PROCESS.BRANCH_CODE) AS CARD_COLLECT_BRANCH  from INST_CARD_PROCESS "
		 * + "where inst_id='" + instid + "' and CARD_STATUS='" + cardstatus +
		 * "' and mkck_status='" + mkckstatus + "'" + condition +
		 * " order by order_ref_no";
		 */

		/*
		 * String select_query =
		 * "select distinct(order_ref_no) as ORDER_REF_NO, CIN,MCARD_NO,ACCT_NO, bin,card_type_id as CARDTYPE,sub_prod_id as SUBPRODID,product_code,EMB_NAME,to_char(generated_date,'DD-MON-YY') as GENDATE,MAKER_ID,branch_code,MOBILENO,"
		 * +
		 * " (SELECT USERNAME  FROM USER_DETAILS WHERE  USERID=INST_CARD_PROCESS.CHECKER_ID) AS CHECKER,(SELECT BRANCH_NAME FROM BRANCH_MASTER WHERE BRANCH_CODE=INST_CARD_PROCESS.BRANCH_CODE) AS CARD_COLLECT_BRANCH  from INST_CARD_PROCESS "
		 * + "where inst_id='" + instid + "' and CARD_STATUS='" + cardstatus +
		 * "' and mkck_status='" + mkckstatus + "'" + condition +
		 * " order by order_ref_no"; // AND CAF_REC_STATUS in ('S','A') This was
		 * added for maintenance // activity //// enctrace(
		 * "3030authcards_query : "+select_query); enctrace(
		 * "checking select query for authlist" + select_query); List
		 * instorderlist = jdbctemplate.queryForList(select_query);
		 */

		/// BY GOWTHAM-280819
		String select_query = "select distinct(order_ref_no) as ORDER_REF_NO, CIN,MCARD_NO,ACCT_NO, bin,card_type_id as CARDTYPE,sub_prod_id as SUBPRODID,product_code,EMB_NAME,to_char(generated_date,'DD-MON-YY') as GENDATE,MAKER_ID,branch_code,MOBILENO,"
				+ " (SELECT USERNAME  FROM USER_DETAILS WHERE  USERID=INST_CARD_PROCESS.CHECKER_ID) AS CHECKER,(SELECT BRANCH_NAME FROM BRANCH_MASTER WHERE BRANCH_CODE=INST_CARD_PROCESS.BRANCH_CODE) AS CARD_COLLECT_BRANCH  from INST_CARD_PROCESS "
				+ "where inst_id=? and CARD_STATUS=? and mkck_status=? " + condition + " order by order_ref_no";

		enctrace("checking select query for authlist" + select_query);
		List instorderlist = jdbctemplate.queryForList(select_query, new Object[] { instid, cardstatus, mkckstatus });

		trace("The Query Result is ==========>" + instorderlist);
		ListIterator itr = instorderlist.listIterator();
		while (itr.hasNext()) {
			Map temp = (Map) itr.next();
			String refnum = (String) temp.get("ORDER_REF_NO");
			String prodcode = (String) temp.get("PRODUCT_CODE");
			String usercode = (String) temp.get("MAKER_ID");
			String binno = (String) temp.get("BIN");
			String embossname = (String) temp.get("EMB_NAME");
			// String cin = (String)temp.get("CIN");
			// String productdesc = getProductdesc(instid, binno,prodcode);
			String productdesc = commondesc.getProductdesc(instid, prodcode, jdbctemplate);
			trace("INstituoion ID " + instid + "User Code ===> " + usercode);
			String username = commondesc.getUserName(instid, usercode, jdbctemplate);
			String count = commondesc.getCardcount(instid, refnum, cardstatus, mkckstatus, "REFNUM", jdbctemplate);
			/*
			 * List
			 * embossname=getEmbossingName(instid,(String)temp.get("CIN"),"CIN",
			 * jdbctemplate); ListIterator lit = embossname.listIterator();
			 * while(lit.hasNext()){ Map hm = (Map) lit.next(); String
			 * embossname2 = (String)hm.get("EMB_NAME"); trace(
			 * "EBOSSNAME========== " +embossname2); hm.put("EMBOSSING_NAME",
			 * embossname2); itr.add(hm); }
			 */
			trace("111111111111111111");
			temp.put("SUBPRODDESC", commondesc.getSubProductdesc(instid, (String) temp.get("SUBPRODID"), jdbctemplate));
			trace("22222222222222222222");
			temp.put("CARDNO", (String) temp.get("CARD_NO"));
			trace("3333333333333333");
			temp.put("COUNT", count);
			trace("44444444444444444");
			temp.put("EMBOSSING_NAME", embossname);
			temp.put("PNAME", productdesc);
			trace("555555555555555555");
			temp.put("USERNAME", username);
			trace("66666666666666666");
			itr.remove();
			itr.add(temp);

		}

		return instorderlist;
	}

	public String authourizeInstPin() {
		HttpSession session = getRequest().getSession();

		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);
		IfpTransObj transact = commondesc.myTranObject("PINAUTH", txManager);

		String instid = comInstId();
		String userid = comUserId();
		String authstatus = "";
		String statusmsg = "";
		String err_msg = "";
		String authmsg = "";
		String cardstatus = null;
		String order_refnum[] = getRequest().getParameterValues("instorderrefnum");
		System.out.println("Total Orders Selected ===> " + order_refnum.length);
		
		int size = 0;
		int[] updateCounts = new int[size];
		Connection conn = null;
		Dbcon dbcon = new Dbcon(); 
		conn = dbcon.getDBConnection();
		PreparedStatement pstmt = null;

		// added by gowtham_290719
		String ip = (String) session.getAttribute("REMOTE_IP");//

		if (getRequest().getParameter("authorize") != null) {
			System.out.println("AUTHORIZE...........");
			authstatus = "P";
			cardstatus = "02";
			statusmsg = " Authorized ";
			err_msg = "Authorize";
			authmsg = " .Waiting in Personalization Process ";
		} else if (getRequest().getParameter("deauthorize") != null) {
			System.out.println("DE AUTHORIZE...........");
			cardstatus = "01";
			authstatus = "P";
			statusmsg = " De-Authorized ";
			err_msg = "De-Authorize";
			authmsg = " Generated Pin Cancelled. Waiting For Pin Generation ";
		}

		
		
		
		

	 	try{
			
			if(order_refnum== null ){
				addActionError("No card number selected ....");
					return "required_home";
			}
			int crdcnt = 0;
		
			System.out.println("cardstatus--->"+cardstatus);
			System.out.println("authstatus--->"+authstatus);
			System.out.println("SYSDATE--->"+test.Date.getCurrentDate());
			System.out.println("instid-->"+instid);
			System.out.println("userid-->"+userid);
			
			System.out.println("order_refnum-->"+order_refnum.length);
			
			/*String update_authdeauth_qury = "UPDATE INST_CARD_PROCESS SET CARD_STATUS=?,PRIVILEGE_CODE=?, MKCK_STATUS=?,CHECKER_ID=?,CHECKER_DATE=(sysdate) WHERE INST_ID=? AND ORDER_REF_NO=?  ";
			System.out.println("update_authdeauth_qury======>" + update_authdeauth_qury);
			System.out.println("Before Update ===> " + cnt);
			check = jdbctemplate.update(update_authdeauth_qury,
					new Object[] { cardstatus, "01P", authstatus, userid, instid, order_refnum[i].trim() });
	*/
			
			if (conn != null) {
				System.out.println(" connection opend----->\n");
				trace("connectionopend---\n");
				enctrace("connectionopend---\n");
				conn.setAutoCommit(false);
				String sql = "UPDATE INST_CARD_PROCESS SET CARD_STATUS=?,PRIVILEGE_CODE=?, MKCK_STATUS=?,CHECKER_ID=?,CHECKER_DATE=SYSDATE WHERE INST_ID=? AND ORDER_REF_NO=?  ";
				pstmt = conn.prepareStatement(sql);
				trace("sql qry for update : " + sql);
				
				for (int i = 0; i < order_refnum.length; i++) {
					pstmt.setString(1,cardstatus );
					pstmt.setString(2,"01P");
					pstmt.setString(3,authstatus);
					pstmt.setString(4,userid);
					pstmt.setString(5,instid);
					pstmt.setString(6,order_refnum[i]);
					pstmt.addBatch();
		 		}
				long start = System.currentTimeMillis();
				updateCounts = pstmt.executeBatch();
				long end = System.currentTimeMillis();
				System.out.println("updateCounts--->"+updateCounts.length);
				System.out.println("awaitcardlist.length---->"+order_refnum.length);
				trace("awaitcardlist.length---->"+order_refnum.length);
				
				if(updateCounts.length==order_refnum.length){
				conn.commit();
				//addActionMessage( updateCounts.length + "    Card(s) "+statusmsg+"  Successfully...");
				
				session.setAttribute("prevmsg",  updateCounts.length + "    Card(s) Order " + statusmsg +" Successfully..."+authmsg);
				session.setAttribute("preverr", "S");
				
				System.out.append("Update records-----------> " + updateCounts.length);
				trace(updateCounts.length + " Card(s) "+statusmsg+" Successfully..got committed...");
				trace(order_refnum.length + " Card(s)  "+statusmsg+"  Successfully..got committed...");
				return InstauthPingenerationhome();}
				else{
					conn.rollback();
					pstmt.clearBatch() ;
					System.out.println("could not continue the process.../n--");
					trace("could not continue the process...---");
					addActionError("could not continue the process...!!!");
					return InstauthPingenerationhome();
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
		
		
		try {

			int cnt = 0;
			for (int i = 0; i < order_refnum.length; i++) {
				int check = 0;
				System.out.println("Selected Refnums ==>" + order_refnum[i]);
 

				/// by gowtham-280819
				String update_authdeauth_qury = "UPDATE INST_CARD_PROCESS SET CARD_STATUS=?,PRIVILEGE_CODE=?, MKCK_STATUS=?,CHECKER_ID=?,CHECKER_DATE=(sysdate) WHERE INST_ID=? AND ORDER_REF_NO=?  ";
				System.out.println("update_authdeauth_qury======>" + update_authdeauth_qury);
				System.out.println("Before Update ===> " + cnt);
				check = jdbctemplate.update(update_authdeauth_qury,
						new Object[] { cardstatus, "01P", authstatus, userid, instid, order_refnum[i].trim() });

				System.out.println(" QUery Executed ==check===>    " + check);
				if (check > 0) {
					cnt = cnt + 1;
					System.out.println("After Update ===> " + cnt);
				}

			}
			if (order_refnum.length == cnt) {
				session.setAttribute("preverr", "S");
				session.setAttribute("prevmsg", cnt + "  Order " + statusmsg + " successfully." + authmsg);
				txManager.commit(transact.status);
				System.out.println(" Committed success ");
			} else {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " Selected Orders Not " + statusmsg + " Successfully. " + authmsg);
				txManager.rollback(transact.status);
				System.out.println(" Rollback success ");
			}
		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " Error While" + err_msg + " The Orders " + e.getMessage());
			txManager.rollback(transact.status);
			System.out.println(" Rollback success ");
		}
		 
		try {

			trace("ip address======>  " + ip);// -----------------
			auditbean.setIpAdress(ip);// ----------------------------

			// commented by gowtham
			// auditbean.setActmsg("Pin [ "+order_refnum.length+" ] Authorized
			// Successfully "); auditbean.setActiontype("IC");
			auditbean.setActmsg("For Pin generated card Authorized Successfully ");
			auditbean.setActiontype("IC");
			auditbean.setUsercode(userid);
			auditbean.setAuditactcode("0203");
			commondesc.insertAuditTrail(instid, userid, auditbean, jdbctemplate, txManager);
		} catch (Exception audite) {
			trace("Exception in auditran : " + audite.getMessage());
		}
		 */
		setAct((String) session.getAttribute("PINGEN_ACT"));
		return InstauthPingenerationhome();

	}

	public String getCardcount(String instid, String refnum, String cardstatus, String mkckstatus, String flag,
			JdbcTemplate jdbcTemplate) {
		// String cafstatus = " AND CAF_REC_STATUS in('S','A','D')";
		String condtion = "ORDER_REF_NO";
		if (flag.equals("CARD")) {
			condtion = "CARD_NO";
			// cafstatus ="";
		}

		String count = "";

		/*
		 * String countqury =
		 * "select count(*) from INST_CARD_PROCESS where INST_ID='" + instid +
		 * "' and " + condtion + "='" + refnum + "' and CARD_STATUS='" +
		 * cardstatus + "' and MKCK_STATUS='" + mkckstatus + "'";// +cafstatus;
		 * trace("=countqury====> " + countqury); int cardcount =
		 * jdbcTemplate.queryForInt(countqury);
		 */

		// by gowtham-280819
		String countqury = "select count(*) from INST_CARD_PROCESS where INST_ID=? and " + condtion
				+ "=? and CARD_STATUS=? and MKCK_STATUS=? ";// +cafstatus;
		trace("=countqury====> " + countqury);
		int cardcount = jdbcTemplate.queryForInt(countqury, new Object[] { instid, refnum, cardstatus, mkckstatus });

		count = Integer.toString(cardcount);
		return count;
	}

}// end class
