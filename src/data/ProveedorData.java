/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entites.Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sqlite.SQLiteConfig;

/**
 *
 * @author Asullom
 */
public class ProveedorData {

    static Connection cn = Conn.connectSQLite();
    static PreparedStatement ps;
    static Date dt = new Date();
    static SimpleDateFormat sdf = new SimpleDateFormat(SQLiteConfig.DEFAULT_DATE_STRING_FORMAT);
    //String fecha = sdf_p.format(d.getFecha_nac());
    public static int registrar(Proveedor d) {
        String currentTime = sdf.format(dt);
        System.out.print("currentTime:" + currentTime);
        int rsu = 0;
        String sql = "INSERT INTO proveedor(nombres, infoadic, fecha_nac, date_created, last_updated) "
                + "VALUES(?,?,?,?,?)";
        int i = 0;
        try {
            System.out.print("registrar.getFecha_nac:" + d.getFecha_nac());
            String fecha = sdf.format(d.getFecha_nac());
            System.out.println("registrar.fecha:" + fecha);
            ps = cn.prepareStatement(sql);
            ps.setString(++i, d.getNombres());
            ps.setString(++i, d.getInfoadic());
            ps.setString(++i, fecha);
            ps.setString(++i, sdf.format(dt));
            ps.setString(++i, sdf.format(dt));
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            System.out.print("registrar.ex:" + ex);
            Logger.getLogger(CienteData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsu;
    }

    public static int actualizar(Proveedor d) {
        int rsu = 0;
        String sql = "UPDATE proveedor SET "
                + "nombres=?, "
                + "infoadic=?, "
                + "fecha_nac=?, "
                + "last_updated=? "
                + "WHERE id=?";
        int i = 0;
        try {
             System.out.println("actualizar.getFecha_nac:" + d.getFecha_nac());
             String fecha = sdf.format(dt);
             try {
                fecha = sdf.format(d.getFecha_nac());
            } catch (Exception e) {
                System.out.println("actualizar.getFecha_nac format:" + fecha);
            }
            System.out.println("actualizar.fecha:" + fecha);
            
            ps = cn.prepareStatement(sql);
            ps.setString(++i, d.getNombres());
            ps.setString(++i, d.getInfoadic());
            ps.setString(++i, fecha);
            ps.setString(++i, sdf.format(dt));
            ps.setInt(++i, d.getId());
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CienteData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsu;
    }

    public static int eliminar(int id) {
        int rsu = 0;
        String sql = "DELETE FROM proveedor WHERE id = ?";
        try {
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CienteData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsu;
    }

    public static List<Proveedor> list(String busca) {
        List<Proveedor> ls = new ArrayList<Proveedor>();
        //sql = "SELECT *,strftime('%d/%m/%Y',fecha_nac ) as fecha_nac2 FROM proveedor ORDER BY id";
        String sql = "";
        if (busca.equals("")) {
            sql = "SELECT * FROM proveedor ORDER BY id";
        } else {
            sql = "SELECT * FROM proveedor WHERE (id LIKE'" + busca + "%' OR "
                    + "nombres LIKE'" + busca + "%' OR infoadic LIKE'" + busca + "%' OR "
                    + "id LIKE'" + busca + "%') "
                    + "ORDER BY nombres";
        }
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Proveedor d = new Proveedor();
                d.setId(rs.getInt("id"));
                d.setNombres(rs.getString("nombres"));
                d.setInfoadic(rs.getString("infoadic"));
                //String fecha = sdf_p.format(rs.getDate("fecha_nac"));
                String fecha = rs.getString("fecha_nac");
                System.out.println("list.fecha:" + fecha);
                try {
                    Date date = sdf.parse(fecha);
                    System.out.println("Xlist.date:" + date);
                    d.setFecha_nac(date);

                    d.setDate_created(sdf.parse(rs.getString("date_created")));
                    System.out.println("list.date_created:" + rs.getString("date_created"));
                    System.out.println("list.last_updated:" + rs.getString("last_updated"));
                    d.setLast_updated(sdf.parse(rs.getString("last_updated")));
                    
                } catch (Exception e) {
                }
                ls.add(d);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CienteData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ls;
    }
}
