package br.grupointegrado.facebug.model;

import br.grupointegrado.facebug.util.ValidacaoUtil;
import br.grupointegrado.facebug.exception.ValidacaoException;
import br.grupointegrado.facebug.util.ConversorUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class PostagemDAO extends DAO {

    /**
     * Monta um objeto Postagem a partir dos dados vindos na requisição HTTP.
     *
     * @param req
     * @return
     */
    public static Postagem getPostagemParameters(HttpServletRequest req) throws ValidacaoException {
        Postagem postagem = new Postagem();

        String texto = req.getParameter("texto");
        if (!ValidacaoUtil.validaString(texto, 1)) {
            throw new ValidacaoException("Você precisa escrever algo para publicar uma postagem.");
        }

        postagem.setTexto(texto);
        postagem.setPublica("on".equals(req.getParameter("publica")));
        return postagem;
    }

    public PostagemDAO(Connection conn) {
        super(conn);
    }

    /**
     * Insere uma nova postagem no banco de dados.
     *
     * @param postagem
     * @throws SQLException
     */
    public void inserir(Postagem postagem) throws SQLException {
        executaSQL("INSERT INTO postagem (texto, data, id_usuario, publica) "
                + "VALUES (?, ?, ?, ?) ",
                postagem.getTexto(),
                ConversorUtil.dateParaTimeStamp(postagem.getData()),
                postagem.getUsuario().getId(),
                postagem.isPublica());
    }

    /**
     * Carrega as últimas postagens do Usuário que está logado. Nessas últimas
     * postagens devem conter: <br>
     * - Suas próprias postagens; <br>
     * - Postagens de amigos.
     *
     * @param usuarioLogado
     * @return
     * @throws SQLException
     */
    public List<Postagem> ultimasPostagens(Usuario usuarioLogado) throws SQLException {
        // Chama o método genérico consultaLista()
        List postagens = consultaLista(
                "SELECT * FROM postagem WHERE id_usuario = ? ORDER BY id DESC",
                usuarioLogado.getId());
        return postagens;
    }

    /**
     * Monta um objeto Postagem a partir dos dados do ResultSer.
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    @Override
    protected Object montaObjeto(ResultSet rs) throws SQLException {
        Postagem postagem = new Postagem();
        postagem.setId(rs.getInt("id"));
        postagem.setTexto(rs.getString("texto"));
        Usuario usuario = new UsuarioDAO(conn).consultaId(rs.getInt("id_usuario"));
        postagem.setUsuario(usuario);
        postagem.setData(rs.getTimestamp("data"));
        postagem.setPublica(rs.getBoolean("publica"));
        return postagem;
    }

    /**
     * Consulta uma postagem pelo ID
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public Postagem consultaId(String id) throws SQLException {
        // Chama o método genérico consultaUm()
        Postagem postagem = (Postagem) consultaUm("SELECT * FROM postagem WHERE id = ?", id);
        return postagem;
    }

    /**
     * Edita todos os campos de uma postagem no banco de dados.
     *
     * @param postagem
     * @throws SQLException
     */
    public void editar(Postagem postagem) throws SQLException {
        executaSQL("UPDATE postagem SET texto = ?, data = ?, id_usuario = ?, publica = ? "
                + "WHERE id = ? ",
                postagem.getTexto(),
                ConversorUtil.dateParaTimeStamp(postagem.getData()),
                postagem.getUsuario().getId(),
                postagem.isPublica(),
                // não esquecer de setar o ID no Where
                postagem.getId());
    }

    /**
     * Remove uma postagem do banco de dados
     *
     * @param postagem
     * @throws SQLException
     */
    public void excluir(Postagem postagem) throws SQLException {
        executaSQL("DELETE FROM postagem WHERE id = ? ",
                postagem.getId());
    }
}
