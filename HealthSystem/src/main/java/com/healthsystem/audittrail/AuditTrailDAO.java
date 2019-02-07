package com.healthsystem.audittrail;

import com.healthsystem.errorlog.ErrorLogModel;
import com.healthsystem.user.UserSingleton;
import com.healthsystem.util.MaskFormatterUtil;
import com.healthsystem.util.WEBAPI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
public class AuditTrailDAO {

    public List<AuditTrailModel> getAuditTrailList(String startDate) {

        List<AuditTrailModel> auditTrailList = new ArrayList<>();
        Client newClient = ClientBuilder.newClient();
        Invocation invoke = newClient
                .target(WEBAPI.API)
                .path("auditTrail")
                .path("listAuditTrail")
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

                        AuditTrailModel auditTrailModel = new AuditTrailModel();
                        auditTrailModel.setIdAuditTrail(json.getString("idAuditTrail"));
                        auditTrailModel.setEventName(json.getString("eventName"));
                        auditTrailModel.setCategory(json.getString("category"));
                        auditTrailModel.setAdditionalInfo(json.getString("additionalInfo"));

                        LocalDateTime parse = LocalDateTime.parse(
                                json.getString("eventDate").replace(" ", "T")
                        );
                        
                        auditTrailModel.setEventDate(parse.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
                        auditTrailModel.setPhoto(json.isNull("photo") ? "USER_DEFAULT_PHOTO.jpg" : json.getString("photo"));
                        auditTrailModel.setIdUser(json.isNull("userId") ? "" : json.getString("userId"));
                        auditTrailModel.setName(json.isNull("name") ? "" : json.getString("name"));
                        auditTrailModel.setLogin(json.isNull("login") ? "" : json.getString("login"));
                        auditTrailList.add(auditTrailModel);

                    }
                }
            }

        }

        return auditTrailList;
    }

}
