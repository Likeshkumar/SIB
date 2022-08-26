package com.ifp.personalize;

import java.awt.List;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;



import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.util.Logger;
import org.jpos.util.SimpleLogListener;

public class PersonalCardActivationAction extends BaseAction
{
	private static final long serialVersionUID = 1L;

	
	
	 
	CommonDesc commondesc = new CommonDesc();

	  
	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	public String comInstId(){
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	}
	
	public String cardActivationhome1()
	{
		SampleList();
		
		return "cardactivationhome";
	}
	
	
	@SuppressWarnings("null")
	public String cardActivationhome()
	{
		
		return "cardactivationhome";
	}
	public void logISOMsg(ISOMsg msg) 
	{
		System.out.println("----ISO MESSAGE-----");
		try 
		{
			//System.out.println("  MTI : " + msg.getMTI());
			for (int i=1;i<=msg.getMaxField();i++) 
			{
				//System.out.println("I Value is====> "+i);
				if (msg.hasField(i)) {
					System.out.println("    Field-"+i+" : "+msg.getString(i));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			System.out.println("--------------------");
		}
 
	}
	
	public String reponseCheck(ISOMsg responseMsg){
		System.out.println("  RESPONSE CHECK STARTS ");
		String response = "-1";
		try{
			
			for (int i=1;i<=responseMsg.getMaxField();i++) 
			{
				//System.out.println("I Value is====> "+i);
				if (responseMsg.hasField(i)) {
					//System.out.println("    Field-"+i+" : "+responseMsg.getString(i));
					if(i==39)
					{
						String res = responseMsg.getString(i);
						//System.out.println("Response Code----> "+res);
						if(res.equals("00")){
							response = "00";
						}
						else{
							response = res;
						}
					}
				}
			}
		}catch (Exception e) {
			System.out.println(" Catch----> "+e.getMessage());
		}
		System.out.println("  RESPONSE CHECK ENDS ");
		return response;
	}
	
	
	public String response(ISOMsg isoresponseMsg)
	{
		String resp = null;
		resp = isoresponseMsg.getString(39);
		System.out.println("The Response Is===>  "+resp);
		return resp;
	}
	
	
	public Socket ConnectingHSM(String IPaddress,int port,int hsmTimeout)
	{
		Socket  socket=null;
		try 
		{
			socket = new Socket(IPaddress,port);
		}
		catch (Exception e) 
		{
			System.out.println( "connection is 2  " + socket );
			System.out.println("Socket Creation Excetiopjk-=========> "+e.getMessage());
			return socket;
			//session.setAttribute("curerr", "E");
			//session.setAttribute("curmsg", "Socket Creation Excetion" + e.getMessage()); 
		}
		
		System.out.println("Socket Is Returns is ======####====> "+socket);
		return socket;
	}
	
	public String cardActivationhome3(){
		
		try{
			//Logger logger = new Logger();
			//logger.addListener (new SimpleLogListener(System.out));

		ISOMsg isoMsg = parseISOMessage();
		printISOMessage(isoMsg);
		}
		catch (Exception e) {
			System.out.printf("Exception in unpaking"+e.getMessage());
		}
		
			
		return "cardactivationhome";
		
	}
	
    public ISOMsg parseISOMessage() throws Exception {
        //String message = "0210BE2C00000A100000000000000080000001001000000001000000000001020000000001500011072218064512345620021320041234567890120A5DFGR1234567890sdfrgref021ABCDEFGHIJ 1234567890";
    	//String message = "0210BE2C00000A0000000000000000800000010010000000010000000000010200000000015000110722180645123456200213200412345678901200021ABCDEFGHIJ 1234567890";
    	//String message="0210BE2C00000A100000000000000080000001001000000001000000000001020000000001500011072218064512345620021320041234567890120A5DFGR1234567890sdfrgref021ABCDEFGHIJ 1234567890";
    	//String message="0200F23AC00108E080180000000004000008166047870001030814010000000000002000100112341641806312341610011001100160110660478712741241806310010001      GHANA          NIBGHANA                                28808920^123416^ 10000^78^^6047870001030814=12085010000000000000^GHANA                    ^1^1^1310001009816010166047870001030814";
    	String message="0200722C0000080000181660478700010308140030000000000040001107221806451234562002132004123456789012016201120112011201108920^123416^ 10000^78^^6047870001030814=12085010000000000000^GHANA                    ^1^1^";
    	
        System.out.printf("Message = %s%n", message);
        try {
        	GenericPackager packager = new GenericPackager("D://DECEMBER_IFPSETUP//IFPSETUP_20Dec2012//WebContent//WEB-INF//basic.xml");
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setPackager(packager);
            
            isoMsg.unpack(message.getBytes());
            return isoMsg;
        } catch (ISOException e) {
            throw new Exception(e);
        }
    }
    public void printISOMessage(ISOMsg isoMsg) {
        try {
            System.out.printf("MTI = %s%n", isoMsg.getMTI());
            for (int i = 1; i <= isoMsg.getMaxField(); i++) {
                if (isoMsg.hasField(i)) {
                    System.out.printf("Field (%s) = %s%n", i, isoMsg.getString(i));
                }
            }
        } catch (ISOException e) {
            e.printStackTrace();
        }
    }
	public void SampleList()
	{
		
		List listone= new List();
		String s1 = new String("Sankar1");
		String s2 = new String("Sankar2");
		String s3 = new String("Sankar3");
		String s4 = new String("Sankar4");
		String s5 = new String("Sankar5");
		listone.add(s1);
		listone.add(s2);
		listone.add(s3);
		listone.add(s4);
		listone.add(s5);
	
		System.out.println("1 The List is ======> "+listone);
		ArrayList<Integer> arrlist = new ArrayList<Integer>();
		arrlist.add(0);
		arrlist.add(1);
		arrlist.add(2);
		arrlist.add(3);
		System.out.println("1 The New Array List is---->"+arrlist);
		arrlist.remove(0);
		System.out.println("2 The New Array List is---->"+arrlist);
		arrlist.remove("San3");
		System.out.println("3 The New Array List is---->"+arrlist);
	}
	
	public String cardActivationhome4() throws Exception{
		
		GenericPackager packager = new GenericPackager("D://DECEMBER_IFPSETUP//IFPSETUP_20Dec2012//WebContent//WEB-INF//basic.xml");
		ISOMsg isomsg = new ISOMsg();
		isomsg.setPackager(packager);
		isomsg.setMTI("0200");
		isomsg.set(2,"6047870001030814");
		isomsg.set(3,"003000");
		isomsg.set(4,"004000");
		isomsg.set(7,"110722180645");
		isomsg.set(11,"123456");
		isomsg.set(13,"200213");
		isomsg.set(14,"2004");
		isomsg.set(37,"123456789012");
		isomsg.set(44,"Sankaranarayana 121121212121212");
		isomsg.set(60,"2011201120112011");
		isomsg.set(61,"20^123416^ 10000^78^^6047870001030814=12085010000000000000^GHANA                    ^1^1^2");
		byte[] data = isomsg.pack();
		String isosg = new String(data);
		System.out.println("RESULT : " + isosg);
		
		int s= isomsg.unpack(isosg.getBytes());
		System.out.println("Unpack Message =====> "+s);
		logISOMsg(isomsg);
		return "cardactivationhome";
	}
}
