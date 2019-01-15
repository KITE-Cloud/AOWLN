package linkedList;

import java.util.Iterator;

/**
 * Created by Thomas Farrenkopf on 15.05.2017.
 */
public class LinkedListIterator<T> implements Iterator<ListElement<T>> {
    private LinkedList<T> dieListe;
    private ListElement<T> current;
    private ListElement<T> lastCurrent;

    public LinkedListIterator(LinkedList<T> dieListe) {
        this.dieListe = dieListe;
        current = dieListe.getStart();
    }

    public boolean hasNext() {
        return (current != null);
    }

    public ListElement<T> next() {
        lastCurrent = current;
        current = current.getSuccessor();
        return lastCurrent;
    }

    public void remove() {
        dieListe.delete(lastCurrent);
    }


}

