package com.ifp.iso;

import java.net.Socket; 
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;
import com.ifp.iso.CommonUtil;
import com.ifp.iso.IsoFileGenDAO;
import com.ifp.iso.ServiceBeans;

import com.ifp.iso.IsoService;

public class RequestSender extends BaseAction{ 
	 
	private static final long serialVersionUID = 1L;

	public String reverseTransaction( String instid, String iftxncode, String cardno, String refno, String terminalid, JdbcTemplate jdbctemplate ) throws Exception {
		IsoFileGenDAO isodao = new IsoFileGenDAO();
		ServiceBeans servicebean = new ServiceBeans();
		JSONObject jsonresp = new JSONObject();
		IsoService isosender = new IsoService();
		CommonUtil comutil = new CommonUtil();
		Socket connect_id = null; 
		try{
			   
			
			servicebean.setMti("420");
			servicebean.setFromcardno(cardno);  
			servicebean.setRefereceno(refno);
			servicebean.setTxnamount("00");
			servicebean.setTerminallocation("WEB-APP");
			servicebean.setTxncode(iftxncode);
			servicebean.setDevicetype("FEND"); 
			servicebean.setTerminallocation("PREPAID");
			servicebean.setTerminalid(terminalid); 
			 
			
			isodao.log("Getting socket ");
			connect_id = comutil.getSocketConnection(instid, jdbctemplate);
			if( connect_id == null ){ 
				System.out.println("COULD NOT CONNECT HOST. CONNECTION TIMEOUT..");   
				isodao.log("COULD NOT CONNECT HOST. CONNECTION TIMEOUT.." );
				jsonresp.put("INTERRESP", 1);
				jsonresp.put("INTERRESPVAL", "Could not connect host..");
		    	return jsonresp.toString();
			} 
			//SENDING ISO MESSAGE....
			isodao.log("Sending iso request to host " );
			JSONObject jsoniso = isosender.sendingISOMessage(instid, servicebean.getTxncode(), servicebean, jdbctemplate, connect_id, isodao);
			isodao.log("Got response : " + jsoniso ); 
			if( jsoniso != null ){
			if( jsoniso.getInt("INTERRESP") == 0){
				jsonresp.put("INTERRESP", 0); 
				jsonresp.put("INTERRESPVAL", jsoniso.getString("INTERRESPVAL"));						
			}else{
				jsonresp.put("INTERRESP", 1);
				jsonresp.put("INTERRESPVAL", jsoniso.getString("INTERRESPVAL"));
			}  
			}else{
				jsonresp.put("INTERRESP", 1);
				jsonresp.put("INTERRESPVAL", "Unable To continue the process");
			}
			
		}catch(Exception e ){
			isodao.log("Exception " + e.getMessage() );
			e.printStackTrace();
			jsonresp.put("INTERRESP", 1);
			jsonresp.put("INTERRESPVAL", "Unable to process" );
		} finally{
			try{ connect_id.close(); isodao.log("Socket closed properly"); }catch(Exception e){}
		}
		
		return jsonresp.toString();
	}
	
	
	public int insertAutoFeeDebit( String instid, String ifptxncode, String fromcardno,  JdbcTemplate jdbctemplate ) throws Exception{
		int x = -1;
		String txnrefnum = fromcardno+getDateTimeStamp();
		String feedebitqry = "INSERT INTO IFPS_FUND_AUTOTRANSFER (INST_ID, ACTION_CODE, FROMENTITY, TXNDATE, TXNREFNUM  ) VALUES ('"+instid+"','"+ifptxncode+"','"+fromcardno+"', SYSDATE, '"+txnrefnum+"')";
		enctrace("feedebitqry :"+ feedebitqry);
		x = jdbctemplate.update( feedebitqry );
		return x;
	}
	
}
