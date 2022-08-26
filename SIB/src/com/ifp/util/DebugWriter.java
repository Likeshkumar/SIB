package com.ifp.util;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ifp.Action.BaseAction;


public   class DebugWriter extends BaseAction 
{
	private static final long serialVersionUID = 1L;
	
	private JdbcTemplate jdbctemplate = new JdbcTemplate();
	private PlatformTransactionManager  txManager=new DataSourceTransactionManager();
	
	
	public JdbcTemplate getJdbctemplate() {
		return jdbctemplate;
	}

	public void setJdbctemplate(JdbcTemplate jdbctemplate) {
		this.jdbctemplate = jdbctemplate;
	}

	public PlatformTransactionManager getTxManager() {
		return txManager;
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.txManager = txManager;
	}

	public String getDatetime()
	{
		Date date = new Date();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
		String today_date = dateFormat.format(date);
		Calendar cal = Calendar.getInstance();
		String Current_time = timeFormat.format(cal.getTime());
		String current_datetime = today_date+"~"+Current_time;
		return current_datetime;
	}
	
	public String getFilename(String insti_id)
	{
		
		String file_name = "Default";
		String Inst_name =insti_id;
		//System.out.println(" Institution Id Selected from Config is "+Inst_name);
		String[] Inst_split = Inst_name.split("~");
		int Len_inst = Inst_split.length;
		//System.out.println(" Instituion Id String Lenght is ===> "+Len_inst);
		for(int l=0; l<Len_inst; l++)
		{
			if(insti_id.equals(Inst_split[l]))
			{
				file_name = Inst_split[l];
			}
		}
		//System.out.println(" Instituion Id Return is  ######### "+file_name);
		return file_name;
		
		//===========================================================================
		//===========================================================================
		//===========================================================================
		/*JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		String allInst="select INST_ID from INSTITUTION order by PREFERENCE";
		  
		try
		{
		List Inst_list;
		Inst_list = jdbcTemplate.queryForList(allInst);
		Iterator inst_itr = Inst_list.iterator();
		while(inst_itr.hasNext())
		{
			Map map = (Map)inst_itr.next();
			String name = ((String)map.get("INST_ID"));
			//System.out.println(" THe Name Received Is  "+name);
			if(insti_id.equals(name))
			{
				file_name = name;
			}
		}
		}
		catch (Exception e) 
		{
			// TODO: handle exception
		}*/
		//===========================================================================
		//===========================================================================
		
	}
	
