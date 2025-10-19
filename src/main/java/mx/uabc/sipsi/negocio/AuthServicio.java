package mx.uabc.sipsi.negocio;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.servlet.http.HttpSession;
import mx.uabc.sipsi.entidad.Usuario;
import mx.uabc.sipsi.persistencia.UsuarioDAO;

@ApplicationScoped
public class AuthServicio {

    @Inject
    UsuarioDAO usuarioDAO;

    @Inject
    EntityManager em;

    // Alias para uso en otros códigos - Lo único que hace es mandar iniciarSesion y hacer que no tengamos que reescribir otros archivos
    public ResultadoLogin autenticar(String usuarioOCorreo, String passwordPlano) {
        return iniciarSesion(usuarioOCorreo, passwordPlano);
    }

    public ResultadoLogin iniciarSesion(String usuarioOCorreo, String passwordPlano) {
        String in = usuarioOCorreo == null ? "" : usuarioOCorreo.trim();
        String pw = passwordPlano == null ? "" : passwordPlano;

        System.out.println("[AUTH] input='" + in + "' pw.len=" + pw.length());

        if (in.isEmpty() || pw.isEmpty()) return ResultadoLogin.error("Credenciales inválidas");

        Usuario u = usuarioDAO.buscarPorUsuarioOCorreo(in);
        System.out.println("[AUTH] usuarioEncontrado=" + (u != null) +
                (u != null ? " user=" + u.getUsuario() + " activo=" + u.isActivo() +
                        " hash.len=" + (u.getPasswordHash()==null?0:u.getPasswordHash().length()) : ""));

        if (u == null || !u.isActivo()) return ResultadoLogin.error("Credenciales inválidas");

        boolean ok = at.favre.lib.crypto.bcrypt.BCrypt.verifyer()
                .verify(pw.toCharArray(), u.getPasswordHash()).verified;

        System.out.println("[AUTH] bcrypt.ok=" + ok);

        if (!ok) return ResultadoLogin.error("Credenciales inválidas");

        return ResultadoLogin.ok(u);
    }

    // Cierra la sesión actual
    public void logout() {
        FacesContext fc = FacesContext.getCurrentInstance();    // Del Jakarta, para realizar conexiones y agarrar el contexto de la petición actual
        ExternalContext ec = fc.getExternalContext();           // Da acceso al control, da la interfaz del usuario para que pueda realizar operaciones

        HttpSession ses = (HttpSession) ec.getSession(false);   // Conecta con el navegador de internet
        if (ses != null) ses.invalidate(); // Si hay sesión, la invalidamos

        ec.getFlash().put("logoutOk", "Sesión cerrada correctamente");  // Mostrar popup "salimos bien :)"
        ec.getFlash().setKeepMessages(true);    // Sigue mostrando el popup después de salir a otra página
    }


    public static final class ResultadoLogin {
        private final boolean ok;
        private final String mensaje;
        private final Usuario usuario;

        private ResultadoLogin(boolean ok, String mensaje, Usuario usuario) {
            this.ok = ok; this.mensaje = mensaje; this.usuario = usuario;
        }

        public static ResultadoLogin ok(Usuario u) { return new ResultadoLogin(true, null, u); }
        public static ResultadoLogin error(String m) { return new ResultadoLogin(false, m, null); }

        public boolean isOk() { return ok; }
        public boolean exito() { return ok; }           // Duplicado para compatibilidad con diferentes llamadas
        public String getMensaje() { return mensaje; }
        public String mensaje() { return mensaje; }     // Duplicado para compatibilidad con diferentes llamadas
        public Usuario getUsuario() { return usuario; }
        public Usuario usuario() { return usuario; }    // Duplicado para compatibilidad con diferentes llamadas
    }

}
