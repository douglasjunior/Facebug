/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.grupointegrado.facebug.controller;

import br.grupointegrado.facebug.model.PostagemDAO;
import br.grupointegrado.facebug.model.Postagem;
import br.grupointegrado.facebug.model.Usuario;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Douglas
 */
public class TimelineServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // implementar consulta
        try{
            Usuario usuario = (Usuario) req.getSession().getAttribute("usuario_logado");
            Connection conn = (Connection) req.getAttribute("conexao");
            List<Postagem> postagens = new PostagemDAO(conn).ultimasPostagens(usuario);
            req.setAttribute("postagens", postagens);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Usuario usuario = (Usuario) req.getSession().getAttribute("usuario_logado");
            Connection conn = (Connection) req.getAttribute("conexao");
            Postagem postagem = PostagemDAO.getPostagemParameters(req);
            postagem.setData(new Date());
            postagem.setUsuario(usuario);
            new PostagemDAO(conn).inserir(postagem);
        } catch (SQLException ex) {
            ex.printStackTrace();
            req.setAttribute("mensagem_erro", "Não foi possível salvar sua postagem.");
        }
        doGet(req, resp);
    }

}
