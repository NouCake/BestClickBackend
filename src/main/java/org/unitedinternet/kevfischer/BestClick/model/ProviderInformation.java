package org.unitedinternet.kevfischer.BestClick.model;

import org.unitedinternet.kevfischer.BestClick.model.database.UserProfile;

public class ProviderInformation extends UserProfile {

    private String providerId;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
