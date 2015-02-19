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
public class User implements Observer {
JsonObjectRequest request = null;
private UserProfile profile;
boolean available = false;


    public User(){

         profile = new UserProfile();
        profile.addObserver(this);
        available= false;


    }


    @Override
    public void update(Observable observable, Object data) {

            profile = (UserProfile)observable;
            available = true;
           Log.d("Updating", "Observer id Value: "+ String.valueOf(profile.id));
           Log.d("HasChanged", "Observer:   "+ profile.hasChanged());

    }


    public UserProfile getProfile(){

        return this.profile;
    }

    public void testPrint(){


        Log.d("MyOBSERVER LOG", "id:__  "+String.valueOf(profile.id));
       // Log.d("HasChanged", "Observer:   "+ profile.hasChanged());


    }










    private class UserProfile extends Observable {
        private  HashMap<String, Object>[] userProfileRessult = new HashMap[1];
        private String id;
        private String name;
        private String username;
        private URL photo;
        private URL cover;
        private Tag[] tags;
        private int followingCount;
        private int followersCownt;
        private int likesCount;
        private int photosCount;
        private Location location;
        final String url = PicsArtConst.USER_PROFILE_URL+PicsArtConst.TOKEN_URL_PREFIX+MainActivity.getAccessToken();

        /**
         * @param id id of User
         *
         **/
        public UserProfile (int id ){



        }

        /**
         * @ my profile
         *
         **/
        public UserProfile ( ){
            JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,

                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // display response
                            Log.d("Response", response.toString());

                            try {
                                userProfileRessult[0] = new ObjectMapper().readValue(response.toString(), HashMap.class);

                                id= String.valueOf(userProfileRessult[0].get("id"));//PicsArtConst.paramsUserProfile[2]);
                                name= (String)userProfileRessult[0].get(PicsArtConst.paramsUserProfile[1]);
                                username= (String)userProfileRessult[0].get(PicsArtConst.paramsUserProfile[0]);
                               // photo = (URL)userProfileRessult[0].get(PicsArtConst.paramsUserProfile[7]);
                              //  cover = (URL)userProfileRessult[0].get(PicsArtConst.paramsUserProfile[19]);
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
                RequestQueue que = Volley.newRequestQueue(MainActivity.getAppContext());
             que.add(getRequest);


        }


    }

}

