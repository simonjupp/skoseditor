package org.sealife.skos.editor.panels;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.entity.OWLEntityCreationException;
import org.protege.editor.owl.model.entity.OWLEntityCreationSet;
import org.protege.editor.owl.ui.OWLEntityCreationPanel;
import org.protege.editor.owl.ui.UIHelper;
import org.sealife.skos.editor.SKOSVocabulary;
import org.semanticweb.owlapi.model.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
 * Date: Oct 13, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class SKOSEntityCreationPanel<T extends OWLEntity> extends OWLEntityCreationPanel {

    JComboBox schemaBox;
    OWLEditorKit owlEditorKit;
    Class<T> type;

    OWLClass conceptSchemeClass;
    OWLClass conceptClass;
    OWLObjectProperty inScheme;

    public SKOSEntityCreationPanel(OWLEditorKit owlEditorKit, String message, Class<T> type) {
        super(owlEditorKit, message, type);
        this.owlEditorKit = owlEditorKit;
        this.type = type;

        this.conceptSchemeClass = owlEditorKit.getModelManager().getOWLDataFactory().getOWLClass(SKOSVocabulary.CONCEPTSCHEME.getIRI());
        this.conceptClass = owlEditorKit.getModelManager().getOWLDataFactory().getOWLClass(SKOSVocabulary.CONCEPT.getIRI());
        this.inScheme = owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.INSCHEME.getIRI());

        schemaBox = ConceptSchemeComboBox.getConceptSchemeComboBox(owlEditorKit);

        if(!ConceptSchemeComboBox.getConceptSchemes(owlEditorKit).isEmpty()) {
            JPanel schemeSelector = new JPanel(new BorderLayout(4,4));

            JLabel jLabel = new JLabel("Choose a Concept Scheme for this Concept");
            jLabel.setBorder(new EmptyBorder(2,10,2,2));
            schemeSelector.add(jLabel, BorderLayout.NORTH);
            schemeSelector.add(schemaBox, BorderLayout.CENTER);
            add(schemeSelector);
        }
    }

    private OWLNamedIndividual getSelectedConceptScheme () {
        return (OWLNamedIndividual) schemaBox.getSelectedItem();
    }


    public static <T extends OWLEntity> OWLEntityCreationSet<T> showDialog(OWLEditorKit owlEditorKit, String message, Class<T> type) {
        SKOSEntityCreationPanel panel = new SKOSEntityCreationPanel<T>(owlEditorKit, message, type);

        int ret = new UIHelper(owlEditorKit).showValidatingDialog("Select a SKOS Concept", panel, panel.getFocusComponent());

        if (ret == JOptionPane.OK_OPTION) {
            return panel.getOWLEntityCreationSet();
        }
        else {
            return null;
        }
    }

    public OWLEntityCreationSet<T> getOWLEntityCreationSet() {


        try {

            OWLEntityCreationSet newInd = owlEditorKit.getModelManager().getOWLEntityFactory().createOWLEntity(type,
                                                                                            getEntityName(),
                                                                                            getBaseIRI());

            OWLClassAssertionAxiom skosConceptAx = owlEditorKit.getModelManager().getOWLDataFactory().getOWLClassAssertionAxiom(conceptClass, newInd.getOWLEntity().asOWLNamedIndividual());
            newInd.getOntologyChanges().add(new AddAxiom(owlEditorKit.getModelManager().getActiveOntology(), skosConceptAx));

            if (getSelectedConceptScheme() != null) {
                OWLPropertyAssertionAxiom inSchemeAx = owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(inScheme, newInd.getOWLEntity().asOWLNamedIndividual() , getSelectedConceptScheme());
                newInd.getOntologyChanges().add(new AddAxiom(owlEditorKit.getModelManager().getActiveOntology(), inSchemeAx));
            }

            return newInd;

        } catch (OWLEntityCreationException e) {
            return null;
        }


    }
}
