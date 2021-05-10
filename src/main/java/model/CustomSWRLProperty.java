package model;

/**
 * Created by Thomas Farrenkopf on 31.05.2017.
 */
public abstract class CustomSWRLProperty extends CustomSWRLAtom {

    private String firstArgument;

    public CustomSWRLProperty(String key) {
        super(key);
    }

    public CustomSWRLProperty(String firstArgument, String key, String label) {
        super(key, label);
        this.firstArgument = firstArgument;
    }

    public String getFirstArgument() {
        return firstArgument;
    }

}
