package org.unitedinternet.kevfischer.BestClick.model.redis;


import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@RedisHash("session")
public class Session {

    @Id private UUID id;
    private UUID uuid;
    private Date expires;

    public UUID getUserId() {
        return uuid;
    }

    public void setUserId(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getSession() {
        return id;
    }

    public void setSession(UUID session) {
        this.id = session;
    }

    public Date getExpires() {
        return expires;
    }

    public void setExpires(Date expires) {
        this.expires = expires;
    }
}
