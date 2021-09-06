package org.unitedinternet.kevfischer.BestClick.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "user_app")
public class UserAppData implements Serializable {

    @Id
    @Type(type="uuid-char")
    private UUID userId;

    @JsonIgnore
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="app_user_id")
    private User user;

    private Long counter;


    public UserAppData(User user, Long counter) {
        this.user = user;
        this.counter = counter;

        this.userId = user.getId();
        user.setAppData(this);
    }

    public UserAppData() {
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

    public Long getCounter() {
        return counter;
    }

    public void setCounter(Long counter) {
        this.counter = counter;
    }
}
