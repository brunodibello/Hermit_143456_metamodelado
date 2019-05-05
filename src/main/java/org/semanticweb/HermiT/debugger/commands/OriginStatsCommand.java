/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.debugger.commands;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.debugger.Debugger;
import org.semanticweb.HermiT.debugger.Printing;
import org.semanticweb.HermiT.debugger.commands.AbstractCommand;
import org.semanticweb.HermiT.model.AtLeastConcept;
import org.semanticweb.HermiT.model.Concept;
import org.semanticweb.HermiT.model.ExistentialConcept;
import org.semanticweb.HermiT.model.LiteralConcept;
import org.semanticweb.HermiT.tableau.Node;

public class OriginStatsCommand
extends AbstractCommand {
    public OriginStatsCommand(Debugger debugger) {
        super(debugger);
    }

    @Override
    public String getCommandName() {
        return "originStats";
    }

    @Override
    public String[] getDescription() {
        return new String[]{"", "prints origin information for nodes in the model"};
    }

    @Override
    public void printHelp(PrintWriter writer) {
        writer.println("usage: originStats");
        writer.println("    Prints origin information for the nodes in the current model.");
    }

    @Override
    public void execute(String[] args) {
        Node node;
        HashMap<LiteralConcept, OriginInfo> originInfos = new HashMap<LiteralConcept, OriginInfo>();
        for (node = this.m_debugger.getTableau().getFirstTableauNode(); node != null; node = node.getNextTableauNode()) {
            Debugger.NodeCreationInfo nodeCreationInfo = this.m_debugger.getNodeCreationInfo(node);
            ExistentialConcept existentialConcept = nodeCreationInfo.m_createdByExistential;
            if (!(existentialConcept instanceof AtLeastConcept)) continue;
            LiteralConcept toConcept = ((AtLeastConcept)existentialConcept).getToConcept();
            OriginInfo originInfo = originInfos.get(toConcept);
            if (originInfo == null) {
                originInfo = new OriginInfo(toConcept);
                originInfos.put(toConcept, originInfo);
            }
            originInfo.m_nodes.add(node);
            if (node.isActive()) continue;
            ++originInfo.m_numberOfNonactiveOccurrences;
        }
        OriginInfo[] originInfosArray = new OriginInfo[originInfos.size()];
        originInfos.values().toArray(originInfosArray);
        Arrays.sort(originInfosArray, OriginInfoComparator.INSTANCE);
        CharArrayWriter buffer = new CharArrayWriter();
        PrintWriter writer = new PrintWriter(buffer);
        writer.println("Statistics of node origins");
        writer.println("====================================");
        writer.println("  Occurrence    Nonactive   Concept");
        writer.println("====================================");
        for (OriginInfo originInfo : originInfosArray) {
            writer.print("  ");
            Printing.printPadded(writer, originInfo.m_nodes.size(), 8);
            writer.print("    ");
            Printing.printPadded(writer, originInfo.m_numberOfNonactiveOccurrences, 8);
            writer.print("    ");
            writer.print(originInfo.m_concept.toString(this.m_debugger.getPrefixes()));
            if (originInfo.m_nodes.size() <= 5) {
                writer.print("  [ ");
                for (int index = 0; index < originInfo.m_nodes.size(); ++index) {
                    if (index != 0) {
                        writer.print(", ");
                    }
                    node = originInfo.m_nodes.get(index);
                    writer.print(node.getNodeID());
                    if (node.isActive()) continue;
                    writer.print('*');
                }
                writer.print(" ]");
            }
            writer.println();
        }
        writer.println("====================================");
        writer.flush();
        this.showTextInWindow(buffer.toString(), "Statistics of node origins");
        this.selectConsoleWindow();
    }

    protected static class OriginInfoComparator
    implements Comparator<OriginInfo> {
        public static final OriginInfoComparator INSTANCE = new OriginInfoComparator();

        protected OriginInfoComparator() {
        }

        @Override
        public int compare(OriginInfo o1, OriginInfo o2) {
            int comparison = o1.m_nodes.size() - o2.m_nodes.size();
            if (comparison == 0 && (comparison = o1.m_numberOfNonactiveOccurrences - o2.m_numberOfNonactiveOccurrences) == 0) {
                comparison = Printing.ConceptComparator.INSTANCE.compare(o1.m_concept, o2.m_concept);
            }
            return comparison;
        }
    }

    protected static class OriginInfo {
        public final Concept m_concept;
        public final List<Node> m_nodes;
        public int m_numberOfNonactiveOccurrences;

        public OriginInfo(Concept concept) {
            this.m_concept = concept;
            this.m_nodes = new ArrayList<Node>();
        }
    }

}

