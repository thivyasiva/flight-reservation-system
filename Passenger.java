/**
 * Passenger.java
 *
 * Represents a passenger making a reservation.
 */
public class Passenger {
    private final String name;

    public Passenger(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Passenger name cannot be blank.");
        }
        this.name = name.trim();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
