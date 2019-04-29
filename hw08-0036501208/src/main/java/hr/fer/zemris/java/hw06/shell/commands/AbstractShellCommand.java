package hr.fer.zemris.java.hw06.shell.commands;

import java.util.List;

/**
 * Models an abstract representation of a shell
 * command.
 *
 * @author Filip Nemec
 */
public abstract class AbstractShellCommand implements ShellCommand {
	
	/** This command's description derived as a list of {@code String} objects. */
	protected List<String> DESCRIPTION;
	
	/** This command's name. */
	protected String NAME;
	
	/**
	 * Constructs a new abstract shell command and
	 * forces its implementation to initialize.
	 */
	public AbstractShellCommand() {
		init();
	}
	
	protected abstract void init();
	
	@Override
	public final String getCommandName() {
		return NAME;
	}

	@Override
	public final List<String> getCommandDescription() {
		return DESCRIPTION;
	}
}
