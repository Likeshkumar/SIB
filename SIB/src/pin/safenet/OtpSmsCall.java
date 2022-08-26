package pin.safenet;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.ifp.Action.BaseAction;
/*import com.squareup.okhttp.MediaType;
//import com.sun.net.ssl.HttpsURLConnection;
//import com.squareup.okhttp.*;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;*/


@SuppressWarnings("deprecation")
public class OtpSmsCall extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	
	public  String uploadToServer(String mobilno,String message ) throws Exception {
		message = message.replaceAll(" ", "%20");
		
		smslog("-----OTP sms service starts----");
		String urlstr = "https://api.sierrahive.com/v1/messages/sms?clientid=ef21abfe83d3&clientsecret=a83b6ed00f9f42b8ac665b6cf2992eec&token=0a2f6b1b60104851855ea99b7e3a4cbb&from=RCBank&"
				+ "to="+mobilno+"&reference=1234&content="+message+"";
		CloseableHttpClient client = HttpClients.custom().
                setHostnameVerifier(new AllowAllHostnameVerifier()).
                setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy()
                {
					@Override
					public boolean isTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
							throws java.security.cert.CertificateException {
						return true;
					}
                }).build()).build();
		
		String result = "",ret=""; 
		try {
            HttpGet httpget = new HttpGet(urlstr);
            
            
            //System.out.println("executing request" + httpget.getRequestLine());
            CloseableHttpResponse response = client.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    BufferedReader rd = new BufferedReader(
			                new InputStreamReader(response.getEntity().getContent()));
                    String line = "";
					while ((line = rd.readLine()) != null) {
						result += line;
					}
                }
                
                	System.out.println("result--->"+result);
                	JSONObject object = new JSONObject(result.trim());
                	
                	if(result.contains("Status")){
                		ret = (String) object.get("Status");
                	}
			       System.out.println(ret);
			       
                EntityUtils.consume(entity);
            } finally {
                response.close();
                client.close();
            }
        } catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		
		return ret;
		
	
	}

	public JSONObject example() {

		JSONObject obj = new JSONObject();
		obj.put("AcctTypeId", "CURRENT ACCOUNT PRIVATE INDIVIDUAL");
		obj.put("Customer Name", "MAWME SANDRA");
		obj.put("Telephone", "992353524234");
		obj.put("Customer Id", "02006116");
		obj.put("Account Currency", "GHS");
		obj.put("DOB", "1970-01-01T00:00:00");
		obj.put("Address1", "Accra Ghana");
		obj.put("Address2", "");
		obj.put("Address3", "");
		obj.put("Address4", "");
		obj.put("City", "Accra");
		obj.put("Country code", "GHA");

		JSONArray arr = new JSONArray();
		
		arr.put(obj);
		
		JSONObject obj1 = new JSONObject();
		obj1.put("Table", arr);

		System.out.println("arr :::  "+arr);
		return obj1;
	}
	
	public static void main(String[] args) throws Exception {
		OtpSmsCall sms = new OtpSmsCall();
		sms.uploadToServer("23278195738", " Welcome To SIB");
	}
}
