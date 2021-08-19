package org.unitedinternet.kevfischer.BestClick.model.database;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "user_sessions")
public class Session {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_user_id")
    private User user;

    @Column(unique = true)
    private String session;

    private Date expires;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }
}
