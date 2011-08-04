package org.sealife.skos.editor.frames;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.frame.AbstractOWLFrame;
import org.sealife.skos.editor.SKOSVocabulary;
import org.sealife.skos.editor.panels.SKOSConceptSchemeSelectorPanel;
import org.sealife.skos.editor.panels.SKOSConceptSelectorPanel;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

import javax.swing.*;
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
public class SKOSObjectPropertyAssertionsFrame extends AbstractOWLFrame<OWLNamedIndividual> {

    private SKOSConceptSelectorPanel con1;
    private SKOSConceptSchemeSelectorPanel cons1;
    private SKOSConceptSchemeSelectorPanel cons2;
    private SKOSConceptSelectorPanel con2;
    private SKOSConceptSelectorPanel con3;
    private SKOSConceptSelectorPanel con4;
    private SKOSConceptSelectorPanel con5;


    public SKOSObjectPropertyAssertionsFrame(OWLEditorKit owlEditorKit) {
        super(owlEditorKit.getModelManager().getOWLOntologyManager());
        Set<OWLObjectProperty> filetrProps = new HashSet<OWLObjectProperty>(7);
        addSection(new SKOSRelatedPropertyAssertionAxiomFrameSection(
                owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.RELATED.getIRI()), "SKOS related assertion", con1 = new SKOSConceptSelectorPanel(owlEditorKit, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)));
        filetrProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.RELATED.getIRI()));
        addSection(new SKOSRelatedPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.INSCHEME.getIRI()), "SKOS in scheme assertion", cons1 = new SKOSConceptSchemeSelectorPanel(owlEditorKit, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)));
        filetrProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.INSCHEME.getIRI()));
        addSection(new SKOSRelatedPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.TOPCONCEPTOF.getIRI()), "SKOS top concept of assertion", cons2 = new SKOSConceptSchemeSelectorPanel(owlEditorKit, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)));
        filetrProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.TOPCONCEPTOF.getIRI()));
        addSection(new SKOSRelatedPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.BROADMATCH.getIRI()), "SKOS broad match assertion", con2 = new SKOSConceptSelectorPanel(owlEditorKit, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)));
        filetrProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.BROADMATCH.getIRI()));
        addSection(new SKOSRelatedPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.NARROWMATCH.getIRI()), "SKOS narrow match assertion", con3 = new SKOSConceptSelectorPanel(owlEditorKit, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)));
        filetrProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.NARROWMATCH.getIRI()));
        addSection(new SKOSRelatedPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.RELATEDMATCH.getIRI()), "SKOS related match assertion", con4 = new SKOSConceptSelectorPanel(owlEditorKit, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)));
        filetrProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.RELATEDMATCH.getIRI()));
        addSection(new SKOSRelatedPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.EXACTMATCH.getIRI()), "SKOS exact match assertion", con5 = new SKOSConceptSelectorPanel(owlEditorKit, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)));
        filetrProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSVocabulary.EXACTMATCH.getIRI()));
        addSection(new SKOSOtherObjectPropertyAssertionAxiomFrameSection(owlEditorKit, this, filetrProps));

//        addSection(new OWLDataPropertyAssertionAxiomFrameSection(owlEditorKit, this));
//        addSection(new OWLNegativeObjectPropertyAssertionFrameSection(owlEditorKit, this));
//        addSection(new OWLNegativeDataPropertyAssertionFrameSection(owlEditorKit, this));
    }


    public void dispose() {
        super.dispose();    //To change body of overridden methods use File | Settings | File Templates.
        con1.dispose();
        con2.dispose();
        con3.dispose();
        con4.dispose();
        con5.dispose();
        cons1.dispose();
        cons2.dispose();

    }
}
