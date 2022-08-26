package connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.expression.spel.generated.SpringExpressionsParser.startNode_return;

import com.ifg.Config.padss.PadssSecurity;

public class MigrationForRepin {

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
		StringBuilder sc = new StringBuilder();
		//sc.append("select ORG_CHN,HCARD_NO,CARD_NO,MCARD_NO,ACCOUNT_NO FROM CARD_PRODUCTION WHERE  HCARD_NO in ('7f40afada402cdae2b794c927cb5491f1f05238924534f7398084c3eca2ba33018bb115f838a698ca27c8c40b97b3b34e5dc2080a8fdad2ed39ebd238eb1feb4','f5c13af4672515c09235c5dc0b70c14b02eb4a2c0174d736cce989fa4b18dc46a156f67b00704b053d9298299884eb267834f949a29303c4d8ca49e61ed1f211','b2a73115bc6aa4d9af495b3644d4bf1bee0fd94cc1412b13b141ad0d1d8eee112d404b2392b2cf22959eb153fb3a547f62ebee25467a75a216b2b61b12811d68','af8ae5b30aebf87f090511891af97d287ba2a3825fcfd75089e155ff45137cfb4b93dce075d89ff48a6575e253d71fefbf95df1282a99f0c834f6e9455b8e93d','fb8613de7bcc4d4312ca1050eea017667b6b89f14384d44a98b2f90ee1be594fec4cd2319b7590eb2eb686a987604de9435792b882077cdb0b5e929913b7fa55','c6b3833159631abfd85770ab00d6102a28a72a6237ad6838591cb260402cc92092490808c91a02fe6b6f642e9923d40ceeb5a544fa77c18234ea50817587b912','58db8c1cd6c1111a155981a184dde52e3f61ce5f53dd45d86af6b3aa5ae50c825199e54aae24e04ddb3a971e901364bcf32fdb31ae2169a3dd935eb463abd32a','15fad3266f2f0f1425b08c1442724e0ce39153e15997c5c8718f9feffd8e164709b626a9226fcfa355dd2b7928bcb598124988402a21d85ec1d682d4ff03138b','e29545ab14a129832afb8d1de06578080f49e937942c2259c50bc549ece48f1b349a199e7273518805d634ddff505229c77ab7335e6b2abaa2e456922fe1eac9','1b97615ba0eba693b3c8c07f3b537660226971ab556d382b5f68b84413d95dc047296e66089f9b4eb9d95ace8d4d6e13575bab4597a598745b5c7afe5432a888','6a3a3bd2eb49872a700de312e1a663a288830126c532cadb7078aa765ea60a0819f3b7a8f92ca15e4646430e495d0a33cc9b5fdf594dab48df19b898713eff62','d20c3ccd129eb846d7cac1fb5e894b0829334376a1eb0f912140310e8958bf5e17a71caad36cbd049108440586cfcc1f10271994d198c9c8d77c1622447ecc99','82010a2d876d0bf1fb272960a5b8bc61a74c04450cea57399cd013ef008b909a30dbb36a7ac6dd541cd1f5b5a436cd78a23cc0865b40b846a10f60f7b9a8d3ca','ccb5a13e671206f4d2bf0db74bedc8bdba54f9425eaa8e1c41ddffa809005f655e927e3a437ae5ffb4fc031b6e64cd045063add16285e754a177fd3f108d5ade','6a36d5bd5126db2c07e902e43fdadcc4b8fbd05a287d767c3db3c7f5637d8ead6c3d736402ecc72b3150385457b5e2a2b99d6b8a4f6fac2a1a619209344369c2','12d5dc3180560fd803017f0322bb349fec6a18b83504d08984b2e5e811fefc468c3ef22e9244e90e0199f2d20fcf1ad62887c0ee84d4716b4361c05a739d0e71')");
		sc.append("select ORG_CHN,HCARD_NO,CARD_NO,MCARD_NO,ACCOUNT_NO FROM CARD_PRODUCTION WHERE  ORG_CHN='4322810022018845'");
				System.out.println(sc.toString());
		ResultSet rrs = rst.executeQuery(sc.toString());
		String org_chn="",hcard_no="",newcardno="",newmaskcardno="",accountno ="",instid="ORBL";
		int pcpcnt = 0;int aicnt = 0;int cicnt = 0,totalcount=0;
		while(rrs.next())    
		{
			totalcount = totalcount +1;
			org_chn = rrs.getString("ORG_CHN");
			hcard_no = rrs.getString("HCARD_NO");
			newcardno = rrs.getString("CARD_NO");
			newmaskcardno = rrs.getString("MCARD_NO");
			accountno = rrs.getString("ACCOUNT_NO");
			//String NEW_HCARD_INSERT="UPDATE CARD_PRODUCTION SET NEW_HCARD='"+sec.getHashedValue(org_chn+"ORBL")+"' WHERE HCARD_NO='"+hcard_no+"'";
			
			String repinquery="";
			
			repinquery = "INSERT INTO  PERS_CARD_PROCESS (";
			repinquery += "INST_ID, CARD_NO,HCARD_NO, MCARD_NO,ACCT_NO,ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY, CIN, ORDER_REF_NO, CARD_STATUS, CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY, ";
			repinquery += "GENERATED_DATE, EXPIRY_DATE, PRE_DATE,MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, MKCK_STATUS, SERVICE_CODE, REG_DATE,";
			repinquery += "APP_DATE, PIN_DATE, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, CAF_REC_STATUS, FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT,";
			repinquery += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, ORG_CHN, USED_CHN, COURIER_ID,";
			repinquery += "BIN, AUTH_DATE, STATUS_CODE,MOBILENO,PANSEQ_NO)";
			
			repinquery += " SELECT INST_ID, '"+newcardno+"','"+hcard_no+"','"+newmaskcardno+"','"+accountno+"',ACCTTYPE_ID,ACCTSUB_TYPE_ID,ACC_CCY, CIN, ORDER_REF_NO, '01', CARD_TYPE_ID, SUB_PROD_ID, PRODUCT_CODE, BRANCH_CODE, PC_FLAG, CARD_CCY,";
			repinquery += "GENERATED_DATE, EXPIRY_DATE, PRE_DATE, MAKER_ID, MAKER_DATE, CHECKER_ID, CHECKER_DATE, 'M', SERVICE_CODE, REG_DATE, ";
			repinquery += "APP_DATE, sysdate, RECV_DATE, ISSUE_DATE, ACTIVE_DATE, 'R', FEE_CODE, LIMIT_ID, PRIVILEGE_CODE, WDL_AMT, ";
			repinquery += "WDL_CNT, PUR_AMT, PUR_CNT, ECOM_AMT, ECOM_CNT, EMB_NAME, ENC_NAME, APP_NO, ORDER_FLAG, '"+org_chn+"', USED_CHN, COURIER_ID, ";
			repinquery += "BIN, AUTH_DATE, STATUS_CODE,MOBILENO,'01' FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND HCARD_NO='"+hcard_no+"'";
			
			
			System.out.println("-"+repinquery);
			pcpcnt = rst2.executeUpdate(repinquery);
			
			//pcpcnt= pcpcnt+pcpcnt;
		}
		System.out.println("totaalcount--"+totalcount);
		if(totalcount==1)
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
