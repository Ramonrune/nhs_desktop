/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.patient;

import com.google.common.io.Files;
import com.healthsystem.healthinstitution.HealthInstitutionAddWindow;
import com.healthsystem.healthinstitution.HealthInstitutionSingleton;
import com.healthsystem.healthinstitution.HealthInstitutionUpdateWindow;
import com.healthsystem.util.ResizeUtil;
import com.healthsystem.util.azure.AzureBlob;
import com.healthsystem.util.azure.ImageFilter;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Usuario
 */
public class ExamAddWindow extends javax.swing.JDialog {

    private PatientDAO patientDAO = new PatientDAO();
    private String patientId;
    private String physicianId;
    private PatientPanel patientPanel;

    public ExamAddWindow(String patientId, String physicianId) {
        this.patientId = patientId;
        this.physicianId = physicianId;

        setResizable(false);

        initComponents();

        setLocationRelativeTo(null);

        attachmentPanel.setLayout(new BoxLayout(attachmentPanel, BoxLayout.PAGE_AXIS));

        fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(true);

        fcSignature = new JFileChooser();
        fcSignature.setFileFilter(new ImageFilter());
        fcSignature.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fcSignature.setMultiSelectionEnabled(true);
    }

    private List<File> uploadList = new ArrayList<>();

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToggleButton1 = new javax.swing.JToggleButton();
        jPanel1 = new javax.swing.JPanel();
        addDiagnosisButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        examTextPane = new javax.swing.JEditorPane();
        drawPad = new com.healthsystem.util.component.DrawPad();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        descriptionExamLabel = new javax.swing.JLabel();
        attachmentScrollPanel = new javax.swing.JScrollPane(attachmentPanel);
        attachmentsLabel = new javax.swing.JLabel();
        addAttachmentButton = new javax.swing.JButton();
        uploadSignatureButton = new javax.swing.JButton();
        signatureLabel = new javax.swing.JLabel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/healthsystem/patient/Bundle"); // NOI18N
        jToggleButton1.setText(bundle.getString("ExamAddWindow.jToggleButton1.text")); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(bundle.getString("ExamAddWindow.title_1")); // NOI18N
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        addDiagnosisButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/success.png"))); // NOI18N
        addDiagnosisButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDiagnosisButtonActionPerformed(evt);
            }
        });
        jPanel1.add(addDiagnosisButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 550, 50, 40));

        examTextPane.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jScrollPane1.setViewportView(examTextPane);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 310, 310));

        drawPad.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.add(drawPad, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 420, 630, 125));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel1.setText(bundle.getString("DiagnosisAddWindow.jLabel1.text")); // NOI18N
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 399, -1, -1));

        jButton1.setText(bundle.getString("DiagnosisAddWindow.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 551, 100, 40));

        descriptionExamLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        descriptionExamLabel.setText(bundle.getString("ExamAddWindow.descriptionExamLabel.text")); // NOI18N
        jPanel1.add(descriptionExamLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 31, -1, -1));
        jPanel1.add(attachmentScrollPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 60, 300, 310));

        attachmentsLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        attachmentsLabel.setText(bundle.getString("ExamAddWindow.attachmentsLabel.text")); // NOI18N
        jPanel1.add(attachmentsLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 30, -1, -1));

        addAttachmentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/attachment.png"))); // NOI18N
        addAttachmentButton.setText(bundle.getString("ExamAddWindow.addAttachmentButton.text")); // NOI18N
        addAttachmentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addAttachmentButtonActionPerformed(evt);
            }
        });
        jPanel1.add(addAttachmentButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 10, 47, -1));

        uploadSignatureButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/upload.png"))); // NOI18N
        uploadSignatureButton.setText(bundle.getString("ExamAddWindow.uploadSignatureButton.text")); // NOI18N
        uploadSignatureButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadSignatureButtonActionPerformed(evt);
            }
        });
        jPanel1.add(uploadSignatureButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 551, 50, -1));

        signatureLabel.setText(bundle.getString("ExamAddWindow.signatureLabel.text_1")); // NOI18N
        jPanel1.add(signatureLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 420, 630, 120));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 654, 604));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private JPanel attachmentPanel = new JPanel();
    private void addDiagnosisButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDiagnosisButtonActionPerformed

        boolean success = true;

        if (examTextPane.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, i18nPatient.getString("ExamAddWindow.validate.description"));
            success = false;
        }

        if (uploadImage == null) {
            if (success) {
                if (!drawPad.isValid()) {
                    JOptionPane.showMessageDialog(null, i18nPatient.getString("DiagnosisAddWindow.validate.signature"));
                    success = false;
                }
            }
        }

        if (success) {

            String id = UUID.randomUUID().toString();
            boolean ok = patientDAO.addExam(id, examTextPane.getText(), patientId, physicianId, HealthInstitutionSingleton.getInstance().getIdHealthInstitution(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            if (ok) {
                AzureBlob.upload(uploadImage == null ? drawPad.generateBufferedImage() : uploadImage, id + ".jpg", "exam");
                for (File file : uploadList) {

                    String idAttachment = UUID.randomUUID().toString();
                    boolean result = patientDAO.addExamAttachment(idAttachment, file.getName(), id);
                    if (!result) {
                        ok = false;
                        break;
                    }
                    AzureBlob.upload(file, idAttachment + "." + Files.getFileExtension(file.getAbsolutePath()), "exam-attachments");

                }

                if (ok) {
                    patientPanel.chargeExams();
                    setVisible(false);
                    JOptionPane.showMessageDialog(null, i18nPatient.getString("ExamAddWindow.success"));
                    dispose();
                }

            }

        }


    }//GEN-LAST:event_addDiagnosisButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        drawPad.clear();
    }//GEN-LAST:event_jButton1ActionPerformed
    private int size = 0;
    private void addAttachmentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addAttachmentButtonActionPerformed

        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (size < 5) {
                File[] files = fc.getSelectedFiles();
                int len = 0;
                boolean dontAttach = false;
                for (File file : files) {
                    if (len == 5) {
                        JOptionPane.showMessageDialog(null, i18nPatient.getString("ExamAddWindow.message.filelimit"));
                        break;
                    }

                    // Get length of file in bytes
                    long fileSizeInBytes = file.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
                    long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
                    long fileSizeInMB = fileSizeInKB / 1024;

                    if (fileSizeInMB > 20) {
                        dontAttach = true;
                        continue;
                    }

                    attachmentScrollPanel.getVerticalScrollBar().setUnitIncrement(16);
                    JPanel jpanel = new JPanel(new GridBagLayout());
                    jpanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                    JButton chooseAttachmentButton = new JButton(file.getName());
                    chooseAttachmentButton.setPreferredSize(new Dimension(200, 40));
                    GridBagConstraints c = new GridBagConstraints();
                    c.gridx = 0;
                    c.gridy = 0;
                    c.weightx = 1;

                    jpanel.add(chooseAttachmentButton, c);

                    JButton deleteAttachmentButton = new JButton();

                    deleteAttachmentButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/delete.png"))); // NOI18N

                    deleteAttachmentButton.addMouseListener(new MouseListener() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            attachmentPanel.remove(jpanel);
                            attachmentPanel.revalidate();
                            attachmentPanel.repaint();
                            uploadList.remove(file);
                            size--;
                        }

                        @Override
                        public void mousePressed(MouseEvent e) {

                        }

                        @Override
                        public void mouseReleased(MouseEvent e) {

                        }

                        @Override
                        public void mouseEntered(MouseEvent e) {
                        }

                        @Override
                        public void mouseExited(MouseEvent e) {

                        }
                    });

                    c = new GridBagConstraints();
                    c.gridx = 1;
                    c.gridy = 0;
                    c.weightx = 1;
                    jpanel.add(deleteAttachmentButton, c);

                    attachmentPanel.add(jpanel);
                    attachmentPanel.revalidate();
                    attachmentPanel.repaint();

                    uploadList.add(file);
                    len++;
                    size++;

                }

                if (dontAttach) {
                    JOptionPane.showMessageDialog(null, i18nPatient.getString("ExamAddWindow.message.fileSizeLimit"));

                }

            } else {
                JOptionPane.showMessageDialog(null, i18nPatient.getString("ExamAddWindow.message.filelimit"));

            }
        }

    }//GEN-LAST:event_addAttachmentButtonActionPerformed

    private BufferedImage uploadImage;
    private JFileChooser fcSignature;

    private void uploadSignatureButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadSignatureButtonActionPerformed

        int returnVal = fcSignature.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fcSignature.getSelectedFile();
            try {
                BufferedImage read = ImageIO.read(file);
                uploadImage = ResizeUtil.resize(read, 630, 120);
                ImageIcon icon = new ImageIcon(uploadImage);
                drawPad.setVisible(false);
                drawPad.revalidate();
                drawPad.repaint();
                signatureLabel.setIcon(icon);
            } catch (IOException ex) {
                Logger.getLogger(HealthInstitutionAddWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_uploadSignatureButtonActionPerformed
    private java.util.ResourceBundle i18nPatient = java.util.ResourceBundle.getBundle("com/healthsystem/patient/Bundle"); // NOI18N
    private JFileChooser fc;


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addAttachmentButton;
    private javax.swing.JButton addDiagnosisButton;
    private javax.swing.JScrollPane attachmentScrollPanel;
    private javax.swing.JLabel attachmentsLabel;
    private javax.swing.JLabel descriptionExamLabel;
    private com.healthsystem.util.component.DrawPad drawPad;
    private javax.swing.JEditorPane examTextPane;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel signatureLabel;
    private javax.swing.JButton uploadSignatureButton;
    // End of variables declaration//GEN-END:variables

    public void setPatientPanel(PatientPanel patientPanel) {
        this.patientPanel = patientPanel;
    }
}
