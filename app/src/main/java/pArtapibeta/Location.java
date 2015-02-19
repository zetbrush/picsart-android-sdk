package pArtapibeta;

/**
 * Created by Arman on 2/19/15.
 */


public class Location {
    String street;
    String city;
    String state;
    String zip;
    String country;
    String coordinates;

    public Location(String str, String cit, String stat, String zip, String country, String coord){
        this.street=str;
        this.city = cit;
        this.state = stat;
        this.zip = zip;
        this.country = country;
        this.coordinates = coord;
    }

    @Override
    public String toString(){
        return street + " " + city + " "+ state + " "+ zip + " "+ country+ " " + coordinates;

    }


}
