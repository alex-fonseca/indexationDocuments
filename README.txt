Alexsandro Fernandes da Fonseca

Il faut cr�er un dossier  "C:\indexationAFF\indexationDocuments" et decompresser les
fichier l�-bas.
Les fichiers TREC peuvent �tre decompress� sur "C:\indexationAFF"

Il faut avoir un serveur web install� pour que le programme fonctionne. 
J'utilise le serveur apache-tomcat-7.0.33 pour l'application.
Pour faire le download de Tomcat: http://tomcat.apache.org/download-70.cgi

Apr�s l'installation de Tomcat, allez sur: C:\Program Files (x86)\apache-tomcat-7.0.33\bin
et �xecuter le programme startup.bat pour initializer le serveur

Apr�s l'initialisation de Tomcat, ouvrir le navegateur web et �crire:
http://localhost:8080/manager/html

En bas, au-dessous de "Deploy", pour "Context Path" entrez: "/indexationDocuments"
et pour XML Configuration file URL: "C:\indexationAFF\indexationDocuments\indexationDocuments.xml"
cliquez sur "Deploy"
Le message: "OK - Deployed application at context path /indexationDocuments" doit apparaitre en haut.

Apr�s le deploy, ouvrir le navegateur web et �crire:
http://localhost:8080/indexationDocuments/


