package org.sealife.skos.editor.views;

import org.protege.editor.owl.model.OWLModelManager;
import org.sealife.skos.editor.SKOSVocabulary;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

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
 * Date: Oct 12, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class SKOSInSchemeInferredHierarchyProvider extends AbstractSKOSHierarchyProvider {


    private OWLReasoner reasoner;

    public boolean isClassified() {
        return isClassified;
    }

    private boolean isClassified = false;

    private final OWLModelManager modelManager;

    private OWLClass skosConcept;

    private OWLObjectProperty inScheme;

    private OWLObjectProperty topConceptOf;

    private Set<OWLNamedIndividual> topConcepts;

    private Set<OWLNamedIndividual> conceptsToView;

    public OWLNamedIndividual getConceptSchema() {
        return conceptSchema;
    }

    public void setConceptSchema(OWLNamedIndividual conceptSchema) {
        this.conceptSchema = conceptSchema;
    }

    private OWLNamedIndividual conceptSchema;

    protected SKOSInSchemeInferredHierarchyProvider(final OWLModelManager modelManager, OWLReasoner reasoner) {
        super(modelManager.getOWLOntologyManager());
        this.modelManager = modelManager;
        this.reasoner = reasoner;

        skosConcept = modelManager.getOWLDataFactory().getOWLClass(SKOSVocabulary.CONCEPT.getIRI());
        inScheme = modelManager.getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.INSCHEME.getIRI());
        topConceptOf = modelManager.getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.TOPCONCEPTOF.getIRI());

        conceptsToView = new HashSet<OWLNamedIndividual>(10000);
        topConcepts = new HashSet<OWLNamedIndividual>(10000);

    }

    public void setOntologies(Set<OWLOntology> ontologies) {

        if (conceptSchema == null) {
            return;
        }
        reasoner = modelManager.getReasoner();
        if (reasoner != null) {
            isClassified = true;
        }

        if (!isClassified) {
            return;
        }

        setFireEvents(false);
        this.reasoner = modelManager.getReasoner();
        getRoots().clear();
        conceptsToView.clear();
        topConcepts.clear();
        setUp();


            // get all the individuals
            for (OWLNamedIndividual ind : reasoner.getInstances( skosConcept, false).getFlattened()) {

                // get everything related to this individual

                // find out if any of these are inscheme relationships
                if (!reasoner.getObjectPropertyValues(ind, inScheme).isEmpty()) {

                    for (OWLObjectProperty prop : modelManager.getActiveOntology().getObjectPropertiesInSignature()) {

                        // go and find any broader and narrower to build the tree
                        for (OWLNamedIndividual relind : reasoner.getObjectPropertyValues(ind, prop).getFlattened()) {

                            if (reasoner.getObjectPropertyValues(relind, inScheme).containsEntity(conceptSchema)) {
                                OWLObjectPropertyAssertionAxiom ax = getManager().getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(prop,ind, relind);
                                ax.accept(getSKOSFilter());
                            }
                            conceptsToView.add(ind);
                        }
                    }
                }

                if (!reasoner.getObjectPropertyValues(ind, topConceptOf).isEmpty()) {

                    for (OWLNamedIndividual top : reasoner.getObjectPropertyValues(ind, topConceptOf).getFlattened()) {
//                        if (!reasoner.getObjectPropertyValues(top, conceptSchema).isEmpty()) {
//
//                        }
                    }
                }

//                if (map.containsKey(topConceptOf)) {
//                    if (map.get(topConceptOf).contains(conceptSchema)) {
//                        conceptsToView.add(ind);
//                        topConcepts.add(ind);
//                    }
//                }
            }

            // go through and remove and concepts that shouldn't be in the tree
            for (OWLNamedIndividual ind : reasoner.getInstances( skosConcept, false).getFlattened()) {
                if (!inSchemea(ind, conceptSchema)) {
                    getChild2Parent().remove(ind);
                    getParent2Child().remove(ind);
                    conceptsToView.remove(ind);
                }
            }

            conceptsToView.remove(conceptSchema);
            // now we know all the concepts in a particular schema
            // call update roots to find the roots
            for (OWLOntology ont : ontologies) {
                updateRoots(ont, conceptsToView);
            }
            getRoots().addAll(topConcepts);

            for (OWLNamedIndividual oldRoot : getRoots()) {
                getIndexedSet(getChild2Parent(), oldRoot, true).add(conceptSchema);
                getIndexedSet(getParent2Child(), conceptSchema, true).add(oldRoot);
            }

            getRoots().clear();
            getRoots().add(conceptSchema);
            // get all the top concpets for this scheme

            fireHierarchyChanged();
            setFireEvents(true);



    }

    private boolean inSchemea(OWLNamedIndividual ind, OWLIndividual conceptSchema) {

        return reasoner.getObjectPropertyValues(ind, inScheme).getFlattened().contains(conceptSchema);
    }


    public void reasonerUpdated () {
        isClassified = true;
        setOntologies(modelManager.getOntologies());
        fireHierarchyChanged();
        setFireEvents(true);
    }

    public void setReasoner(OWLReasoner reasoner) {
        this.reasoner = reasoner;
    }



    protected Set<OWLObjectProperty> loadBroaderProps() {
        Set<OWLObjectPropertyExpression> broaderProperty = new HashSet<OWLObjectPropertyExpression>();
        broaderProperty = reasoner.getSubObjectProperties(getManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.BROADER.getIRI()), true).getFlattened();
        broaderProperties.add(getManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.BROADER.getIRI()));

        for (OWLObjectPropertyExpression pb1 : broaderProperty) {
                broaderProperties.add(pb1.asOWLObjectProperty());

        }
        return broaderProperties;
    }

    protected Set<OWLObjectProperty> loadNarrowerProps() {
        Set<OWLObjectPropertyExpression> narrowerProperty = new HashSet<OWLObjectPropertyExpression>();
        narrowerProperty = reasoner.getSubObjectProperties(getManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.NARROWER.getIRI()),true).getFlattened();

        narrowerProperties.add(getManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.NARROWER.getIRI()));

        for (OWLObjectPropertyExpression pb1 : narrowerProperty) {
            narrowerProperties.add(pb1.asOWLObjectProperty());
        }
        return narrowerProperties;
    }

}
