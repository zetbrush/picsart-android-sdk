package picsArtAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * This class serves as POJO.
 * Also it serves as class type for instantiating
 * Location objects from Json.
 * <p>Some Getters of this class throw a <tt>NullPointerException</tt>
 * if the collections provided to them are null.
 *
 * <p>This class is a member of the
 * <a href="www.picsart.com">
 * </a>.
 *
 * @author  Arman Andreasyan
 */

public class Location {

    private final String[] keywords = {"location_place", "location_street", "location_city",
            "location_state", "location_zip", "location_country"};



    BasicNameValuePair[] locationPair;

    @SerializedName("street")
    @Expose
    String street;

    @SerializedName("city")
    @Expose
    String city;

    @SerializedName("place")
    @Expose
    String place;

    @SerializedName("state")
    @Expose
    String state;

    @SerializedName("zip")
    @Expose
    String zip;

    @SerializedName("country")
    @Expose
    String country;

    @SerializedName("coordinates")
    @Expose
    List<Double> coordinates;


    public BasicNameValuePair[] getLocationPair() {
        return locationPair;
    }

    public List<Double> getCoordinates() throws NullPointerException{
        return coordinates;
    }

    public void setCoordinates(List<Double> coordinates) {
        this.coordinates = new ArrayList<>(coordinates);
    }
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }



    public Location(String str, String cit, String place, String stat, String zip, String country, ArrayList<Double> coord) {

        coordinates = new ArrayList<Double>();

        this.street = str;
        this.city = cit;
        this.state = stat;
        this.zip = zip;
        this.place = place;
        this.country = country;
        this.coordinates = coord;

        locationPair = new BasicNameValuePair[6];
        locationPair[0] = new BasicNameValuePair(keywords[0], place);
        locationPair[1] = new BasicNameValuePair(keywords[1], str);
        locationPair[2] = new BasicNameValuePair(keywords[2], cit);
        locationPair[3] = new BasicNameValuePair(keywords[3], stat);
        locationPair[4] = new BasicNameValuePair(keywords[4], zip);
        locationPair[5] = new BasicNameValuePair(keywords[5], country);


    }

    @Override
    public String toString() {
        return " " + street + " " + city + " " + state + " " + zip + " " + country + " " + coordinates;

    }
}
