package com.ifp.dao;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jpos.iso.ISOMsg;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;


@Transactional
public class IsoFileGenDAO extends BaseAction{
	
	public void log(String message){
		//System.out.println( "log msg :" +message);
		try {
			 
			Properties prop = getCommonDescProperty();
			String strfiledir = prop.getProperty("service.logdir");   
			String logfilename = "ISOCOM_"+getDate()+".log";// getText("service.logfilename");
			
			File nwfile = new File( strfiledir+"/"+logfilename); 
			File logfiledir = new File( strfiledir );
			if( logfiledir.exists() ){ 
				if(!nwfile.exists()) {
					nwfile.createNewFile();
				}
			}else{
				System.out.println( "COULD NOT UNDERSTAND THE LOG FILE PATH SPECIEFIED [ "+strfiledir+" ] ");
			}
			
			 
			FileWriter fstream = new FileWriter(nwfile,true);
			BufferedWriter prewriter = new BufferedWriter(fstream);
			prewriter.write( getDateTimeStamp() + ":" + message + "\n"); 
			prewriter.close();
		} catch (IOException e) { 
			e.printStackTrace();
		} 

	}
	
	public String getDateTimeStamp()
	{
		Date date = new Date();
		 DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh_m_ss");
		String today_date = dateFormat.format(date);
		return today_date;
	}
	
	
	public String getDate()
	{
		Date date = new Date();
		 DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		String today_date = dateFormat.format(date);
		return today_date;
	}
	
	 
	
	public String paddingZero(String orignalvalue, int strlen){
		String val = orignalvalue.toString();
		
		String formattedresult = org.apache.commons.lang.StringUtils.leftPad(val, strlen, '0');
		return formattedresult; 
	}
	
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
		String isovalqry = "SELECT ISO_KEY, ISO_VALUE FROM IFP_ISO8583_MAP WHERE INST_ID='"+instid+"' AND ISOMAPKEY='"+pcode+"'  AND ISO_STATUS='1' ORDER BY ISO_KEY ASC";
		System.out.println( "isovalqry : " +isovalqry);
		List isokeyval = jdbctemplate.queryForList(isovalqry);
		if( !isokeyval.isEmpty() ){
			Iterator itr = isokeyval.iterator();
			
			while( itr.hasNext() ){
				Map mp = (Map) itr.next();
				Object isokey = (Object)mp.get("ISO_KEY");
				String isovalueobj =(String)mp.get("ISO_VALUE");
				hmap.put(isokey.toString(), isovalueobj);
			}
		}else{
			hmap = null;
		}
		return hmap;
	}
	
		
	 
	
	public String getLocaldatetime(String flag)
	{
		String datetime = null;
		String formatflag="";
		if(flag.equals("DT"))
		{
			formatflag = "ddMMyyHHmmss";
		}
		if(flag.equals("D"))
		{
			formatflag = "ddMMyy";
		}
		if(flag.equals("T"))
		{
			formatflag = "HHmmss";
		}
		DateFormat dateFormat = new SimpleDateFormat(formatflag);
		Date date = new Date();
		datetime = dateFormat.format(date); 
		return datetime;
	}
	
	public String getTransactionNumber() {
		return "111111";
	}

	public String getIpAddress() {
		String ipaddress = "140.0.0.106";
		return ipaddress;
	}

	public String getLocation() {
		String location = "REMITTANCE-PORTAL";
		return location;
	}
	
	public String getReponseDescription(String respcode, JdbcTemplate jdbctemplate){
		String respdesc = respcode;
		
		try {
			String respdescqry = "SELECT DESCRIPTION FROM IFP_RESPCODE WHERE RESPCODE='"+respcode+"'";
			enctrace("respdescqry :" + respdescqry );
			respdesc = (String)jdbctemplate.queryForObject(respdescqry, String.class);
		} catch (DataAccessException e) {  }
		
		return respdesc;
	}
	
	
	public String getIsoMapKey(String instid, String msgtype, String actioncode, JdbcTemplate jdbctemplate ) throws Exception {
		String isokeymap = null;
		try{
			String isokeyqry = "SELECT ISOMAPKEY FROM IFP_ISO8583_CONFIG WHERE MSGTYPE='"+msgtype+"' AND TXNCODE='"+actioncode+"' AND DEVICETYPE='FEND'";
			isokeymap = (String)jdbctemplate.queryForObject(isokeyqry, String.class);
		}catch(EmptyResultDataAccessException e){}
		return isokeymap ; 
	}
}
