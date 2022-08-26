package com.ifp.iso;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

import com.ifp.Action.BaseAction;

public class IsoFileGenDAO extends BaseAction{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void log(String message){
		//System.out.println( "log msg :" +message);
		
		
		try {
			 
			Properties prop = getCommonDescProperty();
			String strfiledir =prop.getProperty("service.logdir");   
			String logfilename = "ISOAUTO_"+getDate()+".log";// getText("service.logfilename");
			
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
	
	public Map getIsoFieldSet(String instid, String pcode, JdbcTemplate jdbctemplate) {
		HashMap<String, String> hmap = new HashMap<String, String>(); 
		String isovalqry = "SELECT ISO_KEY, ISO_VALUE FROM IFP_ISO8583_MAP WHERE INST_ID='"+instid+"' AND ISOMAPKEY='"+pcode+"'  AND ISO_STATUS='1' ORDER BY ISO_KEY ASC";
		enctrace( "isovalqry : " +isovalqry);
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
	
	
	public String getIsoMapKey(String instid, String msgtype, String actioncode, Connection con) throws Exception { 
		String isokeymap = null;
		ResultSet rs = null;PreparedStatement prp = null;
		try{
			String isokeyqry = "SELECT ISOMAPKEY FROM IFP_ISO8583_CONFIG WHERE MSGTYPE='"+msgtype+"' AND TXNCODE='"+actioncode+"' AND DEVICETYPE='FEND'";
			enctrace( "isokeyqry : " +isokeyqry);
			prp = con.prepareStatement(isokeyqry);
			rs = prp.executeQuery();
			if( !rs.next() ){
				isokeymap = null;
			}else{
				do{
					isokeymap = rs.getString("ISOMAPKEY");
				}while( rs.next() );
			}
		}catch(EmptyResultDataAccessException e){}
		finally{
			if( rs != null ){ rs.close();}
			if( prp != null ){ prp.close();} 
		}
		return isokeymap ; 
	}
	
	
	public String getIsoMapKey(String instid, String msgtype, String actioncode, JdbcTemplate jdbctemplate) throws Exception { 
		String isokeymap = null; 
		try{
			String isokeyqry = "SELECT ISOMAPKEY FROM IFP_ISO8583_CONFIG WHERE MSGTYPE='"+msgtype+"' AND TXNCODE='"+actioncode+"' AND DEVICETYPE='FEND'";
			enctrace( "isokeyqry : " +isokeyqry); 
			isokeymap = (String)jdbctemplate.queryForObject(isokeyqry, String.class);
		}catch(EmptyResultDataAccessException e){} 
		return isokeymap ; 
	}
	
	
	
	public Map getIsoFieldSet(String instid, String isomapkey, Connection con) throws Exception {
		ResultSet rs = null;PreparedStatement prp = null;
		HashMap<String, String> hmap = new HashMap<String, String>(); 
		String isovalqry = "SELECT ISO_KEY, ISO_VALUE FROM IFP_ISO8583_MAP WHERE INST_ID='"+instid+"' AND ISOMAPKEY='"+isomapkey+"'  AND ISO_STATUS='1' ORDER BY ISO_KEY ASC";
		enctrace( "isovalqry : " +isovalqry);
		prp = con.prepareStatement(isovalqry);
		rs = prp.executeQuery();
		if( !rs.next() ){
			return null;
		}else {		 
			String isokey="",isovalueobj="";
			do{ 
				isokey = rs.getString("ISO_KEY");
				isovalueobj =rs.getString("ISO_VALUE");
				hmap.put(isokey.toString(), isovalueobj);
			}while( rs.next() );			  
		}
		if( rs != null ){ rs.close();}
		if( prp != null ){ prp.close();} 
		return hmap;
	}
	
	
	public String responseISO(ISOMsg isoresponseMsg)
	{
		String resp = null;
		resp = isoresponseMsg.getString(39);
		System.out.println("The Response Is===>  "+resp);
		return resp;
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
		String location = "BPESA-PORTAL";
		return location;
	}
	
	
	public String getReponseDescription(String respcode, Connection con) throws Exception {
		String respdesc = respcode;
		ResultSet rs = null;
		PreparedStatement prp = null;
		try {
			String respdescqry = "SELECT DESCRIPTION FROM IFP_RESPCODE WHERE RESPCODE='"+respcode+"'";
			prp = con.prepareStatement(respdescqry);
			rs = prp.executeQuery();
			if( !rs.next()){
				return respdesc;
			}else{
				do{
					respdesc = rs.getString("DESCRIPTION");
				}while( rs.next() );
			}
		} catch (Exception e) {}
		finally{
			if( rs !=null )
				rs.close();
			if( prp != null )
				prp.close();
		} 
		return respdesc;
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


	public void printResponse(ISOMsg isomsg) {
		this.log("Parsing Response");
		for( int i =0; i<=128; i++ ) {
			if( isomsg.hasField(i)){
				this.log( "Field :"+i+ " <"+isomsg.getString(i)+">" );
			}
		}  
	}
	
	
}
