package com.ifd.Report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.Action.ReportgenerationAction;
import com.ifp.beans.ReportActionBeans;
import com.ifp.dao.ReportActionDAO;
import com.ifp.util.CommonDesc;
import com.ifp.util.PDFReportGenerator;
import com.itextpdf.text.Document;

public class CardMaintainReport extends BaseAction {

	CardMaintainReportDAO reportdao = new CardMaintainReportDAO();
	ReportActionBeans reportbean = new ReportActionBeans();
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	CommonDesc commondesc = new CommonDesc();
	String auditreportname;
	private ByteArrayInputStream input_stream;
	private ByteArrayOutputStream output_stream;
	MasterReportGenerationDAO dao = new MasterReportGenerationDAO();
	MasterReportGenerationBean cardbean = new MasterReportGenerationBean();

	public ByteArrayInputStream getInput_stream() {
		return input_stream;
	}

	public List BranchList;

	public List getBranchList() {
		return BranchList;
	}

	public List ProductList;

	public List getProductList() {
		return ProductList;
	}

	private String transctionreport;

	public String getTransctionreport() {
		return transctionreport;
	}

	public void setTransctionreport(String transctionreport) {
		this.transctionreport = transctionreport;
	}

	public void setProductList(List productList) {
		ProductList = productList;
	}

	public void setBranchList(List branchList) {
		BranchList = branchList;
	}

	public void setInput_stream(ByteArrayInputStream input_stream) {
		this.input_stream = input_stream;
	}

	public ByteArrayOutputStream getOutput_stream() {
		return output_stream;
	}

	public void setOutput_stream(ByteArrayOutputStream output_stream) {
		this.output_stream = output_stream;
	}

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	public ReportActionBeans getReportbean() {
		return reportbean;
	}

	public void setReportbean(ReportActionBeans reportbean) {
		this.reportbean = reportbean;
	}

	public CardMaintainReportDAO getReportdao() {
		return reportdao;
	}

	public void setReportdao(CardMaintainReportDAO reportdao) {
		this.reportdao = reportdao;
	}

	public String getAuditreportname() {
		return auditreportname;
	}

	public void setAuditreportname(String auditreportname) {
		this.auditreportname = auditreportname;
	}

	/*
	 * public String comInstId() { HttpSession session =
	 * getRequest().getSession(); String instid = (String)
	 * session.getAttribute("Instname"); return instid; }
	 * 
	 * public String comUserCode() { HttpSession session =
	 * getRequest().getSession(); String instid = (String)
	 * session.getAttribute("USERID"); return instid; }
	 * 
	 * public String comUsername() { HttpSession session =
	 * getRequest().getSession(); String username = (String)
	 * session.getAttribute("USERNAME"); return username; }
	 */

