<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String mensagemErro = (String) request.getAttribute("mensagem_erro");
    String mensagemSucesso = (String) session.getAttribute("mensagem_sucesso"); // devido ao problema do PRG, nossa mensagem de sucesso deve trafegar na sessão
    session.removeAttribute("mensagem_sucesso"); // sempre devemos remover a mensagem de sucesso depois de recuperá-la da sessão
%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="/WEB-INF/includes/header.jsp" %>
        <title>Facebug - Login</title>
        <script type="text/javascript">
            function validarCadastro() {
                var nome = document.cadastro.nome.value;
                var sobrenome = document.cadastro.sobrenome.value;
                var email = document.cadastro.email.value;
                var senha = document.cadastro.senha.value;

                if (!validaTexto(nome)) {
                    alert("O campo Nome deve ter no mínimo 1 caractere.");
                    document.cadastro.nome.focus();
                    return false;
                }

                if (!validaTexto(sobrenome)) {
                    alert("O campo Sobrenome deve ter no mínimo 1 caractere.");
                    document.cadastro.sobrenome.focus();
                    return false;
                }

                if (!validaEmail(email)) {
                    alert("Digite um e-mail válido!");
                    document.cadastro.email.focus();
                    return false;
                }

                if (!validaSenha(senha)) {
                    alert("O campo Senha deve ter no mínimo 8 caracteres.");
                    document.cadastro.senha.focus();
                    return false;
                }
                
                return true;
            }
        </script>
        <style type="text/css">
            /*
            Código CSS para ajustar o tamanho dos formulários
            */
            .control-group .control-label {
                width: 80px !important;
            }
            .form-horizontal .controls {
                margin-left: 100px !important;
            }
        </style>
    </head>
    <body>
        <%@include file="/WEB-INF/includes/topo.jsp" %>

        <!-- MENU LATERAL ESQUERDO E CONTEÚDO -->
        <div class="container-fluid">
            <div class="row-fluid">

                <!--  CONTEÚDO -->
                <div class="span12" >
                    <% if (mensagemSucesso != null) {%>
                    <div class="alert alert-success">
                        <%= mensagemSucesso%>
                    </div>
                    <%
                        }
                        if (mensagemErro != null) {
                    %>
                    <div class="alert alert-error">
                        <%= mensagemErro%>
                    </div>
                    <%}%>
                    <!-- FORMULARIO DE LOGIN -->
                    <div class="span5" >
                        <form name="login" method="POST" action="Login" class="form-horizontal" style="float: right"  >
                            <h3 style="float: right">Faça o seu Login!</h3>
                            <br class="blank-line" />
                            <input type="hidden" name="acao" value="login" />
                            <div class="control-group">
                                <label class="control-label" for="loginEmail">E-mail</label>
                                <div class="controls">
                                    <input type="text" id="loginEmail" name="email" value="" size="40" /> 
                                </div>
                            </div>
                            <div class="control-group" >
                                <label class="control-label" for="loginSenha">Senha</label>
                                <div class="controls">
                                    <input type="password" id="loginSenha" name="senha" value="" size="40" /> 
                                </div>
                            </div>
                            <div class="control-group">
                                <div class="controls">
                                    <input type="submit" value="Entrar" class="btn btn-primary" />
                                </div>
                            </div>
                        </form>
                    </div>

                    <!-- COLUNA DO MEIO -->
                    <div class="span1" style="text-align: center">
                        <h3>Ou</h3>
                    </div>

                    <!-- FORMULARIO DE CADASTRO -->
                    <div class="span5" >
                        <form name="cadastro" method="POST" action="Login" onsubmit="return validarCadastro();"
                              class="form-horizontal" style="float: left" >
                            <h3 style="float: right">Faça o seu Cadastro!</h3>
                            <br class="blank-line" />
                            <input type="hidden" name="acao" value="cadastro" />
                            <div class="control-group">
                                <label class="control-label" for="cadNome">Nome</label>
                                <div class="controls">
                                    <input type="text" id="cadNome" name="nome" value="" size="40" /> 
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label" for="cadSobrenome">Sobrenome</label>
                                <div class="controls">
                                    <input type="text" id="cadSobrenome" name="sobrenome" value="" size="40" /> 
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label" for="cadEmail">E-mail</label>
                                <div class="controls">
                                    <input type="text" id="cadEmail" name="email" value="" size="40" /> 
                                </div>
                            </div>
                            <div class="control-group">
                                <label class="control-label" for="cadSenha">Senha</label>
                                <div class="controls">
                                    <input type="password" id="cadSenha" name="senha" value="" size="40" /> 
                                </div>
                            </div>
                            <div class="control-group">
                                <div class="controls">
                                    <input type="submit" value="Cadastro" class="btn btn-primary" />
                                </div>
                            </div>
                        </form>
                    </div>

                </div>
            </div>
        </div>
        <%@include file="/WEB-INF/includes/footer.jsp" %>
    </body>
</html>
