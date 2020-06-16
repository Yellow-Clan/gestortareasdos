/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.reports;

import data.Conn;
import entites.views.ComprasProveMov;
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
public class ComprasProveMovReport {

    static Connection cn = Conn.connectSQLite();
    static PreparedStatement ps;
    static Date dt = new Date();
    static SimpleDateFormat sdf = new SimpleDateFormat(SQLiteConfig.DEFAULT_DATE_STRING_FORMAT);

    static String currentTime = sdf.format(dt);

    public static List<ComprasProveMov> list(Date fechai, Date fecha, String busca) {
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
        if (busca == null) {
            busca = "";
        }
        System.out.println("list.fechat:" + fechat);
        List<ComprasProveMov> ls = new ArrayList<ComprasProveMov>();
        String sql = "SELECT "
                + "*\n"
                + "FROM\n"
                + "(\n"
                + " SELECT \n"
                + "  id,\n"
                + "  fecha,\n"
                + "  prove_id,\n"
                + "  prove_nom,\n"
                + "  cant_gr as ingreso_cant_gr,\n"
                + "  onza,\n"
                + "  porc,\n"
                + "  ley,\n"
                + "  tcambio,\n"
                + "  precio_do,\n"
                + "  precio_so,\n"
                + "  total_do,\n"
                + "  total_so,\n"
                + "  saldo_porpagar_do,\n"
                + "  saldo_porpagar_so\n"
                + " ,total_do - saldo_porpagar_do as egreso_do\n"
                + " ,total_so - saldo_porpagar_so as egreso_so\n"
                + " ,0 as ingreso_do\n"
                + " ,0 as ingreso_so\n"
                + " ,'' as glosa\n"
                + " FROM compra\n"
                + " UNION\n"
                + " SELECT \n"
                + "  id,\n"
                + "  fecha,\n"
                + "  prove_id,\n"
                + "  prove_nom,\n"
                + "  0 as ingreso_cant_gr,\n"
                + "  0 as onza,\n"
                + "  0 as porc,\n"
                + "  0 as ley,\n"
                + "  0 as tcambio,\n"
                + "  0 as precio_do,\n"
                + "  0 as precio_so,\n"
                + "  0 as total_do,\n"
                + "  0 as total_so,\n"
                + "  0 as saldo_porpagar_do,\n"
                + "  0 as saldo_porpagar_so\n"
                + " ,adelanto_do as egreso_do\n"
                + " ,adelanto_so as egreso_so\n"
                + " ,cobro_do as ingreso_do\n"
                + " ,cobro_so as ingreso_so\n"
                + " ,glosa\n"
                + " FROM prove_mov\n"
                + ") as G "
                + "WHERE (id LIKE'" + busca + "%'  "
                + " OR prove_nom LIKE'" + busca + "%' OR "
                + "id LIKE'" + busca + "%') "
                + " AND strftime('%Y-%m-%d', fecha)  between strftime('%Y-%m-%d', '" + fechati + "') and strftime('%Y-%m-%d', '" + fechat + "') "
                + "ORDER BY fecha";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                ComprasProveMov d = new ComprasProveMov();

                d.setId(rs.getInt("id"));
                String fechax = rs.getString("fecha");
                System.out.println("-:list.fecha:" + fechax + " " + d.getId());
                try {
                    Date datex = sdf.parse(fechax);
                    System.out.println("list.date:" + datex);
                    d.setFecha(datex);
                } catch (Exception e) {
                }
                d.setProve_id(rs.getInt("prove_id"));
                d.setProve_nom(rs.getString("prove_nom"));
                d.setIngreso_cant_gr(rs.getDouble("ingreso_cant_gr"));

                d.setOnza(rs.getDouble("onza"));
                d.setPorc(rs.getDouble("porc"));
                d.setLey(rs.getDouble("ley"));
                d.setTcambio(rs.getDouble("tcambio"));
                d.setPrecio_do(rs.getDouble("precio_do"));
                d.setPrecio_so(rs.getDouble("precio_so"));

                d.setTotal_do(rs.getDouble("total_do"));
                d.setTotal_so(rs.getDouble("total_so"));
                d.setSaldo_porpagar_do(rs.getDouble("saldo_porpagar_do"));
                d.setSaldo_porpagar_so(rs.getDouble("saldo_porpagar_so"));

                d.setEgreso_do(rs.getDouble("egreso_do"));
                d.setEgreso_so(rs.getDouble("egreso_so"));
                d.setIngreso_do(rs.getDouble("ingreso_do"));
                d.setIngreso_so(rs.getDouble("ingreso_so"));

                d.setGlosa(rs.getString("glosa"));
                ls.add(d);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ComprasProveMovReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ls;
    }

