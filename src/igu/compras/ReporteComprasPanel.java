/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package igu.compras;

import data.reports.ComprasProveMovReport;
import data.ProveedorData;
import data.reports.ProveSaldoReport;
import java.io.IOException;
import igu.util.tables.ExportarExcel;
import entites.Proveedor;
import entites.views.ComprasProveMov;
import entites.views.ProveSaldo;
import igu.princ.CambiaPanel;
import igu.princ.MainFrame;
import igu.princ.Validate;

import igu.util.tables.EstiloTablaHeader;
import igu.util.tables.MyScrollbarUI;
import igu.util.Config;
import igu.util.PrintTicketera;
import igu.util.alerts.ErrorAlert;
import igu.util.tables.EstiloTablaFootRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultListModel;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Asullom
 */
public class ReporteComprasPanel extends javax.swing.JPanel {

    ExportarExcel obj;
    SimpleDateFormat iguSDF = new SimpleDateFormat(Config.DEFAULT_DATE_STRING_FORMAT_PE);

    DefaultListModel<Proveedor> defaultListModel = new DefaultListModel();
    DefaultListModel defaultListModelValue = new DefaultListModel();

    public ReporteComprasPanel() {

        initComponents();

        paintParams(1);
        Date date_i = new Date();

        fechaIniChooser.setDate(date_i);
        fechaChooser.setDate(date_i);
        nombres.requestFocus();
        prove_id.setText("");
        myJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myJList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (myJList.getSelectedIndex() != -1) {
                    nombres.setText(myJList.getSelectedValue());
                    prove_id.setText(defaultListModel.getElementAt(myJList.getSelectedIndex()).getId() + "");
                    try {
                        Validate.proveIdSelected = Integer.parseInt(prove_id.getText());
                    } catch (NumberFormatException e1) {
                        System.err.println("err" + e1);
                    }
                    ProveSaldo pro = ProveSaldoReport.getSaldoById(defaultListModel.getElementAt(myJList.getSelectedIndex()).getId());
                    saldo_do.setText(new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(pro.getSaldo_do()));
                    saldo_so.setText(new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(pro.getSaldo_so()));

                } else {
                    System.out.println("Sin resultados");
                }
            }
        });
        paintList("");

        tabla.getTableHeader().setDefaultRenderer(new EstiloTablaHeader());
        tabla.setDefaultRenderer(Object.class, new EstiloTablaFootRenderer());

        jScrollPane1.getViewport().setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.getVerticalScrollBar().setUI(new MyScrollbarUI());
        jScrollPane1.getHorizontalScrollBar().setUI(new MyScrollbarUI());

        id.setText("");
        paintTable("", fechaIniChooser.getDate(), fechaChooser.getDate(), "");

        tabla.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (tabla.getRowCount() > 0) {
                    int[] row = tabla.getSelectedRows();
                    if (row.length > 0) {
                        limpiarSoloCampos();
                        id.setText("" + tabla.getValueAt(row[0], 1));
                        if (!id.getText().equals("")) {
                            nombres.setText("" + tabla.getValueAt(row[0], 2));

                            try {
                                Date datex = iguSDF.parse("" + tabla.getValueAt(row[0], 10));
                                System.out.println("list.date:" + datex);
                            } catch (Exception de) {
                            }

                        }
                    }
                } else {
                    System.out.println("eee");
                }
            }

        });

    }

    private void paintParams(int id) {

    }

    //Search/Filter proveedores
    private void paintList(String buscar) {
        DefaultListModel<Proveedor> filteredItems = new DefaultListModel();
        DefaultListModel filteredItemsValue = new DefaultListModel();
        ProveedorData.list(buscar).stream().forEach((d) -> {
            filteredItemsValue.addElement(d.getNombres());
            filteredItems.addElement(d);
        });
        defaultListModel = filteredItems;
        defaultListModelValue = filteredItemsValue;
        myJList.setModel(defaultListModelValue);
    }

    private void paintTable(String prove_id, Date datei, Date date, String buscar) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();

        List<ComprasProveMov> lis = null;
        try {
            lis = ComprasProveMovReport.list(Integer.parseInt(prove_id), datei, date, buscar);
        } catch (NumberFormatException e1) {
            lis = ComprasProveMovReport.list(0, datei, date, buscar);
        }

        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
        String datos[] = new String[20];
        int cont = 0;
        double scant_gr = 0;
        double stotal_do = 0;
        double stotal_so = 0;
        double ssaldo_do_porpagar = 0;
        double ssaldo_so_porpagar = 0;
        double segreso_do = 0;
        double segreso_so = 0;
        double singreso_do = 0;
        double singreso_so = 0;
        for (ComprasProveMov d : lis) {
            scant_gr = scant_gr + d.getIngreso_cant_gr();
            stotal_do = stotal_do + d.getTotal_do();
            stotal_so = stotal_so + d.getTotal_so();
            ssaldo_do_porpagar = ssaldo_do_porpagar + d.getSaldo_porpagar_do();
            ssaldo_so_porpagar = ssaldo_so_porpagar + d.getSaldo_porpagar_so();
            segreso_do = segreso_do + d.getEgreso_do();
            segreso_so = segreso_so + d.getEgreso_so();
            singreso_do = singreso_do + d.getIngreso_do();
            singreso_so = singreso_so + d.getIngreso_so();
            datos[0] = ++cont + "";
            datos[1] = d.getId() + "";
            datos[2] = d.getProve_nom();
            datos[3] = d.getIngreso_cant_gr() == 0 ? "" : new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getIngreso_cant_gr()) + "";
            datos[4] = d.getOnza() == 0 ? "" : new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getOnza()) + "";
            datos[5] = d.getPorc() == 0 ? "" : new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getPorc()) + "";
            datos[6] = d.getLey() == 0 ? "" : new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getLey()) + "";
            datos[7] = d.getTcambio() == 0 ? "" : new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getTcambio()) + "";
            datos[8] = d.getPrecio_do() == 0 ? "" : new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getPrecio_do()) + "";
            datos[9] = d.getPrecio_so() == 0 ? "" : new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getPrecio_so()) + "";
            datos[10] = d.getTotal_do() == 0 ? "" : new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getTotal_do()) + "";
            datos[11] = d.getTotal_so() == 0 ? "" : new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getTotal_so()) + "";
            datos[12] = d.getSaldo_porpagar_do() == 0 ? "" : new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getSaldo_porpagar_do()) + "";
            datos[13] = d.getSaldo_porpagar_so() == 0 ? "" : new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getSaldo_porpagar_so()) + "";
            datos[14] = d.getEgreso_do() == 0 ? "" : new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getEgreso_do()) + "";
            datos[15] = d.getEgreso_so() == 0 ? "" : new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getEgreso_so()) + "";
            datos[16] = d.getIngreso_do() == 0 ? "" : new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getIngreso_do()) + "";
            datos[17] = d.getIngreso_so() == 0 ? "" : new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getIngreso_so()) + "";

            datos[18] = iguSDF.format(d.getFecha());
            datos[19] = d.getGlosa();
            modelo.addRow(datos);
        }
        datos[0] = "";
        datos[1] = "";
        datos[2] = "SUMAS";
        datos[3] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(scant_gr) + "";
        datos[4] = "";
        datos[5] = "";
        datos[6] = "";
        datos[7] = "";
        datos[8] = "";
        datos[9] = "";
        datos[10] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(stotal_do) + "";
        datos[11] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(stotal_so) + "";
        datos[12] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(ssaldo_do_porpagar) + "";
        datos[13] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(ssaldo_so_porpagar) + "";
        datos[14] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(segreso_do) + "";
        datos[15] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(segreso_so) + "";
        datos[16] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(singreso_do) + "";
        datos[17] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(singreso_so) + "";
        datos[18] = "";
        datos[19] = "";
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
        DefaultTableCellRenderer rightRenderer = new EstiloTablaFootRenderer("numerico");
        tabla.getColumnModel().getColumn(3).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);
        tabla.getColumnModel().getColumn(6).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(6).setCellRenderer(rightRenderer);
        tabla.getColumnModel().getColumn(7).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(7).setCellRenderer(rightRenderer);
        tabla.getColumnModel().getColumn(8).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(8).setCellRenderer(rightRenderer);
        tabla.getColumnModel().getColumn(9).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(9).setCellRenderer(rightRenderer);

        tabla.getColumnModel().getColumn(10).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(10).setCellRenderer(rightRenderer);
        tabla.getColumnModel().getColumn(11).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(11).setCellRenderer(rightRenderer);
        tabla.getColumnModel().getColumn(12).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(12).setCellRenderer(rightRenderer);
        tabla.getColumnModel().getColumn(13).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(13).setCellRenderer(rightRenderer);
        tabla.getColumnModel().getColumn(14).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(14).setCellRenderer(rightRenderer);
        tabla.getColumnModel().getColumn(15).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(15).setCellRenderer(rightRenderer);
        tabla.getColumnModel().getColumn(16).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(16).setCellRenderer(rightRenderer);
        tabla.getColumnModel().getColumn(17).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(17).setCellRenderer(rightRenderer);

        tabla.getColumnModel().getColumn(18).setPreferredWidth(10);
        tabla.getColumnModel().getColumn(18).setCellRenderer(new EstiloTablaFootRenderer("fecha"));
        tabla.getColumnModel().getColumn(19).setPreferredWidth(35);
        tabla.getColumnModel().getColumn(19).setCellRenderer(new EstiloTablaFootRenderer("texto"));

    }

    private void limpiarSoloCampos() {
        id.setText("");
        prove_id.setText("");

        nombres.setText("");
        saldo_do.setText("");
        saldo_so.setText("");

    }

    private void limpiarCampos() {
        limpiarSoloCampos();
        paintTable("", fechaIniChooser.getDate(), fechaChooser.getDate(), "");
        nombres.requestFocus();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        monedaGroup = new javax.swing.ButtonGroup();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        buscarField = new javax.swing.JTextField();
        aSIconButton4 = new igu.util.buttons.ASIconButton();
        jLabel4 = new javax.swing.JLabel();
        fechaChooser = new com.toedter.calendar.JDateChooser();
        fechaIniChooser = new com.toedter.calendar.JDateChooser();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        nombres = new javax.swing.JTextField();
        id = new javax.swing.JLabel();
        prove_id = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        myJList = new javax.swing.JList<>();
        saldo_do = new javax.swing.JTextField();
        saldo_so = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        jPanel5.setBackground(new java.awt.Color(58, 159, 171));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("REPORTE DE COMPRAS");
        jLabel1.setToolTipText("");

        buscarField.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        buscarField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buscarFieldActionPerformed(evt);
            }
        });
        buscarField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                buscarFieldKeyReleased(evt);
            }
        });

        aSIconButton4.setText("Excel");
        aSIconButton4.setColorHover(new java.awt.Color(0, 102, 102));
        aSIconButton4.setColorNormal(new java.awt.Color(153, 153, 153));
        aSIconButton4.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        aSIconButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aSIconButton4ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        jLabel4.setText("Buscar.");

        fechaChooser.setDateFormatString("dd/MM/yyyy");
        fechaChooser.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        fechaChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fechaChooserPropertyChange(evt);
            }
        });

        fechaIniChooser.setDateFormatString("dd/MM/yyyy");
        fechaIniChooser.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        fechaIniChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fechaIniChooserPropertyChange(evt);
            }
        });

        jLabel6.setText("EXPORT");

        jLabel7.setText("HASTA.");

        jLabel8.setText("DESDE.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel1)
                        .addGap(69, 69, 69))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(745, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fechaIniChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(fechaChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buscarField, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(aSIconButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buscarField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(aSIconButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(fechaChooser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fechaIniChooser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(102, 255, 102));

        jPanel4.setBackground(new java.awt.Color(58, 159, 171));

        tabla.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Nº", "ID", "NOMBRES", "CANT. GR", "ONZA", "%", "LEY", "T.CAMB", "PREC. DOL", "PREC. SOL", "TOTAL DO", "TOTAL SO", "XPAGAR DO", "XPAGAR SO", "EGRESO DO", "EGRESO SO", "INGRESO DO", "INGRESO SO", "FECHA", "GLOSA"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla.setDoubleBuffered(true);
        tabla.setRowHeight(25);
        tabla.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tabla);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(58, 159, 171));

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel2.setText("PROVEEDOR: ");

        nombres.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        nombres.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nombresActionPerformed(evt);
            }
        });
        nombres.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                nombresKeyReleased(evt);
            }
        });

        id.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        id.setText("id");

        prove_id.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        prove_id.setText("prove_id");

        myJList.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        myJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                myJListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(myJList);

        saldo_do.setEditable(false);
        saldo_do.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        saldo_do.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        saldo_so.setEditable(false);
        saldo_so.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        saldo_so.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setText("SALDO ACTUAL:");

        jButton1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jButton1.setText("FILTRAR PROVE.");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("DÓLARES");

        jLabel5.setText("SOLES");

        jButton2.setText("VER ESTADO CTA.");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(prove_id))
                        .addComponent(nombres, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jLabel12)
                    .addComponent(jLabel3)
                    .addComponent(jLabel5)
                    .addComponent(id)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(saldo_so, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)
                        .addComponent(saldo_do, javax.swing.GroupLayout.Alignment.LEADING))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE))
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(prove_id, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nombres, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saldo_do, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saldo_so, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(id)
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(134, 134, 134))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void aSIconButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aSIconButton4ActionPerformed
        try {
            obj = new ExportarExcel();
            obj.exportarExcel(tabla);
        } catch (IOException ex) {
            Logger.getLogger(ComprasPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_aSIconButton4ActionPerformed

    private void buscarFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarFieldKeyReleased
        // TODO add your handling code here:
        paintTable("", fechaIniChooser.getDate(), fechaChooser.getDate(), buscarField.getText());
    }//GEN-LAST:event_buscarFieldKeyReleased

    private void buscarFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buscarFieldActionPerformed

    private void fechaChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fechaChooserPropertyChange
        // TODO add your handling code here:
        Date test = this.fechaChooser.getDate(); //"02/03/2020";
        System.out.println("panel.fechaaaaaaaaaaaaaa: " + test);
        paintTable("", fechaIniChooser.getDate(), fechaChooser.getDate(), buscarField.getText());

    }//GEN-LAST:event_fechaChooserPropertyChange

    private void fechaIniChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fechaIniChooserPropertyChange
        // TODO add your handling code here:
        // TODO add your handling code here:
        Date test = this.fechaChooser.getDate(); //"02/03/2020";
        System.out.println("panel.fechaaaaaaaaaaaaaa: " + test);
        paintTable("", fechaIniChooser.getDate(), fechaChooser.getDate(), buscarField.getText());

    }//GEN-LAST:event_fechaIniChooserPropertyChange

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //new CambiaPanel(MainFrame.pnlPrincipal, new AdelantosPanel());

        if (prove_id.getText().equals("")) {
            ErrorAlert er = new ErrorAlert(new JFrame(), true);
            er.titulo.setText("OOPS...");
            er.msj.setText("SELECCIONA UN");
            er.msj1.setText("PROVEEDOR");
            er.setVisible(true);
        } else {
            paintTable(prove_id.getText(), fechaIniChooser.getDate(), fechaChooser.getDate(), buscarField.getText());
        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void myJListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_myJListMouseClicked

        if (myJList.getSelectedIndex() != -1) {
            nombres.setText(myJList.getSelectedValue());
            prove_id.setText(defaultListModel.getElementAt(myJList.getSelectedIndex()).getId() + "");
            try {
                Validate.proveIdSelected = Integer.parseInt(prove_id.getText());
            } catch (NumberFormatException e1) {
                System.err.println("err" + e1);
            }
            ProveSaldo pro = ProveSaldoReport.getSaldoById(defaultListModel.getElementAt(myJList.getSelectedIndex()).getId());
            saldo_do.setText(new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(pro.getSaldo_do()));
            saldo_so.setText(new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(pro.getSaldo_so()));

        }
    }//GEN-LAST:event_myJListMouseClicked

    private void nombresKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombresKeyReleased
        // TODO add your handling code here:
        paintList(nombres.getText());
        prove_id.setText("");
        saldo_do.setText("");
        saldo_so.setText("");
    }//GEN-LAST:event_nombresKeyReleased

    private void nombresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nombresActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        if (prove_id.getText().equals("")) {
            ErrorAlert er = new ErrorAlert(new JFrame(), true);
            er.titulo.setText("OOPS...");
            er.msj.setText("SELECCIONA UN");
            er.msj1.setText("PROVEEDOR");
            er.setVisible(true);
        } else {
            ProveEstadoCtaDialog w = new ProveEstadoCtaDialog(new JFrame(), true);
            try {
                Validate.proveIdSelected = Integer.parseInt(prove_id.getText());
            } catch (NumberFormatException e1) {
                System.err.println("err" + e1);
            }
            //w.prove_id.setText(prove_id.getText());
            w.setVisible(true);
        }


    }//GEN-LAST:event_jButton2ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private igu.util.buttons.ASIconButton aSIconButton4;
    private javax.swing.JTextField buscarField;
    private com.toedter.calendar.JDateChooser fechaChooser;
    private com.toedter.calendar.JDateChooser fechaIniChooser;
    private javax.swing.JLabel id;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    public static javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.ButtonGroup monedaGroup;
    private javax.swing.JList<String> myJList;
    private javax.swing.JTextField nombres;
    private javax.swing.JLabel prove_id;
    private javax.swing.JTextField saldo_do;
    private javax.swing.JTextField saldo_so;
    public static javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables

}
