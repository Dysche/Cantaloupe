package org.cantaloupe.commands;

import java.util.Optional;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.audio.AudioWrapper;
import org.cantaloupe.command.CommandExecutor;
import org.cantaloupe.command.CommandResult;
import org.cantaloupe.command.CommandSource;
import org.cantaloupe.command.CommandSpec;
import org.cantaloupe.command.args.CommandContext;
import org.cantaloupe.command.args.GeneralArguments;
import org.cantaloupe.player.Player;

public class CMDAudioServer {
    public static CommandSpec create() {
        return CommandSpec.builder().arguments(GeneralArguments.string("action")).executor(new CommandExecutor() {
            @Override
            public CommandResult execute(CommandSource src, CommandContext args) {
                if (src.isPlayer()) {
                    Optional<Player> playerOpt = Cantaloupe.getPlayerManager().getPlayerFromCommandSource(src);

                    if (playerOpt.isPresent()) {
                        Player player = playerOpt.get();
                        AudioWrapper wrapper = player.getWrapper(AudioWrapper.class);

                        if (wrapper != null) {
                            Optional<String> actionOpt = args.getOne("action");

                            if (actionOpt.isPresent()) {
                                if (actionOpt.get().equalsIgnoreCase("connect")) {
                                    wrapper.connect("http://localhost/Cantaloupe/AudioClient/index.html");

                                    return CommandResult.SUCCESS;
                                }
                            }
                        }
                    }
                }

                return CommandResult.FAILURE;
            }
        }).build();
    }
}