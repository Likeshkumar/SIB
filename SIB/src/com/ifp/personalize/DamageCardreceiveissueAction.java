package com.ifp.personalize;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

public class DamageCardreceiveissueAction extends BaseAction
{
	private static final long serialVersionUID = 1L;

	
	
	
	 
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
	
	public String comUserId(){
		HttpSession session = getRequest().getSession();
		String userid = (String)session.getAttribute("USERID"); 
		return userid;
	}
	public String comBranchId(){
		HttpSession session = getRequest().getSession();
		String br_id = (String)session.getAttribute("BRANCHCODE"); 
		return br_id;
	}
	public String comuserType(){
		HttpSession session = getRequest().getSession();
		String usertype = (String)session.getAttribute("USERTYPE"); 
		return usertype;
	}
	public String comUsername(){
		HttpSession session = getRequest().getSession();
		String username = (String)session.getAttribute("USERNAME"); 
		return username;
	}
	
	
	private String act;
	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}
	
	
	private List branchlist;
	
	
	public List getBranchlist() {
		return branchlist;
	}

	public void setBranchlist(List branchlist) {
		this.branchlist = branchlist;
	}


	private List personalproductlist;
	public List getPersonalproductlist() {
		return personalproductlist;
	}

	public void setPersonalproductlist(List personalproductlist) {
		this.personalproductlist = personalproductlist;
	}
		


	public String damageCardreceivehome()
	{
		List pers_prodlist=null,br_list=null;
		String inst_id =comInstId();
		String usertype = comuserType();
		String branch = comBranchId();
		HttpSession session = getRequest().getSession();
		 
		String temp = act;
		System.out.println(temp);
		session.setAttribute("DAMAGECARDREISS_ACT", act);
		String session_act = (String) session.getAttribute("DAMAGECARDREISS_ACT");
		System.out.println("session_act " + session_act);
		
		System.out.println("The DATEFILTER_REQ===> "+(String)session.getAttribute("DATEFILTER_REQ"));
		try {
			System.out.println("Inst Id===>"+inst_id+"  Branch Code ===>"+branch);
			if (usertype.equals("INSTADMIN")) {
				System.out.println("Branch list start");
				br_list = commondesc.generateBranchList(inst_id, jdbctemplate);
				System.out.println("Branch list "+br_list);
				if(!(br_list.isEmpty())){
					setBranchlist(br_list);
					session.setAttribute("curerr", "S");
					session.setAttribute("curmsg","");
					System.out.println("Branch list is not empty");
				}
				else{
					setBranchlist(br_list);
					session.setAttribute("curerr", "E");
					session.setAttribute("curmsg"," No Branch Details Found ");
					System.out.println("Branch List is empty ");
				}
			}
			pers_prodlist=commondesc.getProductList(inst_id, jdbctemplate, session);
			if (!(pers_prodlist.isEmpty())){
				setPersonalproductlist(pers_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg","");
				System.out.println("Product List is ===> "+pers_prodlist);
			} else{
				System.out.println("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg"," No Product Details Found ");
			}
			
		} catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg"," Error While Fetching The Product Details  "+ e.getMessage());
			
		}

		return "damagecardreceivehome";
	}
	
	
	private List damagereceivelist;
	
	
	public List getDamagereceivelist() {
		return damagereceivelist;
	}

	public void setDamagereceivelist(List damagereceivelist) {
		this.damagereceivelist = damagereceivelist;
	}

	public String getDamagedcardsreceivelist()
	{
		
		HttpSession session = getRequest().getSession(); 
		//String pingentype = getRequest().getParameter("pingentype");
		String branch = getRequest().getParameter("branchcode");
		String binno = getRequest().getParameter("cardtype");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");

		
		//System.out.println("branch==> "+branch+"   binno==>"+binno+"  fromdate===> "+fromdate+"  todate===>"+todate);
		String dateflag ="PRE_DATE";
		String inst_id=comInstId();

		
		String cardstatus = "03",mkckstatus="P",caf_rec_status="D";
		List authorizeorderlist = null;
		String session_act = (String) session.getAttribute("DAMAGECARDREISS_ACT");
		System.out.println("session_act " + session_act);		
		try {
			
			String condition = commondesc.filterCondition(binno, branch, fromdate, todate, dateflag);
			System.out.println("Condition Value----->  "+condition);
			
			
			authorizeorderlist = commondesc.maintenanceCardslist( inst_id,cardstatus, mkckstatus,caf_rec_status,condition, jdbctemplate);
			System.out.println("authorizeorderlist===> "+authorizeorderlist);
			if(!(authorizeorderlist.isEmpty())){
				setDamagereceivelist(authorizeorderlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg","");
				
			}else{
				setDamagereceivelist(authorizeorderlist);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg"," No Damage Cards To Receive ");
				setAct((String) session.getAttribute("DAMAGECARDREISS_ACT"));
				return damageCardreceivehome();
			}
		} catch (Exception e) {
			System.out.println("Exception--->"+e);
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg","Error While Fetching The Orders for Damage Card PRE  ,ERROR:"+e.getMessage());
			
		}
		
		return "damagecardreceivelist";
	}

	public String confirmDamagecardreceived()
	{
		HttpSession session = getRequest().getSession(); 
		String cardnum[] = getRequest().getParameterValues("personalrefnum");
		int cards_count = cardnum.length;
		String makerid = comUserId();
		String instid = comInstId();
		IfpTransObj updatecard = commondesc.myTranObject("CONF",txManager);
		
	/*	StringBuffer updatequry = new StringBuffer("UPDATE PERS_CARD_PROCESS SET CARD_STATUS='04',"
		+ "RECV_DATE=(sysdate),MAKER_ID='"+makerid+"',MAKER_DATE=(sysdate) WHERE INST_ID='"+instid+"' AND ");
		int transtatus = 0;
		String errormsg = "";
		try {
		for(int i=0;i<cards_count;i++)
		{
			int update_status = 0;
			System.out.println("Card Number ==> "+cardnum[i]);
			updatequry.append("CARD_NO='"+cardnum[i]+"'");
			System.out.println("UPdate Query is ===> "+updatequry.toString());
			update_status = commondesc.executeTransaction(updatequry.toString(), jdbctemplate);*/
		
		
		///by gowtham
		
		StringBuffer updatequry = new StringBuffer("UPDATE PERS_CARD_PROCESS SET CARD_STATUS=?,"
				+ "RECV_DATE=(sysdate),MAKER_ID=?,MAKER_DATE=(sysdate) WHERE INST_ID=? AND ");
				int transtatus = 0;
				String errormsg = "";
				try {
				for(int i=0;i<cards_count;i++)
				{
					int update_status = 0;
					System.out.println("Card Number ==> "+cardnum[i]);
					updatequry.append("CARD_NO='"+cardnum[i]+"'");
					System.out.println("UPdate Query is ===> "+updatequry.toString());
					update_status =jdbctemplate.update(updatequry.toString(),new Object[]{"04",makerid,instid});
				
				
				
				if(update_status == 1)
				{
					transtatus = transtatus + 1;
				}
			}
		} catch (Exception e) 
		{
			errormsg = e.getMessage();
		}
		System.out.println("transtatus ===> "+transtatus);
		if(cards_count == transtatus)
		{
			updatecard.txManager.commit(updatecard.status);
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", cards_count+" Cards Received Status Updated Successfully ");
			System.out.println("Card Received Txn Got Commited  ");
		}else
		{
			updatecard.txManager.rollback(updatecard.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg"," Card Receive Failed "+errormsg);
			System.out.println("Card Received Txn Got Roll Backed ");
		}
		setAct((String) session.getAttribute("DAMAGECARDREISS_ACT"));
		return damageCardreceivehome();
	}
}
