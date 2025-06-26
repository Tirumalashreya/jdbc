package Server;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
public class ServerMain {
 private static Registry registry;

 public static void main(String[] args) {
 try {

 System.out.println("Testing database connection...");
 DBConnection.testConnection();
 
 int port = 2001;
 System.out.println("Starting RMI registry on port " + port + "...");
 registry = LocateRegistry.createRegistry(port);

 CabBookingInterface stub = new CabBookingImpl();
 String serviceName = "rmi://localhost:" + port + "/CabService";
 Naming.rebind(serviceName, stub);

 System.out.println("✅ Cab Booking Service is ready!");
 System.out.println("🌐 Service URL: " + serviceName);
 System.out.println("📋 Available operations:");
 System.out.println(" - Book Cab");
 System.out.println(" - View Available Cabs");
 System.out.println(" - Cancel Booking");
 System.out.println(" - View Booking History");
 System.out.println(" - Add New Cab");
 System.out.println(" - Check Cab Status");
 System.out.println("\n🛑 Press Ctrl+C to stop the server");


 Runtime.getRuntime().addShutdownHook(new Thread(() -> {
 System.out.println("\n🛑 Shutting down server...");
 try {
 Naming.unbind(serviceName);
 UnicastRemoteObject.unexportObject(registry, true);
 System.out.println(" Server shutdown complete!");
 } catch (Exception e) {
 System.err.println("Error during shutdown: " + e.getMessage());
 }
 }));


 Object lock = new Object();
 synchronized (lock) {
 lock.wait();
 }

 } catch (Exception e) {
 System.err.println(" Server startup failed: " + e.getMessage());
 e.printStackTrace();
 }
 }
}