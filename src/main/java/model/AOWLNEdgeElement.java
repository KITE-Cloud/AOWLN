package model;

/**
 * Created by Thomas Farrenkopf on 31.05.2017.
 */
public class AOWLNEdgeElement{

    AOWLNElement from;
    AOWLNElement to;
    EdgeTypeEnum edgeType;
    String label;

    public AOWLNEdgeElement(AOWLNElement from, AOWLNElement to, EdgeTypeEnum edgeType) {
        this.from = from;
        this.to = to;
        this.edgeType = edgeType;
    }

    public AOWLNEdgeElement(AOWLNElement from, AOWLNElement to, EdgeTypeEnum edgeType, String label) {
        this(from, to, edgeType);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public AOWLNElement getFrom() {
        return from;
    }

    public AOWLNElement getTo() {
        return to;
    }

    public EdgeTypeEnum getEdgeType() {
        return edgeType;
    }


}
