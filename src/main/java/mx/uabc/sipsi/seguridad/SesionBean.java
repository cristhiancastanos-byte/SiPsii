package mx.uabc.sipsi.seguridad;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import mx.uabc.sipsi.entidad.Usuario;

@Named
@SessionScoped
public class SesionBean implements Serializable {

    private Usuario usuarioActual;

    public boolean isAutenticado() { return usuarioActual != null; }
    public Usuario getUsuarioActual() { return usuarioActual; }
    public void setUsuarioActual(Usuario u) { this.usuarioActual = u; }
    public void cerrar() { usuarioActual = null; }
}
