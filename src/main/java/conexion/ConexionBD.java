package conexion;
import java.sql.*;
import java.util.Properties;
/** Configuración compartida por formularios, DAO, consultas y reportes. */
public final class ConexionBD {
 private ConexionBD() {}
 private static String valor(String n,String d){return System.getProperty(n,System.getenv().getOrDefault(n,d));}
 public static Connection obtenerConexion() throws SQLException {
  String url="jdbc:mysql://"+valor("DB_HOST","127.0.0.1")+":"+valor("DB_PORT","3306")+"/"+valor("DB_NAME","sistema_alertas");
  Properties p=new Properties();
  p.setProperty("user",valor("DB_USER","root"));p.setProperty("password",valor("DB_PASSWORD","123456"));
  p.setProperty("serverTimezone","America/Lima");p.setProperty("connectTimeout","5000");p.setProperty("socketTimeout","30000");
  p.setProperty("sslMode",valor("DB_SSL_MODE","PREFERRED"));    
  p.setProperty("allowPublicKeyRetrieval",valor("DB_ALLOW_PUBLIC_KEY_RETRIEVAL","false"));
  return DriverManager.getConnection(url,p);
 }
}
