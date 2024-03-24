import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class PlaneManagement {
    private static boolean[][] seatingPlan = new boolean[4][]; // Array to store the seating plan
    private static final int[] seatsPerRow = {14, 12, 12, 14}; // Number of seats per row
    private static ArrayList<Ticket> ticketsSold = new ArrayList<>();
    public static void main(String[] args) {
        initializeSeatingPlan();
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            try {
                System.out.println("*************************************************");
                System.out.println("*                   MENU OPTIONS                 *");
                System.out.println("*************************************************");
                System.out.println("1) Buy a seat");
                System.out.println("2) Cancel a seat");
                System.out.println("3) Find first available seat");
                System.out.println("4) Show seating plan");
                System.out.println("5) Print tickets information and total sales");
                System.out.println("6) Search ticket");
                System.out.println("0) Quit");
                System.out.println("*************************************************");
                System.out.print("Please select an option: ");
                option = scanner.nextInt();

                switch (option) {
                    case 1:
                        System.out.println("You selected: Buy a seat");
                        buy_seat(scanner);
                        break;
                    case 2:
                        System.out.println("You selected: Cancel a seat");
                        cancel_seat(scanner);
                        break;
                    case 3:
                        System.out.println("You selected: Find first available seat");
                        find_first_available();
                        break;
                    case 4:
                        System.out.println("You selected: Show seating plan");
                        show_seating_plan();
                        break;
                    case 5:
                        System.out.println("You selected: Print tickets information and total sales");
                        print_tickets_info();
                        break;
                    case 6:
                        System.out.println("You selected: Search ticket");
                        search_ticket(scanner);
                        break;
                    case 0:
                        System.out.println("Exiting program...");
                        break;
                    default:
                        System.out.println("Invalid option! Please select a valid option.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid integer option.");
                scanner.nextLine(); // Clear the input buffer
                option = -1; // Reset the option to continue the loop
            }
        } while (option != 0);

        scanner.close();
    }
    private static void initializeSeatingPlan() {
        for (int i = 0; i < seatingPlan.length; i++) {
            seatingPlan[i] = new boolean[seatsPerRow[i]]; // Initialize each row with the corresponding number of seats
        }
    }

    private static void buy_seat(Scanner scanner) {
        System.out.print("Enter the row letter (A-D): ");
        char rowLetter = scanner.next().toUpperCase().charAt(0); // Convert input to uppercase and get the first character
        System.out.print("Enter the seat number (1-14 for row A and D, 1-12 for rows B and C): ");
        int seatNumber = scanner.nextInt();

        int row = rowLetter - 'A'; // Convert the row letter to its corresponding index (A -> 0, B -> 1, etc.)

        // Check if the row and seat number are valid
        if (row < 0 || row >= seatingPlan.length || seatNumber < 1 || seatNumber > seatsPerRow[row]) {
            System.out.println("Invalid row or seat number!");
            return;
        }

        // Check if the seat is available
        if (seatingPlan[row][seatNumber - 1]) {
            System.out.println("Seat is already sold!");
        } else {
            // Ask for person's information
            System.out.print("Enter person's name: ");
            String name = scanner.next();
            System.out.print("Enter person's surname: ");
            String surname = scanner.next();
            System.out.println("Enter person's email: ");
            String email = scanner.next();
            // Create a new Person object
            Person person = new Person(name, surname, email);

            // Calculate ticket price based on seat number
            double price;
            if (seatNumber <= 5) {
                price = 200.0; // Ticket price for seat numbers 1-5 is $200
            } else if (seatNumber <= 9) {
                price = 150.0; // Ticket price for seat numbers 6-9 is $150
            } else {
                price = 180.0; // Ticket price for seat numbers 10-14 is $180
            }

            // Create a new Ticket object and add it to the array list of tickets sold
            Ticket ticket = new Ticket(String.valueOf(rowLetter), seatNumber, price, person);
            ticketsSold.add(ticket);

            // Mark the seat as sold
            seatingPlan[row][seatNumber - 1] = true;
            ticket.save();
            System.out.println("Seat purchased successfully!");
        }
    }

    private static void cancel_seat(Scanner scanner) {
        System.out.print("Enter the row letter (A-D) of the ticket to cancel: ");
        char rowLetter = scanner.next().toUpperCase().charAt(0); // Convert input to uppercase and get the first character
        System.out.print("Enter the seat number of the ticket to cancel: ");
        int seatNumber = scanner.nextInt();

        // Iterate through the array list of tickets sold to find the ticket with the given row and seat number
        for (Ticket ticket : ticketsSold) {
            if (ticket.getRow().charAt(0) == rowLetter && ticket.getSeat() == seatNumber) {
                // Remove the ticket from the array list of tickets sold
                ticketsSold.remove(ticket);
                // Mark the seat as available again
                seatingPlan[rowLetter - 'A'][seatNumber - 1] = false;
                System.out.println("Ticket cancellation successful!");
                return;
            }
        }
        System.out.println("Ticket not found!");
    }

    private static void find_first_available() {
        int row = -1;
        int seat = -1;

        // Iterate through each row starting from row A to find the first available seat
        for (int i = 0; i < seatingPlan.length; i++) {
            for (int j = 0; j < seatingPlan[i].length; j++) {
                if (!seatingPlan[i][j]) {
                    row = i;
                    seat = j;
                    break;
                }
            }
            if (row != -1) {
                break; // Exit the loop if an available seat is found
            }
        }

        if (row != -1 && seat != -1) {
            char rowLetter = (char) ('A' + row); // Convert the row index to its corresponding letter
            System.out.println("First available seat found at Row " + rowLetter + ", Seat " + (seat + 1));
        } else {
            System.out.println("No available seats found.");
        }
    }

    private static void show_seating_plan() {
        System.out.println("Seating Plan:");
        for (int i = 0; i < seatingPlan.length; i++) {
            char rowLetter = (char) ('A' + i); // Convert the row index to its corresponding letter
            System.out.print("Row " + rowLetter + ": ");
            for (int j = 0; j < seatingPlan[i].length; j++) {
                if (seatingPlan[i][j]) {
                    System.out.print("X "); // Mark sold seats with 'X'
                } else {
                    System.out.print("O "); // Mark available seats with 'O'
                }
            }
            System.out.println(); // Move to the next line for the next row
        }
    }

    private static void print_tickets_info() {
        double totalAmount = 0.0;

        System.out.println("Tickets Sold during the Session:");
        for (Ticket ticket : ticketsSold) {
            System.out.println("---------------------------");
            ticket.printTicketInfo();
            totalAmount += ticket.getPrice();
        }

        System.out.println("---------------------------");
        System.out.println("Total Price of Tickets Sold: $" + totalAmount);
    }

    private static void search_ticket(Scanner scanner) {
        System.out.print("Enter the row letter (A-D): ");
        char rowLetter = scanner.next().toUpperCase().charAt(0); // Convert input to uppercase and get the first character
        System.out.print("Enter the seat number (1-14 for row A, 1-12 for rows B, C, D): ");
        int seatNumber = scanner.nextInt();

        // Iterate through the array list of tickets sold to find the ticket with the given row and seat number
        boolean ticketFound = false;
        for (Ticket ticket : ticketsSold) {
            if (ticket.getRow().charAt(0) == rowLetter && ticket.getSeat() == seatNumber) {
                // Print the ticket and person information
                System.out.println("Ticket Information:");
                ticket.printTicketInfo();
                ticketFound = true;
                break;
            }
        }

        if (!ticketFound) {
            System.out.println("This seat is available.");
        }
    }


}
