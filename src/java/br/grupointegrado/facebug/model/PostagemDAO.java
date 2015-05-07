package br.grupointegrado.facebug.model;

import br.grupointegrado.facebug.util.ConversorUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

public class PostagemDAO {

    public static Postagem getPostagemParameters(HttpServletRequest req) {
        Postagem postagem = new Postagem();
        postagem.setTexto(req.getParameter("texto"));
        postagem.setPublica("on".equals(req.getParameter("publica")));
        return postagem;
    }
    private final Connection conn;

    public PostagemDAO(Connection conn) {
        this.conn = conn;
    }

    public void inserir(Postagem postagem) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO postagem (texto, data, id_usuario, publica) "
                + "VALUES (?, ?, ?, ?) ");
        ps.setString(1, postagem.getTexto());
        ps.setTimestamp(2, ConversorUtil.dateParaTimeStamp(postagem.getData()));
        ps.setInt(3, postagem.getUsuario().getId());
        ps.setBoolean(4, postagem.isPublica());
        ps.execute();
        ps.close();
    }

    public List<Postagem> ultimasPostagens(Usuario usuarioLogado) throws SQLException {
        List<Postagem> postagens = new ArrayList<Postagem>();
        PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM postagem WHERE id_usuario = ? ORDER BY id DESC");
        ps.setInt(1, usuarioLogado.getId());
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Postagem postagem = montaPostagem(rs);
            postagens.add(postagem);
        }
        rs.close();
        ps.close();
        return postagens;
    }

    private Postagem montaPostagem(ResultSet rs) throws SQLException {
        Postagem postagem = new Postagem();
        postagem.setId(rs.getInt("id"));
        postagem.setTexto(rs.getString("texto"));
        Usuario usuario = new UsuarioDAO(conn).consultaId(rs.getInt("id_usuario"));
        postagem.setUsuario(usuario);
        postagem.setData(rs.getTimestamp("data"));
        postagem.setPublica(rs.getBoolean("publica"));
        return postagem;
    }

}
