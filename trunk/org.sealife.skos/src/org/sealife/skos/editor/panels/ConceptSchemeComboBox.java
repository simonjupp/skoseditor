package org.sealife.skos.editor.panels;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.OWLObjectComparatorAdapter;
import org.protege.editor.owl.ui.renderer.OWLCellRendererSimple;
import org.semanticweb.owl.model.OWLClassAssertionAxiom;
import org.semanticweb.owl.model.OWLIndividual;
import org.semanticweb.owl.model.OWLOntology;
import uk.ac.manchester.cs.skos.SKOSRDFVocabulary;

import javax.swing.*;
import java.util.*;
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
 * Date: Nov 12, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class ConceptSchemeComboBox {

    JComboBox schemaBox;

    public static JComboBox getConceptSchemeComboBox(OWLEditorKit owlEditorKit) {

        final Comparator<OWLIndividual> comp = owlEditorKit.getModelManager().getOWLObjectComparator();

        List<OWLIndividual> sorted;
        Collections.sort(sorted = new ArrayList<OWLIndividual>(getConceptSchemes(owlEditorKit)), new OWLObjectComparatorAdapter<OWLIndividual>(comp) {

            public int compare(OWLIndividual o1, OWLIndividual o2) {
                return super.compare(o1,o2);
            }
        });

        JComboBox schemaBox = new JComboBox(sorted.toArray());
        schemaBox.setRenderer(new OWLCellRendererSimple(owlEditorKit));
        if(!getConceptSchemes(owlEditorKit).isEmpty()) {
            schemaBox.setSelectedIndex(0);
        }

        return schemaBox;

    }

    public static Set<OWLIndividual> getConceptSchemes (OWLEditorKit owlEditorKit) {

        Set<OWLIndividual> inds = new HashSet<OWLIndividual>();

        for (OWLOntology onto  : owlEditorKit.getModelManager().getOntologies()) {
            Set<OWLClassAssertionAxiom> axioms = onto.getClassAssertionAxioms(owlEditorKit.getModelManager().getOWLDataFactory().getOWLClass(SKOSRDFVocabulary.CONCEPTSCHEME.getURI()));
            for (OWLClassAssertionAxiom clssAx : axioms) {
                inds.add(clssAx.getIndividual());
            }
        }
        return inds;
    }


}
