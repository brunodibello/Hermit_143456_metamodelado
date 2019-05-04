/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.debugger.commands;

import java.io.PrintWriter;
import org.semanticweb.HermiT.debugger.Debugger;
import org.semanticweb.HermiT.debugger.commands.AbstractCommand;

public class BreakpointTimeCommand
extends AbstractCommand {
    public BreakpointTimeCommand(Debugger debugger) {
        super(debugger);
    }

    @Override
    public String getCommandName() {
        return "bpTime";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"timeInSeconds", "sets the break point time"};
    }

    @Override
    public void printHelp(PrintWriter writer) {
        writer.println("usage: bpTime timeInSeconds");
        writer.println("    Sets the breakpoint time -- that is, after timeInSeconds,");
        writer.println("    the debugger will return control to the user.");
    }

    @Override
    public void execute(String[] args) {
        int breakpointTimeSeconds;
        if (args.length < 2) {
            this.m_debugger.getOutput().println("Time is missing.");
            return;
        }
        try {
            breakpointTimeSeconds = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e) {
            this.m_debugger.getOutput().println("Invalid time. " + e.getMessage());
            return;
        }
        this.m_debugger.getOutput().println("Breakpoint time is " + breakpointTimeSeconds + " seconds.");
        this.m_debugger.setBreakpointTime(breakpointTimeSeconds * 1000);
    }
}

