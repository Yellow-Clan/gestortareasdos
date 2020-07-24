package igu;

import igu.util.alerts.ErrorAlert;
import igu.princ.MainFrame;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Yellow C
 */
public class App {
    private static File archivo = new File("db2.db");

    public static void main(String[] args) {
        
         if (!archivo.exists()) {
            ErrorAlert er = new ErrorAlert(null, true);
            er.titulo.setText("SORRY");
            er.msj.setText("NO SE ENCONTRÓ LA BASE DE DATOS!");
            er.msj1.setText("Revise que la DB este junto al programa, o comuníquese con su proveedor");
            er.setVisible(true);
            System.exit(0);
        }
         
        //String s = "com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel";
        //try {
            MainFrame m = new MainFrame();
            //javax.swing.UIManager.setLookAndFeel(s);
            m.setLocationRelativeTo(null);
            m.setVisible(true);

        //} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        //    Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        //}
    }
}
