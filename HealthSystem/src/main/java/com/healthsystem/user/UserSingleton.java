package com.healthsystem.user;

import com.healthsystem.user.physician.PhysicianDAO;
import com.healthsystem.user.physician.PhysicianModel;

public class UserSingleton {

    private String token;
    private String userType;
    private String userId;
    private String userName;
    private PhysicianModel physicianModel;
    private PhysicianDAO physicianDAO = new PhysicianDAO();

    private static UserSingleton instance = null;

    private UserSingleton() {

    }

    public static UserSingleton getInstance() {
        if (instance == null) {
            instance = new UserSingleton();
        }
        return instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {

        return userType.equals("0");
    }

    public boolean isHealthInstitutionAdmin() {
        return userType.equals("1");
    }

    public boolean isPhysician() {
        return userType.equals("2");
    }

    public boolean isParamedic() {
        return userType.equals("3");
    }

    public boolean isNurse() {
        return userType.equals("4");
    }

    public boolean isPatient() {
        return userType.equals("5");
    }

    public String getUserName() {
        return userName;
    }

    public void setUserType(String userType) {
        this.userType = userType;
        if(isPhysician()){
            if(userId.isEmpty()){
                throw new IllegalStateException("User id cannot be null");
            }
            physicianModel = physicianDAO.getPhysician(userId);
        }
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public PhysicianModel getPhysicianModel() {
        return physicianModel;
    }
    
    

}
