<%@page import="br.grupointegrado.facebug.util.HtmlUtil"%>
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
    String mensagemSucesso = (String) session.getAttribute("mensagem_sucesso");
    session.removeAttribute("mensagem_sucesso"); // sempre devemos remover a mensagem de sucesso depois de recuperá-la da sessão

    Postagem postagemEditar = (Postagem) request.getAttribute("postagem");
    postagemEditar = postagemEditar != null ? postagemEditar : new Postagem(); // evita que uma postagem NULL chegue ao formulário
%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="/WEB-INF/includes/header.jsp" %>
        <title>Facebug - Timeline</title>
        <script type="text/javascript">
            // verifica se o usuário tem certeza da exclusão
            function excluirPostagem(codigo) {
                var result = confirm("Deseja excluir a postagem " + codigo + " ?");
                if (result) {
                    window.location = "Timeline?acao=excluir&id=" + codigo;
                }
            }
             function validarPostagem() {
                var post = document.postagem.texto.value;
                if (!validaTexto(post)) {
                    alert("A postagem deve ter no mínimo 1 caractere para ser publicada!");
                    document.postagem.texto.focus();
                    return false;
                }
                return true;
            }
        </script>
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
                        <h3>Olá <%= HtmlUtil.xss(usuarioLogado.getNome())%>, bem vindo ao Facebug!</h3><br />
                        <% if (mensagemErro != null) {%>
                        <div class="alert alert-error">
                            <%= mensagemErro%>
                        </div>
                        <%}%>
                        <% if (mensagemSucesso != null) {%>
                        <div class="alert alert-success">
                            <%= mensagemSucesso%>
                        </div>
                        <%}%>
                        <div class="span12 postagem-form" >
                            <form name="postagem" method="POST" action="Timeline" class="form-inline" onsubmit="return validarPostagem();" >
                                <% if (postagemEditar.getId() > 0) {
                                        out.write("Editando postagem " + postagemEditar.getId());
                                    }
                                %> 
                                <input type="hidden" name="id" value="<%= postagemEditar.getId()%>" />
                                <textarea name="texto" rows="3" style="resize: none; width: 483px" placeholder="O que você não está pensando?" ><%=HtmlUtil.xss(postagemEditar.getTexto())%></textarea> 
                                <div style="float: right">
                                    <label class="checkbox">
                                        <input type="checkbox" name="publica" <%= postagemEditar.isPublica() ? "checked" : ""%> /> Público
                                    </label>
                                    <% if (postagemEditar.getId() > 0) { %>
                                    <a href="/Facebug/Timeline" class="btn" >Cancelar</a>
                                    <% } %> 
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
                                <div class="btn-group" style="float: right">
                                    <button class="btn btn-mini dropdown-toggle" data-toggle="dropdown"><span class="caret"></span></button>
                                    <ul class="dropdown-menu">
                                        <li><a href="Timeline?acao=editar&id=<%=postagem.getId()%>">Editar</a></li>
                                        <li><a onclick="excluirPostagem(<%=postagem.getId()%>)">Excluir</a></li>
                                    </ul>
                                </div>
                                <h4><%= HtmlUtil.xss(postagem.getUsuario().getNomeCompleto())%></h4>
                                <small class="muted">Compartilhado com <%=postagem.isPublica() ? "público" : "amigos"%> - <%=postagem.getDataToString()%></small>
                            </div>
                            <br class="blank-line" />
                            <hr class="bs-docs-separator blank-line" /> 
                            <div class="span12  postagem-box">
                                <%=HtmlUtil.quebraLinha(HtmlUtil.xss(postagem.getTexto()))%>
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
