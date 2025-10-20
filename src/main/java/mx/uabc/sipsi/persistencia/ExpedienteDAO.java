package mx.uabc.sipsi.persistencia;

import mx.uabc.sipsi.entidad.ArchivoExpediente;
import mx.uabc.sipsi.entidad.NotaExpediente;

import java.util.List;

public interface ExpedienteDAO {
    List<NotaExpediente> consultarNotasPorPaciente(Long idPaciente) throws Exception;

    List<ArchivoExpediente> consultarArchivosPorPaciente(Long idPaciente) throws Exception;
}
