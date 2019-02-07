/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.healthinstitution;

import com.healthsystem.user.UserDAO;
import com.healthsystem.user.UserPanel;
import com.healthsystem.util.WindowManager;
import com.healthsystem.util.dataprovider.CountryList;
import com.healthsystem.util.dataprovider.CountryModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HealthInstitutionPanel extends javax.swing.JPanel {

    private HealthInstitutionDAO healthInstitutionDAO = new HealthInstitutionDAO();
    private java.util.ResourceBundle i18n = java.util.ResourceBundle.getBundle("com/healthsystem/healthinstitution/Bundle"); // NOI18N

    private int start = 1;
    private int end = 20;
    private int count = 1;

    public HealthInstitutionPanel() {
        initComponents();
        countryComboBox.setModel(new DefaultComboBoxModel(CountryList.countryList.toArray()));

        disableIcons();

        populateData(1, 20);

        healthInstitutionsTable.removeColumn(healthInstitutionsTable.getColumnModel().getColumn(0));
        healthInstitutionsTable.setRowHeight(40);

        healthInstitutionsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {

                deleteInstituitionButton.setEnabled(true);
                editHealthInstitutionButton.setEnabled(true);
                addUserButton.setEnabled(true);
            }
        });

    }

    private void disableIcons() {
        deleteInstituitionButton.setEnabled(false);
        editHealthInstitutionButton.setEnabled(false);
        addUserButton.setEnabled(false);
    }

    protected void resetCount() {
        this.start = 1;
        this.end = 20;
        count = 1;
        pageCountTextField.setText("1");
    }

    protected HealthInstitutionDAO getHealthInstitutionDAO() {
        return this.healthInstitutionDAO;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        searchTF = new javax.swing.JTextField();
        countryComboBox = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        healthInstitutionsTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        addInstituitionButton = new javax.swing.JButton();
        deleteInstituitionButton = new javax.swing.JButton();
        previousPageButton = new javax.swing.JButton();
        nextPageButton = new javax.swing.JButton();
        pageCountTextField = new javax.swing.JTextField();
        editHealthInstitutionButton = new javax.swing.JButton();
        addUserButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setMinimumSize(new java.awt.Dimension(721, 506));
        setPreferredSize(new java.awt.Dimension(721, 506));

        searchTF.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/healthsystem/healthinstitution/Bundle"); // NOI18N
        searchTF.setToolTipText(bundle.getString("HealthInstitutionPanel.searchTF.toolTipText")); // NOI18N
        searchTF.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        searchTF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchTFActionPerformed(evt);
            }
        });
        searchTF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                searchTFKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchTFKeyTyped(evt);
            }
        });

        countryComboBox.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        countryComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countryComboBoxActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/search.png"))); // NOI18N
        jButton1.setMaximumSize(new java.awt.Dimension(40, 40));
        jButton1.setMinimumSize(new java.awt.Dimension(40, 40));
        jButton1.setPreferredSize(new java.awt.Dimension(40, 40));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        healthInstitutionsTable.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        healthInstitutionsTable.setModel(new javax.swing.table.DefaultTableModel(

            new Object [][] {

            },
            new String [] {
                bundle.getString("HealthInstitutionPanel.table.ID"),
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
        healthInstitutionsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                healthInstitutionsTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(healthInstitutionsTable);

        jLabel1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jLabel1.setText(bundle.getString("HealthInstitutionPanel.jLabel1.text")); // NOI18N

        addInstituitionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/add.png"))); // NOI18N
        addInstituitionButton.setMinimumSize(new java.awt.Dimension(56, 20));
        addInstituitionButton.setPreferredSize(new java.awt.Dimension(57, 33));
        addInstituitionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addInstituitionButtonActionPerformed(evt);
            }
        });

        deleteInstituitionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/delete.png"))); // NOI18N
        deleteInstituitionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteInstituitionButtonActionPerformed(evt);
            }
        });

        previousPageButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/rewind.png"))); // NOI18N
        previousPageButton.setMaximumSize(new java.awt.Dimension(40, 40));
        previousPageButton.setMinimumSize(new java.awt.Dimension(40, 40));
        previousPageButton.setPreferredSize(new java.awt.Dimension(40, 40));
        previousPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previousPageButtonActionPerformed(evt);
            }
        });

        nextPageButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/fast-forward.png"))); // NOI18N
        nextPageButton.setMaximumSize(new java.awt.Dimension(40, 40));
        nextPageButton.setMinimumSize(new java.awt.Dimension(40, 40));
        nextPageButton.setPreferredSize(new java.awt.Dimension(40, 40));
        nextPageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextPageButtonActionPerformed(evt);
            }
        });

        pageCountTextField.setText(bundle.getString("HealthInstitutionPanel.pageCountTextField.text")); // NOI18N
        pageCountTextField.setMaximumSize(new java.awt.Dimension(40, 40));
        pageCountTextField.setMinimumSize(new java.awt.Dimension(40, 40));
        pageCountTextField.setPreferredSize(new java.awt.Dimension(40, 40));
        pageCountTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pageCountTextFieldActionPerformed(evt);
            }
        });

        editHealthInstitutionButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/edit.png"))); // NOI18N
        editHealthInstitutionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editHealthInstitutionButtonActionPerformed(evt);
            }
        });

        addUserButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/user_menu_item_1.png"))); // NOI18N
        addUserButton.setMaximumSize(new java.awt.Dimension(40, 40));
        addUserButton.setMinimumSize(new java.awt.Dimension(40, 40));
        addUserButton.setPreferredSize(new java.awt.Dimension(40, 40));
        addUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addUserButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 0, 18)); // NOI18N
        jLabel2.setText(bundle.getString("HealthInstitutionPanel.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(searchTF, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(addInstituitionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(editHealthInstitutionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(deleteInstituitionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(addUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(countryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(previousPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pageCountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(nextPageButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(editHealthInstitutionButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nextPageButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(pageCountTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(previousPageButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(deleteInstituitionButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addInstituitionButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(countryComboBox)
                    .addComponent(searchTF)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(addUserButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
                .addGap(17, 17, 17))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void countryComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_countryComboBoxActionPerformed
        resetCount();
        populateData(1, 20);

    }//GEN-LAST:event_countryComboBoxActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        resetCount();
        populateData(1, 20);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void searchTFKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTFKeyPressed


    }//GEN-LAST:event_searchTFKeyPressed

    private void searchTFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchTFActionPerformed
        populateData(1, 20);
        resetCount();

    }//GEN-LAST:event_searchTFActionPerformed

    private void searchTFKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTFKeyTyped


    }//GEN-LAST:event_searchTFKeyTyped

    private void addInstituitionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addInstituitionButtonActionPerformed

        HealthInstitutionAddWindow healthInstitutionAddWindow = new HealthInstitutionAddWindow();
        healthInstitutionAddWindow.setHealthInstitutionPanel(this);
        healthInstitutionAddWindow.setVisible(true);
    }//GEN-LAST:event_addInstituitionButtonActionPerformed

    private void deleteInstituitionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteInstituitionButtonActionPerformed
        DefaultTableModel model = (DefaultTableModel) healthInstitutionsTable.getModel();
        if (healthInstitutionsTable.getRowCount() > 0) {
            if (healthInstitutionsTable.getSelectedRowCount() > 0) {
                boolean success = true;

                int showConfirmDialog = JOptionPane.showConfirmDialog(null, i18n.getString("HealthInstitutionPanel.excludeMessageConfirm"));
                if (showConfirmDialog == JOptionPane.YES_OPTION) {
                    int selectedRow[] = healthInstitutionsTable.getSelectedRows();
                    for (int i : selectedRow) {
                        String id = healthInstitutionsTable.getModel().getValueAt(i, 0).toString();
                        boolean ok = healthInstitutionDAO.delete(id);
                        if (!ok) {
                            success = false;
                        } else {
                            model.removeRow(i);

                        }
                    }

                    if (success) {
                        JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionPanel.excludeMessage"));
                    }
                }

            }
        }
    }//GEN-LAST:event_deleteInstituitionButtonActionPerformed

    private void nextPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextPageButtonActionPerformed

        if (healthInstitutionsTable.getRowCount() != 0) {
            count++;
            pageCountTextField.setText(String.valueOf(count));
            start = end + 1;
            end = end + 20;
            populateData(start, end);
            System.out.println(start + "----" + end);

        }

    }//GEN-LAST:event_nextPageButtonActionPerformed

    private void previousPageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previousPageButtonActionPerformed

        if (count != 1) {

            count--;
            pageCountTextField.setText(String.valueOf(count));
            start = start - 20;
            end = end - 20;
            populateData(start, end);

            System.out.println(start + "----" + end);
        }


    }//GEN-LAST:event_previousPageButtonActionPerformed

    private void pageCountTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pageCountTextFieldActionPerformed
        try {
            if (Integer.parseInt(pageCountTextField.getText()) < 1) {

                resetCount();
                pageCountTextField.setText("1");
                populateData(start, end);
            } else {

                count = Integer.parseInt(pageCountTextField.getText());
                end = count * 20;
                start = end - 19;
                populateData(start, end);

            }
        } catch (Exception e) {

            resetCount();
            pageCountTextField.setText("1");
            populateData(start, end);
        }

    }//GEN-LAST:event_pageCountTextFieldActionPerformed

    private void editHealthInstitutionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editHealthInstitutionButtonActionPerformed
        DefaultTableModel model = (DefaultTableModel) healthInstitutionsTable.getModel();
        if (healthInstitutionsTable.getRowCount() > 0) {
            if (healthInstitutionsTable.getSelectedRowCount() > 0) {
                boolean success = true;

                int selectedRow[] = healthInstitutionsTable.getSelectedRows();
                for (int i : selectedRow) {
                    String id = healthInstitutionsTable.getModel().getValueAt(i, 0).toString();

                    HealthInstitutionUpdateWindow healthInstitutionUpdateWindow = new HealthInstitutionUpdateWindow();
                    healthInstitutionUpdateWindow.setHealthInstitutionModel(healthInstitutionDAO.getHealthInstitution(id));
                    healthInstitutionUpdateWindow.setHealthInstitutionPanel(this);
                    healthInstitutionUpdateWindow.setVisible(true);

                }
            }
        }
    }//GEN-LAST:event_editHealthInstitutionButtonActionPerformed

    private void addUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addUserButtonActionPerformed
        DefaultTableModel model = (DefaultTableModel) healthInstitutionsTable.getModel();
        if (healthInstitutionsTable.getRowCount() > 0) {
            if (healthInstitutionsTable.getSelectedRowCount() > 0) {
                boolean success = true;

                int selectedRow[] = healthInstitutionsTable.getSelectedRows();
                for (int i : selectedRow) {
                    String id = healthInstitutionsTable.getModel().getValueAt(i, 0).toString();
                    HealthInstitutionSingleton.getInstance().setIdHealthInstitution(id);
                    UserPanel userPanel = new UserPanel();
                    WindowManager.changePanel(userPanel, this);
                }
            }
        }
    }//GEN-LAST:event_addUserButtonActionPerformed

    private void healthInstitutionsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_healthInstitutionsTableMouseClicked

    }//GEN-LAST:event_healthInstitutionsTableMouseClicked

    protected void populateData(int start, int end) {
        String search = searchTF.getText().trim().equals("") ? null : searchTF.getText();
        String country = ((CountryModel) countryComboBox.getSelectedItem()).getCode();

        for (int i = 0; i <= healthInstitutionsTable.getRowCount(); i++) {
            ((DefaultTableModel) healthInstitutionsTable.getModel()).setNumRows(0);
        }
        healthInstitutionsTable.updateUI();

        DefaultTableModel model = (DefaultTableModel) healthInstitutionsTable.getModel();
        for (HealthInstitutionModel healthInstitutionModel : healthInstitutionDAO.getHealthInstitutions(start, end, country, search)) {

            model.addRow(new Object[]{
                healthInstitutionModel.getIdHealthInstitution(),
                healthInstitutionModel.getName(),
                healthInstitutionModel.getIdentityCode(),
                healthInstitutionModel.getCountry(),
                healthInstitutionModel.getState(),
                healthInstitutionModel.getCity()
            });
        }

        disableIcons();

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addInstituitionButton;
    private javax.swing.JButton addUserButton;
    private javax.swing.JComboBox<String> countryComboBox;
    private javax.swing.JButton deleteInstituitionButton;
    private javax.swing.JButton editHealthInstitutionButton;
    private javax.swing.JTable healthInstitutionsTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton nextPageButton;
    private javax.swing.JTextField pageCountTextField;
    private javax.swing.JButton previousPageButton;
    private javax.swing.JTextField searchTF;
    // End of variables declaration//GEN-END:variables
}
