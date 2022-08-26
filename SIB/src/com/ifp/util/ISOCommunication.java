package com.ifp.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.jpos.iso.ISOMsg;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.util.CommonUtil;

public class ISOCommunication {
	
	 
	 
	
	
	public Socket connectHost(String ipaddress, int port, int timeout){
		Socket  socket=null;
		try  {
			
			socket =  new Socket(ipaddress,port);
			socket.setSoTimeout(timeout);
		}
		catch (Exception e)  {
			System.out.println( "connection is  " + socket );
			System.out.println("Socket Creation Excetiopjk-=========> "+e.getMessage());
			return socket; 
		} 
		System.out.println("Socket Is Returns is ======####====> "+socket);
		return socket;
	}
	
	
	
	
	public String responseISO(ISOMsg isoresponseMsg)
	{
		String resp = null;
		resp = isoresponseMsg.getString(39);
		System.out.println("The Response Is===>  "+resp);
		return resp;
	}

	public Map getIsoFieldSet(String instid, String pcode, JdbcTemplate jdbctemplate) {
		HashMap<String, String> hmap = new HashMap<String, String>();
		
		String isovalqry = "SELECT ISO_KEY, ISO_VALUE FROM IFP_ISO8583_MAP WHERE"
			+ " INST_ID='"+instid+"' AND TXN_CODE='"+pcode+"'  AND ISO_STATUS='1' ORDER BY ISO_KEY ASC";
		List isokeyval = jdbctemplate.queryForList(isovalqry);
		if( !isokeyval.isEmpty() ){
			Iterator itr = isokeyval.iterator();
			
			while( itr.hasNext() ){
				Map mp = (Map) itr.next();
				Object isokey = (Object)mp.get("ISO_KEY");
				String isovalueobj =(String)mp.get("ISO_VALUE");
				hmap.put(isokey.toString(), isovalueobj);
			}
		}
		return hmap;
	}
	
	
}
