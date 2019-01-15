package services;

import guru.nidi.graphviz.attribute.Shape;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.model.Node;

import static guru.nidi.graphviz.model.Factory.node;

public class NodeFactory {

    public Node getBuiltInNode(String text){
        Node builtInNode = node(text).with(Shape.TRAPEZIUM);
        return builtInNode;
    }

    public Node getClassNode(String text){
        Node builtInNode = node(text).with(Shape.RECTANGLE);
        return builtInNode;
    }

    public Node getDataPropertyNode(String text){
        Node builtInNode = node(text).with(Shape.TRAPEZIUM);
        return builtInNode;
    }

    public void linkClassNodeToPNode(Node start, Node target){
        start.link(target).with(Style.BOLD);
    }

}
