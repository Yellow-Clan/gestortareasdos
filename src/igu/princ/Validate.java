/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package igu.princ;

/**
 *
 * @author Asullom
 */
public class Validate {

    public static boolean isPin = false;
    public static int userId = 0;
    public static int proveIdSelected = 0;
    

    public boolean comprobar() {

       

        if (isPin) {
            /*
                ErrorAlert er = new ErrorAlert(null, true);
                er.titulo.setText("OOPS...");
                er.msj.setText("LA APLICACIÓN YA ESTA EN EJECUCIÓN!");
                er.msj1.setText("");
                er.setVisible(true);
             */
            return true;

        } else {

            return false;
        }
    }

}
