/**
 * Flight.java
 *
 * Represents one flight departing from Toronto Pearson (YYZ) on a single day.
 *
 * A flight has a flight number, an airline, a destination airport, a
 * departure time, a duration, a seat capacity and a price. Times are stored
 * as "HH:MM" strings and as minutes since midnight, so flights can be
 * sorted and searched by departure time and an arrival time can be worked
 * out from the departure time plus the duration.
 *
 * DomesticFlight and InternationalFlight extend this class.
 */
public class Flight implements Reservable {
    private final String flightNumber;
    private final String airline;
    private final Airport destination;
    private final String departureTime; // "HH:MM", 24-hour clock
    private final int departureMinutes; // minutes since midnight
    private final int durationMinutes;
    private final int capacity;
    private final double price;
    private int seatsBooked;

    public Flight(String flightNumber, String airline, Airport destination,
                  String departureTime, int durationMinutes,
                  int capacity, double price) {
        this.flightNumber = flightNumber;
        this.airline = airline;
        this.destination = destination;
        this.departureTime = departureTime;
        this.departureMinutes = toMinutes(departureTime);
        this.durationMinutes = durationMinutes;
        this.capacity = capacity;
        this.price = price;
        this.seatsBooked = 0;
    }

    /** Convert a "HH:MM" time to minutes since midnight. */
    public static int toMinutes(String time) {
        String[] parts = time.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Time must be in HH:MM format: " + time);
        }
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
            throw new IllegalArgumentException("Invalid time: " + time);
        }
        return hours * 60 + minutes;
    }

    /** Format minutes since midnight as "HH:MM", wrapping past midnight. */
    public static String toTimeString(int minutes) {
        int total = ((minutes % (24 * 60)) + (24 * 60)) % (24 * 60);
        return String.format("%02d:%02d", total / 60, total % 60);
    }

    /** Arrival time, worked out from departure time + duration. */
    public String getArrivalTime() {
        return toTimeString(departureMinutes + durationMinutes);
    }

    public String getType() {
        return "Flight";
    }

    @Override
    public String getFlightNumber() {
        return flightNumber;
    }

    public String getAirline() {
        return airline;
    }

    public Airport getDestination() {
        return destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public int getDepartureMinutes() {
        return departureMinutes;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getPrice() {
        return price;
    }

    public int getSeatsBooked() {
        return seatsBooked;
    }

    @Override
    public int getAvailableSeats() {
        return capacity - seatsBooked;
    }

    @Override
    public boolean reserveSeat() {
        if (getAvailableSeats() <= 0) {
            return false;
        }
        seatsBooked++;
        return true;
    }

    @Override
    public boolean cancelSeat() {
        if (seatsBooked <= 0) {
            return false;
        }
        seatsBooked--;
        return true;
    }

    @Override
    public String toString() {
        return String.format("%-7s %-15s YYZ -> %-28s Dep %s  Arr %s  (%d min)  $%.2f  Seats: %d/%d available",
                flightNumber, airline, destination,
                departureTime, getArrivalTime(), durationMinutes,
                price, getAvailableSeats(), capacity);
    }
}
