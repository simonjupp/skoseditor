package org.sealife.skos.editor;

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
 * The University Of Manchester<br>
 * Bio-Health Informatics Group<br>
 * Date: 04-May-2007<br><br>
 */
public class SKOSVocabulary {


    public static String NAMESPACE = "http://www.w3.org/2004/02/skos/core#";
    public static URI PREFLABEL = URI.create(NAMESPACE + "prefLabel");
    public static URI ALTLABEL = URI.create(NAMESPACE + "altLabel");
    public static URI HIDDENLABEL = URI.create(NAMESPACE + "hiddenLabel");
    public static URI BROADER = URI.create(NAMESPACE + "broader");
    public static URI NARROWER = URI.create(NAMESPACE + "narrower");
    public static URI INSCHEME = URI.create(NAMESPACE + "inScheme");
    public static URI CONCEPT = URI.create(NAMESPACE + "Concept");
    public static URI CONCEPTSCHEME = URI.create(NAMESPACE + "ConceptScheme");


    public SKOSVocabulary(String s) {
    }

}
