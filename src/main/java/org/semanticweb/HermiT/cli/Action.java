/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.cli;

import java.io.PrintWriter;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.cli.StatusOutput;

interface Action {
    public void run(Reasoner var1, StatusOutput var2, PrintWriter var3, boolean var4);
}

