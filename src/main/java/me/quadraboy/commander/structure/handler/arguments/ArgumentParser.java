package me.quadraboy.commander.structure.handler.arguments;

import org.jetbrains.annotations.NotNull;

public interface ArgumentParser<T> {

    T parse(@NotNull final String argument);
}