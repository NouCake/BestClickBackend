package org.unitedinternet.kevfischer.BestClick.model.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.unitedinternet.kevfischer.BestClick.model.database.LeaderboardPage;
import org.unitedinternet.kevfischer.BestClick.model.repository.UserAppRepository;

import javax.annotation.Resource;
import java.time.Duration;

public class LeaderboardCache {

    private static final Duration cacheDuration = Duration.ofSeconds(15);

    @Resource(name = "lbTemplate") private ValueOperations<String, LeaderboardPage> lbOperations;
    @Autowired private UserAppRepository appRepository;

    public LeaderboardPage getPage(int size, int page){
        String key = getKeyForPage(size, page);

        return lbOperations.get(key);
    }

    public void cache(LeaderboardPage lbPage){
        lbOperations.set(getKeyForPage(lbPage), lbPage, cacheDuration);
    }

    private static String getKeyForPage(LeaderboardPage lbPage) {
        return getKeyForPage(lbPage.getSize(), lbPage.getPage());
    }

    private static String getKeyForPage(int size, int page) {
        return String.join(":", "lbpage", String.valueOf(size), String.valueOf(page));
    }

}
