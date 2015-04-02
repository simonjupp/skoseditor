# Introduction #

SKOS is a model for describing controlled vocabularies, thesauri, classification systems and other similar knowledge artefacts. SKOS is currently undergoing standardisation by the Semantic Web Deployment Working Group (SWDWG) and will hopefully become a W3C recommendation sometime in 2008. You can find more information about SKOS here http://www.w3.org/2004/02/skos/


# Protege 4 #

Protege 4 (alpha) is an editor for OWL ontologies. It is being developed at The University of Manchester in collaboration with Stanford University. SKOS vocabularies can be built within the constraints of OWL which makes Protege an ideal tool for viewing and editing SKOS vocabularies. One of the major features of a SKOS model is the broader and narrower properties which are used to relate instances of the SKOS Concepts class. The broader/narrower relationships are often viewed as hierarchies, the first version of this plugin allows you to view the instance level broader/narrower hierarchy as a tree in Protege. The plan is to develop a range of views one might want when working with SKOS vocabularies in Protege.

# Download #

You will need the latest beta build of Protege 4. You can get this from the Stanford web site http://protege.stanford.edu/.

Next download the jar folder from this site and place it in the plugins directory.  And thats it!

Once the plugin is installed there is a set of built operators that allow you to create new SKOS vocabularies, if you want access to the full SKOS data model you need to load it from the web. You can do this by selecting the "Load SKOS data model" option from the SKOSEd menu. If this doesn't work you can always load the SKOS datamodel as an OWL ontology from a file using the load options from the "File" menu.




# Feedback #

At the moment if you have any feedback e-mail me directly at simon.jupp AT manchester.ac.uk. There is probably a  load of bugs (some I know about) some I don't. I am hoping to have a decent release available early 2008.