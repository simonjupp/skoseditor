package org.sealife.skos.editor.menu;

import org.protege.editor.owl.ui.action.FocusedComponentAction;
import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.OWLEntityRenamer;
import org.sealife.skos.editor.views.SKOSConceptAssertedHierarchyViewComponent;
import org.sealife.skos.editor.panels.MergeSKOSConceptsPanel;
import org.sealife.skos.SKOSVocabulary;

import java.awt.event.ActionEvent;
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
 * Date: Apr 30, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class MergeSKOSConcepts extends FocusedComponentAction<SKOSConceptAssertedHierarchyViewComponent> {

    Set<String> labels;

    public void actionPerformed(ActionEvent actionEvent) {

        SKOSConceptAssertedHierarchyViewComponent c = getCurrentTarget();

        String term = MergeSKOSConceptsPanel.showDialog(getOWLEditorKit(), "Specify prefered label");

        System.err.println("term entered: " + term);

        OWLOntology onto = getOWLModelManager().getActiveOntology();
        OWLDataFactory factory =  getOWLModelManager().getOWLDataFactory();

        labels = new HashSet<String>();

        // they will essentialy all get merged into this one!
        List<RemoveAxiom> axs = new ArrayList<RemoveAxiom>();

        for (OWLIndividual in : c.getSelectedIndividuals()) {

            System.err.println("Selected: " + in.getURI().getFragment());

            Set<OWLDataPropertyAssertionAxiom> assertion = onto.getDataPropertyAssertionAxioms(in);

            for (OWLDataPropertyAssertionAxiom ax : assertion) {

                OWLDataPropertyExpression prop = ax.getProperty();

                if (prop.asOWLDataProperty().getURI().equals(SKOSVocabulary.PREFLABEL)) {
                    axs.add(new RemoveAxiom(onto, ax));
                    labels.add(ax.getObject().getLiteral());
                }
                if (prop.asOWLDataProperty().getURI().equals(SKOSVocabulary.ALTLABEL)) {
                    axs.add(new RemoveAxiom(onto, ax));
                    labels.add(ax.getObject().getLiteral());
                }
                if (prop.asOWLDataProperty().getURI().equals(SKOSVocabulary.HIDDENLABEL)) {
                    axs.add(new RemoveAxiom(onto, ax));
                    labels.add(ax.getObject().getLiteral());
                }
            }
        }

        labels.remove(term);
        getOWLModelManager().applyChanges(axs);

        List<OWLIndividual> indList = new ArrayList<OWLIndividual>(c.getSelectedIndividuals());

        OWLEntityRenamer renamer = new OWLEntityRenamer(getOWLModelManager().getOWLOntologyManager(), getOWLModelManager().getOntologies());
        // get the last thing, remove it.
        OWLIndividual mainInd = indList.get(indList.size() -1);
        indList.remove(indList.size() -1);

        Iterator it = indList.iterator();
        while (it.hasNext()) {
            OWLIndividual next = (OWLIndividual) it.next();
            getOWLModelManager().applyChanges(renamer.changeURI(next, mainInd.getURI()));
        }


        // now add the remaining labels

        for (String lab : labels) {
            OWLDataPropertyAssertionAxiom ax = factory.getOWLDataPropertyAssertionAxiom(mainInd, factory.getOWLDataProperty(SKOSVocabulary.ALTLABEL), factory.getOWLUntypedConstant(lab));
            OWLDataPropertyAssertionAxiom ax1 = factory.getOWLDataPropertyAssertionAxiom(mainInd, factory.getOWLDataProperty(SKOSVocabulary.PREFLABEL), factory.getOWLUntypedConstant(term));
            AddAxiom addAx = new AddAxiom(onto, ax);
            AddAxiom addAx1 = new AddAxiom(onto, ax1);
            getOWLModelManager().applyChange(addAx);
            getOWLModelManager().applyChange(addAx1);
        }

        // remove any self referencing relationship

        for (OWLAxiom ax : new HashSet<OWLAxiom>(onto.getReferencingAxioms(mainInd))) {

            if (ax instanceof OWLObjectPropertyAssertionAxiom) {

                OWLPropertyAssertionAxiom rAx = (OWLPropertyAssertionAxiom) ax;
                if (rAx.getSubject().equals(rAx.getObject())) {

                    // remove this axiom

                    getOWLModelManager().applyChange(new RemoveAxiom(onto, ax));

                }

            }

        }



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
