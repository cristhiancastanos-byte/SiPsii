package mx.uabc.sipsi.persistencia;

import mx.uabc.sipsi.entidad.Paciente;
import java.util.List;

public interface PacienteDAO {
    List<Paciente> listar(String termino);
    boolean existePorNombreYEdad(String nombreCompleto, int edad) throws Exception;
    boolean existeCorreo(String correo) throws Exception;
    long insertar(Paciente p) throws Exception;
    Paciente obtenerPorId(Long id) throws Exception;
    boolean existePorNombreYEdadExceptoId(String nombreCompleto, int edad, long id) throws Exception;
    boolean existeCorreoExceptoId(String correo, long id) throws Exception;
    boolean actualizar(Paciente p) throws Exception;
}
