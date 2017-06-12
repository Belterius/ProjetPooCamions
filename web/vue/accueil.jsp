<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <c:if test="${requestScope.listSolutions == null}">
            Il est nécessaire de passer par la page /vue/login.jsp de manière à passer par le controleur !
            <%--<jsp:forward page="/CTP_Bailleul_Loic_2017/Controleur?action=accueil"/>--%> 
        </c:if>
        <c:forEach var="solution" items="${requestScope.listSolutions}">
            <li> 
                <a href="/ProjetPooWeb/Controleur?action=detailSolution&idSolution=${solution.getIdSolution()}"> <c:out value="Type de solution : ${solution.getSolutionType()}"/></a><br/>
            </li>
            </br>
        </c:forEach>
    </body>
</html>
