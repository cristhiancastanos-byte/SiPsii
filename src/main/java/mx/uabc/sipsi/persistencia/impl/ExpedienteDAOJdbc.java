package mx.uabc.sipsi.persistencia.impl;

import jakarta.enterprise.context.ApplicationScoped;
import mx.uabc.sipsi.entidad.ArchivoExpediente;
import mx.uabc.sipsi.entidad.NotaExpediente;
import mx.uabc.sipsi.persistencia.ExpedienteDAO;
import mx.uabc.sipsi.persistencia.MySQLRepo;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@MySQLRepo
public class ExpedienteDAOJdbc implements ExpedienteDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/sipsi?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "root";

    @Override
    public List<NotaExpediente> consultarNotasPorPaciente(Long idPaciente) throws Exception {
        String sql = "SELECT id, id_paciente, creado_en, titulo, contenido " +
                "FROM nota_expediente WHERE id_paciente=? " +
                "ORDER BY creado_en DESC, id DESC";
        List<NotaExpediente> out = new ArrayList<>();
        try (Connection cn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    NotaExpediente n = new NotaExpediente();
                    n.setId(rs.getLong("id"));
                    n.setIdPaciente(rs.getLong("id_paciente"));
                    Timestamp ts = rs.getTimestamp("creado_en");
                    n.setFecha(ts != null ? ts.toLocalDateTime() : LocalDateTime.now());
                    n.setNombre(rs.getString("titulo"));
                    n.setContenido(rs.getString("contenido"));
                    out.add(n);
                }
            }
        }
        return out;
    }

    @Override
    public List<ArchivoExpediente> consultarArchivosPorPaciente(Long idPaciente) throws Exception {
        String sql = "SELECT id, id_paciente, creado_en, nombre, ruta " +
                "FROM archivo_expediente WHERE id_paciente=? " +
                "ORDER BY creado_en DESC, id DESC";
        List<ArchivoExpediente> out = new ArrayList<>();
        try (Connection cn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ArchivoExpediente a = new ArchivoExpediente();
                    a.setId(rs.getLong("id"));
                    a.setIdPaciente(rs.getLong("id_paciente"));
                    Timestamp ts = rs.getTimestamp("creado_en");
                    a.setFecha(ts != null ? ts.toLocalDateTime() : LocalDateTime.now());
                    a.setNombre(rs.getString("nombre"));
                    a.setRuta(rs.getString("ruta"));
                    out.add(a);
                }
            }
        }
        return out;
    }
}