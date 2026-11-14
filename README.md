# Flight Reservation System

Solo project by Thivya Siva — Toronto Metropolitan University, Bachelor of Commerce, Business Technology Management (minor in Computer Science), expected April 2027.

Completed Winter 2021 — TMU CPS 209 (Computer Science II) final project.

A command-line Java program that models flights departing from Toronto Pearson Airport (YYZ) during a single day. Users can list flights, search by destination or departure time, make a reservation, cancel a reservation, and view all reservations.

> **Note on this repository:** this is the final project for **TMU CPS 209 (Computer Science II)**. It was rebuilt from the assignment description — the original code/files were not available — so it is a fresh implementation of the same assignment, not a copy of anyone else's code. The flight data in `flights.csv` is sample data created for this project.

## What it does

- Loads 12 sample Pearson departures for a single day from `flights.csv` (file I/O)
- Lists all flights in departure-time order, with departure and arrival times, duration, price and seats available
- Searches flights by destination city or airport code, and by earliest departure time
- Makes reservations (one seat per reservation, with a confirmation code) and cancels them, updating the seats available on that flight
- Handles bad input and file problems with exceptions instead of crashing — for example, a missing `flights.csv`, a malformed CSV line, an unknown flight number, or an invalid time

## Skills

Java, object-oriented programming (objects and classes, inheritance, interfaces), collections (`ArrayList`, `HashMap`), file I/O, exception handling.

## Files

| File | What it does |
| --- | --- |
| `FlightReservationSystem.java` | Main / driver class. Loads flights from CSV, runs the command-line menu, and handles list, search, reserve, cancel and view. |
| `Flight.java` | One flight departing YYZ: flight number, airline, destination, departure/arrival time, duration, capacity, price and seats booked. Implements `Reservable`. |
| `DomesticFlight.java` | A flight to a destination inside Canada. Extends `Flight`. |
| `InternationalFlight.java` | A flight to a destination outside Canada (passport required). Extends `Flight`. |
| `Reservable.java` | Interface for reserving and cancelling seats (`reserveSeat`, `cancelSeat`, `getAvailableSeats`). |
| `Airport.java` | A destination airport: code, city and country. |
| `Passenger.java` | A passenger making a reservation. |
| `Reservation.java` | One booked seat: confirmation code (RES-1001, RES-1002, …), passenger and flight. |
| `flights.csv` | Sample data: 12 Pearson departures for a single day (flight number, airline, domestic/international, destination, departure time, duration, capacity, price). |

## How to run

Requires Java 8 or later (tested with Java 21).

```bash
# Compile all Java files
javac *.java

# Run the program (run it from this folder, so it can find flights.csv)
java FlightReservationSystem
```

Then choose from the menu:

```
1. List all flights
2. Search flights by destination
3. Search flights departing at or after a time
4. Make a reservation
5. Cancel a reservation
6. View all reservations
7. Exit
```

Example session:

```
Enter your choice (1-7): 2
Enter a destination city or airport code (e.g. Vancouver or YVR): Vancouver
1 flight(s) to 'vancouver':
  AC101   Air Canada      YYZ -> YVR - Vancouver, Canada       Dep 06:30  Arr 11:35  (305 min)  $289.00  Seats: 180/180 available  [Domestic]

Enter your choice (1-7): 4
Enter the flight number (e.g. AC101): AC101
Enter the passenger's name: Thivya Siva
Reservation confirmed: RES-1001  Thivya Siva  on AC101 (Air Canada)  YYZ -> YVR - Vancouver, Canada  Dep 06:30  $289.00
```

## How the arrival time is worked out

Arrival time = departure time + flight duration. For example, AC101 departs at 06:30 and takes 305 minutes, so it arrives at 11:35.
