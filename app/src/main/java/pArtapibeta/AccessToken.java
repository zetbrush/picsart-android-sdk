package pArtapibeta;

import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arman on 3/9/15.
 */
public class AccessToken {

    static String Code;
    static SharedPreferences pref;

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        AccessToken.accessToken = accessToken;
    }

    private static String accessToken;
    private static RequestListener listener = null;

    private AccessToken() {
    }


    public static void setListener(RequestListener listener) {
        AccessToken.listener = listener;
    }

    public static void requestAccessToken(String address, final String token, final String client_id, final String client_secret, final String redirect_uri, final String grant_type) {

        String userCredentials = client_id + ":" + client_secret;
        final String base64EncodedCredentials = Base64.encodeToString(userCredentials.getBytes(), Base64.NO_WRAP);

        StringRequest req = new StringRequest(PARequest.Method.POST, address, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {

                JSONObject jsOOb = null;
                try {
                    jsOOb = new JSONObject(response);
                    String tok = null;
                    tok = jsOOb.getString("access_token");
                    AccessToken.accessToken = tok;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("accessTokenResp: ", response);
                AccessToken.listener.onRequestReady(7777, "");

            }
        }
                , null) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("code", token);
                params.put("client_id", client_id);
                params.put("client_secret", client_secret);
                params.put("redirect_uri", redirect_uri);
                params.put("grant_type", grant_type);

                return params;

            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Basic " + base64EncodedCredentials);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;

            }
        };

        SingletoneRequestQue.getInstance(MainActivity.getAppContext()).addToRequestQueue(req);

    }


}
