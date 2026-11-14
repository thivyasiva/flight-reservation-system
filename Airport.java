/**
 * Airport.java
 *
 * Represents a destination airport.
 * A Flight departs from Toronto Pearson (YYZ) and flies to one of these airports.
 */
public class Airport {
    private final String code;   // e.g. "YVR"
    private final String city;   // e.g. "Vancouver"
    private final String country;

    public Airport(String code, String city, String country) {
        this.code = code;
        this.city = city;
        this.country = country;
    }

    public String getCode() {
        return code;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return code + " - " + city + ", " + country;
    }
}
