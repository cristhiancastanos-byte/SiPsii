package mx.uabc.sipsi.negocio;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mx.uabc.sipsi.entidad.Paciente;
import mx.uabc.sipsi.persistencia.MySQLRepo;
import mx.uabc.sipsi.persistencia.PacienteDAO;

import java.util.List;
import java.util.regex.Pattern;

@ApplicationScoped
public class PacienteServicio {

    @Inject
    @MySQLRepo
    private PacienteDAO pacienteDAO;

    public List<Paciente> listar(String termino) {
        return pacienteDAO.listar(termino == null ? null : termino.trim());
    }

    public Resultado registrar(String nombreCompleto, Integer edad, String generoStr, String correo) {
        try {
            if (nombreCompleto == null || nombreCompleto.trim().isEmpty()
                    || edad == null || generoStr == null || generoStr.isBlank()) {
                return Resultado.fail("Campo Obligatorio");
            }

            nombreCompleto = nombreCompleto.trim();
            if (correo != null) correo = correo.trim();

            Character genero = generoStr.trim().charAt(0);

            if (edad < 0 || edad > 120) {
                return Resultado.fail("Edad fuera de rango: Debe estar entre 0 y 120");
            }

            if (correo != null && !correo.isBlank() && !EMAIL_REGEX.matcher(correo).matches()) {
                return Resultado.fail("Formato de correo inválido");
            }

            if (pacienteDAO.existePorNombreYEdad(nombreCompleto, edad)) {
                return Resultado.fail("Ya existe ese paciente registrado");
            }

            if (correo != null && !correo.isBlank() && pacienteDAO.existeCorreo(correo)) {
                return Resultado.fail("El correo ingresado ya fue registrado con otro paciente");
            }

            Paciente p = new Paciente();
            p.setNombreCompleto(nombreCompleto);
            p.setEdad(edad);
            p.setGenero(genero);
            p.setCorreo((correo == null || correo.isBlank()) ? null : correo);
            p.setActivo(true);

            pacienteDAO.insertar(p);
            return Resultado.ok();

        } catch (Exception e) {
            e.printStackTrace();
            return Resultado.fail("Error al registrar paciente");
        }
    }

    public Resultado editar(Long id, String nombreCompleto, Integer edad, String generoStr, String correo) {
        try {
            if (id == null) {
                return Resultado.fail("Paciente no encontrado");
            }
            if (nombreCompleto == null || nombreCompleto.trim().isEmpty()
                    || edad == null || generoStr == null || generoStr.isBlank()) {
                return Resultado.fail("Campo Obligatorio");
            }

            nombreCompleto = nombreCompleto.trim();
            if (correo != null) correo = correo.trim();
            Character genero = generoStr.trim().charAt(0);

            if (edad < 0 || edad > 120) {
                return Resultado.fail("Edad fuera de rango: Debe estar entre 0 y 120");
            }

            if (correo != null && !correo.isBlank() && !EMAIL_REGEX.matcher(correo).matches()) {
                return Resultado.fail("Formato de correo inválido");
            }

            if (pacienteDAO.existePorNombreYEdadExceptoId(nombreCompleto, edad, id)) {
                return Resultado.fail("Ya existe ese paciente registrado");
            }
            if (correo != null && !correo.isBlank() && pacienteDAO.existeCorreoExceptoId(correo, id)) {
                return Resultado.fail("El correo ingresado ya fue registrado con otro paciente");
            }

            Paciente p = pacienteDAO.obtenerPorId(id);
            if (p == null) {
                return Resultado.fail("Paciente no encontrado");
            }

            p.setNombreCompleto(nombreCompleto);
            p.setEdad(edad);
            p.setGenero(genero);
            p.setCorreo((correo == null || correo.isBlank()) ? null : correo);

            boolean ok = pacienteDAO.actualizar(p);
            if (!ok) {
                return Resultado.fail("No fue posible guardar los cambios");
            }

            return Resultado.ok();

        } catch (Exception e) {
            e.printStackTrace();
            return Resultado.fail("Error al editar paciente");
        }
    }

    public static class Resultado {
        private final boolean exito;
        private final String mensaje;

        private Resultado(boolean exito, String mensaje) {
            this.exito = exito;
            this.mensaje = mensaje;
        }

        public static Resultado ok() {
            return new Resultado(true, null);
        }

        public static Resultado fail(String m) {
            return new Resultado(false, m);
        }

        public boolean isExito() {
            return exito;
        }

        public String getMensaje() {
            return mensaje;
        }
    }

    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
}
