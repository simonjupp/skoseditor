package org.sealife.skos.editor.frames;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.editor.AbstractOWLObjectEditor;
import org.protege.editor.owl.ui.frame.OWLObjectPropertyIndividualPair;
import org.protege.editor.owl.ui.selector.AbstractSelectorPanel;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;

import javax.swing.*;
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
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 22-Feb-2007<br>
 * <br>
 */
public class SKOSObjectPropertyIndividualPairEditor extends
        AbstractOWLObjectEditor<OWLObjectPropertyIndividualPair> {
	private JPanel editorPanel;
//	private OWLObjectPropertySelectorPanel objectPropertyPanel;
	private AbstractSelectorPanel individualSelectorPanel;

    private OWLObjectProperty objectProperty;

    public SKOSObjectPropertyIndividualPairEditor(OWLEditorKit owlEditorKit, OWLObjectProperty property, AbstractSelectorPanel panel) {
		this.objectProperty = property;
        this.editorPanel = new JPanel(new BorderLayout());
//		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
//		this.objectPropertyPanel = new OWLObjectPropertySelectorPanel(
//				owlEditorKit);
//		splitPane.setLeftComponent(this.objectPropertyPanel);
		this.individualSelectorPanel = panel;
//		splitPane.setRightComponent(this.individualSelectorPanel);
		this.editorPanel.add(individualSelectorPanel, BorderLayout.CENTER);
	}

	public void setObjectPropertyAxiom(
			OWLPropertyAssertionAxiom<OWLObjectPropertyExpression, OWLNamedIndividual> ax) {
//		OWLObjectPropertyExpression p = ax.getProperty();
//		if (p instanceof OWLObjectProperty) {
//			this.objectPropertyPanel.setSelection((OWLObjectProperty) p);
//		}
		this.individualSelectorPanel.setSelection(ax.getObject());
	}


	public OWLObjectPropertyIndividualPair getEditedObject() {
        OWLObjectPropertyIndividualPair pair = new OWLObjectPropertyIndividualPair(objectProperty, (OWLNamedIndividual) this.individualSelectorPanel.getSelectedObject());
        this.individualSelectorPanel.dispose();
        return pair;
	}

    public boolean setEditedObject(OWLObjectPropertyIndividualPair editedObject) {
        return false;
    }

//	@Override
//	public Set<OWLObjectPropertyIndividualPair> getEditedObjects() {
//		Set<OWLObjectPropertyIndividualPair> pairs = new HashSet<OWLObjectPropertyIndividualPair>();
//		for (OWLObjectProperty prop : this.objectPropertyPanel.getSelectedObjects()) {
//			for (OWLIndividual ind : this.individualSelectorPanel.getSelectedObjects()) {
//				pairs.add(new OWLObjectPropertyIndividualPair(prop, ind));
//			}
//		}
//		return pairs;
//	}

    public String getEditorTypeName() {
        return null;
    }

    public boolean canEdit(Object object) {
        return false;
    }

    public JComponent getEditorComponent() {
		return this.editorPanel;
	}

	public void dispose() {
//		this.objectPropertyPanel.dispose();
        this.individualSelectorPanel.dispose();
	}
}
