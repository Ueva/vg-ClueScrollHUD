package io.github.ueva.cluescrollhud.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;


public record ClueScroll(String tier, int created, int expire, List<ClueTask> clues) {

    public static final Codec<ClueScroll> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("tier")
                            .forGetter(ClueScroll::tier),
                    Codec.INT.fieldOf("amount")
                            .forGetter(ClueScroll::created),
                    Codec.INT.fieldOf("completed")
                            .forGetter(ClueScroll::expire),
                    ClueTask.CODEC.listOf()
                            .fieldOf("clues")
                            .forGetter(ClueScroll::clues)
            )
            .apply(instance, ClueScroll::new));
}
// Note: Currently not used, but prepared in case Vulengate decide to move from old-style NBT to new-style data
// components.