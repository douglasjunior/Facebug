package br.grupointegrado.facebug.filter;

import br.grupointegrado.facebug.model.Usuario;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Filtro utilizado para checar se o usuário está logado. Se estiver logado
 * então permite que acesse o sistema, caso contrário encaminhe para a página de
 * Login. <br />
 * Obs: Não esquecer de permitir que os arquivos CSS e JavaScript sejam
 * acessados mesmo sem Login.
 *
 * @author Douglas
 */
public class LoginFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        if (isArquivoRecurso(req) || isPaginaLogin(req) || estaLogado(req)) {
            System.out.println("Liberou: " + req.getServletPath());

            // verifica se o usuário está tentando acessar a página de Login depois de estar logado.
            if (isPaginaLogin(req) && estaLogado(req) && !acaoSair(req)) {
                resp.sendRedirect("/Facebug/Timeline");
            } else {
                chain.doFilter(request, response);
            }

        } else {
            salvarPaginaRedirecionada(req);
            System.out.println("Bloqueou: " + req.getServletPath());
            resp.sendRedirect("/Facebug/Login");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    /**
     * Verifica se o usuário está logado
     *
     * @param req
     * @return
     */
    private boolean estaLogado(HttpServletRequest req) {
        HttpSession sessao = req.getSession();
        Object usuarioLogado = sessao.getAttribute("usuario_logado");
        boolean estaLogado = usuarioLogado != null && usuarioLogado instanceof Usuario;
        return estaLogado;
    }

    /**
     * Verifica se a requisição é para a página ou servlet de Login
     *
     * @param req
     * @return
     */
    private boolean isPaginaLogin(HttpServletRequest req) {
        String servletPath = req.getServletPath();
        boolean isPaginaLogin = servletPath.equals("/Login");
        return isPaginaLogin;
    }

    /**
     * Verifica se a requisição é um arquivo de recurso como CSS, JS ou Imagem
     *
     * @param req
     * @return
     */
    private boolean isArquivoRecurso(HttpServletRequest req) {
        /*
         * /imagens/perfil-padrao.jpg 
         * /css/bootstrap.min.css
         * /js/jquery-1.11.2.min.js
         *
         */
        String servletPath = req.getServletPath();
        boolean isArquivoRecurso = servletPath.startsWith("/imagens/")
                || servletPath.startsWith("/css/")
                || servletPath.startsWith("/js/")
                || servletPath.startsWith("/img/")
                || servletPath.equals("/Imagem");
        return isArquivoRecurso;
    }

    /**
     * Salva a página que o usuário tentou acessar para após efetuar o Login<br>
     * então redirecioná-lo corretamente.
     *
     * @param req
     */
    private void salvarPaginaRedirecionada(HttpServletRequest req) {
        String servletPath = req.getServletPath();
        req.getSession().setAttribute("pagina_redireciona", servletPath);
    }

    /**
     * Verifica se o usuário clicou no botão de SAIR.
     *
     * @param req
     * @return
     */
    private boolean acaoSair(HttpServletRequest req) {
        /*
         Para verificar se o usuário clicou na ação Sair, não basta verificar o nome da ação, 
         tem que verificar se é o Servlet de Login.
         */
        String acaoParam = req.getParameter("acao");
        return isPaginaLogin(req) && "sair".equals(acaoParam);
    }

}
