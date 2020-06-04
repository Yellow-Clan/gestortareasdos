/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import entites.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Asullom
 */
public class UserData {

    static Connection cn = Conn.connectSQLite();
    static PreparedStatement ps;

    public static User getByPin(String pin, String rol) {
        User d = new User();

        String sql = "SELECT * FROM user WHERE pin = ? and  rol = ? ";
        int i = 0;
        try {
            ps = cn.prepareStatement(sql);
            ps.setString(++i, pin);
            ps.setString(++i, rol);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                d.setId(rs.getInt("id"));
                d.setPin(rs.getString("pin"));
                d.setRol(rs.getInt("rol"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return d;
    }
    
    //Las siguientes funciones no son usadas
    public static int registrar(User d) {
        int rsu = 0;
        String sql = "INSERT INTO user(rol, pin) "
                + "VALUES(?,?)";
        int i = 0;
        try {
            ps = cn.prepareStatement(sql);
            ps.setInt(++i, d.getRol());
            ps.setString(++i, d.getPin());
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsu;
    }

    public static int actualizar(User d) {
        int rsu = 0;
        String sql = "UPDATE user SET "
                + "rol=?, "
                + "pin=? "
                + "WHERE id=?";
        int i = 0;
        try {
            ps = cn.prepareStatement(sql);
            ps.setInt(++i, d.getRol());
            ps.setString(++i, d.getPin());
            ps.setInt(++i, d.getId());
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsu;
    }

    public static int eliminar(int id) {
        int rsu = 0;
        String sql = "DELETE FROM user WHERE id = ?";
        try {
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsu;
    }

    public static List<User> list(String busca) {
        List<User> ls = new ArrayList<User>();
        String sql = "";
        if (busca.equals("")) {
            sql = "SELECT * FROM user ORDER BY id";
        } else {
            sql = "SELECT * FROM user WHERE (id LIKE'" + busca + "%' OR "
                    + "rol LIKE'" + busca + "%' OR pin LIKE'" + busca + "%' OR "
                    + "id LIKE'" + busca + "%') "
                    + "ORDER BY rol";
        }
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                User d = new User();
                d.setId(rs.getInt("id"));
                d.setRol(rs.getInt("rol"));
                d.setPin(rs.getString("pin"));
                ls.add(d);
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ls;
    }
     
}
