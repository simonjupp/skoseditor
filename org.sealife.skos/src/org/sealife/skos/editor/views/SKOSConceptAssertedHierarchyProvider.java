package org.sealife.skos.editor.views;

import org.sealife.skos.editor.SKOSVocabulary;
import org.semanticweb.owl.model.*;
import org.semanticweb.skos.SKOSCreationException;
import org.semanticweb.skosapibinding.SKOSManager;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
 * Author: Matthew Horridge, Simon Jupp<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 04-May-2007<br><br>
 */
public class SKOSConceptAssertedHierarchyProvider extends AbstractSKOSHierarchyProvider implements OWLOntologyChangeListener {


    private Set<OWLOntology> ontologies;

    private Set<OWLIndividual> conceptsToView;

    private OWLClass skosConcept;

    private SKOSManager skosManager;

    public SKOSConceptAssertedHierarchyProvider(OWLOntologyManager owlOntologyManager) {

        super(owlOntologyManager);

        skosManager = new SKOSManager(owlOntologyManager);

        ontologies = new HashSet<OWLOntology>(10);
        conceptsToView = new HashSet<OWLIndividual>(10000);

        owlOntologyManager.addOntologyChangeListener(this);
    }

    protected Set<OWLObjectProperty> loadBroaderProps() {
        return Collections.singleton(getManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.BROADER));
    }

    protected Set<OWLObjectProperty> loadNarrowerProps() {
        return Collections.singleton(getManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.NARROWER));
    }

    public void setOntologies(Set<OWLOntology> ontologies) {
//        setFireEvents(false);
        this.ontologies.clear();
        this.ontologies.addAll(ontologies);
        this.conceptsToView.clear();

        skosConcept = getManager().getOWLDataFactory().getOWLClass(SKOSVocabulary.CONCEPT);

        for (OWLOntology ont : ontologies) {
            try {
                skosManager.loadDataset(ont.getURI());
            } catch (SKOSCreationException e) {
                e.printStackTrace();  
            }

            for (OWLObjectPropertyAssertionAxiom propAx : ont.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
                propAx.accept(getFilter());
            }


            for (OWLClassAssertionAxiom axiom : ont.getAxioms(AxiomType.CLASS_ASSERTION)) {
                if (axiom.getDescription().equals(skosConcept)) {
                    conceptsToView.add(axiom.getIndividual());
                }
            }
            updateRoots(ont, conceptsToView);

        }

        fireHierarchyChanged();
//        setFireEvents(true);
    }


    public void dispose() {
        super.dispose();        
        getManager().removeOntologyChangeListener(this);
    }


    public void ontologiesChanged(List<? extends OWLOntologyChange> changes) throws OWLException {
        Set<OWLIndividual> affectedIndividuals = null;

        for(OWLOntologyChange change : changes) {
            if(change.isAxiomChange()) {
                if(change.getAxiom() instanceof OWLObjectPropertyAssertionAxiom || change.getAxiom() instanceof OWLClassAssertionAxiom) {


                    if(ontologies.contains(change.getOntology())) {

                        setAdd(change instanceof AddAxiom);

                        change.getAxiom().accept(getFilter());
                        if(affectedIndividuals == null) {
                            affectedIndividuals = new HashSet<OWLIndividual>();
                        }

                        if (change.getAxiom() instanceof OWLObjectPropertyAssertionAxiom) {
                            affectedIndividuals.add(((OWLObjectPropertyAssertionAxiom) change.getAxiom()).getSubject());
                            affectedIndividuals.add(((OWLObjectPropertyAssertionAxiom) change.getAxiom()).getObject());
                        }
                        else if(change.getAxiom() instanceof OWLClassAssertionAxiom) {
                            affectedIndividuals.add(((OWLClassAssertionAxiom) change.getAxiom()).getIndividual());
                        }
                    }
                }
            }
        }

        if (affectedIndividuals != null) {
            getRoots().clear();
            getNonroots().clear();
            getParent2Child().clear();
            getParent2Child().clear();
            conceptsToView.clear();
            for (OWLOntology ont : ontologies) {

                for (OWLObjectPropertyAssertionAxiom propAx : ont.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION)) {
                    setAdd(true);
                    propAx.accept(getFilter());
                }

                for (OWLClassAssertionAxiom axiom : ont.getAxioms(AxiomType.CLASS_ASSERTION)) {
                    if (axiom.getDescription().equals(skosConcept)) {
                        conceptsToView.add(axiom.getIndividual());
                    }
                }
                updateRoots(ont, conceptsToView);
            }

            for(OWLIndividual ind : affectedIndividuals) {
                fireNodeChanged(ind);
            }
        }
    }



    public SKOSManager getSKOSManager() {
        return skosManager;  //To change body of created methods use File | Settings | File Templates.
    }

}
