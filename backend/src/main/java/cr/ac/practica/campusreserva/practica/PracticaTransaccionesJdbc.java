package cr.ac.practica.campusreserva.practica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;
import javax.sql.DataSource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("practica-jdbc")
public class PracticaTransaccionesJdbc implements CommandLineRunner {

    private static final String INSERT_EQUIPO = """
            INSERT INTO EQUIPO (CODIGO, NOMBRE, ACTIVO)
            VALUES (?, ?, 1)
            """;

    private final DataSource dataSource;

    public PracticaTransaccionesJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws SQLException {
        String prefijo = "JDBC-" + UUID.randomUUID().toString().substring(0, 8);
        String codigoA = prefijo + "-A";
        String codigoB = prefijo + "-B";
        String codigoRollback = prefijo + "-RB";

        System.out.println("Prefijo de esta práctica: " + prefijo);

        insertarDosEquipos(codigoA, codigoB);
        System.out.println("COMMIT confirmado: " + codigoA + ", " + codigoB);

        try {
            insertarDosEquipos(codigoRollback, codigoRollback);
            throw new IllegalStateException("Se esperaba un error por código duplicado.");
        } catch (SQLException ex) {
            if (ex.getErrorCode() != 1 || ex.getSuppressed().length > 0) {
                throw ex;
            }
            System.out.println("ORA-00001 y ROLLBACK confirmado: " + codigoRollback);
        }
    }

    private void insertarDosEquipos(String codigoPrimero, String codigoSegundo)
            throws SQLException {

        try (Connection conexion = dataSource.getConnection()) {
            boolean autoCommitOriginal = conexion.getAutoCommit();
            conexion.setAutoCommit(false);

            try {
                try (PreparedStatement consulta = conexion.prepareStatement(INSERT_EQUIPO)) {
                    insertarEquipo(consulta, codigoPrimero);
                    insertarEquipo(consulta, codigoSegundo);
                }

                conexion.commit();
            } catch (SQLException ex) {
                try {
                    conexion.rollback();
                    conexion.setAutoCommit(autoCommitOriginal);
                } catch (SQLException errorDeRecuperacion) {
                    ex.addSuppressed(errorDeRecuperacion);
                }
                throw ex;
            }

            conexion.setAutoCommit(autoCommitOriginal);
        }
    }

    private void insertarEquipo(PreparedStatement consulta, String codigo)
            throws SQLException {
        consulta.setString(1, codigo);
        consulta.setString(2, "Equipo de práctica JDBC " + codigo);

        int filas = consulta.executeUpdate();
        System.out.printf("INSERT ejecutado para %s, filas afectadas: %d%n", codigo, filas);
    }
}