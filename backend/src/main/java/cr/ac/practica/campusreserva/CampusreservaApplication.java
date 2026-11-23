package cr.ac.practica.campusreserva;

import java.time.Clock;
import java.time.ZoneId;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CampusreservaApplication {
    @Bean
    Clock relojReservas() {
        return Clock.system(ZoneId.of("America/Costa_Rica"));
    }

    public static void main(String[] args) {
        SpringApplication.run(CampusreservaApplication.class, args);
    }
}
