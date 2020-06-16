/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package igu.compras;

import data.ProveMovData;
import data.ProveedorData;
import data.reports.ProveSaldoReport;
import entites.ProveMov;
import java.io.IOException;
import igu.util.tables.ExportarExcel;
import entites.Proveedor;
import entites.views.ProveSaldo;
import igu.princ.Validate;
import igu.util.alerts.ConfirmDialog;
import igu.util.alerts.ErrorAlert;
import igu.util.alerts.SuccessAlert;
import igu.util.tables.EstiloTablaHeader;
import igu.util.tables.MyScrollbarUI;
import igu.util.Config;
import igu.util.PrintTicketera;
import igu.util.tables.EstiloTablaFootRenderer;
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
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Asullom
 */
public class AdelantosPanel extends javax.swing.JPanel {

    ExportarExcel obj;
    SimpleDateFormat iguSDF = new SimpleDateFormat(Config.DEFAULT_DATE_STRING_FORMAT_PE);

    DefaultListModel<Proveedor> defaultListModel = new DefaultListModel();
    DefaultListModel defaultListModelValue = new DefaultListModel();

    public AdelantosPanel() {

        initComponents();

        Date date_i = new Date();
        fecha.setDate(date_i);
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
        paintTable(fechaIniChooser.getDate(), fechaChooser.getDate(), "");

        tabla.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (tabla.getRowCount() > 0) {
                    int[] row = tabla.getSelectedRows();
                    if (row.length > 0) {

                        id.setText("" + tabla.getValueAt(row[0], 1));
                        if (!id.getText().equals("")) {
                            nombres.setText("" + tabla.getValueAt(row[0], 2));
                            glosa.setText("" + tabla.getValueAt(row[0], 3));

                            try {
                                Date datex = iguSDF.parse("" + tabla.getValueAt(row[0], 8));
                                System.out.println("list.date:" + datex);
                                fecha.setDate(datex);
                            } catch (Exception de) {
                            }

                            guardarButton.setText("MODIFICAR NO SE PUEDE");
                            guardarButton.setToolTipText("MODIFICAR NO SE PUEDE, ELIMINE Y VUELA A INGRESAR");
                            guardarButton.setEnabled(false);
                            guardarButton.setSelected(false);
                        }
                    }
                } else {
                    System.out.println("eee");
                }
            }

        });

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

    private void paintTable(Date datei,Date date, String buscar) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        List<ProveMov> lis = ProveMovData.list(datei, date, buscar);
        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
        String datos[] = new String[9];
        int cont = 0;
        double sadelanto_do = 0;
        double sadelanto_so = 0;
        double scobro_do = 0;
        double scobro_so = 0;

        for (ProveMov d : lis) {
            sadelanto_do = sadelanto_do + d.getAdelanto_do();
            sadelanto_so = sadelanto_so + d.getAdelanto_so();
            scobro_do = scobro_do + d.getCobro_do();
            scobro_so = scobro_so + d.getCobro_so();
            datos[0] = ++cont + "";
            datos[1] = d.getId() + "";
            datos[2] = d.getProve_nom();
            datos[3] = d.getGlosa() + "";
            datos[4] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getAdelanto_do());
            datos[5] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getAdelanto_so());
            datos[6] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getCobro_do());
            datos[7] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getCobro_so());

            datos[8] = iguSDF.format(d.getFecha());
            modelo.addRow(datos);
        }
        datos[0] = "";
        datos[1] = "";
        datos[2] = "";
        datos[3] = "SUMAS";
        datos[4] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(sadelanto_do) + "";
        datos[5] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(sadelanto_so) + "";
        datos[6] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(scobro_do) + "";
        datos[7] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(scobro_so) + "";
        datos[8] = "";
        modelo.addRow(datos);

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

    private void limpiarSoloCampos() {
        id.setText("");
        prove_id.setText("");
        nombres.setText("");
        monto.setText("");
        saldo_do.setText("");
        saldo_so.setText("");
        glosa.setText("");
        nombres.requestFocus();

    }

    private void limpiarCampos() {

        limpiarSoloCampos();
        paintTable(fechaIniChooser.getDate(), fechaChooser.getDate(), "");

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
        movGroup = new javax.swing.ButtonGroup();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        buscarField = new javax.swing.JTextField();
        aSIconButton4 = new igu.util.buttons.ASIconButton();
        jLabel4 = new javax.swing.JLabel();
        fechaChooser = new com.toedter.calendar.JDateChooser();
        fechaIniChooser = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
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
        moneda_dolares = new javax.swing.JRadioButton();
        moneda_soles = new javax.swing.JRadioButton();
        prove_id = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        myJList = new javax.swing.JList<>();
        monto_validate = new javax.swing.JLabel();
        monto = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        mov_adelanto = new javax.swing.JRadioButton();
        mov_cobrar = new javax.swing.JRadioButton();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        glosa = new javax.swing.JTextArea();
        saldo_do = new javax.swing.JTextField();
        saldo_so = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        fecha = new com.toedter.calendar.JDateChooser();

        jPanel5.setBackground(new java.awt.Color(58, 159, 171));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("ADELANTOS/PAGOS Y COBROS A PROVEEDORES");
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
        jLabel4.setToolTipText("");

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

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setText("EXPORT");

        jLabel9.setText("HASTA");

        jLabel10.setText("DESDE.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fechaIniChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fechaChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buscarField, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
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
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10))
                        .addGap(5, 5, 5)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fechaChooser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(fechaIniChooser, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(buscarField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(aSIconButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(102, 255, 102));

        jPanel4.setBackground(new java.awt.Color(58, 159, 171));

        tabla.setFont(new java.awt.Font("Tahoma", 0, 16)); // NOI18N
        tabla.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Nº", "ID", "NOMBRES", "GLOSA", "ADELANTO/PAG. DO", "ADELANTO/PAG. SO", "COBRO DÓ", "COBRO SO", "FECHA"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 486, Short.MAX_VALUE)
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
        jLabel2.setText("PROVEEDOR: ");

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
        jLabel5.setText("FECHA: ");
        jLabel5.setToolTipText("");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("MONTO: ");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("MONEDA: ");
        jLabel7.setToolTipText("");

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

        monto_validate.setForeground(new java.awt.Color(255, 0, 0));
        monto_validate.setText(".");

        monto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        monto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        monto.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        monto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                montoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                montoKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel11.setText(" SALDO ACTUAL: ");

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("MOVIMIENTO: ");
        jLabel12.setToolTipText("");

        movGroup.add(mov_adelanto);
        mov_adelanto.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        mov_adelanto.setSelected(true);
        mov_adelanto.setText("ADELANTO/PAGO(egreso)");
        mov_adelanto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mov_adelantoActionPerformed(evt);
            }
        });

        movGroup.add(mov_cobrar);
        mov_cobrar.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        mov_cobrar.setText("COBRO (ingreso)");
        mov_cobrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mov_cobrarActionPerformed(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("GLOSA: ");
        jLabel13.setToolTipText("");

        glosa.setColumns(20);
        glosa.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        glosa.setRows(5);
        glosa.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane3.setViewportView(glosa);

        saldo_do.setEditable(false);
        saldo_do.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        saldo_do.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        saldo_so.setEditable(false);
        saldo_so.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        saldo_so.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel3.setText("\"dd/MM/yyyy\"");

        fecha.setDateFormatString("dd/MM/yyyy");
        fecha.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGap(0, 17, Short.MAX_VALUE)
                                .addComponent(jLabel11))
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(monto_validate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jScrollPane3)
                                            .addComponent(fecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addGap(12, 12, 12)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel3))))
                                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(saldo_do)
                                        .addComponent(moneda_dolares, javax.swing.GroupLayout.DEFAULT_SIZE, 134, Short.MAX_VALUE)))
                                .addGap(0, 99, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(saldo_so, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addComponent(mov_adelanto, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(13, 13, 13)
                                        .addComponent(mov_cobrar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(moneda_soles, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(nuevoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(guardarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(eliminarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addComponent(id))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE))
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel6Layout.createSequentialGroup()
                                        .addGap(35, 35, 35)
                                        .addComponent(nombres, javax.swing.GroupLayout.PREFERRED_SIZE, 226, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                        .addGap(74, 74, 74)
                                        .addComponent(monto, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addContainerGap())))
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
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(nombres, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6)
                            .addComponent(monto, javax.swing.GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(monto_validate))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(7, 7, 7)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(moneda_soles)
                    .addComponent(moneda_dolares))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(saldo_do, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saldo_so, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(mov_cobrar)
                    .addComponent(mov_adelanto)
                    .addComponent(jLabel12))
                .addGap(20, 20, 20)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel5)
                        .addComponent(jLabel3))
                    .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
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
                    int opcion = ProveMovData.eliminar(Integer.parseInt(id.getText()));
                    if (opcion != 0) {
                        limpiarCampos();
                        id.setText("");
                        nombres.setText("");
                        monto.setText("");
                        guardarButton.setText("REGISTRAR");
                        guardarButton.setToolTipText("REGISTRAR");
                    }
                }
            }
        }

    }//GEN-LAST:event_eliminarButtonActionPerformed

    private void nuevoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoButtonActionPerformed
        // TODO add your handling code here:
        //this.tituloLabel.setText("REGISTRAR");
        guardarButton.setText("REGISTRAR");
        guardarButton.setToolTipText("REGISTRAR");
        guardarButton.setEnabled(true);
        guardarButton.setSelected(true);
        limpiarSoloCampos();


    }//GEN-LAST:event_nuevoButtonActionPerformed

    private void guardarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarButtonActionPerformed

        if (nombres.getText().equals("") || prove_id.getText().equals("") || monto.getText().equals("")
                || fecha.getDate() == null) {
            ErrorAlert er = new ErrorAlert(new JFrame(), true);
            er.titulo.setText("OOPS...");
            er.msj.setText("FALTAN COMPLETAR CAMPOS");
            er.msj1.setText("");
            er.setVisible(true);

        } else {
            boolean continuar = true;
            System.out.println("id: " + id.getText());
            ProveMov s = new ProveMov();
            s.setUser(Validate.userId);
            s.setProve_id(Integer.parseInt(prove_id.getText()));
            s.setProve_nom(nombres.getText());
            s.setGlosa(glosa.getText());
            s.setFecha(fecha.getDate());

            if (moneda_soles.isSelected()) {
                s.setEsdolares(0);
                if (mov_cobrar.isSelected()) {
                    s.setEsadelanto(0);
                    s.setCobro_so(Double.parseDouble(monto.getText().replaceAll(",", "")));
                    s.setCobro_do(0);
                } else {
                    s.setEsadelanto(1);
                    s.setAdelanto_so(Double.parseDouble(monto.getText().replaceAll(",", "")));
                    s.setAdelanto_do(0);
                }
            } else {
                s.setEsdolares(1);
                if (mov_cobrar.isSelected()) {
                    s.setEsadelanto(0);
                    s.setCobro_so(0);
                    s.setCobro_do(Double.parseDouble(monto.getText().replaceAll(",", "")));

                } else {
                    s.setEsadelanto(1);
                    s.setAdelanto_so(0);
                    s.setAdelanto_do(Double.parseDouble(monto.getText().replaceAll(",", "")));
                }
            }

            if (continuar) {
                if (id.getText().equals("")) {
                    int rid = ProveMovData.registrar(s);
                    if (rid != 0) {
                        limpiarCampos();
                        SuccessAlert sa = new SuccessAlert(new JFrame(), true);
                        sa.titulo.setText("¡HECHO!");
                        sa.msj.setText("SE HA REGISTRADO UNA");
                        sa.msj1.setText("NUEVA COMPRA ");
                        sa.setVisible(true);

                        System.out.println("rid=" + rid);
                        ProveMov rc = ProveMovData.getById(rid);
                        System.out.println("rc.getProve_nom=" + rc.getProve_nom());
                        PrintTicketera.imp_prove_mov(rid);
                    }
                } else {
                    s.setId(Integer.parseInt(id.getText()));
                    int opcion = ProveMovData.actualizar(s);
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
            Logger.getLogger(AdelantosPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_aSIconButton4ActionPerformed

    private void buscarFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarFieldKeyReleased
        // TODO add your handling code here:
        paintTable(fechaIniChooser.getDate(), fechaChooser.getDate(), buscarField.getText());
    }//GEN-LAST:event_buscarFieldKeyReleased

    private void moneda_dolaresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moneda_dolaresActionPerformed
        // TODO add your handling code here:
        // precio.setText(precio_do.getText());
        // saldo_porpagar.setText("");
        //saldo.setText("");
        if (monto.getText().equals("")) {
            ErrorAlert er = new ErrorAlert(new JFrame(), true);
            er.titulo.setText("OOPS...");
            er.msj.setText("FALTAN LLENAR CANTIDAD EN GRAMOS");
            er.msj1.setText("");
            er.setVisible(true);
            monto.requestFocus();
        } else {
            try {
                //double totalx = Math.round(Double.parseDouble(precio_do.getText()) * Double.parseDouble(cant_gr.getText()) * 100.0) / 100.0;
                // total.setText(new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(totalx));
            } catch (NumberFormatException nfe) {
                System.err.println("" + nfe);
            }
        }
    }//GEN-LAST:event_moneda_dolaresActionPerformed

    private void moneda_solesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_moneda_solesActionPerformed
        // TODO add your handling code here:
        //precio.setText(precio_so.getText());
        // saldo_porpagar.setText("");
        //saldo.setText("");
        if (monto.getText().equals("")) {
            ErrorAlert er = new ErrorAlert(new JFrame(), true);
            er.titulo.setText("OOPS...");
            er.msj.setText("FALTAN LLENAR CANTIDAD EN GRAMOS");
            er.msj1.setText("");
            er.setVisible(true);
            monto.requestFocus();
        } else {
            try {
                //double totalx = Math.round(Double.parseDouble(precio_so.getText()) * Double.parseDouble(cant_gr.getText()) * 100.0) / 100.0;
                // total.setText(new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(totalx));
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
    }//GEN-LAST:event_nombresKeyReleased

    private void myJListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_myJListMouseClicked

        if (myJList.getSelectedIndex() != -1) {
            nombres.setText(myJList.getSelectedValue());
            prove_id.setText(defaultListModel.getElementAt(myJList.getSelectedIndex()).getId() + "");
            ProveSaldo pro = ProveSaldoReport.getSaldoById(defaultListModel.getElementAt(myJList.getSelectedIndex()).getId());
            saldo_do.setText(new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(pro.getSaldo_do()));
            saldo_so.setText(new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(pro.getSaldo_so()));
        }

    }//GEN-LAST:event_myJListMouseClicked

    private void montoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_montoKeyReleased
        // TODO add your handling code here:
        monto_validate.setText("");
        if (!monto.getText().equals("")) {
            try {
                if (moneda_soles.isSelected()) {
                    // precio.setText(precio_so.getText());
                    //  double totalx = Math.round(Double.parseDouble(precio_so.getText()) * Double.parseDouble(cant_gr.getText()) * 100.0) / 100.0;
                    //  total.setText(new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(totalx));
                } else {
                    //   precio.setText(precio_do.getText());
                    //   double totalx = Math.round(Double.parseDouble(precio_do.getText()) * Double.parseDouble(cant_gr.getText()) * 100.0) / 100.0;
                    //   total.setText(new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(totalx));
                    moneda_dolares.setSelected(true);
                    mov_adelanto.setSelected(true);
                }
                // saldo_porpagar.setText("");
                //saldo.setText("");
            } catch (NumberFormatException nfe) {
                System.err.println("" + nfe);
                monto_validate.setText("Número no válido");
            }
        }
    }//GEN-LAST:event_montoKeyReleased

    private void montoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_montoKeyTyped
        // TODO add your handling code here:
        String filterStr = "0123456789.";
        char c = (char) evt.getKeyChar();
        if (filterStr.indexOf(c) < 0) {
            evt.consume();
        }
    }//GEN-LAST:event_montoKeyTyped

    private void mov_adelantoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mov_adelantoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mov_adelantoActionPerformed

    private void mov_cobrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mov_cobrarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mov_cobrarActionPerformed

    private void fechaChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fechaChooserPropertyChange
        // TODO add your handling code here:
        paintTable(fechaIniChooser.getDate(), fechaChooser.getDate(), buscarField.getText());
    }//GEN-LAST:event_fechaChooserPropertyChange

    private void fechaIniChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fechaIniChooserPropertyChange
        // TODO add your handling code here:
        paintTable(fechaIniChooser.getDate(), fechaChooser.getDate(), buscarField.getText());
    }//GEN-LAST:event_fechaIniChooserPropertyChange


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private igu.util.buttons.ASIconButton aSIconButton4;
    private javax.swing.JTextField buscarField;
    private igu.util.buttons.ASIconButton eliminarButton;
    private com.toedter.calendar.JDateChooser fecha;
    private com.toedter.calendar.JDateChooser fechaChooser;
    private com.toedter.calendar.JDateChooser fechaIniChooser;
    private javax.swing.JTextArea glosa;
    private igu.util.buttons.ASIconButton guardarButton;
    private javax.swing.JLabel id;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
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
    public static javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.ButtonGroup monedaGroup;
    private javax.swing.JRadioButton moneda_dolares;
    private javax.swing.JRadioButton moneda_soles;
    private javax.swing.JFormattedTextField monto;
    private javax.swing.JLabel monto_validate;
    private javax.swing.ButtonGroup movGroup;
    private javax.swing.JRadioButton mov_adelanto;
    private javax.swing.JRadioButton mov_cobrar;
    private javax.swing.JList<String> myJList;
    private javax.swing.JTextField nombres;
    private igu.util.buttons.ASIconButton nuevoButton;
    private javax.swing.JLabel prove_id;
    private javax.swing.JTextField saldo_do;
    private javax.swing.JTextField saldo_so;
    public static javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables

}
