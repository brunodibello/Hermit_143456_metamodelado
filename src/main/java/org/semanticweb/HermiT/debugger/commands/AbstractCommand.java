package org.semanticweb.HermiT.debugger.commands;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import org.semanticweb.HermiT.debugger.Debugger;
import org.semanticweb.HermiT.model.AtomicConcept;
import org.semanticweb.HermiT.model.AtomicRole;
import org.semanticweb.HermiT.model.DLPredicate;
import org.semanticweb.HermiT.model.DescriptionGraph;
import org.semanticweb.HermiT.model.Equality;
import org.semanticweb.HermiT.model.Inequality;
import org.semanticweb.HermiT.tableau.Node;

public abstract class AbstractCommand
implements DebuggerCommand {
    protected final Debugger m_debugger;

    public AbstractCommand(Debugger debugger) {
        this.m_debugger = debugger;
    }

    protected void showTextInWindow(String string, String title) {
        JTextArea textArea = new JTextArea(string);
        textArea.setFont(Debugger.s_monospacedFont);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(2);
        frame.setContentPane(scrollPane);
        frame.pack();
        frame.setLocation(100, 100);
        frame.setVisible(true);
    }

    protected void selectConsoleWindow() {
        SwingUtilities.invokeLater(new Runnable(){

            @Override
            public void run() {
                if (AbstractCommand.this.m_debugger != null) {
                    AbstractCommand.this.m_debugger.getMainFrame().toFront();
                }
            }
        });
    }

    protected DLPredicate getDLPredicate(String predicate) {
        if ("==".equals(predicate)) {
            return Equality.INSTANCE;
        }
        if ("!=".equals(predicate)) {
            return Inequality.INSTANCE;
        }
        if (predicate.startsWith("+")) {
            return AtomicConcept.create(this.m_debugger.getPrefixes().expandAbbreviatedIRI(predicate.substring(1)));
        }
        if (predicate.startsWith("-")) {
            return AtomicRole.create(this.m_debugger.getPrefixes().expandAbbreviatedIRI(predicate.substring(1)));
        }
        if (predicate.startsWith("$")) {
            String graphName = this.m_debugger.getPrefixes().expandAbbreviatedIRI(predicate.substring(1));
            for (DescriptionGraph descriptionGraph : this.m_debugger.getTableau().getPermanentDLOntology().getAllDescriptionGraphs()) {
                if (!graphName.equals(descriptionGraph.getName())) continue;
                return descriptionGraph;
            }
            return null;
        }
        return null;
    }

    protected static String formatBlockingStatus(Node node) {
        if (!node.isBlocked()) {
            return "no";
        }
        if (node.isDirectlyBlocked()) {
            return "directly by " + (node.getBlocker() == Node.SIGNATURE_CACHE_BLOCKER ? "signature in cache" : Integer.valueOf(node.getBlocker().getNodeID()));
        }
        return "indirectly by " + (node.getBlocker() == Node.SIGNATURE_CACHE_BLOCKER ? "signature in cache" : Integer.valueOf(node.getBlocker().getNodeID()));
    }

}

