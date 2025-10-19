package mx.uabc.sipsi.negocio;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import mx.uabc.sipsi.entidad.Usuario;
import mx.uabc.sipsi.persistencia.UsuarioDAO;

@ApplicationScoped
public class AuthServicio {

    @Inject
    UsuarioDAO usuarioDAO;

    @Inject
    EntityManager em;

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
        public String getMensaje() { return mensaje; }
        public Usuario getUsuario() { return usuario; }
    }

}
