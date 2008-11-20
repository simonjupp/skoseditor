package org.sealife.skos.editor.frames;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.frame.AbstractOWLFrame;
import org.sealife.skos.editor.panels.SKOSConceptSchemeSelectorPanel;
import org.sealife.skos.editor.panels.SKOSConceptSelectorPanel;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLObjectProperty;
import uk.ac.manchester.cs.skos.SKOSRDFVocabulary;

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
public class SKOSObjectPropertyAssertionsFrame extends AbstractOWLFrame<OWLIndividual> {

    public SKOSObjectPropertyAssertionsFrame(OWLEditorKit owlEditorKit) {
        super(owlEditorKit.getModelManager().getOWLOntologyManager());
        Set<OWLObjectProperty> filetrProps = new HashSet<OWLObjectProperty>(7);
        addSection(new SKOSRelatedPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSRDFVocabulary.RELATED.getURI()), "SKOS related assertion", new SKOSConceptSelectorPanel(owlEditorKit, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)));
        filetrProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSRDFVocabulary.RELATED.getURI()));
        addSection(new SKOSRelatedPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSRDFVocabulary.INSCHEME.getURI()), "SKOS in scheme assertion", new SKOSConceptSchemeSelectorPanel(owlEditorKit, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)));
        filetrProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSRDFVocabulary.INSCHEME.getURI()));
        addSection(new SKOSRelatedPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSRDFVocabulary.TOPCONCEPTOF.getURI()), "SKOS top concept of assertion", new SKOSConceptSchemeSelectorPanel(owlEditorKit, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)));
        filetrProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSRDFVocabulary.TOPCONCEPTOF.getURI()));
        addSection(new SKOSRelatedPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSRDFVocabulary.BROADMATCH.getURI()), "SKOS broad match assertion", new SKOSConceptSelectorPanel(owlEditorKit, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)));
        filetrProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSRDFVocabulary.BROADMATCH.getURI()));
        addSection(new SKOSRelatedPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSRDFVocabulary.NARROWMATCH.getURI()), "SKOS narrow match assertion", new SKOSConceptSelectorPanel(owlEditorKit, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)));
        filetrProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSRDFVocabulary.NARROWMATCH.getURI()));
        addSection(new SKOSRelatedPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSRDFVocabulary.RELATEDMATCH.getURI()), "SKOS related match assertion", new SKOSConceptSelectorPanel(owlEditorKit, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)));
        filetrProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSRDFVocabulary.RELATEDMATCH.getURI()));
        addSection(new SKOSRelatedPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSRDFVocabulary.EXACTMATCH.getURI()), "SKOS exact match assertion", new SKOSConceptSelectorPanel(owlEditorKit, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)));
        filetrProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLObjectProperty(SKOSRDFVocabulary.EXACTMATCH.getURI()));
        addSection(new SKOSOtherObjectPropertyAssertionAxiomFrameSection(owlEditorKit, this, filetrProps));

//        addSection(new OWLDataPropertyAssertionAxiomFrameSection(owlEditorKit, this));
//        addSection(new OWLNegativeObjectPropertyAssertionFrameSection(owlEditorKit, this));
//        addSection(new OWLNegativeDataPropertyAssertionFrameSection(owlEditorKit, this));
    }
}
