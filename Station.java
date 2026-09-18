package metro;

public class Station {
    private int stationId;
    private String stationName;
    private String lineName;
    private double distanceFromOrigin;

    public Station(int stationId, String stationName, String lineName, double distanceFromOrigin) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.lineName = lineName;
        this.distanceFromOrigin = distanceFromOrigin;
    }

    public int getStationId() { return stationId; }
    public String getStationName() { return stationName; }
    public String getLineName() { return lineName; }
    public double getDistanceFromOrigin() { return distanceFromOrigin; }

    @Override
    public String toString() {
        return stationId + " - " + stationName + " (" + lineName + ")";
    }
}
