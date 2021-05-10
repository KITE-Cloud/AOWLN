package model;

import model.CustomSWRLProperty;

/**
 * Created by Thomas Farrenkopf on 31.05.2017.
 */
public class ObjectPropertyAtomCustom extends CustomSWRLProperty {

    public ObjectPropertyAtomCustom(String key) {
        super(key);
    }

    public ObjectPropertyAtomCustom(String firstArgument, String key, String label) {
        super(firstArgument, key, label);
    }
    public String getLastArgument(){
        return this.getKey();
    }
}
