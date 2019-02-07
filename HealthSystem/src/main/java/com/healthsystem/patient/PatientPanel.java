/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.patient;

import com.healthsystem.healthinstitution.HealthInstitutionModel;
import com.healthsystem.healthinstitution.HealthInstitutionSingleton;
import com.healthsystem.user.UserDAO;
import com.healthsystem.user.UserModel;
import com.healthsystem.user.UserSingleton;
import com.healthsystem.user.physician.PhysicianDAO;
import com.healthsystem.user.physician.PhysicianModel;
import com.healthsystem.util.ColorManager;
import com.healthsystem.util.ResizeUtil;
import com.healthsystem.util.WEBAPI;
import com.healthsystem.util.dataprovider.CountryList;
import com.healthsystem.util.dataprovider.CountryModel;
import com.healthsystem.util.dataprovider.DiseaseList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class PatientPanel extends javax.swing.JPanel {

    private java.util.ResourceBundle i18nUser = java.util.ResourceBundle.getBundle("com/healthsystem/user/Bundle"); // NOI18N
    private java.util.ResourceBundle i18nPatient = java.util.ResourceBundle.getBundle("com/healthsystem/patient/Bundle"); // NOI18N

    public PatientPanel() {
        try {
            initComponents();
        } catch (Exception e) {

        }
        List<String> genderList = new ArrayList<>();
        genderList.add(i18nPatient.getString("PatientPanel.notSelected"));
        genderList.add(i18nUser.getString("UserAddWindow.gender.man"));
        genderList.add(i18nUser.getString("UserAddWindow.gender.woman"));
        genderList.add(i18nUser.getString("UserAddWindow.gender.other"));
        genderComboBox.setModel(new DefaultComboBoxModel(genderList.toArray()));
        countryComboBox.setModel(new DefaultComboBoxModel(CountryList.listOnlyCountries().toArray()));

        List<String> bloodList = new ArrayList<>();
        bloodList.add(i18nPatient.getString("PatientPanel.notSelected"));
        bloodList.add("A+");
        bloodList.add("B+");
        bloodList.add("AB+");
        bloodList.add("O+");
        bloodList.add("A-");
        bloodList.add("B-");
        bloodList.add("AB-");
        bloodList.add("O-");
        bloodTypeComboBox.setModel(new DefaultComboBoxModel(bloodList.toArray()));

        /* List<String> colorList = new ArrayList<>();
        colorList.add(i18nPatient.getString("PatientPanel.notSelected"));
        colorList.add(i18nPatient.getString("PatientPanel.color.color0"));
        colorList.add(i18nPatient.getString("PatientPanel.color.color1"));
        colorList.add(i18nPatient.getString("PatientPanel.color.color2"));
        colorList.add(i18nPatient.getString("PatientPanel.color.color3"));
        colorList.add(i18nPatient.getString("PatientPanel.color.color4"));
        colorList.add(i18nPatient.getString("PatientPanel.color.color5"));
         *///colorComboBox.setModel(new DefaultComboBoxModel(colorList.toArray()));
        List<String> statusList = new ArrayList<>();
        statusList.add(i18nPatient.getString("PatientPanel.status.0"));
        statusList.add(i18nPatient.getString("PatientPanel.status.1"));
        statusComboBox.setModel(new DefaultComboBoxModel(statusList.toArray()));

    }

    private UserDAO userDAO = new UserDAO();
    private UserModel userModel;
    private PatientModel patientModel;

    private PhysicianDAO physicianDAO = new PhysicianDAO();

    public PatientModel getPatientModel() {
        return patientModel;
    }

    public void setPatientModel(PatientModel patientModel) {
        this.patientModel = patientModel;
        this.userModel = userDAO.getUser(patientModel.getIdUser());
        this.physicianModel = UserSingleton.getInstance().getPhysicianModel();

        physicianDAO.addPhysicianAttendance(UUID.randomUUID().toString(), LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), patientModel.getIdPatient(),physicianModel.getIdPhysician(), HealthInstitutionSingleton.getInstance().getIdHealthInstitution());
        patientNameTextField.setText(userModel.getName());
        patientNameTextField.setEnabled(false);

        identityDocumentTextField.setText(userModel.getIdentityDocumentFormatted());
        identityDocumentTextField.setEnabled(false);

        ageTextField.setText(userModel.getAge() == 0 ? i18nPatient.getString("PatientPanel.notInformed") : String.valueOf(userModel.getAge()));
        ageTextField.setEnabled(false);

        for (CountryModel country : CountryList.listOnlyCountries()) {
            if (country.getCode().equals(userModel.getCountry())) {
                countryComboBox.setSelectedItem(country);
            }
        }
        countryComboBox.setEnabled(false);

        if (userModel.getGender() == null) {
            genderComboBox.setSelectedIndex(0);

        } else {
            if (userModel.getGender().equals("1")) {
                genderComboBox.setSelectedIndex(1);
            }

            if (userModel.getGender().equals("2")) {
                genderComboBox.setSelectedIndex(2);
            }
            if (userModel.getGender().equals("3")) {
                genderComboBox.setSelectedIndex(3);
            }
        }

        genderComboBox.setEnabled(false);

        bloodTypeComboBox.setSelectedIndex(patientModel.getBloodType() == null ? 0 : Integer.parseInt(patientModel.getBloodType()));
        int color = patientModel.getColor() == null ? 1 : Integer.parseInt(patientModel.getColor());
        alterColor(color);
        
        statusComboBox.setSelectedIndex(Integer.parseInt(patientModel.getStatus()));

        motherNameTextField.setText(patientModel.getMotherName());
        fatherNameTextField.setText(patientModel.getFatherName());
        weightTextField.setText(String.valueOf(patientModel.getWeight()));
        heightTextField.setText(String.valueOf(patientModel.getHeight()));

        try {
            URL url = new URL("https://healthsystem.blob.core.windows.net/userhealth/" + userModel.getPhoto() + "?" + String.valueOf(System.currentTimeMillis()));
            BufferedImage buff = ImageIO.read(url);

            patientLabel.setIcon(new ImageIcon(ResizeUtil.circle(ResizeUtil.resize(buff, 128, 128))));

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                URL url = new URL("https://healthsystem.blob.core.windows.net/userhealth/USER_DEFAULT_PHOTO.jpg?" + String.valueOf(System.currentTimeMillis()));
                URLConnection con = url.openConnection();
                con.setUseCaches(false);

                InputStream is = url.openStream();
                patientLabel.setIcon(new ImageIcon(ImageIO.read(is)));
            } catch (Exception e) {
                e.printStackTrace();

            }

        }

        chargeDiagnosis();

        chargeDiseases();

        diseaseTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {

                deleteDiseaseButton.setEnabled(true);
                editDiseaseButton.setEnabled(true);
            }
        });

        diseaseTable.removeColumn(diseaseTable.getColumnModel().getColumn(0));
        diseaseTable.removeColumn(diseaseTable.getColumnModel().getColumn(0));
        //diseaseTable.removeColumn(diseaseTable.getColumnModel().getColumn(1));

        diseaseTable.setRowHeight(40);

        chargeExams();

        tagTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {

                updateTagButton.setEnabled(true);
                deleteTagButton.setEnabled(true);

            }
        });
        tagTable.removeColumn(tagTable.getColumnModel().getColumn(0));

        
        chargeTags();
        
        medicineTable.setRowHeight(40);
        chargeMedicine();
        


    }

    protected void chargeDiagnosis() {
        List<DiagnosisModel> patientDiagnosis = patientDAO.getPatientDiagnosis(patientModel.getIdPatient());
        diagnosisPanel.removeAll();
        diagnosisScrollPanel.getVerticalScrollBar().setUnitIncrement(16);

        for (DiagnosisModel diagnosisModel : patientDiagnosis) {

            JPanel jpanel = new JPanel();
            JPanel boxlayout = new JPanel();

            jpanel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    DiagnosisManagerWindow diagnosisSeeWindow = new DiagnosisManagerWindow();
                    diagnosisSeeWindow.setDiagnosisModel(diagnosisModel);
                    diagnosisSeeWindow.setPhysicianModel(physicianModel);
                    diagnosisSeeWindow.setPatientPanel(PatientPanel.this);
                    diagnosisSeeWindow.setVisible(true);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    jpanel.setBackground(new Color(52, 152, 219));
                    boxlayout.setBackground(new Color(52, 152, 219));
                    Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
                    setCursor(cursor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    jpanel.setBackground(Color.WHITE);
                    boxlayout.setBackground(Color.WHITE);
                    Cursor cursor = Cursor.getDefaultCursor();
                    setCursor(cursor);
                }
            });
            jpanel.setLayout(new BorderLayout());
            jpanel.setBackground(Color.WHITE);
            jpanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));

            String date = "";
            try{
            LocalDateTime ldt = LocalDateTime.parse(diagnosisModel.getDateDiagnosis(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));

            date = ldt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            
            }catch(Exception e){
                date = diagnosisModel.getDateDiagnosis();
            }
            boxlayout.setBackground(Color.WHITE);

            boxlayout.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));

            boxlayout.setLayout(new BoxLayout(boxlayout, BoxLayout.PAGE_AXIS));
            JLabel jLabel = new JLabel(i18nPatient.getString("DiagnosisAddWindow.physician") + ": " + diagnosisModel.getPhysicianName());
            jLabel.setFont(new java.awt.Font("Arial", 0, 12));
            boxlayout.add(jLabel);

            jLabel = new JLabel(i18nPatient.getString("DiagnosisAddWindow.physician.document") + ": " + diagnosisModel.getPhysicianPracticeNumber());
            jLabel.setFont(new java.awt.Font("Arial", 0, 12));
            boxlayout.add(jLabel);

            jLabel = new JLabel(i18nPatient.getString("DiagnosisAddWindow.healthinstitution") + ": " + diagnosisModel.getHealthInstitutionName());
            jLabel.setFont(new java.awt.Font("Arial", 0, 12));
            boxlayout.add(jLabel);

            jLabel = new JLabel(i18nPatient.getString("DiagnosisAddWindow.date") + ": " + date);
            jLabel.setFont(new java.awt.Font("Arial", 0, 12));
            boxlayout.add(jLabel);

            jpanel.add(boxlayout, BorderLayout.CENTER);

            try {
                URL url = new URL(WEBAPI.IMAGE + "healthinstitution/" + diagnosisModel.getHealthInstitutionPhoto() + "?" + System.currentTimeMillis());
                BufferedImage buff = ImageIO.read(url);
                BufferedImage resize = ResizeUtil.resize(buff, 75, 75);
                resize = ResizeUtil.circle(resize);
                jpanel.add(new JLabel(new ImageIcon(resize)), BorderLayout.LINE_START);

            } catch (Exception e) {

            }


            /*JButton seeButton = new JButton();
            seeButton.addActionListener(e -> {

            });
            seeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/binoculars.png"))); // NOI18N
            jpanel.add(seeButton, BorderLayout.LINE_END);
             */
            diagnosisPanel.add(jpanel);

        }

        diagnosisPanel.revalidate();
        diagnosisPanel.repaint();
    }

    protected void chargeExams() {
        List<ExamModel> patientExams = patientDAO.getPatientExams(patientModel.getIdPatient());
        examsPanel.removeAll();
        examScrollPanel.getVerticalScrollBar().setUnitIncrement(16);

        for (ExamModel examModel : patientExams) {

            JPanel jpanel = new JPanel();
            JPanel boxlayout = new JPanel();

            jpanel.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {

                }

                @Override
                public void mousePressed(MouseEvent e) {
                    ExamManagerWindow examManagerWindow = new ExamManagerWindow();
                    examManagerWindow.setExamModel(examModel);
                    examManagerWindow.setPhysicianModel(physicianModel);
                    examManagerWindow.setPatientPanel(PatientPanel.this);
                    examManagerWindow.setVisible(true);

                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    jpanel.setBackground(new Color(52, 152, 219));
                    boxlayout.setBackground(new Color(52, 152, 219));
                    Cursor cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
                    setCursor(cursor);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    jpanel.setBackground(Color.WHITE);
                    boxlayout.setBackground(Color.WHITE);
                    Cursor cursor = Cursor.getDefaultCursor();
                    setCursor(cursor);
                }
            });
            jpanel.setLayout(new BorderLayout());
            jpanel.setBackground(Color.WHITE);
            jpanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));

            LocalDateTime ldt = LocalDateTime.parse(examModel.getDateExam(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));

            boxlayout.setBackground(Color.WHITE);

            boxlayout.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 0));

            boxlayout.setLayout(new BoxLayout(boxlayout, BoxLayout.PAGE_AXIS));
            JLabel jLabel = new JLabel(i18nPatient.getString("DiagnosisAddWindow.physician") + ": " + examModel.getPhysicianName());
            jLabel.setFont(new java.awt.Font("Arial", 0, 12));
            boxlayout.add(jLabel);

            jLabel = new JLabel(i18nPatient.getString("DiagnosisAddWindow.physician.document") + ": " + examModel.getPhysicianPracticeNumber());
            jLabel.setFont(new java.awt.Font("Arial", 0, 12));
            boxlayout.add(jLabel);

            jLabel = new JLabel(i18nPatient.getString("DiagnosisAddWindow.healthinstitution") + ": " + examModel.getHealthInstitutionName());
            jLabel.setFont(new java.awt.Font("Arial", 0, 12));
            boxlayout.add(jLabel);

            jLabel = new JLabel(i18nPatient.getString("EmailManagerWindow.date") + ": " + ldt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            jLabel.setFont(new java.awt.Font("Arial", 0, 12));
            boxlayout.add(jLabel);

            if (examModel.getAttachmentCount() > 0) {

                JLabel labelClips = new JLabel();
                labelClips.setSize(200, 40);
                labelClips.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paper-clip-outline.png"))); // NOI18N
                boxlayout.add(labelClips);

            }
            jpanel.add(boxlayout, BorderLayout.CENTER);

            try {
                URL url = new URL(WEBAPI.IMAGE + "healthinstitution/" + examModel.getHealthInstitutionPhoto() + "?" + System.currentTimeMillis());
                BufferedImage buff = ImageIO.read(url);
                BufferedImage resize = ResizeUtil.resize(buff, 75, 75);
                resize = ResizeUtil.circle(resize);
                jpanel.add(new JLabel(new ImageIcon(resize)), BorderLayout.LINE_START);

            } catch (Exception e) {

            }


            /*JButton seeButton = new JButton();
            seeButton.addActionListener(e -> {

            });
            seeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/binoculars.png"))); // NOI18N
            jpanel.add(seeButton, BorderLayout.LINE_END);
             */
            examsPanel.add(jpanel);

        }

        examsPanel.revalidate();
        examsPanel.repaint();
    }

    public void chargeDiseases() {

        diseaseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        List<PatientHasDiseaseModel> patientDisease = patientDAO.getPatientDisease(patientModel.getIdPatient());

        for (int i = 0; i <= diseaseTable.getRowCount(); i++) {
            ((DefaultTableModel) diseaseTable.getModel()).setNumRows(0);
        }
        diseaseTable.updateUI();

        DefaultTableModel model = (DefaultTableModel) diseaseTable.getModel();
        for (PatientHasDiseaseModel patientHasDiseaseModel : patientDisease) {

            model.addRow(new Object[]{
                patientHasDiseaseModel.getIdPatientHasDisease(),
                patientHasDiseaseModel.getIdDisease(),
                patientHasDiseaseModel.getNameDisease(),
                patientHasDiseaseModel.getAnotation()});

        }

        diseaseTable.updateUI();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        patientLabel = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        patientNameTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        identityDocumentTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        ageTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        genderComboBox = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        countryComboBox = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        bloodTypeComboBox = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        weightTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        heightTextField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        statusComboBox = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        motherNameTextField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        fatherNameTextField = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        lockButton = new javax.swing.JButton();
        colorSlider = new javax.swing.JSlider();
        colorPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        addDiseaseButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        diseaseTable = new javax.swing.JTable();
        deleteDiseaseButton = new javax.swing.JButton();
        editDiseaseButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        diagnosisScrollPanel = new javax.swing.JScrollPane(diagnosisPanel);
        jButton2 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        examScrollPanel = new javax.swing.JScrollPane(examsPanel);
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        medicineTable = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tagTable = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        deleteTagButton = new javax.swing.JButton();
        updateTagButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(721, 506));
        setMinimumSize(new java.awt.Dimension(721, 506));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTabbedPane1.setMaximumSize(new java.awt.Dimension(721, 506));
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(721, 506));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        patientLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        patientLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/user_128_128.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/healthsystem/patient/Bundle"); // NOI18N
        patientLabel.setText(bundle.getString("PatientPanel.patientLabel.text")); // NOI18N

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setText(bundle.getString("PatientPanel.jLabel2.text")); // NOI18N

        patientNameTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        patientNameTextField.setText(bundle.getString("PatientPanel.patientNameTextField.text")); // NOI18N

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText(bundle.getString("PatientPanel.jLabel3.text")); // NOI18N

        identityDocumentTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        identityDocumentTextField.setText(bundle.getString("PatientPanel.identityDocumentTextField.text")); // NOI18N

        jLabel4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel4.setText(bundle.getString("PatientPanel.jLabel4.text")); // NOI18N

        ageTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        ageTextField.setText(bundle.getString("PatientPanel.ageTextField.text")); // NOI18N

        jLabel5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel5.setText(bundle.getString("PatientPanel.jLabel5.text")); // NOI18N

        genderComboBox.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel6.setText(bundle.getString("PatientPanel.jLabel6.text")); // NOI18N

        countryComboBox.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel7.setText(bundle.getString("PatientPanel.jLabel7.text")); // NOI18N

        bloodTypeComboBox.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        bloodTypeComboBox.setEnabled(false);

        jLabel8.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel8.setText(bundle.getString("PatientPanel.jLabel8.text")); // NOI18N

        weightTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        weightTextField.setText(bundle.getString("PatientPanel.weightTextField.text")); // NOI18N
        weightTextField.setEnabled(false);

        jLabel9.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel9.setText(bundle.getString("PatientPanel.jLabel9.text")); // NOI18N

        heightTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        heightTextField.setText(bundle.getString("PatientPanel.heightTextField.text")); // NOI18N
        heightTextField.setEnabled(false);

        jLabel10.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel10.setText(bundle.getString("PatientPanel.jLabel10.text")); // NOI18N

        jLabel11.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel11.setText(bundle.getString("PatientPanel.jLabel11.text")); // NOI18N

        statusComboBox.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        statusComboBox.setEnabled(false);

        jLabel12.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel12.setText(bundle.getString("PatientPanel.jLabel12.text")); // NOI18N

        motherNameTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        motherNameTextField.setText(bundle.getString("PatientPanel.motherNameTextField.text")); // NOI18N
        motherNameTextField.setEnabled(false);

        jLabel13.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel13.setText(bundle.getString("PatientPanel.jLabel13.text")); // NOI18N

        fatherNameTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        fatherNameTextField.setText(bundle.getString("PatientPanel.fatherNameTextField.text")); // NOI18N
        fatherNameTextField.setEnabled(false);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/success.png"))); // NOI18N
        jButton1.setText(bundle.getString("PatientPanel.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lockButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/locked.png"))); // NOI18N
        lockButton.setText(bundle.getString("PatientPanel.lockButton.text")); // NOI18N
        lockButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lockButtonActionPerformed(evt);
            }
        });

        colorSlider.setMaximum(36);
        colorSlider.setMinimum(1);
        colorSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                colorSliderStateChanged(evt);
            }
        });

        javax.swing.GroupLayout colorPanelLayout = new javax.swing.GroupLayout(colorPanel);
        colorPanel.setLayout(colorPanelLayout);
        colorPanelLayout.setHorizontalGroup(
            colorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );
        colorPanelLayout.setVerticalGroup(
            colorPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(51, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lockButton, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(fatherNameTextField, javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(ageTextField))
                                        .addGap(39, 39, 39))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(weightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(47, 47, 47)))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel8)
                                        .addComponent(patientLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(31, 31, 31)))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(patientNameTextField)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel5)
                                            .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(genderComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(heightTextField)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(89, 89, 89)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel6)
                                        .addComponent(countryComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel10)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(colorSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(colorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(47, 47, 47)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(bloodTypeComboBox, 0, 130, Short.MAX_VALUE)
                                        .addComponent(statusComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel11)))
                                .addComponent(identityDocumentTextField)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel3))
                                    .addGap(0, 0, Short.MAX_VALUE))))
                        .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(motherNameTextField, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(51, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(45, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(patientNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(identityDocumentTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(patientLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ageTextField)
                    .addComponent(bloodTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(countryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(genderComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(heightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(weightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(colorSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(statusComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(jLabel12)
                .addGap(7, 7, 7)
                .addComponent(motherNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fatherNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lockButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1))
                .addContainerGap(83, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(bundle.getString("PatientPanel.jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        addDiseaseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/add_disease.png"))); // NOI18N
        addDiseaseButton.setText(bundle.getString("PatientPanel.addDiseaseButton.text")); // NOI18N
        addDiseaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDiseaseButtonActionPerformed(evt);
            }
        });

        diseaseTable.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        diseaseTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                i18nPatient.getString("PatientPanel.diseaseTable.title1"),
                i18nPatient.getString("PatientPanel.diseaseTable.title2"),
                i18nPatient.getString("PatientPanel.diseaseTable.title3"),
                i18nPatient.getString("PatientPanel.diseaseTable.title4")
            }

        ){
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(diseaseTable);

        deleteDiseaseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/delete.png"))); // NOI18N
        deleteDiseaseButton.setText(bundle.getString("PatientPanel.deleteDiseaseButton.text")); // NOI18N
        deleteDiseaseButton.setEnabled(false);
        deleteDiseaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteDiseaseButtonActionPerformed(evt);
            }
        });

        editDiseaseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edit.png"))); // NOI18N
        editDiseaseButton.setText(bundle.getString("PatientPanel.editDiseaseButton.text")); // NOI18N
        editDiseaseButton.setEnabled(false);
        editDiseaseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editDiseaseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addDiseaseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editDiseaseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteDiseaseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(addDiseaseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteDiseaseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editDiseaseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("PatientPanel.jPanel4.TabConstraints.tabTitle"), jPanel4); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        diagnosisScrollPanel.setBackground(new java.awt.Color(255, 255, 255));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/medical-records.png"))); // NOI18N
        jButton2.setText(bundle.getString("PatientPanel.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(diagnosisScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(diagnosisScrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("PatientPanel.jPanel1.TabConstraints.tabTitle_1"), jPanel1); // NOI18N

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/exam.png"))); // NOI18N
        jButton3.setText(bundle.getString("PatientPanel.jButton3.text")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        examScrollPanel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(examScrollPanel)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 667, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(examScrollPanel)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("PatientPanel.jPanel5.TabConstraints.tabTitle"), jPanel5); // NOI18N

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));

        medicineTable.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        medicineTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                i18nPatient.getString("PatientPanel.medicineTable.title1")

            }

        ){
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(medicineTable);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sync.png"))); // NOI18N
        jButton5.setText(bundle.getString("PatientPanel.jButton5.text")); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("PatientPanel.jPanel7.TabConstraints.tabTitle"), jPanel7); // NOI18N

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));

        tagTable.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        tagTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                i18nPatient.getString("PatientPanel.tagTable.title0"),
                i18nPatient.getString("PatientPanel.tagTable.title1"),
                i18nPatient.getString("PatientPanel.tagTable.title2")    }

        ){
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tagTable);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/add.png"))); // NOI18N
        jButton4.setText(bundle.getString("PatientPanel.jButton4.text")); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        deleteTagButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/delete.png"))); // NOI18N
        deleteTagButton.setText(bundle.getString("PatientPanel.deleteTagButton.text")); // NOI18N
        deleteTagButton.setEnabled(false);
        deleteTagButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteTagButtonActionPerformed(evt);
            }
        });

        updateTagButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edit.png"))); // NOI18N
        updateTagButton.setText(bundle.getString("PatientPanel.updateTagButton.text")); // NOI18N
        updateTagButton.setEnabled(false);
        updateTagButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateTagButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(updateTagButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteTagButton, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(updateTagButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteTagButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab(bundle.getString("PatientPanel.jPanel6.TabConstraints.tabTitle"), jPanel6); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleName(bundle.getString("PatientPanel.AccessibleContext.accessibleName")); // NOI18N
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        ExamAddWindow examAddWindow = new ExamAddWindow(patientModel.getIdPatient(), physicianModel.getIdPhysician());
        examAddWindow.setPatientPanel(PatientPanel.this);

        examAddWindow.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        DiagnosisAddWindow diagnosisAddWindow = new DiagnosisAddWindow(patientModel.getIdPatient(), physicianModel.getIdPhysician());
        diagnosisAddWindow.setPatientPanel(this);

        diagnosisAddWindow.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void editDiseaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editDiseaseButtonActionPerformed
        DefaultTableModel model = (DefaultTableModel) diseaseTable.getModel();
        if (diseaseTable.getRowCount() > 0) {
            if (diseaseTable.getSelectedRowCount() > 0) {
                int selectedRow[] = diseaseTable.getSelectedRows();
                for (int i : selectedRow) {
                    String id = diseaseTable.getModel().getValueAt(i, 0).toString();
                    String idDisease = diseaseTable.getModel().getValueAt(i, 1).toString();
                    String anotation = diseaseTable.getModel().getValueAt(i, 3).toString();

                    DiseaseUpdateWindow diseaseUpdateWindow = new DiseaseUpdateWindow();
                    diseaseUpdateWindow.setPatientPanel(this);
                    diseaseUpdateWindow.setPatientDisease(idDisease, patientModel.getIdPatient(), id, anotation);
                    diseaseUpdateWindow.setVisible(true);
                }
            }
        }
    }//GEN-LAST:event_editDiseaseButtonActionPerformed

    private void deleteDiseaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteDiseaseButtonActionPerformed
        DefaultTableModel model = (DefaultTableModel) diseaseTable.getModel();
        if (diseaseTable.getRowCount() > 0) {
            if (diseaseTable.getSelectedRowCount() > 0) {
                boolean success = true;

                int showConfirmDialog = JOptionPane.showConfirmDialog(null, i18nPatient.getString("PatientPanel.confirmDelete"));
                if (showConfirmDialog == JOptionPane.YES_OPTION) {
                    int selectedRow[] = diseaseTable.getSelectedRows();
                    for (int i : selectedRow) {
                        String id = diseaseTable.getModel().getValueAt(i, 0).toString();
                        boolean ok = patientDAO.unbindDisease(id);
                        if (!ok) {
                            success = false;
                        } else {
                            deleteDiseaseButton.setEnabled(false);
                            editDiseaseButton.setEnabled(false);
                            model.removeRow(i);

                        }
                    }

                    if (success) {
                        JOptionPane.showMessageDialog(null, i18nPatient.getString("PatientPanel.delete"));
                    }
                }

            }
        }
    }//GEN-LAST:event_deleteDiseaseButtonActionPerformed

    private void addDiseaseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDiseaseButtonActionPerformed

        DiseaseAddWindow diseaseAddWindow = new DiseaseAddWindow();
        diseaseAddWindow.setPatientPanel(this);
        diseaseAddWindow.setPatientModel(patientModel);
        diseaseAddWindow.setVisible(true);
    }//GEN-LAST:event_addDiseaseButtonActionPerformed

    private void lockButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lockButtonActionPerformed
        isLocked = !isLocked;
        if (isLocked) {
            lockButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/locked.png"))); // NOI18N
            bloodTypeComboBox.setEnabled(false);
            colorSlider.setEnabled(false);
            motherNameTextField.setEnabled(false);
            fatherNameTextField.setEnabled(false);
            heightTextField.setEnabled(false);
            weightTextField.setEnabled(false);
            statusComboBox.setEnabled(false);

        } else {
            lockButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/unlocked.png"))); // NOI18N
            bloodTypeComboBox.setEnabled(true);
            colorSlider.setEnabled(true);
            motherNameTextField.setEnabled(true);
            fatherNameTextField.setEnabled(true);
            heightTextField.setEnabled(true);
            weightTextField.setEnabled(true);
            statusComboBox.setEnabled(true);

        }
    }//GEN-LAST:event_lockButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        boolean success = true;

        try {

            Double.parseDouble(weightTextField.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, i18nPatient.getString("PatientPanel.validation.weight"));
            success = false;
        }

        if (success) {
            try {

                double num = Double.parseDouble(weightTextField.getText());

                if (num < 0 || num >= 1000) {
                    throw new Exception();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, i18nPatient.getString("PatientPanel.validation.weight.invalid"));
                success = false;
            }
        }

        if (success) {
            try {
                Double.parseDouble(heightTextField.getText());

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, i18nPatient.getString("PatientPanel.validation.height"));
                success = false;
            }

        }

        if (success) {
            try {

                double num = Double.parseDouble(heightTextField.getText());

                if (num < 0.0 || num >= 3.0) {
                    throw new Exception();
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, i18nPatient.getString("PatientPanel.validation.height.invalid"));
                success = false;
            }
        }

        if (success) {
            boolean ok = patientDAO.update(
                    patientModel.getIdPatient(),
                    String.valueOf(bloodTypeComboBox.getSelectedIndex()),
                    String.valueOf(colorSlider.getValue()),
                    fatherNameTextField.getText(),
                    motherNameTextField.getText(),
                    weightTextField.getText(),
                    heightTextField.getText(),
                    String.valueOf(statusComboBox.getSelectedIndex()),
                    userModel.getId()
            );

            if (ok) {
                JOptionPane.showMessageDialog(null, i18nPatient.getString("PatientPanel.update.success"));
            }

        }

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        AddPatientTagWindow addPatientTagWindow = new AddPatientTagWindow();
        addPatientTagWindow.setPatientPanel(this);
        addPatientTagWindow.setPatientId(patientModel.getIdPatient());
        addPatientTagWindow.setVisible(true);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void deleteTagButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteTagButtonActionPerformed
        DefaultTableModel model = (DefaultTableModel) tagTable.getModel();
        if (tagTable.getRowCount() > 0) {
            if (tagTable.getSelectedRowCount() > 0) {
                boolean success = true;

                int showConfirmDialog = JOptionPane.showConfirmDialog(null, i18nPatient.getString("PatientPanel.deleteTagMessage"));
                if (showConfirmDialog == JOptionPane.YES_OPTION) {
                    int selectedRow[] = tagTable.getSelectedRows();
                    for (int i : selectedRow) {
                        String id = tagTable.getModel().getValueAt(i, 0).toString();
                        boolean ok = patientDAO.deleteTag(id);
                        if (!ok) {
                            success = false;
                        } else {
                            model.removeRow(i);

                        }
                    }

                    if (success) {
                        JOptionPane.showMessageDialog(null, i18nPatient.getString("PatientPanel.deletedTagMessage"));
                    }
                }

            }
        }
    }//GEN-LAST:event_deleteTagButtonActionPerformed

    private void updateTagButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateTagButtonActionPerformed
        DefaultTableModel model = (DefaultTableModel) tagTable.getModel();
        if (tagTable.getRowCount() > 0) {
            if (tagTable.getSelectedRowCount() > 0) {
                boolean success = true;

                int selectedRow[] = tagTable.getSelectedRows();
                for (int i : selectedRow) {
                    String id = tagTable.getModel().getValueAt(i, 0).toString();
                    String name = tagTable.getModel().getValueAt(i, 1).toString();

                    String typeOfTag = getTagType(tagTable.getModel().getValueAt(i, 2).toString());

                    PatientTagModel patientTagModel = new PatientTagModel();
                    patientTagModel.setIdPatientTag(id);
                    patientTagModel.setName(name);
                    patientTagModel.setTagType(typeOfTag);

                    UpdatePatientTagWindow updatePatientTagWindow = new UpdatePatientTagWindow();
                    updatePatientTagWindow.setPatientPanel(this);
                    updatePatientTagWindow.setTagModel(patientTagModel);
                    updatePatientTagWindow.setVisible(true);

                }

            }
        }
    }//GEN-LAST:event_updateTagButtonActionPerformed

    private void colorSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_colorSliderStateChanged
        int progress = colorSlider.getValue();
        alterColor(progress);
        

    }//GEN-LAST:event_colorSliderStateChanged

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        chargeMedicine();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void alterColor(int progress){
        if (progress == 1) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#f4f2f5"));
        }
        if (progress == 2) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#ecebe9"));
        }
        if (progress == 3) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#faf9f7"));
        }
        if (progress == 4) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#fdfbe6"));
        }
        if (progress == 5) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#fdf6e6"));
        }
        if (progress == 6) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#fef7e5"));
        }
        if (progress == 7) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#faf0ef"));
        }
        if (progress == 8) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#f3eae5"));
        }
        if (progress == 9) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#f4f1ea"));
        }
        if (progress == 10) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#fbfcf4"));
        }
        if (progress == 11) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#fcf8ed"));
        }
        if (progress == 12) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#fef6e1"));
        }
        if (progress == 13) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#fff9e1"));
        }
        if (progress == 14) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#fff9e1"));
        }
        if (progress == 15) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#f1e7c3"));
        }
        if (progress == 16) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#efe2ad"));
        }
        if (progress == 17) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#e0d293"));
        }
        if (progress == 18) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#f2e297"));
        }
        if (progress == 19) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#ebd69f"));
        }
        if (progress == 20) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#ebd985"));
        }
        if (progress == 21) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#e3c467"));
        }
        if (progress == 22) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#e1c16a"));
        }
        if (progress == 23) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#dfc17b"));
        }
        if (progress == 24) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#deb877"));
        }
        if (progress == 25) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#c7a464"));
        }
        if (progress == 26) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#bc9762"));
        }
        if (progress == 27) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#9c6b43"));
        }
        if (progress == 28) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#8e583e"));
        }
        if (progress == 29) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#794d30"));
        }
        if (progress == 30) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#643116"));
        }
        if (progress == 31) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#653020"));
        }
        if (progress == 32) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#603121"));
        }
        if (progress == 33) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#603121"));
        }
        if (progress == 34) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#402015"));
        }
        if (progress == 35) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#312529"));
        }
        if (progress == 36) {
            colorPanel.setBackground(ColorManager.hex2Rgb("#1b1c2e"));
        }

    }
    
    private JPanel diagnosisPanel = new JPanel(new GridLayout(0, 2));
    private JPanel examsPanel = new JPanel(new GridLayout(0, 2));

    private PhysicianModel physicianModel;
    private DiseaseList diseaseList = DiseaseList.getInstance();

    private boolean isLocked = true;
    private PatientDAO patientDAO = new PatientDAO();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addDiseaseButton;
    private javax.swing.JTextField ageTextField;
    private javax.swing.JComboBox<String> bloodTypeComboBox;
    private javax.swing.JPanel colorPanel;
    private javax.swing.JSlider colorSlider;
    private javax.swing.JComboBox<String> countryComboBox;
    private javax.swing.JButton deleteDiseaseButton;
    private javax.swing.JButton deleteTagButton;
    private javax.swing.JScrollPane diagnosisScrollPanel;
    private javax.swing.JTable diseaseTable;
    private javax.swing.JButton editDiseaseButton;
    private javax.swing.JScrollPane examScrollPanel;
    private javax.swing.JTextField fatherNameTextField;
    private javax.swing.JComboBox<String> genderComboBox;
    private javax.swing.JTextField heightTextField;
    private javax.swing.JTextField identityDocumentTextField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton lockButton;
    private javax.swing.JTable medicineTable;
    private javax.swing.JTextField motherNameTextField;
    private javax.swing.JLabel patientLabel;
    private javax.swing.JTextField patientNameTextField;
    private javax.swing.JComboBox<String> statusComboBox;
    private javax.swing.JTable tagTable;
    private javax.swing.JButton updateTagButton;
    private javax.swing.JTextField weightTextField;
    // End of variables declaration//GEN-END:variables

    public void chargeTags() {

        updateTagButton.setEnabled(false);
        deleteTagButton.setEnabled(false);

        tagTable.setRowHeight(40);
        tagTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        List<PatientTagModel> patientTag = patientDAO.getPatientTags(patientModel.getIdPatient());

        for (int i = 0; i <= tagTable.getRowCount(); i++) {
            ((DefaultTableModel) tagTable.getModel()).setNumRows(0);
        }
        tagTable.updateUI();

        DefaultTableModel model = (DefaultTableModel) tagTable.getModel();
        for (PatientTagModel patientTagModel : patientTag) {

            String tagDescription = i18nPatient.getString("UpdatePatientTagWindow.typeofequipament.0");

            if (patientTagModel.getTagType().equals("1")) {
                tagDescription = i18nPatient.getString("UpdatePatientTagWindow.typeofequipament.1");
            }

            if (patientTagModel.getTagType().equals("2")) {
                tagDescription = i18nPatient.getString("UpdatePatientTagWindow.typeofequipament.2");
            }

            if (patientTagModel.getTagType().equals("3")) {
                tagDescription = i18nPatient.getString("UpdatePatientTagWindow.typeofequipament.3");
            }

            if (patientTagModel.getTagType().equals("4")) {
                tagDescription = i18nPatient.getString("UpdatePatientTagWindow.typeofequipament.4");
            }

            model.addRow(new Object[]{
                patientTagModel.getIdPatientTag(),
                patientTagModel.getName(),
                tagDescription});

        }

        tagTable.updateUI();
    }

    
    public void chargeMedicine() {

      
        medicineTable.setRowHeight(40);
        medicineTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        List<MedicineModel> patientMedicineList = patientDAO.getPatientMedicines(patientModel.getIdPatient());

        for (int i = 0; i <= medicineTable.getRowCount(); i++) {
            ((DefaultTableModel) medicineTable.getModel()).setNumRows(0);
        }
        medicineTable.updateUI();

        DefaultTableModel model = (DefaultTableModel) medicineTable.getModel();
        for (MedicineModel medicineModel : patientMedicineList) {

            model.addRow(new Object[]{
                medicineModel.getName()
               });

        }

        medicineTable.updateUI();
    }

    
    private String getTagType(String tag) {
        if (tag.equals(i18nPatient.getString("UpdatePatientTagWindow.typeofequipament.1"))) {
            return "1";
        }

        if (tag.equals(i18nPatient.getString("UpdatePatientTagWindow.typeofequipament.2"))) {
            return "2";
        }

        if (tag.equals(i18nPatient.getString("UpdatePatientTagWindow.typeofequipament.3"))) {
            return "3";
        }

        if (tag.equals(i18nPatient.getString("UpdatePatientTagWindow.typeofequipament.4"))) {
            return "4";
        }
        return "0";
    }

}
