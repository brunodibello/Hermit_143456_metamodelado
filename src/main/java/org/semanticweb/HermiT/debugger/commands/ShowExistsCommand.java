/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.debugger.commands;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collection;
import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.debugger.Debugger;
import org.semanticweb.HermiT.debugger.Printing;
import org.semanticweb.HermiT.debugger.commands.AbstractCommand;
import org.semanticweb.HermiT.model.ExistentialConcept;
import org.semanticweb.HermiT.tableau.Node;

public class ShowExistsCommand
extends AbstractCommand {
    public ShowExistsCommand(Debugger debugger) {
        super(debugger);
    }

    @Override
    public String getCommandName() {
        return "showExists";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"", "prints nodes with unprocessed existentials"};
    }

    @Override
    public void printHelp(PrintWriter writer) {
        writer.println("usage: showExists");
        writer.println("    Prints a list of nodes that have unprocessed existentials, together with information that generated these nodes.");
    }

    @Override
    public void execute(String[] args) {
        CharArrayWriter buffer = new CharArrayWriter();
        PrintWriter writer = new PrintWriter(buffer);
        writer.println("Nodes with existentials");
        writer.println("================================================================================");
        writer.println("      ID    # Existentials    Start Existential");
        writer.println("================================================================================");
        for (Node node = this.m_debugger.getTableau().getFirstTableauNode(); node != null; node = node.getNextTableauNode()) {
            if (!node.isActive() || node.isBlocked() || !node.hasUnprocessedExistentials()) continue;
            writer.print("  ");
            Printing.printPadded(writer, node.getNodeID(), 6);
            writer.print("      ");
            Printing.printPadded(writer, node.getUnprocessedExistentials().size(), 6);
            writer.print("        ");
            this.printStartExistential(node, writer);
            writer.println();
        }
        writer.println("===========================================");
        writer.flush();
        this.showTextInWindow(buffer.toString(), "Nodes with existentials");
        this.selectConsoleWindow();
    }

    protected void printStartExistential(Node node, PrintWriter writer) {
        Debugger.NodeCreationInfo nodeCreationInfo = this.m_debugger.getNodeCreationInfo(node);
        ExistentialConcept startExistential = nodeCreationInfo.m_createdByExistential;
        if (startExistential == null) {
            writer.print("(root)");
        } else {
            writer.print(startExistential.toString(this.m_debugger.getPrefixes()));
        }
    }
}

