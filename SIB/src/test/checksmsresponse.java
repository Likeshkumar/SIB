package test;

import java.text.ParseException;
import java.util.Iterator;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.json.JSONArray;
import org.json.JSONObject;

import net.sf.json.JSONException;
import net.sf.json.JSONSerializer;

public class checksmsresponse {
	
	public static void main(String[] args) throws JSONException, ParseException {
		// TODO Auto-generated method stub

		
		String conn="0";
		if(conn.contains("0")) {System.out.println("if");}else{System.out.println("else");}
				 
		
		checksmsresponse u=new checksmsresponse();
		JSONObject jsonObj = u.example();

		System.out.println("1111"+jsonObj);
		//JSONObject obj1 = new JSONObject();
		//obj1.put("status", obj1);
		//System.out.println("obj1-->"+obj1);
		
		JSONArray jArray = jsonObj.getJSONArray("messages");
         System.out.println("jArray---->"+jArray);
         for(int i = 0; i < jArray .length(); i++)
         {
        	 JSONObject status=new JSONObject();
            JSONObject object3 = jArray.getJSONObject(i);
            String to = object3.getString("to");
            String messageid = object3.getString("messageId");
            String status1 = object3.getString("status");
            System.out.println("to ******" +to);
            System.out.println("messageid ******" +messageid);
            System.out.println("status1 ******" +status1);}
         
         
            
            //jsonObj.put(object3, "status");
            
         

		//System.out.println("arra ******" +jArray);


		/*for(int i=0; i<jArray.length(); i++)
		{
		JSONObject jObj = jArray.getJSONObject(i);
		String mobileno = jObj.optString("to");
		// Array responsestatus=jObj.getString("status");
		// String responsestatus=jObj.optString("status");
		JSONObject responsestatus=new JSONObject(jObj.optString("status").toString());
		String responsestatus1=jObj.optString("groupName");

		String messageid=jObj.optString("messageId");

		Iterator itr = responsestatus.keys();

		while(itr.hasNext()){
		String key = (String) itr.next();
		System.out.println(key + " "+responsestatus.getString(key));

		}

*/
		//System.out.println("acctId ******" +mobileno);
		//System.out.println("accCurenccy ******" +responsestatus);
		//System.out.println("message id ******" +messageid);
		// System.out.println("responsestatus1 id ******" +responsestatus1);




		//}

		}
		public JSONObject example() throws JSONException, ParseException {

		JSONObject obj = new JSONObject();
		JSONObject obj1 = new JSONObject();

		
		obj.put("status", obj1);
		obj.put("messageId","30758493332503597791");
		obj.put("to", "231777991125");
		
		obj1.put("groupId", "1");
		obj1.put("groupName", "PENDING");
		obj1.put("id", "26");
		obj1.put("name", "PENDING_ACCEPTED");
		obj1.put("description", "Message sent to next instance");
		

		System.out.println("obj ::: "+obj);
		System.out.println("obj1 ::: "+obj1);


		JSONArray jArray= new JSONArray();
		jArray.put(obj1);
		
		 String groupid = obj1.getString("groupId");
		 System.out.println("------groupid--------"+groupid);
		
		 System.out.println("--------------");
		 String jsonStr = "{\"name\":\"SK\",\"arr\":{\"a\":\"1\",\"b\":\"2\"}}";
	        JSONObject jsonObj = new JSONObject(jsonStr);
	        String name = jsonObj.getString("name");
	        System.out.println(name);

	        String first = jsonObj.getJSONObject("arr").getString("a");
	        System.out.println(first);

			 System.out.println("--------------");
		
		//System.out.println("jArray");

		//obj.put("status", "MAWME SANDRA");
   
		System.out.println("jArray ::: "+jArray);

		JSONArray arr = new JSONArray();

		arr.put(obj);

		JSONObject obj2 = new JSONObject();
		obj2.put("messages", arr);

		
		//System.out.println("arr 1234 ::: "+arr);
		return obj2;
		}

		}

