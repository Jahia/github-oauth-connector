# Github OAuth Connector

### INFORMATION
This module works with Jahia OAuth and allows you to use Github open authentication API on your site.

### MINIMAL REQUIREMENTS
* DX 7.2.0.0
* Jahia OAuth module 1.1.0

### INSTALLATION
Download the jar and deploy it on your instance then activate the module on the site you wish to use.

### WHAT DOES THIS MODULE DO?
It brings the possibility to configure Github open authentication on your site.  
It allows you to display a button `Sign in with Github` on your site.

### HOW TO USE IT?
Once you have downloaded Jahia OAuth and at least one action module (type provider):
* Go to your `site > site settings > Jahia OAuth`
* In the panel you will see the Github OAuth Connector
* You will need to go to the Github developer website and setup an app to get the parameters. Please refer to the documentation on their website
* Once this is done a new button will appear `Actions` and if you click on it you will access to the action modules part
* On this part you can activate as many action modules type mapper as you which but you can only activate one provider
* Create a mapping for the provider
* Then in edit mode add the Github connection button to a page  
* Publish your site
* Your users can now connect using Github open authentication
