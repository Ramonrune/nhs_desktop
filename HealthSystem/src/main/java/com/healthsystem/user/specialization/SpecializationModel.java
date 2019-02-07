package com.healthsystem.user.specialization;

import java.util.Objects;

public class SpecializationModel {

    private String idSpecialization;
    private String name;
    private String country;

    public String getIdSpecialization() {
        return idSpecialization;
    }

    public void setIdSpecialization(String idSpecialization) {
        this.idSpecialization = idSpecialization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.idSpecialization);
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
        final SpecializationModel other = (SpecializationModel) obj;
        if (!Objects.equals(this.idSpecialization, other.idSpecialization)) {
            return false;
        }
        return true;
    }

    
    
}
