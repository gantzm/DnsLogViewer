package us.gantzfamily.dlv.cli.cmd;

import com.gantzgulch.logging.core.GGLogger;

import us.gantzfamily.dlv.common.io.DlvConsole;

public abstract class AbstractCommand<T extends AbstractParameters> implements Command<T> {

	protected final GGLogger LOG = GGLogger.getLogger(getClass());

	protected final DlvConsole con = DlvConsole.INSTANCE;
	
	private final String commandName;
	
	protected final T params;

	public AbstractCommand(final String commandName, final T params) {
		this.commandName = commandName;
		this.params = params;
	}

	public String getCommandName() {
		return commandName;
	}
	
	public T getParameters() {
		return params;
	}

}
