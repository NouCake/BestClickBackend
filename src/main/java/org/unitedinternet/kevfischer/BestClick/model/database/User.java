package org.unitedinternet.kevfischer.BestClick.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user")
@PrimaryKeyJoinColumn(name="user_id")
public class User {

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
