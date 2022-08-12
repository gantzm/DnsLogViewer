package us.gantzfamily.dlv.cli.cmd;

public interface Command<T extends Parameters> {

    String getCommandName();

    T getParameters();

    int run();
}
