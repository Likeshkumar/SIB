package com.ifp.personalize;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;

public class TopupAction extends BaseAction
{
	private static final long serialVersionUID = 1L;
	
	private String act;
	 
	
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

	public String comInstId(){
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	}
	
	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}
 
	
	public String cardTopuphome()
	{
		System.out.println("Card Topup Home");
	
		return "cardtopuphome";
	}
	private List cardaccountlist;
	
	public List getCardaccountlist() {
		return cardaccountlist;
	}
	public void setCardaccountlist(List cardaccountlist) {
		this.cardaccountlist = cardaccountlist;
	}
	public String getAccountdetails()
	{
		 
		HttpSession session = getRequest().getSession();
		try
		{
			String instid = comInstId();
			String cardinfo=getRequest().getParameter("cardnum");
			System.out.println("THe Card Number Is ===> "+cardinfo);
			List accounts = commondesc.getCardAccountnumbers(cardinfo,instid,jdbctemplate);
			if(!(accounts.isEmpty()))
			{
				setCardaccountlist(accounts);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg","");
			}
			else
			{
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg"," No Account Details Found ");
			}
					
		}
		catch (Exception e) 
		{
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg"," Error While Getting the Accounts "+e.getMessage());
		}
		return "topupaccounts";
	}
	
	public void gettingCardaccounts() 
	{
		 
		String instid = comInstId();
		String cardinfo=getRequest().getParameter("cardinfo");
		System.out.println("THe Card Number Is ===> "+cardinfo);
		String ajaxresult = "";
		try
		{
			List acctlist = commondesc.getCardAccountnumbers(cardinfo,instid, jdbctemplate);
			System.out.println("acctlist===> "+acctlist);
			if(acctlist.isEmpty())
			{
				ajaxresult = "<b>No Result Found</b>";
				System.out.println("ajaxresult ===> "+ajaxresult);
				getResponse().getWriter().write(ajaxresult);
			}
			else
			{
				Iterator itr = acctlist.iterator();
				while(itr.hasNext())
				{
					Map map = (Map)itr.next();
					String acctnum = (String)map.get("ACCT_NO"); 
					String acctccy = (String)map.get("ACCT_CCY");
					String acctccydesc = (String)map.get("CCY_DESC");
					ajaxresult=ajaxresult+ "<tr><td>"+acctnum+"</td><td>"+acctccy+"</td><td>"+acctccydesc+"</td></tr>";
				}
				System.out.println("ajaxresult ===> "+ajaxresult);
				getResponse().getWriter().write(ajaxresult);
			}
		}
		catch (Exception e) 
		{
			System.out.println("Error While Getting the Result");
			try
			{
				getResponse().getWriter().write("<b>Error</b>");
			}catch (Exception ex) 
			{
				System.out.println("Error Send the Error Response");
			}
		}
		
	}
	
	
}
