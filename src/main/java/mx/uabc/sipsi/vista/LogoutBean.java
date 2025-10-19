package mx.uabc.sipsi.vista;

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.uabc.sipsi.negocio.AuthServicio;

@Named          // Usado para indicar nombre del bean como LogoutBean (la clase)
@RequestScoped  // Vive solo durante la petición de cerrar sesión
public class LogoutBean {

    @Inject
    private AuthServicio authServicio;

    public String cerrarSesion() {
        authServicio.logout();
        // Mostrar mensaje de sesión cerrada...
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sesión cerrada correctamente", null));
        // ...y que siga existiendo después de redirección
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        return "/login.xhtml?faces-redirect=true";
    }
}