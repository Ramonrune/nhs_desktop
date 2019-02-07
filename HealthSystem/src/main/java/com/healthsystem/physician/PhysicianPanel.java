/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.physician;

import com.healthsystem.healthinstitution.HealthInstitutionModel;
import com.healthsystem.healthinstitution.HealthInstitutionSingleton;
import com.healthsystem.home.HomePanel;
import com.healthsystem.patient.PatientDAO;
import com.healthsystem.patient.PatientModel;
import com.healthsystem.patient.PatientPanel;
import static com.healthsystem.physician.NFCReader.nfcReaderPanel;
import com.healthsystem.user.UserSingleton;
import com.healthsystem.user.physician.DiagnosisHistoryModel;
import com.healthsystem.user.physician.ExamHistoryModel;
import com.healthsystem.user.physician.PhysicianAttendanceModel;
import com.healthsystem.user.physician.PhysicianDAO;
import com.healthsystem.util.ResizeUtil;
import com.healthsystem.util.WEBAPI;
import com.healthsystem.util.WindowManager;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

/**
 *
 * @author Usuario
 */
public class PhysicianPanel extends javax.swing.JPanel {

    private java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/healthsystem/physician/Bundle"); // NOI18N
    private PhysicianDAO physicianDAO = new PhysicianDAO();
    private PatientDAO patientDAO = new PatientDAO();
    private LoadingPatientWindow loadingWindow = new LoadingPatientWindow();

