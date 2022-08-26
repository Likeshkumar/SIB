package com.ifd.SubProduct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
	   
public class AddSubProductAction extends BaseAction   {  
	
 private static String display = "" ;
 private HttpServletRequest servletRequest;
	
   //private AddSubProductAction addsubProductInstance;
	public String getDisplay() {
	return display;
}
public void setDisplay(String display) {
	this.display = display;
}





	class Myproduct {  
	    private String deployid;
	    private String deployname;
	    
	    public String getProductid() {
			return deployid;
		}
	    
	    public void setProductid(String productid) {
			this.deployid = deployid;
		}
	    
	    public String getProductname() {
			return deployname;
		}

		public void setProductname(String productname) {
			this.deployname = deployname;
		}
	
	    public Myproduct(String deployid, String deployname) {  
	        super();  
	        this.deployid = deployid;  
	        this.deployname = deployname;  
	    }  
	   
	}  

	 

		private List<String> prods;
		public List<String> getProds() {
			return prods;
			}
		public void setProds(List<String> prods) {
			this.prods = prods;
			}

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

		List<Myproduct> productlist = new ArrayList<Myproduct>();
		
		public List<Myproduct> getProductlist() {
			return productlist;
			}
		public void setProductlist(List<Myproduct> productlist) {
			this.productlist = productlist;
			}
		
	
	
		public String display() 
		{
			
			List result;String deployname;String deployid;
			
			String qury="select CARD_TYPE_ID,CARD_TYPE_NAME from PRODUCT_MASTER ";
			result =jdbctemplate.queryForList(qury);
			Iterator itr = result.iterator();
			while(itr.hasNext())
			{  
			Map map = (Map)itr.next();
			deployid=((String)map.get("PRODUCT_ID"));
			deployname=((String)map.get("PRODUCT_TYPE"));
			productlist.add(new Myproduct(deployname,deployid));
			String realpath = servletRequest.getContextPath();
			System.out.println("Url Path ========== > ====== "+realpath);
			
			}	
			return "sucess";
	
		}
	 
		
		


public String addproduct()
{
	
	
	
	String deployment= (getRequest().getParameter("deployment"));
	//System.out.println("Result is" +deployment);
	String product = (getRequest().getParameter("product"));
	//System.out.println("Result is" +product); 
 
	String count_query = "SELECT count(*) from IFPRODUCT_SUBTYPE";
	int count = jdbctemplate.queryForInt(count_query);
	//List resu=jdbctemplate.queryForList(count_query);
	//System.out.println("Result is" +count);
	if (count > 0)
	{
		
		String query= " select MAX(SUB_PROD_ID)+1 as PRODUCT_CODE  from IFPRODUCT_SUBTYPE";
		int productid = jdbctemplate.queryForInt(query);
		
		String subproductquery= " select SUB_PROD_CNT from PRODUCT_MASTER where PRODUCT_ID='"+deployment+"' ";
		int subproductid = jdbctemplate.queryForInt(subproductquery);
		//System.out.println("subproductid is" +subproductid);
		
		String countprodqry = "select count(SUB_PROD_DESC) from IFPRODUCT_SUBTYPE where PRODUCT_ID='"+deployment+"' ";
		int subproductqry = jdbctemplate.queryForInt(countprodqry);
		//System.out.println("subproductqry is" +subproductqry);
		
		if( subproductqry  < subproductid )
		{
		String cardtype = deployment.concat(Integer.toString(productid));
		System.out.println("cardtype is" +cardtype);
		String productquery="INSERT INTO IFPRODUCT_SUBTYPE(CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,SUB_PROD_DESC)VALUES ('"+deployment+"','"+productid+"','"+cardtype+"','"+product+"')";
		//System.out.println("+++++ Data inserted in db +++++\n" +productquery);
		int result = jdbctemplate.update(productquery);
		
		System.out.println("+++++ Data inserted in db +++++\n");
		//addsubProductInstance.display();
		display = "Data Inserted Successfully";
		return "check" ;
		
		}
		else
		{
			System.out.println("Please check your Sub Product Count" );
			display = "Please check your Sub Product Count";
			return "check" ;
			
		}
	}
	else
	{
		 int product_id = 10;
		 String cardtype = deployment.concat(Integer.toString(product_id));
		 System.out.println("Result is ZERO" +product_id);
		 String productquery="INSERT INTO IFPRODUCT_SUBTYPE(CARD_TYPE_ID,SUB_PROD_ID,PRODUCT_CODE,SUB_PROD_DESC)VALUES ('"+deployment+"','"+product_id+"','"+cardtype+"','"+product+"')";
		 int result = jdbctemplate.update(productquery);
			
		 System.out.println("+++++ Data inserted in db +++++\n");
		 display = "Please check your Sub Product Count";
		 
		 //int product_id = product_id + 1;
	}
	
	display = "Data Inserted Successfully";
	return "check";
}


	private List subProductname;
	
	
	public List getSubProductname() {
		return subProductname;
	}
	public void setSubProductname(List subProductname) {
		this.subProductname = subProductname;
	}
	public String viewsubProduct()
	{
		// Subproductbean product = new Subproductbean();
		List result;String productname;String productid; 
		String qury="select sub.CARD_TYPE_ID,sub.SUB_PROD_ID,sub.SUB_PROD_DESC,mainprod.CARD_TYPE_ID,mainprod.CARD_TYPE_NAME from PRODUCT_MASTER  mainprod,IFPRODUCT_SUBTYPE  sub where sub.CARD_TYPE_ID = mainprod.CARD_TYPE_ID ";
		result =jdbctemplate.queryForList(qury);
		System.out.println("Result" +result);
		setSubProductname(result);
		
		
		
	/*	while(itr.hasNext())
		{
		Map map = (Map)itr.next();
		productname=((String)map.get("PRODUCT_TYPE"));
		productid=((String)map.get("PRODUCT_ID"));
		System.out.println("Result" +productname); 
		mainproductlist.add(new Mainproduct(productname,productid));
	
		}	*/
		return "viewsubproduct";
	}

}
