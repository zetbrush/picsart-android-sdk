package pArtapibeta;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import test.api.picsart.com.picsart_api_test.PicsArtConst;

/**
 * Created by Arman on 2/19/15.
 */
public class User {
    private String id;
    private String name;
    private String username;
    private String photo;
    private String cover;
    private Tag[] tags;
    private int followingCount;
    private int followersCownt;
    private int likesCount;
    private int photosCount;
    private Location location;
    private String[] followers;

   //String url = PicsArtConst.MY_PROFILE_URL+PicsArtConst.TOKEN_URL_PREFIX+MainActivity.getAccessToken();

    public User( ){


    }

    public User(String id){
        this.id = id;

    }


    public void parseFrom(String id, String name, String username, String photo, String cover, Tag[] tags, int followingCount, int followersCownt, int likesCount, int photosCount, Location location, String[] followers) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.photo = photo;
        this.cover = cover;
        this.tags = tags;
        this.followingCount = followingCount;
        this.followersCownt = followersCownt;
        this.likesCount = likesCount;
        this.photosCount = photosCount;
        this.location = location;
        this.followers = followers;
    }
    public void parseFrom(String id, String name, String username, String photo, String cover,  int followingCount, int followersCownt, int likesCount, int photosCount ) {
       parseFrom( id,  name,  username,  photo,  cover, null,  followingCount,  followersCownt,  likesCount,  photosCount, null, null);
    }



// url = PicsArtConst.USE_PROFILE_URL+id+PicsArtConst.TOKEN_URL_PREFIX+MainActivity.getAccessToken();;


                           /* try {
                                userProfileRessult[0] = new ObjectMapper().readValue(response.toString(), HashMap.class);

                                id= String.valueOf(userProfileRessult[0].get(PicsArtConst.paramsUserProfile[2]));
                                name= (String)userProfileRessult[0].get(PicsArtConst.paramsUserProfile[1]);
                                username= (String)userProfileRessult[0].get(PicsArtConst.paramsUserProfile[0]);
                                photo = (String)userProfileRessult[0].get(PicsArtConst.paramsUserProfile[7]);
                                //cover = (String)userProfileRessult[0].get(PicsArtConst.paramsUserProfile[19]);
                                followingCount = (int)userProfileRessult[0].get(PicsArtConst.paramsUserProfile[12]);
                                followersCownt = (int)userProfileRessult[0].get(PicsArtConst.paramsUserProfile[20]);
                                likesCount = (int)userProfileRessult[0].get(PicsArtConst.paramsUserProfile[8]);
                                photosCount = (int)userProfileRessult[0].get(PicsArtConst.paramsUserProfile[6]);
                                //location = (Location)userProfileRessult[0].get(PicsArtConst.paramsUserProfile[9]);
                                UserProfile.this.setChanged();
                                UserProfile.this.notifyObservers();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/


    public String[] getFollowers() {
        return followers;
    }

    public void setFollowers(String[] followers) {
        this.followers = followers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Tag[] getTags() {
        return tags;
    }

    public void setTags(Tag[] tags) {
        this.tags = tags;
    }

    public int getFollowingCount() {
        return followingCount;
    }

    public void setFollowingCount(int followingCount) {
        this.followingCount = followingCount;
    }

    public int getFollowersCownt() {
        return followersCownt;
    }

    public void setFollowersCownt(int followersCownt) {
        this.followersCownt = followersCownt;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getPhotosCount() {
        return photosCount;
    }

    public void setPhotosCount(int photosCount) {
        this.photosCount = photosCount;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }




}

