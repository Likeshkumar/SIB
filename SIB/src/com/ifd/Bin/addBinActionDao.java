package com.ifd.Bin;

import java.util.ArrayList;
import java.util.List;  

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;

import test.Date;

import com.ifd.Bin.AddBinActionBeans;
 
@Transactional
public class addBinActionDao extends BaseAction{
	List servicecode;
	public List getServicecode() {
		return servicecode;
	}
	public void setServicecode(List servicecode) {
		this.servicecode = servicecode;
	}
	
	/*public List<?> getHSM_result(String instid,JdbcTemplate jdbctemplate)
	{
		String hsm_qury="select HSM_ID,HSMNAME from INST_HSM_MAP where INST_ID='"+instid+"'";
		enctrace("hsm_qury==> "+hsm_qury);
		List<?> HSM_result =jdbctemplate.queryForList(hsm_qury);
		trace("HSM RESULT====> "+HSM_result.size()); 
		return HSM_result;
	}
	
	public List<?> getservicecode(String instid,JdbcTemplate jdbctemplate) 
	{
		List<?> service = null;
		String servicecode = "SELECT SERVICE_CODE FROM IFD_SERVICE_CODE  WHERE INST_ID='"+instid+"'";
		enctrace("servicecode---> "+servicecode);
		service = jdbctemplate.queryForList(servicecode);
		return service;
	}
	
	public List<?> getpinmailerdesc(String instid, JdbcTemplate jdbctemplate)
	{
		List<?> mailername = null;
		String mailernameqry ="SELECT PINMAILER_ID,PINMAILER_NAME FROM PINMAILER_DESC WHERE INST_ID='"+instid+"' AND STATUS='1'";
		enctrace("mailernameqry :" + mailernameqry );
		mailername = jdbctemplate.queryForList(mailernameqry);
		trace("mailername--> "+mailername.size());
		return mailername;
	}

	public List<?> viewbranchDetail(String instid,JdbcTemplate jdbctemplate){
		
		String query="select BRANCHATTCHED,BRCODELEN from INSTITUTION where INST_ID='"+instid+"'";	
		List<?> result=jdbctemplate.queryForList(query );
		trace("result viewbranchDetail      "+result.size());
		return result;
	}
	
	public int bincount_binreltable(String instid,JdbcTemplate jdbctemplate)
	{
		int x=-1;
		String query="select count(*) from PRODUCTINFO where INST_ID='"+instid+"'";
		int bincount_binreltable =jdbctemplate.queryForInt(query);
		//query="select BIN_COUNT from INSTITUTION where INST_ID='"+instid+"'";
		//int bincount_ifinsttable =jdbctemplate.queryForInt(query);
		return x;
	}
	
	public int checkValue(String bin,String instid,JdbcTemplate jdbctemplate)
	{
	int check=-1;	
	String qury="select count(*) from PRODUCTINFO where BIN ='"+ bin+"' and INST_ID='"+instid+"'";
	check=jdbctemplate.queryForInt(qury);
	
	return check;
	}
	
	
	public int addBinInfo(String instid ,String bin ,String bindesc ,String chnlen ,String attach_prodtype_cardtype ,String attach_brcode,
 			String baselen ,String nos_cards_gen ,String hsmid ,String pvk ,String pinlenght ,String panoffset ,String pinoffsetlenght ,String panpadchar ,String decimilisation_table ,
 			String cvv_req ,String CVV_LENGTH ,String cvk1 ,String cvk2 ,String pvk1 ,String pvk2 ,String pin_method ,String pvki ,String panvalidationlenght,
 			String deslength ,String pinmailer_id ,String sec_curr ,String float_gl_code ,String bankcardno ,String customerid ,String bkrevenueglcode ,String mbrevglcode,JdbcTemplate jdbctemplate,String userid,String auth_code,String mkchkrstatus)
	{
	int result=-1;	
	String addbin_qury="INSERT INTO PRODUCTINFO(INST_ID,BIN,BIN_DESC,CHNLEN,ATTACH_PRODTYPE_CARDTYPE,ATTACH_BRCODE," +
 			"BASELEN,NOS_CARDS_GEN,HSM_ID,PVK,PIN_LENGTH,PAN_OFFSET,PINOFFSET_LENGTH,PANPADCHAR,DECIMILISATION_TABLE," +
 			"CVV_REQUIRED,CVV_LENGTH,CVK1,CVK2,PVK1,PVK2,GEN_METHOD,PVKI,PANVALIDATION_LENGTH," +
 			"EPVK,DESLENGTH,PINMAILER_ID,SEC_CUR,FLOAT_GL_CODE,CARDNO,CUSTOMER_ID,BK_NIR_GL_CODE,MB_NIR_GL_CODE,AUTH_CODE,MKCK_STATUS,MAKER_ID,MAKER_DATE) VALUES " +
 			"('"+instid+"','"+bin+"','"+bindesc+"','"+chnlen+"','"+attach_prodtype_cardtype+"','"+attach_brcode+"'," +
 			"'"+baselen+"','"+nos_cards_gen+"','"+hsmid+"','"+pvk+"','"+pinlenght+"','"+panoffset+"','"+pinoffsetlenght+"','"+panpadchar+"','"+decimilisation_table+"'," +
 			"'"+cvv_req+"','"+CVV_LENGTH+"','"+cvk1+"','"+cvk2+"','"+pvk1+"','"+pvk2+"','"+pin_method.trim()+"','"+pvki+"','"+panvalidationlenght+"'," +
 			"'','"+deslength+"','"+pinmailer_id+"','"+sec_curr+"','"+float_gl_code+"','"+bankcardno+"','"+customerid+"','"+bkrevenueglcode+"','"+mbrevglcode+"','"+auth_code+"','"+mkchkrstatus+"','"+userid+"',sysdate)";
		trace("addbin_qury====> "+addbin_qury); 
		String columname = bin+"_CHN_BASE_NO";
		trace("columname====> "+columname); 
		result = jdbctemplate.update(addbin_qury);
		trace("result====>"+result);
		
		return result;
	
	}
	
	@Transactional
	public int addBinInfo(String instid, String usercode, String bin, String bindesc, String tablename, AddBinActionBeans binbean, JdbcTemplate jdbctemplate ) throws Exception {
		int result=-1;	
		String addbin_qury="INSERT INTO "+tablename+" (INST_ID,BIN,BIN_DESC,CHNLEN,ATTACH_PRODTYPE_CARDTYPE,ATTACH_BRCODE," +
	 			"BASELEN,NOS_CARDS_GEN,HSM_ID,PVK," +
	 			"PIN_LENGTH,PAN_OFFSET,PINOFFSET_LENGTH,PANPADCHAR,DECIMILISATION_TABLE," +
	 			"CVV_REQUIRED,CVV_LENGTH,CVK1,CVK2,PVK1," +
	 			"PVK2,GEN_METHOD,PVKI,PANVALIDATION_LENGTH," +
	 			"EPVK,DESLENGTH,PINMAILER_ID,CHN_BASE_NO,SEC_CUR," +
	 			"FLOAT_GL_CODE,CARDNO,CUSTOMER_ID,BK_NIR_GL_CODE,MB_NIR_GL_CODE,AUTH_CODE,MKCK_STATUS,MAKER_ID,MAKER_DATE, ATTACH_APP_TYPE ,SERVICE_CODE ,CARD_TYPE, SEQ_OPTION,INTAMAPTYPE) VALUES " +
	 			"('"+instid+"','"+bin+"','"+bindesc+"','"+binbean.getChnlenght()+"','"+binbean.getAttachproductcode()+"','"+binbean.getAttachbranchcode()+"'," +
	 			 "'"+binbean.getBaselength()+"','"+binbean.getNumberofcardsgen()+"','"+binbean.getHsmid()+"','"+binbean.getPinverificationkey()+"'," +
	 			 "'"+binbean.getPinlength()+"','"+ binbean.getPanoffset()+"','"+binbean.getPinoffsetlength()+"','"+binbean.getPinpadchar()+"'," +"'"+binbean.getDecimilsationtable()+"'," +
	 			 "'"+binbean.getCvvrequired()+"','"+binbean.getCvvlength()+"','"+binbean.getCvk1()+"','"+binbean.getCvk2()+"'," +"'"+binbean.getPinverificationkey1()+"',"+
	 			 "'"+binbean.getPinverificationkey2()+"','"+binbean.getPingentype()+"','"+binbean.getPinverificationkeyindex()+"','"+binbean.getPanvalidationlength()+"',"+
	 			 "'"+binbean.getPanvalidationlength()+"',"+"'"+binbean.getPinenclength()+"','"+binbean.getPinmailer()+"','"+binbean.getBaselength()+"','"+binbean.getSecondarycur()+"'," +
	 			 "'000','000','000','000','000','"+binbean.getAuthstatus()+"','"+binbean.getMkckstatus()+"','"+usercode+"',sysdate,'"+binbean.getAttacheappcode()+"','"+binbean.getServicecode()+"','"+binbean.getMagcardorchip()+"','"+binbean.getSecoption()+"','"+binbean.getInstantmapptype()+"')";
		enctrace("addbin_qury : "+addbin_qury);		  
		result = jdbctemplate.update(addbin_qury);	
		return result; 
	}
	
	@Transactional
	public int addBinInfoTemp(String instid, String usercode, AddBinActionBeans binbean, JdbcTemplate jdbctemplate ) throws Exception {
		int result=-1;	
		String addbin_qury="INSERT INTO PRODUCTINFO(INST_ID,BIN,BIN_DESC,CHNLEN,ATTACH_PRODTYPE_CARDTYPE,ATTACH_BRCODE," +
	 			"BASELEN,NOS_CARDS_GEN,HSM_ID,PVK,PIN_LENGTH,PAN_OFFSET,PINOFFSET_LENGTH,PANPADCHAR,DECIMILISATION_TABLE," +
	 			"CVV_REQUIRED,CVV_LENGTH,CVK1,CVK2,PVK1,PVK2,GEN_METHOD,PVKI,PANVALIDATION_LENGTH," +
	 			"EPVK,DESLENGTH,PINMAILER_ID,SEC_CUR,FLOAT_GL_CODE,CARDNO,CUSTOMER_ID,BK_NIR_GL_CODE,MB_NIR_GL_CODE,AUTH_CODE,MKCK_STATUS,MAKER_ID,MAKER_DATE) VALUES " +
	 			"('"+instid+"','"+binbean.getBinvalue()+"','"+binbean.getBindesc()+"','"+binbean.getChnlenght()+"','"+binbean.getAttachbranchcode()+"'," +
	 			 "'"+binbean.getAttachbranchcode()+"'," + "'"+binbean.getBaselength()+"','"+binbean.getNumberofcardsgen()+"','"+binbean.getHsmid()+"','"+binbean.getPinverificationkey()+"'," +
	 			 "'"+binbean.getPinlength()+"','"+ binbean.getPanoffset()+"','"+binbean.getPanvalidationlength()+"','"+binbean.getPinpadchar()+"'," +
	 			 "'"+binbean.getDecimilsationtable()+"'," +"'"+binbean.getCvvrequired()+"','"+binbean.getCvvlength()+"','"+binbean.getCvk1()+"','"+binbean.getCvk2()+"'," +
	 			 "'"+binbean.getPinverificationkey1()+"','"+binbean.getPinverificationkey2()+"','"+binbean.getPingentype()+"','"+binbean.getPinverificationkeyindex()+"'," +
	 			 "'"+binbean.getPanvalidationlength()+"','"+binbean.getPinenclength()+"','"+binbean.getPinmailer()+"','"+binbean.getSecondarycur()+"'," +
	 			 "000','000','000','000','000','"+binbean.getAuthstatus()+"','"+binbean.getMkckstatus()+"','"+usercode+"',sysdate)";
		enctrace("addbin_qurytemp : "+addbin_qury);		  
		result = jdbctemplate.update(addbin_qury);	
		return result; 
	}
	
	
	public int ccy_inst_query(String instid, String bin,String[] multi_currency_code,int i, String minimum_amount, String maximum_amount,JdbcTemplate jdbctemplate)
	{
	int check_status=-1;
	String ccy_inst_query="INSERT INTO IFP_BIN_CURRENCY (INST_ID, BIN, CUR_CODE, CUR_PRIORITY, MIN_AMOUNT, MAX_AMOUNT) VALUES ('"+instid+"', '"+bin+"', '"+multi_currency_code[i]+"', 'S', '"+minimum_amount+"', '"+maximum_amount+"')";
	  enctrace("ccy_inst_query "+ccy_inst_query);
	  check_status=jdbctemplate.update(ccy_inst_query);
	 
	 return check_status;
	} 
	
	public List<?> binlist(String instid,JdbcTemplate jdbctemplate)
	{
		
		String qury="select * from PRODUCTINFO where inst_id='"+instid+"' AND AUTH_CODE='1' AND DELETED_FLAG !='2' ";
		enctrace(qury);
		List<?> binlist =jdbctemplate.queryForList(qury);
		return binlist;
	}
	public List<?> binlistForMaker(String instid,JdbcTemplate jdbctemplate)
	{
		
		String qury="select * from PRODUCTINFO where inst_id='"+instid+"' AND DELETED_FLAG !='2'  ";
		enctrace(qury);
		List<?> binlist =jdbctemplate.queryForList(qury);
		return binlist;
	}
	
	public List<?> bindetails(String bin,String instid,JdbcTemplate jdbctemplate)
	{
		String bindetails_query="select INST_ID,BIN,CHNLEN,DECODE(ATTACH_PRODTYPE_CARDTYPE,'C','CARD TYPE','P','PRODUCT CODE','N','NO') AS ATTACH_PRODTYPE_CARDTYPE,CASE ATTACH_BRCODE WHEN 'N' THEN 'NO' ELSE 'YES' END as ATTACH_BRCODE,BASELEN,NOS_CARDS_GEN,HSM_ID," +
		"PVK,PIN_LENGTH,PAN_OFFSET,PINOFFSET_LENGTH,PANPADCHAR,DECIMILISATION_TABLE,DECODE(CVV_REQUIRED,'N','NO','Y','YES') AS CVV_REQUIRED,CVV_LENGTH,CVK1,CVK2,PVK1,PVK2,GEN_METHOD,PVKI,PANVALIDATION_LENGTH,EPVK," +
		"DESLENGTH,PINMAILER_ID,CHN_BASE_NO,DECODE(SEC_CUR,'1','YES','0','NO') AS SEC_CUR ,ACCT_LEN,ACCT_SEQ_NO,BIN_DESC,DECODE(AUTH_CODE,'0','Waiting for authorize','1','Authorized','9','DeAuthorized') AS AUTH_CODE,MAKER_ID,NVL(REMARKS,'--') AS REMARKS, CHECKER_ID, to_char(CHECKER_DATE,'DD-MON-YYYY') AS CHECKER_DATE, TO_CHAR(MAKER_DATE,'DD-MON-YYYY') AS MAKER_DATE,SERVICE_CODE, DECODE(CARD_TYPE,'M','Magnatic Card','C','Chip Card')CARD_TYPE, DECODE(SEQ_OPTION,'P','Pin Only','PC','Pin and CVV','C','CVV Only')SEQ_OPTION,DECODE(INTAMAPTYPE,'WEBSRV','FETCH DATA USING WEB SERVICE','CBSLINK',' FETCH DATA USING CBS LINK','SCHEMA','FETCH DATA USING OTHER SCHEMA')INTAMAPTYPE" +
		"  from PRODUCTINFO where " +
		"bin='"+bin+"' and inst_id='"+instid+"'";
		enctrace("bindetails_query===> "+bindetails_query);
		
		List<?> bindetails = jdbctemplate.queryForList(bindetails_query);
		return bindetails;
	}
	
	
	
	public List multiccy_list(String instid,String bin,JdbcTemplate jdbctemplate) throws Exception
	{
		String multicurr = "SELECT CUR_CODE,MIN_AMOUNT,MAX_AMOUNT FROM IFP_BIN_CURRENCY WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
		enctrace("multicurr -----   "+multicurr);
		List multiccy_list = jdbctemplate.queryForList(multicurr);
		return multiccy_list;
	}

	public String gethsmname(String instid,String hsmid,JdbcTemplate jdbctemplate) throws Exception
	{
		String ret_val = null;
		try {
			String qury_str  = "select HSMNAME from INST_HSM_MAP where INST_ID='"+instid+"' and HSM_ID='"+hsmid+"'";
			enctrace("qury_str==> "+qury_str);
			ret_val = (String)jdbctemplate.queryForObject(qury_str, String.class);
		} catch (EmptyResultDataAccessException e) {}
		return ret_val;
	} 

	public String getpinmailername(String instid,String pinmailerid,JdbcTemplate jdbctemplate)	{
		String ret_val = "N"; 
		try{
			String qury_str  = "select DISTINCT(PINMAILER_NAME) from PINMAILER_DESC where INST_ID='"+instid+"' and PINMAILER_ID='"+pinmailerid+"' AND STATUS='1'";
			enctrace("qury_str==> "+qury_str);
			ret_val = (String)jdbctemplate.queryForObject(qury_str, String.class);
		}catch(EmptyResultDataAccessException e ){}
		return ret_val;
	}

	public String getcurrencydesc(String ccycode,JdbcTemplate jdbctemplate)
	{
		String curdesc = "Select CURRENCY_DESC from GLOBAL_CURRENCY where CURRENCY_CODE='"+ccycode+"'  OR NUMERIC_CODE='"+ccycode+"'";
		enctrace("curdesc==> "+curdesc);
		String ret_val = (String)jdbctemplate.queryForObject(curdesc, String.class);
		return ret_val;
	}
	

	
	public int checkingBininproduction(String inst_id, String bin_no,JdbcTemplate jdbctemplate)
	{
		int isBinExsist = -1;
		//JdbcTemplate jdbctemplate = new JdbcTemplate(dataSource);
		//String check_bin = "select count(distinct (substr(card_no,1,6))) from ifpcard_production  WHERE INST_ID='"+inst_id+"' and substr(card_no,1,6) = '"+bin_no+"'";
		String check_bin="select COUNT(*) from CARD_PRODUCTION where INST_ID='"+inst_id+"' and BIN='"+bin_no+"'";
		System.out.println(" check_bin query is "+check_bin);
		try
		{
			int bin_count = jdbctemplate.queryForInt(check_bin);
			System.out.println("Value from the Query is "+bin_count);
			if(bin_count > 0)
			{
				isBinExsist = 1;
			}
			else
			{
				isBinExsist = 0;
			}
		}
		catch (Exception e) 
		{
			System.out.println("The Exception in Bin Counting "+e.getMessage());
			isBinExsist = -2;
		}
		System.out.println("The Retunr value of checkingBininproduction "+isBinExsist);
		return isBinExsist;
	} 

	//temprary code........................
	public String getCurrencyDesc( String curcode,JdbcTemplate jdbctemplate ){
		String sec_curdesc = "SELECT CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE CURRENCY_CODE='"+curcode+"'  OR NUMERIC_CODE='"+curcode+"' and rownum <= 1";
		System.out.println("sec_cur_req___" + sec_curdesc);
		
		//JdbcTemplate jdbctemplate = new JdbcTemplate(dataSource);
		String sec_cur_det =(String)jdbctemplate.queryForObject(sec_curdesc,String.class);
		
		return sec_cur_det;
	}
	
	public List<?> multicurrency_qury(String instid,JdbcTemplate jdbctemplate)
	{
	List<?> selectlist=null;	
	String multicurrency_qury="select CUR_CODE from INSTITUTION_CURRENCY WHERE INST_ID='"+instid+"'";
	System.out.println( "multicurrency_qury"+multicurrency_qury);
	selectlist =jdbctemplate.queryForList(multicurrency_qury);
	System.out.println("List From DB===>"+selectlist);
	return selectlist;
	} 
	
	public List<?> multicurrency_qurynew(String instid,JdbcTemplate jdbctemplate)
	{
	List<?> selectlist=null;	
	String multicurrency_qury="select * from PRODUCTINFO where inst_id='"+instid+"' AND MKCK_STATUS='M'";
	System.out.println( "multicurrency_qurynew--->"+multicurrency_qury);
	selectlist =jdbctemplate.queryForList(multicurrency_qury);
	System.out.println("multicurrency_qurynew From DB===>"+selectlist);
	return selectlist;
	} 

	public String getbindesc(String inst_Name,String bin,JdbcTemplate jdbcTemplate)
	{
		String bindesc = "Select BIN_DESC from PRODUCTINFO where BIN='"+bin+"' and INST_ID='"+inst_Name+"'";
		enctrace("bin==> "+bindesc);
		String ret_val = (String)jdbcTemplate.queryForObject(bindesc, String.class);
		return ret_val;
	}
	public int updateAuthBin(String authstatus,String userid,String instid,String bin, String remarks, JdbcTemplate jdbctemplate ) throws Exception  {
		int x = -1;
		String update_authdeauth_qury = "UPDATE PRODUCTINFO SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='1',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
		enctrace ( "update_authdeauth_qury : "+ update_authdeauth_qury );
		x = jdbctemplate.update(update_authdeauth_qury);
		return x; 
	}
	
	public int moveToMainBin( String instid, String bin,  JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String binmoveqry = "INSERT INTO PRODUCTINFO SELECT * FROM PRODUCTBIN_REL WHERE inst_id='"+instid+"' AND BIN='"+bin+"'" ;
			   enctrace("binmoveqry :" + binmoveqry );
		x = jdbctemplate.update(binmoveqry);
		return x;  
	}
	
 
		
	public int updateBinTempRecords(String instid, String usercode, String bin, String bindesc, String tablename, AddBinActionBeans binbean, JdbcTemplate jdbctemplate ) throws Exception {
		int result=-1;	
		String updateqry = "UPDATE PRODUCTINFO SET BIN_DESC='"+bindesc+"', HSM_ID='"+binbean.getHsmid()+"', PVK='"+binbean.getPinverificationkey()+"', ";
			   updateqry += " MKCK_STATUS='"+binbean.getMkckstatus()+"', MAKER_ID='"+usercode+"', MAKER_DATE='sysdate' ";
		       updateqry += " WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
		enctrace("update bin qry : "+ updateqry );		  
		result = jdbctemplate.update(updateqry);	
		return result; 
	} 
	
	public int updateDeAuthBin(String authstatus,String userid,String remarks,String instid,String bin, JdbcTemplate jdbctemplate) {
		int x = -1;
		String update_authdeauth_qury = "UPDATE PRODUCTINFO SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='9',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"',REMARKS='"+remarks+"', DELETED_FLAG ='0'  WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
		enctrace( "update_authdeauth_qury " +  update_authdeauth_qury );
		x = jdbctemplate.update(update_authdeauth_qury );
		return x; 
	}
	public int getAuthexistCardTypeList(String instid,JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String del_qury="SELECT COUNT(*) FROM  PRODUCTINFO  where INST_ID='"+instid+"' and AUTH_CODE='0'  AND DELETED_FLAG != '2' ";
		enctrace( "del_qury : " + del_qury );
		x= jdbctemplate.queryForInt(del_qury);
		return x; 
	}
	
	
	
	public List binlistForChecker(String instid, JdbcTemplate jdbctemplate)
	{
		List<String> binlist = new ArrayList<>();
		String mailernameqry ="select * from PRODUCTINFO where inst_id='"+instid+"' and AUTH_CODE='0' AND MKCK_STATUS='M'";
		enctrace("mailernameqry :" + mailernameqry );
		binlist = jdbctemplate.queryForList(mailernameqry);
		trace("mailername--> "+binlist.size());
		return binlist;
		
		
		List selectlist=new ArrayList<>();	
		String multicurrency_qury="select * from PRODUCTINFO where inst_id='"+instid+"' and AUTH_CODE='0' AND MKCK_STATUS='M'";
		System.out.println( "multicurrency_qury"+multicurrency_qury);
		selectlist =jdbctemplate.queryForList(multicurrency_qury);
		System.out.println("List From DB===>"+selectlist);
		return selectlist;
		
	}
	
	public List<?> multicurrency_qury1(String instid,JdbcTemplate jdbctemplate)
	{
	List<?> selectlist=null;	
	String multicurrency_qury="select BIN from PRODUCTINFO where inst_id='"+instid+"' and AUTH_CODE='0' AND MKCK_STATUS='M'";
	System.out.println( "multicurrency_qury"+multicurrency_qury);
	selectlist =jdbctemplate.queryForList(multicurrency_qury);
	System.out.println("List From DB===>"+selectlist);
	return selectlist;
	} 
	
	public List<?> binlistForChecker(String instid,JdbcTemplate jdbctemplate)
	{
		List binlist=new ArrayList();
		
		String qury="select BIN,BIN_DESC from PRODUCTINFO where inst_id='"+instid+"' and AUTH_CODE='0' AND MKCK_STATUS='M'";
		enctrace(qury);
		 binlist =jdbctemplate.queryForList(qury);
		System.out.println("Getting Bin List"+binlist);
		return binlist;
	}
	
	public List<?> binlistForEdit(String instid, String usercode, JdbcTemplate jdbctemplate) throws Exception {
		
		String qury="select BIN,BIN_DESC from PRODUCTINFO where inst_id='"+instid+"'  AND DELETED_FLAG != '2'  ";
		enctrace(qury);
		List<?> binlist =jdbctemplate.queryForList(qury);
		return binlist;
	}
	
	public List<?> binlistForDeleteAuth(String instid, JdbcTemplate jdbctemplate) throws Exception {
		
		String qury="select BIN,BIN_DESC from PRODUCTINFO where inst_id='"+instid+"' AND DELETED_FLAG='2' AND AUTH_CODE='0'";
		enctrace(qury);
		List<?> binlist =jdbctemplate.queryForList(qury);
		return binlist;
	}
	
	public List getBinDetailsForEdit(String instid, String bin, JdbcTemplate jdbctemplate ) throws Exception {
		List bindata = null;
		String bindetails = "SELECT * FROM PRODUCTINFO WHERE INST_ID='"+instid+"' AND BIN='"+bin+"' AND DELETED_FLAG != '2' ";
		enctrace("bindetails :" + bindetails);
		bindata = jdbctemplate.queryForList(bindetails);
		return bindata;
	}
	
	public int updateBinRecords(String instid, String usercode, String bin, String bindesc, String tablename, AddBinActionBeans binbean, JdbcTemplate jdbctemplate ) throws Exception {
		int result=-1;	
		String updateqry =  " UPDATE "+tablename+" SET BIN_DESC='"+bindesc+"', CHNLEN='"+binbean.getChnlenght()+"', ATTACH_PRODTYPE_CARDTYPE='"+binbean.getAttachproductcode()+"', ";
			   updateqry += " ATTACH_BRCODE='"+binbean.getAttachbranchcode()+"', BASELEN='"+binbean.getBaselength()+"', NOS_CARDS_GEN='"+binbean.getNumberofcardsgen()+"', ";
			   updateqry += " HSM_ID='"+binbean.getHsmid()+"', PVK='"+binbean.getPinverificationkey()+"', PIN_LENGTH='"+binbean.getPinlength()+"', ";
			   updateqry += " PAN_OFFSET='"+binbean.getPanoffset()+"', CVV_REQUIRED='"+binbean.getCvvrequired()+"', CVV_LENGTH='"+binbean.getCvvlength()+"', ";
			   updateqry += " DECIMILISATION_TABLE='"+binbean.getDecimilsationtable()+"',";
			   updateqry += " CVK1='"+binbean.getCvk1()+"', CVK2='"+binbean.getCvk2()+"', PVK1='"+binbean.getPinverificationkey1()+"', ";
			   updateqry += " PVK2='"+binbean.getPinverificationkey2()+"', GEN_METHOD='"+binbean.getPingentype()+"', PVKI='"+binbean.getPinverificationkeyindex()+"', ";
			   updateqry += " PANVALIDATION_LENGTH='"+binbean.getPanvalidationlength()+"',  DESLENGTH='"+binbean.getPinenclength()+"', ";
			   updateqry += " PINMAILER_ID='"+binbean.getPinmailer()+"', CHN_BASE_NO='"+binbean.getBaselength()+"', ";
			   updateqry += " SERVICE_CODE='"+binbean.getServicecode()+"', CARD_TYPE='"+binbean.getMagcardorchip()+"', SEQ_OPTION='"+binbean.getSecoption()+"' , ";
			   updateqry += " AUTH_CODE='"+binbean.getAuthstatus()+"', MKCK_STATUS='"+binbean.getMkckstatus()+"', MAKER_ID='"+usercode+"', ";
			   updateqry += " MAKER_DATE=sysdate, REMARKS='', CHECKER_ID='', CHECKER_DATE=''";
			   updateqry += " WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
			   enctrace("update bin qry : "+ updateqry );		  
			   result = jdbctemplate.update(updateqry);	 
			   return result;    
	}    
	
	public int checkBinExist(String instid, String bin, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String existbinqry="SELECT COUNT(*) FROM  PRODUCTINFO  where INST_ID='"+instid+"' and BIN='"+bin+"'";
		enctrace( "existbinqry : " + existbinqry );
		x= jdbctemplate.queryForInt( existbinqry );
		return x; 
	}
	
	public int checkBinExistTemp(String instid, String bin, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String existbinqry="SELECT COUNT(*) FROM  PRODUCTINFO  where INST_ID='"+instid+"' and BIN='"+bin+"'";
		enctrace( "existbinqry : " + existbinqry );
		x= jdbctemplate.queryForInt( existbinqry );
		return x; 
	}
	
	
	public int deleteBinFromProduction(String instid,String bin,JdbcTemplate jdbctemplate) throws Exception	{
		int x = -1;
		String qury="delete from PRODUCTINFO where inst_id='"+instid+"' and BIN='"+bin+"'";
		enctrace("qury "+qury);
		x = jdbctemplate.update(qury);
		return x;
	}
	
	
	public int deleteBin(String instid, String tablename, String bin, String mkckstatus, String authstatus, String usercode, String remarks, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String update_authdeauth_qury = "UPDATE "+tablename+" SET DELETED_FLAG='2', MKCK_STATUS='"+mkckstatus+"', AUTH_CODE='"+authstatus+"',MAKER_DATE=sysdate,MAKER_ID='"+usercode+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
		enctrace ( "update_authdeauth_qury : "+ update_authdeauth_qury );
		x = jdbctemplate.update(update_authdeauth_qury);
		return x; 
	}
	
	public int insertIntoBinHistory(String instid,  String bin, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1; 
		String update_authdeauth_qury = "INSERT INTO PRODUCTINFO_HIST SELECT * FROM PRODUCTBIN_REL WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
		enctrace ( "update_authdeauth_qury : "+ update_authdeauth_qury );
		x = jdbctemplate.update(update_authdeauth_qury);
		return x; 
	}
	
	 
	public int deleteProduct(String instid, String tablename, String bin, String mkckstatus, String deletedflag, String authstatus, String usercode, String remarks, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String update_authdeauth_qury = "UPDATE "+tablename+" SET DELETED_FLAG='"+deletedflag+"', MKCK_STATUS='"+mkckstatus+"', AUTH_CODE='"+authstatus+"',MAKER_DATE=sysdate,MAKER_ID='"+usercode+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
		enctrace ( "update_authdeauth_qury : "+ update_authdeauth_qury );
		x = jdbctemplate.update(update_authdeauth_qury);
		return x; 
	}
	
	public int deleteProductAuth(String instid, String tablename, String bin, String mkckstatus, String deletedflag, String authstatus, String usercode, String remarks, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String update_authdeauth_qury = "UPDATE PRODUCT_MASTER SET DELETED_FLAG='"+deletedflag+"', MKCK_STATUS='"+mkckstatus+"', AUTH_CODE='"+authstatus+"',CHECKER_DATE=sysdate,CHECKER_ID='"+usercode+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
		enctrace ( "update_authdeauth_qury : "+ update_authdeauth_qury );
		x = jdbctemplate.update(update_authdeauth_qury);
		return x; 
	}
	
	public List<?> getAcctTypes(String instid, JdbcTemplate jdbctemplate)
	{
		List<?> accttypes = null;
		String accttypesqry ="SELECT ACCT_FLAG,ACCTTYPEDESC FROM ACCT_TYPES WHERE INST_ID='"+instid+"' AND STATUS='1'";
		enctrace("accttypesqry :" + accttypesqry );
		accttypes = jdbctemplate.queryForList(accttypesqry);
		trace("accttypes--> "+accttypes.size());
		return accttypes;
	}
	
	public List<?> getDta(String instid,JdbcTemplate jdbctemplate)
	{
		System.out.println("jdbc "+jdbctemplate);
		
		List binlist=new ArrayList();
		
		String qury="select BIN from PRODUCTINFO where inst_id='"+instid+"' and AUTH_CODE='0' AND MKCK_STATUS='M'";
		enctrace(qury);
		 binlist =jdbctemplate.queryForList(qury);
		System.out.println("Getting Bin List"+binlist);
		return binlist;
	}
	*/
	
	
	
	
	public List<?> getHSM_result(String instid,JdbcTemplate jdbctemplate)
	{
		/*String hsm_qury="select HSM_ID,HSMNAME from INST_HSM_MAP where INST_ID='"+instid+"'";
		enctrace("hsm_qury==> "+hsm_qury);
		List<?> HSM_result =jdbctemplate.queryForList(hsm_qury);
		trace("HSM RESULT====> "+HSM_result.size()); */
		
		//ADDED BY GOWTHAM_090819
		
		String hsm_qury="select HSM_CODE,HSM_DESC from HSM_TYPE where INST_ID=?";
		enctrace("hsm_qury==> "+hsm_qury);
		List HSM_result= jdbctemplate.queryForList(hsm_qury, new Object[]{instid});
		trace("HSM RESULT====> "+HSM_result.size());
		return HSM_result;
	}
	
