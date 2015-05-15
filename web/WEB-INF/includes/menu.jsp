<%@page import="br.grupointegrado.facebug.model.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Usuario usuarioLogadoMenu = (Usuario) session.getAttribute("usuario_logado");
%>
<ul class="nav nav-list">
    <li><img class="nav-list-profile-image" src="/Facebug/imagens/perfil-padrao.jpg" title="<%=usuarioLogadoMenu.getNomeCompleto()%>"  /><br />
        <%= usuarioLogadoMenu.getNomeCompleto()%></li>
    <br />
    <li><a href="/Facebug/Timeline">Timeline</a></li>
    <li><a href="#">Amigos</a></li>
    <li><a href="#">Fotos</a></li>
    <li class="nav-header">Configurações</li>
    <li><a href="/Facebug/Perfil">Perfil</a></li>
    <li><a href="#">Termo de uso</a></li>
    <li><a href="#">Ajuda</a></li>
</ul>