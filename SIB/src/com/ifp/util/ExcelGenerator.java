package com.ifp.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.personalize.PreprocessAction;

public class ExcelGenerator extends BaseAction {
	private WritableCellFormat timesBoldUnderline;
	private WritableCellFormat times;
	private String inputFile;
	CommonDesc commondesc = new CommonDesc();

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	public void setOutputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public void write() throws IOException, WriteException {
		File file = new File(inputFile);
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		// createLabel(excelSheet);
		// createContent(excelSheet);

		workbook.write();
		workbook.close();
	}

	private void createLabel(WritableSheet sheet, JSONObject jsonheaderpre) throws WriteException {
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		times = new WritableCellFormat(times10pt);
		times.setWrap(true);

		// Create create a bold font with unterlines
		WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 13, WritableFont.BOLD, false);
		timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
		// Lets automatically wrap the cells
		timesBoldUnderline.setWrap(true);
		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBoldUnderline);
		cv.setSize(15);
		cv.setHidden(true);
		cv.setAutosize(true);
		int headerlen = jsonheaderpre.length();
		System.out.println("Length of the header : " + headerlen);
		for (int x = 1; x <= headerlen; x++) {
			String hkey = "H" + x;
			System.out.println(hkey + " : " + (String) jsonheaderpre.getString(hkey));
			addCaption(sheet, x - 1, 0, (String) jsonheaderpre.getString(hkey));
		}
	}

	private int createContent(WritableSheet sheet, String instid, String productcode, String prefilename,
			String tablename, JSONObject jsondbflds, JdbcTemplate jdbctemplate) throws Exception {

		int headerlen = jsondbflds.length();
		trace("Length of the header : " + headerlen);

		String preqrysel = "";
		for (int x = 1; x <= headerlen; x++) {
			String hkey = "H" + x;
			String dbfld = (String) jsondbflds.getString(hkey);
			System.out.println(hkey + " : " + dbfld);
			if (dbfld.equals("SLNO")) {
				preqrysel += "'SLNO', ";
			} else {

				if (dbfld.equals("TODAYDATE")) {
					preqrysel += "TO_CHAR(SYSDATE,'mm/yyyy') as todate , ";
				}
				if (dbfld.equals("EXPDATE")) {
					//preqrysel += "to_char(EXPDATE,'yymmdd'), ";
					preqrysel += "EXPDATE,";
				}else{
					preqrysel += "to_char(" + dbfld + "),";
					}
				

				/*else { 
					if (dbfld.equals("CARD_TYPE")) {
						// preqrysel += "to_char(EXPDATE,'dd-MON-yy'), ";
						preqrysel += "(select card_type_desc from card_type where card_type_id=substr('" + productcode+ "','9',2)) as CARD_TYPE";
					}else{
					preqrysel += "to_char(" + dbfld + "),";
					}
				}*/

			}
		}

		String qrystring = (String) preqrysel.subSequence(0, preqrysel.length() - 1);
		String qry = "SELECT " + qrystring + " FROM " + tablename;
		trace("Generating rows... ." + qry);

		List predatalist = this.getneratePREList(instid, productcode, prefilename, qry, jdbctemplate);
		trace("Generating rows....got : " + predatalist.size());

		if (predatalist == null || predatalist.isEmpty()) {
			trace("pre list null or empty");
			return 0;
		}

		// ------for clear chn---------//

		String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);

		PadssSecurity padsssec = new PadssSecurity();

		String eDMK = "", eDPK = "", clearchn = "";

		try {
			trace("inside of excelGeneration");
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);

			Iterator secitr = secList.iterator();
			while (secitr.hasNext())

			{
				Map map = (Map) secitr.next();
				eDMK = ((String) map.get("DMK"));
				// eDPK = ((String) map.get("DPK"));
			}

		} catch (Exception e1) {
			e1.printStackTrace();
			return 0;
		}

		// System.out.println("income 123" );
		trace("income 123");

		Properties props = getCommonDescProperty();
		String EDPK = props.getProperty("EDPK");

		Iterator itr = predatalist.iterator();

		trace(" list data =====  " + predatalist);
		int row = 1;
		while (itr.hasNext()) {
			Map mp = (Map) itr.next();
			int col = 0;
			String datavalue = "";

			for (Object key : mp.keySet()) {
				//System.out.println(mp.get(key));
				if (mp.get(key).equals("SLNO")) {
					datavalue = Integer.toString(row);
				} else {
					if (key.equals("TO_CHAR(CARD_NO)")) {
						// trace("chn before"+CARD_NO);
						String ocard = (String) mp.get(key);
						String CDPK = padsssec.decryptDPK(eDMK, EDPK);
						//datavalue = " " + padsssec.getCHN(CDPK, ocard);
						datavalue =padsssec.getCHN(CDPK, ocard);
						// System.out.println("income 2222" );
						trace("income 2222");
						trace("chn" + datavalue);
					} else {
						datavalue = (String) mp.get(key);
						// System.out.println("datavalue");
						trace("datavalue" + datavalue);
						if (datavalue == null) {
							trace("table records Record comes with null value.....");
						}

					}

				}
				// System.out.println("income 4444" );
				trace("income 4444");
				addCaption(sheet, col, row, datavalue);
				trace("after add captiion");
				// System.out.println("after add captiion");
				col++;

			}
			// System.out.println("after add row");
			row++;
		}

		trace("Excel File generated successfully........ ");

		return 1;
	}

	private void addCaption(WritableSheet sheet, int column, int row, String s)
			throws RowsExceededException, WriteException {
		// System.out.println("s"+s);
		// System.out.println("column"+column);
		// System.out.println("row"+row);

		Label label;
		label = new Label(column, row, s);
		sheet.addCell(label);
	}

	/*
	 * private void addNumber(WritableSheet sheet, int column, int row, Integer
	 * integer) throws WriteException, RowsExceededException { Number number;
	 * number = new Number(column, row, integer, times); sheet.addCell(number);
	 * }
	 * 
	 * private void addLabel(WritableSheet sheet, int column, int row, String s)
	 * throws WriteException, RowsExceededException { Label label; label = new
	 * Label(column, row, s, times); sheet.addCell(label); }
	 */

	public int generatePRE(String instid, String productcode, String prefilename, String tablename,
			JSONObject listofpreheaders, JSONObject predbfields, HttpSession session, JdbcTemplate jdbctemplate,
			CommonDesc commondesc, PreprocessAction preprocess) throws Exception {
		Properties prop = commondesc.getCommonDescProperty();
		String prefilelocation = prop.getProperty("PREFILELOCATION");
		// this.setOutputFile(prefilelocation);
		String prodname = commondesc.getProductdesc(instid, productcode, jdbctemplate);
		String prodkeyname = prodname.replace(" ", "_");
		String downprefilename = productcode + "[" + prodkeyname + "]" + "_" + commondesc.getDateTimeStamp() + ".pre";
		trace("PRE Filename : " + downprefilename);
		File nwfile = new File(prefilelocation + "/" + downprefilename);
		session.setAttribute("PRENAME", downprefilename);
		File prefiledir = new File(prefilelocation);
		prefiledir.mkdir();
		if (prefiledir.exists()) {
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
			// preprocess.createPREContent(writer,instid, productcode,
			// prefilename, tablename, predbfields, new File(""), "",
			// jdbctemplate);
			trace("content generated....");

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

	// made by siva 24-10-2018

	public int generatePREtxt(String instid, String productcode, String prefilename, String tablename,
			JSONObject listofpreheaders, JSONObject predbfields, HttpSession session, JdbcTemplate jdbctemplate,
			CommonDesc commondesc) throws Exception

	{
		// List wrPreList1;
		String lpad;
		String rpad;
		String cl_cardno = null;
		Properties prop = commondesc.getCommonDescProperty();
		String prefilelocation = prop.getProperty("PREFILELOCATION");
		this.setOutputFile(prefilelocation);
		/*
		 * String todate = "select to_char(sysdate,'yyyymmdd_hhmm') from dual";
		 * String refnum111 = (String) jdbctemplate.queryForObject(todate,
		 * String.class);
		 */

		String prodname = commondesc.getProductdesc(instid, productcode, jdbctemplate);
		String prodkeyname = prodname.replace(" ", "_");
		String downprefilename = "KIB_MCD" + "_" + commondesc.getDateTimeStamp1() + ".txt";
		/* String downprefilename = "KIB_MCD"+"_"+refnum111+".txt"; */
		// String cl_cardno;
		trace("TXT Filename : " + downprefilename);

		File nwfile = new File(prefilelocation + "/" + downprefilename);
		session.setAttribute("PRENAME", downprefilename);
		File prefiledir = new File(prefilelocation);
		prefiledir.mkdir();

		if (prefiledir.exists()) {
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

		trace("worksheet created....");
		FileWriter writer = new FileWriter(nwfile);
		try {
			////////////
			int x = 1;

			StringBuilder wrPre = new StringBuilder();
			StringBuilder wrrrPre = new StringBuilder();
			// StringBuilder wrrPre = new StringBuilder();

			StringBuilder res = new StringBuilder();
			// String curdatetime = new SimpleDateFormat("yyyyMMdd").format(new
			// Date());
			// String curdatetimenew = new SimpleDateFormat("yyMMdd").format(new
			// Date());
			long j = 1;

			System.out.println("table name for the instant card is " + tablename);
			List wrPreList = null;
			List wrrPreList = null;
			List wrrrPreList = null;
			int sno = 0;
			int sno1 = 1;
			int sno2 = 1;
			List wrPreList1 = null;

			trace("trace1");
			if (tablename.equalsIgnoreCase("PERS_PRE_DATA")) {
				System.out.println("inside HIACEDET name for the instant card is " + tablename);

				/*
				 * wrPre.append(
				 * "select VALID_FROM,enc_name,emb_name,valid_to,cvv2,track1,track2,ADDRESS1,ADDRESS2,ADDRESS3,ADDRESS4,ADDRESS5 from '"
				 * +tablename+"' and RNFNAME  = '" + prefilename + "'");
				 */
				trace("table name" + tablename);
				enctrace("table name" + tablename);

				wrPre.append(
						"select BRANCH_CODE,ICVV,CARD_COLLECT_BRANCH,cin,order_ref_no,generated_date,card_no,emb_cardno,VALID_FROM,enc_name,emb_name,valid_to,cvv2,track1,track2,ADDRESS1,ADDRESS2,ADDRESS3,ADDRESS4,ADDRESS5 from "
								+ tablename + " WHERE PRE_NAME  = '" + prefilename + "' ");
				// wrrPre.append("select
				// P_PO_BOX,P_HOUSE_NO,P_STREET_NAME,P_WARD_NAME,P_CITY,P_DISTRICT,MOBILE
				// FROM CUSTOMERINFO");
				// wrrrPre.append("SELECT BRANCH_NAME FROM ");

				trace("table value" + wrPre);
				enctrace("table value" + wrPre);
				// enctrace("table value - 1 "+wrrPre);

				String padssenable = commondesc.checkPadssEnable(instid, jdbctemplate);

				String keyid = "";
				String EDMK = "", EDPK = "";
				StringBuffer hcardno = new StringBuffer();
				PadssSecurity padsssec = new PadssSecurity();
				List secList = null;
				;
				if (padssenable.equals("Y")) {
					trace("inside padss check");
					keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
					// System.out.println("keyid::"+keyid);
					secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
					// System.out.println("secList::"+secList);

				}

				// wrPre.append("select track1,track2 from "+tablename+"");
				wrPreList = jdbctemplate.queryForList(wrPre.toString());

				// wrrPreList = jdbctemplate.queryForList(wrrPre.toString());
				// enctrace("table value - 2 "+wrrPreList);

				enctrace("getting values from wre append HIACEDET" + wrPre);
				if (!(wrPreList.isEmpty())) {
					String result = null;
					trace("trace2");

					String count1 = "select to_char('" + sno2 + "','FM0000')from dual";
					String refnum1 = (String) jdbctemplate.queryForObject(count1, String.class);
					sno2++;

					String result1 = "select to_char(sysdate,'yyDDD') from dual";
					String pregendate = (String) jdbctemplate.queryForObject(result1, String.class);
					enctrace("PRE generation date" + pregendate);

					// result = jdbctemplate.queryForList(result1);
					// String pregendate = jdbctemplate.queryForList(result1);

					res.append("0KFH VIS PHOTOCARD                       " + "TST" + pregendate + "" + refnum1
							+ "KFV309                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     ")
							.append("\r\n");

					trace("trace3");

					Iterator pre_itr = wrPreList.iterator();

					// Iterator pree_itr = wrrPreList.iterator();

					trace("trace4" + pre_itr);
					enctrace("TTTTT 1" + pre_itr);
					// enctrace("TTTTT 2"+pree_itr);

					while (pre_itr.hasNext()) {
						/*
						 * trace("inside 1"); while(pree_itr.hasNext()) {
						 */
						trace("inside 2");

						String count = "select to_char('" + sno1 + "','FM000000')from dual";
						String refnum = (String) jdbctemplate.queryForObject(count, String.class);
						trace("trace4");
						sno++;
						sno1++;
						Map map = (Map) pre_itr.next();

						String valid_from = ((String) map.get("VALID_FROM"));
						trace("valid_from" + valid_from);
						String valid_to = ((String) map.get("VALID_TO"));
						trace("valid_to" + valid_to);
						String cvv2 = ((String) map.get("CVV2"));
						trace("cvv2" + cvv2);
						String ICVV = ((String) map.get("ICVV"));
						trace("cvv2" + cvv2);
						String enc_name = ((String) map.get("ENC_NAME"));
						trace("enc_name" + enc_name);
						String emb_name = ((String) map.get("EMB_NAME"));
						trace("emb_name" + emb_name);
						String card_no = ((String) map.get("EMB_CARDNO"));
						trace("card_no" + card_no);
						String track1 = ((String) map.get("TRACK1"));
						trace("track1" + track1);
						String track2 = ((String) map.get("TRACK2"));
						trace("track2" + track2);
						String add1 = ((String) map.get("ADDRESS1"));
						String add2 = ((String) map.get("ADDRESS2"));
						String add3 = ((String) map.get("ADDRESS3"));
						String add4 = ((String) map.get("ADDRESS4"));
						String add5 = ((String) map.get("ADDRESS5"));
						String cardno = ((String) map.get("CARD_NO"));
						String home_tel_no = ((String) map.get("PHONENO"));
						// String generated_date =
						// map.get("GENERATED_DATE").toString();
						String CIN = ((String) map.get("CIN"));

						String delsortcode = ((String) map.get("BRANCH_CODE"));
						String cardcollect = ((String) map.get("CARD_COLLECT_BRANCH"));

						String ORDER_REF = ((String) map.get("ORDER_REF_NO"));
						// cin,order_ref_no

						// Map map2 = (Map) pree_itr.next();

						/*
						 * String add11 = ((String) map2.get("P_PO_BOX"));
						 * String add22 = ((String) map2.get("P_HOUSE_NO"));
						 * String add33 = ((String) map2.get("P_STREET_NAME"));
						 * String add44 = ((String) map2.get("P_WARD_NAME"));
						 * String add55 = ((String) map2.get("P_CITY")); String
						 * add66 = ((String) map2.get("P_DISTRICT"));
						 */

						// String MOBILE = ((String) map2.get("MOBILE"));

						trace("cardnumber1" + cardno);
						/* enctrace("table value - 3 "+add11); */
						// enctrace("table value - 4 "+MOBILE);

						Properties props = getCommonDescProperty();
						EDPK = props.getProperty("EDPK");
						trace("rerady to padss change");
						if (padssenable.equals("Y")) {
							trace("inside the padss change");
							Iterator secitr = secList.iterator();
							if (!secList.isEmpty()) {
								while (secitr.hasNext()) {
									trace("inside while loop");
									Map map1 = (Map) secitr.next();
									String eDMK = ((String) map1.get("DMK"));
									// String eDPK = ((String)map1.get("DPK"));
									trace("cardnumber" + cardno);
									String CDPK = padsssec.decryptDPK(eDMK, EDPK);
									cl_cardno = padsssec.getCHN(CDPK, cardno);
									trace("cardnumber" + cl_cardno);
								}
							}
						}
						trace("card number" + cl_cardno);

						String custreg = "select TO_CHAR(MAKER_DATE,'mmyy') from customerinfo where CIN='" + CIN
								+ "' AND ORDER_REF_NO='" + ORDER_REF + "'";
						trace("checking part1" + custreg);
						enctrace("checking part1" + custreg);
						String membersince = (String) jdbctemplate.queryForObject(custreg, String.class);

						// String cardcollect1="select CARD_COLLECT_BRANCH FROM
						// PERS_CARD_PROCESS WHERE CIN='"+CIN+"' AND
						// ORDER_REF_NO='"+ORDER_REF+"' AND
						// ORG_CHN='"+cl_cardno+"' ";
						// String cardcollect = (String)
						// jdbctemplate.queryForObject(cardcollect1,
						// String.class);

						String branch = "select BRANCH_CODE ||'-'|| BRANCH_NAME from BRANCH_MASTER where branch_code='"
								+ cardcollect + "' ";
						trace("checking part2" + branch);
						enctrace("checking part2" + branch);
						String branchname = (String) jdbctemplate.queryForObject(branch, String.class);

						String mobile1 = "select MOBILE FROM CUSTOMERINFO WHERE CIN='" + CIN + "' AND ORDER_REF_NO='"
								+ ORDER_REF + "' ";
						trace("checking part3" + mobile1);
						enctrace("checking part3" + mobile1);

						String mobile = (String) jdbctemplate.queryForObject(mobile1, String.class);

						// trace("cardcollect111"+cardcollect);
						// enctrace("cardcollect111"+cardcollect);

						trace("cardcollect222" + branchname);
						enctrace("cardcollect222" + branchname);

						// String membersince1="select
						// to_char("+membersince+",'MMYY') from dual";
						// String membersince2 = (String)
						// jdbctemplate.queryForObject(membersince1,
						// String.class);

						// String delcode="select BRANCH_CODE FROM
						// PERS_CARD_PROCESS WHERE CIN='"+CIN+"' AND
						// ORDER_REF_NO='"+ORDER_REF+"' AND
						// ORG_CHN='"+cl_cardno+"' ";
						// String delsortcode = (String)
						// jdbctemplate.queryForObject(delcode, String.class);
						enctrace("customer register date" + membersince);

						trace("track1");
						res.append("1" + refnum);
						res.append(rightPadding(emb_name, 54));
						res.append(rightPadding(card_no, 23));
						res.append(rightPadding(valid_from, 5));
						res.append(rightPadding(valid_to, 5));
						res.append("C" + cvv2);
						trace("track2");
						res.append(rightPadding(track1, 79));
						res.append(rightPadding(track2, 166));

						// res.append(rightPadding(NVL(add1,' '),48));
						// res.append(rightPadding(decode(CVV1,' ','000',CVV1))
						// );

						res.append(rightPadding(branchname, 48));

						/*
						 * res.append(rightPadding(add22,48));
						 * res.append(rightPadding(add33,48));
						 * res.append(rightPadding(add44,48));
						 * res.append(rightPadding(add55,48));
						 * res.append(rightPadding(add66,48));
						 */

						res.append(rightPadding("", 384));

						trace("track3");
						// res.append(" ");
						// res.append(rightPadding("",432));

						res.append("0" + "00");
						res.append(
								"     $250000.00%00.00%00.00%$2500.00                                   00000000000000");
						res.append("03");
						res.append(rightPadding(cl_cardno, 16));
						res.append("                                             00-00-00 ");
						res.append(rightPadding(delsortcode, 6));
						res.append(rightPadding(membersince, 4));
						res.append("N ");
						res.append(refnum);
						res.append("B  00                ");
						trace("track4");
						res.append(rightPadding(mobile, 20));
						res.append(rightPadding("", 20));
						// res.append("261042701978");
						res.append(rightPadding("", 12));
						res.append("        KFV309");
						res.append(rightPadding(ICVV, 4));
						res.append("#END#");
						trace("track5");

						// res.append(lpad("+card_no+",4,' '));

						res.append("\r\n");

						// res.append("1"+refnum+""+emb_name+ ""+card_no+
						// ""+valid_from+""+valid_to+"C"+cvv2+""+track1+
						// ""+track2+" GZAYL S SOUD ALAJMY
						// TEL:(97829300/97829300/97829300)-1 BLK 5 ST 52 JDA
						// HOUSE 19 12345 JABAR HAALI KUWAIT 12345 000
						// $250000.00%00.00%00.00%$2500.00
						// 0000000000000003"+cl_cardno+ "00-00-00 0818N 000004Y
						// 00 97829300 97829300 276091201242 KFV309
						// #END#"+"\n");

						/*
						 * res.append("select"); res.append('1'+refnum);
						 * res.append("rpad("+emb_name+",54,' ')");
						 * res.append("lpad("+card_no+",4,' ')");
						 * res.append(valid_from); res.append("from " +
						 * tablename + " WHERE PRE_NAME =" + prefilename);
						 * 
						 * enctrace("getting values from wre append" + res);
						 * wrPreList1 =
						 * jdbctemplate.queryForList(res.toString()); if
						 * (!(wrPreList.isEmpty())) { Iterator pre_itr1 =
						 * wrPreList.iterator(); while (pre_itr.hasNext()) { Map
						 * map2 = (Map) pre_itr.next(); String preData =
						 * ((String) map.get("TRACKDATA")); writer.write(preData
						 * + "\n"); } }
						 */

						// rpad(add1,28,' ')
						// res.append("1"+"+refnum+rpad(add1,28,' ')");
						// res.append(substr(EMB_CARDNO,1,4));

						// res.append("1000001HIBAT ABDULLAH ALOTHMAN 5391 7169
						// 9047 9644
						// 04/1804/21P102%B5391716990479644^ALOTHMAN/HIBAT
						// ABDULLAH^2104201301955000000000156000000?
						// ;5391716990479644=21042013019515650000?0 TALAL HUSAIN
						// H ALJADAN TEL:(4821000/9661215/9661215)-1 P.O.BOX: 39
						// 80000 FARWANIYA KUWAIT 80000 000
						// $1000000.00%00.00%00.00%$2500.00
						// 00000000000000035391716990479644 00-00-00 0418N
						// 000004Y 00 4821000 9661215 261042701978 KFV309
						// #END#"+"\n");

						trace("trace5");
						res.append(
								"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\r\n");
						// j++;

					}
					trace("trace6");
					String count = "select to_char('" + sno + "','FM000000')from dual";
					String refnum = (String) jdbctemplate.queryForObject(count, String.class);
					res.append("2" + refnum + "01" + "00000000000000001" + refnum
							+ "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\r\n");

				}
			}
			trace("trace7");
			writer.write(res.toString());

		}

		catch (Exception e) {
			trace("trace8");
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.getMessage());
			trace("Exception ..generating text file : " + e.getMessage());
			return -1;
		} finally {
			trace("trace9");
			writer.close();
		}

		return 1;
	}

	// made by siva 24-10-2018

	private String NVL(String add1, char c) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String padLeftSpaces(String str, int n) {
		return String.format("%1$" + n + "s", str);
	}

	public static String rightPadding(String str, int num) {
		return String.format("%1$-" + num + "s", str);
	}

	public int generatePREbranchname(String instid, String productcode, String prefilename,String FINDbranchname, String tablename,
			JSONObject listofpreheaders, JSONObject predbfields, HttpSession session, JdbcTemplate jdbctemplate,
			CommonDesc commondesc) throws IOException, WriteException {
		Properties prop = commondesc.getCommonDescProperty();
		String prefilelocation = prop.getProperty("PREFILELOCATION");
		this.setOutputFile(prefilelocation);

		String prodname = commondesc.getProductdesc(instid, productcode, jdbctemplate);

		String prodkeyname = prodname.replace(" ", "_");
		String downprefilename = productcode + "[" + prodkeyname + "]" + "_" +FINDbranchname+"_"+ commondesc.getDateTimeStamp() + ".xls";

		trace("Excel Filename : " + downprefilename);

		File nwfile = new File(prefilelocation + "/" + downprefilename);
		session.setAttribute("PRENAME", downprefilename);
		File prefiledir = new File(prefilelocation);
		prefiledir.mkdir();

		if (prefiledir.exists()) {
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

		WorkbookSettings wbSettings = new WorkbookSettings();
		WritableWorkbook workbook = Workbook.createWorkbook(nwfile, wbSettings);
		workbook.createSheet("Personalization", 0);
		WritableSheet excelSheet = workbook.getSheet(0);

		trace("worksheet created....");
		try {
			createLabel(excelSheet, listofpreheaders);
			trace("label created....");

			trace("ready to check content generate");
			createContent(excelSheet, instid, productcode, prefilename, tablename, predbfields, jdbctemplate);
			trace("content generated....");

			workbook.write();
			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.getMessage());
			trace("Exception ..generating excel file : " + e.getMessage());
			return -1;
		}

		trace("Check the result file under  " + nwfile);

		return 1;
	}
	
	public int generatePRE(String instid, String productcode, String prefilename, String tablename,
			JSONObject listofpreheaders, JSONObject predbfields, HttpSession session, JdbcTemplate jdbctemplate,
			CommonDesc commondesc) throws IOException, WriteException {
		Properties prop = commondesc.getCommonDescProperty();
		String prefilelocation = prop.getProperty("PREFILELOCATION");
		this.setOutputFile(prefilelocation);

		String prodname = commondesc.getProductdesc(instid, productcode, jdbctemplate);

		String prodkeyname = prodname.replace(" ", "_");
		String downprefilename = productcode + "[" + prodkeyname + "]" + "_" + commondesc.getDateTimeStamp() + ".xls";

		trace("Excel Filename : " + downprefilename);

		File nwfile = new File(prefilelocation + "/" + downprefilename);
		session.setAttribute("PRENAME", downprefilename);
		File prefiledir = new File(prefilelocation);
		prefiledir.mkdir();

		if (prefiledir.exists()) {
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

		WorkbookSettings wbSettings = new WorkbookSettings();
		WritableWorkbook workbook = Workbook.createWorkbook(nwfile, wbSettings);
		workbook.createSheet("Personalization", 0);
		WritableSheet excelSheet = workbook.getSheet(0);

		trace("worksheet created....");
		try {
			createLabel(excelSheet, listofpreheaders);
			trace("label created....");

			trace("ready to check content generate");
			createContent(excelSheet, instid, productcode, prefilename, tablename, predbfields, jdbctemplate);
			trace("content generated....");

			workbook.write();
			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.getMessage());
			trace("Exception ..generating excel file : " + e.getMessage());
			return -1;
		}

		trace("Check the result file under  " + nwfile);

		return 1;
	}

	public List getneratePREList(String instid, String productcode, String prefilename, String query,
			JdbcTemplate jdbctemplate) {
		List predata = null;

		String predataqry = query + " WHERE INST_ID='" + instid + "' AND PRODUCT_CODE='" + productcode
				+ "' AND PRE_NAME='" + prefilename + "' AND AUTH_CODE='1'";
		enctrace("pre gen predataqry: " + predataqry);
		predata = jdbctemplate.queryForList(predataqry);
		return predata;
	}

}
