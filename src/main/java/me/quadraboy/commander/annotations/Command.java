package me.quadraboy.commander.annotations;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    @NotNull String name();

    @NotNull InitiatorType target();

    String description() default "";

    String usage() default "";

    String permission() default "";

    String[] aliases() default { };

    enum InitiatorType {
        PLAYER,
        CONSOLE,
        BOTH
    }

    enum Status {
        SUCCESS,
        FAILED
    }
}