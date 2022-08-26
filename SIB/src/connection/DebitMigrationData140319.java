package connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

//import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.expression.spel.generated.SpringExpressionsParser.startNode_return;

//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//import org.springframework.transaction.PlatformTransactionManager;


import com.ifg.Config.padss.PadssSecurity;




import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.sql.DataSource;


//import org.apache.commons.codec.binary.Base64;
//import org.springframework.expression.spel.generated.SpringExpressionsParser.startNode_return;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

public class DebitMigrationData140319 {
	static AljaberMigrationUtil migrationutil = new AljaberMigrationUtil();
	/**
	 * @param args
	 * @throws Exception 
	 */
	//private static PlatformTransactionManager  txManager = new DataSourceTransactionManager();
	
	final static String CARDACTIVATEDCODE="09";
	public static void main(String[] args) throws Exception {
		

		
		PadssSecurity sec = new PadssSecurity();
	//	IfpTransObj transact = migrationutil.myTranObject("UPDATECOMM", txManager);
		
		Dbcon dbcon = new Dbcon();
		migrationutil.mtrace(" Start con...");

		DataSource ds = dbcon.getConnection();
	
		JdbcTemplate jdbctemplate2 = new JdbcTemplate(ds);
		Connection con1 = dbcon.getDBConnection();
		
		

		

		//migrationutil.mtrace("*************CARD MIGRATION SELECTED CARD LIST*****************");
		//encmigrationutil.mtrace("*************CARD MIGRATION SELECTED CARD LIST*****************");
		//HttpSession session = getRequest().getSession();
		String instid = "IBA";
		String userid = "sardar";
		//IfpTransObj transact = commondesc.myTranObject("RECVORDER", txManager);
		//String card_no[] = getRequest().getParameterValues("personalrefnum");
		//String accountno[] = getRequest().getParameterValues("accountno");
		
		
		
		//last order ref no=33822
		//int orderrefno = 140224;
		int orderrefnolength = 12;
		//String cardtypeid="001";
		//String acctsubtype="99";
		
		String accounttype="CDTP";
		
		String subproductCode="";
		
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
		
		ClearDMK = sec.getFormattedKey(DMKComponent1, DMKComponent2, DMKComponent3);
	System.out.println("ClearDMK--->" + ClearDMK );
		DMK_KVC = sec.getCheckDigit(ClearDMK);
		System.out.println("DMK KCV--->" + DMK_KVC ); //For Display & Store in DB
		//EDMK = sec.getEDMK(ClearDMK);
		//System.out.println("Encrpted DMK--->" + EDMK );//Store in DB
		//Step 2 -- Get Encrypted DPK
		String DPKComponent1 = "0123456789ABCDEFFEDCBA9876543210";
		String DPKComponent2 = "FEDCBA98765432100123456789ABCDEF";
			
		ClearDPK = sec.getFormattedKey(DPKComponent1, DPKComponent2);
	//	System.out.println("ClearDPK--->" + ClearDPK );
		
		String tablename ="CARD_PRODUCTION";
		String productcode="";
		
		DPK_KVC = sec.getCheckDigit(ClearDPK);
		//System.out.println("DPK KCV--->" + DPK_KVC ); //For Display & Store in DB
		
		//EDPK = sec.getEDPK(EDMK, ClearDPK);
		//System.out.println("Encrpted DPK--->" + EDPK );
		
		String checkPadssEnable ="SELECT PADSS_ENABLE FROM INSTITUTION WHERE INST_ID='"+instid+"'";

		//Store in DB
		
		String padssenable = "Y";
		int cnt=0,updatecount=0,rescardcnt=0;
		StringBuffer hcardno = null;
		String mcardno="",ecardno="",orederno="",cardtypeid="",limitid="";
		
		
		//	System.out.println("coming into for loop area");
		
			int cardcount = 0,pcpcnt=0,aicnt=0,cicnt=0,ezauthcount=0,updezauthcnt=0,updatecuim=0;
			
			int resprod = 0,rescin = 0;
			String getcardmarchdet123 = "select CARD_HOLDER_NUMBER FROM CARDMARCH WHERE customer_name!='WELCOME TO BAKHTAR BANK' AND HCARD_NO='0'";// AND PRIMARY_ACCOUNT_NUMBER ='"+accountno[i].toString()+"'";
			List Migration_data123 = jdbctemplate2.queryForList(getcardmarchdet123);
			
			Iterator cardmarchitr11 = Migration_data123.iterator();
			
			while(cardmarchitr11.hasNext())
			{
				Map map1 = (Map)cardmarchitr11.next();
				String CARD_HOLDER_NUMBER1 	= (String) map1.get("CARD_HOLDER_NUMBER");
			
			String getcardmarchdet = "select * FROM CARDMARCH WHERE customer_name!='WELCOME TO BAKHTAR BANK' AND HCARD_NO='0' AND  CARD_HOLDER_NUMBER='"+CARD_HOLDER_NUMBER1+"' ";// AND PRIMARY_ACCOUNT_NUMBER ='"+accountno[i].toString()+"'";
		//	System.out.println("geettingin into card march " + getcardmarchdet);
			
			
			
			String orderreferenceno="";
			List Migration_data = jdbctemplate2.queryForList(getcardmarchdet);
			
			
			
			int count=0;
			
			Iterator cardmarchitr = Migration_data.iterator();
			while(cardmarchitr.hasNext())
			{
				count++;
			String BIN_CODE="",CARD_HOLDER_NUMBER="",expdate="",BRANCH_CODE="",pacct="",
					s1account="",s1branch="",s1prod="",custname="",mail1="",
					mail2="",mail3="",mail4="",mobno="",email="",custid="",
							currcode="",acctype="";
						System.out.println("coming into bean area");
				
						Map map = (Map)cardmarchitr.next();
						BIN_CODE 	= (String) map.get("BIN_CODE");
						
			
			CARD_HOLDER_NUMBER = (String) map.get("CARD_HOLDER_NUMBER");
			expdate = (String) map.get("EXPIRY_DATE");
			BRANCH_CODE = (String) map.get("BRANCH_CODE");
			pacct = (String) map.get("PRIMARY_ACCOUNT_NUMBER");
			
			System.out.println("total account"+count+"for the card"+CARD_HOLDER_NUMBER+"account"+pacct);
			s1account = (String) map.get("S1_ACCOUNT_NUMBER");
			s1branch = (String) map.get("S1_BRANCH_CODE");
			s1prod = (String) map.get("S1_PRODUCT_CODE");
			custname = (String) map.get("CUSTOMER_NAME");
			mail1 = (String) map.get("MAILING_ADDRESS_1");
			
			mail2 = (String) map.get("MAILING_ADDRESS_2");
			mail3 = (String) map.get("MAILING_ADDRESS_3");
			mail4 = (String) map.get("MAILING_ADDRESS_4");
			mobno = (String) map.get("MOBILE_NUMBER");
			email = (String) map.get("E_MAIL");
			
			custid = (String) map.get("CUSTOMER_ID");
			currcode = (String) map.get("CURR_CODE");
			acctype = (String) map.get("ACCOUNT_TYPE");
			
			
				
		
				
				hcardno =sec.getHashedValue(CARD_HOLDER_NUMBER+instid);
				//ecardno =sec.getECHN(EDMK, EDPK,CARD_HOLDER_NUMBER);
				mcardno =sec.getMakedCardno( CARD_HOLDER_NUMBER);
				//primary
			
				cardtypeid = "001";//this.comProductId(instid,cardmigratebean.getBincode(),jdbctemplate2);
			//	System.out.println("ggeting card type id" +cardtypeid);
				productcode ="4137031001";// this.getProductcode(instid,cardmigratebean.getBincode(),jdbctemplate2);
				//System.out.println("getting product code list "+productcode);
				subproductCode = "00000001";//this.getProductBySubProduct(instid, productcode, jdbctemplate2);
				//System.out.println("getting sub product code "+ subproductCode);
				limitid ="2";// this.getSubProductLimitId(instid, subproductCode, jdbctemplate2);
			
				int production=0,customerinfo=0,accountinfo=0;
				String cardcheckqry1="select count(CARD_NO) as cardcnt from CARD_PRODUCTION where CARD_NO='"+ecardno+"' ";
				
				
				int getcountcard1 = jdbctemplate2.queryForInt(cardcheckqry1);
				//System.out.println("getcountcard1 "+getcountcard1);					
				
				
			if (getcountcard1 ==0 ){
			
				
				//ORDER REF NUMBER	
				
					orderreferenceno =migrationutil.generateorderRefno( instid, jdbctemplate2 );
					orederno = orderreferenceno;
				
				if(orederno == null){
						orederno = "LPAD("+orederno+","+orderrefnolength+",'0')";
				}
						StringBuilder pcp = new StringBuilder();
						pcp.append("INSERT INTO CARD_PRODUCTION  ( ");
						pcp.append("INST_ID, CARD_NO, CIN, ORDER_REF_NO, CARD_STATUS, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY, ");
						pcp.append("GENERATED_DATE, EXPIRY_DATE, PRE_DATE,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE,");
						pcp.append("APP_DATE, PIN_DATE, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT,");
						pcp.append("WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID,");
						pcp.append("BIN, AUTH_DATE, STATUS_CODE, PIN_OFFSET, OLD_PIN_OFFSET, PIN_RETRY_CNT, AUTO_ACCT_FLAG, CVV1, CVV2, ICVV, CARD_REF_NO,HCARD_NO,MCARD_NO,ACCOUNT_NO,MOBILENO,CARD_COLLECT_BRANCH)");
						pcp.append(" SELECT '"+instid+"', '"+ecardno+"' CARD_NO, CUSTOMER_ID, LPAD("+orederno+","+orderrefnolength+",'0') ORDER_REF_NO, '05',");
						pcp.append("'"+cardtypeid+"' CARD_TYPE_ID,'"+subproductCode+"' SUB_PROD_ID, '4137031011', trim(BRANCH_CODE), '', '',");
						pcp.append("SYSDATE, to_date(EXPIRY_DATE,'YYYYMMDD') EXPIRY_DATE, '', '1', SYSDATE, '', '', 'P', '500', SYSDATE, ");
						pcp.append("SYSDATE, '', '', '','', 'A', (select FEE_CODE from INSTPROD_DETAILS where '"+subproductCode+"'=SUB_PROD_ID)FEE_CODE, (select LIMIT_ID from INSTPROD_DETAILS where '"+subproductCode+"'=SUB_PROD_ID)LIMIT_ID, '', '', ");
						pcp.append("'','','','','',CUSTOMER_NAME,CUSTOMER_NAME,'','',CARD_HOLDER_NUMBER,CARD_HOLDER_NUMBER,'',BIN_CODE, '', '97', PIN_OFFSET, '', '0', '', '', '', '', '','"+hcardno+"' HCARD_NO,'"+mcardno+"' MCARD_NO,PRIMARY_ACCOUNT_NUMBER,NVL(MOBILE_NUMBER,'NA') MOBILE_NUMBER,'"+BRANCH_CODE+"' FROM CARDMARCH WHERE CARD_HOLDER_NUMBER='"+CARD_HOLDER_NUMBER+"' AND ROWNUM=1");
						//encmigrationutil.mtrace("inserting  into ifd card production " + pcp.toString());	
						pcpcnt = jdbctemplate2.update(pcp.toString());
	
						production=pcpcnt;
					}
					else{
						
						orederno = getorderrefno(instid, hcardno.toString(), jdbctemplate2);
						production=1;
						
						migrationutil.mtrace("else already production available  "+production+"\n orederno"+orederno);	
						
					}
					
					String customercheckqry="select count(CIN) as custcnt from CUSTOMERINFO WHERE CIN='"+custid+"'";
					//System.out.println("customercheckqry"+customercheckqry);
					int custcount = jdbctemplate2.queryForInt(customercheckqry);
					//System.out.println("customerCINCOUNT"+custcount);
					if(custcount == 0){
							
					 
						StringBuilder ci = new StringBuilder();
						System.out.println("instid-->"+instid+"--orderrefno-->"+orederno); 
						ci.append("INSERT INTO CUSTOMERINFO ");
						ci.append("select '"+instid+"'INST_ID, '"+orederno+"' ORDER_REF_NO, '"+custid+"' CIN, CUSTOMER_NAME FNAME, ''MNAME, ''LNAME, ");
						ci.append("NVL(BIRTH_DATE,'01-JAN-1900') DOB, '' GENDER, '' MARITAL_STATUS, '' NATIONALITY, '' DOCUMENT_PROVIDED, '' DOCUMENT_NUMBER, '' SPOUCE_NAME, ");
						ci.append("'' MOTHER_NAME, ''FATHER_NAME, MOBILE_NUMBER MOBILE, E_MAIL E_MAIL, '' P_PO_BOX, '' P_HOUSE_NO, '' P_STREET_NAME, '' P_WARD_NAME, ");
						ci.append("'' P_CITY, '' P_DISTRICT, '' P_PHONE1, '' P_PHONE2, '' C_PO_BOX, '' C_HOUSE_NO, '' C_STREET_NAME, '' C_WARD_NAME, ");
						ci.append("'' C_CITY, '' C_DISTRICT, '' C_PHONE1, '' C_PHONE2, '' MAKER_DATE, '1' MAKER_ID, ''CHECKER_DATE, ''CHECKER_ID, 'P'MKCK_STATUS, 'M'CUSTOMER_STATUS from CARDMARCH ");
						ci.append("WHERE CUSTOMER_ID='"+custid+"' and ROWNUM<='1'");
						//encmigrationutil.mtrace("insert into ifd customerinfo "+ci.toString());
						System.out.println(ci.toString());     
						
						cicnt = jdbctemplate2.update(ci.toString());
						
						customerinfo=cicnt;						
					
					
					}	
					
					else{
						migrationutil.mtrace("else already customerinfo available  "+customerinfo);
						customerinfo=1;
					}
					orederno = getorderrefno(instid, hcardno.toString(), jdbctemplate2);
					String oredernoval="";
					if(orederno!=null){
						oredernoval=orederno;	
					}
					else{
						oredernoval=orederno;
					}
					String accountcheckqry="select count(*) as cnt from ACCOUNTINFO where ACCOUNTNO='"+pacct+"'";				
					System.out.println("accountcheckqry"+accountcheckqry);
					int rescount = jdbctemplate2.queryForInt(accountcheckqry);						 
					if (rescount == 0){	
						
							StringBuilder ai = new StringBuilder();
							ai.append("INSERT INTO ACCOUNTINFO ");
							ai.append("(INST_ID, ORDER_REF_NO, CIN, ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACCT_CURRENCY, ACCOUNTNO, ACCOUNTTYPE, AVAILBAL, LEDGERBAL, ADDEDBY, ADDED_DATE )");
							ai.append("(select '"+instid+"' INST_ID,'"+oredernoval+"', '"+custid+"' CIN, ACCOUNT_TYPE ACCTTYPE_ID, ");
							ai.append("PRODUCT_CODE ACCTSUB_TYPE_ID, CURR_CODE ACCT_CURRENCY, PRIMARY_ACCOUNT_NUMBER ACCOUNTNO, '"+accounttype+"'ACCOUNTTYPE, ");
							ai.append("'0'AVAILBAL, '0'LEDGERBAL,'1'ADDEDBY, sysdate ADDED_DATE from CARDMARCH ");
							ai.append("WHERE CUSTOMER_ID='"+custid+"' and PRIMARY_ACCOUNT_NUMBER='"+pacct+"' )");
							System.out.println(ai.toString());    
							//encmigrationutil.mtrace("insert into ifdaccountinfo"+ ai.toString());
							aicnt = jdbctemplate2.update(ai.toString());
							accountinfo=aicnt;
							System.out.println("number of times "+count+"account"+pacct);						
							
					}
					else{
						accountinfo=1;
						migrationutil.mtrace("else already account available  "+accountinfo);
					}
					
					migrationutil.mtrace("getting count for account "+accountinfo);
					migrationutil.mtrace("getting count for prodcount "+production);
					migrationutil.mtrace("getting count for custcount "+customerinfo);
					
					
			int pcustcount =0,pcardninfo=0,pezaccount=0,pezauth=0,pezaccum=0;
							try {
								
									//int personalcardissue = this.personalCardIssuence(hcardno.toString(),padssenable,instid,userid,tablename,jdbctemplate2);
									
							//	     if (production > 0 && customerinfo>=0 && accountinfo >=0){
									
									
									
									//INSERTING INTO PRIMARY DETAILS
									String status_code= getSwitchCardStatus(instid, CARDACTIVATEDCODE, jdbctemplate2);
									
									String custexistqry = "SELECT COUNT(*) as cnt FROM EZCUSTOMERINFO WHERE CUSTID= '"+custid+"'";		
									int ezcustcount = jdbctemplate2.queryForInt(custexistqry);
									if (ezcustcount == 0){
										StringBuilder cinf_4 = new StringBuilder();		
												cinf_4.append("INSERT INTO EZCUSTOMERINFO ");
												cinf_4.append("(INSTID, CUSTID, NAME, DOB, SPOUSENAME, ADDRESS1, ADDRESS2, ADDRESS3, OFFPHONE, MOBILE, EMAIL, RESPHONE) ");
												cinf_4.append("VALUES ");
												cinf_4.append("('"+instid+"','"+custid+"','"+custname+"','','', ");
												cinf_4.append("'"+mail1+"','"+mail2+"','"+mail3+"','"+mail4+"','"+mobno+"','"+email+"','"+mobno+"') ");
												pcustcount =jdbctemplate2.update(cinf_4.toString());
												//encmigrationutil.mtrace("insert into ezcustinfo " +cinf_4);
									}
									
									else{
										pcustcount=1;
										migrationutil.mtrace("else already ezcustcount available  "+pcustcount);
									}
									
									String EZCARDINFOeistqry = "SELECT COUNT(*) as cnt FROM EZCARDINFO WHERE CHN= '"+hcardno+"'";		
									int ezcardinfoexist = jdbctemplate2.queryForInt(EZCARDINFOeistqry);
									if (ezcardinfoexist == 0){
												StringBuilder crdinf_3 = new StringBuilder();		
												crdinf_3.append("INSERT INTO EZCARDINFO ");
												crdinf_3.append("(INSTID, CHN, CARDTYPE, CUSTID, TXNGROUPID, LIMITFLG, EXPIRYDATE, STATUS, PINOFFSET, OLDPINOFFSET, TPINOFFSET, OLDTPINOFFSET, PINRETRYCOUNT, TPINRETRYCOUNT, PVKI, LASTTXNDATE, LASTTXNTIME,CVV, PANSEQNO) ");
												crdinf_3.append("VALUES ");
												crdinf_3.append("('"+instid+"','"+hcardno.toString()+"','"+cardtypeid+"','"+custid+"','01','"+limitid+"', to_char(to_date(lpad('"+expdate+"' ,'8','0'), 'YYYYMMDD'), 'DD-MON-YY'),'97','0','0',");
												crdinf_3.append("'0','0','0','0','0',TO_CHAR(SYSDATE,'DD-MON-YY'),'00','0' ,");
												crdinf_3.append("'00' )");
												pcardninfo = jdbctemplate2.update(crdinf_3.toString());											
												//encmigrationutil.mtrace("insert into cardinfo "+ crdinf_3);
									}											
								//2	
									else{
										pcardninfo=1;
										migrationutil.mtrace("else already ezcardinfoexist available  "+pcardninfo);
									}
									 
									
												  
								//3	
											 String acctexistqry = "SELECT COUNT(*) as cnt FROM EZACCOUNTINFO WHERE ACCOUNTNO= '"+pacct+"'";		
												int acctexist = jdbctemplate2.queryForInt(acctexistqry);	
												if(acctexist==0){
												StringBuilder ezac_2 = new StringBuilder();		
												ezac_2.append("INSERT INTO EZACCOUNTINFO ");
												ezac_2.append("(INSTID, ACCOUNTNO, ACCOUNTTYPE, CURRCODE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE) ");
												ezac_2.append("VALUES ");
												ezac_2.append("('"+instid+"','"+pacct+"','"+acctype+"','"+currcode+"','0','0','','"+status_code+"',");
												ezac_2.append("'01',TO_CHAR(SYSDATE,'DD-MON-YY'),TO_CHAR(SYSDATE,'HH24MISS'),'"+BRANCH_CODE+"','"+subproductCode+"' )");	
												//encmigrationutil.mtrace("ezac_2::::"+ezac_2.toString());	
												pezaccount = jdbctemplate2.update(ezac_2.toString());
													
												
												
												
												}
												else{
													pezaccount=1;
													//migrationutil.mtrace("else already acctexistqry available  "+pezaccount);
												}
													      
								
																	
													
								//4	
													
													
								//System.out.println("coming into ezauthrel");
								String authrelcountqry="select count(ACCOUNTNO) as cnt from EZAUTHREL where chn='"+hcardno+"'";	
								ezauthcount = jdbctemplate2.queryForInt(authrelcountqry);
								//System.out.println("coming into count"+ezauthcount);
							/*	String authrelcountqry123="select count(ACCOUNTNO) as cnt from EZAUTHREL where chn='"+hcardno+"'";	
								int ezauthcount123 = jdbctemplate2.queryForInt(authrelcountqry123);
*/
								if(ezauthcount ==0 ){
									
									StringBuilder authrel_1 = new StringBuilder();	
									authrel_1.append("INSERT INTO EZAUTHREL ");
									authrel_1.append("(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) ");
									authrel_1.append("VALUES ");
									authrel_1.append("('"+instid+"','"+hcardno.toString()+"','"+pacct+"','"+acctype+"','P','1','"+currcode+"') ");
							       // encmigrationutil.mtrace("authrel_1::::"+authrel_1.toString());
									pezauth = jdbctemplate2.update(authrel_1.toString());
									
								}else{
									
									

									StringBuilder authrel_1 = new StringBuilder();	
									authrel_1.append("INSERT INTO EZAUTHREL ");
									authrel_1.append("(INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE) ");
									authrel_1.append("VALUES ");
									authrel_1.append("('"+instid+"','"+hcardno.toString()+"','"+pacct+"','"+acctype+"','S','1','"+currcode+"') ");
							       // encmigrationutil.mtrace("authrel_1::::"+authrel_1.toString());
									pezauth = jdbctemplate2.update(authrel_1.toString());
									
									
								}
								
								
								String INSTID_5="", LIMITTYPE_5="", LIMITID_5="", TXNCODE_5="", CURRCODE_5="", AMOUNT_5="", COUNT_5="", WAMOUNT_5="", WCOUNT_5="", MAMOUNT_5="", MCOUNT_5="", YAMOUNT_5="", YCOUNT_5="", LIMITDATE_5="";
								
								StringBuilder accinfo_5 = new StringBuilder();
								accinfo_5.append("SELECT ");
								//--EZACCUMINFO start
								//--INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, LIMITDATE
								accinfo_5.append("INSTID INSTID_5, CASE  LIMITTYPE ");
								accinfo_5.append("WHEN 'CDTP'  THEN 'CARD' ");  
								accinfo_5.append("WHEN 'CARD' THEN 'CARD' "); 
								accinfo_5.append("WHEN 'ACTP'  THEN 'ACCT' ");  
								accinfo_5.append("WHEN 'ACCT' THEN 'ACCT' END LIMITTYPE_5 ,"); 
								accinfo_5.append("CASE  LIMITTYPE "); 
								if(padssenable.equals("Y")){
								accinfo_5.append(" WHEN 'CDTP'  THEN '"+hcardno.toString()+"'");      
								accinfo_5.append(" WHEN 'CARD' THEN '"+hcardno.toString()+"' "); 
								}
								//wait i wil come
								{
								}
								accinfo_5.append(" WHEN 'ACTP'  THEN '"+pacct+"' ");  
								accinfo_5.append(" WHEN 'ACCT' THEN '"+pacct+"' END LIMITID_5 ,");  
								accinfo_5.append(" TXNCODE TXNCODE_5, CURRCODE CURRCODE_5, AMOUNT AMOUNT_5, COUNT COUNT_5, "); 
								accinfo_5.append("WAMOUNT WAMOUNT_5, WCOUNT WCOUNT_5, MAMOUNT MAMOUNT_5, MCOUNT MCOUNT_5, YAMOUNT YAMOUNT_5, YCOUNT YCOUNT_5, ");
								accinfo_5.append("(select TO_CHAR(AUTH_DATE,'DD-MON-YYYY') from LIMIT_DESC where INSTID='"+instid+"' AND LIMIT_ID='1') LIMITDATE_5 FROM EZLIMITINFO ");
								accinfo_5.append("WHERE INSTID='"+instid+"' AND LIMIT_RECID = '"+limitid+"'");
								//--EZACCUMINFO end  
								//encmigrationutil.mtrace("accinfo_5:::::"+accinfo_5.toString());
								
								
								
								
								List movetoAccuminfo= jdbctemplate2.queryForList(accinfo_5.toString());
								migrationutil.mtrace("movetoAccuminfo:::::::::::"+movetoAccuminfo.size()+":::::::"+movetoAccuminfo);   
								Iterator accitr = movetoAccuminfo.iterator();
								int as =0; int incCount = 0;
								while(accitr.hasNext())
								{
									////System.out.println("testing :::::::::::::::::::::::1"+as++);
									incCount = incCount+1;    
									
									Map mp2 = (Map)accitr.next();  
									INSTID_5   = mp2.get("INSTID_5").toString();
									////System.out.println("testing :::::::::::::::::::::::2"+as++);
									LIMITTYPE_5   = mp2.get("LIMITTYPE_5").toString();
									////System.out.println("testing :::::::::::::::::::::::3"+as++);
									LIMITID_5   = mp2.get("LIMITID_5").toString();
									////System.out.println("testing :::::::::::::::::::::::4"+mp2.get("LIMITID_5").toString());
									TXNCODE_5= mp2.get("TXNCODE_5").toString();;
									////System.out.println("testing :::::::::::TXNCODE_5::::::::::::4"+mp2.get("TXNCODE_5").toString());
								
									CURRCODE_5   = mp2.get("CURRCODE_5").toString();;
								////System.out.println("testing :::::::::::currr::::::::::"+mp2.get("CURRCODE_5").toString());
								AMOUNT_5   = mp2.get("AMOUNT_5").toString();;
								////System.out.println("testing :::::::::::::::::::::::"+as++);
								COUNT_5   = mp2.get("COUNT_5").toString();
								////System.out.println("testing :::::::::::::::::::::::"+as++);
								WAMOUNT_5   = mp2.get("WAMOUNT_5").toString();
								////System.out.println("testing :::::::::::::::::::::::"+as++);
								WCOUNT_5   = mp2.get("WCOUNT_5").toString();
								////System.out.println("testing :::::::::::::::::::::::"+as++);
								MAMOUNT_5   = mp2.get("MAMOUNT_5").toString();
								////System.out.println("testing :::::::::::::::::::::::"+as++);
								MCOUNT_5   = mp2.get("MCOUNT_5").toString();
								////System.out.println("testing :::::::::::::::::::::::"+as++);
								YAMOUNT_5   = mp2.get("YAMOUNT_5").toString();
								YCOUNT_5   = mp2.get("YCOUNT_5").toString();
								////System.out.println("testing :::::::::::::::::::::::"+as++);
								LIMITDATE_5   = mp2.get("LIMITDATE_5").toString();
								////System.out.println("testing :::::::::::::::::::::::"+as++);
						    
								
										
								
								//String getres_5Query = getres_5Query(instid, LIMITTYPE_5, LIMITID_5, TXNCODE_5, CURRCODE_5, AMOUNT_5, COUNT_5, WAMOUNT_5, WCOUNT_5, MAMOUNT_5, MCOUNT_5, YAMOUNT_5, YCOUNT_5, LIMITDATE_5);
								System.out.println("coming into updatecuim");
								String cumimcountqry="select count(ACCOUNTNO) as cnt from EZAUTHREL where chn='"+hcardno+"'";
								updatecuim = jdbctemplate2.queryForInt(cumimcountqry);
								System.out.println("coming into count"+updatecuim);
								if(updatecuim==0){
									StringBuilder AccinfiInsert = new StringBuilder();
									AccinfiInsert.append("INSERT INTO EZACCUMINFO ");
									AccinfiInsert.append("(INSTID, LIMITTYPE, LIMITID, TXNCODE, CURRCODE, AMOUNT, COUNT, WAMOUNT, WCOUNT, MAMOUNT, MCOUNT, YAMOUNT, YCOUNT, LIMITDATE) ");
									AccinfiInsert.append("VALUES ");
									AccinfiInsert.append("('"+INSTID_5+"','"+LIMITTYPE_5+"','"+LIMITID_5+"','"+TXNCODE_5+"','"+CURRCODE_5+"','0',");
									AccinfiInsert.append("'0','0','0','0','0','0','0','"+LIMITDATE_5+"' )");
									//encmigrationutil.mtrace("accum info::"+AccinfiInsert);  
									 
									pezaccum = jdbctemplate2.update(AccinfiInsert.toString());
									//AccinfiInsert = "";
								}
								else{
									pezaccum=1;
									migrationutil.mtrace("else already pezaccum available  "+pezaccount);
								}
									
								} 
								migrationutil.mtrace("getting count for pcustcount "+pcustcount);
								migrationutil.mtrace("getting count for pcardninfo "+pcardninfo);
								migrationutil.mtrace("getting count for pezaccount "+pezaccount);
								migrationutil.mtrace("getting count for pezauth "+pezauth);
								migrationutil.mtrace("getting count for pezaccum "+pezaccum);
								
								if (pcustcount >=0 && pcardninfo >=0 && pezaccount >0 && pezauth >0 && pezaccum >=0){
									
									String hashcard="UPDATE CARDMARCH SET HCARD_NO='"+hcardno.toString()+"' WHERE CARD_HOLDER_NUMBER='"+CARD_HOLDER_NUMBER+"'";
									int hashupdate  = jdbctemplate2.update(hashcard);
									
									migrationutil.mtrace("success acc"+pacct);	

									// System.out.println("success accoun"+pacct);
									/*String cinorder_sequpdate = "UPDATE SEQUENCE_MASTER SET ORDER_REFNO=ORDER_REFNO+1 WHERE INST_ID='"+instid+"'";
									encmigrationutil.mtrace("cinorder_sequpdate :  "+cinorder_sequpdate);
									int orderupdate = jdbctemplate2.update(cinorder_sequpdate);
									if (orderupdate > 0 ){*/
										updatecount = updatecount + 1;									
									//}
								 }else{
										migrationutil.mtrace("failure cards"+pacct);	

									// System.out.println("failure cards"+pacct);
								
									//txManager.rollback(transact.status);
									
								 }
								
									
													
																						
									//ended secondary
					
							}
					
							catch(Exception e){
								
								e.printStackTrace();			
							}
			
				
					
							
				try{
					System.out.println("FINAL UPDATED COUNT "+updatecount);
					if(updatecount > 0){
						//txManager.commit(transact.status);
						//addActionMessage("Card Succesfully Migrated For "+updatecount);
						
					}else{
						//addActionError("Unable to process");
					//	txManager.rollback(transact.status);				
					}
				}catch(Exception e){
					e.printStackTrace();		
				}
				
	}
			}
	

	}
	public static String getorderrefno(String instid, String chn, JdbcTemplate jdbctemplate) {
		String order_qry ="SELECT ORDER_REF_NO FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND HCARD_NO='"+chn+"' AND ROWNUM <=1" ;
		//migrationutil.mtrace("getorderrefno"+order_qry);
		String orderno = null;
		try {
			orderno=(String)jdbctemplate.queryForObject(order_qry,String.class);
			//migrationutil.mtrace("getorderrefno" + orderno );
			 
		} catch (Exception e) { 
			orderno="";
		} 
		return  orderno;  
	}
	

	public static String getSwitchCardStatus(String instid, String cardstatus, JdbcTemplate jdbctemplate){
		String switchcardstatus = null;
		try {
			String statusqry ="SELECT STATUS FROM MAINTAIN_DESC WHERE INST_ID='"+instid+"' AND card_act_code='"+cardstatus+"' AND ROWNUM<=1";
			//enctrace("getSwitchCardStatus"+statusqry);
			switchcardstatus = (String)jdbctemplate.queryForObject(statusqry, String.class);
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return switchcardstatus;
	}
	}
