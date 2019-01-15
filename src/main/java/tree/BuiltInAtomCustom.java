package tree;

/**
 * Created by Thomas Farrenkopf on 31.05.2017.
 */
public class BuiltInAtomCustom extends CustomSWRLAtom {

    String[] arguments;

    boolean isBound;

    public BuiltInAtomCustom(String key) {
        super(key);
    }

    public BuiltInAtomCustom(String key, String label) {
        super(key, label);
    }


    public BuiltInAtomCustom(String key, String label, boolean isBound, String[] argumentsFromTheSecond) {
        super(key, label);
        this.isBound = isBound;
        this.arguments = argumentsFromTheSecond;
    }

    public String[] getArguments() {
        return arguments;
    }

    public boolean isBound() {
        return isBound;
    }
}
