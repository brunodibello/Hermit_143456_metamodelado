/*
 * Decompiled with CFR 0.137.
 */
package org.semanticweb.HermiT.cli;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.cli.Action;
import org.semanticweb.HermiT.cli.StatusOutput;

class DumpPrefixesAction
implements Action {
    DumpPrefixesAction() {
    }

    @Override
    public void run(Reasoner hermit, StatusOutput status, PrintWriter output, boolean ignoreOntologyPrefixes) {
        output.println("Prefixes:");
        for (Map.Entry<String, String> e : hermit.getPrefixes().getPrefixIRIsByPrefixName().entrySet()) {
            output.println("\t" + e.getKey() + "\t" + e.getValue());
        }
        output.flush();
    }
}

