package us.gantzfamily.dlv.cli;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.JCommander.Builder;
import com.gantzgulch.logging.core.GGLogger;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import us.gantzfamily.dlv.cli.cmd.GlobalParameters;
import us.gantzfamily.dlv.cli.cmd.help.UsageCommand;
import us.gantzfamily.dlv.common.JavaPackageInfo;
import us.gantzfamily.dlv.common.io.DlvConsole;
import us.gantzfamily.dlv.core.CoreModule;

public class Main {

    private static final GGLogger LOG = GGLogger.getLogger(Main.class);

    private final GlobalParameters globalParameters;
    
    private final CommandDispatcher commandDispatcher;

    private String[] args;
    
    private JCommander jc;
    
    private UsageCommand usageCommand;


    @Inject
    public Main(//
            final GlobalParameters globalParameters, //
            final CommandDispatcher commandDispatcher) {
        
        this.globalParameters = globalParameters;
        this.commandDispatcher = commandDispatcher;
        
    }
    
    public int execute(final String[] args) {

        this.args = args;
        
        this.jc = buildJCommander();
    
        this.usageCommand = new UsageCommand(this.jc);
        
        commandDispatcher.addCommand(this.usageCommand);
        
        return run();
    }
    
    private JCommander buildJCommander() {

        final Builder builder = JCommander.newBuilder();
        
        builder.programName("dlv");
        builder.addObject(globalParameters);
        
        commandDispatcher.forEach( (cn,c) -> {
            builder.addCommand(cn, c.getParameters());
        });
        
        return builder.build();
    }
    
    
    public int run() {
        
        try {
            
            final String command = parseParameters();
    
            executeCommand(command);
            
            return 0;
            
        }catch(final RuntimeException e) {

            System.err.println("Exeception occured...");
            
            e.printStackTrace();
            
            LOG.fatal(e, "Execution occured.");
        }
        
        return 1;
    }
    
    private String parseParameters() {

        try {

            jc.parse(this.args);

            if (globalParameters.isHelp()) {
                return "help";
            }

            final String parsedCommand = jc.getParsedCommand();
            
            return parsedCommand != null ? parsedCommand : "help";

        } catch (final RuntimeException e) {

            e.printStackTrace();

            System.out.println();
            System.out.println();

            jc.usage();

            System.exit(-1);
        }

        return null;
    }

    private void executeCommand(final String command) {

        if( globalParameters.isTrace() ) {
            DlvConsole.INSTANCE.setLevel(DlvConsole.LEVEL_TRACE);
        } else if( globalParameters.isVerbose() ) {
            DlvConsole.INSTANCE.setLevel(DlvConsole.LEVEL_DEBUG);
        }
        
        if( commandDispatcher.isHelp(command) ) {
            
            jc.getCommands().get(command).usage();
            
            return;
            
        }
        
    	commandDispatcher.executeCommand(command);
        
        LOG.info("SUCCESS!");
    }

    private static void banner() {
        
        final JavaPackageInfo pi = new JavaPackageInfo(Main.class);
        
        LOG.info("+--");
        LOG.info("|  ");
        LOG.info("|  Title .......................: %s", pi.getTitle());
        LOG.info("|  Version .....................: %s", pi.getVersion());
        LOG.info("|  Vendor ......................: %s", pi.getVendor());
        LOG.info("|  ");
        LOG.info("+--");
        
        DlvConsole.INSTANCE.info("%s %s", "PodLogViewer", pi.getVersion());
    }
    

	public static void main(final String[] args) {

	    ToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);

	    Main.banner();
	    
	    final Injector injector = Guice.createInjector(new CliModule(), new CoreModule());
	    
	    final Main main = injector.getInstance(Main.class);

	    int returnCode = main.execute(args);

	    System.exit(returnCode);
	}

}

