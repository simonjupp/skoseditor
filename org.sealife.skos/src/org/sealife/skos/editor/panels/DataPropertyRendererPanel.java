package org.sealife.skos.editor.panels;

import org.protege.editor.core.ui.util.Icons;
import org.protege.editor.core.ui.util.JOptionPaneEx;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.OWLIcons;
import org.protege.editor.owl.ui.selector.OWLDataPropertySelectorPanel;
import org.sealife.skos.editor.SKOSRendererPreferences;
import org.sealife.skos.editor.SKOSIcons;
import org.semanticweb.owl.model.OWLDataProperty;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * Author: drummond<br>
 * http://www.cs.man.ac.uk/~drummond/<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Apr 8, 2008<br><br>
 */
public class DataPropertyRendererPanel extends JPanel {

    private JTable table;
    private JToolBar toolbar;

    private DefaultTableModel model;

    private boolean dirty = false;

    private OWLEditorKit eKit;


    private Action addAction = new AbstractAction("Add Label", SKOSIcons.getIcon("dataprop.add.png")){
        public void actionPerformed(ActionEvent actionEvent) {
            handleAddAnnotation();
        }
    };

    private Action removeAction = new AbstractAction("Remove Label", OWLIcons.getIcon("property.annotation.remove.png")){
        public void actionPerformed(ActionEvent actionEvent) {
            handleRemoveAnnotation();
        }
    };

    private Action upAction = new AbstractAction("Move Up", Icons.getIcon("object.move_up.gif")){
        public void actionPerformed(ActionEvent actionEvent) {
            handleMoveUp();
        }
    };

    private Action downAction = new AbstractAction("Move Down", Icons.getIcon("object.move_down.gif")){
        public void actionPerformed(ActionEvent actionEvent) {
            handleMoveDown();
        }

    };


    public DataPropertyRendererPanel(OWLEditorKit owlEditorKit) {

        this.eKit = owlEditorKit;

        setLayout(new BorderLayout());

        toolbar = new JToolBar();
        toolbar.setFloatable(false);
        addToolbarAction(addAction);
        addToolbarAction(removeAction);
        toolbar.addSeparator(new Dimension(6, 6));
        addToolbarAction(upAction);
        addToolbarAction(downAction);

        add(toolbar, BorderLayout.NORTH);

        model = new DefaultTableModel(){
            public boolean isCellEditable(int row, int col) {
                return col > 0;
            }
        };
        model.addTableModelListener(new TableModelListener(){
            public void tableChanged(TableModelEvent tableModelEvent) {
                dirty = true;
            }
        });
        model.addColumn("SKOS Labels");
        model.addColumn("Languages (comma separated in order of preference, ! for none)");
        load();

        table = new JTable(model);
        table.setShowVerticalLines(false);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        table.getColumnModel().getColumn(0).setWidth(200);
        final JScrollPane scroller = new JScrollPane(table);
        add(scroller, BorderLayout.CENTER);
    }

    public Dimension getPreferredSize() {
        return new Dimension(800, 500);
    }

    public static void showDialog(OWLEditorKit owlEditorKit) {
        DataPropertyRendererPanel panel = new DataPropertyRendererPanel(owlEditorKit);
        int ret = JOptionPaneEx.showConfirmDialog(owlEditorKit.getWorkspace(),
                                                  "Annotation Renderer",
                                                  panel,
                                                  JOptionPane.PLAIN_MESSAGE,
                                                  JOptionPane.OK_CANCEL_OPTION,
                                                  panel);
        if(ret == JOptionPane.OK_OPTION) {
            panel.applyChanges();
        }

    }

    protected void load(){
        List<OWLDataProperty> rows = SKOSRendererPreferences.getInstance().getDataPropertyLabels();
        for (OWLDataProperty row : rows){
            List<String> langs = SKOSRendererPreferences.getInstance().getPropertyLangs(row);

            Object[] rowData = new Object[2];
            rowData[0] = row;
            StringBuilder langsAsString = new StringBuilder();
            for (String lang : langs) {
                if (langsAsString.length() != 0) {
                    langsAsString.append(", ");
                }
                if (lang == null){
                    lang = SKOSRendererPreferences.ANY_LANGUAGE;
                }
                langsAsString.append(lang);
            }
            if (langsAsString.length() > 1){
                rowData[1] = langsAsString.toString();
            }
            model.addRow(rowData);
        }
    }

    protected void applyChanges() {
        if (dirty){
            List<OWLDataProperty> dataProp = new ArrayList<OWLDataProperty>();
            Map<OWLDataProperty,List<String>> langMap = new HashMap<OWLDataProperty, List<String>>();

            for (int i=0; i<model.getRowCount(); i++){
                OWLDataProperty prop = (OWLDataProperty)model.getValueAt(i, 0);
                String langsAsString = (String)model.getValueAt(i, 1);
                if (langsAsString != null){
                    List<String> langs = new ArrayList<String>();
                    for (String token : langsAsString.split(",")){
                        token = token.trim();
                        if (token.equals(SKOSRendererPreferences.ANY_LANGUAGE)){
                            token = null; // OWL API treats this as "no language"
                        }
                        langs.add(token);
                    }
                    langMap.put(prop, langs);
                }
                dataProp.add(prop);
            }
            SKOSRendererPreferences.getInstance().setDataProperties(dataProp, langMap);
            dirty = false;
        }
    }


    private void addToolbarAction(Action action) {
        JButton button = new JButton(action);
        button.setToolTipText((String)action.getValue(Action.NAME));
        button.setText(null);
        button.setBorder(new EmptyBorder(4, 4, 4, 4));
        toolbar.add(button);
    }


    public void dispose() throws Exception {
        // do nothing
    }

    private void handleAddAnnotation() {

        // get new data property
        OWLDataPropertySelectorPanel dataPanel = new OWLDataPropertySelectorPanel(eKit);
//        AnnotationURIList list = new AnnotationURIList(eKit);
//        list.rebuildAnnotationURIList();
        final JScrollPane scroller = new JScrollPane(dataPanel);
        scroller.setPreferredSize(new Dimension(400, 300));
        if (JOptionPane.showConfirmDialog(DataPropertyRendererPanel.this, scroller, "Pick a data property",
                                          JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION){
            Object[] rowData = new Object[]{dataPanel.getSelectedObject(), null};
            model.addRow(rowData);
            table.getSelectionModel().setSelectionInterval(model.getRowCount()-1, model.getRowCount()-1);
        }
    }


    private void handleRemoveAnnotation() {
        final int row = table.getSelectedRow();
        if (row != -1){
            model.removeRow(row);
            if (row < model.getRowCount()){
                table.getSelectionModel().setSelectionInterval(row, row);
            }
            else if (row-1 > 0){
                table.getSelectionModel().setSelectionInterval(row-1, row-1);
            }
        }
    }

    private void handleMoveUp() {
        final int row = table.getSelectedRow();
        if (row > 0){
            model.moveRow(row, row, row-1);
            table.getSelectionModel().setSelectionInterval(row-1, row-1);
        }
    }

    private void handleMoveDown() {
        final int row = table.getSelectedRow();
        if (row < model.getRowCount()-1){
            model.moveRow(row, row, row+1);
            table.getSelectionModel().setSelectionInterval(row+1, row+1);
        }
    }
}
