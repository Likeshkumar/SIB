package com.ifp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;



import com.ifp.Action.BaseAction;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;



public class newPDFReportGenerator extends BaseAction{
	private static Document documentObj=null;
	public String fontfamilyname;
	public String reportlogo;
	public String reportheader;
	public String reporttitles;
	public int reporttablewidth;
	public String reportsumfield;
	public String reportsummarytitle;
	public int reportsummarywidth;
	private int imageflag=1;
	private String autosummary="";
	public int precision_point=0;
	public String reportsummaryheader;
	public String filename;
	private String logopos;	
	private String tableheader_color="66A9BD",tableoddrow_color="ADA4D8",tableevenrow_color="FFFFFF";
	private float logoabsleft=0;
	private float logoabstop=0;
	private String countrecord="YES";
	
	private float fontsize=20;	
	private final int ALIGN_LEFT = 0,ALIGN_CENTER = 1,ALIGN_RIGHT = 2;	
	private String reportpropertyname="";
	private String propprefix="PDF";
	private String propfontkey="Font";
	private String proplogokey="logo";
	private String propheaderkey="header";
	private String proptitlekey="reporttitle";
	private String propsumkey="sumfields";
	private String proptableheader_color="#66A9BD";
	private String proptableheader_fontcolor="#66A9BD";
	private String proptableoddrow_color="#E3F2F9";
	private String proptableevenrow_color="#FFFFFF";
	private String pdftitlebackgroundcolor="#E3F2F9";
	private String pdfbodybackgroundcolor="#FFFFFF";
	private String propreportwidthkey="reportwidth";
	private String propsummarytitlekey="summarytitle";
	private String propsummarywidthkey="summarytablewidth";
	private String propprecision_pointkey="precision_point";
	private String proppdftitlebackgroundcolor="Pdftitlebackgroundcolor";
	private String proppdfbodybackgroundcolor="Pdfbodybackgroundcolor";
	private String proptableheaderfontcolor="tableheader.fontcolor";
	private String proptableheader="tableheader.color";
	private String protableoddrow="tableoddrow.color";
	private String proptableevenrow="tableevenrow.color";
	private String proplogoleftxkey="lxleft";
	private String proplogoleftykey="lytop";
	private String proplogoposkey="logopos";
	private String proplogorightxkey="rxleft";
	private String proplogorightykey="rytop";
	private String propautosummarykey="autosummary";
	private String propsummaryheader="summaryheading";
	private String propheaderfontcolor="";
	private String proplogo="logostatus";
	private String propsaveaskey="filename";
	private String propcountrecordkey="countrecord";
	private Map gradtotalrow;
	PdfPTable txntable;
	
	private String[] alignright= new String[] {"CNT", "BAL", "WDL_AMT", "PIN" , "FUND","FAILER", "SUCESS", "REVERSAL", "SUSPECT","OPENCASH","DISPENSEDCASH","CLOSEDCASH","TOTAL","SUBTOTAL","AMOUNT","SUM","AVERAGE","TXN AMOUNT"};
	
	public CommonUtil comUtil(){
		CommonUtil  comutil = new CommonUtil();
		return comutil;
	} 
	/* Single table Pdf report ,it is create a Pdf File with (report title,header,data,and grant total)
	public PDFReportGenerator(Document documentObj,ByteArrayOutputStream output_streamObj,String propertyname,HttpServletRequest req) throws DocumentException, MalformedURLException, IOException{
		//try{
		//	System.out.println("Contractor Called");
			System.out.println("Called PDFReportGenerator single =====>");
			PdfWriter.getInstance(documentObj,output_streamObj);
			documentObj.open();
			this.reportpropertyname=propertyname;
			this.fontfamilyname=getText(propprefix+"."+reportpropertyname+"."+propfontkey).trim();			
			this.reporttitles =getText(propprefix+"."+reportpropertyname+"."+proptitlekey).trim();
			this.reportheader =getText(propprefix+"."+reportpropertyname+"."+propheaderkey).trim();
			this.reporttablewidth =Integer.parseInt(getText(propprefix+"."+reportpropertyname+"."+propreportwidthkey).trim());		
			this.reportsumfield =getText(propprefix+"."+reportpropertyname+"."+propsumkey).trim();
			this.reportsummarytitle= getText(propprefix+"."+reportpropertyname+"."+propsummarytitlekey).trim();
			this.reportsummarywidth = Integer.parseInt(getText(propprefix+"."+reportpropertyname+"."+propsummarywidthkey).trim());
			this.reportsummaryheader=getText(propprefix+"."+reportpropertyname+"."+propsummaryheader).trim();
			this.precision_point = Integer.parseInt(getText(propprefix+"."+reportpropertyname+"."+propprecision_pointkey).trim());			
			this.autosummary= getText(propprefix+"."+reportpropertyname+"."+propautosummarykey).trim();
			String logoyes = getText(propprefix+"."+reportpropertyname+"."+proplogo).trim();
			System.out.println("Logo Yes ===============>"+logoyes);
			if(logoyes.equals("YES")){
				this.imageflag=0;
				this.reportlogo=comUtil().GetUrlReportIcon(req,getText(propprefix+"."+reportpropertyname+"."+proplogokey).trim());
				this.logopos=getText(propprefix+"."+reportpropertyname+"."+proplogoposkey).trim();			
				if(logopos.equals("LEFT")){
					this.logoabsleft=Float.parseFloat(getText(propprefix+"."+proplogoleftxkey).trim());
					this.logoabstop=Float.parseFloat(getText(propprefix+"."+proplogoleftykey).trim());
				}
				else{
					this.logoabsleft=Float.parseFloat(getText(propprefix+"."+proplogorightxkey).trim());
					this.logoabstop=Float.parseFloat(getText(propprefix+"."+proplogorightykey).trim());
				}
			}			
			addPDFTitles(documentObj,this.reporttitles,titlealign);
			addPDFHeading(documentObj,this.reportheader,tablealign,this.reporttablewidth);
			addPDFData(documentObj,this.reportsumfield,this.reportheader,reportdata_list);
			//String sumoffield[] = this.reportsumfield.split(",");
			
	}
	*/
	
