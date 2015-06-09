/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.grupointegrado.facebug.model;

import br.grupointegrado.facebug.util.ConversorUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import static sun.net.www.http.HttpClient.New;

/**
 *
 * @author Rafael
 */
public class FotoDAO extends DAO {

    public static Foto getFotoParameters(Map<String, Object> parametrosMultipart) {
        Foto foto = new Foto();
        foto.setFoto((byte[]) parametrosMultipart.get("foto"));
        return foto;
    }

    public FotoDAO(Connection conn) {
        super(conn);
    }

    @Override
    protected Object montaObjeto(ResultSet rs) throws SQLException {
        Foto foto = new Foto();
        foto.setId(rs.getInt("id"));
        foto.setFoto(rs.getBytes("foto"));
        foto.setUsuario(new UsuarioDAO(conn).consultaId(rs.getInt("id_usuario")));
        return foto;
    }

    public List<Foto> consultaFotosUsuario(Integer idUsuarioParam) throws SQLException {
        List fotos = consultaLista("SELECT * FROM foto WHERE id_usuario = ?", idUsuarioParam);
        return fotos;

    }

    public Foto consultaId(Integer idParam) throws SQLException {
        Object fotos = consultaUm("SELECT * FROM foto WHERE id = ?", idParam);
        return (Foto) fotos;

    }

    public void inserirFoto(Foto foto) throws SQLException {
        executaSQL("INSERT INTO foto (foto, id_usuario) "
                + "VALUES (?, ?) ",
                foto.getFoto(),
                foto.getUsuario().getId()); 
    }
}
