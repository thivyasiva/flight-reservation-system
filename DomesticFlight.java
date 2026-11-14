/**
 * DomesticFlight.java
 *
 * A flight to a destination inside Canada. No passport is required.
 * Extends Flight and overrides getType() — an example of inheritance.
 */
public class DomesticFlight extends Flight {

    public DomesticFlight(String flightNumber, String airline, Airport destination,
                          String departureTime, int durationMinutes,
                          int capacity, double price) {
        super(flightNumber, airline, destination,
                departureTime, durationMinutes, capacity, price);
    }

    @Override
    public String getType() {
        return "Domestic";
    }

    @Override
    public String toString() {
        return super.toString() + "  [Domestic]";
    }
}
