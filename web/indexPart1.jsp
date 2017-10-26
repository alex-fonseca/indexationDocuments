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
		<td style="width: 160px; text-align: center;" colspan="2">
		<b>Creer un Index:</b>
		</td>
		<td style="width: 160px; text-align: center;" colspan="1">
			<s:form action="create">
				<s:token/>
				<s:submit value="Creer" cssClass="btn btn-primary"/>
			</s:form>
		</td>
	</tr>
	<tr>
		<td style="width: 160px; text-align: center;" colspan="2">
		<b>Lire un Index:</b>
		</td>
		<td style="width: 160px; text-align: center;" colspan="1">
			<s:form action="read">
				<s:token/>
				<s:submit value="Lire" cssClass="btn btn-primary"/>
			</s:form>
		</td>
	</tr>
	<tr>
		<td style="width: 300px; text-align: center;" colspan="3">
		<s:form action="docFreq">
			<s:token/>
			<s:textfield label="Terme" name="termName" required="true" id="tName" value=""/>
			<s:submit value="Doc Frequence" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
	<!--tr>
		<td style="width: 160px; text-align: center;" colspan="3">
		<s:form action="searchTerm">
			<s:token/>
			<s:textfield label="Terme" name="termName" required="true" id="tName" value=""/>
			<s:submit value="Recherche Terme" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr-->
	<tr>
		<td style="width: 300px; text-align: center;" colspan="3">
		<s:form action="termFreq">
			<s:token/>
			<s:textfield label="Nro Document" name="docNO" required="true" id="tfreq" value=""/>
			<s:submit value="Terme Frequence" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
</table>
</body>
</html>