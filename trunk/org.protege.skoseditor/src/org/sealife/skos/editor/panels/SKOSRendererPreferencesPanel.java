package org.sealife.skos.editor.panels;

import org.protege.editor.owl.ui.preferences.OWLPreferencesPanel;
import org.protege.editor.owl.ui.renderer.OWLEntityRendererImpl;
import org.protege.editor.owl.ui.renderer.OWLModelManagerEntityRenderer;
import org.protege.editor.owl.ui.renderer.OWLRendererPreferences;
import org.sealife.skos.editor.SKOSEntityRenderer;

import javax.swing.*;
import java.awt.*;
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
 * Date: 08-Jun-2006<br><br>
 * <p/>
 * matthew.horridge@cs.man.ac.uk<br>
 * www.cs.man.ac.uk/~horridgm<br><br>
 */
public class SKOSRendererPreferencesPanel extends OWLPreferencesPanel {

    private JCheckBox setSKOSRenderingCheckBox;

    private DataPropertyRendererPanel dataPropRendererPanel;

    public void applyChanges() {
        OWLRendererPreferences prefs = OWLRendererPreferences.getInstance();

        dataPropRendererPanel.applyChanges();

        if (setSKOSRenderingCheckBox.isSelected()) {

//            prefs.setRendererClass(SKOSEntityRenderer.class.getName());
            setupRenderer(new SKOSEntityRenderer());

        }
        else {
//            prefs.setRendererClass(OWLEntityRendererImpl.class.getName());
            setupRenderer(new OWLEntityRendererImpl());
        }
        getOWLEditorKit().getWorkspace().refreshComponents();

    }


    private void setupRenderer(OWLModelManagerEntityRenderer renderer) {
        renderer.setup(getOWLModelManager());
        renderer.initialise();
        getOWLModelManager().setOWLEntityRenderer(renderer);
    }


