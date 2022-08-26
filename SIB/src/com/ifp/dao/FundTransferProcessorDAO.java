package com.ifp.dao;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;

@Transactional
public class FundTransferProcessorDAO extends BaseAction {

	public List getWaitingFundTranferList(String bin, JdbcTemplate jdbctemplate ){
		List fundtxnlist=null;
		String fundtxnqry= "SELECT  INST_ID, FROMENTITY, 'CARDHOLDERNAME' AS CARDHOLDERNAME, 'BANKNAME' AS BANKNAME, TOENTITY, 'ACCTHOLDERNAME' AS ACCTHOLDERNAME,  NVL(TO_CHAR(BEFORE_TXN,'999G999G999G999G999MI'),'0') AS BEFORE_TXN , TO_CHAR(TXN_AMOUNT) as TEXT_TXNAMOUNT, TO_CHAR(TXN_AMOUNT,'999G999G999G999G999MI') AS TXN_AMOUNT  , nvl(TXN_COMMENT,'') AS TXN_COMMENT, NVL(TO_CHAR(AFTER_TXN,'999G999G999G999G999MI'),'0') AS AFTER_TXN ,( TO_CHAR(RECIEVED_DATE,'DD-MON-YYYY') ||' : ' ||  TRANTIME ) RECIEVED_DATE  FROM IFPS_FUND_TRANSFER WHERE BIN='"+bin+"' AND TXN_STATUS='S' AND ENTITY_KEY='FNDTRANS' ORDER BY RECIEVED_DATE";
		System.out.println("fundtxnqry :" + fundtxnqry );
		fundtxnlist = jdbctemplate.queryForList(fundtxnqry);
		return fundtxnlist;		
	}
	
	public List cbsAccountDetails( String instid, String cardno, String acctno, JdbcTemplate jdbctemplate ){
		List acctdetlist=null;
		String acctdetlistqry= "SELECT ACCTHOLDER_NAME, BRANCH, ACCT_CURCODE, BANK_CODE  FROM IFP_CARD_CBSACCT WHERE INST_ID='"+instid+"' AND CARD_NO='"+cardno+"' AND CBS_ACCTNO='"+acctno+"'";
		System.out.println("acctdetlistqry :" + acctdetlistqry );
		acctdetlist = jdbctemplate.queryForList(acctdetlistqry);
		return acctdetlist;	
	}
	
	public List getFundTransferedBin( JdbcTemplate jdbctemplate ){
		List fundtxnbin=null;
		String fundtxnbinqry= "SELECT BIN FROM IFPS_FUND_TRANSFER WHERE TXN_STATUS='S' AND BIN IS NOT NULL GROUP BY BIN";
		System.out.println("fundtxnbinqry :" + fundtxnbinqry );
		fundtxnbin = jdbctemplate.queryForList(fundtxnbinqry);
		return fundtxnbin;	
	}
	
	
	public int deleteFundTransfer(String instid, String bin, JdbcTemplate jdbctemplate) throws Exception {
		int x = -1;
		String deletefund = "DELETE FROM IFPS_FUND_TRANSFER WHERE INST_ID='"+instid+"' AND BIN='"+bin+"' AND TXN_STATUS='S'";
		System.out.println("deletefund :" + deletefund );
		x = jdbctemplate.update(deletefund);
		return x;
	}

	public List getBankAttr(String instid, String bankcode, JdbcTemplate jdbctemplate) throws Exception {
		List sortcode=null;
		String sortcodeqry= "SELECT BANK_NAME, SORT_CODE, SWIFT_CODE FROM IFP_BANK WHERE INST_ID='"+instid+"' AND BANK_CODE='"+bankcode+"' AND ROWNUM <=1 ";
		System.out.println("sortcodeqry :" + sortcodeqry );
		sortcode = jdbctemplate.queryForList(sortcodeqry);
		return sortcode;	
	}
}
