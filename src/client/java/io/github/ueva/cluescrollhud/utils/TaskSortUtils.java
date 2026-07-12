package io.github.ueva.cluescrollhud.utils;

import io.github.ueva.cluescrollhud.config.TaskSortMode;
import io.github.ueva.cluescrollhud.models.ClueTask;

import java.util.Comparator;

/**
 * Shared task-sorting logic used when ordering clues within a single scroll
 * and when ordering collated tasks across all scrolls.
 */
public class TaskSortUtils {

    public static Comparator<ClueTask> comparator(TaskSortMode mode) {
        return switch (mode) {
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
    }
}
