<%@page language="java" contentType="text/html"%>
<%@page import="java.io.*, java.util.*, java.sql.*,java.text.*" %>
<jsp:useBean id="dbcon" class="connection.Dbcon" />
<html><head><title>CodeNarc Report: Sample Project</title>

</head>
<%
String msg=request.getParameter("msg")==null?"":request.getParameter("msg").trim();
String txtLayoutViewer=request.getParameter("Query")==null?"":request.getParameter("Query").trim();

String txtLine=request.getParameter("txtLine")==null?"":request.getParameter("txtLine").trim();
String txtColumn=request.getParameter("txtColumn")==null?"":request.getParameter("txtColumn").trim();


String curr_cnt =request.getParameter("curr_cnt")==null?"1":request.getParameter("curr_cnt").trim();
String rno = request.getParameter("RNO")==null?"":request.getParameter("RNO").trim();

%>

<html>
<script>

</script>

<head>
    <style type="text/css">
        body, td, tg, input, select {
            font-family: Verdana;
            font-size: 10px;
        }
    </style>
</head>
<body >
<img class='logo' src="image/orient/Azizi Logo.png" alt='Orient Bank' align='right'/>
<%
String reportq = "SELECT * FROM IFD_PDFREPORTGENRATOR where RNO='"+rno+"'";
System.out.println("reportq---"+reportq);    
String reportname="";
String reportQuery="";
String hfont="";
String hfontcolor="";
String hfontsize="";
String T_Hbgcolor="";
ResultSet rrs=null;
Connection rcon=null;    
rcon=dbcon.getDBConnection();
Statement rst=rcon.createStatement();
rrs = rst.executeQuery(reportq);
while(rrs.next())    
{
	reportname = rrs.getString("REPORTNAME"); 
	reportQuery = rrs.getString("REPORTQUERY");
	hfont = rrs.getString("TABLE_HFONT");
	hfontcolor = rrs.getString("TABLE_HCOLOR");
	hfontsize = rrs.getString("TABLE_HFONT_SIZE");
	T_Hbgcolor = rrs.getString("TABLE_HBGCOLOR");
	
	
}
rrs.close();
rst.close();
rcon.close();

String condition = "";
if(rno.equals("2"))
{
	String filename = request.getParameter("filename")==null?"":request.getParameter("filename").trim();
	reportQuery = reportQuery.replace("COND1", filename);
	reportQuery = reportQuery.replace("COND2", filename);     
}

%>

<h1 nowrap></h1>
<%=reportname %>
<form name="Executeqry1" action=post>
<INPUT TYPE="hidden" NAME="msg">
<INPUT TYPE="hidden" NAME="text">
<INPUT TYPE="hidden" NAME="curr_cnt" value=<%=curr_cnt%>>
<table>
<%
SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");//dd/MM/yyyy
java.util.Date now = new java.util.Date();
String strDate = sdfDate.format(now);
%>   
<tr>
<td >Date:</td><td><%=strDate%></td>
</tr>
<tr>
<td >Generated with:</td><td><a >DebitCardMan</a>
</td>
</tr>
</table>
<%

rcon=dbcon.getDBConnection();
rst=rcon.createStatement();
rrs = rst.executeQuery("SELECT COLUMN_NAME FROM IFD_PDFREPORTCONFIG where RNO='"+rno+"' ORDER BY ORDER_BY");
			String strQuery= "";
			String sFilename="";
			String org_qry = "";

			ResultSet rs=null;
			Connection con=null;
			con=dbcon.getDBConnection();
			Statement st=con.createStatement();
			
			strQuery =  txtLayoutViewer.trim();
			org_qry = txtLayoutViewer.trim();

			int int_curr = Integer.parseInt(curr_cnt);
						
			   

				System.out.println("The Eod Status Query:="+reportQuery );
				try    
				{
					rs = st.executeQuery(	" SELECT * FROM ( "+
											" SELECT ROWNUM SNO,SRC_QRY.* FROM  "+
											" ( "+ reportQuery+
											" ) SRC_QRY "+
											" )  ");
					ResultSetMetaData rsmd =rs.getMetaData();

					%>   
					<div >
					<table width="100%" border=1 cellpadding=0 cellspacing=0>
					<tr  >
					
					<%
					for(int j=0;j<rsmd.getColumnCount();j++)
					{
						%>
						<th bgcolor="<%=T_Hbgcolor%>" ><font face="<%=hfont %>" color="<%=hfontcolor%>" size="<%=hfontsize%>"> <%=rsmd.getColumnName(j+1)%></font></th>
						<%
					}
					%>
					</tr>
					<%
					int count=0;
					while(rs.next())
					{
						count++;
						%><tr>
						<%
						for(int j=0;j<rsmd.getColumnCount();j++)
						{
							%>
							<td ><%=rs.getString(j+1)==null?"'":rs.getString(j+1)%></td>
							<%
						}
						%></tr><%

					}
					%>
					</table>
					</div>

					<%
					}
					catch(Exception e)
					{
						StackTraceElement trace[] =null; 
						trace = e.getStackTrace() ;

						for(int i=0 ;i<trace.length;i++){
						if(i==1) break;
						out.println("<tr><td>");
						out.println("<b>Error :" + e.toString() + "</b>"); 
						out.println("</td></tr>");
						}
					}
				finally
				{
					if(rs!=null)
							rs.close();
					if(st!=null)
							st.close();
					if(con!=null)
							con.close();
				}
			
			
		%>
		
		<input type="hidden" name="txtLayoutViewer" value="<%=strQuery%>">

    </form>
</body>
</html>


