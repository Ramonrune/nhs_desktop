/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */

package com.healthsystem.user.specialization;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

/**
 * @author Ramon Lacava Gutierrez Gonçales
 * @version 1.0.0
 * @date 14/07/2018 15:05:50
 */
public class SpecializationInterfaceModel {

    private JCheckBox checkbox;
    private String name;
    private String id;
    private boolean activated;

    public JCheckBox getComboBox() {
        return checkbox;
    }

    public void setComboBox(JCheckBox checkbox) {
        this.checkbox = checkbox;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JCheckBox getCheckbox() {
        return checkbox;
    }

    public void setCheckbox(JCheckBox checkbox) {
        this.checkbox = checkbox;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
    
    
    
    
}
