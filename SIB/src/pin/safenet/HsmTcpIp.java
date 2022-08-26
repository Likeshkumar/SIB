package pin.safenet;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream; 
import java.net.Socket;
import java.util.Map; 
import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate; 
import com.ifp.Action.BaseAction; 
import com.ifp.instant.HSMParameter;
import com.ifp.util.CommonDesc;
 


public class HsmTcpIp extends BaseAction{ 
	 
	
	public Socket ConnectingHSM(String IPaddress,int port,int hsmTimeout){
		Socket  socket=null;
		try {
			socket = new Socket(IPaddress,port);
			trace("Result from socket----> "+socket);
		}
		catch (Exception e)		{
			trace(" connection is 2  " + socket );
			trace("Socket Creation Excetiopjk-=========> "+e.getMessage());
			return socket;
		} 
		trace(" Socket Is Returns is ======####====> "+socket);
		return socket;
	}
	
	
	public String sendToHSMForPrintPin( ByteArrayOutputStream message, EHsm ehsm, DataOutputStream out, DataInputStream in    ) throws Exception{
		
		
		trace("Message : " + message + ":  out " + out + " : in " + in );
		trace("Got ByteArrayoutputstream... " + new String ( Hex.encodeHex( message.toByteArray() )  ) );
		
		ByteArrayOutputStream hsmmsg = new ByteArrayOutputStream(); 
	 
		trace("SOH Writing...");
		hsmmsg.write(  EHsm.ascii_to_bcd("01", 1) ); // #OH
		
		trace("Sequence Number Writing...");
		hsmmsg.write(  EHsm.ascii_to_bcd("01", 1) ); //Sequence Number
		String HsmHeader = "0001"; //hsm header
		
		
		hsmmsg.write(  EHsm.ascii_to_bcd(HsmHeader, 2) );
		hsmmsg.write(  EHsm.ascii_to_bcd("00", 1) );
		
		String msglen = hexa(  message.size() );
		trace("Message lenght is [ " +message.size()+ " ] and hexa value is " + msglen ); 
		
		hsmmsg.write(    EHsm.ascii_to_bcd( msglen , msglen.length()/2 )  );
		hsmmsg.write( message.toByteArray() );
		
		trace("----request begin------");
		trace( new String ( Hex.encodeHex( hsmmsg.toByteArray() ) ) );
		trace("----request end ------"); 
		out.write( hsmmsg.toByteArray() );
		out.flush();
		
		byte[] msgheader = new byte[6];
		
		
				
		in.read(msgheader);
		
		trace("----response begin ------");
		trace( "msgheader :" + new String ( Hex.encodeHex( msgheader )  ) );
		String headerdata =  new String ( Hex.encodeHex( msgheader )  );
		String headerlength = headerdata.substring(headerdata.length()-4, headerdata.length() ) ; 
		int headerlen = Integer.parseInt(headerlength, 16);   
		byte[] response = new byte[headerlen];
		in.read(response);	 
		trace( "message :" + new String ( Hex.encodeHex( response )  ) );
		trace("----response end------"); 
		return new String ( Hex.encodeHex( response )  );
	}
	
	
	public   Socket connectingHSM(String IPaddress,int port,int hsmTimeout) throws Exception {
		Socket  socket=null;
		try {
			socket = new Socket(IPaddress,port);
			trace("Result from socket----> "+socket);
		} catch (Exception e) {
			trace(" connection is 2  " + socket );
			trace("Socket Creation Excetiopjk-=========> "+e.getMessage());
			return socket;
		}
		
		trace(" Socket Is Returns is ======####====> "+socket);
		return socket;
	}
	
	
	public JSONObject decomposeKeys( String command, String data, JSONObject jsonresp ) {
		//ee0400  00  01  10 1315cf2ec2c570bf057fc4a28c94c8fe   11 11 0af763b8fdf927f387fcc9d8074ed3af6a  3516
		String respmsg = ""; 
		if ( data == null || data.length() <= 0 ){
			jsonresp.put("RESPCODE", 1);
			jsonresp.put("REASON", "Message Not Received from HSM" );
			return jsonresp;
		} 
	    String[] resp1 = data.toUpperCase().split(command);//EE0E06
	    
		String responsecode = resp1[1].substring(0, 2);
		if( responsecode.trim().equals("00")){
			String keycount = resp1[1].substring(0, 2);			 
		 	trace("data : " + data.substring(data.length()-4, data.length()) ); 
		 	trace("commond : " + data.substring(0, 6));
		 	trace("resp : " + data.substring(6, 8));
		 	trace("keycount : " + data.substring(8, 10));
		 	trace("keylength : " + Integer.parseInt(data.substring(10, 12) , 16) );
			int keylen = Integer.parseInt(data.substring(10, 12) , 16) ;
			int keyendposition = 12+(keylen*2);
			String SESSIONKEY_TMK = data.substring(12, keyendposition );
			trace("SESSIONKEY_LMK: " + SESSIONKEY_TMK  );
			jsonresp.put("SESSIONKEY_TMK", SESSIONKEY_TMK );
			int index1pos = keyendposition+2;
			trace("index 1 : " + data.substring(keyendposition, index1pos ));
			int index2pos = index1pos+2;
			trace("index 1 : " + data.substring(index1pos, index2pos ));
			int key2endpos  = index2pos+(keylen*2);
			
			String SESSIONKEY_LMK = data.substring(index2pos, key2endpos );
			trace("SESSIONKEY_LMK : " + SESSIONKEY_LMK );
			jsonresp.put("SESSIONKEY_LMK", SESSIONKEY_LMK );
			
			int checkdigitpos  = key2endpos+6;
			trace("checkdigit : " + data.substring(key2endpos, checkdigitpos ));	
			
			jsonresp.put("RESPCODE", 0);
			
		}else{
			jsonresp.put("RESPCODE", 1);
			respmsg  = this.getHSMResponse(responsecode);
			jsonresp.put("REASON", "Message Not Received from HSM" );
		}
		
		return jsonresp;
		
	}
	
