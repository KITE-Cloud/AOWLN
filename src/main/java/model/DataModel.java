package model;

import java.util.ArrayList;
import interfaces.*;


public class DataModel {

    public DataModel() {
    }

    private ArrayList<String> SWRLRulesAsString;

    ArrayList<ModelObserver> observerList = new ArrayList<>();

    public void registerObserver(ModelObserver modelObserver) {
        observerList.add(modelObserver);
    }

    public void removeObserver(ModelObserver modelObserver) {
        observerList.remove(modelObserver);
    }



    public ArrayList<String> getSWRLRulesAsString() {
        return SWRLRulesAsString;
    }

    public void setSWRLRulesAsString(ArrayList<String> SWRLRulesAsString) {
        this.SWRLRulesAsString = SWRLRulesAsString;

        for (ModelObserver modelObserver : observerList) {
            modelObserver.ruleListHasChanged(SWRLRulesAsString);
        }
    }
}
