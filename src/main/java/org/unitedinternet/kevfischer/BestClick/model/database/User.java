package org.unitedinternet.kevfischer.BestClick.model.database;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;


@Entity
@Table(name = "users")
@PrimaryKeyJoinColumn(name="user_id")
public class User implements Serializable {

    @Id
    @Column(name="user_id")
    @Type(type="uuid-char")
    private UUID id;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(referencedColumnName = "profile_user_id")
    private UserProfile profile;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(referencedColumnName = "app_user_id")
    private UserAppData appData;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn(referencedColumnName = "auth_user_id")
    private UserAuthData authData;

    public User() {
    }

    public User(UUID id) {
        this.id = id;
    }

    public User(UUID id, UserProfile profile, UserAppData appData, UserAuthData authData) {
        this.id = id;
        this.profile = profile;
        this.appData = appData;
        this.authData = authData;
    }

    public UserAuthData getAuthData() {
        return authData;
    }

    public void setAuthData(UserAuthData authData) {
        this.authData = authData;
    }

    public UserAppData getAppData() {
        return appData;
    }

    public void setAppData(UserAppData appData) {
        this.appData = appData;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
