<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Recherche d'information</title>
</head>
<body>
<table border="1" bordercolor="green" cellpadding="2" cellspacing="2">
	<tr>
		<td style="width: 550px; text-align: center;" colspan="7">
			<b>Nro Doc:</b><s:property value="docAP.nroDoc"/>
		</td>
	</tr>
	<tr>
		<td style="width: 550px; text-align: left;" colspan="7">
			<b>Texte:</b> <s:property value="docAP.text"/>
		</td>
	</tr>
	<tr>
		<td style="width: 550px; text-align: center;" colspan="7">
			<s:form action="return">
				<s:token/>
				<s:submit value="Retourner" cssClass="btn btn-primary"/>
			</s:form>
		</td>
	</tr>
</table>
</body>
</html>