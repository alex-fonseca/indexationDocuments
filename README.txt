Alexsandro Fernandes da Fonseca

Il faut créer un dossier  "C:\indexationAFF\indexationDocuments" et decompresser les
fichier là-bas.
Les fichiers TREC peuvent être decompressé sur "C:\indexationAFF"

Il faut avoir un serveur web installé pour que le programme fonctionne. 
J'utilise le serveur apache-tomcat-7.0.33 pour l'application.
Pour faire le download de Tomcat: http://tomcat.apache.org/download-70.cgi

Après l'installation de Tomcat, allez sur: C:\Program Files (x86)\apache-tomcat-7.0.33\bin
et éxecuter le programme startup.bat pour initializer le serveur

Après l'initialisation de Tomcat, ouvrir le navegateur web et écrire:
http://localhost:8080/manager/html

En bas, au-dessous de "Deploy", pour "Context Path" entrez: "/indexationDocuments"
et pour XML Configuration file URL: "C:\indexationAFF\indexationDocuments\indexationDocuments.xml"
cliquez sur "Deploy"
Le message: "OK - Deployed application at context path /indexationDocuments" doit apparaitre en haut.

Après le deploy, ouvrir le navegateur web et écrire:
http://localhost:8080/indexationDocuments/


