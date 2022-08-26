package com.ifp.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;

public class CardCvvDecryption extends BaseAction {
	
	
	
	/**
	 * 
	 * @param encCardNo
	 * @param jdbctemplate
	 * @return
	 */
	public List  getCVVValues(String encCardNo,JdbcTemplate jdbctemplate){
		
		 //String clearCvv1="";
		  List  listEncValues=null;
		try{
			String queryForCVvalues1="SELECT CVV1,CVV2,ICVV  FROM PERS_CARD_PROCESS WHERE ORG_CHN='"+encCardNo+"'";
			   
			     listEncValues=jdbctemplate.queryForList(queryForCVvalues1);
			
			//clearCvv1=encTrack.decrypt(encCVV1);
			trace(" queryForCVV1 ====   "+queryForCVvalues1);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return listEncValues;
	}

}
