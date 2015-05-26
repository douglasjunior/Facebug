package br.grupointegrado.facebug.filter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * Filtro utilizado para conectar com o banco de dados e passar a conexão para
 * os Servlets
 *
 * @author douglas
 */
public class ConexaoFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        /*
         Alguns servidores como o TOMCAT, vem por padrão com codificação ISO-8859-1, sendo assim é necessário mudar essa configuração para UTF-8. Caso contrário teremos problemas com acentuação nos parâmetros.
         Para evitar de ter que mudar a configuração no Servidor, podemos alterar o Encoding do Request e do Response em todas as requisições.
         Neste caso, podemos aproveitar o filtro ConexaoFilter para tal ação.
         */
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        Connection conn = null;
        try {
            conn = abrirConexao();
            request.setAttribute("conexao", conn);
            chain.doFilter(request, response);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            fecharConexao(conn);
        }
    }

    /**
     * Cria uma conexão JDBC
     *
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private Connection abrirConexao() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/facebugdb";
        return DriverManager.getConnection(url, "root", "xy234786");
    }

    /**
     * Encerra uma conexão JDBC
     *
     * @param conn
     */
    private void fecharConexao(Connection conn) {
        try {
            conn.close();
        } catch (Exception ex) {
        }
    }
    
    @Override
    public void destroy() {
    }
}
