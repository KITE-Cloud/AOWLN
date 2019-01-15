package tree;

/**
 * Created by Thomas Farrenkopf on 15.05.2017.
 */
public class AOWLNElement{
 AOWLNElementTypeEnum elementType;
    String identifier;
    String label;

    public AOWLNElement(AOWLNElementTypeEnum elementType, String identifier, String label) {
        this.elementType = elementType;
        this.identifier = identifier;
        this.label = label;
    }

    public AOWLNElementTypeEnum getElementType() {
        return elementType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AOWLNElement element = (AOWLNElement) o;

        if (elementType != element.elementType) return false;
        if (identifier != null ? !identifier.equals(element.identifier) : element.identifier != null) return false;
        return label != null ? label.equals(element.label) : element.label == null;
    }

    @Override
    public int hashCode() {
        int result = elementType != null ? elementType.hashCode() : 0;
        result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        return result;
    }
}