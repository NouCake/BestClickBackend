package org.unitedinternet.kevfischer.BestClick.model;


import javax.validation.constraints.NotNull;

public class RegisterRequest {

    @NotNull private String username;
    @NotNull private String password;

    @NotNull private String name;
    private String email;
    private String profile;

    public RegisterRequest() {
    }

    public RegisterRequest(String username, String password, String name, String email, String profile) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.profile = profile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
