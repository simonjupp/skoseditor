package org.sealife.skos.editor.views;

import org.protege.editor.owl.model.OWLModelManager;
import org.sealife.skos.editor.SKOSVocabulary;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.*;

import java.util.HashSet;
import java.util.Map;
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

    Set<OWLIndividual> conceptsToView;

    protected SKOSConceptInferredHierarchyProvider(final OWLModelManager modelManager, OWLReasoner reasoner) {
        super(modelManager.getOWLOntologyManager());

        this.modelManager = modelManager;
        this.reasoner = reasoner;
        conceptsToView = new HashSet<OWLIndividual>(10000);

        skosConcept = modelManager.getOWLDataFactory().getOWLClass(SKOSVocabulary.CONCEPT.getURI());
    }

    public void reasonerUpdated () {
        isClassified = true;
        setOntologies(modelManager.getOntologies());
        fireHierarchyChanged();
        setFireEvents(true);
    }


    public void setOntologies(Set<OWLOntology> ontologies) {
        reasoner = modelManager.getReasoner();
        if (reasoner != null) {
            isClassified = true;
        }

        if (!isClassified) {
            return;
        }
        setFireEvents(false);
        this.reasoner = modelManager.getReasoner();
        conceptsToView.clear();
        setUp();

        try {
            for (OWLIndividual ind : reasoner.getIndividuals( skosConcept, false)) {

                Map<OWLObjectProperty, Set<OWLIndividual>> map = reasoner.getObjectPropertyRelationships(ind);
                for (OWLObjectProperty prop : map.keySet()) {

                    for (OWLIndividual relInd : map.get(prop)) {
                        OWLObjectPropertyAssertionAxiom ax = getManager().getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(ind, prop, relInd);
                        ax.accept(getFilter());
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
        } catch (OWLReasonerException e) {
            e.printStackTrace();
        }

    }

    public void setReasoner(OWLReasoner reasoner) {
        this.reasoner = reasoner;
    }

    protected Set<OWLObjectProperty> loadBroaderProps() {
        Set<Set<OWLObjectProperty>> broaderProperty = new HashSet<Set<OWLObjectProperty>>();
        try {
            broaderProperty = reasoner.getDescendantProperties(getManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.BROADER.getURI()));
        } catch (OWLReasonerException e) {
            e.printStackTrace();
        }
        broaderProperties.add(getManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.BROADER.getURI()));

        for (Set<OWLObjectProperty> pb1 : broaderProperty) {
            for (OWLObjectProperty pb1a : pb1) {
                broaderProperties.add(pb1a);
            }
        }
        return broaderProperties;
    }

    protected Set<OWLObjectProperty> loadNarrowerProps() {
        Set<Set<OWLObjectProperty>> narrowerProperty = new HashSet<Set<OWLObjectProperty>>();;
        try {
            narrowerProperty = reasoner.getDescendantProperties(getManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.NARROWER.getURI()));
        } catch (OWLReasonerException e) {
            e.printStackTrace();
        }
        narrowerProperties.add(getManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.NARROWER.getURI()));

        for (Set<OWLObjectProperty> pb1 : narrowerProperty) {
            for (OWLObjectProperty pb1a : pb1) {
                narrowerProperties.add(pb1a);
            }
        }
        return narrowerProperties;
    }
}
