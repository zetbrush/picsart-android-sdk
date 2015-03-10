package pArtapibeta;

import org.apache.http.message.BasicNameValuePair;

import pArtapibeta.pojo.PojoLocation;

/**
 * Created by Arman on 2/19/15.
 */


public class Location {
    private final String[] keywords = {"location_place", "location_street", "location_city",
                                        "location_state", "location_zip", "location_country", "location_lat", "location_lon"};
    public BasicNameValuePair[] getLocationPair() {
        return locationPair;
    }

    BasicNameValuePair[] locationPair;
    String street;
    String city;
    String place;
    String state;
    String zip;
    String country;
    Coordiantes coordinates;

    public Location(String str, String cit, String place, String stat, String zip, String country, Coordiantes coord) {

        this.street = str;
        this.city = cit;
        this.state = stat;
        this.zip = zip;
        this.place = place;
        this.country = country;
        this.coordinates = coord;

        locationPair = new BasicNameValuePair[8];
        locationPair[0] = new BasicNameValuePair(keywords[0], place);
        locationPair[1] = new BasicNameValuePair(keywords[1], str);
        locationPair[2] = new BasicNameValuePair(keywords[2], cit);
        locationPair[3] = new BasicNameValuePair(keywords[3], stat);
        locationPair[4] = new BasicNameValuePair(keywords[4], zip);
        locationPair[5] = new BasicNameValuePair(keywords[5], country);
        locationPair[6] = new BasicNameValuePair(keywords[6], coord.getLocation_lat());
        locationPair[7] = new BasicNameValuePair(keywords[7], coord.getLocation_lon());

    }

    public Location(PojoLocation loc){
        this.street = loc.getStreet();
        this.place = loc.getPlace();
        this.city = loc.getCity();
        this.state = loc.getState();
        this.zip = loc.getZip();
        //this.coordinates = new Coordiantes(loc.getCoordinates().get(0).toString(), loc.getCoordinates().get(1).toString());
    }

    @Override
    public String toString() {
        return " "+street + " " + city + " " + state + " " + zip + " " + country + " " + coordinates;

    }


}
