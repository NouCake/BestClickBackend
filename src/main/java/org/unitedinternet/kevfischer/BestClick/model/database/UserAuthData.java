package org.unitedinternet.kevfischer.BestClick.model.database;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_auth")
public class UserAuthData {

    @Id
    @Type(type="uuid-char")
    private UUID userId;

    @JsonIgnore
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="auth_user_id")
    private User user;

    private String username;
    private String passwordHash;

    public UserAuthData(User user, String username, String passwordHash) {
        this.user = user;
        this.username = username;
        this.passwordHash = passwordHash;

        this.userId = user.getId();
        user.setAuthData(this);
    }

    public UserAuthData() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
