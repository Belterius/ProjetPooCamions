<%-- 
    Document   : accueil
    Created on : 16 mai 2017, 14:12:01
    Author     : Tektiv
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Renseignez vos identifiants</h1>
        <form action="/ProjetPooCamions/Controleur" method="get">
            <label for="login">Login :</label>
            <input type="text" id="login" value="admin" name="login"/>
            <br/>
            <label for="password">Mot de passe :</label>
            <input type="password" id="password" value="admin" name="password"/>            
            <br/>            
            <input type="submit" name="action" value="accueil"/>
            Note : La connection marche quel que soit l'utilisateur/Mot de passe.
        </form>
    </body>
</html>
