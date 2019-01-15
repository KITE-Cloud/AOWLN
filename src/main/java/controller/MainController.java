package controller;

import model.DataModel;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.swrlapi.core.SWRLAPIRule;
import org.swrlapi.core.SWRLRuleRenderer;
import services.AOWLNEngine;
import services.GraphVizGenerator;
import services.OWLUtil;
import tree.CustomSWRLAtom;
import tree.GraphListsForViz;
import view.AOWLNPanel;
import view.AOWLNProtegeView;
import view.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;

public class MainController {

    private DataModel dataModel;

    private OWLUtil owlUtil;

    private AOWLNProtegeView protegeView;
    private AOWLNPanel aowlnPanel;

    private ArrayList<SWRLAPIRule> allRules;
    private AOWLNEngine aowlnEngine;

    private static MainController ourInstance = new MainController();


    public static MainController getInstance() {
        return ourInstance;
    }

    private MainController() {
    }

    public void start(AOWLNProtegeView aowlnProtegeView) {

        protegeView = aowlnProtegeView;
        protegeView.setLayout(new BorderLayout());

        aowlnPanel = new AOWLNPanel(this);
        protegeView.add(aowlnPanel, BorderLayout.CENTER);

        dataModel = new DataModel();
        dataModel.registerObserver(aowlnPanel);

        this.aowlnEngine = new AOWLNEngine();

        loadRulesfromOntology();

        new Thread((Runnable) () -> {
            protegeView.getOWLModelManager().getActiveOntology().getOWLOntologyManager().addOntologyChangeListener(aowlnPanel);
        }).start();


    }



    private void loadRulesfromOntology() {

        owlUtil = new OWLUtil();
        owlUtil.setOntology(protegeView.getOWLModelManager().getActiveOntology());
        SWRLRuleRenderer ruleRenderer = owlUtil.getRuleRenderer();

        allRules = owlUtil.getAllRules();

        ArrayList<String> SWRLRulesAsString = new ArrayList<>();
        for (SWRLAPIRule swrlapiRule : allRules) {
            SWRLRulesAsString.add(swrlapiRule.getRuleName() + ": " + ruleRenderer.renderSWRLRule(swrlapiRule));
        }
        dataModel.setSWRLRulesAsString(SWRLRulesAsString);
    }

   /* public static void main(String[] args) {
        MainController mainController = new MainController();
        mainController.startSystem();
    }*/

   /*    public void startSystem() {

        MainFrame mainFrame = new MainFrame(this);
        aowlnPanel = mainFrame.getAOWLNPanel();
        mainFrame.setVisible(true);
    }
*/


    public void produceRuleImages(String ruleName) {
        SWRLAPIRule swrlRule = owlUtil.getSWRLRule(ruleName);

        ArrayList<CustomSWRLAtom> bodyTree = null;
        ArrayList<CustomSWRLAtom> headTree = null;
        HashSet<SWRLAtom> body = new HashSet<org.semanticweb.owlapi.model.SWRLAtom>(swrlRule.getBody()) ;
        HashSet<SWRLAtom> head = new HashSet<org.semanticweb.owlapi.model.SWRLAtom>(swrlRule.getHead()) ;

        bodyTree =  aowlnEngine.createSWRLAtomsForTree(body);
        headTree =  aowlnEngine.createSWRLAtomsForTree(head);

        GraphListsForViz vizListBody = aowlnEngine.megaAlgorithmus(bodyTree);
        GraphListsForViz vizListHead = aowlnEngine.megaAlgorithmus(headTree);

        GraphVizGenerator graphVizGenerator = new GraphVizGenerator();

        String fileNameHead = "head_"+swrlRule.getRuleName()+".png";
        String fileNameBody = "body_"+swrlRule.getRuleName()+".png";

        graphVizGenerator.generateGraphImage(vizListBody, "body.png");
        graphVizGenerator.generateGraphImage(vizListHead, "head.png");



        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                aowlnPanel.loadImages("body.png", "head.png");

            }
        });

    }
}
