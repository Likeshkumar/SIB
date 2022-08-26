package com.ifd.cbslink;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

public class CbsCardLinkAction extends BaseAction{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private PlatformTransactionManager txManager = new DataSourceTransactionManager();
	JdbcTemplate jdbctemplate = new JdbcTemplate();
	CommonDesc commondesc = new CommonDesc();

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public String comInstId(HttpSession session) {
		String instid = (String) session.getAttribute("Instname");
		return instid;
	}

	public String comUserId(HttpSession session) {
		String userid = (String) session.getAttribute("USERID");
		return userid;
	}

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}
	
	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}
	
	public List cbscardslinklist;
	
	public List getCbscardslinklist() {
		return cbscardslinklist;
	}

	public void setCbscardslinklist(List cbscardslinklist) {
		this.cbscardslinklist = cbscardslinklist;
	}

	public String cardLinkHome(){
		System.out.println("Enters cardLinkHome");
		return "cbscardlinkhome";
	}
	public String cardlinklist(){
		
		System.out.println("cardlinklist");
		
		HttpSession session = getRequest().getSession();

		//String fromdate = getRequest().getParameter("fromdate");
		//String todate = getRequest().getParameter("todate");
		String cardno = getRequest().getParameter("cardno");
		String instid = comInstId(session);
		
		
		String keyid = "",ecardno="";
		String EDMK = "", EDPK = "";

		PadssSecurity padsssec = new PadssSecurity();
		Properties props = getCommonDescProperty();
		String EDPK1 = props.getProperty("EDPK");
		try{
			keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
			System.out.println("keyid::" + keyid);
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			// System.out.println("secList::" + secList);
			Iterator secitr = secList.iterator();
			if (!secList.isEmpty()) {
				while (secitr.hasNext()) {
					Map map = (Map) secitr.next();
					String CDMK = ((String) map.get("DMK"));
					// String eDPK = ((String) map.get("DPK"));

					//hcardno = padsssec.getHashedValue(cardno + instid);
					String CDPK = padsssec.decryptDPK(CDMK, EDPK1);
					// trace("cardno"+cardno);
					ecardno = padsssec.getECHN(CDPK, cardno);
					 System.out.println(cardno);
					 trace("cardno1"+ecardno);
				}
			}
		
		
			String cardsqury = "select BRANCH_CODE,ORG_CHN,CIN,ACCOUNT_NO,EMB_NAME,to_char(ISSUE_DATE,'yyyy-mm-dd') as ISSUE_DATE,"
  					+ "ORDER_REF_NO,MCARD_NO,ORDER_REF_NO,to_char(EXPIRY_DATE,'yyyy-mm-dd') as EXPIRY_DATE,BULK_REG_ID "
  					+ "from CARD_PRODUCTION WHERE INST_ID=? and org_chn=?";
  			System.out.println(cardsqury);
  			trace("cardsqury===>"+cardsqury);
		
			//cbscardslinklist = jdbctemplate.queryForList(cardslistqry, new Object[] { instid,fromdate,todate});
			cbscardslinklist = jdbctemplate.queryForList(cardsqury, new Object[] { instid,ecardno});
			System.out.println(cbscardslinklist);
			if(cbscardslinklist.size()<0){
				addActionError("No Records Found");
				return "required_home";
			}
		}catch (Exception e) {
			addActionError("Unable to Process");
			return "required_home";
		}
		
		return "cardslinklist";
	}
	
	public String postToCBS(){
		HttpSession session = getRequest().getSession();
		IfpTransObj transact1 = commondesc.myTranObject("INSCBSLINK", txManager);
		String instid = comInstId(session);
		String userid = comUserId(session);
		String[] pendingcardslist = getRequest().getParameterValues("cardno");
		
		System.out.println("pendingcardslist ===>"+pendingcardslist);
		System.out.println("instid ===>"+instid);
		System.out.println("userid ===>"+userid);
		
		if( pendingcardslist== null ){
			addActionError("No card number selected ....");
			return "required_home";
		}
		
		PadssSecurity padsssec = new PadssSecurity();
		String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
		String eDMK = "", eDPK = "",cdpd="",clearCardNumber="NA",CDPK="";
		Properties props = getCommonDescProperty();
		String EDPK = props.getProperty("EDPK");
		try {
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			Iterator secitr = secList.iterator();
			while (secitr.hasNext()) {
				Map map = (Map) secitr.next();
				eDMK = ((String) map.get("DMK"));
				eDPK = ((String) map.get("DPK"));
				CDPK = padsssec.decryptDPK(eDMK, EDPK);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "required_home";
		}
		
		try {
	        // doing some work asynchronously ...
			int insert = 0;
	    	  int ordercount = pendingcardslist.length;
	    	  trace("ordercount --->"+ordercount);
	    	  for (int i = 0; i < ordercount; i++) {
	    		  clearCardNumber = padsssec.getCHN(CDPK, pendingcardslist[i]);
	  			//String hcardNumber = commondesc.getHcardNo(order_refnum[i], jdbctemplate);
	  			System.out.println(" Inside Active Card Report Post To CBS ==> ");
	  			trace(" Inside Active Card Report Post To CBS ==> ");
	  			
	  			String docu_qury = "select BRANCH_CODE,ORG_CHN,CIN,ACCOUNT_NO,EMB_NAME,to_char(ISSUE_DATE,'yyyy-mm-dd') as ISSUE_DATE,"
	  					+ "ORDER_REF_NO,MCARD_NO,ORDER_REF_NO,to_char(EXPIRY_DATE,'yyyy-mm-dd') as EXPIRY_DATE,BULK_REG_ID "
	  					+ "from CARD_PRODUCTION WHERE INST_ID='"+instid+"' and org_chn='"+pendingcardslist[i]+"'";
	  			System.out.println(docu_qury);
	  			trace(docu_qury);
	  			
	  			List carddetlist = jdbctemplate.queryForList(docu_qury);
	  			String request = "";
	  			Iterator custitr = carddetlist.iterator();
	  			while (custitr.hasNext()) {
	  				Map mp = (Map) custitr.next();
	  				System.out.println(mp);
	  				trace("mp ===>"+mp);
	  				request= "{ \"activeCards\":[{" +
	  				   		"\"branch\":\""+(String) mp.get("BRANCH_CODE")+"\"," +
	  				   		"\"cardNumber\":\""+clearCardNumber+"\"," +
	  				   		"\"customerNumber\":\""+(String) mp.get("CIN")+"\"," +
	  				   		"\"accountNumber\":\""+(String) mp.get("ACCOUNT_NO")+"\"," +
	  				   		"\"emborsedName\":\""+(String) mp.get("EMB_NAME")+"\"," +
	  				   		"\"cardType\":\"INDIVIDUAL\"," +
	  				   		"\"issueDate\":\""+(String) mp.get("ISSUE_DATE")+"\"," +
	  				   		"\"orderRefNo\":\""+(String) mp.get("ORDER_REF_NO")+"\"," +
	  				   		"\"maskedCardNumber\":\""+(String) mp.get("MCARD_NO")+"\"," +
	  				   		"\"makerId\":\""+userid+"\"," +
	  				   		"\"bulkRefId\":\""+(String) mp.get("BULK_REG_ID")+"\"," +
	  				   		"\"cardFlag\":\"\"," +
	  				   		"\"expiryDate\":\""+(String) mp.get("EXPIRY_DATE")+"\"}]}";
	  				
	  			}
	  			
	  			String result = "",ret=""; 
	  			JSONObject json = null;
	  			Properties props1 = getCommonDescProperty();
	  			String cbsapiurl = props1.getProperty("cbs.api.url");
	  			String cbsapikey = props1.getProperty("cbs.api.key");
	  			String cbsapisecret = props1.getProperty("cbs.api.secret");
	  			String forwd = props.getProperty("cbs.api.fwd");
	  			try {
	  				String url = cbsapiurl+"core/api/v1.0/account/activeCardReport";
	  					System.out.println("url-->"+url);
	  					trace("url-->"+url);
	  					HttpPost post = new HttpPost(url);
	  					HttpClient client = HttpClientBuilder.create().build();
	  					post.setHeader("Content-Type", "application/json");
	  					post.setHeader("x-api-key", cbsapikey);
	  					post.setHeader("x-api-secret", cbsapisecret);
	  					post.setHeader("X-FORWARDED-FOR", forwd);
	  					
	  					System.out.println(request);
	  					trace("request-->"+request);
	  					
	  					StringEntity params =new StringEntity(request);
	  					post.setEntity(params);
	  					HttpResponse response = client.execute(post);
	  					int responseCode = response.getStatusLine().getStatusCode();
	  					System.out.println("Response Code : " + responseCode);
	  					trace("Response Code : " + responseCode);
	  					
	  					BufferedReader rd = new BufferedReader(
	  				                new InputStreamReader(response.getEntity().getContent()));
	  					String line = "";
	  					while ((line = rd.readLine()) != null) {
	  						result+= line;
	  					}
	  					ret = result;
	  				    System.out.println(ret);
	  				    trace("Response  : " + ret);
	  				    if(ret.contains("message")){
	  				    	json = new JSONObject(ret);
	  				    	
	  				    	if(json.getString("responseCode").equals("000")){
	  			  				System.out.println("production cbs card link update success");
	  			  				addActionMessage("Successfully Uploded");
	  			  				txManager.commit(transact1.status);
	  			  			}else{
	  			  				System.out.println("production cbs card link update fail");
	  			  				addActionError("Upload Failed");
	  			  				txManager.rollback(transact1.status);
	  			  			}
	  				    	
	  				    }else{
	  				    	addActionError("Unable to Connect CBS API");
	  				    	return "required_home";
	  				    }
	  				       
	  	        } catch (Exception e) {
	  				e.printStackTrace();
	  				System.out.println("Exception");
	  			}
	  			
	  			/*String cbslinkinsert = "UPDATE CBS_CARD_LINK SET (INSTID,CARDNO,MCARDNO,ACCT_NO,BRANCH,CIN,EMB_NAME,"
	  					+ "ORDER_REF_NO,UPLOAD_BY,UPLOAD_STATUS,UPLOAD_RESPCODE,ADDED_DATE,TYPE) = ( SELECT INST_ID,ORG_CHN,MCARD_NO,"
	  					+ "ACCOUNT_NO,BRANCH_CODE,CIN,EMB_NAME,ORDER_REF_NO,'"+userid+"','"+json.getString("message")+"',"
	  							+ "'"+json.getString("responseCode")+"',SYSDATE,'PR' "
	  					+ "FROM CARD_PRODUCTION WHERE ORG_CHN='"+pendingcardslist[i]+"') where CARDNO='"+pendingcardslist[i]+"'";
	  			System.out.println(cbslinkinsert);
	  			insert = jdbctemplate.update(cbslinkinsert);*/
	  			
	  		}
	    	  /*if(insert > 0){
	  				System.out.println("production cbs card link update success");
	  				addActionMessage("Successfully Uploded");
	  				txManager.commit(transact1.status);
	  			}else{
	  				System.out.println("production cbs card link update fail");
	  				addActionError("Upload Failed");
	  				txManager.rollback(transact1.status);
	  			}*/
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return "required_home";
	}
	
	@Async
	public void cardLinkWithCbs(String[] order_refnum, String instid, String userid) {
	    // to be executed in the background
		
		
		IfpTransObj transact1 = commondesc.myTranObject("INSCBSLINK", txManager);
		HttpServletRequest req = ServletActionContext.getRequest();
		  HttpServletResponse res = ServletActionContext.getResponse();

		  final AsyncContext asyncContext = req.startAsync(req, res);
		  asyncContext.start(new Runnable() {
		    @Override
		    public void run() {
		      try {
		        // doing some work asynchronously ...
		    	  
		    	  int ordercount = order_refnum.length;
		    	  for (int i = 0; i < ordercount; i++) {
		  			//String hcardNumber = commondesc.getHcardNo(order_refnum[i], jdbctemplate);
		  			System.out.println(" Inside Active Card Report Post To CBS ==> ");
		  			trace(" Inside Active Card Report Post To CBS ==> ");
		  			
		  			String docu_qury = "select BRANCH_CODE,ORG_CHN,CIN,ACCOUNT_NO,EMB_NAME,to_char(ISSUE_DATE,'dd-mm-yyyy') as ISSUE_DATE,"
		  					+ "ORDER_REF_NO,MCARD_NO,ORDER_REF_NO,to_char(EXPIRY_DATE,'dd-mm-yyyy') as EXPIRY_DATE "
		  					+ "from INST_CARD_PROCESS WHERE INST_ID='"+instid+"' and org_chn='"+order_refnum[i]+"'";
		  			System.out.println(docu_qury);
		  			trace(docu_qury);
		  			
		  			List carddetlist = jdbctemplate.queryForList(docu_qury);
		  			String request = "";
		  			Iterator custitr = carddetlist.iterator();
		  			while (custitr.hasNext()) {
		  				Map mp = (Map) custitr.next();
		  				System.out.println(mp);
		  				trace("mp ===>"+mp);
		  				request= "{ \"printedCards\":[{" +
		  				   		//"\"branch\":\""+(String) mp.get("BRANCH_CODE")+"\"," +
		  				   		"\"cardNumber\":\""+(String) mp.get("MCARD_NO")+"\"," +
		  				   		//"\"customerNumber\":\""+(String) mp.get("CIN")+"\"," +
		  				   		//"\"accountNumber\":\""+(String) mp.get("ACCOUNT_NO")+"\"," +
		  				   		//"\"emborsedName\":\""+(String) mp.get("EMB_NAME")+"\"," +
		  				   		"\"cardmanReference\":\""+(String) mp.get("ORDER_REF_NO")+"\"," +
		  				   		"\"issueDate\":\""+(String) mp.get("ISSUE_DATE")+"\"," +
		  				   		"\"orderRefNo\":\""+(String) mp.get("ORDER_REF_NO")+"\"," +
		  				   		"\"maskedCardNumber\":\""+(String) mp.get("MCARD_NO")+"\"," +
		  				   		"\"makerId\":\""+userid+"\"," +
		  				   		//"\"bulkRefId\":\""+(String) mp.get("ORDER_REF_NO")+"\"," +
		  				   		"\"cardFlag\":\"\"," +
		  				   		"\"expiryDate\":\""+(String) mp.get("EXPIRY_DATE")+"\"}]}";
		  				
		  			}
		  			
		  			String result = "",ret=""; 
		  			JSONObject json = null;
		  			Properties props = getCommonDescProperty();
		  			String cbsapiurl = props.getProperty("cbs.api.url");
		  			String cbsapikey = props.getProperty("cbs.api.key");
		  			String cbsapisecret = props.getProperty("cbs.api.secret");
		  			String forwd = props.getProperty("cbs.api.fwd");
		  			try {
		  					String url = cbsapiurl+"core/api/v1.0/account/printedCardReport";
		  					System.out.println("url-->"+url);
		  					trace("url-->"+url);
		  					HttpPost post = new HttpPost(url);
		  					HttpClient client = HttpClientBuilder.create().build();
		  					post.setHeader("Content-Type", "application/json");
		  					post.setHeader("x-api-key", cbsapikey);
		  					post.setHeader("x-api-secret", cbsapisecret);
		  					post.setHeader("X-FORWARDED-FOR", forwd);
		  					
		  					System.out.println(request);
		  					trace("request-->"+request);
		  					
		  					StringEntity params =new StringEntity(request);
		  					post.setEntity(params);
		  					HttpResponse response = client.execute(post);
		  					int responseCode = response.getStatusLine().getStatusCode();
		  					System.out.println("Response Code : " + responseCode);
		  					trace("Response Code : " + responseCode);
		  					
		  					BufferedReader rd = new BufferedReader(
		  				                new InputStreamReader(response.getEntity().getContent()));
		  					String line = "";
		  					while ((line = rd.readLine()) != null) {
		  						result+= line;
		  					}
		  					ret = result;
		  				    System.out.println(ret);
		  				    trace("Response  : " + ret);
		  				    json = new JSONObject(ret);
		  				       
		  	        } catch (Exception e) {
		  				e.printStackTrace();
		  				System.out.println("Exception");
		  			}
		  			
		  			String cbslinkinsert = "INSERT INTO CBS_CARD_LINK(INSTID,CARDNO,MCARDNO,ACCT_NO,BRANCH,CIN,EMB_NAME,"
		  					+ "ORDER_REF_NO,UPLOAD_BY,UPLOAD_STATUS,UPLOAD_RESPCODE,ADDED_DATE,TYPE) SELECT INST_ID,ORG_CHN,MCARD_NO,"
		  					+ "ACCOUNT_NO,BRANCH_CODE,CIN,EMB_NAME,ORDER_REF_NO,'"+userid+"','"+json.getString("message")+"',"
		  							+ "'"+json.getString("responseCode")+"',SYSDATE,'I' "
		  					+ "FROM INST_CARD_PROCESS WHERE ORG_CHN='"+order_refnum[i]+"'";
		  			System.out.println(cbslinkinsert);
		  			int insert = jdbctemplate.update(cbslinkinsert);
		  			if(insert > 0){
		  				System.out.println("instant cbs card link insert success");
		  				txManager.commit(transact1.status);
		  			}else{
		  				System.out.println("instant cbs card link insert fail");
		  				txManager.rollback(transact1.status);
		  			}
		  		}
		      }
		      finally {
		        asyncContext.complete();
		      }
		    }
		  });
	}

}
