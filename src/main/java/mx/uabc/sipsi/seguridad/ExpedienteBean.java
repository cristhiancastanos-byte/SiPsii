package mx.uabc.sipsi.seguridad;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.uabc.sipsi.entidad.ArchivoExpediente;
import mx.uabc.sipsi.entidad.NotaExpediente;
import mx.uabc.sipsi.entidad.Paciente;
import mx.uabc.sipsi.negocio.ExpedienteServicio;

import java.io.Serializable;
import java.util.List;

@Named("expedienteBean")
@ViewScoped
public class ExpedienteBean implements Serializable {

    @Inject
    private ExpedienteServicio expedienteServicio;

    private Long pacienteId;
    private Paciente paciente;
    private List<NotaExpediente> notas;
    private List<ArchivoExpediente> archivos;

    @PostConstruct
    public void init() {
        String pid = FacesContext.getCurrentInstance().getExternalContext()
                .getRequestParameterMap().get("pacienteId");
        if (pid != null && !pid.isBlank()) {
            pacienteId = Long.valueOf(pid);
            paciente = expedienteServicio.obtenerPaciente(pacienteId);
            refrescarListas();
        }
    }

    public void refrescarListas() {
        notas = expedienteServicio.notas(pacienteId);
        archivos = expedienteServicio.archivos(pacienteId);
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public List<NotaExpediente> getNotas() {
        return notas;
    }

    public List<ArchivoExpediente> getArchivos() {
        return archivos;
    }
}
