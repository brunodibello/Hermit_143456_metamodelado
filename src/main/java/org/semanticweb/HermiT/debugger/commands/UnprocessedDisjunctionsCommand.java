package org.semanticweb.HermiT.debugger.commands;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import org.semanticweb.HermiT.debugger.Debugger;
import org.semanticweb.HermiT.model.DLPredicate;
import org.semanticweb.HermiT.model.Equality;
import org.semanticweb.HermiT.tableau.GroundDisjunction;

public class UnprocessedDisjunctionsCommand
extends AbstractCommand {
    public UnprocessedDisjunctionsCommand(Debugger debugger) {
        super(debugger);
    }

    @Override
    public String getCommandName() {
        return "uDisjunctions";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"", "shows unprocessed ground disjunctions"};
    }

    @Override
    public void printHelp(PrintWriter writer) {
        writer.println("usage: uDisjunctions");
        writer.println("    Prints a list of unprocessed ground disjunctions.");
    }

    @Override
    public void execute(String[] args) {
        CharArrayWriter buffer = new CharArrayWriter();
        PrintWriter writer = new PrintWriter(buffer);
        writer.println("Unprocessed ground disjunctions");
        writer.println("===========================================");
        for (GroundDisjunction groundDisjunction = this.m_debugger.getTableau().getFirstUnprocessedGroundDisjunction(); groundDisjunction != null; groundDisjunction = groundDisjunction.getPreviousGroundDisjunction()) {
            for (int disjunctIndex = 0; disjunctIndex < groundDisjunction.getNumberOfDisjuncts(); ++disjunctIndex) {
                DLPredicate dlPredicate;
                if (disjunctIndex != 0) {
                    writer.print(" v ");
                }
                if (Equality.INSTANCE.equals(dlPredicate = groundDisjunction.getDLPredicate(disjunctIndex))) {
                    writer.print(groundDisjunction.getArgument(disjunctIndex, 0).getNodeID());
                    writer.print(" == ");
                    writer.print(groundDisjunction.getArgument(disjunctIndex, 1).getNodeID());
                    continue;
                }
                writer.print(dlPredicate.toString(this.m_debugger.getPrefixes()));
                writer.print('(');
                for (int argumentIndex = 0; argumentIndex < dlPredicate.getArity(); ++argumentIndex) {
                    if (argumentIndex != 0) {
                        buffer.append(',');
                    }
                    writer.print(groundDisjunction.getArgument(disjunctIndex, argumentIndex).getNodeID());
                }
                writer.print(')');
            }
            writer.println();
        }
        writer.flush();
        this.showTextInWindow(buffer.toString(), "Unprocessed ground disjunctions");
        this.selectConsoleWindow();
    }
}

