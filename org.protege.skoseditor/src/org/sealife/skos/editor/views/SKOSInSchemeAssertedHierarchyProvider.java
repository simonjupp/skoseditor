package org.sealife.skos.editor.views;

import org.protege.editor.owl.model.OWLModelManager;
import org.sealife.skos.editor.SKOSVocabulary;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.skos.SKOSRDFVocabulary;

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
 * Date: Oct 12, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class SKOSInSchemeAssertedHierarchyProvider extends AbstractSKOSHierarchyProvider {


    private final OWLModelManager modelManager;


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

    protected SKOSInSchemeAssertedHierarchyProvider(final OWLModelManager modelManager) {
        super(modelManager.getOWLOntologyManager());
        this.modelManager = modelManager;

        inScheme = modelManager.getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.INSCHEME.getIRI());
        topConceptOf = modelManager.getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.TOPCONCEPTOF.getIRI());

        conceptsToView = new HashSet<OWLNamedIndividual>(10000);
        topConcepts = new HashSet<OWLNamedIndividual>(10000);

    }

    public void setOntologies(Set<OWLOntology> ontologies) {

        if (conceptSchema == null) {
            return;
        }

        setFireEvents(false);
        getRoots().clear();
        conceptsToView.clear();
        topConcepts.clear();
        setUp();

        // get all the individuals
        for (OWLOntology onto : ontologies) {
            for (OWLAxiom ax : onto.getReferencingAxioms(conceptSchema)) {
                if (ax instanceof OWLObjectPropertyAssertionAxiom) {
                    OWLObjectPropertyAssertionAxiom propAx = (OWLObjectPropertyAssertionAxiom) ax;
                    if (propAx.getProperty().asOWLObjectProperty().equals(inScheme)) {
                        conceptsToView.add(propAx.getSubject().asOWLNamedIndividual());
                    }
                    if (propAx.getProperty().asOWLObjectProperty().equals(topConceptOf)) {
                        topConcepts.add(propAx.getSubject().asOWLNamedIndividual());
                        conceptsToView.add(propAx.getSubject().asOWLNamedIndividual());
                    }
                }
            }
        }

        // I should probably check for every relation between the two concepts, that each concept is in the correct concept scheme!
        for (OWLNamedIndividual ind : conceptsToView) {
            for (OWLOntology onto : ontologies) {

                for (OWLAxiom ax : onto.getReferencingAxioms(ind)) {
                    ax.accept(getSKOSFilter());
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

        for (OWLNamedIndividual oldRoot : getRoots()) {
            getIndexedSet(getChild2Parent(), oldRoot, true).add(conceptSchema);
            getIndexedSet(getParent2Child(), conceptSchema, true).add(oldRoot);
        }

        getRoots().clear();
        getRoots().add(conceptSchema);
        for (OWLNamedIndividual root : getRoots()) {
            System.err.println("root: " + root.getIRI());
        }
        // get all the top concpets for this scheme
        System.out.println("tree built!");

        fireHierarchyChanged();
        setFireEvents(true);
    }

    protected Set<OWLObjectProperty> loadBroaderProps() {
        broaderProperties.add(getManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.BROADER.getIRI()));
        return broaderProperties;
    }
    protected Set<OWLObjectProperty> loadNarrowerProps() {
        narrowerProperties.add(getManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.NARROWER.getIRI()));
        return narrowerProperties;
    }

    public void update(Set<OWLOntology> ontologies) {
        setOntologies(modelManager.getOntologies());
        fireHierarchyChanged();
        setFireEvents(true);
    }
}
