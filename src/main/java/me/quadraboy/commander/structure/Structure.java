package me.quadraboy.commander.structure;

import me.quadraboy.commander.structure.handler.Argument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Structure {
    private final CommandSender commandSender;
    private final Argument argument;

    public Structure(@NotNull final CommandSender commandSender, @NotNull final Argument argument) {
        this.commandSender = commandSender;
        this.argument = argument;
    }

    public Player getPlayer() {
        return (Player) this.commandSender;
    }

    public Argument getArgument() {
        return this.argument;
    }
}