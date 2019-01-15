package tree;


import linkedList.LinkedList;
import linkedList.ListElement;

/**
 * Created by Thomas Farrenkopf on 15.05.2017.
 */
public class Stack<T> {

    private LinkedList<T> elts = new LinkedList<T>();

    public T pop() {
        ListElement<T> e = elts.getStart();
        if (e != null) {
            elts.delete(e);
            return e.getData();
        } else
            return null;
    }

    public void push(T e) {
        elts.insertBefore(elts.getStart(),
                new ListElement<T>(e));
    }

    public boolean isEmpty() {
        return elts.getStart() == null;
    }
}
