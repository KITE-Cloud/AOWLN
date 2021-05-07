# AOWLN Visualiser
![version](https://img.shields.io/badge/version-0.0.2-blue) ![license](https://img.shields.io/badge/license-GPLv3-purple) ![status](https://img.shields.io/badge/activity%20status-paused-lightgrey)

AOWLN Visualiser is a prototypical software to visualise Semantic Web Rule Language (SWRL) using our proposed graphical notation AOWLN (Aided OWL Notation). The visualiser is written in Java and has been implemented as a plugin for the open source ontology editor Protégé. AOWLN Visualiser uses [Graphviz](https://www.graphviz.org/) to create and display graphs. 

![image](https://user-images.githubusercontent.com/20316120/117473296-163dd300-af5a-11eb-97e7-883ecff1ad8e.png)


For citation of AOWLN Visualiser, please refer to our publication:
[Nguyen J, Geyer J, Farrenkopf T, Guckert M. (2018) Aided OWL Notation (AOWLN): Conceptual Modelling and Visualisation of Advanced SWRL Rules, IC3K 2018, 10th International Joint Conference on Knowledge Discovery, Knowledge Engineering and Knowledge Management, Seville, Spain](http://bit.ly/AOWLN-Paper)


## Demo
Try our online demo [here](https://bit.ly/2NfjH7v). 
You can also download our `demo.owl` available in this repository and try the plugin in Protégé.


## Getting started
To get started with the plugin please follow instructions: 

### 1. Download the plugin:
There are two options to download the plugin. 
You can find the latest version of the plugin within the Protégé Ontology editor using the auto-update function, which checks for available plugins. Older versions of the plugin are be available on this Github Repository (Kite-Cloud/AOWLN). Under "releases" you will find all published versions which have to be manually added to the "plugins" folder of your Protégé.

Furthermore, the plugin requires an external jar file `aowln-image-engine.jar` which is available in the `setup` folder of this repository. Please download this jar to a local folder. This external jar contains the rendering engine for visualisation of the rule graphs.

### 2. Using the plugin
To open the installed plugin in Protége navigate to: `Window -> Tabs -> AOWLN-Tab`

When using the AOWLN Tab in Protégé you will be requested to enter a local path to the external jar `aowln-image-engine.jar` that has been previously downloaded in step 1. To continue you must provide a valid path to the jar. Use the File-Explorer to select the jar file and click on the `Continue` button. 

The plugin is now ready and can be used to visualize SWRL rules in the ontology. A dropdown menu located at the top of the plugin provides a list of all available rules in the ontology. Press the `Select` button to start the visualisation process. The plugin only visualises SWRL rules that comply to the rule convention presented in [AOWLN](http://bit.ly/AOWLN-Paper). 

## Licensing
AGADE Traffic is an open source product licensed under GPLv3
