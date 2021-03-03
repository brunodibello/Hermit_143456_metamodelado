package org.semanticweb.HermiT.cli;

import java.io.PrintWriter;
import java.util.HashSet;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.reasoner.InferenceType;

class ClassifyAction
implements Action {
    final boolean classifyClasses;
    final boolean classifyOPs;
    final boolean classifyDPs;
    final boolean prettyPrint;
    final String outputLocation;

    public ClassifyAction(boolean classifyClasses, boolean classifyOPs, boolean classifyDPs, boolean prettyPrint, String outputLocation) {
        this.classifyClasses = classifyClasses;
        this.classifyOPs = classifyOPs;
        this.classifyDPs = classifyDPs;
        this.prettyPrint = prettyPrint;
        this.outputLocation = outputLocation;
    }

    @Override
    public void run(Reasoner hermit, StatusOutput status, PrintWriter output, boolean ignoreOntologyPrefixes) {
        HashSet<InferenceType> inferences = new HashSet<InferenceType>();
        if (this.classifyClasses) {
            inferences.add(InferenceType.CLASS_HIERARCHY);
        }
        if (this.classifyOPs) {
            inferences.add(InferenceType.OBJECT_PROPERTY_HIERARCHY);
        }
        if (this.classifyDPs) {
            inferences.add(InferenceType.DATA_PROPERTY_HIERARCHY);
        }
        status.log(2, "Classifying...");
        hermit.precomputeInferences(inferences.toArray(new InferenceType[0]));
        if (output != null) {
            if (this.outputLocation != null) {
                status.log(2, "Writing results to " + this.outputLocation);
            } else {
                status.log(2, "Writing results...");
            }
            if (this.prettyPrint) {
                hermit.printHierarchies(output, this.classifyClasses, this.classifyOPs, this.classifyDPs);
            } else {
                hermit.dumpHierarchies(output, this.classifyClasses, this.classifyOPs, this.classifyDPs);
            }
            output.flush();
        }
    }
}

