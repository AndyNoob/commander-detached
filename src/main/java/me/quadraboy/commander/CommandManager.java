package me.quadraboy.commander;

import me.quadraboy.commander.annotations.Command;
import me.quadraboy.commander.annotations.Executor;
import me.quadraboy.commander.annotations.Suggester;
import me.quadraboy.commander.structure.Flow;
import me.quadraboy.commander.structure.handlers.SuggestionHandler;
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
import java.util.*;

public class CommandManager {
    private final Plugin plugin;
    private final CommandMap commandMap = Bukkit.getServer().getCommandMap();

    public CommandManager(@NotNull final Plugin plugin) {
        this.plugin = plugin;
    }

    public void registerCommand(@NotNull final Object object) {
        final Class<?> targetClass = object.getClass();

        if (!targetClass.isAnnotationPresent(Command.class))
            throw new CommandException("The target class (" + targetClass.getName() + ") is not a command.");

        final Command command = targetClass.getAnnotation(Command.class);

        final Method executorMethod = Arrays.stream(targetClass.getMethods()).filter(method -> method.isAnnotationPresent(Executor.class)).findAny().orElseThrow(() -> new CommandException("The target class (" + targetClass.getName() + ") does not have a Executor method."));
        final Optional<Method> suggesterMethod = Arrays.stream(targetClass.getMethods()).filter(method -> method.isAnnotationPresent(Suggester.class)).findAny();

        final String name = command.name(), description = command.description(), usage = command.usage(), permission = command.permission();

        final List<String> aliases = Arrays.asList(command.aliases());

        final Command.InitiatorType target = command.target();

        final boolean preventEmptyArgument = executorMethod.getAnnotation(Executor.class).preventEmptyArgument();

        final org.bukkit.command.Command baseCommand = new org.bukkit.command.Command(name, description, usage, aliases) {

            @Override
            public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
                final boolean allowPlayer = (target == Command.InitiatorType.PLAYER && sender instanceof Player) ,
                              allowConsole = (target == Command.InitiatorType.CONSOLE && sender instanceof ConsoleCommandSender),
                              allowBoth = (target == Command.InitiatorType.BOTH && ( (sender instanceof Player) || (sender instanceof ConsoleCommandSender) ));

                final Component formattedUsage = MiniMessage.miniMessage().deserialize(getUsage());
                final Flow flow = new Flow(sender, args);

                if(allowPlayer || allowConsole || allowBoth) {
                    if(args != null) {

                        if(preventEmptyArgument && args.length == 0) {
                            sender.sendMessage(formattedUsage);
                            return false;
                        }

                        try {
                            final Command.Status status = (Command.Status) executorMethod.invoke(object, sender, flow);

                            if(status == Command.Status.FAILED) sender.sendMessage(formattedUsage);
                        } catch (Exception e) {
                            throw new CommandException("An error occurred when running the Executor method: " + e);
                        }
                        return true;
                    }
                } else {
                    sender.sendMessage(flow.notAllowedMessage());
                }
                return false;
            }

            @Override
            public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                final SuggestionHandler suggestionHandler = new SuggestionHandler(args);

                suggesterMethod.ifPresent(method -> {
                    try {
                        method.invoke(object, sender, suggestionHandler);
                    } catch (Exception e) {
                        throw new CommandException("An error occurred when running the Suggester method: " + e);
                    }

                    if(method.getAnnotation(Suggester.class).sorted()) Collections.sort(suggestionHandler.getCompletions());
                });

                return suggestionHandler.getCompletions();
            }
        };

        baseCommand.setPermission(permission);

        if(!baseCommand.isRegistered()) commandMap.register(plugin.getName(), baseCommand);
    }

    public void unregisterCommand(@NotNull final Object object) {
        final Class<?> targetClass = object.getClass();

        if (!targetClass.isAnnotationPresent(Command.class)) throw new CommandException("The target class (" + targetClass.getName() + ") is not a command.");

        final Command command = targetClass.getAnnotation(Command.class);

        if(Objects.requireNonNull(commandMap.getCommand(command.name())).isRegistered()) Objects.requireNonNull(commandMap.getCommand(command.name())).unregister(commandMap);
    }
}