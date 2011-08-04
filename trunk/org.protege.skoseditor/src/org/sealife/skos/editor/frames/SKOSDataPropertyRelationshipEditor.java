package org.sealife.skos.editor.frames;

import org.protege.editor.core.ui.util.InputVerificationStatusChangedListener;
import org.protege.editor.core.ui.util.VerifiedInputEditor;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.editor.AbstractOWLObjectEditor;
import org.protege.editor.owl.ui.editor.OWLConstantEditor;
import org.protege.editor.owl.ui.frame.OWLDataPropertyConstantPair;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLPropertyAssertionAxiom;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
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
 * Date: 20-May-2007<br><br>
 */
public class SKOSDataPropertyRelationshipEditor extends AbstractOWLObjectEditor<OWLDataPropertyConstantPair> implements VerifiedInputEditor {


    private OWLConstantEditor constantEditorComponent;

//    private OWLDataPropertySelectorPanel dataPropertySelectorPanel;

    private JPanel componentHolder;

    private java.util.List<InputVerificationStatusChangedListener> listeners = new ArrayList<InputVerificationStatusChangedListener>();

    private boolean currentStatus = false;

//    private ChangeListener changeListener = new ChangeListener(){
//        public void stateChanged(ChangeEvent event) {
//            checkStatus();
//        }
//    };

    private OWLDataProperty dataProperty;


    public SKOSDataPropertyRelationshipEditor(OWLEditorKit owlEditorKit, OWLDataProperty prop) {
        this.dataProperty = prop;
        final Border paddingBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);

        constantEditorComponent = new OWLConstantEditor(owlEditorKit);
        constantEditorComponent.setBorder(paddingBorder);

        componentHolder = new JPanel(new BorderLayout());
        componentHolder.add(constantEditorComponent, BorderLayout.CENTER);
    }


    public void setDataPropertyAxiom(OWLPropertyAssertionAxiom<OWLDataPropertyExpression, OWLLiteral> ax) {
        constantEditorComponent.setEditedObject(ax.getObject());
    }


    public String getEditorTypeName() {
        return null;
    }

    public boolean canEdit(Object object) {
        return false;
    }

    public JComponent getEditorComponent() {
        return componentHolder;
    }


    public OWLDataPropertyConstantPair getEditedObject() {
        OWLDataProperty prop = dataProperty;
        if (prop == null) {
            return null;
        }
        OWLLiteral con = constantEditorComponent.getEditedObject();
        if (con == null) {
            return null;
        }
        return new OWLDataPropertyConstantPair(prop, con);
    }

    public boolean setEditedObject(OWLDataPropertyConstantPair editedObject) {
        return false;
    }




    public void dispose() {
//        dataPropertySelectorPanel.dispose();
        listeners.clear();
    }

    public void addStatusChangedListener(InputVerificationStatusChangedListener listener) {
//        listeners.add(listener);
//        listener.verifiedStatusChanged(currentStatus);
    }


    public void removeStatusChangedListener(InputVerificationStatusChangedListener listener) {
//        listeners.remove(listener);
    }
}
