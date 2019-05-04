/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.debugger.commands;

import java.io.PrintWriter;
import org.semanticweb.HermiT.debugger.Debugger;
import org.semanticweb.HermiT.debugger.commands.AbstractCommand;
import org.semanticweb.HermiT.debugger.commands.SubtreeViewer;
import org.semanticweb.HermiT.tableau.Node;
import org.semanticweb.HermiT.tableau.Tableau;

public class ShowSubtreeCommand
extends AbstractCommand {
    public ShowSubtreeCommand(Debugger debugger) {
        super(debugger);
    }

    @Override
    public String getCommandName() {
        return "showSubtree";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"nodeID", "shows the subtree rooted at nodeID"};
    }

    @Override
    public void printHelp(PrintWriter writer) {
        writer.println("usage: showSubtree nodeID");
        writer.println("    Shows the subtree of the model rooted at the given node.");
        writer.println("    black: root node");
        writer.println("    darkgrey: named node");
        writer.println("    green: blockable node (not blocked)");
        writer.println("    light gray: inactive node");
        writer.println("    cyan: blocked node");
        writer.println("    red: node with unprocessed existentials");
        writer.println("    magenta: description graph node");
        writer.println("    blue: concrete/data value node");
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
        Node subtreeRoot = this.m_debugger.getTableau().getNode(nodeID);
        if (subtreeRoot == null) {
            this.m_debugger.getOutput().println("Node with ID '" + nodeID + "' not found.");
            return;
        }
        new SubtreeViewer(this.m_debugger, subtreeRoot);
    }
}