	// Multiple table Pdf Report , It is initialize only values of instance,While use this function we should explicit call another methods (title,head,data,summary) 
	public newPDFReportGenerator(ByteArrayOutputStream output_streamObj,String propertyname,HttpServletRequest req) throws DocumentException{
		//System.out.println("Called PDFReportGenerator multi =====>");
			System.out.println("Contractor Called");	

			Rectangle layout = new Rectangle(PageSize.A4);
			layout.setBackgroundColor(BaseColor.WHITE);
			documentObj = new Document(layout); 		    
		    PdfWriter writer=PdfWriter.getInstance(documentObj,output_streamObj);
			documentObj.open();
			documentObj.setPageSize(PageSize.A4);
		    PdfContentByte cb = writer.getDirectContent();
		    cb.moveTo(PageSize.A4.RIGHT / 2, PageSize.A4.LEFT/ 2);
		    cb.lineTo(PageSize.A4.TOP / 2, PageSize.A4.BOTTOM);
		    cb.stroke();
			this.reportpropertyname=propertyname;
			System.out.println("------------propertyname---------------"+propertyname);
			this.fontfamilyname=getText(propprefix+"."+reportpropertyname+"."+propfontkey).trim();
			System.out.println("logo key "+propprefix+"."+reportpropertyname+"."+proplogokey);			
			System.out.println("logo geteText: "+getText(propprefix+"."+reportpropertyname+"."+proplogokey).trim());
			this.filename=getText(propprefix+"."+reportpropertyname+"."+propsaveaskey).trim();
			System.out.println("");
			this.reporttitles =getText(propprefix+"."+reportpropertyname+"."+proptitlekey).trim();
			System.out.println("HAI"+propprefix+"."+reportpropertyname+"."+proptitlekey);
			this.reportheader =getText(propprefix+"."+reportpropertyname+"."+propheaderkey).trim();
			System.out.println("D"+propprefix+"."+reportpropertyname+"."+propheaderkey);
			System.out.println("DDD"+propprefix+"."+reportpropertyname+"."+propreportwidthkey);
			this.reporttablewidth =Integer.parseInt(getText(propprefix+"."+reportpropertyname+"."+propreportwidthkey).trim());
			
			this.reportsumfield =getText(propprefix+"."+reportpropertyname+"."+propsumkey).trim();
			System.out.println("REPORT1"+propprefix+"."+reportpropertyname+"."+propsumkey);
			this.reportsummarytitle= getText(propprefix+"."+reportpropertyname+"."+propsummarytitlekey).trim();
			System.out.println("REPORT2"+propprefix+"."+reportpropertyname+"."+propsummarytitlekey);
			this.reportsummaryheader=getText(propprefix+"."+reportpropertyname+"."+propsummaryheader).trim();
			System.out.println("REPORT3"+propprefix+"."+reportpropertyname+"."+propsummaryheader);
			this.reportsummarywidth = Integer.parseInt(getText(propprefix+"."+reportpropertyname+"."+propsummarywidthkey).trim());
			System.out.println("REP4"+propprefix+"."+reportpropertyname+"."+propsummarywidthkey);
			this.precision_point = Integer.parseInt(getText(propprefix+"."+reportpropertyname+"."+propprecision_pointkey).trim());
			System.out.println("REP5"+propprefix+"."+reportpropertyname+"."+propprecision_pointkey);
			this.tableheader_color = getText(propprefix+"."+reportpropertyname+"."+proptableheader).trim();
			this.proptableheader_fontcolor = getText(propprefix+"."+reportpropertyname+"."+proptableheaderfontcolor).trim();			
			System.out.println("REP5"+propprefix+"."+reportpropertyname+"."+proptableheader);
			this.tableevenrow_color = getText(propprefix+"."+reportpropertyname+"."+proptableevenrow).trim();
			System.out.println("REP5"+propprefix+"."+reportpropertyname+"."+proptableevenrow);
			this.tableoddrow_color = getText(propprefix+"."+reportpropertyname+"."+protableoddrow).trim();
			System.out.println("REP5"+propprefix+"."+reportpropertyname+"."+protableoddrow);
			//this.logoabsleft=Float.parseFloat(getText(propprefix+"."+reportpropertyname+"."+proplogoleftkey));
			//this.logoabstop=Float.parseFloat(getText(propprefix+"."+reportpropertyname+"."+proplogotopkey));
			this.autosummary= getText(propprefix+"."+reportpropertyname+"."+propautosummarykey).trim();
			System.out.println("rep6"+propprefix+"."+reportpropertyname+"."+propautosummarykey);
			this.countrecord= getText(propprefix+"."+reportpropertyname+"."+propcountrecordkey).trim();
			
			if(!countrecord.equals("YES")){
				this.countrecord="NO";
			}
			String logoyes = getText(propprefix+"."+reportpropertyname+"."+proplogo).trim();
			System.out.println("Logo Yes ===============>"+logoyes);
			if(logoyes.equals("YES")){
				this.imageflag=0;
				this.reportlogo=comUtil().GetUrlReportIcon(req,getText(propprefix+"."+reportpropertyname+"."+proplogokey).trim());
				this.logopos=getText(propprefix+"."+reportpropertyname+"."+proplogoposkey).trim();
				System.out.println("Logo Possition"+logopos);
				if(logopos.equals("LEFT")){
					this.logoabsleft=Float.parseFloat(getText(propprefix+"."+proplogoleftxkey).trim());
					this.logoabstop=Float.parseFloat(getText(propprefix+"."+proplogoleftykey).trim());
				}
				else{
					this.logoabsleft=Float.parseFloat(getText(propprefix+"."+proplogorightxkey).trim());
					this.logoabstop=Float.parseFloat(getText(propprefix+"."+proplogorightykey).trim());
				}
			}	
		
	}

