package us.gantzfamily.dlv.cli.cmd.help;

import com.beust.jcommander.JCommander;

import us.gantzfamily.dlv.cli.cmd.AbstractCommand;

public class UsageCommand extends AbstractCommand<UsageParameters> {

    private final JCommander jc;

    public UsageCommand(final JCommander jc) {
        super("help", new UsageParameters());
        this.jc = jc;
    }
    
    @Override
    public int run() {
        
        jc.usage();

        return 0;
    }
}