	public String deComposePinMailer( String command, String msg ) {
		if ( msg.length() <= 0 ){
			return "Message Not Received from HSM" ;
		} 
		String pinoffset = "";
		trace("command"+command);
		trace("msg"+msg);
	    String[] resp1 = msg.toUpperCase().split(command);//EE0E06
	    
		String responsecode = resp1[1].substring(0, 2);
		
		trace("Command : " + resp1[1] );
		
		trace("responsecode : " + responsecode );
		String respmsg = this.getHSMResponse(responsecode);
		if( respmsg.equals("00") ){
			pinoffset = resp1[1].substring(2,6)+"~0";
		}else{
			pinoffset = respmsg+"~1";  
		}
		return pinoffset;
	}
	
	
	public String getHSMResponse(String responsecode ){
		String respmsg = responsecode;
		if( responsecode.trim().equals("00")){
			respmsg = "00" ; 
		}else if( responsecode.trim().equals("01")){
			respmsg = "DES_FAULT";
		}else if( responsecode.trim().equals("02")){
			respmsg = "PIN MAILER NOT ENABLED";
		}else if( responsecode.trim().equals("03")){
			respmsg = "INCORRECT MESSAGE LENGTH";
		}else if( responsecode.trim().equals("04")){
			respmsg = "INVALID DATA IN MESSAGE";
		}else if( responsecode.trim().equals("05")){
			respmsg = "INVALID_PIN_FORMAT_SPECIFIER";
		}else if( responsecode.trim().equals("06")){
			respmsg = "PIN_FORMAT_ERROR";
		}else if( responsecode.trim().equals("07")){
			respmsg = "VERIFICATION FAILURE";
		}else if( responsecode.trim().equals("08")){
			respmsg = "DES_FAULT";
		}else if( responsecode.trim().equals("09")){
			respmsg = "CONTENTS OF KEY MEMORY DESTROYED";
		}else if( responsecode.trim().equals("21")){
			respmsg = "Unsupported key specifier";
		}else if( responsecode.trim().equals("22")){
			respmsg = "Invalid key specifier content";
		}else if( responsecode.trim().equals("23")){
			respmsg = "Invalid key specifier formatr";
		}else if( responsecode.trim().equals("21")){
			respmsg = "Unsupported key specifier";
		}else if( responsecode.trim().equals("90")){
			respmsg = "GENERAL PRINTER ERROR";
		}  
		
		trace("respreason : " + respmsg );
		return respmsg;
	}
	 
	//Safenet pin generation
	
