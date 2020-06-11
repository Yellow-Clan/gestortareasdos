/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package igu.provee;

import data.ProveedorData;
import igu.util.tables.ExportarExcel;
import entites.Proveedor;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFormattedTextField;

/**
 *
 * @author Asullom
 */
public class ProveedoresPanel extends javax.swing.JPanel {

    ExportarExcel obj;
    SimpleDateFormat iguSDF = new SimpleDateFormat(Config.DEFAULT_DATE_STRING_FORMAT_PE);

    public ProveedoresPanel() {
        //fecha_nac= new JFormattedTextField( iguSDF );
        initComponents();
        
        Date date_i = new Date();
        fecha_nac.setText(iguSDF.format(date_i));

        this.tabla.getTableHeader().setDefaultRenderer(new EstiloTablaHeader());
        this.tabla.setDefaultRenderer(Object.class, new EstiloTablaRenderer());

        jScrollPane1.getViewport().setBackground(new java.awt.Color(255, 255, 255));
        jScrollPane1.getVerticalScrollBar().setUI(new MyScrollbarUI());
        jScrollPane1.getHorizontalScrollBar().setUI(new MyScrollbarUI());

        this.id.setText("");
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
                        String infoadics = (String) tabla.getValueAt(row[0], 3);
                        infoadic.setText("" + infoadics);
                        String fecha = (String) tabla.getValueAt(row[0], 4);
                        fecha_nac.setText("" + fecha);

                        System.out.println("Table element selected es: " + ids);
                        guardarButton.setText("MODIFICAR");
                    }
                } else {
                    System.out.println("eee");
                }
            }

        });

        this.nombres.requestFocus();

    }

    private void paintTable(String buscar) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        List<Proveedor> lis = ProveedorData.list(buscar);
        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
        String datos[] = new String[7];
        int cont = 0;
        for (Proveedor d : lis) {
            datos[0] = ++cont + "";
            datos[1] = d.getId() + "";
            datos[2] = d.getNombres();
            datos[3] = d.getInfoadic();
            datos[4] = iguSDF.format(d.getFecha_nac());
            datos[5] = iguSDF.format(d.getDate_created());
            datos[6] = iguSDF.format(d.getLast_updated());
            modelo.addRow(datos);
        }
        tabla.getColumnModel().getColumn(0).setMaxWidth(35);
        tabla.getColumnModel().getColumn(0).setCellRenderer(new EstiloTablaRenderer("texto"));
        tabla.getColumnModel().getColumn(1).setMaxWidth(35);
        tabla.getColumnModel().getColumn(1).setCellRenderer(new EstiloTablaRenderer("texto"));
        tabla.getColumnModel().getColumn(2).setPreferredWidth(500);
        tabla.getColumnModel().getColumn(2).setCellRenderer(new EstiloTablaRenderer("texto"));
        tabla.getColumnModel().getColumn(3).setPreferredWidth(600);
        tabla.getColumnModel().getColumn(3).setCellRenderer(new EstiloTablaRenderer("texto"));
        tabla.getColumnModel().getColumn(4).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(4).setCellRenderer(new EstiloTablaRenderer("fecha"));
        tabla.getColumnModel().getColumn(5).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(5).setCellRenderer(new EstiloTablaRenderer("fecha"));
        tabla.getColumnModel().getColumn(6).setPreferredWidth(200);
        tabla.getColumnModel().getColumn(6).setCellRenderer(new EstiloTablaRenderer("fecha"));
    }

    private void limpiarCampos() {
        this.nombres.requestFocus();
        this.nombres.setText("");
        this.infoadic.setText("");
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

        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        buscarField = new javax.swing.JTextField();
        aSIconButton4 = new igu.util.buttons.ASIconButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        nuevoButton = new igu.util.buttons.ASIconButton();
        guardarButton = new igu.util.buttons.ASIconButton();
        eliminarButton = new igu.util.buttons.ASIconButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        infoadic = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        nombres = new javax.swing.JTextField();
        id = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        fecha_nac = new javax.swing.JTextField();

        jPanel5.setBackground(new java.awt.Color(58, 159, 171));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("PROVEEDORES");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 499, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buscarField, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(aSIconButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(buscarField, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(aSIconButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addContainerGap(43, Short.MAX_VALUE))
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
                "Nº", "ID", "NOMBRES", "INFORMACIÓN ADIC", "FECHA", "CREATED", "LAST UPDATE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, false
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
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

        infoadic.setColumns(20);
        infoadic.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        infoadic.setRows(5);
        jScrollPane2.setViewportView(infoadic);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Información adicional:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Nombres PROVEEDOR: ");

        nombres.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        id.setText("id");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel5.setText("FECHA \"dd/MM/yyyy\"");
        jLabel5.setToolTipText("");

        fecha_nac.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        fecha_nac.setToolTipText("");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(fecha_nac))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addComponent(nuevoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(guardarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(eliminarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(id))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nombres)))
                .addContainerGap(30, Short.MAX_VALUE))
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
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nombres, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(fecha_nac, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(130, Short.MAX_VALUE))
        );

        fecha_nac.getAccessibleContext().setAccessibleName("");

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
                    int opcion = ProveedorData.eliminar(Integer.parseInt(this.id.getText()));
                    if (opcion != 0) {
                        limpiarCampos();
                        this.id.setText("");
                        this.nombres.setText("");
                        this.infoadic.setText("");
                        this.guardarButton.setText("REGISTRAR");
                    }
                }
            }
        }

    }//GEN-LAST:event_eliminarButtonActionPerformed

    private void nuevoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoButtonActionPerformed
        // TODO add your handling code here:
        //this.tituloLabel.setText("REGISTRAR");
        this.guardarButton.setText("REGISTRAR");
        this.id.setText("");
        this.nombres.setText("");
        this.infoadic.setText("");
        this.nombres.requestFocus();

    }//GEN-LAST:event_nuevoButtonActionPerformed

    private void guardarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarButtonActionPerformed

        if (this.nombres.getText().equals("")) {
            ErrorAlert er = new ErrorAlert(new JFrame(), true);
            er.titulo.setText("OOPS...");
            er.msj.setText("FALTAN CAMPOS DE LLENAR");
            er.msj1.setText("");
            er.setVisible(true);

        } else {

            System.out.println("id: " + this.id.getText());
            Proveedor s = new Proveedor();

            s.setNombres(this.nombres.getText());
            s.setInfoadic(this.infoadic.getText());

            Date date = new Date();
            String test = this.fecha_nac.getText(); //"02/03/2020";
            System.out.println("panel.fecha_nac.test: " + test);
            iguSDF.setLenient(false);
            try {
                date = iguSDF.parse(test);
                if (!iguSDF.format(date).equals(test)) {
                    throw new ParseException(test + " is not a valid format for " + Config.DEFAULT_DATE_STRING_FORMAT_PE, 0);
                }
            } catch (ParseException ex1) {
                System.out.println("panel.ParseException error: " + ex1);
                ErrorAlert er = new ErrorAlert(new JFrame(), true);
                er.titulo.setText("OOPS...");
                er.msj.setText("FECHA NO VÁLIDO " + ex1);
                er.msj1.setText("" + test + " is not a valid format for " + Config.DEFAULT_DATE_STRING_FORMAT_PE);
                er.setVisible(true);
            }
            s.setFecha_nac(date);

            if (this.id.getText().equals("")) {
                int opcion = ProveedorData.registrar(s);
                if (opcion != 0) {
                    limpiarCampos();
                    SuccessAlert sa = new SuccessAlert(new JFrame(), true);
                    sa.titulo.setText("¡HECHO!");
                    sa.msj.setText("SE HA REGISTRADO UN");
                    sa.msj1.setText("NUEVO PRODUCTO");
                    sa.setVisible(true);
                }
            } else {
                s.setId(Integer.parseInt(this.id.getText()));
                int opcion = ProveedorData.actualizar(s);
                if (opcion != 0) {
                    limpiarCampos();
                    SuccessAlert sa = new SuccessAlert(new JFrame(), true);
                    sa.titulo.setText("¡HECHO!");
                    sa.msj.setText("SE HAN GUARDADO LOS CAMBIOS");
                    sa.msj1.setText("EN PRODUCTO");
                    sa.setVisible(true);
                }
            }

        }
    }//GEN-LAST:event_guardarButtonActionPerformed

    private void aSIconButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aSIconButton4ActionPerformed
        try {
            obj = new ExportarExcel();
            obj.exportarExcel(tabla);
        } catch (IOException ex) {
            Logger.getLogger(ProveedoresPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_aSIconButton4ActionPerformed

    private void buscarFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarFieldKeyReleased
        // TODO add your handling code here:
        //Opciones.listar(this.buscarField.getText());
        paintTable(this.buscarField.getText());
    }//GEN-LAST:event_buscarFieldKeyReleased

    private void buscarFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buscarFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_buscarFieldActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private igu.util.buttons.ASIconButton aSIconButton4;
    private javax.swing.JTextField buscarField;
    private igu.util.buttons.ASIconButton eliminarButton;
    private javax.swing.JTextField fecha_nac;
    private igu.util.buttons.ASIconButton guardarButton;
    private javax.swing.JLabel id;
    private javax.swing.JTextArea infoadic;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    public static javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField nombres;
    private igu.util.buttons.ASIconButton nuevoButton;
    public static javax.swing.JTable tabla;
    // End of variables declaration//GEN-END:variables
}
