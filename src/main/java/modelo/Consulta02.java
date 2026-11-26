package modelo;

import java.util.List;

/** Modelo inmutable para Consulta 02: Despacho de Auxilio y Cercos Preventivos por Alerta. */
public final class Consulta02 {
    private Consulta02() {}

    public record Filtro(
            String desde,
            String hasta,
            String estadoAlerta,
            String comisaria,
            String estadoDespacho,
            String estadoCerco,
            String bus,
            String modoBus,
            String operador,
            boolean cualquiera,
            int orden,
            boolean desc
    ) {}

    public record Sentencia(String sql, List<Object> valores) {}

    public record Resultado(Filtro filtro, Sentencia sentencia, List<Object[]> filas) {}

    public record Catalogos(
            List<String> estadosAlerta,
            List<String> comisarias,
            List<String> estadosDespacho,
            List<String> estadosCerco
    ) {}
}
