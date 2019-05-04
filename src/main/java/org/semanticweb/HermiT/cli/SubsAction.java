/*
 * Decompiled with CFR 0.137.
 * 
 * Could not load the following classes:
 *  org.semanticweb.owlapi.apibinding.OWLManager
 *  org.semanticweb.owlapi.model.IRI
 *  org.semanticweb.owlapi.model.OWLClass
 *  org.semanticweb.owlapi.model.OWLClassExpression
 *  org.semanticweb.owlapi.reasoner.Node
 *  org.semanticweb.owlapi.reasoner.NodeSet
 */
package org.semanticweb.HermiT.cli;

import java.io.PrintWriter;
import org.semanticweb.HermiT.Prefixes;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.HermiT.cli.Action;
import org.semanticweb.HermiT.cli.StatusOutput;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;

class SubsAction
implements Action {
    final String conceptName;
    final boolean all;

    public SubsAction(String name, boolean getAll) {
        this.conceptName = name;
        this.all = getAll;
    }

    @Override
    public void run(Reasoner hermit, StatusOutput status, PrintWriter output, boolean ignoreOntologyPrefixes) {
        OWLClass owlClass;
        NodeSet<OWLClass> classes;
        String conceptUri;
        status.log(2, "Finding subs of '" + this.conceptName + "'");
        Prefixes prefixes = hermit.getPrefixes();
        String string = conceptUri = prefixes.canBeExpanded(this.conceptName) ? prefixes.expandAbbreviatedIRI(this.conceptName) : this.conceptName;
        if (conceptUri.startsWith("<") && conceptUri.endsWith(">")) {
            conceptUri = conceptUri.substring(1, conceptUri.length() - 1);
        }
        if (!hermit.isDefined(owlClass = OWLManager.getOWLDataFactory().getOWLClass(IRI.create((String)conceptUri)))) {
            status.log(0, "Warning: class '" + conceptUri + "' was not declared in the ontology.");
        }
        if (this.all) {
            classes = hermit.getSubClasses((OWLClassExpression)owlClass, false);
            output.println("All sub-classes of '" + this.conceptName + "':");
        } else {
            classes = hermit.getSubClasses((OWLClassExpression)owlClass, true);
            output.println("Direct sub-classes of '" + this.conceptName + "':");
        }
        for (Node set : classes) {
            for (OWLClass classInSet : set) {
                if (ignoreOntologyPrefixes) {
                    String iri = classInSet.getIRI().toString();
                    if (prefixes.canBeExpanded(iri)) {
                        output.println("\t" + prefixes.expandAbbreviatedIRI(iri));
                        continue;
                    }
                    output.println("\t" + iri);
                    continue;
                }
                output.println("\t" + prefixes.abbreviateIRI(classInSet.getIRI().toString()));
            }
        }
        output.flush();
    }
}