	public List<?> getservicecode(String instid,JdbcTemplate jdbctemplate) 
	{
		List<?> service = null;
		String servicecode = "SELECT SERVICE_CODE FROM IFD_SERVICE_CODE  WHERE INST_ID='"+instid+"'";
		enctrace("servicecode---> "+servicecode);
		service = jdbctemplate.queryForList(servicecode);
		return service;
	}
	
	public List<?> getpinmailerdesc(String instid, JdbcTemplate jdbctemplate)
	{
		List<?> mailername = null;
		
		/*String mailernameqry ="SELECT PINMAILER_ID,PINMAILER_NAME FROM PINMAILER_DESC WHERE INST_ID='"+instid+"' AND STATUS='1'";
		enctrace("mailernameqry :" + mailernameqry );
		mailername = jdbctemplate.queryForList(mailernameqry);*/

		//by gowtham-170819
		String mailernameqry ="SELECT PINMAILER_ID,PINMAILER_NAME FROM PINMAILER_DESC WHERE INST_ID=? AND STATUS=?";
		enctrace("mailernameqry :" + mailernameqry );
		mailername = jdbctemplate.queryForList(mailernameqry,new Object[]{instid,"1"});
		
		trace("mailername--> "+mailername.size());
		return mailername;
	}

	public List<?> viewbranchDetail(String instid,JdbcTemplate jdbctemplate){
		
		/*String query="select BRANCHATTCHED,BRCODELEN from INSTITUTION where INST_ID='"+instid+"'";	
		List<?> result=jdbctemplate.queryForList(query );
		trace("result viewbranchDetail      "+result.size());*/
		
		//by gowtham-170819
		String query="select BRANCHATTCHED,BRCODELEN from INSTITUTION where INST_ID=?";	
		List<?> result=jdbctemplate.queryForList(query,new Object[]{instid} );
		trace("result viewbranchDetail      "+result.size());
		
		
		return result;
	}
	
