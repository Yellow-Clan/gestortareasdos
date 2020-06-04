/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package igu.compras;

import data.CompraData;
import data.ParametroData;
import data.ProveedorData;
import data.reports.ProveSaldoReport;
import entites.Compra;
import entites.Parametro;
import java.io.IOException;
import igu.util.tables.ExportarExcel;
import entites.Proveedor;
import entites.views.ProveSaldo;
import igu.princ.CambiaPanel;
import static igu.princ.MainFrame.pnlPrincipal;
import igu.princ.Validate;
import igu.util.alerts.ConfirmDialog;
import igu.util.alerts.ErrorAlert;
import igu.util.alerts.SuccessAlert;
import igu.util.tables.EstiloTablaHeader;
import igu.util.tables.EstiloTablaRenderer;
import igu.util.tables.MyScrollbarUI;
import igu.util.Config;
import javax.swing.ListSelectionModel;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultListModel;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author Asullom
 */
public class ComprasPanel extends javax.swing.JPanel {

    ExportarExcel obj;
    SimpleDateFormat iguSDF = new SimpleDateFormat(Config.DEFAULT_DATE_STRING_FORMAT_PE);

    DefaultListModel<Proveedor> defaultListModel = new DefaultListModel();
    DefaultListModel defaultListModelValue = new DefaultListModel();

