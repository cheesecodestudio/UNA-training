package cr.ac.practica.campusreserva.reserva;

import java.time.LocalDateTime;

import cr.ac.practica.campusreserva.Enums;
import cr.ac.practica.campusreserva.equipo.Equipo;
import cr.ac.practica.campusreserva.usuario.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "RESERVA")
public class Reserva {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "usuario_id", nullable = false) 
    Usuario usuario;
    
    @ManyToOne (fetch = FetchType.LAZY, optional = false)
    @JoinColumn (name = "equipo_id", nullable = false) 
    Equipo equipo;
    
    LocalDateTime inicio;
    LocalDateTime fin;

    @Enumerated (EnumType.STRING)
    Enums.EstadoReserva estado;

    protected Reserva() {
    }

    public Reserva(Usuario usuario, Equipo equipo, LocalDateTime inicio, LocalDateTime fin, Enums.EstadoReserva estado) {
        this.usuario = usuario;
        this.equipo = equipo;
        this.inicio = inicio;
        this.fin = fin;
        this.estado = estado;
    }

    //getter and setters
    public Long getId() {
        return id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario; 
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo; 
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio; 
    }

    
    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin; 
    }

    public Enums.EstadoReserva getEstado() {
        return estado;
    }

    public void setEstado(Enums.EstadoReserva estado) {
        this.estado = estado;
    }

    public boolean IsCancelada() {
        return estado.equals(Enums.EstadoReserva.CANCELADA);
    }
}
