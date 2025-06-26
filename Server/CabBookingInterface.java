package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface CabBookingInterface extends Remote {
    String bookCab(String name, String source, String destination) throws RemoteException;
    List<String> getAvailableCabs(String location) throws RemoteException;
    String cancelBooking(int bookingId) throws RemoteException;
    String getBookingHistory(String customerName) throws RemoteException;
    String addCab(String location) throws RemoteException;
    String getCabStatus() throws RemoteException;
}