    public PhysicianPanel() {
        initComponents();

        patientTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {

                try {
                    patientTable.setRowSelectionInterval(patientTable.getSelectedRow(), patientTable.getSelectedRow());

                    String id = patientTable.getModel().getValueAt(patientTable.getSelectedRow(), 0).toString();
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.execute(() -> {
                        loadingWindow.setVisible(true);

                        PatientModel patientModel = patientDAO.getPatient(id);
                        PatientPanel patientPanel = new PatientPanel();
                        patientPanel.setPatientModel(patientModel);
                        loadingWindow.dispose();
                        WindowManager.changePanel(patientPanel, PhysicianPanel.this);
                    });

                } catch (Exception e) {

                }
            }

        });

        patientTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

        dateAttendanceDatePicker.setDate(new Date());

        patientTable.removeColumn(patientTable.getColumnModel().getColumn(0));

        chargePatient();

    }

    private List<PhysicianAttendanceModel> listPatients;

    private void chargePatient() {
        for (int i = 0; i <= patientTable.getRowCount(); i++) {
            ((DefaultTableModel) patientTable.getModel()).setNumRows(0);
        }
        patientTable.updateUI();

        DefaultTableModel model = (DefaultTableModel) patientTable.getModel();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        listPatients = physicianDAO.listPhysicianAttendance(UserSingleton.getInstance().getPhysicianModel().getIdPhysician(),
                HealthInstitutionSingleton.getInstance().getIdHealthInstitution(), df.format(dateAttendanceDatePicker.getDate()));

        for (PhysicianAttendanceModel physicianAttendanceModel : listPatients) {

            try {
                URL url = new URL(WEBAPI.IMAGE + "userhealth/" + physicianAttendanceModel.getPhoto() + "?" + System.currentTimeMillis());
                BufferedImage buff = ImageIO.read(url);
                BufferedImage resize = ResizeUtil.resize(buff, 40, 40);
                resize = ResizeUtil.circle(resize);

                model.addRow(new Object[]{
                    physicianAttendanceModel.getIdUser(),
                    new ImageIcon(resize),
                    physicianAttendanceModel.getName(),
                    physicianAttendanceModel.getDateAttendance()

                });

            } catch (Exception e) {

            }
        }
        patientTable.setRowHeight(40);

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
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        patientTable = new javax.swing.JTable();
        searchPatientTextField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        dateAttendanceDatePicker = new com.toedter.calendar.JDateChooser();

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(721, 506));
        setPreferredSize(new java.awt.Dimension(721, 506));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/scanner.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/healthsystem/physician/Bundle"); // NOI18N
        jButton1.setText(bundle.getString("PhysicianPanel.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/patient.png"))); // NOI18N
        jButton2.setText(bundle.getString("PhysicianPanel.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/key (2).png"))); // NOI18N
        jButton3.setText(bundle.getString("PhysicianPanel.jButton3.text")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel1.setText(bundle.getString("PhysicianPanel.jLabel1.text")); // NOI18N

        jScrollPane2.setBackground(new java.awt.Color(255, 255, 255));

        patientTable.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        patientTable.setModel(new javax.swing.table.DefaultTableModel(

            new Object [][] {

            },
            new String [] {
                "id",
                bundle.getString("PhysicianPanel.diagnosisTable.columnModel.title0"),
                bundle.getString("PhysicianPanel.diagnosisTable.columnModel.title1"),
                bundle.getString("PhysicianPanel.diagnosisTable.columnModel.title2")
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(patientTable);

        searchPatientTextField.setText(bundle.getString("PhysicianPanel.searchPatientTextField.text")); // NOI18N
        searchPatientTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchPatientTextFieldActionPerformed(evt);
            }
        });
        searchPatientTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchPatientTextFieldKeyPressed(evt);
            }
        });

        searchButton.setText(bundle.getString("PhysicianPanel.searchButton.text")); // NOI18N
        searchButton.setPreferredSize(new java.awt.Dimension(73, 30));
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        dateAttendanceDatePicker.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateAttendanceDatePickerPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(searchPatientTextField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(searchButton, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(dateAttendanceDatePicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(1, 1, 1)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 45, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(7, 7, 7)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchPatientTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dateAttendanceDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
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

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        NFCReaderPanel nFCReaderPanel = new NFCReaderPanel();
        WindowManager.changePanel(nFCReaderPanel, this, false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        AddNewPatientWindow addNewPatientWindow = new AddNewPatientWindow(this);
        addNewPatientWindow.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        AuthenticateWindow authenticateWindow = new AuthenticateWindow();
        authenticateWindow.setPhysicianPanel(this);
        authenticateWindow.setVisible(true);

    }//GEN-LAST:event_jButton3ActionPerformed

    private void searchPatientTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchPatientTextFieldKeyPressed

    }//GEN-LAST:event_searchPatientTextFieldKeyPressed

    private void searchPatientTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchPatientTextFieldActionPerformed
        search();

    }//GEN-LAST:event_searchPatientTextFieldActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        search();
    }//GEN-LAST:event_searchButtonActionPerformed

    private void dateAttendanceDatePickerPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dateAttendanceDatePickerPropertyChange
        chargePatient();
    }//GEN-LAST:event_dateAttendanceDatePickerPropertyChange

    private void search() {
        for (int i = 0; i <= patientTable.getRowCount(); i++) {
            ((DefaultTableModel) patientTable.getModel()).setNumRows(0);
        }
        patientTable.updateUI();
        DefaultTableModel model = (DefaultTableModel) patientTable.getModel();

        for (PhysicianAttendanceModel physicianAttendanceModel : listPatients) {
            if (physicianAttendanceModel.getName().toLowerCase().contains(searchPatientTextField.getText().toString().toLowerCase())) {
                try {
                    URL url = new URL(WEBAPI.IMAGE + "userhealth/" + physicianAttendanceModel.getPhoto() + "?" + System.currentTimeMillis());
                    BufferedImage buff = ImageIO.read(url);
                    BufferedImage resize = ResizeUtil.resize(buff, 40, 40);
                    resize = ResizeUtil.circle(resize);

                    model.addRow(new Object[]{
                        physicianAttendanceModel.getIdUser(),
                        new ImageIcon(resize),
                        physicianAttendanceModel.getName(),
                        physicianAttendanceModel.getDateAttendance()

                    });

                } catch (Exception e) {

                }
            }
        }
        patientTable.setRowHeight(40);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.toedter.calendar.JDateChooser dateAttendanceDatePicker;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable patientTable;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchPatientTextField;
    // End of variables declaration//GEN-END:variables

}
