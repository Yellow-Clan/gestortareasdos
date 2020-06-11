/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package igu.princ;

import data.CajaAperCierreData;
import igu.util.tables.ExportarExcel;
import entites.CajaAperCierre;
import igu.util.alerts.ConfirmDialog;
import igu.util.alerts.ErrorAlert;
import igu.util.alerts.SuccessAlert;
import javax.swing.ListSelectionModel;

import igu.util.tables.EstiloTablaHeader;
import igu.util.tables.EstiloTablaRenderer;
import igu.util.tables.MyScrollbarUI;
import igu.util.Config;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Asullom
 */
public class AbrirCerrarCajaPanel extends javax.swing.JPanel {

    ExportarExcel obj;
    SimpleDateFormat iguSDF = new SimpleDateFormat(Config.DEFAULT_DATE_STRING_FORMAT_PE);
    

    public AbrirCerrarCajaPanel() {
        //fecha_nac= new JFormattedTextField( iguSDF );
        initComponents();

        Date date_i = new Date();
        fecha.setDate(date_i);
        fechaChooser.setDate(date_i);

        this.tabla.getTableHeader().setDefaultRenderer(new EstiloTablaHeader());
        this.tabla.setDefaultRenderer(Object.class, new EstiloTablaRenderer());

        jScrollPane1.getViewport().setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.getVerticalScrollBar().setUI(new MyScrollbarUI());
        jScrollPane1.getHorizontalScrollBar().setUI(new MyScrollbarUI());

        this.id.setText("");
        paintTable(fechaChooser.getDate(), "");

        tabla.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (tabla.getRowCount() > 0) {
                    int[] row = tabla.getSelectedRows();
                    if (row.length > 0) {

                        id.setText("" + tabla.getValueAt(row[0], 1));
                        if (!id.getText().equals("")) {
                            saldo_do.setText("" + tabla.getValueAt(row[0], 2));
                            saldo_so.setText("" + tabla.getValueAt(row[0], 3));
                            saldo_do_bancos.setText("" + tabla.getValueAt(row[0], 4));
                            saldo_so_bancos.setText("" + tabla.getValueAt(row[0], 5));
                            try {
                                Date datex = iguSDF.parse("" + tabla.getValueAt(row[0], 6));
                                System.out.println("list.date:" + datex);
                                fecha.setDate(datex);
                            } catch (Exception de) {
                            }

                            guardarButton.setText("MODIFICAR");
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

        this.saldo_do.requestFocus();

    }

    private void paintTable(Date date, String buscar) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        List<CajaAperCierre> lis = CajaAperCierreData.list(date, buscar);
        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
        String datos[] = new String[7];
        int cont = 0;
        for (CajaAperCierre d : lis) {
            datos[0] = ++cont + "";
            datos[1] = d.getId() + "";

            datos[2] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getSaldo_do());
            datos[3] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getSaldo_so());
            datos[4] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getSaldo_do_bancos());
            datos[5] = new DecimalFormat(Config.DEFAULT_DECIMAL_FORMAT).format(d.getSaldo_so_bancos());
            datos[6] = iguSDF.format(d.getFecha());
            modelo.addRow(datos);
        }
        tabla.getColumnModel().getColumn(0).setMaxWidth(35);
        tabla.getColumnModel().getColumn(0).setCellRenderer(new EstiloTablaRenderer("texto"));
        tabla.getColumnModel().getColumn(1).setMaxWidth(35);
        tabla.getColumnModel().getColumn(1).setCellRenderer(new EstiloTablaRenderer("texto"));

