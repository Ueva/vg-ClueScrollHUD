package io.github.ueva.cluescrollhud.config;

public enum ScrollSortMode {
    DEFAULT("Default"),
    INV_POSITION("Inventory Position"),
    START_TIME("Start Time"),
    EXPIRY_TIME("Expiry Time"),
    TIER("Tier");

    private final String label;

    ScrollSortMode(String label) {
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
