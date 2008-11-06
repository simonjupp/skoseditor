package org.sealife.skos;

import org.protege.editor.owl.model.OWLModelManager;
import org.protege.editor.owl.ui.renderer.OWLEntityRendererListener;
import org.protege.editor.owl.ui.renderer.OWLModelManagerEntityRenderer;
import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.PropertyAssertionValueShortFormProvider;
import org.sealife.skos.editor.SKOSRendererPreferences;

import java.util.*;

/**
 * Author: Simon Jupp<br>
 * Date: Jul 10, 2007<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class SKOSEntityRenderer implements OWLModelManagerEntityRenderer {

    PropertyAssertionValueShortFormProvider propVal;

    final static String SPACE = " ";

    final static String IVCOMMA = "'";

    public void setup(final OWLModelManager owlModelManager) {

        SKOSRendererPreferences prefs = SKOSRendererPreferences.getInstance();

        List<OWLPropertyExpression> properties = new ArrayList<OWLPropertyExpression>(prefs.getDataPropertyLabels());


        Map<OWLDataPropertyExpression, List<String>> propToLangs = new HashMap<OWLDataPropertyExpression, List<String>>(prefs.getPropertyLangs());

        propVal = new PropertyAssertionValueShortFormProvider(properties, propToLangs, new OWLOntologySetProvider() {

                    public Set<OWLOntology> getOntologies() {
                        return owlModelManager.getOntologies();

                    }
                });

//        OWLDataProperty prefLabelProp = owlModelManager.getOWLDataFactory().getOWLDataProperty(SKOSVocabulary.PREFLABEL);
//        List<OWLPropertyExpression> properties = new ArrayList<OWLPropertyExpression>();
//        properties.add(prefLabelProp);
//        propVal = new PropertyAssertionValueShortFormProvider(properties, new HashMap(), new OWLOntologySetProvider() {
//
//            public Set<OWLOntology> getOntologies() {
//                return owlModelManager.getOntologies();
//
//            }
//        });

    }

    public void initialise() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String render(OWLEntity entity) {
        if (propVal.getShortForm(entity).contains(SPACE)) {
            return IVCOMMA + propVal.getShortForm(entity) + IVCOMMA;
        }
        return propVal.getShortForm(entity);
    }

    public SKOSEntityRenderer() {
        super();    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void addListener(OWLEntityRendererListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removeListener(OWLEntityRendererListener listener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void dispose() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
