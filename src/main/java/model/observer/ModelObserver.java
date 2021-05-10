package model.observer;

import java.util.ArrayList;

public interface ModelObserver {

    void ruleListHasChanged(ArrayList<String> rules);

}
