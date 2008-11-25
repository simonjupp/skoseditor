package org.sealife.skos.editor.views;

import org.protege.editor.core.ui.RefreshableComponent;
import org.protege.editor.core.ui.view.DisposableAction;
import org.protege.editor.owl.model.entity.OWLEntityCreationSet;
import org.protege.editor.owl.model.event.EventType;
import org.protege.editor.owl.model.event.OWLModelManagerChangeEvent;
import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.protege.editor.owl.ui.OWLIcons;
import org.protege.editor.owl.ui.list.OWLObjectList;
import org.protege.editor.owl.ui.view.*;
import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.OWLEntityCollector;
import org.semanticweb.owl.util.OWLEntityRemover;
import org.semanticweb.owl.util.OWLEntitySetProvider;
import org.semanticweb.skos.SKOSConceptScheme;
import org.semanticweb.skos.SKOSDataFactory;
import org.semanticweb.skos.SKOSDataset;
import org.semanticweb.skosapibinding.SKOSManager;
import org.semanticweb.skosapibinding.SKOStoOWLConverter;
import uk.ac.manchester.cs.skos.SKOSRDFVocabulary;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
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
 * Date: 29-Jan-2007<br>
 * <br>
 * <p/> This definitely needs a rethink - it is a totally inefficient hack!
 */
