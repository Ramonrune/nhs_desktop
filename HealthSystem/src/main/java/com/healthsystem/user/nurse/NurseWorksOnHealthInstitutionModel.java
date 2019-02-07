package com.healthsystem.user.nurse;

import com.healthsystem.util.dataprovider.NurseTypeList;
import com.healthsystem.util.dataprovider.NurseTypeModel;
import java.util.List;

public class NurseWorksOnHealthInstitutionModel {

    private String idUser;
    private String idNurse;
    private String name;
    private String city;
    private String country;
    private String state;
    private String nurseCode;
    private String photo;
    private String nurseType;

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdNurse() {
        return idNurse;
    }

    public void setIdNurse(String idNurse) {
        this.idNurse = idNurse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNurseCode() {
        return nurseCode;
    }

    public void setNurseCode(String nurseCode) {
        this.nurseCode = nurseCode;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNurseType() {
        return nurseType;
    }

    public void setNurseType(String ntype) {
             if (country == null) {
            throw new IllegalArgumentException("Set country first");
        }
        List<NurseTypeModel> nurseTypes = NurseTypeList.getNurseTypes(country);

        for (NurseTypeModel nurseType : nurseTypes) {
            
            System.out.println(nurseType.getCode().equals(ntype) +" ----- ");
            if (nurseType.getCode().equals(ntype)) {
                this.nurseType = nurseType.getName();
            }
        }
    }

}
