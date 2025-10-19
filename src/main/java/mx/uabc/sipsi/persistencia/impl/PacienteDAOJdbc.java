package mx.uabc.sipsi.persistencia.impl;

import jakarta.enterprise.context.ApplicationScoped;
import mx.uabc.sipsi.entidad.Paciente;
import mx.uabc.sipsi.persistencia.MySQLRepo;
import mx.uabc.sipsi.persistencia.PacienteDAO;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

@ApplicationScoped
@MySQLRepo
public class PacienteDAOJdbc implements PacienteDAO {

    private static final String URL  =
            "jdbc:mysql://localhost:3306/sipsi?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "root";

    static {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) { throw new RuntimeException("Falta mysql-connector-j", e); }
    }

    @Override
    public List<Paciente> listar(String termino) {
        String base = "SELECT id, nombre_completo, edad, genero, correo " +
                "FROM paciente WHERE activo=1";
        List<String> filtros = new ArrayList<>();
        List<Object> params  = new ArrayList<>();

        if (termino != null && !termino.trim().isEmpty()) {
            filtros.add("(LOWER(nombre_completo) LIKE ? OR LPAD(id,3,'0') LIKE ?)");
            String t = "%" + termino.trim().toLowerCase(Locale.ROOT) + "%";
            params.add(t);
            params.add("%" + termino.trim() + "%");
        }

        String where = filtros.isEmpty() ? "" : (" AND " + filtros.stream().collect(Collectors.joining(" AND ")));
        String sql = base + where + " ORDER BY id ASC";

        List<Paciente> out = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Paciente p = new Paciente();
                    p.setId(rs.getLong("id"));
                    p.setNombreCompleto(rs.getString("nombre_completo"));
                    p.setEdad(rs.getInt("edad"));
                    p.setGenero(rs.getString("genero"));
                    p.setCorreo(rs.getString("correo"));
                    out.add(p);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando pacientes", e);
        }
        return out;
    }
}