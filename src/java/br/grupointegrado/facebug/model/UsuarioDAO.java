package br.grupointegrado.facebug.model;

import br.grupointegrado.facebug.exception.ValidacaoException;
import br.grupointegrado.facebug.util.ConversorUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

public class UsuarioDAO extends DAO {

    /**
     * Monta um objeto Usuário a partir dos dados vindos da requsição HTTP.
     *
     * @param req
     * @return
     */
    public static Usuario getUsuarioParameters(HttpServletRequest req) {
        Usuario usuario = new Usuario();
        usuario.setId(ConversorUtil.stringParaInteger(req.getParameter("id")));
        usuario.setNome(req.getParameter("nome"));
        usuario.setSobrenome(req.getParameter("sobrenome"));
        usuario.setEmail(req.getParameter("email"));
        usuario.setSenha(req.getParameter("senha"));
        usuario.setNascimento(ConversorUtil.stringParaDate(req.getParameter("nascimento")));
        usuario.setApelido(req.getParameter("apelido"));
        usuario.setFoto(null);
        return usuario;
    }

    public UsuarioDAO(Connection conn) {
        super(conn);
    }

    /**
     * Insere um novo Usuario no banco de dados.
     *
     * @param usuario
     * @throws SQLException
     */
    public void inserir(Usuario usuario) throws ValidacaoException {
        // Chamada do métoto genérico executaSQL(), basta passar os parâmetros na ordem correta
        try {
            executaSQL("INSERT INTO usuario (nome, sobrenome, nascimento, apelido, foto, email,  senha) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?) ",
                usuario.getNome(),
                usuario.getSobrenome(),
                ConversorUtil.dateParaSQLDate(usuario.getNascimento()),
                usuario.getApelido(),
                usuario.getFoto(),
                usuario.getEmail(),
                usuario.getSenha());
        } catch (SQLException ex) {
            throw new ValidacaoException("Não foi possível concluir o cadastro, tente novamente mais tarde.");
        }
    }

    /**
     * Consulta um Usuário a partir do e-mail e senha.
     *
     * @param email
     * @param senha
     * @return
     * @throws SQLException
     */
    public Usuario consultaEmailSenha(String email, String senha) throws ValidacaoException {
        try {
            // Utilizo o método genérico para fazer a consulta.
            Object usuario = consultaUm("SELECT * FROM usuario WHERE email = ? AND senha = ? ", email, senha);
            
            if (null == usuario)
                throw new ValidacaoException("E-mail ou senha incorretos, tente novamente.");

            // Podemos fazer um cast de Object para Usuário sem problemas, 
            // pois a implementação do nosso método montaObjeto() criou um objeto do tipo Usuário.
            return (Usuario) usuario;
        } catch (SQLException ex) {
            throw new ValidacaoException("Não foi possível efetuar o login, tente novamente mais tarde.");
        }
    }

    /**
     * Consulta um usuário a partir de seu ID.
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public Usuario consultaId(int id) throws SQLException {
        // Utilizo o método genérico para fazer a consulta.
        Object usuario = consultaUm("SELECT * FROM usuario WHERE id = ? ", id);
        // Podemos fazer um cast de Object para Usuário sem problemas, 
        // pois a implementação do nosso método montaObjeto() criou um objeto do tipo Usuário.
        return (Usuario) usuario;
    }

    /**
     * Implementação do método abstrato, responsável por montar um Usuário a
     * partir dos dados do ResultSet.
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    @Override
    protected Object montaObjeto(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setSobrenome(rs.getString("sobrenome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setNascimento(rs.getDate("nascimento"));
        usuario.setApelido(rs.getString("apelido"));
        usuario.setFoto(rs.getBytes("foto"));
        return usuario;
    }

}
