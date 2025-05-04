package io.github.ueva.cluescrollhud.config;

public enum TaskSortMode {
    DEFAULT("Default"),
    PROGRESS_AMOUNT("Progress (Amount)"),
    PROGRESS_PERCENT("Progress (Percentage)"),
    OBJECTIVE_TYPE("Objective Type");

    private final String label;

    TaskSortMode(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