	public String printPin( String instid, String cardno, String cin, String productcode,  String bin, String gentype, DataInputStream in, DataOutputStream out, JdbcTemplate jdbctemplate , HSMParameter hsmobj ) throws Exception {
		
		String offsetresp = null;
		CommonDesc commondesc = new CommonDesc();  
		String pinmailerid = hsmobj.PINMAILER_ID;
	String pvkvalue  = hsmobj.PVK;
		String hsmid  = hsmobj.HSM_ID;
		trace("Hsm id ["+hsmid+"] PVK ["+pvkvalue+"] PINMAILERID ["+pinmailerid+"]" ); 
		trace("=================================================================="); 
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		//String similuatorcommand = "EE0604";
		String hsmcommand = "EE0E06";
		String command = hsmcommand;
		try{  
			trace("Getting product code ..got : " + productcode );
			String subproduct = commondesc.getSubProductByCHNProcess(instid, cardno, gentype, jdbctemplate) ;
			trace("Getting sub-product code ..got : " + subproduct ); 
			trace("Getting pinmailer ..got : " + pinmailerid  );		
			EHsm ehsm = new EHsm();   
			ByteArrayOutputStream pinRequestBuffer = ehsm.ComposePINMailer(instid,command,pvkvalue,cardno, cin, productcode, subproduct, gentype,jdbctemplate, commondesc, outputStream, hsmobj);
			trace("Send to hsm....");  
			trace("Send to hsm...."+pinRequestBuffer);  
			String response = sendToHSMForPrintPin(pinRequestBuffer, ehsm, out, in);
			trace("got final response : " + response );
			offsetresp = deComposePinMailer(command,response); 
			outputStream.flush();
			outputStream.close();
		}catch(Exception e ){
			e.printStackTrace();
		}finally{
			outputStream.close();
		}
		
		return offsetresp;
	} 
	
	
	//Racal Pin generation
	
   /* public String printRacalPin( String instid, String cardno,String mcard, String cin, String productcode,  String bin, String gentype, DataInputStream in, DataOutputStream out, JdbcTemplate jdbctemplate , HSMParameter hsmobj,String CHN ) throws Exception {
		
		String offsetresp = null;
		CommonDesc commondesc = new CommonDesc();  
		String pinmailerid = hsmobj.PINMAILER_ID;
		String pvkvalue  = hsmobj.PVK;
		String hsmid  = hsmobj.HSM_ID;
		trace("Hsm id ["+hsmid+"] PVK ["+pvkvalue+"] PINMAILERID ["+pinmailerid+"]" ); 
		trace("=================================================================="); 
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		//String command = hsmcommand;
		
		try{  
			trace("Getting product code ..got : " + productcode );
			String subproduct = commondesc.getSubProductByCHNProcess(instid, CHN, gentype, jdbctemplate) ;
			trace("Getting sub-product code ..got : " + subproduct ); 
			trace("Getting pinmailer ..got : " + pinmailerid  );		
			EHsm ehsm = new EHsm();  
			//ByteArrayOutputStream pinRequestBuffer = ehsm.ComposeRacalPINMailer(instid,pvkvalue,cardno,mcard, cin, productcode, subproduct, gentype,jdbctemplate, commondesc, outputStream, hsmobj,in,out);
			String  res = ehsm.ComposeRacalPINMailer(instid,pvkvalue,cardno,mcard, cin, productcode, subproduct, gentype,jdbctemplate, commondesc, outputStream, hsmobj,in,out);
			trace("Send to hsm...."); 
		
			byte c[] = pinRequestBuffer.toByteArray();//21mar18
		    for (int i = 0; i < c.length; i++) {
		      System.out.println((char) c[i]);
		    }
		    offsetresp = new String(c);
		   
		}catch(Exception e ){
			e.printStackTrace();
		}finally{
			outputStream.close();
		}
		
		return offsetresp;
	} 
			
			byte c[] = pinRequestBuffer.toByteArray();
		    for (int i = 0; i < c.length; i++) {
		      System.out.println((char) c[i]);
		    }
		    offsetresp = new String(c);
		    String response = sendToHSMForPrintPin(pinRequestBuffer, ehsm, out, in);
			trace("got final response : " + response );
			offsetresp = deComposePinMailer(command,response); 
			outputStream.flush();
			outputStream.close();
		}catch(Exception e ){
			e.printStackTrace();
		}finally{
			outputStream.close();
		}
		
		return offsetresp;
	} 
			
	*/		
//public String printRacalPin( String instid, String cardno,String mcard, String cin, String productcode,  String bin, String gentype, DataInputStream in, DataOutputStream out, JdbcTemplate jdbctemplate , HSMParameter hsmobj ) throws Exception {
	public String printRacalPin( String instid, String cardno,String mcard, String cin, String productcode,  String bin, String gentype, DataInputStream in, DataOutputStream out, JdbcTemplate jdbctemplate , HSMParameter hsmobj,String CHN ) throws Exception {	
		String offsetresp = null;
		CommonDesc commondesc = new CommonDesc();  
		String pinmailerid = hsmobj.PINMAILER_ID;
		String pvkvalue  = hsmobj.PVK;
		String hsmid  = hsmobj.HSM_ID;
		trace("Hsm id ["+hsmid+"] PVK ["+pvkvalue+"] PINMAILERID ["+pinmailerid+"]" ); 
		trace("=================================================================="); 
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		String outputstrem[] = null;
		try{  
			trace("Getting product code ..got : " + productcode );
			//String subproduct = commondesc.getSubProductByCHNProcess(instid, cardno, gentype, jdbctemplate) ;
			String subproduct = commondesc.getSubProductByCHNProcess(instid, CHN, gentype, jdbctemplate) ;
			trace("Getting sub-product code ..got : " + subproduct ); 
			trace("Getting pinmailer ..got : " + pinmailerid  );		
			EHsm ehsm = new EHsm();  
			//ByteArrayOutputStream pinRequestBuffer = ehsm.ComposeRacalPINMailer(instid,pvkvalue,cardno,mcard, cin, productcode, subproduct, gentype,jdbctemplate, commondesc, outputStream, hsmobj,in,out);
			String resp = ehsm.ComposeRacalPINMailer(instid,pvkvalue,cardno,mcard, cin, productcode, subproduct, gentype,jdbctemplate, commondesc, outputStream, hsmobj,in,out);
			trace("Send to hsm...."); 
			//byte c[] = pinRequestBuffer.toByteArray();
			
			outputstrem = resp.split("~");
			
			byte[] c = outputstrem[0].getBytes();
		    for (int i = 0; i < c.length; i++) {
		      System.out.println((char) c[i]);
		    }
		    offsetresp = new String(c);
		    System.out.println(offsetresp+"<---outputstream--->"+outputstrem[1]);
		    /*String response = sendToHSMForPrintPin(pinRequestBuffer, ehsm, out, in);
			trace("got final response : " + response );
			offsetresp = deComposePinMailer(command,response); 
			outputStream.flush();
			outputStream.close();*/
		}catch(Exception e ){
			e.printStackTrace();
		}finally{
			outputStream.close();
		}
		
		return offsetresp;
	}

