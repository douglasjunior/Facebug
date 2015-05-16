<%@page import="br.grupointegrado.facebug.util.HtmlUtil"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuario_logado");
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
                            <img class="nav-list-profile-image" src="/Facebug/imagens/perfil-padrao.jpg" title="<%=HtmlUtil.xss(usuarioLogado.getNomeCompleto())%>"  />
                        </div>
                        <div class="span10">
                            <h4><%= HtmlUtil.xss(usuarioLogado.getNomeCompleto())%></h4>
                            <% if (usuarioLogado.getApelido() != null) {%>
                            <h5>(<%= HtmlUtil.xss(usuarioLogado.getApelido())%>)</h5>
                            <% }%>
                        </div>
                        <br class="blank-line" />
                        <!-- LISTA QUE REPRESENTA AS ABAS -->
                        <ul class="nav nav-pills" id="myTab" style="background-color: white">
                            <li class="active"><a href="#sobre">Sobre</a></li>
                            <li><a href="#fotos">Fotos</a></li>
                            <li><a href="#amigos">Amigos</a></li>
                            <li><a href="#perfil">Perfil</a></li>
                        </ul>
                        <div class="tab-content">
                            <!-- TAB SOBRE -->
                            <div class="tab-pane active" id="sobre">
                                <!-- DIV SOBRE -->
                                <div class="div-menu span2">
                                    <ul class="nav nav-list" >
                                        <li class="nav-header">Sobre</li>
                                        <li><strong>Nome: </strong>nome aqui</li>
                                        <li><strong>Apelido: </strong>apelido aqui</li>
                                        <li><strong>Email: </strong>email aqui</li>
                                        <li><strong>Nascimento: </strong>nascimento aqui</li>
                                    </ul>
                                </div>
                                <!--  DIV POSTAGENS -->
                                <div class="div-conteudo span10">
                                    <!-- LISTA DE POSTAGENS -->
                                    <div class="span12 postagem" >
                                        <div class="span2">
                                            <img src="/Facebug/imagens/perfil-padrao.jpg" class="postagem-profile-image"  />
                                        </div>
                                        <div class="span10 postagem-nome"  >
                                            <h4>Usuário</h4>
                                            <small class="muted">Compartilhado com amigos - 00/00/0000 00:00</small>
                                        </div>
                                        <br class="blank-line" />
                                        <hr class="bs-docs-separator blank-line" /> 
                                        <div class="span12">
                                            Texto da postagem aqui.
                                        </div>
                                    </div>
                                    <br class="blank-line" /><br class="blank-line"/>
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
                            <!-- TAB PERFIL -->
                            <div class="tab-pane" id="perfil">
                                Aqui o usuário poderá editar o perfil
                            </div>
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
