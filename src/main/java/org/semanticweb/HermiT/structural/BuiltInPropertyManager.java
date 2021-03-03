package org.semanticweb.HermiT.structural;

import org.semanticweb.HermiT.model.AtomicRole;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiomVisitor;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLClassExpressionVisitor;
import org.semanticweb.owlapi.model.OWLDataAllValuesFrom;
import org.semanticweb.owlapi.model.OWLDataExactCardinality;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataHasValue;
import org.semanticweb.owlapi.model.OWLDataMaxCardinality;
import org.semanticweb.owlapi.model.OWLDataMinCardinality;
import org.semanticweb.owlapi.model.OWLDataOneOf;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLDataSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLDifferentIndividualsAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLIndividualAxiom;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLNegativeDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLNegativeObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectAllValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectExactCardinality;
import org.semanticweb.owlapi.model.OWLObjectHasSelf;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectMaxCardinality;
import org.semanticweb.owlapi.model.OWLObjectMinCardinality;
import org.semanticweb.owlapi.model.OWLObjectOneOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLObjectUnionOf;
import org.semanticweb.owlapi.model.OWLSameIndividualAxiom;
import org.semanticweb.owlapi.util.OWLAxiomVisitorAdapter;

public class BuiltInPropertyManager {
    protected final OWLDataFactory m_factory;
    protected final OWLObjectProperty m_topObjectProperty;
    protected final OWLObjectProperty m_bottomObjectProperty;
    protected final OWLDataProperty m_topDataProperty;
    protected final OWLDataProperty m_bottomDataProperty;

    public BuiltInPropertyManager(OWLDataFactory factory) {
        this.m_factory = factory;
        this.m_topObjectProperty = this.m_factory.getOWLObjectProperty(IRI.create((String)AtomicRole.TOP_OBJECT_ROLE.getIRI()));
        this.m_bottomObjectProperty = this.m_factory.getOWLObjectProperty(IRI.create((String)AtomicRole.BOTTOM_OBJECT_ROLE.getIRI()));
        this.m_topDataProperty = this.m_factory.getOWLDataProperty(IRI.create((String)AtomicRole.TOP_DATA_ROLE.getIRI()));
        this.m_bottomDataProperty = this.m_factory.getOWLDataProperty(IRI.create((String)AtomicRole.BOTTOM_DATA_ROLE.getIRI()));
    }

    public void axiomatizeBuiltInPropertiesAsNeeded(OWLAxioms axioms, boolean skipTopObjectProperty, boolean skipBottomObjectProperty, boolean skipTopDataProperty, boolean skipBottomDataProperty) {
        Checker checker = new Checker(axioms);
        if (checker.m_usesTopObjectProperty && !skipTopObjectProperty) {
            this.axiomatizeTopObjectProperty(axioms);
        }
        if (checker.m_usesBottomObjectProperty && !skipBottomObjectProperty) {
            this.axiomatizeBottomObjectProperty(axioms);
        }
        if (checker.m_usesTopDataProperty && !skipTopDataProperty) {
            this.axiomatizeTopDataProperty(axioms);
        }
        if (checker.m_usesBottomDataProperty && !skipBottomDataProperty) {
            this.axiomatizeBottomDataProperty(axioms);
        }
    }

    public void axiomatizeBuiltInPropertiesAsNeeded(OWLAxioms axioms) {
        this.axiomatizeBuiltInPropertiesAsNeeded(axioms, false, false, false, false);
    }

    protected void axiomatizeTopObjectProperty(OWLAxioms axioms) {
        axioms.m_complexObjectPropertyInclusions.add(new OWLAxioms.ComplexObjectPropertyInclusion((OWLObjectPropertyExpression)this.m_topObjectProperty));
        axioms.m_simpleObjectPropertyInclusions.add(new OWLObjectPropertyExpression[]{this.m_topObjectProperty, this.m_topObjectProperty.getInverseProperty()});
        OWLNamedIndividual newIndividual = this.m_factory.getOWLNamedIndividual(IRI.create((String)"internal:nam#topIndividual"));
        OWLObjectOneOf oneOfNewIndividual = this.m_factory.getOWLObjectOneOf(new OWLIndividual[]{newIndividual});
        OWLObjectSomeValuesFrom hasTopNewIndividual = this.m_factory.getOWLObjectSomeValuesFrom((OWLObjectPropertyExpression)this.m_topObjectProperty, (OWLClassExpression)oneOfNewIndividual);
        axioms.m_conceptInclusions.add(new OWLClassExpression[]{hasTopNewIndividual});
    }

