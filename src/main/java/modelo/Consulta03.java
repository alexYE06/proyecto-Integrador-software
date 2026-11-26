package modelo;

import java.util.List;

/** Modelo de Consulta 03: análisis diario y distribución de alertas por bus. */
public final class Consulta03 {
    private Consulta03() {}

    public record Filtro(String desde, String hasta) {}
    public record Resultado(Filtro filtro, List<Object[]> filas) {}
}
