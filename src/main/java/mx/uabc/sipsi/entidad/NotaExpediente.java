package mx.uabc.sipsi.entidad;

import java.time.LocalDateTime;

public class NotaExpediente {
    private Long id;
    private Long idPaciente;
    private LocalDateTime fecha;
    private String nombre;
    private String contenido;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Long id) { this.idPaciente = id; }
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
}
