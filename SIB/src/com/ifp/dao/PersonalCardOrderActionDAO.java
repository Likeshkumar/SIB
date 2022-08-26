package com.ifp.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;
import com.ifp.beans.PersonalCardOrderActionBean;

public class PersonalCardOrderActionDAO extends BaseAction { 
	private static final long serialVersionUID = 1L;

	public int insertCardOrder(String instid, String applicationid, String customerid, PersonalCardOrderActionBean orderbean, JdbcTemplate jdbctemplate ) throws Exception {
		int x = -1;
		trace("Inserting PERS_CARD_ORDER.....");
		String order_qury = "INSERT INTO PERS_CARD_ORDER (INST_ID, ORDER_REF_NO, CARD_TYPE_ID, SUB_PROD_ID, " ;
		order_qury +=  " PRODUCT_CODE, CARD_QUANTITY, ORDER_STATUS, ORDER_TYPE, ORDERED_DATE, ORDERED_TIME, EMBOSSING_NAME, " ;
		order_qury += 	" MKCK_STATUS, REMARKS,ENCODE_DATA, MAKER_DATE, MAKER_TIME, MAKER_ID, CHECKER_DATE, CHECKER_TIME, CHECKER_ID, BRANCH_CODE, " ;
		order_qury += " BIN, APP_DATE, CIN, APP_NO,KYC_FLAG) VALUES ('"+instid+"', '"+applicationid+"', '"+orderbean.cardtypeid+"', '"+orderbean.subprodid+"', " ;
		order_qury += " '"+orderbean.productcode+"', '"+orderbean.cardquantity+"', '"+orderbean.orderstatus+"', '"+orderbean.ordertype+"',SYSDATE, '00', '"+orderbean.embossingname+"'," ;
		order_qury += " '"+orderbean.mkckstatus+"', '"+orderbean.remarks+"', '"+orderbean.encodedata+"', SYSDATE, '00', '"+orderbean.makerid+"', SYSDATE, '00', '"+orderbean.checkerid+"', '"+orderbean.branchcode+"', " ;
		order_qury += " '"+orderbean.bin+"', SYSDATE, '"+orderbean.cin+"', '"+orderbean.app_no+"', '"+orderbean.kyc_flag+"')" ;
		enctrace("order_qury : " + order_qury );
		x = jdbctemplate.update(order_qury);
		return x;
	}
}
