/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.debugger.commands;

import java.io.PrintWriter;
import org.semanticweb.HermiT.debugger.Debugger;
import org.semanticweb.HermiT.debugger.commands.AbstractCommand;

public class SingleStepCommand
extends AbstractCommand {
    public SingleStepCommand(Debugger debugger) {
        super(debugger);
    }

    @Override
    public String getCommandName() {
        return "singleStep";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"on|off", "step-by-step mode on or off"};
    }

    @Override
    public void printHelp(PrintWriter writer) {
        writer.println("usage: singleStep on|off");
        writer.println("    If on, the debugger will return control to the user after each step.");
        writer.println("    If off, the debugger will run until a breakpoint is reached.");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            this.m_debugger.getOutput().println("The status is missing.");
            return;
        }
        String status = args[1].toLowerCase();
        if ("on".equals(status)) {
            this.m_debugger.setSinglestep(true);
            this.m_debugger.getOutput().println("Single step mode on.");
        } else if ("off".equals(status)) {
            this.m_debugger.setSinglestep(false);
            this.m_debugger.getOutput().println("Single step mode off.");
        } else {
            this.m_debugger.getOutput().println("Incorrect single step mode '" + status + "'.");
        }
    }
}

