package app;

import conexion.ConexionBD;
import java.nio.file.*;
import java.util.List;
import net.sf.jasperreports.data.jdbc.JdbcDataAdapterImpl;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.util.JacksonUtil;

/** Genera un adaptador privado para importar en Studio, sin publicar credenciales. */
public final class PrepararJasper {
    public static void main(String[] args) throws Exception {
        var config = ConexionBD.configuracion();
        try (var c = ConexionBD.obtenerConexion()) { if (!c.isValid(5)) throw new IllegalStateException("Revisa la conexión MySQL."); }
        var adapter = new JdbcDataAdapterImpl();
        adapter.setName("SAT-CARMEN local");
        adapter.setDriver("com.mysql.cj.jdbc.Driver");
        adapter.setUrl("jdbc:mysql://" + config.getProperty("DB_HOST") + ":" + config.getProperty("DB_PORT") + "/" + config.getProperty("DB_NAME") + "?serverTimezone=America/Lima");
        adapter.setUsername(config.getProperty("DB_USER"));
        adapter.setPassword(config.getProperty("DB_PASSWORD"));
        adapter.setSavePassword(true); adapter.setReadOnly(true);
        try (var files = Files.list(Path.of("lib"))) {
            adapter.setClasspath(files.filter(p -> p.getFileName().toString().startsWith("mysql-connector-j-")).map(p -> p.toAbsolutePath().toString()).toList());
        }
        if (adapter.getClasspath().isEmpty()) throw new IllegalStateException("Falta el conector MySQL en lib.");
        var serializer = JacksonUtil.getInstance(DefaultJasperReportsContext.getInstance());
        String xml = serializer.getXmlString(adapter);
        var loaded = serializer.loadXml(xml, JdbcDataAdapterImpl.class);
        if (!adapter.getUrl().equals(loaded.getUrl())) throw new IllegalStateException("Adaptador inválido.");
        Path dest = ConexionBD.archivoLocal().getParent().resolve("SAT-CARMEN.xml");
        Files.writeString(dest, xml);
        System.out.println("Adaptador privado para Studio preparado: " + dest);
    }
}
