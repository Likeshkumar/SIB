package com.ifg.Config.HSM;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;

import test.Date;

/*public class HsmActionDao extends BaseAction{
	public int HsmNameExist(String hsmname,JdbcTemplate jdbctemplate ) {
		int x = -1;
		String hsm_exist_query="select count(*) from HSM_DETAILS where HSMNAME='"+hsmname.toUpperCase()+"'  and AUTH_CODE !='9'";
		enctrace("Hsm Name exist ..."+hsm_exist_query);
		x =jdbctemplate.queryForInt(hsm_exist_query);
		return x;  
	}
	public int getMaxHsmID(JdbcTemplate jdbctemplate) {
		int hsmid;
		String max_id_query="select max(to_number(HSM_ID)) from HSM_DETAILS";
		enctrace("Getting Max HsmId .."+max_id_query);
		hsmid = jdbctemplate.queryForInt(max_id_query);   
		return hsmid;
	}
	public int insertHsmDetailsMain(String hsmname,String hsmprotocol,String hsmtype,String hsmip,String hsmport,String headlength,String hsmheadertype,String hsmhedlen,String hsmtimeout,String hsmconinterval,String hsmstatus,int hsmid,JdbcTemplate jdbctemplate,String authcode,String usercode) {
		String hsm_insert_query="INSERT INTO HSM_DETAILS (HSMNAME,HSMPROTOCOL,HSMTYPE,HSMADDRESS,HSMPORT,HEADERLEN,HEADERTYPE,HSMHEADERLEN,HSMTIMEOUT,CONNECTIONINTERVAL,HSMSTATUS,HSM_ID,AUTH_CODE,ADDED_BY,ADDED_DATE)";
		hsm_insert_query+=" VALUES ('"+hsmname.toUpperCase()+"','"+hsmprotocol+"','"+hsmtype+"','"+hsmip+"','"+hsmport+"','"+headlength+"','"+hsmheadertype+"','"+hsmhedlen+"','"+hsmtimeout+"','"+hsmconinterval+"','"+hsmstatus+"','"+hsmid+"','"+authcode+"','"+usercode+"',sysdate)";
		enctrace("Main query Inserting Hsm Details .. "+hsm_insert_query);
		int ipsert_hsm = jdbctemplate.update(hsm_insert_query);
		return ipsert_hsm;
	}
	public int insertHsmDetailsTemp(String hsmname,String hsmprotocol,String hsmtype,String hsmip,String hsmport,String headlength,String hsmheadertype,String hsmhedlen,String hsmtimeout,String hsmconinterval,String hsmstatus,int hsmid,JdbcTemplate jdbctemplate,String authcode,String usercode) {
		String hsm_insert_query="INSERT INTO HSM_DETAILS (HSMNAME,HSMPROTOCOL,HSMTYPE,HSMADDRESS,HSMPORT,HEADERLEN,HEADERTYPE,HSMHEADERLEN,HSMTIMEOUT,CONNECTIONINTERVAL,HSMSTATUS,HSM_ID,AUTH_CODE,ADDED_BY,ADDED_DATE)";
		hsm_insert_query+=" VALUES ('"+hsmname.toUpperCase()+"','"+hsmprotocol+"','"+hsmtype+"','"+hsmip+"','"+hsmport+"','"+headlength+"','"+hsmheadertype+"','"+hsmhedlen+"','"+hsmtimeout+"','"+hsmconinterval+"','"+hsmstatus+"','"+hsmid+"','"+authcode+"','"+usercode+"',sysdate)";
		enctrace("Temp query Inserting Hsm Details .. "+hsm_insert_query);
		int ipsert_hsm = jdbctemplate.update(hsm_insert_query);
		return ipsert_hsm;
	}
	public List gethsmDetailsTemp(JdbcTemplate jdbctemplate) {
		String qury="select HSM_ID,HSMNAME from HSM_DETAILS order by HSM_ID";
		enctrace("Query getting Hsm Details .."+qury);
		List hsmdetailslist =jdbctemplate.queryForList(qury);
		return hsmdetailslist;
	}
	public List gethsmDetailsTempByHsmID(String hsm_id,JdbcTemplate jdbctemplate) {
		String qury="select HSMNAME,HSMPROTOCOL,HSMTYPE,HSMADDRESS,HSMPORT,HEADERLEN,HEADERTYPE,HSMHEADERLEN,HSMTIMEOUT,CONNECTIONINTERVAL,DECODE(HSMSTATUS,'0','DISABLED','1','ENABLED') AS HSMSTATUS,HSM_ID,DECODE(AUTH_CODE,'0','Waiting for authorization','1','Authorized','9','Rejected') AS AUTH_CODE,nvl(to_char(AUTH_DATE,'dd-mm-yyyy'),'--') as AUTH_DATE,NVL(AUTH_BY,'--') AS AUTH_BY,NVL(ADDED_BY,'--') AS ADDED_BY,nvl(to_char(ADDED_DATE,'dd-mm-yyyy'),'--') as ADDED_DATE,NVL(REMARKS,'--') AS REMARKS from HSM_DETAILS where HSM_ID='"+hsm_id+"'";
		System.out.println("Getting HSM QUERY"+qury);
		List hsmdetail =jdbctemplate.queryForList(qury);
		return hsmdetail;
	}
	public List gethsmDetailsTempEditByHsmID(String hsm_id,JdbcTemplate jdbctemplate) {
		String qury="select HSMNAME,HSMPROTOCOL,HSMTYPE,HSMADDRESS,HSMPORT,HEADERLEN,HEADERTYPE,HSMHEADERLEN,HSMTIMEOUT,CONNECTIONINTERVAL,HSMSTATUS,HSM_ID,DECODE(AUTH_CODE,'0','Waiting for authorization','1','Authorized','9','Rejected') AS AUTH_CODE,nvl(to_char(AUTH_DATE,'dd-mm-yyyy'),'--') as AUTH_DATE,NVL(AUTH_BY,'--') AS AUTH_BY,NVL(ADDED_BY,'--') AS ADDED_BY,nvl(to_char(ADDED_DATE,'dd-mm-yyyy'),'--') as ADDED_DATE,NVL(REMARKS,'--') AS REMARKS from HSM_DETAILS where HSM_ID='"+hsm_id+"'";
		List hsmdetail =jdbctemplate.queryForList(qury);
		return hsmdetail;
	}
	public int updateHsmTemp(String HSMNAME,String HSMPROTOCOL,String HSMTYPE,String HSMADDRESS,String HSMPORT,String HEADERLEN,String HEADERTYPE,String HSMHEADERLEN,String HSMTIMEOUT,String CONNECTIONINTERVAL,String HSMSTATUS,String HSM_ID,JdbcTemplate jdbctemplate,String authcode,String usercode) {
		String query="update HSM_DETAILS set HSMNAME='"+HSMNAME.toUpperCase()+"',HSMPROTOCOL='"+HSMPROTOCOL.toUpperCase() +"',HSMTYPE='"+HSMTYPE.toUpperCase()+"',HSMADDRESS='"+HSMADDRESS+"',HSMPORT='"+HSMPORT+"',HEADERLEN='"+HEADERLEN+"',HEADERTYPE='"+HEADERTYPE+"',HSMHEADERLEN='"+HSMHEADERLEN+"',HSMTIMEOUT='"+HSMTIMEOUT+"',CONNECTIONINTERVAL='"+CONNECTIONINTERVAL+"',HSMSTATUS='"+HSMSTATUS+"',AUTH_CODE='"+authcode+"',ADDED_BY='"+usercode+"',ADDED_DATE=sysdate,AUTH_DATE='',AUTH_BY='',REMARKS='' where HSM_ID='"+HSM_ID.trim()+"'";
		System.out.println(query);
		int result = jdbctemplate.update(query);
		return result;
	}
	public int updateHsmMain(String HSMNAME,String HSMPROTOCOL,String HSMTYPE,String HSMADDRESS,String HSMPORT,String HEADERLEN,String HEADERTYPE,String HSMHEADERLEN,String HSMTIMEOUT,String CONNECTIONINTERVAL,String HSMSTATUS,String HSM_ID,JdbcTemplate jdbctemplate,String authcode,String usercode) {
		String query="update HSM_DETAILS set HSMNAME='"+HSMNAME.toUpperCase()+"',HSMPROTOCOL='"+HSMPROTOCOL.toUpperCase() +"',HSMTYPE='"+HSMTYPE.toUpperCase()+"',HSMADDRESS='"+HSMADDRESS+"',HSMPORT='"+HSMPORT+"',HEADERLEN='"+HEADERLEN+"',HEADERTYPE='"+HEADERTYPE+"',HSMHEADERLEN='"+HSMHEADERLEN+"',HSMTIMEOUT='"+HSMTIMEOUT+"',CONNECTIONINTERVAL='"+CONNECTIONINTERVAL+"',HSMSTATUS='"+HSMSTATUS+"',AUTH_CODE='"+authcode+"',ADDED_BY='"+usercode+"',ADDED_DATE=sysdate,AUTH_DATE='',AUTH_BY='',REMARKS='' where HSM_ID='"+HSM_ID.trim()+"'";
		System.out.println(query);
		int result = jdbctemplate.update(query);
		return result;
	}
	public List gethsmDetailsTempByUsername(String usercode,JdbcTemplate jdbctemplate) {
		String qury="select HSM_ID,HSMNAME from HSM_DETAILS where ADDED_BY!='"+usercode+"' and AUTH_CODE='0' order by HSM_ID";
		enctrace("Query getting Hsm Details for auth/deauth .."+qury);
		List hsmdetailslist =jdbctemplate.queryForList(qury);
		return hsmdetailslist;
	}
	public int updateAuthhsm(String userid,String hsmid,JdbcTemplate jdbctemplate) {
		String update_authdeauth_qury = "UPDATE HSM_DETAILS SET AUTH_CODE='1',AUTH_DATE=sysdate,AUTH_BY='"+userid+"' WHERE HSM_ID='"+hsmid+"'";
		enctrace("update to Authorize hsm .. "+update_authdeauth_qury);
		int update_authdeauth = jdbctemplate.update(update_authdeauth_qury);
		return update_authdeauth;
	}
	public int getmaintablestatus(String hsmid,JdbcTemplate jdbctemplate) {
		String count_main_qury = "SELECT COUNT(*) FROM HSM_DETAILS WHERE HSM_ID='"+hsmid.trim()+"'";
		System.out.println("--- count_main_qury --- "+count_main_qury);
		int cntinst = jdbctemplate.queryForInt(count_main_qury);
		return cntinst;  
	}
	public int insertHsmMaintable(String hsmid,JdbcTemplate jdbctemplate) {
		String insert_main_qury = "INSERT INTO HSM_DETAILS (HSMNAME,HSMPROTOCOL,HSMTYPE,HSMADDRESS,HSMPORT,HEADERLEN,HEADERTYPE,HSMHEADERLEN,HSMTIMEOUT,CONNECTIONINTERVAL,HSMSTATUS,HSM_ID,AUTH_CODE,AUTH_DATE,AUTH_BY,ADDED_BY,ADDED_DATE,REMARKS)" + "(SELECT HSMNAME,HSMPROTOCOL,HSMTYPE,HSMADDRESS,HSMPORT,HEADERLEN,HEADERTYPE,HSMHEADERLEN,HSMTIMEOUT,CONNECTIONINTERVAL,HSMSTATUS,HSM_ID,AUTH_CODE,AUTH_DATE,AUTH_BY,ADDED_BY,ADDED_DATE,REMARKS FROM HSM_DETAILS WHERE HSM_ID='"+hsmid+"')";
		System.out.println("--- insert_main_qury --- "+insert_main_qury);
		enctrace(" Inserting in main table .. "+insert_main_qury);
		int insertedmain = jdbctemplate.update(insert_main_qury);	
		return insertedmain;
	}  
	public int deleteHsmMaintable(String hsmid,JdbcTemplate jdbctemplate) {
		String deleteHsm = "DELETE FROM HSM_DETAILS where HSM_ID='"+hsmid+"'";
		int deleteHsm_result = jdbctemplate.update(deleteHsm);
		return deleteHsm_result;
	}
	public int updateDeAuthHsm(String userid,String remarks,String hsmid,JdbcTemplate jdbctemplate) {
		String update_authdeauth_qury = "UPDATE HSM_DETAILS SET AUTH_CODE='9',AUTH_DATE=sysdate,AUTH_BY='"+userid+"',REMARKS='"+remarks+"' where HSM_ID='"+hsmid+"'";
		int update_authdeauth = jdbctemplate.update(update_authdeauth_qury);
		return update_authdeauth;
	}*/
