package com.ifp.maintain;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

//import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;

import connection.SwitchConnection;

public class SearchTxn extends BaseAction {
	
	CommonDesc commondesc = new CommonDesc();
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
	public CommonDesc getCommondesc() {
		return commondesc;
	} 
	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	public String comInstId( HttpSession session  ){
		 
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	} 
	public String comUserCode( HttpSession session ){ 
		String instid = (String)session.getAttribute("USERID"); 
		return instid;
	}
	
	
	public String cardTxnSearch(){
		return "cardtxnsearch_home";
	}
	
	

	public void searchTransaction() throws IOException, SQLException{
		trace("************ searchTransaction ***********" );trace("********** searchTransaction ***********" );
		JSONObject json = new JSONObject();
		HttpSession session = getRequest().getSession();
		Connection conn = null;
		ResultSet rs = null;
		try{  
			String instid = comInstId( session ); 
			trace("instid-->"+instid);
			String selectedtype = getRequest().getParameter("selectedtype").trim();
			trace("selected type -->" + selectedtype);
			String searchtxnvalue=getRequest().getParameter("searchtxnvalue").trim();
			
			
			//String cardno = getRequest().getParameter("cardno").trim();
			//String txndate = getRequest().getParameter("txndate").trim();
			//String traceno = getRequest().getParameter("tracenoval").trim();
			//String txnrefno = getRequest().getParameter("txnrefno").trim();
			
			String keyid = "";
			String EDMK="", EDPK="";
			StringBuffer hcardno = new StringBuffer();
			String ecardno = new String();
			PadssSecurity padsssec = new PadssSecurity();
				
				keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
				//System.out.println("keyid::"+keyid);
				List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			//	System.out.println("secList::"+secList);  
				Iterator secitr = secList.iterator();
				Properties prop = null;
				if(!secList.isEmpty()){
					while(secitr.hasNext())
					{
						String CDMK = "";
						String CDPK = "";
						Map map = (Map) secitr.next(); 
						String eDMK = ((String)map.get("DMK"));
						String eDPK = ((String)map.get("DPK"));
						hcardno = padsssec.getHashedValue(searchtxnvalue+instid);
						prop = getCommonDescProperty();
						EDPK = prop.getProperty("EDPK");
						CDPK = padsssec.decryptDPK(eDMK, EDPK);
						ecardno = padsssec.getECHN(CDPK, searchtxnvalue);
					}      
					}
			 System.out.println("ecardno --->"+ecardno);
			String searchcond = "";
			if( selectedtype.equals("cardno")){
				searchcond += " TCHN = '"+hcardno+"'";
			}	
			if( selectedtype.equals("acctno")){
				searchcond += " FROMACCOUNT = '"+searchtxnvalue+"'";
			}
			if( selectedtype.equals("txndate")){ 
				searchcond += "  TRANDATE=TO_DATE('"+searchtxnvalue+"','dd-mm-yyyy')";
			}
			
			if( selectedtype.equals("traceno")){  
				searchcond += " TRACENO='"+searchtxnvalue+"'";
			}
			
			if( selectedtype.equals("txnrefno")){   
				searchcond += " REFNUM='"+searchtxnvalue+"'";
			}
			
			trace("search condition" + searchcond);
			//searchcond += "  ORDER BY TXNLOGSEQNO DESC";
			trace("Getting records....");
			
			SwitchConnection swhcon = new SwitchConnection();
			conn = swhcon.getConnection();

			int cnt = 0;
			String respcode="";
			String transdesc="";
			String reasoncode = "";
			
			searchcond = searchcond +"  AND MSGTYPE in ('210','420') AND TXNCODE NOT LIKE '36%' AND TXNCODE!='940000' "; 
			
			String txnqry = "SELECT CHN, TRACENO, TXNCODE, NVL(REFNUM,'--') as REFNUM,FROMACCOUNT,TERMINALID, ";
			txnqry += " to_char(TRANDATE,'dd-MON-yy ') || ' ' ||  (substr(lpad(trantime,6,0),0,2) || ':' || substr(lpad(trantime,6,0),3,2) || ':' ||  substr(lpad(trantime,6,0),5,2)) as TRANDATE ";
			txnqry +=", NVL(AMOUNT,'--') as AMOUNT, NVL(termloc,'--') as ACCEPTORNAME,acqcurrencycode,NVL(billingamount,'--') as billingamount," ;
			txnqry += " TO_CHAR(RESPCODE) as RESPCODE,TO_CHAR(REASONCODE) AS REASONCODE,TCHN  FROM (select * from  EZTXNLOG ";
			txnqry += " WHERE "+ searchcond+ " order by REFNUM desc)  where ROWNUM<=500";
			enctrace( "txnqry : " + txnqry);
			PreparedStatement ps = conn.prepareStatement(txnqry);
			rs = ps.executeQuery(txnqry);
			//System.out.println("rs value" + rs);
			
			if(rs.next()){
				do {
					JSONObject jsonnew = new JSONObject();
					json.put("RESP", 0);
					
					trace("card number "+rs.getString("CHN"));
					jsonnew.put("CHN", (String)rs.getString("CHN"));
					
					jsonnew.put("TCHN", (String)rs.getString("TCHN"));
					System.out.println("tchn...."+(String)rs.getString("TCHN"));
					
					String custnameqry = "SELECT NVL(ENC_NAME,'--') AS CUST_NAME FROM CARD_PRODUCTION WHERE ORG_CHN='"+ecardno+"'";
					System.out.println("rs value" + custnameqry);
					enctrace( "custnameqry : " + custnameqry);
					List<Map<String,Object>> custnamelist =  jdbctemplate.queryForList(custnameqry);
					String custname = "";
					if(custnamelist.isEmpty()){
						custname = "--";
					}else{
						custname = (String) custnamelist.get(0).get("CUST_NAME");
					}
					
					jsonnew.put("CUST_NAME",custname);
					System.out.println("CUST_NAME...."+custname);
					
					transdesc = rs.getString("TXNCODE");
					trace("transcode "+rs.getString("TXNCODE"));
					//String trans = getTransdesc(transdesc,conn);
					String txndesc = commondesc.getTransactionDesc(instid, transdesc, jdbctemplate);
					trace("Getting transaction desc [ "+transdesc+" ] ...got : " + txndesc);
					if( txndesc.equals("NOREC")){
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", "NO TRANSACTION DESCRIPTION FOUND FOR TXN CODE [ "+transdesc+" ] ");
						trace("NO TRANSACTION DESCRIPTION FOUND FOR TXN CODE [ "+transdesc+" ] ");
					}
					
					trace("trans desc "+txndesc);
					jsonnew.put("TXNDESC", txndesc);
					
					
					
					
					Object tracenoobj = (Object)rs.getInt("TRACENO");
					trace("trace number "+rs.getString("TRACENO"));
					jsonnew.put("TRACENO", tracenoobj.toString());
					
					//Object accountno = (String)rs.getString("FROMACCOUNT");
					//trace("trace number "+rs.getString("FROMACCOUNT"));
					jsonnew.put("FROMACCOUNT", (String)rs.getString("FROMACCOUNT"));
					jsonnew.put("TERMINALID", (String)rs.getString("TERMINALID"));
					
					
					trace("txn refnum " +rs.getString("REFNUM"));
					jsonnew.put("TXNREFNUM", rs.getString("REFNUM"));
					
					trace("tran date "+rs.getString("TRANDATE"));
					jsonnew.put("TRANDATE", (String)rs.getString("TRANDATE"));
					//jsonnew.put("TERMLOC", (String)rs.getString("TERMLOC"));
					trace("txn amount "+rs.getString("AMOUNT"));
					jsonnew.put("TXNAMOUNT", (String)rs.getString("AMOUNT"));
					
					trace("Accptor name "+rs.getString("ACCEPTORNAME"));
					jsonnew.put("ACCEPTORNAME", (String)rs.getString("ACCEPTORNAME"));
					
					trace("Acq Currence "+rs.getString("acqcurrencycode"));
					jsonnew.put("acqcurrencycode", (String)rs.getString("acqcurrencycode"));
					
					trace("billingamount "+rs.getString("billingamount"));
					jsonnew.put("billingamount", (String)rs.getString("billingamount"));
					
					
				    respcode = rs.getString("RESPCODE");
				    trace("resp code "+respcode);
				    String resp = getRespDesc(respcode,conn);
				    trace("resp description "+resp);
				    jsonnew.put("RESPCODE",resp);
				   
				    reasoncode = (String)rs.getString("REASONCODE");
				    trace("reason code "+reasoncode);
				    String reason = getReasonDesc(reasoncode,conn);
				    trace("reason desc"+reason);
				    jsonnew.put("REASONCODE",reason);
		
					json.put("SET"+cnt, jsonnew);
					cnt++;
				} while (rs.next());
				json.put("RECORDCNT", cnt);
				trace("Count of record "+cnt);
			}else{
				json.put("RESP", 1);
				json.put("REASON", "NO RECORDS FOUND");
			}
			
				/*while(rs.next()){
					trace("inside whilt");
					JSONObject jsonnew = new JSONObject();
					json.put("RESP", 0);
					
					trace("card number "+rs.getString("CHN"));
					jsonnew.put("CHN", (String)rs.getString("CHN"));
					
					transdesc = rs.getString("TXNCODE");
					String trans = getTransdesc(transdesc,conn);
					jsonnew.put("TXNDESC", trans);
					
					
					Object tracenoobj = (Object)rs.getInt("TRACENO");					
					jsonnew.put("TRACENO", tracenoobj.toString());
					
					
					jsonnew.put("TXNREFNUM", rs.getString("REFNUM"));
					
					jsonnew.put("TRANDATE", (String)rs.getString("TRANDATE"));
					//jsonnew.put("TERMLOC", (String)rs.getString("TERMLOC"));
					jsonnew.put("TXNAMOUNT", (String)rs.getString("AMOUNT"));
					jsonnew.put("ACCEPTORNAME", (String)rs.getString("ACCEPTORNAME"));
					
					
				    respcode = rs.getString("RESPCODE");
				    String resp = getRespDesc(respcode,conn);
				    jsonnew.put("RESPCODE",resp);
				   
				    reasoncode = (String)rs.getString("REASONCODE");
				    String reason = getReasonDesc(reasoncode,conn);
				    jsonnew.put("REASONCODE",reason);
		
					json.put("SET"+cnt, jsonnew);
					cnt++;
				}
				
				json.put("RECORDCNT", cnt);
				trace("Count of record "+cnt);*/
		}catch(Exception e ){
			json.put("RESP", 1);
			json.put("REASON", "Exception : Couldn not continue the process ");
			trace("Exception : " + e.getMessage() );
			e.printStackTrace();
		}finally{
			rs.close();
			conn.close();
			trace("Connection closed");
		}
		trace("\n\n");enctrace("\n\n");	
		getResponse().getWriter().write(json.toString());
	}

	
	public List searchPLTxnContent(String instid, String cardno, String txndate, String traceno, String txnrefno, JdbcTemplate jdbctemplate ){
		HttpSession session = getRequest().getSession();
		List txnlist = null;
		String searchcond = "";
		if( cardno != "" &&  !cardno.equals("null") ){
			searchcond += " AND CHN like '%"+cardno+"'";
		}	
		
		if( txndate != "" &&  !txndate.equals("null") ){ 
			searchcond += " AND trunc(TRANDATE)=to_date('"+txndate+"','dd-mm-yyyy')";
		}
		
		if( traceno != "" &&  !traceno.equals("null") ){  
			searchcond += " AND TRACENO='"+traceno+"'";
		}
		
		if( txnrefno != "" &&  !txnrefno.equals("null") ){   
			searchcond += " AND REFNUM='"+txnrefno+"'";
		}
		
		searchcond += "   ORDER BY PLSEQNO DESC ";
		trace("Getting records....");
		String txnqry = "SELECT CHN, TXNDESC, REFNUM, TO_CHAR(TRANDATE,'dd-mon-yyyy') AS TRANDATE, OPTYPE, ACCEPTORNAME, TERMLOC , ";
		txnqry += " SUBSTR(LPAD(TRANTIME,6,0),1,2)||':'||SUBSTR(LPAD(TRANTIME,6,0),3,2)||':'||SUBSTR(LPAD(TRANTIME,6,0),5,2) AS TRANTIME, ";
		txnqry +="  TO_CHAR(TXNAMOUNT,'999G999G999G999G999G999D99MI') AS  TXNAMOUNT FROM IFP_PL_TXN ";
		txnqry += " WHERE INST_ID='"+instid+"' "+ searchcond;
		enctrace( "txnqry : " + txnqry);
		txnlist = jdbctemplate.queryForList(txnqry);  
		return txnlist;
	}
	
