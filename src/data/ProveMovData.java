/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entites.ProveMov;
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
public class ProveMovData {

    static Connection cn = Conn.connectSQLite();
    static PreparedStatement ps;
    static Date dt = new Date();
    static SimpleDateFormat sdf = new SimpleDateFormat(SQLiteConfig.DEFAULT_DATE_STRING_FORMAT);

    static String currentTime = sdf.format(dt);

    public static ProveMov getById(int id) {
        ProveMov d = new ProveMov();

        String sql = "SELECT * FROM prove_mov WHERE id = '" + id + "'";
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
                d.setGlosa(rs.getString("glosa"));
                d.setEsdolares(rs.getInt("esdolares"));
                d.setEsadelanto(rs.getInt("esadelanto"));
                d.setAdelanto_do(rs.getDouble("adelanto_do"));
                d.setAdelanto_so(rs.getDouble("adelanto_so"));
                d.setCobro_do(rs.getDouble("cobro_do"));
                d.setCobro_so(rs.getDouble("cobro_so"));
                d.setUser(rs.getInt("user"));
                d.setActivo(rs.getInt("activo"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProveMovData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return d;
    }

    public static int registrar(ProveMov d) {
        int rsu = 0;
        String[] returnId = {"id"};
        String sql = "INSERT INTO prove_mov(fecha,  prove_id, prove_nom, glosa, esdolares, "
                + "esadelanto, adelanto_do, adelanto_so, cobro_do, cobro_so,  "
                + "user) "
                + "VALUES(?,?,?,?,?  ,?,?,?,?,?   ,? )";
        int i = 0;
        try {
            String fecha = sdf.format(d.getFecha());
            ps = cn.prepareStatement(sql, returnId);
            ps.setString(++i, fecha);
            ps.setInt(++i, d.getProve_id());
            ps.setString(++i, d.getProve_nom());
            ps.setString(++i, d.getGlosa());
            ps.setInt(++i, d.getEsdolares());
            ps.setInt(++i, d.getEsadelanto());
            ps.setDouble(++i, d.getAdelanto_do());
            ps.setDouble(++i, d.getAdelanto_so());
            ps.setDouble(++i, d.getCobro_do());
            ps.setDouble(++i, d.getCobro_so());
            ps.setInt(++i, d.getUser());
            rsu = ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    rsu = rs.getInt(1);
                    System.out.println("rs.getInt(1): " + rsu);
                }
                rs.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProveMovData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsu;
    }

    public static int actualizar(ProveMov d) {
        int rsu = 0;
        String sql = "UPDATE prove_mov SET "
                + "fecha=?, "
                + "prove_id=?, "
                + "prove_nom=?, "
                + "glosa=?, "
                + "esdolares=?, "
                + "esadelanto=?, "
                + "adelanto_do=?, "
                + "adelanto_so=?, "
                + "cobro_do=?, "
                + "cobro_so=?, "
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
            ps.setString(++i, d.getGlosa());
            ps.setInt(++i, d.getEsdolares());
            ps.setInt(++i, d.getEsadelanto());
            ps.setDouble(++i, d.getAdelanto_do());
            ps.setDouble(++i, d.getAdelanto_so());
            ps.setDouble(++i, d.getCobro_do());
            ps.setDouble(++i, d.getCobro_so());
            ps.setInt(++i, d.getUser());
            ps.setInt(++i, d.getActivo());
            ps.setString(++i, sdf.format(dt));
            ps.setInt(++i, d.getId());
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProveMovData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsu;
    }

    public static int eliminar(int id) {
        int rsu = 0;
        String sql = "DELETE FROM prove_mov WHERE id = ?";
        try {
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(ProveMovData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsu;
    }

    public static List<ProveMov> list(Date fecha, String busca) {
        String fechat = null;
        if (fecha == null) {
            System.out.println("list.fechat: SIN FECHAAA");
            fechat = currentTime;
        } else {
            fechat = sdf.format(fecha);
        }
        System.out.println("list.fechat:" + fechat);

        List<ProveMov> ls = new ArrayList<ProveMov>();
        String sql = "";
        if (busca.equals("")) {
            sql = "SELECT * FROM prove_mov "
                    + "WHERE strftime('%Y-%m-%d', fecha) = strftime('%Y-%m-%d', '" + fechat + "') "
                    + "ORDER BY fecha";
        } else {
            sql = "SELECT * FROM prove_mov WHERE (id LIKE'" + busca + "%'  "
                    + " OR prove_nom LIKE'" + busca + "%' OR "
                    + "id LIKE'" + busca + "%') "
                    + " AND strftime('%Y-%m-%d', fecha) = strftime('%Y-%m-%d', '" + fechat + "') "
                    + "ORDER BY fecha";
        }

        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                ProveMov d = new ProveMov();
                d.setId(rs.getInt("id"));
                //d.setFecha(rs.getDate("fecha"));
                String fechax = rs.getString("fecha");
                System.out.println("list.fecha:" + fechax);
                try {
                    Date date = sdf.parse(fechax);
                    System.out.println("list.date:" + date);
                    d.setFecha(date);
                    d.setDate_created(sdf.parse(rs.getString("date_created")));
                    d.setLast_updated(sdf.parse(rs.getString("last_updated")));
                } catch (Exception e) {
                }
                d.setProve_id(rs.getInt("prove_id"));
                d.setProve_nom(rs.getString("prove_nom"));
                d.setGlosa(rs.getString("glosa"));
                d.setEsdolares(rs.getInt("esdolares"));
                d.setEsadelanto(rs.getInt("esadelanto"));
                d.setAdelanto_do(rs.getDouble("adelanto_do"));
                d.setAdelanto_so(rs.getDouble("adelanto_so"));
                d.setCobro_do(rs.getDouble("cobro_do"));
                d.setCobro_so(rs.getDouble("cobro_so"));
                d.setUser(rs.getInt("user"));
                d.setActivo(rs.getInt("activo"));
                ls.add(d);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProveMovData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ls;
    }
}
