package com.ifp.util;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifg.Config.padss.PadssSecurity;
import com.ifp.Action.BaseAction;

public class FindcardAction080719 extends BaseAction
{
	
	private static final long serialVersionUID = 1L;
	 
	private List findcardreceivedlist;
	CommonDesc commondesc = new CommonDesc();
	
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

	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	public List getFindcardreceivedlist() {
		return findcardreceivedlist;
	}

	public void setFindcardreceivedlist(List findcardreceivedlist) {
		this.findcardreceivedlist = findcardreceivedlist;
	}
	
	 
	public String findCard()
	{
		trace("Clicked find card");
		return "findcard";
	}
	
	public String comInstId(){
		HttpSession session = getRequest().getSession();
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	} 
	
	
	public void searchCard() throws Exception
	{
		HttpSession session=getRequest().getSession();
		String instid = comInstId();
	 
		String card_no = getRequest().getParameter("cardnumber");

		try
		{
			String instantcount = "select count(*) from INST_CARD_PROCESS where INST_ID='"+instid+"' and CARD_NO = '"+card_no+"'";
			//String instantstatus = "select * from INST_CARD_ORDER where INST_ID='"+instid+"' and ORDER_REF_NO like '%"+orderref+"%'";
			System.out.println("instantcount==> "+instantcount);
			int result_inst_qury = jdbctemplate.queryForInt(instantcount);
			System.out.println("result_inst_qury==> "+result_inst_qury);
			
			String persoalizecount = "select count(*) from PERS_CARD_PROCESS where INST_ID='"+instid+"' and CARD_NO = '"+card_no+"'";
			//String instantstatus = "select * from INST_CARD_ORDER where INST_ID='"+instid+"' and ORDER_REF_NO like '%"+orderref+"%'";
			System.out.println("persoalizecount==> "+persoalizecount);
			int result_personalize_qury = jdbctemplate.queryForInt(persoalizecount);
			System.out.println("result_personalize_qury==> "+result_personalize_qury);
			
			
			String carddesc = null;
			if(card_no != ""&& !card_no.equals("null"))
			{
				if(result_inst_qury!=0)
				{
					String cardstatus=this.cardstatus_inst(instid, card_no, jdbctemplate);
					System.out.println(" cardstatus==> "+cardstatus);
					String cur_desc ="SELECT CUR_STATUS_DESC from IFP_CARD_STATUS_DESC where CARD_FLAG='I' and STATUS_CODE ='"+cardstatus+"'";
					carddesc=(String)jdbctemplate.queryForObject(cur_desc,String.class);
					System.out.println("carddesc Instants==> "+carddesc);
					String status="<table class='CSSTableGenerator'><tr><td style='text-align:center;color:black;border-width:0px 1px 1px 0px;'>Card Number</td><td style='text-align:center;color:black'>Status</td></tr><tr><td style='text-align:center;color:black;font-size: medium'>"+card_no+"</td><td style='text-align:center;color:black;font-size: medium'>"+carddesc+"</td></tr></table>";
					getResponse().getWriter().write(status);
				}
				else if(result_personalize_qury!=0)
				{
					String cardstatus=this.cardstatus_personalize(instid, card_no, jdbctemplate);
					System.out.println(" cardstatus==> "+cardstatus);
					String cur_desc ="SELECT CUR_STATUS_DESC from IFP_CARD_STATUS_DESC where CARD_FLAG='I' and STATUS_CODE ='"+cardstatus+"'";
					carddesc=(String)jdbctemplate.queryForObject(cur_desc,String.class);
					System.out.println("carddesc personalized==> "+carddesc);
					String status="<table class='CSSTableGenerator'><tr><td style='text-align:center;color:black;border-width:0px 1px 1px 0px;'>Card Number</td><td style='text-align:center;color:black'>Status</td></tr><tr><td style='text-align:center;color:black;font-size: medium'>"+card_no+"</td><td style='text-align:center;color:black;font-size: medium'>"+carddesc+"</td></tr></table>";
					getResponse().getWriter().write(status);
				}
				else
				{
					String status="<table><tr><td style='text-align:center;color:red;border-width:0px 1px 1px 0px;;font-size: medium'>No results Found</td></tr></table>";
					getResponse().getWriter().write(status);
					System.out.println("No results Found");
			
				}
				
			}
			else{
				String status="<table><tr><td style='text-align:center;color:red;border-width:0px 1px 1px 0px;;font-size: medium'>No results Found</td></tr></table>";
				getResponse().getWriter().write(status);
				System.out.println("No results Found");
			}
	
		}
		catch(Exception e)
		{
			System.out.println("Error while getting values     "+e.getMessage());
			String error = "<table><tr><td style='text-align:center;color:red;border-width:0px 1px 1px 0px;;font-size: medium'>Error :"+e.getMessage()+"</td></tr></table>";
			getResponse().getWriter().write(error);
		}

	}
	
