package org.unitedinternet.kevfischer.BestClick.model;

public class RegisterRequest {

    private String name;
    private String email;
    private String profile;

    public RegisterRequest() {
    }

    public RegisterRequest(String name, String email, String profile) {
        this.name = name;
        this.email = email;
        this.profile = profile;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
