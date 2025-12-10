public class Node {
    private Car data;
    private Node next;
    public Node(Car car){
        this.data = car;
        this.next = null;
    }
    public Car getData(){
        return data;
    }
    public Node getNext(){
        return next;
    }
    public void setNext(Node next){
        this.next = next;
    }
    
}
