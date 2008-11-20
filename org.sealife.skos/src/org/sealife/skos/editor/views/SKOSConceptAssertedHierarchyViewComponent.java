package org.sealife.skos.editor.views;

import org.protege.editor.core.ui.util.JOptionPaneEx;
import org.protege.editor.core.ui.view.DisposableAction;
import org.protege.editor.owl.model.entity.OWLEntityCreationSet;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerChangeEvent;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.protege.editor.owl.ui.OWLIcons;
import org.protege.editor.owl.ui.frame.OWLDataPropertyConstantPair;
import org.protege.editor.owl.ui.frame.OWLDataPropertyRelationshipEditor;
import org.protege.editor.owl.ui.frame.OWLObjectPropertyIndividualPair;
import org.protege.editor.owl.ui.frame.OWLObjectPropertyIndividualPairEditor;
import org.protege.editor.owl.ui.tree.OWLModelManagerTree;
import org.protege.editor.owl.ui.tree.OWLTreeDragAndDropHandler;
import org.protege.editor.owl.ui.view.CreateNewTarget;
import org.protege.editor.owl.ui.view.DeleteIndividualAction;
import org.protege.editor.owl.ui.view.Deleteable;
import org.sealife.skos.editor.SKOSIcons;
import org.sealife.skos.editor.SKOSVocabulary;
import org.sealife.skos.editor.panels.SKOSConceptSelectorPanel;
import org.sealife.skos.editor.panels.SKOSEntityCreationPanel;
import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.OWLEntitySetProvider;
import org.semanticweb.skos.SKOSConcept;
import org.semanticweb.skos.SKOSDataFactory;
import org.semanticweb.skosapibinding.SKOSManager;
import uk.ac.manchester.cs.skos.SKOSObjectRelationAssertionImpl;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
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
public class SKOSConceptAssertedHierarchyViewComponent extends AbstractHierarchyViewComponent implements Deleteable, CreateNewTarget {

    private OWLModelManagerListener modelListener;

    private AddNarrowerConceptAction addNarrowerConceptAction;

    private AddBroaderConceptAction addBroaderConceptAction;

    private AddSKOSConceptAction addSKOSConceptAction;

    private AddRelatedConceptAction addRelatedConceptAction;

    private AddRelatedDataPropertyAction addRelatedDataPropertyAction;

    private SKOSManager skosManager;

    private SKOSDataFactory skosFactory;

    protected AbstractSKOSHierarchyProvider createProvider() {
        return new SKOSConceptAssertedHierarchyProvider(getOWLModelManager().getOWLOntologyManager());
    }

    protected OWLModelManagerTree createTree() {
        return null;
    }


    public boolean canDelete() {
        return !getTree().getSelectedOWLObjects().isEmpty();
    }


    public void handleDelete() {
    }


    public void initialiseIndividualsView() throws Exception {

        super.initialiseIndividualsView();

        getProvider().setOntologies(getOWLModelManager().getOntologies());
        
        createTree(getProvider());

        getOWLModelManager().addListener(modelListener = new OWLModelManagerListener() {

            public void handleChange(OWLModelManagerChangeEvent event) {
                if (event.isType(EventType.ONTOLOGY_LOADED) || event.isType(EventType.ONTOLOGY_CREATED)) {
                    getProvider().setOntologies(getOWLModelManager().getOntologies());
                }
            }
        });



        skosManager = new SKOSManager(getOWLModelManager().getOWLOntologyManager());
        skosFactory = skosManager.getSKOSDataFactory();


        getTree().getSelectionModel().addTreeSelectionListener( new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                transmitSelection();
                addSKOSConceptAction.updateState();
                addRelatedConceptAction.updateState();
                addNarrowerConceptAction.updateState();
                addBroaderConceptAction.updateState();
                addRelatedDataPropertyAction.updateState();
            }
        });

        getTree().setAutoscrolls(true);
        getTree().setDragAndDropHandler(new OWLTreeDragAndDropHandler<OWLIndividual>() {


            public void move(OWLIndividual child, OWLIndividual fromParent, OWLIndividual toParent) {
                List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
                changes.add(new AddAxiom(getOWLModelManager().getActiveOntology(),
                                         getOWLModelManager().getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(child, getOWLModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.BROADER),
                                                    toParent)));
                changes.add(new RemoveAxiom(getOWLModelManager().getActiveOntology(),
                                            getOWLModelManager().getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(child, getOWLModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.BROADER),
                                                                                                         fromParent)));
                getOWLModelManager().applyChanges(changes);
            }

            public void add(OWLIndividual child, OWLIndividual parent) {
                List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
                changes.add(new AddAxiom(getOWLModelManager().getActiveOntology(),
                                         getOWLModelManager().getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(child, getOWLModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.BROADER),
                                                    parent)));

                getOWLModelManager().applyChanges(changes);
            }
        });

