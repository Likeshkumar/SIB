package com.ifp.Action; 

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
 

 
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.IfpTransObj;



public class SchemeAction extends BaseAction {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final RowMapper String = null;
	private List schemeactionlist;
  
	public String schmres = null;
	public List runningSchemeList;
	
	public List schemeFeeList;
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
	 
	
	CommonDesc commondesc = new CommonDesc();
	
	
	
	public CommonDesc getCommondesc() {
		return commondesc;
	}
	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}
 
	CommonUtil comutil= new CommonUtil(); 
	public CommonUtil getComutil() {
		return comutil;
	}
	public void setComutil(CommonUtil comutil) {
		this.comutil = comutil;
	}
	
	static Boolean  initmail = true; 
	static  String menuid = "000";
	 
 
	
	public List getSchemeFeeList() {
		return schemeFeeList;
	}

	public void setSchemeFeeList(List schemeFeeList) {
		this.schemeFeeList = schemeFeeList;
	}



	public List getRunningSchemeList() {
		return runningSchemeList;
	}

	public void setRunningSchemeList(List runningSchemeList) {
		this.runningSchemeList = runningSchemeList;
	}

	public String getSchmres() {
		return schmres;
	}

	public void setSchmres(String schmres) {
		this.schmres = schmres;
	}

	 
	public List getSchemeactionlist() {
		return schemeactionlist;
	}

	public void setSchemeactionlist(List schemeactionlist) {
		this.schemeactionlist = schemeactionlist;
	}
	
	public List subFeeDetail;
	
	public List getSubFeeDetail() {
		return subFeeDetail;
	}
	public void setSubFeeDetail(List subFeeDetail) {
		this.subFeeDetail = subFeeDetail;
	}
	
 
	
	
	public String addNewfeeconfig() 
	{ 
		trace("*************** Entering Next page of fee Configuration **********");
		enctrace("*************** Entering Next page of fee Configuration **********");

		HttpSession session=getRequest().getSession();
		trace( "Calling schemeMethod ###################");
		String instid = this.comInstId();
		List actioncodes;
		try 
		{
			 
			//String feecode = getRequest().getParameter("feecode");
			String feetype = getRequest().getParameter("feetype");
			trace("feetype   "+feetype);
			session.setAttribute("feetype",feetype);
			if(feetype.equals("new"))
			{
				String feename = getRequest().getParameter("feename");
				trace("feename   "+feename);
				setFeenamebean(feename);
				
			}
			else
			{
				String feecode = getRequest().getParameter("feecode");
				String query= "select FEE_DESC from IFP_FEE_DESC where FEE_CODE='"+feecode+"' AND INST_ID='"+instid+"'";
				String fee = (String)jdbctemplate.queryForObject(query,String.class);
				session.setAttribute("addfeename", fee);
				session.setAttribute("addfeecode", feecode);
				setFeenamebean(fee);

				//String qrysubfeecodeqry ="select SUBFEE_DESC, DECODE(SUBFEE_TYPE,'S','SPECIAL FEE','D','DEFAULT FEE') AS SUBFEE_TYPE, TO_CHAR(FROMDATE,'DD-MON-YYYY') AS FROMDATE, TO_CHAR(TODATE,'DD-MON-YYYY') AS TODATE,  TO_CHAR(CONFIG_DATE,'DD-MON-YYYY') AS CONFIG_DATE, USER_CODE from IFP_FEE_SUBCODE where INST_ID = '"+instid+"' AND FEE_CODE='"+feecode+"'";
				String qrysubfeecodeqry ="select SUBFEE_DESC, DECODE(SUBFEE_TYPE,'S','SPECIAL FEE','D','DEFAULT FEE') AS SUBFEE_TYPE,FEE_SUBCODE,TO_CHAR(FROMDATE,'DD-MON-YYYY') AS FROMDATE, TO_CHAR(TODATE,'DD-MON-YYYY') AS TODATE,  TO_CHAR(CONFIG_DATE,'DD-MON-YYYY') AS CONFIG_DATE, USER_CODE,FEE_SUBCODE from IFP_FEE_SUBCODE where INST_ID = '"+instid+"' AND FEE_CODE='"+feecode+"' AND DELETED_FLAG='0'";
				enctrace( "qrysubfeecodelist_" + qrysubfeecodeqry);
				List subfeecodelist = jdbctemplate.queryForList(qrysubfeecodeqry);
				trace("subfeecodelist "+subfeecodelist.size());
				if(!subfeecodelist.isEmpty())
				{ 
					issubfeeexist = true;
					setSubfeelist(subfeecodelist);
				}
			}
			
			
			String qryactioncodes="select ACTION_CODE, ACTION_DESC from IFACTIONCODES where INST_ID = '"+instid+"' AND ACTION_STATUS ='1'"; 
			actioncodes = jdbctemplate.queryForList(qryactioncodes);
			trace("Lis of Actioj Code is-----> "+actioncodes.size());
			if(actioncodes.isEmpty())
			{
				trace("No Fee Actions Configured");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "No Fee Actions Configured");
			}
			else
			{
				System.out.println("Actions Configured");
				setSchemeactionlist(actioncodes);
				session.setAttribute("curmsg", "");
			}
			session.setAttribute("feetype",feetype);
		} 
		catch (Exception e) 
		{			
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "No Fee Actions Configured");
			trace("No Fee Actions Configured : "+e.getMessage());
			trace("\n\n"); 
			enctrace("\n\n");
		}

		trace("\n\n"); 
		enctrace("\n\n");
		return "addnewfeeconfig";	
	}	
	
	
	public String feehome()
	{ 
		trace("********* Adding fee begins *********");
		enctrace("********* Adding fee begins *********");
		HttpSession session=getRequest().getSession();
		String inst_id = comInstId();
 
		
		/*** MAIL BLOCK ****/
		System.out.println( "initmail--" + initmail);
		System.out.println( "initmail--" + initmail +" parentid :  " + this.menuid ); 
		if( initmail ){
			HttpServletRequest req = getRequest();
			String menuid = comutil.getUrlMenuId( req, jdbctemplate ); 
			if( !menuid.equals("NOREC")){ this.menuid = menuid;  }
			else{	this.menuid = "000"; }
			initmail = false;
		}
		/*** MAIL BLOCK ****/
		
		trace("Getting fee desc details");
		String qryschemelist="select FEE_CODE, FEE_DESC, STATUS_FLAG, USER_NAME, to_char(INTRODATE,'DD-MON-YY') as INTRO_DATE from IFP_FEE_DESC WHERE INST_ID='"+inst_id+"' order by INTRODATE";
		enctrace("Getting fee desc details : " +qryschemelist);
		
		try
		{
			
			
			List resschemelist = jdbctemplate.queryForList(qryschemelist);
			trace("Got fee desc : " + resschemelist );
			System.out.println("resschemelist====> "+resschemelist);
			if(resschemelist.isEmpty())
			{
				session.setAttribute("curerr","E");
				session.setAttribute("curmsg","No Fee Configured.Select New Fee");
				trace("No Fee Configured.Select New Fee");
			}else
			{
				
				ListIterator itr = resschemelist.listIterator();
				while(itr.hasNext())
				{
					Map map = (Map) itr.next();
					String usrame = (String) map.get("USER_NAME");
					System.out.println("usrame	"+usrame);
					String userame =commondesc.getUserName(inst_id,usrame, jdbctemplate); 
					map.put("USERNAME",userame);
					itr.remove();
					itr.add(map);	
				}
				setRunningSchemeList(resschemelist);
				session.setAttribute("curerr","S");
			}
		}
		catch( Exception ex )
		{
			System.out.println( " Error in qry while selecting sheme list " +ex.getMessage());
			session.setAttribute("curerr","E");
			session.setAttribute("curmsg","ERROR :"+ex.getMessage());			
		}
		
		return "feeConfighome";
	}
	
	 
	
	
	
	public String configScheme()
	{ 

		IfpTransObj transact = commondesc.myTranObject("CONFIG", txManager);
		 
		System.out.println("process====> 1");
		HttpSession session=getRequest().getSession();
		String feetype = (String)session.getAttribute("feetype");
		System.out.println("feetype	"+feetype);
		try {
			int feedesc_insert = 0,subfeedesc_insert=0;
			System.out.println("process====> 2");
			String inst_id = comInstId();
			String user_id = comUserId();
			System.out.println("process====> 3"); 
			System.out.println("process====> 4");
			//String sheme_code =  getRequest().getParameter("schemecode");
			//String sheme_desc =  getRequest().getParameter("schemedesc");
			String mode[] = getRequest().getParameterValues("mode");
			String childfeetype = getRequest().getParameter("childfeetype");
			String actionnames[] =  getRequest().getParameterValues("hidaactionlist"); 
			int list_len = actionnames.length;
			System.out.println("Action Lenght===> "+list_len);
			String subfeedecs =  getRequest().getParameter("subfeedecs").toUpperCase();
			System.out.println("subfeedecs	"+subfeedecs);
			String fromdate =  getRequest().getParameter("fromdate");
			System.out.println("fromdate	"+fromdate);
			String todate =  getRequest().getParameter("todate");
			System.out.println("todate	"+todate);
			String feecode=null,subfeecode=null;
			String feename = getRequest().getParameter("msterfeename");
			System.out.println("feename	"+feename);
			int sub_max_total = 0;			
			if(feetype.equals("new"))
			{
				String countexist = "SELECT COUNT(*) FROM IFP_SEQUENCE_MASTER WHERE INST_ID='"+inst_id+"'";
				int count = jdbctemplate.queryForInt(countexist);
				if(count==0)
				{
					
					String insertfeecode = "INSERT INTO IFP_SEQUENCE_MASTER(INST_ID,FEECODE_SEQ) VALUES('"+inst_id+"','1')";
					System.out.println("insertfeecode==> "+insertfeecode);
					int fee_insert= jdbctemplate.update(insertfeecode);
					if(fee_insert==1)
					{
						transact.txManager.commit(transact.status);
					}
					else
					{
						transact.txManager.rollback(transact.status);
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", " Fee Configuration Failed ");
						return"required_home";
					}
					
				}
				feecode = commondesc.feecodeSequance(inst_id, jdbctemplate);
				System.out.println("feecode			"+feecode);
				session.setAttribute("feecode", feecode);
				
				subfeecode = commondesc.subfeecodeSequance(inst_id, jdbctemplate);
				System.out.println("subfeecode			"+subfeecode);
				session.setAttribute("subfeecode", subfeecode);
				
				String feedesc_qury = "INSERT INTO IFP_FEE_DESC(INST_ID, FEE_CODE, FEE_DESC, STATUS_FLAG, USER_NAME,INTRODATE) values ( '"+inst_id+"','"+feecode+"','"+feename+"','1', '"+user_id+"', SYSDATE )";
				System.out.println("fee desc ===> "+feedesc_qury);
				feedesc_insert= jdbctemplate.update(feedesc_qury);
				  
				String subcode_qury = "INSERT INTO IFP_FEE_SUBCODE(INST_ID,FEE_CODE,FEE_SUBCODE,SUBFEE_DESC,FROMDATE,TODATE,SUBFEE_TYPE, CONFIG_DATE, USER_CODE) VALUES('"+inst_id+"','"+feecode+"','"+subfeecode+"','"+subfeedecs+"',to_date('"+fromdate+"','DD-MM-YY'),to_date('"+todate+"','DD-MM-YY'),'"+childfeetype+"',sysdate,'"+user_id+"')";
				System.out.println("subcode_qury  ====>"+subcode_qury);
				subfeedesc_insert= jdbctemplate.update(subcode_qury);
			}
			else{
				feecode = (String)session.getAttribute("addfeecode");	
				feedesc_insert = 1;
				subfeecode = commondesc.subfeecodeSequance(inst_id, jdbctemplate);
				System.out.println("subfeecode	"+subfeecode);
				subfeedecs =getRequest().getParameter("subfeedecs").toUpperCase();
				System.out.println("subfeedecs	"+subfeedecs);
				//String maxsubfee =getRequest().getParameter("subcode");	
				
				System.out.println("subfeecode			"+subfeecode);
				String subcode_qury = "INSERT INTO IFP_FEE_SUBCODE(INST_ID,FEE_CODE,FEE_SUBCODE,SUBFEE_DESC,FROMDATE,TODATE,SUBFEE_TYPE, CONFIG_DATE, USER_CODE) VALUES('"+inst_id+"','"+feecode+"','"+subfeecode+"','"+subfeedecs+"',to_date('"+fromdate+"','DD-MM-YY'),to_date('"+todate+"','DD-MM-YY'),'"+childfeetype+"',sysdate,'"+user_id+"')";
				System.out.println("subcode_qury  ====>"+subcode_qury);
				subfeedesc_insert= jdbctemplate.update(subcode_qury);
			}
			
			int all_status = 0;
			for(int j=0;j<list_len;j++)
			{
				int insert_status = 0;
				System.out.println("actionnames["+j+"]===> "+actionnames[j]);
				String actioncode = actionnames[j].toString();
				System.out.println("actioncode===> "+actioncode);
				System.out.println("mode["+j+"]---> "+mode[j]);
				String modevalue = mode[j].toString();
				System.out.println("modevalue===> "+modevalue);

				String codeamt = getRequest().getParameter(actioncode);
				System.out.println("codeamt===> "+codeamt);
				String feedetails_qury = "INSERT INTO IFP_FEE_MASTER(INST_ID,FEE_CODE,FEE_ACTION,FEE_AMOUNT,FEE_MODE,FEE_SUBCODE) VALUES('"+inst_id+"','"+feecode+"','"+actioncode+"','"+codeamt+"','"+modevalue+"','"+subfeecode+"')";
				System.out.println("feedetails_qury====>"+feedetails_qury);
				insert_status = jdbctemplate.update(feedetails_qury);
				if(insert_status == 1)
				{
					all_status = all_status + 1;
				}else{
					break;
				}
			}
			
			System.out.println("all_status===>"+all_status+"\nlist_len===>"+list_len+"feedesc_insert===>"+feedesc_insert);
			if(all_status == list_len && feedesc_insert == 1 && subfeedesc_insert==1)
			{
				transact.txManager.commit(transact.status);
				session.setAttribute("preverr", "S");
				session.setAttribute("prevmsg", "Fee Configured Successfully ");
				System.out.println("TXN COMMITED");
				
				
				/*** MAIL BLOCK ****//*
				IfpTransObj transactmail = commondesc.myTranObject(dataSource); 
				try {
					String alertid = (String) session.getAttribute("URLMENUID"); 
					if( alertid != null && !alertid.equals("000")){
						String keymsg = "The Fee  " + feename;
						int mail = comutil.sendMail( inst_id, alertid, keymsg, jdbcTemplate, session, getMailSender() );
						System.out.println( "mail return__" + mail);
					}
					
				} catch (Exception e) {  e.printStackTrace(); }
				  finally{
					transactmail.txManager.commit(transactmail.status);
					System.out.println( "mail commit successfully");
				} 
				*//*** MAIL BLOCK ****//*
				*/
			}
			else{
				transact.txManager.rollback(transact.status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " Fee Configuration Failed ");
				System.out.println("TXN ROOLLED BACK");
			}

		}
		catch (Exception e) 
		{
			System.out.println("Error: "+e.getMessage());
			transact.txManager.rollback(transact.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " ERROR : "+e.getMessage());
		}
				
		return feehome();
	}	
	
	
	private String checkfeecode;
	
	public String getCheckfeecode() {
		return checkfeecode;
	}
	public void setCheckfeecode(String checkfeecode) {
		this.checkfeecode = checkfeecode;
	}
	public void checkFeecodeexsist() throws IOException
	{
		HttpSession session=getRequest().getSession();
		String inst_id = comInstId();
		 
		String checkschme_qury ="select count(*) as exist from IFP_FEE_DESC where inst_id='"+inst_id+"' and FEE_CODE='"+checkfeecode+"'";
		System.out.println("checkschme----> "+checkschme_qury);
		int feecode_count = jdbctemplate.queryForInt(checkschme_qury);
		System.out.println("feecode_count===>  "+feecode_count);
		if(feecode_count == 1)
		{
			getResponse().getWriter().write("1");
			System.out.println("ONEsz");
		}
		else
		{
			getResponse().getWriter().write("0");
		System.out.println("Zerosss");	
		}
	}
	
	
	public String viewScheme()
	{
		trace("*************** View Scheme **********");
		enctrace("*************** View Scheme **********");

		HttpSession session=getRequest().getSession();
		String inst_id = comInstId(); 
		String qryschemelist="select FEE_CODE, FEE_DESC, STATUS_FLAG, USER_NAME, to_char(INTRODATE,'DD-MON-YY') as INTRO_DATE from IFP_FEE_DESC WHERE INST_ID='"+inst_id+"' order by INTRODATE";
		enctrace("qryschemelist "+qryschemelist);
		try
		{
			List resschemelist = jdbctemplate.queryForList(qryschemelist);
			trace("resschemelist "+resschemelist.size());
			if(resschemelist.isEmpty())
			{
				session.setAttribute("curerr","E");
				session.setAttribute("curmsg","No Fee Configured");
				trace("No Fee Configured");
			}else
			{
				setRunningSchemeList(resschemelist);
				session.setAttribute("curerr","S");
			}
		}
		catch( Exception ex )
		{
			session.setAttribute("curerr","E");
			session.setAttribute("curmsg","Error in qry while selecting sheme list :");	
			trace("Error in qry while selecting sheme list" +ex.getMessage());
			trace("\n\n"); 
			enctrace("\n\n");
		}
		trace("\n\n"); 
		enctrace("\n\n");
		return "schemelist";
	}
	
	public String viewSchemedetails()
	{
		System.out.println("Next To SELECT BOX");
		HttpSession session=getRequest().getSession();
		String inst_id = comInstId();
		List actioncodes;
		 
		String feecode = getRequest().getParameter("feecode");
		System.out.println("feecode--> "+feecode);
		String qryschemelist="select FEE_CODE, FEE_DESC, STATUS_FLAG, USER_NAME, to_char(INTRODATE,'DD-MON-YY') as INTRO_DATE from IFP_FEE_DESC WHERE INST_ID='"+inst_id+"' AND FEE_CODE='"+feecode+"' order by INTRODATE";
		System.out.println("qryschemelist====> "+qryschemelist);
		String qryactioncodes="select ACTION_CODE, ACTION_DESC from IFACTIONCODES where INST_ID = '"+inst_id+"' AND ACTION_STATUS ='1'"; 
		String qryschemfeelist="select FEE_ACTION, FEE_AMOUNT,DECODE(FEE_MODE,'F','FLAT FEE','P','PERCENTAGE FEE') AS FEE_MODE from IFP_FEE_MASTER where inst_id='"+inst_id+"' AND FEE_CODE='"+feecode+"'";
		try
		{
			List resschemelist = jdbctemplate.queryForList(qryschemelist);
			System.out.println("resschemelist====> "+resschemelist);
			if(resschemelist.isEmpty())
			{
				session.setAttribute("curerr","E");
				session.setAttribute("curmsg","No Fee Configured");
			}else
			{
				setRunningSchemeList(resschemelist);
				session.setAttribute("curerr","S");
			}
			actioncodes = jdbctemplate.queryForList(qryactioncodes);
			System.out.println("Lis of Actioj Code is-----> "+actioncodes);
			if(actioncodes.isEmpty())
			{
				System.out.println("No Fee Actions Configured");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "No Fee Actions Configured");
			}
			else
			{
				System.out.println("Actions Configured");
				setSchemeactionlist(actioncodes);
				session.setAttribute("curmsg", "");
			}
			List schemefeelist = jdbctemplate.queryForList(qryschemfeelist);
			if(schemefeelist.isEmpty())
			{
				System.out.println(" Fee Details Not Found "+schemefeelist);
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "No Fee Actions Configured");
			}
			else{
				System.out.println(" Fee Details Found "+schemefeelist);
				setSchemeFeeList(schemefeelist);
			}
		}
		catch( Exception ex )
		{
			System.out.println( " Error in qry while selecting sheme list " +ex.getMessage());
			session.setAttribute("curerr","E");
			session.setAttribute("curmsg","ERROR :"+ex.getMessage());			
		}
		
		return "viewScheme";
	}

	public String feeStatusupdate(){
		String msg=null;
		String qryschememanage = null;
		HttpSession session=getRequest().getSession();
		String inst_id = (String)session.getAttribute("Instname");
		 
		
		
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);	
		
		System.out.println( "flag===>"+ getRequest().getParameter("flagcode"));
		 
		String schemecodenew = getRequest().getParameter("schemecode");
		
		if ( getRequest().getParameter("flagcode").toString().equals("1")){
			qryschememanage = "UPDATE IFP_FEE_DESC SET STATUS_FLAG='2' WHERE INST_ID='"+inst_id+"' AND FEE_CODE='"+schemecodenew+"'";
			msg = " Status Disabled ";
			System.out.println(qryschememanage);
		}else if ( getRequest().getParameter("flagcode").toString().equals("2")){
			qryschememanage = "UPDATE IFP_FEE_DESC SET STATUS_FLAG='1' WHERE INST_ID='"+inst_id+"' AND FEE_CODE='"+schemecodenew+"'";
			System.out.println(qryschememanage);
			msg = " Status Enabled ";
		}
		
		try{
			int update_status = jdbctemplate.update(qryschememanage);
			if(update_status == 1)
			{
				txManager.commit(status);
				System.out.println( "commited");
				session.setAttribute("preverr", "S");
				session.setAttribute("prevmsg", msg);
			}
			else{
				txManager.rollback(status);
				System.out.println( "Txn Rolled Back ");
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Fee Status Not Changed");
			}
			
		}catch(Exception e){
			txManager.rollback(status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "Error : "+e.getMessage());
			System.out.println(e);
		}
		//return "schemeedit";
		//return "viewScheme";
		return viewafterupdate(inst_id,schemecodenew,jdbctemplate );
	}
	private char viewstatus;
	public char getViewstatus() {
		return viewstatus;
	}
	public void setViewstatus(char viewstatus) {
		this.viewstatus = viewstatus;
	}
	public String viewSchemeFeeStruct()
	{
		HttpSession session=getRequest().getSession();
		String inst_id = comInstId();
	 
		String fee_code = getRequest().getParameter("schemecode");
		System.out.println(" FEE CODE FROM QURY STRING IS ---> "+fee_code);
		String qryschemfeelist="select FEE_ACTION, FEE_AMOUNT,DECODE(FEE_MODE,'F','FLAT FEE','P','PERCENTAGE FEE') AS FEE_MODE from IFP_FEE_MASTER where inst_id='"+inst_id+"' AND FEE_CODE='"+fee_code+"'";
		try {
			List schemefeelist = jdbctemplate.queryForList(qryschemfeelist);
			if(schemefeelist.isEmpty())
			{
				System.out.println(" Fee Details Not Found "+schemefeelist);
				setViewstatus('N');
			}
			else{
				System.out.println(" Fee Details Found "+schemefeelist);
				setSchemeFeeList(schemefeelist);
				setViewstatus('Y');
			}
		} catch (Exception e) {
			setViewstatus('E');
		}
		return "viewfeestructure";

	}
	private List editdetailslist;
	public List getEditdetailslist() {
		return editdetailslist;
	}
	public void setEditdetailslist(List editdetailslist) {
		this.editdetailslist = editdetailslist;
	}
	private String bean_fee_code;
	private String bean_fee_desc;
	
	
	public String getBean_fee_code() {
		return bean_fee_code;
	}
	public void setBean_fee_code(String bean_fee_code) {
		this.bean_fee_code = bean_fee_code;
	}
	public String getBean_fee_desc() {
		return bean_fee_desc;
	}
	public void setBean_fee_desc(String bean_fee_desc) {
		this.bean_fee_desc = bean_fee_desc;
	}
	public String schemeEdit() 
	{
		HttpSession session=getRequest().getSession();
		String inst_id = comInstId();
		List actioncodes;
		 
		String feecode = getRequest().getParameter("feecode");
		System.out.println("feecode--> "+feecode);
		String qryschemelist="select FEE_CODE, FEE_DESC, STATUS_FLAG, USER_NAME, to_char(INTRODATE,'DD-MON-YY') as INTRO_DATE from IFP_FEE_DESC WHERE INST_ID='"+inst_id+"' AND FEE_CODE='"+feecode+"' order by INTRODATE";
		System.out.println("qryschemelist====> "+qryschemelist);
		String qryactioncodes="select ACTION_CODE, ACTION_DESC from IFACTIONCODES where INST_ID = '"+inst_id+"' AND ACTION_STATUS ='1'"; 
		String qryschemfeelist="select FEE_ACTION, FEE_AMOUNT,DECODE(FEE_MODE,'F','FLAT FEE','P','PERCENTAGE FEE') AS FEE_MODE from IFP_FEE_MASTER where inst_id='"+inst_id+"' AND FEE_CODE='"+feecode+"'";
		try
		{
			List resschemelist = jdbctemplate.queryForList(qryschemelist);
			System.out.println("resschemelist====> "+resschemelist);
			if(resschemelist.isEmpty())
			{
				session.setAttribute("curerr","E");
				session.setAttribute("curmsg","No Fee Configured");
			}else
			{
				setRunningSchemeList(resschemelist);
				session.setAttribute("curerr","S");
			}
			actioncodes = jdbctemplate.queryForList(qryactioncodes);
			System.out.println("Lis of Actioj Code is-----> "+actioncodes);
			if(actioncodes.isEmpty())
			{
				System.out.println("No Fee Actions Configured");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "No Fee Actions Configured");
			}
			else
			{
				System.out.println("Actions Configured");
				setSchemeactionlist(actioncodes);
				session.setAttribute("curmsg", "");
			}
			List schemefeelist = jdbctemplate.queryForList(qryschemfeelist);
			if(schemefeelist.isEmpty())
			{
				System.out.println(" Fee Details Not Found "+schemefeelist);
			}
			else{
				System.out.println(" Fee Details Found "+schemefeelist);
				setSchemeFeeList(schemefeelist);
			}
		}
		catch( Exception ex )
		{
			System.out.println( " Error in qry while selecting sheme list " +ex.getMessage());
			session.setAttribute("curerr","E");
			session.setAttribute("curmsg","ERROR :"+ex.getMessage());			
		}
		
		return "schemeedit";
	}
	
	public String updateFeeConfiguration()
	{
		System.out.println("<====== CALLING UPDATE ====>");
		IfpTransObj trnasct = commondesc.myTranObject("FEE", txManager);
		HttpSession session=getRequest().getSession();
		String inst_id = comInstId();
 
		try {
			
			String user_id = comUserId();
			
			String feecode =  getRequest().getParameter("feecode");
			String feedesc =  getRequest().getParameter("feedesc");
			System.out.println("feecode===> "+feecode+"\feedesc===> "+feedesc);
			String actionnames[] =  getRequest().getParameterValues("hidaactionlist"); 
			int list_len = actionnames.length;
			System.out.println("Action Lenght===> "+list_len);
			String feedesc_qury = "UPDATE IFP_FEE_DESC set INST_ID='"+inst_id+"', FEE_CODE='"+feecode+"', FEE_DESC='"+feedesc+"', STATUS_FLAG='1', USER_NAME='"+user_id+"',INTRODATE=(SYSDATE) WHERE INST_ID='"+inst_id+"' AND FEE_CODE='"+feecode+"'";
			System.out.println("fee desc ===> "+feedesc_qury);
			int feedesc_insert = jdbctemplate.update(feedesc_qury);
			System.out.println("Fee Desc ===> "+feedesc_insert);
			int all_status = 0;
			for(int j=0;j<list_len;j++)
			{
				int insert_status = 0;
				System.out.println("actionnames["+j+"]===> "+actionnames[j]);
				String actioncode = actionnames[j].toString();
				System.out.println("actioncode====> "+actioncode);
				String codeamt = getRequest().getParameter(actioncode);
				System.out.println("codeamt===> "+codeamt);
				String feedetails_qury = "UPDATE IFP_FEE_MASTER SET INST_ID='"+inst_id+"',FEE_CODE='"+feecode+"',FEE_ACTION='"+actioncode+"',FEE_AMOUNT='"+codeamt+"' WHERE INST_ID='"+inst_id+"'AND FEE_CODE='"+feecode+"' AND FEE_ACTION='"+actionnames[j]+"'";
				System.out.println("feedetails_qury====>"+feedetails_qury);
				insert_status = jdbctemplate.update(feedetails_qury);
				System.out.println("UPDATE STATUS===> "+insert_status);
				if(insert_status == 1)
				{
					all_status = all_status + 1;
				}else{
					break;
				}
			}
			System.out.println("all_status===>"+all_status+"\nlist_len===>"+list_len+"feedesc_insert===>"+feedesc_insert);
			if(all_status == list_len && feedesc_insert == 1)
			{
				trnasct.txManager.commit(trnasct.status);
				session.setAttribute("preverr", "S");
				session.setAttribute("prevmsg", " Fee Configured Successfully ");
				System.out.println("TXN COMMITED");
				
				
				
			}
			else{
				trnasct.txManager.rollback(trnasct.status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " Fee Configuration Failed ");
				System.out.println("TXN ROOLLED BACK");
			}

		}
		catch (Exception e) 
		{
			System.out.println("Error: "+e.getMessage());
			trnasct.txManager.rollback(trnasct.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " ERROR : "+e.getMessage());
		}

		return viewScheme();
	}
	
	public String schemeDelete()
	{
		IfpTransObj trnasct = commondesc.myTranObject("SCHEME", txManager);
		try {
			
			HttpSession session=getRequest().getSession();
			
			String inst_id = comInstId();
			String user_id = comUserId();
 
			String sheme_code =  getRequest().getParameter("fee_code");
			String sheme_desc =  getRequest().getParameter("fee_desc");
			System.out.println("sheme_code---> "+sheme_code+" sheme_desc==> "+sheme_desc);
			String delete_qury = "DELETE FROM IFP_FEE_DESC WHERE INST_ID='"+inst_id+"' AND FEE_CODE='"+sheme_code+"'";
			String delete_feedetails = "DELETE FROM IFP_FEE_MASTER WHERE INST_ID='"+inst_id+"' AND FEE_CODE='"+sheme_code+"'";
			System.out.println("delete_qury===> "+delete_qury+"delete_feedetails===> "+delete_feedetails);
			int del_feedesc = jdbctemplate.update(delete_qury);
			int del_feedet = jdbctemplate.update(delete_feedetails);
			if(del_feedesc == 1 && del_feedet>0)
			{
				trnasct.txManager.commit(trnasct.status);
				session.setAttribute("preverr", "S");
				session.setAttribute("prevmsg", sheme_desc+" Fee Configuration Deleted ");
				System.out.println("TXN COMMITED");
			}else
			{
				trnasct.txManager.rollback(trnasct.status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", sheme_desc+" Fee Configuration Deletion Failed ");
				System.out.println("TXN ROOLLED BACK");
			}
		}
		catch (Exception e) 
		{
			trnasct.txManager.rollback(trnasct.status);
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "ERROR : "+e.getMessage());
			System.out.println("ERROR : "+e.getMessage());
		}
		return viewScheme();
	}
	//##################################################################################################
	
	public String viewafterupdate(String instid,String feecode,JdbcTemplate jtemp )
	{
		System.out.println("Next To SELECT BOX");
		HttpSession session=getRequest().getSession();
		String inst_id = comInstId();
		List actioncodes;
		//JdbcTemplate jtemp = new JdbcTemplate(dataSource);
		//String feecode = getRequest().getParameter("feecode");
		System.out.println("feecode--> "+feecode);
		String qryschemelist="select FEE_CODE, FEE_DESC, STATUS_FLAG, USER_NAME, to_char(INTRODATE,'DD-MON-YY') as INTRO_DATE from IFP_FEE_DESC WHERE INST_ID='"+inst_id+"' AND FEE_CODE='"+feecode+"' order by INTRODATE";
		System.out.println("qryschemelist====> "+qryschemelist);
		String qryactioncodes="select ACTION_CODE, ACTION_DESC from IFACTIONCODES where INST_ID = '"+inst_id+"' AND ACTION_STATUS ='1'"; 
		String qryschemfeelist="Select FEE_ACTION, FEE_AMOUNT,DECODE(FEE_MODE,'F','FLAT FEE','P','PERCENTAGE FEE') AS FEE_MODE from IFP_FEE_MASTER where inst_id='"+inst_id+"' AND FEE_CODE='"+feecode+"'";
		try
		{
			List resschemelist = jtemp.queryForList(qryschemelist);
			System.out.println("resschemelist====> "+resschemelist);
			if(resschemelist.isEmpty())
			{
				session.setAttribute("curerr","E");
				session.setAttribute("curmsg","No Fee Configured");
			}else
			{
				setRunningSchemeList(resschemelist);
				session.setAttribute("curerr","S");
			}
			actioncodes = jtemp.queryForList(qryactioncodes);
			System.out.println("Lis of Actioj Code is-----> "+actioncodes);
			if(actioncodes.isEmpty())
			{
				System.out.println("No Fee Actions Configured");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg", "No Fee Actions Configured");
			}
			else
			{
				System.out.println("Actions Configured");
				setSchemeactionlist(actioncodes);
				session.setAttribute("curmsg", "");
			}
			List schemefeelist = jtemp.queryForList(qryschemfeelist);
			if(schemefeelist.isEmpty())
			{
				System.out.println(" Fee Details Not Found "+schemefeelist);
			}
			else{
				System.out.println(" Fee Details Found "+schemefeelist);
				setSchemeFeeList(schemefeelist);
			}
		}
		catch( Exception ex )
		{
			System.out.println( " Error in qry while selecting sheme list " +ex.getMessage());
			session.setAttribute("curerr","E");
			session.setAttribute("curmsg","ERROR :"+ex.getMessage());			
		}
		
		return "viewScheme";
	}
	
	/*************pavithra begins *******************/
	String feenamebean;
	public String getFeenamebean() {
		return feenamebean;
	}

	public void setFeenamebean(String feenamebean) {
		this.feenamebean = feenamebean;
	}
	boolean issubfeeexist = false;
	public boolean isIssubfeeexist() {
		return issubfeeexist;
	}

	public void setIssubfeeexist(boolean issubfeeexist) {
		this.issubfeeexist = issubfeeexist;
	}
	List subfeelist;
	public List getSubfeelist() {
		return subfeelist;
	}

	public void setSubfeelist(List subfeelist) {
		this.subfeelist = subfeelist;
	}
	
	public void deleteFee() throws IOException
	{  
		trace("*************** Deleting Fee **********");
		enctrace("*************** Deleting Fee **********");

		String feecode= getRequest().getParameter("feeid");
		trace("feecode "+feecode);
		HttpSession session=getRequest().getSession();
		String errorstatus = "Deleted Succussfully";
		IfpTransObj transobj =  getCommondesc().myTranObject("DELFEE", txManager);
		try
		{
 
			String del_qury="UPDATE IFP_FEE_SUBCODE SET DELETED_FLAG='1' where FEE_SUBCODE='"+feecode+"'";
			enctrace("del_qury	"+del_qury);
			
			int del_status=jdbctemplate.update(del_qury);
			if(del_status<=0){
				transobj.txManager.rollback(transobj.status);
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Error while deleting the Fee configuration ");	
				trace("Error while deleting the Fee configuration ");
			}else{
			transobj.txManager.commit(transobj.status);
			session.setAttribute("preverr", "S");
			session.setAttribute("prevmsg", "Fee Deleted Sucessfully");
			trace("Fee Deleted Sucessfully");
			}
		}
		catch (Exception e) 
		{
			transobj.txManager.rollback(transobj.status);
			session.setAttribute("preverr","E");
			session.setAttribute("prevmsg", "Error while deleting the Fee ");
			trace("Exception :Error while deleting the Fee "+e.getMessage());
			trace("\n\n"); 
			enctrace("\n\n");
		}
		trace("\n\n"); 
		enctrace("\n\n");
	}
	
	String feesubdesc;
	
	 public String getFeesubdesc() {
		return feesubdesc;
	}
	public void setFeesubdesc(String feesubdesc) {
		this.feesubdesc = feesubdesc;
	}
	public String getViewDetails() 
		{ 

			HttpSession session=getRequest().getSession();
			 
			String inst_id = comInstId();
			String subfeecode = getRequest().getParameter("subcode");
			System.out.println("subfeecode	"+subfeecode);
			String feedetails = "SELECT FEE_SUBCODE,FEE_ACTION,FEE_AMOUNT,DECODE(FEE_MODE,'F','FLAT','P','PERCENTAGE') AS FEEMODE FROM IFP_FEE_MASTER WHERE FEE_SUBCODE='"+subfeecode+"'";
			System.out.println("feedetails	"+feedetails);
			List fee_Details = jdbctemplate.queryForList(feedetails);
			System.out.println("feeDetails====> "+fee_Details);
			String fee_subcodeDesc = null;
			if(fee_Details.isEmpty())
			{
				session.setAttribute("curerr","E");
				session.setAttribute("curmsg","No Fee Details");
			}else
			{
				ListIterator itr = fee_Details.listIterator();
				while(itr.hasNext())
				{
					Map map = (Map) itr.next();
					String feesubcode = (String) map.get("FEE_SUBCODE");
					System.out.println("feesubcode	"+feesubcode);
					fee_subcodeDesc = getFeesubcode(inst_id,feesubcode, jdbctemplate);
					System.out.println("fee_subcodeDesc	"+fee_subcodeDesc);
					map.put("FEESUBCODE",fee_subcodeDesc);
					itr.remove();
					itr.add(map);	
				}
				setFeesubdesc(fee_subcodeDesc);
				System.out.println("setFeesubdesc	"+getFeesubdesc());
				setRunningSchemeList(fee_Details);
				session.setAttribute("curerr","S");
			}
			return "getViewDetails";
		}
	 
	 
	 public String getFeesubcode(String inst_id,String feesubcode,JdbcTemplate jdbcTemplate)
	 {
	  String getsubfee = "SELECT SUBFEE_DESC FROM IFP_FEE_SUBCODE WHERE FEE_SUBCODE='"+feesubcode+"'";
	  System.out.println("getsubfee "+getsubfee);
	  String getSubfeecode = (String)jdbcTemplate.queryForObject(getsubfee, String.class);
	  System.out.println("getSubfeecode "+getSubfeecode);
	  return getSubfeecode;
	 }
	 
	/*************pavithra end *******************/
	
	//###############################################################################################
}
