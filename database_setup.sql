CREATE DATABASE IF NOT EXISTS booking_system;
USE booking_system;

-- Elimina le tabelle se esistono già (rispettando l'ordine per le chiavi esterne)
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS customers;

-- Creazione tabella customers
CREATE TABLE customers (
    id_customer INT AUTO_INCREMENT PRIMARY KEY,
    customer_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL
);

-- Creazione tabella bookings
CREATE TABLE bookings (
    id_booking INT AUTO_INCREMENT PRIMARY KEY,
    id_customer INT NOT NULL,
    booking_date DATE NOT NULL,
    booking_time TIME NOT NULL,
    status VARCHAR(50) NOT NULL,
    FOREIGN KEY (id_customer) REFERENCES customers(id_customer) ON DELETE CASCADE
);

-- Popolamento tabella customers
INSERT INTO customers (customer_name, email) VALUES
('Giorgio', 'jojo@foobar.dev'),
('Gyro', 'gyro@steelball.run'),
('Mario', 'mario.rossi@outlook.it');

-- Popolamento tabella bookings
INSERT INTO bookings (id_customer, booking_date, booking_time, status) VALUES
(1, '2026-10-15', '14:30:00', 'confermata'),
(1, '2026-11-02', '10:00:00', 'in attesa'),
(2, '2026-10-20', '18:00:00', 'confermata'),
(3, '2026-10-22', '12:15:00', 'cancellata');
