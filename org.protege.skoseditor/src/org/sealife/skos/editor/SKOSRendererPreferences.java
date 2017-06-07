package org.sealife.skos.editor;

import org.protege.editor.core.prefs.Preferences;
import org.protege.editor.core.prefs.PreferencesManager;
import org.protege.editor.owl.ui.renderer.OWLEntityRendererImpl;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;

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
 * The University Of Manchester<br>
 * Medical Informatics Group<br>
 * Date: 19-Jun-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class SKOSRendererPreferences {



    public static final String SKOSLABELS = "SKOSLABELS";

    public static final String ANY_LANGUAGE = "!";


    private static final OWLDataFactory df=OWLManager.getOWLDataFactory();

    private boolean useSKOSRenderer = false;

    private static SKOSRendererPreferences instance;

    private List<OWLDataProperty> dataProperties;
    private Map<OWLDataProperty, List<String>> dataPropLanguages;

//    private List<URI> annotationURIS;
//
//    private Map<URI, java.util.List<String>> annotationLanguages;

    private String rendererClass;
    public static final String RENDERER_CLASS = "RENDERER_CLASS";


    public boolean isUseSKOSRenderer() {
        return useSKOSRenderer;
    }

    public void setUseSKOSRenderer(boolean useSKOSRenderer) {
        this.useSKOSRenderer = useSKOSRenderer;
    }

    public void setDataProperties (List<OWLDataProperty> dataProps, Map<OWLDataProperty, List<String>> langMap) {

        dataProperties = dataProps;
        dataPropLanguages= langMap;

        List<String> values = new ArrayList<String> ();

        for (OWLDataProperty prop : dataProperties) {
            StringBuilder str = new StringBuilder((prop.getIRI().toString()));
            final List<String> langs = langMap.get(prop);
            if (langs != null) {
                for (String lang : langs) {
                    if (lang == null) {
                        lang = ANY_LANGUAGE;
                    }
                    str.append(", ").append(lang);
                }
            }
            values.add(str.toString());
        }
        getPreferences().putStringList(SKOSLABELS, values);
    }


//    public void setAnnotations(java.util.List<URI> uris, Map<URI, java.util.List<String>> langMap){
//        annotationURIS = uris;
//        annotationLanguages = langMap;
//        java.util.List<String> values = new ArrayList<String>();
//
//        for (URI uri : uris){
//            StringBuilder str = new StringBuilder(uri.toString());
//            final java.util.List<String> langs = langMap.get(uri);
//            if (langs != null){
//                for (String lang : langs) {
//                    if (lang == null){
//                        lang = ANY_LANGUAGE;
//                    }
//                    str.append(", ").append(lang);
//                }
//            }
//            values.add(str.toString());
//        }
//        getPreferences().putStringList(SKOSLABELS, values);
//    }

    public List<OWLDataProperty> getDataPropertyLabels() {
        return new ArrayList<OWLDataProperty>(dataProperties);
    }


//    public java.util.List<URI> getAnnotationURIs(){
//        return new ArrayList<URI>(annotationURIS);
//    }


    public List<String> getPropertyLangs(OWLDataProperty dataProp){
        final List<String> langs = dataPropLanguages.get(dataProp);
        if (langs != null){
            return new ArrayList<String>(langs);
        }
        else{
            return Collections.emptyList();
        }
    }

//    public java.util.List<String> getAnnotationLangs(URI uri){
//        final java.util.List<String> langs = annotationLanguages.get(uri);
//        if (langs != null){
//            return new ArrayList<String>(langs);
//        }
//        else{
//            return Collections.emptyList();
//        }
//    }

    public Map<OWLDataProperty, List<String>> getPropertyLangs(){
        return dataPropLanguages;
    }

//    public Map<URI, java.util.List<String>> getAnnotationLangs(){
//        return annotationLanguages;
//    }

    private SKOSRendererPreferences() {
        rendererClass = OWLEntityRendererImpl.class.getName();
        load();
    }


    public static synchronized SKOSRendererPreferences getInstance() {
        if (instance == null) {
            instance = new SKOSRendererPreferences();
        }
        return instance;
    }


    private Preferences getPreferences() {
        return PreferencesManager.getInstance().getApplicationPreferences(getClass());
    }


    private void load() {
        Preferences p = getPreferences();
        rendererClass = p.getString(RENDERER_CLASS, OWLEntityRendererImpl.class.getName());
        loadDataProperties();
    }

    private void loadDataProperties() {

        dataProperties = new ArrayList<OWLDataProperty>();
        dataPropLanguages = new HashMap<OWLDataProperty, List<String>>();
        final List<String> defaultValues = Collections.emptyList();
        List<String> values = getPreferences().getStringList(SKOSLABELS, defaultValues);

        if (values.equals(defaultValues)){
              dataProperties.add(df.getOWLDataProperty(SKOSVocabulary.PREFLABEL.getIRI()));
//            annotationURIS.add(OWLRDFVocabulary.RDFS_LABEL.getURI());
//            annotationURIS.add(URI.create("http://www.w3.org/2004/02/skos/core#prefLabel"));
        }
        else{
            for (String value : values){
                String[] tokens = value.split(",");
                    IRI iri = IRI.create(tokens[0].trim());
                    List<String> langs = new ArrayList<String>();
                    for (int i=1; i<tokens.length; i++){
                        String token = tokens[i].trim();
                        if (token.equals(ANY_LANGUAGE)){
                            token = null; // OWL API treats this as "no language set"
                        }
                        langs.add(token);
                    }
                    OWLDataProperty p1 = df.getOWLDataProperty(iri);
                    dataProperties.add(p1);
                    dataPropLanguages.put(p1, langs);
            }
        }
    }

//    private void loadAnnotations() {
//        annotationURIS = new ArrayList<URI>();
//        annotationLanguages = new HashMap<URI, java.util.List<String>>();
//        final java.util.List<String> defaultValues = Collections.emptyList();
//        java.util.List<String> values = getPreferences().getStringList(SKOSLABELS, defaultValues);
//
//        if (values.equals(defaultValues)){
//            annotationURIS.add(OWLRDFVocabulary.RDFS_LABEL.getURI());
//            annotationURIS.add(URI.create("http://www.w3.org/2004/02/skos/core#prefLabel"));
//        }
//        else{
//            for (String value : values){
//                String[] tokens = value.split(",");
//                try {
//                    URI uri = new URI(tokens[0].trim());
//                    java.util.List<String> langs = new ArrayList<String>();
//                    for (int i=1; i<tokens.length; i++){
//                        String token = tokens[i].trim();
//                        if (token.equals(ANY_LANGUAGE)){
//                            token = null; // OWL API treats this as "no language set"
//                        }
//                        langs.add(token);
//                    }
//                    annotationURIS.add(uri);
//                    annotationLanguages.put(uri, langs);
//                }
//                catch (URISyntaxException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//


    public String getRendererClass() {
        return rendererClass;
    }


    public void setRendererClass(String rendererClass) {
        this.rendererClass = rendererClass;
        getPreferences().putString(RENDERER_CLASS, rendererClass);
    }

}
