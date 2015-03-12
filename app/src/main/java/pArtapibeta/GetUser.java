package pArtapibeta;

import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;


/**
 * Created by Arman on 2/13/15.
 */
public class GetUser extends AsyncTask<String,String, JSONObject> {
    public static String namee;
    private RequestListener listener;
    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";
    DefaultHttpClient httpClient;

    public GetUser(RequestListener listener){
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected JSONObject doInBackground(String... args) {
        GetUser jParser = new GetUser(listener);
       // JSONObject json = jParser.getUserObject("http://stage.i.picsart.com/api/users/show/me.json?token=", token, CLIENT_ID);
        JSONObject json = jParser.getUserObject(args[0], args[1], args[2]);
        return json;

    }

    @Override
    protected void onPostExecute(JSONObject json) {
        if (json != null) {
            try {

                String name = json.getString("name");
                namee=name;
                String username = json.getString("username");
                String photo = json.getString("photo");

                contentPOJO userInfo =  new contentPOJO(name,username,photo);
                //Toast.makeText(getApplicationContext(), " " + name + "\n" + username + "\n" + photo, Toast.LENGTH_LONG).show();
                Log.i( "pArtApi: ",  "\nname "+ userInfo.name + "\nusername " + userInfo.username + "\nphoto " + userInfo.photoLink);
            } catch (JSONException e) {

            }
            listener.onRequestReady(1,"gag");


        }
    }


    public JSONObject getUserObject(String address,String token,String client_id) {
        // Making HTTP request
        try {
            httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(URI.create(address + token));
            HttpResponse httpResponse = httpClient.execute(request);
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
        }
        // Return JSON String
        return jObj;
    }


    private class contentPOJO{
        String name;
        String username;
        String photoLink;

        public contentPOJO(String nname, String nusername, String photo){
            this.name = nname;
            this.username = nusername;
            this.photoLink = photo;
        }

    }

}
