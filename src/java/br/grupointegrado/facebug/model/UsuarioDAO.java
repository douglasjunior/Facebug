package br.grupointegrado.facebug.model;

import br.grupointegrado.facebug.util.ConversorUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;

public class UsuarioDAO {

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

    private final Connection conn;

    public UsuarioDAO(Connection conn) {
        this.conn = conn;
    }

    public void inserir(Usuario usuario) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO usuario (nome, sobrenome, nascimento, apelido, foto, email,  senha) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?) ");
        ps.setString(1, usuario.getNome());
        ps.setString(2, usuario.getSobrenome());
        ps.setDate(3, ConversorUtil.dateParaSQLDate(usuario.getNascimento()));
        ps.setString(4, usuario.getApelido());
        ps.setBytes(5, usuario.getFoto());
        ps.setString(6, usuario.getEmail());
        ps.setString(7, usuario.getSenha());
        ps.execute();
        ps.close();
    }

    public Usuario consultaEmailSenha(String email, String senha) throws SQLException {
        Usuario usuario = null;
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM usuario WHERE email = ? AND senha = ? ");
        ps.setString(1, email);
        ps.setString(2, senha);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            usuario = montaUsuario(rs);
        }
        rs.close();
        ps.close();
        return usuario;
    }

    private Usuario montaUsuario(ResultSet rs) throws SQLException {
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
