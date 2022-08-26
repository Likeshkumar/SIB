package com.ifp.webcall;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.util.Properties;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import com.ifp.Action.BaseAction;
import com.ifp.util.IfpTransObj;

public class SMSServiceCall extends BaseAction{
	
	public int SMSCall( String chn, String OTP, String MobNo, String Lang ) throws Exception{

		String internalResp = "", Request = "";
		int ret = -1;
		  try{
			  Request = ComposeXMLRequest(  chn,  OTP, MobNo, Lang );
			  internalResp = RESTCall( Request );
			  trace("SMS RESPONSE :\n"+internalResp);
			  ret = DecomposeXMLResp( internalResp );
		  
			 return ret;
			 }catch(Exception e ){
				 trace("Exception on GIPCall:" +e.getMessage());
			 }
		return ret; 
	}
	
	public String ComposeXMLRequest( String chn, String OTP, String MobNum, String Lang ) throws Exception{
		String Request = "";
		  try{

			  Request += "<SMSRequest>\n\t";
			  Request += "<SMSType>OTP</SMSType>\n\t";
			  Request += "<Username>"+this.getsmslink("smsusername")+"</Username>\n\t";
			  Request += "<Password>"+this.getsmslink("smspassword")+"</Password>\n\t";
			
			  Request += "<OTP>"+OTP+"</OTP>\n\t";
			  Request += "<CARDNO>"+chn+"</CARDNO>\n\t";
			  Request += "<ReceiverMobile>"+MobNum+"</ReceiverMobile>\n\t";
			 // Request += "<ReceiverMobile>738395186</ReceiverMobile>\n\t";
			  Request += "<Language>AR</Language>\n";
			  Request += "</SMSRequest>\n";
			  
			  System.out.println("SMS Request :\n"+Request);
			  
			 return Request;
			 }catch(Exception e ){
				 trace("Exception on Composing XML Request:" +e.getMessage());
			 }
		return "01"; 
	}
	public int DecomposeXMLResp( String Resp ) throws Exception{
		String GIPResp="";
		int ret = -1;
		
		  try{
			int x,y;
			String Header ="", Footer="";String returnval="";
			
		      for(int i=0; i< 65; i++) {
	    	  	  y=x=0; 
	              switch(i)
	              {
	               
	              case 1:
	            	  Header = "<RespCode>"; Footer ="</RespCode>";
	            	  x = Resp.indexOf(Header); y = Resp.indexOf(Footer);
	            	  GIPResp = Resp.substring(x +Header.length() , y);
	            	  trace("SMS RESPONSE CODE [ "+GIPResp+ " ]");
		          break;
		              

	              }
	      	 }
			 
		      if(GIPResp.equalsIgnoreCase("000"))
		    	  return 0; // SUCCESS
		      else 
		    	  return ret;
		      
			 }catch(Exception e ){
				 trace("Exception on De-Composing XML Request:" +e.getMessage());
			 }
		
		
		return 0; 
	}

	public String RESTCall( String Request ) throws Exception{
		
		String internalResp = "";
		DefaultHttpClient httpClient = new DefaultHttpClient();
		
	    try {
	    	  
	    	  HttpPost post = new HttpPost( this.getsmslink("pinsmslink"));
	    	  post.setHeader( "Accept", "application/xml" );
	    	  HttpEntity entity = new ByteArrayEntity( Request.toString().getBytes("UTF-8") );
	          post.setEntity( entity );

	          HttpResponse response = httpClient.execute(post);
	    	  trace("REST Resp:" +response);
	    	  InputStreamReader i_buff= new InputStreamReader(response.getEntity().getContent());
	    	  BufferedReader rd = new BufferedReader( i_buff );
	    	  String line = "";
	    	  
	    	  while ((line = rd.readLine()) != null) {
	    	   internalResp += line;
	    	   System.out.println(line);
	    	  }

		      trace("----------------------------------------");
		      System.out.println(response.getStatusLine());
		      trace(internalResp);
		      trace("----------------------------------------");
		      
		    return  internalResp.toString();
	    	} catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	      httpClient.getConnectionManager().shutdown();
	      
	    }
		return "01";
	}
	public String getsmslink(String searchservice) throws IOException
	{

		Properties prop = getProp( "smspin.properties" );
		String filedir = prop.getProperty("smspin.link");
		
		BufferedReader buff = new BufferedReader(new FileReader(filedir));
		String line="";
		try {
		    for( ; (line = buff.readLine()) != null; ) {
			if(line.contains(searchservice)){ break; }
		    }
		}catch (IOException e) { 
			trace("Exception: " + e.getMessage());
			
		}finally {buff.close();}

	  return line.replace(searchservice+"= ","");
	}
	
	public Properties getProp( String filename )
	{
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream(filename));
		} catch (IOException e) {
			 
			e.printStackTrace();
		} 
	  return prop;
	}


}
