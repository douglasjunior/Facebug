package br.grupointegrado.facebug.controller;

import br.grupointegrado.facebug.exception.ValidacaoException;
import br.grupointegrado.facebug.model.Amigo;
import br.grupointegrado.facebug.model.AmigoDAO;
import br.grupointegrado.facebug.model.Foto;
import br.grupointegrado.facebug.model.FotoDAO;
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
            String acaoParam = req.getParameter("acao");
            Connection conn = (Connection) req.getAttribute("conexao");
            Usuario usuarioLogado = (Usuario) req.getSession().getAttribute("usuario_logado");
            /*
             *  Verifica qual ação será executadas
             */
            if ("adicionar".equals(acaoParam)) {
                doGetAdicionarAmigo(req, resp, conn, idParam);
            } else if ("remover".equals(acaoParam)) {
                doGetRemoverAmigo(req, resp, conn, idParam);
            }
            /*
             * Tenta carregar o usuário através do ID, se não conseguir então carrega o usuário logado
             */
            Usuario usuario = new UsuarioDAO(conn).consultaId(ConversorUtil.stringParaInteger(idParam));
            if (usuario == null) {
                usuario = usuarioLogado;
            }
            req.setAttribute("usuario", usuario);
            /*
             * Carrega somente as postagens do usuário que foi carregado. 
             */
            List<Postagem> postagens = new PostagemDAO(conn).ultimasPostagens(usuario);
            req.setAttribute("postagens", postagens);
            /*
             * Carrega todos os amigos do usuário do perfil
             */
            List<Amigo> amigos = new AmigoDAO(conn).consultaAmigos(usuario);
            req.setAttribute("amigos", amigos);
            /*
             * Carrega a relação de amigo do usuário do perfil com o usuário logado (se eles não forem os mesmos)
             */
            if (!usuario.equals(usuarioLogado)) {
                Amigo amigo = new AmigoDAO(conn).consultaUsuarioAmigo(usuario, usuarioLogado);
                req.setAttribute("amigo", amigo);
            }
            /*
             Carrega as fotos do usuário.
             */
            List<Foto> fotos = new FotoDAO(conn).consultaFotosUsuario(usuario.getId());
            req.setAttribute("fotos", fotos);
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
            } else if ("salvarAlbum".equals(acao)) {
                doPostSalvarFoto(req, parametrosMultipart);
                resp.sendRedirect("/Facebug/Perfil#fotos");
                System.out.println("ENTROU PARA SALVAR FOTO");
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

    private void doGetAdicionarAmigo(HttpServletRequest req, HttpServletResponse resp, Connection conn, String idParam) throws SQLException {
        UsuarioDAO usuarioDao = new UsuarioDAO(conn);
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario_logado");
        /*
         * Recupera o usuário amigo e verifica se ele existe.
         */
        Usuario usuarioAmigo = usuarioDao.consultaId(ConversorUtil.stringParaInteger(idParam));
        if (usuarioAmigo == null) {
            // Se o usuário amigo não existe, então retorna sem fazer nada
            return;
        }
        if (usuario.equals(usuarioAmigo)) {
            // Se o amigo é igual ao usuário logado, então retorna sem fazer nada
            return;
        }
        Amigo amigo = AmigoDAO.getUsuarioAmigo(usuario, usuarioAmigo);
        new AmigoDAO(conn).inserir(amigo);
    }

    private void doGetRemoverAmigo(HttpServletRequest req, HttpServletResponse resp, Connection conn, String idParam) throws SQLException {
        UsuarioDAO usuarioDao = new UsuarioDAO(conn);
        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario_logado");
        /*
         * Recupera o usuário amigo e verifica se ele existe.
         */
        Usuario usuarioAmigo = usuarioDao.consultaId(ConversorUtil.stringParaInteger(idParam));
        if (usuarioAmigo == null) {
            // Se o usuário amigo não existe, então retorna sem fazer nada
            return;
        }
        if (usuario.equals(usuarioAmigo)) {
            // Se o amigo é igual ao usuário logado, então retorna sem fazer nada
            return;
        }
        AmigoDAO dao = new AmigoDAO(conn);
        Amigo amigo = dao.consultaUsuarioAmigo(usuario, usuarioAmigo);
        if (amigo == null) {
            // Se a relação de amizade não existe, então retorna sem fazer nada
            return;
        }
        dao.excluir(amigo);
    }

    private void doPostSalvarFoto(HttpServletRequest req, Map<String, Object> parametrosMultipart) throws ServletException, IOException, ValidacaoException, FileUploadException, Exception {

        Usuario usuario = (Usuario) req.getSession().getAttribute("usuario_logado");
        Foto foto = FotoDAO.getFotoParameters(parametrosMultipart);
        foto.setUsuario(usuario);
        Connection conn = (Connection) req.getAttribute("conexao");

        FotoDAO dao = new FotoDAO(conn);

        dao.inserirFoto(foto);
        req.getSession().setAttribute("mensagem_sucesso", "Foto Inserida com sucesso.");
    }
}
