package com.ifp.services;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.text.ParseException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
//import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.dao.IsoFileGenDAO;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;

public class Reveral extends BaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5818830283737112271L;
 
	
	public String comInstId(){
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	} 
	
	CommonDesc commondesc = new CommonDesc();
	CommonUtil comutil = new CommonUtil();
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	
	
	
	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public CommonUtil getComutil() {
		return comutil;
	}

	public void setComutil(CommonUtil comutil) {
		this.comutil = comutil;
	}

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

 
	
	public JSONObject fullReversalTransction( String instid, String cardno, String refno, String traceno, String txnamount, String txncode, String terminalid, String terminalloc,
			String devicetype, String trandatetime, String acquireid, String merchantid, IsoFileGenDAO isodao, JdbcTemplate jdbctemplate, HttpServletRequest request, Socket connect_id  ) {
		 
		System.out.println("/************************ 	ISO COMMUNICATION BEGINS	******************************************************");
		 
		int acctcnt = 1;
		DataOutputStream out = null;
		DataInputStream in = null;
		
		JSONObject jsonresp = new JSONObject(); 
		String PROCESSCODE = "FULLREV";
		try {   
			
			String isomapkey = isodao.getIsoMapKey(instid, "420", PROCESSCODE, jdbctemplate);
			
			isodao.log("/************************ 	ISO COMMUNICATION BEGINS ["+PROCESSCODE+"]	******************************************************");
			
			String relativeWebPath = "/WEB-INF/StaticFiles/basic.xml";
			String absoluteDiskPath = request.getSession().getServletContext().getRealPath(relativeWebPath); 
			GenericPackager packager = new GenericPackager( absoluteDiskPath );  
			ISOMsg isomsg = new ISOMsg();
			isomsg.setPackager(packager);  
			 
			 
			 
			String datetime = "";
			Map isokeyvalmap = isodao.getIsoFieldSet(instid, isomapkey, jdbctemplate );
		 
			if(  isokeyvalmap != null ){
				String[] keys =  (String[])( isokeyvalmap.keySet().toArray( new String[isokeyvalmap.size()] ) );
				int error = 0; 
				System.out.println("CONFIGURED ISO LENGTH--"+keys.length);
				
				for( int i=0; i<keys.length; i++ ){
					String isovalue = (String) isokeyvalmap.get(keys[i]); 
					int isokey = Integer.parseInt(keys[i]);
					System.out.println( " ******************\n");
					if( isovalue.equals("MTI")){
						System.out.println( "isovalue_" + isovalue );
						isomsg.setMTI("0420");
						continue;
					}
					
					if( isovalue.equals("INSTID")){
						System.out.println( "isovalue_" + isovalue ); 
						isomsg.set(isokey, instid);
						System.out.println("SET KEY : " + isokey + "-VALUE :" +instid);
						isodao.log("KEY : " + isokey + "-VALUE :" +instid);
						continue;
					}
					
					if( isovalue.equals("CARDNO")){
						System.out.println( "isovalue_" + isovalue ); 
						isomsg.set(isokey, cardno);
						System.out.println("SET KEY : " + isokey + "-VALUE :" +cardno);
						isodao.log("KEY : " + isokey + "-VALUE :" +cardno);
						continue;
					} 
					
					if( isovalue.equals("TXNCODE")){
						System.out.println( "isovalue_" + isovalue ); 
						isomsg.set(isokey, txncode);
						System.out.println("SET KEY : " + isokey + "-VALUE :" +txncode);
						isodao.log("KEY : " + isokey + "-VALUE :" +txncode);
						continue;
					}
					
					if( isovalue.equals("REFNO")){  
						System.out.println( "isovalue_" + isovalue); 
						isomsg.set(isokey, refno);
						System.out.println("SET KEY : " + isokey + " -VALUE :" +refno);
						isodao.log("KEY : " + isokey + "-VALUE :" +refno);
						continue;
					}
					
					
					if( isovalue.equals("TRANDATETIME")){
						System.out.println( "isovalue_" + isovalue ); 
						isomsg.set(isokey, trandatetime);
						System.out.println("SET KEY : " + isokey + "-VALUE :" +trandatetime);
						isodao.log("KEY : " + isokey + "-VALUE :" +trandatetime);
						continue;
					}
					
					if( isovalue.equals("TRANNO")){
						System.out.println( "isovalue_" + isovalue );  
						isomsg.set(isokey, traceno); 
						System.out.println("SET KEY : " + isokey + "-VALUE :" +traceno);
						isodao.log("KEY : " + isokey + "-VALUE :" +traceno);
						continue;
					}  
					
					if( isovalue.equals("TERMINAL")){ 
						System.out.println( "isovalue_" + isovalue );  
						isomsg.set(isokey, terminalid);
						System.out.println("SET KEY : " + isokey + "-VALUE :" +terminalid);
						isodao.log("KEY : " + isokey + "-VALUE :" +terminalid);
						continue;
					} 
					
					if( isovalue.equals("LOCATION")){ 
						System.out.println( "isovalue_" + isovalue ); 
						String location = isodao.getLocation();
						isomsg.set(isokey, location);
						System.out.println("SET KEY : " + isokey + " -VALUE :" +location);
						isodao.log("KEY : " + isokey + "-VALUE :" +location);
						continue;
					} 
					
				  
					if( isovalue.equals("TXNAMOUNT")){  
						System.out.println( "isovalue_" + isovalue); 
						isomsg.set(isokey, txnamount);
						System.out.println("SET KEY : " + isokey + " -VALUE :" +txnamount);
						isodao.log("KEY : " + isokey + "-VALUE :" +txnamount);
						continue;
					}  
					
					if( isovalue.equals("DEVICETYPE")){  
						System.out.println( "isovalue_" + isovalue); 
						isomsg.set(isokey, devicetype);
						System.out.println("SET KEY : " + isokey + " -VALUE :" +devicetype);
						isodao.log("KEY : " + isokey + "-VALUE :" +devicetype);
						continue;
					} 
					
					if( isovalue.equals("MERCHANTID")){  
						System.out.println( "isovalue_" + isovalue); 
						isomsg.set(isokey, merchantid);
						System.out.println("SET KEY : " + isokey + " -VALUE :" +merchantid);
						isodao.log("KEY : " + isokey + "-VALUE :" +merchantid);
						continue;
					} 
					
					
					if( isovalue.equals("ACQBIN")){  
						System.out.println( "isovalue_" + isovalue); 
						isomsg.set(isokey, acquireid);
						System.out.println("SET KEY : " + isokey + " -VALUE :" +acquireid);
						isodao.log("KEY : " + isokey + "-VALUE :" +acquireid);
						continue;
					}
					
					
				} 
			} else{
				jsonresp.put("INTERRESP", 1);
		    	jsonresp.put("INTERRESPVAL", "No iso key and value configured.");
		    	trace("No key value pair configured for [ "+PROCESSCODE+" ] table [ IFP_ISO8583_MAP ]  ");
		    	return jsonresp;
			}
			
		 
			 System.out.println("********** ISO MESSAGE ***************** " );
			
			 System.out.println("Message is : " + isomsg );
			
			 out = new DataOutputStream (connect_id.getOutputStream());
			 byte[] data = isomsg.pack();
			 String isomsg_str = new String(data);
			 out.writeUTF(isomsg_str);
			 System.out.println("MESSAGE SEND SUCCESFULLY." + isomsg_str);
			 isodao.log("MESSAGE SEND SUCCESFULLY." + isomsg_str);
			 System.out.println("WAITING FOR RESPONSE...........");
			 in = new DataInputStream (connect_id.getInputStream());
			 
			
			 out.flush();
			 System.out.println("Recieved ....." + in);
			 String responsemsg = in.readUTF();  
			 System.out.println("RESPONSE RECEIVED SUCCESFULLY." + responsemsg);
			 isodao.log("RESPONSE RECEIVED SUCCESFULLY." + responsemsg);
			 System.out.println("data__" + data );
			 System.out.println("isomsg__" + isomsg );
			 System.out.println("isomsg_str_" + isomsg_str );
			 
			  
			 isomsg.unpack(responsemsg.getBytes());
			 System.out.println( "39 nth bit: " + isomsg.getString(39));
			 
			 System.out.println( "0 th bit: " + isomsg.getString(0));
			 
			 if( isomsg.getString(0).equals("0430") ){
				 String resp = isodao.responseISO(isomsg);
				 System.out.println( "response : " + resp );
					String respnosestring = getText(resp);
					String respdesc = "";
					if( Integer.parseInt( isomsg.getString(11) ) == Integer.parseInt(traceno) ){
						  respdesc = isodao.getReponseDescription(isomsg.getString(39), jdbctemplate);
						  isodao.log("Response :" + respdesc);
						if( Integer.parseInt( isomsg.getString(39) ) == 0 ){
							jsonresp.put("INTERRESP", 0);
					    	jsonresp.put("INTERRESPVAL", respnosestring); 
					    	System.out.println( "Response code is " + resp);
							jsonresp.put("HOSTRESPCODE", resp); 
						}else{
							jsonresp.put("INTERRESP", 1);
							
					    	jsonresp.put("INTERRESPVAL",  respdesc );
						}
					}else{
						jsonresp.put("INTERRESP", 1);
				    	jsonresp.put("INTERRESPVAL", respdesc ); 
					}
					
					//getResponse().getWriter().write(jsonresp.toString());
					return jsonresp;  
					
			 }else{
				 jsonresp.put("INTERRESP", 1);
		    	 jsonresp.put("INTERRESPVAL", "Unable to process..."); 
		    	 //getResponse().getWriter().write(jsonresp.toString());
				 return jsonresp; 
			 }
			  
		} 
		catch( ISOException ie ){
			System.out.println("EXECPTION : " +ie.getMessage());
			ie.printStackTrace();
			isodao.log("EXECPTION : " +ie.getMessage());
			jsonresp.put("INTERRESP", 1);
	    	jsonresp.put("INTERRESPVAL", "ISO Exception....Unable to process"); 
		} catch (Exception e) { 
			System.out.println("EXECPTION : " +e.getMessage());
			e.printStackTrace();
			isodao.log("EXECPTION : " +e.getMessage());
			jsonresp.put("INTERRESP", 1);
	    	jsonresp.put("INTERRESPVAL", "Unable to processs"); 
			    
		}  
		System.out.println("/**************************** 	ISO COMMUNICATION END	*********************************************************");
		return jsonresp;
	}
	
	 
}