	public String comInstId(HttpSession session) {
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String comUserId(HttpSession session) {
		String userid = (String) session.getAttribute("USERID");
		return userid;
	}

	public String cardmaintainhome() throws Exception {

		trace("============cardmaintainhome=   ");
		reportbean.actionheadlist = reportdao.getAuditActionHeadList(jdbctemplate);

		if (reportbean.actionheadlist.isEmpty()) {
			addActionError("Audit Action Code Not Configured.");
			return "required_home";
		}

		trace("-----Audit Action Code   -------  " + reportbean.actionheadlist);
		HttpSession session = getRequest().getSession();

		trace("------------ after============= ");
		List cardreportlist = dao.getActionList(jdbctemplate);
		cardbean.setCardreportlist(cardreportlist);
		String instid = (String) session.getAttribute("Instname");

		List branchList = commondesc.generateBranchList(instid, jdbctemplate);
		setBranchList(branchList);

		List productList = commondesc.getProductListView(instid, jdbctemplate, session);
		setProductList(productList);
		return "cardmaintainhome";
	}

	public void getsubActionList() throws IOException {
		String headcode = getRequest().getParameter("actionhead");
		List subactlist = reportdao.getAuditActionList(headcode, jdbctemplate);
		if (subactlist.isEmpty()) {
			return;
		}
		Iterator itr = subactlist.iterator();
		String opt = "";
		while (itr.hasNext()) {
			Map mp = (Map) itr.next();
			opt += "<option value='" + (String) mp.get("AUDIT_ACTIONCODE") + "'> " + (String) mp.get("AUDIT_ACTIONDESC")
					+ " </option>";
		}
		getResponse().getWriter().write(opt.toString());
	}

	public String generateAuditReport() {
		output_stream = new ByteArrayOutputStream();
		Document document = new Document();

		String propertyname = "auditreport";
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		String loginuser = comUserId(session);
		String cardprocess = getRequest().getParameter("cardprocess");
		String cardno = getRequest().getParameter("cardno");
		String branchlist = getRequest().getParameter("branchlist");
		String prodcutcode = getRequest().getParameter("productCode");
		String usercode = getRequest().getParameter("usercode");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		String getreport = getRequest().getParameter("getreport");
		// String[] actionhead= getRequest().getParameterValues("actionhead");
		String[] subactionhead = getRequest().getParameterValues("actionhead");
		String orderby = getRequest().getParameter("orderby");
		String ascdesc = getRequest().getParameter("ascdesc");
		String querycondition = "", summarycond = "";
		String orderbycondition = "";
		String reportheader = " From Date , : , " + fromdate + ",";
		reportheader += " To Date , : , " + todate + ",";
		String subconcode="";

		int ALIGN_LEFT = 0, ALIGN_CENTER = 1, ALIGN_RIGHT = 2;
		String reportname = "Audit_";
		try {
			String subcond = "";
			// System.out.println("cardno :" + cardno );
			System.out.println("usercode :" + loginuser);
			trace("subactionhead :" + subactionhead + "branchlist" + branchlist);
			DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
			Date date = new Date();
			String condi2 = "";
			if (branchlist != null) {

				reportheader = " From Date , : , " + fromdate + ",";
				reportheader += " To Date , : , " + todate + ",";
				String conditionpro = "";

				if (branchlist.equals("ALL")) {

					querycondition = "";
					reportheader += "Generated for ,:,ALL BRANCHES,";
				} else {
					String branchname = commondesc.genBranchDesc(branchlist, instid, jdbctemplate);

					String branchnamewithcode = branchlist + "-" + branchname;
					reportheader += "Generated for ,:," + branchnamewithcode + ", ";
					reportname += branchlist + "_";
					if (subcond.equals("11111")) {
						condi2 = " AND  trim(A.CARD_COLLECT_BRANCH) = '" + branchlist + "'";
					} else {
						querycondition = " AND  BRANCHCODE = '" + branchlist + "'";
					}

				}
			}

			if (prodcutcode != null) {
				if (prodcutcode.equals("ALL")) {

					querycondition = "";
					reportheader += "Generated for ,:,ALL PRODUCTS,";
					summarycond = "";

				} else {

					reportheader += "Generated for ,:, Product," + prodcutcode + ", ";
					querycondition += " AND  PRODUCTCODE = '" + prodcutcode + "'";
					summarycond = " AND  PRODUCT_CODE = '" + prodcutcode + "'";

				}
			}
			if (cardno != null) {
				reportheader = "Card Number ,:," + cardno + ", ";
				reportname += cardno + "_";
				//querycondition = " AND  CARDNO = '" + cardno + "'";
				String mcard_no= cardno.substring(0,4)+"-"+cardno.substring(4,6)+"xx-xxxxxxx-"+cardno.substring(15,19);
				trace("m-card number-->"+mcard_no);
				querycondition = " AND  CARDNO = '" + mcard_no + "'";
			}

			if (usercode != null) {
				String username = commondesc.getUserNameFromTemp(instid, usercode, jdbctemplate);
				if (username == null) {
					username = usercode;
				}

				reportheader += "User Name ,:," + username + ", ";
				String userid = commondesc.getUserIdFromTemp(instid, usercode, jdbctemplate);
				if (userid == null) {
					userid = usercode;
				}
				querycondition += " AND (  USERCODE = '" + username + "' OR USERCODE ='" + userid + "' ) ";

				reportname += username + "_";
			}

			if (subactionhead != null) {
				String username = commondesc.getUserNameFromTemp(instid, loginuser, jdbctemplate);

				reportheader += "Generated By ,:," + username + " , ";

				for (int i = 0; i < subactionhead.length; i++) {
					subcond += subactionhead[i] + ",";
					trace("findtype" + subcond);
				}

				
				if (subcond.endsWith(",")) {
					subcond = subcond.substring(0, subcond.length() - 1);
				}
                 
				if(subcond.equals("9091")){
					subconcode="'9091','9092','9093'";
					querycondition += " AND (  AUDITACTCODE IN ("+subconcode+ ") ) ";
					}
				else{
				querycondition += " AND (  AUDITACTCODE IN (" + subcond + ") ) ";}
				
				trace("querycondition::::::::   "+querycondition);
				trace("subcond::::::::   "+subcond);

				if (subcond.equals("4109")) {
					reportname = "InstantcardMapWithNewAccount" + "_" + dateFormat.format(date);
					;
				} else if (subcond.equals("4110")) {
					reportname = "Instant_Summary" + "_" + dateFormat.format(date);
					;
				} else if (subcond.equals("9091")) {
					reportname = "Account_Linkage" + "_" + dateFormat.format(date);
					;
				} else if (subcond.equals("4104")) {
					reportname = "Reissued_card" + "_" + dateFormat.format(date);
					;
				} else if (subcond.equals("4111")) {
					reportname = "TotalCards_Status" + "_" + dateFormat.format(date);
				} else if (subcond.equals("11111")) {
					reportname = "TotalIssuedCardsInProduction" + "_" + dateFormat.format(date);
				} else {
					reportname += "ActionBased_";
				}

			}

			if (orderby != null) {
				if (subcond.equals("11111")) {
					propertyname = "TotalIssuedCardsInProduction";
					// querycondition += " AND ( AUDITACTCODE IN ("+subcond+") )
					// ";
					querycondition += " AND TRUNC(A.ISSUE_DATE) BETWEEN TO_DATE('" + fromdate
							+ "','dd-mm-yyyy') AND TO_DATE('" + todate + "','dd-mm-yyyy') ";

					if (orderby.equals("$ACTDATE")) {
						orderbycondition = " ORDER BY A.ISSUE_DATE " + ascdesc;
					} else if (orderby.equals("$ACTIONWISE")) {
						orderbycondition += " ORDER BY A.ISSUE_DATE " + ascdesc;

					}
					System.out.println("ssssssssssss" + orderbycondition);
				} else {
					// querycondition += " AND ( AUDITACTCODE IN ("+subcond+") )
					// ";
					querycondition += " AND TRUNC(ACTIONDATE) BETWEEN TO_DATE('" + fromdate
							+ "','dd-mm-yyyy') AND TO_DATE('" + todate + "','dd-mm-yyyy') ";

					if (orderby.equals("$ACTDATE")) {
						orderbycondition = " ORDER BY ACTIONDATE " + ascdesc;
					} else if (orderby.equals("$ACTIONWISE")) {
						orderbycondition += " ORDER BY AUDITACTCODE " + ascdesc;

					}
				}

			}

			// System.out.println("reportheader :"+ reportheader);

			// trace("Formed Condition : " + querycondition );
			trace("Order By Condition : " + orderbycondition);
			List recordslist = null;
			String title = "";
			if (!subcond.equals("4110") && !subcond.equals("11111")) {

				if (getreport.equals("EXCEL")) {
					String Query = querycondition;
					enctrace("Query WITH EXCEL Condition" + Query);
					PadssSecurity sec = new PadssSecurity();
					CommonDesc commondesc = new CommonDesc();
					String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
					System.out.println("keyid::" + keyid);
					List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
					// System.out.println("secList::"+secList);
					Iterator secitr = secList.iterator();
					String dcardno = "", eDMK = "", eDPK = "";
					if (!secList.isEmpty()) {
						while (secitr.hasNext()) {
							Map map = (Map) secitr.next();
							eDMK = ((String) map.get("DMK"));
							eDPK = ((String) map.get("DPK"));

						}
					}
					// List cardList =
					// dao.getMasterReportList(instid,eDMK,eDPK,sec,Query,
					// jdbctemplate);
					List cardList = reportdao.getAuditReport(instid, subcond, loginuser, querycondition,
							orderbycondition, jdbctemplate);

					return ExcelReportGenMaster(subcond, cardList);
				}

				recordslist = reportdao.getAuditReport(instid, subcond, loginuser, querycondition, orderbycondition,
						jdbctemplate);
				trace("Generating audit report...got : " + recordslist.size());
				if (recordslist.isEmpty()) {
					addActionError("No Records Found");
				}
				PadssSecurity padsssec = new PadssSecurity();
				PadssSecurity sec = new PadssSecurity();
				String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
				System.out.println("keyid::" + keyid);
				List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
				// System.out.println("secList::"+secList);
				Iterator secitr = secList.iterator();
				String dcardno = "", eDMK = "", eDPK = "";

				// by siva
				Properties props = getCommonDescProperty();
				String EDPK = props.getProperty("EDPK");
				// by siva

				if (!secList.isEmpty()) {
					while (secitr.hasNext()) {
						Map map = (Map) secitr.next();
						String CDMK = ((String) map.get("DMK"));
						// eDPK = ((String)map.get("DPK"));
						String CDPK = padsssec.decryptDPK(CDMK, EDPK);

					}
				}
				ListIterator itr = recordslist.listIterator();
				String dbuser = "", chekusr = "";
				// String orgchn="";
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					dbuser = commondesc.getUserNameFromTemp(instid, (String) mp.get("USERCODE"), jdbctemplate);
					if (dbuser == null) {
						dbuser = (String) mp.get("USERCODE");
					}

					mp.put("USERCODE", dbuser);

					chekusr = commondesc.getUserNameFromTemp(instid, (String) mp.get("CHECKERID"), jdbctemplate);
					if (chekusr == null) {
						chekusr = (String) mp.get("CHECKERID");
					}

					mp.put("CHECKERID", chekusr);
					String cardno_new = (String) mp.get("CARDNO");
					String auditmsg = (String) mp.get("AUDITMSG");
					// String accno = (String)mp.get("ACCNO");
					// String custname = (String)mp.get("CUSTNAME");
					// String branchcode = (String)mp.get("BRANCHCODE");

					trace("getting cardno in audit trial" + cardno_new);
					if (cardno_new.equals("--") || cardno_new == null) {
						// System.out.println("coming inside");
						mp.put("CARDNO", "Card N/A");
						mp.put("AUDITMSG", "Remarks N/A");
					} else {
						System.out.println("coming outside");
						// String orgchn = sec.getCHN(eDMK, eDPK,cardno_new);
						// String newauditmsg=auditmsg.replaceAll(cardno_new,
						// orgchn);
						if (cardno_new.length() == 32) {
							String CDMK = null;
							// cardno_new= sec.getCHN(eDMK, eDPK,cardno_new);
							cardno_new = padsssec.getCHN(CDMK, cardno_new);
						}
						mp.put("CARDNO", cardno_new);
						mp.put("AUDITMSG", auditmsg);
						// mp.put("ACCOUNT",accno);
						// mp.put("CUSTNAME",custname);
						// mp.put("BRANCHCODE",branchcode);

					}
					itr.remove();
					itr.add(mp);
				}
			} else if (subcond.equals("11111")) {
				if (branchlist.equals("ALL")) {

					condi2 += "";
				}

				else {
					condi2 += " AND  trim(A.CARD_COLLECT_BRANCH) = '" + branchlist + "'";

				}
				if (prodcutcode != null) {
					if (prodcutcode.equals("ALL")) {
						condi2 += "";

					} else {

						condi2 += " AND  PRODUCT_CODE = '" + prodcutcode + "'";

					}
				}
				condi2 += " AND TRUNC(A.ISSUE_DATE) BETWEEN TO_DATE('" + fromdate + "','dd-mm-yyyy') AND TO_DATE('"
						+ todate + "','dd-mm-yyyy') ";

				if (getreport.equals("EXCEL")) {
					String Query = querycondition;
					enctrace("Query WITH EXCEL Condition" + Query);
					PadssSecurity sec = new PadssSecurity();
					CommonDesc commondesc = new CommonDesc();
					String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
					System.out.println("keyid::" + keyid);
					List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
					// System.out.println("secList::"+secList);
					Iterator secitr = secList.iterator();
					String dcardno = "", eDMK = "", eDPK = "";
					if (!secList.isEmpty()) {
						while (secitr.hasNext()) {
							Map map = (Map) secitr.next();
							eDMK = ((String) map.get("DMK"));
							eDPK = ((String) map.get("DPK"));

						}
					}
					// List cardList =
					// dao.getMasterReportList(instid,eDMK,eDPK,sec,Query,
					// jdbctemplate);
					List cardList = reportdao.getAuditReport(instid, subcond, loginuser, condi2, orderbycondition,
							jdbctemplate);

					return ExcelReportGenMaster(subcond, cardList);
				}

				recordslist = reportdao.getAuditReport(instid, subcond, loginuser, condi2, orderbycondition,
						jdbctemplate);
				trace("Generating audit report...got : " + recordslist.size());
				if (recordslist.isEmpty()) {
					addActionError("No Records Found");
				}
				PadssSecurity sec = new PadssSecurity();
				String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
				System.out.println("keyid::" + keyid);
				List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
				// System.out.println("secList::"+secList);
				Iterator secitr = secList.iterator();
				String dcardno = "", eDMK = "", eDPK = "";
				if (!secList.isEmpty()) {
					while (secitr.hasNext()) {
						Map map = (Map) secitr.next();
						eDMK = ((String) map.get("DMK"));
						eDPK = ((String) map.get("DPK"));

					}
				}
				ListIterator itr = recordslist.listIterator();
				/*
				 * String dbuser = ""; //String orgchn=""; while( itr.hasNext()
				 * ){ Map mp = (Map)itr.next(); dbuser =
				 * commondesc.getUserNameFromTemp(instid,
				 * (String)mp.get("USERCODE"), jdbctemplate); if( dbuser == null
				 * ){ dbuser = (String)mp.get("USERCODE"); }
				 * 
				 * mp.put("USERCODE", dbuser); String cardno_new =
				 * (String)mp.get("CARDNO"); String auditmsg =
				 * (String)mp.get("AUDITMSG"); //String accno =
				 * (String)mp.get("ACCNO"); //String custname =
				 * (String)mp.get("CUSTNAME"); //String branchcode =
				 * (String)mp.get("BRANCHCODE");
				 * 
				 * trace("getting cardno in audit trial" + cardno_new); if
				 * (cardno_new.equals("--")|| cardno_new ==null){
				 * System.out.println("coming inside"); mp.put("CARDNO",
				 * "Card N/A"); mp.put("AUDITMSG","Remarks N/A"); } else{
				 * System.out.println("coming outside"); //String orgchn =
				 * sec.getCHN(eDMK, eDPK,cardno_new); //String
				 * newauditmsg=auditmsg.replaceAll(cardno_new, orgchn);
				 * mp.put("CARDNO",cardno_new); mp.put("AUDITMSG",auditmsg);
				 * //mp.put("ACCOUNT",accno); //mp.put("CUSTNAME",custname);
				 * //mp.put("BRANCHCODE",branchcode);
				 * 
				 * } itr.remove(); itr.add(mp); }
				 */
			}
			// summary report
			else if (subcond.equals("4111")) {

			} else {
				String condi1 = " AND TRUNC(ISSUE_DATE) BETWEEN TO_DATE('" + fromdate + "','dd-mm-yyyy') AND TO_DATE('"
						+ todate + "','dd-mm-yyyy') ";
				// String condi2 = " AND TRUNC(b.ISSUE_DATE) BETWEEN
				// TO_DATE('"+fromdate+"','dd-mm-yyyy') AND
				// TO_DATE('"+todate+"','dd-mm-yyyy') ";
				// String condi3 = " AND TRUNC(c.ISSUE_DATE) BETWEEN
				// TO_DATE('"+fromdate+"','dd-mm-yyyy') AND
				// TO_DATE('"+todate+"','dd-mm-yyyy')";

				propertyname = "InstantSummary";

				if (branchlist.equals("ALL")) {
					recordslist = commondesc.generateBranchList(instid, jdbctemplate);
				} else {
					recordslist = commondesc.generateBranchList(instid, branchlist, jdbctemplate);
				}
				int alltot = 0, totalcountval = 0;
				ListIterator itr = recordslist.listIterator();
				while (itr.hasNext()) {
					Map mp = (Map) itr.next();
					System.out.println("coming recordslist" + recordslist);

					String brcode = ((String) mp.get("BRANCH_CODE"));

					System.out.println("coming brcode" + brcode);

					String productioncount = "select to_char(count(*)) from card_production where INST_ID='" + instid
							+ "' " + summarycond + " AND trim(CARD_COLLECT_BRANCH)='" + brcode + "' " + condi1;
					String processNotMap = "select to_char(count(*)) from INST_CARD_PROCESS where CARD_STATUS='05' AND MKCK_STATUS='P' "
							+ summarycond + "  AND INST_ID='" + instid + "'AND trim(CARD_COLLECT_BRANCH)='" + brcode
							+ "' ";
					String processMAPPED = "select to_char(count(*)) from INST_CARD_PROCESS where CARD_STATUS='06' AND MKCK_STATUS='M' "
							+ summarycond + "  AND INST_ID='" + instid + "' AND trim(CARD_COLLECT_BRANCH)='" + brcode
							+ "'" + condi1;
					System.out.println(productioncount + "\n " + processNotMap + "\n " + processMAPPED);
					String prodcount = (String) jdbctemplate.queryForObject(productioncount, String.class);
					String watingtomap = (String) jdbctemplate.queryForObject(processNotMap, String.class);
					String waitforauth = (String) jdbctemplate.queryForObject(processMAPPED, String.class);

					int producttot = Integer.parseInt(prodcount);
					int watingtomaptot = Integer.parseInt(watingtomap);
					int waitforauthtot = Integer.parseInt(waitforauth);

					alltot = producttot + watingtomaptot + waitforauthtot;
					totalcountval += producttot + watingtomaptot + waitforauthtot;
					System.out.println("coming ddd" + alltot);

					String TOTALCARDS = Integer.toString(alltot);
					String allbranchtot = Integer.toString(totalcountval);

					// String tot=TOTALCARDS + " - Total - " + totalcountval;
					mp.put("InProduction", prodcount);
					mp.put("WaitingForCustMap", watingtomap);
					mp.put("WaitingCustMapAuth", waitforauth);
					mp.put("TOTALCARDS", TOTALCARDS);
					mp.put("AlbranchTotal", allbranchtot);

					itr.remove();
					itr.add(mp);
				}

			}

			PDFReportGenerator pdfgen = new PDFReportGenerator(document, output_stream, propertyname, getRequest());

			if (subcond.equals("4109")) {
				title = "Instant card MapWith NewAccount";
			}else if (subcond.equals("4110")) {
				title = "Instant_Summary";
			}  else if (subcond.equals("4102")) {
				reportname = "Lost/Stolen_Cards";
			}  else if (subcond.equals("4103")) {
				reportname = "Damaged Cards History";
				} 
			else if (subcond.equals("4105")) {
				reportname = "Re-pinned Cards History";
				} 
			else if (subcond.equals("4106")) {
				reportname = "Closed_Cards";
				}
			else if (subcond.equals("4107")) {
				reportname = "Normal_Cards";
			} else if (subcond.equals("9091")) {
				title = "Account_Linkage";
			} else if (subcond.equals("4104")) {
				reportname = "Reissued_card";
			} else if (subcond.equals("11111")) {
				reportname = "TotalIssuedCardsInProduction";
			} else {
				title = "Audit Trial Report";
			}

			pdfgen.addPDFTitles(document, title, ALIGN_CENTER);

			setAuditreportname(reportname + ".pdf");

			ReportgenerationAction excel = new ReportgenerationAction();

			if (reportheader.endsWith(",")) {
				reportheader = reportheader.substring(0, reportheader.length() - 1);
			}
			pdfgen.addSingleHeader(document, 3, reportheader, ALIGN_LEFT, 50);

			pdfgen.createSimplePDF(document, title, pdfgen.reportheader, recordslist, pdfgen.reportsumfield,
					ALIGN_CENTER, ALIGN_CENTER, 100);
			pdfgen.closePDF(document);
			input_stream = new ByteArrayInputStream(output_stream.toByteArray());
			output_stream.flush();
			return "itextpdfreport";
		} catch (Exception e) {
			addActionError("Unable to continue the report generation ");
			trace("Exception : " + e.getMessage());
			e.printStackTrace();

		}
		return "cardmaintainhome";
	}

