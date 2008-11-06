package org.sealife.skos.editor.views;

import org.protege.editor.owl.model.event.OWLModelManagerListener;
import org.protege.editor.owl.ui.tree.OWLModelManagerTree;
import org.protege.editor.owl.ui.view.AbstractOWLIndividualViewComponent;
import org.protege.editor.owl.ui.view.ChangeListenerMediator;
import org.protege.editor.owl.ui.view.Findable;
import org.protege.editor.owl.ui.OWLObjectComparatorAdapter;
import org.semanticweb.owl.model.OWLEntity;
import org.semanticweb.owl.model.OWLIndividual;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Comparator;
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

    private OWLModelManagerTree<OWLIndividual> tree;

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

    public Set<OWLIndividual> getSelectedIndividuals() {
        Set<OWLIndividual> inds = new HashSet<OWLIndividual>();
        for (Object obj : tree.getSelectedOWLObjects()) {
            inds.add((OWLIndividual) obj);
        }
        return inds;
    }

    protected OWLIndividual updateView(OWLIndividual selelectedIndividual) {
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

    public OWLModelManagerTree<OWLIndividual> createTree(AbstractSKOSHierarchyProvider provider) {
        tree = new OWLModelManagerTree<OWLIndividual> (getOWLEditorKit(), provider);
                final Comparator<OWLIndividual> comp = getOWLModelManager().getOWLObjectComparator();
        getTree().setOWLObjectComparator(new OWLObjectComparatorAdapter<OWLIndividual>(comp) {
            public int compare(OWLIndividual o1, OWLIndividual o2) {
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
        return new ArrayList<OWLIndividual>(getOWLModelManager().getEntityFinder().getMatchingOWLIndividuals(match));
    }

    public void show(OWLEntity owlEntity) {
        tree.setSelectedOWLObject((OWLIndividual) owlEntity);
    }

    public OWLModelManagerTree<OWLIndividual> getTree() {
        return tree;
    }

    public void disposeView() {
        tree.dispose();
        provider.dispose();
        getOWLModelManager().removeListener(modelListener);
    }
}