	public void searchPLTransaction() throws IOException{
		trace("************ searchTransaction ***********" );trace("********** searchTransaction ***********" );
		HttpSession session = getRequest().getSession();
		String tablerec = "";
		try{ 
			String instid = comInstId( session ); 
			String cardno = getRequest().getParameter("cardno").trim();
			String txndate = getRequest().getParameter("txndate").trim();
			String traceno = getRequest().getParameter("tracenoval").trim();
			String txnrefno = getRequest().getParameter("txnrefno").trim(); 
			
			List result = searchPLTxnContent(instid, cardno, txndate, traceno, txnrefno, jdbctemplate);
			if( result == null){
				tablerec ="No Transaction Found" ;
			}else{
				tablerec = "<table border='0' cellpadding='0' cellspacing='0' width='100%' class='formtable'>";
				tablerec += "<tr><th>Card No</th> <th>Txn Description</th> <th>Txn Ref no</th> <th>Txn Date</th>  <th>Txn Time</th>   <th style='text-align:right'>Txn Amount</th> <th>Txn Type</th>  <th>Merchant name</th> <th>Location</th> </tr>";
				Iterator itr = result.iterator();
				while( itr.hasNext() ){
					Map mp = (Map)itr.next(); 
					 tablerec += "<tr>";
					 tablerec +="<td>"+(String)mp.get("CHN")+"</td>"; 
					 tablerec += "<td style='text-align:center'>"+(String)mp.get("TXNDESC")+"</td>";
					 tablerec += "<td>"+(String)mp.get("REFNUM")+"</td>"; 
					 tablerec += "<td>"+(String)mp.get("TRANDATE")+"</td>";  
					 tablerec += "<td>"+(String)mp.get("TRANTIME")+"</td>"; 
					 tablerec += "<td  style='text-align:right' >"+(String)(Object)mp.get("TXNAMOUNT")+"</td>";
					 tablerec += "<td>"+(String)mp.get("OPTYPE")+"</td>";  
					 tablerec += "<td>"+(String)mp.get("ACCEPTORNAME")+"</td>";  
					 tablerec += "<td>"+(String)mp.get("TERMLOC")+"</td>"; 
					
					 tablerec += "</tr>"; 
				}
			}
		 
		}catch(Exception e){
			tablerec = "Unable to continue the process";
			trace("Exception: "+ e.getMessage());
			e.printStackTrace();
		}
		getResponse().getWriter().write(tablerec);
}
	
