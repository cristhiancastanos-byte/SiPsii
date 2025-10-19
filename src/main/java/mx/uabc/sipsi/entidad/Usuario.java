package mx.uabc.sipsi.entidad;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="usuario", length = 100, nullable = false, unique = true)
    private String usuario;

    @Column(name="correo", length = 150, unique = true)
    private String correo;

    @Column(name="password_hash", length = 100, nullable = false)
    private String passwordHash;

    @Column(name="activo", nullable = false)
    private boolean activo = true;

    public Long getId() { return id; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
