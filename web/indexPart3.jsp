<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Recherche d'information translingue</title>
</head>
<body>
<table border="1" bordercolor="blue" cellpadding="2" cellspacing="2">
	<!--tr>
		<td style="width: 160px; text-align: center;" colspan="3">
		<s:form action="createQueryFrench">
			<s:token/>
			<s:submit value="Lire Fichier Français" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
	<tr>
		<td style="width: 160px; text-align: center;" colspan="3">
		<s:form action="createQueryPort">
			<s:token/>
			<s:submit value="Lire Fichier Portugais" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
		<tr>
		<td style="width: 160px; text-align: center;" colspan="3">
		<s:form action="translateQueriesFrench">
			<s:token/>
			<s:submit value="Traduire Requêtes Français" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
	<tr>
		<td style="width: 160px; text-align: center;" colspan="3">
		<s:form action="translateQueriesPort">
			<s:token/>
			<s:submit value="Traduire Requêtes Portugais" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
	<tr>
		<td style="width: 160px; text-align: center;" colspan="3">
		<s:form action="showQueryFrench">
			<s:token/>
			<s:submit value="Montrer Requêtes Français" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
		<tr>
		<td style="width: 160px; text-align: center;" colspan="3">
		<s:form action="showQueryFrenchTranslated">
			<s:token/>
			<s:submit value="Montrer Requêtes Français Traduites" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
	<tr>
		<td style="width: 160px; text-align: center;" colspan="3">
		<s:form action="showQueryPort">
			<s:token/>
			<s:submit value="Montrer Requêtes Portugais" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
		<tr>
		<td style="width: 160px; text-align: center;" colspan="3">
		<s:form action="showQueryPortTranslated">
			<s:token/>
			<s:submit value="Montrer Requêtes Portugais Traduites" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr-->
	<tr>
		<td style="width: 400px; text-align: center;" colspan="3">
		<s:form action="searchShortQueriesFrench">
			<s:token/>
			<s:submit value="Chercher Requêtes Courtes Français" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
	<tr>
		<td style="width: 400px; text-align: center;" colspan="3">
		<s:form action="searchShortQueriesPort">
			<s:token/>
			<s:submit value="Chercher Requêtes Courtes Portugais" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
	<!--tr>
		<td style="width: 160px; text-align: center;" colspan="3">
		<s:form action="generateTrecFrenchShort">
			<s:token/>
			<s:submit value="Creer trec_file - Req Courtes Français" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
		<tr>
		<td style="width: 160px; text-align: center;" colspan="3">
		<s:form action="generateTrecPortShort">
			<s:token/>
			<s:submit value="Creer trec_file - Req Courtes Portugais" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr-->
	<tr>
		<td style="width: 400px; text-align: center;" colspan="3">
		<s:form action="translateFrench">
			<s:token/>
			<s:textfield label="Requête en Français" name="teste" required="true" id="tst" value=""/>
			<s:submit value="Traduire vers l'anglais" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr>
	<!--tr>
		<td style="width: 400px; text-align: center;" colspan="3">
		<s:form action="teste">
			<s:token/>
			<s:textfield label="Teste" name="teste" required="true" id="tst" value=""/>
			<s:submit value="Tester" cssClass="btn btn-primary"/>
		</s:form>
		</td>
	</tr-->
</table>
</body>
</html>