public class HsmActionDao extends BaseAction{
	public int HsmNameExist(String hsmname,JdbcTemplate jdbctemplate ) {
		int x = -1;
		/*String hsm_exist_query="select count(*) from HSM_DETAILS where HSMNAME='"+hsmname.toUpperCase()+"'  and AUTH_CODE !='9'";
		enctrace("Hsm Name exist ..."+hsm_exist_query);
		x =jdbctemplate.queryForInt(hsm_exist_query);*/
		
		//added by gowtham_120819
		String hsm_exist_query="select count(*) from HSM_DETAILS where HSMNAME=?  and AUTH_CODE !=?";
		enctrace("Hsm Name exist ..."+hsm_exist_query);
		x =jdbctemplate.queryForInt(hsm_exist_query,new Object[]{hsmname.toUpperCase(),"9"});
		return x;  
	}
	public int getMaxHsmID(JdbcTemplate jdbctemplate) {
		int hsmid;
		String max_id_query="select max(to_number(HSM_ID)) from HSM_DETAILS";
		enctrace("Getting Max HsmId .."+max_id_query);
		hsmid = jdbctemplate.queryForInt(max_id_query);   
		return hsmid;
	}
	public int insertHsmDetailsMain(String hsmname,String hsmprotocol,String hsmtype,String hsmip,String hsmport,String headlength,String hsmheadertype,String hsmhedlen,String hsmtimeout,String hsmconinterval,String hsmstatus,int hsmid,JdbcTemplate jdbctemplate,String authcode,String usercode) {
		/*String hsm_insert_query="INSERT INTO HSM_DETAILS (HSMNAME,HSMPROTOCOL,HSMTYPE,HSMADDRESS,HSMPORT,HEADERLEN,HEADERTYPE,HSMHEADERLEN,HSMTIMEOUT,CONNECTIONINTERVAL,HSMSTATUS,HSM_ID,AUTH_CODE,ADDED_BY,ADDED_DATE)";
		hsm_insert_query+=" VALUES ('"+hsmname.toUpperCase()+"','"+hsmprotocol+"','"+hsmtype+"','"+hsmip+"','"+hsmport+"','"+headlength+"','"+hsmheadertype+"','"+hsmhedlen+"','"+hsmtimeout+"','"+hsmconinterval+"','"+hsmstatus+"','"+hsmid+"','"+authcode+"','"+usercode+"',sysdate)";
		enctrace("Main query Inserting Hsm Details .. "+hsm_insert_query);
		int ipsert_hsm = jdbctemplate.update(hsm_insert_query);*/
		
		
		//added by gowtham_120819
		Date date =  new Date();
		String hsm_insert_query="INSERT INTO HSM_DETAILS (HSMNAME,HSMPROTOCOL,HSMTYPE,HSMADDRESS,HSMPORT,HEADERLEN,HEADERTYPE,HSMHEADERLEN,HSMTIMEOUT,CONNECTIONINTERVAL,HSMSTATUS,HSM_ID,AUTH_CODE,ADDED_BY,ADDED_DATE)";
		hsm_insert_query+=" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		enctrace("Main query Inserting Hsm Details .. "+hsm_insert_query);
		int ipsert_hsm = jdbctemplate.update(hsm_insert_query ,new Object[]{hsmname.toUpperCase(),hsmprotocol,hsmtype,hsmip,hsmport,headlength,hsmheadertype,hsmhedlen,hsmtimeout,hsmconinterval,hsmstatus,hsmid,authcode,usercode,date.getCurrentDate()});
		return ipsert_hsm;
	}
	public int insertHsmDetailsTemp(String hsmname,String hsmprotocol,String hsmtype,String hsmip,String hsmport,String headlength,String hsmheadertype,String hsmhedlen,String hsmtimeout,String hsmconinterval,String hsmstatus,int hsmid,JdbcTemplate jdbctemplate,String authcode,String usercode) {
		/*String hsm_insert_query="INSERT INTO HSM_DETAILS (HSMNAME,HSMPROTOCOL,HSMTYPE,HSMADDRESS,HSMPORT,HEADERLEN,HEADERTYPE,HSMHEADERLEN,HSMTIMEOUT,CONNECTIONINTERVAL,HSMSTATUS,HSM_ID,AUTH_CODE,ADDED_BY,ADDED_DATE)";
		hsm_insert_query+=" VALUES ('"+hsmname.toUpperCase()+"','"+hsmprotocol+"','"+hsmtype+"','"+hsmip+"','"+hsmport+"','"+headlength+"','"+hsmheadertype+"','"+hsmhedlen+"','"+hsmtimeout+"','"+hsmconinterval+"','"+hsmstatus+"','"+hsmid+"','"+authcode+"','"+usercode+"',sysdate)";
		enctrace("Temp query Inserting Hsm Details .. "+hsm_insert_query);
		int ipsert_hsm = jdbctemplate.update(hsm_insert_query);*/
		
		//added by gowtham_120819
		Date date =  new Date();
		String hsm_insert_query="INSERT INTO HSM_DETAILS (HSMNAME,HSMPROTOCOL,HSMTYPE,HSMADDRESS,HSMPORT,HEADERLEN,HEADERTYPE,HSMHEADERLEN,HSMTIMEOUT,CONNECTIONINTERVAL,HSMSTATUS,HSM_ID,AUTH_CODE,ADDED_BY,ADDED_DATE)";
		hsm_insert_query+=" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		enctrace("Temp query Inserting Hsm Details .. "+hsm_insert_query);
		int ipsert_hsm = jdbctemplate.update(hsm_insert_query,new Object[]{hsmname.toUpperCase(),hsmprotocol,hsmtype,hsmip,hsmport,headlength,hsmheadertype,hsmhedlen,hsmtimeout,hsmconinterval,hsmstatus,hsmid,authcode,usercode,date.getCurrentDate()});
		return ipsert_hsm;
	}
	public List gethsmDetailsTemp(JdbcTemplate jdbctemplate) {
		String qury="select HSM_ID,HSMNAME from HSM_DETAILS order by HSM_ID";
		enctrace("Query getting Hsm Details .."+qury);
		List hsmdetailslist =jdbctemplate.queryForList(qury);
		return hsmdetailslist;
	}
	public List gethsmDetailsTempByHsmID(String hsm_id,JdbcTemplate jdbctemplate) {
		/*String qury="select HSMNAME,HSMPROTOCOL,HSMTYPE,HSMADDRESS,HSMPORT,HEADERLEN,HEADERTYPE,HSMHEADERLEN,HSMTIMEOUT,CONNECTIONINTERVAL,DECODE(HSMSTATUS,'0','DISABLED','1','ENABLED') AS HSMSTATUS,HSM_ID,DECODE(AUTH_CODE,'0','Waiting for authorization','1','Authorized','9','Rejected') AS AUTH_CODE,nvl(to_char(AUTH_DATE,'dd-mm-yyyy'),'--') as AUTH_DATE,NVL(AUTH_BY,'--') AS AUTH_BY,NVL(ADDED_BY,'--') AS ADDED_BY,nvl(to_char(ADDED_DATE,'dd-mm-yyyy'),'--') as ADDED_DATE,NVL(REMARKS,'--') AS REMARKS from HSM_DETAILS where HSM_ID='"+hsm_id+"'";
		System.out.println("Getting HSM QUERY"+qury);
		List hsmdetail =jdbctemplate.queryForList(qury);*/
		
		//added by gowtham_120819
		String qury="select HSMNAME,HSMPROTOCOL,HSMTYPE,HSMADDRESS,HSMPORT,HEADERLEN,HEADERTYPE,HSMHEADERLEN,HSMTIMEOUT,CONNECTIONINTERVAL,DECODE(HSMSTATUS,'0','DISABLED','1','ENABLED') AS HSMSTATUS,HSM_ID,DECODE(AUTH_CODE,'0','Waiting for authorization','1','Authorized','9','Rejected') AS AUTH_CODE,nvl(to_char(AUTH_DATE,'dd-mm-yyyy'),'--') as AUTH_DATE,NVL(AUTH_BY,'--') AS AUTH_BY,NVL(ADDED_BY,'--') AS ADDED_BY,nvl(to_char(ADDED_DATE,'dd-mm-yyyy'),'--') as ADDED_DATE,NVL(REMARKS,'--') AS REMARKS from HSM_DETAILS where HSM_ID=?";
		System.out.println("Getting HSM QUERY"+qury);
		List hsmdetail =jdbctemplate.queryForList(qury,new Object[]{hsm_id});
		return hsmdetail;
	}
	public List gethsmDetailsTempEditByHsmID(String hsm_id,JdbcTemplate jdbctemplate) {
		/*String qury="select HSMNAME,HSMPROTOCOL,HSMTYPE,HSMADDRESS,HSMPORT,HEADERLEN,HEADERTYPE,HSMHEADERLEN,HSMTIMEOUT,CONNECTIONINTERVAL,HSMSTATUS,HSM_ID,DECODE(AUTH_CODE,'0','Waiting for authorization','1','Authorized','9','Rejected') AS AUTH_CODE,nvl(to_char(AUTH_DATE,'dd-mm-yyyy'),'--') as AUTH_DATE,NVL(AUTH_BY,'--') AS AUTH_BY,NVL(ADDED_BY,'--') AS ADDED_BY,nvl(to_char(ADDED_DATE,'dd-mm-yyyy'),'--') as ADDED_DATE,NVL(REMARKS,'--') AS REMARKS from HSM_DETAILS where HSM_ID='"+hsm_id+"'";
		List hsmdetail =jdbctemplate.queryForList(qury);*/
		
		//added by gowtham_120819
		String qury="select HSMNAME,HSMPROTOCOL,HSMTYPE,HSMADDRESS,HSMPORT,HEADERLEN,HEADERTYPE,HSMHEADERLEN,HSMTIMEOUT,CONNECTIONINTERVAL,HSMSTATUS,HSM_ID,DECODE(AUTH_CODE,'0','Waiting for authorization','1','Authorized','9','Rejected') AS AUTH_CODE,nvl(to_char(AUTH_DATE,'dd-mm-yyyy'),'--') as AUTH_DATE,NVL(AUTH_BY,'--') AS AUTH_BY,NVL(ADDED_BY,'--') AS ADDED_BY,nvl(to_char(ADDED_DATE,'dd-mm-yyyy'),'--') as ADDED_DATE,NVL(REMARKS,'--') AS REMARKS from HSM_DETAILS where HSM_ID=?";
		List hsmdetail =jdbctemplate.queryForList(qury,new Object[]{hsm_id});
		return hsmdetail;
	}
	public int updateHsmTemp(String HSMNAME,String HSMPROTOCOL,String HSMTYPE,String HSMADDRESS,String HSMPORT,String HEADERLEN,String HEADERTYPE,String HSMHEADERLEN,String HSMTIMEOUT,String CONNECTIONINTERVAL,String HSMSTATUS,String HSM_ID,JdbcTemplate jdbctemplate,String authcode,String usercode) {
		/*String query="update HSM_DETAILS set HSMNAME='"+HSMNAME.toUpperCase()+"',HSMPROTOCOL='"+HSMPROTOCOL.toUpperCase() +"',HSMTYPE='"+HSMTYPE.toUpperCase()+"',HSMADDRESS='"+HSMADDRESS+"',HSMPORT='"+HSMPORT+"',HEADERLEN='"+HEADERLEN+"',HEADERTYPE='"+HEADERTYPE+"',HSMHEADERLEN='"+HSMHEADERLEN+"',HSMTIMEOUT='"+HSMTIMEOUT+"',CONNECTIONINTERVAL='"+CONNECTIONINTERVAL+"',HSMSTATUS='"+HSMSTATUS+"',AUTH_CODE='"+authcode+"',ADDED_BY='"+usercode+"',ADDED_DATE=sysdate,AUTH_DATE='',AUTH_BY='',REMARKS='' where HSM_ID='"+HSM_ID.trim()+"'";
		System.out.println(query);
		int result = jdbctemplate.update(query);*/
		
		Date date =  new Date();
		String query="update HSM_DETAILS set HSMNAME=?,HSMPROTOCOL=?,HSMTYPE=?,HSMADDRESS=?,HSMPORT=?,HEADERLEN=?,HEADERTYPE=?,HSMHEADERLEN=?,HSMTIMEOUT=?,CONNECTIONINTERVAL=?,HSMSTATUS=?,AUTH_CODE=?,ADDED_BY=?,ADDED_DATE=?,AUTH_DATE=?,AUTH_BY=?,REMARKS=? where HSM_ID=?";
		System.out.println(query);
		int result = jdbctemplate.update(query,new Object[]{HSMNAME.toUpperCase(),HSMPROTOCOL.toUpperCase(),HSMTYPE.toUpperCase(),HSMADDRESS,HSMADDRESS,HSMPORT,HEADERLEN,HEADERTYPE,HSMHEADERLEN,HSMTIMEOUT,CONNECTIONINTERVAL,HSMSTATUS,authcode,date.getCurrentDate(),"","","",HSM_ID.trim()});
		
		return result;
	}
	public int updateHsmMain(String HSMNAME,String HSMPROTOCOL,String HSMTYPE,String HSMADDRESS,String HSMPORT,String HEADERLEN,String HEADERTYPE,String HSMHEADERLEN,String HSMTIMEOUT,String CONNECTIONINTERVAL,String HSMSTATUS,String HSM_ID,JdbcTemplate jdbctemplate,String authcode,String usercode) {
		/*String query="update HSM_DETAILS set HSMNAME='"+HSMNAME.toUpperCase()+"',HSMPROTOCOL='"+HSMPROTOCOL.toUpperCase() +"',HSMTYPE='"+HSMTYPE.toUpperCase()+"',HSMADDRESS='"+HSMADDRESS+"',HSMPORT='"+HSMPORT+"',HEADERLEN='"+HEADERLEN+"',HEADERTYPE='"+HEADERTYPE+"',HSMHEADERLEN='"+HSMHEADERLEN+"',HSMTIMEOUT='"+HSMTIMEOUT+"',CONNECTIONINTERVAL='"+CONNECTIONINTERVAL+"',HSMSTATUS='"+HSMSTATUS+"',AUTH_CODE='"+authcode+"',ADDED_BY='"+usercode+"',ADDED_DATE=sysdate,AUTH_DATE='',AUTH_BY='',REMARKS='' where HSM_ID='"+HSM_ID.trim()+"'";
		System.out.println(query);
		int result = jdbctemplate.update(query);*/
		
		
		//added by gowtham_120819
		Date date =  new Date();
		String query="update HSM_DETAILS set HSMNAME=?,HSMPROTOCOL=?,HSMTYPE=?,HSMADDRESS=?,HSMPORT=?,HEADERLEN=?,HEADERTYPE=?,HSMHEADERLEN=?,HSMTIMEOUT=?,CONNECTIONINTERVAL=?,HSMSTATUS=?,AUTH_CODE=?,ADDED_BY=?,ADDED_DATE=?,AUTH_DATE=?,REMARKS=? where HSM_ID=?";
		System.out.println(query);
		int result = jdbctemplate.update(query,new Object[]{HSMNAME.toUpperCase(),HSMPROTOCOL.toUpperCase(),HSMTYPE.toUpperCase(),HSMADDRESS,HSMADDRESS,HSMPORT,HEADERLEN,HEADERTYPE,HSMHEADERLEN,HSMTIMEOUT,CONNECTIONINTERVAL,HSMSTATUS,authcode,date.getCurrentDate(),"","","",HSM_ID.trim()});
		return result;
	}
	public List gethsmDetailsTempByUsername(String usercode,JdbcTemplate jdbctemplate) {
		/*String qury="select HSM_ID,HSMNAME from HSM_DETAILS where ADDED_BY!='"+usercode+"' and AUTH_CODE='0' order by HSM_ID";
		enctrace("Query getting Hsm Details for auth/deauth .."+qury);
		List hsmdetailslist =jdbctemplate.queryForList(qury);*/
		
		//ADDED BY GOWTHAM_120819
		String qury="select HSM_ID,HSMNAME from HSM_DETAILS where ADDED_BY!=? and AUTH_CODE=? order by HSM_ID";
		enctrace("Query getting Hsm Details for auth/deauth .."+qury);
		List hsmdetailslist =jdbctemplate.queryForList(qury,new Object[]{usercode,"0"});
		return hsmdetailslist;
	}
	public int updateAuthhsm(String userid,String hsmid,JdbcTemplate jdbctemplate) {
		Date date =  new Date();
		/*String update_authdeauth_qury = "UPDATE HSM_DETAILS SET AUTH_CODE='1',AUTH_DATE=sysdate,AUTH_BY='"+userid+"' WHERE HSM_ID='"+hsmid+"'";
		enctrace("update to Authorize hsm .. "+update_authdeauth_qury);
		int update_authdeauth = jdbctemplate.update(update_authdeauth_qury);*/
		
		//ADDED BY GOWTHAM_120819
		String update_authdeauth_qury = "UPDATE HSM_DETAILS SET AUTH_CODE=?,AUTH_DATE=?,AUTH_BY=? WHERE HSM_ID=?";
		enctrace("update to Authorize hsm .. "+update_authdeauth_qury);
		int update_authdeauth = jdbctemplate.update(update_authdeauth_qury,new Object[]{"1",date.getCurrentDate(),userid,hsmid});
		return update_authdeauth;
	}
	public int getmaintablestatus(String hsmid,JdbcTemplate jdbctemplate) {
		String count_main_qury = "SELECT COUNT(*) FROM HSM_DETAILS WHERE HSM_ID='"+hsmid.trim()+"'";
		System.out.println("--- count_main_qury --- "+count_main_qury);
		int cntinst = jdbctemplate.queryForInt(count_main_qury);
		return cntinst;  
	}
	public int insertHsmMaintable(String hsmid,JdbcTemplate jdbctemplate) {
		String insert_main_qury = "INSERT INTO HSM_DETAILS (HSMNAME,HSMPROTOCOL,HSMTYPE,HSMADDRESS,HSMPORT,HEADERLEN,HEADERTYPE,HSMHEADERLEN,HSMTIMEOUT,CONNECTIONINTERVAL,HSMSTATUS,HSM_ID,AUTH_CODE,AUTH_DATE,AUTH_BY,ADDED_BY,ADDED_DATE,REMARKS)" + "(SELECT HSMNAME,HSMPROTOCOL,HSMTYPE,HSMADDRESS,HSMPORT,HEADERLEN,HEADERTYPE,HSMHEADERLEN,HSMTIMEOUT,CONNECTIONINTERVAL,HSMSTATUS,HSM_ID,AUTH_CODE,AUTH_DATE,AUTH_BY,ADDED_BY,ADDED_DATE,REMARKS FROM HSM_DETAILS WHERE HSM_ID='"+hsmid+"')";
		System.out.println("--- insert_main_qury --- "+insert_main_qury);
		enctrace(" Inserting in main table .. "+insert_main_qury);
		int insertedmain = jdbctemplate.update(insert_main_qury);	
		return insertedmain;
	}  
	public int deleteHsmMaintable(String hsmid,JdbcTemplate jdbctemplate) {
		String deleteHsm = "DELETE FROM HSM_DETAILS where HSM_ID='"+hsmid+"'";
		int deleteHsm_result = jdbctemplate.update(deleteHsm);
		return deleteHsm_result;
	}
	public int updateDeAuthHsm(String userid,String remarks,String hsmid,JdbcTemplate jdbctemplate) {
		/*String update_authdeauth_qury = "UPDATE HSM_DETAILS SET AUTH_CODE='9',AUTH_DATE=sysdate,AUTH_BY='"+userid+"',REMARKS='"+remarks+"' where HSM_ID='"+hsmid+"'";
		int update_authdeauth = jdbctemplate.update(update_authdeauth_qury);*/
		
		//ADDED BY GOWTHAM_120819
		Date date =  new Date();
		String update_authdeauth_qury = "UPDATE HSM_DETAILS SET AUTH_CODE=?,AUTH_DATE=?,AUTH_BY=?,REMARKS=? where HSM_ID=?";
		int update_authdeauth = jdbctemplate.update(update_authdeauth_qury,new Object[]{"9",date.getCurrentDate(),userid,remarks,hsmid});
		return update_authdeauth;
	}


}
