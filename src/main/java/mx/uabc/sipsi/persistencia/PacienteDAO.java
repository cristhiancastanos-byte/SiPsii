package mx.uabc.sipsi.persistencia;

import mx.uabc.sipsi.entidad.Paciente;
import java.util.List;

public interface PacienteDAO {
    List<Paciente> listar(String terminoNormalizado);
}