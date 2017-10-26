<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Indéxation de Documents</title>
</head>
<body>
<!-- s:form action="recherche"-->
<table border="1" bordercolor="purple" cellpadding="2" cellspacing="2">
	<tr>
		<td style="width: 160px; text-align: center;" colspan="1">
			Quantité bigrames: <b><s:property value="qtdBigrames"/></b>
		</td>
		<td style="width: 160px; text-align: center;" colspan="2">
			Quantité bigrames freq plus que 3: <b><s:property value="qtdBigramesFreqPlus3"/></b>
		</td>
	</tr>
	<tr>
		<td style="width: 160px; text-align: center;" colspan="1">
			Quantité bigrames com sw: <b><s:property value="qtdBigramesCommenceStopWord"/></b>
		</td>
		<td style="width: 160px; text-align: center;" colspan="2">
			Quantité bigrames ter sw: <b><s:property value="qtdBigramesFinisStopWord"/></b>
		</td>
	</tr>
	<tr>
		<td style="width: 160px; text-align: center;" colspan="1">
			Quantité expressions pl: <b><s:property value="qtdExprPolylexicales"/></b>
		</td>
		<td style="width: 160px; text-align: center;" colspan="2">
			Quantité expressions non-pl: <b><s:property value="qtdExprNonPolylexicales"/></b>
		</td>
	</tr>
	<tr>
		<td style="width: 160px; text-align: center;" colspan="3"><b>Résultat de la indexation</b></td>
	</tr>
	<s:iterator value="bigrams" var="big">
	<tr>
		<td style="width: 480px; text-align: center;" colspan="3">${big}</td>
	</tr>
	</s:iterator>
	<tr>
		<td style="width: 160px; text-align: center;" colspan="3"><b>Bigrames</b></td>
	</tr>
	<s:iterator value="termList">
	<tr>
		<td style="width: 160px; text-align: center;" colspan="2"><s:property value="nom" /></td>
		<td style="width: 160px; text-align: center;" colspan="1"><s:property value="freq" /></td>
	</tr>
	</s:iterator>
	<tr>
		<td style="width: 160px; text-align: center;" colspan="3"><b>Expressions polylexicales</b></td>
	</tr>
	<s:iterator value="expressionsPolylexicales">
	<tr>
		<td style="width: 160px; text-align: center;" colspan="2"><s:property value="nom" /></td>
		<td style="width: 160px; text-align: center;" colspan="1"><s:property value="freq" /></td>
	</tr>
	</s:iterator>
	<tr>
		<td style="width: 160px; text-align: center;" colspan="3"><b>Expressions non-polylexicales</b></td>
	</tr>
	<s:iterator value="expressionsNonPolylexicales">
	<tr>
		<td style="width: 160px; text-align: center;" colspan="2"><s:property value="nom" /></td>
		<td style="width: 160px; text-align: center;" colspan="1"><s:property value="freq" /></td>
	</tr>
	</s:iterator>
</table>
<!--/s:form-->
</body>
</html>