    public void initialise() throws Exception {
        setLayout(new BorderLayout());

        OWLRendererPreferences prefs = OWLRendererPreferences.getInstance();

        Box holderBox = new Box(BoxLayout.Y_AXIS);
        Box topBox = new Box(BoxLayout.X_AXIS);
        add(topBox, BorderLayout.NORTH);
        add(holderBox, BorderLayout.CENTER);
//        holderBox.add(setSKOSRenderingCheckBox = new JCheckBox("Use SKOS Rendering", prefs.getRendererClass().equals(SKOSEntityRenderer.class.getName())));

        holderBox.add(dataPropRendererPanel = new DataPropertyRendererPanel(getOWLEditorKit()));

//        highlightAOStatementsCheckBox = new JCheckBox("Highlight active ontology statements",
//                                                      prefs.isHighlightActiveOntologyStatements());
//        showHyperlinksCheckBox = new JCheckBox("Show hyperlinks in components that support them",
//                                               prefs.isRenderHyperlinks());
//        highlightKeyWordsCheckBox = new JCheckBox("Highlight keywords", prefs.isHighlightKeyWords());
//
//        useThatAsSynonymForAndCheckBox = new JCheckBox("Use the 'that' keyword as a synonym for the 'and' keyword",
//                                                       prefs.isUseThatKeyword());
//
//        Box optBox = new Box(BoxLayout.Y_AXIS);
//        optBox.setAlignmentX(0.0f);
//        optBox.setBorder(ComponentFactory.createTitledBorder("Appearance"));
//        optBox.add(highlightAOStatementsCheckBox);
//        optBox.add(showHyperlinksCheckBox);
//        optBox.add(highlightKeyWordsCheckBox);
//        optBox.add(useThatAsSynonymForAndCheckBox);
//        optBox.add(Box.createVerticalStrut(4));
//        holderBox.add(optBox);
//
//
//        JPanel fontSizePanel = new JPanel();
//        PreferencesPanelLayoutManager man = new PreferencesPanelLayoutManager(fontSizePanel);
//        fontSizePanel.setLayout(man);
//        fontSizePanel.setBorder(ComponentFactory.createTitledBorder("Font"));
//        fontSizeSpinner = new JSpinner(new SpinnerNumberModel(prefs.getFontSize(), 1, 120, 1));
//        fontSizePanel.add("Font size", fontSizeSpinner);
//        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//        Font [] fonts = ge.getAllFonts();
//        ArrayList<String> fontNames = new ArrayList<String>();
//        for(Font f : fonts) {
//            fontNames.add(f.getName());
//        }
//        fontCombo = new JComboBox(fontNames.toArray());
//        fontSizePanel.add("Font", fontCombo);
//        fontCombo.setSelectedItem(prefs.getFontName());
//
//        holderBox.add(fontSizePanel);
    }

//    private Component createRendererSelectionPanel() {
//        SKOSRendererPreferences prefs = SKOSRendererPreferences.getInstance();
//        String clsName = prefs.getRendererClass();

//        uriFragmentRadioButton = new JRadioButton("Render entities using URI fragment",
//                                                  clsName.equals(OWLEntityRendererImpl.class.getName()));
//        qnameRendererRadioButton = new JRadioButton("Render entities using qnames",
//                                                    clsName.equals(OWLEntityQNameRenderer.class.getName()));
//        annotationValueRadioButton = new JRadioButton("Render entities using annotation values",
//                                                      clsName.equals(OWLEntityAnnotationValueRenderer.class.getName()) && !prefs.isRenderPrefixes());
//        annotationWithPrefixesRadioButton = new JRadioButton("Render entities using annotation values with prefixes",
//                                                      clsName.equals(OWLEntityAnnotationValueRenderer.class.getName()) && prefs.isRenderPrefixes());
//        ButtonGroup bg = new ButtonGroup();
//        bg.add(uriFragmentRadioButton);
//        bg.add(qnameRendererRadioButton);
//        bg.add(annotationValueRadioButton);
//        bg.add(annotationWithPrefixesRadioButton);
//
//        Box box = new Box(BoxLayout.Y_AXIS);
//        box.setBorder(ComponentFactory.createTitledBorder("Entity rendering"));
//        box.add(uriFragmentRadioButton);
//        box.add(qnameRendererRadioButton);
//        box.add(annotationValueRadioButton);
//        box.add(annotationWithPrefixesRadioButton);
//        box.add(Box.createVerticalStrut(4));
//
//        JPanel optsPanel = new JPanel();
//        optsPanel.setLayout(new BoxLayout(optsPanel, BoxLayout.LINE_AXIS));
//        optsPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
//        optsPanel.setAlignmentX(0.0f);
//
//        showPrefixedsButton = new JButton(new AbstractAction("Prefixes...") {
//            public void actionPerformed(ActionEvent e) {
//                PrefixMappingPanel.showDialog(getOWLEditorKit());
//            }
//        });
//        optsPanel.add(showPrefixedsButton);
//
//        showAnnotationsButton = new JButton(new AbstractAction("Annotations...") {
//            public void actionPerformed(ActionEvent e) {
//                AnnotationRendererPanel.showDialog(getOWLEditorKit());
//            }
//        });
//        optsPanel.add(showAnnotationsButton);
//
//        box.add(optsPanel);
//
//        final ChangeListener l = new ChangeListener() {
//            public void stateChanged(ChangeEvent e) {
//                updateRendererButtons();
//            }
//        };
//        uriFragmentRadioButton.addChangeListener(l);
//        qnameRendererRadioButton.addChangeListener(l);
//        annotationValueRadioButton.addChangeListener(l);
//        annotationWithPrefixesRadioButton.addChangeListener(l);
//
//        updateRendererButtons();

//        return box;
//    }

//    private void updateRendererButtons() {
//        showPrefixedsButton.setEnabled(qnameRendererRadioButton.isSelected() ||
//                                       annotationWithPrefixesRadioButton.isSelected());
//
//        showAnnotationsButton.setEnabled(annotationValueRadioButton.isSelected() ||
//                                         annotationWithPrefixesRadioButton.isSelected());
//    }


    public void dispose() {
    }
}
