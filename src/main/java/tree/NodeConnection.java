package tree;

/**
 * Created by Thomas Farrenkopf on 19.06.2017.
 */
public class NodeConnection {
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
