<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Indéxation Documents</title>
</head>
<body>
<table border="1" bordercolor="blue" cellpadding="2" cellspacing="2">
	<tr>
		<td style="width: 300px; text-align: center;" colspan="3">
		<s:form action="teste">
			<s:token/>
			<s:textfield label="Teste" name="teste" required="true" id="tName" value=""/>
			<s:submit value="Tester" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
</table>
</body>
</html>