<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="/struts-tags" prefix="s"%>
<html>
<body>
<s:form action="sendMailForgetpasswordAction" namespace="/" name="sendmail">
   <label for="to">To</label>
   <input type="text" name="to"/><br/>
   <label for="subject">Subject</label>
   <input type="text" name="subject"/><br/>
   <label for="body">Body</label>
   <input type="text" name="body"/><br/>
   <input type="submit" value="Send Email"/>
   <input type="submit" name="sum" value="Send"/>
</s:form>
</body>
</html>
