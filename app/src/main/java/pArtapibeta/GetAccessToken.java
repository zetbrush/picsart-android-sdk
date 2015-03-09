package pArtapibeta;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import test.api.picsart.com.picsart_api_test.PicsArtConst;

public class GetAccessToken {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    private Context context;

    public GetAccessToken(Context ctx) {
        context = ctx;
    }

    /*List<NameValuePair> params = new ArrayList<NameValuePair>();
    Map<String, String> mapn;
    DefaultHttpClient httpClient;
    HttpPost httpPost;*/



    public JSONObject gettoken(String address, final String token, final String client_id, final String client_secret, final String redirect_uri, final String grant_type) {


        String url = address;

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("code", token);
            jsonObject.put("client_id", client_id);
            jsonObject.put("client_secret", client_secret);
            jsonObject.put("redirect_uri", redirect_uri);
            jsonObject.put("grant_type", grant_type);

            String userCredentials = client_id+":"+client_secret;
            String base64EncodedCredentials = Base64.encodeToString(userCredentials.getBytes(), Base64.NO_WRAP);

            jsonObject.put("Content-Type", "application/x-www-form-urlencoded");
            jsonObject.put("Authorization", "Basic " + base64EncodedCredentials);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        PARequest req = new PARequest(Request.Method.POST, url, jsonObject, null) {

           /* @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("code", token);
                params.put("client_id", client_id);
                params.put("client_secret", client_secret);
                params.put("redirect_uri", redirect_uri);
                params.put("grant_type", grant_type);
                return params;
            }
            String userCredentials = client_id+":"+client_secret;
            String base64EncodedCredentials = Base64.encodeToString(userCredentials.getBytes(), Base64.NO_WRAP);

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                params.put("Authorization", "Basic " + base64EncodedCredentials);

                return super.getHeaders();
            }*/
        };

        SingletoneRequestQue.getInstance(context).addToRequestQueue(req);
        req.setRequestListener(new PARequest.PARequestListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

            @Override
            public void onResponse(Object response) {

                Log.d("gagagagaga", response.toString());

            }
        });
        // Making HTTP request
        /*try {
            // DefaultHttpClient
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(address);

            params.add(new BasicNameValuePair("code", token));
            params.add(new BasicNameValuePair("client_id", client_id));
            params.add(new BasicNameValuePair("client_secret", client_secret));
            params.add(new BasicNameValuePair("redirect_uri", redirect_uri));
            params.add(new BasicNameValuePair("grant_type", grant_type));
            String userCredentials = client_id+":"+client_secret;
            String base64EncodedCredentials = Base64.encodeToString(userCredentials.getBytes(), Base64.NO_WRAP);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.addHeader("Authorization", "Basic " + base64EncodedCredentials);
            httpPost.setEntity(new UrlEncodedFormEntity(params));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
           
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            
            json = sb.toString();
            Log.e("JSONStr", json);
        } catch (Exception e) {
        	e.getMessage();
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        // Parse the String to a JSON Object
        try {
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }*/
        // Return JSON String
        return jObj;
    }
}