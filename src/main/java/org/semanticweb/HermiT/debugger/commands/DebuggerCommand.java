package org.semanticweb.HermiT.debugger.commands;

import java.io.PrintWriter;

public interface DebuggerCommand {
    public String getCommandName();

    public String[] getDescription();

    public void printHelp(PrintWriter var1);

    public void execute(String[] var1);
}

