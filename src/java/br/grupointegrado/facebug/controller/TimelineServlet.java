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
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Douglas
 */
public class TimelineServlet extends HttpServlet {

    /**
     * Neste método devemos carregar todos os dados necessários para exibir na
     * Timeline
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String acaoParam = req.getParameter("acao");
            if ("excluir".equals(acaoParam)) {
                doGetExcuir(req, resp);
            } else if ("editar".equals(acaoParam)) {
                doGetEditar(req, resp);
            }
            Connection conn = (Connection) req.getAttribute("conexao");
            Usuario usuario = (Usuario) req.getSession().getAttribute("usuario_logado");
            List<Postagem> postagens = new PostagemDAO(conn).ultimasPostagens(usuario);
            req.setAttribute("postagens", postagens);
        } catch (SQLException ex) {
            ex.printStackTrace();
            req.setAttribute("mensagem_erro", "Não foi possível carregar a timeline completamente, tente novamente mais tarde.");
        }
        req.getRequestDispatcher("/WEB-INF/index.jsp").forward(req, resp);
    }

    /**
     * Recebe a requisição GET e exclui a postagem do banco de dados.
     *
     * @param req
     * @param resp
     */
    private void doGetExcuir(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String idParam = req.getParameter("id");
        Connection conn = (Connection) req.getAttribute("conexao");
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario_logado");
        PostagemDAO dao = new PostagemDAO(conn);
        Postagem postagem = dao.consultaId(idParam);
        /*
         * Verifica se a postagem a ser alterada é do usuário que está alterando
         */
        if (!postagem.getUsuario().equals(usuario)) {
            req.setAttribute("mensagem_erro", "Você não tem permissão para excluir esta postagem.");
        } else {
            dao.excluir(postagem);
            req.getSession().setAttribute("mensagem_sucesso", "Postagem excluida com sucesso!");
        }
    }

    /**
     * Recebe a requisição GET, carrega a postagem para edição e envia para a
     * página
     *
     * @param req
     * @param resp
     * @throws SQLException
     */
    private void doGetEditar(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String idParam = req.getParameter("id");
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario_logado");
        Connection conn = (Connection) req.getAttribute("conexao");
        Postagem postagem = new PostagemDAO(conn).consultaId(idParam);
        /*
         * Verifica se a postagem a ser alterada é do usuário que está alterando
         */
        if (!postagem.getUsuario().equals(usuario)) {
            req.setAttribute("mensagem_erro", "Você não tem permissão para editar esta postagem.");
        } else {
            req.setAttribute("postagem", postagem);
        }
    }

    /**
     * Para evitar duplicidade na subimissão de uma requisição POST, precisamos
     * respeitar o padrão PRG: http://en.wikipedia.org/wiki/Post/Redirect/Get
     * <br />
     * Este padrão diz que, a grosso modo: Quando uma requisição é bem sucediada
     * deve-se fazer um Redirect para a próxima página.
     *
     * Sendo assim, quando a postagem for gravada com sucesso, temos que efetuar
     * um sendRedirec() para concluir o processo.
     *
     * Sabemos que ao usar o sendRedirect() não conseguimos enviar parâmetros
     * internos para a página. Sendo assim, devemos enviar a mensagem de sucesso
     * através da Sessão.
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            /*
             Recupera o parâmetro vindo do formulário e verifica se é uma inserção ou edição
             */
            int idParam = ConversorUtil.stringParaInteger(req.getParameter("id"));
            if (idParam > 0) {
                doPostEditar(req, resp);
            } else {
                doPostInserir(req, resp);
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

    private void doPostInserir(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ValidacaoException {
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario_logado");
        Connection conn = (Connection) req.getAttribute("conexao");
        Postagem postagem = PostagemDAO.getPostagemParameters(req);
        postagem.setData(new Date());
        postagem.setUsuario(usuario);
        new PostagemDAO(conn).inserir(postagem);
    }

    private void doPostEditar(HttpServletRequest req, HttpServletResponse resp) throws SQLException, ValidacaoException {
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario_logado");
        Connection conn = (Connection) req.getAttribute("conexao");
        PostagemDAO dao = new PostagemDAO(conn);
        String idParam = req.getParameter("id");
        String textoParam = req.getParameter("texto");
        boolean publica = "on".equals(req.getParameter("publica"));
        if (!ValidacaoUtil.validaString(textoParam, 1)) {
            throw new ValidacaoException("Você precisa escrever algo para publicar uma postagem.");
        }
        Postagem postagem = dao.consultaId(idParam);
        /*
         * Verifica se a postagem a ser alterada é do usuário que está alterando
         */
        if (!postagem.getUsuario().equals(usuario)) {
            throw new ValidacaoException("Você não tem permissão para editar esta postagem.");
        }
        postagem.setTexto(textoParam);
        postagem.setPublica(publica);
        dao.editar(postagem);
        req.getSession().setAttribute("mensagem_sucesso", "Postagem editada com sucesso!");
    }

}
