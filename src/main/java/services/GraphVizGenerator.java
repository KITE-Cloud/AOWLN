package services;

import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.*;
import model.MutableNodeExt;
import tree.EdgeTypeEnum;
import tree.GraphListsForViz;
import tree.NodeConnection;
import tree.NodeInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.mutNode;
import static guru.nidi.graphviz.model.Link.to;

public class GraphVizGenerator {

    private static final String CLASS = "Class";
    private static final String PROPERTY = "Property";
    private static final String BICOLLECTION = "BuiltInCollection";
    private static final String VARIABLE = "Variable";
    private static final int PIXEL_PER_NODE = 250;

    public void generateGraphImage(GraphListsForViz nodeInfo, String rulePart) {

        NodeInfo[] nodes = nodeInfo.getNodes();

        ArrayList<MutableNodeExt> mutableNodes = new ArrayList<>();
        MutableGraph graph = new MutableGraph();
        graph.setDirected(true);

        for (NodeInfo node : nodes) {
            MutableNode mutNode = mutNode(node.getCaption());

            switch (node.getType()) {

                case "Class":
                    mutNode.add(Shape.RECTANGLE);
                    break;
                case "Property":
                    mutNode.add(Shape.ELLIPSE);
                    break;
                case "BuiltInCollection":
                    mutNode.add(Shape.DIAMOND);
                    break;
                case "Variable":
                    mutNode.add(Shape.TRAPEZIUM);
                    break;

            }

            //graph.add(mutNode);
            mutableNodes.add(new MutableNodeExt(mutNode, node.getType()));
        }

        NodeConnection[] connections = nodeInfo.getConnections();

        for (NodeConnection connection : connections) {

            String childCaption = connection.getChild().getCaption();
            String parentCaption = connection.getParent().getCaption();

            MutableNodeExt child = null, parent = null;

            for (MutableNodeExt mutableNodeExt : mutableNodes) {

                if (mutableNodeExt.getNode().label().toString().equals(childCaption)) {
                    child = mutableNodeExt;
                } else if (mutableNodeExt.getNode().label().toString().equals(parentCaption)) {
                    parent = mutableNodeExt;
                }

            }


            if (child != null && parent != null) {
                Link childLinkTo = child.getNode().linkTo();
                //Link childLinkTo = Link.between(parent.getNode(), child.getNode());
                if ((parent.getType().equals(PROPERTY) || child.getType().equals(PROPERTY)) && connection.getType().equals(EdgeTypeEnum.ObjectProperty)) {
                    childLinkTo = childLinkTo.with(Style.DASHED);
                    System.out.println();
                }
                if (child.getType().equals(CLASS) && parent.getType().equals(PROPERTY) && connection.getType().equals(EdgeTypeEnum.Normal)) {
                    childLinkTo = childLinkTo.with(Style.SOLID);
                    System.out.println();
                }
                else if (parent.getType().equals(PROPERTY) && child.getType().equals(VARIABLE)) {
                    if(connection.getLabel() != null) {
                        childLinkTo = childLinkTo.with(Label.of(connection.getLabel()));
                    }
                }
                parent.getNode().addLink(childLinkTo);

                graph.add(parent.getNode());
            }

        }

        if(connections.length == 0){
            for (MutableNodeExt node : mutableNodes) {
                graph.add(node.getNode());
            }
        }

        int imageWidth = nodes.length * PIXEL_PER_NODE;

        System.out.println();
        try {
            Graphviz.fromGraph(graph).width(imageWidth).render(Format.PNG).toFile(new File("images/"+rulePart));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private MutableNode getNodeByLabel(String label, MutableGraph graph){

        Collection<MutableNode> nodes = graph.nodes();

        for (MutableNode node : nodes) {
            if(node.label().toString().equals(label)) return node;
        }
        return null;
    }

}
