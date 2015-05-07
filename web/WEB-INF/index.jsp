<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="br.grupointegrado.facebug.model.Postagem"%>
<%@page import="br.grupointegrado.facebug.model.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuario_logado");
    List<Postagem> postagens = (List<Postagem>) request.getAttribute("postagens");
    postagens = postagens != null ? postagens : new ArrayList<Postagem>();
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
                    <div class="span10" >
                        <form name="postagem" method="POST" action="Timeline" class="form-inline" >
                            <div class="span6">
                                <textarea name="texto" rows="3" style="resize: none; width: 100%" placeholder="O que você não está pensando?" ></textarea> 
                            </div>
                            <div class="span2">
                                <label class="checkbox">
                                    <input type="checkbox" name="publica"> Público
                                </label>
                                <input type="submit" value="Postar" class="btn btn-primary" />
                            </div>
                        </form>
                    </div>
                    <br class="blank-line" /><br class="blank-line" /><br class="blank-line" />
                    <% for (Postagem postagem : postagens) {%>
                    <div class="span6 postagem" >
                        <div class="span2">
                            <img src="/Facebug/imagens/perfil-padrao.jpg" class="postagem-profile-image"  />
                        </div>
                        <div class="span10 postagem-nome"  >
                            <h4><%= postagem.getUsuario().getNomeCompleto()%></h4>
                            <small class="muted">Compartilhado com <%=postagem.isPublica() ? "público" : "amigos"%> - <%=postagem.getDataToString()%></small>
                        </div>
                        <br class="blank-line" />
                        <hr class="bs-docs-separator blank-line" /> 
                        <div class="span12">
                            <%=postagem.getTexto()%>
                        </div>
                    </div>
                    <br class="blank-line" /><br class="blank-line"/>
                    <% }%>
                </div>
            </div>
        </div>
        <%@include file="/WEB-INF/includes/footer.jsp" %>
    </body>
</html>
