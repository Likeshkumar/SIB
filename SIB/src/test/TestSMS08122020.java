package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.ifp.Action.BaseAction;

public class TestSMS08122020 extends BaseAction {
	
	
	
	
	
	
	String urlstr="http://apps.txtmategh.com/sms/api";

	
	public static void main(String[] args) {
		TestSMS08122020 t=new TestSMS08122020();
		t.smscall("5555", "gnhjgj");
	}
	
	public  String smscall(String Mobile,String Message){
		//String urlstr="http://api.nalosolutions.com/bulksms/";
		StringBuffer response = new StringBuffer();
		try{
		URL url = new URL(urlstr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		String redirect = conn.getHeaderField("Location");
		if (redirect != null){
			conn = (HttpURLConnection) new URL(redirect).openConnection();
		}
		//HttpsURLConnection conn=(HttpsURLConnection)url.openConnection();
		conn.setReadTimeout(10000);
		conn.setConnectTimeout(15000);
		conn.setRequestMethod("PUT");
		conn.setDoInput(true);
		conn.setDoOutput(true);
		Properties prop = getCommonDescProperty();
		String action =prop.getProperty("pin.acion");
		String api_key =prop.getProperty("pin.api_key");
		String sender_id =prop.getProperty("pin.sender_id");
		String mobile=Mobile;
		String message=Message;
		
		smslog ("api_key--->"+api_key +"sender_id--->"+sender_id);
		smslog ("mobile--->"+mobile);
		smslog ("message--->"+message);
		trace ("mobile--->"+mobile);
		trace ("message--->"+message);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("action","send-sms"));
		params.add(new BasicNameValuePair("api_key",api_key));
		params.add(new BasicNameValuePair("to", mobile));
		params.add(new BasicNameValuePair("from", sender_id));
		params.add(new BasicNameValuePair("sms", message));

		String query = getQuery(params);
		
		System.out.println(query);
		
		OutputStream os = conn.getOutputStream();
		BufferedWriter writer = new BufferedWriter(
		        new OutputStreamWriter(os, "UTF-8"));
		writer.write(getQuery(params));
		System.out.println("params query -->" +writer.toString());
		writer.flush();
		writer.close();
		os.close();

		conn.connect();
		
		
		int respCode = conn.getResponseCode();
		smslog("respCode --->" +respCode);
		System.out.println(respCode);
		
		if(respCode == HttpURLConnection.HTTP_OK){
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			smslog("Final Response from SMS Vendor --->" +response.toString());
			System.out.println(response.toString());
		}
		
		
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return response.toString();
	}
	
	
	
	
	
	public   String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
	{
	    StringBuilder result = new StringBuilder();
	    boolean first = true;

	    for (NameValuePair pair : params)
	    {
	        if (first)
	            first = false;
	        else
	            result.append("&");

	        result.append(URLEncoder.encode(pair.getName(), StandardCharsets.UTF_8.toString()));
	        result.append("=");
	        result.append(URLEncoder.encode(pair.getValue(), StandardCharsets.UTF_8.toString()));
	    }
	    smslog("Request params parameters --->"+urlstr+"?"+result.toString());
System.out.println("getquery params -->" + result.toString());
	    return result.toString();
	}

}
