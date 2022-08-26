package com.ifp.maintain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

import java.io.IOException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.maintain.dao.AlphaGasPriceActionDAO; 
public class AlphaGasPriceAction extends BaseAction {
	private static final long serialVersionUID = -8376161637970676446L;
	
	JdbcTemplate jdbctemplate = new JdbcTemplate();
	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}
	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}
	
	CommonDesc commondesc = new CommonDesc();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager(); 
	
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
	
	AlphaGasPriceActionDAO alphagaspricedao = new AlphaGasPriceActionDAO();
	public String comInstId( HttpSession session ){
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	} 
	public String comUserCode( HttpSession session ){
		String usercode = (String)session.getAttribute("USERID"); 
		return usercode;
	}
	public String comuserType( HttpSession session){
		String usertype = (String)session.getAttribute("USERTYPE"); 
		return usertype;
	}
	public String comUsername( HttpSession session ){
		String username = (String)session.getAttribute("USERNAME"); 
		return username;
	}

	private List productlist;
	public List getProductlist() {
		return productlist;
	}
	public void setProductlist(List productlist) {
		this.productlist = productlist;
	}
	
	public String alphaGasPrice(){
		HttpSession session = getRequest().getSession();
		try {
			String inst_id =comInstId( session );
			List list_prodlist=commondesc.getProductList(inst_id, jdbctemplate, session);
			if (!(list_prodlist.isEmpty())){
				setProductlist(list_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg","");
				trace("Product List is ===> "+list_prodlist);
			} else{
				trace("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg"," No Product Details Found ");
			}
		}catch (Exception e) {
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg"," Error While Fetching The Product Details  "+ e.getMessage());
		}
		return "alphaGasPrice";
	}
	private List authList;	
	public List getAuthList() {
		return authList;
	}
	public void setAuthList(List authList) {
		this.authList = authList;
	}
	
	public String authListalphaGasPrice(){
		HttpSession session = getRequest().getSession();
		try{
			String instid = comInstId( session );
			String unit_price = getRequest().getParameter("unitprice");
			String productid = getRequest().getParameter("productid");		
			String product_desc=commondesc.getProductdesc(instid, productid, jdbctemplate);
			trace("product_desc --- "+product_desc);
			trace(unit_price);	
			List viewauthList = alphagaspricedao.getUnitPriceListForAuth(instid, productid, jdbctemplate);
			if(!viewauthList.isEmpty()){
				setAuthList(viewauthList);
			}else{
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg","No records found to authorize ");
			}			
		}catch (Exception e) {
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg"," Error while fetching unit price authorize list"+ e.getMessage());
		}
		return "authalphaGasPrice";
	}
	public String alphaGaspriceauthorize(){
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("ALPHAGAS", txManager);
		try{
			String instid = comInstId( session );
			String product_id = getRequest().getParameter("productid");
			String authorize = getRequest().getParameter("authorize");
			String unitprice = getRequest().getParameter("unitprice");
			trace("unitprice ---- "+unitprice);
			trace("Total Orders Selected ===> "+product_id);						
			String username = comUsername( session );
			trace("userid  ---  "+username);
			int check = 0,historytoalphaprice_result = 0,delpricefrmHstry_result = 1;String msg = null;
			if(authorize.equals("Authorize")){
				trace("Entered Authorized");
				check = alphagaspricedao.updateAuthUnitPrice(instid,unitprice,username,product_id,jdbctemplate);
				trace(" ***** check ****** "+check);
				List getList_exist = alphagaspricedao.getAuthorizedUnitPriceList(instid,product_id,jdbctemplate);
				if(!getList_exist.isEmpty()){
				trace("Enterede exsisting one");
				delpricefrmHstry_result = alphagaspricedao.deletepricefromHistory(instid,unitprice,product_id, jdbctemplate);
				trace("delpricefrmHstry_result --- "+delpricefrmHstry_result);
				}
				historytoalphaprice_result = alphagaspricedao.movehistorytoalphaprice(instid, product_id,unitprice,jdbctemplate);
				trace(" ***** historytoalphaprice_result ****** "+historytoalphaprice_result);
				if(check >= 1 && historytoalphaprice_result >= 1 && delpricefrmHstry_result >=1){
					session.setAttribute("preverr", "S");
					session.setAttribute("prevmsg"," Authorized successfully.");	
					txManager.commit(transact.status);
				}else{
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " Error while authorizing ");				
					txManager.rollback(transact.status);
				}
			}else{
				trace("Entered Deauthorized");
				check = alphagaspricedao.updateDeAuthUnitPrice(instid,unitprice,username,product_id,jdbctemplate);
				trace("check --- "+check);
				if( check >= 1 ){
					session.setAttribute("preverr", "S");
					session.setAttribute("prevmsg"," De-Authorized successfully." );	
					txManager.commit(transact.status);
					return authalphaGasPrice();
				}else{
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " Error while authorizing ");				
					txManager.rollback(transact.status);
					return authalphaGasPrice();
				}
			}
		}catch (Exception e){
			txManager.rollback(transact.status);
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg"," Error while fetching unit price authorize list"+ e.getMessage());
		}
		return authalphaGasPrice();
	}
	
	List historyList;
	public List getHistoryList(){
		return historyList;
	}	
	public void setHistoryList(List historyList){
		this.historyList = historyList;
	}
	
	private char statusflag;
	public char getStatusflag() {
		return statusflag;
	}
	public void setStatusflag(char statusflag) {
		this.statusflag = statusflag;
	}
	
	private char insertflag;
	public char getInsertflag() {
		return insertflag;
	}
	public void setInsertflag(char insertflag) {
		this.insertflag = insertflag;
	}
	
	public String insertprice(){
		HttpSession session = getRequest().getSession();
		IfpTransObj transact = commondesc.myTranObject("ALPHAPRICE", txManager);
		try {
			String instid = comInstId( session );
			String usertype = comuserType( session );
			String usercode = comUserCode( session );
			String username = comUsername( session );
			String unit_price = getRequest().getParameter("unitprice");
			trace("unit_price  ---  "+unit_price);
			String productid = getRequest().getParameter("productid");
			trace("productid  ---  "+productid);
			String type = getRequest().getParameter("type");
			trace("type  ---  "+type);
			String product_desc=commondesc.getProductdesc(instid, productid, jdbctemplate);
			trace("product_desc  ---  "+product_desc);
			int chck_authorize = alphagaspricedao.checkAuthorization(instid, productid,unit_price,jdbctemplate);
			trace("chck_authorize  ---  "+chck_authorize);
			if(	chck_authorize >= 1	){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Unable to continue the process...Product waiting for authorization");
				return alphaGasPrice();
			}			
			int updateinhist=1;
			if(!type.equals("E")){
				int chk_unitprice = alphagaspricedao.checkUnitprice(instid, productid,unit_price.trim(),jdbctemplate);
				trace("chk_unitprice ===  "+chk_unitprice);
				if(chk_unitprice  == 1){
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " Product has same unit price already.Configure with different unitprice");
					return alphaGasPrice();
				}
				trace("Entered into else part");
				String msg;if(type.equals("U")){
					msg="updated";
					updateinhist = alphagaspricedao.updateActivestatus(instid, productid,jdbctemplate);
					System.out.println("updateinhist -- "+updateinhist);
				}else{msg="inserted";}
				int insertinHistory = alphagaspricedao.insertUnitPrice(instid, productid, unit_price, product_desc, username,usercode, jdbctemplate);
				System.out.println("insertinHistory -- "+insertinHistory);
				if(insertinHistory < 0 && updateinhist < 0){
					txManager.rollback(transact.status);
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", "Unable to contine the process...");
					return "required_home";
				}else{					 				
					txManager.commit(transact.status);
					session.setAttribute("preverr", "S");
					session.setAttribute("prevmsg", "Unit price "+msg+" sucessfully.Waiting for Authorization ");
				}
			}else{
				int chk_unitprice = alphagaspricedao.checkUnitprice(instid, productid,unit_price.trim(),jdbctemplate);
				trace("chk_unitprice ===  "+chk_unitprice);
				if(chk_unitprice  == 1){
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg", " Product has same unit price already.Configure with different unitprice");
					return alphaGasPrice();
				}
				List viewauthList = alphagaspricedao.getAuthorizedUnitPriceList(instid, productid, jdbctemplate);
				trace("viewauthList--- "+viewauthList);
				if(!viewauthList.isEmpty()){
					setInsertflag('Y');
					setAuthList(viewauthList);
				}else{
					setInsertflag('N'); 
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg"," No records found ");
				}
				List viewHistoryList = alphagaspricedao.getHistoryList(instid, productid, jdbctemplate);
				trace("viewauthList--- "+viewHistoryList);
				if(!viewHistoryList.isEmpty()){
					setStatusflag('Y');
					setHistoryList(viewHistoryList);
				}else{
					setStatusflag('N'); 
					session.setAttribute("preverr", "E");
					session.setAttribute("prevmsg"," No records found ");
				}
			}
		}catch (Exception e){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", " Error While Fetching The Product Details  "+ e.getMessage());	
			txManager.rollback(transact.status);
			e.printStackTrace();
		}
		return alphaGasPrice();
	}
	public String authalphaGasPrice(){
		HttpSession session = getRequest().getSession();
		try {
			String inst_id =comInstId( session );
			List list_prodlist=commondesc.getProductList(inst_id, jdbctemplate, session);
			if (!(list_prodlist.isEmpty())){
				setProductlist(list_prodlist);
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg","");
				trace("Product List is ===> "+list_prodlist);
			} else{
				trace("No Product Details Found ");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg"," No Product Details Found ");
			}
		}catch (Exception e) {
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg"," Error While Fetching The Product Details  "+ e.getMessage());
		}
		return "authorizealphaGasPrice";
	}
	public void countUnitPriceExist() throws IOException{
		HttpSession session = getRequest().getSession();
		String instid = comInstId( session );
		String productid = getRequest().getParameter("productid");
		String result = "0" ;
		try{
			 trace("  Checking for product exist  ");
			 List checkproductexist  = alphagaspricedao.getAuthorizedUnitPriceList(instid,productid,jdbctemplate);
			 if(!checkproductexist.isEmpty()){
				 result = "1";
			 }else{
				 trace("Entered else part");
				 int checkproductHistoryexist  = alphagaspricedao.getproductHistoryexist(instid,productid,jdbctemplate);
				 trace(" checkproductHistoryexist  ---- > "+checkproductHistoryexist  );
				 if(checkproductHistoryexist >= 1){
					 int checkDeAuthorizationexist  = alphagaspricedao.checkDeAuthorization(instid,productid,jdbctemplate);
					 if(checkDeAuthorizationexist>=1){
						 result = "0";
					 }else{
						 result = "1"; 
					 }
				 }else{
					 result = "0"; 
				 }
			 }
		}catch(Exception e){
			trace("Exception : " + e.getMessage());
			e.printStackTrace();
			result = "0" ;
		} 
		getResponse().getWriter().write(result);
	}	
}
