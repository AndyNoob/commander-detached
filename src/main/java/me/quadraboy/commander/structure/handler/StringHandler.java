package me.quadraboy.commander.structure.handler;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public abstract class StringHandler {
    private final String[] strings;

    public StringHandler(@NotNull final String[] strings) {
        this.strings = strings;
    }

    public String getString(final int targetIndex) {
        return (strings.length == targetIndex + 1) ? strings[targetIndex] : "";
    }

    public String[] getStringsFrom(final int fromIndex) {
        return (strings.length > 0) ? Arrays.copyOfRange(strings, fromIndex, strings.length) : new String[0];
    }

    public boolean compare(@NotNull final String toCompare, final int targetIndex) {
        return toCompare.equalsIgnoreCase(getString(targetIndex));
    }

    public String[] getStrings() {
        return this.strings;
    }
}