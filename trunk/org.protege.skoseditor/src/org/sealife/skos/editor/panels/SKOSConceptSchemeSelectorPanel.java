package org.sealife.skos.editor.panels;

import org.protege.editor.core.ui.view.ViewComponent;
import org.protege.editor.core.ui.view.ViewComponentPlugin;
import org.protege.editor.core.ui.view.ViewComponentPluginAdapter;
import org.protege.editor.core.ui.workspace.Workspace;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.UIHelper;
import org.protege.editor.owl.ui.renderer.OWLSystemColors;
import org.protege.editor.owl.ui.selector.AbstractSelectorPanel;
import org.sealife.skos.editor.views.SKOSConceptSchemeListViewComponent;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLOntology;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
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
 * Medical Informatics Group<br>
 * Date: 05-Jul-2006<br>
 * <br>
 * <p/> matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br>
 * <br>
 */
public class SKOSConceptSchemeSelectorPanel extends AbstractSelectorPanel<OWLNamedIndividual> {

    private SKOSConceptSchemeListViewComponent viewComponent;

    private Set<OWLOntology> ontologies;

    public SKOSConceptSchemeSelectorPanel(OWLEditorKit eKit) {
        this(eKit, true);
    }

    public SKOSConceptSchemeSelectorPanel(OWLEditorKit eKit, boolean editable) {
        this(eKit, editable, eKit.getModelManager().getActiveOntologies());
    }

    public SKOSConceptSchemeSelectorPanel(OWLEditorKit eKit, boolean editable, Set<OWLOntology> ontologies) {
        this(eKit, editable, ontologies, ListSelectionModel.SINGLE_SELECTION);
    }

    public SKOSConceptSchemeSelectorPanel(OWLEditorKit eKit, int selectionMode) {
        this(eKit, true, null, selectionMode);
    }

    /**
     * Builds an OWLIndividualSelectorPanel with the input selection mode. The
     * valid values are the same described in the constants in
     * javax.swing.ListSelectionModel (the default is
     * ListSelectionModel.SINGLE_SELECTION)
     *
     * @param eKit
     * @param selectionMode
     */
    public SKOSConceptSchemeSelectorPanel(OWLEditorKit eKit, boolean editable, Set<OWLOntology> ontologies, int selectionMode) {
        super(eKit, editable, false);
        this.ontologies = ontologies;
        createUI();
        this.viewComponent.setSelectionMode(selectionMode);
    }

    public static OWLNamedIndividual showDialog(OWLEditorKit owlEditorKit) {
        SKOSConceptSchemeSelectorPanel panel = new SKOSConceptSchemeSelectorPanel(owlEditorKit, true, owlEditorKit.getModelManager().getOntologies(), ListSelectionModel.SINGLE_SELECTION);

        int ret = new UIHelper(owlEditorKit).showDialog("Create a new SKOS Concept Scheme", panel, panel.viewComponent);

        if (ret == JOptionPane.OK_OPTION) {
            OWLNamedIndividual ind = panel.getSelectedObject();
            panel.viewComponent.dispose();
            return ind;
        }
        else {
            panel.viewComponent.dispose();
            return null;
        }
    }

    public void setSelection(OWLNamedIndividual ind) {
        if (viewComponent.getView() != null) {
            viewComponent.getView().setPinned(false);
        }
        viewComponent.setSelectedIndividual(ind);
    }


    public void setSelection(Set<OWLNamedIndividual> entities) {
        viewComponent.setSelectedIndividuals(entities);
    }


    public OWLNamedIndividual getSelectedObject() {
        return viewComponent.getSelectedIndividual();
    }

    public Set<OWLNamedIndividual> getSelectedObjects() {
        return viewComponent.getSelectedIndividuals();
    }


    public void dispose() {
        viewComponent.dispose();
    }

    protected ViewComponentPlugin getViewComponentPlugin() {
        return new ViewComponentPluginAdapter() {
            public String getLabel() {
                return "Individuals";
            }

            public Workspace getWorkspace() {
                return getOWLEditorKit().getWorkspace();
            }

            public ViewComponent newInstance() throws ClassNotFoundException,
                    IllegalAccessException, InstantiationException {
                viewComponent = new SKOSConceptSchemeListViewComponent(){
                    protected void setupActions() {
                        if (isEditable()){
                            super.setupActions();
                        }
                    }

                    protected Set<OWLOntology> getOntologies() {
                        if (ontologies != null){
                            return ontologies;
                        }
                        return super.getOntologies();
                    }
                };
                viewComponent.setup(this);
                return viewComponent;
            }

            public Color getBackgroundColor() {
                return OWLSystemColors.getOWLIndividualColor();
            }
        };
    }

    public void setOntologies(Set<OWLOntology> ontologies) {

    }

    public void addSelectionListener(ChangeListener listener) {
        viewComponent.addChangeListener(listener);
    }

    public void removeSelectionListener(ChangeListener listener) {
        viewComponent.removeChangeListener(listener);
    }

    /**
     * @deprecated Use <code>getSelectedObject</code>
     * @return
     */
    public OWLNamedIndividual getSelectedIndividual() {
        return getSelectedObject();
    }

    /**
     * @deprecated Use <code>getSelectedObjects</code>
     * @return
     */
    public Set<OWLNamedIndividual> getSelectedIndividuals() {
        return getSelectedObjects();
    }

    /**
     * @deprecated Use <code>setSelection</code>
     * @param ind
     */
    public void setSelectedIndividual(OWLNamedIndividual ind) {
        setSelection(ind);
    }
}
