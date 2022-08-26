
package test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ifp.Action.BaseAction;
import com.sun.jndi.toolkit.url.Uri;

/**
 * 
 * This class is used for
 * 
 * @author 
 * @version 1.0
 * 
 * @serial 1000
 * 
 *
 */

 
public class URLClientRegistration extends BaseAction {

	public static void main(String[] args) throws Exception {
		URLClientRegistration u = new URLClientRegistration();

		JSONObject jsonObj = u.example("1234567890098765432");
 

		System.out.println(jsonObj);

		JSONArray jArray = jsonObj.getJSONArray("data");

		System.out.println("arra ******" + jArray);

		for (int i = 0; i < jArray.length(); i++) {
			JSONObject jObj = jArray.getJSONObject(i);
			String acctId = jObj.optString("AcctTypeId");
			String accCurenccy = jObj.optString("Account Currency");

			System.out.println("acctId ******" + acctId);
			System.out.println("accCurenccy ******" + accCurenccy);

		}

 
	}

	public static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
		StringBuilder result = new StringBuilder();
		boolean first = true;

		for (NameValuePair pair : params) {
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
			result.append("/");
			result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
		}
		System.out.println("result-->"+result.toString());
		return result.toString();
	}

	public JSONObject uploadToServer(String Accountno) throws Exception {
		
		Properties props = getCommonDescProperty();
		String cbsapiurl = props.getProperty("cbs.api.url");
		String cbsapikey = props.getProperty("cbs.api.key");
		String cbsapisecret = props.getProperty("cbs.api.secret");
		 
	 	String urlstr = cbsapiurl+"core/api/v1.0/account/verifyacctpan/"+Accountno;
		 
	 	trace("Calling uploadTo Server method with account no --->" + Accountno);
		 
		JSONObject jsonObject =null;
		 
		try {
			
			StringBuffer response = new StringBuffer();

			URL url = new URL(urlstr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(20000);
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("x-api-key", cbsapikey);
			conn.setRequestProperty("x-api-secret", cbsapisecret);
			//conn.setRequestProperty("X-FORWARDED-FOR", "172.16.10.95");
			conn.setDoOutput(true);
			 
		 	System.out.println("URL ::::::  " + urlstr);
			
			OutputStream os = conn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
			writer.write(urlstr);
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
				smslog("Final Response  --->" + response.toString());
				System.out.println(response.toString());
				jsonObject = new JSONObject(response.toString()
						);
			}
		 	
			
		} catch (Exception e) {
			e.printStackTrace();
			trace("Exception : while getting the customer details ... " + e.getMessage());
		}
		return jsonObject;
	}

	public JSONObject example(String Accountno) {

		JSONObject obj = new JSONObject();
		
		 
		
        obj.put("lastName", "KORTU");
        obj.put("accountDescription", "VIVIAN M. KORTU");
        obj.put("gender", "M");
        obj.put("address3", "2534");
		obj.put("address2", "1324");
		obj.put("city", "2642");
		obj.put("mobileNumber", "99235324234");
		obj.put("address1", "Accra Ghana");
		obj.put("accountType", "CURRENT ACCOUNT PRIVATE INDIVIDUAL");
		obj.put("dateOfBirth", "1970-01-01");
		obj.put("customerNumber", "138531");
		obj.put("fullBban", "005001130385310291");
		obj.put("firstName", "VIVIAN");
		obj.put("countryCode", "GHA");
		obj.put("currency", "GHS");
		obj.put("middleName", "VIVIAN");
		obj.put("maritalStatus", "s");
		obj.put("accountBranch", "001");
		
		
		//obj.put("Customer Name", "MAWME SANDRA");
		
		
		JSONArray arr = new JSONArray();

		arr.put(obj);

		JSONObject obj1 = new JSONObject();
		//JSONObject obj2 = new JSONObject();
		obj1.put("responseCode","000");
		obj1.put("message","Account number is already linked to a card");
		obj1.put("data", arr);
		System.out.println("arr :::  " + arr);
		//System.out.println("responseCode :::  " + responseCode);
		return obj1;
	}

}