    public static List<ComprasProveMov> list(int prove_id, Date fechai, Date fecha, String busca) {
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
        if (busca == null) {
            busca = "";
        }
        String and_prove_id = " ";
        if (prove_id > 0) {
            and_prove_id = " AND prove_id = " + prove_id + " ";
        }
        System.out.println("list.fechat:" + fechat);
        List<ComprasProveMov> ls = new ArrayList<ComprasProveMov>();
        String sql = "SELECT "
                + "*\n"
                + "FROM\n"
                + "(\n"
                + " SELECT \n"
                + "  id,\n"
                + "  fecha,\n"
                + "  prove_id,\n"
                + "  prove_nom,\n"
                + "  cant_gr as ingreso_cant_gr,\n"
                + "  onza,\n"
                + "  porc,\n"
                + "  ley,\n"
                + "  tcambio,\n"
                + "  precio_do,\n"
                + "  precio_so,\n"
                + "  total_do,\n"
                + "  total_so,\n"
                + "  saldo_porpagar_do,\n"
                + "  saldo_porpagar_so\n"
                + " ,total_do - saldo_porpagar_do as egreso_do\n"
                + " ,total_so - saldo_porpagar_so as egreso_so\n"
                + " ,0 as ingreso_do\n"
                + " ,0 as ingreso_so\n"
                + " ,'' as glosa\n"
                + " FROM compra\n"
                + " UNION\n"
                + " SELECT \n"
                + "  id,\n"
                + "  fecha,\n"
                + "  prove_id,\n"
                + "  prove_nom,\n"
                + "  0 as ingreso_cant_gr,\n"
                + "  0 as onza,\n"
                + "  0 as porc,\n"
                + "  0 as ley,\n"
                + "  0 as tcambio,\n"
                + "  0 as precio_do,\n"
                + "  0 as precio_so,\n"
                + "  0 as total_do,\n"
                + "  0 as total_so,\n"
                + "  0 as saldo_porpagar_do,\n"
                + "  0 as saldo_porpagar_so\n"
                + " ,adelanto_do as egreso_do\n"
                + " ,adelanto_so as egreso_so\n"
                + " ,cobro_do as ingreso_do\n"
                + " ,cobro_so as ingreso_so\n"
                + " ,glosa\n"
                + " FROM prove_mov\n"
                + ") as G "
                + "WHERE (id LIKE'" + busca + "%'  "
                + " OR prove_nom LIKE'" + busca + "%' OR "
                + "id LIKE'" + busca + "%') "
                + and_prove_id
                + " AND strftime('%Y-%m-%d', fecha)  between strftime('%Y-%m-%d', '" + fechati + "') and strftime('%Y-%m-%d', '" + fechat + "') "
                + "ORDER BY fecha";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                ComprasProveMov d = new ComprasProveMov();

                d.setId(rs.getInt("id"));
                String fechax = rs.getString("fecha");
                System.out.println("-:list.fecha:" + fechax + " " + d.getId());
                try {
                    Date datex = sdf.parse(fechax);
                    System.out.println("list.date:" + datex);
                    d.setFecha(datex);
                } catch (Exception e) {
                }
                d.setProve_id(rs.getInt("prove_id"));
                d.setProve_nom(rs.getString("prove_nom"));
                d.setIngreso_cant_gr(rs.getDouble("ingreso_cant_gr"));

                d.setOnza(rs.getDouble("onza"));
                d.setPorc(rs.getDouble("porc"));
                d.setLey(rs.getDouble("ley"));
                d.setTcambio(rs.getDouble("tcambio"));
                d.setPrecio_do(rs.getDouble("precio_do"));
                d.setPrecio_so(rs.getDouble("precio_so"));

                d.setTotal_do(rs.getDouble("total_do"));
                d.setTotal_so(rs.getDouble("total_so"));
                d.setSaldo_porpagar_do(rs.getDouble("saldo_porpagar_do"));
                d.setSaldo_porpagar_so(rs.getDouble("saldo_porpagar_so"));

                d.setEgreso_do(rs.getDouble("egreso_do"));
                d.setEgreso_so(rs.getDouble("egreso_so"));
                d.setIngreso_do(rs.getDouble("ingreso_do"));
                d.setIngreso_so(rs.getDouble("ingreso_so"));

                d.setGlosa(rs.getString("glosa"));
                ls.add(d);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ComprasProveMovReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ls;
    }
}
