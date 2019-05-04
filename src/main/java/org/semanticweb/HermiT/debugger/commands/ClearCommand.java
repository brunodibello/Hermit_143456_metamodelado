/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.debugger.commands;

import java.io.PrintWriter;
import org.semanticweb.HermiT.debugger.ConsoleTextArea;
import org.semanticweb.HermiT.debugger.Debugger;
import org.semanticweb.HermiT.debugger.commands.AbstractCommand;

public class ClearCommand
extends AbstractCommand {
    public ClearCommand(Debugger debugger) {
        super(debugger);
    }

    @Override
    public String getCommandName() {
        return "clear";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"", "clear the screen"};
    }

    @Override
    public void printHelp(PrintWriter writer) {
        writer.println("usage: clear");
        writer.println("    Clear the command line screen. ");
    }

    @Override
    public void execute(String[] args) {
        this.m_debugger.getConsoleTextArea().clear();
    }
}

