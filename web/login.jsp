<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <%@include file="/WEB-INF/includes/header.jsp" %>
        <title>Facebug - Login</title>
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

                    <h1>Faça o seu Login!</h1>
                    <br />
                    <form name="login" method="POST" action="LoginServlet" >
                        <input type="hidden" name="acao" value="login" />
                        E-mail: <input type="text" name="email" value="" size="40" /> <br />
                        Senha: <input type="password" name="senha" value="" size="40" /> <br />
                        <input type="submit" value="Entrar" />
                    </form>
                    <br />
                    <h1>Faça o seu Cadastro!</h1>
                    <br />
                    <form name="cadastro" method="POST" action="LoginServlet" >
                        <input type="hidden" name="acao" value="cadastro" />
                        Nome: <input type="text" name="nome" value="" size="40" /> <br />
                        Sobrenome: <input type="text" name="sobrenome" value="" size="40" /> <br />
                        E-mail: <input type="text" name="email" value="" size="40" /> <br />
                        Senha: <input type="password" name="senha" value="" size="40" /> <br />
                        <input type="submit" value="Cadastro" />
                    </form>

                </div>
            </div>
        </div>
        <%@include file="/WEB-INF/includes/footer.jsp" %>
    </body>
</html>
