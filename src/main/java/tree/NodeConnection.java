package tree;

import java.io.Serializable;

/**
 * Created by Thomas Farrenkopf on 19.06.2017.
 */
public class NodeConnection implements Serializable {


    private static final long serialVersionUID = -7784394492127404634L;

    NodeInfo parent;
    NodeInfo child;
    EdgeTypeEnum type;
    String label;

    public NodeConnection(NodeInfo parent, NodeInfo child, EdgeTypeEnum type, String label) {
        super();
        this.parent = parent;
        this.child = child;
        this.type = type;
        this.label = label;
    }


    public String getLabel() {
        return label;
    }

    public NodeInfo getParent() {
        return parent;
    }

    public NodeInfo getChild() {
        return child;
    }

    public EdgeTypeEnum getType() {
        return type;
    }


}
