package org.sealife.skos.editor.menu;

import org.protege.editor.owl.model.entity.OWLEntityCreationSet;
import org.protege.editor.owl.ui.OWLEntityCreationPanel;
import org.protege.editor.owl.ui.action.FocusedComponentAction;
import org.sealife.skos.editor.SKOSVocabulary;
import org.sealife.skos.editor.views.SKOSConceptAssertedHierarchyViewComponent;
import org.semanticweb.owl.model.*;
import uk.ac.manchester.cs.skos.SKOSRDFVocabulary;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
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
 * Date: Apr 30, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class CreateCollection extends FocusedComponentAction<SKOSConceptAssertedHierarchyViewComponent> {

    List<AddAxiom> axSet;

    public void actionPerformed(ActionEvent actionEvent) {

        SKOSConceptAssertedHierarchyViewComponent c = getCurrentTarget();

//        String term = MergeSKOSConceptsPanel.showDialog(getOWLEditorKit(), "Name the Collection");

        OWLIndividual ind = addIndividual();
        System.err.println("New Collection: " + ind.getURI());

        OWLOntology onto = getOWLModelManager().getActiveOntology();
        OWLDataFactory factory =  getOWLModelManager().getOWLDataFactory();

        axSet = new ArrayList<AddAxiom>();

        OWLClassAssertionAxiom collAx = factory.getOWLClassAssertionAxiom(ind, factory.getOWLClass(SKOSVocabulary.COLLECTION.getURI()));
        axSet.add(new AddAxiom(onto, collAx));

        for (OWLIndividual in : c.getSelectedIndividuals()) {

            OWLObjectProperty prop = factory.getOWLObjectProperty(SKOSVocabulary.MEMBER.getURI());
            axSet.add(new AddAxiom(onto, factory.getOWLObjectPropertyAssertionAxiom(ind, prop, in)));

        }

        getOWLModelManager().applyChanges(axSet);

    }

    private OWLIndividual addIndividual() {

        OWLEntityCreationSet<OWLIndividual> set = OWLEntityCreationPanel.showDialog(getOWLEditorKit(), "Please enter a Collection name", OWLIndividual.class);
        if (set == null) {
            return null;
        }
        java.util.List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        changes.addAll(set.getOntologyChanges());
        OWLClass conceptScheme = getOWLEditorKit().getModelManager().getOWLDataFactory().getOWLClass(SKOSRDFVocabulary.COLLECTION.getURI());
        OWLAxiom ax = getOWLEditorKit().getModelManager().getOWLDataFactory().getOWLClassAssertionAxiom(set.getOWLEntity(), conceptScheme);
        changes.add(new AddAxiom(getOWLModelManager().getActiveOntology(), ax));

        this.getOWLModelManager().applyChanges(changes);
        return set.getOWLEntity();
//        if (ind != null) {
//            this.list.setSelectedValue(ind, true);
//            if (!this.isPinned()) {
//                this.getOWLWorkspace().getOWLSelectionModel()
//                        .setSelectedEntity(ind);
//
//
//            }
//        }
    }


    protected Class<SKOSConceptAssertedHierarchyViewComponent> initialiseAction() {
        return SKOSConceptAssertedHierarchyViewComponent.class;
    }

    protected boolean canPerform() {
        SKOSConceptAssertedHierarchyViewComponent c = getCurrentTarget();
        if (c.getSelectedIndividuals().size() < 2) {
            return false;
        }
        return !c.getSelectedIndividuals().isEmpty();
    }
}
