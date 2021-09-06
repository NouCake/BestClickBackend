package org.unitedinternet.kevfischer.BestClick.model.database;

import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@RedisHash(value = "lb", timeToLive = 15)
public class LeaderboardPage implements Serializable {

    private int size;
    private int page;
    private List<UserAppData> users;

    public LeaderboardPage(int size, int page, List<UserAppData> users) {
        this.size = size;
        this.page = page;
        this.users = users;
    }

    public LeaderboardPage() {
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<UserAppData> getUsers() {
        return users;
    }

    public void setUsers(List<UserAppData> users) {
        this.users = users;
    }
}
