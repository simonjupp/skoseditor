package org.sealife.skos.editor.frames;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.frame.AbstractOWLFrame;
import org.semanticweb.owl.model.OWLDataProperty;
import org.semanticweb.owl.model.OWLIndividual;
import uk.ac.manchester.cs.skos.SKOSRDFVocabulary;

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
public class SKOSDataPropertyAssertionsFrame extends AbstractOWLFrame<OWLIndividual> {

    public SKOSDataPropertyAssertionsFrame(OWLEditorKit owlEditorKit) {
        super(owlEditorKit.getModelManager().getOWLOntologyManager());
        Set<OWLDataProperty> filterProps = new HashSet<OWLDataProperty>(7);

        addSection(new SKOSDataPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLDataProperty(SKOSRDFVocabulary.PREFLABEL.getURI()), "SKOS preferred label"));
        filterProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLDataProperty(SKOSRDFVocabulary.PREFLABEL.getURI()));
        addSection(new SKOSDataPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLDataProperty(SKOSRDFVocabulary.NOTATION  .getURI()), "SKOS notation"));
        filterProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLDataProperty(SKOSRDFVocabulary.NOTATION.getURI()));
        addSection(new SKOSDataPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLDataProperty(SKOSRDFVocabulary.ALTLABEL.getURI()), "SKOS alternate label"));
        filterProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLDataProperty(SKOSRDFVocabulary.ALTLABEL.getURI()));
        addSection(new SKOSDataPropertyAssertionAxiomFrameSection(owlEditorKit, this, owlEditorKit.getModelManager().getOWLDataFactory().getOWLDataProperty(SKOSRDFVocabulary.HIDDENLABEL.getURI()), "SKOS hidden label"));
        filterProps.add(owlEditorKit.getModelManager().getOWLDataFactory().getOWLDataProperty(SKOSRDFVocabulary.HIDDENLABEL.getURI()));
        addSection(new SKOSOtherDataPropertyAssertionAxiomFrameSection(owlEditorKit, this, filterProps));

//        addSection(new OWLDataPropertyAssertionAxiomFrameSection(owlEditorKit, this));
//        addSection(new OWLNegativeObjectPropertyAssertionFrameSection(owlEditorKit, this));
//        addSection(new OWLNegativeDataPropertyAssertionFrameSection(owlEditorKit, this));
    }
}
