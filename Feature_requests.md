# Introduction #

Here is a list of the basic features outlines for the protege 4 skos editor.


# Details #

**SKOS Hierarchy View**

Must have features:

Displays the broader/narrower hierarchy of concepts in a concept scheme, showing the preferred label for each concept.

Each hierarchy node is expandable/collapsible.

Can choose the language in which to show preferred labels.

Can create a new concept, with selected concept(s) as hierarchy parent(s), or as top concepts.

Can delete selected concepts.

Can rearrange hierarchy, by dragging and dropping concepts.

Enforces constraint that transitive closure of skos:broader is disjoint with skos:related. I.e. cannot have clash between hierarchical and associative relations between two concepts. (Maybe handle this outside specific view?)

**SKOS Associations View**

Shows list of all concepts skos:related to selected concepts in hierarchy view. Preflabel is displayed for each concept in list. Language for label display can be chosen.

Can add/remove concepts from list of related.

Maybe SKOS Associations View is part of SKOS Hierarchy View? Could call this view SKOS Navigator View?

**SKOS Labels View**

Show all lexical labels for the current selected concept in the hierarchy view. Shows distinction between preferred, alternative and hidden labels.

Can either show all labels in all languages, or all labels in a chosen language.

Can add new labels, edit and remove existing labels.

Enforces constraint that there cannot be more than one preferred label per language.

Enforces constraint that skos:prefLabel, skos:altLabel and skos:hiddenLabel are disjoint.

(Maybe handle checking of constraints outside specific views?)

Has some way of preventing user from specifying alternative labels in some language without specifying a preferred label that language too -- this isn't a semantic constraint, but is probably a sensible thing to do.

**SKOS Semantic Relations View**

Shows list of broader, narrower and related concepts for current selected concept in hierarchy view (or search view?).

Can add/remove broader, narrower and related concepts.

**SKOS Search View**

Can enter a text search expression, and returns a list of concepts with labels and/or annotations matching the search expression.

Can select which concept scheme to search in.

Can select concept from search results and then go to concept position in hierarchy view.

**SKOS Documentation (Notes) View**

Shows all plain text notes - scope note, definition etc. - for selected concept.

Can choose which language to view notes in.

Can add/remove/edit plain text notes.

**SKOS Concept Scheme View**

Shows list of currently loaded concept schemes. Display's meta-properties (title, description, rights etc.) for the selected concept scheme.

Can create a new concept scheme (maybe via wizard?).

Can import one concept scheme into another. (We recently resolved that you MAY use owl:imports to assert logical imports statements between two SKOS concept schemes.)