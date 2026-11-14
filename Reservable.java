/**
 * Reservable.java
 *
 * Interface for anything that can have seats reserved and cancelled.
 * Flight implements this interface.
 */
public interface Reservable {
    /** Try to reserve one seat. Returns true if a seat was available. */
    boolean reserveSeat();

    /** Try to cancel one booked seat. Returns true if a seat was booked. */
    boolean cancelSeat();

    /** Number of seats still available. */
    int getAvailableSeats();

    /** Flight number, e.g. "AC101". */
    String getFlightNumber();
}
