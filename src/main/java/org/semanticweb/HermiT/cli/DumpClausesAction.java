package org.semanticweb.HermiT.cli;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.Reasoner;

class DumpClausesAction
implements Action {
    final String file;

    public DumpClausesAction(String fileName) {
        this.file = fileName;
    }

    @Override
    public void run(Reasoner hermit, StatusOutput status, PrintWriter output, boolean ignoreOntologyPrefixes) {
        if (this.file != null) {
            if (this.file.equals("-")) {
                output = new PrintWriter(System.out);
            } else {
                FileOutputStream f;
                try {
                    f = new FileOutputStream(this.file);
                }
                catch (FileNotFoundException e) {
                    throw new IllegalArgumentException("unable to open " + this.file + " for writing");
                }
                catch (SecurityException e) {
                    throw new IllegalArgumentException("unable to write to " + this.file);
                }
                output = new PrintWriter(f);
            }
        }
        if (ignoreOntologyPrefixes) {
            output.println(hermit.getDLOntology().toString(new Prefixes()));
        } else {
            output.println(hermit.getDLOntology().toString(hermit.getPrefixes()));
        }
        output.flush();
    }
}

