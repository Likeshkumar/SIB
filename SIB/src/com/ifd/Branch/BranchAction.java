package com.ifd.Branch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.IfpTransObj;

import test.Date;
import test.Validation;

public class BranchAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(BranchAction.class);

	private String brcodlen;
	private List branch_list;
	private List branch_detail;
	static Boolean initmail = true;
	static String parentid = "000";

	AuditBeans auditbean = new AuditBeans();
	CommonUtil comutil = new CommonUtil();
	CommonDesc commondesc = new CommonDesc();
	BranchActionBeans branchbean = new BranchActionBeans();

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	public CommonUtil getComutil() {
		return comutil;
	}

	public BranchActionBeans getBranchbean() {
		return branchbean;
	}

	public void setBranchbean(BranchActionBeans branchbean) {
		this.branchbean = branchbean;
	}

	public void setComutil(CommonUtil comutil) {
		this.comutil = comutil;
	}

	private PlatformTransactionManager txManager = new DataSourceTransactionManager();

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	BranchActionDao branchActionDao = new BranchActionDao();

	public String comInstId(HttpSession session) {
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String comUserCode(HttpSession session) {

		String instid = (String) session.getAttribute("USERID");
		return instid;
	}

	public String comUserId(HttpSession session) {
		String userid = (String) session.getAttribute("USERID");
		return userid;
	}

	private JdbcTemplate jdbctemplate = new JdbcTemplate();

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	public String getBrcodlen() {
		return brcodlen;
	}

	public void setBrcodlen(String brcodlen) {
		this.brcodlen = brcodlen;
	}

	public List getBranch_list() {
		return branch_list;
	}

	public void setBranch_list(List branch_list) {
		this.branch_list = branch_list;
	}

	public List getBranch_detail() {
		return branch_detail;
	}

	public void setBranch_detail(List branch_detail) {
		this.branch_detail = branch_detail;
	}

	BranchActionDao branchdao = new BranchActionDao();

	public String display() {
		trace("*************** Adding Branch Begins **********");
		enctrace("*************** Adding Branch Begins **********");

		HttpSession session = getRequest().getSession();
		String branch_attached = null, brcode_length = null;
		String flag = getRequest().getParameter("act");
		if (flag != null) {
			session.setAttribute("act", flag);
		}
		
		
		/*String result = "",ret="",respmsg="NA", respcode="NA";
		JSONObject json = null;
		try {
				String url = "https://api.rokelbank.sl:9097/core/api/v1.0/account/activeCardReport";
				System.out.println("url-->"+url);
				HttpPost post = new HttpPost(url);
				HttpClient client = HttpClientBuilder.create().build();
				post.setHeader("Content-Type", "application/json");
				post.setHeader("x-api-key", "C@rdm@n2022");
				post.setHeader("x-api-secret", "C@rdm@nProd");
				post.setHeader("X-FORWARDED-FOR", "10.93.121.105");
				
				
				String request= "{ \"activeCards\":[{" +
				   		"\"branch\":\"001\"," +
				   		"\"cardNumber\":\"6398070002000000094\"," +
				   		"\"customerNumber\":\"322554\"," +
				   		"\"accountNumber\":\"002001270606110118\"," +
				   		"\"emborsedName\":\"SIB TEST CARD 3\"," +
				   		"\"cardType\":\"INDIVIDUAL\"," +
				   		"\"issueDate\":\"10-04-2020\"," +
				   		"\"orderRefNo\":\"sdededed\"," +
				   		"\"maskedCardNumber\":\"6398-07xx-xxxxxxx-0094\"," +
				   		"\"makerId\":\"SDSF3R\"," +
				   		"\"bulkRefId\":\"2344552\"," +
				   		"\"cardFlag\":\"\"," +
				   		"\"expiryDate\":\"11-NOV-2024\"}]}";
				
				System.out.println(request);
				
				StringEntity params =new StringEntity(request);
				post.setEntity(params);
				HttpResponse response = client.execute(post);
				int responseCode = response.getStatusLine().getStatusCode();
				System.out.println("Response Code : " + responseCode);
				System.out.println();
				
				BufferedReader rd = new BufferedReader(
			                new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
					result+= line;
				}
				ret = result;
			    System.out.println(ret);
			    json = new JSONObject(ret);
				System.out.println(json.getString("message")+"=="+json.getString("responseCode"));
			       
        } catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
		branchbean.formaction = "savebranchBranchAction.do";
		try {
			/*** MAIL BLOCK ****/
			trace("initmail--" + initmail + " parentid :  " + this.parentid);
			if (initmail) {
				HttpServletRequest req = getRequest();
				String menuid = comutil.getUrlMenuId(req, jdbctemplate);
				if (!menuid.equals("NOREC")) {
					trace("parentid--" + menuid);
					this.parentid = menuid;
				} else {
					this.parentid = "000";
				}
				initmail = false;
			}
			/*** MAIL BLOCK ****/

			String i_Name = (String) session.getAttribute("Instname");

			List branch_prop_result = branchActionDao.getBranchPropResult(i_Name, jdbctemplate);
			trace("branch_prop_result" + branch_prop_result.size());
			Iterator iterator = branch_prop_result.iterator();
			while (iterator.hasNext()) {
				Map map = (Map) iterator.next();
				branch_attached = ((String) map.get("BRANCHATTCHED"));
				brcode_length = ((String) map.get("BRCODELEN"));
				System.out.println(" brcode_length " + brcode_length);
			}
			setBrcodlen(brcode_length);
			if (branch_attached.contentEquals("N")) {
				session.setAttribute("addBranchErrorStatus", "S");
				return "AddBranchError";
			}

		} catch (Exception e) {
			session.setAttribute("addBranchMessage", "Unable to Continue The Process");
			trace("Error While Fethchin Branch Detail : ERROR " + e.getMessage());
			session.setAttribute("addBranchErrorStatus", "E");
			return "AddBranchError";
		}
		trace("\n\n");
		enctrace("\n\n");
		return "displayBranch";
	}

	public String savebranch() {
		trace("*************** Saving Branch Begins **********");
		enctrace("*************** Saving Branch Begins **********");
		HttpSession session = getRequest().getSession();
		String instid = this.comInstId(session);
		String usercode = comUserCode(session);

		// ADDED BY GOWTHAM_200719
		String ip = (String) session.getAttribute("REMOTE_IP");// ------------------
		Date date = new Date();

		String userid = comUserCode(session);

		String br_code = (getRequest().getParameter("BRANCH_CODE"));
		String br_name = (getRequest().getParameter("BRANCH_NAME"));
		String br_addr1 = (getRequest().getParameter("BR_ADDR1"));
		String br_addr2 = (getRequest().getParameter("BR_ADDR2"));
		String br_addr3 = (getRequest().getParameter("BR_ADDR3"));
		String br_city = (getRequest().getParameter("BR_CITY"));
		String br_state = (getRequest().getParameter("BR_STATE"));
		String br_phone = (getRequest().getParameter("BR_PHONE"));

		branchbean.setBranchcode(br_code);
		branchbean.setBranchname(br_name);
		branchbean.setBr_addr1(br_addr1);
		branchbean.setBr_addr2(br_addr2);
		branchbean.setBr_addr3(br_addr3);
		branchbean.setBr_city(br_city);
		branchbean.setBr_state(br_state);
		branchbean.setBr_phone(br_phone);

		int br_codeval = Validation.number(br_code);
		if (br_codeval == 0) {
			System.out.println("validation part branch code");
			addFieldError("BRANCH_CODE", " Please enter Branch code");

			return display();
		}
		boolean br_name_val = Validation.Spccharcter(br_name);
		if (!br_name_val) {
			System.out.println("validation part branch name ");
			addFieldError("BRANCH_NAME", " Please enter Branch Name ");
			return display();
		}

		boolean br_addr1_val = Validation.SpcNumberCharcter(br_addr1);
		if (!br_addr1_val) {
			addFieldError("BR_ADDR1", " Please enter Branch address with characters and numbers");
			return display();
		}

		boolean br_addr2_val = Validation.CheckSpcNumberCharcter(br_addr2);
		if (!br_addr2_val) {
			addActionError(" Please enter address2 with characters or numbers");
			return display();
		}

		boolean br_addr3_val = Validation.CheckSpcNumberCharcter(br_addr3);
		if (!br_addr3_val) {
			addActionError(" Please enter address3 with characters and numbers");
			return display();
		}

		boolean br_city_val = Validation.CheckSpccharcter(br_city);
		if (!br_city_val) {
			System.out.println("validation part branch city ");
			addActionError(" Please enter Branch CITY ");
			return display();
		}

		boolean br_state_val = Validation.CheckSpccharcter(br_state);
		if (!br_state_val) {
			System.out.println("validation part branch state ");
			addActionError(" Please enter Branch STATE ");

			return display();
		}

		boolean br_phone_val = Validation.CheckNumber(br_phone);
		if (!br_phone_val) {
			addActionError(" Please enter numbers for phone number");
			return display();
		}

		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		System.out.println("Testing user name " + username);

		IfpTransObj transact = commondesc.myTranObject("BRANCH", txManager);
		String respmsg = " Added ";
		String[] prodlist = getRequest().getParameterValues("productlist");
		String[] productmast = getRequest().getParameterValues("productmast");
		try {
			// checking branch code already exists or not
			String flag = getRequest().getParameter("act");
			System.out.println("flag ---- " + flag);
			String auth_code = "1";
			String mkchkrstatus = "D";
			String authmsg = "";
			if (flag.equals("D")) {
				auth_code = "1";
				mkchkrstatus = "D";
			} else {
				auth_code = "0";
				mkchkrstatus = "M";
				authmsg = "  Waiting for Authorization ....";
			}

			int  branchqryResult = branchActionDao.delBranchMaster(instid, br_code,jdbctemplate);

			/* int x = jdbctemplate.update(branchqry); */

		
			trace("Deleted existing branch...got : " + branchqryResult);
			if (branchqryResult > 0) {
				respmsg = " Updated ";
			}

			int checkbnchcode_exist = branchActionDao.getCheckbnchcodeExist(instid, br_code, jdbctemplate);

			int checkbnchname_exist = branchActionDao.getCheckbnchcodeExist(instid, br_name, jdbctemplate);
			if (checkbnchcode_exist > 0) {
				System.out.println("BRANCH CODE AlREADY EXIST");
				addActionError("BRANCH CODE AlREADY EXIST");
				trace("BRANCH CODE AlREADY EXIST");
			} else if (checkbnchname_exist > 0) // checking branch name already
												// exists or not
			{
				System.out.println("BRANCH NAME AlREADY EXIST");
				addActionError("BRANCH NAME AlREADY EXIST");
				trace("BRANCH NAME AlREADY EXIST");
				return display();
			} else {
				String branch_insert_query = branchActionDao.getBranchInsertQuery(instid, br_code, br_name, br_addr1,
						br_addr2, br_addr3, br_city, br_state, br_phone, usercode, auth_code, mkchkrstatus);
				enctrace("branch insert" + branch_insert_query);
				jdbctemplate.update(branch_insert_query);

				//// Inserting Card Base No

				if (flag.equals("D")) {

					List binlist = branchActionDao.gettingBinList(instid, jdbctemplate);
					trace("bin list size branch " + binlist);
					if (!binlist.isEmpty()) {
						ListIterator lstitr = binlist.listIterator();
						int insertswitchstatus[] = new int[binlist.size()];
						String ctbaseinsert[] = new String[binlist.size()];
						int i = 0;
						while (lstitr.hasNext()) {
							Map mp = (Map) lstitr.next();
							ctbaseinsert[i] = "INSERT INTO BASENO(INST_ID,BASENO_CODE,CHN_BASE_NO,BASE_NO_LEN,BIN) VALUES('"
									+ instid + "','" + br_code + "','1','" + mp.get("BASELEN") + "','" + mp.get("BIN")
									+ "')";
							enctrace("insert baseno query branch" + ctbaseinsert[i]);
							i++;
						}
						insertswitchstatus = jdbctemplate.batchUpdate(ctbaseinsert);
						trace("batch update got branch..." + insertswitchstatus.length);
					} else {
						trace("NO BINS ARE THERE BASED ON BRANCH...FOR CARD GENERATED SEQUANCE");
					}

				}

				txManager.commit(transact.status);
				getRequest().getSession().setAttribute("preverr", "S");
				getRequest().getSession().setAttribute("prevmsg",
						"Branch '" + br_name + "' " + respmsg + " Sucessfully" + authmsg);
				trace("Branch '" + br_name + "' Added Sucessfully");

				// ------------------changes made 10/12/2015----------------//

				try {
					// ADDED BY GOWTHAM_200719
					trace("ip address======>  " + ip);// -----------------
					auditbean.setIpAdress(ip);// ----------------------
					auditbean.setActmsg("Branch [ " + br_name + " - " + br_code + " ] Added. for institution " + instid
							+ " " + authmsg);
					auditbean.setUsercode(username);
					auditbean.setAuditactcode("51");
					commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);

				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}

				// ------------------changes made 10/12/2015----------------//

				/*** MAIL BLOCK ****/

				/*
				 * IfpTransObj transactmail = comInsntance().myTranObject(); try
				 * { String alertid = this.parentid; if( alertid != null && !
				 * alertid.equals("000")){ String keymsg = "The Branch " +
				 * br_name; int mail = comutil.sendMail( instid, alertid,
				 * keymsg, jdbcTemplate, session, getMailSender() ); trace(
				 * "mail return__" + mail); } } catch (Exception e) {
				 * e.printStackTrace(); } finally{
				 * transactmail.txManager.commit(transactmail.status); trace(
				 * "mail commit successfully"); }
				 */
				/*** MAIL BLOCK ****/
				return "required_home";
			}
		} catch (Exception e) {
			txManager.rollback(transact.status);
			getRequest().getSession().setAttribute("preverr", "E");
			getRequest().getSession().setAttribute("prevmsg", "Error While Adding Branch '" + br_name + "' ");
			trace("Error While Adding Branch '" + br_name + "' " + e.getMessage());
			return display();
		}
		trace("\n\n");
		enctrace("\n\n");
		return "required_home";
	}

	public String viewallBranches() {
		System.out.println("viewallBranches");
		HttpSession session = getRequest().getSession();

		String branch_attached = null, brcode_length = null;
		branchbean.formaction = "viewbranchDetailBranchAction.do";
		try {
			String i_Name = (String) session.getAttribute("Instname");
			String flag = getRequest().getParameter("act");
			trace("got act is ..........: " + flag);
			if (flag != null) {
				session.setAttribute("act", flag);
			}
			List branch_prop_result = branchActionDao.getBranchPropResult(i_Name, jdbctemplate);
			Iterator iterator = branch_prop_result.iterator();
			while (iterator.hasNext()) {
				Map map = (Map) iterator.next();
				branch_attached = ((String) map.get("BRANCHATTCHED"));
			}
			if (branch_attached.contentEquals("N")) {
				session.setAttribute("addBranchErrorStatus", "S");
				return "AddBranchError";
			} else {
				System.out.println("viewallBranches     ");
				List br_list_result;
				if (flag.equals("D")) {
					br_list_result = branchActionDao.getBrlistQuery(i_Name, jdbctemplate);
				} else if (flag.equals("M")) {
					br_list_result = branchActionDao.getBrlistQueryForMaker(i_Name, jdbctemplate);
				} else {
					br_list_result = branchActionDao.getBrlistQueryForChecker(i_Name, jdbctemplate);
				}
				System.out.println("br_list_result--> " + br_list_result);
				if (!(br_list_result.isEmpty())) {
					setBranch_list(br_list_result);
				} else {
					addActionError(" No Records found ");
					return "required_home";
				}
				return "viewallBranches";
			}
		} catch (Exception e) {
			addActionError("Error While Fethchin Branch Detail : ERROR " + e.getMessage());

			return "AddBranchError";
		}
	}

	private char flag;

	public char getFlag() {
		return flag;
	}

	public void setFlag(char flag) {
		this.flag = flag;
	}

	public String viewbranchDetail() {
		String inst_id = (String) getRequest().getSession().getAttribute("Instname");
		String br_code = (getRequest().getParameter("branchcode"));

		if (br_code.equals("-1")) {
			System.out.println("validation part branchcode");
			addActionError(" Please select branch name ");
			// return viewallBranches();
		}

		System.out.println("INST_ID   " + inst_id + "BRANCH_CODE   " + br_code);
		HttpSession session = getRequest().getSession();

		try {
			String act = (String) session.getAttribute("act");
			char a = act.charAt(0);
			setFlag(a);
			System.out.println("flag ---- a" + a);

			Boolean hasbranchprod = commondesc.hasBranchBasedProductEnabled(inst_id, jdbctemplate);
			trace("hasbranchprod :" + hasbranchprod);

			List br_detail_result = branchActionDao.getBrDetailResult(inst_id, br_code, jdbctemplate);
			if (!(br_detail_result.isEmpty())) {
				ListIterator itr = br_detail_result.listIterator();
				String makername;
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					String mkrid = (String) mp.get("MAKER_ID");
					System.out.println("mkrid --- " + mkrid);
					if (mkrid != null) {
						makername = commondesc.getUserName(inst_id, mkrid, jdbctemplate);
					} else {
						makername = "--";
					}
					mp.put("USER_NAME", makername);
					itr.remove();
					itr.add(mp);
					System.out.println("result viewbranchDetail      " + mp);
				}
				System.out.println("result viewbranchDetail      " + br_detail_result);
				setBranch_detail(br_detail_result);
			} else {
				session.setAttribute("preverr", "E");
				addActionError(" No Records found ");
				return "required_home";
			}
		} catch (Exception e) {
			addActionError("Error While Getting Branch Details ");
			trace("exception:::" + e.getMessage());
			return viewallBranches();
		}
		return "viewBranchDetails";
	}

	public String editbranchDetail() {
		String inst_id = (String) getRequest().getSession().getAttribute("Instname");
		String br_code = (getRequest().getParameter("branchcode"));
		System.out.println("INST_ID   " + inst_id + "BRANCH_CODE   " + br_code);
		try {

			List br_detail_result = branchActionDao.getBrDetailResult(inst_id, br_code, jdbctemplate);
			System.out.println("result viewbranchDetail      " + br_detail_result);
			setBranch_detail(br_detail_result);

			return "dispEditBranch";
		} catch (Exception e) {
			getRequest().getSession().setAttribute("preverr", "E");
			getRequest().getSession().setAttribute("prevmsg", "Error While Getting Branch Details " + e.getMessage());
			return viewallBranches();
		}
	}

	public String saveEditbranch() {
		String inst_id = (String) getRequest().getSession().getAttribute("Instname");
		String br_code = (getRequest().getParameter("BRANCH_CODE"));
		String br_name = (getRequest().getParameter("BRANCH_NAME"));
		String br_addr1 = (getRequest().getParameter("BR_ADDR1"));
		String br_addr2 = (getRequest().getParameter("BR_ADDR2"));
		String br_addr3 = (getRequest().getParameter("BR_ADDR3"));
		String br_city = (getRequest().getParameter("BR_CITY"));
		String br_state = (getRequest().getParameter("BR_STATE"));
		String br_phone = (getRequest().getParameter("BR_PHONE"));
		IfpTransObj transact = commondesc.myTranObject("SAVEBRANCH", txManager);

		int br_codeval = Validation.number(br_code);
		if (br_codeval == 0) {
			System.out.println("validation part branch code");
			addFieldError("BRANCH_CODE", " Please enter Branch code");

			return editBranchDetails();
		}
		boolean br_name_val = Validation.Spccharcter(br_name);
		if (!br_name_val) {
			System.out.println("validation part branch name ");
			addFieldError("BRANCH_NAME", " Please enter Branch Name ");
			return editBranchDetails();
		}

		boolean br_addr1_val = Validation.SpcNumberCharcter(br_addr1);
		if (!br_addr1_val) {
			addFieldError("BR_ADDR1", " Please enter Branch address with characters or numbers");
			return editBranchDetails();
		}

		boolean br_addr2_val = Validation.SpcNumberCharcter(br_addr2);
		if (!br_addr2_val) {
			addFieldError("BR_ADDR2", " Please enter address2 with characters or numbers");
			return editBranchDetails();
		}
		boolean br_addr3_val = Validation.SpcNumberCharcter(br_addr3);
		if (!br_addr3_val) {
			addFieldError("BR_ADDR3", " Please enter address3 with characters and numbers");
			return editBranchDetails();
		}

		boolean br_city_val = Validation.CheckSpccharcter(br_city);
		if (!br_city_val) {
			System.out.println("validation part branch city ");
			addActionError(" Please enter Branch CITY ");
			return display();
		}

		boolean br_state_val = Validation.CheckSpccharcter(br_state);
		if (!br_state_val) {
			System.out.println("validation part branch state ");
			addActionError(" Please enter Branch STATE ");
			return display();
		}

		boolean br_phone_val = Validation.Number(br_phone);
		if (!br_phone_val) {
			addFieldError("BR_PHONE", " Please enter numbers for phone number");
			return editBranchDetails();
		}

		// BRANCH_CODE=chck_brcodelength(BRANCH_CODE);
		// String update_branch_query = "update BRANCH_MASTER set
		// BRANCH_NAME='"+br_name.toUpperCase()+"',BR_ADDR1='"+br_addr1.toUpperCase()+"',BR_ADDR2='"+br_addr2.toUpperCase()+"',BR_ADDR3='"+br_addr3.toUpperCase()+"',BR_CITY='"+br_city.toUpperCase()
		// +"',BR_PHONE='"+br_phone.toUpperCase()+"',BR_STATE='"+br_state.toUpperCase()
		// +"' where INST_ID='"+inst_id+"' and BRANCH_CODE='"+br_code+"'";
		String update_branch_query = branchActionDao.updateBranchMaster(inst_id, br_code, br_name, br_addr1, br_addr2,
				br_addr3, br_city, br_state, br_phone);
		System.out.println(update_branch_query);

		try {

			/* int rse = jdbctemplate.update(update_branch_query); */
			// by gowtham300819
			int rse = jdbctemplate.update(update_branch_query,
					new Object[] { br_name.toUpperCase(), br_addr1.toUpperCase(), br_addr2.toUpperCase(),
							br_addr3.toUpperCase(), br_city.toUpperCase(), br_phone.toUpperCase(),
							br_state.toUpperCase(), inst_id, br_code });

			if (rse == 1) {
				txManager.commit(transact.status);
				getRequest().getSession().setAttribute("preverr", "S");
				getRequest().getSession().setAttribute("prevmsg", "Branch Edited Sucessfully");
				return viewallBranches();
			} else {
				txManager.rollback(transact.status);
				getRequest().getSession().setAttribute("preverr", "E");
				getRequest().getSession().setAttribute("prevmsg", "Branch Cannot Be Edited");
				return viewallBranches();
			}
		} catch (Exception e) {
			txManager.rollback(transact.status);
			getRequest().getSession().setAttribute("preverr", "E");
			getRequest().getSession().setAttribute("prevmsg", "Error While Editing Branch " + e.getMessage());
			return viewallBranches();
		}
	}

	public String deletebranchDetail() {
		String inst_id = (String) getRequest().getSession().getAttribute("Instname");
		String br_code = (getRequest().getParameter("branchcode"));
		System.out.println("INST_ID   " + inst_id + "BRANCH_CODE   " + br_code);

		IfpTransObj transact = commondesc.myTranObject("DELBRANCH", txManager);
		try {
			// String delete_query="delete from BRANCH_MASTER where INST_ID
			// ='"+inst_id+"' and BRANCH_CODE='"+br_code+"'" ;
			int  delete_query_result = branchdao.delBranchMaster(inst_id, br_code,jdbctemplate);
			//int rse = jdbctemplate.update(delete_query);
			if (delete_query_result == 1) {
				txManager.commit(transact.status);
				getRequest().getSession().setAttribute("preverr", "S");
				getRequest().getSession().setAttribute("prevmsg", "Branch Deleted Sucessfully");
				return viewallBranches();
			} else {
				txManager.rollback(transact.status);
				getRequest().getSession().setAttribute("preverr", "E");
				getRequest().getSession().setAttribute("prevmsg", "Branch Cannot Be Deleted");
				return viewallBranches();
			}

		}

		catch (Exception e) {
			txManager.rollback(transact.status);
			getRequest().getSession().setAttribute("preverr", "E");
			getRequest().getSession().setAttribute("prevmsg", "Error While Deleting Branch " + e.getMessage());
			return viewallBranches();
		}
	}

	/*
	 * public String editallBranches() { HttpSession session =
	 * getRequest().getSession(); JdbcTemplate jdbcTemplate = new
	 * JdbcTemplate(dataSource); try { String i_Name =
	 * (String)session.getAttribute("Instname"); INST_ID = i_Name.toUpperCase();
	 * setInst_id(INST_ID); System.out.println("INST_ID   "+INST_ID); query=
	 * "select BRANCHATTCHED,BRCODELEN from INSTITUTION where INST_ID='"
	 * +INST_ID+"'"; result=jdbcTemplate.queryForList(query ); Iterator
	 * iterator=result.iterator(); while(iterator.hasNext()) { Map map =
	 * (Map)iterator.next(); BRANCHATTCHED=((String)map.get("BRANCHATTCHED"));
	 * BRCODELEN=((String)map.get("BRCODELEN")); } setBrcodlen(BRCODELEN);
	 * System.out.println("result   "+result); System.out.println(
	 * "BRANCHATTCHED   "+BRANCHATTCHED); System.out.println("BRCODELEN   "
	 * +BRCODELEN); if(BRANCHATTCHED.contentEquals("N")) {
	 * session.setAttribute("addBranchErrorStatus", "S"); return
	 * "AddBranchError"; } else { System.out.println("viewallBranches     ");
	 * String i_Name1 = (String)session.getAttribute("Instname");
	 * System.out.println("i_Name     "+i_Name1); INST_ID =
	 * i_Name1.toUpperCase(); query=
	 * "select * from BRANCH_MASTER where INST_ID ='"+INST_ID+"'";
	 * result=jdbcTemplate.queryForList(query); System.out.println("result     "
	 * +result); setBranch_details(result); return "editAllBranches"; String
	 * usercode = comUserCode(session); } } catch (Exception e) {
	 * session.setAttribute("addBranchMessage",
	 * "Error While Fethchin Institution While Editing Branch Detail : ERROR " +
	 * e.getMessage()); session.setAttribute("addBranchErrorStatus", "E");
	 * return "AddBranchError"; }
	 * 
	 * }
	 * 
	 * private String branchattached; public String getBranchattached() { return
	 * branchattached; } public void setBranchattached(String branchattached) {
	 * this.branchattached = branchattached; }
	 * 
	 * public String chck_brcodelength(String brcode) { if(brcode.length()==4) {
	 * System.out.println("Branch code length is 4"); return brcode; }
	 * 
	 * else if(brcode.length()==3) { System.out.println(
	 * "Branch code length is 3"); brcode="0"+brcode; return brcode; } return
	 * brcode; }
	 */

	public String authdeauthbranch() {
		trace("*********Branch authorize Action");
		enctrace("*********Branch authorize Action");
		HttpSession session = getRequest().getSession();

		IfpTransObj transact = commondesc.myTranObject("AUTHBRANCH", txManager);
		String br_name = getRequest().getParameter("BRANCH_NAME");
		String branchid = getRequest().getParameter("branchid");
		String auth = getRequest().getParameter("auth");
		System.out.println(" auth ##### " + auth);
		String instid = comInstId(session);
		String usercode = comUserCode(session);
		String userid = comUserId(session);

		// added by gowtham_220719
		String ip = (String) session.getAttribute("REMOTE_IP");

		String username = commondesc.getUserName(instid, userid, jdbctemplate);
		System.out.println("Testing user name " + username);

		try {

			try {
				int updstatus;
				String authstatus = branchdao.getAuthBranch(instid, branchid, jdbctemplate);
				String mkrstatus;
				String errstatus;

				if (auth.equals("Reject")) {
					mkrstatus = "D";
					errstatus = "De-authorized";
					String remarks = getRequest().getParameter("reason");
					updstatus = branchdao.updateDeAuthBranchStatus(instid, mkrstatus, branchid, usercode, remarks,
							jdbctemplate);
					trace("Updating Deauthozation status...." + updstatus);
				} else {
					errstatus = "authorized";
					mkrstatus = "P";
					updstatus = branchdao.updateAuthBranchStatus(instid, mkrstatus, branchid, usercode, jdbctemplate);
					trace("Updating authozation status...." + updstatus);

					int barchbase = branchdao.branchbaseexit(instid, branchid, jdbctemplate);

					trace("Got regreq: " + barchbase);
					if (barchbase > 0) {
						trace("Alredy BaseNO is existfor the same branch...");
					} else {

						//// Inserting Card Base No

						List binlist = branchActionDao.gettingBinList(instid, jdbctemplate);
						trace("bin list size branch " + binlist);
						if (!binlist.isEmpty()) {
							ListIterator lstitr = binlist.listIterator();
							int insertswitchstatus[] = new int[binlist.size()];
							String ctbaseinsert[] = new String[binlist.size()];
							int i = 0;
							while (lstitr.hasNext()) {
								Map mp = (Map) lstitr.next();
								ctbaseinsert[i] = "INSERT INTO BASENO(INST_ID,BASENO_CODE,CHN_BASE_NO,BASE_NO_LEN,BIN) VALUES('"
										+ instid + "','" + branchid + "','1','" + mp.get("BASELEN") + "','"
										+ mp.get("BIN") + "')";
								enctrace("insert baseno query branch" + ctbaseinsert[i]);
								i++;
							}
							insertswitchstatus = jdbctemplate.batchUpdate(ctbaseinsert);
							trace("batch update got branch..." + insertswitchstatus.length);
						} else {
							trace("NO BINS ARE THERE BASED ON BRANCH...FOR CARD GENERATED SEQUANCE");
						}

					}
				}
				if (updstatus <= 0) {
					txManager.rollback(transact.status);
					addActionError("Could not authorize....");

				}
				txManager.commit(transact.status);
				addActionMessage("Branch " + errstatus + " successfully.");

				// ------------inserted audit COding 10/12/2015---------------//
				try {
					auditbean.setActmsg("Branch [ " + br_name + "-" + branchid + " ] is for institution id[" + instid
							+ "] " + errstatus);

					// added by gowtham_220719
					trace("ip address======>  " + ip);
					auditbean.setIpAdress(ip);

					auditbean.setUsercode(username);
					auditbean.setAuditactcode("51");
					commondesc.insertAuditTrail(instid, username, auditbean, jdbctemplate, txManager);

				} catch (Exception audite) {
					trace("Exception in auditran : " + audite.getMessage());
				}

				/*
				 * if( authstatus.equals("1")){ session.setAttribute("preverr",
				 * "E"); addActionError(, "Branch already authorized..."); }
				 */

			} catch (EmptyResultDataAccessException e) {
				trace("Exception: " + e.getMessage());
				addActionError("Exception : Could not authorize");
			}

		} catch (Exception e) {
			trace("Exception : " + e.getMessage());
			addActionError("Exception : Could not authorize");
		}
		return "required_home";
	}

	public String editBranchHome() {
		trace("*****editBranchHome");
		enctrace("*****editBranchHome");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		branchbean.formaction = "editBranchDetailsBranchAction.do";
		try {
			List branhlist = branchActionDao.getBranchListForEdit(instid, jdbctemplate);
			if (branhlist.isEmpty()) {
				addActionError("No Branch Found");
				return "required_home";
			}

			setBranch_list(branhlist);

		} catch (Exception e) {
			trace("Exception: " + e.getMessage());
			addActionError("Unable to continue the process");
		}
		return "viewallBranches_edithome";
	}

	public String editBranchDetails() {
		trace("*****editBranchDetails");
		enctrace("*****editBranchDetails");
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		branchbean.formaction = "savebranchBranchAction.do";
		branchbean.readonly = true;
		branchbean.branchcode = getRequest().getParameter("branchcode");
		try {
			List branchdetails = branchActionDao.getBrDetailResult(instid, branchbean.branchcode, jdbctemplate);
			trace("Getting branch details...got :   " + branchdetails);

			if (branchdetails.isEmpty()) {
				addActionError("Unable to continue the process");
			}

			Iterator itr = branchdetails.iterator();
			while (itr.hasNext()) {
				Map mp = (Map) itr.next();
				branchbean.branchname = (String) mp.get("BRANCH_NAME");
				branchbean.br_addr1 = (String) mp.get("BR_ADDR1");
				branchbean.br_addr2 = (String) mp.get("BR_ADDR1");
				branchbean.br_addr3 = (String) mp.get("BR_ADDR1");
				branchbean.br_city = (String) mp.get("BR_CITY");
				branchbean.br_state = (String) mp.get("BR_STATE");
				branchbean.br_phone = (String) mp.get("BR_PHONE");
			}

			List branch_prop_result = branchActionDao.getBranchPropResult(instid, jdbctemplate);
			trace("branch_prop_result" + branch_prop_result.size());
			Iterator iterator = branch_prop_result.iterator();
			while (iterator.hasNext()) {
				Map map = (Map) iterator.next();
				setBrcodlen((String) map.get("BRCODELEN"));
			}

		} catch (Exception e) {
			trace("Exception: " + e.getMessage());
			addActionError("Unable to continue the process");
		}
		return "displayBranch_edit";
	}

}
