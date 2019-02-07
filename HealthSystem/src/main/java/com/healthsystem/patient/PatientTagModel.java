package com.healthsystem.patient;

public class PatientTagModel {

    private String idPatientTag;

    private String macCode;

    private String idPatient;

    private String tagType;

    private String name;

    public String getIdPatientTag() {
        return idPatientTag;
    }

    public void setIdPatientTag(String idPatientTag) {
        this.idPatientTag = idPatientTag;
    }

    public String getMacCode() {
        return macCode;
    }

    public void setMacCode(String macCode) {
        this.macCode = macCode;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }

    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
