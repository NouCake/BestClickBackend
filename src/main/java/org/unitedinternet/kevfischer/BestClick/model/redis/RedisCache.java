package org.unitedinternet.kevfischer.BestClick.model.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.unitedinternet.kevfischer.BestClick.model.Ticket;
import org.unitedinternet.kevfischer.BestClick.model.database.LeaderboardPage;
import org.unitedinternet.kevfischer.BestClick.model.database.Session;
import org.unitedinternet.kevfischer.BestClick.model.repository.UserAppRepository;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class RedisCache {

    private static final Duration cacheDuration = Duration.ofSeconds(15);
    private static final Duration ticketDuration = Duration.ofSeconds(60);
    private static final Duration sessionDuration = Duration.ofDays(7);

    @Resource(name = "redisTemplate") private ValueOperations<String, LeaderboardPage> lbOperations;
    @Resource(name = "redisTemplate") private HashOperations<String, String, Session> sessionOperations;
    @Resource(name = "redisTemplate") private ValueOperations<String, Object> redisValueOperations;

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

    public void cache(Ticket ticket){
        redisValueOperations.set(getKeyForTicket(ticket), ticket, ticketDuration);
    }

    public void uncache(UUID session) {
        sessionOperations.delete("sessions", session.toString());
        //sessionOperations.set(getKeyForSession(session), null, 1, TimeUnit.MILLISECONDS);
    }

    public Session getSession(UUID sessionId) {
        return sessionOperations.get("sessions", sessionId.toString());
    }


    public Ticket getTicket(String ticketId){
        return (Ticket)redisValueOperations.get(getKeyForTicket(ticketId));
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

    private static String getKeyForTicket(String ticketId) {
        return String.join(":", "ticket", ticketId);
    }

    private static String getKeyForTicket(Ticket ticket) {
        return String.join(":", "ticket", ticket.getTicketId());
    }

}
