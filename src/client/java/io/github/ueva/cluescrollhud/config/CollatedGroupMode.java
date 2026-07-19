package io.github.ueva.cluescrollhud.config;

/** Controls how tasks are grouped in collated HUD mode. */
public enum CollatedGroupMode {
    MERGE_OBJECTIVES("Merge Objectives"),
    BY_SCROLL("By Scroll"),
    BY_OBJECTIVE_TYPE("By Objective Type");

    private final String label;

    CollatedGroupMode(String label) {
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
