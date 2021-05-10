package model;

/**
 * Created by Thomas Farrenkopf on 31.05.2017.
 */
public abstract class CustomSWRLAtom {

    private String key;
    private String label;

    public CustomSWRLAtom(String key) {
        this.key = key;
    }

    public CustomSWRLAtom(String key, String label) {
        this(key);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        System.out.println("label: " + label);
        return label;
    }

    public String getKey() {
        return key;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof CustomSWRLAtom){
            return key.equals(((CustomSWRLAtom)obj).getKey());
        }
        return super.equals(obj);
    }
}
