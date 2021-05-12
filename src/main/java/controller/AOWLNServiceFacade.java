package controller;

import model.*;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.swrlapi.core.SWRLAPIRule;
import org.swrlapi.core.SWRLRuleRenderer;
import utilities.AOWLNEngine;
import utilities.GraphVizGenerator;
import utilities.OWLUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AOWLNServiceFacade {
    private OWLUtil owlUtil;
    private AOWLNEngine aowlnEngine;
    private ArrayList<SWRLAPIRule> allRules;
    private SWRLRuleRenderer ruleRenderer;

    public AOWLNServiceFacade() {
        this.aowlnEngine = new AOWLNEngine();
        this.owlUtil = new OWLUtil();
    }

    public void produceRuleImages(String ruleName) {
        SWRLAPIRule swrlRule = owlUtil.getSWRLRule(ruleName);
        HashSet<SWRLAtom> body = new HashSet<org.semanticweb.owlapi.model.SWRLAtom>(swrlRule.getBody());
        HashSet<SWRLAtom> head = new HashSet<org.semanticweb.owlapi.model.SWRLAtom>(swrlRule.getHead());
        ArrayList<CustomSWRLAtom> bodyTree = aowlnEngine.createSWRLAtomsForTree(body);
        ArrayList<CustomSWRLAtom> headTree = aowlnEngine.createSWRLAtomsForTree(head);

        //Remove Dependency for AOWLN Convention: By creating necessary concepts
        List<ClassAtomCustom> bodyClasses = new ArrayList<>();
        List<ObjectPropertyAtomCustom> bodyOP = new ArrayList<>();
        List<DataPropertyAtomCustom> bodyDP = new ArrayList<>();
        List<ClassAtomCustom> headClasses = new ArrayList<>();
        List<ObjectPropertyAtomCustom> headOP = new ArrayList<>();
        List<DataPropertyAtomCustom> headDP = new ArrayList<>();

        for (CustomSWRLAtom swrlAtom : bodyTree) {
            if (swrlAtom instanceof ClassAtomCustom) {
                bodyClasses.add((ClassAtomCustom) swrlAtom);
            } else if (swrlAtom instanceof DataPropertyAtomCustom) {
                bodyDP.add((DataPropertyAtomCustom) swrlAtom);
            } else if (swrlAtom instanceof ObjectPropertyAtomCustom) {
                bodyOP.add((ObjectPropertyAtomCustom) swrlAtom);
            }
        }
        for (CustomSWRLAtom swrlAtom : headTree) {
            if (swrlAtom instanceof ClassAtomCustom) {
                headClasses.add((ClassAtomCustom) swrlAtom);
            } else if (swrlAtom instanceof DataPropertyAtomCustom) {
                headDP.add((DataPropertyAtomCustom) swrlAtom);
            } else if (swrlAtom instanceof ObjectPropertyAtomCustom) {
                headOP.add((ObjectPropertyAtomCustom) swrlAtom);
            }
        }

        List<String> keysToCheck = new ArrayList();
        for (ObjectPropertyAtomCustom op : bodyOP) {
            keysToCheck.add(op.getFirstArgument());
            keysToCheck.add(op.getLastArgument());
        }
        for (DataPropertyAtomCustom dp : bodyDP) {
            keysToCheck.add(dp.getFirstArgument());
        }

        //create default concepts for body
        for (String key : keysToCheck) {
            if (!checkIfConceptExists(key, bodyClasses)) {
                ClassAtomCustom defaultClassAtom = new ClassAtomCustom(key, "Thing");
                bodyClasses.add(0, defaultClassAtom);
                bodyTree.add(0, defaultClassAtom);
            }
        }

        keysToCheck = new ArrayList<>();
        for (ObjectPropertyAtomCustom op : headOP) {
            keysToCheck.add(op.getFirstArgument());
            keysToCheck.add(op.getLastArgument());
        }
        for (DataPropertyAtomCustom dp : headDP) {
            keysToCheck.add(dp.getFirstArgument());
        }

        //create default concepts for head
        for (String key : keysToCheck) {
            if (!checkIfConceptExists(key, headClasses)) {
                if (!checkIfConceptExists(key, bodyClasses)) {
                    ClassAtomCustom defaultClassAtom = new ClassAtomCustom(key, "Thing");
                    headClasses.add(0, defaultClassAtom);
                    headTree.add(0, defaultClassAtom);
                } else {
                    //getClassFrom Body
                    ClassAtomCustom conceptFromBody = findConceptByKey(key, bodyClasses);
                    headClasses.add(0, conceptFromBody);
                    headTree.add(0, conceptFromBody);
                }
            }
        }

        GraphListsForViz vizListBody = aowlnEngine.megaAlgorithmus(bodyTree);
        GraphListsForViz vizListHead = aowlnEngine.megaAlgorithmus(headTree);
        GraphVizGenerator graphVizGenerator = new GraphVizGenerator();

        graphVizGenerator.generateGraphImage(vizListBody, "body");
        System.out.println("Body Image generated");
        graphVizGenerator.generateGraphImage(vizListHead, "head");
        System.out.println("Head Image generated");
    }

    public ArrayList<SWRLAPIRule> loadRulesfromOntology(OWLOntology ontology) {
        owlUtil.setOntology(ontology);
        allRules = owlUtil.getAllRules();
        this.ruleRenderer = owlUtil.getRuleRenderer();
        ArrayList<String> SWRLRulesAsString = new ArrayList<>();
        for (SWRLAPIRule swrlapiRule : allRules) {
            SWRLRulesAsString.add(swrlapiRule.getRuleName() + ": " + ruleRenderer.renderSWRLRule(swrlapiRule));
        }
        return allRules;
    }

    public List<String> getRulesAsStrings(ArrayList<SWRLAPIRule> rules) {
        ArrayList<String> SWRLRulesAsStrings = new ArrayList<>();
        for (SWRLAPIRule swrlapiRule : rules) {
            SWRLRulesAsStrings.add(swrlapiRule.getRuleName() + ": " + ruleRenderer.renderSWRLRule(swrlapiRule));
        }
        return SWRLRulesAsStrings;
    }

    private boolean checkIfConceptExists(String key, List<ClassAtomCustom> ruleFragment) {
        boolean exist = false;

        for (ClassAtomCustom c : ruleFragment) {
            if (c.getKey().equals(key)) {
                exist = true;
            }
        }
        return exist;
    }

    private ClassAtomCustom findConceptByKey(String key, List<ClassAtomCustom> ruleFragment) {
        ClassAtomCustom relevant = null;
        for (ClassAtomCustom c : ruleFragment) {
            if (c.getKey().equals(key)) {
                relevant = c;
            }
        }
        return relevant;
    }
}
