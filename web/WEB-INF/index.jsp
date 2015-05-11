<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="br.grupointegrado.facebug.model.Postagem"%>
<%@page import="br.grupointegrado.facebug.model.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuario_logado");
    List<Postagem> postagens = (List<Postagem>) request.getAttribute("postagens");
    postagens = postagens != null ? postagens : new ArrayList<Postagem>();
    
    String mensagemErro = (String) request.getAttribute("mensagem_erro");
    String mensagemInfo = (String) session.getAttribute("mensagem_info");
    
    Postagem post = (Postagem) session.getAttribute("postagem");
    if (null == post) {
        post = new Postagem();
    }
    
    session.removeAttribute("mensagem_info");
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
                <div class="div-menu-conteudo">
                    <!-- MENU LATERAL ESQUERDO -->
                    <div class="div-menu span2">
                        <%@include file="/WEB-INF/includes/menu.jsp" %>
                    </div>
                    <!--  CONTEÚDO -->
                    <div class="div-conteudo span10">
                        <h3>Olá <%= usuarioLogado.getNome()%>, bem vindo ao Facebug!</h3><br />
                        <% if (mensagemErro != null) { %>
                        <div class="alert alert-error">
                            <%= mensagemErro%>
                        </div>
                        <%} else if (mensagemInfo != null) { %>
                        <div class="alert alert-warning">
                            <%= mensagemInfo%>
                        </div>
                        <% } %>
                        <div class="span12" >
                            <form name="postagem" method="POST" action="Timeline" class="form-inline" >
                                <input type="hidden" name="acao" value="<%= (String) request.getParameter("acao") %>" />
                                <textarea name="texto" rows="3" style="resize: none; width: 483px" placeholder="O que você não está pensando?" ><%=  post.getTexto() %></textarea> 
                                <div style="float: right">
                                    <label class="checkbox">
                                        <input type="checkbox" <%= post.isPublica() ? "checked" : "" %> name="publica"> Público
                                    </label>
                                    <input type="submit" value="Postar" class="btn btn-primary" />
                                </div>
                            </form>
                        </div>
                        <br class="blank-line" /><br class="blank-line" /><br class="blank-line" />
                        <% for (Postagem postagem : postagens) {%>
                        <div class="span12 postagem" >
                            <div class="span2">
                                <img src="/Facebug/imagens/perfil-padrao.jpg" class="postagem-profile-image"  />
                            </div>
                            <div class="span10 postagem-nome"  >
                                <h4><%= postagem.getUsuario().getNomeCompleto()%></h4>
                                <small class="muted">Compartilhado com <%= postagem.getStringCompartilhado() %> - <%=postagem.getDataToString()%></small>
                                    
                                <div class="dropdown" style="float: right;">
                                    <button class="dropdown-toggle btn btn-mini" id="dLabel" role="button" data-toggle="dropdown">
                                      Ações
                                      <b class="caret"></b>
                                    </button>
                                    <ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">
                                      <li><a href="/Facebug/Timeline?acao=editar&codigo=<%= postagem.getId() %>">Editar</a></li>
                                      <li><a onclick="return confirm('Excluir', 'Deseja realmente excluir esta postagem?');" href="/Facebug/Timeline?acao=excluir&codigo=<%= postagem.getId() %>">Excluir</a></li>
                                    </ul>
                                </div>
                                    
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
        </div>
        <%@include file="/WEB-INF/includes/footer.jsp" %>
    </body>
</html>
