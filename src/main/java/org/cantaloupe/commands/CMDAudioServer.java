package org.cantaloupe.commands;

import java.util.Optional;

import org.cantaloupe.Cantaloupe;
import org.cantaloupe.audio.AudioWrapper;
import org.cantaloupe.audio.sources.PositionedSource;
import org.cantaloupe.command.CommandResult;
import org.cantaloupe.command.CommandSource;
import org.cantaloupe.command.CommandSpec;
import org.cantaloupe.command.ICommandExecutor;
import org.cantaloupe.command.args.CommandContext;
import org.cantaloupe.command.args.GeneralArguments;
import org.cantaloupe.player.Player;
import org.cantaloupe.text.Text;
import org.cantaloupe.text.action.TextActions;

public class CMDAudioServer {
    public static CommandSpec create() {
        return CommandSpec.builder().arguments(GeneralArguments.string("action")).executor(new ICommandExecutor() {
            @Override
            public CommandResult execute(CommandSource src, CommandContext args) {
                if (src.isPlayer()) {
                    Optional<Player> playerOpt = Cantaloupe.getPlayerManager().getPlayerFromCommandSource(src);

                    if (playerOpt.isPresent()) {
                        Player player = playerOpt.get();
                        Optional<AudioWrapper> wrapperOpt = player.getWrapper(AudioWrapper.class);

                        if (wrapperOpt.isPresent()) {
                            AudioWrapper wrapper = wrapperOpt.get();
                            Optional<String> actionOpt = args.getOne("action");

                            if (actionOpt.isPresent()) {
                                if (actionOpt.get().equalsIgnoreCase("connect")) {
                                    player.sendMessage(Text.of("Click here to connect.").onClick(TextActions.openUrl(wrapper.generateConnectURL("http://localhost/Cantaloupe/AudioClient/index.html"))));

                                    return CommandResult.SUCCESS;
                                } else if (actionOpt.get().equalsIgnoreCase("test")) {
                                    PositionedSource source = (PositionedSource) Cantaloupe.getAudioServer().get().getSourceManager().getSource("test").get();
                                    source.start();
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