	public  newPDFReportGenerator(FileOutputStream output_streamObj,String propertyname, ServletContext context ) throws DocumentException{
		//System.out.println("Called PDFReportGenerator multi =====>");
		try{
			
			Rectangle layout = new Rectangle(PageSize.A4);
			layout.setBackgroundColor(BaseColor.PINK);
			    
			documentObj = new Document(layout); 
			System.out.println("Contractor Called");
			//String propertyfilepath= comUtil().GetUrlReportIcon(context.getr,"PDFReportGenerator.properties");			
			//FileReader reader = new FileReader(propertyfilepath);
			//Properties properties = new Properties();
			
			
			InputStream is = context.getResourceAsStream("/WEB-INF/ReportProperties.properties");
			Properties properties = new Properties();
			properties.load(is); 
			
			
			//Properties properties = (Properties)context.getAttribute("PDFReportGenerator.properties"); 
			//properties.load(reader);
			System.out.println("Properties :" + properties );
			PdfWriter writer=PdfWriter.getInstance(documentObj,output_streamObj);
			documentObj.open();
			documentObj.setPageSize(PageSize.A4);
		    PdfContentByte cb = writer.getDirectContent();
		    cb.moveTo(PageSize.A4.RIGHT / 2, PageSize.A4.LEFT/ 2);
		    cb.lineTo(PageSize.A4.TOP / 2, PageSize.A4.BOTTOM);
		    cb.stroke();
			this.reportpropertyname=propertyname;
			System.out.println("------------propertyname---------------"+propertyname);
			this.fontfamilyname=properties.getProperty(propprefix+"."+reportpropertyname+"."+propfontkey).trim();
			System.out.println("logo key "+propprefix+"."+reportpropertyname+"."+proplogokey);			
			System.out.println("logo geteText: "+properties.getProperty(propprefix+"."+reportpropertyname+"."+proplogokey).trim());
			this.filename=properties.getProperty(propprefix+"."+reportpropertyname+"."+propsaveaskey).trim();			
			this.reporttitles =properties.getProperty(propprefix+"."+reportpropertyname+"."+proptitlekey).trim();
			System.out.println("HAI"+propprefix+"."+reportpropertyname+"."+proptitlekey);
			this.reportheader =properties.getProperty(propprefix+"."+reportpropertyname+"."+propheaderkey).trim();
			System.out.println("D"+propprefix+"."+reportpropertyname+"."+propheaderkey);
			this.reporttablewidth =Integer.parseInt(properties.getProperty(propprefix+"."+reportpropertyname+"."+propreportwidthkey).trim());
			System.out.println("DDD"+propprefix+"."+reportpropertyname+"."+propreportwidthkey);
			this.reportsumfield =properties.getProperty(propprefix+"."+reportpropertyname+"."+propsumkey).trim();
			System.out.println("REPORT1"+propprefix+"."+reportpropertyname+"."+propsumkey);
			this.reportsummarytitle= properties.getProperty(propprefix+"."+reportpropertyname+"."+propsummarytitlekey).trim();
			System.out.println("REPORT2"+propprefix+"."+reportpropertyname+"."+propsummarytitlekey); 
			this.reportsummarywidth = Integer.parseInt(properties.getProperty(propprefix+"."+reportpropertyname+"."+propsummarywidthkey).trim());
			System.out.println("REP4"+propprefix+"."+reportpropertyname+"."+propsummarywidthkey);
			this.precision_point = Integer.parseInt(properties.getProperty(propprefix+"."+reportpropertyname+"."+propprecision_pointkey).trim());
			System.out.println("REP5"+propprefix+"."+reportpropertyname+"."+propprecision_pointkey);
			//this.logoabsleft=Float.parseFloat(properties.getProperty(propprefix+"."+reportpropertyname+"."+proplogoleftkey));
			//this.logoabstop=Float.parseFloat(properties.getProperty(propprefix+"."+reportpropertyname+"."+proplogotopkey));
			this.autosummary= properties.getProperty(propprefix+"."+reportpropertyname+"."+propautosummarykey).trim();
			System.out.println("rep6"+propprefix+"."+reportpropertyname+"."+propautosummarykey);
			this.countrecord= properties.getProperty(propprefix+"."+reportpropertyname+"."+propcountrecordkey).trim();
			
			if(!countrecord.equals("YES")){
				this.countrecord="NO";
			}
			String logoyes = properties.getProperty(propprefix+"."+reportpropertyname+"."+proplogo).trim();
			System.out.println("Logo Yes ===============>"+logoyes);
			if(logoyes.equals("YES")){
				this.imageflag=0;
			//	this.reportlogo=comUtil().GetUrlReportIcon(req,properties.getProperty(propprefix+"."+reportpropertyname+"."+proplogokey).trim());
				this.logopos=properties.getProperty(propprefix+"."+reportpropertyname+"."+proplogoposkey).trim();			
				if(logopos.equals("LEFT")){
					this.logoabsleft=Float.parseFloat(properties.getProperty(propprefix+"."+proplogoleftxkey).trim());
					this.logoabstop=Float.parseFloat(properties.getProperty(propprefix+"."+proplogoleftykey).trim());
				}
				else{
					this.logoabsleft=Float.parseFloat(properties.getProperty(propprefix+"."+proplogorightxkey).trim());
					this.logoabstop=Float.parseFloat(properties.getProperty(propprefix+"."+proplogorightykey).trim());
				}
			}	
		}
		catch(Exception e){
			e.printStackTrace();
		}
		//return documentObj;
	}

	
	public void createSimplePDF(String reporttitles,String reportheader,List reportdata_list,String reportsumfield,int titlealign,int tablealign,int reporttablewidth) throws MalformedURLException, DocumentException, IOException{		
		//addPDFTitles(documentObj,reporttitles,titlealign);
		addPDFHeading(reportheader,tablealign,reporttablewidth);
		addPDFData(reportsumfield,reportheader,reportdata_list);
		//closePDF(documentObj);
	}
	

public void addPDFTitles(String titles,int setalign) throws DocumentException, MalformedURLException, IOException{
		//try {		

			System.out.println("Called addPDFTitles =====>");
			String datestring =" Generated time :";
			int headerlen=1;
			String reporttiles[] = titles.split(",");			
			int titlelen =reporttiles.length;	

			System.out.println("headerlen ======>"+headerlen);
		
			
		    
			
			if(imageflag==0){				
				Image logo =Image.getInstance(reportlogo);
				logo.setAlignment(Image.RIGHT | Image.TEXTWRAP);
				logo.scalePercent(45);
				System.out.println(logoabsleft+","+logoabstop);
				logo.setAbsolutePosition(logoabsleft,logoabstop);
				documentObj.add(logo);
				imageflag++;
			}
			for(int i=0;i<titlelen;i++){				
				//Phrase phrasetitle = new Phrase();
				Paragraph paragraph_title=new Paragraph(20);
				Phrase phrasereporthead = new Phrase();
			//	System.out.println("col"+i+reporttiles);
				Font font = FontFactory.getFont(fontfamilyname,fontsize,1);
				font.setColor(WebColors.getRGBColor(proptableheader_fontcolor));
				Chunk pdftitle;
				if(fontsize==20){
					
					pdftitle = new Chunk(reporttiles[i].toUpperCase()+"\n", font);
					Rectangle pageSize = new Rectangle(565,20,30,771);
					//1st co-ordinate - moves to left,2nd co-ordinate - increases bottom,3rd co-ordinate - moves to left,4th co-ordinate - increases top 					
					pageSize.setBackgroundColor(new BaseColor(230, 221, 238));					
					
					Rectangle pageSize1 = new Rectangle(565,20,30,820);
					pageSize1.setBorderWidthLeft(0.5f);
					pageSize1.setBorderWidthBottom(0.5f);
					pageSize1.setBorderWidthRight(0.5f);
					pageSize1.setBorderWidthTop(0.5f);
					pageSize1.setBorderColorLeft(BaseColor.BLACK);
			        pageSize1.setBorderColorBottom(BaseColor.BLACK);
			        pageSize1.setBorderColorRight(BaseColor.BLACK);
			        pageSize1.setBorderColorTop(BaseColor.BLACK);
					
					
					documentObj.add(pageSize);					
					documentObj.add(pageSize1);
				    //pdftitle.setBackground(BaseColor.RED);
				    //document.add(new Paragraph("Text"));
				}
				else{
					pdftitle = new Chunk(reporttiles[i]+"\n", font);
				}				
				phrasereporthead.add(pdftitle);				
				paragraph_title.add(phrasereporthead);
				paragraph_title.setAlignment(setalign);
				fontsize=10;				
				documentObj.add(paragraph_title);				
			}
			
			//txnheadertable.completeRow();
				
			
			//document.add(paragraph_title);
		/*}
		catch(Exception e) {
			System.out.println("Error "+e);
			e.printStackTrace();			
		}*/
	}	






