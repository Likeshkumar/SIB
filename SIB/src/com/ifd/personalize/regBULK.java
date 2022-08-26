package com.ifd.personalize;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.io.FileUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;
import com.ifp.beans.AuditBeans;
import com.ifp.util.CommonDesc;
import com.ifp.util.IfpTransObj;

public class regBULK extends BaseAction{
	
	 DebitBulkCustomerRegisterDAO dao = new DebitBulkCustomerRegisterDAO();
	
	CommonDesc commondesc = new CommonDesc();
	AuditBeans auditbean = new AuditBeans();
	
	public AuditBeans getAuditbean() {
		return auditbean;
	}  

	public void setAuditbean(AuditBeans auditbean) {
		this.auditbean = auditbean;
	}
	public CommonDesc getCommondesc() {
		return commondesc;
	}

	public void setCommondesc(CommonDesc commondesc) {
		this.commondesc = commondesc;
	}

	
	private List uploadStatus;
	 
	private PlatformTransactionManager  txManager  = new DataSourceTransactionManager();

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}
	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}
	public String comInstId( HttpSession session ){ 
		String instid = (String)session.getAttribute("Instname"); 
		return instid;
	}
	public String comUserId( HttpSession session ){
		String userid = (String)session.getAttribute("USERID"); 
		return userid;
	}
	JdbcTemplate jdbctemplate = new JdbcTemplate();
	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}
	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}
	
	
	
	private File uploadFile;
	private String uploadFileContentType;
	private String uploadFileFileName;
	   
	   
	   public String checkLength(String line,int lineno)
	   {
	 	  String emsg = "";
	 	 // System.out.println("Lineno:{"+lineno+"}Length{"+line.length()+"}");
	 	  if(line.length()!=332)
	 	  {
	 		  emsg="Length Not Matched in Line No :"+lineno; 
	 	  }
	 	  else
	 	  {
	 		  emsg = String.valueOf(line.length());
	 	  }
	 	  return emsg;
	   }
	   
	     
	   
	   public String validateField(int checkCorrect,String columnfld,String value,int lineno,int length,String mand,String valType,String columnDesc,List itlist)
	 	  { String emsg = "";
	 	
	 	  
	 	  if(columnfld.equals("ACTCODE")){
	 		 System.err.println(validateAlphabet(value));
	 		  if(validateAlphabet(value)==false)
					 	  {
	 			  		  emsg =  "ERROR-"+value+"-"+columnDesc+" AlphaBet at Line no:"+lineno; 
					 	  }else
					 	  {
					 		 emsg= value;
					 	  }     
	 		  }
	 	
	 	  if(columnfld.equals("CARDPRODUCT")){
	 		 if(validateInDb(itlist,value)==false)      
			 {
				 emsg = "ERROR-"+value+"-"+columnfld+" Not Valid at:"+lineno;
			 }
			 	else    
				  {         
					  emsg = value;              
				  }
	 	  }      
	 	  
	 	   	if(columnfld.equals("PBRANCHCODE")){  
	 	   	 if(validateInDb(itlist,value)==false)      
			 {
	 	   		 System.err.println("columnfld:::"+value);
				 emsg = "ERROR-"+value+"-"+columnfld+" BRANCH no:"+lineno;
			 }
			 	else    
				  {   
					  emsg = value;              
				  }
			 }
	 	 
		
		 if(columnfld.equals("PACCTNO")){
			 if(validateNumeric(value)==false)      
			 {
				 emsg = "ERROR-"+value+"-"+columnfld+" Should be Numeric :"+lineno;
			 }
		     else if(validateInDb(itlist,value)==true)      
			 {
				 emsg = "ERROR-"+value+"-"+columnfld+" Already Exist no:"+lineno;
			 }
			 
			 	else    
				  {   
					  emsg = value;              
				  }
	 		 
		 }
		    
		 
		 if(columnfld.equals("CUSTOMERID")){
			 if(validateAlphaNumeric(value)==false)      
			 {emsg = "ERROR-"+value+"-"+columnfld+" Should be Alpha-Numeric :"+lineno;}
		     else if(validateInDb(itlist,value)==true)      
			 {emsg = "ERROR-"+value+"-"+columnfld+" Already Exist no:"+lineno;}
			 else{emsg = value;}
		 }
		 
		  
		 if(columnfld.equals("PACCTTYPE") ){
			 if(validateInDb(itlist,value)==false)      
			 {emsg = "ERROR-"+value+"-"+columnfld+" BRANCH no:"+lineno;}
			 else { emsg = value;}
		 }
		 
		 
		 if(columnfld.equals("PACCTSUBTYPE") ){
			 if(validateInDb(itlist,value)==false)      
			 {
				 emsg="ERROR-"+value+"-"+columnfld+" Not Valid in Line no:"+lineno; 
			 }
			 	else    
				  {   
					  emsg = value;              
				  }
		 }
		 
		 if(columnfld.equals("PACCTCCY")){
			 if(validateInDb(itlist,value)==false)      
			 {
				 emsg="ERROR-"+value+"-"+columnfld+" Not Valid in Line no:"+lineno; 
			 }
			 	else    
				  {   
					  emsg = value;              
				  }
		 }
		 
		 
		 if(columnfld.equals("ENCODINGNAME")){
			 
			 System.out.println("enc"+(validateAlphabet(value.replaceAll(" ", ""))==false));
		 if(validateAlphabet(value.replaceAll(" ", ""))==false)
		 {
			 emsg = "ERROR-"+value+"-"+columnfld+" Shuld Be AlphaBet no:"+lineno;
		 }
		 	else
			  {   
				  emsg = value;              
			  }
		 }
		 
		 if(columnfld.equals("EMBOSINGNAME")){
			 System.err.println("enc"+(validateAlphabet(value.replaceAll(" ", ""))==false));
			 if(validateAlphabet(value.replaceAll(" ", ""))==false)
			 {
				 emsg = "ERROR-"+value+"-"+columnfld+" Shuld Be AlphaBet no:"+lineno;
			 }
			 	else
				  {   
					  emsg = value;    
				  }
			 }
		 
		 if(columnfld.equals("GENDER")){
			 if((value.equals("M")) || (value.equals("F")) || (value.equals("O")))
			  { 
				 emsg=value;
				 //emsg = "ERROR-"+value+"-"+columnfld+" Should be in M or F or O no:"+lineno;
				// emsg = value;    
			  }else
			  {    
				  emsg = "ERROR-"+value+"-"+columnfld+" Should be in M or F or O no:"+lineno;
				  //emsg=value; 
			  }
		 }
		 
		 
		 if(columnfld.equals("cardcollectbranch")){
	 		 if(validateInDb(itlist,value)==false)      
			 {
				 emsg = "ERROR-"+value+"-"+columnfld+" Not Valid at:"+lineno;
			 }
			 	else    
				  {         
					  emsg = value;              
				  }
	 	  }    
		 if(columnfld.equals("MAIL1") ){ emsg=value; }    
		 if(columnfld.equals("MAIL2") ){ emsg=value; }
		 if(columnfld.equals("MAIL3") ){ emsg=value; }
		 if(columnfld.equals("MAIL4") ){ emsg=value; }
		 if(columnfld.equals("MAIL5") ){ emsg=value; }   
		 if(columnfld.equals("EMAIL") ){ emsg=value; }    
		 if(columnfld.equals("P_PO_BOX") ){
			 if(value.trim().length() == 0)
			 {emsg = "ERROR-"+value+"-"+columnfld+" the value is null:"+lineno;}
			 else{ emsg=value; }
		 }
		 if(columnfld.equals("P_HOUSE_NO") )
		 {
			 if(value.trim().length() == 0)
			 {emsg = "ERROR-"+value+"-"+columnfld+" the value is null:"+lineno;}
			 else{ emsg=value; }
		 }

		 
		 if(columnfld.equals("OFFICEPHONE")){
			 if(validateNumeric(value.replaceAll(" ", ""))==false)
			 { emsg = "ERROR-"+value+"-"+columnfld+" Shuld Be Numeric no:"+lineno;}
			 	else{emsg = value;}
			 }
		 if(columnfld.equals("HOMEPHONE")){
			 if(validateNumeric(value.replaceAll(" ", ""))==false)
			 { emsg = "ERROR-"+value+"-"+columnfld+" Shuld Be Numeric no:"+lineno;}
			 	else{emsg = value;}
			 }   
		 if(columnfld.equals("MOBILENO")){
			 
			 if(validateNumeric(value.replaceAll(" ", ""))==false)
			 { emsg = "ERROR-"+value+"-"+columnfld+" Shuld Be Numeric no:"+lineno;}
			 else if (value.trim().length() != 10)
			 { emsg = "ERROR-"+value+"-"+columnfld+" Shuld Be value or 10 digit value:"+lineno;}
			 
				else{emsg = value;}
		 }
			/* if(validateNumeric(value.replaceAll(" ", ""))==false)
			 { emsg = "ERROR-"+value+"-"+columnfld+" Shuld Be Numeric no:"+lineno;}
			 else if(value.equals("          "))
			 {emsg = "ERROR-"+value+"-"+columnfld+" Mobile no column is mandatory:"+lineno;}
			 	else{emsg = value;}
			 }*/
		 if(columnfld.equals("APPDATE")){
			 if(!value.trim().equals("28/03/2014"))
			  {
				  emsg="ERROR-"+value+"-"+columnfld+" Not Matched no:"+lineno; 
			  }else
			  {
				  emsg = value;
			  }
		 }
		 if(columnfld.equals("REGDATE")){
			 if(validateDateFormat(value.replaceAll(" ", ""))==false)
			 { emsg = "ERROR-"+value+"-"+columnfld+" Date Format Not Matched at:"+lineno;}
			 	else{emsg = value;}
			 }
		 if(columnfld.equals("REGID")){
			 if(validateNumeric(value.replaceAll(" ", ""))==false)
			 { emsg = "ERROR-"+value+"-"+columnfld+" Date Format Not Matched at:"+lineno;}
			 	else{emsg = value;}
			 }
		 if(columnfld.equals("KYCDOCTYPE")){
			 
			 if((value.equals("001")) || (value.equals("999"))  || (value.equals("002")) )
			  { 
				 emsg=value;
				 //emsg = "ERROR-"+value+"-"+columnfld+" Should be in M or F or O no:"+lineno;
				// emsg = value;    
			  }else
			  {    
				  emsg = "ERROR-"+value+"-"+columnfld+" SHOULD BE 001 / 999 / 002:"+lineno;
				  //emsg=value; 
			  }
			 
			/* if(validateNumeric(value.replaceAll(" ", ""))==false)
			 { emsg = "ERROR-"+value+"-"+columnfld+" Date Format Not Matched at:"+lineno;}
			 	else{emsg = value;}*/
			 }
		 if(columnfld.equals("KYCDOCID")){
			 if(validateAlphaNumeric(value.replaceAll(" ", ""))==false)
			 { emsg = "ERROR-"+value+"-"+columnfld+" Date Format Not Matched at:"+lineno;}
			 	else{emsg = value;}    
			 }
		 
		 if(columnfld.equals("MARITAL_STATUS")){
			 if((value.equals("M")) || (value.equals("U")) )
			  { 
				 emsg=value;
				 //emsg = "ERROR-"+value+"-"+columnfld+" Should be in M or F or O no:"+lineno;
				// emsg = value;    
			  }else
			  {    
				  emsg = "ERROR-"+value+"-"+columnfld+"  SHOULD BE M/U:"+lineno;
				  //emsg=value; 
			  }
		 }
		
				
		 System.err.println("columnfld::"+columnfld+"------"+emsg);
		        
		 return emsg;
	   }
	   public boolean validateNumeric(String val)
	   {
	 	  boolean result =true;
	 	  //System.out.println(val);  
	 	  for(int i=1;i<val.length();i++)
	 	  {
	 		  //System.out.println(i);
	 		  if(!Character.isDigit(val.charAt(i)))
	 		  {
	 			  return false;}
	 		  
	 	  }
	 	 return result;
	   }
	   
	   
	   public boolean validateDateFormat(String val)
	   {boolean checkformat;
	   if (val.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})"))
		    checkformat=true;
		else
		   checkformat=false;
	   	return checkformat;
	   }
	   
	   public boolean validateAlphabet(String val)
	   {
		  boolean result =true;
	 	  for(int i=0;i<val.length();i++)
	 	  {
	 		  if(!Character.isAlphabetic(val.charAt(i)))
	 		  {result= false;}
	 	  }
	 	 return result;
	   }    
	   
	   
	   public boolean validateInDb(List<String> valList,String value)
	   {
		  boolean result =false;
	 	  for(int i=0;i<valList.size();i++)
	 	  {   
	 		  if(value.equals(valList.get(i)))
	 		  {return true;}
	 	  }
	 	 return result;
	   }    
	   
	   
	   public boolean validateAlphaNumeric(String val)
	   {
	 	  boolean result =true;
	 	  //System.out.println(val);
	 	  for(int i=0;i<val.length();i++)
	 	  {
	 		
	 		  if(!Character.isLetter(val.charAt(i)) && !Character.isDigit(val.charAt(i)))
	 		  {
	 			
	 			  result= false;}
	 		  
	 	  }
	 	 return result;
	   }
	  
	   
	   public int readFile(HttpSession session,File path,String fileName,FileWriter succwriter,FileWriter failwriter,String usercode,JdbcTemplate jdbcTemplate) throws IOException
	    {
		  System.out.println("test read");
		   int finalresult=-1;
		   Scanner sc = new Scanner(path);
		    try {     
		    	int iterat = 0;int successRecord=0;int failrecord=0;
		    	 String resoncode="";int insertFailStatus = 0 ,insertFailCount = 0, rejectCount=0;
	               
	               int orderinsert=0,updateref = 0,accinfo=0,custinfo=0;
	               int orderinsertCount=0,accinfoCount=0,custinfoCount=0;
	               
		    	String values = "",vvalues="",columnName = "",instid=comInstId(session),rejectreason="",rejectQry="";
		    	   
		    	String actionCode="",actionCodeFFld="",actionCodeToFld="",valactionCode="";
	            String subProduct = "",subProductFFld="",subProductToFld="",valsubProduct="";
	            String pbranchCode="",pbranchCodeFFld="",pbranchCodeToFld="",valpbranchCode="";
	            String pAccountNo="",pAccountNoFFld="",pAccountNoToFld="",valpAccountNo="";
	            String pAccountType="",pAccountTypeFFld="",pAccountTypeToFld="",valpAccountType="";
	            String pAccountSubType = "",pAccountSubTypeFFld="",pAccountSubTypeToFld="",valpAccountSubType="";
	            String pAccountCcy="",pAccountCcyFFld="",pAccountCcyToFld="",valpAccountCcy="";
	            String customerId="",customerIdFFld="",customerIdToFld="",valcustomerId="";
	            String encodingName="",encodingNameFFld="",encodingNameToFld="",valencodingName="";
	            String embosingName="",embosingNameFFld="",embosingNameToFld="",valembosingName="";
	            String gender="",genderFFld="",genderToFld="",valgender="";
	            String mail1="",mail1FFld="",mail1ToFld="",valmail1="";
	            String mail2="",mail2FFld="",mail2ToFld="",valmail2="";
	            String mail3="",mail3FFld="",mail3ToFld="",valmail3="";
	            String mail4="",mail4FFld="",mail4ToFld="",valmail4="";
	            String mail5="",mail5FFld="",mail5ToFld="",valmail5="";
	            String officePhone="",officePhoneFFld="",officePhoneToFld="",valofficePhone="";
	            String homePhone="",homePhoneFFld="",homePhoneToFld="",valhomePhone="";
	            String mobilePhone="",mobilePhoneFFld="",mobilePhoneToFld="",valmobilePhone="";
	            String email="",emailFFld="",emailToFld="",valemail="";
	            String regid="",regidFFld="",regidToFld="",valregid="";
	            String regdate="",regdateFFld="",regdateToFld="",valregdate="";
	            String kycdoctype="",kycdoctypeFFld="",kycdoctypeToFld="",valkycdoctype="";
	            String kycdocid="",kycdocidFFld="",kycdocidToFld="",valkycdocid="";
	            String maritalstatus="",msFFld="",msToFld="",valms="";
	            String cardcollectbranch="",valcardcollectbranch="",valcardcollectbranchFFld="",valcardcollectbranchToFld="";
	            String pP_PO_BOX="",pP_PO_BOXFFld="",pP_PO_BOXToFld="",valP_PO_BOX="";
	            String pP_HOUSE_NO="",pP_HOUSE_NOFFld="",pP_HOUSE_NOToFld="",valP_HOUSE_NO="";
	            
	            List<String> cardProductList = dao.getSubProductList(instid, jdbcTemplate);
	            List<String> getBrancList = dao.getBranchList(instid, jdbcTemplate);
	            System.out.println("getBrancList");
	            List<String> AccountList = dao.getAccountList(instid, jdbcTemplate);
	            List<String> AccountTypeList = dao.getAccountTypeList(instid, jdbcTemplate);
	            List<String> AccountSubTypeList = dao.getAccountSubTypeList(instid, jdbcTemplate);
	            List<String> AccountCcyList = dao.getAccountCcyList(jdbcTemplate);
	            List<String> CustIdList = dao.getCustIdList(instid,jdbcTemplate);
	            

	            
	            System.out.println("getBrancList222");
	            List getFields = dao.getBulkConfigList("ORBL","TEST",jdbcTemplate);
	               Iterator itr = getFields.iterator();
	               while(itr.hasNext()) {      
						 Map map = (Map)itr.next();
						 columnName = (String)map.get("COLUMN_NAME");
						 
					if(columnName.equals("ACTCODE")){		 
						actionCodeFFld =  (String)map.get("FROM_FLD");
						actionCodeToFld =  (String)map.get("TO_FLD");
					}		 
						 
				if(columnName.equals("CARDPRODUCT")){		 
						subProductFFld =  (String)map.get("FROM_FLD");
						subProductToFld =  (String)map.get("TO_FLD");
				}
				if(columnName.equals("PBRANCHCODE")){		 
					pbranchCodeFFld =  (String)map.get("FROM_FLD");
					pbranchCodeToFld =  (String)map.get("TO_FLD");
				}	 
				
	            if(columnName.equals("PACCTNO")){		 
	            		pAccountNoFFld =  map.get("FROM_FLD").toString();
	            		pAccountNoToFld =  map.get("TO_FLD").toString();   
	            		
					}	
	            if(columnName.equals("PACCTTYPE")){		 
	            	pAccountTypeFFld =  (String)map.get("FROM_FLD");
	            	pAccountTypeToFld =  (String)map.get("TO_FLD");
	            	
				}        
	            
	            if(columnName.equals("PACCTSUBTYPE")){		 
	            	pAccountSubTypeFFld =  (String)map.get("FROM_FLD");
	            	pAccountSubTypeToFld =  (String)map.get("TO_FLD");
	            	  
				}  
	            
	            if(columnName.equals("PACCTCCY")){		 
	            	pAccountCcyFFld =  (String)map.get("FROM_FLD");
	            	pAccountCcyToFld =  (String)map.get("TO_FLD");
				}
	            if(columnName.equals("CUSTOMERID")){		 
	            	customerIdFFld =  (String)map.get("FROM_FLD");
	            	customerIdToFld =  (String)map.get("TO_FLD");
				}
	            
	            if(columnName.equals("ENCODINGNAME")){		 
	            	encodingNameFFld =  (String)map.get("FROM_FLD");
	            	encodingNameToFld =  (String)map.get("TO_FLD");
				}
	            
	            if(columnName.equals("EMBOSINGNAME")){		 
	            	embosingNameFFld =  (String)map.get("FROM_FLD");
	            	embosingNameToFld =  (String)map.get("TO_FLD");
				}
	            if(columnName.equals("GENDER")){		 
	            	genderFFld =  (String)map.get("FROM_FLD");
	            	genderToFld =  (String)map.get("TO_FLD");
				}
	            if(columnName.equals("MAIL1")){		 
	            	mail1FFld =  (String)map.get("FROM_FLD");
	            	mail1ToFld =  (String)map.get("TO_FLD");
				}
	            if(columnName.equals("MAIL2")){		 
	            	mail2FFld =  (String)map.get("FROM_FLD");
	            	mail2ToFld =  (String)map.get("TO_FLD");
				}
	            if(columnName.equals("MAIL3")){		 
	            	mail3FFld =  (String)map.get("FROM_FLD");
	            	mail3ToFld =  (String)map.get("TO_FLD");
				}
	            if(columnName.equals("MAIL4")){		 
	            	mail4FFld =  (String)map.get("FROM_FLD");
	            	mail4ToFld =  (String)map.get("TO_FLD");
				}
	            if(columnName.equals("MAIL5")){		 
	            	mail5FFld =  (String)map.get("FROM_FLD");
	            	mail5ToFld =  (String)map.get("TO_FLD");
				}
	            if(columnName.equals("OFFICEPHONE")){		 
	            	officePhoneFFld =  (String)map.get("FROM_FLD");
	            	officePhoneToFld =  (String)map.get("TO_FLD");
				}
	            if(columnName.equals("HOMEPHONE")){		 
	            	homePhoneFFld =  (String)map.get("FROM_FLD");
	            	homePhoneToFld =  (String)map.get("TO_FLD");
				}
	            if(columnName.equals("MOBILENO")){		 
	            	mobilePhoneFFld =  (String)map.get("FROM_FLD");
	            	mobilePhoneToFld =  (String)map.get("TO_FLD");
				}
	            
	            if(columnName.equals("EMAIL")){		 
	            	emailFFld =  (String)map.get("FROM_FLD");
	            	emailToFld =  (String)map.get("TO_FLD");
				}
	            if(columnName.equals("REGDATE")){		 
	            	regdateFFld =  (String)map.get("FROM_FLD");
	            	regdateToFld =  (String)map.get("TO_FLD");
				}
	            if(columnName.equals("REGID")){		 
	            	regidFFld =  (String)map.get("FROM_FLD");
	            	regidToFld =  (String)map.get("TO_FLD");
				}
	            if(columnName.equals("KYCDOCTYPE")){		 
	            	kycdoctypeFFld =  (String)map.get("FROM_FLD");   
	            	kycdoctypeToFld =  (String)map.get("TO_FLD");
				}
	            if(columnName.equals("KYCDOCID")){		 
	            	kycdocidFFld =  (String)map.get("FROM_FLD");
	            	kycdocidToFld =  (String)map.get("TO_FLD");
				}
	            if(columnName.equals("MARITAL_STATUS")){		 
	            	msFFld =  (String)map.get("FROM_FLD");
	            	msToFld =  (String)map.get("TO_FLD");
				}
	            if(columnName.equals("CARDCOLLECTBRANCH"))
	            {
	            	valcardcollectbranchFFld =  (String)map.get("FROM_FLD");
	            	valcardcollectbranchToFld =  (String)map.get("TO_FLD");
	            }
	            if(columnName.equals("P_PO_BOX")){		 
	            	pP_PO_BOXFFld =  (String)map.get("FROM_FLD");
	            	pP_PO_BOXToFld =  (String)map.get("TO_FLD");
				}
	            if(columnName.equals("P_HOUSE_NO")){		 
	            	pP_HOUSE_NOFFld =  (String)map.get("FROM_FLD");
	            	pP_HOUSE_NOToFld =  (String)map.get("TO_FLD");
				}
	            
		    }      
	               String maxLineNo = dao.getMAxLineNo(instid, jdbcTemplate);
	               
	               
	             
	               
	              
					
	            do{     
	               String next = sc.nextLine();
	               iterat++;   
	               System.out.println(""+maxLineNo);
	               System.out.println(""+next.length());
if(Integer.parseInt(maxLineNo)==next.length())             
{          
	               actionCode = next.substring(Integer.parseInt(actionCodeFFld)-1, Integer.parseInt(actionCodeToFld));
	               subProduct = next.substring(Integer.parseInt(subProductFFld)-1, Integer.parseInt(subProductToFld));
	               pbranchCode = next.substring(Integer.parseInt(pbranchCodeFFld)-1, Integer.parseInt(pbranchCodeToFld));
	               pAccountNo = next.substring(Integer.parseInt(pAccountNoFFld)-1, Integer.parseInt(pAccountNoToFld));
	               pAccountType = next.substring(Integer.parseInt(pAccountTypeFFld)-1, Integer.parseInt(pAccountTypeToFld));
	               pAccountSubType = next.substring(Integer.parseInt(pAccountSubTypeFFld)-1, Integer.parseInt(pAccountSubTypeToFld));
	               pAccountCcy = next.substring(Integer.parseInt(pAccountCcyFFld)-1, Integer.parseInt(pAccountCcyToFld));
	               customerId = next.substring(Integer.parseInt(customerIdFFld)-1, Integer.parseInt(customerIdToFld));
	               encodingName = next.substring(Integer.parseInt(encodingNameFFld)-1, Integer.parseInt(encodingNameToFld));
	               embosingName = next.substring(Integer.parseInt(embosingNameFFld)-1, Integer.parseInt(embosingNameToFld));
	               gender = next.substring(Integer.parseInt(genderFFld)-1, Integer.parseInt(genderToFld));
	               mail1 = next.substring(Integer.parseInt(mail1FFld)-1, Integer.parseInt(mail1ToFld));
	               mail2 = next.substring(Integer.parseInt(mail2FFld)-1, Integer.parseInt(mail2ToFld));
	               mail3 = next.substring(Integer.parseInt(mail3FFld)-1, Integer.parseInt(mail3ToFld));
	               mail4 = next.substring(Integer.parseInt(mail4FFld)-1, Integer.parseInt(mail4ToFld));
	               mail5 = next.substring(Integer.parseInt(mail5FFld)-1, Integer.parseInt(mail5ToFld));
	               officePhone = next.substring(Integer.parseInt(officePhoneFFld)-1, Integer.parseInt(officePhoneToFld));
	               homePhone = next.substring(Integer.parseInt(homePhoneFFld)-1, Integer.parseInt(homePhoneToFld));
	               mobilePhone = next.substring(Integer.parseInt(mobilePhoneFFld)-1, Integer.parseInt(mobilePhoneToFld));
	               email = next.substring(Integer.parseInt(emailFFld)-1, Integer.parseInt(emailToFld));
	               regdate = next.substring(Integer.parseInt(regdateFFld)-1, Integer.parseInt(regdateToFld));
	               regid = next.substring(Integer.parseInt(regidFFld)-1, Integer.parseInt(regidToFld));
	               kycdoctype = next.substring(Integer.parseInt(kycdoctypeFFld)-1, Integer.parseInt(kycdoctypeToFld));
	               kycdocid = next.substring(Integer.parseInt(kycdocidFFld)-1, Integer.parseInt(kycdocidToFld));
	               maritalstatus = next.substring(Integer.parseInt(msFFld)-1, Integer.parseInt(msToFld));
	               //
	               
	               System.out.println(valcardcollectbranchFFld);
	               System.out.println(valcardcollectbranchToFld);
	               cardcollectbranch = next.substring(Integer.parseInt(valcardcollectbranchFFld)-1, Integer.parseInt(valcardcollectbranchToFld));
	               System.out.println(cardcollectbranch);
	               
		            pP_PO_BOX = next.substring(Integer.parseInt(pP_PO_BOXFFld)-1, Integer.parseInt(pP_PO_BOXToFld));
		            System.out.println(pP_PO_BOX);
		            pP_HOUSE_NO = next.substring(Integer.parseInt(pP_HOUSE_NOFFld)-1, Integer.parseInt(pP_HOUSE_NOToFld));
		            System.out.println(pP_HOUSE_NO);
	               
	               System.out.println("actionCode:::::"+actionCode);      
				    
	               if("S".equalsIgnoreCase(pAccountType.trim())){
						pAccountType = "10";
					}else if("U".equalsIgnoreCase(pAccountType.trim())){
						pAccountType = "20";
					}
	               
				valactionCode = validateField(9,"ACTCODE", actionCode, iterat, actionCode.length(), "", "", "action Code",null);
				valsubProduct = validateField(9,"CARDPRODUCT", subProduct, iterat, subProduct.length(), "", "", "Product Code",cardProductList);    
				valpbranchCode = validateField(9,"PBRANCHCODE", pbranchCode, iterat, pbranchCode.length(), "", "", "BranchCode",getBrancList);
				valpAccountNo = validateField(9,"PACCTNO", pAccountNo, iterat, pAccountNo.length(), "", "", "Primary Account No",AccountList);
				valpAccountType = validateField(9,"PACCTTYPE", pAccountType, iterat, pAccountType.length(), "", "", "Primary Account No",AccountTypeList);
				valpAccountSubType = validateField(9,"PACCTSUBTYPE", pAccountSubType, iterat, pAccountSubType.length(), "", "", "Primary Account No",AccountSubTypeList);
				valpAccountCcy = validateField(9,"PACCTCCY", pAccountCcy, iterat, pAccountCcy.length(), "", "", "Primary Account No",AccountCcyList);
				valcustomerId = validateField(9,"CUSTOMERID", customerId, iterat, customerId.length(), "", "", "Primary Account No",CustIdList);
				valencodingName = validateField(9,"ENCODINGNAME", encodingName, iterat, encodingName.length(), "", "", "Primary Account No",null);
				valembosingName = validateField(9,"EMBOSINGNAME", embosingName, iterat, embosingName.length(), "", "", "Primary Account No",null);
				valgender = validateField(9,"GENDER", gender, iterat, gender.length(), "", "", "Primary Account No",null);
				valmail1 = validateField(9,"MAIL1", mail1, iterat, mail1.length(), "", "", "Primary Account No",null);
				valmail2 = validateField(9,"MAIL2", mail2, iterat, mail2.length(), "", "", "Primary Account No",null);    
				valmail3 = validateField(9,"MAIL3", mail3, iterat, mail3.length(), "", "", "Primary Account No",null);
				valmail4 = validateField(9,"MAIL4", mail4, iterat, mail4.length(), "", "", "Primary Account No",null);
				valmail5 = validateField(9,"MAIL5", mail5, iterat, mail5.length(), "", "", "Primary Account No",null);
				valofficePhone = validateField(9,"OFFICEPHONE", officePhone, iterat, officePhone.length(), "", "", "Primary Account No",null);
				valhomePhone = validateField(9,"HOMEPHONE", homePhone, iterat, homePhone.length(), "", "", "Primary Account No",null);
				valmobilePhone = validateField(9,"MOBILENO", mobilePhone, iterat, mobilePhone.length(), "", "", "Primary Account No",null);
				
				valemail = validateField(9,"EMAIL", email, iterat, email.length(), "", "", "Primary Account No",null);
				valregdate = validateField(9,"REGDATE", regdate, iterat, regdate.length(), "", "", "Primary Account No",null);
				valregid = validateField(9,"REGID", regid, iterat, regid.length(), "", "", "Primary Account No",null);
				valkycdoctype = validateField(9,"KYCDOCTYPE", kycdoctype, iterat, kycdoctype.length(), "", "", "Primary Account No",null);
				valkycdocid = validateField(9,"KYCDOCID", kycdocid, iterat, kycdocid.length(), "", "", "Primary Account No",null);
				valms = validateField(9,"MARITAL_STATUS", maritalstatus, iterat, maritalstatus.length(), "", "", "Primary Account No",null);
						
				valcardcollectbranch = validateField(9,"cardcollectbranch",cardcollectbranch, iterat,cardcollectbranch.length(),"","","BranchCode",getBrancList);
		          
				valP_PO_BOX = validateField(9,"P_PO_BOX",pP_PO_BOX, iterat,pP_PO_BOX.length(),"","","Primary Account No",null);
				valP_HOUSE_NO = validateField(9,"P_HOUSE_NO",pP_HOUSE_NO, iterat,pP_HOUSE_NO.length(),"","","Primary Account No",null);
		                 
				
		if(true){		    
				
			//String acctexistqry = "SELECT COUNT(1) AS CNT FROM ACCOUNTINFO WHERE ACCOUNTNO='"+pAccountNo+"' AND CIN='"+customerId+"'";
			String acctexistqry = "SELECT COUNT(1) AS CNT FROM ACCOUNTINFO WHERE ACCOUNTNO='"+pAccountNo+"'";
			enctrace("acctexistqry-->"+acctexistqry);
			String acctexist = (String)jdbcTemplate.queryForObject(acctexistqry, String.class);
			
			/*String acctexistqry = "SELECT COUNT(1) AS CNT FROM ACCOUNTINFO WHERE ACCOUNTNO='"+pAccountNo+"' AND CIN='"+customerId+"'";
			enctrace("acctexistqry-->"+acctexistqry);
			String acctexist = (String)jdbcTemplate.queryForObject(acctexistqry, String.class);
			*/
			String orderrefno = commondesc.generateorderRefno(instid, jdbcTemplate);
			trace("Generated order reference number is : " + orderrefno);
			  //  System.out.println("Checking"+valBin+"|"+binno);
			   
				if(valactionCode.equals(actionCode) && valsubProduct.equals(subProduct) && valpbranchCode.equals(pbranchCode) 
				&& valpAccountNo.equals(pAccountNo) 
				&& valpAccountType.equals(pAccountType) 
				&& valpAccountSubType.equals(pAccountSubType) && valpAccountCcy.equals(pAccountCcy)
				&& valcustomerId.equals(customerId) 
				&& valencodingName.equals(encodingName) && valembosingName.equals(embosingName)
				&& valgender.equals(gender) && valmail1.equals(mail1)
				&& valmail2.equals(mail2)
				&& valmail3.equals(mail3)
				&& valmail4.equals(mail4)
				&& valmail5.equals(mail5)
				&& valofficePhone.equals(officePhone)
				&& valhomePhone.equals(homePhone)
				&& valmobilePhone.equals(mobilePhone)
				&& valemail.equals(email)
				&& valregdate.equals(regdate)
				&& valregid.equals(regid)
				&& valkycdoctype.equals(kycdoctype)
				&& valkycdocid.equals(kycdocid)
				&& valms.equals(maritalstatus)
				&& "0".equalsIgnoreCase(acctexist)
				&& valcardcollectbranch.equals(cardcollectbranch)
				&& valP_PO_BOX.equals(pP_PO_BOX)
				&& valP_HOUSE_NO.equals(pP_HOUSE_NO)
						)
				{
					succwriter.write(iterat+"-"+actionCode+subProduct+pbranchCode+pAccountNo+pAccountType+pAccountType+
							pAccountSubType+customerId+encodingName+embosingName+gender+mail1+mail2+mail3+mail4+mail5+
							officePhone+homePhone+mobilePhone+email+regdate+regid+kycdoctype+kycdocid+maritalstatus+cardcollectbranch+pP_PO_BOX+pP_HOUSE_NO);
					//System.out.println(iterat+"-"+binno+branchCode+pAccountNo+valpAccountType+valpAccountCcy);
					
					StringBuilder of = new StringBuilder();
					of.append("INSERT INTO PERS_CARD_ORDER ");
					of.append("select '"+instid+"', '"+orderrefno+"' ORDER_REF_NO, CARD_TYPE_ID,'"+pAccountType+"', '"+pAccountSubType+"', '"+pAccountCcy+"', ");
					of.append("'"+pAccountNo+"' ACCOUNT_NO, '"+subProduct+"' ,");
					of.append("PRODUCT_CODE,LIMIT_ID,FEE_CODE,'1'CARD_QUANTITY,'01' ORDER_STATUS ,'B' ORDER_TYPE,sysdate ORDERED_DATE, '' ORDERED_TIME,");
					of.append("'"+embosingName+"' EMBOSSING_NAME, 'M' MKCK_STATUS,'--'REMARKS, '"+encodingName+"' ENCODE_DATA, sysdate MAKER_DATE, '' MAKER_TIME, ");
					of.append("'"+usercode+"' MAKER_ID, '' CHECKER_DATE, '' CHECKER_TIME, '' CHECKER_ID, '"+pbranchCode+"' BRANCH_CODE, BIN, ");
					of.append("sysdate APP_DATE, '"+customerId+"' CIN, '000'APP_NO, '0' KYC_FLAG, '$DEBIT' APPTYPE, 'P' CARDISSUETYPE, '0000' PARENTCARD, '' LIMIT_BASEDON,'4','','N','"+cardcollectbranch+"' card_collect_branch ");
					of.append("from ( ");
					of.append("select CARD_TYPE_ID,a.PRODUCT_CODE,b.LIMIT_ID, b.FEE_CODE,A.BIN from PRODUCT_MASTER a, INSTPROD_DETAILS B where A.PRODUCT_CODE in "); 
					of.append("(select PRODUCT_CODE from INSTPROD_DETAILS where  SUB_PROD_ID='"+subProduct+"' AND INST_ID='"+instid+"') AND A.PRODUCT_CODE=B.PRODUCT_CODE  and B.SUB_PROD_ID='"+subProduct+"'");   
					of.append(")  ");
					    
					StringBuilder getLimitType = new StringBuilder();
					getLimitType.append("select LIMITTYPE from EZLIMITINFO where LIMIT_RECID in ( ");
					getLimitType.append("select b.LIMIT_ID from PRODUCT_MASTER a, INSTPROD_DETAILS B where A.PRODUCT_CODE in "); 
					getLimitType.append("(select PRODUCT_CODE from INSTPROD_DETAILS where  SUB_PROD_ID='"+subProduct+"' AND INST_ID='"+instid+"') AND A.PRODUCT_CODE=B.PRODUCT_CODE and B.SUB_PROD_ID='"+subProduct+"'");
					getLimitType.append("		) and rownum=1 ");
					
					enctrace("limitlistqry :"+ getLimitType.toString());    
					String limitbasedon= (String) jdbcTemplate.queryForObject(getLimitType.toString(), String.class);
					
					StringBuilder ai = new StringBuilder();   
					
					ai.append("INSERT INTO ACCOUNTINFO ");
					ai.append("(INST_ID, ORDER_REF_NO, CIN, ACCTTYPE_ID, ACCTSUB_TYPE_ID, ACCT_CURRENCY, ACCOUNTNO, ACCOUNTTYPE, AVAILBAL, LEDGERBAL,ADDEDBY, ADDED_DATE) ");
					ai.append("VALUES ");
					ai.append("('"+instid+"', '"+orderrefno+"', '"+customerId+"', '"+pAccountType+"', '"+pAccountSubType+"', '"+pAccountCcy+"', '"+pAccountNo+"', '"+limitbasedon+"', '0', '0','"+usercode+"', sysdate )");
					
					
					StringBuilder ci = new StringBuilder();     
					ci.append("INSERT INTO CUSTOMERINFO ");
					ci.append("(INST_ID, ORDER_REF_NO, CIN, FNAME, MNAME, LNAME, DOB, GENDER, MARITAL_STATUS, NATIONALITY, "); 
					ci.append("DOCUMENT_PROVIDED, DOCUMENT_NUMBER, SPOUCE_NAME, MOTHER_NAME, FATHER_NAME, MOBILE,  ");
					ci.append("E_MAIL, P_PO_BOX, P_HOUSE_NO, P_STREET_NAME, P_WARD_NAME, P_CITY, P_DISTRICT, P_PHONE1, P_PHONE2, "); 
					ci.append("C_PO_BOX, C_HOUSE_NO, C_STREET_NAME, C_WARD_NAME, C_CITY, C_DISTRICT, C_PHONE1, C_PHONE2,  ");
					ci.append("MAKER_DATE, MAKER_ID, CHECKER_DATE, CHECKER_ID, MKCK_STATUS, CUSTOMER_STATUS) ");
					ci.append("VALUES ");
					ci.append("('"+instid+"', '"+orderrefno+"', '"+customerId+"', '"+embosingName+"', '', '', to_date('01-01-1900','dd-mm-yyyy'), '"+gender+"', '"+maritalstatus.trim()+"', '', "); 
					ci.append("'"+kycdoctype+"', '"+kycdocid+"', '', '', '', '"+mobilePhone+"',  ");
					ci.append("'"+email+"', '"+valP_PO_BOX+"', '"+valP_HOUSE_NO+"', '', '', '', '', '', '"+homePhone+"', "); 
					ci.append("'', '', '', '', '', '', '', '',  ");
					ci.append("sysdate, '"+usercode+"', '', '', 'P', '' )");          
       
					
					
					enctrace("-----------INSERTING CARD ORDER ---------------------");
					
					enctrace(""+of.toString());
					enctrace(""+ai.toString());
					enctrace(""+ci.toString());
					    
					try{
					orderinsert = commondesc.executeTransaction(of.toString(), jdbcTemplate);
					
					// changes - allow same customer with different accounts
					
					if("0".equalsIgnoreCase(acctexist)){
						accinfo = commondesc.executeTransaction(ai.toString(), jdbcTemplate);
					}
					String custexistqry = "SELECT COUNT(1) AS CNT FROM CUSTOMERINFO WHERE CIN='"+customerId+"'";
					enctrace("custexistqry-->"+custexistqry);
					String custexist = (String)jdbcTemplate.queryForObject(custexistqry, String.class);
					if("0".equalsIgnoreCase(custexist)){
						custinfo  = commondesc.executeTransaction(ci.toString(), jdbcTemplate);
					}
					//end
					
					String updaterefqry = commondesc.updateOrderrefnumcount(instid);
					
					updateref = jdbcTemplate.update(updaterefqry);
					      
					}
					catch(Exception e)
					{   e.printStackTrace();
						trace("Exception while inserting---"+e);    
						return finalresult;
					}
					enctrace("-----------INSERTING CARD ORDER ---------------------");
					

					succwriter.write("\n");   
					successRecord++;
					orderinsertCount++;
					accinfoCount++;
					custinfoCount++;
				}    
				else           
				{       
					String failStatus = "";
					if(!valactionCode.equals(actionCode)){
					failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valactionCode, usercode);
					insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
					insertFailCount =insertFailCount+1;   
					}
					if(!valsubProduct.equals(subProduct)){
					failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valsubProduct, usercode);
					insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
					insertFailCount =insertFailCount+1;           
					}
					if(!valpbranchCode.equals(pbranchCode)){
						failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valpbranchCode, usercode);
						insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
						insertFailCount =insertFailCount+1;  	
					}
					if(!valpbranchCode.equals(valcardcollectbranch)){
						failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valcardcollectbranch, usercode);
						insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
						insertFailCount =insertFailCount+1;  	
					}
					if(!valpAccountNo.equals(pAccountNo)){
						failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valpAccountNo, usercode);
						insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
						insertFailCount =insertFailCount+1;
					}
					if(!valpAccountSubType.equals(pAccountSubType)){
						failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valpAccountSubType, usercode);
						insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
						insertFailCount =insertFailCount+1;
					}
					if(!valpAccountCcy.equals(pAccountCcy)){
						failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valpAccountCcy, usercode);
						insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
						insertFailCount =insertFailCount+1;
					}
					if(!valpAccountType.equals(pAccountType)){
						failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valpAccountType, usercode);
						insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
						insertFailCount =insertFailCount+1;
					}
					if(!valcustomerId.equals(customerId)){
						failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valcustomerId, usercode);
						insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
						insertFailCount =insertFailCount+1;
					}
					if(!valencodingName.equals(encodingName)){
						failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valencodingName, usercode);
						insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
						insertFailCount =insertFailCount+1;
					}
					if(!valgender.equals(gender)){
						failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valgender, usercode);
						insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
						insertFailCount =insertFailCount+1;
					}
					if(!valmail1.equals(mail1)){failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valmail1, usercode);
					insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
					insertFailCount =insertFailCount+1;
					}
					if(!valmail2.equals(mail2)){failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valmail2, usercode);
					insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
					insertFailCount =insertFailCount+1;
					}
					if(!valmail3.equals(mail3)){failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valmail3, usercode);
					insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
					insertFailCount =insertFailCount+1;
					}
					if(!valmail4.equals(mail4)){failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valmail4, usercode);
					insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
					insertFailCount =insertFailCount+1;
					}    
					if(!valmail5.equals(mail5)){failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valmail5, usercode);
					insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
					insertFailCount =insertFailCount+1;
					}
					if(!valofficePhone.equals(officePhone)){failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valofficePhone, usercode);
					insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
					insertFailCount =insertFailCount+1;
					}
					if(!valhomePhone.equals(homePhone)){failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valhomePhone, usercode);
					insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
					insertFailCount =insertFailCount+1;
					}
					if(!valemail.equals(email)){failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valemail, usercode);
					insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
					insertFailCount =insertFailCount+1;
					}
					if(!valregdate.equals(regdate)){failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valregdate, usercode);
					insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
					insertFailCount =insertFailCount+1;
					}
					if(!valmobilePhone.equals(mobilePhone)){failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valmobilePhone, usercode);
					insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
					insertFailCount =insertFailCount+1;
					}
					if(!valregid.equals(regid)){failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valregid, usercode);
					insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
					insertFailCount =insertFailCount+1;
					}
					if(!valkycdoctype.equals(kycdoctype)){failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valkycdoctype, usercode);
					insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
					insertFailCount =insertFailCount+1;
					}
					if(!valkycdocid.equals(kycdocid)){failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valkycdocid, usercode);
					insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
					insertFailCount =insertFailCount+1;
					}  
					if(!valms.equals(maritalstatus)){failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, valms, usercode);
					insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
					insertFailCount =insertFailCount+1;
					}  
					
					if(!"0".equalsIgnoreCase(acctexist)){failStatus = dao.getFailRegStatusQry(instid, fileName, iterat, "Same customer id and same account no already exists", usercode);
					insertFailStatus = commondesc.executeTransaction(failStatus.toString(), jdbcTemplate);
					insertFailCount =insertFailCount+1;
					} 
					
					//System.err.println(iterat+"REASON:"+valactionCode+valsubProduct+valpbranchCode+valpAccountNo+valpAccountType+
					//		valpAccountSubType+valpAccountCcy+valcustomerId+valencodingName+valencodingName+valgender+
					//		valmail1+valmail2+valmail3+valmail4+valmail5+valofficePhone+valhomePhone+valmobilePhone+
					//		valemail+valregdate+valregid+valkycdoctype+valkycdocid);   
					failwriter.write(actionCode+subProduct+pbranchCode+pAccountNo+pAccountType+pAccountSubType+
							pAccountCcy+customerId+encodingName+embosingName+gender+mail1+mail2+mail3+mail4+mail5+
							officePhone+homePhone+mobilePhone+email+regdate+regid+kycdoctype+kycdocid+cardcollectbranch+pP_PO_BOX+pP_HOUSE_NO);	       
					//System.err.println(iterat+"REASON:"+valBin+valBranch+valPAccountNo+valpAccountType+valpAccountCcy);
					//System.err.println(iterat+"-"+binno+branchCode+pAccountNo+pAccountType+pAccountCcy);	
					failwriter.write("\n");    
					failrecord++;
				}  
				
				
		}  
		
}else
{
	rejectCount++;
	StringBuilder rejectStatus = new StringBuilder();
	
	String errormsg ="Length Should be " + maxLineNo + "Degit ";
	rejectStatus.append("INSERT INTO BULKFAIL_REG_STATUS ");
	rejectStatus.append("(INST_ID, FILENAME, LINE_NO, REASON, ADDED_BY, ADDED_DATE,FAIL_REJECT)");
	rejectStatus.append("VALUES");
	rejectStatus.append("('"+instid+"', '"+fileName+"', '"+iterat+"', '"+errormsg+"', '"+usercode+"', sysdate,'R')");
	
	trace("rejectStatus"+rejectStatus.toString());  
	
	insertFailStatus = commondesc.executeTransaction(rejectStatus.toString(), jdbcTemplate);
	   
}
	            }while(sc.hasNextLine());
	            
	            
	            trace("FileName::"+fileName);
	            trace("total record::"+iterat);
	            trace("total success::"+successRecord);
	            trace("total fail::"+failrecord);    
	            trace("total rejectCount::"+rejectCount);    
	            StringBuilder insertstat = new StringBuilder();
	            
	            insertstat.append("INSERT INTO BULK_REG_STATUS (INST_ID, FILENAME, UPLOADDATE, UPLOADEDBY, STATUS, TATAL_RECORD, SUCCESS, FAIL,REJECT_REC)");
	            insertstat.append("VALUES");
	            insertstat.append("('"+instid+"','"+fileName+"',sysdate,'"+usercode+"','1','"+iterat+"','"+successRecord+"','"+failrecord+"','"+rejectCount+"')");
	            enctrace("insertstat::"+insertstat.toString());
	            int insertupload = -1;
	            try{
	            insertupload = commondesc.executeTransaction(insertstat.toString(), jdbcTemplate);
	            }catch(Exception e)
	            {
	            	return finalresult;
	            }
	               
	            
	          
	           trace(""+insertupload+"-"+insertFailCount+"-"+failrecord+"-"+orderinsertCount+"-"+successRecord+"-"+custinfoCount+"-"+successRecord+"-"+accinfoCount+"-"+successRecord);
	            
	                
	    		if(insertupload==1 && insertFailCount>=failrecord && orderinsertCount == successRecord 	&& custinfoCount  ==successRecord && accinfoCount == successRecord)
	    		{
	    			
	    			finalresult= 1;     
	    		}else
	    		{
	    			finalresult=-1;
	    		}
	            
	            
	            }
	        catch (Exception ex) 
	        {
	            Logger.getLogger(DebitBulkCustomerRegister.class.getName()).log(Level.SEVERE, null, ex);
	            
	            return -1;
	        }
	        finally{}
	 
		    return 1;
	    }
	 
	   
	   public String readSuccesRecord(File path,JdbcTemplate jdbcTemplate)
	   {
		   
	   try{
	   }
	        catch (Exception ex) 
	        {
	            Logger.getLogger(DebitBulkCustomerRegister.class.getName()).log(Level.SEVERE, null, ex);
	        }
	        finally{}
	 	   return "DebitBulkRegHome";  
	   }
	   
	   public String DebitBulkRegView()
	   {
		   trace("DebitBulkRegView method called....");
		   
		   return "DebitBulkRegView";
	   }
	 

	public String UploadFileintoDb()
	{
		System.out.println("UploadFileintoDb method calledd");
		
		
		String filePath = "c:/Myuploads";  // Path where uploaded file will be stored
        System.out.println("Server path:" + filePath); // check your path in console
        
	    
             
        
        
        
		DebitBulkCustomerRegister scanner= new DebitBulkCustomerRegister();
		//String filename = getMyFile();
		 
        scanner.readSuccesRecord(new File("E:/29032014_0006.txt"),jdbctemplate);
        
		return "DebitBulkRegHome";
	}
	
	public String DebitBulkRegHome() throws IOException
	{
		System.out.println("DebitBulkRegHome method called");
		
		    
		
		DebitBulkCustomerRegister scanner= new DebitBulkCustomerRegister();
		//String filename = getMyFile();
		 
        scanner.readSuccesRecord(new File("E:/29032014_0006.txt"),jdbctemplate);
		 
		
		return "DebitBulkRegHome";
	}
	
	
	public String UploadFile() throws Exception
	{
		trace("UploadFile method called .......");
		IfpTransObj transact = commondesc.myTranObject("BULKUPLOAD", txManager);
		
		HttpSession session = getRequest().getSession();	
    	String usercode = comUserId(session);
		String instid = comInstId(session);
		System.out.println("failename:::"+this.getUploadFileFileName());   
		String todaydate = commondesc.getDate("ddMMyyyy");      
		
		// Workbook workbook = Workbook.getWorkbook(new BufferedReader(new InputStreamReader(new FileInputStream(("E:/xls/BULK_REG_PRODUCTION_CUSTOMER.xls")));
Workbook workbook = Workbook.getWorkbook(new File(this.getUploadFileFileName()));
      Sheet sheet = workbook.getSheet(0);
      String[] sheetName = workbook.getSheetNames();
      int totalNoOfRows = sheet.getRows();
      int totalNoOfCols = sheet.getColumns();
      
      System.out.println( "Sheet Name: "+sheetName[0] ); /* Table Name Must be the Xls Sheet Name */
      
      /* Composing Header data */
      String InsertQryHeader = "INSERT INTO \""+sheetName[0]+"\" (";
      
      for( int FirstColumnHeaders = 0 ; FirstColumnHeaders < totalNoOfCols ; FirstColumnHeaders++ )
      {
    	  if( !sheet.getCell( FirstColumnHeaders, 0 ).getContents().isEmpty() ){
    		  Cell cell1 = sheet.getCell( FirstColumnHeaders , 0);
    		  InsertQryHeader += "\""+cell1.getContents()+"\"";
    		  if( FirstColumnHeaders != totalNoOfCols - 1 )
    			  InsertQryHeader += ", ";
    	  }
      }
      InsertQryHeader += ") ";
      System.out.println( "Header: "+InsertQryHeader );
      
      /* Composing Values for the available rows */
      for( int i=0; i < totalNoOfRows; i++ )
      {
    	  String InsertQryValues = "VALUES ( "; /* Iteration of Values */
          for( int Column=0 ; Column < totalNoOfCols ; Column++ )
          {
        	  if( !sheet.getCell( Column, i ).getContents().isEmpty() ){
        		  Cell cell = sheet.getCell( Column , i);
        		  InsertQryValues += "\'"+cell.getContents()+"\'";
        		  if( Column != totalNoOfCols - 1 )
        			  InsertQryValues += ", ";
        	  }
        	  else 
        	  {
        		  InsertQryValues += "\'\'";
        		  if( Column != totalNoOfCols - 1 )
        			  InsertQryValues += ", ";
        	  }
          }
          InsertQryValues += ") "; /* Iteration of Values End */
          
          /* Adding Header and Value of each row */
          String InsertQry = InsertQryHeader + InsertQryValues; 
          System.out.println( i+" query: "+InsertQry );
          /*
           * ***************************************************
           * Do insert to the table. Write your Code
           * ***************************************************
           */
      }
  
      workbook.close();
	
      System.out.println("UploadSQLFile"+"called");
		return "DebitBulk_SQLRegHome";

   }
	public File getUploadFile() {
		return uploadFile;
	}
	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}
	public String getUploadFileContentType() {
		return uploadFileContentType;
	}
	public void setUploadFileContentType(String uploadFileContentType) {
		this.uploadFileContentType = uploadFileContentType;
	}
	public String getUploadFileFileName() {
		return uploadFileFileName;
	}
	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}

	public List getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(List uploadStatus) {
		this.uploadStatus = uploadStatus;
	}
	


	
}
