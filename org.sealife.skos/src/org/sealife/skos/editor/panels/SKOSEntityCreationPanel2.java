package org.sealife.skos.editor.panels;

import org.protege.editor.core.ui.util.JOptionPaneEx;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.renderer.OWLCellRenderer;
import org.protege.editor.owl.ui.selector.OWLObjectPropertySelectorPanel;
import org.sealife.skos.editor.SKOSVocabulary;
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import org.semanticweb.owl.model.OWLOntology;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.util.HashSet;
import java.util.Iterator;
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
*
*
/**
* Author: Simon Jupp<br>
* Date: Jul 4, 2007<br>
* The University of Manchester<br>
* Bio-Health Informatics Group<br>
*/
public class SKOSEntityCreationPanel2 extends JPanel {

    private OWLEditorKit owlEditorKit;

    private JTextField textField;

    private JCheckBox addPrefLabel;

    private JComboBox schemaBox;

    private OWLObjectPropertySelectorPanel view;

    public SKOSEntityCreationPanel2(OWLEditorKit owlEditorKit, String message) {
        this.owlEditorKit = owlEditorKit;
        createUI(message);
    }


    // hack at the moment but we need to supply the correct URI based on the conceptSchema being used.
    public URI getBaseURI() {
//        return owlEditorKit.getOWLModelManager().getActiveOntology().getURI();
        OWLIndividual ind = getSelectedSchema();
        URI uri = ind.getURI();
        //System.err.println("host: " + base.toString());
        return URI.create("http://" + uri.getHost() + uri.getPath());
    }

    public String getName() {
        return textField.getText();
    }

    public Set<OWLIndividual> getConceptSchemes () {

        Set<OWLIndividual> inds = new HashSet();

        for (OWLOntology onto  : owlEditorKit.getModelManager().getOntologies()) {
            Set<OWLClassAssertionAxiom> axioms = onto.getClassAssertionAxioms(owlEditorKit.getModelManager().getOWLDataFactory().getOWLClass(SKOSVocabulary.CONCEPTSCHEME));
            Iterator it = axioms.iterator();
            while (it.hasNext()) {
                OWLClassAssertionAxiom axiom = (OWLClassAssertionAxiom) it.next();
                inds.add(axiom.getIndividual());
            }

        }
        return inds;
    }

    public OWLIndividual getCurrentConceptScheme () {

        OWLOntology onto = owlEditorKit.getModelManager().getActiveOntology();
        Set<OWLClassAssertionAxiom> axioms = onto.getClassAssertionAxioms(owlEditorKit.getModelManager().getOWLDataFactory().getOWLClass(SKOSVocabulary.CONCEPTSCHEME));
        Iterator it = axioms.iterator();
        OWLClassAssertionAxiom ax = (OWLClassAssertionAxiom) it.next();
        return ax.getIndividual();

    }

    public OWLIndividual getSelectedSchema () {
        return (OWLIndividual) schemaBox.getSelectedItem();
    }

    private void createUI(String message) {
        setLayout(new BorderLayout());

        JPanel entryPanel = new JPanel(new BorderLayout(7,7));

        schemaBox = new JComboBox(getConceptSchemes().toArray());
        if(!getConceptSchemes().isEmpty()) {
            schemaBox.setSelectedIndex(0);
        }
        schemaBox.setRenderer(new OWLCellRenderer(owlEditorKit));
        
        add(schemaBox, BorderLayout.NORTH);

        add(entryPanel, BorderLayout.CENTER);

        entryPanel.add(new JLabel(message), BorderLayout.NORTH);
        textField = new JTextField(30);
        entryPanel.add(textField, BorderLayout.CENTER);
        addPrefLabel = new JCheckBox("set as prefered label?", true);
        entryPanel.add(addPrefLabel, BorderLayout.SOUTH);

        JPanel propertiesPanel = new JPanel(new BorderLayout(7, 7));
        add(propertiesPanel, BorderLayout.SOUTH);

        view = new OWLObjectPropertySelectorPanel(owlEditorKit);
        propertiesPanel.add(new JLabel("Select relationship to current term"), BorderLayout.CENTER);
        propertiesPanel.add(view, BorderLayout.SOUTH);

        schemaBox.setSelectedIndex(0);
        schemaBox.revalidate();


    }

    public boolean addPrefLabel () {
        return addPrefLabel.isSelected();
    }


    public OWLObjectProperty getSelectedProperty() {

        return (OWLObjectProperty) view.getSelectedObjects();

    }

    public static SKOSEntityCreation showDialog(OWLEditorKit owlEditorKit, String message) {
        SKOSEntityCreationPanel2 panel2 = new SKOSEntityCreationPanel2(owlEditorKit, message);
        int ret = JOptionPaneEx.showConfirmDialog(owlEditorKit.getWorkspace(),
                                                  "Specify name",
                panel2,
                                                  JOptionPane.PLAIN_MESSAGE,
                                                  JOptionPane.OK_CANCEL_OPTION,
                                                  panel2.textField);
        if (ret == JOptionPane.OK_OPTION) {
            return panel2.getUriShortNamePair();
        }
        else {
            return null;
        }
    }

    public SKOSEntityCreation getUriShortNamePair() {
        if (getName().trim().length() > 0) {
            return new SKOSEntityCreation(getBaseURI(), getName(), getSelectedProperty(), addPrefLabel(),getSelectedSchema());
        }
        else {
            return null;
        }
    }


    public class SKOSEntityCreation {

        private URI uri;

        private String shortName;

        private OWLObjectProperty prop;

        private boolean addPrefLabel;

        private OWLIndividual schema;

        public SKOSEntityCreation(URI uri, String shortName, OWLObjectProperty prop, boolean addPrefLabel, OWLIndividual schema) {
            this.uri = uri;
            this.shortName = shortName;
            this.prop = prop;
            this.addPrefLabel = addPrefLabel;
            this.schema = schema;
        }

        public OWLIndividual getConceptSchema () {
            return schema;
        }

        public URI getUri() {
            return uri;
        }


        public String getShortName() {
            return shortName;
        }

        public OWLObjectProperty getProperty () {
            return prop;
        }

        public boolean addPrefLabel () {
            return addPrefLabel;
        }
    }

}
