/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.util;

import com.healthsystem.patient.DiseaseAddWindow;
import com.healthsystem.patient.DiseaseModel;
import java.util.Comparator;

public class LexicographicComparator implements Comparator<DiseaseModel> {

    @Override
    public int compare(DiseaseModel a, DiseaseModel b) {
        return a.getName().compareToIgnoreCase(b.getName());
    }
    
    
}
