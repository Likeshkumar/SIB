package com.ifp.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import com.ifp.Action.BaseAction;


@Transactional
public class ReportgenerationActionDAO  extends BaseAction{
	public List getGlGroup(String instid, JdbcTemplate jdbctemplate ) throws Exception {
		List gl_llist=null;
		String gllistqry = "SELECT GROUP_CODE, GROUP_NAME FROM IFP_GL_GROUP WHERE INST_ID='"+instid+"'";
		enctrace("gllistqry :" + gllistqry);
		gl_llist = jdbctemplate.queryForList(gllistqry);
		return gl_llist ; 
	}
	
	public List getList(String instid, String glgrpid, JdbcTemplate jdbctemplate ) throws Exception {
		List gl_llist=null;
		String gllistqry = "SELECT GL_CODE,GL_NAME,GL_BAL_TYPE,GL_BAL_TYPE,GL_ALIE,GL_POSITION,GL_STATEMENT_TYPE,CUR_CODE  FROM IFP_GL_MASTER WHERE INST_ID='"+instid+"'";
		enctrace("gllistqry :" + gllistqry);
		gl_llist = jdbctemplate.queryForList(gllistqry);
		return gl_llist ; 
	}
	
	public List getSubList(String instid, String glid, JdbcTemplate jdbctemplate ) throws Exception {
		List gl_llist=null;
		String gllistqry = "SELECT SCH_CODE,SCH_NAME   FROM   IFP_GL_SCHEME_MASTER WHERE INST_ID='"+instid+"' AND GL_CODE='"+glid+"'";
		enctrace("gllistqry: " + gllistqry);
		gl_llist = jdbctemplate.queryForList(gllistqry);
		return gl_llist ; 
	}
	
	public String getGloableDescription(String key, JdbcTemplate jdbctemplate ){
		String desc=null;
		try {
			String descqry= "SELECT DESCRIPTION FROM GLOBAL_DESCRIPTION WHERE CODE='"+key+"'";
			enctrace("descqry :" + descqry );
			desc = (String)jdbctemplate.queryForObject(descqry, String.class);
		} catch (DataAccessException e) {
			desc = key; 
		}
		return desc; 
	}
	
}
