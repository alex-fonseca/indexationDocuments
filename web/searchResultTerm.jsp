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
		<td style="width: 160px; text-align: center;" colspan="1">
			<b>Terme:</b><s:property value="termName"/>
		</td>
		<td style="width: 160px; text-align: center;" colspan="1">
			<b>Nro Docs:</b> <s:property value="terme.df" />
		</td>
	</tr>
	<tr>
		<td style="width: 160px; text-align: center;" colspan="2"><b>Doc nro</b></td>
		<td style="width: 160px; text-align: center;" colspan="1"><b>Score</b></td>
	</tr>
	<s:iterator value="terme.docFreqList">
	<tr>
		<td style="width: 160px; text-align: center;" colspan="2"><s:property value="nroDoc" /></td>
		<td style="width: 160px; text-align: center;" colspan="1"><s:property value="score" /></td>
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