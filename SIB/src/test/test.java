package  test;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class test {

public static void main(String[] args) throws JSONException, ParseException {
// TODO Auto-generated method stub

test u=new test();
JSONObject jsonObj = u.example();

System.out.println(jsonObj);

JSONArray jArray = jsonObj.getJSONArray("messages");


System.out.println("arra ******" +jArray);


for(int i=0; i<jArray.length(); i++)
{
JSONObject jObj = jArray.getJSONObject(i);
String mobileno = jObj.optString("to");
JSONObject responsestatus=new JSONObject(jObj.optString("status").toString());
String grpname=responsestatus.getString("groupId");
System.out.println("key--->"+grpname);
}

}
public JSONObject example() throws JSONException {

JSONObject obj = new JSONObject();
JSONObject obj1 = new JSONObject();

obj1.put("groupId", "1");
obj1.put("groupName", "PENDING");
obj1.put("id", "26");
obj1.put("name", "PENDING_ACCEPTED");
obj1.put("description", "1970-01-01T00:00:00");



obj.put("to", "231777991125");
obj.put("status", obj1);
obj.put("messageId","30758493332503597791");

//System.out.println("arr111 ::: "+obj);
//System.out.println("arr 123 ::: "+obj1);


//JSONArray jArray = obj1.getJSONArray("status");

//obj.put("status", "MAWME SANDRA");


JSONArray arr = new JSONArray();

arr.put(obj);

JSONObject obj2 = new JSONObject();
obj2.put("messages", arr);

//System.out.println("arr 1234 ::: "+arr);
return obj2;
}

}