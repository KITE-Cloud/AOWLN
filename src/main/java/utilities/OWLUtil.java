package utilities;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.swrlapi.core.IRIResolver;
import org.swrlapi.core.SWRLAPIRule;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.core.SWRLRuleRenderer;
import org.swrlapi.factory.SWRLAPIFactory;
import org.swrlapi.factory.SWRLAPIInternalFactory;

import java.util.ArrayList;
import java.util.Set;

public class OWLUtil {

    private OWLOntologyManager manager;
    private OWLOntology ontology;
    private DefaultPrefixManager prefixManager;
    private Set<SWRLAPIRule> allRules;

    public void loadOntology(String filepath) {
        manager = OWLManager.createOWLOntologyManager();
        try {
            ontology = manager.loadOntologyFromOntologyDocument(FileUtil.getInputStream(filepath));
        } catch (OWLOntologyCreationException e) {
            e.printStackTrace();
        }

        prefixManager = new DefaultPrefixManager(null, null, ontology.getOntologyID().getOntologyIRI().get().toString() + "#");
        allRules = getSWRLRuleEngine().getSWRLRules();
    }

    public void setOntology(OWLOntology ontology) {
        manager = OWLManager.createOWLOntologyManager();
        this.ontology = ontology;
        prefixManager = new DefaultPrefixManager(null, null, ontology.getOntologyID().getOntologyIRI().get().toString() + "#");
        allRules = getSWRLRuleEngine().getSWRLRules();

    }

    public SWRLRuleEngine getSWRLRuleEngine() {
        IRIResolver iriResolver = SWRLAPIFactory.createIRIResolver(prefixManager.getDefaultPrefix());
        SWRLRuleEngine ruleEngine = SWRLAPIFactory.createSWRLRuleEngine(ontology, iriResolver);
        return ruleEngine;
    }

    public SWRLRuleRenderer getRuleRenderer() {
        IRIResolver iriResolver = SWRLAPIFactory.createIRIResolver(prefixManager.getDefaultPrefix());
        SWRLRuleRenderer swrlRuleRenderer = SWRLAPIInternalFactory.createSWRLRuleRenderer(ontology, iriResolver);
        return swrlRuleRenderer;
    }

    public ArrayList<SWRLAPIRule> getAllRules() {
        return new ArrayList<SWRLAPIRule>(allRules);
    }

    public SWRLAPIRule getSWRLRule(String ruleName) {
        ArrayList<SWRLAPIRule> allRules = getAllRules();
        for (SWRLAPIRule rule : allRules) {
            if (rule.getRuleName().equals(ruleName)) return rule;
        }
        return null;
    }

}
