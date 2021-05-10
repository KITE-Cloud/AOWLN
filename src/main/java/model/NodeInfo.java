package model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Thomas Farrenkopf on 19.06.2017.
 */
public class NodeInfo implements Serializable {

    private static final long serialVersionUID = -1893127371755106415L;

    String caption;
    String info;
    String type;
    ArrayList <String>builtInCollection;
    Boolean isBuiltInCollection;
    String builtInFirstVariable;
    static int builtInNumber = 0;



    public NodeInfo(String caption, String info, String type) {
        super();
        this.caption = caption;
        this.info = info;
        this.type = type;
        builtInCollection = new ArrayList();
        builtInCollection.add(caption);
        isBuiltInCollection = false;

    }

    public String getCaption() {

        return caption;
    }

    public String getInfo() {
        return info;
    }


    public String getType() {
        return type;
    }


    public ArrayList<String> getBuiltInCollection() {
        return builtInCollection;
    }


    public Boolean getIsBuiltInCollection(){return this.isBuiltInCollection;};

    public void setIsBuiltInCollection(Boolean isBuiltInCollection) {
        this.isBuiltInCollection = isBuiltInCollection;
    }

    public String getBuiltInFirstVariable() {
        return builtInFirstVariable;
    }

    public void setBuiltInFirstVariable(String builtInFirstVariable) {
        this.builtInFirstVariable = builtInFirstVariable;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
