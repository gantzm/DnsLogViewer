package us.gantzfamily.dlv.cli;

import static com.gantzgulch.tools.common.lang.GGBiConsumer.mapConsumer;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import com.gantzgulch.logging.core.GGLogger;
import com.gantzgulch.tools.common.lang.GGMaps;
import com.google.inject.Inject;

import us.gantzfamily.dlv.cli.cmd.Command;
import us.gantzfamily.dlv.cli.cmd.Parameters;

public class CommandDispatcher {

    private static final GGLogger LOG = GGLogger.getLogger(CommandDispatcher.class);

    private final Map<String, Command<? extends Parameters>> commandMap = new HashMap<>();

    @Inject
    public CommandDispatcher(final Set<Command<? extends Parameters>> commands) {
        commands.forEach(this::addCommand);
    }

    public void addCommand(final Command<? extends Parameters> command) {

        LOG.info("addCommand: Adding command: %s", command.getCommandName());

        commandMap.put(command.getCommandName(), command);
    }

    public void forEach(final BiConsumer<String, Command<? extends Parameters>> consumer) {

        commandMap //
                .entrySet() //
                 .stream() //
                 .sorted(GGMaps.mapEntryKeyComparator()) //
                .forEach(mapConsumer(consumer));

    }
    
    public boolean isHelp(final String command) {
        
        return commandMap.get(command).getParameters().isHelp();
        
    }

    public void executeCommand(final String command) {

        LOG.info("Executing command: %s", command);

        commandMap//
                .get(command) //
                .run();

    }
}
