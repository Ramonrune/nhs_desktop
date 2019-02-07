/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.util;

import java.text.ParseException;
import javax.swing.text.MaskFormatter;

/**
 * @author Ramon Lacava Gutierrez Gonçales
 * @version 1.0.0
 * @date 07/07/2018 10:04:20
 */
public class MaskFormatterUtil {

    private MaskFormatterUtil() {
    }

    public static String format(String text, String mask) {
        try {
            MaskFormatter mf = new MaskFormatter(mask);
            mf.setValueContainsLiteralCharacters(false);
            return mf.valueToString(text);
        } catch (Exception e) {
            return text;
        }
    }
}