	public String printRacalPinOtp( String instid, String cardno,String mcard, String cin, String productcode,  String bin, String gentype, DataInputStream in, DataOutputStream out, JdbcTemplate jdbctemplate , HSMParameter hsmobj,String CHN ) throws Exception {	
		String offsetresp = null;
		CommonDesc commondesc = new CommonDesc();  
		String pinmailerid = hsmobj.PINMAILER_ID;
		String pvkvalue  = hsmobj.PVK;
		String hsmid  = hsmobj.HSM_ID;
		trace("Hsm id ["+hsmid+"] PVK ["+pvkvalue+"] PINMAILERID ["+pinmailerid+"]" ); 
		trace("=================================================================="); 
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
		String outputstrem[] = null;
		try{  
			trace("Getting product code ..got : " + productcode );
			//String subproduct = commondesc.getSubProductByCHNProcess(instid, cardno, gentype, jdbctemplate) ;
			String subproduct = commondesc.getSubProductByCHNProcess(instid, CHN, gentype, jdbctemplate) ;
			trace("Getting sub-product code ..got : " + subproduct ); 
			trace("Getting pinmailer ..got : " + pinmailerid  );		
			EHsm ehsm = new EHsm();  
			//ByteArrayOutputStream pinRequestBuffer = ehsm.ComposeRacalPINMailer(instid,pvkvalue,cardno,mcard, cin, productcode, subproduct, gentype,jdbctemplate, commondesc, outputStream, hsmobj,in,out);
			String resp = ehsm.ComposeRacalPINMailerOtp(instid,pvkvalue,cardno,mcard, cin, productcode, subproduct, gentype,jdbctemplate, commondesc, outputStream, hsmobj,in,out);
			trace("Send to hsm...."); 
			//byte c[] = pinRequestBuffer.toByteArray();
			
			/*outputstrem = resp.split("~");
			
			byte[] c = outputstrem[0].getBytes();
		    for (int i = 0; i < c.length; i++) {
		      System.out.println((char) c[i]);
		    }*/
		    offsetresp = resp;
		    System.out.println(offsetresp+"<---outputstream--->");
		    /*String response = sendToHSMForPrintPin(pinRequestBuffer, ehsm, out, in);
			trace("got final response : " + response );
			offsetresp = deComposePinMailer(command,response); 
			outputStream.flush();
			outputStream.close();*/
		}catch(Exception e ){
			e.printStackTrace();
		}finally{
			outputStream.close();
		}
		
		return offsetresp;
	}
	
	
	public String generateCVV_Values( String CHN, String expirydate, String ServiceCode,  HSMParameter hsmobj,  DataInputStream in, DataOutputStream out ) throws Exception {
		String cvv_value = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( ); 
		EHsm ehsm = new EHsm();  
	 
		String CVK = hsmobj.CVVK1+ hsmobj.CVVK2; 
		String Message = "";
		try{
			outputStream = ehsm.eracom_out( "01", 2, "length1", 1, outputStream );
			outputStream = ehsm.eracom_out( "01", 2, "length2", 1, outputStream ); 
			outputStream =  ehsm.eracom_out( "0001", 4, "length3", 1, outputStream );    
			String val = "0026"; 
			outputStream.write(  ehsm.ascii_to_bcd( val, 2 ) ); 
			ehsm.ComposeCVV(CVK, CHN, expirydate, ServiceCode, outputStream);  
			ehsm.converttoString( Message ); 
			trace("----request begin------");
			trace( new String ( Hex.encodeHex( outputStream.toByteArray() ) ) );
			trace("----request end ------"); 
			
			ByteArrayOutputStream bout = new ByteArrayOutputStream(); 
			String cvvmsg= new String ( Hex.encodeHex( outputStream.toByteArray() ) ); 
			trace("Sending : " + cvvmsg );
			bout.write( EHsm.ascii_to_bcd( cvvmsg, cvvmsg.length()/2 ) ) ; 
			out.write( bout.toByteArray() );
			out.flush();
			byte[] response = new byte[18];
			in.read(response);		
			trace("----response begin ------");
			trace( new String ( Hex.encodeHex( response)  ) );
			trace("----response end------");
			
			String rsp = new String ( Hex.encodeHex( response)  ); 
			cvv_value = ehsm.getCVVResponse(rsp);
			bout.flush();
			outputStream.flush();  
			trace("cvv_value :" + cvv_value);
		
			
			
			/*String val = "HMAS";
			val += "CW";
			val += CVK;
			val += CHN;
			val += ";";
			val += expirydate;
			val += ServiceCode;
			trace("sadfasfasf"+val+"length is"+val.length());
			outputStream.write(  ehsm.ascii_to_bcd( String.format("%04d",val.length()), 2 ) );
			//outputStream.write(  ehsm.ascii_to_bcd( val, 2 ) ); 
			outputStream.write(val.getBytes()); 
			//ehsm.ComposeCVV(CVK, CHN, expirydate, ServiceCode, outputStream); 
			//ehsm.converttoString( Message ); 
			trace("----request begin------");
			trace( new String ( Hex.encodeHex( outputStream.toByteArray() ) ) );
			trace("----request end ------"); 
			
			ByteArrayOutputStream bout = new ByteArrayOutputStream(); 
			String cvvmsg= new String ( Hex.encodeHex( outputStream.toByteArray() ) ); 
			trace("Sending : " + cvvmsg );
			bout.write( EHsm.ascii_to_bcd( cvvmsg, cvvmsg.length()/2 ) ) ; 
			//out.write( bout.toByteArray() )
			trace("sdfasfasfsa"+outputStream.toByteArray());
			//String bytestring =new String( Hex.encodeHex(outputStream.toByteArray()));
			//outputStream.write(bytestring.getBytes());
			out.write( outputStream.toByteArray() );
			out.flush();
			byte[] response = new byte[18];
			in.read(response);		
			trace("----response begin ------");
			trace( new String ( Hex.encodeHex( response)  ) );
			trace("----response end------");
			
			//String rsp = new String ( Hex.encodeHex( response)  ); 
			String rsp = new String (  response );
			String convrsp = rsp.trim();
			cvv_value = ehsm.getCVVResponse(convrsp);
			bout.flush();
			outputStream.flush();  
			trace("cvv_value :" + cvv_value);*/
		
		
		
		
		}catch(Exception e ){
			trace("Exception while generating cvv..." + e.getMessage());
			e.printStackTrace();
		}finally{
			outputStream.close(); 
		}
		return cvv_value;
		
	}
	
	
	public String generateRacalCVV_Values( String CHN, String expirydate, String ServiceCode,  HSMParameter hsmobj,  DataInputStream in, DataOutputStream out ) throws Exception {
		String cvv_value = null;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream( ); 
		EHsm ehsm = new EHsm();  
	 
		String CVK = "U"+hsmobj.CVVK1+ hsmobj.CVVK2; 
		String Message = "";
		try{
			//HMASCW U466D5AAFA26C4ED0E0EB1E3B55DEFE845077620200000018;1710501 HSM Parameter63
			String val = "HMAS";
			val += "CW";
			val += CVK;
			val += CHN;
			val += ";";
			val += expirydate;
			val += ServiceCode;
			trace("HSM Input:::"+val+" HSM Parameter"+val.length());
			String pad = "00";
			ByteArrayOutputStream newhex = new ByteArrayOutputStream();
			newhex.write(pad.getBytes());
			outputStream.write(val.length() );
			trace("Byte to Hex:::"+byteToHexString(outputStream.toByteArray()));
			ByteArrayOutputStream bout = new ByteArrayOutputStream(); 
			String cvvmsg= byteToHexString(outputStream.toByteArray());
			newhex.write(cvvmsg.getBytes());
			String newdsf = new String(newhex.toByteArray());
			String ival = hexToASCII(newdsf);
			trace("Hex to ASCII value::"+ival);
			bout.write(ival.getBytes());
			bout.write(val.getBytes());
			trace("Input Parameter to HSM::"+bout.toByteArray());
			out.write( bout.toByteArray() );
			out.flush();
			byte[] response = new byte[23];
			in.read(response);		
			String rsp = new String (  response );
			String convrsp = rsp.trim();
			trace("Response from HSM::"+convrsp);
			if(convrsp == null || convrsp == "")
			{
				cvv_value = null;
				trace("CVV got NULL");
			}
			cvv_value = ehsm.getRacalCVVResponse(convrsp);
			trace("CVV Value "+cvv_value);
			bout.flush();
			outputStream.flush();  
			
			
         }catch(Exception e ){
			trace("Exception while generating cvv..." + e.getMessage());
			e.printStackTrace();
		}finally{
			outputStream.close(); 
		}
		return cvv_value;
		
	}
	
