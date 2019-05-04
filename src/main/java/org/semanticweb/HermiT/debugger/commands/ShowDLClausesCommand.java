/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.debugger.commands;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Set;
import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.debugger.Debugger;
import org.semanticweb.HermiT.debugger.commands.AbstractCommand;
import org.semanticweb.HermiT.model.DLClause;
import org.semanticweb.HermiT.model.DLOntology;
import org.semanticweb.HermiT.tableau.Tableau;

public class ShowDLClausesCommand
extends AbstractCommand {
    public ShowDLClausesCommand(Debugger debugger) {
        super(debugger);
    }

    @Override
    public String getCommandName() {
        return "showDLClauses";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"", "prints the currently used set of DL-clauses"};
    }

    @Override
    public void printHelp(PrintWriter writer) {
        writer.println("usage: showDLClauses");
        writer.println("    Prints the currently used set of DL-clauses.");
    }

    @Override
    public void execute(String[] args) {
        CharArrayWriter buffer = new CharArrayWriter();
        PrintWriter writer = new PrintWriter(buffer);
        if (!this.m_debugger.getTableau().getPermanentDLOntology().getDLClauses().isEmpty()) {
            writer.println("-----------------------------------------------");
            writer.println("Permanent DL-clauses:");
            writer.println("-----------------------------------------------");
            for (DLClause dlClause : this.m_debugger.getTableau().getPermanentDLOntology().getDLClauses()) {
                writer.println(dlClause.toString(this.m_debugger.getPrefixes()));
            }
        }
        if (this.m_debugger.getTableau().getAdditionalDLOntology() != null && !this.m_debugger.getTableau().getAdditionalDLOntology().getDLClauses().isEmpty()) {
            writer.println("-----------------------------------------------");
            writer.println("Additional DL-clauses:");
            writer.println("-----------------------------------------------");
            for (DLClause dlClause : this.m_debugger.getTableau().getAdditionalDLOntology().getDLClauses()) {
                writer.println(dlClause.toString(this.m_debugger.getPrefixes()));
            }
        }
        writer.flush();
        this.showTextInWindow(buffer.toString(), "DL-clauses");
        this.selectConsoleWindow();
    }
}

