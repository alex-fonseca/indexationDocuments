<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Recherche d'information</title>
</head>
<body>
<table border="1" bordercolor="blue" cellpadding="2" cellspacing="2">
	<tr>
		<td style="width: 160px; text-align: center;" colspan="3">
		<s:form action="createQueryEnglish">
			<s:token/>
			<s:submit value="Lire Fichier Anglais" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
	<tr>
		<td style="width: 160px; text-align: center;" colspan="3">
		<s:form action="showQueryEnglish">
			<s:token/>
			<s:submit value="Montrer Requêtes Anglais" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
	<tr>
		<td style="width: 160px; text-align: center;" colspan="3">
		<s:form action="searchShortQueriesEnglish">
			<s:token/>
			<s:submit value="Chercher Requêtes Courtes Anglais" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
	<tr>
		<td style="width: 160px; text-align: center;" colspan="3">
		<s:form action="searchLongQueriesEnglish">
			<s:token/>
			<s:submit value="Chercher Requêtes Longues Anglais" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
	<tr>
		<td style="width: 160px; text-align: center;" colspan="3">
		<s:form action="generateTrecEnglishShort">
			<s:token/>
			<s:submit value="Creer trec_file - Req Courtes Anglais" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
	<tr>
		<td style="width: 160px; text-align: center;" colspan="3">
		<s:form action="generateTrecEnglishLong">
			<s:token/>
			<s:submit value="Creer trec_file - Req Longues Anglais" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
	<tr>
		<td style="width: 160px; text-align: center;" colspan="3">
		<s:form action="userQuery">
			<s:token/>
			<s:textfield label="Requête" name="teste" required="true" id="tst" value=""/>
			<s:submit value="Chercher" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
</table>
</body>
</html>