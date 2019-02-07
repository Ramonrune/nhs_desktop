/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.util.dataprovider;

import com.healthsystem.util.LanguageUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * @author Ramon Lacava Gutierrez Gonçales
 * @version 1.0.0
 * @date 17/07/2018 19:13:39
 */
public class NurseTypeList {

    private static final List<NurseTypeModel> list = new ArrayList<>();

    static {

        list.add(new NurseTypeModel("1", LanguageUtil.language().equals("PT") ? "Enfermeiro(a)" : "Nurse", "BRA"));
        list.add(new NurseTypeModel("2", LanguageUtil.language().equals("PT") ?  "Tecnico(a) em Enfermagem" : "Nurse technician", "BRA"));
        list.add(new NurseTypeModel("3", LanguageUtil.language().equals("PT") ?"Auxiliar em Enfermagem" : "Nurse auxiliar", "BRA"));
        list.add(new NurseTypeModel("6", LanguageUtil.language().equals("PT") ? "Enfermeiro(a) registrado(a)" : "Registered Nurse", "FS"));
        list.add(new NurseTypeModel("4", LanguageUtil.language().equals("PT") ? "Enfermeiro(a) assistente" : "Enrolled Nursing Assistant", "FS"));
        list.add(new NurseTypeModel("5",  LanguageUtil.language().equals("PT") ? "Enfermeiro(a) inscrito(a)"  :"Enrolled Nurse", "FS"));

    }
    
    
    public static List<NurseTypeModel> getNurseTypes(String country){
        return list.stream().filter(nursetype -> nursetype.getCountry().equals(country)).collect(Collectors.toList());
    }

}
