package mx.uabc.sipsi.vista;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;
import mx.uabc.sipsi.entidad.Paciente;
import mx.uabc.sipsi.negocio.PacienteServicio;

@Named("pacientesBean")
@ViewScoped
public class PacientesBean implements Serializable {

    private String termino;
    private List<Paciente> pacientes;

    private boolean mostrarDialogo;
    private Paciente seleccionado;

    private boolean mostrarNuevoPaciente;
    private String nombreCompleto;
    private Integer edad;
    private String genero;
    private String correo;

    private boolean errNombre;
    private boolean errEdad;
    private boolean errGenero;

    private boolean mostrarAlerta;
    private String mensajeAlerta;

    @Inject
    private PacienteServicio pacienteServicio;

    @PostConstruct
    public void init() {
        refrescar();
    }

    public void buscar() {
        refrescar();
    }

    private void refrescar() {
        pacientes = pacienteServicio.listar(termino);
    }

    public void abrirDetalles(Paciente p) {
        this.seleccionado = p;
        this.mostrarDialogo = true;
    }

    public void cerrarDetalles() {
        this.mostrarDialogo = false;
    }

    public void abrirNuevoPaciente() {
        limpiarForm();
        this.mostrarNuevoPaciente = true;
    }

    public void cancelarNuevoPaciente() {
        this.mostrarNuevoPaciente = false;
    }

    private void limpiarForm() {
        nombreCompleto = null;
        edad = null;
        genero = null;
        correo = null;
        errNombre = false;
        errEdad = false;
        errGenero = false;
    }

    public void onNombreChange() {
        errNombre = (nombreCompleto == null || nombreCompleto.trim().isEmpty());
    }

    public void onEdadChange() {
        errEdad = (edad == null);
    }

    public void onGeneroChange() {
        errGenero = (genero == null || genero.isBlank());
    }

    public void guardarNuevoPaciente() {
        errNombre = (nombreCompleto == null || nombreCompleto.trim().isEmpty());
        errEdad = (edad == null);
        errGenero = (genero == null || genero.isBlank());
        if (errNombre || errEdad || errGenero) return;

        var res = pacienteServicio.registrar(nombreCompleto, edad, genero, correo);
        if (!res.isExito()) {
            mensajeAlerta = res.getMensaje();
            mostrarAlerta = true;
            return;
        }

        mostrarNuevoPaciente = false;
        limpiarForm();
        refrescar();
        mensajeAlerta = "Paciente registrado correctamente";
        mostrarAlerta = true;
    }

    public void cerrarPop() {
        this.mostrarAlerta = false;
    }

    public String getTermino() {
        return termino;
    }

    public void setTermino(String termino) {
        this.termino = termino;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public boolean isMostrarDialogo() {
        return mostrarDialogo;
    }

    public Paciente getSeleccionado() {
        return seleccionado;
    }

    public boolean isMostrarNuevoPaciente() {
        return mostrarNuevoPaciente;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public boolean isErrNombre() {
        return errNombre;
    }

    public boolean isErrEdad() {
        return errEdad;
    }

    public boolean isErrGenero() {
        return errGenero;
    }

    public boolean isMostrarAlerta() {
        return mostrarAlerta;
    }

    public String getMensajeAlerta() {
        return mensajeAlerta;
    }
}