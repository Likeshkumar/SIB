package connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.expression.spel.generated.SpringExpressionsParser.startNode_return;

import com.ifg.Config.padss.PadssSecurity;

public class DebitMigrationDataSec050719 {

	/**
	 * @param args
	 * @throws Exception 
	 */
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
		Statement rst7= con1.createStatement();
		Statement rst8= con1.createStatement();
		StringBuilder sc = new StringBuilder();
		sc.append("select ACCTNO as S1_ACCOUNT_NUMBER ,CHN as card_holder_number,substr(ACCTNO,1,4) as s1_branch_code,acct_type,curr_code from MISSEDDATA  where BIN='501817' and ACCTNO is not null "); 				
				/*" and SUBSTR(S1_ACCOUNT_NUMBER,7,2)!='90'" +
				" and SUBSTR(S1_ACCOUNT_NUMBER,7,2)!='50'" );*/
				System.out.println(sc.toString());
		ResultSet rrs = rst.executeQuery(sc.toString());
		StringBuffer hcard_no =new StringBuffer();
		String instid="AZIZI",accountcurrency="", s1accno="",card_no="",s1brcode="",orderrefno="",cin="",limitid="",statuscode="",accflag="",accttype="",acctsubtype="",limittype="";
		int pcpcnt = 0;int aicnt = 0;int cicnt = 0,totalcount=0;
		try{
			while(rrs.next())    
			{
				totalcount = totalcount +1;
				s1accno = rrs.getString("S1_ACCOUNT_NUMBER");
				card_no = rrs.getString("card_holder_number");
				s1brcode = rrs.getString("s1_branch_code");
				hcard_no = sec.getHashedValue(card_no+"AZIZI");
				accountcurrency=rrs.getString("curr_code");
				accttype=rrs.getString("acct_type");
				String dtlsqry="SELECT ORDER_REF_NO,CIN,LIMIT_ID,STATUS_CODE FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND HCARD_NO='"+hcard_no+"' AND ROWNUM<=1";
				System.out.println("-"+dtlsqry);
				ResultSet rrs1 = rst2.executeQuery(dtlsqry);
				while(rrs1.next())    
				{
					orderrefno = rrs1.getString("ORDER_REF_NO");
					cin = rrs1.getString("CIN");
					limitid = rrs1.getString("LIMIT_ID");
					statuscode = rrs1.getString("STATUS_CODE");
				}
				/*String accountsubtype=s1accno.substring(6,8);
				System.out.println("accountsubtype-"+accountsubtype);
				String acctypeqry="SELECT ACCTTYPEID FROM ACCTSUBTYPE WHERE ACCTSUBTYPEID='"+accountsubtype+"' AND ROWNUM<=1";
				System.out.println("-"+acctypeqry);
				ResultSet rrs2 = rst3.executeQuery(acctypeqry);
				while(rrs2.next())    
				{
					accttype = rrs2.getString("ACCTTYPEID");
				}*/
				
				String limitflagqry="SELECT LIMITTYPE FROM  EZLIMITINFO WHERE INSTID='"+instid+"' AND LIMIT_RECID = '"+limitid+"' and rownum<=1";
				System.out.println("-"+limitflagqry);
				ResultSet rrs3 = rst4.executeQuery(limitflagqry);
				while(rrs3.next())    
				{
					limittype = rrs3.getString("LIMITTYPE");
				}
				
				String accflagqry="select ACCT_FLAG from GLOBAL_CARDDETAILS where LIMIT_TYPE in ( select LIMITTYPE from LIMITINFO where limit_recid='"+limitid+"' and INSTID='"+instid+"' and rownum<=1)";
				System.out.println("-"+accflagqry);
				ResultSet rrs4 = rst5.executeQuery(accflagqry);
				while(rrs4.next())    
				{
					accflag = rrs4.getString("ACCT_FLAG");
				}
				
				String ifdcountqry="select count(ACCOUNTNO) as cnt from ACCOUNTINFO where ACCOUNTNO='"+s1accno+"'";
				System.out.println("-"+ifdcountqry);
				ResultSet rrs5 = rst6.executeQuery(ifdcountqry);
				while(rrs5.next())    
				{
					int count = rrs5.getInt("cnt");
					if(count==0){
						String insertcustdata = " INSERT INTO ACCOUNTINFO (" +
						 		"INST_ID, ORDER_REF_NO, CIN, ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACCT_CURRENCY, ACCOUNTNO, ACCOUNTTYPE, ADDEDBY,ADDED_DATE ) VALUES ( " +
						 		"'"+instid+"', '"+orderrefno+"','"+cin+"' ,'"+accttype+"', '','"+accountcurrency+"','"+s1accno+"','"+limittype+"','1',sysdate) ";
						
						System.out.println("-"+insertcustdata);
						pcpcnt = rst2.executeUpdate(insertcustdata);
					}
				}
				
				String ezcountqry="select count(ACCOUNTNO) as cnt from EZACCOUNTINFO where ACCOUNTNO='"+s1accno+"'";
				System.out.println("-"+ezcountqry);
				ResultSet rrs6 = rst7.executeQuery(ezcountqry);
				while(rrs6.next())    
				{
					int count = rrs6.getInt("cnt");
					if(count==0){
						String insertcustdata = " INSERT INTO EZACCOUNTINFO (" +
						 		"INSTID, ACCOUNTNO, ACCOUNTTYPE, CURRCODE, AVAILBAL, LEDGERBAL, LIMITFLAG, STATUS, TXNGROUPID, LASTTXNDATE, LASTTXNTIME, BRANCHCODE, PRODUCTCODE ) VALUES ( " +
						 		"'"+instid+"', '"+s1accno+"','"+accttype+"' ,'"+accountcurrency+"','0','0', '"+accflag+"','"+statuscode+"','01',sysdate,TO_CHAR(SYSDATE,'HH24MISS'),'"+s1brcode+"','') ";
						
						System.out.println("-"+insertcustdata);
						pcpcnt = rst2.executeUpdate(insertcustdata);
					}
				}
				
				String authrelcountqry="select count(ACCOUNTNO) as cnt from EZAUTHREL where ACCOUNTNO='"+s1accno+"' and chn='"+sec.getHashedValue(card_no+"AZIZI")+"'";
				System.out.println("-"+authrelcountqry);
				ResultSet rrs7 = rst8.executeQuery(authrelcountqry);
				while(rrs7.next())    
				{
					int count = rrs7.getInt("cnt");
					if(count==0){
						 String insertcustdata = " INSERT INTO EZAUTHREL (" +
							 		"INSTID, CHN, ACCOUNTNO, ACCOUNTTYPE, ACCOUNTFLAG, ACCOUNTPRIORITY, CURRCODE ) VALUES ( " +
							 		"'"+instid+"', '"+sec.getHashedValue(card_no+"AZIZI")+"','"+s1accno+"' ,'"+accttype+"','S','2','"+accountcurrency+"') ";
						System.out.println("-"+insertcustdata);
						pcpcnt = rst2.executeUpdate(insertcustdata);
					}
				}
				 
			}
		}
		catch (Exception e) {
			con1.rollback();
			e.printStackTrace();
		}
		System.out.println("-"+pcpcnt+"-"+aicnt+"-"+cicnt);
		System.out.println("totaalcount--"+totalcount);
		if(totalcount >0)
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
