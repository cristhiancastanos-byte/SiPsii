package mx.uabc.sipsi.vista;

import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import mx.uabc.sipsi.negocio.AuthServicio;
import mx.uabc.sipsi.seguridad.SesionBean;

@Named
@ViewScoped
public class LoginBean implements Serializable {

    private String usuario;
    private String password;
    private boolean cargando;

    private boolean mostrarError;

    @Inject
    AuthServicio authServicio;

    @Inject
    SesionBean sesion;

    public void login() throws IOException {
        cargando = true;
        try {
            var res = authServicio.iniciarSesion(usuario, password);
            if (!res.isOk()) {
                mostrarError = true;
                return;
            }
            sesion.setUsuarioActual(res.getUsuario());
            var ec = jakarta.faces.context.FacesContext.getCurrentInstance().getExternalContext();
            ec.redirect(ec.getRequestContextPath() + "/pages/home.xhtml?faces-redirect=true");
        } finally {
            cargando = false;
        }
    }

    public void cerrarError() {
        mostrarError = false;
    }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public boolean isCargando() { return cargando; }
    public boolean isMostrarError() { return mostrarError; }
}