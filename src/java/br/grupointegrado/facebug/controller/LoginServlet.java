package br.grupointegrado.facebug.controller;

import br.grupointegrado.facebug.exception.ValidacaoException;
import br.grupointegrado.facebug.model.Usuario;
import br.grupointegrado.facebug.model.UsuarioDAO;
import br.grupointegrado.facebug.util.CriptografiaUtil;
import br.grupointegrado.facebug.util.ValidacaoUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
         Verifica qual é a ação que foi recebida via POST
         */
        String acaoParam = req.getParameter("acao");

        if ("login".equals(acaoParam)) {
            efetuarLogin(req, resp);
        } else if ("cadastro".equals(acaoParam)) {
            efetuarCadastro(req, resp);
        } else {
            /*
             Se nenhuma ação foi recebida, então só encaminha para a página principal
             */
            resp.sendRedirect("/Facebug/");
        }
    }

    private void efetuarLogin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String senha = req.getParameter("senha");
        Integer tentativa = (Integer) req.getSession().getAttribute("tentativa_login");
        tentativa = tentativa != null ? tentativa : 0;

        try {
            // valida se foi informado um e-mail válido
            if (!ValidacaoUtil.validaEmail(email)) {
                throw new ValidacaoException("Informe um enedreço de e-mail válido.");
            }
            Connection conn = (Connection) req.getAttribute("conexao");
            Usuario usuario = new UsuarioDAO(conn).consultaEmailSenha(email, CriptografiaUtil.criptografarMD5(senha));
            // verifica se o usuário não excedeu o número de tentativas de login
            if (tentativa < 3) {
                if (usuario != null) {
                    HttpSession sessao = req.getSession();
                    // se o Login passar, então zera as tentativas
                    sessao.removeAttribute("tentativa_login");
                    sessao.setAttribute("usuario_logado", usuario);
                    resp.sendRedirect(redirecionaPaginaLogar(req));
                } else {
                    // se o Login falhar, incrementa uma tentativa
                    tentativa++;
                    req.getSession().setAttribute("tentativa_login", tentativa);
                    req.setAttribute("mensagem_erro", "E-mail ou senha incorretos, tente novamente.");
                    req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
                }
            } else {
                req.setAttribute("mensagem_erro", "Sua sessão expirou. Você cometeu três acessos indevidos!");
                req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
            }
        } catch (ValidacaoException ex) {
            ex.printStackTrace();
            req.setAttribute("mensagem_erro", ex.getMessage());
            req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
        } catch (SQLException ex) {
            ex.printStackTrace();
            req.setAttribute("mensagem_erro", "Não foi possível efetuar o login, tente novamente mais tarde.");
            req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
        }
    }

    private void efetuarCadastro(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /*
         * Para evitar duplicidade na subimissão de uma requisição POST,
         * precisamos respeitar o padrão PRG: http://en.wikipedia.org/wiki/Post/Redirect/Get
         * Este padrão diz que, a grosso modo:
         * Quando uma requisição é bem sucediada deve-se fazer um Redirect para a próxima página.
         *
         * Sendo assim, quando a postagem for gravada com sucesso, temos que
         * efetuar um sendRedirec() para concluir o processo.
         */
        Usuario usuario = null;
        try {
            usuario = UsuarioDAO.getUsuarioParameters(req);
            Connection conn = (Connection) req.getAttribute("conexao");
            new UsuarioDAO(conn).inserir(usuario);
            req.getSession().setAttribute("mensagem_sucesso", "Usuário cadastrado com sucesso, efetue Login para continuar.");
            resp.sendRedirect("/Facebug/Login");
        } catch (ValidacaoException ex) {
            ex.printStackTrace();
            req.setAttribute("mensagem_erro", ex.getMessage());
            req.setAttribute("usuario_cadastro", usuario);
            req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
        } catch (SQLException ex) {
            ex.printStackTrace();
            req.setAttribute("mensagem_erro", "Não foi possível efetuar o cadastro, tente novamente mais tarde.");
            req.setAttribute("usuario_cadastro", usuario);
            req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
        }
    }

    /**
     * Recupera a página que po usuário tentou acessar antes de efetuar o Login.
     *
     * @param request
     * @return
     */
    private String redirecionaPaginaLogar(HttpServletRequest request) {
        String pagina = (String) request.getSession().getAttribute("pagina_redireciona");
        request.getSession().removeAttribute("pagina_redireciona");
        if (pagina != null) {
            return "/Facebug" + pagina;
        } else {
            // não precisa especificar /Timeline, pois ela já está definida como página inicial no web.xml
            return ("/Facebug/");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
         Verifica qual é a ação que foi recebida via GET
         */
        String acaoParam = req.getParameter("acao");
        if ("sair".equals(acaoParam)) {
            /*
             Para efetuar o Logout, basta invalidar a sessão
             */
            HttpSession sessao = req.getSession();
            sessao.invalidate();
        }
        /*
         Independende de qualquer ação recebida, sempre encaminha para a página principal
         */
        req.getRequestDispatcher("/WEB-INF/login.jsp").forward(req, resp);
    }
}