	public int bincount_binreltable(String instid,JdbcTemplate jdbctemplate)
	{
		int x=-1;
		
		/*String query="select count(*) from PRODUCTINFO where INST_ID='"+instid+"'";
		int bincount_binreltable =jdbctemplate.queryForInt(query);*/
		
		//by gowtham-190819
		String query="select count(*) from PRODUCTINFO where INST_ID=?";
		int bincount_binreltable =jdbctemplate.queryForInt(query,new Object[]{instid});
		
		
		//query="select BIN_COUNT from INSTITUTION where INST_ID='"+instid+"'";
		//int bincount_ifinsttable =jdbctemplate.queryForInt(query);
		return x;
	}
	
	public int checkValue(String bin,String instid,JdbcTemplate jdbctemplate)
	{
	int check=-1;	

	String productcode = null;
	String qury="select count(*) from PRODUCTINFO where PRD_CODE ='"+ productcode+"' and INST_ID='"+instid+"'";
	check=jdbctemplate.queryForInt(qury);
	
	return check;
	}
	
	
	/*public int addBinInfo(String instid ,String bin ,String bindesc ,String chnlen ,String attach_prodtype_cardtype ,String attach_brcode,
 			String baselen ,String nos_cards_gen ,String hsmid ,String pvk ,String pinlenght ,String panoffset ,String pinoffsetlenght ,String panpadchar ,String decimilisation_table ,
 			String cvv_req ,String CVV_LENGTH ,String cvk1 ,String cvk2 ,String pvk1 ,String pvk2 ,String pin_method ,String pvki ,String panvalidationlenght,
 			String deslength ,String pinmailer_id ,String sec_curr ,String float_gl_code ,String bankcardno ,String customerid ,String bkrevenueglcode ,String mbrevglcode,JdbcTemplate jdbctemplate,String userid,String auth_code,String mkchkrstatus)
	{
	int result=-1;	
	String addbin_qury="INSERT INTO PRODUCTINFO(INST_ID,BIN,BIN_DESC,CHNLEN,ATTACH_PRODTYPE_CARDTYPE,ATTACH_BRCODE," +
 			"BASELEN,NOS_CARDS_GEN,HSM_ID,PVK,PIN_LENGTH,PAN_OFFSET,PINOFFSET_LENGTH,PANPADCHAR,DECIMILISATION_TABLE," +
 			"CVV_REQUIRED,CVV_LENGTH,CVK1,CVK2,PVK1,PVK2,GEN_METHOD,PVKI,PANVALIDATION_LENGTH," +
 			"EPVK,DESLENGTH,PINMAILER_ID,SEC_CUR,FLOAT_GL_CODE,CARDNO,CUSTOMER_ID,BK_NIR_GL_CODE,MB_NIR_GL_CODE,AUTH_CODE,MKCK_STATUS,MAKER_ID,MAKER_DATE) VALUES " +
 			"('"+instid+"','"+bin+"','"+bindesc+"','"+chnlen+"','"+attach_prodtype_cardtype+"','"+attach_brcode+"'," +
 			"'"+baselen+"','"+nos_cards_gen+"','"+hsmid+"','"+pvk+"','"+pinlenght+"','"+panoffset+"','"+pinoffsetlenght+"','"+panpadchar+"','"+decimilisation_table+"'," +
 			"'"+cvv_req+"','"+CVV_LENGTH+"','"+cvk1+"','"+cvk2+"','"+pvk1+"','"+pvk2+"','"+pin_method.trim()+"','"+pvki+"','"+panvalidationlenght+"'," +
 			"'','"+deslength+"','"+pinmailer_id+"','"+sec_curr+"','"+float_gl_code+"','"+bankcardno+"','"+customerid+"','"+bkrevenueglcode+"','"+mbrevglcode+"','"+auth_code+"','"+mkchkrstatus+"','"+userid+"',sysdate)";
		trace("addbin_qury====> "+addbin_qury); 
		String columname = bin+"_CHN_BASE_NO";
		trace("columname====> "+columname); 
		result = jdbctemplate.update(addbin_qury);
		trace("result====>"+result);
		
		return result;
	
	}*/
	
