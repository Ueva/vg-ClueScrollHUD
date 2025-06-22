package io.github.ueva.cluescrollhud.models;

import io.github.ueva.cluescrollhud.config.TaskSortMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ClueScroll {

    private final String uuid;
    private final String tier;
    private final long created;
    private final ArrayList<ClueTask> clues;
    private long expire;
    private int invPosition;

    public ClueScroll(String uuid, String tier, long created, long expire, int invPosition, ArrayList<ClueTask> clues) {
        this.uuid = uuid;
        this.tier = tier;
        this.created = created;
        this.expire = expire;
        this.invPosition = invPosition;
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

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public int getInvPosition() {
        return invPosition;
    }

    public void setInvPosition(int invPosition) {
        this.invPosition = invPosition;
    }

    public ArrayList<ClueTask> getClues() {
        return clues;
    }

    public ArrayList<ClueTask> getSortedClues(TaskSortMode mode, boolean reverse) {
        ArrayList<ClueTask> sortedClues = new ArrayList<>(clues);

        Comparator<ClueTask> comparator = switch (mode) {
            // Sort by the raw progress amount.
            case PROGRESS_AMOUNT -> Comparator.comparingInt(ClueTask::getCompleted);

            // Sort by the percentage of total amount completed.
            case PROGRESS_PERCENT -> Comparator.comparingInt(ClueTask::getPercentCompleted);

            // Sort by the first word of the objective, effectively grouping similar tasks together.
            case OBJECTIVE_TYPE -> Comparator.comparing(
                    clueTask -> clueTask.getFormattedObjective().split(" ")[0],
                    String.CASE_INSENSITIVE_ORDER
            );

            // Otherwise, use the order the clues were stored in the original NBT data.
            case DEFAULT -> null;
        };

        // If necessary, reverse the order of the (sorted or unsorted) clues.
        if (comparator != null) {
            sortedClues.sort(reverse ? comparator.reversed() : comparator);
        }
        else if (reverse) {
            Collections.reverse(sortedClues);
        }

        return sortedClues;
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
