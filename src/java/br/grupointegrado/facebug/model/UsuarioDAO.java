package br.grupointegrado.facebug.model;

import br.grupointegrado.facebug.util.ConversorUtil;
import java.sql.Connection;
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

    public UsuarioDAO(Connection conn) {
        // implementar
    }

    public void inserir(Usuario usuario) {
        // implementar
    }

}