	public void addPDFHeading(String ReportHeader,int tablealign,int percentagewidth){
		//try{
			//float[] txncolumnWidths = new float[] {35f,20f,20f, 20f, 20f,30f,20f};
			//txntable.setWidths(txncolumnWidths);
			int tds =1;
			System.out.println("Called addPDFHeading =====>");
			System.out.println("test header "+ReportHeader);
			String header[]= ReportHeader.split(",");
			tds=header.length;
			txntable = new PdfPTable(tds);
			txntable.setHorizontalAlignment(tablealign);
			txntable.setSpacingBefore(20);
			txntable.setWidthPercentage(percentagewidth);
			//System.out.println("####################################################"+tableheader_color);
			if(!("".equals(ReportHeader))){					
				for(int i=0;i<header.length;i++){
					PdfPCell txncell = new PdfPCell();
					txncell.setBorderColor(BaseColor.WHITE);
					txncell.setBorder(Rectangle.BOX); 
					txncell.setBackgroundColor(WebColors.getRGBColor("#"+tableheader_color));
					Font font = FontFactory.getFont(fontfamilyname,fontsize,Font.BOLD);
					font.setColor(WebColors.getRGBColor(proptableheader_fontcolor));
					txncell.setPhrase(new Phrase(header[i].toUpperCase(),font));
					txncell.setHorizontalAlignment(Element.ALIGN_CENTER);
					txncell.setVerticalAlignment(Element.ALIGN_CENTER);
					txncell.setPadding(2f);
					txntable.addCell(txncell);
				}
			}

			System.out.println("Added Heading In Report");
		/*}
		catch(Exception e) {
			System.out.println("Error "+e);
			e.printStackTrace();			
		}*/
	}	
	