	public static String byteToHexString(byte[] bytes)
	{
		StringBuilder sb = new StringBuilder();
		for(byte b :  bytes)
		{
			sb.append(String.format("%02x", b&0xff));
		}
		return sb.toString();
	}
	
	public static String hexToASCII(String hexValue)
	   {
	      StringBuilder output = new StringBuilder("");
	      for (int i = 0; i < hexValue.length(); i += 2)
	      {
	         String str = hexValue.substring(i, i + 2);
	         output.append((char) Integer.parseInt(str, 16));
	      }
	      return output.toString();
		
		/*int value = Integer.parseInt(hexValue, 16);   
		  char c = (char) value;
		  String val = Character.toString(c);
		  System.out.println(val);
		  return val;*/
	   }
	
	
	public String hexa(int num) {
	  /*  int m = 0;
	    if( (m = num >>> 4) != 0 ) {
	        hexa( m );
	    }
	    return (char)((m=num & 0x0F)+(m<10 ? 48 : 55));*/
		
		return Integer.toHexString( num );
	}
	
	
	
 
	
	public String pinVerifiCationInit( String CHN, String pinoffset, String pinblock, String ppk, String EPVK,  DataInputStream in, DataOutputStream out ) throws Exception {
		EHsm ehsm = new EHsm();
		ByteArrayOutputStream outputStream  = new ByteArrayOutputStream();   
		String pinverified = null;
		int acctblockdigits = 12;
		String command = "EE0603";
		
		try{
			System.out.println("Pin Verification...."); 
			//EPVK = "DD818BECE2AC2B09DD818BECE2AC2B09CBA23E21D63C652D";
			//CHN = "4660421004230075";
			//pinblock = "A8DAAFD21B5F6DFE";
			//ppk = "BC6A9B4080CD5F58020DCF33D1121F22";
			//pinoffset = "852900000000";
			String acctblock = CHN.substring(CHN.length()-(acctblockdigits+1), CHN.length() );
			acctblock = acctblock.substring(0, acctblock.length()-1);
			outputStream  = ehsm.pinVerifiCationSafeNet( command, EPVK, CHN, pinblock, ppk, pinoffset, acctblock,  outputStream);
			pinverified  = this.sendToHSMForPrintPin(outputStream, ehsm, out, in);
			System.out.println("result : " + pinverified  );
			trace("Verification result : " + pinverified  );
			this.deComposePinMailer(command,pinverified);
			
		}catch(Exception e ){
			e.printStackTrace();
			trace("Exception : " + e);
		}finally{
			if( outputStream != null ){ outputStream.close(); }
			ehsm = null;
		}
		return pinverified ;
	}
	
	
	public String getRacalHSMResponse(String respcode ){
		String respmsg = respcode;
		String responsecode = respmsg.substring(6,8);
		trace("Respcode :::"+responsecode);
		if( responsecode.trim().equals("00") || responsecode.trim().equals("02")){
			respmsg = "00" ; 
		}else if( responsecode.trim().equals("01")){
			respmsg = "CVV Failed Verification";
		}else if( responsecode.trim().equals("10")){
			respmsg = " CVK A or B parity error";
		}else if( responsecode.trim().equals("27")){
			respmsg = "CVK not double length";
		}else if( responsecode.trim().equals("68")){
			respmsg = "Command disabled";
			
		}
		/*else if( responsecode.trim().equals("02")){
			respmsg = "PVK_NOT_SINGLE_LENGTH";
			
		}*/else if( responsecode.trim().equals("12")){
			respmsg = "NO_KEYS_LOADED";
		}else if( responsecode.trim().equals("13")){
			respmsg = "HSM_LMK_ERROR";
		}else if( responsecode.trim().equals("14")){
			respmsg = "ERROR_IN_ENCRYPTED_PIN";
		}else if( responsecode.trim().equals("15")){
			respmsg = "HSM_ERROR_INPUT_DATA";
		}else if( responsecode.trim().equals("16")){
			respmsg = "PRINTER_NOT_READY";
		}else if( responsecode.trim().equals("17")){
			respmsg = "NOT_IN_AUTHORIZED_STATE";
		}else if( responsecode.trim().equals("27")){
			respmsg = "CVK_NOT_DOUBLE_LENGTH";
		}else if( responsecode.trim().equals("21")){
			respmsg = "INVALID_USER_STORAGE_INDEX";
		}else if( responsecode.trim().equals("90")){
			respmsg = "GENERAL PRINTER ERROR";
		}  
		
		trace("RespReason as ::" + responsecode );
		return respmsg;
	}
	
