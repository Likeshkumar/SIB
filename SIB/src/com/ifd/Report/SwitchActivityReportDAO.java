package com.ifd.Report;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;

public class SwitchActivityReportDAO extends BaseAction {

	public List getAccountStatusList(String querycondition,JdbcTemplate jdbctemplate) {
		List result = null;
		try {
			String selectqry = "SELECT ORDER_REF_NO,ACCOUNT_NO,ACCTTYPE_ID FROM CARD_PRODUCTION WHERE " +querycondition;
			trace("select qry ===== > "+selectqry);
			result = jdbctemplate.queryForList(selectqry);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
