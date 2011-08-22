package org.sealife.skos.editor.frames;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.editor.OWLDataPropertyRelationshipEditor;
import org.protege.editor.owl.ui.editor.OWLObjectEditor;
import org.protege.editor.owl.ui.frame.AbstractOWLFrameSection;
import org.protege.editor.owl.ui.frame.OWLDataPropertyConstantPair;
import org.protege.editor.owl.ui.frame.OWLFrame;
import org.protege.editor.owl.ui.frame.OWLFrameSectionRow;
import org.protege.editor.owl.ui.frame.individual.OWLDataPropertyAssertionAxiomFrameSectionRow;
import org.semanticweb.owlapi.model.*;

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
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 30-Jan-2007<br><br>
 */
public class SKOSOtherDataPropertyAssertionAxiomFrameSection extends AbstractOWLFrameSection<OWLIndividual, OWLDataPropertyAssertionAxiom, OWLDataPropertyConstantPair> {

    public static final String LABEL = "Other property assertions";

    private OWLDataPropertyRelationshipEditor editor;

    private Set<OWLDataPropertyAssertionAxiom> added = new HashSet<OWLDataPropertyAssertionAxiom>();

    Set<OWLDataProperty> propertyFiltersSet;

    protected void clear() {
        if (editor != null) {
            editor.clear();
        }
    }


    public SKOSOtherDataPropertyAssertionAxiomFrameSection(OWLEditorKit editorKit, OWLFrame<? extends OWLIndividual> frame, Set<OWLDataProperty> propertyFilter) {
        super(editorKit, LABEL, "Other property assertion", frame);
        this.propertyFiltersSet = propertyFilter;
    }


    /**
     * Refills the section with rows.  This method will be called
     * by the system and should be directly called.
     */
    protected void refill(OWLOntology ontology) {
        added.clear();
        for (OWLDataPropertyAssertionAxiom ax : ontology.getDataPropertyAssertionAxioms(getRootObject())) {
            if (!propertyFiltersSet.contains(ax.getProperty().asOWLDataProperty())) {
                addRow(new OWLDataPropertyAssertionAxiomFrameSectionRow(getOWLEditorKit(),
                                                                        this,
                                                                        ontology,
                                                                        getRootObject(),
                                                                        ax));
                added.add(ax);
            }
        }
    }


    protected void refillInferred() {


        Set<OWLDataProperty> allDataProperties = new HashSet<OWLDataProperty>();

        for (OWLOntology ontology : getOWLOntologyManager().getOntologies()) {

            for (OWLDataProperty property : ontology.getDataPropertiesInSignature()) {

                for (OWLDataProperty ancProp : getReasoner().getSuperDataProperties(property, true).getFlattened()) {

                    if (propertyFiltersSet.contains(ancProp)) {
                        propertyFiltersSet.add(property);
                    }
                }
                allDataProperties.add(property);
            }
        }

        for (OWLDataProperty prop : allDataProperties) {

            for (OWLDataProperty property : getReasoner().getSubDataProperties(prop, true).getFlattened()) {

                if (!propertyFiltersSet.contains(property)) {
                    Set<OWLLiteral> literals = getReasoner().getDataPropertyValues(getRootObject().asOWLNamedIndividual(), property);
                    for (OWLLiteral lit : literals) {
                        OWLDataPropertyAssertionAxiom ax = getOWLDataFactory().getOWLDataPropertyAssertionAxiom(
                                property,
                                getRootObject(),
                                lit);
                        if (!added.contains(ax)) {
                            addRow(new OWLDataPropertyAssertionAxiomFrameSectionRow(getOWLEditorKit(),
                                    this,
                                    null,
                                    getRootObject(),
                                    ax));
                        }
                    }
                }
            }
        }
    }


    protected OWLDataPropertyAssertionAxiom createAxiom(OWLDataPropertyConstantPair object) {
        return getOWLDataFactory().getOWLDataPropertyAssertionAxiom(object.getProperty(),
                                                                    getRootObject(),
                                                                    object.getConstant());
    }


    public OWLObjectEditor<OWLDataPropertyConstantPair> getObjectEditor() {
        if (editor == null) {
            editor = new OWLDataPropertyRelationshipEditor(getOWLEditorKit());
        }
        return editor;
    }


    /**
     * Obtains a comparator which can be used to sort the rows
     * in this section.
     * @return A comparator if to sort the rows in this section,
     *         or <code>null</code> if the rows shouldn't be sorted.
     */
    public Comparator<OWLFrameSectionRow<OWLIndividual, OWLDataPropertyAssertionAxiom, OWLDataPropertyConstantPair>> getRowComparator() {
        return null;
    }


    public void visit(OWLDataPropertyAssertionAxiom axiom) {
        if (axiom.getSubject().equals(getRootObject())) {
            reset();
        }
    }
}
