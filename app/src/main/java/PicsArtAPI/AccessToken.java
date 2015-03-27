package picsArtAPI;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * This class consists exclusively of static methods, that operate on
 * requesting OAuth 2.0 token.
 *
 * Static listener is used to notify client.
 *
 * <p>This class is a member of the
 * <a href="www.picsart.com">
 * </a>.
 *
 * @author  Arman Andreasyan 3/9/15
 */

public class AccessToken {

    private static String accessToken;
    private static RequestListener listener = null;

    public static String getAccessToken() {
        return accessToken;
    }
    public static void setAccessToken(String accessToken) {
        AccessToken.accessToken = accessToken;
    }

    public static void setListener(RequestListener listener) {
        AccessToken.listener = listener;
    }


    private AccessToken() {
    }

    public static void requestAccessToken(String address, final String token, final String client_id, final String client_secret, final String redirect_uri, final String grant_type, Context ctx) {

        String userCredentials = client_id + ":" + client_secret;
        final String base64EncodedCredentials = Base64.encodeToString(userCredentials.getBytes(), Base64.NO_WRAP);

        StringRequest req = new StringRequest(PARequest.Method.POST, address, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {

                JSONObject jsOOb;
                try {
                    jsOOb = new JSONObject(response);
                    String tok;
                    tok = jsOOb.getString("access_token");
                    AccessToken.accessToken = tok;
                    AccessToken.listener.onRequestReady(7777, tok);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("accessTokenResp: ", response);


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

        SingletoneRequestQue.getInstance(ctx).addToRequestQueue(req);

    }


}
