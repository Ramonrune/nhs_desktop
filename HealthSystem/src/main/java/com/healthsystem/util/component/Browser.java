/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.util.component;

import com.healthsystem.healthinstitution.HealthInstitutionSingleton;
import com.healthsystem.user.UserSingleton;
import com.healthsystem.util.WEBAPI;
import java.util.Locale;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javax.swing.JPanel;

/**
 * @author Ramon Lacava Gutierrez Gonçales
 * @version 1.0.0
 * @date 28/07/2018 14:17:59
 */
public class Browser extends JPanel {

    private JFXPanel jfxPanel;

    public Browser() {
     
        jfxPanel = new JFXPanel();
        add(jfxPanel);

// Creation of scene and future interactions with JFXPanel
// should take place on the JavaFX Application Thread
    }
    
     public void attendance() {
         
      
        System.out.println(WEBAPI.MAP + "physician_graph.jsp?lang=" + Locale.getDefault().getLanguage() + "&id_physician=" + UserSingleton.getInstance().getPhysicianModel().getIdPhysician() + "&id_health_institution=" + HealthInstitutionSingleton.getInstance().getIdHealthInstitution());
        Platform.setImplicitExit(false);

        Platform.runLater(() -> {
            WebView webView = new WebView();

            jfxPanel.setScene(new Scene(webView));

            webView.getEngine().load(WEBAPI.MAP + "physician_graph.jsp?lang=" + Locale.getDefault().getLanguage() + "&id_physician=" + UserSingleton.getInstance().getPhysicianModel().getIdPhysician() + "&id_health_institution=" + HealthInstitutionSingleton.getInstance().getIdHealthInstitution());
        });
    }

    public void run(String lat, String lng) {
        System.out.println(WEBAPI.MAP + "map.jsp?lat=" + lat + "&lng=" + lng);
        Platform.setImplicitExit(false);

        Platform.runLater(() -> {
            WebView webView = new WebView();

            jfxPanel.setScene(new Scene(webView));

            webView.getEngine().load(WEBAPI.MAP + "map.jsp?lat=" + lat + "&lng=" + lng);
        });
    }
    
     public void run() {
        System.out.println(WEBAPI.MAP + "health_institutions.jsp");
        Platform.setImplicitExit(false);

        Platform.runLater(() -> {
            WebView webView = new WebView();

            jfxPanel.setScene(new Scene(webView));

            webView.getEngine().load(WEBAPI.MAP + "health_institutions.jsp");
        });
    }
     
        public void runErrorGraph() {
            
        System.out.println(WEBAPI.MAP + "error_chart.jsp?lang=" + Locale.getDefault().getLanguage());
        Platform.setImplicitExit(false);

        Platform.runLater(() -> {
            WebView webView = new WebView();

            jfxPanel.setScene(new Scene(webView));

            webView.getEngine().load(WEBAPI.MAP + "error_chart.jsp");
        });
    }
}
