package com.ifp.Action;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;
import com.ifg.Config.Licence.Licensemanager;


public class LnkBinAction extends BaseAction 
{
	
	private static final long serialVersionUID = 1L;
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	CommonDesc commondesc = new CommonDesc();
	
	
	
	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
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

	public String redirecting() {
		return "redirecting";
	}
	
	private List binList;
	public List getBinList() {
		return binList;
	}

	public void setBinList(List binList) {
		this.binList = binList;
	}

	public String input()
	{
		HttpSession session=getRequest().getSession();
		try
		{
			String institution_name=(String)session.getAttribute("Instname");
			 
			/*String query_getting_bin="select distinct(PRODUCT_ID),PRODUCT_NAME, BIN from x` where INST_ID='"+institution_name+"'";
			//System.out.println(" query for gettirngr bin detalis "+query_getting_bin );
			binList=jdbctemplate.queryForList(query_getting_bin);*/

			
			//by gowtham
			String query_getting_bin="select distinct(PRODUCT_ID),PRODUCT_NAME, BIN from x` where INST_ID=? ";
			//System.out.println(" query for gettirngr bin detalis "+query_getting_bin );
			binList=jdbctemplate.queryForList(query_getting_bin,new Object[]{institution_name});
			//session.setAttribute("link_bin_error", "error");
			System.out.println(" result "+binList );
			return "input";
		}
		catch (Exception e) 
		{
			System.out.println(" catch block exception " +e.getMessage() );
			session.setAttribute("link_bin_error", "error");
			session.removeAttribute("link_bin_message");
			session.setAttribute("link_bin_message", e.getMessage());
			return "input";
		}
	}
	
	public String save() throws Throwable
	{
		HttpSession session=getRequest().getSession();
		IfpTransObj trnasct = commondesc.myTranObject("SAVEDATA", txManager);
		try {
			String institution_name=(String)session.getAttribute("Instname"); 
			String parent_bin=getRequest().getParameter("parent_bin");
			String child_bin=getRequest().getParameter("child_bin");
			System.out.println(" parent_bin " +parent_bin );
			System.out.println(" child_bin " +child_bin );
			
			/*String query_prod_code="select PRODUCT_ID from PRODUCTINFO where bin='"+parent_bin+"' and INST_ID='"+institution_name+"'";
			String product=(String) jdbctemplate.queryForObject(query_prod_code, String.class);
			String query_parant_property="select count(*) from PRODUCTINFO where bin='"+child_bin+"' and INST_ID='"+institution_name+"'";
			System.out.println(" query_parant_property " +query_parant_property );
			int count= jdbctemplate.queryForInt(query_parant_property);*/
			
			//by gowtham
			String query_prod_code="select CUSTOMER_ID from PRODUCTINFO where PRD_CODE='"+parent_bin+"' and INST_ID='"+institution_name+"'";
			String product=(String) jdbctemplate.queryForObject(query_prod_code, String.class);
			String query_parant_property="select count(*) from PRODUCTINFO where PRD_CODE='"+child_bin+"' and INST_ID='"+institution_name+"'";
			System.out.println(" query_parant_property " +query_parant_property );
			int count= jdbctemplate.queryForInt(query_parant_property);
			
			System.out.println(" count " +count );
			System.out.println(" product " +product );
			
			/*String query="select count(*) from PRODUCTINFO where INST_ID='"+institution_name+"'";
			int bincount_binreltable =jdbctemplate.queryForInt(query);
			query="select BIN_COUNT from INSTITUTION where INST_ID='"+institution_name+"'";
			int bincount_ifinsttable =jdbctemplate.queryForInt(query);*/
			
			
			//by gowtham
			String query="select count(*) from PRODUCTINFO where INST_ID=? ";
			int bincount_binreltable =jdbctemplate.queryForInt(query,new Object[]{institution_name});
			query="select BIN_COUNT from INSTITUTION where INST_ID=? ";
			int bincount_ifinsttable =jdbctemplate.queryForInt(query,new Object[]{institution_name});
			  
			if (bincount_binreltable<bincount_ifinsttable)
			{	
				if(count>0)
				{
					session.setAttribute("link_bin_message", "Bin ' "+child_bin+ " ' Is Already Exists");
					return "redirecting";
				}
				Licensemanager license_manager=new Licensemanager();
				String rt=license_manager.chckBinLicence(institution_name,child_bin);
				if(rt=="sucess")
				{
					
					String query_insert_bin="INSERT INTO PRODUCTINFO (INST_ID,PRD_CODE,PRD_DESC,CVV1,CVV2,SIGNATURE,PIN_LENGTH,CHN_LENGTH,ENC_LENGTH,START_POSITION,PAN_OFFSET,SERVICE_CODE,SERV_PROVID,USER_NAME,BASE_NO_LEN,PAN_VER_LEN,CITY_ATTACHED,PVKI,PIN_DESC,CUSTOMER_ID,CHN_BASE_NO,INS_CHN_BASE_NO,SCM_CBL,CAFFORMATION,AUTH_STATUS,AUTH_USER,AUTH_DATE,REMARK,NCPI_PROD_CODE,CHN_BASED_ON,NCPI_ATTACHED,BR_CT_LENGTH,EXP_PERIOD,CHN_MASK_FROM,CHN_MASK_TO,EMV_SERVICECODE,BIN_STATUS)" +
					"select INST_ID,"+child_bin+",PRD_DESC,CVV1,CVV2,SIGNATURE,PIN_LENGTH,CHN_LENGTH,ENC_LENGTH,START_POSITION,PAN_OFFSET,SERVICE_CODE,SERV_PROVID,USER_NAME,BASE_NO_LEN,PAN_VER_LEN,CITY_ATTACHED,PVKI,PIN_DESC,CUSTOMER_ID,CHN_BASE_NO,INS_CHN_BASE_NO,SCM_CBL,CAFFORMATION,AUTH_STATUS,AUTH_USER,AUTH_DATE,REMARK,NCPI_PROD_CODE,CHN_BASED_ON,NCPI_ATTACHED,BR_CT_LENGTH,EXP_PERIOD,CHN_MASK_FROM,CHN_MASK_TO,EMV_SERVICECODE,BIN_STATUS)from PRODUCTINFO where PRD_CODE='"+parent_bin+"' and INST_ID='"+institution_name+"'";
					System.out.println(" query_insert_bin " +query_insert_bin );
					jdbctemplate.execute(query_insert_bin);	
					
					session.setAttribute("link_bin_message", "Bin ' "+child_bin+ " ' Is Linked Successfully To ' "+parent_bin+"'"); 
					trnasct.txManager.commit(trnasct.status);
					return "redirecting";
				}
				else
				{
					trnasct.txManager.rollback(trnasct.status);
					session.setAttribute("link_bin_message", " License For Bin ' "+child_bin+ " ' Is Not Valid");
					return "redirecting";
				}
				//session.setAttribute("link_bin_message", "Bin ' "+child_bin+ " ' Is Linked Successfully To ' "+parent_bin+"'");
				
			}
			else
			{
				session.setAttribute("link_bin_message", "You Have Attained Maximum No of Bin Allowed For This Institution");
				trnasct.txManager.rollback(trnasct.status);
				return "redirecting";
			}
			
		} catch (Exception e)  {
			trnasct.txManager.rollback(trnasct.status);
			System.out.println(" catch block exception " +e.getMessage() );
			session.setAttribute("link_bin_error", "error");
			session.removeAttribute("link_bin_message");
			session.setAttribute("link_bin_message", e.getMessage());
			return "input";
		}
	}
	
		

}