    protected void axiomatizeBottomObjectProperty(OWLAxioms axioms) {
        axioms.m_conceptInclusions.add(new OWLClassExpression[]{this.m_factory.getOWLObjectAllValuesFrom((OWLObjectPropertyExpression)this.m_bottomObjectProperty, (OWLClassExpression)this.m_factory.getOWLNothing())});
    }

    protected void axiomatizeTopDataProperty(OWLAxioms axioms) {
        OWLDatatype anonymousConstantsDatatype = this.m_factory.getOWLDatatype(IRI.create((String)"internal:anonymous-constants"));
        OWLLiteral newConstant = this.m_factory.getOWLLiteral("internal:constant", anonymousConstantsDatatype);
        OWLDataOneOf oneOfNewConstant = this.m_factory.getOWLDataOneOf(new OWLLiteral[]{newConstant});
        OWLDataSomeValuesFrom hasTopNewConstant = this.m_factory.getOWLDataSomeValuesFrom((OWLDataPropertyExpression)this.m_topDataProperty, (OWLDataRange)oneOfNewConstant);
        axioms.m_conceptInclusions.add(new OWLClassExpression[]{hasTopNewConstant});
    }

    protected void axiomatizeBottomDataProperty(OWLAxioms axioms) {
        axioms.m_conceptInclusions.add(new OWLClassExpression[]{this.m_factory.getOWLDataAllValuesFrom((OWLDataPropertyExpression)this.m_bottomDataProperty, (OWLDataRange)this.m_factory.getOWLDataComplementOf((OWLDataRange)this.m_factory.getTopDatatype()))});
    }