        DefaultTableCellRenderer rightRenderer = new EstiloTablaRenderer("numerico");
        tabla.getColumnModel().getColumn(2).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);
        tabla.getColumnModel().getColumn(3).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(3).setCellRenderer(rightRenderer);
        tabla.getColumnModel().getColumn(4).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
        tabla.getColumnModel().getColumn(5).setPreferredWidth(40);
        tabla.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);

        tabla.getColumnModel().getColumn(6).setPreferredWidth(10);
        tabla.getColumnModel().getColumn(6).setCellRenderer(new EstiloTablaRenderer("fecha"));
    }

    private void limpiarSoloCampos() {
        id.setText("");
        saldo_do.requestFocus();
        saldo_do.setText("");
        saldo_so.setText("");
        saldo_do_bancos.setText("");
        saldo_so_bancos.setText("");
    }

    private void limpiarCampos() {
        limpiarSoloCampos();

        paintTable(fechaChooser.getDate(), "");

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        opeGroup = new javax.swing.ButtonGroup();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        buscarField = new javax.swing.JTextField();
        aSIconButton4 = new igu.util.buttons.ASIconButton();
        jLabel4 = new javax.swing.JLabel();
        fechaChooser = new com.toedter.calendar.JDateChooser();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        nuevoButton = new igu.util.buttons.ASIconButton();
        guardarButton = new igu.util.buttons.ASIconButton();
        eliminarButton = new igu.util.buttons.ASIconButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        id = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        saldo_do_validate = new javax.swing.JLabel();
        saldo_so_validate = new javax.swing.JLabel();
        saldo_so_validate1 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        saldo_so_validate2 = new javax.swing.JLabel();
        saldo_do = new javax.swing.JFormattedTextField();
        saldo_so = new javax.swing.JFormattedTextField();
        saldo_do_bancos = new javax.swing.JFormattedTextField();
        saldo_so_bancos = new javax.swing.JFormattedTextField();
        saldo_so_validate3 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        abrir = new javax.swing.JRadioButton();
        cerrar = new javax.swing.JRadioButton();
        fecha = new com.toedter.calendar.JDateChooser();

        jPanel5.setBackground(new java.awt.Color(58, 159, 171));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("ABRIR Y CERRAR CAJA");

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

        fechaChooser.setDateFormatString("dd/MM/yyyy");
        fechaChooser.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        fechaChooser.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                fechaChooserPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(fechaChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
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
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addContainerGap(43, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(fechaChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(buscarField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(aSIconButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4)))
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
                "Nº", "ID", "MONTO DÓLARES", "MONTO SOLES", "EN BANCOS DÓLARES", "EN BANCO SOLES", "FECHA"
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 549, Short.MAX_VALUE)
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

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("SALDO EN SOLES:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("SALDO EN DOLARES:");

        id.setText("id");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("FECHA \"dd/MM/yyyy\"");
        jLabel5.setToolTipText("");

        saldo_do_validate.setForeground(new java.awt.Color(204, 0, 0));
        saldo_do_validate.setText(".");

        saldo_so_validate.setForeground(new java.awt.Color(204, 0, 0));
        saldo_so_validate.setText(".");

        saldo_so_validate1.setForeground(new java.awt.Color(204, 0, 0));
        saldo_so_validate1.setText(".");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("SALDO EN BANCOS DOLARES:");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("SALDO EN BANCOS SOLES:");

        saldo_so_validate2.setForeground(new java.awt.Color(204, 0, 0));
        saldo_so_validate2.setText(".");

        saldo_do.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        saldo_do.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        saldo_do.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        saldo_do.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                saldo_doKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                saldo_doKeyTyped(evt);
            }
        });

        saldo_so.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        saldo_so.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        saldo_so.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        saldo_so.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                saldo_soKeyTyped(evt);
            }
        });

        saldo_do_bancos.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        saldo_do_bancos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        saldo_do_bancos.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        saldo_do_bancos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                saldo_do_bancosKeyTyped(evt);
            }
        });

        saldo_so_bancos.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        saldo_so_bancos.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        saldo_so_bancos.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        saldo_so_bancos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                saldo_so_bancosKeyTyped(evt);
            }
        });

        saldo_so_validate3.setForeground(new java.awt.Color(204, 0, 0));
        saldo_so_validate3.setText(".");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("OPERACIÓN: ");

        opeGroup.add(abrir);
        abrir.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        abrir.setSelected(true);
        abrir.setText("ABRIR CAJA");

        opeGroup.add(cerrar);
        cerrar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cerrar.setText("CERRAR CAJA");

        fecha.setDateFormatString("dd/MM/yyyy");
        fecha.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addComponent(nuevoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(guardarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(eliminarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(id))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(saldo_do_validate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(saldo_so_validate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(saldo_so_validate1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(saldo_so_validate2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(saldo_do)
                            .addComponent(saldo_so)
                            .addComponent(saldo_do_bancos)
                            .addComponent(saldo_so_bancos)
                            .addComponent(saldo_so_validate3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(abrir)
                                .addGap(18, 18, 18)
                                .addComponent(cerrar))
                            .addComponent(fecha, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
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
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saldo_do, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saldo_do_validate)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saldo_so, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saldo_so_validate)
                .addGap(11, 11, 11)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saldo_do_bancos, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saldo_so_validate1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saldo_so_bancos, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saldo_so_validate2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(abrir)
                        .addComponent(cerrar))
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saldo_so_validate3)
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(fecha, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        if (this.tabla.getRowCount() < 1) {
            ErrorAlert er = new ErrorAlert(new JFrame(), true);
            er.titulo.setText("OOPS...");
            er.msj.setText("LA TABLA ESTA VACÍA");
            er.msj1.setText("");
            er.setVisible(true);
        } else {
            System.out.println("id: " + this.id.getText());
            if (this.id.getText().equals("")) {
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
                    int opcion = CajaAperCierreData.eliminar(Integer.parseInt(this.id.getText()));
                    if (opcion != 0) {
                        limpiarCampos();
                        this.id.setText("");
                        this.saldo_do.setText("");
                        this.saldo_so.setText("");
                        this.guardarButton.setText("REGISTRAR");
                    }
                }
            }
        }

    }//GEN-LAST:event_eliminarButtonActionPerformed

    private void nuevoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoButtonActionPerformed
        // TODO add your handling code here:
        guardarButton.setText("REGISTRAR");
        guardarButton.setToolTipText("REGISTRAR");
        guardarButton.setEnabled(true);
        guardarButton.setSelected(true);
        limpiarSoloCampos();

    }//GEN-LAST:event_nuevoButtonActionPerformed

    private void guardarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarButtonActionPerformed

        if (this.saldo_do.getText().equals("") || this.saldo_so.getText().equals("")
                || fecha.getDate() == null) {
            ErrorAlert er = new ErrorAlert(new JFrame(), true);
            er.titulo.setText("OOPS...");
            er.msj.setText("FALTAN LLENAR CAMPOS: SALDOS DÓLARES y SOLES");
            er.msj1.setText("");
            er.setVisible(true);

        } else {

            boolean continuar = true;
            System.out.println("id: " + id.getText());
            CajaAperCierre s = new CajaAperCierre();
            s.setUser(Validate.userId);
            s.setFecha(fecha.getDate());

            if (abrir.isSelected()) {
                s.setEsaper(1);
            } else {
                s.setEsaper(0);
            }
            if (saldo_do.getText().equals("")) {
                s.setSaldo_do(0);
            } else {
                s.setSaldo_do(Double.parseDouble(saldo_do.getText().replaceAll(",", "")));
            }
            if (saldo_so.getText().equals("")) {
                s.setSaldo_so(0);
            } else {
                s.setSaldo_so(Double.parseDouble(saldo_so.getText().replaceAll(",", "")));
            }
            if (saldo_do_bancos.getText().equals("")) {
                s.setSaldo_do_bancos(0);
            } else {
                s.setSaldo_do_bancos(Double.parseDouble(saldo_do_bancos.getText().replaceAll(",", "")));
            }
            if (saldo_so_bancos.getText().equals("")) {
                s.setSaldo_so_bancos(0);
            } else {
                s.setSaldo_so_bancos(Double.parseDouble(saldo_so_bancos.getText().replaceAll(",", "")));
            }

            if (continuar) {

                CajaAperCierre ver = CajaAperCierreData.getByFechaAndEsaper(s.getFecha(), s.getEsaper());
                if (ver.getFecha() != null) {
                    ErrorAlert er = new ErrorAlert(new JFrame(), true);
                    er.titulo.setText("OOPS...");
                    if (ver.getEsaper() == 1) {
                        er.msj.setText("LA CAJA PARA " + iguSDF.format(ver.getFecha()) + " YA ESTÁ ABIERTA");
                    } else {
                        er.msj.setText("LA CAJA PARA " + iguSDF.format(ver.getFecha()) + " YA ESTÁ CERRADA ");
                    }

                    er.msj1.setText("ELIMINE Y VUELVA A CREAR");
                    er.setVisible(true);
                } else {
                    if (id.getText().equals("")) {
                        int opcion = CajaAperCierreData.registrar(s);
                        if (opcion != 0) {
                            limpiarCampos();
                            SuccessAlert sa = new SuccessAlert(new JFrame(), true);
                            sa.titulo.setText("¡HECHO!");
                            sa.msj.setText("SE HA REGISTRADO UNA");
                            sa.msj1.setText("ABRIR/CERRAR CAJA ");
                            sa.setVisible(true);
                        }
                    } else {
                        s.setId(Integer.parseInt(id.getText()));
                        int opcion = CajaAperCierreData.actualizar(s);
                        if (opcion != 0) {
                            limpiarCampos();
                            SuccessAlert sa = new SuccessAlert(new JFrame(), true);
                            sa.titulo.setText("¡HECHO!");
                            sa.msj.setText("SE HAN GUARDADO LOS CAMBIOS");
                            sa.msj1.setText("ABRIR/CERRAR CAJA");
                            sa.setVisible(true);
                        }
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
            Logger.getLogger(AbrirCerrarCajaPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_aSIconButton4ActionPerformed

    private void buscarFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarFieldKeyReleased
        // TODO add your handling code here:
        //Opciones.listar(this.buscarField.getText());
        paintTable(fechaChooser.getDate(), this.buscarField.getText());
    }//GEN-LAST:event_buscarFieldKeyReleased

    private void buscarFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buscarFieldActionPerformed

    private void saldo_doKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saldo_doKeyReleased
        // TODO add your handling code here:
        saldo_do_validate.setText("");
        if (!saldo_do.getText().equals("")) {
            try {
                //double totalx = Math.round(Double.parseDouble(saldo_do.getText()) * 100.0) / 100.0;
                //total.setText(new DecimalFormat(Config.DEFAULT_DECIMAL_STRING_FORMAT).format(totalx));
            } catch (NumberFormatException nfe) {
                System.err.println("" + nfe);
                saldo_do_validate.setText("Número no válido");
            }
        }
    }//GEN-LAST:event_saldo_doKeyReleased

    private void saldo_doKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saldo_doKeyTyped
        // TODO add your handling code here:
        String filterStr = "0123456789.";
        char c = (char) evt.getKeyChar();
        if (filterStr.indexOf(c) < 0) {
            evt.consume();
        }
    }//GEN-LAST:event_saldo_doKeyTyped

    private void saldo_soKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saldo_soKeyTyped
        // TODO add your handling code here:
        String filterStr = "0123456789.";
        char c = (char) evt.getKeyChar();
        if (filterStr.indexOf(c) < 0) {
            evt.consume();
        }
    }//GEN-LAST:event_saldo_soKeyTyped

    private void saldo_do_bancosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saldo_do_bancosKeyTyped
        // TODO add your handling code here:
        String filterStr = "0123456789.";
        char c = (char) evt.getKeyChar();
        if (filterStr.indexOf(c) < 0) {
            evt.consume();
        }
    }//GEN-LAST:event_saldo_do_bancosKeyTyped

    private void saldo_so_bancosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saldo_so_bancosKeyTyped
        // TODO add your handling code here:
        String filterStr = "0123456789.";
        char c = (char) evt.getKeyChar();
        if (filterStr.indexOf(c) < 0) {
            evt.consume();
        }
    }//GEN-LAST:event_saldo_so_bancosKeyTyped

    private void fechaChooserPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_fechaChooserPropertyChange
        // TODO add your handling code here:
        paintTable(fechaChooser.getDate(), buscarField.getText());
    }//GEN-LAST:event_fechaChooserPropertyChange


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private igu.util.buttons.ASIconButton aSIconButton4;
    private javax.swing.JRadioButton abrir;
    private javax.swing.JTextField buscarField;
    private javax.swing.JRadioButton cerrar;
    private igu.util.buttons.ASIconButton eliminarButton;
    private com.toedter.calendar.JDateChooser fecha;
    private com.toedter.calendar.JDateChooser fechaChooser;
    private igu.util.buttons.ASIconButton guardarButton;
    private javax.swing.JLabel id;
    private javax.swing.JLabel jLabel1;
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
    private igu.util.buttons.ASIconButton nuevoButton;
    private javax.swing.ButtonGroup opeGroup;
    private javax.swing.JFormattedTextField saldo_do;
    private javax.swing.JFormattedTextField saldo_do_bancos;
    private javax.swing.JLabel saldo_do_validate;
    private javax.swing.JFormattedTextField saldo_so;
    private javax.swing.JFormattedTextField saldo_so_bancos;
    private javax.swing.JLabel saldo_so_validate;
    private javax.swing.JLabel saldo_so_validate1;
    private javax.swing.JLabel saldo_so_validate2;
    private javax.swing.JLabel saldo_so_validate3;
    public static javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
