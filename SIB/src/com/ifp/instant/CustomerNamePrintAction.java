package com.ifp.instant;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.ExcelGenerator;
import com.ifp.util.IfpTransObj;
import com.ifp.util.NameExcelGenerator;

public class CustomerNamePrintAction extends BaseAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();
	JdbcTemplate jdbctemplate = new JdbcTemplate();
	CommonDesc commondesc = new CommonDesc();

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
	
	public List cardslist;
	
	public List getCardslist() {
		return cardslist;
	}

	public void setCardslist(List cardslist) {
		this.cardslist = cardslist;
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
	

	public String namePrintHome(){
		
		
		System.out.println(" Enters namePrintHome");
		
		return "namePrintHome";
	}
	
public String namePrintList() throws Exception{
		
		
		System.out.println(" Enters namePrintList");
		String ecardno = "";
		String cardno = getRequest().getParameter("cardno");
		
		List secList = commondesc.getPADSSDetailById("2", jdbctemplate);

		Properties props = getCommonDescProperty();
		String EDPK = props.getProperty("EDPK");

		Iterator secitr = secList.iterator();
		if (!secList.isEmpty()) {
			while (secitr.hasNext()) {
				Map map = (Map) secitr.next();
				String CDMK = ((String) map.get("DMK"));
				PadssSecurity padsssec = new PadssSecurity();
				String CDPK = padsssec.decryptDPK(CDMK, EDPK);
				ecardno = padsssec.getECHN(CDPK, cardno);
				System.out.println("ecardno-->" + ecardno);

			}
		}
		
		HttpSession session = getRequest().getSession();

		String instid = comInstId(session);
		
		System.out.println("cardno===>"+cardno+" instid==>"+instid);
		
		String cardslistqry = "SELECT A.ORDER_REF_NO,A.MCARD_NO,C.HCARD_NO,A.ORG_CHN,A.ACCOUNT_NO,A.EMB_NAME,A.CIN,A.ORG_CHN,A.BIN,A.MOBILENO"
				+ " FROM CARD_PRODUCTION A,EZCARDINFO B, CARD_PRODUCTION_HASH C WHERE A.INST_ID=B.INSTID AND A.INST_ID=? AND  "
				+ " C.HCARD_NO=B.CHN and a.cin=c.cin AND A.ORG_CHN=? AND B.STATUS=? ";
		enctrace("cardslistqry  :" + cardslistqry);
		System.out.println(cardslistqry);
		
		try{
			cardslist = jdbctemplate.queryForList(cardslistqry, new Object[] { instid,ecardno,"16"});
			System.out.println(cardslist);
		}catch (Exception e) {
			addActionError("Unable to Process");
			return "required_home";
		}
		
		return "namePrintList";
	}


	public String nameFileDownload() throws KeyException, Exception{
		
		
		HttpSession session = getRequest().getSession();

		Properties prop = commondesc.getCommonDescProperty();
		String prefilelocation = prop.getProperty("PREFILELOCATION");

		trace("instantpre" + prefilelocation);

		String instid = comInstId(session);
		String usercode = comUserId(session);
		String ecardno = getRequest().getParameter("cardno");
		System.out.println("cardno--->"+ecardno);
		IfpTransObj transact = commondesc.myTranObject("PREDOWNLOAD", txManager);

		JSONObject listofpreheaders = this.generatePREHeader();
		JSONObject predbfields = this.generatePREFDBFields();
		NameExcelGenerator excelgen = new NameExcelGenerator();
		
		String cardno = "NA";
		
		Properties props = getCommonDescProperty();
		String EDPK = props.getProperty("EDPK");
		List secList = commondesc.getPADSSDetailById("2", jdbctemplate);
		Iterator secitr = secList.iterator();
		if (!secList.isEmpty()) {
			while (secitr.hasNext()) {
				Map map = (Map) secitr.next();
				String CDMK = ((String) map.get("DMK"));
				PadssSecurity padsssec = new PadssSecurity();
				String CDPK = padsssec.decryptDPK(CDMK, EDPK);
				cardno = padsssec.getCHN(CDPK, ecardno);
				System.out.println("cardno-->" + cardno);

			}
		}
		String filename = null;
		try {

			trace("Generating presonalization file....excel  " + predbfields);
			int x = excelgen.generatePREbranchname(instid,ecardno,cardno, listofpreheaders, predbfields, session,jdbctemplate, commondesc);
			trace("Generating presonalization file....got : " + x);
			if (x < 0) {
				return "required_home";
			}
			session.setAttribute("preverr", "S");
			filename = "<span style='color:maroon'>" + prefilelocation + session.getAttribute("PRENAME")
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
		fileName = cardno+".xls";
		inputStream = new FileInputStream(new File( prefilelocation + session.getAttribute("PRENAME")));

		return "namedownload";

	}
	
	private JSONObject generatePREHeader() {
		JSONObject jsonpre = new JSONObject();
		jsonpre.put("H1", "SerialNo");
		jsonpre.put("H2", "EmbName");
		jsonpre.put("H3", "EncName");
		return jsonpre;
	}
	private JSONObject generatePREFDBFields() {
		JSONObject jsonpre = new JSONObject();
		jsonpre.put("H1", "SLNO");
		jsonpre.put("H2", "EMB_NAME");
		jsonpre.put("H3", "ENC_NAME");
		return jsonpre;
	}


}
