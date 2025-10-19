package mx.uabc.sipsi.persistencia;

import mx.uabc.sipsi.entidad.Paciente;
import java.util.List;

public interface PacienteDAO {
    List<Paciente> listar(String termino);
    boolean existePorNombreYEdad(String nombreCompleto, int edad) throws Exception;
    boolean existeCorreo(String correo) throws Exception;
    long insertar(Paciente p) throws Exception;
}