/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.errorlog;

import com.healthsystem.user.UserSingleton;
import com.healthsystem.util.MaskFormatterUtil;
import com.healthsystem.util.WEBAPI;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Ramon Lacava Gutierrez Gonçales
 * @version 1.0.0
 * @date 15/09/2018 21:43:41
 */
public class ErrorLogDAO {
    
    public List<ErrorLogModel> getErrors(String startDate) {
        
        List<ErrorLogModel> listErrors = new ArrayList<>();
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("errorlog")
                .path("listErrors")
                .queryParam("startDate", startDate)
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
                        
                        ErrorLogModel errorLogModel = new ErrorLogModel();
                        errorLogModel.setIdError(json.getString("idError"));
                        LocalDateTime parse = LocalDateTime.parse(
                                json.getString("eventDate").replace(" ", "T")
                        );
                        
                        errorLogModel.setEventDate(parse.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                        errorLogModel.setClassName(json.getString("className"));
                        errorLogModel.setLineNumber(json.getInt("lineNumber"));
                        errorLogModel.setMethodName(json.getString("methodName"));
                        errorLogModel.setNameOfFile(json.getString("nameOfFile"));
                        errorLogModel.setPhoto(json.isNull("photo") ? "USER_DEFAULT_PHOTO.jpg" : json.getString("photo"));
                        errorLogModel.setUserId(json.isNull("userId") ? "" : json.getString("userId"));
                        errorLogModel.setName(json.isNull("name") ? "" : json.getString("name"));
                        errorLogModel.setLogin(json.isNull("login") ? "" : json.getString("login"));
                        errorLogModel.setAdditionalInfo(json.getString("additionalInfo"));
                        
                        listErrors.add(errorLogModel);
                        
                    }
                }
            }
            
        }
        
        return listErrors;
    }
    
}
