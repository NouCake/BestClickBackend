package org.unitedinternet.kevfischer.BestClick.model.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.unitedinternet.kevfischer.BestClick.model.database.LeaderboardPage;
import org.unitedinternet.kevfischer.BestClick.model.database.Session;
import org.unitedinternet.kevfischer.BestClick.model.repository.UserAppRepository;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RedisCache {

    private static final Duration cacheDuration = Duration.ofSeconds(15);
    private static final Duration sessionDuration = Duration.ofDays(7);

    @Resource(name = "lbTemplate") private ValueOperations<String, LeaderboardPage> lbOperations;
    @Resource(name = "sessionTemplate") private HashOperations<String, String, Session> sessionOperations;

    public LeaderboardPage getPage(int size, int page){
        String key = getKeyForPage(size, page);

        return lbOperations.get(key);
    }

    public void cache(LeaderboardPage lbPage){
        lbOperations.set(getKeyForPage(lbPage), lbPage, cacheDuration);
    }
    public void cache(Session session){
        sessionOperations.put("sessions", session.getSession().toString(), session);
        //sessionOperations.set(getKeyForSession(session), session, sessionDuration);
    }
    public void uncache(UUID session) {
        sessionOperations.delete("sessions", session.toString());
        //sessionOperations.set(getKeyForSession(session), null, 1, TimeUnit.MILLISECONDS);
    }

    public Session getSession(UUID sessionId) {
        return sessionOperations.get("sessions", sessionId.toString());
    }


    private static String getKeyForSession(UUID session) {
        return String.join(":", "session", session.toString());
    }

    private static String getKeyForSession(Session session) {
        return getKeyForSession(session.getSession());
    }

    private static String getKeyForPage(LeaderboardPage lbPage) {
        return getKeyForPage(lbPage.getSize(), lbPage.getPage());
    }

    private static String getKeyForPage(int size, int page) {
        return String.join(":", "lbpage", String.valueOf(size), String.valueOf(page));
    }

}
