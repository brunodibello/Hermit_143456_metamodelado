package org.semanticweb.HermiT;

import org.protege.editor.owl.model.inference.AbstractProtegeOWLReasonerInfo;
import org.protege.editor.owl.model.inference.ReasonerPreferences;
import org.semanticweb.owlapi.reasoner.BufferingMode;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.ReasonerProgressMonitor;

public class ProtegeReasonerFactory
extends AbstractProtegeOWLReasonerInfo {
    protected final ReasonerFactory factory = new ReasonerFactory();

    public BufferingMode getRecommendedBuffering() {
        return BufferingMode.BUFFERING;
    }

    public OWLReasonerFactory getReasonerFactory() {
        return this.factory;
    }

    public OWLReasonerConfiguration getConfiguration(ReasonerProgressMonitor monitor) {
        Configuration configuration = new Configuration();
        configuration.ignoreUnsupportedDatatypes = true;
        configuration.reasonerProgressMonitor = monitor;
        try {
            AbstractProtegeOWLReasonerInfo.class.getMethod("getOWLModelManager", null);
            ReasonerPreferences preferences = this.getOWLModelManager().getReasonerPreferences();
            Configuration.PrepareReasonerInferences prepareReasonerInferences = new Configuration.PrepareReasonerInferences();
            prepareReasonerInferences.classClassificationRequired = preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_CLASS_UNSATISFIABILITY) || preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_EQUIVALENT_CLASSES) || preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_DISJOINT_CLASSES) || preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_SUPER_CLASSES) || preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_OBJECT_PROPERTY_DOMAINS) || preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_OBJECT_PROPERTY_RANGES) || preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_DATATYPE_PROPERTY_DOMAINS);
            prepareReasonerInferences.objectPropertyClassificationRequired = preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_EQUIVALENT_OBJECT_PROPERTIES) || preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_INVERSE_PROPERTIES) || preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_SUPER_OBJECT_PROPERTIES) || preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_OBJECT_PROPERTY_UNSATISFIABILITY);
            prepareReasonerInferences.dataPropertyClassificationRequired = preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_EQUIVALENT_DATATYPE_PROPERTIES) || preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_SUPER_DATATYPE_PROPERTIES) || preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_DATA_PROPERTY_ASSERTIONS);
            prepareReasonerInferences.realisationRequired = preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERED_CLASS_MEMBERS) || preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_TYPES);
            prepareReasonerInferences.objectPropertyRealisationRequired = preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_OBJECT_PROPERTY_ASSERTIONS);
            prepareReasonerInferences.dataPropertyRealisationRequired = preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_DATA_PROPERTY_ASSERTIONS);
            prepareReasonerInferences.objectPropertyDomainsRequired = preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_OBJECT_PROPERTY_DOMAINS);
            prepareReasonerInferences.objectPropertyRangesRequired = preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_OBJECT_PROPERTY_RANGES);
            prepareReasonerInferences.sameAs = preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_DATA_PROPERTY_ASSERTIONS) || preferences.isEnabled(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_SAMEAS_INDIVIDUAL_ASSERTIONS);
            configuration.prepareReasonerInferences = prepareReasonerInferences;
        }
        catch (NoSuchMethodException preferences) {
            // empty catch block
        }
        return configuration;
    }
}

