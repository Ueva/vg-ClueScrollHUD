package io.github.ueva.cluescrollhud.config;

/** Controls whether the HUD shows one scroll at a time or all scrolls collated together. */
public enum ElementDisplayMode {
    SINGLE_SCROLL("Single Scroll"),
    COLLATED("Collated");

    private final String label;

    ElementDisplayMode(String label) {
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
