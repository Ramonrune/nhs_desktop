/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.util;

import java.util.Locale;

/**
 * @author Ramon Lacava Gutierrez Gonçales
 * @version 1.0.0
 * @date 17/08/2018 21:59:14
 */
public class LanguageUtil {

    private LanguageUtil() {

    }

    public static String language() {
        Locale locale = Locale.getDefault();
      
        String lang = "PT";
        if (locale.getCountry().equals("BR")) {
            lang = "PT";
        }
        if (locale.getCountry().equals("ZA")) {
            lang = "EN";
        }

        return lang;
    }
}