	public List transactionSearchList(String instid, String searchcond, Connection conn) {
		List searchResult = null;
		ResultSet rs = null;
		JSONObject list = new JSONObject();
		try {
			String txnqry = "SELECT CHN, TXNDESC, TRACENO,  REFNUM, ";
			txnqry += " to_char(TRANDATE,'dd-mon-yyyy ') || ' ' ||  (substr(lpad(trantime,6,0),0,2) || ':' || substr(lpad(trantime,6,0),3,2) || ':' ||  substr(lpad(trantime,6,0),5,2)) as TRANDATE ";
			txnqry +=",   TXNAMOUNT, NVL( DECODE(ACCEPTORNAME,'000000000000000', 'WEB-APP', ACCEPTORNAME ) , '--') as ACCEPTORNAME, " ;
			txnqry += " TO_CHAR(RESPCODE) as RESPCODE,TO_CHAR(REASONCODE) AS REASONCODE  FROM  IFP_TXN_LOG ";
			txnqry += " WHERE INST_ID='"+instid+"' "+ searchcond;
			enctrace( "txnqry : " + txnqry);
			
			PreparedStatement ps = conn.prepareStatement(txnqry);
			rs = ps.executeQuery(txnqry);
			
			enctrace("Result Set Value is "+rs);
			
			while(rs.next()){
				list.put("CHN", rs.getString("CHN"));
				
				String chn = rs.getString("CHN");
			}
		} catch (Exception e) {
			
		}
		return null;
	}
	
