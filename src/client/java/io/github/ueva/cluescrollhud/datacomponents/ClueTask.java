package io.github.ueva.cluescrollhud.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;


public record ClueTask(String objective, float amount, float completed) {

    public static final Codec<ClueTask> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("objective").forGetter(ClueTask::objective),
            Codec.FLOAT.fieldOf("amount").forGetter(ClueTask::amount),
            Codec.FLOAT.fieldOf("completed").forGetter(ClueTask::completed)
    ).apply(instance, ClueTask::new));
}
// Note: Currently not used, but prepared in case Vulengate decide to move from old-style NBT to new-style data
// components.