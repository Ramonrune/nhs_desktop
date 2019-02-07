/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.healthinstitution;

import com.healthsystem.user.UserDAO;
import com.healthsystem.user.UserModel;
import com.healthsystem.user.UserSingleton;
import com.healthsystem.user.nurse.NurseWorksOnHealthInstitutionModel;
import com.healthsystem.util.MaskFormatterUtil;
import com.healthsystem.util.WEBAPI;
import com.healthsystem.util.azure.AzureBlob;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
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

/**
 * @author Ramon Lacava Gutierrez Gonçales
 * @version 1.0.0
 * @date 30/06/2018 17:38:13
 */
public class HealthInstitutionDAO {

    private java.util.ResourceBundle i18n = java.util.ResourceBundle.getBundle("com/healthsystem/healthinstitution/Bundle"); // NOI18N
    private java.util.ResourceBundle i18nUser = java.util.ResourceBundle.getBundle("com/healthsystem/user/Bundle"); // NOI18N

    public static void findAddress(String postalCode, JTextField stateTextField, JTextField streetTextField, JTextField neighborhoodTextField, JTextField cityTextField) {

        List<HealthInstitutionModel> healthInstitutionList = new ArrayList<HealthInstitutionModel>();
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target("https://viacep.com.br/ws")
                .path(postalCode.replace("-", ""))
                .path("json")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildGet();

        Response response = invoke.invoke();

        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            JSONObject jsonObject = new JSONObject(jsonStr);
            try {
                stateTextField.setText(jsonObject.getString("uf"));
                streetTextField.setText(jsonObject.getString("logradouro"));
                neighborhoodTextField.setText(jsonObject.getString("bairro"));
                cityTextField.setText(jsonObject.getString("localidade"));

            } catch (Exception e) {

            }
        }

    }

    public List<HealthInstitutionModel> getHealthInstitutions(int start, int end, String country, String search) {

        System.err.println(country);
        List<HealthInstitutionModel> healthInstitutionList = new ArrayList<HealthInstitutionModel>();
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("healthinstitution")
                .path("list")
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("country", country)
                .queryParam("search", search)
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

                        String identityCode = json.getString("identityCode");
                        if (json.getString("country").equals("BRA")) {

                            identityCode = MaskFormatterUtil.format(identityCode, "##.###.###/####-##");

                        }

                        if (json.getString("country").equals("FS")) {
                            identityCode = MaskFormatterUtil.format(identityCode, "####/######/##");

                        }
                        HealthInstitutionModel healthInstitutionModel = new HealthInstitutionModel.HealthInstitutionModelBuilder(json.getString("idHealthInstitution"), json.getString("name"))
                                .country(json.getString("country"))
                                .state(json.getString("state"))
                                .identityCode(identityCode)
                                .city(json.getString("city"))
                                .build();

                        healthInstitutionList.add(healthInstitutionModel);

                    }
                }
            }

        }

        return healthInstitutionList;
    }

    public HealthInstitutionModel getHealthInstitution(String id) {

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("healthinstitution")
                .path(id)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildGet();

        Response response = invoke.invoke();
        HealthInstitutionModel healthInstitutionModel = null;
        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            System.out.println("====== " + jsonStr);
            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject json = (JSONObject) jsonArray.get(i);
                        healthInstitutionModel = new HealthInstitutionModel.HealthInstitutionModelBuilder(json.getString("idHealthInstitution"), json.getString("name"))
                                .country(json.getString("country"))
                                .state(json.getString("state"))
                                .identityCode(json.getString("identityCode"))
                                .city(json.getString("city"))
                                .neighborhood(json.getString("neighborhood"))
                                .photo(json.getString("photo"))
                                .number(json.getString("number"))
                                .street(json.getString("street"))
                                .postalCode(json.getString("postalCode"))
                                .telephone(json.isNull("telephone") ? "" : json.getString("telephone"))
                                .build();

                    }
                }
            }

        }

        return healthInstitutionModel;
    }

    public List<UserModel> getUsers(String idHealthInstitution) {

        List<UserModel> listUsers = new ArrayList<>();
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("healthinstitution")
                .path("userlist")
                .path(idHealthInstitution)
                .queryParam("status", "1")
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

                        if (!json.getString("id").equals(UserSingleton.getInstance().getUserId())) {
                            String typeOfUser = "";
                            if (json.getString("typeOfUser").equals("1")) {
                                typeOfUser = i18nUser.getString("UserAddWindow.usetype1");
                            }

                            if (json.getString("typeOfUser").equals("2")) {
                                typeOfUser = i18nUser.getString("UserAddWindow.usetype2");
                            }

                            if (json.getString("typeOfUser").equals("3")) {
                                typeOfUser = i18nUser.getString("UserAddWindow.usetype3");
                            }

                            if (json.getString("typeOfUser").equals("4")) {
                                typeOfUser = i18nUser.getString("UserAddWindow.usetype4");
                            }

                            String identityDocument = json.getString("identityDocument");

                            if (json.getString("identityDocument").length() == 36) {
                                identityDocument = i18nUser.getString("UserAddWindow.notapplicable");
                            } else if (json.getString("country").equals("BRA")) {

                                identityDocument = MaskFormatterUtil.format(identityDocument, "###.###.###-##");

                            }

                            UserModel userModel = new UserModel.UserModelBuilder(json.getString("login"), "")
                                    .id(json.getString("id"))
                                    .name(json.getString("name"))
                                    .country(json.getString("country"))
                                    .state(json.getString("state"))
                                    .identityDocument(identityDocument)
                                    .city(json.getString("city"))
                                    .photo(json.getString("photo"))
                                    .typeOfUser(typeOfUser)
                                    .build();

                            listUsers.add(userModel);
                        }

                    }
                }
            }

        }

        return listUsers;
    }

    public boolean add(String healthInstitutionName,
            String identityCode,
            String country,
            String postalCode,
            String state,
            String city,
            String neighborhood,
            String street,
            String number,
            String photo,
            String telephone) {

        Form form = new Form();
        form.param("_name", healthInstitutionName);
        form.param("_identity_code", identityCode);
        form.param("_postal_code", postalCode);
        form.param("_country", country);
        form.param("_state", state);
        form.param("_city", city);
        form.param("_neighborhood", neighborhood);
        form.param("_street", street);
        form.param("_number", number);
        form.param("_photo", photo);
        form.param("_telephone", telephone);

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("healthinstitution")
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

                JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.limit"));

                return false;
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public boolean update(
            String idHealthInstitution,
            String healthInstitutionName,
            String identityCode,
            String country,
            String postalCode,
            String state,
            String city,
            String neighborhood,
            String street,
            String number,
            String photo,
            String telephone) {

        Form form = new Form();
        form.param("_id_health_institution", idHealthInstitution);
        form.param("_name", healthInstitutionName);
        form.param("_identity_code", identityCode);
        form.param("_postal_code", postalCode);
        form.param("_country", country);
        form.param("_state", state);
        form.param("_city", city);
        form.param("_neighborhood", neighborhood);
        form.param("_street", street);
        form.param("_number", number);
        form.param("_photo", photo);
        form.param("_telephone", telephone);

        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);

        Client newClient = ClientBuilder.newClient(config);
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("healthinstitution")
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

                JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.limit"));

                return false;
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public boolean delete(String id) {

        HealthInstitutionModel healthInstitution = getHealthInstitution(id);
        if (!healthInstitution.getPhoto().equals("HEALTH.jpg")) {
            AzureBlob.delete(healthInstitution.getPhoto());

        }

        Form form = new Form();
        form.param("_id_health_institution", id);
        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        Client newClient = ClientBuilder.newClient(config);

        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("healthinstitution")
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
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public boolean deleteBind(String idHealthInstitution, String idUser) {
        Form form = new Form();
        form.param("_id_health_institution", idHealthInstitution);
        form.param("_id_user", idUser);

        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        Client newClient = ClientBuilder.newClient(config);

        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("healthinstitution")
                .path("deletebind")
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
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public boolean updateBind(String idHealthInstitution, String idUser) {
        Form form = new Form();
        form.param("_id_health_institution", idHealthInstitution);
        form.param("_id_user", idUser);
        form.param("_status", "1");

        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        Client newClient = ClientBuilder.newClient(config);

        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("healthinstitution")
                .path("updatebind")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .build("PUT", Entity.form(form));

        Response response = invoke.invoke();

        System.out.println(response.getStatus() + "-----");
        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);
            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    return true;
                }
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public boolean addBind(String idHealthInstitution, String idUser, String status) {
        Form form = new Form();
        form.param("_id_health_institution", idHealthInstitution);
        form.param("_status", status);

        if (status.equals("0")) {
            form.param("_id_user", new UserDAO().getUser(idUser).getId());

        } else {
            form.param("_id_user", idUser);

        }

        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);
        Client newClient = ClientBuilder.newClient(config);

        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("healthinstitution")
                .path("bind")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .build("POST", Entity.form(form));

        Response response = invoke.invoke();

        System.out.println(response.getStatus() + "-----");
        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            System.out.println(jsonStr);
            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    return true;
                }
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("HealthInstitutionDAO.error"));
        }

        return false;

    }

    public List<NurseWorksOnHealthInstitutionModel> getNurses(String idHealthInstitution) {

        List<NurseWorksOnHealthInstitutionModel> nurseList = new ArrayList<NurseWorksOnHealthInstitutionModel>();
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("healthinstitution")
                .path("nurse")
                .queryParam("idHealthInstitution", idHealthInstitution)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildGet();

        Response response = invoke.invoke();

        System.out.println(response.getStatus() + " asosaoaso");
        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            System.out.println(jsonStr);
            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject json = (JSONObject) jsonArray.get(i);

                        NurseWorksOnHealthInstitutionModel nurseWorksOnHealthInstitutionModel = new NurseWorksOnHealthInstitutionModel();

                        nurseWorksOnHealthInstitutionModel.setCity(json.getString("city"));
                        nurseWorksOnHealthInstitutionModel.setCountry(json.getString("country"));
                        nurseWorksOnHealthInstitutionModel.setIdNurse(json.getString("idNurse"));
                        nurseWorksOnHealthInstitutionModel.setIdUser(json.getString("idUser"));
                        nurseWorksOnHealthInstitutionModel.setName(json.getString("name"));
                        nurseWorksOnHealthInstitutionModel.setState(json.getString("state"));
                        nurseWorksOnHealthInstitutionModel.setPhoto(json.getString("photo"));
                        nurseWorksOnHealthInstitutionModel.setNurseCode(json.getString("nurseCode"));
                        nurseWorksOnHealthInstitutionModel.setNurseType(json.getString("nurseType"));

                        nurseList.add(nurseWorksOnHealthInstitutionModel);

                    }
                }
            }

        }

        return nurseList;
    }

}
