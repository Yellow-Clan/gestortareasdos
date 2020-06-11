/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entites.CajaAperCierre;
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
public class CajaAperCierreData {

    static Connection cn = Conn.connectSQLite();
    static PreparedStatement ps;
    static Date dt = new Date();
    static SimpleDateFormat sdf = new SimpleDateFormat(SQLiteConfig.DEFAULT_DATE_STRING_FORMAT);

    static String currentTime = sdf.format(dt);

    public static CajaAperCierre getByFechaAndEsaper(Date date, int esaper) {
        CajaAperCierre d = new CajaAperCierre();
        String sql = "SELECT * FROM caja_aper_cierre WHERE strftime('%Y-%m-%d', fecha)= strftime('%Y-%m-%d', '" + sdf.format(date) + "') and esaper = '" + esaper + "'";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                d.setId(rs.getInt("id"));
                String fecha = rs.getString("fecha");
                System.out.println("getById.fecha:" + fecha);
                try {
                    Date datex = sdf.parse(fecha);
                    System.out.println("getById.datex:" + datex);
                    d.setFecha(date);
                    d.setDate_created(sdf.parse(rs.getString("date_created")));
                    d.setLast_updated(sdf.parse(rs.getString("last_updated")));
                } catch (Exception e) {
                }

                d.setEsaper(rs.getInt("esaper"));
                d.setSaldo_do(rs.getDouble("saldo_do"));
                d.setSaldo_so(rs.getDouble("saldo_so"));
                d.setSaldo_do_bancos(rs.getDouble("saldo_do_bancos"));
                d.setSaldo_so_bancos(rs.getDouble("saldo_so_bancos"));
                d.setGramos(rs.getDouble("gramos"));
                d.setUser(rs.getInt("user"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CajaAperCierreData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return d;
    }

    public static CajaAperCierre getById(int id) {
        CajaAperCierre d = new CajaAperCierre();

        String sql = "SELECT * FROM caja_aper_cierre WHERE id = '" + id + "'";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                d.setId(rs.getInt("id"));
                String fecha = rs.getString("fecha");
                System.out.println("getById.fecha:" + fecha);
                try {
                    Date date = sdf.parse(fecha);
                    System.out.println("getById.date:" + date);
                    d.setFecha(date);
                    d.setDate_created(sdf.parse(rs.getString("date_created")));
                    d.setLast_updated(sdf.parse(rs.getString("last_updated")));
                } catch (Exception e) {
                }

                d.setEsaper(rs.getInt("esaper"));
                d.setSaldo_do(rs.getDouble("saldo_do"));
                d.setSaldo_so(rs.getDouble("saldo_so"));
                d.setSaldo_do_bancos(rs.getDouble("saldo_do_bancos"));
                d.setSaldo_so_bancos(rs.getDouble("saldo_so_bancos"));
                d.setGramos(rs.getDouble("gramos"));
                d.setUser(rs.getInt("user"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CajaAperCierreData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return d;
    }

    public static int registrar(CajaAperCierre d) {
        int rsu = 0;

        String sql = "INSERT INTO caja_aper_cierre(fecha,  esaper, saldo_do, saldo_so, saldo_do_bancos, "
                + "saldo_so_bancos,   " //gramos,
                + "user) "
                + "VALUES(?,?,?,?,?  ,?,? )";
        int i = 0;
        try {
            String fecha = sdf.format(d.getFecha());
            ps = cn.prepareStatement(sql);
            ps.setString(++i, fecha);
            ps.setInt(++i, d.getEsaper());
            ps.setDouble(++i, d.getSaldo_do());
            ps.setDouble(++i, d.getSaldo_so());
            ps.setDouble(++i, d.getSaldo_do_bancos());
            ps.setDouble(++i, d.getSaldo_so_bancos());
            ps.setInt(++i, d.getUser());
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CajaAperCierreData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsu;
    }

    public static int actualizar(CajaAperCierre d) {
        int rsu = 0;
        String sql = "UPDATE caja_aper_cierre SET "
                + "fecha=?, "
                + "esaper=?, "
                + "saldo_do=?, "
                + "saldo_so=?, "
                + "saldo_do_bancos=?, "
                + "saldo_so_bancos=?, "
                + "user=?, "
                + "last_updated=? "
                + "WHERE id=?";
        int i = 0;
        try {
            String fecha = sdf.format(d.getFecha());
            ps = cn.prepareStatement(sql);
            ps.setString(++i, fecha);
            ps.setInt(++i, d.getEsaper());
            ps.setDouble(++i, d.getSaldo_do());
            ps.setDouble(++i, d.getSaldo_so());
            ps.setDouble(++i, d.getSaldo_do_bancos());
            ps.setDouble(++i, d.getSaldo_so_bancos());
            ps.setInt(++i, d.getUser());
            ps.setString(++i, sdf.format(dt));
            ps.setInt(++i, d.getId());
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CajaAperCierreData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsu;
    }

    public static int eliminar(int id) {
        int rsu = 0;
        String sql = "DELETE FROM caja_aper_cierre WHERE id = ?";
        try {
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CajaAperCierreData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsu;
    }

    public static List<CajaAperCierre> list(Date fecha, String busca) {
        String fechat = null;
        if (fecha == null) {
            System.out.println("list.fechat: SIN FECHAAA");
            fechat = currentTime;
        } else {
            fechat = sdf.format(fecha);
        }
        System.out.println("list.fechat:" + fechat);
        List<CajaAperCierre> ls = new ArrayList<CajaAperCierre>();
        String sql = "";
        
        if (busca.equals("")) {
            sql = "SELECT * FROM caja_aper_cierre "
                    + "WHERE strftime('%Y-%m-%d', fecha) = strftime('%Y-%m-%d', '" + fechat + "') "
                    + "ORDER BY fecha";
        } else {
            sql = "SELECT * FROM caja_aper_cierre WHERE (id LIKE'" + busca + "%'  "
                    + " OR esaper LIKE'" + busca + "%' OR "
                    + "id LIKE'" + busca + "%') "
                    + " AND strftime('%Y-%m-%d', fecha) = strftime('%Y-%m-%d', '" + fechat + "') "
                    + "ORDER BY fecha";
        }
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                CajaAperCierre d = new CajaAperCierre();
                d.setId(rs.getInt("id"));
                //d.setFecha(rs.getDate("fecha"));
                String fechax = rs.getString("fecha");
                System.out.println("list.fechax:" + fechax);
                try {
                    Date date = sdf.parse(fechax);
                    System.out.println("list.date:" + date);
                    d.setFecha(date);
                    d.setDate_created(sdf.parse(rs.getString("date_created")));
                    d.setLast_updated(sdf.parse(rs.getString("last_updated")));
                } catch (Exception e) {
                }
                d.setEsaper(rs.getInt("esaper"));
                d.setSaldo_do(rs.getDouble("saldo_do"));
                d.setSaldo_so(rs.getDouble("saldo_so"));
                d.setSaldo_do_bancos(rs.getDouble("saldo_do_bancos"));
                d.setSaldo_so_bancos(rs.getDouble("saldo_so_bancos"));
                d.setGramos(rs.getDouble("gramos"));
                d.setUser(rs.getInt("user"));
                ls.add(d);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CajaAperCierreData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ls;
    }
}
