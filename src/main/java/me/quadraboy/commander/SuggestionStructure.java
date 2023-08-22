package me.quadraboy.commander;

import me.quadraboy.commander.structure.Structure;
import me.quadraboy.commander.structure.handler.Suggestion;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class SuggestionStructure extends Structure {
    private final Suggestion suggestion;

    public SuggestionStructure(@NotNull final CommandSender commandSender, @NotNull final Suggestion suggestion) {
        super(commandSender);
        this.suggestion = suggestion;
    }

    public Suggestion getSuggestion() {
        return this.suggestion;
    }
}