	@Transactional
	public int addBinInfo(String instid, String usercode, String bin, String bindesc, String tablename, AddBinActionBeans binbean, JdbcTemplate jdbctemplate ) throws Exception {
		int result=-1;	
		
		
		Date date=new Date();
				/*String addbin_qury="INSERT INTO "+tablename+" (INST_ID,BIN,BIN_DESC,CHNLEN,ATTACH_PRODTYPE_CARDTYPE,ATTACH_BRCODE," +
	 			"BASELEN,NOS_CARDS_GEN,HSM_ID,PVK," +
	 			"PIN_LENGTH,PAN_OFFSET,PINOFFSET_LENGTH,PANPADCHAR,DECIMILISATION_TABLE," +
	 			"CVV_REQUIRED,CVV_LENGTH,CVK1,CVK2,PVK1," +
	 			"PVK2,GEN_METHOD,PVKI,PANVALIDATION_LENGTH," +
	 			"EPVK,DESLENGTH,PINMAILER_ID,CHN_BASE_NO,SEC_CUR," +
	 			"FLOAT_GL_CODE,CARDNO,CUSTOMER_ID,BK_NIR_GL_CODE,MB_NIR_GL_CODE,AUTH_CODE,MKCK_STATUS,MAKER_ID,MAKER_DATE, ATTACH_APP_TYPE ,SERVICE_CODE ,CARD_TYPE, SEQ_OPTION,INTAMAPTYPE) "
	 			+ "VALUES " +
	 			"('"+instid+"','"+bin+"','"+bindesc+"','"+binbean.getChnlenght()+"','"+binbean.getAttachproductcode()+"','"+binbean.getAttachbranchcode()+"'," +
	 			 "'"+binbean.getBaselength()+"','"+binbean.getNumberofcardsgen()+"','"+binbean.getHsmid()+"','"+binbean.getPinverificationkey()+"'," +
	 			 "'"+binbean.getPinlength()+"','"+ binbean.getPanoffset()+"','"+binbean.getPinoffsetlength()+"','"+binbean.getPinpadchar()+"'," +"'"+binbean.getDecimilsationtable()+"'," +
	 			 "'"+binbean.getCvvrequired()+"','"+binbean.getCvvlength()+"','"+binbean.getCvk1()+"','"+binbean.getCvk2()+"'," +"'"+binbean.getPinverificationkey1()+"',"+
	 			 "'"+binbean.getPinverificationkey2()+"','"+binbean.getPingentype()+"','"+binbean.getPinverificationkeyindex()+"','"+binbean.getPanvalidationlength()+"',"+
	 			 "'"+binbean.getPanvalidationlength()+"',"+"'"+binbean.getPinenclength()+"','"+binbean.getPinmailer()+"','"+binbean.getBaselength()+"','"+binbean.getSecondarycur()+"'," +
	 			 "'000','000','000','000','000','"+binbean.getAuthstatus()+"','"+binbean.getMkckstatus()+"','"+usercode+"',sysdate,'"+binbean.getAttacheappcode()+"','"+binbean.getServicecode()+"','"+binbean.getMagcardorchip()+"','"+binbean.getSecoption()+"','"+binbean.getInstantmapptype()+"')";
				enctrace("addbin_qury : "+addbin_qury);		  
				result = jdbctemplate.update(addbin_qury);	*/
		
		String addbin_qury=
		"INSERT INTO "+tablename+ "(INST_ID,			BIN,			BIN_DESC,			CHNLEN,					ATTACH_PRODTYPE_CARDTYPE,"+
									"ATTACH_BRCODE,		BASELEN,		NOS_CARDS_GEN,		HSM_ID,					PVK,"+
									"PIN_LENGTH,		PAN_OFFSET,		PINOFFSET_LENGTH,	PANPADCHAR,				DECIMILISATION_TABLE,"+
									"CVV_REQUIRED,		CVV_LENGTH,		CVK1,				CVK2,					PVK1,"+
									"PVK2,				GEN_METHOD,		PVKI,				PANVALIDATION_LENGTH,	EPVK,"+
									"DESLENGTH,			PINMAILER_ID,	CHN_BASE_NO,		SEC_CUR,				FLOAT_GL_CODE,"+
									"CARDNO,			CUSTOMER_ID,	BK_NIR_GL_CODE,		MB_NIR_GL_CODE,			AUTH_CODE,"+
									"MKCK_STATUS,		MAKER_ID,		MAKER_DATE, 		ATTACH_APP_TYPE ,		SERVICE_CODE ,"+	
									"CARD_TYPE,			 SEQ_OPTION,	INTAMAPTYPE)"
									
									+"VALUES "+"(?,?,?,?,?,  ?,?,?,?,?,  ?,?,?,?,?,  ?,?,?,?,?,  ?,?,?,?,?,  ?,?,?,?,?,  ?,?,?,?,?,  ?,?,?,?,?,  ?,?,?)";
					
				enctrace("addbin_qury : "+addbin_qury);		
		
				result = jdbctemplate.update(addbin_qury,new Object[]{
				instid,								bin,							bindesc,								binbean.getChnlenght(),				binbean.getAttachproductcode(),
				binbean.getAttachbranchcode(),		binbean.getBaselength(),		binbean.getNumberofcardsgen(),			binbean.getHsmid(),					binbean.getPinverificationkey(),
				binbean.getPinlength(),				binbean.getPanoffset(),			binbean.getPinoffsetlength(),			binbean.getPinpadchar(),			binbean.getDecimilsationtable(),
				binbean.getCvvrequired(),			binbean.getCvvlength(),			binbean.getCvk1(),						binbean.getCvk2(),					binbean.getPinverificationkey1(),
				binbean.getPinverificationkey2(),	binbean.getPingentype(),		binbean.getPinverificationkeyindex(),	binbean.getPanvalidationlength(),	binbean.getPanvalidationlength(),
				binbean.getPinenclength(),			binbean.getPinmailer(),			binbean.getBaselength(),				binbean.getSecondarycur(),			"000",
				"000",								"000",							"000",									"000",								binbean.getAuthstatus(),
				binbean.getMkckstatus(),			usercode,						date.getCurrentDate(),					binbean.getAttacheappcode(),		binbean.getServicecode(),
				binbean.getMagcardorchip(),			binbean.getSecoption(),			binbean.getInstantmapptype()
				});

		
		
		return result; 
	}
	
