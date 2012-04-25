Follow these steps to deploy changes made to the Acceleo modules:

1. Clean project (Project->clean)
2. Delete 'features', 'plugins', 'artifacts.jar' and 'content.jar' 
   from the 'fr.imag.model2roo.update.site' project
3. Open the 'fr.imag.model2roo.update.site/site.xml',
   and execute the 'Build All' command
4. Export the 'fr.imag.model2roo.ecore.ui' and 'fr.imag.model2roo.uml.profiles'
   projects, as explained in the README files in each of these projects
5. Commit changes ('fr.imag.model2roo.uml' and 'fr.imag.model2roo.update.site') 
   to the project repository