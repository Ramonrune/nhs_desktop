/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.util.dataprovider;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Ramon Lacava Gutierrez Gonçales
 * @version 1.0.0
 * @date 30/06/2018 17:13:14
 */
public class CountryList {
    private static java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/healthsystem/util/dataprovider/Bundle"); // NOI18N

    public static List<CountryModel> countryList = new ArrayList<>();

    public static List<CountryModel> listOnlyCountries(){
        List<CountryModel> arrayList = new ArrayList<CountryModel>(countryList);
        arrayList.remove(0);
        return arrayList;
    }
    
    static {
        countryList.add(new CountryModel(null, bundle.getString("all")));
        countryList.add(new CountryModel("FS", bundle.getString("southafrica")));
        countryList.add(new CountryModel("BRA", bundle.getString("brazil")));
       
    }
    
    public static int index(){
      
        Locale locale = Locale.getDefault();
        
        if(locale.getCountry().equals("BR")){
            return 1;
        }
        
        if(locale.getCountry().equals("ZA")){
            return 0;
        }
        
        return 1;
    }

    private CountryList() {

    }
}
