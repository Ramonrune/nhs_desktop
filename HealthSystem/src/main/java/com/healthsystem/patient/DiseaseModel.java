/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.patient;

import java.util.Objects;

public class DiseaseModel {

    private String idDisease;
    private String name;

    public String getIdDisease() {
        return idDisease;
    }

    public void setIdDisease(String idDisease) {
        this.idDisease = idDisease;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.idDisease);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DiseaseModel other = (DiseaseModel) obj;
        if (!Objects.equals(this.idDisease, other.idDisease)) {
            return false;
        }
        return true;
    }

    
    
}
