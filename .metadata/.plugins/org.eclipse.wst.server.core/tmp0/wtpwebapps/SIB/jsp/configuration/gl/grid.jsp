<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
  <head>
       <link rel="stylesheet" type="text/css" href="style/jmesa/jmesa.css"></link>
       <link rel="stylesheet" type="text/css" href="style/jmesa/example.css"></link>
       <script type="text/javascript" src="js/jmesa/jmesa.js"></script>
		<script type="text/javascript" src="js/jmesa/jquery.jmesa.js"></script>
		
		<script type="text/javascript">
function onInvokeAction(id) {
    createHiddenInputFieldsForLimitAndSubmit(id);
}
</script>
  </head>
  <body>
         ${presidents}
  </body>
</html>