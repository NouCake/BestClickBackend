package org.unitedinternet.kevfischer.BestClick.model.database;

import org.springframework.data.redis.core.RedisHash;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeInt(size);
        out.writeInt(page);
        out.writeInt(users.size());
        for(UserAppData appData : users) {
            out.writeObject(appData.getUserId());
            out.writeObject(appData.getCounter());
        }
    }
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        size = in.readInt();
        page = in.readInt();
        int length = in.readInt();


        users = new ArrayList<>(length);
        for(int i = 0; i < length; i++) {
            UUID uuid = (UUID) in.readObject();
            Long counter = (Long)in.readObject();
            users.add(i, new UserAppData(new User(uuid), counter));
        }

    }

}
