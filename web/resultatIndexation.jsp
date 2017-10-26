<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Indéxation de Documents</title>
</head>
<body>
<table border="1" bordercolor="purple" cellpadding="2" cellspacing="2">
	<tr>
		<!--td style="width: 160px; text-align: center;" colspan="3">
			Doc num: <b><s:property value="docNO"/></b>
		</td-->
		<td style="width: 800px; text-align: center;" colspan="2">
			<b>Traduction:</b> <s:property value="teste"/>
		</td>
	</tr>
	<s:iterator value="bigrams" var="big">
	<tr>
		<td style="width: 480px; text-align: center;" colspan="3">${big}</td>
	</tr>
	</s:iterator>
	<tr>
		<td style="width: 160px; text-align: center;" colspan="3">
			<s:form action="return">
				<s:token/>
				<s:submit value="Retourner" cssClass="btn btn-primary"/>
			</s:form>
		</td>
	</tr>
</table>
</body>
</html>