package connection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.expression.spel.generated.SpringExpressionsParser.startNode_return;

import com.ifg.Config.padss.PadssSecurity;

public class DebitMigrationDataNew050719 {

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
		StringBuilder sc = new StringBuilder();
		sc.append("select CHN FROM ACCT_DATA_FILE");
				System.out.println(sc.toString());
		ResultSet rrs = rst.executeQuery(sc.toString());
		String org_chn="",hcard_no="";
		int pcpcnt = 0;int aicnt = 0;int cicnt = 0,totalcount=0,swpcnt=0;
		while(rrs.next())    
		{
			totalcount = totalcount +1;			
			hcard_no = rrs.getString("CHN");
			String CARDMAN_INSERT="UPDATE ACCT_DATA_FILE SET HCARD_NO='"+sec.getHashedValue(hcard_no+"AZIZI")+"' WHERE CHN='"+ rrs.getString("CHN")+"'";			
			System.out.println("-"+CARDMAN_INSERT);
			
			pcpcnt = rst2.executeUpdate(CARDMAN_INSERT);			
			
			pcpcnt= pcpcnt+1;
			con1.commit();
		}				
		rrs.close();
		rst.close();
		con1.close();

	}

}
