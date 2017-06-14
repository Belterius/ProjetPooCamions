<%@page import="metier.Solution"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">

        <title>Tournées</title>

        <!-- Bootstrap -->
        <link href="css/bootstrap.css" rel="stylesheet">
        <!-- Custom CSS -->
        <link href="css/custom.css" rel="stylesheet">
    </head>
    <body>
        <c:if test="${requestScope.myVehicule == null}">
            Il est nécessaire de passer par la page /vue/login.jsp de manière à passer par le controleur !
            <%--<jsp:forward page="/CTP_Bailleul_Loic_2017/Controleur?action=accueil"/>--%> 
        </c:if>

        <div class="container">


            <div class="row">
                <div class="col-md-8" id="container">
                    <div id="map">
                        <p>Veuillez patienter pendant le chargement de la carte...</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="row">
                        <h1>Liste des étapes</h1>
                    </div>
                    <table class="table">
                        <thead>
                            <tr>
                                <th>#</th>
                                <th>Lieu n°</th>
                                <th>Quantité 1</th>
                                <th>Quantité 2</th>
                                <th class="x">Location X</th>
                                <th clas="y">Location Y</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="client" items="${requestScope.myVehicule}" varStatus="status">
                                <tr id="${client.getLocation_id()}">
                                    <td>${status.count}</td>
                                    <td><c:out value="${client.getLocation_id()}"/></td>
                                    <td><c:out value="${client.getSwap_body_1_quantity()}"/></td>
                                    <td><c:out value="${client.getSwap_body_2_quantity()}"/></td>
                                    <td><c:out value="${client.getLocation_x()}"/></td>                
                                    <td><c:out value="${client.getLocation_y()}"/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.js"></script>

    <script src="http://maps.google.com/maps/api/js?key=AIzaSyCwGH4tJhoPRlxgp-7-5kffWc5b_uktoEU&language=fr"></script>
    <script src="js/functions.js"></script>
</html>
