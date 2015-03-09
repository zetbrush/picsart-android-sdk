package pArtapibeta;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

//import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

import java.util.HashMap;

import test.api.picsart.com.picsart_api_test.PicsArtConst;


public class TempUserProfile {
    static HashMap<String, Object> userProfileRessult = null;
    static StringBuilder str = new StringBuilder();
    final JSONObject[] jobj = new JSONObject[1];

    final String url = PicsArtConst.USE_PROFILE_URL + PicsArtConst.TOKEN_URL_PREFIX + MainActivity.getAccessToken();

    // RequestQueue qeue = Volley.newRequestQueue();
    // prepare the Request

    public JsonObjectRequest makeRequest() {
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        Log.d("Response", response.toString());

                        try {
                            //userProfileRessult = new ObjectMapper().readValue(response.toString(), HashMap.class);
                            for (int i = 0; i < TempUserProfile.userProfileRessult.size() && i < 13; i++) {
                                str.append("\n" + PicsArtConst.paramsUserProfile[i] + "   " + TempUserProfile.userProfileRessult.get(PicsArtConst.paramsUserProfile[i]));
                            }
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

        return getRequest;
    }


    public static HashMap<String, Object> getUserProfileRessult() {

        return userProfileRessult;
    }

    public static StringBuilder getAsString() {

        return TempUserProfile.str;
    }
}
