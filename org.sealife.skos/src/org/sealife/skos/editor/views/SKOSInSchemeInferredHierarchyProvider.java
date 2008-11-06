package org.sealife.skos.editor.views;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.sealife.skos.SKOSVocabulary;
import org.semanticweb.owl.inference.OWLReasoner;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.semanticweb.owl.model.*;
import uk.ac.manchester.cs.skos.SKOSRDFVocabulary;

import java.util.*;
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

    private Set<OWLOntology> ontologies;

    private OWLReasoner reasoner;

    public boolean isClassified() {
        return isClassified;
    }

    private boolean isClassified = false;

    private OWLModelManagerListener modelListener;

    private final OWLModelManager modelManager;

    private OWLClass skosConcept;

    private OWLClass skosConceptScheme;

    private OWLObjectProperty inScheme;

    private OWLObjectProperty topConceptOf;

    private Set<OWLIndividual> topConcepts;

    private Set<OWLIndividual> conceptsToView;

    public OWLIndividual getConceptSchema() {
        return conceptSchema;
    }

    public void setConceptSchema(OWLIndividual conceptSchema) {
        this.conceptSchema = conceptSchema;
    }

    private OWLIndividual conceptSchema;

    protected SKOSInSchemeInferredHierarchyProvider(final OWLModelManager modelManager, OWLReasoner reasoner) {
        super(modelManager.getOWLOntologyManager());
        this.modelManager = modelManager;
        this.reasoner = reasoner;
        ontologies = new HashSet<OWLOntology>(10);

        skosConcept = modelManager.getOWLDataFactory().getOWLClass(SKOSRDFVocabulary.CONCEPT.getURI());
        skosConceptScheme = modelManager.getOWLDataFactory().getOWLClass(SKOSRDFVocabulary.CONCEPTSCHEME.getURI());
        inScheme = modelManager.getOWLDataFactory().getOWLObjectProperty(SKOSRDFVocabulary.INSCHEME.getURI());
        topConceptOf = modelManager.getOWLDataFactory().getOWLObjectProperty(SKOSRDFVocabulary.TOPCONCEPTOF.getURI());

        conceptsToView = new HashSet<OWLIndividual>(10000);
        topConcepts = new HashSet<OWLIndividual>(10000);

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
        this.ontologies.clear();
        this.ontologies.addAll(ontologies);
        this.reasoner = modelManager.getReasoner();
        getRoots().clear();
        conceptsToView.clear();
        topConcepts.clear();
        setUp();

        try {
            // get all the individuals
            for (OWLIndividual ind : reasoner.getIndividuals( skosConcept, false)) {

                // get everything related to this individual
                Map<OWLObjectProperty, Set<OWLIndividual>> map = reasoner.getObjectPropertyRelationships(ind);

                // find out if any of these are inscheme relationships
                if (map.containsKey(inScheme)) {
                    // go and find any broader and narrower to build the tree
                    for (OWLObjectProperty prop : map.keySet()) {
                        for (OWLIndividual relInd : map.get(prop)) {
                            if (reasoner.getRelatedIndividuals(relInd, inScheme).contains(conceptSchema)) {
                                OWLObjectPropertyAssertionAxiom ax = getManager().getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(ind, prop, relInd);
                                ax.accept(getFilter());
                                conceptsToView.add(ind);
                            }
                        }
                    }
                }
                if (map.containsKey(topConceptOf)) {
                    if (map.get(topConceptOf).contains(conceptSchema)) {
                        conceptsToView.add(ind);
                        topConcepts.add(ind);
                    }
                }
            }

            conceptsToView.remove(conceptSchema);
            // now we know al the concepts in a particular scheme
            // call update roots to find the roots
            for (OWLOntology ont : ontologies) {
                updateRoots(ont, conceptsToView);
            }
            getRoots().addAll(topConcepts);

            for (OWLIndividual oldRoot : getRoots()) {
                getIndexedSet(getChild2Parent(), oldRoot, true).add(conceptSchema);
                getIndexedSet(getParent2Child(), conceptSchema, true).add(oldRoot);
            }

            getRoots().clear();
            getRoots().add(conceptSchema);
            // get all the top concpets for this scheme

            fireHierarchyChanged();
            setFireEvents(true);
        } catch (OWLReasonerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

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
        Set<Set<OWLObjectProperty>> broaderProperty = null;
        try {
            broaderProperty = reasoner.getDescendantProperties(getManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.BROADER));
        } catch (OWLReasonerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        broaderProperties.add(getManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.BROADER));

        for (Set<OWLObjectProperty> pb1 : broaderProperty) {
            for (OWLObjectProperty pb1a : pb1) {
                broaderProperties.add(pb1a);
            }
        }
        return broaderProperties;
    }

    protected Set<OWLObjectProperty> loadNarrowerProps() {
        Set<Set<OWLObjectProperty>> narrowerProperty = null;
        try {
            narrowerProperty = reasoner.getDescendantProperties(getManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.NARROWER));
        } catch (OWLReasonerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        narrowerProperties.add(getManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.NARROWER));

        for (Set<OWLObjectProperty> pb1 : narrowerProperty) {
            for (OWLObjectProperty pb1a : pb1) {
                narrowerProperties.add(pb1a);
            }
        }
        return narrowerProperties;
    }

}
