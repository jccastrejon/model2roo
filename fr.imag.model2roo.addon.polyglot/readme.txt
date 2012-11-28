Model2Roo - Add-on for polyglot persistence
-----------------------------------------------
To deploy new versions:
  mvn clean install
  copy /target/fr.imag.model2roo.addon.polyglot[version].jar and /target/fr.imag.model2roo.addon.polyglot[version]-sources.jar to /dist/

To install new versions:
  roo osgi uninstall --bundleSymbolicName fr.imag.model2roo.addon.polyglot
  roo osgi start --url https://model2roo.googlecode.com/git/fr.imag.model2roo.addon.polyglot/dist/fr.imag.model2roo.addon.polyglot[version].jar