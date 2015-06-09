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
        <title>Facebug - Erro 404</title>
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
        <p><strong>A PÁGINA QUE VOCÊ TENTOU ACESSAR NÃO EXISTE.</strong></p>
        <p><strong>Clique <a href="/Facebug/">aqui</a> para retornar a página inicial.</strong></p>
        <%@include file="/WEB-INF/includes/footer.jsp" %>
    </body>
</html> 