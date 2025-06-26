package Client;

import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;
import Server.CabBookingInterface;

public class CabBookingClient {
    private static final String SERVICE_URL = "rmi://localhost:2001/CabService"; // Updated port
    
    public static void main(String[] args) {
        try {
            CabBookingInterface booking = (CabBookingInterface) Naming.lookup(SERVICE_URL);
            Scanner sc = new Scanner(System.in);
            
            System.out.println("🚕 Welcome to Enhanced Cab Booking System!");
            System.out.println("==========================================");
            
            while (true) {
                showMenu();
                int choice = getChoice(sc);
                
                switch (choice) {
                    case 1:
                        bookCab(booking, sc);
                        break;
                    case 2:
                        viewAvailableCabs(booking, sc);
                        break;
                    case 3:
                        cancelBooking(booking, sc);
                        break;
                    case 4:
                        viewBookingHistory(booking, sc);
                        break;
                    case 5:
                        addNewCab(booking, sc);
                        break;
                    case 6:
                        viewCabStatus(booking);
                        break;
                    case 7:
                        System.out.println("👋 Thank you for using Cab Booking System!");
                        return;
                    default:
                        System.out.println("❌ Invalid choice! Please try again.");
                }
                
                System.out.println("\nPress Enter to continue...");
                sc.nextLine();
            }
        } catch (Exception e) {
            System.err.println("❌ Connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void showMenu() {
        System.out.println("\n📋 Menu Options:");
        System.out.println("1. 🚖 Book a Cab");
        System.out.println("2. 📍 View Available Cabs");
        System.out.println("3. ❌ Cancel Booking");
        System.out.println("4. 📋 View Booking History");
        System.out.println("5. ➕ Add New Cab");
        System.out.println("6. 📊 View Cab Status");
        System.out.println("7. 🚪 Exit");
        System.out.print("\nEnter your choice (1-7): ");
    }
    
    private static int getChoice(Scanner sc) {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    private static void bookCab(CabBookingInterface booking, Scanner sc) {
        try {
            System.out.print("Enter your name: ");
            String name = sc.nextLine();
            System.out.print("Enter source location: ");
            String source = sc.nextLine();
            System.out.print("Enter destination: ");
            String dest = sc.nextLine();
            
            String result = booking.bookCab(name, source, dest);
            System.out.println("\n" + result);
        } catch (Exception e) {
            System.err.println("❌ Error booking cab: " + e.getMessage());
        }
    }
    
    private static void viewAvailableCabs(CabBookingInterface booking, Scanner sc) {
        try {
            System.out.print("Enter location to check: ");
            String location = sc.nextLine();
            
            List<String> cabs = booking.getAvailableCabs(location);
            System.out.println("\n🚕 Available Cabs at " + location + ":");
            if (cabs.isEmpty()) {
                System.out.println("No cabs available at this location.");
            } else {
                for (String cab : cabs) {
                    System.out.println("  " + cab);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error fetching available cabs: " + e.getMessage());
        }
    }
    
    private static void cancelBooking(CabBookingInterface booking, Scanner sc) {
        try {
            System.out.print("Enter booking ID to cancel: ");
            int bookingId = Integer.parseInt(sc.nextLine());
            
            String result = booking.cancelBooking(bookingId);
            System.out.println("\n" + result);
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid booking ID format!");
        } catch (Exception e) {
            System.err.println("❌ Error cancelling booking: " + e.getMessage());
        }
    }
    
    private static void viewBookingHistory(CabBookingInterface booking, Scanner sc) {
        try {
            System.out.print("Enter customer name: ");
            String name = sc.nextLine();
            
            String history = booking.getBookingHistory(name);
            System.out.println("\n" + history);
        } catch (Exception e) {
            System.err.println("❌ Error fetching booking history: " + e.getMessage());
        }
    }
    
    private static void addNewCab(CabBookingInterface booking, Scanner sc) {
        try {
            System.out.print("Enter cab location: ");
            String location = sc.nextLine();
            
            String result = booking.addCab(location);
            System.out.println("\n" + result);
        } catch (Exception e) {
            System.err.println("❌ Error adding cab: " + e.getMessage());
        }
    }
    
    private static void viewCabStatus(CabBookingInterface booking) {
        try {
            String status = booking.getCabStatus();
            System.out.println("\n" + status);
        } catch (Exception e) {
            System.err.println("❌ Error fetching cab status: " + e.getMessage());
        }
    }
}