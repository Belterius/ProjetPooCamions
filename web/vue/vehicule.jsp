<%@page import="metier.Solution"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">

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

        <div class="row">
            <div class="col-md-8">

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
                            <th>Location X</th>
                            <th>Location Y</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="client" items="${requestScope.myVehicule}" varStatus="status">
                            <tr>
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
    </body>
</html>
