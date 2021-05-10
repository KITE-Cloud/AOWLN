package controller;

import model.CustomSWRLAtom;
import model.GraphListsForViz;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.swrlapi.core.SWRLAPIRule;
import org.swrlapi.core.SWRLRuleRenderer;
import services.AOWLNEngine;
import services.GraphVizGenerator;
import services.OWLUtil;

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
}