	public void debugWriter(String class_name, String instid,String DebugMessage)
	{
 
		/* 
		//System.out.println(" Instituion Id Received is  "+instid);
		String FileName = getFilename(instid);
		//System.out.println("File Name Starts WIth  "+FileName);
		
		String filePath = getText("fileName");
		//Properties prop = commondesc.getCommonDescProperty();
		//String filePath = prop.getProperty("fileName");
		System.out.println(" File Path Is "+filePath);
		//String Extension = prop.getProperty("Extension");
		String Extension = getText("Extension");
		//System.out.println(" Extention For File is "+Extension);		
		String instLevelFilePath = getText("instLevelfileName");
		
		//String instLevelFilePath = prop.getProperty("instLevelfileName");
		//System.out.println("Instituion Level Path is  "+instLevelFilePath);
		String instExtension = getText("instLevelExtension");
		//String instExtension = prop.getProperty("instLevelExtension");
		//System.out.println("Institiotn Log FIle Extention is  "+instExtension);
		String toDaydatetime = getDatetime();
		//System.out.println("Datte Time Got is  "+toDaydatetime);
		String[] dateTime = toDaydatetime.split("~");
		String toDate = dateTime[0];
		String toTime = dateTime[1];
		String Debug_Message = toDate+" "+toTime+" DEBUG : "+class_name+" :"+DebugMessage;
		//System.out.println("Debug Message is ==> "+Debug_Message);
		String file_name =filePath+"_"+toDate+Extension;
		//System.out.println(" File Path and Name Compined is "+file_name);
		String inst_filename = instLevelFilePath+FileName+"_"+toDate+instExtension;
		//System.out.println(" Instituoion Level File Path and Name is =====>"+inst_filename);
		

			try
			{
				FileWriter instfstream = new FileWriter(inst_filename,true);
				BufferedWriter instdebugwriter = new BufferedWriter(instfstream);
				FileWriter fstream = new FileWriter(file_name,true);
				BufferedWriter prewriter = new BufferedWriter(fstream);
				try
				{
					prewriter.write(Debug_Message);
					prewriter.write("\n");
					instdebugwriter.write(Debug_Message);
					instdebugwriter.write("\n");
					//System.out.println(" File Is Created and written into File ");
				}
				catch (Exception e) 
				{
					prewriter.close();
					instdebugwriter.close();
					System.out.println(" Error While Written Into File "+e.getMessage());
				}
				//System.out.println(" Message written into File ");
				prewriter.close();
				instdebugwriter.close();
				//System.out.println(" File Closed ");
			}
			catch (Exception e) 
			{
				//System.out.println(" Error While Cretae the File "+e.getMessage());
			}*/
		//}
			//return 1;
	}
	//public void  errorWriter(String class_name,HttpSession session,String ErrorMessage)
	public void  errorWriter(String class_name,String in_name,String ErrorMessage)
	{		
		/*//String i_Name = (String)session.getAttribute("Instname");
		//String in_name = i_Name.toUpperCase();
		String FileName = getFilename(in_name);
		
		//Properties prop = commondesc.getCommonDescProperty();
		
		String filePath = getText("fileName");
		//String filePath = prop.getProperty("fileName");
				
		String Extension = getText("Extension");
		//String Extension = prop.getProperty("Extension");
		
		String instLevelFilePath = getText("instLevelfileName");
		//String instLevelFilePath = prop.getProperty("instLevelfileName");
		
		String instExtension = getText("instLevelExtension");
		//String instExtension = prop.getProperty("instExtension");
		
		String toDaydatetime = getDatetime();
		String[] dateTime = toDaydatetime.split("~");
		String toDate = dateTime[0];
		String toTime = dateTime[1];
		String Error_Message = toDate+" "+toTime+" ERROR : "+class_name+" :"+ErrorMessage;
		String file_name =filePath+"_"+toDate+Extension;
		String inst_filename = instLevelFilePath+FileName+"_"+toDate+instExtension;
		try
		{
			FileWriter fstream = new FileWriter(file_name,true);
			BufferedWriter prewriter = new BufferedWriter(fstream);
			FileWriter insterrstream = new FileWriter(inst_filename,true);
			BufferedWriter insterrwriter = new BufferedWriter(insterrstream);
			try
			{
				insterrwriter.write(Error_Message);
				prewriter.write("\n");
				insterrwriter.write(Error_Message);
				insterrwriter.write("\n");
				//System.out.println(" File Is Created and written into File ");
			}
			catch (Exception e) 
			{
				prewriter.close();
				insterrwriter.close();
				//System.out.println(" Error While Written Into File "+e.getMessage());
			}
			//System.out.println(" Message written into File ");
			prewriter.close();
			insterrwriter.close();
			//System.out.println(" File Closed ");
		}
		catch (Exception e) 
		{
			//System.out.println(" Error While Cretae the File "+e.getMessage());
		}*/
		//return 1;
	}
	
	public void hsmLog(String instid,String DebugMessage)
	{
 
		/* 
		//System.out.println(" Instituion Id Received is  "+instid);
		String FileName = "HSM";
		//System.out.println("File Name Starts WIth  "+FileName);
		
		String filePath = "abc";//getText("fileName");
		//System.out.println(" File Path Is "+filePath);
		String Extension = ".debug"; //getText("Extension");
		//System.out.println(" Extention For File is "+Extension);
		
		String instLevelFilePath = "aaa";// getText("instLevelfileName");
		//System.out.println("Instituion Level Path is  "+instLevelFilePath);
		String instExtension = "efg";//getText("instLevelExtension");
		//System.out.println("Institiotn Log FIle Extention is  "+instExtension);
		String toDaydatetime = getDatetime();
		//System.out.println("Datte Time Got is  "+toDaydatetime);
		String[] dateTime = toDaydatetime.split("~");
		String toDate = dateTime[0];
		String toTime = dateTime[1];
		String Debug_Message = toDate+" "+toTime+" DEBUG :"+DebugMessage;
		 
		//System.out.println("Debug Message is ==> "+Debug_Message);
		String file_name =filePath+"_"+toDate+Extension;
		 
		 
		//String inst_filename = instLevelFilePath+FileName+"_"+toDate+instExtension;
		//System.out.println(" Instituoion Level File Path and Name is =====>"+inst_filename);
		

			try
			{
				//FileWriter instfstream = new FileWriter(inst_filename,true);
				//BufferedWriter instdebugwriter = new BufferedWriter(instfstream);
				FileWriter fstream = new FileWriter(file_name,true);
				BufferedWriter prewriter = new BufferedWriter(fstream);
				try
				{
					prewriter.write(Debug_Message);
					prewriter.write("\n");
					//instdebugwriter.write(Debug_Message);
					//instdebugwriter.write("\n");
					//System.out.println(" File Is Created and written into File ");
				}
				catch (Exception e) 
				{
					prewriter.close();
					//instdebugwriter.close();
					//System.out.println(" Error While Written Into File "+e.getMessage());
				}
				 
				prewriter.close();
				//instdebugwriter.close();
				//System.out.println(" File Closed ");
			}
			catch (Exception e) 
			{
				System.out.println(" Error While Cretae the File "+e.getMessage());
			}*/
		//}
			//return 1;
	}
	
}
