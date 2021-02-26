package org.semanticweb.HermiT.debugger.commands;

import java.io.PrintWriter;
import org.semanticweb.HermiT.debugger.Debugger;

public class ExitCommand
extends AbstractCommand {
    public ExitCommand(Debugger debugger) {
        super(debugger);
    }

    @Override
    public String getCommandName() {
        return "exit";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"", "exits the curtrent process"};
    }

    @Override
    public void printHelp(PrintWriter writer) {
        writer.println("usage: exit");
        writer.println("    Exits the current process.");
    }

    @Override
    public void execute(String[] args) {
        System.exit(0);
    }
}

