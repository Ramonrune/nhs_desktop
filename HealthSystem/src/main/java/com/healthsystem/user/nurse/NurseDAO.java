/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.user.nurse;

import com.healthsystem.user.physician.*;
import com.healthsystem.user.specialization.SpecializationModel;
import com.healthsystem.user.UserSingleton;
import com.healthsystem.util.LanguageUtil;
import com.healthsystem.util.WEBAPI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.json.JSONArray;
import org.json.JSONObject;

public class NurseDAO {

    private java.util.ResourceBundle i18n = java.util.ResourceBundle.getBundle("com/healthsystem/user/Bundle"); // NOI18N

    public boolean add(
            String idUser,
            String idNurse,
            String nurseCode,
            String nurseType
    ) {

        Form form = new Form();
        form.param("_id_user", idUser);
        form.param("_id_nurse", idNurse);
        form.param("_nurse_code", nurseCode);
        form.param("_nurse_type", nurseType);

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("nurse")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildPost(Entity.form(form));

        Response response = invoke.invoke();

        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    return true;
                }
            } else {
                JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.error1"));

            }
        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.error1"));
        }

        return false;
    }

    public boolean update(
            String idUser,
            String idNurse,
            String nurseCode,
            String nurseType
    ) {

        Form form = new Form();
        form.param("_id_user", idUser);
        form.param("_id_nurse", idNurse);
        form.param("_nurse_code", nurseCode);
        form.param("_nurse_type", nurseType);

        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);

        Client newClient = ClientBuilder.newClient(config);
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("nurse")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .build("PUT", Entity.form(form));

        Response response = invoke.invoke();

        System.out.println(response.getStatus() + " -----");

        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);
            System.out.println(jsonStr + " -----");

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    return true;
                }
            } else {
                JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.error1"));

            }
        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.error1"));
        }

        return false;
    }

    public NurseModel getNurse(String idUser) {

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("nurse")
                .queryParam("id_user", idUser)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildGet();

        NurseModel nurseModel = null;
        Response response = invoke.invoke();
        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject json = (JSONObject) jsonArray.get(i);

                        nurseModel = new NurseModel();
                        nurseModel.setIdNurse(json.getString("idNurse"));
                        nurseModel.setIdUser(idUser);
                        nurseModel.setNurseCode(json.getString("nurseCode"));
                        nurseModel.setNurseType(json.getString("nurseType"));

                    }

                }
            }
        }

        return nurseModel;
    }

    public List<SpecializationModel> listNurseSpecialization(String idNurse) {

        List<SpecializationModel> list = new ArrayList<>();
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("nurse")
                .path("listSpecializations")
                .queryParam("id_nurse", idNurse)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildGet();

        Response response = invoke.invoke();
        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject json = (JSONObject) jsonArray.get(i);

                        SpecializationModel specializationModel = new SpecializationModel();
                        specializationModel.setIdSpecialization(json.getString("idSpecialization"));
                        list.add(specializationModel);
                    }

                }
            }
        }

        return list;
    }

    public List<SpecializationModel> listSpecialization(String country) {

        List<SpecializationModel> list = new ArrayList<>();
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("nurse")
                .path("specialization")
                .queryParam("country", country)
                .queryParam("language", LanguageUtil.language())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildGet();

        Response response = invoke.invoke();
        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject json = (JSONObject) jsonArray.get(i);

                        SpecializationModel specializationModel = new SpecializationModel();
                        specializationModel.setIdSpecialization(json.getString("idSpecialization"));
                        specializationModel.setName(json.getString("name"));

                        list.add(specializationModel);
                    }

                }
            }
        }

  
        Collections.sort(list,  (s1, s2) -> {
            return s1.getName().compareTo(s2.getName());
        });
        
        return list;
    }

    public boolean bindSpecialization(
            String idNurse,
            String idSpecialization
    ) {

        Form form = new Form();
        form.param("_id_nurse", idNurse);
        form.param("_id_specialization", idSpecialization);

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("nurse")
                .path("bindSpecialization")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildPost(Entity.form(form));

        Response response = invoke.invoke();

        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    return true;
                }
            } else {
                JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.error1"));

            }
        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.error1"));
        }

        return false;
    }

    public boolean unbindSpecialization(
            String idNurse,
            String idSpecialization) {

        Form form = new Form();
        form.param("_id_nurse", idNurse);
        form.param("_id_specialization", idSpecialization);

        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);

        Client newClient = ClientBuilder.newClient(config);

        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("nurse")
                .path("unbindSpecialization")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .build("DELETE", Entity.form(form));

        Response response = invoke.invoke();

        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    return true;
                }
            } else {
                JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.error1"));

            }
        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.error1"));
        }

        return false;
    }
    
    
      public boolean addToWaitList(
            String idHealthInstitution,
            String idDiagnosis
    ) {

        Form form = new Form();
        form.param("_id_diagnosis", idDiagnosis);
      
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("healthinstitution")
                .path("waitlist")
                .path(idHealthInstitution)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildPost(Entity.form(form));

        Response response = invoke.invoke();

        System.out.println(response.getStatus());
        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            System.out.println(" --- " + jsonStr);
            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    return true;
                }
            } else {
                JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.error1"));

            }
        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.error1"));
        }

        return false;
    }
}
