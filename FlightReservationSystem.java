/**
 * FlightReservationSystem.java
 *
 * Main / driver class for the Flight Reservation System.
 *
 * A command-line program that models flights departing from Toronto
 * Pearson Airport (YYZ) during a single day. Flights are loaded from
 * flights.csv using file I/O. The menu lets the user:
 *   1. List all flights
 *   2. Search flights by destination
 *   3. Search flights by departure time
 *   4. Make a reservation
 *   5. Cancel a reservation
 *   6. View all reservations
 *   7. Exit
 *
 * Collections used: ArrayList (flights and reservations in order) and
 * HashMap (flights looked up by flight number, reservations looked up
 * by confirmation code). Bad input and file problems are handled with
 * exceptions instead of crashing the program.
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FlightReservationSystem {

    private final List<Flight> flights = new ArrayList<>();
    private final Map<String, Flight> flightsByNumber = new HashMap<>();
    private final List<Reservation> reservations = new ArrayList<>();
    private final Map<String, Reservation> reservationsByCode = new HashMap<>();

    public static void main(String[] args) {
        FlightReservationSystem system = new FlightReservationSystem();
        try {
            system.loadFlights("flights.csv");
        } catch (FileNotFoundException e) {
            System.out.println("Could not find flights.csv in the current folder.");
            System.out.println("Please run the program from the folder containing flights.csv.");
            return;
        } catch (IOException e) {
            System.out.println("Could not read flights.csv: " + e.getMessage());
            return;
        }
        if (system.flights.isEmpty()) {
            System.out.println("No flights were loaded from flights.csv.");
            return;
        }
        system.runMenu();
    }

    /**
     * Load flights from a CSV file. Each line (after the header) is:
     * flightNumber,airline,type,destinationCode,destinationCity,destinationCountry,
     * departureTime,durationMinutes,capacity,price
     * where type is "Domestic" or "International".
     * Lines that cannot be read are reported and skipped, not fatal.
     */
    public void loadFlights(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine(); // header line
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    Flight flight = parseFlight(line);
                    flights.add(flight);
                    flightsByNumber.put(flight.getFlightNumber().toUpperCase(), flight);
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping line " + lineNumber
                            + " in flights.csv: " + e.getMessage());
                }
            }
        }
        // Show flights in departure-time order.
        flights.sort(Comparator.comparingInt(Flight::getDepartureMinutes));
    }

    private Flight parseFlight(String line) {
        String[] parts = line.split(",");
        if (parts.length != 10) {
            throw new IllegalArgumentException(
                    "expected 10 fields, found " + parts.length + ": " + line);
        }
        String flightNumber = parts[0].trim();
        String airline = parts[1].trim();
        String type = parts[2].trim();
        Airport destination = new Airport(parts[3].trim(), parts[4].trim(), parts[5].trim());
        String departureTime = parts[6].trim();
        int durationMinutes = Integer.parseInt(parts[7].trim());
        int capacity = Integer.parseInt(parts[8].trim());
        double price = Double.parseDouble(parts[9].trim());

        if (type.equalsIgnoreCase("International")) {
            return new InternationalFlight(flightNumber, airline, destination,
                    departureTime, durationMinutes, capacity, price);
        } else if (type.equalsIgnoreCase("Domestic")) {
            return new DomesticFlight(flightNumber, airline, destination,
                    departureTime, durationMinutes, capacity, price);
        } else {
            throw new IllegalArgumentException("unknown flight type: " + type);
        }
    }

    private void runMenu() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("==============================================");
        System.out.println("  Flight Reservation System - Toronto Pearson (YYZ)");
        System.out.println("  " + flights.size() + " flights today, departing YYZ");
        System.out.println("==============================================");

        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("Enter your choice (1-7): ");
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    listFlights();
                    break;
                case "2":
                    searchByDestination(scanner);
                    break;
                case "3":
                    searchByDepartureTime(scanner);
                    break;
                case "4":
                    makeReservation(scanner);
                    break;
                case "5":
                    cancelReservation(scanner);
                    break;
                case "6":
                    viewReservations();
                    break;
                case "7":
                    running = false;
                    System.out.println("Thank you for using the Flight Reservation System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 7.");
                    break;
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println();
        System.out.println("--- Menu ---");
        System.out.println("1. List all flights");
        System.out.println("2. Search flights by destination");
        System.out.println("3. Search flights departing at or after a time");
        System.out.println("4. Make a reservation");
        System.out.println("5. Cancel a reservation");
        System.out.println("6. View all reservations");
        System.out.println("7. Exit");
    }

    private void listFlights() {
        System.out.println();
        System.out.println("All flights departing Toronto Pearson (YYZ) today:");
        for (Flight flight : flights) {
            System.out.println("  " + flight);
        }
    }

    private void searchByDestination(Scanner scanner) {
        System.out.print("Enter a destination city or airport code (e.g. Vancouver or YVR): ");
        String query = scanner.nextLine().trim().toLowerCase();
        List<Flight> matches = new ArrayList<>();
        for (Flight flight : flights) {
            Airport destination = flight.getDestination();
            if (destination.getCity().toLowerCase().contains(query)
                    || destination.getCode().toLowerCase().equals(query)) {
                matches.add(flight);
            }
        }
        System.out.println();
        if (matches.isEmpty()) {
            System.out.println("No flights found to '" + query + "'.");
        } else {
            System.out.println(matches.size() + " flight(s) to '" + query + "':");
            for (Flight flight : matches) {
                System.out.println("  " + flight);
            }
        }
    }

    private void searchByDepartureTime(Scanner scanner) {
        System.out.print("Enter the earliest departure time (HH:MM, 24-hour clock): ");
        String input = scanner.nextLine().trim();
        int earliest;
        try {
            earliest = Flight.toMinutes(input);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid time. Please use HH:MM, e.g. 09:30.");
            return;
        }
        List<Flight> matches = new ArrayList<>();
        for (Flight flight : flights) {
            if (flight.getDepartureMinutes() >= earliest) {
                matches.add(flight);
            }
        }
        System.out.println();
        if (matches.isEmpty()) {
            System.out.println("No flights depart at or after " + input + ".");
        } else {
            System.out.println(matches.size() + " flight(s) departing at or after " + input + ":");
            for (Flight flight : matches) {
                System.out.println("  " + flight);
            }
        }
    }

    private void makeReservation(Scanner scanner) {
        System.out.print("Enter the flight number (e.g. AC101): ");
        String flightNumber = scanner.nextLine().trim().toUpperCase();
        Flight flight = flightsByNumber.get(flightNumber);
        if (flight == null) {
            System.out.println("No flight with number " + flightNumber + " was found.");
            return;
        }
        System.out.print("Enter the passenger's name: ");
        String name = scanner.nextLine().trim();
        Passenger passenger;
        try {
            passenger = new Passenger(name);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }
        if (!flight.reserveSeat()) {
            System.out.println("Sorry, flight " + flightNumber + " is fully booked.");
            return;
        }
        Reservation reservation = new Reservation(passenger, flight);
        reservations.add(reservation);
        reservationsByCode.put(reservation.getConfirmationCode(), reservation);
        System.out.println("Reservation confirmed: " + reservation);
        System.out.println("Please keep your confirmation code: "
                + reservation.getConfirmationCode());
    }

    private void cancelReservation(Scanner scanner) {
        System.out.print("Enter the confirmation code (e.g. RES-1001): ");
        String code = scanner.nextLine().trim().toUpperCase();
        Reservation reservation = reservationsByCode.get(code);
        if (reservation == null) {
            System.out.println("No reservation found with code " + code + ".");
            return;
        }
        reservation.getFlight().cancelSeat();
        reservations.remove(reservation);
        reservationsByCode.remove(code);
        System.out.println("Reservation " + code + " has been cancelled.");
    }

    private void viewReservations() {
        System.out.println();
        if (reservations.isEmpty()) {
            System.out.println("No reservations have been made yet.");
            return;
        }
        System.out.println("All reservations (" + reservations.size() + "):");
        for (Reservation reservation : reservations) {
            System.out.println("  " + reservation);
        }
    }
}
