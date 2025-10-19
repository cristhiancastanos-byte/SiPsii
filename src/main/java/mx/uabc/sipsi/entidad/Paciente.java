package mx.uabc.sipsi.entidad;

import java.util.Objects;

public class Paciente {
    private Long id;
    private String nombreCompleto;
    private Integer edad;
    private String genero;
    private String correo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    // Derivados para la UI
    public Integer getExpedienteNumero(){ return id == null ? null : id.intValue(); }
    public String getExpedienteConCeros(){ return id == null ? "" : String.format("%03d", id); }

    @Override public boolean equals(Object o){ return o instanceof Paciente p && Objects.equals(id,p.id); }
    @Override public int hashCode(){ return Objects.hash(id); }
}