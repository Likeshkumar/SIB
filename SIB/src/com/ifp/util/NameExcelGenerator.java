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

public class NameExcelGenerator extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

	private int createContent(WritableSheet sheet, String instid, String ecardno, JSONObject jsondbflds, JdbcTemplate jdbctemplate) throws Exception {

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
				

			}
		}

		String qrystring = (String) preqrysel.subSequence(0, preqrysel.length() - 1);
		String qry = "SELECT " + qrystring + " FROM " + "CARD_PRODUCTION";
		trace("Generating rows... ." + qry);

		List predatalist = this.getneratePREList(instid, qry,ecardno, jdbctemplate);
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

	public int generatePREbranchname(String instid, String ecardno,String cardno,JSONObject listofpreheaders, JSONObject predbfields, HttpSession session, JdbcTemplate jdbctemplate,
			CommonDesc commondesc) throws IOException, WriteException {
		Properties prop = commondesc.getCommonDescProperty();
		String prefilelocation = prop.getProperty("PREFILELOCATION");
		this.setOutputFile(prefilelocation);


		String downprefilename = cardno+ ".xls";

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
			createContent(excelSheet, instid, ecardno, predbfields, jdbctemplate);
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
	
	

	public List getneratePREList(String instid, String query,String ecardno, JdbcTemplate jdbctemplate) {
		List predata = null;

		String predataqry = query + " WHERE INST_ID='" + instid + "'AND ORG_CHN='"+ecardno+"'";
		enctrace("pre gen predataqry: " + predataqry);
		predata = jdbctemplate.queryForList(predataqry);
		return predata;
	}

}
