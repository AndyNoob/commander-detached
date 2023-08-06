package me.quadraboy.commander.structure.handlers;

import org.jetbrains.annotations.NotNull;

public abstract class StringHandler {
    private final String[] commandArguments;

    public StringHandler(@NotNull final String[] commandArguments) {
        this.commandArguments = commandArguments;
    }

    public String getString(final int targetIndex) {
        return (this.commandArguments.length > 0) ? this.commandArguments[targetIndex] : "";
    }

    public boolean compare(@NotNull final String toCompare, final int targetIndex) {
        return this.commandArguments[targetIndex].equalsIgnoreCase(toCompare);
    }

    public String[] getStringArray() {
        return this.commandArguments;
    }
}