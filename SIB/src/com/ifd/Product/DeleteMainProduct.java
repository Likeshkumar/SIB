package com.ifd.Product;

import java.util.List;
//import com.ifp.beans.editproductbean;
//import com.ifp.beans.productbean;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

public class DeleteMainProduct extends BaseAction {

private static String display ;  

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

	//private AddSubProductAction addsubProductInstance;
	public String getDisplay() {
	return display;
}
	
public void setDisplay(String display) {
	DeleteMainProduct.display = display;
}	

 
CommonDesc commondesc = new CommonDesc();

	public CommonDesc getCommondesc() {
	return commondesc;
}

public void setCommondesc(CommonDesc commondesc) {
	this.commondesc = commondesc;
}

	 

	 
	public String deleteproduct()
	{
		IfpTransObj trnasct = commondesc.myTranObject("DELETEPROD", txManager);
		String productid= (getRequest().getParameter("productid"));
		System.out.println("welcome to delete"); 
		String delete_query = "DELETE FROM IFPRODUCT_MASTER WHERE PRODUCT_CODE = '"+productid+"' ";
		System.out.println("Result is" +delete_query);
		jdbctemplate.update(delete_query);
		//System.out.println("Result is" +delete_query);
		display = "Deleted Successfully";
		jdbctemplate.update(delete_query);
		trnasct.txManager.commit( trnasct.status );
		return "success";
		
	}
		
	public String editproduct()
	{
		System.out.println("Welcime to edit");
		//editproductbean product = new editproductbean();
		List result;
		String productid= (getRequest().getParameter("productid")); 
		String qury="select * from IFPRODUCT_MASTER where PRODUCT_CODE = '"+productid+"'";
		System.out.println("Result is" +qury);
		result =jdbctemplate.queryForList(qury);
		setProductname(result);
	    return "editproduct";
	}
	
	private List productname;


	public List getProductname() {
		return productname;
	}

	public void setProductname(List productname) {
		this.productname = productname;
	} 
}
