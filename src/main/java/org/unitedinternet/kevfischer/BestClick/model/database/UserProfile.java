package org.unitedinternet.kevfischer.BestClick.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "user_profile")
public class UserProfile implements Serializable {

    @Id
    @Type(type="uuid-char")
    private UUID userId;

    @JsonIgnore
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="profile_user_id")
    private User user;

    @Column(length = 255, name="name", unique = true)
    private String name;
    @Column(length = 255, name="picture")
    private String pictureUrl;
    @Column(length = 255, name = "email", unique = true)
    private String email;

    public UserProfile(User user, String name, String pictureUrl, String email) {
        this.user = user;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.email = email;

        this.userId = user.getId();
        user.setProfile(this);
    }

    public UserProfile() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.userId = user.getId();
        this.user = user;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }
}
