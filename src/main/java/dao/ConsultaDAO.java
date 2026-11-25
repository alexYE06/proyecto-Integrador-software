package dao;
import modelo.Consulta.*;
import java.sql.*;
import java.time.*;
import java.util.List;
import java.util.ArrayList;
public class ConsultaDAO {
    public static String patron(String texto,String modo){
        String t=texto.replace("!","!!").replace("%","!%").replace("_","!_");
        return switch(modo){case "Empieza en" -> t+"%"; case "Termina en" -> "%"+t; default -> "%"+t+"%";};
    }
    public static Sentencia construir(Filtro f){
        LocalDate ini=f.desde().isBlank()?null:LocalDate.parse(f.desde());
        LocalDate fin=f.hasta().isBlank()?null:LocalDate.parse(f.hasta());
        if(ini!=null && fin!=null && ini.isAfter(fin))throw new IllegalArgumentException("La fecha inicial debe ser menor o igual que la final.");
        List<String> condiciones=new ArrayList<>(); List<Object> parametros=new ArrayList<>();
        String sql="SELECT a.id_alerta, a.fecha_hora, b.placa, b.codigo_unidad, CONCAT(c.nombres, ' ', c.apellidos) AS conductor, a.tipo_activacion, a.estado FROM alerta a JOIN bus b ON b.id_bus=a.id_bus JOIN conductor c ON c.id_conductor=a.id_conductor";
        // El intervalo es una sola condición lógica: nunca se separan sus extremos con OR.
        if(ini!=null && fin!=null){condiciones.add("(a.fecha_hora >= ? AND a.fecha_hora < ?)");parametros.add(Timestamp.valueOf(ini.atStartOfDay()));parametros.add(Timestamp.valueOf(fin.plusDays(1).atStartOfDay()));}
        else if(ini!=null){condiciones.add("a.fecha_hora >= ?");parametros.add(Timestamp.valueOf(ini.atStartOfDay()));}
        else if(fin!=null){condiciones.add("a.fecha_hora < ?");parametros.add(Timestamp.valueOf(fin.plusDays(1).atStartOfDay()));}
        if(!f.estado().equals("Todos")){condiciones.add("a.estado = ?");parametros.add(f.estado());}
        if(!f.placa().isBlank()){condiciones.add("b.placa LIKE ? ESCAPE '!'");parametros.add(patron(f.placa(),f.modo()));}
        if(!f.conductor().isBlank()){condiciones.add("CONCAT(c.nombres, ' ', c.apellidos) LIKE ? ESCAPE '!'");parametros.add(patron(f.conductor(),"Contiene"));}
        if(!condiciones.isEmpty())sql+=" WHERE ("+String.join(f.cualquiera()?" OR ":" AND ",condiciones)+")";
        String campo=switch(f.orden()){case 0->"a.fecha_hora";case 1->"b.placa";case 2->"conductor";case 3->"a.estado";default->throw new IllegalArgumentException("Orden inválido");};
        sql+=" ORDER BY "+campo+(f.desc()?" DESC":" ASC")+", a.id_alerta ASC";
        return new Sentencia(sql,List.copyOf(parametros));
    }
    public static Resultado ejecutar(Filtro f) throws SQLException {
        Sentencia s=construir(f); List<Object[]> filas=new ArrayList<>();
        try(Connection cn=conexion.ConexionBD.obtenerConexion();PreparedStatement ps=cn.prepareStatement(s.sql())){
            ps.setQueryTimeout(30);
            for(int i=0;i<s.valores().size();i++)ps.setObject(i+1,s.valores().get(i));
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next()){
                    Object[] fila=new Object[7]; for(int i=0;i<7;i++)fila[i]=i==1?rs.getTimestamp(i+1):rs.getObject(i+1);
                    filas.add(fila);
                }
            }
        }
        return new Resultado(f,s,List.copyOf(filas));
    }
}
