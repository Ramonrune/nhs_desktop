/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.home;

import com.healthsystem.audittrail.AuditTrailPanel;
import com.healthsystem.errorlog.ErrorLogPanel;
import com.healthsystem.healthinstitution.HealthInstitutionModel;
import com.healthsystem.healthinstitution.HealthInstitutionPanel;
import com.healthsystem.healthinstitution.HealthInstitutionSelectPanel;
import com.healthsystem.healthinstitution.HealthInstitutionSingleton;
import com.healthsystem.physician.NFCReaderPanel;
import com.healthsystem.notification.NotificationPanel;
import com.healthsystem.physician.PhysicianPanel;
import com.healthsystem.user.UserDAO;
import com.healthsystem.user.UserPanel;
import com.healthsystem.user.UserSingleton;
import com.healthsystem.user.UserUpdateWindow;
import com.healthsystem.util.WindowManager;
import com.healthsystem.util.ViewConfigurable;
import java.awt.Dimension;
import java.awt.Panel;
import java.awt.Toolkit;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author Ramon Lacava Gutierrez Gonçales
 * @version 1.0.0
 * @date 30/06/2018 15:13:20
 */
public class HomeScreen extends javax.swing.JFrame implements ViewConfigurable {

    private java.util.ResourceBundle i18n = java.util.ResourceBundle.getBundle("com/healthsystem/home/Bundle"); // NOI18N

    private UserDAO userDAO = new UserDAO();

    /**
     * Cria um novo formulário HomeScreen
     */
    public HomeScreen() {
        defineScreen(this);
       // setResizable(false);
        initComponents();

        centerScreen(this);
        Dimension DimMax = Toolkit.getDefaultToolkit().getScreenSize();
        setMaximumSize(DimMax);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        if (UserSingleton.getInstance().isPhysician() || UserSingleton.getInstance().isParamedic()
                || UserSingleton.getInstance().isNurse()) {
            userManagementMenuItem.setVisible(false);
            
        }

        
        
        if (UserSingleton.getInstance().isPhysician()) {
            patientMenuItem.setVisible(true);
        } else {
            patientMenuItem.setVisible(false);

        }

        if (UserSingleton.getInstance().isAdmin()) {
            userManagementMenuItem.setVisible(false);
            HomePanel homePanel = new HomePanel();
            WindowManager.changePanel(homePanel, panel);
            activityLogMenuItem.setVisible(true);
            errorMenuItem.setVisible(true);

        } else {
            activityLogMenuItem.setVisible(false);
            errorMenuItem.setVisible(false);
            
            List<HealthInstitutionModel> healthInstitutions = userDAO.getHealthInstitutions(UserSingleton.getInstance().getUserId(), "1");

            if (healthInstitutions.size() == 0) {

                HomePanel homePanel = new HomePanel();
                WindowManager.changePanel(homePanel, panel);
                return;
            }

            if (healthInstitutions.size() > 1) {
                HealthInstitutionSelectPanel healthInstitutionSelectPanel = new HealthInstitutionSelectPanel();
                healthInstitutionSelectPanel.setHealthInstitutionList(healthInstitutions);
                healthInstitutionSelectPanel.setHomeScreen(this, false);
                WindowManager.changePanel(healthInstitutionSelectPanel, panel);

            } else {

                HealthInstitutionSingleton.getInstance().setIdHealthInstitution(healthInstitutions.get(0).getIdHealthInstitution());
                HealthInstitutionSingleton.getInstance().setName(healthInstitutions.get(0).getName());

                HomePanel homePanel = new HomePanel();
                WindowManager.changePanel(homePanel, panel);

            }

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        menuBarBase = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        healthInstitutionMenuItem = new javax.swing.JMenuItem();
        userManagementMenuItem = new javax.swing.JMenuItem();
        patientMenuItem = new javax.swing.JMenuItem();
        errorMenuItem = new javax.swing.JMenuItem();
        activityLogMenuItem = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(null);

        panel.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 721, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 506, Short.MAX_VALUE)
        );

        menuBarBase.setBackground(new java.awt.Color(255, 255, 255));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/healthsystem/home/Bundle"); // NOI18N
        jMenu1.setText(bundle.getString("HomeScreen.jMenu1.text")); // NOI18N
        jMenu1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N

