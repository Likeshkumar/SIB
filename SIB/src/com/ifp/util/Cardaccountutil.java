package com.ifp.util;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.commons.lang.xwork.StringEscapeUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import org.springframework.transaction.support.TransactionSynchronizationManager;
import com.ifp.Action.BaseAction;
import com.ifp.instant.HSMParameter;
import com.ifp.personalize.Personalizeorderdetails;
import com.ifp.instant.RequiredCheck;
public class Cardaccountutil extends BaseAction
{
	private static final long serialVersionUID = 1L;
	private DataSource dataSource;
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public Cardaccountutil(){
		
	}
	
	public Cardaccountutil( DataSource dataSource ){
		this.dataSource = dataSource;
	}
	
	
	
	
}
