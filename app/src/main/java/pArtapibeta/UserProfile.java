package pArtapibeta;


import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UserProfile {

    private static final String MY_LOGS = "My_Logs";
    private static final String SHOW_USER_URL = "http://stage.i.picsart.com/api/users/show/me.json?token=";
    private static final String ACCESS_TOKEN = "tMQdLWML0kspa8qQDvWyzm235Id2Cv9ypicsart11VNuSx6BNk4cMX7VH";

    private long userID;
    private String name;
    private String userName;
    private String userEmail;
    private String userPhoto;
    private int likesCount;
    private int photosCount;
    private int followingCount;
    private int followerCount;

    private Context context;

    public UserProfile(Context cntxt) {

        context = cntxt;

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = SHOW_USER_URL + ACCESS_TOKEN;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Log.i(MY_LOGS, "status is: " + response.toString());
                try {

                    userID = Long.parseLong(response.getString("id"));
                    name = response.getString("name");
                    userName = response.getString("username");
                    userEmail = response.getString("email");
                    photosCount = Integer.parseInt(response.getString("photos_count"));
                    likesCount = Integer.parseInt(response.getString("likes_count"));
                    followerCount = response.getInt("followers_count");
                    followingCount = response.getInt("following_count");
                    userPhoto = response.getString("photo");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(MY_LOGS, "ERROR:  can not get responce");
            }
        });
        queue.add(jsonObjectRequest);
    }

    public long getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public int getPhotosCount() {
        return photosCount;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public int getFollowingCount() {
        return followingCount;
    }
}
