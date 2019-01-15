package tree;

/**
 * Created by Thomas Farrenkopf on 19.06.2017.
 */
public class GraphListsForViz {



    NodeConnection[] connections;
    NodeInfo[] nodes;

    public GraphListsForViz(NodeInfo[] nodes, NodeConnection[] connections){
        this.nodes = nodes;
        this.connections = connections;
    }

    public NodeConnection[] getConnections() {
        return connections;
    }

    public NodeInfo[] getNodes() {
        return nodes;
    }
}
