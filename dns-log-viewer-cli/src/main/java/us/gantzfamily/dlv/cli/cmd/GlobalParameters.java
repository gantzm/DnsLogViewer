package us.gantzfamily.dlv.cli.cmd;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "Global options.")
public class GlobalParameters extends AbstractParameters {

    @Parameter(names = { "--verbose" }, required = false, description = "Produce verbose output.", order = 5)
    private boolean verbose = false;
    
    @Parameter(names = { "--trace" }, required = false, description = "Produce trace output.", order = 6)
    private boolean trace = false;
    
    public boolean isVerbose() {
        return verbose;
    }

    public boolean isTrace() {
        return trace;
    }
}
