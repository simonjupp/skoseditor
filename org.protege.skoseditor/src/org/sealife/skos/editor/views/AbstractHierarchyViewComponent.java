package org.sealife.skos.editor.views;

import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.protege.editor.owl.ui.OWLObjectComparatorAdapter;
import org.protege.editor.owl.ui.tree.OWLModelManagerTree;
import org.protege.editor.owl.ui.view.ChangeListenerMediator;
import org.protege.editor.owl.ui.view.Findable;
import org.protege.editor.owl.ui.view.individual.AbstractOWLIndividualViewComponent;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObject;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
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
public abstract class AbstractHierarchyViewComponent  extends AbstractOWLIndividualViewComponent implements Findable {

    public boolean isIgnoreSelected() {
        return ignoreSelected;
    }

    public void setIgnoreSelected(boolean ignoreSelected) {
        this.ignoreSelected = ignoreSelected;
    }

    private boolean ignoreSelected = false;

    private OWLModelManagerTree<OWLNamedIndividual> tree;

    private ChangeListenerMediator mediator = new ChangeListenerMediator();

    private AbstractSKOSHierarchyProvider provider;

    private OWLModelManagerListener modelListener;

    private TreeSelectionListener treeListener;


    protected AbstractSKOSHierarchyProvider getProvider () {
        return provider;
    }

    protected TreeSelectionListener getTreeListener() {
        return treeListener;
    }

    protected abstract AbstractSKOSHierarchyProvider createProvider();

    protected abstract OWLModelManagerTree createTree();

    protected void transmitSelection() {
        ignoreSelected = true;
        getOWLWorkspace().getOWLSelectionModel().setSelectedEntity(tree.getSelectedOWLObject());
        mediator.fireStateChanged(this);
    }

    public boolean canPerformAction() {
        return !tree.getSelectedOWLObjects().isEmpty();
    }

    public void addChangeListener(ChangeListener listener) {
        mediator.addChangeListener(listener);
    }

    public void removeChangeListener(ChangeListener listener) {
        mediator.removeChangeListener(listener);
    }

    public Set<OWLNamedIndividual> getSelectedIndividuals() {
        Set<OWLNamedIndividual> inds = new HashSet<OWLNamedIndividual>();
        for (Object obj : tree.getSelectedOWLObjects()) {
            inds.add((OWLNamedIndividual) obj);
        }
        return inds;
    }

    public OWLNamedIndividual updateView(OWLNamedIndividual selelectedIndividual) {
        if (selelectedIndividual != null && !ignoreSelected) {
            tree.setSelectedOWLObject(selelectedIndividual);
        }
        ignoreSelected = false;
        return selelectedIndividual;
    }

    public void initialiseIndividualsView() throws Exception {

        provider = createProvider();
//        provider.setOntologies(getOWLModelManager().getOntologies());

//        SKOSEntityRenderer rend = new SKOSEntityRenderer();
//        rend.setup(getOWLModelManager());
//        getOWLModelManager().setOWLEntityRenderer(rend);

    }

    public OWLModelManagerTree<OWLNamedIndividual> createTree(AbstractSKOSHierarchyProvider provider) {
        tree = new OWLModelManagerTree<OWLNamedIndividual> (getOWLEditorKit(), provider);

        final Comparator<OWLObject> comp = getOWLModelManager().getOWLObjectComparator();
        getTree().setOWLObjectComparator(new OWLObjectComparatorAdapter<OWLObject>(comp) {
            public int compare(OWLNamedIndividual o1, OWLNamedIndividual o2) {
               return super.compare(o1, o2);

            }
        });
        return tree;
    }



    protected void showTree () {
        setLayout(new BorderLayout());
        add(new JScrollPane(tree));
    }

    public java.util.List find(String match) {
        return new ArrayList<OWLNamedIndividual>(getOWLModelManager().getOWLEntityFinder().getMatchingOWLIndividuals(match));
    }

    public void show(OWLEntity owlEntity) {
        tree.setSelectedOWLObject((OWLNamedIndividual) owlEntity);
    }

    public OWLModelManagerTree<OWLNamedIndividual> getTree() {
        return tree;
    }

    public void disposeView() {
        tree.dispose();
        provider.dispose();
        getOWLModelManager().removeListener(modelListener);
    }
}