	public void addPDFDataByGroup(String totalneededfields,String Header,List reportdata_list,String groupbyField) throws DocumentException, MalformedURLException, IOException{
		System.out.println("Called addPDFDataByGroup =====>");
//		System.out.println("report list in PDF GEN =====> "+ reportdata_list);
//		System.out.println("Grouby ----------------->" + groupbyField);
		ListIterator itr_card_list	=	reportdata_list.listIterator();
		String sumoffield[]= totalneededfields.split(",");
		List multilist = new ArrayList<>();
		LinkedHashMap sublistmap = new LinkedHashMap();
		String previousproduct="NO_PRODUCT";
		while(itr_card_list.hasNext()) {			
				LinkedHashMap mapper_orderdetails	   = (LinkedHashMap)itr_card_list.next();
				String resultqry_groupvalu =  (String) mapper_orderdetails.get(groupbyField);
				System.out.println("===="+previousproduct+"====");
				if(previousproduct.equals("NO_PRODUCT")){
					multilist  = new ArrayList<>();
					
					previousproduct=resultqry_groupvalu;
					System.out.println("Create New List");
				}else if(!previousproduct.equals(resultqry_groupvalu)){
					System.out.println("Add in new Group");					
					sublistmap.put(previousproduct, multilist);
					System.out.println("$$$$$$$$$$$$$$$resultqry_groupvalu$$$$$$$$$$$$$$$"+resultqry_groupvalu);
					previousproduct=resultqry_groupvalu;
					multilist  = new ArrayList<>();	   					
				}
				System.out.println("before remove map "+mapper_orderdetails);
				mapper_orderdetails.remove(groupbyField);
				System.out.println("after remove map "+mapper_orderdetails);
				System.out.println("added values in List");   				
				multilist.add(mapper_orderdetails);
				System.out.println(" Multilist =====> "+sublistmap);
		}
		System.out.println(" Sub list "+sublistmap.size());		
		sublistmap.put(previousproduct, multilist);
		System.out.println(" Multilist =====> "+sublistmap);
		System.out.println(" Multilist =====> "+sublistmap);
		Iterator keyitr = sublistmap.keySet().iterator();
		int arrlen =sublistmap.size();
		String grpby[]=new String[arrlen];
		int i=0;
		while(keyitr.hasNext()){
		  String getkey = (String)keyitr.next();				  
		  List pdfreportlist= (List)sublistmap.get(getkey);
		  System.out.println("final result ======"+pdfreportlist);
		  System.out.println("Group by key "+ getkey);
		  addPDFTitles( getkey, ALIGN_LEFT);
		  addPDFHeading(this.reportheader,ALIGN_LEFT,this.reporttablewidth);
		  addPDFData(this.reportsumfield,this.reportheader,pdfreportlist);
		  grpby[i]=getkey;
		  i++;
		}
		System.out.println("summary test "+autosummary);
		System.out.println(""+ !("".equals(sumoffield)));
		System.out.println("sumoffield len "+sumoffield.length);
		if((autosummary.equals("ON")) && (!("".equals(sumoffield)))){
			System.out.println("Called  Summary");
			addDefaultSummary(this.reportsummarytitle,this.reportsummarywidth,grpby);			
		}		
}
	public void addPDFTableWithTitle(String tabletitles,String tableheader,List tabledatalist,String sumneedfield,int titlealign,int tablealign,int tablewidth) throws MalformedURLException, DocumentException, IOException{
	   addPDFTitles( tabletitles, titlealign);
	   addPDFHeading(tableheader,tablealign,tablewidth);
	   addPDFData(sumneedfield,tableheader,tabledatalist);
	}
	public void createSimplePDFwithtitle(String reporttitles,String reportheader,List reportdata_list,String reportsumfield,int titlealign,int tablealign,int reporttablewidth) throws MalformedURLException, DocumentException, IOException{
		addPDFTitles(reporttitles,titlealign);
		addPDFHeading(reportheader,tablealign,reporttablewidth);
		addPDFData(reportsumfield,reportheader,reportdata_list);
	}

