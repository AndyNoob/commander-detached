package me.quadraboy.commander;

import me.quadraboy.commander.annotations.Command;
import me.quadraboy.commander.annotations.Executor;
import me.quadraboy.commander.annotations.Suggester;
import me.quadraboy.commander.structure.Structure;
import me.quadraboy.commander.structure.handler.Argument;
import me.quadraboy.commander.structure.handler.Suggestion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class CommandRegistry {
    private final Plugin plugin;
    private final CommandMap commandMap = Bukkit.getServer().getCommandMap();

    public CommandRegistry(@NotNull final Plugin plugin) {
        this.plugin = plugin;
    }

    public void registerCommand(@NotNull final Object commandObject) {
        final Class<?> targetClass = commandObject.getClass();

        final Command command = Optional.of(targetClass.getAnnotation(Command.class)).orElseThrow(() -> new CommandException("The target class (" + targetClass.getName() + ") is not a command."));

        final Method executorMethod = Arrays.stream(targetClass.getMethods()).filter((method -> method.isAnnotationPresent(Executor.class))).findAny().orElseThrow(() -> new CommandException("The target class (" + targetClass.getName() + ") does not have an Executor method."));
        final Optional<Method> suggesterMethod = Arrays.stream(targetClass.getMethods()).filter((method -> method.isAnnotationPresent(Suggester.class))).findAny();

        final Command.InitiatorType target = command.target();

        final org.bukkit.command.Command baseCommand = new org.bukkit.command.Command(command.name(), command.description(), command.usage(), List.of(command.aliases())) {
            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                final boolean allowPlayer = (target == Command.InitiatorType.PLAYER && sender instanceof Player) ,
                        allowConsole = (target == Command.InitiatorType.CONSOLE && sender instanceof ConsoleCommandSender),
                        allowBoth = (target == Command.InitiatorType.BOTH && ( (sender instanceof Player) || (sender instanceof ConsoleCommandSender) ));

                final MiniMessage miniMessage = MiniMessage.miniMessage();
                final Component formattedUsage = miniMessage.deserialize(getUsage());

                if(allowPlayer || allowConsole || allowBoth) {
                    if(args != null) {
                        final Argument argument = new Argument(args);
                        final Structure structure = new Structure(sender, argument);

                        if(executorMethod.getAnnotation(Executor.class).preventEmptyArgument() && args.length == 0) {
                            sender.sendMessage(formattedUsage);
                            return false;
                        }

                        try {

                            final Command.Status status = (Command.Status) executorMethod.invoke(commandObject, sender, structure);

                            if(status == Command.Status.FAILED) sender.sendMessage(formattedUsage);

                        } catch (Exception e) {
                            throw new CommandException("An error occurred when running the Executor method: " + e);
                        }
                        return true;
                    }
                }
                return false;
            }

            @Override
            public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                final Suggestion suggestion = new Suggestion(args);
                suggesterMethod.ifPresent(method -> {
                    try {
                        method.invoke(commandObject, sender, suggestion);
                    } catch (Exception e) {
                        throw new CommandException("An error occurred when running the Suggester method: " + e);
                    }
                });
                return suggestion.getCompletions();
            }
        };

        baseCommand.setPermission(command.permission());

        if(!baseCommand.isRegistered()) commandMap.register(plugin.getName(), baseCommand);
    }

    public void unregisterCommand(@NotNull final Object object) {
        final Class<?> targetClass = object.getClass();

        final Command command = Optional.of(targetClass.getAnnotation(Command.class)).orElseThrow(() -> new CommandException("The target class (" + targetClass.getName() + ") is not a command."));

        if(Objects.requireNonNull(commandMap.getCommand(command.name())).isRegistered()) Objects.requireNonNull(commandMap.getCommand(command.name())).unregister(commandMap);
    }
}