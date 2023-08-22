package me.quadraboy.commander.structure.handler;

import me.quadraboy.commander.structure.handler.StringHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Suggestion extends StringHandler {
    private final List<String> completions = new ArrayList<>();


    public Suggestion(@NotNull String[] strings) {
        super(strings);
    }

    public void create(final int targetIndex, @NotNull final String... suggestions) {
        if(getStrings().length == targetIndex + 1) completions.addAll(List.of(suggestions));
    }

    public void createIf(@NotNull final Supplier<Boolean> condition, final int targetIndex, @NotNull final String... suggestions) {
        if(condition.get()) create(targetIndex, suggestions);
    }

    public List<String> getCompletions() {
        return this.completions;
    }
}