	public void addPDFData(String totalneededfields,String Header,List reportdata_list) throws DocumentException{
		//try{
	//		System.out.println("Called addPDFData =====>");
		    String sumoffield[]= totalneededfields.split(",");
			List <String> sumfieldslist = Arrays.asList(sumoffield);
			List <String> alignrightlist = Arrays.asList(alignright);
			System.out.println(" sumfieldslist ================> "+sumfieldslist);
//			System.out.println(" Alignright list : "+alignright);
			//List fieldvalues = new ArrayList();
			//System.out.println("grant Field List ---------------> "+sumfieldslist);
			System.out.println(" Header List --------------->"+sumfieldslist);
			System.out.println("Report Header"+Header);
		//	System.out.println("result List"+reportdata_list);
			String ReportHeader[] = Header.split(",");			
			Iterator itr = reportdata_list.iterator();
			Iterator keyitr=null;
			int loop = 0,rowcount = 0;
			this.gradtotalrow= new HashMap();
			PdfPCell txn_cell = new PdfPCell();
			//System.out.println(" Report List =====>"+reportdata_list);
			System.out.println("report header length "+ReportHeader.length);
			while(itr.hasNext())
			{
				LinkedHashMap keymap = (LinkedHashMap)itr.next();
				//Iterator keyItr = keymap.keySet().iterator();
				if(loop==0){
					keyitr = keymap.keySet().iterator();
				}
				int colcount=0;
				
				
				int sno =0;
				while(keyitr.hasNext())
				{ 
					String bgcolor1;			
					String datakey = (String)keyitr.next();
					String datavalue=(String)keymap.get(datakey);
					System.out.println(" Datavalue===============> "+datavalue);
					
					int align;
					
					if((rowcount%2)==0)
						bgcolor1 = "#"+tableoddrow_color;
				    else
				    	bgcolor1 = "#"+tableevenrow_color;
					//System.out.println("tableoddrow_color "+tableoddrow_color);
//					System.out.println(ReportHeader[colcount]);
//					System.out.println("data "+datavalue);
					txn_cell.setBorderColor(BaseColor.WHITE);
					txn_cell.setBorder(Rectangle.BOX); 
					txn_cell.setBackgroundColor(WebColors.getRGBColor(bgcolor1));
					txn_cell.setPhrase(new Phrase( datavalue ,new Font(Font.getFamily(fontfamilyname), 7, Font.NORMAL)));					
					// Calculation for grant total
					
					if(sumfieldslist.contains(ReportHeader[colcount].toUpperCase())){
						System.out.println("calculate sum ");
						align=2; // Cell Align Right 
					    //Float fObj2 = Float.valueOf(datavalue);
					    //system.out.println("float value "+fObj2);
					   // float datainint = Float.parseFloat(datavalue);
				    	boolean testval= this.gradtotalrow.containsKey(ReportHeader[colcount]);
				    	BigDecimal bigdecimaldatavalue = new BigDecimal(datavalue.toString());
//				    	System.out.println("get value to total"+bigdecimaldatavalue);
				    	if(testval){
				    		String strtemp = gradtotalrow.get(ReportHeader[colcount]).toString();				    		
				    		BigDecimal total = new BigDecimal(strtemp);
				    		total=total.add(bigdecimaldatavalue);
				    		this.gradtotalrow.put(ReportHeader[colcount],total);
				    	}else{
				    		String formatedvalue	= String.format("%."+this.precision_point+"f", bigdecimaldatavalue);
//				    		System.out.println("total formatedvalue"+formatedvalue);
				    		this.gradtotalrow.put(ReportHeader[colcount],formatedvalue);
				    	}
					}					
					else if(alignrightlist.contains(ReportHeader[colcount].toUpperCase()))
					{
//					    System.out.println("align right************************");
						align=2;  //  Cell Align Right
					}
					else{
						align=1;  //  Cell Align Center
					}
					txn_cell.setHorizontalAlignment(align);
					txntable.addCell(txn_cell);
					colcount=colcount+1;
				}
				rowcount = rowcount + 1;
			}			
			// grant Total Row  for report
//			System.out.println("grant total ===================> "+this.gradtotalrow.toString());
			if(this.gradtotalrow.size()>0){
				for(int j=0;j<ReportHeader.length;j++)
				{
					String addvalue= "";
					//system.out.println(" header value +++++++++++++++"+ReportHeader[j]);
					if(sumfieldslist.contains(ReportHeader[j])){
						Object granttotal_obj= this.gradtotalrow.get(ReportHeader[j]);
						String stramount=granttotal_obj.toString();
						float floatamount= Float.valueOf(stramount);
						if((floatamount%1)==0){
							//system.out.println(" iiiiiiiiiiiiiiiiiiiiii");	
							addvalue=granttotal_obj.toString();
						}else{
							//system.out.println(" elseeeeeeeeeeeeeeeeeeeeee ");
							addvalue=granttotal_obj.toString();
						}
						//system.out.println(" Add values =====> "+addvalue);	
					}
					txn_cell.setPhrase(new Phrase( addvalue ,new Font(Font.getFamily(fontfamilyname), 7, Font.NORMAL)));
					//txn_cell.setBackgroundColor(bgcolor1);
					txn_cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
					txntable.addCell(txn_cell);
				}
			}
			txntable.completeRow();			
			documentObj.add(txntable);
			
			if(countrecord.equals("YES")){
				noofRecords(rowcount,2);
			}
		/*}
		catch(Exception e) {
			System.out.println("Error "+e);
			e.printStackTrace();			
		}*/
	}
	
