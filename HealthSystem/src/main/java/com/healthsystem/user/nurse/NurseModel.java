package com.healthsystem.user.nurse;

public class NurseModel {

    private String idNurse;
    private String idUser;
    private String nurseCode;
    private String nurseType;

    public String getIdNurse() {
        return idNurse;
    }

    public void setIdNurse(String idNurse) {
        this.idNurse = idNurse;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNurseCode() {
        return nurseCode;
    }

    public void setNurseCode(String nurseCode) {
        this.nurseCode = nurseCode;
    }

    public String getNurseType() {
        return nurseType;
    }

    public void setNurseType(String nurseType) {
        this.nurseType = nurseType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((idNurse == null) ? 0 : idNurse.hashCode());
        result = prime * result + ((idUser == null) ? 0 : idUser.hashCode());
        return result;
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
        NurseModel other = (NurseModel) obj;
        if (idNurse == null) {
            if (other.idNurse != null) {
                return false;
            }
        } else if (!idNurse.equals(other.idNurse)) {
            return false;
        }
        if (idUser == null) {
            if (other.idUser != null) {
                return false;
            }
        } else if (!idUser.equals(other.idUser)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "NurseModel [idNurse=" + idNurse + ", idUser=" + idUser + ", nurseCode=" + nurseCode + ", nurseType="
                + nurseType + "]";
    }

}
