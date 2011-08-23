package org.sealife.skos.editor.frames;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.frame.AbstractOWLFrame;
import org.sealife.skos.editor.SKOSVocabulary;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

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
public class SKOSDataPropertyAssertionsFrame extends AbstractOWLFrame<OWLNamedIndividual> {

    public SKOSDataPropertyAssertionsFrame(OWLEditorKit owlEditorKit) {
        super(owlEditorKit.getModelManager().getOWLOntologyManager());
        Set<OWLDataProperty> filterProps = new HashSet<OWLDataProperty>(7);

//        addSection(new SKOSDataPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLDataProperty(SKOSVocabulary.PREFLABEL.getIRI()), "SKOS preferred label"));
//        filterProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLDataProperty(SKOSVocabulary.PREFLABEL.getIRI()));
        addSection(new SKOSDataPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLDataProperty(SKOSVocabulary.NOTATION  .getIRI()), "SKOS notation"));
        filterProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLDataProperty(SKOSVocabulary.NOTATION.getIRI()));
//        addSection(new SKOSDataPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLDataProperty(SKOSVocabulary.ALTLABEL.getIRI()), "SKOS alternate label"));
//        filterProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLDataProperty(SKOSVocabulary.ALTLABEL.getIRI()));
//        addSection(new SKOSDataPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLDataProperty(SKOSVocabulary.HIDDENLABEL.getIRI()), "SKOS hidden label"));
//        filterProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLDataProperty(SKOSVocabulary.HIDDENLABEL.getIRI()));
        addSection(new SKOSOtherDataPropertyAssertionAxiomFrameSection(owlEditorKit, this, filterProps));

//        addSection(new OWLDataPropertyAssertionAxiomFrameSection(owlEditorKit, this));
//        addSection(new OWLNegativeObjectPropertyAssertionFrameSection(owlEditorKit, this));
//        addSection(new OWLNegativeDataPropertyAssertionFrameSection(owlEditorKit, this));
    }
}
