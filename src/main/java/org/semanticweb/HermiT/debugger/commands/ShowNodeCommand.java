/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.debugger.commands;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.io.Writer;
import org.semanticweb.HermiT.debugger.Debugger;
import org.semanticweb.HermiT.debugger.Printing;
import org.semanticweb.HermiT.debugger.commands.AbstractCommand;
import org.semanticweb.HermiT.tableau.Node;
import org.semanticweb.HermiT.tableau.Tableau;

public class ShowNodeCommand
extends AbstractCommand {
    public ShowNodeCommand(Debugger debugger) {
        super(debugger);
    }

    @Override
    public String getCommandName() {
        return "showNode";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"nodeID", "prints information about the given node"};
    }

    @Override
    public void printHelp(PrintWriter writer) {
        writer.println("usage: showNode nodeID");
        writer.println("    Prints information about the node for the given node ID.");
    }

    @Override
    public void execute(String[] args) {
        int nodeID;
        if (args.length < 2) {
            this.m_debugger.getOutput().println("Node ID is missing.");
            return;
        }
        try {
            nodeID = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e) {
            this.m_debugger.getOutput().println("Invalid ID of the first node. " + e.getMessage());
            return;
        }
        Node node = this.m_debugger.getTableau().getNode(nodeID);
        if (node == null) {
            this.m_debugger.getOutput().println("Node with ID '" + nodeID + "' not found.");
            return;
        }
        CharArrayWriter buffer = new CharArrayWriter();
        PrintWriter writer = new PrintWriter(buffer);
        Printing.printNodeData(this.m_debugger, node, writer);
        writer.flush();
        this.showTextInWindow(buffer.toString(), "Node '" + node.getNodeID() + "'");
        this.selectConsoleWindow();
    }
}

