package servicio;
import conexion.ConexionBD;
import java.sql.*;
import java.util.*;
public class EstadoAlertaService {
 public static final List<String> ESTADOS=List.of("Recibida","En Evaluación","Auxilio Despachado","Atendida","Falsa Alarma");
 public record Incidente(int id,String fecha,String bus,String conductor,String modalidad,String estado,String descripcion){
  public String toString(){return "Alerta "+id+" · "+bus+" · "+estado;}
 }
 public List<Incidente> listar() throws SQLException {
  var lista=new ArrayList<Incidente>();
  String sql="SELECT a.*,b.codigo_unidad,CONCAT(c.nombres,' ',c.apellidos) conductor FROM alerta a JOIN bus b ON b.id_bus=a.id_bus JOIN conductor c ON c.id_conductor=a.id_conductor ORDER BY a.fecha_hora DESC,a.id_alerta DESC";
  try(var c=ConexionBD.obtenerConexion();var p=c.prepareStatement(sql);var r=p.executeQuery()){
   while(r.next())lista.add(new Incidente(r.getInt("id_alerta"),r.getString("fecha_hora"),r.getString("codigo_unidad"),r.getString("conductor"),r.getString("tipo_activacion"),r.getString("estado"),r.getString("descripcion")));
  }return lista;
 }
 public void actualizar(int id,String anterior,String nuevo) throws SQLException {
  if(id<=0||!ESTADOS.contains(nuevo))throw new IllegalArgumentException("Selecciona una alerta y un estado válido.");
  if(nuevo.equals(anterior))throw new IllegalArgumentException("La alerta ya tiene ese estado.");
  // Conserva el operador y evita sobrescribir un cambio concurrente.
  try(var c=ConexionBD.obtenerConexion();var p=c.prepareStatement("UPDATE alerta SET estado=? WHERE id_alerta=? AND estado=?")){
   p.setString(1,nuevo);p.setInt(2,id);p.setString(3,anterior);
   if(p.executeUpdate()!=1)throw new SQLException("La alerta cambió o ya no existe. Actualiza la lista.");
  }
 }
}
