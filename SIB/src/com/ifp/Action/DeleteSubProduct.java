package com.ifp.Action;

import java.util.List;
//import com.ifp.beans.editsubproductbean;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

//import com.ifp.beans.editproductbean;

public class DeleteSubProduct extends BaseAction {
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	 
		public String deletesubproduct() { 
			String productid= (getRequest().getParameter("productid"));  
			
			/*String delete_query = "DELETE FROM IFPRODUCT_SUBTYPE WHERE SUB_PROD_ID = '"+productid+"' ";
			jdbctemplate.update(delete_query);*/
			
			///byg owtham
			String delete_query = "DELETE FROM IFPRODUCT_SUBTYPE WHERE SUB_PROD_ID =? ";
			jdbctemplate.update(delete_query,new Object[]{productid});
			
			System.out.println("Result is" +delete_query); 
			return "delsubproduct"; 
		}
		
		private List subproductname;
		
		public List getSubproductname() {
			return subproductname;
		}
		public void setSubproductname(List subproductname) {
			this.subproductname = subproductname;
		}
		
		
		public String editsubproduct()
		{
			//editsubproductbean product = new editsubproductbean();
			List result;
			String productid= (getRequest().getParameter("productid")); 
			
			/*String qury="select * from IFPRODUCT_SUBTYPE where SUB_PROD_ID = '"+productid+"'";
			System.out.println("Result is" +qury);
			result =jdbctemplate.queryForList(qury);*/
			
			///by gowtham
			String qury="select * from IFPRODUCT_SUBTYPE where SUB_PROD_ID = ? ";
			System.out.println("Result is" +qury);
			result =jdbctemplate.queryForList(qury,new Object[]{productid});
			
			setSubproductname(result);
		    return "editsubproduct";
		}
}
