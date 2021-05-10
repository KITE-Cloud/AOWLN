package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas Farrenkopf on 31.05.2017.
 */
public class BuiltInAtomCustom extends CustomSWRLAtom {

    String[] arguments;
    List<String> literals;
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
        this.literals = new ArrayList<>();
    }

    public String[] getArguments() {
        return arguments;
    }

    public boolean isBound() {
        return isBound;
    }

    public List<String> getLiterals() {
        return literals;
    }

    public void setLiterals(List<String> literals) {
        this.literals = literals;
    }
}
