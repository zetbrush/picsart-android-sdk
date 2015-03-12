package pArtapibeta;


import com.google.gson.annotations.Expose;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Location {

    @Expose
    private String state;
    @Expose
    private String zip;
    @Expose
    private List<Object> coordinates = new ArrayList<Object>();
    @Expose
    private String country;
    @Expose
    private String city;
    @Expose
    private String street;


    /**
     *
     * @return
     * The state
     */
    public String getState() {
        return state;
    }

    /**
     *
     * @param state
     * The state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     *
     * @return
     * The zip
     */
    public String getZip() {
        return zip;
    }

    /**
     *
     * @param zip
     * The zip
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     *
     * @return
     * The coordinates
     */
    public List<Object> getCoordinates() {
        return coordinates;
    }

    /**
     *
     * @param coordinates
     * The coordinates
     */
    public void setCoordinates(List<Object> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     *
     * @return
     * The country
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     * The country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     *
     * @return
     * The city
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @param city
     * The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     *
     * @return
     * The street
     */
    public String getStreet() {
        return street;
    }

    /**
     *
     * @param street
     * The street
     */
    public void setStreet(String street) {
        this.street = street;
    }
}
