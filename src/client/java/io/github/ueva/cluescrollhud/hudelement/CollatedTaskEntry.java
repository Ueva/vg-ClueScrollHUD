package io.github.ueva.cluescrollhud.hudelement;

import io.github.ueva.cluescrollhud.models.ClueScroll;
import io.github.ueva.cluescrollhud.models.ClueTask;

/**
 * A clue task to display in collated HUD mode.
 * Tasks with the same objective template are merged into one entry with combined progress.
 */
public record CollatedTaskEntry(ClueScroll scroll, ClueTask task, int taskIndex, boolean spansMultipleTiers) {

    public CollatedTaskEntry(ClueScroll scroll, ClueTask task, int taskIndex) {
        this(scroll, task, taskIndex, false);
    }
}
