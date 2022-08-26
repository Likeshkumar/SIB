package com.ifp.Action;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifd.Bin.AddBinActionBeans;
import com.ifp.beans.AuditBeans;
import com.ifp.beans.CourierConfigBeans;
import com.ifp.dao.CourierConfigDAO;
import com.ifp.util.CommonDesc;  
import com.ifp.util.CommonUtil;  
import com.ifp.util.IfpTransObj;
import com.ifg.Config.Licence.Licensemanager;

public class CourierConfig extends BaseAction {
	private static final long serialVersionUID = 1L;
	Logger logger = Logger.getLogger(this.getClass());
	Licensemanager chcbin=new Licensemanager(); 
	static Boolean  initmail = true; 
	static  String parentid = "000";
	CourierConfigBeans courierbean = new CourierConfigBeans();
	CourierConfigDAO courierdao = new CourierConfigDAO (); 
	CommonUtil comutil= new CommonUtil(); 
	private JdbcTemplate jdbctemplate = new JdbcTemplate();	
	private PlatformTransactionManager  txManager  = new DataSourceTransactionManager();
	AddBinActionBeans binbean = new AddBinActionBeans();
	CommonDesc commondesc = new CommonDesc();
	AuditBeans auditbean = new AuditBeans();
	
	
	public CourierConfigBeans getCourierbean() {
		return courierbean;
	}

	public void setCourierbean(CourierConfigBeans courierbean) {
		this.courierbean = courierbean;
	}

	public String comInstId( HttpSession session ){
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	}
	
	public String comUserCode( HttpSession session ){
		String instid = (String)session.getAttribute("USERID"); 
		return instid;
	}
	
	 
	public AuditBeans getAuditbean() {
		return auditbean;
	}
	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
	}
	public AddBinActionBeans getBinbean() {
		return binbean;
	}
	public void setBinbean(AddBinActionBeans binbean) {
		this.binbean = binbean;
	}
	public CommonUtil getComutil() {
		return comutil;
	}
	public void setComutil(CommonUtil comutil) {
		this.comutil = comutil;
	} 
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
	
	public String addCourierHome(){
		courierbean.actionname="addCourierActionCourierConfig.do";
		trace("Adding Couier home....");
		return "courier_add_home";
	}
	
	public String addCourierAction(){
		trace("Adding Courier Action....");
		HttpSession session = getRequest().getSession();
		IfpTransObj transct = commondesc.myTranObject("ADDCOURER", txManager);
		String instid = comInstId(session);
		String couriername = getRequest().getParameter("crname");
		String courieroffice = getRequest().getParameter("courieroffice");
		String contactnumber = getRequest().getParameter("contactnumber");
		String cstatus =  getRequest().getParameter("cstatus");
		try{
			int x = courierdao.insertCourierData(instid, couriername, courieroffice, contactnumber, cstatus, jdbctemplate);
			if( x < 0 ){
				transct.txManager.rollback(transct.status);
				addActionError("Unable to process");
				return "required_home";
			}
			
			transct.txManager.commit(transct.status);
			addActionMessage("Courier Details Configured Successfully....");
		}catch(Exception e ){
			e.printStackTrace();
			trace("Exception...."+ e.getMessage());
			addActionError("Unable to continue the process"); 
		}
		return addCourierHome();
	}
	
	
	public String viewCourierDetails(){
		courierbean.actionname="johnpritto!!!!!!!!!!!!!";
		trace("Adding Courier Action....");
		HttpSession session = getRequest().getSession(); 
		String instid = comInstId(session);
	 
		
		try{
			 List clist = courierdao.getAllCourierList(instid, jdbctemplate);
			 if( clist.isEmpty() ){
				 addActionError("No Courier Details Available");
				 return "required_home";
			 }
			 
			 courierbean.setAllcourierlist(clist);
			 
			 System.out.println(courierbean.allcourierlist);
		}catch(Exception e ){
			e.printStackTrace();
			trace("Exception...."+ e.getMessage());
			addActionError("Unable to continue the process"); 
		}
		return "viewcourier_list";
	}
	
	public String editCourier(){
		courierbean.actionname="editCourierActionCourierConfig.do";
		HttpSession session = getRequest().getSession(); 
		String instid = comInstId(session);
		String courierid = getRequest().getParameter("courierid");
		try{
			List courierdata = courierdao.getCourierList(instid, courierid, jdbctemplate);
			if( courierdata.isEmpty() ){
				addActionError("Unable to get courier data ");
				return "required_home";
			}
			Iterator itr = courierdata.iterator();
			while( itr.hasNext() ){
				Map mp = (Map)itr.next();
				courierbean.courierid=(String)mp.get("COURIERMASTER_ID");
				courierbean.couriername=(String)mp.get("COURIER_NAME");
				courierbean.courieroffice=(String)mp.get("COURIER_HOFFICE");
				courierbean.contactnumber=(String)mp.get("COURIER_CONTACTNO");
				courierbean.cstatus=(String)mp.get("COURIER_STATUS");
			}
		}catch(Exception e ){
			e.printStackTrace();
			trace("Exception...."+ e.getMessage());
			addActionError("Unable to continue the process"); 
		}
		return "courier_add_home";
	}
	
	public String editCourierAction(){
		trace("Adding Courier Action....");
		HttpSession session = getRequest().getSession();
		IfpTransObj transct = commondesc.myTranObject("ADDCOURER", txManager);
		String instid = comInstId(session);
		String courierid  = getRequest().getParameter("courierid");
		String couriername = getRequest().getParameter("crname");
		String courieroffice = getRequest().getParameter("courieroffice");
		String contactnumber = getRequest().getParameter("contactnumber");
		String cstatus =  getRequest().getParameter("cstatus");
		try{
			int x = courierdao.updateCourierDetails(instid, courierid, couriername, courieroffice, contactnumber, cstatus, jdbctemplate);
			if( x < 0 ){
				transct.txManager.rollback(transct.status);
				addActionError("Unable to process");
				return "required_home";
			}
			
			transct.txManager.commit(transct.status);
			addActionMessage("Courier Details Updated Successfully....");
		}catch(Exception e ){
			e.printStackTrace();
			trace("Exception...."+ e.getMessage());
			addActionError("Unable to continue the process"); 
		}
		return  this.viewCourierDetails();
	}
	 
}