	public String getRespCode(String respcode){
        
		String respmsg ="";
		String responsecode = respcode;
		trace("respcode we got"+responsecode);
		if( responsecode.trim().equals("00") || responsecode.trim().equals("02")){
			respmsg = "00" ; 
		}else if( responsecode.trim().equals("01")){
			respmsg = "CVV Failed Verification";
		}else if( responsecode.trim().equals("10")){
			respmsg = " CVK A or B parity error";
		}else if( responsecode.trim().equals("27")){
			respmsg = "CVK not double length";
		}else if( responsecode.trim().equals("68")){
			respmsg = "Command disabled";
			
		}
		/*else if( responsecode.trim().equals("02")){
			respmsg = "PVK_NOT_SINGLE_LENGTH";
			
		}*/else if( responsecode.trim().equals("12")){
			respmsg = "NO_KEYS_LOADED";
		}else if( responsecode.trim().equals("13")){
			respmsg = "HSM_LMK_ERROR";
		}else if( responsecode.trim().equals("14")){
			respmsg = "ERROR_IN_ENCRYPTED_PIN";
		}else if( responsecode.trim().equals("15")){
			respmsg = "HSM_ERROR_INPUT_DATA";
		}else if( responsecode.trim().equals("16")){
			respmsg = "PRINTER_NOT_READY";
		}else if( responsecode.trim().equals("17")){
			respmsg = "NOT_IN_AUTHORIZED_STATE";
		}else if( responsecode.trim().equals("27")){
			respmsg = "CVK_NOT_DOUBLE_LENGTH";
		}else if( responsecode.trim().equals("21")){
			respmsg = "INVALID_USER_STORAGE_INDEX";
		}else if( responsecode.trim().equals("90")){
			respmsg = "GENERAL PRINTER ERROR";
		}  
		
		trace("respreason : " + respmsg );
		return respmsg;
	
	}
	
	
	public String getRacalHSMAfterResponse(String respcode ){
		String respmsg = respcode;
		String responsecode = respmsg.substring(12,14);
		trace("respcode we got"+responsecode);
		if( responsecode.trim().equals("00") || responsecode.trim().equals("02")){
			respmsg = "00" ; 
		}else if( responsecode.trim().equals("01")){
			respmsg = "CVV Failed Verification";
		}else if( responsecode.trim().equals("10")){
			respmsg = " CVK A or B parity error";
		}else if( responsecode.trim().equals("27")){
			respmsg = "CVK not double length";
		}else if( responsecode.trim().equals("68")){
			respmsg = "Command disabled";
			
		}
		/*else if( responsecode.trim().equals("02")){
			respmsg = "PVK_NOT_SINGLE_LENGTH";
			
		}*/else if( responsecode.trim().equals("12")){
			respmsg = "NO_KEYS_LOADED";
		}else if( responsecode.trim().equals("13")){
			respmsg = "HSM_LMK_ERROR";
		}else if( responsecode.trim().equals("14")){
			respmsg = "ERROR_IN_ENCRYPTED_PIN";
		}else if( responsecode.trim().equals("15")){
			respmsg = "HSM_ERROR_INPUT_DATA";
		}else if( responsecode.trim().equals("16")){
			respmsg = "PRINTER_NOT_READY";
		}else if( responsecode.trim().equals("17")){
			respmsg = "NOT_IN_AUTHORIZED_STATE";
		}else if( responsecode.trim().equals("27")){
			respmsg = "CVK_NOT_DOUBLE_LENGTH";
		}else if( responsecode.trim().equals("21")){
			respmsg = "INVALID_USER_STORAGE_INDEX";
		}else if( responsecode.trim().equals("90")){
			respmsg = "GENERAL PRINTER ERROR";
		}  
		
		trace("respreason : " + responsecode );
		return respmsg;
	}
	
	
	public String generateSessionKey(  DataInputStream in, DataOutputStream out  ) throws Exception {
		String command = "EE0400";
		EHsm ehsm = new EHsm(); 
		ByteArrayOutputStream outputStream  = null;
		JSONObject jsonkeys = new JSONObject();
		try{
			outputStream = new ByteArrayOutputStream();
			String TMK = "66A35E1B43C02C2E66A35E1B43C02C2E";
			outputStream = ehsm.generateSessionkeyMessage(command, TMK, outputStream);
			
			String sessionkeymsg = this.sendToHSMForPrintPin(outputStream, ehsm, out, in);
			System.out.println("Session key from hsm : " + sessionkeymsg );
			trace("Session key from hsm : " + sessionkeymsg);
			//this.deComposePinMailer(command,sessionkey);
			this.decomposeKeys(command, sessionkeymsg, jsonkeys);
			trace("jsonkeys :" + jsonkeys );
		}catch(Exception e){
			e.printStackTrace();
			trace("Exception while generating session key : " + e);
			return null;
		}
		finally{
			if( outputStream != null ){ outputStream.close(); }
			ehsm = null;
		}
		
		
		
		return "JOHNPRITTO";
		
	}
	
	
	
