<%@page import="metier.Solution"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <c:if test="${requestScope.myVehicule == null}">
            Il est nécessaire de passer par la page /vue/login.jsp de manière à passer par le controleur !
            <%--<jsp:forward page="/CTP_Bailleul_Loic_2017/Controleur?action=accueil"/>--%> 
        </c:if>
        <c:forEach var="client" items="${requestScope.myVehicule}">
            <li> 
                <c:out value="${client.toString()}"/><br/>                
                <c:out value="Position X : ${client.getLocation_x()}"/><br/>                
                <c:out value="Position Y : ${client.getLocation_y()}"/><br/>

            </li>
            </br>
        </c:forEach>
    </body>
</html>
