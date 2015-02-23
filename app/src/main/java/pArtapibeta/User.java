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
public class User implements Observer, OnRequestReady {
JsonObjectRequest request = null;
private static UserProfile profile;
boolean available = false;
private OnRequestReady reqReady = null;

    public User(OnRequestReady listener){
        this.reqReady = listener;
        profile = new UserProfile();
        profile.addObserver(this);
        available= false;


    }
    public User(OnRequestReady listener, String id){
        this.reqReady = listener;
        profile = new UserProfile(id);
        profile.addObserver(this);
        available= false;


    }




    @Override
    public void update(Observable observable, Object data) {

            profile = (UserProfile)observable;
            available = true;
            reqReady.onRequestReady(5);
           Log.d("Updating", "Observer id Value: "+ String.valueOf(profile.id));
           Log.d("HasChanged", "Observer:   "+ profile.hasChanged());

    }


    public static  UserProfile getProfile(){

        return profile;
    }

    public static String tooString(){
        return   profile.id+", " +
                ""+profile.name+", " +
                ""+ profile.username +
                ", "+ profile.photo.toString() +
               // ", "+ profile.cover.toString()+
                ", "+profile.followingCount+
                ", "+ profile.followersCownt+
                ", "+ profile.likesCount+
                ", "+ profile.photosCount+
                ", ";
    }


    @Override
    public void onRequestReady(int requmber) {

    }


    private class UserProfile extends Observable {
        private  HashMap<String, Object>[] userProfileRessult = new HashMap[1];
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
         String url = PicsArtConst.MY_PROFILE_URL+PicsArtConst.TOKEN_URL_PREFIX+MainActivity.getAccessToken();

        /**
         * @param id id of User
         *
         **/
        public UserProfile (String id ){

        url = PicsArtConst.USE_PROFILE_URL+id+PicsArtConst.TOKEN_URL_PREFIX+MainActivity.getAccessToken();;

        doRequest();

        }



       /* @Override
        public  String toString(){
            return id+", "+name+", "+ username + ", "+ photo.toString() + ", "+ cover.toString()+", "+followingCount+", "+ followersCownt+ ", "+ likesCount+ ", "+ photosCount+", ";
        }*/

        /**
         * @ my profile
         *
         **/
        public UserProfile ( ){

           doRequest();


        }



        public void doRequest(){
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // display response
                            Log.d("Response", response.toString());

                            try {
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
                            }


                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error.Response", error.toString());
                        }
                    }


            );
            SingletoneRequestQue que = SingletoneRequestQue.getInstance(MainActivity.getAppContext());
            que.addToRequestQueue(getRequest);

        }


    }

}

