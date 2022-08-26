package com.ifp.instant;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList; 
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
 
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.GenericPackager;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.ifp.Action.BaseAction;
import com.ifp.beans.InstCardActivateProcessBeans;
import com.ifp.dao.InstCardActivateProcessDAO;
import com.ifp.util.CommonDesc;
import com.ifp.util.CommonUtil;
import com.ifp.util.ISOCommunication;
import com.ifp.util.IfpTransObj;
 
 
 

public class InstCardActivateProcess extends BaseAction 
{ 
	
	 
	private static final long serialVersionUID = -8376161637970676446L;
	 
	public final String CARDACTIVECODE = "CARDACT";
	public final String CURCONV = "CURCONV";
	public final String FEE_ENTITY = "FEEAMT";
	public final String LOAD_ENTITY = "LOADAMT";
	public final String acctinfotag = "10F2";
	public final String curtag = "20CF";
	public final String acctnotag = "10F2";
	public final String amounttag = "M0F2";
	public final int amountlength = 12;
	String ACTIVESTTUSCODE = "09";
	public final String ACTIVEFLAG = "05";
	
	private static Boolean  initmail = true; 
	private static  String parentid = "000";
 
	CommonUtil comutil= new CommonUtil(); 
	public CommonUtil getComutil() {
		return comutil;
	}
	public void setComutil(CommonUtil comutil) {
		this.comutil = comutil;
	}
	
	
	InstCardActivateProcessDAO activatedao = new InstCardActivateProcessDAO();
	InstCardActivateProcessBeans activatebeans = new InstCardActivateProcessBeans(); 
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
	public InstCardActivateProcessDAO getActivatedao() {
		return activatedao;
	}

	public void setActivatedao(InstCardActivateProcessDAO activatedao) {
		this.activatedao = activatedao;
	}

	public InstCardActivateProcessBeans getActivatebeans() {
		return activatebeans;
	}

