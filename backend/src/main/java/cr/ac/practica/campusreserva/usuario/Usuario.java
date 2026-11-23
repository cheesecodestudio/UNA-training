package cr.ac.practica.campusreserva.usuario;

import cr.ac.practica.campusreserva.Enums;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "USUARIO")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String correo;
    @Enumerated(EnumType.STRING)
    private Enums.TipoUsuario tipo;
    private Integer activo;

    protected Usuario() {
    }

    public Usuario(String nombre, String correo, Enums.TipoUsuario tipo, Integer activo) {
        this.nombre = nombre;
        this.correo = correo;
        this.tipo = tipo;
        this.activo = activo;
    }

    public Usuario(Long id) {
        this.id = id;
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Enums.TipoUsuario getTipo() {
        return tipo;
    }

    public void setTipo(Enums.TipoUsuario tipo) {
        this.tipo = tipo;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    public boolean isActivo() {
        return activo == 1;
    }
}
