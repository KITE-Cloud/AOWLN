package controller;

import model.observer.DataModel;
import org.swrlapi.core.SWRLAPIRule;
import view.AOWLNPanel;
import view.AOWLNProtegeView;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ViewController {

    private DataModel dataModel;
    private AOWLNProtegeView protegeView;
    private AOWLNPanel aowlnPanel;
    AOWLNServiceFacade facade;


    private static ViewController ourInstance = new ViewController();

    public static ViewController getInstance() {
        return ourInstance;
    }

    private ViewController() {
    }

    public void start(AOWLNProtegeView aowlnProtegeView) {
        this.facade = new AOWLNServiceFacade();
        protegeView = aowlnProtegeView;
        protegeView.setLayout(new BorderLayout());
        aowlnPanel = new AOWLNPanel(this);
        protegeView.add(aowlnPanel, BorderLayout.CENTER);
        dataModel = DataModel.getInstance();
        dataModel.registerObserver(aowlnPanel);

        new Thread((Runnable) () -> {
            loadRulesfromOntology();
        }).start();

        new Thread((Runnable) () -> {
            protegeView.getOWLModelManager().getActiveOntology().getOWLOntologyManager().addOntologyChangeListener(aowlnPanel);
        }).start();
    }

    public void loadRulesfromOntology() {
        ArrayList<SWRLAPIRule> rules = facade.loadRulesfromOntology(protegeView.getOWLModelManager().getActiveOntology());
        dataModel.setSWRLRulesAsString((ArrayList<String>) facade.getRulesAsStrings(rules));
    }

    public void loadRuleImages(String ruleName) {

        facade.produceRuleImages(ruleName);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                aowlnPanel.loadImages();
            }
        });
    }

    /* public static void main(String[] args) {
           ViewController mainController = new ViewController();
           mainController.startSystem();
       }*/
       /*    public void startSystem() {

            MainFrame mainFrame = new MainFrame(this);
            aowlnPanel = mainFrame.getAOWLNPanel();
            mainFrame.setVisible(true);
        } */
}
