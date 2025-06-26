Great job organizing your Java RMI-based Cab Booking System! 🚖 Here's an **enhanced and professional version of your `README.md`**, with:

* 📦 Complete folder and file structure
* ⚙️ Setup & execution guide
* 🛠️ Database queries
* 📜 Shell scripts & permissions
* 🛡️ Security policy reference

---

# 🚖 Cab Booking System (Java RMI + MySQL)

This is a **Java RMI-based Cab Booking System** with a MySQL backend. It provides functionality for booking cabs, managing drivers, and viewing bookings via a GUI or CLI client.

---

## 📁 Project Structure

```
CabBookingSystem/
├── lib/
│   └── mysql-connector-j-8.0.33.jar         # MySQL JDBC Driver
├── Server/
│   ├── DBConnection.java                    # Handles DB connections
│   ├── CabBookingInterface.java             # RMI Interface
│   ├── CabBookingImpl.java                  # RMI Implementation
│   ├── ServerMain.java                      # RMI Server Entry Point
│   └── TestDB.java                          # (Optional) DB test class
├── Client/
│   ├── CabBookingClient.java                # CLI-based Client
│   ├── CabBookingGUI.java                   # GUI-based Client
├── schema.sql                               # SQL file to set up DB schema
├── server.policy                            # Java Security Policy for RMI
├── run_cab_book.sh                          # Shell script to run project
├── setcp.sh                                 # Set classpath utility (optional)
├── .gitignore                               # Git ignore rules
```

---

## ✅ Full Setup & Command Guide (for Unix/macOS)

> 🪟 Windows? Replace `:` with `;` in the `-cp` (classpath).

---

### 🔧 1. Compile All Java Files

```bash
javac -cp .:lib/mysql-connector-j-8.0.33.jar Server/*.java Client/*.java
```

---

### 🛰️ 2. Start RMI Registry (in new terminal)

```bash
rmiregistry 2001
```

> 🔐 Run it from the **project root folder** and ensure port matches the server.

---

### 🚀 3. Run the RMI Server

```bash
java -cp .:lib/mysql-connector-j-8.0.33.jar -Djava.security.policy=server.policy Server.ServerMain
```

---

### 💻 4. Run the Client

#### 📟 CLI Client:

```bash
java -cp .:lib/mysql-connector-j-8.0.33.jar Client.CabBookingClient
```

#### 🖥️ GUI Client:

```bash
java -cp .:lib/mysql-connector-j-8.0.33.jar Client.CabBookingGUI
```

---

### 🧪 5. Test DB Connection (Optional)

```bash
java -cp .:lib/mysql-connector-j-8.0.33.jar Server.TestDB
```

---

## 🛢️ SQL Utilities

### 📍 Check Available Cabs by Location

```sql
SELECT * FROM available_cabs_view WHERE location = 'Gachibowli';
```

### 👤 Customer Booking History

```sql
SELECT * FROM booking_summary_view WHERE customer_name = 'Amit Sharma';
```

### 🚗 Driver Performance Ranking

```sql
SELECT * FROM driver_performance_view ORDER BY total_bookings DESC;
```

### 📦 All Active Bookings

```sql
SELECT * FROM booking_summary_view WHERE status IN ('CONFIRMED', 'IN_PROGRESS');
```

### 💰 Total Revenue Generated

```sql
SELECT SUM(fare) AS total_revenue FROM bookings WHERE status = 'COMPLETED';
```

---

## 🛡️ Java RMI Security Policy

Ensure your `server.policy` includes basic permissions:

```java
grant {
    permission java.security.AllPermission;
};
```

---

## 📜 Shell Script Usage

### 🔄 Make Executable

```bash
chmod +x run_cab_book.sh
```

### ▶️ Run Script

```bash
./run_cab_book.sh
```


