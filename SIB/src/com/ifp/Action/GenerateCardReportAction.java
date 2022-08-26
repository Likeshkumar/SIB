package com.ifp.Action;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

public class GenerateCardReportAction extends BaseAction
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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

	
	private String reportname;
	public String getReportname() {
		return reportname;
	}

	public void setReportname(String reportname) {
		this.reportname = reportname;
	}

	public String renderReport()
	{
		HttpSession session = getRequest().getSession(); 
		String i_Name = (String)session.getAttribute("Instname");
		String in_name = i_Name.toUpperCase();
		String reportId=(getRequest().getParameter("reportvalue"));
		String from_date=(getRequest().getParameter("fromdate").trim());
		String to_date=(getRequest().getParameter("todate").trim());
		System.out.println("=======================================================================================");
		System.out.println("The Seleted Report is "+reportId+" Institution is "+in_name);
		System.out.println("The Seleted From Date is "+from_date);
		System.out.println("The Seleted To Date is "+to_date);
		System.out.println("=======================================================================================");
		List card_list;
		String[] reportytype = reportId.split("~");
		String reportid  = reportytype[0];
		String type = reportytype[1];
		System.out.println("reportid===> "+reportid+"   Type ===> "+type);
		String report_name="",report_titel="",table="";
		String Query_toProcess = "null";
		try
		{
		int option = Integer.parseInt(reportid);
		switch(option)
		{
			case 1:

				table = "PERS_CARD_ORDER";
				report_name = "PersonalCardOrderReport.pdf";
				report_titel = "PERSONAL CARD ORDER REPORT";
				if(type.equals("I"))
				{
					table = "INST_CARD_ORDER";
					report_name = "NonPersonalCardOrderReport.pdf";
					report_titel = "NON-PERSONAL CARD ORDER REPORT";
				}

				System.out.println("Card Order Report");
				//Query_toProcess = "select * from IFPCARD_ORDER_DETAILS where ORDER_STATUS ='U' and to_char(ordered_date,'DD-MM-YYYY') between '"+from_date+"' and '"+to_date+"'";
				Query_toProcess ="select co.order_ref_no AS ORDER_REF_NO ,co.CARD_QUANTITY as COUNT, pd.product_name AS PRODUCT_NAME, to_char(co.ordered_date,'DD-MON-YYYY') AS ORDERED_DATE from "+table+" co,IFP_INSTPROD_DETAILS pd where co.inst_id=pd.inst_id and co.PRODUCT_CODE=pd.PRODUCT_CODE and co.ORDER_STATUS='01' and co.MKCK_STATUS in ('M','P') and to_char(co.ordered_date,'DD/MM/YYYY') between '"+from_date+"' and '"+to_date+"' and co.inst_id='"+in_name+"' order by co.ordered_date";
				System.out.println("Query_toProcess ---- "+Query_toProcess);
				card_list=jdbctemplate.queryForList(Query_toProcess);
				System.out.println("reuults _____________ ---- "+card_list);

				if(card_list.isEmpty())
				{
					
					return "no_data_period";
				}
				else
				{
					formatReport(card_list);
					setReportname(report_name);
					System.out.println("Report Title ===> "+report_titel);
					
					return "cardorderreport";
				}
				
				
			case 2:
				
				System.out.println("Card Generaed Report");
				String table1 = "PERS_CARD_PROCESS";
				report_name="PersonalCardGeneratedReport.pdf";
				if(type.equals("I"))
				{
					table1 = "INST_CARD_PROCESS";
					report_name="NonPersonalCardGeneratedReport.pdf";
				}
				String wait_Pin_Generation = "select distinct(cp.ORDER_REF_NO) as ORDER_REF_NO,to_char(cp.GENERATED_DATE,'DD-MON-YYYY') AS GEN_DATE,pd.PRODUCT_NAME as PRODUCT_NAME from "+table1+" cp,IFP_INSTPROD_DETAILS pd where cp.INST_ID='"+in_name+"' and cp.CARD_STATUS='01' and cp.INST_ID=pd.INST_ID and cp.PRODUCT_CODE=pd.PRODUCT_CODE order by GEN_DATE"; 
				System.out.println("wait_Pin_Generation _____________ ---- "+wait_Pin_Generation);
				card_list=jdbctemplate.queryForList(wait_Pin_Generation);
				if(card_list.isEmpty())
				{
					
					return "no_data_period";
				}
				else
				{
					formatCardgenreport(card_list);
					setReportname(report_name);
					return "cardgenreport";
				}
				
			case 3:
				System.out.println("OREDERS WAITING FOR PRE GENERATION REPORTS");
				String wait_Pre_Generation = "select distinct(cp.refnum) AS ORDER_REF_NO, pd.product_name AS PRODUCT_NAME,to_char(cp.maker_date,'DD-MON-YYYY') AS ORDERED_DATE from IFPCARD_PRODUCTION cp ,IFP_INSTPROD_DETAILS pd "+
				"where cp.card_type= pd.cardtype_id and cp.inst_id= pd.inst_id and cp.card_status='F' and "+
				"to_char(cp.maker_date,'DD/MM/YYYY') between '"+from_date+"' and '"+to_date+"' and "+
				"cp.inst_id='"+in_name+"' order by ORDERED_DATE desc";
				System.out.println("wait_Pin_Generation _____________ ---- "+wait_Pre_Generation);
				card_list=jdbctemplate.queryForList(wait_Pre_Generation);
				if(card_list.isEmpty()) {
					
					return "no_data_period";
				} else {
					formatReport(card_list);
					return "pre_gen_wait";
				}
				
				
			default:
				System.out.println("ORDERS WAITING FOR PRE GENERATION");
				break;
				
		}
		
		System.out.println("Switch Get the Queries"+Query_toProcess);
		session.setAttribute("Errorstat", "E");
		session.setAttribute("ErrorMessage", "No Reports Configured ");
		return "report_error";
		}
		catch (Exception e) 
		{
			session.setAttribute("Errorstat", "E");
			session.setAttribute("ErrorMessage", "Error While Generating The Report "+e.getMessage());
			return "report_error";
		}
	
	}
	
	
	
	public void formatReport(List card_list)
	{
		Iterator itr_card_list=card_list.iterator();
		String ORDER_REF_NO,COUNT,PRODUCT_NAME,ORDERED_DATE;
		 while(itr_card_list.hasNext())
					{	
		   				Map mapper_orderdetails=(Map)itr_card_list.next();
						ORDER_REF_NO=((String)mapper_orderdetails.get("ORDER_REF_NO"));
						COUNT=((String)mapper_orderdetails.get("COUNT"));
						ORDERED_DATE=((String)mapper_orderdetails.get("ORDERED_DATE"));
						PRODUCT_NAME=((String)mapper_orderdetails.get("PRODUCT_NAME"));
						
						
					}
		
	}

	public void formatCardgenreport(List card_list)
	{
		Iterator itr_card_list=card_list.iterator();
		String ORDER_REF_NO,COUNT,PRODUCT_NAME,ORDERED_DATE;
		 while(itr_card_list.hasNext())
					{	
		   				Map mapper_orderdetails=(Map)itr_card_list.next();
						ORDER_REF_NO=((String)mapper_orderdetails.get("ORDER_REF_NO"));
						ORDERED_DATE=((String)mapper_orderdetails.get("GEN_DATE"));
						PRODUCT_NAME=((String)mapper_orderdetails.get("PRODUCT_NAME"));
						
					}
		
	}

	
}