	/*@Transactional
	public int addBinInfoTemp(String instid, String usercode, AddBinActionBeans binbean, JdbcTemplate jdbctemplate ) throws Exception {
		int result=-1;	
		String addbin_qury="INSERT INTO PRODUCTINFO(INST_ID,BIN,BIN_DESC,CHNLEN,ATTACH_PRODTYPE_CARDTYPE,ATTACH_BRCODE," +
	 			"BASELEN,NOS_CARDS_GEN,HSM_ID,PVK,PIN_LENGTH,PAN_OFFSET,PINOFFSET_LENGTH,PANPADCHAR,DECIMILISATION_TABLE," +
	 			"CVV_REQUIRED,CVV_LENGTH,CVK1,CVK2,PVK1,PVK2,GEN_METHOD,PVKI,PANVALIDATION_LENGTH," +
	 			"EPVK,DESLENGTH,PINMAILER_ID,SEC_CUR,FLOAT_GL_CODE,CARDNO,CUSTOMER_ID,BK_NIR_GL_CODE,MB_NIR_GL_CODE,AUTH_CODE,MKCK_STATUS,MAKER_ID,MAKER_DATE) VALUES " +
	 			"('"+instid+"','"+binbean.getBinvalue()+"','"+binbean.getBindesc()+"','"+binbean.getChnlenght()+"','"+binbean.getAttachbranchcode()+"'," +
	 			 "'"+binbean.getAttachbranchcode()+"'," + "'"+binbean.getBaselength()+"','"+binbean.getNumberofcardsgen()+"','"+binbean.getHsmid()+"','"+binbean.getPinverificationkey()+"'," +
	 			 "'"+binbean.getPinlength()+"','"+ binbean.getPanoffset()+"','"+binbean.getPanvalidationlength()+"','"+binbean.getPinpadchar()+"'," +
	 			 "'"+binbean.getDecimilsationtable()+"'," +"'"+binbean.getCvvrequired()+"','"+binbean.getCvvlength()+"','"+binbean.getCvk1()+"','"+binbean.getCvk2()+"'," +
	 			 "'"+binbean.getPinverificationkey1()+"','"+binbean.getPinverificationkey2()+"','"+binbean.getPingentype()+"','"+binbean.getPinverificationkeyindex()+"'," +
	 			 "'"+binbean.getPanvalidationlength()+"','"+binbean.getPinenclength()+"','"+binbean.getPinmailer()+"','"+binbean.getSecondarycur()+"'," +
	 			 "000','000','000','000','000','"+binbean.getAuthstatus()+"','"+binbean.getMkckstatus()+"','"+usercode+"',sysdate)";
		enctrace("addbin_qurytemp : "+addbin_qury);		  
		result = jdbctemplate.update(addbin_qury);	
		return result; 
	}*/
	
	
	public int ccy_inst_query(String instid, String bin,String[] multi_currency_code,int i, String minimum_amount, String maximum_amount,JdbcTemplate jdbctemplate)
	{
	int check_status=-1;
	String ccy_inst_query="INSERT INTO IFP_BIN_CURRENCY (INST_ID, BIN, CUR_CODE, CUR_PRIORITY, MIN_AMOUNT, MAX_AMOUNT) VALUES ('"+instid+"', '"+bin+"', '"+multi_currency_code[i]+"', 'S', '"+minimum_amount+"', '"+maximum_amount+"')";
	  enctrace("ccy_inst_query "+ccy_inst_query);
	  check_status=jdbctemplate.update(ccy_inst_query);
	 
	 return check_status;
	} 
	
	public List<?> binlist(String instid,JdbcTemplate jdbctemplate)
	{
		
		/*String qury="select * from PRODUCTINFO where inst_id='"+instid+"' AND AUTH_CODE='1' AND DELETED_FLAG !='2' ";
		enctrace(qury);
		List<?> binlist =jdbctemplate.queryForList(qury);*/
		
		//by gowtham-190819
		String qury="select * from PRODUCTINFO where inst_id=? AND AUTH_STATUS=?";
		enctrace(qury);
		List<?> binlist =jdbctemplate.queryForList(qury,new Object[]{instid,"1","2",});
		
		return binlist;
	}
	public List<?> binlistForMaker(String instid,JdbcTemplate jdbctemplate)
	{
		
		/*String qury="select * from PRODUCTINFO where inst_id='"+instid+"' AND DELETED_FLAG !='2'  ";
		enctrace(qury);
		List<?> binlist =jdbctemplate.queryForList(qury);*/
		
		//by gowtham-190819
		String qury="select * from PRODUCTINFO where inst_id=?";
		enctrace(qury);
		List<?> binlist =jdbctemplate.queryForList(qury,new Object[]{instid,"2"});
		return binlist;
	}
	
