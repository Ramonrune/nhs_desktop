/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.healthinstitution;

import com.healthsystem.home.HomePanel;
import com.healthsystem.home.HomeScreen;
import com.healthsystem.user.UserDAO;
import com.healthsystem.user.UserSingleton;
import com.healthsystem.util.ResizeUtil;
import com.healthsystem.util.WEBAPI;
import com.healthsystem.util.dataprovider.CountryList;
import com.healthsystem.util.dataprovider.CountryModel;
import com.healthsystem.util.WindowManager;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HealthInstitutionSelectPanel extends javax.swing.JPanel {

    private UserDAO userDAO = new UserDAO();
    private java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/healthsystem/healthinstitution/Bundle"); // NOI18N

    private int start = 1;
    private int end = 20;
    private int count = 1;

    public HealthInstitutionSelectPanel() {
        initComponents();

        healthInstitutionsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                healthInstitutionsTable.setRowSelectionInterval(healthInstitutionsTable.getSelectedRow(), healthInstitutionsTable.getSelectedRow());

                String id = healthInstitutionsTable.getModel().getValueAt(healthInstitutionsTable.getSelectedRow(), 0).toString();

                String name = healthInstitutionsTable.getModel().getValueAt(healthInstitutionsTable.getSelectedRow(), 2).toString();

                HealthInstitutionSingleton.getInstance().setIdHealthInstitution(id);
                HealthInstitutionSingleton.getInstance().setName(name);

                homeScreen.getJMenuBar().setVisible(true);
                homeScreen.getJMenuBar().updateUI();
                WindowManager.changePanel(new HomePanel(), homeScreen.getPanel());

            }
        });

        healthInstitutionsTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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
        
healthInstitutionsTable.setFillsViewportHeight(true);



    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        healthInstitutionsTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(721, 506));
        setPreferredSize(new java.awt.Dimension(721, 506));

        jScrollPane1.setMinimumSize(null);
        jScrollPane1.setPreferredSize(null);

        healthInstitutionsTable.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        healthInstitutionsTable.setModel(new javax.swing.table.DefaultTableModel(

            new Object [][] {

            },
            new String [] {
                bundle.getString("HealthInstitutionPanel.table.ID"),
                bundle.getString("HealthInstitutionPanel.table.photo"),
                bundle.getString("HealthInstitutionPanel.table.institution"),
                bundle.getString("HealthInstitutionPanel.table.identification"),
                bundle.getString("HealthInstitutionPanel.table.country"),
                bundle.getString("HealthInstitutionPanel.table.state"),
                bundle.getString("HealthInstitutionPanel.table.city")
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        healthInstitutionsTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(healthInstitutionsTable);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/healthsystem/healthinstitution/Bundle"); // NOI18N
        jLabel1.setText(bundle.getString("HealthInstitutionSelectPanel.jLabel1.text")); // NOI18N
        jLabel1.setToolTipText(bundle.getString("HealthInstitutionSelectPanel.jLabel1.toolTipText")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 426, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable healthInstitutionsTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    public void setHealthInstitutionList(List<HealthInstitutionModel> healthInstitutions) {

        for (int i = 0; i <= healthInstitutionsTable.getRowCount(); i++) {
            ((DefaultTableModel) healthInstitutionsTable.getModel()).setNumRows(0);
        }
        healthInstitutionsTable.updateUI();

        DefaultTableModel model = (DefaultTableModel) healthInstitutionsTable.getModel();
        for (HealthInstitutionModel healthInstitutionModel : healthInstitutions) {

            try {
                URL url = new URL(WEBAPI.IMAGE + "healthinstitution/" + healthInstitutionModel.getPhoto() + "?" + System.currentTimeMillis());
                
                System.out.println(url.toString());
                BufferedImage buff = ImageIO.read(url);
                BufferedImage resize = ResizeUtil.resize(buff, 40, 40);
                resize = ResizeUtil.circle(resize);

                model.addRow(new Object[]{
                    healthInstitutionModel.getIdHealthInstitution(),
                    new ImageIcon(resize),
                    healthInstitutionModel.getName(),
                    healthInstitutionModel.getIdentityCode(),
                    healthInstitutionModel.getCountry(),
                    healthInstitutionModel.getState(),
                    healthInstitutionModel.getCity()
                });

            } catch (Exception e) {
                e.printStackTrace();

            }
        }

        healthInstitutionsTable.removeColumn(healthInstitutionsTable.getColumnModel().getColumn(0));
        healthInstitutionsTable.setRowHeight(40);

    }

    private HomeScreen homeScreen;

    public void setHomeScreen(HomeScreen homeScreen, boolean visibility) {
        this.homeScreen = homeScreen;
        if (!visibility) {
            homeScreen.getMenuBarBase().setVisible(false);
            homeScreen.getMenuBarBase().updateUI();
        }
    }
}