	public void setActivatebeans(InstCardActivateProcessBeans activatebeans) {
		this.activatebeans = activatebeans;
	}

	 
	String act;  
	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	
	CommonDesc commondesc = new CommonDesc();
	
	
	public CommonDesc getCommondesc() {
		return commondesc;
	}
	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}
	public CommonUtil comUtil(){
		CommonUtil Comutil = new CommonUtil();
		return Comutil;
	}  
	
	public String comInstId(){
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	} 
	public String comUserCode(){
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("USERID"); 
		return instid;
	}  
	
	public String activateInstCardHome() { 
		HttpSession session = getRequest().getSession(); 
		 
		String type = getRequest().getParameter("type");
		try{
			System.out.println( "intant card activate ");
			String instid =  comInstId();  
			int reqch= commondesc.reqCheck().requiredCheck(instid, session, jdbctemplate);
			if( reqch < 0 ){
				return "required_home";
			}
			
			if(type != null)
			{
				session.setAttribute("PROCESS_TYPE", type);
			}
			
			System.out.println( "PROCESS_TYPE__" + session.getAttribute("PROCESS_TYPE"));
			
			String act = getRequest().getParameter("act");
			if( act != null ){
				session.setAttribute("act", act);
			} 
			
			String table_type = "";
			if( session.getAttribute("PROCESS_TYPE").equals("INSTANT")){
				table_type = "INST_CARD_PROCESS ";
			}else{
				table_type = "PERS_CARD_PROCESS ";
			}
			 
			
			//String act = getRequest().getParameter("act");
			System.out.println("#########################################################");
			System.out.println("Act *******====> "+act);
			System.out.println("#########################################################");	
			
			//String ordertype = getRequest().getParameter("type");
			System.out.println("#########################################################");
			System.out.println("Order Type *******====> "+activatebeans.type);
			System.out.println("#########################################################");	
			
			 
			
			activatebeans.branchlist =  this.commondesc.generateBranchList(instid, jdbctemplate);
			activatebeans.setBranchlist(activatebeans.branchlist);
			
			activatebeans.prodlist = commondesc.getProductList( instid, jdbctemplate, session ); 
			System.out.println( "john__" + activatebeans.prodlist );
			activatebeans.setProdlist(activatebeans.prodlist); 
			
			/*String orderlistqry = "SELECT DISTINCT ORDER_REF_NO FROM " +table_type+ " WHERE"
			+ " INST_ID='"+instid+"' AND CARD_STATUS='"+ACTIVEFLAG+"' ";
			System.out.println("\norderlistqry : " + orderlistqry);
			activatebeans.setOrdernolist( jdbctemplate.queryForList(orderlistqry));*/
			
			///BY GOWTHAM
			String orderlistqry = "SELECT DISTINCT ORDER_REF_NO FROM " +table_type+ " WHERE"
					+ " INST_ID=? AND CARD_STATUS=? ";
					System.out.println("\norderlistqry : " + orderlistqry);
					activatebeans.setOrdernolist( jdbctemplate.queryForList(orderlistqry,new Object[]{instid,ACTIVEFLAG}));
			
			
			
			/*** MAIL BLOCK ****/
			System.out.println( "initmail--" + initmail +" parentid :  " + this.parentid );  
			if( initmail ){
				HttpServletRequest req = getRequest();
				String menuid = comutil.getUrlMenuId( req, jdbctemplate ); 
				if( !menuid.equals("NOREC")){
					System.out.println( "parentid--"+menuid); 
					this.parentid = menuid;
				}else{ 
					this.parentid = "000";
				}
				initmail = false;
			} 
			/*** MAIL BLOCK ****/

			
			
		}catch(Exception e){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", e.getMessage());
			return "required_home";
		}
		return "insactivatecard_home"; 
	}
	
	public String activateInstCardList() {
		System.out.println( "Activation list....");
		
		HttpSession session = getRequest().getSession();
		 
		String instid =  comInstId();  
		String mkckstatus = "P";
		 
		String  datefld = "REG_DATE";
		 
		
		String branch = getRequest().getParameter("branch");
		String cardtype = getRequest().getParameter("cardtype");
		String[] bin = cardtype.split("~");
		String fromdate = getRequest().getParameter("fromdate");
		String todate = getRequest().getParameter("todate");
		
		String filtercond = commondesc.filterCondition(bin[0],branch,fromdate,todate,datefld);
		System.out.println( "filter condition " + filtercond );
		
		List waitingforcardpin;
		try {
			waitingforcardpin = this.commondesc.waitingForInstCardProcessCards(instid, ACTIVEFLAG,  mkckstatus, filtercond, jdbctemplate);
		
			System.out.println( "waitingforcardpin " + waitingforcardpin);
			if( waitingforcardpin.isEmpty() ){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " No records found " ); 
				return this.activateInstCardHome();
			}
			
			activatebeans.setInstorderlist(waitingforcardpin); 
			
			if( waitingforcardpin.isEmpty() ){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", " No records found " ); 
				return this.activateInstCardHome();
			}
		} catch (Exception e) {
			System.out.println( "Error whie view activate list " + e);
			e.printStackTrace();
		} 
		return "insactivatecard_list";   
	}
	 
	public String getCurrencyDesc( String curcode, JdbcTemplate jdbctemplate ){
		
		/*String sec_curdesc = "SELECT CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE"
		+ " CURRENCY_CODE='"+curcode+"' and rownum<=1";
		System.out.println("sec_cur_req___" + sec_curdesc);
		String sec_cur_det =(String)jdbctemplate.queryForObject(sec_curdesc,String.class);*/
		
		//by gowtham
		String sec_curdesc = "SELECT CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE"
				+ " CURRENCY_CODE=? and rownum<=?";
				System.out.println("sec_cur_req___" + sec_curdesc);
				String sec_cur_det =(String)jdbctemplate.queryForObject(sec_curdesc,new Object[]{curcode,"1"},String.class);
		
		return sec_cur_det;
	}
	
	public void getCurrencydetails() throws IOException {
		 
		String cardno = getRequest().getParameter("cardno");
		String curtable = "";
		String instid = getRequest().getParameter("instid");
		try{			
			String productcode = activatedao.getProductCode(instid,cardno,jdbctemplate);  
			if ( !productcode.equals("NOREC")){ 
				String bin = commondesc.getBin(instid, productcode, jdbctemplate); 
				
				/*String sec_cur_req = "SELECT SEC_CUR FROM PRODUCTINFO WHERE INST_ID='"+instid+"'"
				+ " AND BIN='"+bin+"' ";
				System.out.println("sec_cur_req___" + sec_cur_req);
				 String sec_cur_det =(String)jdbctemplate.queryForObject(sec_cur_req,String.class);*/
				
				///by gowtham
				String sec_cur_req = "SELECT SEC_CUR FROM PRODUCTINFO WHERE INST_ID=? "
						+ " AND PRD_CODE=? ";
						System.out.println("sec_cur_req___" + sec_cur_req);
						 String sec_cur_det =(String)jdbctemplate.queryForObject(sec_cur_req,new Object[]{instid,bin},String.class);
				 
				 
				System.out.println( "sec_cur_det___" + sec_cur_det );
				if( sec_cur_det.equals("1") ){
					
					/*String curdetqry = "SELECT CUR_CODE FROM IFP_BIN_CURRENCY WHERE INST_ID='"+instid+"' AND"
					+ " BIN='"+bin+"'"; 
					System.out.println("curdetqry__" + curdetqry);
					List curlist =(List)jdbctemplate.queryForList(curdetqry);*/
					
					///by gowtham
					String curdetqry = "SELECT CUR_CODE FROM IFP_BIN_CURRENCY WHERE INST_ID=? AND"
							+ " BIN=? "; 
							System.out.println("curdetqry__" + curdetqry);
							List curlist =(List)jdbctemplate.queryForList(curdetqry,new Object[]{instid,bin});
					
					if( ! curlist.isEmpty() ){
						Iterator itr = curlist.iterator(); 
						while( itr.hasNext() ){
							Map temp = (Map) itr.next();
							String curcode = (String)temp.get("CUR_CODE");
							String curdesc = this.getCurrencyDesc(curcode, jdbctemplate);
							curtable += curcode+"-"+curdesc+"~";	 
						} 
					}
				}   
			}else{
				curtable = "INVALID CARD NO ";
			}
		}catch(Exception e ){
			curtable = e.getMessage();
		}
		getResponse().getWriter().write(curtable);
	}
	
	public void getSubProdDetailsHome() throws IOException{
		System.out.println( "getSubProdDetailsHome FUNCTION CALLING...."); 
		
		String instid = comInstId();
		System.out.println("instid__" + instid);
		HttpSession session = getRequest().getSession();
		
		String activetype = getRequest().getParameter("activetype");
		String entityno = getRequest().getParameter("entityno");
		String ent_cardno = null;
		String orederrefno = null;
		String processtype = "INSTANT";
		String result = "";
		String subprodid = null; 
		String table  = "";
		if( session.getAttribute("PROCESS_TYPE").equals("INSTANT") ){ 
			table = "INST_CARD_PROCESS"; 
		}else{
			table = "PERS_CARD_PROCESS";
		}
		String productcode ="";
		String bin= "";
		
		try {
			System.out.println( "activetype==>" +activetype);
			if( activetype.equals("ORDERBASED")){
				orederrefno = entityno;
				subprodid = commondesc.getSubProductByOrder(instid, orederrefno, jdbctemplate, processtype);
				productcode = commondesc.getProductCode(instid, orederrefno, jdbctemplate);
				bin = commondesc.getBin(instid, productcode, jdbctemplate);
			}else{
				ent_cardno = entityno;
				int cardstatus = activatedao.checkValidCard(instid,ent_cardno,table,ACTIVEFLAG,processtype,jdbctemplate,session);
				//System.out.println("ent_cardno__"+ent_cardno+"__and card status "+cardstatus); 
				
				if( cardstatus < 0 ){
					result = "Could not activate the card. Invalid Card No.";
					getResponse().getWriter().write(result);
					return ;
				}else{
					subprodid = commondesc.getSubProductByCHN(instid, ent_cardno, jdbctemplate, processtype);
					productcode = commondesc.getProductByCHN(instid, ent_cardno, jdbctemplate, processtype);
					bin = commondesc.getBin(instid, productcode, jdbctemplate);
				} 
			}
			
			if( subprodid != null ){
				result = getSubProdDetails(instid, subprodid, productcode, bin, processtype, jdbctemplate); 
				getResponse().getWriter().write(result);
				return ;
			}
			
			System.out.println("subprodid__" + subprodid );
			
			
		} catch (Exception e) { 
			result = e.toString();
			e.printStackTrace();
		}
		getResponse().getWriter().write(result);
	}
	
	
	public String getSubProdDetails(String instid, String subprodid, String productcode, String bin, String processtype, JdbcTemplate jdbctemplate) throws Exception{
		System.out.println( "getSubProdDetails FUNCTION CALLED");
	 
		String activetable = "";
		try {
			 
			String table = "";
			 
			String processcode = ""; 
			 
			String productdesc  = "";
			String subproddesc = "";
			String def_minamount = "0",def_maxamount="0";
			String defcur = "";
			String defcursymb = "" ;
			Boolean seccurrency = false;
			String sec_cur_req = "";
			String rupeesymbol = " <span style='font-family:rupee;font-size:20px'>R</span>";
			 
			 
			String registeration_req = commondesc.checkRegisterRequired(instid, subprodid, jdbctemplate);
			System.out.println("registeration_req___"+registeration_req);
			 
			
			
			
			if( processtype.equals("INSTANT")){
				table = "INST_CARD_PROCESS";
				processcode = "05";
				if( registeration_req.equals("N")){
					processcode = "04";
				}
				
			}else{
				table = "PERS_CARD_PROCESS";
				processcode = "06";
			}
			
			activetable = "";  
			List cardreclist; 
				 
			
			productdesc = commondesc.getProductdesc(instid, productcode, jdbctemplate);
			System.out.println( "bindesc__" + productdesc );
			 
			subproddesc = commondesc.getSubProductdesc(instid, subprodid, jdbctemplate);
			
			String subprodamt_qry = "SELECT MIN_LOAD_AMOUNT,MAX_LOAD_AMOUNT,CARD_CCY FROM IFP_INSTPROD_DETAILS WHERE INST_ID='"+instid+"' AND SUB_PROD_ID='"+subprodid+"'";
			 
			System.out.println("subprodamt_qry__" + subprodamt_qry);
			List subprodamt_list = jdbctemplate.queryForList(subprodamt_qry);
			if( !subprodamt_list.isEmpty() ){
				Iterator itrsub = subprodamt_list.iterator();
				while( itrsub.hasNext() ){
					Map submp = (Map)itrsub.next();
					
					def_minamount = (String)submp.get("MIN_LOAD_AMOUNT");
					def_maxamount = (String)submp.get("MAX_LOAD_AMOUNT");
					defcur  = (String)submp.get("CARD_CCY");
					String defcurdesc = commondesc.getCurDesc(defcur, jdbctemplate);  
					
					if( defcur.equals("356")){
						defcursymb = rupeesymbol;
					}else{
						if( ! defcursymb.equals("NOREC")){
							defcursymb = commondesc.getCurSymbol(defcur,jdbctemplate);
						}
					}
					
				}
			}
			 
			
			
			String sec_cur_req_qry = "SELECT SEC_CUR FROM PRODUCTINFO WHERE INST_ID='"+instid+"' AND PRD_CODE='"+bin+"'";
			System.out.println( "sec_cur_req_qry_" + sec_cur_req_qry);
			
			
			try {
				sec_cur_req = (String) jdbctemplate.queryForObject(sec_cur_req_qry, String.class);
				 
			} catch (Exception e) { 
				sec_cur_req ="0";
				e.printStackTrace();
			}
			 
				
		 
		 
		
		activetable = "<table border='0' cellpadding='0' cellspacing='0' width='100%' class='headertable'> <!--  header table  -->" +
				"				<tr>		" +
				"				<td>" +
				"				<table border='0' cellpadding='0' cellspacing='0' width='100%' class='innertable'>" +
				"				<tr>" +
				"					<td>PRODUCT </td><td> : "+productdesc+ "</td>" +
				"					<td>SUB PRODUCT </td><td> : "+subproddesc+" </td>" +
				"				</tr>" +
				"				" +
				"				<tr>" +
				"					<td>AMOUNT TO BE LOAD  </td><td> : "+defcursymb+ "  <input type='text' name='defloadamt' id='defloadamt' value='"+def_minamount+"' style='text-align:right;width:150px' onchange=\"return makeSummery('"+subprodid+"')\"    onKeyPress='return numerals(event);' maxlength='6'  >  </td>" ;
		
				if( sec_cur_req.equals("1")){
					activetable +="	<td>SECONDARY CURRENCY REQUIRED </td><td> : <input type='checkbox' name='sec_curreq' id='sec_curreq' onclick='enableSecondary(this.checked)' onchange=\"return makeSummery('"+subprodid+"')\" /> </td>" ;
					seccurrency = true;
				}else{
					activetable +=" <td> &nbsp; </td>";
				}
				
				activetable +=	"</tr></table></tr>" ;
				 
		
				if ( seccurrency ){
					
					System.out.println( "seccurrency __ " + seccurrency);
					activetable += "<tr> <td colspan='4'> <table border='1' cellpadding='0' cellspacing='0' width='100%' class='curtable'><tr>" ; 
							
					String curlistqry = "SELECT CUR_CODE FROM IFP_BIN_CURRENCY WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
					System.out.println( "curlistqry__" + curlistqry );
					List curlist = jdbctemplate.queryForList(curlistqry);
					int tdno = 3;
					String tmptab = "";
					if( ! curlist.isEmpty() ){
						int curcnt = 0;
						Iterator curitr = curlist.iterator();
						while( curitr.hasNext() ){
							
							Map curmp = (Map) curitr.next();
							String curcode = (String) curmp.get("CUR_CODE");
							String curdesc = commondesc.getCurDesc(curcode, jdbctemplate);
							String cursymb = "" ;
							if( ! cursymb.equals("NOREC")){
								cursymb = commondesc.getCurSymbol(curcode,jdbctemplate);
							}
							
							String curid = "sec_cur"+curcnt;
							String loadid = "loadamt"+curcnt;
							System.out.println( "laod amt__" + loadid );
							String inputloadamt = "<td><input type='checkbox' onchange=\"return makeSummery('"+subprodid+"')\" disabled name='sec_cur' value='"+curcode+"' id='"+curid+"' onclick='enableSecondaryAmount(this.checked, this.id)' />  "+curdesc+" </td><td> :   "+cursymb+" <input type='text' name='loadamt' id='"+loadid+"' style='text-align:right;width:150px'  disabled  onKeyPress='return numerals(event);' maxlength='6' onchange=\"return makeSummery('"+subprodid+"')\" >   </td>" ;
							
							if( curcnt < tdno ){
								activetable += inputloadamt;
								curcnt++;
							}else{
								 
								activetable += "</tr><tr>";
								activetable += inputloadamt;
								curcnt=0;
							}
							
						} 
						
						System.out.println( "curcnt__" + curcnt );
						if( curcnt == 1 ){
							activetable += "<td width='25%'> &nbsp;   </td> <td width='25%' > &nbsp; </td> ";
						}
						activetable += "</tr></table></tr>"; 
					}  
				} 
				
				activetable += "				<tr>" +
				"					<td colspan='4' style='text-align:center;'>" +
				"						 <input type='button'  value='Summery' name='order' id='order' onclick=\"return makeSummery('"+subprodid+"')\"/>" +
				"					</td>" + 
				"				</tr>" ; 
				activetable += "	<tr>" +
				"					<td colspan='4' id='summeryblock'>" + 
				"					</td>" +
				"				</tr>" + 
				"				</table>" +
				"			</td>" +
				"		</tr>" ; 
				activetable += "</table>" ; 
				
				activetable +=	"</tr></table>" +
						"<input type='hidden' name='minamount' id='minamount' value='"+def_minamount+"'/>" +
						"<input type='hidden' name='maxamount' id='maxamount' value='"+def_maxamount+"'/>" +
						"</tr>" ;
	 
		 
		 
		String amount = "";
		 
	 
		 
			try {
				String subprodamt_qry1 = "SELECT MAX_LOAD_AMOUNT FROM IFP_INSTPROD_DETAILS WHERE INST_ID='"+instid+"' AND PRODUCT_CODE='"+productcode+"' AND SUB_PROD_ID='"+subprodid+"'";
				System.out.println("subprodamt_qry__" + subprodamt_qry1);
				amount = (String)jdbctemplate.queryForObject(subprodamt_qry1,String.class);
			} catch (Exception e) {
				amount = "Error while fetch amount " + e;
				e.printStackTrace();
			}    
		
	} catch (Exception e) { 
		e.printStackTrace();
	} 
		 
		return activetable;
		 
}
	

	
	public void getCurrencyAmount() throws IOException  {
		String instid = comInstId();
		String curcode = getRequest().getParameter("curcode");
		String bin =  getRequest().getParameter("bin");
		 
		
		String curtable = "";
		try {
			
			String curamt_qry = "SELECT MIN_AMOUNT, MAX_AMOUNT  FROM IFP_BIN_CURRENCY WHERE INST_ID='"+instid+"' AND BIN='"+bin+"' AND CUR_CODE='"+curcode+"' ";	
			
			List curamtlist =  jdbctemplate.queryForList(curamt_qry);
			
			System.out.println("subprod_qry___" + curamt_qry);
			
			if( ! curamtlist.isEmpty() ){
				Iterator itr = curamtlist.iterator(); 
				while( itr.hasNext() ){
					Map temp = (Map) itr.next();
					System.out.println("map__" + temp );
					Object minamt = ( Object )  temp.get("MIN_AMOUNT");
					Object maxamt =  ( Object )  temp.get("MAX_AMOUNT");
					curtable += minamt+"~"+maxamt;	 
				}  
			}  
			 
		} catch (Exception e) { 
			 System.out.println("Error while get currycy amt " + e );
			 e.printStackTrace();
		} 
		getResponse().getWriter().write(curtable); 
	}
	
	
	public String activateInstantCardActionHome(){
		 
		HttpSession session = getRequest().getSession();
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);
		 
		
		String instid = comInstId();
		String usercode = comUserCode();
		String processtype = "INSTANT";
		String table_type = "INST_CARD_PROCESS";
		String activationtype = getRequest().getParameter("activationtype");
		String enteredcardno =  getRequest().getParameter("enteredcardno"); 
		String[] affectfee = getRequest().getParameterValues("affectfee");
		System.out.println("activation type__" + activationtype);
		boolean activesuc = true;
		int cardcount = 0;
		
		String curamountfield = getRequest().getParameter("curamountfield") ;
		String feeamountfield = getRequest().getParameter("feeamountfield") ;
		
		String defcurrency = null;
		String defloadamt ;
		String curamountfld ;
		 
		String curcode = "";
		String curloadableamt = "";
		
		String feecode = "";
		String feeloadableamt = "";
		
		String basecurrency = commondesc.getInstDefaultCurrency(instid, jdbctemplate);
		
		try{
			if( activationtype.equals("ORDERBASED")){
				String cardnolist[] = getRequest().getParameterValues("cardnolist");
				for( int i=0; i<cardnolist.length; i++ ){
					enteredcardno = cardnolist[i];  
					 
					/*String[] curamount = curamountfield.split("~");
					for( int acctcnt=0; acctcnt<curamount.length;acctcnt++){
						String[] curamountval =  curamount[acctcnt].split("-");
						if( curamountval != null ){
							curcode = curamountval[0];
							curloadableamt = curamountval[1];
						}
						if( insertCardAmountValues(instid, enteredcardno, LOAD_ENTITY, CARDACTIVECODE, CARDACTIVECODE, curcode, curloadableamt, basecurrency, jdbctemplate ) < 0  ){
							activesuc = false; 
							break;
						}
						
					}*/
					
					System.out.println("feeamountfield__" + feeamountfield );
					if( feeamountfield != "" ){
						String[] feeamount = feeamountfield.split("~");
						for( int feecnt=0; feecnt<feeamount.length;feecnt++){
							String[] feeamountval =  feeamount[feecnt].split("-");
							feecode = feeamountval[0];
							feeloadableamt = feeamountval[1];
							
							if( insertCardAmountValues(instid, enteredcardno, FEE_ENTITY, CARDACTIVECODE, feecode, basecurrency, feeloadableamt, basecurrency, jdbctemplate ) < 0  ){
								activesuc = false; 
								break;
							}
							
						}
					}
				 
				//	System.out.println("selected cardno __" + enteredcardno);
					
					defcurrency = commondesc.fchDefaultCurrency(instid, enteredcardno, table_type, jdbctemplate);
					//defloadamt = getRequest().getParameter("defloadamt");
					
					
					
					 if( activateInstCardActionInit(instid, enteredcardno, usercode, processtype, jdbctemplate, session ) < 0 ){ 
						activesuc = false;
						break;
					} 
					
					cardcount++;
				}
			}else{
				String curlist[] = getRequest().getParameterValues("selectedcurrency");
				
				/*for (int i=0; i<curlist.length; i++){
					curamountfld = curlist[i]+"amount";
					System.out.println( "curamountfld__"+ curamountfld);
					String curamount = getRequest().getParameter(curamountfld); 
					if( insertCardAmountValues(instid, enteredcardno, LOAD_ENTITY, CARDACTIVECODE, CARDACTIVECODE, curlist[i], curamount, basecurrency, jdbctemplate ) < 0  ){
						activesuc = false; 
						break;
					} 
				}*/
				
				if( getRequest().getParameterValues("selectedfee") != null ){ 
					String[] affect_fee_list = getRequest().getParameterValues("selectedfee");
					System.out.println("SINGLE CARD selectedfee...");
					for( int afcnt=0; afcnt<affect_fee_list.length; afcnt++ ){
						String feeid = affect_fee_list[afcnt];
						String feeamount = getRequest().getParameter( feeid+"amount" );
						 
					//	String default_currency = commondesc.fchDefaultCurrency(instid, cardno, table_type, jdbctemplate);
						System.out.println("SINGLE CARD ACTIVATINO FEE....");
						if( insertCardAmountValues(instid, enteredcardno, FEE_ENTITY, CARDACTIVECODE, feeid, basecurrency, feeamount, basecurrency, jdbctemplate ) < 0  ){
							activesuc = false; 
							break;
						}  
					} 
				}
				
				//defloadamt = getRequest().getParameter("defloadamt");
				//defcurrency = commondesc.fchDefaultCurrency(instid, enteredcardno, table_type, jdbctemplate);
				
				 if( activateInstCardActionInit(instid, enteredcardno,  usercode, processtype, jdbctemplate, session ) < 0 ){ 
					activesuc = false; 
				} 
				
				cardcount++;
			}
			
			if( activesuc ){				
				txManager.commit(status);
				System.out.println( "COMMITTED SUCCESSFULLY..........");
				session.setAttribute("curerr", "S");
				session.setAttribute("curmsg", cardcount +" CARD(S) ACTIVATED SUCCESSFULLY. WAITING FOR AUTHORIZATION" ); 
				
				/*** MAIL BLOCK ****/
			/*	IfpTransObj transactmail = commondesc.myTranObject(); 
				try {
					String alertid = this.parentid; 
					if( alertid != null && ! alertid.equals("000")){
						String keymsg = cardcount + "Instant card Activated ";
						int mail = comutil.sendMail( instid, alertid, keymsg, jdbctemplate, session, getMailSender() );
						System.out.println( "mail return__" + mail);
					} 
				} catch (Exception e) {  e.printStackTrace(); }
				  finally{
					transactmail.txManager.commit(transactmail.status);
					System.out.println( "mail commit successfully");
				} */
				/*** MAIL BLOCK ****/
				
				
			}else{
				txManager.rollback(status);
				System.out.println( "ROLLBACK SUCCESSFULLY..........");
				session.setAttribute("curerr", "E");
				session.setAttribute("curmsg",  " CARD ACTIVATION FAILED." ); 
			}
		}catch(Exception e ){
			
			txManager.rollback(status);
			e.printStackTrace();
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", "COULD NOT ACTIVATE THE CARD. "+e ); 
		}
		
		return this.activateInstCardHome();
	}
	
	public int activateInstCardActionInit(String instid, String card_no, String usercode, String processtype, JdbcTemplate jdbctemplate, HttpSession session) throws Exception
	{ 
		
		System.out.println("processtype======> "+processtype);
		String table = (String)session.getAttribute("PROCESS_TYPE");
		System.out.println("Table TYpe==> "+table);
		String table_type="INST_CARD_PROCESS";
		if(table.equals("PERSONAL"))
		{
			table_type = "PERS_CARD_PROCESS";
		} 
		 
	 
		System.out.println( " auth active  instant card ....");
		
	 
		Boolean errfld = false;
		System.out.println( "instid " + instid);
		 
		
		String processcode=null;
		if( processtype.equals("INSTANT")){
			processcode = "I";
		}
		else{
			processcode = "P";
		}
		String productcode = commondesc.getProductByCHN(instid, card_no, jdbctemplate, processtype );
		if( productcode.equals("NOREC")){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO PRODUCT CODE FOUND FOR THE CARD NO " + card_no  ); 
			return -1;
		}
		
		String subproducode = commondesc.getSubProductByCHN(instid, card_no, jdbctemplate, processtype );
		if( subproducode.equals("NOREC")){
			session.setAttribute("preverr", "E");
			session.setAttribute("prevmsg", "NO SUB PRODUCT CODE FOUND FOR THE CARD NO " + card_no  ); 
			return -1;
		}
		
		
		String conversionswipe = "0";
		String activefeeswipe = "0";
		if ( getRequest().getParameter("activefeeadjustamt") != null ){
			activefeeswipe = "1";
		}
	 
		
		//System.out.println( "cardno__ " + cardno + " -- conversionfee-" + conversionfee + "--activationfee -" +activationfee);
		
		String cardactivecurrencyqry = "";
		String cardactiveentry = "INSERT INTO IFP_CARD_ACTIVE(INST_ID, CARD_NO, ENTITY_CODE, ACTIVE_FEE_SWIPE, CONVERTION_FEE_SWIPE, MKCK_STATUS, USER_CODE, ACTIVATED_DATE, PROCESS_FLAG, PRODUCT_CODE, SUB_PROD_ID) values ";
		cardactiveentry += "('"+instid+"','"+card_no+"','"+CARDACTIVECODE+"','"+activefeeswipe+"','"+conversionswipe+"','M','"+usercode+"',sysdate,'"+processcode+"','"+productcode+"','"+subproducode+"' )";
	 	
		String auto_acct_flag ="0";
		String curamountfld ; 
		 
		System.out.println("cardactiveentry==> "+cardactiveentry);
		
		String updautoacctqry = "UPDATE INST_CARD_PROCESS SET AUTO_ACCT_FLAG='"+auto_acct_flag+"' "
		+ "WHERE INST_ID='"+instid+"' AND CARD_NO='"+card_no+"'"; 
		System.out.println("updautoacctqry==> "+updautoacctqry);
		int updacct = commondesc.executeTransaction(updautoacctqry, jdbctemplate);
		
		System.out.println("updacct__" + updacct);
		int instcrd = commondesc.executeTransaction(cardactiveentry, jdbctemplate);
		System.out.println("instcrd__" + instcrd); 
		 
		if(updacct != 1 || instcrd != 1 )
		{
			return -1;
		}
		
		int status = 0;
	 
		
		String updatestatusqry ;
		if( processtype.equals("INSTANT")){ 
			if ( activateInstCardAction(instid, card_no, subproducode, jdbctemplate, processtype) < 0 ){
				System.out.println( "INST CARD ACTIVATION FAILED ");
				return -1;
			}
		}  
		return 1; 
	} 
	public int insertCardAmountValues(String instid, String cardno, String entity_code, String action_code, String affect_code, String cur_code, String load_amount, String basecurrency, JdbcTemplate jdbctemplate){
		
		int x=-1;
		try {
			String conversionrate = commondesc.getConversionRate(instid, cur_code, jdbctemplate);
			Double base_convertedrate = Double.parseDouble(conversionrate) *  Double.parseDouble(load_amount) ;
			
			String cardactivecurrencyqry = "INSERT INTO IFP_CARD_ACTIVE_AMT (INST_ID, CARD_NO, ENTITY_CODE, ACTION_CODE, AFFECT_CODE, CUR_CODE, LOAD_AMT, CONVERT_RATE, BASE_CUR_CONVERTED_AMT, BASE_CURRENCY) VALUES ";
			cardactivecurrencyqry += "('"+instid+"','"+cardno+"','"+entity_code+"','"+action_code+"',"
			+ "'"+affect_code+"','"+cur_code+"','"+load_amount+"', '"+conversionrate+"', "
			+ " '"+base_convertedrate+"', '"+basecurrency+"')";
			System.out.println("cardactivecurrencyqry_" + cardactivecurrencyqry);
			x = commondesc.executeTransaction(cardactivecurrencyqry, jdbctemplate);
			
			 
		} catch (Exception e) { 
			e.printStackTrace();
		}
		return x;
		
		
	}
	

	
	public String getIssuerCurrency(String instid, String card_no, String tablename, JdbcTemplate jdbctemplate){
		
		String issuerccy = null; 
		try {
			
			/*String issuercurqry = "SELECT CARD_CCY FROM "+tablename+" WHERE "
			+ "INST_ID='"+instid+"' AND CARD_NO='"+card_no+"' AND ROWNUM<=1";	
			System.out.println("issuercurqry__"+issuercurqry);
			issuerccy = (String)jdbctemplate.queryForObject(issuercurqry, String.class);*/
			
			///by gowtham
			String issuercurqry = "SELECT CARD_CCY FROM "+tablename+" WHERE "
					+ "INST_ID=? AND CARD_NO=? AND ROWNUM<=?";	
					System.out.println("issuercurqry__"+issuercurqry);
					issuerccy = (String)jdbctemplate.queryForObject(issuercurqry,new Object[]{instid,card_no,"1"}, String.class);
			
		} catch (DataAccessException e) { 
			e.printStackTrace();
		}
		
		return issuerccy;
	}
	 
	
	// AUTHOURIZATION............ACTIVATED CARDS...
	public String authActivateInstCardAction(){ 
		System.out.println( " Authorize the activation instant card ....");
		HttpSession session = getRequest().getSession(); 
	
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus newstatus = txManager.getTransaction(def);
		 
		 
		String instid = comInstId();
	 
	 
		Boolean errfld = false;
		System.out.println( "instid " + instid);  
		String table = (String)session.getAttribute("PROCESS_TYPE"); 
		
		String usercode = comUserCode();
		
		String messageformat = "";
		
		String[] cardnolist =  getRequest().getParameterValues("activecardno"); 
		
		 DataOutputStream out = null;
		 DataInputStream in = null;
		 for ( int cardcnt=0; cardcnt<cardnolist.length; cardcnt++){
			List curlist = new ArrayList() ;
			Map curlistamt = new HashMap(); 
			
			String cardno = cardnolist[cardcnt];
			
			String curstrcode = this.getIssuerCurrency(instid, cardno, "CARD_PRODUCTION", jdbctemplate) ;
			String issuercurrency = commondesc.getCurrencyNumerals( curstrcode , jdbctemplate) ;
			if( issuercurrency == null ){
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "ISSUER CURRENCY VALUE EMPTY FOR THE CARD " +	cardno); 
				return this.activateInstCardHome(); 
			}
			 
			 
			int acctcnt = 0;
			
			String activemasterlistqry = "SELECT CUR_CODE, LOAD_AMT FROM IFP_CARD_ACTIVE_AMT WHERE"
			+ " INST_ID='"+instid+"' AND ACTION_CODE='"+CARDACTIVECODE+"' AND ENTITY_CODE='"+LOAD_ENTITY+"' AND CARD_NO='"+cardno+"'";
			System.out.println("activemasterlistqry__" + activemasterlistqry );
			List activemasterlist = jdbctemplate.queryForList(activemasterlistqry);  
			
			if( !activemasterlist.isEmpty() ){
				Iterator itr = activemasterlist.iterator();
				String isoacctformat = "";
				int x = 0;
				while( itr.hasNext() ){ 
					Map mp = (Map) itr.next();
					
					String curcode = commondesc.getCurrencyNumerals( (String)mp.get("CUR_CODE"), jdbctemplate); 
					String loadingamt = (String)mp.get("LOAD_AMT");
					 
					
					System.out.println("curcode_"+curcode);
				
					int curacctseq = commondesc.fchAcctSequance(instid, jdbctemplate);
					String acctnolen = commondesc.getAccountNoLength(instid, jdbctemplate );
					System.out.println( "Secondary acctnolength" + acctnolen );
					
					String acctno = commondesc.paddingZero(Integer.toString(curacctseq), Integer.parseInt(acctnolen));
					System.out.println("generated acctno___" + acctno );
					
					 
					
					 
					
					String acctnolength = commondesc.paddingZero( acctnolen , 3);
					String curcodelentth = commondesc.paddingZero( Integer.toString(curcode.length()) , 3);
					String amtfldlength = commondesc.paddingZero( Integer.toString(amountlength) , 3);
					
					String formattedamt = org.apache.commons.lang.StringUtils.leftPad(loadingamt, amountlength, '0');
					
					
					isoacctformat += acctnotag+acctnolength+acctno+curtag+curcodelentth+curcode+amounttag+amtfldlength+formattedamt;
					
					 
					
					acctcnt++;
					 
			}
					
					
				
					System.out.println("/************************ 	ISO COMMUNICATION BEGINS	******************************************************");
			 
					 
					String totalacctlenght =   commondesc.paddingZero( Integer.toString(acctcnt) , 3);
					String isoformattedacct = acctinfotag+totalacctlenght+isoacctformat;
					
					String isoformattedacctlength = commondesc.paddingZero( Integer.toString( isoformattedacct.length() ) , 3);
					
					isoformattedacct = isoformattedacctlength+isoformattedacct;
					
					System.out.println("isoformattedacct__"+isoformattedacct);
					Socket connect_id = null;
					try {
						 
						ISOCommunication iso = new ISOCommunication();
						
						String hostip = this.comUtil().getHostIpAddress(instid, jdbctemplate);
						if( hostip.equals("000.000.000.000")){
							txManager.rollback(newstatus);	
							session.setAttribute("curerr", "E");
							session.setAttribute("curmsg", "HOST IP ADDRESS AND PORT NOT CONFIGURED..." ); 
							return this.activateAuthInstCardHome();
						}
						
						int port = this.comUtil().getHostPort(hostip, jdbctemplate);
						if( port != -1 ){
							System.out.println("CONNECTION SERVER...HOST : "+ hostip +"- PORT : "+port);
							connect_id = iso.connectHost( hostip, port, 9000 ) ; 
							if( connect_id == null ){
								txManager.rollback(newstatus);
								session.setAttribute("preverr", "E");
								session.setAttribute("prevmsg", "COULD NOT CONNECT HOST. CONNECTION TIMEOUT..IP: "+hostip+ " - PORT : " +port ); 
								return this.activateAuthInstCardHome();
							}
						}else{
							
							txManager.rollback(newstatus);
							
							
							session.setAttribute("preverr", "E");
							session.setAttribute("prevmsg", "NETWORK BUSY...." ); 
							return this.activateAuthInstCardHome();
						}
						
						System.out.println("hostip_"+hostip+"_port_"+port);
						
						 
						
						Properties prop = comUtil().getPropertyFile();
						URL ISOCONFIGXML = getClass().getResource("basic.xml");
						File isoxmlfile = new File(ISOCONFIGXML.getPath());
						
						GenericPackager packager = new GenericPackager( ISOCONFIGXML.toString() ); //prop.getProperty("IS0.PACKAGER")
						
						ISOMsg isomsg = new ISOMsg();
						isomsg.setPackager(packager); 
						
						String PCODE = "810000";
						String datetime = "";
						Map isokeyvalmap = iso.getIsoFieldSet(instid, PCODE, jdbctemplate );
						if( !isokeyvalmap.isEmpty() ){
							String[] keys =  (String[])( isokeyvalmap.keySet().toArray( new String[isokeyvalmap.size()] ) );
							int error = 0;
							
							System.out.println("CONFIGURED ISO LENGTH--"+keys.length);
							
							for( int i=0; i<keys.length; i++ ){
								String isovalue = (String) isokeyvalmap.get(keys[i]);
								
								int isokey = Integer.parseInt(keys[i]);
								System.out.println( " ******************\n");
								if( isovalue.equals("MTI")){
									System.out.println( "isovalue_" + isovalue + "\n");
									isomsg.setMTI("0200");
									continue;
								}
								
								if( isovalue.equals("CHN")){
									System.out.println( "isovalue_" + isovalue + "\n");
									
									isomsg.set(isokey, cardno);
									System.out.println("SET KEY : " + isokey + "-VALUE :" +cardno);
									continue;
								}
								
								if( isovalue.equals("TXNCODE")){
									System.out.println( "isovalue_" + isovalue + "\n");
									
									isomsg.set(isokey, PCODE);
									System.out.println("SET KEY : " + isokey + "-VALUE :" +PCODE);
									continue;
								}
								
								if( isovalue.equals("TRANDATETIME")){
									System.out.println( "isovalue_" + isovalue + "\n");
									
									datetime = commondesc.getLocaldatetime("DT");
									if(datetime != null){
										isomsg.set(isokey, datetime);
									}else{
										System.out.println("Tran Date Time Received as null");
										error = 1;
									}
									
									System.out.println("SET KEY : " + isokey + "-VALUE :" +datetime);
									continue;
								}
								
								if( isovalue.equals("TRANNO")){
									System.out.println( "isovalue_" + isovalue + "\n");
									
									String tranno = comUtil().getTransactionNumber();
									isomsg.set(isokey, tranno);
									
									System.out.println("SET KEY : " + isokey + "-VALUE :" +tranno);
									continue;
								} 
								
								if( isovalue.equals("LOCALDATE")){
									System.out.println( "isovalue_" + isovalue + "\n");
									
									datetime = commondesc.getLocaldatetime("D");
									if(datetime != null){
										isomsg.set(isokey, datetime);
									}else{
										System.out.println("Local Date Time Received as null");
										error = 1;
									}
									System.out.println("SET KEY : " + isokey + "-VALUE :" +datetime);
									continue;
								}
								
								if( isovalue.equals("LOCALTIME")){
									System.out.println( "isovalue_" + isovalue + "\n");
									
									datetime = commondesc.getLocaldatetime("T");
									if(datetime != null){
										isomsg.set(isokey, datetime);
									}else{
										System.out.println("Local Time Received as null");
										error = 1;
									}
									System.out.println("SET KEY : " + isokey + "-VALUE :" +datetime);
									continue;
								}
								
								if( isovalue.equals("INSTID")){ 
									System.out.println( "isovalue_" + isovalue + "\n");
									
									isomsg.set(isokey, instid);
									System.out.println("SET KEY : " + isokey + "-VALUE :" +instid);
									continue;
								}
								
								if( isovalue.equals("IPADDRESS")){ 
									System.out.println( "isovalue_" + isovalue + "\n");
									
									String ipaddress = comUtil().getIpAddress();
									isomsg.set(isokey, ipaddress);
									System.out.println("SET KEY : " + isokey + " -VALUE :" +ipaddress);
									continue;
								} 
								
								if( isovalue.equals("LOCATION")){ 
									System.out.println( "isovalue_" + isovalue + "\n");
									
									String location = comUtil().getLocation();
									isomsg.set(isokey, location);
									System.out.println("SET KEY : " + isokey + " -VALUE :" +location);
									continue;
								} 
								
								if( isovalue.equals("BASECCY")){  
									System.out.println( "isovalue_" + isovalue);
									
									isomsg.set(isokey, issuercurrency);
									System.out.println("SET KEY : " + isokey + " -VALUE :" +issuercurrency);
									continue;
								}
								
								if( isovalue.equals("ACCTINFO")){  
									System.out.println( "isovalue_" + isovalue + "\n");
									
									isomsg.set(isokey, isoformattedacct);
									System.out.println("SET KEY : " + isokey + " -VALUE :" +isoformattedacct);
									continue;
								}  
								
							}
							
							
						
						}else{
							if( newstatus != null ){
								txManager.rollback(newstatus);
							}
							session.setAttribute("preverr", "E");
							session.setAttribute("prevmsg", "NO ISO VALUE CONFIGURED AND MAPPED..." +	isokeyvalmap ); 
							return this.activateInstCardHome(); 
						}
						
						System.out.println("isoformattedacct__"+ isoformattedacct );
						System.out.println("********** ISO MESSAGE ***************** " );
						
						
						out = new DataOutputStream (connect_id.getOutputStream());
						byte[] data = isomsg.pack();
						String isomsg_str = new String(data);
						out.writeUTF(isomsg_str);
						System.out.println("MESSAGE SEND SUCCESFULLY." + isomsg_str);
						
						 in = new DataInputStream (connect_id.getInputStream());
						 out.flush();
						 String responsemsg = in.readUTF();  
						 System.out.println("RESPONSE RECEIVED SUCCESFULLY." + responsemsg);
						 
						 System.out.println("data__" + data );
						System.out.println("isomsg__" + isomsg );
						System.out.println("isomsg_str_" + isomsg_str );
						
						 isomsg.unpack(responsemsg.getBytes());
						 String resp = iso.responseISO(isomsg);
						    if( ! resp.equals("00")){
						    	session.setAttribute("preverr", "E");
								session.setAttribute("prevmsg", " HOST ERROR...RECIVED RESPONSE CODE  " + resp ); 
								return this.activateAuthInstCardHome(); 
						    }else{
						    	
						    	System.out.println( "Response code is " + resp);
						    } 
						    
						    
						     
					 
					} catch (Exception e) { 
						if( newstatus != null ){
							txManager.rollback(newstatus);
						}
						e.printStackTrace();
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", "Could not activate the card" + e ); 
						return this.activateAuthInstCardHome();   
					}finally{
						try { 
							if( connect_id != null ){
								connect_id.close();
							} 
						} catch (IOException e) { 
							e.printStackTrace();
						}
						
						System.out.println(" SOCKET CONNECTION CLOSED PROPERLY..." );
						
					}
					
					int sendmessage = commondesc.sendMessage( messageformat );
					if( sendmessage < 0 ){
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", "Could not activate the card" ); 
						return this.activateAuthInstCardHome(); 
					}
			 
					System.out.println("/**************************** 	ISO COMMUNICATION END	*********************************************************");
					
					 
					 
				 
				session.setAttribute("preverr", "S");
				session.setAttribute("prevmsg", "Card Activated Successfully" ); 
				txManager.commit(newstatus);
				System.out.println( "Committed success");
				return this.activateInstCardHome(); 
				 
			}    
		
		} 
		
		
		return null;
	}
	
	
	/****************** CARD ACTIVATION PROCESS*********************************************************/
