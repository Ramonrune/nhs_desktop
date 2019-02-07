/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.user.physician;

import com.healthsystem.user.specialization.SpecializationModel;
import com.healthsystem.user.UserModel;
import com.healthsystem.user.UserSingleton;
import com.healthsystem.util.LanguageUtil;
import com.healthsystem.util.WEBAPI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
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

public class PhysicianDAO {

    private java.util.ResourceBundle i18n = java.util.ResourceBundle.getBundle("com/healthsystem/user/Bundle"); // NOI18N

    public boolean add(
            String idUser,
            String idPhysician,
            String practiceDocument
    ) {

        Form form = new Form();
        form.param("_id_user", idUser);
        form.param("_id_physician", idPhysician);
        form.param("_practice_document", practiceDocument);

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("physician")
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
            String idPhysician,
            String practiceDocument
    ) {

        Form form = new Form();
        form.param("_id_user", idUser);
        form.param("_id_physician", idPhysician);
        System.out.println(practiceDocument + " --------------");
        form.param("_practice_document", practiceDocument);

        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);

        Client newClient = ClientBuilder.newClient(config);
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("physician")
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

    public PhysicianModel getPhysician(String idUser) {

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("physician")
                .queryParam("id_user", idUser)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildGet();

        PhysicianModel physicianModel = null;
        Response response = invoke.invoke();
        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject json = (JSONObject) jsonArray.get(i);

                        physicianModel = new PhysicianModel();
                        physicianModel.setIdPhysician(json.getString("idPhysician"));
                        physicianModel.setIdUser(idUser);
                        physicianModel.setPracticeDocument(json.getString("practiceDocument"));

                    }

                }
            }
        }

        return physicianModel;
    }

    public List<SpecializationModel> listPhysicianSpecialization(String idPhysician) {

        List<SpecializationModel> list = new ArrayList<>();
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("physician")
                .path("listSpecializations")
                .queryParam("id_physician", idPhysician)
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
                .path("physician")
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

        Collections.sort(list, (s1, s2) -> {
            return s1.getName().compareTo(s2.getName());
        });

        return list;
    }

    public boolean bindSpecialization(
            String idPhysician,
            String idSpecialization
    ) {

        Form form = new Form();
        form.param("_id_physician", idPhysician);
        form.param("_id_specialization", idSpecialization);

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("physician")
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
            String idPhysician,
            String idSpecialization) {

        Form form = new Form();
        form.param("_id_physician", idPhysician);
        form.param("_id_specialization", idSpecialization);

        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);

        Client newClient = ClientBuilder.newClient(config);

        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("physician")
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

    public List<DiagnosisHistoryModel> listDiagnosisHistory(String idPhysician, String idHealthInstitution) {

        List<DiagnosisHistoryModel> list = new ArrayList<>();
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("physician")
                .path("listDiagnosisHistory")
                .queryParam("id_physician", idPhysician)
                .queryParam("id_health_institution", idHealthInstitution)
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

                        DiagnosisHistoryModel diagnosisHistoryModel = new DiagnosisHistoryModel();
                        diagnosisHistoryModel.setDateDiagnosis(json.getString("dateDiagnosis"));
                        diagnosisHistoryModel.setName(json.getString("name"));
                        if (json.isNull("photo")) {
                            diagnosisHistoryModel.setPhoto("USER_DEFAULT_PHOTO.jpg");

                        } else {
                            diagnosisHistoryModel.setPhoto(json.getString("photo"));

                        }

                        list.add(diagnosisHistoryModel);
                    }

                }
            }
        }

        return list;
    }

    public List<ExamHistoryModel> listExamsHistory(String idPhysician, String idHealthInstitution) {

        List<ExamHistoryModel> list = new ArrayList<>();
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("physician")
                .path("listExamsHistory")
                .queryParam("id_physician", idPhysician)
                .queryParam("id_health_institution", idHealthInstitution)
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

                        ExamHistoryModel examHistoryModel = new ExamHistoryModel();
                        examHistoryModel.setDateExam(json.getString("dateExam"));
                        examHistoryModel.setName(json.getString("name"));
                        if (json.isNull("photo")) {
                            examHistoryModel.setPhoto("USER_DEFAULT_PHOTO.jpg");

                        } else {
                            examHistoryModel.setPhoto(json.getString("photo"));

                        }
                        list.add(examHistoryModel);
                    }

                }
            }
        }

        return list;
    }

    public boolean addPhysicianAttendance(
            String idPhysicianAttendance,
            String dateAttendance,
            String idPatient,
            String idPhysician,
            String idHealthInstitution
    ) {

        Form form = new Form();
        form.param("_id_physician_attendance", idPhysicianAttendance);
        form.param("_date_attendance", dateAttendance);
        form.param("_id_patient", idPatient);
        form.param("_id_physician", idPhysician);
        form.param("_id_health_institution", idHealthInstitution);

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("physician")
                .path("physicianAttendanceRegister")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildPost(Entity.form(form));

        Response response = invoke.invoke();

        System.out.println(response.getStatus() + " sakoasokasdok");
        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            System.out.println(jsonStr + " asoaoas");
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

    public List<PhysicianAttendanceModel> listPhysicianAttendance(
            String idPhysician,
            String idHealthInstitution,
            String date) {

        List<PhysicianAttendanceModel> list = new ArrayList<>();
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("physician")
                .path("listPhysicianAttendance")
                .queryParam("id_physician", idPhysician)
                .queryParam("id_health_institution", idHealthInstitution)
                .queryParam("date", date)
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

                        PhysicianAttendanceModel physicianAttendanceModel = new PhysicianAttendanceModel();
                        physicianAttendanceModel.setName(json.getString("name"));
                        physicianAttendanceModel.setIdHealthInstitution(json.getString("idHealthInstitution"));
                        physicianAttendanceModel.setDateAttendance(json.getString("dateAttendance"));
                        physicianAttendanceModel.setPhoto(json.getString("photo"));
                        physicianAttendanceModel.setIdPhysician(json.getString("idPhysician"));
                        physicianAttendanceModel.setIdPatient(json.getString("idPatient"));
                        physicianAttendanceModel.setIdUser(json.getString("idUser"));
                        physicianAttendanceModel.setIdPhysicianAttendance(json.getString("idPhysicianAttendance"));

                        list.add(physicianAttendanceModel);
                    }

                }
            }
        }

        return list;
    }
}
