That’s awesome! 🎉 Since your **Java RMI-based Cab Booking System** is now working, here’s a clean list of **all commands** you need to:

> 🔁 Compile
> 🚀 Start the RMI registry
> ✅ Run the RMI server
> 💻 Run the client

---

## ✅ FULL COMMAND LIST (UNIX/MacOS)

> Replace `:` with `;` in the `-cp` if you're on **Windows**.

---

### 🧱 1. Compile all Java files

```bash
javac -cp .:lib/mysql-connector-j-8.0.33.jar Server/*.java Client/*.java
```

---

### 🚦 2. Start the RMI Registry (in a **new terminal**)

```bash
rmiregistry 2001
```

> Make sure you run this **from your project root folder**, and the port (2001) matches your code.

---

### 🖥️ 3. Run the RMI Server (in another terminal)

```bash
java -cp .:lib/mysql-connector-j-8.0.33.jar Server.ServerMain
```

---

### 👤 4. Run the Client

If your `main()` method is in:

#### 🔹 `CabBookingClient.java`:

```bash
java -cp .:lib/mysql-connector-j-8.0.33.jar Client.CabBookingClient
```

#### 🔹 `CabBookingGUI.java`:

```bash
java -cp .:lib/mysql-connector-j-8.0.33.jar Client.CabBookingGUI
```

---

### 🔧 5. (Optional) If Port 2001 is Busy

To check what’s using it:

```bash
lsof -i :2001
```

To kill the process (replace `<PID>` with the process ID from above):

```bash
kill -9 <PID>
```

---

### ✅ Test DB Connection Separately (if needed)

If you wrote a `TestDB` class inside `Server`:

```bash
java -cp .:lib/mysql-connector-j-8.0.33.jar Server.TestDB
```

---

### 🗃️ Recap Folder Structure

```
CabBookingSystem/
├── lib/
│   └── mysql-connector-j-8.0.33.jar
├── Server/
│   ├── DBConnection.java
│   ├── CabBookingInterface.java
│   ├── CabBookingImpl.java
│   ├── ServerMain.java
│   └── TestDB.java (optional)
├── Client/
│   ├── CabBookingClient.java
│   ├── CabBookingGUI.java
```

---

Let me know if you’d like a **single shell script** to automate all these commands!
rmiregistry     
java -cp .:lib/mysql-connector-j-8.0.33.jar Server.ServerMain
java -cp .:lib/mysql-connector-j-8.0.33.jar Client.CabBookingClient
ava -cp .:lib/mysql-connector-j-8.0.33.jar Client.CabBookingGUI

Check available cabs
-- SELECT * FROM available_cabs_view WHERE location = 'Gachibowli';

-- Get booking history for a customer
-- SELECT * FROM booking_summary_view WHERE customer_name = 'Amit Sharma';

-- Check driver performance
-- SELECT * FROM driver_performance_view ORDER BY total_bookings DESC;

-- Get all active bookings
-- SELECT * FROM booking_summary_view WHERE status IN ('CONFIRMED', 'IN_PROGRESS');

-- Calculate total revenue
-- SELECT SUM(fare) as total_revenue FROM bookings WHERE status = 'COMPLETED';
#
//chmod +x run_cab_book.sh
//./run_cab_book.sh
