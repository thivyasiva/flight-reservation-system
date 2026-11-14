/**
 * Reservation.java
 *
 * Represents one booked seat on one flight for one passenger.
 * Confirmation codes are assigned in order (RES-1001, RES-1002, ...).
 */
public class Reservation {
    private static int nextNumber = 1001;

    private final String confirmationCode;
    private final Passenger passenger;
    private final Flight flight;

    public Reservation(Passenger passenger, Flight flight) {
        this.confirmationCode = "RES-" + nextNumber;
        nextNumber++;
        this.passenger = passenger;
        this.flight = flight;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public Flight getFlight() {
        return flight;
    }

    @Override
    public String toString() {
        return String.format("%s  %s  on %s (%s)  YYZ -> %s  Dep %s  $%.2f",
                confirmationCode, passenger.getName(),
                flight.getFlightNumber(), flight.getAirline(),
                flight.getDestination(), flight.getDepartureTime(),
                flight.getPrice());
    }
}
