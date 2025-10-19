package mx.uabc.sipsi.vista;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import mx.uabc.sipsi.entidad.Paciente;
import mx.uabc.sipsi.negocio.PacienteServicio;

import java.io.Serializable;
import java.util.List;

@Named("pacientesBean")
@ViewScoped
public class PacientesBean implements Serializable {

    private String termino;
    private List<Paciente> pacientes;

    private boolean mostrarDialogo;
    private Paciente seleccionado;

    @Inject
    private PacienteServicio pacienteServicio;

    @PostConstruct
    public void init() { refrescar(); }

    public void buscar() { refrescar(); }

    private void refrescar() {
        pacientes = pacienteServicio.listar(termino);
    }

    public void abrirDetalles(Paciente p){ this.seleccionado = p; this.mostrarDialogo = true; }
    public void cerrarDetalles(){ this.mostrarDialogo = false; }

    public String getTermino() { return termino; }
    public void setTermino(String termino) { this.termino = termino; }
    public List<Paciente> getPacientes() { return pacientes; }
    public boolean isMostrarDialogo() { return mostrarDialogo; }
    public Paciente getSeleccionado() { return seleccionado; }
}