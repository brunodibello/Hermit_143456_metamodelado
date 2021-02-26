package org.semanticweb.HermiT.debugger.commands;

import java.io.PrintWriter;
import org.semanticweb.HermiT.debugger.Debugger;

public class HistoryCommand
extends AbstractCommand {
    public HistoryCommand(Debugger debugger) {
        super(debugger);
    }

    @Override
    public String getCommandName() {
        return "history";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"on|off", "switch derivation history on/off"};
    }

    @Override
    public void printHelp(PrintWriter writer) {
        writer.println("usage: history on/off");
        writer.println("    Switches the derivation history on or off.");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            this.m_debugger.getOutput().println("The status is missing.");
            return;
        }
        String status = args[1].toLowerCase();
        if ("on".equals(status)) {
            this.m_debugger.setForwardingOn(true);
            this.m_debugger.getOutput().println("Derivation history on.");
        } else if ("off".equals(status)) {
            this.m_debugger.setForwardingOn(false);
            this.m_debugger.getOutput().println("Derivation history off.");
        } else {
            this.m_debugger.getOutput().println("Incorrect history status '" + status + "'.");
        }
    }
}

