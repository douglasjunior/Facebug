package br.grupointegrado.facebug.controller;

import br.grupointegrado.facebug.model.Usuario;
import br.grupointegrado.facebug.model.UsuarioDAO;
import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acaoParam = req.getParameter("acao");
        if ("login".equals(acaoParam)) {
            efetuarLogin(req, resp);
        } else if ("cadastro".equals(acaoParam)) {
            efetuarCadastro(req, resp);
        }
    }

    private void efetuarLogin(HttpServletRequest req, HttpServletResponse resp) {

    }

    private void efetuarCadastro(HttpServletRequest req, HttpServletResponse resp) {
        Usuario usuario = UsuarioDAO.getUsuarioParameters(req);
        Connection conn = (Connection) req.getAttribute("conexao");
        new UsuarioDAO(conn).inserir(usuario);
    }
}