	private String getRespDesc(String respcode, Connection conn) {
		String res=null;
		try {
			String selectqry="SELECT TO_CHAR(DESCRIPTION) as DESCRIPTION  FROM EZRESPCODE WHERE RESPCODE='"+respcode+"'";
		    PreparedStatement ps = conn.prepareStatement(selectqry);
		    ResultSet rs = ps.executeQuery(selectqry);
		    while(rs.next()){
		    	res = (String)rs.getString("DESCRIPTION");
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res ;
	}
	
	private String getTransdesc(String transdesc, Connection conn) {
		String result = null;
		try {
			String selectqry="SELECT TO_CHAR(TXNDESC) as TXNDESC  FROM EZTXNDESC WHERE TXNCODE='"+transdesc+"'";
		    PreparedStatement ps = conn.prepareStatement(selectqry);
		    ResultSet rs = ps.executeQuery(selectqry);
		    while(rs.next()){
		    	result = (String)rs.getString("TXNDESC");
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private String getReasonDesc(String reasoncode, Connection conn) {
		String result = null;
		try {
			String selectqry="SELECT TO_CHAR(REASONDESCRIPTION) as REASONDESCRIPTION  FROM EZREASONCODE WHERE REASONCODE='"+reasoncode+"'";
		    PreparedStatement ps = conn.prepareStatement(selectqry);
		    ResultSet rs = ps.executeQuery(selectqry);
		    while(rs.next()){
		    	result = (String)rs.getString("REASONDESCRIPTION");
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
