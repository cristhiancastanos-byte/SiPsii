package mx.uabc.sipsi.negocio;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mx.uabc.sipsi.entidad.Paciente;
import mx.uabc.sipsi.persistencia.MySQLRepo;
import mx.uabc.sipsi.persistencia.PacienteDAO;

import java.util.Comparator;
import java.util.List;

@ApplicationScoped
public class PacienteServicio {

    @Inject @MySQLRepo
    private PacienteDAO pacienteDAO;

    public List<Paciente> listar(String termino) {
        String t = (termino == null) ? null : termino.trim();
        List<Paciente> out = pacienteDAO.listar(t);
        out.sort(Comparator.comparing(p -> p.getId() == null ? Long.MAX_VALUE : p.getId()));
        return out;
    }
}