public class SKOSConceptSchemeListViewComponent extends
        AbstractOWLIndividualViewComponent implements Findable<OWLIndividual>,
        Deleteable, CreateNewTarget, RefreshableComponent {
	private OWLObjectList<OWLIndividual> list;
	private OWLOntologyChangeListener listener;
	private ChangeListenerMediator changeListenerMediator;
	private Set<OWLIndividual> individualsInList;
	private OWLModelManagerListener modelManagerListener;
	private int selectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;

    SKOSManager skosManager;
    SKOSDataFactory skosDataFactory;

    @Override
	public void initialiseIndividualsView() throws Exception {

        skosManager = new SKOSManager(getOWLEditorKit().getModelManager().getOWLOntologyManager());
        skosDataFactory = skosManager.getSKOSDataFactory();

        this.list = new OWLObjectList<OWLIndividual>(this.getOWLEditorKit());
		this.list.setSelectionMode(this.selectionMode);
		this.setLayout(new BorderLayout());
		this.add(new JScrollPane(this.list));
		this.list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					if (SKOSConceptSchemeListViewComponent.this.list
							.getSelectedValue() != null) {
						SKOSConceptSchemeListViewComponent.this
								.setSelectedEntity((OWLIndividual) SKOSConceptSchemeListViewComponent.this.list
										.getSelectedValue());
					}
					SKOSConceptSchemeListViewComponent.this.changeListenerMediator
							.fireStateChanged(SKOSConceptSchemeListViewComponent.this);
				}
			}
		});
		this.list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				SKOSConceptSchemeListViewComponent.this
						.setSelectedEntity((OWLIndividual) SKOSConceptSchemeListViewComponent.this.list
								.getSelectedValue());
			}
		});
		this.listener = new OWLOntologyChangeListener() {
			public void ontologiesChanged(
					java.util.List<? extends OWLOntologyChange> changes) {
				SKOSConceptSchemeListViewComponent.this.processChanges(changes);
			}
		};
		this.getOWLModelManager().addOntologyChangeListener(this.listener);

        setupActions();
		this.changeListenerMediator = new ChangeListenerMediator();
		this.individualsInList = new TreeSet<OWLIndividual>(getOWLModelManager().getOWLObjectComparator());
		this.refill();
		this.modelManagerListener = new OWLModelManagerListener() {
			public void handleChange(OWLModelManagerChangeEvent event) {
				if (event.isType(EventType.ACTIVE_ONTOLOGY_CHANGED)) {
					SKOSConceptSchemeListViewComponent.this.refill();
				}
			}
		};
		this.getOWLModelManager().addListener(this.modelManagerListener);
	}


    protected void setupActions() {
        this.addAction(new SKOSConceptSchemeListViewComponent.AddIndividualAction(), "A", "A");
		this.addAction(new DeleteIndividualAction(this.getOWLEditorKit(),
				new OWLEntitySetProvider<OWLIndividual>() {
					public Set<OWLIndividual> getEntities() {
						return SKOSConceptSchemeListViewComponent.this
								.getSelectedIndividuals();
					}
				}), "B", "A");
    }


    @Override
	public void refreshComponent() {
		this.refill();
	}

	private void refill() {
		// Initial fill
		this.individualsInList.clear();

        SKOStoOWLConverter conv = new SKOStoOWLConverter();

        for (SKOSDataset dataset :  skosManager.getSKOSDataSets()) {
            for (SKOSConceptScheme con : dataset.getSKOSConceptSchemes()) {
                this.individualsInList.add(conv.getAsOWLIndiviudal(con));
            }
		}
		this.reset();
	}

    protected Set<OWLOntology> getOntologies() {
        return getOWLModelManager().getActiveOntologies();
    }

    public void setSelectedIndividual(OWLIndividual individual) {
		this.list.setSelectedValue(individual, true);
	}

	private void reset() {
		this.list.setListData(this.individualsInList.toArray());
		OWLEntity entity = this.getSelectedOWLIndividual();
		if (entity instanceof OWLIndividual) {
			this.list.setSelectedValue(entity, true);
		}
	}

	@Override
	protected OWLIndividual updateView(OWLIndividual selelectedIndividual) {
		if (!this.isPinned()) {
			this.list.setSelectedValue(selelectedIndividual, true);
		}
		return (OWLIndividual) this.list.getSelectedValue();
	}

	@Override
	public void disposeView() {
        this.getOWLModelManager().removeOntologyChangeListener(this.listener);
		this.getOWLModelManager().removeListener(this.modelManagerListener);
	}

	public OWLIndividual getSelectedIndividual() {
		return (OWLIndividual) this.list.getSelectedValue();
	}

	public Set<OWLIndividual> getSelectedIndividuals() {
		Set<OWLIndividual> inds = new HashSet<OWLIndividual>();
		for (Object obj : this.list.getSelectedValues()) {
			inds.add((OWLIndividual) obj);
		}
		return inds;
	}

	private void processChanges(java.util.List<? extends OWLOntologyChange> changes) {
		OWLEntityCollector addedCollector = new OWLEntityCollector();
		OWLEntityCollector removedCollector = new OWLEntityCollector();
		for (OWLOntologyChange chg : changes) {
			if (chg.isAxiomChange()) {
				OWLAxiomChange axChg = (OWLAxiomChange) chg;
				if (axChg instanceof AddAxiom) {
					axChg.getAxiom().accept(addedCollector);
				} else {
					axChg.getAxiom().accept(removedCollector);
				}
			}
		}
		boolean mod = false;
		for (OWLEntity ent : addedCollector.getObjects()) {
			if (ent instanceof OWLIndividual) {

                for (SKOSDataset dataset: skosManager.getSKOSDataSets()) {
                    for (SKOSConceptScheme con : dataset.getSKOSConceptSchemes()) {
                        if (con.getURI().equals(ent.getURI())) {
                            if (this.individualsInList.add((OWLIndividual) ent)) {
                                mod = true;
                            }
                        }
                    }
                }

			}
		}
		for (OWLEntity ent : removedCollector.getObjects()) {
			if (ent instanceof OWLIndividual) {
				boolean stillReferenced = false;
				for (OWLOntology ont : getOntologies()) {
					if (ont.containsIndividualReference(ent.getURI())) {
						stillReferenced = true;
						break;
					}
				}
				if (!stillReferenced) {
					if (this.individualsInList.remove(ent)) {
						mod = true;
					}
				}
			}
		}
		if (mod) {
			this.reset();
		}
	}

	private void addIndividual() {

        OWLEntityCreationSet<OWLIndividual> set = this.getOWLWorkspace().createOWLIndividual();
		if (set == null) {
			return;
		}
		java.util.List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
		changes.addAll(set.getOntologyChanges());
        OWLClass conceptScheme = getOWLEditorKit().getModelManager().getOWLDataFactory().getOWLClass(SKOSRDFVocabulary.CONCEPTSCHEME.getURI());
        OWLAxiom ax = getOWLEditorKit().getModelManager().getOWLDataFactory().getOWLClassAssertionAxiom(set.getOWLEntity(), conceptScheme);
        changes.add(new AddAxiom(getOWLModelManager().getActiveOntology(), ax));
        
        this.getOWLModelManager().applyChanges(changes);
		OWLIndividual ind = set.getOWLEntity();
		if (ind != null) {
			this.list.setSelectedValue(ind, true);
			if (!this.isPinned()) {
				this.getOWLWorkspace().getOWLSelectionModel()
						.setSelectedEntity(ind);
			}
		}
	}

	public java.util.List<OWLIndividual> find(String match) {
        return new ArrayList<OWLIndividual>(getOWLModelManager().getEntityFinder().getMatchingOWLIndividuals(match));
	}

	public void show(OWLIndividual owlEntity) {
		list.setSelectedValue(owlEntity, true);
	}


    public void setSelectedIndividuals(Set<OWLIndividual> individuals) {
        list.setSelectedValues(individuals, true);
    }


    private class AddIndividualAction extends DisposableAction {
		public AddIndividualAction() {
			super("Add concept scheme", OWLIcons.getIcon("individual.add.png"));
		}

		public void actionPerformed(ActionEvent e) {
			SKOSConceptSchemeListViewComponent.this.addIndividual();
		}

		@Override
		public void dispose() {
        }
	}

	public void addChangeListener(ChangeListener listener) {
		this.changeListenerMediator.addChangeListener(listener);
	}

	public void removeChangeListener(ChangeListener listener) {
		this.changeListenerMediator.removeChangeListener(listener);
	}

	public void handleDelete() {
		OWLEntityRemover entityRemover = new OWLEntityRemover(this
				.getOWLModelManager().getOWLOntologyManager(), this
				.getOWLModelManager().getOntologies());
		for (OWLIndividual ind : this.getSelectedIndividuals()) {
			ind.accept(entityRemover);
		}
		this.getOWLModelManager().applyChanges(entityRemover.getChanges());
	}

	public boolean canDelete() {
		return !this.getSelectedIndividuals().isEmpty();
	}

	public boolean canCreateNew() {
		return true;
	}

	public void createNewObject() {
		this.addIndividual();
	}

	public void setSelectionMode(int selectionMode) {
		this.selectionMode = selectionMode;
		if (this.list != null) {
			this.list.setSelectionMode(selectionMode);
		}
	}
}
