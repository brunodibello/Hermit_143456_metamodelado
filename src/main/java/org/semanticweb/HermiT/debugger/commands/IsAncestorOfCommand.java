/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.debugger.commands;

import java.io.PrintWriter;
import org.semanticweb.HermiT.debugger.Debugger;
import org.semanticweb.HermiT.debugger.commands.AbstractCommand;
import org.semanticweb.HermiT.tableau.Node;
import org.semanticweb.HermiT.tableau.Tableau;

public class IsAncestorOfCommand
extends AbstractCommand {
    public IsAncestorOfCommand(Debugger debugger) {
        super(debugger);
    }

    @Override
    public String getCommandName() {
        return "isAncOf";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"nodeID1 nodeID2", "tests whether nodeID1 is an ancestor of nodeID2"};
    }

    @Override
    public void printHelp(PrintWriter writer) {
        writer.println("usage: isAncOf nodeID1 nodeID2");
        writer.println("    Prints whether the node for nodeID1 is an ancestor of the node for nodeID2.");
    }

    @Override
    public void execute(String[] args) {
        int nodeID2;
        boolean result;
        int nodeID1;
        if (args.length < 3) {
            this.m_debugger.getOutput().println("Node IDs are missing.");
            return;
        }
        try {
            nodeID1 = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e) {
            this.m_debugger.getOutput().println("Invalid ID of the first node. " + e.getMessage());
            return;
        }
        try {
            nodeID2 = Integer.parseInt(args[2]);
        }
        catch (NumberFormatException e) {
            this.m_debugger.getOutput().println("Invalid ID of the second node. " + e.getMessage());
            return;
        }
        Node node1 = this.m_debugger.getTableau().getNode(nodeID1);
        Node node2 = this.m_debugger.getTableau().getNode(nodeID2);
        if (node1 == null) {
            this.m_debugger.getOutput().println("Node with ID '" + nodeID1 + "' not found.");
            return;
        }
        if (node2 == null) {
            this.m_debugger.getOutput().println("Node with ID '" + nodeID2 + "' not found.");
            return;
        }
        this.m_debugger.getOutput().print("Node " + node1.getNodeID() + " is " + ((result = node1.isAncestorOf(node2)) ? "" : "not ") + "an ancestor of node " + node2.getNodeID() + ".");
    }
}

