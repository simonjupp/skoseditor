package org.sealife.skos.editor.views;

import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerChangeEvent;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.protege.editor.owl.ui.tree.OWLModelManagerTree;
import org.sealife.skos.editor.SKOSVocabulary;
import org.sealife.skos.editor.panels.ConceptSchemeComboBox;
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLOntology;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
 * Date: Oct 9, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class SKOSConceptSchemeInferredHierarchyViewComponent extends AbstractHierarchyViewComponent {

    private OWLModelManagerListener modelListener;


    private JComboBox schemaBox;

    private SKOSInSchemeInferredHierarchyProvider provider;

    protected AbstractSKOSHierarchyProvider createProvider() {
        provider = new SKOSInSchemeInferredHierarchyProvider(getOWLModelManager(), getOWLModelManager().getReasoner());
        return provider;
    }

    protected OWLModelManagerTree createTree() {
        return new OWLModelManagerTree<OWLIndividual>(getOWLEditorKit(), getProvider());
    }

    public OWLIndividual getSelectedSchema () {
        
        return (OWLIndividual) schemaBox.getSelectedItem();
    }

    public Set<OWLIndividual> getConceptSchemes () {

        Set<OWLIndividual> inds = new HashSet<OWLIndividual>(10);

        for (OWLOntology onto  : getOWLEditorKit().getModelManager().getOntologies()) {
            Set<OWLClassAssertionAxiom> axioms = onto.getClassAssertionAxioms(getOWLEditorKit().getModelManager().getOWLDataFactory().getOWLClass(SKOSVocabulary.CONCEPTSCHEME.getURI()));
            for (OWLClassAssertionAxiom axiom : axioms) {
                inds.add(axiom.getIndividual());
            }

        }
        return inds;
    }

    public void initialiseIndividualsView() throws Exception {

        setIgnoreSelected(true);
        super.initialiseIndividualsView();


        schemaBox = ConceptSchemeComboBox.getConceptSchemeComboBox(getOWLEditorKit());
        schemaBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {

                if (provider.isClassified()) {
//                    System.err.println("new schema selected: " + getSelectedSchema().getURI().getFragment());
                    provider.setConceptSchema(getSelectedSchema());
                    provider.reasonerUpdated();
//                    SKOSInSchemeInferredHierarchyProvider infProv = (SKOSInSchemeInferredHierarchyProvider) getProvider();
//                    infProv.reasonerUpdated();
                }
            }
        });

        createTree(getProvider());

        if (getOWLModelManager().getReasoner().isClassified()) {
            setIgnoreSelected(true);
            getTree().getSelectionModel().addTreeSelectionListener( new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {
                    transmitSelection();
                }
            });

            SKOSInSchemeInferredHierarchyProvider infProv = (SKOSInSchemeInferredHierarchyProvider) getProvider();
            infProv.reasonerUpdated();
        }

        getOWLModelManager().addListener(modelListener = new OWLModelManagerListener() {


            public void handleChange(OWLModelManagerChangeEvent event) {

                if (event.isType(EventType.ONTOLOGY_CLASSIFIED)) {
//                    createTree(getProvider());
                    setIgnoreSelected(true);
                    getTree().getSelectionModel().addTreeSelectionListener( new TreeSelectionListener() {
                        public void valueChanged(TreeSelectionEvent e) {
                            transmitSelection();
                        }
                    });

                    provider.setConceptSchema(getSelectedSchema());
                    provider.reasonerUpdated();
//                    SKOSInSchemeInferredHierarchyProvider infProv = (SKOSInSchemeInferredHierarchyProvider) getProvider();
//                    infProv.reasonerUpdated();
                }
                if (event.isType(EventType.ONTOLOGY_CREATED) | event.isType(EventType.ONTOLOGY_LOADED)) {
                    schemaBox.removeAllItems();
                    for (Object ind : getConceptSchemes().toArray()) {
                        schemaBox.addItem(ind);
                    }
                }
            }
        });
        getTree().setBackground(new Color(255,255,215));

        showTree();
    }


    public void disposeView() {
        getOWLModelManager().removeListener(modelListener);
        super.disposeView();    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected void showTree() {
        setLayout(new BorderLayout(4,4));
        add (schemaBox, BorderLayout.NORTH);
        add(new JScrollPane(getTree()), BorderLayout.CENTER);
    }
}
