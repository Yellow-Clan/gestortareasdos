package data;


import entites.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Asullom
 */
public class CienteData {

    static Connection cn = Conn.connectSQLite();
    static PreparedStatement ps;

    public static int registrar(Cliente d) {
        //d.nombres = "Juan";
        int rsu = 0;
        String sql = "INSERT INTO cliente(nombres, infoadic) "
                + "VALUES(?,?)";
        int i = 0;
        try {
            ps = cn.prepareStatement(sql);
            ps.setString(++i, d.getNombres());
            ps.setString(++i, d.getInfoadic());
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CienteData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsu;
    }

    public static int actualizar(Cliente d) {
        int rsu = 0;
        String sql = "UPDATE cliente SET "
                + "nombres=?, "
                + "infoadic=? "
                + "WHERE id=?";
        int i = 0;
        try {
            ps = cn.prepareStatement(sql);
            ps.setString(++i, d.getNombres());
            ps.setString(++i, d.getInfoadic());
            ps.setInt(++i, d.getId());
            rsu = ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(CienteData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rsu;
    }

        public static int eliminar(int id) throws Exception {
        int rsu = 0;
        String sql = "DELETE FROM cliente WHERE id = ?";
        try {
            ps = cn.prepareStatement(sql);
            ps.setInt(1, id);
            rsu = ps.executeUpdate();

        } catch (SQLException ex) {
            // Logger.getLogger(CienteData.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("NO del " + ex.toString());
            throw new Exception("Detalle:" + ex.getMessage());
        }
        return rsu;
    }
        

    public static List<Cliente> list(String busca) {
        List<Cliente> ls = new ArrayList<Cliente>();

        String sql = "";
        if (busca.equals("")) {
            sql = "SELECT * FROM cliente ORDER BY id";
        } else {
            sql = "SELECT * FROM cliente WHERE (id LIKE'" + busca + "%' OR "
                    + "nombres LIKE'" + busca + "%' OR infoadic LIKE'" + busca + "%' OR "
                    + "id LIKE'" + busca + "%') "
                    + "ORDER BY nombres";
        }
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Cliente d = new Cliente();
                d.setId(rs.getInt("id"));
                d.setNombres(rs.getString("nombres"));
                d.setInfoadic(rs.getString("infoadic"));
                ls.add(d);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CienteData.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ls;
    }

    public static void iniciarTransaccion() {
        try {
            cn.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(CienteData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void finalizarTransaccion() {
        try {
            cn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(CienteData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cancelarTransaccion() {
        try {
            cn.rollback();
        } catch (SQLException ex) {
            Logger.getLogger(CienteData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
