package modelo;
import java.util.List;
public final class Consulta {
    public record Filtro(String desde, String hasta, String estado, String placa,
                         String modo, String conductor, boolean cualquiera, int orden, boolean desc) {}
    public record Sentencia(String sql, List<Object> valores) {}
    public record Resultado(Filtro filtro, Sentencia sentencia, List<Object[]> filas) {}
}
