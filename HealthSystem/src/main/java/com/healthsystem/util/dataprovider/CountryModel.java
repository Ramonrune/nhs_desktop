/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */

package com.healthsystem.util.dataprovider;


public class CountryModel {

    private String code;
    private String name;
    

    public CountryModel(String code, String name) {
        this.code = code;
        this.name = name;
    }
    

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
    
    @Override
    public String toString(){
        return name;
    }
    
    
    
}
