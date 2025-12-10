public class Queue {
    private Node front; 
    private Node rear;  
    private int size;   
    public Queue() {
        front = null;
        rear = null;
        size = 0;
    }

    public void enqueue(Car car) {
        Node newNode = new Node(car);
        if (rear == null) { 
            front = rear = newNode;
        } else {
            rear.setNext(newNode);
            rear = newNode;
        }
        size++;
    }

    public Car dequeue() {
        if (isEmpty()) {
            return null; 
        }
        Car car = front.getData();
        front = front.getNext();
        if (front == null) { 
            rear = null;
        }
        size--;
        return car;
    }

    public boolean isEmpty() {
        return front == null;
    }

    public int getSize() {
        return size;
    }

    public Car peek() {
        if (isEmpty()) return null;
        return front.getData();
    }
}