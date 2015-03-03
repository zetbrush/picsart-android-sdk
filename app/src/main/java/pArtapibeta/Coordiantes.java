package pArtapibeta;

/**
 * Created by intern on 3/3/15.
 */
public class Coordiantes {
    String location_lat;
    String location_lon;

    public Coordiantes(String lat, String longit){
        this.location_lat=lat;
        this.location_lon=longit;
    }

    public String getLocation_lat() {
        return location_lat;
    }

    public String getLocation_lon() {
        return location_lon;
    }
}
