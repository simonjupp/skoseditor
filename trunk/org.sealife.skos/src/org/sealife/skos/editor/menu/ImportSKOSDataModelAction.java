package org.sealife.skos.editor.menu;

import org.protege.editor.owl.ui.action.ProtegeOWLAction;
import org.semanticweb.owl.model.OWLOntologyCreationException;
import org.semanticweb.owl.model.OWLOntologyManager;
import org.semanticweb.owl.util.SimpleURIMapper;

import java.awt.event.ActionEvent;
import java.net.URI;
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
 * Date: Oct 8, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class ImportSKOSDataModelAction extends ProtegeOWLAction {

    public void actionPerformed(ActionEvent actionEvent) {

        try {
//            ClassLoader loader = SKOSIcons.class.getClassLoader();
//            URL url = loader.getResource("skos-core-2004.owl");
////            URI uri  = ClassLoader.class.getResource().toURI();
//            System.err.println("URI: " + url.toString());

            OWLOntologyManager man = getOWLModelManager().getOWLOntologyManager();
            man.addURIMapper(new SimpleURIMapper(URI.create("http://www.w3.org/2004/02/skos/core"), URI.create("http://www.cs.man.ac.uk/~sjupp/skos/skos-core-2004.owl")));
            getOWLModelManager().getOWLOntologyManager().loadOntology(URI.create("http://www.w3.org/2004/02/skos/core"));
          
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } 

    }

    public void initialise() throws Exception {
    }

    public void dispose() throws Exception {
    }
}
