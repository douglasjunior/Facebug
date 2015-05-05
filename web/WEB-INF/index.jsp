<%@page import="br.grupointegrado.facebug.model.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuario_logado");
%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="/WEB-INF/includes/header.jsp" %>
        <title>Facebug - Timeline</title>
    </head>
    <body>
        <%@include file="/WEB-INF/includes/topo.jsp" %>

        <!-- MENU LATERAL ESQUERDO E CONTEÚDO -->
        <div class="container-fluid">
            <div class="row-fluid">
                <!-- MENU LATERAL ESQUERDO -->
                <div class="span2">
                    <%@include file="/WEB-INF/includes/menu.jsp" %>
                </div>
                <!--  CONTEÚDO -->
                <div class="span10">

                    <h3>Olá <%= usuarioLogado.getNome()%>, bem vindo ao Facebug!</h3><br />
                    <form name="postagem" method="POST" action="Timeline" >
                        <textarea name="texto" rows="3" style="resize: none; width: 400px" placeholder="O que você não está pensando?" ></textarea> 
                        <input type="submit" value="Postar" class="btn btn-primary" />
                    </form>
                </div>
            </div>
        </div>
        <%@include file="/WEB-INF/includes/footer.jsp" %>
    </body>
</html>
