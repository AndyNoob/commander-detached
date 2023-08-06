package me.quadraboy.commander.structure.handlers;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SuggestionHandler extends StringHandler {
    private final List<String> completions = new ArrayList<>();

    public SuggestionHandler(@NotNull String[] commandArguments) {
        super(commandArguments);
    }

    public void create(final int targetIndex, @NotNull final String... suggestions) {
        if(getStringArray().length == targetIndex + 1) completions.addAll(List.of(suggestions));
    }

    public void createIf(@NotNull final Supplier<Boolean> condition, final int targetIndex, @NotNull final String... suggestions) {
        if(condition.get()) create(targetIndex, suggestions);
    }

    public List<String> getCompletions() {
        return this.completions;
    }
}