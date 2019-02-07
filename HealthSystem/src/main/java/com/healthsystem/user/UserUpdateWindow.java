/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.user;

import com.healthsystem.util.EmailManager;
import com.healthsystem.healthinstitution.*;
import com.healthsystem.user.nurse.NurseDAO;
import com.healthsystem.user.nurse.NurseModel;
import com.healthsystem.user.physician.PhysicianDAO;
import com.healthsystem.user.physician.PhysicianModel;
import com.healthsystem.user.specialization.SpecializationInterfaceModel;
import com.healthsystem.user.specialization.SpecializationModel;
import com.healthsystem.util.dataprovider.CountryList;
import com.healthsystem.util.dataprovider.CountryModel;
import com.healthsystem.util.Criptography;
import com.healthsystem.util.DocumentValidator;
import com.healthsystem.util.ResizeUtil;
import com.healthsystem.util.ViewConfigurable;
import com.healthsystem.util.azure.AzureBlob;
import com.healthsystem.util.azure.ImageFilter;
import com.healthsystem.util.dataprovider.NurseTypeList;
import com.healthsystem.util.dataprovider.NurseTypeModel;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * @author Ramon Lacava Gutierrez Gonçales
 * @version 1.0.0
 * @date 30/06/2018 19:33:20
 */
public class UserUpdateWindow extends JDialog implements ViewConfigurable {

    private java.util.ResourceBundle i18n = java.util.ResourceBundle.getBundle("com/healthsystem/user/Bundle"); // NOI18N

    private JFileChooser fc;

    private UserPanel userPanel;

    private UserModel userModel;

    /**
     * Cria um novo formulário HealthInstitutionAddWindow
     */
    public UserUpdateWindow() {

        setResizable(false);

        initComponents();
        setLocationRelativeTo(null);

        countryComboBox.setModel(new DefaultComboBoxModel(CountryList.listOnlyCountries().toArray()));
        countryComboBox.setSelectedIndex(1);

        ArrayList<String> list = new ArrayList<>();
        if (UserSingleton.getInstance().isAdmin()) {
            list.add(i18n.getString("UserAddWindow.usetype0"));

        }
        list.add(i18n.getString("UserAddWindow.usetype1"));

        //}
        list.add(i18n.getString("UserAddWindow.usetype2"));
        list.add(i18n.getString("UserAddWindow.usetype3"));
        list.add(i18n.getString("UserAddWindow.usetype4"));

        userTypeComboBox.setModel(new DefaultComboBoxModel(list.toArray()));
        countryComboBox.setSelectedIndex(1);

        fc = new JFileChooser();
        fc.setFileFilter(new ImageFilter());
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        List<String> genderList = new ArrayList<>();
        genderList.add(i18n.getString("UserAddWindow.gender.man"));
        genderList.add(i18n.getString("UserAddWindow.gender.woman"));
        genderList.add(i18n.getString("UserAddWindow.gender.other"));
        genderComboBox.setModel(new DefaultComboBoxModel(genderList.toArray()));

    }

    private boolean isOwnerAccount;

    public void setUser(UserModel userModel, boolean isOwnerAccount) {
        this.userModel = userModel;
        this.isOwnerAccount = isOwnerAccount;
        loginTextField.setText(userModel.getLogin());
        nameTextField.setText(userModel.getName());
        if (userModel.getTypeOfUser().equals("0")) {
            userTypeComboBox.setSelectedItem(i18n.getString("UserAddWindow.usetype0"));
        }

        if (userModel.getTypeOfUser().equals("1")) {
            userTypeComboBox.setSelectedItem(i18n.getString("UserAddWindow.usetype1"));
        }
        if (userModel.getTypeOfUser().equals("2")) {
            userTypeComboBox.setSelectedItem(i18n.getString("UserAddWindow.usetype2"));
            physicianModel = physicianDAO.getPhysician(userModel.getId());
            listPhysicianSpecialization = physicianDAO.listPhysicianSpecialization(physicianModel.getIdPhysician());

        }

        if (userModel.getTypeOfUser().equals("3")) {
            userTypeComboBox.setSelectedItem(i18n.getString("UserAddWindow.usetype3"));
        }
        if (userModel.getTypeOfUser().equals("4")) {
            userTypeComboBox.setSelectedItem(i18n.getString("UserAddWindow.usetype4"));
            nurseModel = nurseDAO.getNurse(userModel.getId());

            System.out.println(nurseModel.getIdNurse() + "------------");

            listNurseSpecialization = nurseDAO.listNurseSpecialization(nurseModel.getIdNurse());

        }
        userTypeComboBox.setEnabled(false);

        for (CountryModel country : CountryList.listOnlyCountries()) {
            if (country.getCode().equals(userModel.getCountry())) {
                countryComboBox.setSelectedItem(country);
            }
        }

        if (userModel.getIdentityDocument().length() == 36) {
            try {
                identityDocumentTextField.setFormatterFactory(null);
            } catch (Exception e) {

            }
        }

        identityDocumentTextField.setText(userModel.getIdentityDocument());
        postalCodeTextField.setText(userModel.getPostalCode());
        stateTextField.setText(userModel.getState());
        cityTextField.setText(userModel.getCity());
        neighborhoodTextField.setText(userModel.getNeighborhood());
        streetTextField.setText(userModel.getStreet());
        numberTextField.setText(userModel.getNumber());

        System.out.println(userModel.getTelephone() + " di9jsadijadjisadia");
        telephoneTextField.setText(userModel.getTelephone());

        if (userModel.getGender().equals("1")) {
            genderComboBox.setSelectedIndex(0);
        }

        if (userModel.getGender().equals("2")) {
            genderComboBox.setSelectedIndex(1);
        }
        if (userModel.getGender().equals("3")) {
            genderComboBox.setSelectedIndex(2);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            bornDateDatePicker.setDate(sdf.parse(userModel.getBornDate()));

        } catch (ParseException ex) {
        }

        try {
            URL url = new URL("https://healthsystem.blob.core.windows.net/userhealth/" + userModel.getPhoto() + "?" + String.valueOf(System.currentTimeMillis()));
            userHealthPhoto.setIcon(new ImageIcon(url));
            uploadImage = ImageIO.read(url);

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                URL url = new URL("https://healthsystem.blob.core.windows.net/userhealth/USER_DEFAULT_PHOTO.jpg?" + String.valueOf(System.currentTimeMillis()));
                URLConnection con = url.openConnection();
                con.setUseCaches(false);

                InputStream is = url.openStream();
                userHealthPhoto.setIcon(new ImageIcon(ImageIO.read(is)));
                uploadImage = ImageIO.read(is);
            } catch (Exception e) {
                e.printStackTrace();

            }

        }