	public String generatetotalcards() throws Exception {
		HttpSession session = getRequest().getSession();
		String instid = comInstId(session);
		List recordslist = null;
		String title = "Total Card Staus";
		int ALIGN_LEFT = 0, ALIGN_CENTER = 1, ALIGN_RIGHT = 2;
		output_stream = new ByteArrayOutputStream();
		Document document = new Document();
		DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss");
		Date date = new Date();
		String reportname = "TOTALCARD_REPORT" + "_" + dateFormat.format(date);
		;

		String propertyname = "TOTALCARDREPORT";
		PDFReportGenerator pdfgen = new PDFReportGenerator(document, output_stream, propertyname, getRequest());
		String reportheader = "";
		String loginuser = comUserId(session);
		String username = commondesc.getUserNameFromTemp(instid, loginuser, jdbctemplate);

		reportheader += "Generated By ,:," + username + " , ";

		recordslist = reportdao.getcardstatus(instid, jdbctemplate);

		/*
		 * ListIterator itr = recordslist.listIterator(); String dbuser = "";
		 * //String orgchn=""; while( itr.hasNext() ){ Map mp = (Map)itr.next();
		 * 
		 * String TOTALCARDS = (String)mp.get("TOTALCARDS"); String STATUS =
		 * (String)mp.get("STATUS");
		 * 
		 * 
		 * 
		 * System.out.println("coming inside"); mp.put("CARDNO","Card N/A");
		 * mp.put("AUDITMSG","Remarks N/A");
		 * 
		 * 
		 * 
		 * itr.remove(); itr.add(mp); }
		 */
		pdfgen.addPDFTitles(document, title, ALIGN_CENTER);

		setAuditreportname(reportname + ".pdf");

		ReportgenerationAction excel = new ReportgenerationAction();

		if (reportheader.endsWith(",")) {
			reportheader = reportheader.substring(0, reportheader.length() - 1);
		}
		pdfgen.addSingleHeader(document, 3, reportheader, ALIGN_LEFT, 50);

		pdfgen.createSimplePDF(document, title, pdfgen.reportheader, recordslist, pdfgen.reportsumfield, ALIGN_CENTER,
				ALIGN_CENTER, 100);
		pdfgen.closePDF(document);
		input_stream = new ByteArrayInputStream(output_stream.toByteArray());
		output_stream.flush();
		return "itextpdfreport";
	}

