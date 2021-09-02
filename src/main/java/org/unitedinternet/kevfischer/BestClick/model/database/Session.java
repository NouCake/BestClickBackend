package org.unitedinternet.kevfischer.BestClick.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "user_sessions")
public class Session {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_user_id")
    private User user;

    @Id
    @Column(unique = true)
    @Type(type="uuid-char")
    private UUID session;

    private Date expires;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UUID getSession() {
        return session;
    }

    public void setSession(UUID session) {
        this.session = session;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }
}
