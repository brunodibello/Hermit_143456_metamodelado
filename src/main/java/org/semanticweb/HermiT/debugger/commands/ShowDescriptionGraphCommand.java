/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.debugger.commands;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Set;
import org.semanticweb.HermiT.debugger.Debugger;
import org.semanticweb.HermiT.debugger.commands.AbstractCommand;
import org.semanticweb.HermiT.model.DLOntology;
import org.semanticweb.HermiT.model.DescriptionGraph;
import org.semanticweb.HermiT.tableau.Tableau;

public class ShowDescriptionGraphCommand
extends AbstractCommand {
    public ShowDescriptionGraphCommand(Debugger debugger) {
        super(debugger);
    }

    @Override
    public String getCommandName() {
        return "showDGraph";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"graphName", "prints a text representation of the description graph graphName"};
    }

    @Override
    public void printHelp(PrintWriter writer) {
        writer.println("usage: showDGraph graphName");
        writer.println("    Prints information about the description graph with the given name.");
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            this.m_debugger.getOutput().println("Graph name is missing.");
            return;
        }
        String graphName = args[1];
        for (DescriptionGraph descriptionGraph : this.m_debugger.getTableau().getPermanentDLOntology().getAllDescriptionGraphs()) {
            if (!descriptionGraph.getName().equals(graphName)) continue;
            CharArrayWriter buffer = new CharArrayWriter();
            PrintWriter writer = new PrintWriter(buffer);
            writer.println("===========================================");
            writer.println("    Contents of the graph '" + descriptionGraph.getName() + "'");
            writer.println("===========================================");
            writer.println(descriptionGraph.getTextRepresentation());
            writer.flush();
            this.showTextInWindow(buffer.toString(), "Contents of the graph '" + descriptionGraph.getName() + "'");
            this.selectConsoleWindow();
            return;
        }
        this.m_debugger.getOutput().println("Graph '" + graphName + "' not found.");
    }
}

