package test;

import java.io.*;  
import java.net.*;  
public class JavaSocketConnectExample2 {  
public static void main(String[] args) {  
try{      
Socket s=new Socket("10.93.101.118",1500);  
DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
dout.writeUTF("Hello Server");  
dout.flush();  
dout.close();  
s.close();  
}catch(Exception e){
	e.printStackTrace();System.out.println(e);}  
}  
}  