package org.sealife.skos.editor.frames;

import org.semanticweb.owl.model.*;
import org.semanticweb.owl.inference.OWLReasonerException;
import org.protege.editor.owl.ui.frame.*;
import org.protege.editor.owl.OWLEditorKit;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
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
 * Author: Matthew Horridge<br>
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


    public SKOSDataPropertyAssertionAxiomFrameSection(OWLEditorKit editorKit, OWLFrame<? extends OWLIndividual> frame, OWLDataProperty property, String label) {
        super(editorKit, label, label, frame);
        this.relatedProperty = property;
    }


    protected SKOSDataPropertyAssertionAxiomFrameSection(OWLEditorKit editorKit, String label, String rowLabel, OWLFrame<? extends OWLIndividual> owlFrame) {
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


                    protected OWLFrameSectionRowObjectEditor<OWLDataPropertyConstantPair> getObjectEditor() {
                        SKOSDataPropertyRelationshipEditor relEd = new SKOSDataPropertyRelationshipEditor(getOWLEditorKit(), relatedProperty);
                        relEd.setDataPropertyAxiom(ax);
                        return relEd;
                    }
                }) ;
                added.add(ax);
            }
        }
    }


    protected void refillInferred() throws OWLReasonerException {
        Map<OWLDataProperty, Set<OWLConstant>> rels = getReasoner().getDataPropertyRelationships(getRootObject());

        Set<OWLDataProperty> relatedProps = new HashSet<OWLDataProperty>(10);
        relatedProps.add(relatedProperty);
        Set<Set<OWLDataProperty>> inferredSet = new HashSet<Set<OWLDataProperty>>();
        inferredSet.addAll(getReasoner().getDescendantProperties(relatedProperty));
        inferredSet.addAll(new HashSet(getReasoner().getEquivalentProperties(relatedProperty)));
        for (Set<OWLDataProperty> set : inferredSet) {
            relatedProps.addAll(set);
        }

        for (OWLDataProperty prop : rels.keySet()) {
            if (relatedProps.contains(prop)) {
                for (OWLConstant constant : rels.get(prop)) {
                    OWLDataPropertyAssertionAxiom ax = getOWLDataFactory().getOWLDataPropertyAssertionAxiom(
                            getRootObject(),
                            prop,
                            constant);
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


    protected OWLDataPropertyAssertionAxiom createAxiom(OWLDataPropertyConstantPair object) {
        return getOWLDataFactory().getOWLDataPropertyAssertionAxiom(getRootObject(),
                                                                    object.getProperty(),
                                                                    object.getConstant());
    }


    public OWLFrameSectionRowObjectEditor<OWLDataPropertyConstantPair> getObjectEditor() {
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
    public Comparator<OWLFrameSectionRow<OWLIndividual, OWLDataPropertyAssertionAxiom, OWLDataPropertyConstantPair>> getRowComparator() {
        return null;
    }


    public void visit(OWLDataPropertyAssertionAxiom axiom) {
        if (axiom.getSubject().equals(getRootObject())) {
            reset();
        }
    }
}
