package model;

import guru.nidi.graphviz.model.MutableNode;


public class MutableNodeExt {

    MutableNode node;
    String type;


    public MutableNodeExt(MutableNode node, String type) {
        this.node = node;
        this.type = type;
    }

    public MutableNode getNode() {
        return node;
    }

    public void setNode(MutableNode node) {
        this.node = node;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
