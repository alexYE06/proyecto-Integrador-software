package pe.carmendelapunta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "pe.carmendelapunta", "controlador.api", "servicio", "dao", "excepcion", "modelo", "dto", "config" })
public class SistemaAlertasApplication {

    public static void main(String[] args) {
        SpringApplication.run(SistemaAlertasApplication.class, args);
    }
}
