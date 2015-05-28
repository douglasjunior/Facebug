package br.grupointegrado.facebug.controller;

import br.grupointegrado.facebug.exception.ValidacaoException;
import br.grupointegrado.facebug.model.Postagem;
import br.grupointegrado.facebug.model.PostagemDAO;
import br.grupointegrado.facebug.model.Usuario;
import br.grupointegrado.facebug.model.UsuarioDAO;
import br.grupointegrado.facebug.util.ConversorUtil;
import br.grupointegrado.facebug.util.ServletUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.FileUploadException;

/**
 * Servlet resposnábel pelas requisições da página de Perfil.
 *
 * @author Douglas
 */
public class PerfilServlet extends HttpServlet {
 
    /**
     * Na requisição por GET devemos carregar todos os dados necessários para
     * exibir a página de perfil do isioário.<br>
     * Como os dados do Usuário, Postagens, Fotos e Amigos.
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String idParam = req.getParameter("id");
            Connection conn = (Connection) req.getAttribute("conexao");
            /*
             * Tenta carregar o usuário através do ID, se não conseguir então carrega o usuário logado
             */
            Usuario usuario = new UsuarioDAO(conn).consultaId(ConversorUtil.stringParaInteger(idParam));
            if (usuario == null) {
                usuario = (Usuario) req.getSession().getAttribute("usuario_logado");
            }
            req.setAttribute("usuario", usuario);
            /*
             * Carrega somentes as postagens do usuário que foi carregado. 
             */
            List<Postagem> postagens = new PostagemDAO(conn).ultimasPostagens(usuario);
            req.setAttribute("postagens", postagens);
        } catch (SQLException ex) {
            ex.printStackTrace();
            req.setAttribute("mensagem_erro", "Não foi possível carregar o perfil completamente, tente novamente mais tarde.");
        }
        req.getRequestDispatcher("/WEB-INF/perfil.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String acao = "";
            // recupera os parâmetros de requisição multipart
            Map<String, Object> parametrosMultipart = ServletUtil.recuperaParametrosMultipart(req);
            // se os parâmetros não forem NULL, então se trata de uma requisição multipart
            if (parametrosMultipart != null) {
                acao = (String) parametrosMultipart.get("acao");
            } else {
                // se a requisição não é multipart, então os parâmetros são recuperados normalmente
                acao = req.getParameter("acao");
            }

            // verifica qual é a ação recebida
            if ("editar".equals(acao)) {
                doPostEditarPerfil(req, parametrosMultipart);
                resp.sendRedirect("/Facebug/Perfil#perfil");
            }

        } catch (ValidacaoException ex) {
            ex.printStackTrace();
            req.setAttribute("mensagem_erro", ex.getMessage());
            doGet(req, resp);
        } catch (FileUploadException ex) {
            ex.printStackTrace();
            req.setAttribute("mensagem_erro", "Não foi possível realizar o upload do arquivo, verifique se o arquivo está correto.");
            doGet(req, resp);
        } catch (Exception ex) {
            ex.printStackTrace();
            req.setAttribute("mensagem_erro", "Ocorreu um erro inesperado, tente novamente mais tarde.");
            doGet(req, resp);
        }
    }

    private void doPostEditarPerfil(HttpServletRequest req, Map<String, Object> parametrosMultipart) throws ServletException, IOException, ValidacaoException, FileUploadException, Exception {
        Connection conn = (Connection) req.getAttribute("conexao");
        UsuarioDAO dao = new UsuarioDAO(conn);

        /*
         Monta o usuário com os parâmetros vindos na requisição Multipart
         */
        Usuario usuario = UsuarioDAO.getUsuarioParameters(parametrosMultipart);
        dao.editar(usuario);

        /*
         Substitui o usuário logado na sessão com os dados atualizados
         */
        req.getSession().setAttribute("usuario_logado", dao.consultaId(usuario.getId()));

        req.getSession().setAttribute("mensagem_sucesso", "Os dados do usuário foram atualizados com sucesso.");
    }
}
