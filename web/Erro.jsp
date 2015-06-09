<%-- 

    Document   : Erro
    Created on : 27/05/2015, 20:29:22
    Author     : Bruno Martins dos Santos    
    Página de erro personalisada FACEBUG.  

--%>

<%@page contentType="text/html" pageEncoding="UTF-8" isErrorPage="true"%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="/WEB-INF/includes/header.jsp" %>
        <title>Facebug - Erro</title>
        <style type="text/css">
            .body {
                background-color:#E5E5E5;
                color: black;
                text-align: center;
            }
        </style>
    </head>
    <body class="body">
        <%@include file="/WEB-INF/includes/topo.jsp" %>
        <h1>OPS!</h1>
        <p><strong>FACEBUG PAROU DE RESPONDER</strong></p>
        <p><strong>Clique <a href="Login.jsp">aqui</a> para realizar o login novamente</strong></p>
        <%@include file="/WEB-INF/includes/footer.jsp" %>
    </body>
</html> 