/*
 * Direitos reservados a Ramon Lacava Gutierrez Gonçales
 * ramonrune@gmail.com
 */
package com.healthsystem.user;

import com.healthsystem.util.MaskFormatterUtil;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class UserModel {

    private String id;
    private String login;
    private String password;
    private String name;
    private String typeOfUser;
    private String bornDate;
    private String gender;
    private String postalCode;
    private String country;
    private String state;
    private String city;
    private String street;
    private String neighborhood;
    private String number;
    private String photo;
    private String identityDocument;
    private String telephone;

    public static class UserModelBuilder {

        private String id;
        private String login;
        private String password;
        private String name;
        private String typeOfUser;
        private String bornDate;
        private String gender;
        private String postalCode;
        private String country;
        private String state;
        private String city;
        private String street;
        private String neighborhood;
        private String number;
        private String photo;
        private String identityDocument;
        private String telephone;

        public UserModelBuilder(String password) {
            this.login = "not-needed";
            this.password = password;
        }

        public UserModelBuilder(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public UserModelBuilder name(String name) {
            this.name = name;
            return this;
        }

        public UserModelBuilder id(String id) {
            this.id = id;
            return this;
        }

        public UserModelBuilder typeOfUser(String typeOfUser) {
            this.typeOfUser = typeOfUser;
            return this;
        }

        public UserModelBuilder bornDate(String bornDate) {
            this.bornDate = bornDate;
            return this;
        }

        public UserModelBuilder gender(String gender) {
            this.gender = gender;
            return this;
        }

        public UserModelBuilder postalCode(String postalCode) {
            this.postalCode = postalCode;
            return this;
        }

        public UserModelBuilder country(String country) {
            this.country = country;
            return this;
        }

        public UserModelBuilder state(String state) {
            this.state = state;
            return this;
        }

        public UserModelBuilder city(String city) {
            this.city = city;
            return this;
        }

        public UserModelBuilder street(String street) {
            this.street = street;
            return this;
        }

        public UserModelBuilder neighborhood(String neighborhood) {
            this.neighborhood = neighborhood;
            return this;
        }

        public UserModelBuilder number(String number) {
            this.number = number;
            return this;
        }

        public UserModelBuilder photo(String photo) {
            this.photo = photo;
            return this;
        }

        public UserModelBuilder identityDocument(String identityDocument) {
            this.identityDocument = identityDocument;
            return this;
        }

        public UserModelBuilder telephone(String telephone) {
            this.telephone = telephone;
            return this;
        }

        public UserModel build() {
            return new UserModel(this);
        }

    }

    private UserModel(UserModelBuilder builder) {
        this.id = builder.id;
        this.login = builder.login;
        this.password = builder.password;
        this.name = builder.name;
        this.typeOfUser = builder.typeOfUser;
        this.bornDate = builder.bornDate;
        this.gender = builder.gender;
        this.postalCode = builder.postalCode;
        this.country = builder.country;
        this.state = builder.state;
        this.city = builder.city;
        this.street = builder.street;
        this.neighborhood = builder.neighborhood;
        this.number = builder.number;
        this.photo = builder.photo;
        this.identityDocument = builder.identityDocument;
        this.telephone = builder.telephone;

    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getTypeOfUser() {
        return typeOfUser;
    }

    public String getBornDate() {
        return bornDate;
    }

    public int getAge() {

        try {
            DateTimeFormatter DATEFORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate bornDateLocalDate = LocalDate.parse(bornDate, DATEFORMATTER);

            return Period.between(bornDateLocalDate, LocalDate.now()).getYears();

        } catch (Exception e) {
            return 0;
        }
    }

    public String getGender() {
        return gender;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getNumber() {
        return number;
    }

    public String getPhoto() {
        return photo;
    }

    public String getIdentityDocument() {
        return identityDocument;
    }

    public String getIdentityDocumentFormatted() {

        if (country.equals("BRA")) {
            return MaskFormatterUtil.format(identityDocument, "###.###.###-##");

        }
        if (country.equals("FS")) {
            return MaskFormatterUtil.format(identityDocument, "#############");

        }
        return identityDocument;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

}
