/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.debugger.commands;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.io.Writer;
import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.debugger.Debugger;
import org.semanticweb.HermiT.debugger.Printing;
import org.semanticweb.HermiT.debugger.commands.AbstractCommand;
import org.semanticweb.HermiT.model.AtLeastConcept;
import org.semanticweb.HermiT.model.AtomicConcept;
import org.semanticweb.HermiT.model.ExistentialConcept;
import org.semanticweb.HermiT.model.LiteralConcept;
import org.semanticweb.HermiT.tableau.Node;

public class NodesForCommand
extends AbstractCommand {
    public NodesForCommand(Debugger debugger) {
        super(debugger);
    }

    @Override
    public String getCommandName() {
        return "nodesFor";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"conceptName", "prints nodes that have been created by (atleast n r.conceptName)"};
    }

    @Override
    public void printHelp(PrintWriter writer) {
        writer.println("usage: nodesFor conceptName");
        writer.println("    Prints all nodes that have been created by a concept (atleast n r.conceptName)");
        writer.println("    together with the information whether the nodes are active or not.");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            this.m_debugger.getOutput().println("Concept name is missing.");
            return;
        }
        String conceptName = args[1];
        CharArrayWriter buffer = new CharArrayWriter();
        PrintWriter writer = new PrintWriter(buffer);
        AtomicConcept atomicConcept = null;
        try {
            atomicConcept = AtomicConcept.create(this.m_debugger.getPrefixes().expandAbbreviatedIRI(conceptName));
            writer.println("Nodes for '" + conceptName + "'");
            writer.println("====================================================================");
            int index = 0;
            for (Node node = this.m_debugger.getTableau().getFirstTableauNode(); node != null; node = node.getNextTableauNode()) {
                Debugger.NodeCreationInfo nodeCreationInfo = this.m_debugger.getNodeCreationInfo(node);
                ExistentialConcept existentialConcept = nodeCreationInfo.m_createdByExistential;
                if (!(existentialConcept instanceof AtLeastConcept) || !((AtLeastConcept)existentialConcept).getToConcept().equals(atomicConcept)) continue;
                if (index != 0) {
                    writer.print(",");
                    if (index % 5 == 0) {
                        writer.println();
                    } else {
                        writer.print("  ");
                    }
                }
                Printing.printPadded(writer, node.getNodeID() + (node.isActive() ? "" : "*"), 8);
                ++index;
            }
            writer.println();
            writer.println("====================================================================");
        }
        catch (IllegalArgumentException e) {
            writer.println(conceptName + " is invalid: " + e.getMessage());
        }
        writer.flush();
        this.showTextInWindow(buffer.toString(), "Nodes for '" + conceptName + "'");
        this.selectConsoleWindow();
    }
}

