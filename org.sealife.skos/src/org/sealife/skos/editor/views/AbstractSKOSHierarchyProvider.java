package org.sealife.skos.editor.views;

import org.protege.editor.owl.model.hierarchy.AbstractOWLObjectHierarchyProvider;
import org.semanticweb.owl.model.*;
import org.semanticweb.owl.util.OWLAxiomVisitorAdapter;

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
 * Date: Oct 9, 2008<br>
 * The University of Manchester<br>
 * Bio-Health Informatics Group<br>
 */
public abstract class AbstractSKOSHierarchyProvider extends AbstractOWLObjectHierarchyProvider<OWLIndividual> {

    private Set<OWLIndividual> roots;
    private Set<OWLIndividual> nonroots;

    public OWLIndividualAxiomFilter getFilter() {
        return filter;
    }

    private OWLIndividualAxiomFilter filter = new OWLIndividualAxiomFilter();

    private Map<OWLIndividual, Set<OWLIndividual>> parent2Child;

    private Map<OWLIndividual, Set<OWLIndividual>> child2Parent;

    Set<OWLObjectProperty> broaderProperties;
    Set<OWLObjectProperty> narrowerProperties;

    public Set<OWLIndividual> getNonroots() {
        return nonroots;
    }

    public Map<OWLIndividual, Set<OWLIndividual>> getParent2Child() {
        return parent2Child;
    }

    public Map<OWLIndividual, Set<OWLIndividual>> getChild2Parent() {
        return child2Parent;
    }

    public Set<OWLObjectProperty> getBroaderProperties() {
        return broaderProperties;
    }

    public Set<OWLObjectProperty> getNarrowerProperties() {
        return narrowerProperties;
    }

    protected void setUp() {
        roots = new HashSet<OWLIndividual>(10);
        nonroots = new HashSet<OWLIndividual>(10000);
        parent2Child = new HashMap<OWLIndividual, Set<OWLIndividual>>(50000);
        child2Parent = new HashMap<OWLIndividual, Set<OWLIndividual>>(50000);

        broaderProperties = new HashSet<OWLObjectProperty>(10);
        narrowerProperties = new HashSet<OWLObjectProperty>(10);

        

    }

    protected AbstractSKOSHierarchyProvider(OWLOntologyManager owlOntologyManager) {
        super(owlOntologyManager);
        setUp();


//
    }

    protected abstract Set<OWLObjectProperty> loadBroaderProps ();
    protected abstract Set<OWLObjectProperty> loadNarrowerProps ();

//    public void dispose() {
//        super.dispose();
//        getManager().removeOntologyChangeListener(this);
//    }

    public void updateRoots (OWLOntology onto, Set<OWLIndividual> indSet) {

//        for (OWLClassAssertionAxiom axiom : onto.getAxioms(AxiomType.CLASS_ASSERTION)) {
//            for (OWLClass desc : descSet) {
//                if (axiom.getDescription().equals(desc)) {
//                    if (!nonroots.contains(axiom.getIndividual())) {
//                        roots.add(axiom.getIndividual());
//                    }
//                }
//            }
//        }

        for (OWLIndividual ind : indSet) {
            if (!nonroots.contains(ind)) {
                roots.add(ind);
            }            
        }
    }

    public boolean setAdd(boolean add) {
        filter.setAdd(add);
        return add;
    }

    private class OWLIndividualAxiomFilter extends OWLAxiomVisitorAdapter {

        private boolean add = true;


        public void setAdd(boolean add) {
            this.add = add;
        }


        public void visit(OWLClassAssertionAxiom owlClassAssertionAxiom) {
//            if (!add) {
//                nonroots.remove (owlClassAssertionAxiom.getIndividual());
//                roots.remove(owlClassAssertionAxiom.getIndividual());
//            }
        }


        public void visit(OWLObjectPropertyAssertionAxiom axiom) {
                    
            if(loadBroaderProps().contains(axiom.getProperty().asOWLObjectProperty())) {
                if (add) {
                    getIndexedSet(child2Parent, axiom.getSubject(), true).add(axiom.getObject());
                    getIndexedSet(parent2Child, axiom.getObject(), true).add(axiom.getSubject());
                    nonroots.add(axiom.getSubject());
                }
                else {
                    getIndexedSet(child2Parent, axiom.getSubject(), false).remove(axiom.getObject());
                    getIndexedSet(parent2Child, axiom.getObject(), false).remove(axiom.getSubject());
                }
           }
            else if(loadNarrowerProps().contains(axiom.getProperty().asOWLObjectProperty())) {

                if (add) {
                    getIndexedSet(parent2Child, axiom.getSubject(), true).add(axiom.getObject());
                    getIndexedSet(child2Parent, axiom.getObject(), true).add(axiom.getSubject());
                    nonroots.add(axiom.getObject());
                }
                else {
                    getIndexedSet(parent2Child, axiom.getSubject(), false).remove(axiom.getObject());
                    getIndexedSet(child2Parent, axiom.getObject(), false).remove(axiom.getSubject());

                }
            }

        }
    }

    public Set<OWLIndividual> getIndexedSet(Map<OWLIndividual, Set<OWLIndividual>> map, OWLIndividual key, boolean create) {
        Set<OWLIndividual> values = map.get(key);
        if(values == null) {
            if(create) {
                values = new HashSet<OWLIndividual>();
                map.put(key, values);
            }
            else {
                values = Collections.emptySet();
            }
        }
        return values;
    }

    public Set<OWLIndividual> getChildren(OWLIndividual object) {
        return getIndexedSet(parent2Child, object, false);
    }

    public Set<OWLIndividual> getParents(OWLIndividual object) {
        return getIndexedSet(child2Parent, object, false);
    }

    public Set<OWLIndividual> getEquivalents(OWLIndividual object) {
        return Collections.emptySet();
    }

    public boolean containsReference(OWLIndividual object) {
        return false;
    }

    public Set<OWLIndividual> getRoots() {
        return roots;
    }
}
