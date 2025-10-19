package mx.uabc.sipsi.vista.filtro;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName="SesionFiltro", urlPatterns={"/pages/*"}) // Filtros para página principal
public class SesionFiltro implements Filter {
    @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Para comunicar con el servidor Tomcat
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        // Permitir recursos estáticos de JSF y /resources/*
        if (uri.contains("/javax.faces.resource/") || uri.contains("/resources/")) {
            chain.doFilter(request, response);
            return;
        }

        // No-cache: Si el usuario intenta regresar, no podrá porque no hay cache y no tiene sesión activa
        res.setHeader("Cache-Control","no-cache, no-store, must-revalidate");
        res.setHeader("Pragma","no-cache");
        res.setDateHeader("Expires", 0);    // Expirar la sesión IMEDIATAMENTE

        HttpSession ses = req.getSession(false);    // Ya no es válida la sesión en el navegador
        boolean logged = ((ses != null) && (ses.getAttribute("usuario") != null));  // Si existe la sesión (que no debería), está logeado
        // los parentesis son redundantes pero me ayudan con claridad

        if (!logged) {
            res.sendRedirect(req.getContextPath() + "/login.xhtml?expired=1");  // Regresa al login, específicamente diciendo que se expiró la sesión
            return;
        }
        chain.doFilter(request, response);
    }
}