	public List<?> bindetails(String bin,String instid,JdbcTemplate jdbctemplate)
	{
		/*String bindetails_query="select INST_ID,BIN,CHNLEN,DECODE(ATTACH_PRODTYPE_CARDTYPE,'C','CARD TYPE','P','PRODUCT CODE','N','NO') AS ATTACH_PRODTYPE_CARDTYPE,CASE ATTACH_BRCODE WHEN 'N' THEN 'NO' ELSE 'YES' END as ATTACH_BRCODE,BASELEN,NOS_CARDS_GEN,HSM_ID," +
		"PVK,PIN_LENGTH,PAN_OFFSET,PINOFFSET_LENGTH,PANPADCHAR,DECIMILISATION_TABLE,DECODE(CVV_REQUIRED,'N','NO','Y','YES') AS CVV_REQUIRED,CVV_LENGTH,CVK1,CVK2,PVK1,PVK2,GEN_METHOD,PVKI,PANVALIDATION_LENGTH,EPVK," +
		"DESLENGTH,PINMAILER_ID,CHN_BASE_NO,DECODE(SEC_CUR,'1','YES','0','NO') AS SEC_CUR ,ACCT_LEN,ACCT_SEQ_NO,BIN_DESC,DECODE(AUTH_CODE,'0','Waiting for authorize','1','Authorized','9','DeAuthorized') AS AUTH_CODE,MAKER_ID,NVL(REMARKS,'--') AS REMARKS, CHECKER_ID, to_char(CHECKER_DATE,'DD-MON-YYYY') AS CHECKER_DATE, TO_CHAR(MAKER_DATE,'DD-MON-YYYY') AS MAKER_DATE,SERVICE_CODE, DECODE(CARD_TYPE,'M','Magnatic Card','C','Chip Card')CARD_TYPE, DECODE(SEQ_OPTION,'P','Pin Only','PC','Pin and CVV','C','CVV Only')SEQ_OPTION,DECODE(INTAMAPTYPE,'WEBSRV','FETCH DATA USING WEB SERVICE','CBSLINK',' FETCH DATA USING CBS LINK','SCHEMA','FETCH DATA USING OTHER SCHEMA')INTAMAPTYPE" +
		" from PRODUCTINFO where bin='"+bin+"' and inst_id='"+instid+"'";
		enctrace("bindetails_query===> "+bindetails_query);
		List<?> bindetails = jdbctemplate.queryForList(bindetails_query);*/
		
		//by gowtham-190819
		String bindetails_query="select INST_ID,BIN,CHNLEN,DECODE(ATTACH_PRODTYPE_CARDTYPE,'C','CARD TYPE','P','PRODUCT CODE','N','NO') AS ATTACH_PRODTYPE_CARDTYPE,CASE ATTACH_BRCODE WHEN 'N' THEN 'NO' ELSE 'YES' END as ATTACH_BRCODE,BASELEN,NOS_CARDS_GEN,HSM_ID," +
				"PVK,PIN_LENGTH,PAN_OFFSET,PINOFFSET_LENGTH,PANPADCHAR,DECIMILISATION_TABLE,DECODE(CVV_REQUIRED,'N','NO','Y','YES') AS CVV_REQUIRED,CVV_LENGTH,CVK1,CVK2,PVK1,PVK2,GEN_METHOD,PVKI,PANVALIDATION_LENGTH,EPVK," +
				"DESLENGTH,PINMAILER_ID,CHN_BASE_NO,DECODE(SEC_CUR,'1','YES','0','NO') AS SEC_CUR ,ACCT_LEN,ACCT_SEQ_NO,BIN_DESC,DECODE(AUTH_CODE,'0','Waiting for authorize','1','Authorized','9','DeAuthorized') AS AUTH_CODE,MAKER_ID,NVL(REMARKS,'--') AS REMARKS, CHECKER_ID, to_char(CHECKER_DATE,'DD-MON-YYYY') AS CHECKER_DATE, TO_CHAR(MAKER_DATE,'DD-MON-YYYY') AS MAKER_DATE,SERVICE_CODE, DECODE(CARD_TYPE,'M','Magnatic Card','C','Chip Card')CARD_TYPE, DECODE(SEQ_OPTION,'P','Pin Only','PC','Pin and CVV','C','CVV Only')SEQ_OPTION,DECODE(INTAMAPTYPE,'WEBSRV','FETCH DATA USING WEB SERVICE','CBSLINK',' FETCH DATA USING CBS LINK','SCHEMA','FETCH DATA USING OTHER SCHEMA')INTAMAPTYPE" +
				" from PRODUCTINFO where PRD_CODE=? and inst_id=?";
				enctrace("bindetails_query===> "+bindetails_query);
				List<?> bindetails = jdbctemplate.queryForList(bindetails_query,new Object[]{bin,instid});
		
		
		return bindetails;
	}
	
	
	
	public List multiccy_list(String instid,String bin,JdbcTemplate jdbctemplate) throws Exception
	{
		/*String multicurr = "SELECT CUR_CODE,MIN_AMOUNT,MAX_AMOUNT FROM IFP_BIN_CURRENCY WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
		enctrace("multicurr -----   "+multicurr);
		List multiccy_list = jdbctemplate.queryForList(multicurr);*/
		
		//by gowtham-190819
		String multicurr = "SELECT CUR_CODE,MIN_AMOUNT,MAX_AMOUNT FROM IFP_BIN_CURRENCY WHERE INST_ID=? AND BIN=?";
		enctrace("multicurr -----   "+multicurr);
		List multiccy_list = jdbctemplate.queryForList(multicurr,new Object[]{instid,bin});
		
		return multiccy_list;
	}

	public String gethsmname(String instid,String hsmid,JdbcTemplate jdbctemplate) throws Exception
	{
		String ret_val = null;
		try {
			
			/*String qury_str  = "select HSMNAME from INST_HSM_MAP where INST_ID='"+instid+"' and HSM_ID='"+hsmid+"'";
			enctrace("qury_str==> "+qury_str);
			ret_val = (String)jdbctemplate.queryForObject(qury_str, String.class);*/
			
			//by gowtham-190819
			String qury_str  = "select HSM_DESC from HSM_TYPE where INST_ID=? and HSM_ID=?";
			enctrace("qury_str==> "+qury_str);
			ret_val = (String)jdbctemplate.queryForObject(qury_str,new Object[]{instid,hsmid}, String.class);
			
		} catch (EmptyResultDataAccessException e) {}
		return ret_val;
	} 

	public String getpinmailername(String instid,String pinmailerid,JdbcTemplate jdbctemplate)	{
		String ret_val = "N"; 
		try{
			
			/*String qury_str  = "select DISTINCT(PINMAILER_NAME) from PINMAILER_DESC where INST_ID='"+instid+"' and PINMAILER_ID='"+pinmailerid+"' AND STATUS='1'";
			enctrace("qury_str==> "+qury_str);
			ret_val = (String)jdbctemplate.queryForObject(qury_str, String.class);*/
			
			//by gowtham-190819
			String qury_str  = "select DISTINCT(PINMAILER_NAME) from PINMAILER_DESC where INST_ID=? and PINMAILER_ID=? AND STATUS=?";
			enctrace("qury_str==> "+qury_str);
			ret_val = (String)jdbctemplate.queryForObject(qury_str,new Object[]{instid,pinmailerid,"1"}, String.class);
			
		}catch(EmptyResultDataAccessException e ){}
		return ret_val;
	}

	public String getcurrencydesc(String ccycode,JdbcTemplate jdbctemplate)
	{
		/*String curdesc = "Select CURRENCY_DESC from GLOBAL_CURRENCY where CURRENCY_CODE='"+ccycode+"'  OR NUMERIC_CODE='"+ccycode+"'";
		enctrace("curdesc==> "+curdesc);
		String ret_val = (String)jdbctemplate.queryForObject(curdesc, String.class);*/
		
		//by gowtham-190819
		String curdesc = "Select CURRENCY_DESC from GLOBAL_CURRENCY where CURRENCY_CODE=?  OR NUMERIC_CODE=?";
		enctrace("curdesc==> "+curdesc);
		String ret_val = (String)jdbctemplate.queryForObject(curdesc,new Object[]{ccycode,ccycode} ,String.class);
		
		return ret_val;
	}
	

	
	public int checkingBininproduction(String inst_id, String bin_no,JdbcTemplate jdbctemplate)
	{
		int isBinExsist = -1;
		//JdbcTemplate jdbctemplate = new JdbcTemplate(dataSource);
		//String check_bin = "select count(distinct (substr(card_no,1,6))) from ifpcard_production  WHERE INST_ID='"+inst_id+"' and substr(card_no,1,6) = '"+bin_no+"'";
		
		//by gowtham-190819
		//String check_bin="select COUNT(*) from CARD_PRODUCTION where INST_ID='"+inst_id+"' and BIN='"+bin_no+"'";
		String check_bin="select COUNT(*) from CARD_PRODUCTION where INST_ID=? and BIN=?";
		System.out.println(" check_bin query is "+check_bin);
		try
		{
			//int bin_count = jdbctemplate.queryForInt(check_bin);
			int bin_count = jdbctemplate.queryForInt(check_bin,new Object[]{inst_id,bin_no});
			System.out.println("Value from the Query is "+bin_count);
			if(bin_count > 0)
			{
				isBinExsist = 1;
			}
			else
			{
				isBinExsist = 0;
			}
		}
		catch (Exception e) 
		{
			System.out.println("The Exception in Bin Counting "+e.getMessage());
			isBinExsist = -2;
		}
		System.out.println("The Retunr value of checkingBininproduction "+isBinExsist);
		return isBinExsist;
	} 

	//temprary code........................
	public String getCurrencyDesc( String curcode,JdbcTemplate jdbctemplate ){
		
		/*String sec_curdesc = "SELECT CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE CURRENCY_CODE='"+curcode+"'  OR NUMERIC_CODE='"+curcode+"' and rownum <= 1";
		System.out.println("sec_cur_req___" + sec_curdesc);
		//JdbcTemplate jdbctemplate = new JdbcTemplate(dataSource);
		String sec_cur_det =(String)jdbctemplate.queryForObject(sec_curdesc,String.class);*/
		
		//by gowtham-190819
		String sec_curdesc = "SELECT CURRENCY_DESC FROM GLOBAL_CURRENCY WHERE CURRENCY_CODE=?  OR NUMERIC_CODE=? and rownum <= ?";
		System.out.println("sec_cur_req___" + sec_curdesc);
		//JdbcTemplate jdbctemplate = new JdbcTemplate(dataSource);
		String sec_cur_det =(String)jdbctemplate.queryForObject(sec_curdesc,new Object[]{curcode,curcode,"1"},String.class);
		
		return sec_cur_det;
	}
	
	public List<?> multicurrency_qury(String instid,JdbcTemplate jdbctemplate)
	{
	List<?> selectlist=null;	
	
	/*String multicurrency_qury="select CUR_CODE from INSTITUTION_CURRENCY WHERE INST_ID='"+instid+"'";
	System.out.println( "multicurrency_qury"+multicurrency_qury);
	selectlist =jdbctemplate.queryForList(multicurrency_qury);*/
	
	//by gowtham-170819
	String multicurrency_qury="select CUR_CODE from INSTITUTION_CURRENCY WHERE INST_ID=?";
	System.out.println( "multicurrency_qury"+multicurrency_qury);
	selectlist =jdbctemplate.queryForList(multicurrency_qury,new Object[]{instid});
	
	System.out.println("List From DB===>"+selectlist);
	return selectlist;
	} 
	
