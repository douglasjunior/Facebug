package br.grupointegrado.facebug.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Classe responsável por gerenciar a tabela de Amigo
 */
public class AmigoDAO extends DAO {

    private UsuarioDAO usuarioDao;

    /**
     * Cria um objeto Amigo para representar a amizade entre dois usuários.
     *
     * @param usuario
     * @param amigo
     * @return
     */
    public static Amigo getUsuarioAmigo(Usuario usuario, Usuario amigo) {
        Amigo usuarioAmigo = new Amigo();
        usuarioAmigo.setUsuario(usuario);
        usuarioAmigo.setAmigo(amigo);
        return usuarioAmigo;
    }

    public AmigoDAO(Connection conn) {
        super(conn);
        usuarioDao = new UsuarioDAO(conn);
    }

    /**
     * Implementa a criação de um Amigo a partir do ResultSet
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    @Override
    protected Object montaObjeto(ResultSet rs) throws SQLException {
        Amigo amigo = new Amigo();
        amigo.setUsuario(usuarioDao.consultaId(rs.getInt("id_usuario")));
        amigo.setAmigo(usuarioDao.consultaId(rs.getInt("id_amigo")));
        amigo.setAceitou(rs.getBoolean("aceitou"));
        return amigo;
    }

    /**
     * Insere um novo Amigo no banco de dados
     *
     * @param amigo
     * @throws SQLException
     */
    public void inserir(Amigo amigo) throws SQLException {
        executaSQL("INSERT INTO amigo (id_usuario, id_amigo, aceitou) VALUES (?, ?, ?)",
                amigo.getUsuario().getId(),
                amigo.getAmigo().getId(),
                amigo.isAceitou());
    }

    /**
     * Exclui um Amigo do banco de dados
     *
     * @param amigo
     * @throws SQLException
     */
    public void excluir(Amigo amigo) throws SQLException {
        executaSQL("DELETE FROM amigo WHERE id_usuario = ? AND id_amigo = ?",
                amigo.getUsuario().getId(),
                amigo.getAmigo().getId());
    }

    /**
     * Consulta a relação de amizade entre dois usuários
     *
     * @param usuario
     * @param usuarioAmigo
     * @return
     * @throws SQLException
     */
    public Amigo consultaUsuarioAmigo(Usuario usuario, Usuario usuarioAmigo) throws SQLException {
        Amigo amigo = (Amigo) consultaUm("SELECT * FROM amigo WHERE (id_usuario = ? AND id_amigo = ?) OR (id_amigo = ? AND id_usuario = ?) LIMIT 1 ",
                usuario.getId(),
                usuarioAmigo.getId(),
                usuario.getId(),
                usuarioAmigo.getId());
        return amigo;
    }

    /**
     * Consulta todos os amigos de um Usuário
     *
     * @param usuario
     * @return
     * @throws SQLException
     */
    public List<Amigo> consultaAmigos(Usuario usuario) throws SQLException {
        List<Amigo> amigos = consultaLista("SELECT * FROM amigo WHERE id_usuario = ? OR id_amigo = ?",
                usuario.getId(),
                usuario.getId());
        return amigos;
    }
}
