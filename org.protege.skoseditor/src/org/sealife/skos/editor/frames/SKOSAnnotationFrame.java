package org.sealife.skos.editor.frames;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.frame.AbstractOWLFrame;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import uk.ac.manchester.cs.skos.SKOSRDFVocabulary;/*
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
public class SKOSAnnotationFrame extends AbstractOWLFrame<OWLAnnotationSubject> {

    public SKOSAnnotationFrame(OWLEditorKit man) {
        super(man.getModelManager().getOWLOntologyManager());
        addSection(new SKOSAnnotationFrameSection(man, this, "Preferred label",
                man.getModelManager().getOWLDataFactory().getOWLAnnotationProperty(IRI.create(SKOSRDFVocabulary.PREFLABEL.getURI()))));
        addSection(new SKOSAnnotationFrameSection(man, this, "Alternate label",
                man.getModelManager().getOWLDataFactory().getOWLAnnotationProperty(IRI.create(SKOSRDFVocabulary.ALTLABEL.getURI()))));
        addSection(new SKOSAnnotationFrameSection(man, this, "Hidden label",
                man.getModelManager().getOWLDataFactory().getOWLAnnotationProperty(IRI.create(SKOSRDFVocabulary.HIDDENLABEL.getURI()))));
//        addSection(new OWLAnnotationFrameSection(man, this));
    }
}