    public ComprasPanel() {

        initComponents();

        paintParams(1);
        Date date_i = new Date();
        fecha.setText(iguSDF.format(date_i));
        nombres.requestFocus();
        prove_id.setText("");
        myJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        myJList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (myJList.getSelectedIndex() != -1) {
                    cant_gr.setText("");
                    nombres.setText(myJList.getSelectedValue());
                    prove_id.setText(defaultListModel.getElementAt(myJList.getSelectedIndex()).getId() + "");
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
        tabla.setDefaultRenderer(Object.class, new EstiloTablaRenderer());

        jScrollPane1.getViewport().setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.getVerticalScrollBar().setUI(new MyScrollbarUI());
        jScrollPane1.getHorizontalScrollBar().setUI(new MyScrollbarUI());

        id.setText("");
        paintTable("");

        tabla.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (tabla.getRowCount() > 0) {
                    int[] row = tabla.getSelectedRows();
                    if (row.length > 0) {

                        String ids = (String) tabla.getValueAt(row[0], 1);
                        id.setText("" + ids);
                        String nombress = (String) tabla.getValueAt(row[0], 2);
                        nombres.setText("" + nombress);
                        /*
                        String infoadics = (String) tabla.getValueAt(row[0], 3);
                        cant_gr.setText("" + infoadics);
                        String fechax = (String) tabla.getValueAt(row[0], 4);
                        fecha.setText("" + fechax);

                        System.out.println("Table element selected es: " + ids);
                        guardarButton.setText("MODIFICAR");
                         */
                    }
                } else {
                    System.out.println("eee");
                }
            }

        });

    }

    private void paintParams(int id) {
        Parametro p = ParametroData.getById(id);
        onza.setText(p.getOnza() + "");
        porc.setText(p.getPorc() + "");
        ley.setText(p.getLey() + "");
        sistema.setText(p.getSistema() + "");
        tcambio.setText(p.getTcambio() + "");
        precio_do.setText(p.getPrecio_do() + "");
        precio_so.setText(p.getPrecio_so() + "");

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

    private void paintTable(String buscar) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        List<Compra> lis = CompraData.list(buscar);
        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
        String datos[] = new String[5];
        int cont = 0;
        for (Compra d : lis) {
            datos[0] = ++cont + "";
            datos[1] = d.getId() + "";
            datos[2] = d.getProve_nom();
            datos[3] = d.getCant_gr() + "";
            datos[4] = iguSDF.format(d.getFecha());
            modelo.addRow(datos);
        }
        tabla.getColumnModel().getColumn(0).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(80);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(200);
    }

    private void limpiarCampos() {
        nombres.requestFocus();
        nombres.setText("");
        cant_gr.setText("");
        saldo_do.setText("");
        saldo_so.setText("");

        paintTable("");

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
        jPanel7 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        onza = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        porc = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        ley = new javax.swing.JTextField();
        tcambio = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        sistema = new javax.swing.JTextField();
        aSIconButton1 = new igu.util.buttons.ASIconButton();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        precio_do = new javax.swing.JTextField();
        precio_so = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        nuevoButton = new igu.util.buttons.ASIconButton();
        guardarButton = new igu.util.buttons.ASIconButton();
        eliminarButton = new igu.util.buttons.ASIconButton();
        jLabel2 = new javax.swing.JLabel();
        nombres = new javax.swing.JTextField();
        id = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        moneda_dolares = new javax.swing.JRadioButton();
        moneda_soles = new javax.swing.JRadioButton();
        prove_id = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        myJList = new javax.swing.JList<>();
        jLabel9 = new javax.swing.JLabel();
        precio = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        total = new javax.swing.JTextField();
        cant_gr_validate = new javax.swing.JLabel();
        saldo_porpagar = new javax.swing.JFormattedTextField();
        cant_gr = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        pagado = new javax.swing.JFormattedTextField();
        saldo_porpagar_validate = new javax.swing.JLabel();
        fecha = new javax.swing.JTextField();
        saldo_do = new javax.swing.JTextField();
        saldo_so = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        jPanel5.setBackground(new java.awt.Color(58, 159, 171));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("COMPRAS");

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
        jLabel4.setText("Buscar:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("ONZA");

        onza.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        onza.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        onza.setToolTipText("");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel13.setText("%");

        porc.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        porc.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        porc.setToolTipText("");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel14.setText("LEY");

        jLabel15.setText("TIPO-CAMBIO");

        ley.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        ley.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        tcambio.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tcambio.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel16.setText("SISTEMA");

        sistema.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        sistema.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        aSIconButton1.setText("MODIFICAR PARAMS");
        aSIconButton1.setColorHover(new java.awt.Color(102, 102, 102));
        aSIconButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aSIconButton1ActionPerformed(evt);
            }
        });

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel17.setText("DÓLARES");

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel18.setText("SOLES");

        precio_do.setEditable(false);
        precio_do.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        precio_so.setEditable(false);
        precio_so.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        precio_so.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        precio_so.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                precio_soActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(onza, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel3)))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel13))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(porc, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ley, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel14)))
                .addGap(5, 5, 5)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(tcambio, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sistema, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(precio_do, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(7, 7, 7)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(precio_so, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(aSIconButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel18))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(onza, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(precio_do, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(precio_so, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(aSIconButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addGap(16, 16, 16)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(sistema, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
                            .addComponent(tcambio)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel17)
                                .addComponent(jLabel18))
                            .addComponent(jLabel16))
                        .addGap(43, 43, 43))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(jLabel14))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 15, Short.MAX_VALUE)
                                .addComponent(porc, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(ley)))))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscarField, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(aSIconButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(buscarField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(aSIconButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 29, Short.MAX_VALUE)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(102, 255, 102));

        jPanel4.setBackground(new java.awt.Color(58, 159, 171));

        tabla.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Nº", "ID", "NOMBRES", "GR", "DOLARES", "SOLES", "FECHA"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tabla.setDoubleBuffered(true);
        tabla.setRowHeight(25);
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE)
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

        nuevoButton.setText("NUEVO");
        nuevoButton.setColorHover(new java.awt.Color(0, 102, 102));
        nuevoButton.setColorNormal(new java.awt.Color(153, 153, 153));
        nuevoButton.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        nuevoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoButtonActionPerformed(evt);
            }
        });

        guardarButton.setText("GUARDAR");
        guardarButton.setColorHover(new java.awt.Color(0, 102, 102));
        guardarButton.setColorNormal(new java.awt.Color(153, 153, 153));
        guardarButton.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        guardarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarButtonActionPerformed(evt);
            }
        });

        eliminarButton.setText("ELIMINAR");
        eliminarButton.setColorHover(new java.awt.Color(0, 102, 102));
        eliminarButton.setColorNormal(new java.awt.Color(153, 153, 153));
        eliminarButton.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        eliminarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("BUSCAR PROVEEDOR: ");

        nombres.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
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

        id.setText("id");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("FECHA : ");
        jLabel5.setToolTipText("");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("CANTIDAD EN GRAMOS: ");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("PAGAR EN: ");
        jLabel7.setToolTipText("");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("TOTAL: ");

        monedaGroup.add(moneda_dolares);
        moneda_dolares.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        moneda_dolares.setText("DÓLARES");
        moneda_dolares.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moneda_dolaresActionPerformed(evt);
            }
        });

        monedaGroup.add(moneda_soles);
        moneda_soles.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        moneda_soles.setText("SOLES");
        moneda_soles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moneda_solesActionPerformed(evt);
            }
        });

        prove_id.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        prove_id.setText("prove_id");

        myJList.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        myJList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                myJListMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(myJList);

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("SALDO POR PAGAR: ");

        precio.setEditable(false);
        precio.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        precio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel10.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("PRECIO: ");

        total.setEditable(false);
        total.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        cant_gr_validate.setForeground(new java.awt.Color(255, 0, 0));
        cant_gr_validate.setText(".");

        saldo_porpagar.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        saldo_porpagar.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        saldo_porpagar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        saldo_porpagar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                saldo_porpagarKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                saldo_porpagarKeyTyped(evt);
            }
        });

        cant_gr.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        cant_gr.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        cant_gr.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cant_gr.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cant_grKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cant_grKeyTyped(evt);
            }
        });

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel11.setText(" A PAGAR");
        jLabel11.setToolTipText("se descontará el adelanto en caso tuviese");

        pagado.setEditable(false);
        pagado.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        pagado.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        saldo_porpagar_validate.setForeground(new java.awt.Color(255, 0, 0));
        saldo_porpagar_validate.setText(".");

        fecha.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        saldo_do.setEditable(false);
        saldo_do.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        saldo_do.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        saldo_so.setEditable(false);
        saldo_so.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        saldo_so.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setText("SALDO ACTUAL:");

        jLabel19.setText("\"dd/MM/yyyy\"");

        jLabel20.setText("Para cobrar deuda ir al MENÚ");

        jButton1.setText("MOV. PROVE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(cant_gr, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(nombres, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGap(18, 18, 18))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jLabel12)
                                        .addGap(31, 31, 31)))
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(saldo_porpagar_validate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(fecha)
                                    .addComponent(total)
                                    .addComponent(precio)
                                    .addComponent(cant_gr_validate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(saldo_porpagar, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(moneda_dolares)
                                            .addComponent(saldo_do, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(moneda_soles, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(saldo_so, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(pagado, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel20)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(52, 52, 52)
                                        .addComponent(jButton1)))))
                        .addGap(192, 192, 192))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(nuevoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(guardarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(eliminarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(id)
                        .addGap(14, 14, 14))))
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(prove_id)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(id)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(nuevoButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(eliminarButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(guardarButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(11, 11, 11)
                .addComponent(prove_id, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nombres, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6)
                            .addComponent(cant_gr, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cant_gr_validate))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(moneda_soles)
                    .addComponent(moneda_dolares))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(saldo_do, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel12)
                                .addComponent(saldo_so, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(precio, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel20)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(total, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saldo_porpagar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel9)
                        .addComponent(pagado, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(7, 7, 7)
                .addComponent(saldo_porpagar_validate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void eliminarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarButtonActionPerformed
        if (tabla.getRowCount() < 1) {
            ErrorAlert er = new ErrorAlert(new JFrame(), true);
            er.titulo.setText("OOPS...");
            er.msj.setText("LA TABLA ESTA VACÍA");
            er.msj1.setText("");
            er.setVisible(true);
        } else {
            System.out.println("id: " + id.getText());
            if (id.getText().equals("")) {
                ErrorAlert er = new ErrorAlert(new JFrame(), true);
                er.titulo.setText("OOPS...");
                er.msj.setText("SELECCIONA UN");
                er.msj1.setText("REGISTRO");
                er.setVisible(true);
            } else {
                System.out.println("DELETE ");
                ConfirmDialog cd = new ConfirmDialog(new JFrame(), true);
                cd.titulo.setText("DELETE...");
                cd.msj.setText("ESTAS SEGURO ELIMINAR?!");
                cd.msj1.setText("");
                cd.setVisible(true);
                if (cd.YES_OPTION) {
                    int opcion = CompraData.eliminar(Integer.parseInt(id.getText()));
                    if (opcion != 0) {
                        limpiarCampos();
                        id.setText("");
                        nombres.setText("");
                        cant_gr.setText("");
                        guardarButton.setText("REGISTRAR");
                    }
                }
            }
        }

    }//GEN-LAST:event_eliminarButtonActionPerformed

    private void nuevoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoButtonActionPerformed
        // TODO add your handling code here:
        //this.tituloLabel.setText("REGISTRAR");
        guardarButton.setText("REGISTRAR");
        id.setText("");
        nombres.setText("");
        cant_gr.setText("");
        nombres.requestFocus();
        prove_id.setText("");
        saldo_porpagar.setText("");
        saldo_do.setText("");
        saldo_so.setText("");

    }//GEN-LAST:event_nuevoButtonActionPerformed

    private void guardarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarButtonActionPerformed

        if (nombres.getText().equals("") || prove_id.getText().equals("") || cant_gr.getText().equals("")
                || total.getText().equals("") || fecha.getText().equals("")
                || tcambio.getText().equals("") || onza.getText().equals("")
                || porc.getText().equals("") || ley.getText().equals("")
                || sistema.getText().equals("")
                || precio_do.getText().equals("") || precio_so.getText().equals("")) {
            ErrorAlert er = new ErrorAlert(new JFrame(), true);
            er.titulo.setText("OOPS...");
            er.msj.setText("FALTAN COMPLETAR CAMPOS");
            er.msj1.setText("");
            er.setVisible(true);

        } else {
            boolean continuar = false;
            System.out.println("id: " + id.getText());
            Compra s = new Compra();
            s.setUser(Validate.userId);
            s.setProve_id(Integer.parseInt(prove_id.getText()));
            s.setProve_nom(nombres.getText());
            s.setCant_gr(Double.parseDouble(cant_gr.getText().replaceAll(",", "")));

            Date date = new Date();
            String test = this.fecha.getText(); //"02/03/2020";
            System.out.println("panel.fecha: " + test);
            iguSDF.setLenient(false);
            try {
                date = iguSDF.parse(test);
                if (!iguSDF.format(date).equals(test)) {
                    continuar = false;
                    throw new ParseException(test + " is not a valid format for " + Config.DEFAULT_DATE_STRING_FORMAT_PE, 0);
                } else {
                    continuar = true;
                    s.setFecha(date);
                }
            } catch (ParseException ex1) {
                System.out.println("panel.ParseException error: " + ex1);
                ErrorAlert er = new ErrorAlert(new JFrame(), true);
                er.titulo.setText("OOPS...");
                er.msj.setText("FECHA NO VÁLIDO " + ex1);
                er.msj1.setText("" + test + " is not a valid format for " + Config.DEFAULT_DATE_STRING_FORMAT_PE);
                er.setVisible(true);
            }

            if (moneda_soles.isSelected()) {
                s.setEsdolares(0);
                s.setPrecio_so(Double.parseDouble(precio_so.getText()));
                s.setPrecio_do(0);
                s.setTotal_so(Double.parseDouble(total.getText().replaceAll(",", "")));
                s.setTotal_do(0);
                // saldo_porpagar
                if (saldo_porpagar.getText().equals("")) {
                    s.setSaldo_so_porpagar(0);
                    s.setSaldo_do_porpagar(0);
                } else {
                    s.setSaldo_so_porpagar(Double.parseDouble(saldo_porpagar.getText().replaceAll(",", "")));
                    s.setSaldo_do_porpagar(0);
                }
            } else {
                s.setEsdolares(1);
                s.setPrecio_so(0);
                s.setPrecio_do(Double.parseDouble(precio_do.getText()));
                s.setTotal_so(0);
                s.setTotal_do(Double.parseDouble(total.getText().replaceAll(",", "")));
                // saldo_porpagar
                if (saldo_porpagar.getText().equals("")) {
                    s.setSaldo_so_porpagar(0);
                    s.setSaldo_do_porpagar(0);
                } else {
                    s.setSaldo_so_porpagar(0);
                    s.setSaldo_do_porpagar(Double.parseDouble(saldo_porpagar.getText().replaceAll(",", "")));
                }
            }

            s.setOnza(Double.parseDouble(onza.getText()));
            s.setPorc(Double.parseDouble(porc.getText()));
            s.setLey(Double.parseDouble(ley.getText()));
            s.setSistema(Double.parseDouble(sistema.getText()));
            s.setTcambio(Double.parseDouble(tcambio.getText()));

            if (continuar) {
                if (id.getText().equals("")) {
                    int opcion = CompraData.registrar(s);
                    if (opcion != 0) {
                        limpiarCampos();
                        SuccessAlert sa = new SuccessAlert(new JFrame(), true);
                        sa.titulo.setText("¡HECHO!");
                        sa.msj.setText("SE HA REGISTRADO UNA");
                        sa.msj1.setText("NUEVA COMPRA ");
                        sa.setVisible(true);
                    }
                } else {
                    s.setId(Integer.parseInt(id.getText()));
                    int opcion = CompraData.actualizar(s);
                    if (opcion != 0) {
                        limpiarCampos();
                        SuccessAlert sa = new SuccessAlert(new JFrame(), true);
                        sa.titulo.setText("¡HECHO!");
                        sa.msj.setText("SE HAN GUARDADO LOS CAMBIOS");
                        sa.msj1.setText("EN COMPRA");
                        sa.setVisible(true);
                    }
                }

            }

        }
    }//GEN-LAST:event_guardarButtonActionPerformed

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
        paintTable(buscarField.getText());
    }//GEN-LAST:event_buscarFieldKeyReleased

    private void moneda_dolaresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moneda_dolaresActionPerformed
        // TODO add your handling code here:
        precio.setText(precio_do.getText());
        saldo_porpagar.setText("");
        pagado.setText("");
        if (cant_gr.getText().equals("")) {
            ErrorAlert er = new ErrorAlert(new JFrame(), true);
            er.titulo.setText("OOPS...");
            er.msj.setText("FALTAN LLENAR CANTIDAD EN GRAMOS");
            er.msj1.setText("");
            er.setVisible(true);
            cant_gr.requestFocus();
        } else {
            try {
                double totalx = Math.round(Double.parseDouble(precio_do.getText()) * Double.parseDouble(cant_gr.getText().replaceAll(",", "")) * 100.0) / 100.0;
                total.setText(new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(totalx));
            } catch (NumberFormatException nfe) {
                System.err.println("" + nfe);
            }
        }
    }//GEN-LAST:event_moneda_dolaresActionPerformed

    private void moneda_solesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moneda_solesActionPerformed
        // TODO add your handling code here:
        precio.setText(precio_so.getText());
        saldo_porpagar.setText("");
        pagado.setText("");
        if (cant_gr.getText().equals("")) {
            ErrorAlert er = new ErrorAlert(new JFrame(), true);
            er.titulo.setText("OOPS...");
            er.msj.setText("FALTAN LLENAR CANTIDAD EN GRAMOS");
            er.msj1.setText("");
            er.setVisible(true);
            cant_gr.requestFocus();
        } else {
            try {
                double totalx = Math.round(Double.parseDouble(precio_so.getText()) * Double.parseDouble(cant_gr.getText().replaceAll(",", "")) * 100.0) / 100.0;
                total.setText(new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(totalx));
            } catch (NumberFormatException nfe) {
                System.err.println("" + nfe);
            }
        }
    }//GEN-LAST:event_moneda_solesActionPerformed

    private void nombresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nombresActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nombresActionPerformed

    private void buscarFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buscarFieldActionPerformed

    private void nombresKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombresKeyReleased
        // TODO add your handling code here:
        paintList(nombres.getText());
        prove_id.setText("");
        saldo_do.setText("");
        saldo_so.setText("");
    }//GEN-LAST:event_nombresKeyReleased

    private void myJListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_myJListMouseClicked

        if (myJList.getSelectedIndex() != -1) {
            cant_gr.setText("");
            nombres.setText(myJList.getSelectedValue());
            prove_id.setText(defaultListModel.getElementAt(myJList.getSelectedIndex()).getId() + "");
            ProveSaldo pro = ProveSaldoReport.getSaldoById(defaultListModel.getElementAt(myJList.getSelectedIndex()).getId());
            saldo_do.setText(new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(pro.getSaldo_do()));
            saldo_so.setText(new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(pro.getSaldo_so()));

        }

    }//GEN-LAST:event_myJListMouseClicked

    private void aSIconButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aSIconButton1ActionPerformed
        if (onza.getText().equals("") || porc.getText().equals("") || ley.getText().equals("") || sistema.getText().equals("") || tcambio.getText().equals("")) {
            ErrorAlert er = new ErrorAlert(new JFrame(), true);
            er.titulo.setText("OOPS...");
            er.msj.setText("FALTAN LLENAR CAMPOS DE LOS PARÁMETROS");
            er.msj1.setText("");
            er.setVisible(true);
        } else {
            Parametro p = new Parametro();
            p.setId(1);
            p.setOnza(Double.parseDouble(onza.getText()));
            p.setPorc(Double.parseDouble(porc.getText()));
            p.setLey(Double.parseDouble(ley.getText()));
            p.setSistema(Double.parseDouble(sistema.getText()));
            p.setTcambio(Double.parseDouble(tcambio.getText()));

            int opcion = ParametroData.actualizar(p);
            if (opcion != 0) {
                SuccessAlert sa = new SuccessAlert(new JFrame(), true);
                sa.titulo.setText("¡HECHO!");
                sa.msj.setText("SE HAN GUARDADO LOS CAMBIOS");
                sa.msj1.setText("EN PARÁMETRO");
                sa.setVisible(true);
                paintParams(1);

                if (!cant_gr.getText().equals("")) {
                    try {
                        if (moneda_soles.isSelected()) {
                            precio.setText(precio_so.getText());
                            double totalx = Math.round(Double.parseDouble(precio_so.getText()) * Double.parseDouble(cant_gr.getText().replaceAll(",", "")) * 100.0) / 100.0;
                            total.setText(new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(totalx));
                        } else {
                            precio.setText(precio_do.getText());
                            double totalx = Math.round(Double.parseDouble(precio_do.getText()) * Double.parseDouble(cant_gr.getText().replaceAll(",", "")) * 100.0) / 100.0;
                            total.setText(new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(totalx));
                            moneda_dolares.setSelected(true);
                        }
                    } catch (NumberFormatException nfe) {
                        System.err.println("" + nfe);
                    }
                }
                saldo_porpagar.setText("");
                pagado.setText("");
            }
        }
    }//GEN-LAST:event_aSIconButton1ActionPerformed

    private void precio_soActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_precio_soActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_precio_soActionPerformed

    private void saldo_porpagarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saldo_porpagarKeyReleased
        // TODO add your handling code here:
        saldo_porpagar_validate.setText("");
        if (!cant_gr.getText().equals("")) {
            try {
                double pagadox = Double.parseDouble(total.getText().replaceAll(",", "")) - Double.parseDouble(saldo_porpagar.getText().replaceAll(",", ""));
                pagado.setText(new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(pagadox) + "");
            } catch (NumberFormatException nfe) {
                System.err.println("" + nfe);
                saldo_porpagar_validate.setText("Número no válido");
            }
        }
    }//GEN-LAST:event_saldo_porpagarKeyReleased

    private void cant_grKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cant_grKeyReleased
        // TODO add your handling code here:
        cant_gr_validate.setText("");
        if (!cant_gr.getText().equals("")) {
            try {
                if (moneda_soles.isSelected()) {
                    precio.setText(precio_so.getText());
                    double totalx = Math.round(Double.parseDouble(precio_so.getText()) * Double.parseDouble(cant_gr.getText().replaceAll(",", "")) * 100.0) / 100.0;
                    total.setText(new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(totalx));
                } else {
                    precio.setText(precio_do.getText());
                    double totalx = Math.round(Double.parseDouble(precio_do.getText()) * Double.parseDouble(cant_gr.getText().replaceAll(",", "")) * 100.0) / 100.0;
                    total.setText(new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(totalx));
                    moneda_dolares.setSelected(true);
                }
                saldo_porpagar.setText("");
                pagado.setText("");
            } catch (NumberFormatException nfe) {
                System.err.println("" + nfe);
                cant_gr_validate.setText("Número no válido");
            }
        }
    }//GEN-LAST:event_cant_grKeyReleased

    private void cant_grKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cant_grKeyTyped
        // TODO add your handling code here:
        String filterStr = "0123456789.";
        char c = (char) evt.getKeyChar();
        if (filterStr.indexOf(c) < 0) {
            evt.consume();
        }
    }//GEN-LAST:event_cant_grKeyTyped

    private void saldo_porpagarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saldo_porpagarKeyTyped
        // TODO add your handling code here:
        String filterStr = "0123456789.";
        char c = (char) evt.getKeyChar();
        if (filterStr.indexOf(c) < 0) {
            evt.consume();
        }
    }//GEN-LAST:event_saldo_porpagarKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        new CambiaPanel(pnlPrincipal, new AdelantosPanel());
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private igu.util.buttons.ASIconButton aSIconButton1;
    private igu.util.buttons.ASIconButton aSIconButton4;
    private javax.swing.JTextField buscarField;
    private javax.swing.JFormattedTextField cant_gr;
    private javax.swing.JLabel cant_gr_validate;
    private igu.util.buttons.ASIconButton eliminarButton;
    private javax.swing.JTextField fecha;
    private igu.util.buttons.ASIconButton guardarButton;
    private javax.swing.JLabel id;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    public static javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField ley;
    private javax.swing.ButtonGroup monedaGroup;
    private javax.swing.JRadioButton moneda_dolares;
    private javax.swing.JRadioButton moneda_soles;
    private javax.swing.JList<String> myJList;
    private javax.swing.JTextField nombres;
    private igu.util.buttons.ASIconButton nuevoButton;
    private javax.swing.JTextField onza;
    private javax.swing.JFormattedTextField pagado;
    private javax.swing.JTextField porc;
    private javax.swing.JTextField precio;
    private javax.swing.JTextField precio_do;
    private javax.swing.JTextField precio_so;
    private javax.swing.JLabel prove_id;
    private javax.swing.JTextField saldo_do;
    private javax.swing.JFormattedTextField saldo_porpagar;
    private javax.swing.JLabel saldo_porpagar_validate;
    private javax.swing.JTextField saldo_so;
    private javax.swing.JTextField sistema;
    public static javax.swing.JTable tabla;
    private javax.swing.JTextField tcambio;
    private javax.swing.JTextField total;
    // End of variables declaration//GEN-END:variables

}
