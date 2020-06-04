/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.reports;

import data.Conn;
import entites.views.ProveSaldo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sqlite.SQLiteConfig;

/**
 *
 * @author Asullom
 */
public class ProveSaldoReport {
    static Connection cn = Conn.connectSQLite();
    static PreparedStatement ps;

    public static ProveSaldo getSaldoById(int id) {
        ProveSaldo d = new ProveSaldo();
        String sql = "SELECT "
                +"p.id, p.nombres "
                +", ((SELECT coalesce( sum(DISTINCT saldo_do_porpagar), 0) FROM compra WHERE prove_id  =p.id  ) - "
                +"   (SELECT coalesce( sum(DISTINCT adelanto_do)- sum(DISTINCT cobro_do), 0) FROM prove_mov WHERE prove_id  =p.id))  as saldo_do "
                +", ((SELECT coalesce( sum(DISTINCT saldo_so_porpagar), 0) FROM compra WHERE prove_id  =p.id  ) - "
                +"   (SELECT coalesce( sum(DISTINCT adelanto_so)- sum(DISTINCT cobro_so), 0) FROM prove_mov WHERE prove_id  =p.id))  as saldo_so "
                +"FROM proveedor as p   "
              
                +"WHERE p.id = '" + id + "'"
                +"GROUP BY p.id, p.nombres ";
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                d.setId(rs.getInt("id"));
                d.setNombres(rs.getString("nombres"));
                d.setSaldo_do(rs.getDouble("saldo_do"));
                d.setSaldo_so(rs.getDouble("saldo_so"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProveSaldoReport.class.getName()).log(Level.SEVERE, null, ex);
        }
        return d;
    }
}
