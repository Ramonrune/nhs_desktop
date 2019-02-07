/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.patient;

public class PatientHasDiseaseModel {

    private String idPatientHasDisease;

    private String idPatient;

    private String idDisease;

    private String anotation;
    
    private String nameDisease;

    public String getIdPatientHasDisease() {
        return idPatientHasDisease;
    }

    public void setIdPatientHasDisease(String idPatientHasDisease) {
        this.idPatientHasDisease = idPatientHasDisease;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public void setIdPatient(String idPatient) {
        this.idPatient = idPatient;
    }

    public String getIdDisease() {
        return idDisease;
    }

    public void setIdDisease(String idDisease) {
        this.idDisease = idDisease;
    }

    public String getAnotation() {
        return anotation;
    }

    public void setAnotation(String anotation) {
        this.anotation = anotation;
    }

    public String getNameDisease() {
        return nameDisease;
    }

    public void setNameDisease(String nameDisease) {
        this.nameDisease = nameDisease;
    }

    
}
