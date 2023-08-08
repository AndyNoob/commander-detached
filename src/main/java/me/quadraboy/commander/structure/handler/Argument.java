package me.quadraboy.commander.structure.handler;

import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Supplier;

public class Argument extends StringHandler {

    public Argument(@NotNull String[] strings) {
        super(strings);
    }

    public void create(@NotNull final String name, final int targetIndex, @NotNull final Runnable action, @NotNull final Runnable invalidArgumentAction) {
        if(compare(name, targetIndex)) {
            action.run();
        } else {
            invalidArgumentAction.run();
        }
    }

    public void create(@NotNull final String name, final int targetIndex, @NotNull final Runnable action) {
        create(name, targetIndex, action, () -> {});
    }

    public void createIf(@NotNull final Supplier<Boolean> condition, @NotNull final String name, final int targetIndex, @NotNull final Runnable action, @NotNull final Runnable invalidArgumentAction) {
        if(condition.get()) create(name, targetIndex, action, invalidArgumentAction);
    }

    public void createIf(@NotNull final Supplier<Boolean> condition, @NotNull final String name, final int targetIndex, @NotNull final Runnable action) {
        createIf(condition, name, targetIndex, action, () -> {});
    }

    public Optional<Player> toPlayer(final int targetIndex) {
        return Optional.ofNullable(Bukkit.getPlayer(getString(targetIndex)));
    }

    public Optional<Material> toMaterial(final int targetIndex) {
        return Optional.ofNullable(Material.getMaterial(getString(targetIndex).toUpperCase()));
    }

    public Optional<EntityType> toEntityType(final int targetIndex) {
        return Optional.ofNullable(EntityType.fromName(getString(targetIndex).toUpperCase()));
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