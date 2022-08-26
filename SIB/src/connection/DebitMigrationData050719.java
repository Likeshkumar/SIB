package connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.expression.spel.generated.SpringExpressionsParser.startNode_return;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.util.CommonDesc;

public class DebitMigrationData050719 {

	/**
	 * @param args
	 * @throws Exception 
	 */
	static JdbcTemplate jdbctemplate = new JdbcTemplate();
	
	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}
	
	public static void main(String[] args) throws Exception {
		
		
		String ClearDMK = null;
		String EDMK = null;
		String DMK_KVC = null;
		String ClearDPK = null;
		String EDPK = null;
		String DPK_KVC = null;
		String ECHN = null;
		//Step 1 -- Get Encrypted DMK
		String DMKComponent1 = "11112222333344445555666677778888";
		String DMKComponent2 = "88887777666655554444333322221111";
		String DMKComponent3 = "AAAABBBBCCCCDDDDEEEEFFFF99990000";
		PadssSecurity sec = new PadssSecurity();
		ClearDMK = sec.getFormattedKey(DMKComponent1, DMKComponent2, DMKComponent3);
		System.out.println("ClearDMK--->" + ClearDMK );
		DMK_KVC = sec.getCheckDigit(ClearDMK);
		System.out.println("DMK KCV--->" + DMK_KVC ); //For Display & Store in DB
		//EDMK = sec.getEDMK(ClearDMK);
		System.out.println("Encrpted DMK--->" + EDMK );//Store in DB
		//Step 2 -- Get Encrypted DPK
		String DPKComponent1 = "0123456789ABCDEFFEDCBA9876543210";
		String DPKComponent2 = "FEDCBA98765432100123456789ABCDEF";
			
		ClearDPK = sec.getFormattedKey(DPKComponent1, DPKComponent2);
		System.out.println("ClearDPK--->" + ClearDPK );
		
		
		
		
		DPK_KVC = sec.getCheckDigit(ClearDPK);
		System.out.println("DPK KCV--->" + DPK_KVC ); //For Display & Store in DB
		
		//EDPK = sec.getEDPK(EDMK, ClearDPK);
		System.out.println("Encrpted DPK--->" + EDPK );//Store in DB
		
		//Step 3 -- Get Encrypted CHN
		

		//ECHN = sec.getECHN(EDMK, EDPK, CHN);
		//System.out.println("Encrpted CHN--->" + ECHN );//Store in DB
		
		
		
		Dbcon dbcon = new Dbcon();
		
		Connection con1 = dbcon.getDBConnection();
		
		
		Statement rst=con1.createStatement();
		Statement rst2 = con1.createStatement();
		Statement rst3 = con1.createStatement();
		Statement rst4 = con1.createStatement();
		Statement rst5 = con1.createStatement();
		Statement rst6 = con1.createStatement();
		Statement rst7 = con1.createStatement();
		Statement rst8 = con1.createStatement();
		StringBuilder sc = new StringBuilder();
		sc.append("select * FROM BIC_MIGRATION ");
				System.out.println(sc.toString());
		ResultSet rrs = rst.executeQuery(sc.toString());
		
		String instid = "BIC";
		//last order ref no=33822
		int orderrefno = 100;
		int orderrefnolength = 12;
		String cardtypeid="001";
		//String acctsubtype="99";
		
		String accounttype="CDTP";
		
		String subproductCode="00000001";
		CommonDesc commondesc = new CommonDesc();
		
		int pcpcnt = 0;int aicnt = 0;int cicnt = 0,totalcount=0;
		while(rrs.next())    
		{
			totalcount = totalcount +1;
			orderrefno = orderrefno +1;
			String cardnumber = rrs.getString("CARDNUM"); 
			
			String customerid = commondesc.cinnumberGeneratoer(instid,jdbctemplate);
			String docid = rrs.getString("OTHER_ID");
			
			String accountno = rrs.getString("ACCTNO");
			
			System.out.println("cardno:"+cardnumber);
			
			String cardcheckqry="select count(*) as cardcnt from PERS_CARD_PROCESS where ACCT_NO='"+accountno+"'";
			int cardcount = 0;
			ResultSet rrs4 = rst6.executeQuery(cardcheckqry);
			while(rrs4.next()){
				cardcount = rrs4.getInt("cardcnt");
				if(cardcount == 0){
					StringBuilder pcp = new StringBuilder();
					pcp.append("INSERT INTO PERS_CARD_PROCESS ");
					pcp.append("select '"+instid+"' INST_ID, LPAD("+orderrefno+","+orderrefnolength+",'0') ORDER_REF_NO,SUBSTR(BIN, 1, 6) BIN, '"+"' CARD_NO,'"+sec.getHashedValue(cardnumber+"BIC")+"' HCARD_NO, '"+sec.getMakedCardno(cardnumber)+"' MCARD_NO,ACCTNO ACCT_NO, ");
					pcp.append("DECODE(ACTYP,'SA','10','CA','20') ACCTTYPE_ID,DECODE(CARD_CLASS,'INDV','001','STAF','002') ACCTSUB_TYPE_ID,'418' ACC_CCY, ");
					pcp.append("OTHER_ID CIN,'"+cardtypeid+"' CARD_TYPE_ID,'"+subproductCode+"' SUB_PROD_ID, ");
					pcp.append("(select PRODUCT_CODE from INSTPROD_DETAILS where '"+subproductCode+"'=SUB_PROD_ID)PRODUCT_CODE, ");
					pcp.append("HOMEBRN BRANCH_CODE,'05'CARD_STATUS,'A'CAF_REC_STATUS,'16' STATUS_CODE,'' CARD_CCY, ");
					pcp.append("'P'PC_FLAG, 'P'ORDER_FLAG,CARDNUM ORG_CHN, CARDNUM USED_CHN,sysdate GENERATED_DATE,add_months(to_date(EXPIRY_DT,'MM-DD-YYYY'),0) EXPIRY_DATE,'000' APP_NO, ");
					pcp.append("sysdate APP_DATE, sysdate PIN_DATE, sysdate PRE_DATE, sysdate PRE_FILE, add_months(to_date(INIT_ISSUE_DT,'MM-DD-YYYY'),0) REG_DATE, sysdate RECV_DATE,sysdate  ISSUE_DATE, sysdate ACTIVE_DATE, ");
					pcp.append("'1'MAKER_ID,sysdate MAKER_DATE, '' CHECKER_ID, '' CHECKER_DATE, 'M' MKCK_STATUS, ");
					pcp.append("'221' SERVICE_CODE,(select FEE_CODE from INSTPROD_DETAILS where '"+subproductCode+"'=SUB_PROD_ID)FEE_CODE, ");
					pcp.append("(select LIMIT_ID from INSTPROD_DETAILS where '"+subproductCode+"'=SUB_PROD_ID)LIMIT_ID, ");
					pcp.append("'0' PRIVILEGE_CODE, '0' WDL_AMT, '0' WDL_CNT, '0' PUR_AMT, '0' PUR_CNT, '0' ECOM_AMT, '0' ECOM_CNT, ");
					pcp.append("LONG_NM EMB_NAME,LONG_NM ENC_NAME,'BIC_MG'COURIER_ID, ''AUTH_DATE, PIN_OFFSET PIN_OFFSET, ");
					pcp.append("''OLD_PIN_OFFSET, ''PIN_RETRY_COUNT,  ");
					pcp.append("''AUTO_ACCT_FLAG, ''CVV1, ''CVV2, ''ICVV, ''CARD_REF_NO, ''APPTYPE, ''REMARKS, 'P'CARDISSUETYPE, "); 
					pcp.append("''COURIERMASTER_ID, ''COURIER_DATE, ''SENDINGADDRESS, PHONENUM MOBILENO,'01' PANSEQ_NO,'N',HOMEBRN CARD_COLLECT_BRANCH,'' ");
					pcp.append("from BIC_MIGRATION  where CARDNUM='"+cardnumber+"'");
					System.out.println(pcp.toString());
					
					pcpcnt = rst2.executeUpdate(pcp.toString());
					
					pcpcnt= pcpcnt+pcpcnt;
				}
			}
			
			
			
			String accountcheckqry="select count(*) as cnt from ACCOUNTINFO where ACCOUNTNO='"+accountno+"'";
			int count = 0;
			ResultSet rrs1 = rst3.executeQuery(accountcheckqry);
			while(rrs1.next()){
				count = rrs1.getInt("cnt");
				System.out.println("Account Count-->"+count);
				
				String orderrefnoqry="select ORDER_REF_NO from PERS_CARD_PROCESS where CIN='"+customerid+"'";
				ResultSet rrs8 = rst8.executeQuery(orderrefnoqry);
				String orederno = "";
				while(rrs8.next()){
					orederno = rrs8.getString("ORDER_REF_NO");
					if(orederno == null){
						orederno = "LPAD("+orderrefno+","+orderrefnolength+",'0')";
					}else{
						orederno = "'"+orederno+"'";
					}
				}
				if(count == 0){
					StringBuilder ai = new StringBuilder();
					ai.append("INSERT INTO ACCOUNTINFO ");
					ai.append("(INST_ID, ORDER_REF_NO, CIN, ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACCT_CURRENCY, ACCOUNTNO, ACCOUNTTYPE, AVAILBAL, LEDGERBAL, ADDEDBY, ADDED_DATE )");
					ai.append("(select '"+instid+"' INST_ID,LPAD("+orderrefno+","+orderrefnolength+",'0')  ORDER_REF_NO, '"+customerid+"' CIN, DECODE(ACTYP,'SA','10','CA','20') ACCTTYPE_ID, ");
					ai.append("DECODE(CARD_CLASS,'INDV','001','STAF','002') ACCTSUB_TYPE_ID, '418' ACCT_CURRENCY, ACCTNO ACCOUNTNO, '"+accounttype+"'ACCOUNTTYPE, ");
					ai.append("'0'AVAILBAL, '0'LEDGERBAL,'1'ADDEDBY, sysdate ADDED_DATE from BIC_MIGRATION  where CARDNUM='"+cardnumber+"' )");
					System.out.println(ai.toString());    
					
					aicnt = rst2.executeUpdate(ai.toString());
					aicnt= aicnt+aicnt;
					
					String customercheckqry="select count(*) as custcnt from CUSTOMERINFO WHERE CIN='"+customerid+"'";
					ResultSet rrs7 = rst7.executeQuery(customercheckqry);
					while(rrs7.next())
					{
						int custcount = rrs7.getInt("custcnt");
						if(custcount == 0){
							StringBuilder ci = new StringBuilder();
							System.out.println("instid-->"+instid+"--orderrefno-->"+orderrefno); 
							ci.append("INSERT INTO CUSTOMERINFO ");
							ci.append("select '"+instid+"'INST_ID, LPAD("+orderrefno+","+orderrefnolength+",'0') ORDER_REF_NO, '"+customerid+"' CIN, LONG_NM FNAME, ''MNAME, ''LNAME, ");
							ci.append(" add_months(to_date(DOB,'MM-DD-YYYY'),0) DOB, '' GENDER, '' MARITAL_STATUS, '' NATIONALITY, '' DOCUMENT_PROVIDED, '"+docid+"' DOCUMENT_NUMBER, '' SPOUCE_NAME, ");
							ci.append("'' MOTHER_NAME, ''FATHER_NAME, PHONENUM MOBILE, E_MAIL E_MAIL, '' P_PO_BOX, '' P_HOUSE_NO, ADDR1 P_STREET_NAME, ADDR2 P_WARD_NAME, ");
							ci.append("'' P_CITY, ADDR3 P_DISTRICT, '' P_PHONE1, '' P_PHONE2, '' C_PO_BOX, '' C_HOUSE_NO, ADDR1 C_STREET_NAME, ADDR2 C_WARD_NAME, ");
							ci.append("'' C_CITY, ADDR3 C_DISTRICT, '' C_PHONE1, '' C_PHONE2, '' MAKER_DATE, '1' MAKER_ID, ''CHECKER_DATE, ''CHECKER_ID, 'P'MKCK_STATUS, 'M'CUSTOMER_STATUS " +
									"from BIC_MIGRATION  where CARDNUM='"+cardnumber+"'  ");
							System.out.println(ci.toString());     
							
							cicnt = rst2.executeUpdate(ci.toString());
							
							cicnt= cicnt+cicnt;
						}
					}
					
					String updaterefqry = commondesc.updatecustidcount(instid);
					jdbctemplate.update(updaterefqry);
				}
			}
		}

		System.out.println("-"+pcpcnt+"-"+aicnt+"-"+cicnt);
		System.out.println("totaalcount--"+totalcount);
		if(pcpcnt==aicnt && aicnt==cicnt)
		{
			con1.commit();
			
		}
		else
		{
			con1.rollback();
		}
		
		rrs.close();
		rst.close();
		con1.close();

	}

}
