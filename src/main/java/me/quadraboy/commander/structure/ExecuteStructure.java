package me.quadraboy.commander.structure;

import me.quadraboy.commander.structure.Structure;
import me.quadraboy.commander.structure.handler.arguments.Argument;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ExecuteStructure extends Structure {
    private Runnable notAllowedAction = () -> {};
    private final Argument argument;
    public ExecuteStructure(@NotNull CommandSender commandSender, @NotNull Argument argument) {
        super(commandSender);
        this.argument = argument;
    }

    public Argument getArgument() {
        return this.argument;
    }

    public void setNotAllowedAction(@NotNull final Runnable notAllowedAction) {
        this.notAllowedAction = notAllowedAction;
    }

    public Runnable getNotAllowedAction() {
        return notAllowedAction;
    }
}