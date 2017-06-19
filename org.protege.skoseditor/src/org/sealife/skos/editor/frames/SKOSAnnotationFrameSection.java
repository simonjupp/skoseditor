package org.sealife.skos.editor.frames;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.editor.OWLObjectEditor;
import org.protege.editor.owl.ui.frame.AbstractOWLFrameSection;
import org.protege.editor.owl.ui.frame.OWLAnnotationsFrameSectionRow;
import org.protege.editor.owl.ui.frame.OWLFrame;
import org.protege.editor.owl.ui.frame.OWLFrameSectionRow;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.search.EntitySearcher;

import java.util.*;/*
 * Copyright (C) 2010, University of Manchester
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
 * Date: Aug 22, 2011<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class SKOSAnnotationFrameSection extends AbstractOWLFrameSection<OWLAnnotationSubject, OWLAnnotationAssertionAxiom, OWLAnnotation> {

    private String LABEL;

    private static OWLAnnotationSectionRowComparator comparator;

    OWLAnnotationProperty property;

    public SKOSAnnotationFrameSection(OWLEditorKit editorKit, OWLFrame<? extends OWLAnnotationSubject> frame, String label, OWLAnnotationProperty property) {
        super(editorKit, label, "Entity annotation", frame);
        this.LABEL = label;
        this.property = property;
        comparator = new OWLAnnotationSectionRowComparator(editorKit.getModelManager());
    }


    protected void clear() {
    }


    protected void refill(OWLOntology ontology) {
        boolean hidden = false;
        final OWLAnnotationSubject annotationSubject = getRootObject();

        Set<OWLAnnotationProperty> filterProperty = new HashSet<OWLAnnotationProperty>();
        filterProperty.add(property);
        filterProperty.addAll(getSubProperties(property));

        for (OWLAnnotationAssertionAxiom ax : ontology.getAnnotationAssertionAxioms(annotationSubject)) {
            if (!getOWLEditorKit().getWorkspace().isHiddenAnnotationURI(ax.getAnnotation().getProperty().getIRI().toURI())) {
                if (filterProperty.contains(ax.getAnnotation().getProperty())) {
                    addRow(new OWLAnnotationsFrameSectionRow(getOWLEditorKit(), this, ontology, annotationSubject, ax));
                }
            }
            else {
                hidden = true;
            }
        }
        if (hidden) {
            setLabel(LABEL + " (some annotations are hidden)");
        }
        else {
            setLabel(LABEL);
        }
    }

    private Set<OWLAnnotationProperty> getSubProperties(OWLAnnotationProperty property) {

        Set<OWLAnnotationProperty> subProps = new HashSet<OWLAnnotationProperty>();
        subProps.add(property);
        for (OWLAnnotationProperty subProp : EntitySearcher.getSubProperties(property, getOWLEditorKit().getModelManager().getActiveOntology())) {
            subProps.addAll(getSubProperties(subProp));
        }
        return subProps;
    }

    protected OWLAnnotationAssertionAxiom createAxiom(OWLAnnotation object) {
        return getOWLDataFactory().getOWLAnnotationAssertionAxiom(getRootObject(), object);
    }


    public OWLObjectEditor<OWLAnnotation> getObjectEditor() {
        if (!getOWLEditorKit().getModelManager().getActiveOntology().getAnnotationPropertiesInSignature().contains(property)) {
            // create the SKOS label property
            OWLModelManager man = getOWLEditorKit().getModelManager();
            OWLAxiom ax = man.getOWLDataFactory().getOWLDeclarationAxiom(property);
            man.applyChange(new AddAxiom(getOWLEditorKit().getModelManager().getActiveOntology(), ax));

            OWLAnnotationProperty label = man.getOWLDataFactory().getRDFSLabel();
            OWLAxiom ax2 = man.getOWLDataFactory().getOWLSubAnnotationPropertyOfAxiom(property, label);
            man.applyChange(new AddAxiom(man.getActiveOntology(), ax2));
        }
        return new SKOSAnnotationEditor(getOWLEditorKit(), property);
    }


    /**
     * Obtains a comparator which can be used to sort the rows
     * in this section.
     * @return A comparator if to sort the rows in this section,
     *         or <code>null</code> if the rows shouldn't be sorted.
     */
    public Comparator<OWLFrameSectionRow<OWLAnnotationSubject, OWLAnnotationAssertionAxiom, OWLAnnotation>> getRowComparator() {
        return comparator;
    }


    public boolean canAcceptDrop(List<OWLObject> objects) {
        for (OWLObject obj : objects) {
            if (!(obj instanceof OWLAnnotation)) {
                return false;
            }
        }
        return true;
    }


    public boolean dropObjects(List<OWLObject> objects) {
        List<OWLOntologyChange> changes = new ArrayList<OWLOntologyChange>();
        for (OWLObject obj : objects) {
            if (obj instanceof OWLAnnotation) {
                OWLAnnotation annot = (OWLAnnotation)obj;
                OWLAxiom ax = getOWLDataFactory().getOWLAnnotationAssertionAxiom(getRootObject(), annot);
                changes.add(new AddAxiom(getOWLModelManager().getActiveOntology(), ax));
            }
            else {
                return false;
            }
        }
        getOWLModelManager().applyChanges(changes);
        return true;
    }


    public void visit(OWLAnnotationAssertionAxiom axiom) {
        final OWLAnnotationSubject root = getRootObject();
        if (axiom.getSubject().equals(root)){
            reset();
        }
    }


    private static class OWLAnnotationSectionRowComparator implements Comparator<OWLFrameSectionRow<OWLAnnotationSubject, OWLAnnotationAssertionAxiom, OWLAnnotation>> {

        private Comparator<OWLObject> owlObjectComparator;

        public OWLAnnotationSectionRowComparator(OWLModelManager owlModelManager) {
            owlObjectComparator = owlModelManager.getOWLObjectComparator();
        }

        public int compare(OWLFrameSectionRow<OWLAnnotationSubject, OWLAnnotationAssertionAxiom, OWLAnnotation> o1,
                           OWLFrameSectionRow<OWLAnnotationSubject, OWLAnnotationAssertionAxiom, OWLAnnotation> o2) {
            return owlObjectComparator.compare(o1.getAxiom(), o2.getAxiom());
        }
    }
}

