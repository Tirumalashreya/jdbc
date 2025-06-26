


CREATE DATABASE IF NOT EXISTS cabdb;
USE cabdb;


DROP TABLE IF EXISTS cabs;
CREATE TABLE cabs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    location VARCHAR(100) NOT NULL,
    current_location VARCHAR(100) DEFAULT NULL,
    is_available BOOLEAN DEFAULT TRUE,
    driver_name VARCHAR(100) NOT NULL,
    driver_phone VARCHAR(15) DEFAULT NULL,
    cab_type ENUM('Sedan', 'SUV', 'Hatchback', 'Luxury') DEFAULT 'Sedan',
    license_plate VARCHAR(20) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_location (location),
    INDEX idx_available (is_available),
    INDEX idx_driver (driver_name)
);


DROP TABLE IF EXISTS bookings;
CREATE TABLE bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) DEFAULT NULL,
    source VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    cab_id INT NOT NULL,
    booking_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    pickup_time TIMESTAMP NULL,
    completion_time TIMESTAMP NULL,
    status ENUM('CONFIRMED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') DEFAULT 'CONFIRMED',
    fare DECIMAL(10, 2) DEFAULT NULL,
    rating INT DEFAULT NULL CHECK (rating >= 1 AND rating <= 5),
    feedback TEXT DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cab_id) REFERENCES cabs(id) ON DELETE CASCADE,
    INDEX idx_customer (name),
    INDEX idx_status (status),
    INDEX idx_booking_time (booking_time),
    INDEX idx_cab_id (cab_id)
);


CREATE TABLE IF NOT EXISTS locations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    latitude DECIMAL(10, 8) DEFAULT NULL,
    longitude DECIMAL(11, 8) DEFAULT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS fare_rates (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cab_type ENUM('Sedan', 'SUV', 'Hatchback', 'Luxury') NOT NULL,
    base_fare DECIMAL(8, 2) NOT NULL,
    per_km_rate DECIMAL(8, 2) NOT NULL,
    per_minute_rate DECIMAL(8, 2) NOT NULL,
    surge_multiplier DECIMAL(3, 2) DEFAULT 1.0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS drivers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(15) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE,
    license_number VARCHAR(50) UNIQUE NOT NULL,
    address TEXT,
    date_of_birth DATE,
    hire_date DATE DEFAULT (CURRENT_DATE),
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') DEFAULT 'ACTIVE',
    rating DECIMAL(3, 2) DEFAULT 5.0,
    total_rides INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);




INSERT INTO drivers (name, phone, email, license_number, address) VALUES
('Rajesh Kumar', '9876543210', 'rajesh@example.com', 'DL123456789', 'Hyderabad, Telangana'),
('Suresh Reddy', '9876543211', 'suresh@example.com', 'DL123456790', 'Secunderabad, Telangana'),
('Mahesh Singh', '9876543212', 'mahesh@example.com', 'DL123456791', 'Gachibowli, Hyderabad'),
('Ramesh Rao', '9876543213', 'ramesh@example.com', 'DL123456792', 'Banjara Hills, Hyderabad'),
('Ganesh Patel', '9876543214', 'ganesh@example.com', 'DL123456793', 'Jubilee Hills, Hyderabad');


INSERT INTO locations (name, latitude, longitude) VALUES
('Hyderabad Airport', 17.2403, 78.4294),
('Gachibowli', 17.4399, 78.3482),
('Banjara Hills', 17.4065, 78.4691),
('Jubilee Hills', 17.4239, 78.4738),
('Secunderabad', 17.5040, 78.5030),
('Hitec City', 17.4484, 78.3908),
('Begumpet', 17.4399, 78.4482),
('Madhapur', 17.4477, 78.3984),
('Kondapur', 17.4638, 78.3644),
('Kukatpally', 17.4842, 78.4016);


INSERT INTO fare_rates (cab_type, base_fare, per_km_rate, per_minute_rate) VALUES
('Hatchback', 50.00, 12.00, 2.00),
('Sedan', 80.00, 15.00, 2.50),
('SUV', 120.00, 18.00, 3.00),
('Luxury', 200.00, 25.00, 4.00);


