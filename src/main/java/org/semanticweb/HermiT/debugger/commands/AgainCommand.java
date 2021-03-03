package org.semanticweb.HermiT.debugger.commands;

import java.io.PrintWriter;
import org.semanticweb.HermiT.debugger.Debugger;

public class AgainCommand
extends AbstractCommand {
    public AgainCommand(Debugger debugger) {
        super(debugger);
    }

    @Override
    public String getCommandName() {
        return "a";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"", "executes the last command again"};
    }

    @Override
    public void printHelp(PrintWriter writer) {
        writer.println("usage: a");
        writer.println("    Executes the last command again.");
    }

    @Override
    public void execute(String[] args) {
        String commandLine = this.m_debugger.getLastCommand();
        if (commandLine != null) {
            this.m_debugger.getOutput().println("# " + commandLine);
            this.m_debugger.processCommandLine(commandLine);
        }
    }
}

