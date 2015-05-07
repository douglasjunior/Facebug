<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%-- 
Página para ser utilizada como Exemplo de Template na criação de novas páginas.
--%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="/WEB-INF/includes/header.jsp" %>
        <title>Exemplo template</title>
    </head>
    <body>
        <%@include file="/WEB-INF/includes/topo.jsp" %>

        <!-- MENU LATERAL ESQUERDO E CONTEÚDO -->
        <div class="container-fluid">
            <div class="row-fluid">
                <div class="div-menu-conteudo">
                    <!-- MENU LATERAL ESQUERDO -->
                    <div class="div-menu span2">
                        <%@include file="/WEB-INF/includes/menu.jsp" %>
                    </div>
                    <!--  CONTEÚDO -->
                    <div class="div-conteudo span10">
                        <h3>Olá usuário, bem vindo ao Facebug!</h3><br />

                    </div>
                </div>
            </div>
        </div>
        <%@include file="/WEB-INF/includes/footer.jsp" %>
    </body>
</html>
