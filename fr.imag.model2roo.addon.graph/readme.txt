Model2Roo - Add-on for graph databases support.
-----------------------------------------------
To deploy new versions:
  mvn clean install
  copy /target/fr.imag.model2roo.addon.graph[version].jar and /target/fr.imag.model2roo.addon.graph[version]-sources.jar to /dist/

To install new versions:
  roo osgi uninstall --bundleSymbolicName fr.imag.model2roo.addon.graph
  roo osgi start --url https://model2roo.googlecode.com/git/fr.imag.model2roo.addon.graph/dist/fr.imag.model2roo.addon.graph[version].jar