	public String ExcelReportGenMaster(String subaction, List excelReportList) throws IOException {
		trace("reportGenMaster started ..." + subaction);
		HttpSession session = getRequest().getSession();
		String instid = getRequest().getParameter("INSTID");
		String reportno = "3";
		String filename = "";

		if (subaction.equals("4109")) {
			filename = "InstantCardMapWithNewAccount";
		} else if (subaction.equals("4107")) {
			filename = "Normal_Cards_";
		}
		 else if (subaction.equals("4102")) {
			 filename = "Lost/Stolen_Cards_";
			} 
		 else if (subaction.equals("4105")) {
			 filename = "Re-pinned Cards History_";
			} 
		 else if (subaction.equals("4106")) {
			 filename = "Closed_Cards_";
			} 
		 else if (subaction.equals("4103")) {
			 filename = "Damaged Cards History_";
			} 
		else if (subaction.equals("4110")) {
			filename = "Instant_Summary";
		} else if (subaction.equals("9091")) {
			filename = "Account_Linkage";
		} else if (subaction.equals("4104")) {
			filename = "Reissued_card";
		} else if (subaction.equals("11111")) {
			filename = "TotalIssuedCardsInProduction";
		}

		else {

			System.out.println("reportno::" + reportno);

			List repconfig = this.getReportConfigDetails(instid, reportno, jdbctemplate);
			Iterator iterator = repconfig.iterator();
			String passwdReq = "";
			while (iterator.hasNext()) {
				Map map = (Map) iterator.next();
				filename = (String) map.get("REPORTNAME");
			}
		}
		List result = excelReportList;

		String excelparam = this.getExcelParam(instid, reportno, jdbctemplate);

		List combinedlist = new ArrayList();
		combinedlist.add(result);
		combinedlist.add(result);
		String keyDesc = "successful - unsuccessful";
		String res = this.getExcelReport(combinedlist, excelparam, filename);
		System.out.println("reportGenMaster():::reportGenMaster" + res);
		return res;
	}

