package org.semanticweb.HermiT.hierarchy;

import java.util.Map;
import java.util.Set;
import org.semanticweb.HermiT.model.AtomicConcept;
import org.semanticweb.HermiT.model.AtomicRole;
import org.semanticweb.HermiT.model.DLClause;
import org.semanticweb.HermiT.model.DLPredicate;
import org.semanticweb.HermiT.model.Role;
import org.semanticweb.HermiT.tableau.ReasoningTaskDescription;
import org.semanticweb.HermiT.tableau.Tableau;

public class QuasiOrderClassificationForRoles
extends QuasiOrderClassification {
    protected final boolean m_hasInverses;
    protected final Map<Role, AtomicConcept> m_conceptsForRoles;
    protected final Map<AtomicConcept, Role> m_rolesForConcepts;

    public QuasiOrderClassificationForRoles(Tableau tableau, ClassificationProgressMonitor progressMonitor, AtomicConcept topElement, AtomicConcept bottomElement, Set<AtomicConcept> elements, boolean hasInverses, Map<Role, AtomicConcept> conceptsForRoles, Map<AtomicConcept, Role> rolesForConcepts) {
        super(tableau, progressMonitor, topElement, bottomElement, elements);
        this.m_hasInverses = hasInverses;
        this.m_conceptsForRoles = conceptsForRoles;
        this.m_rolesForConcepts = rolesForConcepts;
    }

    @Override
    protected void initialiseKnownSubsumptionsUsingToldSubsumers(Set<DLClause> dlClauses) {
        for (DLClause dlClause : dlClauses) {
            if (dlClause.getHeadLength() != 1 || dlClause.getBodyLength() != 1) continue;
            DLPredicate headPredicate = dlClause.getHeadAtom(0).getDLPredicate();
            DLPredicate bodyPredicate = dlClause.getBodyAtom(0).getDLPredicate();
            if (!(headPredicate instanceof AtomicRole) || !this.m_conceptsForRoles.containsKey(headPredicate) || !(bodyPredicate instanceof AtomicRole) || !this.m_conceptsForRoles.containsKey(bodyPredicate)) continue;
            AtomicRole headRole = (AtomicRole)headPredicate;
            AtomicRole bodyRole = (AtomicRole)bodyPredicate;
            AtomicConcept conceptForHeadRole = this.m_conceptsForRoles.get(headRole);
            AtomicConcept conceptForBodyRole = this.m_conceptsForRoles.get(bodyRole);
            assert (conceptForBodyRole != null);
            assert (conceptForHeadRole != null);
            if (dlClause.getBodyAtom(0).getArgument(0) != dlClause.getHeadAtom(0).getArgument(0)) {
                AtomicConcept conceptForBodyInvRole = this.m_conceptsForRoles.get(bodyRole.getInverse());
                this.addKnownSubsumption(conceptForBodyInvRole, conceptForHeadRole);
                continue;
            }
            this.addKnownSubsumption(conceptForBodyRole, conceptForHeadRole);
        }
    }

    @Override
    protected void addKnownSubsumption(AtomicConcept subConcept, AtomicConcept superConcept) {
        super.addKnownSubsumption(subConcept, superConcept);
        if (this.m_hasInverses) {
            AtomicConcept subConceptForInverse = this.m_conceptsForRoles.get(this.m_rolesForConcepts.get(subConcept).getInverse());
            AtomicConcept superConceptForInverse = this.m_conceptsForRoles.get(this.m_rolesForConcepts.get(superConcept).getInverse());
            super.addKnownSubsumption(subConceptForInverse, superConceptForInverse);
        }
    }

    @Override
    protected void addPossibleSubsumption(AtomicConcept subConcept, AtomicConcept superConcept) {
        super.addPossibleSubsumption(subConcept, superConcept);
        if (this.m_hasInverses) {
            AtomicConcept subConceptForInverse = this.m_conceptsForRoles.get(this.m_rolesForConcepts.get(subConcept).getInverse());
            AtomicConcept superConceptForInverse = this.m_conceptsForRoles.get(this.m_rolesForConcepts.get(superConcept).getInverse());
            super.addPossibleSubsumption(subConceptForInverse, superConceptForInverse);
        }
    }

    @Override
    protected ReasoningTaskDescription getSatTestDescription(AtomicConcept atomicConcept) {
        return ReasoningTaskDescription.isRoleSatisfiable(this.m_rolesForConcepts.get(atomicConcept), true);
    }

    @Override
    protected ReasoningTaskDescription getSubsumptionTestDescription(AtomicConcept subConcept, AtomicConcept superConcept) {
        return ReasoningTaskDescription.isRoleSubsumedBy(this.m_rolesForConcepts.get(subConcept), this.m_rolesForConcepts.get(superConcept), true);
    }

    @Override
    protected ReasoningTaskDescription getSubsumedByListTestDescription(AtomicConcept subConcept, Object[] superconcepts) {
        Object[] roles = new Object[superconcepts.length];
        for (int i = 0; i < roles.length; ++i) {
            assert (superconcepts[i] instanceof AtomicConcept);
            roles[i] = this.m_rolesForConcepts.get(superconcepts[i]);
        }
        return ReasoningTaskDescription.isRoleSubsumedByList(this.m_rolesForConcepts.get(subConcept), roles, true);
    }
}

