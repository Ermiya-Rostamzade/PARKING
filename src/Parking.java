import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;


public class Parking {
    private LinkedListStack[] stacks;
    private Queue queue;
    private int numStacks;
    private CarDAO carDAO;

    public Parking(int n, int m) {
        this.numStacks = n;
        stacks = new LinkedListStack[n];
        // --- اضافه شدن قسمت دیتابیس ---
        SQLiteConnection.createTable(); // ایجاد جدول در زمان شروع
        this.carDAO = new CarDAO();
        // -----------------------------
        for (int i = 0; i < n; i++) {
            stacks[i] = new LinkedListStack(m, i, carDAO);
        }
        this.queue = new Queue();
    }

    public void addToQueue(Car car) {
        queue.enqueue(car);
    }

    public void ParkFirstAvaliable() {
       /*  Car carToPark = queue.dequeue();
        if (carToPark == null){
            System.out.println("the entry queue is empty");
            return;
        }*/
        //پیدا کردن استک خالی برای ماشین خارج شده از صف
        for (int i = 0; i < numStacks; i++) {
            if (!stacks[i].isFull()) {
                Car carToPark = queue.dequeue();
                if (carToPark == null) {
                    System.out.println("the entry queue is empty");
                    return;
                }

                stacks[i].push(carToPark);
                System.out.println("The Car : " + carToPark.getId() + " in stack number " + (i + 1) + " was parked");
                return;
            }
        }
        System.out.println("The parking is full. There is no parking available.");
    }

    /// / این ابهام داره******************************************************
    public void parkSpecificStack(int stackIndex) {

        if (stacks[stackIndex].isFull()) {
            System.out.println("Stack number " + (stackIndex + 1) + " is full. Cannot park car ");
            return;
        } else {
            Car carToPark = queue.dequeue();

            if (carToPark == null) {
                System.out.println("the entry queue is empty");
                return;
            }

            stacks[stackIndex].push(carToPark);
            System.out.println("The Car : " + carToPark.getId() + " in stack number " +
                    stackIndex + 1 + " was parked");

        }

    }

    public int[] findCar(int carid) {
        for (int i = 0; i < numStacks; i++) {
            Node current = stacks[i].getTop();
            int positionFromTop = 1;
            while (current != null) {
                if (current.getData().getId() == carid) {
                    // int stackcapasity = stacks[i].getCapacity();
                    return new int[]{i, positionFromTop};
                }
                current = current.getNext();
                positionFromTop++;

            }
        }
        return new int[]{-1, -1};

    }

    public Car exitCar(int carId) {
        int[] res = findCar(carId);

        int stackIndex = res[0];
        int position = res[1];

        if (stackIndex == -1) {
            System.out.println("Car not found.");
            return null;
        }

        // فقط اگر ماشین در بالای Stack باشد (جایگاه 1):
        if (position == 1) {
            Car removedCar = stacks[stackIndex].pop();
            // --- حذف از دیتابیس هنگام خروج ---
            if (removedCar != null) {
                carDAO.deleteCar(removedCar.getId());
            }
            // ------------------------------------
            System.out.println("Car " + carId + " exited from stack " + (stackIndex + 1));
            return removedCar;
        }

        // در غیر این صورت خروج ممنوع است:
        System.out.println("Car " + carId + " is not at the top of stack " + (stackIndex + 1) +
                ". Exit not allowed.");
        return null;
    }

    public void sortStack(int stackIndex) {
        if (stackIndex < 0 || stackIndex >= numStacks) {
            System.out.println("Invalid stack index.");
            return;
        }
        // حذف تمام رکوردهای Stack از دیتابیس قبل از مرتب سازی
        carDAO.deleteStackCars(stackIndex);

        // مرتب سازی در حافظه
        stacks[stackIndex].sortStack();

        // ذخیره مجدد تمام ماشین های Stack در دیتابیس با ترتیب جدید
        stacks[stackIndex].saveAllToDatabase();

        System.out.println("Stack " + (stackIndex + 1) + " has been sorted and database updated.");
    }

    public void transferStacks(int i, int j) {
        if (i < 0 || i >= numStacks || j < 0 || j >= numStacks || i == j) {
            System.out.println("Invalid stack indices");
            return;
        }
        while (!stacks[i].isEmpty()) {
            Car carToMove = stacks[i].pop();
            // --- اضافه کردن منطق حذف از دیتابیس ---
            if (carToMove != null) {
                carDAO.deleteCar(carToMove.getId()); // 2. حذف رکورد قبلی از دیتابیس
            }
            // ----------------------------------------
            if (!stacks[j].isFull()) {
                stacks[j].push(carToMove);
            } else {
                boolean parked = false;
                for (int k = 0; k < numStacks; k++) {
                    if (k != j && k != i && !stacks[k].isFull()) {
                        stacks[k].push(carToMove);
                        parked = true;
                        break;
                    }
                }
                if (!parked) {
                    System.out.println("Parking is full " + carToMove.getId());
                    stacks[i].push(carToMove);
                    return;
                }
            }
        }
        System.out.println("Transfer from stack " + (i + 1) + " to " + (j + 1) + " completed");
    }

    public void showStacksStatus() {

        System.out.println("===== Parking Stacks Status =====");

        for (int i = 0; i < numStacks; i++) {
            System.out.print("Stack " + (i + 1) + ": ");

            if (stacks[i].isEmpty()) {
                System.out.println("[ EMPTY ]");
                continue;
            }

            Node current = stacks[i].getTop();
            System.out.print("[TOP] ");

            while (current != null) {
                System.out.print(current.getData().getId());

                current = current.getNext();
                if (current != null) System.out.print(" -> ");
            }

            System.out.println(" [BOTTOM]");
        }

        System.out.println("=================================");
    }

    // متد جدید برای نمایش وضعیت دیتابیس
    private void showDatabaseStatus() {
        System.out.println("\n--- Current Database Status (SQLite) ---");
        String sql = "SELECT CarID, StackIndex, PositionInStack FROM Cars ORDER BY StackIndex, PositionInStack DESC";

        try (Connection conn = SQLiteConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("CarID | StackIndex | PositionInStack");
            System.out.println("---------------------------------");

            boolean found = false;
            while (rs.next()) {
                found = true;
                int carID = rs.getInt("CarID");
                int stackIndex = rs.getInt("StackIndex");
                int position = rs.getInt("PositionInStack");
                System.out.printf("%5d | %10d | %15d\n", carID, stackIndex + 1, position);
            }

            if (!found) {
                System.out.println("No cars found in the database.");
            }

        } catch (SQLException e) {
            System.err.println("خطا در خواندن از دیتابیس: " + e.getMessage());
        }


    }
}
