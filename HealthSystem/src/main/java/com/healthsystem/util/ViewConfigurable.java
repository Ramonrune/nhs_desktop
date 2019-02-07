/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author Usuario
 */
public interface ViewConfigurable {
    
    public default void defineScreenUndecorated(JFrame jframe){
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/logo_icon.png"));
        jframe.setIconImage(imageIcon.getImage());
        jframe.setUndecorated(true);
        jframe.setTitle("Health System");
    }
    
    public default void defineScreen(JFrame jframe){
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/logo_icon.png"));
        jframe.setIconImage(imageIcon.getImage());
        jframe.setTitle("Health System");

    }
    
    
    public default void centerScreen(JFrame jframe){
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jframe.setLocation(dim.width / 2 - jframe.getSize().width / 2, dim.height / 2 - jframe.getSize().height / 2);

    }
}
