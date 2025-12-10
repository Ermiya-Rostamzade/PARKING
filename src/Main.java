//import java.util.Scanner;

//public class Main {
    //public static void main(String[] args) {
        // System.out.println("--- 1. Initializing Parking (3 Stacks, Capacity 3 each) ---");
        // Parking parking = new Parking(3, 3);
        // System.out.println("\n--- 2. Adding Cars to Queue ---");
        // int[] carIds = {101, 102, 103, 201, 202, 301, 302, 303, 400};
        // for (int id : carIds) {
        //     parking.addToQueue(new Car(id));
        //     System.out.println("Car " + id + " added to queue.");
        // }
        // System.out.println("\n--- 3. Parking Cars (First Available) ---");
        // for (int i = 0; i < 5; i++) {
        //     parking.ParkFirstAvaliable();
        // }
        // System.out.println("\n--- 4. Parking in Specific Stack ---");
        // parking.parkSpecificStack(2);
        // System.out.println("\n--- 5. Finding Cars ---");
        // int searchId = 102;
        // int[] location = parking.findCar(searchId);
        // if (location[0] != -1) {
        //     System.out.println("Car " + searchId + " found in Stack " + (location[0] + 1) +
        //             ", Position from top: " + location[1]);
        // } else {
        //     System.out.println("Car " + searchId + " not found.");
        // }
        // int[] missing = parking.findCar(999);
        // if (missing[0] == -1) {
        //     System.out.println("Car 999 correctly not found.");
        // }
        // System.out.println("\n--- 6. Exiting Cars ---");
        // System.out.println("Attempting to exit Car 101 (Bottom of Stack 1)...");
        // parking.exitCar(101);
        // System.out.println("Attempting to exit Car 103 (Top of Stack 1)...");
        // parking.exitCar(103);
        // System.out.println("\n--- 7. Testing Transfer Stacks ---");
        // System.out.println("Transferring from Stack 2 to Stack 3...");
        // parking.transferStacks(1, 2);
        // int[] movedCarLoc = parking.findCar(202);
        // System.out.println("Car 202 is now in Stack: " + (movedCarLoc[0] + 1));
        // System.out.println("\n--- 8. Testing Sort Stack ---");
        // parking.addToQueue(new Car(50));
        // parking.addToQueue(new Car(10));
        // parking.addToQueue(new Car(30));
        // System.out.println("Sorting Stack 3...");
        // parking.sortStack(2);
        // System.out.println("Sort operation called.");
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("=== Parking System Initialized ===");
        System.out.print("Enter number of stacks: ");
        int numStacks = input.nextInt();

        System.out.print("Enter capacity of each stack: ");
        int capacity = input.nextInt();

        Parking parking = new Parking(numStacks, capacity);

        int choice;

        do {
            System.out.println("\n===========================");
            System.out.println("       PARKING MENU");
            System.out.println("===========================");
            System.out.println("1. Add Car to Queue");
            System.out.println("2. Park First Available");
            System.out.println("3. Park in Specific Stack");
            System.out.println("4. Find Car");
            System.out.println("5. Exit Car");
            System.out.println("6. Transfer Stacks");
            System.out.println("7. Sort a Stack");
            System.out.println("8. Show Parking Status");
            System.out.println("9. Exit Program");
            System.out.println("===========================");
            System.out.print("Enter choice: ");
            // --- اعتبارسنجی ورودی منو ---
            if (input.hasNextInt()) {
                choice = input.nextInt();
            } else {
                System.out.println("Invalid input! Please enter a number (1-9).");
                input.next(); // پاک کردن ورودی نامعتبر
                choice = 0;   // انتخاب 0 برای تکرار مجدد حلقه
            }
            // --------------------------

            switch (choice) {

                case 1:
                    System.out.print("Enter Car ID: ");
                    int id = input.nextInt();
                    parking.addToQueue(new Car(id));
                    System.out.println("Car " + id + " added to entry queue.");
                    break;

                case 2:
                    parking.ParkFirstAvaliable();
                    break;

                case 3:
                    System.out.print("Enter Stack Index (1 to " + numStacks + "): ");
                    int s = input.nextInt() - 1;
                    parking.parkSpecificStack(s);
                    break;

                case 4:
                    System.out.print("Enter Car ID to find: ");
                    int findId = input.nextInt();
                    int[] loc = parking.findCar(findId);
                    if (loc[0] == -1)
                        System.out.println("Car not found.");
                    else
                        System.out.println("Car is in Stack " + (loc[0] + 1) +
                                ", Position from top = " + loc[1]);
                    break;

                case 5:
                    System.out.print("Enter Car ID to exit: ");
                    int exitId = input.nextInt();
                    parking.exitCar(exitId);
                    break;

                case 6:
                    System.out.print("Transfer FROM stack index: ");
                    int from = input.nextInt() - 1;
                    System.out.print("Transfer TO stack index: ");
                    int to = input.nextInt() - 1;
                    parking.transferStacks(from, to);
                    break;

                case 7:
                    System.out.print("Enter stack number to sort: ");
                    int sortIndex = input.nextInt() - 1;
                    parking.sortStack(sortIndex);
                    System.out.println("Sort completed.");
                    break;

                case 8:
                    parking.showStacksStatus();
                    break;

                case 9:
                    System.out.println("Exiting program...");
                    break;

                default:
                    System.out.println("Invalid choice!");
            }

        } while (choice != 9);

        input.close();
    }
}

