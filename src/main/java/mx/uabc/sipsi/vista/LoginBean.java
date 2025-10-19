package mx.uabc.sipsi.vista;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.faces.context.FacesContext;
import mx.uabc.sipsi.negocio.AuthServicio;

@Named
@RequestScoped  // Vive solo durante las peticiones donde se usa
public class LoginBean {
    @Inject AuthServicio authServicio;

    private String usuario;
    private String password;
    private boolean mostrarError;

    public String login() {
        var res = authServicio.autenticar(usuario, password);
        if (res.exito()) {
            var ec  = FacesContext.getCurrentInstance().getExternalContext();
            var ses = (jakarta.servlet.http.HttpSession) ec.getSession(true); // crea si no existe
            ses.setAttribute("usuario", res.usuario());                       // **misma llave**
            System.out.println("[LOGIN] OK -> sessionId=" + ses.getId());
            return "/pages/principal.xhtml?faces-redirect=true";
        } else {
            mostrarError = true;
            System.out.println("[LOGIN] ERROR -> " + res.mensaje());
            return null; // permanecer en login.xhtml
        }
    }

    public void cerrarError(){ mostrarError = false; }

    // getters/setters
    public String getUsuario(){ return usuario; }
    public void setUsuario(String u){ this.usuario = u; }
    public String getPassword(){ return password; }
    public void setPassword(String p){ this.password = p; }
    public boolean isMostrarError(){ return mostrarError; }
}
