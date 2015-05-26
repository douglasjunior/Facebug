<%@page import="java.util.List"%>
<%@page import="br.grupointegrado.facebug.model.Postagem"%>
<%@page import="br.grupointegrado.facebug.util.HtmlUtil"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuario_logado");
    Usuario usuario = (Usuario) request.getAttribute("usuario");
    List<Postagem> postagens = (List<Postagem>) request.getAttribute("postagens");
%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="/WEB-INF/includes/header.jsp" %>
        <title>Facebug - Perfil</title>

    </head>
    <body>
        <%@include file="/WEB-INF/includes/topo.jsp" %>

        <!-- MENU LATERAL ESQUERDO E CONTEÚDO -->
        <div class="container-fluid">
            <div class="row-fluid">
                <div class="div-menu-conteudo">
                    <div class="span12">
                        <!-- IMAGEM DO PERFIL -->
                        <div class="span2">
                            <img class="nav-list-profile-image" src="/Facebug/imagens/perfil-padrao.jpg" title="<%=HtmlUtil.xss(usuario.getNomeCompleto())%>"  />
                        </div>
                        <div class="span10">
                            <h4><%= HtmlUtil.xss(usuario.getNomeCompleto())%></h4>
                            <% if (usuario.getApelido() != null) {%>
                            <h5>(<%= HtmlUtil.xss(usuario.getApelido())%>)</h5>
                            <% }%>
                        </div>
                        <br class="blank-line" />

                        <!-- LISTA QUE REPRESENTA AS ABAS -->
                        <ul class="nav nav-pills" id="myTab" style="background-color: white">
                            <li class="active"><a href="#sobre">Sobre</a></li>
                            <li><a href="#fotos">Fotos</a></li>
                            <li><a href="#amigos">Amigos</a></li>
                                <%-- se o usuário logado está acessando o próprio perfil, então exibe a aba Perfil 
                                    e não exibe o botão "Adicionar amigo" --%>
                                <% if (usuarioLogado.equals(usuario)) { %>
                            <li><a href="#perfil">Perfil</a></li>
                                <% } else { %>
                            <li class="pull-right"><a href="#" class="btn btn-success">Adicionar amigo</a></li>
                                <% }%>
                        </ul>
                        <div class="tab-content">
                            <!-- TAB SOBRE -->
                            <div class="tab-pane active" id="sobre">
                                <!-- DIV SOBRE -->
                                <div class="div-menu span2" style="word-break: break-all">
                                    <ul class="nav nav-list" >
                                        <li class="nav-header">Sobre</li>
                                        <li><strong>Nome: </strong><%=HtmlUtil.xss(usuario.getNomeCompleto())%></li>
                                        <li><strong>Apelido: </strong><%=HtmlUtil.xss(usuario.getApelido())%></li>
                                        <li><strong>Email: </strong><%=HtmlUtil.xss(usuario.getEmail())%></li>
                                        <li><strong>Nascimento: </strong><%=HtmlUtil.xss(usuario.getNascimentoString())%></li>
                                    </ul>
                                </div>
                                <!--  DIV POSTAGENS -->

                                <div class="div-conteudo span10">
                                    <!-- LISTA DE POSTAGENS -->
                                    <% for (Postagem postagem : postagens) {%>
                                    <div class="span12 postagem" >
                                        <div class="span2">
                                            <img src="/Facebug/imagens/perfil-padrao.jpg" class="postagem-profile-image"  />
                                        </div>
                                        <div class="span10 postagem-nome"  >
                                            <h4><%= HtmlUtil.xss(postagem.getUsuario().getNomeCompleto())%></h4>
                                            <small class="muted">Compartilhado com <%=postagem.isPublica() ? "público" : "amigos"%> - <%=postagem.getDataToString()%></small>
                                        </div>
                                        <br class="blank-line" />
                                        <hr class="bs-docs-separator blank-line" /> 
                                        <div class="span12">
                                            <%=HtmlUtil.xss(postagem.getTexto())%>
                                        </div>
                                    </div>
                                    <br class="blank-line" /><br class="blank-line"/>
                                    <% }%>
                                </div>

                            </div>
                            <!-- TAB FOTOS -->
                            <div class="tab-pane" id="fotos">
                                Listar fotos aqui
                            </div>
                            <!-- TAB AMIGOS -->
                            <div class="tab-pane" id="amigos">
                                Listar amigos aqui
                            </div>
                            <%-- se o usuário logado está acessando o próprio perfil,
                                então exibe o conteúdo da aba Perfil --%>
                            <% if (usuarioLogado.equals(usuario)) {%>
                            <!-- TAB PERFIL -->
                            <div class="tab-pane" id="perfil">
                                Aqui o usuário poderá editar o perfil
                            </div>
                            <% }%>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <%@include file="/WEB-INF/includes/footer.jsp" %>

        <script type="text/javascript">
            // código que seleciona a aba de acordo com a âncora contida na URL
            $(function () {
                var tabName = window.location.hash;
                $('#myTab a[href="' + tabName + '"]').tab('show');
            });
        </script>
        <script type="text/javascript">
            // código que seleciona a aba quando clicada em cada uma delas
            $('#myTab a').click(function (e) {
                e.preventDefault();
                $(this).tab('show');
                window.location = this;
            });
        </script>
    </body>
</html>
