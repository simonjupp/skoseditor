package org.sealife.skos.editor.panels;

import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.core.ui.util.JOptionPaneEx;

import javax.swing.*;
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
 * Date: Apr 30, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public class MergeSKOSConceptsPanel extends JPanel {

    private OWLEditorKit owlEditorKit;

    private JTextField textField;
    
    private String newTerm;

    public MergeSKOSConceptsPanel (OWLEditorKit owlEditorKit, String message) {
        this.owlEditorKit = owlEditorKit;
        createUI(message);
    }

    public void createUI (String message) {
        add(new JLabel(message));
        textField = new JTextField(30);
        add(textField);

    }


    public static String showDialog(OWLEditorKit owlEditorKit, String message) {
        MergeSKOSConceptsPanel panel = new MergeSKOSConceptsPanel(owlEditorKit, message);
        int ret = JOptionPaneEx.showConfirmDialog(owlEditorKit.getWorkspace(),
                                                  "Specify name",
                                                  panel,
                                                  JOptionPane.PLAIN_MESSAGE,
                                                  JOptionPane.OK_CANCEL_OPTION,
                                                  panel.textField);
        if (ret == JOptionPane.OK_OPTION) {
           return panel.getUriShortNamePair();
        }
        else {
            return null;
        }
    }

    public String getUriShortNamePair() {
        if (textField.getText().trim().length() > 0) {
            return textField.getText();
        }
        else {
            return null;
        }
    }
}
