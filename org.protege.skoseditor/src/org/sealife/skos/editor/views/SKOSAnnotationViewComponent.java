package org.sealife.skos.editor.views;

import org.protege.editor.owl.ui.framelist.OWLFrameList;
import org.protege.editor.owl.ui.view.individual.AbstractOWLIndividualViewComponent;
import org.sealife.skos.editor.frames.SKOSAnnotationFrame;
import org.semanticweb.owlapi.model.OWLAnnotationSubject;
import org.semanticweb.owlapi.model.OWLNamedIndividual;

import javax.swing.*;
import java.awt.*;/*
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
public class SKOSAnnotationViewComponent extends AbstractOWLIndividualViewComponent {

    /**
     *
     */
    private static final long serialVersionUID = -3036939007124710864L;
    private OWLFrameList<OWLAnnotationSubject> list;


    public void initialiseIndividualsView() throws Exception {
        list = new OWLFrameList<OWLAnnotationSubject>(getOWLEditorKit(), new SKOSAnnotationFrame(getOWLEditorKit()));
        setLayout(new BorderLayout());
        add(new JScrollPane(list));
    }


    public void disposeView() {
        list.dispose();
    }


    public OWLNamedIndividual updateView(OWLNamedIndividual selectedIndividual) {
        list.setRootObject(selectedIndividual == null ? null : selectedIndividual.getIRI());
        return selectedIndividual;
    }
}

