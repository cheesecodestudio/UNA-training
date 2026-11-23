package cr.ac.practica.campusreserva.reporte;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

import cr.ac.practica.campusreserva.reporte.dto.UsoEquipoResponse;

@Repository
public class ReporteJdbcDao {
    private static final String SQL_USO_EQUIPOS = """
            SELECT e.id AS id_equipo,
                   e.nombre AS nombre_equipo,
                   COUNT(r.id) AS total_reservas
            FROM equipo e
            LEFT JOIN reserva r
                ON r.equipo_id = e.id
               AND r.estado = 'ACTIVA'
               AND r.inicio >= ?
               AND r.inicio < ?
            GROUP BY e.id, e.nombre
            ORDER BY e.id
            """;

    private final DataSource dataSource;

    public ReporteJdbcDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<UsoEquipoResponse> equiposUso(LocalDateTime desde, LocalDateTime hasta) {
        List<UsoEquipoResponse> resultados = new ArrayList<>();

        try (Connection conexion = dataSource.getConnection();
                PreparedStatement ps = conexion.prepareStatement(SQL_USO_EQUIPOS)) {
            ps.setTimestamp(1, Timestamp.valueOf(desde));
            ps.setTimestamp(2, Timestamp.valueOf(hasta));
            try (ResultSet filas = ps.executeQuery()) {
                while (filas.next()) {
                    resultados.add(new UsoEquipoResponse(
                            filas.getLong("id_equipo"),
                            filas.getString("nombre_equipo"),
                            filas.getLong("total_reservas")));
                }
            }
        } catch (SQLException ex) {
            throw new IllegalStateException("No se pudo consultar el uso de equipos.", ex);
        }

        return resultados;
    }
}
