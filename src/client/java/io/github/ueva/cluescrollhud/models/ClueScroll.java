package io.github.ueva.cluescrollhud.models;

import java.util.ArrayList;


public class ClueScroll {

    private final String uuid;
    private final String tier;
    private final long created;
    private final long expire;
    private ArrayList<ClueTask> clues;

    public ClueScroll(String uuid, String tier, long created, long expire, ArrayList<ClueTask> clues) {
        this.uuid = uuid;
        this.tier = tier;
        this.created = created;
        this.expire = expire;
        this.clues = clues;
    }

    public String getUuid() {
        return uuid;
    }

    public String getTier() {
        return tier;
    }

    public long getCreated() {
        return created;
    }

    public long getExpire() {
        return expire;
    }

    public ArrayList<ClueTask> getClues() {
        return clues;
    }

    public ClueTask getClueTask(int index) {
        if (index < 0 || index >= clues.size()) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return clues.get(index);
    }

    public int getClueCount() {
        return clues.size();
    }

    public boolean isCompleted() {
        for (ClueTask clue : clues) {
            if (!clue.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expire;
    }

}