	private List getReportConfigDetails(String instid, String reportno, JdbcTemplate jdbctemplate) {
		List getrep = null;
		try {
			String getrepqry = "select REPORTNAME,PASSWORD_REQ,PAGENO_REQ,FOOTERCONTENT from PDFREPORTGENRATOR where RNO='"
					+ reportno + "' ";
			enctrace("getrepqry : " + getrepqry);
			getrep = jdbctemplate.queryForList(getrepqry);
		} catch (EmptyResultDataAccessException e) {
			getrep = null;
		}
		return getrep;

	}

	private String getExcelParam(String instid, String reportno, JdbcTemplate jdbctemplate2) {
		String xlparam = null;
		try {
			String xlparamqry = "select EXCEL_PARAM from PDFREPORTGENRATOR where RNO='" + reportno + "' ";
			enctrace("xlparamqry : " + xlparamqry);
			xlparam = (String) jdbctemplate.queryForObject(xlparamqry, String.class);
		} catch (EmptyResultDataAccessException e) {
			xlparam = null;
		}
		return xlparam;
	}

	public String getExcelReport(List listqry, String keyDesc, String namestr) {
		trace("	 ***************** Transaction Details report ****************");
		enctrace("**************** Transaction Details report ****************");

		trace("listqry:::::" + listqry);
		trace(":::::keyDesc" + keyDesc);
		trace(":::::namestr" + namestr);

		HttpSession session = getRequest().getSession();
		try {
			output_stream = new ByteArrayOutputStream();
			String curdatetime = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
			trace("dateFormat.format(date)    --->  " + curdatetime);
			String defaultname = namestr + curdatetime + ".xls";
			trace("default name" + defaultname);
			SXSSFWorkbook workbook = new SXSSFWorkbook();
			Sheet sheet = null;
			trace(" keyDesc " + keyDesc);
			String keydesc_split[] = keyDesc.split("-");
			System.out.println("keydesc_split[] ==== " + keydesc_split[0]);
			Iterator combined_itr = listqry.iterator();
			int i = 0;
			while (combined_itr.hasNext()) {
				List lst = (List) combined_itr.next();
				trace(" ----- lst  ---- " + lst.size());
				sheet = workbook.createSheet(keydesc_split[i]);
				int rownum = 0;
				int cellnum = 0;
				Iterator itr1 = lst.iterator();
				int rowno = 0;
				Row rowheading = null;
				while (itr1.hasNext()) {
					trace("rowno = ******** = " + rowno);
					Map map = (Map) itr1.next();
					Iterator keyItr = map.keySet().iterator();
					if (rowno == 0) {
						rowheading = sheet.createRow((short) rowno++);
						trace("*******rowheading *****");
					}
					Row row = sheet.createRow((short) rowno++);
					String key = null;
					int cellno = 0;
					while (keyItr.hasNext()) {
						trace("rowno inside second while loop = ******** = " + rowno);
						Cell cell = null;
						key = (String) keyItr.next();
						if (rowno == 2) {
							cell = rowheading.createCell((short) cellno);
							cell.setCellValue(key);
							trace("Key .........." + key);
						}
						cell = row.createCell((short) cellno);
						cell.setCellValue((String) map.get(key));
						trace(" value .... " + (String) map.get(key));
						cellno++;
					}
					trace("rowno outside while loop " + row.getRowNum());
					if (row.getRowNum() > 49998) {
						trace("entered rownum ");
						sheet = workbook.createSheet(keydesc_split[i]);
						rowno = 0;
						continue;
					}
				}
				i++;
				trace("	value of i at the bottom --	" + i + "	rownum -- " + rowno);
			}
			workbook.write(output_stream);
			String fromdate = getRequest().getParameter("fromdate");
			trace("from date getting" + fromdate);
			String todate = getRequest().getParameter("todate");
			trace("from date getting" + fromdate);
			setTransctionreport(defaultname);
			input_stream = new ByteArrayInputStream(output_stream.toByteArray());
			output_stream.flush();
			trace("Close file");
			return "transactionexcel";
		} catch (Exception e) {
			trace("ERROR: ->" + e.getMessage());
			e.printStackTrace();
			addActionError("Could not generate report");
		}
		return "globalreporterror";
	}
}
