package com.reports.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;
import com.ifp.beans.ReconBeans;
import com.ifp.util.CommonDesc;

public class TransactionReportDao extends BaseAction
{
	ReconBeans reconbean = new ReconBeans();
	CommonDesc commondesc = new CommonDesc();
	private  DataSource dataSource;   
	
	public String comInstId(HttpSession session){ 
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	} 
	public String comUserCode( HttpSession session ){ 
		String instid = (String)session.getAttribute("USERID"); 
		return instid;
	}
	private ByteArrayInputStream input_stream;
	private String report_name;
	public String getReport_name() {
		return report_name;
	}
	public void setReport_name(String report_name) {
		this.report_name = report_name;
	}
	public ByteArrayInputStream getInput_stream() {
		return input_stream;
	}
	public void setInput_stream(ByteArrayInputStream input_stream) {
		this.input_stream = input_stream;
	}
	private ByteArrayOutputStream output_stream;
	public ByteArrayOutputStream getOutput_stream() {
		return output_stream;
	}
	public void setOutput_stream(ByteArrayOutputStream output_stream) {
		this.output_stream = output_stream;
	}
	 
	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	public ReconBeans getReconbean() {
		return reconbean;
	}

	public void setReconbean(ReconBeans reconbean) {
		this.reconbean = reconbean;
	}

	public List corporateList( String productid,String instid, JdbcTemplate jdbctemplate) throws Exception {
		List corp_result=null;
		try{
		String corp_query = "select SUB_PRODUCT_NAME as CompanyName, CORPRATE_CARDNO as CARDNO from IFP_INSTPROD_DETAILS where PRODUCT_CODE='"+productid+"' AND INST_ID='"+instid+"'";
		corp_result = jdbctemplate.queryForList(corp_query);
		}catch( Exception e ){
			trace( "waitingForInstCardProcess__" +e );
			trace( "Exception : " + e.getMessage());
			e.printStackTrace();
		}
		return corp_result;
	}
	public String getCinByCard(String instid, String cardno,JdbcTemplate jdbctemplate ) throws Exception {
		String customerid = null;
		try {
			String customernamemobileqry = "SELECT CIN FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
			enctrace("customernameqry :" + customernamemobileqry);
			customerid = (String)jdbctemplate.queryForObject(customernamemobileqry,String.class);
		} catch (Exception e) {}
			return customerid;
	}
	public List fchCustNameMblNum(String instid, String custid, JdbcTemplate jdbctemplate) throws Exception{
		List custname = null;
		String custnameqry ="SELECT MOBILE_NO,( FNAME || ' ' || DECODE(MNAME,UPPER('NA'),'') || ' '|| LNAME ) AS NAME FROM IFP_CUSTINFO_PRODUCTION WHERE INST_ID='"+instid+"' AND CIN='"+custid+"' AND ROWNUM<=1";
		enctrace( "custnameqry__ " + custnameqry);		
		try {
			custname = jdbctemplate.queryForList(custnameqry);
			System.out.println("custname custname custname "+custname);
		} catch (EmptyResultDataAccessException e){e.printStackTrace();}
		return  custname ;
	}
}