	/*
	public String findcardstatus() throws Exception
	{
		trace("Finding Cards....");
		enctrace("Finding Cards....");
		
		String instid = comInstId();
		HttpSession session = getRequest().getSession();
		JdbcTemplate jdbctemplate = new JdbcTemplate(dataSource); 
		List receivedlist;
		String orderref = commondesc.escSql( getRequest().getParameter("orderref").trim() );
		System.out.println("orderref : " + orderref );
		
		String cardnum = commondesc.escSql( getRequest().getParameter("cardnum").trim() );
		System.out.println("cardnum : " + cardnum );
		String cardno = "",orderrefnum= "",cardtypeid= "",bin= "",product_code= "",mkrckr= "",branchcode= "",enbname= "",chackerid= "",makerid= "",status_code="";
		String orderstatus ="",orderstatusDesc="",cardstatus="",result="",qury_card="",statusorder="NO", mkckstatus = null;
			if( orderref != "" &&  !orderref.equals("null"))
			{
				qury_card = "and ORDER_REF_NO LIKE '%"+orderref+"'";
				receivedlist = this.getCardsFromOrder(instid,orderref,"INST_CARD_PROCESS",qury_card,jdbctemplate);
				if(receivedlist.isEmpty()){
					receivedlist = this.getCardsFromOrder(instid,orderref,"PERS_CARD_PROCESS",qury_card,jdbctemplate);
					if(receivedlist.isEmpty()){
						receivedlist = this.getCardsFromOrder(instid,orderref,"CARD_PRODUCTION",qury_card,jdbctemplate);
						if( receivedlist.isEmpty()){
							receivedlist = this.getCardsFromOrder(instid,orderref,"IFC_CARD_PRODUCTION",qury_card,jdbctemplate);
						}
						statusorder="YES";
						if(receivedlist.isEmpty()){
							session.setAttribute("curerr", "E");
							session.setAttribute("curmsg"," NO RECORDS FOUND " );
						    return "required_home";
					  }
					}
				}
				ListIterator custitr = receivedlist.listIterator();
			    while( custitr.hasNext() ){
				Map cmp = (Map)custitr.next(); 
				cardno = (String)cmp.get("CARD_NO");  
				orderrefnum = (String)cmp.get("ORDER_REF_NO");
				product_code = commondesc.getProductdesc(instid,(String)cmp.get("PRODUCT_CODE"),jdbctemplate) ;
				cardstatus = (String)cmp.get("CARD_STATUS");
				if(statusorder.equals("YES")){
					orderstatusDesc=commondesc.getCardStatusDesc(instid, cardstatus, jdbctemplate);
				}else{
					orderstatusDesc = this.getorderstatusDesc(cardstatus, jdbctemplate);
				}
				if( orderstatusDesc == null ){
					addActionError("Card Process Flow Not Configured");
					return "required_home";
				}
				enbname = (String)cmp.get("EMB_NAME"); 
				cmp.put("PRODUCT_DESC",product_code);
				cmp.put("STATUS_DESC",orderstatusDesc);
				custitr.remove();
				custitr.add(cmp);
				System.out.println("PRINT LIST ---- >" +cmp);
			}
			
			setFindcardreceivedlist(receivedlist); 
			return "findcardnext";
		    }
		  else if(cardnum != "" &&  !cardnum.equals("null")){
			  
			  qury_card= " and CARD_NO LIKE '%"+cardnum+"'";
			  receivedlist = this.getCardsFromOrder(instid, cardnum, "INST_CARD_PROCESS",qury_card, jdbctemplate);
			  if(receivedlist.isEmpty()){
				  receivedlist = this.getCardsFromOrder(instid, cardnum, "PERS_CARD_PROCESS",qury_card, jdbctemplate);
				  if(receivedlist.isEmpty()){
					  receivedlist = this.getCardsFromOrder(instid, cardnum, "CARD_PRODUCTION",qury_card, jdbctemplate);
					  if( receivedlist.isEmpty() ){
						  receivedlist = this.getCardsFromOrder(instid, cardnum, "IFC_CARD_PRODUCTION",qury_card, jdbctemplate);
					  }
					  statusorder="YES";
					  if(receivedlist.isEmpty()){
						  session.setAttribute("curerr", "E");
							session.setAttribute("curmsg","NO RECORDS FOUND " ); 
					  }
				  }
			  }
			    ListIterator custitr = receivedlist.listIterator();
			    while( custitr.hasNext() ){
				Map cmp = (Map)custitr.next(); 
				cardno = (String)cmp.get("CARD_NO");  
				orderrefnum = (String)cmp.get("ORDER_REF_NO");
				product_code = commondesc.getProductdesc(instid,(String)cmp.get("PRODUCT_CODE"),jdbctemplate) ;
				cardstatus = (String)cmp.get("CARD_STATUS"); 
				if(statusorder.equals("YES")){
					orderstatusDesc=commondesc.getCardStatusDesc(instid, cardstatus, jdbctemplate);
				}else{
					orderstatusDesc = this.getorderstatusDesc(cardstatus, jdbctemplate);
				}
				enbname = (String)cmp.get("EMB_NAME");
				cmp.put("STATUS_DESC",orderstatusDesc);
				cmp.put("PRODUCT_DESC",product_code);
				custitr.remove();
				custitr.add(cmp);
				System.out.println("PRINT LIST ---- >" +cmp);
			}	
			   
			    setFindcardreceivedlist(receivedlist); 
			    return "findcardnext";  
		  }else{
			  session.setAttribute("curerr", "E");
			  session.setAttribute("curmsg"," NO RECORDS FOUND " );
			  return "required_home";
		  }
	}
	*/
	
	
	
	
	/*public String findcardstatus() throws Exception
	{
		trace("Finding Cards....");
		enctrace("Finding Cards....");
		
		String instid = comInstId();
		HttpSession session = getRequest().getSession();
		 
		List receivedlist;
		String orderref = commondesc.escSql( getRequest().getParameter("orderref").trim() );
		System.out.println("orderref : " + orderref );
		String instanttable = "PERS_CARD_PROCESS";
		String personaltable = "PERS_CARD_PROCESS";  
		String productiontable = "PERS_CARD_PROCESS";
		String cardnum = commondesc.escSql( getRequest().getParameter("cardnum").trim() );
		System.out.println("cardnum : " + cardnum );
		String cardno = "",orderrefnum= "",cardtypeid= "",bin= "",product_code= "",mkrckr= "",branchcode= "",chackerid= "",makerid= "",status_code="",prename="",issuedtype="";
		String orderstatus ="",orderstatusDesc="",cardstatus="",result="",qury_card="",statusorder="NO";
			if( orderref != "" &&  !orderref.equals("null"))
			{
				qury_card = "and ORDER_REF_NO LIKE '%"+orderref+"'";
				
				receivedlist = this.getCardsFromOrder(instid,orderref,instanttable,qury_card,jdbctemplate);
				if(receivedlist.isEmpty()){
					
					receivedlist = this.getCardsFromOrder(instid,orderref,personaltable,qury_card,jdbctemplate);
					if(receivedlist.isEmpty()){ 
						receivedlist = this.getCardsFromOrder(instid,orderref,productiontable,qury_card,jdbctemplate);
						statusorder="YES";
						if(receivedlist.isEmpty()){
							session.setAttribute("curerr", "E");
							session.setAttribute("curmsg"," NO RECORDS FOUND " );
						    return "required_home";
					  } 
					}else{
						issuedtype = "Personalized";
					}
				}else{
					issuedtype = "Instant";
				}
				ListIterator custitr = receivedlist.listIterator();
			    while( custitr.hasNext() ){
				Map cmp = (Map)custitr.next(); 
				cardno = (String)cmp.get("CARD_NO");  
				orderrefnum = (String)cmp.get("ORDER_REF_NO");
				product_code = commondesc.getProductdesc(instid,(String)cmp.get("PRODUCT_CODE"),jdbctemplate) ;
				cardstatus = (String)cmp.get("CARD_STATUS");
				if(statusorder.equals("YES")){
					orderstatusDesc=commondesc.getCardStatusDesc(instid, cardstatus, jdbctemplate);
				}else{
					orderstatusDesc = this.getorderstatusDesc(cardstatus, jdbctemplate);
				}
				
				prename = this.getPreFile(instid, orderrefnum, "ORDERBASED", jdbctemplate);
				
				if( issuedtype != null ){
					issuedtype = this.getCardIssudType(instid, orderrefnum, "ORDERBASED", jdbctemplate);
				}
				cmp.put("ISSUEDTYPE",issuedtype);
				cmp.put("PRENAME",prename);
				//(String)cmp.get("EMB_NAME"); 
				cmp.put("PRODUCT_DESC",product_code);
				cmp.put("STATUS_DESC",orderstatusDesc);
				custitr.remove();
				custitr.add(cmp);
				System.out.println("PRINT LIST ---- >" +cmp);
			}
			
			setFindcardreceivedlist(receivedlist); 
			return "findcardnext";
		    }
		  else if(cardnum != "" &&  !cardnum.equals("null")){
			  String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
				PadssSecurity padsssec = new PadssSecurity();
				String eDMK = "",eDPK="";
				try {
					List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
					Iterator secitr = secList.iterator();
						while(secitr.hasNext())
						{
							Map map = (Map) secitr.next(); 
							eDMK = ((String)map.get("DMK"));
							eDPK = ((String)map.get("DPK"));
						} 
				} catch (Exception e1) {
					e1.printStackTrace();
					return "required_home";
				}
				String echn = padsssec.getECHN(eDMK, eDPK, cardnum);
			  qury_card= " and CARD_NO LIKE '%"+echn+"'";
			  receivedlist = this.getCardsFromOrder(instid, cardnum, instanttable,qury_card, jdbctemplate);
			  if(receivedlist.isEmpty()){
				  
				  receivedlist = this.getCardsFromOrder(instid, cardnum, personaltable,qury_card, jdbctemplate);
				  if(receivedlist.isEmpty()){
					  receivedlist = this.getCardsFromOrder(instid, cardnum, productiontable,qury_card, jdbctemplate);
					  statusorder="YES";
					  if(receivedlist.isEmpty()){
						  session.setAttribute("curerr", "E");
							session.setAttribute("curmsg","NO RECORDS FOUND " ); 
					  } 
				  }else{
					  issuedtype = "Personalized";
				  }
			  }else{
				  issuedtype = "Instant";
			  }
			    ListIterator custitr = receivedlist.listIterator();
			    while( custitr.hasNext() ){
				Map cmp = (Map)custitr.next(); 
				cardno = (String)cmp.get("CARD_NO");  
				orderrefnum = (String)cmp.get("ORDER_REF_NO");
				product_code = commondesc.getProductdesc(instid,(String)cmp.get("PRODUCT_CODE"),jdbctemplate) ;
				cardstatus = (String)cmp.get("CARD_STATUS"); 
				if(statusorder.equals("YES")){
					//orderstatusDesc=commondesc.getCardStatusDesc(instid, cardstatus, jdbctemplate);
				}else{
					//orderstatusDesc = this.getorderstatusDesc(cardstatus, jdbctemplate);
				}
				prename = this.getPreFile(instid, cardno, "CARDBASED", jdbctemplate); 
				//(String)cmp.get("EMB_NAME");
				
				if( issuedtype != null ){
					issuedtype = this.getCardIssudType(instid, cardno, "CARDBASED", jdbctemplate);
				}
				cmp.put("ISSUEDTYPE",issuedtype);
				cmp.put("STATUS_DESC",orderstatusDesc);
				cmp.put("PRODUCT_DESC",product_code);
				cmp.put("PRENAME",prename);
				custitr.remove();
				custitr.add(cmp);
				System.out.println("PRINT LIST ---- >" +cmp);
			}	
			   
			    setFindcardreceivedlist(receivedlist); 
			    return "findcardnext";  
		  }else{
			  session.setAttribute("curerr", "E");
			  session.setAttribute("curmsg"," NO RECORDS FOUND " );
			  return "required_home";
		  }
	}*/
	