	public List<?> multicurrency_qurynew(String instid,JdbcTemplate jdbctemplate)
	{
	List<?> selectlist=null;
	
	/*String multicurrency_qury="select * from PRODUCTINFO where inst_id='"+instid+"' AND MKCK_STATUS='M'";
	System.out.println( "multicurrency_qurynew--->"+multicurrency_qury);
	selectlist =jdbctemplate.queryForList(multicurrency_qury);*/
	
	//by gowtham-170819
	String multicurrency_qury="select * from PRODUCTINFO where inst_id=?";
	System.out.println( "multicurrency_qurynew--->"+multicurrency_qury);
	selectlist =jdbctemplate.queryForList(multicurrency_qury,new Object[]{instid,"M"});
	
	System.out.println("multicurrency_qurynew From DB===>"+selectlist);
	return selectlist;
	} 

	public String getbindesc(String inst_Name,String bin,JdbcTemplate jdbcTemplate)
	{
		String productcode = null;
		String bindesc = "Select BIN_DESC from PRODUCTINFO where PRD_CODE='"+productcode+"' and INST_ID='"+inst_Name+"'";
		enctrace("bin==> "+bindesc);
		String ret_val = (String)jdbcTemplate.queryForObject(bindesc, String.class);
		return ret_val;
	}
	public int updateAuthBin(String authstatus,String userid,String instid,String bin, String remarks, JdbcTemplate jdbctemplate ) throws Exception  {
		int x = -1;
		
		Date date =  new Date();
		
		/*String update_authdeauth_qury = "UPDATE PRODUCTINFO SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='1',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
		enctrace ( "update_authdeauth_qury : "+ update_authdeauth_qury );
		x = jdbctemplate.update(update_authdeauth_qury);*/
		
		//by gowtham-170819
		String update_authdeauth_qury = "UPDATE PRODUCTINFO SET PRD_DESC=?, AUTH_STATUS=?,AUTH_DATE=?,AUTH_USER=?,REMARK=?WHERE INST_ID=? AND PRD_CODE=?";
		enctrace ( "update_authdeauth_qury : "+ update_authdeauth_qury);
		x = jdbctemplate.update(update_authdeauth_qury,new Object[]{authstatus,"1",date.getCurrentDate(),userid,remarks,instid,bin});
		
		return x; 
	}
	
	public int moveToMainBin( String instid, String bin,  JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String productcode = null;
		String binmoveqry = "INSERT INTO PRODUCTINFO SELECT * FROM PRODUCTINFO WHERE inst_id='"+instid+"' AND PRD_CODE='"+productcode+"'" ;
			   enctrace("binmoveqry :" + binmoveqry );
		x = jdbctemplate.update(binmoveqry);
		return x;  
	}
	
 
		
	/*public int updateBinTempRecords(String instid, String usercode, String bin, String bindesc, String tablename, AddBinActionBeans binbean, JdbcTemplate jdbctemplate ) throws Exception {
		int result=-1;	
		String updateqry = "UPDATE PRODUCTINFO SET BIN_DESC='"+bindesc+"', HSM_ID='"+binbean.getHsmid()+"', PVK='"+binbean.getPinverificationkey()+"', ";
			   updateqry += " MKCK_STATUS='"+binbean.getMkckstatus()+"', MAKER_ID='"+usercode+"', MAKER_DATE='sysdate' ";
		       updateqry += " WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
		enctrace("update bin qry : "+ updateqry );		  
		result = jdbctemplate.update(updateqry);	
		return result; 
	} */
	
	public int updateDeAuthBin(String authstatus,String userid,String remarks,String instid,String bin, JdbcTemplate jdbctemplate) {
		int x = -1;
		
		Date date = new Date(); 
		/*String update_authdeauth_qury = "UPDATE PRODUCTINFO SET MKCK_STATUS='"+authstatus+"', AUTH_CODE='9',CHECKER_DATE=sysdate,CHECKER_ID='"+userid+"',REMARKS='"+remarks+"', DELETED_FLAG ='0'  WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
		enctrace( "update_authdeauth_qury " +  update_authdeauth_qury );
		x = jdbctemplate.update(update_authdeauth_qury );*/

		//by gowtham-170819
		String update_authdeauth_qury = "UPDATE PRODUCTINFO SET PRD_DESC=?, AUTH_STATUS=?,AUTH_DATE=?,AUTH_USER=?,REMARK=? WHERE INST_ID=? AND PRD_CODE=?";
		enctrace( "update_authdeauth_qury " +  update_authdeauth_qury );
		x = jdbctemplate.update(update_authdeauth_qury,new Object[]{authstatus,"9",date.getCurrentDate(),userid,remarks,"0",instid,bin} );
		
		return x; 
	}
	public int getAuthexistCardTypeList(String instid,JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String del_qury="SELECT COUNT(*) FROM  PRODUCTINFO  where INST_ID='"+instid+"' and AUTH_STATUS='0'";
		enctrace( "del_qury : " + del_qury );
		x= jdbctemplate.queryForInt(del_qury);
		return x; 
	}
	
	
	
	/*public List binlistForChecker(String instid, JdbcTemplate jdbctemplate)
	{
		List<String> binlist = new ArrayList<>();
		String mailernameqry ="select * from PRODUCTINFO where inst_id='"+instid+"' and AUTH_CODE='0' AND MKCK_STATUS='M'";
		enctrace("mailernameqry :" + mailernameqry );
		binlist = jdbctemplate.queryForList(mailernameqry);
		trace("mailername--> "+binlist.size());
		return binlist;
		
		
		List selectlist=new ArrayList<>();	
		String multicurrency_qury="select * from PRODUCTINFO where inst_id='"+instid+"' and AUTH_CODE='0' AND MKCK_STATUS='M'";
		System.out.println( "multicurrency_qury"+multicurrency_qury);
		selectlist =jdbctemplate.queryForList(multicurrency_qury);
		System.out.println("List From DB===>"+selectlist);
		return selectlist;
		
	}*/
	
	/*public List<?> multicurrency_qury1(String instid,JdbcTemplate jdbctemplate)
	{
	List<?> selectlist=null;	
	String multicurrency_qury="select BIN from PRODUCTINFO where inst_id='"+instid+"' and AUTH_CODE='0' AND MKCK_STATUS='M'";
	System.out.println( "multicurrency_qury"+multicurrency_qury);
	selectlist =jdbctemplate.queryForList(multicurrency_qury);
	System.out.println("List From DB===>"+selectlist);
	return selectlist;
	} */
	
	public List<?> binlistForChecker(String instid,JdbcTemplate jdbctemplate)
	{
		List binlist=new ArrayList();
		
		/*String qury="select BIN,BIN_DESC from PRODUCTINFO where inst_id='"+instid+"' and AUTH_CODE='0' AND MKCK_STATUS='M'";
		enctrace(qury);
		binlist =jdbctemplate.queryForList(qury);*/
		
		//BY GOWTHAM-170819
		String qury="select PRD_CODE,PRD_DESC from PRODUCTINFO where inst_id=? and AUTH_STATUS=? ";
		enctrace(qury);
		binlist =jdbctemplate.queryForList(qury,new Object[]{instid,"0","M"});
		
		System.out.println("Getting Bin List"+binlist);
		return binlist;
	}
	
	public List<?> binlistForEdit(String instid, String usercode, JdbcTemplate jdbctemplate) throws Exception {
		
		/*String qury="select BIN,BIN_DESC from PRODUCTINFO where inst_id='"+instid+"'  AND DELETED_FLAG != '2'  ";
		enctrace(qury);
		List<?> binlist =jdbctemplate.queryForList(qury);*/
		
		//by gowtham 170819
		String qury="select PRD_CODE,PRD_DESC from PRODUCTINFO where inst_id=?";
		enctrace(qury);
		List<?> binlist =jdbctemplate.queryForList(qury,new Object[]{instid,"2"});
		return binlist;
	}
	
	public List<?> binlistForDeleteAuth(String instid, JdbcTemplate jdbctemplate) throws Exception {
		
		/*String qury="select BIN,BIN_DESC from PRODUCTINFO where inst_id='"+instid+"' AND DELETED_FLAG='2' AND AUTH_CODE='0'";
		enctrace(qury);
		List<?> binlist =jdbctemplate.queryForList(qury);*/

		//by gowtham-170819
		String qury="select PRD_CODE,PRD_DESC from PRODUCTINFO where inst_id=? AND AUTH_STATUS=?";
		enctrace(qury);
		List<?> binlist =jdbctemplate.queryForList(qury,new Object[]{instid,"2","0"});
		
		return binlist;
	}
	
	public List getBinDetailsForEdit(String instid, String bin, JdbcTemplate jdbctemplate ) throws Exception {
		List bindata = null;
		
		/*String bindetails = "SELECT * FROM PRODUCTINFO WHERE INST_ID='"+instid+"' AND BIN='"+bin+"' AND DELETED_FLAG != '2' ";
		enctrace("bindetails :" + bindetails);
		bindata = jdbctemplate.queryForList(bindetails);*/

		//by gowtham-170819
		String bindetails = "SELECT * FROM PRODUCTINFO WHERE INST_ID=? AND PRD_CODE=?";
		enctrace("bindetails :" + bindetails);
		bindata = jdbctemplate.queryForList(bindetails,new Object[]{instid,bin,"2"});
		
		return bindata;
		
	}
	
