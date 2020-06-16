/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package igu.compras;

import data.ProveedorData;
import data.reports.ProveEstadoCtaReport;
import data.reports.ProveSaldoReport;
import entites.Proveedor;
import entites.views.ProveEstadoCta;
import entites.views.ProveSaldo;
import static igu.compras.ReporteComprasPanel.jScrollPane1;
import static igu.compras.ReporteComprasPanel.tabla;
import igu.princ.Validate;
import igu.util.Config;
import igu.util.tables.EstiloTablaFootRenderer;
import igu.util.tables.EstiloTablaHeader;
import igu.util.tables.ExportarExcel;
import igu.util.tables.MyScrollbarUI;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Asullom
 */
public class ProveEstadoCtaDialog extends javax.swing.JDialog {
    
    ExportarExcel obj;
    SimpleDateFormat iguSDF = new SimpleDateFormat(Config.DEFAULT_DATE_STRING_FORMAT_PE);

    DefaultListModel<Proveedor> defaultListModel = new DefaultListModel();
    DefaultListModel defaultListModelValue = new DefaultListModel();

    public ProveEstadoCtaDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);

        initComponents();
        // super.pack();
        // repaint();
        ProveSaldo pro = null;
        //JSONObject pWindow = getContextJSONObject(parent);
        //String prove_idt= prove_id.getText();// parent.getOwner().getClass().getCanonicalName();
        try {
            System.out.println("prove_idt:" + Validate.proveIdSelected);
            pro = ProveSaldoReport.getSaldoById(Validate.proveIdSelected);
            prove_nom.setText(pro.getNombres());
            saldo_do.setText(pro.getSaldo_do() + "");
            saldo_so.setText(pro.getSaldo_so() + "");
        } catch (NumberFormatException e1) {
            System.err.println("err" + e1);
        }
         Date date_i = new Date();
        tabla.getTableHeader().setDefaultRenderer(new EstiloTablaHeader());
        tabla.setDefaultRenderer(Object.class, new EstiloTablaFootRenderer());

        jScrollPane1.getViewport().setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.getVerticalScrollBar().setUI(new MyScrollbarUI());
        jScrollPane1.getHorizontalScrollBar().setUI(new MyScrollbarUI());

        paintTable(Validate.proveIdSelected+"", date_i, date_i, "");
    }
    
    private void paintTable(String prove_id, Date datei, Date date, String buscar) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();

        List<ProveEstadoCta> lis = null;
        try {
            lis = ProveEstadoCtaReport.list(Integer.parseInt(prove_id), datei, date, buscar);
        } catch (NumberFormatException e1) {
          //  lis = ProveEstadoCtaReport.list(0, datei, date, buscar);
        }

        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
        String datos[] = new String[10];
        int cont = 0;
        double sdebito_do = 0;
        double sdebito_so = 0;
        double scredito_do = 0;
        double scredito_so = 0;
        for (ProveEstadoCta d : lis) {
            
            sdebito_do = sdebito_do + d.getDebito_do();
            sdebito_so = sdebito_so + d.getDebito_so();
            scredito_do = scredito_do + d.getCredito_do();
            scredito_so = scredito_so + d.getCredito_so();
            datos[0] = ++cont + "";
            datos[1] = d.getId() + "";
            datos[2] = d.getProve_nom();
            datos[3] = d.getGlosa();
           
            datos[4] = d.getDebito_do() == 0 ? "" : new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getDebito_do()) + "";
            datos[5] = d.getDebito_so() == 0 ? "" : new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getDebito_so()) + "";
            datos[6] = d.getCredito_do() == 0 ? "" : new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getCredito_do()) + "";
            datos[7] = d.getCredito_so() == 0 ? "" : new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getCredito_so()) + "";

            datos[8] = iguSDF.format(d.getFecha());
            modelo.addRow(datos);
        }
        datos[0] = "";
        datos[1] = "";
        datos[2] = "";

        datos[3] = "SUMAS";

        datos[4] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(sdebito_do) + "";
        datos[5] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(sdebito_so) + "";
        datos[6] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(scredito_do) + "";
        datos[7] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(scredito_so) + "";
        datos[8] = "";
        modelo.addRow(datos);

        // tabla.getTableHeader().setReorderingAllowed(false);
        //tabla.setRowHeight(25);//tamaño de las celdas
        //tabla.setGridColor(new java.awt.Color(0, 0, 0));
        //Se define el tamaño de largo para cada columna y su contenido
        tabla.getColumnModel().getColumn(0).setMaxWidth(35);
        tabla.getColumnModel().getColumn(0).setCellRenderer(new EstiloTablaFootRenderer("texto"));
        tabla.getColumnModel().getColumn(1).setMaxWidth(35);
        tabla.getColumnModel().getColumn(1).setCellRenderer(new EstiloTablaFootRenderer("texto"));
        
        tabla.getColumnModel().getColumn(2).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(2).setCellRenderer(new EstiloTablaFootRenderer("texto"));
        
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(3).setCellRenderer(new EstiloTablaFootRenderer("texto"));
        
        DefaultTableCellRenderer rightRenderer = new EstiloTablaFootRenderer("numerico");
       
        tabla.getColumnModel().getColumn(4).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        tabla.getColumnModel().getColumn(6).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        tabla.getColumnModel().getColumn(7).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
       

        tabla.getColumnModel().getColumn(8).setPreferredWidth(10);
        tabla.getColumnModel().getColumn(8).setCellRenderer(new EstiloTablaFootRenderer("fecha"));


    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        prove_id = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        prove_nom = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        saldo_do = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        saldo_so = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Estado de cuenta Proveedor");

        jLabel2.setText("PROVEEDOR:");

        prove_nom.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel3.setText("SALDO DO:");

        saldo_do.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        jLabel4.setText("SALDO SO:");

        saldo_so.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(prove_nom, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(saldo_so, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(saldo_do, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(420, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(prove_id, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(prove_id, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(prove_nom, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(saldo_do, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(saldo_so, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 58, Short.MAX_VALUE))
        );

        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "N", "ID", "NOMBRES", "GLOSA", "DEBITO DO", "DEBITO SO", "CREDITO DO", "CREDITO SO", "FECHA"
            }
        ));
        jScrollPane1.setViewportView(tabla);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ProveEstadoCtaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ProveEstadoCtaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ProveEstadoCtaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ProveEstadoCtaDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ProveEstadoCtaDialog dialog = new ProveEstadoCtaDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JLabel prove_id;
    private javax.swing.JLabel prove_nom;
    private javax.swing.JLabel saldo_do;
    private javax.swing.JLabel saldo_so;
    private javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