        jMenuItem1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/home_icon.png"))); // NOI18N
        jMenuItem1.setText(bundle.getString("HomeScreen.jMenuItem1.text")); // NOI18N
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        healthInstitutionMenuItem.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        healthInstitutionMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/hospital.png"))); // NOI18N
        healthInstitutionMenuItem.setText(bundle.getString("HomeScreen.healthInstitutionMenuItem.text")); // NOI18N
        healthInstitutionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                healthInstitutionMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(healthInstitutionMenuItem);

        userManagementMenuItem.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        userManagementMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/users.png"))); // NOI18N
        userManagementMenuItem.setText(bundle.getString("HomeScreen.userManagementMenuItem.text")); // NOI18N
        userManagementMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userManagementMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(userManagementMenuItem);

        patientMenuItem.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        patientMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/patient_management.png"))); // NOI18N
        patientMenuItem.setText(bundle.getString("HomeScreen.patientMenuItem.text")); // NOI18N
        patientMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                patientMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(patientMenuItem);

        errorMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/error.png"))); // NOI18N
        errorMenuItem.setText(bundle.getString("HomeScreen.errorMenuItem.text")); // NOI18N
        errorMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                errorMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(errorMenuItem);

        activityLogMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/audit_trail.png"))); // NOI18N
        activityLogMenuItem.setText(bundle.getString("HomeScreen.activityLogMenuItem.text")); // NOI18N
        activityLogMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activityLogMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(activityLogMenuItem);

        jMenuItem2.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/user_menu_item_1.png"))); // NOI18N
        jMenuItem2.setText(bundle.getString("HomeScreen.jMenuItem2.text")); // NOI18N
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        menuBarBase.add(jMenu1);

        setJMenuBar(menuBarBase);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void healthInstitutionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_healthInstitutionMenuItemActionPerformed

        if (UserSingleton.getInstance().isAdmin()) {
            WindowManager.changePanel(new HealthInstitutionPanel(), panel);

        } else {
            List<HealthInstitutionModel> healthInstitutions = userDAO.getHealthInstitutions(UserSingleton.getInstance().getUserId(), "1");
            HealthInstitutionSelectPanel healthInstitutionSelectPanel = new HealthInstitutionSelectPanel();
            healthInstitutionSelectPanel.setHealthInstitutionList(healthInstitutions);
            healthInstitutionSelectPanel.setHomeScreen(this, true);
            WindowManager.changePanel(healthInstitutionSelectPanel, panel);
        }
    }//GEN-LAST:event_healthInstitutionMenuItemActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        HomePanel homePanel = new HomePanel();
        WindowManager.changePanel(homePanel, panel);

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void userManagementMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userManagementMenuItemActionPerformed
        if (HealthInstitutionSingleton.getInstance().getIdHealthInstitution() != null) {
            UserPanel userPanel = new UserPanel();
            WindowManager.changePanel(userPanel, panel);
        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HomePanel.noenterpriseselected"));
        }

    }//GEN-LAST:event_userManagementMenuItemActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        UserUpdateWindow userUpdateWindow = new UserUpdateWindow();
        userUpdateWindow.setUser(userDAO.getUser(UserSingleton.getInstance().getUserId()), true);
        userUpdateWindow.setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void patientMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_patientMenuItemActionPerformed
        if (HealthInstitutionSingleton.getInstance().getIdHealthInstitution() != null) {
            PhysicianPanel physicianPanel = new PhysicianPanel();
            WindowManager.changePanel(physicianPanel, panel);
        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HomePanel.noenterpriseselected"));

        }

    }//GEN-LAST:event_patientMenuItemActionPerformed

    private void errorMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_errorMenuItemActionPerformed
     
        ErrorLogPanel errorLogPanel = new ErrorLogPanel();
        WindowManager.changePanel(errorLogPanel, panel);
        
        
    }//GEN-LAST:event_errorMenuItemActionPerformed

    private void activityLogMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activityLogMenuItemActionPerformed
        AuditTrailPanel auditTrailPanel = new AuditTrailPanel();
        WindowManager.changePanel(auditTrailPanel, panel);
        
    }//GEN-LAST:event_activityLogMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                new HomeScreen().setVisible(true);

            }
        });
    }

    public JMenuBar getMenuBarBase() {
        return menuBarBase;
    }

    public JPanel getPanel() {
        return panel;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem activityLogMenuItem;
    private javax.swing.JMenuItem errorMenuItem;
    private javax.swing.JMenuItem healthInstitutionMenuItem;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuBar menuBarBase;
    private javax.swing.JPanel panel;
    private javax.swing.JMenuItem patientMenuItem;
    private javax.swing.JMenuItem userManagementMenuItem;
    // End of variables declaration//GEN-END:variables

}
