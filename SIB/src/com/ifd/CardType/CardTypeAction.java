package com.ifd.CardType;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.dao.support.DaoSupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.beans.CardActivationBeans;
import com.ifd.CardType.CardTypeActionDao;
import com.ifd.beans.PersonalizeBean;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

import test.Date;
import test.Validation;

public class CardTypeAction extends BaseAction {

	CommonDesc commondesc = new CommonDesc();
	AuditBeans auditbean = new AuditBeans();
	PersonalizeBean bean = new PersonalizeBean();

	public PersonalizeBean getBean() {
		return bean;
	}

	public void setBean(PersonalizeBean bean) {
		this.bean = bean;
	}

	public AuditBeans getAuditbean() {
		return auditbean;
	}

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
	}

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	private PlatformTransactionManager txManager = new DataSourceTransactionManager();

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public String comInstId(HttpSession session) {
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String comUserId(HttpSession session) {
		String userid = (String) session.getAttribute("USERID");
		return userid;
	}

	JdbcTemplate jdbctemplate = new JdbcTemplate();

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	List cardtypelist;
	List binlist;
	String cardtypelenval;

	public String getCardtypelenval() {
		return cardtypelenval;
	}

	public void setCardtypelenval(String cardtypelenval) {
		this.cardtypelenval = cardtypelenval;
	}

	public List getBinlist() {
		return binlist;
	}

	public void setBinlist(List binlist) {
		this.binlist = binlist;
	}

	public List getCardtypelist() {
		return cardtypelist;
	}

	public void setCardtypelist(List cardtypelist) {
		this.cardtypelist = cardtypelist;
	}

	String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	List x;

	public List getX() {
		return x;
	}

	public void setX(List x) {
		this.x = x;
	}

	List instlist;

	public List getInstlist() {
		return instlist;
	}

	public void setInstlist(List instlist) {
		this.instlist = instlist;
	}

	CardTypeActionDao cardTypeActionDao = new CardTypeActionDao();
	CardActivationBeans cardbean = new CardActivationBeans();

	public CardActivationBeans getCardbean() {
		return cardbean;
	}

	public void setCardbean(CardActivationBeans cardbean) {
		this.cardbean = cardbean;
	}

	public String addCardtype() {
		trace("*************** Adding Card Type Begins **********");
		enctrace("*************** Adding Card Type Begins **********");
		HttpSession session = getRequest().getSession();

		String instid = comInstId(session);
		try {
			String flag = getRequest().getParameter("act");
			trace("got act is ..........: " + flag);
			if (flag != null) {
				session.setAttribute("act", flag);
			}

			if (getRequest().getParameter("type") == "MERCH") {
				session.setAttribute("CURAPPTYPE", "MERCHANT");
				trace("Session Type.. MERCHANT");
			}
			instlist = cardTypeActionDao.instList(jdbctemplate);

			cardtypelenval = commondesc.getCardTypeLen(instid, jdbctemplate);
			trace("Getting bin length ..got  with cardtype: " + cardtypelenval);
			if (cardtypelenval == null) {
				addActionError("Card Type Length Not configured....");
				return "required_home";
			}

		} catch (Exception e) {
			addActionError(" Error ");
			enctrace("addCardtype Error ::::::" + e);

		}
		trace("\n\n");
		enctrace("\n\n");
		return "add_cardtype_home";

	}

	public String addCardtypeConfig() {
		trace("*************** Adding Card Type Begins **********");
		enctrace("*************** Adding Card Type Begins **********");
		HttpSession session = getRequest().getSession();
		try {
			if (getRequest().getParameter("type") == "MERCH") {
				session.setAttribute("CURAPPTYPE", "MERCHANT");
				trace("Session Type.. MERCHANT");
			}
			instlist = cardTypeActionDao.instList(jdbctemplate);

		} catch (Exception e) {
			addActionError(" Error ");
		}
		trace("\n\n");
		enctrace("\n\n");
		return "add_cardtype_home";

	}

	public String editCardtype() {
		trace("*************** Edit Card Type Begins **********");
		enctrace("*************** Edit Card Type Begins **********");
		HttpSession session = getRequest().getSession();
		String cardtypeqry = "";
		String type = getRequest().getParameter("type");
		String instid = comInstId(session);
		instlist = cardTypeActionDao.instList(jdbctemplate);

		try {
			List cardtype_detail_result = cardTypeActionDao.getCardtypeList(instid, jdbctemplate);
			System.out.println("result viewcardtypeDetail      " + cardtype_detail_result);
			setCardtypelist(cardtype_detail_result);
		} catch (Exception e) {
			addActionError("Error while getting  the card type list " + e);
			trace("Error while getting card type list " + e.getMessage());
		}

		trace("\n\n");
		enctrace("\n\n");
		return "edit_cardtype_home";
	}

	public String editCardtypeDetails() throws Exception {
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String cardtypeid = getRequest().getParameter("cardtypeid");
		trace("getting edit cardtypeid" + cardtypeid);
		if (cardtypeid.equals("-1")) {
			System.out.println("validation part cardtypeid");
			addActionError("Please select cardtype ");
			return editCardtype();
		}

		try {
			List cardtype_detail_result = cardTypeActionDao.editcardtypeResult(instid, cardtypeid, jdbctemplate);
			System.out.println("result viewcardtypeDetail      " + cardtype_detail_result);
			setCardtypelist(cardtype_detail_result);
		} catch (Exception e) {
			addActionError("Error while getting  the card type list " + e);
			trace("Error while getting card type list " + e.getMessage());
		}

		trace("*************** Viewing Card Type Begins **********");
		enctrace("*************** Viewing Card Type Begins **********");
		trace("\n\n");
		enctrace("\n\n");
		return "edit_cardtype_view";

	}

	public String editsaveCardType() throws Exception {
		trace("*************** update Card Type Begins **********");
		enctrace("*************** update Card Type Begins **********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);// getRequest().getParameter("instid");
		String usercode = comUserId(session);

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");
		Date date = new Date();

		String cardtpeid = getRequest().getParameter("cardtypeid");
		String cardtypedesc = getRequest().getParameter("cardtypename");

		bean.setCardtypename(cardtypedesc);
		boolean carddesc = Validation.Spccharcter(cardtypedesc);
		if (!carddesc) {
			addFieldError("cardtypename", " Please enter cardtype name in characters");
			return editCardtypeDetails();
		}

		IfpTransObj transact = commondesc.myTranObject("UPDATECARDTYPE", txManager);
		String flag = getRequest().getParameter("act");
		System.out.println("flag == " + flag);

		String userid = comUserId(session);
		String username = commondesc.getUserName(instid, userid, jdbctemplate);

		String auth_code = "1";
		String mkchkrstatus = "D";
		String authmsg = "";

		if (flag.equals("D")) {
			auth_code = "1";
			mkchkrstatus = "D";
		} else {
			auth_code = "0";
			mkchkrstatus = "M";
			authmsg = ". Waiting for Authorization";
		}

		try {

			/*
			 * String updateqry =cardTypeActionDao.updatecardtypedetails(instid,
			 * cardtpeid,
			 * cardtypedesc,mkchkrstatus,auth_code,usercode,jdbctemplate);
			 * enctrace("update qry __" + updateqry );
			 * commondesc.executeTransaction(updateqry, jdbctemplate);
			 */

			//// by gowtham300819
			String updateqry = cardTypeActionDao.updatecardtypedetails(instid, cardtpeid, cardtypedesc, mkchkrstatus,
					auth_code, usercode, jdbctemplate);
			/* commondesc.executeTransaction(updateqry ,jdbctemplate); */
			jdbctemplate.update(updateqry, new Object[] { cardtypedesc, mkchkrstatus, auth_code, usercode,
					date.getCurrentDate(), instid, cardtpeid });

			transact.txManager.commit(transact.status);
			addActionMessage("Card type \"" + cardtypedesc + "\" Updated successfully" + authmsg);
			trace("Card type updated successfully");

			try {

				// added by gowtham_220719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setActmsg(
						"Card Type [ " + cardtypedesc + " - " + cardtpeid + " ] Updated Successfully. " + authmsg);
				auditbean.setUsercode(username);
				auditbean.setAuditactcode("3030");
				commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}

			/************* AUDIT BLOCK **************/

		} catch (NumberFormatException ne) {
			transact.txManager.rollback(transact.status);
			addActionError("Invalid update type id...");
			trace(" Could not update card type. " + ne.getMessage());
			ne.printStackTrace();
		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			addActionError(" Could not update card type. ");
			trace(" Could not update card type. " + e.getMessage());
			e.printStackTrace();
		}
		trace("\n\n");
		enctrace("\n\n");
		return "required_home";
	}

	public String saveCardType() {
		trace("*************** Save Card Type Begins **********");
		enctrace("*************** Save Card Type Begins **********");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);// getRequest().getParameter("instid");
		String usercode = comUserId(session);

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");
		String blen = "";
		String bin = "";
		String saveqry = "";

		IfpTransObj transact = commondesc.myTranObject("SAVECARDTYPE", txManager);
		// String cardtpeid = commondesc.fchCardTypeSequance(instid,
		// jdbctemplate);
		String cardtpeid = getRequest().getParameter("cardtypeid");
		String cardtypedesc = getRequest().getParameter("cardtypename");
		String cardtypelenval = getRequest().getParameter("cardtypelenval");

		bean.setCardtpeid(cardtpeid);
		System.out.println("cardtpeid " + cardtpeid);
		bean.setCardtypename(cardtypedesc);

		int cardid = Validation.number(cardtpeid);
		if (cardid == 0) {
			addFieldError("cardtypeid", " Please enter cardtype id in number");
			return addCardtype();
		}

		int cardid_len = cardtpeid.length();
		int cardtypelenval_len = Integer.parseInt(cardtypelenval);
		System.out.println("cardid_len " + cardid_len);
		System.out.println("cardtypelenval_len " + cardtypelenval_len);
		if (cardid_len != cardtypelenval_len) {
			System.out.println("cardid_len111111 " + cardtypelenval_len);
			addActionError("Card type value length should be " + cardtypelenval_len + " digit");
			return addCardtype();
		}
		boolean carddesc = Validation.Spccharcter(cardtypedesc);
		if (!carddesc) {
			addFieldError("cardtypename", " Please enter cardtype name in characters");
			return addCardtype();
		}

		String flag = getRequest().getParameter("act");
		System.out.println("flag == " + flag);
		String auth_code = "1";
		String mkchkrstatus = "D";
		String authmsg = "";

		String userid = comUserId(session);
		String username = commondesc.getUserName(instid, userid, jdbctemplate);

		if (flag.equals("D")) {
			auth_code = "1";
			mkchkrstatus = "D";
		} else {
			auth_code = "0";
			mkchkrstatus = "M";
			authmsg = ". Waiting for Authorization";
		}

		String merchallowed = "0";
		String prepaidallowed = "1";
		if (session.getAttribute("CURAPPTYPE") == "MERCHANT") {
			merchallowed = "1";
		}
		trace("Checking card type existance..");

		if (cardTypeActionDao.checkCardTypeExist(cardtpeid, instid, jdbctemplate) != 0) {
			addActionError(cardtypedesc + " Card type Already exist");
			trace(cardtypedesc + " Card type Already exist");
			return this.addCardtypeConfig();
		}

		int cardtypenameexist = cardTypeActionDao.checkCardTypeNameExist(cardtypedesc, instid, jdbctemplate);
		trace("Checking card type name existance..got : " + cardtypenameexist);
		if (cardtypenameexist != 0) {
			addActionError(" Card type name [ " + cardtypedesc + " ] Alredy exist. Try with another name");
			trace(cardtypedesc + " Card type Already exist");
			return this.addCardtypeConfig();
		}

		try {

			saveqry = cardTypeActionDao.getSaveqry(instid, cardtpeid, cardtypedesc, merchallowed, prepaidallowed,
					usercode, auth_code, mkchkrstatus);
			// enctrace("save qry __" + saveqry );
			commondesc.executeTransaction(saveqry, jdbctemplate);
			transact.txManager.commit(transact.status);
			addActionMessage("Card type \"" + cardtypedesc + "\" configured successfully" + authmsg);
			trace("Card type saved successfully");

			//// Inserting Card Base No

			if (flag.equals("D")) {

				List binlist = cardTypeActionDao.gettingBinList(instid, jdbctemplate);
				trace("bin list size " + binlist);
				if (!binlist.isEmpty()) {
					ListIterator lstitr = binlist.listIterator();
					int insertswitchstatus[] = new int[binlist.size()];
					String ctbaseinsert[] = new String[binlist.size()];
					int i = 0;
					while (lstitr.hasNext()) {
						Map mp = (Map) lstitr.next();
						ctbaseinsert[i] = "INSERT INTO BASENO(INST_ID,BASENO_CODE,CHN_BASE_NO,BASE_NO_LEN,BIN) VALUES('"
								+ instid + "','" + cardtpeid + "','1','" + mp.get("BASELEN") + "','" + mp.get("BIN")
								+ "')";
						enctrace("insert baseno query " + ctbaseinsert[i]);
						i++;
					}
					insertswitchstatus = jdbctemplate.batchUpdate(ctbaseinsert);
					trace("batch update got..." + insertswitchstatus.length);
				} else {

					trace("NO BINS ARE THERE BASED ON CARDTYPE...FOR CARD GENERATED SEQUANCE");
				}

			}

			/************* AUDIT BLOCK **************/
			try {

				// added by gowtham_220719
				trace("ip address======>  " + ip);
				auditbean.setIpAdress(ip);

				auditbean.setActmsg("Card Type [ " + cardtypedesc + " - " + cardtpeid + " ] configured. " + authmsg);
				auditbean.setUsercode(username);
				auditbean.setAuditactcode("3030");
				commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
			} catch (Exception audite) {
				trace("Exception in auditran : " + audite.getMessage());
			}
			/************* AUDIT BLOCK **************/

		} catch (NumberFormatException ne) {
			transact.txManager.rollback(transact.status);
			addActionError("Invalid card type id...");
			trace(" Could not save card type. " + ne.getMessage());
			ne.printStackTrace();
		} catch (Exception e) {
			transact.txManager.rollback(transact.status);
			addActionError(" Could not save card type. ");
			trace(" Could not save card type. " + e.getMessage());
			e.printStackTrace();
		}
		trace("\n\n");
		enctrace("\n\n");
		return "required_home";
	}

	public String viewCardtype() {
		trace("*************** Viewing Card Type Begins **********");
		enctrace("*************** Viewing Card Type Begins **********");
		HttpSession session = getRequest().getSession();
		String cardtypeqry = "";
		String type = getRequest().getParameter("type");
		instlist = cardTypeActionDao.instList(jdbctemplate);
		trace("\n\n");
		enctrace("\n\n");
		return "view_cardtype_home";
	}

	public void listOfCardType() throws IOException {
		HttpSession session = getRequest().getSession();
		String instid = getRequest().getParameter("instid");

		cardtypelist = cardTypeActionDao.getCardtypeList(instid, jdbctemplate);
		String opt = "<option value='-1'>-SELECT-</option>";

		if (!cardtypelist.isEmpty()) {
			Iterator itr = cardtypelist.iterator();
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				String cardtypeid = (String) mp.get("CARD_TYPE_ID");
				String cardtypedesc = (String) mp.get("CARD_TYPE_DESC");
				opt += "<option value=" + cardtypeid + ">" + cardtypedesc + "</option>";
			}
		}
		getResponse().getWriter().write(opt);
	}

	public void fchCardtypeDesc() throws IOException {

		String cardtypeid = getRequest().getParameter("cardtypeid");
		String cardtypedesc = cardTypeActionDao.fchCardtypeDesc(cardtypeid);
		String cardtype = (String) jdbctemplate.queryForObject(cardtypedesc, String.class);
		getResponse().getWriter().write(cardtype);
	}

	public String authCardtype() {
		HttpSession session = getRequest().getSession();

		String instid = comInstId(session);
		try {
			String cardtypeid = getRequest().getParameter("cardtype");

			if (cardtypeid.equals("-1")) {
				System.out.println("validation part cardtypeid");
				addActionError(" Please select card type");
				return authListCardtype();
			}

			List getallcardtype_qury = cardTypeActionDao.getCardTypeListByCardIDForauth(instid, jdbctemplate,
					cardtypeid);
			if (!getallcardtype_qury.isEmpty()) {
				ListIterator itr = getallcardtype_qury.listIterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					String makerid = (String) mp.get("MAKER_ID");
					String username = commondesc.getUserName(instid, makerid, jdbctemplate);
					mp.put("USER_NAME", username);
					itr.remove();
					itr.add(mp);
				}
				System.out.println(getallcardtype_qury + "   ---getallsubfee_qury---   ");
				cardbean.setCardtype(getallcardtype_qury);
			} else {
				addActionError("Records not found");
			}
		} catch (Exception e) {
			addActionError("Error while deleting the Fee " + e);
			trace("Error while deleting the Fee " + e.getMessage());
		}
		return "authcardtype";
	}

	public String authListCardtype() {
		HttpSession session = getRequest().getSession();

		String instid = comInstId(session);
		try {
			List getallcardtype_qury = cardTypeActionDao.getAllCardTypeList(instid, jdbctemplate);
			if (!getallcardtype_qury.isEmpty()) {
				System.out.println(getallcardtype_qury + "   ---getallsubfee_qury---   ");
				cardbean.setCardtype(getallcardtype_qury);
			} else {
				addActionError("Records not found");
				return "required_home";
			}
		} catch (Exception e) {
			addActionError("Error while deleting the Fee " + e);
			trace("Error while deleting the Fee " + e.getMessage());
		}
		return "authselectcardtype";
	}

	public String authdeauthcardtype() {
		trace("*************card generation authorization *****************");
		enctrace("*************card generation authorization *****************");
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("AUTHCARDTYPE", txManager);
		String statusmsg = "", remarks = "";
		int ezcardinfoUpd = 0;

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");
		Date date = new Date();
		try {
			String instid = comInstId(session);
			String userid = comUserId(session);
			// String username = commondesc.getUserName(instid, userid,
			// jdbctemplate);
			String cardtypeid = getRequest().getParameter("cardtypeid");
			System.out.println("cardtypeid === " + cardtypeid);
			String authstatus = "";
			String update_authdeauth_qury = null;
			String auth = getRequest().getParameter("auth");
			System.out.println("auth-- " + auth);

			String username = commondesc.getUserName(instid, userid, jdbctemplate);

			if (auth.equals("Authorize")) {
				System.out.println("AUTHORIZE...........");
				authstatus = "P";
				statusmsg = " Authorized ";

				int validreissucard = cardTypeActionDao.getcardreissucardvalid(instid, cardtypeid.toString(),
						jdbctemplate);
				if (validreissucard > 0) {
					update_authdeauth_qury = cardTypeActionDao.updateAuthCardType(authstatus, userid, instid,
							cardtypeid);
				} else {

					StringBuilder ezcardinfo = new StringBuilder();

					/*
					 * ezcardinfo.append("INSERT INTO EZCARDTYPE ");
					 * ezcardinfo.append(
					 * "select INST_ID INSTID,CARD_TYPE_ID CARDTYPE,CARD_TYPE_DESC DESCRIPTION  from CARD_TYPE where CARD_TYPE_ID "
					 * ); ezcardinfo.append(
					 * "NOT IN (SELECT CARDTYPE FROM EZCARDTYPE WHERE INSTID='"
					 * +instid+"') AND INST_ID='"+instid+"' ");
					 * 
					 * enctrace("ezcardinfo:::::"+ezcardinfo.toString());
					 * ezcardinfoUpd =
					 * jdbctemplate.update(ezcardinfo.toString());
					 */

					// by gowtham-190819
					ezcardinfo.append("INSERT INTO EZCARDTYPE ");
					ezcardinfo.append(
							"select INST_ID INSTID,CARD_TYPE_ID CARDTYPE,CARD_TYPE_DESC DESCRIPTION  from CARD_TYPE where CARD_TYPE_ID ");
					ezcardinfo.append("NOT IN (SELECT CARDTYPE FROM EZCARDTYPE WHERE INSTID=?) AND INST_ID=? ");

					enctrace("ezcardinfo:::::" + ezcardinfo.toString());
					ezcardinfoUpd = jdbctemplate.update(ezcardinfo.toString(), new Object[] { instid, instid });

					trace("ezcardinfoUpd:::" + ezcardinfoUpd);

					//// Inserting Card Base No

					List binlist = cardTypeActionDao.gettingBinList(instid, jdbctemplate);
					trace("bin list size " + binlist);
					if (!binlist.isEmpty()) {
						ListIterator lstitr = binlist.listIterator();
						int insertswitchstatus[] = new int[binlist.size()];
						String ctbaseinsert[] = new String[binlist.size()];
						int i = 0;
						while (lstitr.hasNext()) {
							Map mp = (Map) lstitr.next();
							ctbaseinsert[i] = "INSERT INTO BASENO(INST_ID,BASENO_CODE,CHN_BASE_NO,BASE_NO_LEN,BIN) VALUES('"
									+ instid + "','" + cardtypeid + "','1','" + mp.get("BASELEN") + "','"
									+ mp.get("BIN") + "')";
							enctrace("insert baseno query " + ctbaseinsert[i]);
							i++;
						}
						insertswitchstatus = jdbctemplate.batchUpdate(ctbaseinsert);
						trace("batch update got..." + insertswitchstatus.length);
						if (insertswitchstatus.length >= 1) {
							update_authdeauth_qury = cardTypeActionDao.updateauthcodecardtype(authstatus, userid,
									remarks, instid, cardtypeid);
							//int update_authdeauth = jdbctemplate.update(update_authdeauth_qury);

							//trace("Commiting..");
							//txManager.commit(transact.status);
						}

					} else {
						update_authdeauth_qury = cardTypeActionDao.updateauthcodecardtype(authstatus, userid, remarks,
								instid, cardtypeid);
						//int update_authdeauth = jdbctemplate.update(update_authdeauth_qury);

						//trace("Commiting..");
						//txManager.commit(transact.status);
						trace("NO BINS ARE THERE BASED ON CARDTYPE...FOR CARD GENERATED SEQUANCE");
					}
				}

			} else {
				authstatus = "D";
				statusmsg = " De-Authorized ";
				remarks = getRequest().getParameter("reason");
				System.out.println("remarks -- " + remarks);
				update_authdeauth_qury = cardTypeActionDao.updateDeAuthCardType(authstatus, userid, remarks, instid,
						cardtypeid);
			}
			/*
			 * int update_authdeauth =
			 * jdbctemplate.update(update_authdeauth_qury);
			 */

			// by gowtham=190819
			int update_authdeauth = jdbctemplate.update(update_authdeauth_qury);

			if (update_authdeauth >= 1) {
				txManager.commit(transact.status);
				addActionMessage("Card type " + statusmsg + " Successfully");

				// int update_authdeauth =
				// commondesc.executeTransaction(update_authdeauth_qury,
				// jdbctemplate);

				//addActionMessage("Card type " + statusmsg + " Successfully");

				/************* AUDIT BLOCK **************/
				try {
					String cardtyepdesc = cardTypeActionDao.getCardTypeDesc(instid, cardtypeid, jdbctemplate);

					// added by gowtham_220719
					trace("ip address======>  " + ip);
					auditbean.setIpAdress(ip);

					auditbean.setActmsg("Card type [ " + cardtyepdesc + " ] " + statusmsg + "");
					auditbean.setUsercode(username);
					auditbean.setAuditactcode("3030");
					auditbean.setRemarks(remarks);
					commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);
				} catch (Exception audite) {
					audite.printStackTrace();
					trace("Exception in auditran : " + audite.getMessage());
				}
				/************* AUDIT BLOCK **************/

			} else {
				txManager.rollback(transact.status);
				addActionError("could not " + statusmsg + " ");
			}

		} catch (Exception e) {
			e.printStackTrace();
			txManager.rollback(transact.status);
			addActionError(" Error while " + statusmsg + " the Card type ");
			trace("Error while deleting the Fee " + e.getMessage());
		}
		return "required_home";
	}

	public String viewCardtypeDetails() {
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		try {
			String cardtypeid = getRequest().getParameter("cardtype");
			System.out.println("cardtypeid-----  - - -" + cardtypeid);
			List getallcardtype_qury = cardTypeActionDao.getCardTypeListByCardID(instid, jdbctemplate, cardtypeid);
			System.out.println("--------getallcardtype_qury-----  - - -" + getallcardtype_qury);
			if (!getallcardtype_qury.isEmpty()) {
				ListIterator itr = getallcardtype_qury.listIterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					String makerid = (String) mp.get("MAKER_ID");
					String username = commondesc.getUserName(instid, makerid, jdbctemplate);
					mp.put("USER_NAME", username);
					itr.remove();
					itr.add(mp);
				}
				cardbean.setCardtype(getallcardtype_qury);
			} else {
				addActionError("Records not found");
				return "required_home";
			}
		} catch (Exception e) {
			addActionError("Error while fetching the cardtype " + e);
			trace("Error while fetching the cardtype " + e.getMessage());
		}
		return "viewCardTypeDetails";
	}

	public String viewCardtypeMaker() {
		HttpSession session = getRequest().getSession();

		String instid = comInstId(session);
		System.out.println("session:" + session + "|||instid::" + instid);
		try {
			List getallcardtype_qury = cardTypeActionDao.getCardTypeListByMakerCardID(instid, jdbctemplate);
			System.out.println("--------getallcardtype_qury-----  - - -" + getallcardtype_qury);
			if (!getallcardtype_qury.isEmpty()) {
				ListIterator itr = getallcardtype_qury.listIterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					String makerid = (String) mp.get("MAKER_ID");
					String username = commondesc.getUserName(instid, makerid, jdbctemplate);
					mp.put("USER_NAME", username);
					itr.remove();
					itr.add(mp);
				}
				cardbean.setCardtype(getallcardtype_qury);
			} else {
				addActionError("Records not found");
				return "required_home";
			}
		} catch (Exception e) {
			addActionError("Error while fetching the cardtype " + e);
			trace("Error while fetching the cardtype " + e.getMessage());
		}
		return "viewCardTypeDetails";
	}
} // end class
