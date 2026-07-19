package io.github.ueva.cluescrollhud.hudelement;

import io.github.ueva.cluescrollhud.models.ClueScroll;
import io.github.ueva.cluescrollhud.models.ClueTask;

/**
 * A clue task to display in collated HUD mode.
 * In merge mode, tasks with the same objective template are combined into one entry with summed progress.
 * In By Scroll / By Objective Type modes, each entry is a single unmerged task.
 */
public record CollatedTaskEntry(ClueScroll scroll, ClueTask task, int taskIndex, boolean spansMultipleTiers) {

    public CollatedTaskEntry(ClueScroll scroll, ClueTask task, int taskIndex) {
        this(scroll, task, taskIndex, false);
    }
}
