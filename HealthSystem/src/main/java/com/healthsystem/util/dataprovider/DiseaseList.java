/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.util.dataprovider;

import com.healthsystem.patient.DiseaseModel;
import com.healthsystem.patient.PatientDAO;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ramon Lacava Gutierrez Gonçales
 * @version 1.0.0
 * @date 05/08/2018 11:29:05
 */
public class DiseaseList {

    private DiseaseList(){
        diseaseList = patientDAO.getDiseases();
    }
    
    private static DiseaseList diseaseInstance;
    
    public static DiseaseList getInstance(){
        if(diseaseInstance == null){
            diseaseInstance = new DiseaseList();
        }
        
        return diseaseInstance;
    }
    
    private PatientDAO patientDAO = new PatientDAO();
    private List<DiseaseModel> diseaseList;
    
    public List<DiseaseModel> getDiseases(){
        return diseaseList;
    }
    
    public String getDiseaseName(String idDisease){
        
        List<DiseaseModel> disease = diseaseList.stream().filter(e -> e.getIdDisease().equals(idDisease)).collect(Collectors.toList());
        
        return disease.get(0).getName();
    }
    
    
    
}
