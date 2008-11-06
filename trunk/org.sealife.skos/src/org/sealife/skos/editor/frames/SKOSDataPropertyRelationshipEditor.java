package org.sealife.skos.editor.frames;

import org.protege.editor.core.ui.util.InputVerificationStatusChangedListener;
import org.protege.editor.core.ui.util.VerifiedInputEditor;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.frame.AbstractOWLFrameSectionRowObjectEditor;
import org.protege.editor.owl.ui.frame.OWLConstantEditor;
import org.protege.editor.owl.ui.frame.OWLDataPropertyConstantPair;
import org.semanticweb.owl.model.*;

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
 * Author: Matthew Horridge<br>
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 20-May-2007<br><br>
 */
public class SKOSDataPropertyRelationshipEditor extends AbstractOWLFrameSectionRowObjectEditor<OWLDataPropertyConstantPair> implements VerifiedInputEditor {


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

//        dataPropertySelectorPanel = new OWLDataPropertySelectorPanel(owlEditorKit);
//        dataPropertySelectorPanel.setBorder(BorderFactory.createCompoundBorder(paddingBorder,
//                                                                               BorderFactory.createTitledBorder("Data Property")));
//        dataPropertySelectorPanel.addSelectionListener(changeListener);

        constantEditorComponent = new OWLConstantEditor(owlEditorKit);
        constantEditorComponent.setBorder(paddingBorder);

        componentHolder = new JPanel(new BorderLayout());
//        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
//        splitPane.setLeftComponent(dataPropertySelectorPanel);
//        splitPane.setRightComponent(constantEditorComponent);
        componentHolder.add(constantEditorComponent, BorderLayout.CENTER);
    }


    public void setDataPropertyAxiom(OWLPropertyAssertionAxiom<OWLDataPropertyExpression, OWLConstant> ax) {
        OWLDataPropertyExpression p = ax.getProperty();
//        if (p instanceof OWLDataProperty){
//            dataPropertySelectorPanel.setSelection((OWLDataProperty)p);
//        }
//        System.err.println("shoul be here: " + ax.getObject().getLiteral());
        constantEditorComponent.setEditedObject(ax.getObject());
    }


    public JComponent getEditorComponent() {
        return componentHolder;
    }


    public OWLDataPropertyConstantPair getEditedObject() {
        OWLDataProperty prop = dataProperty;
        if (prop == null) {
            return null;
        }
        OWLConstant con = constantEditorComponent.getEditedObject();
        if (con == null) {
            return null;
        }
        return new OWLDataPropertyConstantPair(prop, con);
    }


    public void clear() {
        constantEditorComponent.setEditedObject(null);
    }


    public void dispose() {
//        dataPropertySelectorPanel.dispose();
        listeners.clear();
    }


    private void checkStatus(){
//        boolean status = constantEditorComponent.getEditedObject() != null;
//        if (status != currentStatus){
//            currentStatus = status;
//            for (InputVerificationStatusChangedListener l : listeners){
//                l.verifiedStatusChanged(currentStatus);
//            }
//        }
    }


    public void addStatusChangedListener(InputVerificationStatusChangedListener listener) {
//        listeners.add(listener);
//        listener.verifiedStatusChanged(currentStatus);
    }


    public void removeStatusChangedListener(InputVerificationStatusChangedListener listener) {
//        listeners.remove(listener);
    }
}