        if (!isOwnerAccount) {
            disableEdit();
            setTitle(i18n.getString("UserUpdateWindow.view"));

        } else {
            setTitle(i18n.getString("UserUpdateWindow.update"));
        }

        if (userModel.getIdentityDocument().length() == 36) {
            disableEdit();

            loginTextField.setEnabled(true);
            passwordPasswordField.setEnabled(true);
            confirmPasswordPasswordField.setEnabled(true);
            nameTextField.setEnabled(true);
            selectImageButton.setEnabled(true);
            updateUserButton.setEnabled(true);
        }

        if (userModel.getTypeOfUser().equals("0") || userModel.getTypeOfUser().equals("1")) {
            dadosTab.removeTabAt(1);
            dadosTab.removeTabAt(1);

        }

        if (userModel.getTypeOfUser().equals("2")) {
            dadosTab.removeTabAt(2);

            practiceDocumentTextField.setValue(physicianModel.getPracticeDocument().trim());
        }

        if (userModel.getTypeOfUser().equals("4")) {
            dadosTab.removeTabAt(2);

            nurseRegisterTextField.setValue(nurseModel.getNurseCode());

            List<NurseTypeModel> nurseTypes = NurseTypeList.getNurseTypes(((CountryModel) countryComboBox.getSelectedItem()).getCode());

            for (NurseTypeModel nurseType : nurseTypes) {
                if (nurseType.getCode().equals(nurseModel.getNurseType())) {
                    nurseTypeComboBox.setSelectedItem(nurseType);
                }
            }
            //practiceDocumentTextField.setValue(physicianModel.getPracticeDocument().trim());
        }

    }

    private List<SpecializationModel> listNurseSpecialization;
    private List<SpecializationModel> listPhysicianSpecialization;
    private List<SpecializationModel> listSpecialization;

    private PhysicianDAO physicianDAO = new PhysicianDAO();
    private NurseDAO nurseDAO = new NurseDAO();

    private PhysicianModel physicianModel;
    private NurseModel nurseModel;

    private void updatePhysician() {
        if (userTypeComboBox.getSelectedItem().toString().charAt(0) == '0' || userTypeComboBox.getSelectedItem().toString().charAt(0) == '1'
                || userTypeComboBox.getSelectedItem().toString().charAt(0) == '3' || userTypeComboBox.getSelectedItem().toString().charAt(0) == '4') {
            if (dadosTab.getTabCount() == 2) {
                dadosTab.removeTabAt(1);
            }
        }

        if (userTypeComboBox.getSelectedItem().toString().charAt(0) == '2') {

            practiceDocumentTextField.setText("");
            practiceDocumentTextField.setFocusLostBehavior(JFormattedTextField.PERSIST);
            if (!isOwnerAccount) {
                practiceDocumentTextField.setEnabled(false);
            }
            specializationPhysicianPanel.removeAll();
            specializationList.clear();

            dadosTab.insertTab(i18n.getString("UserAddWindow.physicianPanel.TabConstraints.tabTitle"), null, physicianPanel, "", 1);

            jscrollPanel.getVerticalScrollBar().setUnitIncrement(16);

            for (SpecializationModel specializationModel : listSpecialization) {

                JCheckBox jCheckBox = new JCheckBox(specializationModel.getName());

                if (listPhysicianSpecialization.contains(specializationModel)) {
                    jCheckBox.setSelected(true);
                }
                if (!isOwnerAccount) {
                    jCheckBox.setEnabled(false);
                }

                specializationPhysicianPanel.add(jCheckBox);

                SpecializationInterfaceModel specializationInterfaceModel = new SpecializationInterfaceModel();
                specializationInterfaceModel.setComboBox(jCheckBox);
                specializationInterfaceModel.setId(specializationModel.getIdSpecialization());
                specializationInterfaceModel.setName(specializationInterfaceModel.getName());
                specializationInterfaceModel.setActivated(listPhysicianSpecialization.contains(specializationModel));
                specializationList.add(specializationInterfaceModel);

            }
            specializationPhysicianPanel.revalidate();
            specializationPhysicianPanel.repaint();

        }

    }

    private void updateNurse() {
        if (userTypeComboBox.getSelectedItem().toString().charAt(0) == '0' || userTypeComboBox.getSelectedItem().toString().charAt(0) == '1'
                || userTypeComboBox.getSelectedItem().toString().charAt(0) == '2' || userTypeComboBox.getSelectedItem().toString().charAt(0) == '3') {
            if (dadosTab.getTabCount() == 2) {
                dadosTab.removeTabAt(1);
            }
        }

        if (userTypeComboBox.getSelectedItem().toString().charAt(0) == '4') {

            nurseTypeComboBox.setModel(new DefaultComboBoxModel(NurseTypeList.getNurseTypes(((CountryModel) countryComboBox.getSelectedItem()).getCode()).toArray()));
            nurseTypeComboBox.setSelectedIndex(0);
            nurseRegisterTextField.setText("");
            nurseRegisterTextField.setFocusLostBehavior(JFormattedTextField.PERSIST);
            if (!isOwnerAccount) {
                nurseRegisterTextField.setEnabled(false);
                nurseTypeComboBox.setEnabled(false);
            }
            specializationNursePanel.removeAll();
            specializationList.clear();

            dadosTab.insertTab(i18n.getString("UserAddWindow.nursePanel.TabConstraints.tabTitle"), null, nursePanel, "", 1);

            jscrollPanelNurse.getVerticalScrollBar().setUnitIncrement(16);

            for (SpecializationModel specializationModel : listSpecialization) {

                JCheckBox jCheckBox = new JCheckBox(specializationModel.getName());

                if (listNurseSpecialization.contains(specializationModel)) {
                    jCheckBox.setSelected(true);
                }
                if (!isOwnerAccount) {
                    jCheckBox.setEnabled(false);
                }

                specializationNursePanel.add(jCheckBox);

                SpecializationInterfaceModel specializationInterfaceModel = new SpecializationInterfaceModel();
                specializationInterfaceModel.setComboBox(jCheckBox);
                specializationInterfaceModel.setId(specializationModel.getIdSpecialization());
                specializationInterfaceModel.setName(specializationInterfaceModel.getName());
                specializationInterfaceModel.setActivated(listNurseSpecialization.contains(specializationModel));
                specializationList.add(specializationInterfaceModel);

            }
            specializationPhysicianPanel.revalidate();
            specializationPhysicianPanel.repaint();

        }

    }

    private List<SpecializationInterfaceModel> specializationList = new ArrayList<>();

    private void disableEdit() {
        loginTextField.setEnabled(false);
        passwordPasswordField.setEnabled(false);
        confirmPasswordPasswordField.setEnabled(false);
        nameTextField.setEnabled(false);
        userTypeComboBox.setEnabled(false);
        identityDocumentTextField.setEnabled(false);
        postalCodeTextField.setEnabled(false);
        countryComboBox.setEnabled(false);
        stateTextField.setEnabled(false);
        cityTextField.setEnabled(false);
        streetTextField.setEnabled(false);
        neighborhoodTextField.setEnabled(false);
        numberTextField.setEnabled(false);
        telephoneTextField.setEnabled(false);
        genderComboBox.setEnabled(false);
        bornDateDatePicker.setEnabled(false);
        selectImageButton.setEnabled(false);
        updateUserButton.setEnabled(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dadosTab = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        bornDateDatePicker = new org.jdesktop.swingx.JXDatePicker();
        jLabel1 = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        countryComboBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        stateTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cityTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        neighborhoodTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        streetTextField = new javax.swing.JTextField();
        numberTextField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        userHealthPhoto = new javax.swing.JLabel();
        selectImageButton = new javax.swing.JButton();
        postalCodeTextField = new javax.swing.JFormattedTextField();
        identityDocumentTextField = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();
        telephoneTextField = new javax.swing.JFormattedTextField();
        userTypeComboBox = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        passwordPasswordField = new javax.swing.JPasswordField();
        jLabel14 = new javax.swing.JLabel();
        confirmPasswordPasswordField = new javax.swing.JPasswordField();
        loginTextField = new javax.swing.JTextField();
        genderComboBox = new javax.swing.JComboBox<>();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        physicianPanel = new javax.swing.JPanel();
        searchSpecializationsTextField = new javax.swing.JTextField();
        practiceDocumentTextField = new javax.swing.JFormattedTextField();
        jLabel18 = new javax.swing.JLabel();
        jscrollPanel = new javax.swing.JScrollPane(specializationPhysicianPanel);
        practiceDocumentLabel = new javax.swing.JLabel();
        nursePanel = new javax.swing.JPanel();
        nurseRegisterLabel = new javax.swing.JLabel();
        nurseRegisterTextField = new javax.swing.JFormattedTextField();
        nurseTypeComboBox = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        jscrollPanelNurse = new javax.swing.JScrollPane(specializationNursePanel);
        jLabel20 = new javax.swing.JLabel();
        searchSpecializationsNurseTextField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        updateUserButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        dadosTab.setBackground(new java.awt.Color(255, 255, 255));
        dadosTab.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        bornDateDatePicker.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        bornDateDatePicker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bornDateDatePickerActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/healthsystem/user/Bundle"); // NOI18N
        jLabel1.setText(bundle.getString("UserUpdateWindow.jLabel1.text")); // NOI18N

        nameTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        nameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextFieldActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel2.setText(bundle.getString("UserUpdateWindow.jLabel2.text")); // NOI18N

        countryComboBox.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        countryComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countryComboBoxActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel3.setText(bundle.getString("UserUpdateWindow.jLabel3.text")); // NOI18N

        jLabel4.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel4.setText(bundle.getString("UserUpdateWindow.jLabel4.text")); // NOI18N

        stateTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel5.setText(bundle.getString("UserUpdateWindow.jLabel5.text")); // NOI18N

        cityTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel6.setText(bundle.getString("UserUpdateWindow.jLabel6.text")); // NOI18N

        neighborhoodTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel7.setText(bundle.getString("UserUpdateWindow.jLabel7.text")); // NOI18N

        streetTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        numberTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel8.setText(bundle.getString("UserUpdateWindow.jLabel8.text")); // NOI18N

        jLabel9.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel9.setText(bundle.getString("UserUpdateWindow.jLabel9.text")); // NOI18N

        userHealthPhoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/user_128_128.png"))); // NOI18N
        userHealthPhoto.setMaximumSize(new java.awt.Dimension(150, 150));
        userHealthPhoto.setMinimumSize(new java.awt.Dimension(150, 150));
        userHealthPhoto.setPreferredSize(new java.awt.Dimension(150, 150));

        selectImageButton.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        selectImageButton.setText(bundle.getString("UserUpdateWindow.selectImageButton.text")); // NOI18N
        selectImageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectImageButtonActionPerformed(evt);
            }
        });

        try {
            postalCodeTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        postalCodeTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        postalCodeTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                postalCodeTextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                postalCodeTextFieldKeyTyped(evt);
            }
        });

        try {
            identityDocumentTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        identityDocumentTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        identityDocumentTextField.setMaximumSize(new java.awt.Dimension(6, 21));
        identityDocumentTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                identityDocumentTextFieldActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel10.setText(bundle.getString("UserUpdateWindow.jLabel10.text")); // NOI18N

        try {
            telephoneTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        telephoneTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        userTypeComboBox.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        userTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userTypeComboBoxActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel11.setText(bundle.getString("UserUpdateWindow.jLabel11.text")); // NOI18N

        jLabel12.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel12.setText(bundle.getString("UserUpdateWindow.jLabel12.text")); // NOI18N

        jLabel13.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel13.setText(bundle.getString("UserUpdateWindow.jLabel13.text")); // NOI18N

        passwordPasswordField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        passwordPasswordField.setText(bundle.getString("UserUpdateWindow.passwordPasswordField.text")); // NOI18N
        passwordPasswordField.setMaximumSize(new java.awt.Dimension(6, 21));

        jLabel14.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel14.setText(bundle.getString("UserUpdateWindow.jLabel14.text")); // NOI18N

        confirmPasswordPasswordField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        confirmPasswordPasswordField.setText(bundle.getString("UserUpdateWindow.confirmPasswordPasswordField.text")); // NOI18N
        confirmPasswordPasswordField.setMaximumSize(new java.awt.Dimension(6, 21));

        loginTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        genderComboBox.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        genderComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genderComboBoxActionPerformed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel15.setText(bundle.getString("UserUpdateWindow.jLabel15.text")); // NOI18N

        jLabel16.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel16.setText(bundle.getString("UserUpdateWindow.jLabel16.text")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(9, 9, 9)
                                        .addComponent(userHealthPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(selectImageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(383, 383, 383))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(identityDocumentTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(postalCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(stateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(203, 203, 203)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(198, 198, 198)
                                        .addComponent(confirmPasswordPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(passwordPasswordField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(loginTextField)
                                                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addComponent(nameTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(userTypeComboBox, 0, 192, Short.MAX_VALUE)
                                                .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addComponent(countryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                        .addGap(31, 31, 31))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cityTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(neighborhoodTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(21, 21, 21)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(streetTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(bornDateDatePicker, javax.swing.GroupLayout.DEFAULT_SIZE, 183, Short.MAX_VALUE)
                                .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(numberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(telephoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(genderComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(userHealthPhoto, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(selectImageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel11))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(loginTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(userTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel13))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(passwordPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(confirmPasswordPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(countryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(identityDocumentTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(postalCodeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(stateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(neighborhoodTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(streetTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(35, 35, 35)))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel8)
                    .addComponent(jLabel15))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(numberTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(telephoneTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(genderComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bornDateDatePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        dadosTab.addTab(bundle.getString("UserUpdateWindow.jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        searchSpecializationsTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        searchSpecializationsTextField.setText(bundle.getString("UserUpdateWindow.searchSpecializationsTextField.text")); // NOI18N
        searchSpecializationsTextField.setToolTipText(bundle.getString("UserUpdateWindow.searchSpecializationsTextField.toolTipText")); // NOI18N
        searchSpecializationsTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchSpecializationsTextFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchSpecializationsTextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchSpecializationsTextFieldKeyTyped(evt);
            }
        });

        try {
            practiceDocumentTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#######")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        practiceDocumentTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel18.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel18.setText(bundle.getString("UserUpdateWindow.jLabel18.text")); // NOI18N

        jscrollPanel.setBackground(new java.awt.Color(255, 255, 255));
        jscrollPanel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N

        practiceDocumentLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        practiceDocumentLabel.setText(bundle.getString("UserUpdateWindow.practiceDocumentLabel.text")); // NOI18N

        javax.swing.GroupLayout physicianPanelLayout = new javax.swing.GroupLayout(physicianPanel);
        physicianPanel.setLayout(physicianPanelLayout);
        physicianPanelLayout.setHorizontalGroup(
            physicianPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(physicianPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(physicianPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jscrollPanel)
                    .addGroup(physicianPanelLayout.createSequentialGroup()
                        .addGroup(physicianPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(practiceDocumentLabel)
                            .addComponent(jLabel18)
                            .addComponent(practiceDocumentTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 337, Short.MAX_VALUE))
                    .addComponent(searchSpecializationsTextField))
                .addContainerGap())
        );
        physicianPanelLayout.setVerticalGroup(
            physicianPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(physicianPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(practiceDocumentLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(practiceDocumentTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchSpecializationsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(jscrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        dadosTab.addTab(bundle.getString("UserUpdateWindow.physicianPanel.TabConstraints.tabTitle"), physicianPanel); // NOI18N

        nurseRegisterLabel.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        nurseRegisterLabel.setText(bundle.getString("UserUpdateWindow.nurseRegisterLabel.text")); // NOI18N

        try {
            nurseRegisterTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("######")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        nurseRegisterTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        nurseTypeComboBox.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        nurseTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nurseTypeComboBoxActionPerformed(evt);
            }
        });

        jLabel19.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel19.setText(bundle.getString("UserUpdateWindow.jLabel19.text")); // NOI18N

        jscrollPanelNurse.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jLabel20.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel20.setText(bundle.getString("UserUpdateWindow.jLabel20.text")); // NOI18N

        searchSpecializationsNurseTextField.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        searchSpecializationsNurseTextField.setText(bundle.getString("UserUpdateWindow.searchSpecializationsNurseTextField.text")); // NOI18N
        searchSpecializationsNurseTextField.setToolTipText(bundle.getString("UserUpdateWindow.searchSpecializationsNurseTextField.toolTipText")); // NOI18N
        searchSpecializationsNurseTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchSpecializationsNurseTextFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchSpecializationsNurseTextFieldKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchSpecializationsNurseTextFieldKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout nursePanelLayout = new javax.swing.GroupLayout(nursePanel);
        nursePanel.setLayout(nursePanelLayout);
        nursePanelLayout.setHorizontalGroup(
            nursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nursePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(nursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(nursePanelLayout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, nursePanelLayout.createSequentialGroup()
                        .addGroup(nursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nurseRegisterLabel)
                            .addComponent(nurseRegisterTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 274, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 84, Short.MAX_VALUE)
                        .addGroup(nursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(nurseTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, nursePanelLayout.createSequentialGroup()
                        .addGroup(nursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jscrollPanelNurse, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(searchSpecializationsNurseTextField))
                        .addContainerGap())))
        );
        nursePanelLayout.setVerticalGroup(
            nursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(nursePanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(nursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nurseRegisterLabel)
                    .addComponent(jLabel19))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(nursePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(nurseTypeComboBox, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(nurseRegisterTextField))
                .addGap(18, 18, 18)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(searchSpecializationsNurseTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jscrollPanelNurse, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
                .addContainerGap())
        );

        dadosTab.addTab(bundle.getString("UserUpdateWindow.nursePanel.TabConstraints.tabTitle"), nursePanel); // NOI18N

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        updateUserButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/success.png"))); // NOI18N
        updateUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateUserButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(updateUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(updateUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dadosTab)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(dadosTab, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void countryComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_countryComboBoxActionPerformed

        String code = ((CountryModel) countryComboBox.getSelectedItem()).getCode();
        identityDocumentTextField.setText("");
        postalCodeTextField.setText("");
        telephoneTextField.setText("");
        practiceDocumentTextField.setText("");
        nurseRegisterTextField.setText("");

        identityDocumentTextField.setFocusLostBehavior(JFormattedTextField.PERSIST);
        postalCodeTextField.setFocusLostBehavior(JFormattedTextField.PERSIST);
        telephoneTextField.setFocusLostBehavior(JFormattedTextField.PERSIST);
        practiceDocumentTextField.setFocusLostBehavior(JFormattedTextField.PERSIST);
        nurseRegisterTextField.setFocusLostBehavior(JFormattedTextField.PERSIST);

        if (code.equals("BRA")) {
            try {
                practiceDocumentTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#######")));
                postalCodeTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
                identityDocumentTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
                telephoneTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) #####-####")));
                nurseRegisterTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("######")));

            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
        } else if (code.equals("FS")) {
            try {

                practiceDocumentTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#######")));
                postalCodeTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####")));
                identityDocumentTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#############")));
                telephoneTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("## ### ####")));
                nurseRegisterTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("########")));

            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
        } else {
            postalCodeTextField.setText("");
            identityDocumentTextField.setText("");
            telephoneTextField.setText("");
            practiceDocumentTextField.setText("");
            nurseRegisterTextField.setText("");

            practiceDocumentTextField.setFormatterFactory(null);
            postalCodeTextField.setFormatterFactory(null);
            identityDocumentTextField.setFormatterFactory(null);
            telephoneTextField.setFormatterFactory(null);
            nurseRegisterTextField.setFormatterFactory(null);

        }
        if (userTypeComboBox.getSelectedItem() != null && userTypeComboBox.getSelectedItem().toString().charAt(0) == '2') {

            listSpecialization = physicianDAO.listSpecialization(((CountryModel) countryComboBox.getSelectedItem()).getCode());

            updatePhysician();

            practiceDocumentTextField.setText("");

        }

        if (userTypeComboBox.getSelectedItem() != null && userTypeComboBox.getSelectedItem().toString().charAt(0) == '4') {

            listSpecialization = nurseDAO.listSpecialization(((CountryModel) countryComboBox.getSelectedItem()).getCode());

            updateNurse();

            nurseRegisterTextField.setText("");
            nurseTypeComboBox.setSelectedIndex(0);

        }
    }//GEN-LAST:event_countryComboBoxActionPerformed

    private void postalCodeTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_postalCodeTextFieldKeyTyped

    }//GEN-LAST:event_postalCodeTextFieldKeyTyped

    private void postalCodeTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_postalCodeTextFieldKeyReleased
        String code = ((CountryModel) countryComboBox.getSelectedItem()).getCode();
        if (code.equals("BRA")) {

            String val = postalCodeTextField.getText().trim();

            System.out.println(val);
            if (val.length() == 9) {
                HealthInstitutionDAO.findAddress(val, stateTextField, streetTextField, neighborhoodTextField, cityTextField);
            }

        }
     }//GEN-LAST:event_postalCodeTextFieldKeyReleased

    private void identityDocumentTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_identityDocumentTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_identityDocumentTextFieldActionPerformed

    private void updateUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateUserButtonActionPerformed

        boolean success = true;

        if (success) {
            String login = loginTextField.getText();

            if (login.length() > 100) {
                JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.login"));
                success = false;
            }
        }

        if (success) {
            String login = loginTextField.getText();

            if (login.trim().equals("")) {
                JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.login.notnull"));
                success = false;
            }
        }

        if (success) {
            String login = loginTextField.getText();

            if (!EmailManager.isValidEmailAddress(login)) {
                JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.login.invalid"));
                success = false;
            }
        }

        if (success) {

            if (!passwordPasswordField.getText().equals(confirmPasswordPasswordField.getText()) && !passwordPasswordField.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.password.compare"));
                success = false;
            }
        }

        if (success) {
            if (nameTextField.getText().trim().equals("")) {

                JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.name"));
                success = false;
            }

        }
        if (userModel.getIdentityDocument().length() != 36) {
            if (success) {
                String docIdentity = identityDocumentTextField.getText().replaceAll("\\D+", "");

                if (docIdentity.length() > 20) {
                    JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.identityDocument"));
                    success = false;
                }
            }

            if (success) {

                String docIdentity = identityDocumentTextField.getText().replaceAll("\\D+", "");

                String code = ((CountryModel) countryComboBox.getSelectedItem()).getCode();
                if (code.equals("BRA")) {

                    if (!DocumentValidator.isCPF(docIdentity)) {
                        JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.identityDocument.invalid"));
                        success = false;
                    }
                }

                if (code.equals("FS")) {
                    if (docIdentity.trim().length() != 13) {
                        JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.identityDocument.invalid.fs"));
                        success = false;
                    }
                }

            }

            if (success) {
                String postalCode = postalCodeTextField.getText().replaceAll("\\D+", "");

                if (postalCode.length() > 8) {
                    JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.postalCode"));
                    success = false;
                }
            }

            if (success) {
                String state = stateTextField.getText();

                if (state.length() > 100) {
                    JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.state"));
                    success = false;
                }
            }

            if (success) {
                String state = stateTextField.getText();

                if (state.trim().equals("")) {
                    JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.state.notnull"));
                    success = false;
                }
            }

            if (success) {
                String city = cityTextField.getText();

                if (city.length() > 100) {
                    JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.city"));
                    success = false;
                }
            }

            if (success) {
                String city = cityTextField.getText();

                if (city.trim().equals("")) {
                    JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.city.notnull"));
                    success = false;
                }
            }

            if (success) {
                String neighborhood = neighborhoodTextField.getText();

                if (neighborhood.length() > 50) {
                    JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.neighborhood"));
                    success = false;
                }
            }

            if (success) {
                String neighborhood = neighborhoodTextField.getText();

                if (neighborhood.trim().equals("")) {
                    JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.neighborhood.notnull"));
                    success = false;
                }
            }

            if (success) {
                String street = streetTextField.getText();

                if (street.length() > 100) {
                    JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.street"));
                    success = false;
                }
            }

            if (success) {
                String street = streetTextField.getText();

                if (street.trim().equals("")) {
                    JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.street.notnull"));
                    success = false;
                }
            }

            if (success) {
                String number = numberTextField.getText();

                if (number.length() > 10) {
                    JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.number"));
                    success = false;
                }
            }

            if (success) {
                String number = numberTextField.getText();

                if (number.trim().equals("")) {
                    JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.number.notnull"));
                    success = false;
                }
            }

            if (success) {
                String telephone = telephoneTextField.getText();

                if (telephone.length() > 20) {
                    JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.telephone"));
                    success = false;
                }
            }

            if (success) {
                Date date = bornDateDatePicker.getDate();

                if (date == null) {
                    JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.validate.borndate"));
                    success = false;
                }
            }

        }

        if (userTypeComboBox.getSelectedItem().toString().charAt(0) == '2') {
            if (success) {

                if (practiceDocumentTextField.getText().trim().equals("")) {
                    dadosTab.setSelectedIndex(1);

                    JOptionPane.showMessageDialog(null, practiceDocumentLabel.getText() + " " + i18n.getString("UserAddWindow.validate.practiceDocument.notnull"));
                    success = false;
                }

            }

         
        }

        if (userTypeComboBox.getSelectedItem().toString().charAt(0) == '4') {
            if (success) {

                if (nurseRegisterTextField.getText().trim().equals("")) {
                    dadosTab.setSelectedIndex(1);

                    JOptionPane.showMessageDialog(null, nurseRegisterLabel.getText() + " " + i18n.getString("UserAddWindow.validate.nurseRegister.notnull"));
                    success = false;
                }

            }

        }

        if (success) {
            String photo = userModel.getPhoto();
            if (uploadImage == null) {
                photo = "USER_DEFAULT_PHOTO.jpg";
            }

            if (photo.equals("USER_DEFAULT_PHOTO.jpg") && userChangedPhoto) {
                photo = UUID.randomUUID() + ".jpg";
            }

            String date = "";

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            date = sdf.format(bornDateDatePicker.getDate());

            boolean ok = userDAO.update(
                    loginTextField.getText(),
                    passwordPasswordField.getText(),
                    nameTextField.getText(),
                    userModel.getIdentityDocument().length() != 36 ? identityDocumentTextField.getText().replaceAll("\\D+", "") : userModel.getIdentityDocument(),
                    ((CountryModel) countryComboBox.getSelectedItem()).getCode(),
                    postalCodeTextField.getText().replaceAll("\\D+", ""),
                    stateTextField.getText(),
                    cityTextField.getText(),
                    neighborhoodTextField.getText(),
                    streetTextField.getText(),
                    numberTextField.getText(),
                    photo,
                    telephoneTextField.getText().replaceAll("\\D+", ""),
                    String.valueOf(userTypeComboBox.getSelectedItem().toString().charAt(0)),
                    String.valueOf(genderComboBox.getSelectedItem().toString().charAt(0)),
                    date,
                    userModel.getId(),
                    !userModel.getLogin().equals(loginTextField.getText()),
                    !userModel.getPassword().equals(Criptography.sha256(passwordPasswordField.getText())),
                    !userModel.getIdentityDocument().equals(identityDocumentTextField.getText().replaceAll("\\D+", ""))
                    || !userModel.getCountry().equals(((CountryModel) countryComboBox.getSelectedItem()).getCode())
            );

            if (userTypeComboBox.getSelectedItem().toString().charAt(0) == '2') {
                if (ok) {
                    try {
                        practiceDocumentTextField.commitEdit();
                    } catch (ParseException ex) {
                    }

                    ok = physicianDAO.update(userModel.getId(), physicianModel.getIdPhysician(), practiceDocumentTextField.getValue().toString().trim());
                }

                if (ok) {
                    if (!userModel.getCountry().equals(((CountryModel) countryComboBox.getSelectedItem()).getCode())) {
                        List<SpecializationModel> listPhysicianReset = physicianDAO.listPhysicianSpecialization(physicianModel.getIdPhysician());
                        for (SpecializationModel specializationModel : listPhysicianReset) {
                            boolean status = physicianDAO.unbindSpecialization(physicianModel.getIdPhysician(), specializationModel.getIdSpecialization());
                            if (!status) {
                                ok = false;
                                break;
                            }
                        }
                    }
                }

                if (ok) {

                    for (SpecializationInterfaceModel specializationInterfaceModel : specializationList) {

                        if (specializationInterfaceModel.getCheckbox().isSelected() && !specializationInterfaceModel.isActivated()) {
                            boolean status = physicianDAO.bindSpecialization(physicianModel.getIdPhysician(), specializationInterfaceModel.getId());
                            if (!status) {
                                ok = false;
                                break;
                            }
                        }

                        if (!specializationInterfaceModel.getCheckbox().isSelected() && specializationInterfaceModel.isActivated()) {
                            boolean status = physicianDAO.unbindSpecialization(physicianModel.getIdPhysician(), specializationInterfaceModel.getId());
                            if (!status) {
                                ok = false;
                                break;
                            }
                        }

                    }

                }

            }

            if (userTypeComboBox.getSelectedItem().toString().charAt(0) == '4') {
                if (ok) {
                    try {
                        nurseRegisterTextField.commitEdit();
                    } catch (ParseException ex) {
                    }

                    ok = nurseDAO.update(
                            userModel.getId(),
                            nurseModel.getIdNurse(),
                            nurseRegisterTextField.getValue().toString().trim(),
                            ((NurseTypeModel) nurseTypeComboBox.getSelectedItem()).getCode());
                }

                if (ok) {
                    if (!userModel.getCountry().equals(((CountryModel) countryComboBox.getSelectedItem()).getCode())) {
                        List<SpecializationModel> listNurseReset = nurseDAO.listNurseSpecialization(nurseModel.getIdNurse());
                        for (SpecializationModel specializationModel : listNurseReset) {
                            boolean status = nurseDAO.unbindSpecialization(nurseModel.getIdNurse(), specializationModel.getIdSpecialization());
                            if (!status) {
                                ok = false;
                                break;
                            }
                        }
                    }
                }

                if (ok) {

                    for (SpecializationInterfaceModel specializationInterfaceModel : specializationList) {

                        if (specializationInterfaceModel.getCheckbox().isSelected() && !specializationInterfaceModel.isActivated()) {
                            boolean status = nurseDAO.bindSpecialization(nurseModel.getIdNurse(), specializationInterfaceModel.getId());
                            if (!status) {
                                ok = false;
                                break;
                            }
                        }

                        if (!specializationInterfaceModel.getCheckbox().isSelected() && specializationInterfaceModel.isActivated()) {
                            boolean status = nurseDAO.unbindSpecialization(nurseModel.getIdNurse(), specializationInterfaceModel.getId());
                            if (!status) {
                                ok = false;
                                break;
                            }
                        }

                    }

                }

            }

            if (ok) {

                if (uploadImage != null) {
                    AzureBlob.upload(uploadImage, photo, "userhealth");

                }

                setVisible(false);
                JOptionPane.showMessageDialog(null, i18n.getString("UserUpdateWindow.success"));
                if (userPanel != null) {
                    userPanel.populateData();
                }
                dispose();

            }

        }

    }//GEN-LAST:event_updateUserButtonActionPerformed

    private HealthInstitutionDAO healthInstitutionDAO = new HealthInstitutionDAO();
    private UserDAO userDAO = new UserDAO();
    private boolean userChangedPhoto = false;

    private void selectImageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectImageButtonActionPerformed

        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
                BufferedImage read = ImageIO.read(file);
                uploadImage = ResizeUtil.resize(read, 150, 150);
                ImageIcon icon = new ImageIcon(uploadImage);

                userHealthPhoto.setIcon(icon);
                userChangedPhoto = true;
            } catch (IOException ex) {
                Logger.getLogger(UserUpdateWindow.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_selectImageButtonActionPerformed

    private void userTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userTypeComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userTypeComboBoxActionPerformed

    private void genderComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genderComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_genderComboBoxActionPerformed

    private void bornDateDatePickerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bornDateDatePickerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bornDateDatePickerActionPerformed

    private void nurseTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nurseTypeComboBoxActionPerformed
        if (((NurseTypeModel) nurseTypeComboBox.getSelectedItem()).getCode().equals("1")
                || ((NurseTypeModel) nurseTypeComboBox.getSelectedItem()).getCode().equals("6")) {
            for (SpecializationInterfaceModel specializationInterfaceModel : specializationList) {

                if (isOwnerAccount) {
                    specializationInterfaceModel.getComboBox().setEnabled(true);
                }
            }

        } else {
            for (SpecializationInterfaceModel specializationInterfaceModel : specializationList) {

                specializationInterfaceModel.getComboBox().setSelected(false);
                specializationInterfaceModel.getComboBox().setEnabled(false);

            }

        }
    }//GEN-LAST:event_nurseTypeComboBoxActionPerformed

    private void nameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameTextFieldActionPerformed

    private void searchSpecializationsTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchSpecializationsTextFieldKeyPressed

    }//GEN-LAST:event_searchSpecializationsTextFieldKeyPressed

    private void searchSpecializationsTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchSpecializationsTextFieldKeyReleased

        specializationPhysicianPanel.removeAll();
        String physicianSpecialization = searchSpecializationsTextField.getText();
        for (SpecializationInterfaceModel specializationInterfaceModel : specializationList) {
            if (specializationInterfaceModel.getCheckbox().getText().toLowerCase().contains(physicianSpecialization.toLowerCase())) {
                specializationPhysicianPanel.add(specializationInterfaceModel.getCheckbox());
            }
        }

        specializationPhysicianPanel.repaint();
        specializationPhysicianPanel.revalidate();
    }//GEN-LAST:event_searchSpecializationsTextFieldKeyReleased

    private void searchSpecializationsTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchSpecializationsTextFieldKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_searchSpecializationsTextFieldKeyTyped

    private void searchSpecializationsNurseTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchSpecializationsNurseTextFieldKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchSpecializationsNurseTextFieldKeyPressed

    private void searchSpecializationsNurseTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchSpecializationsNurseTextFieldKeyReleased
        
        specializationNursePanel.removeAll();
        String nurseSpecialization = searchSpecializationsNurseTextField.getText();
        for (SpecializationInterfaceModel specializationInterfaceModel : specializationList) {
            if (specializationInterfaceModel.getCheckbox().getText().toLowerCase().contains(nurseSpecialization.toLowerCase())) {
                specializationNursePanel.add(specializationInterfaceModel.getCheckbox());
            }
        }

        specializationNursePanel.repaint();
        specializationNursePanel.revalidate();
        
    }//GEN-LAST:event_searchSpecializationsNurseTextFieldKeyReleased

    private void searchSpecializationsNurseTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchSpecializationsNurseTextFieldKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_searchSpecializationsNurseTextFieldKeyTyped

    public void setUserPanel(UserPanel userPanel) {
        this.userPanel = userPanel;
    }

    private BufferedImage uploadImage;

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserUpdateWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserUpdateWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserUpdateWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserUpdateWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UserUpdateWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXDatePicker bornDateDatePicker;
    private javax.swing.JTextField cityTextField;
    private javax.swing.JPasswordField confirmPasswordPasswordField;
    private javax.swing.JComboBox<String> countryComboBox;
    private javax.swing.JTabbedPane dadosTab;
    private javax.swing.JComboBox<String> genderComboBox;
    private javax.swing.JFormattedTextField identityDocumentTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jscrollPanel;
    private javax.swing.JScrollPane jscrollPanelNurse;
    private javax.swing.JTextField loginTextField;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JTextField neighborhoodTextField;
    private javax.swing.JTextField numberTextField;
    private javax.swing.JPanel nursePanel;
    private javax.swing.JLabel nurseRegisterLabel;
    private javax.swing.JFormattedTextField nurseRegisterTextField;
    private javax.swing.JComboBox<String> nurseTypeComboBox;
    private javax.swing.JPasswordField passwordPasswordField;
    private javax.swing.JPanel physicianPanel;
    private javax.swing.JFormattedTextField postalCodeTextField;
    private javax.swing.JLabel practiceDocumentLabel;
    private javax.swing.JFormattedTextField practiceDocumentTextField;
    private javax.swing.JTextField searchSpecializationsNurseTextField;
    private javax.swing.JTextField searchSpecializationsTextField;
    private javax.swing.JButton selectImageButton;
    private javax.swing.JTextField stateTextField;
    private javax.swing.JTextField streetTextField;
    private javax.swing.JFormattedTextField telephoneTextField;
    private javax.swing.JButton updateUserButton;
    private javax.swing.JLabel userHealthPhoto;
    private javax.swing.JComboBox<String> userTypeComboBox;
    // End of variables declaration//GEN-END:variables
    private JPanel specializationPhysicianPanel = new JPanel(new GridLayout(0, 2));
    private JPanel specializationNursePanel = new JPanel(new GridLayout(0, 2));

}
