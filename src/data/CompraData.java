/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entites.Compra;
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
public class CompraData {

    static Connection cn = Conn.connectSQLite();
    static PreparedStatement ps;
    static Date dt = new Date();
    static SimpleDateFormat sdf = new SimpleDateFormat(SQLiteConfig.DEFAULT_DATE_STRING_FORMAT);

    static String currentTime = sdf.format(dt);

    public static Compra getById(int id) {
        Compra d = new Compra();

        String sql = "SELECT * FROM compra WHERE id = '" + id + "'";
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
                d.setProve_id(rs.getInt("prove_id"));
                d.setProve_nom(rs.getString("prove_nom"));
                d.setCant_gr(rs.getDouble("cant_gr"));
                d.setEsdolares(rs.getInt("esdolares"));

                d.setOnza(rs.getDouble("onza"));
                d.setPorc(rs.getDouble("porc"));
                d.setLey(rs.getDouble("ley"));
                d.setSistema(rs.getDouble("sistema"));
                d.setTcambio(rs.getDouble("tcambio"));
                d.setPrecio_do(rs.getDouble("precio_do"));
                d.setPrecio_so(rs.getDouble("precio_so"));

                d.setTotal_do(rs.getDouble("total_do"));
                d.setTotal_so(rs.getDouble("total_so"));
                d.setSaldo_porpagar_do(rs.getDouble("saldo_porpagar_do"));
                d.setSaldo_porpagar_so(rs.getDouble("saldo_porpagar_so"));
                d.setUser(rs.getInt("user"));
                d.setActivo(rs.getInt("activo"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CompraData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return d;
    }

    public static int registrar(Compra d) {
        int rsu = 0;
        String[] returnId = {"id"};
        String sql = "INSERT INTO compra(fecha,  prove_id, prove_nom, cant_gr, esdolares, "
                + "onza, porc, ley, sistema, tcambio, "
                + "precio_do, precio_so, total_do, total_so, saldo_porpagar_do, "
                + "saldo_porpagar_so, user "
                + ") "
                + "VALUES(?,?,?,?,?  ,?,?,?,?,?   ,?,?,?,?,?  ,?,?)";
        int i = 0;
        try {
            String fecha = sdf.format(d.getFecha());
            ps = cn.prepareStatement(sql, returnId);
            ps.setString(++i, fecha);
            ps.setInt(++i, d.getProve_id());
            ps.setString(++i, d.getProve_nom());
            ps.setDouble(++i, d.getCant_gr());
            ps.setInt(++i, d.getEsdolares());

            ps.setDouble(++i, d.getOnza());
            ps.setDouble(++i, d.getPorc());
            ps.setDouble(++i, d.getLey());
            ps.setDouble(++i, d.getSistema());
            ps.setDouble(++i, d.getTcambio());
            ps.setDouble(++i, d.getPrecio_do());
            ps.setDouble(++i, d.getPrecio_so());

            ps.setDouble(++i, d.getTotal_do());
            ps.setDouble(++i, d.getTotal_so());
            ps.setDouble(++i, d.getSaldo_porpagar_do());
            ps.setDouble(++i, d.getSaldo_porpagar_so());
            ps.setInt(++i, d.getUser());
            rsu = ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    rsu = rs.getInt(1);
                    System.out.println("rs.getInt(1): "+rsu);
                }
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(CompraData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsu;
    }

    public static int actualizar(Compra d) {
        int rsu = 0;
        String sql = "UPDATE compra SET "
                + "fecha=?, "
                + "prove_id=?, "
                + "prove_nom=?, "
                + "cant_gr=?, "
                + "esdolares=?, "
                + "onza=?, "
                + "porc=?, "
                + "ley=?, "
                + "sistema=?, "
                + "tcambio=?, "
                + "precio_do=?, "
                + "precio_so=?, "
                + "total_do=?, "
                + "total_so=?, "
                + "saldo_porpagar_do=?, "
                + "saldo_porpagar_so=?, "
                + "user=?, "
                + "activo=?, "
                + "last_updated=? "
                + "WHERE id=?";
        int i = 0;
        try {
            String fecha = sdf.format(d.getFecha());
            ps = cn.prepareStatement(sql);
            ps.setString(++i, fecha);
            ps.setInt(++i, d.getProve_id());
            ps.setString(++i, d.getProve_nom());
            ps.setDouble(++i, d.getCant_gr());
            ps.setInt(++i, d.getEsdolares());

            ps.setDouble(++i, d.getOnza());
            ps.setDouble(++i, d.getPorc());
            ps.setDouble(++i, d.getLey());
            ps.setDouble(++i, d.getSistema());
            ps.setDouble(++i, d.getTcambio());
            ps.setDouble(++i, d.getPrecio_do());
            ps.setDouble(++i, d.getPrecio_so());

            ps.setDouble(++i, d.getTotal_do());
            ps.setDouble(++i, d.getTotal_so());
            ps.setDouble(++i, d.getSaldo_porpagar_do());
            ps.setDouble(++i, d.getSaldo_porpagar_so());
            ps.setInt(++i, d.getUser());
            ps.setInt(++i, d.getActivo());
            ps.setString(++i, sdf.format(dt));
            ps.setInt(++i, d.getId());
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CompraData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsu;
    }

    public static int eliminar(int id) {
        int rsu = 0;
        String sql = "DELETE FROM compra WHERE id = ?";
        try {
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CompraData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsu;
    }

    public static List<Compra> list(Date fecha, String busca) {
        String fechat = null;
        if (fecha == null) {
            System.out.println("list.fechat: SIN FECHAAA");
            fechat = currentTime;
        } else {
            fechat = sdf.format(fecha);
        }
        System.out.println("list.fechat:" + fechat);

        List<Compra> ls = new ArrayList<Compra>();
        String sql = "";
        if (busca.equals("")) {
            sql = "SELECT * FROM compra "
                    + "WHERE strftime('%Y-%m-%d', fecha) = strftime('%Y-%m-%d', '" + fechat + "') "
                    + "ORDER BY fecha";
        } else {
            sql = "SELECT * FROM compra WHERE (id LIKE'" + busca + "%'  "
                    + " OR prove_nom LIKE'" + busca + "%' OR "
                    + "id LIKE'" + busca + "%') "
                    + " AND strftime('%Y-%m-%d', fecha) = strftime('%Y-%m-%d', '" + fechat + "') "
                    + "ORDER BY fecha";
        }
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Compra d = new Compra();
                d.setId(rs.getInt("id"));
                String fechax = rs.getString("fecha");
                System.out.println("list.fecha:" + fechax);
                try {
                    Date datex = sdf.parse(fechax);
                    System.out.println("list.date:" + datex);
                    d.setFecha(datex);
                    d.setDate_created(sdf.parse(rs.getString("date_created")));
                    d.setLast_updated(sdf.parse(rs.getString("last_updated")));
                } catch (Exception e) {
                }
                d.setProve_id(rs.getInt("prove_id"));
                d.setProve_nom(rs.getString("prove_nom"));
                d.setCant_gr(rs.getDouble("cant_gr"));
                d.setEsdolares(rs.getInt("esdolares"));

                d.setOnza(rs.getDouble("onza"));
                d.setPorc(rs.getDouble("porc"));
                d.setLey(rs.getDouble("ley"));
                d.setSistema(rs.getDouble("sistema"));
                d.setTcambio(rs.getDouble("tcambio"));
                d.setPrecio_do(rs.getDouble("precio_do"));
                d.setPrecio_so(rs.getDouble("precio_so"));

                d.setTotal_do(rs.getDouble("total_do"));
                d.setTotal_so(rs.getDouble("total_so"));
                d.setSaldo_porpagar_do(rs.getDouble("saldo_porpagar_do"));
                d.setSaldo_porpagar_so(rs.getDouble("saldo_porpagar_so"));
                d.setUser(rs.getInt("user"));
                d.setActivo(rs.getInt("activo"));
                ls.add(d);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CompraData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ls;
    }
    
    
    public static List<Compra> list(Date fechai, Date fecha, String busca) {
        String fechati = null;
        if (fechai == null) {
            System.out.println("list.fechat: SIN FECHAAAiiiiii");
            fechati = currentTime;
        } else {
            fechati = sdf.format(fechai);
        }
         System.out.println("list.fechati:" + fechati);
        
        String fechat = null;
        if (fecha == null) {
            System.out.println("list.fechat: SIN FECHAAA");
            fechat = currentTime;
        } else {
            fechat = sdf.format(fecha);
        }
        System.out.println("list.fechat:" + fechat);

        List<Compra> ls = new ArrayList<Compra>();
        String sql = "";
        if (busca.equals("")) {
            sql = "SELECT * FROM compra "
                    + "WHERE strftime('%Y-%m-%d', fecha) between strftime('%Y-%m-%d', '" + fechati + "') and strftime('%Y-%m-%d', '" + fechat + "') "
                    + "ORDER BY fecha";
        } else {
            sql = "SELECT * FROM compra WHERE (id LIKE'" + busca + "%'  "
                    + " OR prove_nom LIKE'" + busca + "%' OR "
                    + "id LIKE'" + busca + "%') "
                    + " AND strftime('%Y-%m-%d', fecha)  between strftime('%Y-%m-%d', '" + fechati + "') and strftime('%Y-%m-%d', '" + fechat + "') "
                    + "ORDER BY fecha";
        }
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Compra d = new Compra();
                d.setId(rs.getInt("id"));
                String fechax = rs.getString("fecha");
                System.out.println("list.fecha:" + fechax);
                try {
                    Date datex = sdf.parse(fechax);
                    System.out.println("list.date:" + datex);
                    d.setFecha(datex);
                    d.setDate_created(sdf.parse(rs.getString("date_created")));
                    d.setLast_updated(sdf.parse(rs.getString("last_updated")));
                } catch (Exception e) {
                }
                d.setProve_id(rs.getInt("prove_id"));
                d.setProve_nom(rs.getString("prove_nom"));
                d.setCant_gr(rs.getDouble("cant_gr"));
                d.setEsdolares(rs.getInt("esdolares"));

                d.setOnza(rs.getDouble("onza"));
                d.setPorc(rs.getDouble("porc"));
                d.setLey(rs.getDouble("ley"));
                d.setSistema(rs.getDouble("sistema"));
                d.setTcambio(rs.getDouble("tcambio"));
                d.setPrecio_do(rs.getDouble("precio_do"));
                d.setPrecio_so(rs.getDouble("precio_so"));

                d.setTotal_do(rs.getDouble("total_do"));
                d.setTotal_so(rs.getDouble("total_so"));
                d.setSaldo_porpagar_do(rs.getDouble("saldo_porpagar_do"));
                d.setSaldo_porpagar_so(rs.getDouble("saldo_porpagar_so"));
                d.setUser(rs.getInt("user"));
                d.setActivo(rs.getInt("activo"));
                ls.add(d);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CompraData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ls;
    }
}
