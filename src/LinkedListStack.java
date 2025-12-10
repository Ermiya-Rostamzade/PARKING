public class LinkedListStack {
    private Node top;
    private int capacity;
    private int size;
    private int stackIndex; // اضافه شدن: شماره Stack
    private CarDAO carDAO; // اضافه شدن: شیء DAO

    public LinkedListStack(int capacity, int stackIndex, CarDAO carDAO) {
        this.capacity = capacity;
        this.top = null;
        this.size = 0;
        this.stackIndex = stackIndex;
        this.carDAO = carDAO;

    }

    public Node getTop() {
        return top;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean push(Car car) {
        if (size >= capacity) {
            return false;
        }
        Node newNode = new Node(car);
        newNode.setNext(top);
        top = newNode;
        size++;

        // --- اضافه شدن قسمت دیتابیس (ذخیره) ---
        // موقعیت در استک (PositionInStack) برای Pop اولویت 1 دارد و هر چه به عقب می رویم بیشتر می شود.
        carDAO.saveCar(car, stackIndex , size);
        // ----------------------------------------
        return true;

    }

    public Car pop() {
        if (isEmpty()) {
            return null;
        }
        Car carpopped = top.getData();
        top = top.getNext();
        size--;
        // در متد exitCar از کلاس Parking، حذف از دیتابیس انجام می شود.

        return carpopped;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public void sortStack() {
        System.out.println("the sorting stack is starded.");
        this.top = mergeSort(this.top);
        int newSize = 0;
        Node current = this.top;
        while (current != null) {
            newSize++;
            current = current.getNext();
        }
        this.size = newSize;

    }

    private Node mergeSort(Node head) {
        if (head == null || head.getNext() == null) {
            return head;
        }
        Node middle = getMiddle(head);
        Node nextOfmiddle = middle.getNext();
        middle.setNext(null);// قطع کردن لینک برای تقسیم لیست
        Node left = mergeSort(head);
        Node right = mergeSort(nextOfmiddle);
        Node sortedList = sortedMerge(left, right);
        return sortedList;
    }

    private Node getMiddle(Node head) {
        if (head == null) {
            return head;
        }
        Node slow = head;
        Node fast = head.getNext();

        while (fast != null && fast.getNext() != null) {
            slow = slow.getNext();
            fast = fast.getNext().getNext();
        }
        return slow;

    }

    private Node sortedMerge(Node a, Node b) {
        Node result = null;
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        if (a.getData().getId() <= b.getData().getId()) {
            result = a;
            result.setNext(sortedMerge(a.getNext(), b));
        } else {
            result = b;
            result.setNext(sortedMerge(a, b.getNext()));
        }
        return result;
    }
    // متد جدید برای ذخیره مجدد تمام ماشین‌ها بعد از مرتب سازی
    public void saveAllToDatabase() {
        Node current = this.top;
        int positionFromTop = 1;

        // ذخیره از بالا به پایین
        // از آنجا که استک باید ذخیره شود، ما ابتدا همه را حذف و سپس دوباره ذخیره می کنیم.
        while (current != null) {
            carDAO.saveCar(current.getData(), stackIndex, positionFromTop);
            current = current.getNext();
            positionFromTop++;
        }
    }
}