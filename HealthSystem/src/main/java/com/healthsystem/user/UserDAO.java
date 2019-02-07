/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.user;

import com.healthsystem.healthinstitution.HealthInstitutionModel;
import com.healthsystem.util.Criptography;
import com.healthsystem.util.MaskFormatterUtil;
import com.healthsystem.util.WEBAPI;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;
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

/**
 * @author Ramon Lacava Gutierrez Gonçales
 * @version 1.0.0
 * @date 23/06/2018 21:27:45
 */
public class UserDAO {

    public boolean userExist(String login, String password) {

        Form form = new Form();
        form.param("_login", login);
        form.param("_password", Criptography.sha256(password));

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("user")
                .path("auth")
                .request(MediaType.APPLICATION_JSON)
                .buildPost(Entity.form(form));

        Response response = invoke.invoke();

        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    
                    UserSingleton.getInstance().setToken("Bearer " + jsonObject.getString("token"));
                    UserSingleton.getInstance().setUserId(jsonObject.getString("userId"));
                    UserSingleton.getInstance().setUserName(jsonObject.getString("userName"));
                    UserSingleton.getInstance().setUserType(jsonObject.getString("userType").trim());

                    return true;
                }
            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.error1"));
        }

        return false;
    }
    
    
     public String userCorrect(String login, String password) {

        Form form = new Form();
        form.param("_login", login);
        form.param("_password", Criptography.sha256(password));

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("user")
                .path("auth")
                .request(MediaType.APPLICATION_JSON)
                .buildPost(Entity.form(form));

        Response response = invoke.invoke();

        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                  
                    return jsonObject.getString("userId");
                }
            }

        }

        return "";
    }

     
      public String getUserBySecretCode(String secretCode) {

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("user")
                .path("secretCode")
                .queryParam("secretCode", secretCode)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildGet();

        Response response = invoke.invoke();

        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                  
                    return jsonObject.getString("userId");
                }
            }

        }

        return "";
    }
      
    public List<HealthInstitutionModel> getHealthInstitutions(String userId, String status) {

        List<HealthInstitutionModel> healthInstitutionList = new ArrayList<HealthInstitutionModel>();
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("user")
                .path("healthinstitutionbind")
                .path(userId)
                .queryParam("status", status)
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
                                .photo(json.getString("photo"))
                                .city(json.getString("city"))
                                .build();

                        healthInstitutionList.add(healthInstitutionModel);

                    }
                }
            }

        }

        return healthInstitutionList;
    }
    private java.util.ResourceBundle i18n = java.util.ResourceBundle.getBundle("com/healthsystem/user/Bundle"); // NOI18N

    public boolean add(
            String name,
            String identityDocument,
            String country,
            String postalCode,
            String state,
            String city,
            String neighborhood,
            String street,
            String number,
            String photo,
            String telephone,
            String login,
            String password,
            String typeOfUser,
            String gender,
            String bornDate,
            StringBuilder id) {

        System.out.println("aisij " + country);

        Form form = new Form();
        form.param("_login", login);
        form.param("_password", password);
        form.param("_name", name);
        form.param("_country", country);
        form.param("_type_of_user", typeOfUser);
        form.param("_postal_code", postalCode);
        form.param("_state", state);
        form.param("_city", city);
        form.param("_street", street);
        form.param("_neighborhood", neighborhood);
        form.param("_number", number);
        form.param("_photo", photo);
        form.param("_identity_document", identityDocument);
        form.param("_born_date", bornDate);
        form.param("_gender", gender);
        form.param("_telephone", telephone);

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("user")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildPost(Entity.form(form));

        Response response = invoke.invoke();

        System.out.println("aquiiii");

        System.out.println("aqqqquiii " + response.getStatus());

        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            System.out.println("---- " + jsonStr);

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {
                    id.append(jsonObject.getString("id"));

                    return true;
                }
            } else if (jsonObject.getInt("code") == -2 || jsonObject.getInt("code") == -3) {
                JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.error2"));

                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.error1"));
        }

        return false;
    }

    public boolean update(
            String login,
            String password,
            String name,
            String identityDocument,
            String country,
            String postalCode,
            String state,
            String city,
            String neighborhood,
            String street,
            String number,
            String photo,
            String telephone,
            String typeOfUser,
            String gender,
            String bornDate,
            String id,
            boolean loginChanged,
            boolean passwordChanged,
            boolean identityDocumentChanged
    ) {

        Form form = new Form();
        form.param("_user_id", id);
        if (loginChanged) {
            form.param("_login", login);
        }
        if (identityDocumentChanged) {
            form.param("_identity_document", identityDocument);

        }

        if (passwordChanged && !password.equals("")) {
            form.param("_password", Criptography.sha256(password));

        }

        form.param("_name", name);
        form.param("_country", country);
        form.param("_type_of_user", typeOfUser);
        form.param("_postal_code", postalCode);
        form.param("_state", state);
        form.param("_city", city);
        form.param("_street", street);
        form.param("_neighborhood", neighborhood);
        form.param("_number", number);
        form.param("_photo", photo);
        form.param("_born_date", bornDate);
        form.param("_gender", gender);
        form.param("_telephone", telephone);

        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);

        Client newClient = ClientBuilder.newClient(config);
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("user")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .build("PUT", Entity.form(form));

        Response response = invoke.invoke();

        System.out.println(response.getStatus() + " ----");

        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);
            System.out.println(jsonStr + " ----");

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    return true;
                }
            } else if (jsonObject.getInt("code") == -2) {
                JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.error3"));

            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.error1"));
        }

        return false;
    }

    public boolean updateUserType(
            String login,
            String typeOfUser
    ) {

        Form form = new Form();

        form.param("_login", login);
        form.param("_type_of_user", typeOfUser);

        ClientConfig config = new ClientConfig();
        config.property(ClientProperties.SUPPRESS_HTTP_COMPLIANCE_VALIDATION, true);

        Client newClient = ClientBuilder.newClient(config);
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("user")
                .path("updateUserType")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .build("PUT", Entity.form(form));

        Response response = invoke.invoke();

        System.out.println(response.getStatus() + " ----");

        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);
            System.out.println(jsonStr + " ----");

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    return true;
                }
            } else if (jsonObject.getInt("code") == -2) {
                JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.error3"));

            }

        } else {
            JOptionPane.showMessageDialog(null, i18n.getString("UserAddWindow.error1"));
        }

        return false;
    }

    public UserModel getUser(String idUser) {

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("user")
                .path(idUser)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildGet();
        UserModel userModel = null;
        Response response = invoke.invoke();
        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject json = (JSONObject) jsonArray.get(i);

                        userModel = new UserModel.UserModelBuilder(json.getString("login"), "")
                                .id(json.getString("id"))
                                .name(json.getString("name"))
                                .country(json.getString("country"))
                                .state(json.getString("state"))
                                .identityDocument(json.getString("identityDocument"))
                                .city(json.getString("city"))
                                .photo(json.isNull("photo") ? "USER_DEFAULT_PHOTO.jpg" : json.getString("photo"))
                                .typeOfUser(json.getString("typeOfUser"))
                                .bornDate(json.isNull("bornDate") ? null : json.getString("bornDate"))
                                .postalCode(json.getString("postalCode"))
                                .number(json.getString("number"))
                                .gender(json.getString("gender"))
                                .street(json.getString("street"))
                                .neighborhood(json.getString("neighborhood"))
                                .telephone(json.isNull("telephone") ? "" : json.getString("telephone"))
                                .build();

                    }

                }
            }
        }

        return userModel;
    }

    public UserModel getUserByLogin(String login) {

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("user")
                .path("search")
                .path(login)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildGet();
        UserModel userModel = null;
        Response response = invoke.invoke();
        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject json = (JSONObject) jsonArray.get(i);

                        userModel = new UserModel.UserModelBuilder(json.getString("login"), "")
                                .id(json.getString("id"))
                                .name(json.getString("name"))
                                .country(json.getString("country"))
                                .state(json.getString("state"))
                                .identityDocument(json.getString("identityDocument"))
                                .city(json.getString("city"))
                                .photo(json.getString("photo"))
                                .typeOfUser(json.getString("typeOfUser"))
                                .bornDate(json.isNull("bornDate") ? "" : json.getString("bornDate"))
                                .postalCode(json.getString("postalCode"))
                                .number(json.getString("number"))
                                .gender(json.getString("gender"))
                                .street(json.getString("street"))
                                .neighborhood(json.getString("neighborhood"))
                                .telephone(json.isNull("telephone") ? "" : json.getString("telephone"))
                                .build();

                    }

                }
            }
        }

        return userModel;
    }

    public UserModel getUserSearch(String login) {

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("user")
                .path("search")
                .path(login)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildGet();
        UserModel userModel = null;
        Response response = invoke.invoke();
        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);

            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    JSONArray jsonArray = jsonObject.getJSONArray("list");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject json = (JSONObject) jsonArray.get(i);

                        userModel = new UserModel.UserModelBuilder(json.getString("login"), "")
                                .id(json.getString("id"))
                                .name(json.getString("name"))
                                .country(json.getString("country"))
                                .state(json.getString("state"))
                                .identityDocument(json.getString("identityDocument"))
                                .city(json.getString("city"))
                                .photo(json.getString("photo"))
                                .typeOfUser(json.getString("typeOfUser"))
                                .bornDate(json.getString("bornDate"))
                                .postalCode(json.getString("postalCode"))
                                .number(json.getString("number"))
                                .gender(json.getString("gender"))
                                .street(json.getString("street"))
                                .neighborhood(json.getString("neighborhood"))
                                .telephone(json.isNull("telephone") ? "" : json.getString("telephone"))
                                .build();

                    }

                }
            }
        }

        return userModel;
    }
    
 
    public boolean sendEmailPassword(String login, String password, String country) {

        Form form = new Form();
        form.param("_login", login);
        form.param("_password", password);
        form.param("_country", country);

        System.out.println(login + " ---- " + password);

        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("user")
                .path("sendPasswordMail")
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, UserSingleton.getInstance().getToken())
                .buildPost(Entity.form(form));

        Response response = invoke.invoke();

        
        System.out.println(response.getStatus());
        if (response.getStatus() == 200) {
            String jsonStr = response.readEntity(String.class);
            System.out.println(jsonStr + " --------");
            JSONObject jsonObject = new JSONObject(jsonStr);

            if (jsonObject.getBoolean("success")) {
                if (jsonObject.getInt("code") == 0) {

                    return true;
                }
            }

        }

        return false;
    }
}
