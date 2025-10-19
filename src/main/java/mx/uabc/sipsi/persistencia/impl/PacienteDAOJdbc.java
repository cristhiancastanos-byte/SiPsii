package mx.uabc.sipsi.persistencia.impl;

import jakarta.enterprise.context.ApplicationScoped;
import mx.uabc.sipsi.entidad.Paciente;
import mx.uabc.sipsi.persistencia.MySQLRepo;
import mx.uabc.sipsi.persistencia.PacienteDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
@MySQLRepo
public class PacienteDAOJdbc implements PacienteDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/sipsi?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "root";

    @Override
    public List<Paciente> listar(String termino) {
        List<Paciente> out = new ArrayList<>();
        String base = "SELECT id, nombre_completo, edad, genero, correo FROM paciente WHERE activo = 1";
        String sql = (termino == null || termino.isBlank())
                ? base + " ORDER BY id DESC"
                : base + " AND (LOWER(nombre_completo) LIKE ? OR LOWER(correo) LIKE ?) ORDER BY id DESC";

        try (Connection cn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = cn.prepareStatement(sql)) {

            if (termino != null && !termino.isBlank()) {
                String t = "%" + termino.toLowerCase().trim() + "%";
                ps.setString(1, t);
                ps.setString(2, t);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Paciente p = new Paciente();
                    p.setId(rs.getLong("id"));
                    p.setNombreCompleto(rs.getString("nombre_completo"));
                    p.setEdad(rs.getInt("edad"));
                    String gen = rs.getString("genero");
                    p.setGenero((gen != null && !gen.isEmpty()) ? gen.charAt(0) : null);
                    p.setCorreo(rs.getString("correo"));
                    out.add(p);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando pacientes", e);
        }
        return out;
    }

    @Override
    public boolean existePorNombreYEdad(String nombreCompleto, int edad) throws Exception {
        String sql = "SELECT 1 FROM paciente WHERE nombre_completo = ? AND edad = ? LIMIT 1";
        try (Connection cn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombreCompleto);
            ps.setInt(2, edad);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean existeCorreo(String correo) throws Exception {
        if (correo == null || correo.isBlank()) return false;
        String sql = "SELECT 1 FROM paciente WHERE correo = ? LIMIT 1";
        try (Connection cn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public long insertar(Paciente p) throws Exception {
        String sql = "INSERT INTO paciente (nombre_completo, edad, genero, correo, activo) VALUES (?,?,?,?,1)";
        try (Connection cn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getNombreCompleto());
            ps.setInt(2, p.getEdad());
            ps.setString(3, (p.getGenero() == null) ? null : String.valueOf(p.getGenero()));

            if (p.getCorreo() == null || p.getCorreo().isBlank()) {
                ps.setNull(4, Types.VARCHAR);
            } else {
                ps.setString(4, p.getCorreo());
            }

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getLong(1) : 0L;
            }
        }
    }

    @Override
    public Paciente obtenerPorId(Long id) throws Exception {
        String sql = "SELECT id, nombre_completo, edad, genero, correo, activo FROM paciente WHERE id = ?";
        try (Connection cn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Paciente p = new Paciente();
                    p.setId(rs.getLong("id"));
                    p.setNombreCompleto(rs.getString("nombre_completo"));
                    p.setEdad(rs.getInt("edad"));
                    String gen = rs.getString("genero");
                    p.setGenero((gen != null && !gen.isEmpty()) ? gen.charAt(0) : null);
                    p.setCorreo(rs.getString("correo"));
                    try {
                        p.setActivo(rs.getBoolean("activo"));
                    } catch (Throwable ignore) {}
                    return p;
                }
            }
        }
        return null;
    }

    @Override
    public boolean existePorNombreYEdadExceptoId(String nombreCompleto, int edad, long id) throws Exception {
        String sql = "SELECT 1 FROM paciente WHERE nombre_completo = ? AND edad = ? AND id <> ? LIMIT 1";
        try (Connection cn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, nombreCompleto);
            ps.setInt(2, edad);
            ps.setLong(3, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean existeCorreoExceptoId(String correo, long id) throws Exception {
        if (correo == null || correo.isBlank()) return false;
        String sql = "SELECT 1 FROM paciente WHERE correo = ? AND id <> ? LIMIT 1";
        try (Connection cn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, correo);
            ps.setLong(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean actualizar(Paciente p) throws Exception {
        String sql = "UPDATE paciente SET nombre_completo = ?, edad = ?, genero = ?, correo = ? WHERE id = ?";
        try (Connection cn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setString(1, p.getNombreCompleto());
            ps.setInt(2, p.getEdad());
            ps.setString(3, (p.getGenero() == null) ? null : String.valueOf(p.getGenero()));

            if (p.getCorreo() == null || p.getCorreo().isBlank()) {
                ps.setNull(4, Types.VARCHAR);
            } else {
                ps.setString(4, p.getCorreo());
            }

            ps.setLong(5, p.getId());

            return ps.executeUpdate() == 1;
        }
    }
}
