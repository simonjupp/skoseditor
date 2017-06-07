package org.sealife.skos.editor.views;

import org.protege.editor.owl.model.OWLModelManager;
import org.sealife.skos.editor.SKOSVocabulary;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

import java.util.HashSet;
import java.util.Set;
/*
 * Copyright (C) 2007, University of Manchester
 *
 * Modifications to the initial code base are copyright of their
 * respective authors, or their employers as appropriate.  Authorship
 * of the modifications may be determined from the ChangeLog placed at
 * the end of this file.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

/**
 * Author: Simon Jupp<br>
 * Date: Oct 2, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class SKOSConceptInferredHierarchyProvider extends AbstractSKOSHierarchyProvider {


    private OWLReasoner reasoner;

    private boolean isClassified = false;

    private final OWLModelManager modelManager;

    private OWLClass skosConcept;

    Set<OWLNamedIndividual> conceptsToView;

    protected SKOSConceptInferredHierarchyProvider(final OWLModelManager modelManager, OWLReasoner reasoner) {
        super(modelManager.getOWLOntologyManager());

        this.modelManager = modelManager;
        this.reasoner = reasoner;
        conceptsToView = new HashSet<OWLNamedIndividual>(10000);

        skosConcept = modelManager.getOWLDataFactory().getOWLClass(SKOSVocabulary.CONCEPT.getIRI());
    }

    public void reasonerUpdated () {
        isClassified = true;
        setOntologies(modelManager.getOntologies());
        fireHierarchyChanged();
        setFireEvents(true);
    }


    public void setOntologies(Set<OWLOntology> ontologies) {

        reasoner = modelManager.getOWLReasonerManager().getCurrentReasoner();

        if (reasoner != null) {
            isClassified = true;
        }

        if (!isClassified) {
            return;
        }
        setFireEvents(false);
        conceptsToView.clear();
        setUp();

        Set<OWLObjectProperty> allObjectProperties = reasoner.getRootOntology().getObjectPropertiesInSignature(true);

        for (OWLNamedIndividual ind : reasoner.getInstances( skosConcept, false).getFlattened()) {

            for (OWLObjectProperty prop : allObjectProperties) {
                if (prop.equals(getManager().getOWLDataFactory().getOWLTopObjectProperty())) {
                    continue;
                }
                NodeSet<OWLNamedIndividual> values = reasoner.getObjectPropertyValues(ind, prop);
                for (OWLNamedIndividual relInd : values.getFlattened()) {

                    OWLObjectPropertyAssertionAxiom ax = getManager().getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(prop, ind, relInd);
                    ax.accept(getSKOSFilter());

                }
            }
            conceptsToView.add(ind);
        }

        for (OWLOntology ont : ontologies) {
//                for (OWLClassAssertionAxiom axiom : ont.getAxioms(AxiomType.CLASS_ASSERTION)) {
//                    if (axiom.getDescription().equals(skosConcept)) {
//                        conceptsToView.add(axiom.getIndividual());
//                    }
//                }
            updateRoots(ont, conceptsToView);
        }

        fireHierarchyChanged();
        setFireEvents(true);


    }

    public void setReasoner(OWLReasoner reasoner) {
        this.reasoner = reasoner;
    }

    protected Set<OWLObjectProperty> loadBroaderProps() {

        OWLObjectProperty p = getManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.BROADER.getIRI());
        Set<OWLObjectPropertyExpression> broaderProperty = reasoner.getSubObjectProperties(p, true).getFlattened();

        broaderProperties.add(getManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.BROADER.getIRI()));

        for (OWLObjectPropertyExpression pb1 : broaderProperty) {
            if (!pb1.isAnonymous()) {
                broaderProperties.add(pb1.asOWLObjectProperty());
            }
        }
        return broaderProperties;
    }

    protected Set<OWLObjectProperty> loadNarrowerProps() {

        OWLObjectProperty p = getManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.NARROWER.getIRI());
        Set<OWLObjectPropertyExpression> narrowerProperty = reasoner.getSubObjectProperties(p, true).getFlattened();

        narrowerProperties.add(getManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.NARROWER.getIRI()));

        for (OWLObjectPropertyExpression pb1 : narrowerProperty) {
            if (!pb1.isAnonymous()) {
                narrowerProperties.add(pb1.asOWLObjectProperty());
            }
        }
        return narrowerProperties;

    }
}
