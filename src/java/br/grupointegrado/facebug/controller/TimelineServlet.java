/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.grupointegrado.facebug.controller;

import br.grupointegrado.facebug.exception.ValidacaoException;
import br.grupointegrado.facebug.model.PostagemDAO;
import br.grupointegrado.facebug.model.Postagem;
import br.grupointegrado.facebug.model.Usuario;
import br.grupointegrado.facebug.util.ConversorUtil;
import br.grupointegrado.facebug.util.ValidacaoUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Douglas
 */
public class TimelineServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = req.getParameter("acao");
        String codigo = req.getParameter("codigo");
        
        if (ValidacaoUtil.validaString(acao, 1) && ValidacaoUtil.validaString(codigo, 1)) {
            HttpSession session = req.getSession();
            Usuario usuario = (Usuario) session.getAttribute("usuario_logado");
            Connection conexao = (Connection) req.getAttribute("conexao");
            
            if (acao.equals("editar")) {
                try {
                    Postagem postagem = new PostagemDAO(conexao).selecionarParaEdicao(ConversorUtil.stringParaInteger(codigo), usuario);
                    session.setAttribute("mensagem_info", "Editando postagem");
                    session.setAttribute("postagem", postagem);
                } catch (SQLException ex) {
                    session.setAttribute("mensagem_info", "Não foi possível editar a postagem");
                }
            } else if (acao.equals("excluir")) {
                try {
                    new PostagemDAO(conexao).excluir(ConversorUtil.stringParaInteger(codigo));
                    session.setAttribute("mensagem_info", "Postagem excluído com sucesso.");
                } catch (SQLException ex) {
                    req.setAttribute("mensagem_erro", "Não foi possível s postagem");
                }
            }
        }
        
        /*
         * Neste método devemos carregar todos os dados necessários para exibir na Timeline
         */
        try {
            Usuario usuario = (Usuario) req.getSession().getAttribute("usuario_logado");
            Connection conn = (Connection) req.getAttribute("conexao");
            List<Postagem> postagens = new PostagemDAO(conn).ultimasPostagens(usuario);
            req.setAttribute("postagens", postagens);
        } catch (SQLException ex) {
            ex.printStackTrace();
            req.setAttribute("mensagem_erro", "Não foi possível carregar a timeline completamente, tente novamente mais tarde.");
        }
        req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String acao = (String) req.getParameter("acao");
        HttpSession session = req.getSession();
        /*
         * Para evitar duplicidade na subimissão de uma requisição POST,
         * precisamos respeitar o padrão PRG: http://en.wikipedia.org/wiki/Post/Redirect/Get
         * Este padrão diz que, a grosso modo: 
         * Quando uma requisição é bem sucediada deve-se fazer um Redirect para a próxima página.
         *
         * Sendo assim, quando a postagem for gravada com sucesso, temos que
         * efetuar um sendRedirec() para concluir o processo.
         *
         * Sabemos que ao usar o sendRedirect() não conseguimos enviar parâmetros internos para a página.
         * Sendo assim, devemos enviar a mensagem de sucesso através da Sessão.
         */
        try {
            Usuario usuario = (Usuario) req.getSession().getAttribute("usuario_logado");
            Connection conn = (Connection) req.getAttribute("conexao");
            
            Postagem postagem;
            if (ValidacaoUtil.validaValorString(acao, "editar")) {
                
                postagem = (Postagem) session.getAttribute("postagem");
                
                String texto = req.getParameter("texto");
                if (!ValidacaoUtil.validaString(texto, 1)) {
                    throw new ValidacaoException("Você precisa escrever algo para publicar uma postagem.");
                }

                postagem.setTexto(texto);
                postagem.setPublica("on".equals(req.getParameter("publica")));
                if (!postagem.getUsuario().equals(usuario)) {
                    session.setAttribute("mensagem_info", "Você não tem permissão para alterar esta postagem");
                } else {
                    new PostagemDAO(conn).alterar(postagem);
                }
                session.removeAttribute("postagem");
            } else {
                postagem = PostagemDAO.getPostagemParameters(req);
                postagem.setData(new Date());
                postagem.setUsuario(usuario);
                new PostagemDAO(conn).inserir(postagem);
            }
            
            resp.sendRedirect("/Facebug/Timeline");
        } catch (ValidacaoException ex) {
            ex.printStackTrace();
            req.setAttribute("mensagem_erro", ex.getMessage());
            doGet(req, resp);
        } catch (SQLException ex) {
            ex.printStackTrace();
            req.setAttribute("mensagem_erro", "Não foi possível inserir sua postagem, tente novamente mais tarde.");
            doGet(req, resp);
        }
    }
    
    public void teste(String nome) {
        System.out.println("Meu nome é: " + nome);
    }
}