    protected class Checker
    implements OWLClassExpressionVisitor {
        public boolean m_usesTopObjectProperty;
        public boolean m_usesBottomObjectProperty;
        public boolean m_usesTopDataProperty;
        public boolean m_usesBottomDataProperty;

        public Checker(OWLAxioms axioms) {
            for (OWLClassExpression[] inclusion : axioms.m_conceptInclusions) {
                for (OWLClassExpression description : inclusion) {
                    description.accept((OWLClassExpressionVisitor)this);
                }
            }
            for (OWLObjectPropertyExpression[] inclusion : axioms.m_simpleObjectPropertyInclusions) {
                this.visitProperty(inclusion[0]);
                this.visitProperty(inclusion[1]);
            }
            for (OWLAxioms.ComplexObjectPropertyInclusion inclusion : axioms.m_complexObjectPropertyInclusions) {
                for (OWLObjectPropertyExpression subObjectProperty : inclusion.m_subObjectProperties)
                    visitProperty(subObjectProperty);
                visitProperty(inclusion.m_superObjectProperty);
            }
            for (OWLObjectPropertyExpression[] disjoint : axioms.m_disjointObjectProperties) {
                for (int index = 0; index < disjoint.length; ++index) {
                    this.visitProperty(disjoint[index]);
                }
            }
            for (OWLObjectPropertyExpression property : axioms.m_reflexiveObjectProperties) {
                this.visitProperty(property);
            }
            for (OWLObjectPropertyExpression property : axioms.m_irreflexiveObjectProperties) {
                this.visitProperty(property);
            }
            for (OWLObjectPropertyExpression property : axioms.m_asymmetricObjectProperties) {
                this.visitProperty(property);
            }
            for (OWLDataPropertyExpression[] inclusion : axioms.m_dataPropertyInclusions) {
                this.visitProperty(inclusion[0]);
                this.visitProperty(inclusion[1]);
            }
            for (OWLDataPropertyExpression[] disjoint : axioms.m_disjointDataProperties) {
                for (int index = 0; index < disjoint.length; ++index) {
                    this.visitProperty(disjoint[index]);
                }
            }
            FactVisitor factVisitor = new FactVisitor();
            for (OWLIndividualAxiom fact : axioms.m_facts) {
                fact.accept((OWLAxiomVisitor)factVisitor);
            }
        }

        protected void visitProperty(OWLObjectPropertyExpression object) {
            if (object.getNamedProperty().equals((Object)BuiltInPropertyManager.this.m_topObjectProperty)) {
                this.m_usesTopObjectProperty = true;
            } else if (object.getNamedProperty().equals((Object)BuiltInPropertyManager.this.m_bottomObjectProperty)) {
                this.m_usesBottomObjectProperty = true;
            }
        }

        protected void visitProperty(OWLDataPropertyExpression object) {
            if (object.asOWLDataProperty().equals((Object)BuiltInPropertyManager.this.m_topDataProperty)) {
                this.m_usesTopDataProperty = true;
            } else if (object.asOWLDataProperty().equals((Object)BuiltInPropertyManager.this.m_bottomDataProperty)) {
                this.m_usesBottomDataProperty = true;
            }
        }

        public void visit(OWLClass object) {
        }

        public void visit(OWLObjectComplementOf object) {
            object.getOperand().accept((OWLClassExpressionVisitor)this);
        }

        public void visit(OWLObjectIntersectionOf object) {
            for (OWLClassExpression description : object.getOperands()) {
                description.accept((OWLClassExpressionVisitor)this);
            }
        }

        public void visit(OWLObjectUnionOf object) {
            for (OWLClassExpression description : object.getOperands()) {
                description.accept((OWLClassExpressionVisitor)this);
            }
        }

        public void visit(OWLObjectOneOf object) {
        }

        public void visit(OWLObjectSomeValuesFrom object) {
            this.visitProperty(object.getProperty());
            ((OWLClassExpression)object.getFiller()).accept((OWLClassExpressionVisitor)this);
        }

        public void visit(OWLObjectHasValue object) {
            this.visitProperty(object.getProperty());
        }

        public void visit(OWLObjectHasSelf object) {
            this.visitProperty(object.getProperty());
        }

        public void visit(OWLObjectAllValuesFrom object) {
            this.visitProperty(object.getProperty());
            ((OWLClassExpression)object.getFiller()).accept((OWLClassExpressionVisitor)this);
        }

        public void visit(OWLObjectMinCardinality object) {
            this.visitProperty(object.getProperty());
            ((OWLClassExpression)object.getFiller()).accept((OWLClassExpressionVisitor)this);
        }

        public void visit(OWLObjectMaxCardinality object) {
            this.visitProperty(object.getProperty());
            ((OWLClassExpression)object.getFiller()).accept((OWLClassExpressionVisitor)this);
        }

        public void visit(OWLObjectExactCardinality object) {
            this.visitProperty(object.getProperty());
            ((OWLClassExpression)object.getFiller()).accept((OWLClassExpressionVisitor)this);
        }

        public void visit(OWLDataHasValue object) {
            this.visitProperty(object.getProperty());
        }

        public void visit(OWLDataSomeValuesFrom object) {
            this.visitProperty(object.getProperty());
        }

        public void visit(OWLDataAllValuesFrom object) {
            this.visitProperty(object.getProperty());
        }

        public void visit(OWLDataMinCardinality object) {
            this.visitProperty(object.getProperty());
        }

        public void visit(OWLDataMaxCardinality object) {
            this.visitProperty(object.getProperty());
        }

        public void visit(OWLDataExactCardinality object) {
            this.visitProperty(object.getProperty());
        }

        protected class FactVisitor
        extends OWLAxiomVisitorAdapter {
            protected FactVisitor() {
            }

            public void visit(OWLSameIndividualAxiom object) {
            }

            public void visit(OWLDifferentIndividualsAxiom object) {
            }

            public void visit(OWLClassAssertionAxiom object) {
                object.getClassExpression().accept((OWLClassExpressionVisitor)Checker.this);
            }

            public void visit(OWLObjectPropertyAssertionAxiom object) {
                Checker.this.visitProperty((OWLObjectPropertyExpression)object.getProperty());
            }

            public void visit(OWLNegativeObjectPropertyAssertionAxiom object) {
                Checker.this.visitProperty((OWLObjectPropertyExpression)object.getProperty());
            }

            public void visit(OWLDataPropertyAssertionAxiom object) {
                Checker.this.visitProperty((OWLDataPropertyExpression)object.getProperty());
            }

            public void visit(OWLNegativeDataPropertyAssertionAxiom object) {
                Checker.this.visitProperty((OWLDataPropertyExpression)object.getProperty());
            }
        }

    }

}