	public static void main( String args[] ) throws Exception {
		HsmTcpIp tcp = new HsmTcpIp();
		String hsmip = "192.168.15.17";
		int hsmport = 4500;
		DataInputStream in = null;
		DataOutputStream out  = null;
		System.out.println("Opening  connection....");
		Socket connect_id = tcp.connectingHSM(hsmip, hsmport, 10000);
		if ( connect_id != null ) {				
			in= new DataInputStream (new BufferedInputStream(connect_id.getInputStream()));
			out = new DataOutputStream (new BufferedOutputStream(connect_id.getOutputStream()));  
		}else{				 
			System.out.println("Could not connect hsm"); 
		} 
		
		try{
			EHsm ehsm = new EHsm(); 
			/*System.out.println("Pin Verification...."); 
			ByteArrayOutputStream outputStream  = new ByteArrayOutputStream();   
			String EPVK = "DD818BECE2AC2B09DD818BECE2AC2B09CBA23E21D63C652D";
			String CHN = "4660421004230075";
			String pinblock = "A8DAAFD21B5F6DFE";
			String ppk = "BC6A9B4080CD5F58020DCF33D1121F22";
			String pinoffset = "852900000000";
			String acctblock = "042100423007";
			outputStream  = ehsm.pinVerifiCationSafeNet( EPVK, CHN, pinblock, ppk, pinoffset, acctblock, connect_id, outputStream);
			tcp.sendToHSMForPrintPin(outputStream, ehsm, out, in);
			 */
			
			
			/*HsmTcpIp tcp = new HsmTcpIp();
			
			String CHN="4660421004230075" ;
			String expirydate="1601";
			String ServiceCode="901";
			
			tcp.generateCVV(CHN,expirydate,ServiceCode,"192.168.15.212", 9000);
			for ( int i=0;i<=10; i++ ){
				trace("Printin...."+i+ " Pin");
			//	tcp.printPin( "192.168.15.212", 9000  );
			}
			//tcp.generateCVV( "192.168.15.212", 9000 );
			trace ( tcp.hexa( 61 ) ) ;
			
			
			 trace( "ascii to bcd " +  EHsm.ascii_to_bcd( "3d" , 1 ) ); 
			 
			 tcp.deComposePinMailer("");*/
			 
		}catch(Exception e ){
			//e.printStacktrace();
		}finally{
			connect_id.close();
			System.out.println("Connection closed....");
		}
	} 
}
