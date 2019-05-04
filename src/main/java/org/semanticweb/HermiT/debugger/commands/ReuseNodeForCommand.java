/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.debugger.commands;

import java.io.PrintWriter;
import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.debugger.Debugger;
import org.semanticweb.HermiT.debugger.commands.AbstractCommand;
import org.semanticweb.HermiT.existentials.ExistentialExpansionStrategy;
import org.semanticweb.HermiT.existentials.IndividualReuseStrategy;
import org.semanticweb.HermiT.model.AtomicConcept;
import org.semanticweb.HermiT.tableau.Node;
import org.semanticweb.HermiT.tableau.Tableau;

public class ReuseNodeForCommand
extends AbstractCommand {
    public ReuseNodeForCommand(Debugger debugger) {
        super(debugger);
    }

    @Override
    public String getCommandName() {
        return "reuseNodeFor";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"nodeID", "prints concepts for which the given node is a reuse node under individual reuse strategy"};
    }

    @Override
    public void printHelp(PrintWriter writer) {
        writer.println("usage: reuseNodeFor nodeID");
        writer.println("    If individual reuse strategy is used, prints the concepts for which the given node is a reuse node.");
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
            this.m_debugger.getOutput().println("Invalid ID of the node. " + e.getMessage());
            return;
        }
        Node node = this.m_debugger.getTableau().getNode(nodeID);
        if (node == null) {
            this.m_debugger.getOutput().println("Node with ID '" + nodeID + "' not found.");
            return;
        }
        ExistentialExpansionStrategy strategy = this.m_debugger.getTableau().getExistentialsExpansionStrategy();
        if (strategy instanceof IndividualReuseStrategy) {
            IndividualReuseStrategy reuseStrategy = (IndividualReuseStrategy)strategy;
            AtomicConcept conceptForNode = reuseStrategy.getConceptForNode(node);
            this.m_debugger.getOutput().print("Node '");
            this.m_debugger.getOutput().print(node.getNodeID());
            this.m_debugger.getOutput().print("' is ");
            if (conceptForNode == null) {
                this.m_debugger.getOutput().println("not a reuse node for any concept.");
            } else {
                this.m_debugger.getOutput().print("a reuse node for the '");
                this.m_debugger.getOutput().print(conceptForNode.toString(this.m_debugger.getPrefixes()));
                this.m_debugger.getOutput().println("' concept.");
            }
        } else {
            this.m_debugger.getOutput().println("Node reuse strategy is not currently in effect.");
        }
    }
}

