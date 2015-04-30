package br.grupointegrado.facebug.controller;

import br.grupointegrado.facebug.model.Usuario;
import br.grupointegrado.facebug.model.UsuarioDAO;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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

    private void efetuarLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");
        try {
            Connection conn = (Connection) req.getAttribute("conexao");
            Usuario usuario = new UsuarioDAO(conn).consultaEmailSenha(email, senha);
            if (usuario != null) {
                HttpSession sessao = req.getSession();
                sessao.setAttribute("usuario_logado", usuario);
                resp.sendRedirect("/Facebug/index.jsp");
            } else {
                req.setAttribute("mensagem_erro", "E-mail ou senha incorretos, tente novamente.");
                req.getRequestDispatcher("/login.jsp").forward(req, resp);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            req.setAttribute("mensagem_erro", "Não foi possível efetuar o login, tente novamente mais tarde.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    private void efetuarCadastro(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Usuario usuario = UsuarioDAO.getUsuarioParameters(req);
            Connection conn = (Connection) req.getAttribute("conexao");
            new UsuarioDAO(conn).inserir(usuario);
            req.setAttribute("mensagem_sucesso", "Cadastro efetuado com sucesso!");
        } catch (SQLException ex) {
            ex.printStackTrace();
            req.setAttribute("mensagem_erro", "Não foi possível concluir o cadastro, tente novamente mais tarde.");
        }
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }
}