	public void addDefaultSummary(String title,int width,String [] groupbyname) throws MalformedURLException, DocumentException, IOException{
//		System.out.println("Called Default Summary =====>");
		Map summarymap = getTotalAmtMap();
		System.out.println("summary map "+summarymap);
		Iterator summaryitr = getTotalAmtMap().keySet().iterator();
	//	 BigDecimal summarytotal = new BigDecimal("0");
		 List summarylist= new ArrayList();
		 int i=0;
		 while(summaryitr.hasNext()){
			 Map summarytotalmap = new LinkedHashMap();
			 String getsumkey = (String)summaryitr.next();
			 BigDecimal bigtransamt = (BigDecimal)summarymap.get(getsumkey);
			 String formatedvalue	= String.format("%."+precision_point+"f", bigtransamt);			 
			 summarytotalmap.put("summarykey", groupbyname[i]);
			 summarytotalmap.put("summaryvalue", formatedvalue);
			// summarytotal = summarytotal.subtract(bigtransamt);
			 summarylist.add(summarytotalmap);
			 i++;
		 }
//		 System.out.println("summary list "+ summarylist);
		 addPDFSummary("Amount",this.reportsummaryheader,summarylist,title,width);
		
	}
	public void addPDFSummary(String totalneededfields, String ReportHeader,List reportdata_list,String summarytitle,int sumwidth) throws MalformedURLException, DocumentException, IOException{
		//try{		
//			System.out.println("Called Summary =====>");
		    //system.out.println("Report Title"+summarytitle);
			addPDFTitles(summarytitle,ALIGN_LEFT);
//			System.out.println("summary " + ReportHeader);
			addPDFHeading(ReportHeader,ALIGN_LEFT,sumwidth);
			addPDFData(totalneededfields,ReportHeader,reportdata_list);
		/*}
		catch(Exception e){
			System.out.println("Error "+e);
			e.printStackTrace();
		}*/
	}
	public void closePDF(){
		//try{
			System.out.println("Close Report");
			documentObj.close();
		/*}
		catch(Exception e) {
			System.out.println("Error "+e);
			e.printStackTrace();			
		}*/
	}
	public String getTotalAmtbyKey(String key){
		String sumvalues;		
		System.out.println("Key : "+key);
		System.out.println("values : "+gradtotalrow.keySet());		
		sumvalues=this.gradtotalrow.get(key).toString();
		System.out.println(" total sum amt"+sumvalues);
		return sumvalues;
	}
	public void noofRecords(int totalrecords,int setalign) throws DocumentException{
		Phrase rowcount = new Phrase();
		Paragraph pararowcount= new Paragraph();
		Font font = FontFactory.getFont(fontfamilyname,8,1);							
		Chunk pdftitle = new Chunk("No of Records "+totalrecords+"\n", font);		
		rowcount.add(pdftitle);				
		pararowcount.add(rowcount);
		pararowcount.setAlignment(setalign);	
		documentObj.add(pararowcount);
	}
	public Map getTotalAmtMap(){
		List s = new ArrayList();
		s.add(this.gradtotalrow);
		System.out.println("summary total :"+s);
		return this.gradtotalrow;
	}
	
