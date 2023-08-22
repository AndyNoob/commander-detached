package me.quadraboy.commander.structure;

import me.quadraboy.commander.structure.handler.arguments.Argument;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class Structure {
    private final CommandSender commandSender;

    public Structure(@NotNull final CommandSender commandSender) {
        this.commandSender = commandSender;
    }

    public CommandSender getSender() {
        return this.commandSender;
    }

    public Player getPlayer() {
        return (Player) this.commandSender;
    }
}