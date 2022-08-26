package com.ifp.instant;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;
import com.ifp.util.CommonDesc;

public class RequiredCheck extends BaseAction  { 
 
	private static final long serialVersionUID = 1L;

	public CommonDesc comInsntance(){
		CommonDesc commondesc = new CommonDesc();
		return commondesc;
	} 
	public int requireMerchantRegister( String instid, HttpSession session, JdbcTemplate jdbctemplate  ) throws Exception {
		String merchacctqry= "SELECT COUNT(ACCT_TYPE) FROM EZMMS_ACCOUNTTYPE WHERE INST_ID='"+instid+"'";
		enctrace("merchacctqry : " + merchacctqry );
		int acctcnt = jdbctemplate.queryForInt(merchacctqry);
		if( acctcnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO ACCOUNT TYPE HAS BEEN CONFIGURED." );
			trace("NO ACCOUNT TYPE HAS BEEN CONFIGURED." );
			return -1;
		}	
		
		return 1;
		
	}
	public int requiredCheck( String instid, HttpSession session, JdbcTemplate jdbctemplate ){
		
		String branchexistqry = "SELECT COUNT(BRANCH_CODE) FROM BRANCH_MASTER WHERE INST_ID='"+instid+"'";
		enctrace( "branchexistqry : " + branchexistqry );
		int branchcnt = jdbctemplate.queryForInt(branchexistqry);
		if( branchcnt <= 0 ){   
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "No Branch configured. Configure the Branch and try again" ); 
			trace("No Branch configured. Configure the Branch and try again");
			return -1;
		}
		
		
		String binexistqry = "SELECT COUNT(PRD_CODE) FROM PRODUCTINFO WHERE INST_ID='"+instid+"'";
		enctrace("binexistqry  : " + binexistqry  );
		int bincnt = jdbctemplate.queryForInt(binexistqry);
		if( bincnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "No BIN configured. Configure the BIN and try again" );
			trace("No BIN configured. Configure the BIN and try again");
			return -1;
		}
		      
		
		String subprodexistqry = "SELECT COUNT(SUB_PROD_ID) FROM INSTPROD_DETAILS WHERE INST_ID='"+instid+"'";
		enctrace("subprodexistqry : "  + subprodexistqry );
		int subprodcnt = jdbctemplate.queryForInt(subprodexistqry);
		if( subprodcnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "No Product Configured. Configure the Product and try again" ); 
			trace("No Product Configured. Configure the Product and try again");
			return -1;
		}
		 
		return 1;
	}
	
	
	public int reqcheckMaintainMap( String instid, HttpSession session, JdbcTemplate jdbctemplate ){
		
		 
		
		String maintainlist = "SELECT COUNT(*) FROM MAINTAIN_DESC";
		enctrace("maintainlist.."+maintainlist);
		int maintlistcnt = jdbctemplate.queryForInt(maintainlist);
		if( maintlistcnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "No CARD MAINTAINENCE ACTIVITY CONFIGURED IN MAINTAIN_DESC" ); 
			trace("No CARD MAINTAINENCE ACTIVITY CONFIGURED IN MAINTAIN_DESC" );
			return -1;
		}
		
		String maintmapqry = "SELECT COUNT(*) FROM MAINTAIN_CONFIG WHERE INST_ID='"+instid+"'";
		enctrace("maintmapqry.."+maintmapqry);
		int maintcnt =  jdbctemplate.queryForInt(maintmapqry);
		if( maintcnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO Maintenence mapping configured in MAINTAIN_CONFIG. Configure and try again " ); 
			trace("NO Maintenence mapping configured in MAINTAIN_CONFIG. Configure and try again " );
			return -1;
		}
		return 1;
	}
	
	public int reqCheckStaticTable( HttpSession session, JdbcTemplate jdbctemplate  ){
		String maintainlist = "SELECT COUNT(*) FROM DEPLOYMENT";
		int maintlistcnt = jdbctemplate.queryForInt(maintainlist);
		if( maintlistcnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO DATA FOUND IN DEPLOYMENT. CONFIGURE THE DEPLOYMENT DETAILS" );
			trace("NO DATA FOUND IN DEPLOYMENT. CONFIGURE THE DEPLOYMENT DETAILS" ); 
			return -1;
		}
		
		String curlistcntqry = "SELECT COUNT(*) FROM GLOBAL_CURRENCY";
		int curlistcnt = jdbctemplate.queryForInt(curlistcntqry);
		if( curlistcnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO CURRENCY DETAILS FOUND. CONFIGURE THE CURRENCY AND TRY AGAIN" ); 
			trace("NO DATA FOUND IN GLOBAL_CURRENCY. CONFIGURE THE CURRENCY DETAILS" );
			return -1;
		} 
		
		return 1;
	}
	
	public int reqBinDetail( String instid, HttpSession session, JdbcTemplate jdbctemplate ){ 
		return 1;
	}
	
	public int reqCardTypeConfig( String instid,  HttpSession session, JdbcTemplate jdbctemplate){
		String globcardtypeqry = "SELECT COUNT(*) FROM CARD_TYPE ";
		int globcardcnt = jdbctemplate.queryForInt(globcardtypeqry);
		if( globcardcnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO CARD TYPE CONFIGURED. CONTACT ADMINSTRATOR" );
			trace("NO CARD TYPE CONFIGURED. CONTACT ADMINSTRATOR" ); 
			return -1;
		}
		return 1;
	}

	public int reqGLConfigured( String instid, HttpSession session, JdbcTemplate jdbctemplate ){
		String glcntqry = "SELECT COUNT(GL_CODE) FROM IFP_GL_MASTER WHERE INST_ID='"+instid+"' AND GL_ENABLE_STATUS='1' AND MKCK_STATUS='P' AND GL_COM_ID = 'BIN' ";
		enctrace( "glcntqry  :  " + glcntqry );
		int glcnt = jdbctemplate.queryForInt(glcntqry);
		if( glcnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO GL CONFIGURED. CONFIGURE THE GL AND TRY AGAIN" ); 
			return -1;
		}
		return 1;
	}  
	
	public int reqSubProduct( String instid, HttpSession session, JdbcTemplate jdbctemplate ){
		
		/*String subtypeqry = "SELECT COUNT(PRODUCT_CODE) FROM PRODUCT_MASTER  WHERE INST_ID='"+instid+"' AND ACTIVE_FLAG='1' ";	  
		enctrace("subtypeqry  :  " + subtypeqry);  
		int prodcnt = jdbctemplate.queryForInt(subtypeqry);*/
		
		//by gowtham-210819
				String subtypeqry = "SELECT COUNT(PRODUCT_CODE) FROM PRODUCT_MASTER  WHERE INST_ID=? AND ACTIVE_FLAG=? ";	  
				enctrace("subtypeqry  :  " + subtypeqry);  
				int prodcnt = jdbctemplate.queryForInt(subtypeqry,new Object[]{instid,"1",});
		
		if( prodcnt<= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO PRODUCT CONFIGURED. CONFIGURE THE PRODUCT AND TRY AGAIN" ); 
			trace("NO PRODUCT CONFIGURED. CONFIGURE THE PRODUCT AND TRY AGAIN" );
			return -1;
		}
		 
		String feeschemeqry = "SELECT COUNT(FEE_CODE) FROM FEE_DESC  WHERE INST_ID='"+instid+"' ";	  
		enctrace("feeschemeqry::"+feeschemeqry);
		int feeschemecnt = jdbctemplate.queryForInt(feeschemeqry);
		if( feeschemecnt<= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO FEE DETAILS CONFIGURED. CONFIGURE THE FEE AND TRY AGAIN" ); 
			trace("NO FEE DETAILS CONFIGURED. CONFIGURE THE FEE AND TRY AGAIN" );
			return -1;
		} 
		
		String limitqry = "SELECT COUNT(LIMIT_ID) FROM LIMIT_DESC  WHERE INST_ID='"+instid+"' AND AUTH_STATUS='1' ";	
		enctrace("limitqry:::"+limitqry);
		int limitcnt = jdbctemplate.queryForInt(limitqry);    
		if( limitcnt<= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO LIMIT DETAILS CONFIGURED. CONFIGURE THE LIMIT AND TRY AGAIN" ); 
			trace("NO LIMIT DETAILS CONFIGURED. CONFIGURE THE LIMIT AND TRY AGAIN" );
			return -1;
		} 
		/*
		String servicecodeqry = "SELECT COUNT(SERVICE_CODE) FROM IFD_SERVICE_CODE  WHERE INST_ID='"+instid+"' AND STATUS='1' ";	  
		enctrace("servicecodeqry::"+servicecodeqry);
		int servicecode = jdbctemplate.queryForInt(servicecodeqry);
		enctrace("servicecodeqry  :  " + servicecodeqry);
		if( servicecode<= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO SERVICE CODE FOR THIS INSTITUTION." ); 
			trace("NO SERVICE CODE FOR THIS INSTITUTION." ); 
			return -1;
		}
		*/    
		return 1;
	}   
	
	public int reqProduct( String instid, HttpSession session, JdbcTemplate jdbctemplate ){
		
		String maintainlist = "SELECT COUNT(*) FROM DEPLOYMENT";
		int maintlistcnt = jdbctemplate.queryForInt(maintainlist);
		if( maintlistcnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO DATA FOUND IN DEPLOYMENT. CONFIGURE THE DEPLOYMENT DETAILS" ); 
			return -1;
		}
		    
		String globcardtypeqry = "SELECT COUNT(*) FROM CARD_TYPE ";
		int globcardcnt = jdbctemplate.queryForInt(globcardtypeqry);
		if( globcardcnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO CARD TYPE CONFIGURED. CONTACT ADMINSTRATOR" ); 
			return -1;
		}
		
		String binexistqry = "SELECT COUNT(PRD_CODE) FROM PRODUCTINFO WHERE INST_ID='"+instid+"'";
		int bincnt = jdbctemplate.queryForInt(binexistqry);
		if( bincnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO BIN CONFIGURED. CONFIGURE THE BIN AND TRY AGAIN" ); 
			return -1;
		}
		
		/*String glcntqry = "SELECT COUNT(GL_CODE) FROM IFP_GL_MASTER WHERE INST_ID='"+instid+"' AND GL_ENABLE_STATUS='1' AND MKCK_STATUS='P' AND GL_COM_ID = 'BIN' ";
		enctrace( "glcntqry  :  " + glcntqry );
		int glcnt = jdbctemplate.queryForInt(glcntqry);
		if( glcnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO GL CONFIGURED. CONFIGURE THE GL AND TRY AGAIN" ); 
			return -1;
		}*/
		
		String binmapqry = "SELECT COUNT(*) FROM BIN_CARD_MAPPING WHERE INST_ID='"+instid+"'";
		int binmapcnt = jdbctemplate.queryForInt(binmapqry);
		if( binmapcnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO DATA FOUND IN BIN_CARD_MAPPING. CONFIGURE THE MAPPING AND TRY AGAIN" ); 
			return -1;
		}
		 
		return 1; 
	}
	  
	public int reqCheckHsmMap(String instid,  HttpSession session, JdbcTemplate jdbctemplate ){
		String instlist = "SELECT COUNT(*) FROM INSTITUTION";
		int instcnt = jdbctemplate.queryForInt(instlist);
		if( instcnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO INSTITUTION CONFIGURED. CONFIGURE THE INSTITUTION AND TRY AGAIN" ); 
			return -1;
		}
		
		String hsmlist = "SELECT COUNT(*) FROM HSM_DETAILS  WHERE HSMSTATUS='1' AND AUTH_CODE='1'";
		int hsmcnt = jdbctemplate.queryForInt(hsmlist);
		if( hsmcnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO HSM CONFIGURED. CONFIGURE THE HSM AND TRY AGAIN" ); 
			return -1;
		}  
		
		return 1;
	}
	public int requiredCheckMerchantAcctRule(String instid,	HttpSession session, JdbcTemplate jdbctemplate) {
		 
		String msglist = "SELECT COUNT(*) FROM IFP_ACCT_MSGTYPE  ";
		int msgcnt = jdbctemplate.queryForInt(msglist);
		if( msgcnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO MESSAGE TYPE CONFIGURED. CONTACT ADMINSTRATOR" ); 
			return -1;
		}
		
		String txnlist = "SELECT COUNT(*)  FROM IFACTIONCODES WHERE INST_ID ='"+instid+"' AND TXN_FLAG=1" ;
		int txncnt = jdbctemplate.queryForInt(txnlist);
		if( txncnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO TRANSACTION CODE CONFIGURED FOR THIS INSTITUTION." ); 
			return -1;
		}
		
	 
		String resplist = "SELECT COUNT(*) FROM IFP_ACCT_RESPCODE";
		int respcnt = jdbctemplate.queryForInt(resplist);
		if( respcnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO RESPONSE CODE CONFIGURED. CONTACT ADMINSTRATOR" ); 
			return -1;
		}
		
		String merchtypelist ="SELECT COUNT(*) FROM EZMMS_MERCHANTTYPE WHERE INST_ID='"+instid+"'";
		int merchtypecnt = jdbctemplate.queryForInt( merchtypelist );
		if( merchtypecnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO MERCHANT TYPE CONFIGURED" ); 
			return -1;
		}
		
		String orginchannelqry ="SELECT COUNT(*) FROM IFP_ACCT_ORGINCHANNEL ORDER BY ORGIN_CHANNEL";
		int orginchannelcnt = jdbctemplate.queryForInt( orginchannelqry );
		if( orginchannelcnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO ORGIN CHANNEL CONFIUGRED. CONTACT ADMINSTRATOR" ); 
			return -1;
		}
		
		String commissionTypeqry ="SELECT COUNT(*) FROM EZMMS_COMMISSIONINFO WHERE INST_ID='"+instid+"'";
		int commissioncnt = jdbctemplate.queryForInt( commissionTypeqry );
		if( commissioncnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO COMMISSTION  CONFIUGRED." ); 
			return -1;
		} 
		
		
		String feeTypeqry ="SELECT COUNT(*) FROM EZMMS_FEEINFO  WHERE INST_ID='"+instid+"'";
		int feeTypecnt = jdbctemplate.queryForInt( feeTypeqry );
		if( feeTypecnt <= 0 ){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO FEE LIST  CONFIUGRED." ); 
			return -1;
		}
		return 1;
	}
}
