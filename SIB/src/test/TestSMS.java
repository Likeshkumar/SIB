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

public class TestSMS extends BaseAction {
	
	
	
	
	
	
	String urlstr="https://vjjy5e.api.infobip.com/sms/2/text/single";

	
	public static void main(String[] args) {
		TestSMS t=new TestSMS();
		t.smscall("5555", "gnhjgj");
	}
	
	public  String smscall(String Mobile,String Message){
		//String urlstr="http://api.nalosolutions.com/bulksms/";
		StringBuffer response = new StringBuffer();
		try{
			URL url = new URL(urlstr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("Authoriztion", "App 14da842ab1ffaa99326736d42b77cf07-b1296950-91d0-46f6-8da6-a936fbb37ade");
			conn.setDoInput(true);
			 conn.setDoOutput(true);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("from", "SIB"));
			params.add(new BasicNameValuePair("to", "23275845005"));
			params.add(new BasicNameValuePair("test", "hello"));
			 

			String query = getQuery(params);

			System.out.println("encoded values ::::::  " + query);
			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(query);
			System.out.println("params query -->" + writer.toString());
			writer.flush();
			writer.close();
			os.close();

			conn.connect();

			int respCode = conn.getResponseCode();
			smslog("respCode --->" + respCode);
			System.out.println(respCode);

			if (respCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String inputLine;

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();

				// print result
				smslog("Final Response from SMS Vendor --->" + response.toString());
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