	public void addSingleHeader(Document document,int totalcoloum,String reportdata_list,int HeaderAlign,int tablewidth) throws MalformedURLException, DocumentException, IOException{
		System.out.println("Called sungle header");
		PdfPTable headertable = new PdfPTable(1);	
		String [] data= reportdata_list.split(",");
		ArrayList headerdatas = new ArrayList(Arrays.asList(data));
		headertable.setHorizontalAlignment(HeaderAlign);
		headertable.setSpacingBefore(20);
		headertable.setWidthPercentage(tablewidth);
		PdfPCell header_cell1 = new PdfPCell();
		PdfPTable tableleft = addPDFHeaderData(totalcoloum,headerdatas);
		header_cell1.addElement(tableleft);
		headertable.addCell(header_cell1);
		headertable.completeRow();				
		document.add(headertable);	
  }
	public void addLeftRightHeader(Document document,int totalcoloum,String reportdata1,String reportdata2) throws MalformedURLException, DocumentException, IOException{
		PdfPTable headertable = new PdfPTable(3);
		String [] data1= reportdata1.split(",");
		ArrayList headerdata1 = new ArrayList(Arrays.asList(data1));
		String [] data2= reportdata2.split(",");
		ArrayList headerdata2 = new ArrayList(Arrays.asList(data2));
		headertable.setHorizontalAlignment(ALIGN_LEFT);
		headertable.setSpacingBefore(20);
		headertable.setLockedWidth(true);
		headertable.setTotalWidth(new float[]{ 255, 10, 255 });
		PdfPCell header_cell1 = new PdfPCell();
		PdfPCell header_cell2 = new PdfPCell();
		PdfPCell header_cell3 = new PdfPCell();
		header_cell2.setBorder(0);
		PdfPTable tableleft = addPDFHeaderData(1,headerdata1);		
		PdfPTable tableright= addPDFHeaderDatatemplate1(totalcoloum,headerdata2);
		header_cell1.addElement(tableleft);	
		header_cell1.setBorder(0);
		header_cell2.setPhrase(new  Phrase(""));
		header_cell3.addElement(tableright);		
		header_cell3.setBorder(0);
		headertable.addCell(header_cell1);
		headertable.addCell(header_cell2);	
		headertable.addCell(header_cell3);		
		headertable.completeRow();		
		document.add(headertable);		
   }
	public void addLeftRightHeadertemplate1(int totalcoloum,String reportdata1,String reportdata2) throws MalformedURLException, DocumentException, IOException{
		PdfPTable headertable = new PdfPTable(3);
		String [] data1= reportdata1.split(",");
		ArrayList headerdata1 = new ArrayList(Arrays.asList(data1));
		String [] data2= reportdata2.split(",");
		ArrayList headerdata2 = new ArrayList(Arrays.asList(data2));
		headertable.setHorizontalAlignment(ALIGN_LEFT);
		headertable.setSpacingBefore(20);
		headertable.setLockedWidth(true);
		headertable.setTotalWidth(new float[]{ 255, 10, 255 });
		PdfPCell header_cell1 = new PdfPCell();
		PdfPCell header_cell2 = new PdfPCell();
		PdfPCell header_cell3 = new PdfPCell();
		header_cell2.setBorder(0);
		PdfPTable tableleft = addPDFHeaderData(1,headerdata1);		
		PdfPTable tableright= addPDFHeaderDatatemplate1(totalcoloum,headerdata2);
		header_cell1.addElement(tableleft);	
		header_cell1.setBorder(0);
		header_cell2.setPhrase(new  Phrase(""));
		header_cell3.addElement(tableright);		
		header_cell3.setBorder(0);
		headertable.addCell(header_cell1);
		headertable.addCell(header_cell2);	
		headertable.addCell(header_cell3);		
		headertable.completeRow();		
		documentObj.add(headertable);		
   }
	public PdfPTable addPDFHeaderData(int coloumcount,ArrayList reportdata_list) throws DocumentException{
			//System.out.println("result List"+reportdata_list);
			//System.out.println(" Report List =====>"+reportdata_list);
			PdfPTable table = new PdfPTable(coloumcount);
			table.setHorizontalAlignment(ALIGN_LEFT);
			table.setWidthPercentage(100);
			table.getDefaultCell().setBorder(0);
			PdfPCell txn_cell = new PdfPCell();
			for (int i=0; i < reportdata_list.size(); i++) {
				String headerval = (String)reportdata_list.get(i);
				txn_cell.setPhrase(new Phrase( headerval ,new Font(Font.getFamily(fontfamilyname), 8, Font.NORMAL)));
				txn_cell.setHorizontalAlignment(ALIGN_LEFT);
				txn_cell.setBorder(0);
				table.addCell(txn_cell);
			}			
			table.completeRow();		
			return table; 
	}
	
	public PdfPTable addPDFHeaderDatatemplate1(int coloumcount,ArrayList reportdata_list) throws DocumentException{
		//System.out.println("result List"+reportdata_list);
		//System.out.println(" Report List =====>"+reportdata_list);
		PdfPTable table = new PdfPTable(coloumcount);
		table.setHorizontalAlignment(ALIGN_LEFT);
		table.setWidthPercentage(100);
		table.getDefaultCell().setBorder(1);
		PdfPCell txn_cell = new PdfPCell();
		System.out.println(tableheader_color);
		for (int i=0; i < reportdata_list.size(); i++) {
			String headerval = (String)reportdata_list.get(i);
			String bgcolor1;
			if(i<coloumcount)
				bgcolor1 = "#"+tableheader_color;
		    else
		    	bgcolor1 = "#"+tableevenrow_color;
			txn_cell.setBorder(1);
			txn_cell.setBorderColor(BaseColor.WHITE);
			txn_cell.setBorder(Rectangle.BOX); 
			txn_cell.setBackgroundColor(WebColors.getRGBColor(bgcolor1));
			txn_cell.setPhrase(new Phrase( headerval ,new Font(Font.getFamily(fontfamilyname), 7, Font.NORMAL)));
			txn_cell.setHorizontalAlignment(ALIGN_CENTER);
			
			table.addCell(txn_cell);
		}			
		table.completeRow();		
		return table; 
}

}
