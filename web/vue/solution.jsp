<%@page import="metier.Solution"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>Solution</title>

        <!-- Bootstrap -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <!-- Custom CSS -->
        <link href="css/custom.css" rel="stylesheet">
    </head>
    <body>
        <div class="row">
            <h1>Liste des véhicules</h1>
        </div>
        <c:if test="${requestScope.myVehicules == null}">
            Il est nécessaire de passer par la page /vue/login.jsp de manière à passer par le controleur !
            <%--<jsp:forward page="/CTP_Bailleul_Loic_2017/Controleur?action=accueil"/>--%> 
        </c:if>

        <div class="row">
            <div class="col-md-2">
                <table class="table">
                    <thead>
                        <tr>
                            <th>#</th>
                            <th>Véhicule n°</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="vehicule" items="${requestScope.myVehicules}" varStatus="status">
                            <c:if test="${status.count % 15 == 0}">
                                </tbody>
                                </table>
                                </div>
                                <div class="col-md-2">
                                <table class="table">
                                <thead>
                                <tr>
                                <th>#</th>
                                <th>Véhicule n°</th>
                                </tr>
                                </thead>
                                <tbody>
                            </c:if>
                            <tr>
                                <td>
                                    ${status.count}
                                </td>
                                <td>
                                    <a href="/ProjetPooCamions/Controleur?action=detailVehicule&routeNumber=${vehicule}"> <c:out value="${vehicule}"/></a><br/>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="js/bootstrap.js"></script> 
    </body>
</html>
