
package com.ifp.personalize;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import jxl.write.WriteException;

import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

import org.apache.commons.io.FileUtils;
//import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.dao.CardCvvDecryption;
import com.ifp.instant.InstCardPREProcess;
import com.ifp.util.CommonDesc;
import com.ifp.util.ExcelGenerator;
import com.ifp.util.IfpTransObj;
import com.ifp.util.TrackEncryption;

import connection.Dbcon;

public class PreprocessAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	TrackEncryption EncDec = new TrackEncryption();
	CommonDesc commondesc = new CommonDesc();
	// TrackEncryption encTrack=new TrackEncryption();
	JdbcTemplate jdbctemplate = new JdbcTemplate();
	Date date = new Date();
	public final String Appid = "Rupaiya";
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();
	AuditBeans auditbean = new AuditBeans();
	private static String OS = System.getProperty("os.name").toLowerCase();

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

	public String comInstId() {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("Instname");
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

	public String comUserCode() {
		HttpSession session = getRequest().getSession();
		String instid = (String) session.getAttribute("USERID");
		return instid;
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

	PersionalizedcardCondition brcodecon = new PersionalizedcardCondition();

	public String personalPregenerationhome() {
		List pers_prodlist = null, br_list = null;
		String inst_id = comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		HttpSession session = getRequest().getSession();
		String cardStatus = "02", mkrstatus = "P";
		act = getRequest().getParameter("act");
		trace("act value : " + act);
		if (act != null) {
			session.setAttribute("PREGEN_ACT", act);
		}
		String session_act = (String) session.getAttribute("PREGEN_ACT");
		System.out.println("session_act " + session_act);
		try {
			System.out.println("Inst Id===>" + inst_id + "  Branch Code ===>" + branch);
			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = brcodecon.getBranchCodefmProcess(inst_id, cardStatus, mkrstatus, jdbctemplate);
				System.out.println("Branch list " + br_list);
				if (!(br_list.isEmpty())) {
					setBranchlist(br_list);
					System.out.println("Branch list is not empty");

				}

			}
			pers_prodlist = commondesc.getProductListBySelected(inst_id, cardStatus, mkrstatus, jdbctemplate);
			if (!(pers_prodlist.isEmpty())) {
				setPersonalproductlist(pers_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "");
				System.out.println("Product List is ===> " + pers_prodlist);
				// setCardgenstatus('Y');
			} else {
				System.out.println("No Product Details Found ");
				/*
				 * session.setAttribute("curerr", "E");
				 * session.setAttribute("curmsg",
				 * "<br> No Product Details Found ");
				 */
				// setCardgenstatus('N');
			}

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Error While Fetching The Product Details");
			trace("Error While Fetching The Product Details  " + e.getMessage());
			e.printStackTrace();
			// setCardgenstatus('N');
		}

		return "pregenerationhome";
	}

	private List perspregenauthlist;

	public List getPerspregenauthlist() {
		return perspregenauthlist;
	}

	public void setPerspregenauthlist(List perspregenauthlist) {
		this.perspregenauthlist = perspregenauthlist;
	}

	public String preGenerationorder() {
		trace("******** preGenerationorder ********* ");
		enctrace("******** preGenerationorder *********  ");
		HttpSession session = getRequest().getSession();

		try {
			String branch = getRequest().getParameter("branchcode");
			String cardtype = getRequest().getParameter("cardtype");
			String fromdate = getRequest().getParameter("fromdate");
			String todate = getRequest().getParameter("todate");
			String instid = comInstId();

			String cardstatus = "02";
			String mkckstatus = "P";

			trace("Branch : " + branch + " Card Type : " + cardtype + " From Date : " + fromdate + "  To Date : "
					+ todate);
			List authcardorder = null;
			String dateflag = "PIN_DATE";

			String condition = commondesc.filterCondition(cardtype, branch, fromdate, todate, dateflag);
			trace("Condition Value : " + condition);
			authcardorder = commondesc.personaliseCardauthlist(instid, cardstatus, mkckstatus, condition, jdbctemplate);

			this.fromdate = fromdate;
			this.todate = todate;

			trace("authcardorder : " + authcardorder);
			if (authcardorder.isEmpty()) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "No Orders Found");
				trace("No Orders Found");
				return personalPregenerationhome();
			} else {
				setPerspregenauthlist(authcardorder);
				session.setAttribute("curerr", "S");
			}
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Error :Could not continue the process..");
			trace("Exception : " + e.getMessage());
			e.printStackTrace();
		}
		trace("\n\n");
		enctrace("\n\n ");

		return "pregenerationorders";
	}

	public String deleteInstPreFile() {
		trace("******* Delete personalization file begiin********");
		enctrace("******* Delete personalization file begin********");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("DELINSTPRE", txManager);

		String instid = comInstId();
		String prefilename = getRequest().getParameter("prefilename");
		try {
			trace("Getting downloaded count for the file : " + prefilename);
			int checkdowncnt = this.checkDownloadCounts(instid, prefilename, jdbctemplate);
			trace("Got : checkdowncnt " + checkdowncnt);
			if (checkdowncnt <= 0) {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Could not delete. Download personalization file and then delete...");
				trace("Personalization file deleted successfully..got committed");
				return this.personalPredownloadhome();
			}

			trace("Deleting the personalization file...");
			int deletepre = this.delelePreFile(instid, prefilename, jdbctemplate);
			trace("Got deletepre : " + deletepre);
			if (deletepre < 0) {
				// commondesc.rollbackTxn(jdbctemplate);
				txManager.rollback(transact.status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Could not delte the file ");
				trace("Could not delte the file ..got rolled back");
				return this.personalPredownloadhome();
			}

			// commondesc.commitTxn(jdbctemplate);
			// txManager.commit(transact.status);
			txManager.commit(transact.status);
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", "Personalization file deleted successfully");
			trace("Personalization file deleted successfully..got committed");

		} catch (Exception e) {
			// commondesc.rollbackTxn(jdbctemplate);
			// txManager.rollback(transact.status);
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception : Could not delete file");
			trace("Exception : could not delete : " + e.getMessage());
			e.printStackTrace();
		}

		trace("******* Delete personalization file end********\n\n");
		enctrace("******* Delete personalization file end********\n\n");

		return this.personalPredownloadhome();
	}

	public int checkDownloadCount(String bin, String prefilename, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		try {

			/*
			 * String downcntqry =
			 * "SELECT count(*)as count FROM PERS_PRE_DATA WHERE PRODUCT_CODE='"
			 * + bin + "' AND PRE_NAME='" + prefilename + "'"; enctrace(
			 * "no.of card count query" + downcntqry); x =
			 * jdbctemplate.queryForInt(downcntqry);
			 */

			// by gowtham-260819
			String downcntqry = "SELECT count(*)as count FROM PERS_PRE_DATA WHERE PRODUCT_CODE=? AND PRE_NAME=? ";
			enctrace("no.of card count query" + downcntqry);
			x = jdbctemplate.queryForInt(downcntqry, new Object[] { bin, prefilename });

			trace("Card count" + x);
		} catch (EmptyResultDataAccessException e) {
		}
		return x;
	}

	public int checkDownloadCounts(String instid, String prefilename, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		try {

			/*
			 * String downcntqry =
			 * "SELECT DOWN_CNT FROM PERS_PRE_DATA WHERE INST_ID='" + instid +
			 * "' AND PRE_NAME='" + prefilename + "' AND ROWNUM<=1";
			 * enctrace(downcntqry); x = jdbctemplate.queryForInt(downcntqry);
			 */

			// by gowtham-260819
			String downcntqry = "SELECT DOWN_CNT FROM PERS_PRE_DATA WHERE INST_ID=? AND PRE_NAME=? AND ROWNUM<=?";
			enctrace(downcntqry);
			x = jdbctemplate.queryForInt(downcntqry, new Object[] { instid, prefilename, "1" });

		} catch (EmptyResultDataAccessException e) {
		}
		return x;
	}

	public int delelePreFile(String instid, String prefilename, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;

		/*
		 * String delteqry = "DELETE FROM PERS_PRE_DATA WHERE INST_ID='" +
		 * instid + "' AND PRE_NAME='" + prefilename + "'"; enctrace(delteqry);
		 * x = jdbctemplate.update(delteqry);
		 */

		// by gowtham-260819
		String delteqry = "DELETE FROM PERS_PRE_DATA WHERE INST_ID=? AND PRE_NAME=? ";
		enctrace(delteqry);
		x = jdbctemplate.update(delteqry, new Object[] { instid, prefilename });

		return x;
	}
	
	public String preGenerationprocess() throws Exception {
		//Method Modified on 02-MAR-2021
		trace("******* Persionalize preGenerationprocess *******\n");
		enctrace("******* Persionalize preGenerationprocess  *******\n");

		String predata = null;			String cardnum = null;							String expiry_1 = null;				String enc_name = null;			String expiry_2 = null;
		String cvv1 = null;					String cvv2 = null;									String icvv = null;						String servicecode = null;		String cardref_no = null;
		String validto = null;				String cardCollectBranch = null;		String ADDRESS1 = null;			String ADDRESS2 = null;			String ADDRESS3 = null;
		String ADDRESS4 = null;		String ADDRESS5 = null;						String PHONENO = null;			int process_count = 0;				Boolean presuc = true;
		int ordercnt = 0;						int status = 0;											String track1 = null;					String track2 = null;					String authmsg = null;
		String dcardno = null;			String general_data = null;					String custdata = null;				String CDMK = null;					String mcardno = null;
		String makerid = null;			String checkerid = null;						String makerdate = null;			String checkerdate = null;		String mkckflag = null;
		String ckdate = null;				String printdata = null;						String prerecord = null;	 		String cin = null;
		List carddetails = null;			List cvvdata = null;								List addressdetails = null;		String barnchName=null;
		
		
		//added by gowtham_02-MAR-2021
		String errormsg="";				String encode_name="";						String countrycode="";			String cardcurrency="";
		
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
		StringBuilder sb=new StringBuilder("");
		//added end

		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		CardCvvDecryption desc = new CardCvvDecryption();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus txnstatus = txManager.getTransaction(def);

		HttpSession session = getRequest().getSession();
		String[] order_refnum = getRequest().getParameterValues("personalrefnum");
		System.out.println("pre list of cards" + order_refnum);
		
		String instid = comInstId();
		String usercode = comUserCode();
		String username = comUsername();
		
		trace("instid-->"+instid);
		trace("usercode-->"+usercode);
		trace("username-->"+username);
		trace("Total Orders Selected : " + order_refnum.length);
		
		String productcode = getRequest().getParameter("binno");
		trace("Getting binno" + productcode);
		String bin = commondesc.getBin(instid, productcode, jdbctemplate);
		String branchcode = getRequest().getParameter("branchcode");
		String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);
		String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
		PadssSecurity padsssec = new PadssSecurity();
		List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);

		Properties props = getCommonDescProperty();
		String EDPK = props.getProperty("EDPK");
		String ip = (String) session.getAttribute("REMOTE_IP");
	 
		try {

		 	String act = "M";
			System.out.println("ACT VALUE FOR PRE FILE GENERATION" + act);

			if (act.equals("M")) {
				System.out.println("act value " + act);
				makerid = usercode;
				mkckflag = "M";
				ckdate = commondesc.default_date_query;
				makerdate = "SYSDATE";
				authmsg = " and Waiting for Authorization ........";
				}
			
	 	    	String table = "PERS_CARD_PROCESS";
	 	    	DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
	 	    	Date date = new Date();

			String userdesc = commondesc.getUserName(instid, usercode, jdbctemplate);
			String downproductname = productcode;
			String prename = downproductname + "_" + userdesc + "_" + dateFormat.format(date);
			trace("prename : " + prename);
			
			
			String preinst_qury="INSERT INTO PERS_PRE_DATA ("
					+"INST_ID,					BIN, 								PRODUCT_CODE, 			CARD_NO,						ORDER_REF_NO,			BRANCH_CODE,						GENERATED_DATE,"
					+"PRE_NAME,			TRACK_DATA,			USER_CODE,						EMB_NAME, 					ENC_NAME, 						TRACK1, 										TRACK2, "
					+"CVV1, 						CVV2, 							ICVV, 									COUNTRY_CODE, 		CURRENCY_CODE, 		EXPDATE, 									APP_NAME," 
					+"PAN_SEQNO, 		SERVICE_CODE,		VALID_FROM, 					VALID_TO, 						CARD_REFNO, 					EMB_CARDNO,								ADDRESS1,"
					+"ADDRESS2,			ADDRESS3,				ADDRESS4,						ADDRESS5,					BRANCH_NAME,				CURRENCY_EXPONENET,		CIN, "
					+"PHONENO,			DOWN_CNT,				CARD_TYPE_DESC ) "
					+"VALUES  (?,?,?,?,?,?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			 insertpstmt = conn.prepareStatement(preinst_qury);
			 enctrace("update_qury : " + preinst_qury);
			 
			 
			 	String pre_upd_qury = "UPDATE PERS_CARD_PROCESS SET  CARD_STATUS=?, PRE_DATE=sysdate,PRE_FILE=?, MAKER_DATE= "+ makerdate + ", MKCK_STATUS = ? "
			 											+ "WHERE INST_ID=? AND ORG_CHN=?";
			 	enctrace("update_qury : " + pre_upd_qury);
			 	updatepstmt = conn.prepareStatement(pre_upd_qury);
			 	
	           
			 	String Auditinsert="INSERT INTO AUDITRAN (INST_ID, BIN, PRODUCTCODE, SUBPRODUCT, CARDNO, USERCODE, ACTIONDATE, AUDITMSG, REMARKS , AUDITACTCODE, APPLICATIONID, PREFILE_NAME, APPTYPE,ACTIONTYPE,ACCNO,CUSTNAME,BRANCHCODE,CIN,CHECKERID,IP_ADDRESS ) VALUES "
					    + " (?,?,?,?, ?,?,SYSDATE,?, ?,?,?,?, ?,?,?,?,?,?,?,?)";
				enctrace("insert_qury : " + Auditinsert);
				AUDITRANpstmt = conn.prepareStatement(Auditinsert);
			 
			

			for (int i = 0; i < order_refnum.length; i++) {

				String refnum = order_refnum[i].toString().trim();
	 			trace("Getting card list for the order-ref-no [ " + refnum + " ] ");
	 			
				List chnlist = commondesc.getCardsFromOrder(instid, refnum, "PERSONAL", "P", "02", jdbctemplate);
				if (!chnlist.isEmpty()) {
					Iterator itr = chnlist.iterator();

					while (itr.hasNext()) {
						Map temp = (Map) itr.next();
						String ecardno = (String) temp.get("ORG_CHN");
						trace("Getting card details .... for the cardno[ " + ecardno + " ] ");
						// System.out.println("------------ "+secList);
						Iterator secitr = secList.iterator();
						while (secitr.hasNext()) {
							Map map = (Map) secitr.next();
							CDMK = ((String) map.get("DMK"));
							String CDPK = padsssec.decryptDPK(CDMK, EDPK);
							dcardno = padsssec.getCHN(CDPK, ecardno);
						}
						
						carddetails = commondesc.getCarddetails(table, instid, ecardno, padssenable, keyid, secList,jdbctemplate);
						trace("List data is ======   " + carddetails);
						
						if (!(carddetails.isEmpty())) {
							Iterator crdItr = carddetails.iterator();

							while (crdItr.hasNext()) {
								Map crdmap = (Map) crdItr.next();
								cardnum = ((String) crdmap.get("CARD_NO"));
								trace("cardnumber" + cardnum);
								expiry_1 = ((String) crdmap.get("EXP_2"));
								cin = ((String) crdmap.get("CIN"));
								trace("Expiry date1 " + expiry_1);
								cardCollectBranch = ((String) crdmap.get("CARD_COLLECT_BRANCH"));
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
								custdata = (String) crdmap.get("GENDATA");}
							
 
							String subProdDesc=commondesc.getSubProduct(instid, ecardno, jdbctemplate);
							barnchName = commondesc.getBranchName(cardCollectBranch, jdbctemplate);
							String embossingcardno = commondesc.embossingCardNumber(dcardno);
							String validfrom = commondesc.getDate("MM/YY");
							
							trace("subProdDesc-->"+subProdDesc);
							trace("barnchName-->"+barnchName);
							trace("embossingcardno-->"+embossingcardno);
							trace("validfrom-->"+validfrom);
						 
							if (enc_name == null) {
								System.out.println(" Enc / Emb name empty ");
								status = -5;
								break;
							}
							System.out.println("enc name ---> " + enc_name);

							if(cvv1==null || cvv2==null || icvv==null){
								System.out.println("  CVV values  name empty ");
								status = -3;
								break;
								}
							
								encode_name = commondesc.formateEncodingname(enc_name);
								countrycode = commondesc.getCountryCode(instid, jdbctemplate);
							 
							if (countrycode == null) {
								errormsg="Could not get country code....";
								return "required_home";
							}
							cardcurrency = commondesc.getCardCurrencyCode(instid, "PERS", ecardno, jdbctemplate);
							
							if (cardcurrency == null) {
							    errormsg="Could not get card currecny code [" + ecardno + "] ....";
								return "required_home";
							}
							mcardno = padsssec.getMakedCardno(ecardno);
							String appname = "IFD";
							String panseqno = "01";
 
							if (padssenable.equals("Y")) {
								embossingcardno = commondesc.embossingCardNumber(dcardno);
								System.out.println("embossingcardno--->"+embossingcardno);
								printdata = dcardno + expiry_1 + encode_name;
								trace("print data  embosing details ======>>"+printdata);
							} else {
								printdata = ecardno + expiry_1 + encode_name;
								trace("print data  embosing details ======>>"+printdata);
							}
							
							predata = track1 + track2;
					    	prerecord = printdata + cvv2 + track1 + track2 + icvv;
					    	
					    	trace("pre data--->"+predata);
					    	trace("track1--->"+track1);
					    	trace("track2--->"+track2);
					    	trace("prerecord--->"+prerecord);

			 				int insrt_status = 1;
							try {
								trace("Inserting pre data....");

								insertpstmt.setString(1, instid);
					 			insertpstmt.setString(2, bin);
					 			insertpstmt.setString(3, productcode);
								insertpstmt.setString(4, ecardno);
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
								insertpstmt.setString(19, expiry_1);
								insertpstmt.setString(20, appname);
								insertpstmt.setString(21, panseqno);
								insertpstmt.setString(22, "0"+servicecode);
								insertpstmt.setString(23, validfrom);
								insertpstmt.setString(24, validto);
								insertpstmt.setString(25, cardref_no);
								insertpstmt.setString(26, embossingcardno);
								insertpstmt.setString(27, ADDRESS1);
								insertpstmt.setString(28, ADDRESS2);
								insertpstmt.setString(29, ADDRESS3);
								insertpstmt.setString(30, ADDRESS4);
								insertpstmt.setString(31, ADDRESS5);
								insertpstmt.setString(32, barnchName);
								insertpstmt.setString(33, "2");
								insertpstmt.setString(34, cin);
								insertpstmt.setString(35, PHONENO);
								insertpstmt.setString(36, "0");
								insertpstmt.setString(37,subProdDesc);
								insertpstmt.addBatch();
								
	 						 	enctrace("preinst_qury-------->"+preinst_qury);
			 	 				trace("Got PRE-INSERT VALUE.... " + preinst_qury);
								
							} catch (Exception e) {
								e.printStackTrace();
								insrt_status = -1;
							}
 
			 				try{
			 					trace("updating pre status....");
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
							}}
							
					else {
							trace("No Card Details Found for the " + ecardno);
							status = -2;
							break;
						}
					}
				} else {
					errormsg="No Cards data found the the Ref no  " + refnum;
					trace("No Cards data found the the Ref no  " + refnum);
					 
				}

		 		if (status == -1) {
		 			errormsg=("Card Numbers List is Empty===> For the order" + order_refnum[i]);
					trace("Card Numbers List is Empty For the order : " + order_refnum[i]);
					presuc = false;
		 			break;
				}
				if (status == -2) {
					errormsg=( "Card Numbers List is Empty===> For the order" + order_refnum[i]);
					trace("Card Numbers List is Empty : " + order_refnum[i]);
					presuc = false;
					break;
				}
				if (status == -3) {
					errormsg=( "CVV Data List is Empty===> For the order" + order_refnum[i]);
					trace("CVV Data List is Empty For the order : " + order_refnum[i]);
					presuc = false;
					break;
				}
				if (status == -4) {
					errormsg=("Error While Insert The PRE Data " + order_refnum[i]);
					trace("Error While Insert The PRE Data : " + order_refnum[i]);
					presuc = false;
					break;
				}

				if (status == -5) {
					errormsg=("Encoding / Embossing name should not be empty ");
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
				addActionMessage(errormsg);
				}
				
				else{
					insertpstmt.clearBatch() ;
					updatepstmt.clearBatch() ;
					AUDITRANpstmt.clearBatch() ;
 					addActionError("could not continue the Personalization process......!!!");
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
	 		// adding data
			sb = new StringBuilder(dcardno);
			sb.append(cvv1);
			sb.append(cvv2);
			sb.append(icvv);
			sb.append(prerecord);
			sb.append(predata);

		} catch (Exception e) {
			addActionError("CATCH could not continue the Personalization process...!!! " + errormsg);
	 		trace("Exception : could not continue the PRE Process " + e.getMessage());
			e.printStackTrace();
			return "required_home";
		} finally {

			prerecord = null;				predata = null;					track1 = null;						track2 = null;						cvv1 = null;							cvv2 = null;
			icvv = null;							dcardno = null;				sb = null;								cardnum = null;					expiry_1 = null;					enc_name = null;
			expiry_2 = null;					servicecode = null;		cardref_no = null;				validto = null;						ADDRESS1 = null;				ADDRESS2 = null;
			ADDRESS3 = null;				ADDRESS4 = null;			ADDRESS5 = null;				PHONENO = null;				process_count = 0;				presuc = true;
			ordercnt = 0;							status = 0;							authmsg = null;					general_data = null;			custdata = null;					CDMK = null;
			mcardno = null;					makerid = null;				checkerid = null;				makerdate = null;				checkerdate = null;			mkckflag = null;	
			ckdate = null;						printdata = null;				sb = null;								carddetails = null;				cvvdata = null;					addressdetails = null;
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

		return this.personalPregenerationhome();

	}



	public String personalPregenerationauthhome() {
		trace("******** personalPregenerationauthhome ********* ");
		enctrace("******** personalPregenerationauthhome *********  ");
		List pers_prodlist = null, br_list = null;
		String inst_id = comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		HttpSession session = getRequest().getSession();
		String cardStatus = "03", mkrstatus = "M";
		try {
			System.out.println("Inst Id===>" + inst_id + "  Branch Code ===>" + branch);
			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = brcodecon.getBranchCodefmProcess(inst_id, cardStatus, mkrstatus, jdbctemplate);
				System.out.println("Branch list " + br_list);
				if (!(br_list.isEmpty())) {
					setBranchlist(br_list);
				}

			}
			pers_prodlist = commondesc.getProductListBySelected(inst_id, cardStatus, mkrstatus, jdbctemplate);
			if (!(pers_prodlist.isEmpty())) {
				setPersonalproductlist(pers_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "");
				trace("Product List is : " + pers_prodlist.size());
				// setCardgenstatus('Y');
			} else {
				System.out.println("No Product Details Found ");
				/*
				 * session.setAttribute("curerr", "E");
				 * session.setAttribute("curmsg"," No Product Details Found ");
				 */
				trace(" No Product Details Found ");
				// setCardgenstatus('N');
			}

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "Error While Fetching The Product Details  ");
			trace("Error While Fetching The Product Details  " + e.getMessage());
			// setCardgenstatus('N');
		}
		trace("\n\n");
		enctrace("\n\n ");
		return "preauthorizehome";
	}

	private List preauthorderlist;

	public List getPreauthorderlist() {
		return preauthorderlist;
	}

	public void setPreauthorderlist(List preauthorderlist) {
		this.preauthorderlist = preauthorderlist;
	}

	public String getPreauthorizeorders() {
		System.out.println("################# Auth Rize Order LIST ########################");
		HttpSession session = getRequest().getSession();

		String instid = comInstId();
		String branch = getRequest().getParameter("branchcode");
		String bin = getRequest().getParameter("cardtype");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		System.out.println("Branch===>" + branch + "\n Card Type ===> " + bin + "\n From Date===>" + fromdate
				+ "\n To Date===> " + todate);
		String dateflag = "PRE_DATE", cardstatus = "03", mkckstatus = "M";
		List preauthlist = null;
		try {

			String condition = commondesc.filterCondition(bin, branch, fromdate, todate, dateflag);
			System.out.println("Condition Value----->  " + condition);
			preauthlist = commondesc.personaliseCardauthlist(instid, cardstatus, mkckstatus, condition, jdbctemplate);

			if (!(preauthlist.isEmpty())) {
				setPreauthorderlist(preauthlist);
				session.setAttribute("curerr", "S");
			} else {
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "No Orders Found");
				return personalPregenerationauthhome();
			}
		} catch (Exception e) {
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Error While Getting the Orders , Error :" + e.getMessage());
			return personalPregenerationauthhome();
		}

		return "preauthorders";
	}

	public String authPregeneration() throws Exception {

		trace("Authorize pre file");
		enctrace("Authorize pre file");
		PreprocessAction preprocess = new PreprocessAction();
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("AUTHPRE", txManager);
		// -------------added by sardar on 11-12-15---------//
		String username = comUsername();

		// -------------ended by sardar on 11-12-15---------//
		Personalizeorderdetails persorderdetails, bindetails, extradetails;

		// added by gowtham_230719
		String ip = (String) session.getAttribute("REMOTE_IP");

		String remarks = getRequest().getParameter("reason");
		String instid = comInstId();
		String usercode = comUserId();
		String authstatus = "";
		String statusmsg = "";
		String err_msg = "";
		String order_refnum[] = getRequest().getParameterValues("personalrefnum");
		System.out.println("Total Orders Selected ===> " + order_refnum.length);
		if (getRequest().getParameter("authorize") != null) {
			System.out.println("AUTHORIZE...........");
			authstatus = "P";
			statusmsg = " Authorized ";
			err_msg = "Authorize";
		} else if (getRequest().getParameter("deauthorize") != null) {
			System.out.println("DE AUTHORIZE...........");
			authstatus = "D";
			statusmsg = " De-Authorized ";
			err_msg = "De-Authorize";
		}


		try {

			String tablename = "PERS_PRE_DATA";
			InstCardPREProcess instprocess = new InstCardPREProcess();

			String bin = "", prefilename = "";

			int cardcount = 0;
			int cardtoprocesscnt = order_refnum.length;

			for (int i = 0; i < order_refnum.length; i++) {
				int update_result = 0;
				trace("GEnerating Pre Sequence no");
				trace("order refernce length is ----->  " + order_refnum[i]);
				int preSeqNo = this.sequencePREFILE(instid, jdbctemplate);
				trace("Generating presonalization file....");

				String preDetailsQry = "SELECT PRODUCT_CODE,PRE_NAME FROM PERS_PRE_DATA WHERE INST_ID='" + instid
						+ "' AND CARD_NO='" + order_refnum[i] + "' ORDER BY GENERATED_DATE DESC";
				trace("preDetailsQry ....." + preDetailsQry);
				enctrace("preDetailsQry ....." + preDetailsQry);
				List<Map<String, Object>> list = jdbctemplate.queryForList(preDetailsQry);

				/*
				 * //by gowtham-260819 String preDetailsQry =
				 * "SELECT PRODUCT_CODE,PRE_NAME FROM PERS_PRE_DATA WHERE INST_ID=? AND CARD_NO=? ORDER BY GENERATED_DATE DESC"
				 * ; trace("preDetailsQry ....." + preDetailsQry); enctrace(
				 * "preDetailsQry ....." + preDetailsQry); List<Map<String,
				 * Object>> list = jdbctemplate.queryForList(preDetailsQry,new
				 * Object[]{instid,order_refnum[i]});
				 */

				bin = (String) list.get(0).get("PRODUCT_CODE");
				prefilename = (String) list.get(0).get("PRE_NAME");
				// int y = this.generatePREInst(instid, bin, prefilename,
				// tablename, preSeqNo, session, jdbctemplate,
				// commondesc,preprocess );
				// int y = generatePRE(instid, bin, prefilename,cardcount,
				// tablename, preSeqNo, session, jdbctemplate, commondesc );
				int y = 0;

				trace("Generating presonalization file....got : " + y);
				if (y < 0) {

					addActionError("unable to proceess could not genereate pre file");
					return "required_home";
				}

				System.out.println("Selected Refnums ==>" + order_refnum[i]);
				String update_authdeauth_qury = "UPDATE PERS_CARD_PROCESS SET CHECKER_ID='" + usercode
						+ "',CHECKER_DATE=(sysdate),MKCK_STATUS='" + authstatus + "' WHERE INST_ID='" + instid
						+ "' AND ORG_CHN='" + order_refnum[i] + "'";
				System.out.println(" UPdate Queyr ====> " + update_authdeauth_qury);
				update_result = jdbctemplate.update(update_authdeauth_qury);

				update_authdeauth_qury = "UPDATE PERS_PRE_DATA SET AUTH_CODE='1', AUTH_BY='" + usercode
						+ "', AUTH_DATE=SYSDATE WHERE INST_ID='" + instid + "' AND CARD_NO='" + order_refnum[i] + "'";
				System.out.println(" UPdate Queyr ====> " + update_authdeauth_qury);
				update_result = jdbctemplate.update(update_authdeauth_qury);

			
				System.out.println(" update_result =====>" + update_result);

				if (update_result > 0 && jdbctemplate.update(update_authdeauth_qury) > 0) {
					// updatecount = updatecount + 1;
					cardcount++;
					System.out.println("Current Count is =====> " + cardcount);
				}

				/************* AUDIT BLOCK **************/
				try {
					String mcardno = commondesc.getMaskedCardNo(instid, order_refnum[i], "C", jdbctemplate);
					String cin=commondesc.getCinFromProcess(order_refnum[i], instid, "", jdbctemplate);
					if (mcardno == null) {
						mcardno = order_refnum[i];
					}
					auditbean.setActmsg("Personalize File " + statusmsg + "  [ " + mcardno + " ]");
					auditbean.setUsercode(username);
					auditbean.setAuditactcode("0104");
					auditbean.setCardno(mcardno);
					auditbean.setCin(cin);
					auditbean.setProduct(bin);

					// added by gowtham_230719
					trace("ip address======>  " + ip);
					auditbean.setIpAdress(ip);

					// auditbean.setCardnumber(order_refnum[i].toString());
					// commondesc.insertAuditTrail(in_name, Maker_id, auditbean,
					// jdbctemplate, txManager);
					commondesc.insertAuditTrailPendingCommit(instid, username, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					audite.printStackTrace();
					trace("Exception in auditran : " + audite.getMessage());
				}

				/************* AUDIT BLOCK **************/
			}
			trace("checking process result cnt" + cardtoprocesscnt);
			trace("checking order cnt" + cardcount);
			if (cardtoprocesscnt == cardcount) {
				session.setAttribute("preverr", "S");
				session.setAttribute("prevmsg", cardcount
						+ "  Cards Authorized Successfully and Waiting for Personalization File Download ... ");
				txManager.commit(transact.status);
				System.out.println(" Committed success ");
			} else {
				addActionError("Unable to continue Pre Authorization process");
				txManager.rollback(transact.status);
				trace("Unable to continue Pre Authorization process");

			}

		} catch (TransactionException e) {
			e.printStackTrace();
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " Unable to " + err_msg + " The Cards " + e.getMessage());
			// commondesc.rollbackTxn(jdbctemplate);
			txManager.rollback(transact.status);
			System.out.println(" Rollback success ");
		}

	

		return personalPregenerationauthhome();
	}

	public String personalPredownloadhome() {
		List pers_prodlist = null, br_list = null;
		String inst_id = comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		HttpSession session = getRequest().getSession();

		try {
			System.out.println("Inst Id===>" + inst_id + "  Branch Code ===>" + branch);
			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = commondesc.generateBranchListPRE(inst_id, jdbctemplate);
				System.out.println("Branch list " + br_list);
				if (!(br_list.isEmpty())) {
					setBranchlist(br_list);
					session.setAttribute("curerr", "S");
					session.setAttribute("curmsg", "");
					System.out.println("Branch list is not empty");
				} else {
					setBranchlist(br_list);
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg", " No Branch Details Found ");
					System.out.println("Branch List is empty ");
				}
			}
			pers_prodlist = commondesc.getProductList(inst_id, jdbctemplate, session);
			if (!(pers_prodlist.isEmpty())) {
				setPersonalproductlist(pers_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", "");
				System.out.println("Product List is ===> " + pers_prodlist);
			} else {
				System.out.println("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", " No Product Details Found ");
			}

		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " Error While Fetching The Product Details  " + e.getMessage());
			e.printStackTrace();
		}

		return "predownloadhome";
	}

	public void getPREFiles() {
		String instid = comInstId();

		String bin = getRequest().getParameter("prodid");
		String branch = getRequest().getParameter("branchcode");
		String filetype = getRequest().getParameter("filetype");
		System.out.println("bin__" + bin + "_instid_" + instid + " Branch===>" + branch);
		String opt = "";
		try {

			int predispdays = commondesc.getPREDisplayDays(instid, jdbctemplate);
			trace(" Display timer intervel on days : " + predispdays);

			List prenamelist = commondesc.getPersonalPREList(instid, branch, bin, predispdays, filetype, jdbctemplate);
			System.out.println(prenamelist);
			opt = "<option value='-1'> -SELECT- </option>";
			if (prenamelist.isEmpty()) {
				opt = "<option value='-1'> No Files found </option>";
			} else {
				Iterator itr = prenamelist.iterator();
				while (itr.hasNext()) {
					Map temp = (Map) itr.next();
					String prename = (String) temp.get("PRE_NAME");
					opt += "<option value='" + prename + "'>" + prename + ".mc</option>";
				}
			}
			getResponse().getWriter().write(opt);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception===> " + e.getMessage());
			opt = "<option value='-1'> -ERROR- </option>";
			try {
				getResponse().getWriter().write(opt);
			} catch (Exception ex) {

			}
		}

	}

	public int updatePreFileDownloadCnt(String instid, String prefilename, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;

		/*
		 * String delteqry =
		 * "UPDATE PERS_PRE_DATA SET DOWN_CNT=DOWN_CNT+1 WHERE INST_ID='" +
		 * instid + "' AND PRE_NAME='" + prefilename + "'"; enctrace(delteqry);
		 * x = jdbctemplate.update(delteqry);
		 */

		/// by gowtham-260819
		String delteqry = "UPDATE PERS_PRE_DATA SET DOWN_CNT=DOWN_CNT+1 WHERE INST_ID=? AND PRE_NAME=?";
		enctrace(delteqry);
		x = jdbctemplate.update(delteqry, new Object[] { instid, prefilename });

		return x;
	}

	public void getPREFileList() throws Exception {
		String instid = comInstId();
		String filename = getRequest().getParameter("filename");
		String cardlist = "";
		try {

			/*
			 * String previewqry =
			 * "SELECT CARD_NO FROM PERS_PRE_DATA WHERE INST_ID='" + instid +
			 * "' AND  PRE_NAME='" + filename + "'"; List previewqryview =
			 * jdbctemplate.queryForList(previewqry);
			 */

			// by gowtham-260819
			String previewqry = "SELECT CARD_NO FROM PERS_PRE_DATA WHERE INST_ID=? AND  PRE_NAME=? ";
			List previewqryview = jdbctemplate.queryForList(previewqry, new Object[] { instid, filename });

			if (!previewqryview.isEmpty()) {
				Iterator itr = previewqryview.iterator();
				cardlist += "!!! Card Number List !!! \n\n";
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					cardlist += (String) mp.get("CARD_NO") + "\n";
				}
			}
		} catch (Exception e) {
			trace("Exception : " + e.getMessage());
			cardlist = "COULD NOT GET CARDNUMBER LIST";
		}
		getResponse().getWriter().write(cardlist);
	}

	public void getPREFilesDetails() throws Exception {
		String instid = getRequest().getParameter("instid");
		String filename = getRequest().getParameter("filename");
		String product = getRequest().getParameter("prodid");
		JSONObject prejson = new JSONObject();
		try {

			/*
			 * String previewqry =
			 * "SELECT DOWN_CNT, PRE_NAME, COUNT(*) AS CARDCOUNT  FROM PERS_PRE_DATA WHERE PRODUCT_CODE='"
			 * + product + "' AND  PRE_NAME='" + filename +
			 * "'  GROUP BY PRE_NAME,DOWN_CNT"; List previewqryview =
			 * jdbctemplate.queryForList(previewqry);
			 */

			// by gowtham-260819
			String previewqry = "SELECT DOWN_CNT, PRE_NAME, COUNT(*) AS CARDCOUNT  FROM PERS_PRE_DATA WHERE PRODUCT_CODE=? AND  PRE_NAME=? GROUP BY PRE_NAME,DOWN_CNT";
			List previewqryview = jdbctemplate.queryForList(previewqry, new Object[] { product, filename });

			if (!previewqryview.isEmpty()) {

				Iterator itr = previewqryview.iterator();

				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					prejson.put("DOWNCNT", (String) (Object) mp.get("DOWN_CNT").toString());
					prejson.put("PRENAME", (String) mp.get("PRE_NAME"));
					prejson.put("CARDCOUNT", (String) (Object) mp.get("CARDCOUNT").toString());
				}

				String[] filearray = filename.split("_");
				String usercode = (String) filearray[1];
				String gendate1 = (String) filearray[2].substring(0, 2) + "-" + (String) filearray[2].substring(2, 4)
						+ "-" + (String) filearray[2].substring(4, 8);
				String gendate = gendate1 + " " + filearray[3];

				// String username = commondesc.getUserName(instid, usercode,
				// jdbctemplate);
				// System.out.println("username---> "+username);
				prejson.put("USERNAME", usercode);
				prejson.put("GENDATE", gendate);
				prejson.put("RESP", 0);
			}

		} catch (Exception e) {
			prejson.put("RESPREASON", "Could not get filename view");
			prejson.put("RESP", 1);
			trace("Exception Pre view : " + e.getMessage());
			e.printStackTrace();
		}
		getResponse().getWriter().write(prejson.toString());
	}

	

	private JSONObject generatePREFDBFields() {
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
		jsonpre.put("H20", "BRANCH_NAME");
		jsonpre.put("H21", "CARD_TYPE_DESC");
		// jsonpre.put("H20", "CIN");
		// jsonpre.put("H20", "BRANCH_CODE");
		// jsonpre.put("H22", "BRANCH_NAME");
		return jsonpre;
	}

	
	public String downloadFilePreFormat() {
		trace("***************** downloadFilePreFormat is begin ****************** \n\n");
		enctrace("******************** downloadFilePreFormat is begin ****************** \n\n");
		String actname = getRequest().getParameter("submit");
		trace("action name is " + actname);
		String instid = comInstId();
		String usercode = comUserId();
		String bin = getRequest().getParameter("cardtype");
		String prefilename = getRequest().getParameter("prefilename");
		HttpSession session = getRequest().getSession();
		String tablename = "PERS_PRE_DATA";
		InstCardPREProcess instprocess = new InstCardPREProcess();
		JSONObject listofpreheaders = this.generatePREHeader();
		JSONObject predbfields = this.downloadFilePreBDField();
		// ExcelGenerator excelgen = new ExcelGenerator();

		// added by gowtham_230719
		String ip = (String) session.getAttribute("REMOTE_IP");

		IfpTransObj transact = commondesc.myTranObject("PREDONW", txManager);
		try {

			if (actname.equals("Delete")) {
				String delmsg = null;
				int predel;
				try {
					predel = commondesc.deletePREFiles(instid, bin, prefilename, tablename, jdbctemplate);
					if (predel < 0) {
						session.setAttribute("preverr", "E");
						delmsg = "No Records Deleted";
					} else if (predel == 0) {
						session.setAttribute("preverr", "E");
						delmsg = "Download the file atleast one time. Then try again to delete";
					} else if (predel > 0) {
						txManager.commit(transact.status);
						delmsg = predel + " Records Deleted successfully ";
						session.setAttribute("preverr", "S");
					}
					System.out.println("delete rec count is " + predel);
				} catch (Exception e) {
					txManager.rollback(transact.status);
					session.setAttribute("preverr", "E");
					delmsg = "Error while delete Records " + e;
					e.printStackTrace();
				}
				session.setAttribute("prevmsg", delmsg);
				return personalPredownloadhome();
			}

			// Changed the PREFile generated flow for Orient Bank Requirement

			/*
			 * trace("GEnerating Pre Sequence no"); int preSeqNo =
			 * this.sequencePREFILE(instid, jdbctemplate);
			 * 
			 * 
			 * trace("Generating presonalization file...."); int x =
			 * generatePRE(instid, bin, prefilename, tablename, preSeqNo,
			 * session, jdbctemplate, commondesc ); trace(
			 * "Generating presonalization file....got : " + x); if( x < 0 ){
			 * 
			 * 
			 * return "required_home"; }
			 */
			Properties prop = commondesc.getCommonDescProperty();
			String prefilelocation = prop.getProperty("PREFILELOCATION");
			fileName = prefilename.replaceAll(" ", "_");
			String predtls[] = prefilename.split("_");
			String predate = predtls[2];
			String datesplit = predate.substring(0, 4);
			String year = predate.substring(6, 8);
			predate = datesplit + year;
			enctrace("prefilename--->" + prefilename);
			String username = comUsername();
			inputStreampers = new FileInputStream(
					new File(prefilelocation + "/" + predate + "/" + prefilename + ".mc"));
			int downcnt = instprocess.updatePersonalizePreFileDownloadCnt(instid, prefilename, jdbctemplate);
			if (downcnt < 0) {
				txManager.rollback(transact.status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Could not continue the download process..");
				trace("Could not update the download count. got rolled back");
			}

			trace("update PRE seqno ...");
			int preseq = jdbctemplate.update(this.updatePRESEQNO(instid));

			if (preseq < 0) {
				txManager.rollback(transact.status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Could not continue the download process..");
				trace("Could not update the PRE SEQ NO got rolled back");
			}

			session.setAttribute("preverr", "S");
			String filename = "<span style='color:maroon'>" + session.getAttribute("PRENAME") + "</span>";

			// session.setAttribute("prevmsg", "Personalization file downloaded
			// successfully <br/> "+filename+" ");
			trace("Persionalization file generated successfully under the specified folder.File name[ " + prefilename
					+ " ].");

			txManager.commit(transact.status);

			/*************
			 * AUDIT BLOCK Edited by sardar on 11-12-15
			 **************/
			try {
				auditbean.setActmsg("Personalization File [ " + prefilename + " ] Downloaded");
				auditbean.setUsercode(username);
				auditbean.setAuditactcode("0104");
				auditbean.setPrefilename((String) session.getAttribute("PRENAME"));
				auditbean.setProduct(bin);

				// added by gowtham_230719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				// commondesc.insertAuditTrail(instid, username, auditbean,
				// jdbctemplate, txManager);
				commondesc.insertAuditTrailPendingCommit(instid, usercode, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/*************
			 * AUDIT BLOCK Ended By sardar on 11-12-15
			 **************/

		} catch (Exception e) {
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception : could not continue the download proces...");
			trace("Exception : could not continue the dowload process : " + e.getMessage());
			e.printStackTrace();
		}

		return "predownload";
		// IfpTransObj transObj = commondesc.myTranObject();

	}

	public int sequencePREFILE(String inst_id, JdbcTemplate jdbctemplate) {
		int x = -1;

		/*
		 * String AccoutSubtypeseq =
		 * "SELECT PREFILE_SEQ FROM SEQUENCE_MASTER WHERE INST_ID='" + inst_id +
		 * "'"; enctrace("getting sequence master values" + AccoutSubtypeseq); x
		 * = jdbctemplate.queryForInt(AccoutSubtypeseq);
		 */

		// by gowtham-260819
		String AccoutSubtypeseq = "SELECT PREFILE_SEQ FROM SEQUENCE_MASTER WHERE INST_ID=?";
		enctrace("getting sequence master values" + AccoutSubtypeseq);
		x = jdbctemplate.queryForInt(AccoutSubtypeseq, new Object[] { inst_id });

		return x;
	}

	public synchronized String updatePRESEQNO(String instid) {
		return "update SEQUENCE_MASTER set PREFILE_SEQ=PREFILE_SEQ+1 where INST_ID='" + instid + "'";
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
		jsonpre.put("H19", "EmbCardno");
		// jsonpre.put("H20", "CIN");
		jsonpre.put("H20", "BRANCH_NAME");
		jsonpre.put("H21", "CARD_TYPE");
		// jsonpre.put("H22", "BRANCH_NAME");
		return jsonpre;
	}

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


	public int createPREContent(FileWriter writer, String instid, String productcode, int cardcount, String prefilename,
			String tablename, int seqNo, JdbcTemplate jdbctemplate) throws Exception {

		// Archana
		trace("<---------CREATE PRE CONTENT METHOD CALLED------->");
		enctrace("<---------CREATE PRE CONTENT METHOD CALLED------->");
		trace("instid{" + instid + "}productcode{" + productcode + "}prefilename{" + prefilename + "}tablename"
				+ tablename);
		String preData = null;
		StringBuilder sb = null;

		// int total_count = 999999;
		String Cardstotalcount = "";
		String PRE_HEADER = "EMVPR";
		int card_sequence = 1;

		String card_counts = Integer.toString(cardcount);

		// if((cardcount)<=(total_count)){
		Cardstotalcount = StringUtils.leftPad(card_counts, 6, "0");
		// }
		Format formatter = new SimpleDateFormat("yyyyMMdd");
		String s = formatter.format(date);
		System.out.println("Getting date" + s);
		Format timeformater = new SimpleDateFormat("hhmmss");
		String m = timeformater.format(date);
		System.out.println("Getting date" + m);
		trace("PRE Header--->" + PRE_HEADER + "/" + Cardstotalcount + s + m + "\n");
		// writer.write(PRE_HEADER + "/" + Cardstotalcount + s + m + "\n");
		try {
			String preData1 = "";
			StringBuilder wrPre = new StringBuilder();

			wrPre.append("select TRACK_DATA");
			wrPre.append("  from " + tablename + " WHERE PRE_NAME = '" + prefilename + "' AND PRODUCT_CODE = '"
					+ productcode + "'ORDER BY EMB_CARDNO  ");
			enctrace("Formed PRE-FILE GETTING QUERY---" + wrPre);

			List wrPreList = jdbctemplate.queryForList(wrPre.toString());
			/*
			 * 
			 * //by gowtham-260819 wrPre.append("select TRACK_DATA");
			 * wrPre.append("  from " + tablename +
			 * " WHERE PRE_NAME = ? AND PRODUCT_CODE =? ORDER BY EMB_CARDNO  ");
			 * enctrace("Formed PRE-FILE GETTING QUERY---" + wrPre); List
			 * wrPreList = jdbctemplate.queryForList(wrPre.toString(),new
			 * Object[]{prefilename,productcode});
			 */

			if (!(wrPreList.isEmpty())) {
				Iterator pre_itr = wrPreList.iterator();
				while (pre_itr.hasNext()) {
					String sequence = Integer.toString(card_sequence);
					// String cardsequence = StringUtils.leftPad(sequence, 6,
					// "0");
					// trace("Card Sequence incrementation" + cardsequence);
					Map map = (Map) pre_itr.next();
					preData = ((String) map.get("TRACK_DATA"));

					// by siva 05-07-2019

					// trace("preData"+preData);
					// preData1 = EncDec.decrypt(preData);
					trace("preData1 ===== " + preData);

					// by siva 05-07-2019

					// writer.write(cardsequence + preData + "\n");
					writer.write(preData + "\n");
					// card_sequence++;
				}
				sb = new StringBuilder(preData);
			}
		} catch (Exception e) {
			e.printStackTrace();
			trace("Exception in PRE FILE Generation " + e);
		} /*
			 * finally { //nullify the objects
			 * 
			 * preData = null; //sb.setLength(0); sb=null; }
			 */
		trace(" <-----PRE  File Content Generated Successfully------> ");
		enctrace(" <-----PRE  File Content Generated Successfully------> ");
		return 1;

	}

	// BY SIVA 17-07-2019 07:45 PM
	/*
	 * public int createPREContent(FileWriter writer, String instid, String
	 * productcode, int cardcount, String prefilename, String tablename, int
	 * seqNo, JdbcTemplate jdbctemplate) throws Exception {
	 * 
	 * // Archana trace("<---------CREATE PRE CONTENT METHOD CALLED------->");
	 * enctrace("<---------CREATE PRE CONTENT METHOD CALLED------->");
	 * trace("instid{" + instid + "}productcode{" + productcode +
	 * "}prefilename{" + prefilename + "}tablename" + tablename); String preData
	 * = ""; int total_count = 999999; String Cardstotalcount = ""; String
	 * PRE_HEADER_BIC = "EMVPR"; int card_sequence = 1;
	 * 
	 * String card_counts = Integer.toString(cardcount);
	 * 
	 * // if((cardcount)<=(total_count)){ Cardstotalcount =
	 * StringUtils.leftPad(card_counts, 6, "0"); // } Format formatter = new
	 * SimpleDateFormat("yyyyMMdd"); String s = formatter.format(date);
	 * System.out.println("Getting date" + s); Format timeformater = new
	 * SimpleDateFormat("hhmmss"); String m = timeformater.format(date);
	 * System.out.println("Getting date" + m); trace("PRE Header--->" +
	 * PRE_HEADER_BIC + "/" + Cardstotalcount + s + m + "\n");
	 * writer.write(PRE_HEADER_BIC + "/" + Cardstotalcount + s + m + "\n"); try
	 * {
	 * 
	 * StringBuilder wrPre = new StringBuilder(); wrPre.append(
	 * "select TRACK_DATA"); wrPre.append("  from " + tablename +
	 * " WHERE PRE_NAME = '" + prefilename + "' AND PRODUCT_CODE = '" +
	 * productcode + "'ORDER BY EMB_CARDNO  "); enctrace(
	 * "Formed PRE-FILE GETTING QUERY---" + wrPre);
	 * 
	 * List wrPreList = jdbctemplate.queryForList(wrPre.toString()); if
	 * (!(wrPreList.isEmpty())) { Iterator pre_itr = wrPreList.iterator(); while
	 * (pre_itr.hasNext()) { String sequence = Integer.toString(card_sequence);
	 * String cardsequence = StringUtils.leftPad(sequence, 6, "0"); trace(
	 * "Card Sequence incrementation" + cardsequence); Map map = (Map)
	 * pre_itr.next(); preData = ((String) map.get("TRACK_DATA"));
	 * 
	 * // by siva 05-07-2019
	 * 
	 * trace("preData"+preData); String preData1 = EncDec.decrypt(preData);
	 * trace("preData1"+preData1);
	 * 
	 * // by siva 05-07-2019
	 * 
	 * writer.write(cardsequence + preData + "\n"); card_sequence++; }
	 * 
	 * } } catch (Exception e) { trace("Exception in PRE FILE Generation " + e);
	 * } trace(" <-----PRE  File Content Generated Successfully------> ");
	 * enctrace(" <-----PRE  File Content Generated Successfully------> ");
	 * return 1;
	 * 
	 * }
	 */

	public List getneratePREList(String instid, String productcode, String prefilename, String query,
			JdbcTemplate jdbctemplate) {
		List predata = null;

		/*
		 * String predataqry = query + " WHERE INST_ID='" + instid +
		 * "' AND PRODUCT_CODE='" + productcode + "' AND PRE_NAME='" +
		 * prefilename + "'"; enctrace("pre gen predataqry: " + predataqry);
		 * predata = jdbctemplate.queryForList(predataqry);
		 */

		// by gowtham-260819
		String predataqry = query + " WHERE INST_ID=? AND PRODUCT_CODE=? AND PRE_NAME=? ";
		enctrace("pre gen predataqry: " + predataqry);
		predata = jdbctemplate.queryForList(predataqry, new Object[] { instid, productcode, prefilename });

		return predata;
	}

	private InputStream inputStream;

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	private String fileName;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String fromdate;
	public String todate;

	public String getFromdate() {
		return fromdate;
	}

	public void setFromdate(String fromdate) {
		this.fromdate = fromdate;
	}

	public String getTodate() {
		return todate;
	}

	public void setTodate(String todate) {
		this.todate = todate;
	}

	private InputStream inputStreampers;

	public InputStream getInputStreampers() {
		return inputStreampers;
	}

	public void setInputStreampers(InputStream inputStreampers) {
		this.inputStreampers = inputStreampers;
	}

	public int generatePREInst(String instid, String productcode, int cardcount, String prefilename, String tablename,
			int preSeqNo, HttpSession session, JdbcTemplate jdbctemplate, CommonDesc commondesc,
			PreprocessAction preprocess) throws Exception {
		trace("PRE-FILE GENERATION LOGIC");
		Properties prop = commondesc.getCommonDescProperty();
		String prefilelocation = "";
		if (instid.equalsIgnoreCase("PMT")) {
			prefilelocation = prop.getProperty("PREFILELOCATION");
		} else {
			prefilelocation = prop.getProperty("PREFILELOCATION");
			trace("PRE FILE LOCATION----->" + prefilelocation);
		}

		// this.setOutputFile(prefilelocation);
		String prodname = commondesc.getProductdesc(instid, productcode, jdbctemplate);
		String prodkeyname = prodname.replace(" ", "_");
		String downprefilename = productcode + "[" + prodkeyname + "]" + "_" + commondesc.getDateTimeStamp() + ".pre";
		trace("PRE Filename---------> " + downprefilename);
		File nwfile = new File(prefilelocation + "/" + downprefilename);
		session.setAttribute("PRENAME", downprefilename);
		File prefiledir = new File(prefilelocation);
		trace("PRE FILE DIRECTORY----->" + prefilelocation);
		prefiledir.mkdir();
		if (prefiledir.exists()) {
			trace("Inside if condition");
			if (!nwfile.exists()) {
				nwfile.createNewFile();
			}
		} else {
			trace("COULD NOT UNDERSTAND THE PRE FILE PATH SPECIEFIED [ " + prefiledir + " ] ");
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg",
					"COULD NOT UNDERSTAND THE PERSONALIZATION FILE PATH SPECIEFIED [ " + prefiledir + " ]");
			return -1;
		}
		trace("PRE FILE CREATED ....");
		FileWriter writer = new FileWriter(nwfile);
		try {
			preprocess.createPREContent(writer, instid, productcode, cardcount, prefilename, tablename, preSeqNo,
					jdbctemplate);
			trace("CONTENT CREATED");

		} catch (Exception e) {
			writer.close();
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "COULD NOT BE DOWNLOADED " + e.getMessage());
			trace("Exception ..generating pre file : " + e.getMessage());
			e.printStackTrace();
			return -1;
		} finally {
			writer.close();
		}
		trace("Check the result file under  " + nwfile);

		return 1;
	}

	public List getting_cardcount(String bin, String prefilename, JdbcTemplate jdbctemplate) throws Exception {

		List cardlist = null;
		try {

			/*
			 * String cardlistqry =
			 * "SELECT DOWN_CNT, PRE_NAME, COUNT(*) AS CARDCOUNT  FROM PERS_PRE_DATA WHERE PRODUCT_CODE='"
			 * + bin + "' AND  PRE_NAME='" + prefilename +
			 * "'  GROUP BY PRE_NAME,DOWN_CNT"; enctrace("cardlistqry : " +
			 * cardlistqry); cardlist = jdbctemplate.queryForList(cardlistqry);
			 */

			// by gowtham-260819
			String cardlistqry = "SELECT DOWN_CNT, PRE_NAME, COUNT(*) AS CARDCOUNT  FROM PERS_PRE_DATA WHERE PRODUCT_CODE=? AND  PRE_NAME=?  GROUP BY PRE_NAME,DOWN_CNT";
			enctrace("cardlistqry : " + cardlistqry);
			cardlist = jdbctemplate.queryForList(cardlistqry, new Object[] { bin, prefilename, });

			return cardlist;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cardlist;
	}

	// by siva

	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);

	}

	public static boolean isSolaris() {

		return (OS.indexOf("sunos") >= 0);

	}

	// by siva

	public int deletepre(String instid, String prefilename, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;

		/*
		 * String delteqry = "DELETE FROM PERS_PRE_DATA WHERE INST_ID='"+instid+
		 * "' AND PRE_NAME='"+prefilename+"'"; enctrace("delete query -->"
		 * +delteqry); x = jdbctemplate.update(delteqry);
		 */

		// by gowtham-260819
		String delteqry = "DELETE FROM PERS_PRE_DATA WHERE INST_ID=? AND PRE_NAME=? ";
		enctrace("delete query -->" + delteqry);
		x = jdbctemplate.update(delteqry, new Object[] { instid, prefilename });

		return x;
	}

	public String prefileDownload() {

		Properties prop = commondesc.getCommonDescProperty();
		String prefilelocation = prop.getProperty("PREFILELOCATION");

		trace("instantpre" + prefilelocation);

		String actname = getRequest().getParameter("submit");
		// System.out.println( "action name is " + actname );
		String instid = comInstId();
		String usercode = comUserCode();
		String branchname = getRequest().getParameter("branchcode");
		System.out.println("branchname--->"+branchname);
		String FINDbranchnameQUERY="",FINDbranchname="";
		String bin = getRequest().getParameter("cardtype");
		String prefilename = getRequest().getParameter("prefilename");
		HttpSession session = getRequest().getSession();
		String tablename = "PERS_PRE_DATA";
		IfpTransObj transact = commondesc.myTranObject("PREDOWNLOAD", txManager);

		JSONObject listofpreheaders = this.generatePREHeader();
		JSONObject predbfields = this.generatePREFDBFields();
		ExcelGenerator excelgen = new ExcelGenerator();
		try {

			if (actname.equals("Delete")) {
				String delmsg = null;
				int predel;
				try {
					predel = commondesc.deletePREFiles(instid, bin, prefilename, tablename, jdbctemplate);
					if (predel < 0) {
						session.setAttribute("preverr", "E");
						delmsg = "No Records Deleted";
					} else if (predel == 0) {
						session.setAttribute("preverr", "E");
						delmsg = "Download the file atleast one time. Then try again to delete";
					} else if (predel > 0) {
						delmsg = predel + " Records Deleted successfully ";
						session.setAttribute("preverr", "S");
					}
					System.out.println("delete rec count is " + predel);
				} catch (Exception e) {
					session.setAttribute("preverr", "E");
					delmsg = "Error while delete Records " + e;
					e.printStackTrace();
				}
				session.setAttribute("prevmsg", delmsg);
				return personalPredownloadhome();
			}

			 FINDbranchnameQUERY= "SELECT BRANCH_NAME FROM BRANCH_MASTER WHERE BRANCH_CODE='"+branchname+"' AND AUTH_CODE='1'";
			FINDbranchname = (String) jdbctemplate.queryForObject(FINDbranchnameQUERY, String.class);
			System.out.println("FINDbranchname-->"+FINDbranchname);
			
			trace("Generating presonalization file....excel  " + predbfields);
			int x = excelgen.generatePREbranchname(instid, bin, prefilename,FINDbranchname, tablename, listofpreheaders, predbfields, session,
					jdbctemplate, commondesc);
			trace("Generating presonalization file....got : " + x);
			if (x < 0) {
				return "required_home";
			}

			int downcnt = updatePreFileDownloadCnt(instid, prefilename, jdbctemplate);
			if (downcnt < 0) {
				// commondesc.rollbackTxn(jdbctemplate);
				txManager.rollback(transact.status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Could not continue the download process..");
				trace("Could not update the download count. got rolled back");
			}
			// commondesc.commitTxn(jdbctemplate);
			txManager.commit(transact.status);
			session.setAttribute("preverr", "S");
			String filename = "<span style='color:maroon'>" + prefilelocation + session.getAttribute("PRENAME")
					+ "</span>";

			session.setAttribute("prevmsg",
					"Persionalization file generated successfully under the specified folder.File name : <br/> "
							+ filename + "  ");
			trace("Persionalization file generated successfully under the specified folder.File name[ " + filename
					+ " ].");

		} catch (Exception e) {
			// commondesc.rollbackTxn(jdbctemplate);
			txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Exception : could not continue the download proces...");
			trace("Exception : could not continue the dowload process : " + e.getMessage());
			e.printStackTrace();
		}

		return personalPredownloadhome();

		// IfpTransObj transObj = commondesc.myTranObject();
	}

}
