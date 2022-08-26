package com.ifp.beans;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MyRowMapper {
	public TESTBEAN mapRow(ResultSet rs, int rownum) throws SQLException {  
	      TESTBEAN test = new TESTBEAN(); 
	     
	      return test;
	}
}