	public String findcardstatus() throws Exception{
		String acctno = getRequest().getParameter("acctno");
		String processcount = "",findcarddtlsqry = "", clearchn = "";
		
		HttpSession session = getRequest().getSession(); 
		String instid = comInstId();
		String keyid = commondesc.getSecurityKeyid(instid, jdbctemplate);
		PadssSecurity padsssec = new PadssSecurity();
		String eDMK = "",eDPK="";
		try {
			List secList = commondesc.getPADSSDetailById(keyid, jdbctemplate);
			Iterator secitr = secList.iterator();
				while(secitr.hasNext())
				{
					Map map = (Map) secitr.next(); 
					eDMK = ((String)map.get("DMK"));
					//eDPK = ((String)map.get("DPK"));
				} 
		} catch (Exception e1) {
			e1.printStackTrace();
			return "required_home";
		}
		Properties props=getCommonDescProperty();
		String EDPK = props.getProperty("EDPK");
		String processcountqry = "SELECT COUNT(1) AS CNT FROM PERS_CARD_ORDER WHERE ACCOUNT_NO='"+acctno+"'";
		processcount = (String) jdbctemplate.queryForObject(processcountqry, String.class);
		if(!"0".equalsIgnoreCase(processcount)){
			findcarddtlsqry = "SELECT B.CARD_NO,DECODE(A.ORDER_TYPE,'B','BULK REGISTRATION','P','NORMAL REGISTRATION') AS REGTYPE, B.CIN,B.MCARD_NO,NVL(B.PRE_FILE,'--') AS PREFILE,B.EMB_NAME,TO_CHAR(B.REG_DATE,'MM-DD-YY') AS REG_DATE,TO_CHAR(B.PRE_DATE,'DD-MM-YY') AS PRE_DATE,TO_CHAR(B.PIN_DATE,'DD-MM-YY') AS PIN_DATE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE PRODUCT_CODE=B.PRODUCT_CODE) AS PRODUCT, CASE WHEN B.CAF_REC_STATUS='S' THEN 'REISSUED' WHEN B.CAF_REC_STATUS='A' THEN 'NEW CARD' WHEN B.CAF_REC_STATUS='AC' THEN 'ADD ON CARD' WHEN B.CAF_REC_STATUS='DE' THEN 'DAMAGE WITH EXPIRY DATE' WHEN B.CAF_REC_STATUS='D' THEN 'DAMAGE' WHEN B.CAF_REC_STATUS='BN' THEN 'RENEWED' WHEN B.CAF_REC_STATUS='R' THEN 'REPIN' END AS ISSUEDTYPE, CASE WHEN B.CARD_STATUS='01' AND B.MKCK_STATUS='P' THEN 'Waiting For Security Data Generation' WHEN B.CARD_STATUS='01' AND B.MKCK_STATUS='M' THEN 'Waiting For Card Generation Authorization' WHEN B.CARD_STATUS='02' AND B.MKCK_STATUS='M' THEN 'Waiting For Security Data Authorization' WHEN B.CARD_STATUS='02' AND B.MKCK_STATUS='P' THEN 'Waiting For PRE Generation' WHEN B.CARD_STATUS='03' AND B.MKCK_STATUS='M' THEN 'Waiting For PRE Authorization' WHEN B.CARD_STATUS='03' AND B.MKCK_STATUS='P' THEN 'Waiting For Receive Card' WHEN B.CARD_STATUS='04' AND B.MKCK_STATUS='M' THEN 'Waiting For Receive Card Authorization' WHEN B.CARD_STATUS='04' AND B.MKCK_STATUS='P' THEN 'Waiting For Card Issuance' WHEN B.CARD_STATUS='05' AND B.MKCK_STATUS='M' THEN 'Waiting For Card Issuance Authorization' END AS CARD_STATUS FROM PERS_CARD_ORDER A,PERS_CARD_PROCESS B,CUSTOMERINFO C WHERE A.ORDER_REF_NO=B.ORDER_REF_NO AND b.CIN=c.CIN AND A.CIN=C.CIN AND B.ACCT_NO='"+acctno+"'";
		}else{
			findcarddtlsqry = "SELECT  B.CARD_NO,'MIGRATED' AS REGTYPE,B.CIN,B.MCARD_NO,NVL(B.PRE_FILE,'--') AS PREFILE,B.EMB_NAME,TO_CHAR(B.REG_DATE,'MM-DD-YY') AS REG_DATE,TO_CHAR(B.PRE_DATE,'DD-MM-YY') AS PRE_DATE,TO_CHAR(B.PIN_DATE,'DD-MM-YY') AS PIN_DATE,(SELECT CARD_TYPE_NAME FROM PRODUCT_MASTER WHERE PRODUCT_CODE=B.PRODUCT_CODE) AS PRODUCT, CASE WHEN B.CAF_REC_STATUS='S' THEN 'REISSUED' WHEN B.CAF_REC_STATUS='A' THEN 'NEW CARD' WHEN B.CAF_REC_STATUS='AC' THEN 'ADD ON CARD' WHEN B.CAF_REC_STATUS='DE' THEN 'DAMAGE WITH EXPIRY DATE' WHEN B.CAF_REC_STATUS='D' THEN 'DAMAGE' WHEN B.CAF_REC_STATUS='BN' THEN 'RENEWED' WHEN B.CAF_REC_STATUS='R' THEN 'REPIN' END AS ISSUEDTYPE, CASE WHEN B.CARD_STATUS='01' AND B.MKCK_STATUS='P' THEN 'Waiting For Security Data Generation' WHEN B.CARD_STATUS='01' AND B.MKCK_STATUS='M' THEN 'Waiting For Card Generation Authorization' WHEN B.CARD_STATUS='02' AND B.MKCK_STATUS='M' THEN 'Waiting For Security Data Authorization' WHEN B.CARD_STATUS='02' AND B.MKCK_STATUS='P' THEN 'Waiting For PRE Generation' WHEN B.CARD_STATUS='03' AND B.MKCK_STATUS='M' THEN 'Waiting For PRE Authorization' WHEN B.CARD_STATUS='03' AND B.MKCK_STATUS='P' THEN 'Waiting For Receive Card' WHEN B.CARD_STATUS='04' AND B.MKCK_STATUS='M' THEN 'Waiting For Receive Card Authorization' WHEN B.CARD_STATUS='04' AND B.MKCK_STATUS='P' THEN 'Waiting For Card Issuance' WHEN B.CARD_STATUS='05' AND B.MKCK_STATUS='M' THEN 'Waiting For Card Issuance Authorization' END AS CARD_STATUS FROM PERS_CARD_PROCESS B,CUSTOMERINFO C WHERE b.CIN=c.CIN  AND B.ACCT_NO='"+acctno+"'";
		}
		enctrace("findcarddtlsqry--> "+findcarddtlsqry);
		System.out.println(findcarddtlsqry);
		List<Map<String,Object>> list = jdbctemplate.queryForList(findcarddtlsqry);
		for(int j=0;j<list.size();j++)
		{
			String CDPK=padsssec.decryptDPK(eDMK, EDPK);
			clearchn =  padsssec.getCHN(CDPK, (String) list.get(j).get("CARD_NO"));
			list.get(j).put("MCARD_NO", clearchn);
			
			String prefile = (String)list.get(j).get("PREFILE");
			if(prefile != null){
				if(prefile.contains("_")){
					String[] namesplit = prefile.split("_");
					String filedate = namesplit[2];
					String filetime = namesplit[3];
					filedate = filedate.substring(0,4) + filedate.substring(6,8);
					prefile = filedate +"-"+filetime;
				}else{
					prefile = "MIGRATED";
				}
			}else{prefile = "--";}
			list.get(j).put("PREFILE", prefile);
		}
		carddetailslist = list;
		return "findcardview";
	}
	
	
	public String getCardIssudType(String instid, String value, String key, JdbcTemplate jdbctemplate ) throws Exception {
		String issuedtype = null;
		trace("Got the key : " + key );
		String issuercond = "";
		if( key.equals("ORDERBASED")){
			issuercond = " AND ORDER_REF_NO='"+value+"'";
		}else{
			issuercond = " AND CARD_NO='"+value+"'";
		}
		
		try{
			String issuedtypeqry = " SELECT ORDER_FLAG FROM CARD_PRODUCTION WHERE INST_ID='"+instid+"' "+issuercond+" AND ROWNUM<=1 ";
			enctrace("issuedtypeqry :"+issuedtypeqry);
			issuedtype = (String)jdbctemplate.queryForObject(issuedtypeqry, String.class);
			if( issuedtype.equals("I")){
				return "Instant";
			}else if( issuedtype.equals("P")){
				return "Personalized";
			}else{
				return "--";
			}
		}catch(Exception e ){
			return "--";
		}
		
	}
	
	
	public String getPreFile(String instid, String value, String key, JdbcTemplate jdbctemplate ) throws Exception {
		String prefile = null;
		
		String precondition = "";
		String prefilequery = "";
		if( key.equals("ORDERBASED")){
			precondition = " AND ORDER_REF_NO='"+value+"'";
		}else{
			precondition = " AND CARD_NO='"+value+"'";
		}
		
		try{
			prefilequery = "SELECT PRE_FILE FROM CARD_PRODUCTION  WHERE INST_ID='"+instid+"' "+precondition+" AND ROWNUM<=1 ";
			enctrace("prefilequery1 : "+ prefilequery);
			prefile = (String)jdbctemplate.queryForObject(prefilequery, String.class); 
			enctrace("prefile2 : "+prefile.charAt(0));
		}catch(Exception e){
			trace("Exception e :"+ e.getMessage());
			try{
				prefilequery = "SELECT PRE_NAME FROM PERS_PRE_DATA  WHERE INST_ID='"+instid+"' "+precondition+" AND ROWNUM<=1";
				enctrace("prefilequery2 : "+ prefilequery);
				prefile = (String)jdbctemplate.queryForObject(prefilequery, String.class);
				enctrace("prefile2 :  "+prefile.charAt(0));
			}catch( Exception e1 ){
				trace("Exception e1 :"+ e1.getMessage());
				try {
					prefilequery = "SELECT PRE_NAME FROM PERS_PRE_DATA_HIST  WHERE INST_ID='"+instid+"' "+precondition+" AND ROWNUM<=1";
					enctrace("prefilequery3 : "+ prefilequery);
					prefile = (String)jdbctemplate.queryForObject(prefilequery, String.class);
					enctrace("prefile3 : "+prefile.charAt(0));
				}catch(Exception e2){ 
					trace("Exception e2 :"+ e2.getMessage());
						try{
							prefilequery = "SELECT PRE_NAME FROM INST_PRE_DATA  WHERE INST_ID='"+instid+"' "+precondition+" AND ROWNUM<=1";
							enctrace("prefilequery4 : "+ prefilequery);
							prefile = (String)jdbctemplate.queryForObject(prefilequery, String.class);
							enctrace("prefile4 : "+prefile.charAt(0));
						}catch(Exception e3){
							trace("Exception e3 :"+ e3.getMessage());
						   try{
								prefilequery = "SELECT PRE_NAME FROM INST_PRE_DATA_HIST  WHERE INST_ID='"+instid+"' "+precondition+" AND ROWNUM<=1 ";
								enctrace("prefilequery5 : "+ prefilequery);
								prefile = (String)jdbctemplate.queryForObject(prefilequery, String.class);
								enctrace("prefile4 :  "+prefile.charAt(0));
						}catch(Exception e5){}
					}
				}
			}
		}
		
		return prefile;
		
	}
	