//        listener = new OWLOntologyChangeListener() {
//            public int hashCode() {
//                return super.hashCode();    //To change body of overridden methods use File | Settings | File Templates.
//            }
//
//            public void ontologiesChanged(List<? extends OWLOntologyChange> changes) {
////                System.err.println("the ontology has changed!");
//            }
//        };


        addSKOSConceptAction = new AddSKOSConceptAction(getProvider());
        addAction(addSKOSConceptAction, "A", "A");

        addBroaderConceptAction = new AddBroaderConceptAction(getProvider());
        addAction(addBroaderConceptAction, "B", "A");

        addNarrowerConceptAction = new AddNarrowerConceptAction(getProvider());
        addAction(addNarrowerConceptAction, "C", "A");

        addRelatedConceptAction = new AddRelatedConceptAction(getProvider());
        addAction(addRelatedConceptAction, "D", "A");

        addRelatedDataPropertyAction = new AddRelatedDataPropertyAction();
        addAction(addRelatedDataPropertyAction, "DD", "A");

        addAction(new DeleteIndividualAction(getOWLEditorKit(), new OWLEntitySetProvider<OWLIndividual>() {
                                                                          public Set getEntities() {
                                                                              return new HashSet<OWLIndividual>(getTree().getSelectedOWLObjects());
                                                                          }
                                                                      }), "E", "A");

        showTree();
    }


    public void disposeView() {
        getOWLModelManager().removeListener(modelListener);

        super.disposeView();
    }

    public boolean canCreateNew() {
        return true;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void createNewObject() {
        addConcept();
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addRelatedConcept() {

        //OWLEntityCreationSet<OWLIndividual> set = getOWLWorkspace().createOWLIndividual();
        OWLObjectPropertyIndividualPairEditor editor = new OWLObjectPropertyIndividualPairEditor(getOWLEditorKit());

        int ret = JOptionPaneEx.showConfirmDialog(getOWLEditorKit().getWorkspace(),
                                                  "Specify name",
                editor.getEditorComponent(),
                                                  JOptionPane.PLAIN_MESSAGE,
                                                  JOptionPane.OK_CANCEL_OPTION,
                                                  editor.getEditorComponent());
        if (ret == JOptionPane.OK_OPTION) {
            OWLIndividual selectedInd = getSelectedOWLIndividual();
            if (selectedInd == null) {
                return;
            }
            OWLObjectPropertyIndividualPair pair = editor.getEditedObject();

            OWLAxiom ax = getOWLModelManager().getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(selectedInd, pair.getProperty(), pair.getIndividual());
            this.getOWLModelManager().applyChange(new AddAxiom(getOWLModelManager().getActiveOntology(), ax));
        }
    }

    private void addRelatedDataProperty() {
        OWLDataPropertyRelationshipEditor editor = new OWLDataPropertyRelationshipEditor(getOWLEditorKit());

        int ret = JOptionPaneEx.showConfirmDialog(getOWLEditorKit().getWorkspace(),
                                                  "Specify name",
                editor.getEditorComponent(),
                                                  JOptionPane.PLAIN_MESSAGE,
                                                  JOptionPane.OK_CANCEL_OPTION,
                                                  editor.getEditorComponent());
        if (ret == JOptionPane.OK_OPTION) {
            OWLIndividual selectedInd = getSelectedOWLIndividual();
            if (selectedInd == null) {
                return;
            }
            OWLDataPropertyConstantPair pair = editor.getEditedObject();

            OWLAxiom ax = getOWLModelManager().getOWLDataFactory().getOWLDataPropertyAssertionAxiom(selectedInd, pair.getProperty(), pair.getConstant());
            this.getOWLModelManager().applyChange(new AddAxiom(getOWLModelManager().getActiveOntology(), ax));
        }
    }


    public void addConcept() {


        OWLEntityCreationSet<OWLIndividual> set = SKOSEntityCreationPanel.showDialog(getOWLEditorKit(), "Please enter a concept name", OWLIndividual.class);
		if (set == null) {
			return;
		}
		List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();

        // type it as a SKOS concept
        OWLIndividual ind = set.getOWLEntity();
		this.getOWLModelManager().applyChanges(set.getOntologyChanges());
		if (ind != null) {
			this.getOWLWorkspace().getOWLSelectionModel().setSelectedEntity(ind);
		}
    }

    public void addNarrowerConcept () {

        OWLIndividual relatedInd = SKOSConceptSelectorPanel.showDialog(getOWLEditorKit());

        OWLIndividual selected = getSelectedOWLIndividual();

        if (relatedInd == null) {
			return;
		}
		List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();

        // type it as a SKOS concept
        SKOSConcept concept = skosFactory.getSKOSConcept(relatedInd.getURI());
        SKOSConcept selectedConcept = skosFactory.getSKOSConcept(selected.getURI());

        SKOSObjectRelationAssertionImpl ass2 = (SKOSObjectRelationAssertionImpl) skosFactory.getSKOSObjectRelationAssertion(selectedConcept, skosFactory.getSKOSNarrowerProperty(), concept);

        changes.add(new AddAxiom(getOWLModelManager().getActiveOntology(), ass2.getAssertionAxiom()));
		this.getOWLModelManager().applyChanges(changes);
		if (relatedInd != null) {
			this.getOWLWorkspace().getOWLSelectionModel().setSelectedEntity(relatedInd);
		}

    }



    public void addBroaderConcept () {

        OWLIndividual relatedInd = SKOSConceptSelectorPanel.showDialog(getOWLEditorKit());
        
        OWLIndividual selected = getSelectedOWLIndividual();

        if (relatedInd == null) {
			return;
		}
		List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();

        // type it as a SKOS concept
        SKOSConcept concept = skosFactory.getSKOSConcept(relatedInd.getURI());
        SKOSConcept selectedConcept = skosFactory.getSKOSConcept(selected.getURI());

        SKOSObjectRelationAssertionImpl ass2 = (SKOSObjectRelationAssertionImpl) skosFactory.getSKOSObjectRelationAssertion(selectedConcept, skosFactory.getSKOSBroaderProperty(), concept);

        changes.add(new AddAxiom(getOWLModelManager().getActiveOntology(), ass2.getAssertionAxiom()));
		this.getOWLModelManager().applyChanges(changes);
		if (relatedInd != null) {
			this.getOWLWorkspace().getOWLSelectionModel().setSelectedEntity(relatedInd);
		}

    }


    private class AddSKOSConceptAction extends DisposableAction {

        AbstractSKOSHierarchyProvider provider;
        
        public AddSKOSConceptAction(AbstractSKOSHierarchyProvider provider) {
            super("Add Concept", OWLIcons.getIcon("individual.add.png"));
            this.provider = provider;
            updateState();
        }

        public void dispose() {
        }

        public void updateState() {
            setEnabled(true);
        }

        public void actionPerformed(ActionEvent actionEvent) {
            addConcept();
        }
    }

    private class AddBroaderConceptAction extends DisposableAction {

        AbstractSKOSHierarchyProvider provider;

        public AddBroaderConceptAction (AbstractSKOSHierarchyProvider provider) {
            super("Add Broader Concept", SKOSIcons.getIcon("broader.add.png"));
            this.provider = provider;
            updateState();
        }

        public void updateState() {
            setEnabled(false);
            OWLEntity ent = getOWLWorkspace().getOWLSelectionModel().getSelectedEntity();
            if (ent != null) {
                setEnabled(true);
            }
        }

        public void dispose() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            addBroaderConcept();
        }
    }

    private class AddNarrowerConceptAction extends DisposableAction {

        AbstractSKOSHierarchyProvider provider;

        public AddNarrowerConceptAction (AbstractSKOSHierarchyProvider provider) {
            super("Add Narrower Concept", SKOSIcons.getIcon("narrower.add.png"));
            this.provider = provider;
            updateState();
        }

        public void updateState() {
            setEnabled(false);
            OWLEntity ent = getOWLWorkspace().getOWLSelectionModel().getSelectedEntity();
            if (ent != null) {
                setEnabled(true);
            }
        }

        public void dispose() {
        }

        public void actionPerformed(ActionEvent actionEvent) {
            addNarrowerConcept();
        }
    }

    private class AddRelatedConceptAction extends DisposableAction {

        AbstractSKOSHierarchyProvider provider;

        public AddRelatedConceptAction(AbstractSKOSHierarchyProvider provider) {
            super("Add related Concept", SKOSIcons.getIcon("objectprop.add.png"));
            this.provider = provider;
            updateState();
        }

        public void actionPerformed(ActionEvent e) {
            addRelatedConcept();
        }

        public void updateState() {
            setEnabled(false);
            OWLEntity ent = getOWLWorkspace().getOWLSelectionModel().getSelectedEntity();
            if (ent != null) {
                setEnabled(true);
            }
        }


        public void dispose() {
        }
    }

    private class AddRelatedDataPropertyAction extends DisposableAction {

        public AddRelatedDataPropertyAction() {
            super("Add data relation", SKOSIcons.getIcon("dataprop.add.png"));
            updateState();
        }

        public void actionPerformed(ActionEvent e) {
            addRelatedDataProperty();
        }

        public void updateState() {
            setEnabled(false);
            OWLEntity ent = getOWLWorkspace().getOWLSelectionModel().getSelectedEntity();
            if (ent != null) {
                setEnabled(true);
            }
        }


        public void dispose() {
        }
    }



}
