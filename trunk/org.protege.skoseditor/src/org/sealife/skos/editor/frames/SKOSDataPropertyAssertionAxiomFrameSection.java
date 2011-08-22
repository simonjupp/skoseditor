package org.sealife.skos.editor.frames;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.editor.AbstractOWLObjectEditor;
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
public class SKOSDataPropertyAssertionAxiomFrameSection extends AbstractOWLFrameSection<OWLIndividual, OWLDataPropertyAssertionAxiom, OWLDataPropertyConstantPair> {

    public static final String LABEL = "Data property assertions";

    private SKOSDataPropertyRelationshipEditor editor;

    private Set<OWLDataPropertyAssertionAxiom> added = new HashSet<OWLDataPropertyAssertionAxiom>();

    private OWLDataProperty relatedProperty;

    protected void clear() {
        if (editor != null) {
            editor.clear();
        }
    }


    public SKOSDataPropertyAssertionAxiomFrameSection(OWLEditorKit editorKit, OWLFrame<? extends OWLNamedIndividual> frame, OWLDataProperty property, String label) {
        super(editorKit, label, label, frame);
        this.relatedProperty = property;
    }


    protected SKOSDataPropertyAssertionAxiomFrameSection(OWLEditorKit editorKit, String label, String rowLabel, OWLFrame<? extends OWLNamedIndividual> owlFrame) {
        super(editorKit, label, rowLabel, owlFrame);    //To change body of overridden methods use File | Settings | File Templates.
    }

    /**
     * Refills the section with rows.  This method will be called
     * by the system and should be directly called.
     */
    protected void refill(OWLOntology ontology) {
        added.clear();
        for (final OWLDataPropertyAssertionAxiom ax : ontology.getDataPropertyAssertionAxioms(getRootObject())) {
            if (ax.getProperty().equals(relatedProperty)) {
                addRow(new OWLDataPropertyAssertionAxiomFrameSectionRow(getOWLEditorKit(),
                        this,
                        ontology,
                        getRootObject(),
                        ax) {


                    protected AbstractOWLObjectEditor<OWLDataPropertyConstantPair> getObjectEditor() {
                        SKOSDataPropertyRelationshipEditor relEd = new SKOSDataPropertyRelationshipEditor(getOWLEditorKit(), relatedProperty);
                        relEd.setDataPropertyAxiom(ax);
                        return relEd;
                    }
                }) ;
                added.add(ax);
            }
        }
    }


    protected void refillInferred() {


        for (OWLDataProperty property : getReasoner().getSubDataProperties(relatedProperty, true).getFlattened()) {
            
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


    protected OWLDataPropertyAssertionAxiom createAxiom(OWLDataPropertyConstantPair object) {
        return getOWLDataFactory().getOWLDataPropertyAssertionAxiom(object.getProperty(),
                                                                    getRootObject(),
                                                                    object.getConstant());
    }


    public AbstractOWLObjectEditor<OWLDataPropertyConstantPair> getObjectEditor() {
        if (editor == null) {
            editor = new SKOSDataPropertyRelationshipEditor(getOWLEditorKit(), relatedProperty);
//            editor.setDataPropertyAxiom();
        }
        return editor;
    }


    /**
     * Obtains a comparator which can be used to sort the rows
     * in this section.
     * @return A comparator if to sort the rows in this section,
     *         or <code>null</code> if the rows shouldn't be sorted.
     */


    public void visit(OWLDataPropertyAssertionAxiom axiom) {
        if (axiom.getSubject().equals(getRootObject())) {
            reset();
        }
    }

    public Comparator<OWLFrameSectionRow<OWLIndividual, OWLDataPropertyAssertionAxiom, OWLDataPropertyConstantPair>> getRowComparator() {
        return null;
    }
}
