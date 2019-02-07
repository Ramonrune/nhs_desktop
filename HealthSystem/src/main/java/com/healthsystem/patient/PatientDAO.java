/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.patient;

import com.healthsystem.healthinstitution.HealthInstitutionModel;
import com.healthsystem.user.UserSingleton;
import com.healthsystem.util.LanguageUtil;
import com.healthsystem.util.MaskFormatterUtil;
import com.healthsystem.util.WEBAPI;
import com.healthsystem.util.dataprovider.DiseaseList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
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

public class PatientDAO {

    private java.util.ResourceBundle i18n = java.util.ResourceBundle.getBundle("com/healthsystem/healthinstitution/Bundle"); // NOI18N
    private java.util.ResourceBundle i18nPatient = java.util.ResourceBundle.getBundle("com/healthsystem/patient/Bundle"); // NOI18N

    public boolean add(
            String idPatient,
            String bloodType,
            String color,
            String fatherName,
            String motherName,
            String weight,
            String height,
            String status,
            String idStatus,
            StringBuilder id
    ) {

        Form form = new Form();
        form.param("_id_patient", idPatient);
        form.param("_blood_type", bloodType);
        form.param("_color", color);
        form.param("_father_name", fatherName);
        form.param("_mother_name", motherName);
        form.param("_weight", weight);
        form.param("_height", height);
        form.param("_status", status);
        form.param("_id_user", idStatus);

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildPost(Entity.form(form));

        Response response = invoke.invoke();

        System.out.println("aquuiii " + response.getStatus());
        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            System.out.println("==== " + jsonStr);
            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    return true;
                }
            } else {

                if (jsonObject.getInt("code") == -1) {
                    id.append(jsonObject.getString("description"));
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
                }

                return false;
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public boolean update(
            String idPatient,
            String bloodType,
            String color,
            String fatherName,
            String motherName,
            String weight,
            String height,
            String status,
            String idUser
    ) {

        Form form = new Form();
        form.param("_id_patient", idPatient);
        form.param("_blood_type", bloodType);
        form.param("_color", color);
        form.param("_father_name", fatherName);
        form.param("_mother_name", motherName);
        form.param("_weight", weight);
        form.param("_height", height);
        form.param("_status", status);
        form.param("_id_user", idUser);

        System.out.println("_weight " + weight);
        System.out.println("_height " + height);

        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);

        Client newClient = ClientBuilder.newClient(config);
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .build("PUT", Entity.form(form));

        Response response = invoke.invoke();

        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);
            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    return true;
                }
            } else {

                JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));

                return false;
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public PatientModel getPatientByTagCode(String idTag) {

        PatientModel patientModel = null;
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("tag")
                .queryParam("id_tag", idTag.trim())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildGet();

        Response response = invoke.invoke();

        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            System.out.println("retornou : " + jsonStr);
            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        if (jsonArray.isNull(i)) {
                            return null;
                        }

                        JSONObject json = (JSONObject) jsonArray.get(i);
                        patientModel = new PatientModel();
                        patientModel.setColor(json.isNull("color") ? null : json.getString("color"));
                        patientModel.setBloodType(json.isNull("bloodType") ? null : json.getString("bloodType"));
                        patientModel.setIdPatient(json.getString("idPatient"));
                        patientModel.setIdUser(json.getString("idUser"));
                        patientModel.setFatherName(json.isNull("fatherName") ? "" : json.getString("fatherName"));
                        patientModel.setMotherName(json.isNull("motherName") ? "" : json.getString("motherName"));
                        patientModel.setHeight(json.getDouble("height"));
                        patientModel.setWeight(json.getDouble("weight"));
                        patientModel.setStatus(json.getString("status"));

                        break;

                    }
                }
            }

        }

        return patientModel;
    }

    public PatientModel getPatient(String idUser) {

        PatientModel patientModel = null;
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("patientData")
                .queryParam("id_user", idUser)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildGet();

        Response response = invoke.invoke();

        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            System.out.println("retornou : " + jsonStr);
            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        if (jsonArray.isNull(i)) {
                            return null;
                        }

                        JSONObject json = (JSONObject) jsonArray.get(i);
                        patientModel = new PatientModel();
                        patientModel.setColor(json.isNull("color") ? null : json.getString("color"));
                        patientModel.setBloodType(json.isNull("bloodType") ? null : json.getString("bloodType"));
                        patientModel.setIdPatient(json.getString("idPatient"));
                        patientModel.setIdUser(json.getString("idUser"));
                        patientModel.setFatherName(json.isNull("fatherName") ? "" : json.getString("fatherName"));
                        patientModel.setMotherName(json.isNull("motherName") ? "" : json.getString("motherName"));
                        patientModel.setHeight(json.getDouble("height"));
                        patientModel.setWeight(json.getDouble("weight"));
                        patientModel.setStatus(json.getString("status"));

                        break;

                    }
                }
            }

        }

        return patientModel;
    }

    public boolean tagExist(String idTag) {

        PatientModel patientModel = null;
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("tagExist")
                .queryParam("id_tag", idTag.trim())
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildGet();

        Response response = invoke.invoke();

        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                return true;
            } else {
                return false;
            }

        }

        return false;
    }

    public boolean registerTag(
            String idPatientTag,
            String idTag,
            String idPatient
    ) {

        Form form = new Form();
        form.param("_id_patient_tag", idPatientTag);
        form.param("_id_tag", idTag);
        form.param("_id_patient", idPatient);

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("tagRegister")
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

                JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));

                return false;
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public boolean updateTag(
            String idPatientTag,
            String name,
            String tagType
    ) {

        Form form = new Form();
        form.param("_id_patient_tag", idPatientTag);
        form.param("_name", name);
        form.param("_tag_type", tagType);

        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);

        Client newClient = ClientBuilder.newClient(config);
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("tagUpdate")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .build("PUT", Entity.form(form));

        Response response = invoke.invoke();

        if (response.getStatus() == 200) {

            String jsonStr = response.readEntity(String.class);
            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    return true;
                }
            } else {

                JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));

                return false;
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public boolean deleteTag(
            String idPatientTag
    ) {

        Form form = new Form();
        form.param("_id_patient_tag", idPatientTag);

        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);

        Client newClient = ClientBuilder.newClient(config);
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("tagDelete")
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

                JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));

                return false;
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public List<PatientTagModel> getPatientTags(String idPatient) {

        List<PatientTagModel> list = new ArrayList<>();
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("listPatientTag")
                .queryParam("id_patient", idPatient)
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
                        PatientTagModel patientTagModel = new PatientTagModel();
                        patientTagModel.setIdPatient(idPatient);
                        patientTagModel.setIdPatientTag(json.getString("idPatientTag"));
                        patientTagModel.setName(json.isNull("name") ? i18nPatient.getString("PatientPanel.notInformed") : json.getString("name"));
                        patientTagModel.setMacCode(json.getString("macCode"));
                        patientTagModel.setTagType(json.isNull("tagType") ? i18nPatient.getString("PatientPanel.notInformed") : json.getString("tagType"));
                        list.add(patientTagModel);
                    }
                }
            }

        }

        return list;
    }

    public boolean addDiagnosis(
            String idDiagnosis,
            String dateDiagnosis,
            String anotation,
            String idPatient,
            String idPhysician,
            String idHealthInstitution
    ) {

        Form form = new Form();
        form.param("_id_diagnosis", idDiagnosis);
        form.param("_date_diagnosis", dateDiagnosis);
        form.param("_anotation", anotation);
        form.param("_id_patient", idPatient);
        form.param("_id_physician", idPhysician);
        form.param("_id_health_institution", idHealthInstitution);

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("diagnosis")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildPost(Entity.form(form));

        Response response = invoke.invoke();

        if (response.getStatus() == 200) {

            System.out.println("aquiiiii deu 200");
            String jsonStr = response.readEntity(String.class);
            System.out.println(jsonStr + " --------------");
            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    return true;
                }
            } else {

                JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));

                return false;
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public boolean updateDiagnosis(
            String idDiagnosis,
            String anotation
    ) {

        Form form = new Form();
        form.param("_id_diagnosis", idDiagnosis);
        form.param("_anotation", anotation);

        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);

        Client newClient = ClientBuilder.newClient(config);

        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("diagnosis")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .build("PUT", Entity.form(form));

        Response response = invoke.invoke();

        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);
            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    return true;
                }
            } else {

                JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));

                return false;
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public boolean deleteDiagnosis(
            String idDiagnosis
    ) {

        Form form = new Form();
        form.param("_id_diagnosis", idDiagnosis);

        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);

        Client newClient = ClientBuilder.newClient(config);

        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("diagnosis")
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

                JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));

                return false;
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public List<DiagnosisModel> getPatientDiagnosis(String idPatient) {

        List<DiagnosisModel> list = new ArrayList<>();
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("diagnosis")
                .queryParam("id_patient", idPatient)
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
                        DiagnosisModel diagnosisModel = new DiagnosisModel();
                        diagnosisModel.setIdDiagnosis(json.getString("idDiagnosis"));
                        diagnosisModel.setDateDiagnosis(json.getString("dateDiagnosis"));
                        diagnosisModel.setAnnotation(json.getString("annotation"));
                        diagnosisModel.setIdPatient(idPatient);
                        diagnosisModel.setIdPhysician(json.getString("idPhysician"));
                        diagnosisModel.setIdHealthInstitution(json.getString("idHealthInstitution"));
                        diagnosisModel.setHealthInstitutionName(json.getString("healthInstitutionName"));
                        diagnosisModel.setPhysicianName(json.getString("physicianName"));
                        diagnosisModel.setPhysicianPracticeNumber(json.getString("physicianPracticeNumber"));
                        diagnosisModel.setPhysicianCountry(json.getString("physicianCountry"));
                        diagnosisModel.setPhysicianPhoto(json.getString("physicianPhoto"));
                        diagnosisModel.setHealthInstitutionPhoto(json.getString("healthInstitutionPhoto"));
                        diagnosisModel.setHealthInstitutionLatitute(json.getDouble("healthInstitutionLatitute"));
                        diagnosisModel.setHealthInstitutionLongitute(json.getDouble("healthInstitutionLongitute"));

                        list.add(diagnosisModel);
                    }
                }
            }

        }

        return list;
    }

    public boolean bindDisease(
            String idPatientHasDisease,
            String idPatient,
            String idDisease,
            String anotation
    ) {

        Form form = new Form();
        form.param("_id_patient_has_disease", idPatientHasDisease);
        form.param("_id_patient", idPatient);
        form.param("_id_disease", idDisease);
        form.param("_anotation", anotation);

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("disease")
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

                JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));

                return false;
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public boolean updateBindDisease(
            String idPatientHasDisease,
            String anotation
    ) {

        Form form = new Form();
        form.param("_id_patient_has_disease", idPatientHasDisease);
        form.param("_anotation", anotation);

        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);

        Client newClient = ClientBuilder.newClient(config);
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("disease")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .build("PUT", Entity.form(form));

        Response response = invoke.invoke();

        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);
            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    return true;
                }
            } else {

                JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));

                return false;
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public boolean unbindDisease(
            String idPatientHasDisease) {

        Form form = new Form();
        form.param("_id_patient_has_disease", idPatientHasDisease);

        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);

        Client newClient = ClientBuilder.newClient(config);
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("disease")
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

                JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));

                return false;
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public List<DiseaseModel> getDiseases() {

        List<DiseaseModel> list = new ArrayList<>();
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("disease")
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
                        DiseaseModel diseaseModel = new DiseaseModel();
                        diseaseModel.setIdDisease(json.getString("idDisease"));
                        String name = "";
                        if (Locale.getDefault().getCountry().equals("BR")) {
                            name = json.getString("namePt");

                        } else {
                            name = json.getString("nameEn");
                        }
                        diseaseModel.setName(name);

                        list.add(diseaseModel);
                    }
                }
            }

        }

        return list;
    }

    public List<PatientHasDiseaseModel> getPatientDisease(String idPatient) {

        List<PatientHasDiseaseModel> list = new ArrayList<>();
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("patientDisease")
                .queryParam("id_patient", idPatient)
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

                        PatientHasDiseaseModel patientHasDiseaseModel = new PatientHasDiseaseModel();
                        patientHasDiseaseModel.setAnotation(json.getString("anotation"));
                        patientHasDiseaseModel.setIdPatientHasDisease(json.getString("idPatientHasDisease"));
                        patientHasDiseaseModel.setIdPatient(idPatient);
                        patientHasDiseaseModel.setIdDisease(json.getString("idDisease"));
                        patientHasDiseaseModel.setNameDisease(DiseaseList.getInstance().getDiseaseName(json.getString("idDisease")));

                        list.add(patientHasDiseaseModel);
                    }
                }
            }

        }

        return list.stream().sorted((e1, e2) -> e1.getNameDisease().compareTo(e2.getNameDisease())).collect(Collectors.toList());

    }

    public boolean addExam(
            String idExam,
            String anotation,
            String idPatient,
            String idPhysician,
            String idHealthInstitution,
            String dateExam
    ) {

        Form form = new Form();
        form.param("_id_exam", idExam);
        form.param("_anotation", anotation);
        form.param("_id_patient", idPatient);
        form.param("_id_physician", idPhysician);
        form.param("_id_health_institution", idHealthInstitution);
        form.param("_date_exam", dateExam);

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("exam")
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

                JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));

                return false;
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public boolean updateExam(
            String idExam,
            String anotation
    ) {

        Form form = new Form();
        form.param("_id_exam", idExam);
        form.param("_anotation", anotation);

        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);

        Client newClient = ClientBuilder.newClient(config);

        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("exam")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .build("PUT", Entity.form(form));

        Response response = invoke.invoke();

        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);
            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    return true;
                }
            } else {

                JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));

                return false;
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public List<ExamModel> getPatientExams(String idPatient) {

        List<ExamModel> list = new ArrayList<>();
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("exam")
                .queryParam("id_patient", idPatient)
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
                        ExamModel examModel = new ExamModel();
                        examModel.setIdExam(json.getString("idExam"));
                        examModel.setDateExam(json.getString("dateExam"));
                        examModel.setAnotation(json.getString("anotation"));
                        examModel.setIdPatient(idPatient);
                        examModel.setIdPhysician(json.getString("idPhysician"));
                        examModel.setIdHealthInstitution(json.getString("idHealthInstitution"));
                        examModel.setHealthInstitutionName(json.getString("healthInstitutionName"));
                        examModel.setPhysicianName(json.getString("physicianName"));
                        examModel.setPhysicianPracticeNumber(json.getString("physicianPracticeNumber"));
                        examModel.setPhysicianCountry(json.getString("physicianCountry"));
                        examModel.setPhysicianPhoto(json.getString("physicianPhoto"));
                        examModel.setHealthInstitutionPhoto(json.getString("healthInstitutionPhoto"));
                        examModel.setHealthInstitutionLatitute(json.getDouble("healthInstitutionLatitute"));
                        examModel.setHealthInstitutionLongitute(json.getDouble("healthInstitutionLongitute"));
                        examModel.setAttachmentCount(json.getInt("attachmentSize"));
                        list.add(examModel);
                    }
                }
            }

        }

        return list;
    }

    public boolean addExamAttachment(
            String idExamAttachment,
            String attachmentName,
            String idExam
    ) {

        Form form = new Form();
        form.param("_id_exam_attachment", idExamAttachment);
        form.param("_attachment_name", attachmentName);
        form.param("_id_exam", idExam);

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("exam")
                .path("attachment")
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

                JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));

                return false;
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public boolean deleteExamAttachment(
            String idExamAttachment) {

        Form form = new Form();
        form.param("_id_exam_attachment", idExamAttachment);

        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);

        Client newClient = ClientBuilder.newClient(config);

        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("exam")
                .path("attachment")
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

                JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));

                return false;
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public List<ExamAttachmentModel> getExamAttachments(String idExam) {

        List<ExamAttachmentModel> list = new ArrayList<>();
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("exam")
                .path("attachment")
                .queryParam("id_exam", idExam)
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
                        ExamAttachmentModel examAttachmentModel = new ExamAttachmentModel();
                        examAttachmentModel.setIdExam(json.getString("idExam"));
                        examAttachmentModel.setIdExamAttachment(json.getString("idExamAttachment"));
                        examAttachmentModel.setAttachmentName(json.getString("attachmentName"));

                        list.add(examAttachmentModel);
                    }
                }
            }

        }

        return list;
    }

    public List<MedicineModel> listMedicines(String search, String country, String language) {

        List<MedicineModel> list = new ArrayList<>();
        return list;
        /*
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("listMedicines")
                .queryParam("search", search)
                .queryParam("language", language)
                .queryParam("country", country)
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
                        DiseaseModel diseaseModel = new DiseaseModel();
                        diseaseModel.setIdDisease(json.getString("idDisease"));
                        String name = "";
                        if (Locale.getDefault().getCountry().equals("BR")) {
                            name = json.getString("namePt");

                        } else {
                            name = json.getString("nameEn");
                        }
                        diseaseModel.setName(name);

                        list.add(diseaseModel);
                    }
                }
            }

        }

        return list;
         */
    }

    public List<MedicineModel> getPatientMedicines(String idPatient) {

        List<MedicineModel> list = new ArrayList<>();
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("listPatientMedicines")
                .queryParam("id_patient", idPatient)
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
                        MedicineModel medicineModel = new MedicineModel();
                        medicineModel.setIdMedicine(json.getString("idMedicine"));
                        medicineModel.setLanguage(json.getString("language"));
                        medicineModel.setName(json.getString("name"));
                        medicineModel.setCountry(json.getString("country"));
                        medicineModel.setStatus(json.getString("status"));
                        list.add(medicineModel);
                    }
                }
            }

        }

        return list;
    }
    
     public DiagnosisProcedureModel getDiagnosisProcedure(String idDiagnosisProcedure) {

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("patient")
                .path("diagnosis")
                .path("procedures")
                .queryParam("id_diagnosis", idDiagnosisProcedure)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildGet();

        Response response = invoke.invoke();

        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);
            System.out.println(jsonStr);
            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    System.out.println(jsonArray.length());
                   
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject json = (JSONObject) jsonArray.get(i);
                        
                        if(json.isNull("nurseName")){
                            return null;
                        }
                        
                        DiagnosisProcedureModel diagnosisProcedureModel = new DiagnosisProcedureModel();
                        diagnosisProcedureModel.setNurseName(json.getString("nurseName"));
                        diagnosisProcedureModel.setNursePhoto(json.getString("nursePhoto"));
                        diagnosisProcedureModel.setAnotation(json.getString("anotation"));
                     
                        return diagnosisProcedureModel;
                    }
                }
            }

        }

        return null;
    }

}
