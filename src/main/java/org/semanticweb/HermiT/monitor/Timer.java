/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.monitor;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.monitor.TableauMonitorAdapter;
import org.semanticweb.HermiT.tableau.BranchingPoint;
import org.semanticweb.HermiT.tableau.DependencySetFactory;
import org.semanticweb.HermiT.tableau.ExtensionManager;
import org.semanticweb.HermiT.tableau.ExtensionTable;
import org.semanticweb.HermiT.tableau.ReasoningTaskDescription;
import org.semanticweb.HermiT.tableau.Tableau;

public class Timer
extends TableauMonitorAdapter {
    private static final long serialVersionUID = -8144444618897251350L;
    protected transient PrintWriter m_output;
    protected long m_problemStartTime;
    protected long m_lastStatusTime;
    protected int m_numberOfBacktrackings;
    protected int m_testNumber = 0;

    public Timer(OutputStream out) {
        this.m_output = new PrintWriter(out);
    }

    protected Object readResolve() {
        this.m_output = new PrintWriter(System.out);
        return this;
    }

    protected void start() {
        this.m_numberOfBacktrackings = 0;
        this.m_lastStatusTime = this.m_problemStartTime = System.currentTimeMillis();
    }

    @Override
    public void isSatisfiableStarted(ReasoningTaskDescription reasoningTaskDescription) {
        this.m_output.print(reasoningTaskDescription.getTaskDescription(Prefixes.STANDARD_PREFIXES) + " ...");
        this.m_output.flush();
        this.start();
    }

    @Override
    public void isSatisfiableFinished(ReasoningTaskDescription reasoningTaskDescription, boolean result) {
        if (reasoningTaskDescription.flipSatisfiabilityResult()) {
            result = !result;
        }
        this.m_output.println(result ? "YES" : "NO");
        this.doStatistics();
    }

    @Override
    public void iterationStarted() {
        if (System.currentTimeMillis() - this.m_lastStatusTime > 30000L) {
            if (this.m_lastStatusTime == this.m_problemStartTime) {
                this.m_output.println();
            }
            this.doStatistics();
            this.m_lastStatusTime = System.currentTimeMillis();
        }
    }

    @Override
    public void saturateStarted() {
        ++this.m_testNumber;
    }

    @Override
    public void backtrackToFinished(BranchingPoint newCurrentBrancingPoint) {
        ++this.m_numberOfBacktrackings;
    }

    protected void doStatistics() {
        long duartionSoFar = System.currentTimeMillis() - this.m_problemStartTime;
        this.m_output.print("    Test:   ");
        this.printPadded(this.m_testNumber, 7);
        this.m_output.print("  Duration:  ");
        this.printPaddedMS(duartionSoFar, 7);
        this.m_output.print("   Current branching point: ");
        this.printPadded(this.m_tableau.getCurrentBranchingPointLevel(), 7);
        if (this.m_numberOfBacktrackings > 0) {
            this.m_output.print("    Backtrackings: ");
            this.m_output.print(this.m_numberOfBacktrackings);
        }
        this.m_output.println();
        this.m_output.print("    Nodes:  allocated:    ");
        this.printPadded(this.m_tableau.getNumberOfAllocatedNodes(), 7);
        this.m_output.print("    used: ");
        this.printPadded(this.m_tableau.getNumberOfNodeCreations(), 7);
        this.m_output.print("    in tableau: ");
        this.printPadded(this.m_tableau.getNumberOfNodesInTableau(), 7);
        if (this.m_tableau.getNumberOfMergedOrPrunedNodes() > 0) {
            this.m_output.print("    merged/pruned: ");
            this.m_output.print(this.m_tableau.getNumberOfMergedOrPrunedNodes());
        }
        this.m_output.println();
        this.m_output.print("    Sizes:  binary table: ");
        this.printPaddedKB(this.m_tableau.getExtensionManager().getBinaryExtensionTable().sizeInMemory() / 1000, 7);
        this.m_output.print("    ternary table: ");
        this.printPaddedKB(this.m_tableau.getExtensionManager().getTernaryExtensionTable().sizeInMemory() / 1000, 7);
        this.m_output.print("    dependency set factory: ");
        this.printPaddedKB(this.m_tableau.getDependencySetFactory().sizeInMemory() / 1000, 7);
        this.m_output.println();
        this.m_output.println();
        this.m_output.flush();
    }

    protected void printPadded(int number, int padding) {
        String numberString = String.valueOf(number);
        this.m_output.print(numberString);
        for (int index = numberString.length(); index < padding; ++index) {
            this.m_output.print(' ');
        }
    }

    protected void printPaddedMS(long number, int padding) {
        String numberString = number + " ms";
        this.m_output.print(numberString);
        for (int index = numberString.length(); index < padding; ++index) {
            this.m_output.print(' ');
        }
    }

    protected void printPaddedKB(int number, int padding) {
        String numberString = number + " kb";
        this.m_output.print(numberString);
        for (int index = numberString.length(); index < padding; ++index) {
            this.m_output.print(' ');
        }
    }
}

