package br.grupointegrado.facebug.model;

import br.grupointegrado.facebug.exception.ValidacaoException;
import br.grupointegrado.facebug.util.ConversorUtil;
import br.grupointegrado.facebug.util.CriptografiaUtil;
import br.grupointegrado.facebug.util.ValidacaoUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

public class UsuarioDAO extends DAO {

    /**
     * Monta um objeto Usuário a partir dos dados vindos da requsição HTTP.
     *
     * @param req
     * @return
     * @throws br.grupointegrado.facebug.exception.ValidacaoException
     */
    public static Usuario getUsuarioParameters(HttpServletRequest req) throws ValidacaoException {
        /*
         Converte os parâmetros vindos na requisição, por um Map contendo todos os parâmetros.
         Assim podemos aproveitar o mesmo método que faz a validação e montagem do objeto.
         */
        Map<String, Object> parametros = new HashMap<String, Object>();
        Enumeration<String> nomesParametros = req.getParameterNames();
        while (nomesParametros.hasMoreElements()) {
            String nome = nomesParametros.nextElement();
            String valor = req.getParameter(nome);
            // salva o parâmetro e seu valor no Map
            parametros.put(nome, valor);
        }
        return getUsuarioParameters(parametros);
    }

    /**
     * Monta um objeto Usuário a partir dos dados vindos de um Map
     *
     * @param parametrosMultipart
     * @return
     * @throws br.grupointegrado.facebug.exception.ValidacaoException
     */
    public static Usuario getUsuarioParameters(Map<String, Object> parametrosMultipart) throws ValidacaoException {
        Integer id = ConversorUtil.stringParaInteger((String) parametrosMultipart.get("id"));
        Boolean cadastrando = id == 0;
        String nome = (String) parametrosMultipart.get("nome");
        String sobrenome = (String) parametrosMultipart.get("sobrenome");
        String email = (String) parametrosMultipart.get("email");
        byte[] foto = (byte[]) parametrosMultipart.get("foto");
        String senha = (String) parametrosMultipart.get("senha");
        Boolean habilitaSenha = "on".equals(parametrosMultipart.get("habilitaSenha"));

        if (!ValidacaoUtil.validaString(nome, 1)) {
            throw new ValidacaoException("Informe o nome.");
        }

        if (!ValidacaoUtil.validaString(sobrenome, 1)) {
            throw new ValidacaoException("Informe o sobrenome.");
        }

        // valida o e-mail só quando o ID for igual a 0, ou seja, quando estiver inserindo
        if (id == 0 && !ValidacaoUtil.validaEmail(email)) {
            throw new ValidacaoException("Informe um enedreço de e-mail válido.");
        }

        // Valida a senha somente se estiver cadastrando, ou se o "habilitaSenha" for selecionado.
        if (cadastrando || habilitaSenha) {
            if (!ValidacaoUtil.validaSenha(senha)) {
                throw new ValidacaoException("Informe a senha com 8 caracteres ou mais, e que contenha letras e números.");
            }
        }

        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome(nome);
        usuario.setSobrenome(sobrenome);
        usuario.setEmail(email);
        usuario.setNascimento(ConversorUtil.stringParaDate((String) parametrosMultipart.get("nascimento")));
        usuario.setApelido((String) parametrosMultipart.get("apelido"));
        usuario.setFoto(foto);
        usuario.setSenha(CriptografiaUtil.criptografarMD5(senha));
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
    public void inserir(Usuario usuario) throws SQLException {
        // Chamada do métoto genérico executaSQL(), basta passar os parâmetros na ordem correta
        executaSQL("INSERT INTO usuario (nome, sobrenome, nascimento, apelido, foto, email,  senha) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?) ",
                usuario.getNome(),
                usuario.getSobrenome(),
                ConversorUtil.dateParaSQLDate(usuario.getNascimento()),
                usuario.getApelido(),
                usuario.getFoto(),
                usuario.getEmail(),
                usuario.getSenha());
    }

    /**
     * Edita todos os campos (exceto o e-mail) de um usuário no banco de dados.
     *
     * @param novoUsuario
     * @throws SQLException
     */
    public void editar(Usuario novoUsuario) throws SQLException {
        /*
         usuarioNovo: é o objeto Usuário com os novos dados para edição
         usuarioAntigo: é o objeto Usuário com as informações antigas do banco de dados
         */
        Usuario usuarioAntigo = consultaId(novoUsuario.getId());

        // busca a foto antiga que estava armazenada no banco de dados.
        byte[] foto = consultaFoto(novoUsuario.getId());

        // se o novo usuário está sem foto, então mantem a foto antiga
        if (novoUsuario.getFoto() == null || novoUsuario.getFoto().length == 0) {
            novoUsuario.setFoto(foto);
        }

        // se o novo usuário está sem senha, então mantem a senha antiga
        if (novoUsuario.getSenha() == null || novoUsuario.getSenha().isEmpty()) {
            novoUsuario.setSenha(usuarioAntigo.getSenha());
        }

        // finalmente executa o UPDATE
        executaSQL("UPDATE usuario SET nome = ?, sobrenome = ?, apelido = ?, foto = ?, nascimento = ?, senha = ?"
                + "WHERE id = ? ",
                novoUsuario.getNome(),
                novoUsuario.getSobrenome(),
                novoUsuario.getApelido(),
                novoUsuario.getFoto(),
                ConversorUtil.dateParaSQLDate(novoUsuario.getNascimento()),
                novoUsuario.getSenha(),
                // não esquecer de setar o ID no Where
                novoUsuario.getId());
    }

    /**
     * Consulta um Usuário a partir do e-mail e senha.
     *
     * @param email
     * @param senha
     * @return
     * @throws SQLException
     */
    public Usuario consultaEmailSenha(String email, String senha) throws SQLException {
        // Utilizo o método genérico para fazer a consulta.
        Object usuario = consultaUm("SELECT * FROM usuario WHERE email = ? AND senha = ? ", email, senha);
        // Podemos fazer um cast de Object para Usuário sem problemas, 
        // pois a implementação do nosso método montaObjeto() criou um objeto do tipo Usuário.
        return (Usuario) usuario;
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
     * Implementação do método abstrato responsável por montar um Usuário a
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
        return usuario;
    }

    /**
     * Carrega a foto em um array de bytes a partir do ID do usuário.<br>
     * Como a foto se trata de um array de bytes, pode ser um processo lento
     * para carregá-la do banco de dados. Por isso foi criado um método separado
     * para buscar a foto.
     *
     * @param idParam
     * @return
     * @throws SQLException
     */
    public byte[] consultaFoto(Integer idParam) throws SQLException {
        byte[] foto = null;
        PreparedStatement ps = conn.prepareStatement("SELECT foto FROM usuario WHERE id = ? ");
        ps.setInt(1, idParam);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            foto = rs.getBytes("foto");
        }
        rs.close();
        ps.close();
        return foto;
    }

}
