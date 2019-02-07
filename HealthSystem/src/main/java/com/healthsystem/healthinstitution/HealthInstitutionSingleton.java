/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */

package com.healthsystem.healthinstitution;

import javax.swing.ImageIcon;


/**
 * @author Ramon Lacava Gutierrez Gonçales
 * @version 1.0.0
 * @date 06/07/2018 17:15:50
 */
public class HealthInstitutionSingleton {

    private String idHealthInstitution;
    private String name;
    
    
    private static HealthInstitutionSingleton instance = null;
    private HealthInstitutionSingleton() {

    }
    public static HealthInstitutionSingleton getInstance() {
       if(instance == null) {
          instance = new HealthInstitutionSingleton();
       }
       return instance;
    }

    public String getIdHealthInstitution() {
        return idHealthInstitution;
    }

    public void setIdHealthInstitution(String idHealthInstitution) {
        this.idHealthInstitution = idHealthInstitution;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   
    
    
}
