/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.grupointegrado.facebug.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Classe responsável por manter o cadastro de Fotos no banco de dados.
 *
 * @author Rafael
 */
public class FotoDAO extends DAO {

    /**
     * Cria um objeto Foto a partir dos parâmetros recebidos do formulário
     * Multipart.
     *
     * @param parametrosMultipart
     * @return
     */
    public static Foto getFotoParameters(Map<String, Object> parametrosMultipart) {
        Foto foto = new Foto();
        foto.setFoto((byte[]) parametrosMultipart.get("foto"));
        return foto;
    }

    public FotoDAO(Connection conn) {
        super(conn);
    }

    /**
     * Monta um objeto Foto a partir do Resultset.
     *
     * @param rs
     * @return
     * @throws SQLException
     */
    @Override
    protected Object montaObjeto(ResultSet rs) throws SQLException {
        Foto foto = new Foto();
        foto.setId(rs.getInt("id"));
        foto.setFoto(rs.getBytes("foto"));
        foto.setUsuario(new UsuarioDAO(conn).consultaId(rs.getInt("id_usuario")));
        return foto;
    }

    /**
     * Retorna todas as fotos de um usuário.
     *
     * @param usuario
     * @return
     * @throws SQLException
     */
    public List<Foto> consultaFotosUsuario(Usuario usuario) throws SQLException {
        List fotos = consultaLista("SELECT * FROM foto WHERE id_usuario = ?", usuario.getId());
        return fotos;
    }

    /**
     * Consulta uma foto a partir de seu ID.
     *
     * @param idParam
     * @return
     * @throws SQLException
     */
    public Foto consultaId(Integer idParam) throws SQLException {
        Object fotos = consultaUm("SELECT * FROM foto WHERE id = ?", idParam);
        return (Foto) fotos;
    }

    /**
     * Insere uma nova foto no banco de dados.
     *
     * @param foto
     * @throws SQLException
     */
    public void inserirFoto(Foto foto) throws SQLException {
        executaSQL("INSERT INTO foto (foto, id_usuario) "
                + "VALUES (?, ?) ",
                foto.getFoto(),
                foto.getUsuario().getId());
    }
}
