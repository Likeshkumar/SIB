package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

public class HttpClientTest {
	
	public static void main(String[] args) {
		/*JSONObject carddet = new JSONObject();
		for(int i = 0;i<=2; i++){
			if(i == 1){
				carddet.put("branch", "001");
				carddet.put("cardno", "123456789");}
			if(i == 2){
				carddet.put("branch", "002");
				carddet.put("cardno", "023456789");}
		}
		JSONArray jsonarr = new JSONArray();
		jsonarr.put(carddet);
		System.out.println(jsonarr);*/
		String result = "",ret="",respmsg="NA", respcode="NA";
		JSONObject json = null;
		try {
				String url = "https://api.rokelbank.sl:9097/core/api/v1.0/account/activeCardReport";
				System.out.println("url-->"+url);
				HttpPost post = new HttpPost(url);
				HttpClient client = HttpClientBuilder.create().build();
				post.setHeader("Content-Type", "application/json");
				//post.setHeader("x-api-key", "C@rdm@n2021");
				//post.setHeader("x-api-secret", "C@rdm@nTest");
				
				
				String request= "{ \"activeCards\":[{" +
				   		"\"branch\":\"001\"," +
				   		"\"cardNumber\":\"6398070002000000094\"," +
				   		"\"customerNumber\":\"322554\"," +
				   		"\"accountNumber\":\"002001270606110118\"," +
				   		"\"emborsedName\":\"SIB TEST CARD 3\"," +
				   		"\"cardType\":\"INDIVIDUAL\"," +
				   		"\"issueDate\":\"10-04-2020\"," +
				   		"\"orderRefNo\":\"sdededed\"," +
				   		"\"maskedCardNumber\":\"6398-07xx-xxxxxxx-0094\"," +
				   		"\"makerId\":\"SDSF3R\"," +
				   		"\"bulkRefId\":\"234455\"," +
				   		"\"cardFlag\":\"\"," +
				   		"\"expiryDate\":\"11-NOV-2024\"}]}";
				
				/*String request= "{ " +
				   		"\"username\":\"RCBANK\"," +
				   		"\"password\":\"D6I8XA6CSZ\"" +
				   		"}";*/
				
				System.out.println(request);
				
				StringEntity params =new StringEntity(request);
				post.setEntity(params);
				HttpResponse response = client.execute(post);
				int responseCode = response.getStatusLine().getStatusCode();
				System.out.println("Response Code : " + responseCode);
				System.out.println();
				
				BufferedReader rd = new BufferedReader(
			                new InputStreamReader(response.getEntity().getContent()));
				String line = "";
				while ((line = rd.readLine()) != null) {
					result+= line;
				}
				ret = result;
			    System.out.println(ret);
			    json = new JSONObject(ret);
				System.out.println(json.getString("message")+"=="+json.getString("responseCode"));
			       
        } catch (Exception e) {
			e.printStackTrace();
		}
	}

}
