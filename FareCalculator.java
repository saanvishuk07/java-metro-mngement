package metro;

public class FareCalculator {
    public static double calculate(double distance) {
        if (distance <= 5) return 10.0;
        if (distance <= 10) return 20.0;
        if (distance <= 20) return 30.0;
        return 40.0;
    }
}