	public int updateBinRecords(String instid, String usercode, String bin, String bindesc, String tablename, AddBinActionBeans binbean, JdbcTemplate jdbctemplate ) throws Exception {
		int result=-1;	
		
		Date date = new Date();
		
		/*String updateqry =  " UPDATE "+tablename+" SET BIN_DESC='"+bindesc+"', CHNLEN='"+binbean.getChnlenght()+"', ATTACH_PRODTYPE_CARDTYPE='"+binbean.getAttachproductcode()+"', ";
			   updateqry += " ATTACH_BRCODE='"+binbean.getAttachbranchcode()+"', BASELEN='"+binbean.getBaselength()+"', NOS_CARDS_GEN='"+binbean.getNumberofcardsgen()+"', ";
			   updateqry += " HSM_ID='"+binbean.getHsmid()+"', PVK='"+binbean.getPinverificationkey()+"', PIN_LENGTH='"+binbean.getPinlength()+"', ";
			   updateqry += " PAN_OFFSET='"+binbean.getPanoffset()+"', CVV_REQUIRED='"+binbean.getCvvrequired()+"', CVV_LENGTH='"+binbean.getCvvlength()+"', ";
			   updateqry += " DECIMILISATION_TABLE='"+binbean.getDecimilsationtable()+"',";
			   updateqry += " CVK1='"+binbean.getCvk1()+"', CVK2='"+binbean.getCvk2()+"', PVK1='"+binbean.getPinverificationkey1()+"', ";
			   updateqry += " PVK2='"+binbean.getPinverificationkey2()+"', GEN_METHOD='"+binbean.getPingentype()+"', PVKI='"+binbean.getPinverificationkeyindex()+"', ";
			   updateqry += " PANVALIDATION_LENGTH='"+binbean.getPanvalidationlength()+"',  DESLENGTH='"+binbean.getPinenclength()+"', ";
			   updateqry += " PINMAILER_ID='"+binbean.getPinmailer()+"', CHN_BASE_NO='"+binbean.getBaselength()+"', ";
			   updateqry += " SERVICE_CODE='"+binbean.getServicecode()+"', CARD_TYPE='"+binbean.getMagcardorchip()+"', SEQ_OPTION='"+binbean.getSecoption()+"' , ";
			   updateqry += " AUTH_CODE='"+binbean.getAuthstatus()+"', MKCK_STATUS='"+binbean.getMkckstatus()+"', MAKER_ID='"+usercode+"', ";
			   updateqry += " MAKER_DATE=sysdate, REMARKS='', CHECKER_ID='', CHECKER_DATE=''";
			   updateqry += " WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
			   enctrace("update bin qry : "+ updateqry );		  
			   result = jdbctemplate.update(updateqry);	 */
		
		
		// by gowtham-170819
		String updateqry =  " UPDATE "+tablename+" SET BIN_DESC=?, CHNLEN=?, ATTACH_PRODTYPE_CARDTYPE=?,ATTACH_BRCODE=?, BASELEN=?, NOS_CARDS_GEN=?, ";
		updateqry += " HSM_ID=?, PVK=?, PIN_LENGTH=?,PAN_OFFSET=?, CVV_REQUIRED=?, CVV_LENGTH=?,DECIMILISATION_TABLE=?, ";
		updateqry += " CVK1=?, CVK2=?, PVK1=?,PVK2=?, GEN_METHOD=?, PVKI=?,PANVALIDATION_LENGTH=?,  DESLENGTH=?, ";
		updateqry += " PINMAILER_ID=?, CHN_BASE_NO=?, SERVICE_CODE=?, CARD_TYPE=?, SEQ_OPTION=? ,";
		updateqry += " AUTH_CODE=?, MKCK_STATUS=?, MAKER_ID=?,MAKER_DATE=?, REMARKS=?, CHECKER_ID=?, CHECKER_DATE=? ";
		updateqry += " WHERE INST_ID=? AND BIN=?";
		
		result = jdbctemplate.update(updateqry,new Object[]{bindesc,binbean.getChnlenght(),binbean.getAttachproductcode(),
				binbean.getAttachbranchcode(),binbean.getBaselength(),binbean.getNumberofcardsgen(),binbean.getHsmid(),binbean.getPinverificationkey(),binbean.getPinlength(),
				binbean.getPanoffset(),binbean.getCvvrequired(),binbean.getCvvlength(),binbean.getDecimilsationtable(),binbean.getCvk1(),
				binbean.getCvk2(),binbean.getPinverificationkey1(),binbean.getPinverificationkey2(),binbean.getPingentype(),binbean.getPinverificationkeyindex(),
				binbean.getPanvalidationlength(),binbean.getPinenclength(),binbean.getPinmailer(),binbean.getBaselength(),binbean.getServicecode(),
				binbean.getMagcardorchip(),binbean.getSecoption(),binbean.getAuthstatus(),binbean.getMkckstatus(),usercode,date.getCurrentDate(),
				"","","",instid,bin});	 
		
			   
			   return result;    
	}    
	
	public int checkBinExist(String instid, String bin, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String productcode = null;
		String existbinqry="SELECT COUNT(*) FROM  PRODUCTINFO  where INST_ID='"+instid+"' and PRD_CODE='"+productcode+"'";
		enctrace( "existbinqry : " + existbinqry );
		x= jdbctemplate.queryForInt( existbinqry );
		return x; 
	}
	
	public int checkBinExistTemp(String instid, String bin, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		
		/*String existbinqry="SELECT COUNT(*) FROM  PRODUCTINFO  where INST_ID='"+instid+"' and BIN='"+bin+"'";
		enctrace( "existbinqry : " + existbinqry );
		x= jdbctemplate.queryForInt( existbinqry );*/
		
		//BY GOWTHAM-190819
		String existbinqry="SELECT COUNT(*) FROM  PRODUCTINFO  where INST_ID=? and PRD_CODE=?";
		enctrace( "existbinqry : " + existbinqry );
		x= jdbctemplate.queryForInt( existbinqry ,new Object[]{instid,bin});
		
		return x; 
	}
	
	
		int x = -1;
		public int deleteBinFromProduction(String instid,String bin,JdbcTemplate jdbctemplate) throws Exception	{
		
		/*String qury="delete from PRODUCTINFO where inst_id='"+instid+"' and BIN='"+bin+"'";
		enctrace("qury "+qury);
		x = jdbctemplate.update(qury);*/

		//by gowtham-170819
		/*String update_authdeauth_qury = "INSERT INTO PRODUCTINFO_HIST SELECT * FROM PRODUCTINFO WHERE INST_ID=? AND PRD_CODE=?";
		enctrace ( "update_authdeauth_qury : "+ update_authdeauth_qury );
		x = jdbctemplate.update(update_authdeauth_qury,new Object[]{instid,bin});*/

		
		return x;
	}
	
	
	public int deleteBin(String instid, String tablename, String bin, String mkckstatus, String authstatus, String usercode, String remarks, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		
		Date date = new Date();
		/*String update_authdeauth_qury = "UPDATE "+tablename+" SET DELETED_FLAG='2', MKCK_STATUS='"+mkckstatus+"', AUTH_CODE='"+authstatus+"',MAKER_DATE=sysdate,MAKER_ID='"+usercode+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
		enctrace ( "update_authdeauth_qury : "+ update_authdeauth_qury );
		x = jdbctemplate.update(update_authdeauth_qury);*/
		
		//by gowtham-170819
		String update_authdeauth_qury = "UPDATE "+tablename+" SET DELETED_FLAG=?, MKCK_STATUS=?, AUTH_CODE=?,MAKER_DATE=?,MAKER_ID=?,REMARKS=? WHERE INST_ID=? AND BIN=?";
		enctrace ( "update_authdeauth_qury : "+ update_authdeauth_qury );
		x = jdbctemplate.update(update_authdeauth_qury,new Object[]{"2",mkckstatus,authstatus,date.getCurrentDate(),usercode,remarks,instid,bin});
		
		
		return x; 
	}
	
	public int insertIntoBinHistory(String instid,  String bin, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1; 
		
		/*String update_authdeauth_qury = "INSERT INTO PRODUCTBIN_REL_HIST SELECT * FROM PRODUCTBIN_REL WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
		enctrace ( "update_authdeauth_qury : "+ update_authdeauth_qury );
		x = jdbctemplate.update(update_authdeauth_qury);*/
		
		
		//by gowtham-170819
		String update_authdeauth_qury = "INSERT INTO PRODUCTBIN_REL_HIST SELECT * FROM PRODUCTBIN_REL WHERE INST_ID=? AND BIN=?";
		enctrace ( "update_authdeauth_qury : "+ update_authdeauth_qury );
		x = jdbctemplate.update(update_authdeauth_qury,new Object[]{instid,bin});

		
		return x; 
	}
	
	 
	public int deleteProduct(String instid, String tablename, String bin, String mkckstatus, String deletedflag, String authstatus, String usercode, String remarks, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		
		Date date=new Date();
		/*String update_authdeauth_qury = "UPDATE "+tablename+" SET DELETED_FLAG='"+deletedflag+"', MKCK_STATUS='"+mkckstatus+"', AUTH_CODE='"+authstatus+"',MAKER_DATE=sysdate,MAKER_ID='"+usercode+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
		enctrace ( "update_authdeauth_qury : "+ update_authdeauth_qury );
		x = jdbctemplate.update(update_authdeauth_qury);*/

		//by gowtham-170819
		String update_authdeauth_qury = "UPDATE "+tablename+" SET DELETED_FLAG=?, MKCK_STATUS=?, AUTH_CODE=?,MAKER_DATE=?,MAKER_ID=?,REMARKS=? WHERE INST_ID=? AND BIN=?";
		enctrace ( "update_authdeauth_qury : "+ update_authdeauth_qury );
		x = jdbctemplate.update(update_authdeauth_qury,new Object[]{deletedflag,mkckstatus,authstatus,date.getCurrentDate(),usercode,remarks,instid,bin});
		return x; 
	}
	
	public int deleteProductAuth(String instid, String tablename, String bin, String mkckstatus, String deletedflag, String authstatus, String usercode, String remarks, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		String update_authdeauth_qury = "UPDATE PRODUCT_MASTER SET DELETED_FLAG='"+deletedflag+"', MKCK_STATUS='"+mkckstatus+"', AUTH_CODE='"+authstatus+"',CHECKER_DATE=sysdate,CHECKER_ID='"+usercode+"',REMARKS='"+remarks+"' WHERE INST_ID='"+instid+"' AND BIN='"+bin+"'";
		enctrace ( "update_authdeauth_qury : "+ update_authdeauth_qury );
		x = jdbctemplate.update(update_authdeauth_qury);
		return x; 
	}
	
	public List<?> getAcctTypes(String instid, JdbcTemplate jdbctemplate)
	{
		List<?> accttypes = null;
		String accttypesqry ="SELECT ACCT_FLAG,ACCTTYPEDESC FROM ACCT_TYPES WHERE INST_ID='"+instid+"' AND STATUS='1'";
		enctrace("accttypesqry :" + accttypesqry );
		accttypes = jdbctemplate.queryForList(accttypesqry);
		trace("accttypes--> "+accttypes.size());
		return accttypes;
	}
	
	public List<?> getDta(String instid,JdbcTemplate jdbctemplate)
	{
		System.out.println("jdbc "+jdbctemplate);
		
		List binlist=new ArrayList();
		
		String qury="select BIN from PRODUCTINFO where inst_id='"+instid+"' and AUTH_STATUS='0'";
		enctrace(qury);
		 binlist =jdbctemplate.queryForList(qury);
		System.out.println("Getting Bin List"+binlist);
		return binlist;
	}
	
	
	
}
