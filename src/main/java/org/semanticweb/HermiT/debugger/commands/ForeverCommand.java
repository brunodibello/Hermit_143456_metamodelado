package org.semanticweb.HermiT.debugger.commands;

import java.io.PrintWriter;
import org.semanticweb.HermiT.debugger.Debugger;

public class ForeverCommand
extends AbstractCommand {
    public ForeverCommand(Debugger debugger) {
        super(debugger);
    }

    @Override
    public String getCommandName() {
        return "forever";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"", "run and do not wait for further input"};
    }

    @Override
    public void printHelp(PrintWriter writer) {
        writer.println("usage: forever");
        writer.println("    Continues with the current reasoning task without");
        writer.println("    waiting for further input by the user.");
    }

    @Override
    public void execute(String[] args) {
        this.m_debugger.setInMainLoop(false);
        this.m_debugger.setForever(true);
        this.m_debugger.setSinglestep(false);
    }
}

