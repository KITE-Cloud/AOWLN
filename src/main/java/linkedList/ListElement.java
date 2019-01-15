package linkedList;

/**
 * Created by Thomas Farrenkopf on 15.05.2017.
 */
public class ListElement<T>
{
    private T data;
    private ListElement<T> successor;

    public ListElement(T data){
        this.data=data;
    }    
    public ListElement(){
    }        

    public T getData() {
        return data;
    }
    public void setData(T obj) {
        data=obj;
    }    
    public ListElement<T> getSuccessor() {
        return successor;
    }            
    public void setSuccessor(ListElement<T> elt) {
        successor=elt;
    }           
    public String toString () {
        return data.toString();
    }
    
}
