package br.grupointegrado.facebug.controller;

import br.grupointegrado.facebug.model.UsuarioDAO;
import br.grupointegrado.facebug.util.ConversorUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet utilizado para carregamento de imagens do banco de dados.
 *
 * @author Antonio
 */
public class ImagemServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // configura que o retorno da requisição será uma imagem
        resp.setContentType("image/jpg;image/png");
        try {
            /*
             verifica qual o tipo de imagem está sendo solicitada, se é do perfil, do album de fotos, etc...
             */
            String origem = req.getParameter("origem");
            if ("usuario".equals(origem)) {
                // recupera imagem do perfil do usuário
                doGetFotoUsuario(req, resp);
            } else if ("album".equals(origem)) {
                // recupera imagem do album de fotos
                doGetFotoAlbum(req, resp);
            } else {
                // se a Origem não foi informada, devolve um erro 400 BAD REQUEST
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Recupera a foto do perfil do usuário do banco de dados.
     *
     * @param req
     * @param resp
     * @throws IOException
     * @throws SQLException
     */
    private void doGetFotoUsuario(HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        Connection conn = (Connection) req.getAttribute("conexao");
        Integer idParam = ConversorUtil.stringParaInteger(req.getParameter("id"));
        if (idParam == 0) {
            // se o ID não foi informado, devolve um erro 400 BAD REQUEST
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        byte[] fotoUsuario = new UsuarioDAO(conn).consultaFoto(idParam);
        // se o usuário não tem foto, então carrega uma imagem padrão
        if (fotoUsuario == null) {
            fotoUsuario = carregarImagemPadrao();
        }
        // escreve os bytes da imagem como resposta na requisição
        OutputStream out = resp.getOutputStream();
        out.write(fotoUsuario);
        out.flush();
    }

    /**
     * Lê a imagem padrão do usuário.
     *
     * @return
     * @throws IOException
     */
    private byte[] carregarImagemPadrao() throws IOException {
        String caminho = getServletContext().getRealPath("/imagens/");
        FileInputStream fileinputstream = new FileInputStream(new File(caminho, "perfil-padrao.jpg"));
        byte bytearray[] = null;
        try {
            int numberBytes = fileinputstream.available();
            bytearray = new byte[numberBytes];
            fileinputstream.read(bytearray);
        } finally {
            fileinputstream.close();
        }
        return bytearray;
    }

    private void doGetFotoAlbum(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        /*
         IMPLEMENTAR
         */
        byte[] fotoAlbum = null;
        // se o usuário não tem foto, então carrega uma imagem padrão
        if (fotoAlbum == null) {
            fotoAlbum = carregarImagemPadrao();
        }
        // escreve os bytes da imagem como resposta na requisição
        OutputStream out = resp.getOutputStream();
        out.write(fotoAlbum);
        out.flush();
    }
}
