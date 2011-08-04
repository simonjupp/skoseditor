package org.sealife.skos.editor.frames;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.model.inference.ReasonerPreferences;
import org.protege.editor.owl.ui.editor.AbstractOWLObjectEditor;
import org.protege.editor.owl.ui.frame.AbstractOWLFrameSection;
import org.protege.editor.owl.ui.frame.OWLFrame;
import org.protege.editor.owl.ui.frame.OWLFrameSectionRow;
import org.protege.editor.owl.ui.frame.OWLObjectPropertyIndividualPair;
import org.protege.editor.owl.ui.frame.individual.OWLObjectPropertyAssertionAxiomFrameSectionRow;
import org.protege.editor.owl.ui.selector.AbstractSelectorPanel;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.NodeSet;

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
public class SKOSRelatedPropertyAssertionAxiomFrameSection extends AbstractOWLFrameSection<OWLIndividual, OWLObjectPropertyAssertionAxiom, OWLObjectPropertyIndividualPair> {

    public static final String LABEL = "Related property assertions";

    private Set<OWLObjectPropertyAssertionAxiom> added;

    private OWLObjectProperty relatedProp;

    AbstractSelectorPanel panel;

    public SKOSRelatedPropertyAssertionAxiomFrameSection(OWLEditorKit owlEditorKit, OWLFrame<OWLNamedIndividual> frame, OWLObjectProperty property, String label, AbstractSelectorPanel panel ) {
        super(owlEditorKit, label, label, frame);
        relatedProp = property;
        added = new HashSet<OWLObjectPropertyAssertionAxiom>();
        this.panel = panel;
    }


    protected void clear() {

    }


    /**
     * Refills the section with rows.  This method will be called
     * by the system and should be directly called.
     */
    protected void refill(OWLOntology ontology) {
        added.clear();
        for (OWLObjectPropertyAssertionAxiom ax : ontology.getObjectPropertyAssertionAxioms(getRootObject())) {
            if (ax.getProperty().equals(relatedProp)) {
                addRow(new OWLObjectPropertyAssertionAxiomFrameSectionRow(getOWLEditorKit(),
                                                                      this,
                                                                      ontology,
                                                                      getRootObject(),
                                                                      ax) {

                    protected AbstractOWLObjectEditor<OWLObjectPropertyIndividualPair> getObjectEditor() {
                        return new SKOSObjectPropertyIndividualPairEditor(getOWLEditorKit(), relatedProp, panel);
                    }
                });
                added.add(ax);
            }
        }
    }


    protected void refillInferred() {

        final Set<OWLObjectProperty> relatedProps = new HashSet<OWLObjectProperty>(10);
        relatedProps.add(relatedProp);

        Set<OWLObjectPropertyExpression> inferredSet = new HashSet<OWLObjectPropertyExpression>();
        inferredSet.addAll(getReasoner().getSubObjectProperties(relatedProp, true).getFlattened());

        inferredSet.addAll(new HashSet<OWLObjectPropertyExpression>(getReasoner().getEquivalentObjectProperties(relatedProp).getEntities()));

        for (OWLObjectPropertyExpression set : inferredSet) {
            relatedProps.add(set.asOWLObjectProperty());
        }

        getOWLModelManager().getReasonerPreferences().executeTask(ReasonerPreferences.OptionalInferenceTask.SHOW_INFERRED_OBJECT_PROPERTY_ASSERTIONS, new Runnable() {


                public void run() {
                    OWLDataFactory factory = getOWLDataFactory();
                    if (!getRootObject().isAnonymous()){
                        for (OWLObjectProperty prop : getReasoner().getRootOntology().getObjectPropertiesInSignature(true)) {
                            if (prop.equals(factory.getOWLTopObjectProperty())) {
                                continue;
                            }
                             if (relatedProps.contains(prop)) {
                                 NodeSet<OWLNamedIndividual> values = getReasoner().getObjectPropertyValues(getRootObject().asOWLNamedIndividual(), prop);
                                 for (OWLNamedIndividual ind : values.getFlattened()) {
                                     OWLObjectPropertyAssertionAxiom ax = getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(prop,
                                                                                                                                 getRootObject(),
                                                                                                                                 ind);
                                     if (!added.contains(ax)) {
                                         addRow(new OWLObjectPropertyAssertionAxiomFrameSectionRow(getOWLEditorKit(),
                                                                                                   SKOSRelatedPropertyAssertionAxiomFrameSection.this,
                                                                                                   null,
                                                                                                   getRootObject(),
                                                                                                   ax));
                                     }
                                 }

                             }
                        }
                    }
                }
            });

    }


    protected OWLObjectPropertyAssertionAxiom createAxiom(OWLObjectPropertyIndividualPair object) {
        return getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(object.getProperty(),
                                                                      getRootObject(),
                                                                      object.getIndividual());
    }


    public AbstractOWLObjectEditor<OWLObjectPropertyIndividualPair> getObjectEditor() {
//        return new SKOSConceptSelectorPanel(getOWLEditorKit());
//        SKOSConceptSelectorPanel panel = new SKOSConceptSelectorPanel(getOWLEditorKit());
        return new SKOSObjectPropertyIndividualPairEditor(getOWLEditorKit(), relatedProp, panel);
//        return new OWLObjectPropertyIndividualPair(relatedProp, panel.getSelectedObject());
//        return new OWLObjectPropertyIndividualPairEditor(getOWLEditorKit());
    }


    /**
     * Obtains a comparator which can be used to sort the rows
     * in this section.
     * @return A comparator if to sort the rows in this section,
     *         or <code>null</code> if the rows shouldn't be sorted.
     */


    public void visit(OWLObjectPropertyAssertionAxiom axiom) {
        if (axiom.getSubject().equals(getRootObject())) {
            reset();
        }
    }


    public Comparator<OWLFrameSectionRow<OWLIndividual, OWLObjectPropertyAssertionAxiom, OWLObjectPropertyIndividualPair>> getRowComparator() {

        return null;
    }
}