	public String getorderstatusDesc(String orderstatus, JdbcTemplate jdbctemplate){
		String cardstus = null;
		String cur_desc ="SELECT CUR_STATUS_DESC from IFP_CARD_STATUS_DESC where CARD_FLAG='I' and STATUS_CODE ='"+orderstatus+"'";
		enctrace (" cur_desc  : " + cur_desc );
		try{
			cardstus = (String) jdbctemplate.queryForObject(cur_desc, String.class);
		}catch(EmptyResultDataAccessException e){}
		
		return cardstus;
	}
	
	public List getCardsFromOrder(String instid, String orderrefno,String tablename,String qury_card, JdbcTemplate jdbctemplate){ // order type is "INSTANT" OR "PERSONAL"
		List chnlist = null;
		String query="select CARD_NO,CARD_STATUS,STATUS_CODE,ORDER_REF_NO, CARD_TYPE_ID, BIN, PRODUCT_CODE,  MKCK_STATUS, BRANCH_CODE,  EMB_NAME, CHECKER_ID, to_char(MAKER_DATE, 'dd-mon-yyyy') as ORDERED_DATE, MAKER_ID from "+tablename+" where INST_ID='"+instid+"' "+qury_card+"";
		chnlist = jdbctemplate.queryForList(query);
		System.out.println("query : " + query);
		return chnlist;
	}
	