public int activateInstCardAction( String instid, String passedcardno, String subproducode, JdbcTemplate jdbctemplate, String processtype ) {
		System.out.println( " active  instant card ....");
		HttpSession session = getRequest().getSession(); 
		//String instid = comInstId();
		//IfpTransObj transactobj = commondesc.myTranObject();
		//JdbcTemplate jdbctemplate = new JdbcTemplate(dataSource);
		Boolean errfld = false;
		System.out.println( "instid " + instid); 
		
		//String processtype = "INSTANT";
		String table = (String)session.getAttribute("PROCESS_TYPE");
		System.out.println("Table TYpe==> "+table);
		String table_type="INST_CARD_PROCESS";
		if(table.equals("PERSONAL"))
		{
			table_type = "PERS_CARD_PROCESS";
		} 
		
		
		
		String usercode = comUserCode();
		
		String messageformat = "";
		
		//String[] cardnolist =  getRequest().getParameterValues("cardno"); 
		
		//for ( int cardcnt=0; cardcnt<cardnolist.length; cardcnt++){
			List curlist = new ArrayList() ;
			Map curlistamt = new HashMap();
			
			DataOutputStream out = null;
			DataInputStream in = null;
			 
			
			String cardcurrency = commondesc.getSubProductCurrencyCode(instid, subproducode, jdbctemplate);
			boolean insert_status = true;
			String cardno = passedcardno;
			String customerid = commondesc.fchCustomerId(instid, cardno, jdbctemplate);
			int acctcnt = 0;
			
			/*String activemasterlistqry = "SELECT CUR_CODE, LOAD_AMT FROM IFP_CARD_ACTIVE_AMT"
			+ " WHERE INST_ID='"+instid+"' AND ACTION_CODE='"+CARDACTIVECODE+"' AND"
			+ " ENTITY_CODE='"+LOAD_ENTITY+"'  AND CARD_NO='"+passedcardno+"'";
			System.out.println("activemasterlistqry__" + activemasterlistqry );
			List activemasterlist = jdbctemplate.queryForList(activemasterlistqry);*/
			
			//by gowtham
			String activemasterlistqry = "SELECT CUR_CODE, LOAD_AMT FROM IFP_CARD_ACTIVE_AMT"
					+ " WHERE INST_ID=? AND ACTION_CODE=? AND"
					+ " ENTITY_CODE=?  AND CARD_NO=? ";
					System.out.println("activemasterlistqry__" + activemasterlistqry );
					List activemasterlist = jdbctemplate.queryForList(activemasterlistqry,new Object[]{instid,CARDACTIVECODE,
							LOAD_ENTITY,passedcardno});
			
			System.out.println("activemasterlist====>  "+activemasterlist);
			
			
			//if( !activemasterlist.isEmpty() ){
			try{
				System.out.println("activemasterlist===. "+activemasterlist);
				Iterator itr = activemasterlist.iterator();
				String isoacctformat = "";
				int primflag = 2;
				while( itr.hasNext() ){ 
					Map mp = (Map) itr.next();
					
					String curcode = commondesc.getCurrencyNumerals( (String)mp.get("CUR_CODE"), jdbctemplate); 
					
					
					if( cardcurrency.equals(curcode) || cardcurrency.equals( (String)mp.get("CUR_CODE") )){
						primflag = 1;
					}else{
						primflag++;
					}
					
					 
					String loadingamt = (String)mp.get("LOAD_AMT");
					 
					
					System.out.println("curcode_"+curcode);
				
					int curacctseq = commondesc.fchAcctSequance(instid, jdbctemplate);
					String acctnolen = commondesc.getAccountNoLength(instid, jdbctemplate );
					System.out.println( "Secondary acctnolength" + acctnolen ); 
					String acctno = curcode+commondesc.paddingZero(Integer.toString(curacctseq), Integer.parseInt(acctnolen)-curcode.length());
					System.out.println("generated acctno___" + acctno );
					
					 
					 
					 
					
					/*String inserqry = "INSERT INTO ACCOUNTINFO ( INST_ID, ACCT_NO, ACCT_CCY, ACCT_STATUS, AVAIL_BALANCE, FEE_AMOUNT,COMMISSION_AMOUNT,EXPENSE_AMOUNT,DISCOUNT_AMOUNT, ACCT_TYPE, PRIM_FLAG, LEDGER_BALANCE) VALUES " ;
					inserqry += " ( '"+instid+"','"+acctno+"','"+curcode+"','1','"+loadingamt+"',0,0,0,0,'S', '"+primflag+"', '0' )"; 
					System.out.println("Secondary insert__" + inserqry);
					
					String insertcardlink = "INSERT INTO IFP_CARD_ACCT_LINK ( INST_ID, CARD_NO, CIN, ACCT_NO, MAKER_DATE, ACCT_CCY, ACCT_STATUS, MAKER_ID, ACCT_PRIORITY) VALUES";
					insertcardlink += "('"+instid+"','"+passedcardno+"','"+customerid+"','"+acctno+"', sysdate, '"+curcode+"','1','"+usercode+"', '"+primflag+"')";
					System.out.println("Secondary insert__" + insertcardlink);
					int acctinsert = commondesc.executeTransaction(inserqry, jdbctemplate);
					int acctlink =  commondesc.executeTransaction(insertcardlink, jdbctemplate); */
					
					///by gowtham
					String inserqry = "INSERT INTO ACCOUNTINFO ( INST_ID, ACCT_NO, ACCT_CCY, ACCT_STATUS, AVAIL_BALANCE, FEE_AMOUNT,COMMISSION_AMOUNT,EXPENSE_AMOUNT,DISCOUNT_AMOUNT, ACCT_TYPE, PRIM_FLAG, LEDGER_BALANCE) VALUES " ;
					inserqry += " ( '"+instid+"','"+acctno+"','"+curcode+"','1','"+loadingamt+"',0,0,0,0,'S', "
					+ "'"+primflag+"', '0' )"; 
					System.out.println("Secondary insert__" + inserqry);
					
					String insertcardlink = "INSERT INTO IFP_CARD_ACCT_LINK ( INST_ID, CARD_NO, CIN, ACCT_NO, MAKER_DATE, ACCT_CCY, ACCT_STATUS, MAKER_ID, ACCT_PRIORITY) VALUES";
					insertcardlink += "('"+instid+"','"+passedcardno+"','"+customerid+"','"+acctno+"', "
					+ "sysdate, '"+curcode+"','1','"+usercode+"', '"+primflag+"')";
					System.out.println("Secondary insert__" + insertcardlink);
					int acctinsert = commondesc.executeTransaction(inserqry, jdbctemplate);
					int acctlink =  commondesc.executeTransaction(insertcardlink, jdbctemplate); 
					
					
					System.out.println("acctinsert ==> "+acctinsert+ "  acctlink===> "+acctlink);	  
					if(acctinsert != 1 || acctlink != 1 )
					{
						System.out.println("Insert Failed for ACCT INFO or ACCT LINK ");
						insert_status = false;
						break;
						
					} 
					 
				}
					if(!insert_status)
					{
						session.setAttribute("curerr", "E");
						session.setAttribute("curmsg", "Error while insert the record to production " ); 
						System.out.println("Error while insert the record to production");
						return -1; 
					}
					
					if ( activatedao.moveCardToProduction(instid, cardno,  jdbctemplate ) != 1 ) {
						//transactobj.txManager.rollback(transactobj.status);
						System.out.println( "while move to card production..problem ..Rollback success");
						session.setAttribute("curerr", "E");
						session.setAttribute("curmsg", "Error while moving the record to production " ); 
						return -1; 
					}
					 
					if( activatedao.updateCardStatus(instid, cardno, ACTIVESTTUSCODE, jdbctemplate, session ) < 0 ){ 
						//transactobj.txManager.rollback(transactobj.status);
						System.out.println( "while update the card status..problem ..Rollback success");
						session.setAttribute("preverr", "E");
						session.setAttribute("prevmsg", "Error while moving the record to production " ); 
						return -1; 
					} 
	
	
					String subprod = commondesc.getSubProductByCHN(instid, cardno, jdbctemplate, "INSTANT");
					
					/*String cardreg_req_qry = "SELECT CUST_REG_REQ FROM IFP_INSTPROD_DETAILS WHERE INST_ID='"+instid+"'"
					+ " AND SUB_PROD_ID='"+subprod+"'";
					String cardreg_req = (String) jdbctemplate.queryForObject(cardreg_req_qry, String.class);*/
					
					//by gowtham
					String cardreg_req_qry = "SELECT CUST_REG_REQ FROM IFP_INSTPROD_DETAILS WHERE INST_ID=? "
							+ " AND SUB_PROD_ID=? ";
							String cardreg_req = (String) jdbctemplate.queryForObject(cardreg_req_qry,new Object[]{instid,subprod}, String.class);
					
					if( cardreg_req.equals( "Y")){
					
						/*if( this.movePintoProduction(instid, cardno) != 1 ){
							transact.txManager.rollback(transact.status);
							System.out.println( "Move pin to production issue..Rollback success");
							session.setAttribute("preverr", "E");
							session.setAttribute("prevmsg", "Error while moving the pin data to production " ); 
							return this.activateAuthInstCardHome(); 
						}*/
						
						String existcustid = activatedao.getCustomerId(instid, cardno, jdbctemplate );
						int moveingproduction = activatedao.moveCustProcessToProduction( instid, cardno, existcustid, jdbctemplate,  session );
						if (    moveingproduction < 0 ) { 
							//transactobj.txManager.rollback(transactobj.status);
							System.out.println( "Customer issue..Rollback success");
							session.setAttribute("preverr", "E");
							session.setAttribute("prevmsg", "Error while moving the customer details to production [ "+moveingproduction+" ] " ); 
							return -1; 
						}
						
						if( activatedao.deleteCardProcessRecords(  instid, cardno,  jdbctemplate, session ) < 0 ){ 
							System.out.println( "while delete from card process issue..Rollback success"); 
							return -1; 
						} 
						
						System.out.println( "Deleting customer process table....\n");
						if( activatedao.deleteCustomerProcessRecords(instid, cardno, existcustid, jdbctemplate, session)  < 0 ){ 
							System.out.println( "while delete from customer process issue..Rollback success"); 
							return -1; 
						} 
						
					}else{
						String CIN =  commondesc.cinnumberGeneratoer(instid,jdbctemplate);
						String UNCIN = "UNK"+CIN;
						String updatecin_qury = commondesc.updateCINcount(instid);	
						System.out.print("updatecin_qury==> "+updatecin_qury); 
						int cinnum_update = jdbctemplate.update(updatecin_qury);
						System.out.print("cinnum_update==> "+cinnum_update); 
						if( this.updateUnKnownCustomerId(  instid, cardno, UNCIN , jdbctemplate ) < 0 ){
							//transactobj.txManager.rollback(transactobj.status);
							System.out.println( "while update unknown customer id issue..Rollback success");
							session.setAttribute("preverr", "E");
							session.setAttribute("prevmsg", "ERROR WHILE UPDATE UN CUSTOMER ID " );   
							return -1; 
						} 
						
						if( activatedao.deleteCardProcessRecords(  instid, cardno,  jdbctemplate, session ) < 0 ){
							//transactobj.txManager.rollback(transactobj.status);
							System.out.println( "while delete from card process issue..Rollback success"); 
							return -1; 
						} 
						
					} 
			}catch(Exception e ){
				e.printStackTrace();
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "COULD NOT AUTHORIZE. "+e.toString()  ); 
			}
				/*
				if( deleteCardActivationTable(instid, cardno, CARDACTIVECODE, jdbctemplate, session) < 0 ){ 
					transact.txManager.rollback(transact.status);
					System.out.println( "ERROR .... Rollback success");
					return this.activateInstCardHome(); 
				} 
				*/
				return 1; 
				 
			/*} 
			else{
				session.setAttribute("preverr", "E");
				session.setAttribute("prevmsg", "Currency Not Found , Card Activtion Failed ." ); 
				return -1;
			}*/

	}
	
	/****************** CARD ACTIVATION PROCESS*********************************************************/
	
	
	

	
	
	public int updateUnKnownCustomerId( String instid, String cardno, String UNCIN , JdbcTemplate jdbctemplate ){
		try {
			
			String updqry = "UPDATE CARD_PRODUCTION SET CIN='"+UNCIN+"' WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
			String updacctlink = "UPDATE IFP_CARD_ACCT_LINK  SET CIN='"+UNCIN+"' WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"'";
			System.out.println("updqry__" + updqry );
			System.out.println("updacctlink__" + updacctlink ); 
			commondesc.executeTransaction(updqry, jdbctemplate);
			commondesc.executeTransaction(updacctlink, jdbctemplate);
			
			
			return 1;
			 
		} catch (Exception e) { 
			System.out.println("Error while update updateUnKnownCustomerId " + e);
			e.printStackTrace();
			return -1;
		}
		
	}
	
	
	
	
	
	
	
	public int generateAccountDetails( String instid ){
		return 1;
	}
	
	public int generateAccountLink( String instid ){
		return 1;
	}
	
	
	
	
	

	
	
	public void makeSummery( ) throws IOException  {
		try {
			System.out.println( "Making summery...."  );
			String resptext = "";
			Boolean secondarycurrency = false;
		 
			String curselected = getRequest().getParameter("mymap");
			String defcuramt = getRequest().getParameter("defcuramt");
			String feedesc = "";
			String instid = comInstId();
			Map  curcodeamtmap = new HashMap();
			HttpSession session = getRequest().getSession();
			String activetype = getRequest().getParameter("ACTIVETYPE");
			String subproductid  = getRequest().getParameter("SUBPRODUCT") ;
			System.out.println( "subproductid__" + subproductid );
			String orderrefno = getRequest().getParameter("orderrefno");
			String processtype = "INSTANT";
			String table = (String)session.getAttribute("PROCESS_TYPE");
			System.out.println("Table TYpe==> "+table);
			String table_type="INST_CARD_PROCESS";
			if(table.equals("PERSONAL"))
			{
				table_type = "PERS_CARD_PROCESS";
			} 
			 
			String defcurrencycode =  commondesc.getSubProductCurrencyCode(instid, subproductid, jdbctemplate);// commondesc.fchDefaultCurrency(instid, cardno, table_type, jdbctemplate);
			String defcurrency = commondesc.getCurrencyAlphaCode(defcurrencycode, jdbctemplate);
			System.out.println(table_type+"_defcurrency__" + defcurrency );
			if( defcurrency.equals("NOREC")){ 
				 defcurrency = commondesc.fchDefaultCurrency(instid, activatebeans.cardno, "CARD_PRODUCTION", jdbctemplate);
				 if( defcurrency.equals("NOREC")){
					 	resptext = " COULD NOT GET CARD CURRENCY FROM PROCESS <BR/>" ;
					 	getResponse().getWriter().write(resptext);
				 } 
			}
			
			String curarry[] = curselected.split("~");
			for ( int x=0; x<curarry.length; x++ ){
				String carpair = curarry[x];
				if( !carpair.equals("")  && carpair != null ){
					String curcodevalue[] = carpair.split("-");
					String curcode = curcodevalue[0];
					String curamt = curcodevalue[1];
					curcodeamtmap.put(curcode, curamt);
					secondarycurrency = true;
				}
			}
			curcodeamtmap.put(defcurrency, defcuramt);
			
			System.out.println( "curcodeamt__" + curcodeamtmap.size());
			
			Iterator <Map.Entry<String, String>> mpitr = curcodeamtmap.entrySet().iterator();
			
			 
			String summerytable  = "<table border='0' cellpadding='0' cellspacing='0' width='70%' class='summerytable' align='center'>";
			summerytable  += "<input type='hidden' name='enteredcardno' value='"+activatebeans.cardno+"' />";
			String activationfee = "0";
			String conversionfee = "0";
			double totalamt = 0.0;
			Boolean activestatus = true;
			
			
			/***************** GL MAPPING **********************/
		 
		 
			String glsccode = commondesc.fchGlSchemeCode(instid, subproductid, jdbctemplate);
			if( glsccode.equals("NOREC")){
				resptext = "NO GL CONFIGURED FOR THE SUB PRODUCT " + subproductid; 
			}
			
			/*List glmaplist = commondesc.getAffectingFeeGlCodes(instid, glsccode, CARDACTIVECODE, jdbctemplate);
			if( glmaplist != null ){ 
				Iterator glmpitr = glmaplist.iterator();
				while( glmpitr.hasNext() ){
					Map glmp = (Map)glmpitr.next();
					String affgl = (String)glmp.get("AFFECTING_SCH_CODE");
					String affecting_fees = commondesc.getAffectingFees( instid, affgl, jdbctemplate );
					if(! affecting_fees.equals("NOREC")){
						
						System.out.println( " affecting_fees__ "  + affecting_fees ); 
						String cardactiondesc = commondesc.getFeeDescription(instid, affecting_fees, jdbctemplate);
						
						String feecode = commondesc.getFeeCode(instid,subproductid,jdbctemplate);
						if ( feecode.equals("NOREC")){
							resptext = "NO FEE CODE FOUND FOR THE SUBPRODUCT " + subproductid; 
						}
						
						
						 activationfee = commondesc.getFeeAmount(instid, affecting_fees, feecode, jdbctemplate);
						 
						 
						summerytable += "<tr><td> " + cardactiondesc +" </td> <td class='amount' > : " + activationfee + " <input type='hidden' name='affectfee' value='"+affecting_fees+"~"+activationfee+"' /></td></tr>";
						
						 
						totalamt +=  Double.parseDouble(conversionfee);
						
					}else{
						resptext = "NO ACTION MAPPED TO THE GL SCHEME CODE " + affgl ;
					}
					
				}
			}*/
			 
			/***************** GL MAPPING **********************/
			
			System.out.println("GETTING ACCT RULE ");
			String acctrule = "";
			
			/*String txnqry = "SELECT MSG_TYPE, RESP_CODE, ORIGIN_CHANNEL, DEVICE_TYPE FROM"
			+ " IFP_CARD_ACTIVE_TXNPROPERTIES WHERE INST_ID='"+instid+"' AND TXN_CODE='"+CARDACTIVECODE+"' AND ROWNUM<=1";
			System.out.println( "txnqry__" + txnqry );
			List txnqrylist = jdbctemplate.queryForList(txnqry);*/

			//by gowtham
			String txnqry = "SELECT MSG_TYPE, RESP_CODE, ORIGIN_CHANNEL, DEVICE_TYPE FROM"
					+ " IFP_CARD_ACTIVE_TXNPROPERTIES WHERE INST_ID=? AND TXN_CODE=? AND ROWNUM<=?";
					System.out.println( "txnqry__" + txnqry );
					List txnqrylist = jdbctemplate.queryForList(txnqry,new Object[]{instid,CARDACTIVECODE,"1"});
			if( txnqrylist.isEmpty() ){  
				resptext = "NO ACCOUNT RULE CONFIGURED FOR THE CARD ACTIVATION." ;
				getResponse().getWriter().write(resptext);
				return ;
			}
			else{
				Iterator itr = txnqrylist.iterator();
				while( itr.hasNext() ){
					Map mp = (Map)itr.next();
					String msgtype = (String) mp.get("MSG_TYPE");
					String txncode = commondesc.getTxcCodeByAction(instid, CARDACTIVECODE, jdbctemplate);
					if ( txncode.equals("NOREC")){
						resptext = "NO TXNCODE CONFIGURED FOR THE MAPPING CODE [ '"+ CARDACTIVECODE +"' ]." ;
						getResponse().getWriter().write(resptext);
						return ; 
					}
					String respcode = (String) mp.get("RESP_CODE");
					String orginchannel = (String) mp.get("ORIGIN_CHANNEL");
					String devicetype = (String) mp.get("DEVICE_TYPE");
					String acctruleargs = instid+subproductid+msgtype+txncode+respcode+orginchannel+devicetype;
					
					/*acctrule = commondesc.getAccountRuleCode(acctruleargs, jdbctemplate);
					if(acctrule.equals("NOREC")){
						resptext = "NO ACCOUNT RULE CONFIGURED FOR THE  SUBPRODUCT[ "+subproductid+" ], MSGTYPE[ "+msgtype+" ], TXNCODE[ "+txncode+" ], RESPONSECODE[ "+respcode+" ], ORGINCHANNEL[ "+orginchannel+" ], DEVICETYPE[ "+devicetype+" ] " ;
						getResponse().getWriter().write(resptext);
						return ;
					}*/
					
				}
			}
			
			
			
			
			int curcnt = 1;
			while ( mpitr.hasNext()  ){
				 Map.Entry<String, String> entry = mpitr.next();
				String entrycurcode = entry.getKey();
				String entrycuramt = entry.getValue();
				System.out.println( "entrycurcode__" + entrycurcode);
				String checkvalidamt = this.checkMinMaxLoadAmt(instid, entrycurcode, entrycuramt, jdbctemplate);
				System.out.println("checkvalidamt__" + checkvalidamt);
				if( !checkvalidamt.equals("SUC")){
					//getResponse().getWriter().write(checkvalidamt);
					resptext = checkvalidamt;
					activestatus = false;
					break; 
				}
				
				
				String curdesc = commondesc.getCurDesc(entrycurcode, jdbctemplate);
				
				//totalamt +=  Double.parseDouble(entrycuramt);
				
				
			 	/*String conversionrate = commondesc.getConversionRate(instid, entrycurcode, jdbctemplate);
				if(conversionrate.equals("NOREC")){
					resptext = "NO CURRENCY CONVERSION RATE CONFIGURED FOR " + curdesc ;
					activestatus = false;
					break; 
				}
			 
				
				Double convertedrate =  Double.parseDouble(conversionrate)  *   Double.parseDouble(entrycuramt);
				summerytable += "<tr><td> " + curdesc + " Loding Amount </td> <td class='amount'> : ";
				if( !defcurrency.equals(entrycurcode)){
					summerytable += "( "+entrycuramt+" * "+conversionrate+"   ) ";
				}
				summerytable += defcurrency + " " + moneyForamatter(convertedrate) +   " <input type='hidden' name='selectedcurrency' id='cur"+curcnt+"' value='"+entrycurcode+"' />   <input type='hidden' name='"+entrycurcode+"amount' id='"+entrycurcode+"' value='"+entrycuramt+"' /> "  ;
				
			
				totalamt +=convertedrate;
					*/
				curcnt++;
			}
			
			if( activestatus ){
				System.out.println("activestatus__" + activestatus);
				 
				
				
			 
				
				/*********  FEE BLOCK ***************/
				
				
				 String feecode = commondesc.getFeeCode(instid,subproductid,jdbctemplate);
				if ( feecode.equals("NOREC")){
					resptext = "NO FEE CODE FOUND FOR THE SUBPRODUCT " + subproductid; 
				}
				
				/*List affectingfeelist = commondesc.getListOfAffectingFees(instid, acctrule, jdbctemplate);
				if( !affectingfeelist.isEmpty() ){
					Iterator itr = affectingfeelist.iterator();
					int feecnt = 1;
					while ( itr.hasNext() ){
						Map mp = (Map)itr.next();
						String applicableaction = (String)mp.get("APP_ACTION");
						
						activationfee = commondesc.getFeeAmount(instid, applicableaction, feecode, jdbctemplate);
						feedesc = commondesc.getFeeDescription(instid, applicableaction, jdbctemplate );
					
					
						if( activationfee.equals("NOREC")){
							resptext = "NO " +feedesc+" FEE AMOUNT FOUND FOR THE SUBPRODUCT " + subproductid; 
						}else{
							if ( !feedesc.equals("NOREC")){
								summerytable += "<tr><td> " + feedesc +" </td> <td  class='amount'  > : " + defcurrency + " " + activationfee + " <input type='hidden' name='selectedfee'  id='fee"+feecnt+"' value='"+applicableaction+"' /> <input type='hidden' name='"+applicableaction+"amount' id='"+applicableaction+"' value='"+activationfee+"' /> </td></tr>";
								totalamt +=   Double.parseDouble(activationfee);
							}else{
								resptext = "NO DESCRIPTION FOUND FOR CARDACTIVECODE"; 
							}
							
						}
						
						feecnt++;
					}
					
				} */
				
				/*********  FEE BLOCK ***************/
			}
			
		 
			
			 
			
			summerytable += "<tr><td> Sum of Amount </td> <td class='amount' > :<b> " + defcurrency + " " + moneyForamatter(totalamt) + "</b></td></tr>";
			
			
			summerytable += "</table>";
			
			System.out.println( "ACTIVATION TYPE IS " + activetype);
			if( !activetype.equals("ORDERBASED")){
				summerytable += "<table border='0' cellpadding='0' cellspacing='4' width='20%' >" +
						"		 <tr> <td>	<input type='submit' value='Submit' name='order' id='order' />	</td>" +
						"<td><input type='button' name='cancel' id='cancel' value='Cancel'  class='cancelbtn'  onclick='return confirmCancel()'/></td></tr>" +
						"</table>";
			}else{
				summerytable += this.getOrderDetails(instid, orderrefno, defcuramt, jdbctemplate);
			}
			System.out.println("resptext__" + resptext);
			
			if( !resptext.equals("")){
				getResponse().getWriter().write(resptext);
			}else{
				getResponse().getWriter().write(summerytable);
			}
			 
			 
		} catch (Exception e) { 
			getResponse().getWriter().write(e.toString());
			e.printStackTrace();
		}
		
		

		//getResponse().getWriter().write("prito");
		
		/*
		 String x = getRequest().getParameter("mymap");
		 System.out.println( "x__" + x ); 
		try {
			JSONObject myObject = new JSONObject();
			 System.out.println( "myObject__" + myObject );
		} catch (Exception e) {

			e.printStackTrace();
		}
		 */
		
	}
	
	private String moneyForamatter(double d) {
	    String s = null;
	    if (Math.round(d) != d) {
	        s = String.format("%.2f", d);
	    } else {
	        s = String.format("%.0f", d);
	    }
	    return s;
	}
	

	public String makeSummeryConfirm() throws IOException  {
		
		 
		String summerytable  = "<table border='0' cellpadding='0' cellspacing='0' width='70%' class='summerytable' align='center'>";
		try {
			
			System.out.println( "Making summery confirm...."  );
			String resptext = "";
			Boolean secondarycurrency = false;
			 
			String[] curselected =  getRequest().getParameterValues("sec_cur");
			String defcuramt = getRequest().getParameter("defloadamt");
			String sec_curreq = getRequest().getParameter("sec_curreq");
			
			System.out.println( "defcuramt__" + defcuramt);
			
			System.out.println( "sec_curreq__" + sec_curreq);
			
			System.out.println( "curselected_" + curselected.length);
			
			String feedesc = "";
			String instid = comInstId();
			Map  curcodeamtmap = new HashMap();
			
		}catch(Exception e ){} 
		 
		return "confirmactivate_home";

		//getResponse().getWriter().write("prito");
		
		/*
		 String x = getRequest().getParameter("mymap");
		 System.out.println( "x__" + x ); 
		try {
			JSONObject myObject = new JSONObject();
			 System.out.println( "myObject__" + myObject );
		} catch (Exception e) {

			e.printStackTrace();
		}
		 */
		
	}
	
	
	
	
	
	
	

	
 
	public String checkMinMaxLoadAmt( String instid, String curcode, String actualamt, JdbcTemplate jdbctemplate){
			try{ 
				
				/*String getamtqry = "SELECT MIN_AMOUNT, MAX_AMOUNT FROM IFP_BIN_CURRENCY WHERE "
				+ "INST_ID='"+instid+"' AND CUR_CODE='"+curcode+"'";
				System.out.println("getamtqry__" + getamtqry);
				List getamtlist = jdbctemplate.queryForList(getamtqry);*/
				
				///by gowtham
				String getamtqry = "SELECT MIN_AMOUNT, MAX_AMOUNT FROM IFP_BIN_CURRENCY WHERE "
						+ "INST_ID=? AND CUR_CODE=? ";
						System.out.println("getamtqry__" + getamtqry);
						List getamtlist = jdbctemplate.queryForList(getamtqry,new Object[]{instid,curcode});
				
				System.out.println( "getamtlist__" + getamtlist );
				if( !getamtlist.isEmpty() ){
					 
					Iterator itr = getamtlist.iterator();
					int actamt = Integer.parseInt(actualamt);
					String ermsg = "";
					String curdesc = commondesc.getCurDesc(curcode, jdbctemplate);
					System.out.println( "curdesc__" + curdesc );
					while( itr.hasNext() ){
						Map mp = (Map)itr.next();
						
						int minamt =  Integer.parseInt(mp.get("MIN_AMOUNT").toString() );
						int  maxamt =Integer.parseInt(mp.get("MAX_AMOUNT").toString() );
						
						System.out.println( "actual- "+actamt+"---minimum-"+minamt+"----maximum-"+maxamt);
						if( actamt < minamt  || actamt > maxamt ){
							ermsg = curdesc + " Amount Between Minimum  " + minamt + " And Maximum  " + maxamt;
							return ermsg;
						} 
					}
					 return "SUC";
				} 
		}catch (Exception e) {
			e.printStackTrace();
		}
			return "SUC";
	}
	
	
	public String confirmActivate(){
		System.out.println("confirm card activation....");
		return "confirmactivate_home";
	}
	    

	 

	public String activateAuthInstCardHome( ) {
		String instid = comInstId();
		 
		HttpSession session = getRequest().getSession();
		
		if(activatebeans.type != null)
		{
			session.setAttribute("PROCESS_TYPE", activatebeans.type);
		} 
		
		/*String waitactiveauthqry = "SELECT PRODUCT_CODE FROM IFP_CARD_ACTIVE "
		+ "WHERE INST_ID='"+instid+"' AND MKCK_STATUS='M' AND PROCESS_FLAG='I' GROUP BY PRODUCT_CODE ";
		System.out.println( "waitactiveauthqry__" + waitactiveauthqry );
		List waitactiveauthlist = jdbctemplate.queryForList(waitactiveauthqry);*/
		
		//by gowtham
		String waitactiveauthqry = "SELECT PRODUCT_CODE FROM IFP_CARD_ACTIVE "
				+ "WHERE INST_ID=? AND MKCK_STATUS=? AND PROCESS_FLAG=? GROUP BY PRODUCT_CODE ";
				System.out.println( "waitactiveauthqry__" + waitactiveauthqry );
				List waitactiveauthlist = jdbctemplate.queryForList(waitactiveauthqry,new Object[]{instid,"M","I"});
		
		if( !waitactiveauthlist.isEmpty() ){
			
			activatebeans.branchlist =  this.commondesc.generateBranchList(instid, jdbctemplate);
			activatebeans.setBranchlist(activatebeans.branchlist);
			 
			ListIterator itr = waitactiveauthlist.listIterator();
			while ( itr.hasNext() ){
				
				Map mp = (Map)itr.next();
				String prodcode = (String)mp.get("PRODUCT_CODE");
				String proddesc = commondesc.getProductdesc(instid, prodcode,jdbctemplate);
				mp.put("PROD_DESC", proddesc);
				itr.remove();
				itr.add(mp);
				
			}
			
			activatebeans.setProdlist(waitactiveauthlist);
		}else{
			session.setAttribute("curerr", "E");
			session.setAttribute("curmsg", " NO CARDS Waiting  FOR AUTHORIZATION "  );
			return "required_home";
		}
		
		return "activate_auth_home";
	}
	
	List activeauthlist;
	
	public List getActiveauthlist() {
		return activeauthlist;
	}

	public void setActiveauthlist(List activeauthlist) {
		this.activeauthlist = activeauthlist;
	}

	public String activateAuthList( ) throws Exception{
		HttpSession session = getRequest().getSession();
		String table = (String)session.getAttribute("PROCESS_TYPE");
		System.out.println("Table TYpe==> "+table);
		String table_type="INST_CARD_PROCESS";
		if(table.equals("PERSONAL"))
		{
			table_type = "PERS_CARD_PROCESS";
		} 
		
		try {
			String instid = comInstId();
		 
			String prodcodecode = getRequest().getParameter("product");
			
			/*String waitactiveauthqry = "SELECT CARD_NO, ENTITY_CODE, PRODUCT_CODE, SUB_PROD_ID, ACTIVE_FEE_SWIPE, "
			+ "CONVERTION_FEE_SWIPE, to_char(ACTIVATED_DATE, 'dd-mon-yyyy') AS ACTIVATED_DATE, USER_CODE "
			+ "FROM IFP_CARD_ACTIVE WHERE INST_ID='"+instid+"' AND MKCK_STATUS='M' AND PROCESS_FLAG='I' "
			+ "AND PRODUCT_CODE='"+prodcodecode+"'";
			System.out.println( "waitactiveauthqry__" + waitactiveauthqry );
			List waitactiveauthlist = jdbctemplate.queryForList(waitactiveauthqry);*/
			
			///by gowtham
			String waitactiveauthqry = "SELECT CARD_NO, ENTITY_CODE, PRODUCT_CODE, SUB_PROD_ID, ACTIVE_FEE_SWIPE, "
					+ "CONVERTION_FEE_SWIPE, to_char(ACTIVATED_DATE, 'dd-mon-yyyy') AS ACTIVATED_DATE, USER_CODE "
					+ "FROM IFP_CARD_ACTIVE WHERE INST_ID=? AND MKCK_STATUS=? AND PROCESS_FLAG=?  AND PRODUCT_CODE=?";
					System.out.println( "waitactiveauthqry__" + waitactiveauthqry );
					List waitactiveauthlist = jdbctemplate.queryForList(waitactiveauthqry,new Object[]{instid,"M","I",prodcodecode});
			
			if( !waitactiveauthlist.isEmpty() ){
				 
				ListIterator itrcardno = waitactiveauthlist.listIterator();
				while ( itrcardno.hasNext() ){
					
					Map mp = (Map)itrcardno.next();
					String prodcode = (String)mp.get("PRODUCT_CODE");
					String proddesc = commondesc.getProductdesc(instid,prodcode,jdbctemplate);
					mp.put("PROD_DESC", proddesc);
					
					String subproduct = (String)mp.get("SUB_PROD_ID");
					String subprod_desc = commondesc.getSubProductdesc(instid, subproduct,jdbctemplate);
					mp.put("SUB_PROD_DESC", subprod_desc);
					
					String cardno =  (String)mp.get("CARD_NO");
					
					
					String entitycode = (String)mp.get("ENTITY_CODE");
					
					String user_code = (String)mp.get("USER_CODE");
					String user_desc = commondesc.getUserName(instid, user_code, jdbctemplate);
					mp.put("USER_DESC", user_desc);
					
					
					String feecode = commondesc.getFeeCode(instid,subproduct,jdbctemplate);
					if ( feecode.equals("NOREC")){
						 
						session.setAttribute("curerr", "E");
						session.setAttribute("curmsg", "NO FEE CODE FOUND FOR THE SUBPRODUCT " + subproduct  );
						this.activateAuthInstCardHome();
					} 
					 
					String loadamount = "";
					String default_currency = commondesc.fchDefaultCurrency(instid, cardno, table_type, jdbctemplate);
					
					/*String activeamtqry = "SELECT TO_CHAR( SUM(BASE_CUR_CONVERTED_AMT) ) AS TOTAL_LOADAMT ,"
					+ " BASE_CURRENCY  FROM IFP_CARD_ACTIVE_AMT WHERE INST_ID='"+instid+"' AND "
					+ "CARD_NO='"+cardno+"' AND ACTION_CODE='"+entitycode+"' GROUP BY BASE_CURRENCY";
					System.out.println( "activeamtqry__" + activeamtqry );
					List activeamt = jdbctemplate.queryForList(activeamtqry);*/
					
					///BY GOWTHAM
					String activeamtqry = "SELECT TO_CHAR( SUM(BASE_CUR_CONVERTED_AMT) ) AS TOTAL_LOADAMT ,"
							+ " BASE_CURRENCY  FROM IFP_CARD_ACTIVE_AMT WHERE INST_ID=? AND "
							+ "CARD_NO=? AND ACTION_CODE=? GROUP BY BASE_CURRENCY";
							System.out.println( "activeamtqry__" + activeamtqry );
							List activeamt = jdbctemplate.queryForList(activeamtqry,new Object[]{instid,cardno,entitycode});
					
					if( !activeamt.isEmpty()){
						ListIterator amtitr = activeamt.listIterator();
						
						while( amtitr.hasNext() ){
							Map amtmp = (Map)amtitr.next();
							
							String entity_code = (String)amtmp.get("ENTITY_CODE");
							String instbasecur = (String)amtmp.get("BASE_CURRENCY");
							loadamount = instbasecur+" "+(String)amtmp.get("TOTAL_LOADAMT"); 
						}
							 
					} 
						System.out.println( "finalLoadAmount__" + loadamount );
						mp.put("LOADING_AMOUNT",loadamount );
					} 
				 
				}  
				
				setActiveauthlist(waitactiveauthlist); 
			
		} catch (Exception e) {
			 
			e.printStackTrace();
		}
		
		
		return "activate_auth_list";
	}
	
	
	public String displayDetailedAmount() throws IOException{
		String cardno = getRequest().getParameter("cardno");
		activatebeans.setCardno(cardno);
		return "activate_amount_list";
	}
	
	public void dispLoadAmt() throws IOException{
	 
		String instid = comInstId();  
		String processtype = "INSTANT"; 
		String card_no = getRequest().getParameter("cardno");
		 
		/*String feedispqry = "SELECT CUR_CODE, LOAD_AMT, ENTITY_CODE, AFFECT_CODE FROM IFP_CARD_ACTIVE_AMT "
		+ "WHERE INST_ID='"+instid+"' AND CARD_NO='"+card_no+"' AND ACTION_CODE='"+CARDACTIVECODE+"'";
		List feelist = jdbctemplate.queryForList(feedispqry);*/

		///by gowtham
		String feedispqry = "SELECT CUR_CODE, LOAD_AMT, ENTITY_CODE, AFFECT_CODE FROM IFP_CARD_ACTIVE_AMT "
				+ "WHERE INST_ID=?  AND CARD_NO=? AND ACTION_CODE=? ";
				List feelist = jdbctemplate.queryForList(feedispqry,new Object[]{instid,card_no,CARDACTIVECODE});
		
		if( !feelist.isEmpty() ){
			Iterator itr = feelist.iterator();
			String table = "<table border='1' cellpadding='0' cellspacing='0' width='90%'>";
			while( itr.hasNext() ){
				Map mp = (Map) itr.next();
				String currencycode = (String) mp.get("CUR_CODE");
				String loadingamt = (String) mp.get("LOAD_AMT");
				String entitycode = (String) mp.get("ENTITY_CODE");
				String txncode = (String) mp.get("AFFECT_CODE");
				
				String currencydesc = commondesc.getCurSymbol(currencycode, jdbctemplate);
						
				if( entitycode.equals(LOAD_ENTITY)){
					table += "<tr> <td> LOADING AMOUNT  </td> <td class='amount' > : "  + currencydesc + " "+ loadingamt + " </td> </tr>";
				}else if ( entitycode.equals(FEE_ENTITY)){
					String actiondesc = commondesc.getTransactionDesc(instid, txncode, jdbctemplate); 
					 
					table += "<tr> <td> FEE AMOUNT - " + actiondesc + "   </td> <td class='amount'> : "  + currencydesc + " " + loadingamt +" </td> </tr>";
					
				}else{
					table += "<tr> <td> INVALID ENTITY . ENTITY COMES OTHER THAN LOADING AMT OR FEE AMOUNT </td> </tr>";
				} 
			}
			table += "</table>" ;
			getResponse().getWriter().write(table);
		}
	} 

	public String getOrderDetails(String instid, String orderrefno, String defcuramt, JdbcTemplate jdbctemplate) throws IOException{
		String summtable = "<table border='0' cellpadding='0' cellspacing='0' width='70%' >";
	
	//	String instid = comInstId();
	//	JdbcTemplate jdbctemplate = new JdbcTemplate(dataSource);
	//	String orderrefno = getRequest().getParameter("orderrefno");
		
		try { 
			
			/*String orderdataqry = "SELECT CARD_QUANTITY, EMBOSSING_NAME, PRODUCT_CODE, SUB_PROD_ID FROM "
			+ "INST_CARD_ORDER WHERE INST_ID='"+instid+"' AND ORDER_REF_NO='"+orderrefno+"'";
			System.out.println("orderdataqry_"+orderdataqry);
			List orderdatalist = jdbctemplate.queryForList(orderdataqry);*/
			
			//by gowtham
			String orderdataqry = "SELECT CARD_QUANTITY, EMBOSSING_NAME, PRODUCT_CODE, SUB_PROD_ID FROM "
					+ "INST_CARD_ORDER WHERE INST_ID=?  AND ORDER_REF_NO=? ";
					System.out.println("orderdataqry_"+orderdataqry);
					List orderdatalist = jdbctemplate.queryForList(orderdataqry,new Object[]{instid,orderrefno});
			
			
			String prodname = null;
			String subprodname = null;
			if( !orderdatalist.isEmpty()){
				Iterator itr = orderdatalist.iterator();
				while( itr.hasNext() ){
					Map mp = (Map) itr.next();
					String cardquantity = (String)mp.get("CARD_QUANTITY");
					String embname = (String)mp.get("CARD_QUANTITY");
					prodname = commondesc.getProductdesc(instid,  (String)mp.get("PRODUCT_CODE"),jdbctemplate);
					subprodname = commondesc.getSubProductdesc(instid,  (String)mp.get("SUB_PROD_ID"),jdbctemplate); 
					
					//summtable += " <tr><td   > PRODUCT  :<span  class='textcolor'> "+prodname+ " </span> </td> <td> SUB PRODUCT <span  class='textcolor'> : "+subprodname+ " </span>  </td> </tr>";
					 
					int cardcnt = activatedao.getReadyForActivationCardCount(instid, orderrefno, "05", jdbctemplate);
					
					if( cardcnt < Integer.parseInt(cardquantity) ){  
						summtable += " <tr><td colspan='4' class='textcolor' >THIS ORDER GENERATED WITH  <b>  "+cardquantity+"</b> CARDS. <b> "+cardcnt+"</b>  CARDS READY FOR ACTIVATION.  &nbsp; </td></tr>";
						
					}else{
						summtable += " <tr><td colspan='4' class='textcolor' >ORDER CONTAINS  <b>: "+cardcnt + " CARDS </b> </td></tr>";
					} 
					 
					summtable += "<input type='hidden' name='orderrefbean' id='orderrefbean' value='"+orderrefno+"' />";
					summtable += "<input type='hidden' name='defloadamt' id='defloadamt' value='"+defcuramt+"' />";
					summtable += " <tr><td colspan='4' style='text-align:center' > <input type='submit' onclick='return showActiveCardList()' name='continue' id='continue'  value='Continue'  />";
					summtable += " 	 &nbsp; <input type='button' name='cancel' id='cancel'  value='Cancel' class='cancelbtn' onclick='return confirmCancel()'/>  </td></tr>";
					 
				} 
				
			}else{
				return "NO RECORDS FOUND FOR THE GIVEN ORDER REF NO : " + orderrefno;
			}
		} catch (Exception e) {
			getResponse().getWriter().write(e.toString());
			e.printStackTrace();
		}
		 
		summtable += "</table>";
		return summtable;
		
	}
	
	String defaultloadamtbean;
	public String getDefaultloadamtbean() {
		return defaultloadamtbean;
	}

	public void setDefaultloadamtbean(String defaultloadamtbean) {
		this.defaultloadamtbean = defaultloadamtbean;
	}
	
	 
	public String activateOrderedCardList()  { // CARD ACTIVATION PROCESS ..............
		
		HttpSession session = getRequest().getSession();
		
		String instid = comInstId();
		String usercode = comUserCode();
		
		 
		 
		String orderrefno = getRequest().getParameter("ORDERREFNO");
		String defaultloadamt = getRequest().getParameter("DEFAULTLOADINGAMT");
		System.out.println("active card default loading amt :  " + defaultloadamt);
		setDefaultloadamtbean(defaultloadamt);
		List activatecardlist = activateByOrder(instid, orderrefno, jdbctemplate); 
		activatebeans.setOrderedcardlist(activatecardlist);
		
		String curlist[] = getRequest().getParameterValues("selectedcurrency");
		System.out.println("curlist__" + curlist);
		String curamountfld = getRequest().getParameter("LOADCURVALUES");
		String feeamountfld = getRequest().getParameter("FEEVALUES");
		int n;  
		
		activatebeans.setCuramountfieldbean(curamountfld);
		activatebeans.setFeeamountbean(feeamountfld);
		return "activecard_list";
	} 
	
	private List activateByOrder(String instid, String orderrefno,	JdbcTemplate jdbctemplate) {
		List orderedcardlist = null;
		try {
			
			/*String orderlistqry = "SELECT CARD_NO, to_char(RECV_DATE, 'dd-mon-yyyy' ) as RECV_DATE FROM INST_CARD_PROCESS WHERE INST_ID='"+instid+"' AND ORDER_REF_NO='"+orderrefno+"' AND CARD_STATUS='05'";
			System.out.println("orderlistqry__"+orderlistqry); 
			orderedcardlist = jdbctemplate.queryForList(orderlistqry);
			*/
			
			///by gowtham
			String orderlistqry = "SELECT CARD_NO, to_char(RECV_DATE, 'dd-mon-yyyy' ) as RECV_DATE FROM"
			+ " INST_CARD_PROCESS WHERE INST_ID=? AND ORDER_REF_NO=? AND CARD_STATUS=? ";
			System.out.println("orderlistqry__"+orderlistqry); 
			orderedcardlist = jdbctemplate.queryForList(orderlistqry,new Object[]{instid,orderrefno,"05"});
			
			
		} catch (DataAccessException e) { 
			e.printStackTrace();
		}
		return orderedcardlist;
		
	}
	
} // class end
 
 
