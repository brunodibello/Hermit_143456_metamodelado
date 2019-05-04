/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.debugger.commands;

import java.io.PrintWriter;
import org.semanticweb.HermiT.debugger.Debugger;
import org.semanticweb.HermiT.debugger.commands.AbstractCommand;

public class ContinueCommand
extends AbstractCommand {
    public ContinueCommand(Debugger debugger) {
        super(debugger);
    }

    @Override
    public String getCommandName() {
        return "c";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"", "continues with the current reasoning tasks"};
    }

    @Override
    public void printHelp(PrintWriter writer) {
        writer.println("usage: c");
        writer.println("    Continues with the current reasoning tasks.");
    }

    @Override
    public void execute(String[] args) {
        this.m_debugger.setInMainLoop(false);
    }
}

