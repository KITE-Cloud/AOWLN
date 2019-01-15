package linkedList;

/**
 * Created by Thomas Farrenkopf on 15.05.2017.
 */
public class LinkedList<T> {
    private ListElement<T> start;

    public void setStart(ListElement<T> elt) {
        start = elt;
    }

    public ListElement<T> getStart() {
        return start;
    }

    public void append(ListElement<T> neuelt) {
        if (start == null) {
            setStart(neuelt);
            return;
        } else {
            ListElement<T> elt = start;
            while (elt.getSuccessor() != null)
                elt = elt.getSuccessor();
            elt.setSuccessor(neuelt);
        }

    }

    public ListElement<T> find(ListElement<T> find) {
        ListElement<T> elt = start;
        while (elt != null) {
            if (elt == find) return elt;
            elt = elt.getSuccessor();
        }
        return null;
    }

    public String toString() {
        String str = "";
        ListElement<T> elt = start;
        while (elt != null) {
            str = str + " " + elt.toString();
            elt = elt.getSuccessor();
        }
        return str;
    }

    public void insertAfter(ListElement<T> wo, ListElement<T> element) {
        element.setSuccessor(wo.getSuccessor());
        wo.setSuccessor(element);
    }

    public void insertBefore(ListElement<T> wo, ListElement<T> elt) {
        if (this.start == wo) {
            elt.setSuccessor(start);
            setStart(elt);
        } else {
            ListElement<T> x = start;
            ListElement<T> y;
            do {
                y = x;
                x = x.getSuccessor();
            } while ((x != null) && (x != wo));
            if (x != null) {
                elt.setSuccessor(x);
                y.setSuccessor(elt);
            }
        }
    }

    public void delete(ListElement<T> elt) {
        if (this.start == elt) start = elt.getSuccessor();
        else {
            ListElement<T> x = start;
            ListElement<T> y;
            do {
                y = x;
                x = x.getSuccessor();
            } while ((x != null) && (x != elt));
            if (x != null)
                y.setSuccessor(elt.getSuccessor());
        }

    }

    public void printToConsole() {
        System.out.println(this.toString());
    }

    public ListElement<T> pop() {
        ListElement<T> elt = this.start;
        if (this.start != null)
            this.setStart(start.getSuccessor());
        return elt;
    }

    public void push(ListElement<T> elt) {
        if (start == null)
            this.setStart(elt);
        else {
            elt.setSuccessor(start);
            this.setStart(elt);
        }
    }

    public ListElement<T> findData(Object obj) {
        ListElement<T> elt = start;
        while (elt != null) {
            if (elt.getData().equals(obj)) return elt;
            elt = elt.getSuccessor();
        }
        return null;
    }

    public ListElement<T> findData(int intwert) {
        ListElement<T> elt = start;
        while (elt != null) {
            if (elt.getData() instanceof Integer)
                if (((Integer) elt.getData()).intValue() >= intwert) return elt;
            elt = elt.getSuccessor();
        }
        return null;
    }

    public LinkedListIterator<T> iterator() {
        return new LinkedListIterator<T>(this);
    }


}
