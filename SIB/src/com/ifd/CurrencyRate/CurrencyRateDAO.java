package com.ifd.CurrencyRate;

import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;

public class CurrencyRateDAO extends BaseAction{

	private static final long serialVersionUID = 1L;

	/*public List<?> getCurrencyList(JdbcTemplate jdbctemplate) {
		List<?> currencylist = null;
		try{
			String currencyqry = "SELECT NUMERIC_CODE,CURRENCY_CODE FROM GLOBAL_CURRENCY WHERE NUMERIC_CODE NOT IN (SELECT CUR_CODE FROM INSTITUTION_CURRENCY WHERE PREFERENCE='P')";
			enctrace("CurrencyRte currencyqry-->"+currencyqry);
			currencylist = jdbctemplate.queryForList(currencyqry);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return currencylist;
	}*/
	
	
	
	public List<?> getCurrencyList(JdbcTemplate jdbctemplate) {
		List<?> currencylist = null;
		try{
			
			/*String currencyqry = "SELECT NUMERIC_CODE,CURRENCY_CODE FROM GLOBAL_CURRENCY WHERE NUMERIC_CODE NOT IN (SELECT CUR_CODE FROM INSTITUTION_CURRENCY WHERE PREFERENCE='P')";
			enctrace("CurrencyRte currencyqry-->"+currencyqry);
			currencylist = jdbctemplate.queryForList(currencyqry);*/
			
			//by gowtham-210819
			String currencyqry = "SELECT NUMERIC_CODE,CURRENCY_CODE FROM GLOBAL_CURRENCY WHERE NUMERIC_CODE NOT IN (SELECT CUR_CODE FROM INSTITUTION_CURRENCY WHERE PREFERENCE=?)";
			enctrace("CurrencyRte currencyqry-->"+currencyqry);
			currencylist = jdbctemplate.queryForList(currencyqry,new Object[]{"P"});
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return currencylist;
	}

	public int saveCurrency(String instid, CurrencyRateBeans bean, String userid, String action, JdbcTemplate jdbctemplate) {
		String savecurrency = "";
		int insert = 0;
		if("INSERT".equalsIgnoreCase(action)){
			savecurrency = "INSERT INTO EZCURRENCY_RATE(INSTID,CURRCODE,SELLINGRATE,BUYINGRATE,MAKER_ID,MAKER_DATE,AUTH_CODE,MKCK_STATUS) VALUES" +
					"('"+instid+"','"+bean.getCurrencycode()+"','"+bean.getSellingrate()+"','"+bean.getBuyingrate()+"','"+userid+"',SYSDATE,'0','M')";
			enctrace("CurrencyRte savecurrency-->"+savecurrency);
		}else{
			savecurrency = "UPDATE EZCURRENCY_RATE SET SELLINGRATE='"+bean.getSellingrate()+"',BUYINGRATE='"+bean.getBuyingrate()+"' WHERE CURRCODE='"+bean.getCurrencycode()+"'";
			enctrace("CurrencyRte savecurrency-->"+savecurrency);
		}
		
		try{
			insert = jdbctemplate.update(savecurrency);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return insert;
	}

	/*public List<Map<String, Object>> getPreviousRate(String currencycode,JdbcTemplate jdbctemplate) {
		List prerate = null;
		String prerateqry= "SELECT BUYINGRATE,SELLINGRATE FROM EZCURRENCY WHERE CURRCODE='"+currencycode+"'";
		enctrace("CurrencyRte prerateqry-->"+prerateqry);
		try{
			prerate = jdbctemplate.queryForList(prerateqry);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return prerate;
	}*/
	
	
	

	public List<Map<String, Object>> getPreviousRate(String currencycode,JdbcTemplate jdbctemplate) {
		List prerate = null;
		
		/*String prerateqry= "SELECT BUYINGRATE,SELLINGRATE FROM EZCURRENCY WHERE CURRCODE='"+currencycode+"'";*/
		String prerateqry= "SELECT BUYINGRATE,SELLINGRATE FROM EZCURRENCY WHERE CURRCODE=?";
		
		
		enctrace("CurrencyRte prerateqry-->"+prerateqry);
		try{
			
			/*prerate = jdbctemplate.queryForList(prerateqry);*/
			
			prerate = jdbctemplate.queryForList(prerateqry,new Object[]{currencycode});
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return prerate;
	}
/*
	public String checkCurrencyExists(String instid, CurrencyRateBeans bean, JdbcTemplate jdbctemplate) {
		String currencycount = null; 
		try {
			String currencycodeqry ="SELECT COUNT(1) as CNT FROM EZCURRENCY_RATE WHERE INSTID='"+instid+"' AND  CURRCODE='"+bean.getCurrencycode()+"'" ;
			trace("currencycodeqry : "+ currencycodeqry );
			currencycount = (String)jdbctemplate.queryForObject(currencycodeqry, String.class);
		} catch (EmptyResultDataAccessException e) { }
		return currencycount;
	}

	public List<?> getAuthCurrency(JdbcTemplate jdbctemplate) {
		List<?> authlist= null;
		String authcurrencyqry = "SELECT CURRCODE,DECODE(CURRCODE,'840','USD','800','UGX') AS CURRDESC FROM EZCURRENCY_RATE WHERE AUTH_CODE='0' AND MKCK_STATUS='M'";
		enctrace("CurrencyRte authcurrencyqry-->"+authcurrencyqry);
		authlist = jdbctemplate.queryForList(authcurrencyqry);
		return authlist;
	}*/
	
	
	public String checkCurrencyExists(String instid, CurrencyRateBeans bean, JdbcTemplate jdbctemplate) {
		String currencycount = null; 
		try {
			
			/*String currencycodeqry ="SELECT COUNT(1) as CNT FROM EZCURRENCY_RATE WHERE INSTID='"+instid+"' AND  CURRCODE='"+bean.getCurrencycode()+"'" ;
			trace("currencycodeqry : "+ currencycodeqry );
			currencycount = (String)jdbctemplate.queryForObject(currencycodeqry, String.class);*/
			
			//by gowtham-210819
			String currencycodeqry ="SELECT COUNT(1) as CNT FROM EZCURRENCY_RATE WHERE INSTID=? AND  CURRCODE=?" ;
			trace("currencycodeqry : "+ currencycodeqry );
			currencycount = (String)jdbctemplate.queryForObject(currencycodeqry,new Object[]{instid,bean.getCurrencycode()}, String.class);
			
		} catch (EmptyResultDataAccessException e) { }
		return currencycount;
	}

	public List<?> getAuthCurrency(JdbcTemplate jdbctemplate) {
		List<?> authlist= null;
		
		/*String authcurrencyqry = "SELECT CURRCODE,DECODE(CURRCODE,'840','USD','800','UGX') AS CURRDESC FROM EZCURRENCY_RATE WHERE AUTH_CODE='0' AND MKCK_STATUS='M'";
		enctrace("CurrencyRte authcurrencyqry-->"+authcurrencyqry);
		authlist = jdbctemplate.queryForList(authcurrencyqry);*/
		
		//by gowtham-210819
		String authcurrencyqry = "SELECT CURRCODE,DECODE(CURRCODE,'840','USD','800','UGX') AS CURRDESC FROM EZCURRENCY_RATE WHERE AUTH_CODE=? AND MKCK_STATUS=?";
		enctrace("CurrencyRte authcurrencyqry-->"+authcurrencyqry);
		authlist = jdbctemplate.queryForList(authcurrencyqry,new Object[]{"0","M"});
		
		return authlist;
		
	}
	
	public List<?> getAuthCurrencyList(CurrencyRateBeans bean, JdbcTemplate jdbctemplate) {
		List<?> authlist= null;
		String authcurrencyqry = "SELECT BUYINGRATE,SELLINGRATE,DECODE(CURRCODE,'840','USD','800','UGX') AS CURRDESC,CURRCODE FROM EZCURRENCY_RATE WHERE CURRCODE='"+bean.getCurrencycode()+"'";
		enctrace("CurrencyRte authcurrencyqry-->"+authcurrencyqry);
		authlist = jdbctemplate.queryForList(authcurrencyqry);
		return authlist;
	}
	
	public int movetoEZHistory(CurrencyRateBeans bean, String userid,JdbcTemplate jdbctemplate) {
		String movetoezhistqry = "INSERT INTO EZCURRENCYHIST SELECT * FROM EZCURRENCY WHERE CURRCODE='"+bean.getCurrencycode()+"'";
		enctrace("CurrencyRte movetoezhistqry-->"+movetoezhistqry);
		int hist = jdbctemplate.update(movetoezhistqry);
		return hist;
	}
	
	public int movetoEZCurrency(CurrencyRateBeans bean, String userid,JdbcTemplate jdbctemplate) {
		String movetoezcurrencyqry = "UPDATE EZCURRENCY E SET  (BUYINGRATE,SELLINGRATE,AUTH_CODE,ADDED_DATE,CHECKER_DATE,CHECKER_ID,MAKER_DATE,MAKER_ID,MKCK_STATUS) = (SELECT BUYINGRATE,SELLINGRATE,'1' AUTH_CODE,SYSDATE ADDED_DATE,SYSDATE CHECKER_DATE,'"+userid+"' CHECKER_ID,MAKER_DATE,MAKER_ID,'P' MKCK_STATUS FROM EZCURRENCY_RATE WHERE CURRCODE='"+bean.getCurrencycode()+"') WHERE CURRCODE='"+bean.getCurrencycode()+"'";
		enctrace("CurrencyRte movetoezcurrencyqry-->"+movetoezcurrencyqry);
		int movetoezcurrency = jdbctemplate.update(movetoezcurrencyqry);
		return movetoezcurrency;
	}

	public int movetoTempHistory(CurrencyRateBeans bean, String userid,JdbcTemplate jdbctemplate) {
		String movetotemphistqry = "INSERT INTO EZCURRENCY_RATEHIST SELECT * FROM EZCURRENCY_RATE WHERE CURRCODE='"+bean.getCurrencycode()+"'";
		enctrace("CurrencyRte movetotemphistqry-->"+movetotemphistqry);
		int temphist = jdbctemplate.update(movetotemphistqry);
		return temphist;
	}

	public int tempDelete(CurrencyRateBeans bean, JdbcTemplate jdbctemplate) {
		String deletetempqry = "DELETE FROM EZCURRENCY_RATE WHERE CURRCODE='"+bean.getCurrencycode()+"'";
		enctrace("CurrencyRte deletetempqry-->"+deletetempqry);
		int deletetemp = jdbctemplate.update(deletetempqry);
		return deletetemp;
	}

}
