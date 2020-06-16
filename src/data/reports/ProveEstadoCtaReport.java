/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.reports;

import data.Conn;
import entites.views.ProveEstadoCta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

import java.sql.Statement;
import org.sqlite.SQLiteConfig;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Asullom
 */
public class ProveEstadoCtaReport {

    static Connection cn = Conn.connectSQLite();
    static PreparedStatement ps;
    static Date dt = new Date();
    static SimpleDateFormat sdf = new SimpleDateFormat(SQLiteConfig.DEFAULT_DATE_STRING_FORMAT);

    static String currentTime = sdf.format(dt);

    public static List<ProveEstadoCta> list(int prove_id, Date fechai, Date fecha, String busca) {
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
        List<ProveEstadoCta> ls = new ArrayList<ProveEstadoCta>();

        String sql = "SELECT "
                + "*\n"
                + "FROM\n"
                + "(\n"
                + " SELECT \n"
                + "  id,\n"
                + "  fecha,\n"
                + "  prove_id,\n"
                + "  prove_nom,\n"
                + "  'Compra ' || cant_gr  || 'gr ('  ||  onza  || 'onza '  || porc  || '% '  || ley  || 'ley tc='  || tcambio  ||' '  ||\n"
                + "  CASE\n"
                + "	WHEN esdolares ==1   THEN 'pre$' || precio_do|| ') tot='  || total_do\n"
                + "	WHEN esdolares ==0   THEN 'preS/' || precio_so|| ') tot='  || total_so\n"
                + "	ELSE ')ERROR'\n"
                + "  END glosa\n"
                + " ,0 as debito_do\n"
                + " ,0 as debito_so\n"
                + " ,saldo_porpagar_do as credito_do\n"
                + " ,saldo_porpagar_so as credito_so\n"
                + " FROM compra\n"
                + " UNION\n"
                + " SELECT \n"
                + "  id,\n"
                + "  fecha,\n"
                + "  prove_id,\n"
                + "  prove_nom,\n"
                + "  glosa\n"
                + " ,adelanto_do as debito_do\n"
                + " ,adelanto_so as debito_so\n"
                + " ,cobro_do as credito_do\n"
                + " ,cobro_so as credito_so\n"
                + "\n"
                + " FROM prove_mov\n"
                + ") as G "
                + "WHERE (id LIKE'" + busca + "%'  "
                + " OR prove_nom LIKE'" + busca + "%' OR "
                + "id LIKE'" + busca + "%') "
                + and_prove_id
              //  + " AND strftime('%Y-%m-%d', fecha)  between strftime('%Y-%m-%d', '" + fechati + "') and strftime('%Y-%m-%d', '" + fechat + "') "
                + "ORDER BY fecha";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                ProveEstadoCta d = new ProveEstadoCta();

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
                d.setGlosa(rs.getString("glosa"));

                d.setDebito_do(rs.getDouble("debito_do"));
                d.setDebito_so(rs.getDouble("debito_so"));
                d.setCredito_do(rs.getDouble("credito_do"));
                d.setCredito_so(rs.getDouble("credito_so"));

                ls.add(d);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ComprasProveMovReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ls;
    }
}
