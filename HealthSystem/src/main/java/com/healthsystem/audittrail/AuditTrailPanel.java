/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.audittrail;

import com.healthsystem.errorlog.ErrorLogDAO;
import com.healthsystem.errorlog.ErrorLogModel;
import com.healthsystem.util.ResizeUtil;
import com.healthsystem.util.WEBAPI;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class AuditTrailPanel extends javax.swing.JPanel {

    private java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/healthsystem/audittrail/Bundle"); // NOI18N

    /**
     * Creates new form AuditTrailPanel
     */
    public AuditTrailPanel() {
        initComponents();

        auditTrailTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            protected void setValue(Object value) {
                if (value instanceof ImageIcon) {
                    setIcon((ImageIcon) value);
                    setText("");
                } else {
                    setIcon(null);
                    super.setValue(value);
                }
            }
        });

        dateDatePicker.setDate(new Date());

        chargeLog();
    }

    private AuditTrailDAO auditTrailDAO = new AuditTrailDAO();

    private void chargeLog() {
        for (int i = 0; i <= auditTrailTable.getRowCount(); i++) {
            ((DefaultTableModel) auditTrailTable.getModel()).setNumRows(0);
        }
        auditTrailTable.updateUI();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        List<AuditTrailModel> auditTrails = auditTrailDAO.getAuditTrailList(df.format(dateDatePicker.getDate()));
        DefaultTableModel model = (DefaultTableModel) auditTrailTable.getModel();
        for (AuditTrailModel auditTrailModel : auditTrails) {

            try {
                URL url = new URL(WEBAPI.IMAGE + "userhealth/" + auditTrailModel.getPhoto() + "?" + System.currentTimeMillis());

                System.out.println(url.toString());
                BufferedImage buff = ImageIO.read(url);
                BufferedImage resize = ResizeUtil.resize(buff, 40, 40);
                resize = ResizeUtil.circle(resize);

                model.addRow(new Object[]{
                    new ImageIcon(resize),
                    auditTrailModel.getName(),
                    auditTrailModel.getEventDate(),
                    auditTrailModel.getCategory(),
                    auditTrailModel.getEventName(),
                    auditTrailModel.getAdditionalInfo()
                });

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        auditTrailTable.setRowHeight(40);
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
        dateDatePicker = new org.jdesktop.swingx.JXDatePicker();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        auditTrailTable = new javax.swing.JTable();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        dateDatePicker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dateDatePickerActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/healthsystem/audittrail/Bundle"); // NOI18N
        jLabel1.setText(bundle.getString("AuditTrailPanel.jLabel1.text")); // NOI18N

        auditTrailTable.setModel(new javax.swing.table.DefaultTableModel(

            new Object [][] {

            },
            new String [] {
                bundle.getString("AuditTrailPanel.table.column0"),
                bundle.getString("AuditTrailPanel.table.column1"),
                bundle.getString("AuditTrailPanel.table.column2"),
                bundle.getString("AuditTrailPanel.table.column3"),
                bundle.getString("AuditTrailPanel.table.column4"),
                bundle.getString("AuditTrailPanel.table.column5")

            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(auditTrailTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dateDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dateDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void dateDatePickerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dateDatePickerActionPerformed
        chargeLog();
    }//GEN-LAST:event_dateDatePickerActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable auditTrailTable;
    private org.jdesktop.swingx.JXDatePicker dateDatePicker;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}