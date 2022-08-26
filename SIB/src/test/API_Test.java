package test;

import org.json.JSONArray;
import org.json.JSONObject;

public class API_Test {
	

	public static void main(String[] args) {
		JSONObject jsonObj=new JSONObject();
		
		jsonObj.put("fullBban", "005001200385290117");
		jsonObj.put("accountDescription", "LYDIA ANN BREHUN");
		jsonObj.put("accountType", "SAVINGS ACCOUNT");
		jsonObj.put("currency", "840");
		jsonObj.put("customerNumber", "656565");
		jsonObj.put("accountBranch", "001");
	
		
		
		JSONObject j=new JSONObject();
		j.put("data", jsonObj);
		j.put("responseCode", "000");
		j.put("message", "Account is available");
		
		
		/*JSONArray arr=new JSONArray();
		arr.put(jsonObj);
		
		j.put("", arr);
*/		
		
		
		System.out.println("arra ::: "+j);
		
	}
	
}
