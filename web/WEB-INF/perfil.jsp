<%@page import="br.grupointegrado.facebug.model.Amigo"%>
<%@page import="br.grupointegrado.facebug.model.Foto"%>
<%@page import="java.util.List"%>
<%@page import="br.grupointegrado.facebug.model.Postagem"%>
<%@page import="br.grupointegrado.facebug.util.HtmlUtil"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Usuario usuarioLogado = (Usuario) session.getAttribute("usuario_logado");
    Usuario usuario = (Usuario) request.getAttribute("usuario");
    Amigo amigo = (Amigo) request.getAttribute("amigo");

    List<Postagem> postagens = (List<Postagem>) request.getAttribute("postagens");
    List<Amigo> amigos = (List<Amigo>) request.getAttribute("amigos");
    List<Foto> fotos = (List<Foto>) request.getAttribute("fotos");

    String mensagemErro = (String) request.getAttribute("mensagem_erro");
    String mensagemSucesso = (String) session.getAttribute("mensagem_sucesso");
    session.removeAttribute("mensagem_sucesso"); // sempre devemos remover a mensagem de sucesso depois de recuperá-la da sessão
%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="/WEB-INF/includes/header.jsp" %>
        <title>Facebug - Perfil</title>
        <style type="text/css">
            .product {
                width: 150px;
                height: 150px;
                display: inline-block;
            }
        </style>

        <script type="text/javascript">
            function validarPerfil() {
                var nome = document.perfil.nome.value;
                var sobrenome = document.perfil.sobrenome.value;
                var nascimento = document.perfil.nascimento.value;
                var email = document.perfil.email.value;
                var senha = document.perfil.senha.value;

                if (!validaTexto(nome)) {
                    alert(ano);
                    document.perfil.nome.focus();
                    return false;
                }

                if (!validaTexto(sobrenome)) {
                    alert("O campo Nome deve ter no mínimo 1 caractere.");
                    document.perfil.sobrenome.focus();
                    return false;
                }

                var anoAtual = new Date().getFullYear();
                if (!validaData(nascimento, 1900, anoAtual)) {
                    alert("A data de nascimento deve estar entre 1900 e " + anoAtual + ".");
                    document.perfil.nascimento.focus();
                    return false;
                }

                if (!validaEmail(email)) {
                    alert("Digite um e-mail válido!");
                    document.perfil.email.focus();
                    return false;
                }

                if ((document.perfil.habilitaSenha.checked) && (!validaSenha(senha))) {
                    alert("Senha necessita de 8 digititos no minino!");
                    document.perfil.senha.focus();
                    return false;
                }
                return true;
            }
            function habilitarDesabilitaSenha() {
                // método que habilita ou desabilita o campo de senha
                if (document.perfil.habilitaSenha.checked)
                    document.perfil.senha.removeAttribute('readonly');
                else
                    document.perfil.senha.setAttribute("readonly", "readonly");
            }
        </script>
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
                            <img class="nav-list-profile-image" src="/Facebug/Imagem?origem=usuario&id=<%=usuario.getId()%>" title="<%=HtmlUtil.xss(usuario.getNomeCompleto())%>"  />
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
                                <%-- se o usuário logado está acessando o próprio perfil, então exibe a aba Perfil --%>
                                <% if (usuarioLogado.equals(usuario)) { %>
                            <li><a href="#perfil">Perfil</a></li>
                                <% } %>
                        </ul>
                        <%
                            // Verifica se o usuário logado não está acessando o p´roprio perfil,
                            // então exibe o botão "Adicionar amigo"
                            if (!usuarioLogado.equals(usuario)) {
                                // Verifica se existe a amizade ou não, então exibe Adicionar ou Remover
                                if (amigo == null) {
                        %>
                        <a href="/Facebug/Perfil?acao=adicionar&id=<%=usuario.getId()%>" class="btn btn-success" >Adicionar amigo</a>
                        <% } else {%>
                        <a href="/Facebug/Perfil?acao=remover&id=<%=usuario.getId()%>" class="btn btn-danger" >Remover amigo</a>
                        <%
                                }
                            }
                        %>
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
                                            <img src="/Facebug/Imagem?origem=usuario&id=<%=postagem.getUsuario().getId()%>" class="postagem-profile-image"  />
                                        </div>
                                        <div class="span10 postagem-nome"  >
                                            <h4><%=HtmlUtil.xss(postagem.getUsuario().getNomeCompleto())%></h4>
                                            <small class="muted">Compartilhado com <%=postagem.isPublica() ? "público" : "amigos"%> - <%=postagem.getDataToString()%></small>
                                        </div>
                                        <br class="blank-line" />
                                        <hr class="bs-docs-separator blank-line" /> 
                                        <div class="span12 postagem-box">
                                            <%=HtmlUtil.quebraLinha(HtmlUtil.xss(postagem.getTexto()))%>
                                        </div>
                                    </div>
                                    <br class="blank-line" /><br class="blank-line"/>
                                    <% }%>
                                </div>

                            </div>
                            <!-- TAB FOTOS -->
                            <div class="tab-pane" id="fotos">
                                <form name="formAlbum" method="POST" action="Perfil" enctype="multipart/form-data" accept-charset="utf-8">
                                    <input type="hidden" name="acao" value="salvarAlbum"/>
                                    <input type="file" name="foto" value="" size="60" />
                                    <input type="submit" value="Enviar" class="btn btn-primary" />
                                </form>
                                <h3>Album de fotos.</h3>
                                <div class="container-fluid">
                                    <div class="row-fluid">
                                        <div class="span12 products">
                                            <% for (Foto foto : fotos) {%>
                                            <div class="product">
                                                <a href="/Facebug/Imagem?origem=album&id=<%=foto.getId()%>" >
                                                    <img src="/Facebug/Imagem?origem=album&id=<%=foto.getId()%>" class="nav-list-profile-image">
                                                </a>
                                            </div>
                                            <% } %>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- TAB AMIGOS -->
                            <div class="tab-pane" id="amigos">
                                <h3>Lista de amigos.</h3>
                                <div class="container-fluid">
                                    <div class="row-fluid">
                                        <div class="span12 products">
                                            <% for (Amigo a : amigos) {
                                                    // Verifica qual dos dois objetos é o amigo, pois o outro será o p´roprio usuário
                                                    Usuario usuarioAmigo = a.getUsuario().equals(usuario) ? a.getAmigo() : a.getUsuario();
                                            %>
                                            <div class="product">
                                                <a href="/Facebug/Perfil?id=<%=usuarioAmigo.getId()%>" >
                                                    <img src="/Facebug/Imagem?origem=usuario&id=<%=usuarioAmigo.getId()%>" class="nav-list-profile-image">
                                                    <br class="blank-line" />
                                                    <%=usuarioAmigo.getNomeCompleto()%>
                                                </a>
                                            </div>
                                            <% } %>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <%-- se o usuário logado está acessando o próprio perfil,
                                então exibe o conteúdo da aba Perfil --%>
                            <% if (usuarioLogado.equals(usuario)) {%>
                            <!-- TAB PERFIL -->
                            <div class="tab-pane" id="perfil">
                                <!-- Devido ao fato deste formulário fazer upload de Imagens, é preciso adicionar os atributos < enctype="multipart/form-data" accept-charset="utf-8" > -->
                                <form name="perfil" method="POST" action="Perfil" onsubmit="return validarPerfil();" enctype="multipart/form-data" accept-charset="utf-8" 
                                      class="form-horizontal" autocomplete="off" >
                                    <h3>Edição do perfil.</h3>
                                    <br class="blank-line" />
                                    <input type="hidden" name="acao" value="editar" />
                                    <input type="hidden" name="id" value="<%=usuario.getId()%>" />
                                    <div class="control-group">
                                        <label class="control-label" for="perfilFoto">Foto</label>
                                        <div class="controls">
                                            <input type="file" id="perfilFoto" name="foto" value=""  onclick="comprova_extensao(this.form, this.form.arquivoupload.value)"  size="60" /> 
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label" for="perfilNome">Nome</label>
                                        <div class="controls">
                                            <input type="text" id="perfilNome" name="nome" value="<%=HtmlUtil.xss(usuario.getNome())%>" size="60" /> 
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label" for="perfilSobrenome">Sobrenome</label>
                                        <div class="controls">
                                            <input type="text" id="perfilSobrenome" name="sobrenome" value="<%=HtmlUtil.xss(usuario.getSobrenome())%>" size="60" /> 
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label" for="perfilApelido">Apelido</label>
                                        <div class="controls">
                                            <input type="text" id="perfilApelido" name="apelido" value="<%=HtmlUtil.xss(usuario.getApelido())%>" size="60" /> 
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label" for="perfilNascimento">Nascimento</label>
                                        <div id="datepickerNascimento" class="controls input-append date" style="margin-left: 20px">
                                            <input type="text" id="perfilNascimento" name="nascimento" value="<%=HtmlUtil.xss(usuario.getNascimentoString())%>" size="60">
                                            <span class="add-on"><i class="icon-th"></i></span>
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label" for="perfilEmail">E-mail</label>
                                        <div class="controls">
                                            <input type="text" id="perfilEmail" name="email" value="<%=HtmlUtil.xss(usuario.getEmail())%>" size="60" readonly="readonly" disabled="disabled" /> 
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <label class="control-label" for="perfilSenha">Senha</label>
                                        <div class="controls">
                                            <input type="password" id="perfilSenha" name="senha" readonly autocomplete="off" size="60" /> 
                                            <input type="checkbox" name="habilitaSenha" onchange="habilitarDesabilitaSenha();" />
                                        </div>
                                    </div>
                                    <div class="control-group">
                                        <div class="controls">
                                            <input type="submit" value="Editar" class="btn btn-primary" />
                                        </div>
                                    </div>
                                </form>
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
        <script type="text/javascript">
            // código para configurar o Datepicker da data de nascimento
            $(document).ready(function () {
                $('#datepickerNascimento').datepicker({
                    format: "dd/mm/yyyy",
                    language: "pt-BR",
                    autoclose: true
                });
            });
        </script>

        <script type="text/javascript">
            // Ecte
            $(function(){ comprova_extensao(formulario, arquivo) {
                extensoes_permitidas = new Array(".gif", ".jpg", ".jpeg" , ".PNG");
                meuerro = "";
                if (!arquivo) {
                  
                    meuerro = "Não foi selecionado nenhum arquivo";
                } else {
                  
                    extensao = (arquivo.substring(arquivo.lastIndexOf("."))).toLowerCase();
                    
                    permitida = false;
                    for (var i = 0; i < extensoes_permitidas.length; i++) {
                        if (extensoes_permitidas[i] == extensao) {
                            permitida = true;
                            break;
                        }
                    }
                    if (!permitida) {
                        meuerro = "Comprova a extensão dos arquivos a subir. \nSó se podem subir arquivos com extensões: " + extensoes_permitidas.join();
                    } else {
                     
                        alert("Tudo correto. Vou submeter o formulário.");
                        formulario.submit();
                        return 1;
                    }
                }
              
                alert(meuerro);
                return 0;
            });
        </script>

    </body>
</html>
