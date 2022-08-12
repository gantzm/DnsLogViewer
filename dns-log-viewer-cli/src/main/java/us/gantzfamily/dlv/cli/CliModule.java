package us.gantzfamily.dlv.cli;


import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.Multibinder;

import us.gantzfamily.dlv.cli.cmd.Command;
import us.gantzfamily.dlv.cli.cmd.GlobalParameters;
import us.gantzfamily.dlv.cli.cmd.Parameters;
import us.gantzfamily.dlv.cli.cmd.help.AboutCommand;
import us.gantzfamily.dlv.cli.cmd.pod.QueryReportCommand;

public class CliModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(Main.class).in(Singleton.class);
        bind(GlobalParameters.class).in(Singleton.class);
        bind(CommandDispatcher.class).in(Singleton.class);

        Multibinder<Command<?>> cmdBinder = Multibinder.newSetBinder(binder(), new CommandLiteral());

        cmdBinder.addBinding().to(QueryReportCommand.class).in(Singleton.class);
        
        cmdBinder.addBinding().to(AboutCommand.class).in(Singleton.class);
        
    }
    
    public static class CommandLiteral extends TypeLiteral<Command<? extends Parameters>> {
        
    }
}
