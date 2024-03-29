/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */

package com.healthsystem.util.azure;


import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

/* ImageFilter.java is used by FileChooserDemo2.java. */
public class ImageFilter extends FileFilter {

    //Accept all directories and all gif, jpg, tiff, or png files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (
                extension.equals(Utils.jpeg) ||
                extension.equals(Utils.jpg)
               ) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "Imagens";
    }
}