package connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import com.ifg.Config.padss.PadssSecurity;

public class DebitMigrate {
	static PadssSecurity padsssec = new PadssSecurity();
	public static void main(String[] args) throws Throwable {
		
		
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
		
		Properties props=getCommonDescProperty();
		 EDPK=props.getProperty("EDPK");
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
		sc.append("select CUSTOMER_ID,CARD_HOLDER_NUMBER,PRIMARY_ACCOUNT_NUMBER FROM CARDMARCH where BIN_CODE='501817' and STATUS='01' AND PRIMARY_ACCOUNT_NUMBER is not null ");
		System.out.println("data get form cardmarch"+sc.toString());
		ResultSet rrs = rst.executeQuery(sc.toString());
		
		String instid = "AZIZI";
		//last order ref no=33822
		int orderrefno = 52060;
		int orderrefnolength = 12;
		String cardtypeid="001";
		//String acctsubtype="99";
		
		String accounttype="CDTP";
		
		String subproductCode="00000006";
		
		
		int pcpcnt = 0;int aicnt = 0;int cicnt = 0,totalcount=0;
		while(rrs.next())    
		{
			
			//String cardnumber ="5018170010000291";//
			String cardnumber = rrs.getString("CARD_HOLDER_NUMBER"); 
			String customerid=rrs.getString("CUSTOMER_ID");
			//String accountno ="00103150968547"; 
			String accountno = rrs.getString("PRIMARY_ACCOUNT_NUMBER");
			int prodcount = 0;
			//Remove this check.. do it in backend
			String cardcheckqrycount="select count(*) as cardcnt from CARD_PRODUCTION where ORG_CHN='"+cardnumber+"'";
			System.out.println("checking fro card count"+cardcheckqrycount);
			ResultSet rrs5 = rst6.executeQuery(cardcheckqrycount);
			if(rrs5.next()){
				prodcount = rrs5.getInt("cardcnt");
				if(prodcount > 0){
					continue;
				}//end of if
				else{
					
					
					//String customerid ="0010000291";//rrs.getString("CUSTOMER_ID");
										
					System.out.println("cardno:"+cardnumber);
					
					    
					String cardcheckqry="select count(*) as cardcnt from PERS_CARD_PROCESS where ACCT_NO='"+accountno+"'";
					int cardcount = 0;
					ResultSet rrs4 = rst6.executeQuery(cardcheckqry);
					while(rrs4.next()){
						cardcount = rrs4.getInt("cardcnt");
						if(cardcount> 0){
							continue;
							
						}//end of if 
						else{
							
							String CDPK=padsssec.decryptDPK(EDMK, EDPK);
							StringBuilder pcp = new StringBuilder();
							pcp.append("INSERT INTO PERS_CARD_PROCESS ");
							pcp.append("select '"+instid+"' INST_ID, LPAD("+orderrefno+","+orderrefnolength+",'0') ORDER_REF_NO,BIN_CODE BIN, '"+sec.getECHN(CDPK, cardnumber)+"' CARD_NO,'"+sec.getHashedValue(cardnumber+"AZIZI")+"' HCARD_NO, '"+sec.getMakedCardno(cardnumber)+"' MCARD_NO,PRIMARY_ACCOUNT_NUMBER ACCT_NO, ");
							pcp.append("ACCOUNT_TYPE ACCTTYPE_ID,PRODUCT_CODE ACCTSUB_TYPE_ID,'971' ACC_CCY, ");
							pcp.append("CUSTOMER_ID CIN,'"+cardtypeid+"' CARD_TYPE_ID,'"+subproductCode+"' SUB_PROD_ID, ");
							pcp.append("(select PRODUCT_CODE from INSTPROD_DETAILS where '"+subproductCode+"'=SUB_PROD_ID)PRODUCT_CODE, ");
							pcp.append("BRANCH_CODE BRANCH_CODE,'05'CARD_STATUS,'A'CAF_REC_STATUS,'16' STATUS_CODE,'' CARD_CCY, ");
							pcp.append("'P'PC_FLAG, 'P'ORDER_FLAG,CARD_HOLDER_NUMBER ORG_CHN, CARD_HOLDER_NUMBER USED_CHN,sysdate GENERATED_DATE,to_date(EXPIRY_DATE,'YYYYMMDD') EXPIRY_DATE,'000' APP_NO, ");
							pcp.append("sysdate APP_DATE, sysdate PIN_DATE, sysdate PRE_DATE, sysdate PRE_FILE, sysdate REG_DATE, sysdate RECV_DATE, sysdate ISSUE_DATE, sysdate ACTIVE_DATE, ");
							pcp.append("'1'MAKER_ID,sysdate MAKER_DATE, '' CHECKER_ID, '' CHECKER_DATE, 'M' MKCK_STATUS, ");
							pcp.append("'500' SERVICE_CODE,(select FEE_CODE from INSTPROD_DETAILS where '"+subproductCode+"'=SUB_PROD_ID)FEE_CODE, ");
							pcp.append("(select LIMIT_ID from INSTPROD_DETAILS where '"+subproductCode+"'=SUB_PROD_ID)LIMIT_ID, ");
							pcp.append("'0' PRIVILEGE_CODE, '0' WDL_AMT, '0' WDL_CNT, '0' PUR_AMT, '0' PUR_CNT, '0' ECOM_AMT, '0' ECOM_CNT, ");
							pcp.append("CUSTOMER_NAME EMB_NAME,CUSTOMER_NAME ENC_NAME,''COURIER_ID, ''AUTH_DATE, '0000' PIN_OFFSET, ");
							pcp.append("''OLD_PIN_OFFSET, ''PIN_RETRY_COUNT,  ");
							pcp.append("''AUTO_ACCT_FLAG, ''CVV1, ''CVV2, ''ICVV, ''CARD_REF_NO, ''APPTYPE, ''REMARKS, 'P'CARDISSUETYPE, "); 
							pcp.append("''COURIERMASTER_ID, ''COURIER_DATE, ''SENDINGADDRESS, MOBILE_NUMBER MOBILENO,PAN_SEQUENCE_NUMBER PANSEQ_NO,'N',''");
							pcp.append("from CARDMARCH  where BIN_CODE='501817' and PRIMARY_ACCOUNT_NUMBER is not null and CARD_HOLDER_NUMBER='"+cardnumber+"'");
							System.out.println(pcp.toString());
		
							pcpcnt = rst2.executeUpdate(pcp.toString());
		
							pcpcnt= pcpcnt+pcpcnt;
	
						}//end of Else
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
						if(count > 0){
							continue;
						}//End of if
						else{

							StringBuilder ai = new StringBuilder();
							ai.append("INSERT INTO ACCOUNTINFO ");
							ai.append("(INST_ID, ORDER_REF_NO, CIN, ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACCT_CURRENCY, ACCOUNTNO, ACCOUNTTYPE, AVAILBAL, LEDGERBAL, ADDEDBY, ADDED_DATE )");
							ai.append("(select '"+instid+"' INST_ID,"+orederno+"  ORDER_REF_NO, '"+customerid+"' CIN, ACCOUNT_TYPE ACCTTYPE_ID, ");
							ai.append("PRODUCT_CODE ACCTSUB_TYPE_ID, '971' ACCT_CURRENCY, PRIMARY_ACCOUNT_NUMBER ACCOUNTNO, '"+accounttype+"'ACCOUNTTYPE, ");
							ai.append("'0'AVAILBAL, '0'LEDGERBAL,'1'ADDEDBY, sysdate ADDED_DATE from CARDMARCH ");
							ai.append("WHERE CUSTOMER_ID='"+customerid+"' and CARD_HOLDER_NUMBER='"+cardnumber+"' )");
							System.out.println(ai.toString());    
							
							aicnt = rst2.executeUpdate(ai.toString());
							aicnt= aicnt+aicnt;
							
							String customercheckqry="select count(*) as custcnt from CUSTOMERINFO WHERE CIN='"+customerid+"'";
							ResultSet rrs7 = rst7.executeQuery(customercheckqry);
							while(rrs7.next())
							{
								int custcount = rrs7.getInt("custcnt");
								if(custcount> 0){
									
								}//end of IF
								else{
									StringBuilder ci = new StringBuilder();
									System.out.println("instid-->"+instid+"--orderrefno-->"+orderrefno); 
									ci.append("INSERT INTO CUSTOMERINFO ");
									ci.append("select '"+instid+"'INST_ID, "+orederno+" ORDER_REF_NO, '"+customerid+"' CIN, CUSTOMER_NAME FNAME, ''MNAME, ''LNAME, ");
									ci.append("NVL(BIRTH_DATE,'01-JAN-1900') DOB, '' GENDER, '' MARITAL_STATUS, '' NATIONALITY, '' DOCUMENT_PROVIDED, '' DOCUMENT_NUMBER, '' SPOUCE_NAME, ");
									ci.append("'' MOTHER_NAME, ''FATHER_NAME, MOBILE_NUMBER MOBILE, E_MAIL E_MAIL, '' P_PO_BOX, '' P_HOUSE_NO, '' P_STREET_NAME, '' P_WARD_NAME, ");
									ci.append("'' P_CITY, '' P_DISTRICT, '' P_PHONE1, '' P_PHONE2, '' C_PO_BOX, '' C_HOUSE_NO, '' C_STREET_NAME, '' C_WARD_NAME, ");
									ci.append("'' C_CITY, '' C_DISTRICT, '' C_PHONE1, '' C_PHONE2, '' MAKER_DATE, '1' MAKER_ID, ''CHECKER_DATE, ''CHECKER_ID, 'P'MKCK_STATUS, 'M'CUSTOMER_STATUS from CARDMARCH ");
									ci.append("WHERE CUSTOMER_ID='"+customerid+"' and CARD_HOLDER_NUMBER='"+cardnumber+"' and BIN_CODE='501817' and PRIMARY_ACCOUNT_NUMBER is not null ");
									System.out.println(ci.toString());     
									
									cicnt = rst2.executeUpdate(ci.toString());
									
									cicnt= cicnt+cicnt;
								}
							}
						
						}//End of Else
					}
				
				}
			}
			/*String addoncheckqry="select count(*) as acccount from PERS_CARD_PROCESS  where hcard_no='"+sec.getHashedValue(cardnumber+"AZIZI")+"'";
			ResultSet rrs2 = rst4.executeQuery(addoncheckqry);
			while(rrs2.next()){
				int addonacc = rrs2.getInt("acccount");
				if(addonacc == 0){
					StringBuilder pcp1 = new StringBuilder();
					pcp1.append("INSERT INTO PERS_CARD_PROCESS ");
					pcp1.append("select '"+instid+"' INST_ID, (select ORDER_REF_NO from PERS_CARD_PROCESS where '"+accountno+"'=ACCT_NO and rownum=1) ORDER_REF_NO,BIN_CODE BIN, '"+sec.getECHN(EDMK, EDPK, cardnumber)+"' CARD_NO,'"+sec.getHashedValue(cardnumber+"AZIZI")+"' HCARD_NO, '"+sec.getMakedCardno(cardnumber)+"' MCARD_NO,PRIMARY_ACCOUNT_NUMBER ACCT_NO, ");
					pcp1.append("ACCOUNT_TYPE ACCTTYPE_ID,PRODUCT_CODE ACCTSUB_TYPE_ID,'971' ACC_CCY, ");
					pcp1.append("CUSTOMER_ID CIN,'"+cardtypeid+"' CARD_TYPE_ID,'"+subproductCode+"' SUB_PROD_ID, ");
					pcp1.append("(select PRODUCT_CODE from INSTPROD_DETAILS where '"+subproductCode+"'=SUB_PROD_ID)PRODUCT_CODE, ");
					pcp1.append("BRANCH_CODE BRANCH_CODE,'05'CARD_STATUS,'AC'CAF_REC_STATUS,'16' STATUS_CODE,'' CARD_CCY, ");
					pcp1.append("'P'PC_FLAG, 'P'ORDER_FLAG,CARD_HOLDER_NUMBER ORG_CHN, CARD_HOLDER_NUMBER USED_CHN,sysdate GENERATED_DATE,EXPIRY_DATE EXPIRY_DATE,'000' APP_NO, ");
					pcp1.append("sysdate APP_DATE, sysdate PIN_DATE, sysdate PRE_DATE, sysdate PRE_FILE, sysdate REG_DATE, sysdate RECV_DATE, sysdate ISSUE_DATE, sysdate ACTIVE_DATE, ");
					pcp1.append("'1'MAKER_ID,sysdate MAKER_DATE, '' CHECKER_ID, '' CHECKER_DATE, 'M' MKCK_STATUS, ");
					pcp1.append("'500' SERVICE_CODE,(select FEE_CODE from INSTPROD_DETAILS where '"+subproductCode+"'=SUB_PROD_ID)FEE_CODE, ");
					pcp1.append("(select LIMIT_ID from INSTPROD_DETAILS where '"+subproductCode+"'=SUB_PROD_ID)LIMIT_ID, ");
					pcp1.append("'0' PRIVILEGE_CODE, '0' WDL_AMT, '0' WDL_CNT, '0' PUR_AMT, '0' PUR_CNT, '0' ECOM_AMT, '0' ECOM_CNT, ");
					pcp1.append("CUSTOMER_NAME EMB_NAME,CUSTOMER_NAME ENC_NAME,''COURIER_ID, ''AUTH_DATE, '0000' PIN_OFFSET, ");
					pcp1.append("''OLD_PIN_OFFSET, ''PIN_RETRY_COUNT,  ");
					pcp1.append("''AUTO_ACCT_FLAG, ''CVV1, ''CVV2, ''ICVV, ''CARD_REF_NO, ''APPTYPE, ''REMARKS, 'M'CARDISSUETYPE, "); 
					pcp1.append("''COURIERMASTER_ID, ''COURIER_DATE, ''SENDINGADDRESS, MOBILE_NUMBER MOBILENO,PAN_SEQUENCE_NUMBER PANSEQ_NO,'N','' ");
					pcp1.append("from CARD_MARCH  where BIN_CODE='501817' and PRIMARY_ACCOUNT_NUMBER is not null and CARD_HOLDER_NUMBER='"+cardnumber+"'");
					System.out.println(pcp1.toString());
					
					pcpcnt = rst5.executeUpdate(pcp1.toString());
					pcpcnt= pcpcnt+pcpcnt;
				}
			}*/
			
			        
			
		}

		System.out.println("-"+pcpcnt+"-"+aicnt+"-"+cicnt);
		System.out.println("totaalcount--"+totalcount);
		if(pcpcnt==totalcount)
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

	}//end of Main Method
	private static Properties getCommonDescProperty() {
		// TODO Auto-generated method stub
		return null;
	}

}
