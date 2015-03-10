
package pArtapibeta.pojo;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import org.apache.commons.lang.builder.ToStringBuilder;

public class PojoLocation {

    @Expose
    private String state;
    @Expose
    private String zip;
    @Expose
    private String place;
    @Expose
    private List<Integer> coordinates = new ArrayList<Integer>();
    @Expose
    private String street;
    @Expose
    private String city;
    @Expose
    private String country;

    /**
     *
     * @return
     *     The state
     */
    public String getState() {
        return state;
    }

    /**
     *
     * @param state
     *     The state
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     *
     * @return
     *     The zip
     */
    public String getZip() {
        return zip;
    }

    /**
     *
     * @param zip
     *     The zip
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     *
     * @return
     *     The place
     */
    public String getPlace() {
        return place;
    }

    /**
     *
     * @param place
     *     The place
     */
    public void setPlace(String place) {
        this.place = place;
    }

    /**
     *
     * @return
     *     The coordinates
     */
    public List<Integer> getCoordinates() {
        return coordinates;
    }

    /**
     *
     * @param coordinates
     *     The coordinates
     */
    public void setCoordinates(List<Integer> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     *
     * @return
     *     The street
     */
    public String getStreet() {
        return street;
    }

    /**
     *
     * @param street
     *     The street
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     *
     * @return
     *     The city
     */
    public String getCity() {
        return city;
    }

    /**
     *
     * @param city
     *     The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     *
     * @return
     *     The country
     */
    public String getCountry() {
        return country;
    }

    /**
     *
     * @param country
     *     The country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
