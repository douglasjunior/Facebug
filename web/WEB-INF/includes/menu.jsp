<%@page import="br.grupointegrado.facebug.util.HtmlUtil"%>
<%@page import="br.grupointegrado.facebug.model.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Usuario usuarioLogadoMenu = (Usuario) session.getAttribute("usuario_logado");
%>
<ul class="nav nav-list">
    <li><img class="nav-list-profile-image" src="/Facebug/imagens/perfil-padrao.jpg" title="<%=HtmlUtil.xss(usuarioLogadoMenu.getNomeCompleto())%>"  /><br />
        <%= HtmlUtil.xss(usuarioLogadoMenu.getNomeCompleto())%></li>
    <br />
    <li class="nav-header">Links</li>
    <li><a href="/Facebug/Timeline">Timeline</a></li>
    <li><a href="/Facebug/Perfil#amigos">Amigos</a></li>
    <li><a href="/Facebug/Perfil#fotos">Fotos</a></li>
    <li class="nav-header">Configurações</li>
    <li><a href="/Facebug/Perfil">Perfil</a></li>
    <li><a href="#">Termo de uso</a></li>
    <li><a href="#">Ajuda</a></li>
</ul>