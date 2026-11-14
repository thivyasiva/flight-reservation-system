/**
 * InternationalFlight.java
 *
 * A flight to a destination outside Canada. A passport is required.
 * Extends Flight and overrides getType() — an example of inheritance.
 */
public class InternationalFlight extends Flight {

    public InternationalFlight(String flightNumber, String airline, Airport destination,
                               String departureTime, int durationMinutes,
                               int capacity, double price) {
        super(flightNumber, airline, destination,
                departureTime, durationMinutes, capacity, price);
    }

    @Override
    public String getType() {
        return "International";
    }

    @Override
    public String toString() {
        return super.toString() + "  [International - passport required]";
    }
}
