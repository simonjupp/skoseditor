# Introduction #

SKOSEd is still under development so full documentation will come later. In the meantime I will try and put some hints and tips to help get people new to SKOS and particularly new to Protege up to speed.


# Background #

SKOSEd is built on top of a pretty sophisticated ontology engineering tool called Protege. This means that new users may be daunted by all the functionality offered by Protege! The advantage of building SKOSEd on top of Protege is you get an extremely powerful and flexible editor for working with SKOS, but to exploit this functionality you need a good understanding of both OWL's relationship to SKOS and the Protege interface in general.

At some point in the future a SKOSEd light version will be available that will be a standalone SKOS editor that hides a lot of the more advanced Protege features. This will be intended for users who simply want to work with pure SKOS and don't care about extending the underlying OWL data model for SKOS.

It is worth pointing out that a default version of Protege 4 is a perfectly capable SKOS editor, what SKOSEd really provides is a set of useful views over SKOS and some convenience buttons and menu items for working with SKOS. Finally, the Protege environment  is built on top of the OSGI plugin framework, so all the views, tabs, menus etc are just plugins. This means that if you know a bit of Java it isn't too difficult to write your own views that would link up to Protege and SKOSEd. All the code is open source and available from the [sources](http://code.google.com/p/skoseditor/source/checkout) section of this site. If you want to get involved with SKOSEd just send me an e-mail at jupp AT ebi.ac.uk.


More to come.... This is a Wiki so feel free to help me write some simple documentation :-)