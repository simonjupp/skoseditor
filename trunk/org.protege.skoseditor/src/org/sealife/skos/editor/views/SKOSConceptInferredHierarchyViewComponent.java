package org.sealife.skos.editor.views;

import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerChangeEvent;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.protege.editor.owl.ui.tree.OWLModelManagerTree;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
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
 * Date: Oct 9, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class SKOSConceptInferredHierarchyViewComponent extends AbstractHierarchyViewComponent {

    private OWLModelManagerListener modelListener;

    protected AbstractSKOSHierarchyProvider createProvider() {
        return new SKOSConceptInferredHierarchyProvider(getOWLModelManager(), getOWLModelManager().getReasoner());
    }

    protected OWLModelManagerTree createTree() {
        return new OWLModelManagerTree<OWLNamedIndividual>(getOWLEditorKit(), getProvider());
    }

    public void initialiseIndividualsView() throws Exception {
        setIgnoreSelected(true);
        super.initialiseIndividualsView();

        createTree(getProvider());

        // todo check this is a valid check to see if the reasoner is classified!
        if (getOWLModelManager().getOWLReasonerManager().getReasonerStatus().isEnableInitialization()) {
            setIgnoreSelected(false);
            getTree().getSelectionModel().addTreeSelectionListener( new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {
                    transmitSelection();
                }
            });

            SKOSConceptInferredHierarchyProvider infProv = (SKOSConceptInferredHierarchyProvider) getProvider();
            infProv.reasonerUpdated();
        }


        // add a listener for classification
        getOWLModelManager().addListener(modelListener = new OWLModelManagerListener() {


            public void handleChange(OWLModelManagerChangeEvent event) {

                if (event.isType(EventType.ONTOLOGY_CLASSIFIED)) {
//                    createTree(getProvider());
                    setIgnoreSelected(false);
                    getTree().getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
                        public void valueChanged(TreeSelectionEvent e) {
                            transmitSelection();
                        }
                    });

                    SKOSConceptInferredHierarchyProvider infProv = (SKOSConceptInferredHierarchyProvider) getProvider();
                    infProv.reasonerUpdated();
                }
            }
        });
        getTree().setBackground(new Color(255,255,215));

        showTree();
    }


    public void disposeView() {
        getOWLModelManager().removeListener(modelListener);
        super.disposeView();
    }

}
