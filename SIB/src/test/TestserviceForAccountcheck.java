package test;


import java.io.*;
import java.net.*;
import java.util.*;
import javax.net.ssl.HttpsURLConnection;
/*import org.json.simple.JSONObject;*/
import org.json.JSONObject;
import org.apache.commons.httpclient.HttpHost;
import org.apache.commons.httpclient.HttpsURL;
import org.json.JSONArray;

import org.json.JSONObject;

public class TestserviceForAccountcheck {

		private final String USER_AGENT = "Mozilla/5.0";
		
		public static void main(String[] args)
		{
			TestserviceForAccountcheck urlTest = new TestserviceForAccountcheck();
			urlTest.testGetWalletInfo();
			//urlTest.testRefreshCards();
			//urlTest.testFundsTransfer();
		}
		
		public void testGetWalletInfo()
		{
			//Test Wallet Account Info
			JSONObject inputObject = new JSONObject();
			inputObject.put("REQ", "GET_WALLET_ACCOUNT_INFO");
			inputObject.put("UID", "Cardsys23");
			inputObject.put("PWD", "ahbj0$34");
			inputObject.put("SYS", "prepaidcard");
			inputObject.put("ACC", "5000007");

			try
			{
				HashMap inputData = new HashMap();
				inputData.put("rd", inputObject.toString());
				
				String resultString = sendPost("https://uatapp.orient-bank.com/wb/lstn/cardserviceequio.jsp", inputObject.toString(), inputData);
				if (resultString == null) resultString = "";
				resultString = resultString.trim();
				
				System.out.println("Input:" + inputObject.toString());
				System.out.println("Output response:" + resultString);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		public void testRefreshCards()
		{
			
			JSONObject inputObject = new JSONObject();
			inputObject.put("REQ", "REFRESH_CARDS");
			inputObject.put("UID", "Cardsys23");
			inputObject.put("PWD", "ahbj0$34");
			inputObject.put("SYS", "prepaidcard");
			inputObject.put("CIF", "2000516");

			try
			{
				HashMap inputData = new HashMap();
				inputData.put("rd", inputObject.toString());
				
				String resultString = sendPost("https://uatapp.orient-bank.com/wb/lstn/cardserviceequio.jsp", inputObject.toString(), inputData);
				if (resultString == null) resultString = "";
				resultString = resultString.trim();
				
				System.out.println("Input:" + inputObject.toString());
				System.out.println("Output:" + resultString);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

		public void testFundsTransfer()
		{
			//Test Wallet Account Info
			JSONObject inputObject = new JSONObject();
			//Test FT
			JSONObject ftObject = new JSONObject();
			ftObject.put("REQ", "PERFORM_FT");
			ftObject.put("UID", "Cardsys23");
			ftObject.put("PWD", "ahbj0$34");
			ftObject.put("SYS", "prepaidcard");
			
			ftObject.put("FT_FROM", "5000031");
			ftObject.put("FT_TO", "5000032");
			ftObject.put("FT_REMARKS", "Transfer Remarks");
			ftObject.put("FT_AMT", "500");
			ftObject.put("FT_TRAN_REF", "R232323");

			try
			{
				HashMap inputData = new HashMap();
				inputData.put("rd", ftObject.toString());
				
				String resultString = sendPost("https://uatapp.orient-bank.com/wb/lstn/cardserviceequio.jsp", inputObject.toString(), inputData);
				if (resultString == null) resultString = "";
				resultString = resultString.trim();
				
				System.out.println("Input:" + ftObject.toString());
				System.out.println("Output:" + resultString);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		public String noNull(Object input)
		{
			if (input == null) return "";
			return (String)input;
		}
		
		private String sendPost(String inputURL, String dataToBePosted, HashMap reqParamMap) 
		throws Exception 
		{
			URL obj = new URL(inputURL);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

			//add request header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			
			if (reqParamMap == null) reqParamMap = new HashMap();
			Iterator keyIterator = reqParamMap.keySet().iterator();
			while(keyIterator.hasNext())
			{
				String key = noNull(keyIterator.next());
				String value = noNull(reqParamMap.get(key));
				
				con.setRequestProperty(key,value);
			}
			
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(dataToBePosted);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + inputURL);
			System.out.println("Post parameters : " + dataToBePosted);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));

			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//print result
			System.out.println(response.toString());
			return response.toString();
		}
		
				
		
	}