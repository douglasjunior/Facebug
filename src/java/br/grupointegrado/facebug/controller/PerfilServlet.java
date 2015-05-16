package br.grupointegrado.facebug.controller;

import br.grupointegrado.facebug.model.Postagem;
import br.grupointegrado.facebug.model.PostagemDAO;
import br.grupointegrado.facebug.model.Usuario;
import br.grupointegrado.facebug.model.UsuarioDAO;
import br.grupointegrado.facebug.util.ConversorUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
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

}
