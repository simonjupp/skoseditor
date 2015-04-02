# Introduction #

The files in SKOS EditorialV11.zip should help a newbie learn how to make SKOS-based glossaries using the SKOSed plugin for Protege.  It also provides an example glossary for the Semantic Web.  Since Google doesn't allow project downloads any more, I put it on my Google shared drive at https://drive.google.com/file/d/0BwJWZwjXwkPla2lBZV9aWUJ1OFk/edit?usp=sharing

# Details (of the SKOS Editor Tutorial zip file) #

**SKOS Editor TutorialV11.docx - A tutorial for the Protege SKOSEd plugin**

**SemanticWebGlossaryV12.docx - A glossary of Semantic Web symbols, acronyms, and terms.**

**GlossaryParser.java - A Java version; exports the glossary into SKOS/RDF format.**

**SemanticWebGlossary.owl - The RDF/SKOS version of the glossary, readable by Protégé.**

**README.TXT - This table, in pure ASCII format.**

# Text Only Version of Tutorial #

SKOS Editor Tutorial: Creating a Simple Glossary

By Tihamer T. Toth-Fejel

The SKOS (Simple Knowledge Organization System) Editor is a Protégé plugin designed to make the development of simple knowledge ontology systems easy. This tutorial assumes that you know little about RDF, OWL, Protégé, SKOS, or DLs.   The Semantic Web glossary (in its various forms) that comes included may help.

Step One: Get Protégé

Download Protégé 4.3.0 (Build 304) from http://protege.stanford.edu/download/download.html and install it by double clicking and going through the wizard).

Step Two: Get the SKOSEd plugin

Download SKOSEd-2.0-alpha(build 39).zip from https://code.google.com/p/skoseditor/, unzip it,  and drop the SKOSEd.jar it contains into the Protégé plugins directory (C:\Program Files\Protege\_4.3\plugins\). Restart Protégé, and the SKOSEd Menu Item just shows up in the File/Edit/View Toolbar:

Step Three: Create a New Ontology

Click on File --> New to create a new ontology and (with the “Active Ontology” tab on) type the new name into the “Ontology IRI” field. (BTW, if you click on the “Ontology IRI” label, and some others, Protégé opens up a web browser to that item in the W3C’s specification in http://www.w3.org/TR/2009/REC-owl2-syntax-20091027/):

Step Four: Import the SKOS ontology

Click on the plus sign after “Direct Imports” to get:

Continue and paste http://www.w3.org/2008/05/skos#:

After finishing the wizard, the SKOS ontology will show up under direct imports:

You’ll be able to see the SKOS classes (Thing; Collection, Concept, ‘Concept Scheme’, and List) in the Class hierarchy by click on the “Classes” tab, and then on the arrow to the left of “Thing”:

If you click on the “Object Properties” tab to the SKOS properties:

Step Five: Turn on the SKOS view

On the top Windows Toolbar, Click Windows  Tabs  SKOS view:

The tab will show up immediately. Click on it to bring up the set of windows for entering SKOS data.

Step Six:  Add Terms to the Glossary

While on the “SKOS view” tab, click on the “Add Concept”   icon to bring up the SKOS Concept input screen where you type in “DL” (your first Acronym):

After you click “OK” (or hit Enter), you will see your Concept Instance in the Asserted Concept Hierarchy, next:

Click on the + sign after “Preferred label” in the “SKOS Annotations: DL” view to give it a standard label (in this simple case, just “DL”):

After you click “OK” (or hit Enter), you’ll see a blue prefLabel just under “Preferred label” to show that it has been set (with options to annotate, remove, or edit). It really should be visible, too (and it is, for longer strings), but there seems to be a bug in Protégé. Farther down in the same window, you’ll see “Definition” in light gray:

Click on the plus sign right after it to set the definition for “DL”:

As before, setting an annotation results in it showing up in blue (though again, because it’s so short, it’s not visible; it’s a bug in Protégé, apparently):

Repeat the above with the phrase “Description Logic”, where the need for a preferred label becomes clearer-- it includes spaces.  Also add the definition: “A family of knowledge representation (KR) languages that are widely used in ontological modelling. An important practical reason for this is that they provide one of the main underpinnings for the OWL Web Ontology Language as standardized by the World Wide Web Consortium (W3C).”

Step Seven: Add the Broader Concepts to the Glossary

Normally, glossaries contain not only unusual words, but also acronyms.  Unfortunately, there are really more than ten different kinds of acronyms, including initialisms (like NATO: North Atlantic Treaty Organization) and words with non-initial letters (like Amphetamine: alpha-methyl-phenethylamine).  Let’s not be picky, but go with anything that looks like an acronym. After all, SKOS is supposed to trade precision for ease of use, and we don’t want to agonize over terms like LASER/laser (just put them both in and let them point to each other). Symbols are also very different from both acronyms and words and phrases, so it is a good idea to add those concepts. Add these broader concepts the same way you added the words. Assuming that you are already in the “SKOS view” click on the “Add Concept”   icon to bring up the SKOS concept input screen where you type in “Word”:

After you click “OK” (or hit Enter), and add the other terms (Acronym, Phrase, and Symbol) you will see the entire Asserted Concept Hierarchy:

The DL and ‘Description Logic’ should be under Acronym and Phrase respectively but it’s an easy matter to just grab them and move them. After clicking on “DL” in the Asserted Concept Hierarchy, drag it on top of Acronym and drop it.  Below the “Descriptive Logic” has already been clicked, but hasn’t been moved yet.

This is what it looks like afterwards:

Now add the rest of your words and you’re done! Well actually, you’ve probably been handed a legacy glossary and asked to turn it into SKOS. Good luck with that!  Unless you know computer languages; in which case all you need to do is use SKOSed to check your work after you’ve programmatically translated your legacy glossary into SKOS.  If you do know java, then see how I did it in the GlossaryParser class.

Step Eight: Save Your Work

At some point, you’ll want to save your work with File --> Save As:

Hit OK (or the Enter key) and then type anything that makes sense (for me, that was GlossaryForWorkingOntologist.skos, but you can use foo.owl or bar or test.xml). At any rate, Protégé adds an “.owl” extension if that isn’t what you already have. Afterwards, hit control-S to just save your work to this file.