import java.util.*;
import java.io.*;

class Car {
    int id;
    String make;
    String model;
    int year;
    double price;
    String color;
    int mileage;
    boolean available;

    Car(int id, String make, String model, int year, double price, String color, int mileage, boolean available) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
        this.color = color;
        this.mileage = mileage;
        this.available = available;
    }

    public String toString() {
        return "Car ID: " + id +
               ", Make: " + make +
               ", Model: " + model +
               ", Year: " + year +
               ", Price: " + price +
               ", Color: " + color +
               ", Mileage: " + mileage +
               ", Status: " + (available ? "Available" : "Sold");
    }

    public String toFileString() {
        return id + "," + make + "," + model + "," + year + "," + price + "," + color + "," + mileage + "," + available;
    }

    public static Car fromFileString(String line) {
        String[] p = line.split(",");
        return new Car(Integer.parseInt(p[0]), p[1], p[2], Integer.parseInt(p[3]), Double.parseDouble(p[4]), p[5], Integer.parseInt(p[6]), Boolean.parseBoolean(p[7]));
    }
}

public class CarInventoryManagement {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Car> inventory = new ArrayList<Car>();
    static int nextId = 1;
    static File file = new File("cars.txt");

    public static void main(String[] args) {
        loadFromFile();
        int choice = -1;
        while (choice != 0) {
            System.out.println("\n===============================");
            System.out.println("    CAR INVENTORY SYSTEM");
            System.out.println("===============================");
            System.out.println("1. Add New Car");
            System.out.println("2. View All Cars");
            System.out.println("3. Search Car by Model");
            System.out.println("4. Filter Cars by Price Range");
            System.out.println("5. Mark Car as Sold");
            System.out.println("6. Remove Car");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
            } else {
                System.out.println("Invalid input! Please enter a number.");
                sc.next();
                continue;
            }
            switch (choice) {
                case 1: addCar(); break;
                case 2: viewCars(); break;
                case 3: searchByModel(); break;
                case 4: filterByPrice(); break;
                case 5: markAsSold(); break;
                case 6: removeCar(); break;
                case 0: saveToFile(); System.out.println("Exiting System... Goodbye!"); break;
                default: System.out.println("Invalid Choice! Try again.");
            }
        }
    }

    public static void addCar() {
        sc.nextLine();
        System.out.print("Enter Car Make: ");
        String make = sc.nextLine();
        System.out.print("Enter Car Model: ");
        String model = sc.nextLine();
        System.out.print("Enter Manufacturing Year: ");
        int year = sc.nextInt();
        System.out.print("Enter Price: ");
        double price = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Color: ");
        String color = sc.nextLine();
        System.out.print("Enter Mileage (in KM): ");
        int mileage = sc.nextInt();
        Car c = new Car(nextId, make, model, year, price, color, mileage, true);
        nextId++;
        inventory.add(c);
        System.out.println("Car Added Successfully!");
        saveToFile();
    }

    public static void viewCars() {
        if (inventory.isEmpty()) {
            System.out.println("No Cars in Inventory.");
        } else {
            System.out.println("---- List of Cars ----");
            for (Car c : inventory) {
                System.out.println(c);
            }
        }
    }

    public static void searchByModel() {
        sc.nextLine();
        System.out.print("Enter Car Model to Search: ");
        String model = sc.nextLine();
        boolean found = false;
        for (Car c : inventory) {
            if (c.model.equalsIgnoreCase(model)) {
                System.out.println(c);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No Cars Found with Model: " + model);
        }
    }

    public static void filterByPrice() {
        System.out.print("Enter Minimum Price: ");
        double min = sc.nextDouble();
        System.out.print("Enter Maximum Price: ");
        double max = sc.nextDouble();
        boolean found = false;
        for (Car c : inventory) {
            if (c.price >= min && c.price <= max) {
                System.out.println(c);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No Cars Found in Price Range: " + min + " - " + max);
        }
    }

    public static void markAsSold() {
        System.out.print("Enter Car ID to Mark as Sold: ");
        int id = sc.nextInt();
        for (Car c : inventory) {
            if (c.id == id) {
                if (c.available) {
                    c.available = false;
                    System.out.println("Car with ID " + id + " marked as SOLD.");
                    saveToFile();
                } else {
                    System.out.println("Car is already SOLD.");
                }
                return;
            }
        }
        System.out.println("Car with ID " + id + " not found.");
    }

    public static void removeCar() {
        System.out.print("Enter Car ID to Remove: ");
        int id = sc.nextInt();
        Iterator<Car> it = inventory.iterator();
        while (it.hasNext()) {
            Car c = it.next();
            if (c.id == id) {
                it.remove();
                System.out.println("Car with ID " + id + " removed successfully.");
                saveToFile();
                return;
            }
        }
