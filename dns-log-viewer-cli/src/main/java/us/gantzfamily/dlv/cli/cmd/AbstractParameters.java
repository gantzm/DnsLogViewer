package us.gantzfamily.dlv.cli.cmd;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.beust.jcommander.Parameter;

public class AbstractParameters implements Parameters{

    @Parameter(names = { "--help", "-h" }, description = "Display usage information.", help = true)
    private boolean help;

    public boolean isHelp() {
        return help;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
