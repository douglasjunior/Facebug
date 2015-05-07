package br.grupointegrado.facebug.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Este DAO abstrato é responsável por agrupar os códigos que são comuns e
 * existem em todos os DAO.
 *
 * @author douglas
 */
public abstract class DAO {

    protected final Connection conn;

    public DAO(Connection conn) {
        this.conn = conn;
    }

    /**
     * Método de consulta genérico, responsável por retornar um único objeto.
     *
     * @param sqlConsulta
     * @param parametros
     * @return
     * @throws SQLException
     */
    protected Object consultaUm(String sqlConsulta, Object... parametros) throws SQLException {
        Object objeto = null;
        PreparedStatement ps = conn.prepareStatement(sqlConsulta);
        // percorre o array de parâmetros para inserir no ResultSet
        for (int i = 0; i < parametros.length; i++) {
            int index = i + 1;
            ps.setObject(index, parametros[i]);
        }
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            objeto = montaObjeto(rs);
        }
        rs.close();
        ps.close();
        return objeto;
    }

    /**
     * Método de consulta genérico, responsável por retornar uma lista de
     * objetos.
     *
     * @param sqlConsulta
     * @param parametros
     * @return
     * @throws SQLException
     */
    protected List consultaLista(String sqlConsulta, Object... parametros) throws SQLException {
        List<Object> objetos = new ArrayList<Object>();
        PreparedStatement ps = conn.prepareStatement(sqlConsulta);
        // percorre o array de parâmetros para inserir no ResultSet
        for (int i = 0; i < parametros.length; i++) {
            int index = i + 1;
            ps.setObject(index, parametros[i]);
        }
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Object objeto = montaObjeto(rs);
            objetos.add(objeto);
        }
        rs.close();
        ps.close();
        return objetos;
    }

    /**
     * Método genérico para executar instruções no banco de dados. Como um
     * INSERT, UPDATE, ALTER TABLE, etc
     *
     * @param sqlConsulta
     * @param parametros
     * @throws SQLException
     */
    protected void executaSQL(String sqlConsulta, Object... parametros) throws SQLException {
        PreparedStatement ps = conn.prepareStatement(sqlConsulta);
        for (int i = 0; i < parametros.length; i++) {
            int index = i + 1;
            ps.setObject(index, parametros[i]);
        }
        ps.execute();
        ps.close();
    }

    /**
     * Método responsável por montar o objeto com os dados do ResultSet. <br>
     * Este método deve ser abstrato, pois sua implementação será diferente em
     * cada um dos DAOs.
     *
     * @param rs
     * @return
     * @throws java.sql.SQLException
     */
    protected abstract Object montaObjeto(ResultSet rs) throws SQLException;
}