	public String getorderstatus(String instid,String tablename,String orderrefnum, JdbcTemplate jdbctemplate)
	{
		String order_status = "SELECT ORDER_STATUS from "+tablename+" where INST_ID='"+instid+"' and ORDER_REF_NO ='"+orderrefnum+"'";
		System.out.println("order_status changed tablename	---"+order_status);
		String orderstus = (String) jdbctemplate.queryForObject(order_status, String.class);
		System.out.println("query : " + orderstus);
		return orderstus;
	}
	
	public String cardstatus_inst(String instid,String card_no,JdbcTemplate jdbctemplate)
	{
		String card_inst = null;
		String card_status = "select card_status from INST_CARD_PROCESS where INST_ID='"+instid+"' and ( CARD_NO = '"+card_no+"' OR ORG_CHN = '"+card_no+"')";
		card_inst = (String) jdbctemplate.queryForObject(card_status,String.class);
		System.out.println("card_status for instant===> "+card_inst);
		return card_inst;
		
	}
	public String cardstatus_personalize(String instid,String card_no,JdbcTemplate jdbctemplate)
	{
		String card_personalize = null;
		String card_status = "select card_status from PERS_CARD_PROCESS where INST_ID='"+instid+"' and ( CARD_NO = '"+card_no+"' OR ORG_CHN = '"+card_no+"')";
		card_personalize = (String) jdbctemplate.queryForObject(card_status,String.class);
		System.out.println("card_status for personalized===> "+card_personalize);
		return card_personalize;
	}
	
	public List carddetailslist;


	public List getCarddetailslist() {
		return carddetailslist;
	}

	public void setCarddetailslist(List carddetailslist) {
		this.carddetailslist = carddetailslist;
	}

}
