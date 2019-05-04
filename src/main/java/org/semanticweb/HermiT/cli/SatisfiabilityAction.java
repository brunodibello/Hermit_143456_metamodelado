/*
 * Decompiled with CFR 0.137.
 * 
 * Could not load the following classes:
 *  org.semanticweb.owlapi.apibinding.OWLManager
 *  org.semanticweb.owlapi.model.IRI
 *  org.semanticweb.owlapi.model.OWLClass
 *  org.semanticweb.owlapi.model.OWLClassExpression
 *  org.semanticweb.owlapi.model.OWLDataFactory
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
import org.semanticweb.owlapi.model.OWLDataFactory;

class SatisfiabilityAction
implements Action {
    final String conceptName;

    public SatisfiabilityAction(String c) {
        this.conceptName = c;
    }

    @Override
    public void run(Reasoner hermit, StatusOutput status, PrintWriter output, boolean ignoreOntologyPrefixes) {
        OWLClass owlClass;
        boolean result;
        String conceptUri;
        status.log(2, "Checking satisfiability of '" + this.conceptName + "'");
        Prefixes prefixes = hermit.getPrefixes();
        String string = conceptUri = prefixes.canBeExpanded(this.conceptName) ? prefixes.expandAbbreviatedIRI(this.conceptName) : this.conceptName;
        if (conceptUri.startsWith("<") && conceptUri.endsWith(">")) {
            conceptUri = conceptUri.substring(1, conceptUri.length() - 1);
        }
        if (!hermit.isDefined(owlClass = OWLManager.createOWLOntologyManager().getOWLDataFactory().getOWLClass(IRI.create((String)conceptUri)))) {
            status.log(0, "Warning: class '" + conceptUri + "' was not declared in the ontology.");
        }
        output.println(this.conceptName + ((result = hermit.isSatisfiable((OWLClassExpression)owlClass)) ? " is satisfiable." : " is not satisfiable."));
        output.flush();
    }
}

