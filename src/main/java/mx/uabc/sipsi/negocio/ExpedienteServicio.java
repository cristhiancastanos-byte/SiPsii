package mx.uabc.sipsi.negocio;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mx.uabc.sipsi.entidad.ArchivoExpediente;
import mx.uabc.sipsi.entidad.NotaExpediente;
import mx.uabc.sipsi.entidad.Paciente;
import mx.uabc.sipsi.persistencia.ExpedienteDAO;
import mx.uabc.sipsi.persistencia.MySQLRepo;
import mx.uabc.sipsi.persistencia.PacienteDAO;

import java.util.List;

@ApplicationScoped  // Esta es la instancia principla: Se crea una para toda la aplicación
public class ExpedienteServicio {
    @Inject
    @MySQLRepo
    private ExpedienteDAO expedienteDAO;

    @Inject
    @MySQLRepo
    private PacienteDAO pacienteDAO;

    // Creado como failsafe
    public Paciente obtenerPaciente(Long id) {
        try {
            return pacienteDAO.obtenerPorId(id);
        } catch (Exception e) {
            throw new RuntimeException("Error obteniendo paciente", e);
        }
    }

    public List<NotaExpediente> notas(Long idPaciente) {
        try {
            return expedienteDAO.consultarNotasPorPaciente(idPaciente);
        } catch (Exception e) {
            throw new RuntimeException("Error listando notas", e);
        }
    }

    public List<ArchivoExpediente> archivos(Long idPaciente) {
        try {
            return expedienteDAO.consultarArchivosPorPaciente(idPaciente);
        } catch (Exception e) {
            throw new RuntimeException("Error listando archivos", e);
        }
    }
}
