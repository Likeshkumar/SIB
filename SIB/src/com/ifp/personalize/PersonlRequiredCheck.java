package com.ifp.personalize;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;
import com.ifp.util.DebugWriter;

public class PersonlRequiredCheck extends BaseAction 
{
	private static final long serialVersionUID = 1L; 
	
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
	DebugWriter debugr = new DebugWriter();
	public int cardorderRequiredcheck(String instid,HttpSession session,JdbcTemplate jdbcTemplate)
	{
		debugr.debugWriter("PersonlRequiredCheck", instid, "Checking Card Order Pre Configurartions");
		System.out.println("Check Required Data ");
		int checked_val = 0;
		
		/*String branch_check_qury = "SELECT COUNT(*) FROM BRANCH_MASTER WHERE INST_ID='"+instid+"'";
		System.out.println("Branch Check ===> "+branch_check_qury);
		String cardtype_check_qury = "SELECT COUNT(*) FROM IFD_RODUCT_MASTER WHERE INST_ID='"+instid+"'";
		System.out.println("Cardtype Check ===> "+cardtype_check_qury);
		String product_check_qury = "SELECT COUNT(*) FROM INSTPROD_DETAILS WHERE INST_ID='"+instid+"'";
		System.out.println("Product Check ===> "+product_check_qury);
		int branch_val = jdbcTemplate.queryForInt(branch_check_qury);*/
		
		
		//by gowtham-
		String branch_check_qury = "SELECT COUNT(*) FROM BRANCH_MASTER WHERE INST_ID=? ";
		System.out.println("Branch Check ===> "+branch_check_qury);
		String cardtype_check_qury = "SELECT COUNT(*) FROM IFD_RODUCT_MASTER WHERE INST_ID=? ";
		System.out.println("Cardtype Check ===> "+cardtype_check_qury);
		String product_check_qury = "SELECT COUNT(*) FROM INSTPROD_DETAILS WHERE INST_ID=? ";
		System.out.println("Product Check ===> "+product_check_qury);
		int branch_val = jdbcTemplate.queryForInt(branch_check_qury,new Object[]{instid});
		
		System.out.println("Banch Val count===> "+branch_val);
		if(branch_val == 0)
		{
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg"," Branch Not Confidgured ");
			debugr.debugWriter("PersonlRequiredCheck", instid, "Branch Not Confidgured ");
			checked_val = 1;
		}
		
		/*int cardtype_val = jdbcTemplate.queryForInt(cardtype_check_qury);*/
		int cardtype_val = jdbcTemplate.queryForInt(cardtype_check_qury,new Object[]{instid});
		
		System.out.println("CardType Val count===> "+cardtype_val);
		if(cardtype_val == 0)
		{
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg"," Card Type Not Configured ");
			debugr.debugWriter("PersonlRequiredCheck", instid, "Card Type Not Configured ");
			checked_val = 2;
		}
		
		/*int product_val = jdbcTemplate.queryForInt(product_check_qury);*/
		int product_val = jdbcTemplate.queryForInt(product_check_qury,new Object[]{instid});
		
		System.out.println("Product Val count===> "+product_val);
		if(product_val == 0)
		{
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg"," Product Not Configure ");
			debugr.debugWriter("PersonlRequiredCheck", instid, "Product Not Confidgure");
			checked_val = 3;
		}
		if(branch_val == 0 && cardtype_val == 0 && product_val == 0)
		{
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg"," System Not Configured Properly ");	
			debugr.debugWriter("PersonlRequiredCheck", instid, "System Not Configured Properly ");
			checked_val = 4;
		}
		System.out.println("Reurn Value is===> "+checked_val);
		debugr.debugWriter("PersonlRequiredCheck", instid, "Return vlaue is "+checked_val);
		return checked_val;
	}
	
	
	

}
