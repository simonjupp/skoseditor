package org.sealife.skos.editor.menu;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.action.ProtegeOWLAction;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.OWLEntityRemover;
import uk.ac.manchester.cs.skos.SKOSRDFVocabulary;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;/*
 * Copyright (C) 2010, University of Manchester
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
 * Date: Aug 23, 2011<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class FixSKOSAnnotations extends ProtegeOWLAction {


    private List<AddAxiom> axiomsToAdd = new ArrayList<AddAxiom>();
    private OWLModelManager manager;

    public void actionPerformed(ActionEvent actionEvent) {

        manager = getOWLModelManager();


        // shamefully inefficient hack... probably a more elegant way to do this in the OWL API

        Set<OWLDataProperty> dataPropertiesToRemove = new HashSet<OWLDataProperty>();

        for (OWLOntology onto : manager.getOntologies()) {

            convertAxioms(onto, manager.getOWLDataFactory().getOWLDataProperty(IRI.create(SKOSRDFVocabulary.PREFLABEL.getURI())));
            dataPropertiesToRemove.addAll(getDecendantProperties(onto,
                    manager.getOWLDataFactory().getOWLDataProperty(IRI.create(SKOSRDFVocabulary.PREFLABEL.getURI()))));

            convertAxioms(onto, manager.getOWLDataFactory().getOWLDataProperty(IRI.create(SKOSRDFVocabulary.ALTLABEL.getURI())));
            dataPropertiesToRemove.addAll(getDecendantProperties(onto,
                    manager.getOWLDataFactory().getOWLDataProperty(IRI.create(SKOSRDFVocabulary.ALTLABEL.getURI()))));

            convertAxioms(onto, manager.getOWLDataFactory().getOWLDataProperty(IRI.create(SKOSRDFVocabulary.HIDDENLABEL.getURI())));
            dataPropertiesToRemove.addAll(getDecendantProperties(onto,
                    manager.getOWLDataFactory().getOWLDataProperty(IRI.create(SKOSRDFVocabulary.HIDDENLABEL.getURI()))));

        }

        OWLEntityRemover remover = new OWLEntityRemover(manager.getOWLOntologyManager(), manager.getOntologies());

        for (OWLDataProperty p : dataPropertiesToRemove) {
            remover.visit(p);
        }

        manager.applyChanges(remover.getChanges());
        manager.applyChanges(axiomsToAdd);

    }

    private Set<OWLDataProperty> getDecendantProperties(OWLOntology onto, OWLDataProperty owlDataProperty) {
        Set<OWLDataProperty> allProps = new HashSet<OWLDataProperty>();

        allProps.add(owlDataProperty);

        for (OWLDataPropertyExpression subProp : owlDataProperty.getSubProperties(onto)) {
            if (!subProp.isAnonymous()) {
                convertAxioms(onto, subProp.asOWLDataProperty());
                createSubAnnotionProp(onto, owlDataProperty, subProp.asOWLDataProperty());
                allProps.addAll(getDecendantProperties(onto, subProp.asOWLDataProperty()));
            }
        }

        return allProps;
    }

    private void createSubAnnotionProp(OWLOntology onto, OWLDataProperty superDataProperty, OWLDataProperty subDataProperty) {

        OWLAnnotationProperty superAnno = manager.getOWLDataFactory().getOWLAnnotationProperty(superDataProperty.getIRI());
        OWLAnnotationProperty subAnno = manager.getOWLDataFactory().getOWLAnnotationProperty(subDataProperty.getIRI());

        OWLSubAnnotationPropertyOfAxiom axiom = manager.getOWLDataFactory().getOWLSubAnnotationPropertyOfAxiom(subAnno, superAnno);
        axiomsToAdd.add(new AddAxiom(onto, axiom));

    }

    public void convertAxioms (OWLOntology onto, OWLDataProperty property) {

        OWLAnnotationProperty annoProperty = manager.getOWLDataFactory().getOWLAnnotationProperty(property.getIRI());
        for (OWLAxiom ax : onto.getReferencingAxioms(property)) {
            if (ax instanceof OWLDataPropertyAssertionAxiom) {

                OWLDataPropertyAssertionAxiom axiom = (OWLDataPropertyAssertionAxiom) ax;

                OWLIndividual individual = axiom.getSubject();
                OWLLiteral literal = axiom.getObject();

                // create the new annotatoin axiom

                OWLAnnotationAssertionAxiom newAxiom = manager.getOWLDataFactory().getOWLAnnotationAssertionAxiom(annoProperty, individual.asOWLNamedIndividual().getIRI(), literal);
                axiomsToAdd.add(new AddAxiom(onto, newAxiom));

            }


        }

        


    }

    public void dispose() throws Exception {
    }

    public void initialise() throws Exception {
    }
}
