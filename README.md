# AOWLN Visualiser
This work proposes a graphical visualisation for SWRL rules that we call Aided OWL Notation (AOWLN).
For practical application, we implemented a prototypical AOWLN Visualiser tool as a Java-based Protégé Plugin.


## Demo
Try our online Demo at: https://bit.ly/2NfjH7v
You can also download our "demo.owl" available in this repository and try the plugin in Protégé.


## Getting started
To get started with the plugin please follow the instructions: 

### 1. Download the plugin:
There are two options to download the plugin. 
You can find the latest version of the plugin within the Protégé Ontology editor using the auto-update function, which checks for available plugins. Older versions of the plugin are be available on this Github Repository (Kite-Cloud/AOWLN). Under "releases" you will find all published versions which have to be manually added to the "plugins" folder of your Protégé.

Furthermore, the plugin requires an external jar (aowln-image-engine.jar) which is available in the "setup" folder of this repository. Please download this jar to a local folder. This external jar contains the rendering engine for visualisation of the rule graphs.

### 2. Using the plugin
To open the installed plugin in Protége navigate to: Window -> Tabs -> AOWLN-Tab

When using the AOWLN Tab in Protégé you will be requested to input a local path for the external jar downloaded in step 1 (aowln-image-engine.jar). To continue you must provide the correct path to the jar. Use the File-Explorer to select the jar file and click on the "Continue" button. 

The plugin is now ready and can be used to visualize SWRL rules in the ontology. A dropdown menu located at the top of the plugin provides a list of all available rules in the ontology. Press the "Select" button to start the visualisation process. The plugin only visualises SWRL rules that comply to the rule convention introduced in AOWLN (for more information see http://bit.ly/AOWLN-Paper . 