package com.ifd.CurrencyRate;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;
import com.opensymphony.xwork2.ModelDriven;

public class CurrencyRateAction extends BaseAction implements ModelDriven<CurrencyRateBeans> {

	private static final long serialVersionUID = -613989708087404296L;
	CurrencyRateBeans bean = new CurrencyRateBeans();
	JdbcTemplate jdbctemplate = new JdbcTemplate();
	CurrencyRateDAO dao = new CurrencyRateDAO();
	CommonDesc commondesc = new CommonDesc();
	
	public CommonDesc getCommondesc() {
		return commondesc;
	}
	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}
	public CurrencyRateBeans getModel() {
		return bean;
	}
	private PlatformTransactionManager  txManager  = new DataSourceTransactionManager();
	
	public PlatformTransactionManager getTxManager() {
		return txManager;
	}
	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}
	//added by gowtham220719
	AuditBeans auditbean = new AuditBeans();
	
	public AuditBeans getAuditbean() {
	return auditbean;  
}

public void setAuditbean(AuditBeans auditbean) {
	this.auditbean = auditbean;
}
//added end
	
	public String comInstId( HttpSession session ){ 
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	}
	
	public String comUserId( HttpSession session ){
		String userid = (String)session.getAttribute("USERID"); 
		return userid;
	}
	
	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}
	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}
	
	public String addCurrency(){
		List<?> currencylist = dao.getCurrencyList(jdbctemplate);
		if(currencylist.isEmpty()){
			addActionError("No Currency is configured");
			return "required_home";
		}else{
			bean.setCurrencylist(currencylist);
		}
		return "addcurrencyrate";
	}
	public String saveCurrency(){
		IfpTransObj transact = commondesc.myTranObject("CHANGEPASS", txManager);
		
		//added by gowtham_240719
		String  ip=(String) session.getAttribute("REMOTE_IP");
		
		try{
			HttpSession session=getRequest().getSession();
			String instid = comInstId(session);
			String userid = comUserId(session);
			userid = commondesc.getUserName(instid, userid, jdbctemplate);
			int save = 0;
			String count = dao.checkCurrencyExists(instid,bean,jdbctemplate); 
			if("0".equalsIgnoreCase(count)){
				save = dao.saveCurrency(instid,bean,userid,"INSERT",jdbctemplate);
			}else{
				save = dao.saveCurrency(instid,bean,userid,"UPDATE",jdbctemplate);
			}
			
			if(save > 0){
				transact.txManager.commit(transact.status);
				

				/*************AUDIT BLOCK**************/ 
				 try{
					 
				auditbean.setActmsg("Rate updated Successfully, Waiting for Authorization" );
				auditbean.setUsercode(userid);
				auditbean.setAuditactcode("1334"); 
			
				//added by gowtham_240719
				trace("ip address======>  "+ip);
				auditbean.setIpAdress(ip);
				commondesc.insertAuditTrail(instid, userid, auditbean, jdbctemplate, txManager);
				}catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
				/*************AUDIT BLOCK**************/
				
				
				addActionMessage("Rate updated Successfully, Waiting for Authorization");
			}else{
				transact.txManager.rollback(transact.status);
				addActionError("Fail to update the Rate");
			}
		}catch (Exception e) {
			transact.txManager.rollback(transact.status);
			e.printStackTrace();
		}
		return "required_home";
	}
	public String authCurrencyHome(){
		
		
		
		List<?> currencylist = dao.getAuthCurrency(jdbctemplate);
		if(currencylist.isEmpty()){
			addActionError("No Currency is waiting for Authorization");
			return "required_home";
		}else{
			bean.setCurrencylist(currencylist);
		}
		return "authcurrencyhome";
	}
	
	public String getAuthCurrency(){
		List<Map<String,Object>> currencylist = (List<Map<String, Object>>) dao.getAuthCurrencyList(bean,jdbctemplate);
		if(currencylist.isEmpty()){
			addActionError("Rates Not Found");
			return "required_home";
		}else{
			bean.setCurrencycode((String) currencylist.get(0).get("CURRCODE"));
			bean.setBuyrate((BigDecimal) currencylist.get(0).get("BUYINGRATE"));
			bean.setSellrate((BigDecimal) currencylist.get(0).get("SELLINGRATE"));
			bean.setCurrencydesc((String) currencylist.get(0).get("CURRDESC"));
		}
		return "authcurrency";
	}
	
	public String authorizeCurrency(){
		
		//added by gowtham_240719
		String  ip=(String) session.getAttribute("REMOTE_IP");

		
		IfpTransObj transact = commondesc.myTranObject("CHANGEPASS", txManager);
		try{
			HttpSession session=getRequest().getSession();
			String instid = comInstId(session);
			String userid = comUserId(session);
			userid = commondesc.getUserName(instid, userid, jdbctemplate);
			int ezhist = dao.movetoEZHistory(bean,userid,jdbctemplate);
			int ezcurrency = dao.movetoEZCurrency(bean,userid,jdbctemplate);
			//int temphist = dao.movetoTempHistory(bean,userid,jdbctemplate);
			int tempdelete = dao.tempDelete(bean,jdbctemplate);
			//if(ezcurrency > 0 && ezhist > 0 && temphist > 0 && tempdelete > 0){
			if(ezcurrency > 0 && ezhist > 0 && tempdelete > 0){
				transact.txManager.commit(transact.status);
				addActionMessage("Currency Rate Authorized Successfully");
				
				//added by gowtham_240719

				/*************AUDIT BLOCK**************/ 
				try{
					
				auditbean.setActmsg("Currency Rate Authorized by"+userid );
				auditbean.setUsercode(userid);
				auditbean.setAuditactcode("01334"); 
				
				//added by gowtham_240719
				trace("ip address======>  "+ip);
				auditbean.setIpAdress(ip);
				commondesc.insertAuditTrail(instid, userid, auditbean, jdbctemplate, txManager);
				}catch(Exception audite ){ trace("Exception in auditran : "+ audite.getMessage()); }
				/*************AUDIT BLOCK**************/
				
				
			}else{
				transact.txManager.rollback(transact.status);
				addActionError("Fail to authorize  Rate");
			}
		}catch (Exception e) {
			transact.txManager.rollback(transact.status);
			addActionError("unable to continue");
			e.printStackTrace();
		}
		return "required_home";
	}
	public void getPreviousRate() throws IOException{
		String currencycode = getRequest().getParameter("currencycode");
		List<Map<String,Object>> curlist = dao.getPreviousRate(currencycode,jdbctemplate);
		String opt =" <br> <tr> <td> previous rate</td><tr> <tr> <td>"+(BigDecimal) curlist.get(0).get("BUYINGRATE")+"</td> </tr> <tr> <td> "+(BigDecimal) curlist.get(0).get("SELLINGRATE")+"</td> </tr>";
		getResponse().getWriter().write(opt);
	}
	public List prevratelist;

	public List getPrevratelist() {
		return prevratelist;
	}
	public void setPrevratelist(List prevratelist) {
		this.prevratelist = prevratelist;
	}
}
