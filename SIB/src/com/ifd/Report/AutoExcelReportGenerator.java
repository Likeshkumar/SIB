package com.ifd.Report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ifp.Action.BaseAction;

public class AutoExcelReportGenerator extends BaseAction{
	
	private ByteArrayOutputStream output_stream;
	private ByteArrayInputStream input_stream;
	private String transctionreport; 
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	
	public ByteArrayInputStream getInput_stream() {
		return input_stream;
	}

	public void setInput_stream(ByteArrayInputStream input_stream) {
		this.input_stream = input_stream;
	}

	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	public ByteArrayOutputStream getOutput_stream() {
		return output_stream;
	}

	public void setOutput_stream(ByteArrayOutputStream output_stream) {
		this.output_stream = output_stream;
	}

	
	private List getReportConfigDetails(String instid, String reportno,JdbcTemplate jdbctemplate) {
		List getrep = null;
		try {
			String getrepqry= "select REPORTNAME,PASSWORD_REQ,PAGENO_REQ,FOOTERCONTENT from PDFREPORTGENRATOR where RNO='"+reportno+"' ";
			enctrace("getrepqry : " + getrepqry );
			getrep = jdbctemplate.queryForList(getrepqry);
		} catch (EmptyResultDataAccessException e) { getrep=null; }
		return getrep;
	}
	
	public String ExcelReportGenMaster(List excelReportList) throws IOException
	{
		trace("reportGenMaster started ...");
		HttpSession session = getRequest().getSession();	
		String instid = (String)session.getAttribute("Instname");
		String reportno="4";
		
		System.out.println("reportno::"+reportno);   
		
		List repconfig = this.getReportConfigDetails(instid,reportno,jdbctemplate); 
		Iterator iterator=repconfig.iterator();
		String filename="",passwdReq="";
		while(iterator.hasNext())	{
			Map map = (Map)iterator.next();
			filename = (String)map.get("REPORTNAME");
			}
		
		List result = excelReportList;
		
		String excelparam = this.getExcelParam(instid,reportno,jdbctemplate);
		
		List combinedlist= new ArrayList();
		combinedlist.add(result);   
		combinedlist.add(result);
		String keyDesc = "successful - unsuccessful";
		String res = this.getExcelReport(combinedlist,excelparam,filename);		
		System.out.println("reportGenMaster():::reportGenMaster"+res);
		return res;
	}
	
	private String getExcelParam(String instid, String reportno,JdbcTemplate jdbctemplate2) {
		String xlparam = null;
		try {
			String xlparamqry= "select EXCEL_PARAM from PDFREPORTGENRATOR where RNO='"+reportno+"' ";
			enctrace("xlparamqry : " + xlparamqry );
			xlparam = (String)jdbctemplate.queryForObject(xlparamqry,String.class);
		} catch (EmptyResultDataAccessException e) { xlparam=null; }
		return xlparam;
	}
	
	public String getExcelReport(List listqry,String keyDesc,String namestr){
		trace("	 ***************** Transaction Details report ****************");
		enctrace("**************** Transaction Details report ****************");
		
		trace("listqry:::::"+listqry);
		trace(":::::keyDesc"+keyDesc);
		trace(":::::namestr"+namestr);     
		
		HttpSession session=getRequest().getSession();
			try {
					output_stream = new ByteArrayOutputStream();
				    String curdatetime = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
				    trace("dateFormat.format(date)    --->  "+curdatetime);
				    String defaultname = namestr+curdatetime+".xls";
				    trace("default name"+ defaultname);
				HSSFWorkbook workbook = new HSSFWorkbook();						
				HSSFSheet sheet = null;
				trace(" keyDesc "+keyDesc);
				String keydesc_split[] = keyDesc.split("-");
				System.out.println("keydesc_split[] ==== "+keydesc_split[0]);
				Iterator combined_itr = listqry.iterator();
				int i=0;
				while(combined_itr.hasNext()){
					List lst = (List) combined_itr.next();
					trace(" ----- lst  ---- "+lst.size());
					sheet = workbook.createSheet(keydesc_split[i]);
					int rownum = 0;int cellnum = 0;
					Iterator  itr1 = lst.iterator();
					int rowno=0;
					HSSFRow rowheading=null;
					while( itr1.hasNext() ){
						trace("rowno = ******** = "+rowno);
						Map map = (Map)itr1.next();
						Iterator keyItr = map.keySet().iterator();
						if(rowno==0){
							rowheading = sheet.createRow((short)rowno++);
							trace("*******rowheading *****");
						}
						HSSFRow row = sheet.createRow((short) rowno++);
						String key = null;
						int cellno=0;
						while(keyItr.hasNext()){	
							trace("rowno inside second while loop = ******** = "+rowno);
							HSSFCell cell = null;
							key = (String) keyItr.next();
							if(rowno==2){
								cell = rowheading.createCell((short)cellno);
								cell.setCellValue(key);
								trace("Key .........."+key);
							}                                           
							cell = row.createCell((short)cellno);
							cell.setCellValue((String)map.get(key));
							trace(" value .... "+ (String)map.get(key));
							cellno++;
						}
						trace("rowno outside while loop "+row.getRowNum());
						if(row.getRowNum() > 49998){
							trace("entered rownum ");
							sheet = workbook.createSheet(keydesc_split[i]);
							rowno=0;
							continue;
						}
					}
					i++;
					trace("	value of i at the bottom --	"+i+"	rownum -- "+rowno);
				}				
				workbook.write(output_stream);
				setTransctionreport(defaultname);
				input_stream = new ByteArrayInputStream(output_stream.toByteArray());		
				output_stream.flush();
				trace("Close file");
				return "transactionexcel";
			}catch (Exception e) {
				trace("ERROR: ->"+e.getMessage());	
				e.printStackTrace();
				addActionError("Could not generate report");
			}
			return "globalreporterror";
		}

	public String getTransctionreport() {
		return transctionreport;
	}

	public void setTransctionreport(String transctionreport) {
		this.transctionreport = transctionreport;
	}	

}