INSERT INTO cabs (location, driver_name, driver_phone, cab_type, license_plate) VALUES
('Gachibowli', 'Rajesh Kumar', '9876543210', 'Sedan', 'TS09EA1234'),
('Banjara Hills', 'Suresh Reddy', '9876543211', 'SUV', 'TS09EA1235'),
('Jubilee Hills', 'Mahesh Singh', '9876543212', 'Hatchback', 'TS09EA1236'),
('Secunderabad', 'Ramesh Rao', '9876543213', 'Sedan', 'TS09EA1237'),
('Hitec City', 'Ganesh Patel', '9876543214', 'Luxury', 'TS09EA1238'),
('Madhapur', 'Rajesh Kumar', '9876543210', 'Sedan', 'TS09EA1239'),
('Kondapur', 'Suresh Reddy', '9876543211', 'Hatchback', 'TS09EA1240'),
('Kukatpally', 'Mahesh Singh', '9876543212', 'SUV', 'TS09EA1241'),
('Begumpet', 'Ramesh Rao', '9876543213', 'Sedan', 'TS09EA1242'),
('Hyderabad Airport', 'Ganesh Patel', '9876543214', 'Luxury', 'TS09EA1243');


INSERT INTO bookings (name, phone, source, destination, cab_id, status, fare) VALUES
('Amit Sharma', '9123456789', 'Gachibowli', 'Hyderabad Airport', 1, 'COMPLETED', 450.00),
('Priya Singh', '9123456790', 'Banjara Hills', 'Hitec City', 2, 'COMPLETED', 320.00),
('Rohit Kumar', '9123456791', 'Jubilee Hills', 'Secunderabad', 3, 'CONFIRMED', 280.00),
('Sneha Reddy', '9123456792', 'Madhapur', 'Kondapur', 4, 'IN_PROGRESS', 180.00);



CREATE OR REPLACE VIEW available_cabs_view AS
SELECT 
    c.id as cab_id,
    c.location,
    c.driver_name,
    c.driver_phone,
    c.cab_type,
    c.license_plate,
    fr.base_fare,
    fr.per_km_rate
FROM cabs c
JOIN fare_rates fr ON c.cab_type = fr.cab_type
WHERE c.is_available = TRUE AND fr.is_active = TRUE;

-- View for booking summary
CREATE OR REPLACE VIEW booking_summary_view AS
SELECT 
    b.id as booking_id,
    b.name as customer_name,
    b.phone as customer_phone,
    b.source,
    b.destination,
    b.booking_time,
    b.status,
    b.fare,
    c.driver_name,
    c.driver_phone,
    c.cab_type,
    c.license_plate
FROM bookings b
JOIN cabs c ON b.cab_id = c.id
ORDER BY b.booking_time DESC;


CREATE OR REPLACE VIEW driver_performance_view AS
SELECT 
    c.driver_name,
    c.driver_phone,
    COUNT(b.id) as total_bookings,
    COUNT(CASE WHEN b.status = 'COMPLETED' THEN 1 END) as completed_rides,
    COUNT(CASE WHEN b.status = 'CANCELLED' THEN 1 END) as cancelled_rides,
    AVG(b.fare) as average_fare,
    SUM(b.fare) as total_earnings,
    AVG(b.rating) as average_rating
FROM cabs c
LEFT JOIN bookings b ON c.id = b.cab_id
GROUP BY c.driver_name, c.driver_phone;


DELIMITER //
CREATE PROCEDURE UpdateCabLocation(
    IN p_cab_id INT,
    IN p_new_location VARCHAR(100)
)
BEGIN
    UPDATE cabs 
    SET current_location = p_new_location, 
        updated_at = CURRENT_TIMESTAMP 
    WHERE id = p_cab_id;
END //
DELIMITER ;


DELIMITER //
CREATE PROCEDURE CompleteBooking(
    IN p_booking_id INT,
    IN p_fare DECIMAL(10,2),
    IN p_rating INT
)
BEGIN
    DECLARE v_cab_id INT;
    DECLARE v_destination VARCHAR(100);
    
   
    SELECT cab_id, destination INTO v_cab_id, v_destination 
    FROM bookings 
    WHERE id = p_booking_id;
    
    
    UPDATE bookings 
    SET status = 'COMPLETED', 
        completion_time = CURRENT_TIMESTAMP,
        fare = p_fare,
        rating = p_rating,
        updated_at = CURRENT_TIMESTAMP
    WHERE id = p_booking_id;
    
   
    UPDATE cabs 
    SET is_available = TRUE, 
        location = v_destination,
        current_location = v_destination,
        updated_at = CURRENT_TIMESTAMP
    WHERE id = v_cab_id;
END //
DELIMITER ;




CREATE INDEX idx_bookings_customer_status ON bookings(name, status);
CREATE INDEX idx_bookings_time_range ON bookings(booking_time, completion_time);
CREATE INDEX idx_cabs_location_available ON cabs(location, is_available);
CREATE INDEX idx_cabs_type_available ON cabs(cab_type, is_available);




COMMIT;