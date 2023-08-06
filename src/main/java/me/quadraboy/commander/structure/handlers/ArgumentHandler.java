package me.quadraboy.commander.structure.handlers;

import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class ArgumentHandler extends StringHandler {
    private final CommandSender sender;
    private final Component invalidArgumentMessage;

    public ArgumentHandler(@NotNull final String[] commandArguments, @NotNull final CommandSender sender, @NotNull final Component invalidArgumentMessage) {
        super(commandArguments);
        this.sender = sender;
        this.invalidArgumentMessage = invalidArgumentMessage;
    }

    public void create(@NotNull final String name, final int targetIndex, @NotNull final Runnable action) {
        if(compare(name, targetIndex)) {
            action.run();
        } else {
            sender.sendMessage(invalidArgumentMessage);
        }
    }

    public void createIf(@NotNull final Supplier<Boolean> condition, @NotNull final String name, final int targetIndex, @NotNull final Runnable action) {
        if(condition.get()) create(name, targetIndex, action);
    }

    public Optional<Player> toPlayer(final int targetIndex) {
        return Optional.ofNullable(Bukkit.getPlayer(getString(targetIndex)));
    }

    public Optional<Material> toMaterial(final int targetIndex) {
        return Optional.of(Material.valueOf(getString(targetIndex).toUpperCase()));
    }

    public Optional<EntityType> toEntityType(final int targetIndex) {
        return Optional.of(EntityType.valueOf(getString(targetIndex).toUpperCase()));
    }

    public int toInteger(final int targetIndex, final int defaultValue) {
        return (NumberUtils.isCreatable(getString(targetIndex))) ? Integer.parseInt(getString(targetIndex)) : defaultValue;
    }

    public long toLong(final int targetIndex, final long defaultValue) {
        return (NumberUtils.isCreatable(getString(targetIndex))) ? Long.parseLong(getString(targetIndex)) : defaultValue;
    }

    public double toDouble(final int targetIndex, final double defaultValue) {
        return (NumberUtils.isCreatable(getString(targetIndex))) ? Double.parseDouble(getString(targetIndex)) : defaultValue;
    }

    public float toFloat(final int targetIndex, final float defaultValue) {
        return (NumberUtils.isCreatable(getString(targetIndex))) ? Float.parseFloat(getString(targetIndex)) : defaultValue;
    }

    public byte toByte(final int targetIndex, final byte defaultValue) {
        return (NumberUtils.isCreatable(getString(targetIndex))) ? Byte.parseByte(getString(targetIndex)) : defaultValue;
    }
}