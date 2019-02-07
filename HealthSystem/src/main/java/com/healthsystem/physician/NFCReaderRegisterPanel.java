/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.physician;

import com.healthsystem.patient.PatientDAO;
import static com.healthsystem.physician.NFCReaderPanel.cont;
import com.healthsystem.user.UserDAO;
import com.healthsystem.util.Criptography;
import com.healthsystem.util.PasswordGenerator;
import com.healthsystem.util.WindowManager;
import com.healthsystem.util.dataprovider.CountryModel;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class NFCReaderRegisterPanel extends javax.swing.JPanel {

    protected static boolean cont = true;

    /**
     * Creates new form NFCReaderRegisterpanel
     */
    public NFCReaderRegisterPanel(AddNewPatientWindow addNewPatientWindow) {
        initComponents();

        NFCReaderNewSensor.NFCReaderRegisterPanel = NFCReaderRegisterPanel.this;
        NFCReaderNewSensor.addNewPatientWindow = addNewPatientWindow;

        if (!NFCReaderNewSensor.setupOk()) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {

                while (cont) {

                    ExecutorService executor11 = Executors.newSingleThreadExecutor();
                    executor11.execute(() -> {
                        if (NFCReaderNewSensor.setupOk()) {

                            statusOk();
                            executor11.shutdown();

                        } else {
                            statusNotOk();

                        }
                    });

                    try {

                        TimeUnit.MILLISECONDS.sleep(1000);

                    } catch (InterruptedException ex) {
                        Logger.getLogger(NFCReaderPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }

            });
        } else {
            statusOk();
        }
    }

    private static String name;
    private static String identityDocument;
    private static String country;
    private static String login;
    private static boolean alreadyRegistered;
    private static String idUser;

    private AddNewPatientWindow addNewPatientWindow;

    public void setNewPatient(String name, String identityDocument, String country, String login, boolean alreadyRegistered, String idUser) {
        this.name = "";
        this.name = name;

        this.identityDocument = "";
        this.identityDocument = identityDocument;

        this.country = "";
        this.country = country;

        this.login = "";
        this.login = login;

        this.alreadyRegistered = false;
        this.alreadyRegistered = alreadyRegistered;

        this.idUser = "";
        this.idUser = idUser;

    }

    public static String send() {
        boolean ok = true;
        StringBuilder id = new StringBuilder();
        String password = "";
        if (!alreadyRegistered) {
            PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
                    .useDigits(true)
                    .useLower(true)
                    .useUpper(true)
                    .build();
            password = passwordGenerator.generate(8);
            String passEncript = Criptography.sha256(password);
            ok = userDAO.add(
                    name,
                    identityDocument,
                    country,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "USER_DEFAULT_PHOTO.jpg",
                    "",
                    login,
                    passEncript,
                    "5",
                    "",
                    "",
                    id
            );
        } else {
            id.append(idUser);
        }

        if (ok) {
            StringBuilder idPatientAlreadyExist = new StringBuilder();
            String idPatient = UUID.randomUUID().toString();
            PatientDAO patientDAO = new PatientDAO();
            ok = patientDAO.add(idPatient, null, null, null, null, null, null, "1", id.toString(), idPatientAlreadyExist);

            if (ok) {

                if(!alreadyRegistered){
                    ok = userDAO.sendEmailPassword(login, password, country);
                }
                if (ok) {
                    return idPatientAlreadyExist.length() != 0 ? idPatientAlreadyExist.toString() : idPatient;
                }
            }
        }

        return "";
    }

    private java.util.ResourceBundle i18n = java.util.ResourceBundle.getBundle("com/healthsystem/physician/Bundle"); // NOI18N

    private void statusOk() {
        errorImageLabel.setVisible(false);
        imageLabel.setVisible(true);

        messageLabel.setText(i18n.getString("NFCReaderPanel.message.insert"));
        messageLabel.updateUI();
        imageLabel.updateUI();
        errorImageLabel.updateUI();
    }

    private void statusNotOk() {
        errorImageLabel.setVisible(true);
        errorImageLabel.updateUI();
        imageLabel.setVisible(false);
        imageLabel.updateUI();
        messageLabel.setText(i18n.getString("NFCReaderPanel.messageLabel.text"));
        messageLabel.updateUI();

    }

    private static UserDAO userDAO = new UserDAO();

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        messageLabel = new javax.swing.JLabel();
        imageLabel = new javax.swing.JLabel();
        errorImageLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(400, 350));
        setMinimumSize(new java.awt.Dimension(400, 350));
        setPreferredSize(new java.awt.Dimension(400, 350));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(400, 350));
        jPanel1.setMinimumSize(new java.awt.Dimension(400, 350));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 350));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        messageLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/healthsystem/physician/Bundle"); // NOI18N
        messageLabel.setText(bundle.getString("NFCReaderPanel.messageLabel.text")); // NOI18N
        jPanel1.add(messageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 290, 470, 40));

        imageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/nfc_loading.gif"))); // NOI18N
        imageLabel.setText(bundle.getString("NFCReaderPanel.imageLabel.text")); // NOI18N
        jPanel1.add(imageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, -40, 262, 367));

        errorImageLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/usb_help.gif"))); // NOI18N
        errorImageLabel.setText(bundle.getString("NFCReaderPanel.errorImageLabel.text")); // NOI18N
        jPanel1.add(errorImageLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -30, 517, 367));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, -1));
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel errorImageLabel;
    public static javax.swing.JLabel imageLabel;
    private javax.swing.JPanel jPanel1;
    public static javax.swing.JLabel messageLabel;
    // End of variables declaration//GEN-END:variables
}
