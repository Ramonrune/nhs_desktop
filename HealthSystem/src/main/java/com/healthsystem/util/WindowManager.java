package com.healthsystem.util;

import com.healthsystem.physician.NFCReader;
import javax.swing.JPanel;

public class WindowManager {

    private WindowManager() {

    }

    public static void changePanel(JPanel auxPanel, JPanel remove, boolean... close) {
        if (close.length == 0) {
            NFCReader.close();
        }

        remove.removeAll();
        javax.swing.GroupLayout painelPrincipalLayout = new javax.swing.GroupLayout(remove);
        remove.setLayout(painelPrincipalLayout);
        painelPrincipalLayout.setHorizontalGroup(
                painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(auxPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        painelPrincipalLayout.setVerticalGroup(
                painelPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(auxPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

    }
}
