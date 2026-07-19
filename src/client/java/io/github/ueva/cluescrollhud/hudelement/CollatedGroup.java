package io.github.ueva.cluescrollhud.hudelement;

import java.util.List;

/**
 * A labeled section of collated HUD tasks.
 * When {@code header} is null (merge mode), the group is rendered as a flat task list with no section label.
 */
public record CollatedGroup(String header, Integer headerColour, List<CollatedTaskEntry> entries) {
}
