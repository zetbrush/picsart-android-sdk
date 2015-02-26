package pArtapibeta;

import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

import test.api.picsart.com.picsart_api_test.PicsArtConst;

/**
 * Created by Arman on 2/23/15.
 */
public class RequestGetPhoto extends AsyncTask<String, String, String> {

    private RequestListener listener;
    private static HashMap<String, Object> photoInfo = new HashMap<>();

    public static Photo getPhoto() {
        return photo;
    }

    private static Photo photo = null;
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    DefaultHttpClient httpClient;

    public static HashMap<String, Object> getPhotoHashM() throws IOException {
        photoInfo = new ObjectMapper().readValue(jObj.toString(), HashMap.class);
        return photoInfo;

    }


    public RequestGetPhoto(RequestListener listener) {
        this.listener = listener;

    }

    @Override
    protected String doInBackground(String... params) {

        getPhoto(params[0], params[1], params[2]);

        return null;


    }


    JsonObjectRequest getRequest;

    public void getPhoto(final String address, String id, String token) {

        String url = address + id + token;
        getRequest = new JsonObjectRequest(Request.Method.GET, url, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("Response", response.toString());
                         jObj = response;
                         photo = new Photo();
                         photo.parseFrom(response);
                            Log.d("initing photo "," ");
                            listener.onRequestReady(9);
                            //   Location location = (Location)response.get(PicsArtConst.paramsPhotoInfo[21]);

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }


        );

        SingletoneRequestQue quu = SingletoneRequestQue.getInstance(MainActivity.getAppContext());
        quu.addToRequestQueue(getRequest);


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }


}