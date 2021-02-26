package org.semanticweb.HermiT.debugger.commands;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import org.semanticweb.HermiT.debugger.Debugger;
import org.semanticweb.HermiT.tableau.Node;

public class ModelStatsCommand
extends AbstractCommand {
    public ModelStatsCommand(Debugger debugger) {
        super(debugger);
    }

    @Override
    public String getCommandName() {
        return "modelStats";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"", "prints statistics about a model"};
    }

    @Override
    public void printHelp(PrintWriter writer) {
        writer.println("usage: modelStats");
        writer.println("    Prints statistics about the current model.");
    }

    @Override
    public void execute(String[] args) {
        int noNodes = 0;
        int noUnblockedNodes = 0;
        int noDirectlyBlockedNodes = 0;
        int noIndirectlyBlockedNodes = 0;
        for (Node node = this.m_debugger.getTableau().getFirstTableauNode(); node != null; node = node.getNextTableauNode()) {
            ++noNodes;
            if (node.isDirectlyBlocked()) {
                ++noDirectlyBlockedNodes;
                continue;
            }
            if (node.isIndirectlyBlocked()) {
                ++noIndirectlyBlockedNodes;
                continue;
            }
            ++noUnblockedNodes;
        }
        CharArrayWriter buffer = new CharArrayWriter();
        PrintWriter writer = new PrintWriter(buffer);
        writer.println("  Model statistics");
        writer.println("================================================");
        writer.println("  Number of nodes:                    " + noNodes);
        writer.println("  Number of unblocked nodes:          " + noUnblockedNodes);
        writer.println("  Number of directly blocked nodes:   " + noDirectlyBlockedNodes);
        writer.println("  Number of indirectly blocked nodes: " + noIndirectlyBlockedNodes);
        writer.println("================================================");
        writer.flush();
        this.showTextInWindow(buffer.toString(), "Model statistics");
        this.selectConsoleWindow();
    }
}

